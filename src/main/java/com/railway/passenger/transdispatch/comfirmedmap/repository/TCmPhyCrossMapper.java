package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCross;

public interface TCmPhyCrossMapper {
	/**
	 * 
	 * @Description: 根据cmCrossId获取车底交路列表
	 * @param @param cmCrossId
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	List<TCmPhyCross> selectByCmCrossId(String cmCrossId);
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmPhyCrossId);
    /**
     * 
     * @Description: 根据主键逻辑删除
     * @param @param cmPhyCrossId
     * @param @return   
     * @return int  
     * @throws
     * @author Administrator
     * @date 2015年9月22日
     */
    int deleteByPrimaryKeyLogic(String cmPhyCrossId);
    /**
     * 
     * @Description: 根据逻辑交路ID删除物理交路
     * @param @param cmCrossId
     * @param @return   
     * @return int  
     * @throws
     * @author Administrator
     * @date 2015年9月22日
     */
    int deleteByCmCross(String cmCrossId);
    /**
     * 
     * @Description: 根据逻辑交路ID逻辑删除物理交路
     * @param @param cmCrossId
     * @param @return   
     * @return int  
     * @throws
     * @author Administrator
     * @date 2015年9月22日
     */
    int deleteByCmCrossLogic(String cmCrossId);
    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmPhyCross record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmPhyCross record);

    /**
     * 根据主键查询记录
     */
    TCmPhyCross selectByPrimaryKey(String cmPhyCrossId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmPhyCross record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmPhyCross record);
}