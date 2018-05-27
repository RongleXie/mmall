package com.ronglexie.mmall.service;

import com.ronglexie.mmall.common.ServerResponse;

/**
 * 种类Service
 *
 * @author ronglexie
 * @version 2018/5/26
 */
public interface ICategoryService {

	/**
	 * 添加种类
	 *
	 * @param categoryName
	 * @param parentId
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/5/26
	 */
	ServerResponse addCategory(String categoryName, Integer parentId);

	/**
	 * 更新种类
	 *
	 * @param categoryId
	 * @param categoryName
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/5/27
	 */
	ServerResponse updateCategoryName(Integer categoryId, String categoryName);

	/**
	 * 获取子节点的种类，不递归
	 *
	 * @param categoryId
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/5/27
	 */
	ServerResponse getChildrenParallelCategory(Integer categoryId);
}
