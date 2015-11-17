package org.railway.com.trainplan.entity;

import java.util.Date;
import java.util.UUID;

/**
 * 对数表原始信息
 * @author heyy
 *
 */
public class OriginalCross {
	//对象id
	private String originalCrossId = UUID.randomUUID().toString() ;
	//高线开行规律
	private String highlineRule;
	//普线开行规律
	private String commonlineRule;
	//是否截断原交路
	private int cutOld;
	//组数
	private int groupTotalNbr;
	//供电
	private int elecSupply;
	//集便
	private int dejCollect;
	//空调
	private int airCondition;
	//客运担当局
	private String tokenPsgBureau;
	//车辆担当局
	private String tokenVehBureau;
	//对数
	private String pairNbr;
	//交替日期
	private String alterNateDate;
	//交易原车次
	private String alterNateTranNbr;
	//交路名
	private String crossName;
	//备用套跑交路名
	private String crossSpareName;
	//高线标记
	private String highlineFlag; 
	//经由铁路线
	private String throughline;
	//指定星期
	private String appointWeek;
	//指定日期
	private String appointDay;
	//创建人
	private String createPeople; 
	//创建人单位
	private String createPeopleOrg; 
	//运行区段
	private String crossSection;
	//审核人
	private String checkPeople; 
	//审核时间
	private String checkTime; 
	//是否审核
	private String checkFlag;
	//指定周期
	private String appointPeriod;
	//审核人单位
	private String checkPeopleOrg;
	//动车组车型
	private String crhType;
	//备注
	private String note;
	//担当车辆段/动车段
	private String tokenVehDept;
	//担当动车所
	private String tokenVehDepot;
	//客运担当单位
	private String tokenPsgDept;
	//备用及停运标记
	private String spareFlag;
	//机车类型
	private String locoType;
	//创建时间
	private String createTime ;
	//基本方案id
	private String chartId;
	//基本方案名
	private String chartName;
	
	
	/**
	 * 根据9月10号提供的新对数表格式新增的字段
	 * date 2015/09/11  10:16
	 * heyy
	 * @return
	 */
	
	//生成交路时间
	private String creatCrossTime;	
	//交路等级
	private String crossLevel;
	//运行距离	
	private int runRange;
	//编组辆数
	private int marshallingNums;
	//定员
	private int peopleNums;
	//编组内容
	private String marshallingContent;
	//相关局
	private String relevantBureau;
	
	//为了匹配前端js
	private String crossId;
	
	private String createCrossFlag;
	
	//为了匹配前端js
	private String unitCrossId;
	
	public String getCreatCrossTime() {
		return creatCrossTime;
	}
	public void setCreatCrossTime(String creatCrossTime) {
		this.creatCrossTime = creatCrossTime;
	}
	public String getCrossLevel() {
		return crossLevel;
	}
	public void setCrossLevel(String crossLevel) {
		this.crossLevel = crossLevel;
	}
	public int getRunRange() {
		return runRange;
	}
	public void setRunRange(int runRange) {
		this.runRange = runRange;
	}
	public int getMarshallingNums() {
		return marshallingNums;
	}
	public void setMarshallingNums(int marshallingNums) {
		this.marshallingNums = marshallingNums;
	}
	public int getPeopleNums() {
		return peopleNums;
	}
	public void setPeopleNums(int peopleNums) {
		this.peopleNums = peopleNums;
	}
	public String getMarshallingContent() {
		return marshallingContent;
	}
	public void setMarshallingContent(String marshallingContent) {
		this.marshallingContent = marshallingContent;
	}

	public String getRelevantBureau() {
		return relevantBureau;
	}
	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}
	public String getOriginalCrossId() {
		return originalCrossId;
	}
	public void setOriginalCrossId(String originalCrossId) {
		this.originalCrossId = originalCrossId;
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
	public String getTokenPsgBureau() {
		return tokenPsgBureau;
	}
	public void setTokenPsgBureau(String tokenPsgBureau) {
		this.tokenPsgBureau = tokenPsgBureau;
	}
	public String getTokenVehBureau() {
		return tokenVehBureau;
	}
	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}
	public String getPairNbr() {
		return pairNbr;
	}
	public void setPairNbr(String pairNbr) {
		this.pairNbr = pairNbr;
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
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public String getCrossSpareName() {
		return crossSpareName;
	}
	public void setCrossSpareName(String crossSpareName) {
		this.crossSpareName = crossSpareName;
	}
	public String getHighlineFlag() {
		return highlineFlag;
	}
	public void setHighlineFlag(String highlineFlag) {
		this.highlineFlag = highlineFlag;
	}
	public String getThroughline() {
		return throughline;
	}
	public void setThroughline(String throughline) {
		this.throughline = throughline;
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
	public String getCrossSection() {
		return crossSection;
	}
	public void setCrossSection(String crossSection) {
		this.crossSection = crossSection;
	}
	public String getCheckPeople() {
		return checkPeople;
	}
	public void setCheckPeople(String checkPeople) {
		this.checkPeople = checkPeople;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getAppointPeriod() {
		return appointPeriod;
	}
	public void setAppointPeriod(String appointPeriod) {
		this.appointPeriod = appointPeriod;
	}
	public String getCheckPeopleOrg() {
		return checkPeopleOrg;
	}
	public void setCheckPeopleOrg(String checkPeopleOrg) {
		this.checkPeopleOrg = checkPeopleOrg;
	}
	public String getCrhType() {
		return crhType;
	}
	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
	public String getTokenPsgDept() {
		return tokenPsgDept;
	}
	public void setTokenPsgDept(String tokenPsgDept) {
		this.tokenPsgDept = tokenPsgDept;
	}
	public String getSpareFlag() {
		return spareFlag;
	}
	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}
	public String getLocoType() {
		return locoType;
	}
	public void setLocoType(String locoType) {
		this.locoType = locoType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getCrossId() {
		return crossId;
	}
	public void setCrossId(String crossId) {
		this.crossId = crossId;
	}
	public String getCreateCrossFlag() {
		return createCrossFlag;
	}
	public void setCreateCrossFlag(String createCrossFlag) {
		this.createCrossFlag = createCrossFlag;
	}
	public String getUnitCrossId() {
		return unitCrossId;
	}
	public void setUnitCrossId(String unitCrossId) {
		this.unitCrossId = unitCrossId;
	}

}
