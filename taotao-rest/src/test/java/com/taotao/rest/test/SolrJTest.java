package com.taotao.rest.test;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {
	@Test
	public void t1() throws Exception{
		//创建一个连接
		SolrServer solrServer = new HttpSolrServer("http://192.168.146.128:8080/solr");
		//创建一个文档对象
		SolrInputDocument doucment=new SolrInputDocument();
		doucment.addField("id", "test001");
		doucment.addField("item_title", "测试商品01");
		doucment.addField("item_price", 12234);
		//将对象写入
		solrServer.add(doucment);
		//提交
		solrServer.commit();
	}
}
