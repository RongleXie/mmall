package com.ronglexie.mmall.service;

import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.Shipping;

/**
 * 收货地址Service层
 *
 * @author ronglexie
 * @version 2018/7/22
 */
public interface IShippingService {

	/**
	 * 新增收货地址
	 *
	 * @param userId
	 * @param shipping
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/7/22
	 */
	ServerResponse add(Integer userId, Shipping shipping);

	/**
	 * 删除收货地址
	 *
	 * @param userId
	 * @param shippingId
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/7/22
	 */
	ServerResponse delete(Integer userId, Integer shippingId);

	/**
	 * 更新收货地址
	 *
	 * @param userId
	 * @param shipping
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/7/22
	 */
	ServerResponse update(Integer userId, Shipping shipping);

	/**
	 * 查询收货地址
	 *
	 * @param userId
	 * @param shippingId
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/7/22
	 */
	ServerResponse select(Integer userId, Integer shippingId);

	/**
	 * 分页查询收货地址
	 *
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/7/22
	 */
	ServerResponse getShippingList(Integer userId, Integer pageNum, Integer pageSize);
}
