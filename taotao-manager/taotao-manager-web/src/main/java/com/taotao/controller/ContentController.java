package com.taotao.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtils;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/category/list")
	@ResponseBody
	public List<EasyUITreeNode> showCategory(@RequestParam(value="id", defaultValue="0") long parentid) {
		List<EasyUITreeNode> list = contentService.getContentCategoryList(parentid);
		return list;
	}
	@RequestMapping("/category/create")
	@ResponseBody
	public TaotaoResult createCategory(Long parentId,String name) {
		TaotaoResult result = contentService.addNode(parentId, name);
		return result;
	}
	@RequestMapping("/category/delete")
	@ResponseBody
	public TaotaoResult deleteCategory(Long id) {
		TaotaoResult result = contentService.deleteNode(id);
		return result;
	}
	@RequestMapping("/category/update")
	@ResponseBody
	public TaotaoResult update(Long id,String name) {
		TaotaoResult result = contentService.update(id,name);
		return result;
	}
	@RequestMapping("/query/list")
	@ResponseBody
	public EUDateGridResult update(Long categoryId,Integer page, Integer rows) {
		EUDateGridResult list = contentService.getCategoryList(categoryId,page, rows);
		return list;
	}
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult save(TbContent content) {
		TaotaoResult result = contentService.insertContent(content);
		return result;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult delete(Long[] ids) {
		TaotaoResult result = contentService.deleteFromIds(ids);
		return result;
	}
	@RequestMapping("/edit")
	@ResponseBody
	public TaotaoResult edit(TbContent content) {
		TaotaoResult result = contentService.editContent(content);
		return result;
	}
	@RequestMapping("/list/{contentCategoryId}")
	@ResponseBody
	public TaotaoResult getContentList(@PathVariable Long contentCategoryId) {
		try {
			List<TbContent> list = contentService.getContentList(contentCategoryId);
			return TaotaoResult.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtils.getStackTrace(e));
		}
	}

	
}
