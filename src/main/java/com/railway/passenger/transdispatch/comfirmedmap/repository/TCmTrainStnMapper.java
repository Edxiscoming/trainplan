package com.railway.passenger.transdispatch.comfirmedmap.repository;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainStn;

public interface TCmTrainStnMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmTrainStnId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmTrainStn record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmTrainStn record);

    /**
     * 根据主键查询记录
     */
    TCmTrainStn selectByPrimaryKey(String cmTrainStnId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmTrainStn record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmTrainStn record);
}