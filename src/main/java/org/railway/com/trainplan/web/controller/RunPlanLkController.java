package org.railway.com.trainplan.web.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import mor.railway.cmd.adapter.model.CmdInfoModel;
//import mor.railway.cmd.adapter.util.ConstantUtil;
//import mor.railway.cmd.adapter.util.JDomUtil;
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
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.ConstantUtil;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.SqlUtil;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CmdInfo;
import org.railway.com.trainplan.entity.CmdInfoModel;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.CmdTrainStn;
import org.railway.com.trainplan.entity.ModifyPlanDTO;
import org.railway.com.trainplan.entity.Node;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.entity.RunPlan;
import org.railway.com.trainplan.entity.RunPlanStn;
import org.railway.com.trainplan.entity.TrainLineSubInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfoTime;
import org.railway.com.trainplan.jdbcConnection.CmdAdapterServiceImpl;
import org.railway.com.trainplan.jdbcConnection.ICmdAdapterService;
import org.railway.com.trainplan.repository.mybatis.BureauDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanStnDao;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.MessageService;
import org.railway.com.trainplan.service.ModifyPlanService;
import org.railway.com.trainplan.service.PlanCheckService;
import org.railway.com.trainplan.service.RunPlanLkService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.RunPlanTrainDto;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;
import org.railway.com.trainplan.web.dto.PlanLineSTNDto;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplan.web.dto.TrainInfoDto;
import org.springframework.beans.BeanUtils;
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

/**
 * 临客相关操作
 * 
 * @author Think
 * 
 */
@Controller
@RequestMapping(value = "/runPlanLk")
public class RunPlanLkController {

	private static Log logger = LogFactory.getLog(RunPlanLkController.class
			.getName());
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Autowired
	private RunPlanLkService runPlanLkService;

	@Autowired
	private RunPlanDao runPlanDao;

	@Autowired
	private RunPlanStnDao runPlanStnDao;

	@Autowired
	private ModifyPlanService modifyService;

	@Autowired
	private BureauDao bureauDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private RunPlanService runPlanService;

	@Autowired
	private ModifyPlanService modifyPlanService;

	@Autowired
	private PlanCheckService planCheckService;

	@Autowired
	private MessageService messageService;

	private static boolean isRunling = true;

	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public ModelAndView addPage(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		modelAndView.setViewName("runPlanLk/runPlanLk_add");
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}

	@RequestMapping(value = "/addPageGT", method = RequestMethod.GET)
	public ModelAndView addPageGT(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		modelAndView.setViewName("runPlanLk/runPlanLkGT_add");
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}

	@RequestMapping(value = "/stopPage", method = RequestMethod.GET)
	public ModelAndView stopPage(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		modelAndView.setViewName("runPlanLk/runPlanLk_stop");
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}

	@RequestMapping(value = "/stopPageGT", method = RequestMethod.GET)
	public ModelAndView stopPageGT(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		modelAndView.setViewName("runPlanLk/runPlanLkGT_stop");
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}

	@RequestMapping(value = "/wdAddPage", method = RequestMethod.GET)
	public String wdAddPage() {
		return "runPlanLk/runPlanLk_wd_add";
	}

	@RequestMapping(value = "/sjAddPage", method = RequestMethod.GET)
	public String sjAddPage() {
		return "runPlanLk/runPlanLk_sj_add";
	}

	@RequestMapping(value = "/sjAddPageJy", method = RequestMethod.GET)
	public String sjAddPageJy() {
		return "runPlanLk/runPlanLk_sj_add_jy";
	}

	@RequestMapping(value = "/czAddPage", method = RequestMethod.GET)
	public String czAddPage() {
		return "runPlanLk/runPlanLk_cz_add";
	}

	@RequestMapping(value = "/czAddPageJy", method = RequestMethod.GET)
	public String czAddPageJy() {
		return "runPlanLk/runPlanLk_cz_add_jy";
	}

	@RequestMapping(value = "/jxsAddPage", method = RequestMethod.GET)
	public String jxsAddPage() {
		return "runPlanLk/runPlanLk_jxs_add";
	}

	@RequestMapping(value = "/jxsEditPage", method = RequestMethod.GET)
	public String jxsEditPage() {
		return "runPlanLk/runPlanLk_jxs_edit";
	}

	@RequestMapping(value = "/czEditPage", method = RequestMethod.GET)
	public String czEditPage() {
		return "runPlanLk/runPlanLk_cz_edit";
	}

	@RequestMapping(value = "/czEditPageJy", method = RequestMethod.GET)
	public String czEditPageJy() {
		return "runPlanLk/runPlanLk_cz_edit_jy";
	}

	@RequestMapping(value = "/sjEditPage", method = RequestMethod.GET)
	public String sjEditPage() {
		return "runPlanLk/runPlanLk_sj_edit";
	}

	@RequestMapping(value = "/sjEditPageJy", method = RequestMethod.GET)
	public String sjEditPageJy() {
		return "runPlanLk/runPlanLk_sj_edit_jy";
	}

	@RequestMapping(value = "/wdStopPage", method = RequestMethod.GET)
	public String wdStopPage() {
		return "runPlanLk/runPlanLk_wd_stop";
	}

	@RequestMapping(value = "/wdAddEditPage", method = RequestMethod.GET)
	public String wdAddEditPage() {
		// return "runPlanLk/runPlanLk_wd_add_edit?cmdTrainId=" + cmdTrainId;
		return "runPlanLk/runPlanLk_wd_add_edit";
	}

	@RequestMapping(value = "/wdStopEditPage", method = RequestMethod.GET)
	public String wdStopEditPage() {
		return "runPlanLk/runPlanLk_wd_stop_edit";
	}

	@RequestMapping(value = "/mainPage", method = RequestMethod.GET)
	public ModelAndView mainPage(
			@RequestParam(defaultValue = "") String train_type,
			ModelAndView modelAndView) {
		if ("0".equals(train_type))
			modelAndView.setViewName("runPlanLk/runPlanLk_main");
		if ("1".equals(train_type))
			modelAndView.setViewName("runPlanLk/runPlanLkGT_main");
		modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = "/getSelectDateStr", method = RequestMethod.POST)
	public Result getSelectDate(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("getSelectDateStr~~reqStr==" + reqStr);

		JSONObject reqObj = JSONObject.fromObject(reqStr);
		//
		List<String> dateList = null;
		dateList = reqObj.getJSONArray("dateList");
		//
		String str = ConstantUtil.getSelectedDateStrListToString(dateList);
		logger.info("getSelectDateStr~~reqStr==" + str);

		result.setMessage(str);
		return result;
	}

	@RequestMapping(value = "/jbtTrainInfoPage", method = RequestMethod.GET)
	public ModelAndView jbtTrainInfoPage(HttpServletRequest request) {
		return new ModelAndView("runPlanLk/jbt_traininfo").addObject("tabType",
				request.getParameter("tabType")).addObject("trainNbr",
				request.getParameter("trainNbr"));
	}

	@RequestMapping(value = "/hisTrainInfoPage", method = RequestMethod.GET)
	public ModelAndView hisTrainInfoPage(HttpServletRequest request) {
		return new ModelAndView("runPlanLk/his_traininfo")
				.addObject("tabType", request.getParameter("tabType"))
				.addObject("trainNbr", request.getParameter("trainNbr"))
				.addObject("cmdTrainId", request.getParameter("cmdTrainId"));
	}

