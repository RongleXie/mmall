package com.ronglexie.mmall.service;

import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.vo.CartVo;

/**
 * 购物车Service
 *
 * @author ronglexie
 * @version 2018/7/22
 */
public interface ICartService {

	/**
	 * 添加购物车
	 *
	 * @param userId
	 * @param productId
	 * @param count
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.vo.CartVo>
	 * @author wxt.xqr
	 * @version 2018/7/22
	 */
	ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

	/**
	 * 更新购物车
	 *
	 * @param userId
	 * @param productId
	 * @param count
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.vo.CartVo>
	 * @author wxt.xqr
	 * @version 2018/7/22
	 */
	ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

	/**
	 * 删除购物车
	 *
	 * @param userId
	 * @param productIds
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.vo.CartVo>
	 * @author wxt.xqr
	 * @version 2018/7/22
	 */
	ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

	/**
	 * 查询购物车
	 *
	 * @param userId
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.vo.CartVo>
	 * @author wxt.xqr
	 * @version 2018/7/22
	 */
	ServerResponse<CartVo> getCartList(Integer userId);

	/**
	 * 全选或全不选
	 *
	 * @param userId
	 * @param checked
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.vo.CartVo>
	 * @author wxt.xqr
	 * @version 2018/7/22
	 */
	ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checked, Integer productId);

	/**
	 * 获取购物车商品总数
	 *
	 * @param userId
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.Integer>
	 * @author wxt.xqr
	 * @version 2018/7/22
	 */
	ServerResponse<Integer> getCartProductCount(Integer userId);
}
