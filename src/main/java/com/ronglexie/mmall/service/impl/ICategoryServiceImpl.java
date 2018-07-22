package com.ronglexie.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.dao.CategoryMapper;
import com.ronglexie.mmall.domain.Category;
import com.ronglexie.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 种类Service层接口实现
 *
 * @author ronglexie
 * @version 2018/5/26
 */
@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService{

	Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public ServerResponse addCategory(String categoryName, Integer parentId){
		if(parentId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMsg("添加种类参数错误");
		}
		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);
		category.setCreateTime(new Date());
		category.setUpdateTime(new Date());
		int rowCount = categoryMapper.insert(category);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMsg("添加种类成功");
		}
		return ServerResponse.createByErrorMsg("添加种类失败");
	}

	@Override
	public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
		if(categoryId == null || StringUtils.isBlank(categoryName)){
			return ServerResponse.createByErrorMsg("更新种类参数错误");
		}

		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);
        category.setUpdateTime(new Date());
		int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
		if(rowCount > 0){
			return ServerResponse.createBySuccessMsg("更新种类成功");
		}
		return ServerResponse.createByErrorMsg("添加种类失败");
	}

	@Override
	public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
		if(categoryId == null){
			return ServerResponse.createByErrorMsg("获取种类子节点参数错误");
		}
		List<Category> categoryList = categoryMapper.getChildrenParallelCategoryByParentId(categoryId);
		if (CollectionUtils.isEmpty(categoryList)){
			logger.info("未找到当前种类的子节点种类，categoryId:{}",categoryId);
		}
		return ServerResponse.createBySuccess(categoryList);
	}

	/***
	 * 递归查询当前种类及子各类的Id集合
	 *
	 * @param categoryId
	 * @return com.ronglexie.mmall.common.ServerResponse
	 * @author ronglexie
	 * @version 2018/7/15
	 */
	@Override
	public ServerResponse<List<Integer>> getChildrenDeepCategoryId(Integer categoryId){
        if(categoryId == null){
            return ServerResponse.createByErrorMsg("获取当前种类及子各类的Id集合参数错误");
        }
		Set<Category> categorySet = Sets.newHashSet();
		getChildrenCategory(categorySet, categoryId);

		List<Integer> categoryIdList = Lists.newArrayList();
		if(categoryId != null){
			for (Category categoryItem : categorySet) {
				categoryIdList.add(categoryItem.getId());
			}
		}
		return ServerResponse.createBySuccess(categoryIdList);
	}

	/***
	 * 递归查询
	 *
	 * @param categorySet
 	 * @param categoryId
	 * @return java.util.Set<com.ronglexie.mmall.domain.Category>
	 * @author ronglexie
	 * @version 2018/7/15
	 */
	private Set<Category> getChildrenCategory(Set<Category> categorySet,Integer categoryId){
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			categorySet.add(category);
		}
		List<Category> categoryList = categoryMapper.getChildrenParallelCategoryByParentId(categoryId);
		for (Category categoryItem : categoryList) {
			getChildrenCategory(categorySet,categoryItem.getId());
		}
		return categorySet;
	}


}
