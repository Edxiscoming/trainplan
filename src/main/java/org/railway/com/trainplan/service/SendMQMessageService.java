package org.railway.com.trainplan.service;

import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.PlanTrainIDMap;
import org.railway.com.trainplan.entity.runline.CreateRunLineData;
import org.railway.com.trainplan.entity.runline.MessageBody;
import org.railway.com.trainplan.entity.runline.MqHead;
import org.railway.com.trainplan.entity.runline.ReceiveMsg;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Monitored
@Transactional
public class SendMQMessageService {
	private static Log logger = LogFactory.getLog(SendMQMessageService.class.getName());
	private String routingKey = "crec.event.trainlineEvent";
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private RunLineDataCheckService runLineDataCheckService;
	
	@Autowired
	private RunLineDataRollBackService runLineDataRollBackService;

	
	private String initData(CreateRunLineData createRunLineData, String requestId) {
		MqHead head = new MqHead();
		head.setBatch("0");
		head.setEvent("trainlineEvent");
		head.setUser(null);
		head.setRequestId(requestId);
		
		MessageBody messageBody = new MessageBody();
		
		messageBody.setHead(head);
		messageBody.setParam(createRunLineData);
		
		String jstr = JSONObject.fromObject(messageBody)
				.toString();
		
		return jstr;
	}
	
	
	//创建运行线接口
	public void sendMq(String msgReceiveUrl, CreateRunLineData createRunLineData, 
			String planTrainId, ShiroRealm.ShiroUser user) {
		
		String requestId = UUID.randomUUID().toString();
		String jstr = initData(createRunLineData, requestId);
		ReceiveMsg rMsg = new ReceiveMsg();
		rMsg.setDelete(false);
		rMsg.setMsgReceiveUrl(msgReceiveUrl);
		rMsg.setPlanTrainId(planTrainId);
		rMsg.setBureau(user.getBureau());
		rMsg.setCreatePeople(user.getName());
		rMsg.setCreateRunLineData(createRunLineData);
		PlanTrainIDMap.planTrainIDMap.put(requestId, rMsg);
		logger.info("json: " + jstr);
		
		amqpTemplate.convertAndSend(routingKey, jstr);
	}
	
	//调用删除接口之后，系统自动再调用创建接口
	public void sendMq(ReceiveMsg rMsg, String requestId) {
		//rMsg.getCreateRunLineData().getRunline().setId(UUID.randomUUID().toString());
		String jstr = initData(rMsg.getCreateRunLineData(), requestId);
		PlanTrainIDMap.planTrainIDMap.put(requestId, rMsg);
		logger.info("json: " + jstr);
		
		amqpTemplate.convertAndSend(routingKey, jstr);
	}
	
}
