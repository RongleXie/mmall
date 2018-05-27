package com.ronglexie.mmall.controller.backend;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.ICategoryService;
import com.ronglexie.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 种类后台controller层
 *
 * @author ronglexie
 * @version 2018/5/26
 */
@Controller
@RequestMapping("manage/category")
public class CategoryManagerController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private ICategoryService iCategoryService;

	@RequestMapping("add_category.do")
	@ResponseBody
	public ServerResponse addCategory(HttpSession session, String categoryName,@RequestParam(value = "parentId", defaultValue = "0") int parentId){
		User currentUser = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			return iCategoryService.addCategory(categoryName,parentId);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

	@RequestMapping("update_category.do")
	@ResponseBody
	public ServerResponse updateCategory(HttpSession session, Integer categoryId, String categoryName){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			return iCategoryService.updateCategoryName(categoryId,categoryName);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

	@RequestMapping("get_category.do")
	@ResponseBody
	public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			//获取子节点的种类，不递归
			return iCategoryService.getChildrenParallelCategory(categoryId);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}
}
