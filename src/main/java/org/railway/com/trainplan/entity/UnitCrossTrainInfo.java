package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

public class UnitCrossTrainInfo {

	private List<UnitCrossTrainSubInfo> trainInfoList ;
	private String unitCrossTrainId;
	private String unitCrossId;
	private String groupSerialNbr;
	public List<UnitCrossTrainSubInfo> getTrainInfoList() {
		return trainInfoList;
	}
	public void setTrainInfoList(List<UnitCrossTrainSubInfo> trainInfoList) {
		this.trainInfoList = trainInfoList;
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
	public String getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(String groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	} 
	
	
	
}
