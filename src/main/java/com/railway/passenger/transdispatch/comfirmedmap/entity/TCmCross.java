package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 集中基本图交路信息（逻辑交路）
 */
public class TCmCross {
    /**
     * 主键
     */
    private String cmCrossId;

    /**
     * 逻辑交路名
     */
    private String cmCrossName;

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
     * 逻辑交路使用状态
     */
    private Short useStatus;

    /**
     * 审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String checkTime;

    /**
     * 对数信息ID
     */
    private String cmOriginalCrossId;

    /**
     * 审核状态
     */
    private Short checkFlag;
    
    /**
     *  但当局
     */
    private String tokenpsgbureau;
    
    /**
     * 经由局
     */
    private String throughline;
    
    /**
     * 方案id
     */
    private String cmVersionId;
    
    
    /**
     * 高线规律
     */
    private String highlineflag;
    
    
    public String getHighlineflag() {
		return highlineflag;
	}

	public void setHighlineflag(String highlineflag) {
		this.highlineflag = highlineflag;
	}

	/**
     * @return
     * 获取但当局
     */
    public String getTokenPsgBureau() {
		return tokenpsgbureau;
	}

	/**
	 * @param tokenPsgBureau
	 * 设置但当局
	 */
	public void setTokenPsgBureau(String tokenpsgbureau) {
		this.tokenpsgbureau = tokenpsgbureau;
	}

    /**
     * @return
     * 获取经由局
     */
    public String getThroughLine() {
		return throughline;
	}

	/**
	 * @param throughLine
	 * 设置经由局
	 */
	public void setThroughLine(String throughline) {
		this.throughline = throughline;
	}

    /**
     * @return
     * 方案ID
     */
    public String getCmVersionId() {
		return cmVersionId;
	}

	/**
	 * @param cmVersionId
	 * 方案ID
	 */
	public void setCmVersionId(String cmVersionId) {
		this.cmVersionId = cmVersionId;
	}

	/**
     * @return 主键
     */
    public String getCmCrossId() {
        return cmCrossId;
    }

    /**
     * @param cmCrossId 
	 *            主键
     */
    public void setCmCrossId(String cmCrossId) {
        this.cmCrossId = cmCrossId;
    }

    /**
     * @return 逻辑交路名
     */
    public String getCmCrossName() {
        return cmCrossName;
    }

    /**
     * @param cmCrossName 
	 *            逻辑交路名
     */
    public void setCmCrossName(String cmCrossName) {
        this.cmCrossName = cmCrossName;
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
     * @return 逻辑交路使用状态
     */
    public Short getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            逻辑交路使用状态
     */
    public void setUseStatus(Short useStatus) {
        this.useStatus = useStatus;
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
     * @return 对数信息ID
     */
    public String getCmOriginalCrossId() {
        return cmOriginalCrossId;
    }

    /**
     * @param cmOriginalCrossId 
	 *            对数信息ID
     */
    public void setCmOriginalCrossId(String cmOriginalCrossId) {
        this.cmOriginalCrossId = cmOriginalCrossId;
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