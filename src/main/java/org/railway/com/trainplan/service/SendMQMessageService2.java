package org.railway.com.trainplan.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.PlanTrainIDMap;
import org.railway.com.trainplan.entity.PlanLineStat;
import org.railway.com.trainplan.entity.PlanTrainInFoTemp;
import org.railway.com.trainplan.entity.runline.CreateRunLineData;
import org.railway.com.trainplan.entity.runline.MessageBody;
import org.railway.com.trainplan.entity.runline.MqHead;
import org.railway.com.trainplan.entity.runline.ReceiveMsg;
import org.railway.com.trainplan.repository.mybatis.PlanLineStatDao;
import org.railway.com.trainplanv2.dto.CreateRunLineData2;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Monitored
@Transactional
public class SendMQMessageService2 {
	private static Log logger = LogFactory.getLog(SendMQMessageService2.class.getName());
	private String routingKey = "crec.event.trainlineEvent";
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private RunLineDataCheckService runLineDataCheckService;
	
	@Autowired
	private RunLineDataRollBackService runLineDataRollBackService;
	
	@Autowired
	private PlanTrainInfoTempService planTrainInfoTempService;
	
    @Autowired
    private PlanLineStatDao planLineStatDao;
	
	
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
	private String initData(CreateRunLineData2 createRunLineData, String requestId) {
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
	
	private Message initMessage(CreateRunLineData2 createRunLineData, String requestId) throws UnsupportedEncodingException {
		MessageProperties mp = new MessageProperties();
		mp.setHeader("event", "trainlineEvent");
		mp.setHeader("batch", "0");
		mp.setHeader("requestId", requestId);
		
		String jstr = JSONObject.fromObject(createRunLineData)
				.toString();
	
		Message msg = new Message(jstr.getBytes("UTF-8"), mp);

		return msg;
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
	//创建运行线接口
	public void sendMq2(String msgReceiveUrl, CreateRunLineData2 createRunLineData, 
			String planTrainId, ShiroRealm.ShiroUser user) {
		
		String requestId = UUID.randomUUID().toString();
		
		//logger.debug(jstr);
//		ReceiveMsg2 rMsg = new ReceiveMsg2();
//		rMsg.setDelete(false);
//		rMsg.setMsgReceiveUrl(msgReceiveUrl);
//		rMsg.setPlanTrainId(planTrainId);
//		rMsg.setBureau(user.getBureau());
//		rMsg.setCreatePeople(user.getName());
//		rMsg.setCreateRunLineData(createRunLineData);
		//PlanTrainIDMap2.planTrainIDMap.put(requestId, rMsg);
		
		
		//保存数据到PLAN_TRAIN_INFO_TEMP表中
		
		Message msg = null;
		
		try {	
			msg = initMessage(createRunLineData, requestId);
			PlanTrainInFoTemp  planTrainInFoTemp = new PlanTrainInFoTemp();
			planTrainInFoTemp.setId(UUID.randomUUID().toString());
			planTrainInFoTemp.setCreateTime(new Date());
			planTrainInFoTemp.setMsgUrl(msgReceiveUrl);
			planTrainInFoTemp.setPlanTrainId(planTrainId);
			planTrainInFoTemp.setRequestId(requestId);
			planTrainInFoTemp.setBureau(user.getBureau());
			planTrainInFoTemp.setCreatPeople(user.getName());
			planTrainInfoTempService.insertPlanTrainInFoTemp(planTrainInFoTemp);
			//统计
			PlanLineStat planLineStat = new PlanLineStat();
			planLineStat.setFlag("0");
			planLineStatDao.insertStat(planLineStat);
			//发送消息到amqp
			amqpTemplate.send(routingKey, msg);		
		} catch (AmqpException e) {
			logger.error("amqp 消息发送错误 ： " + e);
			amqpTemplate.send(routingKey, msg);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			logger.error("amqp 消息发送转码错误 ： " + e);
			amqpTemplate.send(routingKey, msg);
			e.printStackTrace();
		}
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
