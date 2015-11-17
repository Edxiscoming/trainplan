package com.railway.passenger.transdispatch.operationplan.service;

import java.util.Map;

import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TPlanTrain;


public interface IRunPlanService {
	/**
	 * 
	 * @Description: 查询开行计划信息
	 * @param @param planCross
	 * @param @return 
	 * @return List<TCmCross>  
	 * @throws
	 * @author zhangmenghao
	 * @date 2015年9月22日
	 */
	Result <TCmCross> getPlanCrosses(Map<String, Object> map);
	
	/**
	 * 
	 * @Description: 删除开行计划交路（删除交路下所有开行计划）
	 * @param @param planCross
	 * @param @return 
	 * @return List<TCmCross>  
	 * @throws
	 * @author heyy
	 * @date 2015年9月23日
	 */
	Result <TCmCross> deletePlanCross(String ids);
	
	
	/**
	 * 
	 * @Description: 查询开行计划详情
	 * @param @param planCross
	 * @param @return 
	 * @return List<TCmCross>  
	 * @throws
	 * @author heyy
	 * @date 2015年9月23日
	 */
	Result <TCmCross> getPlanCrossDetail(String id);
	
	
	/**
	 * 
	 * @Description: 审核开行计划，按plantrain的粒度来审核
	 * @param @param planCross
	 * @param @return 
	 * @return List<TCmCross>  
	 * @throws
	 * @author heyy
	 * @date 2015年9月23日
	 */
	Result <TPlanTrain> checkPlanTrain(String ids);
	
	
	
	/**
	 * 
	 * @Description: 生成开行计划成功后往页面推送消息
	 * @param @param planCross
	 * @param @return 
	 * @return List<TCmCross>  
	 * @throws
	 * @author heyy
	 * @date 2015年9月23日
	 */
	Result <TCmCross> sendPlanTrainMsg(Map<String, Object> map);
	
	
}
