package com.railway.passenger.transdispatch.operationplan.entity;

/**
 * 开行计划自动生成相关信息
 */
public class TCmPlanAutogenerate {
    /**
     * 主键ID
     */
    private String cmAutogenId;

    /**
     * 是否开启自动生成(1-开启 0-关闭)
     */
    private Short isAutoGenerate;

    /**
     * 自动生成的时刻(时)
     */
    private String generateTime;

    /**
     * 担当局
     */
    private String tokenVehBureau;

    /**
     * 维护的开行计划天数(从当前日期开始，往后生成多少天的开行计划)
     */
    private Short maintainDays;

    /**
     * 高线标记（1:高线；0:普线；2:混合）
     */
    private Short highlineFlag;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String createPeople;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 修改人
     */
    private String updatePeople;

    /**
     * 使用状态(1-使用 0-未使用)
     */
    private Short useStatus;

    /**
     * @return 主键ID
     */
    public String getCmAutogenId() {
        return cmAutogenId;
    }

    /**
     * @param cmAutogenId 
	 *            主键ID
     */
    public void setCmAutogenId(String cmAutogenId) {
        this.cmAutogenId = cmAutogenId;
    }

    /**
     * @return 是否开启自动生成(1-开启 0-关闭)
     */
    public Short getIsAutoGenerate() {
        return isAutoGenerate;
    }

    /**
     * @param isAutoGenerate 
	 *            是否开启自动生成(1-开启 0-关闭)
     */
    public void setIsAutoGenerate(Short isAutoGenerate) {
        this.isAutoGenerate = isAutoGenerate;
    }

    /**
     * @return 自动生成的时刻(时)
     */
    public String getGenerateTime() {
        return generateTime;
    }

    /**
     * @param generateTime 
	 *            自动生成的时刻(时)
     */
    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
    }

    /**
     * @return 担当局
     */
    public String getTokenVehBureau() {
        return tokenVehBureau;
    }

    /**
     * @param tokenVehBureau 
	 *            担当局
     */
    public void setTokenVehBureau(String tokenVehBureau) {
        this.tokenVehBureau = tokenVehBureau;
    }

    /**
     * @return 维护的开行计划天数(从当前日期开始，往后生成多少天的开行计划)
     */
    public Short getMaintainDays() {
        return maintainDays;
    }

    /**
     * @param maintainDays 
	 *            维护的开行计划天数(从当前日期开始，往后生成多少天的开行计划)
     */
    public void setMaintainDays(Short maintainDays) {
        this.maintainDays = maintainDays;
    }

    /**
     * @return 高线标记（1:高线；0:普线；2:混合）
     */
    public Short getHighlineFlag() {
        return highlineFlag;
    }

    /**
     * @param highlineFlag 
	 *            高线标记（1:高线；0:普线；2:混合）
     */
    public void setHighlineFlag(Short highlineFlag) {
        this.highlineFlag = highlineFlag;
    }

    /**
     * @return 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note 
	 *            备注
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return 记录创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 
	 *            记录创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 创建人
     */
    public String getCreatePeople() {
        return createPeople;
    }

    /**
     * @param createPeople 
	 *            创建人
     */
    public void setCreatePeople(String createPeople) {
        this.createPeople = createPeople;
    }

    /**
     * @return 修改时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime 
	 *            修改时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return 修改人
     */
    public String getUpdatePeople() {
        return updatePeople;
    }

    /**
     * @param updatePeople 
	 *            修改人
     */
    public void setUpdatePeople(String updatePeople) {
        this.updatePeople = updatePeople;
    }

    /**
     * @return 使用状态(1-使用 0-未使用)
     */
    public Short getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            使用状态(1-使用 0-未使用)
     */
    public void setUseStatus(Short useStatus) {
        this.useStatus = useStatus;
    }
}