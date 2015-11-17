package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

public class BaseCrossTrainInfo {

	private String baseCrossId;
	private String crossStartDate;
	private String crossEndDate;
	private String crossName;
	private List<BaseCrossTrainSubInfo> trainList = new ArrayList<BaseCrossTrainSubInfo>();
	public String getBaseCrossId() {
		return baseCrossId;
	}
	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	}
	public String getCrossStartDate() {
		return crossStartDate;
	}
	public void setCrossStartDate(String crossStartDate) {
		this.crossStartDate = crossStartDate;
	}
	public String getCrossEndDate() {
		return crossEndDate;
	}
	public void setCrossEndDate(String crossEndDate) {
		this.crossEndDate = crossEndDate;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public List<BaseCrossTrainSubInfo> getTrainList() {
		return trainList;
	}
	public void setTrainList(List<BaseCrossTrainSubInfo> trainList) {
		this.trainList = trainList;
	}
	
	
}
