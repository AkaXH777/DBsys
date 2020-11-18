package com.cy.pj.common.pojo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
/**基于此对象封装分页信息*/
@Data
public class PageObject<T> implements Serializable {//类泛型
	private static final long serialVersionUID = 
			6426099106948270347L;
	/**当前页的页码值*/
	private Integer pageCurrent=1;
	/**页面大小*/ 
	private Integer pageSize=3;
	/**总行数（通过查询获得）*/
	private Integer rowCount=0;
	/**总页数（通过计算获得）*/
	private Integer pageCount=0;
	/**当前页记录*/
	private List<T> records;
	
	public PageObject() {}
	public PageObject(Integer pageCurrent, Integer pageSize, Integer rowCount, List<T> records) {
		super();
		this.pageCurrent = pageCurrent;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.records = records;
		//1
		this.pageCount = (rowCount-1)/pageSize+1;
		//2
//		this.pageCount=rowCount/pageSize;
//		if(rowCount%pageSize!=0)pageCount++;
	}
	
	
}
