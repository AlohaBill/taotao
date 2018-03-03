package com.taotao.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 图片上传处理
 * @author Administrator
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.service.PictureService;
@Controller
public class PictureController {
	@Autowired
	private PictureService pictureService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/pic/upload")
	@ResponseBody
	public String pictureUpload(MultipartFile uploadFile){
		Map map = pictureService.uploadPicturet(uploadFile);
		String string = JsonUtils.objectToJson(map);
		return string;
	}
}
