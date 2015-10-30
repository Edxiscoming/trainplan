package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.ConstantUtil;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.entity.CmdInfoModel;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.CmdTrainStn;
import org.railway.com.trainplan.entity.CrossRunPlanInfo;
import org.railway.com.trainplan.entity.MTrainLine;
import org.railway.com.trainplan.entity.MTrainLineStn;
import org.railway.com.trainplan.entity.PlanCheckInfo;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.RunPlanStn;
import org.railway.com.trainplan.entity.TrainLineSubInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfoTime;
import org.railway.com.trainplan.entity.runline.TrainTypeModel;
import org.railway.com.trainplan.jdbcConnection.CmdAdapterServiceImpl;
import org.railway.com.trainplan.jdbcConnection.ICmdAdapterService;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.CmdDataDao;
import org.railway.com.trainplan.repository.mybatis.MTrainLineDao;
import org.railway.com.trainplan.repository.mybatis.MTrainLineItemDao;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplanv2.dto.TrainTypeModel2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
//import mor.railway.cmd.adapter.model.CmdInfoModel;
//import mor.railway.cmd.adapter.service.ICmdAdapterService;
//import mor.railway.cmd.adapter.service.impl.CmdAdapterServiceImpl;
//import mor.railway.cmd.adapter.util.ConstantUtil;
import org.apache.commons.collections.MapUtils;

/**
 * 与临客相关的操作
 * 
 * @author join
 *
 */
@Component
@Transactional
@Monitored
public class RunPlanLkService {

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private MTrainLineDao mTrainLineDao;

	@Autowired
	private MTrainLineItemDao mTrainLineItemDao;
	@Autowired
	private CmdDataDao cmdDataDao;

	private static Log logger = LogFactory.getLog(RunPlanLkService.class
			.getName());

	/**
	 * 查询临客的基本信息
	 * 
	 * @param reqMap
	 *            ，包括的字段：startDate，endDate，trainNbr，
	 *            startBureau，isRelationBureau，bureau，tokenVehBureau
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RunPlan> getPlanTrainLkInfo(Map<String, Object> reqMap) {
		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_PLANTRAIN_LK_INFO, reqMap);
	}

	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RunPlanTrainDto> getTrainLkRunPlans(Map<String, Object> map)
			throws Exception {
		List<RunPlanTrainDto> runPlans = Lists.newArrayList();
		Map<String, RunPlanTrainDto> runPlanTrainMap = Maps.newHashMap();
		List<CrossRunPlanInfo> crossRunPlans = baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_TRAINLK_RUNPLAN, map);
		String startDay = map.get("startDate").toString();
		String endDay = map.get("endDate").toString();
		for (CrossRunPlanInfo runPlan : crossRunPlans) {
			RunPlanTrainDto currTrain = runPlanTrainMap.get(runPlan
					.getTrainNbr());
			String isModified = runPlan.getIsModified();
			String planTrainId = runPlan.getPlanTrainId();
			if (currTrain == null) {
				currTrain = new RunPlanTrainDto(startDay, endDay, null,
						planTrainId, isModified);
				currTrain.setTrainNbr(runPlan.getTrainNbr());
				runPlanTrainMap.put(runPlan.getTrainNbr(), currTrain);
			}
			currTrain.setRunFlag(runPlan.getRunDay(), runPlan.getRunFlag(),
					planTrainId, isModified);
		}
		runPlans.addAll(runPlanTrainMap.values());
		return runPlans;
	}

	@SuppressWarnings("unchecked")
	public List<CrossRunPlanInfo> getTrainLkRunPlans1(String map)
			throws Exception {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("cmdTrainId", map);
		List<CrossRunPlanInfo> crossRunPlans = baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_TRAINLK_RUNPLAN1, reqMap);

		return crossRunPlans;
	}

	/**
	 * 根据车次等信息查询列车信息列表(分页)
	 */
	public List<CmdTrain> getCmdTrainForPage(Map<String, Object> params) {
		return baseDao.selectListBySql(Constants.RUNPLANLK_GETCMDTRAIN_PAGE,
				params);
	}

