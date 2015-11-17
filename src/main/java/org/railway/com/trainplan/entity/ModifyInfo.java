package org.railway.com.trainplan.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PlanTrainDTOForModify;

public class ModifyInfo {
	private String planModifyId;
	private String planCrossId;
	private String planTrainId;
	private String crossName;
	private String runDate;// yyyymmdd
	private String trainNbr;
	private String modifyType;// 0:调整时刻；1：调整经路；2：开行转停运；4：停运转开行；3：备用转开行；5：开行转备用；6：删除数据
	private String modifyReason;
	private String startDate;
	private String endDate;
	private String rule;
	private String selectedDate;// 择日日期
	private String modifyContent;
	private Date modifyTime;// yyyy-mm-dd hh24:mi:ss
	private String modifyPeople;// 调整人
	private String modifyPeopleOrg;// 所属单位
	private String modifyPeopleBureau;// 简称

	public ModifyInfo() {
		super();
	}

	public ModifyInfo(PlanTrainDTOForModify trainPlanDto,ShiroRealm.ShiroUser user,String stopType,Integer spareFlag) {
		this.planTrainId = trainPlanDto.getPlanTrainId();
		this.planCrossId = trainPlanDto.getPlanCrossId();
		this.crossName = trainPlanDto.getCrossName();
		this.runDate = trainPlanDto.getRunDate();
		this.trainNbr = trainPlanDto.getTrainNbr();
		this.modifyPeople = user.getName();
		this.modifyPeopleOrg = user.getBureauFullName();
		this.modifyPeopleBureau = user.getBureauShortName();
		if(StringUtils.equals(stopType, "1") || StringUtils.equals(stopType, "2")){
			String str = "";
			if(spareFlag == 9){
				str = "开行转停运";
				this.modifyType = "2";
			}else if(spareFlag == 1){
				str = "停运转开行";
				this.modifyType = "4";
			}
			this.modifyContent = trainPlanDto.getTrainNbr() + "车次在" + trainPlanDto.getRunDate() + str + "调整";
		}
		if(StringUtils.equals(stopType, "3") || StringUtils.equals(stopType, "4")){
			String str = "";
			if(spareFlag == 2){
				str = "开行转备用";
				this.modifyType = "5";
			}else if(spareFlag == 1){
				str = "备用转开行";
				this.modifyType = "3";
			}
			this.modifyContent = trainPlanDto.getTrainNbr() + "车次在" + trainPlanDto.getRunDate() + str + "调整";
		}
	}

	public ModifyInfo(PlanTrainDTOForModify trainPlanDto,ShiroRealm.ShiroUser user) {
		this.planTrainId = trainPlanDto.getPlanTrainId();
		this.planCrossId = trainPlanDto.getPlanCrossId();
		this.crossName = trainPlanDto.getCrossName();
		this.runDate = trainPlanDto.getRunDate();
		this.trainNbr = trainPlanDto.getTrainNbr();
		this.modifyPeople = user.getName();
		this.modifyPeopleOrg = user.getBureauFullName();
		this.modifyPeopleBureau = user.getBureauShortName();
				this.modifyType = "6";
			this.modifyContent = trainPlanDto.getTrainNbr() + "车次在" + trainPlanDto.getRunDate() + "被删除";
	}

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

	@Override
	public String toString() {
		return "ModifyInfo [planModifyId=" + planModifyId + ", planCrossId="
				+ planCrossId + ", planTrainId=" + planTrainId + ", crossName="
				+ crossName + ", runDate=" + runDate + ", trainNbr=" + trainNbr
				+ ", modifyType=" + modifyType + ", modifyReason="
				+ modifyReason + ", startDate=" + startDate + ", endDate="
				+ endDate + ", rule=" + rule + ", selectedDate=" + selectedDate
				+ ", modifyContent=" + modifyContent + ", modifyTime="
				+ modifyTime + ", modifyPeople=" + modifyPeople
				+ ", modifyPeopleOrg=" + modifyPeopleOrg
				+ ", modifyPeopleBureau=" + modifyPeopleBureau + "]";
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
