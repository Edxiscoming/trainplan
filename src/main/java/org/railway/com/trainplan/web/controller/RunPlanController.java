package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.constants.OperationConstants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.constants.TPResponseCode;
import org.railway.com.trainplan.common.utils.ClientUtil;
import org.railway.com.trainplan.common.utils.CommonUtil;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.SqlUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.ComparatorCrossRunPlanInfo;
import org.railway.com.trainplan.entity.CrossRunPlanInfo;
import org.railway.com.trainplan.entity.ModifyPlanDTO;
import org.railway.com.trainplan.entity.PlanCheckInfo;
import org.railway.com.trainplan.entity.PlanCross;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.entity.TrainNbrDwrInfo;
import org.railway.com.trainplan.entity.UnitCrossTrain;
import org.railway.com.trainplan.exceptions.ParamValidationException;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.MessageService;
import org.railway.com.trainplan.service.ModifyPlanService;
import org.railway.com.trainplan.service.PlanCheckService;
import org.railway.com.trainplan.service.PlanLineService;
import org.railway.com.trainplan.service.PlanLineService2;
import org.railway.com.trainplan.service.PlanTrainStnService;
import org.railway.com.trainplan.service.RunLineDataCheckService;
import org.railway.com.trainplan.service.RunLineDataRollBackService;
import org.railway.com.trainplan.service.RunPlanLkService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.SendMQMessageService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.UnitCrossTrainService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.service.dto.ParamDto;
import org.railway.com.trainplan.service.dto.PlanCrossDto;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.railway.passenger.transdispatch.operationplan.service.impl.OperationlanServiceImpl;
import com.railway.passenger.transdispatch.util.TimeUtils;

@Controller
@RequestMapping(value = "/runPlan")
public class RunPlanController {
	private static Log logger = LogFactory.getLog(RunPlanController.class
			.getName());
	@Autowired
	private RunPlanService runPlanService;

	@Autowired
	private SendMsgService sendMsgService;
	@Autowired
	private PlanTrainStnService planTrainStnService;

	@Autowired
	private RunPlanLkService runPlanLkService;

	@Autowired
	private CrossService crossService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private RunLineDataCheckService runLineDataCheckService;

	@Autowired
	private RunLineDataRollBackService runLineDataRollBackService;

	@Autowired
	private SendMQMessageService sendMQMessageService;

	@Autowired
	private PlanLineService planLineService;
	@Autowired
	private PlanLineService2 planLineService2;

	@Autowired
	private ModifyPlanService modifyService;

	@Autowired
	private ModifyPlanService modifyPlanService;

	@Autowired
	private PlanCheckService planCheckService;

	@Autowired
	private UnitCrossTrainService unitCrossTrainService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private OperationlanServiceImpl operationlanServiceImpl;

	@RequestMapping(method = RequestMethod.GET)
	public String runPlan() {
		return "runPlan/run_plan";
	}