	/**
	 * 通过plan_train_id从PLAN_TRAIN_STN中获取列车时刻表
	 * 
	 * @param planTrainIds
	 *            , 格式如： '','',''
	 * @return
	 */
	public List<TrainLineSubInfo> getTrainLkInfoForPlanTrainId(
			String planTrainIds) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("planTrainIds", planTrainIds);
		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_TRAINLK_FOR_PLAN_TRAIN_ID, reqMap);
	}

	/**
	 * 查询cmdTrainStn表信息
	 * 
	 * @param cmdTrainId
	 *            cmdTrain表主键
	 * @return
	 */
	public List<CmdTrainStn> getCmdTrainStnInfo(String cmdTrainId) {
		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_CMD_TRAINSTN_INFO, cmdTrainId);
	}

	/**
	 * 根据cmdTxtmlItemId查询cmdTrain信息
	 * 
	 * @param cmdTxtmlId
	 *            // String cmdTxtmlItemId
	 * @return
	 */
	public CmdTrain getCmdTrainInfoForCmdTxtmlItemId(Map<String, String> reqMap) {
		// Map<String, String> reqMap = new HashMap<String, String>();
		// reqMap.put("cmdTxtmlItemId", cmdTxtmlItemId);
		return (CmdTrain) baseDao.selectOneBySql(
				Constants.RUNPLANLKDAO_GET_CMD_TRAININFO_FOR_CMDMLID, reqMap);
	}

	/**
	 * 关键车次等信息查询列车总数
	 */
	public int getCmdTrainfoCount(Map<String, Object> params) {
		List<Map<String, Object>> countList = baseDao.selectListBySql(
				Constants.RUNPLANLK_GETCMDTRAIN_COUNT, params);
		int count = 0;
		if (countList != null && countList.size() > 0) {
			// 只有一条数据
			Map<String, Object> map = countList.get(0);
			count = ((BigDecimal) map.get("COUNT")).intValue();
		}
		return count;
	}

	/**
	 * 根据CmdTrainId查询cmdTrain信息
	 * 
	 * @param CmdTrainId
	 * @return
	 */
	public CmdTrain getCmdTrainInfoForCmdTrainId(String cmdTrainId) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("cmdTrainId", cmdTrainId);
		return (CmdTrain) baseDao.selectOneBySql(
				Constants.RUNPLANLKDAO_GET_CMD_TRAININFO_FOR_CMDTRAIN, reqMap);
	}

	/**
	 * 
	 * @param startDate
	 *            格式:yyyy-MM-dd
	 * @param endDate
	 *            格式:yyyy-MM-dd
	 * @param bureuaCode
	 *            局码
	 * @return
	 */
	public List<CmdInfoModel> getCmdTrainInfoFromRemote(CmdInfoModel model) {// lilong-jar
		// 构造接口服务实例
		ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
		// 服务初始化
		service.initilize(model.getCmdBureau());
		// 根据开始结束时间，查询符合条件的临客命令对象集合
		List<CmdInfoModel> list = service
				.findCmdInfoModelListByDateAndBureau(model);
		return list;
	}

	// public List<CmdInfoModel> getCmdTrainInfoFromRemote(CmdInfoModel model)
	// {//suntao
	// // 根据开始结束时间，查询符合条件的临客命令对象集合
	// ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
	// .getSubject().getPrincipal();
	//
	// StringBuffer message = new StringBuffer();
	// List<CmdInfoModel> modelList = new ArrayList<CmdInfoModel>();
	// message.append("\r\n路局码：" + user.getBureau());
	// message.append("\r\n路局参数：" + user.getBureauFullName());
	//
	// // message.append("\r\n路局新参数：" + bureauName);
	//
	// modelList = cmdDataDao.findLKMlByStartAndDate(
	// paraModel, bureauName);
	//
	// message.append("[临客查询：局码="
	// + user.getBureau()
	// + ",局简称="
	// + user.getBureauShortName()
	// + ",开始日期="
	// + StringAndTimeUtil.yearMonthDayFullSdf.format(paraModel
	// .getStartDate())
	// + ",结束日期="
	// + StringAndTimeUtil.yearMonthDayFullSdf.format(paraModel
	// .getEndDate()) + ",个数" + modelList.size() + "]");
	//
	// // 20150202 调试临客命令查不到情况。何宇阳
	// logger.error(message.toString());
	// return modelList;
	// }

	public CmdInfoModel getCmdTrainInfoByCmdItem(String bureuaCode,
			String cmdItemId) {

		// 构造接口服务实例
		ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
		// 服务初始化
		service.initilize(bureuaCode);
		CmdInfoModel model = service.findCmdInfoModelByBureauAndItemId(bureuaCode, cmdItemId);
		return model;
	}

	/**
	 * 根据CMD_TRAIN(命令生成开行计划列车表)中字段计算可以生成开行计划(停运开行计划)的日期集合
	 * 
	 * @param startDate
	 *            格式yyyy-MM-dd
	 * @param endDate
	 *            格式yyyy-MM-dd
	 * @param cmdType
	 *            命令类型 (既有加开；既有停运；高铁加开；高铁停运)
	 * @param rule
	 * @param selectedDate
	 * @return
	 */
	public List<Date> getSelectedDateListFromRemote(CmdInfoModel model) {

		return ConstantUtil.getSelectedDateList(model);
	}

	/**
	 * 保存表cmd_train_stn中数据
	 * 
	 * @param cmdTrainStn
	 * @return
	 */
	public int insertCmdTrainStn(CmdTrainStn cmdTrainStn) {
		return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_CMD_TRAIN_STN,
				cmdTrainStn);
	}

	/**
	 * 保存表cmd_train_stn中数据
	 * 
	 * @param cmdTrainStn
	 * @return
	 */
	public int updateCmdTrain(String startStn, String endStn,
			String cmdTrainId, String startBureauId, String endBureauId) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("startStn", startStn);
		reqMap.put("endStn", endStn);
		reqMap.put("cmdTrainId", cmdTrainId);
		reqMap.put("startBureauId", startBureauId);
		reqMap.put("endBureauId", endBureauId);

		return baseDao.updateBySql(Constants.UPDATECMDTRAINFORCMDTRAINDID,
				reqMap);
	}

	/**
	 * 保存表cmd_train中数据
	 * 
	 * @param cmdTrain
	 * @return
	 */
	public int insertCmdTrain(CmdTrain cmdTrain) {
		return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_CMD_TRAIN,
				cmdTrain);
	}

	/**
	 * 通过cmdTrainId删除表cmd_train_stn中的数据
	 * 
	 * @param cmdTrainId
	 * @return
	 */
	public int deleteCmdTrainStnForCmdTrainId(String cmdTrainId,
			String stnBureau) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("cmdTrainId", cmdTrainId);
		reqMap.put("stnBureau", stnBureau);
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_DELETE_CMD_TRAINSTN_FOR_CMDTRAINID,
				reqMap);
	}

	/**
	 * 根据cmdTrainId更新途径局passBureau
	 * 
	 * @param passBureau
	 *            途径局
	 * @param cmdTrainId
	 * @return
	 */

	public int updatePassBureauForCmdTraindId(String passBureau,
			String selectState, String cmdTrainId, String business,
			String startBureauId, String startStnId, String endBureauId,
			String endStnId, String endDays, String trainTypeId,
			int CmdTxtMlId, int CmdTxtMlItemId) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("passBureau", passBureau);
		reqMap.put("selectState", selectState);
		reqMap.put("cmdTrainId", cmdTrainId);

		reqMap.put("business", business);
		reqMap.put("startBureauId", startBureauId);
		reqMap.put("startStnId", startStnId);

		reqMap.put("endBureauId", endBureauId);
		reqMap.put("endStnId", endStnId);
		reqMap.put("endDays", endDays);

		reqMap.put("trainTypeId", trainTypeId);
		reqMap.put("CmdTxtMlId", String.valueOf(CmdTxtMlId));
		reqMap.put("CmdTxtMlItemId", String.valueOf(CmdTxtMlItemId));

		return baseDao.updateBySql(
				Constants.RUNPLANLKDAO_UPDATE_PASS_BUREAU_FOR_CMD_TRAINID,
				reqMap);
	}

	/**
	 * 根据cmdTrainId更新途径局passBureau1
	 * 
	 * @param passBureau
	 *            途径局
	 * @param cmdTrainId
	 * @return
	 */
	public int updatePassBureauForCmdTraindId1(String passBureau,
			String selectState, String cmdTrainId, String business,
			String startBureauId, String startStnId, String endBureauId,
			String endStnId, String endDays, String trainTypeId,
			int CmdTxtMlId, int CmdTxtMlItemId, Map<String, String> paramMap) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("passBureau", passBureau);
		reqMap.put("selectState", selectState);
		reqMap.put("cmdTrainId", cmdTrainId);

		reqMap.put("business", business);
		reqMap.put("startBureauId", startBureauId);
		reqMap.put("startStnId", startStnId);

		reqMap.put("endBureauId", endBureauId);
		reqMap.put("endStnId", endStnId);
		reqMap.put("endDays", endDays);

		reqMap.put("trainTypeId", trainTypeId);
		reqMap.put("CmdTxtMlId", String.valueOf(CmdTxtMlId));
		reqMap.put("CmdTxtMlItemId", String.valueOf(CmdTxtMlItemId));

		// 新增字段
		reqMap.put("start_station_stn_id",
				MapUtils.getString(paramMap, "sourceNodeStationId"));
		reqMap.put("start_station_stn_name",
				MapUtils.getString(paramMap, "sourceNodeStationName"));
		reqMap.put("start_stn_tdcs_id",
				MapUtils.getString(paramMap, "sourceNodeTdcsId"));
		reqMap.put("start_stn_tdcs_name",
				MapUtils.getString(paramMap, "sourceNodeTdcsName"));
		reqMap.put("end_station_stn_id",
				MapUtils.getString(paramMap, "targetNodeStationId"));
		reqMap.put("end_station_stn_name",
				MapUtils.getString(paramMap, "targetNodeStationName"));
		reqMap.put("end_stn_tdcs_id",
				MapUtils.getString(paramMap, "targetNodeTdcsId"));
		reqMap.put("end_stn_tdcs_name",
				MapUtils.getString(paramMap, "targetNodeTdcsName"));

		return baseDao.updateBySql(
				Constants.RUNPLANLKDAO_UPDATE_PASS_BUREAU_FOR_CMD_TRAINID,
				reqMap);
	}

	/**
	 * 根据cmdTrainId更新cmdTrain
	 * 
	 * @param cmdTrain
	 * @param cmdTrainId
	 * @return
	 */
	public int updateCmdTrainForCmdTraindId(CmdTrain cmdTrain) {
		return baseDao.updateBySql(
				Constants.RUNPLANLKDAO_UPDATE_CMD_TRAIN_FOR_CMDTRAIN, cmdTrain);
	}

	/**
	 * 更新下标
	 * 
	 * @param cmdTrain
	 * @param cmdTrainId
	 * @return
	 */
	public int updateCmdTrainStnSort(CmdTrainStn cmdTrainStn) {
		return baseDao.updateBySql(
				Constants.RUNPLANLKDAO_UPDATE_CMDTRAINSTN_FOR_CMDTRAIN,
				cmdTrainStn);
	}

	/**
	 * 根据cmdTrainId查询表cmd_train和表cmd_train_stn的关联数据
	 * 
	 * @param cmdTrainId
	 * @return
	 */
	public List<CmdTrain> getCmdTrandAndStnInfo(String cmdTrainId) {
		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_CMDTRAIN_AND_STNINFO, cmdTrainId);
	}

	/**
	 * 根据多条件查询表cmd_train数据对象
	 * 
	 * @param cmdTrain
	 * @return
	 */
	public List<CmdTrain> getCmdTraindForMultipleParame(CmdTrain cmdTrain) {
		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_CMDTRAIN_FOR_MULTIPLE_PARAME,
				cmdTrain);
	}

	/**
	 * 生成临客客运计划，对表plan_train插入数据
	 * 
	 * @param runPlan
	 * @return
	 */
	public int addRunPlanLk(RunPlan runPlan) {
		return baseDao.insertBySql(Constants.RUNPLANLKDAO_ADD_RUN_PLAN_LK,
				runPlan);
	}

	/**
	 * 生成临客客运计划，对表plan_train_stn插入数据
	 * 
	 * @param trainStnList
	 * @return
	 */
	public int addRunPlanLkTrainStn(List<RunPlanStn> trainStnList) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("trainStnList", trainStnList);
		return baseDao.insertBySql(Constants.RUNPLANLKDAO_ADD_RUN_PLAN_STN_LK,
				reqMap);
	}

	/**
	 * 根据站名获取站的基本信息，包括站名，站所属局局码，局简称，局全称
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getBaseStationInfo(String stnName) {
		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_BASE_STATION_INFO, stnName);
	}

	public List<Map<String, Object>> getPlanTrainIdForCmdTrainId(
			String cmdTrainId) {

		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_PLANTRAINID_FOR_CMDTRAINID,
				cmdTrainId);
	}

	public int deleteTrainForCmdTrainId(String cmdTrainId) {
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_DELETE_TRAIN_FOR_CMDTRAINID, cmdTrainId);
	}

	public int deleteCmdTrainForCmdTrainId(String cmdTrainId) {
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_DELETE_CMDTRAIN_FOR_CMDTRAINID,
				cmdTrainId);
	}

	public int deleteTrainStnForPlanTrainId(String planTrainId) {
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_DELETE_TRAINSTN_FOR_PLANTRAINID,
				planTrainId);
	}

	public int deleteTrainStnForPlanTrainIdFa(String cmdTrainId) {
		return baseDao.deleteBySql(
				Constants.RUNPLANLKDAO_DELETE_TRAINSTN_FOR_PLANTRAINIDFA,
				cmdTrainId);
	}

	/**
	 * 更新表cmd_train中字段createState
	 * 
	 * @param cmdTrainId
	 * @param createState
	 *            0:未生成 1：已生成
	 * @return
	 */
	public int updateCreateStateForCmdTrainId(String cmdTrainId,
			String createState) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("cmdTrainId", cmdTrainId);
		reqMap.put("createState", createState);
		return baseDao.updateBySql(Constants.RUNPLANLKDAO_UPDATE_CREATESTATE,
				reqMap);
	}

	/**
	 * 生成运行线，对表M_TRAINLINE添加数据
	 * 
	 * @param trainLine
	 * @return
	 */
	public int insertMTrainLine(MTrainLine trainLine) {
		return baseDao.insertBySql(Constants.RUNPLANLKDAO_INSERT_MTRAINLINE,
				trainLine);
	}

	/**
	 * 生成运行线，对表M_TRAINLINE_ITEM添加数据
	 * 
	 * @param trainStnList
	 * @return
	 */
	public int insertMTrainLineStnRoute(MTrainLineStn trainLineStn) {
		return baseDao.insertBySql(
				Constants.RUNPLANLKDAO_INSERT_MTRAINLINE_ROUTE, trainLineStn);
	}

	/**
	 * 根据列车类型id，获取列车类型
	 * 
	 * @param typeId
	 * @return
	 */
	public TrainTypeModel getTrainTypeForTypeId(String typeId) {
		return (TrainTypeModel) baseDao.selectOneBySql(
				Constants.RUNPLANLKDAO_GET_TRAINTYPE_FOR_ID, typeId);
	}

	/**
	 * 根据列车类型id，获取列车类型
	 * 
	 * @param typeId
	 * @return suntao
	 */
	public TrainTypeModel2 getTrainTypeForTypeId2(String typeId) {
		return (TrainTypeModel2) baseDao.selectOneBySql2(
				Constants.RUNPLANLKDAO_GET_TRAINTYPE_FOR_ID2, typeId);
	}

	public List<TrainLineSubInfoTime> getTrainLineSubinfoTime(String planTrainId) {
		return baseDao.selectListBySql(
				Constants.RUNPLANLKDAO_GET_TRAINLINE_SUBINFO_TIME, planTrainId);
	}

	/**
	 * 查询运行线数量
	 * 
	 * @param
	 * @return
	 */
	public int selectMTrainLineCount(String MTrainLineId) {
		return mTrainLineDao.selectMTrainLineCount(MTrainLineId);
	}

	/**
	 * 查询运行线子项数量
	 * 
	 * @param
	 * @return
	 */
	public int selectMTrainLineItemCount(String MTrainLineId) {
		return mTrainLineDao.selectMTrainLineItemCount(MTrainLineId);
	}

	/**
	 * 删除运行线
	 * 
	 * @param
	 * @return
	 */
	public void deleteMTrainLineByMTrainLineId(String MTrainLineId) {
		mTrainLineDao.deleteMTrainLineByMTrainLineId(MTrainLineId);
	}

	/**
	 * 删除运行线子项
	 * 
	 * @param
	 * @return
	 */
	public void deleteMTrainLineItemByMTrainLineId(String MTrainLineId) {
		mTrainLineDao.deleteMTrainLineItemByMTrainLineId(MTrainLineId);
	}

	/**
	 * 根据CmdTrainId查询cmdTrain信息
	 * 
	 * @param CmdTrainId
	 * @return
	 * @throws ParseException
	 */
	public int updateRunPlanByStopCmdTrain(List<String> cmdTrainIdList)
			throws ParseException {
		int stopCount = 0;
		// ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)
		// SecurityUtils.getSubject().getPrincipal();

		for (String cmdId : cmdTrainIdList) {
			if (cmdId != null && !"".equals(cmdId)) {
				Map<String, String> reqMap = new HashMap<String, String>();
				reqMap.put("cmdTrainId", cmdId);
				CmdTrain cmdObj = (CmdTrain) baseDao.selectOneBySql(
						Constants.RUNPLANLKDAO_GET_CMD_TRAININFO_FOR_CMDTRAIN,
						reqMap);
				if (cmdObj != null) {

					logger.info("updateRunPlanByStopCmdTrain~~cmdObj!=null");
					List<String> stopDateList = new ArrayList<String>();
					if (cmdObj.getRule() == null || "".equals(cmdObj.getRule())
							|| "择日".equals(cmdObj.getRule())) {
						// null、"":从命令加载来 择日：文电（本系统界面录入）
						CmdInfoModel model = new CmdInfoModel();
						model.setSelectedDate(cmdObj.getSelectedDate());
						stopDateList = ConstantUtil
								.getSelectedDateStrList(model);// 获取停运日期
					} else if ("每日".equals(cmdObj.getRule())) {
						stopDateList = DateUtil.getDateStrBetweenStartToEnd(
								cmdObj.getStartDate(), cmdObj.getEndDate(), -1);
					} else if ("隔日".equals(cmdObj.getRule())) {
						stopDateList = DateUtil.getDateStrBetweenStartToEnd(
								cmdObj.getStartDate(), cmdObj.getEndDate(), -2);
					}

					// 1、停运开行计划
					for (String stopDate : stopDateList) {
						logger.info("updateRunPlanByStopCmdTrain~~String stopDate: stopDateList: "
								+ stopDate);
						Map<String, Object> stopMap = new HashMap<String, Object>();
						stopMap.put("trainNbr", cmdObj.getTrainNbr());
						stopMap.put("runDate", stopDate.replaceAll("-", ""));
						/* stopCount += */baseDao
								.updateBySql(
										Constants.TRAININFO_UPDATE_SPARE_FLAG_BY_CMD_TRAIN_ID,
										stopMap);// 将开行计划标识改为停运
					}
				}

				// 2.修改cmd_train状态 createState(0：未；1：已，默认值0)
				stopCount += this.updateCreateStateForCmdTrainId(cmdId, "1");
			}
		}

		return stopCount;
	}

	/**
	 * 查询m_trainline_type数据.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMTrainlineTypeByMap(
			Map<String, Object> paramMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("'").append("客运").append("'").append(",").append("'")
				.append("单机和路用").append("'").append(",").append("'")
				.append("其他").append("'");
		paramMap.put("param", sql);
		return baseDao.selectListBySql(Constants.Get_M_Trainline_Type_Map,
				paramMap);
	}

	public String getMTrainlineTypeByTrainNbr(String trainNbr) {
//		if(trainNbr.length() <= 2){
//			return dealWithType2(trainNbr);
//		}else{
			return dealWithType1(trainNbr);
//		}
		
	}

	@SuppressWarnings("unchecked")
	private String dealWithType1(String trainNbr) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = null;

		String[] str = trainNbr(trainNbr, 2);
		StringBuilder sql = new StringBuilder();
		sql.append("'").append("客运").append("'").append(",").append("'")
				.append("单机和路用").append("'").append(",").append("'")
				.append("其他").append("'");
		paramMap.put("param", sql);
		paramMap.put("identify", (String) str[0]);
		paramMap.put("code", (String) str[1]);
		returnList = baseDao.selectListBySql(
				Constants.Get_M_Trainline_Type_Map, paramMap);
		if (null != returnList && !returnList.isEmpty()) {
			return (String) returnList.get(0).get("ID");
		} else {
			returnList.clear();
			str = trainNbr(trainNbr, 1);
			paramMap.put("identify", (String) str[0]);
			if (!str[1].matches("[0-9]+")) {
				return null;
			}
			paramMap.put("code", (String) str[1]);
			returnList = baseDao.selectListBySql(
					Constants.Get_M_Trainline_Type_Map, paramMap);
			if (null != returnList && !returnList.isEmpty()) {
				return (String) returnList.get(0).get("ID");
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private String dealWithType2(String trainNbr) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> returnList = null;
		StringBuilder sql = new StringBuilder();
		sql.append("'").append("客运").append("'").append(",").append("'")
				.append("单机和路用").append("'").append(",").append("'")
				.append("其他").append("'");
		paramMap.put("param", sql);
		String[] str = trainNbr(trainNbr, 1);
		paramMap.put("identify", (String) str[0]);
		if (!str[1].matches("[0-9]+")) {
			return null;
		}
		paramMap.put("code", (String) str[1]);
		returnList = baseDao.selectListBySql(
				Constants.Get_M_Trainline_Type_Map, paramMap);
		if (null != returnList && !returnList.isEmpty()) {
			return (String) returnList.get(0).get("ID");
		}
		return null;
	}

	private String[] trainNbr(String trainNbr, int i) {
		if (trainNbr.indexOf("/") != -1) {
			trainNbr = trainNbr.split("/")[0];
		}
		if (trainNbr.length() > i) {
			String str1 = trainNbr.substring(0, i);
			String str2 = trainNbr.substring(i, trainNbr.length());
			String[] str3 = new String[] { str1, str2 };
			return str3;
		} else {
			return new String[] { trainNbr, trainNbr };
		}
	}

	/**
	 * 校验当前的审核状态.
	 * 
	 * @param a
	 *            planCrossIds.
	 * @param runPlanService
	 * @param bureau
	 * @return
	 */
	public Result checkCurrent(String[] a, RunPlanService runPlanService,
			String bureau) {
		Result result = new Result();
		for (int i = 0; i < a.length; i++) {
			CmdTrain cmdlist = getCmdTrainInfoForCmdTrainId(a[i]);
			String passburu = "";
			if (cmdlist != null) {
				passburu = cmdlist.getPassBureau();
			}
			List<PlanCheckInfo> checklist = runPlanService
					.getPlanCheckInfoForPlanCrossIdcmdtel(a[i]);
			if (checklist.size() != 0) {
				if (passburu.trim().length() == checklist.size()) {
					// 途经局已经全部审核
					result.setCode("5");// 已经审核
					result.setMessage(cmdlist.getTrainNbr());
					return result;
				}
				for (PlanCheckInfo planCheckInfo : checklist) {
					if (planCheckInfo.getCheckBureau().equals(bureau)) {
						result.setCode("3");// 已经审核
						result.setMessage(cmdlist.getTrainNbr());
						return result;
					}
				}
			}
		}
		return result;
	}

	public void deleteRunPlanLk(Map<String, Object> reqMap) {
		// del plan_train_stn
		baseDao.deleteBySql(Constants.DELETE_PLANTRAINSTN, reqMap);
		// del plan_train
		baseDao.deleteBySql(Constants.DELETE_PLANTRAIN, reqMap);
		// DEL cmd_train_stn
		baseDao.deleteBySql(Constants.DELETE_CMDTRAINSTN, reqMap);
		// del cmd_train
		baseDao.deleteBySql(Constants.DELETE_CMDTRAIN, reqMap);

	}

	/**
	 * 根据map中的信息，删除运行线数据.
	 * 
	 * @param reqMap
	 */
	public void deleteMTrainLineByMap(Map<String, Object> reqMap) {
		// del m_trainline_item
		mTrainLineItemDao.deleteMTrainLineItemByCmdId(reqMap);
		// del m_trainline
		mTrainLineDao.deleteMTrainLineByCmdId(reqMap);
	}

	/**
	 * 查询cmd_train.
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getCmdTrainByMap(Map<String, Object> params) {
		return baseDao.selectListBySql(Constants.GET_CMD_TRAIN_BY_MAP, params);
	}

	/**
	 * 
	 * 
	 * @param reqMap
	 * @return
	 */
	public List<PlanTrain> getPlanTrainByMap(Map<String, Object> reqMap) {
		return baseDao.selectListBySql(Constants.GET_PLAN_TRAIN_BY_MAP, reqMap);
	}

	/**
	 * 处理交路(1.G1-G2(太西京)).
	 * 
	 * @param ptList
	 *            交路集合(planCross).
	 * @return
	 */
	public String dealWithCross(List<PlanTrain> ptList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ptList.size(); i++) {
			if (org.apache.commons.lang.StringUtils.isNotEmpty(sb.toString())) {
				sb.append("\n");
			}
			PlanTrain pc = ptList.get(i);
			sb.append(i + 1);
			sb.append(".");
			sb.append(pc.getTrainNbr());
			sb.append("(");
			sb.append(pc.getPassBureau());
			sb.append(")");
		}
		return sb.toString();
	}

	/**
	 * 处理交路.
	 * 
	 * @param relevantBureau
	 *            (NHPV)
	 * @return (武上京啊)
	 */
	public String dealWithRelevantBureau(String relevantBureau) {
		StringBuilder sb = new StringBuilder();
		if (org.apache.commons.lang.StringUtils.isNotEmpty(relevantBureau)) {
			for (int i = 0; i < relevantBureau.length(); i++) {
				sb.append(LjUtil.getLjByNameBs(
						relevantBureau.substring(i, i + 1), 2));
			}
		}
		return sb.toString();
	}

	/**
	 * 处理交路.
	 * 
	 * @param relevantBureau
	 *            (武上京啊)
	 * @return (NHPV)
	 */
	public String dealWithRelevantBureau2(String relevantBureau) {
		StringBuilder sb = new StringBuilder();
		if (org.apache.commons.lang.StringUtils.isNotEmpty(relevantBureau)) {
			for (int i = 0; i < relevantBureau.length(); i++) {
				sb.append(LjUtil.getLjByName(
						relevantBureau.substring(i, i + 1), 2));
			}
		}
		return sb.toString();
	}

	/**
	 * 处理交路.
	 * 
	 * @param relevantBureau
	 *            (NHPV).
	 * @param dqBureau
	 *            当前局.
	 * @return ('N','H','P','V')
	 */
	public String dealWithRelevantBureau1(List<PlanTrain> ptList,
			String dqBureau) {
		StringBuilder sb = new StringBuilder();
		if (org.apache.commons.lang.StringUtils.isNotEmpty(dqBureau)) {
			for (int i = 0; i < ptList.size(); i++) {
				String relevantBureau = ptList.get(i).getPassBureau();
				if (org.apache.commons.lang.StringUtils
						.isNotEmpty(relevantBureau)) {
					for (int j = 0; j < relevantBureau.length(); j++) {
						String str = LjUtil.getLjByName(
								relevantBureau.substring(j, j + 1), 2);
						if (!org.apache.commons.lang.StringUtils.equals(str,
								dqBureau)) {
							if (org.apache.commons.lang.StringUtils
									.isNotEmpty(sb.toString())) {
								sb.append(",");
							}
							sb.append("'");
							sb.append(str);
							sb.append("'");
						}
					}
				}
			}
		}
		return sb.toString();
	}
}
