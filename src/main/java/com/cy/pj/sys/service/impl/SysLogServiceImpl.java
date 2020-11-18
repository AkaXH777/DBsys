package com.cy.pj.sys.service.impl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;

	//将来希望此业务方法参与到其它事务中执行,传播特性设置为Propagation.REQUIRED
	//将来希望此业务方法始终运行在一个独立事务中,传播特性设置为Propagation.REQUIRES_NEW
	//将写日志操作放在一个独立的事务
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Async	//此注解描述的方法会运行在spring框架提供的一个线程中
	@Override
	public void saveObject(SysLog entity) {
		sysLogDao.insertObject(entity);
	}
	@Override
	public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
		//1.验证参数合法性
		//1.1验证pageCount的合法性
		//不合法抛出IllegalArgumentException异常
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码不正确");
		//统计总记录数并校验
		int rowCount=sysLogDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("系统没有查到对应记录");
		//查询当前页数据
		int pageSize=6;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysLog> records=sysLogDao.findPageObjects(username, startIndex, pageSize);
		//封装查询以及计算结果
		PageObject<SysLog> pageObject=new PageObject<>();
		pageObject.setRowCount(rowCount);
		pageObject.setRecords(records);
		pageObject.setPageSize(pageSize);
		pageObject.setPageCurrent(pageCurrent);
		int pageCount=rowCount/pageSize;
		if(rowCount%pageSize!=0)pageCount++;
		pageObject.setPageCount(pageCount);
		return pageObject;
//		return new PageObject<>(pageCurrent,pageSize,rowCount,records);
	}
	
	/**
	 * 	@RequiresPermissions 为shiro框架用于描述切入点方法的一个注解对象，
	 * 	一旦由这个注解对业务方法进行 了描述，就表示这个业务方法必须授权才能访问。
	 * 	那什么情况下才可以授权访问呢？（当用户权限标识中包含注解中定义的权限标识时。）
	 */
	@RequiresPermissions("sys:log:delete")
	@Override
	public int deleteObjects(Integer... ids) {
		//1.判定参数合法性
		if(ids==null||ids.length==0)
			throw new IllegalArgumentException("请选择一个");
		//2.执行删除操作 
		int rows=sysLogDao.deleteObjects(ids);
		if(rows==0)
			throw new ServiceException("该记录可能已经不存在了");
		//5.返回结果
		return rows;
	}
	
	
	
	
	
	

}
