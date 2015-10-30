package org.railway.com.trainplan.common.constants;

/**
 * 定义基本常用的参数
 * 
 * @author join
 *
 */
public class Constants {

	// rest请求超时设置30秒
	public static final int CONNECT_TIME_OUT = 30 * 1000;
	public static final String DATE_FORMAT_1 = "yyyyMMddHHmm";
	public static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";
	public static final String REMOTE_SERVICE_SUCCESS = "200";
	// TODO:后台服务地址，以后要改为配置文件
	// 后台接口地址
	// TODO 从配置文件中获取不到值

	// 获取方案列表
	public static final String GET_SCHEME_LIST = "/rail/template/TemplateSchemes";
	// 基本图方案包含的基本图运行线
	public static final String GET_TRAINLINE_TEMPLATES = "/rail/template/TrainlineTemplates";
	// 5.2.3 查询给定列车车次的基本图运行线
	public static final String GET_TRAINNBR_INFO = "/rail/template/TrainlineTemplates?name=";
	// 查询始发日期为给定日期范围的日计划运行线统计数
	public static final String GET_TRAINLINS = "/rail/plan";
	// 5.2.4 更新给定列车的基本图运行线车底交路id
	public static final String UPDATE_UNIT_CROSS_ID = "/rail/template/TrainlineTemplates/VehicleCycles";
	// 5.2.2 查询给定id的基本图运行线
	public static final String GET_TRAIN_LINES_INFO_WITH_ID = "/rail/template/TrainlineTemplates/";
	/**** 站点类型【1：始发 2：经由 3：终到】 ***/
	public static final String STATION_BEGIN = "1";
	public static final String STATION_ROUTE = "2";
	public static final String STATION_END = "3";

	// 列车类型：客运
	public static final String ZUOYE_DENGJI_KEYUN = "客运";
	// 查询全部的经由
	public static final String STATION_TYPE_ALL = "ALL";
	// 查询始发站和终到站
	public static final String STATION_TYPE_START_END = "START_END";

	public static final String TYPE_CROSS = "cross";
	public static final String TYPE_UNIT_CROSS = "unitcross";
	/****** mapper.xml文件中的id常量 *****/
	// 获取路局信息
	public static final String LJZDDAO_GET_LJ_INFO = "ljzdDao.getLjInfo";
	// 获取trainType 货运或客运getTrainType
	public static final String LJZDDAO_GET_TRAIN_TYPE = "ljzdDao.getTrainType";
	// 获取18个路局信息getFullStationInfo
	public static final String LJZDDAO_GET_FULL_STATION_INFO = "ljzdDao.getFullStationInfo";
	// 获取站名
	public static final String LJZDDAO_GET_FULL_NODE_INFO = "NodeDao.getFullNodeInfo";
	// 更新表train_plan字段check_state trainPlanDao updateCheckState
	public static final String TRAINPLANDAO_UPDATE_CHECKSTATE = "trainPlanDao.updateCheckState";
	// 更新表train_plan字段 plan_flag updatePlanTrainDaylyPlanFlag
	public static final String TRAINPLANDAO_UPDATE_PLANFLAG = "trainPlanDao.updatePlanTrainDaylyPlanFlag";
	// 根据路局名称和运行时间查询列车
	public static final String TRAINPLANDAO_GET_TOTALTRAINS = "trainPlanDao.getTotalTrains";
	// 根据planCrossId查询需要上图的列车信息
	public static final String TRAINPLANDAO_GET_TOTALTRAINS_FOR_PLAN_CROSS_ID = "trainPlanDao.getTotalTrainsForPlanCrossId";
	// getTrainShortInfo
	public static final String TRAINPLANDAO_GET_TRAIN_SHORTINFO = "trainPlanDao.getTrainShortInfo";
	// 对表train_plan插入计划数据 addTrainPlan
	public static final String TRAINPLANDAO_ADD_TRAIN_PLAN = "trainPlanDao.addTrainPlan";
	// 对表train_plan_stn插入经由数据addTrainPlanStn
	public static final String TRAINPLANDAO_ADD_TRAIN_PLAN_STN = "trainPlanDao.addTrainPlanStn";
	// 获取最大的train_plan_id getMaxPlanTrainId
	public static final String TRAINPLANDAO_GET_MAX_PLANTRAIN_ID = "trainPlanDao.getMaxPlanTrainId";
	public static final String RUNPLANLK_GETCMDTRAIN_COUNT = "runPlanLkDao.getCmdTrainTotalCount";
	// getTrainTimeDetail
	public static final String TRAINPLANDAO_GET_TRAIN_TIME_DETAIL = "trainPlanDao.getTrainTimeDetail";
	// 统计全路局列车信息 getTotalStationInfo
	public static final String TRAINPLANDAO_GET_TOTAL_STATION_INFO = "trainPlanDao.getTotalStationInfo";
	// 统计一个路局列车信息getOneStationInfo
	public static final String TRAINPLANDAO_GET_ONE_STATION_INFO = "trainPlanDao.getOneStationInfo";
	// getGatherTotalStationJieru
	public static final String TRAINPLANDAO_GET_TOTAL_STATION_JIERU = "trainPlanDao.getGatherTotalStationJieru";
	// 查询某天某局的车次 getGatherPeriodRundateTrains
	public static final String TRAINPLANDAO_GET_PERIOD_RUNDATE_TRAINS = "trainPlanDao.getGatherPeriodRundateTrains";
	// 查询数据总的条数 getPeriodRundateTrainsTotal
	public static final String TRAINPLANDAO_GET_RUNDATE_TRAINS_TOTAL = "trainPlanDao.getPeriodRundateTrainsTotal";
	// getRundateTrainCount
	public static final String TRAINPLANDAO_GET_RUNDATE_TRAIN_COUNT = "trainPlanDao.getRundateTrainCount";
	// 根据rundate和train_nbr删除表plan_train中的数据 deleteTrainRundateTrainNbr
	public static final String TRAINPLANDAO_DELETE_TRAIN_RUNDATE_NBR = "trainPlanDao.deleteTrainRundateTrainNbr";
	// deleteTrainRundateTrainStn
	public static final String TRAINPLANDAO_DELETE_TRAIN_RUNDATE_TRAIN_STN = "trainPlanDao.deleteTrainRundateTrainStn";
	// 导入数据前删除历史数据 deleteTrainRundateTrainStnInit
	public static final String TRAINPLANDAO_DELETE_TRAIN_RUNDATE_TRAIN_STN_INIT = "trainPlanDao.deleteTrainRundateTrainStnInit";
	public static final String LJZDDAO_GET_NODE_BYNAME = "NodeDao.getNodeByName";
	//
	public static final String TRAINPLANDAO_DELETE_TRAIN_RUNDATE_TRAIN_NBR_INIT = "trainPlanDao.deleteTrainRundateTrainNbrInit";
	// findPlanTrainByStartBureauAndRundate
	public static final String TRAINPLANDAO_FIND_PLANTRAIN_BY_START_BUREAU = "trainPlanDao.findPlanTrainByStartBureauAndRundate";
	// findPlanTrainByStartBureauCount
	public static final String TRAINPLANDAO_FIND_PLANTRAIN_BY_START_BUREAU_COUNT = "trainPlanDao.findPlanTrainByStartBureauCount";

