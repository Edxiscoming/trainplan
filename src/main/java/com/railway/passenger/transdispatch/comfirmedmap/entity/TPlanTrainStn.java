package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 开行计划_列车内经由站
 */
public class TPlanTrainStn {
    /**
     * 列车经由车站ID（本表ID）
     */
    private String planTrainStnId;

    /**
     * 列车ID（对应PLAN_TRAIN表中的列车ID）
     */
    private String planTrainId;

    /**
     * 站序
     */
    private Short stnSort;

    /**
     * 车站ID
     */
    private String stnId;

    /**
     * 车站名
     */
    private String stnName;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 节点名
     */
    private String nodeName;

    /**
     * 节点TDCS对应ID
     */
    private String nodeTdcsId;

    /**
     * 节点TDCS对应名
     */
    private String nodeTdcsName;

    /**
     * 车站所属局ID
     */
    private String stnBureauId;

    /**
     * 车站所属局简称
     */
    private String stnBureau;

    /**
     * 车站所属局全称
     */
    private String stnBureauFull;

    /**
     * 到达车次
     */
    private String arrTrainNbr;

    /**
     * 到达时间 （格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String arrTime;

    /**
     * 图定到达时间（日期+图定到达时刻）（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String baseArrTime;

    /**
     * 运行天数_到达 （每过一次0点，天数加1，默认值0）
     */
    private Short arrRunDays;

    /**
     * 出发车次
     */
    private String dptTrainNbr;

    /**
     * 出发时间 （格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String dptTime;

    /**
     * 图定出发时间（日期+图定出发时刻）（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String baseDptTime;

    /**
     * 运行天数_出发（每过一次0点，天数加1，默认值0）
     */
    private Short runDays;

    /**
     * 股道号
     */
    private String trackNbr;

    /**
     * 股道名
     */
    private String trackName;

    /**
     * 站台
     */
    private Short platform;

    /**
     * 车站属性
     */
    private String jobs;

    /**
     * 车站类型（1:始发站；2:终到站；4:分界口）
     */
    private String stnType;

    /**
     * 客运作业标记（0：无；1：有）
     */
    private Short psgFlg;

    /**
     * 机务作业标记（0：无；1：司机换班；2：机车换挂；3：机车换挂和司机换班）
     */
    private Short locoFlag;

    /**
     * 技术作业类型（出库、入库、车底下线、到达车底、上水等）
     */
    private String tecType;

    /**
     * 分界口交接标记（0：接入；1：交出）
     */
    private Short boundaryInOut;

    /**
     * 上下行（S：上行；X：下行）
     */
    private String upDown;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * @return 列车经由车站ID（本表ID）
     */
    public String getPlanTrainStnId() {
        return planTrainStnId;
    }

    /**
     * @param planTrainStnId 
	 *            列车经由车站ID（本表ID）
     */
    public void setPlanTrainStnId(String planTrainStnId) {
        this.planTrainStnId = planTrainStnId;
    }

    /**
     * @return 列车ID（对应PLAN_TRAIN表中的列车ID）
     */
    public String getPlanTrainId() {
        return planTrainId;
    }

    /**
     * @param planTrainId 
	 *            列车ID（对应PLAN_TRAIN表中的列车ID）
     */
    public void setPlanTrainId(String planTrainId) {
        this.planTrainId = planTrainId;
    }

    /**
     * @return 站序
     */
    public Short getStnSort() {
        return stnSort;
    }

    /**
     * @param stnSort 
	 *            站序
     */
    public void setStnSort(Short stnSort) {
        this.stnSort = stnSort;
    }

    /**
     * @return 车站ID
     */
    public String getStnId() {
        return stnId;
    }

    /**
     * @param stnId 
	 *            车站ID
     */
    public void setStnId(String stnId) {
        this.stnId = stnId;
    }

    /**
     * @return 车站名
     */
    public String getStnName() {
        return stnName;
    }

    /**
     * @param stnName 
	 *            车站名
     */
    public void setStnName(String stnName) {
        this.stnName = stnName;
    }

    /**
     * @return 节点ID
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId 
	 *            节点ID
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @return 节点名
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName 
	 *            节点名
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return 节点TDCS对应ID
     */
    public String getNodeTdcsId() {
        return nodeTdcsId;
    }

    /**
     * @param nodeTdcsId 
	 *            节点TDCS对应ID
     */
    public void setNodeTdcsId(String nodeTdcsId) {
        this.nodeTdcsId = nodeTdcsId;
    }

    /**
     * @return 节点TDCS对应名
     */
    public String getNodeTdcsName() {
        return nodeTdcsName;
    }

    /**
     * @param nodeTdcsName 
	 *            节点TDCS对应名
     */
    public void setNodeTdcsName(String nodeTdcsName) {
        this.nodeTdcsName = nodeTdcsName;
    }

    /**
     * @return 车站所属局ID
     */
    public String getStnBureauId() {
        return stnBureauId;
    }

    /**
     * @param stnBureauId 
	 *            车站所属局ID
     */
    public void setStnBureauId(String stnBureauId) {
        this.stnBureauId = stnBureauId;
    }

    /**
     * @return 车站所属局简称
     */
    public String getStnBureau() {
        return stnBureau;
    }

    /**
     * @param stnBureau 
	 *            车站所属局简称
     */
    public void setStnBureau(String stnBureau) {
        this.stnBureau = stnBureau;
    }

    /**
     * @return 车站所属局全称
     */
    public String getStnBureauFull() {
        return stnBureauFull;
    }

