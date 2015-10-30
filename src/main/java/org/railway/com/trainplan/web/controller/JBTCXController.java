package org.railway.com.trainplan.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.ConstantUtil;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.BusinessInfo;
import org.railway.com.trainplan.entity.CmdInfoModel;
import org.railway.com.trainplan.entity.ModifyInfo;
import org.railway.com.trainplan.entity.PlanTrainForJbtEdit;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.entity.TrainTimeInfo;
import org.railway.com.trainplan.entity.TrainTimeInfoJbt;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.ModifyPlanService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.SchemeService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.TrainTimeService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.service.dto.PlanTrainDTOForModify;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;
import org.railway.com.trainplan.web.dto.PlanLineSTNDto;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplan.web.dto.TrainInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/jbtcx")
public class JBTCXController {
	private static Log logger = LogFactory.getLog(JBTCXController.class
			.getName());

	@Autowired
	private SchemeService schemeService;

	@Autowired
	private TrainTimeService trainTimeService;

	@Autowired
	private TrainInfoService trainInfoService;

	@Autowired
	private RunPlanService runPlanService;

	@Autowired
	private ModifyPlanService modifyPlanService;

	@Autowired
	private CrossService crossService;

	// @RequestMapping(method = RequestMethod.GET)
	// public String content() {
	// return "plan/plan_runline_check";
	// }
	//

	/**
	 * 跳转到列车运行时刻表图形页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView jbtUpdate(@RequestParam(defaultValue = "") String type,
			HttpServletRequest request) {
		if ("jbtUpdate".equals(type)) {
			return new ModelAndView("plan/jbtTrainInfo");
		}
		return new ModelAndView("plan/plan_runline_check");

	}

	// ============================jbt修改 start========================
	/**
	 * 根据trainLineId修改基本图列车信息 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/toUpdateJbtTrainNewIframe", method = RequestMethod.POST)
	public ModelAndView toUpdateJbtTrainNewIframe(HttpServletRequest request,
			@RequestBody Map<String, Object> reqMap,
			HttpServletResponse response) {
		ModelAndView result = new ModelAndView("plan/jbtTrainInfoEdit");
		try {
			String trainLineId = StringUtil.objToStr(reqMap.get("trainLineId"));
			request.setAttribute("trainLineId", trainLineId);
			logger.info("toUpdateJbtTrainNewIframe~~trainLineId=="
					+ trainLineId);
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
	 * 统计路局运行车次信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainsForEdit", method = RequestMethod.POST)
	public Result queryTrainsForEdit(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.info("queryTrains~~reqMap=" + reqMap);
			reqMap.put("operation", "客运");
			// 调用后台接口
			// PagingResult page = new
			// PagingResult(trainInfoService.getTrainInfoCount(reqMap),
			// trainInfoService.getTrainsForPage(reqMap));
			List<PlanTrainForJbtEdit> data = trainInfoService
					.getTrainsForPageForEdit(reqMap);
			for (int i = 0; i < data.size(); i++) {
				data.get(i).setLastTimeText(data.get(i).getStartTimeAll(),
						data.get(i).getEndTimeAll());
			}
			result.setData(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	// #########################################
	@ResponseBody
	@RequestMapping(value = "/queryTrainTimesforJbtQuery", method = RequestMethod.POST)
	public Result queryTrainTimesforJbtQuery(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			// 调用后台接口
			String trainId = StringUtil.objToStr(reqMap.get("trainId"));
			List<TrainTimeInfoJbt> times = trainTimeService
					.getTrainTimesforJbtQuery(trainId);
			result.setData(times);
			logger.info("queryTrainTimes~~trainId==" + trainId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 统计路局运行车次信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainTimesInfo", method = RequestMethod.POST)
	public Result getTrainTimesInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.info("queryTrains~~reqMap=" + reqMap);
			reqMap.put("operation", "客运");
			// 调用后台接口
			String trainLineId = StringUtil.objToStr(reqMap.get("trainLineId"));
			List<PlanTrainForJbtEdit> data = trainInfoService
					.getTrainsForPageForEdit(reqMap);
			result.setData(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 修改运行线的列车运行时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editJbtPlanLineTrainTimes", method = RequestMethod.POST)
	public Result editJbtPlanLineTrainTimes(@RequestBody String reqStr) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		Result result = new Result();
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);

			// 请求参数校验
			// 经由list
			JSONArray timesObj = JSONArray.fromObject(reqObj.get("items"));
			JSONObject trainlineObj = JSONObject.fromObject(reqObj
					.get("trainline"));

			String trainlineTempId = null;
			List<TrainTimeInfoJbt> list = new ArrayList<TrainTimeInfoJbt>();
			if (timesObj != null && timesObj.size() > 0) {
				for (int i = 0; i < timesObj.size(); i++) {
					TrainTimeInfoJbt temp = new TrainTimeInfoJbt();
					JSONObject obj = JSONObject.fromObject(timesObj
							.getJSONObject(i));
					String planTrainStnId = String.valueOf(obj
							.getString("planTrainStnId"));
					String parentId = String.valueOf(obj
							.getString("trainlineTempId"));
					trainlineTempId = parentId;
					Integer stnSort = Integer.valueOf(String.valueOf(obj
							.getString("stnSort")));
					String stnName = String.valueOf(obj.getString("stnName"));
					String nodeId = String.valueOf(obj.getString("nodeId"));
					String nodeName = String.valueOf(obj.getString("nodeName"));
					String bureauId = String.valueOf(obj.getString("bureauId"));
					String bureauShortName = String.valueOf(obj
							.getString("bureauShortName"));
					String stnBureauFull = String.valueOf(obj
							.getString("stnBureauFull"));
					String arrTrainNbr = String.valueOf(obj
							.getString("sourceParentName"));
					arrTrainNbr = "null".equals(arrTrainNbr) ? "" : arrTrainNbr;

					String arrTime = String
							.valueOf(obj.getString("sourceTime"));
					String dptTime = String
							.valueOf(obj.getString("targetTime"));
					arrTime = arrTime.equals("--") ? "" : arrTime;
					dptTime = dptTime.equals("--") ? "" : dptTime;
					if (arrTime != null) {
						arrTime = arrTime.split(" ")[arrTime.split(" ").length - 1];
					}
					if (dptTime != null) {
						dptTime = dptTime.split(" ")[dptTime.split(" ").length - 1];
					}

					String baseArrTime = String.valueOf(obj
							.getString("baseArrTime"));
					Integer arrRunDays = Integer.valueOf(String.valueOf(obj
							.getString("sourceRunDays")));
					String dptTrainNbr = String.valueOf(obj
							.getString("targetParentName"));
					dptTrainNbr = "null".equals(dptTrainNbr) ? "" : dptTrainNbr;

					String baseDptTime = String.valueOf(obj
							.getString("baseDptTime"));
					Integer runDays = Integer.valueOf(String.valueOf(obj
							.getString("targetRunDays")));
					String trackName = String.valueOf(obj
							.getString("trackName"));
					String platForm = String.valueOf(obj.getString("platForm"));
					String jobs = String.valueOf(obj.getString("jobsValue"));
					// String stepStr =
					// String.valueOf(obj.getString("stepStr"));
					String stationFlag = String.valueOf(obj
							.getString("stationFlag"));
					int childIndex = Integer.valueOf(obj
							.getString("childIndex"));
					String kyyy = String.valueOf(obj.getString("kyyy"));
					String arrTimeAll = String.valueOf(obj
							.getString("arrTimeAll"));
					String dptTimeAll = String.valueOf(obj
							.getString("dptTimeAll"));
					// Integer isChangeValue2
					// =Integer.valueOf(String.valueOf(obj.getString("isChangeValue")));

					temp.setPlanTrainStnId(UUID.randomUUID().toString());
					temp.setTrainlineTempId(parentId);
					temp.setStnSort(stnSort);
					temp.setStnName(stnName);
					temp.setNodeId(nodeId);
					temp.setNodeName(nodeName);
					temp.setBureauId(bureauId);
					temp.setBureauShortName(bureauShortName);
					temp.setStnBureauFull(stnBureauFull);
					temp.setArrTrainNbr(arrTrainNbr);
					temp.setArrTime(arrTime);
					temp.setBaseArrTime(baseArrTime.equals("--") ? ""
							: baseArrTime);
					temp.setArrRunDays(arrRunDays);
					temp.setDptTrainNbr(dptTrainNbr);
					temp.setDptTime(dptTime);
					temp.setBaseDptTime(baseDptTime.equals("--") ? ""
							: baseDptTime);
					temp.setRunDays(runDays);
					temp.setTrackName(trackName);
					temp.setPlatForm(platForm);
					temp.setJobs(jobs);
					temp.setStationFlag(stationFlag);
					temp.setChildIndex(childIndex);
					temp.setKyyy(kyyy);
//					temp.setPlatForm(String.valueOf(obj.getString("platForm")));

					if (!"".equals(arrTime) && !"--".equals(arrTime)) {
						String[] arrtimeArr = arrTime.split(":");
						temp.setSOURCE_TIME_SCHEDULE_HOUR(arrtimeArr[0]);
						temp.setSOURCE_TIME_SCHEDULE_MINUTE(arrtimeArr[1]);
						temp.setSOURCE_TIME_SCHEDULE_SECOND(arrtimeArr[2]);
					}
					if (!"".equals(dptTime) && !"--".equals(dptTime)) {
						String[] dpttimeArr = dptTime.split(":");
						temp.setTARGET_TIME_SCHEDULE_HOUR(dpttimeArr[0]);
						temp.setTARGET_TIME_SCHEDULE_MINUTE(dpttimeArr[1]);
						temp.setTARGET_TIME_SCHEDULE_SECOND(dpttimeArr[2]);
					}
					if (!"".equals(arrTimeAll) && !"--".equals(arrTimeAll)
							&& !"null".equals(arrTimeAll)) {
						String[] arrTimeArr = arrTimeAll.split(" ");
						String arr = arrTimeArr[0] + " " + arrTime;
						temp.setArrTimeAll(arr);
					}
					if (!"".equals(dptTimeAll) && !"--".equals(dptTimeAll)
							&& !"null".equals(dptTimeAll)) {
						String[] dpttimeArr = dptTimeAll.split(" ");
						String dpt = dpttimeArr[0] + " " + dptTime;
						temp.setDptTimeAll(dpt);
					}

					temp.setParentNbr(String.valueOf(trainlineObj
							.get("trainNbr")));

					System.out.println("JOBS::" + temp.getJobs() + ",站台::"
							+ temp.getPlatForm());
					list.add(temp);
				}
			}

			/** 原本打算直接修改原数据,但是下面已经实现了,批量插入 **/
			// 删除原来的（根据父建id删除）
			trainInfoService.deleteTrainlineItemTempByParentId(trainlineTempId);
			trainTimeService.saveAllTrainlineItemTemp(list);

