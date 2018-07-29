package com.ronglexie.mmall.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品详情VO
 *
 * @author ronglexie
 * @version 2018/7/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailVo {

	private Integer id;

	private Integer categoryId;

	private String name;

	private String subtitle;

	private String mainImage;

	private String subImages;

	private String detail;

	private BigDecimal price;

	private Integer stock;

	private Integer status;

	private String createTime;

	private String updateTime;

	private String imageHost;

	private Integer parentCategoryId;

}
