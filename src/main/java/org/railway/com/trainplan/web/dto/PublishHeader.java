package org.railway.com.trainplan.web.dto;

import java.io.Serializable;

public class PublishHeader implements Serializable {
	private String jhrq;//计划时间
	private String jhbc;//计划班次  0
	private String jdsk;//计划开始时间    00-23:00    18:00-18：00
	private String jdjs;//计划时间结束
	private int lcs;//列车数量
	private String sbry;//计划编制人姓名
	private String jsdw;//路局拼音码
	private String state;//标记第几次 落成
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getJhrq() {
		return jhrq;
	}
	public void setJhrq(String jhrq) {
		this.jhrq = jhrq;
	}
	public String getJhbc() {
		return jhbc;
	}
	public void setJhbc(String jhbc) {
		this.jhbc = jhbc;
	}
	public String getJdsk() {
		return jdsk;
	}
	public void setJdsk(String jdsk) {
		this.jdsk = jdsk;
	}
	public String getJdjs() {
		return jdjs;
	}
	public void setJdjs(String jdjs) {
		this.jdjs = jdjs;
	}
	public String getSbry() {
		return sbry;
	}
	public void setSbry(String sbry) {
		this.sbry = sbry;
	}
	public String getJsdw() {
		return jsdw;
	}
	public void setJsdw(String jsdw) {
		this.jsdw = jsdw;
	}
	public int getLcs() {
		return lcs;
	}
	public void setLcs(int lcs) {
		this.lcs = lcs;
	}
	
}
