package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

public class TrainLineInfo {

	private String groupSerialNbr;
	private List<TrainLineSubInfo> trainSubInfoList = new ArrayList<TrainLineSubInfo>();
	
	public String getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(String groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}
	public List<TrainLineSubInfo> getTrainSubInfoList() {
		return trainSubInfoList;
	}
	public void setTrainSubInfoList(List<TrainLineSubInfo> trainSubInfoList) {
		this.trainSubInfoList = trainSubInfoList;
	}
	
	
	
}
