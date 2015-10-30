package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 集中基本图列车交替信息
 */
public class TCmTrainAlternate {
    /**
     * 基本图交替信息ID
     */
    private String cmTrainAlternateId;

    /**
     * 集中基本图列车ID
     */
    private String cmTrainId;

    /**
     * 交替日期
     */
    private String alternateDate;

    /**
     * 交替原车次
     */
    private String alternateTrainNbr;

    /**
     * 创建人
     */
    private String creatPeople;

    /**
     * 创建人单位
     */
    private String creatPeopleOrg;

    /**
     * 创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String creatTime;

    /**
     * 审核人
     */
    private String checkPeople;

    /**
     * 审核人单位
     */
    private String checkPeopleOrg;

    /**
     * 审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String checkTime;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * 审核状态
     */
    private Short checkFlag;

    /**
     * @return 基本图交替信息ID
     */
    public String getCmTrainAlternateId() {
        return cmTrainAlternateId;
    }

    /**
     * @param cmTrainAlternateId 
	 *            基本图交替信息ID
     */
    public void setCmTrainAlternateId(String cmTrainAlternateId) {
        this.cmTrainAlternateId = cmTrainAlternateId;
    }

    /**
     * @return 集中基本图列车ID
     */
    public String getCmTrainId() {
        return cmTrainId;
    }

    /**
     * @param cmTrainId 
	 *            集中基本图列车ID
     */
    public void setCmTrainId(String cmTrainId) {
        this.cmTrainId = cmTrainId;
    }

    /**
     * @return 交替日期
     */
    public String getAlternateDate() {
        return alternateDate;
    }

    /**
     * @param alternateDate 
	 *            交替日期
     */
    public void setAlternateDate(String alternateDate) {
        this.alternateDate = alternateDate;
    }

    /**
     * @return 交替原车次
     */
    public String getAlternateTrainNbr() {
        return alternateTrainNbr;
    }

    /**
     * @param alternateTrainNbr 
	 *            交替原车次
     */
    public void setAlternateTrainNbr(String alternateTrainNbr) {
        this.alternateTrainNbr = alternateTrainNbr;
    }

    /**
     * @return 创建人
     */
    public String getCreatPeople() {
        return creatPeople;
    }

    /**
     * @param creatPeople 
	 *            创建人
     */
    public void setCreatPeople(String creatPeople) {
        this.creatPeople = creatPeople;
    }

    /**
     * @return 创建人单位
     */
    public String getCreatPeopleOrg() {
        return creatPeopleOrg;
    }

    /**
     * @param creatPeopleOrg 
	 *            创建人单位
     */
    public void setCreatPeopleOrg(String creatPeopleOrg) {
        this.creatPeopleOrg = creatPeopleOrg;
    }

    /**
     * @return 创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getCreatTime() {
        return creatTime;
    }

    /**
     * @param creatTime 
	 *            创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    /**
     * @return 审核人
     */
    public String getCheckPeople() {
        return checkPeople;
    }

    /**
     * @param checkPeople 
	 *            审核人
     */
    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    /**
     * @return 审核人单位
     */
    public String getCheckPeopleOrg() {
        return checkPeopleOrg;
    }

    /**
     * @param checkPeopleOrg 
	 *            审核人单位
     */
    public void setCheckPeopleOrg(String checkPeopleOrg) {
        this.checkPeopleOrg = checkPeopleOrg;
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

    /**
     * @return 审核状态
     */
    public Short getCheckFlag() {
        return checkFlag;
    }

    /**
     * @param checkFlag 
	 *            审核状态
     */
    public void setCheckFlag(Short checkFlag) {
        this.checkFlag = checkFlag;
    }
}