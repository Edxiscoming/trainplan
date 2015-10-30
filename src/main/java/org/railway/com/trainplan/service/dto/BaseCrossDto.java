package org.railway.com.trainplan.service.dto;

import java.util.ArrayList;
import java.util.List;

public class BaseCrossDto {

	private String crossName;
	private String crossStartDate;
	private String crossEndDate;
	private String baseCrossId;
	private List<BaseCrossTrainDto> listBaseCrossTrain = new ArrayList<BaseCrossTrainDto>();
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
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
	public List<BaseCrossTrainDto> getListBaseCrossTrain() {
		return listBaseCrossTrain;
	}
	public void setListBaseCrossTrain(List<BaseCrossTrainDto> listBaseCrossTrain) {
		this.listBaseCrossTrain = listBaseCrossTrain;
	}
	public String getBaseCrossId() {
		return baseCrossId;
	}
	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	}
	
	
}
