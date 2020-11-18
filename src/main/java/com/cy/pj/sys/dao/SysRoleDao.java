package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.sys.pojo.CheckBox;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.pojo.SysRoleMenu;

@Mapper
public interface SysRoleDao {
	
	@Select("select id,name from sys_roles")
	List<CheckBox> findRoles();
	
	/**
	 * 	基于角色名统计查询角色相关信息
	 * @param name	角色名
	 * @return	统计的个数
	 */
	int getRowCount(String name);
	
	/**
	 * 	按条件从指定位置查询当前页角色信息
	 * @param name	角色名
	 * @param startIndex	起始位置
	 * @param pageSize	页面大小
	 * @return	当前页的角色信息
	 */
	List<SysRole> findPageObjects(String name,
			Integer startIndex,Integer pageSize);
	/**
	 * 	保存角色自身信息
	 * @param entitiy
	 * @return
	 */
	int insertObject(SysRole entitiy);
	
	/**
	 * 	基于角色id查找角色自身信息
	 * @param id
	 * @return
	 */
	SysRoleMenu findObjectById(Integer id);
	
	int updateObject(SysRole entity);

	@Delete("delete from sys_roles where id=#{id}")
	int deleteObject(Integer id);
}