			// 列车信息

			PlanTrainForJbtEdit trainTemp = trainInfoService
					.getTrainsForPageForjbtId(String.valueOf(trainlineObj
							.getString("id")));// 数据库的数据
			// PlanTrainForJbtEdit trainTemp = new PlanTrainForJbtEdit();

			String trainId = String.valueOf(trainlineObj.get("id"));// 车次号
			String trainNbr = String.valueOf(trainlineObj.get("trainNbr"));// 车次号
			String typeId = String.valueOf(trainlineObj.get("typeId"));// 类型
			String typeName = String.valueOf(trainlineObj.get("typeName"));// 类型
			String highSpeedStr = String.valueOf(trainlineObj
					.get("highSpeedStr")); // true为运行在高铁线路，false为运行在既有线
			String sourceBureauShortName = String.valueOf(trainlineObj
					.get("sourceBureauShortName"));// 始发局
			String sourceNodeName = String.valueOf(trainlineObj
					.get("sourceNodeName"));// 始发站
			String sourceTime = String.valueOf(trainlineObj.get("sourceTime"));// 始发时间
			String targetTime = String.valueOf(trainlineObj.get("targetTime"));// 终到时间
			String sourceTimeAll = String.valueOf(trainlineObj
					.get("sourceTimeAll"));// 始发时间
			String targetTimeAll = String.valueOf(trainlineObj
					.get("targetTimeAll"));// 终到时间
			String sourceTimeActivity = String.valueOf(trainlineObj
					.get("sourceTimeActivity"));// 有效期
			String targetTimeActivity = String.valueOf(trainlineObj
					.get("targetTimeActivity"));// 有效期
			String targetBureauShortName = String.valueOf(trainlineObj
					.get("targetBureauShortName"));// 终到局
			String targetNodeName = String.valueOf(trainlineObj
					.get("targetNodeName"));// 终到站
			// String runDays =
			// String.valueOf(trainlineObj.get("runDays"));//运行天数
			// String targetDays =
			// String.valueOf(trainlineObj.get("targetRunDays"));//到达天数
			String routeBureauShortNames = String.valueOf(trainlineObj
					.get("routeBureauShortNames"));// 途经局
			// String executionText =
			// String.valueOf(trainlineObj.get("executionText"));//执行有效期

			trainTemp.setPlanTrainId(trainId);
			trainTemp.setTrainNbr(trainNbr);
			trainTemp.setTrainTypeId(typeId);
			trainTemp.setTrainTypeName(typeName);
			trainTemp.setHighline_flag(highSpeedStr);
			trainTemp.setStartBureauShortName(sourceBureauShortName);
			trainTemp.setStartStn(sourceNodeName);

			if (!"".equals(sourceTime) && !"--".equals(sourceTime)) {
				String[] arrtimeArr = sourceTime.split(":");
				trainTemp.setSourceHour(arrtimeArr[0]);
				trainTemp.setSourceMin(arrtimeArr[1]);
				trainTemp.setSourceSecend(arrtimeArr[2]);
			}
			if (!"".equals(targetTime) && !"--".equals(targetTime)) {
				String[] dpttimeArr = targetTime.split(":");
				trainTemp.setTargetHour(dpttimeArr[0]);
				trainTemp.setTargetMin(dpttimeArr[1]);
				trainTemp.setTargetSecend(dpttimeArr[2]);
			}
			if (!"".equals(sourceTimeAll) && sourceTimeAll != null) {
				sourceTime = sourceTimeAll.substring(0,
						sourceTimeAll.indexOf(" ") + 1)
						+ sourceTime;
			}
			if (!"".equals(targetTimeAll) && targetTimeAll != null) {
				targetTime = targetTimeAll.substring(0,
						targetTimeAll.indexOf(" ") + 1)
						+ targetTime;
			}
			trainTemp.setSourceTime(sourceTime);
			if (!"".equals(sourceTimeActivity) && sourceTimeActivity != null) {
				sourceTimeActivity = DateUtil
						.parseStringDateTOYYYYMMDD(sourceTimeActivity)
						+ " 00:00:00";
			}
			if (!"".equals(targetTimeActivity) && targetTimeActivity != null) {
				targetTimeActivity = DateUtil
						.parseStringDateTOYYYYMMDD(targetTimeActivity)
						+ " 00:00:00";
			}

			trainTemp.setSourceTimeActivity(sourceTimeActivity);
			trainTemp.setTargetTime(targetTime);
			trainTemp.setTargetTimeActivity(targetTimeActivity);
			trainTemp.setEndStn(targetNodeName);
			trainTemp.setEndBreauShortName(targetBureauShortName);
			trainTemp.setRoutingBureauShortName(routeBureauShortNames);

			// trainTemp.set
			// trainTemp.set
			// trainTemp.set

			// 保存数据
			int count = trainTimeService
					.updatePlanLineTrainForJbtEdit(trainTemp);
			logger.info("批量修改开行计划成功~~count==" + "");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	// ============================jbt修改 end========================

	@RequestMapping(value = "/getTrainTimeCanvasPage", method = RequestMethod.GET)
	public ModelAndView getTrainTimeCanvasPage(HttpServletRequest request) {
		return new ModelAndView("plan/train_runline_canvas").addObject(
				"planTrainId", request.getParameter("planTrainId")).addObject(
				"trainNbr", request.getParameter("trainNbr"));
	}

	@RequestMapping(value = "/getTrainTimeCanvasPagejy", method = RequestMethod.GET)
	public ModelAndView getTrainTimeCanvasPagejy(HttpServletRequest request) {
		return new ModelAndView("plan/train_runline_canvasjy").addObject(
				"planTrainId", request.getParameter("planTrainId")).addObject(
				"trainNbr", request.getParameter("trainNbr"));
	}

	@RequestMapping(value = "/getTrainTimeCanvasPagegt", method = RequestMethod.GET)
	public ModelAndView getTrainTimeCanvasPagegt(HttpServletRequest request) {
		return new ModelAndView("plan/train_runline_canvasgt").addObject(
				"planTrainId", request.getParameter("planTrainId")).addObject(
				"trainNbr", request.getParameter("trainNbr"));
	}

