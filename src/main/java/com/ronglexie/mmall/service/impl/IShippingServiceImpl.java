package com.ronglexie.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.dao.ShippingMapper;
import com.ronglexie.mmall.domain.Shipping;
import com.ronglexie.mmall.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ronglexie
 * @version 2018/7/22
 */
@Service("iShippingService")
@Slf4j
public class IShippingServiceImpl implements IShippingService {

	@Autowired
	private ShippingMapper shippingMapper;

	public ServerResponse add(Integer userId,Shipping shipping){
		if (userId == null || shipping == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		shipping.setUserId(userId);
		int rowCount = shippingMapper.insert(shipping);
		if(rowCount > 0){
			Map result = Maps.newHashMap();
			result.put("shippingId",shipping.getId());
			return ServerResponse.createBySuccess("新增收货地址成功",result);
		}
		return ServerResponse.createByErrorMsg("新增收货地址失败");
	}

	@Override
	public ServerResponse delete(Integer userId, Integer shippingId) {
		if (userId == null || shippingId == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		int rowCount = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMsg("删除收货地址成功");
		}
		return ServerResponse.createByErrorMsg("删除收货地址失败");
	}

	@Override
	public ServerResponse update(Integer userId, Shipping shipping) {
		if (userId == null || shipping == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		shipping.setUserId(userId);
		int rowCount = shippingMapper.updateByShipping(shipping);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMsg("修改收货地址成功");
		}
		return ServerResponse.createByErrorMsg("修改收货地址失败");
	}

	@Override
	public ServerResponse select(Integer userId, Integer shippingId) {
		if (userId == null || shippingId == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
		if(shipping != null ){
			return ServerResponse.createBySuccess("查询收货地址成功",shipping);
		}
		return ServerResponse.createByErrorMsg("无法查询到该地址");
	}

	@Override
	public ServerResponse getShippingList(Integer userId, Integer pageNum, Integer pageSize) {
		if (userId == null || pageNum == null || pageSize == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		PageHelper.startPage(pageNum,pageSize);

		List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
		if(CollectionUtils.isNotEmpty(shippingList)){
			PageInfo pageInfo = new PageInfo(shippingList);
			return ServerResponse.createBySuccess("查询收货地址成功",pageInfo);
		}
		return ServerResponse.createByErrorMsg("无法查询到该地址");
	}
}
