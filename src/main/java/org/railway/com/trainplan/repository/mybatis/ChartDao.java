package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.service.dto.PlanTrainDTO2;

/**
 * 审核页面统计
 * Created by speeder on 2014/5/27.
 */
 
public interface ChartDao {

    Map<String, Object> getPlanTypeCount(Map<String, Object> map);

    Map<String, Object> getPlanLineCount(Map<String, Object> map);

    Map<String, Object> getLev1CheckCount(Map<String, Object> map);

    Map<String, Object> getLev2CheckCount(Map<String, Object> map);
    List<PlanTrainDTO2> getChatCount(Map<String, Object> map);
    List<PlanTrainDTO2> getChatCountGT(Map<String, Object> map);
}
