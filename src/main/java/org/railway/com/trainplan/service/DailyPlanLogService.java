package org.railway.com.trainplan.service;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.DailyPlanLog;
import org.railway.com.trainplan.entity.DwrMessageData;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.DailyPlanLogDao;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author heyy
 *生成运行线消息调用日志记录
 */
@Component
@Monitored
@Transactional
public class DailyPlanLogService {

	  private final static Log logger = LogFactory.getLog(DailyPlanLogService.class);
	  
	  @Autowired
	  private DailyPlanLogDao dailyPlanLogDao;
	  
	  @Autowired
	  private BaseDao baseDao;
	  
	  @Autowired
	  private AmqpTemplate amqpDwrTemplate;
	  
	  public int addDailyPlanLog(DailyPlanLog dailyPlanLog) {
		  return dailyPlanLogDao.insertDailyPlanLog(dailyPlanLog);
	  }
	  
	  public int addDailyPlanLogSendMessage(DailyPlanLog dailyPlanLog, DwrMessageData dwrMessageData) {
		  int count = dailyPlanLogDao.insertDailyPlanLog(dailyPlanLog);
		  String params = JSONObject.fromObject(dwrMessageData).toString();
	      amqpDwrTemplate.convertAndSend(params);  	    	
	      return count;
	  }
	  
	  public int deleteDailyPlanLogByDate(String date){
		  return baseDao.deleteBySql("", date);
	  }
	  
}
