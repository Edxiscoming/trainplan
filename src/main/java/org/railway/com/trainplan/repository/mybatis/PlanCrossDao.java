package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.PlanCrossInfo;

import java.util.List;
import java.util.Map;

/**
 * PlanCross操作类
 * Created by speeder on 2014/8/1.
 */
 
public interface PlanCrossDao {
    /**
     * 新增一条plancross记录
     * @param planCrossInfo 实体对象
     */
    void save(PlanCrossInfo planCrossInfo);

    /**
     * 根据plancrossid更新一条记录
     * @param planCrossInfo 实体对象
     */
    void update(PlanCrossInfo planCrossInfo);

    /**
     * 根据plancrossid删除一条记录
     * @param planCrossId 表主键
     */
    void deleteById(String planCrossId);

    /**
     * 根据unitcrossid查询一条记录，unitcross和plancross是一一对应的
     * @param unitCrossId 外键参数
     */
    PlanCrossInfo findByUnitCrossId(String unitCrossId);

    /**
     * 根据unitcrossid查询一条记录，unitcross和plancross是一一对应的
     * @param unitCrossName 交路名
     */
    List<PlanCrossInfo> findByUnitCrossName(Map<String,Object> map);

    /**
     * 根据参数集合查询一条记录，unitcross和plancross是一一对应的
     * 
     */
    List<PlanCrossInfo>findByParamMap(Map<String,Object> map);
    
    /**
     * 根据主键查询一条记录
     * @param planCrossId 表主键
     */
    PlanCrossInfo findById(String planCrossId);
    // Map<String, Object>
    String getCorssNameById(String planCrossId);
}
