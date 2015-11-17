package org.railway.com.trainplan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自动清理生成运行线时，PLAN_TRAIN_INFO_TEMP表中没能被正确删除的冗余数据
 * 
 * @author heyy
 * 
 */

@Component
@Transactional
@Monitored
public class AutoClearPlanTrainInfoTempService extends QuartzJobBean {

	private static Log logger = LogFactory.getLog(AutoClearPlanTrainInfoTempService.class
			.getName());

	private PlanTrainInfoTempService planTrainInfoTempService;
	private ApplicationContext applicationContext = null;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("RollingGenerationRP is begin!");
		
		//注入服务类
		 try {
			applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContextKey");
			planTrainInfoTempService = applicationContext.getBean("planTrainInfoTempService", PlanTrainInfoTempService.class);
		 } catch (SchedulerException e) {	
			e.printStackTrace();
		}
		 
		//执行程序
		 doWork();
		
	}

	private void doWork() {
		//获取系统当前时间并转换为相应格式
		
		//以这个日期为条件，删除一定天数的errorlog数据
	}

}
