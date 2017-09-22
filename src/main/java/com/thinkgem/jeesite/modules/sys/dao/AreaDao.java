/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	/**
	 * 查询指定id或者code所有的父级编码 
	 * @param area
	 * @return
	 */
	public Area findParentId(Area area);
	/**
	 * 查询指定code下的省、市、区 
	 * @param area
	 * @return
	 */
	public List<Area> findList4(Map<String,Object> map);
	/**
	 * 根据指定的code查询area
	 * @param area
	 * @return
	 */
	public Area getAreaByCode(Area area);
	
	/**
	 * 更新
	 * @param area
	 */
	public void updateArea(Area area);
	
	/**
	 * 查询所有父级信息
	 * @param map
	 * @return
	 */
	public List<Area> getAreaByPIds(Map<String,Object> map);
	
	/**
	 * 验证当前节点或者子节点下是否关联过学校信息
	 * @param code
	 * @return
	 */
	public int checkIsLinkSchool(String code);
	/**
	 * 插入区域信息
	 * @param area
	 * @return
	 */
	public boolean insertArea(Area area);
	/**
	 * 查询指定id的第一级子类
	 * @param parentId
	 * @return
	 */
	public List<Area> findFirstChild(String parentId);
}
