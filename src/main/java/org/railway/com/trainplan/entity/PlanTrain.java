package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;

//@Entity
//@Table(name = "PLAN_TRAIN")
public class PlanTrain implements Serializable {
	// select PLAN_TRAIN_ID as planTrainId,TRAIN_NBR as trainNbr,START_STN as
	// startStn,
	// END_STN as endStn,RUN_DATE as runDate,START_TIME as startTime,
	// END_TIME as endTime,BASE_CHART_ID as baseChartId,BASE_TRAIN_ID as
	// baseTrainId,
	// START_BUREAU as startBureau,END_BUREAU as endBureau,END_BUREAU_FULL as
	// endBureauFull,
	// START_BUREAU_FULL as startBureauFull from plan_train where
	// run_date=#{runDate} and START_BUREAU_FULL=#{startBureauFull}
	// order by TRAIN_NBR desc

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 列车id
	private String planTrainId;// PLAN_TRAIN_ID;
	// 车次
	private String trainNbr;// TRAIN_NBR;
	private int trainSort;
	// 始发站
	private String startStn;// START_STN;
	// 终点站
	private String endStn;// END_STN;
	// 开行日期
	private String runDate;
	private Date startTime;
	private Date endTime;
	// 基本方案id
	private String baseChartId;
	// 基本图列车id
	private String baseTrainId;

	private String startBureau;// 始发局局码
	private String startBureauShortName;
	private String routingBureauShortName;
	private int relativeTargetTimeDay;
	private String endBureau;// 终到局局码
	private String endBureauFull;// 终到局全称
	private String startTimeStr;
	private String endTimeStr;
	private String sourceTime;
	private String targetTime;

	private String groupSerialNbr;

	private String startBureauFull;// 始发局全称
	private String jylj;// 经由路局 如：“京,济,上”
	// 列车种类：货运，客运
	private String operation;
	/***** 新加的字段 *****/
	private String sourceBureauId;
	private String sourceNodeId;
	private String targetBureauId;
	private String targetNodeId;
	private String targetTimeScheduleDates;
	private String trainTypeId;
	private String routeId;
	/*****************/

	//
	// t2.check_lev1_bureau as checklev1breau,
	// case
	// when t2.dailyplan_id is null
	// then 0
	// when t2.dailyplan_id is not null
	// then 1
	// end as redundance
	private String checklev1breau;
	private String redundance;

	private String highline_flag;

	// 到达天数 出发天数

	// t.source_time_schedule_dates
	// t.target_time_schedule_dates
	private String source_time_schedule_dates;
	private String target_time_schedule_dates;

	/**
	 * 始发站ID.
	 */
	private String sourceNodeStationId;
	/**
	 * 始发站名.
	 */
	private String sourceNodeStationName;
	/**
	 * 始发节点TDCS对应标识.
	 */
	private String sourceNodeTdcsId;
	/**
	 * 始发节点TDCS对应名称.
	 */
	private String sourceNodeTdcsName;
	/**
	 * 终到站ID.
	 */
	private String targetNodeStationId;
	/**
	 * 终到站名.
	 */
	private String targetNodeStationName;
	/**
	 * 终到节点TDCS对应标识.
	 */
	private String targetNodeTdcsId;
	/**
	 * 终到节点TDCS对应名称.
	 */
	private String targetNodeTdcsName;

	/**
	 * 相关局.
	 */
	private String passBureau;
	
	 /**
     * plantrain对应的逻辑交路车次id
     */
    private String cmTrainId;

	public String getSource_time_schedule_dates() {
		return source_time_schedule_dates;
	}

	public void setSource_time_schedule_dates(String source_time_schedule_dates) {
		this.source_time_schedule_dates = source_time_schedule_dates;
	}

	public String getTarget_time_schedule_dates() {
		return target_time_schedule_dates;
	}

	public void setTarget_time_schedule_dates(String target_time_schedule_dates) {
		this.target_time_schedule_dates = target_time_schedule_dates;
	}

	public String getHighline_flag() {
		return highline_flag;
	}

	public void setHighline_flag(String highline_flag) {
		this.highline_flag = highline_flag;
	}

	public String getChecklev1breau() {
		return checklev1breau;
	}

	public void setChecklev1breau(String checklev1breau) {
		this.checklev1breau = checklev1breau;
	}

	public String getRedundance() {
		return redundance;
	}

	public void setRedundance(String redundance) {
		this.redundance = redundance;
	}

	public int getTrainSort() {
		return trainSort;
	}

	public String getTrainTypeId() {
		return trainTypeId;
	}

