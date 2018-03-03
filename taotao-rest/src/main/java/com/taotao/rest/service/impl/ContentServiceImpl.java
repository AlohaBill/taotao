package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.dao.impl.JedisClientSingle;
import com.taotao.rest.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Value(value="${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;

	@Override
	public List<TbContent> getContentList(Long contentCategoryId) {
		// 从缓存中提取数据
		try {
			String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCategoryId + "");
			if (!StringUtils.isBlank(result)) {
				// 字符串转换成list
				List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
				return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据内容分类id查询内容列表
		TbContentExample example = new TbContentExample();
		com.taotao.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCategoryId);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		// 往缓存中存入数据
		try {
			String cacheString = JsonUtils.objectToJson(list);
			jedisClient.hset(INDEX_CONTENT_REDIS_KEY, contentCategoryId + "", cacheString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}