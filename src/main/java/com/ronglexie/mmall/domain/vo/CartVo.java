package com.ronglexie.mmall.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车Vo
 *
 * @author ronglexie
 * @version 2018/7/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartVo {

	private List<CartProductVo> cartProductVoList;
	private BigDecimal cartTotalPrice;
	private Boolean allChecked;//是否全部勾选
	private String imageHost;

	public List<CartProductVo> getCartProductVoList() {
		return cartProductVoList;
	}

}
