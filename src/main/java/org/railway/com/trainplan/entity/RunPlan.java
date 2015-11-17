package org.railway.com.trainplan.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Plan_Train对象
 * Created by speeder on 2014/5/28.
 */
public class RunPlan {

    private String planTrainId;
    private String planTrainSign;
    private String planCrossId;
    private String marshallingName;
    private int groupSerialNbr;
    private int trainSort;
    private String preTrainId;
    private String nextTrainId;
    private String runDate;
    private String trainNbr;
    private String startTimeStr;
    private Timestamp startDateTime;
    private String endTimeStr;
    private Timestamp endDateTime;
    private String startBureauShortName;
    private String startBureauFullName;
    private String startStn;
    private String endBureauShortName;
    private String endBureauFullName;
    private String endStn;
    private String passBureau;
    private int trainScope;
    private String trainTypeId;
    private int highLineFlag;
    private String baseChartId;
    private String baseTrainId;
    private int hightLineRule;
    private int commonLineRule;
    private String appointWeek;
    private String appointDay;
    private int dayGap;
    private int spareFlag;
    private int spareApplyFlag;
    private int createType;
    private Date createDateTime;
    private int checkLev1Type;
    private int checkLev2Type;
    // 上图标记
    private int dailyPlanFlag;
    // 上图时间
    private Date dailyPlanTime;
    // 上图次数
    private int dailyPlanTimes;
    private String telBureau;
    private String telId;
    
    private String telName; 
    private String telShortinfo;
    
    private String importantFlag;
    
    
    private String checkflagchoice;
    
//    SOURCE_NODE_STATION_ID(jbt)	VARCHAR2(36)	始发站ID	存进 START_STATION_STN_ID(plan_train表)
//    SOURCE_NODE_STATION_NAME	VARCHAR2(50)	始发站名	存进 START_STATION_STN_NAME
//    SOURCE_NODE_TDCS_ID	VARCHAR2(50)	始发节点TDCS对应标识	存进 START_STN_TDCS_ID
//    SOURCE_NODE_TDCS_NAME	VARCHAR2(50)	始发节点TDCS对应名称	存进 START_STN_TDCS_NAME
//    TARGET_NODE_STATION_ID	VARCHAR2(36)	终到站ID	存进 END_STATION_STN_ID
//    TARGET_NODE_STATION_NAME	VARCHAR2(50)	终到站名	存进 END_STATION_STN_NAME
//    TARGET_NODE_TDCS_ID	VARCHAR2(50)	终到节点TDCS对应标识	存进 END_STN_TDCS_ID
//    TARGET_NODE_TDCS_NAME	VARCHAR2(50)	终到节点TDCS对应名称	存进 END_STN_TDCS_NAME

//    private String  startStationStnId;
//    private String  startStationStnName;
//    private String  startStnTdcsId;
//    private String  startStnTdcsName;
//    private String  endStationStnId;
//    private String  endStationStnName;
//    private String  endStnTdcsId;
//    private String  endStnTdcsName;
    
    
    private String checkType;
    private String checkBureau;

	public String getCheckflagchoice() {
		return checkflagchoice;
	}

	public void setCheckflagchoice(String checkflagchoice) {
		this.checkflagchoice = checkflagchoice;
	}

	public String getStartStationStnId() {
		return startStationStnId;
	}

	public void setStartStationStnId(String startStationStnId) {
		this.startStationStnId = startStationStnId;
	}

	public String getStartStationStnName() {
		return startStationStnName;
	}

	public void setStartStationStnName(String startStationStnName) {
		this.startStationStnName = startStationStnName;
	}

	public String getStartStnTdcsId() {
		return startStnTdcsId;
	}

	public void setStartStnTdcsId(String startStnTdcsId) {
		this.startStnTdcsId = startStnTdcsId;
	}

	public String getStartStnTdcsName() {
		return startStnTdcsName;
	}

	public void setStartStnTdcsName(String startStnTdcsName) {
		this.startStnTdcsName = startStnTdcsName;
	}

	public String getEndStationStnId() {
		return endStationStnId;
	}

	public void setEndStationStnId(String endStationStnId) {
		this.endStationStnId = endStationStnId;
	}

	public String getEndStationStnName() {
		return endStationStnName;
	}

	public void setEndStationStnName(String endStationStnName) {
		this.endStationStnName = endStationStnName;
	}

	public String getEndStnTdcsId() {
		return endStnTdcsId;
	}

