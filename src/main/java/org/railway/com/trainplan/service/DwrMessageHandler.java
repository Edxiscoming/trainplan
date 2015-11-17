package org.railway.com.trainplan.service;



import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.entity.DwrMessageData;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.rabbitmq.client.Channel;


public class DwrMessageHandler implements ChannelAwareMessageListener{

	private static Log logger = LogFactory.getLog(DwrMessageHandler.class.getName());
	
	@Autowired
	private SendMsgService sendMsgService;
	
//    @Value("#{restConfig['rabbitmq.isack']}")
//    private boolean isAck;//是否启用rabbitmq的ack功能
	
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		
        try {
        	JSONObject msg = null;
        	
            msg = JSONObject.fromObject(new String(message.getBody(),"utf-8"));
            logger.debug("msg=========" + msg);
            //消息事务结束，并被成功处理时触发
        	//TrainlineMsghandlerSync(msg);
            DwrMessageData dwrMessageData = (DwrMessageData) JSONObject.toBean(msg, DwrMessageData.class);
            sendMsgService.sendMessage(dwrMessageData.getMessage(), dwrMessageData.getPageUrl(), dwrMessageData.getJsFuncName());
/*        	if(isAck) {
	            try {
	                channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
	            }
	            catch(Exception e2)
	            {
	            	logger.error("处理ack出错:", e2);
	            }
        	}*/
        } catch (Exception e) {
        	logger.error("dwr分发接受消息错误:", e);
           /* //消息没有被成功处理时触发，消息会重新放入队列
        	if(isAck) {
	            try {
	                channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
	            }
	            catch(Exception e1)
	            {
	            	logger.error("dwr分发ack错误:", e);
	            }
        	}*/
            return;
        }				
	}

}
