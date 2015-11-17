package org.railway.com.trainplan.entity;


public class CrossRunPlanInfo {
	 
	private String trainNbr;
	private String runDay;
	
	private String startStn;
	private String endStn;
	private String runFlag;
	private String crossName;
	private String createFlag;
	private String planTrainId;
	private String baseTrainId;
	
	private String tokenVehBureau;
	
	private String planCrossId;
	private String unitCrossId;
	private String cmdTrainId;
	private String telCmdId;
	private String telName;
	private String isModified;
	
    /**
     * plancross对应的逻辑交路id
     */
    private String cmCrossId;
	
	/**
	 * 交路开行日期,交路单元第一组的始发日期（格式：yyyymmdd）.
	 */
	private String cross_start_date; 
	
	/**
	 * 车序.
	 */
	private Integer trainSort;
	/**
	 * 车组.
	 */
	private String groupSerialNbr;
	
	public String getTelCmdId() {
		return telCmdId;
	}
	public void setTelCmdId(String telCmdId) {
		this.telCmdId = telCmdId;
	}
	public String getCmdTrainId() {
		return cmdTrainId;
	}
	public void setCmdTrainId(String cmdTrainId) {
		this.cmdTrainId = cmdTrainId;
	}
	public String getUnitCrossId() {
		return unitCrossId;
	}
	public void setUnitCrossId(String unitCrossId) {
		this.unitCrossId = unitCrossId;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	 
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	public String getTokenVehBureau() {
		return tokenVehBureau;
	}
	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}
	public String getPlanCrossId() {
		return planCrossId;
	}
	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public String getCreateFlag() {
		return createFlag;
	}
	public void setCreateFlag(String createFlag) {
		this.createFlag = createFlag;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getRunDay() {
		return runDay;
	}
	public void setRunDay(String runDay) {
		this.runDay = runDay;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	public String getCross_start_date() {
		return cross_start_date;
	}
	public void setCross_start_date(String cross_start_date) {
		this.cross_start_date = cross_start_date;
	}
	public String getTelName() {
		return telName;
	}
	public void setTelName(String telName) {
		this.telName = telName;
	}
	public String getIsModified() {
		return isModified;
	}
	public void setIsModified(String isModified) {
		this.isModified = isModified;
	}
	public Integer getTrainSort() {
		return trainSort;
	}
	public void setTrainSort(Integer trainSort) {
		this.trainSort = trainSort;
	}
	public String getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(String groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}
	public String getCmCrossId() {
		return cmCrossId;
	}
	public void setCmCrossId(String cmCrossId) {
		this.cmCrossId = cmCrossId;
	}
}
