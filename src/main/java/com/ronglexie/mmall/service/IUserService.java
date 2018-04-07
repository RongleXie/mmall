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

	/**
	 * 根据用户名查询用户找回密码问题
	 *
	 * @param username 用户名
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author wxt.xqr
	 * @version 2018/4/7
	 */
	ServerResponse selectQuestion(String username);
	
	/**
	 * 校验找回密码问题答案是否正确
	 *
	 * @param username 用户名
	 * @param question 问题
	 * @param answer 答案
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author wxt.xqr
	 * @version 2018/4/7
	 */
	ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);
}
