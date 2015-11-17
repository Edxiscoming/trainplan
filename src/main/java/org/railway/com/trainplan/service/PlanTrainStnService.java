package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.DwrMessageData;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class PlanTrainStnService {
	private static final Logger logger = Logger.getLogger(PlanTrainStnService.class);
	
    @Autowired
    private CommonService commonService;
    
    @Autowired
    private RemoteService remoteService;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private BaseDao baseDao;
    
    @Autowired
    private TrainInfoService trainInfoService;
    
    @Autowired
    private TrainTimeService trainTimeService;

    @Autowired
    private CrossService crossService;
    
    @Autowired
    private RunPlanService runPlanService;
	
    @Autowired
	private AmqpTemplate amqpDwrTemplate;
	

    
    /**
	 * 更新数据表plan_train中字段daylyplan_flag,的值
	 * @param base_train_id
	 * @return
	 * @throws Exception
	 */
   
    public Result updatePlanTrainDaylyPlanFlag(Map<String,Object> reqMap) throws Exception {
    	Result result = new Result();
    	baseDao.updateBySql(Constants.TRAINPLANDAO_UPDATE_PLANFLAG, reqMap);
    	return result;
    }
    
    /**
	 * 更新数据表plan_train中字段daylyplan_flag,的值,然后向mq发消息
	 * @param base_train_id
	 * @return
	 * @throws Exception
	 */
    public Result updatePlanTrainDaylyPlanFlagSendMessage(Map<String,Object> reqMap, DwrMessageData dwrMessageData) throws Exception {
    	Result result = new Result();
    	baseDao.updateBySql(Constants.TRAINPLANDAO_UPDATE_PLANFLAG, reqMap);
    	String params = JSONObject.fromObject(dwrMessageData).toString();
    	amqpDwrTemplate.convertAndSend(params);   	
    	return result;
    }
    /**
     * 更新表plan_train中字段check_state的值
     */
 
    public Result updatePlanTrainCheckStats(String base_train_id) throws Exception{
    	   Result result = new Result();
    	   //更新数据库
    	   baseDao.updateBySql(Constants.TRAINPLANDAO_UPDATE_CHECKSTATE, base_train_id);
    	   return result;
    }
  
    /**
     * runDate :格式：yyyyMMdd
     */
  
    public  List<ParamDto>  getTotalTrains(String runDate,String startBureauFull,String trainNbr){
    	    List<ParamDto>  list = new ArrayList<ParamDto>();
    	    Map<String,String> paramMap = new HashMap<String,String>();
    	    paramMap.put("runDate", runDate);
    	    paramMap.put("startBureauFull",startBureauFull );
    	    if(trainNbr != null && !"".equals(trainNbr.trim())){
    	    	paramMap.put("trainNbr", trainNbr);
    	    }
    	    
    	    List<Map<String,Object>> mapList = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TOTALTRAINS, paramMap);
    	    if(mapList != null && mapList.size() > 0){
    	    	 //System.err.println("mapList.size===" + mapList.size());
    	    	for(Map<String,Object> map :mapList ){
    	    		ParamDto dto = new ParamDto();
    	    		dto.setSourceEntityId(StringUtil.objToStr(map.get("BASE_TRAIN_ID")));
    	    		dto.setPlanTrainId(StringUtil.objToStr(map.get("PLAN_TRAIN_ID")));
    	    		String time = StringUtil.objToStr(map.get("RUN_DATE"));
    	    		dto.setTime(DateUtil.formateDate(time));
    	    		list.add(dto);
    	    	}
    	    }
    	    return list;
    }
    
    
   

    
	/**
	 * 查询车次的始发站和终点站等信息
	 * 
	 * @param runDate
	 *            开行日期
	 * @param trainNbr
	 *            车次
	 * @param count
	 *            返回的条数
	 */
	
	public List<PlanTrainDto> getTrainShortInfo(String runDate,
			String trainNbr, int count) {
		List<PlanTrainDto> returnList = new ArrayList<PlanTrainDto>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("runDate",runDate);
		paramMap.put("rownum",count);
		if (trainNbr != null && !"".equals(trainNbr)) {
			paramMap.put("trainNbr",trainNbr);
		}
		// 调用dao获取数据  TRAINPLANDAO_GET_TRAIN_SHORTINFO
		List<Map<String, Object>> listMap = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TRAIN_SHORTINFO, paramMap);
		if (listMap != null && listMap.size() > 0) {
			for (Map<String, Object> map : listMap) {
				PlanTrainDto dto = new PlanTrainDto();
				dto.setPlanTrainId(StringUtil.objToStr(map.get("PLAN_TRAIN_ID")));
				dto.setEndStn(StringUtil.objToStr(map.get("END_STN")));
				dto.setRunDate(StringUtil.objToStr(map.get("RUN_DATE")));
				dto.setStartStn(StringUtil.objToStr(map.get("START_STN")));
				dto.setTrainNbr(StringUtil.objToStr(map.get("TRAIN_NBR")));
				returnList.add(dto);
			}
		}
		return returnList;
	}

	/**
	 * 访问接口，将返回数据存到本地数据库
	 * startDate 格式:yyyy-mm-dd
	 * @throws Exception
	 */
    @Deprecated
	public int importTainPlan( String schemeId,
			 String startDate,  String dayCount) throws Exception {
		//调用后台接口之前推送一条消息到页面
		quoteService.sendQuotes("", 0, 0, "plan.getInfo.begin");
		List<PlanCrossInfo> listPlanCross = new ArrayList<PlanCrossInfo>();
		List<CrossInfo> listCross = crossService.getUnitCrossInfoForChartId(schemeId);
		if(listCross != null && listCross.size() > 0){
			for(CrossInfo cross : listCross){
				PlanCrossInfo planCross = new PlanCrossInfo();
				BeanUtils.copyProperties(planCross, cross);
				//单独设置属性名不一样的字段
				planCross.setPlanCrossId(UUID.randomUUID().toString());
				planCross.setBaseChartId(cross.getChartId());
				planCross.setBaseChartName(cross.getChartName());
				planCross.setBaseCrossId(cross.getBaseCrossId());
				planCross.setCrossStartDate(startDate.replaceAll("-",""));
				String crossStartDate = cross.getCrossStartDate();
				String crossEndDate = cross.getCrossEndDate();
				//int crossDayGap = DateUtil.getDaysBetween(DateUtil.getFormateDay(crossStartDate), DateUtil.getFormateDay(crossEndDate));
				//planCross.setCrossEndDate(DateUtil.getDateByDay(startDate, -(Integer.valueOf(dayCount) + crossDayGap)));
				planCross.setCrossEndDate("20140930");
				listPlanCross.add(planCross);
			}
		}
		//添加数据到表plan_cross
		int count = crossService.addPlanCrossInfo(listPlanCross);
		//做交路计划，第一个参数传null,表示全部交路都做
//		int crossCount = runPlanService.generateRunPlan(null, startDate, Integer.valueOf(dayCount));
		//全部处理完了，推送一条消息到页面
		quoteService.sendQuotes("", 0, 0, "plan.end");
		
		return 0;
	}
}
