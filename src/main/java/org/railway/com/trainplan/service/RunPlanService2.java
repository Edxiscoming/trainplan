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
import java.util.List;
import java.util.Map;
import java.util.UUID;



import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.utils.CommonUtil;
import org.railway.com.trainplan.entity.LastRunPlan;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.RunPlanStn;
import org.railway.com.trainplan.entity.TrainNbrAndStartTime;
import org.railway.com.trainplan.entity.TrainNbrDwrInfo;
import org.railway.com.trainplan.entity.UnitCross;
import org.railway.com.trainplan.entity.UnitCrossTrain;
import org.railway.com.trainplan.exceptions.WrongDataException;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.BaseTrainDao;
import org.railway.com.trainplan.repository.mybatis.PlanCrossDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanStnDao;
import org.railway.com.trainplan.repository.mybatis.UnitCrossDao;
import org.railway.com.trainplan.service.message.SendMsgService;
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
 * 客运计划服务类
 * Created by star on 5/12/14.
 */
@Component
@Transactional
@Monitored
@Scope("prototype")	
public class RunPlanService2 {

    private static final Log logger = LogFactory.getLog(RunPlanService2.class);

    @Autowired
    private RunPlanDao runPlanDao;

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

//    @Value("#{restConfig['plan.generatr.thread']}")
    private int threadNbr = 25;
	
    /**
     *
     * @param baseChartId 基本图id
     * @param startDate yyyy-MM-dd
     * @param days 比如:30
     * @return 生成了多少个plancross的计划
     */
    public List<String> generateRunPlan(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, 
    		boolean specialRuleIsSendMsg, int createType, String tokenVehBureau) {

    	GenerateRunPlan tt = new GenerateRunPlan(unitCross, startDate, days, msgReceiveUrl, specialRuleIsSendMsg, createType, tokenVehBureau);
		tt.run();
		return null;

    }

    
    //===============================
    
    class GenerateRunPlan {


        // 传入参数
        private UnitCross unitCross;

        private String startDate;

        private int days;

        private String msgReceiveUrl;

        private boolean specialRuleIsSendMsg = false;
        
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        private int createType = 0;
        
        private String tokenVehBureau;
        
        GenerateRunPlan(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, boolean specialRuleIsSendMsg, String tokenVehBureau) {

//            this.unitCross = unitCross;
//            this.startDate = startDate;
//            this.days = days;
//            this.specialRuleIsSendMsg = specialRuleIsSendMsg;
//            this.msgReceiveUrl = msgReceiveUrl;
        	this(unitCross, startDate, days, msgReceiveUrl, specialRuleIsSendMsg, 0, tokenVehBureau);
        }
        
        GenerateRunPlan(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, boolean specialRuleIsSendMsg, int createType, String tokenVehBureau) {

            this.unitCross = unitCross;
            this.startDate = startDate;
            this.days = days;
            this.specialRuleIsSendMsg = specialRuleIsSendMsg;
            this.msgReceiveUrl = msgReceiveUrl;
            this.createType = createType;
            this.tokenVehBureau = tokenVehBureau;
        }

        GenerateRunPlan(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, String tokenVehBureau) {

//            this.unitCross = unitCross;
//            this.startDate = startDate;
//            this.days = days;
//            this.msgReceiveUrl = msgReceiveUrl;
        	this(unitCross, startDate, days, msgReceiveUrl, 0, tokenVehBureau);
        }
        
        GenerateRunPlan(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, int createType, String tokenVehBureau) {

            this.unitCross = unitCross;
            this.startDate = startDate;
            this.days = days;
            this.msgReceiveUrl = msgReceiveUrl;
            this.createType = createType;
            this.tokenVehBureau = tokenVehBureau;
        }
        

        public void run() {
        	//deleteTest(unitCross.getCrossName(), startDate);
        	
        	
            //logger.debug("thread start:" + LocalTime.now().toString("hh:mm:ss"));
            try {
                // 查询同名交路，用来补全时间空挡，只查询生成过计划的交路（只有同一个基本图的交路才套用）
            	Map<String,Object> reqMap = new HashMap<String,Object>();
            	reqMap.put("unitCrossName", this.unitCross.getCrossName());
            	reqMap.put("baseChartId", this.unitCross.getBaseChartId());
                List<PlanCrossInfo> planCrossInfoList = planCrossDao.findByParamMap(reqMap);
                
                // 按启用时间排序
                Collections.sort(planCrossInfoList, new Comparator<PlanCrossInfo>() {
                    @Override
                    public int compare(PlanCrossInfo o1, PlanCrossInfo o2) {
                        LocalDate date1 = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(o1.getCrossEndDate());
                        LocalDate date2 = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(o2.getCrossEndDate());
                        if(date1.compareTo(date2) != 0) {
                            return date1.compareTo(date2);
                        } else {
                            date1 = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(o1.getCrossStartDate());
                            date2 = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(o2.getCrossStartDate());
                            return date1.compareTo(date2);
                        }
                    }
                });
               /******** 注释了以下代码，为解决生成交路计划的时候给界面推了两次信息 *************//*
                
//            	if(!(null != unitCross.getAppointWeek() && !"null".equals(unitCross.getAppointWeek()) &&
//            			!"".equals(unitCross.getAppointWeek()))) {            		           	
//	                // 已存在的最新的交路不是当前要生成计划的交路，则先补齐已存在交路(暂时取消该功能)
//	                if(planCrossInfoList.size() > 0 && !planCrossInfoList.get(planCrossInfoList.size() - 1).getUnitCrossId().equals(this.unitCross.getUnitCrossId())) {
//	                    PlanCrossInfo planCrossInfo = planCrossInfoList.get(planCrossInfoList.size() - 1);
//	                    //UnitCross unitCross = unitCrossDao.findById(planCrossInfo.getUnitCrossId());
//	                    UnitCross unitCross = unitCrossDao.findByName(planCrossInfo.getCrossName());
//	                    logger.error("576 unitCrossID ~~" +  unitCross.getUnitCrossId());
//	                    logger.error("576 unitCrossName ~~" +  unitCross.getCrossName());
//	                    logger.error("576 unitCrossTrainList ~~" +  unitCross.getUnitCrossTrainList().size());
//	                    generateRunPlan(this.startDate, 0, unitCross, false);
//	                }
//            	}
                // 生成这次请求的计划
                /********  *************/
               
                generateRunPlan(this.startDate, this.days, this.unitCross, true);
            } catch (WrongDataException e) {
                logger.error("数据错误：unitCross_id = " + this.unitCross.getUnitCrossId(), e);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("生成计划失败：unitCross_id = " + this.unitCross.getUnitCrossId(), e);
            }
            logger.debug("thread end:" + LocalTime.now().toString("hh:mm:ss"));
       }

