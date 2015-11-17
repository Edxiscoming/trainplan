package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;
import java.util.Map;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;

public interface TCmOriginalCrossMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmOriginalCrossId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmOriginalCross record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmOriginalCross record);

    /**
     * 根据主键查询记录
     */
    TCmOriginalCross selectByPrimaryKey(String cmOriginalCrossId);

    /**
     * 根据主键更新属性不为空的记录
     */
    TCmOriginalCross selectByPrimaryKey_Cross(String record);
    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmOriginalCross record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmOriginalCross record);
    
    /**
     * 根据对数表列表批量更新对数表信息
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月22日
     */
    int batchUpdateOriginalCross(List<TCmOriginalCross> list);
    /**
     * 根据ID数据查询对数表列表
     * @Description: TODO
     * @param @param cmOriginalCrossIds
     * @param @return   
     * @return List<TCmOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年9月22日
     */
    List<TCmOriginalCross> selectByPrimaryKeys(List<String> list);
    
    /**
     * 分页查询对数表信息
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return List<TCmOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年9月22日
     */
    List<TCmOriginalCross> pageQuery(Map<String, Object> map);
    
    /**
     * 获取符合条件的对数表总数
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月22日
     */
    int queryTotalCount(Map<String, Object> map);
    
    /**
     * 批量更新对数表审核状态
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月26日
     */
    int batchUpdateCheckFlag(List<String> list);
    
    /**
     * 批量更新对数表审核状态(审核不通过)
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月26日
     */
    int batchUpdateCheckFlagNotPass(List<String> list);
    
    /**
     * 批量更新对数表生成交路状态
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年9月28日
     */
    int batchUpdateCreateCrossFlag(List<String> list);
    
    /**
     * 根据条件批量查询对数表信息
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return List<TCmOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年9月28日
     */
    List<TCmOriginalCross> queryByParam(Map<String, Object> map);
    
    /**
     * 根据交路列表查询符合条件已存在的交路
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return List<TCmOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年10月25日
     */
    List<TCmOriginalCross> queryExistCross(List<TCmOriginalCross> list);
    
    /**
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月4日
     */
    int batchInsert(List<TCmOriginalCross> list);
    
    /**
     * @Description: TODO
     * @param @param map
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月4日
     */
    int deleteByParam(List<TCmOriginalCross> list);
    
    /**
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return int  
     * @throws
     * @author yuyangxing
     * @date 2015年10月5日
     */
    int deleteByKeys(List<String> list);
    
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
    int batchConfigOperationRule(List<TCmOriginalCross> list);
    
    /**
     * 批量设置交替信息
     * @param list
     * @return
     */
    int batchConfigAlternate(List<TCmOriginalCross> list);
    
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
     * 查询所有对数交路信息
     * @Description: TODO
     * @param @return   
     * @return List<TCmOriginalCross>  
     * @throws
     * @author yuyangxing
     * @date 2015年10月27日
     */
    List<TCmOriginalCross> queryNoOperationCross(Map<String, Object> map);
    
    /**
     * 查询所有未设置交替信息的交路
     * @param map
     * @return
     */
    List<TCmOriginalCross> queryNoAlternateCross(Map<String, Object> map);
}