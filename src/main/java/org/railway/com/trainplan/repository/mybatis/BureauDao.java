package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
 
public interface BureauDao {
    List<Map<String, Object>> getBureauList();
    String getAllBureauShortName();
    String getShortBureauNameByCode(String bureauCode);
}
