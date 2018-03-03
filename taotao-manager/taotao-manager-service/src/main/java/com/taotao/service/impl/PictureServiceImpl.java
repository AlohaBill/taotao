package com.taotao.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.service.PictureService;

/**
 * 图片上传服务
 * 
 * @author Administrator
 *
 */
@Service
public class PictureServiceImpl implements PictureService {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map uploadPicturet(MultipartFile uploadFile) {
		Map resultMap = new HashMap();
		// 生成一个新的文件名,首先要将文件名取出来
		String oldName = uploadFile.getOriginalFilename();
		String newName = UUID.randomUUID().toString().replaceAll("-", "");
		newName = newName + oldName.substring(oldName.lastIndexOf("."));
		// 图片上传
		// 目录如果不存在就新建文件夹
		String pathname = "D:" + File.separator + "apache-tomcat-8.0.48" + File.separator + "webapps" + File.separator
				+ "image"+File.separator+"taotao";
		File file = new File(pathname);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		// 安全起见,判断是否为空
		if (uploadFile != null) {
			String path = pathname + File.separator + newName;
			if (!StringUtils.isEmpty(uploadFile.getOriginalFilename())) {
				try {
					uploadFile.transferTo(new File(path));
					resultMap.put("error", 0);
					String url = "image" + File.separator +"taotao"+File.separator+ newName;
					resultMap.put("url", url);
					return resultMap;
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
					resultMap.put("error", 1);
					resultMap.put("message", "文件上传失败");
					return resultMap;
				}
			}
		}

		return resultMap;
	}

}
