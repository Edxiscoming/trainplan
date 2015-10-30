package org.railway.com.trainplan.entity;

public class TrainLineSubInfoTime {

	private String stationType;
	private int stnSort;
	private String stnBureau;
	private String stnName;
	private String nodeName;
	private String arrTime;
	private String dptTime;
	private int stayTime;
	private String planTrainStnId;
	private String bureauShortName;
	
	
	public int getStayTime() {
		return stayTime;
	}
	public void setStayTime(int stayTime) {
		this.stayTime = stayTime;
	}
	public String getPlanTrainStnId() {
		return planTrainStnId;
	}
	public void setPlanTrainStnId(String planTrainStnId) {
		this.planTrainStnId = planTrainStnId;
	}
	
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
	public int getStnSort() {
		return stnSort;
	}
	public void setStnSort(int stnSort) {
		this.stnSort = stnSort;
	}
	public String getStnBureau() {
		return stnBureau;
	}
	public void setStnBureau(String stnBureau) {
		this.stnBureau = stnBureau;
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
	public String getBureauShortName() {
		return bureauShortName;
	}
	public void setBureauShortName(String bureauShortName) {
		this.bureauShortName = bureauShortName;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	
}
