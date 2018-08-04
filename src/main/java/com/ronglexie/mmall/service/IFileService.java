package com.ronglexie.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件Service
 *
 * @author ronglexie
 * @version 2018/7/21
 */
public interface IFileService {

	/**
	 * 上传文件
	 *
	 * @param file
	 * @param path
	 * @return java.lang.String
	 * @author ronglexie
	 * @version 2018/7/21
	 */
	String upload(MultipartFile file, String path);
}
