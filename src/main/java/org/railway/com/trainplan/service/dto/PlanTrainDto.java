package org.railway.com.trainplan.service.dto;

import java.io.Serializable;
import java.util.List;

public class PlanTrainDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//列车id
	private String planTrainId;//ID;
	private String trainNbr;//车次;
	private String startStn;//始发站;
	private String endStn;//终到站;
	private String runDate;//开行日期
	private String telName;
	
	
	
	//2014-09-17增加以下属性，用于高铁计划查询
	private String startBureau;//始发局简称
	private String startTime;//始发时间yyyy-MM-dd HH:mm:ss
	private String endBureau;//终到局简称
	private String endTime;//终到时间yyyy-MM-dd HH:mm:ss
	private String endDays;//终到运行天数
	private String passBureau;//途经局（按顺序列出途经路局简称）
	private String planCrossId;//交路ID（对应PLAN_CROSS中的交路ID）
	private String runType;//运行方式 (始发交出  ：接入终到  ：接入交出  ：始发终到)
	private String runTypeCode;//运行方式 (SFJC：始发交出  JRZD：接入终到  JRJC：接入交出  SFZD：始发终到)
	private String creatType;//"创建方式 （0:基本图；1:基本图滚动；2:文件电报；3:命令）"
	private String trainTypeShortName;//列车类型
	
	//2014-09-26增加以下属性，用于高铁计划查询  统计接入、交出时间
	private int fjkCount;	//列车经由点单 分界口总个数
	private int otherFjkCount;	//列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	
	//2014-10-27增加属性
	private String maxRunDate;//该车次在开行计划数据表中最大开行日期（格式：yyyymmdd）
	private String baseTrainId;//基本图中列车ID
	
	private List<PlanTrainStnDto> planTrainStnList;//列车经由时刻集合（只包含发到站、分界口）
	
	private String highLineFlag;
	
	
	private String  laiyuan;
	private String  spareFlagTxt;
	
	private String day;
	private String groupSerialNbr;
	
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
	public String getTelName() {
		return telName;
	}
	public void setTelName(String telName) {
		this.telName = telName;
	}
	
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
		if (planTrainStnList!=null && planTrainStnList.size()>0) {
			for (PlanTrainStnDto obj :planTrainStnList) {
				if ("1".equals(obj.getIsfjk())) {
					_count ++ ;
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
		if (planTrainStnList!=null && planTrainStnList.size()>0) {
			for (PlanTrainStnDto obj :planTrainStnList) {
				if ("1".equals(obj.getIsfjk()) && !"SFZ".equals(obj.getIsfdz()) && !"ZDZ".equals(obj.getIsfdz())) {
					_count ++ ;
				}
			}
		}
		this.setOtherFjkCount(_count);
		return otherFjkCount;
	}
	public void setOtherFjkCount(int otherFjkCount) {
		this.otherFjkCount = otherFjkCount;
	}
	public String getMaxRunDate() {
		return maxRunDate;
	}
	public void setMaxRunDate(String maxRunDate) {
		this.maxRunDate = maxRunDate;
	}
	public String getBaseTrainId() {
		return baseTrainId;
	}
	public void setBaseTrainId(String baseTrainId) {
		this.baseTrainId = baseTrainId;
	}
	public String getHighLineFlag() {
		return highLineFlag;
	}
	public void setHighLineFlag(String highLineFlag) {
		this.highLineFlag = highLineFlag;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(String groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}
	
	
	
	
}

