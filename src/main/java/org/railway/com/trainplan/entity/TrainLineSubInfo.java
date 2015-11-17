package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

public class TrainLineSubInfo {

	private String planTrainId;
	private String trainNbr;
	private int trainSort;
	private String startTime;
	private String endTime;
	private String startStn;
	private String endStn;
	/**
	 * 途径局.
	 */
	private String passBureau;
	private List<TrainLineSubInfoTime> trainStaionList = new ArrayList<TrainLineSubInfoTime>();
	
	private String planCrossId;

	public List<TrainLineSubInfoTime> getTrainStaionList() {
		return trainStaionList;
	}

	public void setTrainStaionList(List<TrainLineSubInfoTime> trainStaionList) {
		this.trainStaionList = trainStaionList;
	}

	public String getTrainNbr() {
		return trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}

	public String getPlanTrainId() {
		return planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	public int getTrainSort() {
		return trainSort;
	}

	public void setTrainSort(int trainSort) {
		this.trainSort = trainSort;
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

	public String getPassBureau() {
		return passBureau;
	}

	public String getPlanCrossId() {
		return planCrossId;
	}

	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}

	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}

}
