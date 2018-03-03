package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDateGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {
	@Value(value="${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value(value="${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Autowired
	private TbContentMapper contentMapper;
	/**
	 * 通过父节点找子节点
	 */
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentid) {
		// 根据parentid查询内容分类列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentid);
		// 取出所有的分类信息
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			// 循环的目标:取出数据,封装成一个个节点类
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			// 判断是否是父节点
			if (tbContentCategory.getIsParent()) {
				node.setState("closed");
			} else {
				node.setState("open");
			}
			resultList.add(node);
		}
		return resultList;
	}
	/**
	 * 加入节点
	 */
	@Override
	public TaotaoResult addNode(Long parentId, String name) {
		TbContentCategory node = new TbContentCategory(null, parentId, name, 1, 1, false, new Date(), new Date());
		contentCategoryMapper.insert(node);
		// 判断如果父节点的isparent不是true修改为true
		// 取父节点的内容
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKeySelective(parentNode);
		}
		// 把新节点返回
		return TaotaoResult.ok(node);
	}
	/**
	 * 删除节点
	 */
	@Override
	public TaotaoResult deleteNode(Long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 判断删除的节点是否为父类
		if (contentCategory.getIsParent()) {
			List<TbContentCategory> list = getContentCategoryListByParentId(id);
			// 递归删除
			for (TbContentCategory tbContentCategory : list) {
				deleteNode(tbContentCategory.getId());
			}
		}
		contentCategoryMapper.deleteByPrimaryKey(id);
		// 判断父类中是否还有子类节点，没有的话，把父类改成子类
		if (getContentCategoryListByParentId(contentCategory.getParentId()).size() == 0) {
			TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
			parentCategory.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		return TaotaoResult.ok();
	}

	/**
	 *  通过父节点id来查询所有子节点，这是抽离出来的公共方法
	 * @param id
	 * @return
	 */
	private List<TbContentCategory> getContentCategoryListByParentId(long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		return list;
	}
	/**
	 * 更新节点信息
	 */
	@Override
	public TaotaoResult update(Long id, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setId(id);
		category.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(category);
		return TaotaoResult.ok();
	}
	/**
	 * 根据分类查询数据,分页展示
	 */
	@Override
	public EUDateGridResult getCategoryList(Long categoryId, Integer page, Integer rows) {
		// 根据分类ID查出所有的商品
		TbContentExample example = new TbContentExample();
		com.taotao.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		// 分页处理
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 创建一个返回值对象
		EUDateGridResult result = new EUDateGridResult();
		result.setRows(list);
		// 封装页面数据
		PageInfo<TbContent> info = new PageInfo<>(list);
		result.setTotal(info.getTotal());
		return result;
	}
	/**
	 * 增加内容
	 */
	@Override
	public TaotaoResult insertContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		//缓存同步
		try {
			HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}
	/**
	 * 删除内容
	 */
	@Override
	public TaotaoResult deleteFromIds(Long[] ids) {
		TbContentExample example = new TbContentExample();
		com.taotao.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andIdIn(Arrays.asList(ids));
		contentMapper.deleteByExample(example);

		return TaotaoResult.ok();
	}
	/**
	 * 编辑内容
	 */
	@Override
	public TaotaoResult editContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeyWithBLOBs(content);
		//缓存同步
		try {
			HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}
	/**
	 * 根据内容分类id,查询内容列表
	 */
	@Override
	public List<TbContent> getContentList(Long contentCategoryId) {
		// 根据内容分类id查询内容列表
		TbContentExample example = new TbContentExample();
		com.taotao.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCategoryId);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		return list;
	}

}