        /**
         * 生成开行计划，可以重复生成。
         * 原则：保证生成的计划是在一个连续的时间区间内。
         * @param startDateStr 起始日期
         * @throws WrongDataException
         * @throws Exception
         */
        private void generateRunPlan(String startDateStr, int days, UnitCross unitCross, boolean sendMsg) throws WrongDataException, Exception {
            LocalDate startDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDateStr);
            //生成plan_cross逻辑
            //只要生成了第一开行计划，这个时候unitCross中的planCrossId就不该为空，但是按星期规律生成的时候，
            //这是一个连贯操作，unitCross中的planCrossId没有被重新赋值,所以这样操作是错误的
            String planCrossId = unitCross.getPlanCrossId();
            boolean isGenerationOnce = false;
            boolean isAppointWeek = false;
        	boolean isAppointDay = false;
        	if(null != unitCross.getAppointWeek() && !"null".equals(unitCross.getAppointWeek()) &&
        			!"".equals(unitCross.getAppointWeek())) 
        		isAppointWeek = true;
        	if(null != unitCross.getAppointDay() && !"null".equals(unitCross.getAppointDay()) &&
        			!"".equals(unitCross.getAppointDay()))
        		isAppointDay = true;
        	
        	if(isAppointWeek) {                         
        		isGenerationOnce = runPlanCreateAuxiliaryService.isGenerationOnce(unitCross);
        	}
        	else if(isAppointDay) {
        		isGenerationOnce = false;
        	}
            
            /***给界面推送一个交路开始的信息 added by liuhang***/
            if(sendMsg) {
                sendUnitCrossMsg(unitCross.getUnitCrossId(), 1);
            }
            /*************************/
            PlanCrossInfo planCrossInfo;
            // 按组别保存最后一个计划
            LastRunPlan lastRunPlan = null;
            RunPlan lastNextIdPlan = null;
            Map<Integer, RunPlan> lastRunPlans = Maps.newHashMap();
            if(planCrossId == null) { // 之前未生成过开行计划
                planCrossInfo = new PlanCrossInfo();
                BeanUtils.copyProperties(planCrossInfo, unitCross);
                planCrossInfo.setPlanCrossId(UUID.randomUUID().toString());
                planCrossInfo.setCrossStartDate(startDateStr);
                planCrossInfo.setIsAutoGenerate(1);
                planCrossId = planCrossInfo.getPlanCrossId();
//                planCrossInfo.setThroughline(unitCross.getThroughline());
//                logger.info("Throughline ~~~ " + unitCross.getThroughline());              
                lastRunPlan = this.getLastRunPlans(startDateStr, unitCross, true, true);
                lastRunPlans = lastRunPlan.getLastRunPlans();
                lastNextIdPlan = lastRunPlan.getRunPlan();
                planCrossDao.save(planCrossInfo);
            } else if(isAppointWeek || isAppointDay) {
            	//按特殊规律生成，特殊处理
            	planCrossInfo = crossService.getPlanCrossInfoForPlanCrossId(planCrossId);
            	if(null != planCrossInfo) {
            		 planCrossInfo.setThroughLine(unitCross.getThroughLine());
                     // 查询每组车最新的计划，作为新计划的前序车
            		 lastRunPlan = this.getLastRunPlans(startDateStr, unitCross, true, false);
                     lastRunPlans = lastRunPlan.getLastRunPlans();
                     lastNextIdPlan = lastRunPlan.getRunPlan();
//                     if(null == lastRunPlans || lastRunPlans.isEmpty()) {
//                     	unitCross.setPlanCrossId(null);
//                    	 generateRunPlan(startDateStr, days, unitCross, sendMsg);
//                    }  
            	}
            	else {
            		planCrossInfo = new PlanCrossInfo();
                    BeanUtils.copyProperties(planCrossInfo, unitCross);
                    planCrossInfo.setPlanCrossId(unitCross.getPlanCrossId());
                    planCrossInfo.setCrossStartDate(startDateStr);
                    planCrossId = planCrossInfo.getPlanCrossId();
                    planCrossInfo.setIsAutoGenerate(1);
//                    planCrossInfo.setThroughline(unitCross.getThroughline());
//                    logger.info("Throughline ~~~ " + unitCross.getThroughline());                  
                    lastRunPlan = this.getLastRunPlans(startDateStr, unitCross, true, true);            
                    lastRunPlans = lastRunPlan.getLastRunPlans();
                    lastNextIdPlan = lastRunPlan.getRunPlan();
                    planCrossDao.save(planCrossInfo);
            	}
            
            }
            else {
            	//这种情况，发生截断时，plancross不改变滚动生成状态，并且不删除plancross
            	 // 按时间删除已存在的开行计划，后面重新生成
                // 查询planCross对象，生成完开行计划后需要更新crossEndDate
                planCrossInfo = crossService.getPlanCrossInfoForPlanCrossId(planCrossId);
                planCrossInfo.setThroughLine(unitCross.getThroughLine());
                // 查询每组车最新的计划，作为新计划的前序车
                lastRunPlan = this.getLastRunPlans(startDateStr, unitCross);              
                lastRunPlans = lastRunPlan.getLastRunPlans();                   
//                if(null == lastRunPlans || lastRunPlans.isEmpty()) {
//                	unitCross.setPlanCrossId(null);
//               	 generateRunPlan(startDateStr, days, unitCross, sendMsg);
//               }  
                lastNextIdPlan = lastRunPlan.getRunPlan();
            }
            // 用来保存最后一个交路起点
            RunPlan lastStartPoint = null;
            final List<UnitCrossTrain> unitCrossTrainList = unitCross.getUnitCrossTrainList();
            // 交路使用的车
            List<RunPlan> baseRunPlanList = findBaseTrainByCrossName(unitCross.getCrossName());

