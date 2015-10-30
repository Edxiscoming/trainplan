package org.railway.com.trainplan.entity;

import java.util.List;

public class UnitCrossTrainSubInfo {

	private int trainSort;
	private int dayGap;
	private String unitCrossTrainId;
	private String trainNbr;
	private String runDate;
	private String endDate;
	private String baseTrainId;
	private String startBureau;
	private String endStn;
	private String startStn;
	private String endBureau;
	public int groupSerialNbr;
	private List<UnitCrossTrainSubInfoTime> stationTimeList ;
	
	
	
	public int getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(int groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
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
	public List<UnitCrossTrainSubInfoTime> getStationTimeList() {
		return stationTimeList;
	}
	public void setStationTimeList(List<UnitCrossTrainSubInfoTime> stationTimeList) {
		this.stationTimeList = stationTimeList;
	}
	public String getUnitCrossTrainId() {
		return unitCrossTrainId;
	}
	public void setUnitCrossTrainId(String unitCrossTrainId) {
		this.unitCrossTrainId = unitCrossTrainId;
	}
	public int getTrainSort() {
		return trainSort;
	}
	public void setTrainSort(int trainSort) {
		this.trainSort = trainSort;
	}
	public int getDayGap() {
		return dayGap;
	}
	public void setDayGap(int dayGap) {
		this.dayGap = dayGap;
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
	
}
