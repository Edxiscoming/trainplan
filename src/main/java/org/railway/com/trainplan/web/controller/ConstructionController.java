package org.railway.com.trainplan.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.service.ConstructionService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/construction")
public class ConstructionController {
	private static Log logger = LogFactory.getLog(ConstructionController.class.getName());
	
	
	@Autowired
	private ConstructionService constructionService;
	/**
	 * 统计路局运行车次信息
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryConstructionDetail", method = RequestMethod.POST)
	public Result getFullStationTrains(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			//System.err.println("queryConstructionDetail~~reqMap="+reqMap);
			 
			String trainNbr="";
			if(reqMap.get("trainNbr") !=null || !"".equals(reqMap.get("trainNbr").toString())) {
				trainNbr = reqMap.get("trainNbr").toString();
			}
//			//路局全称
//			String startBureauFull = StringUtil.objToStr(reqMap.get("startBureauFull"));
			//调用后台接口
			String string = constructionService.getConstructions(trainNbr);
			Map map = new HashedMap();
			map.put("result", string);
			result.setData(map);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/createConstructionPlain", method = RequestMethod.POST)
	public Result createConstructionPlain(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			//System.err.println("createConstructionPlain~~reqMap="+reqMap);
			String runDate="";
			if(reqMap.get("runDate") ==null || "".equals(reqMap.get("runDate").toString())) {
				runDate = DateUtil.format(new Date(), "yyyy-MM-dd");
				result.setCode("-1");
				result.setMessage("请填写日期");
				return result;
			} else {
				runDate = DateUtil.format(DateUtil.parse(StringUtil.objToStr(reqMap.get("runDate"))), "yyyy-MM-dd");
			}
			
			String trainNbr="";
			if(reqMap.get("trainNbr") !=null || !"".equals(reqMap.get("trainNbr").toString())) {
				trainNbr = reqMap.get("trainNbr").toString(); 
			}else{
				result.setCode("-1");
				result.setMessage("请填写客运班次");
				return result;
			}
//			//路局全称
//			String startBureauFull = StringUtil.objToStr(reqMap.get("startBureauFull"));
			//调用后台接口
			String resultData = constructionService.createConstructionPlain(trainNbr, runDate);
			Map map = new HashedMap();
			map.put("result", resultData);
			result.setData(map);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryTrainForBureau", method = RequestMethod.POST)
	public Result queryTrainForBureau(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			//System.err.println("queryTrainForBureau~~reqMap="+reqMap);
			String runDate="";
			if(reqMap.get("runDate") ==null || "".equals(reqMap.get("runDate").toString())) {
				runDate = DateUtil.format(new Date(), "yyyy-MM-dd");
				result.setCode("-1");
				result.setMessage("请填写日期");
				return result;
			} else {
				runDate = DateUtil.format(DateUtil.parse(StringUtil.objToStr(reqMap.get("runDate"))), "yyyy-MM-dd");
			}
			
			String bureauNrm="";
			if(reqMap.get("bureauNrm") !=null || !"".equals(reqMap.get("bureauNrm").toString())) {
				bureauNrm = reqMap.get("bureauNrm").toString(); 
			}else{
				result.setCode("-1");
				result.setMessage("请选择路局");
				return result;
			}
			
			int currentPage= 0;
			if(reqMap.get("currentPage") !=null || !"".equals(reqMap.get("currentPage").toString())) {
				currentPage = Integer.valueOf(reqMap.get("currentPage").toString()); 
			}else{
				result.setCode("-1");
				result.setMessage("请选择路局");
				return result;
			}
			
			int pageSize= 20;
			if(reqMap.get("pageSize") !=null || !"".equals(reqMap.get("pageSize").toString())) {
				pageSize = Integer.valueOf(reqMap.get("pageSize").toString()); 
			}else{
				result.setCode("-1");
				result.setMessage("请选择路局");
				return result;
			}
			
			//currentPage
			
//			//路局全称
//			String startBureauFull = StringUtil.objToStr(reqMap.get("startBureauFull"));
			//调用后台接口
			String resultData = constructionService.queryTrainForBureau(bureauNrm, runDate, currentPage, pageSize);
			Map map = new HashedMap();
			map.put("result", resultData);
			result.setData(map);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
}
