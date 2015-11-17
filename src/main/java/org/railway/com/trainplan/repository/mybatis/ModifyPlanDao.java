package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.ModifyInfo;
import org.railway.com.trainplan.entity.ModifyPlanDTO;

 
public interface ModifyPlanDao {
	int addModifyList(List<ModifyInfo> modifys);

	int updatePlanCheckHisFlag(Map<String, Object> map);

	int updatePlanCheckHisFlagCmd(Map<String, Object> map);

	List<ModifyPlanDTO> getModifyRecordsByPlanCrossId(String planCrossId);

	List<ModifyPlanDTO> getModifyRecordsByPlanTrainId(String planTrainId);

	List<ModifyPlanDTO> getModifyRecordsByPlanTrainId1(Map<String, Object> map);

	int updateTrainPlanSentBuearu(String planTrainId);

	/**
	 * 批量插入.
	 * 
	 * @param map
	 * @return
	 */
	int addModifyList1(Map<String, Object> map);
}
