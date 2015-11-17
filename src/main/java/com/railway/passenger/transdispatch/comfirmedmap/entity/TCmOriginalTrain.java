package com.railway.passenger.transdispatch.comfirmedmap.entity;

import java.util.UUID;

/**
 * 原始对数表车次信息
 */
public class TCmOriginalTrain {
    /**
     * 原始对数表车次ID
     */
    private String cmOriginalTrainId = UUID.randomUUID().toString();

    /**
     * 原始对数表ID
     */
    private String cmOriginalCrossId;

    /**
     * 原始对数表车次名称
     */
    private String trainName;

    /**
     * 交替时间
     */
    private String alternateDate;

    /**
     * 交替车次
     */
    private String alternateTrainNbr;

    /**
     * 对数
     */
    private String pairNbr;

    /**
     * 高线标志
     */
    private String highlineFlag;

    /**
     * 高线规则
     */
    private String highlineRule;

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
     * 运行区段
     */
    private String crossSection;

    /**
     * 经由区域
     */
    private String throughLine;

    /**
     * 担当局
     */
    private String tokenVehBureau;

    /**
     * 机车类型（客机、货机）
     */
    private String locoType;

    /**
     * 动车组车型（用于高铁）
     */
    private String crhType;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人
     */
    private String creatPeople;

    /**
     * 创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String creatTime;

    /**
     * 生成交路时间
     */
    private String creatCrossTime;

    /**
     * 生成交路标志
     */
    private int createCrossFlag;

    /**
     * 有效状态
     */
    private int useStatus;

    /**
     * 对数表excel序号
     */
    private String excelId;

    /**
     * 车次排列序号
     */
    private String trainSort;
    /**
     * 等级
     */
    private String crossLevel;
    /**
     * 普线开行规律（1:每日;2:隔日）
     */
    private String commonlineRule;
    /**
     * 出发站名
     */
    private String sourceNode;
    /**
     * 出发时间
     */
    private String sourceTime;
    /**
     * 到达站名
     */
    private String targetNode;
    /**
     * 到达时间
     */
    private String targetTime;
    /**
     * 方案名称
     */
    private String versionName;
    /**
     * 方案ID
     */
    private String versionId;
    /**
     * 过期时间
     */
    private String executionSourceTime;
    /**
     * 过期时间
     */
    private String executionTargetTime;
    /**
     * 开行状态
     */
    private String spareFlag;
    /**
     * 数据异常标志
     */
    private int exceptionflag;
    /**
     * 自动生成标志
     */
    private int autoFlag;
    
    /**
     * 始发局
     */
    private String sourceBureauShortName;
    
    /**
     * 终到局
     */
    private String targetBureauShortName;

    public String getSourceBureauShortName() {
		return sourceBureauShortName;
	}

	public void setSourceBureauShortName(String sourceBureauShortName) {
		this.sourceBureauShortName = sourceBureauShortName;
	}

	public String getTargetBureauShortName() {
		return targetBureauShortName;
	}

	public void setTargetBureauShortName(String targetBureauShortName) {
		this.targetBureauShortName = targetBureauShortName;
	}

	public int getAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(int autoFlag) {
		this.autoFlag = autoFlag;
	}

	/**
     * @return 原始对数表车次ID
     */
    public String getCmOriginalTrainId() {
        return cmOriginalTrainId;
    }

    /**
     * @param cmOriginalTrainId 
	 *            原始对数表车次ID
     */
    public void setCmOriginalTrainId(String cmOriginalTrainId) {
        this.cmOriginalTrainId = cmOriginalTrainId;
    }

    /**
     * @return 原始对数表ID
     */
    public String getCmOriginalCrossId() {
        return cmOriginalCrossId;
    }

    /**
     * @param cmOriginalCrossId 
	 *            原始对数表ID
     */
    public void setCmOriginalCrossId(String cmOriginalCrossId) {
        this.cmOriginalCrossId = cmOriginalCrossId;
    }

    /**
     * @return 原始对数表车次名称
     */
    public String getTrainName() {
        return trainName;
    }

    /**
     * @param trainName 
	 *            原始对数表车次名称
     */
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    /**
     * @return 交替时间
     */
    public String getAlternateDate() {
        return alternateDate;
    }

    /**
     * @param alternateDate 
	 *            交替时间
     */
    public void setAlternateDate(String alternateDate) {
        this.alternateDate = alternateDate;
    }

    /**
     * @return 交替车次
     */
    public String getAlternateTrainNbr() {
        return alternateTrainNbr;
    }

