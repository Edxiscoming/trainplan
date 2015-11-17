package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the PLAN_TRAIN database table.
 * 
 */
public class PlanTrainModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String planTrainId;

	private String appointDay;

	private String appointPeriod;

	private String appointWeek;

	private String baseChartId;

	private String baseTrainId;

	private String checkLev1Bureau;

	private Integer checkLev1Type;

	private String checkLev2Bureau;

	private Integer checkLev2Type;

	private String cmdBureau;

	private String cmdShortinfo;

	private String cmdTrainId;

	private Integer cmdTxtmlid;

	private Integer cmdTxtmlitemid;

	private Integer commonlineRule;

	private String creatPeople;

	private String creatPeopleOrg;

	private Date creatTime;

	private Integer creatType;

	private String dailyplanFlag;

	private String dailyplanId;

	private Date dailyplanTime;

	private Integer dailyplanTimes;

	private Integer dayGap;

	private String endBureau;

	private String endBureauFull;

	private String endStn;

	private String endStnDbm;

	private Date endTime;

	private Integer groupSerialNbr;

	private Integer highlineFlag;

	private Integer highlineRule;

	private String marshallingId;

	private String marshallingName;

	private String nextTrainId;

	private String note;

	private String passBureau;

	private String planCrossId;

	private String planTrainSign;

	private String preTrainId;

	private String routeId;

	private String runDate;

	private Integer spareApplyFlag;

	private Integer spareFlag;

	private String startBureau;

	private String startBureauFull;

	private String startStn;

	private String startStnDbm;

	private Date startTime;

	private String telBureau;

	private Integer telId;

	private String telNum;

	private String tokenPsgBureau;

	private String tokenVehBureau;

	private Integer trainCapacity;

	private String trainNbr;

	private Integer trainScope;

	private Integer trainSort;

	private String trainTypeId;
	
	private String relevantBureau;
	
	private String createReason;
	
    /**
     * plantrain对应的逻辑交路车次id
     */
    private String cmTrainId;
    
    public PlanTrainModel() {
    }

	public String getPlanTrainId() {
		return this.planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
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

	public String getBaseTrainId() {
		return this.baseTrainId;
	}

	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}

	public String getCheckLev1Bureau() {
		return this.checkLev1Bureau;
	}

	public void setCheckLev1Bureau(String checkLev1Bureau) {
		this.checkLev1Bureau = checkLev1Bureau;
	}

	public Integer getCheckLev1Type() {
		return this.checkLev1Type;
	}

	public void setCheckLev1Type(Integer checkLev1Type) {
		this.checkLev1Type = checkLev1Type;
	}

	public String getCheckLev2Bureau() {
		return this.checkLev2Bureau;
	}

	public void setCheckLev2Bureau(String checkLev2Bureau) {
		this.checkLev2Bureau = checkLev2Bureau;
	}

	public Integer getCheckLev2Type() {
		return this.checkLev2Type;
	}

	public void setCheckLev2Type(Integer checkLev2Type) {
		this.checkLev2Type = checkLev2Type;
	}

	public String getCmdBureau() {
		return this.cmdBureau;
	}

	public void setCmdBureau(String cmdBureau) {
		this.cmdBureau = cmdBureau;
	}

	public String getCmdShortinfo() {
		return this.cmdShortinfo;
	}

	public void setCmdShortinfo(String cmdShortinfo) {
		this.cmdShortinfo = cmdShortinfo;
	}

	public String getCmdTrainId() {
		return this.cmdTrainId;
	}

	public void setCmdTrainId(String cmdTrainId) {
		this.cmdTrainId = cmdTrainId;
	}

	public Integer getCmdTxtmlid() {
		return this.cmdTxtmlid;
	}

	public void setCmdTxtmlid(Integer cmdTxtmlid) {
		this.cmdTxtmlid = cmdTxtmlid;
	}

	public Integer getCmdTxtmlitemid() {
		return this.cmdTxtmlitemid;
	}

	public void setCmdTxtmlitemid(Integer cmdTxtmlitemid) {
		this.cmdTxtmlitemid = cmdTxtmlitemid;
	}

	public Integer getCommonlineRule() {
		return this.commonlineRule;
	}

	public void setCommonlineRule(Integer commonlineRule) {
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

	public Integer getCreatType() {
		return this.creatType;
	}

	public void setCreatType(Integer creatType) {
		this.creatType = creatType;
	}

	public String getDailyplanFlag() {
		return this.dailyplanFlag;
	}

	public void setDailyplanFlag(String dailyplanFlag) {
		this.dailyplanFlag = dailyplanFlag;
	}

	public String getDailyplanId() {
		return this.dailyplanId;
	}

	public void setDailyplanId(String dailyplanId) {
		this.dailyplanId = dailyplanId;
	}

	public Date getDailyplanTime() {
		return this.dailyplanTime;
	}

	public void setDailyplanTime(Date dailyplanTime) {
		this.dailyplanTime = dailyplanTime;
	}

	public Integer getDailyplanTimes() {
		return this.dailyplanTimes;
	}

	public void setDailyplanTimes(Integer dailyplanTimes) {
		this.dailyplanTimes = dailyplanTimes;
	}

	public Integer getDayGap() {
		return this.dayGap;
	}

	public void setDayGap(Integer dayGap) {
		this.dayGap = dayGap;
	}

	public String getEndBureau() {
		return this.endBureau;
	}

	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}

	public String getEndBureauFull() {
		return this.endBureauFull;
	}

	public void setEndBureauFull(String endBureauFull) {
		this.endBureauFull = endBureauFull;
	}

	public String getEndStn() {
		return this.endStn;
	}

	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}

	public String getEndStnDbm() {
		return this.endStnDbm;
	}

	public void setEndStnDbm(String endStnDbm) {
		this.endStnDbm = endStnDbm;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getGroupSerialNbr() {
		return this.groupSerialNbr;
	}

	public void setGroupSerialNbr(Integer groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}

	public Integer getHighlineFlag() {
		return this.highlineFlag;
	}

	public void setHighlineFlag(Integer highlineFlag) {
		this.highlineFlag = highlineFlag;
	}

	public Integer getHighlineRule() {
		return this.highlineRule;
	}

	public void setHighlineRule(Integer highlineRule) {
		this.highlineRule = highlineRule;
	}

	public String getMarshallingId() {
		return this.marshallingId;
	}

	public void setMarshallingId(String marshallingId) {
		this.marshallingId = marshallingId;
	}

	public String getMarshallingName() {
		return this.marshallingName;
	}

	public void setMarshallingName(String marshallingName) {
		this.marshallingName = marshallingName;
	}

	public String getNextTrainId() {
		return this.nextTrainId;
	}

	public void setNextTrainId(String nextTrainId) {
		this.nextTrainId = nextTrainId;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPassBureau() {
		return this.passBureau;
	}

	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}

	public String getPlanCrossId() {
		return this.planCrossId;
	}

	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}

	public String getPlanTrainSign() {
		return this.planTrainSign;
	}

	public void setPlanTrainSign(String planTrainSign) {
		this.planTrainSign = planTrainSign;
	}

	public String getPreTrainId() {
		return this.preTrainId;
	}

	public void setPreTrainId(String preTrainId) {
		this.preTrainId = preTrainId;
	}

	public String getRouteId() {
		return this.routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getRunDate() {
		return this.runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public Integer getSpareApplyFlag() {
		return this.spareApplyFlag;
	}

	public void setSpareApplyFlag(Integer spareApplyFlag) {
		this.spareApplyFlag = spareApplyFlag;
	}

	public Integer getSpareFlag() {
		return this.spareFlag;
	}

	public void setSpareFlag(Integer spareFlag) {
		this.spareFlag = spareFlag;
	}

	public String getStartBureau() {
		return this.startBureau;
	}

	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}

	public String getStartBureauFull() {
		return this.startBureauFull;
	}

	public void setStartBureauFull(String startBureauFull) {
		this.startBureauFull = startBureauFull;
	}

	public String getStartStn() {
		return this.startStn;
	}

	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}

	public String getStartStnDbm() {
		return this.startStnDbm;
	}

	public void setStartStnDbm(String startStnDbm) {
		this.startStnDbm = startStnDbm;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTelBureau() {
		return this.telBureau;
	}

	public void setTelBureau(String telBureau) {
		this.telBureau = telBureau;
	}

	public Integer getTelId() {
		return this.telId;
	}

	public void setTelId(Integer telId) {
		this.telId = telId;
	}

	public String getTelNum() {
		return this.telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public String getTokenPsgBureau() {
		return this.tokenPsgBureau;
	}

	public void setTokenPsgBureau(String tokenPsgBureau) {
		this.tokenPsgBureau = tokenPsgBureau;
	}

	public String getTokenVehBureau() {
		return this.tokenVehBureau;
	}

	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}

	public Integer getTrainCapacity() {
		return this.trainCapacity;
	}

	public void setTrainCapacity(Integer trainCapacity) {
		this.trainCapacity = trainCapacity;
	}

	public String getTrainNbr() {
		return this.trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}

	public Integer getTrainScope() {
		return this.trainScope;
	}

	public void setTrainScope(Integer trainScope) {
		this.trainScope = trainScope;
	}

	public Integer getTrainSort() {
		return this.trainSort;
	}

	public void setTrainSort(Integer trainSort) {
		this.trainSort = trainSort;
	}

	public String getTrainTypeId() {
		return this.trainTypeId;
	}

	public void setTrainTypeId(String trainTypeId) {
		this.trainTypeId = trainTypeId;
	}

	public String getRelevantBureau() {
		return relevantBureau;
	}

	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}

	public String getCreateReason() {
		return createReason;
	}

	public void setCreateReason(String createReason) {
		this.createReason = createReason;
	}

	public String getCmTrainId() {
		return cmTrainId;
	}

	public void setCmTrainId(String cmTrainId) {
		this.cmTrainId = cmTrainId;
	}

}