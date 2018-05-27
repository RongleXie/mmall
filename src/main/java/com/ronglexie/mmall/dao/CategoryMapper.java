package com.ronglexie.mmall.dao;

import com.ronglexie.mmall.domain.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    /**
     * 根据parentId查询子节点，不递归
     *
     * @param parentId
     * @return java.util.List<com.ronglexie.mmall.domain.Category>
     * @author ronglexie
     * @version 2018/5/27
     */
    List<Category> getChildrenParallelCategoryByParentId(Integer parentId);
}