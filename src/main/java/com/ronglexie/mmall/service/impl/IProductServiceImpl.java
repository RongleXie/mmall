package com.ronglexie.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.dao.CategoryMapper;
import com.ronglexie.mmall.dao.ProductMapper;
import com.ronglexie.mmall.domain.Category;
import com.ronglexie.mmall.domain.Product;
import com.ronglexie.mmall.domain.vo.ProductDetailVo;
import com.ronglexie.mmall.domain.vo.ProductListVo;
import com.ronglexie.mmall.service.ICategoryService;
import com.ronglexie.mmall.service.IProductService;
import com.ronglexie.mmall.util.DateTimeUtil;
import com.ronglexie.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品Service层接口实现
 *
 * @author ronglexie
 * @version 2018/7/15
 */
@Service("iProductService")
@Slf4j
public class IProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if(product.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("更新商品成功");
                }
                return ServerResponse.createBySuccess("更新商品失败");
            }else {
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("新增商品成功");
                }
                return ServerResponse.createBySuccess("新增商品失败");
            }
        }
        return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId ,Integer status) {
        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("修改商品状态成功");
        }
        return ServerResponse.createBySuccess("修改商品状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if (productId == null){
            return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("商品已下架或已删除");
        }
		ProductDetailVo productDetailVo = assembleProductDetailVO(product);
		return ServerResponse.createBySuccess(productDetailVo);
    }


    /**
	 * 将PO转换为VO
	 *
     * @param product
     * @return com.ronglexie.mmall.domain.vo.ProductDetailVo
     * @author ronglexie
     * @version 2018/7/21
     */
    private ProductDetailVo assembleProductDetailVO(Product product){
		ProductDetailVo productDetailVo = new ProductDetailVo();
    	BeanUtils.copyProperties(product,productDetailVo);
    	productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.ronglexie.top/"));
		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if(category == null){
			productDetailVo.setParentCategoryId(0);
		}else {
			productDetailVo.setParentCategoryId(category.getParentId());
		}
		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(category.getUpdateTime()));
		return productDetailVo;
	}

	public ServerResponse getProductList(int pageNum, int pagetSize){
		PageHelper.startPage(pageNum,pagetSize);

		List<Product> productList = productMapper.selectList();

		List<ProductListVo> productListVoList = new ArrayList<>();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVO(product);
			productListVoList.add(productListVo);
		}

		PageInfo pageInfo = new PageInfo(productListVoList);

		return ServerResponse.createBySuccess(pageInfo);
    }

	/**
	 * 将PO转换为VO
	 *
	 * @param product
	 * @return com.ronglexie.mmall.domain.vo.ProductDetailVo
	 * @author ronglexie
	 * @version 2018/7/21
	 */
    private ProductListVo assembleProductListVO(Product product){
		ProductListVo productListVo = new ProductListVo();
		BeanUtils.copyProperties(product,productListVo);
		productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.ronglexie.top/"));
		return productListVo;
	}


	public ServerResponse searchProduct(String productName,Integer productId,int pageNum, int pagetSize){
		PageHelper.startPage(pageNum,pagetSize);
		if (StringUtils.isNotBlank(productName)){
			productName = new StringBuilder().append("%").append(productName).append("%").toString();
		}
		List<Product> productList = productMapper.selectListByProductNameAndProductId(productName, productId);
		PageInfo pageInfo = new PageInfo(productList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
		if (productId == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.createByErrorMsg("商品已下架或已删除");
		}
		if (product.getStatus() != PublicConst.ProductStatusEunm.ON_SALE.getCode()) {
			return ServerResponse.createByErrorMsg("商品已下架或已删除");
		}
		ProductDetailVo productDetailVo = assembleProductDetailVO(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}

	public ServerResponse getProductListByKeywordAndCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
    	if(StringUtils.isBlank(keyword) && categoryId == null){
    		return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}

		List<Integer> categoryIdList = Lists.newArrayList();

		if(categoryId != null){
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if (category == null && StringUtils.isBlank(keyword)) {
				PageHelper.startPage(pageNum,pageSize);
				PageInfo pageInfo = new PageInfo();
				pageInfo.setList(Lists.newArrayList());
				return ServerResponse.createBySuccess(pageInfo);
			}
			categoryIdList = iCategoryService.getChildrenDeepCategoryId(categoryId).getData();
		}

		if(StringUtils.isNotBlank(keyword)){
			keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
		}

		PageHelper.startPage(pageNum,pageSize);

		if(StringUtils.isNotBlank(orderBy)){
			if(PublicConst.ProductListOrderBy.PRICE_ASC_DSC.contains(orderBy)){
				String[] orderBys = orderBy.split("_");
				PageHelper.orderBy(orderBys[0] +" "+ orderBys[1]);
			}
		}

		List<Product> productList = productMapper.selectListByProductNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword, categoryIdList.size()==0?null:categoryIdList);
		ArrayList<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVO(product);
			productListVoList.add(productListVo);
		}

		PageInfo pageInfo = new PageInfo(productListVoList);

		return ServerResponse.createBySuccess(pageInfo);
	}

}
