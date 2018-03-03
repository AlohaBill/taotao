package com.taotao.service;

import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.TaotaoResult;

public interface ItemParamService {

	EUDateGridResult getItemParamList(Integer page, Integer rows);
	TaotaoResult getItemParamByCid(Long cid);
	TaotaoResult addItemParam(Long cid, String data);
	TaotaoResult deleteItemParam(Long[] ids);

}
