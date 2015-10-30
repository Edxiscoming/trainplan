package org.railway.com.trainplan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;







import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.DailyPlanLog;
import org.railway.com.trainplan.entity.DwrMessageData;
import org.railway.com.trainplan.entity.PlanTrainInFoTemp;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.rabbitmq.client.Channel;


public class MessageHandler implements ChannelAwareMessageListener{

	private static Log logger = LogFactory.getLog(MessageHandler.class.getName());
	@Autowired
	private SendMsgService sendMsgService;
	@Autowired
	private PlanTrainStnService planTrainStnService;
	@Autowired
	private SendMQMessageService sendMQMessageService;
	@Autowired
	private DailyPlanLogService dailyPlanLogService;
	
	@Autowired
	private RunLineDataCheckService runLineDataCheckService;
	
	@Autowired
	private RunLineDataRollBackService runLineDataRollBackService;
	
	@Autowired
	private MessageHandlerThreadPool messageHandlerThreadPool;
	
	@Autowired
	private PlanTrainInfoTempService planTrainInfoTempService;
	
    @Value("#{restConfig['rabbitmq.isack']}")
    private boolean isAck;//是否启用rabbitmq的ack功能
	
    @Value("#{restConfig['dwrmsg.isshare']}")
    private boolean configIsShare;//是否使用mq来执行dwr消息分发
    
//	private boolean isCheck = true;
//	private boolean isRollBack = true;
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		
        try {
        	JSONObject msg = null;
            msg = JSONObject.fromObject(new String(message.getBody(),"utf-8"));
        	//msg = JSONObject.fromObject(new String(message.getBody()));
            //调用线程处理任务
        	//messageHandlerThreadPool.getExecutorService().execute(new TrainlineMsghandler(msg));
            //消息事务结束，并被成功处理时触发
        	TrainlineMsghandlerSync(msg);
        	if(isAck) {
	            try {
	                channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
	            }
	            catch(Exception e2)
	            {
	            	logger.error("处理ack出错:", e2);
	            }
        	}
        } catch (Exception e) {
        	logger.error("接受消息错误:", e);
            //消息没有被成功处理时触发，消息会重新放入队列
        	if(isAck) {
	            try {
	                channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
	            }
	            catch(Exception e1)
	            {
	            	logger.error("接受消息错误:", e);
	            }
        	}
            return;
        }				
	}
	
	private DailyPlanLog getDailyPlanLog(String errorLog, String requestId,String bureau,String creatPeople,String planTrainId) {
		DailyPlanLog dailyPlanLog = new DailyPlanLog();
		dailyPlanLog.setCreateTime(DateUtil.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
//		dailyPlanLog.setBureau(PlanTrainIDMap.planTrainIDMap.get(requestId).getBureau());
//		dailyPlanLog.setCreatePeople(PlanTrainIDMap.planTrainIDMap.get(requestId).getCreatePeople());
//		dailyPlanLog.setDailyPlanId(PlanTrainIDMap.planTrainIDMap.get(requestId).getCreateRunLineData().getRunline().getId());
		dailyPlanLog.setId(UUID.randomUUID().toString());
		dailyPlanLog.setLog(errorLog);
//		dailyPlanLog.setPlanTrainId(PlanTrainIDMap.planTrainIDMap.get(requestId).getPlanTrainId());
		dailyPlanLog.setRequestId(requestId);
		dailyPlanLog.setBureau(bureau);
		dailyPlanLog.setCreatePeople(creatPeople);
		dailyPlanLog.setPlanTrainId(planTrainId);
		dailyPlanLog.setDailyPlanId(planTrainId);
		return dailyPlanLog;
		
	}
		 
	 //新结构方法
//	 class TrainlineMsghandler implements Runnable {
//			private JSONObject msg;
//			
//			TrainlineMsghandler(JSONObject msg) {
//				this.msg = msg;
//			}
//			@Override
//			public void run() {			
//				 String planTrainId = null;
//				 String msgReceiveUrl = null;
//				 String bureau = null;
//				 String creatPeople = null;
//				 logger.debug("msg======" + msg);
//				 
//				 if(msg != null ){ 			 
//					 JSONObject result = msg.getJSONObject("result");
//					 String requestId = (String) msg.get("reuqestId");
//					 String code = (String) msg.get("code");
//					 if(result != null){					 			
//						 PlanTrainInFoTemp  pt = null;
//						 PlanTrainInFoTemp ptit = new PlanTrainInFoTemp();
//						 ptit.setRequestId(requestId);
//						 List<PlanTrainInFoTemp> list = planTrainInfoTempService.getPlanTrainInFoTemp(ptit);
//						 if(list!=null && list.size()==1) {
//							 pt = list.get(0);
//		//					 planTrainId = PlanTrainIDMap2.planTrainIDMap.get(requestId).getPlanTrainId();
//							 planTrainId = pt.getPlanTrainId();
//		//					 msgReceiveUrl = PlanTrainIDMap2.planTrainIDMap.get(requestId).getMsgReceiveUrl();
//							 msgReceiveUrl = pt.getMsgUrl();
//							 bureau = pt.getBureau();
//							 creatPeople = pt.getCreatPeople();
//								
//								 
//							 //成功
//							 if("0".equals(code) && "true".equals(String.valueOf(result.get("success")))){
//								 Map<String,Object> reqMap = new HashMap<String,Object>();
//								 String daylyPlanId = StringUtil.objToStr(result.get("id"));
//								 reqMap.put("planTrainId", planTrainId);
//								 reqMap.put("daylyPlanId",daylyPlanId );
//								 //更新表plan_train中字段DAILYPLAN_FLAG值为0
//								 System.out.println("==================" + reqMap);
//								 try {
//									 planTrainStnService.updatePlanTrainDaylyPlanFlag(reqMap);
//									 JSONObject jsonMsg = new JSONObject();
//									 jsonMsg.put("planTrainId", planTrainId);
//									 jsonMsg.put("createFlag", 1);
//									 sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl, "updateTrainRunPlanStatus");
//								} catch (Exception e) {
//									 String errorContent = "更新开行计划生成运行线状态异常>>>>>>>>>>>>>>>>>>>planTrainId="+planTrainId;
//									 logger.error(errorContent);
//									 //记录错误消息
//									 DailyPlanLog dailyPlanLog = getDailyPlanLog(errorContent, requestId,bureau,creatPeople,planTrainId);
//									 dailyPlanLogService.addDailyPlanLog(dailyPlanLog);
//									 JSONObject jsonMsg = new JSONObject();
//									 jsonMsg.put("planTrainId", planTrainId);
//									 jsonMsg.put("createFlag", 0);
//									 sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl, "updateTrainRunPlanStatus");
//									 e.printStackTrace();
//								}									 									 
//			
//							 }else{
//								   String errorContent = "生成运行线失败" + StringUtil.objToStr(result.get("error"));
//								   errorContent = errorContent + "#####message" + StringUtil.objToStr((String)result.get("message"));
//								   //记录错误消息
//								   DailyPlanLog dailyPlanLog = getDailyPlanLog(errorContent, requestId,bureau,creatPeople,planTrainId);
//								   dailyPlanLogService.addDailyPlanLog(dailyPlanLog);						   
//								   JSONObject jsonMsg = new JSONObject();
//								   jsonMsg.put("planTrainId", planTrainId);
//								   jsonMsg.put("createFlag", 0);
//								   sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl, "updateTrainRunPlanStatus");
//							 }
//							 
//							 //无论这次调用成功还是失败，这条数据已经失效，需要删除
//							 //PlanTrainIDMap2.planTrainIDMap.remove(requestId);
//							 //根据requestId删除PLAN_TRAIN_INFO_TEMP
//							 try{
//								 
//								 if(requestId!=null){
//									 planTrainInfoTempService.deletePlanTrainInfoTempByRequestId(requestId);
//									 logger.debug("根据requestid删除成功>>>>>>>>>>>>>>>>>>>requestId="+requestId);
//								 }
//								
//							 }catch(Exception e){
//								 logger.error("根据requestid删除错误>>>>>>>>>>>>>>>>>>>requestId="+requestId);
//								 e.printStackTrace();
//							 }								 				
//						 }else if(list == null || list.size() > 1 || list.size() == 0){
//							 
//							   String errorContent = "生成运行线失败" + StringUtil.objToStr(result.get("error"));
//							   errorContent = errorContent + "#####message" + StringUtil.objToStr((String)result.get("message"));
//							   //记录错误消息
//							   DailyPlanLog dailyPlanLog = getDailyPlanLog(errorContent, requestId,bureau,creatPeople,planTrainId);
//							   dailyPlanLogService.addDailyPlanLog(dailyPlanLog);
//							   
//							   JSONObject jsonMsg = new JSONObject();
//							   jsonMsg.put("planTrainId", planTrainId);
//							   jsonMsg.put("createFlag", 0);
//							   sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl, "updateTrainRunPlanStatus");
//							 
//							 try{
//								 
//								 if(requestId!=null){
//									 planTrainInfoTempService.deletePlanTrainInfoTempByRequestId(requestId);
//									 logger.debug("根据requestid删除成功>>>>>>>>>>>>>>>>>>>requestId="+requestId);
//								 }
//								 
//							 }catch(Exception e){
//								 logger.error("根据requestid删除错误>>>>>>>>>>>>>>>>>>>requestId="+requestId);
//								 e.printStackTrace();
//							 }
//							 
//						 }
//					 }	 
//				 }
//			}
//		 }
	 
	 //生成运行线消息处理，使用同步方法
	 private void TrainlineMsghandlerSync(JSONObject msg) {
			
		 String planTrainId = null;
		 String msgReceiveUrl = null;
		 String bureau = null;
		 String creatPeople = null;
		 logger.debug("msg======" + msg);
		 
		 if(msg != null ){ 			 
			 JSONObject result = msg.getJSONObject("result");
			 String requestId = (String) msg.get("reuqestId");
			 String code = (String) msg.get("code");
			 if(result != null){					 			
				 PlanTrainInFoTemp  pt = null;
				 PlanTrainInFoTemp ptit = new PlanTrainInFoTemp();
				 ptit.setRequestId(requestId);
				 List<PlanTrainInFoTemp> list = planTrainInfoTempService.getPlanTrainInFoTemp(ptit);
				 if(list!=null && list.size()==1) {
					 pt = list.get(0);
//					 planTrainId = PlanTrainIDMap2.planTrainIDMap.get(requestId).getPlanTrainId();
					 planTrainId = pt.getPlanTrainId();
//					 msgReceiveUrl = PlanTrainIDMap2.planTrainIDMap.get(requestId).getMsgReceiveUrl();
					 msgReceiveUrl = pt.getMsgUrl();
					 bureau = pt.getBureau();
					 creatPeople = pt.getCreatPeople();
						
						 
					 //成功
					 if("0".equals(code) && "true".equals(String.valueOf(result.get("success")))){
						 Map<String,Object> reqMap = new HashMap<String,Object>();
						 String daylyPlanId = StringUtil.objToStr(result.get("id"));
						 reqMap.put("planTrainId", planTrainId);
						 reqMap.put("daylyPlanId",daylyPlanId );
						 //更新表plan_train中字段DAILYPLAN_FLAG值为0
						 //System.out.println("==================" + reqMap);
						 try {							
							 updateDateAndSendMsg(planTrainId, msgReceiveUrl, reqMap);							
						} catch (Exception e) {
							 String errorContent = "更新开行计划生成运行线状态异常>>>>>>>>>>>>>>>>>>>planTrainId="+planTrainId;
							 logger.error(errorContent);
							 //记录错误消息
							 DailyPlanLog dailyPlanLog = getDailyPlanLog(errorContent, requestId,bureau,creatPeople,planTrainId);
							 updateDateAndSendMsgError(planTrainId, msgReceiveUrl, dailyPlanLog);
							 e.printStackTrace();
						}									 									 
	
					 }else{
						   String errorContent = "生成运行线失败,error: " + StringUtil.objToStr(result.get("error"));
						   //errorContent = errorContent + "#####message" + StringUtil.objToStr((String)result.get("message"));
						   //记录错误消息
						   DailyPlanLog dailyPlanLog = getDailyPlanLog(errorContent, requestId,bureau,creatPeople,planTrainId);
						   updateDateAndSendMsgError(planTrainId, msgReceiveUrl, dailyPlanLog);
					 }
					 
					 //无论这次调用成功还是失败，这条数据已经失效，需要删除
					 //PlanTrainIDMap2.planTrainIDMap.remove(requestId);
					 //根据requestId删除PLAN_TRAIN_INFO_TEMP
					 try{
						 
						 if(requestId!=null){
							 planTrainInfoTempService.deletePlanTrainInfoTempByRequestId(requestId);
							 logger.debug("根据requestid删除成功>>>>>>>>>>>>>>>>>>>requestId="+requestId);
						 }
						
					 }catch(Exception e){
						 logger.error("根据requestid删除错误>>>>>>>>>>>>>>>>>>>requestId="+requestId);
						 e.printStackTrace();
					 }								 				
				 }else if(list == null || list.size() > 1 || list.size() == 0){
					 
					   String errorContent = "生成运行线失败,temp数据错误,error: " + StringUtil.objToStr(result.get("error"));
					   //errorContent = errorContent + "#####message" + StringUtil.objToStr((String)result.get("message"));
					   //记录错误消息
					   DailyPlanLog dailyPlanLog = getDailyPlanLog(errorContent, requestId,bureau,creatPeople,planTrainId);
					   updateDateAndSendMsgError(planTrainId, msgReceiveUrl, dailyPlanLog);
					 
					 try{
						 
						 if(requestId!=null){
							 planTrainInfoTempService.deletePlanTrainInfoTempByRequestId(requestId);
							 logger.debug("根据requestid删除成功>>>>>>>>>>>>>>>>>>>requestId="+requestId);
						 }
						 
					 }catch(Exception e){
						 logger.error("根据requestid删除错误>>>>>>>>>>>>>>>>>>>requestId="+requestId);
						 e.printStackTrace();
					 }
					 
				 }
			 }	 
		 }
	
	 }
	 
	 private void updateDateAndSendMsg(String planTrainId, String msgReceiveUrl, Map<String,Object> reqMap) throws Exception {
		 if(configIsShare) {
			 JSONObject jsonMsg = new JSONObject();
			 jsonMsg.put("planTrainId", planTrainId);
			 jsonMsg.put("createFlag", 1);
			 DwrMessageData dwrMessageData = new DwrMessageData();
			 dwrMessageData.setJsFuncName("updateTrainRunPlanStatus");
			 dwrMessageData.setMessage(jsonMsg.toString());
			 dwrMessageData.setPageUrl(msgReceiveUrl);
			 planTrainStnService.updatePlanTrainDaylyPlanFlagSendMessage(reqMap, dwrMessageData);
		 }
		 else {
			 planTrainStnService.updatePlanTrainDaylyPlanFlag(reqMap);
			 JSONObject jsonMsg = new JSONObject();
			 jsonMsg.put("planTrainId", planTrainId);
			 jsonMsg.put("createFlag", 1);
			 sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl, "updateTrainRunPlanStatus");
		 }
	 }
	 
	 private void updateDateAndSendMsgError(String planTrainId, String msgReceiveUrl, DailyPlanLog dailyPlanLog) {
		 if(configIsShare) {
			 JSONObject jsonMsg = new JSONObject();
			 jsonMsg.put("planTrainId", planTrainId);
			 jsonMsg.put("createFlag", 0);
			 DwrMessageData dwrMessageData = new DwrMessageData();
			 dwrMessageData.setJsFuncName("updateTrainRunPlanStatus");
			 dwrMessageData.setMessage(jsonMsg.toString());
			 dwrMessageData.setPageUrl(msgReceiveUrl);
			 dailyPlanLogService.addDailyPlanLogSendMessage(dailyPlanLog, dwrMessageData);
			 
		 }
		 else {	
			 dailyPlanLogService.addDailyPlanLog(dailyPlanLog);
			 JSONObject jsonMsg = new JSONObject();
			 jsonMsg.put("planTrainId", planTrainId);
			 jsonMsg.put("createFlag", 0);
			 sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl, "updateTrainRunPlanStatus");
		 }
	 }
}