	/** lilong 20140822 add, for loadHighlineCross **/
	// findPlanTrainListByPassBureauAndRunDate
	public static final String TRAINPLANDAO_FIND_PLANTRAINLIST_BY_PASS_BUREAU = "trainPlanDao.findPlanTrainListByPassBureauAndRunDate";
	public static final String TRAINPLANDAO_FIND_PLANTRAIN_BY_PLANTRAINID2 = "trainPlanDao.findPlanTrainByPlanTrainId2";
	// findPlanTrainByPlanTrainId
	public static final String TRAINPLANDAO_FIND_PLANTRAIN_BY_PLANTRAINID = "trainPlanDao.findPlanTrainByPlanTrainId";
	// findCompareModelByBureauAndRunDate
	public static final String TRAINPLANDAO_FIND_COMPAREMODEL_BY_BUREAU = "trainPlanDao.findCompareModelByBureauAndRunDate";
	// findPlanCrossByPlanCrossId
	public static final String TRAINPLANDAO_FIND_PLANCROSS_BY_PLANCROSSID = "trainPlanDao.findPlanCrossByPlanCrossId";
	// insertHighlineCross
	public static final String TRAINPLANDAO_INSERT_HIGHLINECROSS = "trainPlanDao.insertHighlineCross";
	// insertHighlineCrossTrain
	public static final String TRAINPLANDAO_INSERT_HIGHLINECROSSTRAIN = "trainPlanDao.insertHighlineCrossTrain";
	// deleteHighlineCrossByCrossDate
	public static final String CROSSDAO_DELETE_HIGHLINECROSS_BY_CROSSDATE = "crossDao.deleteHighlineCrossByCrossDate";
	// deleteHighlineCrossTrainByHighlineCrossDate
	public static final String CROSSDAO_DELETE_HIGHLINECROSSTRAIN_BY_CROSSDATE = "crossDao.deleteHighlineCrossTrainByCrossDate";

	/**** CrossMapper.xml ****/
	// 表plan_cross插入数据
	public static final String CROSSDAO_ADD_PLAN_CROSS_INFO = "crossDao.addPlanCrossInfo";
	// 表base_cross插入数据addCrossInfo
	public static final String CROSSDAO_ADD_CROSS_INFO = "crossDao.addCrossInfo";
	// 表base_cross_train插入数据addCrossTrainInfo
	public static final String CROSSDAO_ADD_CROSS_TRAIN_INFO = "crossDao.addCrossTrainInfo";
	// 根据基本图列车id查询列车有效期
	public static final String CROSSDAO_GET_PERIOD_FOR_CROSSTRAINID = "crossDao.getPeriodTimdByCrossTrainId";
	// 查询crossinfo信息
	public static final String CROSSDAO_GET_CROSS_INFO = "crossDao.getCrossInfo";
	// 查询crossinfo信息的条数
	public static final String CROSSDAO_GET_CROSS_INFO_COUNT = "crossDao.getCrossInfoTotalCount";
	// 通过crossid获取crossinfo信息
	public static final String CROSSDAO_GET_CROSS_INFO_FOR_PARAM = "crossDao.getCrossInfoForParam";
	// 通过planCrossId查询plancross信息
	public static final String CROSSDAO_GET_PLANCROSSINFO_FOR_PLANCROSSID = "crossDao.getPlanCrossInfoForPlanCrossId";
	public static final String CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID = "crossDao.getPlanCrossInfoByPlanCrossId";
	public static final String CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID_VEHICLE = "crossDao.getPlanCrossInfoByPlanCrossIdVehicle";
	public static final String CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID_VEHICLEBY2 = "crossDao.getPlanCrossInfoByPlanCrossIdVehicleBy2";
	public static final String CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID_VEHICLESEARCH = "crossDao.getPlanCrossInfoByPlanCrossIdVehicleSearch";
	// 通过crossid获取crossinfo信息
	public static final String CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM = "crossDao.getBaseTrainInfoByParams";
	public static final String CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM_BY_END_STN = "crossDao.getBaseTrainInfoByParamsAndEndStn";
	public static final String CROSSDAO_GET_BASETRAIN_INFO_ONE_FOR_PARAM = "crossDao.getBaseTrainInfoOneByParam";
	// 通过crossid查询crosstrainInfo信息
	public static final String CROSSDAO_GET_CROSS_TRAIN_INFO_FOR_CROSSID = "crossDao.getCrossTrainInfoForCrossid";
	// 插入表unit_cross addUnitCrossInfo
	public static final String CROSSDAO_ADD_UNIT_CROSS_INFO = "crossDao.addUnitCrossInfo";
	// 插入表unit_cross_train
	public static final String CROSSDAO_ADD_UNIT_CROSS_TRAIN_INFO = "crossDao.addUnitCrossTrainInfo";
	// 根据unit_cross_id查询unitcrossInfo信息 getUnitCrossInfoForUnitCrossid
	public static final String CROSSDAO_GET_UNIT_CROSS_INFO_FOR_UNIT_CROSSID = "crossDao.getUnitCrossInfoForUnitCrossid";
	public static final String CROSSDAO_GET_UNIT_CROSS_TRAIN_INFO_FOR_UNIT_CROSSID = "crossDao.getUnitCrossTrainInfoForUnitCrossid";
	// getUnitCrossTrainInfoForCrossid
	public static final String CROSSDAO_GET_UNIT_CROSSTRAIN_INFO_FOR_UNIT_CROSSID = "crossDao.getUnitCrossTrainInfoForUnitCrossId";
	// 分页查询表unit_cross getUnitCrossInfo
	public static final String CROSSDAO_GET_UNIT_CROSS_INFO = "crossDao.getUnitCrossInfo";
	// 分页查询的总条数
	public static final String CROSSDAO_GET_UNIT_CROSS_INFO_COUNT = "crossDao.getUnitCrossInfoCount";
	// 通过unit_cross_id查询train_nbr getTrainNbrFromUnitCross
	public static final String CROSSDAO_GET_TRAINNBR_FROM_UNIT_CROSS = "crossDao.getTrainNbrFromUnitCross";

