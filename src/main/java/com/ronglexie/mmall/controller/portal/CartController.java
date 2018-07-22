package com.ronglexie.mmall.controller.portal;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.domain.vo.CartVo;
import com.ronglexie.mmall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 购物车前台Controller
 *
 * @author ronglexie
 * @version 2018/7/22
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

	@Autowired
	private ICartService iCartService;

	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<CartVo> list(HttpSession session){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.getCartList(currentUser.getId());
	}

	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.add(currentUser.getId(),productId,count);
	}

	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse<CartVo> update(HttpSession session, Integer productId, Integer count){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.update(currentUser.getId(),productId,count);
	}

	@RequestMapping("delete_product.do")
	@ResponseBody
	public ServerResponse<CartVo> deleteProduct(HttpSession session, String productIds){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.deleteProduct(currentUser.getId(),productIds);
	}

	@RequestMapping("select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpSession session){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.selectOrUnSelect(currentUser.getId(),PublicConst.Cart.CHECKED,null);
	}

	@RequestMapping("un_select_all.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelectAll(HttpSession session){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.selectOrUnSelect(currentUser.getId(),PublicConst.Cart.UN_CHECKED,null);
	}

	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<CartVo> select(HttpSession session,Integer productId){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.selectOrUnSelect(currentUser.getId(),PublicConst.Cart.CHECKED,productId);
	}

	@RequestMapping("un_select.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelect(HttpSession session,Integer productId){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		return iCartService.selectOrUnSelect(currentUser.getId(),PublicConst.Cart.UN_CHECKED,productId);
	}

	@RequestMapping("get_cart_product_count.do")
	@ResponseBody
	public ServerResponse<Integer> getCartProductCount(HttpSession session){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createBySuccess(0);
		}
		return iCartService.getCartProductCount(currentUser.getId());
	}
}
