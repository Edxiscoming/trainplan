package org.railway.com.trainplan.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.PlanTrainCheckService;
import org.railway.com.trainplan.service.PlanTrainStnService;
import org.railway.com.trainplan.service.RemoteService;
import org.railway.com.trainplan.service.SchemeService;
import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.TrainTimeService;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PlanTrainStnController {
	private static Log logger = LogFactory.getLog(PlanTrainStnController.class.getName());

	@Autowired
	private PlanTrainStnService planTrainStnService;
	
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private PlanTrainCheckService planTrainCheckService;
	@Autowired
	private CommonService commonService ;
	@Autowired
	private RemoteService remoteService;
	
	@Autowired
	private CrossService crossService;
	
	@Autowired
	private TrainInfoService trainInfoService;
	
	@Autowired
	private TrainTimeService trainTimeService;
	

	
	@Autowired
	private SchemeService schemeService;
	//fortest
	@Autowired
	private BaseDao baseDao;
	@ResponseBody
	@RequestMapping(value = "/mytest", method = RequestMethod.POST)
	public Result getTest(@RequestBody Map<String,Object> reqMap) throws Exception{
		Result result = new Result();
		//String ljqc = reqMap.get("ljqc").toString();
		//System.err.println("ljqc == " + ljqc);
		//Ljzd ljzd = commonService.getLjInfo(ljqc);
		//System.err.println("ljdm==" + ljzd.getLjdm());
		/*List<CrossInfo> alllist = new ArrayList<CrossInfo>();
		for(int i = 0;i<10;i++){
			CrossInfo cross = new CrossInfo();
			cross.setCreatePeople("people"+i);
			alllist.add(cross);
		}
		if(alllist != null && alllist.size() > 0){
			//保存交路信息
			baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_INFO,alllist);
			
		}
		ArrayList<CrossTrainInfo> crossTrains = new ArrayList<CrossTrainInfo>();
    	//保存列车
		for(int i = 0;i<10;i++){
			CrossTrainInfo cross = new CrossTrainInfo();
			cross.setCrossId(i);
			crossTrains.add(cross);
		}
		
		if(crossTrains != null && crossTrains.size() > 0 ){
			
			//baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO, crossTrains);
		}*/
		
		//List list = crossService.getCrossInfo(reqMap);
		//result.setData(list);
		
       // CrossInfo crossinfo = crossService.getCrossInfoForCrossid("100");
        //result.setData(crossinfo);
		
		//fortest导入
		/*InputStream is;
		try {
			is = new FileInputStream(
					"C:\\test.xls");
			crossService.actionExcel(is, UUID.randomUUID().toString(), "20140513", "测试方案");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//CrossService a = new CrossService();
		//测试插入unit_cross表
		/*try{
			String baseCrossId = reqMap.get("baseCrossId").toString();
			System.err.println("baseCrossId==" + baseCrossId);
			crossService.completeUnitCrossInfo(baseCrossId);
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
		/*String baseTrainId = reqMap.get("baseTrainId").toString();
		//测试根据车次查询车次信息
		TrainlineTemplateDto dto = remoteService.getTrainLinesInfoWithId(baseTrainId);*/
		//result.setData(dto);
		
		//test
		//List<PlanTrainDto> list = planTrainStnService.getTrainShortInfo("20140502", "", 50);
		//result.setData(list);
		
		
		//test
		/*int count = trainInfoService.getTrainInfoCount(reqMap);
		System.err.println("count==" + count);
		result.setData(trainInfoService.getTrains(reqMap));*/
		
		
		//test
		/*String trainId = StringUtil.objToStr(reqMap.get("trainId"));
		List<TrainTimeInfo> list = trainTimeService.getTrainTimes(trainId);
		result.setData(list);*/
		
		
		/*String chartId = StringUtil.objToStr(reqMap.get("chartId"));
		String operation = StringUtil.objToStr(reqMap.get("operation"));
		List<TrainlineTemplateDto> list = planTrainStnService.getTrainsWithSchemeId(chartId, operation, "2014-05-22");
		System.err.println("trains--size==" + list.size());*/
//		long time1 = System.currentTimeMillis();
//		DaytaskDto reqDto = new DaytaskDto();
//		reqDto.setChartId("b4264afd-7755-48ba-9cf7-b31eeac6e085");
//		reqDto.setOperation("客运");
//		reqDto.setRunDate("2014-05-23");
//		reqDto.setRownumend(10);
//		reqDto.setRownumstart(1);
		
		
//		treadService.actionDayWork(reqDto, 1);
		//List<TrainlineTemplateDto> list = trainInfoService.getTrainsAndTimesForList(reqDto);
//		long time2 = System.currentTimeMillis();
//		System.err.println("time==" + (time2-time1)/1000);
		//System.err.println("size==" + list.size());
		
		//result.setData(list);
		
		
		//fortest
		/*String unitCrossId = StringUtil.objToStr(reqMap.get("unitCrossId"));
		System.err.println("unitCrossId==" +unitCrossId );
		List<UnitCrossTrainInfo> list = crossService.getUnitCrossTrainInfoForUnitCrossid(unitCrossId);
		result.setData(list);*/
		
		
		/*String unitCrossId = StringUtil.objToStr(reqMap.get("unitCrossId"));
		List<UnitCrossTrainInfo>  list = crossService.getUnitCrossTrainInfoForUnitCrossid(unitCrossId);
		result.setData(list);*/
		
		/*String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
		List<TrainLineInfo>  list = crossService.getTrainPlanLineInfoForPlanCrossId(planCrossId,"京");
		result.setData(list);*/
		
		
		//List<String> list = crossService.getStationListForPlanCrossId(planCrossId);
		//result.setData(list);
		
		//test update base_cross
		CrossInfo crossInfo = new CrossInfo();
		crossInfo.setBaseCrossId("2b3a19b1-508c-4ccf-a625-0c2c809dc1eb");
		crossInfo.setCrossStartDate("20140910");
		crossInfo.setCrossEndDate("20141010");
		int count = crossService.updateBaseCross(crossInfo);
		//System.err.println("count==" + count);
		return result;
	}
	
	/**
	 * 获取全路局信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/plan/getFullStationInfo", method = RequestMethod.GET)
	public Result  getFullStationInfo(){
		Result result  = new Result();
		try{
			List<Ljzd> list = commonService.getFullStationInfo();
			result.setData(list);
		}catch(Exception e){
	    	logger.error("getFullStationInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }
		return result;
	}
	
	/**
	 * 批量生成开行计划
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/plan/batchHandleTrainLines", method = RequestMethod.POST)
	public Result batchHandleTrainLines(@RequestBody Map<String,Object> reqMap){
		logger.debug("batchHandleTrainLines~~~~~~ reqMap="+reqMap);
		Result result  = new Result();
		try{
		String runDate = StringUtil.objToStr(reqMap.get("runDate"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		//路局全称
		String startBureauFull  = StringUtil.objToStr(reqMap.get("startBureauFull"));
		String dayCount =  StringUtil.objToStr(reqMap.get("dayCount"));
		int count = Integer.valueOf(dayCount);
		for(int i = 1;i<=count;i++){
			String tempDate = DateUtil.getDateByDay(runDate, -(i-1));
			tempDate = DateUtil.format(DateUtil.parse(tempDate), "yyyyMMdd");
			//System.err.println();
			List<ParamDto> listDto = planTrainStnService.getTotalTrains(tempDate,startBureauFull, trainNbr);
			if(listDto != null && listDto.size() > 0){
				String jsonStr = commonService.combinationMessage(listDto);
				logger.debug("jsonStr====" + jsonStr);
				//System.err.println("jsonStr====" + jsonStr);
				//向rabbit发送消息
				amqpTemplate.convertAndSend("crec.event.trainplan",jsonStr);	
			}
				
			
			
		}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("batchHandleTrainLines error==" + e.getMessage());
			
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
		return result;	
	}
	
	/**
	 * 审核并生成开行计划
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/plan/handleTrainLines", method = RequestMethod.POST)
	public Result handleTrainLines(@RequestBody Map<String,Object> reqMap){
		Result result  = new Result();
		try{
			
			String runDate = DateUtil.format(DateUtil.parse(StringUtil.objToStr(reqMap.get("runDate"))), "yyyyMMdd");
			logger.debug("runDate==" + runDate);
			//路局全称
			String startBureauFull  = StringUtil.objToStr(reqMap.get("startBureauFull"));
			
			List<ParamDto> listDto = planTrainStnService.getTotalTrains(runDate,startBureauFull,null);
			if(listDto != null && listDto.size() > 0){
				String jsonStr = commonService.combinationMessage(listDto);
				//向rabbit发送消息
				amqpTemplate.convertAndSend("crec.event.trainplan",jsonStr);
					
			}
			
	    }catch(Exception e){
	    	logger.error("handleTrainLines error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }
		
		return result;
	}
	/**
	 * 获取方案列表
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/plan/getSchemeList", method = RequestMethod.POST)
	public Result getSchemeList(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
	    try{
	    	List<SchemeInfo> schemeInfos = schemeService.getSchemes();
	    	result.setData(schemeInfos);
	    }catch(Exception e){
	    	logger.error("getSchemeList error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }
		
		return result;
	}
	
	/**
	 * 通过开行日期等查询车次，始发站，终点站信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/plan/getTrainShortInfo", method = RequestMethod.POST)
	public Result getTrainShortInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		String runDate = StringUtil.objToStr(reqMap.get("runDate"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		String count = StringUtil.objToStr(reqMap.get("count"));
		logger.info("getTrainShortInfo--[runDate=" + runDate +",count="+count+",trainNbr"+trainNbr);
	    try{
	    	List<PlanTrainDto> list =  planTrainStnService.getTrainShortInfo(runDate.replaceAll("-",""), trainNbr, Integer.valueOf(count));
	    	if(list != null && list.size() > 0){
	    	   result.setData(list);	
	    	}
	    }catch(Exception e){
	    	logger.error("getTrainShortInfo error==" + e.getMessage());
	    	e.printStackTrace();
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }
		
		return result;
	}
	
	/**
	 * 通过开行日期等查询车次，始发站，终点站信息,包括分页
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/plan/getTrainFullInfo", method = RequestMethod.POST)
	public Result getTrainFullInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
	    try{
	    	
	    }catch(Exception e){
	    	logger.error("getTrainFullInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }
		
		return result;
	}
	
	/**
	 * 导入开行计划
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/plan/importTrainPlan", method = RequestMethod.POST)
	public Result importTainPlan(@RequestBody Map<String,Object> reqMap){
		
		Result result = new Result();
		 final String startDate = StringUtil.objToStr(reqMap.get("startDate"));
		
		 final String dayCount = StringUtil.objToStr(reqMap.get("dayCount"));
		 final String schemeId = StringUtil.objToStr(reqMap.get("schemeId"));
		logger.info("[importTainPlan],param[startDate=" + startDate + ",dayCount="+ dayCount +",schemeId=" + schemeId );
		//检验必输项
		if(StringUtils.isEmpty(startDate) ||
		   StringUtils.isEmpty(dayCount)  ||
		   StringUtils.isEmpty(schemeId)){
		   result.setCode(StaticCodeType.SYSTEM_PARAM_LOST.getCode());
		   result.setMessage(StaticCodeType.SYSTEM_PARAM_LOST.getDescription());
		   return result;
		}
		try{
			 DateTime dateTime = new DateTime(startDate);  
			//调入后台导入数据
			planTrainStnService.importTainPlan(schemeId, dateTime.toString("yyyy-MM-dd"), dayCount);	
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("importTainPlan error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		//计算enddate
		String endDate = DateUtil.getDateByDay(startDate, -Integer.valueOf(dayCount) + 1);
		Map<String,String> dataMap = new HashMap<String,String>();
		dataMap.put("enddate", endDate);
		result.setData(dataMap);
		return result;
	}
	
	
	
	
	
}