	// 通过corssid在表unit_cross中查询unitcrossInfo信息
	public static final String CROSSDAO_GET_UNIT_CROSS_INFO_FOR_CROSSID = "crossDao.getUnitCrossInfoForCrossId";
	// 通过corssid在表unit_cross中查询unitcrossInfo信息
	public static final String CROSSDAO_GET_UNIT_CROSS_INFO_BY_CROSSNAME_CHARTID = "crossDao.getUnitCrossInfoByCrossNameAndChartId";
	// 通过cross查询交路基本信息和经由始发和终到信息
	public static final String CROSSDAO_GET_CROSS_TRAININFO_FOR_CROSSID = "crossDao.getCrossTrainInfoForCrossId";
	// 更新base_cross的check_time
	public static final String CROSSDAO_UPDATE_CROSS_CHECKTIME = "crossDao.updateCrossCheckTime";
	// 更新base_cross的creat_unit_time
	public static final String CROSSDAO_UPDATE_CROSS_CREATETIME = "crossDao.updateCrossUnitCreateTime";
	public static final String CROSSDAO_UPDATE_CROSS_CREATETIME_TO_NULL = "crossDao.updateCrossUnitCreateTimeToNull";
	public static final String CROSSDAO_UPDATE_CROSS_CREATETIME_TO_NULL_BYUNITID = "crossDao.updateCrossUnitCreateTimeToNullByUnitCrossId";
	public static final String CROSSDAO_UPDATE_CROSS_CHECKTIME_TO_NULL_BYUNITID = "crossDao.updateCrossCheckTimeToNullByUnitCrossId";
	// 对表plan_check添加一条数据
	public static final String CROSSDAO_INSERT_PLAN_CHECK_INFO = "crossDao.insertPlanCheckInfo";
	// insertPlanCheckInfocmd
	public static final String CROSSDAO_INSERT_CMD_CHECK_INFO = "crossDao.insertPlanCheckInfocmd";
	public static final String CROSSDAO_UPDATE_Unit_CROSS_CREATETIME = "crossDao.updateUnitCrossUnitCreateTime";
	// 根据plan_cross_id查询运行线
	public static final String CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID = "crossDao.getTrainPlanLineInfoForPlanCrossId";
	public static final String CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_ANDBUREAUSHORTNAME = "crossDao.getTrainPlanLineInfoForPlanCrossIdAndBureauShortName";
	public static final String CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_AND_VEHICLE = "crossDao.getTrainPlanLineInfoForPlanCrossIdAndVehicle";
	public static final String CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_AND_VEHICLEBY2 = "crossDao.getTrainPlanLineInfoForPlanCrossIdAndVehicleBy2";
	public static final String CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_AND_VEHICLE_VEHICLESEARCH = "crossDao.getTrainPlanLineInfoForPlanCrossIdAndVehicleSearch";
	// 通过plan_cross_id查询经由的始发站和终到站 getStationListForPlanCrossId
	public static final String CROSSDAO_GET_STATIONLIST_FOR_PLANCROSSID = "crossDao.getStationListForPlanCrossId";
	// 更新base_cross的check_time
	public static final String CROSSDAO_UPDATE_UNIT_CROSS_CHECKTIME = "crossDao.updateUnitCrossCheckTime";

	// 更新base_cross的check_time为null
	public static final String CROSSDAO_UPDATE_UNIT_CROSS_CHECKTIMETONULL = "crossDao.updateUnitCrossCheckTimeToNull";
	// 根据crossids批量删除base_cross表中数据
	public static final String CROSSDAO_DELETE_CROSS_INFO_FOR_CROSSIDS = "crossDao.deleteCrossInfoForCrossIds";
	// 根据交路名和基本图ID，删除已经存在的UNITCROSS和UNITCROSSTRAIN
	public static final String CROSSDAO_DELETE_UNITCROSS_UNITCROSSTRAIN = "crossDao.deleteUnitCrossAndCrossTrain";
	// 根据交路名和基本图ID，删除已经存在的UNITCROSS和UNITCROSSTRAIN
	public static final String CROSSDAO_DELETE_UNIT_CROSS_BY_CROSSNAMEANDCHARTID = "crossDao.deleteUnitCrossInfoByCrossNameAndChartId";
	// 根据crossIds批量删除base_cross_train表中数据
	public static final String CROSSDAO_DELETE_CROSS_INFO_TRAIN_FOR_CROSSIDS = "crossDao.deleteCrossInfoTrainForCrossIds";
	// 更新表base_cross
	public static final String CROSSDAO_UPDATE_BASE_CROSS_INFO = "crossDao.updateBaseCross";
	// 更新表base_cross
	public static final String CROSSDAO_UPDATE_PLAN_CROSS_CHECKTYPE = "crossDao.updatePlanCrossCheckType";
	// 更新表base_cross_train中的有效期
	public static final String CROSSDAO_UPDATE_BASE_CROSS_TRAIN_PERIOD = "crossDao.updateBaseCrossTrainPeriod";
	public static final String CROSSDAO_UPDATE_BASE_CROSS_BASECROSSID = "crossDao.updateBaseCrossBaseCrossId";
	// 根据baseCrossId查询unitCross对象列表
	public static final String CROSSDAO_GET_UNITCROSS_INFO_FOR_BASECROSSID = "crossDao.getUnitCrossInfoForBaseCrossId";
	// 根据crossids批量删除unit_cross表中数据
	public static final String CROSSDAO_DELETE_UNIT_CROSS_INFO_FOR_CROSSIDS = "crossDao.deleteUnitCrossInfoForCrossIds";
	// 根据crossIds批量删除unit_cross_train表中数据
	public static final String CROSSDAO_DELETE_UNIT_CROSS_INFO_TRAIN_FOR_CROSSIDS = "crossDao.deleteUnitCrossInfoTrainForCrossIds";

	public static final String CROSSDAO_UPDATE_UNIT_CROSS_INFO_TRAIN_FOR_CROSSIDS = "crossDao.updateUnitCrossInfoTrainForCrossIds";
	
	public static final String UPDATE_UNIT_CROSS_TRAIN_BY_TRAINID = "crossDao.updateUnitCrossTrainByTrainId";

	public static final String CROSSDAO_GET_COUNT_BASECROSS_NONCHECK = "crossDao.getCountfromBaseCrossNonCheck";
	public static final String CROSSDAO_GET_COUNT_UNITCROSS_NONCHECK = "crossDao.getCountfromUnitCrossNonCheck";

	public static final String CROSSDAO_DELETE_UNITCROSSTRAIN_BY_CHARID = "crossDao.deleteUnitCrossTrainByChartId";
	public static final String CROSSDAO_DELETE_UNITCROSS_INFO_BY_CHARID = "crossDao.deleteUnitCrossInfoByChartId";
	public static final String CROSSDAO_DELETE_CROSSTRAIN_BY_CHARID = "crossDao.deleteCrossTrainByChartId";
	public static final String CROSSDAO_DELETE_CROSSINFO_BY_CHARID = "crossDao.deleteCrossInfoByChartId";
	// 根据chartId查询unit_cross信息
	public static final String CROSSDAO_GET_UNIT_CROSSINFO_FOR_CHARTID = "crossDao.getUnitCrossInfoForChartId";
	// 获取全部方案列表
	public static final String SCHEME_GETSCHEMEINFO = "schemeDao.getSchemeInfo";
	// 获取全部列车类型
	public static final String BUSINESS_GETBUSINESSINFO = "org.railway.com.trainplan.repository.mybatis.BaseTrainDao.getBusinessInfo";
	// 根据方案ID和始发终到局获取列车列表
	public static final String TRAININFO_GETTRAININFO = "trainInfoDao.getTrainInfo";
	public static final String TRAININFO_GETTRAININFO_PAGE = "trainInfoDao.getTrainInfoForPage";
	public static final String TRAININFO_GETTRAININFO_FROM_JBT_BY_CHARTID_TRAINNBR = "trainInfoDao.getTrainInfoFromJbtByChartidTrainnbr";
	public static final String TRAININFO_GETTRAININFO_FROM_JBT_BY_CHARTID_TRAINNBR_BEFORE_TRAIN = "trainInfoDao.getTrainInfoFromJbtByChartidTrainnbrBeforeTrain";