            int totalGroupNbr = unitCross.getGroupTotalNbr();
            // 计算每组有几个车次
            if(unitCrossTrainList.size() % totalGroupNbr != 0) {
                throw new WrongDataException("交路数据错误，每组车数量不一样");
            }
            // 每组有几个车
            int trainCount = unitCrossTrainList.size() / totalGroupNbr;
            
            //第一组车和第二组车的间隔天数，如果是0，我们就可以判断其是对开的车
            //只取第一组和第二组无法判断，对开的真实情况是所有组加起来的组间间隔是0
          /*  int firstAndLastGroupintervalDays = 0;
            if(unitCross.getGroupTotalNbr() >= 2) {
	            for(int i=0; i<unitCross.getGroupTotalNbr(); i++) {
	            	firstAndLastGroupintervalDays = firstAndLastGroupintervalDays +  unitCrossTrainList.get(trainCount * i).getGroupGap();
	            }
            }*/
            
            //是否交替开行，这个值决定生成开行计划的时候，是否要把某一组的车全部生成完为止
            boolean isAlternateRun = true;
            if(unitCross.getAlternateDate().contains("-")) {
            	isAlternateRun = false;
            }
               
            // 计算结束时间
            LocalDate lastDate = startDate.plusDays(days);
            // 记录daygap
            int totalDayGap = 0;
            //特殊规律不补全
            //暂时都不补全
        	/*if(!(isAppointWeek || isAppointDay)) { 
		        // 一刀切后可能存在不完整交路，先补全不完整的交路
		        lastRunPlans = completeUnitCross(lastRunPlans, unitCross);
        	}*/
            
            //lastRunPlans = completeUnitCross(lastRunPlans, unitCross);

            // 找到最后结束的计划车组，然后继续生成下去
            int firstGroup = getLastEndedGroup(lastRunPlans, unitCross.getGroupTotalNbr());
            
            /**
             * 时间上最后结束的车组，不一定是我们需要的车组，应为对开的情况下，可能出现第二组车比第一组车先结束，
             *  但是我们任然要从第三组车开，又因为对开必须是偶数的组数，所以我们只需要判断第一组和第二组是否是这种情况,
             *  如果是这种情况，那么firstGroup = firstGroup + 1;
             */
         
//            if(firstGroup != 0 && unitCross.getCrossName().equals("Z161-Z162")) {
//            	firstGroup = firstGroup + 1;
//            }
            //为什么要-1 ?
            //int initTrainSort = (firstGroup - 1) * trainCount;

            int initTrainSort = firstGroup * trainCount;
            
