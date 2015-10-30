package org.railway.com.trainplan.service;

import java.beans.IntrospectionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.CompareUtil;
import org.railway.com.trainplan.common.utils.ExcelUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.entity.OriginalCross;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.CrossMybatisDao;
import org.railway.com.trainplan.repository.mybatis.OriginalCrossDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
@SuppressWarnings("unchecked")
public class OriginalCrossService {

	private static Log logger = LogFactory.getLog(OriginalCrossService.class);

	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Autowired
	private OriginalCrossDao originalCrossDao;

	@Autowired
	private CommonService commonService;

	@Autowired
	private CreateUnitCrossTrainUtilService createUnitCrossTrainUtilService;

	@Autowired
	private SchemeService schemeService;

	@Autowired
	private CrossService crossService;

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private CrossMybatisDao crossMybatisDao;

	/**
	 * 查询originalCross信息
	 * 
	 * @param reqMap
	 * @return
	 */
	public List<OriginalCross> getOriginalCrossInfo(Map<String, Object> reqMap) {
		List<OriginalCross> list = baseDao.selectListBySql(
				Constants.ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO, reqMap);
		return list;
	}

	public OriginalCross getOriginalCrossInfoById(String crossId)
			throws Exception {
		return (OriginalCross) this.baseDao.selectOneBySql(
				Constants.ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_BY_ID,
				crossId);
	}

	/**
	 * 查询 originalCross信息总条数
	 */
	public long getOriginalCrossInfoCount(Map<String, Object> reqMap) {
		List<Map<String, Object>> list = baseDao
				.selectListBySql(
						Constants.ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_COUNT,
						reqMap);
		long count = 0;
		if (list != null) {
			// 只有一条数据
			Map<String, Object> map = list.get(0);
			count = ((BigDecimal) map.get("COUNT")).longValue();
		}
		return count;
	}

	public void addOriginalCross(Map<String, Object> reqMap) throws Exception {
		JSONObject object = JSONObject.fromObject(reqMap.get("result"));
		object.remove("spareFlags");
		object.remove("highlineFlags");
		OriginalCross crossInfo = null;
		try {
			crossInfo = (OriginalCross) JSONObject.toBean(object,
					OriginalCross.class);
		} catch (Exception e) {
			logger.error("addOriginalCross JSONObject 转换对象失败！" + e.getMessage());
			return;
		}
		// 可以从数据库中获取
		Map<String, String> tokenPsgDeptValuesMap = commonService.getStationJCMapping();
		for(String key : tokenPsgDeptValuesMap.keySet()){
			if(crossInfo.getTokenVehBureau().equals(tokenPsgDeptValuesMap.get(key))){
				crossInfo.setTokenVehBureau(key);
			}
		}
		baseDao.insertBySql(Constants.ORIGINALCROSSDAO_ADD_ORIGINALCROSS_INFO,
				crossInfo);
	}

	public void updateOriginalCross(Map<String, Object> reqMap)
			throws Exception {
		JSONObject object = JSONObject.fromObject(reqMap.get("result"));
		String crossId = StringUtil.objToStr(object.get("crossId"));
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("crossId", crossId);
		OriginalCross crossInfo = (OriginalCross) baseDao.selectOneBySql(
				Constants.ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_FOR_PARAM,
				paramMap);
		if (crossInfo != null) {
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

			crossInfo.setMarshallingContent(StringUtil.objToStr(
					object.get("marshallingContent")).equals("null") ? ""
					: StringUtil.objToStr(object.get("marshallingContent")));

			crossInfo.setRunRange(Integer.valueOf(StringUtil.objToStr(
					object.get("runRange")).equals("null") ? "" : StringUtil
					.objToStr(object.get("runRange"))));

			crossInfo.setPeopleNums(Integer.valueOf(StringUtil.objToStr(
					object.get("peopleNums")).equals("null") ? "" : StringUtil
					.objToStr(object.get("peopleNums"))));

			crossInfo.setMarshallingNums(Integer.valueOf(StringUtil.objToStr(
					object.get("marshallingNums")).equals("null") ? ""
					: StringUtil.objToStr(object.get("marshallingNums"))));

			crossInfo.setCrossLevel(StringUtil.objToStr(
					object.get("crossLevel")).equals("null") ? "" : StringUtil
					.objToStr(object.get("crossLevel")));

			// 更新时，设置审核人、审核人单位、审核时间、交路创建时间为空
			crossInfo.setCheckPeople("");
			crossInfo.setCheckPeopleOrg("");
			crossInfo.setCheckTime(null);
			crossInfo.setCreatCrossTime("");

			baseDao.insertBySql(
					Constants.ORINIGALCROSSDAO_UPDATE_ORIGINAL_CROSS_INFO,
					crossInfo);

			// 最后删除其关联的所有信息
			this.deleteOriginalCrossRelevant(crossId.split(","));
		} else {
			throw new Exception("未查询到相关对数表信息");
		}

	}

	public void actionExcel(InputStream inputStream, String chartId,
			String startDay, String chartName, String addFlag)
			throws IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		// 初始化表头映射，因为映射的标题是符合标题行目前不支持，所以直接使用顺序映射(该排列顺序与excel字段排列顺序保持一致)
		LinkedHashMap<String, String> pm = new LinkedHashMap<String, String>();
		pm.put("crossIdForExcel", "");
		pm.put("crossName", "");
		pm.put("tokenVehBureau", "");
		pm.put("groupTotalNbr", "");
		pm.put("highlineFlag", "");
		pm.put("spareFlag", "");
		pm.put("alterNateDate", "");
		pm.put("alterNateTranNbr", "");
		pm.put("commonlineRule", "");
		pm.put("appointWeek", "");
		pm.put("appointDay", "");
		pm.put("appointPeriod", "");
		pm.put("crossSection", "");
		pm.put("crossLevel", "");
		pm.put("pairNbr", "");
		pm.put("runRange", "");
		pm.put("marshallingNums", "");
		pm.put("peopleNums", "");
		pm.put("marshallingContent", "");
		pm.put("airCondition", "");
		pm.put("elecSupply", "");
		pm.put("dejCollect", "");
		pm.put("note", "");
		pm.put("crhType", "");

		// pm.put("crossSpareName", "");
		// pm.put("cutOld", "");
		// pm.put("highlineRule", "");
		// pm.put("throughline", "");
		// pm.put("tokenVehBureau", "");
		// pm.put("tokenVehDept", "");
		// pm.put("tokenVehDepot", "");
		// pm.put("tokenPsgDept", "");
		// pm.put("locoType", "");

		Map<String, Map<String, String>> valuesMap = new HashMap<String, Map<String, String>>();

		// 可以从数据库中获取
		Map<String, String> tokenPsgDeptValuesMap = commonService
				.getStationJCMapping();

		// System.err.println("tokenPsgDeptValuesMap==" +
		// tokenPsgDeptValuesMap);
		valuesMap.put("tokenPsgBureau", tokenPsgDeptValuesMap);
		valuesMap.put("tokenVehBureau", tokenPsgDeptValuesMap);

		// 初始化一个线程池，在解析出交路信息以后，把每一条交路信息作为一个参数用来初始化一个CrossCompletionService作为一个县城丢到线程池中做异步处理
		ExecutorService service = Executors.newFixedThreadPool(10);
		CompletionService<String> completion = new ExecutorCompletionService<String>(
				service);

		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			int allNum = 0;
			int num = workbook.getNumberOfSheets();
			
			// 全部清空再导入，如果界面上选择为清空再倒入的时候
//			if ("1".equals(addFlag)) {
//				clearCrossInfoByChartId(chartId);
//			}

			// 迭代excel中的每一个sheet
			for (int i = 0; i < num; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				ExcelUtil<OriginalCross> test = new ExcelUtil<OriginalCross>(
						pm, sheet, OriginalCross.class);
				test.setValueMapping(valuesMap);
				List<OriginalCross> list = test.getEntitiesHasNoHeader(3);

				for (int j = 0; j < list.size(); j++) {

					OriginalCross crossInfo = list.get(j);
					// if(crossInfo.getCrossName().equals("50712-50713")){
					// int a = 0;
					// }
					// System.out.println(crossInfo.getCrossName());
					// System.out.println(crossInfo.getHighlineFlag());
					crossInfo.setChartId(chartId);
					crossInfo.setChartName(chartName);

					if (StringUtils.isEmpty(crossInfo.getAlterNateDate())) {
						crossInfo.setAlterNateDate(startDay);
					}
					completion.submit(new CrossCompletionService(list.get(j),
							baseDao, addFlag));
				}
				allNum += list.size();
			}
			// 基于ExecutorCompletionService
			// 线程管理模式，必须把结果集线程中的所有结果都显示的处理一次才认为当前线程完成了，当所有线程都被处理才表示当前线程组完成了
			for (int i = 0; i < allNum; i++) {
				try {
					completion.take().get();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			service.shutdown();
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}

	}

	/**
	 * 通过originalCrossId查询originalcrossinfo信息
	 * 
	 * @param originalCrossId
	 * @return
	 */
	public OriginalCross getOriginalCrossInfoForCrossid(String originalCrossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("crossId", originalCrossId);
		return (OriginalCross) baseDao.selectOneBySql(
				Constants.ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_FOR_PARAM,
				paramMap);

	}

