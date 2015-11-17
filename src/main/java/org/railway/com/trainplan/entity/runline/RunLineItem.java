package org.railway.com.trainplan.entity.runline;


public class RunLineItem {
	private String id;//运行线作业条目id
	private String name;//运行线作业条目名称
	private String description;//运行线作业条目描述
	private int index;//作业条目索引值，该值为只读，此处设置无效
	private Job jobs;//作业内容
	private String bureauId;//所属路局id
	private String bureauName;//所属路局名称
	private String bureauShortName;//所属路局简称
	private String nodeId;//节点id
	private String nodeName;//节点名称
	private String trackName;//股道名称
	private String platformName;//站台名称
	private Long sourceTime;//到达时间，毫秒值
	private TimeSchedule sourceTimeSchedule;//到达时刻
	private String sourceParentName;//到达车次
	private Long targetTime;//出发时间，毫秒值
	private TimeSchedule targetTimeSchedule;//出发时刻
	private String targetParentName;//出发车次
	private RunlineModel parent;//所属运行线
	
	
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
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Job getJobs() {
		return jobs;
	}
	public void setJobs(Job jobs) {
		this.jobs = jobs;
	}
	public String getBureauId() {
		return bureauId;
	}
	public void setBureauId(String bureauId) {
		this.bureauId = bureauId;
	}
	public String getBureauName() {
		return bureauName;
	}
	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}
	public String getBureauShortName() {
		return bureauShortName;
	}
	public void setBureauShortName(String bureauShortName) {
		this.bureauShortName = bureauShortName;
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
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
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
	public String getSourceParentName() {
		return sourceParentName;
	}
	public void setSourceParentName(String sourceParentName) {
		this.sourceParentName = sourceParentName;
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
	public String getTargetParentName() {
		return targetParentName;
	}
	public void setTargetParentName(String targetParentName) {
		this.targetParentName = targetParentName;
	}
	public RunlineModel getParent() {
		return parent;
	}
	public void setParent(RunlineModel parent) {
		this.parent = parent;
	}
	
	
}
