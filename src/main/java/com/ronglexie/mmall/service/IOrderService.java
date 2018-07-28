package com.ronglexie.mmall.service;

import com.ronglexie.mmall.common.ServerResponse;

import java.util.Map;

/**
 * 订单Service层
 *
 * @author ronglexie
 * @version 2018/6/10
 */
public interface IOrderService {

    /**
     * 支付
     *
     * @param userId
     * @param orderNo
     * @param path
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/6/10
     */
    ServerResponse pay(Integer userId, Long orderNo, String path);

    /**
     * 支付宝回调
     *
     * @param params
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/6/10
     */
    ServerResponse alipayCallback(Map<String,String> params);

    /**
     * 查询订单状态
     *
     * @param userId
     * @param orderNo
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/6/10
     */
    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    /**
     * 创建订单
     *
     * @param userId
     * @param shippingId
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/24
     */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     *
     * @param userId
     * @param orderNo
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse cancel(Integer userId, Long orderNo);

    /**
     * 获取购物车商品详情
     *
     * @param userId
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 获取订单详情
     *
     * @param userId
     * @param orderNo
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse getOrderDetail(Integer userId, Long orderNo);

    /**
     * 分页查询订单
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse getOrderList(Integer userId, int pageNum, int pageSize);

    /**
     * 后台分页查询订单
     *
     * @param pageNum
     * @param pageSize
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse getManageList(int pageNum, int pageSize);

    /**
     * 后台获取订单详情
     *
     * @param orderNo
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse getManageDetail(Long orderNo);

    /**
     * 后台根据订单号分页搜索订单
     *
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse manageSearch(Long orderNo, int pageNum, int pageSize);

    /**
     * 订单发货
     *
     * @param orderNo
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/25
     */
    ServerResponse sendGoods(Long orderNo);
}
