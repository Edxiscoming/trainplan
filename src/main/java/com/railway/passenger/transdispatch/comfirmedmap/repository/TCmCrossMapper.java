package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;
import java.util.Map;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCrossAndOriginaCross;

public interface TCmCrossMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmCrossId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmCross record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmCross record);

    /**
     * 根据主键查询记录
     */
    TCmCross selectByPrimaryKey(String cmCrossId);
    
    /**
     * 
     * @Description: 根据crossName和方案ID获取新的逻辑交路信息
     * @param @param param
     * @param @return   
     * @return TCmCross  
     * @throws
     * @author qs
     * @date 2015年10月5日
     */
    TCmCross selectByCrossNameAndScheme(Map<String,Object> param);
    
    public List<TCmCross> selectAll();
    
    /**
     * 
     * @Description: 根据原始对数表ID查询逻辑交路信息
     * @param @param cmOriginalCrossId
     * @param @return   
     * @return TCmCross  
     * @throws
     * @author Administrator
     * @date 2015年9月29日
     */
    TCmCross selectByCmOriginalCross(String cmOriginalCrossId);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmCross record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmCross record);
    
    /**
     * @param value
     * @return
     * 根据方案或者车次查询交路信息
     */
    public List<TCmCrossAndOriginaCross> getCmUnitCrossInfo(Map<String, Object> reqMap);
    
    /**
     * @param value
     * @return
     * 根据方案或者车次查询交路信息数量
     */
    public List getUnitCrossInfoCount(Object value);
    
    /**
     * @param value
     * @return
     * 根据逻辑交路信息数量
     */
    public List getUnitCrossTrainInfoCount(Object value);
    
    
    /**
     * @param cmCrossId
     * @return
     * 根据主键查询物理车次信息
     */
    TCmCross selectPhyCrossByCmCrossId(String cmCrossId);
    
    
    /**
     * @param value
     * @return
     * 删除交路
     */
    public int deleteCmUnitCrossInfoForCorssIds(Object value);
    
    /**
     * @param baseTrainId
     * @return
     * 列车时刻表
     */
    public List getTrainTimeInfoByTrainIdFromTrainlineJbt(String baseTrainId);
    /**
     * @param baseTrainId
     * @return  无分页面
     * 列车时刻表
     */
    public List getTrainTimeInfoByTrainIdFromTrainlineJbtNp(String baseTrainId);
    
    
    public List selectListBySql();
    public List getHomePageSum();
    public List getHomePageSumZtjl();
    public List getHomePageSumZtysc();
    public List getHomePageSumGljl();
    public List getHomePageSumGlysc();
}