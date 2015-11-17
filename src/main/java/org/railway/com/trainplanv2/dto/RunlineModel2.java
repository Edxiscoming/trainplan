package org.railway.com.trainplanv2.dto;


public class RunlineModel2 {
	private String id;//运行线id
	private String name;//车次
	private String description;//描述/备注
	private TrainTypeModel2 type;//列车类型
	private String sourceBureauId;//始发站所属路局id
	private String sourceBureauName;//始发站所属路局名
	private String sourceBureauShortName;//始发站所属路局简称
	private String sourceNodeId;//始发站id
	private String sourceNodeName;//始发站名
	private Long sourceTime;//始发时间，毫秒值
	private TimeSchedule2 sourceTimeSchedule;//始发时刻
	private String targetBureauId;//终到站所属路局id
	private String targetBureauName;//终到站所属路局名
	private String targetBureauShortName;//终到站所属路局简称
	private String targetNodeId;//终到站id
	private String targetNodeName;//终到站名
	private Long targetTime;//终到时间，毫秒值
	private TimeSchedule2 targetTimeSchedule;//终到时刻
	private String routeBureauShortNames;//途径路局简称（含始发终到路局）
	private Origin2 origin;//数据来源
	private String lastTimeText;//运行历时
//	private boolean highSpeed; 
	private String highSpeed; 
	
//    private String sourceNodeStationId;//始发站ID
//    private String sourceNodeStationName;//始发站名
    private String sourceNodeTdcsId;//始发节点TDCS对应标识
    private String sourceNodeTdcsName;//始发节点TDCS对应名称
//    private String targetNodeStationId;//终到站ID
//    private String targetNodeStationName;//终到站名
    private String targetNodeTdcsId;//终到节点TDCS对应标识
    private String targetNodeTdcsName;//终到节点TDCS对应名称
	
	private Job2 states;
	private String previousTrainlineId;
	private String previousTrainlineName;
	private String nextTrainlineId;
	private String nextTrainlineName;
	private Job2 vehicles;
    
    
//	public String getSourceNodeStationId() {
//		return sourceNodeStationId;
//	}
//	public void setSourceNodeStationId(String sourceNodeStationId) {
//		this.sourceNodeStationId = sourceNodeStationId;
//	}
//	public String getSourceNodeStationName() {
//		return sourceNodeStationName;
//	}
//	public void setSourceNodeStationName(String sourceNodeStationName) {
//		this.sourceNodeStationName = sourceNodeStationName;
//	}
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
//	public String getTargetNodeStationId() {
//		return targetNodeStationId;
//	}
//	public void setTargetNodeStationId(String targetNodeStationId) {
//		this.targetNodeStationId = targetNodeStationId;
//	}
//	public String getTargetNodeStationName() {
//		return targetNodeStationName;
//	}
//	public void setTargetNodeStationName(String targetNodeStationName) {
//		this.targetNodeStationName = targetNodeStationName;
//	}
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSourceBureauId() {
		return sourceBureauId;
	}
	public void setSourceBureauId(String sourceBureauId) {
		this.sourceBureauId = sourceBureauId;
	}
	public String getSourceBureauName() {
		return sourceBureauName;
	}
	public void setSourceBureauName(String sourceBureauName) {
		this.sourceBureauName = sourceBureauName;
	}
	public String getSourceBureauShortName() {
		return sourceBureauShortName;
	}
	public void setSourceBureauShortName(String sourceBureauShortName) {
		this.sourceBureauShortName = sourceBureauShortName;
	}
	public String getSourceNodeId() {
		return sourceNodeId;
	}
	public void setSourceNodeId(String sourceNodeId) {
		this.sourceNodeId = sourceNodeId;
	}
	public String getSourceNodeName() {
		return sourceNodeName;
	}
	public void setSourceNodeName(String sourceNodeName) {
		this.sourceNodeName = sourceNodeName;
	}
	public Long getSourceTime() {
		return sourceTime;
	}
	public void setSourceTime(Long sourceTime) {
		this.sourceTime = sourceTime;
	}
	public String getTargetBureauId() {
		return targetBureauId;
	}
	public void setTargetBureauId(String targetBureauId) {
		this.targetBureauId = targetBureauId;
	}
	public String getTargetBureauName() {
		return targetBureauName;
	}
	public void setTargetBureauName(String targetBureauName) {
		this.targetBureauName = targetBureauName;
	}
	public String getTargetBureauShortName() {
		return targetBureauShortName;
	}
	public void setTargetBureauShortName(String targetBureauShortName) {
		this.targetBureauShortName = targetBureauShortName;
	}
	public String getTargetNodeId() {
		return targetNodeId;
	}
	public void setTargetNodeId(String targetNodeId) {
		this.targetNodeId = targetNodeId;
	}
	public String getTargetNodeName() {
		return targetNodeName;
	}
	public void setTargetNodeName(String targetNodeName) {
		this.targetNodeName = targetNodeName;
	}
	public Long getTargetTime() {
		return targetTime;
	}
	public void setTargetTime(Long targetTime) {
		this.targetTime = targetTime;
	}
	public String getRouteBureauShortNames() {
		return routeBureauShortNames;
	}
	public void setRouteBureauShortNames(String routeBureauShortNames) {
		this.routeBureauShortNames = routeBureauShortNames;
	}
	public TrainTypeModel2 getType() {
		return type;
	}
	public void setType(TrainTypeModel2 type) {
		this.type = type;
	}
	public TimeSchedule2 getSourceTimeSchedule() {
		return sourceTimeSchedule;
	}
	public void setSourceTimeSchedule(TimeSchedule2 sourceTimeSchedule) {
		this.sourceTimeSchedule = sourceTimeSchedule;
	}
	public TimeSchedule2 getTargetTimeSchedule() {
		return targetTimeSchedule;
	}
	public void setTargetTimeSchedule(TimeSchedule2 targetTimeSchedule) {
		this.targetTimeSchedule = targetTimeSchedule;
	}
	public Origin2 getOrigin() {
		return origin;
	}
	public void setOrigin(Origin2 origin) {
		this.origin = origin;
	}
	public String getLastTimeText() {
		return lastTimeText;
	}
	public void setLastTimeText(String lastTimeText) {
		this.lastTimeText = lastTimeText;
	}
	public String getPreviousTrainlineId() {
		return previousTrainlineId;
	}
	public void setPreviousTrainlineId(String previousTrainlineId) {
		this.previousTrainlineId = previousTrainlineId;
	}
	public String getPreviousTrainlineName() {
		return previousTrainlineName;
	}
	public void setPreviousTrainlineName(String previousTrainlineName) {
		this.previousTrainlineName = previousTrainlineName;
	}
	public String getNextTrainlineId() {
		return nextTrainlineId;
	}
	public void setNextTrainlineId(String nextTrainlineId) {
		this.nextTrainlineId = nextTrainlineId;
	}
	public String getNextTrainlineName() {
		return nextTrainlineName;
	}
	public void setNextTrainlineName(String nextTrainlineName) {
		this.nextTrainlineName = nextTrainlineName;
	}
	public Job2 getStates() {
		return states;
	}
	public void setStates(Job2 states) {
		this.states = states;
	}
	public Job2 getVehicles() {
		return vehicles;
	}
	public void setVehicles(Job2 vehicles) {
		this.vehicles = vehicles;
	}
	public String getHighSpeed() {
		return highSpeed;
	}
	public void setHighSpeed(String highSpeed) {
		this.highSpeed = highSpeed;
	}
	
	
}
