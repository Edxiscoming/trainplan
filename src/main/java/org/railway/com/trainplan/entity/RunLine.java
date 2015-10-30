package org.railway.com.trainplan.entity;

import com.google.common.collect.Lists;

import java.sql.Timestamp;
import java.util.List;

/**
 * JHPT_RJH.M_TRAINLINE
 * Created by speeder on 2014/6/23.
 */
public class RunLine {

    private String id;
    private String name;
    private String pinYinCode;
    private String description;
    private String resourceName;
    private String typeId;
    private String typeName;
    private String resourceBureauId;
    private String resourceBureauName;
    private String sourceBureauShortName;
    private String sourceNodeId;
    private String sourceNodeName;
    private String targetNodeId;
    private String targetNodeName;
    private String targetBureauId;
    private String targetBureauName;
    private String targetBureauShortName;
    private String routeBureauShortName;
    private Timestamp sourceTime;
    private Timestamp targetTime;
    private String dataSourceSource;
    private String dataSourceId;
    private String dataSourceName;
    private int versionMajor;
    private int versionMinor;
    private int versionMicro;
    private String versionQualifier;
    private String code;
    private String vehicleCycleId;
    private String operation;
    private List<RunLineStn> stnList = Lists.newArrayList();

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

    public String getPinYinCode() {
        return pinYinCode;
    }

    public void setPinYinCode(String pinYinCode) {
        this.pinYinCode = pinYinCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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

    public String getResourceBureauId() {
        return resourceBureauId;
    }

    public void setResourceBureauId(String resourceBureauId) {
        this.resourceBureauId = resourceBureauId;
    }

    public String getResourceBureauName() {
        return resourceBureauName;
    }

    public void setResourceBureauName(String resourceBureauName) {
        this.resourceBureauName = resourceBureauName;
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

    public String getRouteBureauShortName() {
        return routeBureauShortName;
    }

    public void setRouteBureauShortName(String routeBureauShortName) {
        this.routeBureauShortName = routeBureauShortName;
    }

    public Timestamp getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(Timestamp sourceTime) {
        this.sourceTime = sourceTime;
    }

    public Timestamp getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(Timestamp targetTime) {
        this.targetTime = targetTime;
    }

    public String getDataSourceSource() {
        return dataSourceSource;
    }

    public void setDataSourceSource(String dataSourceSource) {
        this.dataSourceSource = dataSourceSource;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getVersionMajor() {
        return versionMajor;
    }

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }

    public int getVersionMicro() {
        return versionMicro;
    }

    public void setVersionMicro(int versionMicro) {
        this.versionMicro = versionMicro;
    }

    public String getVersionQualifier() {
        return versionQualifier;
    }

    public void setVersionQualifier(String versionQualifier) {
        this.versionQualifier = versionQualifier;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVehicleCycleId() {
        return vehicleCycleId;
    }

    public void setVehicleCycleId(String vehicleCycleId) {
        this.vehicleCycleId = vehicleCycleId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<RunLineStn> getStnList() {
        return stnList;
    }

    public void setStnList(List<RunLineStn> stnList) {
        this.stnList = stnList;
    }
}