            //在生成开行计划时，开行时间是否增加组间隔天数
            boolean isAddtotalDayGap = false;
            if(0 == firstGroup || 0 == initTrainSort % unitCrossTrainList.size()) {
            	isAddtotalDayGap = true;
            }
            // 继续生成
            generate: {
//            	logger.error("654 unitCrossName ~~" +  unitCross.getCrossName());
//            	logger.error("654 unitCrossTrainList ~~" +  unitCross.getUnitCrossTrainList().size());
                for(int i = initTrainSort; i < 10000; i ++) {
//                	 logger.error("657 unitCrossTrainList i~~" +  i);
                    UnitCrossTrain unitCrossTrain = unitCrossTrainList.get(i % unitCrossTrainList.size());
                    LocalDate runDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                    LocalDate endDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getEndDate()); 
                    int interval = Days.daysBetween(runDate, endDate).getDays();

                    for(RunPlan baseRunPlan: baseRunPlanList) {
                        if (unitCrossTrain.getBaseTrainId().equals(baseRunPlan.getBaseTrainId())) {
                            try {
                                RunPlan runPlan = (RunPlan) BeanUtils.cloneBean(baseRunPlan);
                                // 克隆的对象里，列表没有克隆，重新new一个列表
                                runPlan.setRunPlanStnList(new ArrayList<RunPlanStn>());

                                // 基本信息
                                runPlan.setPlanTrainId(UUID.randomUUID().toString());
                                runPlan.setPlanCrossId(planCrossId);
                                runPlan.setBaseChartId(baseRunPlan.getBaseChartId());
                                runPlan.setBaseTrainId(baseRunPlan.getBaseTrainId());
                                runPlan.setDailyPlanFlag(1);
                                runPlan.setCreateType(createType);
                                runPlan.setTokenVehBureau(tokenVehBureau);
                                // unitcross里的信息
                                runPlan.setGroupSerialNbr(unitCrossTrain.getGroupSerialNbr());
                                runPlan.setTrainSort(unitCrossTrain.getTrainSort());
                                runPlan.setMarshallingName(unitCrossTrain.getMarshallingName());
                                runPlan.setTrainNbr(unitCrossTrain.getTrainNbr());
                                runPlan.setDayGap(unitCrossTrain.getDayGap());
                                runPlan.setSpareFlag(unitCrossTrain.getSpareFlag());
                                runPlan.setSpareApplyFlag(unitCrossTrain.getSpareApplyFlag());
                                runPlan.setHighLineFlag(unitCrossTrain.getHighLineFlag());
                                runPlan.setHightLineRule(unitCrossTrain.getHighLineFlag());
                                runPlan.setCommonLineRule(unitCrossTrain.getCommonLineRule());
                                runPlan.setAppointWeek(unitCrossTrain.getAppointWeek());
                                runPlan.setAppointDay(unitCrossTrain.getAppointDay());
                                
                                RunPlan preRunPlan = lastRunPlans.get(runPlan.getGroupSerialNbr());
                                
                                //System.err.println(unitCrossTrain.getGroupSerialNbr() + " : " + unitCrossTrain.getTrainNbr() + " : " + unitCrossTrain.getTrainSort());
                                if(preRunPlan != null) { 
                                	// 有前序车
                                    // 当前列车的开始日期，是前车的结束日期+daygap(这样计算不准确，当换组的时候，没有考虑到组间间隔)
                                    //LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                                	//LocalDate preStartDate = LocalDate.fromDateFields(new Date(preRunPlan.getStartDateTime().getTime()));
                                    //runPlan.setRunDate(preEndDate.plusDays(unitCrossTrain.getDayGap()).toString("yyyyMMdd"));
                                	
                                	
                                	//新的算法
/*                                	if(1 == runPlan.getTrainSort()) {
                                		LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                                		runPlan.setRunDate(preEndDate.plusDays(unitCrossTrain.getGroupGap()).toString("yyyyMMdd"));
                                		//此种情况说明换组，换组就要考虑组间间隔
                                		//换组的车还应该考虑上一次该组车的开行时间
                                		//换组又换到第一组的，说明一个轮回完了，这个时候要跳出现有框架来
                                		if(1 == runPlan.getGroupSerialNbr()) {
                                			                              			
                                			//每组车与上一组的间隔天数相加，得到最后一组车和第一组车的间隔天数总和
                                			//最后一组车的最后一个车的结束时间减去这个间隔总和，就得到第一组车的最后一个车的结束时间
                                			
                                			//要计算出第一组车的上一次开行中，最后一个车次的结束时间
                                			LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                                			LocalDate preFirstGroupLastTrainEndDate = preEndDate.minusDays(firstAndLastGroupintervalDays);
                                			
                                			runPlan.setRunDate(preFirstGroupLastTrainEndDate.plusDays(unitCrossTrain.getDayGap()).toString("yyyyMMdd"));
                                		}
                                		else {
                                    		//LocalDate preStartDate = LocalDate.fromDateFields(new Date(preRunPlan.getStartDateTime().getTime()));
                                			LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                                    		runPlan.setRunDate(preEndDate.plusDays(unitCrossTrain.getGroupGap()).toString("yyyyMMdd"));
                                		}

                                    	
                                	}
                                	else {
                                		//不换组的，就只需要考虑组内间隔
                                		LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                                		runPlan.setRunDate(preEndDate.plusDays(unitCrossTrain.getDayGap()).toString("yyyyMMdd"));
                                	}*/
                                	
                                	if(i % unitCrossTrainList.size() == 0) {
                                		//说明有前序车，切是第一组的第一个车在开，这种现象说明这个车在开第二轮，此时进行判断，时候需要开第二轮，需要就开，不需要就跳出
                                		if(isGenerationOnce) {
                                    		planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
                                    		logger.error("end a runplan! ************************************************");
                                    		break generate; 
                                		}
                                	}

                                	if((runPlan.getTrainSort() - preRunPlan.getTrainSort())==1 || ((runPlan.getTrainSort() - preRunPlan.getTrainSort())<0 && 
                                			runPlan.getTrainSort() == 1)) {
                                		 // 前后车互基
                                    	LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                                		runPlan.setRunDate(preEndDate.plusDays(unitCrossTrain.getDayGap()).toString("yyyyMMdd"));
                                        runPlan.setPreTrainId(preRunPlan.getPlanTrainId());
                                        preRunPlan.setNextTrainId(runPlan.getPlanTrainId());
                                	}
                                	else {
                                		break;
                                	}
                                	
                                   
                                } else if(i == 0) { // 第一组第一个车
                                	//换了图或者交路单元的车第一次生成开行计划，肯定是进入这个判断条件，增加前序和后续id
                                	if(null != lastNextIdPlan) {
                                		lastNextIdPlan.setNextTrainId(runPlan.getPlanTrainId());
                                		runPlan.setPreTrainId(lastNextIdPlan.getPlanTrainId());
                                		runPlanDao.updateNextTrainId(lastNextIdPlan);
                                		System.err.println("lastNextIdPlan : " + lastNextIdPlan.getTokenVehBureau());
                                	}
                                	
                                    LocalDate unitCrossTrainStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                                    int initInterval = Days.daysBetween(unitCrossTrainStartDate, startDate).getDays();
                                    runPlan.setRunDate(unitCrossTrainStartDate.plusDays(initInterval).toString("yyyyMMdd"));
                                } else { // 每组第一个车（除了第一组）
                                	/**
                                	 * 当截断过原开行计划后，新生成的开行计划以startDate为准，所以这个时候开行的第一个
                                	 * 车的开行日期，不能去加上组与组之间的间隔天数。
                                	 */

                                    LocalDate unitCrossTrainStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                                    int initInterval = Days.daysBetween(unitCrossTrainStartDate, startDate).getDays();
                                   
                                    //0 == initInterval 说明startDate和车次的开行时间一样，说明非第一组车第一次开行的时间与交路开行计划开行时间相同                                    
                                    if(isAddtotalDayGap) {
                                    	totalDayGap += unitCrossTrain.getGroupGap();
                                    	
                                    }
                                    //无论第一次是否增加了组间间隔天数，从第二次开始，都要增加
                                    if(!isAddtotalDayGap) 
                                    	isAddtotalDayGap = true;
                                    runPlan.setRunDate(unitCrossTrainStartDate.plusDays(initInterval).plusDays(totalDayGap).toString("yyyyMMdd"));
                                }

                                runPlan.setStartDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).toString("yyyy-MM-dd") + " " + runPlan.getStartTimeStr()).getTime()));
                                runPlan.setEndDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).plusDays(interval).toString("yyyy-MM-dd") + " " + runPlan.getEndTimeStr()).getTime()));
                                runPlan.setPlanTrainSign(runPlan.getRunDate() + "-" + runPlan.getTrainNbr() + "-" + runPlan.getStartStn() + "-" + runPlan.getStartTimeStr());

                                //在这里做一次判断，如果是某组第一个车， runPlan.setRunDate的日期大于lastDate，则不保存，直接跳出
                                //LocalDate lastStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate());
                                
                                //组间间隔等于0的，用这种方式退出
                                if(isAlternateRun && totalGroupNbr > 1) {
                                	  if((DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).compareTo(lastDate) > 0)) {
                                		  if(totalGroupNbr == runPlan.getGroupSerialNbr()) {
                                        		planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
                                        		logger.error("end a runplan! ************************************************");
                                        		break generate;
                                        	}
                              			  else {
                              				 break;
                              			  }
                                      }
                                	
                                
                                }
                                

                                // 保存当前处理组的第一个车
