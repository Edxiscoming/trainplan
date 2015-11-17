package org.railway.com.trainplan.entity;

public class PlanCrossInfo {
    //本表主键
    private String planCrossId;
    //unit_cross表主键
    private String unitCrossId;
    private String baseCrossId;
    private String baseChartId;
    private String baseChartName;
    private String crossStartDate;
    private String crossEndDate;
    private String crossName;
    private String crossSpareName;
    private String spareFlag = "1";
    private String pairNbr;
    private String highlineFlag;
    private String highlineRule;
    private String commonlineRule;
    private String appointWeek;
    private String appointDay;
    private String crossSection;
    private String checkPeople;
    private String checkPeopleOrg;
    private String checkTime;
    private String startBureau;
    private String tokenVehBureau;
    private String tokenVehDept;
    private String tokenVehDepot;
    private String tokenPsgDept;
    private String tokenPsgBureau;
    private String locoType;
    private String crhType;
    private String note;
    private String createPeople;
    private String createPeopleOrg;
    private String createTime;
    private String vehicle1;
    private String vehicle2;
    private String relevantBureau;//相关局局码
    private String checkedBureau;
    private int groupTotalNbr;
    private int elecSupply;
    private int dejCollect;
    private int airCondition;
    private int checkType;
    private int isAutoGenerate;
    private String postId;
    private String postName;
    private String throughLine;

    //highlinecross生成时需要
    private int groupSerialNbr;
    
    /**
     * plancross对应的逻辑交路id
     */
    private String cmCrossId;


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

	public int getGroupSerialNbr() {
        return groupSerialNbr;
    }

    public void setGroupSerialNbr(int groupSerialNbr) {
        this.groupSerialNbr = groupSerialNbr;
    }

    public String getCheckedBureau() {
        return checkedBureau;
    }

    public void setCheckedBureau(String checkedBureau) {
        this.checkedBureau = checkedBureau;
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

    public String getPlanCrossId() {
        return planCrossId;
    }

    public void setPlanCrossId(String planCrossId) {
        this.planCrossId = planCrossId;
    }

    public String getUnitCrossId() {
        return unitCrossId;
    }

    public void setUnitCrossId(String unitCrossId) {
        this.unitCrossId = unitCrossId;
    }

    public String getBaseCrossId() {
        return baseCrossId;
    }

    public void setBaseCrossId(String baseCrossId) {
        this.baseCrossId = baseCrossId;
    }

    public String getBaseChartId() {
        return baseChartId;
    }

    public void setBaseChartId(String baseChartId) {
        this.baseChartId = baseChartId;
    }

    public String getBaseChartName() {
        return baseChartName;
    }

    public void setBaseChartName(String baseChartName) {
        this.baseChartName = baseChartName;
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

    public String getCrossSpareName() {
        return crossSpareName;
    }

    public void setCrossSpareName(String crossSpareName) {
        this.crossSpareName = crossSpareName;
    }

    public String getSpareFlag() {
        return spareFlag;
    }

    public void setSpareFlag(String spareFlag) {
        this.spareFlag = spareFlag;
    }

    public String getPairNbr() {
        return pairNbr;
    }

    public void setPairNbr(String pairNbr) {
        this.pairNbr = pairNbr;
    }

    public String getHighlineFlag() {
        return highlineFlag;
    }

    public void setHighlineFlag(String highlineFlag) {
        this.highlineFlag = highlineFlag;
    }

    public String getHighlineRule() {
        return highlineRule;
    }

    public void setHighlineRule(String highlineRule) {
        this.highlineRule = highlineRule;
    }

    public String getCommonlineRule() {
        return commonlineRule;
    }

    public void setCommonlineRule(String commonlineRule) {
        this.commonlineRule = commonlineRule;
    }

    public String getAppointWeek() {
        return appointWeek;
    }

    public void setAppointWeek(String appointWeek) {
        this.appointWeek = appointWeek;
    }

    public String getAppointDay() {
        return appointDay;
    }

    public void setAppointDay(String appointDay) {
        this.appointDay = appointDay;
    }

    public String getCrossSection() {
        return crossSection;
    }

    public void setCrossSection(String crossSection) {
        this.crossSection = crossSection;
    }

    public String getCheckPeople() {
        return checkPeople;
    }

    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
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

    public String getStartBureau() {
        return startBureau;
    }

    public void setStartBureau(String startBureau) {
        this.startBureau = startBureau;
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

    public String getTokenPsgDept() {
        return tokenPsgDept;
    }

    public void setTokenPsgDept(String tokenPsgDept) {
        this.tokenPsgDept = tokenPsgDept;
    }

    public String getTokenPsgBureau() {
        return tokenPsgBureau;
    }

    public void setTokenPsgBureau(String tokenPsgBureau) {
        this.tokenPsgBureau = tokenPsgBureau;
    }

    public String getLocoType() {
        return locoType;
    }

    public void setLocoType(String locoType) {
        this.locoType = locoType;
    }

    public String getCrhType() {
        return crhType;
    }

    public void setCrhType(String crhType) {
        this.crhType = crhType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatePeople() {
        return createPeople;
    }

    public void setCreatePeople(String createPeople) {
        this.createPeople = createPeople;
    }

    public String getCreatePeopleOrg() {
        return createPeopleOrg;
    }

    public void setCreatePeopleOrg(String createPeopleOrg) {
        this.createPeopleOrg = createPeopleOrg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public int getGroupTotalNbr() {
        return groupTotalNbr;
    }

    public void setGroupTotalNbr(int groupTotalNbr) {
        this.groupTotalNbr = groupTotalNbr;
    }

    public int getElecSupply() {
        return elecSupply;
    }

    public void setElecSupply(int elecSupply) {
        this.elecSupply = elecSupply;
    }

    public int getDejCollect() {
        return dejCollect;
    }

    public void setDejCollect(int dejCollect) {
        this.dejCollect = dejCollect;
    }

    public int getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(int airCondition) {
        this.airCondition = airCondition;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

	public int getIsAutoGenerate() {
		return isAutoGenerate;
	}

	public void setIsAutoGenerate(int isAutoGenerate) {
		this.isAutoGenerate = isAutoGenerate;
	}

	public String getCmCrossId() {
		return cmCrossId;
	}

	public void setCmCrossId(String cmCrossId) {
		this.cmCrossId = cmCrossId;
	}


}
