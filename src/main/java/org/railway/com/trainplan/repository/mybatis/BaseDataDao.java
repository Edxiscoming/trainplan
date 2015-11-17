package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
 
public interface BaseDataDao {
    List<Map<String, Object>> getFJKDicByBureauCode(String bureauCode);

    List<Map<String, Object>> getBureauList();
}
