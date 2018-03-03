package com.taotao.rest.service;

import java.util.List;

import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	List<TbContent> getContentList(Long contentCategoryId);
}
