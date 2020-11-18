package com.cy.pj.common.pojo;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 	借助此对象封装控制层响应到客户端的数据，在这个对象中会为数据添加一个状态
 */
@Data
@NoArgsConstructor
public class JsonResult implements Serializable {
	private static final long serialVersionUID = -856924038217431339L;//SysResult/Result/R
	/**状态码*/
	private Integer state=1;//1表示SUCCESS,0表示ERROR
	/**状态信息*/
	private String message="ok";
	/**正确数据*/
	private Object data;
	
	public JsonResult(String message) {
		this.message = message;
	}
	public JsonResult(Object data) {
		this.data = data;
	}
	public JsonResult(Throwable e) {
		this.state=0;
		this.message=e.getMessage();
	}
	
	
	
}

