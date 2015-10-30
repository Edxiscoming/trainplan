package org.railway.com.trainplan.repository.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.TrainActivityPeriod;
 
public interface CrossMybatisDao {

	CrossInfo get(String id); 

	List<CrossInfo> search(Map<String, Object> parameters);

	void saveCrossBeach(List<CrossInfo> crosses);

	void delete(String id);
	
	void deleteBach(String ids);

	void updateCrossPlanDayGap(HashMap<String, Object> map);
	
	Map<String, Object> getPeriodTimdByCrossTrainId(String crossTrainId);
	
	List<TrainActivityPeriod> searchPeriod(Map<String,Object> reqMap);

	Map<String, Object> getBaseCrossById(String base_cross_train_id);

	Integer updateBaseCrossById(Map<String, Object> map);

	List<Map<String, Object>> getUnitCrossTrainByMap(Map<String, Object> map);

	Integer updateUnitCrossByMap(Map<String, Object> map);
}

