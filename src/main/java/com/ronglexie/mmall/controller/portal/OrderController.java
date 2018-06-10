package com.ronglexie.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * 订单前台Controller
 *
 * @author ronglexie
 * @version 2018/6/10
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping(value = "pay.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse pay(HttpSession session, @RequestParam Long orderNo, HttpServletRequest request){
        User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(currentUser.getId(),orderNo,path);
    }

    @RequestMapping(value = "alipay_callback.do", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String, String> params = Maps.newHashMap();
        Map parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.keySet().iterator();iterator.hasNext();) {
            String name = (String) iterator.next();
            String[] values = (String[]) parameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                 valueStr = (i == values.length - 1 )? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status:{},参数：{}",params.get("sign"),params.get("trade_status"),params.toString());
        //验证支付宝回调正确性，避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMsg("非法请求，验证不通过，再恶意请求我就找网警报警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }

        /**
         * TODO 验证各种数据是否正确
         * 商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
         * 同时需要校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
         * 上述有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
         * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功*/

        ServerResponse serverResponse = iOrderService.alipayCallback(params);
        if(serverResponse.isSuccess()){
            return PublicConst.AlipayCallback.RESPONSE_SUCCESS;
        }
        return PublicConst.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping(value = "query_order_pay_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> quersyOrderPayStatus(HttpSession session, @RequestParam Long orderNo){
        User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(currentUser.getId(), orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }
}
