package com.railway.passenger.transdispatch.comfirmedmap.entity;

import java.util.UUID;

/**
 * 经由局原始对数表信息
 */
public class TCmPartOriginalCross {
    /**
     * 原始对数表信息ID（本表ID）
     */
    private String cmPartOriginalCrossId = UUID.randomUUID().toString();

    /**
     * 基本方案ID
     */
    private String cmVersionId;

    /**
     * 车底交路名
     */
    private String crossName ;

    /**
     * 备用套跑交路名
     */
    private String crossSpareName ;

    /**
     * 交替日期
     */
    private String alternateDate ;

    /**
     * 交替原车次
     */
    private String alternateTrainNbr ;

    /**
     * 备用及停运标记（1:开行；2:备用；0:停运）
     */
    private String spareFlag  ;

    /**
     * 是否截断原交路（1:是；0:否）
     */
    private int cutOld  ;

    /**
     * 组数（共需几组车底担当）
     */
    private int groupTotalNbr  ;

    /**
     * 对数（1:一对；0.5:半对，2/3表示三天开两天）
     */
    private String pairNbr  ;

    /**
     * 高线标记（1:高线；0:普线；2:混合）
     */
    private String highlineFlag  ;

    /**
     * 高线开行规律（1:平日;2:周末;3:高峰）
     */
    private String highlineRule  ;

    /**
     * 普线开行规律（1:每日;2:隔日）
     */
    private String commonlineRule  ;

    /**
     * 指定星期
     */
    private String appointWeek  ;

    /**
     * 指定日期
     */
    private String appointDay  ;

    /**
     * 指定周期
     */
    private String appointPeriod  ;

    /**
     * 运行区段
     */
    private String crossSection  ;

    /**
     * 经由铁路线（多条线用逗号分隔）
     */
    private String throughLine  ;

    /**
     * 车辆担当局（局码）
     */
    private String tokenVehBureau  ;

    /**
     * 担当车辆段/动车段
     */
    private String tokenVehDept  ;

    /**
     * 担当动车所（用于高铁）
     */
    private String tokenVehDepot  ;

    /**
     * 客运担当局（局码）
     */
    private String tokenPsgBureau  ;

    /**
     * 担当客运段
     */
    private String tokenPsgDept  ;

    /**
     * 机车类型（客机、货机）
     */
    private String locoType  ;

    /**
     * 动车组车型（用于高铁）
     */
    private String crhType  ;

    /**
     * 供电（1:是；0:否）
     */
    private int elecSupply  ;

    /**
     * 集便（1:是；0:否）
     */
    private int dejCollect  ;

    /**
     * 空调（1:是；0:否）
     */
    private int airCondition  ;

    /**
     * 备注
     */
    private String note  ;

    /**
     * 创建人
     */
    private String creatPeople  ;

    /**
     * 创建人单位
     */
    private String creatPeopleOrg  ;

    /**
     * 创建时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String creatTime  ;

    /**
     * 审核人
     */
    private String checkPeople  ;

    /**
     * 审核人单位
     */
    private String checkPeopleOrg  ;

    /**
     * 审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    private String checkTime  ;

    /**
     * 生成交路时间
     */
    private String creatCrossTime  ;

    /**
     * 列车等级（高速，普速，混合）
     */
    private String crossLevel  ;

    /**
     * 列车运行公里数
     */
    private Long runRange  ;

    /**
     * 列车编组辆数
     */
    private int marshallingNums  ;

    /**
     * 列车核定乘员
     */
    private Integer peopleNums  ;

    /**
     * 编组内容
     */
    private String marshallingContent  ;

    /**
     * 列车运行相关局的局码
     */
    private String relevantBureau  ;

    /**
     * 审核标志
     */
    private int checkFlag  ;

    /**
     * 有效状态
     */
    private int useStatus  ;

    /**
     * 创建局
     */
    private String createBureau  ;

    /**
     * @return 原始对数表信息ID（本表ID）
     */
    public String getCmPartOriginalCrossId() {
        return cmPartOriginalCrossId;
    }

