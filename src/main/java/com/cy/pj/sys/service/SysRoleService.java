package com.cy.pj.sys.service;

import java.util.List;

import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.CheckBox;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.pojo.SysRoleMenu;

public interface SysRoleService {
	/**
	 * 	查询当前页角色信息
	 * @param name
	 * @param pageCurrent
	 * @return
	 */
	PageObject<SysRole> findPageObjects(String name,Integer pageCurrent);

	int saveObject(SysRole entity,Integer[] menuIds);
	int updateObject(SysRole entity,Integer[] menuIds);
	
	List<CheckBox> findRoles();

	/**
	 * 	基于角色id获取角色以及角色对应的菜单id
	 * @param id
	 * @return
	 */
	SysRoleMenu findObjectById(Integer id);
	
	int deleteObject(Integer id);
}
