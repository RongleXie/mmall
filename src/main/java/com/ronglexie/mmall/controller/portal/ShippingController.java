package com.ronglexie.mmall.controller.portal;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.Shipping;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 收货地址前台Controller
 *
 * @author ronglexie
 * @version 2018/7/22
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

	@Autowired
	private IShippingService iShippingService;

	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse add(HttpSession session, Shipping shipping){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iShippingService.add(currentUser.getId(),shipping);
	}

	@RequestMapping("del.do")
	@ResponseBody
	public ServerResponse add(HttpSession session, Integer shippingId){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iShippingService.delete(currentUser.getId(),shippingId);
	}

	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse update(HttpSession session, Shipping shipping){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iShippingService.update(currentUser.getId(),shipping);
	}

	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse select(HttpSession session, Integer shippingId){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iShippingService.select(currentUser.getId(),shippingId);
	}

	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse list(HttpSession session,
							   @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
							   @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iShippingService.getShippingList(currentUser.getId(),pageNum,pageSize);
	}
}