	/**
	 * 高铁，复制历史.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hisTrainInfoPageGT", method = RequestMethod.GET)
	public ModelAndView hisTrainInfoPageGT(HttpServletRequest request) {
		return new ModelAndView("runPlanLk/his_traininfo_gt")
				.addObject("tabType", request.getParameter("tabType"))
				.addObject("trainNbr", request.getParameter("trainNbr"))
				.addObject("cmdTrainId", request.getParameter("cmdTrainId"));
	}

	@RequestMapping(value = "/jbtFillSitePage", method = RequestMethod.GET)
	public ModelAndView jbtFillSitePage(HttpServletRequest request) {
		return new ModelAndView("runPlanLk/jbt_fillSite").addObject("tabType",
				request.getParameter("tabType")).addObject("trainNbr",
				request.getParameter("trainNbr"));
	}

	/**
	 * 在表plan_train中查询临客列车信息
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanTrainLkInfo", method = RequestMethod.POST)
	public Result getPlanTrainLkInfo(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		try {
			// String isRelationBureau =
			// StringUtil.objToStr(reqMap.get("isRelationBureau"));
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			reqMap.put("bureau", user.getBureauShortName());
			List<RunPlan> runPlans = runPlanLkService
					.getPlanTrainLkInfo(reqMap);

			// TODO 为什么这样做，为什么用日志表过滤，查看RunPlanController - getPlanCross.
			List<RunPlan> retList = new ArrayList<RunPlan>();
			if (!runPlans.isEmpty() && null != reqMap.get("checkflagchoice")
					&& !"".equals(reqMap.get("checkflagchoice"))) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<Map<String, Object>> checkList = new ArrayList<Map<String, Object>>();
				for (RunPlan runPlan : runPlans) {
					if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkflagchoice"), "0")) {
						// 未审核
						map.put("cmdTelId", runPlan.getCmdTrainId());
						map.put("checkBureau", user.getBureau());
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (checkList.isEmpty()) {
							retList.add(runPlan);
						}
					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkflagchoice"), "1")) {
						// 本局通过
						map.put("cmdTelId", runPlan.getCmdTrainId());
						map.put("checkBureau", user.getBureau());
						map.put("checkState", 0);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty() && checkList.size() > 0) {
							retList.add(runPlan);
						}

					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkflagchoice"), "2")) {
						// 本局不通过
						map.put("cmdTelId", runPlan.getCmdTrainId());
						map.put("checkBureau", user.getBureau());
						map.put("checkState", 1);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty() && checkList.size() > 0) {
							retList.add(runPlan);
						}
					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkflagchoice"), "3")) {
						// 任意不通过
						map.put("cmdTelId", runPlan.getCmdTrainId());
						map.put("checkState", 1);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty() && checkList.size() > 0) {
							retList.add(runPlan);
						}
					} else if (StringUtils.equals(
							MapUtils.getString(reqMap, "checkflagchoice"), "4")) {
						// 全部通过
						map.put("cmdTelId", runPlan.getCmdTrainId());
						map.put("checkState", 0);
						checkList = planCheckService
								.getcheckStateByPlanCrossId(map);
						if (!checkList.isEmpty()) {
							// 全部通过，只判断日志表中的总数据;审核时，只能相关局审核所以不存在会有其他局审核的数据.
							if ((null != runPlan.getPassBureau() ? runPlan
									.getPassBureau().length() : 0) == checkList
									.size()) {
								retList.add(runPlan);
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
			result.setCode("-1");
			result.setMessage("管理临客查询出错:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 查询临客开行情况
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainLkRunPlans", method = RequestMethod.POST)
	public Result getTrainLkRunPlans(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		logger.debug("getTrainLkRunPlans~~~reqMap==" + reqMap);
		try {
			List<RunPlanTrainDto> runPlans = runPlanLkService
					.getTrainLkRunPlans(reqMap);
			result.setData(runPlans);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode("-1");
			result.setMessage("查询临客运行线开行规律出错:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 根据plant_train_id从PLAN_TRAIN_STN中查询列车时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainLkInfoForPlanTrainId", method = RequestMethod.POST)
	public Result getTrainLkInfoForPlanTrainId(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String bureauShortName = user.getBureauShortName();
		logger.info("getTrainLkInfoForPlanTrainId~~reqMap==" + reqMap);
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		String startDate = StringUtil.objToStr(reqMap.get("startDate"));
		String endDate = StringUtil.objToStr(reqMap.get("endDate"));
		// 用于获取纵坐标经由站的planTrainId
		String planTraindForStation = "";
		try {
			StringBuffer planTrainIds = new StringBuffer();
			String[] ids = planTrainId.split(",");
			planTraindForStation = ids[0];
			for (int i = 0; i < ids.length; i++) {
				if (i == ids.length - 1) {
					planTrainIds.append("'").append(ids[i]).append("'");
				} else {
					planTrainIds.append("'").append(ids[i]).append("'")
							.append(",");
				}

			}
			List<TrainLineSubInfo> list = runPlanLkService
					.getTrainLkInfoForPlanTrainId(planTrainIds.toString());
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			// 列车信息
			List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
			Map<String, Object> crossMap = new HashMap<String, Object>();
			// 用于纵坐标的经由站列表
			List<Station> listStation = new ArrayList<Station>();
			// 获取纵坐标
			List<TrainLineSubInfoTime> listSubInfoTime = runPlanLkService
					.getTrainLineSubinfoTime(planTraindForStation);
			// 将纵坐标拼接成 北京西[京]
			for (TrainLineSubInfoTime trainLineSubInfoTime : listSubInfoTime) {
				trainLineSubInfoTime.setStnName(trainLineSubInfoTime
						.getNodeName()
						+ "["
						+ trainLineSubInfoTime.getBureauShortName() + "]");

			}
			int endStnSort = listSubInfoTime.get(listSubInfoTime.size() - 1)
					.getStnSort();
			for (int i = 0; i < listSubInfoTime.size(); i++) {
				TrainLineSubInfoTime subInfo = listSubInfoTime.get(i);

				Station station = new Station();
				station.setStnName(subInfo.getStnName());
				String stationType = subInfo.getStationType();
				// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
				if ("4".equals(stationType)) {
					stationType = "BT";
				} else if ("3".equals(stationType)) {
					stationType = "TZ";
				} else if ("2".equals(stationType)) {
					stationType = "FJK";
				}

				if (i == 0 || i == listSubInfoTime.size() - 1) {
					stationType = "0";
				}
				station.setStationType(stationType);
				if ("BT".equals(stationType)) {
					continue;
				}
				listStation.add(station);
			}
			// 初始化startDate endDate
			startDate = DateUtil.parseDateTOyyyymmdd(list.get(0)
					.getTrainStaionList().get(0).getArrTime());
			endDate = DateUtil.parseDateTOyyyymmdd(list.get(0)
					.getTrainStaionList()
					.get(list.get(0).getTrainStaionList().size() - 1)
					.getDptTime());
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					TrainLineSubInfo train = list.get(j);

					// 设置列车信息
					TrainInfoDto dto = new TrainInfoDto();
					dto.setTrainName(trainNbr);
					dto.setPlanTrainId(train.getPlanTrainId());
					dto.setPassBureau(train.getPassBureau());
					// plan_train_stn表里没有这个字段
					// dto.setPlanCrossId(train.getPlanCrossId());
					// bureauShortName
					if (train.getPassBureau().indexOf(bureauShortName) >= 0) {
						dto.setIsModify(true);
					} else {
						dto.setIsModify(false);
					}
					List<TrainLineSubInfoTime> subInfoTimeList = train
							.getTrainStaionList();

					// 画图 确定画图时间范围 只画有运行线的部分

					// 参数1 就是最小的
					startDate = getStartRunDatesLK(startDate,
							subInfoTimeList.get(0));
					// 参数1 就是最大的
					endDate = getEndRunDatesLK(endDate,
							subInfoTimeList.get(subInfoTimeList.size() - 1));

					// 循环经由站
					List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
					for (int i = 0; i < subInfoTimeList.size(); i++) {
						TrainLineSubInfoTime subInfo = subInfoTimeList.get(i);

						PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
						stnDtoStart.setArrTime(subInfo.getArrTime());
						stnDtoStart.setDptTime(subInfo.getDptTime());
						stnDtoStart.setStnName(subInfo.getNodeName() + "["
								+ subInfo.getBureauShortName() + "]");
						stnDtoStart.setStationType(subInfo.getStationType());

						if (subInfo.getStnSort() == 0) {

							// 设置始发站
							dto.setStartStn(subInfo.getNodeName() + "["
									+ subInfo.getBureauShortName() + "]");
							dto.setStartDate(subInfo.getDptTime());
							stnDtoStart.setStationType("0");

						}
						if (endStnSort == subInfoTimeList.size() - 1) {
							// String dptTime = subInfo.getDptTime();
							// dptDate =
							// DateUtil.format(DateUtil.parseDate(dptTime,"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
							// 设置终到站
							dto.setEndStn(subInfo.getNodeName() + "["
									+ subInfo.getBureauShortName() + "]");
							dto.setEndDate(subInfo.getArrTime());
							stnDtoStart.setStationType("0");
						}
						if ("BT".equals(stnDtoStart.getStationType())) {
							continue;
						}
						trainStns.add(stnDtoStart);
					}
					dto.setTrainStns(trainStns);
					trains.add(dto);
				}

			}
			crossMap.put("trains", trains);
			dataList.add(crossMap);
			PlanLineGrid grid = null;

			// 生成横纵坐标
			List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
			List<PlanLineGridX> listGridX = getPlanLineGridX(startDate, endDate);
			grid = new PlanLineGrid(listGridX, listGridY);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("myJlData", dataList);
			dataMap.put("gridData", grid);

			System.err.println("myJlData==" + dataList);
			System.err.println("gridData==" + grid);
			System.err.println("dataMap==" + dataMap);
			result.setData(dataMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据plant_train_id从PLAN_TRAIN_STN中查询列车时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTrainLkForPlanTrainId", method = RequestMethod.POST)
	public Result getTrainLkForPlanTrainId(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getTrainLkInfoForPlanTrainId~~reqMap==" + reqMap);
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		StringBuffer sb = new StringBuffer();
		sb.append("'").append(planTrainId).append("'");
		try {

			List<TrainLineSubInfo> list = runPlanLkService
					.getTrainLkInfoForPlanTrainId(sb.toString());

			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("list", list);
			result.setData(dataMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 查询外局的cmdTrain信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOtherCmdTrainInfo", method = RequestMethod.POST)
	public Result getOtherCmdTrainInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getOtherCmdTrainInfo~~reqMap==" + reqMap);
		String startDate = StringUtil.objToStr(reqMap.get("startDate"));
		String endDate = StringUtil.objToStr(reqMap.get("endDate"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		// 命令状态 1:既有线临客加开 2：高铁临客加开
		String cmdType = StringUtil.objToStr(reqMap.get("cmdType"));
		// 部令号
		String cmdNbrSuperior = StringUtil.objToStr(reqMap
				.get("cmdNbrSuperior"));
		// 选线状态0：未选择 1：已选择 all:全部
		String selectState = "0";
		// 生成状态0：未生成 1：已生成 all:全部
		String createState = StringUtil.objToStr(reqMap.get("createState"));
		// 担当局
		String cmdBureau = StringUtil.objToStr(reqMap.get("cmdBureau"));
		logger.debug("cmdBureau==" + cmdBureau);
		try {

			CmdTrain cmdTrain = new CmdTrain();
			cmdTrain.setStartDate(startDate);
			cmdTrain.setEndDate(endDate);
			cmdTrain.setCmdBureau(cmdBureau);
			cmdTrain.setTrainNbr(trainNbr);
			cmdTrain.setCmdNbrSuperior(cmdNbrSuperior);
			cmdTrain.setSelectState("all");
			cmdTrain.setCreateState(createState);
			logger.info("cmdType==" + cmdType);
			if ("1".equals(cmdType)) {
				cmdTrain.setCmdType(ConstantUtil.JY_ADD_CMD_NAME);
			} else if ("2".equals(cmdType)) {
				cmdTrain.setCmdType(ConstantUtil.GT_ADD_CMD_NAME);
			} else if ("5".equals(cmdType)) {
				cmdTrain.setCmdType(ConstantUtil.JY_ADD_TELEGRAPH_NAME);
			} else if ("7".equals(cmdType)) {
				cmdTrain.setCmdType(ConstantUtil.GT_ADD_TELEGRAPH_NAME);
			} else {
				cmdTrain.setCmdType(cmdType);
			}
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureuaShortName = user.getBureauShortName();
			cmdTrain.setUserBureau(bureuaShortName);
			// 从本地数据库中查询
			List<CmdTrain> cmdTrainList = runPlanLkService
					.getCmdTraindForMultipleParame(cmdTrain);
			List<CmdTrain> dataList = new ArrayList<CmdTrain>();

			logger.info("getOtherCmdTrainInfo~~dataList==" + dataList.size());

			for (CmdTrain train : cmdTrainList) {
				String passBureau = train.getPassBureau();
				if (!"".equals(passBureau) && passBureau != null) {
					if (passBureau.contains(bureuaShortName)) {

						String start = train.getStartDate();
						String end = train.getEndDate();
						train.setStartDate(DateUtil.format(DateUtil.parseDate(
								start, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd"));
						train.setEndDate(DateUtil.format(
								DateUtil.parseDate(end, "yyyy-MM-dd HH:mm:ss"),
								"yyyy-MM-dd"));
						dataList.add(train);
					}
				}
			}
			result.setData(dataList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 查询本局的cmdTrain信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCmdTrainInfo", method = RequestMethod.POST)
	public Result getCmdTrainInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getCmdTrainInfo~~reqMap==" + reqMap);
		String startDate = StringUtil.objToStr(reqMap.get("startDate"));
		String endDate = StringUtil.objToStr(reqMap.get("endDate"));
		String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
		// 命令状态(如果状态是5或6，文电加开或文电停运，则不需要调用接口查询命令)
		String cmdType = StringUtil.objToStr(reqMap.get("cmdType"));
		// 局令号
		String cmdNbrBureau = StringUtil.objToStr(reqMap.get("cmdNbrBureau"));
		// 部令号
		String cmdNbrSuperior = StringUtil.objToStr(reqMap
				.get("cmdNbrSuperior"));
		// 选线状态0：未选择 1：已选择 all:全部
		String selectState = StringUtil.objToStr(reqMap.get("selectState"));

		// 生成状态0：未生成 1：已生成 all:全部
		String createState = StringUtil.objToStr(reqMap.get("createState"));

		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		// 本局局码
		String bureuaCode = user.getBureau();
		logger.debug("bureuaCode==" + bureuaCode);

		// 二种情况（命令是1-4，命令是5或6）
		List<CmdTrain> returnList = new ArrayList<CmdTrain>();
		try {
			if ("5".equals(cmdType) || "6".equals(cmdType)
					|| "7".equals(cmdType) || "8".equals(cmdType)) {
				CmdTrain cmdTrainQuery = new CmdTrain();
				cmdTrainQuery.setStartDate(startDate);
				cmdTrainQuery.setEndDate(endDate);

				switch (cmdType) {
				case "5":
					cmdTrainQuery
							.setCmdType(ConstantUtil.JY_ADD_TELEGRAPH_NAME);
					break;
				case "6":
					cmdTrainQuery
							.setCmdType(ConstantUtil.JY_STOP_TELEGRAPH_NAME);
					break;
				case "7":
					cmdTrainQuery
							.setCmdType(ConstantUtil.GT_ADD_TELEGRAPH_NAME);
					break;
				case "8":
					cmdTrainQuery
							.setCmdType(ConstantUtil.GT_STOP_TELEGRAPH_NAME);
					break;
				default:
					break;
				}

				cmdTrainQuery.setSelectState(selectState);
				cmdTrainQuery.setCreateState(createState);
				cmdTrainQuery.setTrainNbr(trainNbr);
				cmdTrainQuery.setCmdNbrBureau(cmdNbrBureau);
				cmdTrainQuery.setCmdNbrSuperior(cmdNbrSuperior);
				cmdTrainQuery.setCmdBureau(user.getBureauShortName());

				returnList = runPlanLkService
						.getCmdTraindForMultipleParame(cmdTrainQuery);
				logger.info("文电命令:" + returnList.size());
			} else {

				CmdInfoModel model = new CmdInfoModel();
				model.setStartDate(DateUtil.parse(startDate));
				model.setEndDate(DateUtil.parse(endDate));
				model.setCmdBureau(bureuaCode);
				model.setTrainNbr(trainNbr);
				model.setCmdNbrBureau(cmdNbrBureau);
				model.setCmdNbrSuperior(cmdNbrSuperior);
				if ("all".equals(cmdType)) {
					model.setCmdType(ConstantUtil.ALL_CMD_NAME);
				} else if ("1".equals(cmdType)) {
					// 既有线加开
					model.setCmdType(ConstantUtil.JY_ADD_CMD_NAME);
				} else if ("2".equals(cmdType)) {
					// 高铁加开
					model.setCmdType(ConstantUtil.GT_ADD_CMD_NAME);
				} else if ("3".equals(cmdType)) {
					// 既有线停运
					model.setCmdType(ConstantUtil.JY_STOP_CMD_NAME);
				} else if ("4".equals(cmdType)) {
					// 高铁停运
					model.setCmdType(ConstantUtil.GT_STOP_CMD_NAME);
				}
				List<CmdInfoModel> listModel = runPlanLkService
						.getCmdTrainInfoFromRemote(model);

				if (listModel != null && listModel.size() > 0) {
					for (CmdInfoModel infoModel : listModel) {
						CmdTrain cmdTrainTempl = new CmdTrain();
						Integer cmdTxtMlItemId = infoModel.getCmdTxtMlItemId();
						String cmdBuerau = infoModel.getCmdBureau();
						cmdTrainTempl.setImportantFlag(infoModel
								.isImportCmdFlag() ? "1" : "0");
						// 从本地数据库中查询
						Map<String, String> reqMap1 = new HashMap<String, String>();
						reqMap1.put("cmdBuerau",
								LjUtil.getLjByNameJ(cmdBuerau, 1).trim());
						reqMap1.put("cmdTxtmlItemId",
								String.valueOf(cmdTxtMlItemId));
						CmdTrain cmdTrain = runPlanLkService
								.getCmdTrainInfoForCmdTxtmlItemId(reqMap1);
						// CmdTrain cmdTrain = runPlanLkService
						// .getCmdTrainInfoForCmdTxtmlItemId(String
						// .valueOf(cmdTxtMlItemId));
						if (cmdTrain == null) {
							cmdTrainTempl.setCreateState("0");
							cmdTrainTempl.setSelectState("0");
							cmdTrainTempl.setIsExsitStn("0");
						} else {
							cmdTrainTempl.setCreateState(cmdTrain
									.getCreateState());
							cmdTrainTempl.setSelectState(cmdTrain
									.getSelectState());
							cmdTrainTempl.setCmdTrainId(cmdTrain
									.getCmdTrainId());
							cmdTrainTempl.setPassBureau(cmdTrain
									.getPassBureau());
							cmdTrainTempl.setUpdateTime(cmdTrain
									.getUpdateTime());
							cmdTrainTempl.setBusiness(cmdTrain.getBusiness());
							cmdTrainTempl.setStartBureauId(cmdTrain
									.getStartBureauId());
							cmdTrainTempl.setStartStnId(cmdTrain.getStartStn());
							cmdTrainTempl.setEndBureauId(cmdTrain
									.getEndBureauId());
							cmdTrainTempl.setEndStnId(cmdTrain.getEndStnId());
							cmdTrainTempl.setEndDays(cmdTrain.getEndDays());
							cmdTrainTempl.setRouteId(cmdTrain.getRouteId());
							cmdTrainTempl.setTrainTypeId(cmdTrain
									.getTrainTypeId());
							cmdTrainTempl.setIsExsitStn("1");
						}
						cmdTrainTempl.setCmdBureau(infoModel.getCmdBureau());
						cmdTrainTempl.setCmdItem(infoModel.getCmdItem());
						cmdTrainTempl.setCmdNbrBureau(infoModel
								.getCmdNbrBureau());
						cmdTrainTempl.setCmdNbrSuperior(infoModel
								.getCmdNbrSuperior());
						cmdTrainTempl.setCmdTime(DateUtil.format(
								infoModel.getCmdTime(), "yyyy-MM-dd"));

						cmdTrainTempl.setCmdType(infoModel.getCmdType());
						cmdTrainTempl.setEndDate(DateUtil.format(
								infoModel.getEndDate(), "yyyy-MM-dd"));
						cmdTrainTempl.setStartDate(DateUtil.format(
								infoModel.getStartDate(), "yyyy-MM-dd"));
						cmdTrainTempl.setEndStn(infoModel.getEndStn());
						String rule = infoModel.getRule();
						if (rule == null || "null".equals(rule)) {
							rule = "";
						}
						cmdTrainTempl.setRule(rule);
						cmdTrainTempl.setSelectedDate(infoModel
								.getSelectedDate());
						cmdTrainTempl.setStartStn(infoModel.getStartStn());
						cmdTrainTempl.setTrainNbr(infoModel.getTrainNbr());
						cmdTrainTempl.setCmdTxtMlId(infoModel.getCmdTxtMlId());
						cmdTrainTempl.setCmdTxtMlItemId(cmdTxtMlItemId);

						if ("all".equals(selectState)
								&& !"all".equals(createState)) {
							if (createState.equals(cmdTrainTempl
									.getCreateState())) {
								returnList.add(cmdTrainTempl);
							}
						} else if (!"all".equals(selectState)
								&& "all".equals(createState)) {
							if (selectState.equals(cmdTrainTempl
									.getSelectState())) {
								returnList.add(cmdTrainTempl);
							}
						} else if (!"all".equals(selectState)
								&& !"all".equals(createState)) {
							if (createState.equals(cmdTrainTempl
									.getCreateState())
									&& selectState.equals(cmdTrainTempl
											.getSelectState())) {
								returnList.add(cmdTrainTempl);
							}
						} else {
							// 添加到list中
							returnList.add(cmdTrainTempl);
						}

					}
				}

			}
			result.setData(returnList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// JDomUtil.writeFile(e.toString(), "/error/", "cmdError.txt");
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 获取临客加开命令的数据
	 * 
	 * @param startDate
	 *            令电日期(开始).
	 * @param endDate
	 *            令电日期(截至).
	 * @param bureuaCode
	 *            路局编码.
	 * @param trainNbr
	 *            车次.
	 * @param cmdNbrBureau
	 *            路局令电号.
	 * @param cmdNbrSuperior
	 *            铁总令电号.
	 * @param cmdType
	 *            类型.
	 * @return
	 */
	public List<CmdTrain> getLkMlData(String startDate, String endDate,
			String bureuaCode, String trainNbr, String cmdNbrBureau,
			String cmdNbrSuperior, String cmdType) {
		List<CmdTrain> returnList = new ArrayList<CmdTrain>();
		CmdInfoModel model = new CmdInfoModel();
		model.setStartDate(DateUtil.parse(startDate));
		model.setEndDate(DateUtil.parse(endDate));
		model.setCmdBureau(bureuaCode);
		model.setTrainNbr(trainNbr);
		model.setCmdNbrBureau(cmdNbrBureau);
		model.setCmdNbrSuperior(cmdNbrSuperior);
		model.setCmdType(cmdType);
		// if ("all".equals(cmdType)) {
		// } else if ("1".equals(cmdType)) {
		// // 既有线加开
		// model.setCmdType(ConstantUtil.JY_ADD_CMD_NAME);
		// } else if ("2".equals(cmdType)) {
		// // 高铁加开
		// model.setCmdType(ConstantUtil.GT_ADD_CMD_NAME);
		// } else if ("3".equals(cmdType)) {
		// // 既有线停运
		// model.setCmdType(ConstantUtil.JY_STOP_CMD_NAME);
		// } else if ("4".equals(cmdType)) {
		// // 高铁停运
		// model.setCmdType(ConstantUtil.GT_STOP_CMD_NAME);
		// }
		List<CmdInfoModel> listModel = runPlanLkService
				.getCmdTrainInfoFromRemote(model);

		if (listModel != null && listModel.size() > 0) {
			for (CmdInfoModel infoModel : listModel) {
				CmdTrain cmdTrainTempl = new CmdTrain();
				Integer cmdTxtMlItemId = infoModel.getCmdTxtMlItemId();
				String cmdBuerau = infoModel.getCmdBureau();
				cmdTrainTempl
						.setImportantFlag(infoModel.isImportCmdFlag() ? "1"
								: "0");
				// 从本地数据库中查询
				Map<String, String> reqMap1 = new HashMap<String, String>();
				reqMap1.put("cmdBuerau", LjUtil.getLjByNameJ(cmdBuerau, 1)
						.trim());
				reqMap1.put("cmdTxtmlItemId", String.valueOf(cmdTxtMlItemId));
				CmdTrain cmdTrain = runPlanLkService
						.getCmdTrainInfoForCmdTxtmlItemId(reqMap1);
				if (cmdTrain == null) {
					cmdTrainTempl.setCreateState("0");
					cmdTrainTempl.setSelectState("0");
					cmdTrainTempl.setIsExsitStn("0");
				} else {
					cmdTrainTempl.setCreateState(cmdTrain.getCreateState());
					cmdTrainTempl.setSelectState(cmdTrain.getSelectState());
					cmdTrainTempl.setCmdTrainId(cmdTrain.getCmdTrainId());
					cmdTrainTempl.setPassBureau(cmdTrain.getPassBureau());
					cmdTrainTempl.setUpdateTime(cmdTrain.getUpdateTime());
					cmdTrainTempl.setBusiness(cmdTrain.getBusiness());
					cmdTrainTempl.setStartBureauId(cmdTrain.getStartBureauId());
					cmdTrainTempl.setStartStnId(cmdTrain.getStartStn());
					cmdTrainTempl.setEndBureauId(cmdTrain.getEndBureauId());
					cmdTrainTempl.setEndStnId(cmdTrain.getEndStnId());
					cmdTrainTempl.setEndDays(cmdTrain.getEndDays());
					cmdTrainTempl.setRouteId(cmdTrain.getRouteId());
					cmdTrainTempl.setTrainTypeId(cmdTrain.getTrainTypeId());
					cmdTrainTempl.setIsExsitStn("1");
				}
				cmdTrainTempl.setCmdBureau(infoModel.getCmdBureau());
				cmdTrainTempl.setCmdItem(infoModel.getCmdItem());
				cmdTrainTempl.setCmdNbrBureau(infoModel.getCmdNbrBureau());
				cmdTrainTempl.setCmdNbrSuperior(infoModel.getCmdNbrSuperior());
				cmdTrainTempl.setCmdTime(DateUtil.format(
						infoModel.getCmdTime(), "yyyy-MM-dd"));

				cmdTrainTempl.setCmdType(infoModel.getCmdType());
				cmdTrainTempl.setEndDate(DateUtil.format(
						infoModel.getEndDate(), "yyyy-MM-dd"));
				cmdTrainTempl.setStartDate(DateUtil.format(
						infoModel.getStartDate(), "yyyy-MM-dd"));
				cmdTrainTempl.setEndStn(infoModel.getEndStn());
				String rule = infoModel.getRule();
				if (rule == null || "null".equals(rule)) {
					rule = "";
				}
				cmdTrainTempl.setRule(rule);
				cmdTrainTempl.setSelectedDate(infoModel.getSelectedDate());
				cmdTrainTempl.setStartStn(infoModel.getStartStn());
				cmdTrainTempl.setTrainNbr(infoModel.getTrainNbr());
				cmdTrainTempl.setCmdTxtMlId(infoModel.getCmdTxtMlId());
				cmdTrainTempl.setCmdTxtMlItemId(cmdTxtMlItemId);
				// 添加到list中
				returnList.add(cmdTrainTempl);

			}
		}
		return returnList;

	}

