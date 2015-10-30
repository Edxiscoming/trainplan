package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 集中基本图列车信息
 */
public class TCmTrain {
    /**
     * 集中基本图列车ID（本表ID）
     */
    private String cmTrainId;

    /**
     * 集中基本图交路ID
     */
    private String cmCrossId;

    /**
     * 列车序号
     */
    private Short trainSort;

    /**
     * 车次
     */
    private String trainNbr;

    /**
     * 基本图中列车ID
     */
    private String baseTrainId;

    /**
     * 始发站名
     */
    private String startStn;

    /**
     * 始发局（局码）
     */
    private String startBureau;

    /**
     * 终到站
     */
    private String endStn;

    /**
     * 终到局 （局码）
     */
    private String endBureau;

    /**
     * 备用及停运标记（1:开行；2:备用；9:停运）
     */
    private Short spareFlag;

    /**
     * 备用套跑标记（1:备用套跑）
     */
    private Short spareApplyFlag;

    /**
     * 车辆担当局
     */
    private String tokenVehBureau;

    /**
     * 列车使用状态
     */
    private Short useStatus;

    /**
     * 编辑状态
     */
    private Short editFlag;
    
	 private String startTime;
	 private String  endTime;
	 private int  runDays;
	 
	 

    public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getRunDays() {
		return runDays;
	}

	public void setRunDays(int runDays) {
		this.runDays = runDays;
	}

	/**
     * @return 集中基本图列车ID（本表ID）
     */
    public String getCmTrainId() {
        return cmTrainId;
    }

    /**
     * @param cmTrainId 
	 *            集中基本图列车ID（本表ID）
     */
    public void setCmTrainId(String cmTrainId) {
        this.cmTrainId = cmTrainId;
    }

    /**
     * @return 集中基本图交路ID
     */
    public String getCmCrossId() {
        return cmCrossId;
    }

    /**
     * @param cmCrossId 
	 *            集中基本图交路ID
     */
    public void setCmCrossId(String cmCrossId) {
        this.cmCrossId = cmCrossId;
    }

    /**
     * @return 列车序号
     */
    public Short getTrainSort() {
        return trainSort;
    }

    /**
     * @param trainSort 
	 *            列车序号
     */
    public void setTrainSort(Short trainSort) {
        this.trainSort = trainSort;
    }

    /**
     * @return 车次
     */
    public String getTrainNbr() {
        return trainNbr;
    }

    /**
     * @param trainNbr 
	 *            车次
     */
    public void setTrainNbr(String trainNbr) {
        this.trainNbr = trainNbr;
    }

    /**
     * @return 基本图中列车ID
     */
    public String getBaseTrainId() {
        return baseTrainId;
    }

    /**
     * @param baseTrainId 
	 *            基本图中列车ID
     */
    public void setBaseTrainId(String baseTrainId) {
        this.baseTrainId = baseTrainId;
    }

    /**
     * @return 始发站名
     */
    public String getStartStn() {
        return startStn;
    }

    /**
     * @param startStn 
	 *            始发站名
     */
    public void setStartStn(String startStn) {
        this.startStn = startStn;
    }

    /**
     * @return 始发局（局码）
     */
    public String getStartBureau() {
        return startBureau;
    }

    /**
     * @param startBureau 
	 *            始发局（局码）
     */
    public void setStartBureau(String startBureau) {
        this.startBureau = startBureau;
    }

    /**
     * @return 终到站
     */
    public String getEndStn() {
        return endStn;
    }

    /**
     * @param endStn 
	 *            终到站
     */
    public void setEndStn(String endStn) {
        this.endStn = endStn;
    }

    /**
     * @return 终到局 （局码）
     */
    public String getEndBureau() {
        return endBureau;
    }

    /**
     * @param endBureau 
	 *            终到局 （局码）
     */
    public void setEndBureau(String endBureau) {
        this.endBureau = endBureau;
    }

    /**
     * @return 备用及停运标记（1:开行；2:备用；9:停运）
     */
    public Short getSpareFlag() {
        return spareFlag;
    }

    /**
     * @param spareFlag 
	 *            备用及停运标记（1:开行；2:备用；9:停运）
     */
    public void setSpareFlag(Short spareFlag) {
        this.spareFlag = spareFlag;
    }

    /**
     * @return 备用套跑标记（1:备用套跑）
     */
    public Short getSpareApplyFlag() {
        return spareApplyFlag;
    }

    /**
     * @param spareApplyFlag 
	 *            备用套跑标记（1:备用套跑）
     */
    public void setSpareApplyFlag(Short spareApplyFlag) {
        this.spareApplyFlag = spareApplyFlag;
    }

    /**
     * @return 车辆担当局
     */
    public String getTokenVehBureau() {
        return tokenVehBureau;
    }

    /**
     * @param tokenVehBureau 
	 *            车辆担当局
     */
    public void setTokenVehBureau(String tokenVehBureau) {
        this.tokenVehBureau = tokenVehBureau;
    }

    /**
     * @return 列车使用状态
     */
    public Short getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            列车使用状态
     */
    public void setUseStatus(Short useStatus) {
        this.useStatus = useStatus;
    }

    /**
     * @return 编辑状态
     */
    public Short getEditFlag() {
        return editFlag;
    }

    /**
     * @param editFlag 
	 *            编辑状态
     */
    public void setEditFlag(Short editFlag) {
        this.editFlag = editFlag;
    }
}