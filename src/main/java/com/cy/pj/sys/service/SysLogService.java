package com.cy.pj.sys.service;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.SysLog;
/**	业务层接口
 * 	现在的项目都会分层设计，每次对象都会有对应的接口，层与层对象进行耦合时
 * 	建议耦合于接口
 */
public interface SysLogService {
	/**
	 * 	分页查询日志信息
	 * @param username	用户名
	 * @param pageCurrent	当前页码值
	 * @return	封装了查询和计算结果的一个分页对象
	 */
	PageObject<SysLog> findPageObjects(String username,Integer pageCurrent);
	
	
	int deleteObjects(Integer... ids);
	
	void saveObject(SysLog entity);
}
