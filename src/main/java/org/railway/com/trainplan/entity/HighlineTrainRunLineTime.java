package org.railway.com.trainplan.entity;

public class HighlineTrainRunLineTime {

	
	private String planTrainStnId;
	private String stnSort;
	private String stnName;
	private String arrTime;
	private String dptTime;
	private String trainNbr;
	
	
	
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getPlanTrainStnId() {
		return planTrainStnId;
	}
	public void setPlanTrainStnId(String planTrainStnId) {
		this.planTrainStnId = planTrainStnId;
	}
	public String getStnSort() {
		return stnSort;
	}
	public void setStnSort(String stnSort) {
		this.stnSort = stnSort;
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
	
	
}
