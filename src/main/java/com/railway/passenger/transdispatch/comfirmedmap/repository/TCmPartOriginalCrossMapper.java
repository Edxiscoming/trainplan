package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;
import java.util.Map;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPartOriginalCross;

public interface TCmPartOriginalCrossMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmPartOriginalCrossId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmPartOriginalCross record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmPartOriginalCross record);

    /**
     * 根据主键查询记录
     */
    TCmPartOriginalCross selectByPrimaryKey(String cmPartOriginalCrossId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmPartOriginalCross record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmPartOriginalCross record);
    
    /**
     * 批量插入对数表信息
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月23日
     */
    int batchInsert(List<TCmPartOriginalCross> list);
    
    /**
     * 批量更新对数表信息
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月23日
     */
    int batchUpdateCheckFlag(List<String> list);
    
    /**
     * 批量删除
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月23日
     */
    int batchDelete(List<String> list);
    
    /**
     * 分页查询对数表信息
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return List<TCmPartOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年9月23日
     */
    List<TCmPartOriginalCross> pageQuery(Map<String, Object> map);
    
    /**
     * 查询符合条件的所有数据总数
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月23日
     */
    int queryTotalCount(Map<String, Object> map);
    
    /**
     * 根据条件搜索对数表信息
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return List<TCmPartOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年9月23日
     */
    List<TCmPartOriginalCross> queryByParam(Map<String, Object> map);
    
    /**
     * 根据ID列表批量删除对数表信息
     * @Description: TODO
     * @param @param keys
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月25日
     */
    int batchDeleteByKeys(List<String> keys);
}