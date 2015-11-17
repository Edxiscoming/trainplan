package org.railway.com.trainplan.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.javasimon.aop.Monitored;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.CommonUtil;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BusinessInfo;
import org.railway.com.trainplan.entity.CreateRunLineDate;
import org.railway.com.trainplan.entity.CreateRunPlanBySpecialRules;
import org.railway.com.trainplan.entity.CrossRunPlanInfo;
import org.railway.com.trainplan.entity.LevelCheck;
import org.railway.com.trainplan.entity.PlanCheckInfo;
import org.railway.com.trainplan.entity.PlanCross;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.RunPlanStn;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.entity.UnitCross;
import org.railway.com.trainplan.entity.UnitCrossTrain;
import org.railway.com.trainplan.entity.VaildPlanTrainData;
import org.railway.com.trainplan.entity.VaildPlanTrainTemp;
import org.railway.com.trainplan.exceptions.DailyPlanCheckException;
import org.railway.com.trainplan.exceptions.UnknownCheckTypeException;
import org.railway.com.trainplan.exceptions.WrongBureauCheckException;
import org.railway.com.trainplan.exceptions.WrongCheckTypeException;
import org.railway.com.trainplan.exceptions.WrongDataException;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.BaseTrainDao;
import org.railway.com.trainplan.repository.mybatis.PlanCrossDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanStnDao;
import org.railway.com.trainplan.repository.mybatis.UnitCrossDao;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanCrossDto;
import org.railway.com.trainplan.service.dto.PlanTrainDTO2;
import org.railway.com.trainplan.service.dto.PlanTrainDTOForModify;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.service.dto.TrainRunDto;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.dto.RunPlanRollingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 客运计划服务类 Created by star on 5/12/14.
 */
@Component
@Transactional
@Monitored
public class RunPlanService {

	private static final Log logger = LogFactory.getLog(RunPlanService.class);

	@Autowired
	private RunPlanDao runPlanDao;

	@Autowired
	private RunPlanService2 runPlanService2;

	@Autowired
	private RunPlanStnDao runPlanStnDao;

	@Autowired
	private UnitCrossDao unitCrossDao;

	@Autowired
	private BaseTrainDao baseTrainDao;

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private CrossService crossService;

	@Autowired
	private SendMsgService msgService;

	@Autowired
	private PlanCrossDao planCrossDao;

	@Autowired
	private RunPlanCreateAuxiliaryService runPlanCreateAuxiliaryService;

//	@Value("#{restConfig['plan.generatr.thread']}")
	private int threadNbr = 25;
	
	public PlanCrossInfo findById(String planCrossId){
		return planCrossDao.findById(planCrossId);
	}
	
	public PlanCrossInfo findByUnitCrossId(String unitCrossId){
		return planCrossDao.findByUnitCrossId(unitCrossId);
	}

	/**
	 * 查询客运计划列表（开行计划列表）
	 * 
	 * @param date
	 *            日期
	 * @param bureau
	 *            所属局
	 * @param name
	 *            车次
	 * @param type
	 *            查询类型，详看下面switch
	 * @param trainType
	 *            是否高线
	 * @return 计划列表
	 * @throws ParseException
	 */
	public List<PlanTrainDTO2> findRunPlan(String date, String checkLev2Type,
			String tokenVehBureauNbr, String bureau, String bureauName,
			int type, int trainType) throws ParseException {
		logger.debug("findRunPlan::::");
		Map<String, Object> map = Maps.newHashMap();
		map.put("date", date);
		map.put("checkLev2Type", checkLev2Type);
		map.put("tokenVehBureauNbr", tokenVehBureauNbr);
		map.put("bureauCode", bureau);
		map.put("bureauName", bureauName);
		// map.put("highlineFlag", "1");
		// map.put("name", name);
		map.put("trainType", trainType);

		List<PlanTrainDTO2> list = Lists.newArrayList();
		List<Map<String, Object>> list2 = Lists.newArrayList();
		if (type == 0) {
			map.put("runType", null);
		}
		if (type == 1) {
			map.put("runType", "SFZD");
		}
		if (type == 2) {
			map.put("runType", "SFJC");
		}
		if (type == 3) {
			map.put("runType", "JRZD");
		}
		if (type == 4) {
			map.put("runType", "JRJC");
		}
		if (trainType == 0) {//
			// 普线的分界日期是这一天的18:点，18.之后的车属于明天的车
			Date end = DateUtil.parseDate(date, "yyyy-MM-dd");
			Date start = DateUtil.mulDateOneDay(end);// 开始时间是结束时间减去1
			String dateStart = DateUtil.getStringFromDate(start, "yyyy-MM-dd")
					+ " 18:00:00";
			String dateEnd = DateUtil.getStringFromDate(end, "yyyy-MM-dd")
					+ " 18:00:00";
			// and start_time <= '2014-10-17 18:00:00' and end_time >
			// '2014-10-16 18:00:00'
			map.put("startLimit", dateEnd);
			map.put("endLimit", dateStart);
			list = runPlanDao.findRunPlan_all(map);
		}
		if (trainType == 1) {// highLine
			list = runPlanDao.findRunPlan_allGT(map);
		}
		return list;
	}

	/**
	 * 核对计划,查询.
	 * 
	 * @param date
	 *            日期.
	 * @param checkLev2Type
	 * @param tokenVehBureauNbr
	 * @param bureau
	 * @param bureauName
	 * @param type
	 * @param trainType
	 * @param train_nbr
	 *            车次
	 * @return
	 * @throws ParseException
	 */
	public List<PlanTrainDTO2> findRunPlan1(String date, String sentFlag,
			String tokenVehBureauNbr, String bureau, String bureauName,
			int type, int trainType, String train_nbr) throws ParseException {
		Map<String, Object> map = Maps.newHashMap();
		map.put("date", date);
		map.put("sentFlag", sentFlag);
		map.put("tokenVehBureauNbr", tokenVehBureauNbr);
		map.put("bureauCode", bureau);
		map.put("bureauName", bureauName);
		map.put("trainType", trainType);
		map.put("train_nbr", train_nbr);

		List<PlanTrainDTO2> list = Lists.newArrayList();
		if (type == 0) {
			map.put("runType", null);
		}
		if (type == 1) {
			map.put("runType", "SFZD");
		}
		if (type == 2) {
			map.put("runType", "SFJC");
		}
		if (type == 3) {
			map.put("runType", "JRZD");
		}
		if (type == 4) {
			map.put("runType", "JRJC");
		}
		if (trainType == 0) {//
			// 普线的分界日期是这一天的18:点，18.之后的车属于明天的车
			Date end = DateUtil.parseDate(date, "yyyy-MM-dd");
			Date start = DateUtil.mulDateOneDay(end);// 开始时间是结束时间减去1
			String dateStart = DateUtil.getStringFromDate(start, "yyyy-MM-dd")
					+ " 18:00:00";
			String dateEnd = DateUtil.getStringFromDate(end, "yyyy-MM-dd")
					+ " 18:00:00";
			// and start_time <= '2014-10-17 18:00:00' and end_time >
			// '2014-10-16 18:00:00'
			map.put("startLimit", dateEnd);
			map.put("endLimit", dateStart);

			long time = System.currentTimeMillis();

			// int a = (int)
			// baseDao.selectOneBySql("org.railway.com.trainplan.repository.mybatis.RunPlanDao.judge_findRunPlan_all",
			// null);
			// logger.error(a);
			// logger.error("just a test 11");
			list = runPlanDao.findRunPlan_all(map);
			// logger.error("just a test time : "
			// + (System.currentTimeMillis() - time) + "ms");
			// logger.error("just a test : " + list.get(0).getStr1());
		}
		if (trainType == 1) {// highLine
			list = runPlanDao.findRunPlan_allGT(map);
		}
		return list;
	}

	/**
	 * 根据列车id查询列车时刻表
	 * 
	 * @param planId
	 *            列车id
	 * @return 时刻表
	 */
	public List<Map<String, Object>> findPlanTimeTableByPlanId(String planId) {
		logger.debug("findPlanTimeTableByPlanId::::");
		return runPlanDao.findPlanTimeTableByPlanId(planId);
	}

	/**
	 * 根据plan_cross_id删除m_trainline.
	 * 
	 * @param crossIdsArray
	 *            plan_cross_id
	 * @return
	 */
	public int deleteMTrainLineByPlanCrossId(String[] crossIdsArray) {
		Map<String, Object> reqMap = Maps.newHashMap();
		reqMap.put("planCrossIds", StringUtil.strSqlIn(crossIdsArray));
		// 删除item
		baseDao.deleteBySql(Constants.DELETE_M_TRAINLINE_ITEM_BY_PLANCROSSID,
				reqMap);
		// 删除主表
		return baseDao.deleteBySql(Constants.DELETE_M_TRAINLINE_BY_PLANCROSSID,
				reqMap);
	}

	/**
	 * 修改滚动状态.
	 * 
	 * @param crossIdsArray
	 *            需要调整的数据ID.
	 * @param type
	 *            类型,1滚动,2停止.
	 * @return
	 */
	public int updateModalRun(String[] crossIdsArray, Integer type) {
		Map<String, Object> reqMap = Maps.newHashMap();
		reqMap.put("is_auto_generate", type);
		reqMap.put("plan_cross_id", StringUtil.strSqlIn(crossIdsArray));
		return runPlanDao.updateIsAutoGenerateByMap(reqMap);
	}

	/**
	 * 根据列车id查询列车时刻表
	 * 
	 * @param planId
	 *            列车id
	 * @return 时刻表
	 */
	public List<Map<String, Object>> findPlanTimeTableByPlanId2(String planId) {
		logger.debug("findPlanTimeTableByPlanId::::");
		return runPlanDao.findPlanTimeTableByPlanId2(planId);
	}

	/**
	 * 根据列车id查询列车信息
	 * 
	 * @param planId
	 *            列车id
	 * @return 列车信息
	 */
	public Map<String, Object> findPlanInfoByPlanId(String planId) {
		logger.debug("findPlanInfoByPlanId::::");
		return runPlanDao.findPlanInfoByPlanId(planId);
	}

	/**
	 * 表结构更改，参数更改 根据列车id查询列车信息
	 * 
	 * @param planId
	 *            列车id
	 * @return 列车信息 suntao
	 */
	public Map<String, Object> findPlanInfoByPlanId2(String planId) {
		logger.debug("findPlanInfoByPlanId2::::");
		return runPlanDao.findPlanInfoByPlanId2(planId);
	}

