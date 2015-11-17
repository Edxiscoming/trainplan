package org.railway.com.trainplan.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.IDandRequestId;
import org.railway.com.trainplan.entity.VaildPlanTrainTemp;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class CompletedRunlineService {
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private RunPlanService runPlanService;
	
	private ExecutorService executorService;
	
	public CompletedRunlineService(){		
		executorService = Executors.newFixedThreadPool(50);
	}
	
	//落成计划
	public void completedRunline(ShiroRealm.ShiroUser user, List<String> planTrainIdList) {
		//封装数据，插入VaildPlanTrainTemp表
		int total = planTrainIdList.size();
		String batchNumber = UUID.randomUUID().toString();
			
			for (String planTrainId : planTrainIdList)  { 
				executorService.execute(new CompleteTrainLine(planTrainId, batchNumber,
						total, user, 1));
			} 

	}
	
	//落成计划(参数校验)
		public void completedVaild(ShiroRealm.ShiroUser user, List<String> planTrainIdList) {
			//封装数据，插入VaildPlanTrainTemp表
			int total = planTrainIdList.size();
			String batchNumber = UUID.randomUUID().toString();
				
				for (String planTrainId : planTrainIdList)  { 
					executorService.execute(new CompleteTrainLine(planTrainId, batchNumber,
							total, user, 0));
				} 

		}
	
	 class CompleteTrainLine implements Runnable {

		 private String planTrainId;
		 private String batchNumber;
		 private int total;
		 private ShiroRealm.ShiroUser user;
		 private int type;
		 
		 public CompleteTrainLine(String planTrainId, String batchNumber, int total, ShiroRealm.ShiroUser user,
				 int type) {
			 this.planTrainId = planTrainId;
			 this.batchNumber = batchNumber;
			 this.total = total;
			 this.user = user;
			 this.type = type;
		 }
		@Override
		public void run() {
			String requestId = UUID.randomUUID().toString();
			VaildPlanTrainTemp vaildPlanTrainTemp = new VaildPlanTrainTemp();
			vaildPlanTrainTemp.setId(UUID.randomUUID().toString());
			vaildPlanTrainTemp.setRequestId(requestId);
			vaildPlanTrainTemp.setPlanTrainId(planTrainId);
			vaildPlanTrainTemp.setBatchNumber(batchNumber);
			vaildPlanTrainTemp.setBureau(user.getBureauShortName());
			vaildPlanTrainTemp.setCreatePeople(user.getUsername());
			vaildPlanTrainTemp.setUserPostName(user.getPostName());		
			vaildPlanTrainTemp.setMsgUrl("/trainplan/audit");
			vaildPlanTrainTemp.setTotal(total);
			
			runPlanService.insertVaildPlanTrainTemp(vaildPlanTrainTemp);
			
			IDandRequestId iDandRequestId = new IDandRequestId();
			iDandRequestId.setRunLineId(planTrainId);
			iDandRequestId.setRequestId(requestId);
			iDandRequestId.setType(type);
			
			String msg = JSONObject.fromObject(iDandRequestId)
					.toString();
			//发送消息，内容为iDandRequestId
			
			amqpTemplate.convertAndSend("crec.event.vaildEvent", msg);
			
		}
		 
	 }
}
