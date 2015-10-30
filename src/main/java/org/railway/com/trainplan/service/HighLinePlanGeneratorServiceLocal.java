package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.railway.com.trainplan.common.utils.ConstantUtil;
import org.railway.com.trainplan.common.utils.SortUtil;
import org.railway.com.trainplan.common.utils.StringAndTimeUtil;
import org.railway.com.trainplan.entity.CompareModel;
import org.railway.com.trainplan.entity.DicRelaCrossPost;
import org.railway.com.trainplan.entity.HighlineCrossCmd;
import org.railway.com.trainplan.entity.HighlineCrossTrain;
import org.railway.com.trainplan.entity.PlanCrossCmd;
import org.railway.com.trainplan.entity.PlanTrainCMD;
import org.railway.com.trainplan.repository.mybatis.BureauDao;
import org.railway.com.trainplan.repository.mybatis.CmdPlanDao;
import org.railway.com.trainplan.repository.mybatis.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HighLinePlanGeneratorServiceLocal {
	@Autowired
    private CmdPlanDao cmdPlanDao;
	@Autowired
	private BureauDao bureauDao;

	/**
	 * 加载高铁数据
	 * 
	 * @param bureuaName
	 * @param bureuaCode
	 * @param runDate
	 * @return
	 */
	public List<HighlineCrossCmd> loadHighlineData(String bureuaName,String bureuaCode, Date runDate,String runDateString) {
		// 20150202 调试临客命令查不到情况。何宇阳
		List<HighlineCrossCmd> highlineCrossList = new ArrayList<HighlineCrossCmd>();

		// step1: 查找加载日期及经由局相关的所有列车开行计划
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bureuaName", bureuaName);
		map.put("runDate", runDateString);
		List<PlanTrainCMD> trainList = cmdPlanDao.findPlanTrainListByBureauAndRunDate(map);

		StringBuffer messageInfo = new StringBuffer();
		messageInfo.append("[交路加载：日期="
			+ StringAndTimeUtil.yearMonthDaySdf.format(runDate) + "，"
			+ bureuaName + "局相关高铁运行线个数 =" + trainList.size() + "]");
		// 临客列车集合
		List<PlanTrainCMD> cmdTrainList = new ArrayList<PlanTrainCMD>();

		HashMap<String, List<PlanTrainCMD>> planCrossTrainMap = new HashMap<String, List<PlanTrainCMD>>();
		for (PlanTrainCMD train : trainList) {
			//@Test
//			if(train.getPlanCrossId() != null && (train.getPlanCrossId().equals("21725376-4680-418e-8c2d-dd3c033173e6") || train.getPlanCrossId().equals("21725376-4680-418e-8c2d-dd3c033173e6"))){
//				System.out.println(train.getRunDate() + "##[" + train.getPlanCrossId() + "]##[" + train.getTrainNbr() + "]##" + StringAndTimeUtil.yearMonthDayHourMinuteSdf.format(train.getStartTime()));
//			}
			CompareModel compareModel = null;
			if (train.getBaseTrainId() == null
					|| train.getBaseTrainId().equals("")) {
				// step2: 判定开行计划经由站是否符合条件
//				Map<String,Object> map2 = new HashMap<String,Object>();
				map.clear();
				map.put("bureuaName", bureuaName);
				map.put("runDate", runDateString);
				map.put("planTrainId", train.getPlanTrainId());
				compareModel = cmdPlanDao.findCompareModelByBureauAndRunDate(map);
			}
			else{
//				Map<String,Object> map3 = new HashMap<String,Object>();
				map.clear();
				map.put("bureuaName", bureuaName);
				map.put("runDate", train.getRunDate());
				map.put("baseTrainId", train.getBaseTrainId());
				compareModel = cmdPlanDao.findCompareModelByBureauAndRunDateBaseId(map);
			}
			if (compareModel == null) {
				continue;
			}
			
			if (!StringAndTimeUtil.isStartAndTargetDateInRunDate2(compareModel.getMinArriveDate(),runDate)) {
				continue;
			} 
			
			// step3: 图定开行计划按交路分组
			if (train.getPlanCrossId() != null
					&& !train.getPlanCrossId().trim().equals("")) {

				if (planCrossTrainMap.containsKey(train.getPlanCrossId())) {
					planCrossTrainMap.get(train.getPlanCrossId()).add(train);
				} else {
					List<PlanTrainCMD> tempList = new ArrayList<PlanTrainCMD>();
					tempList.add(train);
					planCrossTrainMap.put(train.getPlanCrossId(), tempList);
				}
			} else {
				cmdTrainList.add(train);
			}
		}
		/**
		 *  特殊交路处理：
		 *          需要提前若干天加载的交路
		 *          首先配置交路线台关系字典表字段：提前加载天数LOADDATE_OFFSET  1:提前1天；默认0
		 *          找到特殊交路的始发车名，并计算偏差日期，查找到始发车开行计划，加入到交路ID列车计划映射中。
		 *               
		 */
		
		List<DicRelaCrossPost> specialCrossList = cmdPlanDao.findCrossNameFromDicRelaCrossPostByOffset(bureuaCode);
		for(DicRelaCrossPost specialCross : specialCrossList){
			
			int offset = specialCross.getLoaddateOffset();
			Calendar cal = Calendar.getInstance();
			cal.setTime(runDate);
			cal.add(Calendar.DAY_OF_MONTH, offset);
//			Map<String,Object> map4 = new HashMap<String,Object>();
			map.clear();
			map.put("trainNbr", StringAndTimeUtil.getSplitedString(specialCross.getCrossName(),ConstantUtil.CROSSNAME_TOKEN).get(0));
			//TODO
			map.put("time", StringAndTimeUtil.yearMonthDaySimpleSdf.format(cal.getTime()));
			PlanTrainCMD firstTrain = cmdPlanDao.findPlanTrainByCrossFirstTrainNbrAndRunDate(map);
			if(firstTrain != null){
				
				List<PlanTrainCMD> listTemp = new ArrayList<PlanTrainCMD>();
				listTemp.add(firstTrain);
				planCrossTrainMap.put(firstTrain.getPlanCrossId(), listTemp);
			}
		}
		
		// step4: 查找交路中的始发车
		Iterator<String> iterator = planCrossTrainMap.keySet().iterator();
		int tdCount = 0;
		while (iterator.hasNext()) {
			
			String planCrossId = iterator.next();
			
			//@Test
			if(planCrossId.equals("21725376-4680-418e-8c2d-dd3c033173e6") || planCrossId.equals("21725376-4680-418e-8c2d-dd3c033173e6")){
				System.out.println("TEST BBB");
			}
			map.clear();
			map.put("planCrossId", planCrossId);
			map.put("bureuaCode", bureuaCode);
			List<PlanCrossCmd> crossList = cmdPlanDao.findPlanCrossByPlanCrossId2(map);
			PlanCrossCmd cross = null;
			if(crossList!=null && crossList.size()>0){
				cross = crossList.get(0);
			}
			for (PlanCrossCmd planCrossCmd : crossList) {
				if(bureuaCode.equals(planCrossCmd.getrBureau())){
					cross = planCrossCmd;
				}
			}
			// 按照始发时间先后对列车排序
			List<PlanTrainCMD> mapTrainList = SortUtil.sortPlanTrainAsStartTime(planCrossTrainMap.get(planCrossId));
			
			HashMap<String, PlanTrainCMD> firstTrainMap = new HashMap<String, PlanTrainCMD>();
			if (cross != null) {

				String crossName = cross.getCrossName();
				List<String> trainNameList = StringAndTimeUtil
						.getSplitedString(crossName,
								ConstantUtil.CROSSNAME_TOKEN);
				String firstTrainName = trainNameList.get(0);
				//临时列车内存集合
				List<PlanTrainCMD> tempTrainList = new ArrayList<PlanTrainCMD>();
				tempTrainList.addAll(mapTrainList);
				for(PlanTrainCMD mapTrain : mapTrainList){
					// step5:查找始发车--先从临时内存集合中找，没有再从数据库中查找
					PlanTrainCMD firstTrain = this.searchPrePlanTrain(firstTrainName, 1, mapTrain, tempTrainList);
					
					if (firstTrain == null) {
						messageInfo.append("[交路加载：" + cross.getCrossName()
								+ "数据有错，没有找到始发车]");
						continue;
					}
					if(!firstTrainMap.containsKey(firstTrain.getPlanTrainId())){
						firstTrainMap.put(firstTrain.getPlanTrainId(), firstTrain);
					}else{
						continue;
					}
				}
				
				Iterator<String> iteTrain = firstTrainMap.keySet().iterator();
				while(iteTrain.hasNext()){
					List<PlanTrainCMD> crossTrainList = new ArrayList<PlanTrainCMD>();
					PlanTrainCMD firstTrain = firstTrainMap.get(iteTrain.next());
					crossTrainList.add(firstTrain);
					PlanTrainCMD nowTrain = null;
					PlanTrainCMD nextTrain = null;
					// step6: 根据始发车依此查找出交路中的后续列车开行
					for (int j = 1; j < trainNameList.size(); j++) {

						if (j == 1) {
							nowTrain = firstTrain;
						}
						nextTrain = this.searchNextPlanTrain(nowTrain,
								tempTrainList);
						// 没有找到后续车，跳过本交路
						if (nextTrain == null) {
							messageInfo.append("[交路加载：" + cross.getCrossName()
									+ "数据有错，没有找到后续车]");
							crossTrainList = null;
							break;
						}

						crossTrainList.add(nextTrain);
						nowTrain = nextTrain;
					}

					if (crossTrainList != null) {
						if (!isCrossSatisfy(crossTrainList, runDate,runDateString, bureuaName))
							continue;
						
						cross.setTrainList(crossTrainList);
						// step7: 生成图定高铁计划
						HighlineCrossCmd highlineCross = generatorHighlineCrossByPlanCross(
								cross, bureuaCode, runDate);
						if(highlineCross != null){
							tdCount++;
							highlineCrossList.add(highlineCross);
						}else{
							messageInfo.append("[交路加载：" + cross.getCrossName()
									+ "数据有错，存库失败]");
						}
						highlineCross.setCrossTrain(generatorHighlineCrossTrainList(
								highlineCross, cross.getTrainList()));
					}
					
				}
			}

		}

		// step8: 生成临客高铁计划
		for (PlanTrainCMD planTrain : cmdTrainList) {
			HighlineCrossCmd highlineCross = generatorHighlineCrossByPlanTrain(
					planTrain, bureuaCode, runDate);
			highlineCrossList.add(highlineCross);
			List<PlanTrainCMD> list = new ArrayList<PlanTrainCMD>();
			list.add(planTrain);
			highlineCross.setCrossTrain(generatorHighlineCrossTrainList(
					highlineCross, list));
		}
		//step9: 更新车底信息。将历史车底信息带过来
		map.clear();
		map.put("runDate", StringAndTimeUtil.yearMonthDaySimpleSdf.format(runDate));
		map.put("bureuaCode", bureuaCode);
//		cmdPlanDao.updateHighlineCrossVehicleByHistory(map);
		messageInfo.append("[交路加载：图定交路数=" + tdCount
				+ "，临客交路数=" + cmdTrainList.size() + "]");
		
		// step10:关闭数据库资源
//		DbUtilDdml.getInstance().closeConnection();
		messageInfo.append("[交路加载：日期= "
				+ StringAndTimeUtil.yearMonthDaySdf.format(runDate) + "，"
				+ bureuaName + "局成功交路数=" + highlineCrossList.size() + "]");
		// 20150202 调试临客命令查不到情况。何宇阳
//		JDomUtil.writeFile(messageInfo.toString(), "/lkmllog/", "highLineCross.txt");
		return highlineCrossList;
	}

	private PlanTrainCMD searchPrePlanTrain(String trainName, int trainSort,
			PlanTrainCMD train, List<PlanTrainCMD> tempTrainList) {

		if (train.getTrainNbr().equals(trainName)
				&& train.getTrainSort().intValue() == trainSort) {

			return train;
		} else {
			if (train.getPreTrainId() != null
					&& !train.getPreTrainId().equals("")) {

				PlanTrainCMD preTrain = null;
				for (PlanTrainCMD planTrain : tempTrainList) {
					if (planTrain.getPlanTrainId()
							.equals(train.getPreTrainId())) {//abce5a47-48bd-40e4-acd7-df7420339c79
						preTrain = planTrain;
						break;
					}
				}
				if (preTrain == null) {
					preTrain = cmdPlanDao.findPlanTrainListByPlanTrainId(train.getPreTrainId());
					tempTrainList.add(preTrain);
				}
				return searchPrePlanTrain(trainName, trainSort, preTrain,
						tempTrainList);
			} else {
				return null;
			}
		}
	}
	private PlanTrainCMD searchNextPlanTrain(PlanTrainCMD train,List<PlanTrainCMD> planTrainList) {
		PlanTrainCMD nextTrain = null;

		if (train.getNextTrainId() != null
				&& !train.getNextTrainId().equals("")) {

			for (PlanTrainCMD planTrain : planTrainList) {
				if (planTrain.getPlanTrainId().equals(train.getNextTrainId())) {
					nextTrain = planTrain;
					break;
				}
			}

			if (nextTrain == null) {
				nextTrain = cmdPlanDao.findPlanTrainListByPlanTrainId(train.getNextTrainId());
			}

		}
		return nextTrain;
	}
	private HighlineCrossCmd generatorHighlineCrossByPlanCross(PlanCrossCmd planCross,
			String bureuaCode, Date runDate) {
		// TODO Auto-generated method stub
		HighlineCrossCmd highlineCross = new HighlineCrossCmd();
		highlineCross.setHighlineCrossId(UUID.randomUUID().toString());

		highlineCross.setPlanCrossId(planCross.getPlanCrossId() == null ? ""
				: planCross.getPlanCrossId());
		highlineCross.setBaseCrossId(planCross.getBaseCrossId());
		highlineCross.setCrossName(planCross.getCrossName() == null ? ""
				: planCross.getCrossName());
		if (planCross.getTrainList() == null
				|| planCross.getTrainList().isEmpty())
			return null;
		PlanTrainCMD firstTrain = planCross.getTrainList().get(0);
		PlanTrainCMD lastTrain = planCross.getTrainList().get(
				planCross.getTrainList().size() - 1);
		highlineCross.setCrossStartDate(firstTrain.getRunDate() == null ? ""
				: firstTrain.getRunDate());
		highlineCross.setCrossEndDate(lastTrain.getRunDate() == null ? ""
				: lastTrain.getRunDate());
		highlineCross.setCrossStartStn(firstTrain.getStartStn() == null ? ""
				: firstTrain.getStartStn());
		highlineCross.setCrossEndStn(lastTrain.getEndStn() == null ? ""
				: lastTrain.getEndStn());
		highlineCross.setSpareFlag(planCross.getSpareFlag());
		highlineCross.setThroughLine(planCross.getThroughLine() == null ? ""
				: planCross.getThroughLine());
		highlineCross
				.setTokenVehBureau(planCross.getTokenVehBureau() == null ? ""
						: planCross.getTokenVehBureau());
		highlineCross
				.setTokenVehDepot(planCross.getTokenVehDepot() == null ? ""
						: planCross.getTokenVehDepot());
		highlineCross.setTokenVehDept(planCross.getTokenVehDept() == null ? ""
				: planCross.getTokenVehDept());
		highlineCross
				.setTokenPsgBureau(planCross.getTokenPsgBureau() == null ? ""
						: planCross.getTokenPsgBureau());
		highlineCross.setTokenPsgDept(planCross.getTokenPsgDept() == null ? ""
				: planCross.getTokenPsgDept());
		highlineCross.setCrossBureau(bureuaCode);
		highlineCross
				.setRelevantBureau(planCross.getRelevantBureau() == null ? ""
						: planCross.getRelevantBureau());
		highlineCross.setCrhType(planCross.getCrhType() == null ? ""
				: planCross.getCrhType());
		highlineCross
				.setCreatReason(ConstantUtil.HIGHLINE_CROSS_CREAT_REASON_BASEMAP);
		highlineCross.setCrossDate(StringAndTimeUtil.yearMonthDaySimpleSdf
				.format(runDate));
		highlineCross.setPostId(planCross.getPostId());
		highlineCross.setPostName(planCross.getPostName() == null ? ""
				: planCross.getPostName());
		// 20150506 增加交路显示名的拼接
		highlineCross.setCrossDisplayName(getHighlineCrossDisplayName(planCross.getTrainList()));
		cmdPlanDao.insertHighlineCross(highlineCross);
		return highlineCross;
	}

	private List<HighlineCrossTrain> generatorHighlineCrossTrainList(
			HighlineCrossCmd highlineCross, List<PlanTrainCMD> trainList) {
		// TODO Auto-generated method stub
		List<HighlineCrossTrain> crossTrainList = new ArrayList<HighlineCrossTrain>();

		for (PlanTrainCMD planTrain : trainList) {
			HighlineCrossTrain crossTrain = new HighlineCrossTrain();
			crossTrain.setHighlineTrainId(UUID.randomUUID().toString());
			crossTrain.setHighlineCrossId(highlineCross.getHighlineCrossId());
			crossTrain.setPlanTrainId(planTrain.getPlanTrainId());
			crossTrain.setRunDate(planTrain.getRunDate());
			crossTrain.setTrainNbr(planTrain.getTrainNbr());
			crossTrain.setTrainSort(planTrain.getTrainSort());
			cmdPlanDao.insertHighlineCrossTrain(crossTrain);
		}
		return crossTrainList;
	}
	private HighlineCrossCmd generatorHighlineCrossByPlanTrain(
			PlanTrainCMD planTrain, String bureuaCode, Date runDate) {
		HighlineCrossCmd highlineCross = new HighlineCrossCmd();
		highlineCross.setHighlineCrossId(UUID.randomUUID().toString());
		highlineCross.setCrossName(planTrain.getTrainNbr());
		highlineCross.setCrossStartDate(planTrain.getRunDate());
		highlineCross.setCrossEndDate(planTrain.getRunDate());
		highlineCross.setCrossStartStn(planTrain.getStartStn());
		highlineCross.setCrossEndStn(planTrain.getEndStn());
		highlineCross.setSpareFlag(planTrain.getSpareFlag() == null ? ""
				: (planTrain.getSpareFlag() + ""));
		//P-京
		highlineCross
				.setTokenVehBureau(planTrain.getTokenVehBureau() == null ? ""
						: bureauDao.getShortBureauNameByCode(planTrain.getTokenVehBureau()));
		highlineCross
				.setTokenPsgBureau(planTrain.getTokenPsgBureau() == null ? ""
						: bureauDao.getShortBureauNameByCode(planTrain.getTokenPsgBureau()));
								
		highlineCross.setCrossBureau(bureuaCode);
		highlineCross.setCrossDate(StringAndTimeUtil.yearMonthDaySimpleSdf.format(runDate));
		// 20150506 增加交路显示名的拼接
		List<PlanTrainCMD> trainList = new ArrayList<PlanTrainCMD>();
		trainList.add(planTrain);
		highlineCross.setCrossDisplayName(getHighlineCrossDisplayName(trainList));
		if(ConstantUtil.PLAN_TRAIN_CREATE_TYPE_CMD==planTrain.getCreatType().intValue()){
			highlineCross.setCreatReason(planTrain.getCmdShortinfo() == null ? ""
					: planTrain.getCmdShortinfo());
		}
		if(ConstantUtil.PLAN_TRAIN_CREATE_TYPE_TELEGRAPH==planTrain.getCreatType().intValue()){
			highlineCross
			.setCreatReason(planTrain.getTelShortinfo() == null ? ""
					: planTrain.getTelShortinfo());
		}

		cmdPlanDao.insertHighlineCross(highlineCross);
		return highlineCross;
	}
	
	/**
	 * 20150506 增加交路判定
	 * 判断交路是否符合要求 20150506 本局始发的列车开行日期必须与加载日期一致,本局接入的列车第一次接入的时间必须在加载日期范围内
	 * 
	 * @param trainList
	 * @param runDate
	 * @param bureauShortName
	 * @return
	 */
	private boolean isCrossSatisfy(List<PlanTrainCMD> trainList, Date runDate,String runDateString,String bureauShortName) {

		boolean result = false;
		if (trainList == null)
			return result;
		PlanTrainCMD firstTrain = trainList.get(0);
		//本局始发与加载日期一致
		if(firstTrain.getPassBureau() != null){
			String startBuearu = firstTrain.getPassBureau().substring(0,1);
			if(startBuearu.equals(bureauShortName)){
				if(firstTrain.getRunDate()
							.equals(runDateString)){
					result = true;
				}else{
					result = false;
				}
				return result;
			}
		}
		//外局接入本局列车第一次接入时间在加载日期范围内
		for (PlanTrainCMD train : trainList) {
			if (train.getPassBureau() != null
					&& train.getPassBureau().contains(bureauShortName)) {
				CompareModel compareModel = null;
				if (train.getBaseTrainId() == null
						|| train.getBaseTrainId().equals("")) {
					// step2: 判定开行计划经由站是否符合条件
					Map<String,Object> map = new HashMap<String,Object>();
					map.clear();
					map.put("bureuaName", bureauShortName);
					map.put("runDate", runDateString);
					map.put("planTrainId", train.getPlanTrainId());
					compareModel = cmdPlanDao.findCompareModelByBureauAndRunDate(map);
				}
				else{
					Map<String,Object> map = new HashMap<String,Object>();
					map.clear();
					map.put("bureuaName", bureauShortName);
					map.put("runDate", train.getRunDate());
					map.put("baseTrainId", train.getBaseTrainId());
					compareModel = cmdPlanDao.findCompareModelByBureauAndRunDateBaseId(map);
				}
//				if (compareModel == null) {
//					continue;
//				}
				
				if (compareModel != null) {
					if (StringAndTimeUtil.isStartAndTargetDateInRunDate2(compareModel.getMinArriveDate(),runDate)) {
						result = true;
					} 
				}else{
					result = false;
				}
				break;
			}
		}

		return result;
	}
	/**
	 * 获取交路显示名
	 * @param trainList
	 * @return
	 */
	private String getHighlineCrossDisplayName(List<PlanTrainCMD> trainList){
		StringBuffer displayBuffer = new StringBuffer();
		
		String temp = "";
		int i = 0;
		for (PlanTrainCMD train : trainList) {
			i++;
			displayBuffer.append(train.getTrainNbr());
			if(!temp.equals(train.getRunDate())){
				displayBuffer.append("(");
				displayBuffer.append(train.getRunDate());
				displayBuffer.append(")");
				temp = train.getRunDate();
			}
			if(i < trainList.size()){
				displayBuffer.append(ConstantUtil.CROSSNAME_TOKEN);
			}
		}
		return displayBuffer.toString();
	}
	
}
