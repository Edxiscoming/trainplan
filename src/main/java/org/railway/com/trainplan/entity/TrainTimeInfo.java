package org.railway.com.trainplan.entity;

import org.railway.com.trainplan.common.utils.StringUtil;

public class TrainTimeInfo {
	
	
	private int rn;
	private String trainlineTempId;
	 private String planTrainStnId;
	 private int childIndex;
	 private String stnName;
	 private String bureauShortName;
	 private String trackName;
	 private String arrTime;
	 private String dptTime;
	 
	 private String arrTimeAll;
	 private String dptTimeAll;
	 
	 private int runDays; 
	 private String stnBureauFull;
	 private String stationFlag;//SFZ:始发站   ZDZ：终到站  FJK：分界口   BTZ：不停站  TZ：停站
	 private String nodeId;
	 private int sourceDay;
	 private int targetDay;
	 private String jobs;//<始发>	<经由><终到>
	 private String nodeName;
	 private String bureauId;//车站所属局ID
	 
	 //2014-09-15 增加stnSort属性   用于批量修改
	 private int stnSort;
	 private int arrRunDays;//到达运行天数
	 
	 //2014-10-27增加以下属性
	 private String arrTrainNbr;//到达车次
	 private String dptTrainNbr;//出发车次
	 private Integer platForm;//站台
	 private String baseArrTime;
	 private String baseDptTime;
	 private String stnType;//车站类型（1:始发站；2:终到站；4:分界口）
	 private String planTrainId;//列车ID
	//2015-1-5增加以下属性 suntao
    private String nodeStationId;
    private String nodeStationName;
    private String nodeTdcsId;
    private String nodeTdcsName;
	 //2015-03-04 
    private String kyyy;//客运营业
	
