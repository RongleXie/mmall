package com.ronglexie.mmall.service;

import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.User;

/**
 * 用户service层
 * 
 * @author ronglexie
 * @version 2018/4/6
 */
public interface IUserService {

	/**
	 * 用户登录
	 *
	 * @param username
	 * @param password 
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.User>
	 * @author ronglexie
	 * @version 2018/4/6
	 */
	ServerResponse<User> login(String username, String password);
	
	/**
	 * 用户注册
	 *
	 * @param user 
	 * @return com.ronglexie.mmall.common.ServerResponse<ava.lang.String>
	 * @author ronglexie
	 * @version 2018/4/6
	 */
	ServerResponse<String> register(User user);

	/**
	 * 参数合法性校验
	 *
	 * @param value 参数值
	 * @param type 参数类型
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	ServerResponse<String> checkValid(String value, String type);
}
