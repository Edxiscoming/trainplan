package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrain;

public interface TCmTrainMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmTrainId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmTrain record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmTrain record);

    /**
     * 根据主键查询记录
     */
    TCmTrain selectByPrimaryKey(String cmTrainId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmTrain record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmTrain record);
    /**
     * 
     * @Description: 通过CrossId查询出TCmTrain列表
     * @param @param tCmCross
     * @param @return   
     * @return List<TCmTrain>  
     * @throws
     * @author Administrator
     * @date 2015年9月21日
     */
    List<TCmTrain> selectByCmCrossId(Object value);
}