    /**
     * @param cmPartOriginalCrossId 
	 *            原始对数表信息ID（本表ID）
     */
    public void setCmPartOriginalCrossId(String cmPartOriginalCrossId) {
        this.cmPartOriginalCrossId = cmPartOriginalCrossId;
    }

    /**
     * @return 基本方案ID
     */
    public String getCmVersionId() {
        return cmVersionId;
    }

    /**
     * @param cmVersionId 
	 *            基本方案ID
     */
    public void setCmVersionId(String cmVersionId) {
        this.cmVersionId = cmVersionId;
    }

    /**
     * @return 车底交路名
     */
    public String getCrossName() {
        return crossName;
    }

    /**
     * @param crossName 
	 *            车底交路名
     */
    public void setCrossName(String crossName) {
        this.crossName = crossName;
    }

    /**
     * @return 备用套跑交路名
     */
    public String getCrossSpareName() {
        return crossSpareName;
    }

    /**
     * @param crossSpareName 
	 *            备用套跑交路名
     */
    public void setCrossSpareName(String crossSpareName) {
        this.crossSpareName = crossSpareName;
    }

    /**
     * @return 交替日期
     */
    public String getAlternateDate() {
        return alternateDate;
    }

    /**
     * @param alternateDate 
	 *            交替日期
     */
    public void setAlternateDate(String alternateDate) {
        this.alternateDate = alternateDate;
    }

    /**
     * @return 交替原车次
     */
    public String getAlternateTrainNbr() {
        return alternateTrainNbr;
    }

    /**
     * @param alternateTrainNbr 
	 *            交替原车次
     */
    public void setAlternateTrainNbr(String alternateTrainNbr) {
        this.alternateTrainNbr = alternateTrainNbr;
    }

    /**
     * @return 备用及停运标记（1:开行；2:备用；0:停运）
     */
    public String getSpareFlag() {
        return spareFlag;
    }

    /**
     * @param spareFlag 
	 *            备用及停运标记（1:开行；2:备用；0:停运）
     */
    public void setSpareFlag(String spareFlag) {
        this.spareFlag = spareFlag;
    }

    /**
     * @return 是否截断原交路（1:是；0:否）
     */
    public int getCutOld() {
        return cutOld;
    }

    /**
     * @param cutOld 
	 *            是否截断原交路（1:是；0:否）
     */
    public void setCutOld(int cutOld) {
        this.cutOld = cutOld;
    }

    /**
     * @return 组数（共需几组车底担当）
     */
    public int getGroupTotalNbr() {
        return groupTotalNbr;
    }

    /**
     * @param groupTotalNbr 
	 *            组数（共需几组车底担当）
     */
    public void setGroupTotalNbr(int groupTotalNbr) {
        this.groupTotalNbr = groupTotalNbr;
    }

    /**
     * @return 对数（1:一对；0.5:半对，2/3表示三天开两天）
     */
    public String getPairNbr() {
        return pairNbr;
    }

    /**
     * @param pairNbr 
	 *            对数（1:一对；0.5:半对，2/3表示三天开两天）
     */
    public void setPairNbr(String pairNbr) {
        this.pairNbr = pairNbr;
    }

    /**
     * @return 高线标记（1:高线；0:普线；2:混合）
     */
    public String getHighlineFlag() {
        return highlineFlag;
    }

    /**
     * @param highlineFlag 
	 *            高线标记（1:高线；0:普线；2:混合）
     */
    public void setHighlineFlag(String highlineFlag) {
        this.highlineFlag = highlineFlag;
    }

    /**
     * @return 高线开行规律（1:平日;2:周末;3:高峰）
     */
    public String getHighlineRule() {
        return highlineRule;
    }

    /**
     * @param highlineRule 
	 *            高线开行规律（1:平日;2:周末;3:高峰）
     */
    public void setHighlineRule(String highlineRule) {
        this.highlineRule = highlineRule;
    }

    /**
     * @return 普线开行规律（1:每日;2:隔日）
     */
    public String getCommonlineRule() {
        return commonlineRule;
    }

    /**
     * @param commonlineRule 
	 *            普线开行规律（1:每日;2:隔日）
     */
    public void setCommonlineRule(String commonlineRule) {
        this.commonlineRule = commonlineRule;
    }

