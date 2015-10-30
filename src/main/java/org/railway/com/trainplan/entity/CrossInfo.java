package org.railway.com.trainplan.entity;

import java.util.UUID;


public class CrossInfo {
	
	//BASE_CROSS_ID	BASE_CHART_ID	BASE_CHART_NAME	CROSS_START_DATE	CROSS_END_DATE	
	//CROSS_NAME	CROSS_SPARE_NAME	ALTERNATE_DATE	ALTERNATE_TRAIN_NBR	SPARE_FLAG	
	//CUT_OLD	GROUP_TOTAL_NBR	PAIR_NBR	HIGHLINE_FLAG	HIGHLINE_RULE	COMMONLINE_RULE	
	//APPOINT_WEEK	APPOINT_DAY	CROSS_SECTION	THROUGH_LINE	START_BUREAU	
	//TOKEN_VEH_BUREAU	TOKEN_VEH_DEPT	TOKEN_VEH_DEPOT	TOKEN_PSG_BUREAU	TOKEN_PSG_DEPT	
	//LOCO_TYPE	CRH_TYPE	ELEC_SUPPLY	DEJ_COLLECT	AIR_CONDITION	NOTE	CREAT_PEOPLE	
	//CREAT_PEOPLE_ORG	CREAT_TIME 
	//LOCO_TYPE	CRH_TYPE	ELEC_SUPPLY	DEJ_COLLECT	AIR_CONDITION	NOTE	CREAT_PEOPLE	
		//CREAT_PEOPLE_ORG	CREAT_TIME 
	private String crossId = UUID.randomUUID().toString() ;
	
	private String crossIdForExcel;
	
	private String highlineRule;
	private String commonlineRule;
	private int cutOld;
	private int groupTotalNbr;
	private int elecSupply;
	private int dejCollect;
	private int airCondition;
	private String tokenPsgBureau;
	private String tokenVehBureau;
	private String startBureau;
	private String pairNbr;
	private String alterNateDate;
	private String alterNateTranNbr;
	private String crossName;
	private String crossSpareName;
	private String highlineFlag = "0"; 
	private String throughline;
	private String appointWeek;
	private String appointDay;
	private String createPeople; 
	private String createPeopleOrg; 
	private String crossSection;
	private String crossStartDate;
	private String crossEndDate;
	private String checkPeople; 
	private String appointPeriod;
	private String checkPeopleOrg;
	private String crhType;
	private String note;
	private String tokenVehDept;
	private String tokenVehDepot;
	private String tokenPsgDept;
	private String tokenPsgDepot;
	private String spareFlag = "1";
	private String locoType;
	private String createTime ;
	private String chartId;
	private String chartName;
	private String baseCrossId;
	private String startStn;
	private String relevantBureau;
	private String createUnitTime;
	
	/****以下字段unit_cross表用到  ****/
	//unit_cross表主键
	private String unitCrossId;
	//编组名
	private String marshallingName;
	//组数序号,只有unit_cross表中有
	private int groupSerialNbr;
	
	private String planCrossId;
	
	
	public String getAppointPeriod() {
		return appointPeriod;
	}
	public void setAppointPeriod(String appointPeriod) {
		this.appointPeriod = appointPeriod;
	}
	 
	public String getRelevantBureau() {
		return relevantBureau;
	}
	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}
