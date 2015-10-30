package org.railway.com.trainplan.entity;

/**
 * 表M_TRAINLINE_ITEM
 * 的实体类
 * @author join
 *
 */
public class MTrainLineStn {

	private String id;
	private String name;
	private String parentId;
	private String parentName;
	private String jobs;
	private String bureauId;
	private String bureauName;
	private String bureauShortname;
	private String nodeId;
	private String nodeName;
	private String trackName;
	private String platformName;
	private String sourceParentName;
	private String targetParentName;
	private String sourceTime;
	private String targetTime;
	private String time;
	private Integer childIndex;
	private Integer sourceTimeScheduleDates;
	private Integer targetTimeScheduleDates;
	
	
	
	public String getJobs() {
		return jobs;
	}
	public void setJobs(String jobs) {
		this.jobs = jobs;
	}
	public String getBureauId() {
		return bureauId;
	}
	public void setBureauId(String bureauId) {
		this.bureauId = bureauId;
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
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getSourceParentName() {
		return sourceParentName;
	}
	public void setSourceParentName(String sourceParentName) {
		this.sourceParentName = sourceParentName;
	}
	public String getTargetParentName() {
		return targetParentName;
	}
	public void setTargetParentName(String targetParentName) {
		this.targetParentName = targetParentName;
	}
	public Integer getSourceTimeScheduleDates() {
		return sourceTimeScheduleDates;
	}
	public void setSourceTimeScheduleDates(Integer sourceTimeScheduleDates) {
		this.sourceTimeScheduleDates = sourceTimeScheduleDates;
	}
	public Integer getTargetTimeScheduleDates() {
		return targetTimeScheduleDates;
	}
	public void setTargetTimeScheduleDates(Integer targetTimeScheduleDates) {
		this.targetTimeScheduleDates = targetTimeScheduleDates;
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getBureauName() {
		return bureauName;
	}
	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}
	public String getBureauShortname() {
		return bureauShortname;
	}
	public void setBureauShortname(String bureauShortname) {
		this.bureauShortname = bureauShortname;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getChildIndex() {
		return childIndex;
	}
	public void setChildIndex(Integer childIndex) {
		this.childIndex = childIndex;
	}

	
}
