package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserDept;

@Mapper
public interface SysUserDao {
	
	int updatePassword(String password,String salt,Integer id);
	
	@Select("select * from sys_users where username=#{username}")
	SysUser findUserByUserName(String username);
	
	int updateObject(SysUser entity);
	
	//@Select("select * from sys_users where id=#{id}")
	SysUserDept findObjectById(Integer id);
	
	int insertObject(SysUser entity);
	
	int validById(Integer id,Integer valid,String modifiedUser);
	
	int getRowCount(String username);
	
	List<SysUserDept> findPageObjects(String username,Integer startIndex,Integer pageSize);
}
