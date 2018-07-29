package com.ronglexie.mmall.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收货地址Vo
 *
 * @author ronglexie
 * @version 2018/7/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingVo {

	private String receiverName;

	private String receiverPhone;

	private String receiverMobile;

	private String receiverProvince;

	private String receiverCity;

	private String receiverDistrict;

	private String receiverAddress;

	private String receiverZip;

}
