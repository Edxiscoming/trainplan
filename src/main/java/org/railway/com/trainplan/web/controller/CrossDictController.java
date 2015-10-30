package org.railway.com.trainplan.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.service.CrossDictService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 交路图字典
 * @author denglj
 *
 */
@Controller
@RequestMapping(value = "/crossdict")
public class CrossDictController {
	private static final Logger logger = Logger.getLogger(CrossDictController.class);

	@Autowired
	private CrossDictService crossDictService;
    
	@RequestMapping(value = "mainpage", method = RequestMethod.GET)
	public String planReviewLines() {
		return "crossdict/cross_dict_main";
	}
	
	
	/**
	 * 获取车底交路字典信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossInfo", method = RequestMethod.POST)
	public Result getCrossInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result(); 
	    try{
	    	System.err.println("!!!!!! reqMap="+reqMap);
	    	
	    	QueryResult queryResult = crossDictService.getCrossDictInfo(reqMap); 
	    	PagingResult page = new PagingResult(queryResult.getTotal(), queryResult.getRows());
	    	result.setData(page);
	    }catch(Exception e){
			logger.error(e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		}
	
		return result;
	}
	
	
	
	
}
