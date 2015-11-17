package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.PlanSent;

/**
 * 记录落成计划.
 * 
 * @author Ad
 *
 */
 
public interface PlanSentDao {

	Integer insertPlanSent(Map<String, Object> map);

	List<PlanSent> getPlanSentByMap(Map<String, Object> map);
}
