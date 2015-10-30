package com.railway.passenger.transdispatch.comfirmedmap.repository;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TPlanTrainStn;



public interface TPlanTrainStnMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String planTrainStnId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TPlanTrainStn record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TPlanTrainStn record);

    /**
     * 根据主键查询记录
     */
    TPlanTrainStn selectByPrimaryKey(String planTrainStnId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TPlanTrainStn record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TPlanTrainStn record);
}