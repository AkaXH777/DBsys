package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.service.SysRoleService;

@RestController
@RequestMapping("/role/")
public class SysRoleController {
	@Autowired
	private SysRoleService sysRoleService;
	
	@RequestMapping("doFindRoles")
	public JsonResult doFindRoles() {
		return new JsonResult(sysRoleService.findRoles());
	}
	
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String name,Integer pageCurrent) {
		return new JsonResult(sysRoleService.findPageObjects(name, pageCurrent));
	}
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysRole entity,Integer[] menuIds) {
		sysRoleService.saveObject(entity, menuIds);
		return new JsonResult("save ok");
	}
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysRole entity,Integer[] menuIds) {
		sysRoleService.saveObject(entity, menuIds);
		return new JsonResult("update ok");
	}
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id){
		return new JsonResult(sysRoleService.findObjectById(id));
	}
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		sysRoleService.deleteObject(id);
		return new JsonResult("delete ok");
	}
	
	
}
