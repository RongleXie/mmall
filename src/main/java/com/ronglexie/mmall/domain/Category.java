package com.ronglexie.mmall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {

    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

}