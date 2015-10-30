package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCrossCheck;

public interface TCmOriginalCrossCheckMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmOriginalCrossCheckId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmOriginalCrossCheck record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmOriginalCrossCheck record);

    /**
     * 根据主键查询记录
     */
    TCmOriginalCrossCheck selectByPrimaryKey(String cmOriginalCrossCheckId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmOriginalCrossCheck record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmOriginalCrossCheck record);
    
    /**
     * 根据对数表ID查询记录
     */
    List selectByPrimaryKey_original(String cmOriginalCrossId);
    
    /**
     * 根据对数表ID删除记录
     */
    int deleteByPrimaryKey_original(String cmOriginalCrossId,String roadBureau);
}