public String getCreateUnitTime() {
		return createUnitTime;
	}
	public void setCreateUnitTime(String createUnitTime) {
		this.createUnitTime = createUnitTime;
	}

	public String getPlanCrossId() {
		return planCrossId;
	}
	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}
	public String getBaseCrossId() {
		return baseCrossId;
	}
	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	}
	public String getCrossIdForExcel() {
		return crossIdForExcel;
	}
	public void setCrossIdForExcel(String crossIdForExcel) {
		
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	} 
	public String getCrossId() {
		return crossId;
	}
	public void setCrossId(String crossId) {
    	this.crossId = crossId;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public String getChartId() {
		return chartId;
	}
	public void setChartId(String chartId) {
		this.chartId = chartId;
	}
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public String getCrossStartDate() {
		return crossStartDate;
	}
	public void setCrossStartDate(String crossStartDate) {
		this.crossStartDate = crossStartDate;
	}
	public String getCrossEndDate() {
		return crossEndDate;
	}
	public void setCrossEndDate(String crossEndDate) {
		this.crossEndDate = crossEndDate;
	}
	public String getCrossSpareName() {
		return crossSpareName;
	}
	public void setCrossSpareName(String crossSpareName) {
		this.crossSpareName = crossSpareName;
	}
	public String getAlterNateDate() {
		return alterNateDate;
	}
	public void setAlterNateDate(String alterNateDate) {
		this.alterNateDate = alterNateDate;
	}
	public String getAlterNateTranNbr() {
		return alterNateTranNbr;
	}
	public void setAlterNateTranNbr(String alterNateTranNbr) {
		this.alterNateTranNbr = alterNateTranNbr;
	}
	public String getSpareFlag() {
		return spareFlag;
	}
	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}
	public int getCutOld() {
		return cutOld;
	}
	public void setCutOld(int cutOld) {
		this.cutOld = cutOld;
	}
	public int getGroupTotalNbr() {
		return groupTotalNbr;
	}
	public void setGroupTotalNbr(int groupTotalNbr) {
		this.groupTotalNbr = groupTotalNbr;
	}
	public String getPairNbr() {
		return pairNbr;
	}
	public void setPairNbr(String pairNbr) {
		this.pairNbr = pairNbr;
	}
	public String getHighlineFlag() {
		return highlineFlag;
	}
	public void setHighlineFlag(String highlineFlag) {
		this.highlineFlag = highlineFlag;
	}
	 
	public String getHighlineRule() {
		return highlineRule;
	}
	public void setHighlineRule(String highlineRule) {
		this.highlineRule = highlineRule;
	}
	public String getCommonlineRule() {
		return commonlineRule;
	}
	public void setCommonlineRule(String commonlineRule) {
		this.commonlineRule = commonlineRule;
	}
	public String getAppointWeek() {
		return appointWeek;
	}
	public void setAppointWeek(String appointWeek) {
		this.appointWeek = appointWeek;
	}
	public String getAppointDay() {
		return appointDay;
	}
	public void setAppointDay(String appointDay) {
		this.appointDay = appointDay;
	}
	public String getCrossSection() {
		return crossSection;
	}
	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}
	public String getThroughline() {
		return throughline;
	}
	public void setThroughline(String throughline) {
		this.throughline = throughline;
	}
	public String getStartBureau() {
		return startBureau;
	}
	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}
	public String getTokenVehBureau() {
		return tokenVehBureau;
	}
	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}
	public String getTokenVehDept() {
		return tokenVehDept;
	}
	public void setTokenVehDept(String tokenVehDept) {
		this.tokenVehDept = tokenVehDept;
	}
	public String getTokenVehDepot() {
		return tokenVehDepot;
	}
	public void setTokenVehDepot(String tokenVehDepot) {
		this.tokenVehDepot = tokenVehDepot;
	}
	public String getTokenPsgBureau() {
		return tokenPsgBureau;
	}
	public void setTokenPsgBureau(String tokenPsgBureau) {
		this.tokenPsgBureau = tokenPsgBureau;
	}
	public String getTokenPsgDept() {
		return tokenPsgDept;
	}
	public void setTokenPsgDept(String tokenPsgDept) {
		this.tokenPsgDept = tokenPsgDept;
	}
	public String getTokenPsgDepot() {
		return tokenPsgDepot;
	}
	public void setTokenPsgDepot(String tokenPsgDepot) {
		this.tokenPsgDepot = tokenPsgDepot;
	}
	public String getLocoType() {
		return locoType;
	}
	public void setLocoType(String locoType) {
		this.locoType = locoType;
	}
	public String getCrhType() {
		return crhType;
	}
	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}
	public int getElecSupply() {
		return elecSupply;
	}
	public void setElecSupply(int elecSupply) {
		this.elecSupply = elecSupply;
	}
	public int getDejCollect() {
		return dejCollect;
	}
	public void setDejCollect(int dejCollect) {
		this.dejCollect = dejCollect;
	}
	public int getAirCondition() {
		return airCondition;
	}
	public void setAirCondition(int airCondition) {
		this.airCondition = airCondition;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCreatePeople() {
		return createPeople;
	}
	public void setCreatePeople(String createPeople) {
		this.createPeople = createPeople;
	}
	public String getCreatePeopleOrg() {
		return createPeopleOrg;
	}
	public void setCreatePeopleOrg(String createPeopleOrg) {
		this.createPeopleOrg = createPeopleOrg;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	 
	
	public String getCheckPeople() {
		return checkPeople;
	}
	public void setCheckPeople(String checkPeople) {
		this.checkPeople = checkPeople;
	}
	public String getCheckPeopleOrg() {
		return checkPeopleOrg;
	}
	public void setCheckPeopleOrg(String checkPeopleOrg) {
		this.checkPeopleOrg = checkPeopleOrg;
	}
	public String getUnitCrossId() {
		return unitCrossId;
	}
	public void setUnitCrossId(String unitCrossId) {
		this.unitCrossId = unitCrossId;
	}
	public String getMarshallingName() {
		return marshallingName;
	}
	public void setMarshallingName(String marshallingName) {
		this.marshallingName = marshallingName;
	}
	public int getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(int groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}
	public static void main(String args[]){
		//System.err.println(new Date());
		
	}
	 

}
