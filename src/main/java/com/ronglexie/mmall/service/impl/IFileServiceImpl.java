package com.ronglexie.mmall.service.impl;

import com.google.common.collect.Lists;
import com.ronglexie.mmall.service.IFileService;
import com.ronglexie.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件Service层接口实现
 *
 * @author ronglexie
 * @version 2018/7/21
 */
@Service("iFileService")
public class IFileServiceImpl implements IFileService {

	private Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);

	public String upload(MultipartFile file,String path){
		String fileName = file.getOriginalFilename();
		//获取扩展名
		String fileExtensionName = fileName.substring(fileName.lastIndexOf(",") + 1);
		String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
		logger.info("开始上传文件，上传文件名：{}，上传路径：{}，新文件名：{}",fileName,path,uploadFileName);

		File fileDir = new File(path);

		if(!fileDir.exists()){
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}

		File targetFile = new File(path,uploadFileName);

		try {
			//保存到本地
			file.transferTo(targetFile);
			//上传至FTP服务器
			FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			//删除本地的文件
			targetFile.delete();
		} catch (IOException e) {
			logger.error("上传文件异常",e);
			return null;
		}
		return targetFile.getName();
	}
}
