package com.cy.pj.sys.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.SysLog;

@SpringBootTest
public class SysLogServiceTests {
	@Autowired
	private SysLogService sysLogService;
	@Test
	public void testFindPageObjects() {
	   PageObject<SysLog> pageObject=
	   sysLogService.findPageObjects("admin", 1);
	   System.out.println(pageObject);
	}
	
	@Test
	void testDeleteObjects() {
		int rows = sysLogService.deleteObjects(15,16,17);
		System.out.println("rows="+rows);
	}
	
	
	
}

