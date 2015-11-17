package com.railway.basicmap.original.repository;

import java.util.List;
import java.util.Map;

import com.railway.basicmap.original.entity.MTrainlineTemp;


public interface MTrainlineTempMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(MTrainlineTemp record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(MTrainlineTemp record);

    /**
     * 根据主键查询记录
     */
    MTrainlineTemp selectByPrimaryKey(String id);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(MTrainlineTemp record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(MTrainlineTemp record);
    
    /**
     * 根据车次列表查询经由局列表
     * @Description: TODO
     * @param @param list
     * @param @return   
     * @return List<String>  
     * @throws
     * @author yuyangxing
     * @date 2015年9月26日
     */
    public List<String> queryViaBureauByTrainNames(List<Map<String, Object>> list);   
    
    
    /**
     * 查询所有基本图车次信息
     * @Description: TODO
     * @param @return   
     * @return List<MTrainlineTemp>  
     * @throws
     * @author yuyangxing
     * @date 2015年10月6日
     */
    public List<MTrainlineTemp> queryAllTrainList(Map<String, Object> map);

    /*
     * 
     */
    public List<MTrainlineTemp> queryVagueAllTrainList(Map<String, Object> map);
    /**
     * @param map
     * @return
     */
    public List<MTrainlineTemp> selectByParam(Map<String, Object> map);
}