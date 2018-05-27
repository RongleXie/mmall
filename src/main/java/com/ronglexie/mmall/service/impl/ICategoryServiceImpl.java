package com.ronglexie.mmall.service.impl;

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

import java.util.List;

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


}
