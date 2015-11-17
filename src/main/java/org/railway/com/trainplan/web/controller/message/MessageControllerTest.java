package org.railway.com.trainplan.web.controller.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.service.RunLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by star on 5/12/14.
 */
@Controller
@RequestMapping(value = "/message")
public class MessageControllerTest {

    private final static Log logger = LogFactory.getLog(MessageControllerTest.class);

    @Autowired
    private SendMsgService sendMsgService;


    @RequestMapping(value = "receive", method = RequestMethod.GET)
    public String audit1() {
        return "message/message_receive";
    }
    
    
    @RequestMapping(value = "send", method = RequestMethod.GET)
    public String audit() {
        return "message/message_send";
    }
    
    
    
	@ResponseBody
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public void sendMessage(@RequestBody Map<String,String> reqMap){
		try {
			//System.err.println("!!!!"+reqMap);
			//System.err.println("!!!!"+reqMap.get("message"));
			//System.err.println("!!!!"+reqMap.get("pageUrl"));
			  sendMsgService.sendMessage(reqMap.get("message"), reqMap.get("pageUrl"), "showMessage");
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return;
	}

}
