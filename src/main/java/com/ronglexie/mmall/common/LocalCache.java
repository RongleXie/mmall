package com.ronglexie.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * token缓存
 *
 * @author ronglexie
 * @version 2018/4/7
 */
public class LocalCache {
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(LocalCache.class);

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
			logger.error("localChche get error",e);
		}
		return null;
	}
}
