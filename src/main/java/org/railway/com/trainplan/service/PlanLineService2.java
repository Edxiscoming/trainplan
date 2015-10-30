package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.utils.CreateRunlineDataUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.runline.CreateRunLineData;
import org.railway.com.trainplan.entity.runline.Job;
import org.railway.com.trainplan.entity.runline.Origin;
import org.railway.com.trainplan.entity.runline.RunLineItem;
import org.railway.com.trainplan.entity.runline.RunlineModel;
import org.railway.com.trainplan.entity.runline.TrainTypeModel;
import org.railway.com.trainplan.repository.mybatis.RunLineDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.railway.com.trainplanv2.dto.CreateRunLineData2;
import org.railway.com.trainplanv2.dto.Job2;
import org.railway.com.trainplanv2.dto.Origin2;
import org.railway.com.trainplanv2.dto.PlanLineInfoDto2;
import org.railway.com.trainplanv2.dto.RunLineItem2;
import org.railway.com.trainplanv2.dto.RunlineModel2;
import org.railway.com.trainplanv2.dto.TrainTypeModel2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by star on 5/21/14.
 */
@Component
@Transactional
@Monitored
public class PlanLineService2 {

    private static final Log logger = LogFactory.getLog(PlanLineService2.class);

    @Autowired
    private RunPlanDao runPlanDao;

    @Autowired
    private RunLineDao runLineDao;
    
    @Autowired
	private RunPlanService runPlanService;

	@Autowired
	private RunPlanLkService runPlanLkService;
	
	@Autowired
	private SendMQMessageService2 sendMQMessageService;
	
    @Value("#{restConfig['trainline.generatr.thread']}")
    private int threadNbr;

    public int checkTrainInfo(String planId, String lineId) {
        logger.debug(":::::::::::::::::::checkTrainInfo:::::::::::::::::::");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("planId", planId);
        param.put("lineId", lineId);
        try {
            List<Map<String, Object>> result = runPlanDao.checkTrainInfo(param);
            if (result.size() == 0) {
                return -1;
            }
            Map<String, Object> cells = result.get(0);
            if (cells.containsValue(0)) {
                return -1;
            }
        } catch (Exception e) {
            logger.error("planId: " + planId + " - lineId:" + lineId, e);
            return 0;
        }
        return 1;
    }

    public int checkTimeTable(String planId, String lineId) {
        logger.debug(":::::::::::::::::::checkTimeTable::::::::::::::::");
        int status = 1;
        try {
            List<Map<String, Object>> plans = runPlanDao.findPlanTimeTableByPlanId(planId);
            List<Map<String, Object>> lines = runLineDao.findLineTimeTableByLineId(lineId);
            if (plans.size() != lines.size()) {
                return -1;
            }
            if (plans.size() == 0) {
                return -1;
            }
            // 客运计划和日计划存储的始发站-到站时间 和 终到站-出发时间 不一样，且时刻表不关心这2个时间，所以不校验。
            Map<String, Object> plan_start = plans.get(0);
            plan_start.remove("ARR_TIME");
            Map<String, Object> line_start = lines.get(0);
            line_start.remove("ARR_TIME");
            Map<String, Object> plan_end = plans.get(plans.size() - 1);
            plan_end.remove("DPT_TIME");
            Map<String, Object> line_end = lines.get(lines.size() - 1);
            line_end.remove("DPT_TIME");
           
            if(plans!=null && plans.size()>0 && lines!=null && lines.size()>0){
            	label:for(int i = 0;i<lines.size();i++){
            		Map<String, Object> linesMap = plans.get(i);
            		Set<String> keys = linesMap.keySet();
            		Iterator<String> keyIt = keys.iterator();
            		while(keyIt.hasNext()){
            			String key = keyIt.next();
            			String lineValue = StringUtil.objToStr(linesMap.get(key));
            			String trainValue = StringUtil.objToStr(linesMap.get(key));
            			if(!lineValue.equals(trainValue)){
            				status = -1;
            				break label;
            			}
            		}
            	}
            }
           
        } catch (Exception e) {
            logger.error("planId: " + planId + " - lineId:" + lineId, e);
            return 0;
        }
        return status;
    }

