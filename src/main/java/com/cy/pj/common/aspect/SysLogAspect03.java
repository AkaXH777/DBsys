package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog03;
import com.cy.pj.sys.common.utils.IPUtils;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class SysLogAspect03 {
	private Logger log =LoggerFactory.getLogger(SysLogAspect03.class);
	@Autowired
	private SysLogService sysLogService;
//	@Pointcut("bean(SysUserServiceImpl)")
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog03)")
	public void doLogPoint2() {}
	
	@Around("doLogPoint2()")
	public Object around(ProceedingJoinPoint jp) throws Throwable{
		long t1=System.currentTimeMillis();
		try {
			Object result = jp.proceed();
			long t2=System.currentTimeMillis();
			log.info("目标方法执行时间：	{}", (t2-t1));
			saveLog(jp,(t2-t1));
			return result;
		} catch (Exception e) {
			log.error("目标方法执行过程出现的问题：	{}",e.getMessage());
			throw e;
		}
	}

	private void saveLog(ProceedingJoinPoint jp, long time) throws Throwable{
		//1.获取用户行为日志信息
		//1.1获取用户名
		String username="小红";
		//1.2获取用户IP地址
		String ip=IPUtils.getIpAddr();
		System.out.println("ip="+ip);
		//1.3获取操作对象--这里是pageObject
		MethodSignature ms = (MethodSignature)jp.getSignature();
		System.out.println("ms="+ms);//ms=PageObject com.cy.pj.sys.service.impl.SysUserServiceImpl.findPageObjects(String,Integer)
		//1.4获取方法名
		String methodName = ms.getName();
		System.out.println("methodName="+methodName);//methodName=findPageObjects
		System.out.println("ms.getParameterTypes()="+ms.getParameterTypes());//ms.getParameterTypes()=[Ljava.lang.Class;@d27eedd
		//1.5获取目标类
		Class<? extends Object> targetCls = jp.getTarget().getClass();//targetCls=class com.cy.pj.sys.service.impl.SysUserServiceImpl
		System.out.println("targetCls="+targetCls);
		//1.6获取目标方法
		Method targetMethod = targetCls.getMethod(methodName, ms.getMethod().getParameterTypes());
		System.out.println("ms.getMethod().getParameterTypes()="+ms.getMethod().getParameterTypes());
		//targetMethod=public com.cy.pj.common.pojo.PageObject com.cy.pj.sys.service.impl.SysUserServiceImpl.findPageObjects(java.lang.String,java.lang.Integer)
		System.out.println("targetMethod="+targetMethod);
		//1.6获取method值
		String method=targetCls.getName()+","+methodName;
		//method=com.cy.pj.sys.service.impl.SysUserServiceImpl,findPageObjects
		System.out.println("method="+method);
		//1.7获取方法上的注解和值
		RequiredLog03 required = targetMethod.getDeclaredAnnotation(RequiredLog03.class);
		//required=@com.cy.pj.common.annotation.RequiredLog03(value=分页查询)
		System.out.println("required="+required);
		String operation=required.value();
		//operation=分页查询
		System.out.println("operation="+operation);
		//1.8获取方法上的参数params
		String params=new ObjectMapper().writeValueAsString(jp.getArgs());
		//params=["",1]
		System.out.println("params="+params);
		//2.封装用户行为日志信息
		SysLog log=new SysLog();
		log.setIp(ip);
		log.setMethod(method);
		log.setOperation(operation);
		log.setParams(params);
		log.setUsername(username);
		log.setTime(time);
		log.setCreatedTime(new Date());
		System.out.println("log="+log);
		//3.存储用户行为日志信息
		sysLogService.saveObject(log);
	}
	
	
	
	
	
	
	
	
}