	@ResponseBody
	@RequestMapping(value = "/querySchemes", method = RequestMethod.POST)
	public Result querySchemes(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.info("querySchemes~~reqMap=" + reqMap);
			// 调用后台接口
			List<SchemeInfo> schemeInfos = schemeService.getSchemes();
			result.setData(schemeInfos);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 获取列车和类型
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryBusinesses", method = RequestMethod.POST)
	public Result queryBusinesses(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.info("queryBusinesses~~reqMap=" + reqMap);
			// 调用后台接口
			List<BusinessInfo> businessInfo = runPlanService.getBusiness();
			for (int i = 0; i < businessInfo.size(); i++) {
				if ("客运".equals(businessInfo.get(i).getBusinessName())) {
					businessInfo.add(0, businessInfo.get(i));
					businessInfo.remove(i + 1);
				}
			}
			result.setData(businessInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 统计路局运行车次信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrains", method = RequestMethod.POST)
	public Result queryTrains(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
//			logger.info("queryTrains~~reqMap=" + reqMap);
			reqMap.put("operation", "客运");
			// 调用后台接口
			PagingResult page = new PagingResult(
					trainInfoService.getTrainInfoCount(reqMap),
					trainInfoService.getTrainsForPage(reqMap));
			result.setData(page);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 统计路局运行车次信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainLines", method = RequestMethod.POST)
	public Result queryTrainLines(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		if (null == user) {
			reqMap.put("user", "");
		} else {
			reqMap.put("user", user.getBureau());
		}

		// 在reqMap 中增加用户信息{startBureauShortName=沈, endBureauShortName=西,
		// startDate=20141016, endDate=20141016, rownumstart=0, rownumend=50}
		try {
			logger.info("queryTrains~~reqMap=" + reqMap);
			reqMap.put("operation", "客运");
			// 调用后台接口
			PagingResult page = new PagingResult(
					trainInfoService.getTrainLinesCount(reqMap),
					trainInfoService.getTrainLinesForPage(reqMap));
			result.setData(page);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	// #########################################
	@ResponseBody
	@RequestMapping(value = "/queryTrainTimes", method = RequestMethod.POST)
	public Result queryTrainTimes(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			// 调用后台接口
			String trainId = StringUtil.objToStr(reqMap.get("trainId"));
			List<TrainTimeInfo> times = trainTimeService.getTrainTimes(trainId);
			result.setData(times);
			logger.info("queryTrainTimes~~trainId==" + trainId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainTimesNewIframe", method = RequestMethod.GET)
	public ModelAndView queryTrainTimesNewIframe(HttpServletRequest request,
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
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainTimesByTrainId", method = RequestMethod.GET)
	public ModelAndView queryTrainTimesByTrainId(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView result = new ModelAndView(
				"trainplan/trainTimes/stn_train_times");
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
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainTimesDepands", method = RequestMethod.GET)
	public Result queryTrainTimesDepands(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		// 参照crossController 355/
		try {

			String trainId = request.getParameter("trainId");
			logger.info("queryTrainTimesDepands~~trainId==" + trainId);
			List<TrainTimeInfo> times = trainTimeService.getTrainTimes(trainId);
			result.setData(times);
			logger.info("times==" + times);
		} catch (Exception e) {
		}
		return result;
	}

	// #########################################
	@ResponseBody
	@RequestMapping(value = "/queryTrainLineTimes", method = RequestMethod.POST)
	public Result queryTrainLineTimes(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			// 调用后台接口
			String trainId = StringUtil.objToStr(reqMap.get("trainId"));
			List<TrainTimeInfo> times = trainTimeService
					.getTrainLineTimes(trainId);
			logger.info("queryTrainLineTimes~~trainId==" + trainId);
			result.setData(times);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainLineTimesNewIframe", method = RequestMethod.GET)
	public ModelAndView queryTrainLineTimesNewIframe(
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView result = new ModelAndView(
				"trainplan/trainTimes/trainline_times");
		// 参照crossController 355/
		try {
			String trainId = request.getParameter("trainId");
			logger.info("queryTrainLineTimesNewIframe~~trainId==" + trainId);
			request.setAttribute("trainId", trainId);
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
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryTrainLineTimesDepands", method = RequestMethod.GET)
	public Result queryTrainLineTimesDepands(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		// 参照crossController 355/
		try {

			String trainId = request.getParameter("trainId");
			logger.info("queryTrainLineTimesDepands~~trainId==" + trainId);
			List<TrainTimeInfo> times = trainTimeService
					.getTrainLineTimes(trainId);
			result.setData(times);
			logger.info("times==" + times);
		} catch (Exception e) {
		}
		return result;
	}

	// #########################################
	/**
	 * 查询运行线的列车运行时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryPlanLineTrainTimes", method = RequestMethod.POST)
	public Result queryPlanLineTrainTimes(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {

			String trainId = StringUtil.objToStr(reqMap.get("trainId"));
			logger.info("queryPlanLineTrainTimes~~trainId==" + trainId);
			List<TrainTimeInfo> times = trainTimeService
					.getTrainTimeInfoByTrainId(trainId);
			result.setData(times);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryPlanLineTrainTimesNewIframe", method = RequestMethod.GET)
	public ModelAndView queryPlanLineTrainTimesNewIframe(
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView result = new ModelAndView(
				"trainplan/trainTimes/planline_train_times");
		// 参照crossController 355/
		try {
			String trainId = request.getParameter("trainId");
			logger.info("queryTrainLineTimesNewIframe~~trainId==" + trainId);
			request.setAttribute("trainId", trainId);
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
	 * 根据planTrainId查询运行线的列车运行时刻表 独立页面出来
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryPlanLineTrainTimesDepands", method = RequestMethod.GET)
	public Result queryPlanLineTrainTimesDepands(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		// 参照crossController 355/
		try {

			String trainId = request.getParameter("trainId");
			logger.info("queryTrainLineTimesDepands~~trainId==" + trainId);
			List<TrainTimeInfo> times = trainTimeService
					.getTrainTimeInfoByTrainId(trainId);
			// 如果是临客,可能始发站，终到站，不是SFZ，ZDZ
			times.get(0).setStationFlag("SFZ");
			times.get(times.size() - 1).setStationFlag("ZDZ");
			result.setData(times);
			logger.info("times==" + times);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 根据planTrainId查询运行线的列车运行时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanTrainStnInfoForPlanTrainId", method = RequestMethod.POST)
	public Result getPlanTrainStnInfoForPlanTrainId(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {

			String trainId = StringUtil.objToStr(reqMap.get("trainId"));
			logger.info("getPlanTrainStnInfoForPlanTrainId~~trainId=="
					+ trainId);
			List<TrainTimeInfo> times = trainTimeService
					.getPlanTrainStnInfoForPlanTrainId(trainId);
			result.setData(times);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getTrainTimeInfoByPlanTrainIdjy", method = RequestMethod.POST)
	public Result getTrainTimeInfoByPlanTrainIdyj(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getTrainTimeInfoByPlanTrainIdjy~~reqMap==" + reqMap);
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		try {

			List<TrainTimeInfo> list = trainTimeService
					.getTrainTimeInfoByPlanTrainIdjy(planTrainId);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			// 列车信息
			List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
			Map<String, Object> crossMap = new HashMap<String, Object>();
			// 用于纵坐标的经由站列表
			List<Station> listStation = new ArrayList<Station>();
			// 横坐标的开始日期
			String arrDate = "";
			// 横坐标的结束日期
			String dptDate = "";
			if (list != null && list.size() > 0) {
				// 设置列车信息
				TrainInfoDto dto = new TrainInfoDto();
				dto.setTrainName(trainNbr);
				List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();

				// 循环经由站
				for (int i = 0; i < list.size(); i++) {

					TrainTimeInfo subInfo = list.get(i);
					PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
					stnDtoStart.setArrTime(subInfo.getArrTime());
					stnDtoStart.setDptTime(subInfo.getDptTime());
					// stnDtoStart.setStayTime(0);
					stnDtoStart.setStnName(subInfo.getStnName());
					stnDtoStart.setStationType(subInfo.getStationFlag());
					trainStns.add(stnDtoStart);
					// 获取起始站的到站日期和终到站的出发日期为横坐标的日期段
					if (i == 0) {
						String arrTime = subInfo.getArrTime();
						String dptTime = subInfo.getDptTime();
						arrDate = DateUtil.format(DateUtil.parseDate(
								arrTime == null || "".equals(arrTime) ? dptTime
										: arrTime, "yyyy-MM-dd hh:mm"),
								"yyyy-MM-dd");
						// 设置始发站
						dto.setStartStn(subInfo.getStnName());
					}
					if (i == list.size() - 1) {
						String arrTime = subInfo.getArrTime();
						String dptTime = subInfo.getDptTime();
						dptDate = DateUtil.format(DateUtil.parseDate(
								dptTime == null || "".equals(dptTime) ? arrTime
										: dptTime, "yyyy-MM-dd hh:mm"),
								"yyyy-MM-dd");
						// 设置终到站
						dto.setEndStn(subInfo.getStnName());
					}
					// 纵坐标数据
					Station station = new Station();
					station.setStnName(subInfo.getStnName());
					station.setStationType(subInfo.getStationFlag());
					listStation.add(station);
				}
				dto.setTrainStns(trainStns);
				trains.add(dto);
			}
			crossMap.put("trains", trains);
			dataList.add(crossMap);
			PlanLineGrid grid = null;

			// 生成横纵坐标
			List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
			List<PlanLineGridX> listGridX = getPlanLineGridX(arrDate, dptDate);
			grid = new PlanLineGrid(listGridX, listGridY);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("myJlData", dataList);
			dataMap.put("gridData", grid);

			System.out.println(dataList);
			System.out.println(grid);
			result.setData(dataMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getTrainTimeInfoByPlanTrainIdgt", method = RequestMethod.POST)
	public Result getTrainTimeInfoByPlanTrainIdgt(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getTrainTimeInfoByPlanTrainIdjy~~reqMap==" + reqMap);
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		try {

			List<TrainTimeInfo> list = trainTimeService
					.getTrainTimeInfoByPlanTrainIdjy(planTrainId);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			// 列车信息
			List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
			Map<String, Object> crossMap = new HashMap<String, Object>();
			// 用于纵坐标的经由站列表
			List<Station> listStation = new ArrayList<Station>();
			// 横坐标的开始日期
			String arrDate = "";
			// 横坐标的结束日期
			String dptDate = "";
			if (list != null && list.size() > 0) {
				// 设置列车信息
				TrainInfoDto dto = new TrainInfoDto();
				dto.setTrainName(trainNbr);
				List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();

				// 循环经由站
				for (int i = 0; i < list.size(); i++) {

					TrainTimeInfo subInfo = list.get(i);
					PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
					stnDtoStart.setArrTime(subInfo.getArrTime());
					stnDtoStart.setDptTime(subInfo.getDptTime());
					// stnDtoStart.setStayTime(0);
					stnDtoStart.setStnName(subInfo.getStnName());
					stnDtoStart.setStationType(subInfo.getStationFlag());
					trainStns.add(stnDtoStart);
					// 获取起始站的到站日期和终到站的出发日期为横坐标的日期段
					if (i == 0) {
						String arrTime = subInfo.getArrTime();
						String dptTime = subInfo.getDptTime();
						arrDate = DateUtil.format(DateUtil.parseDate(
								arrTime == null || "".equals(arrTime) ? dptTime
										: arrTime, "yyyy-MM-dd hh:mm"),
								"yyyy-MM-dd");
						// 设置始发站
						dto.setStartStn(subInfo.getStnName());
					}
					if (i == list.size() - 1) {
						String arrTime = subInfo.getArrTime();
						String dptTime = subInfo.getDptTime();
						dptDate = DateUtil.format(DateUtil.parseDate(
								dptTime == null || "".equals(dptTime) ? arrTime
										: dptTime, "yyyy-MM-dd hh:mm"),
								"yyyy-MM-dd");
						// 设置终到站
						dto.setEndStn(subInfo.getStnName());
					}
					// 纵坐标数据
					Station station = new Station();
					station.setStnName(subInfo.getStnName());
					station.setStationType(subInfo.getStationFlag());
					listStation.add(station);
				}
				dto.setTrainStns(trainStns);
				trains.add(dto);
			}
			crossMap.put("trains", trains);
			dataList.add(crossMap);
			PlanLineGrid grid = null;

			// 生成横纵坐标
			List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
			List<PlanLineGridX> listGridX = getPlanLineGridX(arrDate, dptDate);
			grid = new PlanLineGrid(listGridX, listGridY);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("myJlData", dataList);
			dataMap.put("gridData", grid);

			System.out.println(dataList);
			System.out.println(grid);
			result.setData(dataMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 修改运行线的列车运行时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editPlanLineTrainTimes", method = RequestMethod.POST)
	public Result editPlanLineTrainTimes(@RequestBody String reqStr) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		Result result = new Result();
		logger.info("批量修改开行计划~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String telName = reqObj.getString("telName");
			String startDate = reqObj.getString("startDate");
			String endDate = reqObj.getString("endDate");
			String runRule = reqObj.getString("runRule");// 每日、隔日、择日
			String selectedDate = reqObj.getString("selectedDate");// 择日时，包含的日期yyyy-mm-dd
			String startStn = reqObj.getString("startStn");
			String endStn = reqObj.getString("endStn");
			String trainNbr = reqObj.getString("trainNbr");
			String planTrainId = reqObj.getString("planTrainId");

			// 请求参数校验
			if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)
					|| StringUtil.isEmpty(runRule)
					|| StringUtil.isEmpty(startStn)
					|| StringUtil.isEmpty(endStn)
					|| StringUtil.isEmpty(trainNbr)) {
				result.setCode("-1");
				result.setMessage("无效的请求参数");
				return result;
			}

			// 根据开始日期、 截至日期、运行规律生成rundate字符串
			List<String> runDates = this.getRunDates(startDate, endDate,
					runRule, selectedDate);
			// 经由list
			JSONArray timesObj = JSONArray.fromObject(reqObj.get("times"));
			List<TrainTimeInfo> list = new ArrayList<TrainTimeInfo>();
			if (timesObj != null && timesObj.size() > 0) {
				for (int i = 0; i < timesObj.size(); i++) {
					TrainTimeInfo temp = new TrainTimeInfo();
//					Integer isChangeValue = Integer.valueOf(String
//							.valueOf(timesObj.getJSONObject(i).get(
//									"isChangeValue")));
					Integer isChangeValue = 1;
					if (isChangeValue.equals(0)) {
						JSONObject obj = JSONObject.fromObject(timesObj
								.getJSONObject(i).get("data"));
						BeanUtils.copyProperties(temp, obj);
						if (StringUtil.isEmpty(temp.getArrTime())
								|| "null".equals(temp.getArrTime())) {
							temp.setArrTime("");
						}
						if (StringUtil.isEmpty(temp.getBaseArrTime())
								|| "null".equals(temp.getBaseArrTime())) {
							temp.setBaseArrTime("");
						}
						if (StringUtil.isEmpty(temp.getDptTime())
								|| "null".equals(temp.getDptTime())) {
							temp.setDptTime("");
						}
						if (StringUtil.isEmpty(temp.getBaseDptTime())
								|| "null".equals(temp.getBaseDptTime())) {
							temp.setBaseDptTime("");
						}
						if (StringUtil.isEmpty(temp.getArrTrainNbr())
								|| "null".equals(temp.getArrTrainNbr())) {
							temp.setArrTrainNbr("");
						}
						if (StringUtil.isEmpty(temp.getDptTrainNbr())
								|| "null".equals(temp.getDptTrainNbr())) {
							temp.setDptTrainNbr("");
						}

						// stnType 车站类型（1:始发站；2:终到站；4:分界口; ）
						if ("SFZ".equals(temp.getStationFlag())) {// SFZ:始发站
																	// ZDZ：终到站
																	// FJK：分界口
																	// BTZ：不停站
																	// TZ：停站
							temp.setArrTime(temp.getDptTime());
							temp.setStnType("1");
						} else if ("ZDZ".equals(temp.getStationFlag())) {// SFZ:始发站
																			// ZDZ：终到站
																			// FJK：分界口
																			// BTZ：不停站
																			// TZ：停站
							temp.setDptTime(temp.getArrTime());
							temp.setStnType("2");
						} else if ("FJK".equals(temp.getStationFlag())) {// SFZ:始发站
																			// ZDZ：终到站
																			// FJK：分界口
																			// BTZ：不停站
																			// TZ：停站
							temp.setStnType("4");
						}
					} else {
						JSONObject obj = JSONObject.fromObject(timesObj
								.getJSONObject(i));
						Integer index = Integer.valueOf(String.valueOf(obj
								.getString("index")));
						String planTrainStnId = String.valueOf(obj
								.getString("planTrainStnId"));
						Integer stnSort = Integer.valueOf(String.valueOf(obj
								.getString("stnSort")));
						String stnName = String.valueOf(obj
								.getString("stnName"));
						String nodeId = String.valueOf(obj.getString("nodeId"));
						String nodeName = String.valueOf(obj
								.getString("nodeName"));
						String bureauId = String.valueOf(obj
								.getString("bureauId"));
						String bureauShortName = String.valueOf(obj
								.getString("bureauShortName"));
						String stnBureauFull = String.valueOf(obj
								.getString("stnBureauFull"));
						String arrTrainNbr = String.valueOf(obj
								.getString("arrTrainNbr"));
						String arrTime = String.valueOf(obj
								.getString("arrTime"));
						String baseArrTime = String.valueOf(obj
								.getString("baseArrTime"));
						Integer arrRunDays = Integer.valueOf(String.valueOf(obj
								.getString("arrRunDays")));
						String dptTrainNbr = String.valueOf(obj
								.getString("dptTrainNbr"));
						String dptTime = String.valueOf(obj
								.getString("dptTime"));
						String baseDptTime = String.valueOf(obj
								.getString("baseDptTime"));
						Integer runDays = Integer.valueOf(String.valueOf(obj
								.getString("runDays")));
						String trackName = String.valueOf(obj
								.getString("trackName"));
						Integer platForm = "".equals(obj.getString("platForm"))?null:Integer.valueOf(obj.getString("platForm"));
						String jobs = String.valueOf(obj.getString("jobs"));
						String stepStr = String.valueOf(obj
								.getString("stepStr"));
						String stationFlag = String.valueOf(obj
								.getString("stationFlag"));
						Integer isChangeValue2 = Integer.valueOf(String
								.valueOf(obj.getString("isChangeValue")));

						JSONObject ky = JSONObject.fromObject(obj.getString("kyyy"));
						String kyyy = String.valueOf(ky
								.getString("text"));
						if("是".equals(kyyy)){
							if(!jobs.contains(ConstantUtil.KY_STATION_FLAG_NO)){
								jobs = jobs + ConstantUtil.KY_STATION_FLAG;
							}
						}
						else{
							jobs = jobs.replace(ConstantUtil.KY_STATION_FLAG, "");
						}
						if (("BTZ".equals(temp.getStationFlag()) ||"TZ".equals(temp.getStationFlag())) && !jobs.contains("经由")) {// SFZ:始发站
							// BTZ：不停站
							// TZ：停站
							jobs = "<经由>" + jobs;
						}
						temp.setPlanTrainStnId(planTrainStnId);
						temp.setStnSort(stnSort);
						temp.setStnName(stnName);
						temp.setNodeId(nodeId);
						temp.setNodeName(nodeName);
						temp.setBureauId(bureauId);
						temp.setBureauShortName(bureauShortName);
						temp.setStnBureauFull(stnBureauFull);
						temp.setArrTrainNbr(arrTrainNbr);
						temp.setArrTime(arrTime);
						temp.setBaseArrTime(baseArrTime.equals("--") ? ""
								: baseArrTime);
						temp.setArrRunDays(arrRunDays);
						temp.setDptTrainNbr(dptTrainNbr);
						temp.setDptTime(dptTime);
						temp.setBaseDptTime(baseDptTime.equals("--") ? ""
								: baseDptTime);
						temp.setRunDays(runDays);
						temp.setTrackName(trackName);
						temp.setPlatForm(platForm);
						temp.setJobs(jobs);
						temp.setStationFlag(stationFlag);
						temp.setStationFlag(stationFlag);

						// stnType 车站类型（1:始发站；2:终到站；4:分界口; ）
						if ("SFZ".equals(temp.getStationFlag())) {// SFZ:始发站
																	// ZDZ：终到站
																	// FJK：分界口
																	// BTZ：不停站
																	// TZ：停站
							// temp.setArrTime(temp.getDptTime());
							temp.setStnType("1");
						} else if ("ZDZ".equals(temp.getStationFlag())) {// SFZ:始发站
																			// ZDZ：终到站
																			// FJK：分界口
																			// BTZ：不停站
																			// TZ：停站
							// temp.setDptTime(temp.getArrTime());
							temp.setStnType("2");
						} else if ("FJK".equals(temp.getStationFlag())) {// SFZ:始发站
																			// ZDZ：终到站
																			// FJK：分界口
																			// BTZ：不停站
																			// TZ：停站
							temp.setStnType("4");
						}
					}

					list.add(temp);

				}
			}
			// 把事务放到service
			// 根据开始日期、 截至日期、运行规律生成rundate字符串
			List<ModifyInfo> modifys = new ArrayList<ModifyInfo>();
			if (runDates != null && runDates.size() > 0) {
				for (String date : runDates) {
					// trainNbr
					List<PlanTrainDTOForModify> trainPlanDtoList = runPlanService
							.findPlanInfoByNBrAndRunDate(trainNbr,
									DateUtil.parseStringDateTOyyyymmdd(date),planTrainId,null);
					PlanTrainDTOForModify trainPlanDto = new PlanTrainDTOForModify();
					if (null != trainPlanDtoList && !trainPlanDtoList.isEmpty()) {
						trainPlanDto = trainPlanDtoList.get(0);
					}
					ModifyInfo modifyInfo = new ModifyInfo();
					// 图定：有：planCrossId，crossName,
					// 临客：有 2:telId 3 cmdTrainId
					modifyInfo.setPlanModifyId(UUID.randomUUID().toString());
					modifyInfo.setPlanTrainId(trainPlanDto.getPlanTrainId());
					modifyInfo.setPlanCrossId(trainPlanDto.getPlanCrossId());
					modifyInfo.setCrossName(trainPlanDto.getCrossName());
					modifyInfo.setRunDate(trainPlanDto.getRunDate());
					modifyInfo.setTrainNbr(trainPlanDto.getTrainNbr());
					modifyInfo.setModifyType(0 + "");
					modifyInfo.setModifyReason(telName);
					modifyInfo.setStartDate(startDate);
					modifyInfo.setEndDate(endDate);
					modifyInfo.setRule(runRule);
					modifyInfo.setSelectedDate(selectedDate + "");
					modifyInfo.setModifyContent(trainPlanDto.getTrainNbr()
							+ "车次在" + trainPlanDto.getRunDate() + "调整时刻");
					modifyInfo.setModifyTime(new Date());
					modifyInfo.setModifyPeople(user.getName().equals(null) ? ""
							: user.getName());
					modifyInfo.setModifyPeopleOrg(user.getBureauFullName());
					modifyInfo.setModifyPeopleBureau(user.getBureauShortName());
					// 修改plan_cross：check_type=0
					if ("0".equals(trainPlanDto.getCreatType())
							|| "1".equals(trainPlanDto.getCreatType())) {
						int updateCheckType = crossService
								.updatePlanCrossCheckType(
										trainPlanDto.getPlanCrossId(), 0);
						logger.info("修改plan_cross的check_type=0，成功条数:::"
								+ updateCheckType);
						int updatePlanCheckHisFlag = modifyPlanService
								.updatePlanCheckHisFlag(
										trainPlanDto.getPlanCrossId(), "1");
						logger.info("修改plan_check的check_his_flag=1，成功条数:::"
								+ updatePlanCheckHisFlag);
					}
					// 临客
					else if ("2".equals(trainPlanDto.getCreatType())) {
						int updatePlanCheckHisFlag = modifyPlanService
								.updatePlanCheckHisFlagCmd(
										trainPlanDto.getTelId(), "1");
						logger.info("修改plan_check的check_his_flag=1，成功条数:::"
								+ updatePlanCheckHisFlag);
					} else if ("3".equals(trainPlanDto.getCreatType())) {
						int updatePlanCheckHisFlag = modifyPlanService
								.updatePlanCheckHisFlagCmd(
										trainPlanDto.getCmdTrainId(), "1");
						logger.info("修改plan_check的check_his_flag=1，成功条数:::"
								+ updatePlanCheckHisFlag);
					}
					int updateTrainPlanSentBuearu = modifyPlanService
							.updateTrainPlanSentBuearu(trainPlanDto
									.getPlanTrainId());
					logger.info("修改plan_train的sent_buearu=null，成功条数:::"
							+ updateTrainPlanSentBuearu);
					modifys.add(modifyInfo);
				}
			}
			// 向modify表中插入数据
			int addModifyCount = modifyPlanService.addModifyList(modifys);
			logger.info("批量 add modify 成功~~count==" + addModifyCount);
			// plan_cross：check_type-->0

			// 保存数据
			int count = trainTimeService.updatePlanLineTrainTimes(runDates,
					telName, reqObj.getString("trainNbr"), startStn, endStn,
					list);
			logger.info("批量修改开行计划成功~~count==" + count);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 修改运行线的列车运行经路
	 * 
	 * @param reqMap
	 * @return
	 */
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping(value = "/editPlanLineTrainPathWay", method = RequestMethod.POST)
	public Result editPlanLineTrainPathWay(@RequestBody String reqStr) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		Result result = new Result();
		// logger.info("批量修改开行计划~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String telName = reqObj.getString("telName");
			String startDate = reqObj.getString("startDate");
			String endDate = reqObj.getString("endDate");
			String runRule = reqObj.getString("runRule");// 每日、隔日、择日
			String selectedDate = reqObj.getString("selectedDate");// 择日时，包含的日期yyyy-mm-dd
			String startStn = reqObj.getString("startStn");
			String endStn = reqObj.getString("endStn");
			String trainNbr = reqObj.getString("trainNbr");
			String startStnNew = null;
			String endStnNew = null;
			String planTrainId = reqObj.getString("planTrainId");
			// 请求参数校验
			if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)
					|| StringUtil.isEmpty(runRule)
					|| StringUtil.isEmpty(startStn)
					|| StringUtil.isEmpty(endStn)
					|| StringUtil.isEmpty(trainNbr)) {
				result.setCode("-1");
				result.setMessage("无效的请求参数");
				return result;
			}

			// 根据开始日期、 截至日期、运行规律生成rundate字符串
			List<String> runDates = this.getRunDates(startDate, endDate,
					runRule, selectedDate);
			logger.info("runDates=============" + runDates.size());
			logger.info("runDates=============" + runDates.toString());
			// 经由list
			JSONArray timesObj = JSONArray.fromObject(reqObj.get("times"));
			List<TrainTimeInfo> list = new ArrayList<TrainTimeInfo>();
			if (timesObj != null && timesObj.size() > 0) {
				for (int i = 0; i < timesObj.size(); i++) {
					TrainTimeInfo temp = new TrainTimeInfo();
					Integer isChangeValue = Integer.valueOf(String
							.valueOf(timesObj.getJSONObject(i).get(
									"isChangeValue")));

					JSONObject obj = JSONObject.fromObject(timesObj
							.getJSONObject(i));
					Integer index = Integer.valueOf(String.valueOf(obj
							.getString("index")));
					String planTrainStnId = String.valueOf(obj
							.getString("planTrainStnId"));
					// Integer stnSort
					// =Integer.valueOf(String.valueOf(obj.getString("stnSort")));
					String stnName = String.valueOf(obj.getString("stnName"));
					String nodeId = String.valueOf(obj.getString("nodeId"));
					String nodeName = String.valueOf(obj.getString("nodeName"));
					String bureauId = String.valueOf(obj.getString("bureauId"));
					String bureauShortName = String.valueOf(obj
							.getString("bureauShortName"));
					String stnBureauFull = String.valueOf(obj
							.getString("stnBureauFull"));
					String arrTrainNbr = String.valueOf(obj
							.getString("arrTrainNbr"));
					String arrTime = String.valueOf(obj.getString("arrTime"));
					String baseArrTime = String.valueOf(obj
							.getString("baseArrTime"));
					Integer arrRunDays = Integer.valueOf(String.valueOf(obj
							.getString("arrRunDays")));
					String dptTrainNbr = String.valueOf(obj
							.getString("dptTrainNbr"));
					String dptTime = String.valueOf(obj.getString("dptTime"));
					String baseDptTime = String.valueOf(obj
							.getString("baseDptTime"));
					Integer runDays = Integer.valueOf(String.valueOf(obj
							.getString("runDays")));
					String trackName = String.valueOf(obj
							.getString("trackName"));
					Integer platForm = "".equals(obj.getString("platForm"))?null:Integer.valueOf(obj.getString("platForm"));
					String jobs = String.valueOf(obj.getString("jobs"));
					String stepStr = String.valueOf(obj.getString("stepStr"));
					String stationFlag = String.valueOf(obj
							.getString("stationFlag"));
					Integer isChangeValue2 = Integer.valueOf(String.valueOf(obj
							.getString("isChangeValue")));
					String nodeStationId = String.valueOf(obj
							.getString("nodeStationId"));
					String nodeStationName = String.valueOf(obj
							.getString("nodeStationName"));
					String nodeTdcsId = String.valueOf(obj
							.getString("nodeTdcsId"));
					String nodeTdcsName = String.valueOf(obj
							.getString("nodeTdcsName"));
					
					JSONObject ky = JSONObject.fromObject(obj.getString("kyyy"));
					String kyyy = String.valueOf(ky
							.getString("text"));
					if("是".equals(kyyy)){
						if(!jobs.contains(ConstantUtil.KY_STATION_FLAG_NO)){
							jobs = jobs + ConstantUtil.KY_STATION_FLAG;
						}
					}
					else{
						jobs = jobs.replace(ConstantUtil.KY_STATION_FLAG, "");
					}
					if (("BTZ".equals(temp.getStationFlag()) ||"TZ".equals(temp.getStationFlag())) && !jobs.contains("经由")) {// SFZ:始发站
						// BTZ：不停站
						// TZ：停站
						jobs = "<经由>" + jobs;
					}
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date1 = sdf.parse(arrTime);
					Date date2 = sdf.parse(dptTime);

					temp.setPlanTrainStnId(planTrainStnId);
					temp.setStnSort(index);
					temp.setStnName(stnName);
					temp.setNodeId(nodeId);
					temp.setNodeName(nodeName);
					temp.setBureauId(bureauId);
					temp.setBureauShortName(bureauShortName);
					temp.setStnBureauFull(stnBureauFull);
					temp.setArrTrainNbr(arrTrainNbr);
					if (isChangeValue.equals(1)) {
						temp.setBaseArrTime(arrTime);
						temp.setBaseDptTime(dptTime);
					} else {
						temp.setBaseArrTime(baseArrTime.equals("--") ? ""
								: baseArrTime);
						temp.setBaseDptTime(baseDptTime.equals("--") ? ""
								: baseDptTime);
					}
					temp.setArrTime(arrTime);
					temp.setArrRunDays(arrRunDays);
					temp.setDptTrainNbr(dptTrainNbr);
					temp.setDptTime(dptTime);
					temp.setRunDays(runDays);
					temp.setTrackName(trackName);
					temp.setPlatForm(platForm);
					temp.setJobs(jobs);

					// stnType 车站类型（1:始发站；2:终到站；4:分界口; ）
					if ("SFZ".equals(temp.getStationFlag())) {// SFZ:始发站 ZDZ：终到站
																// FJK：分界口
																// BTZ：不停站 TZ：停站
						// temp.setArrTime(temp.getDptTime());
						temp.setStnType("1");
					} else if ("ZDZ".equals(temp.getStationFlag())) {// SFZ:始发站
																		// ZDZ：终到站
																		// FJK：分界口
																		// BTZ：不停站
																		// TZ：停站
						// temp.setDptTime(temp.getArrTime());
						temp.setStnType("2");
					} else if ("FJK".equals(temp.getStationFlag())) {// SFZ:始发站
																		// ZDZ：终到站
																		// FJK：分界口
																		// BTZ：不停站
																		// TZ：停站
						temp.setStnType("4");
					}

					if (i != 0 && i != timesObj.size() - 1) {
						if (stationFlag.equals("SFZ")) {
							if (date2.getTime() > date1.getTime()) {
								stationFlag = "TZ";
							} else {
								stationFlag = "BTZ";
							}

						}
						if (stationFlag.equals("ZDZ")) {
							if (date2.getTime() > date1.getTime()) {
								stationFlag = "TZ";
							} else {
								stationFlag = "BTZ";
							}
							temp.setStnType("");
						}
					}
					if (i == 0) {
						startStnNew = stnName;// 始发站
						stationFlag = "SFZ";
						temp.setStnType("1");
					} else if (i == timesObj.size() - 1) {
						stationFlag = "ZDZ";
						temp.setStnType("2");
						endStnNew = stnName;// 终到站

					}
					temp.setStationFlag(stationFlag);
					temp.setNodeStationId(nodeStationId);
					temp.setNodeStationName(nodeStationName);
					temp.setNodeTdcsId(nodeTdcsId);
					temp.setNodeTdcsName(nodeTdcsName);

					list.add(temp);
				}
			}

			// 根据开始日期、 截至日期、运行规律生成rundate字符串
			List<ModifyInfo> modifys = new ArrayList<ModifyInfo>();
			if (runDates != null && runDates.size() > 0) {
				for (String date : runDates) {
					logger.info("进入遍历runDates>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					// trainNbr
					List<PlanTrainDTOForModify> trainPlanDtoList = runPlanService
							.findPlanInfoByNBrAndRunDate(trainNbr,
									DateUtil.parseStringDateTOyyyymmdd(date),planTrainId,null);
					PlanTrainDTOForModify trainPlanDto = new PlanTrainDTOForModify();
					if (null != trainPlanDtoList && !trainPlanDtoList.isEmpty()) {
						trainPlanDto = trainPlanDtoList.get(0);
					}
					if (null != trainPlanDto) {
						ModifyInfo modifyInfo = new ModifyInfo();

						modifyInfo
								.setPlanModifyId(UUID.randomUUID().toString());
						modifyInfo
								.setPlanTrainId(trainPlanDto.getPlanTrainId());
						modifyInfo
								.setPlanCrossId(trainPlanDto.getPlanCrossId());
						modifyInfo.setCrossName(trainPlanDto.getCrossName());
						modifyInfo.setRunDate(trainPlanDto.getRunDate());
						modifyInfo.setTrainNbr(trainPlanDto.getTrainNbr());
						// 0:调整时刻；1：调整经路；2：停运；3：启动备用
						modifyInfo.setModifyType(1 + "");
						modifyInfo.setModifyReason(telName);
						modifyInfo.setStartDate(startDate);
						;
						modifyInfo.setEndDate(endDate);
						modifyInfo.setRule(runRule);
						modifyInfo.setSelectedDate(selectedDate + "");
						modifyInfo.setModifyContent(trainPlanDto.getTrainNbr()
								+ "车次在" + trainPlanDto.getRunDate() + "调整经路");
						modifyInfo.setModifyTime(new Date());
						modifyInfo.setModifyPeople(user.getName());
						modifyInfo.setModifyPeopleOrg(user.getBureauFullName());
						modifyInfo.setModifyPeopleBureau(user
								.getBureauShortName());
						// 修改plan_cross：check_type=0
						if ("0".equals(trainPlanDto.getCreatType())
								|| "1".equals(trainPlanDto.getCreatType())) {
							int updateCheckType = crossService
									.updatePlanCrossCheckType(
											trainPlanDto.getPlanCrossId(), 0);
							logger.info("修改plan_cross的check_type=0，成功条数:::"
									+ updateCheckType);
							int updatePlanCheckHisFlag = modifyPlanService
									.updatePlanCheckHisFlag(
											trainPlanDto.getPlanCrossId(), "1");
							logger.info("修改plan_check的check_his_flag=1，成功条数:::"
									+ updatePlanCheckHisFlag);
						}
						// 临客
						else if ("2".equals(trainPlanDto.getCreatType())) {
							int updatePlanCheckHisFlag = modifyPlanService
									.updatePlanCheckHisFlagCmd(
											trainPlanDto.getTelId(), "1");
							logger.info("修改plan_check的check_his_flag=1，成功条数:::"
									+ updatePlanCheckHisFlag);
						} else if ("3".equals(trainPlanDto.getCreatType())) {
							int updatePlanCheckHisFlag = modifyPlanService
									.updatePlanCheckHisFlagCmd(
											trainPlanDto.getCmdTrainId(), "1");
							logger.info("修改plan_check的check_his_flag=1，成功条数:::"
									+ updatePlanCheckHisFlag);
						}
						int updateTrainPlanSentBuearu = modifyPlanService
								.updateTrainPlanSentBuearu(trainPlanDto
										.getPlanTrainId());
						logger.info("修改plan_train的sent_buearu=null，成功条数:::"
								+ updateTrainPlanSentBuearu);
						modifys.add(modifyInfo);
					} else {
						result.setCode(StaticCodeType.SYSTEM_DATA_ISNULL
								.getCode());
						result.setMessage(StaticCodeType.SYSTEM_DATA_ISNULL
								.getDescription());
						return result;
					}
				}
			}
			// 向modify表中插入数据
			logger.info("modifys参数=============" + modifys.size());
			logger.info("modifys参数=============" + modifys.toString());
			int addModifyCount = modifyPlanService.addModifyList(modifys);
			logger.info("批量 add modify 成功~~count==" + addModifyCount);

			// 保存数据
			int count = trainTimeService.updatePlanLineTrainTimes(runDates,
					telName, reqObj.getString("trainNbr"), startStn, endStn,
					list);
			logger.info("批量修改开行计划成功~~count==" + count);

			int updateCount = trainTimeService.updatePlanLineTrain(startStnNew,
					endStnNew, planTrainId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 修改停运
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editPlanLineTrainStop", method = RequestMethod.POST)
	public Result editPlanLineTrainStop(@RequestBody String reqStr) {
		Result result = new Result();
		// 操作类型，停转开true,开转挺false
		boolean operationType = false;
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			
			if(reqObj.containsKey("type")){
				if(StringUtils.equals(reqObj.getString("type"), "2")){
					// 转开行
					operationType = true;
				}
			}
			
			String telName = reqObj.getString("telName");
			String startDate = reqObj.getString("startDate");
			String endDate = reqObj.getString("endDate");
			String runRule = reqObj.getString("runRule");// 每日、隔日、择日
			String selectedDate = reqObj.getString("selectedDate");// 择日时，包含的日期yyyy-mm-dd
			String startStn = reqObj.getString("startStn");
			String endStn = reqObj.getString("endStn");
			String trainNbr = reqObj.getString("trainNbr");
			String stopType = reqObj.getString("stopType");
			String planTrainId = reqObj.getString("planTrainId");
			String planCrossId = reqObj.getString("planCrossId");
			// 请求参数校验
			if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)
					|| StringUtil.isEmpty(runRule) || StringUtil.isEmpty(startStn)
					|| StringUtil.isEmpty(endStn) || StringUtil.isEmpty(trainNbr)
					|| StringUtil.isEmpty(stopType)) {
				result.setCode("-1");
				result.setMessage("无效的请求参数");
				return result;
			}

			// 根据开始日期、 截至日期、运行规律生成rundate字符串
			List<String> runDates = this.getRunDates(
					reqObj.getString("startDate"), reqObj.getString("endDate"),
					reqObj.getString("runRule"),
					reqObj.getString("selectedDate"));

			// (12 停运的； 34备用的) 
			if (StringUtils.equals(stopType, "2") || StringUtils.equals(stopType, "4")) {
				// 整组停运 or 整组开行
				List<String> idList = runPlanService.getAllTrain(
						reqObj.getString("trainNbr"), runDates,planCrossId);
				if (null != idList) {
					// idList根据ID处理
					return updTrainTyOrKxBystopType(stopType, idList, null,
							trainNbr,telName,startDate,endDate,runRule,selectedDate,operationType,planTrainId,planCrossId);
				}
			} else if (StringUtils.equals(stopType, "1") || StringUtils.equals(stopType, "3")) {
				// 单列停运
				return updTrainTyOrKxBystopType(stopType, null, runDates,
						trainNbr,telName,startDate,endDate,runRule,selectedDate,operationType,planTrainId,planCrossId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 列车的停运以及开行.
	 * 
	 * @param stopType
	 * @param idList
	 * @param dates
	 */
	public Result updTrainTyOrKxBystopType(String stopType,
			List<String> idList, List<String> runDates, String trainNbr,
			String telName, String startDate, String endDate, String runRule,
			String selectedDate,boolean operationType,String planTrainId,String planCrossId) {
		Result result = new Result();
		// 成功更改了多少条记录.
		Integer returnI = 0;
		// 总共需要更改多少条记录.
		Integer countI = 0;
		// 用户信息
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		// 状态,默认停运
		int spareFlag = 9;
		if(StringUtils.equals(stopType, "3") || StringUtils.equals(stopType, "4")){
			spareFlag = 2;
		}
		if(operationType){
			// 转开行.
			spareFlag = 1;
		}
		// PS:无论转什么，需要更新的数据全部都要置为初始值，所以不需要修改下面的代码.
		
		// modifyInfo
		List<ModifyInfo> modifys = new ArrayList<ModifyInfo>();
		
		Integer oldSpareFlag = 1;
		if(spareFlag == 1){
			if(StringUtils.equals(stopType, "1") || StringUtils.equals(stopType, "2")){
				oldSpareFlag = 9;
			}else if(StringUtils.equals(stopType, "3") || StringUtils.equals(stopType, "4")){
				oldSpareFlag = 2;
			}
		}
		
		if(org.apache.commons.lang.StringUtils.isEmpty(planCrossId) || org.apache.commons.lang.StringUtils.equals(planCrossId, "null")){
			planCrossId = null;
		}
		
		// 单列停运or单列备用
		if (StringUtils.equals(stopType, "1") || StringUtils.equals(stopType, "3")) {
			if (runDates != null && runDates.size() > 0) {
				for (String date : runDates) {
					countI += 1;
					int updI = trainTimeService.updateSpareFlagPlanTrainNbrAndRunday1(spareFlag, trainNbr,DateUtil.parseStringDateTOyyyymmdd(date),oldSpareFlag,planTrainId,planCrossId);
					if(updI > 0){
						returnI += 1;
						List<PlanTrainDTOForModify> trainPlanDtoList = runPlanService
								.findPlanInfoByNBrAndRunDate(trainNbr,
										DateUtil.parseStringDateTOyyyymmdd(date),planTrainId,planCrossId);
						PlanTrainDTOForModify trainPlanDto = null;
						if (null != trainPlanDtoList && !trainPlanDtoList.isEmpty()) {
							trainPlanDto = trainPlanDtoList.get(0);
							// list modifyInfo
							modifys.add(updPlanCrossOrPlanCheck(trainPlanDto, user,
									stopType, trainNbr, telName, startDate,
									endDate, runRule, selectedDate,spareFlag));
						}
					}
				}
				// 向modify表中插入数据
				if (!modifys.isEmpty()) {
					modifyPlanService.addModifyList(modifys);
				}
			}
		} else if (StringUtils.equals(stopType, "2") || StringUtils.equals(stopType, "4")) {
			// 整组停运or整组备用
			Map<String, Object> params = new HashMap<String, Object>();
			countI = idList.size();
			for (int i = 0; i < idList.size(); i++) {
				params.put("plan_train_id", idList.get(i));
				params.put("oldSpareFlag", oldSpareFlag);
				List<Map<String, Object>> lm = runPlanService
						.getTrainByMap(params);
				if (!lm.isEmpty()) {
					returnI +=1;
					// new trainPlanDto
					PlanTrainDTOForModify trainPlanDto = new PlanTrainDTOForModify(
							lm.get(0));
					// list modifyInfo
					modifys.add(updPlanCrossOrPlanCheck(trainPlanDto, user,
							stopType, trainNbr, telName, startDate, endDate,
							runRule, selectedDate,spareFlag));
				}
			}
			if(returnI > 0){
				// add modifyInfo
				modifyPlanService.addModifyList(modifys);
				// upd plan_train
				trainTimeService.batchUpdPlanTrain(idList, spareFlag,oldSpareFlag);
			}
		} 
		if(returnI == 0){
			result.setCode(StaticCodeType.SYSTEM_DATA_STATUS_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_DATA_STATUS_ERROR.getDescription());
		}else if(returnI > 0 ){
			if(returnI < countI){
				// 只成功修改了部分
				result.setCode(StaticCodeType.SYSTEM_DATA_BUFEN.getCode());
				result.setMessage(StaticCodeType.SYSTEM_DATA_BUFEN.getDescription());
			}else if(returnI == countI){
				// 全部都成功修改
				result.setCode(StaticCodeType.SYSTEM_SUCCESS.getCode());
				result.setMessage(StaticCodeType.SYSTEM_SUCCESS.getDescription());
			}
		}
		return result;
	}
	
	public ModifyInfo updPlanCrossOrPlanCheck(
			PlanTrainDTOForModify trainPlanDto, ShiroRealm.ShiroUser user,
			String stopType, String trainNbr, String telName, String startDate,
			String endDate, String runRule, String selectedDate,
			Integer spareFlag) {
		// new modifyInfo
		ModifyInfo modifyInfo = new ModifyInfo(trainPlanDto, user, stopType,
				spareFlag);
		modifyInfo.setPlanModifyId(UUID.randomUUID().toString());
		modifyInfo.setModifyReason(telName);
		modifyInfo.setStartDate(startDate);
		modifyInfo.setEndDate(endDate);
		modifyInfo.setRule(runRule);
		modifyInfo.setSelectedDate(selectedDate + "");
		modifyInfo.setModifyTime(new Date());

		// 修改plan_cross：check_type=0
		if ("0".equals(trainPlanDto.getCreatType())
				|| "1".equals(trainPlanDto.getCreatType())) {
			// CHECK_TYPE:审核状态（0:未审核1:部分局审核2:途经局全部审核）
			int updateCheckType = crossService.updatePlanCrossCheckType(
					trainPlanDto.getPlanCrossId(), 0);
			// logger.info("修改plan_cross的check_type=0，成功条数:::" +
			// updateCheckType);
			// CHECK_HIS_FLAG:历史审核标记（0:当前；1:历史）
			int updatePlanCheckHisFlag = modifyPlanService
					.updatePlanCheckHisFlag(trainPlanDto.getPlanCrossId(), "1");
			// logger.info("修改plan_check的check_his_flag=1，成功条数:::"
			// + updatePlanCheckHisFlag);
		}
		// 临客
		else if ("2".equals(trainPlanDto.getCreatType())) {
			int updatePlanCheckHisFlag = modifyPlanService
					.updatePlanCheckHisFlagCmd(trainPlanDto.getTelId(), "1");
			// logger.info("修改plan_check的check_his_flag=1，成功条数:::"
			// + updatePlanCheckHisFlag);
		} else if ("3".equals(trainPlanDto.getCreatType())) {
			int updatePlanCheckHisFlag = modifyPlanService
					.updatePlanCheckHisFlagCmd(trainPlanDto.getCmdTrainId(),
							"1");
			// logger.info("修改plan_check的check_his_flag=1，成功条数:::"
			// + updatePlanCheckHisFlag);
		}

		// SENT_BUREAU:运行线落成路局（简称）
		int updateTrainPlanSentBuearu = modifyPlanService
				.updateTrainPlanSentBuearu(trainPlanDto.getPlanTrainId());
		// logger.info("修改plan_train的sent_buearu=null，成功条数:::"
		// + updateTrainPlanSentBuearu);

		return modifyInfo;
	}

	/**
	 * 启用备用
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/trainRunBakToUse", method = RequestMethod.POST)
	public Result trainRunBakToUse(@RequestBody String reqStr) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		// 启用个辣子,当spareFlag=1的时候,修改备用永远不可能成功,真不知道你咋想的.
		// int spareFlag = 1;//启用备用(把备用车变成开行)
		Result result = new Result();
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String telName = reqObj.getString("telName");
			String startDate = reqObj.getString("startDate");
			String endDate = reqObj.getString("endDate");
			String runRule = reqObj.getString("runRule");// 每日、隔日、择日
			String selectedDate = reqObj.getString("selectedDate");// 择日时，包含的日期yyyy-mm-dd
			String startStn = reqObj.getString("startStn");
			String endStn = reqObj.getString("endStn");
			String trainNbr = reqObj.getString("trainNbr");
			String stopType = reqObj.getString("stopType");
			String planTrainId = reqObj.getString("planTrainId");
			String planCrossId = reqObj.getString("planCrossId");
boolean operationType = false;
			if(reqObj.containsKey("type")){
				if(StringUtils.equals(reqObj.getString("type"), "2")){
					// 停运转开行
					operationType = true;
				}
			}
			
			// 请求参数校验
			if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)
					|| StringUtil.isEmpty(runRule)
					|| StringUtil.isEmpty(startStn)
					|| StringUtil.isEmpty(endStn)
					|| StringUtil.isEmpty(trainNbr)) {
				result.setCode("-1");
				result.setMessage("无效的请求参数");
				return result;
			}

			// 根据开始日期、 截至日期、运行规律生成rundate字符串
			List<String> runDates = this.getRunDates(startDate, endDate,
					runRule, selectedDate);
			
			// 停运类型
						if (StringUtils.equals(stopType, "2")) {
							// 整组
							List<String> idList = runPlanService.getAllTrain(
									reqObj.getString("trainNbr"), runDates,planCrossId);
							if (null != idList) {
								// idList根据ID处理
								return updTrainTyOrKxBystopType("4", idList, null,
										trainNbr,telName,startDate,endDate,runRule,selectedDate,operationType,planTrainId,planCrossId);
							}
						} else if (StringUtils.equals(stopType, "1")) {
							// 单列
							return updTrainTyOrKxBystopType("3", null, runDates,
									trainNbr,telName,startDate,endDate,runRule,selectedDate,operationType,planTrainId,planCrossId);
						}
			
			// 根据开始日期、 截至日期、运行规律生成rundate字符串
			List<ModifyInfo> modifys = new ArrayList<ModifyInfo>();
			if (runDates != null && runDates.size() > 0) {
				for (String date : runDates) {
					// trainNbr
					List<PlanTrainDTOForModify> trainPlanDtoList = runPlanService
							.findPlanInfoByNBrAndRunDate(trainNbr,
									DateUtil.parseStringDateTOyyyymmdd(date),planTrainId,planCrossId);
					PlanTrainDTOForModify trainPlanDto = new PlanTrainDTOForModify();
					if (null != trainPlanDtoList && !trainPlanDtoList.isEmpty()) {
						trainPlanDto = trainPlanDtoList.get(0);
					}
					ModifyInfo modifyInfo = new ModifyInfo();

					modifyInfo.setPlanModifyId(UUID.randomUUID().toString());
					modifyInfo.setPlanTrainId(trainPlanDto.getPlanTrainId());
					modifyInfo.setPlanCrossId(trainPlanDto.getPlanCrossId());
					modifyInfo.setCrossName(trainPlanDto.getCrossName());
					modifyInfo.setRunDate(trainPlanDto.getRunDate());
					modifyInfo.setTrainNbr(trainPlanDto.getTrainNbr());
					// 0:调整时刻；1：调整经路；2：停运；3：启动备用
					modifyInfo.setModifyType(3 + "");
					modifyInfo.setModifyReason(telName);
					modifyInfo.setStartDate(startDate);
					;
					modifyInfo.setEndDate(endDate);
					modifyInfo.setRule(runRule);
					modifyInfo.setSelectedDate(selectedDate + "");
					modifyInfo.setModifyContent(trainPlanDto.getTrainNbr()
							+ "车次在" + trainPlanDto.getRunDate() + "启动备用");
					modifyInfo.setModifyTime(new Date());
					modifyInfo.setModifyPeople(user.getName());
					modifyInfo.setModifyPeopleOrg(user.getBureauFullName());
					modifyInfo.setModifyPeopleBureau(user.getBureauShortName());
					// 修改plan_cross：check_type=0
					if ("0".equals(trainPlanDto.getCreatType())
							|| "1".equals(trainPlanDto.getCreatType())) {
						int updateCheckType = crossService
								.updatePlanCrossCheckType(
										trainPlanDto.getPlanCrossId(), 0);
						logger.info("修改plan_cross的check_type=0，成功条数:::"
								+ updateCheckType);
						int updatePlanCheckHisFlag = modifyPlanService
								.updatePlanCheckHisFlag(
										trainPlanDto.getPlanCrossId(), "1");
						logger.info("修改plan_check的check_his_flag=1，成功条数:::"
								+ updatePlanCheckHisFlag);
					}
					// 临客
					else if ("2".equals(trainPlanDto.getCreatType())) {
						int updatePlanCheckHisFlag = modifyPlanService
								.updatePlanCheckHisFlagCmd(
										trainPlanDto.getTelId(), "1");
						logger.info("修改plan_check的check_his_flag=1，成功条数:::"
								+ updatePlanCheckHisFlag);
					} else if ("3".equals(trainPlanDto.getCreatType())) {
						int updatePlanCheckHisFlag = modifyPlanService
								.updatePlanCheckHisFlagCmd(
										trainPlanDto.getCmdTrainId(), "1");
						logger.info("修改plan_check的check_his_flag=1，成功条数:::"
								+ updatePlanCheckHisFlag);
					}
					int updateTrainPlanSentBuearu = modifyPlanService
							.updateTrainPlanSentBuearu(trainPlanDto
									.getPlanTrainId());
					logger.info("修改plan_train的sent_buearu=null，成功条数:::"
							+ updateTrainPlanSentBuearu);
					modifys.add(modifyInfo);
				}
			}
			// 向modify表中插入数据
			int addModifyCount = modifyPlanService.addModifyList(modifys);
			logger.info("批量 add modify 成功~~count==" + addModifyCount);

			logger.info("批量启动备用~~trainNbr==" + trainNbr + " /runDates=="
					+ runDates);
			// 保存数据
			int counts = 0;
			for (String runDay : runDates) {
				runDay = DateUtil.parseStringDateTOyyyymmdd(runDay);
				int count = trainTimeService
						.updateSpareFlagPlanTrainNbrAndRunday(1, trainNbr,
								runDay);
				counts = counts + count;
			}
			// int count = trainTimeService.updatePlanLineTrainTimes(runDates,
			// telName, reqObj.getString("trainNbr"), startStn, endStn, list);
			logger.info("批量启动备用成功~~count==" + counts);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据开始日期、 截至日期、运行规律生成rundate字符串
	 * 
	 * @param startDate
	 *            yyyy-MM-dd
	 * @param endDate
	 *            yyyy-MM-dd
	 * @param runRule
	 *            规律 {"code": "1", "text": "每日"},{"code": "2", "text":
	 *            "隔日"},{"code": "3", "text": "择日"}
	 * @param selectedDate
	 *            当规律为‘择日’时 起用
	 * @return "'20140915','20140916','20140917','20140918'"
	 * @throws ParseException
	 */
	private List<String> getRunDates(String startDate, String endDate,
			String runRule, String selectedDate) throws ParseException {
		List<String> dataList = new ArrayList<String>();// 避免空指针异常
		int dayStep = 0;
		if ("1".equals(runRule)) {// "每日"
			dayStep = -1;
		} else if ("2".equals(runRule)) {// "隔日"
			dayStep = -2;
		}

		if (dayStep != 0) {
			dataList = DateUtil.getDateStrBetweenStartToEnd(startDate, endDate,
					dayStep);
		} else {// 解析selectedDate生成
			CmdInfoModel model = new CmdInfoModel();
			// 择日
			model.setSelectedDate(selectedDate);
			List<String> selectedDateStrList = ConstantUtil
					.getSelectedDateStrList(model);
			if (selectedDateStrList != null) {
				logger.info("selectedDateStrList============="
						+ selectedDateStrList.size());
				logger.info("selectedDateStrList============="
						+ selectedDateStrList.toString());
				return selectedDateStrList;
			}
		}
		logger.info("dataList=============" + dataList.size());
		logger.info("dataList=============" + dataList.toString());
		return dataList;
	}

	/**
	 * 修改运行线的列车运行时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateSpareFlag", method = RequestMethod.POST)
	public Result updateSpareFlag(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("updateSpareFlag~~reqStr==" + reqMap);
		String spareFlag = StringUtil.objToStr(reqMap.get("spareFlag"));
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		try {

			int count = trainTimeService
					.updateSpareFlag(spareFlag, planTrainId);

			logger.info("updateSpareFlag~~count==" + count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据plant_train_id从基本图库中查询列车时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainTimeInfoByPlanTrainId", method = RequestMethod.POST)
	public Result getTrainTimeInfoByPlanTrainId(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getTrainTimeInfoByPlanTrainId~~reqMap==" + reqMap);
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		try {

			List<BaseCrossTrainInfoTime> list = trainTimeService
					.getTrainTimeInfoByPlanTrainId(planTrainId);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			// 列车信息
			List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
			Map<String, Object> crossMap = new HashMap<String, Object>();
			// 用于纵坐标的经由站列表
			List<Station> listStation = new ArrayList<Station>();
			// 横坐标的开始日期
			String arrDate = "";
			// 横坐标的结束日期
			String dptDate = "";
			if (list != null && list.size() > 0) {
				// 设置列车信息
				TrainInfoDto dto = new TrainInfoDto();
				dto.setTrainName(trainNbr);
				List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();

				// 循环经由站
				for (int i = 0; i < list.size(); i++) {

					BaseCrossTrainInfoTime subInfo = list.get(i);
					PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
					stnDtoStart.setArrTime(subInfo.getArrTime());
					stnDtoStart.setDptTime(subInfo.getDptTime());
					// stnDtoStart.setStayTime(subInfo.getStayTime());
					stnDtoStart.setStnName(subInfo.getStnName() + "["
							+ subInfo.getBureauShortName() + "]");
					stnDtoStart.setStationType(subInfo.getStationType());
					trainStns.add(stnDtoStart);
					// 获取起始站的到站日期和终到站的出发日期为横坐标的日期段
					if (i == 0) {
						String arrTime = subInfo.getArrTime();
						String dptTime = subInfo.getDptTime();
						arrDate = DateUtil.format(DateUtil.parseDate(
								arrTime == null || "".equals(arrTime) ? dptTime
										: arrTime, "yyyy-MM-dd hh:mm:ss"),
								"yyyy-MM-dd");
						// 设置始发站
						dto.setStartStn(subInfo.getStnName() + "["
								+ subInfo.getBureauShortName() + "]");
					}
					if (i == list.size() - 1) {
						String arrTime = subInfo.getArrTime();
						String dptTime = subInfo.getDptTime();
						dptDate = DateUtil.format(DateUtil.parseDate(
								dptTime == null || "".equals(dptTime) ? arrTime
										: dptTime, "yyyy-MM-dd hh:mm:ss"),
								"yyyy-MM-dd");
						// 设置终到站
						dto.setEndStn(subInfo.getStnName() + "["
								+ subInfo.getBureauShortName() + "]");
					}
					// 纵坐标数据
					Station station = new Station();
					station.setStnName(subInfo.getStnName() + "["
							+ subInfo.getBureauShortName() + "]");
					station.setStationType(subInfo.getStationType());
					listStation.add(station);
				}
				dto.setTrainStns(trainStns);
				trains.add(dto);
			}
			crossMap.put("trains", trains);
			dataList.add(crossMap);
			PlanLineGrid grid = null;

			// 生成横纵坐标
			List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
			List<PlanLineGridX> listGridX = getPlanLineGridX(arrDate, dptDate);
			grid = new PlanLineGrid(listGridX, listGridY);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("myJlData", dataList);
			dataMap.put("gridData", grid);
			result.setData(dataMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 组装纵坐标
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<PlanLineGridY> getPlanLineGridY(List<Station> list) {
		// 纵坐标
		List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		if (list != null) {
			for (Station station : list) {
				// 0:默认的isCurrentBureau
				planLineGridYList.add(new PlanLineGridY(station.getStnName(),
						0, station.getStationType()));
			}
		}

		return planLineGridYList;
	}

	/**
	 * 组装横坐标
	 * 
	 * @param crossStartDate
	 *            交路开始日期，格式yyyy-MM-dd
	 * @param crossEndDate
	 *            交路终到日期，格式yyyy-MM-dd
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<PlanLineGridX> getPlanLineGridX(String crossStartDate,
			String crossEndDate) {

		// 横坐标
		List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>();

		/***** 组装横坐标 *****/
		LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseLocalDate(crossStartDate);
		LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseLocalDate(crossEndDate));
		while (!start.isAfter(end)) {
			gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
			start = start.plusDays(1);
		}
		return gridXList;
	}
}
