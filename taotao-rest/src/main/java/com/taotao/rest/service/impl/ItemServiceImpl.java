package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper descMapper;
	@Autowired
	private TbItemParamItemMapper itemParamMapper;
	// 缓存添加
	@Autowired
	private JedisClient jedis;
	@Value(value = "${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value(value = "${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;

	@Override
	public TaotaoResult getItemBaseInfo(long itemId) {
		try {
			String str = jedis.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
			if (!StringUtils.isBlank(str)) {
				TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
				return TaotaoResult.ok(item);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			jedis.set(REDIS_ITEM_KEY + ":" + itemId + ":base", JsonUtils.objectToJson(item));
			jedis.expire(REDIS_ITEM_KEY + ":" + itemId + ":base", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(item);
	}

	@Override
	public TaotaoResult getItemDesc(long itemId) {
		try {
			String str = jedis.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
			if (!StringUtils.isBlank(str)) {
				TbItemDesc desc = JsonUtils.jsonToPojo(str, TbItemDesc.class);
				return TaotaoResult.ok(desc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc desc = descMapper.selectByPrimaryKey(itemId);
		try {
			jedis.set(REDIS_ITEM_KEY + ":" + itemId + ":desc", JsonUtils.objectToJson(desc));
			jedis.expire(REDIS_ITEM_KEY + ":" + itemId + ":desc", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(desc);
	}

	@Override
	public TaotaoResult getItemParam(long itemId) {
		//缓存提取
		try {
			String str = jedis.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
			if (!StringUtils.isBlank(str)) {
				TbItemParamItem param = JsonUtils.jsonToPojo(str, TbItemParamItem.class);
				return TaotaoResult.ok(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			TbItemParamItem param = list.get(0);
			//缓存写入
			try {
				jedis.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(param));
				jedis.expire(REDIS_ITEM_KEY + ":" + itemId + ":param", REDIS_ITEM_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TaotaoResult.ok(param);
		}
		return TaotaoResult.build(400, "无此商品规格");
	}

}
