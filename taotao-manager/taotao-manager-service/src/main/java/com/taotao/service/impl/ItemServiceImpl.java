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
import com.taotao.common.utils.IdUtil;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
/**
 * 商品管理service
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService{
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Override
	public TbItem getItemById(long itemId) {
//		TbItem item=itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example=new TbItemExample();
		Criteria criteria=example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list!=null&&list.size()>0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}
	/**
	 * 商品列表查询
	 */
	@Override
	public EUDateGridResult getItemList(int page, int rows) {
		//查询商品列表
		TbItemExample example=new TbItemExample();
		//分页处理
		PageHelper.startPage(page,rows);
		List<TbItem> list=itemMapper.selectByExample(example);
		//创建一个返回值对象
		EUDateGridResult result=new EUDateGridResult();
		result.setRows(list);
		//去记录总条数
		PageInfo<TbItem> info=new PageInfo<>(list);
		result.setTotal(info.getTotal());
		return result;
	}
	@Override
	public TaotaoResult createItem(TbItem item,String desc,String itemParam) throws Exception {
		//创建一个item
		//首先要生成商品的id
		long id=IdUtil.getItemId();
		item.setId(id);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		TaotaoResult result = insertItemDesc(id, desc);
		if(result.getStatus()!=200) {
			throw new Exception();
		}
		result = insertItemParam(id, itemParam);
		if(result.getStatus()!=200) {
			throw new Exception();
		}
		return TaotaoResult.ok();
	}
	
	/**
	 * 添加商品描述
	 */
	private TaotaoResult insertItemDesc(Long itemId,String desc) {
		TbItemDesc itemDesc=new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		int row = tbItemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}
	/**
	 * 添加商品规格参数
	 */
	private TaotaoResult insertItemParam(Long itemId,String itemParam) {
		TbItemParamItem item=new TbItemParamItem();
		item.setItemId(itemId);
		item.setParamData(itemParam);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemParamItemMapper.insert(item);
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult updateItem(TbItem item) {
		int count = itemMapper.updateByPrimaryKeySelective(item);
		if(count>0) {
			return TaotaoResult.ok();
		}
		return null;
	}
	@Override
	public TaotaoResult deleteByIds(Long[] ids) {
		TbItemExample example=new TbItemExample();
		//创建删除条件
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(Arrays.asList(ids));
		//返回影响的行数
		int count = itemMapper.deleteByExample(example);
		if(count>0) {
			//如果有影响说明找到对应的item,返回包装的成功数据
			return TaotaoResult.ok();
		}
		//若没有就返回null
		return null;
	}
	@Override
	public TaotaoResult updateStatusDelByIds(Long[] ids) {
		TbItem item=new TbItem();
		item.setStatus((byte) 3);
		TbItemExample example=new TbItemExample();
		//创建删除条件
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(Arrays.asList(ids));
		int count=itemMapper.updateByExampleSelective(item, example);
		if(count>0) {
			//如果有影响说明找到对应的item,返回包装的成功数据
			return TaotaoResult.ok();
		}
		return null;
	}
	@Override
	public TaotaoResult reshelfByIds(Long[] ids) {
		TbItem item=new TbItem();
		item.setStatus((byte) 1);
		TbItemExample example=new TbItemExample();
		//创建条件
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(Arrays.asList(ids));
		int count=itemMapper.updateByExampleSelective(item, example);
		if(count>0) {
			//如果有影响说明找到对应的item,返回包装的成功数据
			return TaotaoResult.ok();
		}
		return null;
	}
	@Override
	public TaotaoResult instockByIds(Long[] ids) {
		TbItem item=new TbItem();
		item.setStatus((byte) 2);
		TbItemExample example=new TbItemExample();
		//创建条件
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(Arrays.asList(ids));
		int count=itemMapper.updateByExampleSelective(item, example);
		if(count>0) {
			//如果有影响说明找到对应的item,返回包装的成功数据
			return TaotaoResult.ok();
		}
		return null;
	}
}
