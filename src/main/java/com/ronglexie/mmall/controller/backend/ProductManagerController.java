package com.ronglexie.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.Product;
import com.ronglexie.mmall.domain.User;
import com.ronglexie.mmall.service.IFileService;
import com.ronglexie.mmall.service.IProductService;
import com.ronglexie.mmall.service.IUserService;
import com.ronglexie.mmall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品后台管理Controller
 *
 * @author ronglexie
 * @version 2018/7/15
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

	@Autowired
	private IFileService iFileService;


    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse save(HttpSession session, Product product){
        User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否为管理员
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status){
        User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否为管理员
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }
        return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Integer productId){
        User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否为管理员
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return iProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
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
        //校验是否为管理员
        if(iUserService.checkAdminRole(currentUser).isSuccess()){
            return iProductService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
    }


	@RequestMapping("search.do")
	@ResponseBody
	public ServerResponse search(HttpSession session,
							   String productName,
							   Integer productId,
							   @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
							   @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			return iProductService.searchProduct(productName,productId,pageNum,pageSize);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.NEED_LOGIN.getCode(),"用户未登录，请登录");
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.ronglexie.top/").toString() + targetFileName;

			HashMap<Object, Object> fileMap= Maps.newHashMap();
			fileMap.put("uri",targetFileName);
			fileMap.put("url",url);

			return ServerResponse.createBySuccess(fileMap);
		}
		return ServerResponse.createByErrorMsg("无权限操作，需要管理员登录");
	}

	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public Map richtextImgupload(HttpSession session,
								 @RequestParam(value = "upload_file",required = false) MultipartFile file,
								 HttpServletRequest request,
								 HttpServletResponse response){
		/**
		 * 富文本上传图片
		 * simditor
		 */
    	Map resultMap = Maps.newHashMap();
		User currentUser = (User) session.getAttribute(PublicConst.CURRENT_USER);
		if (currentUser == null) {
			resultMap.put("success",false);
			resultMap.put("msg","无权限操作，需要管理员登录");
			return resultMap;
		}
		//校验是否为管理员
		if(iUserService.checkAdminRole(currentUser).isSuccess()){
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.ronglexie.top/").toString() + targetFileName;
			resultMap.put("success",true);
			resultMap.put("msg","上传成功");
			resultMap.put("file_path",url);
			response.addHeader("Access-Control-Allow-Headers","X-File-Name");
			return resultMap;
		}
		resultMap.put("success",false);
		resultMap.put("msg","无权限操作，需要管理员登录");
		return resultMap;
	}


}
