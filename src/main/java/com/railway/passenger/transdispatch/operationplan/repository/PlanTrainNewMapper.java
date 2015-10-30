package com.railway.passenger.transdispatch.operationplan.repository;

import java.util.List;
import java.util.Map;

import com.railway.passenger.transdispatch.operationplan.entity.PlanTrainNew;

public interface PlanTrainNewMapper {
	/**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String planTrainId);
    
    /**
     * 
     * @Description: 根据条件删除记录
     * @param @param param
     * @param @return   
     * @return int  
     * @throws
     * @author qs
     * @date 2015年10月5日
     */
    int deleteByParams(Map<String,Object> param);
    
    /**
     * 
     * @Description: 根据计划交路ID删除对应的所有车次信息
     * @param @param planCrossId
     * @param @return   
     * @return int  
     * @throws
     * @author Administrator
     * @date 2015年9月26日
     */
    int deleteByPlanCrossId(String planCrossId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(PlanTrainNew record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(PlanTrainNew record);

    /**
     * 根据主键查询记录
     */
    PlanTrainNew selectByPrimaryKey(String planTrainId);
    
    /**
     * 
     * @Description: 根据计划交路ID以及车次名获取开行计划中rundate日期最靠后的车次信息
     * @param @param param
     * @param @return   
     * @return PlanTrainNew  
     * @throws
     * @author Administrator
     * @date 2015年10月5日
     */
    PlanTrainNew selectLatestRunDatePlanTrain(Map<String, Object> param);
    
    /**
     * 
     * @Description: 根据计划交路ID查询其下面的所有车次
     * @param @return   
     * @return List<PlanTrainNew>  
     * @throws
     * @author Administrator
     * @date 2015年9月26日
     */
    List<PlanTrainNew> selectByPlanCrossId(String planCrossId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(PlanTrainNew record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(PlanTrainNew record);
    
    /**
     * 
     * @Description: 根据条件统计
     * @param @param param
     * @param @return   
     * @return Map<String,Object>  
     * @throws
     * @author qs
     * @date 2015年10月27日
     */
    List<Map<String,Object>> countByParams(Map<String,Object> param);
}