	/**
	 * 查询cmdTrainStn信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCmdTrainStnInfo", method = RequestMethod.POST)
	public Result getCmdTrainStnInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getCmdTrainStnInfo~~reqMap==" + reqMap);
		// cmdTrain表主键
		String cmdTrainId = StringUtil.objToStr(reqMap.get("cmdTrainId"));

		try {

			List<CmdTrainStn> list = runPlanLkService
					.getCmdTrainStnInfo(cmdTrainId);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 批量生成开行计划
	 * 
	 * @param reqStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/batchCreateRunPlanLine1", method = RequestMethod.POST)
	public Result batchCreateRunPlanLine1(@RequestBody String reqStr) {
		JSONObject reqObj1 = JSONObject.fromObject(reqStr);
		List<String> cmdTraindIdList1 = (List<String>) reqObj1
				.get("cmdTrainIds");
		List<Map<String, Object>> mapList1 = runPlanLkService
				.getPlanTrainIdForCmdTrainId(cmdTraindIdList1.get(0));
		if (mapList1 != null) {
			// 删除表plan_train和表plan_train_stn中对应的数据
			runPlanLkService.deleteTrainForCmdTrainId(cmdTraindIdList1.get(0));
			for (Map<String, Object> map : mapList1) {
				String planTrainId = StringUtil
						.objToStr(map.get("PLANTRAINID"));
				// 删除表plan_train_stn中对应数据
				runPlanLkService.deleteTrainStnForPlanTrainId(planTrainId);
			}
		}

		Result result = new Result();
		// logger.info("batchCreateRunPlanLine~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			List<String> cmdTraindIdList = (List<String>) reqObj
					.get("cmdTrainIds");
			if (cmdTraindIdList != null && cmdTraindIdList.size() > 0) {
				for (String cmdTrainId : cmdTraindIdList) {
					List<CmdTrain> cmdTrainList = runPlanLkService
							.getCmdTrandAndStnInfo(cmdTrainId);
					// 只有一条数据
					CmdTrain cmdTrain = cmdTrainList.get(0);

					// 时刻表信息
					List<CmdTrainStn> listTrainStn = cmdTrain
							.getCmdTrainStnList();

					String startDate = cmdTrain.getStartDate();
					String endDate = cmdTrain.getEndDate();

					// 创建临客命令对象
					CmdInfoModel model = new CmdInfoModel();
					model.setStartDate(DateUtil.parse(startDate));
					model.setEndDate(DateUtil.parse(endDate));
					model.setImportCmdFlag(cmdTrain.getImportantFlag() == "1" ? true
							: false);
					// 命令类型
					model.setCmdType(cmdTrain.getCmdType());
					// 开行规律
					model.setRule(cmdTrain.getRule());
					// 或者择日
					model.setSelectedDate(cmdTrain.getSelectedDate());

					List<Date> listDate = runPlanLkService
							.getSelectedDateListFromRemote(model);
					if (listDate != null && listDate.size() > 0) {
						// 始发站对象
						CmdTrainStn startTrainStn = listTrainStn.get(0);

						// 终到站对象
						CmdTrainStn endTrainStn = listTrainStn.get(listTrainStn
								.size() - 1);
						// 取最后一个站的ArrRunDays作为整个列车的runDays
						int runDays = endTrainStn.getArrRunDays();
						String cmdType = cmdTrain.getCmdType();

						// 先根据cmdTrainId查询表plan_train，确认是否已经生成计划
						List<Map<String, Object>> mapList = runPlanLkService
								.getPlanTrainIdForCmdTrainId(cmdTrainId);
						if (!mapList.isEmpty()) {
							// 删除表plan_train和表plan_train_stn中对应的数据
							runPlanLkService
									.deleteTrainForCmdTrainId(cmdTrainId);
							for (Map<String, Object> map : mapList) {
								String planTrainId = StringUtil.objToStr(map
										.get("PLANTRAINID"));
								// 删除表plan_train_stn中对应数据
								runPlanLkService
										.deleteTrainStnForPlanTrainId(planTrainId);
							}
						}
						for (Date date : listDate) {
							String runDate = DateUtil.format(date, "yyyyMMdd");

							// 表plan_train对应实体类
							RunPlan runPlan = new RunPlan();
							// 表plan_train对应的主键
							String planTrainId = UUID.randomUUID().toString();
							runPlan.setPlanTrainId(planTrainId);

							// 20140802-G1208/5-青岛北-06:25:00
							// 列车全路统一标识 （始发日期+始发车次+始发站+计划始发时刻）
							// String planTrainSign = runDate + "-"
							// + cmdTrain.getTrainNbr() + "-"
							// + startTrainStn.getStnName() + "-"
							// + startTrainStn.getDptTime();
							StringBuilder sb = new StringBuilder();
							sb.append(runDate).append("-");
							sb.append(cmdTrain.getTrainNbr()).append("-");
							sb.append(startTrainStn.getNodeName()).append("-");
							sb.append(startTrainStn.getDptTime());
							runPlan.setPlanTrainSign(sb.toString());
							runPlan.setRunDate(runDate);
							runPlan.setTrainNbr(cmdTrain.getTrainNbr());
							// 标记是否重点列车
							runPlan.setImportantFlag(cmdTrain
									.getImportantFlag());
							runPlan.setStartDateTime(new Timestamp(
									simpleDateFormat.parse(
											DateTimeFormat
													.forPattern("yyyyMMdd")
													.parseLocalDate(runDate)
													.toString("yyyy-MM-dd")
													+ " "
													+ startTrainStn
															.getDptTime())
											.getTime()));
							runPlan.setEndDateTime(new Timestamp(
									simpleDateFormat.parse(
											DateTimeFormat
													.forPattern("yyyyMMdd")
													.parseLocalDate(runDate)
													.plusDays(runDays)
													.toString("yyyy-MM-dd")
													+ " "
													+ endTrainStn.getArrTime())
											.getTime()));
							runPlan.setStartBureauShortName(startTrainStn
									.getStnBureau());
							runPlan.setEndBureauShortName(endTrainStn
									.getStnBureau());
							runPlan.setStartBureauFullName(startTrainStn
									.getStnBureauFull());
							runPlan.setEndBureauFullName(endTrainStn
									.getStnBureauFull());
							runPlan.setStartStn(cmdTrain.getStartStn());
							runPlan.setEndStn(cmdTrain.getEndStn());
							runPlan.setPassBureau(cmdTrain.getPassBureau());
							// 根据passBureau 来确定 列车范围（1:直通；0:管内）
							runPlan.setTrainScope(cmdTrain.getPassBureau()
									.length() == 1 ? 0 : 1);
							// 车辆担当局
							runPlan.setTokenVehBureau(LjUtil.getLjByName(cmdTrain.getCmdBureau(), 2));
							// 高线标记（1:高线；0:普线；2:混合）

							if (cmdType.equals(ConstantUtil.GT_ADD_CMD_NAME)
									|| cmdType
											.equals(ConstantUtil.GT_ADD_TELEGRAPH_NAME)) {
								runPlan.setHighLineFlag(1);
							} else if (cmdType
									.equals(ConstantUtil.JY_ADD_CMD_NAME)
									|| cmdType
											.equals(ConstantUtil.JY_ADD_TELEGRAPH_NAME)) {
								runPlan.setHighLineFlag(0);
							}

							// runPlan.setBaseTrainId(cmdTrain.getBaseTrainId());

							runPlan.setBaseTrainId("");
							// 备用及停运标记（1:开行；2:备用；9:停运）
							runPlan.setSpareFlag(1);
							runPlan.setImportantFlag(cmdTrain
									.getImportantFlag());
							runPlan.setBusiness(cmdTrain.getBusiness());
							runPlan.setSourceBureauId(cmdTrain
									.getStartBureauId());
							runPlan.setSourceNodeId(cmdTrain.getStartStnId());
							runPlan.setTargetBureauId(cmdTrain.getEndBureauId());
							runPlan.setTargetNodeId(cmdTrain.getEndStnId());
							runPlan.setTargetTimeScheduleDates(cmdTrain
									.getEndDays());
							runPlan.setRouteId(cmdTrain.getRouteId() == null ? ""
									: cmdTrain.getRouteId());
							runPlan.setTrainTypeId(cmdTrain.getTrainTypeId());

							String cmdTime = cmdTrain.getCmdTime();
							cmdTime = cmdTime.substring(0, 10).replace("-", "")
									.substring(4, 8);
							String cmdNbrSuperior = cmdTrain
									.getCmdNbrSuperior();

							// 创建方式 （0:基本图初始化；1:基本图滚动；2:文件电报；3:命令；
							if (cmdType
									.equals(ConstantUtil.JY_ADD_TELEGRAPH_NAME)
									|| cmdType
											.equals(ConstantUtil.GT_ADD_TELEGRAPH_NAME)) {
								runPlan.setCreateType(2);

								runPlan.setTelBureau(cmdTrain.getCmdBureau());
								// System.out.println(cmdTrain.getCmdBureau());
								runPlan.setTelId(cmdTrainId);
								runPlan.setTelName(cmdTrain.getCmdNbrBureau());

								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&& (cmdTrain.getCmdNbrBureau() != null
												&& !"".equals(cmdTrain
														.getCmdNbrBureau()) && cmdTrain
												.getCmdNbrBureau() != "null")

								) {
									runPlan.setTelShortinfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau() + "（"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");

								}
								if ((cmdTrain.getCmdNbrBureau() != null
										&& !"".equals(cmdTrain
												.getCmdNbrBureau()) && cmdTrain
										.getCmdNbrBureau() != "null")
										&& (cmdNbrSuperior == null
												|| "".equals(cmdNbrSuperior) || cmdNbrSuperior == "null")) {
									runPlan.setTelShortinfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau());
								}
								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null
												|| "".equals(cmdTrain
														.getCmdNbrBureau()) || cmdTrain
												.getCmdNbrBureau() == "null")) {
									runPlan.setTelShortinfo(cmdTime + "（"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");
								}
								if ((cmdNbrSuperior == null
										|| "".equals(cmdNbrSuperior) || cmdNbrSuperior == "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null
												|| "".equals(cmdTrain
														.getCmdNbrBureau()) || cmdTrain
												.getCmdNbrBureau() == "null")) {
									runPlan.setTelShortinfo(cmdTime);
								}

								// if (cmdNbrSuperior != null
								// && !"".equals(cmdNbrSuperior)&&cmdNbrSuperior
								// != "null") {
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)\
								// if(cmdTrain.getCmdNbrSuperior()!=null
								// ||cmdTrain.getCmdNbrSuperior() !="null"){
								// runPlan.setTelShortinfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau() +
								// "（铁总"
								// + cmdTrain.getCmdNbrSuperior() + "）"
								// );
								// }else{
								// runPlan.setTelShortinfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau());
								// }
								//
								// } else {
								//
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								// runPlan.setTelShortinfo(cmdTime
								// +cmdTrain.getCmdNbrBureau());
								// }

								runPlan.setCmdBureau("");
								runPlan.setCmdTxtmlId(0);
								runPlan.setCmdTxtmlitemId(0);
								runPlan.setCmdTrainId("");
								runPlan.setCmdShortInfo("");

							}

							if (cmdType.equals(ConstantUtil.JY_ADD_CMD_NAME)
									|| cmdType
											.equals(ConstantUtil.GT_ADD_CMD_NAME)) {
								runPlan.setCreateType(3);
								runPlan.setCmdBureau(cmdTrain.getCmdBureau());
								runPlan.setCmdTxtmlId(cmdTrain.getCmdTxtMlId());
								runPlan.setCmdTxtmlitemId(cmdTrain
										.getCmdTxtMlItemId());
								runPlan.setCmdTrainId(cmdTrainId);
								// if (cmdNbrSuperior != null
								// && !"".equals(cmdNbrSuperior)&&cmdNbrSuperior
								// != "null") {
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								// runPlan.setCmdShortInfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau() + "（铁总"
								// + cmdTrain.getCmdNbrSuperior() + "）");
								// } else {
								//
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								// runPlan.setCmdShortInfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau());
								// }

								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&& (cmdTrain.getCmdNbrBureau() != null
												&& !"".equals(cmdTrain
														.getCmdNbrBureau()) && cmdTrain
												.getCmdNbrBureau() != "null")

								) {
									runPlan.setCmdShortInfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau()
											+ "（铁总"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");

								}

								if ((cmdTrain.getCmdNbrBureau() != null
										&& !"".equals(cmdTrain
												.getCmdNbrBureau()) && cmdTrain
										.getCmdNbrBureau() != "null")
										&& (cmdNbrSuperior == null
												|| "".equals(cmdNbrSuperior) || cmdNbrSuperior == "null")) {
									runPlan.setCmdShortInfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau());
								}

								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null
												|| "".equals(cmdTrain
														.getCmdNbrBureau()) || cmdTrain
												.getCmdNbrBureau() == "null")) {
									runPlan.setCmdShortInfo(cmdTime + "（铁总"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");
								}
								if ((cmdNbrSuperior == null
										|| "".equals(cmdNbrSuperior) || cmdNbrSuperior != "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null || ""
												.equals(cmdTrain
														.getCmdNbrBureau())
												&& cmdTrain.getCmdNbrBureau() == "null")) {
									runPlan.setCmdShortInfo(cmdTime);
								}

								runPlan.setTelBureau("");
								runPlan.setTelId("");
								runPlan.setTelName("");

								// 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								runPlan.setTelShortinfo("");

							}

							// 日计划一级审核状态（0:未审核1:部分局审核2:途经局全部审核）
							runPlan.setCheckLev1Type(0);
							// 生成运行线标记（0: 是；1 :否）
							runPlan.setDailyPlanFlag(1);

							ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
									.getSubject().getPrincipal();
							runPlan.setCreatePeople(user.getName());
							runPlan.setCreatePeopleOrg(user.getBureauFullName());

							// 主表新增8个字段.
							runPlan.setStartStationStnId(cmdTrain
									.getStartStationStnId());
							runPlan.setStartStationStnName(cmdTrain
									.getStartStationStnName());
							runPlan.setStartStnTdcsId(cmdTrain
									.getStartStnTdcsId());
							runPlan.setStartStnTdcsName(cmdTrain
									.getStartStnTdcsName());
							runPlan.setEndStationStnId(cmdTrain
									.getEndStationStnId());
							runPlan.setEndStationStnName(cmdTrain
									.getEndStationStnName());
							runPlan.setEndStnTdcsId(cmdTrain.getEndStnTdcsId());
							runPlan.setEndStnTdcsName(cmdTrain
									.getEndStnTdcsName());

							runPlan.setTrainTypeId(cmdTrain.getTrainTypeId());

							List<RunPlanStn> stnList = new ArrayList<RunPlanStn>();
							int size = listTrainStn.size();
							for (int i = 0; i < size; i++) {
								CmdTrainStn trainStn = listTrainStn.get(i);
								RunPlanStn stn = new RunPlanStn();
								String arrTime = trainStn.getArrTime();
								String dptTime = trainStn.getDptTime();
								if (arrTime == null || "".equals(arrTime)
										|| !arrTime.contains(":")) {
									arrTime = dptTime;
								}
								if (dptTime == null || "".equals(dptTime)
										|| !dptTime.contains(":")) {
									dptTime = arrTime;
								}
								Integer arrRunDays = trainStn.getArrRunDays();
								Integer dptRunDays = trainStn.getDptRunDays();

								if (i == size - 1) {
									// 终到站时取到站的rundays
									dptRunDays = trainStn.getArrRunDays();
								}
								stn.setPlanTrainId(planTrainId);

								stn.setPlanTrainStnId(UUID.randomUUID()
										.toString());
								stn.setStnSort(trainStn.getStnSort());
								stn.setStnName(trainStn.getStnName());
								stn.setStnBureauShortName(trainStn
										.getStnBureau());
								stn.setStnBureauFullName(trainStn
										.getStnBureauFull());
								stn.setArrTime(new Timestamp(simpleDateFormat
										.parse(DateTimeFormat
												.forPattern("yyyyMMdd")
												.parseLocalDate(runDate)
												.plusDays(arrRunDays)
												.toString("yyyy-MM-dd")
												+ " " + arrTime).getTime()));
								stn.setDptTime(new Timestamp(simpleDateFormat
										.parse(DateTimeFormat
												.forPattern("yyyyMMdd")
												.parseLocalDate(runDate)
												.plusDays(dptRunDays)
												.toString("yyyy-MM-dd")
												+ " " + dptTime).getTime()));
								stn.setArrTrainNbr(trainStn.getArrTrainNbr());
								stn.setDptTrainNbr(trainStn.getDptTrainNbr());
								stn.setArrTimeStr(arrTime);
								stn.setDptTimeStr(dptTime);
								stn.setBaseArrTimeStr(trainStn.getBaseArrTime());
								stn.setBaseDptTimeStr(trainStn.getBaseDptTime());
								stn.setTrackName(trainStn.getTrackName());
								stn.setPlatform(trainStn.getPlatform());
								stn.setPsgFlag(trainStn.getPsgFlg());
								stn.setLocoFlag(trainStn.getLocoFlag());
								stn.setTecType(trainStn.getTecType());
								stn.setStnType(trainStn.getStnType());
								stn.settRunDays(dptRunDays);
								stn.setsRunDays(arrRunDays);
								stn.setJobs(trainStn.getJobs());
								stn.setBureauId(trainStn.getStnBureauId());
								stn.setNodeId(trainStn.getNodeId());
								stn.setNodeName(trainStn.getNodeName());

								// 新增4个字段,实际对应的是NodeStationId=stnId,NodeStationName=stnName,
								// 不要问我为什么,参考文档,20141223新旧结构对比
								stn.setNodeStationId(trainStn
										.getNodeStationId());
								stn.setNodeStationName(trainStn
										.getNodeStationName());
								stn.setNodeTdcsId(trainStn.getNodeTdcsId());
								stn.setNodeTdcsName(trainStn.getNodeTdcsName());

								// add to list
								stnList.add(stn);

							}
							// logger.info("JSON: "
							// + JSONObject.fromObject(runPlan).toString());
							// 保存数据到plan_train中
							runPlanLkService.addRunPlanLk(runPlan);
							// 保存数据到plan_train_stn中
							runPlanLkService.addRunPlanLkTrainStn(stnList);
						}
					}

					// 更新表cmd_train中的字段createState 0:未生成，1：已生成
					runPlanLkService.updateCreateStateForCmdTrainId(cmdTrainId,
							"1");
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;

	}

	@ResponseBody
	@RequestMapping(value = "/batchCreateRunPlanLine", method = RequestMethod.POST)
	public Result batchCreateRunPlanLine(@RequestBody String reqStr) {
		Result result = new Result();
		// logger.info("batchCreateRunPlanLine~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			List<String> cmdTraindIdList = (List<String>) reqObj
					.get("cmdTrainIds");
			if (cmdTraindIdList != null && cmdTraindIdList.size() > 0) {
				for (String cmdTrainId : cmdTraindIdList) {
					List<CmdTrain> cmdTrainList = runPlanLkService
							.getCmdTrandAndStnInfo(cmdTrainId);
					// 只有一条数据
					CmdTrain cmdTrain = cmdTrainList.get(0);

					// 时刻表信息
					List<CmdTrainStn> listTrainStn = cmdTrain
							.getCmdTrainStnList();

					String startDate = cmdTrain.getStartDate();
					String endDate = cmdTrain.getEndDate();

					// 创建临客命令对象
					CmdInfoModel model = new CmdInfoModel();
					model.setStartDate(DateUtil.parse(startDate));
					model.setImportCmdFlag(cmdTrain.getImportantFlag() == "1" ? true
							: false);
					model.setEndDate(DateUtil.parse(endDate));
					// 命令类型
					model.setCmdType(cmdTrain.getCmdType());
					// 开行规律
					model.setRule(cmdTrain.getRule());
					// 或者择日
					model.setSelectedDate(cmdTrain.getSelectedDate());

					List<Date> listDate = runPlanLkService
							.getSelectedDateListFromRemote(model);
					if (listDate != null && listDate.size() > 0) {
						// 始发站对象
						CmdTrainStn startTrainStn = listTrainStn.get(0);
						// 终到站对象
						CmdTrainStn endTrainStn = listTrainStn.get(listTrainStn
								.size() - 1);
						// 取最后一个站的ArrRunDays作为整个列车的runDays
						int runDays = endTrainStn.getArrRunDays();
						String cmdType = cmdTrain.getCmdType();

						// 先根据cmdTrainId查询表plan_train，确认是否已经生成计划
						List<Map<String, Object>> mapList = runPlanLkService
								.getPlanTrainIdForCmdTrainId(cmdTrainId);
						if (!mapList.isEmpty()) {
							// 删除表plan_train和表plan_train_stn中对应的数据
							runPlanLkService
									.deleteTrainForCmdTrainId(cmdTrainId);
							for (Map<String, Object> map : mapList) {
								String planTrainId = StringUtil.objToStr(map
										.get("PLANTRAINID"));
								// 删除表plan_train_stn中对应数据
								runPlanLkService
										.deleteTrainStnForPlanTrainId(planTrainId);
							}
						}
						for (Date date : listDate) {
							String runDate = DateUtil.format(date, "yyyyMMdd");

							// 表plan_train对应实体类
							RunPlan runPlan = new RunPlan();
							// 表plan_train对应的主键
							String planTrainId = UUID.randomUUID().toString();
							runPlan.setPlanTrainId(planTrainId);

							// 20140802-G1208/5-青岛北-06:25:00
							// 列车全路统一标识 （始发日期+始发车次+始发站+计划始发时刻）
							// String planTrainSign = runDate + "-"
							// + cmdTrain.getTrainNbr() + "-"
							// + startTrainStn.getStnName() != null ?
							// startTrainStn
							// .getStnName() : startTrainStn.getNodeName()
							// + "-" + startTrainStn.getDptTime();
							StringBuilder sb = new StringBuilder();
							sb.append(runDate).append("-");
							sb.append(cmdTrain.getTrainNbr()).append("-");
							sb.append(startTrainStn.getNodeName()).append("-");
							sb.append(startTrainStn.getDptTime());
							runPlan.setPlanTrainSign(sb.toString());
							runPlan.setRunDate(runDate);
							runPlan.setTrainNbr(cmdTrain.getTrainNbr());

							runPlan.setStartDateTime(new Timestamp(
									simpleDateFormat.parse(
											DateTimeFormat
													.forPattern("yyyyMMdd")
													.parseLocalDate(runDate)
													.toString("yyyy-MM-dd")
													+ " "
													+ startTrainStn
															.getDptTime())
											.getTime()));
							runPlan.setEndDateTime(new Timestamp(
									simpleDateFormat.parse(
											DateTimeFormat
													.forPattern("yyyyMMdd")
													.parseLocalDate(runDate)
													.plusDays(runDays)
													.toString("yyyy-MM-dd")
													+ " "
													+ endTrainStn.getArrTime())
											.getTime()));
							runPlan.setStartBureauShortName(startTrainStn
									.getStnBureau());
							runPlan.setEndBureauShortName(endTrainStn
									.getStnBureau());
							runPlan.setStartBureauFullName(startTrainStn
									.getStnBureauFull());
							runPlan.setEndBureauFullName(endTrainStn
									.getStnBureauFull());
							runPlan.setStartStn(cmdTrain.getStartStn());
							runPlan.setEndStn(cmdTrain.getEndStn());
							runPlan.setPassBureau(cmdTrain.getPassBureau());
							// 根据passBureau 来确定 列车范围（1:直通；0:管内）
							runPlan.setTrainScope(cmdTrain.getPassBureau()
									.length() == 1 ? 0 : 1);
							// 车辆担当局
							runPlan.setTokenVehBureau(cmdTrain.getCmdBureau());
							// 高线标记（1:高线；0:普线；2:混合）

							runPlan.setImportantFlag(cmdTrain
									.getImportantFlag());

							if (cmdType.equals(ConstantUtil.GT_ADD_CMD_NAME)
									|| cmdType
											.equals(ConstantUtil.GT_ADD_TELEGRAPH_NAME)) {
								runPlan.setHighLineFlag(1);
							} else if (cmdType
									.equals(ConstantUtil.JY_ADD_CMD_NAME)
									|| cmdType
											.equals(ConstantUtil.JY_ADD_TELEGRAPH_NAME)) {
								runPlan.setHighLineFlag(0);
							}

							// runPlan.setBaseTrainId(cmdTrain.getBaseTrainId());

							runPlan.setBaseTrainId("");
							// 备用及停运标记（1:开行；2:备用；9:停运）
							runPlan.setSpareFlag(1);

							runPlan.setBusiness(cmdTrain.getBusiness());
							runPlan.setSourceBureauId(cmdTrain
									.getStartBureauId());
							runPlan.setSourceNodeId(cmdTrain.getStartStnId());
							runPlan.setTargetBureauId(cmdTrain.getEndBureauId());
							runPlan.setTargetNodeId(cmdTrain.getEndStnId());
							runPlan.setTargetTimeScheduleDates(cmdTrain
									.getEndDays());
							runPlan.setRouteId(cmdTrain.getRouteId() == null ? ""
									: cmdTrain.getRouteId());
							runPlan.setTrainTypeId(cmdTrain.getTrainTypeId());

							String cmdTime = cmdTrain.getCmdTime();
							cmdTime = cmdTime.substring(0, 10).replace("-", "")
									.substring(4, 8);
							String cmdNbrSuperior = cmdTrain
									.getCmdNbrSuperior();

							// 创建方式 （0:基本图初始化；1:基本图滚动；2:文件电报；3:命令；

							if (cmdType
									.equals(ConstantUtil.JY_ADD_TELEGRAPH_NAME)
									|| cmdType
											.equals(ConstantUtil.GT_ADD_TELEGRAPH_NAME)) {
								runPlan.setCreateType(2);

								runPlan.setTelBureau(cmdTrain.getCmdBureau());
								// System.out.println(cmdTrain.getCmdBureau());
								runPlan.setTelId(cmdTrainId);
								runPlan.setTelName(cmdTrain.getCmdNbrBureau());

								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&& (cmdTrain.getCmdNbrBureau() != null
												&& !"".equals(cmdTrain
														.getCmdNbrBureau()) && cmdTrain
												.getCmdNbrBureau() != "null")

								) {
									runPlan.setTelShortinfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau() + "（"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");

								}
								if ((cmdTrain.getCmdNbrBureau() != null
										&& !"".equals(cmdTrain
												.getCmdNbrBureau()) && cmdTrain
										.getCmdNbrBureau() != "null")
										&& (cmdNbrSuperior == null
												|| "".equals(cmdNbrSuperior) || cmdNbrSuperior == "null")) {
									runPlan.setTelShortinfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau());
								}
								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null
												|| "".equals(cmdTrain
														.getCmdNbrBureau()) || cmdTrain
												.getCmdNbrBureau() == "null")) {
									runPlan.setTelShortinfo(cmdTime + "（"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");
								}
								if ((cmdNbrSuperior == null
										|| "".equals(cmdNbrSuperior) || cmdNbrSuperior == "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null
												|| "".equals(cmdTrain
														.getCmdNbrBureau()) || cmdTrain
												.getCmdNbrBureau() == "null")) {
									runPlan.setTelShortinfo(cmdTime);
								}
								// if (cmdNbrSuperior != null
								// && !"".equals(cmdNbrSuperior)&&cmdNbrSuperior
								// != "null") {
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)\
								// if(cmdTrain.getCmdNbrSuperior()!=null
								// ||cmdTrain.getCmdNbrSuperior() !="null"){
								// runPlan.setTelShortinfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau() +
								// "（铁总"
								// + cmdTrain.getCmdNbrSuperior() + "）"
								// );
								// }else{
								// runPlan.setTelShortinfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau());
								// }
								//
								// } else {
								//
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								// runPlan.setTelShortinfo(cmdTime
								// +cmdTrain.getCmdNbrBureau());
								// }

								runPlan.setCmdBureau("");
								runPlan.setCmdTxtmlId(0);
								runPlan.setCmdTxtmlitemId(0);
								runPlan.setCmdTrainId("");
								runPlan.setCmdShortInfo("");

							}

							if (cmdType.equals(ConstantUtil.JY_ADD_CMD_NAME)
									|| cmdType
											.equals(ConstantUtil.GT_ADD_CMD_NAME)) {
								runPlan.setCreateType(3);
								runPlan.setCmdBureau(cmdTrain.getCmdBureau());
								runPlan.setCmdTxtmlId(cmdTrain.getCmdTxtMlId());
								runPlan.setCmdTxtmlitemId(cmdTrain
										.getCmdTxtMlItemId());
								runPlan.setCmdTrainId(cmdTrainId);
								// if (cmdNbrSuperior != null
								// && !"".equals(cmdNbrSuperior)&&cmdNbrSuperior
								// != "null") {
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								// runPlan.setCmdShortInfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau() + "（铁总"
								// + cmdTrain.getCmdNbrSuperior() + "）");
								// } else {
								//
								// // 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								// runPlan.setCmdShortInfo(cmdTime + "-"
								// + cmdTrain.getCmdNbrBureau());
								// }

								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&& (cmdTrain.getCmdNbrBureau() != null
												&& !"".equals(cmdTrain
														.getCmdNbrBureau()) && cmdTrain
												.getCmdNbrBureau() != "null")

								) {
									runPlan.setCmdShortInfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau()
											+ "（铁总"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");

								}

								if ((cmdTrain.getCmdNbrBureau() != null
										&& !"".equals(cmdTrain
												.getCmdNbrBureau()) && cmdTrain
										.getCmdNbrBureau() != "null")
										&& (cmdNbrSuperior == null
												|| "".equals(cmdNbrSuperior) || cmdNbrSuperior == "null")) {
									runPlan.setCmdShortInfo(cmdTime + "-"
											+ cmdTrain.getCmdNbrBureau());
								}

								if ((cmdNbrSuperior != null
										&& !"".equals(cmdNbrSuperior) && cmdNbrSuperior != "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null
												|| "".equals(cmdTrain
														.getCmdNbrBureau()) || cmdTrain
												.getCmdNbrBureau() == "null")) {
									runPlan.setCmdShortInfo(cmdTime + "（铁总"
											+ cmdTrain.getCmdNbrSuperior()
											+ "）");
								}
								if ((cmdNbrSuperior == null
										|| "".equals(cmdNbrSuperior) || cmdNbrSuperior != "null")
										&&

										(cmdTrain.getCmdNbrBureau() == null || ""
												.equals(cmdTrain
														.getCmdNbrBureau())
												&& cmdTrain.getCmdNbrBureau() == "null")) {
									runPlan.setCmdShortInfo(cmdTime);
								}

								runPlan.setTelBureau("");
								runPlan.setTelId("");
								runPlan.setTelName("");

								// 命令简要信息(发令日期+局命令号+命令子项号+部命令号)
								runPlan.setTelShortinfo("");

							}

							// 日计划一级审核状态（0:未审核1:部分局审核2:途经局全部审核）
							runPlan.setCheckLev1Type(0);
							// 生成运行线标记（0: 是；1 :否）
							runPlan.setDailyPlanFlag(1);

							ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
									.getSubject().getPrincipal();
							runPlan.setCreatePeople(user.getName());
							runPlan.setCreatePeopleOrg(user.getBureauFullName());

							// 主表新增8个字段.
							runPlan.setStartStationStnId(cmdTrain
									.getStartStationStnId());
							runPlan.setStartStationStnName(cmdTrain
									.getStartStationStnName());
							runPlan.setStartStnTdcsId(cmdTrain
									.getStartStnTdcsId());
							runPlan.setStartStnTdcsName(cmdTrain
									.getStartStnTdcsName());
							runPlan.setEndStationStnId(cmdTrain
									.getEndStationStnId());
							runPlan.setEndStationStnName(cmdTrain
									.getEndStationStnName());
							runPlan.setEndStnTdcsId(cmdTrain.getEndStnTdcsId());
							runPlan.setEndStnTdcsName(cmdTrain
									.getEndStnTdcsName());

							List<RunPlanStn> stnList = new ArrayList<RunPlanStn>();
							int size = listTrainStn.size();
							for (int i = 0; i < size; i++) {
								CmdTrainStn trainStn = listTrainStn.get(i);
								RunPlanStn stn = new RunPlanStn();
								String arrTime = trainStn.getArrTime();
								String dptTime = trainStn.getDptTime();
								if (arrTime == null || "".equals(arrTime)
										|| !arrTime.contains(":")) {
									arrTime = dptTime;
								}
								if (dptTime == null || "".equals(dptTime)
										|| !dptTime.contains(":")) {
									dptTime = arrTime;
								}
								Integer arrRunDays = trainStn.getArrRunDays();
								Integer dptRunDays = trainStn.getDptRunDays();

								if (i == size - 1) {
									// 终到站时取到站的rundays
									dptRunDays = trainStn.getArrRunDays();
								}
								stn.setPlanTrainId(planTrainId);

								stn.setPlanTrainStnId(UUID.randomUUID()
										.toString());
								stn.setStnSort(trainStn.getStnSort());
								stn.setStnName(trainStn.getStnName());
								stn.setStnBureauShortName(trainStn
										.getStnBureau());
								stn.setStnBureauFullName(trainStn
										.getStnBureauFull());
								stn.setArrTime(new Timestamp(simpleDateFormat
										.parse(DateTimeFormat
												.forPattern("yyyyMMdd")
												.parseLocalDate(runDate)
												.plusDays(arrRunDays)
												.toString("yyyy-MM-dd")
												+ " " + arrTime).getTime()));
								stn.setDptTime(new Timestamp(simpleDateFormat
										.parse(DateTimeFormat
												.forPattern("yyyyMMdd")
												.parseLocalDate(runDate)
												.plusDays(dptRunDays)
												.toString("yyyy-MM-dd")
												+ " " + dptTime).getTime()));
								stn.setArrTrainNbr(trainStn.getArrTrainNbr());
								stn.setDptTrainNbr(trainStn.getDptTrainNbr());
								stn.setArrTimeStr(arrTime);
								stn.setDptTimeStr(dptTime);
								stn.setBaseArrTimeStr(trainStn.getBaseArrTime());
								stn.setBaseDptTimeStr(trainStn.getBaseDptTime());
								stn.setTrackName(trainStn.getTrackName());
								stn.setPlatform(trainStn.getPlatform());
								stn.setPsgFlag(trainStn.getPsgFlg());
								stn.setLocoFlag(trainStn.getLocoFlag());
								stn.setTecType(trainStn.getTecType());
								stn.setStnType(trainStn.getStnType());
								stn.settRunDays(dptRunDays);
								stn.setsRunDays(arrRunDays);
								stn.setJobs(trainStn.getJobs());
								stn.setBureauId(trainStn.getStnBureauId());
								stn.setNodeId(trainStn.getNodeId());
								stn.setNodeName(trainStn.getNodeName());

								// add to list
								stnList.add(stn);

							}
							// logger.info("JSON: "
							// + JSONObject.fromObject(runPlan).toString());
							// 保存数据到plan_train中
							runPlanLkService.addRunPlanLk(runPlan);
							// 保存数据到plan_train_stn中
							runPlanLkService.addRunPlanLkTrainStn(stnList);
						}
					}

					// 更新表cmd_train中的字段createState 0:未生成，1：已生成
					runPlanLkService.updateCreateStateForCmdTrainId(cmdTrainId,
							"1");
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 保存外局的临客列车运行时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOtherLkTrainTimes", method = RequestMethod.POST)
	public Result saveOtherLkTrainTimes(@RequestBody String reqStr) {
		Result result = new Result();

		Map<String, String> lumap = new HashMap<String, String>();
		if (!isRunling) {
			result.setCode("100");
			return result;
		}

		lumap.put("哈", "	哈尔滨铁路局");
		lumap.put("沈", "	沈阳铁路局");
		lumap.put("京", "	北京铁路局");
		lumap.put("太", "	太原铁路局");
		lumap.put("呼", "	呼和浩特铁路局 ");
		lumap.put("郑", "	郑州铁路局");
		lumap.put("武", "	武汉铁路局");
		lumap.put("西", "	西安铁路局");
		lumap.put("济", "	济南铁路局");
		lumap.put("上", "	上海铁路局");
		lumap.put("南", "	南昌铁路局");
		lumap.put("广", "	广州铁路（集团）公司");
		lumap.put("宁", "	南宁铁路局");
		lumap.put("成", "	成都铁路局");
		lumap.put("昆", "	昆明铁路局");
		lumap.put("兰", "	兰州铁路局");
		lumap.put("乌", "	乌鲁木齐铁路局");
		lumap.put("青", "	青藏铁路公司");
		logger.info("saveOtherLkTrainTimes~~reqStr==" + reqStr);
		try {

			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String trainStr = (String) reqObj.get("cmdTrainMap");
			JSONObject trainMap = JSONObject.fromObject(trainStr);
			List<JSONObject> trainStnList = reqObj
					.getJSONArray("cmdTrainStnList");

			// 表cmd_train主键
			String cmdTrainId = StringUtil.objToStr(trainMap.get("cmdTrainId"));

			// System.out.println(trainMap.get("createState"));
			String createState = StringUtil.objToStr(trainMap
					.get("createState"));
			if (StringUtils.equals("1", createState)) {
				// 操作取消！当前车次已生成开行计划。
				result.setCode(StaticCodeType.SYSTEM_DATA_STATUS_ERROR
						.getCode());
				result.setMessage(StaticCodeType.SYSTEM_DATA_STATUS_ERROR
						.getDescription());
				return result;

			}

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureuaShortName = user.getBureauShortName();
			// 先删除表cmd_train_stn中对应的数据
			int count = runPlanLkService.deleteCmdTrainStnForCmdTrainId(
					cmdTrainId, null);
			logger.debug("count==" + count);
			// 循环设置子表cmd_train_stn中的数据对象
			for (int i = 0; i < trainStnList.size(); i++) {
				JSONObject trainStn = trainStnList.get(i);
				// 局码简称
				String stnBureau = StringUtil.objToStr(trainStn
						.get("stnBureau"));
				// 只保存本局的数据
				// if (stnBureau.equals(bureuaShortName)) {
				CmdTrainStn stn = new CmdTrainStn();

				stn.setCmdTrainId(cmdTrainId);
				stn.setCmdTrainStnId(UUID.randomUUID().toString());
				String arrTrainNbr = StringUtil.objToStr(trainStn
						.get("arrTrainNbr"));
				String dptTrainNbr = StringUtil.objToStr(trainStn
						.get("dptTrainNbr"));
				stn.setArrTrainNbr(arrTrainNbr == null ? "" : arrTrainNbr);
				stn.setDptTrainNbr(dptTrainNbr == null ? "" : dptTrainNbr);
				// String arrTime = StringUtil.objToStr(trainStn
				// .get("arrTime"));
				// String endTime = StringUtil.objToStr(trainStn
				// .get("dptTime"));
				String arrTime = StringUtil.objToStr(trainStn.get("arrTime")
						.equals("--") ? null : trainStn.get("arrTime"));
				String endTime = StringUtil.objToStr(trainStn.get("dptTime")
						.equals("--") ? null : trainStn.get("dptTime"));
				String baseArrTime = StringUtil.objToStr(trainStn
						.get("baseArrTime"));
				String baseDptTime = StringUtil.objToStr(trainStn
						.get("baseDptTime"));
				if ((arrTime == null || "".equals(arrTime))
						&& (endTime != null && !"".equals(endTime))) {
					stn.setArrTime(endTime);
					stn.setDptTime(endTime);

					if (baseArrTime == null || "".equals(baseArrTime)) {
						stn.setBaseArrTime(endTime);
					} else {
						stn.setBaseArrTime(baseArrTime);
					}
					if (baseDptTime == null || "".equals(baseDptTime)) {
						stn.setBaseDptTime(endTime);
					} else {
						stn.setBaseDptTime(baseDptTime);
					}
				} else if ((endTime == null || "".equals(endTime))
						&& (arrTime != null && !"".equals(arrTime))) {
					stn.setArrTime(arrTime);
					stn.setDptTime(arrTime);
					if (baseArrTime == null || "".equals(baseArrTime)) {
						stn.setBaseArrTime(arrTime);
					} else {
						stn.setBaseArrTime(baseArrTime);
					}
					if (baseDptTime == null || "".equals(baseDptTime)) {
						stn.setBaseDptTime(arrTime);
					} else {
						stn.setBaseDptTime(baseDptTime);
					}
				} else {
					stn.setArrTime(arrTime);
					stn.setDptTime(endTime);
					if (baseArrTime == null || "".equals(baseArrTime)) {
						stn.setBaseArrTime(arrTime);
					} else {
						stn.setBaseArrTime(baseArrTime);
					}
					if (baseDptTime == null || "".equals(baseDptTime)) {
						stn.setBaseDptTime(endTime);
					} else {
						stn.setBaseDptTime(baseDptTime);
					}
				}
				String plantform = StringUtil
						.objToStr(trainStn.get("platform"));
				Integer plantForm = Integer.valueOf("".equals(plantform) ? "0"
						: plantform);
				stn.setPlatform(plantForm);
				stn.setStnBureau(StringUtil.objToStr(trainStn.get("stnBureau")));

				stn.setStnBureauFull(lumap.get(
						StringUtil.objToStr(trainStn.get("stnBureau"))).trim());
				stn.setStnName(StringUtil.objToStr(trainStn.get("stnName")));

				stn.setStnType(StringUtil.objToStr(trainStn.get("stnType")));
				stn.setTecType(StringUtil.objToStr(trainStn.get("tecType")));
				stn.setTrackNbr(StringUtil.objToStr(trainStn.get("trackNbr")));
				stn.setStnSort((Integer) trainStn.get("childIndex"));

				int psgFlg = trainStn.get("psgFlg") == null ? 0
						: (Integer) trainStn.get("psgFlg");
				int arrRunDays = (trainStn.get("arrRunDays") == null || trainStn
						.get("arrRunDays").equals("--")) ? 0 : Integer
						.valueOf(trainStn.get("arrRunDays").toString());
				int dptRunDays = (trainStn.get("dptRunDays") == null || trainStn
						.get("dptRunDays").equals("--")) ? 0 : Integer
						.valueOf(trainStn.get("dptRunDays").toString());
				int locoFlag = trainStn.get("locoFlag") == null ? 0
						: (Integer) trainStn.get("locoFlag");

				stn.setPsgFlg(psgFlg);
				stn.setArrRunDays(arrRunDays);
				stn.setDptRunDays(dptRunDays);
				stn.setLocoFlag(locoFlag);
				stn.setStnBureauId(StringUtil.objToStr(trainStn
						.get("stnBureauId")));
				stn.setJobs(StringUtil.objToStr(trainStn.get("jobs")));
				stn.setNodeId(StringUtil.objToStr(trainStn.get("nodeId")));
				stn.setNodeName(StringUtil.objToStr(trainStn.get("nodeName")));
				stn.setTrackName(StringUtil.objToStr(trainStn.get("trackName")));
				if (i == 0) {// 第一行数据
					stn.setJobs(ConstantUtil.START_STATION_FLAG);
				} else if (i == trainStnList.size() - 1) {// 最后一行数据
					stn.setJobs(ConstantUtil.END_STATION_FLAG);
				} else {
					stn.setJobs("<经由>");
				}
				// 保存数据到表cmd_train_stn中
				runPlanLkService.insertCmdTrainStn(stn);
				isRunling = false;
				// }
			}
			// // 循环更新数据库改变后的顺序
			// List<CmdTrainStn> list = runPlanLkService
			// .getCmdTrainStnInfo(cmdTrainId);
			// for (int i = 0; i < trainStnList.size(); i++) {
			// for (int j = 0; j < list.size(); j++) {
			// JSONObject trainStn = trainStnList.get(i);
			// CmdTrainStn cmdTrainStn = list.get(j);
			// Integer index = (Integer) trainStn.get("childIndex");
			//
			// Integer index2 = cmdTrainStn.getStnSort();
			// if(index!=null && !index.equals("")){
			// if (index.equals(index2)) {
			// cmdTrainStn.setStnSort(index);
			// runPlanLkService.updateCmdTrainStnSort(cmdTrainStn);
			// }
			// }
			//
			//
			// }
			//
			// }

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		isRunling = true;
		return result;
	}

	/**
	 * 保存临客列车运行时刻表
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveLkTrainTimes", method = RequestMethod.POST)
	public Result saveLkTrainTimes(@RequestBody String reqStr) {
		Result result = new Result();
		Map<String, String> lumap = new HashMap<String, String>();
		Map<String, String> paramMap = new HashMap<String, String>();
		String startStn = null;
		String endStn = null;
		Node startNode = new Node();
		Node endNode = new Node();
		if (!isRunling) {
			result.setCode("100");
			return result;
		}
		lumap.put("哈", "	哈尔滨铁路局");
		lumap.put("沈", "	沈阳铁路局");
		lumap.put("京", "	北京铁路局");
		lumap.put("太", "	太原铁路局");
		lumap.put("呼", "	呼和浩特铁路局 ");
		lumap.put("郑", "	郑州铁路局");
		lumap.put("武", "	武汉铁路局");
		lumap.put("西", "	西安铁路局");
		lumap.put("济", "	济南铁路局");
		lumap.put("上", "	上海铁路局");
		lumap.put("南", "	南昌铁路局");
		lumap.put("广", "	广州铁路（集团）公司");
		lumap.put("宁", "	南宁铁路局");
		lumap.put("成", "	成都铁路局");
		lumap.put("昆", "	昆明铁路局");
		lumap.put("兰", "	兰州铁路局");
		lumap.put("乌", "	乌鲁木齐铁路局");
		lumap.put("青", "	青藏铁路公司");
		logger.info("saveLkTrainTimes~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String trainStr = (String) reqObj.get("cmdTrainMap");
			JSONObject trainMap = JSONObject.fromObject(trainStr);
			List<JSONObject> trainStnList = reqObj
					.getJSONArray("cmdTrainStnList");
			String baseTrainId = StringUtil.objToStr(reqObj.get("baseTrainId"));

			// 新增字段.
			String sourceNodeStationId = (null == StringUtil.objToStr(reqObj
					.get("sourceNodeStationId")) ? "" : StringUtil
					.objToStr(reqObj.get("sourceNodeStationId")));
			String sourceNodeStationName = (null == StringUtil.objToStr(reqObj
					.get("sourceNodeStationName")) ? "" : StringUtil
					.objToStr(reqObj.get("sourceNodeStationName")));
			String sourceNodeTdcsId = (null == StringUtil.objToStr(reqObj
					.get("sourceNodeTdcsId")) ? "" : StringUtil.objToStr(reqObj
					.get("sourceNodeTdcsId")));
			String sourceNodeTdcsName = (null == StringUtil.objToStr(reqObj
					.get("sourceNodeTdcsName")) ? "" : StringUtil
					.objToStr(reqObj.get("sourceNodeTdcsName")));
			String targetNodeStationId = (null == StringUtil.objToStr(reqObj
					.get("targetNodeStationId")) ? "" : StringUtil
					.objToStr(reqObj.get("targetNodeStationId")));
			String targetNodeStationName = (null == StringUtil.objToStr(reqObj
					.get("targetNodeStationName")) ? "" : StringUtil
					.objToStr(reqObj.get("targetNodeStationName")));
			String targetNodeTdcsId = (null == StringUtil.objToStr(reqObj
					.get("targetNodeTdcsId")) ? "" : StringUtil.objToStr(reqObj
					.get("targetNodeTdcsId")));
			String targetNodeTdcsName = (null == StringUtil.objToStr(reqObj
					.get("targetNodeTdcsName")) ? "" : StringUtil
					.objToStr(reqObj.get("targetNodeTdcsName")));
			// 跟哲哥碰过之后,不修改主表信息,如有任何问题,找黄锦超.
			// if(null != trainStnList && !trainStnList.isEmpty()){
			// // 得到时刻表中的第一条信息(始发信息)
			// JSONObject startStn = trainStnList.get(0);
			// sourceNodeStationId = StringUtil.objToStr(startStn.get("stnId"));
			// sourceNodeStationName =
			// StringUtil.objToStr(startStn.get("stnName"));
			// sourceNodeTdcsId =
			// StringUtil.objToStr(startStn.get("node_Tdcs_Id"));
			// sourceNodeTdcsName =
			// StringUtil.objToStr(startStn.get("node_Tdcs_Name"));
			// // 得到时刻表中的最后一条信息(终到信息)
			// JSONObject endStn = trainStnList.get(trainStnList.size()-1);
			// targetNodeStationId =
			// StringUtil.objToStr(endStn.get("node_Tdcs_Name"));
			// targetNodeStationName = StringUtil.objToStr(endStn.get("stnId"));
			// targetNodeTdcsId = StringUtil.objToStr(endStn.get("stnName"));
			// targetNodeTdcsName =
			// StringUtil.objToStr(endStn.get("node_Tdcs_Id"));
			//
			// }

			// 用于编辑
			paramMap.put("sourceNodeStationId", sourceNodeStationId);
			paramMap.put("sourceNodeStationName", sourceNodeStationName);
			paramMap.put("sourceNodeTdcsId", sourceNodeTdcsId);
			paramMap.put("sourceNodeTdcsName", sourceNodeTdcsName);
			paramMap.put("targetNodeStationId", targetNodeStationId);
			paramMap.put("targetNodeStationName", targetNodeStationName);
			paramMap.put("targetNodeTdcsId", targetNodeTdcsId);
			paramMap.put("targetNodeTdcsName", targetNodeTdcsName);

			CmdTrain train = new CmdTrain();
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureuaShortName = user.getBureauShortName();
			String bureuaCode = user.getBureau();
			logger.debug("bureuaShortName==" + bureuaShortName);
			Integer cmdTxtMlItemId = Integer.valueOf(StringUtil
					.objToStr(trainMap.get("cmdTxtMlItemId")));
			CmdTrain cmdTrain = null;
			String cmdType = trainMap.getString("cmdType");
			if (ConstantUtil.JY_ADD_TELEGRAPH_NAME.equals(cmdType)
					|| ConstantUtil.GT_ADD_TELEGRAPH_NAME.equals(cmdType)) {
				cmdTrain = runPlanLkService
						.getCmdTrainInfoForCmdTrainId(trainMap
								.getString("cmdTrainId"));
			} else {
				// 从本地数据库中查询
				Map<String, String> reqMap = new HashMap<String, String>();
				reqMap.put("cmdTxtmlItemId", String.valueOf(cmdTxtMlItemId));
				cmdTrain = runPlanLkService
						.getCmdTrainInfoForCmdTxtmlItemId(reqMap);
				// cmdTrain = runPlanLkService
				// .getCmdTrainInfoForCmdTxtmlItemId(String
				// .valueOf(cmdTxtMlItemId));
			}

			// 列车类型的判断.
			// String typeId =
			// runPlanLkService.getMTrainlineTypeByTrainNbr(StringUtil.objToStr(trainMap.get("trainNbr")));
			// if(!StringUtils.equals(typeId,
			// StringUtil.objToStr(trainMap.get("trainTypeId")))){
			// // 选择的类型,于系统判定的类型不一致.
			// result.setCode("1");
			// result.setMessage("您选择的列车类型与您的车次不匹配");
			// return result;
			// }

			// 表cmd_train主键
			String cmdTrainId = "";
			if (cmdTrain == null) {

				// 表cmd_train主键
				cmdTrainId = UUID.randomUUID().toString();
				train.setCmdTrainId(cmdTrainId);
				train.setCmdBureau(bureuaShortName);
				train.setCmdItem(Integer.valueOf(StringUtil.objToStr(trainMap
						.get("cmdItem"))));
				train.setCmdNbrBureau(StringUtil.objToStr(trainMap
						.get("cmdNbrBureau")));
				CmdInfoModel model = runPlanLkService.getCmdTrainInfoByCmdItem(
						bureuaCode,
						StringUtil.objToStr(trainMap.get("cmdTxtMlItemId")));
				train.setImportantFlag(StringUtil.objToStr(trainMap
						.get("importantFlag")));

				// System.out.println(StringUtil.objToStr(trainMap
				// .get("cmdNbrSuperior")));
				if (StringUtil.objToStr(trainMap.get("cmdNbrSuperior")) == null
						|| StringUtil.objToStr(trainMap.get("cmdNbrSuperior")) == "null") {
					train.setCmdNbrSuperior("");
				} else {
					train.setCmdNbrSuperior(StringUtil.objToStr(trainMap
							.get("cmdNbrSuperior")));
				}

				train.setCmdTime(StringUtil.objToStr(trainMap.get("cmdTime")));
				train.setCmdTxtMlId(Integer.valueOf(StringUtil
						.objToStr(trainMap.get("cmdTxtMlId"))));
				train.setCmdTxtMlItemId(Integer.valueOf(StringUtil
						.objToStr(trainMap.get("cmdTxtMlItemId"))));
				train.setCmdType(StringUtil.objToStr(trainMap.get("cmdType")));
				train.setCreateState(StringUtil.objToStr(trainMap
						.get("createState")));
				train.setEndDate(StringUtil.objToStr(trainMap.get("endDate")));
				train.setEndStn(StringUtil.objToStr(trainMap.get("endStn")));
				train.setPassBureau(getRealPassBureau(StringUtil
						.objToStr(trainMap.get("passBureau"))));
				train.setRule(StringUtil.objToStr(trainMap.get("rule")));
				train.setSelectedDate(StringUtil.objToStr(trainMap
						.get("selectedDate")));
				train.setStartDate(StringUtil.objToStr(trainMap
						.get("startDate")));
				train.setStartStn(StringUtil.objToStr(trainMap.get("startStn")));
				train.setTrainNbr(StringUtil.objToStr(trainMap.get("trainNbr")));
				train.setUpdateTime(StringUtil.objToStr(trainMap
						.get("updateTime")));
				train.setBusiness(StringUtil.objToStr(trainMap.get("business")));
				train.setStartBureauId(StringUtil.objToStr(trainMap
						.get("startBureauId")));
				train.setStartStnId(StringUtil.objToStr(trainMap
						.get("startStnId")));
				train.setEndBureauId(StringUtil.objToStr(trainMap
						.get("endBureauId")));
				train.setEndStnId(StringUtil.objToStr(trainMap.get("endStnId")));
				train.setEndDays(StringUtil.objToStr(trainMap.get("endDays")));
				String routeId = StringUtil.objToStr(trainMap.get("routeId"));
				train.setRouteId(routeId == null || "null".equals(routeId) ? ""
						: routeId);
				train.setTrainTypeId(StringUtil.objToStr(trainMap
						.get("trainTypeId")));

				// 只要保持了选线，选线状态就应该是1
				// train.setSelectState("1");

				logger.info("saveLkTrainTimes~~==baseTrainId" + baseTrainId);

				if (!"".equals(baseTrainId) && baseTrainId != null) {
					train.setSelectState("1");
					logger.info("saveLkTrainTimes~~==baseTrainId 非空"
							+ baseTrainId);
				} else {
					train.setSelectState("0");
					logger.info("saveLkTrainTimes~~==baseTrainId 空"
							+ baseTrainId);
				}
				// train.setBaseTrainId(baseTrainId);
				train.setBaseTrainId("");
				// 新增字段.
				train.setStartStationStnId(sourceNodeStationId);
				train.setStartStationStnName(sourceNodeStationName);
				train.setStartStnTdcsId(sourceNodeTdcsId);
				train.setStartStnTdcsName(sourceNodeTdcsName);
				train.setEndStationStnId(targetNodeStationId);
				train.setEndStationStnName(targetNodeStationName);
				train.setEndStnTdcsId(targetNodeTdcsId);
				train.setEndStnTdcsName(targetNodeTdcsName);

				// 保存数据到表cmd_train中
				runPlanLkService.insertCmdTrain(train);
				isRunling = false;
			} else {
				cmdTrainId = cmdTrain.getCmdTrainId();
				// 更新途径局和选线状态
				String passBureau = getRealPassBureau(StringUtil
						.objToStr(trainMap.get("passBureau")));

				String selectState;

				if (!"".equals(baseTrainId) && baseTrainId != null) {
					selectState = "1";
				} else {
					selectState = "2";
				}

				/*
				 * "trainTypeId":"c51f335b-6b43-4555-b922-0cd42e18c78e",
				 * "business":"客运",
				 * "startBureauId":"b7586bd7-0757-4a9a-a343-3f443a62423f",
				 * "startStnId":"236189ba-d17b-4205-9c66-5d4064990586",
				 * "endBureauId":"b7586bd7-0757-4a9a-a343-3f443a62423f",
				 * "endStnId":"b8a44b37-f932-49ce-848e-fdecea26147a",
				 * "endDays":"0"}
				 */

