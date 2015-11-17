package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.UnitCross;
import org.railway.com.trainplan.entity.UnitCrossTrain;

import java.util.List;
import java.util.Map;

/**
 * UnitCross 操作类 Created by speeder on 2014/5/28.
 */
 
public interface UnitCrossDao {

	List<UnitCross> findUnitCrossBySchemaId(Map<String, Object> params);

	List<UnitCross> findUnitCrossByName(String unitCrossName);

	UnitCross findById(String unitCrossId);

	UnitCross findByName(String crossName);

	UnitCross getUnitCrossByCrossName(String crossName);

	Map<String, Object> getCountForUnitPlanCross(String unitCrossId);

	public int deleteUnitCrossByBaseCrossId(Map<String, Object> reqMap);

	public int deleteUnitCrossTrainByBaseCrossId(Map<String, Object> reqMap);
}
