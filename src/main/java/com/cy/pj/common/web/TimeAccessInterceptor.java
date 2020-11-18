package com.cy.pj.common.web;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cy.pj.common.exception.ServiceException;
/**
 * 	在spring框架所有实现了HandlerInterceptor的对象都是spring mvc中拦截器对象
 */
public class TimeAccessInterceptor implements HandlerInterceptor {
	/**	此方法在@Controller描述的对象方法指向之前
	 * @return true 表示请求放行，false表示请求到此结束	*/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("==preHandle==");
		//获取当前时间（LocalDateTime为jdk8中的一个日历对象）
		LocalDateTime now = LocalDateTime.now();
		//获取当前时间对应的小时
		int hour = now.getHour();
		if(hour<=6||hour>=23)
			throw new ServiceException("请在6:00~23:00之间访问");
		return true;//true表示要执行后续拦截器方法或者目标@Controller对象方法
	}
	// 控制层@Controller方法执行结束后执行	
	@Override 
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("==postHandle==");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("==afterCompletion==");
	}
	
}
