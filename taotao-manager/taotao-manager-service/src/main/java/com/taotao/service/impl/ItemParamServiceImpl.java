package com.taotao.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService{
	
	@Autowired
	TbItemParamMapper itemParamMapper;
	
	
	@Override
	public EUDateGridResult getItemParamList(Integer page, Integer rows) {
		//目标是通过这两个参数,从数据库中的读取到想要的数据
		//1.首先是查询所有商品列表
		TbItemParamExample example=new TbItemParamExample();
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		//分页插件进行分页处理
		PageHelper.startPage(page, rows);
		EUDateGridResult result=new EUDateGridResult();
		//设置行数
		result.setRows(list);
		PageInfo<TbItemParam> info=new PageInfo<>(list);
		//计算数据总条数
		result.setTotal(info.getTotal());
		return result;
	}


	@Override
	public TaotaoResult getItemParamByCid(Long cid) {
		TbItemParamExample example=new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		if(list!=null&&list.size()>0) {
			return TaotaoResult.ok(list.get(0));
		}
		return TaotaoResult.ok();
	}


	@Override
	public TaotaoResult addItemParam(Long cid, String data) {
		TbItemParam itemParam=new TbItemParam();
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		itemParam.setItemCatId(cid);
		itemParam.setParamData(data);
		int row = itemParamMapper.insert(itemParam);
		if(row>0) {
			return TaotaoResult.ok();
		}
		return null;
	}


	@Override
	public TaotaoResult deleteItemParam(Long[] ids) {
		TbItemParamExample example=new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(Arrays.asList(ids));
		int row = itemParamMapper.deleteByExample(example);
		if(row>0) {
			return TaotaoResult.ok();
		}
		return null;
	}

}
