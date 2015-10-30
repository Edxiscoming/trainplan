package org.railway.com.trainplan.entity;

public class HighLineCrossTrainInfo {
    
	//高铁日历交路计划列车ID（本表ID）
	private String  highLineTrainId;
	//列车ID
	private String  planTrainId;
	//高铁日历交路计划ID（对应HIGHLINE_CROSS表中的HIGHLINE_CROSS_ID）
	private String  highLineCrossId;
	//列车序号
	private int trainSort;
	//车次
	private String trainNbr ;
	//开行日期（yyyymmdd）
	private String runDate ;
	 
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	 
	 
	public String getHighLineTrainId() {
		return highLineTrainId;
	}
	public void setHighLineTrainId(String highLineTrainId) {
		this.highLineTrainId = highLineTrainId;
	}
	public String getHighLineCrossId() {
		return highLineCrossId;
	}
	public void setHighLineCrossId(String highLineCrossId) {
		this.highLineCrossId = highLineCrossId;
	}
	public int getTrainSort() {
		return trainSort;
	}
	public void setTrainSort(int trainSort) {
		this.trainSort = trainSort;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	

}
