package com.railway.passenger.transdispatch.comfirmedmap.repository;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TPlanCheck;



public interface TPlanCheckMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String planCheckId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TPlanCheck record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TPlanCheck record);

    /**
     * 根据主键查询记录
     */
    TPlanCheck selectByPrimaryKey(String planCheckId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TPlanCheck record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TPlanCheck record);
}