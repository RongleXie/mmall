package com.ronglexie.mmall.common;

/**
 * 高复用服务响应对象Code枚举
 *
 * @author ronglexie
 * @version 2018/4/6
 */
public enum ResponseEnum {

	SUCCESS(0,"SUCCESS"),
	ERROR(1,"ERROR"),
	NEED_LOGIN(10,"NEED_LOGIN"),
	ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

	private final Integer code;
	private final String desc;

	ResponseEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
