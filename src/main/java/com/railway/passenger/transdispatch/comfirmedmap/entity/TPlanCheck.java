package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 开行计划审核
 */
public class TPlanCheck {
    /**
     * 客运计划审核记录ID
     */
    private String planCheckId;

    /**
     * 开行计划列车ID
     */
    private String planTrainId;

    /**
     * 命令/文电ID（临客）
     */
    private String cmdTelId;

    /**
     * 计划审核起始日期（格式：yyyymmdd）
     */
    private String startDate;

    /**
     * 计划审核终止日期（格式：yyyymmdd）
     */
    private String endDate;

    /**
     * 审核人姓名
     */
    private String checkPeople;

    /**
     * 审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String checkTime;

    /**
     * 审核人岗位
     */
    private String checkDept;

    /**
     * 审核人所属局（局码）
     */
    private String checkBureau;

    /**
     * 历史审核标记（0:当前；1:历史）
     */
    private String checkHisFlag;

    /**
     * 审核结果（0:审核通过；1:审核不通过）
     */
    private String checkState;

    /**
     * 审核不通过原因
     */
    private String checkRejectReason;

    /**
     * 审核人联系方式
     */
    private String checkPeopleTel;

    /**
     * 审核标记
     */
    private Short checkFlag;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * @return 客运计划审核记录ID
     */
    public String getPlanCheckId() {
        return planCheckId;
    }

    /**
     * @param planCheckId 
	 *            客运计划审核记录ID
     */
    public void setPlanCheckId(String planCheckId) {
        this.planCheckId = planCheckId;
    }

    /**
     * @return 开行计划列车ID
     */
    public String getPlanTrainId() {
        return planTrainId;
    }

    /**
     * @param planTrainId 
	 *            开行计划列车ID
     */
    public void setPlanTrainId(String planTrainId) {
        this.planTrainId = planTrainId;
    }

    /**
     * @return 命令/文电ID（临客）
     */
    public String getCmdTelId() {
        return cmdTelId;
    }

    /**
     * @param cmdTelId 
	 *            命令/文电ID（临客）
     */
    public void setCmdTelId(String cmdTelId) {
        this.cmdTelId = cmdTelId;
    }

    /**
     * @return 计划审核起始日期（格式：yyyymmdd）
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate 
	 *            计划审核起始日期（格式：yyyymmdd）
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return 计划审核终止日期（格式：yyyymmdd）
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate 
	 *            计划审核终止日期（格式：yyyymmdd）
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return 审核人姓名
     */
    public String getCheckPeople() {
        return checkPeople;
    }

    /**
     * @param checkPeople 
	 *            审核人姓名
     */
    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    /**
     * @return 审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getCheckTime() {
        return checkTime;
    }

    /**
     * @param checkTime 
	 *            审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * @return 审核人岗位
     */
    public String getCheckDept() {
        return checkDept;
    }

    /**
     * @param checkDept 
	 *            审核人岗位
     */
    public void setCheckDept(String checkDept) {
        this.checkDept = checkDept;
    }

    /**
     * @return 审核人所属局（局码）
     */
    public String getCheckBureau() {
        return checkBureau;
    }

    /**
     * @param checkBureau 
	 *            审核人所属局（局码）
     */
    public void setCheckBureau(String checkBureau) {
        this.checkBureau = checkBureau;
    }

    /**
     * @return 历史审核标记（0:当前；1:历史）
     */
    public String getCheckHisFlag() {
        return checkHisFlag;
    }

    /**
     * @param checkHisFlag 
	 *            历史审核标记（0:当前；1:历史）
     */
    public void setCheckHisFlag(String checkHisFlag) {
        this.checkHisFlag = checkHisFlag;
    }

    /**
     * @return 审核结果（0:审核通过；1:审核不通过）
     */
    public String getCheckState() {
        return checkState;
    }

    /**
     * @param checkState 
	 *            审核结果（0:审核通过；1:审核不通过）
     */
    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    /**
     * @return 审核不通过原因
     */
    public String getCheckRejectReason() {
        return checkRejectReason;
    }

    /**
     * @param checkRejectReason 
	 *            审核不通过原因
     */
    public void setCheckRejectReason(String checkRejectReason) {
        this.checkRejectReason = checkRejectReason;
    }

    /**
     * @return 审核人联系方式
     */
    public String getCheckPeopleTel() {
        return checkPeopleTel;
    }

    /**
     * @param checkPeopleTel 
	 *            审核人联系方式
     */
    public void setCheckPeopleTel(String checkPeopleTel) {
        this.checkPeopleTel = checkPeopleTel;
    }

    /**
     * @return 审核标记
     */
    public Short getCheckFlag() {
        return checkFlag;
    }

    /**
     * @param checkFlag 
	 *            审核标记
     */
    public void setCheckFlag(Short checkFlag) {
        this.checkFlag = checkFlag;
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