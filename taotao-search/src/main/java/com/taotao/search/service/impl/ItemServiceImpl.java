package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService{
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public TaotaoResult importAllItems() throws Exception {
		List<Item> itemList = itemMapper.getItemList();
		for (Item item : itemList) {
			//创建一个连接
//			SolrServer solrServer = new HttpSolrServer("http://192.168.146.128:8080/solr");
			//创建一个文档对象
			SolrInputDocument doucment=new SolrInputDocument();
			doucment.addField("id", item.getId());
			doucment.addField("item_title", item.getTitle());
			doucment.addField("item_sell_point", item.getSell_point());
			doucment.addField("item_price", item.getPrice());
			doucment.addField("item_image", item.getImage());
			doucment.addField("item_category_name", item.getCategory_name());
			doucment.addField("item_des", item.getItem_des());
			//将对象写入
			solrServer.add(doucment);
		}
		//提交
		solrServer.commit();
		return TaotaoResult.ok();
	}

}
