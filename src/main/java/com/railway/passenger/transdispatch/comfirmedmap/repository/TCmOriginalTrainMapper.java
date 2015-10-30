package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;
import java.util.Map;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalTrain;

public interface TCmOriginalTrainMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmOriginalTrainId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmOriginalTrain record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmOriginalTrain record);

    /**
     * 根据主键查询记录
     */
    TCmOriginalTrain selectByPrimaryKey(String cmOriginalTrainId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmOriginalTrain record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmOriginalTrain record);
    
    /**
     * 批量存储车次信息
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月4日
     */
    int batchInsert(List<TCmOriginalTrain> list);
    
    /**
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月4日
     */
    int deleteByParam(List<TCmOriginalTrain> list);
    
    /**
     * 根据主键ID批量删除
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月23日
     */
    int batchDeleteByKeys(List<String> list);
    
    /**
     * 根据外键ID批量删除
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月5日
     */
    int batchDeleteByFKeys(List<String> list);
    
    /**
     * 根据条件查询车次列表
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return List<TCmOriginalTrain>  
     * @throws
     * @author yuyangxing
     * @date 2015年10月5日
     */
    List<TCmOriginalTrain> queryByParam(Map<String, Object> map);
    
    /**
     * 批量修改车次信息
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月7日
     */
    int batchUpdateTrainsSort(List<TCmOriginalTrain> list);
    
    /**
     * 查询交路下的车次总数
     * @Description: TODO
     * @param @param crossId
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月7日
     */
    int queryTrainCountByCrossId(String crossId);
    
    /**
     * 批量设置开行规律
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月25日
     */
    int batchConfigOperationRule(List<TCmOriginalTrain> list);
    
    /**
     * 批量修改使用状态
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月25日
     */
    int batchUpdateUseStatus(List<String> list);
    
    /**
     * 查询所有对数车次信息
     * @Description: TODO
     * @param @return   
     * @return List<TCmOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年10月27日
     */
    List<TCmOriginalTrain> queryNoOperationTrain(Map<String, Object> map);
}