	public static final String TRAININFO_GETTRAININFO_PAGE_EDIT = "trainInfoDao.getTrainInfoForPageEdit";

	public static final String TRAININFO_GETTRAININFO_COUNT = "trainInfoDao.getTrainInfoTotalCount";
	// 根据baseTrainId查询列车基本信息
	public static final String TRAININFO_GETTRAININFO_FOR_TRAINID = "trainInfoDao.getTrainInfoForTrainId";

	// 根据方案ID和始发终到局获取列车列表
	public static final String TRAININFO_GETTRAINTIMEINFO_BY_TRAINID = "trainTimeDao.getTrainTimeInfoByTrainId";
	public static final String TRAININFO_GETTRAINTIMEINFO_BY_TRAINIDFROMTRAINLINE_JBT = "trainTimeDao.getTrainTimeInfoByTrainIdFromTrainlineJbt";
	// 根据plan_train_id获取运行线列车时刻表
	public static final String TRAININFO_GETPLANLINE_TRAINTIMEINFO_BY_TRAINID = "trainTimeDao.getPlanLineTrainTimeInfoByTrainId";
	// 根据plan_train_id从基本图的库中获取列车时刻表getTrainTimeInfoByPlanTrainId
	public static final String TRAININFO_GET_TRAINTIMEINFO_BY_PLAN_TRAIN_ID = "trainTimeDao.getTrainTimeInfoByPlanTrainId";
	public static final String TRAININFO_GET_TRAINTIMEINFO_BY_PLAN_TRAIN_IDJY = "trainTimeDao.getTrainTimeInfoByPlanTrainIdjy";
	public static final String TRAININFO_GET_TRAINTIMEINFO_BY_PLAN_TRAIN_IDGT = "trainTimeDao.getTrainTimeInfoByPlanTrainIdgt";

	//
	public static final String TRAININFO_GET_PLAN_TRAIN_STN_BY_TRAINID = "trainTimeDao.getPlanTrainStnInfoForPlanTrainId";
	// 更改spareFlag字段
	public static final String TRAININFO_UPDATE_SPARE_FLAG_BY_PLANTRAINID = "trainTimeDao.updateSpareFlagByPlanTrainId";
	public static final String TRAININFO_UPDATE_SPARE_FLAG_BY_PLANTRAINNBRANDRUNDAY = "trainTimeDao.updateSpareFlagByPlanTrainNbrAndRunDay";
	public static final String BATCH_UPDATE_PLANTRAIN_BY_IDLIST = "trainTimeDao.batchUpdatePlanTrainByIdlist";
	// getPlanTrainStnInfoForPlanTrainId
	public static final String GET_TRAIN_RUN_PLAN = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getTrainRunPlans";
	public static final String GET_PLAN_CROSS = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanCross";

	/**
	 * cross_name中没有重复车次的.
	 */
	public static final String GET_TRAIN_RUN_PLAN1 = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getTrainRunPlans1";
	/**
	 * cross_name中有相同车次.
	 */
	public static final String GET_TRAIN_RUN_PLAN2 = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getTrainRunPlans2";
	/**
	 * 总有有几组车底.
	 */
	public static final String get_GroupSerialNbr_By_PlanCrossId = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getGroupSerialNbrByPlanCrossId";
	public static final String TRAININFO_GETTRAININFO_PAGE_JBT_ID = "trainInfoDao.getTrainInfoForPageJbtId";

	// GET_PLAN_CHECK
	public static final String GET_PLAN_CHECK = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getCheckPlan";

	public static final String CROSSDAO_DELETE_PLANCROSS_INFO_TRAIN_FOR_CROSSIDS = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.deletePlanCrossByPlanCrossIds";
	public static final String CROSSDAO_DELETE_PLANTRAIN_INFO_TRAIN_FOR_CROSSIDS = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.deletePlanTrainsByPlanCrossIds";
	public static final String CROSSDAO_DELETE_PLANTRAINSTN_INFO_TRAIN_FOR_CROSSIDS = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.deletePlanTrainStnsByPlanCrossIds";

	/**
	 * 根据plan_cross_id删除m_trainline_item.
	 */
	public static final String DELETE_M_TRAINLINE_BY_PLANCROSSID = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.deleteMTrainLineByPlanCrossId";
	public static final String DELETE_M_TRAINLINE_ITEM_BY_PLANCROSSID = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.deleteMTrainLineItemByPlanCrossId";
	public static final String RUNPLANLK_GETCMDTRAIN_PAGE = "runPlanLkDao.getCmdTrainForPage";
	public static final String CROSSDAO_GET_getPlanCheckCountByIDHis = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanCheckCountByIDHis";
	public static final String CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSID = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanCheckInfoForPlanCrossId";
	public static final String CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSID1 = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanCheckInfoForPlanCrossId1";

	public static final String CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSIDCMDTEL = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanCheckInfoForPlanCrossIdcmdtel";

	public static final String CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSIDCMDTEL1 = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanCheckInfoForPlanCrossIdcmdtel1";

	public static final String CROSSDAO_UPDATE_CHECKTYPE_FOR_PLANCROSSID = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.updateCheckTypeForPlanCrossId";
	// updateCheckTypeForcmdId

	public static final String CROSSDAO_UPDATE_CHECKTYPE_FOR_CMDID = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.updateCheckTypeForcmdId";
	public static final String GETTRAINLINES_JHPT_COUNT = "trainInfoDao.getTrainlineTotalCount";
	public static final String GETTRAINLINES_JHPT = "trainInfoDao.getTrainlineForPage";
	public static final String GET_TRAINLINES_BY_TRAINLINEID = "trainTimeDao.getTrainLineTimes";
	// 更新运行线时刻表
	public static final String EDIT_PLAN_LINE_TRAIN_TIMES = "trainTimeDao.editPlanLineTrainTimes";

