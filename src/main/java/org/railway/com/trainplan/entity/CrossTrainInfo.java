package org.railway.com.trainplan.entity;

import java.util.UUID;

public class CrossTrainInfo {

	private String crossTainId = UUID.randomUUID().toString();// BASE_CROSS_TRAIN_ID
	private String baseCrossTrainId;
	private String crossId;// BASE_CROSS_ID
	private int trainSort;// TRAIN_SORT
	private String baseTrainId;
	private String trainNbr;// TRAIN_NBR
	private String startStn;// START_STN
	private String startBureau;// START_BUREAU
	private String endStn;// END_STN
	private String endBureau;// END_BUREAU
	private int dayGap;// DAY_GAP
	private String alertNateTrainNbr;// ALTERNATE_TRAIN_NBR
	private String alertNateTime;// ALTERNATE_TIME
	private int spareFlag; // SPARE_FLAG
	private int spareApplyFlage;// SPARE_APPLY_FLAG
	private int highlineFlag = 0; // HIGHLINE_FLAG
	private int highlineRule = 1;// HIGHLINE_RULE
	private int commonLineRule;// COMMONLINE_RULE
	private String appointWeek;// APPOINT_WEEK
	private String appointDay;// APPOINT_DAY
	private String sourceTargetTime;
	private String runDate;
	private String endDate;
	private String targetTime;
	private int runDay;
	private String routeBureauShortNames;
	private String periodSourceTime; // 有效期起始时期
	private String periodTargetTime; // 有效期终止日期

	/**
	 * 列车序号（对数表中基本序号）.
	 */
	private Integer trainSortBase;

	public String getPeriodSourceTime() {
		return periodSourceTime;
	}

	public void setPeriodSourceTime(String periodSourceTime) {
		this.periodSourceTime = periodSourceTime;
	}

	public String getPeriodTargetTime() {
		return periodTargetTime;
	}

	public void setPeriodTargetTime(String periodTargetTime) {
		this.periodTargetTime = periodTargetTime;
	}

	/** 以下字段是表unit_cross_train中用到 **/
	// unit_cross_train表id
	private String unitCrossTrainId;
	// unit_cross表id
	private String unitCrossId;
	private String marshallingName;

	// 组数序号,只有unit_cross表中有
	private int groupSerialNbr;

	private int groupGap;

	public int getGroupGap() {
		return groupGap;
	}

	public void setGroupGap(int groupGap) {
		this.groupGap = groupGap;
	}

	public String getRouteBureauShortNames() {
		return routeBureauShortNames;
	}

	public void setRouteBureauShortNames(String routeBureauShortNames) {
		this.routeBureauShortNames = routeBureauShortNames;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getGroupSerialNbr() {
		return groupSerialNbr;
	}

	public void setGroupSerialNbr(int groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}

	public String getMarshallingName() {
		return marshallingName;
	}

	public void setMarshallingName(String marshallingName) {
		this.marshallingName = marshallingName;
	}

	public int getRunDay() {
		return runDay;
	}

	public void setRunDay(int runDay) {
		this.runDay = runDay;
	}

	public String getSourceTargetTime() {
		return sourceTargetTime;
	}

	public void setSourceTargetTime(String sourceTargetTime) {
		this.sourceTargetTime = sourceTargetTime;
	}

	public String getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}

	public String getCrossTainId() {
		return crossTainId;
	}

	public void setCrossTainId(String crossTainId) {
		this.crossTainId = crossTainId;
	}

	public String getCrossId() {
		return crossId;
	}

	public void setCrossId(String crossId) {
		this.crossId = crossId;
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

	public String getStartStn() {
		return startStn;
	}

	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}

	public String getStartBureau() {
		return startBureau;
	}

	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}

	public String getEndStn() {
		return endStn;
	}

	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}

	public String getEndBureau() {
		return endBureau;
	}

	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}

	public int getDayGap() {
		return dayGap;
	}

	public void setDayGap(int dayGap) {
		this.dayGap = dayGap;
	}

	public String getAlertNateTrainNbr() {
		return alertNateTrainNbr;
	}

	public void setAlertNateTrainNbr(String alertNateTrainNbr) {
		this.alertNateTrainNbr = alertNateTrainNbr;
	}

	public String getAlertNateTime() {
		return alertNateTime;
	}

	public void setAlertNateTime(String alertNateTime) {
		this.alertNateTime = alertNateTime;
	}

	public int getSpareFlag() {
		return spareFlag;
	}

	public void setSpareFlag(int spareFlag) {
		this.spareFlag = spareFlag;
	}

	public int getSpareApplyFlage() {
		return spareApplyFlage;
	}

	public void setSpareApplyFlage(int spareApplyFlage) {
		this.spareApplyFlage = spareApplyFlage;
	}

	public int getHighlineFlag() {
		return highlineFlag;
	}

	public void setHighlineFlag(int highlineFlag) {
		this.highlineFlag = highlineFlag;
	}

	public int getHighlineRule() {
		return highlineRule;
	}

	public void setHighlineRule(int highlineRule) {
		this.highlineRule = highlineRule;
	}

	public int getCommonLineRule() {
		return commonLineRule;
	}

	public void setCommonLineRule(int commonLineRule) {
		this.commonLineRule = commonLineRule;
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

	public String getBaseTrainId() {
		return baseTrainId;
	}

	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}

	public String getUnitCrossTrainId() {
		return unitCrossTrainId;
	}

	public void setUnitCrossTrainId(String unitCrossTrainId) {
		this.unitCrossTrainId = unitCrossTrainId;
	}

	public String getUnitCrossId() {
		return unitCrossId;
	}

	public void setUnitCrossId(String unitCrossId) {
		this.unitCrossId = unitCrossId;
	}

	public String getBaseCrossTrainId() {
		return baseCrossTrainId;
	}

	public void setBaseCrossTrainId(String baseCrossTrainId) {
		this.baseCrossTrainId = baseCrossTrainId;
	}

	public Integer getTrainSortBase() {
		return trainSortBase;
	}

	public void setTrainSortBase(Integer trainSortBase) {
		this.trainSortBase = trainSortBase;
	}

}
