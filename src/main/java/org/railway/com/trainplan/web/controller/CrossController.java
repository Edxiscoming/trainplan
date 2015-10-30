package org.railway.com.trainplan.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.directwebremoting.json.types.JsonObject;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.constants.TPResponseCode;
import org.railway.com.trainplan.common.utils.CheckUtil;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StattionUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfo;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.BaseCrossTrainSubInfo;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.CrossDictInfo;
import org.railway.com.trainplan.entity.CrossDictStnInfo;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.entity.OriginalCross;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.SubCrossInfo;
import org.railway.com.trainplan.entity.TrainActivityPeriod;
import org.railway.com.trainplan.entity.TrainLineInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfo;
import org.railway.com.trainplan.entity.TrainLineSubInfoTime;
import org.railway.com.trainplan.entity.UnitCrossTrainInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainSubInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainSubInfoTime;
import org.railway.com.trainplan.exceptions.ParamValidationException;
import org.railway.com.trainplan.service.CrossDictService;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.OriginalCrossService;
import org.railway.com.trainplan.service.RemoteService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.CrossRelationDto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/cross")
public class CrossController {
	private static Log logger = LogFactory.getLog(CrossController.class
			.getName());

	@RequestMapping(method = RequestMethod.GET)
	public String content() {
		return "cross/cross_manage";
	}
	@RequestMapping(value = "/passengerServiceExcel",method = RequestMethod.GET)//导客运处 对数表
	public String passengerServiceExcel() {
		return "cross/cross_manage_passengerServiceExcel";
	}
	
	@RequestMapping(value = "/unit", method = RequestMethod.GET)
	public String unit() {
		return "cross/cross_unit_manage";
	}
	
	@RequestMapping(value = "/newunit", method = RequestMethod.GET)
	public String newunit() {
		return "logTable/crossInfo";
	}
	
	@RequestMapping(value = "/original_cross_manage", method = RequestMethod.GET)
	public String originalCrossManage() {
		return "cross/original_cross_manage";
	}
	
	@RequestMapping(value = "/original_cross_import", method = RequestMethod.GET)
	public String originalCrossImport() {
		return "cross/original_cross_import";
	}

	@RequestMapping(value = "/plan", method = RequestMethod.GET)
	public ModelAndView plan(@RequestParam(defaultValue = "") String type,
			ModelAndView modelAndView) {
		if ("jykx".equals(type)) {// 查询既有开行信息
			modelAndView.setViewName("highLine/jykx_cross_plan");
		}
		if ("examine".equals(type)) {// 查询既有开行信息
			modelAndView.setViewName("highLine/cross_plan");
		}
		// modelAndView.setViewName("highLine/cross_plan");
		modelAndView.addObject("type", type);
		return modelAndView;
	}

	@RequestMapping(value = "/crossCanvas", method = RequestMethod.GET)
	public String crossCanvas() {
		return "cross/canvas_event_getvalue";
	}

	@Autowired
	private CrossService crossService;
	
	@Autowired
	private OriginalCrossService originalCrossService;

	@Autowired
	private RemoteService remoteService;

	@Autowired
	private TrainInfoService trainInfoService;

	@Autowired
	private CrossDictService crossDictService;

