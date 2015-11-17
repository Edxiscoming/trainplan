package org.railway.com.trainplan.service.dto;


public class HighLinePlanTrainDto {
	//列车id
	private String planTrainId;//ID;
	private String runType;//运行方式 (SFJC：始发交出  JRZD：接入终到  JRJC：接入交出  SFZD：始发终到)
	private String trainNbr;//车次;
	private String startStn;//始发站;
	private String startTime;//始发时间yyyy-MM-dd HH:mm:ss
	private String jrStn;//接入站;
	private String jrTime;//接入时间yyyy-MM-dd HH:mm:ss
	private String jcStn;//接出站;
	private String jcTime;//接出时间yyyy-MM-dd HH:mm:ss
	private String endStn;//终到站;
	private String endTime;//终到时间yyyy-MM-dd HH:mm:ss
	private String endDays;//终到运行天数
	private String passBureau;//途经局（按顺序列出途经路局简称）
	private String trainTypeShortName;//列车类型
	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	//以下为非界面显示元素
	private String runDate;//开行日期
	private String startBureau;//始发局简称
	private String endBureau;//终到局简称
	private String planCrossId;//交路ID（对应PLAN_CROSS中的交路ID）
	private String creatType;//"创建方式  TD（0:基本图；1:基本图滚动)；LK(2:文件电报；3:命令）"
	private int fjkCount;	//列车经由点单 分界口总个数
	
	private String laiyuan;
	private String spareFlagTxt;
	
	public String getSpareFlagTxt() {
		return spareFlagTxt;
	}
	public void setSpareFlagTxt(String spareFlagTxt) {
		this.spareFlagTxt = spareFlagTxt;
	}
	public String getLaiyuan() {
		return laiyuan;
	}
	public void setLaiyuan(String laiyuan) {
		this.laiyuan = laiyuan;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getRunType() {
		return runType;
	}
	public void setRunType(String runType) {
		this.runType = runType;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getJrStn() {
		return jrStn;
	}
	public void setJrStn(String jrStn) {
		this.jrStn = jrStn;
	}
	public String getJrTime() {
		return jrTime;
	}
	public void setJrTime(String jrTime) {
		this.jrTime = jrTime;
	}
	public String getJcStn() {
		return jcStn;
	}
	public void setJcStn(String jcStn) {
		this.jcStn = jcStn;
	}
	public String getJcTime() {
		return jcTime;
	}
	public void setJcTime(String jcTime) {
		this.jcTime = jcTime;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
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
	public String getEndBureau() {
		return endBureau;
	}
	public void setEndBureau(String endBureau) {
		this.endBureau = endBureau;
	}
	public String getPlanCrossId() {
		return planCrossId;
	}
	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}
	public String getCreatType() {
		return creatType;
	}
	public void setCreatType(String creatType) {
		this.creatType = creatType;
	}
	public int getFjkCount() {
		return fjkCount;
	}
	public void setFjkCount(int fjkCount) {
		this.fjkCount = fjkCount;
	}
	@Override
	public String toString() {
		return "HighLinePlanTrainDto [planTrainId=" + ""
				+ ", runType=" + runType + ", trainNbr=" + trainNbr
				+ ", startStn=" + startStn + ", startTime=" + startTime
				+ ", jrStn=" + jrStn + ", jrTime=" + jrTime + ", jcStn="
				+ jcStn + ", jcTime=" + jcTime + ", endStn=" + endStn
				+ ", endTime=" + endTime + ", endDays=" + endDays
				+ ", passBureau=" + passBureau + ", runDate=" + runDate
				+ ", startBureau=" + startBureau + ", endBureau=" + endBureau
				+ ", planCrossId=" + planCrossId + ", creatType=" + creatType
				+ ", fjkCount=" + fjkCount + ", laiyuan=" + laiyuan + "]";
	}
	
	
	
	
}
