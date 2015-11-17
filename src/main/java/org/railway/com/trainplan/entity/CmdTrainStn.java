package org.railway.com.trainplan.entity;

public class CmdTrainStn {

	//临客命令时刻ID
	private String cmdTrainStnId;
	//临客命令列车ID
	private String cmdTrainId;
	//站序
	private Integer stnSort;
	//站名
	private String stnName;
	//路局 (车站所属局简称)
	private String stnBureau;
	//站所属局全称
	private String stnBureauFull;	
	//到达车次
	private String arrTrainNbr;
	//出发车次
	private String dptTrainNbr;
	//到达时间
	private String arrTime;
	//出发时间
	private String dptTime;
	//图定到达时间 (yyyy-mm-dd h24:mi:ss)
	private String baseArrTime;
	//图定出发时间 (yyyy-mm-dd h24:mi:ss)
	private String baseDptTime;
	//天数 (每过一次0点，天数加1,默认为0) ，到站时刻的rundays
	private Integer arrRunDays; 
	//离站时刻的rundays
	private Integer dptRunDays;
	//股道号
	private String trackNbr;
	//股道名
	private String trackName;
	//站台
	private Integer platform;
	//
	private Integer psgFlg;
	//	机务作业标记 (0:无；1:司机换班；2:机车换挂；3:机车换挂和司机换班)
	private Integer locoFlag ;
	//技术作业类型 (出库、入库、车底下线、到达车底、上水等)
	private String tecType;
	//车站类型 (1:始发站；2:终到站；4:分界口)
	private String stnType;

    /********新加的字段*******/
	//客运是否标记：是（始发、终到、客运营业）
	private String jobs;
	private String stnBureauId;
	private String nodeId;
	private String nodeName;
	/**********************/
	 
	// 2015-1-5 15:34:10 新增字段.
	/**
	 * 车站ID.
	 */
	private String nodeStationId;
	/**
	 * 车站名.
	 */
	private String nodeStationName;
	/**
	 * 节点TDCS对应标识.
	 */
	private String nodeTdcsId;
	/**
	 * 节点TDCS对应名称.
	 */
	private String nodeTdcsName;
	
	/**
	 * 车站ID.
	 */
	private String stnId;
	
	public String getTrackName() {
		return trackName;
	}
	public String getJobs() {
		return jobs;
	}
	public void setJobs(String jobs) {
		this.jobs = jobs;
	}
	public String getStnBureauId() {
		return stnBureauId;
	}
	public void setStnBureauId(String stnBureauId) {
		this.stnBureauId = stnBureauId;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getStnBureauFull() {
		return stnBureauFull;
	}
	public void setStnBureauFull(String stnBureauFull) {
		this.stnBureauFull = stnBureauFull;
	}
	public Integer getArrRunDays() {
		return arrRunDays;
	}
	public void setArrRunDays(Integer arrRunDays) {
		this.arrRunDays = arrRunDays;
	}
	public Integer getDptRunDays() {
		return dptRunDays;
	}
	public void setDptRunDays(Integer dptRunDays) {
		this.dptRunDays = dptRunDays;
	}
	public String getCmdTrainStnId() {
		return cmdTrainStnId;
	}
	public void setCmdTrainStnId(String cmdTrainStnId) {
		this.cmdTrainStnId = cmdTrainStnId;
	}
	public String getCmdTrainId() {
		return cmdTrainId;
	}
	public void setCmdTrainId(String cmdTrainId) {
		this.cmdTrainId = cmdTrainId;
	}
	public Integer getStnSort() {
		return stnSort;
	}
	public void setStnSort(Integer stnSort) {
		this.stnSort = stnSort;
	}
	public String getStnName() {
		return stnName;
	}
	public void setStnName(String stnName) {
		this.stnName = stnName;
	}
	public String getStnBureau() {
		return stnBureau;
	}
	public void setStnBureau(String stnBureau) {
		this.stnBureau = stnBureau;
	}
	public String getArrTrainNbr() {
		return arrTrainNbr;
	}
	public void setArrTrainNbr(String arrTrainNbr) {
		this.arrTrainNbr = arrTrainNbr;
	}
	public String getDptTrainNbr() {
		return dptTrainNbr;
	}
	public void setDptTrainNbr(String dptTrainNbr) {
		this.dptTrainNbr = dptTrainNbr;
	}
	public String getArrTime() {
		return arrTime;
	}
	public void setArrTime(String arrTime) {
		this.arrTime = arrTime;
	}
	public String getDptTime() {
		return dptTime;
	}
	public void setDptTime(String dptTime) {
		this.dptTime = dptTime;
	}
	public String getBaseArrTime() {
		return baseArrTime;
	}
	public void setBaseArrTime(String baseArrTime) {
		this.baseArrTime = baseArrTime;
	}
	public String getBaseDptTime() {
		return baseDptTime;
	}
	public void setBaseDptTime(String baseDptTime) {
		this.baseDptTime = baseDptTime;
	}
	
	public String getTrackNbr() {
		return trackNbr;
	}
	public void setTrackNbr(String trackNbr) {
		this.trackNbr = trackNbr;
	}
	
	public Integer getPlatform() {
		return platform;
	}
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
	public Integer getPsgFlg() {
		return psgFlg;
	}
	public void setPsgFlg(Integer psgFlg) {
		this.psgFlg = psgFlg;
	}
	public Integer getLocoFlag() {
		return locoFlag;
	}
	public void setLocoFlag(Integer locoFlag) {
		this.locoFlag = locoFlag;
	}
	public String getTecType() {
		return tecType;
	}
	public void setTecType(String tecType) {
		this.tecType = tecType;
	}
	public String getStnType() {
		return stnType;
	}
	public void setStnType(String stnType) {
		this.stnType = stnType;
	}
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
	public String getStnId() {
		return stnId;
	}
	public void setStnId(String stnId) {
		this.stnId = stnId;
	}
}
