package com.ronglexie.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.ronglexie.mmall.common.PublicConst;
import com.ronglexie.mmall.common.ResponseEnum;
import com.ronglexie.mmall.common.ServerResponse;
import com.ronglexie.mmall.dao.CartMapper;
import com.ronglexie.mmall.dao.ProductMapper;
import com.ronglexie.mmall.domain.Cart;
import com.ronglexie.mmall.domain.Product;
import com.ronglexie.mmall.domain.vo.CartProductVo;
import com.ronglexie.mmall.domain.vo.CartVo;
import com.ronglexie.mmall.service.ICartService;
import com.ronglexie.mmall.util.BigDecimalUtil;
import com.ronglexie.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车Service层接口实现
 *
 * @author ronglexie
 * @version 2018/7/22
 */
@Service("iCartService")
@Slf4j
public class ICartServiceImpl implements ICartService {

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private ProductMapper productMapper;

	public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
		if(productId == null || count ==null || count == 0){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}

		com.ronglexie.mmall.domain.Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
		if (cart == null) {//不存在，新增
			com.ronglexie.mmall.domain.Cart newCart = new com.ronglexie.mmall.domain.Cart();
			newCart.setUserId(userId);
			newCart.setChecked(PublicConst.Cart.CHECKED);
			newCart.setProductId(productId);
			newCart.setQuantity(count);
			cartMapper.insert(newCart);
		}else {//存在，数量相加
			count = count + cart.getQuantity();
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		return getCartList(userId);
	}

	/**
	 * 获取购物车Vo对象
	 *
	 * @param userId
	 * @return com.ronglexie.mmall.domain.vo.CartVo
	 * @author ronglexie
	 * @version 2018/7/22
	 */
	private CartVo getCartVoLimit(Integer userId){
		CartVo cartVo = new CartVo();
		List<com.ronglexie.mmall.domain.Cart> cartList = cartMapper.selectCartByUserId(userId);
		List<CartProductVo> cartProductVoList = Lists.newArrayList();

		//购物车中总价
		BigDecimal cartTotalPrice = new BigDecimal("0");

		if (CollectionUtils.isNotEmpty(cartList)){
			for (com.ronglexie.mmall.domain.Cart cartItem : cartList) {
				CartProductVo cartProductVo = new CartProductVo();
				cartProductVo.setId(cartItem.getId());
				cartProductVo.setUserId(userId);
				cartProductVo.setProductId(cartItem.getProductId());
				Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
				if (product != null) {
					cartProductVo.setProductMainImage(product.getMainImage());
					cartProductVo.setProductName(product.getName());
					cartProductVo.setProductSubtitle(product.getSubtitle());
					cartProductVo.setProductStatus(product.getStatus());
					cartProductVo.setProductPrice(product.getPrice());
					cartProductVo.setProductStock(product.getStock());
					//判断库存
					int buyLimitCount = 0;
					if(product.getStock() >= cartItem.getQuantity()){
						buyLimitCount = cartItem.getQuantity();
						cartProductVo.setLimitQuantity(PublicConst.Cart.LIMIT_NUM_SUCCESS);
					}else {
						buyLimitCount = product.getStock();
						cartProductVo.setLimitQuantity(PublicConst.Cart.LIMIT_NUM_FAIL);
						//更新购物车中对应数量
						Cart carForQuantity = new Cart();
						carForQuantity.setId(cartItem.getId());
						carForQuantity.setQuantity(buyLimitCount);
						cartMapper.updateByPrimaryKeySelective(carForQuantity);
					}
					cartProductVo.setQuantity(buyLimitCount);
					//计算总价
					cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity().doubleValue()));
					cartProductVo.setProductChecked(cartItem.getChecked());
				}
				if (cartItem.getChecked() == PublicConst.Cart.CHECKED){
					cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
				}
				cartProductVoList.add(cartProductVo);
			}
		}
		cartVo.setCartProductVoList(cartProductVoList);
		cartVo.setCartTotalPrice(cartTotalPrice);
		cartVo.setAllChecked(getAllCheckedStatus(userId));
		cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.ronglexie.top/"));
		return cartVo;
	}

	/**
	 * 获取是否全选状态
	 *
	 * @param userId
	 * @return boolean
	 * @author ronglexie
	 * @version 2018/7/22
	 */
	private boolean getAllCheckedStatus(Integer userId){
		if (userId == null) {
			return false;
		}
		return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
	}

	@Override
	public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
		if(productId == null || count ==null || count == 0){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
		if (cart != null) {
			cart.setQuantity(count);
		}
		cartMapper.updateByPrimaryKeySelective(cart);
		return getCartList(userId);
	}

	@Override
	public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
		List<String> productList = Splitter.on(",").splitToList(productIds);
		if(userId == null || CollectionUtils.isEmpty(productList)){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		cartMapper.deleteCartByUserIdAndProductIds(userId,productList);
		return getCartList(userId);
	}

	@Override
	public ServerResponse<CartVo> getCartList(Integer userId) {
		if(userId == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		CartVo cartVo = getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}

	@Override
	public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checked ,Integer productId) {
		if(userId == null){
			return ServerResponse.createByErrorCodeMsg(ResponseEnum.ILLEGAL_ARGUMENT.getCode(),ResponseEnum.ILLEGAL_ARGUMENT.getDesc());
		}
		cartMapper.checkedOrUnCheckedProduct(userId,checked,productId);
		return getCartList(userId);
	}

	@Override
	public ServerResponse<Integer> getCartProductCount(Integer userId) {
		if(userId == null){
			return ServerResponse.createBySuccess(0);
		}
		return ServerResponse.createBySuccess(cartMapper.selectProductCount(userId));
	}
}
