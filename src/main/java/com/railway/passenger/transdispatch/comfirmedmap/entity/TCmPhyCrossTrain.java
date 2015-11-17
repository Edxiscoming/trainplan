package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 物理交路列车开行安排
 */
public class TCmPhyCrossTrain {
    /**
     * 开行列车ID
     */
    private String cmPhyCrossTrainId;

    /**
     * 列车(车次)ID
     */
    private String cmTrainId;

    /**
     * 车底交路（物理交路）ID
     */
    private String cmPhyCrossId;

    /**
     * 开行日期（格式：yyyymmdd）
     */
    private String runDate;

    /**
     * 使用状态
     */
    private Short useStatus;
    /**
     * 物理交路的车次序号，与逻辑交路车次序号会有所不同
     */
    private Short trainSort;

    /**
     * @return 开行列车ID
     */
    public String getCmPhyCrossTrainId() {
        return cmPhyCrossTrainId;
    }

    /**
     * @param cmPhyCrossTrainId 
	 *            开行列车ID
     */
    public void setCmPhyCrossTrainId(String cmPhyCrossTrainId) {
        this.cmPhyCrossTrainId = cmPhyCrossTrainId;
    }

    /**
     * @return 列车(车次)ID
     */
    public String getCmTrainId() {
        return cmTrainId;
    }

    /**
     * @param cmTrainId 
	 *            列车(车次)ID
     */
    public void setCmTrainId(String cmTrainId) {
        this.cmTrainId = cmTrainId;
    }

    /**
     * @return 车底交路（物理交路）ID
     */
    public String getCmPhyCrossId() {
        return cmPhyCrossId;
    }

    /**
     * @param cmPhyCrossId 
	 *            车底交路（物理交路）ID
     */
    public void setCmPhyCrossId(String cmPhyCrossId) {
        this.cmPhyCrossId = cmPhyCrossId;
    }

    /**
     * @return 开行日期（格式：yyyymmdd）
     */
    public String getRunDate() {
        return runDate;
    }

    /**
     * @param runDate 
	 *            开行日期（格式：yyyymmdd）
     */
    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    /**
     * @return 使用状态
     */
    public Short getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            使用状态
     */
    public void setUseStatus(Short useStatus) {
        this.useStatus = useStatus;
    }
    /**
     * @return 物理交路的车次序号，与逻辑交路车次序号会有所不同
     */
    public Short getTrainSort() {
        return trainSort;
    }

    /**
     * @param trainSort 
	 *            物理交路的车次序号，与逻辑交路车次序号会有所不同
     */
    public void setTrainSort(Short trainSort) {
        this.trainSort = trainSort;
    }
}