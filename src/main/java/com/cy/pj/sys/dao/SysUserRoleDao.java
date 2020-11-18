package com.cy.pj.sys.dao;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface SysUserRoleDao {
	
	
	
	int deleteObjectByUserId(Integer userId);
	
	//@Select("select role_id from sys_user_roles where user_id=#{id}")
	List<Integer> findRoleIdsByUserId(Integer id);
	
	int insertObjects(Integer userId,Integer[]roleIds);
}
