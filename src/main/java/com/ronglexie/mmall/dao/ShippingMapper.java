package com.ronglexie.mmall.dao;

import com.ronglexie.mmall.domain.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByUserIdAndShippingId(@Param(value = "userId") Integer userId,@Param(value = "shippingId") Integer shippingId);

    int updateByShipping(Shipping shipping);

    Shipping selectByUserIdAndShippingId(@Param(value = "userId") Integer userId,@Param(value = "shippingId") Integer shippingId);

    List<Shipping> selectByUserId(@Param(value = "userId") Integer userId);
}