	@ResponseBody
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public Result getFullStationTrains(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String chartId = request.getParameter("chartId");
			String chartName = request.getParameter("chartName");
			String startDay = request.getParameter("startDay");
			String addFlag = request.getParameter("addFlag");
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
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
							crossService.actionExcel(mf.getInputStream(),
									chartId, startDay, chartName, addFlag);
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
	 * 修改对数表信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editBaseCorssInfo", method = RequestMethod.POST)
	public Result editBaseCrossInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		JSONObject object = JSONObject.fromObject(reqMap.get("result"));

		try {
			logger.info("editBaseCorssInfo~~~~reqMap==" + reqMap);
			String baseCrossId = StringUtil.objToStr(object.get("crossId"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("baseCrossId", baseCrossId);
			// 通过baseCrossId查询crossInfo对象
			CrossInfo crossInfo = crossService
					.getCrossInfoForCrossid(baseCrossId);
			crossInfo.setHighlineRule(StringUtil.objToStr(
					object.get("highlineRule")).equals("null") ? ""
					: StringUtil.objToStr(object.get("highlineRule")));
			// crossInfo.setCommonlineRule(StringUtil.objToStr(object.get("commonlineRule")));
			crossInfo.setCommonlineRule(StringUtil.objToStr(
					object.get("commonlineRule")).equals("null") ? ""
					: StringUtil.objToStr(object.get("commonlineRule")));
			crossInfo.setPairNbr(StringUtil.objToStr(object.get("pairNbr")));
			crossInfo.setGroupTotalNbr(Integer.valueOf(StringUtil
					.objToStr(object.get("groupTotalNbr"))));

			if (object.get("cutOld").equals("0")
					|| object.get("cutOld").equals("1")) {
				crossInfo.setCutOld(Integer.valueOf(StringUtil.objToStr(object
						.get("cutOld"))));
			} else if (object.get("cutOld") == "false") {
				crossInfo.setCutOld(0);
			} else {
				crossInfo.setCutOld(1);
			}

			crossInfo.setCrossSection(StringUtil.objToStr(object
					.get("crossSection")));

			crossInfo.setCrossStartDate(StringUtil.objToStr(object
					.get("crossStartDate")));
			crossInfo.setCrossEndDate(StringUtil.objToStr(object
					.get("crossEndDate")));
			crossInfo.setAppointWeek(StringUtil.objToStr(
					object.get("appointWeek")).equals("null") ? "" : StringUtil
					.objToStr(object.get("appointWeek")));
			crossInfo.setAppointDay(StringUtil.objToStr(
					object.get("appointDay")).equals("null") ? "" : StringUtil
					.objToStr(object.get("appointDay")));
			crossInfo.setTokenVehDept(StringUtil.objToStr(
					object.get("tokenVehDept")).equals("null") ? ""
					: StringUtil.objToStr(object.get("tokenVehDept")));
			crossInfo.setTokenVehDepot(StringUtil.objToStr(
					object.get("tokenVehDepot")).equals("null") ? ""
					: StringUtil.objToStr(object.get("tokenVehDepot")));
			crossInfo.setTokenPsgDept(StringUtil.objToStr(
					object.get("tokenPsgDept")).equals("null") ? ""
					: StringUtil.objToStr(object.get("tokenPsgDept")));
			crossInfo.setLocoType(StringUtil.objToStr(object.get("locoType")));
			crossInfo.setCrhType(StringUtil.objToStr(object.get("crhType"))
					.equals("null") ? "" : StringUtil.objToStr(object
					.get("crhType")));
			crossInfo.setElecSupply(Integer.valueOf(StringUtil.objToStr(object
					.get("elecSupply"))));
			crossInfo.setDejCollect(Integer.valueOf(StringUtil.objToStr(object
					.get("dejCollect"))));
			crossInfo.setAirCondition(Integer.valueOf(StringUtil
					.objToStr(object.get("airCondition"))));
			crossInfo.setNote(StringUtil.objToStr(object.get("note")).equals(
					"null") ? "" : StringUtil.objToStr(object.get("note")));
			crossInfo.setThroughline(StringUtil.objToStr(
					object.get("throughline")).equals("null") ? "" : StringUtil
					.objToStr(object.get("throughline")));
			crossInfo.setCreateUnitTime("");
			crossInfo.setBaseCrossId(baseCrossId);

			if (crossInfo != null) {
				// 通过baseCrossId查询unitCross信息
				List<CrossInfo> unitCrossInfoList = crossService
						.getUnitCrossInfosForCrossId(baseCrossId);
				// 删除unit_cross_train中的数据
				if (unitCrossInfoList != null && unitCrossInfoList.size() > 0) {

					List<String> unitCrossIdsList = new ArrayList<String>();
					for (CrossInfo cross : unitCrossInfoList) {
						unitCrossIdsList.add(cross.getBaseCrossId());
					}
					// 删除unit_cross_train中的数据
					crossService
							.deleteUnitCrossInfoTrainForCorssIds(unitCrossIdsList);
					// 删除unit_cross中的数据
					crossService
							.deleteUnitCrossInfoForCorssIds(unitCrossIdsList);

				}

				JSONArray fromObject = JSONArray.fromObject(reqMap.get("map")); // toArray(reqMap.get("map"));
				int length = fromObject.size();
				String ids[] = new String[length];
				int values[] = new int[length];
				for (int i = 0; i < length; i++) {
					JSONObject arr = JSONObject.fromObject(fromObject.get(i));
					ids[i] = arr.get("trainId").toString();
					values[i] = Integer.parseInt(arr.get("dayLay").toString());
				}

				// 更改crossinfo,crossInfo要根据页面传入重新设置，并将createUnitTime设为空
				crossService.updateBaseCross(crossInfo);
				crossService.updateBaseCrossDayGap(ids, values);
				crossService.deleteUnitCrossByBaseCrossId(map);
				crossService.deleteUnitCrossTrainByBaseCrossId(map);
				crossService.updateBaseCrossByBaseCrossId(map);
			}

		} catch (Exception e) {
			logger.error("editBaseCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 删除unit_cross表和表unit_cross_train中数据
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteUnitCorssInfo", method = RequestMethod.POST)
	public Result deleteUnitCorssInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String unitCrossIds = StringUtil.objToStr(reqMap
					.get("unitCrossIds"));
			if (unitCrossIds != null) {
				String[] crossIdsArray = unitCrossIds.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				for (int i = 0; i < crossIdsArray.length; i++) {
					crossIdsList.add(crossIdsArray[i]);
				}

				// 更新BASE_CROSS中的审核状态
				int countBaseCreateTimeToNull = crossService
						.updateCrossUnitCreateTimeToNullByUnitCrossId(crossIdsList);
				int countBaseCheckTimeToNull = crossService
						.updateCrossUnitCheckTimeToNullByUnitCrossId(crossIdsList);

				// 先删除unit_cross_train表中数据
				int countTrain = crossService
						.deleteUnitCrossInfoTrainForCorssIds(crossIdsList);
				// 删除unit_cross表中数据
				int count = crossService
						.deleteUnitCrossInfoForCorssIds(crossIdsList);

			}
		} catch (Exception e) {
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/fixUnitCorssInfo", method = RequestMethod.POST)
	public Result fixUnitCorssInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			JSONArray fromObject = JSONArray.fromObject(reqMap
					.get("unitCrossIds")); // toArray(reqMap.get("map"));

			int length = fromObject.size();
			Map<String, Object> map1 = new HashMap<String, Object>();
			List<Map<String, Object>> map2 = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> map3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> zbMap = new HashMap<String, Object>();

			/** 肯定会问为什么要写这么复杂,那是因为没有找到好的排序方法,只能麻烦点一步一步来了,你行你来 **/
			// 1 根据ID将车序/组建间隔插入.
			for (int i = 0; i < length; i++) {
				map1.clear();
				// map2.clear();
				JSONObject arr = JSONObject.fromObject(fromObject.get(i));
				String id = arr.getString("unitCrossTrainId");
				int trainsort = arr.getInt("trainSort");
				int groupGap = arr.getInt("groupGap");
				map1.put("trainsort", trainsort);
				map1.put("unitCrossIds", id);
				map1.put("groupGap", groupGap);

				// map2 = crossService.getUnitCrossTrainByMap(map1);
				//
				// if(!map2.isEmpty()){
				// if(StringUtils.isEmpty(MapUtils.getString(map2.get(0),
				// "TRAIN_SORT_BASE"))){
				// map1.put("train_sort_base", map2.get(0).get("TRAIN_SORT"));
				// crossService.updateUnitCrossTrainByTrainId(map1);
				// }
				// }

				crossService.updateUnitCrossInfoTrainForCorssIds(map1);

			}
			// 2 依据任何一条train_id查询出主表ID
			map2 = crossService.getUnitCrossTrainByMap(map1);
			// 3 根据主表ID,查询出排好序的车次信息.
			map3 = crossService.getUnitCrossTrainByMap(map2.get(0));
			// 4 更新出发/到达时间.
			for (int i = 0; i < map3.size(); i++) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 第一条数据,不需要修改时间;
				// 之后的数据里的时间,都是根据当前车的上一辆车的到达时间而定,所以需要再查一次.
				// 第2车组的第一辆车要对应第一组的第一辆车,以此类推
				boolean b = false;
				if (i != 0) {
					String str = MapUtils.getString(map3.get(i - 1),
							"UNIT_CROSS_TRAIN_ID");
					// 车序不是1的,才需要去匹配, 车序=1的直接获取上一条数据即可
					if (MapUtils.getInteger(map3.get(i), "TRAIN_SORT") != 1) {
						for (int j = 0; j < map3.size(); j++) {
							// 当前车辆的车序,找到相同车序的数据,然后再找出组序相同的
							if ((MapUtils.getInteger(map3.get(i), "TRAIN_SORT") - 1) == MapUtils
									.getInteger(map3.get(j), "TRAIN_SORT")) {
								// 组序相同
								if (StringUtils.equals(MapUtils.getString(
										map3.get(i), "GROUP_SERIAL_NBR"),
										MapUtils.getString(map3.get(j),
												"GROUP_SERIAL_NBR"))) {
									str = MapUtils.getString(map3.get(j),
											"UNIT_CROSS_TRAIN_ID");
									b = true;
									break;
								}
							}
						}
					}
					// paramMap.put("unitCrossIds",
					// map3.get(i-1).get("UNIT_CROSS_TRAIN_ID"));
					paramMap.put("unitCrossIds", str);
					// 上一条信息
					// paramMap.put("unitCrossIdUp", str);
					map2 = crossService.getUnitCrossTrainByMap(paramMap);
					// b=true,证明不是当前组的第一辆车,所以他的出发时间就需要当前车组里的前一辆的到达时间
					paramMap.put("end_date", map2.get(0).get("END_DATE"));
					if (b) {
						paramMap.put("run_date", map2.get(0).get("END_DATE"));
						paramMap.put("day_gap", map3.get(i).get("DAY_GAP"));
					} else {
						paramMap.put("run_date", map2.get(0).get("RUN_DATE"));
						paramMap.put("day_gap", null);
					}
				}
				paramMap.put("unitCrossIds",
						map3.get(i).get("UNIT_CROSS_TRAIN_ID"));
				paramMap.put("trainsort", map3.get(i).get("TRAIN_SORT"));
				paramMap.put("groupGap", map3.get(i).get("GROUP_GAP"));
				crossService.updateUnitCrossInfoTrainForCorssIds(paramMap);
				// 5 更新主表的开始/结束.
				if (i == (length - 1)) {
					// 修改主表
					zbMap.put("unit_cross_id", map3.get(i).get("UNIT_CROSS_ID"));
					crossService.updateUnitCrossByMap(zbMap);
					crossService.updateUnitCorssCheckTimeToNull(map3.get(i)
							.get("UNIT_CROSS_ID").toString());
				}

			}

			// List<UnitCrossDtoForSort> unitCrosstemp = new
			// ArrayList<UnitCrossDtoForSort>();
			// UnitCrossDtoForSort u = null;
			// for (int i = 0; i < length; i++) {
			// u = new UnitCrossDtoForSort();
			// JSONObject arr = JSONObject.fromObject(fromObject.get(i));
			//
			// String id = arr.get("unitCrossTrainId").toString();
			// int trainsort =
			// Integer.parseInt(arr.get("trainSort").toString());
			// int groupGap = Integer.parseInt(arr.get("groupGap").toString());
			// u.setGroupGap(groupGap);
			// u.setTrainSort(trainsort);
			// u.setUnitCrossTrainId(id);
			// unitCrosstemp.add(u);
			// }
			// //冒泡
			// UnitCrossDtoForSort sorttemp = null;
			// for (int i = 0; i < unitCrosstemp.size(); i++) {
			// UnitCrossDtoForSort uc1 = unitCrosstemp.get(i);
			// for (int j = i; j < unitCrosstemp.size(); j++) {
			// UnitCrossDtoForSort uc2 = unitCrosstemp.get(j);
			// if(uc1.getTrainSort()>uc2.getTrainSort()){
			// sorttemp = uc1;
			// unitCrosstemp.set(i, uc2);
			// unitCrosstemp.set(j, sorttemp);
			// }
			// }
			// }
			//
			// for (int i = 0; i < unitCrosstemp.size(); i++) {
			// // JSONObject arr = JSONObject.fromObject(fromObject.get(i));
			// UnitCrossDtoForSort udf = unitCrosstemp.get(i);
			//
			// String id = udf.getUnitCrossTrainId();
			// int trainsort = udf.getTrainSort();
			// int groupGap = udf.getGroupGap();
			//
			// map1.put("trainsort", trainsort);
			// map1.put("unitCrossIds", id);
			// map1.put("groupGap", groupGap);
			// map1.put("end_date", map2.get("END_DATE"));
			//
			// int updI =
			// crossService.updateUnitCrossInfoTrainForCorssIds(map1);
			//
			// if(updI > 0){
			// map2.clear();
			// // 查询最新的上一条数据
			// map2 = crossService.getUnitCrossTrainByMap(map1);
			// if( i == 0){
			// map3.put("run_date", map2.get("RUN_DATE"));
			// map3.put("unit_cross_id", map2.get("UNIT_CROSS_ID"));
			// }else if( i == (length-1)){
			// map3.put("end_date", map2.get("END_DATE"));
			// // 修改主表
			// crossService.updateUnitCrossByMap(map3);
			// }
			// }
			//
			// }

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("deleteUnitCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 删除base_cross表和表base_cross_train中数据
	 * 
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteCorssInfo", method = RequestMethod.POST)
	public Result deleteCorssInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			if (crossIds != null) {
				String[] crossIdsArray = crossIds.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				for (int i = 0; i < crossIdsArray.length; i++) {
					crossIdsList.add(crossIdsArray[i]);

					/** 下面代码复制的保存,由于对业务不熟悉,所以只能将删除unit_cross代码全拷过来,if里的删除无效 **/
					// 通过baseCrossId查询unitCross信息
					List<CrossInfo> unitCrossInfoList = crossService
							.getUnitCrossInfosForCrossId(crossIdsArray[i]);
					// 删除unit_cross_train中的数据
					if (unitCrossInfoList != null
							&& unitCrossInfoList.size() > 0) {
						List<String> unitCrossIdsList = new ArrayList<String>();
						for (CrossInfo cross : unitCrossInfoList) {
							unitCrossIdsList.add(cross.getUnitCrossId());
						}
						// 删除unit_cross_train中的数据
						crossService
								.deleteUnitCrossInfoTrainForCorssIds(unitCrossIdsList);
						// 删除unit_cross中的数据
						crossService
								.deleteUnitCrossInfoForCorssIds(unitCrossIdsList);
					}

					map.put("baseCrossId", crossIdsArray[i]);
				}
				// // 实际上,最后执行的删除
				// crossService.deleteUnitCrossByBaseCrossId(map);
				// crossService.deleteUnitCrossTrainByBaseCrossId(map);
				// 先删除base_cross_train表中数据
				int countTrain = crossService
						.deleteCrossInfoTrainForCorssIds(crossIdsList);
				// 删除base_cross表中数据
				int count = crossService
						.deleteCrossInfoForCorssIds(crossIdsList);

			}
		} catch (Exception e) {
			logger.error("deleteCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 对数表审核.
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCorssInfo", method = RequestMethod.POST)
	public Result checkCorssInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();

		List<String> returnLM = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();

		boolean b1 = false;
		StringBuilder sb1 = new StringBuilder();
		sb1.append("以下交路中存在车次与基本图中的车次不一致， 或基本图中没有，请检查基本图：");
		sb1.append("\n");

		boolean b2 = false;
		StringBuilder sb2 = new StringBuilder();
		sb2.append("以下交路中存在前车的终到站与后车的始发站不一致：");
		sb2.append("\n");

		boolean b3 = false;
		StringBuilder sb3 = new StringBuilder();
		sb3.append("以下交路首车的始发站与末车的终到站不一致：");
		sb3.append("\n");
		
		boolean b4 = false;
		StringBuilder sb4 = new StringBuilder();
		sb4.append("以下交路的组数不正确，不是正整数：");
		sb4.append("\n");
		
		boolean b5 = false;
		StringBuilder sb5 = new StringBuilder();
		sb5.append("以下交路的星期规律不正确：");
		sb5.append("\n");

		int errCount = 0;
		int updCount = 0;
		try {
			String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			if (crossIds != null) {
				String[] crossIdsArray = crossIds.split(",");
				List<String> crossIdsList = new ArrayList<String>();
				Map<String, String> paramMap = new HashMap<String, String>();
				for (int i = 0; i < crossIdsArray.length; i++) {
					// 审核前的校验.
					paramMap.put("base_cross_id", crossIdsArray[i]);
					List<Map<String, String>> lm = crossService
							.getBaseCrossTrainByMap(paramMap);
					if (!lm.isEmpty()) {
						// 校验
						result = checkCross(lm);
						if (StringUtils.equals(result.getCode(), "0")) {
							crossIdsList.add(crossIdsArray[i]);
							returnLM.add(crossIdsArray[i]);
						} else if (StringUtils.equals(result.getCode(), "1")) {
							sb1.append(result.getMessage());
							errCount++;
							b1 = true;
						} else if (StringUtils.equals(result.getCode(), "2")) {
							sb2.append(result.getMessage());
							errCount++;
							b2 = true;
						} else if (StringUtils.equals(result.getCode(), "3")) {
							sb3.append(result.getMessage());
							errCount++;
							b3 = true;
						} else if (StringUtils.equals(result.getCode(), "4")) {
							sb4.append(result.getMessage());
							errCount++;
							b4 = true;
						} else if (StringUtils.equals(result.getCode(), "5")) {
							sb5.append(result.getMessage());
							errCount++;
							b5 = true;
						}
					}
				}
				if (!crossIdsList.isEmpty()) {
					updCount = crossService.updateCorssCheckTime(crossIdsList);
				}
			}
		} catch (Exception e) {
			logger.error("checkCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		if (errCount > 0) {
			sb.append("审核失败的交路共");
			sb.append(errCount);
			sb.append("条，原因如下：");
			sb.append("\n");
			sb.append("\n");

			// 愚昧的办法，但是想不到其他办法.
			if (b1) {
				sb.append("一、").append(sb1).append("\n");
				if (b2) {
					sb.append("二、").append(sb2).append("\n");
					if (b3) {
						sb.append("三、").append(sb3).append("\n");
						if (b4) {
							sb.append("四、").append(sb4).append("\n");
							if (b5) {
								sb.append("五、").append(sb5).append("\n");
							}
						} else if (b5) {
							sb.append("四、").append(sb5).append("\n");
						}
					} else if (b4) {
						sb.append("三、").append(sb4).append("\n");
						if (b5) {
							sb.append("四、").append(sb5).append("\n");
						}
					} else if (b5) {
						sb.append("三、").append(sb5).append("\n");
					}
				} else if (b3) {
					sb.append("二、").append(sb3).append("\n");
					if (b4) {
						sb.append("三、").append(sb4).append("\n");
						if (b5) {
							sb.append("四、").append(sb5).append("\n");
						}
					} else if (b5) {
						sb.append("三、").append(sb5).append("\n");
					}
				} else if (b4) {
					sb.append("二、").append(sb4).append("\n");
					if (b5) {
						sb.append("三、").append(sb5).append("\n");
					}
				} else if (b5) {
					sb.append("二、").append(sb5).append("\n");
				}
			} else if (b2) {
				sb.append("一、").append(sb2).append("\n");
				if (b3) {
					sb.append("二、").append(sb3).append("\n");
					if (b4) {
						sb.append("三、").append(sb4).append("\n");
						if (b5) {
							sb.append("四、").append(sb5).append("\n");
						}
					} else if (b5) {
						sb.append("三、").append(sb5).append("\n");
					}
				} else if (b4) {
					sb.append("二、").append(sb4).append("\n");
					if (b5) {
						sb.append("三、").append(sb5).append("\n");
					}
				} else if (b5) {
					sb.append("二、").append(sb5).append("\n");
				}
			} else if (b3) {
				sb.append("一、").append(sb3).append("\n");
				if (b4) {
					sb.append("二、").append(sb4).append("\n");
					if (b5) {
						sb.append("三、").append(sb5).append("\n");
					}
				} else if (b5) {
					sb.append("二、").append(sb5).append("\n");
				}
			} else if (b4) {
				sb.append("一、").append(sb4).append("\n");
				if (b5) {
					sb.append("二、").append(sb5).append("\n");
				}
			} else if (b5) {
				sb.append("一、").append(sb5).append("\n");
			}

			// sb.append(sb1).append("\n").append(sb2);
			StringBuilder sbUpd = new StringBuilder();
			if (updCount > 0) {
				// 虽然失败，但是还是有成功的
				sbUpd.append("审核成功的交路共");
				sbUpd.append(updCount);
				sbUpd.append("条。");
				sbUpd.append("\n");
				sbUpd.append("\n");
				result.setMessage(sbUpd.append(sb).toString());
			} else {
				result.setMessage(sb.toString());
			}
			result.setCode(StaticCodeType.SYSTEM_DATA_BUFEN.getCode());
			result.setData(returnLM);
		}
		return result;
	}

	/**
	 * 审核交路信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkUnitCorssInfo", method = RequestMethod.POST)
	public Result checkUnitCorssInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String unitCrossIds = StringUtil.objToStr(reqMap
					.get("unitCrossIds"));
			if (unitCrossIds != null) {
				String[] unitCrossIdsArray = unitCrossIds.split(",");
				List<String> unitCrossIdsList = new ArrayList<String>();
				for (int i = 0; i < unitCrossIdsArray.length; i++) {
					unitCrossIdsList.add(unitCrossIdsArray[i]);
				}
				int count = crossService
						.updateUnitCorssCheckTime(unitCrossIdsList);
				logger.debug("update--count==" + count);
			}
		} catch (Exception e) {
			logger.error("checkCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 提供画交路单元图形的数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideUnitCrossChartData", method = RequestMethod.GET)
	public ModelAndView provideUnitCrossChartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = new ModelAndView("cross/unit_cross_canvas");
		ObjectMapper objectMapper = new ObjectMapper();
		String unitCrossId = StringUtil.objToStr(request
				.getParameter("unitCrossId"));
		PlanLineGrid grid = null;
		String crossStartDate = "";
		String crossEndDate = "";

		logger.info("unitCrossId---unit==" + unitCrossId);
		// 用于纵坐标的列车list
		List<Station> stationDictList = new LinkedList<Station>();
		// 标识纵坐标数据是否来源与经由字典
		boolean isDictDate = false;
		// 查看经由字典是否有数据

		// 使用方案名和交路名来确定
		List<CrossDictStnInfo> listDitStnInfo = crossDictService
				.getCrossDictStnForUnitCorssId(unitCrossId);

		if (listDitStnInfo != null && listDitStnInfo.size() > 0) {
			isDictDate = true;
			for (CrossDictStnInfo dictStnInfo : listDitStnInfo) {
				Station station = new Station();
				station.setStnName(dictStnInfo.getStnName());
				String stationType = dictStnInfo.getStnType();
				// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
				if ("1".equals(stationType)) {
					stationType = "0";
				} else if ("4".equals(stationType)) {
					stationType = "BT";
				} else if ("3".equals(stationType)) {
					stationType = "TZ";
				} else if ("2".equals(stationType)) {
					stationType = "FJK";
				}
				station.setStationType(stationType);
				stationDictList.add(station);
			}
		}
		// 众坐标的站列表
		List<String> stationList = new ArrayList<String>();
		// 通过unitCrossId获取unitCross列表信息
		List<UnitCrossTrainInfo> list = crossService
				.getUnitCrossTrainInfoForUnitCrossid(unitCrossId);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		if (list != null && list.size() > 0) {
			int size = list.size();
			// i是用来循环交路单元的组
			for (int i = 0; i < size; i++) {
				Map<String, Object> crossMap = new HashMap<String, Object>();
				UnitCrossTrainInfo unitCrossInfo = list.get(i);
				String groupSerialNbr = unitCrossInfo.getGroupSerialNbr();
				// 列车信息列表
				List<UnitCrossTrainSubInfo> unitStationsList = unitCrossInfo
						.getTrainInfoList();
				// 列车信息列表
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();

				if (unitStationsList != null && unitStationsList.size() > 0) {
					int sizeStation = unitStationsList.size();
					for (int j = 0; j < unitStationsList.size(); j++) {

						UnitCrossTrainSubInfo subInfo = unitStationsList.get(j);
						TrainInfoDto dto = new TrainInfoDto();
						dto.setTrainName(subInfo.getTrainNbr());

						dto.setStartStn(subInfo.getStartStn() + "["
								+ subInfo.getStartBureau() + "]");
						dto.setEndStn(subInfo.getEndStn() + "["
								+ subInfo.getEndBureau() + "]");
						dto.setStartDate(subInfo.getRunDate());
						dto.setEndDate(subInfo.getEndDate());
						dto.setGroupSerialNbr(groupSerialNbr);
						// 列车经由时刻等信息
						List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
						List<UnitCrossTrainSubInfoTime> stationTimeList = subInfo
								.getStationTimeList();
						if (stationTimeList != null
								&& stationTimeList.size() > 0) {
							for (int k = 0; k < stationTimeList.size(); k++) {
								UnitCrossTrainSubInfoTime subInfoTime = stationTimeList
										.get(k);
								if (!"BT".equals(subInfoTime.getStationType())) {
									subInfoTime.setStnName(subInfoTime
											.getStnName()
											+ "["
											+ subInfoTime.getBureauShortName()
											+ "]");
									PlanLineSTNDto stnDto = new PlanLineSTNDto();
									BeanUtils.copyProperties(stnDto,
											subInfoTime);
									trainStns.add(stnDto);
								}
							}
							dto.setTrainStns(trainStns);
						}

						trains.add(dto);

						if (i == 0 && j == 0) {
							// 取第一组交路单元的第一个列车的始发日期作为横坐标的开始
							crossStartDate = subInfo.getRunDate();
							if (crossStartDate != null) {
								crossStartDate = crossStartDate
										.substring(0, 10);
							}
						} else if (i == size - 1 && j == sizeStation - 1) {
							// 取最后一组交路单元的最后一个列车的终到日期作为横坐标的结束
							List<Map<String, String>> lm = crossService
									.getUnitCrossTrainDateByMap(unitCrossId);
							if (null != lm && !lm.isEmpty()) {
								Map<String, String> m = lm.get(0);
								crossEndDate = lm.get(0).get("END_DATE");
								if (null != crossEndDate
										&& !"".equals(crossEndDate)) {
									StringBuilder sb = new StringBuilder();
									sb.append(crossEndDate.substring(0, 4))
											.append("-")
											.append(crossEndDate
													.substring(4, 6))
											.append("-")
											.append(crossEndDate
													.substring(6, 8));
									crossEndDate = sb.toString();
								}
							} else {
								crossEndDate = subInfo.getEndDate();
								crossEndDate = crossEndDate.substring(0, 10);
							}
						}
						if (!isDictDate) {
							// 合并始发站和终到站
							if (!stationList.contains(subInfo.getStartStn())) {
								stationList.add(subInfo.getStartStn());
							}
							if (!stationList.contains(subInfo.getEndStn())) {
								stationList.add(subInfo.getEndStn());
							}
						}

					}
				}

				// 组装接续关系
				List<CrossRelationDto> jxgx = getJxgx(trains);
				crossMap.put("jxgx", jxgx);
				crossMap.put("trains", trains);
				crossMap.put("groupSerialNbr", groupSerialNbr);
				crossMap.put("crossName", "");
				dataList.add(crossMap);
			}
		}
		logger.info("isDictDate==" + isDictDate);
		if (isDictDate) {
			List<PlanLineGridY> listGridY = getPlanLineGridY(stationDictList);
			List<PlanLineGridX> listGridX = getPlanLineGridX(crossStartDate,
					crossEndDate);
			grid = new PlanLineGrid(listGridX, listGridY);
		} else {
			// 组装坐标
			// grid = getPlanLineGrid(stationList,crossStartDate,crossEndDate);

			if (dataList != null && dataList.size() > 0) {
				String startDate = "";
				String endDate = "";

				List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
				Map<String, Object> crossmap = dataList.get(0);
				List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
						.get("trains");
				for (int k = 0; k < trains.size(); k++) {
					// 取交路中的第一辆车的经由站信息
					TrainInfoDto dto = trains.get(k);
					if (k == 0) {
						startDate = DateUtil.parseDateTOyyyymmdd(dto
								.getStartDate());
					}
					if (k == trains.size() - 1) {
						String endDateTemp = dto.getTrainStns()
								.get(dto.getTrainStns().size() - 1)
								.getArrTime();
						endDate = DateUtil.parseDateTOyyyymmdd(endDateTemp);
					}
					LinkedList<Station> listStation1 = new LinkedList<Station>();

					List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
					for (PlanLineSTNDto trainStn : listTrainStn) {
						Station station = new Station();
						station.setStnName(trainStn.getStnName());
						station.setStationType(trainStn.getStationType());
						station.setDptTime(trainStn.getDptTime());
						listStation1.add(station);
					}
					list1.add(listStation1);
				}
				// 生成横纵坐标
				grid = getPlanLineGridForAll(list1, crossStartDate,
						crossEndDate);

			}
		}

		String myJlData = objectMapper.writeValueAsString(dataList);
		// 图形数据
		result.addObject("myJlData", myJlData);
		logger.info("myJlData==" + myJlData);

		String gridStr = objectMapper.writeValueAsString(grid);
		logger.info("gridStr==" + gridStr);
		result.addObject("gridData", gridStr);

		System.out.println(crossEndDate);
		return result;
	}

	/**
	 * 提供画交路图形的数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideCrossChartData", method = RequestMethod.GET)
	public ModelAndView provideCrossChartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = new ModelAndView("cross/cross_canvas");

		String crossId = StringUtil.objToStr(request.getParameter("crossId"));
		String chartId = StringUtil.objToStr(request.getParameter("chartId"));
		String crossName = StringUtil.objToStr(request
				.getParameter("crossName"));
		logger.debug("chartId==" + chartId);

		String drawGraphIdAllIn = "";// 用于调整站序
		String isCurrentBureau = "0";// 用于调整站序,0 不是 1 是
		// 是否本局担当
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossName", crossName);
		List<CrossInfo> crossInfoList = crossService.getCrossInfoByParam(map);
		if (crossInfoList != null && crossInfoList.size() > 0) {
			String tokenVehBureau = crossInfoList.get(0).getTokenVehBureau();
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if (!"".equals(tokenVehBureau)) {
				String string = user.getBureau();
				isCurrentBureau = tokenVehBureau.equals(user.getBureau()
						.toString()) ? "1" : "0";
			}
		}
		// 经由信息，由后面调用接口获取，用户提供画图的坐标
		PlanLineGrid grid = null;
		Map<String, Object> crossMap = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 先查看表draw_graph中是否有该交路的信息

		// 使用方案名和交路名来确定
		List<CrossDictStnInfo> listDicStn = crossDictService
				.getCrossDictStnForChartId(crossName, chartId);
		// 用于纵坐标的列车list
		List<Station> stationList = new LinkedList<Station>();
		// 标识纵坐标的站点信息是否来源于字典
		boolean isStationFromDic = false;
		if (listDicStn != null && listDicStn.size() > 0) {
			drawGraphIdAllIn = listDicStn.get(0).getDrawGrapId();
			isStationFromDic = true;
			for (CrossDictStnInfo dictStnInfo : listDicStn) {
				Station station = new Station();
				station.setStnName(dictStnInfo.getStnName());
				String stationType = dictStnInfo.getStnType();
				// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
				if ("1".equals(stationType)) {
					stationType = "0";
				} else if ("4".equals(stationType)) {
					stationType = "BT";
				} else if ("3".equals(stationType)) {
					stationType = "TZ";
				} else if ("2".equals(stationType)) {
					stationType = "FJK";
				} else {
					stationType = "-1";
				}

				station.setStationType(stationType);
				stationList.add(station);
			}
		}
		if (crossId != null) {
			// 根据crossId获取列车信息和始发站，终到站等信息
			List<BaseCrossTrainInfo> list = crossService
					.getCrossTrainInfoForCrossId(crossId);
			// 只有一条记录
			if (list != null && list.size() > 0) {
				BaseCrossTrainInfo trainInfo = list.get(0);
				// 列车信息
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				// 众坐标的站列表
				List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
				List<BaseCrossTrainSubInfo> trainList = trainInfo
						.getTrainList();
				if (trainList != null && trainList.size() > 0) {

					for (int i = 0; i < trainList.size(); i++) {

						// 列车信息
						BaseCrossTrainSubInfo subInfo = trainList.get(i);
						TrainInfoDto dto = new TrainInfoDto();
						dto.setTrainName(subInfo.getTrainNbr());
						String startBur = "";
						String endBur = "";
						// 列车经由时刻信息
						List<BaseCrossTrainInfoTime> stationTimeList = subInfo
								.getStationList();
						if (stationTimeList != null
								&& stationTimeList.size() > 0) {
							startBur = "["
									+ stationTimeList.get(0)
											.getBureauShortName() + "]";
							endBur = "["
									+ stationTimeList.get(
											stationTimeList.size() - 1)
											.getBureauShortName() + "]";
						}
						dto.setStartStn(subInfo.getStartStn()
								+ "["
								+ subInfo.getStationList().get(0)
										.getBureauShortName() + "]");
						dto.setEndStn(subInfo.getEndStn()
								+ "["
								+ subInfo
										.getStationList()
										.get(subInfo.getStationList().size() - 1)
										.getBureauShortName() + "]");
						dto.setStartDate(subInfo.getStartTime());
						dto.setEndDate(subInfo.getEndTime());
						// 列车经由时刻等信息
						List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
						// 用于纵坐标的列车list
						LinkedList<Station> trainsList = new LinkedList<Station>();
						if (stationTimeList != null
								&& stationTimeList.size() > 0) {
							for (int j = 0; j < stationTimeList.size(); j++) {
								// 纵坐标的一个站
								PlanLineSTNDto stnDto = new PlanLineSTNDto();
								BaseCrossTrainInfoTime trainTime = stationTimeList
										.get(j);
								trainTime.setStnName(trainTime.getStnName()
										+ "[" + trainTime.getBureauShortName()
										+ "]");
								BeanUtils.copyProperties(stnDto, trainTime);

								// if(!"-1".equals(stnDto.getStationType()) &&
								// !"BT".equals(stnDto.getStationType())) {
								Station station = new Station();
								station.setStnName(stnDto.getStnName());
								station.setDptTime(stnDto.getDptTime());
								station.setStationType(stnDto.getStationType());
								// shortList只要是非 不停站都需要加到纵坐标list中
								if (!"BT".equals(stnDto.getStationType())) {
									trainStns.add(stnDto);
								}
								trainsList.add(station);
								// }
							}
							dto.setTrainStns(trainStns);
							list1.add(trainsList);
						}
						trains.add(dto);
					}
				}

				// 组装接续关系
				List<CrossRelationDto> jxgx = getJxgx(trains);
				// guo
				crossMap.put("jxgx", jxgx);
				crossMap.put("trains", trains);
				crossMap.put("crossName", trainInfo.getCrossName());
				dataList.add(crossMap);

				/******** fortest *********/
				/*
				 * if(list1 != null&&list1.size() > 0){ for(LinkedList<Station>
				 * stationLists : list1){
				 * System.err.println("*****************************");
				 * for(Station station :stationLists){
				 * System.err.println(station.getStnName() + "|" +
				 * station.getDptTime() + "|" + station.getStationType());
				 * 
				 * } } }
				 */
				/*******************/
				// isStationFromDic = false;
				if (isStationFromDic) {
					List<PlanLineGridY> listGridY = getPlanLineGridY(stationList);
					List<PlanLineGridX> listGridX = getPlanLineGridX(
							trainInfo.getCrossStartDate(),
							trainInfo.getCrossEndDate());
					grid = new PlanLineGrid(listGridX, listGridY);
				} else {
					// liuhang
					grid = getPlanLineGridForAll(list1,
							trainInfo.getCrossStartDate(),
							trainInfo.getCrossEndDate());

					// 查询有关baseCross的信息
					CrossInfo crossInfo = crossService
							.getCrossInfoForCrossid(crossId);
					ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
							.getSubject().getPrincipal();
					// 保存到数据库
					// 先保存表draw_graph的数据
					CrossDictInfo dicInfo = new CrossDictInfo();
					// String drawGrapId = UUID.randomUUID().toString();
					String drawGrapId = crossName + "_" + chartId;
					drawGraphIdAllIn = drawGrapId;// 修改站序
					// 防止重复生成画图数据
					dicInfo.setDrawGrapId(drawGrapId);
					dicInfo.setBaseChartId(crossInfo.getChartId());
					dicInfo.setBaseChartName(crossInfo.getChartName());
					dicInfo.setCrossName(crossInfo.getCrossName());
					dicInfo.setBaseCrossId(crossId);
					dicInfo.setCheckPeople(user.getName());
					dicInfo.setCheckPeopleOrg(user.getDeptName());
					dicInfo.setCreatePeople(user.getName());
					dicInfo.setCreatePeopleOrg(user.getDeptName());
					// 保存数据
					try {
						// 保存数据
						int countDic = crossDictService
								.addCrossDictInfo(dicInfo);
						logger.debug("countDic===" + countDic);
					} catch (Exception e) {
						// suntao 说明存失败,这里的失败，是违反主键唯一原则
						return getDrawAgain(result, crossName, chartId, crossId);
					}
					List<CrossDictStnInfo> listDictStn = new ArrayList<CrossDictStnInfo>();
					// 获取纵坐标信息
					List<PlanLineGridY> listGridY = grid.getCrossStns();
					int height = 0;
					int heightSimple = 0;
					int stnsort = 0;
					for (int i = 0; i < listGridY.size(); i++) {
						PlanLineGridY gridY = listGridY.get(i);
						String stationType = gridY.getStationType();
						if (!"BT".equals(stationType)) {
							// 不存不停的站
							CrossDictStnInfo crossStnInfo = new CrossDictStnInfo();
							crossStnInfo.setDrawGrapId(drawGrapId);
							crossStnInfo.setDrawGrapStnId(UUID.randomUUID()
									.toString());
							// TODO 所属局简称
							crossStnInfo.setBureau("");
							// 默认每个站的间隔为100
							height = height + 100;
							crossStnInfo
									.setHeightDetail(String.valueOf(height));
							crossStnInfo.setStnName(gridY.getStnName());
							crossStnInfo.setStnSort(stnsort++);

							// 车站类型（1:发到站，2:分界口，3:停站）
							if ("0".equals(stationType)) {
								stationType = "1";
							} else if ("TZ".equals(stationType)) {
								stationType = "3";
							} else if ("FJK".equals(stationType)) {
								stationType = "2";
							}
							crossStnInfo.setStnType(stationType);

							if (i == 0 && "1".equals(stationType)) {
								crossStnInfo.setHeightSimple(String.valueOf(0));
							} else if (i != 0 && "1".equals(stationType)) {
								heightSimple = heightSimple + 100;
								crossStnInfo.setHeightSimple(String
										.valueOf(heightSimple));
							} else {
								crossStnInfo.setHeightSimple(String.valueOf(0));
							}
							listDictStn.add(crossStnInfo);
						}

					}
					// 保存数据
					int countDicStn = crossDictService
							.batchAddCrossDictStnInfo(listDictStn);
					logger.debug("countDicStn==" + countDicStn);
				}

				// fortest
				// List<PlanLineGridY> listy = grid.getCrossStns();
				// for(PlanLineGridY y :listy){
				// / System.err.println(y.getStnName() + "#" +
				// y.getDptTime()+"#"+y.getStationType());
				// }
				// ////

				JSONObject editStnSort = new JSONObject();
				editStnSort.put("drawGraphIdAllIn", drawGraphIdAllIn);
				editStnSort.put("isCurrentBureau", isCurrentBureau);
				String myJlData = objectMapper.writeValueAsString(dataList);
				// 图形数据
				result.addObject("editStnSort", editStnSort);// 修改站序
				logger.info("drawGraphIdAllIn==" + drawGraphIdAllIn);
				result.addObject("myJlData", myJlData);
				logger.info("myJlData==" + myJlData);
				// System.err.println("myJlData==" + myJlData);
				String gridStr = objectMapper.writeValueAsString(grid);
				logger.info("gridStr==" + gridStr);
				// System.err.println("gridStr==" + gridStr);
				result.addObject("gridData", gridStr);

			}

		}

		return result;
	}

	// 修改站序
	@ResponseBody
	@RequestMapping(value = "/editStationSort", method = RequestMethod.GET)
	public ModelAndView editStationSortNewIframe(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView result = new ModelAndView("cross/editStationSortJsp");
		// 参照crossController 355/
		try {
			String drawGraphId = request.getParameter("drawGraphId");
			request.setAttribute("drawGraphId", drawGraphId);
			logger.info("queryTrainTimesNewIframe~~trainId==" + drawGraphId);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 根据drawGraphId查询drawGraphStnInfo
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossDicStnInfo", method = RequestMethod.GET)
	public Result getCrossDicStnInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		// 参照crossController 355/
		try {

			String drawGraphId = request.getParameter("drawGraphId");
			logger.info("getCrossDicStnInfo~~drawGraphId==" + drawGraphId);
			List<CrossDictStnInfo> stations = crossDictService
					.getCrossDicStnInfoByDrawGraphId(drawGraphId);
			result.setData(stations);
			logger.info("stations==" + stations);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 根据drawGraphId查询drawGraphStnInfo
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteStationSort", method = RequestMethod.GET)
	public Result deleteStationSort(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		// 参照crossController 355/
		try {

			String drawGraphId = request.getParameter("drawGraphId");
			logger.info("deleteStationSort~~drawGraphId==" + drawGraphId);
			crossDictService.deleteDrawGraphStnByDrawGraphId(drawGraphId);

			// List<CrossDictStnInfo> stations =
			// crossDictService.getCrossDicStnInfoByDrawGraphId(drawGraphId);
			// result.setData(stations);
			// logger.info("stations==" + stations);
		} catch (Exception e) {
		}
		return result;
	}

	// saveDicStationSort
	/**
	 * 修改交路图站序及其间距
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveDicStationSort", method = RequestMethod.POST)
	public Result saveDicStationSort(@RequestBody String reqStr) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		Result result = new Result();
		logger.info("修改交路图站序及其间距~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);

			JSONArray stationsObj = JSONArray.fromObject(reqObj
					.get("stationList"));
			if (stationsObj != null && stationsObj.size() > 0) {
				for (int i = 0; i < stationsObj.size(); i++) {
					CrossDictStnInfo temp = new CrossDictStnInfo();
					Integer isChangeed = Integer.valueOf(String
							.valueOf(stationsObj.getJSONObject(i).get(
									"isChangeed")));
					if (isChangeed == 1) {
						JSONObject obj = JSONObject.fromObject(stationsObj
								.getJSONObject(i));
						BeanUtils.copyProperties(temp, obj);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("stnSort", temp.getStnSort());
						map.put("heightDetail", temp.getHeightDetail());
						map.put("drawGraphStnId", temp.getDrawGraphStnId());
						crossDictService.updateDicStnSortAndDetail(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	private ModelAndView getDrawAgain(ModelAndView result, String crossName,
			String chartId, String crossId) throws IllegalAccessException,
			InvocationTargetException, Exception {
		// 经由信息，由后面调用接口获取，用户提供画图的坐标
		PlanLineGrid grid = null;
		Map<String, Object> crossMap = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 先查看表draw_graph中是否有该交路的信息

		// 使用方案名和交路名来确定
		List<CrossDictStnInfo> listDicStn = crossDictService
				.getCrossDictStnForChartId(crossName, chartId);
		// 用于纵坐标的列车list
		List<Station> stationList = new LinkedList<Station>();
		if (listDicStn != null && listDicStn.size() > 0) {
			for (CrossDictStnInfo dictStnInfo : listDicStn) {
				Station station = new Station();
				station.setStnName(dictStnInfo.getStnName());
				String stationType = dictStnInfo.getStnType();
				// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
				if ("1".equals(stationType)) {
					stationType = "0";
				} else if ("4".equals(stationType)) {
					stationType = "BT";
				} else if ("3".equals(stationType)) {
					stationType = "TZ";
				} else if ("2".equals(stationType)) {
					stationType = "FJK";
				} else {
					stationType = "-1";
				}

				station.setStationType(stationType);
				stationList.add(station);
			}
		}
		if (crossId != null) {
			// 根据crossId获取列车信息和始发站，终到站等信息
			List<BaseCrossTrainInfo> list = crossService
					.getCrossTrainInfoForCrossId(crossId);
			// 只有一条记录
			if (list != null && list.size() > 0) {
				BaseCrossTrainInfo trainInfo = list.get(0);
				// 列车信息
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				// 众坐标的站列表
				List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
				List<BaseCrossTrainSubInfo> trainList = trainInfo
						.getTrainList();
				if (trainList != null && trainList.size() > 0) {

					for (int i = 0; i < trainList.size(); i++) {

						// 列车信息
						BaseCrossTrainSubInfo subInfo = trainList.get(i);
						TrainInfoDto dto = new TrainInfoDto();
						dto.setTrainName(subInfo.getTrainNbr());
						String startBur = "";
						String endBur = "";
						// 列车经由时刻信息
						List<BaseCrossTrainInfoTime> stationTimeList = subInfo
								.getStationList();
						if (stationTimeList != null
								&& stationTimeList.size() > 0) {
							startBur = "["
									+ stationTimeList.get(0)
											.getBureauShortName() + "]";
							endBur = "["
									+ stationTimeList.get(
											stationTimeList.size() - 1)
											.getBureauShortName() + "]";
						}
						dto.setStartStn(subInfo.getStartStn()
								+ "["
								+ subInfo.getStationList().get(0)
										.getBureauShortName() + "]");
						dto.setEndStn(subInfo.getEndStn()
								+ "["
								+ subInfo
										.getStationList()
										.get(subInfo.getStationList().size() - 1)
										.getBureauShortName() + "]");
						dto.setStartDate(subInfo.getStartTime());
						dto.setEndDate(subInfo.getEndTime());
						// 列车经由时刻等信息
						List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
						// 用于纵坐标的列车list
						LinkedList<Station> trainsList = new LinkedList<Station>();
						if (stationTimeList != null
								&& stationTimeList.size() > 0) {
							for (int j = 0; j < stationTimeList.size(); j++) {
								// 纵坐标的一个站
								PlanLineSTNDto stnDto = new PlanLineSTNDto();
								BaseCrossTrainInfoTime trainTime = stationTimeList
										.get(j);
								trainTime.setStnName(trainTime.getStnName()
										+ "[" + trainTime.getBureauShortName()
										+ "]");
								BeanUtils.copyProperties(stnDto, trainTime);

								// if(!"-1".equals(stnDto.getStationType()) &&
								// !"BT".equals(stnDto.getStationType())) {
								Station station = new Station();
								station.setStnName(stnDto.getStnName());
								station.setDptTime(stnDto.getDptTime());
								station.setStationType(stnDto.getStationType());
								if (!"BT".equals(stnDto.getStationType())) {
									trainStns.add(stnDto);
								}
								trainsList.add(station);
								// }
							}
							dto.setTrainStns(trainStns);
							list1.add(trainsList);
						}
						trains.add(dto);
					}
				}

				// 组装接续关系
				List<CrossRelationDto> jxgx = getJxgx(trains);
				// guo
				crossMap.put("jxgx", jxgx);
				crossMap.put("trains", trains);
				crossMap.put("crossName", trainInfo.getCrossName());
				dataList.add(crossMap);

				/******** fortest *********/
				/*
				 * if(list1 != null&&list1.size() > 0){ for(LinkedList<Station>
				 * stationLists : list1){
				 * System.err.println("*****************************");
				 * for(Station station :stationLists){
				 * System.err.println(station.getStnName() + "|" +
				 * station.getDptTime() + "|" + station.getStationType());
				 * 
				 * } } }
				 */
				/*******************/
				List<PlanLineGridY> listGridY = getPlanLineGridY(stationList);
				List<PlanLineGridX> listGridX = getPlanLineGridX(
						trainInfo.getCrossStartDate(),
						trainInfo.getCrossEndDate());
				grid = new PlanLineGrid(listGridX, listGridY);

				// fortest
				// List<PlanLineGridY> listy = grid.getCrossStns();
				// for(PlanLineGridY y :listy){
				// / System.err.println(y.getStnName() + "#" +
				// y.getDptTime()+"#"+y.getStationType());
				// }
				// ////
				String myJlData = objectMapper.writeValueAsString(dataList);
				// 图形数据
				result.addObject("myJlData", myJlData);
				logger.info("myJlData==" + myJlData);
				// System.err.println("myJlData==" + myJlData);
				String gridStr = objectMapper.writeValueAsString(grid);
				logger.info("gridStr==" + gridStr);
				// System.err.println("gridStr==" + gridStr);
				result.addObject("gridData", gridStr);

			}

		}

		return result;
	}

	/**
	 * 列车运行线图
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCrossMap", method = RequestMethod.POST)
	public Result provideTrainLineChartDate(
			@RequestBody Map<String, Object> reqMap) throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
		String startTime = StringUtil.objToStr(reqMap.get("startTime"));
		String endTime = StringUtil.objToStr(reqMap.get("endTime"));
		
		
//		String planCrossId = "4c4393a2-092b-4531-8898-d5899f98a362";
//		String startTime = "2015-07-10";
//		String endTime = "2015-07-30";
		
		String startTimeTemp = startTime;
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String bureauShortName = user.getBureauShortName();

		// 查询列车运行线信息
		List<TrainLineInfo> list = crossService
				.getTrainPlanLineInfoForPlanCrossId(planCrossId,
						bureauShortName, startTime, endTime);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		if (list != null && list.size() > 0) {
			// 初始化开始时间，结束时间，画图横坐标
			startTime = DateUtil.parseDateTOyyyymmdd(list.get(0)
					.getTrainSubInfoList().get(0).getStartTime());
			endTime = DateUtil.parseDateTOyyyymmdd(list.get(0)
					.getTrainSubInfoList().get(0).getEndTime());
			// 循环组

			for (TrainLineInfo lineInfo : list) {
				Map<String, Object> crossMap = new HashMap<String, Object>();
				String groupSerialNbr = lineInfo.getGroupSerialNbr();
				// 列车信息列表
				List<TrainLineSubInfo> subInfoList = lineInfo
						.getTrainSubInfoList();
				// 列车信息
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				if (subInfoList != null && subInfoList.size() > 0) {

					// 循环列车
					for (TrainLineSubInfo subInfo : subInfoList) {
						// 取最小开始时间
						startTime = getStartDateMin(startTimeTemp, startTime,
								subInfo.getStartTime());
						endTime = getEndDateMax(endTime, subInfo.getEndTime());
						// 设置列车信息
						TrainInfoDto dto = new TrainInfoDto();
						// 起始站，终到站，分解口列表
						List<TrainLineSubInfoTime> subInfoTimeList = subInfo
								.getTrainStaionList();

						dto.setTrainName(subInfo.getTrainNbr());
						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							dto.setStartStn(subInfo.getStartStn()
									+ "["
									+ subInfoTimeList.get(0)
											.getBureauShortName() + "]");
							dto.setEndStn(subInfo.getEndStn()
									+ "["
									+ subInfoTimeList.get(
											subInfoTimeList.size() - 1)
											.getBureauShortName() + "]");
						}
						dto.setGroupSerialNbr(groupSerialNbr);
						dto.setStartDate(subInfo.getStartTime());
						dto.setEndDate(subInfo.getEndTime());
						dto.setPlanTrainId(subInfo.getPlanTrainId());
						dto.setPlanCrossId(subInfo.getPlanCrossId());
//						subInfo.setPassBureau("济");
						dto.setPassBureau(subInfo.getPassBureau());
						// bureauShortName
						if (subInfo.getPassBureau().indexOf(bureauShortName) >= 0) {
							dto.setIsModify(true);
						} else {
							dto.setIsModify(false);
						}

						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
							// 循环经由站
							for (TrainLineSubInfoTime subInfoTime : subInfoTimeList) {
								subInfoTime.setStnName(subInfoTime.getStnName()
										+ "["
										+ subInfoTime.getBureauShortName()
										+ "]");
								PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
								if (!"BT".equals(subInfoTime.getStationType())) {
									stnDtoStart.setArrTime(subInfoTime
											.getArrTime());
									stnDtoStart.setDptTime(subInfoTime
											.getDptTime());
									// stnDtoStart.setStayTime(subInfoTime.getStayTime());
									stnDtoStart.setStnName(subInfoTime
											.getStnName());
									stnDtoStart.setStationType(subInfoTime
											.getStationType());
									trainStns.add(stnDtoStart);
								}

							}
							dto.setTrainStns(trainStns);
						}
						trains.add(dto);
					}
				}

				// 组装接续关系
				List<CrossRelationDto> jxgx = getJxgx(trains);
				crossMap.put("jxgx", jxgx);
				crossMap.put("trains", trains);
				crossMap.put("groupSerialNbr", groupSerialNbr);
				dataList.add(crossMap);
			}

			PlanLineGrid grid = null;
			ObjectMapper objectMapper = new ObjectMapper();

			// 纵坐标
			List<Station> listStation = new ArrayList<Station>();
			// 组装坐标
			// 首先查看经由字典中是否有数据
			boolean isHaveDate = false;
			PlanCrossInfo planCrossInfo = crossService
					.getPlanCrossInfoForPlanCrossId(planCrossId);

			if (planCrossInfo != null) {

				String crossName = planCrossInfo.getCrossName();
				String chartId = planCrossInfo.getBaseChartId();
				// 通过baseCrossid查询经由站信息
				// 使用方案名和交路名来确定

				List<CrossDictStnInfo> listDictStn = crossDictService
						.getCrossDictStnForChartId(crossName, chartId);
				if (listDictStn != null && listDictStn.size() > 0) {
					isHaveDate = true;

					for (CrossDictStnInfo dictStn : listDictStn) {
						Station station = new Station();
						station.setStnName(dictStn.getStnName());
						String stationType = dictStn.getStnType();
						// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
						if ("1".equals(stationType)) {
							stationType = "0";
						} else if ("4".equals(stationType)) {
							stationType = "BT";
						} else if ("3".equals(stationType)) {
							stationType = "TZ";
						} else if ("2".equals(stationType)) {
							stationType = "FJK";
						}
						station.setStationType(stationType);
						if (!listStation.contains(station)) {

							listStation.add(station);
						}
					}
					// 生成横纵坐标
					List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
					List<PlanLineGridX> listGridX = getPlanLineGridX(startTime,
							endTime);
					grid = new PlanLineGrid(listGridX, listGridY);
				}

			}
			logger.info("isHaveDate===" + isHaveDate);
			// 经由字典中没有数据，则组装
			if (!isHaveDate) {
				if (dataList != null && dataList.size() > 0) {
					String startDate = "";
					String endDate = "";

					List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
					Map<String, Object> crossmap = dataList.get(0);
					List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
							.get("trains");
					for (int k = 0; k < trains.size(); k++) {
						// 取交路中的第一辆车的经由站信息
						TrainInfoDto dto = trains.get(k);
						// if(k==0){
						// startDate =
						// DateUtil.parseDateTOyyyymmdd(dto.getStartDate());
						// }
						// if(k==trains.size()-1){
						// endDate =
						// DateUtil.parseDateTOyyyymmdd(dto.getEndDate());
						// }
						LinkedList<Station> listStation1 = new LinkedList<Station>();

						List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
						for (PlanLineSTNDto trainStn : listTrainStn) {
							Station station = new Station();
							station.setStnName(trainStn.getStnName());
							station.setStationType(trainStn.getStationType());
							station.setDptTime(trainStn.getDptTime());
							listStation1.add(station);
						}
						if (!list1.contains(listStation1)) {
							list1.add(listStation1);
						}
					}
					// 生成横纵坐标
					grid = getPlanLineGridForAll(list1, startTime, endTime);
				}
				/*
				 * if(dataList != null && dataList.size() > 0){
				 * Map<String,Object> crossmap = dataList.get(0);
				 * List<TrainInfoDto> trains = (List<TrainInfoDto>)
				 * crossmap.get("trains"); //取交路中的第一辆车的经由站信息 TrainInfoDto dto =
				 * trains.get(0); List<PlanLineSTNDto> listTrainStn =
				 * dto.getTrainStns(); for(PlanLineSTNDto trainStn :
				 * listTrainStn){ Station station = new Station();
				 * station.setStnName(trainStn.getStnName());
				 * station.setStationType(trainStn.getStationType());
				 * listStation.add(station); } //生成横纵坐标 List<PlanLineGridY>
				 * listGridY = getPlanLineGridY(listStation);
				 * List<PlanLineGridX> listGridX =
				 * getPlanLineGridX(startTime,endTime); grid = new
				 * PlanLineGrid(listGridX, listGridY); }
				 */
			}

			String myJlData = objectMapper.writeValueAsString(dataList);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			String gridStr = objectMapper.writeValueAsString(grid);
			dataMap.put("myJlData", myJlData);
			dataMap.put("gridData", gridStr);
			// System.err.println("myJlData==" + myJlData);
			// System.err.println("gridStr==" + gridStr);
			result.setData(dataMap);

		}
		return result;
	}

