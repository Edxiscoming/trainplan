package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 开行计划_列车
 */
public class TPlanTrain {
    /**
     * 列车ID
     */
    private String planTrainId;

    /**
     * 集中基本图车底交路ID
     */
    private String cmPhyCrossId;

    /**
     * 列车序号
     */
    private Short trainSort;

    /**
     * 开行日期（格式：yyyymmdd）
     */
    private String runDate;

    /**
     * 车次
     */
    private String trainNbr;

    /**
     * 始发局ID
     */
    private String startBureauId;

    /**
     * 始发局简称
     */
    private String startBureau;

    /**
     * 始发局全称
     */
    private String startBureauFull;

    /**
     * 始发站ID
     */
    private String startStationStnId;

    /**
     * 始发站名
     */
    private String startStationStnName;

    /**
     * 始发节点ID
     */
    private String startStnId;

    /**
     * 始发节点名
     */
    private String startStn;

    /**
     * 始发节点TDCS对应ID
     */
    private String startStnTdcsId;

    /**
     * 始发节点TDCS对应名
     */
    private String startStnTdcsName;

    /**
     * 始发时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String startTime;

    /**
     * 终到局ID
     */
    private String endBureauId;

    /**
     * 终到局简称
     */
    private String endBureau;

    /**
     * 终到局全称
     */
    private String endBureauFull;

    /**
     * 终到站ID
     */
    private String endStationStnId;

    /**
     * 终到站名
     */
    private String endStationStnName;

    /**
     * 终到节点ID
     */
    private String endStnId;

    /**
     * 终到节点名
     */
    private String endStn;

    /**
     * 终到节点TDCS对应ID
     */
    private String endStnTdcsId;

    /**
     * 终到节点TDCS对应名
     */
    private String endStnTdcsName;

    /**
     * 终到时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String endTime;

    /**
     * 终到运行天数
     */
    private String endDays;

    /**
     * 途经局（按顺序列出途经路局简称）
     */
    private String passBureau;

    /**
     * 经由ID（对应经由字典）
     */
    private String routeId;

    /**
     * 列车类型ID（对应列车类型字典中ID）
     */
    private String trainTypeId;

    /**
     * 客运或货运
     */
    private String business;

    /**
     * 高线标记（1:高线；0:普线；2:混合）
     */
    private Short highlineFlag;

    /**
     * 重点标记 （0：非重点；1：重点，默认值0）
     */
    private String importantFlag;

    /**
     * 列车范围（1:直通；0:管内）
     */
    private Short trainScope;

    /**
     * 车辆担当局
     */
    private String tokenVehBureau;

    /**
     * 客运担当局
     */
    private String tokenPsgBureau;

    /**
     * 编组ID（对应基本编组表中ID）
     */
    private String marshallingId;

    /**
     * 编组名
     */
    private String marshallingName;

    /**
     * 定员（列车总定员）
     */
    private Short trainCapacity;

    /**
     * 列车全路统一标识 （始发日期+始发车次+始发站+计划始发时刻）
     */
    private String planTrainSign;

    /**
     * 前续列车ID
     */
    private String preTrainId;

    /**
     * 后续列车ID
     */
    private String nextTrainId;

    /**
     * 备用及停运标记（1:开行；2:备用；9:停运）
     */
    private Short spareFlag;

    /**
     * 备用套跑标记（1:备用套跑）
     */
    private Short spareApplyFlag;

    /**
     * 发命令局（简称）
     */
    private String cmdBureau;

    /**
     * 命令主表ID
     */
    private Long cmdTxtmlid;

    /**
     * 命令子表ID
     */
    private Long cmdTxtmlitemid;

    /**
     * 命令/文电中列车ID（对应CMD_TRAIN表中的CMD_TRAIN_ID）
     */
    private String cmdTrainId;

    /**
     * 命令简要信息（发令日期+局令号+总公司令号）
     */
    private String cmdShortinfo;