//                                if(isAppointWeek && isAppointDay ) {
//                                	
//                                }
//                                
//                                if(i % trainCount == 0) {
//                                    lastStartPoint = runPlan;
//                                }
                                // 保存每组车的最后一个车
                                lastRunPlans.put(runPlan.getGroupSerialNbr(), runPlan);

                                runPlanDao.addRunPlan(runPlan);
                                //runPlanStnDao.addRunPlanStn(runPlan.getRunPlanStnList());
                                if(null != preRunPlan) {
                                	  runPlanDao.updateNextTrainId(preRunPlan);                                   
                                }
                              
                                if(sendMsg) {
                                    sendRunPlanMsg(unitCross, runPlan);
                                }
                                // 如果有一组车的第一辆车的开始日期到了计划最后日期，就停止生成
                                //(修改，如果有一组车的任意一个车次的开行时间大于等于结束时间，那么这组车停止生成)
                                LocalDate lastStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate());
                                
/*                                if(unitCross.getCrossName().equals("0C2614-C2614-G383-G384-0C2613") || unitCross.getCrossName().equals("0C2614-C2614-G383-G384-C2613-0C2613")) { 
                                	lastStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate());
                                }*/
                                
                                if(!isAlternateRun || totalGroupNbr == 1) {
                                	if(((i % trainCount) == (trainCount - 1)) && (lastStartDate.compareTo(lastDate) >= 0)) {                               	 
	                        		  planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
	                        		  logger.error("end a runplan! ************************************************");
	                        		  break generate;
	                            	}     
                                	
//                                	if(!isAppointDay) {
//    	                                if(((i % trainCount) == (trainCount - 1)) && (lastStartDate.compareTo(lastDate) >= 0)) {                               	 
//    	                        		  planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
//    	                                  break generate;
//    	                            	}                                                                  
//                                    }
//                                    else {
//                                    	if(((i % trainCount) == (trainCount - 1)) && (lastStartDate.compareTo(lastDate) >= 0)) {                               	 
//      	                        		  planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
//      	                                  break generate;
//      	                            	}     
//                                    }
                                }                            
//                                //组间间隔大于0的，用这种方式退出
//                                if(!isAppointDay) {
//                                	 if(firstAndLastGroupintervalDays > 0) {
//     	                                if(((i % trainCount) == (trainCount - 1)) && (lastStartDate.compareTo(lastDate) >= 0)) {     	                                	
// 		                                	planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
// 		                                    break generate;     	                               	
//     	                                }
//                                     }
//                                }
//                                else {
//                                	 if(firstAndLastGroupintervalDays > 0) {
//     	                                if(((i % trainCount) == (trainCount - 1)) && (lastStartDate.compareTo(lastDate) > 0)) {
//     	                                	if(!isAppointDay) {
//     		                                	planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
//     		                                    break generate;
//     	                                	}
//     	                                }
//                                     }
//                                }
                              
                                
/*                                if(1 == runPlan.getTrainSort() && (lastStartDate.compareTo(lastDate) >= 0)) {
                                    planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
                                    break generate;
                                }*/
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
            if(sendMsg) {
            	if(isAppointWeek || isAppointDay) {
            		if(specialRuleIsSendMsg) {
            			sendUnitCrossMsg(unitCross.getUnitCrossId(), 2);
            		}
            	}
            	else {           		            	
            		sendUnitCrossMsg(unitCross.getUnitCrossId(), 2);
            	}
            }
        }
        
        private List<RunPlan> findBaseTrainByCrossName(String crossName) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("crossName", crossName);
            return baseTrainDao.findBaseTrainByCrossName(params);
        }

        /**
         * 用传入的交路参数补全不完整的交路计划
         * @param lastRunPlans
         * @return
         * @throws NoSuchMethodException
         * @throws InstantiationException
         * @throws IllegalAccessException
         * @throws ParseException
         * @throws InvocationTargetException
         */
        private Map<Integer, RunPlan> completeUnitCross(Map<Integer, RunPlan> lastRunPlans, UnitCross unitCross) throws NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException, InvocationTargetException {
        	List<RunPlan> baseRunPlanList = findBaseTrainByCrossName(unitCross.getCrossName());
            Map<Integer, RunPlan> newMap = Maps.newHashMap(lastRunPlans);
            for(RunPlan runPlan: Lists.newArrayList(lastRunPlans.values())) {
                newMap.put(runPlan.getGroupSerialNbr(), generateRunPlan(runPlan, unitCross, baseRunPlanList));
            }
            return newMap;
        }

        private RunPlan generateRunPlan(RunPlan preRunPlan, UnitCross unitCross, List<RunPlan> baseRunPlanList) throws InvocationTargetException, NoSuchMethodException, InstantiationException, ParseException, IllegalAccessException {
            UnitCrossTrain unitCrossTrain;
            RunPlan runPlan = preRunPlan;
            int groupSeriaNbr = preRunPlan.getGroupSerialNbr();
            int trainSort = preRunPlan.getTrainSort();
            while((unitCrossTrain = getNextUnitCrossTrain(groupSeriaNbr, trainSort, unitCross)) != null) {

                LocalDate runDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getRunDate());
                LocalDate endDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(unitCrossTrain.getEndDate());
                int interval = Days.daysBetween(runDate, endDate).getDays();

                for(RunPlan baseRunPlan: baseRunPlanList) {
                    if (unitCrossTrain.getBaseTrainId().equals(baseRunPlan.getBaseTrainId())) {
                        try {
                            runPlan = (RunPlan) BeanUtils.cloneBean(baseRunPlan);
                            // 克隆的对象里，列表没有克隆，重新new一个列表
                            runPlan.setRunPlanStnList(new ArrayList<RunPlanStn>());

                            // 基本信息
                            runPlan.setPlanTrainId(UUID.randomUUID().toString());
                            runPlan.setPlanCrossId(unitCross.getPlanCrossId());
                            runPlan.setBaseChartId(baseRunPlan.getBaseChartId());
                            runPlan.setBaseTrainId(baseRunPlan.getBaseTrainId());
                            runPlan.setDailyPlanFlag(1);

                            // unitcross里的信息
                            runPlan.setGroupSerialNbr(unitCrossTrain.getGroupSerialNbr());
                            runPlan.setTrainSort(unitCrossTrain.getTrainSort());
                            runPlan.setCreateType(createType);
                            runPlan.setTokenVehBureau(tokenVehBureau);
                            runPlan.setMarshallingName(unitCrossTrain.getMarshallingName());
                            runPlan.setTrainNbr(unitCrossTrain.getTrainNbr());
                            runPlan.setDayGap(unitCrossTrain.getDayGap());
                            runPlan.setSpareFlag(unitCrossTrain.getSpareFlag());
                            runPlan.setSpareApplyFlag(unitCrossTrain.getSpareApplyFlag());
                            runPlan.setHighLineFlag(unitCrossTrain.getHighLineFlag());
                            runPlan.setHightLineRule(unitCrossTrain.getHighLineFlag());
                            runPlan.setCommonLineRule(unitCrossTrain.getCommonLineRule());
                            runPlan.setAppointWeek(unitCrossTrain.getAppointWeek());
                            runPlan.setAppointDay(unitCrossTrain.getAppointDay());

                            // 当前列车的开始日期，是前车的结束日期+daygap
                            LocalDate preEndDate = LocalDate.fromDateFields(new Date(preRunPlan.getEndDateTime().getTime()));
                            runPlan.setRunDate(preEndDate.plusDays(unitCrossTrain.getDayGap()).toString("yyyyMMdd"));

                            // 前后车互基
                            runPlan.setPreTrainId(preRunPlan.getPlanTrainId());
                            preRunPlan.setNextTrainId(runPlan.getPlanTrainId());

                            runPlan.setStartDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).toString("yyyy-MM-dd") + " " + runPlan.getStartTimeStr()).getTime()));
                            runPlan.setEndDateTime(new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate()).plusDays(interval).toString("yyyy-MM-dd") + " " + runPlan.getEndTimeStr()).getTime()));
                            runPlan.setPlanTrainSign(runPlan.getRunDate() + "-" + runPlan.getTrainNbr() + "-" + runPlan.getStartStn() + "-" + runPlan.getStartTimeStr());

                            //LocalDate runPlanDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runPlan.getRunDate());
                            // 计算计划从表信息
