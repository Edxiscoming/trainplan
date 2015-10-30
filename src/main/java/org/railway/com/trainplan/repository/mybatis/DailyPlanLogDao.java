package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.DailyPlanLog;

/**
 * 
 * @author heyy
 *生成运行线消息调用
 */
 
public interface DailyPlanLogDao {
	
/**
 * 插入日志信息
 * @param dailyPlanLog
 * @return
 */
	int insertDailyPlanLog(DailyPlanLog dailyPlanLog);
	
	int deleteDailyPlanLog(String dailyPlanLogId);
}
