package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;
import java.util.Map;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TPlanTrain;

public interface TPlanTrainMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String planTrainId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TPlanTrain record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TPlanTrain record);

    /**
     * 根据主键查询记录
     */
    TPlanTrain selectByPrimaryKey(String planTrainId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TPlanTrain record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TPlanTrain record);
    
    /**
     * 
     * @Description: 根据条件分页查询开行计划
     * @param @param map
     * @param @return   
     * @return List<TCmCross>  
     * @throws
     * @author zhangmenghao
     * @date 2015年9月23日
     */
    List<TCmCross> PageQueryPlanCross(Map<String, Object> map);
    /**
     * 
     * @Description: 获取符合条件的开行计划总数
     * @param @param map
     * @param @return   
     * @return int  
     * @throws
     * @author zhangmenghao
     * @date 2015年9月23日
     */
    int getPlanCrossInfoTotalCount(Map<String, Object> map);

}