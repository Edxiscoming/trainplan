package org.railway.com.trainplan.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.PlanTrainForJbtEdit;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.entity.TrainTimeInfoJbt;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.web.controller.RunPlanController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class TrainTimeService {

	private static Log logger = LogFactory.getLog(TrainTimeService.class
			.getName());

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private RunPlanService runPlanService;

	/**
	 * 通过base_train_id查询列车时刻表，用于对数表和交路单元功能显示详点和简点
	 * 
	 * @param baseTrainId
	 *            列车id
	 * @return
	 */
	public List<TrainTimeInfo> getTrainTimes(String baseTrainId) {
		return baseDao.selectListBySql(
				Constants.TRAININFO_GETTRAINTIMEINFO_BY_TRAINID, baseTrainId);
	}

	/**
	 * 通过plan_train_id查询列车时刻表(关系到表：plan_train和plan_train_stn)
	 * 
	 * @param planTrainId
	 *            表plan_train中的字段plan_train_id
	 * @return
	 */
	public List<TrainTimeInfo> getTrainTimeInfoByTrainId(String planTrainId) {
		return baseDao.selectListBySql(
				Constants.TRAININFO_GETPLANLINE_TRAINTIMEINFO_BY_TRAINID,
				planTrainId);

	}

	/**
	 * 通过plan_train_id从基本图库中获取列车时刻表
	 * 
	 * @param planTrainId
	 * @return
	 */
	public List<BaseCrossTrainInfoTime> getTrainTimeInfoByPlanTrainId(
			String planTrainId) {
		return baseDao.selectListBySql(
				Constants.TRAININFO_GET_TRAINTIMEINFO_BY_PLAN_TRAIN_ID,
				planTrainId);
	}

	public List<TrainTimeInfo> getTrainTimeInfoByPlanTrainIdjy(
			String planTrainId) {
		return baseDao.selectListBySql(
				Constants.TRAININFO_GET_TRAINTIMEINFO_BY_PLAN_TRAIN_IDJY,
				planTrainId);
	}

	public List<TrainTimeInfo> getTrainTimeInfoByPlanTrainIdgt(
			String planTrainId) {
		return baseDao.selectListBySql(
				Constants.TRAININFO_GET_TRAINTIMEINFO_BY_PLAN_TRAIN_IDGT,
				planTrainId);
	}

	/**
	 * 根据planTrainId查询plan_train_stn表信息
	 * 
	 * @param planTrainId
	 * @return
	 */
	public List<TrainTimeInfo> getPlanTrainStnInfoForPlanTrainId(
			String planTrainId) {
		return baseDao.selectListBySql(
				Constants.TRAININFO_GET_PLAN_TRAIN_STN_BY_TRAINID, planTrainId);

	}

	// getTrainTimeInfoByPlanTrainIdjy

	public List<TrainTimeInfo> getTrainLineTimes(String trainId) {
		return baseDao.selectListBySql(Constants.GET_TRAINLINES_BY_TRAINLINEID,
				trainId);
	}

	/**
	 * 批量修改开行计划时刻点单
	 * 
	 * @category 业务规则：1、删除plan_train_stn中相关记录 2、保存新的plan_train_stn信息
	 *           3、根据runDate、
	 *           trainNbr、startStn、endStn确定PLAN_TRAIN表唯一记录，并置空“生成运行线、
	 *           一级审核、二级审核”标记、运行线ID、BASE_TRAIN_ID
	 * 
	 * @param runDates
	 *            yyyy-MM-dd//"'20140915','20140916','20140917','20140918'"
	 * @param trainNbr
	 *            车次号
	 * @param list
	 *            list里存放TrainTimeInfo对象， 主要是对arrTime，dptTime，trackName的更改
	 * @param startStn
	 *            始发站
	 * @param endStn
	 *            终到站
	 * 
	 * @author denglj
	 * @date 2014-09-23
	 * @update 2014-10-28
	 * @return 修改成功的条数
	 */
	public int updatePlanLineTrainTimes(List<String> runDates, String telName,
			String trainNbr, String startStn, String endStn,
			List<TrainTimeInfo> list) {
		int totalCount = 0;
		for (String runDate : runDates) {
			Map<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("runDate", runDate.replaceAll("-", ""));// 20140915
			reqMap.put("telName", telName);
			reqMap.put("trainNbr", trainNbr);
			reqMap.put("startStn", startStn);
			reqMap.put("endStn", endStn);

			// 根据trainNbr、runDate、startStn、endStn查询PLAN_TRAIN开行计划信息（唯一）
			List<PlanTrainDto> planTrainObjList = runPlanService
					.findPlanByOther(reqMap);
			PlanTrainDto planTrainObj = new PlanTrainDto();
			if (null != planTrainObjList && !planTrainObjList.isEmpty()) {
				planTrainObj = planTrainObjList.get(0);
			}
			if (planTrainObj == null) {
				logger.warn("警告：批量修改列车时刻方法。列车开行计划数据不存在，参数条件runDate:"
						+ runDate.replaceAll("-", "") + "  trainNbr:"
						+ trainNbr + "  startStn:" + startStn + "  endStn:"
						+ endStn);
				continue;
			}

			// 1、删除plan_train_stn中相关记录
			reqMap.put("planTrainId", planTrainObj.getPlanTrainId());
			baseDao.deleteBySql(Constants.DELETE_TRAINSTN_BY_PLANTRAINID,
					reqMap);

			// 设置时刻信息
			for (TrainTimeInfo timeInfo : list) {
				try {
					timeInfo.setPlanTrainStnId(UUID.randomUUID().toString());// ID
					timeInfo.setPlanTrainId(planTrainObj.getPlanTrainId());// 列车Id

					if (!StringUtil.isEmpty(timeInfo.getArrTime())) {
						timeInfo.setArrTime(DateUtil.getDateByDay(runDate,
								-timeInfo.getArrRunDays())
								+ " "
								+ DateUtil.format(DateUtil.parseDate(
										timeInfo.getArrTime(),
										"yyyy-MM-dd HH:mm:ss"), "HH:mm:ss"));
					}
					if (!StringUtil.isEmpty(timeInfo.getBaseArrTime())) {
						timeInfo.setBaseArrTime(DateUtil.getDateByDay(runDate,
								-timeInfo.getArrRunDays())
								+ " "
								+ DateUtil.format(DateUtil.parseDate(
										timeInfo.getBaseArrTime(),
										"yyyy-MM-dd HH:mm:ss"), "HH:mm:ss"));
					}
					if (!StringUtil.isEmpty(timeInfo.getDptTime())) {
						timeInfo.setDptTime(DateUtil.getDateByDay(runDate,
								-timeInfo.getRunDays())
								+ " "
								+ DateUtil.format(DateUtil.parseDate(
										timeInfo.getDptTime(),
										"yyyy-MM-dd HH:mm:ss"), "HH:mm:ss"));
					}
					if (!StringUtil.isEmpty(timeInfo.getBaseDptTime())) {
						timeInfo.setBaseDptTime(DateUtil.getDateByDay(runDate,
								-timeInfo.getRunDays())
								+ " "
								+ DateUtil.format(DateUtil.parseDate(
										timeInfo.getBaseDptTime(),
										"yyyy-MM-dd HH:mm:ss"), "HH:mm:ss"));
					}

				} catch (Exception e) {
					logger.error("批量修改时刻出错", e);
					throw new RuntimeException(e);
				}
			}

			reqMap.put("trainStnList", list);
			// 2、保存新的plan_train_stn信息
			baseDao.updateBySql(Constants.ADD_TRAINSTNS, reqMap);

			// 3、根据runDate、trainNbr、startStn、endStn确定PLAN_TRAIN表唯一记录，并重置“生成运行线、一级审核、二级审核”标记、运行线ID、BASE_TRAIN_ID
			totalCount += baseDao.updateBySql(Constants.RESET_PLANTRAIN_BY_ID,
					reqMap);
		}
		return totalCount;
	}

	public int updatePlanLineTrain(String startStn, String endStn,
			String planTrainId) {
		int totalCount = 0;
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("startStn", startStn);
		reqMap.put("endStn", endStn);
		reqMap.put("planTrainId", planTrainId);
		// 2、更新的plan_train信息
		baseDao.updateBySql(Constants.UPDATEPLANTRAIN, reqMap);
		return totalCount;
	}

	/**
	 * 更改spareFlag 备用及停运标记（1:开行；2:备用；9:停运）
	 * 
	 * @param spareFlag
	 * @param planTrainId
	 * @return
	 */
	public int updateSpareFlag(String spareFlag, String planTrainId) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("spareFlag", spareFlag);
		reqMap.put("planTrainId", planTrainId);
		return baseDao.updateBySql(
				Constants.TRAININFO_UPDATE_SPARE_FLAG_BY_PLANTRAINID, reqMap);
	}

	/**
	 * 更改spareFlag 备用及停运标记（1:开行；2:备用；9:停运）
	 * 
	 * @param spareFlag
	 * @param planTrainId
	 * @return
	 */
	public int updateSpareFlagPlanTrainNbrAndRunday(int spareFlag,
			String trainNbr, String runDay) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("spareFlag", spareFlag);
		reqMap.put("trainNbr", trainNbr);
		reqMap.put("runDay", runDay);
		return baseDao.updateBySql(
				Constants.TRAININFO_UPDATE_SPARE_FLAG_BY_PLANTRAINNBRANDRUNDAY,
				reqMap);
	}

	/**
	 * 更改spareFlag 备用及停运标记（1:开行；2:备用；9:停运）.
	 * 
	 * @param spareFlag
	 *            更改后的状态.
	 * @param trainNbr
	 *            需要更改的车次.
	 * @param runDay
	 *            更改车次的开行时间.
	 * @param oldSpareFlag
	 *            车次当前状态.
	 * @return
	 */
	public int updateSpareFlagPlanTrainNbrAndRunday1(int spareFlag,
			String trainNbr, String runDay, Integer oldSpareFlag,
			String planTrainId, String planCrossId) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("spareFlag", spareFlag);
		reqMap.put("trainNbr", trainNbr);
		reqMap.put("runDay", runDay);
		reqMap.put("oldSpareFlag", oldSpareFlag);
		reqMap.put("planTrainId", planTrainId);
		if(StringUtils.isEmpty(planCrossId)){
			reqMap.put("planCrossId", null);
		}else{
			reqMap.put("planCrossId", planCrossId);
		}
		return baseDao.updateBySql(
				Constants.TRAININFO_UPDATE_SPARE_FLAG_BY_PLANTRAINNBRANDRUNDAY,
				reqMap);
	}

	public int saveAllTrainlineItemTemp(List<TrainTimeInfoJbt> trainTimeList) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("trainTimeList", trainTimeList);
		return baseDao.updateBySql(Constants.ADD_TRAINLINE_ITEM_TEMPS, reqMap);
	}

	public int updatePlanLineTrainForJbtEdit(PlanTrainForJbtEdit trainTemp) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("trainTemp", trainTemp);
		return baseDao.updateBySql(Constants.UPDATE_TRAINLINETRAIN_FORJBTEDIT,
				trainTemp);
	}

	public List<TrainTimeInfoJbt> getTrainTimesforJbtQuery(String trainId) {
		return baseDao
				.selectListBySql(
						Constants.TRAININFO_GETTRAINTIMEINFO_BY_TRAINIDFROMTRAINLINE_JBT,
						trainId);
	}

	/**
	 * 批量修改spare_flag.
	 * 
	 * @param idList
	 *            plan_train_id.
	 * @param spare_flag
	 *            需要改为什么状态.
	 * @param oldSpareFlag
	 *            当前数据的状态应该是什么.
	 * @return
	 */
	public int batchUpdPlanTrain(List<String> idList, Integer spare_flag,
			Integer oldSpareFlag) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("idList", idList);
		reqMap.put("spare_flag", spare_flag);
		reqMap.put("oldSpareFlag", oldSpareFlag);
		return baseDao.updateBySql(Constants.BATCH_UPDATE_PLANTRAIN_BY_IDLIST,
				reqMap);
	}
}
