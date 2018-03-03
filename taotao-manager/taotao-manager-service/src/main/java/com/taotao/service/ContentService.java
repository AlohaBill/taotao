package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	List<EasyUITreeNode> getContentCategoryList(long parentid);
	TaotaoResult addNode(Long parentId,String name);
	TaotaoResult deleteNode(Long nodeId);
	TaotaoResult update(Long id, String name);
	EUDateGridResult getCategoryList(Long categoryId, Integer page, Integer rows);
	TaotaoResult insertContent(TbContent content);
	TaotaoResult deleteFromIds(Long[] ids);
	TaotaoResult editContent(TbContent content);
	List<TbContent> getContentList(Long contentCategoryId);
}
