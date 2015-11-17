package org.railway.com.trainplan.entity;

public class HighlineCrossInfo {

	//高铁日历交路计划ID（本表ID）
	private String highLineCrossId;
	//交路计划ID（对应PLAN_CROSS表中的PLAN_CROSS_ID）
	private String planCrossId;
	private String baseCrossId;
	private String crossDate;
	//开始日期（该日历交路第一个车次的始发日期）
	private String crossStartDate;
	//结束日期（该日历交路最后一个车次的终到日期）
	private String crossEndDate;
	private String crossStartStn;
	private String crossEndStn;
	//交路名称
	private String crossName;
	//备用及停运标记（1:开行;2:备用;0:停运）
	private String spareFlag;
	//相关局（局码）
	private String relevantBureau;
	//车辆担当局（局码）
	private String tokenVehBureau;
	//担当车辆段/动车段
	private String tokenVehDept;
	//担当动车所（用于高铁）
	private String tokenVehDepot;
	//客运担当局（局码）
	private String tokenPsgBureau;
	//担当客运段
	private String tokenPsgDept;
	//动车组车型（用于高铁）
	private String crhType;
	//动车组车组号1（用于高铁）
	private String vehicle1;
	//动车组车组号2（用于高铁）
	private String vehicle2;
	//备注
	private String note;
	//创建人
	private String creatPeople;
	//创建人单位
	private String creatPeopleOrg;
	//创建时间（格式：yyyy-mm-dd hh24:mi:ss）
	private String creatTime;

	private String postId;
	private String postName;
	private String throughLine;
	//始发站
	private String startStn;
	
	private String createReason;
	
	private String crossBureau;
	
	//始发站
	private String endStn;
		
	//审核人单位
	private String checkPeopleOrg;
	//审核时间（格式：yyyy-mm-dd hh24:mi:ss）
	private String checkTime;
	
	private Integer vehicleSubType;
	private String vehicleSubPeople;
	private String vehicleSubPeopleOrg;
	private String vehicleSubTime;
	private Integer vehicleCheckType;
	private String vehicleCheckPeople;
	private String vehicleCheckPeopleOrg;
	private String vehicleCheckTime;
	private Integer crossCheckType;
	private String crossCheckPeople;
	private String crossCheckPeopleOrg;
	private String crossCheckTime;
	
	//2014-09-11增加该字段      用于界面权限控制
	private String isEnable;//1:真  0：假
	
	
	
	public String getCrossDate() {
		return crossDate;
	}
	public void setCrossDate(String crossDate) {
		this.crossDate = crossDate;
	}
	public Integer getVehicleSubType() {
		return vehicleSubType;
	}
	public void setVehicleSubType(Integer vehicleSubType) {
		this.vehicleSubType = vehicleSubType;
	}
	public Integer getVehicleCheckType() {
		return vehicleCheckType;
	}
	public void setVehicleCheckType(Integer vehicleCheckType) {
		this.vehicleCheckType = vehicleCheckType;
	}
	public Integer getCrossCheckType() {
		return crossCheckType;
	}
	public void setCrossCheckType(Integer crossCheckType) {
		this.crossCheckType = crossCheckType;
	}
	public String getVehicleSubPeople() {
		return vehicleSubPeople;
	}
	public void setVehicleSubPeople(String vehicleSubPeople) {
		this.vehicleSubPeople = vehicleSubPeople;
	}
	public String getVehicleSubPeopleOrg() {
		return vehicleSubPeopleOrg;
	}
	public void setVehicleSubPeopleOrg(String vehicleSubPeopleOrg) {
		this.vehicleSubPeopleOrg = vehicleSubPeopleOrg;
	}
	public String getVehicleSubTime() {
		return vehicleSubTime;
	}
	public void setVehicleSubTime(String vehicleSubTime) {
		this.vehicleSubTime = vehicleSubTime;
	}
	
	public String getVehicleCheckPeople() {
		return vehicleCheckPeople;
	}
	public void setVehicleCheckPeople(String vehicleCheckPeople) {
		this.vehicleCheckPeople = vehicleCheckPeople;
	}
	public String getVehicleCheckPeopleOrg() {
		return vehicleCheckPeopleOrg;
	}
	public void setVehicleCheckPeopleOrg(String vehicleCheckPeopleOrg) {
		this.vehicleCheckPeopleOrg = vehicleCheckPeopleOrg;
	}
	public String getVehicleCheckTime() {
		return vehicleCheckTime;
	}
	public void setVehicleCheckTime(String vehicleCheckTime) {
		this.vehicleCheckTime = vehicleCheckTime;
	}
	