	/**
	 * 列车运行线图
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCrossMapHighLine", method = RequestMethod.POST)
	public Result createCrossMapHighLine(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
		String startTime = StringUtil.objToStr(reqMap.get("startTime"));
		String endTime = StringUtil.objToStr(reqMap.get("endTime"));
		String highLineCrossId = StringUtil.objToStr(reqMap
				.get("highLineCrossId"));

		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String bureauShortName = user.getBureauShortName();

		// 查询列车运行线信息
		List<TrainLineInfo> list = crossService
				.getTrainPlanLineInfoForPlanCrossIdAndBureauShortName(
						highLineCrossId, bureauShortName);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		if (list != null && list.size() > 0) {

			// 临客
			if (("".equals(planCrossId) || planCrossId == null)
					&& ("".equals(highLineCrossId) || planCrossId == highLineCrossId)) {
				for (TrainLineInfo lineInfo : list) {
					Map<String, Object> crossMap = new HashMap<String, Object>();
					String groupSerialNbr = lineInfo.getGroupSerialNbr();
					// 列车信息列表
					List<TrainLineSubInfo> subInfoList = lineInfo
							.getTrainSubInfoList();// 临客，有很多相同的对象，但本来只应该有一个对象
					// 列车信息
					List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
					if (subInfoList != null && subInfoList.size() > 0) {
						String startBur = "";
						String endBur = "";

						if (subInfoList != null && subInfoList.size() > 0) {
							startBur = "["
									+ subInfoList.get(0).getTrainStaionList()
											.get(0).getBureauShortName() + "]";
							endBur = "["
									+ subInfoList.get(subInfoList.size() - 1)
											.getTrainStaionList().get(0)
											.getBureauShortName() + "]";
						}
						// 循环列车
						List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
						TrainInfoDto dto = new TrainInfoDto();
						for (TrainLineSubInfo subInfo : subInfoList) {// 临客，有很多相同的对象，但本来只应该有一个对象
							// 设置列车信息
							List<TrainLineSubInfoTime> subInfoTimeList = subInfo
									.getTrainStaionList();

							dto.setTrainName(subInfo.getTrainNbr());

							dto.setStartStn(subInfo.getStartStn() + startBur);
							dto.setEndStn(subInfo.getEndStn() + endBur);
							dto.setGroupSerialNbr(groupSerialNbr);
							dto.setStartDate(subInfo.getStartTime());
							dto.setEndDate(subInfo.getEndTime());
							dto.setPlanTrainId(subInfo.getPlanTrainId());
							// 起始站，终到站，分解口列表

							if (subInfoTimeList != null
									&& subInfoTimeList.size() > 0) {

								// 循环经由站
								for (TrainLineSubInfoTime subInfoTime : subInfoTimeList) {
									PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
									if (!"BT".equals(subInfoTime
											.getStationType())) {
										stnDtoStart.setArrTime(subInfoTime
												.getArrTime());
										stnDtoStart.setDptTime(subInfoTime
												.getDptTime());
										// stnDtoStart.setStayTime(subInfoTime.getStayTime());
										stnDtoStart.setStnName(subInfoTime
												.getStnName()
												+ "["
												+ subInfoTime
														.getBureauShortName()
												+ "]");
										stnDtoStart.setStationType(subInfoTime
												.getStationType());
										trainStns.add(stnDtoStart);
									}

								}
							}
						}
						dto.setTrainStns(trainStns);
						trains.add(dto);
					}

					// 组装接续关系
					List<CrossRelationDto> jxgx = getJxgx(trains);
					crossMap.put("jxgx", jxgx);
					crossMap.put("trains", trains);
					crossMap.put("groupSerialNbr", groupSerialNbr);
					dataList.add(crossMap);
				} // for end

				PlanLineGrid grid = null;
				ObjectMapper objectMapper = new ObjectMapper();

				// 纵坐标
				List<Station> listStation = new ArrayList<Station>();
				// 组装坐标

				if (dataList != null && dataList.size() > 0) {
					String startDate = "";
					String endDate = "";

					// List<LinkedList<Station>> list1 = new
					// ArrayList<LinkedList<Station>>();
					Map<String, Object> crossmap = dataList.get(0);
					List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
							.get("trains");
					for (int k = 0; k < trains.size(); k++) {
						// 取交路中的第一辆车的经由站信息
						TrainInfoDto dto = trains.get(k);
						if (k == 0) {
							startDate = DateUtil.parseDateTOyyyymmdd(dto
									.getStartDate());
						}
						if (k == trains.size() - 1) {
							endDate = DateUtil.parseDateTOyyyymmdd(dto
									.getEndDate());
						}
						LinkedList<Station> listStation1 = new LinkedList<Station>();

						List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
						for (PlanLineSTNDto trainStn : listTrainStn) {
							Station station = new Station();
							station.setStnName(trainStn.getStnName());
							station.setStationType(trainStn.getStationType());
							station.setDptTime(trainStn.getDptTime());
							listStation.add(station);
						}
						// list1.add(listStation1);
					}
					// 生成横纵坐标
					// grid = getPlanLineGridForAll(list1, startDate, endDate);
					List<PlanLineGridY> listGridY = getPlanLineGridY(StattionUtils
							.isRealyFJK(listStation));
					List<PlanLineGridX> listGridX = getPlanLineGridX(startTime,
							endTime);
					grid = new PlanLineGrid(listGridX, listGridY);
				}

				String myJlData = objectMapper.writeValueAsString(dataList);
				// 图形数据
				Map<String, Object> dataMap = new HashMap<String, Object>();
				String gridStr = objectMapper.writeValueAsString(grid);
				dataMap.put("myJlData", myJlData);
				dataMap.put("gridData", gridStr);
				// System.err.println("myJlData==" + myJlData);
				// System.err.println("gridStr==" + gridStr);
				result.setData(dataMap);

			} else {

				// 循环组
				for (TrainLineInfo lineInfo : list) {
					Map<String, Object> crossMap = new HashMap<String, Object>();
					String groupSerialNbr = lineInfo.getGroupSerialNbr();
					// 列车信息列表
					List<TrainLineSubInfo> subInfoList = lineInfo
							.getTrainSubInfoList();
					// 列车信息
					List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
					if (subInfoList != null && subInfoList.size() > 0) {

						// 循环列车
						for (TrainLineSubInfo subInfo : subInfoList) {
							// 设置列车信息
							TrainInfoDto dto = new TrainInfoDto();
							List<TrainLineSubInfoTime> subInfoTimeList = subInfo
									.getTrainStaionList();

							dto.setTrainName(subInfo.getTrainNbr());
							String startBur = "";
							String endBur = "";
							if (subInfoTimeList != null
									&& subInfoTimeList.size() > 0) {
								startBur = "["
										+ subInfoTimeList.get(0)
												.getBureauShortName() + "]";
								endBur = "["
										+ subInfoTimeList.get(
												subInfoTimeList.size() - 1)
												.getBureauShortName() + "]";
							}
							dto.setStartStn(subInfo.getStartStn() + startBur);
							dto.setEndStn(subInfo.getEndStn() + endBur);
							dto.setGroupSerialNbr(groupSerialNbr);
							dto.setStartDate(subInfo.getStartTime());
							dto.setEndDate(subInfo.getEndTime());
							dto.setPlanTrainId(subInfo.getPlanTrainId());
							// 起始站，终到站，分解口列表

							if (subInfoTimeList != null
									&& subInfoTimeList.size() > 0) {
								List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
								// 循环经由站
								for (TrainLineSubInfoTime subInfoTime : subInfoTimeList) {
									PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
									// if(!"BT".equals(subInfoTime.getStationType()))
									// {
									stnDtoStart.setArrTime(subInfoTime
											.getArrTime());
									stnDtoStart.setDptTime(subInfoTime
											.getDptTime());
									// stnDtoStart.setStayTime(subInfoTime.getStayTime());
									stnDtoStart.setStnName(subInfoTime
											.getStnName()
											+ "["
											+ subInfoTime.getBureauShortName()
											+ "]");
									stnDtoStart.setStationType(subInfoTime
											.getStationType());
									trainStns.add(stnDtoStart);
									// }

								}
								dto.setTrainStns(trainStns);
							}
							trains.add(dto);
						}
					}

					// 组装接续关系
					List<CrossRelationDto> jxgx = getJxgx(trains);
					crossMap.put("jxgx", jxgx);
					crossMap.put("trains", trains);
					crossMap.put("groupSerialNbr", groupSerialNbr);
					dataList.add(crossMap);
				} // for end

				PlanLineGrid grid = null;
				ObjectMapper objectMapper = new ObjectMapper();

				// 纵坐标
				List<Station> listStation = new ArrayList<Station>();
				// 组装坐标
				// 首先查看经由字典中是否有数据
				boolean isHaveDate = false;
				PlanCrossInfo planCrossInfo = crossService
						.getPlanCrossInfoByPlanCrossId(planCrossId);

				if (planCrossInfo != null) {

					String crossName = planCrossInfo.getCrossName();
					String chartId = planCrossInfo.getBaseChartId();
					// 通过baseCrossid查询经由站信息
					// 使用方案名和交路名来确定

					List<CrossDictStnInfo> listDictStn = crossDictService
							.getCrossDictStnByChartId(crossName, chartId);
					if (listDictStn != null && listDictStn.size() > 0) {
						isHaveDate = true;

						for (CrossDictStnInfo dictStn : listDictStn) {
							Station station = new Station();
							station.setStnName(dictStn.getStnName());
							String stationType = dictStn.getStnType();
							// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
							if ("1".equals(stationType)) {
								stationType = "0";
							} else if ("4".equals(stationType)) {
								stationType = "BT";
							} else if ("3".equals(stationType)) {
								stationType = "TZ";
							} else if ("2".equals(stationType)) {
								stationType = "FJK";
							}
							station.setStationType(stationType);
							listStation.add(station);
						}
						// 生成横纵坐标
						List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
						List<PlanLineGridX> listGridX = getPlanLineGridX(
								startTime, endTime);
						grid = new PlanLineGrid(listGridX, listGridY);
					}

				}
				logger.info("isHaveDate===" + isHaveDate);
				// 经由字典中没有数据，则组装
				if (!isHaveDate) {

					if (dataList != null && dataList.size() > 0) {
						String startDate = "";
						String endDate = "";

						List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
						Map<String, Object> crossmap = dataList.get(0);
						List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
								.get("trains");
						for (int k = 0; k < trains.size(); k++) {
							// 取交路中的第一辆车的经由站信息
							TrainInfoDto dto = trains.get(k);
							if (k == 0) {
								startDate = DateUtil.parseDateTOyyyymmdd(dto
										.getStartDate());
							}
							if (k == trains.size() - 1) {
								endDate = DateUtil.parseDateTOyyyymmdd(dto
										.getEndDate());
							}
							LinkedList<Station> listStation1 = new LinkedList<Station>();

							List<PlanLineSTNDto> listTrainStn = dto
									.getTrainStns();
							for (PlanLineSTNDto trainStn : listTrainStn) {
								Station station = new Station();
								station.setStnName(trainStn.getStnName());
								station.setStationType(trainStn
										.getStationType());
								station.setDptTime(trainStn.getDptTime());
								listStation1.add(station);
							}
							list1.add(listStation1);
						}
						// 生成横纵坐标
						grid = getPlanLineGridForAll(list1, startDate, endDate);
						// List<PlanLineGridY> listGridY =
						// getPlanLineGridY(listStation);
						// List<PlanLineGridX> listGridX =
						// getPlanLineGridX(startTime,endTime);
						// List<PlanLineGridY> listGridY = grid.getCrossStns();
						// List<PlanLineGridX> listGridX = grid.getDays();
						// grid = new PlanLineGrid(listGridX, listGridY);
					}
				}

				String myJlData = objectMapper.writeValueAsString(dataList);
				// 图形数据
				Map<String, Object> dataMap = new HashMap<String, Object>();
				String gridStr = objectMapper.writeValueAsString(grid);
				dataMap.put("myJlData", myJlData);
				dataMap.put("gridData", gridStr);
				// System.err.println("myJlData==" + myJlData);
				// System.err.println("gridStr==" + gridStr);
				result.setData(dataMap);

			}
		}// else end
		return result;
	}

	/**
	 * 列车运行线图
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCrossMapVehicle", method = RequestMethod.POST)
	public Result createCrossMapVehicle(@RequestBody Map<String, Object> reqMap)
			throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
		String startTime = StringUtil.objToStr(reqMap.get("startTime"));
		String endTime = StringUtil.objToStr(reqMap.get("endTime"));
		String highLineCrossId = StringUtil.objToStr(reqMap
				.get("highLineCrossId"));

		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String bureauShortName = user.getBureauShortName();

		// 查询列车运行线信息
		List<TrainLineInfo> list = crossService
				.getTrainPlanLineInfoForPlanCrossIdAndVehicle(highLineCrossId,
						bureauShortName);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		if (list != null && list.size() > 0) {
			// 循环组
			for (TrainLineInfo lineInfo : list) {
				Map<String, Object> crossMap = new HashMap<String, Object>();
				String groupSerialNbr = lineInfo.getGroupSerialNbr();
				// 列车信息列表
				List<TrainLineSubInfo> subInfoList = lineInfo
						.getTrainSubInfoList();
				// 列车信息
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				if (subInfoList != null && subInfoList.size() > 0) {

					// 循环列车
					for (TrainLineSubInfo subInfo : subInfoList) {
						// 设置列车信息
						TrainInfoDto dto = new TrainInfoDto();
						List<TrainLineSubInfoTime> subInfoTimeList = subInfo
								.getTrainStaionList();
						dto.setTrainName(subInfo.getTrainNbr());
						String startBur = "";
						String endBur = "";
						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							startBur = "["
									+ subInfoTimeList.get(0)
											.getBureauShortName() + "]";
							endBur = "["
									+ subInfoTimeList.get(
											subInfoTimeList.size() - 1)
											.getBureauShortName() + "]";
						}

						dto.setStartStn(subInfo.getStartStn() + startBur);
						dto.setEndStn(subInfo.getEndStn() + endBur);
						dto.setGroupSerialNbr(groupSerialNbr);
						dto.setStartDate(subInfo.getStartTime());
						dto.setEndDate(subInfo.getEndTime());
						dto.setPlanTrainId(subInfo.getPlanTrainId());
						// 起始站，终到站，分解口列表

						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
							// 循环经由站
							for (TrainLineSubInfoTime subInfoTime : subInfoTimeList) {
								PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
								// if(!"BT".equals(subInfoTime.getStationType()))
								// {
								stnDtoStart
										.setArrTime(subInfoTime.getArrTime());
								stnDtoStart
										.setDptTime(subInfoTime.getDptTime());
								// stnDtoStart.setStayTime(subInfoTime.getStayTime());
								stnDtoStart.setStnName(subInfoTime.getStnName()
										+ "["
										+ subInfoTime.getBureauShortName()
										+ "]");
								stnDtoStart.setStationType(subInfoTime
										.getStationType());
								trainStns.add(stnDtoStart);
								// }

							}
							dto.setTrainStns(trainStns);
						}
						trains.add(dto);
					}
				}

				// 组装接续关系
				List<CrossRelationDto> jxgx = getJxgx(trains);
				crossMap.put("jxgx", jxgx);
				crossMap.put("trains", trains);
				crossMap.put("groupSerialNbr", groupSerialNbr);
				dataList.add(crossMap);
			}

			PlanLineGrid grid = null;
			ObjectMapper objectMapper = new ObjectMapper();

			// 纵坐标
			List<Station> listStation = new ArrayList<Station>();
			// 组装坐标
			// 首先查看经由字典中是否有数据
			boolean isHaveDate = false;
			PlanCrossInfo planCrossInfo = crossService
					.getPlanCrossInfoByPlanCrossIdVehicle(planCrossId);

			if (planCrossInfo != null) {

				String crossName = planCrossInfo.getCrossName();
				String chartId = planCrossInfo.getBaseChartId();
				// 通过baseCrossid查询经由站信息
				// 使用方案名和交路名来确定

				List<CrossDictStnInfo> listDictStn = crossDictService
						.getCrossDictStnByChartIdVehicle(crossName, chartId);
				if (listDictStn != null && listDictStn.size() > 0) {
					isHaveDate = true;

					for (CrossDictStnInfo dictStn : listDictStn) {
						Station station = new Station();
						station.setStnName(dictStn.getStnName());
						String stationType = dictStn.getStnType();
						// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
						if ("1".equals(stationType)) {
							stationType = "0";
						} else if ("4".equals(stationType)) {
							stationType = "BT";
						} else if ("3".equals(stationType)) {
							stationType = "TZ";
						} else if ("2".equals(stationType)) {
							stationType = "FJK";
						}
						station.setStationType(stationType);
						listStation.add(station);
					}
					// 生成横纵坐标
					List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
					List<PlanLineGridX> listGridX = getPlanLineGridX(startTime,
							endTime);
					grid = new PlanLineGrid(listGridX, listGridY);
				}

			}
			logger.info("isHaveDate===" + isHaveDate);
			// 经由字典中没有数据，则组装
			if (!isHaveDate) {
				if (dataList != null && dataList.size() > 0) {
					String startDate = "";
					String endDate = "";

					List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
					Map<String, Object> crossmap = dataList.get(0);
					List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
							.get("trains");
					for (int k = 0; k < trains.size(); k++) {
						// 取交路中的第一辆车的经由站信息
						TrainInfoDto dto = trains.get(k);
						if (k == 0) {
							startDate = DateUtil.parseDateTOyyyymmdd(dto
									.getStartDate());
						}
						if (k == trains.size() - 1) {
							endDate = DateUtil.parseDateTOyyyymmdd(dto
									.getEndDate());
						}
						LinkedList<Station> listStation1 = new LinkedList<Station>();

						List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
						for (PlanLineSTNDto trainStn : listTrainStn) {
							Station station = new Station();
							station.setStnName(trainStn.getStnName());
							station.setStationType(trainStn.getStationType());
							station.setDptTime(trainStn.getDptTime());
							listStation1.add(station);
						}
						list1.add(listStation1);
					}
					// 生成横纵坐标
					grid = getPlanLineGridForAll(list1, startDate, endDate);
				}
				/*
				 * if(dataList != null && dataList.size() > 0){
				 * Map<String,Object> crossmap = dataList.get(0);
				 * List<TrainInfoDto> trains = (List<TrainInfoDto>)
				 * crossmap.get("trains"); //取交路中的第一辆车的经由站信息 TrainInfoDto dto =
				 * trains.get(0); List<PlanLineSTNDto> listTrainStn =
				 * dto.getTrainStns(); for(PlanLineSTNDto trainStn :
				 * listTrainStn){ Station station = new Station();
				 * station.setStnName(trainStn.getStnName());
				 * station.setStationType(trainStn.getStationType());
				 * listStation.add(station); } //生成横纵坐标 List<PlanLineGridY>
				 * listGridY = getPlanLineGridY(listStation);
				 * List<PlanLineGridX> listGridX =
				 * getPlanLineGridX(startTime,endTime); grid = new
				 * PlanLineGrid(listGridX, listGridY); }
				 */
			}

			String myJlData = objectMapper.writeValueAsString(dataList);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			String gridStr = objectMapper.writeValueAsString(grid);
			dataMap.put("myJlData", myJlData);
			dataMap.put("gridData", gridStr);
			// System.err.println("myJlData==" + myJlData);
			// System.err.println("gridStr==" + gridStr);
			result.setData(dataMap);

		}
		return result;
	}

	/**
	 * 列车运行线图（）
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCrossMapVehicleBy2", method = RequestMethod.POST)
	public Result createCrossMapVehicleBy2(
			@RequestBody Map<String, Object> reqMap) throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
		String startTime = StringUtil.objToStr(reqMap.get("startTime"));
		String endTime = StringUtil.objToStr(reqMap.get("endTime"));
		String highLineCrossId = StringUtil.objToStr(reqMap
				.get("highLineCrossId"));

		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String bureauShortName = user.getBureauShortName();

		// 查询列车运行线信息
		List<TrainLineInfo> list = crossService
				.getTrainPlanLineInfoForPlanCrossIdAndVehicleBy2(
						highLineCrossId, bureauShortName);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		if (list != null && list.size() > 0) {
			// 循环组
			for (TrainLineInfo lineInfo : list) {
				Map<String, Object> crossMap = new HashMap<String, Object>();
				String groupSerialNbr = lineInfo.getGroupSerialNbr();
				// 列车信息列表
				List<TrainLineSubInfo> subInfoList = lineInfo
						.getTrainSubInfoList();
				// 列车信息
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				if (subInfoList != null && subInfoList.size() > 0) {

					// 循环列车
					for (TrainLineSubInfo subInfo : subInfoList) {
						// 设置列车信息
						TrainInfoDto dto = new TrainInfoDto();
						// 起始站，终到站，分解口列表
						List<TrainLineSubInfoTime> subInfoTimeList = subInfo
								.getTrainStaionList();
						String startBur = "";
						String endBur = "";
						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							startBur = "["
									+ subInfoTimeList.get(0)
											.getBureauShortName() + "]";
							endBur = "["
									+ subInfoTimeList.get(
											subInfoTimeList.size() - 1)
											.getBureauShortName() + "]";
						}
						dto.setTrainName(subInfo.getTrainNbr());
						dto.setStartStn(subInfo.getStartStn() + startBur);
						dto.setEndStn(subInfo.getEndStn() + endBur);
						dto.setGroupSerialNbr(groupSerialNbr);
						dto.setStartDate(subInfo.getStartTime());
						dto.setEndDate(subInfo.getEndTime());
						dto.setPlanTrainId(subInfo.getPlanTrainId());

						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
							// 循环经由站
							for (TrainLineSubInfoTime subInfoTime : subInfoTimeList) {
								PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
								// if(!"BT".equals(subInfoTime.getStationType()))
								// {
								stnDtoStart
										.setArrTime(subInfoTime.getArrTime());
								stnDtoStart
										.setDptTime(subInfoTime.getDptTime());
								// stnDtoStart.setStayTime(subInfoTime.getStayTime());
								stnDtoStart.setStnName(subInfoTime.getStnName()
										+ "["
										+ subInfoTime.getBureauShortName()
										+ "]");
								stnDtoStart.setStationType(subInfoTime
										.getStationType());
								trainStns.add(stnDtoStart);
								// }

							}
							dto.setTrainStns(trainStns);
						}
						trains.add(dto);
					}
				}

				// 组装接续关系
				List<CrossRelationDto> jxgx = getJxgx(trains);
				crossMap.put("jxgx", jxgx);
				crossMap.put("trains", trains);
				crossMap.put("groupSerialNbr", groupSerialNbr);
				dataList.add(crossMap);
			}

			PlanLineGrid grid = null;
			ObjectMapper objectMapper = new ObjectMapper();

			// 纵坐标
			List<Station> listStation = new ArrayList<Station>();
			// 组装坐标
			// 首先查看经由字典中是否有数据
			boolean isHaveDate = false;
			PlanCrossInfo planCrossInfo = crossService
					.getPlanCrossInfoByPlanCrossIdVehicleBy2(planCrossId);

			if (planCrossInfo != null) {

				String crossName = planCrossInfo.getCrossName();
				String chartId = planCrossInfo.getBaseChartId();
				// 通过baseCrossid查询经由站信息
				// 使用方案名和交路名来确定

				List<CrossDictStnInfo> listDictStn = crossDictService
						.getCrossDictStnByChartIdVehicleBy2(crossName, chartId);
				if (listDictStn != null && listDictStn.size() > 0) {
					isHaveDate = true;

					for (CrossDictStnInfo dictStn : listDictStn) {
						Station station = new Station();
						station.setStnName(dictStn.getStnName());
						String stationType = dictStn.getStnType();
						// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
						if ("1".equals(stationType)) {
							stationType = "0";
						} else if ("4".equals(stationType)) {
							stationType = "BT";
						} else if ("3".equals(stationType)) {
							stationType = "TZ";
						} else if ("2".equals(stationType)) {
							stationType = "FJK";
						}
						station.setStationType(stationType);
						listStation.add(station);
					}
					// 生成横纵坐标
					List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
					List<PlanLineGridX> listGridX = getPlanLineGridX(startTime,
							endTime);
					grid = new PlanLineGrid(listGridX, listGridY);
				}

			}
			logger.info("isHaveDate===" + isHaveDate);
			// 经由字典中没有数据，则组装
			if (!isHaveDate) {
				if (dataList != null && dataList.size() > 0) {
					String startDate = "";
					String endDate = "";

					List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
					Map<String, Object> crossmap = dataList.get(0);
					List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
							.get("trains");
					for (int k = 0; k < trains.size(); k++) {
						// 取交路中的第一辆车的经由站信息
						TrainInfoDto dto = trains.get(k);
						if (k == 0) {
							startDate = DateUtil.parseDateTOyyyymmdd(dto
									.getStartDate());
						}
						if (k == trains.size() - 1) {
							endDate = DateUtil.parseDateTOyyyymmdd(dto
									.getEndDate());
						}
						LinkedList<Station> listStation1 = new LinkedList<Station>();

						List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
						for (PlanLineSTNDto trainStn : listTrainStn) {
							Station station = new Station();
							station.setStnName(trainStn.getStnName());
							station.setStationType(trainStn.getStationType());
							station.setDptTime(trainStn.getDptTime());
							listStation1.add(station);
						}
						list1.add(listStation1);
					}
					// 生成横纵坐标
					grid = getPlanLineGridForAll(list1, startDate, endDate);
				}
				/*
				 * if(dataList != null && dataList.size() > 0){
				 * Map<String,Object> crossmap = dataList.get(0);
				 * List<TrainInfoDto> trains = (List<TrainInfoDto>)
				 * crossmap.get("trains"); //取交路中的第一辆车的经由站信息 TrainInfoDto dto =
				 * trains.get(0); List<PlanLineSTNDto> listTrainStn =
				 * dto.getTrainStns(); for(PlanLineSTNDto trainStn :
				 * listTrainStn){ Station station = new Station();
				 * station.setStnName(trainStn.getStnName());
				 * station.setStationType(trainStn.getStationType());
				 * listStation.add(station); } //生成横纵坐标 List<PlanLineGridY>
				 * listGridY = getPlanLineGridY(listStation);
				 * List<PlanLineGridX> listGridX =
				 * getPlanLineGridX(startTime,endTime); grid = new
				 * PlanLineGrid(listGridX, listGridY); }
				 */
			}

			String myJlData = objectMapper.writeValueAsString(dataList);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			String gridStr = objectMapper.writeValueAsString(grid);
			dataMap.put("myJlData", myJlData);
			dataMap.put("gridData", gridStr);
			// System.err.println("myJlData==" + myJlData);
			// System.err.println("gridStr==" + gridStr);
			result.setData(dataMap);

		}
		return result;
	}

	/**
	 * 列车运行线图
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCrossMapVehicleSearch", method = RequestMethod.POST)
	public Result createCrossMapVehicleSearch(
			@RequestBody Map<String, Object> reqMap) throws Exception {
		Result result = new Result();
		String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
		String startTime = StringUtil.objToStr(reqMap.get("startTime"));
		String endTime = StringUtil.objToStr(reqMap.get("endTime"));
		String highLineCrossId = StringUtil.objToStr(reqMap
				.get("highLineCrossId"));

		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String bureauShortName = user.getBureauShortName();

		// 查询列车运行线信息
		List<TrainLineInfo> list = crossService
				.getTrainPlanLineInfoForPlanCrossIdAndVehicleSearch(
						highLineCrossId, bureauShortName);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		if (list != null && list.size() > 0) {
			// 循环组
			for (TrainLineInfo lineInfo : list) {
				Map<String, Object> crossMap = new HashMap<String, Object>();
				String groupSerialNbr = lineInfo.getGroupSerialNbr();
				// 列车信息列表
				List<TrainLineSubInfo> subInfoList = lineInfo
						.getTrainSubInfoList();
				// 列车信息
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				if (subInfoList != null && subInfoList.size() > 0) {

					// 循环列车
					for (TrainLineSubInfo subInfo : subInfoList) {
						// 设置列车信息
						TrainInfoDto dto = new TrainInfoDto();
						List<TrainLineSubInfoTime> subInfoTimeList = subInfo
								.getTrainStaionList();
						String stn1 = "";
						String stn2 = "";
						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							stn1 = "["
									+ subInfoTimeList.get(0)
											.getBureauShortName() + "]";
							stn2 = "["
									+ subInfoTimeList.get(
											subInfoTimeList.size() - 1)
											.getBureauShortName() + "]";
						}
						dto.setTrainName(subInfo.getTrainNbr());
						dto.setStartStn(subInfo.getStartStn() + stn1);
						dto.setEndStn(subInfo.getEndStn() + stn2);
						dto.setGroupSerialNbr(groupSerialNbr);
						dto.setStartDate(subInfo.getStartTime());
						dto.setEndDate(subInfo.getEndTime());
						dto.setPlanTrainId(subInfo.getPlanTrainId());
						// 起始站，终到站，分解口列表

						if (subInfoTimeList != null
								&& subInfoTimeList.size() > 0) {
							List<PlanLineSTNDto> trainStns = new ArrayList<PlanLineSTNDto>();
							// 循环经由站
							for (TrainLineSubInfoTime subInfoTime : subInfoTimeList) {
								PlanLineSTNDto stnDtoStart = new PlanLineSTNDto();
								subInfoTime.setStnName(subInfoTime.getStnName()
										+ "["
										+ subInfoTime.getBureauShortName()
										+ "]");
								// if(!"BT".equals(subInfoTime.getStationType()))
								// {
								stnDtoStart
										.setArrTime(subInfoTime.getArrTime());
								stnDtoStart
										.setDptTime(subInfoTime.getDptTime());
								// stnDtoStart.setStayTime(subInfoTime.getStayTime());
								stnDtoStart
										.setStnName(subInfoTime.getStnName());
								stnDtoStart.setStationType(subInfoTime
										.getStationType());
								trainStns.add(stnDtoStart);
								// }

							}
							dto.setTrainStns(trainStns);
						}
						trains.add(dto);
					}
				}

				// 组装接续关系
				List<CrossRelationDto> jxgx = getJxgx(trains);
				crossMap.put("jxgx", jxgx);
				crossMap.put("trains", trains);
				crossMap.put("groupSerialNbr", groupSerialNbr);
				dataList.add(crossMap);
			}

			PlanLineGrid grid = null;
			ObjectMapper objectMapper = new ObjectMapper();

			// 纵坐标
			List<Station> listStation = new ArrayList<Station>();
			// 组装坐标
			// 首先查看经由字典中是否有数据
			boolean isHaveDate = false;
			PlanCrossInfo planCrossInfo = crossService
					.getPlanCrossInfoByPlanCrossIdVehicleSearch(planCrossId);

			if (planCrossInfo != null) {

				String crossName = planCrossInfo.getCrossName();
				String chartId = planCrossInfo.getBaseChartId();
				// 通过baseCrossid查询经由站信息
				// 使用方案名和交路名来确定

				List<CrossDictStnInfo> listDictStn = crossDictService
						.getCrossDictStnByChartIdVehicleSearch(crossName,
								chartId);
				if (listDictStn != null && listDictStn.size() > 0) {
					isHaveDate = true;

					for (CrossDictStnInfo dictStn : listDictStn) {
						Station station = new Station();
						station.setStnName(dictStn.getStnName());
						String stationType = dictStn.getStnType();
						// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
						if ("1".equals(stationType)) {
							stationType = "0";
						} else if ("4".equals(stationType)) {
							stationType = "BT";
						} else if ("3".equals(stationType)) {
							stationType = "TZ";
						} else if ("2".equals(stationType)) {
							stationType = "FJK";
						}
						station.setStationType(stationType);
						listStation.add(station);
					}
					// 生成横纵坐标
					List<PlanLineGridY> listGridY = getPlanLineGridY(listStation);
					List<PlanLineGridX> listGridX = getPlanLineGridX(startTime,
							endTime);
					grid = new PlanLineGrid(listGridX, listGridY);
				}

			}
			logger.info("isHaveDate===" + isHaveDate);
			// 经由字典中没有数据，则组装
			if (!isHaveDate) {
				if (dataList != null && dataList.size() > 0) {
					String startDate = "";
					String endDate = "";

					List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
					Map<String, Object> crossmap = dataList.get(0);
					List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
							.get("trains");
					for (int k = 0; k < trains.size(); k++) {
						// 取交路中的第一辆车的经由站信息
						TrainInfoDto dto = trains.get(k);
						if (k == 0) {
							startDate = DateUtil.parseDateTOyyyymmdd(dto
									.getStartDate());
						}
						if (k == trains.size() - 1) {
							endDate = DateUtil.parseDateTOyyyymmdd(dto
									.getEndDate());
						}
						LinkedList<Station> listStation1 = new LinkedList<Station>();

						List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
						for (PlanLineSTNDto trainStn : listTrainStn) {
							Station station = new Station();
							station.setStnName(trainStn.getStnName());
							station.setStationType(trainStn.getStationType());
							station.setDptTime(trainStn.getDptTime());
							listStation1.add(station);
						}
						list1.add(listStation1);
					}
					// 生成横纵坐标
					grid = getPlanLineGridForAll(list1, startDate, endDate);
				}
				/*
				 * if(dataList != null && dataList.size() > 0){
				 * Map<String,Object> crossmap = dataList.get(0);
				 * List<TrainInfoDto> trains = (List<TrainInfoDto>)
				 * crossmap.get("trains"); //取交路中的第一辆车的经由站信息 TrainInfoDto dto =
				 * trains.get(0); List<PlanLineSTNDto> listTrainStn =
				 * dto.getTrainStns(); for(PlanLineSTNDto trainStn :
				 * listTrainStn){ Station station = new Station();
				 * station.setStnName(trainStn.getStnName());
				 * station.setStationType(trainStn.getStationType());
				 * listStation.add(station); } //生成横纵坐标 List<PlanLineGridY>
				 * listGridY = getPlanLineGridY(listStation);
				 * List<PlanLineGridX> listGridX =
				 * getPlanLineGridX(startTime,endTime); grid = new
				 * PlanLineGrid(listGridX, listGridY); }
				 */
			}

			String myJlData = objectMapper.writeValueAsString(dataList);
			// 图形数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			String gridStr = objectMapper.writeValueAsString(grid);
			dataMap.put("myJlData", myJlData);
			dataMap.put("gridData", gridStr);
			// System.err.println("myJlData==" + myJlData);
			// System.err.println("gridStr==" + gridStr);
			result.setData(dataMap);

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

	/**
	 * 组装坐标轴数据
	 * 
	 * @param list1
	 *            列车列表
	 * @param crossStartDate
	 *            交路开始日期，格式yyyy-MM-dd
	 * @param crossEndDate
	 *            交路终到日期，格式yyyy-MM-dd
	 * @return 坐标轴对象
	 */
	private PlanLineGrid getPlanLineGridForAll(List<LinkedList<Station>> list1,
			String crossStartDate, String crossEndDate) {
		// 纵坐标
		List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		// 横坐标
		List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>();

		// 情空list中的不停站
		// if(!list1.isEmpty()) {
		// for(LinkedList<Station> listStation : list1) {
		// if(!listStation.isEmpty()) {
		// for() {
		//
		// }
		// }
		// }
		// }

		/**** 组装纵坐标 ****/
		int trainSize = list1.size();
		List<Station> mergeList = new LinkedList<Station>();
		for (int i = 0; i < trainSize; i++) {

			if (i == 0) {
				LinkedList<Station> listStation1 = list1.get(0);

				LinkedList<Station> listStation2 = null;
				if (list1.size() > 1) {
					listStation2 = list1.get(1);
				}
				boolean isSameDirection = StattionUtils.isSameDirection(
						listStation1, listStation2);
				if (isSameDirection) {
					mergeList = StattionUtils.mergeStationTheSameDirection(
							listStation1, listStation2);
				} else {

					if (listStation1.size() >= listStation2.size()) {
						mergeList = StattionUtils.mergeStation(listStation1,
								listStation2);
					} else {
						mergeList = StattionUtils.mergeStation(listStation2,
								listStation1);
					}

				}
			} else if (i > 1) {
				LinkedList<Station> trainStationCurrentList = list1.get(i);
				boolean isSameDirection = StattionUtils.isSameDirection(
						trainStationCurrentList, mergeList);
				if (isSameDirection) {
					mergeList = StattionUtils.mergeStationTheSameDirection(
							mergeList, trainStationCurrentList);
				} else {
					if (mergeList.size() >= trainStationCurrentList.size()) {
						mergeList = StattionUtils.mergeStation(mergeList,
								trainStationCurrentList);
					} else {
						mergeList = StattionUtils.mergeStation(
								trainStationCurrentList, mergeList);
					}
				}

			}

		}

		for (Station station : mergeList) {
			// if(null != station && !"BT".equals(station.getStationType())){
			if (null != station) {
				// planLineGridYList.add(new PlanLineGridY(stationName));
				planLineGridYList.add(new PlanLineGridY(station.getStnName(),
						0, station.getStationType()));
			}

		}

		/***** 组装横坐标 *****/

		LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseLocalDate(crossStartDate);
		LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseLocalDate(crossEndDate));
		while (!start.isAfter(end)) {
			gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
			start = start.plusDays(1);
		}

		return new PlanLineGrid(gridXList, planLineGridYList);
	}

	/**
	 * 组装坐标轴数据
	 * 
	 * @param stationsInfo
	 *            经由站信息对象
	 * @param crossStartDate
	 *            交路开始日期，格式yyyy-MM-dd
	 * @param crossEndDate
	 *            交路终到日期，格式yyyy-MM-dd
	 * @return 坐标轴对象
	 */
	private PlanLineGrid getPlanLineGrid(List<String> stationsInfo,
			String crossStartDate, String crossEndDate) {
		// 纵坐标
		List<PlanLineGridY> planLineGridYList = new ArrayList<PlanLineGridY>();
		// 横坐标
		List<PlanLineGridX> gridXList = new ArrayList<PlanLineGridX>();

		/**** 组装纵坐标 ****/
		if (stationsInfo != null) {

			for (String stationName : stationsInfo) {
				if (stationName != null) {
					// planLineGridYList.add(new PlanLineGridY(stationName));
					planLineGridYList
							.add(new PlanLineGridY(stationName, 0, "0"));
				}

			}

		}

		/***** 组装横坐标 *****/

		LocalDate start = DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseLocalDate(crossStartDate);
		LocalDate end = new LocalDate(DateTimeFormat.forPattern("yyyy-MM-dd")
				.parseLocalDate(crossEndDate));
		while (!start.isAfter(end)) {
			gridXList.add(new PlanLineGridX(start.toString("yyyy-MM-dd")));
			start = start.plusDays(1);
		}

		return new PlanLineGrid(gridXList, planLineGridYList);
	}

	/**
	 * 组装接续关系
	 * 
	 * @param trains
	 * @return
	 */
	private List<CrossRelationDto> getJxgx(List<TrainInfoDto> trains) {
		List<CrossRelationDto> returnList = new ArrayList<CrossRelationDto>();
		if (trains != null && trains.size() > 0) {
			int size = trains.size();
			for (int i = 0; i < size; i++) {
				// 接续关系的对象
				CrossRelationDto dto = new CrossRelationDto();
				int temp = i + 1;
				if (temp < size) {
					TrainInfoDto dtoCurrent = trains.get(i);
					TrainInfoDto dtoNext = trains.get(temp);

					// 取i的终点站信息
					dto.setFromStnName(dtoCurrent.getEndStn());
					// 取i的终点站日期和时刻进行组合
					dto.setFromTime(dtoCurrent.getEndDate());
					dto.setFromStartStnName(dtoCurrent.getStartStn());

					// 设置i-1始发日期和时刻
					dto.setToTime(dtoNext.getStartDate());
					// 取i-1的起点信息
					dto.setToStnName(dtoNext.getStartStn());
					dto.setToEndStnName(dtoNext.getEndStn());
					returnList.add(dto);
				}

			}
		}
		return returnList;
	}

	/**
	 * 5.2.4 更新给定列车的基本图运行线车底交路id
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUnitCrossId", method = RequestMethod.POST)
	public Result updateUnitCrossId(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		String unitCrossIds = StringUtil.objToStr(reqMap.get("unitCrossIds"));
		logger.info("updateUnitCrossId----unitCrossIds==" + unitCrossIds);

		try {
			if (unitCrossIds != null) {
				JSONArray resultArr = new JSONArray();
				String[] crossArray = unitCrossIds.split(",");
				// 取一条unit_cross_id查询出方案id
				String unitCrossid = crossArray[0];
				CrossInfo unitCrossInfo = crossService
						.getUnitCrossInfoForUnitCrossid(unitCrossid);
				// 方案id
				String baseChartId = unitCrossInfo.getChartId();

				for (String unitCrossId : crossArray) {
					// List<CrossTrainInfo> listTrainInfo =
					// crossService.getCrossTrainInfoForCrossid(unitCrossId);

					try {
						List<CrossTrainInfo> listTrainInfo = crossService
								.getUnitCrossTrainInfoForUnitCrossId(unitCrossId);
						if (listTrainInfo != null && listTrainInfo.size() > 0) {
							List<String> trainNbrs = new ArrayList<String>();
							for (CrossTrainInfo trainInfo : listTrainInfo) {
								String trainNbr = trainInfo.getTrainNbr();
								trainNbrs.add(trainNbr);
							}
							// 调用后台接口
							String response = remoteService.updateUnitCrossId(
									baseChartId, unitCrossId, trainNbrs);

							if (response
									.equals(Constants.REMOTE_SERVICE_SUCCESS)) {
								// 调用后台接口成功，更新本地数据表unit_cross中字段CREAT_CROSS_TIME
								List<String> unitCrossIdList = new ArrayList<String>();
								unitCrossIdList.add(unitCrossId);
								crossService
										.updateUnitCrossUnitCreateTime(unitCrossIdList);
								JSONObject subResult = new JSONObject();
								subResult.put("unitCrossId", unitCrossId);
								subResult.put("flag", 1);
								resultArr.add(subResult);
							}
						}
					} catch (Exception e) {
						logger.error("updateUnitCrossId:[" + unitCrossId
								+ "]error==" + e.getMessage());
					}
				}
				result.setData(resultArr);
			}
		} catch (Exception e) {
			logger.error("updateUnitCrossId error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 获取车底交路信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnitCrossInfo", method = RequestMethod.POST)
	public Result getUnitCrossInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		List<SubCrossInfo> list = null;
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if (user.getBureau() != null) {
				reqMap.put("currentBureau", user.getBureau());
			}
			
			String all_relevant_token = StringUtil.objToStr(reqMap.get("all_relevant_token"));
			if("1".equals(all_relevant_token)){
				//1:本局担当 
				reqMap.put("tokenVehBureau", user.getBureau());
				reqMap.put("relevantBureau", null);
			} else if("2".equals(all_relevant_token)){
				//2:本局相关
				reqMap.put("tokenVehBureau", null);
				reqMap.put("relevantBureau", user.getBureau());
			} else {
				//others:全部，按照原有的的查询条件进行查询
//				reqMap.put("relevantBureau", null);
//				reqMap.put("tokenVehBureau", null);
			}

			list = crossService.getUnitCrossInfo(reqMap);
			PagingResult page = new PagingResult(
					crossService.getUnitCrossInfoCount(reqMap), list);
			result.setData(page);
		} catch (Exception e) {
			logger.error("getUnitCrossInfo error==" + e.getMessage());
			e.printStackTrace();
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 获取车底交路信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossInfo", method = RequestMethod.POST)
	public Result getCrossInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		List<CrossInfo> list = null;
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if (user.getBureau() != null) {
				reqMap.put("currentBureau", user.getBureau());
			}

			list = crossService.getCrossInfo(reqMap);
			PagingResult page = new PagingResult(
					crossService.getCrossInfoCount(reqMap), list);
			result.setData(page);
		} catch (Exception e) {
			logger.error("getCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 获取基本交路单元信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnitCrossTrainInfo", method = RequestMethod.POST)
	public Result getUnitCrossTrainInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		String unitCrossId = (String) reqMap.get("unitCrossId");
		try {
			// 先获取unitcross基本信息
			CrossInfo crossinfo = crossService
					.getUnitCrossInfoForUnitCrossid(unitCrossId);
			// 再获取unitcrosstrainInfo信息
			List<CrossTrainInfo> list = crossService
					.getUnitCrossTrainInfoForUnitCrossId(unitCrossId);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			Map<String, Object> dataMap = new HashMap<String, Object>();

			if (crossinfo.getHighlineFlag().length() > 1) {
				crossinfo.setHighlineFlag("2");
			}
			
			OriginalCross ocross = originalCrossService.getOriginalCrossInfoById(crossinfo.getCrossId());
			if (ocross.getHighlineFlag().length() > 1) {
				ocross.setHighlineFlag("2");
			}
			
			ocross.setUnitCrossId(crossinfo.getUnitCrossId());
			dataMap.put("crossinfo", crossinfo);
			dataMap.put("oCrossinfo", ocross);
			dataMap.put("unitCrossTrainInfo", list);
			dataList.add(dataMap);
			result.setData(dataList);
		} catch (Exception e) {
			logger.error("getUnitCrossTrainInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 获取车底交路信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCrossTrainInfo", method = RequestMethod.POST)
	public Result getCrossTrainInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		String crossId = (String) reqMap.get("crossId");
		logger.debug("crossId==" + crossId);
		try {
			// 先获取cross基本信息
			CrossInfo crossinfo = crossService.getCrossInfoForCrossid(crossId);
			// 再获取crosstrainInfo信息
			List<CrossTrainInfo> list = crossService
					.getCrossTrainInfoForCrossid(crossId);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			Map<String, Object> dataMap = new HashMap<String, Object>();

			if (crossinfo.getHighlineFlag().length() > 1) {
				crossinfo.setHighlineFlag("2");
			}

			dataMap.put("crossInfo", crossinfo);
			dataMap.put("crossTrainInfo", list);
			dataList.add(dataMap);
			result.setData(dataList);
		} catch (Exception e) {
			logger.error("getCrossTrainInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 获取planCross信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanCrossInfo", method = RequestMethod.POST)
	public Result getPlanCrossInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String planCrossId = StringUtil.objToStr(reqMap.get("planCrossId"));
			PlanCrossInfo planCrossInfo = crossService
					.getPlanCrossInfoForPlanCrossId(planCrossId);

			if (planCrossInfo.getHighlineFlag().length() > 1) {
				planCrossInfo.setHighlineFlag("2");
			}

			result.setData(planCrossInfo);
		} catch (Exception e) {
			logger.error("getPlanCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 生成基本交路单元
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/completeUnitCrossInfo", method = RequestMethod.POST)
	public Result completeUnitCrossInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		String message = "";
		logger.info("reqmap==" + reqMap);
		// 增加一个验证模块，验证交路单元是否已经审核过

		String crossId = StringUtil.objToStr(reqMap.get("crossIds"));
		logger.info("crossId==" + crossId);
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();

		if (crossId != null) {
			String[] crossIds = crossId.split(",");

			try {
				if (crossService.getCountfromBaseCrossNonCheck(crossIds,
						user.getBureau()) > 0) {
					throw new ParamValidationException(
							TPResponseCode.GENERATE_UNITCROSS_ERROR.getCode(),
							"存在未审核交路，不能生成交路单元");
				}
			} catch (ParamValidationException e) {
				logger.error("completeUnitCrossInfo error==" + e.getErrorMsg());
				result.setCode(e.getErrorCode());
				result.setMessage(e.getErrorMsg());
				return result;
			} catch (Exception e) {
				logger.error("completeUnitCrossInfo error==" + e.getMessage());
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
				return result;
			}

			try {
				String createedCrossIds = "";
				String createedCrossNames = "";
				String chartIds = "";
				int errCount = 0;
				int updCount = 0;
				List<String> crossIdsList = new ArrayList<String>();
				JSONArray resultArr = new JSONArray();
				JSONArray crosses = JSONArray.fromObject(reqMap
						.get("Objcrosses"));

				// deleteUnitCrossAndCrossTrain()
				List<CrossInfo> unitCross = null;
				boolean flag = false;
				for (int k = 0; k < crosses.size(); k++) {
					JSONObject crossRow = JSONObject.fromObject(crosses.get(k));
					String unitCreateFlag = crossRow
							.getString("unitCreateFlag2");
					String crossId_ = crossRow.getString("crossId");
					String crossName = crossRow.getString("crossName2");
					String chartId = crossRow.getString("chartId2");

					// 可能是同名的两个交路，但是如果重新导入了对数表，那么交路的crossid 也发生了变化
					unitCross = crossService
							.getUnitCrossInfosForCrossNameAndChartId(crossName,
									chartId);
					// 如果unitCreateFlag==1 说明是已经生成过的，crossid以逗号分隔
					// if ("1".equals(unitCreateFlag)) {
					// 这里有可能 是同一个局本身有两个相同交路名的交路
					// int unitCrossSize = unitCross.size();
					// CrossInfo newCross =
					// crossService.getCrossInfoForCrossid(crossId_);
					// CrossInfo oldCross = null;
					// for (CrossInfo crossInfo : unitCross) {
					// oldCross =
					// crossService.getCrossInfoForCrossid(crossInfo.getBaseCrossId());
					// if(oldCross!=null){
					// String alterNateDate = newCross.getAlterNateDate();
					// if(alterNateDate!=null){
					// if(!alterNateDate.equals(oldCross.getAlterNateDate())){//如果这两个交路的交替日期不一样，那么说明不是重复交路，是正常的两个交路
					// unitCrossSize--;
					// }
					// }
					// }
					// }
					if (unitCross.size() > 0) {
						if (errCount > 0) {
							createedCrossIds = createedCrossIds + ",";
							createedCrossNames = createedCrossNames + ",";
							chartIds = chartIds + ",";
						}
						createedCrossIds = createedCrossIds + crossId_;
						createedCrossNames = createedCrossNames + crossName;
						chartIds = chartIds + chartId;
						errCount++;
						continue;
					}
					// 生成交路单元
					JSONObject obj = new JSONObject();
					crossService.completeUnitCrossInfo(crossId_);
					updCount++;
					crossIdsList.add(crossId_);
					obj.put("crossId", crossId_);
					obj.put("flag", 1);
					resultArr.add(obj);

				}

				// 生成交路单元完成后，更改表base_cross中的creat_unit_time字段的值
				if (crossIdsList.size() > 0) {
					crossService.updateCrossUnitCreateTime(crossIdsList);
				}
				if (errCount > 0) {
					result.setCode("creatUnitWarm");// 显示框
					StringBuilder sb = new StringBuilder();
					//
					// boolean b1 = false;
					// StringBuilder sb1 = new StringBuilder();
					// sb1.append("选中交路中有部分数据已生成交路单元：");
					// sb1.append("\n");
					//
					// boolean b2 = false;
					// StringBuilder sb2 = new StringBuilder();
					// sb2.append("交路车次在基本图找不到相应数据：");
					// sb2.append("\n");
					//
					//
					// sb.append("生成失败的交路共");
					// sb.append(errCount);
					// sb.append("条，原因如下：");
					// sb.append("\n");
					// sb.append("\n");
					// if (b1) {
					// sb.append("一、").append(sb1).append("\n");
					// if (b2) {
					// sb.append("二、").append(sb2);
					// }
					// } else if (b2) {
					// sb.append("一、").append(sb2);
					// }
					// // sb.append(sb1).append("\n").append(sb2);
					// StringBuilder sbUpd = new StringBuilder();
					if (updCount > 0) {
						// 虽然失败，但是还是有成功的
						// sbUpd.append("审核成功的交路共");
						// sbUpd.append(updCount);
						// sbUpd.append("条。");
						// sbUpd.append("\n");
						// sbUpd.append("\n");
						// result.setMessage(sbUpd.append(sb).toString());
					} else {
						result.setMessage(sb.toString());
					}
					result.setData(chartIds + "__" + createedCrossNames + "__"
							+ createedCrossIds);
					result.setData1(resultArr);
				} else {
					result.setData(resultArr);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("completeUnitCrossInfo error==" + e.getMessage());
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
			}
		}
		return result;
	}

	// repeatCross

	/**
	 * 生成交路单元 重复的 在新的iframe 显示
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/repeatCross", method = RequestMethod.GET)
	public ModelAndView repeatCross(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView result = new ModelAndView("cross/crossRepeat/crossRepeat");
		// 参照crossController 355/
		try {
			String chartIdsAndNames = request.getParameter("chartIdsAndNames");
			String message = request.getParameter("message");
			logger.info("repeatCross~~crossIdsAndName==" + chartIdsAndNames);
			logger.info("repeatCross~~message==" + message);
			String chartIds = chartIdsAndNames.split("__")[0];
			String crossNames = chartIdsAndNames.split("__")[1];
			String crossIds = chartIdsAndNames.split("__")[2];
			request.setAttribute("chartIds", chartIds);
			request.setAttribute("crossNames", crossNames);
			request.setAttribute("crossIds", crossIds);
			request.setAttribute("message", message);
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
	 * 生成重复的交路单元, 可能是同名的两个交路，但是如果重新导入了对数表，那么交路的crossid 也发生了变化
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/repeatCreateUnitCross", method = RequestMethod.GET)
	public Result repeatCreateUnitCross(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		// 参照crossController 355/
		try {
			String message = "";
			// 增加一个验证模块，验证交路单元是否已经审核过

			String chartIdsStr = request.getParameter("chartIds");
			String crossNamesStr = request.getParameter("crossNames");
			String crossIdsStr = request.getParameter("crossIds");
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			List<String> crossIdsList = new ArrayList<String>();
			JSONArray resultArr = new JSONArray();
			if (chartIdsStr != null) {
				String[] chartIds = chartIdsStr.split(",");
				String[] crossNames = crossNamesStr.split(",");
				String[] crossIds = crossIdsStr.split(",");

				try {
					if (crossService.getCountfromBaseCrossNonCheck(crossIds,
							user.getBureau()) > 0) {
						throw new ParamValidationException(
								TPResponseCode.GENERATE_UNITCROSS_ERROR
										.getCode(),
								"存在未审核交路，不能生成交路单元");
					}
				} catch (ParamValidationException e) {
					logger.error("completeUnitCrossInfo error=="
							+ e.getErrorMsg());
					result.setCode(e.getErrorCode());
					result.setMessage(e.getErrorMsg());
					return result;
				} catch (Exception e) {
					logger.error("completeUnitCrossInfo error=="
							+ e.getMessage());
					result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
					result.setMessage(StaticCodeType.SYSTEM_ERROR
							.getDescription());
					return result;
				}
				// 生成交路单元
				// for (String crossId_ : crossIds) {
				for (int i = 0; i < crossIds.length; i++) {

					JSONObject obj = new JSONObject();
					crossService.insertRepeatUnitCrossInfo(chartIds[i],
							crossNames[i], crossIds[i]);
					crossIdsList.add(crossIds[i]);
					obj.put("crossId", crossIds[i]);
					obj.put("flag", 1);
					resultArr.add(obj);
				}
				// 生成交路单元完成后，更改表base_cross中的creat_unit_time字段的值
				if (crossIdsList.size() > 0) {
					crossService.updateCrossUnitCreateTime(crossIdsList);
				}
				result.setData(resultArr);
				logger.info("resultArr==" + resultArr);
			}
		} catch (Exception e) {
		}
		return result;

	}

	@ResponseBody
	@RequestMapping(value = "/getTrainPeriod", method = RequestMethod.POST)
	public Result getTrainActivityPeriod(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String trainNbr = StringUtil.objToStr(reqMap.get("trainNbr"));
			String chartName = StringUtil.objToStr(reqMap.get("chartName"));
			String periodSourceTime = StringUtil.objToStr(reqMap
					.get("periodSourceTime"));
			String periodTargetTime = StringUtil.objToStr(reqMap
					.get("periodTargetTime"));
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("trainNbr", trainNbr);
			map.put("chartName", chartName);
			if (trainNbr != null) {
				List<TrainActivityPeriod> periods = crossService
						.getTrainActivityPeriod(map);
				List<TrainActivityPeriod> returnPeriods = new ArrayList<TrainActivityPeriod>();
				for (int i = 0; i < periods.size(); i++) {
					if (StringUtils.equals(periodSourceTime, periods.get(i)
							.getSourceTime())
							&& StringUtils.equals(periodTargetTime, periods
									.get(i).getTargetTime())) {
						returnPeriods.add(periods.get(i));
						periods.remove(i);
					}
				}
				returnPeriods.addAll(periods);
				result.setData(returnPeriods);

				// HashSet<String> hs = new HashSet<String>();
				// for (int i = 0; i < periods.size(); i++) {
				// if(!hs.add(periods.get(i).getBaseTrainId())){
				// periods.remove(i);
				// i = i-1;
				// }
				// }
				// result.setData(periods);
			}
		} catch (Exception e) {
			logger.error("getTrainActivityPeriod error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/updateTrainPeriod", method = RequestMethod.POST)
	public Result updateTrainPeriod(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			/** 2015-4-28 10:54:28 S **/
			// // String baseTrainId =
			// // StringUtil.objToStr(reqMap.get("baseTrainId"));
			// // String sourceTime =
			// // StringUtil.objToStr(reqMap.get("sourceTime"));
			// // String targetTime =
			// // StringUtil.objToStr(reqMap.get("targetTime"));
			// // String baseCrossTrainId =
			// // StringUtil.objToStr(reqMap.get("baseCrossTrainId"));
			//
			// crossService.deleteUnitCrossByBaseCrossId(reqMap);
			// crossService.deleteUnitCrossTrainByBaseCrossId(reqMap);
			//
			// BaseTrainInfo baseTrainInfo = crossService
			// .getBaseTrainInfoOneByParam(reqMap);
			// System.out.println(baseTrainInfo.getBaseTrainId());
			// // 这一步，根据baseTrainId,查询出基本图列车，然后全面更新baseCrossTrain的数据
			// // reqMap.put("startBureau", baseTrainInfo.getStartBureau());
			// // reqMap.put("startBureauShortName",
			// // baseTrainInfo.getStartBureauShortName());
			// reqMap.put("startStn", baseTrainInfo.getStartStn());
			// // reqMap.put("endBureau", baseTrainInfo.getEndBureau());
			// // reqMap.put("endBureanShortName",
			// // baseTrainInfo.getEndBureanShortName());
			// reqMap.put("endStn", baseTrainInfo.getEndStn());
			// // reqMap.put("routeBureauShortNames",
			// // baseTrainInfo.getRouteBureauShortNames());
			// // reqMap.put("startTime", baseTrainInfo.getStartTime());
			// // reqMap.put("endTime", baseTrainInfo.getEndTime());
			// // reqMap.put("runDays", baseTrainInfo.getRunDays());
			// crossService.updateBaseCrossTrainPeriod(reqMap);
			//
			// // 更新baseCross
			// crossService.updateBaseCrossByBaseCrossId(reqMap);
			/** --------------------------E-------------------------- **/

			// 修改当前车次，以及当前车次之后的全部车次数据.
			// 例如：1-2-3-4；现在选择了第2个车，进行有效期调整，那么就需要将：2-3-4全部重新计算.
			Map<String, Object> updBaseCrossTrainMap = new HashMap<String, Object>();
			Map<String, String> selMap = new HashMap<String, String>();
			if (StringUtils.isNotEmpty(MapUtils
					.getString(reqMap, "baseCrossId"))) {
				selMap.put("base_cross_id",
						MapUtils.getString(reqMap, "baseCrossId"));
			}
			if (StringUtils.isNotEmpty(MapUtils.getString(reqMap,
					"baseCrossTrainId"))) {
				selMap.put("base_cross_train_id",
						MapUtils.getString(reqMap, "baseCrossTrainId"));
			}

			// 查询当前选择的有效期数据.
			BaseTrainInfo baseTrainInfo = crossService
					.getBaseTrainInfoOneByParam(reqMap);
			// 需要修改的数据
			List<CrossTrainInfo> crossTrainInfoList = crossService
					.getBaseCrossTrainObjByMap(selMap);
			List<Map<String, String>> baseCrossLm = crossService
					.getBaseCrossByMap(selMap);
			Integer dayGapForCross = 0;
			Integer updI = 0;
			if (!crossTrainInfoList.isEmpty() && !baseCrossLm.isEmpty()) {
				// 是否选择的是：第一行数据
				boolean b = false;
				Integer bI = 0;
				for (int i = 0; i < crossTrainInfoList.size(); i++) {
					CrossTrainInfo crossTrainInfo = crossTrainInfoList.get(i);
					if (StringUtils.equals(
							MapUtils.getString(reqMap, "baseCrossTrainId"),
							crossTrainInfo.getBaseCrossTrainId())) {
						// 用户选择的数据
						crossTrainInfo.setSourceTargetTime(baseTrainInfo
								.getStartTime());
						crossTrainInfo
								.setTargetTime(baseTrainInfo.getEndTime());
						crossTrainInfo.setBaseTrainId(baseTrainInfo
								.getBaseTrainId());
						crossTrainInfo.setStartStn(baseTrainInfo.getStartStn());
						crossTrainInfo.setStartBureau(LjUtil.getLjByName(
								baseTrainInfo.getStartBureauShortName(), 2));
						crossTrainInfo.setEndStn(baseTrainInfo.getEndStn());
						crossTrainInfo.setEndBureau(LjUtil.getLjByName(
								baseTrainInfo.getEndBureanShortName(), 2));
						crossTrainInfo.setPeriodSourceTime(MapUtils.getString(
								reqMap, "sourceTime"));
						crossTrainInfo.setPeriodTargetTime(MapUtils.getString(
								reqMap, "targetTime"));
						b = true;
					}

					if (b) {
						crossTrainInfo.setPeriodSourceTime(MapUtils.getString(
								reqMap, "sourceTime"));
						crossTrainInfo.setPeriodTargetTime(MapUtils.getString(
								reqMap, "targetTime"));
						// 第一个车不需要计算，因为如果选中的是第一个车，那第一个车的数据就是用户填写的；选择的不是第一个车，那第一个车就不需要变
						if (i != 0) {
							bI++;
							// 加载始发、终到、以及时间
							crossService.dealWithStartAndEndStn(crossTrainInfo,
									baseCrossLm.get(0).get("BASE_CHART_ID"),
									LjUtil.getLjByNameBs(crossTrainInfoList
											.get(i - 1).getEndBureau(), 2),
									crossTrainInfoList.get(i - 1).getEndStn(),
									baseTrainInfo.getBaseTrainId(), bI);
						}
						// 间隔日期
						crossService.dealWithDayGap(crossTrainInfo,
								crossTrainInfoList
										.get(i - 1 < 0 ? crossTrainInfoList
												.size() - 1 : i - 1));
					}
					// 开行日期
					dayGapForCross = crossService.dealWithRunDateAndEndDate(
							crossTrainInfo,
							baseCrossLm.get(0).get("CROSS_START_DATE"),
							dayGapForCross, i);
					if (b) {
						// upd,在b=false时，原数据实际并没有改变，只会在b=true，证明重新计算过间隔日期，那么原数据就需要改变
						updBaseCrossTrainMap.put("crossTrainInfo",
								crossTrainInfo);
						updI += crossService
								.updBaseCrossTrainByObj(updBaseCrossTrainMap);
						updBaseCrossTrainMap.clear();
					}
				}

				// 主表，结束时间重新计算得到.
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				Date date = dateFormat.parse(baseCrossLm.get(0).get(
						"CROSS_START_DATE"));
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, dayGapForCross);
				reqMap.put("crossEndDate",
						dateFormat.format(calendar.getTime()));

			} else {
				// return err
				result.setCode(StaticCodeType.SYSTEM_DATA_ISNULL.getCode());
				result.setMessage(StaticCodeType.SYSTEM_DATA_ISNULL
						.getDescription());
			}
			if (updI > 0) {
				// del 交路单元.
				crossService.deleteUnitCrossByBaseCrossId(reqMap);
				crossService.deleteUnitCrossTrainByBaseCrossId(reqMap);
				// 将审核时间、生成交路单元时间 置空.
				// 2015-4-29 11:17:58，和哲哥确认后，主表暂时不需要修改其他信息
				crossService.updateBaseCrossByBaseCrossId(reqMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	// 获取最小开始时间
	private String getStartDateMin(String startTimeTemp, String minDate,
			String date) {
		String startTime = minDate;
		String parseDate = DateUtil.parseDateTOyyyymmdd(date);
		if (minDate != null && parseDate != null && !"".equals(minDate)
				&& !"".equals(parseDate)) {
			startTime = DateUtil.isBefore(minDate, parseDate) ? minDate
					: parseDate;
		} else {
			startTime = minDate;
		}
		return DateUtil.isBefore(startTime, startTimeTemp) ? startTimeTemp
				: startTime;
	}

	// 获取最大结束时间
	private String getEndDateMax(String maxDate, String date) {
		String parseDate = DateUtil.parseDateTOyyyymmdd(date);
		if (maxDate != null && parseDate != null && !"".equals(maxDate)
				&& !"".equals(parseDate)) {
			return DateUtil.isBefore(parseDate, maxDate) ? maxDate : parseDate;
		} else
			return maxDate;
	}

	/**
	 * 校验.
	 * 
	 * @param lm
	 *            查询出来的数据：List<Map<String, String>>，并且要已经判断过是否为空.
	 * @return
	 */
	private Result checkCross(List<Map<String, String>> lm) {
		Result result = new Result();
		String oldStartStn = "";
		String scStartStn = "";
		for (int i = 0; i < lm.size(); i++) {
			Map<String, String> m = lm.get(i);		
			if (CheckUtil.checkStartEndStnIsNotNull(
					MapUtils.getString(m, "START_STN"),
					MapUtils.getString(m, "END_STN"))) {
				if (i == 0) {
					oldStartStn = MapUtils.getString(m, "END_STN");
					scStartStn = MapUtils.getString(m, "START_STN");
				} else {
					if (CheckUtil.checkStartEndStnIsEquals(oldStartStn,
							MapUtils.getString(m, "START_STN"))) {
						oldStartStn = MapUtils.getString(m, "END_STN");
					} else {
						result.setCode("2");
						result.setMessage(MapUtils.getString(m, "CROSS_NAME")
								+ "\n");
						return result;
					}
					if (i == (lm.size() - 1)) {
						if (!StringUtils.equals(scStartStn,
								MapUtils.getString(m, "END_STN"))) {
							result.setCode("3");
							result.setMessage(MapUtils.getString(m,
									"CROSS_NAME") + "\n");
							return result;
						}
					}
				}
			} else {
				result.setCode("1");
				result.setMessage(MapUtils.getString(m, "CROSS_NAME") + "\n");
				return result;
			}
			if(!MapUtils.getString(m, "GROUP_TOTAL_NBR").matches("^[1-9]\\d*$")){
				result.setCode("4");
				result.setMessage(MapUtils.getString(m, "CROSS_NAME") + "\n");
				return result;
			}
			if(MapUtils.getString(m, "APPOINT_WEEK") != null){
				if(!MapUtils.getString(m, "APPOINT_WEEK").matches("^[0-1]{7}$")){
					result.setCode("5");
					result.setMessage(MapUtils.getString(m, "CROSS_NAME") + "\n");
					return result;
				}else{
					if(!MapUtils.getString(m, "APPOINT_WEEK").contains("01") && !MapUtils.getString(m, "APPOINT_WEEK").contains("10")){
						result.setCode("5");
						result.setMessage(MapUtils.getString(m, "CROSS_NAME") + "\n");
						return result;
					}
				}			
			}			
		}
		
		return result;
	}

}