	/**
	 * 根据列车号和运行时间查询trainplan
	 * 
	 * @param bureau
	 * @param runDate
	 * @return auther suntao
	 */
	public List<PlanTrainDTOForModify> findPlanInfoByNBrAndRunDate(
			String trainNbr, String runDate, String planTrainId,
			String planCrossId) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("trainNbr", trainNbr);
		map.put("runDate", runDate);
		map.put("planTrainId", planTrainId);
		if (StringUtils.isEmpty(planCrossId)) {
			// map.put("planCrossId", null);
		} else {
			map.put("planCrossId", planCrossId);
		}
		return runPlanDao.findPlanInfoByNBrAndRunDate(map);
	}

	/**
	 * 根据列车号和运行时间查询trainplan
	 * 
	 * @param bureau
	 * @param runDate
	 * @return auther suntao
	 */
	public List<PlanTrainDTOForModify> findPlanInfoByNBrAndRunDate1(
			String trainNbr, String runDate, Integer spareFlag) {
		logger.debug("findPlanInfoByBureauAndRunDate::::");
		Map<String, Object> map = Maps.newHashMap();
		map.put("trainNbr", trainNbr);
		map.put("runDate", runDate);
		map.put("spareFlag", spareFlag);
		return runPlanDao.findPlanInfoByNBrAndRunDate(map);
	}

	/**
	 * 根据列车id查询列车信息 通过基本图
	 * 
	 * @param planId
	 *            列车id
	 * @return 列车信息
	 */
	public List<Map<String, Object>> findPlanTimeTableByPlanIdFromjbt(
			String planId) {
		logger.debug("findPlanTimeTableByPlanIdFromjbt::::");
		return runPlanDao.findPlanTimeTableByPlanIdFromjbt(planId);
	}

	/**
	 * 根据列车id查询列车信息 通过基本图
	 * 
	 * @param planId
	 *            列车id
	 * @return 列车信息
	 */
	public List<Map<String, Object>> findPlanTimeTableByPlanIdFromjbt2(
			String planId) {
		logger.debug("findPlanTimeTableByPlanIdFromjbt::::");
		return runPlanDao.findPlanTimeTableByPlanIdFromjbt2(planId);
	}

	/**
	 * 一级审核
	 * 
	 * @param list
	 *            列车列表
	 * @param user
	 *            当前审核
	 * @param checkType
	 *            审核类型
	 * @return 审核结果
	 */
	public List<Map<String, Object>> checkLev1(List<Map<String, Object>> list,
			ShiroRealm.ShiroUser user, int checkType) {
		List<Map<String, Object>> checkLev1Result = Lists.newArrayList();
		try {
			// 准备参数
			java.sql.Date now = new java.sql.Date(new Date().getTime());
			List<LevelCheck> params = Lists.newArrayList();
			List<String> planIdList = Lists.newArrayList();
			for (Map<String, Object> item : list) {
				LevelCheck record = new LevelCheck(
						UUID.randomUUID().toString(), user.getName(), now,
						user.getDeptName(), user.getBureau(), checkType,
						MapUtils.getString(item, "planId"), MapUtils.getString(
								item, "lineId"));
				params.add(record);
				planIdList.add(record.getPlanId());
			}
			if (planIdList.size() > 0) {
				// 增加审核记录
				runPlanDao.addCheckHis(params);
				// 修改审核状态和已审核局
				List<Map<String, Object>> planList = runPlanDao
						.findPlanInfoListByPlanId(planIdList);

				for (Map<String, Object> plan : planList) {

					try {
						// 组织返回结果对象
						Map<String, Object> levResult = Maps.newHashMap();
						levResult.put("id",
								MapUtils.getString(plan, "PLAN_TRAIN_ID"));

						int lev1Type = MapUtils.getIntValue(plan,
								"CHECK_LEV1_TYPE");
						String lev1Bureau = MapUtils.getString(plan,
								"CHECK_LEV1_BUREAU", "");
						// int lev2Type = MapUtils.getIntValue(plan,
						// "CHECK_LEV2_TYPE");
						// String lev2Bureau = MapUtils.getString(plan,
						// "CHECK_LEV2_BUREAU", "");
						String passBureau = MapUtils.getString(plan,
								"PASS_BUREAU", "");
						// 计算一级已审核局
						String checkedLev1Bureau = addBureauCode(lev1Bureau,
								user.getBureauShortName());
						String dailyplan_check_bureau = addBureauCode(
								MapUtils.getString(plan,
										"DAILYPLAN_CHECK_BUREAU", ""),
								user.getBureauShortName());
						// 计算新1级审核状态
						int newLev1Type = newLev1Type(lev1Type, passBureau,
								lev1Bureau, user.getBureauShortName());
						// 一级审核完后，二级审核需要重新审核
						int newLev2Type = 0;
						// 二级已审核局也应该是空
						String checkedLev2Bureau = "";
						Map<String, Object> updateParam = Maps.newHashMap();
						updateParam.put("lev1Type", newLev1Type);
						updateParam.put("lev1Bureau", checkedLev1Bureau);
						updateParam.put("lev2Type", newLev2Type);
						updateParam.put("lev2Bureau", checkedLev2Bureau);
						updateParam.put("planId",
								MapUtils.getString(plan, "PLAN_TRAIN_ID"));
						updateParam.put("dailyplan_check_bureau",
								dailyplan_check_bureau);
						runPlanDao.updateCheckInfo(updateParam);
						// 本局一级审核状态
						levResult.put("checkLev1", newLev1Type);
						// 本局二级审核状态
						levResult.put("checkLev2", newLev2Type);
						// 本局一级审核是否已审核
						levResult.put("lev1Checked", 1);
						// 本局二级审核是否已审核
						levResult.put("lev2Checked", 0);
						checkLev1Result.add(levResult);
					} catch (DailyPlanCheckException e) {
						logger.error(e);
					}
				}
			}

		} catch (Exception e) {
			logger.error("checkLev1::::::", e);
		}
		return checkLev1Result;
	}

	/**
	 * 二级审核
	 * 
	 * @param plans
	 *            列车列表
	 * @param user
	 *            当前审核
	 * @param checkType
	 *            审核类型
	 * @return 审核结果
	 */
	public List<Map<String, Object>> checkLev2(List<Map<String, Object>> plans,
			ShiroRealm.ShiroUser user, int checkType) {
		List<Map<String, Object>> checkLev1Result = Lists.newArrayList();
		try {
			// 准备参数
			java.sql.Date now = new java.sql.Date(new Date().getTime());
			List<LevelCheck> params = Lists.newArrayList();
			List<String> planIdList = Lists.newArrayList();
			for (Map<String, Object> item : plans) {
				LevelCheck record = new LevelCheck(
						UUID.randomUUID().toString(), user.getName(), now,
						user.getDeptName(), user.getBureau(), checkType,
						MapUtils.getString(item, "planId"), MapUtils.getString(
								item, "lineId"));
				params.add(record);
				planIdList.add(record.getPlanId());
			}
			if (planIdList.size() > 0) {
				// 增加审核记录
				runPlanDao.addCheckHis(params);
				// 修改审核状态和已审核局
				List<Map<String, Object>> planList = runPlanDao
						.findPlanInfoListByPlanId(planIdList);

				for (Map<String, Object> plan : planList) {

					try {
						// 组织返回结果对象
						Map<String, Object> levResult = Maps.newHashMap();
						levResult.put("id",
								MapUtils.getString(plan, "PLAN_TRAIN_ID"));

						int lev1Type = MapUtils.getIntValue(plan,
								"CHECK_LEV1_TYPE");
						String lev1Bureau = MapUtils.getString(plan,
								"CHECK_LEV1_BUREAU", "");
						int lev2Type = MapUtils.getIntValue(plan,
								"CHECK_LEV2_TYPE");
						String lev2Bureau = MapUtils.getString(plan,
								"CHECK_LEV2_BUREAU", "");
						String passBureau = MapUtils.getString(plan,
								"PASS_BUREAU");
						// 计算二级已审核局
						String checkedLev2Bureau = addBureauCode(lev2Bureau,
								user.getBureau());
						// 计算新1级审核状态
						int newLev2Type = newLev1Type(lev2Type, passBureau,
								lev2Bureau, user.getBureauShortName());
						// 一级审核完后，二级审核需要重新审核
						// int newLev2Type = 0;
						// // 二级已审核局也应该是空
						// String checkedLev2Bureau = "";
						Map<String, Object> updateParam = Maps.newHashMap();
						updateParam.put("lev1Type", lev1Type);
						updateParam.put("lev1Bureau", lev1Bureau);
						updateParam.put("lev2Type", newLev2Type);
						updateParam.put("lev2Bureau", checkedLev2Bureau);
						updateParam.put("planId",
								MapUtils.getString(plan, "PLAN_TRAIN_ID"));
						runPlanDao.updateCheckInfo(updateParam);
						// 本局一级审核状态
						// levResult.put("checkLev1", newLev1Type);
						// 本局二级审核状态
						levResult.put("checkLev2", newLev2Type);
						// 本局一级审核是否已审核
						// levResult.put("lev1Checked", 1);
						// 本局二级审核是否已审核
						levResult.put("lev2Checked", 1);
						checkLev1Result.add(levResult);
					} catch (DailyPlanCheckException e) {
						logger.error(e);
					}
				}
			}

		} catch (Exception e) {
			logger.error("checkLev1::::::", e);
		}
		return checkLev1Result;
	}

	/**
	 * 路局审核了需要修改已审核路局局码字段
	 * 
	 * @param base
	 *            已有局
	 * @param split
	 *            新增局
	 * @return 最终字符
	 */
	private String addBureauCode(String base, String split)
			throws WrongDataException {
		StringBuilder result = new StringBuilder(base);
		if (!base.contains(split)) {
			result.append(split);
		} else {
			throw new WrongDataException(split + " 局已审核过该计划，不能重复审核");
		}
		return result.toString();
	}

	/**
	 * 计算新一级审核状态
	 * 
	 * @param current
	 *            当前状态
	 * @param passBureau
	 *            经由局
	 * @param currentChecked
	 *            当前已审核局
	 * @param split
	 *            当前局
	 * @return 新状态
	 * @throws DailyPlanCheckException
	 */
	private int newLev1Type(int current, String passBureau,
			String currentChecked, String split) throws DailyPlanCheckException {
		int result;
		if (!passBureau.contains(split)) {
			throw new WrongBureauCheckException("计划不属于审核局");
		}
		if (current == 2) {
			throw new WrongCheckTypeException("该计划已经审核过");
		}
		if (current == 1 || current == 0) {
			result = computeLev1Type(current, passBureau,
					addBureauCode(currentChecked, split));
		} else {
			throw new UnknownCheckTypeException("未知审核类型");
		}
		return result;
	}

	/**
	 * 是否已审核完
	 * 
	 * @param current
	 *            当前审核状态
	 * @param passBureau
	 *            经由局
	 * @param checkedBureau
	 *            已审核局
	 * @return 审核状态
	 * @throws WrongDataException
	 */
	private int computeLev1Type(int current, String passBureau,
			String checkedBureau) throws WrongDataException {
		if (current != 2 && isAllChecked(passBureau, checkedBureau)) {
			return 2;
		}
		if (current == 1 && !isAllChecked(passBureau, checkedBureau)) {
			return 1;
		}
		if (current == 2 && isAllChecked(passBureau, checkedBureau)) {
			throw new WrongDataException("该计划已审核完毕，不能再次审核");
		}
		if (current != 2 && passBureau.contains(checkedBureau)) {
			return 1;
		}
		return 0;
	}

	/**
	 * 判断是否所有路局都审核完毕
	 * 
	 * @param standard
	 *            判断标准
	 * @param token
	 *            需要判断的字符
	 * @return 是否全部包含
	 */
	private boolean isAllChecked(String standard, String token)
			throws WrongDataException {
		StringBuilder tStandard = new StringBuilder(standard);
		if (standard.length() != token.length()) {
			return false;
		}
		if (standard.length() < token.length()) {
			throw new WrongDataException("已审核路局比途经局多，请联系管理员");
		}
		while (tStandard.length() > 0) {
			String ele = tStandard.substring(0, 1);
			tStandard = new StringBuilder(tStandard.substring(1));
			if (!token.contains(ele)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<RunPlanTrainDto> getTrainRunPlans(Map<String, Object> map)
			throws Exception {
		List<RunPlanTrainDto> runPlans = Lists.newArrayList();
		// Map<String, RunPlanTrainDto> runPlanTrainMap = Maps.newHashMap();
		/*
		 * List<CrossRunPlanInfo> crossRunPlans =
		 * baseDao.selectListBySql(Constants.GET_TRAIN_RUN_PLAN, map); String
		 * startDay = map.get("startDay").toString(); String endDay =
		 * map.get("endDay").toString(); for(CrossRunPlanInfo runPlan:
		 * crossRunPlans){ RunPlanTrainDto currTrain =
		 * runPlanTrainMap.get(runPlan.getTrainNbr()); if(currTrain == null){
		 * currTrain = new RunPlanTrainDto(startDay, endDay);
		 * currTrain.setTrainNbr(runPlan.getTrainNbr());
		 * runPlanTrainMap.put(runPlan.getTrainNbr(), currTrain); }
		 * currTrain.setRunFlag(runPlan.getRunDay(), runPlan.getRunFlag()); }
		 * runPlans.addAll(runPlanTrainMap.values());
		 */
		List<RunPlanTrainDto> crossRunPlans = baseDao.selectListBySql(
				Constants.GET_TRAIN_RUN_PLAN, map);
		String startDay = map.get("startDay").toString();
		String endDay = map.get("endDay").toString();
		String trainNbr = "";
		List<TrainRunDto> tempSubList = new LinkedList<TrainRunDto>();
		RunPlanTrainDto currTrain = null;
		// for(RunPlanTrainDto runPlan : crossRunPlans){
		for (int j = 0; j < crossRunPlans.size(); j++) {
			RunPlanTrainDto runPlan = crossRunPlans.get(j);
			// 比对不匹配,进行各种赋值.
			if (!trainNbr.equals(runPlan.getTrainNbr())) {
				if (j != 0) {
					runPlans.add(currTrain);
				}
				trainNbr = runPlan.getTrainNbr();
				currTrain = new RunPlanTrainDto(startDay, endDay, null, null,
						null);
				tempSubList = currTrain.getRunPlans();
				currTrain.setTrainNbr(runPlan.getTrainNbr());
			}
			List<TrainRunDto> list = runPlan.getRunPlans();
			for (TrainRunDto dto : list) {
				String day = dto.getDay();
				String runFlag = dto.getRunFlag();
				String telName = dto.getTelName();
				String planTrainId = dto.getPlanTrainId();
				for (TrainRunDto tempDto : tempSubList) {
					if (day.equals(tempDto.getDay())) {
						tempDto.setRunFlag(runFlag);
						tempDto.setTelName(telName);
						tempDto.setPlanTrainId(planTrainId);
						break;
					}
				}
			}
			if (j == crossRunPlans.size() - 1) {
				runPlans.add(currTrain);
			}
		}
		return runPlans;
	}

	/**
	 * 开行情况.
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<RunPlanTrainDto> getTrainRunPlans1(Map<String, Object> map)
			throws Exception {

		List<RunPlanTrainDto> runPlans = Lists.newArrayList();

		String cnMap = planCrossDao.getCorssNameById(MapUtils.getString(map,
				"planCrossId"));
		if (StringUtils.isNotEmpty(cnMap)) {
			boolean cf = StringUtil.strIsChongFu1(cnMap.split("-"));
			if (cf) {
				// 有重复的车次名称.
				// System.out.println("有重复的车次名称");
				runPlans = isChongfu(map, cnMap);
			} else {
				// 没有重复的车次名称.
				// System.out.println("没有重复的车次名称");
				runPlans = isNotChongfu(map);
			}
		}

		return runPlans;
	}

	@SuppressWarnings("unchecked")
	public List<PlanCrossDto> getPlanCross(Map<String, Object> reqMap) {

		return baseDao.selectListBySql(Constants.GET_PLAN_CROSS, reqMap);
	}

	// getCheckPlan

	// @SuppressWarnings("unchecked")
	// public List<PlanCrossDto> getCheckPlan(Map<String, Object> reqMap) {
	//
	// return baseDao.selectListBySql(Constants.GET_PLAN_CHECK, reqMap);
	// }
	/**
	 *
	 * @param baseChartId
	 *            基本图id
	 * @param startDate
	 *            yyyy-MM-dd
	 * @param days
	 *            比如:30
	 * @return 生成了多少个plancross的计划
	 */
	public List<String> generateRunPlan(String baseChartId, String startDate,
			int days, List<String> crossNames, String msgReceiveUrl) {
		return generateRunPlan(baseChartId, startDate, days, crossNames,
				msgReceiveUrl, 0, null);
	}

	public List<String> generateRunPlan(String baseChartId, String startDate,
			int days, List<String> crossNames, String msgReceiveUrl,
			int createType, String tokenVehBureau) {
		ExecutorService executorService = Executors
				.newFixedThreadPool(threadNbr);
		ExecutorService singleExecutorService = Executors
				.newSingleThreadExecutor();
		Map<String, Object> params = Maps.newHashMap();
		params.put("baseChartId", baseChartId);
		// params.put("unitCrossIds", unitCrossIds);
		params.put("crossNames", crossNames);

		if (null == tokenVehBureau) {
			try {
				ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
						.getSubject().getPrincipal();

				params.put("tokenVehBureau", user.getBureau());
			} catch (Exception e) {
				logger.error("获取用户信息失败： " + e);
				return null;
			}

		} else {
			params.put("tokenVehBureau", tokenVehBureau);
		}

		/** added by liuhang 解决页面需要点击两次才能生成的情况 ****/
		/**********************/

		List<CreateRunPlanBySpecialRules> createRunPlanBySpecialRulesList = new ArrayList<CreateRunPlanBySpecialRules>();

		List<UnitCross> unitCrossList = unitCrossDao
				.findUnitCrossBySchemaId(params);
		for (UnitCross unitCross : unitCrossList) {
			boolean isAppointWeek = false;
			boolean isAppointDay = false;
			if (null != unitCross.getAppointWeek()
					&& !"null".equals(unitCross.getAppointWeek())
					&& !"".equals(unitCross.getAppointWeek()))
				isAppointWeek = true;
			if (null != unitCross.getAppointDay()
					&& !"null".equals(unitCross.getAppointDay())
					&& !"".equals(unitCross.getAppointDay()))
				isAppointDay = true;

			CreateRunPlanBySpecialRules createRunPlanBySpecialRules = new CreateRunPlanBySpecialRules();
			// 进入星期规律
			if (isAppointWeek) {

				// 判断这个星期规律能否生成开行计划
				if (!runPlanCreateAuxiliaryService
						.isGenerationByWeek(unitCross)) {
					try {
						pubSendUnitCrossMsg(unitCross.getUnitCrossId(), -2,
								msgReceiveUrl);
						break;
					} catch (JsonProcessingException e1) {
						logger.debug("生成开行计划星期规律数据错误: " + e1);
						e1.printStackTrace();
						break;
					}
				}

				try {
					createRunPlanBySpecialRules
							.setRuleType(Constants.RULETYPE.WEEK_RULE.getMsg());
					createRunPlanBySpecialRules.setUnitCross(unitCross);
					createRunPlanBySpecialRules
							.setCreateRunLineDates(runPlanCreateAuxiliaryService
									.createRunPlanByAppointWeek(startDate,
											days, unitCross.getAppointWeek(),
											unitCross));
					createRunPlanBySpecialRules.setState("TS");
				} catch (ParseException e) {
					e.printStackTrace();
					createRunPlanBySpecialRules.setUnitCross(unitCross);
					createRunPlanBySpecialRules.setCreateRunLineDates(null);
					createRunPlanBySpecialRules.setState("TS");
				}
			}
			// 进入日期规律
			else if (isAppointDay) {

				// 判断这个日期规律能否生成开行计划
				// if(!runPlanCreateAuxiliaryService.isGenerationByWeek(unitCross))
				// {
				// try {
				// //-3对应前端的异常显示
				// pubSendUnitCrossMsg(unitCross.getUnitCrossId(), -3,
				// msgReceiveUrl);
				// break;
				// } catch (JsonProcessingException e1) {
				// logger.debug("生成开行计划日期规律数据错误: " + e1);
				// e1.printStackTrace();
				// break;
				// }
				// }

				try {
					createRunPlanBySpecialRules
							.setRuleType(Constants.RULETYPE.DATE_RULE.getMsg());
					createRunPlanBySpecialRules.setUnitCross(unitCross);
					createRunPlanBySpecialRules
							.setCreateRunLineDates(runPlanCreateAuxiliaryService
									.createRunPlanByAppointDay(startDate, days,
											unitCross.getAppointDay(),
											unitCross));
					createRunPlanBySpecialRules.setState("TS");
				} catch (Exception e) {
					e.printStackTrace();
					createRunPlanBySpecialRules.setUnitCross(unitCross);
					createRunPlanBySpecialRules.setCreateRunLineDates(null);
					createRunPlanBySpecialRules.setState("TS");
				}

			} else {
				createRunPlanBySpecialRules.setUnitCross(unitCross);
				createRunPlanBySpecialRules.setCreateRunLineDates(null);
			}
			createRunPlanBySpecialRulesList.add(createRunPlanBySpecialRules);
		}

		List<String> unitCrossIdList = Lists.newArrayList();
		try {
			for (CreateRunPlanBySpecialRules createRunPlanBySpecialRules : createRunPlanBySpecialRulesList) {
				UnitCross unitCross = createRunPlanBySpecialRules
						.getUnitCross();
				if (null != createRunPlanBySpecialRules.getCreateRunLineDates()
						&& !createRunPlanBySpecialRules.getCreateRunLineDates()
								.isEmpty()) {
					if (null == unitCross.getPlanCrossId()) {
						unitCross.setPlanCrossId(UUID.randomUUID().toString());
					}
					for (int i = 0; i < createRunPlanBySpecialRules
							.getCreateRunLineDates().size(); i++) {
						CreateRunLineDate createRunLineDate = createRunPlanBySpecialRules
								.getCreateRunLineDates().get(i);
						if (i == createRunPlanBySpecialRules
								.getCreateRunLineDates().size() - 1) {
							// singleExecutorService.execute(new
							// RunPlanGenerator(unitCross,
							// createRunLineDate.getStartDate(),
							// createRunLineDate.getDays() - 1, msgReceiveUrl,
							// true));
							singleExecutorService
									.execute(new RunServiceRunnable(unitCross,
											createRunLineDate.getStartDate(),
											createRunLineDate.getDays() - 1,
											msgReceiveUrl, true,
											runPlanService2, createType, params
													.get("tokenVehBureau")
													.toString()));
						} else {
							// singleExecutorService.execute(new
							// RunPlanGenerator(unitCross,
							// createRunLineDate.getStartDate(),
							// createRunLineDate.getDays() - 1, msgReceiveUrl,
							// false));
							singleExecutorService
									.execute(new RunServiceRunnable(unitCross,
											createRunLineDate.getStartDate(),
											createRunLineDate.getDays() - 1,
											msgReceiveUrl, false,
											runPlanService2, createType, params
													.get("tokenVehBureau")
													.toString()));
						}
					}
				} else {
					// executorService.execute(new RunPlanGenerator(unitCross,
					// startDate, days - 1, msgReceiveUrl));
					if ("TS".equals(createRunPlanBySpecialRules.getState())) {
						pubSendUnitCrossMsg(unitCross.getUnitCrossId(), -1,
								msgReceiveUrl);
					} else {
						executorService.execute(new RunServiceRunnable(
								unitCross, startDate, days - 1, msgReceiveUrl,
								runPlanService2, createType, params.get(
										"tokenVehBureau").toString()));
					}

					// CreateRunLineDates()为null的时候，不应该生成开行计划

				}

				unitCrossIdList.add(unitCross.getUnitCrossId());
			}

			// for(UnitCross unitCross: unitCrossList) {
			// executorService.execute(new RunPlanGenerator(unitCross,
			// startDate, days - 1, msgReceiveUrl));
			// unitCrossIdList.add(unitCross.getUnitCrossId());
			// }
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} finally {
			singleExecutorService.shutdown();
			executorService.shutdown();
		}
		return unitCrossIdList;
	}

	class RunPlanGenerator implements Runnable {

		// 传入参数
		private UnitCross unitCross;

		private String startDate;

		private int days;

		private String msgReceiveUrl;

		private boolean specialRuleIsSendMsg = false;

		private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		RunPlanGenerator(UnitCross unitCross, String startDate, int days,
				String msgReceiveUrl, boolean specialRuleIsSendMsg) {

			this.unitCross = unitCross;
			this.startDate = startDate;
			this.days = days;
			this.specialRuleIsSendMsg = specialRuleIsSendMsg;
			this.msgReceiveUrl = msgReceiveUrl;
		}

		RunPlanGenerator(UnitCross unitCross, String startDate, int days,
				String msgReceiveUrl) {

			this.unitCross = unitCross;
			this.startDate = startDate;
			this.days = days;
			this.msgReceiveUrl = msgReceiveUrl;
		}

		@Override
		@Transactional
		@Monitored
		public void run() {
			logger.debug("thread start:" + LocalTime.now().toString("hh:mm:ss"));
			try {
				// 查询同名交路，用来补全时间空挡，只查询生成过计划的交路（只有同一个基本图的交路才套用）
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put("unitCrossName", this.unitCross.getCrossName());
				reqMap.put("baseChartId", this.unitCross.getBaseChartId());
				List<PlanCrossInfo> planCrossInfoList = planCrossDao
						.findByParamMap(reqMap);

				// 按启用时间排序
				Collections.sort(planCrossInfoList,
						new Comparator<PlanCrossInfo>() {
							@Override
							public int compare(PlanCrossInfo o1,
									PlanCrossInfo o2) {
								LocalDate date1 = DateTimeFormat.forPattern(
										"yyyyMMdd").parseLocalDate(
										o1.getCrossEndDate());
								LocalDate date2 = DateTimeFormat.forPattern(
										"yyyyMMdd").parseLocalDate(
										o2.getCrossEndDate());
								if (date1.compareTo(date2) != 0) {
									return date1.compareTo(date2);
								} else {
									date1 = DateTimeFormat.forPattern(
											"yyyyMMdd").parseLocalDate(
											o1.getCrossStartDate());
									date2 = DateTimeFormat.forPattern(
											"yyyyMMdd").parseLocalDate(
											o2.getCrossStartDate());
									return date1.compareTo(date2);
								}
							}
						});
				/******** 注释了以下代码，为解决生成交路计划的时候给界面推了两次信息 *************/

				// if(!(null != unitCross.getAppointWeek() &&
				// !"null".equals(unitCross.getAppointWeek()) &&
				// !"".equals(unitCross.getAppointWeek()))) {
				// // 已存在的最新的交路不是当前要生成计划的交路，则先补齐已存在交路(暂时取消该功能)
				// if(planCrossInfoList.size() > 0 &&
				// !planCrossInfoList.get(planCrossInfoList.size() -
				// 1).getUnitCrossId().equals(this.unitCross.getUnitCrossId()))
				// {
				// PlanCrossInfo planCrossInfo =
				// planCrossInfoList.get(planCrossInfoList.size() - 1);
				// //UnitCross unitCross =
				// unitCrossDao.findById(planCrossInfo.getUnitCrossId());
				// UnitCross unitCross =
				// unitCrossDao.findByName(planCrossInfo.getCrossName());
				// logger.error("576 unitCrossID ~~" +
				// unitCross.getUnitCrossId());
				// logger.error("576 unitCrossName ~~" +
				// unitCross.getCrossName());
				// logger.error("576 unitCrossTrainList ~~" +
				// unitCross.getUnitCrossTrainList().size());
				// generateRunPlan(this.startDate, 0, unitCross, false);
				// }
				// }
				// 生成这次请求的计划
				/********  *************/
				generateRunPlan(this.startDate, this.days, this.unitCross, true);
			} catch (WrongDataException e) {
				logger.error(
						"数据错误：unitCross_id = "
								+ this.unitCross.getUnitCrossId(), e);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(
						"生成计划失败：unitCross_id = "
								+ this.unitCross.getUnitCrossId(), e);
			}
			logger.debug("thread end:" + LocalTime.now().toString("hh:mm:ss"));
		}

		/**
		 * 生成开行计划，可以重复生成。 原则：保证生成的计划是在一个连续的时间区间内。
		 * 
		 * @param startDateStr
		 *            起始日期
		 * @throws WrongDataException
		 * @throws Exception
		 */
		private void generateRunPlan(String startDateStr, int days,
				UnitCross unitCross, boolean sendMsg)
				throws WrongDataException, Exception {
			LocalDate startDate = DateTimeFormat.forPattern("yyyyMMdd")
					.parseLocalDate(startDateStr);
			// 生成plan_cross逻辑
			// 只要生成了第一开行计划，这个时候unitCross中的planCrossId就不该为空，但是按星期规律生成的时候，
			// 这是一个连贯操作，unitCross中的planCrossId没有被重新赋值,所以这样操作是错误的
			String planCrossId = unitCross.getPlanCrossId();

			/*** 给界面推送一个交路开始的信息 added by liuhang ***/
			if (sendMsg) {
				sendUnitCrossMsg(unitCross.getUnitCrossId(), 1);
			}
			/*************************/
			PlanCrossInfo planCrossInfo;
			// 按组别保存最后一个计划
			Map<Integer, RunPlan> lastRunPlans = Maps.newHashMap();
			if (planCrossId == null) { // 之前未生成过开行计划
				planCrossInfo = new PlanCrossInfo();
				BeanUtils.copyProperties(planCrossInfo, unitCross);
				planCrossInfo.setPlanCrossId(UUID.randomUUID().toString());
				planCrossInfo.setCrossStartDate(startDateStr);
				planCrossId = planCrossInfo.getPlanCrossId();
				// planCrossInfo.setThroughline(unitCross.getThroughline());
				// logger.info("Throughline ~~~ " + unitCross.getThroughline());
				planCrossDao.save(planCrossInfo);
				lastRunPlans = this.getLastRunPlans(startDateStr, unitCross,
						true);
			} else if (null != unitCross.getAppointWeek()
					&& !"null".equals(unitCross.getAppointWeek())
					&& !"".equals(unitCross.getAppointWeek())) {
				// 按星期生成，特殊处理
				planCrossInfo = crossService
						.getPlanCrossInfoForPlanCrossId(planCrossId);
				if (null != planCrossInfo) {
					planCrossInfo.setThroughLine(unitCross.getThroughLine());
					// 查询每组车最新的计划，作为新计划的前序车
					lastRunPlans = this
							.getLastRunPlans(startDateStr, unitCross);
				} else {
					planCrossInfo = new PlanCrossInfo();
					BeanUtils.copyProperties(planCrossInfo, unitCross);
					planCrossInfo.setPlanCrossId(unitCross.getPlanCrossId());
					planCrossInfo.setCrossStartDate(startDateStr);
					planCrossId = planCrossInfo.getPlanCrossId();
					// planCrossInfo.setThroughline(unitCross.getThroughline());
					// logger.info("Throughline ~~~ " +
					// unitCross.getThroughline());
					planCrossDao.save(planCrossInfo);
					lastRunPlans = this.getLastRunPlans(startDateStr,
							unitCross, true);
				}

			} else {
				// 按时间删除已存在的开行计划，后面重新生成
				// 查询planCross对象，生成完开行计划后需要更新crossEndDate
				planCrossInfo = crossService
						.getPlanCrossInfoForPlanCrossId(planCrossId);
				planCrossInfo.setThroughLine(unitCross.getThroughLine());
				// 查询每组车最新的计划，作为新计划的前序车
				lastRunPlans = this.getLastRunPlans(startDateStr, unitCross);
			}
			// 用来保存最后一个交路起点
			RunPlan lastStartPoint = null;
			final List<UnitCrossTrain> unitCrossTrainList = unitCross
					.getUnitCrossTrainList();
			// 交路使用的车
			List<RunPlan> baseRunPlanList = findBaseTrainByCrossName(unitCross
					.getCrossName());

			int totalGroupNbr = unitCross.getGroupTotalNbr();
			// 计算每组有几个车次
			if (unitCrossTrainList.size() % totalGroupNbr != 0) {
				throw new WrongDataException("交路数据错误，每组车数量不一样");
			}
			// 每组有几个车
			int trainCount = unitCrossTrainList.size() / totalGroupNbr;
			// 计算结束时间
			LocalDate lastDate = startDate.plusDays(days);
			// 记录daygap
			int totalDayGap = 0;

			if (!(null != unitCross.getAppointWeek()
					&& !"null".equals(unitCross.getAppointWeek()) && !""
						.equals(unitCross.getAppointWeek()))) {
				// 一刀切后可能存在不完整交路，先补全不完整的交路
				lastRunPlans = completeUnitCross(lastRunPlans, unitCross);
			}

			// lastRunPlans = completeUnitCross(lastRunPlans, unitCross);

			// 找到最先结束的计划车组，然后继续生成下去
			int firstGroup = getFirstEndedGroup(lastRunPlans);
			// 为什么要-1 ?
			// int initTrainSort = (firstGroup - 1) * trainCount;

			int initTrainSort = firstGroup * trainCount;

			// 在生成开行计划时，开行时间是否增加组间隔天数
			boolean isAddtotalDayGap = false;
			if (0 == firstGroup
					|| 0 == initTrainSort % unitCrossTrainList.size()) {
				isAddtotalDayGap = true;
			}
			// 继续生成
			generate: {
				// logger.error("654 unitCrossName ~~" +
				// unitCross.getCrossName());
				// logger.error("654 unitCrossTrainList ~~" +
				// unitCross.getUnitCrossTrainList().size());
				for (int i = initTrainSort; i < 10000; i++) {
					// logger.error("657 unitCrossTrainList i~~" + i);
					UnitCrossTrain unitCrossTrain = unitCrossTrainList.get(i
							% unitCrossTrainList.size());
					LocalDate runDate = DateTimeFormat.forPattern("yyyyMMdd")
							.parseLocalDate(unitCrossTrain.getRunDate());
					LocalDate endDate = DateTimeFormat.forPattern("yyyyMMdd")
							.parseLocalDate(unitCrossTrain.getEndDate());
					int interval = Days.daysBetween(runDate, endDate).getDays();

					for (RunPlan baseRunPlan : baseRunPlanList) {
						if (unitCrossTrain.getBaseTrainId().equals(
								baseRunPlan.getBaseTrainId())) {
							try {
								RunPlan runPlan = (RunPlan) BeanUtils
										.cloneBean(baseRunPlan);
								// 克隆的对象里，列表没有克隆，重新new一个列表
								runPlan.setRunPlanStnList(new ArrayList<RunPlanStn>());

								// 基本信息
								runPlan.setPlanTrainId(UUID.randomUUID()
										.toString());
								runPlan.setPlanCrossId(planCrossId);
								runPlan.setBaseChartId(baseRunPlan
										.getBaseChartId());
								runPlan.setBaseTrainId(baseRunPlan
										.getBaseTrainId());
								runPlan.setDailyPlanFlag(1);

								// unitcross里的信息
								runPlan.setGroupSerialNbr(unitCrossTrain
										.getGroupSerialNbr());
								runPlan.setTrainSort(unitCrossTrain
										.getTrainSort());
								runPlan.setMarshallingName(unitCrossTrain
										.getMarshallingName());
								runPlan.setTrainNbr(unitCrossTrain
										.getTrainNbr());
								runPlan.setDayGap(unitCrossTrain.getDayGap());
								runPlan.setSpareFlag(unitCrossTrain
										.getSpareFlag());
								runPlan.setSpareApplyFlag(unitCrossTrain
										.getSpareApplyFlag());
								runPlan.setHighLineFlag(unitCrossTrain
										.getHighLineFlag());
								runPlan.setHightLineRule(unitCrossTrain
										.getHighLineFlag());
								runPlan.setCommonLineRule(unitCrossTrain
										.getCommonLineRule());
								runPlan.setAppointWeek(unitCrossTrain
										.getAppointWeek());
								runPlan.setAppointDay(unitCrossTrain
										.getAppointDay());

								RunPlan preRunPlan = lastRunPlans.get(runPlan
										.getGroupSerialNbr());
								if (preRunPlan != null) {
									// 有前序车
									// 当前列车的开始日期，是前车的结束日期+daygap
									LocalDate preEndDate = LocalDate
											.fromDateFields(new Date(preRunPlan
													.getEndDateTime().getTime()));
									runPlan.setRunDate(preEndDate.plusDays(
											unitCrossTrain.getDayGap())
											.toString("yyyyMMdd"));

									// 前后车互基
									runPlan.setPreTrainId(preRunPlan
											.getPlanTrainId());
									preRunPlan.setNextTrainId(runPlan
											.getPlanTrainId());
								} else if (i == 0) { // 第一组第一个车
									LocalDate unitCrossTrainStartDate = DateTimeFormat
											.forPattern("yyyyMMdd")
											.parseLocalDate(
													unitCrossTrain.getRunDate());
									int initInterval = Days.daysBetween(
											unitCrossTrainStartDate, startDate)
											.getDays();
									runPlan.setRunDate(unitCrossTrainStartDate
											.plusDays(initInterval).toString(
													"yyyyMMdd"));
								} else { // 每组第一个车（除了第一组）
									/**
									 * 当截断过原开行计划后，新生成的开行计划以startDate为准，
									 * 所以这个时候开行的第一个 车的开行日期，不能去加上组与组之间的间隔天数。
									 */

									LocalDate unitCrossTrainStartDate = DateTimeFormat
											.forPattern("yyyyMMdd")
											.parseLocalDate(
													unitCrossTrain.getRunDate());
									int initInterval = Days.daysBetween(
											unitCrossTrainStartDate, startDate)
											.getDays();

									// 0 == initInterval
									// 说明startDate和车次的开行时间一样，说明非第一组车第一次开行的时间与交路开行计划开行时间相同
									if (0 != initInterval || isAddtotalDayGap) {
										totalDayGap += unitCrossTrain
												.getGroupGap();
									}
									runPlan.setRunDate(unitCrossTrainStartDate
											.plusDays(initInterval)
											.plusDays(totalDayGap)
											.toString("yyyyMMdd"));
								}

								runPlan.setStartDateTime(new Timestamp(
										simpleDateFormat
												.parse(DateTimeFormat
														.forPattern("yyyyMMdd")
														.parseLocalDate(
																runPlan.getRunDate())
														.toString("yyyy-MM-dd")
														+ " "
														+ runPlan
																.getStartTimeStr())
												.getTime()));
								runPlan.setEndDateTime(new Timestamp(
										simpleDateFormat
												.parse(DateTimeFormat
														.forPattern("yyyyMMdd")
														.parseLocalDate(
																runPlan.getRunDate())
														.plusDays(interval)
														.toString("yyyy-MM-dd")
														+ " "
														+ runPlan
																.getEndTimeStr())
												.getTime()));
								runPlan.setPlanTrainSign(runPlan.getRunDate()
										+ "-" + runPlan.getTrainNbr() + "-"
										+ runPlan.getStartStn() + "-"
										+ runPlan.getStartTimeStr());

								LocalDate runPlanDate = DateTimeFormat
										.forPattern("yyyyMMdd").parseLocalDate(
												runPlan.getRunDate());
								// 计算计划从表信息
								List<RunPlanStn> runPlanStnList = baseRunPlan
										.getRunPlanStnList();
								for (RunPlanStn baseRunPlanStn : runPlanStnList) {
									RunPlanStn runPlanStn = (RunPlanStn) BeanUtils
											.cloneBean(baseRunPlanStn);
									runPlan.getRunPlanStnList().add(runPlanStn);
									runPlanStn.setPlanTrainId(runPlan
											.getPlanTrainId());
									runPlanStn.setPlanTrainStnId(UUID
											.randomUUID().toString());
									runPlanStn
											.setArrTime(new Timestamp(
													simpleDateFormat
															.parse(runPlanDate
																	.plusDays(
																			runPlanStn
																					.getsRunDays())
																	.toString()
																	+ " "
																	+ runPlanStn
																			.getArrTimeStr())
															.getTime()));
									runPlanStn
											.setDptTime(new Timestamp(
													simpleDateFormat
															.parse(runPlanDate
																	.plusDays(
																			runPlanStn
																					.gettRunDays())
																	.toString()
																	+ " "
																	+ runPlanStn
																			.getDptTimeStr())
															.getTime()));
									runPlanStn.setBaseArrTime(runPlanStn
											.getArrTime());
									runPlanStn.setBaseDptTime(runPlanStn
											.getDptTime());
								}

								// 保存当前处理组的第一个车
								if (i % trainCount == 0) {
									lastStartPoint = runPlan;
								}
								// 保存每组车的最后一个车
								lastRunPlans.put(runPlan.getGroupSerialNbr(),
										runPlan);

								runPlanDao.addRunPlan(runPlan);
								// runPlanStnDao.addRunPlanStn(runPlan.getRunPlanStnList());
								runPlanDao.updateNextTrainId(preRunPlan);
								if (sendMsg) {
									sendRunPlanMsg(unitCross.getUnitCrossId(),
											runPlan);
								}
								// 如果有一组车的第一辆车的开始日期到了计划最后日期，就停止生成
								LocalDate lastStartDate = DateTimeFormat
										.forPattern("yyyyMMdd").parseLocalDate(
												lastStartPoint.getRunDate());

								if (((i % trainCount) == (trainCount - 1))
										&& (lastStartDate.compareTo(lastDate) >= 0)) {
									planCrossInfo.setCrossEndDate(LocalDate
											.fromDateFields(
													new Date(runPlan
															.getEndDateTime()
															.getTime()))
											.toString("yyyyMMdd"));
									break generate;
								}
								break;
							} catch (Exception e) {
								logger.error("生成客运计划出错", e);
								sendUnitCrossMsg(unitCross.getUnitCrossId(), -1);
								throw e;
							}
						}
					}

				}
			}
			planCrossDao.update(planCrossInfo);
			if (sendMsg) {
				if (null != unitCross.getAppointWeek()
						&& !"null".equals(unitCross.getAppointWeek())
						&& !"".equals(unitCross.getAppointWeek())) {
					if (specialRuleIsSendMsg) {
						sendUnitCrossMsg(unitCross.getUnitCrossId(), 2);
					}
				} else {
					sendUnitCrossMsg(unitCross.getUnitCrossId(), 2);
				}
			}
		}

		// private List<RunPlan> findBaseTrainByUnitCrossId(String unitCrossId)
		// {
		// Map<String, Object> params = Maps.newHashMap();
		// params.put("unitCrossId", unitCrossId);
		// return baseTrainDao.findBaseTrainByUnitCrossid(params);
		// }

		private List<RunPlan> findBaseTrainByCrossName(String crossName) {
			Map<String, Object> params = Maps.newHashMap();
			params.put("crossName", crossName);
			return baseTrainDao.findBaseTrainByCrossName(params);
		}

		/**
		 * 用传入的交路参数补全不完整的交路计划
		 * 
		 * @param lastRunPlans
		 * @return
		 * @throws NoSuchMethodException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 * @throws ParseException
		 * @throws InvocationTargetException
		 */
		private Map<Integer, RunPlan> completeUnitCross(
				Map<Integer, RunPlan> lastRunPlans, UnitCross unitCross)
				throws NoSuchMethodException, InstantiationException,
				IllegalAccessException, ParseException,
				InvocationTargetException {
			List<RunPlan> baseRunPlanList = findBaseTrainByCrossName(unitCross
					.getCrossName());
			Map<Integer, RunPlan> newMap = Maps.newHashMap(lastRunPlans);
			for (RunPlan runPlan : Lists.newArrayList(lastRunPlans.values())) {
				newMap.put(runPlan.getGroupSerialNbr(),
						generateRunPlan(runPlan, unitCross, baseRunPlanList));
			}
			return newMap;
		}

		private RunPlan generateRunPlan(RunPlan preRunPlan,
				UnitCross unitCross, List<RunPlan> baseRunPlanList)
				throws InvocationTargetException, NoSuchMethodException,
				InstantiationException, ParseException, IllegalAccessException {
			UnitCrossTrain unitCrossTrain;
			RunPlan runPlan = preRunPlan;
			int groupSeriaNbr = preRunPlan.getGroupSerialNbr();
			int trainSort = preRunPlan.getTrainSort();
			while ((unitCrossTrain = getNextUnitCrossTrain(groupSeriaNbr,
					trainSort, unitCross)) != null) {

				LocalDate runDate = DateTimeFormat.forPattern("yyyyMMdd")
						.parseLocalDate(unitCrossTrain.getRunDate());
				LocalDate endDate = DateTimeFormat.forPattern("yyyyMMdd")
						.parseLocalDate(unitCrossTrain.getEndDate());
				int interval = Days.daysBetween(runDate, endDate).getDays();

				for (RunPlan baseRunPlan : baseRunPlanList) {
					if (unitCrossTrain.getBaseTrainId().equals(
							baseRunPlan.getBaseTrainId())) {
						try {
							runPlan = (RunPlan) BeanUtils
									.cloneBean(baseRunPlan);
							// 克隆的对象里，列表没有克隆，重新new一个列表
							runPlan.setRunPlanStnList(new ArrayList<RunPlanStn>());

							// 基本信息
							runPlan.setPlanTrainId(UUID.randomUUID().toString());
							runPlan.setPlanCrossId(unitCross.getPlanCrossId());
							runPlan.setBaseChartId(baseRunPlan.getBaseChartId());
							runPlan.setBaseTrainId(baseRunPlan.getBaseTrainId());
							runPlan.setDailyPlanFlag(1);

							// unitcross里的信息
							runPlan.setGroupSerialNbr(unitCrossTrain
									.getGroupSerialNbr());
							runPlan.setTrainSort(unitCrossTrain.getTrainSort());
							runPlan.setMarshallingName(unitCrossTrain
									.getMarshallingName());
							runPlan.setTrainNbr(unitCrossTrain.getTrainNbr());
							runPlan.setDayGap(unitCrossTrain.getDayGap());
							runPlan.setSpareFlag(unitCrossTrain.getSpareFlag());
							runPlan.setSpareApplyFlag(unitCrossTrain
									.getSpareApplyFlag());
							runPlan.setHighLineFlag(unitCrossTrain
									.getHighLineFlag());
							runPlan.setHightLineRule(unitCrossTrain
									.getHighLineFlag());
							runPlan.setCommonLineRule(unitCrossTrain
									.getCommonLineRule());
							runPlan.setAppointWeek(unitCrossTrain
									.getAppointWeek());
							runPlan.setAppointDay(unitCrossTrain
									.getAppointDay());

							// 当前列车的开始日期，是前车的结束日期+daygap
							LocalDate preEndDate = LocalDate
									.fromDateFields(new Date(preRunPlan
											.getEndDateTime().getTime()));
							runPlan.setRunDate(preEndDate.plusDays(
									unitCrossTrain.getDayGap()).toString(
									"yyyyMMdd"));

							// 前后车互基
							runPlan.setPreTrainId(preRunPlan.getPlanTrainId());
							preRunPlan.setNextTrainId(runPlan.getPlanTrainId());

							runPlan.setStartDateTime(new Timestamp(
									simpleDateFormat
											.parse(DateTimeFormat
													.forPattern("yyyyMMdd")
													.parseLocalDate(
															runPlan.getRunDate())
													.toString("yyyy-MM-dd")
													+ " "
													+ runPlan.getStartTimeStr())
											.getTime()));
							runPlan.setEndDateTime(new Timestamp(
									simpleDateFormat
											.parse(DateTimeFormat
													.forPattern("yyyyMMdd")
													.parseLocalDate(
															runPlan.getRunDate())
													.plusDays(interval)
													.toString("yyyy-MM-dd")
													+ " "
													+ runPlan.getEndTimeStr())
											.getTime()));
							runPlan.setPlanTrainSign(runPlan.getRunDate() + "-"
									+ runPlan.getTrainNbr() + "-"
									+ runPlan.getStartStn() + "-"
									+ runPlan.getStartTimeStr());

							LocalDate runPlanDate = DateTimeFormat.forPattern(
									"yyyyMMdd").parseLocalDate(
									runPlan.getRunDate());
							// 计算计划从表信息
							List<RunPlanStn> runPlanStnList = baseRunPlan
									.getRunPlanStnList();
							for (RunPlanStn baseRunPlanStn : runPlanStnList) {
								RunPlanStn runPlanStn = (RunPlanStn) BeanUtils
										.cloneBean(baseRunPlanStn);
								runPlan.getRunPlanStnList().add(runPlanStn);
								runPlanStn.setPlanTrainId(runPlan
										.getPlanTrainId());
								runPlanStn.setPlanTrainStnId(UUID.randomUUID()
										.toString());
								runPlanStn
										.setArrTime(new Timestamp(
												simpleDateFormat
														.parse(runPlanDate
																.plusDays(
																		runPlanStn
																				.getsRunDays())
																.toString()
																+ " "
																+ runPlanStn
																		.getArrTimeStr())
														.getTime()));
								runPlanStn
										.setDptTime(new Timestamp(
												simpleDateFormat
														.parse(runPlanDate
																.plusDays(
																		runPlanStn
																				.gettRunDays())
																.toString()
																+ " "
																+ runPlanStn
																		.getDptTimeStr())
														.getTime()));
								runPlanStn.setBaseArrTime(runPlanStn
										.getArrTime());
								runPlanStn.setBaseDptTime(runPlanStn
										.getDptTime());
							}

							runPlanDao.addRunPlan(runPlan);
							// runPlanStnDao.addRunPlanStn(runPlan.getRunPlanStnList());
							runPlanDao.updateNextTrainId(preRunPlan);

							// 把当前车设置为前车进行下一次循环
							preRunPlan = runPlan;
							groupSeriaNbr = preRunPlan.getGroupSerialNbr();
							trainSort = preRunPlan.getTrainSort();
						} catch (Exception e) {
							logger.error("补全交路出错", e);
						}
					}
				}
			}
			return runPlan;
		}

		/**
		 * 根据plancrossid查询前序列车，日期有重叠的开行计划，按新日期一刀切 1、不管三七二十一，切一刀再说
		 * 2、查询每组车的最后一次生成的每个车（可以和unitcrosstrain匹配），通过参数lastRunPlans传出去
		 * 
		 * @param unitCross
		 *            交路信息全部传进来
		 * @return 按groupserianbr保存最新计划
		 */
		private Map<Integer, RunPlan> getLastRunPlans(String startDate,
				UnitCross unitCross) throws ParseException {
			return getLastRunPlans(startDate, unitCross, false);
		}

		private Map<Integer, RunPlan> getLastRunPlans(String startDate,
				UnitCross unitCross, boolean isOnlyTruncation)
				throws ParseException {
			// 按时间切一刀
			Map<String, Object> params = Maps.newHashMap();
			params.put("unitCrossName", unitCross.getCrossName());
			// 如果不截断原交路就增加这个条件
			if (1 != unitCross.getCutOld()) {
				params.put("baseChartId", unitCross.getBaseChartId());
			}
			params.put(
					"startTime",
					new Timestamp(simpleDateFormat.parse(
							DateTimeFormat.forPattern("yyyyMMdd")
									.parseLocalDate(startDate)
									.toString("yyyy-MM-dd")
									+ " 00:00:00").getTime()));

			// 删除plan_train的同时应该删除plan_train_stn
			runPlanStnDao.deleteRunPlanStnByStartTime(params);
			// 再删除plan_train
			runPlanDao.deleteRunPlanByStartTime(params);

			// 每组车的最新一组开行计划
			Map<Integer, RunPlan> lastRunPlans = Maps.newHashMap();
			// 是否只进行截断操作，如果是，则在这里直接返回
			// 只进行截断操作的条件，新的基本图第一次生成，生成的时间与老图重合，需要截断老图
			if (!isOnlyTruncation) {
				if (!(null != unitCross.getAppointWeek()
						&& !"null".equals(unitCross.getAppointWeek()) && !""
							.equals(unitCross.getAppointWeek()))) {
					// 由于不同基本图不能互相去补，所以这个除了crossName再增加一个限制条件
					// List<RunPlan> preGroup =
					// runPlanDao.findPreRunPlanByPlanCrossName(unitCross.getCrossName());
					Map<String, Object> paramMap = Maps.newHashMap();
					paramMap.put("unitCrossName", unitCross.getCrossName());
					paramMap.put("baseChartId", unitCross.getBaseChartId());
					List<RunPlan> preGroup = runPlanDao
							.findPreRunPlanByParam(paramMap);
					for (RunPlan runPlan : preGroup) {
						lastRunPlans.put(runPlan.getGroupSerialNbr(), runPlan);
					}
					Collections.sort(preGroup, new Comparator<RunPlan>() {
						@Override
						public int compare(RunPlan o1, RunPlan o2) {
							LocalDate t1 = LocalDate.fromDateFields(new Date(o1
									.getEndDateTime().getTime()));
							LocalDate t2 = LocalDate.fromDateFields(new Date(o2
									.getEndDateTime().getTime()));
							return t1.compareTo(t2);
						}
					});
					if (preGroup.size() > 0) {
						RunPlan runPlan = preGroup.get(preGroup.size() - 1);
						PlanCrossInfo planCrossInfo = planCrossDao
								.findById(runPlan.getPlanCrossId());
						planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(
								new Date(runPlan.getEndDateTime().getTime()))
								.toString("yyyyMMdd"));
						planCrossDao.update(planCrossInfo);
					}
				}
			}

			return lastRunPlans;
		}

		/**
		 * 根据groupNbr和trainsort获取同一组车的下一辆车
		 * 
		 * @param groupNbr
		 *            组号
		 * @param trainSort
		 *            车号
		 * @return
		 */
		private UnitCrossTrain getNextUnitCrossTrain(int groupNbr,
				int trainSort, UnitCross unitCross) {
			final List<UnitCrossTrain> unitCrossTrainList = unitCross
					.getUnitCrossTrainList();
			for (UnitCrossTrain unitCrossTrain : unitCrossTrainList) {
				if (groupNbr == unitCrossTrain.getGroupSerialNbr()
						&& trainSort == unitCrossTrain.getTrainSort() - 1) {
					return unitCrossTrain;
				}
			}
			return null;
		}

		/**
		 * 找到最先结束的交路组数，后面就从这个交路开始继续生成计划
		 * 
		 * @param lastRunPlans
		 * @return
		 */
		private int getFirstEndedGroup(Map<Integer, RunPlan> lastRunPlans) {
			if (lastRunPlans.values().size() == 0) {
				return 0;
			}
			List<RunPlan> runPlanList = Lists.newArrayList(lastRunPlans
					.values());
			Collections.sort(runPlanList, new Comparator<RunPlan>() {
				@Override
				public int compare(RunPlan o1, RunPlan o2) {
					return o1.getEndDateTime().compareTo(o2.getEndDateTime());
				}
			});
			return runPlanList.get(0).getGroupSerialNbr();
		}

		private void sendRunPlanMsg(String unitCrossId, RunPlan runPlan) {
			if (this.msgReceiveUrl == null) {
				return;
			}
			Map<String, Object> msg = Maps.newHashMap();
			msg.put("unitCrossId", unitCrossId);
			msg.put("trainNbr", runPlan.getTrainNbr());
			msg.put("day", runPlan.getRunDate());
			msg.put("runFlag", runPlan.getSpareFlag());
			ObjectMapper jsonUtil = new ObjectMapper();

			try {
				msgService.sendMessage(jsonUtil.writeValueAsString(msg),
						this.msgReceiveUrl, "updateTrainRunPlanDayFlag");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("发送消息失败", e);
			}
		}

		/**
		 * 推送某个交路的开始生成计划或结束结束生成计划
		 * 
		 * @param unitCrossId
		 * @param status
		 *            1：未完成 2：完成
		 * @throws JsonProcessingException
		 */
		private void sendUnitCrossMsg(String unitCrossId, int status)
				throws JsonProcessingException {
			if (this.msgReceiveUrl == null) {
				return;
			}
			Map<String, Object> msg = Maps.newHashMap();
			msg.put("unitCrossId", unitCrossId);
			msg.put("status", status);
			ObjectMapper jsonUtil = new ObjectMapper();

			try {

				msgService.sendMessage(jsonUtil.writeValueAsString(msg),
						this.msgReceiveUrl, "updateTrainRunPlanStatus");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("发送消息失败", e);
			}
		}
	}

	private void pubSendUnitCrossMsg(String unitCrossId, int status,
			String msgReceiveUrl) throws JsonProcessingException {
		if (msgReceiveUrl == null) {
			return;
		}
		Map<String, Object> msg = Maps.newHashMap();
		msg.put("unitCrossId", unitCrossId);
		msg.put("status", status);
		ObjectMapper jsonUtil = new ObjectMapper();

		try {

			msgService.sendMessage(jsonUtil.writeValueAsString(msg),
					msgReceiveUrl, "updateTrainRunPlanStatus");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送消息失败", e);
		}
	}

	public int deletePlanCrossByPlanCorssIds(String[] crossIdsArray) {
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = Maps.newHashMap();
		int size = crossIdsArray.length;
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIdsArray[i]).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("planCrossIds", bf.toString());
		// 删除经由
		deletePlanTrainStnsByPlanCrossIds(crossIdsArray);
		// 删除车
		deletePlanTrainsByPlanCorssIds(crossIdsArray);

		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_PLANCROSS_INFO_TRAIN_FOR_CROSSIDS,
				reqMap);
	}

	/**
	 * 保存PlanCheckInfo到数据库plan_check中
	 * 
	 * @param planCheckInfo
	 * @return
	 */
	public int savePlanCheckInfo(PlanCheckInfo planCheckInfo) {
		return baseDao.insertBySql(Constants.CROSSDAO_INSERT_PLAN_CHECK_INFO,
				planCheckInfo);
	}

	// CROSSDAO_INSERT_CMD_CHECK_INFO
	public int savePlanCheckInfoCmd(PlanCheckInfo planCheckInfo) {
		return baseDao.insertBySql(Constants.CROSSDAO_INSERT_CMD_CHECK_INFO,
				planCheckInfo);
	}

	/**
	 * 通过planCrossId查询planCheckInfo对象
	 * 
	 * @param planCrossId
	 *            planCrossId
	 * @return List<PlanCheckInfo>
	 */
	public List<PlanCheckInfo> getPlanCheckInfoForPlanCrossId(String planCrossId) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSID,
				planCrossId);
	}

	public List<PlanCheckInfo> getPlanCheckCountByIDHis(Map<String, String> map) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_getPlanCheckCountByIDHis, map);
	}

	public List<PlanCheckInfo> getPlanCheckInfoForPlanCrossId1(
			String planCrossId) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSID1,
				planCrossId);
	}

	public List<PlanCheckInfo> getPlanCheckInfoForPlanCrossIdcmdtel(
			String checkCmdtel) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSIDCMDTEL,
				checkCmdtel);
	}

	public List<PlanCheckInfo> getPlanCheckInfoForPlanCrossIdcmdtel1(
			String checkCmdtel) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_PLANCHECKINFO_FOR_PLANCROSSIDCMDTEL1,
				checkCmdtel);
	}

	/**
	 * 更新表plan_cross中checkType的值
	 * 
	 * @param planCrossId
	 *            planCrossId
	 * @param checkType
	 *            审核状态（0:未审核1:部分局审核2:途经局全部审核）
	 * @return 删除数量
	 */
	public int updateCheckTypeForPlanCrossId(String planCrossId, int checkType) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("planCrossId", planCrossId);
		reqMap.put("checkType", checkType);
		return baseDao.updateBySql(
				Constants.CROSSDAO_UPDATE_CHECKTYPE_FOR_PLANCROSSID, reqMap);
	}

	/**
	 * 更新表cmd_train中checkType的值
	 * 
	 * @param planCrossId
	 *            planCrossId
	 * @param checkType
	 *            审核状态（0:未审核1:部分局审核2:途经局全部审核）
	 * @return 删除数量
	 */
	// CROSSDAO_UPDATE_CHECKTYPE_FOR_CMDID
	public int updateCheckTypeForCmdId(String planCrossId, int checkType) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("planCrossId", planCrossId);
		reqMap.put("checkType", checkType);
		return baseDao.updateBySql(
				Constants.CROSSDAO_UPDATE_CHECKTYPE_FOR_CMDID, reqMap);
	}

	/**
	 * 根据planCrossId列表和rundate的开始时间和结束时间查询上图的列车信息
	 * 
	 * @param startDate
	 *            格式yyyyMMdd
	 * @param endDate
	 *            格式yyyyMMdd
	 * @param planCrossIdList
	 * @return
	 */
	public List<ParamDto> getTotalTrainsForPlanCrossIds(String startDate,
			String endDate, List<String> planCrossIdList) {
		List<ParamDto> list = new ArrayList<ParamDto>();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		StringBuffer bf = new StringBuffer();
		int size = planCrossIdList.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(planCrossIdList.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		paramMap.put("planCrossIds", bf.toString());
		List<Map<String, Object>> mapList = baseDao.selectListBySql(
				Constants.TRAINPLANDAO_GET_TOTALTRAINS_FOR_PLAN_CROSS_ID,
				paramMap);
		if (mapList != null && mapList.size() > 0) {
			// System.err.println("mapList.size===" + mapList.size());
			for (Map<String, Object> map : mapList) {
				ParamDto dto = new ParamDto();
				dto.setSourceEntityId(StringUtil.objToStr(map
						.get("BASE_TRAIN_ID")));
				dto.setPlanTrainId(StringUtil.objToStr(map.get("PLAN_TRAIN_ID")));
				String time = StringUtil.objToStr(map.get("RUN_DATE"));
				dto.setTime(DateUtil.formateDate(time));
				list.add(dto);
			}
		}
		return list;
	}

	private int deletePlanTrainStnsByPlanCrossIds(String[] crossIdsArray) {
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossIdsArray.length;
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIdsArray[i]).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("planCrossIds", bf.toString());

		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_PLANTRAINSTN_INFO_TRAIN_FOR_CROSSIDS,
				reqMap);
	}

	private int deletePlanTrainsByPlanCorssIds(String[] crossIdsArray) {
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossIdsArray.length;
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIdsArray[i]).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("planCrossIds", bf.toString());

		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_PLANTRAIN_INFO_TRAIN_FOR_CROSSIDS,
				reqMap);
	}

	public QueryResult<CrossRunPlanInfo> getTrainRunPlansForCreateLine(
			Map<String, Object> params) throws Exception {
		return baseDao.selectListForPagingBySql(
				Constants.GET_TRAIN_RUN_PLANS_FOR_CREATElINE, params);

	}

	public QueryResult<CrossRunPlanInfo> getTrainRunPlansForCreate(
			Map<String, Object> params) throws Exception {
		return baseDao.selectListForPagingBySql(
				Constants.GET_TRAIN_RUN_PLANS_FOR_CREATE, params);

	}

	public QueryResult<CrossRunPlanInfo> getTrainRunPlansForCreateGT(
			Map<String, Object> params) throws Exception {
		return baseDao.selectListForPagingBySql(
				Constants.GET_TRAIN_RUN_PLANS_FOR_CREATEGT, params);

	}

	public QueryResult<CrossRunPlanInfo> getTrainRunPlanForLk(
			Map<String, Object> params) throws Exception {
		return baseDao.selectListForPagingBySql(
				Constants.RUN_PLAN_DAO_GET_TRAINRUNPLAN_FOR_LK, params);

	}

	public List<RunPlanRollingDTO> getPlanTrainInfoForBaseChartId(
			String baseChartId) throws Exception {
		return baseDao.selectListBySql(
				Constants.RUN_PLAN_DAO_GET_TRAINRUNPLAN_FOR_CHARTID,
				baseChartId);

	}

	/**
	 * 根据PLAN_TRAIN_ID（列车ID）查询开行计划信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)列车ID
	 * @author denglj
	 * @date 2014-10-26
	 */
	public PlanTrainDto findPlanByPlanIdForTrainTime(String planTrainId) {
		return runPlanDao.findPlanByPlanIdForTrainTime(planTrainId);
	}

	/**
	 * 根据PLAN_TRAIN中PLAN_TRAIN_ID查询基本图列车时刻信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @author denglj
	 * @date 2014-10-27
	 */
	public List<TrainTimeInfo> jbtTrainStnByPlanTrainId(String planTrainId) {
		return baseDao.selectListBySql(Constants.JBT_TRAINSTN_BY_PLANTRAINID,
				planTrainId);
	}

	/**
	 * 根据PLAN_TRAIN中PLAN_TRAIN_ID查询客运列车时刻信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @author denglj
	 * @date 2014-10-27
	 */
	public List<TrainTimeInfo> kyTrainStnByPlanTrainId(String planTrainId) {
		return baseDao.selectListBySql(Constants.KY_TRAINSTN_BY_PLANTRAINID,
				planTrainId);
	}

	/**
	 * 根据trainNbr、runDate、startStn、endStn查询PLAN_TRAIN开行计划信息（唯一）
	 * 
	 * @param queryMap
	 *            { trainNbr 车次 (检索条件 必填) runDate 开行日期yyyyMMdd (检索条件 必填)
	 *            startStn 始发站 (检索条件 必填) endStn 终到站 (检索条件 必填) }
	 * @author denglj
	 * @date 2014-10-26
	 */
	public List<PlanTrainDto> findPlanByOther(Map<String, Object> queryMap) {
		return runPlanDao.findPlanByOther(queryMap);
	}

	/**
	 * 修改plan_train表中的落成局.
	 * 
	 * @param sent_bureau
	 *            当前落成局.
	 * @param sent_bureau_his
	 *            历史落成局.
	 * @param id
	 * @return
	 */
	public Integer updPlanTrainBureau(String sent_bureau,
			String sent_bureau_his, String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sent_bureau", sent_bureau);
		map.put("sent_bureau_his", sent_bureau_his);
		map.put("id", id);
		return runPlanDao.updPlanTrainBureau(map);
	}

	/**
	 * jbt,列车类型
	 * 
	 * @return
	 */
	public java.util.List<BusinessInfo> getBusiness() {
		return baseDao
				.selectListBySql(Constants.BUSINESS_GETBUSINESSINFO, null);
	}

	/**
	 * 车次名称重复的.
	 * 
	 * @param map
	 * @return
	 */
	public List<RunPlanTrainDto> isChongfu(Map<String, Object> map, String cnMap) {
		List<Map<String, Object>> gsnMap = runPlanDao
				.getGroupSerialNbrByPlanCrossId(MapUtils.getString(map,
						"planCrossId"));
		if (!gsnMap.isEmpty()) {
			return chongFuTrainNbr(map, gsnMap.size(), cnMap);
		}
		return new ArrayList<RunPlanTrainDto>();
	}

	/**
	 * 重复车次的处理.
	 * 
	 * @param map
	 * @param gsn
	 *            几组车底.
	 * @return
	 */
	public List<RunPlanTrainDto> chongFuTrainNbr(Map<String, Object> map,
			Integer gsn, String cnMap) {

		// 先判断有几组车底,1组:直接挨个赋值,多组:其余组全部想第一组赋值

		List<RunPlanTrainDto> runPlans = Lists.newArrayList();
		List<Map<String, Object>> lm = runPlanDao.getTrainRunPlans2(map);
		if (lm.isEmpty()) {
			return runPlans;
		} else {
			boolean b = false;
			if (gsn > 1) {
				b = true;
			}

			/** 多组车底,需要以车组号来区分 **/

			/** 1组车底 以实现 **/
			List<TrainRunDto> tempSubList = new LinkedList<TrainRunDto>();
			RunPlanTrainDto currTrain = null;
			String trainNbr = "";
			String groupSerialNbr = "";
			int forI = 0;
			for (int i = 0; i < lm.size(); i++) {
				forI = i;
				Map<String, Object> m = lm.get(i);
				// 上一条数据的车次与当前车次不同时,就需要一个新的对象放入集合.
				System.out.println(MapUtils.getString(m, "TRAINNBR"));
				if (!trainNbr.equals(MapUtils.getString(m, "TRAINNBR"))) {
					if (i != 0) {
						runPlans.add(currTrain);
					}
					// 车组如果是多组,并且第一个车组已经全部循环完毕
					// System.out.println(MapUtils.getString(m,
					// "GROUPSERIALNBR"));
					if (b
							&& !"1".equals(MapUtils.getString(m,
									"GROUPSERIALNBR"))) {
						// 跳出循环,将后面剩余的组全部赋值到第一组里
						break;
					}

					currTrain = new RunPlanTrainDto(map.get("startDay")
							.toString(), map.get("endDay").toString(), null,
							null, null);
					tempSubList = currTrain.getRunPlans();
					trainNbr = MapUtils.getString(m, "TRAINNBR");
					groupSerialNbr = MapUtils.getString(m, "GROUPSERIALNBR");
					currTrain.setTrainNbr(trainNbr);
					currTrain.setStartStn(MapUtils.getString(m, "STARTSTN"));
					currTrain.setEndStn(MapUtils.getString(m, "ENDSTN"));
				}
				for (TrainRunDto tempDto : tempSubList) {
					if (MapUtils.getString(m, "RUNDAY")
							.equals(tempDto.getDay())) {
						tempDto.setRunFlag(MapUtils.getString(m, "RUNFLAG"));
						tempDto.setTelName(MapUtils.getString(m, "TELNAME"));
						tempDto.setPlanTrainId(MapUtils.getString(m,
								"PLANTRAINID"));
					}
				}
				if (i == (lm.size() - 1)) {
					runPlans.add(currTrain);
				}
			}
			// 证明有多组,并且第一组已经添加完毕
			if (b) {
				b = false;
				// System.out.println("循环到了第:" + forI + "行");
				// 尚未添加的车组
				if (Integer.parseInt(groupSerialNbr) <= gsn) {
					for (int i = Integer.parseInt(groupSerialNbr); i < gsn; i++) {
						for (int k = 0; k < runPlans.size(); k++) {
							RunPlanTrainDto rptd = runPlans.get(k);
							// 根据获得到的数据,查询想匹配的数据
							for (int j = forI; j < lm.size(); j++) {
								// 从第一组完成哪一行数据开始循环
								// System.out.println(lm.get(j));
								Map<String, Object> m = lm.get(j);
								if (rptd.getTrainNbr().equals(
										MapUtils.getString(m, "TRAINNBR"))
										&& rptd.getStartStn().equals(
												MapUtils.getString(m,
														"STARTSTN"))
										&& rptd.getEndStn()
												.equals(MapUtils.getString(m,
														"ENDSTN"))) {
									b = true;
									// 数据的处理,循环已经有的数据,然后根据当前的数据来进行判断赋值
									tempSubList = rptd.getRunPlans();
									for (TrainRunDto tempDto : tempSubList) {
										if (MapUtils.getString(m, "RUNDAY")
												.equals(tempDto.getDay())) {
											tempDto.setRunFlag(MapUtils
													.getString(m, "RUNFLAG"));
											tempDto.setTelName(MapUtils
													.getString(m, "TELNAME"));
											tempDto.setPlanTrainId(MapUtils
													.getString(m, "PLANTRAINID"));
											break;
										}
									}
									// 移除已经添加的数据
									lm.remove(j);
									--j;
								} else {
									if (b) {
										// 第一组中的第一个车次的数据在第二组中已经添加完毕,进行下一个车次的添加
										// forI = j;
										b = false;
										break;
									}
								}
							}
						}
					}
				}

			}
		}

		return runPlans;
	}

	/**
	 * 车次名称不重复的.
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RunPlanTrainDto> isNotChongfu(Map<String, Object> map) {
		// 由于车次没有重复的,直接全部数据查询出来,不需要其他处理
		List<RunPlanTrainDto> crossRunPlans = baseDao.selectListBySql(
				Constants.GET_TRAIN_RUN_PLAN1, map);

		List<RunPlanTrainDto> runPlans = Lists.newArrayList();

		String startDay = map.get("startDay").toString();
		String endDay = map.get("endDay").toString();
		List<TrainRunDto> tempSubList = new LinkedList<TrainRunDto>();
		RunPlanTrainDto currTrain = null;
		for (int j = 0; j < crossRunPlans.size(); j++) {
			RunPlanTrainDto runPlan = crossRunPlans.get(j);
			currTrain = new RunPlanTrainDto(startDay, endDay, null, null, null);
			tempSubList = currTrain.getRunPlans();
			currTrain.setTrainNbr(runPlan.getTrainNbr());
			List<TrainRunDto> list = runPlan.getRunPlans();
			for (TrainRunDto dto : list) {
				String day = dto.getDay();
				String runFlag = dto.getRunFlag();
				String telName = dto.getTelName();
				String planTrainId = dto.getPlanTrainId();
				for (TrainRunDto tempDto : tempSubList) {
					if (day.equals(tempDto.getDay())) {
						tempDto.setRunFlag(runFlag);
						tempDto.setTelName(telName);
						tempDto.setPlanTrainId(planTrainId);
						break;
					}
				}
			}
			runPlans.add(currTrain);
		}
		return runPlans;
	}

	/**
	 * 查询列车信息.
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getTrainByMap(Map<String, Object> params) {
		return runPlanDao.getTrainByMap(params);
	}

	/**
	 * 根据传递进来的车次，获取当前车次所在车组的全部车辆信息.
	 * 
	 * @param train_nbr
	 * @param date
	 */
	public List<String> getAllTrain(String train_nbr, List<String> dates,
			String planCrossId) {
		List<String> idList = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		for (String date : dates) {
			params.clear();
			params.put("train_nbr", train_nbr);
			params.put("run_date", DateUtil.parseStringDateTOyyyymmdd(date));
			params.put("plan_cross_id", planCrossId);
			// 获取当前列车的信息
			List<Map<String, Object>> lm = getTrainByMap(params);
			if (!lm.isEmpty()) {
				// 获取当前列车所在车组的所有列车信息
				Map<String, Object> dqMap = lm.get(0);
				// 保存当前的id
				idList.add(MapUtils.getString(dqMap, "PLAN_TRAIN_ID"));

				// 临客只有1组，直接返回就行了.
				if (!dqMap.containsKey("CROSS_NAME")) {
					// break;
					continue;
				}

				// 当前车组下共有多少车辆
				Map<String, Object> selMap = CommonUtil.getTrainSelCs(
						MapUtils.getString(dqMap, "CROSS_NAME"),
						MapUtils.getString(dqMap, "TRAIN_NBR"),
						MapUtils.getInteger(dqMap, "TRAIN_SORT"));

				if (MapUtils.getInteger(selMap, "before") > 0) {
					// 得到车辆前面所有车辆信息，语言很难描述啦，我尽力啦。
					// 0D6802-0D6801-G672-G663-0G663，例如传递进来的是0D6801，在这个位置要将0D6802查询出来
					String pre_train_id = MapUtils.getString(dqMap,
							"PRE_TRAIN_ID");
					// 向前查询,根据当前车辆的pre_train_id查询
					for (int i = 0; i < MapUtils.getInteger(selMap, "before"); i++) {
						// 进行查询
						params.clear();
						params.put("pre_train_id", pre_train_id);
						lm = getTrainByMap(params);
						if (!lm.isEmpty()) {
							pre_train_id = MapUtils.getString(lm.get(0),
									"PRE_TRAIN_ID");
							idList.add(MapUtils.getString(lm.get(0),
									"PLAN_TRAIN_ID"));
						}
					}
				}
				if (MapUtils.getInteger(selMap, "after") > 0) {
					// 得到车辆前面所有车辆信息，语言很难描述啦，我尽力啦。
					// 0D6802-0D6801-G672-G663-0G663，例如传递进来的是0D6801，在这个位置要将G672,G663,0G663查询出来
					String next_train_id = MapUtils.getString(dqMap,
							"NEXT_TRAIN_ID");
					// 向后查询,根据当前车辆的next_train_id查询
					for (int i = 0; i < MapUtils.getInteger(selMap, "after"); i++) {
						// 进行查询
						params.clear();
						params.put("next_train_id", next_train_id);
						lm = getTrainByMap(params);
						if (!lm.isEmpty()) {
							next_train_id = MapUtils.getString(lm.get(0),
									"NEXT_TRAIN_ID");
							idList.add(MapUtils.getString(lm.get(0),
									"PLAN_TRAIN_ID"));
						}
					}
				}

				// return idList;

			} else {
				return null;
			}
		}
		return idList;
	}

	/**
	 * 与上一个方法参数不同.
	 * 
	 * @param train_nbr
	 * @param date
	 */
	public List<String> getAllTrainByPlanTrainId(String planTrainId) {
		List<String> idList = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("plan_train_id", planTrainId);
		// 获取当前列车的信息
		List<Map<String, Object>> lm = getTrainByMap(params);
		if (!lm.isEmpty()) {
			// 获取当前列车所在车组的所有列车信息
			Map<String, Object> dqMap = lm.get(0);
			// 保存当前的id
			idList.add(MapUtils.getString(dqMap, "PLAN_TRAIN_ID"));

			// 当前车组下共有多少车辆
			Map<String, Object> selMap = CommonUtil.getTrainSelCs(
					MapUtils.getString(dqMap, "CROSS_NAME"),
					MapUtils.getString(dqMap, "TRAIN_NBR"),
					MapUtils.getInteger(dqMap, "TRAIN_SORT"));

			if (MapUtils.getInteger(selMap, "before") > 0) {
				// 得到车辆前面所有车辆信息，语言很难描述啦，我尽力啦。
				// 0D6802-0D6801-G672-G663-0G663，例如传递进来的是0D6801，在这个位置要将0D6802查询出来
				String pre_train_id = MapUtils.getString(dqMap, "PRE_TRAIN_ID");
				// 向前查询,根据当前车辆的pre_train_id查询
				for (int i = 0; i < MapUtils.getInteger(selMap, "before"); i++) {
					// 进行查询
					params.clear();
					params.put("pre_train_id", pre_train_id);
					lm = getTrainByMap(params);
					if (!lm.isEmpty()) {
						pre_train_id = MapUtils.getString(lm.get(0),
								"PRE_TRAIN_ID");
						idList.add(MapUtils.getString(lm.get(0),
								"PLAN_TRAIN_ID"));
					}
				}
			}
			if (MapUtils.getInteger(selMap, "after") > 0) {
				// 得到车辆前面所有车辆信息，语言很难描述啦，我尽力啦。
				// 0D6802-0D6801-G672-G663-0G663，例如传递进来的是0D6801，在这个位置要将G672,G663,0G663查询出来
				String next_train_id = MapUtils.getString(dqMap,
						"NEXT_TRAIN_ID");
				// 向后查询,根据当前车辆的next_train_id查询
				for (int i = 0; i < MapUtils.getInteger(selMap, "after"); i++) {
					// 进行查询
					params.clear();
					params.put("next_train_id", next_train_id);
					lm = getTrainByMap(params);
					if (!lm.isEmpty()) {
						next_train_id = MapUtils.getString(lm.get(0),
								"NEXT_TRAIN_ID");
						idList.add(MapUtils.getString(lm.get(0),
								"PLAN_TRAIN_ID"));
					}
				}
			}
		} else {
			return null;
		}
		return idList;
	}

	public List<PlanTrainDto> getTrainCfByMap(Map<String, Object> m) {
		return runPlanDao.getTrainByMapReturnObj(m);
	}

	/**
	 * 差别数据：第二个日期没有的数据.
	 * 
	 * @param m
	 * @return
	 */
	public List<PlanTrainDto> getCbPlansQ(Map<String, Object> m) {

		List<PlanTrainDto> lpt = runPlanDao.getCbPlansQ(m);
		if (!lpt.isEmpty()) {

		} else {
			return null;
		}

		return runPlanDao.getCbPlansQ(m);
	}

	/**
	 * 差别数据：第一个日期没有的数据.
	 * 
	 * @param m
	 * @return
	 */
	public List<PlanTrainDto> getCbPlansH(Map<String, Object> m) {
		return runPlanDao.getCbPlansH(m);
	}

	/**
	 * 差别数据：2个日期都有的.
	 * 
	 * @param m
	 * @return
	 */
	public List<PlanTrainDto> getCbPlans(Map<String, Object> m) {
		return runPlanDao.getCbPlans(m);
	}

	/**
	 * 落成，实际处理.
	 * 
	 * @param planTrainIdList
	 */
	public void dealWithCompletion(List<String> planTrainIdList) {
		if (!planTrainIdList.isEmpty()) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("idList", planTrainIdList);
			List<VaildPlanTrainData> vptdList = runPlanDao
					.getPlanTrainByIdRenturnVPTD(m);
			System.out.println(vptdList);
		}
	}

	/**
	 * 查询plan_train.
	 * 
	 * @param list
	 *            批量查询List<String>.
	 * @return VaildPlanTrainData.
	 */
	public List<VaildPlanTrainData> getPlanTrainByIdRenturnVPTD(
			Map<String, Object> m) {
		return runPlanDao.getPlanTrainByIdRenturnVPTD(m);
	}

	public void insertVaildPlanTrainTemp(VaildPlanTrainTemp vaildPlanTrainTemp) {

		runPlanDao.insertVaildPlanTrainTemp(vaildPlanTrainTemp);
	}

	/**
	 * 根据map查询plan_cross.
	 * 
	 * @param reqMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PlanCross> getPlanCrossByMap(Map<String, Object> reqMap) {
		return baseDao.selectListBySql(Constants.GET_PLAN_CROSS_BY_MAP, reqMap);
	}

	/**
	 * 处理交路(1.G1-G2(太西京)).
	 * 
	 * @param pcList
	 *            交路集合(planCross).
	 * @return
	 */
	public String dealWithCross(List<PlanCross> pcList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pcList.size(); i++) {
			if (org.apache.commons.lang.StringUtils.isNotEmpty(sb.toString())) {
				sb.append("\n");
			}
			PlanCross pc = pcList.get(i);
			sb.append(i + 1);
			sb.append(".");
			sb.append(pc.getCrossName());
			sb.append("(");
			sb.append(dealWithRelevantBureau(pc.getRelevantBureau()));
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
	 *            (NHPV).
	 * @param dqBureau
	 *            当前局.
	 * @return ('N','H','P','V')
	 */
	public String dealWithRelevantBureau1(List<PlanCross> pcList,
			String dqBureau) {
		StringBuilder sb = new StringBuilder();
		if (org.apache.commons.lang.StringUtils.isNotEmpty(dqBureau)) {
			for (int i = 0; i < pcList.size(); i++) {
				String relevantBureau = pcList.get(i).getRelevantBureau();
				if (org.apache.commons.lang.StringUtils
						.isNotEmpty(relevantBureau)) {
					for (int j = 0; j < relevantBureau.length(); j++) {
						String str = relevantBureau.substring(j, j + 1);
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
