package com.ronglexie.mmall.service.impl;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.common.LocalCache;
import com.ronglexie.mmall.dao.UserMapper;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.IUserService;
import com.ronglexie.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * 用户Service层接口实现
 *
 * @author ronglexie
 * @version 2018/4/6
 */
public class IUserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUsername(username);
		if(resultCount == 0){
			return ServerResponse.createByErrorMsg("用户名不存在");
		}
		//MD5加密
		String md5password = MD5Util.MD5EncodeUtf8(password);

		User user = userMapper.selectLogin(username, md5password);
		if(user == null){
			return ServerResponse.createByErrorMsg("密码错误");
		}
		return ServerResponse.createBySuccess("登录成功",user);
	}

	@Override
	public ServerResponse<String> register(User user) {
		ServerResponse<String> usernameServerResponse = this.checkValid(user.getUsername(), PublicConst.USERNAME);
		if(!usernameServerResponse.isSuccess()){
			return usernameServerResponse;
		}
		ServerResponse<String> emailServerResponse = this.checkValid(user.getEmail(), PublicConst.EMAIL);
		if(!emailServerResponse.isSuccess()){
			return emailServerResponse;
		}
		//设置默认角色
		user.setRole(PublicConst.Role.ROLE_CUSTOMER);
		//MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

		//保存用户
		int resultCount = userMapper.insert(user);
		if (resultCount == 0){
			return ServerResponse.createByErrorMsg("注册失败");
		}
		return ServerResponse.createBySuccessMsg("注册成功");
	}

	@Override
	public ServerResponse<String> checkValid(String value, String type) {
		if(StringUtils.isNotBlank(type)){
			if(type.equals(PublicConst.USERNAME)){
				int resultCount = userMapper.checkUsername(value);
				if(resultCount > 0){
					return ServerResponse.createByErrorMsg("用户名已存在");
				}
			}else if(type.equals(PublicConst.EMAIL)){
				int resultCount = userMapper.checkEmail(value);
				if(resultCount > 0){
					return ServerResponse.createByErrorMsg("用户邮箱已存在");
				}
			}
		}else {
			return ServerResponse.createByErrorMsg("参数错误");
		}
		return ServerResponse.createBySuccessMsg("校验成功");
	}

	@Override
	public ServerResponse selectQuestion(String username) {
		ServerResponse<String> usernameServerResponse = this.checkValid(username, PublicConst.USERNAME);
		if(!usernameServerResponse.isSuccess()){
			return usernameServerResponse;
		}
		String question = userMapper.selectQuestionByusername(username);
		if(StringUtils.isNotBlank(question)){
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMsg("未设置找回密码的问题");
	}

	@Override
	public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
		int resultCount = userMapper.checkAnswer(username, question, answer);
		if(resultCount > 0){
			//找回密码问题答案正确
			String forgetToken = UUID.randomUUID().toString();
			LocalCache.setKey(LocalCache.TOKEN_PREFIX+username,forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMsg("问题答案错误");
	}
}
