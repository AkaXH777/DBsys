package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.pojo.JsonResult;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Controller
@RequestMapping("/log/")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(String username,Integer pageCurrent){
//		try {
			PageObject<SysLog> pageObject=
					sysLogService.findPageObjects(username,pageCurrent);
			return new JsonResult(pageObject);
//		} catch (Exception e) {
//			return new JsonResult(e);
//		}
	}
	
	@RequestMapping("doDeleteObjects")
	@ResponseBody
	public JsonResult doDeleteObjects(Integer... ids) {
		sysLogService.deleteObjects(ids);
		return new JsonResult("delete ok");
	}
	
	
	
}
