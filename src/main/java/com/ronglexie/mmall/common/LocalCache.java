package com.ronglexie.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 *
 * @author ronglexie
 * @version 2018/4/7
 */
@Slf4j
public class LocalCache {

	/**
	 * Token缓存key前缀
	 */
	public static final String TOKEN_PREFIX = "token_";

	//LRU算法 存储到最大值时使用最小使用算法清除
	public static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
			.maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
				@Override
				public String load(String s) throws Exception {
					return "null";
				}
			});

	public static void setKey(String key, String value){
		localCache.put(key, value);
	}

	public static String getKey(String key){
		String value = null;
		try {
			value = localCache.get(key);
			if("null".equals(value)){
				return null;
			}
			return value;
		} catch (ExecutionException e) {
			log.error("localCache get error",e);
		}
		return null;
	}
}