	public void setEndStnTdcsId(String endStnTdcsId) {
		this.endStnTdcsId = endStnTdcsId;
	}

	public String getEndStnTdcsName() {
		return endStnTdcsName;
	}

	public void setEndStnTdcsName(String endStnTdcsName) {
		this.endStnTdcsName = endStnTdcsName;
	}

	public String getTelName() {
		return telName;
	}

	public void setTelName(String telName) {
		this.telName = telName;
	}

	public String getTelShortinfo() {
		return telShortinfo;
	}

	public void setTelShortinfo(String telShortinfo) {
		this.telShortinfo = telShortinfo;
	}

	//文电名
    private String telNum;
    private String cmdBureau;
    private Integer cmdTxtmlId;
    private Integer cmdTxtmlitemId;
    private String cmdShortInfo;
    private String note;
    private String dailyPlanId;
    //车辆担当局TOKEN_VEH_BUREAU
    private String tokenVehBureau;
    private String createPeople;
    private String createPeopleOrg;
    private String cmdTrainId;
    
    private String business;
    private String sourceBureauId;
    private String sourceNodeId;
    private String targetBureauId;
    private String targetNodeId;
    private String targetTimeScheduleDates;
    private String routeId;
    
    private List<RunPlanStn> runPlanStnList;

    
    
   private String lineReloadStatus;
   


	/**
	 * 始发站ID.
	 */
	private String startStationStnId;
	/**
	 * 始发站名.
	 */
	private String startStationStnName;
	/**
	 * 始发节点TDCS对应标识.
	 */
	private String startStnTdcsId;
	/**
	 * 始发节点TDCS对应名称.
	 */
	private String startStnTdcsName;
	/**
	 * 终到站ID.
	 */
	private String endStationStnId;
	/**
	 * 终到站名.
	 */
	private String endStationStnName;
	/**
	 * 终到节点TDCS对应标识.
	 */
	private String endStnTdcsId;
	/**
	 * 终到节点TDCS对应名称.
	 */
	private String endStnTdcsName;
   
    
    public String getLineReloadStatus() {
	return lineReloadStatus;
}

public void setLineReloadStatus(String lineReloadStatus) {
	this.lineReloadStatus = lineReloadStatus;
}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getSourceBureauId() {
		return sourceBureauId;
	}

	public void setSourceBureauId(String sourceBureauId) {
		this.sourceBureauId = sourceBureauId;
	}

	public String getSourceNodeId() {
		return sourceNodeId;
	}

	public void setSourceNodeId(String sourceNodeId) {
		this.sourceNodeId = sourceNodeId;
	}

	public String getTargetBureauId() {
		return targetBureauId;
	}

	public void setTargetBureauId(String targetBureauId) {
		this.targetBureauId = targetBureauId;
	}

	public String getTargetNodeId() {
		return targetNodeId;
	}

	public void setTargetNodeId(String targetNodeId) {
		this.targetNodeId = targetNodeId;
	}

	public String getTargetTimeScheduleDates() {
		return targetTimeScheduleDates;
	}

	public void setTargetTimeScheduleDates(String targetTimeScheduleDates) {
		this.targetTimeScheduleDates = targetTimeScheduleDates;
	}

	public String getCmdTrainId() {
		return cmdTrainId;
	}

	public void setCmdTrainId(String cmdTrainId) {
		this.cmdTrainId = cmdTrainId;
	}

	public String getCreatePeople() {
		return createPeople;
	}

	public void setCreatePeople(String createPeople) {
		this.createPeople = createPeople;
	}

	public String getCreatePeopleOrg() {
		return createPeopleOrg;
	}

	public void setCreatePeopleOrg(String createPeopleOrg) {
		this.createPeopleOrg = createPeopleOrg;
	}

