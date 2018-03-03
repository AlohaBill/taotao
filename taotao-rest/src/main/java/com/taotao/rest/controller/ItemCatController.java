package com.taotao.rest.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REST_INDEX_CATEGORY_DATA}")
	private String REST_INDEX_CATEGORY_DATA;
	
	@RequestMapping(value="/itemcat/list",produces="application/json;charset=utf-8")
	@ResponseBody
	public String getItemCatList(String callback) {
		//计算程序用时
		long startTime = System.currentTimeMillis();
		System.out.println("============分类列表请求开始处理===========");
		//这里返回的是一个json形成的字符串,缓存要处理的就是查找key,找到字符串返回即可
		//缓存存在,提取数据
		try {
			String result=jedisClient.get(REST_INDEX_CATEGORY_DATA);
			if (!StringUtils.isBlank(result)) {
				System.out.println("==不是第一次了,我是缓存数据,我进来了==");
				long endTime = System.currentTimeMillis();
				System.out.println("缓存直接提取数据时间:"+(endTime-startTime)+"毫秒");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CatResult result = itemCatService.getItemCatList();
		//需要将pojo对象转换成字符串
		String json=JsonUtils.objectToJson(result);
		String str=callback+"("+json+");";
		//缓存不存在,存入数据
		try {
			jedisClient.set(REST_INDEX_CATEGORY_DATA, str);
			System.out.println("==第一次到访,我查询数据库才到缓存这里==");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("============分类列表请求完成响应===========");
		long endTime = System.currentTimeMillis();
		System.out.println("数据库查询数据时间:"+(endTime-startTime)+"毫秒");
		return str;
	}
}
