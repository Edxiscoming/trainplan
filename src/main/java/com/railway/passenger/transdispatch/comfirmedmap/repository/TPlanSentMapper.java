package com.railway.passenger.transdispatch.comfirmedmap.repository;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TPlanSent;


public interface TPlanSentMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String planSentId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TPlanSent record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TPlanSent record);

    /**
     * 根据主键查询记录
     */
    TPlanSent selectByPrimaryKey(String planSentId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TPlanSent record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TPlanSent record);
}