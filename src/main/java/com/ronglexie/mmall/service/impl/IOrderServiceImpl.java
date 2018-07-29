package com.ronglexie.mmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.dao.*;
import com.ronglexie.mmall.domain.*;
import com.ronglexie.mmall.domain.vo.OrderItemVo;
import com.ronglexie.mmall.domain.vo.OrderProductVo;
import com.ronglexie.mmall.domain.vo.OrderVo;
import com.ronglexie.mmall.domain.vo.ShippingVo;
import com.ronglexie.mmall.service.IOrderService;
import com.ronglexie.mmall.util.BigDecimalUtil;
import com.ronglexie.mmall.util.DateTimeUtil;
import com.ronglexie.mmall.util.FTPUtil;
import com.ronglexie.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 订单Service层接口实现
 *
 * @author ronglexie
 * @version 2018/6/10
 */
@Service("iOrderService")
@Slf4j
public class IOrderServiceImpl implements IOrderService {
    
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    public ServerResponse pay(Integer userId, Long orderNo, String path){
        HashMap<Object, Object> resultMap = Maps.newHashMap();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order == null){
            return ServerResponse.createByErrorMsg("用户没有该订单");
        }
        resultMap.put("orderNo",String.valueOf(order.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuffer().append("mmall扫码支付，订单号：").append(order.getOrderNo()).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuffer().append("订单").append(outTradeNo).append("购买商品共")
                .append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId,orderNo);

        for (OrderItem orderItem : orderItemList) {
            GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goodsDetail);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                String qrPath = String.format(path+File.separator+"qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path,qrFileName);
                try{
                    FTPUtil.uploadFile(Lists.<File>newArrayList(targetFile));
                }catch (IOException e){
                    log.error("二维码上传异常",e);
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFile.getName();
                resultMap.put("quUrl",qrUrl);
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMsg("支付宝预下单失败");
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMsg("系统异常，预下单状态未知");
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMsg("不支持的交易状态，交易返回异常");
        }
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }


    public ServerResponse alipayCallback(Map<String,String> params){
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            log.info("非本商城的订单，回调忽略");
            return ServerResponse.createByErrorMsg("非本商城的订单，回调忽略");
        }
        if(order.getStatus() >= PublicConst.OrderStatusEnum.PAID.getCode()){
            log.info("支付宝重复调用");
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        /*回调成功，将订单状态置为已付款*/
        if(PublicConst.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(PublicConst.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        /*构造支付信息*/
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(PublicConst.PayPlatformEunm.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse queryOrderPayStatus(Integer id, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMsg("用户没有该订单");
        }
        if(order.getStatus() >= PublicConst.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        if (userId == null || shippingId == null){
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        //已选中购物车
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        ServerResponse<List<OrderItem>> serverResponse = getCartOrderItems(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        //计算商品总价
        List<OrderItem> orderItemList = serverResponse.getData();
        if(CollectionUtils.isEmpty(orderItemList)){
            return ServerResponse.createByErrorMsg("购物车为空");
        }
        BigDecimal payment = getOrderTotalPrice(orderItemList);

        //创建订单
        Order order = assembleOrder(userId, shippingId, payment);
        if(order == null){
            return ServerResponse.createByErrorMsg("生成订单错误");
        }

        //保存子订单
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(generateOrderNo());
            orderItemMapper.insert(orderItem);
        }

        //减少库存
        reduceProductStock(orderItemList);

        //清空购物车
        cleanCart(cartList);

        //返回前台数据
        OrderVo orderVo = assembleOrderVo(order,orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    /**
     * 组装OrderVo
     *
     * @param order
     * @param orderItemList
     * @return com.ronglexie.mmall.domain.vo.OrderVo
     * @author wxt.xqr
     * @version 2018/7/25
     */
    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(PublicConst.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(PublicConst.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            ShippingVo shippingVo = new ShippingVo();
            BeanUtils.copyProperties(shipping,shippingVo);
            orderVo.setShippingVo(shippingVo);
        }
        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));

        orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.ronglexie.top/"));

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(orderItem,orderItemVo);
            orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    /**
     * 清空购物车
     *
     * @param cartList
     * @return void
     * @author wxt.xqr
     * @version 2018/7/24
     */
    private void cleanCart(List<Cart> cartList){
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    /**
     * 减少库存
     *
     * @param orderItemList
     * @return void
     * @author wxt.xqr
     * @version 2018/7/24
     */
    private void reduceProductStock(List<OrderItem> orderItemList){
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    /**
     * 组装订单
     *
     * @param userId
     * @param shippingId
     * @param payment
     * @return com.ronglexie.mmall.domain.Order
     * @author wxt.xqr
     * @version 2018/7/24
     */
    public Order assembleOrder(Integer userId,Integer shippingId,BigDecimal payment){
        Order order = new Order();
        long orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setStatus(PublicConst.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(PublicConst.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);

        int rowCount = orderMapper.insert(order);
        if(rowCount > 0){
            return order;
        }
        return null;
    }

    /**
     * 生成订单号
     *
     * @param
     * @return long
     * @author wxt.xqr
     * @version 2018/7/24
     */
    public long generateOrderNo(){
        long currentTime = System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }

    /**
     * 计算总价
     *
     * @param orderItemList
     * @return java.math.BigDecimal
     * @author wxt.xqr
     * @version 2018/7/24
     */
    public BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /**
     * 根据购物车获取子订单明细
     *
     * @param userId
     * @param cartList
     * @return com.ronglexie.mmall.common.ServerResponse<java.util.List<com.ronglexie.mmall.domain.OrderItem>>
     * @author wxt.xqr
     * @version 2018/7/24
     */
    private ServerResponse<List<OrderItem>> getCartOrderItems(Integer userId, List<Cart> cartList){
        List<OrderItem> orderItemList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByErrorMsg("购物车为空");
        }

        //校验购物车的数据，包括商品的状态和数量
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            //校验商品是否为在售状态
            if(PublicConst.ProductStatusEunm.ON_SALE.getCode() != product.getStatus()){
                return ServerResponse.createByErrorMsg("商品"+product.getName()+"已下架");
            }
            //校验商品库存是否充足
            if(cartItem.getQuantity() > product.getStatus()){
                return ServerResponse.createByErrorMsg("商品"+product.getName()+"库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }


    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMsg("该用户此订单不存在");
        }
        if(order.getStatus() != PublicConst.OrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.createByErrorMsg("已付款，无法取消此订单");
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(PublicConst.OrderStatusEnum.CANCELED.getCode());
        int rowCount = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("订单取消成功");
        }
        return ServerResponse.createByErrorMsg("订单取消失败");
    }

    @Override
    public ServerResponse getOrderCartProduct(Integer userId){
        OrderProductVo orderProductVo = new OrderProductVo();
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ServerResponse<List<OrderItem>> serverResponse = getCartOrderItems(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        List<OrderItem> orderItemList = serverResponse.getData();

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(orderItem,orderItemVo);
            orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
            orderItemVoList.add(orderItemVo);
        }
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.ronglexie.top/"));
        return ServerResponse.createBySuccess(orderProductVo);
    }

    @Override
    public ServerResponse getOrderDetail(Integer userId, Long orderNo){
        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createByErrorMsg("该用户此订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId, orderNo);
        OrderVo orderVo = assembleOrderVo(order,orderItemList);
        return ServerResponse.createBySuccess(orderVo);

    }

    @Override
    public ServerResponse getOrderList(Integer userId, int pageNum, int pageSize){
        if (userId == null){
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVo> orderVoList = assembleOrderVoList(orderList, userId);
        PageInfo pageInfo = new PageInfo(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId){
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = Lists.newArrayList();
            if(userId == null){
                //管理员查询，不需要传入userId条件
                orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            }else {
                orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId, order.getOrderNo());
            }
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }


    /**
     * backend
     */

    @Override
    public ServerResponse getManageList(int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAllOrder();
        List<OrderVo> orderVoList = assembleOrderVoList(orderList, null);
        PageInfo pageInfo = new PageInfo(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse getManageDetail(Long orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            return ServerResponse.createByErrorMsg("此订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order,orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    @Override
    public ServerResponse manageSearch(Long orderNo, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        Order order= orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            return ServerResponse.createByErrorMsg("此订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        PageInfo pageInfo = new PageInfo(Lists.newArrayList(orderVo));
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse sendGoods(Long orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            return ServerResponse.createByErrorMsg("此订单不存在");
        }
        if(order.getStatus() == PublicConst.OrderStatusEnum.PAID.getCode()){
            order.setStatus(PublicConst.OrderStatusEnum.SHIPPED.getCode());
            order.setSendTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        return ServerResponse.createByErrorMsg("此订单发货失败");
    }

}
