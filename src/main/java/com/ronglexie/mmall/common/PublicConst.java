package com.ronglexie.mmall.common;

/**
 * 公共常量
 *
 * @author ronglexie
 * @version 2018/4/6
 */
public class PublicConst {
	/**
	 * 当前登录用户
	 */
	public static final String CURRENT_USER = "currentUser";

	public interface Role{
		/**
		 * 普通用户
		 */
		int ROLE_CUSTOMER = 0;
		/**
		 * 管理员
		 */
		int ROLE_ADMIN = 1;
	}

	/**
	 * 校验类型：用户名
	 */
	public static final String USERNAME = "Username";
	/**
	 * 校验类型：邮箱
	 */
	public static final String EMAIL = "Email";

}
