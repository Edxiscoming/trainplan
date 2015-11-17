package org.railway.com.trainplan.entity;

import org.apache.commons.lang.StringUtils;

/**
 * 线台
 * 
 * @author chengc
 *
 */
public class DicRelaCrossPost {

	// id
	private Integer id;
	// 局代码
	private String bureau;
	// 交路名
	private String crossName;
	// 动车台名称（对应用户系统中的岗位名称）
	private String postName;
	// 动车所名称（初始数据源自BASE_CROSS，用户后期调整）
	private String depotName;
	// 交路与台关系类型（1：主管；2：辅管）
	private String relattonType;
	// 经由铁路线
	private String throughLine;
	// 备注
	private String note;

	private String post_id;

	/**
	 * 交路(始发车)加载日期相对开行日期提前加载天数(1:交路计划提前一天加载; 0:不变).
	 */
	private Integer loaddateOffset;
	
	/**
	 * 车辆担当局（局码）.
	 */
	private String tokenVehBureau;

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getBureau() {
		return bureau;
	}

	public void setBureau(String bureau) {
		this.bureau = bureau;
	}

	public String getCrossName() {
		return crossName;
	}

	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getRelattonType() {
		return relattonType;
	}

	public void setRelattonType(String relattonType) {
		this.relattonType = relattonType;
	}

	public String getThroughLine() {
		return throughLine;
	}

	public void setThroughLine(String throughLine) {
		this.throughLine = throughLine;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLoaddateOffset() {
		return loaddateOffset;
	}

	public void setLoaddateOffset(Integer loaddateOffset) {
		this.loaddateOffset = loaddateOffset;
	}

	public String getTokenVehBureau() {
		return tokenVehBureau;
	}

	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}

}
