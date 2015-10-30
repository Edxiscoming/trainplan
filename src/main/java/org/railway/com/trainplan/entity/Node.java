package org.railway.com.trainplan.entity;

import java.io.Serializable;


/**
 * 表M_NODE
 * @author join
 *
 */
public class Node implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;
	private String pinyin;
	private String shortName;
	private String telegraphCode;
	private String code;
	private String marks;
	private String bureauId;
	private String bureauName;
	private String bureauShortName;
	private String southwestNodeId;
	private String southwestNodeName;
	private String tdmsNodeId;
	private String tdmsNodeName;
	private String tdcsNodeId;
	private String tdcsNodeName;
	private String pinyinInitials;
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
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getTelegraphCode() {
		return telegraphCode;
	}
	public void setTelegraphCode(String telegraphCode) {
		this.telegraphCode = telegraphCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
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
	public String getSouthwestNodeId() {
		return southwestNodeId;
	}
	public void setSouthwestNodeId(String southwestNodeId) {
		this.southwestNodeId = southwestNodeId;
	}
	public String getSouthwestNodeName() {
		return southwestNodeName;
	}
	public void setSouthwestNodeName(String southwestNodeName) {
		this.southwestNodeName = southwestNodeName;
	}
	public String getTdmsNodeId() {
		return tdmsNodeId;
	}
	public void setTdmsNodeId(String tdmsNodeId) {
		this.tdmsNodeId = tdmsNodeId;
	}
	public String getTdmsNodeName() {
		return tdmsNodeName;
	}
	public void setTdmsNodeName(String tdmsNodeName) {
		this.tdmsNodeName = tdmsNodeName;
	}
	public String getTdcsNodeId() {
		return tdcsNodeId;
	}
	public void setTdcsNodeId(String tdcsNodeId) {
		this.tdcsNodeId = tdcsNodeId;
	}
	public String getTdcsNodeName() {
		return tdcsNodeName;
	}
	public void setTdcsNodeName(String tdcsNodeName) {
		this.tdcsNodeName = tdcsNodeName;
	}
	public String getPinyinInitials() {
		return pinyinInitials;
	}
	public void setPinyinInitials(String pinyinInitials) {
		this.pinyinInitials = pinyinInitials;
	}
	
	
	
	
	
	
	
}