	public void setTrainTypeId(String trainTypeId) {
		this.trainTypeId = trainTypeId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setTrainSort(int trainSort) {
		this.trainSort = trainSort;
	}

	public String getGroupSerialNbr() {
		return groupSerialNbr;
	}

	public void setGroupSerialNbr(String groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}

	public String getRoutingBureauShortName() {
		return routingBureauShortName;
	}

	public void setRoutingBureauShortName(String routingBureauShortName) {
		this.routingBureauShortName = routingBureauShortName;
	}

	public int getRelativeTargetTimeDay() {
		return relativeTargetTimeDay;
	}

	public void setRelativeTargetTimeDay(int relativeTargetTimeDay) {
		this.relativeTargetTimeDay = relativeTargetTimeDay;
	}

	private String endBreauShortName;

	public String getStartBureauShortName() {
		return startBureauShortName;
	}

	public void setStartBureauShortName(String startBureauShortName) {
		this.startBureauShortName = startBureauShortName;
	}

	public String getEndBreauShortName() {
		return endBreauShortName;
	}

	public void setEndBreauShortName(String endBreauShortName) {
		this.endBreauShortName = endBreauShortName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	// @Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE)
	// @Column(name = "PLAN_TRAIN_ID", unique = true, nullable = false)
	public String getPlanTrainId() {
		return planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	// select id as planTrainId,name as trainNbr,source_node_name as startStn,
	// source_bureau_shortname as startBureauShortName, target_bureau_shortname
	// as endBreauShortName,source_time,target_node_name,target_time from
	// jhpt_rjh.m_trainlinetemp t where t.scheme_id =
	// 'b4264afd-7755-48ba-9cf7-b31eeac6e085' AND NAME='G11' order by name
	// @Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	// @Column(name = "START_BUREAU")
	public String getStartBureau() {
		return startBureau;
	}

	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}

	// @Column(name = "START_BUREAU_FULL")
	public String getStartBureauFull() {
		return startBureauFull;
	}

	public void setStartBureauFull(String startBureauFull) {
		this.startBureauFull = startBureauFull;
	}

	// @Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	// @Column(name = "BASE_CHART_ID")
	public String getBaseChartId() {
		return baseChartId;
	}

	public void setBaseChartId(String baseChartId) {
		this.baseChartId = baseChartId;
	}

	// @Column(name = "BASE_TRAIN_ID")
	public String getBaseTrainId() {
		return baseTrainId;
	}

	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}

	// @Column(name = "TRAIN_NBR")
	public String getTrainNbr() {
		return trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}

	// @Column(name = "START_STN")
	public String getStartStn() {
		return startStn;
	}

	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}

	// @Column(name = "END_STN")
	public String getEndStn() {
		return endStn;
	}

	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}

	// @Column(name = "RUN_DATE")
	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	// @Column(name = "END_BUREAU")
	public String getEndBureau() {
		return endBureau;
	}

	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}

	// @Column(name = "END_BUREAU_FULL")
	public String getEndBureauFull() {
		return endBureauFull;
	}

	public void setEndBureauFull(String endBureauFull) {
		this.endBureauFull = endBureauFull;
	}

	// @Transient
	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getSourceTime() {
		return sourceTime;
	}

	public void setSourceTime(String sourceTime) {
		this.sourceTime = sourceTime;
	}

	public String getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}

	// @Transient
	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	// @Transient
	public String getJylj() {
		return jylj;
	}

	public void setJylj(String jylj) {
		this.jylj = jylj;
	}

	public String getSourceNodeStationId() {
		return sourceNodeStationId;
	}

	public void setSourceNodeStationId(String sourceNodeStationId) {
		this.sourceNodeStationId = sourceNodeStationId;
	}

	public String getSourceNodeStationName() {
		return sourceNodeStationName;
	}

	public void setSourceNodeStationName(String sourceNodeStationName) {
		this.sourceNodeStationName = sourceNodeStationName;
	}

	public String getSourceNodeTdcsId() {
		return sourceNodeTdcsId;
	}

	public void setSourceNodeTdcsId(String sourceNodeTdcsId) {
		this.sourceNodeTdcsId = sourceNodeTdcsId;
	}

	public String getSourceNodeTdcsName() {
		return sourceNodeTdcsName;
	}

	public void setSourceNodeTdcsName(String sourceNodeTdcsName) {
		this.sourceNodeTdcsName = sourceNodeTdcsName;
	}

	public String getTargetNodeStationId() {
		return targetNodeStationId;
	}

	public void setTargetNodeStationId(String targetNodeStationId) {
		this.targetNodeStationId = targetNodeStationId;
	}

	public String getTargetNodeStationName() {
		return targetNodeStationName;
	}

	public void setTargetNodeStationName(String targetNodeStationName) {
		this.targetNodeStationName = targetNodeStationName;
	}

	public String getTargetNodeTdcsId() {
		return targetNodeTdcsId;
	}

	public void setTargetNodeTdcsId(String targetNodeTdcsId) {
		this.targetNodeTdcsId = targetNodeTdcsId;
	}

	public String getTargetNodeTdcsName() {
		return targetNodeTdcsName;
	}

	public void setTargetNodeTdcsName(String targetNodeTdcsName) {
		this.targetNodeTdcsName = targetNodeTdcsName;
	}

	public String getPassBureau() {
		return passBureau;
	}

	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}

	public String getCmTrainId() {
		return cmTrainId;
	}

	public void setCmTrainId(String cmTrainId) {
		this.cmTrainId = cmTrainId;
	}

}