    /**
     * @return 指定星期
     */
    public String getAppointWeek() {
        return appointWeek;
    }

    /**
     * @param appointWeek 
	 *            指定星期
     */
    public void setAppointWeek(String appointWeek) {
        this.appointWeek = appointWeek;
    }

    /**
     * @return 指定日期
     */
    public String getAppointDay() {
        return appointDay;
    }

    /**
     * @param appointDay 
	 *            指定日期
     */
    public void setAppointDay(String appointDay) {
        this.appointDay = appointDay;
    }

    /**
     * @return 指定周期
     */
    public String getAppointPeriod() {
        return appointPeriod;
    }

    /**
     * @param appointPeriod 
	 *            指定周期
     */
    public void setAppointPeriod(String appointPeriod) {
        this.appointPeriod = appointPeriod;
    }

    /**
     * @return 运行区段
     */
    public String getCrossSection() {
        return crossSection;
    }

    /**
     * @param crossSection 
	 *            运行区段
     */
    public void setCrossSection(String crossSection) {
        this.crossSection = crossSection;
    }

    /**
     * @return 经由铁路线（多条线用逗号分隔）
     */
    public String getThroughLine() {
        return throughLine;
    }

    /**
     * @param throughLine 
	 *            经由铁路线（多条线用逗号分隔）
     */
    public void setThroughLine(String throughLine) {
        this.throughLine = throughLine;
    }

    /**
     * @return 车辆担当局（局码）
     */
    public String getTokenVehBureau() {
        return tokenVehBureau;
    }

    /**
     * @param tokenVehBureau 
	 *            车辆担当局（局码）
     */
    public void setTokenVehBureau(String tokenVehBureau) {
        this.tokenVehBureau = tokenVehBureau;
    }

    /**
     * @return 担当车辆段/动车段
     */
    public String getTokenVehDept() {
        return tokenVehDept;
    }

    /**
     * @param tokenVehDept 
	 *            担当车辆段/动车段
     */
    public void setTokenVehDept(String tokenVehDept) {
        this.tokenVehDept = tokenVehDept;
    }

    /**
     * @return 担当动车所（用于高铁）
     */
    public String getTokenVehDepot() {
        return tokenVehDepot;
    }

    /**
     * @param tokenVehDepot 
	 *            担当动车所（用于高铁）
     */
    public void setTokenVehDepot(String tokenVehDepot) {
        this.tokenVehDepot = tokenVehDepot;
    }

    /**
     * @return 客运担当局（局码）
     */
    public String getTokenPsgBureau() {
        return tokenPsgBureau;
    }

    /**
     * @param tokenPsgBureau 
	 *            客运担当局（局码）
     */
    public void setTokenPsgBureau(String tokenPsgBureau) {
        this.tokenPsgBureau = tokenPsgBureau;
    }

    /**
     * @return 担当客运段
     */
    public String getTokenPsgDept() {
        return tokenPsgDept;
    }

    /**
     * @param tokenPsgDept 
	 *            担当客运段
     */
    public void setTokenPsgDept(String tokenPsgDept) {
        this.tokenPsgDept = tokenPsgDept;
    }

    /**
     * @return 机车类型（客机、货机）
     */
    public String getLocoType() {
        return locoType;
    }

    /**
     * @param locoType 
	 *            机车类型（客机、货机）
     */
    public void setLocoType(String locoType) {
        this.locoType = locoType;
    }

    /**
     * @return 动车组车型（用于高铁）
     */
    public String getCrhType() {
        return crhType;
    }

    /**
     * @param crhType 
	 *            动车组车型（用于高铁）
     */
    public void setCrhType(String crhType) {
        this.crhType = crhType;
    }

    /**
     * @return 供电（1:是；0:否）
     */
    public int getElecSupply() {
        return elecSupply;
    }

    /**
     * @param elecSupply 
	 *            供电（1:是；0:否）
     */
    public void setElecSupply(int elecSupply) {
        this.elecSupply = elecSupply;
    }

    /**
     * @return 集便（1:是；0:否）
     */
    public int getDejCollect() {
        return dejCollect;
    }

