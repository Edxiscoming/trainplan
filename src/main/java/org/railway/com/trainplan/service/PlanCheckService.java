package org.railway.com.trainplan.service;

import java.util.List;
import java.util.Map;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.PlanCheckDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * plan_check.
 * 
 * @author zhangPengDong
 *
 * 2015年5月16日 下午4:37:51
 */
@Component
@Transactional
@Monitored
public class PlanCheckService {

	@Autowired
	private PlanCheckDao planCheckDao;

	/**
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPlanCheckByMap(Map<String, Object> map){
		return planCheckDao.getPlanCheckByMap(map);
	}

	/**
	 * 根据PlanCrossId，得到当前交路中的各局最新审核通过不通过数据.
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getcheckStateByPlanCrossId(Map<String, Object> map){
		return planCheckDao.getcheckStateByPlanCrossId(map);
	}
	
}