//                            List<RunPlanStn> runPlanStnList = baseRunPlan.getRunPlanStnList();
//                            for(RunPlanStn baseRunPlanStn: runPlanStnList) {
//                                RunPlanStn runPlanStn = (RunPlanStn) BeanUtils.cloneBean(baseRunPlanStn);
//                                runPlan.getRunPlanStnList().add(runPlanStn);
//                                runPlanStn.setPlanTrainId(runPlan.getPlanTrainId());
//                                runPlanStn.setPlanTrainStnId(UUID.randomUUID().toString());
//                                runPlanStn.setArrTime(new Timestamp(simpleDateFormat.parse(runPlanDate.plusDays(runPlanStn.getsRunDays()).toString() + " " + runPlanStn.getArrTimeStr()).getTime()));
//                                runPlanStn.setDptTime(new Timestamp(simpleDateFormat.parse(runPlanDate.plusDays(runPlanStn.gettRunDays()).toString() + " " + runPlanStn.getDptTimeStr()).getTime()));
//                                runPlanStn.setBaseArrTime(runPlanStn.getArrTime());
//                                runPlanStn.setBaseDptTime(runPlanStn.getDptTime());
//                            }

                            runPlanDao.addRunPlan(runPlan);
                            //runPlanStnDao.addRunPlanStn(runPlan.getRunPlanStnList());
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
         * 根据plancrossid查询前序列车，日期有重叠的开行计划，按新日期一刀切
         * 1、不管三七二十一，切一刀再说
         * 2、查询每组车的最后一次生成的每个车（可以和unitcrosstrain匹配），通过参数lastRunPlans传出去
         * @param unitCross 交路信息全部传进来
         * @return 按groupserianbr保存最新计划
         */
        private LastRunPlan getLastRunPlans(String startDate, UnitCross unitCross) throws ParseException {
        	return getLastRunPlans(startDate, unitCross, false, false);
        }
        
        private LastRunPlan getLastRunPlans(String startDate, UnitCross unitCross, boolean isOnlyTruncation, boolean isDeletePlanCross) throws ParseException {
        	LastRunPlan lastRunPlan = new LastRunPlan();
        	boolean isAppointWeek = false;
          	boolean isAppointDay = false;
          	if(null != unitCross.getAppointWeek() && !"null".equals(unitCross.getAppointWeek()) &&
          			!"".equals(unitCross.getAppointWeek())) 
          		isAppointWeek = true;
          	if(null != unitCross.getAppointDay() && !"null".equals(unitCross.getAppointDay()) &&
          			!"".equals(unitCross.getAppointDay()))
          		isAppointDay = true;
          	
            //是否交替开行，这个值决定生成开行计划的时候，是否要把某一组的车全部生成完为止
            boolean isAlternateRun = true;
            if(unitCross.getAlternateDate().contains("-")) {
            	isAlternateRun = false;
            }
        	
          	//String alternateDate = unitCross.getAlternateDate();
          	if(isAlternateRun) {
          		//如果交路交替日期是一个单独的日期，说明不需要按车次去截断交路
          		// 按时间切一刀
                Map<String, Object> params = Maps.newHashMap();
                params.put("unitCrossName", unitCross.getCrossName());
//                ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
//        				.getSubject().getPrincipal(); 
                
                //增加截断条件————但当局
        		params.put("tokenVehBureau", tokenVehBureau);
        		//增加截断条件————交替日期,又按交路名去删除修改为按车次去删除
        		
                //如果不截断原交路就增加这个条件
                if(1 != unitCross.getCutOld()) {
                	params.put("baseChartId", unitCross.getBaseChartId());
                }           
                params.put("startTime", new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDate).toString("yyyy-MM-dd") + " 00:00:00").getTime()));
                
                //删除plan_train的同时应该删除plan_train_stn
                runPlanStnDao.deleteRunPlanStnByStartTime(params);
                //再删除plan_train
                runPlanDao.deleteRunPlanByStartTime(params);
                if(isDeletePlanCross) {
                	 //如果plancross的plan被删除完了，就应该连同plancross一起删除
                    runPlanDao.deletePlanCrossIfNoPlanTrain(params);
                }                       
          	}
          	else {
	          	//如果交路交替日期不是一个单独的日期，而是一组日期，说明需要按车次去截断交路
	          	// 按时间切一刀
          		List<TrainNbrAndStartTime> trainNbrAndStartTimes = runPlanCreateAuxiliaryService.getTrainNbrAndStartTime(startDate, unitCross);
          		Map<String, Object> params = Maps.newHashMap();
          		for(TrainNbrAndStartTime trainNbrAndStartTime : trainNbrAndStartTimes) {			
          			//根据车次的交替时间，按每个车次的不同，循环删除    			
                    params.put("trainNbr",  trainNbrAndStartTime.getTrainNbr());
                    params.put("unitCrossName", unitCross.getCrossName());
//                    ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
//            				.getSubject().getPrincipal(); 
                    
                    //增加截断条件————但当局
            		params.put("tokenVehBureau", tokenVehBureau);
            		//增加截断条件————交替日期,又按交路名去删除修改为按车次去删除
            		
                    //如果不截断原交路就增加这个条件
                    if(1 != unitCross.getCutOld()) {
                    	params.put("baseChartId", unitCross.getBaseChartId());
                    }           
                    params.put("startTime", new Timestamp(simpleDateFormat.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(trainNbrAndStartTime.getStartDate()).toString("yyyy-MM-dd") + " 00:00:00").getTime()));
                    
                    //删除plan_train的同时应该删除plan_train_stn
                    runPlanStnDao.deleteRunPlanStnByStartTime(params);
                    //再删除plan_train
                    runPlanDao.deleteRunPlanByStartTime(params);                             
          		}
          		 if(isDeletePlanCross) {
                	 //如果plancross的plan被删除完了，就应该连同plancross一起删除
                    runPlanDao.deletePlanCrossIfNoPlanTrain(params);
                }                     
                
          	}
        	
        	
            
            // 每组车的最新一组开行计划
            Map<Integer, RunPlan> lastRunPlans = Maps.newHashMap();
            //是否只进行截断操作，如果是，则在这里直接返回
            //只进行截断操作的条件，新的基本图第一次生成，生成的时间与老图重合，需要截断老图
 
			//由于不同基本图不能互相去补，所以这个除了crossName再增加一个限制条件
            //List<RunPlan> preGroup = runPlanDao.findPreRunPlanByPlanCrossName(unitCross.getCrossName());
		 	Map<String, Object> paramMap = Maps.newHashMap();
		 	paramMap.put("unitCrossName", unitCross.getCrossName());
		 	paramMap.put("baseChartId", unitCross.getBaseChartId());
			List<RunPlan> preGroup = runPlanDao.findPreRunPlanByParam(paramMap);
            if(!(isOnlyTruncation || isAppointWeek || isAppointDay)) {            	
	            for(RunPlan runPlan: preGroup) {
	                lastRunPlans.put(runPlan.getGroupSerialNbr(), runPlan);
	            }
            }
            Collections.sort(preGroup, new Comparator<RunPlan>() {
                @Override
                public int compare(RunPlan o1, RunPlan o2) {
                    LocalDate t1 = LocalDate.fromDateFields(new Date(o1.getEndDateTime().getTime()));
                    LocalDate t2 = LocalDate.fromDateFields(new Date(o2.getEndDateTime().getTime()));
                    return t1.compareTo(t2);
                }
            });
            if(preGroup.size() > 0) {
                RunPlan runPlan = preGroup.get(preGroup.size() - 1);
                
                //判断新的startDate跟老的开行计划是否接续了，没接续的，不连接起来
                if(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).plusDays(1).
                		compareTo(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDate)) >= 0) {
                	lastRunPlan.setRunPlan(runPlan);
                }
                else {
                	lastRunPlan.setRunPlan(null);
                }
                
                //lastRunPlans.put(preGroup.get(preGroup.size() - 2).getGroupSerialNbr(), preGroup.get(preGroup.size() - 2));
                //lastRunPlans.put(runPlan.getGroupSerialNbr(), runPlan);
                PlanCrossInfo planCrossInfo = planCrossDao.findById(runPlan.getPlanCrossId());
                planCrossInfo.setCrossEndDate(LocalDate.fromDateFields(new Date(runPlan.getEndDateTime().getTime())).toString("yyyyMMdd"));
                //被截断的交路不再自动滚动生成开行计划
                if(isDeletePlanCross && planCrossInfo.getIsAutoGenerate()!= 0 ) {            	
                	 planCrossInfo.setIsAutoGenerate(0);
                }
                planCrossDao.update(planCrossInfo);
            }
            	
            
