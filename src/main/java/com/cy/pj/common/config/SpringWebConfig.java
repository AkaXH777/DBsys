package com.cy.pj.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cy.pj.common.web.TimeAccessInterceptor;
/**
 * 	Spring MVC 中的配置对象(此对象会在spring容器启动时初始化)
 */
@Configuration
public class SpringWebConfig implements WebMvcConfigurer {
	/**注册拦截器对象*/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册拦截器
		registry.addInterceptor(new TimeAccessInterceptor())
		//设置要拦截的路径
		.addPathPatterns("/user/doLogin");
	}
	
}
