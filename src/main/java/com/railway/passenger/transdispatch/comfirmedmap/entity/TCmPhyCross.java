package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 集中基本图物理交路（车底交路）
 */
public class TCmPhyCross {
    /**
     * 主键
     */
    private String cmPhyCrossId;

    /**
     * 逻辑交路ID
     */
    private String cmCrossId;

    /**
     * 组序号
     */
    private Short groupSn;

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
     * 物理交路使用状态
     */
    private Short useStatus;

    /**
     * 审核状态
     */
    private Short checkFlag;

    /**
     * @return 主键
     */
    public String getCmPhyCrossId() {
        return cmPhyCrossId;
    }

    /**
     * @param cmPhyCrossId 
	 *            主键
     */
    public void setCmPhyCrossId(String cmPhyCrossId) {
        this.cmPhyCrossId = cmPhyCrossId;
    }

    /**
     * @return 逻辑交路ID
     */
    public String getCmCrossId() {
        return cmCrossId;
    }

    /**
     * @param cmCrossId 
	 *            逻辑交路ID
     */
    public void setCmCrossId(String cmCrossId) {
        this.cmCrossId = cmCrossId;
    }

    /**
     * @return 组序号
     */
    public Short getGroupSn() {
        return groupSn;
    }

    /**
     * @param groupSn 
	 *            组序号
     */
    public void setGroupSn(Short groupSn) {
        this.groupSn = groupSn;
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
     * @return 物理交路使用状态
     */
    public Short getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            物理交路使用状态
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