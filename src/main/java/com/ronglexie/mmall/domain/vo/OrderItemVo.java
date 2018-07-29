package com.ronglexie.mmall.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细Vo
 *
 * @author ronglexie
 * @version 2018/7/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVo {

	private Long orderNo;

	private Integer productId;

	private String productName;

	private String productImage;

	private BigDecimal currentUnitPrice;

	private Integer quantity;

	private BigDecimal totalPrice;

	private String createTime;

}
