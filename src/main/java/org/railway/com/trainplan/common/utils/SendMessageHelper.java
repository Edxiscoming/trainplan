package org.railway.com.trainplan.common.utils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendMessageHelper {
	//topic exchange一般不会改变
	private static final String EXCHANGE_NAME = "CREC-EVENT-EXCHANGE";
	//crec.event.是不能改变的，后面的可以使用自己的系统标示
	private static final String ROUTING_KEY = "crec.reply.createFreightTransportEvent";
	
	//一下是rabbitmq的配置以后可能会根据实际情况调整，可以把这些参数放到系统配置文件来加载
	private static final String RABBITMQ_HOST = "localhost";
	private static final String RABBITMQ_VHOST = "crec";
	private static final String RABBITMQ_USERNAME = "crec";
	private static final String RABBITMQ_PASSWORD = "crec2014";
	private static  Channel sender;  
	private static  Connection connection;
	
	static{ 
		try {
			ConnectionFactory factory = new ConnectionFactory();
			 factory.setHost(RABBITMQ_HOST);
	        factory.setVirtualHost(RABBITMQ_VHOST);
	        factory.setUsername(RABBITMQ_USERNAME);
	        factory.setPassword(RABBITMQ_PASSWORD);
       
			connection = factory.newConnection();
			sender = connection.createChannel();  
			sender.exchangeDeclare(EXCHANGE_NAME, "topic", true);
		} catch (IOException e) {
			//异常处理，考虑程序健壮性可以在这里设置重连等机制
			e.printStackTrace();
		}  
	}
	
	public static void sendMessage(String message) throws IOException{ 
		
		 sender.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes()); 
		
	}
	
	public static void bussnessMethod(List<Long> ids){ 
		JSONObject  json = new JSONObject();
		JSONObject head = new JSONObject();
		head.put("event", "constructionsEvent");
		head.put("requestId", UUID.randomUUID().toString());
		head.put("batch", 1);
		head.put("user", "test");
		json.put("head", head); 
		JSONArray  jsonArray = new JSONArray(); 
		//迭代所有的ID
		if(ids != null && ids.size() > 0){ 
			for(Long id : ids){
				JSONObject  temp = new JSONObject();
				temp.put("sourceEntityId", id); 
				temp.put("source", 4);
				temp.put("action", 1);
				jsonArray.add(temp); 
			}
		}
		
		
	
	}

	public static void main(String[] argv) throws Exception {

		 

		String routingKeyOne = "logs.error.one";// 定义一个路由名为“error”
		for (int i = 0; i <= 1; i++) {
			String messageOne = "this is one error logs:" + i;
			sendMessage(messageOne);
			System.out.println(" [x] Sent '" + routingKeyOne + "':'"
					+ messageOne + "'");
		}

		System.out.println("################################");
		String routingKeyTwo = "logs.error.two";
		for (int i = 0; i <= 2; i++) {
			String messageTwo = "this is two error logs:" + i;
			sendMessage(messageTwo);
			System.out.println(" [x] Sent '" + routingKeyTwo + "':'"
					+ messageTwo + "'");
		}

		System.out.println("################################");
		String routingKeyThree = "logs.info.one";
		for (int i = 0; i <= 3; i++) {
			String messageThree = "this is one info logs:" + i;
			sendMessage(messageThree);
			System.out.println(" [x] Sent '" + routingKeyThree + "':'"
					+ messageThree + "'");
		}

		sender.close();
		connection.close();
	}
}
