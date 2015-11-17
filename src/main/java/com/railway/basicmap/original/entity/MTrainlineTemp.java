package com.railway.basicmap.original.entity;

import java.util.Date;

public class MTrainlineTemp {
    private String id;

    private String name;

    private String description;

    private String parentId;

    private String parentName;

    private String typeId;

    private String typeName;

    private String vehicleType;

    private String highSpeed;

    private String business;

    private String routeId;

    private String routeName;

    private String sourceBureauId;

    private String sourceBureauName;

    private String sourceBureauShortName;

    /**
     * 始发站id
     */
    private String sourceNodeStationId;

    /**
     * 始发站名称
     */
    private String sourceNodeStationName;

    private String sourceNodeId;

    private String sourceNodeName;

    /**
     * 始发节点TDCS对应标识
     */
    private String sourceNodeTdcsId;

    /**
     * 始发节点TDCS对应名称
     */
    private String sourceNodeTdcsName;

    private String targetBureauId;

    private String targetBureauName;

    private String targetBureauShortName;

    /**
     * 终到站id
     */
    private String targetNodeStationId;

    /**
     * 终到站名称
     */
    private String targetNodeStationName;

    private String targetNodeId;

    private String targetNodeName;

    /**
     * 终到节点TDCS对应标识
     */
    private String targetNodeTdcsId;

    /**
     * 终到节点TDCS对应名称
     */
    private String targetNodeTdcsName;

    private Date sourceTime;

    private Short sourceTimeScheduleDates;

    private Short sourceTimeScheduleHour;

    private Short sourceTimeScheduleMinute;

    private Short sourceTimeScheduleSecond;

    private Date targetTime;

    private Short targetTimeScheduleDates;

    private Short targetTimeScheduleHour;

    private Short targetTimeScheduleMinute;

    private Short targetTimeScheduleSecond;

    private String routeBureauShortNames;

    private String origin;

    private String originId;

    private String originName;

    private String executionAlgorithmId;

    private String executionAlgorithmName;

    private String executionAlgorithmClassName;

    private Date executionSourceTime;

    private Date executionTargetTime;

    /**
     * 已审核局
     */
    private String checked;

    /**
     * 审核状态：未审核；部分审核；全部审核
     */
    private String checkState;

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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getHighSpeed() {
        return highSpeed;
    }

