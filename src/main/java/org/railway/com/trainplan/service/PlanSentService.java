package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.PlanSent;
import org.railway.com.trainplan.repository.mybatis.PlanSentDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.railway.com.trainplan.service.ShiroRealm.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 记录落成计划.
 * 
 * @author Ad
 *
 */
@Component
@Transactional
@Monitored
public class PlanSentService {

	@Autowired
	private PlanSentDao planSentDao;

	@Autowired
	private RunPlanDao runPlanDao;

	/**
	 * 落成,记录日志.
	 * 
	 * @param user
	 * @param planTrainId
	 * @param sent_time
	 * @return
	 */
	public Integer insertPlanSent(ShiroUser user, String planTrainId,
			String sent_time) {
		Map<String, Object> planMap = runPlanDao
				.findPlanInfoByPlanId(planTrainId);
		if (null != planMap && !planMap.isEmpty()) {
			// 进行数据的组装.
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("plan_sent_id", UUID.randomUUID().toString());
			map.put("plan_cross_id", planMap.get("PLAN_CROSS_ID"));
			map.put("plan_train_id", planTrainId);
			map.put("cross_name", planMap.get("CROSS_NAME"));
			map.put("run_date", planMap.get("RUN_DATE"));
			map.put("train_nbr", planMap.get("TRAIN_NAME"));
			map.put("pre_train_id", planMap.get("PRE_TRAIN_ID"));
			map.put("next_train_id", planMap.get("NEXT_TRAIN_ID"));
			map.put("sent_time", sent_time);
			map.put("sent_people", user.getName());
			map.put("sent_people_org", user.getPostName());
			map.put("sent_people_bureau", user.getBureauShortName());

			return planSentDao.insertPlanSent(map);
		} else {
			return 0;
		}

	}

	/**
	 * 查询落成记录.
	 * 
	 * @param map
	 * @return
	 */
	public List<PlanSent> getPlanSentByMap(Map<String, Object> map){
		return planSentDao.getPlanSentByMap(map);
	}
	
}
