package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCrossTrain;

public interface TCmPhyCrossTrainMapper {
	/**
	 * 
	 * @Description: 根据车底交路ID获取其对应的车次信息
	 * @param @param cmCrossId
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	List<TCmPhyCrossTrain> selectByCmPhyCrossId(String cmPhyCrossId);
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmPhyCrossTrainId);
    /**
	 * 
	 * @Description: 根据主键逻辑删除记录
	 * @param @param cmCrossId
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
    int deleteByPrimaryKeyLogic(String cmPhyCrossTrainId);
    /**
     * 
     * @Description: 根据物理交路ID删除相关的车次信息
     * @param @param cmPhyCrossId
     * @param @return   
     * @return int  
     * @throws
     * @author Administrator
     * @date 2015年9月22日
     */
    int deleteByCmPhyCross(String cmPhyCrossId);
    /**
     * 
     * @Description: 根据物理交路ID逻辑删除相关的车次信息
     * @param @param cmPhyCrossId
     * @param @return   
     * @return int  
     * @throws
     * @author Administrator
     * @date 2015年9月22日
     */
    int deleteByCmPhyCrossLogic(String cmPhyCrossId);
    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmPhyCrossTrain record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmPhyCrossTrain record);

    /**
     * 根据主键查询记录
     */
    TCmPhyCrossTrain selectByPrimaryKey(String cmPhyCrossTrainId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmPhyCrossTrain record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmPhyCrossTrain record);
}