//            else {
//            	//plancross为null，说明生成了新的unitcross，这种情况不需要补全，但是如果是连续的，则需要补上前序id和后续id
//            	if(!(isAppointWeek || isAppointDay)) { 
//            		
//            	}
//            }
            lastRunPlan.setLastRunPlans(lastRunPlans);
            return lastRunPlan;
        }

        /**
         * 根据groupNbr和trainsort获取同一组车的下一辆车
         * @param groupNbr 组号
         * @param trainSort 车号
         * @return
         */
        private UnitCrossTrain getNextUnitCrossTrain(int groupNbr, int trainSort, UnitCross unitCross) {
            final List<UnitCrossTrain> unitCrossTrainList = unitCross.getUnitCrossTrainList();
            for(UnitCrossTrain unitCrossTrain: unitCrossTrainList) {
                if(groupNbr == unitCrossTrain.getGroupSerialNbr() && trainSort == unitCrossTrain.getTrainSort() - 1) {
                    return unitCrossTrain;
                }
            }
            return null;
        }

        /**
         * 找到最先结束的交路组数，后面就从这个交路开始继续生成计划
         * @param lastRunPlans
         * @return
         */
        private int getFirstEndedGroup(Map<Integer, RunPlan> lastRunPlans) {
            if(lastRunPlans.values().size() == 0) {
                return 0;
            }
            List<RunPlan> runPlanList = Lists.newArrayList(lastRunPlans.values());
            Collections.sort(runPlanList, new Comparator<RunPlan>() {
                @Override
                public int compare(RunPlan o1, RunPlan o2) {
                    return o1.getEndDateTime().compareTo(o2.getEndDateTime());
                }
            });
            return runPlanList.get(0).getGroupSerialNbr();
        }
        
        /**
         * 找到最后结束的交路组数，后面就从这个交路开始继续生成计划
         * @param lastRunPlans
         * @return
         */
        private int getLastEndedGroup(Map<Integer, RunPlan> lastRunPlans, int GroupTotalNbr) {
            if(lastRunPlans.values().size() == 0) {
                return 0;
            }
            List<RunPlan> runPlanList = Lists.newArrayList(lastRunPlans.values());
            Collections.sort(runPlanList, new Comparator<RunPlan>() {
                @Override
                public int compare(RunPlan o1, RunPlan o2) {
                    return o1.getEndDateTime().compareTo(o2.getEndDateTime());
                }
            });
            //return runPlanList.get(runPlanList.size() - 1).getGroupSerialNbr();
            return getRealLastEndedGroup(runPlanList, GroupTotalNbr);
        }
        
        private int getRealLastEndedGroup(List<RunPlan> runPlanList, int GroupTotalNbr) {
        	int maxNbr = 0;
        	
        	for(int i=0; i<(runPlanList.size() % GroupTotalNbr != 0 ? runPlanList.size() % GroupTotalNbr : GroupTotalNbr); i++) {
        		if(runPlanList.get(runPlanList.size() - 1 -i).getGroupSerialNbr() > maxNbr) {
    				maxNbr = runPlanList.get(runPlanList.size() - 1 -i).getGroupSerialNbr();
    			}
        	}        	
			return maxNbr;        	
        }

        private void sendRunPlanMsg(UnitCross unitCross, RunPlan runPlan) {
            if(this.msgReceiveUrl == null) {
                return;
            }
            String unitCrossId = unitCross.getUnitCrossId();
            String crossName = unitCross.getCrossName();
            
            Map<String, TrainNbrDwrInfo> trainNbrMap = CommonUtil.getUnitCrossNameMap(crossName);
            Map<String, Object> msg = Maps.newHashMap();
            //crossName.
            //先判断是否有重复车次出现的交路
        
        	List<Integer> sorts = trainNbrMap.get(runPlan.getTrainNbr()).getSorts();
        	if(trainNbrMap.get(runPlan.getTrainNbr()).getTimes() > 1) {
        		 //如果有重复的车次出现,进入判断，重置trainSort
        		 
        		 for(int i = 0; i<sorts.size(); i++) {
        			 if(sorts.get(i) == runPlan.getTrainSort()) {
        				 msg.put("trainSort", runPlan.getTrainSort());
        				 break;
        			 }
        			 else {
        				 if(i+1 < sorts.size()) {
        					 if(sorts.get(i) < runPlan.getTrainSort() && runPlan.getTrainSort() < sorts.get(i + 1)) {
        						 msg.put("trainSort", sorts.get(i));
                				 break;
        					 }
        				 }
        			 }
        				 
        		 }
            	
	    	 }
	    	 else {
	    		 msg.put("trainSort", sorts.get(0));
	    	 }
            	  
            msg.put("unitCrossId", unitCrossId);
            msg.put("trainNbr", runPlan.getTrainNbr());
            msg.put("day", runPlan.getRunDate());
            msg.put("runFlag", runPlan.getSpareFlag());
            msg.put("startStn", runPlan.getStartStn());
            msg.put("endStn", runPlan.getEndStn());
           
            ObjectMapper jsonUtil = new ObjectMapper();

            try {
                msgService.sendMessage(jsonUtil.writeValueAsString(msg), this.msgReceiveUrl, "updateTrainRunPlanDayFlag");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送消息失败", e);
            }
        }
        
        /**
         * 推送某个交路的开始生成计划或结束结束生成计划
         * @param unitCrossId
         * @param status 1：未完成 2：完成
         * @throws JsonProcessingException
         */
        private void sendUnitCrossMsg(String unitCrossId, int status) throws JsonProcessingException {
            if(this.msgReceiveUrl == null) {
                return;
            }
            Map<String, Object> msg = Maps.newHashMap();
            msg.put("unitCrossId", unitCrossId);
            msg.put("status", status);
            ObjectMapper jsonUtil = new ObjectMapper();

            try {           	
                msgService.sendMessage(jsonUtil.writeValueAsString(msg), this.msgReceiveUrl, "updateTrainRunPlanStatus");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送消息失败", e);
            }
        }
    
    } 
	
//	 public void deleteTest(String crossName, String startTime) throws RuntimeException{
//	 Map<String, Object> params = Maps.newHashMap();
//	 SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//     params.put("unitCrossName", crossName);         
//     try {
//			params.put("startTime", new Timestamp(simpleDateFormat1.parse(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startTime).toString("yyyy-MM-dd") + " 00:00:00").getTime()));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//     
//     //删除plan_train的同时应该删除plan_train_stn
//     runPlanStnDao.deleteRunPlanStnByStartTime(params);
//     //再删除plan_train
//     runPlanDao.deleteRunPlanByStartTime(params);
//     System.out.println("just a test");
//     //throw new RuntimeException("error");
//}
}
