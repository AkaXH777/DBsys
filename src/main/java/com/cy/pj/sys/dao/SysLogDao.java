package com.cy.pj.sys.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.cy.pj.sys.pojo.SysLog;
@Mapper
public interface SysLogDao {
	
	int insertObject(SysLog entity);
	
	
	/**
	 * 	基于条件查询用户行为日志记录总数
	 * @param username 查询条件（用户名--例如查询哪个用户的日志信息）
	 * @return 总记录数 （基于这个结果可以计算总页数）
	 */
	int getRowCount(@Param("username") String username);
	
	/**
	 * 	基于条件查询当前页数据
	 * @param username 查询条件（例如查询哪个用户的日志信息）
	 * @param StartIndex 当前页的起始位置
	 * @param pageSize 当前页的页面大小（每页最多显示多少条记录）
	 * @return 当前页的日志记录信息
	 * 	数据库中每条日志信息封装到一个SysLog对象中
	 */
	List<SysLog> findPageObjects(
			@Param("username") String username,
			@Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize);
	/**
	 * 	基于id删除多条记录
	 * @param ids
	 * @return
	 */
	int deleteObjects(Integer... ids);
	
}