	@RequestMapping(value = "/runPlanCreate", method = RequestMethod.GET)
	public ModelAndView runPlanCreate(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		if ("0".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_create");
		}
		if ("1".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_createGT");
		}
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/countPlan", method = RequestMethod.POST)
	public Result countPlan(@RequestBody Map<String, Object> params){
		Result result = new Result();
		try{
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("runDate", TimeUtils.date2String(new Date(), TimeUtils.YYYYMMDD));
			param.put("highlineFlag", 1);
			param.put("baseChartId",MapUtils.getString(params, "baseChartId"));
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("highData",operationlanServiceImpl.countByParams(param));
			param.put("highlineFlag", 0);
			data.put("commonData",operationlanServiceImpl.countByParams(param));
			result.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode("-1");
			result.setMessage("统计开行计划出错:" + e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/runPlanMain", method = RequestMethod.GET)
	public ModelAndView runPlanMain(
			@RequestParam(defaultValue = "") String train_type,
			@RequestParam(defaultValue = "") String baseChartId,
			ModelAndView modelAndView) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("runDate", TimeUtils.date2String(new Date(), TimeUtils.YYYYMMDD));
		param.put("highlineFlag", 1);
		param.put("baseChartId",baseChartId);
		modelAndView.addObject("highData",operationlanServiceImpl.countByParams(param));
		param.put("highlineFlag", 0);
		modelAndView.addObject("commonData",operationlanServiceImpl.countByParams(param));
		
		if ("0".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_main");
		}
		if ("1".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_main_GT");
		}
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}
	
	@RequestMapping(value = "/runPlanCreatePl", method = RequestMethod.GET)
	public ModelAndView runPlanCreatePl(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		if ("0".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_create_check");
		}
		if ("1".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_createGT_check");
		}
		modelAndView.addObject("train_type", train_type);
		modelAndView.addObject("total",10);
		modelAndView.addObject("success",10);
		modelAndView.addObject("fail",0);
		return modelAndView;
	}
	
	@RequestMapping(value = "/runPlanCreateCx", method = RequestMethod.GET)
	public ModelAndView runPlanCreateCx(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		if ("0".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_search");
		}
		if ("1".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_searchGT");
		}
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}
	
	@RequestMapping(value = "/runPlanModify", method = RequestMethod.GET)
	public ModelAndView runPlanModify(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) { 
		if ("0".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_modify");
		}
		if ("1".equals(train_type)) {
			modelAndView.setViewName("runPlan/run_plan_modifyGT");
		}
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}
	

	@RequestMapping(value = "/runPlanGt", method = RequestMethod.GET)
	public String runPlanGt() {
		return "runPlan/run_plan_gt";
	}

	@RequestMapping(value = "/runPlanLineCreate", method = RequestMethod.GET)
	public ModelAndView runPlanLineCreate(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {

		modelAndView.setViewName("runPlan/run_plan_line_create");
		modelAndView.addObject("train_type", train_type);
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		modelAndView.addObject("currentBureauShortName",
				user.getBureauShortName());
		return modelAndView;
	}

	@RequestMapping(value = "/runPlanHighLineCreate", method = RequestMethod.GET)
	public ModelAndView runPlanHighLineCreate(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {

		modelAndView.setViewName("runPlan/run_plan_highline_create");
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}

	/**
	 * 跳转到列车运行时刻表编辑页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/trainRunTimePage", method = RequestMethod.GET)
	public ModelAndView trainRunTimePage(HttpServletRequest request) {
		PlanTrainDto plan = runPlanService.findPlanByPlanIdForTrainTime(request
				.getParameter("trainPlanId"));
		String trainStnSource = "";
		if (plan != null) {
			if (plan.getBaseTrainId() == null
					|| "".equals(plan.getBaseTrainId().trim())) {
				trainStnSource = "KY";
			} else {
				trainStnSource = "JBT";
			}
		}
		// != null ? plan.getTelName() : "请填写调整依据！"
		return new ModelAndView("runPlan/train_runTime")
				.addObject("trainNbr", request.getParameter("trainNbr"))
				.addObject("trainPlanId", request.getParameter("trainPlanId"))
				.addObject("runDate", request.getParameter("runDate"))
				.addObject("telName", plan.getTelName())
				.addObject("startStn", plan.getStartStn())
				.addObject("endStn", plan.getEndStn())
				.addObject("trainStnSource", trainStnSource)// 查询时刻时数据来源 JBT:基本图
															// KY:客运 用于界面查询时刻
				.addObject("maxRunDate",
						DateUtil.formateDate(plan.getMaxRunDate()));// 该车次在开行计划数据表中最大开行日期（格式：yyyy-mm-dd）
	}

	/**
	 * 命令内容
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editCommand", method = RequestMethod.GET)
	public ModelAndView editCommand(HttpServletRequest request) {
		return new ModelAndView("runPlan/command").addObject("planTrainId",
				request.getParameter("planTrainId"));
	}

	/**
	 * 跳转到列车运行经路编辑页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pathwayPage", method = RequestMethod.GET)
	public ModelAndView pathwayPage(HttpServletRequest request) {
		PlanTrainDto plan = runPlanService.findPlanByPlanIdForTrainTime(request
				.getParameter("trainPlanId"));
		String trainStnSource = "";
		if (plan != null) {
			if (plan.getBaseTrainId() == null
					|| "".equals(plan.getBaseTrainId().trim())) {
				trainStnSource = "KY";
			} else {
				trainStnSource = "JBT";
			}
		}

		return new ModelAndView("runPlan/path_way")
				.addObject("trainNbr", request.getParameter("trainNbr"))
				.addObject("trainPlanId", request.getParameter("trainPlanId"))
				.addObject("runDate", request.getParameter("runDate"))
				.addObject("telName", plan.getTelName())
				.addObject("startStn", plan.getStartStn())
				.addObject("endStn", plan.getEndStn())
				.addObject("trainStnSource", trainStnSource)// 查询时刻时数据来源 JBT:基本图
															// KY:客运 用于界面查询时刻
				.addObject("maxRunDate",
						DateUtil.formateDate(plan.getMaxRunDate()));// 该车次在开行计划数据表中最大开行日期（格式：yyyy-mm-dd）
	}

	/**
	 * 跳转到列车停运编辑页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/trainRunTimeStop", method = RequestMethod.GET)
	public ModelAndView trainRunTimePageStop(HttpServletRequest request) {
		PlanTrainDto plan = runPlanService.findPlanByPlanIdForTrainTime(request
				.getParameter("trainPlanId"));
		String trainStnSource = "";
		if (plan.getBaseTrainId() == null
				|| "".equals(plan.getBaseTrainId().trim())) {
			trainStnSource = "KY";
		} else {
			trainStnSource = "JBT";
		}
		ModelAndView model = new ModelAndView();
		model.addObject("trainNbr", request.getParameter("trainNbr"))
				.addObject("trainPlanId", request.getParameter("trainPlanId"))
				.addObject("runDate", request.getParameter("runDate"))
				.addObject("telName", plan.getTelName())
				.addObject("startStn", plan.getStartStn())
				.addObject("endStn", plan.getEndStn())
				.addObject("trainStnSource", trainStnSource)
				.addObject("maxRunDate",
						DateUtil.formateDate(plan.getMaxRunDate()))
				.addObject("planCrossId", request.getParameter("planCrossId"));

		if (null != request.getParameter("type")) {
			if (StringUtils.equals(request.getParameter("type"), "2")) {
				// 停运转开行
				model.setViewName("runPlan/train_run_start");
			}
		} else {
			model.setViewName("runPlan/train_run_stop");
		}

		return model;
	}

	/**
	 * 启动备用
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/trainRunBakToUse", method = RequestMethod.GET)
	public ModelAndView trainRunBakToUse(HttpServletRequest request) {
		PlanTrainDto plan = runPlanService.findPlanByPlanIdForTrainTime(request
				.getParameter("trainPlanId"));
		String trainStnSource = "";
		if (plan.getBaseTrainId() == null
				|| "".equals(plan.getBaseTrainId().trim())) {
			trainStnSource = "KY";
		} else {
			trainStnSource = "JBT";
		}

		ModelAndView model = new ModelAndView();
		model.addObject("trainNbr", request.getParameter("trainNbr"))
				.addObject("trainPlanId", request.getParameter("trainPlanId"))
				.addObject("runDate", request.getParameter("runDate"))
				.addObject("telName", plan.getTelName())
				.addObject("startStn", plan.getStartStn())
				.addObject("endStn", plan.getEndStn())
				.addObject("trainStnSource", trainStnSource)
				// 查询时刻时数据来源 JBT:基本图
				// KY:客运 用于界面查询时刻
				.addObject("maxRunDate",
						DateUtil.formateDate(plan.getMaxRunDate()))
				.addObject("planCrossId", request.getParameter("planCrossId"));
		;// 该车次在开行计划数据表中最大开行日期（格式：yyyy-mm-dd）

		if (null != request.getParameter("type")) {
			if (StringUtils.equals(request.getParameter("type"), "2")) {
				// 停运转开行
				model.setViewName("runPlan/train_run_to_use");
			}
		} else {
			model.setViewName("runPlan/train_use_to_run");
		}
		return model;

	}

	/**
	 * 查看乘务信息跳转页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/trainCrewPage", method = RequestMethod.GET)
	public ModelAndView trainCrewPage(HttpServletRequest request) {
		return new ModelAndView("runPlan/train_crew").addObject("trainNbr",
				request.getParameter("trainNbr")).addObject("runDate",
				request.getParameter("runDate"));
	}

	@ResponseBody
	@RequestMapping(value = "/getRunPlans", method = RequestMethod.POST)
	public Result getRunPlans(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		try {
			List<RunPlanTrainDto> runPlans = runPlanService
					.getTrainRunPlans1(reqMap);
			result.setData(runPlans);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode("-1");
			result.setMessage("查询运行线出错:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 图定开行计划查询
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanCross", method = RequestMethod.POST)
	public Result getPlanCross(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if (user.getBureau() != null) {
				reqMap.put("currentBureau", user.getBureau());
			}

			List<PlanCrossDto> runPlans = runPlanService.getPlanCross(reqMap);

			// TODO 2015-6-3 17:11:44.
			// 1. 之前的审核状态根据主表字段来过滤.
			// 2. 现在根据哲哥的需求（20150601 各局问题（遗留）.doc
			// [第188]）主表字段已经无法满足，并且在不修改数据库结构的情况只能从日志表里过滤.
			// 3. 由于时间紧，所以就没再考虑在SQL里处理，直接在代码里速战速决.
			// 4. 我的想法：应该修改数据结构，在主表增加新需求所需要的字段来满足需求，不应该根据日志来过滤数据.
			List<PlanCrossDto> retList = new ArrayList<PlanCrossDto>();
			if (!runPlans.isEmpty() && null != reqMap.get("checkFlag")) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, Object>> checkList = new ArrayList<Map<String, Object>>();
				for (PlanCrossDto planCrossDto : runPlans) {
					if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkFlag"), "0")) {
						// 未审核
						map.put("planCrossId", planCrossDto.getPlanCrossId());
						map.put("checkBureau", user.getBureau());
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (checkList.isEmpty()) {
							retList.add(planCrossDto);
						}
					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkFlag"), "1")) {
						// 本局通过
						map.put("planCrossId", planCrossDto.getPlanCrossId());
						map.put("checkBureau", user.getBureau());
						map.put("checkState", 0);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty() && checkList.size() > 0) {
							retList.add(planCrossDto);
						}

					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkFlag"), "2")) {
						// 本局不通过
						map.put("planCrossId", planCrossDto.getPlanCrossId());
						map.put("checkBureau", user.getBureau());
						map.put("checkState", 1);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty() && checkList.size() > 0) {
							retList.add(planCrossDto);
						}
					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkFlag"), "3")) {
						// 任意不通过
						map.put("planCrossId", planCrossDto.getPlanCrossId());
						map.put("checkState", 1);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty() && checkList.size() > 0) {
							retList.add(planCrossDto);
						}
					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkFlag"), "4")) {
						// 全部通过
						map.put("planCrossId", planCrossDto.getPlanCrossId());
						map.put("checkState", 0);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty()) {
							// 全部通过，只判断日志表中的总数据;审核时，只能相关局审核所以不存在会有其他局审核的数据.
							if ((null != planCrossDto.getRelevantBureau() ? planCrossDto
									.getRelevantBureau().length() : 0) == checkList
									.size()) {
								retList.add(planCrossDto);
							}
						}
					}
				}
				result.setData(retList);
			} else {
				result.setData(runPlans);
			}

			// result.setData(runPlans);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode("-1");
			result.setMessage("查询运行线出错:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 得到修改的全部历史信息
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCheckPlan", method = RequestMethod.POST)
	public Result getCheckPlan(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));

		List<PlanCheckInfo> runPlans = runPlanService
				.getPlanCheckInfoForPlanCrossId(planCrossId);

		Map<String, String> maplj = new HashMap<String, String>();

		maplj.put("B", "哈");
		maplj.put("T", "沈");
		maplj.put("P", "京");
		maplj.put("V", "太");
		maplj.put("C", "呼");
		maplj.put("F", "郑");
		maplj.put("N", "武");
		maplj.put("Y", "西");
		maplj.put("K", "济");
		maplj.put("H", "上");
		maplj.put("G", "南");
		maplj.put("Q", "广");
		maplj.put("Z", "宁");
		maplj.put("W", "成");
		maplj.put("M", "昆");
		maplj.put("J", "兰");
		maplj.put("R", "乌");
		maplj.put("O", "青");

		if (runPlans.size() != 0) {
			for (PlanCheckInfo planCheckInfo : runPlans) {
				planCheckInfo.setCheckBureau(maplj.get(planCheckInfo
						.getCheckBureau()));
			}

		}

		// List<PlanCrossDto> runPlans = runPlanService.getCheckPlan(reqMap);
		result.setData(runPlans);

		return result;
	}

	/**
	 * 获取已审局.
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCheckPlan1", method = RequestMethod.POST)
	public Result getCheckPlan1(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
		List<PlanCheckInfo> runPlans = runPlanService
				.getPlanCheckInfoForPlanCrossId1(planCrossId);
		if (runPlans.size() != 0) {
			HashSet<String> hs = new HashSet<String>();
			for (int i = 0; i < runPlans.size(); i++) {
				PlanCheckInfo planCheckInfo = runPlans.get(i);
				if (!hs.add(planCheckInfo.getCheckBureau())) {
					runPlans.remove(planCheckInfo);
					i = i - 1;
				} else {
					planCheckInfo.setCheckBureau(LjUtil.getLjByNameBs(
							planCheckInfo.getCheckBureau(), 2));
				}
			}
		}
		result.setData(runPlans);

		return result;
	}

	/**
	 * 查看交路调整记录
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getModifyRecords", method = RequestMethod.POST)
	public Result getModifyRecords(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));

		List<ModifyPlanDTO> modifys = modifyService
				.getModifyRecordsByPlanCrossId(planCrossId);

		if (!modifys.isEmpty()) {
			List pdList1 = new ArrayList();
			List<ModifyPlanDTO> pdList = new ArrayList<ModifyPlanDTO>();
			for (int i = 0; i < modifys.size(); i++) {
				ModifyPlanDTO md = modifys.get(i);
				if (i == 0) {
					pdList.add(md);
				} else {
					/**
					 * 需求：哲哥.
					 * 
					 * 交路名、车次、调整局、调整类型、调整依据、起始日期、终止日期、规律、择日日期、调整内容、调整人、岗位，
					 * 这些字段内容都一样，把他们合并。
					 * 
					 * 注：zpd: 调整内容不一样
					 */
					if (StringUtils.equals(md.getCrossName(), modifys
							.get(i - 1).getCrossName())
							&& StringUtils.equals(md.getTrainNbr(), modifys
									.get(i - 1).getTrainNbr())
							&& StringUtils.equals(md.getModifyPeopleBureau(),
									modifys.get(i - 1).getModifyPeopleBureau())
							&& StringUtils.equals(md.getModifyType(), modifys
									.get(i - 1).getModifyType())
							&& StringUtils.equals(md.getModifyReason(), modifys
									.get(i - 1).getModifyReason())
							&& (md.getStartDate() != null ? md.getStartDate()
									.getTime() : 0) == (modifys.get(i - 1)
									.getStartDate() != null ? modifys
									.get(i - 1).getStartDate().getTime() : 0)
							&& (md.getEndDate() != null ? md.getEndDate()
									.getTime() : 0) == (modifys.get(i - 1)
									.getEndDate() != null ? modifys.get(i - 1)
									.getEndDate().getTime() : 0)
							&& StringUtils.equals(md.getRule(),
									modifys.get(i - 1).getRule())
							&& StringUtils.equals(md.getSelectedDate(), modifys
									.get(i - 1).getSelectedDate())
							// && StringUtils.equals(md.getModifyContent(),
							// modifys.get(i - 1).getModifyContent())
							&& StringUtils.equals(md.getModifyPeople(), modifys
									.get(i - 1).getModifyPeople())
							&& StringUtils.equals(md.getModifyPeopleOrg(),
									modifys.get(i - 1).getModifyPeopleOrg())) {
						pdList.add(md);
					} else {
						pdList1.add(pdList);
						pdList = new ArrayList<ModifyPlanDTO>();
						pdList.add(md);
					}
				}
			}
			pdList1.add(pdList);
			result.setData(pdList1);
			return result;

		}

		// List<PlanCrossDto> runPlans = runPlanService.getCheckPlan(reqMap);
		result.setData(modifys);
		return result;
	}

	/**
	 * 查看列车调整记录
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainModifyRecords", method = RequestMethod.POST)
	public Result getTrainModifyRecords(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		// 查看列车调整记录
		List<ModifyPlanDTO> modifys = modifyService
				.getModifyRecordsByPlanTrainId(planTrainId);

		// List<PlanCrossDto> runPlans = runPlanService.getCheckPlan(reqMap);
		result.setData(modifys);

		return result;
	}

	/**
	 * 显示整组.
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/showTrainGroup", method = RequestMethod.POST)
	public Result showTrainGroup(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();

		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		List<String> planTrainIdList = runPlanService
				.getAllTrainByPlanTrainId(planTrainId);
		reqMap.clear();
		reqMap.put("idList", planTrainIdList);
		List<PlanTrainDto> ptList = runPlanService.getTrainCfByMap(reqMap);
		result.setData(JSONArray.fromObject(ptList));
		// result.setData(ptList);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getCheckPlancmd", method = RequestMethod.POST)
	public Result getCheckPlancmd(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String checkCmdtel = StringUtil.objToStr(reqMap.get("checkCmdtel"));

		List<PlanCheckInfo> modifyRecords = runPlanService
				.getPlanCheckInfoForPlanCrossIdcmdtel(checkCmdtel);

		if (modifyRecords.size() != 0) {
			HashSet<String> hs = new HashSet<String>();
			for (int i = 0; i < modifyRecords.size(); i++) {
				PlanCheckInfo planCheckInfo = modifyRecords.get(i);
				if (!hs.add(planCheckInfo.getCheckBureau())) {
					modifyRecords.remove(planCheckInfo);
					i = i - 1;
				} else {
					planCheckInfo.setCheckBureau(LjUtil.getLjByNameBs(
							planCheckInfo.getCheckBureau(), 2));
				}
			}
		}

		// List<PlanCrossDto> runPlans = runPlanService.getCheckPlan(reqMap);
		result.setData(modifyRecords);

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getCheckPlancmd1", method = RequestMethod.POST)
	public Result getCheckPlancmd1(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String checkCmdtel = StringUtil.objToStr(reqMap.get("checkCmdtel"));

		List<PlanCheckInfo> modifyRecords = runPlanService
				.getPlanCheckInfoForPlanCrossIdcmdtel1(checkCmdtel);

		Map<String, String> maplj = new HashMap<String, String>();

		maplj.put("B", "哈");
		maplj.put("T", "沈");
		maplj.put("P", "京");
		maplj.put("V", "太");
		maplj.put("C", "呼");
		maplj.put("F", "郑");
		maplj.put("N", "武");
		maplj.put("Y", "西");
		maplj.put("K", "济");
		maplj.put("H", "上");
		maplj.put("G", "南");
		maplj.put("Q", "广");
		maplj.put("Z", "宁");
		maplj.put("W", "成");
		maplj.put("M", "昆");
		maplj.put("J", "兰");
		maplj.put("R", "乌");
		maplj.put("O", "青");

		if (modifyRecords.size() != 0) {
			for (PlanCheckInfo planCheckInfo : modifyRecords) {
				planCheckInfo.setCheckBureau(maplj.get(planCheckInfo
						.getCheckBureau()));
			}

		}

		// List<PlanCrossDto> runPlans = runPlanService.getCheckPlan(reqMap);
		result.setData(modifyRecords);

		return result;
	}

	/**
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePlanCrosses", method = RequestMethod.POST)
	public Result deletePlanCrosses(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String crossIds = StringUtil.objToStr(reqMap.get("planCrossIds"));
			if (null != crossIds && !"".equals(crossIds)) {
				String[] crossIdsArray = crossIds.split(",");
				if (null != crossIdsArray && crossIdsArray.length > 0) {
					// 用户信息
					// ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)
					// SecurityUtils
					// .getSubject().getPrincipal();
					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("modify_reason", "管理图定，删除数据");
					// map.put("modify_people", user.getName());
					// map.put("modify_people_org", user.getBureauFullName());
					// map.put("modify_people_bureau",
					// user.getBureauShortName());
					// map.put("crossIds",
					// SqlUtil.strArrayToList(crossIdsArray));
					// // add modify
					// modifyPlanService.addModifyList1(map);
					// 之前是可以选择性的删除1列车1跳线，现在是必须删除，所以去掉了判断.
					// boolean delLX = (boolean) reqMap.get("delLX");
					// if (delLX) {
					// del mtrainline
					runPlanService.deleteMTrainLineByPlanCrossId(crossIdsArray);
					// }
					// del plancross
					runPlanService.deletePlanCrossByPlanCorssIds(crossIdsArray);
				}
			}
		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 图定管理，打回add.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addDhPlanCrosses", method = RequestMethod.GET)
	public ModelAndView addDhPlanCrosses(HttpServletRequest request) {
		// 懒，复制的addDicRelaCrossPost.jsp
		return new ModelAndView("runPlan/dh_add")
				.addObject("startTime", request.getParameter("startTime"))
				.addObject("endTime", request.getParameter("endTime"))
				.addObject("planCrossIds",
						request.getParameter("planCrossIds").split(",")[0])
				.addObject("crossName",
						request.getParameter("crossName").split(",")[0])
				.addObject("type", request.getParameter("type"));
	}

	/**
	 * 图定管理，打回save.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveDhPlanCrosses", method = RequestMethod.POST)
	public Result saveDhPlanCrosses(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {

			String crossName = StringUtil.objToStr(reqMap.get("crossName"));
			// String userName = StringUtil.objToStr(reqMap.get("userName"));
			String userTel = StringUtil.objToStr(reqMap.get("userTel"));
			String userReason = StringUtil.objToStr(reqMap.get("userReason"));
			String startTime = StringUtil.objToStr(reqMap.get("startTime"));
			String endTime = StringUtil.objToStr(reqMap.get("endTime"));
			String crossIds = StringUtil.objToStr(reqMap.get("planCrossIds"));
			String type = StringUtil.objToStr(reqMap.get("type"));

			if (null != crossIds && !"".equals(crossIds)) {
				ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
						.getSubject().getPrincipal();
				PlanCheckInfo planCheckInfo = new PlanCheckInfo();
				planCheckInfo.setPlanCheckId(UUID.randomUUID().toString());
				planCheckInfo.setPlanCrossId(crossIds);
				planCheckInfo.setStartDate(startTime);
				planCheckInfo.setEndDate(endTime);
				planCheckInfo.setCheckBureau(user.getBureau());
				planCheckInfo.setCheckDept(user.getDeptName());
				planCheckInfo.setCheckPeople(user.getName());
				planCheckInfo.setCheckPeopleTel(userTel);
				planCheckInfo.setCheckRejectReason(userReason);
				planCheckInfo.setCheckState("1");
				int count = 0;
				if (StringUtils.equals(type, "1")) {
					// 图定
					count = runPlanService.savePlanCheckInfo(planCheckInfo);
					if (count > 0) {
						runPlanService.updateCheckTypeForPlanCrossId(crossIds,
								3);
					}
				} else if (StringUtils.equals(type, "2")) {
					count = runPlanService.savePlanCheckInfoCmd(planCheckInfo);
					if (count > 0) {
						// 页面返回
						runPlanService.updateCheckTypeForCmdId(crossIds, 3);
					}
				}
				// int count =
				// runPlanService.savePlanCheckInfoCmd(planCheckInfo);
				// int count = runPlanService.savePlanCheckInfo(planCheckInfo);
				// if (count > 0) {
				// // CHECK_TYPE = 3; 打回
				// // 页面返回
				// runPlanService.updateCheckTypeForPlanCrossId(crossIds, 3);
				// // plan_check --> 历史
				// }
			} else {
				result.setCode(StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				result.setMessage(StaticCodeType.SYSTEM_PARAM_LOST
						.getDescription());
			}
		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 审核交路
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCrossRunLine", method = RequestMethod.POST)
	public Result checkCrossRunLine(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("checkCrossRunLine==" + reqMap);
		try {
			String planCrossIds = StringUtil.objToStr(reqMap
					.get("planCrossIds"));
			// 计划审核起始时间（格式：yyyymmdd）
			String startDate = StringUtil.objToStr(reqMap.get("startTime"));
			// 计划审核终止时间（格式：yyyymmdd）
			String endDate = StringUtil.objToStr(reqMap.get("endTime"));
			// 相关局局码
			// String relevantBureau =
			// StringUtil.objToStr(reqMap.get("relevantBureau"));
			List<Map<String, Object>> planCrossIdList = new ArrayList<Map<String, Object>>();

			if (planCrossIds != null && planCrossIds.length() > 0) {
				String[] crossIdAndBureaus = planCrossIds.split(";");
				for (String crossIdAndBureau : crossIdAndBureaus) {
					String[] crossIdBureaus = crossIdAndBureau.split("#");

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("check_his_flag", 0);
					map.put("plan_cross_id", crossIdBureaus[0]);
					List<Map<String, Object>> lm = planCheckService
							.getPlanCheckByMap(map);
					if (!lm.isEmpty()) {
						// 当前局是否有过 通过 操作
						boolean isS = false;
						// 当前局是否有过 不通过 操作
						boolean isN = false;
						for (int i = 0; i < lm.size(); i++) {
							if (StringUtils.equals(crossIdBureaus[2],
									(String) lm.get(i).get("CHECK_BUREAU"))) {
								isS = true;
								if (StringUtils.equals("1", (String) lm.get(i)
										.get("CHECK_STATE"))) {
									isN = true;
								}
								break;
							}
						}
						if (isS) {
							if (!isN) {
								result.setCode(StaticCodeType.SYSTEM_ERR_YS
										.getCode());
								result.setMessage("本局已审核！");
								return result;
							}
						}
						// } else {
						// result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
						// result.setMessage(StaticCodeType.SYSTEM_DATA_ISNULL
						// .getDescription());
						// return result;
					}

					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("planCrossId", crossIdBureaus[0]);
					// if (StringUtils.equals(crossIdBureaus[3], "1")) {
					// map.put("highlineFlag", 1);
					// }
					// List<PlanCrossDto> runPlans = runPlanService
					// .getPlanCross(map);
					// if (null != runPlans && !runPlans.isEmpty()) {
					// String checkedBureau = runPlans.get(0)
					// .getCheckedBureau() != null ? runPlans.get(0)
					// .getCheckedBureau() : "";
					// if (checkedBureau.indexOf(crossIdBureaus[2]) > -1) {
					// result.setCode(StaticCodeType.SYSTEM_ERR_YS
					// .getCode());
					// result.setMessage("本局已审核！");
					// return result;
					// }
					// } else {
					// result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
					// result.setMessage(StaticCodeType.SYSTEM_DATA_ISNULL
					// .getDescription());
					// return result;
					// }

					map.clear();
					map.put("planCrossId", crossIdBureaus[0]);
					if (StringUtils.equals(crossIdBureaus[3], "1")) {
						map.put("highlineFlag", 1);
					}
					map.put("relevantBureau", crossIdBureaus[1]);
					planCrossIdList.add(map);
				}
			}

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if (planCrossIdList != null && planCrossIdList.size() > 0) {
				for (Map<String, Object> map : planCrossIdList) {
					PlanCheckInfo planCheckInfo = new PlanCheckInfo();
					String planCrossId = (String) map.get("planCrossId");
					planCheckInfo.setPlanCheckId(UUID.randomUUID().toString());
					planCheckInfo.setPlanCrossId((String) map
							.get("planCrossId"));
					planCheckInfo.setStartDate(startDate);
					planCheckInfo.setEndDate(endDate);
					planCheckInfo.setCheckBureau(user.getBureau());
					planCheckInfo.setCheckDept(user.getDeptName());
					planCheckInfo.setCheckPeople(user.getName());
					planCheckInfo.setCheckState("0");

					int count = runPlanService.savePlanCheckInfo(planCheckInfo);

					logger.info("checkCrossRunLine~~~~count==" + count);
					String relevantBureau = (String) map.get("relevantBureau");
					// 根据planCrossid查询planCheckinfo对象
					Map<String, String> map1 = new HashMap<String, String>();
					map1.put("planCrossId", planCrossId);
					map1.put("his", "0");
					List<PlanCheckInfo> lp = runPlanService
							.getPlanCheckCountByIDHis(map1);
					if (lp != null && lp.size() > 0) {
						Integer checkType = null;
						// checkType
						List<Map<String, Object>> lm = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!lm.isEmpty()) {
							for (int i = 0; i < lm.size(); i++) {
								if (StringUtils.equals("1", (String) lm.get(i)
										.get("CHECK_STATE"))) {
									checkType = 3;
									break;
								}
							}
						}
						if (relevantBureau.length() == lp.get(0).getCount()) {
							// 途经局已经全部审核
							runPlanService.updateCheckTypeForPlanCrossId(
									planCrossId, checkType != null ? checkType
											: 2);
						} else {
							// 部分局已经审核
							runPlanService.updateCheckTypeForPlanCrossId(
									planCrossId, checkType != null ? checkType
											: 1);
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/checkLkRunLine", method = RequestMethod.POST)
	public Result checkLkRunLine(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("checkCrossRunLine==" + reqMap);
		try {
			String planCrossIds = StringUtil.objToStr(reqMap
					.get("planCrossIds"));

			String[] a = planCrossIds.split(",");

			// 计划审核起始时间（格式：yyyymmdd）
			String startDate = StringUtil.objToStr(reqMap.get("startTime"));
			// 计划审核终止时间（格式：yyyymmdd）
			String endDate = StringUtil.objToStr(reqMap.get("endTime"));
			// 相关局局码
			// String relevantBureau =
			// StringUtil.objToStr(reqMap.get("relevantBureau"));
			// List<Map<String, String>> planCrossIdList = new
			// ArrayList<Map<String, String>>();

			/*
			 * if (planCrossIds != null && planCrossIds.length() > 0) { String[]
			 * crossIdAndBureaus = planCrossIds.split(";"); for (String
			 * crossIdAndBureau : crossIdAndBureaus) { String[] crossIdBureaus =
			 * crossIdAndBureau.split("#"); Map<String, String> map = new
			 * HashMap<String, String>(); map.put("planCrossId",
			 * crossIdBureaus[0]); map.put("relevantBureau", crossIdBureaus[1]);
			 * planCrossIdList.add(map); } }
			 */

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();

			System.out.println(a);

			for (int i = 0; i < a.length; i++) {
				CmdTrain cmdlist = runPlanLkService
						.getCmdTrainInfoForCmdTrainId(a[i]);
				String passburu = "";
				if (cmdlist != null) {
					passburu = cmdlist.getPassBureau();
				}
				List<PlanCheckInfo> checklist = runPlanService
						.getPlanCheckInfoForPlanCrossIdcmdtel(a[i]);
				if (checklist.size() != 0) {

					if (passburu.trim().length() == checklist.size()) {
						// 途经局已经全部审核
						result.setCode("5");// 已经审核
						result.setMessage(cmdlist.getTrainNbr());
						return result;
					}
					for (PlanCheckInfo planCheckInfo : checklist) {
						if (planCheckInfo.getCheckBureau().equals(
								user.getBureau())) {
							result.setCode("3");// 已经审核
							result.setMessage(cmdlist.getTrainNbr());
							return result;
						}
					}

				}

			}

			/*
			 * PlanCheckInfo planCheckInfo = new PlanCheckInfo(); String
			 * planCrossId = planCrossIds;
			 * planCheckInfo.setPlanCheckId(UUID.randomUUID().toString());
			 * planCheckInfo.setPlanCrossId(planCrossIds);
			 * planCheckInfo.setStartDate(startDate);
			 * planCheckInfo.setEndDate(endDate);
			 * planCheckInfo.setCheckBureau(user.getBureau());
			 * planCheckInfo.setCheckDept(user.getDeptName());
			 * planCheckInfo.setCheckPeople(user.getName());
			 * 
			 * int count = runPlanService.savePlanCheckInfoCmd(planCheckInfo);
			 * 
			 * logger.info("checkCrossRunLine~~~~count==" + count); //String
			 * relevantBureau = map.get("relevantBureau"); //
			 * 根据planCrossid查询planCheckinfo对象 List<PlanCheckInfo> list =
			 * runPlanService
			 * .getPlanCheckInfoForPlanCrossIdcmdtel(planCrossId); if (list !=
			 * null && list.size() > 0) {
			 * 
			 * if (passburu.trim().length() == list.size()) { // 途经局已经全部审核
			 * runPlanService.updateCheckTypeForCmdId( planCrossId, 2); } else {
			 * // 部分局已经审核 runPlanService.updateCheckTypeForCmdId( planCrossId,
			 * 1); } result.setCode("1");//已经审核
			 * result.setMessage(cmdlist.getTrainNbr()); return result; }
			 */

		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/checkLkRunLine1", method = RequestMethod.POST)
	public Result checkLkRunLine1(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("checkCrossRunLine==" + reqMap);
		try {
			String planCrossIds = StringUtil.objToStr(reqMap
					.get("planCrossIds"));

			String[] a = planCrossIds.split(",");
			// Map<String, Object> map = new HashMap<String, Object>();
			// for (int i = 0; i < a.length; i++) {
			// map.put("cmdTrainId", a[0]);
			// if(StringUtils.equals(MapUtils.getString(reqMap,"highlineFlag"),
			// "1")){
			// map.put("highlineFlag", reqMap.get("highlineFlag"));
			// }
			// List<RunPlan> runPlans =
			// runPlanLkService.getPlanTrainLkInfo(map);
			// if(null != runPlans && !runPlans.isEmpty()){
			// if(runPlans.get(0).getCheckBureau().indexOf("") > -1){
			// result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			// result.setMessage("本局已审核！");
			// return result;
			// }
			// }
			// }

			// 计划审核起始时间（格式：yyyymmdd）
			String startDate = StringUtil.objToStr(reqMap.get("startTime"));
			// 计划审核终止时间（格式：yyyymmdd）
			String endDate = StringUtil.objToStr(reqMap.get("endTime"));

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();

			if (a.length <= 1) {
				for (int i = 0; i < a.length; i++) {

					if (a[i] != null || a[i] != "") {

						CmdTrain cmdlist = runPlanLkService
								.getCmdTrainInfoForCmdTrainId(a[i]);
						String passburu = "";
						if (cmdlist != null) {
							passburu = cmdlist.getPassBureau();
						}

						PlanCheckInfo planCheckInfo = new PlanCheckInfo();
						// String planCrossId = planCrossIds;
						planCheckInfo.setPlanCheckId(UUID.randomUUID()
								.toString());
						planCheckInfo.setPlanCrossId(a[i]);
						planCheckInfo.setStartDate(startDate);
						planCheckInfo.setEndDate(endDate);
						planCheckInfo.setCheckBureau(user.getBureau());
						planCheckInfo.setCheckDept(user.getDeptName());
						planCheckInfo.setCheckPeople(user.getName());

						int count = runPlanService
								.savePlanCheckInfoCmd(planCheckInfo);

						logger.info("checkCrossRunLine~~~~count==" + count);
						// String relevantBureau = map.get("relevantBureau");
						// 根据planCrossid查询planCheckinfo对象
						List<PlanCheckInfo> list = runPlanService
								.getPlanCheckInfoForPlanCrossIdcmdtel(a[i]);

						if (list != null && list.size() > 0) {

							if (passburu.trim().length() == list.size()) {
								// 途经局已经全部审核
								runPlanService.updateCheckTypeForCmdId(a[i], 2);
							} else {
								// 部分局已经审核
								runPlanService.updateCheckTypeForCmdId(a[i], 1);
							}

						}
						result.setMessage(cmdlist.getTrainNbr());
						result.setCode("1");

					}
				}

				return result;
			} else {

				for (int i = 0; i < a.length; i++) {
					CmdTrain cmdlist = runPlanLkService
							.getCmdTrainInfoForCmdTrainId(a[i]);
					String passburu = "";
					if (cmdlist != null) {
						passburu = cmdlist.getPassBureau();
					}

					PlanCheckInfo planCheckInfo = new PlanCheckInfo();
					// String planCrossId = planCrossIds;
					planCheckInfo.setPlanCheckId(UUID.randomUUID().toString());
					planCheckInfo.setPlanCrossId(a[i]);
					planCheckInfo.setStartDate(startDate);
					planCheckInfo.setEndDate(endDate);
					planCheckInfo.setCheckBureau(user.getBureau());
					planCheckInfo.setCheckDept(user.getDeptName());
					planCheckInfo.setCheckPeople(user.getName());

					int count = runPlanService
							.savePlanCheckInfoCmd(planCheckInfo);

					logger.info("checkCrossRunLine~~~~count==" + count);
					// String relevantBureau = map.get("relevantBureau");
					// 根据planCrossid查询planCheckinfo对象
					List<PlanCheckInfo> list = runPlanService
							.getPlanCheckInfoForPlanCrossIdcmdtel(a[i]);
					if (list != null && list.size() > 0) {

						if (passburu.trim().length() == list.size()) {
							// 途经局已经全部审核
							runPlanService.updateCheckTypeForCmdId(a[i], 2);
						} else {
							// 部分局已经审核
							runPlanService.updateCheckTypeForCmdId(a[i], 1);
						}

					}

				}

				result.setCode("1");
				return result;
			}

		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 按照交路生成开行计划(目前已经没有使用)
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/handleTrainLinesWithCross", method = RequestMethod.POST)
	public Result handleTrainLinesWithCross(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {

			String startDate = StringUtil.objToStr(reqMap.get("startDate"));
			String endDate = StringUtil.objToStr(reqMap.get("endDate"));
			String planCrossIds = StringUtil.objToStr(reqMap
					.get("planCrossIds"));
			logger.debug("startDate==" + startDate);
			logger.debug("endDate==" + endDate);
			logger.debug("planCrossIds==" + planCrossIds);
			if (planCrossIds != null && planCrossIds.length() > 0) {
				List<String> planCrossIdList = new ArrayList<String>();
				String[] planCrossIdsArray = planCrossIds.split(",");
				for (String planCrossId : planCrossIdsArray) {
					planCrossIdList.add(planCrossId);
				}
				List<ParamDto> listDto = runPlanService
						.getTotalTrainsForPlanCrossIds(startDate, endDate,
								planCrossIdList);
				if (listDto != null && listDto.size() > 0) {
					String jsonStr = commonService.combinationMessage(listDto);
					// 向rabbit发送消息
					amqpTemplate
							.convertAndSend("crec.event.trainplan", jsonStr);

				}
			}

		} catch (Exception e) {
			logger.error("handleTrainLines error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 生成开行计划
	 * 
	 * @return 正在生成计划的基本交路id
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/plantrain/gen", method = RequestMethod.POST)
	public Result generatePlanTrainBySchemaId(
			@RequestBody Map<String, Object> params) {
		// logger.debug("generatePlanTrainBySchemaId~~~reqMap = " + params);
		Result result = new Result();
		String baseChartId = MapUtils.getString(params, "baseChartId");
		String startDate = MapUtils.getString(params, "startDate");
		int days = MapUtils.getIntValue(params, "days");
		List<String> unitcrossId = (List<String>) params.get("unitcrossId");
		List<String> crossName = (List<String>) params.get("crossName");
		String msgReceiveUrl = MapUtils.getString(params, "msgReceiveUrl");
		logger.debug("msgReceiveUrl = " + msgReceiveUrl);
		logger.debug("unitcrossId = " + unitcrossId);
		// List<String> unitCrossIds =
		// runPlanService.generateRunPlan(baseChartId,
		// startDate, days, unitcrossId, msgReceiveUrl);
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		try {
			if (crossService.getCountfromUnitCrossNonCheck(crossName,
					baseChartId, user.getBureau()) > 0) {
				throw new ParamValidationException(
						TPResponseCode.GENERATE_UNITCROSS_ERROR.getCode(),
						"存在未审核交路，不能生成开行计划");
			}
	
			LocalDate lDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(DateUtil.format(new Date(), "yyyyMMdd")).plusYears(2);
			if(days <= 0 || days >= 50 || lDate.compareTo(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDate)) < 0) {
				throw new ParamValidationException(
						TPResponseCode.SYSTEM_PARAM_SCOPE_ERROR.getCode(),
						"交路生成日期非法，不能生成开行计划");
			}
			
		} catch (ParamValidationException e) {
			logger.error("generatePlanTrainBySchemaId error=="
					+ e.getErrorMsg());
			result.setCode(e.getErrorCode());
			result.setMessage(e.getErrorMsg());
			return result;
		} catch (Exception e) {
			logger.error("generatePlanTrainBySchemaId error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
			return result;
		}

		List<String> unitCrossIds = runPlanService.generateRunPlan(baseChartId,
				startDate, days, crossName, msgReceiveUrl);
		// return new ResponseEntity<List<String>>(unitCrossIds, HttpStatus.OK);
		result.setData(unitCrossIds);
		return result;
	}

	/**
	 * 
	 * @return 为生成运行线界面查询数据
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainRunPlansForCreateLine", method = RequestMethod.POST)
	public Result getTrainRunPlansForCreateLine(
			@RequestBody Map<String, Object> params) {
		Result result = new Result();
		String createType = StringUtil.objToStr(params.get("createType"));
		List trainPlans = null;
		try {
			QueryResult queryResult = null;
			// "创建方式 （0:基本图初始化；1:基本图滚动；2:文件电报；3:命令；4:人工添加）"
			if ("0".equals(createType)) {
				queryResult = runPlanService
						.getTrainRunPlansForCreateLine(params);

				// 需要对查询出来的数据,进行车序重组
				List<CrossRunPlanInfo> rowList = queryResult.getRows();
				if (!rowList.isEmpty()) {
					for (int j = 0; j < rowList.size(); j++) {
						// 对每一辆车的车序都进行重新赋值,因为无法得到其车序是否被更改过
						CrossRunPlanInfo crossRunPlanInfo = rowList.get(j);
						Map<String, TrainNbrDwrInfo> trainNbrMap = CommonUtil
								.getUnitCrossNameMap(crossRunPlanInfo
										.getCrossName());
						String trainNbr = crossRunPlanInfo.getTrainNbr();
						if (null != trainNbr && !"".equals(trainNbr)) {
							List<Integer> sorts = trainNbrMap.get(trainNbr)
									.getSorts();
							if (trainNbrMap.get(crossRunPlanInfo.getTrainNbr())
									.getTimes() > 1) {
								// 如果有重复的车次出现,进入判断，重置trainSort
								for (int i = 0; i < sorts.size(); i++) {
									if (sorts.get(i) == crossRunPlanInfo
											.getTrainSort()) {
										// 不做修改
										break;
									} else {
										if (i + 1 < sorts.size()) {
											if (sorts.get(i) < crossRunPlanInfo
													.getTrainSort()
													&& crossRunPlanInfo
															.getTrainSort() < sorts
															.get(i + 1)) {
												crossRunPlanInfo
														.setTrainSort(sorts
																.get(i));
												break;
											}
										}
									}

								}

							} else {
								crossRunPlanInfo.setTrainSort(sorts.get(0));
							}
						}
					}

					ComparatorCrossRunPlanInfo comparatorCrossRunPlanInfo = new ComparatorCrossRunPlanInfo();
					Collections.sort(rowList, comparatorCrossRunPlanInfo);
				}

			} else if ("3".equals(createType)) {
				queryResult = runPlanService.getTrainRunPlanForLk(params);
			}
			PagingResult page = new PagingResult(queryResult.getTotal(),
					queryResult.getRows());
			result.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @return 为生成运行线界面查询数据(图定生成)普通车
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/getTrainRunPlansForCreate", method = RequestMethod.POST)
	public Result getTrainRunPlansForCreate(
			@RequestBody Map<String, Object> params) {
		Result result = new Result();
		List<UnitCrossTrain> uctList = new ArrayList<UnitCrossTrain>();
		try {
			QueryResult queryResult = runPlanService
					.getTrainRunPlansForCreate(params);

			// 需要对查询出来的数据,进行车序重组
			List<CrossRunPlanInfo> rowList = queryResult.getRows();
			if (!rowList.isEmpty()) {
				for (int j = 0; j < rowList.size(); j++) {
					// 对每一辆车的车序都进行重新赋值,因为无法得到其车序是否被更改过
					CrossRunPlanInfo crossRunPlanInfo = rowList.get(j);
					if (StringUtils.isEmpty(crossRunPlanInfo.getRunDay())) {
						// 如果没有开行计划的，需要给一个集合中放入对应数据
						params.clear();
						params.put("unit_cross_id",
								crossRunPlanInfo.getUnitCrossId());
						uctList.addAll(unitCrossTrainService
								.getUnitCrossTrainByMap(params));
					}

					Map<String, TrainNbrDwrInfo> trainNbrMap = CommonUtil
							.getUnitCrossNameMap(crossRunPlanInfo
									.getCrossName());
					String trainNbr = crossRunPlanInfo.getTrainNbr();
					if (null != trainNbr && !"".equals(trainNbr)) {
						List<Integer> sorts = trainNbrMap.get(trainNbr)
								.getSorts();
						if (trainNbrMap.get(crossRunPlanInfo.getTrainNbr())
								.getTimes() > 1) {
							// 如果有重复的车次出现,进入判断，重置trainSort
							for (int i = 0; i < sorts.size(); i++) {
								if (sorts.get(i) == crossRunPlanInfo
										.getTrainSort()) {
									// 不做修改
									break;
								} else {
									if (i + 1 < sorts.size()) {
										if (sorts.get(i) < crossRunPlanInfo
												.getTrainSort()
												&& crossRunPlanInfo
														.getTrainSort() < sorts
														.get(i + 1)) {
											crossRunPlanInfo.setTrainSort(sorts
													.get(i));
											break;
										}
									}
								}

							}

						} else {
							crossRunPlanInfo.setTrainSort(sorts.get(0));
						}
					}
				}
				System.setProperty("java.util.Arrays.useLegacyMergeSort",
						"true");
				ComparatorCrossRunPlanInfo comparatorCrossRunPlanInfo = new ComparatorCrossRunPlanInfo();
				Collections.sort(rowList, comparatorCrossRunPlanInfo);
			}

			PagingResult page = new PagingResult(queryResult.getTotal(),
					queryResult.getRows());
			result.setData(page);
			if (!uctList.isEmpty()) {
				result.setData1(uctList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @return 为生成运行线界面查询数据(图定生成)高铁
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/getTrainRunPlansForCreateGT", method = RequestMethod.POST)
	public Result getTrainRunPlansForCreateGT(
			@RequestBody Map<String, Object> params) {
		Result result = new Result();
		List<UnitCrossTrain> uctList = new ArrayList<UnitCrossTrain>();
		try {
			QueryResult queryResult = runPlanService
					.getTrainRunPlansForCreateGT(params);
			// 需要对查询出来的数据,进行车序重组
			List<CrossRunPlanInfo> rowList = queryResult.getRows();
			if (!rowList.isEmpty()) {
				for (int j = 0; j < rowList.size(); j++) {
					// 对每一辆车的车序都进行重新赋值,因为无法得到其车序是否被更改过
					CrossRunPlanInfo crossRunPlanInfo = rowList.get(j);

					if (StringUtils.isEmpty(crossRunPlanInfo.getRunDay())) {
						// 如果没有开行计划的，需要给一个集合中放入对应数据
						params.clear();
						params.put("unit_cross_id",
								crossRunPlanInfo.getUnitCrossId());
						uctList.addAll(unitCrossTrainService
								.getUnitCrossTrainByMap(params));
					}

					Map<String, TrainNbrDwrInfo> trainNbrMap = CommonUtil
							.getUnitCrossNameMap(crossRunPlanInfo
									.getCrossName());
					String trainNbr = crossRunPlanInfo.getTrainNbr();
					if (null != trainNbr && !"".equals(trainNbr)) {
						List<Integer> sorts = trainNbrMap.get(trainNbr)
								.getSorts();
						if (trainNbrMap.get(crossRunPlanInfo.getTrainNbr())
								.getTimes() > 1) {
							// 如果有重复的车次出现,进入判断，重置trainSort
							for (int i = 0; i < sorts.size(); i++) {
								if (sorts.get(i) == crossRunPlanInfo
										.getTrainSort()) {
									// 不做修改
									break;
								} else {
									if (i + 1 < sorts.size()) {
										if (sorts.get(i) < crossRunPlanInfo
												.getTrainSort()
												&& crossRunPlanInfo
														.getTrainSort() < sorts
														.get(i + 1)) {
											crossRunPlanInfo.setTrainSort(sorts
													.get(i));
											break;
										}
									}
								}

							}

						} else {
							crossRunPlanInfo.setTrainSort(sorts.get(0));
						}
					}
				}

				ComparatorCrossRunPlanInfo comparatorCrossRunPlanInfo = new ComparatorCrossRunPlanInfo();
				Collections.sort(rowList, comparatorCrossRunPlanInfo);
			}

			PagingResult page = new PagingResult(queryResult.getTotal(),
					queryResult.getRows());
			result.setData(page);
			if (!uctList.isEmpty()) {
				result.setData1(uctList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 生成运行线（直接改数据库方式）
	 * 
	 * @return
	 */
	// @ResponseBody
	// @RequestMapping(value = "/createRunPlanForPlanTrain", method =
	// RequestMethod.POST)
	// public Result createRunPlanForPlanTrain(@RequestBody Map<String, Object>
	// params) {
	// Result result = new Result();
	// List<Map<String, String>> planTrains = (List<Map<String,
	// String>>)params.get("planTrains");
	// String msgReceiveUrl = (String)params.get("msgReceiveUrl");
	// //通过远程接口生成运行线的list
	// //List<ParamDto> listRemoteDto = new ArrayList<ParamDto>();
	// logger.debug("createRunPlanForPlanTrain~~~reqMap==" + params);
	// try{
	// for(Map<String, String> planTrain: planTrains){
	// String baseTrainId = planTrain.get("baseTrainId");
	// //调用本地接口直接操作数据库
	// //根据planTrainId从表plan_train和表plan_train_stn中查询基本的数据
	// String planTrainId = planTrain.get("planTrainId");
	// // 查询客运计划主体信息
	// Map<String, Object> plan =
	// runPlanService.findPlanInfoByPlanId(planTrainId);
	// PlanLineInfoDto planDto = new PlanLineInfoDto(plan);
	// MTrainLine mtrainLine = new MTrainLine();
	// String dailyPlanId = planDto.getDailyPlanId();
	// String dailyPlanIdLast = planDto.getDailyPlanIdLast();
	// String id = "";
	// if(dailyPlanId != null && !"".equals(dailyPlanId)){
	// id = planDto.getDailyPlanId();
	// mtrainLine.setId(id);
	// }
	// else if(dailyPlanIdLast != null && !"".equals(dailyPlanIdLast)) {
	// id = dailyPlanIdLast;
	// mtrainLine.setId(id);
	// }
	// else{
	// //主键id
	// id = UUID.randomUUID().toString();
	// mtrainLine.setId(id);
	// }
	//
	// mtrainLine.setHighLineFlag(planDto.getHighLineFlag());
	// if("1".equals(planDto.getHighLineFlag())) {
	// mtrainLine.setHighSpeed("T");
	// }
	// else {
	// mtrainLine.setHighSpeed("F");
	// }
	// mtrainLine.setName(planDto.getTrainName());
	// mtrainLine.setBusiness("客运");
	// mtrainLine.setTypeId(planDto.getTrainTypeId());
	// mtrainLine.setRouteId(planDto.getRouteId());
	// mtrainLine.setRouteName(planDto.getRouteName());
	// //途经局
	// mtrainLine.setRouteBureauShortNames(planDto.getPassBureau());
	// mtrainLine.setSourceBureauName(planDto.getStartBureauFull());
	// mtrainLine.setSourceBureauShortname(planDto.getStartBureau());
	// mtrainLine.setSourceNodeName(planDto.getStartStn());
	// mtrainLine.setSourceTime(planDto.getStartTime());
	// mtrainLine.setTargetBureauName(planDto.getEndBureauFull());
	// mtrainLine.setTargetBureauShortname(planDto.getEndBureau());
	// mtrainLine.setTargetNodeName(planDto.getEndStn());
	// mtrainLine.setTargetTime(planDto.getEndTime());
	// mtrainLine.setBusiness(planDto.getBusiness());
	// mtrainLine.setSourceBureauId(planDto.getStartBureauId());
	// mtrainLine.setSourceNodeId(planDto.getStartStnId());
	// mtrainLine.setTargetBureauId(planDto.getEndBureauId());
	// mtrainLine.setTargetNodeId(planDto.getEndStnId());
	// String endDays = planDto.getEndDays();
	// mtrainLine.setTargetTimeScheduleDates(Integer.valueOf(endDays==null ||
	// "".equals(endDays)?"0":planDto.getEndDays()));
	//
	// String createType = planDto.getCreatType();
	// //createType: 0:基本图 2:电报 3:命令
	// if("0".equals(createType)){
	// mtrainLine.setOrgin("基本图");
	// mtrainLine.setOrginId(baseTrainId);
	// mtrainLine.setOrginName(planDto.getTrainName());
	// }else if("2".equals(createType)){
	// mtrainLine.setOrgin("电报");
	// mtrainLine.setOrginId(planDto.getTelId());
	// mtrainLine.setOrginName(planDto.getTelShortInfo());
	// }else if("3".equals(createType)){
	// mtrainLine.setOrgin("命令");
	// mtrainLine.setOrginId(planDto.getCmdTxtmlItemId());
	// mtrainLine.setOrginName(planDto.getCmdShortInfo());
	// }
	//
	// //调用后台插入数据或更新数据
	// runPlanLkService.insertMTrainLine(mtrainLine);
	//
	// List<Map<String, Object>> plans;
	// if("0".equals(createType) && null != baseTrainId &&
	// !"".equals(baseTrainId) && !"null".equals(baseTrainId)) {
	// //查询子表的数据
	// plans = runPlanService.findPlanTimeTableByPlanIdFromjbt(planTrainId);
	//
	// }
	// else {
	// //查询子表的数据
	// plans = runPlanService.findPlanTimeTableByPlanId(planTrainId);
	// }
	//
	//
	//
	// for(int i = 0;i<plans.size();i++) {
	// Map<String, Object> map = plans.get(i);
	//
	// MTrainLineStn stn = new MTrainLineStn();
	// stn.setId(UUID.randomUUID().toString());
	// stn.setParentId(id);
	// stn.setNodeId(MapUtils.getString(map, "NODE_ID",""));
	// stn.setNodeName(MapUtils.getString(map, "NODE_NAME",""));
	// stn.setPlatformName("null".equals(MapUtils.getString(map, "PLATFORM",""))
	// ? null : MapUtils.getString(map, "PLATFORM",""));
	// stn.setSourceParentName(MapUtils.getString(map, "ARR_TRAIN_NBR",""));
	// stn.setTargetParentName(MapUtils.getString(map, "DPT_TRAIN_NBR",""));
	// stn.setBureauId(MapUtils.getString(map, "STN_BUREAU_ID",""));
	// //局全称
	// stn.setBureauName(MapUtils.getString(map, "BUREAU",""));
	// //局简称
	// stn.setBureauShortname(MapUtils.getString(map, "STNBUREAU",""));
	// stn.setChildIndex(MapUtils.getInteger(map, "STN_INDEX"));
	// //车站名
	// stn.setName(MapUtils.getString(map, "STN_NAME",""));
	// //车次
	// stn.setParentName(planDto.getTrainName());
	//
	// stn.setTrackName(MapUtils.getString(map, "TRACK_NAME",""));
	// if(map.containsKey("ARR_RUN_DAYS")) {
	// stn.setSourceTimeScheduleDates("null".equals(MapUtils.getString(map,
	// "ARR_RUN_DAYS","")) ? 0 : Integer.valueOf(MapUtils.getString(map,
	// "ARR_RUN_DAYS")));
	// }
	// else {
	// stn.setSourceTimeScheduleDates(0);
	// }
	//
	// if(map.containsKey("RUN_DAYS")) {
	// stn.setTargetTimeScheduleDates("null".equals(MapUtils.getString(map,
	// "RUN_DAYS","")) ? 0 : Integer.valueOf(MapUtils.getString(map,
	// "RUN_DAYS")));
	// }
	// else {
	// stn.setTargetTimeScheduleDates(0);
	// }
	// stn.setJobs(MapUtils.getString(map, "JOBS",""));
	//
	// //经由站
	// // if() {
	// //
	// // }
	// // else {
	// //
	// // }
	// stn.setSourceTime(CreateRunlineDataUtil
	// .getStringTime(planDto.getStartTime(), MapUtils.getString(map,
	// "ARR_TIME"), stn.getSourceTimeScheduleDates()));
	// stn.setTargetTime(CreateRunlineDataUtil
	// .getStringTime(planDto.getEndTime(), MapUtils.getString(map,
	// "ARR_TIME"), stn.getTargetTimeScheduleDates()));
	// runPlanLkService.insertMTrainLineStnRoute(stn);
	//
	// }
	//
	// Map<String,Object> reqMap = new HashMap<String,Object>();
	// reqMap.put("planTrainId", planTrainId);
	// reqMap.put("daylyPlanId",id );
	// //更新表plan_train中字段DAILYPLAN_FLAG值为0
	//
	// planTrainStnService.updatePlanTrainDaylyPlanFlag(reqMap);
	// //向页面推送信息
	// JSONObject jsonMsg = new JSONObject();
	// jsonMsg.put("planTrainId", planTrainId);
	// jsonMsg.put("createFlag", 1);
	// sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl,
	// "updateTrainRunPlanStatus");
	//
	// }
	// // if(listRemoteDto.size() > 0 ){
	// // System.err.println("listRemoteDto.size()====" + listRemoteDto.size());
	// // String jsonStr = commonService.combinationMessage(listRemoteDto);
	// // logger.debug("jsonStr====" + jsonStr);
	// // //向rabbit发送消息
	// // amqpTemplate.convertAndSend("crec.event.trainplan",jsonStr);
	// // }
	// //
	//
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	@SuppressWarnings("unchecked")
	/**
	 * 生成运行线（通过事件驱动中间件）
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createRunPlanForPlanTrain", method = RequestMethod.POST)
	public Result createRunPlanForPlanTrain(
			@RequestBody Map<String, Object> params) {

		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		Result result = new Result();
		// List<Map<String, String>> planTrains = (List<Map<String, String>>)
		// params.get("planTrains");
		// String msgReceiveUrl = (String) params.get("msgReceiveUrl");
		// String highlineFlag = (String) params.get("highlineFlag");
		// String startDate = (String) params.get("startDate");
		// String endDate = (String) params.get("endDate");
		// logger.debug("推送地址 url:" + msgReceiveUrl);
		// 通过远程接口生成运行线的list
		// List<ParamDto> listRemoteDto = new ArrayList<ParamDto>();
		// logger.debug("createRunPlanForPlanTrain~~~reqMap==" + params);

		params.put("bureau", user.getBureau());
		params.put("createPeople", user.getName());
		// String s = JSONObject.fromObject(params).toString();
		try {
			// http://10.1.186.140:8080/runlineService/create
			// http://127.0.0.1:9090/runlineService/create
			// 2015-6-29 16:31:32；由于plans_service从140服务器上更换至124/125服务器，所以更换地址.
//			ClientUtil.excuteRequest("http://10.1.186.140:8080/runlineService/create",JSONObject.fromObject(params).toString(),OperationConstants.REQUEST_METHOD.POST, 5000);
			// new
			ClientUtil.excuteRequest("http://10.1.186.124:8080/runlineService/create",JSONObject.fromObject(params).toString(),OperationConstants.REQUEST_METHOD.POST, 5000);
			// new new
//			ClientUtil.excuteRequest("http://localhost:8080/runlineService/create",JSONObject.fromObject(params).toString(),OperationConstants.REQUEST_METHOD.POST, 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// planLineService2.generateTrainLine(planTrains, msgReceiveUrl, user);

		// 初始化创建机调消息对象
		// JDMessage jdMessage = new JDMessage();
		// MessageHeader messageHeader = new MessageHeader();
		// MessageBody body = new MessageBody();
		// List<RunlineTrain> runlineTrainList = new ArrayList<RunlineTrain>();
		// messageHeader.setBureau(user.getBureauShortName());
		// messageHeader.setStartTime(startDate + " 00:00:00");
		// messageHeader.setEndTime(endDate + " 23:59:59");
		// messageHeader.setHandleTime(DateUtil.format(new Date(),
		// "yyyy-MM-dd HH:mm:ss"));
		// //线路类型
		// if("1".equals(highlineFlag)) {
		// //高铁
		// messageHeader.setLineType("1");
		// }
		// else {
		// //既有
		// messageHeader.setLineType("0");
		// }
		//
		// //消息类型，发布计划
		// messageHeader.setMessageType("1");
		// messageHeader.setUser(user.getName());
		//
		// jdMessage.setHeader(messageHeader);

		// try {
		// for (Map<String, String> planTrain : planTrains) {
		// String baseTrainId = planTrain.get("baseTrainId");
		// // 调用本地接口直接操作数据库
		// // 根据planTrainId从表plan_train和表plan_train_stn中查询基本的数据
		// String planTrainId = planTrain.get("planTrainId"); // 查询客运计划主体信息
		// Map<String, Object> plan = runPlanService
		// .findPlanInfoByPlanId(planTrainId);
		// PlanLineInfoDto planDto = new PlanLineInfoDto(plan);
		//
		// // 目的就是要构建这些实体，再转成json字符串
		// CreateRunLineData createRunLineData = new CreateRunLineData();
		// //MTrainLine mtrainLine = new MTrainLine();
		//
		// Origin origin = new Origin();
		//
		// TrainTypeModel trainTypeModel = null;
		// trainTypeModel = runPlanLkService.getTrainTypeForTypeId(planDto
		// .getTrainTypeId());
		//
		// RunlineModel runlineModel = new RunlineModel();
		//
		// String dailyPlanId = planDto.getDailyPlanId();
		// String dailyPlanIdLast = planDto.getDailyPlanIdLast();
		//
		// String id = "";
		// if (dailyPlanId != null && !"".equals(dailyPlanId)) {
		// id = dailyPlanId;
		// runlineModel.setId(id);
		// }
		// else if(dailyPlanIdLast != null && !"".equals(dailyPlanIdLast)) {
		// id = dailyPlanIdLast;
		// runlineModel.setId(id);
		// }
		// else {
		// // 主键id
		// id = UUID.randomUUID().toString();
		// runlineModel.setId(id);
		// }
		//
		// if("1".equals(planDto.getHighLineFlag())) {
		// runlineModel.setHighSpeed(true);
		// }
		// else {
		// runlineModel.setHighSpeed(false);
		// }
		// //runlineModel.setHighSpeed(false);
		// runlineModel.setType(trainTypeModel);
		// runlineModel.setName(planDto.getTrainName());
		//
		// // mtrainLine.setBusiness("客运");
		// // mtrainLine.setTypeId(planDto.getTrainTypeId());
		// // mtrainLine.setRouteId(planDto.getRouteId());
		// // mtrainLine.setRouteName(planDto.getRouteName()); // 途经局
		//
		// runlineModel.setRouteBureauShortNames(planDto.getPassBureau());
		// runlineModel.setSourceBureauName(planDto.getStartBureauFull());
		// runlineModel.setSourceBureauShortName(planDto.getStartBureau());
		// runlineModel.setSourceNodeName(planDto.getStartStn());
		//
		// runlineModel.setSourceTime(CreateRunlineDataUtil
		// .getMillisecondTime(planDto.getStartTime()));
		//
		// runlineModel.setTargetBureauName(planDto.getEndBureauFull());
		// runlineModel.setTargetBureauShortName(planDto.getEndBureau());
		// runlineModel.setTargetNodeName(planDto.getEndStn());
		//
		// runlineModel.setTargetTime(CreateRunlineDataUtil
		// .getMillisecondTime(planDto.getEndTime()));
		//
		// runlineModel.setSourceBureauId(planDto.getStartBureauId());
		// runlineModel.setSourceNodeId(planDto.getStartStnId());
		// runlineModel.setTargetBureauId(planDto.getEndBureauId());
		// runlineModel.setTargetNodeId(planDto.getEndStnId());
		//
		// runlineModel.setSourceTimeSchedule(CreateRunlineDataUtil
		// .getTimeSchedule(null, planDto.getStartTime()));
		// runlineModel.setTargetTimeSchedule(CreateRunlineDataUtil
		// .getTimeSchedule(planDto.getStartTime(),
		// planDto.getEndTime()));
		//
		// runlineModel.setLastTimeText(CreateRunlineDataUtil
		// .getLastTimeText(runlineModel.getTargetTimeSchedule()));
		// String createType = planDto.getCreatType();
		// // createType: 0:基本图 2:电报 3:命令
		// if ("0".equals(createType)) {
		// origin.setOrigin("基本图");
		// origin.setId(baseTrainId);
		// origin.setName(planDto.getTrainName());
		// } else if ("2".equals(createType)) {
		// origin.setOrigin("电报");
		// origin.setId(baseTrainId);
		// origin.setName(planDto.getTrainName());
		// } else if ("3".equals(createType)) {
		// origin.setOrigin("命令");
		// origin.setId(baseTrainId);
		// origin.setName(planDto.getTrainName());
		// }
		//
		// runlineModel.setOrigin(origin);
		//
		// String jstr = JSONObject.fromObject(runlineModel).toString();
		//
		// //logger.info("运行线数据： " + jstr);
		//
		//
		// //JD消息，拼装运行线信息
		// //RunlineTrain runlineTrain = new RunlineTrain();
		//
		//
		// //runlineTrainList.add(runlineTrain);
		// // 调用后台插入数据或更新数据
		// //runPlanLkService.insertMTrainLine(mtrainLine);
		//
		// // 查询子表的数据
		// //List<Map<String, Object>> plans = runPlanService
		// // .findPlanTimeTableByPlanId(planTrainId);
		// //String jstr1 = JSONArray.fromObject(plans).toString();
		//
		// // logger.info("运行线子项数据： " + jstr1);
		// // 查询子表的数据
		// List<Map<String, Object>> plans;
		// if("0".equals(createType) && null != baseTrainId &&
		// !"".equals(baseTrainId) && !"null".equals(baseTrainId)) {
		// //查询子表的数据
		// plans = runPlanService.findPlanTimeTableByPlanIdFromjbt(planTrainId);
		//
		// }
		// else {
		// //查询子表的数据
		// plans = runPlanService.findPlanTimeTableByPlanId(planTrainId);
		// }
		// List<RunLineItem> items = new ArrayList<RunLineItem>();
		// for (int i = 0; i < plans.size(); i++) {
		//
		// Map<String, Object> map = plans.get(i);
		//
		// Job job = new Job();
		// RunLineItem runLineItem = new RunLineItem();
		// job.setCode(MapUtils.getString(map, "JOBS", ""));
		// runLineItem.setJobs(job);
		// runLineItem.setId(UUID.randomUUID().toString());
		// runLineItem.setName(planDto.getTrainName() + "运行线条目" + i);
		// runLineItem.setBureauId(MapUtils.getString(map,
		// "STN_BUREAU_ID", ""));
		// runLineItem.setBureauName(MapUtils.getString(map, "BUREAU",
		// ""));
		// runLineItem.setBureauShortName(MapUtils.getString(map,
		// "STNBUREAU", ""));
		// runLineItem.setNodeId(MapUtils
		// .getString(map, "NODE_ID", ""));
		// runLineItem.setNodeName(MapUtils.getString(map,
		// "NODE_NAME", ""));
		// runLineItem.setTrackName(MapUtils.getString(map,
		// "TRACK_NAME", ""));
		// runLineItem.setPlatformName(MapUtils.getString(map,
		// "PLATFORM", ""));
		//
		// runLineItem.setSourceTimeSchedule(CreateRunlineDataUtil.getTimeSchedule(MapUtils.getString(map,
		// "ARR_TIME"),
		// ("null".equals(MapUtils.getString(map, "ARR_RUN_DAYS","")) ? 0 :
		// Integer.valueOf(MapUtils.getString(map,"ARR_RUN_DAYS")))));
		// runLineItem.setSourceTime(CreateRunlineDataUtil.getMillisecondTime1(planDto.getStartTime(),
		// MapUtils.getString(map,"ARR_TIME"),
		// runLineItem.getSourceTimeSchedule().getDates()));
		// runLineItem.setSourceParentName(MapUtils.getString(map,
		// "ARR_TRAIN_NBR"));
		// if("<终到>".equals(runLineItem.getJobs().getCode())) {
		// runLineItem.setTargetTimeSchedule(runLineItem.getSourceTimeSchedule());
		// runLineItem.setTargetTime(runLineItem.getSourceTime());
		// }
		// else {
		// runLineItem.setTargetTimeSchedule(CreateRunlineDataUtil.getTimeSchedule(MapUtils.getString(map,
		// "DPT_TIME"),
		// ("null".equals(MapUtils.getString(map, "RUN_DAYS","")) ? 0 :
		// Integer.valueOf(MapUtils.getString(map,"RUN_DAYS")))));
		// runLineItem.setTargetTime(CreateRunlineDataUtil.getMillisecondTime1(planDto.getStartTime(),
		// MapUtils.getString(map,"DPT_TIME"),
		// runLineItem.getTargetTimeSchedule().getDates()));
		// }
		//
		// runLineItem.setTargetParentName(MapUtils.getString(map,
		// "DPT_TRAIN_NBR"));
		// runLineItem.setParent(runlineModel);
		//
		// items.add(runLineItem);
		// // MTrainLineStn stn = new MTrainLineStn();
		// // stn.setId(UUID.randomUUID().toString());
		// // stn.setParentId(id);
		// // stn.setNodeId(MapUtils.getString(map, "NODE_ID", ""));
		// // stn.setNodeName(MapUtils.getString(map, "NODE_NAME", ""));
		// // stn.setPlatformName(MapUtils.getString(map, "PLATFORM", ""));
		// // stn.setSourceParentName(MapUtils.getString(map,
		// // "ARR_TRAIN_NBR", ""));
		// // stn.setTargetParentName(MapUtils.getString(map,
		// // "DPT_TRAIN_NBR", ""));
		// // stn.setBureauId(MapUtils
		// // .getString(map, "STN_BUREAU_ID", "")); // 局全称
		// // stn.setBureauName(MapUtils.getString(map, "BUREAU", ""));
		// // // 局简称
		// // stn.setBureauShortname(MapUtils.getString(map, "STNBUREAU",
		// // ""));
		// // stn.setChildIndex(MapUtils.getInteger(map, "STN_INDEX"));
		// // // 车站名 //
		// // stn.setName(MapUtils.getString(map, "STN_NAME", "")); // 车次
		// // stn.setParentName(planDto.getTrainName());
		// // stn.setTrackName(MapUtils.getString(map, "TRACK_NAME", ""));
		// //// stn.setSourceTimeScheduleDates(Integer.valueOf(MapUtils
		// //// .getString(map, "ARR_RUN_DAYS")));
		// //// stn.setTargetTimeScheduleDates(Integer.valueOf(MapUtils
		// //// .getString(map, "RUN_DAYS")));
		// // if(map.containsKey("ARR_RUN_DAYS")) {
		// //
		// stn.setSourceTimeScheduleDates("null".equals(MapUtils.getString(map,
		// "ARR_RUN_DAYS","")) ? 0 : Integer.valueOf(MapUtils.getString(map,
		// // "ARR_RUN_DAYS")));
		// // }
		// // else {
		// // stn.setSourceTimeScheduleDates(0);
		// // }
		// //
		// // if(map.containsKey("RUN_DAYS")) {
		// //
		// stn.setTargetTimeScheduleDates("null".equals(MapUtils.getString(map,
		// "RUN_DAYS","")) ? 0 : Integer.valueOf(MapUtils.getString(map,
		// // "RUN_DAYS")));
		// // }
		// // else {
		// // stn.setTargetTimeScheduleDates(0);
		// // }
		// // stn.setJobs(MapUtils.getString(map, "JOBS", "")); // 经由站
		// // stn.setSourceTime(MapUtils.getString(map, "ARR_TIME"));
		// // stn.setTargetTime(MapUtils.getString(map, "DPT_TIME"));
		// //runPlanLkService.insertMTrainLineStnRoute(stn);
		// }
		//
		// createRunLineData.setRunline(runlineModel);
		// createRunLineData.setItems(items);
		// String jstr2 = JSONObject.fromObject(createRunLineData)
		// .toString();
		//
		// //logger.info("运行线全部数据： " + jstr2);
		//
		// //判断是否需要先删除运行线
		// if(null != planDto.getDailyPlanId() &&
		// !"".equals(planDto.getDailyPlanId())) {
		// sendMQMessageService.sendMq(msgReceiveUrl, createRunLineData,
		// planTrainId, planDto.getDailyPlanId(), user);
		// }
		// else if(null != planDto.getDailyPlanIdLast() &&
		// !"".equals(planDto.getDailyPlanIdLast())) {
		// sendMQMessageService.sendMq(msgReceiveUrl, createRunLineData,
		// planTrainId, planDto.getDailyPlanIdLast(), user);
		// }
		// else {
		// sendMQMessageService.sendMq(msgReceiveUrl, createRunLineData,
		// planTrainId, user);
		// }
		//
		//
		// // Map<String,Object> reqMap = new HashMap<String,Object>();
		// // reqMap.put("planTrainId", planTrainId);
		// // reqMap.put("daylyPlanId",id );
		// //更新表plan_train中字段DAILYPLAN_FLAG值为0
		//
		// // planTrainStnService.updatePlanTrainDaylyPlanFlag(reqMap);
		// //
		// // //向页面推送信息
		// // JSONObject jsonMsg = new JSONObject();
		// // jsonMsg.put("planTrainId", planTrainId);
		// // jsonMsg.put("createFlag", 1);
		// // sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl,
		// "updateTrainRunPlanStatus");
		// }
		//
		// //// Map<String,Object> reqMap = new HashMap<String,Object>();
		// //// reqMap.put("planTrainId", planTrainId);
		// //// reqMap.put("daylyPlanId",id );
		// // //更新表plan_train中字段DAILYPLAN_FLAG值为0
		// //
		// // // planTrainStnService.updatePlanTrainDaylyPlanFlag(reqMap);
		// // //向页面推送信息
		// //// JSONObject jsonMsg = new JSONObject();
		// //// jsonMsg.put("planTrainId", planTrainId);
		// //// jsonMsg.put("createFlag", 1);
		// //// sendMsgService.sendMessage(jsonMsg.toString(), msgReceiveUrl,
		// "updateTrainRunPlanStatus");
		// ////
		// //// }
		// // if(listRemoteDto.size() > 0 ){
		// // System.err.println("listRemoteDto.size()====" +
		// listRemoteDto.size());
		// // String jsonStr = commonService.combinationMessage(listRemoteDto);
		// // logger.debug("jsonStr====" + jsonStr);
		// // //向rabbit发送消息
		// // amqpTemplate.convertAndSend("crec.event.trainplan",jsonStr);
		// // }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		return result;
	}

	/**
	 * 根据PLAN_TRAIN中PLAN_TRAIN_ID查询基本图列车时刻信息
	 * 
	 * @param planTrainId
	 *            (检索条件 必填)
	 * @param trainStnSource
	 *            (检索条件 必填)查询时刻时数据来源 JBT:基本图 KY:客运 用于界面查询时刻
	 * @author denglj
	 * @date 2014-10-27
	 */
	@ResponseBody
	@RequestMapping(value = "/trainStnByPlanTrainId", method = RequestMethod.POST)
	public Result jbtTrainStnByPlanTrainId(
			@RequestBody Map<String, Object> params) {
		Result result = new Result();
		try {
			if (params.get("planTrainId") == null
					|| "".equals(params.get("planTrainId"))) {
				result.setCode("-10");
				result.setMessage("请求参数无效");
				return result;
			}

			if (params.get("trainStnSource") == null
					|| (!"JBT".equals(params.get("trainStnSource")) && !"KY"
							.equals(params.get("trainStnSource")))) {
				result.setCode("-10");
				result.setMessage("请求参数无效");
				return result;
			}

			if ("JBT".equals(params.get("trainStnSource"))) {
				result.setData(runPlanService.jbtTrainStnByPlanTrainId(params
						.get("planTrainId").toString()));
			} else if ("KY".equals(params.get("trainStnSource"))) {
				result.setData(runPlanService.kyTrainStnByPlanTrainId(params
						.get("planTrainId").toString()));
			}
		} catch (Exception e) {
			logger.error("获取列车时刻信息出错", e);
			result.setCode("-1");
			result.setMessage("获取列车时刻信息出错");
		}

		return result;
	}

	/**
	 * 滚动.
	 * 
	 * @param reqMap
	 *            传递cross_id,type.
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateIsAutoGenerate", method = RequestMethod.POST)
	public Result updateIsAutoGenerate(@RequestBody Map<String, Object> reqMap) {
		Result result = null;
		try {
			String crossIds = StringUtil.objToStr(reqMap.get("planCrossIds"));
			// 1:滚动;0:停止滚动
			Integer type = Integer.parseInt("".equals(StringUtil
					.objToStr(reqMap.get("type"))) ? "0" : StringUtil
					.objToStr(reqMap.get("type")));
			if (crossIds != null) {
				String[] crossIdsArray = crossIds.split(",");
				int updI = runPlanService.updateModalRun(crossIdsArray, type);
				if (updI > 0) {
					result = new Result();
				}
			}
		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 管理临客,审核.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkLkRunLineNew", method = RequestMethod.POST)
	public Result checkLkRunLineNew(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String planCrossIds = StringUtil.objToStr(reqMap
					.get("planCrossIds"));
			// 计划审核起始时间（格式：yyyymmdd）
			String startDate = StringUtil.objToStr(reqMap.get("startTime"));
			// 计划审核终止时间（格式：yyyymmdd）
			String endDate = StringUtil.objToStr(reqMap.get("endTime"));
			String[] a = null;
			if (StringUtils.isNotEmpty(planCrossIds)) {
				a = planCrossIds.split(",");
			}
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();

			// 校验当前的审核状态.
			// result = runPlanLkService.checkCurrent(a, runPlanService,
			// user.getBureau());
			// if (StringUtils.endsWith(result.getCode(), "5")
			// || StringUtils.endsWith(result.getCode(), "3")) {
			// return result;
			// }

			for (int i = 0; i < a.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("check_his_flag", 0);
				map.put("cmd_tel_id", a[i]);
				List<Map<String, Object>> lm = planCheckService
						.getPlanCheckByMap(map);
				if (!lm.isEmpty()) {
					// 当前局是否有过 通过 操作
					boolean isS = false;
					// 当前局是否有过 不通过 操作
					boolean isN = false;
					for (int j = 0; j < lm.size(); j++) {

						if (StringUtils.equals(user.getBureau(), (String) lm
								.get(j).get("CHECK_BUREAU"))) {
							isS = true;
							if (StringUtils.equals("1",
									(String) lm.get(j).get("CHECK_STATE"))) {
								isN = true;
							}
							break;
						}
					}
					if (isS) {
						if (!isN) {
							result.setCode("5");
							result.setMessage("");
							return result;
						}
					}
					// } else {
					// result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
					// result.setMessage(StaticCodeType.SYSTEM_DATA_ISNULL
					// .getDescription());
					// return result;
				}
			}

			// 可以进行审核

			for (int i = 0; i < a.length; i++) {
				if (a[i] != null || a[i] != "") {
					CmdTrain cmdlist = runPlanLkService
							.getCmdTrainInfoForCmdTrainId(a[i]);
					String passburu = "";
					if (cmdlist != null) {
						passburu = cmdlist.getPassBureau();
					}

					// 保存新的记录
					PlanCheckInfo planCheckInfo = new PlanCheckInfo();
					planCheckInfo.setPlanCheckId(UUID.randomUUID().toString());
					planCheckInfo.setPlanCrossId(a[i]);
					planCheckInfo.setStartDate(startDate);
					planCheckInfo.setEndDate(endDate);
					planCheckInfo.setCheckBureau(user.getBureau());
					planCheckInfo.setCheckDept(user.getDeptName());
					planCheckInfo.setCheckPeople(user.getName());
					planCheckInfo.setCheckState("0");
					runPlanService.savePlanCheckInfoCmd(planCheckInfo);

					// 查询PlanCheckInfo,校验：途经局 = 已审局
					List<PlanCheckInfo> list = runPlanService
							.getPlanCheckInfoForPlanCrossIdcmdtel(a[i]);
					if (list != null && list.size() > 0) {
						Integer checkType = null;
						// checkType
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cmdTelId", a[i]);
						List<Map<String, Object>> lm = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!lm.isEmpty()) {
							for (int j = 0; j < lm.size(); j++) {
								if (StringUtils.equals("1", (String) lm.get(j)
										.get("CHECK_STATE"))) {
									checkType = 3;
									break;
								}
							}
						}

						if (passburu.trim().length() == list.size()) {
							// 途经局已经全部审核
							runPlanService.updateCheckTypeForCmdId(a[i],
									checkType != null ? checkType : 2);
						} else {
							// 部分局已经审核
							runPlanService.updateCheckTypeForCmdId(a[i],
									checkType != null ? checkType : 1);
						}
					}
					result.setMessage(cmdlist.getTrainNbr());
					result.setCode("1");
				}
			}

			//

		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 打开图定发送
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/runPlanSendMsg", method = RequestMethod.GET)
	public ModelAndView runPlanSendMsg(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String crossName = request.getParameter("crossName");
		if (null != crossName) {
			if (crossName.indexOf(",") > -1) {
				String[] str = crossName.split(",");
				for (int i = 0; i < str.length; i++) {
					sb.append(i+1).append(".").append(str[i]).append("\n");
				}
			} else {
				sb.append("1.").append(crossName);
			}
		}
		return new ModelAndView("runPlan/runplan_sendmsg")
				.addObject("crossIds", request.getParameter("crossIds"))
				.addObject("crossName", sb)
				.addObject("type", request.getParameter("type"));
	}

	/**
	 * 图定-发消息.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRunPlanSendMsg", method = RequestMethod.POST)
	public Result saveRunPlanSendMsg(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String crossIds = MapUtils.getString(reqMap, "crossIds");
		// 操作类型，新增？修改？
		String operType = MapUtils.getString(reqMap, "operType");
		// 备注
		String remark = MapUtils.getString(reqMap, "remark");
		// 普速？高铁
		String type = MapUtils.getString(reqMap, "type");

		// 消息内容：广州局新增图定开行计划，交路：……(相关局)，备注：……
		StringBuilder sb = new StringBuilder();
		String typeCode = "";
		if (null != type && null != operType && null != crossIds) {
			sb.append(user.getBureauFullName());
			if (StringUtils.equals(operType, "0")) {
				// 新增
				sb.append("新增图定开行计划");
				typeCode = Constants.MSG_ADD_TD_PLAN;
			} else if (StringUtils.equals(operType, "1")) {
				// 修改
				sb.append("修改图定开行计划");
				typeCode = Constants.MSG_UPD_TD_PLAN;
			}
			// get 交路信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("idList", SqlUtil.strArrayToList(crossIds.split(",")));
			List<PlanCross> pcList = runPlanService.getPlanCrossByMap(params);
			if(!pcList.isEmpty()){
				sb.append("\n").append("交路：").append(runPlanService.dealWithCross(pcList));
				remark = remark.replaceAll(user.getBureau(), "<span style=\"color: red\">" + user.getBureau() + "</span>");
				remark = remark.replaceAll(user.getBureauShortName(), "<span style=\"color: red\">" + user.getBureauShortName() + "</span>");
				sb.append("\n").append("备注：").append(remark);
				Integer count = messageService.dealWithMsg(user, typeCode, sb.toString(),runPlanService.dealWithRelevantBureau1(pcList,user.getBureau()),type);
				if(count == 0){
					result.setCode(StaticCodeType.MSG_ERROR.getCode());
					result.setMessage(StaticCodeType.MSG_ERROR.getDescription());
				}
			}
		}
		return result;
	}
	
}
