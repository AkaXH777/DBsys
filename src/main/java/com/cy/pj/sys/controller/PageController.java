package com.cy.pj.sys.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.sys.common.utils.ShiroUtils;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;
/**
 * 	基于此Controller处理所有页面请求
 */
@Controller
@RequestMapping("/")
public class PageController {
	@Autowired
	private SysMenuService sysMenuService;
	
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		//从shiro框架的session对象中取出登录用户信息
//		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		SysUser user=ShiroUtils.getUser();
		//将用户对象存储到model中
		model.addAttribute("user", user);
		
		model.addAttribute("username", user.getUsername());
		//获取用户有访问权限的菜单
		List<SysUserMenu> userMenus = sysMenuService.findUserMenusByUserId(user.getId());
		model.addAttribute("userMenus", userMenus);
		return "starter";
	}
	@RequestMapping("doLoginUI")
	public String doLoginUI() {
		return "login";
	}
	
	//==================日志模块=======================
//	@RequestMapping("log/log_list")
//	public String doLogUI() {
//		
//		return "sys/log_list";
//	}
	@RequestMapping("doPageUI")
	public String doPageUI() {
		
		return "common/page";
	}
	
	//===================菜单模块===============================
//	@RequestMapping("menu/menu_list")
//	public String doMenuUI() {
//		return "sys/menu_list";
//	}
//	@RequestMapping("menu/menu_edit")
//	public String doMenuEdit() {
//		return "sys/menu_edit";
//	}
//	@RequestMapping("role/role_list")
//	public String doRoleUI() {
//		return "sys/role_list";
//	}
	//rest风格的url
	@RequestMapping("{module}/{moduleUI}")
	public String doModuleUI(@PathVariable String moduleUI) {
		return "sys/"+moduleUI;
	}
	
	
	
	
}