	public String getNodeStationId() {
		return nodeStationId;
	}
	public void setNodeStationId(String nodeStationId) {
		this.nodeStationId = nodeStationId;
	}
	public String getNodeStationName() {
		return nodeStationName;
	}
	public void setNodeStationName(String nodeStationName) {
		this.nodeStationName = nodeStationName;
	}
	public String getNodeTdcsId() {
		return nodeTdcsId;
	}
	public void setNodeTdcsId(String nodeTdcsId) {
		this.nodeTdcsId = nodeTdcsId;
	}
	public String getNodeTdcsName() {
		return nodeTdcsName;
	}
	public void setNodeTdcsName(String nodeTdcsName) {
		this.nodeTdcsName = nodeTdcsName;
	}
	public String getJobs() {
		return jobs;
	}
	public void setJobs(String jobs) {
		this.jobs = jobs;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getBureauId() {
		return bureauId;
	}
	public void setBureauId(String bureauId) {
		this.bureauId = bureauId;
	}
	public int getSourceDay() {
		return sourceDay;
	}
	public void setSourceDay(int sourceDay) {
		this.sourceDay = sourceDay;
	}
	public int getTargetDay() {
		return targetDay;
	}
	public void setTargetDay(int targetDay) {
		this.targetDay = targetDay;
	}
	public String getPlanTrainStnId() {
		return planTrainStnId;
	}
	public void setPlanTrainStnId(String planTrainStnId) {
		this.planTrainStnId = planTrainStnId;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getStnBureauFull() {
		return stnBureauFull;
	}
	public void setStnBureauFull(String stnBureauFull) {
		this.stnBureauFull = stnBureauFull;
	}
	public String getStationFlag() {
		return stationFlag;
	}
	public void setStationFlag(String stationFlag) {
		this.stationFlag = stationFlag;
	}
	public int getChildIndex() {
		return childIndex;
	}
	public void setChildIndex(int childIndex) {
		this.childIndex = childIndex;
	}
	public String getStnName() {
		return stnName;
	}
	public void setStnName(String stnName) {
		this.stnName = stnName;
	}
	public String getBureauShortName() {
		return bureauShortName;
	}
	public void setBureauShortName(String bureauShortName) {
		this.bureauShortName = bureauShortName;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getArrTime() {
		return StringUtil.isEmpty(arrTime)?"":arrTime;
	}
	public void setArrTime(String arrTime) {
		this.arrTime = arrTime;
	}
	public String getDptTime() {
		return StringUtil.isEmpty(dptTime)?"":dptTime;
	}
	public void setDptTime(String dptTime) {
		this.dptTime = dptTime;
	}
	public int getRunDays() {
		return runDays;
	}
	public void setRunDays(int runDays) {
		this.runDays = runDays;
	}
	public int getStnSort() {
		return stnSort;
	}
	public void setStnSort(int stnSort) {
		this.stnSort = stnSort;
	}
	public int getArrRunDays() {
		return arrRunDays;
	}
	public void setArrRunDays(int arrRunDays) {
		this.arrRunDays = arrRunDays;
	}
	public String getArrTrainNbr() {
		return StringUtil.isEmpty(arrTrainNbr)?"":arrTrainNbr;
	}
	public void setArrTrainNbr(String arrTrainNbr) {
		this.arrTrainNbr = arrTrainNbr;
	}
	public String getDptTrainNbr() {
		return dptTrainNbr;
	}
	public void setDptTrainNbr(String dptTrainNbr) {
		this.dptTrainNbr = StringUtil.isEmpty(dptTrainNbr)?"":dptTrainNbr;
	}
	public String getBaseArrTime() {
		return StringUtil.isEmpty(baseArrTime)?"":baseArrTime;
	}
	public void setBaseArrTime(String baseArrTime) {
		this.baseArrTime = baseArrTime;
	}
	public String getBaseDptTime() {
		return StringUtil.isEmpty(baseDptTime)?"":baseDptTime;
	}
	public void setBaseDptTime(String baseDptTime) {
		this.baseDptTime = baseDptTime;
	}
	public String getStnType() {
		return stnType;
	}
	public void setStnType(String stnType) {
		this.stnType = stnType;
	}
	
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	@Override
	public String toString() {
		return "TrainTimeInfo [planTrainStnId=" + planTrainStnId
				+ ", childIndex=" + childIndex + ", stnName=" + stnName
				+ ", bureauShortName=" + bureauShortName + ", trackName="
				+ trackName + ", arrTime=" + arrTime + ", dptTime=" + dptTime
				+ ", runDays=" + runDays + ", stnBureauFull=" + stnBureauFull
				+ ", stationFlag=" + stationFlag + ", nodeId=" + nodeId
				+ ", sourceDay=" + sourceDay + ", targetDay=" + targetDay
				+ ", jobs=" + jobs + ", nodeName=" + nodeName + ", bureauId="
				+ bureauId + ", stnSort=" + stnSort + ", arrRunDays="
				+ arrRunDays + ", arrTrainNbr=" + arrTrainNbr
				+ ", dptTrainNbr=" + dptTrainNbr + ", platForm=" + platForm
				+ ", baseArrTime=" + baseArrTime + ", baseDptTime="
				+ baseDptTime + ", stnType=" + stnType + "]";
	}
	public String getArrTimeAll() {
		return arrTimeAll;
	}
	public void setArrTimeAll(String arrTimeAll) {
		this.arrTimeAll = arrTimeAll;
	}
	public String getDptTimeAll() {
		return dptTimeAll;
	}
	public void setDptTimeAll(String dptTimeAll) {
		this.dptTimeAll = dptTimeAll;
	}
	public String getTrainlineTempId() {
		return trainlineTempId;
	}
	public void setTrainlineTempId(String trainlineTempId) {
		this.trainlineTempId = trainlineTempId;
	}
	public int getRn() {
		return rn;
	}
	public void setRn(int rn) {
		this.rn = rn;
	}
	public String getKyyy() {
		return kyyy;
	}
	public void setKyyy(String kyyy) {
		this.kyyy = kyyy;
	}
	public Integer getPlatForm() {
		return platForm;
	}
	public void setPlatForm(Integer platForm) {
		this.platForm = platForm;
	}

}

