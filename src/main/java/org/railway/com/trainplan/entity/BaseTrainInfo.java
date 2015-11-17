package org.railway.com.trainplan.entity;

public class BaseTrainInfo {
	
	 private String baseTrainId;
	 private String  startBureau;
	 private String  startBureauShortName;
	 private String  startStn;
	 private String  endBureau;
	 private String  endBureanShortName;
	 private String  endStn;
	 private String  routeBureauShortNames;
	 private String startTime;
	 private String  endTime;
	 private int  runDays;
	 
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	public String getStartBureau() {
		return startBureau;
	}
	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}
	public String getStartBureauShortName() {
		return startBureauShortName;
	}
	public void setStartBureauShortName(String startBureauShortName) {
		this.startBureauShortName = startBureauShortName;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndBureau() {
		return endBureau;
	}
	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}
	public String getEndBureanShortName() {
		return endBureanShortName;
	}
	public void setEndBureanShortName(String endBureanShortName) {
		this.endBureanShortName = endBureanShortName;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	public String getRouteBureauShortNames() {
		return routeBureauShortNames;
	}
	public void setRouteBureauShortNames(String routeBureauShortNames) {
		this.routeBureauShortNames = routeBureauShortNames;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	 
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getRunDays() {
		return runDays;
	}
	public void setRunDays(int runDays) {
		this.runDays = runDays;
	} 

}
