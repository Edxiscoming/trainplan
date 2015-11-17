package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainAlternate;

public interface TCmTrainAlternateMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmTrainAlternateId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmTrainAlternate record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmTrainAlternate record);

    /**
     * 根据主键查询记录
     */
    TCmTrainAlternate selectByPrimaryKey(String cmTrainAlternateId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmTrainAlternate record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmTrainAlternate record);
	/**
	 * 
	 * @Description: TODO
	 * @param @param 根据车次ID获取其交替信息
	 * @param @return   
	 * @return TCmTrainAlternate  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
    List<TCmTrainAlternate> getTCmTrainAlternate(String trainId);
}