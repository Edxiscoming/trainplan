package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



/**
 * The persistent class for the HIGHLINE_CROSS database table.
 * 
 */

public class HighlineCrossCmd implements Serializable {
	private static final long serialVersionUID = 1L;

	private String baseCrossId = "";

	private String creatPeople;

	private String creatPeopleOrg;

	private String creatReason = "";

	private Date creatTime;

	private String crhType = "";

	private String crossBureau = "";

	private String crossCheckPeople;

	private String crossCheckPeopleOrg;

	private Date crossCheckTime;

	private Integer crossCheckType;

	private String crossEndDate = "";

	private String crossEndStn = "";

	private String crossName = "";

	private String crossStartDate = "";

	private String crossStartStn = "";

	private String highlineCrossId;

	private String note;

	private String planCrossId = "";

	private Integer postId;

	private String postName = "";

	private String CrossDisplayName = "";
	
	private String relevantBureau = "";

	private String spareFlag = "";

	private String throughLine = "";

	private String tokenPsgBureau = "";

	private String tokenPsgDept = "";

	private String tokenVehBureau = "";

	private String tokenVehDepot = "";

	private String tokenVehDept = "";

	private String vehicleCheckPeople = "";

	private String vehicleCheckPeopleOrg = "";

	private Date vehicleCheckTime;

	private Integer vehicleCheckType;

	private String vehicleSubPeople;

	private String vehicleSubPeopleOrg;

	private Date vehicleSubTime;

	private Integer vehicleSubType;

	private String vehicle1 = "";

	private String vehicle2 = "";
	
	private String crossDate = "";
	
	private List<HighlineCrossTrain> crossTrain;
	
    public HighlineCrossCmd() {
    }

