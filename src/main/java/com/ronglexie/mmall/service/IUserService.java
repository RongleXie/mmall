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
	 * @author ronglexie
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
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);

	/**
	 * 忘记密码后重置密码
	 *
	 * @param username 用户名
	 * @param newPassword 新密码
	 * @param forgetToken token
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

	/**
	 * 登录状态下重置密码
	 *
	 * @param oldPassword
	 * @param newPassword
	 * @param user
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);

	/**
	 * 更新用户个人信息
	 *
	 * @param user
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.User>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	ServerResponse<User> updateUserInfo(User user);

	/**
	 * 获取个人信息
	 *
	 * @param id 用户id
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.User>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	ServerResponse<User> getUserInfo(Integer id);

	/**
	 * 校验是否是管理员
	 *
	 * @param user
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/5/26
	 */
	ServerResponse checkAdminRole(User user);
}
