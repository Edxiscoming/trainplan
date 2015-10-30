package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

public class HighlineTrainRunLine {

	private List<HighlineTrainRunLineTime> highlineTrainTimeList = new ArrayList<HighlineTrainRunLineTime>();
	private String planTrainId;
	private String trainNbr;
	private String highLineTrainId;
	private String passBureau;
	private String createReason;
	public List<HighlineTrainRunLineTime> getHighlineTrainTimeList() {
		return highlineTrainTimeList;
	}
	public void setHighlineTrainTimeList(
			List<HighlineTrainRunLineTime> highlineTrainTimeList) {
		this.highlineTrainTimeList = highlineTrainTimeList;
	}
	
	
	public String getHighLineTrainId() {
		return highLineTrainId;
	}
	public void setHighLineTrainId(String highLineTrainId) {
		this.highLineTrainId = highLineTrainId;
	}
	public String getPassBureau() {
		return passBureau;
	}
	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getCreateReason() {
		return createReason;
	}
	public void setCreateReason(String createReason) {
		this.createReason = createReason;
	}
	
	
}
