package org.railway.com.trainplan.service.dto;

public class TrainRunDto {
	private String day;
	private String runFlag; 
	private String createFlag;
	private String telName;
	private String planTrainId;
	private String isModified;
	private String trainSort;
	private String groupSerialNbr;
	
	public String getTelName() {
		return telName;
	}
	public void setTelName(String telName) {
		this.telName = telName;
	}
	public TrainRunDto(){}
	public TrainRunDto(String day, String runFlag,String telName,String planTrainId,String isModified) {
		super();
		this.day = day;
		this.runFlag = runFlag;
		this.telName = telName;
		this.planTrainId =planTrainId;
		this.isModified =isModified;
	}
	
	public TrainRunDto(String day, String runFlag, String createFlag,String telName,String planTrainId,String isModified) {
		super();
		this.day = day;
		this.runFlag = runFlag;
		this.createFlag = createFlag;
		this.telName = telName;
		this.planTrainId =planTrainId;
		this.isModified =isModified;
	}
	
	
	public String getCreateFlag() {
		return createFlag;
	}


	public void setCreateFlag(String createFlag) {
		this.createFlag = createFlag;
	}


	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getIsModified() {
		return isModified;
	}
	public void setIsModified(String isModified) {
		this.isModified = isModified;
	}
	public String getTrainSort() {
		return trainSort;
	}
	public void setTrainSort(String trainSort) {
		this.trainSort = trainSort;
	}
	public String getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(String groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}
	

}
