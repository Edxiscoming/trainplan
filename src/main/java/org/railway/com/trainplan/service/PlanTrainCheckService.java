package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.Ljzd;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.service.dto.PagingInfo;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.service.dto.PlanBureauStatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Component
@Transactional
@Monitored
public class PlanTrainCheckService {
	private static final Logger logger = Logger.getLogger(PlanTrainCheckService.class);


	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BaseDao baseDao;


	/**
	 * 根据run_date的开始时间和结束时间删除
	 * 表plan_train_stn中的历史数据
	 * @param startRunDate 格式：yyyyMMdd
	 * @param endRunDate 格式：yyyyMMdd
	 * @throws Exception
	 */
	public void deletePlanTrainStnHistoryDate(String startRunDate,String endRunDate) throws Exception {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("startRunDate",startRunDate );
		paramMap.put("endRunDate",endRunDate );
		int deleteStnCount = baseDao.deleteBySql(Constants.TRAINPLANDAO_DELETE_TRAIN_RUNDATE_TRAIN_STN_INIT, paramMap);
		logger.info("deleteStnCount==" +deleteStnCount );
	}
	/**
	 * 根据run_date的开始时间和结束时间删除
	 * 表plan_train中的历史数据
	 * @param startRunDate 格式：yyyyMMdd
	 * @param endRunDate 格式：yyyyMMdd
	 * @throws Exception
	 */
	public void deletePlanTrainHistoryDate(String startRunDate,String endRunDate) throws Exception {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("startRunDate",startRunDate );
		paramMap.put("endRunDate",endRunDate );
		int deleteNbrCount = baseDao.deleteBySql(Constants.TRAINPLANDAO_DELETE_TRAIN_RUNDATE_TRAIN_NBR_INIT, paramMap);
		logger.info("deleteNbrCount==" +deleteNbrCount );
	}
	/**
	 * 根据条件删除表train_plan中数据
	 * @param runDate 开行日期，格式：yyyyMMdd
	 * @param trainNbr 列车号
	 * @throws Exception
	 */
	public void deletePlanTrainWithRundate(String runDate,String trainNbr) throws Exception{
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("runDate",runDate );
		paramMap.put("trainNbr",trainNbr );
		//执行删除动作
	    int deleteCount = baseDao.deleteBySql(Constants.TRAINPLANDAO_DELETE_TRAIN_RUNDATE_NBR, paramMap);
	    logger.info("deleteCount==" +deleteCount );
	}
	
