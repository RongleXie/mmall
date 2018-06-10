package com.ronglexie.mmall.controller.portal;

import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 订单前台Controller
 *
 * @author ronglexie
 * @version 2018/6/10
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, @RequestParam Long orderNo, HttpServletRequest request){
        User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(currentUser.getId(),orderNo,path);
    }
}
