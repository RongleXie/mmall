package com.ronglexie.mmall.dao;

import com.ronglexie.mmall.domain.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdAndProductId(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteCartByUserIdAndProductIds(@Param(value = "userId") Integer userId, @Param(value = "productIds") List<String> productIds);

    int checkedOrUnCheckedProduct(@Param(value = "userId") Integer userId, @Param(value = "checked") Integer checked, @Param(value = "productId") Integer productId);

    int selectProductCount(Integer userId);
}