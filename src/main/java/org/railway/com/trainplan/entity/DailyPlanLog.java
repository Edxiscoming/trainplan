package org.railway.com.trainplan.entity;

import java.util.Date;

public class DailyPlanLog {
	private String id;
	private String planTrainId;
	private String dailyPlanId;
	private String bureau;
	private String log;
	private String createTime;
	private String createPeople;
	private String requestId;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getDailyPlanId() {
		return dailyPlanId;
	}
	public void setDailyPlanId(String dailyPlanId) {
		this.dailyPlanId = dailyPlanId;
	}
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
//	public Date getCreateTime() {
//		return createTime;
//	}
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
	
	
	public String getCreatePeople() {
		return createPeople;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setCreatePeople(String createPeople) {
		this.createPeople = createPeople;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
