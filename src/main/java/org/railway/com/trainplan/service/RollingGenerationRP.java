package org.railway.com.trainplan.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplan.web.dto.RunPlanRollingDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmCrossServiceImpl;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmOriginalCrossServiceImpl;
import com.railway.passenger.transdispatch.operationplan.service.impl.OperationlanServiceImpl;
import com.railway.passenger.transdispatch.util.CrossInfoGenHelper;
import com.railway.passenger.transdispatch.util.LogUtil;
import com.railway.passenger.transdispatch.util.TimeUtils;

/**
 * 滚动生成开行计划任务
 * 
 * @author Administrator
 * 
 */

@Component
@Transactional
@Monitored
public class RollingGenerationRP extends QuartzJobBean {

	private static Log logger = LogFactory.getLog(RollingGenerationRP.class
			.getName());

	private RunPlanService runPlanService;	
	private SchemeService schemeService;	
	private ApplicationContext applicationContext = null;
	
	private OperationlanServiceImpl operationlanServiceImpl;
	private CmCrossServiceImpl cmCrossServiceImpl;
	private CmOriginalCrossServiceImpl cmOriginalCrossServiceImpl;
	
	//特殊规律的开行计划的维护天数
	private int maintainRunplanDays = 40;
	private ExecutorService pool = Executors.newFixedThreadPool(10);
	private CompletionService<String> completion = new ExecutorCompletionService<String>(pool);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("RollingGenerationRP is begin!");
		
		//注入服务类
		 try {
			applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContextKey");
			schemeService = applicationContext.getBean("schemeService", SchemeService.class);
			runPlanService = applicationContext.getBean("runPlanService", RunPlanService.class);
			
			operationlanServiceImpl = applicationContext.getBean("operationlanServiceImpl", OperationlanServiceImpl.class);
			cmCrossServiceImpl = applicationContext.getBean("cmCrossServiceImpl", CmCrossServiceImpl.class);
			cmOriginalCrossServiceImpl = applicationContext.getBean("cmOriginalCrossServiceImpl", CmOriginalCrossServiceImpl.class);
			
			logger.error("RollingGenerationRP is start !##############################");
		 } catch (SchedulerException e) {	
			e.printStackTrace();
		}
		 
		//执行程序
//		 doWork();
		 