    /**
     * @param dejCollect 
	 *            集便（1:是；0:否）
     */
    public void setDejCollect(int dejCollect) {
        this.dejCollect = dejCollect;
    }

    /**
     * @return 空调（1:是；0:否）
     */
    public int getAirCondition() {
        return airCondition;
    }

    /**
     * @param airCondition 
	 *            空调（1:是；0:否）
     */
    public void setAirCondition(int airCondition) {
        this.airCondition = airCondition;
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
     * @return 创建人单位
     */
    public String getCreatPeopleOrg() {
        return creatPeopleOrg;
    }

    /**
     * @param creatPeopleOrg 
	 *            创建人单位
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
     * @return 审核人
     */
    public String getCheckPeople() {
        return checkPeople;
    }

    /**
     * @param checkPeople 
	 *            审核人
     */
    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    /**
     * @return 审核人单位
     */
    public String getCheckPeopleOrg() {
        return checkPeopleOrg;
    }

    /**
     * @param checkPeopleOrg 
	 *            审核人单位
     */
    public void setCheckPeopleOrg(String checkPeopleOrg) {
        this.checkPeopleOrg = checkPeopleOrg;
    }

    /**
     * @return 审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public String getCheckTime() {
        return checkTime;
    }

    /**
     * @param checkTime 
	 *            审核时间（格式：yyyy-mm-dd hh24:mi:ss）
     */
    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * @return 生成交路时间
     */
    public String getCreatCrossTime() {
        return creatCrossTime;
    }

    /**
     * @param creatCrossTime 
	 *            生成交路时间
     */
    public void setCreatCrossTime(String creatCrossTime) {
        this.creatCrossTime = creatCrossTime;
    }

    /**
     * @return 列车等级（高速，普速，混合）
     */
    public String getCrossLevel() {
        return crossLevel;
    }

    /**
     * @param crossLevel 
	 *            列车等级（高速，普速，混合）
     */
    public void setCrossLevel(String crossLevel) {
        this.crossLevel = crossLevel;
    }

    /**
     * @return 列车运行公里数
     */
    public Long getRunRange() {
        return runRange;
    }

    /**
     * @param runRange 
	 *            列车运行公里数
     */
    public void setRunRange(Long runRange) {
        this.runRange = runRange;
    }

    /**
     * @return 列车编组辆数
     */
    public int getMarshallingNums() {
        return marshallingNums;
    }

    /**
     * @param marshallingNums 
	 *            列车编组辆数
     */
    public void setMarshallingNums(int marshallingNums) {
        this.marshallingNums = marshallingNums;
    }

    /**
     * @return 列车核定乘员
     */
    public Integer getPeopleNums() {
        return peopleNums;
    }

    /**
     * @param peopleNums 
	 *            列车核定乘员
     */
    public void setPeopleNums(Integer peopleNums) {
        this.peopleNums = peopleNums;
    }

    /**
     * @return 编组内容
     */
    public String getMarshallingContent() {
        return marshallingContent;
    }

    /**
     * @param marshallingContent 
	 *            编组内容
     */
    public void setMarshallingContent(String marshallingContent) {
        this.marshallingContent = marshallingContent;
    }

    /**
     * @return 列车运行相关局的局码
     */
    public String getRelevantBureau() {
        return relevantBureau;
    }

    /**
     * @param relevantBureau 
	 *            列车运行相关局的局码
     */
    public void setRelevantBureau(String relevantBureau) {
        this.relevantBureau = relevantBureau;
    }

    /**
     * @return 审核标志
     */
    public int getCheckFlag() {
        return checkFlag;
    }

    /**
     * @param checkFlag 
	 *            审核标志
     */
    public void setCheckFlag(int checkFlag) {
        this.checkFlag = checkFlag;
    }

    /**
     * @return 有效状态
     */
    public int getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            有效状态
     */
    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    /**
     * @return 创建局
     */
    public String getCreateBureau() {
        return createBureau;
    }

    /**
     * @param createBureau 
	 *            创建局
     */
    public void setCreateBureau(String createBureau) {
        this.createBureau = createBureau;
    }
}