	public static final String HIGHLINECREWDAO_FIND_RUNPLAN_LIST = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.findRunPlanList";
	public static final String HIGHLINECREWDAO_FIND_RUNPLAN_LIST_BUREAU = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.findRunPlanListBureau";
	public static final String HIGHLINECREWDAO_FIND_HIGHLINE_CREW_LIST = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.findHighlineCrewList";
	public static final String HIGHLINECREWDAO_FIND_HIGHLINE_CREW_LIST_FOR_ALL = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.findHighlineCrewListForAll";
	public static final String HIGHLINECREWDAO_FIND_HIGHLINE_CREW_LIST_FOR_WEEK = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.findHighlineCrewListForWeek";
	public static final String HIGHLINECREWDAO_UPDATE_SUBMIT_TYPE = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.updateSubmitType";
	//
	public static final String HIGHLINECREWDAO_DELETE_HIGHLINE_CREW_FOR_CREWDATE = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.deleteHighlineCrewForCrewDate";
	//
	public static final String HIGHLINECREWDAO_GET_RECORD_PEOPLE_ORG = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.getRecordPeopleOrgList";
	//
	public static final String HIGHLINECREWDAO_GET_HIGHLINE_CREW_BASE_INFO_FOR_PAGE = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.getHighlineCrewBaseInfoForPage";
	//
	public static final String HIGHLINECREWDAO_GET_HIGHLINE_CREW_BASE_INFO = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.getHighlineCrewBaseInfo";
	//
	public static final String GET_HIGHLINE_CREW_FOR_CREWDATE_AND_TRAINNBR = "org.railway.com.trainplan.repository.mybatis.HighLineCrewDao.getHighlineCrewForCrewDateAndTrainNbr";
	// 获取交路字典信息
	public static final String SQL_ID_CROSSDICT_INFO = "crossDictDao.findCrossDictList";
	// 增加交路字典信息
	public static final String SQL_ADD_CROSS_DIC_INFO = "crossDictDao.addCrossDictInfo";
	// 通过unitCrossId查询drawGraphStn信息
	public static final String GET_CROSS_DICT_STN_FOR_UNITCROSSID = "crossDictDao.getCrossDictStnForUnitCorssId";
	// 通过drawGraphId 查找drawGraphStn信息
	public static final String GET_CROSS_DICT_STN_BY_DRAWGRAPHID = "crossDictDao.getCrossDictStnByDrawGraphId";
	// 修改drawgraphstn
	public static final String UPDATE_DRAWGRAPHSTN_SORT_DETAIL = "crossDictDao.updateDrawGraphStnByDrawGraphStnId";
	// 删除drawgraphstn
	public static final String DELETED_RAWGRAPHSTN_BY_DRAWGRAPHID = "crossDictDao.deleteCrossDictStnByDrawGraphId";
	// 删除drawgraph
	public static final String DELETED_RAWGRAPH_BY_DRAWGRAPHID = "crossDictDao.deleteCrossDictByDrawGraphId";

	// 批量增加交路字典经由站信息
	public static final String SQL_BATCH_ADD_CROSS_DIC_STN_INFO = "crossDictDao.batchAddCrossDictStnInfo";
	// 通过baseCrossId查询draw_graph对象
	public static final String SQL_GET_DRAW_GRAPH_FOR_BASECROSSID = "crossDictDao.getDrawGraphForBaseCrossId";
	// 通过baseCrossId查询draw_graph_stn对象
	public static final String SQL_GET_CROSS_DIC_STN_FOR_BASECROSSID = "crossDictDao.getCrossDictStnForChartId";
	public static final String SQL_GET_CROSS_DIC_STN_BY_BASECROSSID = "crossDictDao.getCrossDictStnByChartId";
	public static final String SQL_GET_CROSS_DIC_STN_BY_BASECROSSID_VEHICLE = "crossDictDao.getCrossDictStnByChartIdVehicle";
	public static final String SQL_GET_CROSS_DIC_STN_BY_BASECROSSID_VEHICLEBY2 = "crossDictDao.getCrossDictStnByChartIdVehicleBy2";
	public static final String SQL_GET_CROSS_DIC_STN_BY_BASECROSSID_VEHICLESEARCH = "crossDictDao.getCrossDictStnByChartIdVehicleSearch";
	public static final String GET_PLANCROSSINFO_BY_STARTDATE = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getRunPlanByStartDate";

	public static final String TRAINPLANDAO_FIND_BY_GROUPSERIALBRR = "trainPlanDao.findPlanTrainByGroupSerialNbr";

	// originalCrossDao 
	public static final String ORIGINALCROSSDAO_ADD_ORIGINALCROSS_INFO = "originalCrossDao.addOriginalCrossInfo";
	public static final String ORIGINALCROSSDAO_INSERT_ORIGINALCROSS_INFO = "originalCrossDao.insertOriginalCrossInfo";
	public static final String ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO = "originalCrossDao.getOriginalCrossInfo";
	public static final String ORIGINALCROSSDAO_UPDATE_ORIGINALCROSS_INFO = "originalCrossDao.updateOriginalCrossInfo";
	public static final String ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_COUNT = "originalCrossDao.getOriginalCrossInfoTotalCount";
	public static final String ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_FOR_PARAM = "originalCrossDao.getOriginalCrossInfoForParam";
	public static final String ORINIGALCROSSDAO_UPDATE_ORIGINAL_CROSS_INFO = "originalCrossDao.updateOriginalCrossInfo";
	public static final String ORINIGALCROSSDAO_GET_ORIGINAL_CROSS_INFO_BY_IDS = "originalCrossDao.getOriginalCrossByIds";
	public static final String ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_PARAM = "originalCrossDao.getOriginalCrossInfoForParam";
	public static final String ORIGINALCROSSDAO_DELETE_ORIGINALCROSS_INFO_FOR_CROSSIDS = "originalCrossDao.deleteOriginalCrossInfoForCrossIds";
	public static final String ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_BY_ID = "originalCrossDao.getOriginalCrossById";
	public static final String ORIGINALCROSSDAO_UPDATE_ORIGINALCROSS_CHECKINFO_BY_ID = "originalCrossDao.updateOriginalCrossCheckInfo";
	
	
	
	
	public static final String CROSSDAO_ADD_HIGHLINE_CROSS = "crossDao.addHighLineCrossInfo";
	public static final String CROSSDAO_ADD_HIGHLINE_CROSSSINGLE = "crossDao.addHighLineCrossInfoSingle";
	public static final String CROSSDAO_ADD_HIGHLINE_CROSS_TRAIN = "crossDao.addHighLineCrossTrainInfo";

	// highlineCross
	public static final String HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_LIST = "highlineCrossDao.getHighlineCrossList";

	public static final String HIGHLINECROSSDAO_GETDISRELACROSSPOSTLIST = "dicRelaCrossPostDao.getDicRelaCrossPostList";

	public static final String HIGHLINECROSSDAO_GETDICTHROUGHLINE = "dicThroughLineDao.getDicThroughLine";
	//
	public static final String HIGHLINECROSSDAO_UPDATE_HIGHLINE_CROSSINFO = "highlineCrossDao.updateHighlineCrossInfo";

	public static final String HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_TRAIN_LIST = "highlineCrossDao.getHighlineCrossTrainList";
	public static final String HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_TRAIN_BASEINFO = "highlineCrossDao.getHighlineCrossTrainBaseInfoList";
	// getHighlineTrainTimeForHighlineCrossId
	public static final String HIGHLINECROSSDAO_GET_HIGHLINE_TRAINTIME_FOR_HIGHLINE_CROSSID = "highlineCrossDao.getHighlineTrainTimeForHighlineCrossId";
	// deleteHighlienCrossTrainForHighlineCrossId
	public static final String HIGHLINECROSSDAO_DELETE_HIGHLINECROSSTRAIN_FOR_ID = "highlineCrossDao.deleteHighlienCrossTrainForHighlineCrossId";
	// 根据cross_start_date和cross_bureau删除数据
	public static final String HIGHLINECROSSDAO_DELETE_HIGHLINECROSS_FOR_DATE = "highlineCrossDao.deleteHighLineForDate";
	// 根据条件获取highline_cross数据
	public static final String HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_INFO = "highlineCrossDao.getHighlineCrossInfo";

