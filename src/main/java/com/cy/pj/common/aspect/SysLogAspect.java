package com.cy.pj.common.aspect;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
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
import com.cy.pj.sys.common.utils.IPUtils;
import com.cy.pj.sys.common.utils.ShiroUtils;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.service.SysLogService;
import com.cy.pj.sys.service.SysUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 	@Aspect注解描述的类为spring aop中的一个切面类型，此类型可以定义：
 * 	1)切入点(PointCut)方法(可以是多个)：要进行功能扩展的一些点
 * 	2)通知(Advice)方法(可以是多个)：封装了扩展功能的一些方法(在切入点方法之前或之后要执行的方法)
 */
//@Order(1) 注解描述的是切面的优先级，数字越小代表优先级越高，默认优先级比较低
@Component
@Aspect
public class SysLogAspect {

	private Logger log=LoggerFactory.getLogger(SysLogAspect.class);
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 	@Pointcut 注解描述的方法为切入点方法,注解中定义的内容为切入点表达式(可以有多种)
	 * 	1)bean(bean名称)	切入点表达式，这个表达式中的名字为spring容器中管理的一个bean的名字
	 * 	2)bean表达式是一种粗粒度的切入点表达式，这种表达式定义的切入点表示bean中的所有方法
	 * 	都是将来要切入扩展功能的一些方法(姆博安排方法)
	 * 	在当前应用中，sysUserServiceImpl这个名字对应的bean中所有方法的及集合为切入点
	 */
	//	@Pointcut("bean(sysUserServiceImpl)")
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	public void logPointCut() {}//切入点方法中不写任何内容,只是切入点表达式的载体

	/**
	 * 	@Around	注解描述的方法为一个通知方法，这个通知方法我们称为环绕通知，可以在
	 * 	目标方法执行之前或之后做服务增益。在环绕通知方法我们可以字节控制目标方法调用
	 * @param jp	连接点对象，此对象封装了要执行的目标方法信息
	 * @return	目标的执行结果
	 * @throws Throwable	执行目标方法过程中出现的异常
	 */
	@Around("logPointCut()")
	public Object arround(ProceedingJoinPoint jp) throws Throwable{
		long t1=System.currentTimeMillis();
		try {
			Object result = jp.proceed();//假如本类中有@Before先执行@Before,最后执行目标方法
			long t2=System.currentTimeMillis();
			log.info("方法执行的总时长："+(t2-t1));
			saveSysLog(jp,(t2-t1));
			return result;
		}catch(Throwable e) {
			log.error("目标方法执行过程中出现了问题，具体为{}",e.getMessage());
			throw e;
		}
	}
	private void saveSysLog(ProceedingJoinPoint jp, long time) throws Throwable, Throwable {
		//1.获取用户行为日志
		//1.1获取IP地址
		String ip=IPUtils.getIpAddr();
		//1.2获取登录用户名
//		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		SysUser user=ShiroUtils.getUser();
		String username=user.getUsername();
//		String username=ShiroUtils.getUser().getUsername();
//		String username="AAA";//做完登录操作后再修改，暂时先用固定值
		//1.3获取操作名
		//1.3.1获取目标对象类型
		Class<?> targetCls = jp.getTarget().getClass();
		//1.3.2获取目标方法
		MethodSignature ms = (MethodSignature)jp.getSignature();
		String methodName = ms.getName();
		Class<?>[] parameterTypes = ms.getMethod().getParameterTypes();
		Method targetMethod = targetCls.getMethod(methodName, parameterTypes);
		//1.3.3获取目标方法上的注解
		RequiredLog required = targetMethod.getDeclaredAnnotation(RequiredLog.class);
		/*String operation="operation";
		if(required!=null) {
			operation=required.value();
		}*///@Pointcut("bean(sysUserServiceImpl)")这样写
		//1.3.4获取RequiredLog注解中定义的value
		//用注解描述的方式@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
		String operation=required.value();
		//1.4获取目标方法信息（类全名+方法名）
		String method=targetCls.getName()+"."+methodName;
		//1.5获取方法执行时传入的实际参数
		//		String params=Arrays.toString(jp.getArgs());//转普通串
		String params=new ObjectMapper().writeValueAsString(jp.getArgs());//尽量转json格式
		//2.封装用户行为日志
		SysLog log=new SysLog();
		log.setIp(ip);
		log.setUsername(username);
		log.setMethod(method);//method 构成为目标方法所在类的类全名以及方法名
		log.setOperation(operation);
		log.setParams(params);//访问方法时传递的实际参数
		log.setTime(time);
		log.setCreatedTime(new Date());
		//3.保存用户行为日志
		//		  new Thread() {
		//			 public void run() {
		//				 sysLogService.saveObject(log);
		//			 };
		//		  }.start(); 并发量小的时候可以这样new几个有限的线程
		sysLogService.saveObject(log);
	}
}	
