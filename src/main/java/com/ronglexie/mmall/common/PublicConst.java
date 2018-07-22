package com.ronglexie.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 公共常量
 *
 * @author ronglexie
 * @version 2018/4/6
 */
public class PublicConst {
	/**
	 * 当前登录用户
	 */
	public static final String CURRENT_USER = "currentUser";

	public interface Role{
		/**
		 * 普通用户
		 */
		int ROLE_CUSTOMER = 0;
		/**
		 * 管理员
		 */
		int ROLE_ADMIN = 1;
	}

	/**
	 * 校验类型：用户名
	 */
	public static final String USERNAME = "Username";
	/**
	 * 校验类型：邮箱
	 */
	public static final String EMAIL = "Email";


    /**
     * 订单状态
     */
	public enum  OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(30,"已发货"),
        ORDER_SUCCESS(40,"订单完成"),
        ORDER_CLOSE(50,"订单关闭");

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

	    private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Alipay回调
     */
    public interface AlipayCallback{
	    String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
	    String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

	    String RESPONSE_SUCCESS = "success";
	    String RESPONSE_FAILED = "failed";
    }

    /**
     * 支付平台
     */
    public enum PayPlatformEunm{
        ALIPAY(1,"支付宝");

        private int code;
        private String value;

        PayPlatformEunm(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

    }

    public enum ProductStatusEunm{
		ON_SALE(1,"在线");
		private int code;
    	private String value;

		ProductStatusEunm(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public int getCode() {
			return code;
		}

		public String getValue() {
			return value;
		}
	}

	public interface ProductListOrderBy{
		Set<String> PRICE_ASC_DSC = Sets.newHashSet("price_ase","price_dsc");
    }


}
