package com.railway.common.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.SortUtil;
import org.railway.com.trainplan.common.utils.Station;
import org.railway.com.trainplan.common.utils.StattionUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfo;
import org.railway.com.trainplan.entity.BaseCrossTrainInfoTime;
import org.railway.com.trainplan.entity.BaseCrossTrainSubInfo;
import org.railway.com.trainplan.entity.CrossDictInfo;
import org.railway.com.trainplan.entity.CrossDictStnInfo;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.service.CrossDictService;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.web.dto.CrossRelationDto;
import org.railway.com.trainplan.web.dto.PlanLineGrid;
import org.railway.com.trainplan.web.dto.PlanLineGridX;
import org.railway.com.trainplan.web.dto.PlanLineGridY;
import org.railway.com.trainplan.web.dto.PlanLineSTNDto;
import org.railway.com.trainplan.web.dto.TrainInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railway.common.service.impl.TcmSchemeService;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.ComfirmedmapServiceImpl;

import commonToolKit.Sort.Sort;

@RestController
@RequestMapping(value = "/drawCross")
public class DrawGraphController {
	
	private static Log logger = LogFactory.getLog(DrawGraphController.class
			.getName());
	
	
	@Autowired
	private CrossService crossService;
	@Autowired
	private TcmSchemeService schemeService;


