package org.railway.com.trainplan.entity;

import java.io.Serializable;

/**
 * Created by speeder on 2014/5/28.
 */
public class UnitCrossTrain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String unitCrossTrainId;
	private String unitCrossId;
	private int groupSerialNbr;
	private int groupGap;
	private String marshallingName;
	private int trainSort;
	private String trainNbr;
	private String baseTrainId;
	private String startStn;
	private String startBureauCode;
	private String endStn;
	private String endBureauCode;
	private int dayGap;
	private String alternateTrainNbr;
	private String alternateDateTime;
	private int spareFlag;
	private int spareApplyFlag;
	private int highLineFlag;
	private int highLineRule;
	private int commonLineRule;
	private String appointWeek;
	private String appointDay;
	private String runDate;
	private String endDate;
	
	private String trainSortBase;

	// private UnitCross unitCross;

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

	public String getBaseTrainId() {
		return baseTrainId;
	}

	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}

	public String getStartStn() {
		return startStn;
	}

	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}

	public String getStartBureauCode() {
		return startBureauCode;
	}

	public void setStartBureauCode(String startBureauCode) {
		this.startBureauCode = startBureauCode;
	}

	public String getEndStn() {
		return endStn;
	}

	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}

	public String getEndBureauCode() {
		return endBureauCode;
	}

	public void setEndBureauCode(String endBureauCode) {
		this.endBureauCode = endBureauCode;
	}

	public int getDayGap() {
		return dayGap;
	}

	public void setDayGap(int dayGap) {
		this.dayGap = dayGap;
	}

	public String getAlternateTrainNbr() {
		return alternateTrainNbr;
	}

	public void setAlternateTrainNbr(String alternateTrainNbr) {
		this.alternateTrainNbr = alternateTrainNbr;
	}

	public String getAlternateDateTime() {
		return alternateDateTime;
	}

	public void setAlternateDateTime(String alternateDateTime) {
		this.alternateDateTime = alternateDateTime;
	}

	public int getSpareFlag() {
		return spareFlag;
	}

	public void setSpareFlag(int spareFlag) {
		this.spareFlag = spareFlag;
	}

	public int getSpareApplyFlag() {
		return spareApplyFlag;
	}

	public void setSpareApplyFlag(int spareApplyFlag) {
		this.spareApplyFlag = spareApplyFlag;
	}

	public int getHighLineFlag() {
		return highLineFlag;
	}

	public void setHighLineFlag(int highLineFlag) {
		this.highLineFlag = highLineFlag;
	}

	public int getHighLineRule() {
		return highLineRule;
	}

	public void setHighLineRule(int highLineRule) {
		this.highLineRule = highLineRule;
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

	public int getGroupGap() {
		return groupGap;
	}

	public void setGroupGap(int groupGap) {
		this.groupGap = groupGap;
	}

	public String getTrainSortBase() {
		return trainSortBase;
	}

	public void setTrainSortBase(String trainSortBase) {
		this.trainSortBase = trainSortBase;
	}

	/*
	 * public UnitCross getUnitCross() { return unitCross; }
	 * 
	 * public void setUnitCross(UnitCross unitCross) { this.unitCross =
	 * unitCross; }
	 */
}
