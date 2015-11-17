package org.railway.com.trainplan.web.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.entity.Node;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class NodeController {
	private static Log logger = LogFactory.getLog(NodeController.class.getName());


	@Autowired
	private CommonService commonService ;

	/**
	 * 获取站名
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFullNodeInfo", method = RequestMethod.GET)
	public Result  getFullNodeInfo(){
		Result result  = new Result();
		try{
			List<Node> list = commonService.getFullNodeInfo();
			result.setData(list);
		}catch(Exception e){
	    	logger.error("getFullStationInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
	    }
		return result;
	}
	
	
}
