package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.ModifyInfo;
import org.railway.com.trainplan.entity.ModifyPlanDTO;
import org.railway.com.trainplan.repository.mybatis.ModifyPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class ModifyPlanService {
	@Autowired
	private ModifyPlanDao modifyPlanDao;
	private static Log logger = LogFactory.getLog(ModifyPlanService.class
			.getName());

	public int addModifyList(List<ModifyInfo> modifys) {
		return modifyPlanDao.addModifyList(modifys);
	}

	public int updatePlanCheckHisFlag(String planCrossId, String checkHisFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planCrossId", planCrossId);
		map.put("checkHisFlag", checkHisFlag);
		return modifyPlanDao.updatePlanCheckHisFlag(map);
	}

	public int updatePlanCheckHisFlagCmd(String cmdTelId, String checkHisFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmdTelId", cmdTelId);
		map.put("checkHisFlag", checkHisFlag);
		return modifyPlanDao.updatePlanCheckHisFlagCmd(map);
	}

	public List<ModifyPlanDTO> getModifyRecordsByPlanCrossId(String planCrossId) {
		
		
		
		return modifyPlanDao.getModifyRecordsByPlanCrossId(planCrossId);
	}

	public List<ModifyPlanDTO> getModifyRecordsByPlanTrainId(String planTrainId) {
		return modifyPlanDao.getModifyRecordsByPlanTrainId(planTrainId);
	}

	public List<ModifyPlanDTO> getModifyRecordsByPlanTrainId1(
			Map<String, Object> map) {
		return modifyPlanDao.getModifyRecordsByPlanTrainId1(map);
	}

	public int updateTrainPlanSentBuearu(String planTrainId) {
		return modifyPlanDao.updateTrainPlanSentBuearu(planTrainId);
	}

	/**
	 * 批量插入.
	 * 
	 * @param modifys
	 * @return
	 */
	public int addModifyList1(Map<String, Object> map) {
		return modifyPlanDao.addModifyList1(map);
	}
}
