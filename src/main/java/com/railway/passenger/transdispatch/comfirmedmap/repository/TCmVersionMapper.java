package com.railway.passenger.transdispatch.comfirmedmap.repository;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;

public interface TCmVersionMapper {
    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(String cmVersionId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCmVersion record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCmVersion record);

    /**
     * 根据主键查询记录
     */
    TCmVersion selectByPrimaryKey(String cmVersionId);
    
    /*
     * 根据主键模糊查询
     */
    TCmVersion selectVagueByPrimaryKey(String cmVersionId);
    
    /**
     * 
     * @Description: 根据基本图方案ID查找
     * @param @param mTemplateScheme
     * @param @return   
     * @return TCmVersion  
     * @throws
     * @author qs
     * @date 2015年10月6日
     */
    TCmVersion selectByMTemplateScheme(String mTemplateScheme);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCmVersion record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCmVersion record);
    
    /**
     * 查询所有版本号信息
     * @Description: TODO
     * @param @return   
     * @return List<TCmVersion>  
     * @throws
     * @author yuyangxing
     * @date 2015年9月25日
     */
    List<TCmVersion> queryList();
}