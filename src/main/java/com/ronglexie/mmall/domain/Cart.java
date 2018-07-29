package com.ronglexie.mmall.domain;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;

}