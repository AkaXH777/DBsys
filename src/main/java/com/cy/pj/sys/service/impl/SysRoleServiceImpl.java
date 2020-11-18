package com.cy.pj.sys.service.impl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.pojo.CheckBox;
import com.cy.pj.sys.pojo.SysRole;
import com.cy.pj.sys.pojo.SysRoleMenu;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.service.SysRoleService;
@Service
public class SysRoleServiceImpl implements SysRoleService{
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		//1.参数校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码值无效");
		//2.查询总记录数并校验
		int rowCount=sysRoleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("没有找到对应记录");
		//3.查询当前页记录
		int pageSize=2;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysRole> records = sysRoleDao.findPageObjects(name, startIndex, pageSize);
		//4.封装查询结果并返回
		return new PageObject<>(pageCurrent,pageSize,rowCount,records);
	}
	@RequiresPermissions("sys:role:add")
	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不允许为空");
		if(menuIds==null||menuIds.length==0)
			throw new ServiceException("必须为角色分配权限");
		//2.保存角色自身信息
		int rows=sysRoleDao.insertObject(entity);
		//3.保存角色菜单关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		return rows;
	}
	@RequiresPermissions("sys:role:update")
	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不允许为空");
		if(menuIds==null||menuIds.length==0)
			throw new ServiceException("必须为角色分配权限");
		//2.保存角色自身信息
		int rows=sysRoleDao.updateObject(entity);
		//3.保存角色菜单关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(entity.getId());
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		return rows;
	}
	@Override
	public SysRoleMenu findObjectById(Integer id) {
		//1.合法性验证
		if(id==null||id<1)
			throw new IllegalArgumentException("id值无效");
		//2.基于角色id查询角色自身信息
		SysRoleMenu result=sysRoleDao.findObjectById(id);
		//3.基于角色id查询角色对应的菜单id并进行封装
		//		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleId(id);
		//		result.setMenuIds(menuIds);
		return result;

	}
	@Override
	public List<CheckBox> findRoles() {
		// TODO Auto-generated method stub
		return sysRoleDao.findRoles();
	}
	@RequiresPermissions("sys:role:delete")
	@Override
	public int deleteObject(Integer id) {
		int rows = sysRoleDao.deleteObject(id);
		return rows;
	}

	
}
