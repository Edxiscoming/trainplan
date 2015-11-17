package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PLAN_CROSS database table.
 * 
 */
public class PlanCrossModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String planCrossId;

	private Integer airCondition;

	private String appointDay;

	private String appointPeriod;

	private String appointWeek;

	private String baseChartId;

	private String baseChartName;

	private String baseCrossId;

	private String checkPeople;

	private String checkPeopleOrg;

	private Date checkTime;

	private Integer checkType;

	private String commonlineRule;

	private String creatPeople;

	private String creatPeopleOrg;

	private Date creatTime;

	private String crhType;

	private String crossEndDate;

	private String crossName;

	private String crossSection;

	private String crossSpareName;

	private String crossStartDate;

	private Integer dejCollect;

	private Integer elecSupply;

	private Integer groupTotalNbr;

	private String highlineFlag;

	private String highlineRule;

	private String locoType;

	private String note;

	private String pairNbr;

	private String relevantBureau;

	private String spareFlag;

	private String startBureau;

	private String throughLine;

	private String tokenPsgBureau;

	private String tokenPsgDept;

	private String tokenVehBureau;

	private String tokenVehDepot;

	private String tokenVehDept;

	private String unitCrossId;

	private String vehicle1;

	private String vehicle2;
	
	// ����̨ID
	private String postId;
	// ����̨��
	private String postName;
	
    /**
     * plancross对应的逻辑交路id
     */
    private String cmCrossId;

	/**
	 * ��·�ƻ���Ӧ���г����мƻ�
	 */
	private List<PlanTrainModel> trainList;
	
    public PlanCrossModel() {
    }

	public String getPlanCrossId() {
		return this.planCrossId;
	}

	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}

	public Integer getAirCondition() {
		return this.airCondition;
	}

	public void setAirCondition(Integer airCondition) {
		this.airCondition = airCondition;
	}

	public String getAppointDay() {
		return this.appointDay;
	}

	public void setAppointDay(String appointDay) {
		this.appointDay = appointDay;
	}

	public String getAppointPeriod() {
		return this.appointPeriod;
	}

	public void setAppointPeriod(String appointPeriod) {
		this.appointPeriod = appointPeriod;
	}

	public String getAppointWeek() {
		return this.appointWeek;
	}

	public void setAppointWeek(String appointWeek) {
		this.appointWeek = appointWeek;
	}

	public String getBaseChartId() {
		return this.baseChartId;
	}

	public void setBaseChartId(String baseChartId) {
		this.baseChartId = baseChartId;
	}

	public String getBaseChartName() {
		return this.baseChartName;
	}

	public void setBaseChartName(String baseChartName) {
		this.baseChartName = baseChartName;
	}

	public String getBaseCrossId() {
		return this.baseCrossId;
	}

	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	}

	public String getCheckPeople() {
		return this.checkPeople;
	}

	public void setCheckPeople(String checkPeople) {
		this.checkPeople = checkPeople;
	}

	public String getCheckPeopleOrg() {
		return this.checkPeopleOrg;
	}

	public void setCheckPeopleOrg(String checkPeopleOrg) {
		this.checkPeopleOrg = checkPeopleOrg;
	}

	public Date getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Integer getCheckType() {
		return this.checkType;
	}

	public void setCheckType(Integer checkType) {
		this.checkType = checkType;
	}

	public String getCommonlineRule() {
		return this.commonlineRule;
	}

	public void setCommonlineRule(String commonlineRule) {
		this.commonlineRule = commonlineRule;
	}

	public String getCreatPeople() {
		return this.creatPeople;
	}

	public void setCreatPeople(String creatPeople) {
		this.creatPeople = creatPeople;
	}

	public String getCreatPeopleOrg() {
		return this.creatPeopleOrg;
	}

	public void setCreatPeopleOrg(String creatPeopleOrg) {
		this.creatPeopleOrg = creatPeopleOrg;
	}

	public Date getCreatTime() {
		return this.creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getCrhType() {
		return this.crhType;
	}

	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}

	public String getCrossEndDate() {
		return this.crossEndDate;
	}

	public void setCrossEndDate(String crossEndDate) {
		this.crossEndDate = crossEndDate;
	}

	public String getCrossName() {
		return this.crossName;
	}

	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}

	public String getCrossSection() {
		return this.crossSection;
	}

	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}

	public String getCrossSpareName() {
		return this.crossSpareName;
	}

	public void setCrossSpareName(String crossSpareName) {
		this.crossSpareName = crossSpareName;
	}

	public String getCrossStartDate() {
		return this.crossStartDate;
	}

	public void setCrossStartDate(String crossStartDate) {
		this.crossStartDate = crossStartDate;
	}

	public Integer getDejCollect() {
		return this.dejCollect;
	}

	public void setDejCollect(Integer dejCollect) {
		this.dejCollect = dejCollect;
	}

	public Integer getElecSupply() {
		return this.elecSupply;
	}

	public void setElecSupply(Integer elecSupply) {
		this.elecSupply = elecSupply;
	}

	public Integer getGroupTotalNbr() {
		return this.groupTotalNbr;
	}

	public void setGroupTotalNbr(Integer groupTotalNbr) {
		this.groupTotalNbr = groupTotalNbr;
	}

	public String getHighlineFlag() {
		return this.highlineFlag;
	}

	public void setHighlineFlag(String highlineFlag) {
		this.highlineFlag = highlineFlag;
	}

	public String getHighlineRule() {
		return this.highlineRule;
	}

	public void setHighlineRule(String highlineRule) {
		this.highlineRule = highlineRule;
	}

	public String getLocoType() {
		return this.locoType;
	}

	public void setLocoType(String locoType) {
		this.locoType = locoType;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPairNbr() {
		return this.pairNbr;
	}

	public void setPairNbr(String pairNbr) {
		this.pairNbr = pairNbr;
	}

	public String getRelevantBureau() {
		return this.relevantBureau;
	}

	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}

	public String getSpareFlag() {
		return this.spareFlag;
	}

	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}

	public String getStartBureau() {
		return this.startBureau;
	}

	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}

	public String getThroughLine() {
		return this.throughLine;
	}

	public void setThroughLine(String throughLine) {
		this.throughLine = throughLine;
	}

	public String getTokenPsgBureau() {
		return this.tokenPsgBureau;
	}

	public void setTokenPsgBureau(String tokenPsgBureau) {
		this.tokenPsgBureau = tokenPsgBureau;
	}

	public String getTokenPsgDept() {
		return this.tokenPsgDept;
	}

	public void setTokenPsgDept(String tokenPsgDept) {
		this.tokenPsgDept = tokenPsgDept;
	}

	public String getTokenVehBureau() {
		return this.tokenVehBureau;
	}

	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}

	public String getTokenVehDepot() {
		return this.tokenVehDepot;
	}

	public void setTokenVehDepot(String tokenVehDepot) {
		this.tokenVehDepot = tokenVehDepot;
	}

	public String getTokenVehDept() {
		return this.tokenVehDept;
	}

	public void setTokenVehDept(String tokenVehDept) {
		this.tokenVehDept = tokenVehDept;
	}

	public String getUnitCrossId() {
		return this.unitCrossId;
	}

	public void setUnitCrossId(String unitCrossId) {
		this.unitCrossId = unitCrossId;
	}

	public String getVehicle1() {
		return this.vehicle1;
	}

	public void setVehicle1(String vehicle1) {
		this.vehicle1 = vehicle1;
	}

	public String getVehicle2() {
		return this.vehicle2;
	}

	public void setVehicle2(String vehicle2) {
		this.vehicle2 = vehicle2;
	}

	public List<PlanTrainModel> getTrainList() {
		return trainList;
	}

	public void setTrainList(List<PlanTrainModel> trainList) {
		this.trainList = trainList;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getCmCrossId() {
		return cmCrossId;
	}

	public void setCmCrossId(String cmCrossId) {
		this.cmCrossId = cmCrossId;
	}

}