				String business = StringUtil.objToStr(trainMap.get("business"));
				String startBureauId = StringUtil.objToStr(trainMap
						.get("startBureauId"));
				String startStnId = StringUtil.objToStr(trainMap
						.get("startStnId"));
				String endBureauId = StringUtil.objToStr(trainMap
						.get("endBureauId"));
				String endStnId = StringUtil.objToStr(trainMap.get("endStnId"));
				String endDays = StringUtil.objToStr(trainMap.get("endDays"));
				String trainTypeId = StringUtil.objToStr(trainMap
						.get("trainTypeId"));

				int CmdTxtMlId = 1;
				// train.setCmdTxtMlId(0);
				// train.setCmdTxtMlItemId(0);
				int CmdTxtMlItemId = 1;

				if (trainMap.get("cmdType").equals(
						ConstantUtil.GT_ADD_TELEGRAPH_NAME)
						|| trainMap.get("cmdType").equals(
								ConstantUtil.JY_ADD_TELEGRAPH_NAME)) {

					CmdTxtMlId = 1;
					// train.setCmdTxtMlId(0);
					// train.setCmdTxtMlItemId(0);
					CmdTxtMlItemId = 1;

				} else {

					// 2014-12-23 19:36:26 注释掉,
					// 如果是既有的,原本是42476,结果在这里赋值为1,从此再也查不出来了
					// CmdTxtMlId = 1;
					// train.setCmdTxtMlId(0);
					// train.setCmdTxtMlItemId(0);
					// CmdTxtMlItemId =1;
					CmdTxtMlId = Integer.valueOf(StringUtil.objToStr(trainMap
							.get("cmdTxtMlId")));
					CmdTxtMlItemId = Integer.valueOf(StringUtil
							.objToStr(trainMap.get("cmdTxtMlItemId")));

				}

