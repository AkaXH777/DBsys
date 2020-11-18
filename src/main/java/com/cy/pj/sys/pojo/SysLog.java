package com.cy.pj.sys.pojo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/**
 * 	基于此对象封装从数据库查询到的日志信息：
 * 	类似对象的特性
 * 	1)添加set/get/toString/constructor
 * 	2)实现Serializable接口(建议所有用于封装数据的对象都实现此接口)
 * 	2.1)序列化:将对象转换为字节便于传输和存储
 * 	2.2)反序列化:将网络中的或存储介质中的字节转换为对象
 */
@Data
public class SysLog implements Serializable{
	private static final long serialVersionUID = -4279584376142204743L;
	private Integer id;
	private String username;//用户名
	private String operation;//用户操作
	private String method;//请求方法
	private String params;//请求参数
	private Long time;//执行时长
	private String ip;//IP地址
	private Date createdTime;//创建时间
}
