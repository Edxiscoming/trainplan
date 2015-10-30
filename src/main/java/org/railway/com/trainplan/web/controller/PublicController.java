package org.railway.com.trainplan.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.OperationConstants;
import org.railway.com.trainplan.entity.DwrMessageData;
import org.railway.com.trainplan.entity.PlanLineHourCount;
import org.railway.com.trainplan.entity.PlanLineStat;
import org.railway.com.trainplan.repository.mybatis.PlanLineStatDao;
import org.railway.com.trainplan.service.HttpClientService;
import org.railway.com.trainplan.service.PlanLineStatService;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplanv2.dto.PublishParameters;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by star on 5/15/14. heyy
 * 公共接口，可以不通过用户权限验证，直接访问
 */
@RestController
@RequestMapping(value = "/public")
public class PublicController {

    private final static Log logger = LogFactory.getLog(PublicController.class);
    
    @Autowired
    private HttpClientService httpClientService;
    
    @Autowired
    private SendMsgService sendMsgService;
    
    @Autowired
	private AmqpTemplate amqpDwrTemplate;
    
    @Autowired
	private PlanLineStatDao planLineStatDao;
    
    @Autowired
    private PlanLineStatService planLineStatService;
    
    @Value("#{restConfig['dwrmsg.share.url']}")
    private String urls;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void testRest() throws Exception {
        logger.debug("come in just a test");
        PublishParameters publishParameters = new PublishParameters();
        publishParameters.setBureauShortName("京");
        
        String params = JSONObject.fromObject(publishParameters).toString();
        
        httpClientService.sendHttpClient("http://10.1.186.117:8080/rail/trainlines/trainline-12345-publish", params, OperationConstants.REQUEST_METHOD.POST);
    }
    
    @RequestMapping(value = "/share/dwrmsg", method = RequestMethod.POST)
    public void shareScriptSession(@RequestBody DwrMessageData dwrMessageData) {
        logger.debug("come in shareScriptSession" + dwrMessageData.getMessage());
        
        //收到消息内容后，调用SendMsgService，开始发消息
        //sendMsgService.sendMessage(dwrMessageData.getMessage(), dwrMessageData.getPageUrl(), dwrMessageData.getJsFuncName(), false);
    }
    
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public void testRest2() throws Exception {
        logger.debug("come in just a test2");
        DwrMessageData dwrMessageData = new DwrMessageData();
        dwrMessageData.setMessage("消息测试");
        
        String params = JSONObject.fromObject(dwrMessageData).toString();
        amqpDwrTemplate.convertAndSend(params);
//        List<String> a = StringUtil.getDwrmsgShareUrl(urls);
//        httpClientService.sendHttpClient("http://localhost:8088/trainplan/public/share/dwrmsg", params, OperationConstants.REQUEST_METHOD.POST);
    }
    
    @RequestMapping(value = "/test500", method = RequestMethod.GET)
    public ModelAndView test500(ModelAndView modelAndView) {
    	modelAndView.setViewName("error/500");
    	return  modelAndView;
    }
    
    
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String ckeck() {
    	return "1";
    }       
    @RequestMapping(value = "/check111", method = RequestMethod.GET)
    public String ckeck11() {
    	PlanLineStat planLineStat =  new PlanLineStat();
    	planLineStat.setFlag("v");
    	planLineStatDao.insertStat(planLineStat);
    	return "1";
    } 
    
    @RequestMapping(value = "/getPlanLineStat/{startDate}/{endDate}", method = RequestMethod.GET)
    public List<PlanLineHourCount> getPlanLineStat(@PathVariable("startDate") String startDate, 
    		@PathVariable("endDate") String endDate) {
    	Map<String, String> res = new HashMap<String, String>();
    	
    	startDate = startDate + " 06";
    	endDate = endDate + " 18";
    	
    	res.put("startDate", startDate);
    	res.put("endDate", endDate);
    	
    	List<PlanLineHourCount> planLineStatList = planLineStatDao.getHourCount(res);
    	
    	return planLineStatList;
    }  
    
    @RequestMapping(value = "/getPlanLineStat", method = RequestMethod.GET)
    public List<PlanLineHourCount> getPlanLineStatFor24H() {    	
    	List<PlanLineHourCount> planLineStatList = planLineStatDao.getHourCount();   	
    	return planLineStatList;
    }  
    
    @RequestMapping(value = "/getPlanLineStat/{type}", method = RequestMethod.GET)
    public List<PlanLineHourCount> getPlanLineStatFor24H(@PathVariable("type") int type) {
    	List<PlanLineHourCount> planLineStatList = null;
    	
    	
    	switch(type) {
    	//查询最近24小时
    	case 0 : planLineStatList = planLineStatDao.getHourCount();  
    		break;
    	//按班查询
    	case 1 : planLineStatList = planLineStatService.getBanPlanLineStat(); 
			break;
    	default : planLineStatList = planLineStatDao.getHourCount();
    		break;
    	}
    	
    	
    	return planLineStatList;
    } 
}
