package com.cy.pj.common.web;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cy.pj.common.pojo.JsonResult;
/**
 * 	由此注解描述的类为一个控制层全局异常处理类，在此类中可以定义异常处理方法
 * 	基于这些异常处理方法对异常进行处理(包括异常的子类类型)
 */
//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice	//==@ControllerAdvice+@ResponseBody
public class GlobalExceptionHandler {

	@ExceptionHandler(ShiroException.class) 
	//    @ResponseBody
	public JsonResult doHandleShiroException(
			ShiroException e) {
		JsonResult r=new JsonResult();
		r.setState(0);
		if(e instanceof UnknownAccountException) {
			r.setMessage("账户不存在");
		}else if(e instanceof LockedAccountException) {
			r.setMessage("账户已被禁用");
		}else if(e instanceof IncorrectCredentialsException) {
			r.setMessage("密码不正确");
		}else if(e instanceof AuthorizationException) {
			r.setMessage("没有此操作权限");
		}else {
			r.setMessage("系统维护中");
		}
		e.printStackTrace();
		return r;
	}
	/**
	 * @ExceptionHandler 此注解描述的方法为一个异常处理方法，
	 * 	在注解内部定义的异常类型为此方法可以处理的异常类型
	 * @param e 用于接收出现的异常
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	//	@ResponseBody
	public JsonResult doHandleRuntimeException(RuntimeException e) {
		e.printStackTrace();//在后台打印异常
		return new JsonResult(e);//封装异常信息
	}



}
