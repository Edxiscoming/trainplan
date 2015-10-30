package com.railway.passenger.transdispatch.operationplan.service;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;

public interface IOperationlanService {
	/**
	 * 
	 * @Description: 根据逻辑交路生成开行计划所有相关数据
	 * @param @param cross 逻辑交路
	 * @param @param startDate 计划开始日期
	 * @param @param generateDate 计划天数
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月25日
	 */
	public boolean generatePlanCrossInfo(TCmCross cross, String startDate ,String endDate);
	
	public void autoGeneratePlan();
}
