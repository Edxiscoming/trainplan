package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应表plan_check
 * @author join
 *
 */
public class ModifyPlanDTO  implements Serializable{

	 private String planModifyId;
	 private String planCrossId;
	 private String planTrainId;
	 private String crossName;
	 private String runDate;//yyyymmdd
	 private String trainNbr;
	 private String modifyType;//0:调整时刻；1：调整经路；2：停运；3：启动备用
	 private String modifyReason;
	 private Date startDate;
	 private Date endDate;
	 private String rule;
	 private String selectedDate;//择日日期 
	 private String modifyContent;
	 private Date modifyTime;//yyyy-mm-dd hh24:mi:ss
	 private String modifyPeople;//调整人
	 private String modifyPeopleOrg;//所属单位
	 private String modifyPeopleBureau;//简称
	 
	public String getPlanModifyId() {
		return planModifyId;
	}
	public void setPlanModifyId(String planModifyId) {
		this.planModifyId = planModifyId;
	}
	public String getPlanCrossId() {
		return planCrossId;
	}
	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	public String getModifyReason() {
		return modifyReason;
	}
	public void setModifyReason(String modifyReason) {
		this.modifyReason = modifyReason;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}
	public String getModifyContent() {
		return modifyContent;
	}
	public void setModifyContent(String modifyContent) {
		this.modifyContent = modifyContent;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyPeople() {
		return modifyPeople;
	}
	public void setModifyPeople(String modifyPeople) {
		this.modifyPeople = modifyPeople;
	}
	public String getModifyPeopleOrg() {
		return modifyPeopleOrg;
	}
	public void setModifyPeopleOrg(String modifyPeopleOrg) {
		this.modifyPeopleOrg = modifyPeopleOrg;
	}
	public String getModifyPeopleBureau() {
		return modifyPeopleBureau;
	}
	public void setModifyPeopleBureau(String modifyPeopleBureau) {
		this.modifyPeopleBureau = modifyPeopleBureau;
	}
}
