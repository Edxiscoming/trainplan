package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainRule;

public interface TCmTrainRuleMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmTrainRuleId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmTrainRule record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmTrainRule record);

    /**
     * 根据主键查询记录
     */
    TCmTrainRule selectByPrimaryKey(String cmTrainRuleId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmTrainRule record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmTrainRule record);
    
	/**
	 * 
	 * @Description: TODO
	 * @param @param cross
	 * @param @return   
	 * @return 根据车次ID获取其开行规律  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
    List<TCmTrainRule> getTCmTrainRule(String trainId);
}