package com.ronglexie.mmall.service;

import com.ronglexie.mmall.common.ServerResponse;

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
}
