package com.ronglexie.mmall.service;

import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.domain.Product;
import com.ronglexie.mmall.domain.vo.ProductDetailVo;

/**
 * 商品Service层
 *
 * @author ronglexie
 * @version 2018/7/15
 */
public interface IProductService {

    /**
     * 新增或修改商品
     *
     * @param product
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author ronglexie
     * @version 2018/7/15
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /***
     * 修改商品状态
     *
     * @param productId
     * @param status
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author ronglexie
     * @version 2018/7/21
     */
    ServerResponse setSaleStatus(Integer productId ,Integer status);

    /**
     * 获取商品详情
     *
     * @param productId
     * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.vo.ProductDetailVo>
     * @author wxt.xqr
     * @version 2018/7/21
     */
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    /**
     * 分页查询商品
     *
     * @param pageNum
     * @param pagetSize
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/21
     */
    ServerResponse getProductList(int pageNum, int pagetSize);

    /**
     * 分页搜索商品
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pagetSize
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/21
     */
    ServerResponse searchProduct(String productName,Integer productId,int pageNum, int pagetSize);

    /**
     * 获取商品详情
     *
     * @param productId
     * @return com.ronglexie.mmall.common.ServerResponse<com.ronglexie.mmall.domain.vo.ProductDetailVo>
     * @author wxt.xqr
     * @version 2018/7/21
     */
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 分页搜索商品
     *
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return com.ronglexie.mmall.common.ServerResponse
     * @author wxt.xqr
     * @version 2018/7/22
     */
    ServerResponse getProductListByKeywordAndCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
