package com.ronglexie.mmall.domain.vo;

import com.ronglexie.mmall.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单Vo
 *
 * @author ronglexie
 * @version 2018/7/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {

	private Long orderNo;
	private BigDecimal payment;
	private Integer paymentType;
	private String paymentTypeDesc;
	private Integer postage;
	private Integer status;
	private String statusDesc;
	private String paymentTime;
	private String sendTime;
	private String endTime;
	private String closeTime;
	private String createTime;

	//订单明细
	private List<OrderItemVo> orderItemVoList;

	private String imageHost;
	private Integer shippingId;
	private String  receiverName;

	private ShippingVo shippingVo;

}
