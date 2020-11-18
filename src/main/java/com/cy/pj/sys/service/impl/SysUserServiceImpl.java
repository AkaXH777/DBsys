package com.cy.pj.sys.service.impl;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.annotation.RequiredLog02;
import com.cy.pj.common.annotation.RequiredLog03;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.pojo.PageObject;
import com.cy.pj.sys.common.utils.AssertUtils;
import com.cy.pj.sys.common.utils.ShiroUtils;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.pojo.SysUser;
import com.cy.pj.sys.pojo.SysUserDept;
import com.cy.pj.sys.service.SysUserService;

/*@Transactional	描述类表示类中所有方法都要进行事务控制
	假如方法上也有这个注解，则方法上的事务注解特性优先级较高，
 * 1)readOnly属性
 * 	含义是什么？	是否为只读事务(只读事务只允许查询操作)	
 * 	默认值是什么？	(false)
 * 
 * 2)rollbackFor属性
 * 	含义是什么？（什么异常才回滚事务）	
 * 	默认值是什么？（RuntimeException与Error,但是检查异常（Exception.class）不回滚）
 * 
 * 3)norollbackFor属性
 * 	含义是什么？	什么情况下不回滚
 * 	默认值是什么？	没有默认值
 * 
 * 4)timeout属性
 * 	含义是什么？	是否支持事务超时
 * 	默认值是什么？	-1，表示不支持事务超时，可以给定时间
 */
@Service
@Transactional(readOnly = false,
			   rollbackFor = Throwable.class,
			   timeout = 60,
			   isolation = Isolation.READ_COMMITTED)
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@RequiresPermissions("sys:user:update1")
	@RequiredLog("修改用户")
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		AssertUtils.isArgValid(entity==null, "保存对象不能为空");
		AssertUtils.isArgValid(StringUtils.isEmpty(entity.getUsername()), "用户名不能为空");
		AssertUtils.isArgValid(roleIds==null||roleIds.length==0,"必须为其指定角色");
		//2.更新用户自身信息
		int rows=sysUserDao.updateObject(entity);
		AssertUtils.isServiceValid(rows==0, "该记录可能已经不存在");
		//3.保存用户与角色关系数据
		sysUserRoleDao.deleteObjectByUserId(entity.getId());
		sysUserRoleDao.insertObjects(entity.getId(),
				roleIds);
		return rows;
	}

	@RequiresPermissions("sys:user:insert")
	@Transactional
	@RequiredLog("添加用户")
	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new IllegalArgumentException("密码不能为空");
		if(roleIds==null || roleIds.length==0)
			throw new IllegalArgumentException("至少要为用户分配角色");
		//2.保存用户自身信息
		//2.1对密码进行加密
		String source=entity.getPassword();
		String salt=UUID.randomUUID().toString();
		SimpleHash sh=new SimpleHash(//Shiro框架
				"MD5",//algorithmName 算法
				source,//原密码
				salt, //盐值
				1);//hashIterations表示加密次数
		entity.setSalt(salt);
		entity.setPassword(sh.toHex());
		int rows=sysUserDao.insertObject(entity);
		//3.保存用户角色关系数据
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//4.返回结果
		return rows;
	}
	
	@Transactional(readOnly = true)
	@RequiredLog(value = "分页查询")
	@Override
	public PageObject<SysUserDept> findPageObjects(String username, Integer pageCurrent) {
		//1.参数有效性校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码值无效");
		//2.查询总记录数并校验
		int rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("没有找到对应记录");
		//3.查询当前页记录
		int pageSize=2;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDept> records=
				sysUserDao.findPageObjects(username, startIndex, pageSize);
		//4.封装查询结果并返回
		return new PageObject<>(pageCurrent, pageSize, rowCount, records);
	}
	
	@RequiresPermissions("sys:user:update2")
	@Transactional(rollbackFor = IllegalArgumentException.class)//由此注解描述的方法为一个事务切入点方法,后续运行时会对它描述的描述的方法进行事务控制
	@RequiredLog("禁用-启用")//此注解描述的方法为一个日志切入点方法
	@Override
	public int validById(Integer id, Integer valid) {
		//1.参数校验
		AssertUtils.isArgValid(id==null||id<1, "参数值无效");
		AssertUtils.isArgValid(valid!=0&&valid!=1, "状态值无效");
		//2.修改用户状态
		int rows=sysUserDao.validById(id, valid,"admin");//这里的admin先假设为登录用户
		AssertUtils.isServiceValid(rows==0, "记录可能已经不存在");
		return rows;
	}

	//===================重新写的,正确===============================
	@Transactional(readOnly = true) //所有的查询方法建议readOnly=true (性能会比较高)
	@Override
	public Map<String, Object> findObjectById(Integer userId) {
		if(userId==null||userId<=0)
			throw new IllegalArgumentException("参数值不合法");
		SysUserDept user = sysUserDao.findObjectById(userId);
		if(user==null)
			throw new ServiceException("此用户已经不存在");
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}

	@Override
	public int updatePassword(String password, String newPassword, String cfgPassword) {
//		//1.参数校验
//		AssertUtils.isArgValid(StringUtils.isEmpty(password), "原密码不能为空");
//		AssertUtils.isArgValid(StringUtils.isEmpty(newPassword), "新密码不能为空");
//		AssertUtils.isArgValid(!newPassword.equals(cfgPassword), "两次密码输入不相等");
//		//验证原密码是否正确
//		SysUser user = ShiroUtils.getUser();
//		String hashedPwd=user.getPassword();
//		SimpleHash sh=new SimpleHash("MD5", password, user.getSalt(), 1);
//		AssertUtils.isArgValid(hashedPwd.equals(sh.toHex()), "原密码输入不正确");
//		//2.修改密码
//		//2.1加密新密码
//		String newSalt = UUID.randomUUID().toString();
//		sh=new SimpleHash("MD5", newPassword, newSalt, 1);
//		String newHashedPwd=sh.toHex();
//		//2.2更新新密码
//		int rows = sysUserDao.updatePassword(newHashedPwd, newSalt, user.getId());
//		return rows;
		
		if(StringUtils.isEmpty(newPassword))
			throw new IllegalArgumentException("新密码不能为空");
		if(StringUtils.isEmpty(cfgPassword))
			throw new IllegalArgumentException("确认密码不能为空");
		if(!newPassword.equals(cfgPassword))
			throw new IllegalArgumentException("两次输入的密码不相等");
		if(StringUtils.isEmpty(password))
			throw new IllegalArgumentException("原密码不能为空");
		SysUser user = ShiroUtils.getUser();
		SimpleHash sh=new SimpleHash("MD5", password, user.getSalt(),1);
		if(!user.getPassword().equals(sh.toHex()))
			throw new IllegalArgumentException("原密码不正确");
		String salt=UUID.randomUUID().toString();
		sh=new SimpleHash("MD5", newPassword, salt, 1);
		int rows = sysUserDao.updatePassword(sh.toHex(), salt, user.getId());
		if(rows==0)
			throw new ServiceException("修改失败");
		return rows;
	}
	
	
	//====================正确========================
