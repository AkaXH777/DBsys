package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.annotation.RequiredLog02;
import com.cy.pj.sys.common.utils.IPUtils;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
@Aspect
public class SysLogAspect02 {
	
	private Logger log=LoggerFactory.getLogger(SysLogAspect02.class);
	@Autowired
	private SysLogService sysLogService;
	
//	@Pointcut("bean(sysUsrServiceImpl)")
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog02)")
	public void doLogPoint1() {}
	
	@Around("doLogPoint1()")
	public Object around(ProceedingJoinPoint jp) throws Throwable {
		long t1=System.currentTimeMillis();
		try {
			Object result = jp.proceed();
			long t2=System.currentTimeMillis();
			log.info("目标方法执行总时长{}", (t2-t1));
			saveLog(jp,(t2-t1));
			return result;
		} catch (Exception e) {
			log.error("目标方法在执行过程中出现的问题，具体为{}",e.getMessage());
			throw e;
		}
	}
	private void saveLog(ProceedingJoinPoint jp, long time)throws Throwable {
		//1.获取用户行为日志
		//1.1获取登录用户名
		String username="张九九";
		//1.2获取用户IP地址
		String ip=IPUtils.getIpAddr();
		//1.3获取操作名
		MethodSignature ms = (MethodSignature)jp.getSignature();
		//1.4获取方法名
		String methodName = ms.getName();
		//1.5获取目标class
		Class<? extends Object> targetCls = jp.getTarget().getClass();
		Method targetMethod = targetCls.getMethod(methodName, ms.getMethod().getParameterTypes());
		//1.5.1给method赋值
		String method=targetCls.getName()+"."+methodName;
		//1.6获取目标方法上的注解和值
		RequiredLog02 required = targetMethod.getDeclaredAnnotation(RequiredLog02.class);
		String operation=required.value();
		//获取方法参数并转json格式
		String params=new ObjectMapper().writeValueAsString(jp.getArgs());
		//2.封装用户行为日志
		SysLog log=new SysLog();
		log.setIp(ip);
		log.setOperation(operation);
		log.setMethod(method);
		log.setCreatedTime(new Date());
		log.setParams(params);
		log.setTime(time);
		log.setUsername(username);
		//3.存储用户行为日志
		sysLogService.saveObject(log);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
