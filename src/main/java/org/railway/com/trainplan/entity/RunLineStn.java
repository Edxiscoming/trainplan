package org.railway.com.trainplan.entity;

import java.sql.Timestamp;

/**
 * JHPT_RJH.M_TRAINLINE_SCHEDULE_SITEM
 * JHPT_RJH.M_TRAINLINE_SCHEDULE_RITEM
 * JHPT_RJH.M_TRAINLINE_SCHEDULE_TITEM
 * Created by speeder on 2014/6/23.
 */
public class RunLineStn {
    private String id;
    private String name;
    private String parentId;
    private String parentName;
    private int childIndex;
    private String bureauId;
    private String bureauName;
    private String bureauShortName;
    private String nodeId;
    private String nodeName;
    private String trackName;
    private Timestamp sourceTime;
    private Timestamp targetTime;
    private int versionMajor;
    private int versionMinor;
    private int versionMicro;
    private String versionQualifier;

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

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
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
}
