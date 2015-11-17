package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 开行计划_调整记录
 */
public class TPlanModify {
    /**
     * 客运计划修改记录ID（本表ID）
     */
    private String planModifyId;

    /**
     * 列车ID
     */
    private String planTrainId;

    /**
     * 交路名
     */
    private String crossName;

    /**
     * 开行日期（格式：yyyymmdd）
     */
    private String runDate;

    /**
     * 车次
     */
    private String trainNbr;

    /**
     * 调整类型（0:调整时刻；1:调整经路；2:停运；3:启动备用）
     */
    private String modifyType;

    /**
     * 调整依据
     */
    private String modifyReason;

    /**
     * 起始日期
     */
    private String startDate;

    /**
     * 终止日期
     */
    private String endDate;

    /**
     * 规律
     */
    private String runRule;

    /**
     * 择日日期
     */
    private String selectedDate;

    /**
     * 调整内容
     */
    private String modifyContent;

    /**
     * 调整时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String modifyTime;

    /**
     * 调整人姓名
     */
    private String modifyPeople;

    /**
     * 调整人所属单位
     */
    private String modifyPeopleOrg;

    /**
     * 调整人所属路局（简称）
     */
    private String modifyPeopleBureau;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * @return 客运计划修改记录ID（本表ID）
     */
    public String getPlanModifyId() {
        return planModifyId;
    }

    /**
     * @param planModifyId 
	 *            客运计划修改记录ID（本表ID）
     */
    public void setPlanModifyId(String planModifyId) {
        this.planModifyId = planModifyId;
    }

    /**
     * @return 列车ID
     */
    public String getPlanTrainId() {
        return planTrainId;
    }

    /**
     * @param planTrainId 
	 *            列车ID
     */
    public void setPlanTrainId(String planTrainId) {
        this.planTrainId = planTrainId;
    }

    /**
     * @return 交路名
     */
    public String getCrossName() {
        return crossName;
    }

    /**
     * @param crossName 
	 *            交路名
     */
    public void setCrossName(String crossName) {
        this.crossName = crossName;
    }

    /**
     * @return 开行日期（格式：yyyymmdd）
     */
    public String getRunDate() {
        return runDate;
    }

    /**
     * @param runDate 
	 *            开行日期（格式：yyyymmdd）
     */
    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    /**
     * @return 车次
     */
    public String getTrainNbr() {
        return trainNbr;
    }

    /**
     * @param trainNbr 
	 *            车次
     */
    public void setTrainNbr(String trainNbr) {
        this.trainNbr = trainNbr;
    }

    /**
     * @return 调整类型（0:调整时刻；1:调整经路；2:停运；3:启动备用）
     */
    public String getModifyType() {
        return modifyType;
    }

    /**
     * @param modifyType 
	 *            调整类型（0:调整时刻；1:调整经路；2:停运；3:启动备用）
     */
    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    /**
     * @return 调整依据
     */
    public String getModifyReason() {
        return modifyReason;
    }

    /**
     * @param modifyReason 
	 *            调整依据
     */
    public void setModifyReason(String modifyReason) {
        this.modifyReason = modifyReason;
    }

    /**
     * @return 起始日期
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate 
	 *            起始日期
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return 终止日期
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate 
	 *            终止日期
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return 规律
     */
    public String getRunRule() {
        return runRule;
    }

    /**
     * @param runRule 
	 *            规律
     */
    public void setRunRule(String runRule) {
        this.runRule = runRule;
    }

    /**
     * @return 择日日期
     */
    public String getSelectedDate() {
        return selectedDate;
    }

    /**
     * @param selectedDate 
	 *            择日日期
     */
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    /**
     * @return 调整内容
     */
    public String getModifyContent() {
        return modifyContent;
    }

    /**
     * @param modifyContent 
	 *            调整内容
     */
    public void setModifyContent(String modifyContent) {
        this.modifyContent = modifyContent;
    }

    /**
     * @return 调整时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime 
	 *            调整时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return 调整人姓名
     */
    public String getModifyPeople() {
        return modifyPeople;
    }

    /**
     * @param modifyPeople 
	 *            调整人姓名
     */
    public void setModifyPeople(String modifyPeople) {
        this.modifyPeople = modifyPeople;
    }

    /**
     * @return 调整人所属单位
     */
    public String getModifyPeopleOrg() {
        return modifyPeopleOrg;
    }

    /**
     * @param modifyPeopleOrg 
	 *            调整人所属单位
     */
    public void setModifyPeopleOrg(String modifyPeopleOrg) {
        this.modifyPeopleOrg = modifyPeopleOrg;
    }

    /**
     * @return 调整人所属路局（简称）
     */
    public String getModifyPeopleBureau() {
        return modifyPeopleBureau;
    }

    /**
     * @param modifyPeopleBureau 
	 *            调整人所属路局（简称）
     */
    public void setModifyPeopleBureau(String modifyPeopleBureau) {
        this.modifyPeopleBureau = modifyPeopleBureau;
    }

    /**
     * @return 使用状态
     */
    public Short getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            使用状态
     */
    public void setUseStatus(Short useStatus) {
        this.useStatus = useStatus;
    }
}