package com.ronglexie.mmall.controller.portal;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
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
 * 用户前台controller层
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
	@RequestMapping(value = "logout.do",method = RequestMethod.POST)
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
	@RequestMapping(value = "register.do", method = RequestMethod.POST)
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
	@RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String value, String type){
		return iUserService.checkValid(value, type);
	}

	/**
	 * 根据用户名查询用户找回密码问题
	 *
	 * @param username 用户名
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	@RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return iUserService.selectQuestion(username);
	}

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
	@RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
		return iUserService.forgetCheckAnswer(username, question, answer);
	}

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
	@RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken){
		return iUserService.forgetResetPassword(username, newPassword, forgetToken);
	}

	/**
	 * 登录状态下重置密码
	 *
	 * @param session
	 * @return com.ronglexie.mmall.common.ServerResponse<java.lang.String>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	@RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> resetPassword(String oldPassword, String newPassword, HttpSession session){
		User user = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(user == null){
			return ServerResponse.createByErrorMsg("用户未登录");
		}
		return iUserService.resetPassword(oldPassword, newPassword, user);
	}

	/**
	 * 更新用户个人信息
	 *
	 * @param session
	 * @param user
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.User>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	@RequestMapping(value = "update_user_info.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> updateUserInfo(HttpSession session, User user){
		User currentUser = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorMsg("用户未登录");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServerResponse<User> userServerResponse = iUserService.updateUserInfo(user);
		if(userServerResponse.isSuccess()){
			session.setAttribute(PublicConst.CURRENT_USER,userServerResponse.getData());
		}
		return userServerResponse;
	}

	/**
	 * 获取个人用户信息
	 *
	 * @param session
	 * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.User>
	 * @author ronglexie
	 * @version 2018/4/7
	 */
	@RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User currentUser = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(), "未登录，请重新登录");
		}
		return iUserService.getUserInfo(currentUser.getId());
	}


}