	@Autowired
	private CrossDictService crossDictService;
	@Autowired
	private ComfirmedmapServiceImpl comfirmedmapServiceImpl;
	/**
	 * 提供画交路图形的数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/provideCrossChartData", method = RequestMethod.GET)
	public ModelAndView provideCrossChartData(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView result = new ModelAndView("logTable/crossGraph");

		String crossId = StringUtil.objToStr(request.getParameter("crossId"));
		String chartId = StringUtil.objToStr(request.getParameter("chartId"));
		String crossName = StringUtil.objToStr(request.getParameter("crossName"));
//		String crossId = StringUtil.objToStr(reqMap.get("crossId"));
//		String chartId = StringUtil.objToStr(reqMap.get("chartId"));
//		
//		String crossName = StringUtil.objToStr(reqMap.get("crossName"));
		logger.debug("chartId==" + chartId);

		String drawGraphIdAllIn = "";// 用于调整站序
		String isCurrentBureau = "0";// 用于调整站序,0 不是 1 是
		// 是否本局担当
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossName", crossName);
		// 经由信息，由后面调用接口获取，用户提供画图的坐标
		PlanLineGrid grid = null;
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
			
			for (int i = 0; i < list.size(); i++) {
				List<BaseCrossTrainSubInfo> tl = list.get(i).getTrainList();
				list.get(i).setCrossStartDate(DateUtil.parseDateTOyyyymmdd(tl.get(0).getStartTime()));
				list.get(i).setCrossEndDate(DateUtil.parseDateTOyyyymmdd(tl.get(tl.size()-1).getEndTime()));
			}
			
			// 只有一条记录
			if (list != null && list.size() > 0) {
				BaseCrossTrainInfo trainInfo = null;
				// 列车信息
				
				int sizeStation = list.size();
				List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
				
				for (int m = 0; m < list.size(); m++) {
					List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
					Map<String, Object> crossMap = new HashMap<String, Object>();
					
					trainInfo = list.get(m);	
						
					String groupSerialNbr = "";
					List<BaseCrossTrainSubInfo> trainList = trainInfo
							.getTrainList();
					if (trainList != null && trainList.size() > 0) {
	
						for (int i = 0; i < trainList.size(); i++) {
	
							// 列车信息
							BaseCrossTrainSubInfo subInfo = trainList.get(i);
							groupSerialNbr = subInfo.getGroupSn();
							TrainInfoDto dto = new TrainInfoDto();
							dto.setTrainName(subInfo.getTrainNbr());
							dto.setGroupSerialNbr(subInfo.getGroupSn());
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
					crossMap.put("groupSerialNbr", groupSerialNbr);
					crossMap.put("crossName", trainInfo.getCrossName());
					dataList.add(crossMap);
				}
				/******** fortest *********/
				/*******************/
				 isStationFromDic = false;
				if (isStationFromDic) {
					List<PlanLineGridY> listGridY = getPlanLineGridY(stationList);
					List<PlanLineGridX> listGridX = getPlanLineGridX(
							trainInfo.getCrossStartDate(),
							trainInfo.getCrossEndDate());
					grid = new PlanLineGrid(listGridX, listGridY);
				} else {
					// liuhang
					
					List<LinkedList<Station>> list2 = new ArrayList<LinkedList<Station>>();
					String minStartDate = "2500-01-01";
					String maxEndDate = "1900-01-01";
					for(int n= 0;n<dataList.size();n++){
						Map<String, Object> crossmap = dataList.get(n);
						List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
								.get("trains");
						for (int k = 0; k < trains.size(); k++) {
							// 取交路中的第一辆车的经由站信息
							TrainInfoDto dto = trains.get(k);
							minStartDate = getMinStartDate(minStartDate,dto);
							maxEndDate = getMaxEndDate(maxEndDate,dto);
							LinkedList<Station> listStation1 = new LinkedList<Station>();
	
							List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
							for (PlanLineSTNDto trainStn : listTrainStn) {
								Station station = new Station();
								station.setStnName(trainStn.getStnName());
								station.setStationType(trainStn.getStationType());
								station.setDptTime(trainStn.getDptTime());
								listStation1.add(station);
							}
							list2.add(listStation1);
							
							minStartDate = getMinStartDate(minStartDate, dto);
							maxEndDate = getMaxEndDate(maxEndDate, dto);
						}
					}
					grid = getPlanLineGridForAll(list2,minStartDate.split(" ")[0],maxEndDate.split(" ")[0]);

					// 查询有关baseCross的信息
//					CrossInfo crossInfo = crossService
//							.getCrossInfoForCrossid(crossId);
					List<SchemeInfo> schemeList = null;
//							schemeService.getschemeByCrossId(crossId);
					ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
							.getSubject().getPrincipal();
					// 保存到数据库
					// 先保存表draw_graph的数据
					CrossDictInfo dicInfo = new CrossDictInfo();
					// String drawGrapId = UUID.randomUUID().toString();
					//TODO需要保存到数据drawgraph中
					String drawGrapId = crossName + "_" + chartId;
					drawGraphIdAllIn = drawGrapId;// 修改站序
					// 防止重复生成画图数据
					dicInfo.setDrawGrapId(drawGrapId);
					dicInfo.setBaseChartId("");
					dicInfo.setBaseChartName("");
					//dicInfo.setBaseChartId(schemeList.get(0).getSchemeId());
					//dicInfo.setBaseChartName(schemeList.get(0).getSchemeName());
					dicInfo.setCrossName(crossName);
					dicInfo.setBaseCrossId(crossId);
					dicInfo.setCheckPeople(user.getName());
					dicInfo.setCheckPeopleOrg(user.getDeptName());
					dicInfo.setCreatePeople(user.getName());
					dicInfo.setCreatePeopleOrg(user.getDeptName());
					// 保存数据
					try {
//						 保存数据
						int countDic = crossDictService
								.addCrossDictInfo(dicInfo);
						logger.debug("countDic===" + countDic);
					} catch (Exception e) {
						// suntao 说明存失败,这里的失败，是违反主键唯一原则
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
						return result;
						
						
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
	
	
	/**
	 * 但当局 内存交路预览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/orginalCrossChartData", method = RequestMethod.GET)
	public ModelAndView orginalCrossChartData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = new ModelAndView("cross/cross_canvas_look"); 
		
		
		String orginalCrossId = StringUtil.objToStr(request.getParameter("orginalCrossId"));
		
		List<BaseCrossTrainInfo> baseCrossTrainInfo = comfirmedmapServiceImpl.getBaseCrossTrainInfo(orginalCrossId);
		for (int i = 0; i < baseCrossTrainInfo.size(); i++) {
			baseCrossTrainInfo.get(i).setCrossName("K208/5-K206/7");
			List<BaseCrossTrainSubInfo> tl = baseCrossTrainInfo.get(i).getTrainList();
			baseCrossTrainInfo.get(i).setCrossStartDate(DateUtil.parseDateTOyyyymmdd(tl.get(0).getStartTime()));
			baseCrossTrainInfo.get(i).setCrossEndDate(DateUtil.parseDateTOyyyymmdd(tl.get(tl.size()-1).getEndTime()));
		}
		
		String crossName= null;
		String chartId= null;
		
		
		
		Map<String,Object> resultMap = getJsonDataForView(baseCrossTrainInfo,crossName,chartId);
		
		result.addObject("myJlData", resultMap.get("myJlData"));
		logger.info("myJlData==" + resultMap.get("myJlData"));
		// System.err.println("myJlData==" + myJlData);
		logger.info("gridStr==" + resultMap.get("gridData"));
		// System.err.println("gridStr==" + gridStr);
		result.addObject("gridData", resultMap.get("gridData"));
		return result;
	}
	
	
	/**
	 * 经由局 内存交路预览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/partOrginalCrossChartData", method = RequestMethod.GET)
	public ModelAndView partOrginalCross(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = new ModelAndView("cross/cross_canvas"); 
		
		
		String partOrginalCrossId = StringUtil.objToStr(request.getParameter("partOrginalCrossId"));
		
		
		List<BaseCrossTrainInfo> list = null;
		String crossName= null;
		String chartId= null;
		
		
		
		Map<String,Object> resultMap = getJsonDataForView(list,crossName,chartId);
		
		result.addObject("myJlData", resultMap.get("myJlData"));
		logger.info("myJlData==" + resultMap.get("myJlData"));
		// System.err.println("myJlData==" + myJlData);
		logger.info("gridStr==" + resultMap.get("gridData"));
		// System.err.println("gridStr==" + gridStr);
		result.addObject("gridData", resultMap.get("gridData"));
		return result;
	}
	
	/**
	 * 提供给对数表预览的交路图接口
	 * @throws Exception 
	 */
	private Map<String,Object> getJsonDataForView(List<BaseCrossTrainInfo> list,String crossName,String chartId) throws Exception{

		Map<String,Object> mapresult = new HashMap<String, Object>();
		
		String drawGraphIdAllIn = "";// 用于调整站序
		String isCurrentBureau = "0";// 用于调整站序,0 不是 1 是
		// 是否本局担当
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossName", crossName);
		// 经由信息，由后面调用接口获取，用户提供画图的坐标
		PlanLineGrid grid = null;
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 先查看表draw_graph中是否有该交路的信息
		// 使用方案名和交路名来确定
//		List<CrossDictStnInfo> listDicStn = crossDictService
//				.getCrossDictStnForChartId(crossName, chartId);
		// 用于纵坐标的列车list
		List<Station> stationList = new LinkedList<Station>();
		// 标识纵坐标的站点信息是否来源于字典
//		boolean isStationFromDic = false;
//		if (listDicStn != null && listDicStn.size() > 0) {
//			drawGraphIdAllIn = listDicStn.get(0).getDrawGrapId();
//			isStationFromDic = true;
//			for (CrossDictStnInfo dictStnInfo : listDicStn) {
//				Station station = new Station();
//				station.setStnName(dictStnInfo.getStnName());
//				String stationType = dictStnInfo.getStnType();
//				// 车站类型（1:发到站，2:分界口，3:停站,4:不停站）
//				if ("1".equals(stationType)) {
//					stationType = "0";
//				} else if ("4".equals(stationType)) {
//					stationType = "BT";
//				} else if ("3".equals(stationType)) {
//					stationType = "TZ";
//				} else if ("2".equals(stationType)) {
//					stationType = "FJK";
//				} else {
//					stationType = "-1";
//				}
//				station.setStationType(stationType);
//				stationList.add(station);
//			}
//		}
		if (list != null) {
			
			for (int i = 0; i < list.size(); i++) {
				List<BaseCrossTrainSubInfo> tl = list.get(i).getTrainList();
				list.get(i).setCrossStartDate(DateUtil.parseDateTOyyyymmdd(tl.get(0).getStartTime()));
				list.get(i).setCrossEndDate(DateUtil.parseDateTOyyyymmdd(tl.get(tl.size()-1).getEndTime()));
			}
			
			BaseCrossTrainInfo trainInfo = null;
			// 列车信息
			int sizeStation = list.size();
			List<LinkedList<Station>> list1 = new ArrayList<LinkedList<Station>>();
			
			for (int m = 0; m < list.size(); m++) {
				List<TrainInfoDto> trains = new ArrayList<TrainInfoDto>();
				Map<String, Object> crossMap = new HashMap<String, Object>();
				
				trainInfo = list.get(m);	
					
				String groupSerialNbr = "";
				
				List<BaseCrossTrainSubInfo> trainList0 = trainInfo
						.getTrainList();
				
				List<BaseCrossTrainSubInfo> trainList = SortUtil.sortBaseCrossTrainSubInfo(trainList0);
				
				if (trainList != null && trainList.size() > 0) {
					
					for (int i = 0; i < trainList.size(); i++) {

						// 列车信息
						BaseCrossTrainSubInfo subInfo = trainList.get(i);
						groupSerialNbr = subInfo.getGroupSn();
						TrainInfoDto dto = new TrainInfoDto();
						dto.setTrainName(subInfo.getTrainNbr());
						dto.setGroupSerialNbr(subInfo.getGroupSn());
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
								
								
								if("SFZ".equals(trainTime.getStationType()) || "ZDZ".equals(trainTime.getStationType())){
									trainTime.setStationType("0");
								}
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
								if (!stnDto.getStationType().contains("BT")) {
									trainStns.add(stnDto);
									trainsList.add(station);
								}
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
				crossMap.put("groupSerialNbr", groupSerialNbr);
				crossMap.put("crossName", trainInfo.getCrossName());
				dataList.add(crossMap);
			}
			/*******************/
//				 isStationFromDic = false;
//				if (isStationFromDic) {
//					List<PlanLineGridY> listGridY = getPlanLineGridY(stationList);
//					List<PlanLineGridX> listGridX = getPlanLineGridX(
//							trainInfo.getCrossStartDate(),
//							trainInfo.getCrossEndDate());
//					grid = new PlanLineGrid(listGridX, listGridY);
//				} else {
				// liuhang
				
			List<LinkedList<Station>> list2 = new ArrayList<LinkedList<Station>>();
			String minStartDate = "2500-01-01";
			String maxEndDate = "1900-01-01";
			for(int n= 0;n<dataList.size();n++){
				Map<String, Object> crossmap = dataList.get(n);
				List<TrainInfoDto> trains = (List<TrainInfoDto>) crossmap
						.get("trains");
				for (int k = 0; k < trains.size(); k++) {
					// 取交路中的第一辆车的经由站信息
					TrainInfoDto dto = trains.get(k);
					minStartDate = getMinStartDate(minStartDate,dto);
					maxEndDate = getMaxEndDate(maxEndDate,dto);
					LinkedList<Station> listStation1 = new LinkedList<Station>();

					List<PlanLineSTNDto> listTrainStn = dto.getTrainStns();
					for (PlanLineSTNDto trainStn : listTrainStn) {
						Station station = new Station();
						station.setStnName(trainStn.getStnName());
						station.setStationType(trainStn.getStationType());
						station.setDptTime(trainStn.getDptTime());
						listStation1.add(station);
					}
					list2.add(listStation1);
					
					minStartDate = getMinStartDate(minStartDate, dto);
					maxEndDate = getMaxEndDate(maxEndDate, dto);
				}
			}
			grid = getPlanLineGridForAll(list2,minStartDate.split(" ")[0],maxEndDate.split(" ")[0]);

				// 查询有关baseCross的信息
//					CrossInfo crossInfo = crossService
//							.getCrossInfoForCrossid(crossId);
//					ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
//							.getSubject().getPrincipal();
//					// 保存到数据库
//					// 先保存表draw_graph的数据
//					CrossDictInfo dicInfo = new CrossDictInfo();
//					// String drawGrapId = UUID.randomUUID().toString();
//					String drawGrapId = crossName + "_" + chartId;
//					drawGraphIdAllIn = drawGrapId;// 修改站序
//					// 保存数据
//					List<CrossDictStnInfo> listDictStn = new ArrayList<CrossDictStnInfo>();
//					// 获取纵坐标信息
//					List<PlanLineGridY> listGridY = grid.getCrossStns();
//					int height = 0;
//					int heightSimple = 0;
//					int stnsort = 0;
//					for (int i = 0; i < listGridY.size(); i++) {
//						PlanLineGridY gridY = listGridY.get(i);
//						String stationType = gridY.getStationType();
//						if (!"BT".equals(stationType)) {
//							// 不存不停的站
//							CrossDictStnInfo crossStnInfo = new CrossDictStnInfo();
//							crossStnInfo.setDrawGrapId(drawGrapId);
//							crossStnInfo.setDrawGrapStnId(UUID.randomUUID()
//									.toString());
//							// TODO 所属局简称
//							crossStnInfo.setBureau("");
//							// 默认每个站的间隔为100
//							height = height + 100;
//							crossStnInfo
//									.setHeightDetail(String.valueOf(height));
//							crossStnInfo.setStnName(gridY.getStnName());
//							crossStnInfo.setStnSort(stnsort++);
//
//							// 车站类型（1:发到站，2:分界口，3:停站）
//							if ("0".equals(stationType)) {
//								stationType = "1";
//							} else if ("TZ".equals(stationType)) {
//								stationType = "3";
//							} else if ("FJK".equals(stationType)) {
//								stationType = "2";
//							}
//							crossStnInfo.setStnType(stationType);
//
//							if (i == 0 && "1".equals(stationType)) {
//								crossStnInfo.setHeightSimple(String.valueOf(0));
//							} else if (i != 0 && "1".equals(stationType)) {
//								heightSimple = heightSimple + 100;
//								crossStnInfo.setHeightSimple(String
//										.valueOf(heightSimple));
//							} else {
//								crossStnInfo.setHeightSimple(String.valueOf(0));
//							}
//							listDictStn.add(crossStnInfo);
//						}
//
//					}
				// 保存数据


			String myJlData = objectMapper.writeValueAsString(dataList);
			// 图形数据
//				result.addObject("editStnSort", editStnSort);// 修改站序
//				logger.info("drawGraphIdAllIn==" + drawGraphIdAllIn);
			// System.err.println("myJlData==" + myJlData);
			String gridStr = objectMapper.writeValueAsString(grid);
			// System.err.println("gridStr==" + gridStr);
			
			mapresult.put("myJlData", myJlData);
			mapresult.put("gridData", gridStr);

		}
		return mapresult;
	}
	

	private String getMaxEndDate(String maxEndDate, TrainInfoDto dto) {
		if(maxEndDate!=null && !"".equals(maxEndDate) && dto.getEndDate()!=null && !"".equals(dto.getEndDate())){
			String endDate = dto.getEndDate();
			String maxDate = maxEndDate + " 00:00:00";
			return DateUtil.isBeforeForyyyyMMddHHmmss(maxDate, endDate) ? endDate : maxDate;
		}
		if(maxEndDate==null || "".equals(maxEndDate)){
			return dto.getEndDate().split(" ")[0];
		}
		if(dto.getEndDate()==null || "".equals(dto.getEndDate())){
			return maxEndDate;
		}
		return null;
	}
	private String getMinStartDate(String minStartDate, TrainInfoDto dto) {
		if(minStartDate!=null && !"".equals(minStartDate) && dto.getStartDate()!=null && !"".equals(dto.getStartDate())){
			String startdate = dto.getStartDate();
			String minDate = minStartDate + " 00:00:00";
			return DateUtil.isBeforeForyyyyMMddHHmmss(minDate, startdate) ? minDate : startdate;
		}
		if(minStartDate==null || "".equals(minStartDate)){
			return dto.getStartDate().split(" ")[0];
		}
		if(dto.getStartDate()==null || "".equals(dto.getStartDate())){
			return minStartDate;
		}
		return null;
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
}
