package com.taotao.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ItemParamService;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {
	
	@Autowired
	ItemParamService itemParamService;
	
	@RequestMapping("/list")
	@ResponseBody
	public EUDateGridResult getItemParamList(Integer page, Integer rows) {
		EUDateGridResult result=itemParamService.getItemParamList(page,rows);
		return result;
	}
	@RequestMapping("/query/itemcatid/{cid}")
	@ResponseBody
	public TaotaoResult getItemParamByCid(@PathVariable Long cid) {
		TaotaoResult result=itemParamService.getItemParamByCid(cid);
		return result;
	}
	@RequestMapping("/save/{cid}")
	@ResponseBody
	public TaotaoResult createItemParam(@PathVariable Long cid,HttpServletRequest request) {
		String data = request.getParameter("paramData");
		TaotaoResult result=itemParamService.addItemParam(cid,data);
		return result;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteItemParam(Long[] ids) {
		TaotaoResult result=itemParamService.deleteItemParam(ids);
		return result;
	}
}
