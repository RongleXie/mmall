package com.ronglexie.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 高复用服务响应对象
 *
 * @author ronglexie
 * @version 2018/4/6
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//value值为NULL时忽略
public class ServerResponse<T> implements Serializable {

	private int status;
	private String msg;
	private T data;

	private ServerResponse(int status) {
		this.status = status;
	}

	private ServerResponse(int status, T data) {
		this.status = status;
		this.data = data;
	}

	private ServerResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	private ServerResponse(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 是否成功
	 *
	 * @param  
	 * @return boolean
	 * @author ronglexie
	 * @version 2018/4/6
	 */
	@JsonIgnore//不进行Json序列化
	public boolean isSuccess(){
		return this.status == ResponseEnum.SUCCESS.getCode();
	}

	public Integer getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

	public static <T> ServerResponse<T> createBySuccess(){
		return new ServerResponse<>(ResponseEnum.SUCCESS.getCode());
	}

	public static <T> ServerResponse<T> createBySuccessMsg(String msg){
		return new ServerResponse<>(ResponseEnum.SUCCESS.getCode(), msg);
	}

	public static <T> ServerResponse<T> createBySuccess(T data){
		return new ServerResponse<>(ResponseEnum.SUCCESS.getCode(), data);
	}

	public static <T> ServerResponse<T> createBySuccess(String msg, T data){
		return new ServerResponse<>(ResponseEnum.SUCCESS.getCode(), msg, data);
	}

	public static <T> ServerResponse<T> createByError(){
		return new ServerResponse<>(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getDesc());
	}

	public static <T> ServerResponse<T> createByErrorMsg(String errMsg){
		return new ServerResponse<>(ResponseEnum.ERROR.getCode(), errMsg);
	}

	public static <T> ServerResponse<T> createByErrorCodeMsg(int errCode, String errMsg){
		return new ServerResponse<>(errCode, errMsg);
	}

}
