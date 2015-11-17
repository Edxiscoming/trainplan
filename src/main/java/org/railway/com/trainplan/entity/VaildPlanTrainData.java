package org.railway.com.trainplan.entity;

public class VaildPlanTrainData {
	/**
	 * plan_train,plan_train_id.
	 */
	private String id;
	/**
	 * plan_train,
	 */
	private String trainNbr;
	/**
	 * plan_train,start_bureau_full.
	 */
	private String sourceBureauName;
	/**
	 * plan_train,end_bureau_full.
	 */
	private String targetBureauName;
	/**
	 * plan_train,start_stn.
	 */
	private String sourceNodeName;
	/**
	 * plan_train,end_stn.
	 */
	private String targetNodeName;
	/**
	 * plan_train,start_time.
	 */
	private String startTime;
	/**
	 * plan_train,end_time.
	 */
	private String endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTrainNbr() {
		return trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}

	public String getSourceBureauName() {
		return sourceBureauName;
	}

	public void setSourceBureauName(String sourceBureauName) {
		this.sourceBureauName = sourceBureauName;
	}

	public String getTargetBureauName() {
		return targetBureauName;
	}

	public void setTargetBureauName(String targetBureauName) {
		this.targetBureauName = targetBureauName;
	}

	public String getSourceNodeName() {
		return sourceNodeName;
	}

	public void setSourceNodeName(String sourceNodeName) {
		this.sourceNodeName = sourceNodeName;
	}

	public String getTargetNodeName() {
		return targetNodeName;
	}

	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
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

}
