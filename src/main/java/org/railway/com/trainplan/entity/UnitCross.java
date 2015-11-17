package org.railway.com.trainplan.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * UnitCross实体类
 * Created by speeder on 2014/5/28.
 */
public class UnitCross {

    private String unitCrossId;

    private String baseCrossId;

    private String baseChartId;

    private String baseChartName;

    private String crossStartDate;

    private String crossEndDate;

    private String crossName;

    private String crossSpareName;

    private String alternateDate;

    private String alternateTrainNbr;

    private String spareFlag;

    private int cutOld;

    private int groupTotalNbr;

    private String pairNbr;

    private String highlineFlag;

    private String highlineRule;

    private String commonlineRule;

    private String appointWeek;

    private String appointDay;

    private String appointPeriod;

    private String crossSection;

    private String throughLine;

    private String startBureau;

    private String relevantBureau;

    private String tokenVehBureau;

    private String tokenVehDept;

    private String tokenVehDepot;

    private String tokenPsgBureau;

    private String tokenPsgDept;

    private String locoType;

    private String  crhType;

    private int elecSupply;

    private int dejCollect;

    private int airCondition;

    private String note;

    private String createPeople;

    private String createPeopleOrg;

    private String createTime;

    private String checkPeople;

    private String checkPeopleOrg;

    private String checkTime;

    private String createCrossPeople;

    private String createCrossPeopleOrg;

    private String createCrossTime;

    private String planCrossId;

    private List<UnitCrossTrain> unitCrossTrainList;
    
    private String trainsort;

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

    public String getAlternateDate() {
        return alternateDate;
    }

    public void setAlternateDate(String alternateDate) {
        this.alternateDate = alternateDate;
    }

    public String getAlternateTrainNbr() {
        return alternateTrainNbr;
    }

    public void setAlternateTrainNbr(String alternateTrainNbr) {
        this.alternateTrainNbr = alternateTrainNbr;
    }

    public String getSpareFlag() {
        return spareFlag;
    }

    public void setSpareFlag(String spareFlag) {
        this.spareFlag = spareFlag;
    }

    public int getCutOld() {
        return cutOld;
    }

    public void setCutOld(int cutOld) {
        this.cutOld = cutOld;
    }

    public int getGroupTotalNbr() {
        return groupTotalNbr;
    }

    public void setGroupTotalNbr(int groupTotalNbr) {
        this.groupTotalNbr = groupTotalNbr;
    }

    public String getPairNbr() {
        return pairNbr;
    }

    public void setPairNbr(String pairNbr) {
        this.pairNbr = pairNbr;
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

    public String getAppointPeriod() {
        return appointPeriod;
    }

    public void setAppointPeriod(String appointPeriod) {
        this.appointPeriod = appointPeriod;
    }

    public String getCrossSection() {
        return crossSection;
    }

    public void setCrossSection(String crossSection) {
        this.crossSection = crossSection;
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

    public String getCreateCrossPeople() {
        return createCrossPeople;
    }

    public void setCreateCrossPeople(String createCrossPeople) {
        this.createCrossPeople = createCrossPeople;
    }

    public String getCreateCrossPeopleOrg() {
        return createCrossPeopleOrg;
    }

    public void setCreateCrossPeopleOrg(String createCrossPeopleOrg) {
        this.createCrossPeopleOrg = createCrossPeopleOrg;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCreateCrossTime() {
        return createCrossTime;
    }

    public void setCreateCrossTime(String createCrossTime) {
        this.createCrossTime = createCrossTime;
    }

    public String getPlanCrossId() {
        return planCrossId;
    }

    public void setPlanCrossId(String planCrossId) {
        this.planCrossId = planCrossId;
    }

    public List<UnitCrossTrain> getUnitCrossTrainList() {
        return unitCrossTrainList;
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

    public String getThroughLine() {
		return throughLine;
	}

	public void setThroughLine(String throughLine) {
		this.throughLine = throughLine;
	}

	public String getStartBureau() {
        return startBureau;
    }

    public void setStartBureau(String startBureau) {
        this.startBureau = startBureau;
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

    public String getTokenPsgBureau() {
        return tokenPsgBureau;
    }

    public void setTokenPsgBureau(String tokenPsgBureau) {
        this.tokenPsgBureau = tokenPsgBureau;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUnitCrossTrainList(List<UnitCrossTrain> unitCrossTrainList) {
        this.unitCrossTrainList = unitCrossTrainList;
    }

	public String getTrainsort() {
		return trainsort;
	}

	public void setTrainsort(String trainsort) {
		this.trainsort = trainsort;
	}

}
