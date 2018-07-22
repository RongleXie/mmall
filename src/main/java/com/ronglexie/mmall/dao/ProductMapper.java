package com.ronglexie.mmall.dao;

import com.ronglexie.mmall.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectListByProductNameAndProductId(@Param(value = "name") String productName,@Param(value = "id") Integer productId);

    List<Product> selectListByProductNameAndCategoryIds(@Param(value = "name") String productName,@Param(value = "categoryIds") List<Integer> categoryIds);
}