package com.taotao.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 商品管理controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	//通过id查找商品
	@RequestMapping("/{itemId}")
	@ResponseBody
	public TbItem getById(@PathVariable Long itemId) {
		TbItem tbitem = itemService.getItemById(itemId);
		return tbitem;
	}
	
	//获取指定页面数据(分页)
	@RequestMapping("/list")
	@ResponseBody
	public EUDateGridResult getItemList(Integer page, Integer rows) {
		EUDateGridResult itemList = itemService.getItemList(page, rows);
		return itemList;
	}
	
	//新增商品
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createItem(TbItem item,String desc,String itemParams) throws Exception {
		TaotaoResult result = itemService.createItem(item,desc,itemParams);
		return result;
	}
	
	//修改商品信息
	@RequestMapping("/update")
	@ResponseBody
	public TaotaoResult updateItem(TbItem item) {
		TaotaoResult result = itemService.updateItem(item);
		return result;
	}
	
	//删除商品(数据库抹除)
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteByIds(Long[] ids) {
		TaotaoResult result = itemService.deleteByIds(ids);
		return result;
	}
//	//这个删除不是真的删除,而是修改商品状态为删除(status=3)
//	@RequestMapping("/delete")
//	@ResponseBody
//	public TaotaoResult deleteByIds(Long[] ids) {
//		TaotaoResult result = itemService.updateStatusDelByIds(ids);
//		return result;
//	}
	
	//上架
	@RequestMapping("/reshelf")
	@ResponseBody
	public TaotaoResult reshelfByIds(Long[] ids) {
		TaotaoResult result = itemService.reshelfByIds(ids);
		return result;
	}
	//下架
	@RequestMapping("/instock")
	@ResponseBody
	public TaotaoResult instockByIds(Long[] ids) {
		TaotaoResult result = itemService.instockByIds(ids);
		return result;
	}
	

}
