package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.pojo.Node;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.pojo.SysUserMenu;

public interface SysMenuService {

	List<SysUserMenu> findUserMenusByUserId(Integer id);
	
	List<Map<String,Object>> findObjects();
	
	/**
	 * 	基于菜单id删除菜单以及菜单对应 的关系数据
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	List<Node> findZtreeMenuNodes();
	
	/**
	 * 	保存菜单信息
	 * @param entity
	 * @return
	 */
	int saveObject(SysMenu entity);
	
	/**
	 * 	更新菜单信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysMenu entity);
	
}