	public String getBaseCrossId() {
		return this.baseCrossId;
	}

	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	}

	public String getCreatPeople() {
		return this.creatPeople;
	}

	public void setCreatPeople(String creatPeople) {
		this.creatPeople = creatPeople;
	}

	public String getCreatPeopleOrg() {
		return this.creatPeopleOrg;
	}

	public void setCreatPeopleOrg(String creatPeopleOrg) {
		this.creatPeopleOrg = creatPeopleOrg;
	}

	public String getCreatReason() {
		return this.creatReason;
	}

	public void setCreatReason(String creatReason) {
		this.creatReason = creatReason;
	}

	public Date getCreatTime() {
		return this.creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getCrhType() {
		return this.crhType;
	}

	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}

	public String getCrossBureau() {
		return this.crossBureau;
	}

	public void setCrossBureau(String crossBureau) {
		this.crossBureau = crossBureau;
	}

	public String getCrossCheckPeople() {
		return this.crossCheckPeople;
	}

	public void setCrossCheckPeople(String crossCheckPeople) {
		this.crossCheckPeople = crossCheckPeople;
	}

	public String getCrossCheckPeopleOrg() {
		return this.crossCheckPeopleOrg;
	}

	public void setCrossCheckPeopleOrg(String crossCheckPeopleOrg) {
		this.crossCheckPeopleOrg = crossCheckPeopleOrg;
	}

	public Date getCrossCheckTime() {
		return this.crossCheckTime;
	}

	public void setCrossCheckTime(Date crossCheckTime) {
		this.crossCheckTime = crossCheckTime;
	}

	public Integer getCrossCheckType() {
		return this.crossCheckType;
	}

	public void setCrossCheckType(Integer crossCheckType) {
		this.crossCheckType = crossCheckType;
	}

	public String getCrossEndDate() {
		return this.crossEndDate;
	}

	public void setCrossEndDate(String crossEndDate) {
		this.crossEndDate = crossEndDate;
	}

	public String getCrossEndStn() {
		return this.crossEndStn;
	}

	public void setCrossEndStn(String crossEndStn) {
		this.crossEndStn = crossEndStn;
	}

	public String getCrossName() {
		return this.crossName;
	}

	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}

	public String getCrossStartDate() {
		return this.crossStartDate;
	}

	public void setCrossStartDate(String crossStartDate) {
		this.crossStartDate = crossStartDate;
	}

	public String getCrossStartStn() {
		return this.crossStartStn;
	}

	public void setCrossStartStn(String crossStartStn) {
		this.crossStartStn = crossStartStn;
	}

	public String getHighlineCrossId() {
		return this.highlineCrossId;
	}

	public void setHighlineCrossId(String highlineCrossId) {
		this.highlineCrossId = highlineCrossId;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPlanCrossId() {
		return this.planCrossId;
	}

	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}

	public Integer getPostId() {
		return this.postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return this.postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getRelevantBureau() {
		return this.relevantBureau;
	}

	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}

	public String getSpareFlag() {
		return this.spareFlag;
	}

	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}

	public String getThroughLine() {
		return this.throughLine;
	}

	public void setThroughLine(String throughLine) {
		this.throughLine = throughLine;
	}

	public String getTokenPsgBureau() {
		return this.tokenPsgBureau;
	}

	public void setTokenPsgBureau(String tokenPsgBureau) {
		this.tokenPsgBureau = tokenPsgBureau;
	}

	public String getTokenPsgDept() {
		return this.tokenPsgDept;
	}

	public void setTokenPsgDept(String tokenPsgDept) {
		this.tokenPsgDept = tokenPsgDept;
	}

	public String getTokenVehBureau() {
		return this.tokenVehBureau;
	}

	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}

	public String getTokenVehDepot() {
		return this.tokenVehDepot;
	}

	public void setTokenVehDepot(String tokenVehDepot) {
		this.tokenVehDepot = tokenVehDepot;
	}

	public String getTokenVehDept() {
		return this.tokenVehDept;
	}

	public void setTokenVehDept(String tokenVehDept) {
		this.tokenVehDept = tokenVehDept;
	}

	public String getVehicleCheckPeople() {
		return this.vehicleCheckPeople;
	}

	public void setVehicleCheckPeople(String vehicleCheckPeople) {
		this.vehicleCheckPeople = vehicleCheckPeople;
	}

	public String getVehicleCheckPeopleOrg() {
		return this.vehicleCheckPeopleOrg;
	}

	public void setVehicleCheckPeopleOrg(String vehicleCheckPeopleOrg) {
		this.vehicleCheckPeopleOrg = vehicleCheckPeopleOrg;
	}

	public Date getVehicleCheckTime() {
		return this.vehicleCheckTime;
	}

	public void setVehicleCheckTime(Date vehicleCheckTime) {
		this.vehicleCheckTime = vehicleCheckTime;
	}

	public Integer getVehicleCheckType() {
		return this.vehicleCheckType;
	}

	public void setVehicleCheckType(Integer vehicleCheckType) {
		this.vehicleCheckType = vehicleCheckType;
	}

	public String getVehicleSubPeople() {
		return this.vehicleSubPeople;
	}

	public void setVehicleSubPeople(String vehicleSubPeople) {
		this.vehicleSubPeople = vehicleSubPeople;
	}

	public String getVehicleSubPeopleOrg() {
		return this.vehicleSubPeopleOrg;
	}

	public void setVehicleSubPeopleOrg(String vehicleSubPeopleOrg) {
		this.vehicleSubPeopleOrg = vehicleSubPeopleOrg;
	}

	public Date getVehicleSubTime() {
		return this.vehicleSubTime;
	}

	public void setVehicleSubTime(Date vehicleSubTime) {
		this.vehicleSubTime = vehicleSubTime;
	}

	public Integer getVehicleSubType() {
		return this.vehicleSubType;
	}

	public void setVehicleSubType(Integer vehicleSubType) {
		this.vehicleSubType = vehicleSubType;
	}

	public String getVehicle1() {
		return this.vehicle1;
	}

	public void setVehicle1(String vehicle1) {
		this.vehicle1 = vehicle1;
	}

	public String getVehicle2() {
		return this.vehicle2;
	}

	public void setVehicle2(String vehicle2) {
		this.vehicle2 = vehicle2;
	}

	public String getCrossDate() {
		return crossDate;
	}

	public void setCrossDate(String crossDate) {
		this.crossDate = crossDate;
	}

	public List<HighlineCrossTrain> getCrossTrain() {
		return crossTrain;
	}

	public void setCrossTrain(List<HighlineCrossTrain> crossTrain) {
		this.crossTrain = crossTrain;
	}

	public String getCrossDisplayName() {
		return CrossDisplayName;
	}

	public void setCrossDisplayName(String crossDisplayName) {
		CrossDisplayName = crossDisplayName;
	}

}