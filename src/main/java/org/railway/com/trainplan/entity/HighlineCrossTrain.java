package org.railway.com.trainplan.entity;

import java.io.Serializable;

/**
 * The persistent class for the HIGHLINE_CROSS_TRAIN database table.
 * 
 */
public class HighlineCrossTrain implements Serializable {
	private static final long serialVersionUID = 1L;

	private String highlineCrossId;

	private String highlineTrainId;

	private String planTrainId;

	private String runDate = "";

	private String trainNbr = "";

	private Integer trainSort;

    public HighlineCrossTrain() {
    }

	public String getHighlineCrossId() {
		return this.highlineCrossId;
	}

	public void setHighlineCrossId(String highlineCrossId) {
		this.highlineCrossId = highlineCrossId;
	}

	public String getHighlineTrainId() {
		return this.highlineTrainId;
	}

	public void setHighlineTrainId(String highlineTrainId) {
		this.highlineTrainId = highlineTrainId;
	}

	public String getPlanTrainId() {
		return this.planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	public String getRunDate() {
		return this.runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getTrainNbr() {
		return this.trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}

	public Integer getTrainSort() {
		return this.trainSort;
	}

	public void setTrainSort(Integer trainSort) {
		this.trainSort = trainSort;
	}

}