				/*
				 * String business , String startBureauId, String startStnId ,
				 * String endBureauId , String endStnId , String endDays ,
				 * String trainTypeId , int CmdTxtMlId , int CmdTxtMlItemId
				 */

				if (passBureau != null && !"".equals(passBureau)) {
					// runPlanLkService.updatePassBureauForCmdTraindId(passBureau,
					// selectState, cmdTrainId, business, startBureauId,
					// startStnId, endBureauId, endStnId, endDays,
					// trainTypeId, CmdTxtMlId, CmdTxtMlItemId);
					// 更新CMD_TRAIN表
					runPlanLkService.updatePassBureauForCmdTraindId1(
							passBureau, selectState, cmdTrainId, business,
							startBureauId, startStnId, endBureauId, endStnId,
							endDays, trainTypeId, CmdTxtMlId, CmdTxtMlItemId,
							paramMap);
					isRunling = false;
				}

			}

			// 先删除表cmd_train_stn中对应的数据
			int count = runPlanLkService.deleteCmdTrainStnForCmdTrainId(
					cmdTrainId, null);
			logger.debug("count==" + count);
			// 循环设置子表cmd_train_stn中的数据对象
			for (int i = 0; i < trainStnList.size(); i++) {
				JSONObject trainStn = trainStnList.get(i);
				CmdTrainStn stn = new CmdTrainStn();
				// 新增字段
				stn.setStnId(null == StringUtil.objToStr(trainStn.get("stnId")) ? ""
						: StringUtil.objToStr(trainStn.getString("stnId")));
				stn.setStnName(null == StringUtil.objToStr(trainStn
						.get("stnName")) ? "" : StringUtil.objToStr(trainStn
						.getString("stnName")));
				stn.setNodeTdcsId(null == StringUtil.objToStr(trainStn
						.get("node_Tdcs_Id")) ? "" : StringUtil
						.objToStr(trainStn.getString("node_Tdcs_Id")));
				stn.setNodeTdcsName(null == StringUtil.objToStr(trainStn
						.get("node_Tdcs_Name")) ? "" : StringUtil
						.objToStr(trainStn.getString("node_Tdcs_Name")));

				stn.setCmdTrainId(cmdTrainId);
				stn.setCmdTrainStnId(UUID.randomUUID().toString());
				String arrTrainNbr = StringUtil.objToStr(trainStn
						.get("arrTrainNbr"));
				String dptTrainNbr = StringUtil.objToStr(trainStn
						.get("dptTrainNbr"));
				stn.setArrTrainNbr(arrTrainNbr == null ? "" : arrTrainNbr);
				stn.setDptTrainNbr(dptTrainNbr == null ? "" : dptTrainNbr);
				String arrTime = StringUtil.objToStr(trainStn.get("arrTime"));
				String endTime = StringUtil.objToStr(trainStn.get("dptTime"));
				String baseArrTime = StringUtil.objToStr(trainStn
						.get("baseArrTime"));
				String baseDptTime = StringUtil.objToStr(trainStn
						.get("baseDptTime"));
				if ((arrTime == null || "".equals(arrTime))
						&& (endTime != null && !"".equals(endTime))) {
					stn.setArrTime(endTime);
					stn.setDptTime(endTime);

					if (baseArrTime == null || "".equals(baseArrTime)) {
						stn.setBaseArrTime(endTime);
					} else {
						stn.setBaseArrTime(baseArrTime);
					}
					if (baseDptTime == null || "".equals(baseDptTime)) {
						stn.setBaseDptTime(endTime);
					} else {
						stn.setBaseDptTime(baseDptTime);
					}
				} else if ((endTime == null || "".equals(endTime))
						&& (arrTime != null && !"".equals(arrTime))) {
					stn.setArrTime(arrTime);
					stn.setDptTime(arrTime);
					if (baseArrTime == null || "".equals(baseArrTime)) {
						stn.setBaseArrTime(arrTime);
					} else {
						stn.setBaseArrTime(baseArrTime);
					}
					if (baseDptTime == null || "".equals(baseDptTime)) {
						stn.setBaseDptTime(arrTime);
					} else {
						stn.setBaseDptTime(baseDptTime);
					}
				} else {
					stn.setArrTime(arrTime);
					stn.setDptTime(endTime);
					if (baseArrTime == null || "".equals(baseArrTime)) {
						stn.setBaseArrTime(arrTime);
					} else {
						stn.setBaseArrTime(baseArrTime);
					}
					if (baseDptTime == null || "".equals(baseDptTime)) {
						stn.setBaseDptTime(endTime);
					} else {
						stn.setBaseDptTime(baseDptTime);
					}
				}

				String plantFormStr = StringUtil.objToStr(trainStn
						.get("platform"));
				Integer plantForm = Integer
						.valueOf("".equals(plantFormStr) ? "0" : plantFormStr);
				stn.setPlatform(plantForm == null ? 0 : plantForm);
				stn.setStnBureau(StringUtil.objToStr(trainStn.get("stnBureau")));
				// System.out.println(lumap.get(StringUtil.objToStr(trainStn.get("stnBureau"))).trim());
				stn.setStnBureauFull(lumap.get(
						StringUtil.objToStr(trainStn.get("stnBureau"))).trim());
				// stn.setStnName(StringUtil.objToStr(trainStn.get("stnName")));

				stn.setStnType(StringUtil.objToStr(trainStn.get("stnType")));// 车站类型
																				// (1:始发站；2:终到站；4:分界口)
				stn.setTecType(StringUtil.objToStr(trainStn.get("tecType")));
				stn.setTrackNbr(StringUtil.objToStr(trainStn.get("trackNbr")));
				stn.setStnSort((Integer) trainStn.get("childIndex"));

				int psgFlg = trainStn.get("psgFlg") == null ? 0 : Integer
						.valueOf(trainStn.get("psgFlg").toString());
				// int arrRunDays = trainStn.get("arrRunDays") == null ? 0
				// : Integer
				// .valueOf(trainStn.get("arrRunDays").toString());
				// int dptRunDays = trainStn.get("dptRunDays") == null ? 0
				// : Integer
				// .valueOf(trainStn.get("dptRunDays").toString());
				int arrRunDays = (trainStn.get("arrRunDays") == null || trainStn
						.get("arrRunDays").equals("--")) ? 0 : Integer
						.valueOf(trainStn.get("arrRunDays").toString());
				int dptRunDays = (trainStn.get("dptRunDays") == null || trainStn
						.get("dptRunDays").equals("--")) ? 0 : Integer
						.valueOf(trainStn.get("dptRunDays").toString());
				int locoFlag = trainStn.get("locoFlag") == null ? 0 : Integer
						.valueOf(trainStn.get("locoFlag").toString());
				String jobs = StringUtil.objToStr(trainStn.get("jobs"));
				if(jobs==null || jobs.equals("null")){
					jobs = "";
				}
//				 注释jobs，2015-5-26 15:10:25，哲哥不让添加
				 JSONObject ky = JSONObject.fromObject(trainStn.getString("kyyy"));
				 String kyyy = String.valueOf(ky.getString("text"));
				 if ("是".equals(kyyy)) {
				 if (!jobs.contains(ConstantUtil.KY_STATION_FLAG_NO)) {
				 jobs = jobs + ConstantUtil.KY_STATION_FLAG;
				 }
				 } else {
				 jobs = jobs.replace(ConstantUtil.KY_STATION_FLAG, "");
				 }
				stn.setPsgFlg(psgFlg);
				stn.setArrRunDays(arrRunDays);
				stn.setDptRunDays(dptRunDays);
				stn.setLocoFlag(locoFlag);
				stn.setStnBureauId(StringUtil.objToStr(trainStn.get("bureauId")));
				stn.setNodeId(StringUtil.objToStr(trainStn.get("nodeId")));
				stn.setNodeName(StringUtil.objToStr(trainStn.get("nodeName")));
				stn.setTrackName(StringUtil.objToStr(trainStn.get("trackName")));

				// 注释jobs，2015-5-26 15:10:25，哲哥不让添加
				if (i == 0) {// 第一行数据
					if (!jobs.contains("始发")) {
						// jobs = "<始发>" + jobs;
						jobs = jobs + ConstantUtil.START_STATION_FLAG;
					}
					startStn = StringUtil.objToStr(trainStn.get("nodeName"));
				} else if (i == trainStnList.size() - 1) {// 最后一行数据
					if (!jobs.contains("终到")) {
						// jobs = "<终到>" + jobs;
						jobs = jobs + ConstantUtil.END_STATION_FLAG;
					}
					endStn = StringUtil.objToStr(trainStn.get("nodeName"));
				} else {
					if (!jobs.contains("经由")) {
						// jobs = "<经由>" + jobs;
						jobs = jobs + "<经由>";
					}
				}
				stn.setJobs(jobs);

				// 保存数据到表cmd_train_stn中
				runPlanLkService.insertCmdTrainStn(stn);

				isRunling = false;
			}
			startNode = commonService.getNodeByName(startStn);
			if(StringUtils.isNotEmpty(endStn)){
				endNode = commonService.getNodeByName(endStn);
			}
			runPlanLkService.updateCmdTrain(startStn, endStn, cmdTrainId,
					startNode.getBureauId(), endNode.getBureauId());
			isRunling = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		isRunling = true;
		return result;
	}

	/**
	 * 获取站的基本信息
	 * 
	 * @param
	 * @return 
	 *         data中为list，list中的map的key有：STNNAME(站名)，STNBUREAU(局的拼音码)，STNBUREAUFULLNAME
	 *         （局全称），STNBUREAUSHORTNAME（局简称）
	 */
	@ResponseBody
	@RequestMapping(value = "/getBaseStationInfo", method = RequestMethod.POST)
	public Result getBaseStationInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		String stnName = StringUtil.objToStr(reqMap.get("stnName"));
		try {
			List<Map<String, Object>> list = runPlanLkService
					.getBaseStationInfo(stnName);
			result.setData(list);
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
			String bureauBefore = "";
			String bureau = "";
			String bureauAfter = "";
			for (int i = 0; i < list.size(); i++) {
				Station station = list.get(i);
				if (station != null) {
					if ("FJK".equals(station.getStationType())
							&& i + 1 < list.size() && i > 0) {
						Station stationBefore = list.get(i - 1);
						Station stationAfter = list.get(i + 1);

						bureauBefore = stationBefore.getStnName().substring(
								stationBefore.getStnName().indexOf("[") + 1,
								stationBefore.getStnName().indexOf("]"));
						bureau = station.getStnName().substring(
								station.getStnName().indexOf("[") + 1,
								station.getStnName().indexOf("]"));
						bureauAfter = stationAfter.getStnName().substring(
								stationAfter.getStnName().indexOf("[") + 1,
								stationAfter.getStnName().indexOf("]"));

						if (bureau.equals(bureauBefore)
								&& bureau.equals(bureauAfter)) {
							list.get(i).setStationType("TZ");
						}
					}
					// for (Station station : list) {
					// 0:默认的isCurrentBureau
					planLineGridYList.add(new PlanLineGridY(station
							.getStnName(), 0, station.getStationType()));
					// }
				}
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

	/**
	 * 复制历史，查询数据.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/querycmdTrain", method = RequestMethod.POST)
	public Result querycmdTrain(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			// logger.info("querycmdTrain~~reqMap=" + reqMap);
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 本局
			// String bureuaName = user.getBureauShortName();
			// logger.debug("bureuaName==" + bureuaName);
			// reqMap.put("cmdBureau", bureuaName);
			// reqMap.put("operation", "客运");
			// 调用后台接口
			// PagingResult page = new PagingResult(
			// runPlanLkService.getCmdTrainfoCount(reqMap),
			// runPlanLkService.getCmdTrainForPage(reqMap));

			/**
			 * 注： 1. 为什么if判断的时候，判断名字，不是判断对应的code ?
			 * 之前同事完成的既有临客加开已经这样，为了让高铁与既有统一，所以这样
			 * ，以后接口服务端更换名字后，会查询不到数据，到时候需要根据code查询。 2. 为什么文电和命令的路局参数不同，文电：京，命令：P
			 * ? 因为做临客加开功能的同事是这样定义的，没有闲工夫修改之前的传值。
			 * 
			 * zpd 2015-4-23 10:38:25.
			 */
			if (StringUtils.equals(MapUtils.getString(reqMap, "cmdType"),
					"既有加开文电")
					|| StringUtils.equals(
							MapUtils.getString(reqMap, "cmdType"), "高铁加开文电")) {
				reqMap.put("cmdBureau", user.getBureauShortName());
				result.setData(runPlanLkService.getCmdTrainForPage(reqMap));
			} else if (StringUtils.equals(
					MapUtils.getString(reqMap, "cmdType"), "既有加开命令")
					|| StringUtils.equals(
							MapUtils.getString(reqMap, "cmdType"), "高铁加开命令")) {
				result.setData(getLkMlData(
						MapUtils.getString(reqMap, "startDate"),
						MapUtils.getString(reqMap, "endDate"),
						user.getBureau(),
						MapUtils.getString(reqMap, "trainNbr"),
						MapUtils.getString(reqMap, "cmdNbrBureau"),
						MapUtils.getString(reqMap, "cmdNbrSuperior"),
						MapUtils.getString(reqMap, "cmdType")));

			}
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
			String cmdTrainId = StringUtil.objToStr(reqMap.get("cmdTrainId"));
			List<CmdTrainStn> times = runPlanLkService
					.getCmdTrainStnInfo(cmdTrainId);
			result.setData(times);
			logger.info("queryTrainTimes~~trainId==" + cmdTrainId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 保存文电命令
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveWdCmdTrain", method = RequestMethod.POST)
	public Result saveWdCmdTrain(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("saveWdCmdTrain~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String trainStr = (String) reqObj.get("cmdTrainMap");
			JSONObject trainMap = JSONObject.fromObject(trainStr);
			// List<JSONObject> trainStnList =
			// reqObj.getJSONArray("cmdTrainStnList");
			CmdTrain train = new CmdTrain();
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureuaShortName = user.getBureauShortName();

			// 是否已存在.
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cmd_bureau", bureuaShortName);
			paramMap.put("train_nbr", trainMap.getString("trainNbr"));
			paramMap.put("start_stn", trainMap.getString("startStn"));
			paramMap.put("end_stn", trainMap.getString("endStn"));
			paramMap.put("start_date", trainMap.getString("startDate"));
			paramMap.put("end_date", trainMap.getString("endDate"));
			paramMap.put("rule", trainMap.getJSONObject("wdCmdRuleOption")
					.getString("text"));
			paramMap.put("selected_date", trainMap.getString("selectedDate"));
			List<Map<String, Object>> lm = runPlanLkService
					.getCmdTrainByMap(paramMap);
			if (!lm.isEmpty()) {
				result.setCode(StaticCodeType.SYSTEM_DATA__IS_EXIST.getCode());
				result.setMessage(StaticCodeType.SYSTEM_DATA__IS_EXIST
						.getDescription());
				return result;
			}

			// 表cmd_train主键
			String cmdTrainId = UUID.randomUUID().toString();

			// 封装数据
			train.setCmdTrainId(cmdTrainId);
			train.setCmdBureau(bureuaShortName);
			// train.setCmdNbrBureau(trainMap.getString("cmdNbrBureau"));

			train.setBaseTrainId("");
			train.setCmdItem(Integer.valueOf(StringUtil.objToStr(trainMap
					.get("cmdItem"))));
			train.setCmdTxtMlId(1);
			train.setCmdTxtMlItemId(1);
			train.setCmdType(trainMap.getString("cmdType"));
			train.setCreateState("0");
			train.setPassBureau("");
			train.setImportantFlag(trainMap.getString("importantFlag"));
			train.setCmdNbrBureau("null".equals(trainMap
					.getString("cmdNbrBureau")) ? "" : trainMap
					.getString("cmdNbrBureau"));
			train.setCmdNbrSuperior("null".equals(trainMap
					.getString("cmdNbrSuperior")) ? "" : trainMap
					.getString("cmdNbrSuperior"));
			train.setCmdTime("null".equals(trainMap.getString("cmdTime")) ? ""
					: trainMap.getString("cmdTime"));
			train.setEndDate("null".equals(trainMap.getString("endDate")) ? ""
					: trainMap.getString("endDate"));
			train.setEndStn("null".equals(trainMap.getString("endStn")) ? ""
					: trainMap.getString("endStn"));
			train.setRule(trainMap.getJSONObject("wdCmdRuleOption").getString(
					"text"));
			train.setSelectedDate("null".equals(trainMap
					.getString("selectedDate")) ? "" : trainMap
					.getString("selectedDate"));
			train.setStartDate("null".equals(trainMap.getString("startDate")) ? ""
					: trainMap.getString("startDate"));
			train.setStartStn("null".equals(trainMap.getString("startStn")) ? ""
					: trainMap.getString("startStn"));
			train.setTrainNbr("null".equals(trainMap.getString("trainNbr")) ? ""
					: trainMap.getString("trainNbr"));

			train.setUpdateTime(DateUtil.getStringTimestampFormat(System
					.currentTimeMillis()));
			train.setBusiness("");
			train.setStartBureauId("");
			train.setStartStnId("");
			train.setEndBureauId("");
			train.setEndStnId("");
			train.setEndDays("");
			train.setRouteId("");
			train.setTrainTypeId("");

			train.setSelectState("0");

			// 保存数据到表cmd_train中
			runPlanLkService.insertCmdTrain(train);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 删除文电命令
	 * 
	 * @param reqStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteWdCmd", method = RequestMethod.POST)
	public Result deleteWdCmd(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("deleteWdCmd~~reqStr==" + reqStr);

		JSONObject reqObj = JSONObject.fromObject(reqStr);

		try {
			List<String> cmdTrainIds = null;
			cmdTrainIds = reqObj.getJSONArray("cmdTrainIds");
			//
			if (!cmdTrainIds.isEmpty()) {
				for (String cmdTrainId : cmdTrainIds) {
					runPlanLkService.deleteCmdTrainForCmdTrainId(cmdTrainId);
					runPlanLkService.deleteCmdTrainStnForCmdTrainId(cmdTrainId,
							null);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 编辑文电命令
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editWdCmdTrain", method = RequestMethod.PUT)
	public Result editWdCmdTrain(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("editWdCmdTrain~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String trainStr = (String) reqObj.get("cmdTrainMap");
			JSONObject trainMap = JSONObject.fromObject(trainStr);
			// List<JSONObject> trainStnList =
			// reqObj.getJSONArray("cmdTrainStnList");
			CmdTrain train = null;
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureuaShortName = user.getBureauShortName();
			logger.debug("bureuaShortName==" + bureuaShortName);

			// logger.info("cmdTrainMap data==" +
			// trainMap.getString("startStn"));

			// 查询出CmdTrain实体
			train = runPlanLkService.getCmdTrainInfoForCmdTrainId(trainMap
					.getString("cmdTrainId"));

			train.setCmdBureau(bureuaShortName);
			// train.setCmdNbrBureau(trainMap.getString("cmdNbrBureau"));

			train.setCmdItem(trainMap.getInt("cmdItem"));
			train.setImportantFlag(trainMap.getString("importantFlag"));
			train.setCmdNbrBureau("null".equals(trainMap
					.getString("cmdNbrBureau")) ? "" : trainMap
					.getString("cmdNbrBureau"));
			train.setCmdNbrSuperior("null".equals(trainMap
					.getString("cmdNbrSuperior")) ? "" : trainMap
					.getString("cmdNbrSuperior"));
			train.setCmdTime("null".equals(trainMap.getString("cmdTime")) ? ""
					: trainMap.getString("cmdTime"));
			train.setEndDate("null".equals(trainMap.getString("endDate")) ? ""
					: trainMap.getString("endDate"));
			train.setEndStn("null".equals(trainMap.getString("endStn")) ? ""
					: trainMap.getString("endStn"));
			train.setRule(trainMap.getJSONObject("wdCmdRuleOption").getString(
					"text"));
			train.setSelectedDate("null".equals(trainMap
					.getString("selectedDate")) ? "" : trainMap
					.getString("selectedDate"));
			train.setStartDate("null".equals(trainMap.getString("startDate")) ? ""
					: trainMap.getString("startDate"));
			train.setStartStn("null".equals(trainMap.getString("startStn")) ? ""
					: trainMap.getString("startStn"));
			train.setTrainNbr("null".equals(trainMap.getString("trainNbr")) ? ""
					: trainMap.getString("trainNbr"));
			// train.setUpdateTime(DateUtil.getStringTimestampFormat(System
			// .currentTimeMillis()));

			// 保存数据到表cmd_train中
			runPlanLkService.updateCmdTrainForCmdTraindId(train);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据文电命令ID获取文电命令
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/stopCmdTrain", method = RequestMethod.POST)
	public Result stopCmdTrain(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("stopCmdTrain~~reqStr==" + reqStr);
		/**
		 * 
		 */
		try {

			List<String> cmdTraindIdList = new ArrayList<String>();
			JSONArray reqArray = JSONArray.fromObject(reqStr);

			// String trainStr = (String) reqObj.get("cmdTrainMap");
			// JSONObject trainMap = JSONObject.fromObject(trainStr);
			if (!reqArray.isEmpty()) {
				for (int i = 0; i < reqArray.size(); i++) {
					JSONObject trainMap = reqArray.getJSONObject(i);

					CmdTrain train = new CmdTrain();
					ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
							.getSubject().getPrincipal();
					// 局简称
					String bureuaShortName = user.getBureauShortName();
					String bureuaCode = user.getBureau();
					logger.debug("bureuaShortName==" + bureuaShortName);
					Integer cmdTxtMlItemId = Integer.valueOf(StringUtil
							.objToStr(trainMap.get("cmdTxtMlItemId")));
					CmdTrain cmdTrain = null;
					String cmdType = trainMap.getString("cmdType");
					String cmdTrainId = "";
					if (ConstantUtil.JY_STOP_TELEGRAPH_NAME.equals(cmdType)
							|| ConstantUtil.GT_STOP_TELEGRAPH_NAME
									.equals(cmdType)) {
						cmdTrain = runPlanLkService
								.getCmdTrainInfoForCmdTrainId(trainMap
										.getString("cmdTrainId"));

						if (cmdTrain == null) {

							// 表cmd_train主键
							cmdTrainId = UUID.randomUUID().toString();
							train.setCmdTrainId(cmdTrainId);
							train.setCmdBureau(bureuaShortName);
							train.setCmdItem(Integer.valueOf(StringUtil
									.objToStr(trainMap.get("cmdItem"))));

							if (StringUtil.objToStr(trainMap
									.get("cmdNbrBureau")) == null
									|| StringUtil.objToStr(trainMap
											.get("cmdNbrBureau")) == "null") {
								train.setCmdNbrBureau("");
								train.setCmdNbrSuperior("");
							} else {
								train.setCmdNbrBureau(StringUtil
										.objToStr(trainMap.get("cmdNbrBureau")));
								train.setCmdNbrSuperior(StringUtil
										.objToStr(trainMap
												.get("cmdNbrSuperior")));
							}

							train.setCmdTime(StringUtil.objToStr(trainMap
									.get("cmdTime")));
							train.setCmdTxtMlId(Integer.valueOf(StringUtil
									.objToStr(trainMap.get("cmdTxtMlId"))));

							train.setCmdTxtMlItemId(Integer.valueOf(StringUtil
									.objToStr(trainMap.get("cmdTxtMlItemId"))));

							// runPlanLkService.getCmdTrainInfoByCmdItem(bureuaCode,
							// cmdItemId)

							CmdInfoModel model = runPlanLkService
									.getCmdTrainInfoByCmdItem(bureuaCode,
											StringUtil.objToStr(trainMap
													.get("cmdTxtMlItemId")));
							train.setImportantFlag(model.isImportCmdFlag() ? "1"
									: "0");

							train.setCmdType(StringUtil.objToStr(trainMap
									.get("cmdType")));
							train.setCreateState(StringUtil.objToStr(trainMap
									.get("createState")));
							train.setEndDate(StringUtil.objToStr(trainMap
									.get("endDate")));
							train.setEndStn("");
							train.setPassBureau("");
							train.setRule(StringUtil.objToStr(trainMap
									.get("rule")));
							train.setSelectedDate(StringUtil.objToStr(trainMap
									.get("selectedDate")));
							train.setStartDate(StringUtil.objToStr(trainMap
									.get("startDate")));

							if (StringUtil.objToStr(trainMap.get("startStn")) == null
									|| StringUtil.objToStr(trainMap
											.get("startStn")) == "null") {
								train.setStartStn("");
							} else {
								train.setStartStn(StringUtil.objToStr(trainMap
										.get("startStn")));
							}

							train.setTrainNbr(StringUtil.objToStr(trainMap
									.get("trainNbr")));
							train.setUpdateTime(StringUtil.objToStr(trainMap
									.get("updateTime")));
							train.setBusiness(StringUtil.objToStr(trainMap
									.get("business")));
							train.setStartBureauId(StringUtil.objToStr(trainMap
									.get("startBureauId")));
							train.setStartStnId(StringUtil.objToStr(trainMap
									.get("startStnId")));
							train.setEndBureauId(StringUtil.objToStr(trainMap
									.get("endBureauId")));
							train.setEndStnId(StringUtil.objToStr(trainMap
									.get("endStnId")));
							train.setEndDays(StringUtil.objToStr(trainMap
									.get("endDays")));
							String routeId = StringUtil.objToStr(trainMap
									.get("routeId"));
							train.setRouteId(routeId == null
									|| "null".equals(routeId) ? "" : routeId);
							train.setTrainTypeId(StringUtil.objToStr(trainMap
									.get("trainTypeId")));

							train.setSelectState("0");
							train.setBaseTrainId("");

							// 保存数据到表cmd_train中
							runPlanLkService.insertCmdTrain(train);
							cmdTraindIdList.add(train.getCmdTrainId());
						} else {
							cmdTraindIdList.add(trainMap
									.getString("cmdTrainId"));

						}

					} else {
						// 从本地数据库中查询
						Map<String, String> reqMap1 = new HashMap<String, String>();
						reqMap1.put("cmdTxtmlItemId",
								String.valueOf(cmdTxtMlItemId));
						cmdTrain = runPlanLkService
								.getCmdTrainInfoForCmdTxtmlItemId(reqMap1);
						// cmdTrain = runPlanLkService
						// .getCmdTrainInfoForCmdTxtmlItemId(String
						// .valueOf(cmdTxtMlItemId));
						if (cmdTrain == null) {

							// 表cmd_train主键
							cmdTrainId = UUID.randomUUID().toString();
							train.setCmdTrainId(cmdTrainId);
							train.setCmdBureau(bureuaShortName);
							train.setCmdItem(Integer.valueOf(StringUtil
									.objToStr(trainMap.get("cmdItem"))));

							if (StringUtil.objToStr(trainMap
									.get("cmdNbrBureau")) == null
									|| StringUtil.objToStr(trainMap
											.get("cmdNbrBureau")) == "null") {
								train.setCmdNbrBureau("");

							} else {
								train.setCmdNbrBureau(StringUtil
										.objToStr(trainMap.get("cmdNbrBureau")));

							}

							if (StringUtil.objToStr(trainMap
									.get("cmdNbrSuperior")) == null
									|| StringUtil.objToStr(trainMap
											.get("cmdNbrSuperior")) == "null") {
								train.setCmdNbrSuperior("");

							} else {
								train.setCmdNbrSuperior(StringUtil
										.objToStr(trainMap
												.get("cmdNbrSuperior")));

							}

							CmdInfoModel model = runPlanLkService
									.getCmdTrainInfoByCmdItem(bureuaCode,
											StringUtil.objToStr(trainMap
													.get("cmdTxtMlItemId")));
							train.setImportantFlag(model.isImportCmdFlag() ? "1"
									: "0");

							train.setCmdTime(StringUtil.objToStr(trainMap
									.get("cmdTime")));
							train.setCmdTxtMlId(Integer.valueOf(StringUtil
									.objToStr(trainMap.get("cmdTxtMlId"))));
							train.setCmdTxtMlItemId(Integer.valueOf(StringUtil
									.objToStr(trainMap.get("cmdTxtMlItemId"))));
							train.setCmdType(StringUtil.objToStr(trainMap
									.get("cmdType")));
							train.setCreateState(StringUtil.objToStr(trainMap
									.get("createState")));
							train.setEndDate(StringUtil.objToStr(trainMap
									.get("endDate")));
							train.setEndStn("");
							train.setPassBureau("");
							train.setRule(StringUtil.objToStr(trainMap
									.get("rule")));
							train.setSelectedDate(StringUtil.objToStr(trainMap
									.get("selectedDate")));
							train.setStartDate(StringUtil.objToStr(trainMap
									.get("startDate")));
							// train.setStartStn(StringUtil.objToStr(trainMap
							// .get("startStn")));

							if (StringUtil.objToStr(trainMap.get("startStn")) == null
									|| StringUtil.objToStr(trainMap
											.get("startStn")) == "null") {
								train.setStartStn("");
							} else {
								train.setStartStn(StringUtil.objToStr(trainMap
										.get("startStn")));
							}
							train.setTrainNbr(StringUtil.objToStr(trainMap
									.get("trainNbr")));
							train.setUpdateTime(StringUtil.objToStr(trainMap
									.get("updateTime")));
							train.setBusiness(StringUtil.objToStr(trainMap
									.get("business")));
							train.setStartBureauId(StringUtil.objToStr(trainMap
									.get("startBureauId")));
							train.setStartStnId(StringUtil.objToStr(trainMap
									.get("startStnId")));
							train.setEndBureauId(StringUtil.objToStr(trainMap
									.get("endBureauId")));
							train.setEndStnId(StringUtil.objToStr(trainMap
									.get("endStnId")));
							train.setEndDays(StringUtil.objToStr(trainMap
									.get("endDays")));
							String routeId = StringUtil.objToStr(trainMap
									.get("routeId"));
							train.setRouteId(routeId == null
									|| "null".equals(routeId) ? "" : routeId);
							train.setTrainTypeId(StringUtil.objToStr(trainMap
									.get("trainTypeId")));

							train.setSelectState("0");
							train.setBaseTrainId("");

							// 保存数据到表cmd_train中
							runPlanLkService.insertCmdTrain(train);
							cmdTraindIdList.add(train.getCmdTrainId());
						} else {
							cmdTraindIdList.add(trainMap
									.getString("cmdTrainId"));
						}
					}

					// 表cmd_train主键

				}
			}

			int stopCount = runPlanLkService
					.updateRunPlanByStopCmdTrain(cmdTraindIdList);
			result.setMessage(cmdTraindIdList.size() + "条停运命令执行成功，停运了"
					+ stopCount + "个列车");

			logger.info(cmdTraindIdList.size() + "条停运命令执行成功，停运了" + stopCount
					+ "个列车");

			result.setData(null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		result.setData(null);
		return result;
	}

	/**
	 * 批量停运
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editWdCmdTrain/{cmdTrainId}", method = RequestMethod.GET)
	public Result getWdCmdTrain(@PathVariable("cmdTrainId") String cmdTrainId) {
		Result result = new Result();
		logger.info("getWdCmdTrain~~reqStr==" + cmdTrainId);
		try {

			CmdTrain train = null;
			train = runPlanLkService.getCmdTrainInfoForCmdTrainId(cmdTrainId);
			result.setData(train);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据命令id，查询命令详细信息
	 * 
	 * @param cmdTrainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCmdInfo/{cmdTrainId}", method = RequestMethod.GET)
	public Result getCmdTrain(@PathVariable("cmdTrainId") String cmdTrainId) {
		Result result = new Result();
		logger.info("getCmdTrain~~reqStr==" + cmdTrainId);
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局码
			String bureuaCode = user.getBureau();

			CmdInfo cmdInfo = new CmdInfo();
			CmdTrain train = null;
			train = runPlanLkService.getCmdTrainInfoForCmdTrainId(cmdTrainId);
			BeanUtils.copyProperties(train, cmdInfo);

			CmdInfoModel model = runPlanLkService.getCmdTrainInfoByCmdItem(
					bureuaCode, String.valueOf(train.getCmdTxtMlItemId()));
			if (null != model) {
				cmdInfo.setReleasePeople(model.getCmdReleasePeople());
				cmdInfo.setLargeContent(model.getLargeContent());
			}

			result.setData(cmdInfo);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

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
	@RequestMapping(value = "/getLkTrainModifyRecords", method = RequestMethod.POST)
	public Result getTrainModifyRecords(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String planTrainId = StringUtil.objToStr(reqMap.get("planTrainId"));
		StringBuilder ids = new StringBuilder();
		if (planTrainId.indexOf(",") != -1) {
			String[] str = planTrainId.split(",");
			for (int i = 0; i < str.length; i++) {
				if (i == 0) {
					ids.append("'").append(str[i]).append("'");
				} else {
					ids.append(",'").append(str[i]).append("'");
				}
			}
		} else {
			ids.append("'").append(planTrainId).append("'");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		// 查看列车调整记录
		List<ModifyPlanDTO> modifys = modifyService
				.getModifyRecordsByPlanTrainId1(map);

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
							&& md.getStartDate().getTime() == modifys
									.get(i - 1).getStartDate().getTime()
							&& md.getEndDate().getTime() == modifys.get(i - 1)
									.getEndDate().getTime()
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

	private String getStartRunDatesLK(String minDate,
			TrainLineSubInfoTime trian2) {
		String startDateTemp2 = "".equals(trian2.getArrTime()) ? trian2
				.getDptTime() : trian2.getArrTime();
		if (!"".equals(minDate) && !"".equals(minDate)
				&& !"".equals(startDateTemp2) && !"".equals(startDateTemp2)) {
			String startDate2 = DateUtil.mulDateOneDay(DateUtil
					.parseDateTOyyyymmdd(startDateTemp2));
			if (DateUtil.isBefore(minDate, startDate2)) {
				return minDate;
			} else {
				return startDate2;
			}
		} else
			return minDate;
	}

	private String getEndRunDatesLK(String maxDate, TrainLineSubInfoTime trian2) {
		String startDateTemp2 = "".equals(trian2.getDptTime()) ? trian2
				.getArrTime() : trian2.getDptTime();
		String startDate2 = DateUtil.addDateOneDay(DateUtil
				.parseDateTOyyyymmdd(startDateTemp2));
		if (!"".equals(maxDate) && !"".equals(maxDate)
				&& !"".equals(startDateTemp2) && !"".equals(startDateTemp2)) {
			if (DateUtil.isBefore(maxDate, startDate2)) {
				return startDate2;
			} else {
				return maxDate;
			}
		} else
			return maxDate;
	}

	/**
	 * 查询m_trainline_type数据.
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMTrainlineType", method = RequestMethod.POST)
	public Result getMTrainlineType(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		Map<String, Object> paramMap = new HashMap<String, Object>();

		try {
			// 车次.
			String trainNbr = (String) reqMap.get("trainNbr");
			// 列车类型.
			String typeId = (String) reqMap.get("typeId");
			List<Map<String, Object>> list = runPlanLkService
					.getMTrainlineTypeByMap(paramMap);

			// // 获取车次对用的类型,进行类型的排序
			// String lx = "";
			// if (null != list && !list.isEmpty()) {
			// // 判断类型是否已存在,存在直接排序,不存在的需要根据车次查询出对应的类型,然后再排序
			// if (null != typeId && !"".equals(typeId)) {
			// lx = typeId;
			// } else {
			// lx = runPlanLkService.getMTrainlineTypeByTrainNbr(trainNbr);
			// }
			// }
			/**
			 * 出现的列车：山，DJ888，G1，G2003， mmm，啊大，50002，00159，F
			 */
			String lx = "";
			if (!list.isEmpty() && StringUtils.isNotEmpty(trainNbr)) {
				boolean b = false;
				String[] ss = trainNbr.split("\\D+");
				int jq = 2;
				int ss2 = 0;
				if (ss.length > 1) {
					ss2 = Integer.parseInt(ss[1]);
				} else {
					// 车次中不包含字母，纯数字；所以不需要判断identify
					if (null != ss && !"".equals(ss) && ss.length > 0) {
						ss2 = Integer.parseInt(ss[0]);
						b = true;
//					} else {
//						jq = 1;
					}
					jq = 1;
				}
				outterLoop: for (int i = 0; i < trainNbr.length(); i++) {
					for (int j = 0; j < list.size(); j++) {
						Map<String, Object> map = list.get(j);
						if (null == MapUtils.getInteger(map, "SCODE")
								|| null == MapUtils.getInteger(map, "TCODE")) {
							// code等于null，只判断identify，或者直接返回71
							if (StringUtils.equalsIgnoreCase(
									trainNbr.substring(0, jq),
									MapUtils.getString(map, "IDENTIFY"))) {
								lx = MapUtils.getString(map, "ID");
								break outterLoop;
							}
						} else {
							if (b) {
								// 00159; 1: 00 159; 2: 0 0159
								if (StringUtils.equalsIgnoreCase(
										trainNbr.substring(0, jq),
										MapUtils.getString(map, "IDENTIFY"))
										&& MapUtils.getInteger(map, "SCODE") <= Integer
												.parseInt(trainNbr.substring(
														jq, trainNbr.length()))
										&& MapUtils.getInteger(map, "TCODE") >= Integer
												.parseInt(trainNbr.substring(
														jq, trainNbr.length()))) {
									lx = MapUtils.getString(map, "ID");
									break outterLoop;
								} else if (StringUtils.equalsIgnoreCase(null,
										MapUtils.getString(map, "IDENTIFY"))
										&& MapUtils.getInteger(map, "SCODE") <= Integer
												.parseInt(trainNbr)
										&& MapUtils.getInteger(map, "TCODE") >= Integer
												.parseInt(trainNbr)) {
									// 50002; SCODE >= 50002; TCODE...;
									lx = MapUtils.getString(map, "ID");
									break outterLoop;
								}
							} else {
								// 从第一位开始截不行，例如：DJ888，最后会找到(跨局动车组-D-1-4000)
								// 决定从第二位开始截，为什么不从第三位，因为数据库里identify最大就2位
								if (StringUtils.equalsIgnoreCase(
										trainNbr.substring(0, jq),
										MapUtils.getString(map, "IDENTIFY"))
										&& MapUtils.getInteger(map, "SCODE") <= ss2
										&& MapUtils.getInteger(map, "TCODE") >= ss2) {
									lx = MapUtils.getString(map, "ID");
									break outterLoop;
								}
							}
						}
					}
					if (jq > 1) {
						jq--;
					}
				}
			}

			result.setData(list);
			result.setStr1("" != lx ? lx : "71");
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	private String getRealPassBureau(String passBureau) {
		String allBureauShortName = bureauDao.getAllBureauShortName().replace(
				",", "");
		char[] charArray = passBureau.toCharArray();
		StringBuffer passBureauBuffer = new StringBuffer();
		for (char c : charArray) {
			if (allBureauShortName.contains(String.valueOf(c))) {
				passBureauBuffer.append(String.valueOf(c));
			}
		}
		return passBureauBuffer.toString();
	}

	/**
	 * 管理临客，删除.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteRunPlanLk", method = RequestMethod.POST)
	public Result deleteRunPlanLk(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String cmdIds = (String) reqMap.get("cmdIds");
			if (null != cmdIds && !"".equals(cmdIds)) {
				String[] cmdIdss = cmdIds.split(",");
				if (null != cmdIdss && cmdIdss.length > 0) {
					// 用户信息
					// ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)
					// SecurityUtils
					// .getSubject().getPrincipal();
					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("modify_reason", "管理临客，删除数据");
					// map.put("modify_people", user.getName());
					// map.put("modify_people_org", user.getBureauFullName());
					// map.put("modify_people_bureau",
					// user.getBureauShortName());
					// map.put("telIdOrCmdTrainId",
					// SqlUtil.strArrayToList(cmdIdss));
					// // add modify
					// modifyPlanService.addModifyList1(map);
					// del

					reqMap.put("idList", SqlUtil.strArrayToList(cmdIdss));
					runPlanLkService.deleteRunPlanLk(reqMap);
					// del 1列车1条线
					runPlanLkService.deleteMTrainLineByMap(reqMap);
				}
			} else {
				result.setCode(StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				result.setMessage(StaticCodeType.SYSTEM_PARAM_LOST
						.getDescription());
			}

		} catch (Exception e) {
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 取命令时刻(取命令时刻：调用ICmdAdapterService的findCmdTrainStnByCmdItemId(String
	 * cmdItemId)).
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findCmdTrainStn", method = RequestMethod.POST)
	public Result findCmdTrainStnByCmdItemId(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		// 构造服务实例
		ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
		// 服务初始化
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		service.initilize(user.getBureau());
		List<CmdTrainStn> stnList = service.findCmdTrainStnByCmdItemId(MapUtils.getString(
				reqMap, "cmdTxtMlItemId"));
		//站名对应局简称计算
		for(CmdTrainStn stn :stnList){
			if (commonService.getNodeNameMapping()
					.containsKey(stn.getNodeName())) {
				stn.setStnBureau(commonService
						.getNodeNameMapping()
						.get(stn.getNodeName())
						.getBureauShortName());
			}
		}
		result.setData(stnList);
		return result;
	}

	/**
	 * 导入文件时刻(导入Excel文件中的时刻信息：调用ICmdAdapterService的importCmdTrainStnByExcel(
	 * String trainNbr)).
	 * 
	 * @param reqMap
	 * @return
	 */
	// @ResponseBody
	// @RequestMapping(value = "/importCmdTrainStnByExcel", method =
	// RequestMethod.POST)
	// public Result importCmdTrainStnByExcel(@RequestBody Map<String, Object>
	// reqMap) {
	// Result result = new Result();
	// //构造服务实例
	// ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
	// //服务初始化
	// ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)
	// SecurityUtils.getSubject().getPrincipal();
	// service.initilize(user.getBureau());
	// result.setData(service.importCmdTrainStnByExcel(MapUtils.getString(reqMap,
	// "trainNbr")));
	// return result;
	// }

	/**
	 * 从Excel文件导入列车时刻信息(新-20150610)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/importCmdTrainStnByExcel", method = RequestMethod.POST)
	public Result importCmdTrainStnByExcel(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String trainNbr = request.getParameter("trainNbr");
			String startStn = request.getParameter("startStn");
			String endStn = request.getParameter("endStn");
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			;
			String xlsName;
			String[] name;
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				// 上传文件
				MultipartFile mf = entity.getValue();
				xlsName = mf.getOriginalFilename();
				if (StringUtils.isNotEmpty(xlsName)) {
					name = xlsName.split("\\.");
					if (name.length == 2) {
						if (StringUtils.equals(name[1], "xls")
								|| StringUtils.equals(name[1], "xlsx")) {
							List<CmdTrainStn> trainStnList = CmdAdapterServiceImpl
									.importCmdTrainStnByExcel(
											mf.getInputStream(), trainNbr,
											startStn, endStn);
							int index = 0;
							for (CmdTrainStn trainStn : trainStnList) {
								if (commonService.getNodeNameMapping()
										.containsKey(trainStn.getNodeName())) {
									trainStn.setStnBureau(commonService
											.getNodeNameMapping()
											.get(trainStn.getNodeName())
											.getBureauShortName());
								}

								if (!trainStn.getArrTime().equals(
										trainStn.getDptTime())) {
									trainStn.setJobs(ConstantUtil.KY_STATION_FLAG);
								} 
								if (index == 0) {
									trainStn.setStnType("1");
									trainStn.setPsgFlg(1);
									trainStn.setJobs(ConstantUtil.START_STATION_FLAG);
								}
								if (index == trainStnList.size() - 1) {
									trainStn.setStnType("2");
									trainStn.setPsgFlg(1);
									trainStn.setJobs(ConstantUtil.END_STATION_FLAG);
								}
								index++;
							}
							result.setData(trainStnList);
						} else {
							result.setCode("401");
							result.setMessage("上传失败,不是有效的EXCEL文件");
						}
					} else {
						result.setCode("401");
						result.setMessage("上传失败,文件名不正确");
					}
				} else {
					result.setCode("401");
					result.setMessage("上传失败,文件名不能为空");
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			result.setCode("401");
			result.setMessage("上传失败");
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
	@RequestMapping(value = "/runPlanLkSendMsg", method = RequestMethod.GET)
	public ModelAndView runPlanLkSendMsg(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String trainNbrs = request.getParameter("trainNbrs");
		if (null != trainNbrs) {
			if (trainNbrs.indexOf(",") > -1) {
				String[] str = trainNbrs.split(",");
				for (int i = 0; i < str.length; i++) {
					sb.append(i + 1).append(".").append(str[i]).append("\n");
				}
			} else {
				sb.append("1.").append("trainNbrs");
			}
		}
		return new ModelAndView("runPlanLk/runplanlk_sendmsg")
				.addObject("cmdTrainIds", request.getParameter("cmdTrainIds"))
				.addObject("trainNbrs", sb)
				.addObject("type", request.getParameter("type"));
	}

	/**
	 * 图定-发消息.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRunPlanLkSendMsg", method = RequestMethod.POST)
	public Result saveRunPlanLkSendMsg(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		// System.out.println(reqMap);
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
				typeCode = Constants.MSG_ADD_LK_PLAN;
			} else if (StringUtils.equals(operType, "1")) {
				// 修改
				sb.append("修改图定开行计划");
				typeCode = Constants.MSG_UPD_LK_PLAN;
			}
			// get 交路信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("idList", SqlUtil.strArrayToList(crossIds.split(",")));
			List<PlanTrain> ptList = runPlanLkService.getPlanTrainByMap(params);
			if (!ptList.isEmpty()) {
				sb.append("\n").append("交路：")
						.append(runPlanLkService.dealWithCross(ptList));
				remark = remark.replaceAll(user.getBureau(),
						"<span style=\"color: red\">" + user.getBureau()
								+ "</span>");
				remark = remark.replaceAll(
						user.getBureauShortName(),
						"<span style=\"color: red\">"
								+ user.getBureauShortName() + "</span>");
				sb.append("\n").append("备注：").append(remark);
				Integer count = messageService.dealWithMsg(
						user,
						typeCode,
						sb.toString(),
						runPlanLkService.dealWithRelevantBureau1(ptList,
								user.getBureau()), type);
				if (count == 0) {
					result.setCode(StaticCodeType.MSG_ERROR.getCode());
					result.setMessage(StaticCodeType.MSG_ERROR.getDescription());
				}
			}
		}
		return result;
	}

	/**
	 * 临客加开，发消息.
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/runPlanLkAddSendMsg", method = RequestMethod.GET)
	public ModelAndView runPlanLkAddSendMsg(HttpServletRequest request) {
		return new ModelAndView("runPlanLk/runplanlk_add_sendmsg")
				.addObject("cmdTrainIds", request.getParameter("cmdTrainIds"))
				.addObject("trainNbrs", request.getParameter("trainNbrs"))
				.addObject("type", request.getParameter("type"))
				.addObject("passBureaus", request.getParameter("passBureaus"));
	}

	/**
	 * 临客加开，发消息.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRunPlanLkAddSendMsg", method = RequestMethod.POST)
	public Result saveRunPlanLkAddSendMsg(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		// System.out.println(reqMap);
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String crossIds = MapUtils.getString(reqMap, "crossIds");
		String trainNbrs = MapUtils.getString(reqMap, "trainNbrs");
		String passBureaus = MapUtils.getString(reqMap, "passBureaus");
		String remark = MapUtils.getString(reqMap, "remark");
		String type = MapUtils.getString(reqMap, "type");

		if (null != type && null != trainNbrs && null != crossIds) {
			// 消息内容：广州局担当临客L201，请补充时刻
			StringBuilder sb = new StringBuilder();
			sb.append(user.getBureauFullName()).append("担当临客")
					.append(trainNbrs).append("(").append(passBureaus)
					.append(")").append("，请补充时刻");
			remark = remark.replaceAll(user.getBureau(),
					"<span style=\"color: red\">" + user.getBureau()
							+ "</span>");
			remark = remark.replaceAll(user.getBureauShortName(),
					"<span style=\"color: red\">" + user.getBureauShortName()
							+ "</span>");
			sb.append("\n").append("备注：").append(remark);

			Integer count = messageService.dealWithMsg(user,
					Constants.MSG_BC_LK_STN, sb.toString(), SqlUtil
							.strSqlIn1(runPlanLkService
									.dealWithRelevantBureau2(passBureaus)),
					type);
			if (count == 0) {
				result.setCode(StaticCodeType.MSG_ERROR.getCode());
				result.setMessage(StaticCodeType.MSG_ERROR.getDescription());
			}
		}
		return result;
	}
}
