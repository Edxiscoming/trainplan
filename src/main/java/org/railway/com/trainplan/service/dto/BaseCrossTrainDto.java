package org.railway.com.trainplan.service.dto;

import java.util.ArrayList;
import java.util.List;

import org.railway.com.trainplan.web.dto.PlanLineSTNDto;

public class BaseCrossTrainDto {

	private String trainNbr;
	private int trainSort;
	private int dayGap;
	private String baseTrainId;
	private String runDate;
	private String endDate;
	private String endStn;
	private String startStn;
	public List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
	
	
	
	public List<PlanLineSTNDto> getTrainStns() {
		return trainStns;
	}
	public void setTrainStns(List<PlanLineSTNDto> trainStns) {
		this.trainStns = trainStns;
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
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
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
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	
	
}
