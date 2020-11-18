package com.cy.pj.common.config;

import java.util.LinkedHashMap;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 	@Configuration 注解描述的类为spring容器中的配置类，
 * 	此类的实例也会交给spring管理
 */
@Configuration
public class SpringShiroConfig {

	/**
	 * 	SecurityManager是Shiro框架的核心，用于实现认证，授权等功能
	 * 	Spring容器中整合三方Bean对象时，可以将其Bean对象的创建放在一个方法中，
	 * 	然后使用@Bean注解进行描述，spring容器管理bean对象时就会将方法名作为key
	 * 	对bean对象进行存储
	 */
	//@Bean("aaa") 可以起名字，不写默认为@Bean注解描述的方法名
	@Bean	//功能类似@Component,@Service...
	public SecurityManager securityManager(Realm realm,CacheManager cacheManager,
			RememberMeManager rememberMeManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager sManager=
				new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		sManager.setCacheManager(cacheManager);
		sManager.setRememberMeManager(rememberMeManager);//记住我
		sManager.setSessionManager(sessionManager);
		return sManager;
	}
	
	/**
	 * 	配置过滤规则，例如对哪些请求进行认证，哪些请求可以直接放行
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager) {
		//构建ShiroFilterFactoryBean对象，此对象负责创建过滤器工厂
		ShiroFilterFactoryBean sfBean=new ShiroFilterFactoryBean();
		//关联SecurityManager（要借助此对象对url进行认证分析）
		sfBean.setSecurityManager(securityManager);
		//设置默认认证页面
		sfBean.setLoginUrl("/doLoginUI");
		
		//设置过滤规则
		LinkedHashMap<String,String> map=new LinkedHashMap<>();
		//静态资源允许匿名访问:"anon"为shiro的一个固定值，这个值对应着相关的过滤器
		map.put("/bower_components/**", "anon");
		map.put("/build/**", "anon");
		map.put("/dist/**", "anon");
		map.put("/plugins/**", "anon");
		map.put("/user/doLogin", "anon");
		map.put("/doLogout", "logout");//logout对应着shiro中的一个默认filter
//		map.put("/doIndexUI", "anon");//放行首页
		//除了匿名访问的资源，其它都要认证（"authc"）后访问
		map.put("/**", "user");//记住我(user标识认证可从cookie取信息来实现)
		sfBean.setFilterChainDefinitionMap(map);
		return sfBean;
	}
	
	/**这个Advisor会告诉spring框架谁是切入点方法，这个切入点方法执行时要做权限检查和授权	*/
	@Bean
	public AuthorizationAttributeSourceAdvisor 
	authorizationAttributeSourceAdvisor (
		    		    SecurityManager securityManager) {
			        AuthorizationAttributeSourceAdvisor advisor=
					new AuthorizationAttributeSourceAdvisor();
	advisor.setSecurityManager(securityManager);
		return advisor;
	}
	/**	配置shiro框架中的CacheManager对象，用于缓存用户权限	*/
	@Bean
	public CacheManager shiroCacheManager() {
		return new MemoryConstrainedCacheManager();
	}
	/**	配置记住我管理器对象	*/
	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager cManager=
				new CookieRememberMeManager();
		SimpleCookie cookie=new SimpleCookie("rememberMe");
		cookie.setMaxAge(7*24*60*60);
		cManager.setCookie(cookie);
		return cManager;
	}
	/**会话管理的配置*/
	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sManager=
				new DefaultWebSessionManager();
		//禁止url重写操作
		sManager.setSessionIdUrlRewritingEnabled(false);
		//配置session对象的有效时长(默认为30分钟)
		//这里的计时往往是从用户最后一次活动(会话)开始
		sManager.setGlobalSessionTimeout(60*60*1000);
		return sManager;
	}
}