    public int checkRouting(String planId, String lineId) {
        return 0;
    }
    
    /**
     * 
     * @param baseTrainId 基本图列车id
     * @param planTrains 开行计划列表
     * @return
     */
    public List<String> generateTrainLine(List<Map<String, String>> planTrains, String msgReceiveUrl, 
    		ShiroRealm.ShiroUser user) {
    	
    	ExecutorService executorService = Executors.newFixedThreadPool(threadNbr);
    	try {
			for (Map<String, String> planTrain : planTrains) { 
				executorService.execute(new TrainLineGenerator(planTrain, msgReceiveUrl, user));
			} 
    	}
    	finally {
    		executorService.shutdown();
    	}
		return null;
    	
    }
    
    class TrainLineGenerator implements Runnable {
    	private Map<String, String> planTrain;
    	private String msgReceiveUrl;
    	private ShiroRealm.ShiroUser user;
    	
    	TrainLineGenerator(Map<String, String> planTrain, String msgReceiveUrl, ShiroRealm.ShiroUser user) {
    		this.planTrain = planTrain;
    		this.msgReceiveUrl = msgReceiveUrl;
    		this.user = user;
    	}
    	

    	@Override
        @Transactional
        @Monitored
		public void run() {
			
		try {
			
				String baseTrainId = planTrain.get("baseTrainId");
				// 调用本地接口直接操作数据库
				// 根据planTrainId从表plan_train和表plan_train_stn中查询基本的数据
				String planTrainId = planTrain.get("planTrainId"); // 查询客运计划主体信息
				Map<String, Object> plan = runPlanService.findPlanInfoByPlanId2(planTrainId);
				PlanLineInfoDto2 planDto = new PlanLineInfoDto2(plan);

				// 目的就是要构建这些实体，再转成json字符串
				CreateRunLineData2 createRunLineData = new CreateRunLineData2();
				//MTrainLine mtrainLine = new MTrainLine();

				Origin2 origin = new Origin2();

				TrainTypeModel2 trainTypeModel = null;
				trainTypeModel = runPlanLkService.getTrainTypeForTypeId2(planDto
						.getTrainTypeId());

				RunlineModel2 runlineModel = new RunlineModel2();

				String dailyPlanId = planDto.getDailyPlanId();
				String dailyPlanIdLast = planDto.getDailyPlanIdLast();
				
//				String id = "";
//				if (dailyPlanId != null && !"".equals(dailyPlanId)) {
//					id = dailyPlanId;
//					runlineModel.setId(id);
//				} 
//				else if(dailyPlanIdLast != null && !"".equals(dailyPlanIdLast)) {
//					id = dailyPlanIdLast;
//					runlineModel.setId(id);
//				}
//				else {
//					// 主键id
//					id = planDto.getPlanTrainId();
					runlineModel.setId(planDto.getPlanTrainId());
//				}
				
//				if("1".equals(planDto.getHighLineFlag())) {
//					runlineModel.setHighSpeed(true);
//				}
//				else {
//					runlineModel.setHighSpeed(false);
//				}
				runlineModel.setHighSpeed(planDto.getHighLineFlag());
				runlineModel.setType(trainTypeModel);
				runlineModel.setName(planDto.getTrainName());

//    				mtrainLine.setBusiness("客运");
//    				mtrainLine.setTypeId(planDto.getTrainTypeId());
//    				mtrainLine.setRouteId(planDto.getRouteId());
//    				mtrainLine.setRouteName(planDto.getRouteName()); // 途经局

				runlineModel.setRouteBureauShortNames(planDto.getPassBureau());
				runlineModel.setSourceBureauName(planDto.getStartBureauFull());
				runlineModel.setSourceBureauShortName(planDto.getStartBureau());
				runlineModel.setSourceNodeName(planDto.getStartStn());

				runlineModel.setSourceTime(CreateRunlineDataUtil
						.getMillisecondTime(planDto.getStartTime()));

				runlineModel.setTargetBureauName(planDto.getEndBureauFull());
				runlineModel.setTargetBureauShortName(planDto.getEndBureau());
				runlineModel.setTargetNodeName(planDto.getEndStn());

				runlineModel.setTargetTime(CreateRunlineDataUtil
						.getMillisecondTime(planDto.getEndTime()));

				runlineModel.setSourceBureauId(planDto.getStartBureauId());
				runlineModel.setSourceNodeId(planDto.getStartStnId());
				runlineModel.setTargetBureauId(planDto.getEndBureauId());
				runlineModel.setTargetNodeId(planDto.getEndStnId());

				runlineModel.setSourceTimeSchedule(CreateRunlineDataUtil.getTimeSchedule2(null, planDto.getStartTime()));
				runlineModel.setTargetTimeSchedule(CreateRunlineDataUtil.getTimeSchedule2(planDto.getStartTime(),planDto.getEndTime()));

				runlineModel.setLastTimeText(CreateRunlineDataUtil.getLastTimeText2(runlineModel.getTargetTimeSchedule()));
				
				//suntao
//				runlineModel.setSourceNodeStationId(planDto.getSourceNodeStationId());//始发站ID
//				runlineModel.setSourceNodeStationName(planDto.getSourceNodeStationName());//始发站名
				runlineModel.setSourceNodeTdcsId(planDto.getSourceNodeTdcsId());//始发节点TDCS对应标识
				runlineModel.setSourceNodeTdcsName(planDto.getSourceNodeTdcsName());//始发节点TDCS对应名称
//				runlineModel.setTargetNodeStationId(planDto.getTargetNodeStationId());//终到站ID
//				runlineModel.setTargetNodeStationName(planDto.getTargetNodeStationName());//终到站名
				runlineModel.setTargetNodeTdcsId(planDto.getTargetNodeTdcsId());//终到节点TDCS对应标识
				runlineModel.setTargetNodeTdcsName(planDto.getTargetNodeTdcsName());//终到节点TDCS对应名称
				Job2 statesJob = new Job2();
				statesJob.setItemsText(planDto.getStates());
				runlineModel.setStates(statesJob);
				runlineModel.setPreviousTrainlineId(planDto.getPreviousTrailineId());
				runlineModel.setPreviousTrainlineName(planDto.getPreviousTrailineName());
				runlineModel.setNextTrainlineId(planDto.getNextTrailineId());
				runlineModel.setNextTrainlineName(planDto.getNextTrailineName());
				
				Job2 statesVehicles = new Job2();
				statesVehicles.setItemsText("<"+planDto.getVehicle()+">");
				runlineModel.setVehicles(statesVehicles);
				
				
				String createType = planDto.getCreatType();
				// createType: 0:基本图 2:电报 3:命令
				if ("0".equals(createType) || "1".equals(createType)) {
					origin.setOrigin("基本图");
					origin.setId(baseTrainId);
					origin.setName(planDto.getTrainName());
				} else if ("2".equals(createType)) {
					origin.setOrigin("电报");
					origin.setId(planDto.getTelId());
					origin.setName(planDto.getTelShortInfo());
				} else if ("3".equals(createType)) {
					origin.setOrigin("命令");
					origin.setId(planDto.getCmdTrainId());
					origin.setName(planDto.getCmdShortInfo());
				}

				runlineModel.setOrigin(origin);

				//String jstr = JSONObject.fromObject(runlineModel).toString();

				//logger.info("运行线数据： " + jstr);

				// logger.info("运行线子项数据： " + jstr1);
				// 查询子表的数据
				 List<Map<String, Object>> plans;
				 if("0".equals(createType) && null != baseTrainId  && !"".equals(baseTrainId) && !"null".equals(baseTrainId)) {
					 //查询子表的数据
					 plans = runPlanService.findPlanTimeTableByPlanIdFromjbt2(planTrainId);
					
				 }
				 else {
					 //查询子表的数据
					 plans = runPlanService.findPlanTimeTableByPlanId2(planTrainId);
				 }
				List<RunLineItem2> items = new ArrayList<RunLineItem2>();
				for (int i = 0; i < plans.size(); i++) {

					Map<String, Object> map = plans.get(i);

					Job2 job = new Job2();
					RunLineItem2 runLineItem = new RunLineItem2();
					job.setCode(MapUtils.getString(map, "JOBS", ""));
					runLineItem.setJobs(job);
					runLineItem.setId(UUID.randomUUID().toString());
					runLineItem.setName(planDto.getTrainName() + "运行线条目" + i);
					runLineItem.setBureauId(MapUtils.getString(map,
							"STN_BUREAU_ID", ""));
					runLineItem.setBureauName(MapUtils.getString(map, "BUREAU",
							""));
					runLineItem.setBureauShortName(MapUtils.getString(map,
							"STNBUREAU", ""));
					runLineItem.setNodeId(MapUtils
							.getString(map, "NODE_ID", ""));
					runLineItem.setNodeName(MapUtils.getString(map,
							"NODE_NAME", ""));
					runLineItem.setTrackName(MapUtils.getString(map,
							"TRACK_NAME", ""));
					runLineItem.setPlatformName(MapUtils.getString(map,
							"PLATFORM", ""));
					
//					runLineItem.setNodeStationId(MapUtils.getString(map,"NODE_STATION_ID", ""));
//					runLineItem.setNodeStationName(MapUtils.getString(map,"NODE_STATION_NAME", ""));
					runLineItem.setNodeTdcsId(MapUtils.getString(map,"NODE_TDCS_ID", ""));
					runLineItem.setNodeTdcsName(MapUtils.getString(map,"NODE_TDCS_NAME", ""));
					
					runLineItem.setSourceTimeSchedule(CreateRunlineDataUtil.getTimeSchedule2(MapUtils.getString(map, "ARR_TIME"), 
							("null".equals(MapUtils.getString(map, "ARR_RUN_DAYS","")) ? 0 : Integer.valueOf(MapUtils.getString(map,"ARR_RUN_DAYS")))));
					runLineItem.setSourceTime(CreateRunlineDataUtil.getMillisecondTime1(planDto.getStartTime(), MapUtils.getString(map,"ARR_TIME"), runLineItem.getSourceTimeSchedule().getDates()));
					runLineItem.setSourceParentName(MapUtils.getString(map,
							"ARR_TRAIN_NBR"));
					if("<终到>".equals(runLineItem.getJobs().getCode())) {
						runLineItem.setTargetTimeSchedule(runLineItem.getSourceTimeSchedule());
						runLineItem.setTargetTime(runLineItem.getSourceTime());
					}
					else {
						runLineItem.setTargetTimeSchedule(CreateRunlineDataUtil.getTimeSchedule2(MapUtils.getString(map, "DPT_TIME"), 
								("null".equals(MapUtils.getString(map, "RUN_DAYS","")) ? 0 : Integer.valueOf(MapUtils.getString(map,"RUN_DAYS")))));
						runLineItem.setTargetTime(CreateRunlineDataUtil.getMillisecondTime1(planDto.getStartTime(), MapUtils.getString(map,"DPT_TIME"), runLineItem.getTargetTimeSchedule().getDates()));
					}
					
					runLineItem.setTargetParentName(MapUtils.getString(map,
							"DPT_TRAIN_NBR"));
					runLineItem.setParent(runlineModel);

					items.add(runLineItem);
				}

				createRunLineData.setTrainline(runlineModel);
				createRunLineData.setItems(items);
//				String jstr2 = JSONObject.fromObject(createRunLineData)
//						.toString();

				//System.out.println(jstr2);
				//logger.info("运行线全部数据： " + jstr2);

				sendMQMessageService.sendMq2(msgReceiveUrl, createRunLineData, planTrainId, user);
				//判断是否需要先删除运行线
//				if(null != planDto.getDailyPlanId() && !"".equals(planDto.getDailyPlanId())) {
//					sendMQMessageService.sendMq2(msgReceiveUrl, createRunLineData, planTrainId, planDto.getDailyPlanId(), user);
//				}
//				else if(null != planDto.getDailyPlanIdLast() && !"".equals(planDto.getDailyPlanIdLast())) {
//					sendMQMessageService.sendMq2(msgReceiveUrl, createRunLineData, planTrainId, planDto.getDailyPlanIdLast(), user);
//				}
//				else {
//					sendMQMessageService.sendMq2(msgReceiveUrl, createRunLineData, planTrainId, user);
//				}
			} catch (Exception e) {
    			e.printStackTrace();
    		}
			
		}
    	
    }
}
