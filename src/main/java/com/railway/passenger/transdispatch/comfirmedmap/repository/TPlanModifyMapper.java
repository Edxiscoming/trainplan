package com.railway.passenger.transdispatch.comfirmedmap.repository;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TPlanModify;



public interface TPlanModifyMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String planModifyId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TPlanModify record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TPlanModify record);

    /**
     * 根据主键查询记录
     */
    TPlanModify selectByPrimaryKey(String planModifyId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TPlanModify record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TPlanModify record);
}