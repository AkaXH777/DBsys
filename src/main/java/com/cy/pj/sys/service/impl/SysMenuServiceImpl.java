package com.cy.pj.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysMenu;
import com.cy.pj.sys.pojo.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Override
	public List<Map<String, Object>> findObjects() {
		return sysMenuDao.findObjects();
	}
	@RequiresPermissions("sys:menu:delete")
	@Override
	public int deleteObject(Integer id) {
		//1.参数校验
		if(id==null||id<1)
			throw new IllegalArgumentException("请先选择");
		//2.查询子菜单个数
		int count=sysMenuDao.getChildCount(id);
		if(count>0)	
			throw new ServiceException("请先删除子菜单");
		//3.删除菜单信息
		//3.1删除关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//3.2删除自身信息
		int rows = sysMenuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("此记录可能已经不存在了");
		return rows;
	}
	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
	@RequiresPermissions("sys:menu:add")
	@Override
	public int saveObject(SysMenu entity) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("菜单名不能为空");
		//2.保存菜单信息
		int rows = sysMenuDao.insertObject(entity);
		return rows;
	}
	@RequiresPermissions("sys:menu:update")
	@Override
	public int updateObject(SysMenu entity) {
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("菜单名不能为空");
		int rows=sysMenuDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		return rows;
	}
	@Override
	public List<SysUserMenu> findUserMenusByUserId(Integer id) {
		//1.对用户id进行判断
		//2.基于用户id查找用户对应的角色id
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(id);
		//3.基于角色id获取角色对应的菜单信息，并进行封装
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
		//4.基于菜单id获取用户对应的菜单信息并返回
		return sysMenuDao.findMenusByIds(menuIds);
	}
	
}
