package org.railway.com.trainplan.entity;

import java.io.Serializable;

/**
 * 消息类型(经和超哥确定，消息类型表不会在页面里展现，修改数据只能通过数据库直接操作，并且只要这2个字段).
 * 
 * @author zhangPengDong
 *
 *         2015年6月5日 下午4:05:39
 */
public class MsgType implements Serializable {
	private static final long serialVersionUID = 4256504879521207814L;

	/**
	 * 消息类型.
	 */
	private String code;
	/**
	 * 消息类型.
	 */
	private String name;
	/**
	 * 备注.
	 */
	private String remark;
	/**
	 * 状态；0:可用1:不可用.
	 */
	private String status;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
