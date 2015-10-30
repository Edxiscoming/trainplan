package org.railway.com.trainplan.entity;

import java.util.List;

/**
 * Created by speeder on 2014/5/28.
 */
public class PlanCross {

    private String planCrossId;

    private String unitCrossId;

    private String baseChartId;

    private int groupTotalNbr;

    private int groupSerialNbr;

    private String marshallingName;

    private int trainSort;

    private String crossStartDate;

    private String crossEndDate;

    private List<UnitCrossTrain> unitCrossTrainList;
    
    /**
     * 交路名.
     */
    private String crossName;
    /**
     * 相关局.
     */
    private String relevantBureau;
    
    /**
     * plancross对应的逻辑交路id
     */
    private String cmCrossId;

    public String getPlanCrossId() {
        return planCrossId;
    }

    public int getGroupTotalNbr() {
        return groupTotalNbr;
    }

    public void setGroupTotalNbr(int groupTotalNbr) {
        this.groupTotalNbr = groupTotalNbr;
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

    public List<UnitCrossTrain> getUnitCrossTrainList() {
        return unitCrossTrainList;
    }

    public void setUnitCrossTrainList(List<UnitCrossTrain> unitCrossTrainList) {
        this.unitCrossTrainList = unitCrossTrainList;
    }

    public int getGroupSerialNbr() {
        return groupSerialNbr;
    }

    public void setGroupSerialNbr(int groupSerialNbr) {
        this.groupSerialNbr = groupSerialNbr;
    }

    public String getMarshallingName() {
        return marshallingName;
    }

    public void setMarshallingName(String marshallingName) {
        this.marshallingName = marshallingName;
    }

    public int getTrainSort() {
        return trainSort;
    }

    public void setTrainSort(int trainSort) {
        this.trainSort = trainSort;
    }

    public String getBaseChartId() {
        return baseChartId;
    }

    public void setBaseChartId(String baseChartId) {
        this.baseChartId = baseChartId;
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

	public String getRelevantBureau() {
		return relevantBureau;
	}

	public void setRelevantBureau(String relevantBureau) {
		this.relevantBureau = relevantBureau;
	}

	public String getCmCrossId() {
		return cmCrossId;
	}

	public void setCmCrossId(String cmCrossId) {
		this.cmCrossId = cmCrossId;
	}
}
