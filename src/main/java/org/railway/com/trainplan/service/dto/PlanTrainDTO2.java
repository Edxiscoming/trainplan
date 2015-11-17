package org.railway.com.trainplan.service.dto;

import java.io.Serializable;
import java.util.List;

public class PlanTrainDTO2 implements Serializable {
	public String getCheckedBureau() {
		return checkedBureau;
	}

	public void setCheckedBureau(String checkedBureau) {
		this.checkedBureau = checkedBureau;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 列车id
	private String planTrainId;// ID;
	private String trainNbr;// 车次;
	private String startStn;// 始发站;
	private String endStn;// 终到站;
	private String runDate;// 开行日期
	private String spareFlag;// 发行状态

	// 2014-09-17增加以下属性，用于高铁计划查询
	private String startBureau;// 始发局简称
	private String startTime;// 始发时间yyyy-MM-dd HH:mm:ss
	private String endBureau;// 终到局简称
	private String endTime;// 终到时间yyyy-MM-dd HH:mm:ss
	private String endDays;// 终到运行天数
	private String passBureau;// 途经局（按顺序列出途经路局简称）
	private String planCrossId;// 交路ID（对应PLAN_CROSS中的交路ID）
	private String runType;// 运行方式 (始发交出 ：接入终到 ：接入交出 ：始发终到)
	private String runTypeCode;// 运行方式 (SFJC：始发交出 JRZD：接入终到 JRJC：接入交出 SFZD：始发终到)
	private String creatType;// "创建方式 （0:基本图；1:基本图滚动；2:文件电报；3:命令）"
	private String trainTypeShortName;// 列车类型
	private String checkLev1;// 审核1
	private String checkLev2;// 审核2
	private String dailyLineId;
	private String dailyLineFlag;
	private String highlineFlag;
	private String checkedBureau;//落成计划日常审核
	private int vaildStatus;

	// private String planTrainStnId;
	// private String stnName;
	// private String arrTime;
	// private String dptTime;
	// private String stnSort;
	// private String stnBureau;
	// private String isfdz;
	// private String isfjk;
	// 2014-09-26增加以下属性，用于高铁计划查询 统计接入、交出时间
	private int fjkCount; // 列车经由点单 分界口总个数
	private int otherFjkCount; // 列车经由点单 分界口总个数(不包含发到站同时为分界口的)

	private List<PlanTrainStnDto> planTrainStnList;// 列车经由时刻集合（只包含发到站、分界口）
	/*








	*/

	/**
	 * 运行线审核局（简称）.
	 */
	private String dailyplan_check_bureau;
	
	/**
	 * 备用参数.
	 */
	private String str1;
	
	/**
	 * 
	 */
	private String cmdTrainId;
	/**
	 * 
	 */
	private String telId;

	public String getPlanTrainId() {
		return planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	public String getTrainNbr() {
		return trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
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

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getStartBureau() {
		return startBureau;
	}

	public void setStartBureau(String startBureau) {
		this.startBureau = startBureau;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndBureau() {
		return endBureau;
	}

	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndDays() {
		return endDays;
	}

	public void setEndDays(String endDays) {
		this.endDays = endDays;
	}

	public String getPassBureau() {
		return passBureau;
	}

	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}

	public String getPlanCrossId() {
		return planCrossId;
	}

	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public List<PlanTrainStnDto> getPlanTrainStnList() {
		return planTrainStnList;
	}

	public void setPlanTrainStnList(List<PlanTrainStnDto> planTrainStnList) {
		this.planTrainStnList = planTrainStnList;
	}

	public String getRunTypeCode() {
		return runTypeCode;
	}

	public void setRunTypeCode(String runTypeCode) {
		this.runTypeCode = runTypeCode;
	}

	public String getCreatType() {
		return creatType;
	}

	public void setCreatType(String creatType) {
		this.creatType = creatType;
	}

	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}

	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}

	public int getFjkCount() {
		int _count = 0;
		if (planTrainStnList != null && planTrainStnList.size() > 0) {
			for (PlanTrainStnDto obj : planTrainStnList) {
				if ("1".equals(obj.getIsfjk())) {
					_count++;
				}
			}
		}
		this.setFjkCount(_count);
		return fjkCount;
	}

	public void setFjkCount(int fjkCount) {
		this.fjkCount = fjkCount;
	}

	public int getOtherFjkCount() {
		int _count = 0;
		if (planTrainStnList != null && planTrainStnList.size() > 0) {
			for (PlanTrainStnDto obj : planTrainStnList) {
				if ("1".equals(obj.getIsfjk()) && !"SFZ".equals(obj.getIsfdz())
						&& !"ZDZ".equals(obj.getIsfdz())) {
					_count++;
				}
			}
		}
		this.setOtherFjkCount(_count);
		return otherFjkCount;
	}

	public void setOtherFjkCount(int otherFjkCount) {
		this.otherFjkCount = otherFjkCount;
	}

	public String getSpareFlag() {
		return spareFlag;
	}

	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}

	/*
	 * public String getPlanTrainStnId() { return planTrainStnId; } public void
	 * setPlanTrainStnId(String planTrainStnId) { this.planTrainStnId =
	 * planTrainStnId; } public String getStnName() { return stnName; } public
	 * void setStnName(String stnName) { this.stnName = stnName; } public String
	 * getArrTime() { return arrTime; } public void setArrTime(String arrTime) {
	 * this.arrTime = arrTime; } public String getDptTime() { return dptTime; }
	 * public void setDptTime(String dptTime) { this.dptTime = dptTime; } public
	 * String getStnSort() { return stnSort; } public void setStnSort(String
	 * stnSort) { this.stnSort = stnSort; } public String getStnBureau() {
	 * return stnBureau; } public void setStnBureau(String stnBureau) {
	 * this.stnBureau = stnBureau; } public String getIsfdz() { return isfdz; }
	 * public void setIsfdz(String isfdz) { this.isfdz = isfdz; } public String
	 * getIsfjk() { return isfjk; } public void setIsfjk(String isfjk) {
	 * this.isfjk = isfjk; }
	 */
	public String getDailyLineId() {
		return dailyLineId;
	}

	public void setDailyLineId(String dailyLineId) {
		this.dailyLineId = dailyLineId;
	}

	public String getDailyLineFlag() {
		return dailyLineFlag;
	}

	public void setDailyLineFlag(String dailyLineFlag) {
		this.dailyLineFlag = dailyLineFlag;
	}

	public String getCheckLev1() {
		return checkLev1;
	}

	public void setCheckLev1(String checkLev1) {
		this.checkLev1 = checkLev1;
	}

	public String getCheckLev2() {
		return checkLev2;
	}

	public void setCheckLev2(String checkLev2) {
		this.checkLev2 = checkLev2;
	}

	public String getHighlineFlag() {
		return highlineFlag;
	}

	public void setHighlineFlag(String highlineFlag) {
		this.highlineFlag = highlineFlag;
	}

	public String getDailyplan_check_bureau() {
		return dailyplan_check_bureau;
	}

	public void setDailyplan_check_bureau(String dailyplan_check_bureau) {
		this.dailyplan_check_bureau = dailyplan_check_bureau;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public int getVaildStatus() {
		return vaildStatus;
	}

	public void setVaildStatus(int vaildStatus) {
		this.vaildStatus = vaildStatus;
	}

	public String getCmdTrainId() {
		return cmdTrainId;
	}

	public void setCmdTrainId(String cmdTrainId) {
		this.cmdTrainId = cmdTrainId;
	}

	public String getTelId() {
		return telId;
	}

	public void setTelId(String telId) {
		this.telId = telId;
	}

}