	public static final String HIGHLINECROSSDAO_DELETE_HIGHLINECROSS_FOR_ID = "highlineCrossDao.deleteHighlienCrossForHighlineCrossId";
	public static final String HIGHLINECROSSDAO_UPDATE_HIGHLINE_VEHICLE = "highlineCrossDao.updateHighLineVehicle";
	public static final String HIGHLINECROSSDAO_GET_HIGHLINE_THROUGH_CROSSINFO = "highlineCrossDao.getHighlineThroughCrossInfo";
	public static final String HIGHLINECROSSDAO_UPDATE_HIGHLINE_CHECKINFO = "highlineCrossDao.updateHiglineCheckInfo";
	public static final String GET_TRAIN_RUN_PLANS_FOR_CREATElINE = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getTrainRunPlansForCreateLine";
	public static final String GET_TRAIN_RUN_PLANS_FOR_CREATE = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getTrainRunPlansForCreate";
	public static final String GET_TRAIN_RUN_PLANS_FOR_CREATEGT = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getTrainRunPlansForCreateGT";
	public static final String HIGHLINECROSSDAO_GET_VEHICLES = "highlineCrossDao.getVehicles";

	/*** 与临客相关的mapper配置 ******/
	public static final String RUNPLANLKDAO_GET_PLANTRAIN_LK_INFO = "runPlanLkDao.getPlanTrainLkInfo";
	//
	public static final String RUNPLANLKDAO_GET_TRAINLK_RUNPLAN = "runPlanLkDao.getTrainLkRunPlans";
	public static final String RUNPLANLKDAO_GET_TRAINLK_RUNPLAN1 = "runPlanLkDao.getTrainLkRunPlans1";
	// 查询临客的图形数据
	public static final String RUNPLANLKDAO_GET_TRAINLK_FOR_PLAN_TRAIN_ID = "runPlanLkDao.getTrainLkInfoForPlanTrainId";
	public static final String BASEDAO_GET_LJPYMbyLJJC = "highlineCrossDao.getLJPYMbyLJJC";
	public static final String RUNPLANLKDAO_GET_TRAINLINE_SUBINFO_TIME = "runPlanLkDao.getTrainLineSubinfoTime";
	public static final String BASEDAO_GET_THROUGHLINE = "highlineCrossDao.getThroughLine";
	public static final String BASEDAO_GETTHROUGHLINENAME = "dicThroughLineDao.getThroughLineName";
	public static final String BASEDAO_INITDICRELACROSSPOST = "dicRelaCrossPostDao.initDicRelaCrossPost";
	public static final String BASEDAO_GET_CRHTYPE = "highlineCrossDao.getCrhType";
	public static final String BASEDAO_GET_DEPOT = "highlineCrossDao.getDepot";
	public static final String BASEDAO_GET_ACC = "highlineCrossDao.getAcc";
	public static final String BASEDAO_GETDICRELACROSSPOSTLIST = "dicRelaCrossPostDao.getDicList";

	// 查询cmdTrainStn信息
	public static final String RUNPLANLKDAO_GET_CMD_TRAINSTN_INFO = "runPlanLkDao.getCmdTrainStnInfo";
	// 查询表cmdTrain
	public static final String RUNPLANLKDAO_GET_CMD_TRAININFO_FOR_CMDMLID = "runPlanLkDao.getCmdTrainInfoForCmdTxtmlItemId";
	// 查询表cmdTrain
	public static final String RUNPLANLKDAO_GET_CMD_TRAININFO_FOR_CMDTRAIN = "runPlanLkDao.getCmdTrainInfoForCmdTrainId";
	// 保存表cmd_train_stn的数据
	public static final String RUNPLANLKDAO_INSERT_CMD_TRAIN_STN = "runPlanLkDao.insertCmdTrainStn";
	public static final String PLANTRAININFOTEMPDAO_INSERT_PLANTRAININFOTEMP = "planTrainInfoTempDao.insertPlanTrainInfoTemp";
	public static final String PLANTRAININFOTEMPDAO_DELETE_PLANTRAININFOTEMP = "planTrainInfoTempDao.deletePlanTrainInfoTemp";
	public static final String PLANTRAININFOTEMPDAO_UPDATE_PLANTRAININFOTEMP = "planTrainInfoTempDao.updatePlanTrainInfoTemp";
	public static final String PLANTRAININFOTEMPDAO_LIST_PLANTRAININFOTEMP = "planTrainInfoTempDao.listPlanTrainInfoTemp";
	public static final String PLANTRAININFOTEMPDAO_DELETE_PLANTRAININFOTEMP_BYDATE = "planTrainInfoTempDao.deletePlanTrainInfoTempByDate";
	public static final String PLANTRAININFOTEMPDAO_DELETE_PLANTRAININFOTEMP_BYREQUESTID = "planTrainInfoTempDao.deletePlanTrainInfoTempByRequestid";

	// 保存表cmd_train的数据
	public static final String RUNPLANLKDAO_INSERT_CMD_TRAIN = "runPlanLkDao.insertCmdTrain";
	public static final String RUNPLANLKDAO_INSERTDISRELACROSSPOST = "dicRelaCrossPostDao.insertDicRelaCrossPost";
	public static final String RUNPLANLKDAO_INSERTDICTHROUGHLINE = "dicThroughLineDao.insertDicThroughLine";
	public static final String RUNPLANLKDAO_ISEXISTDICTHROUGHLINE = "dicThroughLineDao.isExistDicThroughLine";
	// 更新cmd_train的数据
	public static final String RUNPLANLKDAO_UPDATE_CMD_TRAIN_FOR_CMDTRAIN = "runPlanLkDao.updateCmdTrainCmdTrainId";
	public static final String RUNPLANLKDAO_UPDATE_CMDTRAINSTN_FOR_CMDTRAIN = "runPlanLkDao.updateCmdTrainStnCmdTrainId";
	// 通过cmd_train_id删除表cmd_train_stn中的数据
	public static final String RUNPLANLKDAO_DELETE_CMD_TRAINSTN_FOR_CMDTRAINID = "runPlanLkDao.deleteCmdTrainStnForCmdTrainId";
	// 根据cmdPlanId更新途径局passBureau
	public static final String RUNPLANLKDAO_UPDATE_PASS_BUREAU_FOR_CMD_TRAINID = "runPlanLkDao.updatePassBureauForCmdTraindId";
	public static final String UPDATECMDTRAINFORCMDTRAINDID = "runPlanLkDao.updateCmdTrainForCmdTraindId";
	// 根据cmd_train_id获取cmdtrain和cmdtrainStn信息
	public static final String RUNPLANLKDAO_GET_CMDTRAIN_AND_STNINFO = "runPlanLkDao.getCmdTrandAndStnInfo";
	// 生成临客客运计划，对表plan_train插入数据
	public static final String RUNPLANLKDAO_ADD_RUN_PLAN_LK = "runPlanLkDao.addRunPlanLk";
	// 生成临客客运计划，对表plan_train_stn插入数据
	public static final String RUNPLANLKDAO_ADD_RUN_PLAN_STN_LK = "runPlanLkDao.addRunPlanLkTrainStn";
	// 获取站的信息
	public static final String RUNPLANLKDAO_GET_BASE_STATION_INFO = "runPlanLkDao.getBaseStationInfo";

