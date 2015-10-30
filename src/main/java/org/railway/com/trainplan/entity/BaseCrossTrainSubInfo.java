package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

public class BaseCrossTrainSubInfo {

	private String baseTrainId;
	private int trainSort;
	private String trainNbr;
	private String startStn;
	private String endStn;
	private String startTime;
	private String endTime;
	private String baseCrossTrainId;
	private String groupSn;
	
	public String getGroupSn() {
		return groupSn;
	}

	public void setGroupSn(String groupSn) {
		this.groupSn = groupSn;
	}

	private List<BaseCrossTrainInfoTime> stationList = new ArrayList<BaseCrossTrainInfoTime>();

	
	
	public String getBaseCrossTrainId() {
		return baseCrossTrainId;
	}

	public void setBaseCrossTrainId(String baseCrossTrainId) {
		this.baseCrossTrainId = baseCrossTrainId;
	}

	public String getBaseTrainId() {
		return baseTrainId;
	}

	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
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

	public String getEndStn() {
		return endStn;
	}

	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<BaseCrossTrainInfoTime> getStationList() {
		return stationList;
	}

	public void setStationList(List<BaseCrossTrainInfoTime> stationList) {
		this.stationList = stationList;
	}
	
	
}