    /**
     * 发文电局（简称）
     */
    private String telBureau;

    /**
     * 文电ID
     */
    private String telId;

    /**
     * 文电名
     */
    private String telName;

    /**
     * 文电简要信息（发文电日期+局文电名+总公司文电名）
     */
    private String telShortinfo;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建方式 （0:基本图；1:基本图滚动；2:文件电报；3:命令）
     */
    private Short creatType;

    /**
     * 创建人
     */
    private String creatPeople;

    /**
     * 创建人所属单位
     */
    private String creatPeopleOrg;

    /**
     * 创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String creatTime;

    /**
     * 生成运行线标记（0: 是；1 :否）
     */
    private String dailyplanFlag;

    /**
     * 生成运行线次数
     */
    private Short dailyplanTimes;

    /**
     * 生成运行线时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String dailyplanTime;

    /**
     * 运行线ID（从计划平台回填，对应运行线ID）
     */
    private String dailyplanId;

    /**
     * 上一次生成的运行线ID
     */
    private String dailyplanIdLast;

    /**
     * 运行线审核局（简称）
     */
    private String dailyplanCheckBureau;

    /**
     * 运行线落成路局（简称）
     */
    private String sentBureau;

    /**
     * 运行线落成路局_历史（简称）
     */
    private String sentBureauHis;

    /**
     * 校验状态
     */
    private Short vaildStatus;

    /**
     * 列车ID
     */
    private String cmTrainId;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * 编辑状态
     */
    private Short editFlag;

    /**
     * @return 列车ID
     */
    public String getPlanTrainId() {
        return planTrainId;
    }

    /**
     * @param planTrainId 
	 *            列车ID
     */
    public void setPlanTrainId(String planTrainId) {
        this.planTrainId = planTrainId;
    }

    /**
     * @return 集中基本图车底交路ID
     */
    public String getCmPhyCrossId() {
        return cmPhyCrossId;
    }

