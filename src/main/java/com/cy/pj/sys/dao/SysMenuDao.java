package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.cy.pj.common.pojo.Node;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.pojo.SysUserMenu;

@Mapper	
public interface SysMenuDao {
	
	
	
	List<SysUserMenu> findMenusByIds(List<Integer> menuIds);
	
	List<String> findPermissions(List<Integer> menuIds);

	
	/**
	 * 	查询所有菜单信息以及这个菜单对应的上级菜单信息
	 * @return
	 */
	List<Map<String,Object>> findObjects();
	
	
	/**
	 * 	基于id获取当前菜单对应的子菜单个数
	 * @param id	菜单id
	 * @return	子菜单的个数
	 */
	int getChildCount(Integer id);
	
	/**
	 * 	基于id删除当前菜单对象
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	/**
	 * 	从菜单表查询菜单id，名称，上级菜单id
	 * @return
	 */
	List<Node> findZtreeMenuNodes();
	
	int insertObject(SysMenu entity);
	
	int updateObject(SysMenu entity);
}