    /**
     * @param stnBureauFull 
	 *            车站所属局全称
     */
    public void setStnBureauFull(String stnBureauFull) {
        this.stnBureauFull = stnBureauFull;
    }

    /**
     * @return 到达车次
     */
    public String getArrTrainNbr() {
        return arrTrainNbr;
    }

    /**
     * @param arrTrainNbr 
	 *            到达车次
     */
    public void setArrTrainNbr(String arrTrainNbr) {
        this.arrTrainNbr = arrTrainNbr;
    }

    /**
     * @return 到达时间 （格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getArrTime() {
        return arrTime;
    }

    /**
     * @param arrTime 
	 *            到达时间 （格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    /**
     * @return 图定到达时间（日期+图定到达时刻）（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getBaseArrTime() {
        return baseArrTime;
    }

    /**
     * @param baseArrTime 
	 *            图定到达时间（日期+图定到达时刻）（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setBaseArrTime(String baseArrTime) {
        this.baseArrTime = baseArrTime;
    }

    /**
     * @return 运行天数_到达 （每过一次0点，天数加1，默认值0）
     */
    public Short getArrRunDays() {
        return arrRunDays;
    }

    /**
     * @param arrRunDays 
	 *            运行天数_到达 （每过一次0点，天数加1，默认值0）
     */
    public void setArrRunDays(Short arrRunDays) {
        this.arrRunDays = arrRunDays;
    }

    /**
     * @return 出发车次
     */
    public String getDptTrainNbr() {
        return dptTrainNbr;
    }

    /**
     * @param dptTrainNbr 
	 *            出发车次
     */
    public void setDptTrainNbr(String dptTrainNbr) {
        this.dptTrainNbr = dptTrainNbr;
    }

    /**
     * @return 出发时间 （格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getDptTime() {
        return dptTime;
    }

    /**
     * @param dptTime 
	 *            出发时间 （格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setDptTime(String dptTime) {
        this.dptTime = dptTime;
    }

    /**
     * @return 图定出发时间（日期+图定出发时刻）（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getBaseDptTime() {
        return baseDptTime;
    }

    /**
     * @param baseDptTime 
	 *            图定出发时间（日期+图定出发时刻）（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setBaseDptTime(String baseDptTime) {
        this.baseDptTime = baseDptTime;
    }

    /**
     * @return 运行天数_出发（每过一次0点，天数加1，默认值0）
     */
    public Short getRunDays() {
        return runDays;
    }

    /**
     * @param runDays 
	 *            运行天数_出发（每过一次0点，天数加1，默认值0）
     */
    public void setRunDays(Short runDays) {
        this.runDays = runDays;
    }

    /**
     * @return 股道号
     */
    public String getTrackNbr() {
        return trackNbr;
    }

    /**
     * @param trackNbr 
	 *            股道号
     */
    public void setTrackNbr(String trackNbr) {
        this.trackNbr = trackNbr;
    }

    /**
     * @return 股道名
     */
    public String getTrackName() {
        return trackName;
    }

    /**
     * @param trackName 
	 *            股道名
     */
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    /**
     * @return 站台
     */
    public Short getPlatform() {
        return platform;
    }

    /**
     * @param platform 
	 *            站台
     */
    public void setPlatform(Short platform) {
        this.platform = platform;
    }

    /**
     * @return 车站属性
     */
    public String getJobs() {
        return jobs;
    }

    /**
     * @param jobs 
	 *            车站属性
     */
    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    /**
     * @return 车站类型（1:始发站；2:终到站；4:分界口）
     */
    public String getStnType() {
        return stnType;
    }

    /**
     * @param stnType 
	 *            车站类型（1:始发站；2:终到站；4:分界口）
     */
    public void setStnType(String stnType) {
        this.stnType = stnType;
    }

    /**
     * @return 客运作业标记（0：无；1：有）
     */
    public Short getPsgFlg() {
        return psgFlg;
    }

    /**
     * @param psgFlg 
	 *            客运作业标记（0：无；1：有）
     */
    public void setPsgFlg(Short psgFlg) {
        this.psgFlg = psgFlg;
    }

    /**
     * @return 机务作业标记（0：无；1：司机换班；2：机车换挂；3：机车换挂和司机换班）
     */
    public Short getLocoFlag() {
        return locoFlag;
    }

    /**
     * @param locoFlag 
	 *            机务作业标记（0：无；1：司机换班；2：机车换挂；3：机车换挂和司机换班）
     */
    public void setLocoFlag(Short locoFlag) {
        this.locoFlag = locoFlag;
    }

    /**
     * @return 技术作业类型（出库、入库、车底下线、到达车底、上水等）
     */
    public String getTecType() {
        return tecType;
    }

    /**
     * @param tecType 
	 *            技术作业类型（出库、入库、车底下线、到达车底、上水等）
     */
    public void setTecType(String tecType) {
        this.tecType = tecType;
    }

    /**
     * @return 分界口交接标记（0：接入；1：交出）
     */
    public Short getBoundaryInOut() {
        return boundaryInOut;
    }

    /**
     * @param boundaryInOut 
	 *            分界口交接标记（0：接入；1：交出）
     */
    public void setBoundaryInOut(Short boundaryInOut) {
        this.boundaryInOut = boundaryInOut;
    }

    /**
     * @return 上下行（S：上行；X：下行）
     */
    public String getUpDown() {
        return upDown;
    }

    /**
     * @param upDown 
	 *            上下行（S：上行；X：下行）
     */
    public void setUpDown(String upDown) {
        this.upDown = upDown;
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
}