    public void setHighSpeed(String highSpeed) {
        this.highSpeed = highSpeed;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
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

    /**
     * @return 始发站id
     */
    public String getSourceNodeStationId() {
        return sourceNodeStationId;
    }

    /**
     * @param sourceNodeStationId 
	 *            始发站id
     */
    public void setSourceNodeStationId(String sourceNodeStationId) {
        this.sourceNodeStationId = sourceNodeStationId;
    }

    /**
     * @return 始发站名称
     */
    public String getSourceNodeStationName() {
        return sourceNodeStationName;
    }

    /**
     * @param sourceNodeStationName 
	 *            始发站名称
     */
    public void setSourceNodeStationName(String sourceNodeStationName) {
        this.sourceNodeStationName = sourceNodeStationName;
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

    /**
     * @return 始发节点TDCS对应标识
     */
    public String getSourceNodeTdcsId() {
        return sourceNodeTdcsId;
    }

    /**
     * @param sourceNodeTdcsId 
	 *            始发节点TDCS对应标识
     */
    public void setSourceNodeTdcsId(String sourceNodeTdcsId) {
        this.sourceNodeTdcsId = sourceNodeTdcsId;
    }

    /**
     * @return 始发节点TDCS对应名称
     */
    public String getSourceNodeTdcsName() {
        return sourceNodeTdcsName;
    }

    /**
     * @param sourceNodeTdcsName 
	 *            始发节点TDCS对应名称
     */
    public void setSourceNodeTdcsName(String sourceNodeTdcsName) {
        this.sourceNodeTdcsName = sourceNodeTdcsName;
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

    /**
     * @return 终到站id
     */
    public String getTargetNodeStationId() {
        return targetNodeStationId;
    }

    /**
     * @param targetNodeStationId 
	 *            终到站id
     */
    public void setTargetNodeStationId(String targetNodeStationId) {
        this.targetNodeStationId = targetNodeStationId;
    }

    /**
     * @return 终到站名称
     */
    public String getTargetNodeStationName() {
        return targetNodeStationName;
    }

    /**
     * @param targetNodeStationName 
	 *            终到站名称
     */
    public void setTargetNodeStationName(String targetNodeStationName) {
        this.targetNodeStationName = targetNodeStationName;
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

    /**
     * @return 终到节点TDCS对应标识
     */
    public String getTargetNodeTdcsId() {
        return targetNodeTdcsId;
    }

    /**
     * @param targetNodeTdcsId 
	 *            终到节点TDCS对应标识
     */
    public void setTargetNodeTdcsId(String targetNodeTdcsId) {
        this.targetNodeTdcsId = targetNodeTdcsId;
    }

    /**
     * @return 终到节点TDCS对应名称
     */
    public String getTargetNodeTdcsName() {
        return targetNodeTdcsName;
    }

    /**
     * @param targetNodeTdcsName 
	 *            终到节点TDCS对应名称
     */
    public void setTargetNodeTdcsName(String targetNodeTdcsName) {
        this.targetNodeTdcsName = targetNodeTdcsName;
    }

    public Date getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(Date sourceTime) {
        this.sourceTime = sourceTime;
    }

    public Short getSourceTimeScheduleDates() {
        return sourceTimeScheduleDates;
    }

    public void setSourceTimeScheduleDates(Short sourceTimeScheduleDates) {
        this.sourceTimeScheduleDates = sourceTimeScheduleDates;
    }

    public Short getSourceTimeScheduleHour() {
        return sourceTimeScheduleHour;
    }

    public void setSourceTimeScheduleHour(Short sourceTimeScheduleHour) {
        this.sourceTimeScheduleHour = sourceTimeScheduleHour;
    }

    public Short getSourceTimeScheduleMinute() {
        return sourceTimeScheduleMinute;
    }

    public void setSourceTimeScheduleMinute(Short sourceTimeScheduleMinute) {
        this.sourceTimeScheduleMinute = sourceTimeScheduleMinute;
    }

    public Short getSourceTimeScheduleSecond() {
        return sourceTimeScheduleSecond;
    }

    public void setSourceTimeScheduleSecond(Short sourceTimeScheduleSecond) {
        this.sourceTimeScheduleSecond = sourceTimeScheduleSecond;
    }

    public Date getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(Date targetTime) {
        this.targetTime = targetTime;
    }

    public Short getTargetTimeScheduleDates() {
        return targetTimeScheduleDates;
    }

    public void setTargetTimeScheduleDates(Short targetTimeScheduleDates) {
        this.targetTimeScheduleDates = targetTimeScheduleDates;
    }

    public Short getTargetTimeScheduleHour() {
        return targetTimeScheduleHour;
    }

    public void setTargetTimeScheduleHour(Short targetTimeScheduleHour) {
        this.targetTimeScheduleHour = targetTimeScheduleHour;
    }

    public Short getTargetTimeScheduleMinute() {
        return targetTimeScheduleMinute;
    }

    public void setTargetTimeScheduleMinute(Short targetTimeScheduleMinute) {
        this.targetTimeScheduleMinute = targetTimeScheduleMinute;
    }

    public Short getTargetTimeScheduleSecond() {
        return targetTimeScheduleSecond;
    }

    public void setTargetTimeScheduleSecond(Short targetTimeScheduleSecond) {
        this.targetTimeScheduleSecond = targetTimeScheduleSecond;
    }

    public String getRouteBureauShortNames() {
        return routeBureauShortNames;
    }

    public void setRouteBureauShortNames(String routeBureauShortNames) {
        this.routeBureauShortNames = routeBureauShortNames;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getExecutionAlgorithmId() {
        return executionAlgorithmId;
    }

    public void setExecutionAlgorithmId(String executionAlgorithmId) {
        this.executionAlgorithmId = executionAlgorithmId;
    }

    public String getExecutionAlgorithmName() {
        return executionAlgorithmName;
    }

    public void setExecutionAlgorithmName(String executionAlgorithmName) {
        this.executionAlgorithmName = executionAlgorithmName;
    }

    public String getExecutionAlgorithmClassName() {
        return executionAlgorithmClassName;
    }

    public void setExecutionAlgorithmClassName(String executionAlgorithmClassName) {
        this.executionAlgorithmClassName = executionAlgorithmClassName;
    }

    public Date getExecutionSourceTime() {
        return executionSourceTime;
    }

    public void setExecutionSourceTime(Date executionSourceTime) {
        this.executionSourceTime = executionSourceTime;
    }

    public Date getExecutionTargetTime() {
        return executionTargetTime;
    }

    public void setExecutionTargetTime(Date executionTargetTime) {
        this.executionTargetTime = executionTargetTime;
    }

    /**
     * @return 已审核局
     */
    public String getChecked() {
        return checked;
    }

    /**
     * @param checked 
	 *            已审核局
     */
    public void setChecked(String checked) {
        this.checked = checked;
    }

    /**
     * @return 审核状态：未审核；部分审核；全部审核
     */
    public String getCheckState() {
        return checkState;
    }

    /**
     * @param checkState 
	 *            审核状态：未审核；部分审核；全部审核
     */
    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }
}