	// 根据多条件查询表cmd_train
	public static final String RUNPLANLKDAO_GET_CMDTRAIN_FOR_MULTIPLE_PARAME = "runPlanLkDao.getCmdTraindForMultipleParame";
	//
	public static final String RUNPLANLKDAO_GET_PLANTRAINID_FOR_CMDTRAINID = "runPlanLkDao.getPlanTrainIdForCmdTrainId";
	// deleteTrainForCmdTrainId
	public static final String RUNPLANLKDAO_DELETE_TRAIN_FOR_CMDTRAINID = "runPlanLkDao.deleteTrainForCmdTrainId";

	public static final String RUNPLANLKDAO_DELETE_CMDTRAIN_FOR_CMDTRAINID = "runPlanLkDao.deleteCmdTrainForCmdTrainId";

	public static final String RUNPLANLKDAO_DELETEDICRELACROSSPOST = "dicRelaCrossPostDao.deleteDicRelaCrossPost";

	public static final String RUNPLANLKDAO_DELETEDICTHROUGHLINE = "dicThroughLineDao.deleteDicThroughLine";

	public static final String RUNPLANLKDAO_UPDATERELACROSSPOST = "dicRelaCrossPostDao.updateRelaCrossPost";

	public static final String RUNPLANLKDAO_DELETE_TRAINSTN_FOR_PLANTRAINID = "runPlanLkDao.deleteTrainStnForPlanTrainId";
	public static final String RUNPLANLKDAO_DELETE_TRAINSTN_FOR_PLANTRAINIDFA = "runPlanLkDao.deleteTrainStnForPlanTrainIdFa";

	public static final String RUNPLANLKDAO_UPDATE_CREATESTATE = "runPlanLkDao.updateCreateStateForCmdTrainId";

	public static final String RUNPLANLKDAO_INSERT_MTRAINLINE = "runPlanLkDao.insertMTrainLine";

	public static final String RUNPLANLKDAO_INSERT_MTRAINLINE_ROUTE = "runPlanLkDao.insertMTrainLineStnRoute";

	// 查询JHPT_JBT.M_TRAINLINE_TYPE表
	public static final String RUNPLANLKDAO_GET_TRAINTYPE_FOR_ID = "runPlanLkDao.getTrainlineTypeForTypeId";

	public static final String RUNPLANLKDAO_GET_TRAINTYPE_FOR_ID2 = "runPlanLkDao.getTrainlineTypeForTypeId2";

	public static final String HIGHLINECROSSDAO_UPDATE_HIGHLINECROSSID = "highlineCrossDao.updateHighLineCrossId";

	public static final String RUN_PLAN_DAO_GET_TRAINRUNPLAN_FOR_LK = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getTrainRunPlanForLk";

	public static final String RUN_PLAN_DAO_GET_TRAINRUNPLAN_FOR_CHARTID = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanTrainInfoForBaseChartId";

	/**
	 * 根据路局、开行日期yyyy-MM-dd查询高铁开行计划
	 */
	public static final String HIGHLINECROSSDAO_GET_HIGHLINEPLAN_BY_BUREAU = "highlineCrossDao.getHighLinePlanByBureau";

	/**
	 * 根据 PLAN_TRAIN_ID查询基本图高铁车底信息
	 */
	public static final String HIGHLINECROSSDAO_GET_HIGHLINEVEH_JBT = "highlineCrossDao.getHighLineVehInfoForJbt";

	/**
	 * 根据 PLAN_TRAIN_ID查询临客高铁车底信息
	 */
	public static final String HIGHLINECROSSDAO_GET_HIGHLINEVEH_LK = "highlineCrossDao.getHighLineVehInfoForLk";

	/**
	 * 根据crhType车型 查询车型基础信息
	 */
	public static final String HIGHLINECROSSDAO_GET_VEH_BY_CRHTYPE = "highlineCrossDao.getCrhVehInfoByGroupNbr";

	/**
	 * 根据 PLAN_CROSS_ID、trainNbr、CREW_DATE、CREW_BUREAU查询基本图高铁乘务信息
	 */
	public static final String HIGHLINECROSSDAO_GET_HIGHLINECREW_JBT = "highlineCrossDao.getHighLineCrewInfoForJbt";

	/**
	 * 根据 PLAN_TRAIN_ID、trainNbr、CREW_DATE、CREW_BUREAU查询临客高铁乘务信息
	 */
	public static final String HIGHLINECROSSDAO_GET_HIGHLINECREW_LK = "highlineCrossDao.getHighLineCrewInfoForLk";

	// 根据CMD_TRAIN_ID和rundate 更新开行计划状态 spareFlag字段
	public static final String TRAININFO_UPDATE_SPARE_FLAG_BY_CMD_TRAIN_ID = "runPlanLkDao.updateSpareFlag";

	/**
	 * 查询m_trainline_type数据.
	 * 
	 * @return
	 */
	public static final String Get_M_Trainline_Type_Map = "runPlanLkDao.getMTrainlineTypeByMap";

	/**
	 * 根据runDates和trainNbr 重置PLAN_TRAIN表“生成运行线、一级审核、二级审核”标记及运行线ID
	 * 
	 * @param runDate
	 *            (检索条件 必填)运行日期yyyyMMdd
	 * @param trainNbr
	 *            (检索条件 必填)车次号
	 * @param spareFlag
	 *            (update字段 可选) 备用及停运标记（1:开行；2:备用；9:停运）
	 * @author denglj
	 * @date 2014-09-23
	 */
	public static final String PLAN_TRAIN_UPDATE_SPARE_FLAG_BY_RUNDATE_TRAINNBR = "trainTimeDao.updateSpareFlagByRunDateAndTrainNbr";

	/**
	 * 根据PLAN_TRAIN中PLAN_TRAIN_ID查询基本图列车时刻信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @author denglj
	 * @date 2014-10-27
	 */
	public static final String JBT_TRAINSTN_BY_PLANTRAINID = "trainTimeDao.jbtTrainStnByPlanTrainId";

	/**
	 * 根据PLAN_TRAIN中PLAN_TRAIN_ID查询客运列车时刻信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @author denglj
	 * @date 2014-10-27
	 */
	public static final String KY_TRAINSTN_BY_PLANTRAINID = "trainTimeDao.kyTrainStnByPlanTrainId";

	/**
	 * 根据planTrainId删除PLAN_TRAIN_STN表中列车时刻信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @author denglj
	 * @date 2014-10-28
	 */
	public static final String DELETETRAINLINES_JBT_BY_PARENTID = "trainTimeDao.deleteTrainlineItemTempByParentId";
	/**
	 * 根据planTrainId删除PLAN_TRAIN_STN表中列车时刻信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @author denglj
	 * @date 2014-10-28
	 */
	public static final String DELETE_TRAINSTN_BY_PLANTRAINID = "trainTimeDao.deleteTrainStnByPlanTrainId";

	public static final String ADD_TRAINLINE_ITEM_TEMPS = "trainTimeDao.addTrainlineItemTemps";