    /**
     * @param alternateTrainNbr 
	 *            交替车次
     */
    public void setAlternateTrainNbr(String alternateTrainNbr) {
        this.alternateTrainNbr = alternateTrainNbr;
    }

    /**
     * @return 对数
     */
    public String getPairNbr() {
        return pairNbr;
    }

    /**
     * @param pairNbr 
	 *            对数
     */
    public void setPairNbr(String pairNbr) {
        this.pairNbr = pairNbr;
    }

    /**
     * @return 高线标志
     */
    public String getHighlineFlag() {
        return highlineFlag;
    }

    /**
     * @param highlineFlag 
	 *            高线标志
     */
    public void setHighlineFlag(String highlineFlag) {
        this.highlineFlag = highlineFlag;
    }

    /**
     * @return 高线规则
     */
    public String getHighlineRule() {
        return highlineRule;
    }

    /**
     * @param highlineRule 
	 *            高线规则
     */
    public void setHighlineRule(String highlineRule) {
        this.highlineRule = highlineRule;
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
     * @return 运行区段
     */
    public String getCrossSection() {
        return crossSection;
    }

    /**
     * @param crossSection 
	 *            运行区段
     */
    public void setCrossSection(String crossSection) {
        this.crossSection = crossSection;
    }

    /**
     * @return 经由区域
     */
    public String getThroughLine() {
        return throughLine;
    }

    /**
     * @param throughLine 
	 *            经由区域
     */
    public void setThroughLine(String throughLine) {
        this.throughLine = throughLine;
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
     * @return 机车类型（客机、货机）
     */
    public String getLocoType() {
        return locoType;
    }

    /**
     * @param locoType 
	 *            机车类型（客机、货机）
     */
    public void setLocoType(String locoType) {
        this.locoType = locoType;
    }

    /**
     * @return 动车组车型（用于高铁）
     */
    public String getCrhType() {
        return crhType;
    }

    /**
     * @param crhType 
	 *            动车组车型（用于高铁）
     */
    public void setCrhType(String crhType) {
        this.crhType = crhType;
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
     * @return 生成交路时间
     */
    public String getCreatCrossTime() {
        return creatCrossTime;
    }

    /**
     * @param creatCrossTime 
	 *            生成交路时间
     */
    public void setCreatCrossTime(String creatCrossTime) {
        this.creatCrossTime = creatCrossTime;
    }

    /**
     * @return 生成交路标志
     */
    public int getCreateCrossFlag() {
        return createCrossFlag;
    }

    /**
     * @param createCrossFlag 
	 *            生成交路标志
     */
    public void setCreateCrossFlag(int createCrossFlag) {
        this.createCrossFlag = createCrossFlag;
    }

    /**
     * @return 有效状态
     */
    public int getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            有效状态
     */
    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    /**
     * @return 对数表excel序号
     */
    public String getExcelId() {
        return excelId;
    }

    /**
     * @param excelId 
	 *            对数表excel序号
     */
    public void setExcelId(String excelId) {
        this.excelId = excelId;
    }

    /**
     * @return 车次排列序号
     */
    public String getTrainSort() {
        return trainSort;
    }

    /**
     * @param trainSort 
	 *            车次排列序号
     */
    public void setTrainSort(String trainSort) {
        this.trainSort = trainSort;
    }

	public String getCrossLevel() {
		return crossLevel;
	}

	public void setCrossLevel(String crossLevel) {
		this.crossLevel = crossLevel;
	}

	public String getCommonlineRule() {
		return commonlineRule;
	}

	public void setCommonlineRule(String commonlineRule) {
		this.commonlineRule = commonlineRule;
	}

	public String getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(String sourceNode) {
		this.sourceNode = sourceNode;
	}

	public String getSourceTime() {
		return sourceTime;
	}

	public void setSourceTime(String sourceTime) {
		this.sourceTime = sourceTime;
	}

	public String getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(String targetNode) {
		this.targetNode = targetNode;
	}

	public String getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getExecutionSourceTime() {
		return executionSourceTime;
	}

	public void setExecutionSourceTime(String executionSourceTime) {
		this.executionSourceTime = executionSourceTime;
	}

	public String getExecutionTargetTime() {
		return executionTargetTime;
	}

	public void setExecutionTargetTime(String executionTargetTime) {
		this.executionTargetTime = executionTargetTime;
	}

	public String getSpareFlag() {
		return spareFlag;
	}

	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}

	public int getExceptionflag() {
		return exceptionflag;
	}

	public void setExceptionflag(int exceptionflag) {
		this.exceptionflag = exceptionflag;
	}
}