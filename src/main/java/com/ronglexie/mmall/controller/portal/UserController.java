package com.ronglexie.mmall.controller.portal;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户controller层
 *
 * @author ronglexie
 * @version 2018/4/6
 */
@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private IUserService iUserService;

	/**
	 * 登录
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @param session
	 * @return java.lang.Object
	 * @author ronglexie
	 * @version 2018/4/6
	 */
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username, String password, HttpSession session){
		ServerResponse<User> userServerResponse = iUserService.login(username, password);
		if(userServerResponse.isSuccess()){
			session.setAttribute(PublicConst.CURRENT_USER,userServerResponse.getData());
		}
		return userServerResponse;
	}

	/**
	 * 登出，清除session中的用户信息
	 *
	 * @param session
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author ronglexie
	 * @version 2018/4/6
	 */
	@RequestMapping(value = "logout.do",method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session){
		session.removeAttribute(PublicConst.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}

	/**
	 * 注册
	 *
	 * @param user
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.User>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	@RequestMapping(value = "register.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> register(User user){
		return iUserService.register(user);
	}

	/**
	 * 参数合法性校验
	 *
	 * @param value
	 * @param type
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	@RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> checkValid(String value, String type){
		return iUserService.checkValid(value, type);
	}
}
