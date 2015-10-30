package org.railway.com.trainplan.service.dto;

import java.io.Serializable;

public class PlanTrainStnDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String planTrainStnId;//列车经由车站ID（本表ID）
	private String stnName;//车站名
	private String arrTime;//到达时间 （格式：yyyy-mm-dd hh24:mi:ss）
	private String dptTime;//出发时间 （格式：yyyy-mm-dd hh24:mi:ss）
	private String arrTrainNbr;//到达车次
	private String dptTrainNbr;//出发车次
	private int stnSort;//站序
	private String stnBureau;//车站所属局简称
	private String isfdz;//是否为始发站、终到站   1：是  0：否
	private String isfjk;//是否为分界口    1：是  0：否
	
	
	public String getPlanTrainStnId() {
		return planTrainStnId;
	}
	public void setPlanTrainStnId(String planTrainStnId) {
		this.planTrainStnId = planTrainStnId;
	}
	public String getStnName() {
		return stnName;
	}
	public void setStnName(String stnName) {
		this.stnName = stnName;
	}
	public String getArrTime() {
		return arrTime;
	}
	public void setArrTime(String arrTime) {
		this.arrTime = arrTime;
	}
	public String getDptTime() {
		return dptTime;
	}
	public void setDptTime(String dptTime) {
		this.dptTime = dptTime;
	}
	public String getArrTrainNbr() {
		return arrTrainNbr;
	}
	public void setArrTrainNbr(String arrTrainNbr) {
		this.arrTrainNbr = arrTrainNbr;
	}
	public String getDptTrainNbr() {
		return dptTrainNbr;
	}
	public void setDptTrainNbr(String dptTrainNbr) {
		this.dptTrainNbr = dptTrainNbr;
	}
	public String getStnBureau() {
		return stnBureau;
	}
	public void setStnBureau(String stnBureau) {
		this.stnBureau = stnBureau;
	}
	public String getIsfdz() {
		return isfdz;
	}
	public void setIsfdz(String isfdz) {
		this.isfdz = isfdz;
	}
	public String getIsfjk() {
		return isfjk;
	}
	public void setIsfjk(String isfjk) {
		this.isfjk = isfjk;
	}
	public int getStnSort() {
		return stnSort;
	}
	public void setStnSort(int stnSort) {
		this.stnSort = stnSort;
	}
	
	
	
	
}