//	@Override
//	public Map<String, Object> findObjectById(Integer userId) {
//		//1.参数合法性验证
//		if(userId==null||userId<=0)
//			throw new IllegalArgumentException(
//					"参数数据不合法,userId="+userId);
//		//2.查询用户以及用户对应的部门信息
//		SysUserDept user=
//				sysUserDao.findObjectById(userId);
//		if(user==null)
//			throw new ServiceException("此用户已经不存在");
//		List<Integer> roleIds=
//				sysUserRoleDao.findRoleIdsByUserId(userId);
//		//3.数据封装
//		Map<String,Object> map=new HashMap<>();
//		map.put("user", user);
//		map.put("roleIds", roleIds);
//		return map;
//	}

	//====================错误========================
//	@Override
//	public Map<String, Object> findObjectById(Integer userId) {
//		AssertUtils.isArgValid(userId==null||userId<=0, "参数数据不合法");
////		if(userId==null||userId<=0)
////			throw new IllegalArgumentException("参数不合法");
//		SysUserDept user=
//				sysUserDao.findObjectById(userId);
//		AssertUtils.isServiceValid(user==null, "此用户已经不存在");
////		if(user==null)
////			throw new ServiceException("此用户已经不存在");
//		List<Integer> roleIds=
//				sysUserRoleDao.findRoleIdsByUserId(userId);
//		Map<String,Object> map=new HashMap<>();
//		map.put("uesr", user);
//		map.put("roleIds", roleIds);
//		return map;
//	}
	
	

}
