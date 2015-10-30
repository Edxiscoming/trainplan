package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.service.PlanTrainCheckService;
import org.railway.com.trainplan.service.RemoteService;
import org.railway.com.trainplan.service.dto.PagingInfo;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.service.dto.PlanBureauStatisticsDto;
import org.railway.com.trainplan.service.dto.PlanBureauTsDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/plancheck")
public class PlanCheckController {
	private static Log logger = LogFactory.getLog(PlanCheckController.class);

	@Autowired
	private PlanTrainCheckService planTrainCheckService;
	
	@Autowired
	private RemoteService remoteService;
	/**
	 * 根据rundate，trainNbr
	 * 删除表plan_train和表plan_train_stn中数据
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePlanTrainWithRunDate", method = RequestMethod.POST)
	public Result deletePlanTrainWithRunDate(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		logger.debug("deletePlanTrainWithRunDate~~reqMap="+reqMap);
		 String deleteKey = StringUtil.objToStr(reqMap.get("deleteKey"));
		 if(deleteKey != null && !"".equals(deleteKey)){
			 String[] keys = deleteKey.split("#"); 
			 if(keys != null && keys.length > 0){
				 for(String key : keys){
					 String[] params = key.split("@@");
					 if(params != null && params.length >0){
						 String trainNbr =  params[0];
						 String rundate =  params[1];
						try{
							//删除子表数据
							planTrainCheckService.deletePlanTrainStnWithRundate(rundate, trainNbr);	
							//删除父表数据
							planTrainCheckService.deletePlanTrainWithRundate(rundate, trainNbr);
						}catch(Exception e){
							logger.error(e.getMessage(), e);
							result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
							result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
						}
						
					 }
				 }
			 }
		 }
		return result;
	}
	/**
	 * 根据时间段统计某局的车次信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findPlanTrainWithPeriodRunDate", method = RequestMethod.POST)
	public Result findPlanTrainWithPeriodRunDate(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		logger.debug("getFullStationTrains~~reqMap="+reqMap);
		try{
			int currentPage = StringUtil.strToInteger(StringUtil.objToStr(reqMap.get("currentPage")));
			int pageSize =  StringUtil.strToInteger(StringUtil.objToStr(reqMap.get("pageSize")));
			
			String startBureauFullName = StringUtil.objToStr(reqMap.get("startBureauFull"));
			String startDate = StringUtil.objToStr(reqMap.get("runDate"));
			//车次，非必输
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			int dayCount = StringUtil.strToInteger(StringUtil.objToStr(reqMap.get("dayCount")));
			PagingInfo pageInfo = new PagingInfo(currentPage,pageSize);
			//调用后台的接口
			PagingResult pageResult = planTrainCheckService.findPlanTrainWithPeriodRunDate(pageInfo, startBureauFullName, startDate, dayCount,trainNbr);
			List<String> daysList = DateUtil.getDateListWithDayCount(startDate, dayCount);
			List<String> copyList = new ArrayList<String>();
			for(int i = 0;i<daysList.size();i++){
				String day = DateUtil.format(DateUtil.parse(daysList.get(i).toString()),"dd");
				copyList.add(day);
			}
			Map map = new HashMap();
			map.put("days",copyList );
			map.put("pageInfo",pageResult );
			result.setData(map);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	}
	/**
	 * 统计全路局运行车次信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFullStationTrains", method = RequestMethod.POST)
	public Result getFullStationTrains(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			logger.debug("getFullStationTrains~~reqMap="+reqMap);
			String runDate="";
			String tempRunDate = StringUtil.objToStr(reqMap.get("runDate"));
			if(reqMap.get("runDate") ==null || "".equals(reqMap.get("runDate").toString())) {
				runDate = DateUtil.format(new Date(), "yyyyMMdd");
			} else {
				runDate = DateUtil.format(DateUtil.parse(tempRunDate), "yyyyMMdd");
			}
			//调用后台接口
			List<Map<String,Object>> listMap = planTrainCheckService.getFullStationTrains(runDate,null);
			//调用后台报文接口获取运行线统计数
			String paramRunDate = DateUtil.format(DateUtil.parse(tempRunDate), "yyyy-MM-dd");
			String sourceTime = paramRunDate+" 00:00:00";
			String targetTime =  paramRunDate+" 23:59:59";
			List<PlanBureauStatisticsDto> listLines = remoteService.getTrainLinesWithDay("10",sourceTime,targetTime);
			//统计接入信息
			if(listMap != null && listMap.size() > 0){
				for(Map<String,Object> map : listMap){
					//路局全称
					String ljqc = StringUtil.objToStr(map.get("LJQC"));
					Map<String,Object> jieru_map = planTrainCheckService.getOneStationTrains(runDate,ljqc);
					//System.err.println(ljqc+"接入统计Map==" + jieru_map);
					//接入终到
					Object jrzd ="";
					//接入交出
					Object jrjc ="";
					//接入小计
					Object jrxj ="";
					if(jieru_map != null){
						jrzd = jieru_map.get("TDJRZD");
						jrjc = jieru_map.get("TDJRJC");
						jrxj = jieru_map.get("TDJRXJ");
					}
					map.put("TDJRZD",StringUtil.objToStr(jrzd));
					//图定接入交出
					map.put("TDJRJC",StringUtil.objToStr(jrjc));
					//图定接入小计
					map.put("TDJRXJ",StringUtil.objToStr(jrxj));
					//图定始发交出线
					map.put("TDSFJCX","" );
					//图定始发终到线
					map.put("TDSFZDX","" );
					//图定始发小计线
					map.put("TDSFXJX", "");
					for(PlanBureauStatisticsDto dto :listLines){
						//路局全称
						String bureauName = dto.getBureauName();
						if(ljqc.equals(bureauName)){
							List<PlanBureauTsDto> subDtoList = dto.getListBureauDto();
							//只有一条数据
							PlanBureauTsDto subDto = subDtoList.get(0);
							int sourceTrainLines = subDto.getSourceSurrenderTrainlineCounts();
							int targetTrainLines = subDto.getSourceTargetTrainlineCounts();
							int total = sourceTrainLines + targetTrainLines;
							//图定始发交出线
							map.put("TDSFJCX", (sourceTrainLines == 0 ? "" : sourceTrainLines)+"");
							//图定始发终到线
							map.put("TDSFZDX", (targetTrainLines == 0 ? "" :targetTrainLines)+"");
							//图定始发小计线
							map.put("TDSFXJX", (total == 0 ? "" :total)+"");
							break;
						}
					}
				}
			}
			//System.err.println("全路局统计ListMap==" +listMap );
			result.setData(listMap);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	}
	/**
	 * 统计单个路局运行车次信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOneStationTrains", method = RequestMethod.POST)
	public Result getOneStationTrains(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			logger.debug("getOneStationTrains~~reqMap="+reqMap);
			String runDate="";
			if(reqMap.get("runDate") ==null || "".equals(reqMap.get("runDate").toString())) {
				runDate = DateUtil.format(new Date(), "yyyyMMdd");
			} else {
				runDate = DateUtil.format(DateUtil.parse(StringUtil.objToStr(reqMap.get("runDate"))), "yyyyMMdd");
			}
			//路局全称
			String startBureauFull = StringUtil.objToStr(reqMap.get("startBureauFull"));
			//调用后台接口
			List<Map<String,Object>> listMap = planTrainCheckService.getFullStationTrains(runDate,startBureauFull);
			//统计接入信息
			Map<String,Object> map = planTrainCheckService.getOneStationTrains(runDate,startBureauFull);
			//将接入统计信息放入listMap中
			//System.err.println("接入统计一个路局接入map==" + map);
			if(listMap != null){
				//只有一条数据
				Map<String,Object> tempMap = listMap.get(0);
				//System.err.println("接入统计一个路局始发map==" + tempMap);
				Object jrzd = "";
				Object jrjc = "";
				Object jrxj = "";
				if(map != null){
					jrzd = map.get("TDJRZD");
					jrjc = map.get("TDJRJC");
					jrxj = map.get("TDJRXJ");
				}
				//图定接入终到
				tempMap.put("TDJRZD",StringUtil.objToStr(jrzd));
				//图定接入交出
				tempMap.put("TDJRJC",StringUtil.objToStr(jrjc));
				//图定接入小计
				tempMap.put("TDJRXJ",StringUtil.objToStr(jrxj));
				result.setData(tempMap);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	}
	
	
	/**
	 * 根据始发局局码、开行日期查询列车开行计划信息
	 * @param startBureau 始发局局码
	 * @param runDate 开行日期 yyyy-MM-dd
	 * @author denglj
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanTrainByStartBureau", method = RequestMethod.POST)
	public Result findPlanTrainByStartBureauAndRundate(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try {
			logger.debug("~~~~~~~~~~~~~~~~~ reqMap="+reqMap);
			String runDate;
			if(reqMap.get("runDate") ==null || "".equals(reqMap.get("runDate").toString())) {
				runDate = DateUtil.format(new Date(), "yyyyMMdd");
			} else {
				runDate = DateUtil.format(DateUtil.parse(StringUtil.objToStr(reqMap.get("runDate"))), "yyyyMMdd");
			}
			
			String trainNbr = null;
			if(reqMap.get("trainNbr") !=null && !"".equals(reqMap.get("trainNbr").toString())) {
				trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			}
			
			String startBureau = StringUtil.objToStr(reqMap.get("startBureau"));
			StringUtil.objToStr(reqMap.get("trainNbr"));
			int currentPage = Integer.parseInt(StringUtil.objToStr(reqMap.get("currentPage")));
			int pageSize = Integer.parseInt(StringUtil.objToStr(reqMap.get("pageSize")));
			
			
			PagingInfo paging = new PagingInfo(currentPage, pageSize);
			result.setData(planTrainCheckService.findPlanTrainByStartBureauAndRundate(paging, startBureau, runDate, trainNbr));
		} catch (Exception e) {
	    	logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }

		return result;
		
	}
	
	
	
	
	/**
	 * 根据车次及开行日期查询 时刻信息
	 * @param trainNbr 车次号
	 * @param runDate 开行日期 yyyy-MM-dd
	 * @author denglj
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/showTrainTimeDetail", method = RequestMethod.POST)
	public Result showTrainTimeDetail(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try {
			logger.debug("~~~~~~~~~~~~~~~~~ reqMap="+reqMap);
			String runDate = DateUtil.format(DateUtil.parse(StringUtil.objToStr(reqMap.get("runDate"))), "yyyyMMdd");
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			result.setData(planTrainCheckService.getTrainTimeDetail(runDate, trainNbr));
		} catch (Exception e) {
	    	logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }

		return result;
		
	}
}
