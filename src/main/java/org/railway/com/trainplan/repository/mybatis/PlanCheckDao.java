package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

/**
 * plan_check.
 * 
 * @author zhangPengDong
 *
 * 2015年5月16日 下午4:38:07
 */
 
public interface PlanCheckDao {

	List<Map<String, Object>> getPlanCheckByMap(Map<String, Object> map);
	List<Map<String, Object>> getcheckStateByPlanCrossId(Map<String, Object> map);
}
