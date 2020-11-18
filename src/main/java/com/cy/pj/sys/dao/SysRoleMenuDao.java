package com.cy.pj.sys.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
/**
 * 	基于此对象操作角色和菜单关系表（sys_role_menus）数据
 */
@Mapper
public interface SysRoleMenuDao {
	
	List<Integer> findMenuIdsByRoleIds(List<Integer> roleIds);
	
	
	/**
	 * 	基于菜单id进行删除角色和菜单关系数据
	 * @param id
	 * @return
	 */
	int deleteObjectsByMenuId(Integer id);
	
	int insertObjects(Integer roleId,Integer[] menuIds);
	
	/**
	 * 	基于角色id获取菜单id
	 * @param id
	 * @return
	 */
	List<Integer> findMenuIdsByRoleId(Integer id);
}