	/**
	 * 新增PLAN_TRAIN_STN表记录
	 * 
	 * @param map
	 *            { trainStnList:
	 *            List<org.railway.com.trainplan.entity.TrainTimeInfo> 必填参数 }
	 * @author denglj
	 * @date 2014-10-28
	 */
	public static final String ADD_TRAINSTNS = "trainTimeDao.addPlanTrainStn";

	/**
	 * 根据planTrainId更新开行计划 重置PLAN_TRAIN表“生成运行线、一级审核、二级审核”标记、运行线ID、基本图中列车ID
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @author denglj
	 * @date 2014-10-28
	 */
	public static final String RESET_PLANTRAIN_BY_ID = "trainTimeDao.resetPlanTrainInfoById";

	public static final String UPDATEPLANTRAIN = "trainTimeDao.updatePlanTrainInfoById";

	/**
	 * 修改基本图数据
	 */
	public static final String UPDATE_TRAINLINETRAIN_FORJBTEDIT = "org.railway.com.trainplan.repository.mybatis.BaseTrainDao.updatePlanTrainInfoByIdForJbtEdit";

	public static enum RULETYPE {
		WEEK_RULE(1, "weekRule"), DATE_RULE(2, "dateRule");

		private final int num;
		private final String msg;

		RULETYPE(int num, String msg) {
			this.num = num;
			this.msg = msg;
		}

		public int getNum() {
			return num;
		}

		public String getMsg() {
			return msg;
		}

		public static RULETYPE getEnum(String msg) {

			for (RULETYPE en : RULETYPE.values()) {
				if (en.getMsg().equals(msg))
					return en;
			}
			return RULETYPE.valueOf(msg);
		}
	}

	public static final String CROSSDAO_GET_UNIT_CROSS_TRAIN_DATE_BYMAP = "crossDao.getUnitCrossTrainDateByMap";

	/**
	 * 删除plan_train_stn.
	 */
	public static final String DELETE_PLANTRAINSTN = "runPlanLkDao.deletePlanTrainStn";
	/**
	 * 删除plan_train.
	 */
	public static final String DELETE_PLANTRAIN = "runPlanLkDao.deletePlanTrain";
	/**
	 * 删除cmd_train_stn.
	 */
	public static final String DELETE_CMDTRAINSTN = "runPlanLkDao.deleteCmdTrainStn";
	/**
	 * 删除cmd_train.
	 */
	public static final String DELETE_CMDTRAIN = "runPlanLkDao.deleteCmdTrain";
	/**
	 * 查询getBaseCrossTrainByMap.
	 */
	public static final String GET_BASE_CROSS_TRAIN_BY_MAP = "crossDao.getBaseCrossTrainByMap";
	/**
	 * 查询getBaseCrossTrainByMap.
	 */
	public static final String GET_BASE_CROSS_TRAIN_OBJ_BY_MAP = "crossDao.getBaseCrossTrainObjByMap";
	/**
	 * 查询getBaseCrossByMap.
	 */
	public static final String GET_BASE_CROSS_BY_MAP = "crossDao.getBaseCrossByMap";
	/**
	 * 查询cmd_train是否存在.
	 */
	public static final String GET_CMD_TRAIN_BY_MAP = "runPlanLkDao.getCmdTrainByMap";

	/**
	 * 根据路局删除数据.
	 */
	public static final String DELETE_DICRELACROSSPOST_BY_BUREAU = "dicRelaCrossPostDao.deleteDicrelacrosspostByBureau";

	/**
	 * 根据路局删除数据.
	 */
	public static final String UPDATE_DICRELACROSSPOST_BY_MAP = "dicRelaCrossPostDao.updateRelaCrossPostByMap";
	public static final String CROSSDAO_GET_BASETRAIN_STARTBUREAU_BY_TRAINNBR = "crossDao.getBaseTrainStartBureauByTrainNbr";
	public static final String CROSSDAO_GET_BASETRAIN_ENDBUREAU_BY_TRAINNBR = "crossDao.getBaseTrainEndBureauByTrainNbr";

	/**
	 * 根据Obj(CrossTrainInfo)修改base_cross_train.
	 */
	public static final String UPD_BASE_CROSS_TRAIN_BY_OBJ = "crossDao.updBaseCrossTrainByObj";
	
	/** 消息类型S **/
	/**
	 * 高铁交路计划.
	 */
	public static final String MSG_GT_CROSS_PLAN = "GT_CROSS_PLAN";
	/**
	 * 填报高铁车底.
	 */
	public static final String MSG_TB_GT_CD = "TB_GT_CD";
	/**
	 * 高铁车底计划.
	 */
	public static final String MSG_GT_CD_PLAN = "GT_CD_PLAN";
	/**
	 * 普速车长乘务.
	 */
	public static final String MSG_PS_CZ_CW = "PS_CZ_CW";
	/**
	 * 高铁车长乘务.
	 */
	public static final String MSG_GT_CZ_CW = "GT_CZ_CW";
	/**
	 * 普速司机乘务.
	 */
	public static final String MSG_PS_SJ_CW = "PS_SJ_CW";
	/**
	 * 高铁司机乘务.
	 */
	public static final String MSG_GT_SJ_CW = "GT_SJ_CW";
	/**
	 * 高铁机械师乘务.
	 */
	public static final String MSG_GT_JXS_CW = "GT_JXS_CW";
	/**
	 * 新增图定开行计划.
	 */
	public static final String MSG_ADD_TD_PLAN = "ADD_TD_PLAN";
	/**
	 * 修改图定开行计划.
	 */
	public static final String MSG_UPD_TD_PLAN = "UPD_TD_PLAN";
	/**
	 * 新增临客开行计划.
	 */
	public static final String MSG_ADD_LK_PLAN = "ADD_LK_PLAN";
	/**
	 * 修改临客开行计划.
	 */
	public static final String MSG_UPD_LK_PLAN = "UPD_LK_PLAN";
	/**
	 * 补充临客时刻.
	 */
	public static final String MSG_BC_LK_STN = "BC_LK_STN";
	/**
	 * 核对基本图运行线.
	 */
	public static final String MSG_JBT_CHECKED = "JBT_CHECKED";
	/**
	 * 查询动车所的条件.
	 */
	public static final String SEL_DCS = "站段动车所";
	/**
	 * 查询动车台的条件.
	 */
	public static final String SEL_DCT = "铁路局动车调";
	/**
	 * 查询高铁计划调的条件.
	 */
	public static final String SEL_GT_JHD = "铁路局高铁计划调";
	/**
	 * 查询铁路局客调的条件.
	 */
	public static final String SEL_KD = "铁路局客调";
	/**
	 * 查询客运段的条件.
	 */
	public static final String SEL_KYD = "站段客运段";
	/**
	 * 查询机务段的条件.
	 */
	public static final String SEL_JWD = "站段机务段";
	/**
	 * 查询技教室的条件.
	 */
	public static final String SEL_JJS = "铁路局技教室";
	/** 消息类型E **/
	public static final String GET_PLAN_CROSS_BY_MAP = "org.railway.com.trainplan.repository.mybatis.RunPlanDao.getPlanCrossByMap";
	public static final String GET_PLAN_TRAIN_BY_MAP = "runPlanLkDao.getPlanTrainByMap";
	
}
