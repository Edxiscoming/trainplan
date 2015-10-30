package org.railway.com.trainplan.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import mor.railway.cmd.adapter.model.HighlineCross;
import mor.railway.cmd.adapter.service.impl.HighLinePlanGeneratorService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.ConstantUtil;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringAndTimeUtil;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.CompareModel;
import org.railway.com.trainplan.entity.DicRelaCrossPost;
import org.railway.com.trainplan.entity.DicThroughLine;
import org.railway.com.trainplan.entity.HighLineCrossTrainInfo;
import org.railway.com.trainplan.entity.HighlineCrossCmd;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.HighlineCrossTrainBaseInfo;
import org.railway.com.trainplan.entity.HighlineThroughlineInfo;
import org.railway.com.trainplan.entity.HighlineTrainRunLine;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.PlanCrossModel;
import org.railway.com.trainplan.entity.PlanTrainModel;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.CmdPlanDao;
import org.railway.com.trainplan.service.dto.HighLinePlanPageDto;
import org.railway.com.trainplan.service.dto.HighLinePlanTrainDto;
import org.railway.com.trainplan.service.dto.HighLinePlanTreeDto;
import org.railway.com.trainplan.service.dto.HighLinePlanVehicleDto;
import org.railway.com.trainplan.service.dto.OptionDto;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.service.dto.PlanTrainStnDto;
import org.railway.com.trainplan.service.dto.VehicleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HighLineService {
	private static final Logger logger = Logger
			.getLogger(HighLineService.class);

	@Autowired
	private BaseDao baseDao;
	@Autowired
    private CmdPlanDao cmdPlanDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private HighLinePlanGeneratorServiceLocal highLinePlanGeneratorServiceLocal;

	/**
	 * 对表plan_cross批量插入数据
	 * 
	 * @param list
	 * @return
	 */
	public int addPlanCrossInfo(List<PlanCrossInfo> list) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("trainCrossList", list);
		int count = baseDao.insertBySql(Constants.CROSSDAO_ADD_PLAN_CROSS_INFO,
				reqMap);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<BaseTrainInfo> getBaseTrainInfoByParam(Map<String, String> map) {
		return this.baseDao.selectListBySql(
				Constants.CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM, map);
	}

	/**
	 * 通过plan_cross_id查询plancross信息
	 * 
	 * @param planCrossId
	 *            表plan_cross中主键
	 * @return
	 */
	public PlanCrossInfo getPlanCrossInfoForPlanCrossId(String planCrossId) {
		return (PlanCrossInfo) this.baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_PLANCROSSINFO_FOR_PLANCROSSID,
				planCrossId);
	}

	/**
	 * 查找加载日期及经由局相关的所有列车开行计划
	 * 
	 * @param bureau
	 * @param runDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PlanTrainModel> findPlanTrainListByBureauAndRunDate(
			String bureau, Date runDate) {
		// 开行日期当天第一分钟
		Date sourceTodayDate = StringAndTimeUtil
				.computerLateDate(runDate, 0, 1);
		// 开行日期前一天最后一分钟
		Date sourceYestodayDate = StringAndTimeUtil.computerLateDate(runDate,
				0, -1);
		// 开行日期后一天开始第0分钟
		Date targetTomorrowDate = StringAndTimeUtil.computerLateDate(runDate,
				1, 0);
		// 参数初始化
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("bureau", bureau);
		paramMap.put("sourceYestodayDate",
				StringAndTimeUtil.yearMonthDayHourMinuteSecondSdf
						.format(sourceYestodayDate));
		paramMap.put("sourceTodayDate",
				StringAndTimeUtil.yearMonthDayHourMinuteSecondSdf
						.format(sourceTodayDate));
		paramMap.put("targetTomorrowDate",
				StringAndTimeUtil.yearMonthDayHourMinuteSecondSdf
						.format(targetTomorrowDate));

		return baseDao.selectListBySql(
				Constants.TRAINPLANDAO_FIND_PLANTRAINLIST_BY_PASS_BUREAU,
				paramMap);
	}

	/**
	 * 判定开行计划经由站是否符合条件
	 * 
	 * @param bureau
	 * @param runDate
	 * @param planTrainId
	 * @return
	 */
	public CompareModel findCompareModelByBureauAndRunDate(String bureau,
			Date runDate, String planTrainId) {
		CompareModel compareModel = null;
		// 参数初始化
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("bureau", bureau);
		paramMap.put("planTrainId", planTrainId);

		compareModel = (CompareModel) baseDao.selectOneBySql(
				Constants.TRAINPLANDAO_FIND_COMPAREMODEL_BY_BUREAU, paramMap);
		if (compareModel != null
				&& StringAndTimeUtil.isStartAndTargetDateInRunDate(
						compareModel.getMinArriveDate(),
						compareModel.getMaxDepartDate(), runDate)) {

			return compareModel;
		} else {
			return null;
		}
	}

	/**
	 * 查询指定交路计划ID对应的交路计划,同时查询交路与调度台关系表,生成岗位ID和岗位名称
	 * 
	 * @param planCrossId
	 *            交路计划ID
	 * @return
	 */
	public PlanCrossModel findPlanCrossByPlanCrossId(String planCrossId) {
		PlanCrossModel planCross = null;

		if (planCrossId != null && !planCrossId.trim().equals("")) {

			for (Object object : baseDao.selectListBySql(
					Constants.TRAINPLANDAO_FIND_PLANCROSS_BY_PLANCROSSID, null)) {
				PlanCrossModel crossModel = (PlanCrossModel) object;
				if (crossModel != null && crossModel.getPlanCrossId() != null
						&& crossModel.getPlanCrossId().equals(planCrossId)) {
					planCross = crossModel;
					break;
				}

			}
		}
		return planCross;
	}

	/**
	 * 查询指定列车开行计划ID对应的列车开行计划
	 * 
	 * @param planTrainId
	 *            列车开行计划ID
	 * @return
	 */
	public PlanTrainModel findPlanTrainListByPlanTrainId(String planTrainId) {
		PlanTrainModel planTrain = null;

		if (planTrainId != null && !planTrainId.trim().equals("")) {
			planTrain = (PlanTrainModel) baseDao.selectOneBySql(
					Constants.TRAINPLANDAO_FIND_PLANTRAIN_BY_PLANTRAINID,
					planTrainId);
		}
		return planTrain;
	}

	/**
	 * 查询指定列车开行计划ID对应的列车开行计划
	 * 
	 * @param planTrainId
	 *            列车开行计划ID
	 * @return
	 */
	public PlanTrainModel findPlanTrainListByPlanTrainId2(String planTrainId) {
		PlanTrainModel planTrain = null;

		if (planTrainId != null && !planTrainId.trim().equals("")) {
			planTrain = (PlanTrainModel) baseDao.selectOneBySql(
					Constants.TRAINPLANDAO_FIND_PLANTRAIN_BY_PLANTRAINID2,
					planTrainId);
		}
		return planTrain;
	}

	/**
	 * 根据车次名及列车序在已知的集合中从后向前查找
	 * 
	 * @param trainName
	 * @param trainSort
	 * @param planTrainList
	 * @return
	 */
	public PlanTrainModel searchPlanTrainFromList(String trainName,
			int trainSort, List<PlanTrainModel> planTrainList) {

		PlanTrainModel result = null;

		for (int i = planTrainList.size() - 1; i > -1; i--) {
			PlanTrainModel train = planTrainList.get(i);
			if (train.getTrainNbr() != null
					&& train.getTrainNbr().equals(trainName)
					&& train.getTrainSort().intValue() == trainSort) {
				result = train;
				break;
			}
		}
		return result;
	}

	/**
	 * 根据当前列车开行计划的前序ID查找前序列车开行计划
	 * 
	 * @param trainName
	 *            车次名
	 * @param trainSort
	 *            车序
	 * @param train
	 * @return
	 */
	public PlanTrainModel searchPrePlanTrainFromDB(String trainName,
			int trainSort, PlanTrainModel train) {
		PlanTrainModel preTrain = null;

		if (train.getPreTrainId() != null && !train.getPreTrainId().equals("")) {
			preTrain = findPlanTrainListByPlanTrainId(train.getPreTrainId());
		}

		if (preTrain.getTrainNbr().equals(trainName)
				&& preTrain.getTrainSort().intValue() == trainSort) {
			return preTrain;
		} else {
			return searchPrePlanTrainFromDB(trainName, trainSort, preTrain);
		}

	}

	/**
	 * 查找后序车
	 * 
	 * @param train
	 * @param planTrainList
	 * @return
	 */
	public PlanTrainModel searchNextPlanTrain(PlanTrainModel train,
			List<PlanTrainModel> planTrainList) {
		PlanTrainModel nextTrain = null;

		if (train.getNextTrainId() != null
				&& !train.getNextTrainId().equals("")) {

			for (PlanTrainModel planTrain : planTrainList) {
				if (planTrain.getPlanTrainId().equals(train.getNextTrainId())) {
					nextTrain = planTrain;
					break;
				}
			}

			if (nextTrain == null) {
				nextTrain = findPlanTrainListByPlanTrainId(train
						.getNextTrainId());
			}

		}
		return nextTrain;
	}

	/**
	 * 生成高铁交路计划--图定
	 * 
	 * @param planCross
	 * @param bureuaCode
	 * @param runDate
	 * @return
	 */
	public HighlineCrossInfo generatorHighlineCrossByPlanCross(
			PlanCrossModel planCross, String bureuaCode, Date runDate) {
		HighlineCrossInfo highlineCross = new HighlineCrossInfo();
		try {
			BeanUtils.copyProperties(highlineCross, planCross);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		highlineCross.setHighLineCrossId(UUID.randomUUID().toString());
		PlanTrainModel firstTrain = planCross.getTrainList().get(0);
		PlanTrainModel lastTrain = planCross.getTrainList().get(
				planCross.getTrainList().size() - 1);
		highlineCross.setCrossStartDate(firstTrain.getRunDate() == null ? ""
				: firstTrain.getRunDate());
		highlineCross.setCrossEndDate(lastTrain.getRunDate() == null ? ""
				: lastTrain.getRunDate());
		highlineCross.setCrossStartStn(firstTrain.getStartStn() == null ? ""
				: firstTrain.getStartStn());
		highlineCross.setCrossEndStn(lastTrain.getEndStn() == null ? ""
				: lastTrain.getEndStn());
		highlineCross
				.setCreateReason(ConstantUtil.HIGHLINE_CROSS_CREAT_REASON_BASEMAP);
		highlineCross.setCrossDate(StringAndTimeUtil.yearMonthDaySimpleSdf
				.format(runDate));
		highlineCross.setCrossBureau(bureuaCode);
		return highlineCross;
	}

	/**
	 * 生成高铁交路计划--临客
	 * 
	 * @param planTrain
	 * @return
	 */
	public HighlineCrossInfo generatorHighlineCrossByPlanTrain(
			PlanTrainModel planTrain, String bureuaCode, Date runDate) {
		HighlineCrossInfo highlineCross = new HighlineCrossInfo();
		highlineCross.setHighLineCrossId(UUID.randomUUID().toString());
		highlineCross.setCrossName(planTrain.getTrainNbr());
		highlineCross.setCrossStartDate(planTrain.getRunDate());
		highlineCross.setCrossEndDate(planTrain.getRunDate());
		highlineCross.setCrossStartStn(planTrain.getStartStn());
		highlineCross.setCrossEndStn(planTrain.getEndStn());
		highlineCross.setSpareFlag(planTrain.getSpareFlag() + "");

		highlineCross
				.setTokenPsgBureau(planTrain.getTokenPsgBureau() == null ? ""
						: planTrain.getTokenPsgBureau());
		highlineCross.setCrossBureau(bureuaCode);
		highlineCross.setCrossDate(StringAndTimeUtil.yearMonthDaySimpleSdf
				.format(runDate));
		highlineCross.setCreateReason(planTrain.getCreateReason());
		String tokenVehBureau = planTrain.getTokenVehBureau();
		highlineCross.setTokenVehBureau(ljjcTOljpym(tokenVehBureau));
		// relevantBureau:京上济
		String relevantBureau = planTrain.getRelevantBureau();
		highlineCross.setRelevantBureau(ljjcTOljpym(relevantBureau));
		return highlineCross;
	}

	private String ljjcTOljpym(String bureauShortName) {
		char[] charArray = bureauShortName.toCharArray();
		StringBuffer rb = new StringBuffer();
		rb.append("");
		for (char c : charArray) {
			rb.append(commonService.getLJPYMbyLJJC(c + ""));
		}
		return rb.toString();
	}

	/**
	 * 生成高铁交路计划列车数据
	 * 
	 * @param highlineCross
	 * @param trainList
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<HighLineCrossTrainInfo> generatorHighlineCrossTrainList(
			HighlineCrossInfo highlineCross, List<PlanTrainModel> trainList) {
		// TODO Auto-generated method stub
		List<HighLineCrossTrainInfo> crossTrainList = new ArrayList<HighLineCrossTrainInfo>();

		for (PlanTrainModel planTrain : trainList) {
			HighLineCrossTrainInfo crossTrain = new HighLineCrossTrainInfo();
			try {
				BeanUtils.copyProperties(crossTrain, planTrain);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			crossTrain.setHighLineTrainId(UUID.randomUUID().toString());
			crossTrain.setHighLineCrossId(highlineCross.getHighLineCrossId());
			crossTrainList.add(crossTrain);
		}
		// 存库
		batchAddHighlineCrossTrain(crossTrainList);
		return crossTrainList;
	}

	/**
	 * 加载高铁交路
	 * 
	 * @param bureauName
	 * @param bureauCode
	 * @param startDate
	 * @return
	 * @throws ParseException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<HighlineCrossInfo> createHighLineCross(String bureauName,
			String bureauCode, String startDate) throws ParseException,
			IllegalAccessException, InvocationTargetException {
		// 开行日期初始化
		Date runDate = DateUtil.parseDate(startDate, "yyyyMMdd");
		// 2015-4-2 12:01:24 和龙哥确定后删除
		// step0: 删除开行日期下的计划
		// deleteHighlienCrossByCrossDate(startDate, bureauCode);
		// deleteHighlienCrossTrainByCrossDate(startDate, bureauCode);

		logger.info("HighlineService-createHighLineCross(444H)删除数据");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("runDate", DateUtil.getStringFromDate(runDate,"yyyyMMdd"));
		map.put("bureauCode", bureauCode);
		cmdPlanDao.deleteHighlineDataByRundateAndBureauCode(map);
		//li long jar
//		HighLinePlanGeneratorService.getInstance().deleteHighlineDataByRundateAndBureauCode(runDate, bureauCode);
		logger.info("HighlineService-createHighLineCross(446H)加载数据");
		List<HighlineCrossCmd> highlineDatas = highLinePlanGeneratorServiceLocal.loadHighlineData(bureauName, bureauCode, runDate,DateUtil.getStringFromDate(runDate,"yyyyMMdd"));
		logger.info("HighlineService-createHighLineCross(448H)数据长度：：" + highlineDatas.size());
		logger.info("HighlineService-createHighLineCross(449H)接口操作完毕");
		List<HighlineCrossInfo> highlineCrossList = new ArrayList<HighlineCrossInfo>();
//		BeanUtils.copyProperties(highlineCrossList, highlineDatas);
//		logger.info("HighlineService-createHighLineCross(453H)数据克隆后：：" + highlineCrossList.size());
		
		
		// List<HighlineCrossInfo> highlineCrossList =
		// List<HighlineCrossInfo> highlineCrossList = new
		// ArrayList<HighlineCrossInfo>();
		//
		// List<PlanTrainModel> trainList = findPlanTrainListByBureauAndRunDate(
		// bureauName, runDate);
		//
		// // 图定交路集合
		// List<PlanCrossModel> tdCrossList = new ArrayList<PlanCrossModel>();
		// // 临客列车集合
		// List<PlanTrainModel> cmdTrainList = new ArrayList<PlanTrainModel>();
		//
		// HashMap<String, List<PlanTrainModel>> planCrossTrainMap = new
		// HashMap<String, List<PlanTrainModel>>();
		//
		// for (PlanTrainModel train : trainList) {
		//
		// // step2: 判定开行计划经由站是否符合条件
		// if (findCompareModelByBureauAndRunDate(bureauName, runDate,
		// train.getPlanTrainId()) == null) {
		// continue;
		// }
		//
		// // step3: 图定开行计划按交路分组
		// if (train.getPlanCrossId() != null
		// && !train.getPlanCrossId().trim().equals("")) {
		//
		// if (planCrossTrainMap.containsKey(train.getPlanCrossId())) {
		// planCrossTrainMap.get(train.getPlanCrossId()).add(train);
		// } else {
		// List<PlanTrainModel> tempList = new ArrayList<PlanTrainModel>();
		// tempList.add(train);
		// planCrossTrainMap.put(train.getPlanCrossId(), tempList);
		// }
		// } else {
		// cmdTrainList.add(train);
		// }
		// }
		// // step4: 查找交路中的始发车
		// Iterator<String> iterator = planCrossTrainMap.keySet().iterator();
		// while (iterator.hasNext()) {
		// String planCrossId = iterator.next();
		// PlanCrossModel cross = findPlanCrossByPlanCrossId(planCrossId);
		// // 按照始发时间先后对列车排序
		// List<PlanTrainModel> mapTrainList = SortUtil
		// .sortPlanTrainAsStartTime(planCrossTrainMap
		// .get(planCrossId));
		// List<PlanTrainModel> crossTrainList = new
		// ArrayList<PlanTrainModel>();
		// if (cross != null) {
		// String crossName = cross.getCrossName();
		// List<String> trainNameList = StringAndTimeUtil
		// .getSplitedString(crossName,
		// ConstantUtil.CROSSNAME_TOKEN);
		// String firstTrainName = trainNameList.get(0);
		// // 查找始发车--先从已知队列中找，没有再从数据库中查找
		// PlanTrainModel firstTrain = searchPlanTrainFromList(
		// firstTrainName, 1, mapTrainList);
		//
		// if (firstTrain == null) {
		// firstTrain = searchPrePlanTrainFromDB(firstTrainName, 1,
		// mapTrainList.get(0));
		// }
		// crossTrainList.add(firstTrain);
		//
		// PlanTrainModel nowTrain = null;
		// PlanTrainModel nextTrain = null;
		// // step5: 根据始发车依此查找出交路中的后续列车开行
		// for (int j = 1; j < trainNameList.size(); j++) {
		//
		// if (j == 1) {
		// nowTrain = firstTrain;
		// }
		// nextTrain = searchNextPlanTrain(nowTrain, mapTrainList);
		// crossTrainList.add(nextTrain);
		// nowTrain = nextTrain;
		// }
		// cross.setTrainList(crossTrainList);
		// tdCrossList.add(cross);
		// }
		//
		// }
		// // step6: 生成图定高铁计划
		// for (PlanCrossModel planCross : tdCrossList) {
		// HighlineCrossInfo highlineCross = generatorHighlineCrossByPlanCross(
		// planCross, bureauCode, runDate);
		// highlineCrossList.add(highlineCross);
		// generatorHighlineCrossTrainList(highlineCross,
		// planCross.getTrainList());
		// }
		//
		// // step7: 生成临客高铁计划
		// for (PlanTrainModel planTrain : cmdTrainList) {
		// PlanTrainModel planTraindb =
		// findPlanTrainListByPlanTrainId2(planTrain.getPlanTrainId());
		// HighlineCrossInfo highlineCross = generatorHighlineCrossByPlanTrain(
		// planTraindb, bureauCode, runDate);
		// highlineCrossList.add(highlineCross);
		// List<PlanTrainModel> list = new ArrayList<PlanTrainModel>();
		// list.add(planTraindb);
		// generatorHighlineCrossTrainList(highlineCross, list);
		// }
		//
		// if(highlineCrossList != null && !highlineCrossList.isEmpty()){
		// batchAddHighlineCross(highlineCrossList);
		// }

		return highlineCrossList;

	}

	/**
	 * 批量向表highline_cross表中插入数据
	 * 
	 * @param hList
	 * @return
	 */
	public int batchAddHighlineCross(List<HighlineCrossInfo> hList) {
		Map<String, Object> hMap = new HashMap<String, Object>();
		hMap.put("hList", hList);
		return this.baseDao.insertBySql(Constants.CROSSDAO_ADD_HIGHLINE_CROSS,
				hMap);
	}
	/**
	 *向表highline_cross表中插入数据
	 * 
	 * @param 
	 * @return
	 */
	public int addHighlineCrossSingle(HighlineCrossInfo highlineCrossInfo) {
		return this.baseDao.insertBySql(Constants.CROSSDAO_ADD_HIGHLINE_CROSSSINGLE,
				highlineCrossInfo);
	}

	/**
	 * 批量向表highline_cross_train表中插入数据
	 * 
	 * @param tList
	 * @return
	 */
	public int batchAddHighlineCrossTrain(List<HighLineCrossTrainInfo> tList) {
		return this.baseDao.insertBySql(
				Constants.CROSSDAO_ADD_HIGHLINE_CROSS_TRAIN, tList);
	}

	/**
	 * 查询DisRelaCrossPost信息
	 * 
	 * @param planCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DicRelaCrossPost> getDisRelaCrossPostList(
			Map<String, Object> reqMap2) {

		return baseDao.selectListBySql(
				Constants.HIGHLINECROSSDAO_GETDISRELACROSSPOSTLIST, reqMap2);
	}
	
	/**
	 * 查询DicRelaCrossPost信息
	 * 
	 * @param planCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DicThroughLine> getDicThroughLine(
			Map<String, Object> reqMap2) {

		return baseDao.selectListBySql(
				Constants.HIGHLINECROSSDAO_GETDICTHROUGHLINE, reqMap2);
	}
	
	
	/**
	 * 查询highlineCross信息
	 * 
	 * @param planCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HighlineCrossInfo> getHighlineCrossList(
			Map<String, Object> reqMap2) {

		return baseDao.selectListBySql(
				Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_LIST, reqMap2);
	}

	/**
	 * 查询highlineCrossTrain信息
	 * 
	 * @param highlineCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HighLineCrossTrainInfo> getHighlineCrossTrainList(
			String highlineCrossId) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("highlineCrossId", highlineCrossId);
		return baseDao.selectListBySql(
				Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_TRAIN_LIST,
				reqMap);
	}

	/**
	 * 通过highlineCrossId查询 交路下所有列车的始发站，终到站，始发时间和终到时间
	 * 
	 * @param highlineCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HighlineCrossTrainBaseInfo> getHighlineCrossTrainBaseInfoList(
			String highlineCrossId) {
		return baseDao.selectListBySql(
				Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_TRAIN_BASEINFO,
				highlineCrossId);
	}

	/**
	 * 通过highlineCrossId查询该交路下的列车经由站信息
	 * 
	 * @param highlineCrossId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HighlineTrainRunLine> getHighlineTrainTimeForHighlineCrossId(
			String highlineCrossId) {
		List<HighlineTrainRunLine> retList = baseDao
				.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_HIGHLINE_TRAINTIME_FOR_HIGHLINE_CROSSID,
						highlineCrossId);
		// 将路局简称转换为路局拼音码
		for (HighlineTrainRunLine obj : retList) {
			if (obj.getPassBureau() != null && !"".equals(obj.getPassBureau())) {
				StringBuffer sbf = new StringBuffer("");
				char[] cArray = obj.getPassBureau().toCharArray();
				for (char c : cArray) {

					sbf.append(commonService.getLjInfo(String.valueOf(c))
							.getLjpym());
				}

				obj.setPassBureau(sbf.toString());
			}

		}
		return retList;
	}

	/**
	 * 根据highlineCrossId删除表highline_cross_train中数据
	 * 
	 * @param highlineCrossIds
	 *            ,多个id，以逗号分隔
	 * @return 成功删除的数目
	 */
	public int deleteHighlienCrossTrainForHighlineCrossId(
			String highlineCrossIds) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("highlineCrossIds", highlineCrossIds);
		return baseDao.deleteBySql(
				Constants.HIGHLINECROSSDAO_DELETE_HIGHLINECROSSTRAIN_FOR_ID,
				reqMap);
	}

	/**
	 * 根据crossDate删除表highline_cross_train中数据
	 * 
	 * @param crossDate
	 *            ,多个crossDate，以逗号分隔
	 * @return 成功删除的数目
	 */
	public int deleteHighlienCrossTrainByCrossDate(String crossDate,
			String bureauCode) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("crossDate", crossDate);
		reqMap.put("crossBureau", bureauCode);
		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_HIGHLINECROSSTRAIN_BY_CROSSDATE,
				reqMap);
	}

	/**
	 * 根据crossDate删除表highline_cross中数据
	 * 
	 * @param crossDate
	 *            , 多个crossDate，以逗号分隔
	 * @return 成功删除的数目
	 */
	public int deleteHighlienCrossByCrossDate(String crossDate,
			String bureauCode) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("crossDate", crossDate);
		reqMap.put("crossBureau", bureauCode);
		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_HIGHLINECROSS_BY_CROSSDATE, reqMap);
	}

	/**
	 * 根据highlineCrossId删除表highline_cross中数据
	 * 
	 * @param highlineCrossIds
	 *            , 多个id，以逗号分隔
	 * @return 成功删除的数目
	 */
	public int deleteHighlienCrossForHighlineCrossId(String highlineCrossIds) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("highlineCrossIds", highlineCrossIds);
		return baseDao.deleteBySql(
				Constants.HIGHLINECROSSDAO_DELETE_HIGHLINECROSS_FOR_ID, reqMap);
	}

	/**
	 * 根据highlineCrossId更新车底信息
	 * 
	 * @param highlineCrossId
	 * @param vehicle1
	 * @param vehicle2
	 * @return
	 */
	public int updateHighLineVehicle(String highlineCrossId, String vehicle1,
			String vehicle2) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("highlineCrossId", highlineCrossId);
		reqMap.put("vehicle1", vehicle1);
		reqMap.put("vehicle2", vehicle2);
		return baseDao.deleteBySql(
				Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINE_VEHICLE, reqMap);
	}

	/**
	 * 根据时间和局码简称删除highline_cross表中数据
	 * 
	 * @param startDate
	 *            对应数据表highline_cross中CROSS_START_DATE，格式yyyyMMdd
	 * @param crossBureau
	 *            局码简称，比如：京
	 * @return
	 */
	public int cleanHighLineForDate(String startDate, String crossBureau) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("crossBureau", crossBureau);
		reqMap.put("startDate", startDate);
		return baseDao.deleteBySql(
				Constants.HIGHLINECROSSDAO_DELETE_HIGHLINECROSS_FOR_DATE,
				reqMap);
	}

	/**
	 * 更新highlinecross中check的基本信息
	 * 
	 * @param checkType
	 *            审核状态
	 * @param checkPeople
	 *            审核人
	 * @param checkPeopleOrg
	 *            审核人所属单位
	 * @param highlineCrossIds
	 * @return
	 */
	public int updateHiglineCheckInfo(String checkType, String checkPeople,
			String checkPeopleOrg, String highlineCrossIds) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("checkType", checkType);
		reqMap.put("checkPeople", checkPeople);
		reqMap.put("checkPeopleOrg", checkPeopleOrg);
		reqMap.put("highlineCrossIds", highlineCrossIds);
		return baseDao.deleteBySql(
				Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINE_CHECKINFO, reqMap);
	}

	public void updateHighLineTrain(String highLineTrainId,
			String highLineCrossId) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("highLineTrainId", highLineTrainId);
		reqMap.put("highLineCrossId", highLineCrossId);

		baseDao.updateBySql(Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINECROSSID,
				reqMap);

	}

	public List<OptionDto> getVehicles() {

		Map<String, Object> reqMap = new HashMap<String, Object>();

		return baseDao.selectListBySql(Constants.HIGHLINECROSSDAO_GET_VEHICLES,
				reqMap);
	}

	/**
	 * 根据条件查询highline_cross表中数据
	 * 
	 * @param crossInfo
	 * @return
	 */
	public List<HighlineCrossInfo> getHighlineCrossInfo(
			HighlineCrossInfo crossInfo) {
		return baseDao.selectListBySql(
				Constants.HIGHLINECROSSDAO_GET_HIGHLINE_CROSS_INFO, crossInfo);
	}

	/**
	 * 更新表highline_cross中数据
	 * 
	 * @param crossInfo
	 * @return
	 */
	public int updateHighlineCrossInfo(HighlineCrossInfo crossInfo) {
		return baseDao
				.updateBySql(
						Constants.HIGHLINECROSSDAO_UPDATE_HIGHLINE_CROSSINFO,
						crossInfo);
	}

	/**
	 * 根据throughline分组查询highlineCross信息
	 * 
	 * @param crossInfo
	 * @return
	 */
	public List<HighlineThroughlineInfo> getHighlineThroughCrossInfo(
			HighlineCrossInfo crossInfo) {
		return baseDao.selectListBySql(
				Constants.HIGHLINECROSSDAO_GET_HIGHLINE_THROUGH_CROSSINFO,
				crossInfo);
	}

	/**
	 * 根据路局、开行日期yyyy-MM-dd查询高铁开行计划
	 * 
	 * 业务规则：1、当第一次接入日期与界面查询日期不相等 则视为无效数据 需要丢弃 2.1整条记录有效 第一步赋值主体信息 2.2赋值接入、交出信息
	 * 3. 当第一次交出日期与界面查询日期不相等 则视为无效数据 需要丢弃
	 * 
	 * 
	 * @param queryMap
	 *            { 界面参数 key:bureauName 路局简称(必须) key:bureauCode 路局拼音码(必须)
	 *            key:runDate 运行日期yyyy-MM-dd(必须)
	 * 
	 * 
	 *            }
	 * 
	 *            既有线部分
	 * @return
	 */
	public HighLinePlanPageDto getExistLinePlanByBureau(
			Map<String, String> queryMap) throws Exception {
		HighLinePlanPageDto retObj = new HighLinePlanPageDto();
		// {trainTypeShortName=, creatType=LK,

		// stnNameTreeRunType=SF, stnName=北京西
		// stnNameTreeRunType=JR
		// stnNameTreeRunType=JC
		// stnNameTreeRunType=JC
		// 始发交出
		// 接入终到
		// 接入交出
		// 始发终到
		String stnNameTreeRunType = queryMap.get("stnNameTreeRunType");
		if (stnNameTreeRunType == null || stnNameTreeRunType == "") {
			stnNameTreeRunType = "";
		}

		String stnName = queryMap.get("stnName");
		if (stnName == null || stnName == "") {
			stnName = "";
		}
		String trainTypeShortName = queryMap.get("trainTypeShortName");
		if (trainTypeShortName == null || trainTypeShortName == "") {
			trainTypeShortName = "";
		}
		String creatType = queryMap.get("creatType");
		if (creatType == null || creatType == "") {
			creatType = "";
		}

		queryMap.put("trainTypeShortName", "");
		queryMap.put("creatType", "");
		queryMap.put("stnNameTreeRunType", "");
		queryMap.put("stnName", "");
		List<HighLinePlanTrainDto> trainDatas = new ArrayList<HighLinePlanTrainDto>();
		List<HighLinePlanTrainDto> trainDatasFliter = new ArrayList<HighLinePlanTrainDto>();
		List<PlanTrainDto> highLinePlanList = baseDao
				.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_HIGHLINEPLAN_BY_BUREAU,
						queryMap);
		for (PlanTrainDto highLinePlanObj : highLinePlanList) {
			List<PlanTrainStnDto> planTrainStnList = highLinePlanObj
					.getPlanTrainStnList();// 列车经由时刻集合（只包含发到站、分界口）
			// 1.此处判断该行记录是否有效
			// auther suntao
			if (isRemoveRecordJC_JR(highLinePlanObj, planTrainStnList,
					queryMap.get("runDate"), queryMap.get("bureauName"))) {// 接入
				continue;// 无效
			}

			// 3.此处判断该行记录是否有效
			// auther suntao
			// if(highLinePlanObj.getRunTypeCode().contains("JC") &&
			// isRemoveRecordJC_JR(highLinePlanObj,planTrainStnList,
			// queryMap.get("runDate"), queryMap.get("bureauName"))) {//交出
			// continue;//无效
			// }

			// 2.1整条记录有效 第一步赋值主体信息
			HighLinePlanTrainDto pageRowObj = new HighLinePlanTrainDto();
			PropertyUtils.copyProperties(pageRowObj, highLinePlanObj);// 将plan主数据复制给页面对象中对应属性中去

			// 2.2赋值接入、交出信息
			if ("SFZD".equals(highLinePlanObj.getRunTypeCode())) {// 始发终到
				this.fillInfoSFZD(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			} else if ("JRZD".equals(highLinePlanObj.getRunTypeCode())) {// 接入终到
				this.fillInfoJRZD(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"));
			} else if ("SFJC".equals(highLinePlanObj.getRunTypeCode())) {// 始发交出
				this.fillInfoSFJC(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			} else if ("JRJC".equals(highLinePlanObj.getRunTypeCode())) {// 接入交出
				this.fillInfoJRJC(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			}

			trainDatas.add(pageRowObj);
		}
		// {trainTypeShortName=, creatType=LK,

		if (creatType != "" && trainTypeShortName == "") {
			for (HighLinePlanTrainDto planTrainDto : trainDatas) {

				if (planTrainDto.getCreatType().equals(creatType)) {
					trainDatasFliter.add(planTrainDto);
				}

			}
			retObj.setTrainDatas(trainDatasFliter);
		}

		if (creatType != "" && trainTypeShortName != "") {

			for (HighLinePlanTrainDto planTrainDto : trainDatas) {
				// 为什么加这个判断,因为用户太傻,总有数据录不全
				if (null != planTrainDto.getTrainTypeShortName()
						&& null != planTrainDto.getCreatType()) {
					if ("OTHER".equals(trainTypeShortName)) {
						// 'K','T','Y','Z','回场','回送','通勤','F' 具体的other包括什么 需要确认
						if (!(planTrainDto.getTrainTypeShortName().equals("K")
								|| planTrainDto.getTrainTypeShortName().equals(
										"T")
								|| planTrainDto.getTrainTypeShortName().equals(
										"Y")
								|| planTrainDto.getTrainTypeShortName().equals(
										"Z")
								|| planTrainDto.getTrainTypeShortName().equals(
										"回场")
								|| planTrainDto.getTrainTypeShortName().equals(
										"回送")
								|| planTrainDto.getTrainTypeShortName().equals(
										"通勤")
								|| planTrainDto.getTrainTypeShortName().equals(
										"F") || planTrainDto
								.getTrainTypeShortName().equals(""))
								&& planTrainDto.getCreatType()
										.equals(creatType)) {
							trainDatasFliter.add(planTrainDto);
						}
					} else {
						// 2015-1-15 10:49:15 注释,因为需要将空送下的回送/出入场合并.
						// if(planTrainDto.getTrainTypeShortName().equals(trainTypeShortName)&&planTrainDto.getCreatType().equals(creatType)){
						// trainDatasFliter.add(planTrainDto);
						// }
						if (planTrainDto.getCreatType().equals(creatType)) {
							if ("KS".equals(trainTypeShortName)) {
								// 如果前台用户点击的是回送,实际需要的数据是:回送+出入场
								if ("回送".equals(planTrainDto
										.getTrainTypeShortName())
										|| "出入场".equals(planTrainDto
												.getTrainTypeShortName())) {
									trainDatasFliter.add(planTrainDto);
								}
							} else if (planTrainDto.getTrainTypeShortName()
									.equals(trainTypeShortName)) {
								// 如果点击的不是回送,那么还是之前的逻辑
								trainDatasFliter.add(planTrainDto);
							}
						}
					}
				}

			}

			retObj.setTrainDatas(trainDatasFliter);
		}

		// {
		// if(stnNameTreeRunType=="SF"){
		// stnNameTreeRunType="始发";
		// }
		// if(stnNameTreeRunType=="JR"){
		// stnNameTreeRunType="接入";
		// }
		// if(stnNameTreeRunType=="JC"){
		// stnNameTreeRunType="交出";
		// }
		// if(stnNameTreeRunType=="ZD"){
		// stnNameTreeRunType="终到";
		// }
		// }

		if (stnNameTreeRunType != "" && stnName == "") {
			if ("SF".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("始发")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JR".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("接入")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JC".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("交出")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("ZD".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("终到")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}

		}
		if (stnNameTreeRunType != "" && stnName != "") {
			if ("SF".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("始发")
							&& planTrainDto.getStartStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JR".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("接入")
							&& planTrainDto.getJrStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JC".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("交出")
							&& planTrainDto.getJcStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("ZD".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("终到")
							&& planTrainDto.getEndStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}

		}
		// stnNameTreeRunType=SF, stnName=北京西
		if (creatType == "" && trainTypeShortName == "" && stnName == ""
				&& stnNameTreeRunType == "") {
			retObj.setTrainDatas(trainDatas);
		}
		this.existFillTreeData(retObj, queryMap.get("bureauName"),
				queryMap.get("bureauCode"), queryMap.get("runDate"),
				queryMap.get("trainNbr"), queryMap.get("runTypeCode"),
				queryMap.get("highlineFlag"));

		return retObj;
	}

	public HighLinePlanPageDto getExistLinePlanByBureau1(
			Map<String, String> queryMap) throws Exception {
		HighLinePlanPageDto retObj = new HighLinePlanPageDto();

		List<HighLinePlanTrainDto> trainDatas = new ArrayList<HighLinePlanTrainDto>();
		List<PlanTrainDto> highLinePlanList = baseDao
				.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_HIGHLINEPLAN_BY_BUREAU,
						queryMap);
		System.out.println("--------------------------------------------");
		for (PlanTrainDto highLinePlanObj : highLinePlanList) {
			List<PlanTrainStnDto> planTrainStnList = highLinePlanObj
					.getPlanTrainStnList();// 列车经由时刻集合（只包含发到站、分界口）
			// 1.此处判断该行记录是否有效
			if (isRemoveRecordJC_JR(highLinePlanObj, planTrainStnList,
					queryMap.get("runDate"), queryMap.get("bureauName"))) {// 接入
				continue;// 无效
			}

			// 3.此处判断该行记录是否有效
			// if(highLinePlanObj.getRunTypeCode().contains("JC") &&
			// isRemoveRecordJC_JR(highLinePlanObj,planTrainStnList,
			// queryMap.get("runDate"), queryMap.get("bureauName"))) {//交出
			// continue;//无效
			// }

			// 2.1整条记录有效 第一步赋值主体信息
			HighLinePlanTrainDto pageRowObj = new HighLinePlanTrainDto();
			PropertyUtils.copyProperties(pageRowObj, highLinePlanObj);// 将plan主数据复制给页面对象中对应属性中去

			// 2.2赋值接入、交出信息
			if ("SFZD".equals(highLinePlanObj.getRunTypeCode())) {// 始发终到
				this.fillInfoSFZD(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			} else if ("JRZD".equals(highLinePlanObj.getRunTypeCode())) {// 接入终到
				this.fillInfoJRZD(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"));
			} else if ("SFJC".equals(highLinePlanObj.getRunTypeCode())) {// 始发交出
				this.fillInfoSFJC(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			} else if ("JRJC".equals(highLinePlanObj.getRunTypeCode())) {// 接入交出
				this.fillInfoJRJC(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			}

			trainDatas.add(pageRowObj);
		}
		retObj.setTrainDatas(trainDatas);

		return retObj;
	}

	/**
	 * 
	 * @param fillObj
	 *            需要赋值树data的对象
	 * @param bureauName
	 *            路局拼音码
	 * @param bureauCode
	 *            路局简称
	 * @param runDate
	 *            运行日期yyyy-MM-dd(必须)
	 * @param trainNbr
	 *            车次
	 * @param runTypeCode
	 *            (SFZD：始发终到 SFJC：始发交出 JRZD：接入交出 JRJC：接入终到)
	 * @throws Exception
	 */
	private void fillTreeData(HighLinePlanPageDto fillObj, String bureauName,
			String bureauCode, String runDate, String trainNbr,
			String runTypeCode, String highlineFlag) throws Exception {
		List<HighLinePlanTreeDto> trainTypeTreeNodes = new ArrayList<HighLinePlanTreeDto>();// 列车类型树data
		List<HighLinePlanTreeDto> stnNameTreeNodes = new ArrayList<HighLinePlanTreeDto>();// 发到站及分界口树data

		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("bureauName", bureauName);
		queryMap.put("bureauCode", bureauCode);
		queryMap.put("runDate", runDate);
		queryMap.put("highlineFlag", highlineFlag);
		if (trainNbr != null && !"null".equals(trainNbr)
				&& !"".equals(trainNbr.trim())) {
			queryMap.put("trainNbr", trainNbr);
		}
		if (runTypeCode != null && !"null".equals(runTypeCode)
				&& !"".equals(runTypeCode.trim())) {
			queryMap.put("runTypeCode", runTypeCode);
		}

		int trainTypeCount = 0;// 列车类型树总合计
		int stnTreeCount = 0;// 发到站及分界口树总合计
		int TDCount = 0, LKCount = 0, GCount = 0, CCount = 0, DCount = 0;
		int DJCount = 0, CRCCount = 0, HSCount = 0;
		int TQCount = 0, ZFCount = 0, OtherCount = 0;
		// 发到站及分界口树
		Map<String, Integer> sfMap = new HashMap<String, Integer>();// 始发map
		Map<String, Integer> zdMap = new HashMap<String, Integer>();// 终到map
		Map<String, Integer> jrMap = new HashMap<String, Integer>();// 接入map
		Map<String, Integer> jcMap = new HashMap<String, Integer>();// 交出map
		Map<String, String> map = queryMap;
		// map.put("stnNameTreeRunType", "");
		HighLinePlanPageDto zongshuju = getExistLinePlanByBureau1(queryMap);

		List<PlanTrainDto> highLinePlanList = baseDao
				.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_HIGHLINEPLAN_BY_BUREAU,
						queryMap);
		if (highLinePlanList != null) {
			for (PlanTrainDto obj : highLinePlanList) {
				// 1.此处判断该行记录是否有效
				if (isRemoveRecordJC_JR(obj, obj.getPlanTrainStnList(),
						runDate, bureauName)) {// 接入
					continue;// 第一次接入日期不等于查询日期则视为无效
				}

				// 3.此处判断该行记录是否有效
				// if(obj.getRunTypeCode().contains("JC") &&
				// isRemoveRecordJC_JR(obj,obj.getPlanTrainStnList(), runDate,
				// bureauName)) {//交出
				// continue;//无效
				// }

				if (null != obj.getTrainTypeShortName()
						&& "TD".equals(obj.getCreatType())) {
					// 统计各列车类型总数
					if ("LK".equals(obj.getCreatType())) {
						LKCount++;
					} else if ("TD".equals(obj.getCreatType())) {
						TDCount++;
					}
					if ("G".equals(obj.getTrainTypeShortName().toUpperCase())) {// 高速动车组旅客列车
						GCount++;
					} else if ("C".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// 城际动车组旅客列车
						CCount++;
					} else if ("D".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// 动车组旅客列车
						DCount++;
					} else if ("DJ".equals(obj.getTrainTypeShortName()
							.toUpperCase())
							|| "动检".equals(obj.getTrainTypeShortName()
									.toUpperCase())) {// 动车组检测车
						DJCount++;
					} else if ("回场".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "回场":回送出入场车底列车
						CRCCount++;
					} else if ("回送".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "回送"://回送图定客车底
						HSCount++;
					} else if ("通勤".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "通勤":通勤列车
						TQCount++;
					} else if ("F".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "F"://因故折返旅客列车
						ZFCount++;
					} else {// OTHER
						OtherCount++;
					}
				}

				// //解析发到站及分界口
				// RunTypeCode 运行方式 (SFJC：始发交出 JRZD：接入终到 JRJC：接入交出 SFZD：始发终到)
				// int fjkIndex = 0;
				// for(int i=0;i<obj.getPlanTrainStnList().size();i++) {
				// PlanTrainStnDto stnObj = obj.getPlanTrainStnList().get(i);
				// if("SFZ".equals(stnObj.getIsfdz()) &&
				// bureauName.equals(obj.getStartBureau())){//只统计查询局始发的
				// 外局始发查询局接入的数据不纳入始发统计项中
				// if (sfMap.containsKey(stnObj.getStnName())) {
				// sfMap.put(stnObj.getStnName(), sfMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// sfMap.put(stnObj.getStnName(), 1);
				// }
				// }else if("ZDZ".equals(stnObj.getIsfdz()) &&
				// bureauName.equals(obj.getEndBureau())){//只统计查询局终到的
				// 外局终到查询局交出的数据不纳入终到统计项中
				// if (zdMap.containsKey(stnObj.getStnName())) {
				// zdMap.put(stnObj.getStnName(), zdMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// zdMap.put(stnObj.getStnName(), 1);
				// }
				// }
				//
				// if("1".equals(stnObj.getIsfjk())){//是否为分界口
				// fjkIndex ++;
				// if(obj.getRunTypeCode().contains("JR") && fjkIndex%2 !=
				// 0){//序号为奇数
				// if (jrMap.containsKey(stnObj.getStnName())) {
				// jrMap.put(stnObj.getStnName(), jrMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// jrMap.put(stnObj.getStnName(), 1);
				// }
				// }
				//
				// if(obj.getRunTypeCode().contains("JC") && (i+1)%2 ==
				// 0){//序号为偶数
				// if (jcMap.containsKey(stnObj.getStnName())) {
				// jcMap.put(stnObj.getStnName(), jcMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// jcMap.put(stnObj.getStnName(), 1);
				// }
				// }
				// }
				// }

			}
			// 树统计 end
		}

		List<HighLinePlanTrainDto> jisuanfjzj = zongshuju.getTrainDatas();
		for (HighLinePlanTrainDto highLinePlanTrainDto : jisuanfjzj) {
			if (highLinePlanTrainDto.getRunType().contains("始发")) {
				// sfMap.put(stnObj.getStnName(), sfMap.get(stnObj.getStnName())
				// +1);
				if (sfMap.get(highLinePlanTrainDto.getStartStn()) == null) {
					sfMap.put(highLinePlanTrainDto.getStartStn(), 1);
				} else {
					sfMap.put(highLinePlanTrainDto.getStartStn(),
							sfMap.get(highLinePlanTrainDto.getStartStn()) + 1);
				}

			}
			if (highLinePlanTrainDto.getRunType().contains("终到")) {
				// zdMap.put(obj.getStartStn(),zdMap.get(obj.getStartStn())+1);
				if (zdMap.get(highLinePlanTrainDto.getEndStn()) == null) {
					zdMap.put(highLinePlanTrainDto.getEndStn(), 1);
				} else {
					zdMap.put(highLinePlanTrainDto.getEndStn(),
							zdMap.get(highLinePlanTrainDto.getEndStn()) + 1);
				}
			}

			if (highLinePlanTrainDto.getRunType().contains("接入")) {
				// jrMap.put(obj.getStartStn(),jrMap.get(obj.getStartStn())+1);
				if (jrMap.get(highLinePlanTrainDto.getJrStn()) == null) {
					jrMap.put(highLinePlanTrainDto.getJrStn(), 1);
				} else {
					jrMap.put(highLinePlanTrainDto.getJrStn(),
							jrMap.get(highLinePlanTrainDto.getJrStn()) + 1);
				}
			}
			if (highLinePlanTrainDto.getRunType().contains("交出")) {
				// jcMap.put(obj.getStartStn(),sfMap.get(obj.getStartStn())+1);
				if (jcMap.get(highLinePlanTrainDto.getJcStn()) == null) {
					jcMap.put(highLinePlanTrainDto.getJcStn(), 1);
				} else {
					jcMap.put(highLinePlanTrainDto.getJcStn(),
							jcMap.get(highLinePlanTrainDto.getJcStn()) + 1);
				}
			}

			// PlanTrainStnDto stnObj = obj.getPlanTrainStnList().get(i);
			// if("SFZ".equals(stnObj.getIsfdz()) &&
			// bureauName.equals(obj.getStartBureau())){//只统计查询局始发的
			// 外局始发查询局接入的数据不纳入始发统计项中
			// if (sfMap.containsKey(stnObj.getStnName())) {
			// sfMap.put(stnObj.getStnName(), sfMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// sfMap.put(stnObj.getStnName(), 1);
			// }
			// }else if("ZDZ".equals(stnObj.getIsfdz()) &&
			// bureauName.equals(obj.getEndBureau())){//只统计查询局终到的
			// 外局终到查询局交出的数据不纳入终到统计项中
			// if (zdMap.containsKey(stnObj.getStnName())) {
			// zdMap.put(stnObj.getStnName(), zdMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// zdMap.put(stnObj.getStnName(), 1);
			// }
			// }
			//
			// if("1".equals(stnObj.getIsfjk())){//是否为分界口
			// fjkIndex ++;
			// if(obj.getRunTypeCode().contains("JR") && fjkIndex%2 !=
			// 0){//序号为奇数
			// if (jrMap.containsKey(stnObj.getStnName())) {
			// jrMap.put(stnObj.getStnName(), jrMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// jrMap.put(stnObj.getStnName(), 1);
			// }
			// }
			//
			// if(obj.getRunTypeCode().contains("JC") && (i+1)%2 == 0){//序号为偶数
			// if (jcMap.containsKey(stnObj.getStnName())) {
			// jcMap.put(stnObj.getStnName(), jcMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// jcMap.put(stnObj.getStnName(), 1);
			// }
			// }
			// }
			// }
		}

		// 集合循环 end

		// Map<String , String>map= queryMap;
		// map.put("stnNameTreeRunType", "");
		// HighLinePlanPageDto zongshuju = getExistLinePlanByBureau1(queryMap);
		// List<HighLinePlanTrainDto> jisuanfjzj =zongshuju.getTrainDatas();
		// for (HighLinePlanTrainDto highLinePlanTrainDto : jisuanfjzj) {
		// if(highLinePlanTrainDto.getRunType().contains("始发")){
		// // sfMap.put(stnObj.getStnName(), sfMap.get(stnObj.getStnName()) +1);
		// if(sfMap.get(highLinePlanTrainDto.getStartStn())==null){
		// sfMap.put(highLinePlanTrainDto.getStartStn(),1);
		// }else{
		// sfMap.put(highLinePlanTrainDto.getStartStn(),sfMap.get(highLinePlanTrainDto.getStartStn())+1);
		// }
		//
		// }
		// if(highLinePlanTrainDto.getRunType().contains("终到")){
		// // zdMap.put(obj.getStartStn(),zdMap.get(obj.getStartStn())+1);
		// if(zdMap.get(highLinePlanTrainDto.getStartStn())==null){
		// zdMap.put(highLinePlanTrainDto.getStartStn(),1);
		// }else{
		// zdMap.put(highLinePlanTrainDto.getStartStn(),zdMap.get(highLinePlanTrainDto.getStartStn())+1);
		// }
		// }
		//
		// if(highLinePlanTrainDto.getRunType().contains("接入")){
		// // jrMap.put(obj.getStartStn(),jrMap.get(obj.getStartStn())+1);
		// if(jrMap.get(highLinePlanTrainDto.getStartStn())==null){
		// jrMap.put(highLinePlanTrainDto.getStartStn(),1);
		// }else{
		// jrMap.put(highLinePlanTrainDto.getStartStn(),jrMap.get(highLinePlanTrainDto.getStartStn())+1);
		// }
		// }
		// if(highLinePlanTrainDto.getRunType().contains("交出")){
		// // jcMap.put(obj.getStartStn(),sfMap.get(obj.getStartStn())+1);
		// if(jcMap.get(highLinePlanTrainDto.getStartStn())==null){
		// jcMap.put(highLinePlanTrainDto.getStartStn(),1);
		// }else{
		// jcMap.put(highLinePlanTrainDto.getStartStn(),jcMap.get(highLinePlanTrainDto.getStartStn())+1);
		// }
		// }

		// PlanTrainStnDto stnObj = obj.getPlanTrainStnList().get(i);
		// if("SFZ".equals(stnObj.getIsfdz()) &&
		// bureauName.equals(obj.getStartBureau())){//只统计查询局始发的
		// 外局始发查询局接入的数据不纳入始发统计项中
		// if (sfMap.containsKey(stnObj.getStnName())) {
		// sfMap.put(stnObj.getStnName(), sfMap.get(stnObj.getStnName()) +1);
		// } else {
		// sfMap.put(stnObj.getStnName(), 1);
		// }
		// }else if("ZDZ".equals(stnObj.getIsfdz()) &&
		// bureauName.equals(obj.getEndBureau())){//只统计查询局终到的
		// 外局终到查询局交出的数据不纳入终到统计项中
		// if (zdMap.containsKey(stnObj.getStnName())) {
		// zdMap.put(stnObj.getStnName(), zdMap.get(stnObj.getStnName()) +1);
		// } else {
		// zdMap.put(stnObj.getStnName(), 1);
		// }
		// }
		//
		// if("1".equals(stnObj.getIsfjk())){//是否为分界口
		// fjkIndex ++;
		// if(obj.getRunTypeCode().contains("JR") && fjkIndex%2 != 0){//序号为奇数
		// if (jrMap.containsKey(stnObj.getStnName())) {
		// jrMap.put(stnObj.getStnName(), jrMap.get(stnObj.getStnName()) +1);
		// } else {
		// jrMap.put(stnObj.getStnName(), 1);
		// }
		// }
		//
		// if(obj.getRunTypeCode().contains("JC") && (i+1)%2 == 0){//序号为偶数
		// if (jcMap.containsKey(stnObj.getStnName())) {
		// jcMap.put(stnObj.getStnName(), jcMap.get(stnObj.getStnName()) +1);
		// } else {
		// jcMap.put(stnObj.getStnName(), 1);
		// }
		// }
		// }
		// }
		// }

		// //////////////////// 列车类型树
		trainTypeCount = TDCount + LKCount;// 列车类型树总合计 = 图定+临客
		HighLinePlanTreeDto rootTreeNodeTrainType = new HighLinePlanTreeDto(
				"trainTypeTree_all", "", "全部", "trainTypeTree", "", "",
				trainTypeCount, true);// 列车类型树根节点
		trainTypeTreeNodes.add(rootTreeNodeTrainType);
		HighLinePlanTreeDto LKNode = new HighLinePlanTreeDto(
				"trainTypeTree_LK", "trainTypeTree_all", "临客", "trainTypeTree",
				"", "LK", LKCount, true);// 临客节点
		trainTypeTreeNodes.add(LKNode);
		HighLinePlanTreeDto TDNode = new HighLinePlanTreeDto(
				"trainTypeTree_TD", "trainTypeTree_all", "图定", "trainTypeTree",
				"", "TD", TDCount, true);// 图定节点
		trainTypeTreeNodes.add(TDNode);
		HighLinePlanTreeDto GNode = new HighLinePlanTreeDto("trainTypeTree_G",
				"trainTypeTree_TD", "G", "trainTypeTree", "G", "TD", GCount,
				true);// G节点
		trainTypeTreeNodes.add(GNode);
		HighLinePlanTreeDto CNode = new HighLinePlanTreeDto("trainTypeTree_C",
				"trainTypeTree_TD", "C", "trainTypeTree", "C", "TD", CCount,
				true);// C节点
		trainTypeTreeNodes.add(CNode);
		HighLinePlanTreeDto DNode = new HighLinePlanTreeDto("trainTypeTree_D",
				"trainTypeTree_TD", "D", "trainTypeTree", "D", "TD", DCount,
				true);// D节点
		trainTypeTreeNodes.add(DNode);
		HighLinePlanTreeDto DJNode = new HighLinePlanTreeDto(
				"trainTypeTree_DJ", "trainTypeTree_TD", "动检", "trainTypeTree",
				"动检", "TD", DJCount, true);// 动检节点
		trainTypeTreeNodes.add(DJNode);
		// 空送改为:回送,和既有开行中的理由是一样的
		HighLinePlanTreeDto KSNode = new HighLinePlanTreeDto(
				"trainTypeTree_KS", "trainTypeTree_TD", "回送", "trainTypeTree",
				"KS", "TD", CRCCount + HSCount, true);// 空送节点
		trainTypeTreeNodes.add(KSNode);
		// HighLinePlanTreeDto CRCNode = new
		// HighLinePlanTreeDto("trainTypeTree_CRC","trainTypeTree_KS","出入场","trainTypeTree","回场","TD",
		// CRCCount, true);//出入场节点
		// trainTypeTreeNodes.add(CRCNode);
		// HighLinePlanTreeDto HSNode = new
		// HighLinePlanTreeDto("trainTypeTree_HS","trainTypeTree_KS","回送","trainTypeTree","回送","TD",
		// HSCount, true);//回送节点
		// trainTypeTreeNodes.add(HSNode);
		HighLinePlanTreeDto TQNode = new HighLinePlanTreeDto(
				"trainTypeTree_TQ", "trainTypeTree_TD", "通勤", "trainTypeTree",
				"通勤", "TD", TQCount, true);// 通勤节点
		trainTypeTreeNodes.add(TQNode);
		HighLinePlanTreeDto ZFNode = new HighLinePlanTreeDto(
				"trainTypeTree_ZF", "trainTypeTree_TD", "折返", "trainTypeTree",
				"F", "TD", ZFCount, true);// 折返节点
		trainTypeTreeNodes.add(ZFNode);
		HighLinePlanTreeDto OtherNode = new HighLinePlanTreeDto(
				"trainTypeTree_OTHER", "trainTypeTree_TD", "其他",
				"trainTypeTree", "OTHER", "TD", OtherCount, true);// 其他节点
		trainTypeTreeNodes.add(OtherNode);
		fillObj.setTrainTypeTreeNodes(trainTypeTreeNodes);

		// //////////////////////汇总发到站及分界口树统计
		int SFCount = 0, JRCount = 0, JCCount = 0, ZDCount = 0;
		Set<String> sfKeySet = sfMap.keySet();// 获取map的key值的集合，set集合
		for (String key : sfKeySet) {// 遍历key
			SFCount += sfMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_SF_"
					+ key, "stnNameTree_SF", key, "stnNameTree", key, "SF",
					sfMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_SF",
				"stnNameTree_all", "始发", "stnNameTree", "", "SF", SFCount,
				false));

		Set<String> jrKeySet = jrMap.keySet();// 获取map的key值的集合，set集合
		for (String key : jrKeySet) {// 遍历key
			JRCount += jrMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JR_"
					+ key, "stnNameTree_JR", key, "stnNameTree", key, "JR",
					jrMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JR",
				"stnNameTree_all", "接入", "stnNameTree", "", "JR", JRCount,
				false));

		Set<String> jcKeySet = jcMap.keySet();// 获取map的key值的集合，set集合
		for (String key : jcKeySet) {// 遍历key
			JCCount += jcMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JC_"
					+ key, "stnNameTree_JC", key, "stnNameTree", key, "JC",
					jcMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JC",
				"stnNameTree_all", "交出", "stnNameTree", "", "JC", JCCount,
				false));

		Set<String> zdKeySet = zdMap.keySet();// 获取map的key值的集合，set集合
		for (String key : zdKeySet) {// 遍历key
			ZDCount += zdMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_ZD_"
					+ key, "stnNameTree_ZD", key, "stnNameTree", key, "ZD",
					zdMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_ZD",
				"stnNameTree_all", "终到", "stnNameTree", "", "ZD", ZDCount,
				false));

		stnTreeCount = SFCount + JRCount;// 发到站及分界口树总合计 = 始发+接入
		HighLinePlanTreeDto rootTreeNodeStnName = new HighLinePlanTreeDto(
				"stnNameTree_all", "", "全部", "stnNameTree", "", "",
				stnTreeCount, true);// 发到站及分界口树根节点
		stnNameTreeNodes.add(rootTreeNodeStnName);
		fillObj.setStnNameTreeNodes(stnNameTreeNodes);
	}

	/**
	 * 根据路局、开行日期yyyy-MM-dd查询高铁开行计划
	 * 
	 * 业务规则：1、当第一次接入日期与界面查询日期不相等 则视为无效数据 需要丢弃 2.1整条记录有效 第一步赋值主体信息 2.2赋值接入、交出信息
	 * 3. 当第一次交出日期与界面查询日期不相等 则视为无效数据 需要丢弃
	 * 
	 * 
	 * @param queryMap
	 *            { 界面参数 key:bureauName 路局简称(必须) key:bureauCode 路局拼音码(必须)
	 *            key:runDate 运行日期yyyy-MM-dd(必须)
	 * 
	 * 
	 *            }
	 * @return
	 */
	public HighLinePlanPageDto getHighLinePlanByBureau(
			Map<String, String> queryMap) throws Exception {
		HighLinePlanPageDto retObj = new HighLinePlanPageDto();

		String stnNameTreeRunType = queryMap.get("stnNameTreeRunType");
		if (stnNameTreeRunType == null || stnNameTreeRunType == "") {
			stnNameTreeRunType = "";
		}

		String stnName = queryMap.get("stnName");
		if (stnName == null || stnName == "") {
			stnName = "";
		}
		String trainTypeShortName = queryMap.get("trainTypeShortName");
		if (trainTypeShortName == null || trainTypeShortName == "") {
			trainTypeShortName = "";
		}
		String creatType = queryMap.get("creatType");
		if (creatType == null || creatType == "") {
			creatType = "";
		}

		queryMap.put("trainTypeShortName", "");
		queryMap.put("creatType", "");
		queryMap.put("stnNameTreeRunType", "");
		queryMap.put("stnName", "");
		List<HighLinePlanTrainDto> trainDatas = new ArrayList<HighLinePlanTrainDto>();
		List<HighLinePlanTrainDto> trainDatasFliter = new ArrayList<HighLinePlanTrainDto>();
		List<PlanTrainDto> highLinePlanList = baseDao
				.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_HIGHLINEPLAN_BY_BUREAU,
						queryMap);
		for (PlanTrainDto highLinePlanObj : highLinePlanList) {
			List<PlanTrainStnDto> planTrainStnList = highLinePlanObj
					.getPlanTrainStnList();// 列车经由时刻集合（只包含发到站、分界口）
			// 1.此处判断该行记录是否有效
			if (isRemoveRecordJC_JR(highLinePlanObj, planTrainStnList,
					queryMap.get("runDate"), queryMap.get("bureauName"))) {// 接入
				continue;// 无效
			}

			// 3.此处判断该行记录是否有效
			// if(highLinePlanObj.getRunTypeCode().contains("JC") &&
			// isRemoveRecordJC_JR(highLinePlanObj,planTrainStnList,
			// queryMap.get("runDate"), queryMap.get("bureauName"))) {//交出
			// continue;//无效
			// }

			// 2.1整条记录有效 第一步赋值主体信息
			HighLinePlanTrainDto pageRowObj = new HighLinePlanTrainDto();
			PropertyUtils.copyProperties(pageRowObj, highLinePlanObj);// 将plan主数据复制给页面对象中对应属性中去

			// 2.2赋值接入、交出信息
			if ("SFZD".equals(highLinePlanObj.getRunTypeCode())) {// 始发终到
				// this.fillInfoSFZD(pageRowObj, planTrainStnList,
				// queryMap.get("bureauName"), highLinePlanObj.getFjkCount());
				
//				String start = highLinePlanObj.getStartTime();
//				String end = highLinePlanObj.getEndTime();
//				
//				if (start.toString() != null && !"".equals(start.toString())
//						&& !"null".equals(start.toString())) {
//					if ("1".equals(highLinePlanObj.getHighlineFlag())) {
//						if ((DateUtil.isBeforeForyyyyMMddHHmmss(start.toString(),
//								DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
//								.isBeforeForyyyyMMddHHmmss(date + " 00:00:00",
//										start.toString()))) {
//							this.isValid = "1";
//						}
//						
//					} else {
//						if ((DateUtil.isBeforeForyyyyMMddHHmmss(start.toString(),
//								date + " 18:00:00") && DateUtil
//								.isBeforeForyyyyMMddHHmmss(DateUtil.mulDateOneDay(date)
//										+ " 18:00:00", start.toString()))) {// 既有线时间分界
//																		// 18:00:00
//							this.isValid = "1";
//						}
//						
//					}
//				}
				
			} else if ("JRZD".equals(highLinePlanObj.getRunTypeCode())) {// 接入终到
				this.fillInfoJRZD(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"));
			} else if ("SFJC".equals(highLinePlanObj.getRunTypeCode())) {// 始发交出
				this.fillInfoSFJC(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			} else if ("JRJC".equals(highLinePlanObj.getRunTypeCode())) {// 接入交出
				this.fillInfoJRJC(pageRowObj, planTrainStnList,
						queryMap.get("bureauName"),
						highLinePlanObj.getFjkCount());
			}

			trainDatas.add(pageRowObj);
		}
		if (creatType != "" && trainTypeShortName == "") {
			for (HighLinePlanTrainDto planTrainDto : trainDatas) {

				if (planTrainDto.getCreatType().equals(creatType)) {
					trainDatasFliter.add(planTrainDto);
				}

			}
			retObj.setTrainDatas(trainDatasFliter);
		}

		if (creatType != "" && trainTypeShortName != "") {

			for (HighLinePlanTrainDto planTrainDto : trainDatas) {
				// 为什么加这个判断,因为用户太傻,总有数据录不全
				if (null != planTrainDto.getTrainTypeShortName()
						&& null != planTrainDto.getCreatType()) {
					if ("OTHER".equals(trainTypeShortName)) {
						// // 'K','T','Y','Z','回场','回送','通勤','F' 具体的other包括什么
						// 需要确认
						if (!(planTrainDto.getTrainTypeShortName().equals("G")

								|| planTrainDto.getTrainTypeShortName().equals(
										"C")
								|| planTrainDto.getTrainTypeShortName().equals(
										"D")
								|| planTrainDto.getTrainTypeShortName().equals(
										"DJ")
								|| planTrainDto.getTrainTypeShortName().equals(
										"回场")
								|| planTrainDto.getTrainTypeShortName().equals(
										"回送")
								|| planTrainDto.getTrainTypeShortName().equals(
										"通勤")
								|| planTrainDto.getTrainTypeShortName().equals(
										"F") || planTrainDto
								.getTrainTypeShortName().equals(""))
								&& planTrainDto.getCreatType()
										.equals(creatType)) {
							trainDatasFliter.add(planTrainDto);
						}
					} else {
						// 2015-1-15 10:49:15 注释,因为需要将空送下的回送/出入场合并.
						// if(planTrainDto.getTrainTypeShortName().equals(trainTypeShortName)&&planTrainDto.getCreatType().equals(creatType)){
						// trainDatasFliter.add(planTrainDto);
						// }
						if (planTrainDto.getCreatType().equals(creatType)) {
							if ("KS".equals(trainTypeShortName)) {
								// 如果前台用户点击的是回送,实际需要的数据是:回送+出入场
								if ("回送".equals(planTrainDto
										.getTrainTypeShortName())
										|| "出入场".equals(planTrainDto
												.getTrainTypeShortName())) {
									trainDatasFliter.add(planTrainDto);
								}
							} else if (planTrainDto.getTrainTypeShortName()
									.equals(trainTypeShortName)) {
								// 如果点击的不是回送,那么还是之前的逻辑
								trainDatasFliter.add(planTrainDto);
							}
						}
					}
				}

			}

			retObj.setTrainDatas(trainDatasFliter);
		}

		// {
		// if(stnNameTreeRunType=="SF"){
		// stnNameTreeRunType="始发";
		// }
		// if(stnNameTreeRunType=="JR"){
		// stnNameTreeRunType="接入";
		// }
		// if(stnNameTreeRunType=="JC"){
		// stnNameTreeRunType="交出";
		// }
		// if(stnNameTreeRunType=="ZD"){
		// stnNameTreeRunType="终到";
		// }
		// }

		if (stnNameTreeRunType != "" && stnName == "") {
			if ("SF".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("始发")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JR".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("接入")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JC".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("交出")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("ZD".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("终到")) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}

		}
		if (stnNameTreeRunType != "" && stnName != "") {
			if ("SF".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("始发")
							&& planTrainDto.getStartStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JR".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("接入")
							&& planTrainDto.getJrStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("JC".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("交出")
							&& planTrainDto.getJcStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}
			if ("ZD".equals(stnNameTreeRunType)) {
				for (HighLinePlanTrainDto planTrainDto : trainDatas) {

					if (planTrainDto.getRunType().contains("终到")
							&& planTrainDto.getEndStn().equals(stnName)) {
						trainDatasFliter.add(planTrainDto);
					}
				}

				retObj.setTrainDatas(trainDatasFliter);
			}

		}
		// stnNameTreeRunType=SF, stnName=北京西
		if (creatType == "" && trainTypeShortName == "" && stnName == ""
				&& stnNameTreeRunType == "") {
			retObj.setTrainDatas(trainDatas);
		}
		this.fillTreeData(retObj, queryMap.get("bureauName"),
				queryMap.get("bureauCode"), queryMap.get("runDate"),
				queryMap.get("trainNbr"), queryMap.get("runTypeCode"),
				queryMap.get("highlineFlag"));

		return retObj;
	}

	/**
	 * 
	 * @param fillObj
	 *            需要赋值树data的对象
	 * @param bureauName
	 *            路局拼音码
	 * @param bureauCode
	 *            路局简称
	 * @param runDate
	 *            运行日期yyyy-MM-dd(必须)
	 * @param trainNbr
	 *            车次
	 * @param runTypeCode
	 *            (SFZD：始发终到 SFJC：始发交出 JRZD：接入交出 JRJC：接入终到)
	 * @throws Exception
	 */
	private void existFillTreeData(HighLinePlanPageDto fillObj,
			String bureauName, String bureauCode, String runDate,
			String trainNbr, String runTypeCode, String highlineFlag)
			throws Exception {
		List<HighLinePlanTreeDto> trainTypeTreeNodes = new ArrayList<HighLinePlanTreeDto>();// 列车类型树data
		List<HighLinePlanTreeDto> stnNameTreeNodes = new ArrayList<HighLinePlanTreeDto>();// 发到站及分界口树data

		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("bureauName", bureauName);
		queryMap.put("bureauCode", bureauCode);
		queryMap.put("runDate", runDate);
		queryMap.put("highlineFlag", highlineFlag);
		if (trainNbr != null && !"null".equals(trainNbr)
				&& !"".equals(trainNbr.trim())) {
			queryMap.put("trainNbr", trainNbr);
		}
		if (runTypeCode != null && !"null".equals(runTypeCode)
				&& !"".equals(runTypeCode.trim())) {
			queryMap.put("runTypeCode", runTypeCode);
		}

		int trainTypeCount = 0;// 列车类型树总合计
		int stnTreeCount = 0;// 发到站及分界口树总合计
		int TDCount = 0, LKCount = 0, GCount = 0, CCount = 0, DCount = 0;
		int DJCount = 0, CRCCount = 0, HSCount = 0;
		int TQCount = 0, ZFCount = 0, OtherCount = 0, LK_PKCount = 0, LK_PMCount = 0, LK_DJCount = 0;
		int KCount = 0, TCount = 0, ZCount = 0, YCount = 0, LKG_Count = 0, LKC_Count = 0, LKD_Count = 0, LKL_Count = 0;
		// 发到站及分界口树
		Map<String, Integer> sfMap = new HashMap<String, Integer>();// 始发map
		Map<String, Integer> zdMap = new HashMap<String, Integer>();// 终到map
		Map<String, Integer> jrMap = new HashMap<String, Integer>();// 接入map
		Map<String, Integer> jcMap = new HashMap<String, Integer>();// 交出map
		Map<String, String> map = queryMap;
		// map.put("stnNameTreeRunType", "");
		HighLinePlanPageDto zongshuju = getExistLinePlanByBureau1(queryMap);

		List<PlanTrainDto> highLinePlanList = baseDao
				.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_HIGHLINEPLAN_BY_BUREAU,
						queryMap);
		if (highLinePlanList != null) {
			for (PlanTrainDto obj : highLinePlanList) {
				// 1.此处判断该行记录是否有效
				if (isRemoveRecordJC_JR(obj, obj.getPlanTrainStnList(),
						runDate, bureauName)) {// 接入
					continue;// 第一次接入日期不等于查询日期则视为无效
				}

				// 3.此处判断该行记录是否有效
				// if(obj.getRunTypeCode().contains("JC") &&
				// isRemoveRecordJC_JR(obj,obj.getPlanTrainStnList(), runDate,
				// bureauName)) {//交出
				// continue;//无效
				// }

				if (null != obj.getTrainTypeShortName()
						&& "TD".equals(obj.getCreatType())) {
					// 统计各列车类型总数
					if ("LK".equals(obj.getCreatType())) {
						LKCount++;
					} else if ("TD".equals(obj.getCreatType())) {
						TDCount++;
					}
					if ("K".equals(obj.getTrainTypeShortName().toUpperCase())) {//
						KCount++;
					} else if ("T".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {//
						TCount++;
					} else if ("Z".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {//
						ZCount++;
					} else if ("Y".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {//
						YCount++;
					} else if ("回场".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "回场":回送出入场车底列车
						CRCCount++;
					} else if ("回送".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "回送"://回送图定客车底
						HSCount++;
					} else if ("通勤".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "通勤":通勤列车
						TQCount++;
					} else if ("F".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {// "F"://因故折返旅客列车
						ZFCount++;
					} else if ("G".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {
						LKG_Count++;
					} else if ("C".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {
						LKC_Count++;
					} else if ("D".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {
						LKD_Count++;
					} else if ("普快".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {
						LK_PKCount++;
					} else if ("普慢".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {
						LK_PMCount++;
					} else if ("DJ".equals(obj.getTrainTypeShortName()
							.toUpperCase())
							|| "动检".equals(obj.getTrainTypeShortName()
									.toUpperCase())) {
						LK_DJCount++;
					} else if ("L".equals(obj.getTrainTypeShortName()
							.toUpperCase())) {
						LKL_Count++;
					} else {// OTHER
						OtherCount++;
					}
				}

				// //解析发到站及分界口
				// RunTypeCode 运行方式 (SFJC：始发交出 JRZD：接入终到 JRJC：接入交出 SFZD：始发终到)

				// for(int i=0;i<obj.getPlanTrainStnList().size();i++) {
				// PlanTrainStnDto stnObj = obj.getPlanTrainStnList().get(i);
				// if("SFZ".equals(stnObj.getIsfdz()) &&
				// bureauName.equals(obj.getStartBureau())){//只统计查询局始发的
				// 外局始发查询局接入的数据不纳入始发统计项中
				// if (sfMap.containsKey(stnObj.getStnName())) {
				// sfMap.put(stnObj.getStnName(), sfMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// sfMap.put(stnObj.getStnName(), 1);
				// }
				// }else if("ZDZ".equals(stnObj.getIsfdz()) &&
				// bureauName.equals(obj.getEndBureau())){//只统计查询局终到的
				// 外局终到查询局交出的数据不纳入终到统计项中
				// if (zdMap.containsKey(stnObj.getStnName())) {
				// zdMap.put(stnObj.getStnName(), zdMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// zdMap.put(stnObj.getStnName(), 1);
				// }
				// }
				//
				// if("1".equals(stnObj.getIsfjk())){//是否为分界口
				//
				// if(obj.getRunTypeCode().contains("JR") ){//序号为奇数
				// if (jrMap.containsKey(stnObj.getStnName())) {
				// jrMap.put(stnObj.getStnName(), jrMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// jrMap.put(stnObj.getStnName(), 1);
				// }
				// }
				//
				// if(obj.getRunTypeCode().contains("JC")){//序号为偶数
				// if (jcMap.containsKey(stnObj.getStnName())) {
				// jcMap.put(stnObj.getStnName(), jcMap.get(stnObj.getStnName())
				// +1);
				// } else {
				// jcMap.put(stnObj.getStnName(), 1);
				// }
				// }
				// }
				// }
			}

		}

		List<HighLinePlanTrainDto> jisuanfjzj = zongshuju.getTrainDatas();
		for (HighLinePlanTrainDto highLinePlanTrainDto : jisuanfjzj) {
			if (null != highLinePlanTrainDto.getTrainTypeShortName()
					&& null != highLinePlanTrainDto.getCreatType()) {
				if (highLinePlanTrainDto.getRunType().contains("始发")) {
					// sfMap.put(stnObj.getStnName(),
					// sfMap.get(stnObj.getStnName())
					// +1);
					if (sfMap.get(highLinePlanTrainDto.getStartStn()) == null) {
						sfMap.put(highLinePlanTrainDto.getStartStn(), 1);
					} else {
						sfMap.put(
								highLinePlanTrainDto.getStartStn(),
								sfMap.get(highLinePlanTrainDto.getStartStn()) + 1);
					}

				}
				if (highLinePlanTrainDto.getRunType().contains("终到")) {
					// zdMap.put(obj.getStartStn(),zdMap.get(obj.getStartStn())+1);
					if (zdMap.get(highLinePlanTrainDto.getEndStn()) == null) {
						zdMap.put(highLinePlanTrainDto.getEndStn(), 1);
					} else {
						zdMap.put(highLinePlanTrainDto.getEndStn(),
								zdMap.get(highLinePlanTrainDto.getEndStn()) + 1);
					}
				}

				if (highLinePlanTrainDto.getRunType().contains("接入")) {
					// jrMap.put(obj.getStartStn(),jrMap.get(obj.getStartStn())+1);
					if (jrMap.get(highLinePlanTrainDto.getJrStn()) == null) {
						jrMap.put(highLinePlanTrainDto.getJrStn(), 1);
					} else {
						jrMap.put(highLinePlanTrainDto.getJrStn(),
								jrMap.get(highLinePlanTrainDto.getJrStn()) + 1);
					}
				}
				if (highLinePlanTrainDto.getRunType().contains("交出")) {
					// jcMap.put(obj.getStartStn(),sfMap.get(obj.getStartStn())+1);
					if (jcMap.get(highLinePlanTrainDto.getJcStn()) == null) {
						jcMap.put(highLinePlanTrainDto.getJcStn(), 1);
					} else {
						jcMap.put(highLinePlanTrainDto.getJcStn(),
								jcMap.get(highLinePlanTrainDto.getJcStn()) + 1);
					}
				}
			}
			// PlanTrainStnDto stnObj = obj.getPlanTrainStnList().get(i);
			// if("SFZ".equals(stnObj.getIsfdz()) &&
			// bureauName.equals(obj.getStartBureau())){//只统计查询局始发的
			// 外局始发查询局接入的数据不纳入始发统计项中
			// if (sfMap.containsKey(stnObj.getStnName())) {
			// sfMap.put(stnObj.getStnName(), sfMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// sfMap.put(stnObj.getStnName(), 1);
			// }
			// }else if("ZDZ".equals(stnObj.getIsfdz()) &&
			// bureauName.equals(obj.getEndBureau())){//只统计查询局终到的
			// 外局终到查询局交出的数据不纳入终到统计项中
			// if (zdMap.containsKey(stnObj.getStnName())) {
			// zdMap.put(stnObj.getStnName(), zdMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// zdMap.put(stnObj.getStnName(), 1);
			// }
			// }
			//
			// if("1".equals(stnObj.getIsfjk())){//是否为分界口
			// fjkIndex ++;
			// if(obj.getRunTypeCode().contains("JR") && fjkIndex%2 !=
			// 0){//序号为奇数
			// if (jrMap.containsKey(stnObj.getStnName())) {
			// jrMap.put(stnObj.getStnName(), jrMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// jrMap.put(stnObj.getStnName(), 1);
			// }
			// }
			//
			// if(obj.getRunTypeCode().contains("JC") && (i+1)%2 == 0){//序号为偶数
			// if (jcMap.containsKey(stnObj.getStnName())) {
			// jcMap.put(stnObj.getStnName(), jcMap.get(stnObj.getStnName())
			// +1);
			// } else {
			// jcMap.put(stnObj.getStnName(), 1);
			// }
			// }
			// }
			// }
		}

		// //////////////////// 列车类型树
		trainTypeCount = TDCount + LKCount;// 列车类型树总合计 = 图定+临客
		HighLinePlanTreeDto rootTreeNodeTrainType = new HighLinePlanTreeDto(
				"trainTypeTree_all", "", "全部", "trainTypeTree", "", "",
				trainTypeCount, true);// 列车类型树根节点
		trainTypeTreeNodes.add(rootTreeNodeTrainType);
		HighLinePlanTreeDto LKNode = new HighLinePlanTreeDto(
				"trainTypeTree_LK", "trainTypeTree_all", "临客", "trainTypeTree",
				"", "LK", LKCount, true);// 临客节点
		trainTypeTreeNodes.add(LKNode);
		HighLinePlanTreeDto TDNode = new HighLinePlanTreeDto(
				"trainTypeTree_TD", "trainTypeTree_all", "图定", "trainTypeTree",
				"", "TD", TDCount, true);// 图定节点
		trainTypeTreeNodes.add(TDNode);
		// HighLinePlanTreeDto GNode = new
		// HighLinePlanTreeDto("trainTypeTree_G","trainTypeTree_TD","G","trainTypeTree","G","TD",
		// GCount, true);//G节点
		// trainTypeTreeNodes.add(GNode);
		// HighLinePlanTreeDto CNode = new
		// HighLinePlanTreeDto("trainTypeTree_C","trainTypeTree_TD","C","trainTypeTree","C","TD",
		// CCount, true);//C节点
		// trainTypeTreeNodes.add(CNode);
		// HighLinePlanTreeDto DNode = new
		// HighLinePlanTreeDto("trainTypeTree_D","trainTypeTree_TD","D","trainTypeTree","D","TD",
		// DCount, true);//D节点
		// trainTypeTreeNodes.add(DNode);
		// HighLinePlanTreeDto DJNode = new
		// HighLinePlanTreeDto("trainTypeTree_DJ","trainTypeTree_TD","动检","trainTypeTree","DJ","TD",
		// DJCount, true);//动检节点
		// trainTypeTreeNodes.add(DJNode);

		HighLinePlanTreeDto LK_GNode = new HighLinePlanTreeDto(
				"trainTypeTree_G", "trainTypeTree_TD", "G", "trainTypeTree",
				"G", "TD", LKG_Count, true);
		trainTypeTreeNodes.add(LK_GNode);
		HighLinePlanTreeDto LK_CNode = new HighLinePlanTreeDto(
				"trainTypeTree_C", "trainTypeTree_TD", "C", "trainTypeTree",
				"C", "TD", LKC_Count, true);
		trainTypeTreeNodes.add(LK_CNode);
		HighLinePlanTreeDto LK_DNode = new HighLinePlanTreeDto(
				"trainTypeTree_D", "trainTypeTree_TD", "D", "trainTypeTree",
				"D", "TD", LKD_Count, true);
		trainTypeTreeNodes.add(LK_DNode);
		HighLinePlanTreeDto ZNode = new HighLinePlanTreeDto("trainTypeTree_Z",
				"trainTypeTree_TD", "Z", "trainTypeTree", "Z", "TD", ZCount,
				true);
		trainTypeTreeNodes.add(ZNode);
		HighLinePlanTreeDto TNode = new HighLinePlanTreeDto("trainTypeTree_T",
				"trainTypeTree_TD", "T", "trainTypeTree", "T", "TD", TCount,
				true);
		trainTypeTreeNodes.add(TNode);
		HighLinePlanTreeDto KNode = new HighLinePlanTreeDto("trainTypeTree_K",
				"trainTypeTree_TD", "K", "trainTypeTree", "K", "TD", KCount,
				true);
		trainTypeTreeNodes.add(KNode);

		HighLinePlanTreeDto LK_PKNode = new HighLinePlanTreeDto(
				"trainTypeTree_PK", "trainTypeTree_TD", "普快", "trainTypeTree",
				"普快", "TD", LK_PKCount, true);
		trainTypeTreeNodes.add(LK_PKNode);
		HighLinePlanTreeDto LK_PMNode = new HighLinePlanTreeDto(
				"trainTypeTree_PM", "trainTypeTree_TD", "普慢", "trainTypeTree",
				"普慢", "TD", LK_PMCount, true);
		trainTypeTreeNodes.add(LK_PMNode);
		HighLinePlanTreeDto LNode = new HighLinePlanTreeDto("trainTypeTree_L",
				"trainTypeTree_TD", "L", "trainTypeTree", "L", "TD", LKL_Count,
				true);
		trainTypeTreeNodes.add(LNode);
		HighLinePlanTreeDto YNode = new HighLinePlanTreeDto("trainTypeTree_Y",
				"trainTypeTree_TD", "Y", "trainTypeTree", "Y", "TD", YCount,
				true);
		trainTypeTreeNodes.add(YNode);
		HighLinePlanTreeDto LK_DJNode = new HighLinePlanTreeDto(
				"trainTypeTree_DJ", "trainTypeTree_TD", "动检", "trainTypeTree",
				"动检", "TD", LK_DJCount, true);// 空送节点
		trainTypeTreeNodes.add(LK_DJNode);
		// 空送改为:回送,并且隐藏掉了出入场和回送,(将出入场和回送合并到一个里面)
		HighLinePlanTreeDto KSNode = new HighLinePlanTreeDto(
				"trainTypeTree_KS", "trainTypeTree_TD", "回送", "trainTypeTree",
				"KS", "TD", CRCCount + HSCount, true);// 空送节点
		trainTypeTreeNodes.add(KSNode);
		// HighLinePlanTreeDto CRCNode = new
		// HighLinePlanTreeDto("trainTypeTree_CRC","trainTypeTree_KS","出入场","trainTypeTree","回场","TD",
		// CRCCount, true);//出入场节点
		// trainTypeTreeNodes.add(CRCNode);
		// HighLinePlanTreeDto HSNode = new
		// HighLinePlanTreeDto("trainTypeTree_HS","trainTypeTree_KS","回送","trainTypeTree","回送","TD",
		// HSCount, true);//回送节点
		// trainTypeTreeNodes.add(HSNode);
		HighLinePlanTreeDto TQNode = new HighLinePlanTreeDto(
				"trainTypeTree_TQ", "trainTypeTree_TD", "通勤", "trainTypeTree",
				"通勤", "TD", TQCount, true);// 通勤节点
		trainTypeTreeNodes.add(TQNode);
		HighLinePlanTreeDto ZFNode = new HighLinePlanTreeDto(
				"trainTypeTree_ZF", "trainTypeTree_TD", "折返", "trainTypeTree",
				"F", "TD", ZFCount, true);// 折返节点
		trainTypeTreeNodes.add(ZFNode);
		HighLinePlanTreeDto OtherNode = new HighLinePlanTreeDto(
				"trainTypeTree_OTHER", "trainTypeTree_TD", "其他",
				"trainTypeTree", "OTHER", "TD", OtherCount, true);// 其他节点
		trainTypeTreeNodes.add(OtherNode);
		fillObj.setTrainTypeTreeNodes(trainTypeTreeNodes);

		// //////////////////////汇总发到站及分界口树统计
		int SFCount = 0, JRCount = 0, JCCount = 0, ZDCount = 0;
		Set<String> sfKeySet = sfMap.keySet();// 获取map的key值的集合，set集合
		for (String key : sfKeySet) {// 遍历key
			SFCount += sfMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_SF_"
					+ key, "stnNameTree_SF", key, "stnNameTree", key, "SF",
					sfMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_SF",
				"stnNameTree_all", "始发", "stnNameTree", "", "SF", SFCount,
				false));

		Set<String> jrKeySet = jrMap.keySet();// 获取map的key值的集合，set集合
		for (String key : jrKeySet) {// 遍历key
			JRCount += jrMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JR_"
					+ key, "stnNameTree_JR", key, "stnNameTree", key, "JR",
					jrMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JR",
				"stnNameTree_all", "接入", "stnNameTree", "", "JR", JRCount,
				false));

		Set<String> jcKeySet = jcMap.keySet();// 获取map的key值的集合，set集合
		for (String key : jcKeySet) {// 遍历key
			JCCount += jcMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JC_"
					+ key, "stnNameTree_JC", key, "stnNameTree", key, "JC",
					jcMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_JC",
				"stnNameTree_all", "交出", "stnNameTree", "", "JC", JCCount,
				false));

		Set<String> zdKeySet = zdMap.keySet();// 获取map的key值的集合，set集合
		for (String key : zdKeySet) {// 遍历key
			ZDCount += zdMap.get(key);
			stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_ZD_"
					+ key, "stnNameTree_ZD", key, "stnNameTree", key, "ZD",
					zdMap.get(key), false));
		}
		stnNameTreeNodes.add(new HighLinePlanTreeDto("stnNameTree_ZD",
				"stnNameTree_all", "终到", "stnNameTree", "", "ZD", ZDCount,
				false));

		stnTreeCount = SFCount + JRCount;// 发到站及分界口树总合计 = 始发+接入
		HighLinePlanTreeDto rootTreeNodeStnName = new HighLinePlanTreeDto(
				"stnNameTree_all", "", "全部", "stnNameTree", "", "",
				stnTreeCount, true);// 发到站及分界口树根节点
		stnNameTreeNodes.add(rootTreeNodeStnName);
		fillObj.setStnNameTreeNodes(stnNameTreeNodes);
	}

	/**
	 * 说明：接入类型时 业务规则：当第一次接入日期与界面查询日期不相等 则视为无效数据 需要丢弃
	 * 
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param runDate
	 *            界面查询运行日期yyyy-MM-dd
	 * @param bureauName
	 *            界面查询路局简称
	 * @return
	 * @throws Exception
	 */
	private boolean isRemoveRecord(List<PlanTrainStnDto> planTrainStnList,
			String runDate, String bureauName) throws Exception {
		for (PlanTrainStnDto obj : planTrainStnList) {
			if ("1".equals(obj.getIsfjk())) {// 是否为分界口 1：是 0：否
				String jrDate = "";
				if (bureauName.equals(obj.getStnBureau())) {// 车站所属局简称==查询局 简称
					jrDate = DateUtil.format(DateUtil.parseDate(
							obj.getArrTime(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd");// 接入日期为到达时间
				} else {
					jrDate = DateUtil.format(DateUtil.parseDate(
							obj.getDptTime(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd");// 接入日期为出发时间
				}

				if (!runDate.equals(jrDate)) {
					return true;// 第一次接入日期与界面查询日期不匹配 视为无效数据 须丢弃
				}
				return false;// 因只需判断该查询局第一次接入日期，故毋须循环完集合即可返回
			}
		}

		return false;
	}

	/**
	 * 说明：接入类型时 业务规则：当第一次接入日期与界面查询日期不相等 则视为无效数据 需要丢弃
	 * 
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param runDate
	 *            界面查询运行日期yyyy-MM-dd
	 * @param bureauName
	 *            界面查询路局简称
	 * @return
	 * @throws Exception
	 */
	private boolean isRemoveRecordJC_JR(PlanTrainDto highLinePlanObj,
			List<PlanTrainStnDto> planTrainStnList, String date,
			String bureauName) throws Exception {
		Boolean isRemove = true;

		// for(PlanTrainStnDto obj : planTrainStnList) {
		// if("1".equals(obj.getIsfjk())) {//是否为分界口 1：是 0：否

		// String jrDate = "";
		// String jcDate = "";
		// if(bureauName.equals(obj.getStnBureau())) {//车站所属局简称==查询局 简称
		// jrDate = obj.getArrTime();//接入日期为到达时间
		// jcDate = obj.getArrTime();//接入日期为到达时间
		//
		// } else {
		// jrDate = obj.getDptTime();//接入日期为出发时间
		// jrDate = obj.getDptTime();//接入日期为出发时间
		// }
		if ("SFZD".equals(highLinePlanObj.getRunTypeCode())) {
			isRemove = true;// 复位每次之后的结果
			String start = highLinePlanObj.getStartTime();
			String end = highLinePlanObj.getEndTime();
			if (start != null && !"".equals(start) && !"null".equals(start)
					&& end != null && !"".equals(end) && !"null".equals(end)) {
				if ("1".equals(highLinePlanObj.getHighLineFlag())) {
					isRemove = false;// 复位每次之后的结果
				} else {
					if (DateUtil.isBeforeForyyyyMMddHHmmss(start, date
							+ " 18:00:00")
							&& DateUtil.isBeforeForyyyyMMddHHmmss(
									DateUtil.mulDateOneDay(date) + " 18:00:00",
									end))
						isRemove = false;
				}
			}
		}
		if ("SFJC".equals(highLinePlanObj.getRunTypeCode())) {
			isRemove = true;// 复位每次之后的结果
			String start = highLinePlanObj.getStartTime();
			String end = highLinePlanObj.getEndTime();
			for (PlanTrainStnDto obj : planTrainStnList) {
				if ("1".equals(obj.getIsfjk())) {// 是否为分界口 1：是 0：否
					String jcTimeSbf = "";
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局简称==查询局
																// 简称
						jcTimeSbf = obj.getDptTime();// 接入日期为到达时间

					} else {
						jcTimeSbf = obj.getArrTime();// 接入日期为出发时间
					}
					if (jcTimeSbf != null && !"".equals(jcTimeSbf)
							&& !"null".equals(jcTimeSbf)) {
					/*	if ("1".equals(highLinePlanObj.getHighLineFlag())) {// SFJC:比较
																			// date
																			// <
																			// jcTimeSbf
							// date:2014-01-01
							// jrTimeSbf.toString() 2014-01-01 01:01:01
							// gt OK 判断要求：交出时间大于 2014-01-01 00:00:00
							// 并且小于2014-01-02 00:00:00
							if ((DateUtil.isBeforeForyyyyMMddHHmmss(jcTimeSbf,
									DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
									.isBeforeForyyyyMMddHHmmss(date
											+ " 00:00:00", jcTimeSbf))
									|| (DateUtil.isBeforeForyyyyMMddHHmmss(
											highLinePlanObj.getStartTime(),
											DateUtil.addDateOneDay(date)
													+ " 00:00:00") && DateUtil
											.isBeforeForyyyyMMddHHmmss(date
													+ " 00:00:00",
													highLinePlanObj
															.getStartTime()))) {
								isRemove = false;
							}
						} else {
							// date-1 <jctime <date
							// gt OK 判断要求：交出时间大于 2014-01-01 18:00:00
							// 并且小于2014-01-02 18:00:00
							if ((DateUtil.isBeforeForyyyyMMddHHmmss(jcTimeSbf,
									date + " 18:00:00") && DateUtil
									.isBeforeForyyyyMMddHHmmss(
											DateUtil.mulDateOneDay(date)
													+ " 18:00:00", jcTimeSbf))
									|| (DateUtil.isBeforeForyyyyMMddHHmmss(
											highLinePlanObj.getStartTime(),
											date + " 18:00:00") && DateUtil
											.isBeforeForyyyyMMddHHmmss(
													DateUtil.mulDateOneDay(date)
															+ " 18:00:00",
													highLinePlanObj
															.getStartTime()))) {// 既有线时间分界
																				// 18:00:00
								isRemove = false;
							}
						}*/
						
						if ("1".equals(highLinePlanObj.getHighLineFlag())) {// SFJC:比较
							if ((DateUtil.isBeforeForyyyyMMddHHmmss(start,
								DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
								.isBeforeForyyyyMMddHHmmss(date
								+ " 00:00:00", start))) {
								isRemove = false;
							}
						} else {
							if (DateUtil.isBeforeForyyyyMMddHHmmss(start, date
									+ " 18:00:00")
									&& DateUtil.isBeforeForyyyyMMddHHmmss(
											DateUtil.mulDateOneDay(date) + " 18:00:00",
											start))
								isRemove = false;
						}
						
					}
				}
			}
		}
		if ("JRZD".equals(highLinePlanObj.getRunTypeCode())) {
			isRemove = true;// 复位每次之后的结果
			for (PlanTrainStnDto obj : planTrainStnList) {
				if ("1".equals(obj.getIsfjk())) {// 是否为分界口 1：是 0：否
					String jrTimeSbf = "";
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局简称==查询局
																// 简称
						jrTimeSbf = obj.getArrTime();// 接入日期为到达时间

					} else {
						jrTimeSbf = obj.getDptTime();// 接入日期为出发时间
					}

					if (jrTimeSbf != null && !"".equals(jrTimeSbf)
							&& !"null".equals(jrTimeSbf)) {
						/*if ("1".equals(highLinePlanObj.getHighLineFlag())) {// JRZD:比较jrTimeSbf
																			// endTime
							// date:2014-01-01
							// jrTimeSbf 2014-01-01 01:01:01
							if ((DateUtil.isBeforeForyyyyMMddHHmmss(date
									+ " 00:00:00", jrTimeSbf) && DateUtil
										.isBeforeForyyyyMMddHHmmss(jrTimeSbf,
												DateUtil.addDateOneDay(date)
														+ " 00:00:00"))
									|| (DateUtil.isBeforeForyyyyMMddHHmmss(date
											+ " 00:00:00",
											highLinePlanObj.getEndTime()) && DateUtil
												.isBeforeForyyyyMMddHHmmss(
														highLinePlanObj
																.getEndTime(),
														DateUtil.addDateOneDay(date)
																+ " 00:00:00"))) {
								isRemove = false;
							}
						} else {
							// date < jr < date+1
							// ok
							if ((DateUtil.isBeforeForyyyyMMddHHmmss(
									DateUtil.mulDateOneDay(date) + " 18:00:00",
									jrTimeSbf) && DateUtil
									.isBeforeForyyyyMMddHHmmss(jrTimeSbf, date
											+ " 18:00:00"))
									|| (DateUtil.isBeforeForyyyyMMddHHmmss(
											DateUtil.mulDateOneDay(date)
													+ " 18:00:00",
											highLinePlanObj.getEndTime()) && DateUtil
											.isBeforeForyyyyMMddHHmmss(
													highLinePlanObj
															.getEndTime(), date
															+ " 18:00:00"))) {// 既有线时间分界
																				// 18:00:00
								isRemove = false;
							}
						}
						*/
						
						
						
						if ("1".equals(highLinePlanObj.getHighLineFlag())) {// SFJC:比较
							if ((DateUtil.isBeforeForyyyyMMddHHmmss(jrTimeSbf,
								DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
								.isBeforeForyyyyMMddHHmmss(date
								+ " 00:00:00", jrTimeSbf))) {
								isRemove = false;
							}
						} else {
							if (DateUtil.isBeforeForyyyyMMddHHmmss(jrTimeSbf, date
									+ " 18:00:00")
									&& DateUtil.isBeforeForyyyyMMddHHmmss(
											DateUtil.mulDateOneDay(date) + " 18:00:00",
											jrTimeSbf))
								isRemove = false;
						}
				}
			}
		}
		}
		if ("JRJC".equals(highLinePlanObj.getRunTypeCode())) {
			isRemove = true;// 复位每次之后的结果
			for (PlanTrainStnDto obj : planTrainStnList) {
				if ("1".equals(obj.getIsfjk())) {// 是否为分界口 1：是 0：否
					String jrDate = "";
					String jcDate = "";
					boolean isFirstFjkBoolean = true;
					if ("1".equals(obj.getIsfjk())) {
						if (isFirstFjkBoolean) {
							if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																		// 接入时间为到达时间
								jrDate = obj.getArrTime();
							} else {// 车站所属局与查询局相等 接入时间为到达时间
								jrDate = obj.getDptTime();
							}
							isFirstFjkBoolean = false;
						}

						if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																	// 交出时间为出发时间
							jcDate = obj.getDptTime();
						} else {// 车站所属局与查询局相等 接入时间为到达时间
							jcDate = obj.getArrTime();
						}
					}

				/*	if ("1".equals(highLinePlanObj.getHighLineFlag())) {
						if (DateUtil.isBeforeForyyyyMMddHHmmss(jrDate, date
								+ " 23:59:59")
								&& DateUtil.isBeforeForyyyyMMddHHmmss(date
										+ " 00:00:00", jcDate)) {
							isRemove = false;
						}
					} else {
						if (DateUtil.isBeforeForyyyyMMddHHmmss(jrDate, date
								+ " 18:00:00")
								&& DateUtil.isBeforeForyyyyMMddHHmmss(
										DateUtil.mulDateOneDay(date)
												+ " 18:00:00", jcDate)) {// 既有线时间分界
																			// 18:00:00
							isRemove = false;
						}
					}*/
					if ("1".equals(highLinePlanObj.getHighLineFlag())) {// SFJC:比较
						if ((DateUtil.isBeforeForyyyyMMddHHmmss(jrDate,
							DateUtil.addDateOneDay(date) + " 00:00:00") && DateUtil
							.isBeforeForyyyyMMddHHmmss(date
							+ " 00:00:00", jrDate))) {
							isRemove = false;
						}
					} else {
						if (DateUtil.isBeforeForyyyyMMddHHmmss(jrDate, date
								+ " 18:00:00")
								&& DateUtil.isBeforeForyyyyMMddHHmmss(
										DateUtil.mulDateOneDay(date) + " 18:00:00",
										jrDate))
							isRemove = false;
						}
				}
			}
		}
		return isRemove;
	}

	/**
	 * 始发终到业务解析规则： otherFjkCount==0 视为管内运行车辆 直接return otherFjkCount>0
	 * other出现的次数序号为偶数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]；
	 * other出现的次数序号为奇数：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * 
	 * @param pageRowObj
	 *            页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoSFZD(HighLinePlanTrainDto pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName,
			int otherFjkCount) {
		StringBuffer jrStnSbf = new StringBuffer("");// 接入站
		StringBuffer jrTimeSbf = new StringBuffer("");// 接入时间
		StringBuffer jcStnSbf = new StringBuffer("");// 交出站
		StringBuffer jcTimeSbf = new StringBuffer("");// 交出时间

		if (otherFjkCount == 0) {
			return;
		}

		int otherIndex = 0;
		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("other".equals(obj.getIsfdz())) {
				otherIndex++;
				if (otherIndex % 2 == 0) {// other出现的次数序号为偶数：接入
					jrStnSbf.append(obj.getStnName()).append("/");
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																// 接入时间为到达时间
						jrTimeSbf.append(obj.getArrTime()).append("/");
					} else {// 车站所属局与查询局相等 接入时间为到达时间
						jrTimeSbf.append(obj.getDptTime()).append("/");
					}
				} else {// other出现的次数序号为奇数：交出
					jcStnSbf.append(obj.getStnName()).append("/");
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																// 交出时间为出发时间
						jcTimeSbf.append(obj.getDptTime()).append("/");
					} else {// 车站所属局与查询局相等 接入时间为到达时间
						jcTimeSbf.append(obj.getArrTime()).append("/");
					}
				}
			}
		}
		// 解析结束

		if (!"".equals(jrStnSbf.toString())) {
			pageRowObj
					.setJrStn(jrStnSbf.substring(0, jrStnSbf.lastIndexOf("/")));
		}
		if (!"".equals(jrTimeSbf.toString())) {
			pageRowObj.setJrTime(jrTimeSbf.substring(0,
					jrTimeSbf.lastIndexOf("/")));
		}
		if (!"".equals(jcStnSbf.toString())) {
			pageRowObj
					.setJcStn(jcStnSbf.substring(0, jcStnSbf.lastIndexOf("/")));
		}
		if (!"".equals(jcTimeSbf.toString())) {
			pageRowObj.setJcTime(jcTimeSbf.substring(0,
					jcTimeSbf.lastIndexOf("/")));
		}
	}

	/**
	 * 始发交出业务解析规则： otherFjkCount%2==0 isFjk==1出现的序号为偶数：接入[接入时间：（车站所属局==查询局 取到站时间
	 * 不相等时取出发时间）]； isFjk==1出现的序号为奇数：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * otherFjkCount%2!=0 other出现的次数序号为偶数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]；
	 * other出现的次数序号为奇数：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * 
	 * @param pageRowObj
	 *            页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoSFJC(HighLinePlanTrainDto pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName,
			int otherFjkCount) {
		// StringBuffer jrStnSbf = new StringBuffer("");//接入站
		// StringBuffer jrTimeSbf = new StringBuffer("");//接入时间
		StringBuffer jcStnSbf = new StringBuffer("");// 交出站
		StringBuffer jcTimeSbf = new StringBuffer("");// 交出时间

		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("1".equals(obj.getIsfjk())) {
				jcStnSbf = new StringBuffer(obj.getStnName());
				if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
															// 交出时间为出发时间
					jcTimeSbf = new StringBuffer(obj.getDptTime());
				} else {// 车站所属局与查询局相等 接入时间为到达时间
					jcTimeSbf = new StringBuffer(obj.getArrTime());
				}
			}
		}
		// 解析结束

		// 以下方式未来可能会启用
		// if (otherFjkCount%2 == 0) {
		// int fjkIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("1".equals(obj.getIsfjk())) {
		// fjkIndex ++;
		// if (fjkIndex%2 == 0) {//fjk出现的序号为偶数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//fjk出现的序号为奇数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// } else {
		// int otherIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("other".equals(obj.getIsfdz())) {
		// otherIndex ++;
		// if (otherIndex%2 == 0) {//other出现的次数序号为偶数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//other出现的次数序号为奇数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// }
		//
		//
		//
		// if (!"".equals(jrStnSbf.toString())) {
		// pageRowObj.setJrStn(jrStnSbf.substring(0,
		// jrStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jrTimeSbf.toString())) {
		// pageRowObj.setJrTime(jrTimeSbf.substring(0,
		// jrTimeSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcStnSbf.toString())) {
		// pageRowObj.setJcStn(jcStnSbf.substring(0,
		// jcStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcTimeSbf.toString())) {
		// pageRowObj.setJcTime(jcTimeSbf.substring(0,
		// jcTimeSbf.lastIndexOf("/")));
		// }

		pageRowObj.setJcStn(jcStnSbf.toString());
		pageRowObj.setJcTime(jcTimeSbf.toString());
	}

	/**
	 * 接入终到业务解析规则： isFjk==1出现的序号最小的为接入（暂时用该方法）
	 * 
	 * // * isFjk==1出现的序号为奇数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]； // *
	 * isFjk==1出现的序号为偶数且不为发到站：交出[交出时间：（车站所属局==查询局 取出发时间 不相等时取到站时间）]
	 * 
	 * @param pageRowObj
	 *            页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoJRZD(HighLinePlanTrainDto pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName) {
		StringBuffer jrStnSbf = new StringBuffer("");// 接入站
		StringBuffer jrTimeSbf = new StringBuffer("");// 接入时间
		// StringBuffer jcStnSbf = new StringBuffer("");//交出站
		// StringBuffer jcTimeSbf = new StringBuffer("");//交出时间

		// int fjkIndex = 0;
		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("1".equals(obj.getIsfjk())) {
				jrStnSbf.append(obj.getStnName());
				if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
															// 接入时间为到达时间
					jrTimeSbf.append(obj.getArrTime());
				} else {// 车站所属局与查询局相等 接入时间为到达时间
					jrTimeSbf.append(obj.getDptTime());
				}
				break;

				// 以下方式未来可能会启用
				// fjkIndex ++;
				// if (fjkIndex%2 != 0) {//fjk出现的序号为奇数：接入
				// jrStnSbf.append(obj.getStnName()).append("/");
				// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等
				// 接入时间为到达时间
				// jrTimeSbf.append(obj.getArrTime()).append("/");
				// } else {//车站所属局与查询局相等 接入时间为到达时间
				// jrTimeSbf.append(obj.getDptTime()).append("/");
				// }
				// } else
				// if("other".equals(obj.getIsfdz())){//fjk出现的序号为偶数且不为发到站：交出
				// jcStnSbf.append(obj.getStnName()).append("/");
				// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等
				// 交出时间为出发时间
				// jcTimeSbf.append(obj.getDptTime()).append("/");
				// } else {//车站所属局与查询局相等 接入时间为到达时间
				// jcTimeSbf.append(obj.getArrTime()).append("/");
				// }
				// }
			}
		}
		// 解析结束

		// if (!"".equals(jrStnSbf.toString())) {
		// pageRowObj.setJrStn(jrStnSbf.substring(0,
		// jrStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jrTimeSbf.toString())) {
		// pageRowObj.setJrTime(jrTimeSbf.substring(0,
		// jrTimeSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcStnSbf.toString())) {
		// pageRowObj.setJcStn(jcStnSbf.substring(0,
		// jcStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcTimeSbf.toString())) {
		// pageRowObj.setJcTime(jcTimeSbf.substring(0,
		// jcTimeSbf.lastIndexOf("/")));
		// }
		pageRowObj.setJrStn(jrStnSbf.toString());
		pageRowObj.setJrTime(jrTimeSbf.toString());
	}

	/**
	 * 接入交出业务解析规则： isFjk==1出现的序号最小的为接入（暂时用该方法） isFjk==1出现的序号最大的为交出（暂时用该方法）
	 * 
	 * // * otherFjkCount==0 始发信息即为接入信息，终到信息即为交出信息 return // * otherFjkCount!=0
	 * // * otherFjkCount%2==0 other出现的次数序号为偶数：交出[交出时间：（车站所属局==查询局 取出发时间
	 * 不相等时取到站时间）]； // * other出现的次数序号为奇数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]
	 * // * otherFjkCount%2!=0 isFjk==1出现的序号为偶数：交出[交出时间：（车站所属局==查询局 取出发时间
	 * 不相等时取到站时间）]； // * isFjk==1出现的序号为奇数：接入[接入时间：（车站所属局==查询局 取到站时间 不相等时取出发时间）]
	 * 
	 * @param pageRowObj
	 *            页面列车列表行数据对象
	 * @param planTrainStnList
	 *            //列车经由时刻集合（只包含发到站、分界口）
	 * @param bureauName
	 *            界面查询路局简称
	 * @param otherFjkCount
	 *            列车经由点单 分界口总个数(不包含发到站同时为分界口的)
	 */
	private void fillInfoJRJC(HighLinePlanTrainDto pageRowObj,
			List<PlanTrainStnDto> planTrainStnList, String bureauName,
			int otherFjkCount) {
		StringBuffer jrStnSbf = new StringBuffer("");// 接入站
		StringBuffer jrTimeSbf = new StringBuffer("");// 接入时间
		StringBuffer jcStnSbf = new StringBuffer("");// 交出站
		StringBuffer jcTimeSbf = new StringBuffer("");// 交出时间

		boolean isFirstFjkBoolean = true;
		for (int i = 0; i < planTrainStnList.size(); i++) {
			PlanTrainStnDto obj = planTrainStnList.get(i);
			if ("1".equals(obj.getIsfjk())) {
				if (isFirstFjkBoolean) {
					jrStnSbf.append(obj.getStnName());
					if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
																// 接入时间为到达时间
						jrTimeSbf.append(obj.getArrTime());
					} else {// 车站所属局与查询局相等 接入时间为到达时间
						jrTimeSbf.append(obj.getDptTime());
					}
					isFirstFjkBoolean = false;
				}

				jcStnSbf = new StringBuffer(obj.getStnName());
				if (bureauName.equals(obj.getStnBureau())) {// 车站所属局与查询局相等
															// 交出时间为出发时间
					jcTimeSbf = new StringBuffer(obj.getDptTime());
				} else {// 车站所属局与查询局相等 接入时间为到达时间
					jcTimeSbf = new StringBuffer(obj.getArrTime());
				}
			}
		}
		// 解析结束

		// 以下方法未来可能会启用
		// if (otherFjkCount == 0) {
		// pageRowObj.setJrStn(pageRowObj.getStartStn());
		// pageRowObj.setJrTime(pageRowObj.getStartTime());
		// pageRowObj.setJcStn(pageRowObj.getEndStn());
		// pageRowObj.setJcTime(pageRowObj.getEndTime());
		// return;
		// }
		//
		//
		// if (otherFjkCount%2 == 0) {
		// int otherIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("other".equals(obj.getIsfdz())) {
		// otherIndex ++;
		// if (otherIndex%2 != 0) {//other出现的次数序号为奇数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//other出现的次数序号为偶数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// } else {
		// int fjkIndex = 0;
		// for(int i=0;i<planTrainStnList.size();i++) {
		// PlanTrainStnDto obj = planTrainStnList.get(i);
		// if("1".equals(obj.getIsfjk())) {
		// fjkIndex ++ ;
		// if (fjkIndex%2 != 0) {//fjk出现的序号为奇数：接入
		// jrStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getArrTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jrTimeSbf.append(obj.getDptTime()).append("/");
		// }
		// } else {//fjk出现的序号为偶数：交出
		// jcStnSbf.append(obj.getStnName()).append("/");
		// if (bureauName.equals(obj.getStnBureau())) {//车站所属局与查询局相等 交出时间为出发时间
		// jcTimeSbf.append(obj.getDptTime()).append("/");
		// } else {//车站所属局与查询局相等 接入时间为到达时间
		// jcTimeSbf.append(obj.getArrTime()).append("/");
		// }
		// }
		// }
		// }
		// //解析结束
		// }
		// if (!"".equals(jrStnSbf.toString())) {
		// pageRowObj.setJrStn(jrStnSbf.substring(0,
		// jrStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jrTimeSbf.toString())) {
		// pageRowObj.setJrTime(jrTimeSbf.substring(0,
		// jrTimeSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcStnSbf.toString())) {
		// pageRowObj.setJcStn(jcStnSbf.substring(0,
		// jcStnSbf.lastIndexOf("/")));
		// }
		// if (!"".equals(jcTimeSbf.toString())) {
		// pageRowObj.setJcTime(jcTimeSbf.substring(0,
		// jcTimeSbf.lastIndexOf("/")));
		// }
		pageRowObj.setJrStn(jrStnSbf.toString());
		pageRowObj.setJrTime(jrTimeSbf.toString());
		pageRowObj.setJcStn(jcStnSbf.toString());
		pageRowObj.setJcTime(jcTimeSbf.toString());
	}

	/**
	 * 查询列车车底信息
	 * 
	 * 业务规则：1、根据planTrainId查询车底信息 2.根据crhType查询车型编组信息
	 * 
	 * @param queryMap
	 *            { key: planTrainId id key: creatType
	 *            //"创建方式  TD（0:基本图；1:基本图滚动)；LK(2:文件电报；3:命令）" }
	 * @return
	 */
	public HighLinePlanVehicleDto getHighLineVehInfo(
			Map<String, String> queryMap) {
		String sqlKey = "";
		if ("TD".equals(queryMap.get("creatType"))) {
			sqlKey = Constants.HIGHLINECROSSDAO_GET_HIGHLINEVEH_JBT;// 查询基本图字段
		} else if ("LK".equals(queryMap.get("creatType"))) {
			sqlKey = Constants.HIGHLINECROSSDAO_GET_HIGHLINEVEH_LK;// 查询临客字段
		}

		List<VehicleDto> vehicleList = new ArrayList<VehicleDto>();
		HighLinePlanVehicleDto retObj = (HighLinePlanVehicleDto) baseDao
				.selectOneBySql(sqlKey, queryMap);
		if (retObj != null) {
			boolean vehicleList1IsEmpty = true;// 车底1为空
			List<VehicleDto> vehicleList1 = null;
			if (retObj.getVehicle1() != null
					&& !"".equals(retObj.getVehicle1().trim())) {
				Map<String, String> vehQueryMap = new HashMap<String, String>();
				vehQueryMap.put("groupNbr", retObj.getVehicle1());
				vehicleList1 = baseDao.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_VEH_BY_CRHTYPE,
						vehQueryMap);
				if (vehicleList1 != null && vehicleList1.size() != 0) {
					vehicleList1IsEmpty = false;
					vehicleList.addAll(vehicleList1);
				}
			}
			if (retObj.getVehicle2() != null
					&& !"".equals(retObj.getVehicle2().trim())) {
				Map<String, String> vehQueryMap = new HashMap<String, String>();
				vehQueryMap.put("groupNbr", retObj.getVehicle2());
				List<VehicleDto> vehicleList2 = baseDao.selectListBySql(
						Constants.HIGHLINECROSSDAO_GET_VEH_BY_CRHTYPE,
						vehQueryMap);
				if (vehicleList2 != null && vehicleList2.size() != 0) {
					if (vehicleList1IsEmpty) {// 当车底1编组数据为空时，直接追加
						vehicleList.addAll(vehicleList2);
					} else {// 为了让车底编号从1~16
						for (int i = 1; i <= vehicleList2.size(); i++) {
							VehicleDto obj = vehicleList2.get(i - 1);
							obj.setVehicleSort(String.valueOf(vehicleList1
									.size() + i));// 转换编号
							vehicleList.add(obj);
						}
					}
				}
			}

			// 控制界面车底编组信息列表只显示20列
			while (vehicleList.size() < 20) {
				vehicleList.add(new VehicleDto("", "", ""));
			}
			while ((vehicleList.size() - 20) > 0) {
				vehicleList.remove(vehicleList.size() - 1);
			}

			retObj.setVehicleList(vehicleList);
		}

		return retObj;
	}

	/**
	 * 查询列车车底信息
	 * 
	 * 业务规则：1、根据planTrainId查询车底信息 2.根据crhType查询车型编组信息
	 * 
	 * @param queryMap
	 *            { key: planTrainId id key: planCrossId 交路id key: trainNbr 车次
	 *            key: crewDate 乘务计划日期 yyyyMMdd key: bureauName 查询局简称 key:
	 *            creatType //"创建方式  TD（0:基本图；1:基本图滚动)；LK(2:文件电报；3:命令）" }
	 * @return
	 */
	public Map<String, String> getHighLineCrewInfo(Map<String, String> queryMap) {
		Map<String, String> retMap = new HashMap<String, String>();

		StringBuffer czNameSbf = new StringBuffer("");// 车长
		StringBuffer czCrossNameSbf = new StringBuffer("");// 车长值乘交路
		StringBuffer czDeptNameSbf = new StringBuffer("");// 车长所属单位
		StringBuffer sjNameSbf = new StringBuffer("");// 司机
		StringBuffer sjCrossNameSbf = new StringBuffer("");// 司机值乘交路
		StringBuffer sjDeptNameSbf = new StringBuffer("");// 司机所属单位
		StringBuffer jxsNameSbf = new StringBuffer("");// 机械师
		StringBuffer jxsCrossNameSbf = new StringBuffer("");// 机械师值乘交路
		StringBuffer jxsDeptNameSbf = new StringBuffer("");// 机械师所属单位

		retMap.put("tokenPsgBureau", "");// 客运担当局（局码）
		retMap.put("tokenPsgBureauName", "");// 客运担当局（简称）
		retMap.put("tokenPsgDept", "");// 担当客运段

		String sqlKey = "";
		if ("TD".equals(queryMap.get("creatType"))) {
			sqlKey = Constants.HIGHLINECROSSDAO_GET_HIGHLINECREW_JBT;// 查询基本图字段
		} else if ("LK".equals(queryMap.get("creatType"))) {
			sqlKey = Constants.HIGHLINECROSSDAO_GET_HIGHLINECREW_LK;// 查询临客字段
		}

		List<Map<String, String>> tempList = baseDao.selectListBySql(sqlKey,
				queryMap);
		if (tempList != null && tempList.size() != 0) {

			for (int i = 0; i < tempList.size(); i++) {
				Map<String, String> mapTemp = tempList.get(i);

				if (mapTemp == null) {
					continue;
				}
				if (i == 0) {
					retMap.put(
							"tokenPsgBureau",
							mapTemp.get("TOKEN_PSG_BUREAU") != null ? mapTemp
									.get("TOKEN_PSG_BUREAU") : "");// 客运担当局（局码）
					retMap.put(
							"tokenPsgBureauName",
							mapTemp.get("TOKEN_PSG_BUREAU_NAME") != null ? mapTemp
									.get("TOKEN_PSG_BUREAU_NAME") : "");// 客运担当局（简称）
					retMap.put(
							"tokenPsgDept",
							mapTemp.get("TOKEN_PSG_DEPT") != null ? mapTemp
									.get("TOKEN_PSG_DEPT") : "");// 担当客运段
				}

				if ("1".equals(mapTemp.get("CREW_TYPE"))) {// 车长
					// 车长值乘交路
					if (mapTemp.get("CREW_CROSS") != null
							&& !"".equals(mapTemp.get("CREW_CROSS"))
							&& !czCrossNameSbf.toString().contains(
									mapTemp.get("CREW_CROSS"))) {
						czCrossNameSbf.append(mapTemp.get("CREW_CROSS"))
								.append(";");
					}
					// 车长所属单位
					if (mapTemp.get("RECORD_PEOPLE_ORG") != null
							&& !"".equals(mapTemp.get("RECORD_PEOPLE_ORG"))
							&& !czDeptNameSbf.toString().contains(
									mapTemp.get("RECORD_PEOPLE_ORG"))) {
						czDeptNameSbf.append(mapTemp.get("RECORD_PEOPLE_ORG"))
								.append(";");
					}

					czNameSbf
							.append(mapTemp.get("NAME1") == null ? "" : mapTemp
									.get("NAME1"))
							.append("-")
							.append(mapTemp.get("TEL1") == null ? "" : mapTemp
									.get("TEL1")).append(";");

					czNameSbf
							.append(mapTemp.get("NAME2") == null ? "" : mapTemp
									.get("NAME2"))
							.append("-")
							.append(mapTemp.get("TEL2") == null ? "" : mapTemp
									.get("TEL2")).append(";");
				} else if ("2".equals(mapTemp.get("CREW_TYPE"))) {// 司机
					// 司机值乘交路
					if (mapTemp.get("CREW_CROSS") != null
							&& !"".equals(mapTemp.get("CREW_CROSS"))
							&& !sjCrossNameSbf.toString().contains(
									mapTemp.get("CREW_CROSS"))) {
						sjCrossNameSbf.append(mapTemp.get("CREW_CROSS"))
								.append(";");
					}
					// 司机所属单位
					if (mapTemp.get("RECORD_PEOPLE_ORG") != null
							&& !"".equals(mapTemp.get("RECORD_PEOPLE_ORG"))
							&& !sjDeptNameSbf.toString().contains(
									mapTemp.get("RECORD_PEOPLE_ORG"))) {
						sjDeptNameSbf.append(mapTemp.get("RECORD_PEOPLE_ORG"))
								.append(";");
					}

					sjNameSbf
							.append(mapTemp.get("NAME1") == null ? "" : mapTemp
									.get("NAME1"))
							.append("-")
							.append(mapTemp.get("TEL1") == null ? "" : mapTemp
									.get("TEL1")).append(";");

					sjNameSbf
							.append(mapTemp.get("NAME2") == null ? "" : mapTemp
									.get("NAME2"))
							.append("-")
							.append(mapTemp.get("TEL2") == null ? "" : mapTemp
									.get("TEL2")).append(";");
				} else if ("3".equals(mapTemp.get("CREW_TYPE"))) {// 机械师
					// 机械师值乘交路
					if (mapTemp.get("CREW_CROSS") != null
							&& !"".equals(mapTemp.get("CREW_CROSS"))
							&& !jxsCrossNameSbf.toString().contains(
									mapTemp.get("CREW_CROSS"))) {
						jxsCrossNameSbf.append(mapTemp.get("CREW_CROSS"))
								.append(";");
					}
					// 机械师所属单位
					if (mapTemp.get("RECORD_PEOPLE_ORG") != null
							&& !"".equals(mapTemp.get("RECORD_PEOPLE_ORG"))
							&& !jxsCrossNameSbf.toString().contains(
									mapTemp.get("RECORD_PEOPLE_ORG"))) {
						jxsDeptNameSbf.append(mapTemp.get("RECORD_PEOPLE_ORG"))
								.append(";");
					}
					jxsNameSbf
							.append(mapTemp.get("NAME1") == null ? "" : mapTemp
									.get("NAME1"))
							.append("-")
							.append(mapTemp.get("TEL1") == null ? "" : mapTemp
									.get("TEL1")).append(";");

					jxsNameSbf
							.append(mapTemp.get("NAME2") == null ? "" : mapTemp
									.get("NAME2"))
							.append("-")
							.append(mapTemp.get("TEL2") == null ? "" : mapTemp
									.get("TEL2")).append(";");
				}
			}
		}

		retMap.put(
				"czName",
				!"".equals(czNameSbf.toString()) ? czNameSbf.substring(0,
						czNameSbf.lastIndexOf(";")) : "");// 司机
		retMap.put(
				"czCrossName",
				!"".equals(czCrossNameSbf.toString()) ? czCrossNameSbf
						.substring(0, czCrossNameSbf.lastIndexOf(";")) : "");// 司机值乘交路
		retMap.put(
				"czDeptName",
				!"".equals(czDeptNameSbf.toString()) ? czDeptNameSbf.substring(
						0, czDeptNameSbf.lastIndexOf(";")) : "");// 司机所属单位

		retMap.put(
				"sjName",
				!"".equals(sjNameSbf.toString()) ? sjNameSbf.substring(0,
						sjNameSbf.lastIndexOf(";")) : "");// 司机
		retMap.put(
				"sjCrossName",
				!"".equals(sjCrossNameSbf.toString()) ? sjCrossNameSbf
						.substring(0, sjCrossNameSbf.lastIndexOf(";")) : "");// 司机值乘交路
		retMap.put(
				"sjDeptName",
				!"".equals(sjDeptNameSbf.toString()) ? sjDeptNameSbf.substring(
						0, sjDeptNameSbf.lastIndexOf(";")) : "");// 司机所属单位

		retMap.put(
				"jxsName",
				!"".equals(jxsNameSbf.toString()) ? jxsNameSbf.substring(0,
						jxsNameSbf.lastIndexOf(";")) : "");// 机械师
		retMap.put(
				"jxsCrossName",
				!"".equals(jxsCrossNameSbf.toString()) ? jxsCrossNameSbf
						.substring(0, jxsCrossNameSbf.lastIndexOf(";")) : "");// 机械师值乘交路
		retMap.put(
				"jxsDeptName",
				!"".equals(jxsDeptNameSbf.toString()) ? jxsDeptNameSbf
						.substring(0, jxsDeptNameSbf.lastIndexOf(";")) : "");// 机械师所属单位

		return retMap;
	}

}
