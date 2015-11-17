package org.railway.com.trainplan.repository.mybatis;



import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/21/14.
 */
 
public interface RunLineDao {

    /**
     * 查询运行线时刻表信息
     * @param lineId 运行线id
     * @return 结果
     */
    List<Map<String, Object>> findLineTimeTableByLineId(String lineId);

    /**
     * 查询日计划主体信息
     * @param lineId 运行线id
     * @return
     */
    Map<String, Object> findLineInfoByLineId(String lineId);

  

    /**
     * 查询在客运计划在时间段内没有对应关系的运行线记录数
     * @param params startDate：开始日期，endDate结束日期
     * @return 结果
     */
    Map<String, Object> findUnknownRunLineCount(Map<String, Object> params);

   
}
