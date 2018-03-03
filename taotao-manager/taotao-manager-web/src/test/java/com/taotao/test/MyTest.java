package com.taotao.test;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;

public class MyTest {
	@Test
	public void testPage() {
		//建立一个spring容器
		ApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");
		//从容器当中获取mapper代理对象
		TbItemMapper mapper = ctx.getBean(TbItemMapper.class);
		//执行查询
		TbItemExample example=new TbItemExample();
		//分页处理
		PageHelper.startPage(1,10);
		List<TbItem> items = mapper.selectByExample(example);
		//取商品列表
		for (TbItem tbItem : items) {
			System.out.println(tbItem.getTitle());
		}
		//取分页信息
		PageInfo<TbItem> pages=new PageInfo<>(items);
		long total = pages.getTotal();
		System.out.println("共有商品信息"+total+"条");
	}
	@Test
	public void testPage2() {
		//建立一个spring容器
		ApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");
		//从容器当中获取mapper代理对象
		TbItemParamMapper mapper = ctx.getBean(TbItemParamMapper.class);
		//执行查询
		TbItemParamExample example=new TbItemParamExample();
		//分页处理
		PageHelper.startPage(1,10);
		List<TbItemParam> items = mapper.selectByExampleWithBLOBs(example);
		//取商品列表
		for (TbItemParam tbItem : items) {
			System.out.println(tbItem.getParamData());
		}
		//取分页信息
		PageInfo<TbItemParam> pages=new PageInfo<>(items);
		long total = pages.getTotal();
		System.out.println("共有商品信息"+total+"条");
	}
	@Test
	public void show() {
		long millis = System.currentTimeMillis();
		Random random = new Random();
		int end2 = random.nextInt(98)+1;
		String str=millis+end2+"";
		System.out.println(str);
	}
	@Test
	public void showHello() {
		System.out.println("hello");
	}
}
