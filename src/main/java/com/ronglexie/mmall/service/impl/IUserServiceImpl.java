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
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户Service层接口实现
 *
 * @author ronglexie
 * @version 2018/4/6
 */
@Service("iUserService")
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

	@Override
	public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
		//校验token是否为空
		if(StringUtils.isBlank(forgetToken)){
			return ServerResponse.createByErrorMsg("参数错误，未传递token参数");
		}
		//校验用户名是否存在
		ServerResponse<String> serverResponse = this.checkValid(username, PublicConst.USERNAME);
		if(!serverResponse.isSuccess()){
			return ServerResponse.createByErrorMsg("用户不存在");
		}
		String token = LocalCache.getKey(LocalCache.TOKEN_PREFIX + username);
		if(StringUtils.isBlank(token)){
			return ServerResponse.createByErrorMsg("token无效或已过期");
		}

		if(StringUtils.equals(forgetToken, token)){
			String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
			int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
			if(resultCount > 0){
				ServerResponse.createBySuccessMsg("修改密码成功");
			}
		}else {
			return ServerResponse.createByErrorMsg("token错误，请重试");
		}
		return ServerResponse.createByErrorMsg("修改密码失败");
	}

	@Override
	public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {
		//防止横向越权，校验密码时需绑定用户一起校验
		int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
		if(resultCount == 0){
			return ServerResponse.createByErrorMsg("原密码错误");
		}
		user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
		int updateCount = userMapper.updateByPrimaryKeySelective(user);
		if(updateCount > 0){
			return ServerResponse.createBySuccessMsg("修改密码成功");
		}
		return ServerResponse.createByErrorMsg("修改密码失败");
	}

	@Override
	public ServerResponse<User> updateUserInfo(User user) {
		//不能修改username
		//email修改时需要做重复校验
		int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
		if(resultCount > 0){
			ServerResponse.createByErrorMsg("email："+user.getEmail()+"已存在，请重新输入");
		}

		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());

		int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);

		if(updateCount > 0){
			return ServerResponse.createBySuccess("修改个人信息成功",updateUser);
		}
		return ServerResponse.createByErrorMsg("修改个人信息失败");
	}

	@Override
	public ServerResponse<User> getUserInfo(Integer userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		if(user == null){
			return ServerResponse.createByErrorMsg("未找到当前用户");
		}
		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("获取个人信息成功", user);
	}

	@Override
	public ServerResponse checkAdminRole(User user){
		if(user != null && user.getRole().intValue() == PublicConst.Role.ROLE_ADMIN){
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
	}

}
