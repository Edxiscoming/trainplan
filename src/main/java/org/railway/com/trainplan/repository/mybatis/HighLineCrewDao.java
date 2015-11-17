package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.HighLineCrewInfo;

import java.util.List;
import java.util.Map;

/**
 * HighLinedao
 * Created by speeder on 2014/6/27.
 */
 
public interface HighLineCrewDao {
    HighLineCrewInfo findOne(Map<String, Object> map);

    List<HighLineCrewInfo> findList(Map<String, Object> map);
    
    List<HighLineCrewInfo> findListAll(Map<String, Object> map);

    void addCrew(HighLineCrewInfo crewHighlineInfo);

    void update(HighLineCrewInfo crewHighlineInfo);

    void delete(Map<String,String> reqMap);
    
    int getMaxSortId();
}
