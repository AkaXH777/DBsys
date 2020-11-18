package com.cy.pj.sys.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.service.SysUserService;

@RestController
@RequestMapping("/user/")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("doLogin")
	public JsonResult doLogin(boolean isRememberMe,String username,String password) {
		System.out.println("sssssssssssss");
		//1.获取Subject对象
		UsernamePasswordToken token=
				new UsernamePasswordToken();
		token.setUsername(username);
		token.setPassword(password.toCharArray());
		if(isRememberMe) {
			token.setRememberMe(true);
		}
		Subject subject = SecurityUtils.getSubject();
		//提交账号信息给securityManager对象
		subject.login(token);
		return new JsonResult("login ok");
	}
	

	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysUser entity,Integer[] roleIds) {
		sysUserService.updateObject(entity, roleIds);
		return new JsonResult("update ok");
	}

	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		Map<String,Object> map=sysUserService.findObjectById(id);
		return new JsonResult(map);
	}
	//	@RequestMapping("doFindObjectById")
	//	public JsonResult doFindObjectById(
	//			Integer id){
	//		Map<String,Object> map=
	//				sysUserService.findObjectById(id);
	//		return new JsonResult(map);
	//	}


	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username,int pageCurrent) {
		return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
	}

	@RequestMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid) {
		sysUserService.validById(id, valid);
		return new JsonResult("update ok");
	}

	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysUser entity,Integer[]roleIds) {
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save ok");
	}
	
	@RequestMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(
	                         String pwd,
	                         String newPwd,
	                         String cfgPwd) {
	                 sysUserService.updatePassword(pwd, newPwd, cfgPwd);
	                 return new JsonResult("update ok");
	}
	
	
}
