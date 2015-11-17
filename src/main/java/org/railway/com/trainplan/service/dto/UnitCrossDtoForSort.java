package org.railway.com.trainplan.service.dto;

public class UnitCrossDtoForSort {
	private String unitCrossTrainId;
	private int trainSort;
	private int groupGap;
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
	public int getGroupGap() {
		return groupGap;
	}
	public void setGroupGap(int groupGap) {
		this.groupGap = groupGap;
	}
	 
}
