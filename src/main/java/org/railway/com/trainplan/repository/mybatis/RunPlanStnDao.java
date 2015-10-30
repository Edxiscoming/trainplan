package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.RunPlanStn;

import java.util.List;
import java.util.Map;

/**
 * Created by speeder on 2014/5/28.
 */
 
public interface RunPlanStnDao {

    int addRunPlanStn(List<RunPlanStn> list);
    
    /**
     * 删除某个时间点之后的列车时刻
     * @param map 参数2
     */
    void deleteRunPlanStnByStartTime(Map<String, Object> map);
}
