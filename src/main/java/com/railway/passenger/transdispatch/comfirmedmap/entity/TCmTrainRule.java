package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 集中基本图列车开行规律
 */
public class TCmTrainRule {
    /**
     * 基本图开行规律ID
     */
    private String cmTrainRuleId;

    /**
     * 集中基本图列车ID
     */
    private String cmTrainId;

    /**
     * 高线开行规律（1:平日;2:周末;3:高峰）
     */
    private String highlineRule;

    /**
     * 普线开行规律（1:每日;2:隔日）
     */
    private String commonlineRule;

    /**
     * 指定星期
     */
    private String appointWeek;

    /**
     * 指定日期
     */
    private String appointDay;

    /**
     * 指定周期
     */
    private String appointPeriod;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * @return 基本图开行规律ID
     */
    public String getCmTrainRuleId() {
        return cmTrainRuleId;
    }

    /**
     * @param cmTrainRuleId 
	 *            基本图开行规律ID
     */
    public void setCmTrainRuleId(String cmTrainRuleId) {
        this.cmTrainRuleId = cmTrainRuleId;
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
     * @return 高线开行规律（1:平日;2:周末;3:高峰）
     */
    public String getHighlineRule() {
        return highlineRule;
    }

    /**
     * @param highlineRule 
	 *            高线开行规律（1:平日;2:周末;3:高峰）
     */
    public void setHighlineRule(String highlineRule) {
        this.highlineRule = highlineRule;
    }

    /**
     * @return 普线开行规律（1:每日;2:隔日）
     */
    public String getCommonlineRule() {
        return commonlineRule;
    }

    /**
     * @param commonlineRule 
	 *            普线开行规律（1:每日;2:隔日）
     */
    public void setCommonlineRule(String commonlineRule) {
        this.commonlineRule = commonlineRule;
    }

    /**
     * @return 指定星期
     */
    public String getAppointWeek() {
        return appointWeek;
    }

    /**
     * @param appointWeek 
	 *            指定星期
     */
    public void setAppointWeek(String appointWeek) {
        this.appointWeek = appointWeek;
    }

    /**
     * @return 指定日期
     */
    public String getAppointDay() {
        return appointDay;
    }

    /**
     * @param appointDay 
	 *            指定日期
     */
    public void setAppointDay(String appointDay) {
        this.appointDay = appointDay;
    }

    /**
     * @return 指定周期
     */
    public String getAppointPeriod() {
        return appointPeriod;
    }

    /**
     * @param appointPeriod 
	 *            指定周期
     */
    public void setAppointPeriod(String appointPeriod) {
        this.appointPeriod = appointPeriod;
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