package org.railway.com.trainplan.entity.runline;


public class RunlineModel {
	private String id;//运行线id
	private String name;//车次
	private String description;//描述/备注
	private TrainTypeModel type;//列车类型
	private String sourceBureauId;//始发站所属路局id
	private String sourceBureauName;//始发站所属路局名
	private String sourceBureauShortName;//始发站所属路局简称
	private String sourceNodeId;//始发站id
	private String sourceNodeName;//始发站名
	private Long sourceTime;//始发时间，毫秒值
	private TimeSchedule sourceTimeSchedule;//始发时刻
	private String targetBureauId;//终到站所属路局id
	private String targetBureauName;//终到站所属路局名
	private String targetBureauShortName;//终到站所属路局简称
	private String targetNodeId;//终到站id
	private String targetNodeName;//终到站名
	private Long targetTime;//终到时间，毫秒值
	private TimeSchedule targetTimeSchedule;//终到时刻
	private String routeBureauShortNames;//途径路局简称（含始发终到路局）
	private Origin origin;//数据来源
	private String lastTimeText;//运行历时
	private boolean highSpeed; 
	
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
	public TrainTypeModel getType() {
		return type;
	}
	public void setType(TrainTypeModel type) {
		this.type = type;
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
	public TimeSchedule getSourceTimeSchedule() {
		return sourceTimeSchedule;
	}
	public void setSourceTimeSchedule(TimeSchedule sourceTimeSchedule) {
		this.sourceTimeSchedule = sourceTimeSchedule;
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
	public TimeSchedule getTargetTimeSchedule() {
		return targetTimeSchedule;
	}
	public void setTargetTimeSchedule(TimeSchedule targetTimeSchedule) {
		this.targetTimeSchedule = targetTimeSchedule;
	}
	public String getRouteBureauShortNames() {
		return routeBureauShortNames;
	}
	public void setRouteBureauShortNames(String routeBureauShortNames) {
		this.routeBureauShortNames = routeBureauShortNames;
	}
	public Origin getOrigin() {
		return origin;
	}
	public void setOrigin(Origin origin) {
		this.origin = origin;
	}
	public String getLastTimeText() {
		return lastTimeText;
	}
	public void setLastTimeText(String lastTimeText) {
		this.lastTimeText = lastTimeText;
	}
	public boolean isHighSpeed() {
		return highSpeed;
	}
	public void setHighSpeed(boolean highSpeed) {
		this.highSpeed = highSpeed;
	}
	
	
}
