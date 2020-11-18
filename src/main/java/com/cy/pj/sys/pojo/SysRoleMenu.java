package com.cy.pj.sys.pojo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
/**
 * 	 当基于角色id执行角色以及对应的菜单信息查询时，将查询到结果存储到此对象
 * 	并将此对象的数据最终更新到编辑页面上
 * 	查询方案 ：(现在数据来自两张表)
 *	1)业务层进行多次单边查询
 *	2)数据层做嵌套查询(数据层多次查询，基于角色表的查询结果再次查询角色菜单关系表)
 *	3)在数据层执行多表关联查询(join)
 */
@Data
public class SysRoleMenu implements Serializable {
	private static final long serialVersionUID = -3168711415616317653L;
	/**角色id*/
	private Integer id;
	/**角色名称*/
	private String name;
	/**角色备注*/
	private String note;
	/**角色对应的菜单id(这个数据存在角色和菜单关系表中)*/
	private List<Integer> menuIds;

	
	
	
}
