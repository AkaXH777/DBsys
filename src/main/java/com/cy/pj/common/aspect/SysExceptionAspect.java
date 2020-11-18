package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class SysExceptionAspect {
	
	/**此方法可以作为一个异常监控方法*/
	@AfterThrowing(value="bean(*ServiceImpl)",throwing = "e")
	public void handleException(JoinPoint jp,Throwable e) {
		Class<?> tc=jp.getTarget().getClass();
		String className = tc.getName();
		System.out.println("className="+className);
		MethodSignature s = (MethodSignature)jp.getSignature();
		String methodName = s.getName();
		System.out.println("name="+methodName);
		String targetClassMethod=className+"."+methodName;
		log.error("{} exception msg is {}",targetClassMethod,e.getMessage());
		//拓展？
		//1)将日志写到日志文件
		//2)将出现异常的这个信息发送到某个人的邮箱(email)
		//3)将出现异常的清空发短信给运维人员
		//4)报警..........(播放音乐)
		
		
		
	}
	
	
}
