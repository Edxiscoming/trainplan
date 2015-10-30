package org.railway.com.trainplan.web.controller.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.PlanTrainStnService;
import org.railway.com.trainplan.service.RunPlanLkService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.controller.RunPlanController;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/amqp")
public class AMQPTest {
	private static Log logger = LogFactory.getLog(AMQPTest.class.getName());

	 
	 @Autowired
	 private AmqpTemplate amqpTemplate;
	 
	 @RequestMapping(method = RequestMethod.GET)
	 public String sendMsg() {
		 logger.info("sendMsg");
		 amqpTemplate.convertAndSend("crec.event.trainplan","hello test!");
		 
		 return null;
    }
}
