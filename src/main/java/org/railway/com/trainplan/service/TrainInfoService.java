package org.railway.com.trainplan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.entity.PlanTrainForJbtEdit;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.TrainInfoServiceDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.railway.com.trainplan.service.task.DaytaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class TrainInfoService {
	
	private static final Logger logger = Logger.getLogger(TrainInfoService.class);
	@Autowired
	private BaseDao baseDao;
	

	/**
	 * 根据列车id查询列车基本信息
	 * @param baseTrainId
	 * @return
	 */
	public TrainlineTemplateDto getTrainInfoForTrainId(String baseTrainId){
		return (TrainlineTemplateDto)baseDao.selectOneBySql(Constants.TRAININFO_GETTRAININFO_FOR_TRAINID, baseTrainId);
	}
	
	
	
	/**
	 * 将后台接口返回数据存入本地PLAN_TRAIN和PLAN_TRAIN_STN库中
	 * @param list
	 * @param tempStartDate 格式yyyy-mm-dd
	 */
	public void addTrainPlanLine(List<TrainlineTemplateDto> list) {
		        try{
		        	if(list != null && list.size() > 0){
		        		
						for(TrainlineTemplateDto dto : list){
							
							List<TrainlineTemplateSubDto> subList = dto.getStationList();
							
							if(subList != null && subList.size() > 0){
								
								Map<String,Object>  map = new HashMap<String,Object>();
								
								map.put("trainStnList", subList);
								//批量插入数据表train_plan_stn
								int successCountStn = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN_STN, map);
								//System.err.println("count of inserting into train_plan_stn==" + successCountStn);
									
							}
							
						}
						//插入数据库
						Map<String,Object>  trainmap = new HashMap<String,Object>();
						trainmap.put("trainList",list);
					    int successCount = baseDao.insertBySql(Constants.TRAINPLANDAO_ADD_TRAIN_PLAN, trainmap);
						logger.info("count of inserting into train_plan==" + successCount);
					}	
		        }catch(Exception e){
					//不做任何处理，只打印日志
					e.printStackTrace();
					//logger.error("存表plan_train失败，plan_train_id["+trainId +"],runDate["+runDate+"]");
				}
			
	}
		
	
	
	/**
	 * 设置列车信息
	 * @param map
	 * @param dto
	 * @param runDate
	 * @return
	 */
	private TrainlineTemplateDto setTrainlineTemplateDto(Map<String,Object> map,TrainlineTemplateDto dto ,String runDate,String chartId){
		
		//所经局
		String passBureau = StringUtil.objToStr(map.get("PASS_BUREAU"));
		
		dto.setPassBureau(passBureau);
		//trainScope1:直通；0:管内
		if(passBureau !=null && !"".equals(passBureau)){
			if(passBureau.length()==1){
				dto.setTrainScope(0);
			}else{
				dto.setTrainScope(1);
			}
		}else{
			dto.setTrainScope(3);
		}
		dto.setTrainTypeId(StringUtil.objToStr(map.get("TRAIN_TYPE_ID")));
		
		dto.setBaseTrainId(StringUtil.objToStr(map.get("BASE_TRAIN_ID")));
		
		//车次
		String trainNbr = StringUtil.objToStr(map.get("TRAIN_NBR"));
		//始发站
		String startStn = StringUtil.objToStr(map.get("START_STN"));
		//始发时刻
		String startTime = StringUtil.objToStr(map.get("START_TIME"));
		dto.setTrainNbr(trainNbr);
		dto.setStartStn(startStn);
		//方案id
		dto.setBaseChartId(chartId);
		dto.setRunDate(runDate);
		
		if(runDate != null && !"".equals(runDate)){
			dto.setRunDate_8(runDate.replaceAll("-", ""));
		}
		
		dto.setEndStn(StringUtil.objToStr(map.get("END_STN")));
		dto.setStartBureauFull(StringUtil.objToStr(map.get("START_BUREAU_FULL")));
		dto.setEndBureauFull(StringUtil.objToStr(map.get("END_BUREAU_FULL")));
		dto.setStartBureau(StringUtil.objToStr(map.get("START_BUREAU")));
		dto.setEndBureau(StringUtil.objToStr(map.get("END_BUREAU")));
		//列车运行的天数
//		int runDaysAll = (( BigDecimal)map.get("RUN_DAYS_ALL")).intValue();
		int runDaysAll = MapUtils.getIntValue(map, "RUN_DAYS_ALL", 0);
		dto.setStartTime(runDate + " " + startTime);
		String endRunDate = DateUtil.getDateByDay(runDate, -runDaysAll);
		dto.setEndTime(endRunDate + " " + StringUtil.objToStr(map.get("END_TIME")));
		//始发日期+始发车次+始发站+计划始发时刻
		String planTrainSign  = runDate.replaceAll("-","") + "-" + trainNbr + "-" +startStn + "-" +startTime;
		dto.setPlanTrainSign(planTrainSign);
		return dto;
	}
	
	/**
	 * 设置经由站信息
	 */
	private TrainlineTemplateSubDto setTrainlineTemplateSubDto(Map<String,Object> map,TrainlineTemplateSubDto subDto,String runDate){
		//设置主键，为后面存库做准备
		subDto.setPlanTrainStnId(UUID.randomUUID().toString());
		//同一列车，设置经由站
		//站点名
		subDto.setName(StringUtil.objToStr(map.get("STN_NAME")));
		//设置列车号
		subDto.setTrainNbr(StringUtil.objToStr(map.get("TRAIN_NBR")));
		//所属局简称
		subDto.setBureauShortName(StringUtil.objToStr(map.get("STN_BUREAU")));
		//所属局全称
		subDto.setStnBureauFull(StringUtil.objToStr(map.get("STN_BUREAU_FULL")));
		//股道
		subDto.setTrackName(StringUtil.objToStr(map.get("TRACK_NAME")));
		subDto.setIndex((( BigDecimal)map.get("STN_SORT")).intValue());
		subDto.setRunDays((( BigDecimal)map.get("RUN_DAYS")).intValue());
		
		//int  rundays = (( BigDecimal)map.get("RUN_DAYS")).intValue();
		int rundays = MapUtils.getIntValue(map, "RUN_DAYS", 0);
		String currentRunDate = DateUtil.getDateByDay(runDate, -rundays);	
		//到站时间
		subDto.setSourceTime(currentRunDate + " " + StringUtil.objToStr(map.get("ARR_TIME")) );
		//离站时间
		subDto.setTargetTime(currentRunDate + " " + StringUtil.objToStr(map.get("DPT_TIME")) );
		
		subDto.setBaseArrTime(subDto.getSourceTime());
		subDto.setBaseDptTime(subDto.getTargetTime());
		return subDto;
	}
	
	
	  /**
	   * 根据方案id等信息查询列车信息列表(分页)
	   */
	  public List<PlanTrain> getTrainsForPage(Map<String, Object> params){
		 return baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO_PAGE, params);
	  }
	  /**
	   * 根据方案id等信息查询列车信息列表
	   */
	  public List<PlanTrainForJbtEdit> getTrainsForPageForEdit(Map<String, Object> params){
		  return baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO_PAGE_EDIT, params);
	  }
	  
	  /**
	   * 根据主键id等信息查询列车信息列表
	   */
	  public PlanTrainForJbtEdit getTrainsForPageForjbtId(String trainlineId){
		  Map<String, Object> params = new HashMap<String, Object>();
		  params.put("trainlineId", trainlineId);
		  return (PlanTrainForJbtEdit) baseDao.selectOneBySql(Constants.TRAININFO_GETTRAININFO_PAGE_JBT_ID, params);
	  }
	  /**
	   * 根据方案id等信息查询列车信息列表
	   */
	  public List<PlanTrain> getTrains(Map<String, Object> params){
		 return baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO, params);
	  }
	  /**
	   * 根据方案id等信息查询列车总数
	   */
	  public int getTrainInfoCount(Map<String,Object> params){
		  List<Map<String,Object>> countList = baseDao.selectListBySql(Constants.TRAININFO_GETTRAININFO_COUNT, params);
		  int count= 0;
		  if(countList != null && countList.size() > 0){
			  //只有一条数据
			  Map<String,Object> map = countList.get(0);
			  count = (( BigDecimal)map.get("COUNT")).intValue();
		  }
		  return count;
	  }
	public long getTrainLinesCount(Map<String, Object> reqMap) {
		  List<Map<String,Object>> countList = baseDao.selectListBySql(Constants.GETTRAINLINES_JHPT_COUNT, reqMap);
		  int count= 0;
		  if(countList != null && countList.size() > 0){
			  //只有一条数据
			  Map<String,Object> map = countList.get(0);
			  count = (( BigDecimal)map.get("COUNT")).intValue();
		  }
		  return count;
	}
	public Object getTrainLinesForPage(Map<String, Object> reqMap) {
		 return baseDao.selectListBySql(Constants.GETTRAINLINES_JHPT, reqMap);
	}
	public void deleteTrainlineItemTempByParentId(String trainlineTempId){
		  Map<String, Object> map = new HashMap<String, Object>();
		  map.put("trainlineTempId", trainlineTempId);
		baseDao.deleteBySql(Constants.DELETETRAINLINES_JBT_BY_PARENTID, map);
	}

}
