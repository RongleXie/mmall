package com.ronglexie.mmall.controller.backend;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.IOrderService;
import com.ronglexie.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 订单后台controller层
 *
 * @author ronglexie
 * @version 2018/7/25
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManagerController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IOrderService iOrderService;

	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse list(HttpSession session,
							   @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
							   @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
		User currentUser = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			return iOrderService.getManageList(pageNum,pageSize);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

	@RequestMapping("detail.do")
	@ResponseBody
	public ServerResponse detail(HttpSession session,Long orderNo){
		User currentUser = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			return iOrderService.getManageDetail(orderNo);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse search(HttpSession session,
							   Long orderNo,
							   @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
							   @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
		User currentUser = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			return iOrderService.manageSearch(orderNo,pageNum,pageSize);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

	@RequestMapping("send_goods.do")
	@ResponseBody
	public ServerResponse sendGoods(HttpSession session,Long orderNo){
		User currentUser = (User)session.getAttribute(PublicConst.CURRENT_USER);
		if(currentUser == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			return iOrderService.sendGoods(orderNo);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

}
