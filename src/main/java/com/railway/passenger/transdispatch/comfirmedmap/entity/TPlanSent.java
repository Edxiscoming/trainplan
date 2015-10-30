package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 落成计划_记录
 */
public class TPlanSent {
    /**
     * 落成运行线记录ID（本表ID）
     */
    private String planSentId;

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
     * 前续列车ID
     */
    private String preTrainId;

    /**
     * 后续列车ID
     */
    private String nextTrainId;

    /**
     * 落成时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String sentTime;

    /**
     * 落成人姓名
     */
    private String sentPeople;

    /**
     * 落成人所属单位
     */
    private String sentPeopleOrg;

    /**
     * 落成人所属路局（简称）
     */
    private String sentPeopleBureau;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * @return 落成运行线记录ID（本表ID）
     */
    public String getPlanSentId() {
        return planSentId;
    }

    /**
     * @param planSentId 
	 *            落成运行线记录ID（本表ID）
     */
    public void setPlanSentId(String planSentId) {
        this.planSentId = planSentId;
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
     * @return 前续列车ID
     */
    public String getPreTrainId() {
        return preTrainId;
    }

    /**
     * @param preTrainId 
	 *            前续列车ID
     */
    public void setPreTrainId(String preTrainId) {
        this.preTrainId = preTrainId;
    }

    /**
     * @return 后续列车ID
     */
    public String getNextTrainId() {
        return nextTrainId;
    }

    /**
     * @param nextTrainId 
	 *            后续列车ID
     */
    public void setNextTrainId(String nextTrainId) {
        this.nextTrainId = nextTrainId;
    }

    /**
     * @return 落成时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getSentTime() {
        return sentTime;
    }

    /**
     * @param sentTime 
	 *            落成时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    /**
     * @return 落成人姓名
     */
    public String getSentPeople() {
        return sentPeople;
    }

    /**
     * @param sentPeople 
	 *            落成人姓名
     */
    public void setSentPeople(String sentPeople) {
        this.sentPeople = sentPeople;
    }

    /**
     * @return 落成人所属单位
     */
    public String getSentPeopleOrg() {
        return sentPeopleOrg;
    }

    /**
     * @param sentPeopleOrg 
	 *            落成人所属单位
     */
    public void setSentPeopleOrg(String sentPeopleOrg) {
        this.sentPeopleOrg = sentPeopleOrg;
    }

    /**
     * @return 落成人所属路局（简称）
     */
    public String getSentPeopleBureau() {
        return sentPeopleBureau;
    }

    /**
     * @param sentPeopleBureau 
	 *            落成人所属路局（简称）
     */
    public void setSentPeopleBureau(String sentPeopleBureau) {
        this.sentPeopleBureau = sentPeopleBureau;
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