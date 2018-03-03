package com.taotao.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.service.ItemParamItemService;

@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {

	@Autowired
	private TbItemParamItemMapper mapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getItemParamByItemId(Long itemId) {
		// 目标:取出规格参数数据,将json数据转成字符串
		// 1.从数据库获得数据
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> items = mapper.selectByExampleWithBLOBs(example);
		if (items == null || items.size() == 0) {
			return "";
		}
		// 2.转换成json数据
		TbItemParamItem tbItemParamItem = items.get(0);
		String paramData = tbItemParamItem.getParamData();
		// 取出来的数据是json格式,将这个数据转化成list格式
		List<Map> list = JsonUtils.jsonToList(paramData, Map.class);
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
		sb.append("<tbody>\n");
		for (Map m1 : list) {
			sb.append("<tr>\n");
			sb.append("<th class=\"tdTitle\" colspan=\"2\">" + m1.get("group") + "</th>\n");
			sb.append("</tr>\n");
			List<Map> list2 = (List<Map>) m1.get("params");
			for (Map m2 : list2) {
				sb.append("<tr>\n");
				sb.append("<td class=\"tdTitle\">" + m2.get("k") + "</td>\n");
				sb.append("<td>" + m2.get("v") + "</td>\n");
				sb.append("</tr>\n");
			}
		}
		sb.append("    </tbody>\n");
		sb.append("</table>");
		return sb.toString();
	}

}
