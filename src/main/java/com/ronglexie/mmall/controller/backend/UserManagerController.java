package com.ronglexie.mmall.controller.backend;

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
 * 用户后台controller层
 *
 * @author ronglexie
 * @version 2018/4/7
 */
@Controller
@RequestMapping("/manage/user")
public class UserManagerController {

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
	 * @version 2018/4/7
	 */
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username, String password, HttpSession session){
		ServerResponse<User> userServerResponse = iUserService.login(username, password);
		if(userServerResponse.isSuccess()){
			User user = userServerResponse.getData();
			if(user.getRole() == PublicConst.Role.ROLE_ADMIN){//管理员才可登录后台
				session.setAttribute(PublicConst.CURRENT_USER, user);
				return userServerResponse;
			}else {
				return ServerResponse.createByErrorMsg("非管理员账户不能登录后台");
			}
		}
		return userServerResponse;
	}
}
