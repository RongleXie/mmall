package com.ronglexie.mmall.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ronglexie
 * @version 2018/7/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListVo {

	private Integer id;

	private Integer categoryId;

	private String name;

	private String subtitle;

	private String mainImage;

	private BigDecimal price;

	private Integer status;

	private String imageHost;

}