		 //执行新的开行计划滚动生成
		 doNewPLanWork();
	}
	
	//遍历设置了自动滚动生成的开行计划 依次往后生成一天的开行计划
	private void doNewPLanWork(){
		int currentHour = TimeUtils.getCurrentDate().get(Calendar.HOUR);
		if(currentHour > 8){
			//每天0到8点之间执行该任务
			return;
		}
		operationlanServiceImpl.autoGeneratePlan();
	}

	private void doWork() {
		List<SchemeInfo> schemeInfos = null;
	
		
		// 先查询出所有开行计划（图定的）
		schemeInfos = getBaseChartIds();
		if(!schemeInfos.isEmpty()) {
			for(SchemeInfo schemeInfo : schemeInfos) {
				//params = getParams(schemeInfo.getSchemeId());
				//generatePlanTrainBySchemaId(params);
				//定义一个map,包含在里面的list是同一个开行日期的CrossName			
				Map<String, List<RunPlanRollingDTO>> runPlanRollingDTOMap = new HashMap<String, List<RunPlanRollingDTO>>();
				
				try {
					List<RunPlanRollingDTO> runPlanRollings = runPlanService.getPlanTrainInfoForBaseChartId(schemeInfo.getSchemeId());
					if(null != runPlanRollings && !runPlanRollings.isEmpty()) {
						for (RunPlanRollingDTO runPlanRollingDTO : runPlanRollings) {
							logger.info("RollingGenerationRP runPlanRollingDTO!" + runPlanRollingDTO.getCrossName());
							String lastRunDate = runPlanRollingDTO.getRunDate();
							
							if(null == runPlanRollingDTOMap.get(lastRunDate)) {
//								List<String> unitCrossIds = new ArrayList<String>();
//								unitCrossIds.add(runPlanRollingDTO.getUnitCrossId());							
//								runPlanRollingDTOMap.put(lastRunDate, unitCrossIds);
								
								List<RunPlanRollingDTO> runPlanRollingDTOs = new ArrayList<RunPlanRollingDTO>();
								runPlanRollingDTOs.add(runPlanRollingDTO);							
								runPlanRollingDTOMap.put(lastRunDate, runPlanRollingDTOs);
							}
							else {
//								List<String> unitCrossIds = runPlanRollingDTOMap.get(lastRunDate);
//								unitCrossIds.add(runPlanRollingDTO.getUnitCrossId());							
//								runPlanRollingDTOMap.put(lastRunDate, unitCrossIds);
								
								List<RunPlanRollingDTO> runPlanRollingDTOs = runPlanRollingDTOMap.get(lastRunDate);
								runPlanRollingDTOs.add(runPlanRollingDTO);							
								runPlanRollingDTOMap.put(lastRunDate, runPlanRollingDTOs);
							}
						}
						//调用方法生成开行计划
						assemblyParamsAndCall(schemeInfo.getSchemeId(), runPlanRollingDTOMap);
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				
			}
		}
	}
	
	
	private void assemblyParamsAndCall (String baseChartId, Map<String, List<RunPlanRollingDTO>> runPlanRollingDTOMap) throws ParseException {
		if (runPlanRollingDTOMap != null) {
			Set<String> keySet = runPlanRollingDTOMap.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				//List<String> unitCrossIds = runPlanRollingDTOMap.get(key);
				List<RunPlanRollingDTO> runPlanRollingDTOs = runPlanRollingDTOMap.get(key);
				Map<String, Object> params = new HashMap<String, Object>();
				//将crossNames分成两类，普通的和特殊规律的
				//List<String> crossNames = new ArrayList<String>();
				//List<String> specialCrossNames = new ArrayList<String>();
				//将crossName再按担当局分组
				Map<String, List<String>> tokenCrossNames = new HashMap<String, List<String>>();
				Map<String, List<String>> tokenSpecialCrossNames = new HashMap<String, List<String>>();
				
				for(RunPlanRollingDTO runPlanRollingDTO : runPlanRollingDTOs) {
					boolean isAppointWeek = false;
		        	boolean isAppointDay = false;		        	
		        	if(null != runPlanRollingDTO.getAppointWeek() && !"null".equals(runPlanRollingDTO.getAppointWeek()) &&
		        			!"".equals(runPlanRollingDTO.getAppointWeek())) 
		        		isAppointWeek = true;
		        	if(null != runPlanRollingDTO.getAppointDay() && !"null".equals(runPlanRollingDTO.getAppointDay()) &&
		        			!"".equals(runPlanRollingDTO.getAppointDay()))
		        		isAppointDay = true;
		        	
		        	if(isAppointWeek || isAppointDay) {
		        		if(tokenSpecialCrossNames.containsKey(runPlanRollingDTO.getTokenVehBureau())) {
		        			tokenSpecialCrossNames.get(runPlanRollingDTO.getTokenVehBureau()).add(runPlanRollingDTO.getCrossName());
		        		}
		        		else {
		        			List<String> specialCrossNames = new ArrayList<String>();
		        			specialCrossNames.add(runPlanRollingDTO.getCrossName());
		        			tokenSpecialCrossNames.put(runPlanRollingDTO.getTokenVehBureau(), specialCrossNames);
		        		}
		        		//specialCrossNames.add(runPlanRollingDTO.getCrossName());
		        	}
		        	else {
		        		if(tokenCrossNames.containsKey(runPlanRollingDTO.getTokenVehBureau())) {
		        			tokenCrossNames.get(runPlanRollingDTO.getTokenVehBureau()).add(runPlanRollingDTO.getCrossName());
		        		}
		        		else {
		        			List<String> crossNames = new ArrayList<String>();
		        			crossNames.add(runPlanRollingDTO.getCrossName());
		        			tokenCrossNames.put(runPlanRollingDTO.getTokenVehBureau(), crossNames);
		        		}
		        		//crossNames.add(runPlanRollingDTO.getCrossName());
		        	}
		        	
				}
				
				Set<String> tokenCrossNamesKeySet = tokenCrossNames.keySet();
				Iterator<String> itTokenCrossNames = tokenCrossNamesKeySet.iterator();
				while (itTokenCrossNames.hasNext()) {
					if(isLegalDate(getStartDate(key), 1)) {
						String tokenCrossNamesKey = itTokenCrossNames.next();
						params.clear();
						
						params.put("baseChartId", baseChartId);
						params.put("startDate", getStartDate(key)); //不用增加一天，直接用最后一天做startDate,生成一天的开行计划
						//params.put("startDate", key);
						params.put("days", 1);
						//params.put("unitcrossId", unitCrossIds);
						params.put("crossNames", tokenCrossNames.get(tokenCrossNamesKey));
						params.put("msgReceiveUrl", null);
						params.put("tokenVehBureau", tokenCrossNamesKey);
						//将crossName再按担当局分组
						
						//调用生成开行计划的方法
						generatePlanTrainBySchemaId(params);
					}
						
				}
				
				Set<String> tokenSpecialCrossNamesKeySet = tokenSpecialCrossNames.keySet();
				Iterator<String> itTokenSpecialCrossNames = tokenSpecialCrossNamesKeySet.iterator();
				while (itTokenSpecialCrossNames.hasNext()) {
					int rundays = getRundays(key);
					if(isLegalDate(getStartDate(key), rundays) && rundays > 0) {
						String tokenSpecialCrossNamesKey = itTokenSpecialCrossNames.next();
						
						params.clear();
						
						params.put("baseChartId", baseChartId);
						params.put("startDate", getStartDate(key)); //不用增加一天，直接用最后一天做startDate,生成一天的开行计划
						//params.put("startDate", key);
						
						//维护天数，当天日期，以及最后一天开行计划天数进行计算，得到需要的生成天数			
						
						
						params.put("days", rundays);
						//params.put("unitcrossId", unitCrossIds);
						params.put("crossNames", tokenSpecialCrossNames.get(tokenSpecialCrossNamesKey));
						params.put("msgReceiveUrl", null);
						params.put("tokenVehBureau", tokenSpecialCrossNamesKey);
						
						//将crossName再按担当局分组
						
						//调用生成开行计划的方法
						generatePlanTrainBySchemaId(params);
					
					
					}
				}
				
//				if(!crossNames.isEmpty()) {
//					params.put("baseChartId", baseChartId);
//					params.put("startDate", getStartDate(key)); //不用增加一天，直接用最后一天做startDate,生成一天的开行计划
//					//params.put("startDate", key);
//					params.put("days", 1);
//					//params.put("unitcrossId", unitCrossIds);
//					params.put("crossNames", crossNames);
//					params.put("msgReceiveUrl", null);
//					
//					
//					//将crossName再按担当局分组
//					
//					//调用生成开行计划的方法
//					generatePlanTrainBySchemaId(params);
//				}
				
//				if(!specialCrossNames.isEmpty()) {
//					params.clear();
//					int rundays = getRundays(key);
//					if (rundays > 0) {
//						params.put("baseChartId", baseChartId);
//						params.put("startDate", getStartDate(key)); //不用增加一天，直接用最后一天做startDate,生成一天的开行计划
//						//params.put("startDate", key);
//						
//						//维护天数，当天日期，以及最后一天开行计划天数进行计算，得到需要的生成天数			
//						
//						
//						params.put("days", rundays);
//						//params.put("unitcrossId", unitCrossIds);
//						params.put("crossNames", specialCrossNames);
//						params.put("msgReceiveUrl", null);
//						
//						//将crossName再按担当局分组
//						
//						//调用生成开行计划的方法
//						generatePlanTrainBySchemaId(params);
//					}
//				}
				
			}
		}
		
		
	}
	
	/**
	 * 生成开行计划
	 * 
	 * @param params
	 * @return
	 */
	private ResponseEntity<List<String>> generatePlanTrainBySchemaId(
			Map<String, Object> params) {
		logger.debug("RollingGenerationRP generatePlanTrainBySchemaId~~~reqMap = " + params);
		String baseChartId = MapUtils.getString(params, "baseChartId");
		String startDate = MapUtils.getString(params, "startDate");
		int days = MapUtils.getIntValue(params, "days");
		//List<String> unitcrossId = (List<String>) params.get("unitcrossId");
		List<String> crossNames = (List<String>) params.get("crossNames");
		String msgReceiveUrl = MapUtils.getString(params, "msgReceiveUrl");
		String tokenVehBureau = MapUtils.getString(params, "tokenVehBureau");
		logger.debug("msgReceiveUrl = " + msgReceiveUrl);
		//logger.debug("unitcrossId = " + unitcrossId);
		logger.debug("crossNames = " + crossNames);
		List<String> unitCrossIds = runPlanService.generateRunPlan(baseChartId,
				startDate, days, crossNames, msgReceiveUrl, 1, tokenVehBureau);
		return new ResponseEntity<List<String>>(unitCrossIds, HttpStatus.OK);
	}


	/**
	 * 查询方案列表
	 * @return
	 */
	private List<SchemeInfo> getBaseChartIds() {       
	
		Result result = new Result();
		try{
			logger.info("RollingGenerationRP querySchemes");
			//调用后台接口
			List<SchemeInfo> schemeInfos = schemeService.getSchemes();
			return schemeInfos;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
			return null;
		}			
	}
	
	/**
	 * 获取生成运行线必要参数
	 * 
	 * @param params
	 * @return
	
	private Map<String, Object> getParams(String baseChartId) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		
		//reqMap.put("", value)
        //先查询出PLAN_CROSS中的交路单元Id,根据基本图Id(baseChartId);
		List<PlanCrossDto> runPlans = runPlanService.getPlanCross(reqMap);
		
        //根据交路ID在PLAN_TRAIN中查询出一个交路单元的第一组车底的最后发车时间
        
        //填充params
        
        
    	//String baseChartId = MapUtils.getString(params, "baseChartId");
        String startDate = MapUtils.getString(params, "startDate");
        int days = MapUtils.getIntValue(params, "days");
        List<String> unitcrossId = (List<String>) params.get("unitcrossId");
        String msgReceiveUrl = MapUtils.getString(params, "msgReceiveUrl");
        logger.debug("msgReceiveUrl = " + msgReceiveUrl);
        logger.debug("unitcrossId = " + unitcrossId);
        List<String> unitCrossIds = runPlanService.generateRunPlan(baseChartId, startDate, days, unitcrossId, msgReceiveUrl);
        return params;
	}
	 * @throws ParseException 
	 */	
	
	
	private String getStartDate(String runDate) throws ParseException {
		// 将开行计划的天数向后增加一天
		return DateUtil.format(DateUtil.addDateOneDay(DateUtil.parseDate(runDate, "yyyyMMdd")), "yyyyMMdd");
	}
	
	private int getRundays(String LastRunDate) {
		
		
		//LastRunDate减去当天的日期，就是已有的开行计划天数，需要维护的天数减去该天数就是要生成开行计划的天数
		LocalDate LsatDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(LastRunDate);
		LocalDate nowDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(DateUtil.getStringFromDate(new Date(), "yyyyMMdd"));
		
		int initInterval = Days.daysBetween(LsatDate, nowDate).getDays();
	 
		return maintainRunplanDays - initInterval;		
	}
	
	private boolean isLegalDate(String startDateStr, int runDays) {
		LocalDate lastDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(DateUtil.format(new Date(), "yyyyMMdd")).plusMonths(3);
		LocalDate runDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDateStr).plusDays(runDays);
		
		if(lastDate.compareTo(runDate) < 0) {
			return false;
		}
		else {
			return true;
		}
		
		
	}
	
}
