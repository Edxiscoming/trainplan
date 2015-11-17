package com.railway.passenger.transdispatch.operationplan.repository;

import java.util.List;
import java.util.Map;

import com.railway.passenger.transdispatch.operationplan.entity.TCmPlanAutogenerate;

public interface TCmPlanAutogenerateMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmAutogenId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmPlanAutogenerate record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmPlanAutogenerate record);

    /**
     * 根据主键查询记录
     */
    TCmPlanAutogenerate selectByPrimaryKey(String cmAutogenId);
    
    /**
     * 
     * @Description: TODO
     * @param @param 条件查询
     * @param @return   
     * @return TCmPlanAutogenerate  
     * @throws
     * @author qs
     * @date 2015年10月26日
     */
    List<TCmPlanAutogenerate> selectByParam(Map<String, Object> map);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmPlanAutogenerate record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmPlanAutogenerate record);
}