	public String getTokenVehBureau() {
		return tokenVehBureau;
	}

	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}

	public String getPlanTrainId() {
        return planTrainId;
    }

    public void setPlanTrainId(String planTrainId) {
        this.planTrainId = planTrainId;
    }

    public String getPlanTrainSign() {
        return planTrainSign;
    }

    public void setPlanTrainSign(String planTrainSign) {
        this.planTrainSign = planTrainSign;
    }

    public String getPlanCrossId() {
        return planCrossId;
    }

    public void setPlanCrossId(String planCrossId) {
        this.planCrossId = planCrossId;
    }

    public String getPreTrainId() {
        return preTrainId;
    }

    public void setPreTrainId(String preTrainId) {
        this.preTrainId = preTrainId;
    }

    public String getNextTrainId() {
        return nextTrainId;
    }

    public void setNextTrainId(String nextTrainId) {
        this.nextTrainId = nextTrainId;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public String getTrainNbr() {
        return trainNbr;
    }

    public void setTrainNbr(String trainNbr) {
        this.trainNbr = trainNbr;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStartBureauShortName() {
        return startBureauShortName;
    }

    public void setStartBureauShortName(String startBureauShortName) {
        this.startBureauShortName = startBureauShortName;
    }

    public String getStartBureauFullName() {
        return startBureauFullName;
    }

    public void setStartBureauFullName(String startBureauFullName) {
        this.startBureauFullName = startBureauFullName;
    }

    public String getStartStn() {
        return startStn;
    }

    public void setStartStn(String startStn) {
        this.startStn = startStn;
    }

    public String getEndBureauShortName() {
        return endBureauShortName;
    }

    public void setEndBureauShortName(String endBureauShortName) {
        this.endBureauShortName = endBureauShortName;
    }

    public String getEndBureauFullName() {
        return endBureauFullName;
    }

    public void setEndBureauFullName(String endBureauFullName) {
        this.endBureauFullName = endBureauFullName;
    }

    public String getEndStn() {
        return endStn;
    }

    public void setEndStn(String endStn) {
        this.endStn = endStn;
    }

    public String getPassBureau() {
        return passBureau;
    }

    public void setPassBureau(String passBureau) {
        this.passBureau = passBureau;
    }

    public int getTrainScope() {
        return trainScope;
    }

    public void setTrainScope(int trainScope) {
        this.trainScope = trainScope;
    }

    public String getTrainTypeId() {
        return trainTypeId;
    }

    public void setTrainTypeId(String trainTypeId) {
        this.trainTypeId = trainTypeId;
    }

    public int getHighLineFlag() {
        return highLineFlag;
    }

    public void setHighLineFlag(int highLineFlag) {
        this.highLineFlag = highLineFlag;
    }

    public String getBaseChartId() {
        return baseChartId;
    }

    public void setBaseChartId(String baseChartId) {
        this.baseChartId = baseChartId;
    }

    public String getBaseTrainId() {
        return baseTrainId;
    }

    public void setBaseTrainId(String baseTrainId) {
        this.baseTrainId = baseTrainId;
    }

    public int getHightLineRule() {
        return hightLineRule;
    }

    public void setHightLineRule(int hightLineRule) {
        this.hightLineRule = hightLineRule;
    }

    public int getCommonLineRule() {
        return commonLineRule;
    }

    public void setCommonLineRule(int commonLineRule) {
        this.commonLineRule = commonLineRule;
    }

    public String getAppointWeek() {
        return appointWeek;
    }

    public void setAppointWeek(String appointWeek) {
        this.appointWeek = appointWeek;
    }

    public String getAppointDay() {
        return appointDay;
    }

    public void setAppointDay(String appointDay) {
        this.appointDay = appointDay;
    }

    public int getDayGap() {
        return dayGap;
    }

    public void setDayGap(int dayGap) {
        this.dayGap = dayGap;
    }

    public int getSpareFlag() {
        return spareFlag;
    }

    public void setSpareFlag(int spareFlag) {
        this.spareFlag = spareFlag;
    }

    public int getSpareApplyFlag() {
        return spareApplyFlag;
    }

    public void setSpareApplyFlag(int spareApplyFlag) {
        this.spareApplyFlag = spareApplyFlag;
    }

    public int getCreateType() {
        return createType;
    }

    public void setCreateType(int createType) {
        this.createType = createType;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public int getCheckLev1Type() {
        return checkLev1Type;
    }

    public void setCheckLev1Type(int checkLev1Type) {
        this.checkLev1Type = checkLev1Type;
    }

    public int getCheckLev2Type() {
        return checkLev2Type;
    }

    public void setCheckLev2Type(int checkLev2Type) {
        this.checkLev2Type = checkLev2Type;
    }

    public int getDailyPlanTimes() {
        return dailyPlanTimes;
    }

    public void setDailyPlanTimes(int dailyPlanTimes) {
        this.dailyPlanTimes = dailyPlanTimes;
    }

    public List<RunPlanStn> getRunPlanStnList() {
        return runPlanStnList;
    }

    public void setRunPlanStnList(List<RunPlanStn> runPlanStnList) {
        this.runPlanStnList = runPlanStnList;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getMarshallingName() {
        return marshallingName;
    }

    public void setMarshallingName(String marshallingName) {
        this.marshallingName = marshallingName;
    }

    public int getGroupSerialNbr() {
        return groupSerialNbr;
    }

    public void setGroupSerialNbr(int groupSerialNbr) {
        this.groupSerialNbr = groupSerialNbr;
    }

    public int getTrainSort() {
        return trainSort;
    }

    public void setTrainSort(int trainSort) {
        this.trainSort = trainSort;
    }

    public int getDailyPlanFlag() {
        return dailyPlanFlag;
    }

    public void setDailyPlanFlag(int dailyPlanFlag) {
        this.dailyPlanFlag = dailyPlanFlag;
    }

    public Date getDailyPlanTime() {
        return dailyPlanTime;
    }

    public void setDailyPlanTime(Date dailyPlanTime) {
        this.dailyPlanTime = dailyPlanTime;
    }

	public String getTelBureau() {
		return telBureau;
	}

	public void setTelBureau(String telBureau) {
		this.telBureau = telBureau;
	}

	public String getTelId() {
		return telId;
	}

	public void setTelId(String telId) {
		this.telId = telId;
	}

	public String getTelNum() {
		return telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public String getCmdBureau() {
		return cmdBureau;
	}

	public void setCmdBureau(String cmdBureau) {
		this.cmdBureau = cmdBureau;
	}

	

	public Integer getCmdTxtmlId() {
		return cmdTxtmlId;
	}

	public void setCmdTxtmlId(Integer cmdTxtmlId) {
		this.cmdTxtmlId = cmdTxtmlId;
	}

	public Integer getCmdTxtmlitemId() {
		return cmdTxtmlitemId;
	}

	public void setCmdTxtmlitemId(Integer cmdTxtmlitemId) {
		this.cmdTxtmlitemId = cmdTxtmlitemId;
	}

	public String getCmdShortInfo() {
		return cmdShortInfo;
	}

	public void setCmdShortInfo(String cmdShortInfo) {
		this.cmdShortInfo = cmdShortInfo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDailyPlanId() {
		return dailyPlanId;
	}

	public void setDailyPlanId(String dailyPlanId) {
		this.dailyPlanId = dailyPlanId;
	}

	public String getImportantFlag() {
		return importantFlag;
	}

	public void setImportantFlag(String importantFlag) {
		this.importantFlag = importantFlag;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getCheckBureau() {
		return checkBureau;
	}

	public void setCheckBureau(String checkBureau) {
		this.checkBureau = checkBureau;
	}

//	public String getStartStationStnId() {
//		return startStationStnId;
//	}
//
//	public void setStartStationStnId(String startStationStnId) {
//		this.startStationStnId = startStationStnId;
//	}
//
//	public String getStartStationStnName() {
//		return startStationStnName;
//	}
//
//	public void setStartStationStnName(String startStationStnName) {
//		this.startStationStnName = startStationStnName;
//	}
//
//	public String getStartStnTdcsId() {
//		return startStnTdcsId;
//	}
//
//	public void setStartStnTdcsId(String startStnTdcsId) {
//		this.startStnTdcsId = startStnTdcsId;
//	}
//
//	public String getStartStnTdcsName() {
//		return startStnTdcsName;
//	}
//
//	public void setStartStnTdcsName(String startStnTdcsName) {
//		this.startStnTdcsName = startStnTdcsName;
//	}
//
//	public String getEndStationStnId() {
//		return endStationStnId;
//	}
//
//	public void setEndStationStnId(String endStationStnId) {
//		this.endStationStnId = endStationStnId;
//	}
//
//	public String getEndStationStnName() {
//		return endStationStnName;
//	}
//
//	public void setEndStationStnName(String endStationStnName) {
//		this.endStationStnName = endStationStnName;
//	}
//
//	public String getEndStnTdcsId() {
//		return endStnTdcsId;
//	}
//
//	public void setEndStnTdcsId(String endStnTdcsId) {
//		this.endStnTdcsId = endStnTdcsId;
//	}
//
//	public String getEndStnTdcsName() {
//		return endStnTdcsName;
//	}
//
//	public void setEndStnTdcsName(String endStnTdcsName) {
//		this.endStnTdcsName = endStnTdcsName;
//	}

}