	/**
	 * 根据条件删除表train_plan_stn中数据
	 * @param runDate 开行日期，格式：yyyyMMdd
	 * @param trainNbr 列车号
	 * @throws Exception
	 */
	public void deletePlanTrainStnWithRundate(String runDate,String trainNbr) throws Exception {
		
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("runDate",runDate );
		paramMap.put("trainNbr",trainNbr );
		int deleteStnCount = baseDao.deleteBySql(Constants.TRAINPLANDAO_DELETE_TRAIN_RUNDATE_TRAIN_STN, paramMap);
		 logger.info("deleteStnCount==" +deleteStnCount );
	}
	/**
	 * 通过开行时间段查询某局的开行客运信息
	 * @param paging  分页对象
	 * @param startBureauFullName  客车始发局全称
	 * @param beginRunDate   开始运行时间,格式yyyy-MM-dd
	 * @param endRunDate   结束运行时间,格式yyyy-MM-dd
	 * @param trainNbr 车次号
	 * @return  分页对象
	 */
	public PagingResult findPlanTrainWithPeriodRunDate(PagingInfo paging,String startBureauFullName,String startDate,int dayCount,String trainNbrFrom){
		int currentPage = paging.getCurrentPage();
		int pageSize = paging.getPageSize();
		
		int startNum = pageSize * currentPage +1;
		int endNum = pageSize * ( currentPage + 1);
		int totalCount = 0;
		List<Map<String,Object>> returnDaysList = new ArrayList<Map<String,Object>>();
		List<String> daysList = DateUtil.getDateListWithDayCount(startDate, dayCount);
		
		if(daysList != null && daysList.size() > 0){
			String endDate = DateUtil.getDateByDay(startDate, -(dayCount-1));
			//相当于格式化时间
			startDate =  DateUtil.getDateByDay(startDate, 0);
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("startDate",DateUtil.format(DateUtil.parse(startDate),"yyyyMMdd"));
			paramMap.put("endDate",DateUtil.format(DateUtil.parse(endDate),"yyyyMMdd"));
			paramMap.put("startBureauFull",startBureauFullName);
			
			if(!"".equals(trainNbrFrom) && trainNbrFrom != null){
				paramMap.put("trainNbr",trainNbrFrom);
			}
			paramMap.put("rownumstart",startNum);
			paramMap.put("rownumend",endNum);
			//查询数据库 
			//某天某局的车次
			List<Map<String,Object>> list = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_PERIOD_RUNDATE_TRAINS, paramMap);
			
			if( list != null && list.size() > 0){
				//查询数据总的条数
				Map<String,Object> paramCountMap = new HashMap<String,Object>();
				paramCountMap.put("startDate",DateUtil.format(DateUtil.parse(startDate),"yyyyMMdd"));
				paramCountMap.put("endDate",DateUtil.format(DateUtil.parse(endDate),"yyyyMMdd"));
				paramCountMap.put("startBureauFull",startBureauFullName);
				if(!"".equals(trainNbrFrom) && trainNbrFrom != null){
					paramCountMap.put("trainNbr",trainNbrFrom);
				}
				
				List<Map<String,Object>> listTotalCount = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_RUNDATE_TRAINS_TOTAL, paramCountMap);
				//只有一条数据
				if(listTotalCount != null){
					Map<String,Object> countMap = listTotalCount.get(0);
					totalCount = StringUtil.strToInteger(StringUtil.objToStr(countMap.get("COUNT")));
					
				}
				for(Map<String,Object> map : list){
					Map<String,Object> pageDataMap = new HashMap<String,Object>();
					List<Map<String,Object>> days = new ArrayList<Map<String,Object>>();
					String trainNbr = StringUtil.objToStr(map.get("TRAIN_NBR"));
					
					pageDataMap.put("trainNbr", trainNbr);
					for(int i = 0;i<daysList.size();i++){
						Map<String,Object> dayMap = new HashMap<String,Object>();
						String myRunDate = DateUtil.getDateByDay(startDate, -i);
						
						dayMap.put("key",trainNbr+"@@"+myRunDate.replaceAll("-", "") );
						dayMap.put("index",i+1);
						
						Map<String,Object> paramMapCount = new HashMap<String,Object>();
						paramMapCount.put("runDate",myRunDate.replaceAll("-", ""));
						paramMapCount.put("trainNbr",trainNbr );
						List<Map<String,Object>> listMap = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_RUNDATE_TRAIN_COUNT, paramMapCount);
						int count = StringUtil.strToInteger(StringUtil.objToStr(listMap.get(0).get("COUNT")));	
						if(count > 0){
							//status:0:不开行，1：开行
							dayMap.put("status",1);
						}else{
							dayMap.put("status",0);
						}
						days.add(dayMap);
					}
					pageDataMap.put("days", days);
					returnDaysList.add(pageDataMap);
				}
			   	
			}
			
		}
	
		return new PagingResult(Long.valueOf(totalCount),Long.valueOf(pageSize),returnDaysList);
	}
	
	/**
	 * 根据始发局局码、开行日期查询列车开行计划信息
	 * @param startBureau 始发局局码
	 * @param runDate 开行日期 yyyyMMdd
	 * @author denglj
	 * @return
	 */
	public PagingResult findPlanTrainByStartBureauAndRundate(PagingInfo paging, String startBureau, String runDate, String trainNbr){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		int currentPage = paging.getCurrentPage();
		int pageSize = paging.getPageSize();
		
		int startNum = pageSize * currentPage +1;
		int endNum = pageSize * ( currentPage + 1);
		int totalCount = 0;
		paramMap.put("runDate",runDate );
		paramMap.put("startBureauFull",startBureau );
		paramMap.put("rownumstart", startNum);
		paramMap.put("rownumend", endNum);
		
		if(trainNbr!=null) {
			paramMap.put("trainNbr", trainNbr);
		}
		
		List<PlanTrain> list = baseDao.selectListBySql(Constants.TRAINPLANDAO_FIND_PLANTRAIN_BY_START_BUREAU, paramMap);
		
		if(list != null && list.size() > 0){
			//查询总的条数
			List<Map<String,Object>> listTotalCount = baseDao.selectListBySql(Constants.TRAINPLANDAO_FIND_PLANTRAIN_BY_START_BUREAU_COUNT, paramMap);
			//只有一条数据
			if(listTotalCount != null){
				Map<String,Object> countMap = listTotalCount.get(0);
				totalCount = StringUtil.strToInteger(StringUtil.objToStr(countMap.get("count")));
				
			}
			for(PlanTrain obj:list) {
				//查询生成经由局
				StringBuffer tempJylj = new StringBuffer("");//经由路局
				String tempJyljStr = "";
				List<Map<String, Object>> listMap = this.getTrainTimeDetail(runDate, obj.getTrainNbr());
				String previousLjjc = "";//上一个路局简称
				if (listMap!=null && listMap.size()>0) {
					for(int i=0;i<listMap.size();i++) {
						String currentLjjc = String.valueOf(listMap.get(i).get("LJJC"));
						if (!previousLjjc.equals(currentLjjc)) {
							tempJylj.append(currentLjjc).append(",");
							previousLjjc = currentLjjc;
						}
					}
					if (!tempJylj.equals("")) {
						tempJyljStr = tempJylj.substring(0, tempJylj.lastIndexOf(","));
					}
					
				}
				obj.setJylj(tempJyljStr);
				
				
				//格式化时间
				obj.setStartTimeStr(DateUtil.format(obj.getStartTime(), "MMdd HH:mm"));
				obj.setEndTimeStr(DateUtil.format(obj.getEndTime(), "MMdd HH:mm"));
				//获取局
				try {
					Ljzd ljzd = commonService.getLjInfo(obj.getStartBureauFull());
					if(ljzd != null ){
						obj.setStartBureau(ljzd.getLjjc());
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//获取局
				try {
					Ljzd ljzd =commonService.getLjInfo(obj.getEndBureauFull());
					if(ljzd != null ){
						obj.setEndBureau(ljzd.getLjjc());
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		    }	
			
		}
		
		PagingResult pageing = new PagingResult(totalCount, paging.getPageSize(), list);
		return pageing;
	}
	
	/**
	 * 统计某天某局接入交出，接入终到的列车数
	 * runDate:开行日期 yyyyMMdd
	 * bureauName :路局全称
	 */
	
	public Map<String,Object> getOneStationTrains(String runDate,String bureauName){
		List<Map<String,Object>> list = null;
		Map<String,Object> returnMap = null;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("endBureauFull",bureauName);
		paramMap.put("runDate",runDate);
		paramMap.put("stnBureauFull",bureauName);
		paramMap.put("startBureauFull",bureauName);
		list = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TOTAL_STATION_JIERU, paramMap);	
		if(list != null){
			//只有一条数据
			returnMap = list.get(0);
		}
		return returnMap;
	}
    /**
     * 统计一个路局某天的图定，临客，施工等信息
     * @param runDate 开行日期，格式：yyyyMMdd
     * @param startBureauFull 路局全称
     */
	public List<Map<String,Object>> getFullStationTrains(String runDate,String startBureauFull){
		List<Map<String,Object>> resultList = null;
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("runDate",runDate);
		//统计始发交出，始发终到
		if(startBureauFull !=null && !"".equals(startBureauFull)){
			paramMap.put("ljqc",startBureauFull);
			//统计一个路局的信息
			resultList = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_ONE_STATION_INFO, paramMap);
			
		}else{
			//统计全路局的信息
			resultList = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TOTAL_STATION_INFO, paramMap);
			
		}
		//统计接入交出，接入终到
		return resultList;
	}
	
	
	
	
	/**
	 * 根据车次及开行日期查询 时刻信息
	 * @param trainNbr 车次号
	 * @param runDate 开行日期 yyyy-MM-dd
	 * @author denglj
	 * @return
	 */
	public List<Map<String, Object>> getTrainTimeDetail(String runDate, String trainNbr) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("runDate", runDate);
		paramMap.put("trainNbr", trainNbr);
		//调用后台数据库
		List<Map<String, Object>> listMap = baseDao.selectListBySql(Constants.TRAINPLANDAO_GET_TRAIN_TIME_DETAIL, paramMap);
		if (listMap != null && listMap.size()>0) {
			for (Map mp :listMap) {
				if("1".equals(String.valueOf(mp.get("IS_START_STN")))) {//始发站
					mp.put("ARR_TIME", "----");//到站时间
					mp.put("STOP_TIME", "----");//停靠时间
				} else if("1".equals(String.valueOf(mp.get("IS_END_STN")))) {//终到站
					mp.put("DPT_TIME", "----");//出发时间
					mp.put("STOP_TIME", "----");//停靠时间
				} else {
					mp.put("STOP_TIME", DateUtil.calcBetweenTwoTimes(String.valueOf(mp.get("ARR_TIME_ALL")), String.valueOf(mp.get("DPT_TIME_ALL"))));//停靠时间
				}
			}
		}
		
		return listMap;
	}
	
	
}
