package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

/**
 * 商品分类服务
 * 
 * @author Bill
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper mapper;

	@Override
	public CatResult getItemCatList() {
		CatResult result = new CatResult();
		result.setData(getCatList(0));
		return result;
	}

	/**
	 * 查询分类列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<?> getCatList(long parentId) {
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询操作
		List<TbItemCat> list = mapper.selectByExample(example);
		// 返回值list
		List resultList = new ArrayList<>();
		int count = 0;
		for (TbItemCat cat : list) {
			// 判断是否为父节点
			if (cat.getIsParent()) {
				CatNode node = new CatNode();
				if (parentId == 0) {
					node.setName("<a href='/products/" + cat.getId() + "1.html'>" + cat.getName() + "</a>");
				} else {
					node.setName(cat.getName());
				}
				node.setUrl("/products/" + cat.getId() + ".html");
				node.setItem(getCatList(cat.getId()));
				resultList.add(node);
				// 因为静态页面项目只有14个,而数据库超过14个,所以需要判断来截取数据
				count++;
				if (count >= 14) {
					break;
				}
			} else {
				resultList.add("/products/" + cat.getId() + ".html|" + cat.getName());
			}
		}
		return resultList;
	}

}
