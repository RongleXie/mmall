package com.ronglexie.mmall.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 购物车商品Vo
 *
 * @author ronglexie
 * @version 2018/7/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductVo {

	private Integer id;
	private Integer userId;
	private Integer productId;
	private Integer quantity;//购物车此商品的数量
	private String productName;
	private String productSubtitle;
	private String productMainImage;
	private BigDecimal productPrice;
	private Integer productStatus;
	private BigDecimal productTotalPrice;
	private Integer productStock;
	private Integer productChecked;//是否选中

	private String limitQuantity;//限制数量，返回结果

}
