package com.cy.pj.common.pojo;

import java.io.Serializable;
/**基于此对象存储树节点数据*/

import lombok.Data;
@Data
public class Node implements Serializable {
	private static final long serialVersionUID = 485127885402380257L;
	private Integer id;
	private String name;
	private Integer parentId;
	
	
	
}