	@SuppressWarnings("rawtypes")
	public boolean verifyOriginalCross(String originalCrossId) {
		try {
			OriginalCross ocross = this
					.getOriginalCrossInfoForCrossid(originalCrossId);
			List<SchemeInfo> schemes = schemeService.getSchemes();
			int index = 0;// 记录给定的cross位于第几条，以便获取其上一个日期的方案
			for (int i = 0; i < schemes.size(); i++) {
				if (ocross.getChartId().equalsIgnoreCase(
						schemes.get(i).getSchemeId())) {
					index = i;
					break;
				}
			}
			if (index != schemes.size() - 1) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("crossName", ocross.getCrossName());
				paramMap.put("chartId", schemes.get(index + 1).getSchemeId());
				List list = baseDao
						.selectListBySql(
								Constants.ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_FOR_PARAM,
								paramMap);
				if (list != null && list.size() > 0) {
					return CompareUtil.compareOriginalCross(ocross,
							(OriginalCross) list.get(0));
				}
				return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 用于并行处理单个交路信息的完善
	 * 
	 * @author Administrator
	 *
	 */
	class CrossCompletionService implements Callable<String> {

		private OriginalCross cross;

		private BaseDao baseDao;

		private String addFlag;

		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		public CrossCompletionService(OriginalCross cross) {
			this.cross = cross;
		}

		public CrossCompletionService(OriginalCross cross, BaseDao baseDao,
				String addFlag) {
			this.cross = cross;
			this.baseDao = baseDao;
			this.addFlag = addFlag;
		}

		// *************暂时注释****************
		private String[] getCrossInfoByCrossName(String crossName, String charId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("crossName", crossName);
			map.put("chartId", charId);
			List<OriginalCross> list = getOriginalCrossInfoByParam(map);
			String[] ids = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				ids[i] = list.get(i).getOriginalCrossId();
			}
			return ids;
		}

		// *************暂时注释****************
		private void clearOldCrossInfo() {
			String[] ids = getCrossInfoByCrossName(this.cross.getCrossName(),
					this.cross.getChartId());

			try {
				if (ids.length > 0) {
					deleteOriginalCrossRelevant(ids);
					deleteOriginalCrossInfoForCorssIds(ids);
				}
			} catch (Exception e) {
				logger.error("删除已有记录出错 ", e);
			}
		}

		// *************暂时注释***************************
		private boolean hasRole(String bureau) {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if (user.getBureau() == null) {
				return true;
			} else if (bureau.equals(user.getBureau())) {
				return true;
			} else {
				return false;
			}
			// List<String> permissionList = user.getPermissionList();
			// String[] bs = bureau.split("[、,]");
			// for(String p : permissionList){
			// for(String b : bs){
			// if(p.endsWith("." + b)){
			// return true;
			// }
			// }
			// }
			// return false;
		}

		public String call() throws Exception {

			if (!hasRole(this.cross.getTokenVehBureau())) {
				return null;
			}
			// *************暂时注释*********************
			if ("0".equals(addFlag)) {
				clearOldCrossInfo();
			}
			// *************暂时注释**********************
			// 普线和混合
			// if ("0".equals(this.cross.getHighlineFlag())
			// || "2".equals(this.cross.getHighlineFlag())) {
			// if (this.cross.getCommonlineRule() == null) {
			// this.cross.setCommonlineRule("1");
			// }
			// } else if ("1".equals(this.cross.getHighlineFlag())) {
			// if (this.cross.getHighlineRule() == null) {
			// this.cross.setHighlineRule("1");
			// }
			// }

			if ("1".equals(this.cross.getHighlineFlag())) {
				if (this.cross.getHighlineRule() == null) {
					this.cross.setHighlineRule("1");
				}
			} else {
				if (this.cross.getCommonlineRule() == null) {
					this.cross.setCommonlineRule("1");
				}
			}
			// *************暂时注释****************
			// LinkedList<CrossTrainInfo> crossTrains = this
			// .createTrainsForCross(this.cross);
			// *************暂时注释****************

			String routeBureauCode = getRelevantBureau(this.cross);
			this.cross.setRelevantBureau(routeBureauCode);
			// 保存交路信息
			ArrayList<OriginalCross> crossList = new ArrayList<OriginalCross>();
			crossList.add(this.cross);

			this.baseDao.insertBySql(
					Constants.ORIGINALCROSSDAO_INSERT_ORIGINALCROSS_INFO,
					crossList);

			// *************暂时注释****************
			// logger.debug(this.cross.getCrossName() + " ===crossTrains==="
			// + crossTrains.size());
			//
			// // 给查询出来的crossTrains添加有效期
			// if (crossTrains != null && crossTrains.size() > 0) {
			// //
			// this.baseDao.updateBySql(Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO,
			// // crossTrains);
			// addPeriodTime(crossTrains);
			// }
			//
			// // 保存列车
			// if (crossTrains != null && crossTrains.size() > 0) {
			// this.baseDao.insertBySql(
			// Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO, crossTrains);
			// }

			return "success";
		}

		private String getRelevantBureau(OriginalCross cross) throws Exception {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();

			String crossName = cross.getCrossName();
			String[] crossSpareNames = StringUtils.isEmpty(cross
					.getCrossSpareName()) ? null : cross.getCrossSpareName()
					.split("-");
			String[] alertNateTrains = StringUtils.isEmpty(cross
					.getAlterNateTranNbr()) ? null : cross
					.getAlterNateTranNbr().split("-");
			String[] alertNateDate = StringUtils.isEmpty(cross
					.getAlterNateDate()) ? null : cross.getAlterNateDate()
					.split("-");
			String[] spareFlag = StringUtils.isEmpty(cross.getSpareFlag()) ? null
					: cross.getSpareFlag().split("-");
			String[] highlineFlag = StringUtils
					.isEmpty(cross.getHighlineFlag()) ? null : cross
					.getHighlineFlag().split("-");
			String[] commonlineRule = StringUtils.isEmpty(cross
					.getCommonlineRule()) ? null : cross.getCommonlineRule()
					.split("-");
			String[] highlineRule = StringUtils
					.isEmpty(cross.getHighlineRule()) ? null : cross
					.getHighlineRule().split("-");

			String[] appointWeek = StringUtils.isEmpty(cross.getAppointWeek()) ? null
					: cross.getAppointWeek().split("-");
			String[] appointDay = StringUtils.isEmpty(cross.getAppointDay()) ? null
					: cross.getAppointDay().split("-");

			String[] trains = crossName.split("-");
			LinkedList<CrossTrainInfo> crossTrains = new LinkedList<CrossTrainInfo>();
			CrossTrainInfo train = null;
			for (int i = 0; i < trains.length; i++) {
				train = new CrossTrainInfo();
				train.setTrainSort(i + 1);
				train.setCrossId(cross.getOriginalCrossId());
				train.setTrainNbr(trains[i]);
				try {
					//
					if (alertNateTrains != null) {
						train.setAlertNateTrainNbr(alertNateTrains[i]);
					}
					//
					if (alertNateDate != null) {
						if (alertNateDate.length == 1) {
							train.setAlertNateTime(alertNateDate[0]
									+ " 00:00:00");
						} else {
							train.setAlertNateTime(alertNateDate[i]
									+ " 00:00:00");
						}
					}
					//
					if (spareFlag != null) {
						if (spareFlag.length == 1) {
							train.setSpareFlag(Integer.parseInt(spareFlag[0]));
						} else {
							train.setSpareFlag(Integer.parseInt(spareFlag[i]));
						}
					}

					if (highlineFlag != null) {
						if (highlineFlag.length == 1) {
							train.setHighlineFlag(Integer
									.parseInt(highlineFlag[0]));
						} else {
							train.setHighlineFlag(Integer
									.parseInt(highlineFlag[i]));
						}
					}

					if (commonlineRule != null) {
						if (commonlineRule.length == 1) {
							train.setCommonLineRule(Integer
									.parseInt(commonlineRule[0]));
						} else {
							train.setCommonLineRule(Integer
									.parseInt(commonlineRule[i]));
						}
					}

					if (highlineRule != null) {
						if (highlineRule.length == 1) {
							train.setHighlineRule(Integer
									.parseInt(highlineRule[0]));
						} else {
							train.setHighlineRule(Integer
									.parseInt(highlineRule[i]));
						}
					}

					if (appointDay != null) {
						if (appointDay.length == 1) {
							train.setAppointDay(appointDay[0]);
						} else {
							train.setAppointDay(appointDay[i]);
						}
					}

					if (appointWeek != null) {
						if (appointWeek.length == 1) {
							train.setAppointWeek(appointWeek[0]);
						} else {
							train.setAppointWeek(appointWeek[i]);
						}
					}

				} catch (Exception e) {
					logger.error("创建列车信息出错:", e);
				}

				crossTrains.add(train);
			}
			// 获取列车时刻表的起始和终到站
			String routeBureauCode = "";
			String routeBureauShortNames = "";
			boolean isSet = false;
			List<Map<String, Object>> allMapList = new ArrayList<Map<String, Object>>();

			boolean flag = false;// 是否直接通过shortBureau来确定

			Map<String, Object> trains0Dxx0 = getBureauFor0Dxxx50301(
					crossTrains, cross.getChartId());
			allMapList.add(trains0Dxx0);
			if (getBureauFor0Dxxx50301_2(crossTrains, cross.getChartId())) {
				List<PlanTrain> PlanTrainlist = ((List<PlanTrain>) trains0Dxx0
						.get(trains0Dxx0.size() - 1 + ""));
				if (PlanTrainlist.size() == 1) {
				} else {
					int acount = 0;
					for (Map<String, Object> map : allMapList) {// map==trains0Dxx
						for (int a = 0; a < map.size(); a++) {
							List<PlanTrain> planTrain = (List<PlanTrain>) map
									.get(a + "");
							for (PlanTrain oTrain : planTrain) {
								if (user.getBureauShortName().equals(
										oTrain.getRoutingBureauShortName())) {
									acount++;
								}
							}
						}
					}
					if (acount == crossTrains.size()) {// 说明 交路中得每一个车train
														// 都有一个是本局的车
						// 那么就确定第一个车了 就是第一个 本局的车
						flag = true;
					} else {
						throw new Exception(
								"this corss is invalid! (user-defined exception)");
					}
				}
			}

			if (!flag) {// 不是50301 的情况
				for (int i = 0; i < crossTrains.size(); i++) {
					CrossTrainInfo crossTrain = crossTrains.get(i);
					// getBureauFor0Dxxx(crossTrains,crossTrain,i);//0D2704-D2703-D2704-0D2703
					// 根据“后车的始发站”或“前车的终到站”自动匹配
					if (i == 0) {

						PlanTrain unionTrain = new PlanTrain();
						String startBureau = "";
						String startStation = "";

						// Map<n,list<PlanTrain>> n为（0.1.2....）， N为唯一确定车的序号

						Map<String, Object> trains0Dxx = getBureauFor0Dxxx(
								crossTrains, cross.getChartId(), i);

						if (null != trains0Dxx.get(trains0Dxx.size() - 1 + "")) {
							unionTrain = ((List<PlanTrain>) trains0Dxx
									.get(trains0Dxx.size() - 1 + "")).get(0);// 需要参照该车的始发站局
							startBureau = unionTrain.getStartBureau();
							startStation = unionTrain.getStartStn();
						}
						// PlanTrain unionTrain = ((List<PlanTrain>) trains0Dxx
						// .get(trains0Dxx.size() - 1 + "")).get(0);//
						// 需要参照该车的始发站局
						// String startBureau = unionTrain.getStartBureau();
						// String startStation = unionTrain.getStartStn();
						// 如果第一个站查询结果为大于1
						if (trains0Dxx.size() > 1) {
							for (int k = trains0Dxx.size() - 2; k >= 0; k--) {
								if (k == trains0Dxx.size() - 2) {
									// 取map.get(n)的始发站，作为参照
									trainInfoFromPainfirst(crossTrains.get(k),
											cross.getChartId(), startStation);
								} else {
									// 取crossTrains.get(k+1)的始发站，作为参照
									trainInfoFromPainfirst(crossTrains.get(k),
											cross.getChartId(), crossTrains
													.get(k + 1).getStartStn());
								}
							}
						} else {// 如果第一个站查询结果为只有一个车
							trainInfoFromPain(crossTrain, cross.getChartId(),
									"");// 说明 交路第一个车是唯一的 不需要参照
						}
					} else {
						// 直接去crossTrains得上一个元素的终到信息
						String bureau = "";
						String endStn = crossTrains.get(i - 1).getEndStn();
						// if (crossTrains.get(i - 1).getEndBureau() != null) {
						// endStn =
						// bureauDao.getShortBureauNameByCode(crossTrains
						// .get(i - 1).getEndStn());
						// }
						trainInfoFromPain(crossTrain, cross.getChartId(),
								endStn);// 这里不用查询数据库
										// 直接去crossTrains得上一个元素的终到信息
					}
					// 设置交路的开始日期和结束日期
					// if
					// (cross.getCrossName().startsWith(crossTrain.getTrainNbr()))
					// {
					// if (!isSet) {
					// cross.setStartBureau(crossTrain.getStartBureau());
					// cross.setCrossStartDate(crossTrain.getAlertNateTime()
					// .substring(0, 8));
					// isSet = true;
					// }
					// }
					routeBureauShortNames += crossTrain
							.getRouteBureauShortNames() != null ? crossTrain
							.getRouteBureauShortNames() : "";
				}
			}// if(!flag) end
			else {// 是 50301的情况

				// crossTrains
				Map<String, Object> trains0Dxx = getBureauFor0Dxxx(crossTrains,
						cross.getChartId(), 0);// 确定该路局的 车
				List<PlanTrain> ootrain = (List<PlanTrain>) trains0Dxx
						.get(0 + "");// 第一个车次 对应所有同名的车
				PlanTrain bureauFirsrPlanTrain = null;
				for (PlanTrain planTrain : ootrain) {
					if (user.getBureauShortName().equals(
							planTrain.getStartBureau())) {// 找到
					// if(user.getBureauShortName().equals(planTrain.getRoutingBureauShortName())){//找到
						bureauFirsrPlanTrain = planTrain;
						crossTrains = trainInfoFromPainfirst2(crossTrains,
								cross.getChartId(), planTrain.getStartBureau());
						// trainInfoFromPainfirst2(crossTrains.get(0),cross.getChartId(),
						// planTrain.getEndStn());
						// break;

					}
				}
				// 设置交路的开始日期和结束日期
				// if
				// (cross.getCrossName().startsWith(crossTrains.get(0).getTrainNbr()))
				// {
				// if (!isSet) {
				// cross.setStartBureau(crossTrains.get(0).getStartBureau());
				// cross.setCrossStartDate(alertNateDate[0]);
				// isSet = true;
				// }
				// }
				for (int i = 0; i < crossTrains.size(); i++) {
					train = crossTrains.get(i);
					train.setTrainSort(i + 1);
					train.setCrossId(cross.getOriginalCrossId());
					// train.setTrainNbr(trains[i]);
					try {
						//
						if (alertNateTrains != null) {
							train.setAlertNateTrainNbr(alertNateTrains[i]);
						}
						//
						if (alertNateDate != null) {
							if (alertNateDate.length == 1) {
								train.setAlertNateTime(alertNateDate[0]
										+ " 00:00:00");
							} else {
								train.setAlertNateTime(alertNateDate[i]
										+ " 00:00:00");
							}
						}
						//
						if (spareFlag != null) {
							if (spareFlag.length == 1) {
								train.setSpareFlag(Integer
										.parseInt(spareFlag[0]));
							} else {
								train.setSpareFlag(Integer
										.parseInt(spareFlag[i]));
							}
						}

						if (highlineFlag != null) {
							if (highlineFlag.length == 1) {
								train.setHighlineFlag(Integer
										.parseInt(highlineFlag[0]));
							} else {
								train.setHighlineFlag(Integer
										.parseInt(highlineFlag[i]));
							}
						}

						if (commonlineRule != null) {
							if (commonlineRule.length == 1) {
								train.setCommonLineRule(Integer
										.parseInt(commonlineRule[0]));
							} else {
								train.setCommonLineRule(Integer
										.parseInt(commonlineRule[i]));
							}
						}

						if (highlineRule != null) {
							if (highlineRule.length == 1) {
								train.setHighlineRule(Integer
										.parseInt(highlineRule[0]));
							} else {
								train.setHighlineRule(Integer
										.parseInt(highlineRule[i]));
							}
						}

						if (appointDay != null) {
							if (appointDay.length == 1) {
								train.setAppointDay(appointDay[0]);
							} else {
								train.setAppointDay(appointDay[i]);
							}
						}

						if (appointWeek != null) {
							if (appointWeek.length == 1) {
								train.setAppointWeek(appointWeek[0]);
							} else {
								train.setAppointWeek(appointWeek[i]);
							}
						}

					} catch (Exception e) {
						logger.error("创建列车信息出错:", e);
					}

					// crossTrains.add(train);
					crossTrains.set(i, train);

					routeBureauShortNames += train.getRouteBureauShortNames() != null ? train
							.getRouteBureauShortNames() : "";
				}
			}// if(!flag) else end

			if (!"".equals(routeBureauShortNames)) {
				String rbsPY = "";
				String py = "";
				for (int i = 0; i < routeBureauShortNames.length(); i++) {
					py = commonService.getStationPy(routeBureauShortNames
							.substring(i, i + 1));
					if ((py != null && rbsPY.indexOf(py) > -1) || py == null) {
						continue;
					}
					rbsPY += py;
				}
				routeBureauCode = rbsPY;
			}
			return routeBureauCode;
		}

		/**
		 * 需要将交路分成两部分，第一部分处理
		 * 
		 * @param train
		 * @param charId
		 * @param startStation
		 *            后车的始发站
		 * @param trains0Dxx
		 *            : Map<n,list<PlanTrain>>
		 *            n为第一个唯一车（0D2702-0D2701-0D2703-D2702 ,n=3,map.size()=4）
		 */
		private void trainInfoFromPainfirst(CrossTrainInfo train,
				String charId, String startStation) {
			// TODO 修改这个方法的时候，需要同时修改2433行的trainInfoFromPain
			try {

				String trainNbr = train.getTrainNbr();
				String stn = null;
				System.err.println("trainNbr==" + trainNbr);
				// String regex = "^(.+?)[\\[（](.+?)[\\）]";
				String regex = "^(.+?)[\\[【\\[](.+?)[\\】\\]]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(trainNbr);

				if (matcher.find()) {
					trainNbr = matcher.group(1);
					stn = matcher.group(2);
					System.err.println("trainNbr11==" + trainNbr);
					System.err.println("stn==" + stn);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("trainNbr", trainNbr);
				map.put("chartId", charId);

				if (stn != null) {
					String[] stns = stn.split("[:：]");
					if (stns.length == 1) {
						map.put("startStn", stns[0]);
					} else if (stns.length == 2) {
						map.put("startStn", stns[0]);
						map.put("endStn", stns[1]);
					}
				}
				List<BaseTrainInfo> baseTrains = getBaseTrainInfoByParam(map);
				if (baseTrains != null && baseTrains.size() > 0) {
					BaseTrainInfo currTrain = baseTrains.get(0);
					if (baseTrains.size() > 1) {
						for (BaseTrainInfo baseTrainInfo : baseTrains) {
							// if (beforeBureau.equals(baseTrainInfo
							// .getEndBureanShortName())) {//
							// 下一个站（该站的终到=下一个的始发局）
							// currTrain = baseTrainInfo;
							// }
							if (startStation.equals(baseTrainInfo.getEndStn())) {// 下一个站（该站的终到=下一个的始发局）1
								currTrain = baseTrainInfo;
							}
						}
					}
					train.setBaseTrainId(currTrain.getBaseTrainId());
					train.setSourceTargetTime(currTrain.getStartTime());
					train.setStartStn(currTrain.getStartStn());

					train.setStartBureau(commonService.getStationPy(currTrain
							.getStartBureauShortName()));
					train.setEndStn(currTrain.getEndStn());
					train.setRunDay(currTrain.getRunDays());
					train.setTargetTime(currTrain.getEndTime());
					train.setRouteBureauShortNames(currTrain
							.getRouteBureauShortNames());
					train.setEndBureau(commonService.getStationPy(currTrain
							.getEndBureanShortName()));
				} else {
					logger.warn(map + "没有可匹配的对象");
				}
			} catch (Exception e) {

				logger.error("获取列车信息失败:", e);
			}

		}

		/**
		 * 需要将交路分成两部分，第2部分处理
		 * 
		 * @param train
		 * @param charId
		 * @param trains0Dxx
		 *            : Map<n,list<PlanTrain>>
		 *            n为第一个唯一车（0D2702-0D2701-0D2703-D2702 ,n=3,map.size()=4）
		 */
		private void trainInfoFromPain(CrossTrainInfo train, String charId,
		// String beforeBureau) {
				String endStn) {

			try {

				String trainNbr = train.getTrainNbr();
				String stn = null;
				System.err.println("trainNbr==" + trainNbr);
				// String regex = "^(.+?)[\\[（](.+?)[\\）]";
				String regex = "^(.+?)[\\[【\\[](.+?)[\\】\\]]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(trainNbr);

				if (matcher.find()) {
					trainNbr = matcher.group(1);
					stn = matcher.group(2);
					System.err.println("trainNbr11==" + trainNbr);
					System.err.println("stn==" + stn);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("trainNbr", trainNbr);
				map.put("chartId", charId);

				if (stn != null) {
					String[] stns = stn.split("[:：]");
					if (stns.length == 1) {
						map.put("startStn", stns[0]);
					} else if (stns.length == 2) {
						map.put("startStn", stns[0]);
						map.put("endStn", stns[1]);
					}
				}
				List<BaseTrainInfo> baseTrains = getBaseTrainInfoByParam(map);
				if (baseTrains != null && baseTrains.size() > 0) {
					BaseTrainInfo currTrain = baseTrains.get(0);
					if (baseTrains.size() > 1) {
						for (BaseTrainInfo baseTrainInfo : baseTrains) {
							// if (beforeBureau.equals(baseTrainInfo
							// .getStartBureauShortName())) {//
							// 该站的始发==参照的终到局（上一个站）
							// currTrain = baseTrainInfo;
							// }
							if (endStn.equals(baseTrainInfo.getStartStn())) {// 该站的始发==参照的终到局（上一个站）
								currTrain = baseTrainInfo;
							}
						}
					}
					train.setBaseTrainId(currTrain.getBaseTrainId());
					train.setSourceTargetTime(currTrain.getStartTime());
					train.setStartStn(currTrain.getStartStn());

					train.setStartBureau(commonService.getStationPy(currTrain
							.getStartBureauShortName()));
					train.setEndStn(currTrain.getEndStn());
					train.setRunDay(currTrain.getRunDays());
					train.setTargetTime(currTrain.getEndTime());
					train.setRouteBureauShortNames(currTrain
							.getRouteBureauShortNames());
					train.setEndBureau(commonService.getStationPy(currTrain
							.getEndBureanShortName()));
				} else {
					logger.warn(map + "没有可匹配的对象");
				}
			} catch (Exception e) {

				logger.error("获取列车信息失败:", e);
			}

		}

		/**
		 * 需要将交路分成两部分，第一部分处理,交路中得每一个车 都在济南局 有train
		 * 
		 * @param train
		 * @param charId
		 * @param bureau
		 * @param trains0Dxx
		 *            :
		 */
		private LinkedList<CrossTrainInfo> trainInfoFromPainfirst2(
				LinkedList<CrossTrainInfo> oldLinkBaseTrains, String charId,
				String bureau) {
			// TODO 修改这个方法的时候，需要同时修改2433行的trainInfoFromPain

			LinkedList<CrossTrainInfo> crossTrains = new LinkedList<CrossTrainInfo>();
			CrossTrainInfo train = null;
			// for (int i=oldLinkBaseTrains.size()-1 ; i >=0 ; i-- ) {
			for (int i = 0; i < oldLinkBaseTrains.size(); i++) {

				train = new CrossTrainInfo();
				String trainNbr = oldLinkBaseTrains.get(i).getTrainNbr();
				String stn = null;
				System.err.println("trainNbr==" + trainNbr);
				// String regex = "^(.+?)[\\[（](.+?)[\\）]";
				String regex = "^(.+?)[\\[【\\[](.+?)[\\】\\]]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(trainNbr);

				if (matcher.find()) {
					trainNbr = matcher.group(1);
					stn = matcher.group(2);
					System.err.println("trainNbr11==" + trainNbr);
					System.err.println("stn==" + stn);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("trainNbr", trainNbr);
				map.put("chartId", charId);
				map.put("bureau", bureau);//
				// map.put("bureau", LjUtil.getLjByName(bureau, 2));//

				if (stn != null) {
					String[] stns = stn.split("[:：]");
					if (stns.length == 1) {
						map.put("startStn", stns[0]);
					} else if (stns.length == 2) {
						map.put("startStn", stns[0]);
						map.put("endStn", stns[1]);
					}
				}

				List<BaseTrainInfo> baseTrainInfoByParamByEndStn = getBaseTrainInfoByParamByEndStn(map);
				BaseTrainInfo currTrain = null;
				if (baseTrainInfoByParamByEndStn != null) {
					currTrain = baseTrainInfoByParamByEndStn.get(0);

					train.setTrainNbr(trainNbr);
					train.setBaseTrainId(currTrain.getBaseTrainId());
					train.setSourceTargetTime(currTrain.getStartTime());
					train.setStartStn(currTrain.getStartStn());

					train.setStartBureau(commonService.getStationPy(currTrain
							.getStartBureauShortName()));
					train.setEndStn(currTrain.getEndStn());
					train.setRunDay(currTrain.getRunDays());
					train.setTargetTime(currTrain.getEndTime());
					train.setRouteBureauShortNames(currTrain
							.getRouteBureauShortNames());
					train.setEndBureau(commonService.getStationPy(currTrain
							.getEndBureanShortName()));
					// train.setAlertNateTime(currTrain.getStartTime());

				}

				// crossTrains.add(oldLinkBaseTrains.size()-1-i, train);
				// crossTrains.add(i, train);
				crossTrains.add(train);
			}
			return crossTrains;
		}
	}

	/**
	 * 转换原始对数表为交路信息
	 * 
	 * @param reqMap
	 * @throws Exception
	 */
	public void transferCross(Map<String, Object> reqMap) throws Exception {
		List<OriginalCross> crossList = null;
		String crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
		if ("".equals(crossIds)) {
			throw new Exception("没有传入相关crossId");
		}
		String[] crossIdArr = crossIds.split(",");
		int len = crossIdArr.length;
		if (len == 0) {
			logger.error("错误格式对数表ID" + crossIds);
			throw new Exception("生成对数表ID数据失败");
		}
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < len; i++) {
			bf.append("'").append(crossIdArr[i]).append("'");
			if (i != len - 1) {
				bf.append(",");
			}
		}
		map.put("crossIds", bf.toString());
		try {
			crossList = baseDao.selectListBySql(
					Constants.ORINIGALCROSSDAO_GET_ORIGINAL_CROSS_INFO_BY_IDS,
					map);
		} catch (Exception e) {
			logger.error("transferCross 根据IDS查询原始对数表信息失败", e);
			throw new Exception(e);
		}
		if (crossList != null && crossList.size() > 0) {
			transferBaseCross(crossList);
		}

		// 生成交路时，最后将其交路生成时间记录下来
		for (OriginalCross ocross : crossList) {
			ocross.setCreatCrossTime(format.format(new Date()));
			baseDao.insertBySql(
					Constants.ORINIGALCROSSDAO_UPDATE_ORIGINAL_CROSS_INFO,
					ocross);
		}

	}

	public void transferUnitCrossInfo(List<CrossInfo> crossList)
			throws Exception {
		if (crossList == null || crossList.size() == 0) {
			logger.error("transferUnitCrossInfo 原始对数表列表为空！");
			return;
		}
		List<String> crossIdList = new ArrayList<String>();
		for (CrossInfo cross : crossList) {
			crossIdList.add(cross.getCrossId());
		}
		String[] crossIdArr = crossIdList
				.toArray(new String[crossIdList.size()]);

		try {
			String createedCrossIds = "";
			String createedCrossNames = "";
			String chartIds = "";
			int errCount = 0;
			int updCount = 0;
			List<String> crossIdsList = new ArrayList<String>();
			// JSONArray resultArr = new JSONArray();
			// JSONArray crosses = JSONArray.fromObject(reqMap
			// .get("Objcrosses"));

			// deleteUnitCrossAndCrossTrain()
			List<CrossInfo> unitCross = null;
			boolean flag = false;
			for (int k = 0; k < crossList.size(); k++) {
				CrossInfo crossRow = crossList.get(k);
				String crossId_ = crossRow.getCrossId();
				String crossName = crossRow.getCrossName();
				String chartId = crossRow.getChartId();

				// 可能是同名的两个交路，但是如果重新导入了对数表，那么交路的crossid 也发生了变化
				unitCross = crossService
						.getUnitCrossInfosForCrossNameAndChartId(crossName,
								chartId);
				// if (unitCross.size() > 0) {
				// if (errCount > 0) {
				// createedCrossIds = createedCrossIds + ",";
				// createedCrossNames = createedCrossNames + ",";
				// chartIds = chartIds + ",";
				// }
				// createedCrossIds = createedCrossIds + crossId_;
				// createedCrossNames = createedCrossNames + crossName;
				// chartIds = chartIds + chartId;
				// errCount++;
				// continue;
				// }
				// 生成交路单元
				JSONObject obj = new JSONObject();
				crossService.completeUnitCrossInfo(crossId_);
				updCount++;
				crossIdsList.add(crossId_);
				obj.put("crossId", crossId_);
				obj.put("flag", 1);

			}

			// 生成交路单元完成后，更改表base_cross中的creat_unit_time字段的值
			if (crossIdsList.size() > 0) {
				crossService.updateCrossUnitCreateTime(crossIdsList);
			}
			// if (errCount > 0) {
			// result.setCode("creatUnitWarm");// 显示框
			// StringBuilder sb = new StringBuilder();
			//
			// if (updCount > 0) {
			//
			// } else {
			// result.setMessage(sb.toString());
			// }
			// result.setData(chartIds + "__" + createedCrossNames + "__"
			// + createedCrossIds);
			// result.setData1(resultArr);
			// } else {
			// result.setData(resultArr);
			// }

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("completeUnitCrossInfo error==" + e.getMessage());
		}
	}

	/**
	 * 转换原始对数表为baseCross和baseCrossTrain
	 * 
	 * @param crossList
	 * @throws Exception
	 */
	public void transferBaseCross(List<OriginalCross> crossList)
			throws Exception {
		if (crossList == null || crossList.size() == 0) {
			logger.error("transferBaseCross 原始对数表列表为空！");
			return;
		}
		List<CrossInfo> baseCrossList = new ArrayList<CrossInfo>();
		for (OriginalCross cross : crossList) {
			CrossInfo baseCross = new CrossInfo();
			BeanUtils.copyProperties(cross, baseCross, "");
			baseCross.setCrossId(cross.getOriginalCrossId());
			baseCrossList.add(baseCross);
		}

		try {
			// 初始化一个线程池，在解析出交路信息以后，把每一条交路信息作为一个参数用来初始化一个CrossCompletionService作为一个县城丢到线程池中做异步处理
			ExecutorService service = Executors.newFixedThreadPool(10);
			CompletionService<String> completion = new ExecutorCompletionService<String>(
					service);
			for (CrossInfo baseCross : baseCrossList) {
				completion.submit(new BaseCrossCompletionService(baseCross,
						baseDao));
			}
			// 基于ExecutorCompletionService
			// 线程管理模式，必须把结果集线程中的所有结果都显示的处理一次才认为当前线程完成了，当所有线程都被处理才表示当前线程组完成了
			for (int i = 0; i < baseCrossList.size(); i++) {
				try {
					completion.take().get();
				} catch (Exception e) {
					logger.error("transferBaseCross 获取线程池线程错误", e);
				}
			}
			service.shutdown();
		} catch (Exception e) {
			logger.error("transferBaseCross 线程池执行错误", e);
		}

	}

	/**
	 * 审核baseCross（修改审核时间）
	 * 
	 * @param baseCrossList
	 * @throws Exception
	 */
	public void checkBaseCrossInfo(List<CrossInfo> baseCrossList)
			throws Exception {
		if (baseCrossList != null && baseCrossList.size() > 0) {
			// 组装字符串
			StringBuffer bf = new StringBuffer();
			Map<String, Object> reqMap = new HashMap<String, Object>();
			int size = baseCrossList.size();
			for (int i = 0; i < size; i++) {
				bf.append("'").append(baseCrossList.get(i).getCrossId())
						.append("'");
				if (i != size - 1) {
					bf.append(",");
				}
			}
			reqMap.put("baseCrossIds", bf.toString());

			baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CHECKTIME,
					reqMap);
			for (CrossInfo cross : baseCrossList) {
				// 在原始对数表中记录下审核人、审核人单位、审核日期信息
				saveCheckInfo(this.getOriginalCrossInfoById(cross.getCrossId()));
			}
		}

	}

	/**
	 * 根据originalCross的id数组记录下对应数据的审核人、审核人单位、审核日期信息
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public void checkBaseCrossInfo(String[] ids) throws Exception {
		for (String id : ids) {
			// 在原始对数表中记录下审核人、审核人单位、审核日期信息
			saveCheckInfo(this.getOriginalCrossInfoById(id));
		}
	}

	// 在原始对数表中记录下审核人、审核人单位、审核日期信息
	private void saveCheckInfo(OriginalCross ocross) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		ocross.setCheckPeople(user.getName());
		ocross.setCheckPeopleOrg(user.getDeptName());
		ocross.setCheckTime(format.format(new Date()));
		baseDao.insertBySql(
				Constants.ORIGINALCROSSDAO_UPDATE_ORIGINALCROSS_CHECKINFO_BY_ID,
				ocross);
	}

	/**
	 * 用于并行处理单个交路信息的完善
	 * 
	 * @author Administrator
	 *
	 */
	class BaseCrossCompletionService implements Callable<String> {

		private CrossInfo cross;

		private BaseDao baseDao;

		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		public BaseCrossCompletionService(CrossInfo cross) {
			this.cross = cross;
		}

		public BaseCrossCompletionService(CrossInfo cross, BaseDao baseDao) {
			this.cross = cross;
			this.baseDao = baseDao;
		}

		private String[] getCrossInfoByCrossName(String crossName, String charId) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("crossName", crossName);
			map.put("chartId", charId);
			List<CrossInfo> list = getCrossInfoByParam(map);
			String[] ids = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				ids[i] = list.get(i).getCrossId();
			}
			return ids;
		}

		private boolean hasRole(String bureau) {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if (user.getBureau() == null) {
				return true;
			} else if (bureau.equals(user.getBureau())) {
				return true;
			} else {
				return false;
			}

		}

		public String call() throws Exception {

			// if (!hasRole(this.cross.getTokenVehBureau())) {
			// return null;
			// }

			if ("1".equals(this.cross.getHighlineFlag())) {
				if (this.cross.getHighlineRule() == null) {
					this.cross.setHighlineRule("1");
				}
			} else {
				if (this.cross.getCommonlineRule() == null) {
					this.cross.setCommonlineRule("1");
				}
			}

			LinkedList<CrossTrainInfo> crossTrains = this
					.createTrainsForCross(this.cross);
			// 保存交路信息
			ArrayList<CrossInfo> crossList = new ArrayList<CrossInfo>();
			crossList.add(this.cross);

			this.baseDao.insertBySql(Constants.CROSSDAO_ADD_CROSS_INFO,
					crossList);

			logger.debug(this.cross.getCrossName() + " ===crossTrains==="
					+ crossTrains.size());

			// 给查询出来的crossTrains添加有效期
			if (crossTrains != null && crossTrains.size() > 0) {
				// this.baseDao.updateBySql(Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO,
				// crossTrains);
				addPeriodTime(crossTrains);
			}

			// 保存列车
			if (crossTrains != null && crossTrains.size() > 0) {
				this.baseDao.insertBySql(
						Constants.CROSSDAO_ADD_CROSS_TRAIN_INFO, crossTrains);
			}

			checkBaseCrossInfo(crossList);
			transferUnitCrossInfo(crossList);
			return "success";
		}

		/**
		 * 需要将交路分成两部分，第一部分处理
		 * 
		 * @param train
		 * @param charId
		 * @param startStation
		 *            后车的始发站
		 * @param trains0Dxx
		 *            : Map<n,list<PlanTrain>>
		 *            n为第一个唯一车（0D2702-0D2701-0D2703-D2702 ,n=3,map.size()=4）
		 */
		// private void trainInfoFromPainfirst(CrossTrainInfo train,
		// String charId, String beforeBureau) {
		private void trainInfoFromPainfirst(CrossTrainInfo train,
				String charId, String startStation) {
			// TODO 修改这个方法的时候，需要同时修改2433行的trainInfoFromPain
			try {

				String trainNbr = train.getTrainNbr();
				String stn = null;
				System.err.println("trainNbr==" + trainNbr);
				// String regex = "^(.+?)[\\[（](.+?)[\\）]";
				String regex = "^(.+?)[\\[【\\[](.+?)[\\】\\]]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(trainNbr);

				if (matcher.find()) {
					trainNbr = matcher.group(1);
					stn = matcher.group(2);
					System.err.println("trainNbr11==" + trainNbr);
					System.err.println("stn==" + stn);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("trainNbr", trainNbr);
				map.put("chartId", charId);

				if (stn != null) {
					String[] stns = stn.split("[:：]");
					if (stns.length == 1) {
						map.put("startStn", stns[0]);
					} else if (stns.length == 2) {
						map.put("startStn", stns[0]);
						map.put("endStn", stns[1]);
					}
				}
				List<BaseTrainInfo> baseTrains = getBaseTrainInfoByParam(map);
				if (baseTrains != null && baseTrains.size() > 0) {
					BaseTrainInfo currTrain = baseTrains.get(0);
					if (baseTrains.size() > 1) {
						for (BaseTrainInfo baseTrainInfo : baseTrains) {
							// if (beforeBureau.equals(baseTrainInfo
							// .getEndBureanShortName())) {//
							// 下一个站（该站的终到=下一个的始发局）
							// currTrain = baseTrainInfo;
							// }
							if (startStation.equals(baseTrainInfo.getEndStn())) {// 下一个站（该站的终到=下一个的始发局）1
								currTrain = baseTrainInfo;
							}
						}
					}
					train.setBaseTrainId(currTrain.getBaseTrainId());
					train.setSourceTargetTime(currTrain.getStartTime());
					train.setStartStn(currTrain.getStartStn());

					train.setStartBureau(commonService.getStationPy(currTrain
							.getStartBureauShortName()));
					train.setEndStn(currTrain.getEndStn());
					train.setRunDay(currTrain.getRunDays());
					train.setTargetTime(currTrain.getEndTime());
					train.setRouteBureauShortNames(currTrain
							.getRouteBureauShortNames());
					train.setEndBureau(commonService.getStationPy(currTrain
							.getEndBureanShortName()));
				} else {
					logger.warn(map + "没有可匹配的对象");
				}
			} catch (Exception e) {

				logger.error("获取列车信息失败:", e);
			}

		}

		/**
		 * 需要将交路分成两部分，第一部分处理,交路中得每一个车 都在济南局 有train
		 * 
		 * @param train
		 * @param charId
		 * @param bureau
		 * @param trains0Dxx
		 *            :
		 */
		private LinkedList<CrossTrainInfo> trainInfoFromPainfirst2(
				LinkedList<CrossTrainInfo> oldLinkBaseTrains, String charId,
				String bureau) {
			// TODO 修改这个方法的时候，需要同时修改2433行的trainInfoFromPain

			LinkedList<CrossTrainInfo> crossTrains = new LinkedList<CrossTrainInfo>();
			CrossTrainInfo train = null;
			// for (int i=oldLinkBaseTrains.size()-1 ; i >=0 ; i-- ) {
			for (int i = 0; i < oldLinkBaseTrains.size(); i++) {

				train = new CrossTrainInfo();
				String trainNbr = oldLinkBaseTrains.get(i).getTrainNbr();
				String stn = null;
				System.err.println("trainNbr==" + trainNbr);
				// String regex = "^(.+?)[\\[（](.+?)[\\）]";
				String regex = "^(.+?)[\\[【\\[](.+?)[\\】\\]]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(trainNbr);

				if (matcher.find()) {
					trainNbr = matcher.group(1);
					stn = matcher.group(2);
					System.err.println("trainNbr11==" + trainNbr);
					System.err.println("stn==" + stn);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("trainNbr", trainNbr);
				map.put("chartId", charId);
				map.put("bureau", bureau);//
				// map.put("bureau", LjUtil.getLjByName(bureau, 2));//

				if (stn != null) {
					String[] stns = stn.split("[:：]");
					if (stns.length == 1) {
						map.put("startStn", stns[0]);
					} else if (stns.length == 2) {
						map.put("startStn", stns[0]);
						map.put("endStn", stns[1]);
					}
				}

				List<BaseTrainInfo> baseTrainInfoByParamByEndStn = getBaseTrainInfoByParamByEndStn(map);
				BaseTrainInfo currTrain = null;
				if (baseTrainInfoByParamByEndStn != null) {
					currTrain = baseTrainInfoByParamByEndStn.get(0);

					train.setTrainNbr(trainNbr);
					train.setBaseTrainId(currTrain.getBaseTrainId());
					train.setSourceTargetTime(currTrain.getStartTime());
					train.setStartStn(currTrain.getStartStn());

					train.setStartBureau(commonService.getStationPy(currTrain
							.getStartBureauShortName()));
					train.setEndStn(currTrain.getEndStn());
					train.setRunDay(currTrain.getRunDays());
					train.setTargetTime(currTrain.getEndTime());
					train.setRouteBureauShortNames(currTrain
							.getRouteBureauShortNames());
					train.setEndBureau(commonService.getStationPy(currTrain
							.getEndBureanShortName()));
					// train.setAlertNateTime(currTrain.getStartTime());

				}

				// crossTrains.add(oldLinkBaseTrains.size()-1-i, train);
				// crossTrains.add(i, train);
				crossTrains.add(train);
			}
			return crossTrains;
		}

		/**
		 * 需要将交路分成两部分，第2部分处理
		 * 
		 * @param train
		 * @param charId
		 * @param trains0Dxx
		 *            : Map<n,list<PlanTrain>>
		 *            n为第一个唯一车（0D2702-0D2701-0D2703-D2702 ,n=3,map.size()=4）
		 */
		private void trainInfoFromPain(CrossTrainInfo train, String charId,
		// String beforeBureau) {
				String endStn) {

			try {

				String trainNbr = train.getTrainNbr();
				String stn = null;
				System.err.println("trainNbr==" + trainNbr);
				// String regex = "^(.+?)[\\[（](.+?)[\\）]";
				String regex = "^(.+?)[\\[【\\[](.+?)[\\】\\]]";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(trainNbr);

				if (matcher.find()) {
					trainNbr = matcher.group(1);
					stn = matcher.group(2);
					System.err.println("trainNbr11==" + trainNbr);
					System.err.println("stn==" + stn);
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("trainNbr", trainNbr);
				map.put("chartId", charId);

				if (stn != null) {
					String[] stns = stn.split("[:：]");
					if (stns.length == 1) {
						map.put("startStn", stns[0]);
					} else if (stns.length == 2) {
						map.put("startStn", stns[0]);
						map.put("endStn", stns[1]);
					}
				}
				List<BaseTrainInfo> baseTrains = getBaseTrainInfoByParam(map);
				if (baseTrains != null && baseTrains.size() > 0) {
					BaseTrainInfo currTrain = baseTrains.get(0);
					if (baseTrains.size() > 1) {
						for (BaseTrainInfo baseTrainInfo : baseTrains) {
							// if (beforeBureau.equals(baseTrainInfo
							// .getStartBureauShortName())) {//
							// 该站的始发==参照的终到局（上一个站）
							// currTrain = baseTrainInfo;
							// }
							if (endStn.equals(baseTrainInfo.getStartStn())) {// 该站的始发==参照的终到局（上一个站）
								currTrain = baseTrainInfo;
							}
						}
					}
					train.setBaseTrainId(currTrain.getBaseTrainId());
					train.setSourceTargetTime(currTrain.getStartTime());
					train.setStartStn(currTrain.getStartStn());

					train.setStartBureau(commonService.getStationPy(currTrain
							.getStartBureauShortName()));
					train.setEndStn(currTrain.getEndStn());
					train.setRunDay(currTrain.getRunDays());
					train.setTargetTime(currTrain.getEndTime());
					train.setRouteBureauShortNames(currTrain
							.getRouteBureauShortNames());
					train.setEndBureau(commonService.getStationPy(currTrain
							.getEndBureanShortName()));
				} else {
					logger.warn(map + "没有可匹配的对象");
				}
			} catch (Exception e) {

				logger.error("获取列车信息失败:", e);
			}

		}

		private void setDayGapForTrains(LinkedList<CrossTrainInfo> crossTrains) {
			for (int i = 0; i < crossTrains.size(); i++) {
				setDayGap(crossTrains.get(i),
						crossTrains.get(i - 1 < 0 ? crossTrains.size() - 1
								: i - 1));
			}

		}

		/**
		 * 设置交路的结束日期和列车的开始和结束日期
		 */
		private void setEndDateForCross(LinkedList<CrossTrainInfo> crossTrains,
				CrossInfo cross) {
			int dayGapForCross = 0;
			String crossStartDate = cross.getCrossStartDate();
			try {
				// 设置交路的终到日期
				for (int i = 0; i < crossTrains.size(); i++) {
					CrossTrainInfo crosstrain = crossTrains.get(i);
					if (i == 0) {
						crosstrain.setRunDate(crossStartDate);
						Date date = this.dateFormat.parse(crossStartDate);
						Calendar calendar = GregorianCalendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DATE, crosstrain.getRunDay());
						crosstrain.setEndDate(this.dateFormat.format(calendar
								.getTime()));
						dayGapForCross += crosstrain.getRunDay();
					} else {
						// 第二个车+前面的车的总天数 + daygap
						Date date = this.dateFormat.parse(crossStartDate);
						Calendar calendar = GregorianCalendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DATE,
								(dayGapForCross + crosstrain.getDayGap()));

						crosstrain.setRunDate(this.dateFormat.format(calendar
								.getTime()));

						dayGapForCross += crosstrain.getRunDay()
								+ crosstrain.getDayGap();

						// 设置结束时间
						calendar.add(Calendar.DATE, crosstrain.getRunDay());
						crosstrain.setEndDate(this.dateFormat.format(calendar
								.getTime()));

					}
				}
				Date date = this.dateFormat.parse(cross.getCrossStartDate());
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, dayGapForCross);

				cross.setCrossEndDate(this.dateFormat.format(calendar.getTime()));

			} catch (Exception e) {
				logger.error("设置结束时间出错 ", e);
			}
		}

		/**
		 * 设置交路的结束时间和开始时间，同时设置列车的开行日期
		 * 
		 * @param crossTrains
		 * @param cross
		 */
		private void setStartAndEndTime(LinkedList<CrossTrainInfo> crossTrains,
				CrossInfo cross) {
			// 有多线程问题
			int dayGapForCross = 0;
			String crossStartDate = cross.getCrossStartDate();
			try {
				// 设置交路的终到日期

				for (int i = 0; i < crossTrains.size(); i++) {
					CrossTrainInfo crosstrain = crossTrains.get(i);
					if (i == 0) {
						// crosstrain.setRunDate(cross.getCrossStartDate());
						crosstrain.setRunDate(cross.getCrossStartDate());
						Date date = this.dateFormat.parse(crossStartDate);
						Calendar calendar = GregorianCalendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DATE, crosstrain.getRunDay());
						crosstrain.setEndDate(this.dateFormat.format(calendar
								.getTime()));

						dayGapForCross += crosstrain.getRunDay();
					} else {
						// 第二个车+前面的车的总天数 + daygap
						Date date = this.dateFormat.parse(crossStartDate);
						Calendar calendar = GregorianCalendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DATE,
								dayGapForCross + crosstrain.getDayGap());

						crosstrain.setRunDate(this.dateFormat.format(calendar
								.getTime()));

						dayGapForCross += crosstrain.getRunDay()
								+ crosstrain.getDayGap();

						// 设置结束时间
						calendar.add(Calendar.DATE, crosstrain.getRunDay());
						crosstrain.setEndDate(this.dateFormat.format(calendar
								.getTime()));

					}
				}
			} catch (Exception e) {
				logger.error("设置结束时间出错 ", e);
			}
		}

		/**
		 * 计算列车间隔时间
		 * 
		 * @param train1
		 *            需要设置间隔时间的列车
		 * @param train2
		 *            前置列车
		 */
		private void setDayGap(CrossTrainInfo train1, CrossTrainInfo train2) {
			try {
				String sourceTimeTemp = train1.getSourceTargetTime();
				String targetTimeTemp = train1.getTargetTime();
				Date sourceTime = null;
				Date targetTime = null;
				if (sourceTimeTemp != null && targetTimeTemp != null) {
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					sourceTime = format
							.parse("1977-01-01 "
									+ (sourceTimeTemp == null
											|| "".equals(sourceTimeTemp) ? targetTimeTemp
											: sourceTimeTemp));
					if (train2 != null) {
						if (null != train2.getTargetTime()
								&& !"".equals(train2.getTargetTime())) {
							targetTime = format.parse("1977-01-01 "
									+ train2.getTargetTime().trim());
						} else {
							targetTime = format
									.parse("1977-01-01 "
											+ (targetTimeTemp == null
													|| "".equals(targetTimeTemp) ? sourceTimeTemp
													: targetTimeTemp));
						}
					} else {
						targetTime = format
								.parse("1977-01-01 "
										+ (targetTimeTemp == null
												|| "".equals(targetTimeTemp) ? sourceTimeTemp
												: targetTimeTemp));
					}
					// targetTime = train2 != null ? format.parse("1977-01-01 "
					// + train2.getTargetTime() == null ||
					// "".equals(train2.getTargetTime())?sourceTimeTemp:targetTimeTemp)
					// : format.parse("1977-01-01 "
					// + (targetTimeTemp == null ||
					// "".equals(targetTimeTemp)?sourceTimeTemp:targetTimeTemp));

					if (sourceTime.compareTo(targetTime) < 0) {
						train1.setDayGap(1);
					} else {
						train1.setDayGap(0);
					}
				}

			} catch (Exception e) {
				// System.out.println("1977-01-01 " +
				// (train1.getSourceTargetTime() == null ||
				// "".equals(train1.getSourceTargetTime())?train1.getTargetTime():train1.getSourceTargetTime()));
				logger.error(train2.getTrainNbr() + "--getTargetTime-"
						+ train2.getTargetTime());
				logger.error(train1.getTrainNbr() + "--getSourceTargetTime-"
						+ train1.getSourceTargetTime());
				logger.error(train1.getTrainNbr() + "--getTargetTime-"
						+ train1.getTargetTime());
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			;

		}

		private LinkedList<CrossTrainInfo> createTrainsForCross(CrossInfo cross)
				throws Exception {

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();

			String crossName = cross.getCrossName();
			String[] crossSpareNames = StringUtils.isEmpty(cross
					.getCrossSpareName()) ? null : cross.getCrossSpareName()
					.split("-");
			String[] alertNateTrains = StringUtils.isEmpty(cross
					.getAlterNateTranNbr()) ? null : cross
					.getAlterNateTranNbr().split("-");
			String[] alertNateDate = StringUtils.isEmpty(cross
					.getAlterNateDate()) ? null : cross.getAlterNateDate()
					.split("-");
			String[] spareFlag = StringUtils.isEmpty(cross.getSpareFlag()) ? null
					: cross.getSpareFlag().split("-");
			String[] highlineFlag = StringUtils
					.isEmpty(cross.getHighlineFlag()) ? null : cross
					.getHighlineFlag().split("-");
			String[] commonlineRule = StringUtils.isEmpty(cross
					.getCommonlineRule()) ? null : cross.getCommonlineRule()
					.split("-");
			String[] highlineRule = StringUtils
					.isEmpty(cross.getHighlineRule()) ? null : cross
					.getHighlineRule().split("-");

			String[] appointWeek = StringUtils.isEmpty(cross.getAppointWeek()) ? null
					: cross.getAppointWeek().split("-");
			String[] appointDay = StringUtils.isEmpty(cross.getAppointDay()) ? null
					: cross.getAppointDay().split("-");

			String[] trains = crossName.split("-");
			LinkedList<CrossTrainInfo> crossTrains = new LinkedList<CrossTrainInfo>();
			CrossTrainInfo train = null;
			for (int i = 0; i < trains.length; i++) {
				train = new CrossTrainInfo();
				train.setTrainSort(i + 1);
				train.setCrossId(cross.getCrossId());
				train.setTrainNbr(trains[i]);
				try {
					//
					if (alertNateTrains != null) {
						train.setAlertNateTrainNbr(alertNateTrains[i]);
					}
					//
					if (alertNateDate != null) {
						if (alertNateDate.length == 1) {
							train.setAlertNateTime(alertNateDate[0]
									+ " 00:00:00");
						} else {
							train.setAlertNateTime(alertNateDate[i]
									+ " 00:00:00");
						}
					}
					//
					if (spareFlag != null) {
						if (spareFlag.length == 1) {
							train.setSpareFlag(Integer.parseInt(spareFlag[0]));
						} else {
							train.setSpareFlag(Integer.parseInt(spareFlag[i]));
						}
					}

					if (highlineFlag != null) {
						if (highlineFlag.length == 1) {
							train.setHighlineFlag(Integer
									.parseInt(highlineFlag[0]));
						} else {
							train.setHighlineFlag(Integer
									.parseInt(highlineFlag[i]));
						}
					}

					if (commonlineRule != null) {
						if (commonlineRule.length == 1) {
							train.setCommonLineRule(Integer
									.parseInt(commonlineRule[0]));
						} else {
							train.setCommonLineRule(Integer
									.parseInt(commonlineRule[i]));
						}
					}

					if (highlineRule != null) {
						if (highlineRule.length == 1) {
							train.setHighlineRule(Integer
									.parseInt(highlineRule[0]));
						} else {
							train.setHighlineRule(Integer
									.parseInt(highlineRule[i]));
						}
					}

					if (appointDay != null) {
						if (appointDay.length == 1) {
							train.setAppointDay(appointDay[0]);
						} else {
							train.setAppointDay(appointDay[i]);
						}
					}

					if (appointWeek != null) {
						if (appointWeek.length == 1) {
							train.setAppointWeek(appointWeek[0]);
						} else {
							train.setAppointWeek(appointWeek[i]);
						}
					}

				} catch (Exception e) {
					logger.error("创建列车信息出错:", e);
				}

				crossTrains.add(train);
			}
			// 获取列车时刻表的起始和终到站
			String routeBureauShortNames = "";
			boolean isSet = false;
			List<Map<String, Object>> allMapList = new ArrayList<Map<String, Object>>();

			boolean flag = false;// 是否直接通过shortBureau来确定

			Map<String, Object> trains0Dxx0 = getBureauFor0Dxxx50301(
					crossTrains, cross.getChartId());
			allMapList.add(trains0Dxx0);
			if (getBureauFor0Dxxx50301_2(crossTrains, cross.getChartId())) {
				List<PlanTrain> PlanTrainlist = ((List<PlanTrain>) trains0Dxx0
						.get(trains0Dxx0.size() - 1 + ""));
				if (PlanTrainlist.size() == 1) {
				} else {
					int acount = 0;
					for (Map<String, Object> map : allMapList) {// map==trains0Dxx
						for (int a = 0; a < map.size(); a++) {
							List<PlanTrain> planTrain = (List<PlanTrain>) map
									.get(a + "");
							for (PlanTrain oTrain : planTrain) {
								if (user.getBureauShortName().equals(
										oTrain.getRoutingBureauShortName())) {
									acount++;
								}
							}
						}
					}
					if (acount == crossTrains.size()) {// 说明 交路中得每一个车train
														// 都有一个是本局的车
						// 那么就确定第一个车了 就是第一个 本局的车
						flag = true;
					} else {
						throw new Exception(
								"this corss is invalid! (user-defined exception)");
					}
				}
			}

			if (!flag) {// 不是50301 的情况
				for (int i = 0; i < crossTrains.size(); i++) {
					CrossTrainInfo crossTrain = crossTrains.get(i);
					// getBureauFor0Dxxx(crossTrains,crossTrain,i);//0D2704-D2703-D2704-0D2703
					// 根据“后车的始发站”或“前车的终到站”自动匹配
					if (i == 0) {

						PlanTrain unionTrain = new PlanTrain();
						String startBureau = "";
						String startStation = "";

						// Map<n,list<PlanTrain>> n为（0.1.2....）， N为唯一确定车的序号

						Map<String, Object> trains0Dxx = getBureauFor0Dxxx(
								crossTrains, cross.getChartId(), i);

						if (null != trains0Dxx.get(trains0Dxx.size() - 1 + "")) {
							unionTrain = ((List<PlanTrain>) trains0Dxx
									.get(trains0Dxx.size() - 1 + "")).get(0);// 需要参照该车的始发站局
							startBureau = unionTrain.getStartBureau();
							startStation = unionTrain.getStartStn();
						}
						// PlanTrain unionTrain = ((List<PlanTrain>) trains0Dxx
						// .get(trains0Dxx.size() - 1 + "")).get(0);//
						// 需要参照该车的始发站局
						// String startBureau = unionTrain.getStartBureau();
						// String startStation = unionTrain.getStartStn();
						// 如果第一个站查询结果为大于1
						if (trains0Dxx.size() > 1) {
							for (int k = trains0Dxx.size() - 2; k >= 0; k--) {
								if (k == trains0Dxx.size() - 2) {
									// 取map.get(n)的始发站，作为参照
									trainInfoFromPainfirst(crossTrains.get(k),
											cross.getChartId(), startStation);
								} else {
									// 取crossTrains.get(k+1)的始发站，作为参照
									trainInfoFromPainfirst(crossTrains.get(k),
											cross.getChartId(), crossTrains
													.get(k + 1).getStartStn());
								}
							}
						} else {// 如果第一个站查询结果为只有一个车
							trainInfoFromPain(crossTrain, cross.getChartId(),
									"");// 说明 交路第一个车是唯一的 不需要参照
						}
					} else {
						// 直接去crossTrains得上一个元素的终到信息
						String bureau = "";
						String endStn = crossTrains.get(i - 1).getEndStn();
						// if (crossTrains.get(i - 1).getEndBureau() != null) {
						// endStn =
						// bureauDao.getShortBureauNameByCode(crossTrains
						// .get(i - 1).getEndStn());
						// }
						trainInfoFromPain(crossTrain, cross.getChartId(),
								endStn);// 这里不用查询数据库
										// 直接去crossTrains得上一个元素的终到信息
					}
					// 设置交路的开始日期和结束日期
					if (cross.getCrossName().startsWith(
							crossTrain.getTrainNbr())) {
						if (!isSet) {
							cross.setStartBureau(crossTrain.getStartBureau());
							cross.setCrossStartDate(crossTrain
									.getAlertNateTime().substring(0, 8));
							isSet = true;
						}
					}
					routeBureauShortNames += crossTrain
							.getRouteBureauShortNames() != null ? crossTrain
							.getRouteBureauShortNames() : "";
				}
			}// if(!flag) end
			else {// 是 50301的情况

				// crossTrains
				Map<String, Object> trains0Dxx = getBureauFor0Dxxx(crossTrains,
						cross.getChartId(), 0);// 确定该路局的 车
				List<PlanTrain> ootrain = (List<PlanTrain>) trains0Dxx
						.get(0 + "");// 第一个车次 对应所有同名的车
				PlanTrain bureauFirsrPlanTrain = null;
				for (PlanTrain planTrain : ootrain) {
					if (user.getBureauShortName().equals(
							planTrain.getStartBureau())) {// 找到
					// if(user.getBureauShortName().equals(planTrain.getRoutingBureauShortName())){//找到
						bureauFirsrPlanTrain = planTrain;
						crossTrains = trainInfoFromPainfirst2(crossTrains,
								cross.getChartId(), planTrain.getStartBureau());
						// trainInfoFromPainfirst2(crossTrains.get(0),cross.getChartId(),
						// planTrain.getEndStn());
						// break;

					}
				}
				// 设置交路的开始日期和结束日期
				if (cross.getCrossName().startsWith(
						crossTrains.get(0).getTrainNbr())) {
					if (!isSet) {
						cross.setStartBureau(crossTrains.get(0)
								.getStartBureau());
						cross.setCrossStartDate(alertNateDate[0]);
						isSet = true;
					}
				}
				for (int i = 0; i < crossTrains.size(); i++) {
					train = crossTrains.get(i);
					train.setTrainSort(i + 1);
					train.setCrossId(cross.getCrossId());
					// train.setTrainNbr(trains[i]);
					try {
						//
						if (alertNateTrains != null) {
							train.setAlertNateTrainNbr(alertNateTrains[i]);
						}
						//
						if (alertNateDate != null) {
							if (alertNateDate.length == 1) {
								train.setAlertNateTime(alertNateDate[0]
										+ " 00:00:00");
							} else {
								train.setAlertNateTime(alertNateDate[i]
										+ " 00:00:00");
							}
						}
						//
						if (spareFlag != null) {
							if (spareFlag.length == 1) {
								train.setSpareFlag(Integer
										.parseInt(spareFlag[0]));
							} else {
								train.setSpareFlag(Integer
										.parseInt(spareFlag[i]));
							}
						}

						if (highlineFlag != null) {
							if (highlineFlag.length == 1) {
								train.setHighlineFlag(Integer
										.parseInt(highlineFlag[0]));
							} else {
								train.setHighlineFlag(Integer
										.parseInt(highlineFlag[i]));
							}
						}

						if (commonlineRule != null) {
							if (commonlineRule.length == 1) {
								train.setCommonLineRule(Integer
										.parseInt(commonlineRule[0]));
							} else {
								train.setCommonLineRule(Integer
										.parseInt(commonlineRule[i]));
							}
						}

						if (highlineRule != null) {
							if (highlineRule.length == 1) {
								train.setHighlineRule(Integer
										.parseInt(highlineRule[0]));
							} else {
								train.setHighlineRule(Integer
										.parseInt(highlineRule[i]));
							}
						}

						if (appointDay != null) {
							if (appointDay.length == 1) {
								train.setAppointDay(appointDay[0]);
							} else {
								train.setAppointDay(appointDay[i]);
							}
						}

						if (appointWeek != null) {
							if (appointWeek.length == 1) {
								train.setAppointWeek(appointWeek[0]);
							} else {
								train.setAppointWeek(appointWeek[i]);
							}
						}

					} catch (Exception e) {
						logger.error("创建列车信息出错:", e);
					}

					// crossTrains.add(train);
					crossTrains.set(i, train);

					routeBureauShortNames += train.getRouteBureauShortNames() != null ? train
							.getRouteBureauShortNames() : "";
				}
			}// if(!flag) else end

			if (!"".equals(routeBureauShortNames)) {
				String rbsPY = "";
				String py = "";
				for (int i = 0; i < routeBureauShortNames.length(); i++) {
					py = commonService.getStationPy(routeBureauShortNames
							.substring(i, i + 1));
					if ((py != null && rbsPY.indexOf(py) > -1) || py == null) {
						continue;
					}
					rbsPY += py;
				}
				cross.setRelevantBureau(rbsPY);
			}

			// 设置列车的间隔时间
			setDayGapForTrains(crossTrains);
			// 设置列车的结束日期
			setEndDateForCross(crossTrains, cross);
			// 再进行一次计算，这次计算的目的是判断首日间隔是否需要增加天数
			if (-1 != createUnitCrossTrainUtilService.addFirstdayGap(cross)) {
				if (crossTrains.get(0).getTrainSort() == 1) {
					crossTrains.get(0).setDayGap(
							createUnitCrossTrainUtilService
									.addFirstdayGap(cross));
				} else {
					for (int i = 0; i < crossTrains.size(); i++) {
						if (crossTrains.get(i).getTrainSort() == 1) {
							crossTrains.get(i).setDayGap(
									createUnitCrossTrainUtilService
											.addFirstdayGap(cross));
							break;
						}
					}
				}
			}
			// -------------------------(2)----------------------------------------------------------------
			LinkedList<CrossTrainInfo> crossSpareTrains = new LinkedList<CrossTrainInfo>();
			if (crossSpareNames != null) {
				for (int i = 0; i < crossSpareNames.length; i++) {
					try {
						train = new CrossTrainInfo();
						train.setCrossId(cross.getCrossId());
						train.setTrainSort(i + 1);
						train.setTrainNbr(crossSpareNames[i]);
						train.setSpareApplyFlage(1);
						train.setSpareFlag(2);
						//
						if (alertNateDate != null) {
							if (alertNateDate.length == 1) {
								train.setAlertNateTime(alertNateDate[0]
										+ " 00:00:00");
							} else {
								train.setAlertNateTime(alertNateDate[i]
										+ " 00:00:00");
							}
						}

						if (highlineFlag != null) {
							if (highlineFlag.length == 1) {
								train.setHighlineFlag(Integer
										.parseInt(highlineFlag[0]));
							} else {
								train.setHighlineFlag(Integer
										.parseInt(highlineFlag[i]));
							}
						}

					} catch (Exception e) {
						logger.error("创建列车信息出错:", e);
					}
					crossSpareTrains.add(train);
				}

				for (int i = 0; i < crossSpareTrains.size(); i++) {
					CrossTrainInfo crossTrain = crossTrains.get(i);
					List<Map<String, Object>> allMapList2 = new ArrayList<Map<String, Object>>();

					boolean flag2 = false;// 是否直接通过shortBureau来确定

					Map<String, Object> trains0Dxx = getBureauFor0Dxxx50301(
							crossTrains, cross.getChartId());
					allMapList2.add(trains0Dxx0);
					if (getBureauFor0Dxxx50301_2(crossTrains,
							cross.getChartId())) {
						List<PlanTrain> PlanTrainlist = ((List<PlanTrain>) trains0Dxx0
								.get(trains0Dxx0.size() - 1 + ""));
						if (PlanTrainlist.size() == 1) {
						} else {
							int acount = 0;
							for (Map<String, Object> map : allMapList2) {// map==trains0Dxx
								for (int a = 0; a < map.size(); a++) {
									List<PlanTrain> planTrain = (List<PlanTrain>) map
											.get(a + "");
									for (PlanTrain oTrain : planTrain) {
										if (user.getBureauShortName()
												.equals(oTrain
														.getRoutingBureauShortName())) {
											acount++;
										}
									}
								}
							}
							if (acount == crossTrains.size()) {// 说明
																// 交路中得每一个车train
																// 都有一个是本局的车
								// 那么就确定第一个车了 就是第一个 本局的车
								flag2 = true;
							} else {
								throw new Exception(
										"this corss is invalid! (user-defined exception)");
							}
						}
					}
					if (!flag2) {
						if (i == 0) {

							PlanTrain unionTrain = ((List<PlanTrain>) trains0Dxx
									.get(trains0Dxx.size() - 1 + "")).get(0);// 需要参照该车的始发站局
							// String startBureau =
							// unionTrain.getStartBureauShortName();
							String startStation = unionTrain.getStartStn();
							// 如果第一个站查询结果为大于1
							if (trains0Dxx.size() > 1) {
								for (int k = trains0Dxx.size() - 2; k >= 0; k--) {
									// List<PlanTrain> ODtrains =
									// (List<PlanTrain>) trains0Dxx.get(k + "");
									// for (int j = 0; j < ODtrains.size(); j++)
									// {
									if (k == trains0Dxx.size() - 2) {
										// 取map.get(n)的始发站，作为参照
										trainInfoFromPainfirst(
												crossTrains.get(k),
												cross.getChartId(),
												startStation);
									} else {
										// 取crossTrains.get(k+1)的始发站，作为参照
										trainInfoFromPainfirst(
												crossTrains.get(k),
												cross.getChartId(), crossTrains
														.get(k + 1)
														.getStartStn());
									}
									// }
								}
							} else {// 如果第一个站查询结果为只有一个车
								trainInfoFromPain(crossTrain,
										cross.getChartId(), "");// 说明 交路第一个车是唯一的
																// 不需要参照
							}
						} else {
							// trainInfoFromPain(crossTrain,
							// cross.getChartId(),bureauDao.getShortBureauNameByCode(crossTrains.get(i-1).getEndBureau()));
							String bureau = "";
							String endStn = crossTrains.get(i - 1).getEndStn();
							// if (crossTrains.get(i - 1).getEndBureau() !=
							// null) {
							// bureau = bureauDao
							// .getShortBureauNameByCode(crossTrains.get(
							// i - 1).getEndBureau());
							// }
							trainInfoFromPain(crossTrain, cross.getChartId(),
									endStn);// 这里不用查询数据库
											// 直接去crossTrains得上一个元素的终到信息
						}

					}// if (!flag2) end
					else {

						Map<String, Object> trains0Dxx_flag2 = getBureauFor0Dxxx(
								crossTrains, cross.getChartId(), 0);// 确定该路局的 车
						List<PlanTrain> ootrain = (List<PlanTrain>) trains0Dxx_flag2
								.get(0 + "");// 第一个车次 对应所有同名的车
						PlanTrain bureauFirsrPlanTrain = null;
						for (PlanTrain planTrain : ootrain) {
							if (user.getBureauShortName().equals(
									planTrain.getStartBureau())) {// 找到
								// if(user.getBureauShortName().equals(planTrain.getRoutingBureauShortName())){//找到
								bureauFirsrPlanTrain = planTrain;
								crossTrains = trainInfoFromPainfirst2(
										crossTrains, cross.getChartId(),
										planTrain.getStartBureau());
								// trainInfoFromPainfirst2(crossTrains.get(0),cross.getChartId(),
								// planTrain.getEndStn());
								// break;

							}
						}
						// 设置交路的开始日期和结束日期
						if (cross.getCrossName().startsWith(
								crossTrains.get(0).getTrainNbr())) {
							if (!isSet) {
								cross.setStartBureau(crossTrains.get(0)
										.getStartBureau());
								cross.setCrossStartDate(alertNateDate[0]);
								isSet = true;
							}
						}
						for (int j = 0; j < crossTrains.size(); j++) {
							train = crossTrains.get(j);
							train.setTrainSort(j + 1);
							train.setCrossId(cross.getCrossId());
							// train.setTrainNbr(trains[i]);
							try {
								//
								if (alertNateTrains != null) {
									train.setAlertNateTrainNbr(alertNateTrains[j]);
								}
								//
								if (alertNateDate != null) {
									if (alertNateDate.length == 1) {
										train.setAlertNateTime(alertNateDate[0]
												+ " 00:00:00");
									} else {
										train.setAlertNateTime(alertNateDate[j]
												+ " 00:00:00");
									}
								}
								//
								if (spareFlag != null) {
									if (spareFlag.length == 1) {
										train.setSpareFlag(Integer
												.parseInt(spareFlag[0]));
									} else {
										train.setSpareFlag(Integer
												.parseInt(spareFlag[j]));
									}
								}

								if (highlineFlag != null) {
									if (highlineFlag.length == 1) {
										train.setHighlineFlag(Integer
												.parseInt(highlineFlag[0]));
									} else {
										train.setHighlineFlag(Integer
												.parseInt(highlineFlag[j]));
									}
								}

								if (commonlineRule != null) {
									if (commonlineRule.length == 1) {
										train.setCommonLineRule(Integer
												.parseInt(commonlineRule[0]));
									} else {
										train.setCommonLineRule(Integer
												.parseInt(commonlineRule[j]));
									}
								}

								if (highlineRule != null) {
									if (highlineRule.length == 1) {
										train.setHighlineRule(Integer
												.parseInt(highlineRule[0]));
									} else {
										train.setHighlineRule(Integer
												.parseInt(highlineRule[j]));
									}
								}

								if (appointDay != null) {
									if (appointDay.length == 1) {
										train.setAppointDay(appointDay[0]);
									} else {
										train.setAppointDay(appointDay[j]);
									}
								}

								if (appointWeek != null) {
									if (appointWeek.length == 1) {
										train.setAppointWeek(appointWeek[0]);
									} else {
										train.setAppointWeek(appointWeek[j]);
									}
								}

							} catch (Exception e) {
								logger.error("创建列车信息出错:", e);
							}
							crossTrains.set(j, train);
						}
					}
					// 设置交路的开始日期和结束日期
					if (cross.getCrossName().startsWith(
							crossTrain.getTrainNbr())) {
						cross.setStartBureau(crossTrain.getStartBureau());

					}
				}

				setDayGapForTrains(crossSpareTrains);

				// 设置车次的开始和结束日期
				cross.setCrossStartDate(alertNateDate[0]);
				setStartAndEndTime(crossSpareTrains, cross);

				crossTrains.addAll(crossSpareTrains);
			}

			logger.debug(this.cross.getCrossName() + "==crossTrains="
					+ crossTrains.size());
			return crossTrains;
		}

	}

	public List<OriginalCross> getOriginalCrossInfoByParam(
			Map<String, Object> paramMap) {
		return baseDao.selectListBySql(
				Constants.ORIGINALCROSSDAO_GET_ORIGINALCROSS_INFO_FOR_PARAM,
				paramMap);
	}

	/**
	 * 通过crossid查询crossinfo信息
	 * 
	 * @param crossId
	 * @return
	 */
	public List<CrossInfo> getCrossInfoByParam(Map<String, Object> paramMap) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_CROSS_INFO_FOR_PARAM, paramMap);
	}

	public List<BaseTrainInfo> getBaseTrainInfoByParam(Map<String, String> map) {
		return this.baseDao.selectListBySql(
				Constants.CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM, map);
	}

	public List<BaseTrainInfo> getBaseTrainInfoByParamByEndStn(
			Map<String, String> map) {
		return this.baseDao
				.selectListBySql(
						Constants.CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM_BY_END_STN,
						map);
	}

	private void addPeriodTime(LinkedList<CrossTrainInfo> crossTrains) {
		if (!crossTrains.isEmpty()) {
			for (CrossTrainInfo crossTrainInfo : crossTrains) {
				// Map<String, Object> PeriodTimd =
				// crossMybatisDao.getPeriodTimdByCrossTrainId(crossTrainInfo.getBaseTrainId());
				System.out.println("1555::" + crossTrainInfo.getBaseTrainId());
				System.out.println("1555::" + null != crossTrainInfo
						.getBaseTrainId() ? crossTrainInfo.getBaseTrainId()
						: "");
				Map<String, Object> PeriodTimd = crossMybatisDao
						.getPeriodTimdByCrossTrainId(null != crossTrainInfo
								.getBaseTrainId() ? crossTrainInfo
								.getBaseTrainId() : "");
				crossTrainInfo.setPeriodSourceTime("null".equals(MapUtils
						.getString(PeriodTimd, "SOURCE_TIME")) ? "" : MapUtils
						.getString(PeriodTimd, "SOURCE_TIME"));
				crossTrainInfo.setPeriodTargetTime("null".equals(MapUtils
						.getString(PeriodTimd, "TARGET_TIME")) ? "" : MapUtils
						.getString(PeriodTimd, "TARGET_TIME"));
			}
		}
	}

	/**
	 * 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
	 * 
	 * @param crossTrains
	 * @param crossTrain
	 * @param i
	 */
	private Map<String, Object> getBureauFor0Dxxx50301(
			LinkedList<CrossTrainInfo> crossTrains, String chartId) {
		// 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossTrains", crossTrains);

		return getBaseTrainStartBureauByTrainNbr50301(map);// 是不是每个车都不能确定的 交路
															// false 某个唯一 ，true
															// 都不唯一
	}

	private boolean getBureauFor0Dxxx50301_2(
			LinkedList<CrossTrainInfo> crossTrains, String chartId) {
		// 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossTrains", crossTrains);

		return getBaseTrainStartBureauByTrainNbr50301_2(map);// 是不是每个车都不能确定的 交路
																// false 某个唯一
																// ，true 都不唯一
	}

	/**
	 * @param param
	 *            (chartId,crossTrains,trainNbr)
	 * @return 从0-N个车次的Map<n,list<PlanTrain>> n为（0.1.2....）， N为唯一确定车的序号
	 */
	public Map<String, Object> getBaseTrainStartBureauByTrainNbr50301(
			Map<String, Object> param) { // 为 50301 - 50304 每个车 不唯一，每个车 都有济南
		Map<String, Object> map = new HashMap<String, Object>();
		LinkedList<CrossTrainInfo> crossTrains = (LinkedList<CrossTrainInfo>) param
				.get("crossTrains");
		for (int i = 0; i < crossTrains.size(); i++) {
			List<PlanTrain> jbtTrains = getPlanTrainFromJbtByTrainNbr_chartId(
					param.get("chartId").toString(), crossTrains.get(i)
							.getTrainNbr());
			map.put(i + "", jbtTrains);
		}
		return map;
	}

	private List<PlanTrain> getPlanTrainFromJbtByTrainNbr_chartId(
			String chartId, String trainNbr) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("trainNbr", trainNbr);
		return baseDao.selectListBySql(
				Constants.TRAININFO_GETTRAININFO_FROM_JBT_BY_CHARTID_TRAINNBR,
				map);
	}

	/**
	 * 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
	 * 
	 * @param crossTrains
	 * @param crossTrain
	 * @param i
	 */
	private Map<String, Object> getBureauFor0Dxxx(
			LinkedList<CrossTrainInfo> crossTrains, String chartId, int i) {
		// 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossTrains", crossTrains);

		if (i == 0) {// 后车的始发站
			// 去数据库查询 后车的始发站
			map.put("trainNbr", crossTrains.get(i + 1).getTrainNbr());
			return getBaseTrainStartBureauByTrainNbr(map);
		}
		return null;
	}

	/**
	 * @param param
	 *            (chartId,crossTrains,trainNbr)
	 * @return 从0-N个车次的Map<n,list<PlanTrain>> n为（0.1.2....）， N为唯一确定车的序号
	 */
	public Map<String, Object> getBaseTrainStartBureauByTrainNbr(
			Map<String, Object> param) {
		// 这里可能第一第二第三。。个车都是0Dxxxx需要参照交路中第一个在m_trainline_temp中唯一的车的始发局，
		Map<String, Object> map = new HashMap<String, Object>();
		LinkedList<CrossTrainInfo> crossTrains = (LinkedList<CrossTrainInfo>) param
				.get("crossTrains");
		for (int i = 0; i < crossTrains.size(); i++) {
			List<PlanTrain> jbtTrains = getPlanTrainFromJbtByTrainNbr_chartId(
					param.get("chartId").toString(), crossTrains.get(i)
							.getTrainNbr());
			if (jbtTrains.size() > 1) {
				// 说明在jbt中有多个，查询下一个，该多个记录保存在map中，序号为1,2,3
				map.put(i + "", jbtTrains);
			}
			if (jbtTrains.size() == 1) {
				// 第一次长度为1 说明是找到第一个唯一车次
				map.put(i + "", jbtTrains);
				break;
			}
		}
		return map;
	}

	public boolean getBaseTrainStartBureauByTrainNbr50301_2(
			Map<String, Object> param) { // 为 50301 - 50304 每个车 不唯一，每个车 都有济南
		Map<String, Object> map = new HashMap<String, Object>();
		LinkedList<CrossTrainInfo> crossTrains = (LinkedList<CrossTrainInfo>) param
				.get("crossTrains");
		for (int i = 0; i < crossTrains.size(); i++) {
			List<PlanTrain> jbtTrains = getPlanTrainFromJbtByTrainNbr_chartId(
					param.get("chartId").toString(), crossTrains.get(i)
							.getTrainNbr());
			map.put(i + "", jbtTrains);
			if (jbtTrains.size() == 1) {
				return false;// 不是每个车都不能确定的 交路
			}
		}
		return true;
	}

	/**
	 * 根据crossIds删除表original_cross表中数据
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteOriginalCrossInfoForCorssIds(String[] crossIds)
			throws Exception {
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossIds.length;
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIds[i]).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());

		return baseDao
				.deleteBySql(
						Constants.ORIGINALCROSSDAO_DELETE_ORIGINALCROSS_INFO_FOR_CROSSIDS,
						reqMap);
	}

	/**
	 * 根据originalCrossId数组，删除其所有关联信息(不删除originalCross本身)
	 * 
	 * @param crossIdsArray
	 *            originalCrossId数组
	 * @throws Exception
	 */
	public void deleteOriginalCrossRelevant(String[] crossIdsArray)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> crossIdsList = new ArrayList<String>();
		for (int i = 0; i < crossIdsArray.length; i++) {
			crossIdsList.add(crossIdsArray[i]);

			/** 下面代码复制的保存,由于对业务不熟悉,所以只能将删除unit_cross代码全拷过来,if里的删除无效 **/
			// 通过baseCrossId查询unitCross信息
			List<CrossInfo> unitCrossInfoList = crossService
					.getUnitCrossInfosForCrossId(crossIdsArray[i]);
			// 删除unit_cross_train中的数据
			if (unitCrossInfoList != null && unitCrossInfoList.size() > 0) {
				List<String> unitCrossIdsList = new ArrayList<String>();
				for (CrossInfo cross : unitCrossInfoList) {
					unitCrossIdsList.add(cross.getUnitCrossId());
				}
				// 删除unit_cross_train中的数据
				crossService
						.deleteUnitCrossInfoTrainForCorssIds(unitCrossIdsList);
				// 删除unit_cross中的数据
				crossService.deleteUnitCrossInfoForCorssIds(unitCrossIdsList);
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
		int count = crossService.deleteCrossInfoForCorssIds(crossIdsList);
	}

}
