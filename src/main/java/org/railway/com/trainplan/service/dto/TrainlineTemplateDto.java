package org.railway.com.trainplan.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基本图方案包含的基本图运行线对象
 * @author join
 *
 */
public class TrainlineTemplateDto {
	
	private List<TrainlineTemplateSubDto> stationList = new ArrayList<TrainlineTemplateSubDto>();
	//列车id
	private String planTrainId ;
	//列车全路统一标识
	private String planTrainSign;
	//交路ID	
	private String planCrossId;
	//途经局	
	private String passBureau;
	//列车范围	TRAIN_SCOPE	NUMBER(1),根据途径局计算。1:直通；0:管内
	private int trainScope;
	//列车类型ID	TRAIN_TYPE_ID	VARCHAR2(36)
	private String trainTypeId;
	//创建方式	CREAT_TYPE	NUMBER(1)
	private int createType;
	//创建时间
	private String createTime;
	//生成运行线标记
	private String dailyPlanFlag;
	//生成运行线次数
	private int dailyPlanTimes;
	//生成运行线时间
	private String dailyPlanTime;
	//运行线ID
	private String dailyPlanId;
   //开行日期
	private String runDate;
	//yyyyMMdd格式
	private String runDate_8;
	//车次
	private String trainNbr;
	//始发时间
	private String startTime ;
	//终到时间
	private String endTime;
    //始发站名
	private String startStn;
	//终到站名
	private String endStn;
	//基本图案id
	private String baseChartId;
	//基本图列车id
	private String baseTrainId;
	//始发局全称
	private String startBureauFull;
	//终到局全称
	private String endBureauFull;
	private String startBureau;
	private String endBureau;
	
	//列车的整个运行天数，来自报文中终到站的day
	private int rundays;
	
	private Map<String,Object> scheduleMap;
	
	
	
	public String getStartBureau() {
		return startBureau;
	}
	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}
	public String getEndBureau() {
		return endBureau;
	}
	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
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
	public int getCreateType() {
		return createType;
	}
	public void setCreateType(int createType) {
		this.createType = createType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDailyPlanFlag() {
		return dailyPlanFlag;
	}
	public void setDailyPlanFlag(String dailyPlanFlag) {
		this.dailyPlanFlag = dailyPlanFlag;
	}
	public int getDailyPlanTimes() {
		return dailyPlanTimes;
	}
	public void setDailyPlanTimes(int dailyPlanTimes) {
		this.dailyPlanTimes = dailyPlanTimes;
	}
	public String getDailyPlanTime() {
		return dailyPlanTime;
	}
	public void setDailyPlanTime(String dailyPlanTime) {
		this.dailyPlanTime = dailyPlanTime;
	}
	public String getDailyPlanId() {
		return dailyPlanId;
	}
	public void setDailyPlanId(String dailyPlanId) {
		this.dailyPlanId = dailyPlanId;
	}
	public String getRunDate_8() {
		return runDate_8;
	}
	public void setRunDate_8(String runDate_8) {
		this.runDate_8 = runDate_8;
	}
	public Map<String, Object> getScheduleMap() {
		return scheduleMap;
	}
	public void setScheduleMap(Map<String, Object> scheduleMap) {
		this.scheduleMap = scheduleMap;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	
	public List<TrainlineTemplateSubDto> getStationList() {
		return stationList;
	}
	public void setStationList(List<TrainlineTemplateSubDto> stationList) {
		this.stationList = stationList;
	}
	public String getRunDate() {
		return runDate;
	}
	
	
	public String getStartBureauFull() {
		return startBureauFull;
	}
	public void setStartBureauFull(String startBureauFull) {
		this.startBureauFull = startBureauFull;
	}
	public String getEndBureauFull() {
		return endBureauFull;
	}
	public void setEndBureauFull(String endBureauFull) {
		this.endBureauFull = endBureauFull;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
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
	public int getRundays() {
		return rundays;
	}
	public void setRundays(int rundays) {
		this.rundays = rundays;
	}
	
	
	
}