    /**
     * @param cmPhyCrossId 
	 *            集中基本图车底交路ID
     */
    public void setCmPhyCrossId(String cmPhyCrossId) {
        this.cmPhyCrossId = cmPhyCrossId;
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
     * @return 始发局ID
     */
    public String getStartBureauId() {
        return startBureauId;
    }

    /**
     * @param startBureauId 
	 *            始发局ID
     */
    public void setStartBureauId(String startBureauId) {
        this.startBureauId = startBureauId;
    }

    /**
     * @return 始发局简称
     */
    public String getStartBureau() {
        return startBureau;
    }

    /**
     * @param startBureau 
	 *            始发局简称
     */
    public void setStartBureau(String startBureau) {
        this.startBureau = startBureau;
    }

    /**
     * @return 始发局全称
     */
    public String getStartBureauFull() {
        return startBureauFull;
    }

    /**
     * @param startBureauFull 
	 *            始发局全称
     */
    public void setStartBureauFull(String startBureauFull) {
        this.startBureauFull = startBureauFull;
    }

    /**
     * @return 始发站ID
     */
    public String getStartStationStnId() {
        return startStationStnId;
    }

    /**
     * @param startStationStnId 
	 *            始发站ID
     */
    public void setStartStationStnId(String startStationStnId) {
        this.startStationStnId = startStationStnId;
    }

    /**
     * @return 始发站名
     */
    public String getStartStationStnName() {
        return startStationStnName;
    }

    /**
     * @param startStationStnName 
	 *            始发站名
     */
    public void setStartStationStnName(String startStationStnName) {
        this.startStationStnName = startStationStnName;
    }

    /**
     * @return 始发节点ID
     */
    public String getStartStnId() {
        return startStnId;
    }

    /**
     * @param startStnId 
	 *            始发节点ID
     */
    public void setStartStnId(String startStnId) {
        this.startStnId = startStnId;
    }

    /**
     * @return 始发节点名
     */
    public String getStartStn() {
        return startStn;
    }

    /**
     * @param startStn 
	 *            始发节点名
     */
    public void setStartStn(String startStn) {
        this.startStn = startStn;
    }

    /**
     * @return 始发节点TDCS对应ID
     */
    public String getStartStnTdcsId() {
        return startStnTdcsId;
    }

    /**
     * @param startStnTdcsId 
	 *            始发节点TDCS对应ID
     */
    public void setStartStnTdcsId(String startStnTdcsId) {
        this.startStnTdcsId = startStnTdcsId;
    }

    /**
     * @return 始发节点TDCS对应名
     */
    public String getStartStnTdcsName() {
        return startStnTdcsName;
    }

    /**
     * @param startStnTdcsName 
	 *            始发节点TDCS对应名
     */
    public void setStartStnTdcsName(String startStnTdcsName) {
        this.startStnTdcsName = startStnTdcsName;
    }

    /**
     * @return 始发时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime 
	 *            始发时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return 终到局ID
     */
    public String getEndBureauId() {
        return endBureauId;
    }

    /**
     * @param endBureauId 
	 *            终到局ID
     */
    public void setEndBureauId(String endBureauId) {
        this.endBureauId = endBureauId;
    }

    /**
     * @return 终到局简称
     */
    public String getEndBureau() {
        return endBureau;
    }

    /**
     * @param endBureau 
	 *            终到局简称
     */
    public void setEndBureau(String endBureau) {
        this.endBureau = endBureau;
    }

    /**
     * @return 终到局全称
     */
    public String getEndBureauFull() {
        return endBureauFull;
    }

    /**
     * @param endBureauFull 
	 *            终到局全称
     */
    public void setEndBureauFull(String endBureauFull) {
        this.endBureauFull = endBureauFull;
    }

    /**
     * @return 终到站ID
     */
    public String getEndStationStnId() {
        return endStationStnId;
    }

    /**
     * @param endStationStnId 
	 *            终到站ID
     */
    public void setEndStationStnId(String endStationStnId) {
        this.endStationStnId = endStationStnId;
    }

    /**
     * @return 终到站名
     */
    public String getEndStationStnName() {
        return endStationStnName;
    }

    /**
     * @param endStationStnName 
	 *            终到站名
     */
    public void setEndStationStnName(String endStationStnName) {
        this.endStationStnName = endStationStnName;
    }

    /**
     * @return 终到节点ID
     */
    public String getEndStnId() {
        return endStnId;
    }

    /**
     * @param endStnId 
	 *            终到节点ID
     */
    public void setEndStnId(String endStnId) {
        this.endStnId = endStnId;
    }

    /**
     * @return 终到节点名
     */
    public String getEndStn() {
        return endStn;
    }

    /**
     * @param endStn 
	 *            终到节点名
     */
    public void setEndStn(String endStn) {
        this.endStn = endStn;
    }

    /**
     * @return 终到节点TDCS对应ID
     */
    public String getEndStnTdcsId() {
        return endStnTdcsId;
    }

    /**
     * @param endStnTdcsId 
	 *            终到节点TDCS对应ID
     */
    public void setEndStnTdcsId(String endStnTdcsId) {
        this.endStnTdcsId = endStnTdcsId;
    }

    /**
     * @return 终到节点TDCS对应名
     */
    public String getEndStnTdcsName() {
        return endStnTdcsName;
    }

    /**
     * @param endStnTdcsName 
	 *            终到节点TDCS对应名
     */
    public void setEndStnTdcsName(String endStnTdcsName) {
        this.endStnTdcsName = endStnTdcsName;
    }

    /**
     * @return 终到时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime 
	 *            终到时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return 终到运行天数
     */
    public String getEndDays() {
        return endDays;
    }

    /**
     * @param endDays 
	 *            终到运行天数
     */
    public void setEndDays(String endDays) {
        this.endDays = endDays;
    }

    /**
     * @return 途经局（按顺序列出途经路局简称）
     */
    public String getPassBureau() {
        return passBureau;
    }

    /**
     * @param passBureau 
	 *            途经局（按顺序列出途经路局简称）
     */
    public void setPassBureau(String passBureau) {
        this.passBureau = passBureau;
    }

    /**
     * @return 经由ID（对应经由字典）
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * @param routeId 
	 *            经由ID（对应经由字典）
     */
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    /**
     * @return 列车类型ID（对应列车类型字典中ID）
     */
    public String getTrainTypeId() {
        return trainTypeId;
    }

    /**
     * @param trainTypeId 
	 *            列车类型ID（对应列车类型字典中ID）
     */
    public void setTrainTypeId(String trainTypeId) {
        this.trainTypeId = trainTypeId;
    }

    /**
     * @return 客运或货运
     */
    public String getBusiness() {
        return business;
    }

    /**
     * @param business 
	 *            客运或货运
     */
    public void setBusiness(String business) {
        this.business = business;
    }

    /**
     * @return 高线标记（1:高线；0:普线；2:混合）
     */
    public Short getHighlineFlag() {
        return highlineFlag;
    }

    /**
     * @param highlineFlag 
	 *            高线标记（1:高线；0:普线；2:混合）
     */
    public void setHighlineFlag(Short highlineFlag) {
        this.highlineFlag = highlineFlag;
    }

    /**
     * @return 重点标记 （0：非重点；1：重点，默认值0）
     */
    public String getImportantFlag() {
        return importantFlag;
    }

    /**
     * @param importantFlag 
	 *            重点标记 （0：非重点；1：重点，默认值0）
     */
    public void setImportantFlag(String importantFlag) {
        this.importantFlag = importantFlag;
    }

    /**
     * @return 列车范围（1:直通；0:管内）
     */
    public Short getTrainScope() {
        return trainScope;
    }

    /**
     * @param trainScope 
	 *            列车范围（1:直通；0:管内）
     */
    public void setTrainScope(Short trainScope) {
        this.trainScope = trainScope;
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
     * @return 客运担当局
     */
    public String getTokenPsgBureau() {
        return tokenPsgBureau;
    }

    /**
     * @param tokenPsgBureau 
	 *            客运担当局
     */
    public void setTokenPsgBureau(String tokenPsgBureau) {
        this.tokenPsgBureau = tokenPsgBureau;
    }

    /**
     * @return 编组ID（对应基本编组表中ID）
     */
    public String getMarshallingId() {
        return marshallingId;
    }

    /**
     * @param marshallingId 
	 *            编组ID（对应基本编组表中ID）
     */
    public void setMarshallingId(String marshallingId) {
        this.marshallingId = marshallingId;
    }

    /**
     * @return 编组名
     */
    public String getMarshallingName() {
        return marshallingName;
    }

    /**
     * @param marshallingName 
	 *            编组名
     */
    public void setMarshallingName(String marshallingName) {
        this.marshallingName = marshallingName;
    }

    /**
     * @return 定员（列车总定员）
     */
    public Short getTrainCapacity() {
        return trainCapacity;
    }

    /**
     * @param trainCapacity 
	 *            定员（列车总定员）
     */
    public void setTrainCapacity(Short trainCapacity) {
        this.trainCapacity = trainCapacity;
    }

    /**
     * @return 列车全路统一标识 （始发日期+始发车次+始发站+计划始发时刻）
     */
    public String getPlanTrainSign() {
        return planTrainSign;
    }

    /**
     * @param planTrainSign 
	 *            列车全路统一标识 （始发日期+始发车次+始发站+计划始发时刻）
     */
    public void setPlanTrainSign(String planTrainSign) {
        this.planTrainSign = planTrainSign;
    }

    /**
     * @return 前续列车ID
     */
    public String getPreTrainId() {
        return preTrainId;
    }

    /**
     * @param preTrainId 
	 *            前续列车ID
     */
    public void setPreTrainId(String preTrainId) {
        this.preTrainId = preTrainId;
    }

    /**
     * @return 后续列车ID
     */
    public String getNextTrainId() {
        return nextTrainId;
    }

    /**
     * @param nextTrainId 
	 *            后续列车ID
     */
    public void setNextTrainId(String nextTrainId) {
        this.nextTrainId = nextTrainId;
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
     * @return 发命令局（简称）
     */
    public String getCmdBureau() {
        return cmdBureau;
    }

    /**
     * @param cmdBureau 
	 *            发命令局（简称）
     */
    public void setCmdBureau(String cmdBureau) {
        this.cmdBureau = cmdBureau;
    }

    /**
     * @return 命令主表ID
     */
    public Long getCmdTxtmlid() {
        return cmdTxtmlid;
    }

    /**
     * @param cmdTxtmlid 
	 *            命令主表ID
     */
    public void setCmdTxtmlid(Long cmdTxtmlid) {
        this.cmdTxtmlid = cmdTxtmlid;
    }

    /**
     * @return 命令子表ID
     */
    public Long getCmdTxtmlitemid() {
        return cmdTxtmlitemid;
    }

    /**
     * @param cmdTxtmlitemid 
	 *            命令子表ID
     */
    public void setCmdTxtmlitemid(Long cmdTxtmlitemid) {
        this.cmdTxtmlitemid = cmdTxtmlitemid;
    }

    /**
     * @return 命令/文电中列车ID（对应CMD_TRAIN表中的CMD_TRAIN_ID）
     */
    public String getCmdTrainId() {
        return cmdTrainId;
    }

    /**
     * @param cmdTrainId 
	 *            命令/文电中列车ID（对应CMD_TRAIN表中的CMD_TRAIN_ID）
     */
    public void setCmdTrainId(String cmdTrainId) {
        this.cmdTrainId = cmdTrainId;
    }

    /**
     * @return 命令简要信息（发令日期+局令号+总公司令号）
     */
    public String getCmdShortinfo() {
        return cmdShortinfo;
    }

    /**
     * @param cmdShortinfo 
	 *            命令简要信息（发令日期+局令号+总公司令号）
     */
    public void setCmdShortinfo(String cmdShortinfo) {
        this.cmdShortinfo = cmdShortinfo;
    }

    /**
     * @return 发文电局（简称）
     */
    public String getTelBureau() {
        return telBureau;
    }

    /**
     * @param telBureau 
	 *            发文电局（简称）
     */
    public void setTelBureau(String telBureau) {
        this.telBureau = telBureau;
    }

    /**
     * @return 文电ID
     */
    public String getTelId() {
        return telId;
    }

    /**
     * @param telId 
	 *            文电ID
     */
    public void setTelId(String telId) {
        this.telId = telId;
    }

    /**
     * @return 文电名
     */
    public String getTelName() {
        return telName;
    }

    /**
     * @param telName 
	 *            文电名
     */
    public void setTelName(String telName) {
        this.telName = telName;
    }

    /**
     * @return 文电简要信息（发文电日期+局文电名+总公司文电名）
     */
    public String getTelShortinfo() {
        return telShortinfo;
    }

    /**
     * @param telShortinfo 
	 *            文电简要信息（发文电日期+局文电名+总公司文电名）
     */
    public void setTelShortinfo(String telShortinfo) {
        this.telShortinfo = telShortinfo;
    }

    /**
     * @return 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note 
	 *            备注
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return 创建方式 （0:基本图；1:基本图滚动；2:文件电报；3:命令）
     */
    public Short getCreatType() {
        return creatType;
    }

    /**
     * @param creatType 
	 *            创建方式 （0:基本图；1:基本图滚动；2:文件电报；3:命令）
     */
    public void setCreatType(Short creatType) {
        this.creatType = creatType;
    }

    /**
     * @return 创建人
     */
    public String getCreatPeople() {
        return creatPeople;
    }

    /**
     * @param creatPeople 
	 *            创建人
     */
    public void setCreatPeople(String creatPeople) {
        this.creatPeople = creatPeople;
    }

    /**
     * @return 创建人所属单位
     */
    public String getCreatPeopleOrg() {
        return creatPeopleOrg;
    }

    /**
     * @param creatPeopleOrg 
	 *            创建人所属单位
     */
    public void setCreatPeopleOrg(String creatPeopleOrg) {
        this.creatPeopleOrg = creatPeopleOrg;
    }

    /**
     * @return 创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getCreatTime() {
        return creatTime;
    }

    /**
     * @param creatTime 
	 *            创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    /**
     * @return 生成运行线标记（0: 是；1 :否）
     */
    public String getDailyplanFlag() {
        return dailyplanFlag;
    }

    /**
     * @param dailyplanFlag 
	 *            生成运行线标记（0: 是；1 :否）
     */
    public void setDailyplanFlag(String dailyplanFlag) {
        this.dailyplanFlag = dailyplanFlag;
    }

    /**
     * @return 生成运行线次数
     */
    public Short getDailyplanTimes() {
        return dailyplanTimes;
    }

    /**
     * @param dailyplanTimes 
	 *            生成运行线次数
     */
    public void setDailyplanTimes(Short dailyplanTimes) {
        this.dailyplanTimes = dailyplanTimes;
    }

    /**
     * @return 生成运行线时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getDailyplanTime() {
        return dailyplanTime;
    }

    /**
     * @param dailyplanTime 
	 *            生成运行线时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setDailyplanTime(String dailyplanTime) {
        this.dailyplanTime = dailyplanTime;
    }

    /**
     * @return 运行线ID（从计划平台回填，对应运行线ID）
     */
    public String getDailyplanId() {
        return dailyplanId;
    }

    /**
     * @param dailyplanId 
	 *            运行线ID（从计划平台回填，对应运行线ID）
     */
    public void setDailyplanId(String dailyplanId) {
        this.dailyplanId = dailyplanId;
    }

    /**
     * @return 上一次生成的运行线ID
     */
    public String getDailyplanIdLast() {
        return dailyplanIdLast;
    }

    /**
     * @param dailyplanIdLast 
	 *            上一次生成的运行线ID
     */
    public void setDailyplanIdLast(String dailyplanIdLast) {
        this.dailyplanIdLast = dailyplanIdLast;
    }

    /**
     * @return 运行线审核局（简称）
     */
    public String getDailyplanCheckBureau() {
        return dailyplanCheckBureau;
    }

    /**
     * @param dailyplanCheckBureau 
	 *            运行线审核局（简称）
     */
    public void setDailyplanCheckBureau(String dailyplanCheckBureau) {
        this.dailyplanCheckBureau = dailyplanCheckBureau;
    }

    /**
     * @return 运行线落成路局（简称）
     */
    public String getSentBureau() {
        return sentBureau;
    }

    /**
     * @param sentBureau 
	 *            运行线落成路局（简称）
     */
    public void setSentBureau(String sentBureau) {
        this.sentBureau = sentBureau;
    }

    /**
     * @return 运行线落成路局_历史（简称）
     */
    public String getSentBureauHis() {
        return sentBureauHis;
    }

    /**
     * @param sentBureauHis 
	 *            运行线落成路局_历史（简称）
     */
    public void setSentBureauHis(String sentBureauHis) {
        this.sentBureauHis = sentBureauHis;
    }

    /**
     * @return 校验状态
     */
    public Short getVaildStatus() {
        return vaildStatus;
    }

    /**
     * @param vaildStatus 
	 *            校验状态
     */
    public void setVaildStatus(Short vaildStatus) {
        this.vaildStatus = vaildStatus;
    }

    /**
     * @return 列车ID
     */
    public String getCmTrainId() {
        return cmTrainId;
    }

    /**
     * @param cmTrainId 
	 *            列车ID
     */
    public void setCmTrainId(String cmTrainId) {
        this.cmTrainId = cmTrainId;
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