	public String getCrossCheckPeople() {
		return crossCheckPeople;
	}
	public void setCrossCheckPeople(String crossCheckPeople) {
		this.crossCheckPeople = crossCheckPeople;
	}
	public String getCrossCheckPeopleOrg() {
		return crossCheckPeopleOrg;
	}
	public void setCrossCheckPeopleOrg(String crossCheckPeopleOrg) {
		this.crossCheckPeopleOrg = crossCheckPeopleOrg;
	}
	public String getCrossCheckTime() {
		return crossCheckTime;
	}
	public void setCrossCheckTime(String crossCheckTime) {
		this.crossCheckTime = crossCheckTime;
	}
	public String getCrossBureau() {
		return crossBureau;
	}
	public void setCrossBureau(String crossBureau) {
		this.crossBureau = crossBureau;
	}
	public String getCreateReason() {
		return createReason;
	}
	public void setCreateReason(String createReason) {
		this.createReason = createReason;
	}
	public String getBaseCrossId() {
		return baseCrossId;
	}
	public void setBaseCrossId(String baseCrossId) {
		this.baseCrossId = baseCrossId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getThroughLine() {
		return throughLine;
	}
	public void setThroughLine(String throughLine) {
		this.throughLine = throughLine;
	}
	public String getCrossStartStn() {
		return crossStartStn;
	}
	public void setCrossStartStn(String crossStartStn) {
		this.crossStartStn = crossStartStn;
	}
	public String getCrossEndStn() {
		return crossEndStn;
	}
	public void setCrossEndStn(String crossEndStn) {
		this.crossEndStn = crossEndStn;
	} 
	
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	 
	public String getHighLineCrossId() {
		return highLineCrossId;
	}
	public void setHighLineCrossId(String highLineCrossId) {
		this.highLineCrossId = highLineCrossId;
	}
	public String getPlanCrossId() {
		return planCrossId;
	}
	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}
	public String getCrossStartDate() {
		return crossStartDate;
	}
	public void setCrossStartDate(String crossStartDate) {
		this.crossStartDate = crossStartDate;
	}
	public String getCrossEndDate() {
		return crossEndDate;
	}
	public void setCrossEndDate(String crossEndDate) {
		this.crossEndDate = crossEndDate;
	}
	public String getCrossName() {
		return crossName;
	}
	public void setCrossName(String crossName) {
		this.crossName = crossName;
	}
	public String getSpareFlag() {
		return spareFlag;
	}
	public void setSpareFlag(String spareFlag) {
		this.spareFlag = spareFlag;
	}
	public String getRelevantBureau() {
		return relevantBureau;
	}
	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}
	public String getTokenVehBureau() {
		return tokenVehBureau;
	}
	public void setTokenVehBureau(String tokenVehBureau) {
		this.tokenVehBureau = tokenVehBureau;
	}
	public String getTokenVehDept() {
		return tokenVehDept;
	}
	public void setTokenVehDept(String tokenVehDept) {
		this.tokenVehDept = tokenVehDept;
	}
	public String getTokenVehDepot() {
		return tokenVehDepot;
	}
	public void setTokenVehDepot(String tokenVehDepot) {
		this.tokenVehDepot = tokenVehDepot;
	}
	public String getTokenPsgBureau() {
		return tokenPsgBureau;
	}
	public void setTokenPsgBureau(String tokenPsgBureau) {
		this.tokenPsgBureau = tokenPsgBureau;
	}
	public String getTokenPsgDept() {
		return tokenPsgDept;
	}
	public void setTokenPsgDept(String tokenPsgDept) {
		this.tokenPsgDept = tokenPsgDept;
	}
	public String getCrhType() {
		return crhType;
	}
	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}
	public String getVehicle1() {
		return vehicle1;
	}
	public void setVehicle1(String vehicle1) {
		this.vehicle1 = vehicle1;
	}
	public String getVehicle2() {
		return vehicle2;
	}
	public void setVehicle2(String vehicle2) {
		this.vehicle2 = vehicle2;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCreatPeople() {
		return creatPeople;
	}
	public void setCreatPeople(String creatPeople) {
		this.creatPeople = creatPeople;
	}
	public String getCreatPeopleOrg() {
		return creatPeopleOrg;
	}
	public void setCreatPeopleOrg(String creatPeopleOrg) {
		this.creatPeopleOrg = creatPeopleOrg;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	
	public String getCheckPeopleOrg() {
		return checkPeopleOrg;
	}
	public void setCheckPeopleOrg(String checkPeopleOrg) {
		this.checkPeopleOrg = checkPeopleOrg;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	
	
}

