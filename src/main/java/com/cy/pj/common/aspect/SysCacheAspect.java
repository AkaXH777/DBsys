package com.cy.pj.common.aspect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SysCacheAspect {
	//假设此对象为存储数据的一个缓存对象
	private Map<String,Object> cache=new ConcurrentHashMap<>();//线程安全的hashMap
	
	//注解方式的切入点表达式的定义(细粒度的表达式)
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
	public void doCache() {}
	
	@Pointcut("@annotation(com.cy.pj.common.annotation.ClearCache)")
	public void doClearCache() {}
	
	@AfterReturning("doClearCache()")
	public void doAfterReturning(JoinPoint jp) throws Throwable{
		if(cache!=null) {
			cache.clear();
		}
	}
	
	@Around("doCache()")
	public Object around(ProceedingJoinPoint jp) throws Throwable{
		System.out.println("Get data from cache");
		Object obj=cache.get("deptKey");//这里的deptKey现在为一个固定值
		if(obj!=null) 
			return obj;
		obj = jp.proceed();
		System.out.println("Put data to cache");
		cache.put("deptKey", obj);
		return obj;
	}
	
}
