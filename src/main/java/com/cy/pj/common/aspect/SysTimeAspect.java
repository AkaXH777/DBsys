package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
public class SysTimeAspect {
	@Pointcut("bean(sysUserServiceImpl)")
	public void doTime() {}
	
	/**before通知在目标方法执行之前执行*/
	@Before("doTime()")
	public void doBefore() {
		System.out.println("time doBefore()");
	}
	/**after通知在目标方法结束之前（return或throw之前）执行*/
	@After("doTime()")
	public void doAfter() {
		System.out.println("time doAfter()");
	}
	/**after之后程序没有出现异常则执行此通知*/
	@AfterReturning("doTime()")
	public void doAfterReturning() {
		System.out.println("time doAfterReturning()");
	}
	/**after之后程序出现异常则执行此通知*/
	@AfterThrowing("doTime()")
	public void doAfterThrowing() {
		System.out.println("time doAfterThrowing()");
	}
	
	//ProceedingJoinPoint 这个类型只能作为环绕通知的方法参数
	@Around("doTime()")
	public Object doAround(ProceedingJoinPoint jp)throws Throwable {
		System.out.println("doAround.before");
		try {
			Object result = jp.proceed();
			System.out.println("doAround.after");
			return result;
		} catch (Exception e) {
			System.out.println("doAround.error");
			throw e;
		}
	}
	
	
}
