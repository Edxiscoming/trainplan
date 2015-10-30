package org.railway.com.trainplan.entity;

import java.io.Serializable;


/**
 * 路局字典model
 * @author join
 *
 */
public class Ljzd implements Serializable {
/**
 * CZSJ	VARCHAR2(12 BYTE)
CZSM	VARCHAR2(1 BYTE)
SJSXSJ	VARCHAR2(12 BYTE)
LJDM	VARCHAR2(2 BYTE)
LJPYM	VARCHAR2(1 BYTE)
LJQC	VARCHAR2(30 BYTE)
LJJC	VARCHAR2(4 BYTE)
 */
	private static final long serialVersionUID = 1L;
	//路局序码
	private String ljdm ;
	//路局拼音码
	private String ljpym ;
	//路局全称
	private String  ljqc;
	//路局简称
	private String  ljjc;
	public String getLjdm() {
		return ljdm;
	}
	public void setLjdm(String ljdm) {
		this.ljdm = ljdm;
	}
	public String getLjpym() {
		return ljpym;
	}
	public void setLjpym(String ljpym) {
		this.ljpym = ljpym;
	}
	public String getLjqc() {
		return ljqc;
	}
	public void setLjqc(String ljqc) {
		this.ljqc = ljqc;
	}
	public String getLjjc() {
		return ljjc;
	}
	public void setLjjc(String ljjc) {
		this.ljjc = ljjc;
	}
	
	
	
}
