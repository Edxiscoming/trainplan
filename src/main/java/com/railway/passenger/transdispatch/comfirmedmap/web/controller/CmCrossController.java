package com.railway.passenger.transdispatch.comfirmedmap.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.TrainTimeInfoJbt;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.railway.passenger.transdispatch.comfirmedmap.entity.Ljzd;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCrossAndOriginaCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmCrossServiceImpl;
import com.railway.common.entity.Result;

@Controller
@RequestMapping(value = "/cmcross")
public class CmCrossController {
	private static Log logger = LogFactory.getLog(CmCrossController.class.getName());
	
	@Autowired
	private CmCrossServiceImpl  cmCrossService;
	
	
	@RequestMapping(value = "/pageIndex/{index}", method = RequestMethod.GET)
	public String pageIndex(@PathVariable("index") String index) {
		String url = "";
		switch(index){
			case "check" : url="/logTable/examineCross";break;
			case "query" : url="/logTable/searchCross";break;
			case "jbsj_home_page" : url="/logTable/jbsj_home_page";break;
			default : url = "";
		}
		return url;
	}
	
	/**
	 * @param reqMap
	 * @return
	 * 查询交路信息列表
	 */
	@ResponseBody 
	@RequestMapping(value = "/getCmUnitCrossInfo", method = RequestMethod.POST)
	public Result getCmUnitCrossInfo(HttpServletRequest request,HttpServletResponse response){
		List<TCmCrossAndOriginaCross> list = null;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String chartId = request.getParameter("chartId");
		String trainNbr = request.getParameter("trainNbr");
		String selfRelevant = request.getParameter("selfRelevant");
		String input_cross_chart_id = request.getParameter("input_cross_chart_id");
		String highlineFlag = request.getParameter("highlineFlag");
		String pageIndex = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		dataMap.put("trainNbr", trainNbr);
		dataMap.put("chartId", chartId);
		dataMap.put("selfRelevant", selfRelevant);
		dataMap.put("input_cross_chart_id", input_cross_chart_id);
		dataMap.put("highlineFlag", highlineFlag);
		dataMap.put("pageIndex", pageIndex);
		dataMap.put("pageSize", pageSize);
		
		Result result = cmCrossService.getCmUnitCrossInfo(dataMap);
		return result;
	}
	@ResponseBody 
	@RequestMapping(value = "/queryHomePage", method = RequestMethod.POST)
	public Map<String,Object> queryHomePage(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		List list=cmCrossService.queryHomePage();
		map.put("list", list);
		return map;
	}
	
	
	/**
	 * 交路详情查询
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCmUnitCrossTrainInfoDetail", method = RequestMethod.POST)
	public Result getCmUnitCrossTrainInfoDetail(@RequestBody Map<String, Object> reqMap){
		Result result = new Result();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		String cmCrossId = StringUtil.objToStr(reqMap.get("crossId"));
		
		dataMap.put("cmCrossId", cmCrossId);
		TCmOriginalCross ocross = cmCrossService.getOriginalCrossInfoByCmCrossId(cmCrossId);
		
		dataMap.put("oCrossinfo", ocross);
		dataList.add(dataMap);
		result.setData(dataList);
		return result;			
	}
	
	/**
	 * 逻辑交路列表（车次）
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCmUnitCrossTrainInfo", method = RequestMethod.POST)
	public Result getCmUnitCrossTrainInfo(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		String cmCrossId = request.getParameter("crossId");
		String pageIndex = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		
		dataMap.put("cmCrossId", cmCrossId);
		dataMap.put("pageIndex", pageIndex);
		dataMap.put("pageSize", pageSize);
		
		Result result = cmCrossService.getTCmTrain(dataMap);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		TCmOriginalCross ocross = cmCrossService.getOriginalCrossInfoByCmCrossId(cmCrossId);
//		if (ocross.getHighlineFlag().length() > 1) {
//			ocross.setHighlineFlag("2");
//		}

		return result;			
	}
	
	
	/**
	 * 根据逻辑交路ID获取物理交路车次信息（对数表查询）
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPhyCrossTrainInfo", method = RequestMethod.POST)
	public Result getPhyCrossTrainInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		String cmCrossId = (String) reqMap.get("cmCrossId");
		logger.debug("cmCrossId==" + cmCrossId);
		List<TCmCross> list = cmCrossService.getCrossInfoForCmCrossid_Phy(cmCrossId);
		PagingResult page = new PagingResult(
				cmCrossService.getCmUnitCrossInfoCount(reqMap), list);
		result.setData(page);

		return result;
	}
	
	/**
	 * @param reqMap
	 * @return
	 * 删除交路
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteCmUnitCorssInfo", method = RequestMethod.POST)
	public Result deleteCmUnitCorssInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
			String cmUnitCrossIds = StringUtil.objToStr(reqMap
					.get("cmUnitCrossIds"));
			if (cmUnitCrossIds != null) {
				String[] crossIdsArray = cmUnitCrossIds.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				for (int i = 0; i < crossIdsArray.length; i++) {
					crossIdsList.add(crossIdsArray[i]);
				}

				int count = cmCrossService.deleteCmUnitCrossInfoForCorssIds(crossIdsList);

			}
		return result;
	}
	
	/**
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来（跳转到列车时刻表页面）
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryCmTrainTimesNewIframe", method = RequestMethod.GET)
	public ModelAndView queryCmTrainTimesNewIframe(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView result = new ModelAndView(
				"trainplan/trainTimes/train_times");
		// 参照crossController 355/
		try {
			String trainId = request.getParameter("trainId");
			request.setAttribute("trainId", trainId);
			logger.info("queryTrainTimesNewIframe~~trainId==" + trainId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.addObject(StaticCodeType.SYSTEM_ERROR.getCode(),
					StaticCodeType.SYSTEM_ERROR.getCode());
			logger.info(StaticCodeType.SYSTEM_ERROR.getCode() + "=="
					+ StaticCodeType.SYSTEM_ERROR.getCode());
			result.addObject(StaticCodeType.SYSTEM_ERROR.getDescription(),
					StaticCodeType.SYSTEM_ERROR.getDescription());
			logger.info(StaticCodeType.SYSTEM_ERROR.getDescription() + "=="
					+ StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * 列车时刻表查询
	 */
	@ResponseBody
	@RequestMapping(value = "/queryCmTrainTimesDepands", method = RequestMethod.POST)
	public Result queryCmTrainTimesDepands(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		try {

			String baseTrainId = request.getParameter("baseTrainId");
			List<TrainTimeInfoJbt> times = cmCrossService.getTrainTimes(baseTrainId);
			result.setData(times);
		} catch (Exception e) {
		}
		return result;
	}
	
	/**
	 * @return
	 * 
	 * 查询18个路局
	 */
	@ResponseBody
	@RequestMapping(value = "/getFullStationInfo", method = RequestMethod.POST)
	public Result getFullStationInfo(){
		Result result = new Result();
		List<Ljzd>  ljzd = cmCrossService.getFullStationInfo();
		result.setData(ljzd);
		return result;
	}
}
