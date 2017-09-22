package com.thinkgem.jeesite.modules.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {
private final Logger logger = Logger.getLogger(AreaService.class);
	
	@Autowired
	private AreaDao areaDao;
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	/**
	 * 插入区域信息
	 * @param area
	 * @return
	 */
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertArea(Area area){
		try{
			//插入parentIds 拿到parent.parentId再加上parent.code
			String parent_code=area.getParent().getCode();
			String parent_name=area.getParent().getName();
			if(parent_code.indexOf(",")!=-1){
				parent_code=parent_code.substring(0,parent_code.length()-1);
			}
			if(parent_name.indexOf(",")!=-1){
				parent_name=parent_name.substring(0,parent_name.length()-1);
			}
			//给parent.code重新设置
			Area p_area=new Area();
			p_area.setCode(parent_code);
			p_area.setName(parent_name);
			area.setParent(p_area);
			//设置parentIds
			//Area area1=new Area();
			//area1.setCode(parent_code);
			Area parent_area=areaDao.getAreaByCode(p_area);
			String parentIds=parent_area.getParentIds();
			//给中国添加下级时,parentIds包含0,00000需要去掉0,才能正常展示树结构
			String lastString=parentIds.substring(parentIds.length()-2, parentIds.length()-1);
			if(lastString.equalsIgnoreCase(",")){
				parentIds=parentIds+parent_code+",";
			}else{
				parentIds=parentIds+","+parent_code+",";	
			}
			int index=parentIds.indexOf("0,");
			if(index!=-1 && index==0){
				parentIds=parentIds.substring(index+2, parentIds.length());
			}
			//注意最后需要加个,号
			area.setParentIds(parentIds);
			area.setId(IdGen.uuid());
			//设置编码
			//find_big_code(parent_code,area);
//			area.setCreate(UserUtils.getUser().getLoginName());
			area.setCreateDate(new Date());
			areaDao.insertArea(area);
			UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
			//移除树形标签 treeData中的缓存
//			CacheUtils.remove(AppConstant.AREASCACHE, AppConstant.CACHE_SYSAREAS_TREE_LIST);
//			CacheUtils.remove(AppConstant.AREASCACHE, AppConstant.CACHE_SYSAREAS_TREE_LIST + UserUtils.getUser().getId());
		}catch(Exception e){
		    e.printStackTrace();
		    logger.info(e.fillInStackTrace());
		    return false;
	    }
		return true;
	}
	
	//查出给定id的下级中最大的code
	public void find_big_code(String parentId,Area area){
		//找出第一级下级
		List<Area> areas=new ArrayList<Area>();
		areas=areaDao.findFirstChild(parentId);
		//找出最大的code
		Comparator<Area> comparator = new Comparator<Area>(){  
			   public int  compare(Area r1, Area r2) {
				   return  r1.getCode().compareTo(r2.getCode()); 
			   }  
        };  
        String type=area.getType();
        if(areas.size()!=0){
        	//有下级,最大编码存在
        	Collections.sort(areas,comparator);
    		String code=areas.get(areas.size()-1).getCode();
          	//区 编码+1 市 编码+100 省 编码+10000
          	if("4".equals(type)){
          		area.setCode(""+(Integer.valueOf(code)+1));
          	}else if("3".equals(type)){
          	    area.setCode(""+(Integer.valueOf(code)+100));	
          	}else if("2".equals(type)){
          		area.setCode(""+(Integer.valueOf(code)+10000));
          		area.setSort(areas.get(areas.size()-1).getSort());
          	}else if("1".equals(type)){
          		area.setCode(""+(Integer.valueOf(code)+100000));
          	}
        }else{
        	if("4".equals(type)){
          		area.setCode(""+(Integer.valueOf(parentId)+1));
          	}else if("3".equals(type)){
          	    area.setCode(""+(Integer.valueOf(parentId)+100));	
          	}else if("2".equals(type)){
          		area.setCode(""+(Integer.valueOf(parentId)+10000));
          	}else if("1".equals(type)){
          		area.setCode(""+(Integer.valueOf(parentId)+100000));
          	}
        }
	}
	
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		//移除树形标签 treeData中的缓存
//		CacheUtils.remove(AppConstant.AREASCACHE, AppConstant.CACHE_SYSAREAS_TREE_LIST);
//		CacheUtils.remove(AppConstant.AREASCACHE, AppConstant.CACHE_SYSAREAS_TREE_LIST + UserUtils.getUser().getId());
	}
	
	public List<Area> findList(Area area){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//area.getSqlMap().put("dsf", dataScopeFilter(area.getCurrentUser(), "p",""));
		//查询指定code下的所有父级编码
		if(StringUtils.isBlank(area.getCode())){
			return dao.findList(area);
		}
		Area area2=dao.findParentId(area);
		if(null!=area2){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("area", area);
			String parentIds = area2.getParentIds();
			map.put("arr", parentIds!=null?parentIds.split(","):0);
			map.put("DEL_FLAG_NORMAL", '0');
			return dao.findList4(map);
		}
		return new ArrayList<Area>();
	}
	
	/**
	 * 根据指定的code返回对应的Area
	 * @param area
	 * @return
	 */
	public Area getAreaByCode(Area area){
		return dao.getAreaByCode(area);
	}
	
	/**
	 * 查询指定id或者code所有的父级编码 
	 * @param area
	 * @return
	 */
	public Area findParentId(Area area){
		return dao.findParentId(area);
	}
	
	/**
	 * 更新
	 * @param area
	 */
	@Transactional(readOnly = false)
	public void updateArea(Area area){
		dao.updateArea(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		//移除树形标签 treeData中的缓存
//		CacheUtils.remove(AppConstant.AREASCACHE, AppConstant.CACHE_SYSAREAS_TREE_LIST);
//		CacheUtils.remove(AppConstant.AREASCACHE, AppConstant.CACHE_SYSAREAS_TREE_LIST + UserUtils.getUser().getId());
		return;
	}
	
	/**
	 * 验证当前节点或者子节点下是否关联过学校信息
	 * @param code
	 * @return
	 */
	public int checkIsLinkSchool(String code) {
		return dao.checkIsLinkSchool(code);
	}
}
