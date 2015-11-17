package org.railway.com.trainplan.service;

import java.beans.IntrospectionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.ExcelUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BaseCrossTrainInfo;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.railway.com.trainplan.entity.PlanCrossInfo;
import org.railway.com.trainplan.entity.PlanTrain;
import org.railway.com.trainplan.entity.SubCrossInfo;
import org.railway.com.trainplan.entity.TrainActivityPeriod;
import org.railway.com.trainplan.entity.TrainLineInfo;
import org.railway.com.trainplan.entity.UnitCrossTrainInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.railway.com.trainplan.repository.mybatis.BureauDao;
import org.railway.com.trainplan.repository.mybatis.CrossMybatisDao;
import org.railway.com.trainplan.repository.mybatis.UnitCrossDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CrossService {
	private static final Logger logger = Logger.getLogger(CommonService.class);
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMdd");

	@Autowired
	private CommonService commonService;

	@Autowired
	private CreateUnitCrossTrainUtilService createUnitCrossTrainUtilService;

//	@Value("#{restConfig['SERVICE_URL']}")
//	private String restUrl;

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private CrossMybatisDao crossMybatisDao;

	@Autowired
	private UnitCrossDao UnitCrossDao;
	@Autowired
	private BureauDao bureauDao;

	public static void main(String[] args) throws IOException,
			IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ParseException {
		// InputStream is = new FileInputStream(
		// "C:\\Users\\Administrator\\Desktop\\work\\交路相关\\对数表模板1.xls");
		//
		// CrossService a = new CrossService();
		// System.out.println(StringUtils.isEmpty(""));
		Date date = dateFormat.parse("20140518");
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 4 - 1);

		// System.out.println(dateFormat.format(calendar.getTime()));
		// a.actionExcel(is);
		// System.out.println("G11(".substring(0,"G11(".indexOf('(')));
	}

	/**
	 * 对表plan_cross批量插入数据
	 * 
	 * @param list
	 * @return
	 */
	public int addPlanCrossInfo(List<PlanCrossInfo> list) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("trainCrossList", list);
		int count = baseDao.insertBySql(Constants.CROSSDAO_ADD_PLAN_CROSS_INFO,
				reqMap);
		return count;
	}

	public List<BaseTrainInfo> getBaseTrainInfoByParam(Map<String, String> map) {
		return this.baseDao.selectListBySql(
				Constants.CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM, map);
	}
	public List<BaseTrainInfo> getBaseTrainInfoByParamByEndStn(Map<String, String> map) {
		return this.baseDao.selectListBySql(
				Constants.CROSSDAO_GET_BASETRAIN_INFO_FOR_PARAM_BY_END_STN, map);
	}

	/**
	 * @param param
	 *            (chartId,crossTrains,trainNbr)
	 * @return 从0-N个车次的Map<n,list<PlanTrain>> n为（0.1.2....）， N为唯一确定车的序号
	 */
	public Map getBaseTrainStartBureauByTrainNbr(Map param) {
		// map.put("chartId", chartId);
		// map.put("crossTrains", crossTrains);
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
		// return
		// this.baseDao.selectListBySql(Constants.CROSSDAO_GET_BASETRAIN_STARTBUREAU_BY_TRAINNBR,
		// trainNbr);
	}
	/**
	 * @param param
	 *            (chartId,crossTrains,trainNbr)
	 * @return 从0-N个车次的Map<n,list<PlanTrain>> n为（0.1.2....）， N为唯一确定车的序号
	 */
	public Map getBaseTrainStartBureauByTrainNbr50301(Map param) {  //为  50301 - 50304  每个车 不唯一，每个车 都有济南
		// map.put("chartId", chartId);
		// map.put("crossTrains", crossTrains);
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
	public boolean getBaseTrainStartBureauByTrainNbr50301_2(Map param) {  //为  50301 - 50304  每个车 不唯一，每个车 都有济南
		// map.put("chartId", chartId);
		// map.put("crossTrains", crossTrains);
		Map<String, Object> map = new HashMap<String, Object>();
		LinkedList<CrossTrainInfo> crossTrains = (LinkedList<CrossTrainInfo>) param
				.get("crossTrains");
		for (int i = 0; i < crossTrains.size(); i++) {
			List<PlanTrain> jbtTrains = getPlanTrainFromJbtByTrainNbr_chartId(
					param.get("chartId").toString(), crossTrains.get(i)
					.getTrainNbr());
			map.put(i + "", jbtTrains);
			if(jbtTrains.size()==1){
				return false;//不是每个车都不能确定的 交路
			}
		}
		return true;
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

	public String getBaseTrainEndBureauByTrainNbr(String trainNbr) {
		return (String) this.baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_BASETRAIN_ENDBUREAU_BY_TRAINNBR,
				trainNbr);
	}

	public BaseTrainInfo getBaseTrainInfoOneByParam(Map<String, Object> reqMap) {
		return (BaseTrainInfo) this.baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_BASETRAIN_INFO_ONE_FOR_PARAM, reqMap);
	}

	/**
	 * 通过plan_cross_id查询plancross信息
	 * 
	 * @param planCrossId
	 *            表plan_cross中主键
	 * @return
	 */
	public PlanCrossInfo getPlanCrossInfoForPlanCrossId(String planCrossId) {
		return (PlanCrossInfo) this.baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_PLANCROSSINFO_FOR_PLANCROSSID,
				planCrossId);
	}

	// 根据交路名和基本图ID，删除已经存在的UNITCROSS和UNITCROSSTRAIN
	public int deleteUnitCrossAndCrossTrain(Map<String, String> reqMap)
			throws Exception {
		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_UNITCROSS_UNITCROSSTRAIN, reqMap);
	}

	/**
	 * 通过plan_cross_id查询plancross信息
	 * 
	 * @param planCrossId
	 *            表plan_cross中主键
	 * @return
	 */
	public PlanCrossInfo getPlanCrossInfoByPlanCrossId(String planCrossId) {
		return (PlanCrossInfo) this.baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID,
				planCrossId);
	}

	/**
	 * 通过plan_cross_id查询plancross信息
	 * 
	 * @param planCrossId
	 *            表plan_cross中主键
	 * @return
	 */
	public PlanCrossInfo getPlanCrossInfoByPlanCrossIdVehicle(String planCrossId) {
		return (PlanCrossInfo) this.baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID_VEHICLE,
				planCrossId);
	}

	/**
	 * 通过plan_cross_id查询plancross信息
	 * 
	 * @param planCrossId
	 *            表plan_cross中主键
	 * @return
	 */
	public PlanCrossInfo getPlanCrossInfoByPlanCrossIdVehicleBy2(
			String planCrossId) {
		return (PlanCrossInfo) this.baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID_VEHICLEBY2,
				planCrossId);
	}

	/**
	 * 通过plan_cross_id查询plancross信息
	 * 
	 * @param planCrossId
	 *            表plan_cross中主键
	 * @return
	 */
	public PlanCrossInfo getPlanCrossInfoByPlanCrossIdVehicleSearch(
			String planCrossId) {
		return (PlanCrossInfo) this.baseDao
				.selectOneBySql(
						Constants.CROSSDAO_GET_PLANCROSSINFO_BY_PLANCROSSID_VEHICLESEARCH,
						planCrossId);
	}

	/**
	 * @param chartId
	 *            方案id 通过chartId查询unit_cross信息
	 */
	public List<CrossInfo> getUnitCrossInfoForChartId(String chartId) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSSINFO_FOR_CHARTID, chartId);
	}

	/**
	 * @param baseCrossId
	 *            base_cross_id 通过baseCrossId查询unit_cross信息列表
	 */
	public List<CrossInfo> getUnitCrossInfoForBaseCrossId(String baseCrossId) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNITCROSS_INFO_FOR_BASECROSSID,
				baseCrossId);
	}

	/**
	 * 通过base_cross_id更新表base_cross
	 * 
	 * @param crossInfo
	 *            base_cross表对应的java对象
	 * @return 更新成功的数据条数
	 */
	public int updateBaseCross(CrossInfo crossInfo) {
		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_BASE_CROSS_INFO,
				crossInfo);
	}

	/**
	 * 通过base_cross_id更新表base_cross
	 * 
	 * @param crossInfo
	 *            base_cross表对应的java对象
	 * @return 更新成功的数据条数
	 */
	public int updatePlanCrossCheckType(String planCrossId, int checkType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planCrossId", planCrossId);
		map.put("checkType", checkType);
		return baseDao.insertBySql(
				Constants.CROSSDAO_UPDATE_PLAN_CROSS_CHECKTYPE, map);
	}

	/**
	 * 更新base_cross中的creat_unit_time字段的值
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateCrossUnitCreateTime(List<String> crossIds)
			throws Exception {

		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());

		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CREATETIME,
				reqMap);
	}

	/**
	 * 更新base_cross中的creat_unit_time字段的值为空
	 * 
	 * @param baseCrossIds
	 *            base_cross_id组成的数组
	 * @return
	 * @throws Exception
	 */
	public int updateCrossUnitCreateTimeToNull(List<String> baseCrossIds)
			throws Exception {

		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = baseCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(baseCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());

		return baseDao.insertBySql(
				Constants.CROSSDAO_UPDATE_CROSS_CREATETIME_TO_NULL, reqMap);
	}

	/**
	 * 更新base_cross中的creat_unit_time字段的值为空
	 * 
	 * @param 组成的数组
	 * @return
	 * @throws Exception
	 */
	public int updateCrossUnitCreateTimeToNullByUnitCrossId(
			List<String> unitCrossIds) throws Exception {

		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = unitCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());

		return baseDao.insertBySql(
				Constants.CROSSDAO_UPDATE_CROSS_CREATETIME_TO_NULL_BYUNITID,
				reqMap);
	}

	/**
	 * 更新base_cross中的check_time字段的值为空
	 * 
	 * @param 组成的数组
	 * @return
	 * @throws Exception
	 */
	public int updateCrossUnitCheckTimeToNullByUnitCrossId(
			List<String> unitCrossIds) throws Exception {

		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = unitCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());

		return baseDao.insertBySql(
				Constants.CROSSDAO_UPDATE_CROSS_CHECKTIME_TO_NULL_BYUNITID,
				reqMap);
	}

	/**
	 * 更新unit_cross中的creat_unit_time字段的值
	 * 
	 * @param unitCrossIds
	 * @return
	 * @throws Exception
	 */
	public int updateUnitCrossUnitCreateTime(List<String> unitCrossIds)
			throws Exception {

		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = unitCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());

		logger.info("update unit createTime :" + reqMap);
		return baseDao.insertBySql(
				Constants.CROSSDAO_UPDATE_Unit_CROSS_CREATETIME, reqMap);
	}

	/**
	 * 根据unitCrossIds删除表unit_cross_train表中数据
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteUnitCrossInfoTrainForCorssIds(List<String> unitCrossIds)
			throws Exception {
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = unitCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());

		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_UNIT_CROSS_INFO_TRAIN_FOR_CROSSIDS,
				reqMap);
	}

	public int updateUnitCrossInfoTrainForCorssIds(Map<String, Object> map)
			throws Exception {
		return baseDao.deleteBySql(
				Constants.CROSSDAO_UPDATE_UNIT_CROSS_INFO_TRAIN_FOR_CROSSIDS,
				map);
	}

	public int updateUnitCrossTrainByTrainId(Map<String, Object> map)
			throws Exception {
		return baseDao.deleteBySql(Constants.UPDATE_UNIT_CROSS_TRAIN_BY_TRAINID,map);
	}
	
	public Long getCountfromBaseCrossNonCheck(String[] crossids, String bureau)
			throws Exception {

		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossids.length;
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossids[i]).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());
		reqMap.put("bureau", bureau);

		List<Map<String, Object>> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_COUNT_BASECROSS_NONCHECK, reqMap);

		long count = 0;
		if (list != null) {
			// 只有一条数据
			Map<String, Object> map = list.get(0);
			count = ((BigDecimal) map.get("COUNT")).longValue();
		}
		return count;
	}

	public Long getCountfromUnitCrossNonCheck(List<String> crossNames,String baseChartId,
			String bureau) throws Exception {

		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossNames.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossNames.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("crossNames", bf.toString());
		reqMap.put("bureau", bureau);
		reqMap.put("baseChartId", baseChartId);

		List<Map<String, Object>> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_COUNT_UNITCROSS_NONCHECK, reqMap);

		long count = 0;
		if (list != null) {
			// 只有一条数据
			Map<String, Object> map = list.get(0);
			count = ((BigDecimal) map.get("COUNT")).longValue();
		}
		return count;
	}

	public List<Map<String, Object>> getUnitCrossTrainByMap(
			Map<String, Object> map) throws Exception {
		return crossMybatisDao.getUnitCrossTrainByMap(map);
	}

	public int updateUnitCrossByMap(Map<String, Object> map) throws Exception {
		return crossMybatisDao.updateUnitCrossByMap(map);
	}

	/**
	 * 根据unitCrossIds删除表unit_cross表中数据
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteUnitCrossInfoForCorssIds(List<String> unitCrossIds)
			throws Exception {
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = unitCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());

		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_UNIT_CROSS_INFO_FOR_CROSSIDS, reqMap);
	}

	/**
	 * 根据crossIds删除表base_cross_train表中数据
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteCrossInfoTrainForCorssIds(List<String> crossIds)
			throws Exception {
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());

		return baseDao
				.deleteBySql(
						Constants.CROSSDAO_DELETE_CROSS_INFO_TRAIN_FOR_CROSSIDS,
						reqMap);
	}

	/**
	 * 根据crossIds删除表base_cross表中数据
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int deleteCrossInfoForCorssIds(List<String> crossIds)
			throws Exception {
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());

		return baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_CROSS_INFO_FOR_CROSSIDS, reqMap);
	}

	/**
	 * 更新base_cross中的check_time字段的值
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateCorssCheckTime(List<String> crossIds) throws Exception {
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = crossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(crossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("baseCrossIds", bf.toString());

		return baseDao.insertBySql(Constants.CROSSDAO_UPDATE_CROSS_CHECKTIME,
				reqMap);

	}

	/**
	 * 更新unit_cross中的check_time字段的值
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateUnitCorssCheckTime(List<String> unitCrossIds)
			throws Exception {
		// 组装字符串
		StringBuffer bf = new StringBuffer();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		int size = unitCrossIds.size();
		for (int i = 0; i < size; i++) {
			bf.append("'").append(unitCrossIds.get(i)).append("'");
			if (i != size - 1) {
				bf.append(",");
			}
		}
		reqMap.put("unitCrossIds", bf.toString());

		logger.info("unitCrossIds==" + bf.toString());
		return baseDao.insertBySql(
				Constants.CROSSDAO_UPDATE_UNIT_CROSS_CHECKTIME, reqMap);

	}

	/**
	 * 更新unit_cross中的check_time字段的值为null
	 * 
	 * @param crossIds
	 * @return
	 * @throws Exception
	 */
	public int updateUnitCorssCheckTimeToNull(String unitCrossId)
			throws Exception {
		Map<String, Object> reqMap = new HashMap<String, Object>();

		reqMap.put("unitCrossId", unitCrossId);

		logger.info("unitCrossId==" + unitCrossId);
		return baseDao.insertBySql(
				Constants.CROSSDAO_UPDATE_UNIT_CROSS_CHECKTIMETONULL, reqMap);

	}

	/**
	 * 根据unitCrossid查询trainNbr
	 * 
	 * @param unitCrossId
	 * @return 以逗号分隔的trainNbr的字符串
	 */
	public List<Map<String, String>> getTrainNbrFromUnitCrossId(
			String unitCrossId) {
		List<Map<String, String>> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_TRAINNBR_FROM_UNIT_CROSS, unitCrossId);

		return list;
	}

	/**
	 * 生成交路单元
	 * 
	 * @param baseCrossId
	 * @throws Exception
	 */
	public void completeUnitCrossInfo(String baseCrossId) throws Exception {
		//
		CrossInfo crossInfo = getCrossInfoForCrossid(baseCrossId);
		// 根据crossInfo中交路名和基本图ID，删除原来的unit_cross和unit_cross_train
		// deleteUnitCrossAndCrossTrain()

		List<CrossTrainInfo> listCrossTrainInfo = getCrossTrainInfoForCrossid(baseCrossId);
		if (listCrossTrainInfo != null && listCrossTrainInfo.size() > 0) {
			// 取第一辆列车的始发站
			crossInfo.setStartStn(listCrossTrainInfo.get(0).getStartStn());
		}
		// logger.info("listCrossTrainInfo.size==" + listCrossTrainInfo.size());
		List<CrossInfo> list = prepareUnitCrossInfo(crossInfo);
		// for (CrossInfo crossInfo2 : list) {
		// System.out.println(crossInfo2.getCrossId());
		// logger.info("crossInfo2.getCrossId()==" + crossInfo2.getCrossId());
		// }
		// logger.info("list.size==" + list.size());
		if (list != null && list.size() > 0) {
			// /前面的规则没变这个地方直接使用前面的结果，如果前面的规则计算错误这里会有影响
			List<CrossInfo> result = new ArrayList<CrossInfo>();
			CrossInfo resultCorssInfo = new CrossInfo();
			BeanUtils.copyProperties(resultCorssInfo, crossInfo);
			resultCorssInfo.setCrossStartDate(crossInfo.getCrossStartDate());
			resultCorssInfo.setCrossEndDate(list.get(list.size() - 1)
					.getCrossEndDate());
	
			resultCorssInfo.setUnitCrossId(UUID.randomUUID().toString());
			result.add(resultCorssInfo);
			// logger.info("resultCorssInfo.getUnitCrossId()==" +
			// resultCorssInfo.getUnitCrossId());
			List<CrossTrainInfo> listCrossTrain = prepareUnitCrossTrainInfo(
					listCrossTrainInfo, list, resultCorssInfo.getUnitCrossId());

			if (listCrossTrain.size() != 0) {
				for (CrossTrainInfo crossTrainInfo : listCrossTrain) {
					if (crossTrainInfo.getAlertNateTime().toString().length() != 19) {
						crossTrainInfo.setAlertNateTime(crossTrainInfo
								.getAlertNateTime()
								.toString()
								.substring(
										0,
										crossTrainInfo.getAlertNateTime()
												.toString().length() - 2));
					}
				}
			}

			if (!resultCorssInfo.getAlterNateDate().contains("-")) {
				createUnitCrossTrainUtilService.trainsortSorting(
						listCrossTrain, result.get(0));
			}

			baseDao.insertBySql(Constants.CROSSDAO_ADD_UNIT_CROSS_INFO, result);

			if (listCrossTrain != null && listCrossTrain.size() > 0) {
				try {
					int b = baseDao.insertBySql(
							Constants.CROSSDAO_ADD_UNIT_CROSS_TRAIN_INFO,
							listCrossTrain);
				} catch (Exception e) {
					logger.error("CROSSDAO_ADD_UNIT_CROSS_TRAIN_INFO error : -----------",e);
				}
				
			}

		}

	}
	/**
	 * 生成交路单元
	 * 
	 * @param baseCrossId
	 * @throws Exception
	 */
	public void insertRepeatUnitCrossInfo(String chartId,String crossName,String baseCrossId) throws Exception {
		//
		CrossInfo crossInfo = getCrossInfoForCrossid(baseCrossId);
		// 根据crossInfo中交路名和基本图ID，删除原来的unit_cross和unit_cross_train
		// deleteUnitCrossAndCrossTrain()
		List<CrossInfo> unitCross = getUnitCrossInfosForCrossNameAndChartId(crossName, chartId);
		List<String> unitCrossIds = new ArrayList<String>();
		for (CrossInfo crossInfo2 : unitCross) {
			unitCrossIds.add(crossInfo2.getUnitCrossId());
		}
		//批量删除unit——cross——train
		deleteUnitCrossInfoTrainForCorssIds(unitCrossIds);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("crossName", crossName);
		map.put("chartId", chartId);
		baseDao.deleteBySql(Constants.CROSSDAO_DELETE_UNIT_CROSS_BY_CROSSNAMEANDCHARTID, map);
		
		
		List<CrossTrainInfo> listCrossTrainInfo = getCrossTrainInfoForCrossid(baseCrossId);
		if (listCrossTrainInfo != null && listCrossTrainInfo.size() > 0) {
			// 取第一辆列车的始发站
			crossInfo.setStartStn(listCrossTrainInfo.get(0).getStartStn());
		}
		// logger.info("listCrossTrainInfo.size==" + listCrossTrainInfo.size());
		List<CrossInfo> list = prepareUnitCrossInfo(crossInfo);
		// for (CrossInfo crossInfo2 : list) {
		// System.out.println(crossInfo2.getCrossId());
		// logger.info("crossInfo2.getCrossId()==" + crossInfo2.getCrossId());
		// }
		// logger.info("list.size==" + list.size());
		
		// /前面的规则没变这个地方直接使用前面的结果，如果前面的规则计算错误这里会有影响
		List<CrossInfo> result = new ArrayList<CrossInfo>();
		CrossInfo resultCorssInfo = new CrossInfo();
		BeanUtils.copyProperties(resultCorssInfo, crossInfo);
		resultCorssInfo.setCrossStartDate(crossInfo.getCrossStartDate());
		resultCorssInfo.setCrossEndDate(list.get(list.size() - 1)
				.getCrossEndDate());
		
		resultCorssInfo.setUnitCrossId(UUID.randomUUID().toString());
		result.add(resultCorssInfo);
		
		if (list != null && list.size() > 0) {
			// logger.info("resultCorssInfo.getUnitCrossId()==" +
			// resultCorssInfo.getUnitCrossId());
			List<CrossTrainInfo> listCrossTrain = prepareUnitCrossTrainInfo(
					listCrossTrainInfo, list, resultCorssInfo.getUnitCrossId());
			
			if (listCrossTrain.size() != 0) {
				for (CrossTrainInfo crossTrainInfo : listCrossTrain) {
					if (crossTrainInfo.getAlertNateTime().toString().length() != 19) {
						crossTrainInfo.setAlertNateTime(crossTrainInfo
								.getAlertNateTime()
								.toString()
								.substring(
										0,
										crossTrainInfo.getAlertNateTime()
										.toString().length() - 2));
					}
				}
			}
			
//			if (!resultCorssInfo.getAlterNateDate().contains("-")) {
//				createUnitCrossTrainUtilService.trainsortSorting(
//						listCrossTrain, result.get(0));
//			}
//			
			baseDao.insertBySql(Constants.CROSSDAO_ADD_UNIT_CROSS_INFO, result);
//			
			if (listCrossTrain != null && listCrossTrain.size() > 0) {
				int b = baseDao.insertBySql(
						Constants.CROSSDAO_ADD_UNIT_CROSS_TRAIN_INFO,
						listCrossTrain);
				// System.out.println(b);
				
			}
			
		}
		
	}

	/**
	 * 准备表unit_cross_train中的数据
	 * 
	 * @param crossTrainInfoList
	 *            通过crossid对表base_cross_train查询结果
	 * @param crossInfoList
	 *            通过crossid对表base_cross查询并对groupTotalNbr进行分组的结果
	 * @return 需要插入到表unit_cross_train表中的数据
	 */
	private List<CrossTrainInfo> prepareUnitCrossTrainInfo(
			List<CrossTrainInfo> crossTrainInfoList,
			List<CrossInfo> crossInfoList, String unitCrossId) throws Exception {
		List<CrossTrainInfo> list = new ArrayList<CrossTrainInfo>();
		if (crossTrainInfoList != null && crossTrainInfoList.size() > 0) {
			for (int i = 0; i < crossTrainInfoList.size(); i++) {
				if (crossInfoList != null && crossInfoList.size() > 0) {
					for (int j = 0; j < crossInfoList.size(); j++) {
						CrossInfo crossInfo = crossInfoList.get(j);
						CrossTrainInfo crossTrainInfo = crossTrainInfoList
								.get(i);

						CrossTrainInfo temp = new CrossTrainInfo();
						BeanUtils.copyProperties(temp, crossTrainInfo);
						// temp.setPeriodSourceTime(crossTrainInfo.getPeriodSourceTime());
						temp.setUnitCrossId(unitCrossId);
						temp.setMarshallingName(crossInfo.getMarshallingName());
						temp.setGroupSerialNbr(crossInfo.getGroupSerialNbr());
						temp.setTrainSortBase(temp.getTrainSort());
						Date date = dateFormat.parse(crossTrainInfo
								.getRunDate());
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(date);
						// 判断是否是隔日开行，计算开始时间
						if (Integer.parseInt(crossInfo.getCommonlineRule()) == 2) {
							if (crossInfo.getGroupSerialNbr() - 1 == 0) {
								// 车组-1,就等于循环中的第一个
								calendar.add(Calendar.DATE,
										crossInfo.getGroupSerialNbr() - 1);
							} else {
								calendar.add(Calendar.DATE,
										(crossInfo.getGroupSerialNbr() - 1) * 2);
							}
						} else {
							calendar.add(Calendar.DATE,
									crossInfo.getGroupSerialNbr() - 1);
						}

						temp.setRunDate(dateFormat.format(calendar.getTime()));

						date = dateFormat.parse(crossTrainInfo.getEndDate());
						calendar = new GregorianCalendar();
						calendar.setTime(date);
						// 判断是否是隔日开行，计算结束时间
						if (Integer.parseInt(crossInfo.getCommonlineRule()) == 2) {
							if (crossInfo.getGroupSerialNbr() - 1 == 0) {
								// 车组-1,就等于循环中的第一个
								calendar.add(Calendar.DATE,
										crossInfo.getGroupSerialNbr() - 1);
							} else {
								calendar.add(Calendar.DATE,
										(crossInfo.getGroupSerialNbr() - 1) * 2);
							}
						} else {
							calendar.add(Calendar.DATE,
									crossInfo.getGroupSerialNbr() - 1);
						}

						temp.setEndDate(dateFormat.format(calendar.getTime()));
						if (j == 0) {
							temp.setGroupGap(0);
						} else {
							int groupGap = daysBetween(
									dateFormat.parse(getPreCrossInfo(
											crossInfoList, crossInfo)
											.getCrossStartDate()),
									dateFormat.parse(crossInfo
											.getCrossStartDate()));
							temp.setGroupGap(groupGap);
						}
						// 设置主键
						temp.setUnitCrossTrainId(UUID.randomUUID().toString());
						list.add(temp);
					}
				}

			}
		}

		return list;
	}

	private CrossInfo getPreCrossInfo(List<CrossInfo> crossInfoList,
			CrossInfo currCross) {
		for (CrossInfo crossInfo : crossInfoList) {
			if (crossInfo.getGroupSerialNbr() == currCross.getGroupSerialNbr() - 1) {
				return crossInfo;
			}
		}
		return null;
	}

	/**
	 * 准备unit_cross表中数据
	 * 
	 * @param crossInfo
	 * @return
	 */
	private List<CrossInfo> prepareUnitCrossInfo(CrossInfo crossInfo)
			throws Exception {
		List<CrossInfo> list = new LinkedList<CrossInfo>();
		// 组数（需几组车底担当）
		int groupTotalNbr = crossInfo.getGroupTotalNbr();

		// 通过crossId查询cross_train信息
		List<CrossTrainInfo> crossTrainList = getCrossTrainInfoForCrossid(crossInfo
				.getCrossId());
		if (crossTrainList != null && crossTrainList.size() > 0) {
			CrossTrainInfo crossTrain = crossTrainList.get(0);
			String marshallingNamePre = crossTrain.getStartStn() == null ? crossTrain
					.getTrainNbr() + "-"
					: crossTrain.getStartStn().substring(0, 1) + "开" + "-"
							+ crossTrain.getTrainNbr() + "-";
			// 高线标记
			int highlineFlag = 0;
			// 高线开行规律
			int highlineRule = 0;
			// 普线开行规律,普线开行规律（1:每日;2:隔日）
			int commonlineRule = 1;

			if (!StringUtil.strIsNull(crossInfo.getHighlineFlag())) {
				highlineFlag = Integer.valueOf(crossTrain.getHighlineFlag());
			}
			if (!StringUtil.strIsNull(crossInfo.getHighlineRule())) {
				highlineRule = Integer.valueOf(crossTrain.getHighlineRule());
				highlineRule = highlineRule == 0 ? 1 : highlineRule;
			}
			if (!StringUtil.strIsNull(crossInfo.getCommonlineRule())) {
				// 如果没有选择,那么保存的时候就是null
				if (!"null".equals(crossInfo.getCommonlineRule())) {
					// commonlineRule =
					// Integer.valueOf(crossTrain.getCommonLineRule());
					// commonlineRule = commonlineRule == 0 ? 1 :
					// commonlineRule;
					commonlineRule = Integer.valueOf(crossInfo
							.getCommonlineRule());
					commonlineRule = commonlineRule == 0 ? 1 : commonlineRule;
				}
			}

			CrossInfo preUnitCross = null;
			String crossStartDate = crossInfo.getCrossStartDate();
			String crossEndDate = crossInfo.getCrossEndDate();
			if (groupTotalNbr > 0) {
				for (int i = 0; i < groupTotalNbr; i++) {
					CrossInfo tempInfo = new CrossInfo();
					if (preUnitCross != null) {
						/** 计算下一个交路单元的开始日期，在上一个交路单元的终到日期的基础上再加上间隔天数 **/
						// 上一个交路单元的终到日期,格式为yyyyMMdd
						String preCrossStartDate = preUnitCross
								.getCrossStartDate();
						String preCrossEndDate = preUnitCross.getCrossEndDate();
						// 高线标记（1:高线；0:普线；2:混合）
						if (highlineFlag == 0 || highlineFlag == 2) {
							crossStartDate = DateUtil.getDateByDay(
									DateUtil.getFormateDay(preCrossStartDate),
									-commonlineRule);
							crossEndDate = DateUtil.getDateByDay(
									DateUtil.getFormateDay(preCrossEndDate),
									-commonlineRule);

						} else {
							// 高线开行规律（1:平日;2:周末;3:高峰）
							if (highlineRule == 1) {
								crossStartDate = DateUtil.getDateByDay(DateUtil
										.getFormateDay(preCrossStartDate),
										-highlineRule);
								crossEndDate = DateUtil
										.getDateByDay(
												DateUtil.getFormateDay(preCrossEndDate),
												-highlineRule);
							} else if (highlineRule == 2 || highlineRule == 3) {
								// TODO 暂时不处理 默认向后推1天
								crossStartDate = DateUtil.getDateByDay(DateUtil
										.getFormateDay(preCrossStartDate), -1);
								crossEndDate = DateUtil
										.getDateByDay(
												DateUtil.getFormateDay(preCrossEndDate),
												-1);
							} else {
								crossStartDate = DateUtil.getDateByDay(DateUtil
										.getFormateDay(preCrossStartDate), -1);
								crossEndDate = DateUtil
										.getDateByDay(
												DateUtil.getFormateDay(preCrossEndDate),
												-1);
							}
						}
					}
					tempInfo.setCrossStartDate(crossStartDate.replaceAll("-",
							""));
					tempInfo.setCrossEndDate(crossEndDate.replaceAll("-", ""));
					tempInfo.setGroupSerialNbr((i + 1));
					tempInfo.setMarshallingName(marshallingNamePre + (i + 1));
					tempInfo.setCommonlineRule(commonlineRule + "");
					list.add(tempInfo);
					preUnitCross = tempInfo;
				}
			}
		}

		return list;
	}

	/**
	 * 查询cross信息
	 * 
	 * @param reqMap
	 * @return
	 */
	public List<CrossInfo> getCrossInfo(Map<String, Object> reqMap) {
		List<CrossInfo> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_CROSS_INFO, reqMap);
		return list;
	}

	/**
	 * 查询 unitcross信息总条数
	 */
	public long getCrossInfoCount(Map<String, Object> reqMap) {
		List<Map<String, Object>> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_CROSS_INFO_COUNT, reqMap);
		long count = 0;
		if (list != null) {
			// 只有一条数据
			Map<String, Object> map = list.get(0);
			count = ((BigDecimal) map.get("COUNT")).longValue();
		}
		return count;
	}

	/**
	 * 查询unitcross信息
	 * 
	 * @param reqMap
	 * @return
	 */
	public List<SubCrossInfo> getUnitCrossInfo(Map<String, Object> reqMap) {
		List<SubCrossInfo> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSS_INFO, reqMap);
		return list;
	}

	/**
	 * 通过crossId在表unitCorss表中查询unitCross基本信息
	 * 
	 * @param crossId
	 * @return
	 */
	public List<CrossInfo> getUnitCrossInfosForCrossId(String crossId) {
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSS_INFO_FOR_CROSSID, crossId);
	}
	/**
	 * 通过crossName,chartId在表unitCorss表中查询unitCross基本信息
	 * 
	 * @param crossName 交路名
	 * @param chartId 方案名
	 * @return
	 */
	public List<CrossInfo> getUnitCrossInfosForCrossNameAndChartId(String crossName,String chartId) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("crossName", crossName);
		map.put("chartId", chartId);
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSS_INFO_BY_CROSSNAME_CHARTID, map);
	}

	/**
	 * 查询 unitcross信息总条数
	 */
	public long getUnitCrossInfoCount(Map<String, Object> reqMap) {
		List<Map<String, Object>> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSS_INFO_COUNT, reqMap);
		long count = 0;
		if (list != null) {
			// 只有一条数据
			Map<String, Object> map = list.get(0);
			count = ((BigDecimal) map.get("COUNT")).longValue();
		}
		return count;
	}

	/**
	 * 通过crossid查询crossinfo信息
	 * 
	 * @param crossId
	 * @return
	 */
	public CrossInfo getCrossInfoForCrossid(String crossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("crossId", crossId);
		return (CrossInfo) baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_CROSS_INFO_FOR_PARAM, paramMap);

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

	/**
	 * 通过crossid查询crosstrainInfo信息
	 * 
	 * @param crossId
	 * @return
	 */
	public List<CrossTrainInfo> getCrossTrainInfoForCrossid(String crossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("crossId", crossId);
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_CROSS_TRAIN_INFO_FOR_CROSSID, paramMap);
	}

	/**
	 * 通过unitCrossId查询unit_cross_trainInfo信息
	 * 
	 * @param crossId
	 * @return
	 */
	public List<CrossTrainInfo> getUnitCrossTrainInfoForUnitCrossId(
			String unitCrossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitCrossId", unitCrossId);
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSSTRAIN_INFO_FOR_UNIT_CROSSID,
				paramMap);
	}

	/**
	 * 通过UNITcrossid查询UNITcrossinfo信息
	 * 
	 * @param unitCrossId
	 * @return
	 */
	public CrossInfo getUnitCrossInfoForUnitCrossid(String unitCrossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitCrossId", unitCrossId);
		return (CrossInfo) baseDao.selectOneBySql(
				Constants.CROSSDAO_GET_UNIT_CROSS_INFO_FOR_UNIT_CROSSID,
				paramMap);
	}

	/**
	 * 通过unitcrossid查询crosstrainInfo信息
	 * 
	 * @param unitCrossId
	 * @return
	 */
	public List<UnitCrossTrainInfo> getUnitCrossTrainInfoForUnitCrossid(
			String unitCrossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitCrossId", unitCrossId);
		List<UnitCrossTrainInfo> list = baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSS_TRAIN_INFO_FOR_UNIT_CROSSID,
				paramMap);
		/*
		 * if(list != null && list.size() > 0 ){ for(UnitCrossTrainInfo
		 * crossInfo : list ){ List<UnitCrossTrainSubInfo> trainList =
		 * crossInfo.getTrainInfoList(); if(trainList != null &&
		 * trainList.size() > 0 ){ for(UnitCrossTrainSubInfo train :trainList){
		 * String runDate = train.getRunDate(); String endDate =
		 * train.getEndDate(); String startStn = train.getStartStn(); String
		 * endStn = train.getEndStn();
		 * 
		 * List<UnitCrossTrainSubInfoTime> tempList = new
		 * ArrayList<UnitCrossTrainSubInfoTime>();
		 * List<UnitCrossTrainSubInfoTime> stationTimeList =
		 * train.getStationTimeList();
		 * 
		 * if(stationTimeList != null && stationTimeList.size() > 0 ){ for(int i
		 * = 0;i<stationTimeList.size();i++){ UnitCrossTrainSubInfoTime
		 * trainTime = stationTimeList.get(i); String arrTime =
		 * trainTime.getArrTime(); String dptTime = trainTime.getDptTime();
		 * String stnName = trainTime.getStnName();
		 * if(startStn.equals(stnName)&& runDate.equals(dptTime)){
		 * 
		 * tempList.add(trainTime); }else if(endStn.equals(stnName) &&
		 * endDate.equals(arrTime)){ tempList.add(trainTime); }
		 * 
		 * } } train.setStationTimeList(tempList);
		 * 
		 * } } } }
		 */
		return list;
	}

	/**
	 * 通过crossid查询crosstrainInfo信息
	 * 
	 * @param unitCrossId
	 * @return
	 */
	public List<BaseCrossTrainInfo> getCrossTrainInfoForCrossId(String crossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("baseCrossId", crossId);
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_CROSS_TRAININFO_FOR_CROSSID, paramMap);
	}

	/**
	 * 通过crossid查询crosstrainInfo信息
	 * 
	 * @param unitCrossId
	 * @param bureauShortName
	 *            所属局简称
	 * @return
	 */
	public List<TrainLineInfo> getTrainPlanLineInfoForPlanCrossId(
			String planCrossId, String bureauShortName, String startTime,
			String endTime) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("planCrossId", planCrossId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		// 如果是总局简称为：总
		paramMap.put("bureauShortName", bureauShortName == null ? "Z"
				: bureauShortName);

		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID,
				paramMap);
	}

	/**
	 * 通过crossid查询crosstrainInfo信息
	 * 
	 * @param unitCrossId
	 * @param bureauShortName
	 *            所属局简称
	 * @return
	 */
	public List<TrainLineInfo> getTrainPlanLineInfoForPlanCrossIdAndBureauShortName(
			String highLineCrossId, String bureauShortName) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("highLineCrossId", highLineCrossId);
		// 如果是总局简称为：总
		paramMap.put("bureauShortName", bureauShortName == null ? "Z"
				: bureauShortName);

		return baseDao
				.selectListBySql(
						Constants.CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_ANDBUREAUSHORTNAME,
						paramMap);
	}

	/**
	 * 通过crossid查询crosstrainInfo信息
	 * 
	 * @param unitCrossId
	 * @param bureauShortName
	 *            所属局简称
	 * @return
	 */
	public List<TrainLineInfo> getTrainPlanLineInfoForPlanCrossIdAndVehicle(
			String highLineCrossId, String bureauShortName) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("highLineCrossId", highLineCrossId);
		// 如果是总局简称为：总
		paramMap.put("bureauShortName", bureauShortName == null ? "Z"
				: bureauShortName);

		return baseDao
				.selectListBySql(
						Constants.CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_AND_VEHICLE,
						paramMap);
	}

	/**
	 * 通过crossid查询crosstrainInfo信息
	 * 
	 * @param unitCrossId
	 * @param bureauShortName
	 *            所属局简称
	 * @return
	 */
	public List<TrainLineInfo> getTrainPlanLineInfoForPlanCrossIdAndVehicleBy2(
			String highLineCrossId, String bureauShortName) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("highLineCrossId", highLineCrossId);
		// 如果是总局简称为：总
		paramMap.put("bureauShortName", bureauShortName == null ? "Z"
				: bureauShortName);

		return baseDao
				.selectListBySql(
						Constants.CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_AND_VEHICLEBY2,
						paramMap);
	}

	/**
	 * 通过crossid查询crosstrainInfo信息
	 * 
	 * @param unitCrossId
	 * @param bureauShortName
	 *            所属局简称
	 * @return
	 */
	public List<TrainLineInfo> getTrainPlanLineInfoForPlanCrossIdAndVehicleSearch(
			String highLineCrossId, String bureauShortName) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("highLineCrossId", highLineCrossId);
		// 如果是总局简称为：总
		paramMap.put("bureauShortName", bureauShortName == null ? "Z"
				: bureauShortName);

		return baseDao
				.selectListBySql(
						Constants.CROSSDAO_GET_TRAINPLANLINE_INFO_FOR_PLANCROSSID_AND_VEHICLE_VEHICLESEARCH,
						paramMap);
	}

	/**
	 * 通过planCrossId查询起始站
	 * 
	 * @param planCrossId
	 * @return
	 */
	public List<Map<String, Object>> getStationListForPlanCrossId(
			String planCrossId, String stnBureau) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("planCrossId", planCrossId);
		reqMap.put("stnBureau", stnBureau == null ? "Z" : stnBureau);
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_STATIONLIST_FOR_PLANCROSSID, reqMap);
	}

	public void actionExcel(InputStream inputStream, String chartId,
			String startDay, String chartName, String addFlag)
			throws IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		// 初始化表头映射，因为映射的标题是符合标题行目前不支持，所以直接使用顺序映射
		LinkedHashMap<String, String> pm = new LinkedHashMap<String, String>();
		pm.put("crossIdForExcel", "");
		pm.put("crossName", "");
		pm.put("crossSpareName", "");
		pm.put("alterNateDate", "");
		pm.put("alterNateTranNbr", "");
		pm.put("spareFlag", "");
		pm.put("cutOld", "");
		pm.put("groupTotalNbr", "");
		pm.put("pairNbr", "");
		pm.put("highlineFlag", "");
		pm.put("highlineRule", "");
		pm.put("commonlineRule", "");
		pm.put("appointWeek", "");
		pm.put("appointDay", "");
		pm.put("appointPeriod", "");
		pm.put("crossSection", "");
		pm.put("throughline", "");
		pm.put("tokenVehBureau", "");
		pm.put("tokenVehDept", "");
		pm.put("tokenVehDepot", "");
		pm.put("tokenPsgBureau", "");
		pm.put("tokenPsgDept", "");
		pm.put("locoType", "");
		pm.put("crhType", "");
		pm.put("elecSupply", "");
		pm.put("dejCollect", "");
		pm.put("airCondition", "");
		pm.put("note", "");

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
			if ("1".equals(addFlag)) {
				clearCrossInfoByChartId(chartId);
			}
			// 迭代excel中的每一个sheet
			for (int i = 0; i < num; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				ExcelUtil<CrossInfo> test = new ExcelUtil<CrossInfo>(pm, sheet,
						CrossInfo.class);
				test.setValueMapping(valuesMap);
				List<CrossInfo> list = test.getEntitiesHasNoHeader(4);

				for (int j = 0; j < list.size(); j++) {

					CrossInfo crossInfo = list.get(j);
//					if(crossInfo.getCrossName().equals("50712-50713")){
//						int a = 0;
//					}
//					System.out.println(crossInfo.getCrossName());
//					System.out.println(crossInfo.getHighlineFlag());
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

	private void clearCrossInfoByChartId(String chartId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("chartId", chartId);
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		if (user.getBureau() != null) {
			map.put("bureaus", user.getBureau());
		}
		this.baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_UNITCROSSTRAIN_BY_CHARID, map);
		this.baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_UNITCROSS_INFO_BY_CHARID, map);
		this.baseDao.deleteBySql(
				Constants.CROSSDAO_DELETE_CROSSTRAIN_BY_CHARID, map);
		this.baseDao.deleteBySql(Constants.CROSSDAO_DELETE_CROSSINFO_BY_CHARID,
				map);
	}

	private static int daysBetween(Date date1, Date date2) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date1);

		long time1 = cal.getTimeInMillis();

		cal.setTime(date2);

		long time2 = cal.getTimeInMillis();

		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));

	}

	/**
	 * 用于并行处理单个交路信息的完善
	 * 
	 * @author Administrator
	 *
	 */
	class CrossCompletionService implements Callable<String> {

		private CrossInfo cross;

		private BaseDao baseDao;

		private String addFlag;

		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		public CrossCompletionService(CrossInfo cross) {
			this.cross = cross;
		}

		public CrossCompletionService(CrossInfo cross, BaseDao baseDao,
				String addFlag) {
			this.cross = cross;
			this.baseDao = baseDao;
			this.addFlag = addFlag;
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

		private void clearOldCrossInfo() {
			String[] ids = getCrossInfoByCrossName(this.cross.getCrossName(),
					this.cross.getChartId());

			try {
				if (ids.length > 0) {
					List<String> idsList = new ArrayList<String>();
					for (int i = 0; i < ids.length; i++) {
						idsList.add(ids[i]);
					}
					deleteUnitCrossInfoTrainForCorssIds(idsList);
					deleteUnitCrossInfoForCorssIds(idsList);
					deleteCrossInfoTrainForCorssIds(idsList);
					deleteCrossInfoForCorssIds(idsList);
				}
			} catch (Exception e) {

				logger.error("删除已有记录出错 ", e);
			}
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

			if ("0".equals(addFlag)) {
				clearOldCrossInfo();
			}
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

			return "success";
		}

		/**
		 * 需要将交路分成两部分，第一部分处理
		 * 
		 * @param train
		 * @param charId
		 * @param startStation 后车的始发站
		 * @param trains0Dxx
		 *            : Map<n,list<PlanTrain>>
		 *            n为第一个唯一车（0D2702-0D2701-0D2703-D2702 ,n=3,map.size()=4）
		 */
//		private void trainInfoFromPainfirst(CrossTrainInfo train,
//				String charId, String beforeBureau) {
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
//							if (beforeBureau.equals(baseTrainInfo
//									.getEndBureanShortName())) {// 下一个站（该站的终到=下一个的始发局）
//								currTrain = baseTrainInfo;
//							}
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
		private LinkedList<CrossTrainInfo> trainInfoFromPainfirst2(LinkedList<CrossTrainInfo> oldLinkBaseTrains,
				String charId, String bureau) {
			// TODO 修改这个方法的时候，需要同时修改2433行的trainInfoFromPain
			
				
			LinkedList<CrossTrainInfo> crossTrains = new LinkedList<CrossTrainInfo>();
			CrossTrainInfo train = null;
//			for (int i=oldLinkBaseTrains.size()-1 ; i >=0 ; i-- ) {
			for (int i=0 ; i < oldLinkBaseTrains.size(); i++ ) {
			
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
//				map.put("bureau", LjUtil.getLjByName(bureau, 2));//
				
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
				if(baseTrainInfoByParamByEndStn!=null){
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
//					train.setAlertNateTime(currTrain.getStartTime());
					
				}
				
				
//				crossTrains.add(oldLinkBaseTrains.size()-1-i, train);
//				crossTrains.add(i, train);
				crossTrains.add(train);
			}
			return 	crossTrains;
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
//				String beforeBureau) {
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
//							if (beforeBureau.equals(baseTrainInfo
//									.getStartBureauShortName())) {// 该站的始发==参照的终到局（上一个站）
//								currTrain = baseTrainInfo;
//							}
							if (endStn.equals(baseTrainInfo
									.getStartStn())) {// 该站的始发==参照的终到局（上一个站）
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
//						crosstrain.setRunDate(cross.getCrossStartDate());
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
			List<Map> allMapList = new ArrayList<Map>();
			
			boolean flag = false;//是否直接通过shortBureau来确定
			
			Map trains0Dxx0 = getBureauFor0Dxxx50301(crossTrains,cross.getChartId());
			allMapList.add(trains0Dxx0);
			if (getBureauFor0Dxxx50301_2(crossTrains,cross.getChartId())) {
				List<PlanTrain> PlanTrainlist = ((List<PlanTrain>) trains0Dxx0.get(trains0Dxx0.size() - 1 + ""));
				if(PlanTrainlist.size()==1){
				}
				else{
					int acount = 0;
					for (Map map : allMapList) {//map==trains0Dxx
						for (int a = 0;a<map.size();a++) {
							List<PlanTrain> planTrain = (List<PlanTrain>) map.get(a+"");
							for (PlanTrain oTrain : planTrain) {
								if(user.getBureauShortName().equals(oTrain.getRoutingBureauShortName())){
									acount++;
								}
							}
						}
					}
					if(acount==crossTrains.size()){//说明   交路中得每一个车train  都有一个是本局的车
						//那么就确定第一个车了  就是第一个  本局的车
						flag = true;
					}else{
						throw new Exception(
								"this corss is invalid! (user-defined exception)");
					}
				}
			}
			
			if(!flag){//不是50301 的情况
				for (int i = 0; i < crossTrains.size(); i++) {
					CrossTrainInfo crossTrain = crossTrains.get(i);
					// getBureauFor0Dxxx(crossTrains,crossTrain,i);//0D2704-D2703-D2704-0D2703
					// 根据“后车的始发站”或“前车的终到站”自动匹配
					if (i == 0) {
						
						PlanTrain unionTrain = new PlanTrain();
						String startBureau = "";
						String startStation = "";
						
						//Map<n,list<PlanTrain>> n为（0.1.2....）， N为唯一确定车的序号
						
						Map trains0Dxx = getBureauFor0Dxxx(crossTrains,
								cross.getChartId(), i);
						
						if(null != trains0Dxx.get(trains0Dxx.size() - 1 + "")){
							unionTrain = ((List<PlanTrain>) trains0Dxx
									.get(trains0Dxx.size() - 1 + "")).get(0);// 需要参照该车的始发站局
							startBureau = unionTrain.getStartBureau();
							startStation = unionTrain.getStartStn();	
						}
	//					PlanTrain unionTrain = ((List<PlanTrain>) trains0Dxx
	//							.get(trains0Dxx.size() - 1 + "")).get(0);// 需要参照该车的始发站局
	//					String startBureau = unionTrain.getStartBureau();
	//					String startStation = unionTrain.getStartStn();
						//如果第一个站查询结果为大于1
						if(trains0Dxx.size() > 1 ){
							for (int k = trains0Dxx.size() - 2; k >= 0; k--) {
								if (k == trains0Dxx.size() - 2) {
									// 取map.get(n)的始发站，作为参照
									trainInfoFromPainfirst(crossTrains.get(k),
											cross.getChartId(), startStation);
								} else {
									// 取crossTrains.get(k+1)的始发站，作为参照
									trainInfoFromPainfirst(crossTrains.get(k),
											cross.getChartId(), crossTrains.get(k + 1)
													.getStartStn());
								}
							}
						}
						else{//如果第一个站查询结果为只有一个车
							trainInfoFromPain(crossTrain, cross.getChartId(), "");//说明  交路第一个车是唯一的  不需要参照
						}
					} else {
						// 直接去crossTrains得上一个元素的终到信息
						String bureau = "";
						String endStn = crossTrains.get(i - 1).getEndStn();
	//					if (crossTrains.get(i - 1).getEndBureau() != null) {
	//						endStn = bureauDao.getShortBureauNameByCode(crossTrains
	//								.get(i - 1).getEndStn());
	//					}
						trainInfoFromPain(crossTrain, cross.getChartId(), endStn);// 这里不用查询数据库
																					// 直接去crossTrains得上一个元素的终到信息
					}
					// 设置交路的开始日期和结束日期
					if (cross.getCrossName().startsWith(crossTrain.getTrainNbr())) {
						if (!isSet) {
							cross.setStartBureau(crossTrain.getStartBureau());
							cross.setCrossStartDate(crossTrain.getAlertNateTime()
									.substring(0, 8));
							isSet = true;
						}
					}
					routeBureauShortNames += crossTrain.getRouteBureauShortNames() != null ? crossTrain
							.getRouteBureauShortNames() : "";
				}
			}//if(!flag) end
			else{//是 50301的情况
				
//				crossTrains
				Map trains0Dxx = getBureauFor0Dxxx(crossTrains,
						cross.getChartId(), 0);//确定该路局的  车
				List<PlanTrain> ootrain = (List<PlanTrain>) trains0Dxx.get(0+"");//第一个车次  对应所有同名的车
				PlanTrain bureauFirsrPlanTrain = null;
				for (PlanTrain planTrain : ootrain) {
					if(user.getBureauShortName().equals(planTrain.getStartBureau())){//找到
//					if(user.getBureauShortName().equals(planTrain.getRoutingBureauShortName())){//找到
						bureauFirsrPlanTrain = planTrain;
						crossTrains = trainInfoFromPainfirst2(crossTrains,cross.getChartId(), planTrain.getStartBureau());
//						trainInfoFromPainfirst2(crossTrains.get(0),cross.getChartId(), planTrain.getEndStn());
//						break;
						
					}
				}
				// 设置交路的开始日期和结束日期
				if (cross.getCrossName().startsWith(crossTrains.get(0).getTrainNbr())) {
					if (!isSet) {
						cross.setStartBureau(crossTrains.get(0).getStartBureau());
						cross.setCrossStartDate(alertNateDate[0]);
						isSet = true;
					}
				}
				for (int i = 0; i < crossTrains.size(); i++) {
					train = crossTrains.get(i);
					train.setTrainSort(i + 1);
					train.setCrossId(cross.getCrossId());
//					train.setTrainNbr(trains[i]);
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

//					crossTrains.add(train);
					crossTrains.set(i, train);
					
					routeBureauShortNames += train.getRouteBureauShortNames() != null ? train
							.getRouteBureauShortNames() : "";
				}
			}//if(!flag)  else end
			
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
//-------------------------(2)----------------------------------------------------------------
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
					List<Map> allMapList2 = new ArrayList<Map>();
					
					boolean flag2 = false;//是否直接通过shortBureau来确定
					
					Map trains0Dxx = getBureauFor0Dxxx50301(crossTrains,cross.getChartId());
					allMapList2.add(trains0Dxx0);
					if (getBureauFor0Dxxx50301_2(crossTrains,cross.getChartId())) {
						List<PlanTrain> PlanTrainlist = ((List<PlanTrain>) trains0Dxx0.get(trains0Dxx0.size() - 1 + ""));
						if(PlanTrainlist.size()==1){
						}
						else{
							int acount = 0;
							for (Map map : allMapList2) {//map==trains0Dxx
								for (int a = 0;a<map.size();a++) {
									List<PlanTrain> planTrain = (List<PlanTrain>) map.get(a+"");
									for (PlanTrain oTrain : planTrain) {
										if(user.getBureauShortName().equals(oTrain.getRoutingBureauShortName())){
											acount++;
										}
									}
								}
							}
							if(acount==crossTrains.size()){//说明   交路中得每一个车train  都有一个是本局的车
								//那么就确定第一个车了  就是第一个  本局的车
								flag2 = true;
							}else{
								throw new Exception(
										"this corss is invalid! (user-defined exception)");
							}
						}
					}
					if(!flag2){
						if (i == 0) {
							
							PlanTrain unionTrain = ((List<PlanTrain>) trains0Dxx
									.get(trains0Dxx.size() - 1 + "")).get(0);// 需要参照该车的始发站局
	//						String startBureau = unionTrain.getStartBureauShortName();
							String startStation = unionTrain.getStartStn();
							//如果第一个站查询结果为大于1
							if(trains0Dxx.size() > 1 ){
								for (int k = trains0Dxx.size() - 2; k >= 0; k--) {
		//							List<PlanTrain> ODtrains = (List<PlanTrain>) trains0Dxx.get(k + "");
									// for (int j = 0; j < ODtrains.size(); j++) {
									if (k == trains0Dxx.size() - 2) {
										// 取map.get(n)的始发站，作为参照
										trainInfoFromPainfirst(crossTrains.get(k),
												cross.getChartId(), startStation);
									} else {
										// 取crossTrains.get(k+1)的始发站，作为参照
										trainInfoFromPainfirst(crossTrains.get(k),
												cross.getChartId(), crossTrains.get(k + 1)
														.getStartStn());
									}
									// }
								}
							}
							else{//如果第一个站查询结果为只有一个车
								trainInfoFromPain(crossTrain, cross.getChartId(), "");//说明  交路第一个车是唯一的  不需要参照
							}
						} else {
							// trainInfoFromPain(crossTrain,
							// cross.getChartId(),bureauDao.getShortBureauNameByCode(crossTrains.get(i-1).getEndBureau()));
							String bureau = "";
							String endStn = crossTrains.get(i - 1).getEndStn();
	//						if (crossTrains.get(i - 1).getEndBureau() != null) {
	//							bureau = bureauDao
	//									.getShortBureauNameByCode(crossTrains.get(
	//											i - 1).getEndBureau());
	//						}
							trainInfoFromPain(crossTrain, cross.getChartId(),
									endStn);// 这里不用查询数据库 直接去crossTrains得上一个元素的终到信息
						}
						
						
					}//if (!flag2) end
					else{
	
						Map trains0Dxx_flag2 = getBureauFor0Dxxx(crossTrains,
								cross.getChartId(), 0);//确定该路局的  车
						List<PlanTrain> ootrain = (List<PlanTrain>) trains0Dxx_flag2.get(0+"");//第一个车次  对应所有同名的车
						PlanTrain bureauFirsrPlanTrain = null;
						for (PlanTrain planTrain : ootrain) {
							if(user.getBureauShortName().equals(planTrain.getStartBureau())){//找到
	//						if(user.getBureauShortName().equals(planTrain.getRoutingBureauShortName())){//找到
								bureauFirsrPlanTrain = planTrain;
								crossTrains = trainInfoFromPainfirst2(crossTrains,cross.getChartId(), planTrain.getStartBureau());
	//							trainInfoFromPainfirst2(crossTrains.get(0),cross.getChartId(), planTrain.getEndStn());
	//							break;
								
							}
						}
						// 设置交路的开始日期和结束日期
						if (cross.getCrossName().startsWith(crossTrains.get(0).getTrainNbr())) {
							if (!isSet) {
								cross.setStartBureau(crossTrains.get(0).getStartBureau());
								cross.setCrossStartDate(alertNateDate[0]);
								isSet = true;
							}
						}
						for (int j = 0; j < crossTrains.size(); j++) {
							train = crossTrains.get(j);
							train.setTrainSort(j + 1);
							train.setCrossId(cross.getCrossId());
	//						train.setTrainNbr(trains[i]);
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
										train.setSpareFlag(Integer.parseInt(spareFlag[0]));
									} else {
										train.setSpareFlag(Integer.parseInt(spareFlag[j]));
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

	/**
	 * 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
	 * 
	 * @param crossTrains
	 * @param crossTrain
	 * @param i
	 */
	private Map getBureauFor0Dxxx(LinkedList<CrossTrainInfo> crossTrains,
			String chartId, int i) {
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
	 * 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
	 * 
	 * @param crossTrains
	 * @param crossTrain
	 * @param i
	 */
	private Map getBureauFor0Dxxx50301(LinkedList<CrossTrainInfo> crossTrains,
			String chartId) {
		// 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossTrains", crossTrains);
		
		return getBaseTrainStartBureauByTrainNbr50301(map);//是不是每个车都不能确定的 交路 false  某个唯一 ，true 都不唯一
	}
	private boolean getBureauFor0Dxxx50301_2(LinkedList<CrossTrainInfo> crossTrains,
			String chartId) {
		// 0D2704-D2703-D2704-0D2703 根据“后车的始发站”或“前车的终到站”自动匹配
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chartId", chartId);
		map.put("crossTrains", crossTrains);
		
		return getBaseTrainStartBureauByTrainNbr50301_2(map);//是不是每个车都不能确定的 交路 false  某个唯一 ，true 都不唯一
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

	public void updateBaseCrossDayGap(String[] ids, int[] values) {
		if (ids != null && values != null && ids.length == values.length) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> map1 = new HashMap<String, Object>();
			Map<String, Object> map2 = new HashMap<String, Object>();
			for (int i = 0; i < values.length; i++) {
				map.put("planId", ids[i]);
				map.put("value", values[i]);
				// 当前组的上一组数据,当前组=2,数据为组1的数据
				map.put("run_date", map1.get("RUN_DATE"));
				map.put("end_date", map1.get("END_DATE"));
				this.crossMybatisDao.updateCrossPlanDayGap(map);
				map1.clear();
				map1 = crossMybatisDao.getBaseCrossById(ids[i]);
				if (i == 0) {
					map2.put("cross_start_date", map1.get("RUN_DATE"));
				}
				if (i == (values.length - 1)) {
					map2.put("cross_end_date", map1.get("END_DATE"));
					map2.put("id", map1.get("BASE_CROSS_ID"));
					crossMybatisDao.updateBaseCrossById(map2);
				}
			}
		}
	}

	public List<TrainActivityPeriod> getTrainActivityPeriod(
			Map<String, Object> reqMap) {
		return crossMybatisDao.searchPeriod(reqMap);
	}

	/**
	 * 跟新base_cross_train表中的base_trainID和有效期字段
	 * 
	 * @param
	 * @return 更新成功的数据条数
	 */
	public int updateBaseCrossTrainPeriod(Map<String, Object> reqMap) {
		return baseDao.updateBySql(
				Constants.CROSSDAO_UPDATE_BASE_CROSS_TRAIN_PERIOD, reqMap);
	}

	/**
	 * 根据BASE_CROSS_ID 删除unit_cross对应的数据
	 * 
	 * @param
	 * @return 更新成功的数据条数
	 */
	public int deleteUnitCrossByBaseCrossId(Map<String, Object> reqMap) {
		return UnitCrossDao.deleteUnitCrossByBaseCrossId(reqMap);
	}

	/**
	 * 根据BASE_CROSS_ID 删除unit_cross_train对应的数据
	 * 
	 * @param
	 * @return 更新成功的数据条数
	 */
	public int deleteUnitCrossTrainByBaseCrossId(Map<String, Object> reqMap) {
		return UnitCrossDao.deleteUnitCrossTrainByBaseCrossId(reqMap);
	}

	/**
	 * 跟新base_cross_train表中的base_trainID和有效期字段
	 * 
	 * @param
	 * @return 更新成功的数据条数
	 */
	public int updateBaseCrossByBaseCrossId(Map<String, Object> reqMap) {
		return baseDao.updateBySql(
				Constants.CROSSDAO_UPDATE_BASE_CROSS_BASECROSSID, reqMap);
	}

	public List<Map<String, String>> getUnitCrossTrainDateByMap(
			String unitCrossId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("unitCrossId", unitCrossId);
		return baseDao.selectListBySql(
				Constants.CROSSDAO_GET_UNIT_CROSS_TRAIN_DATE_BYMAP, paramMap);
	}

	/**
	 * 查询base_cross_train, 返回:list-Map.
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> getBaseCrossTrainByMap(
			Map<String, String> paramMap) {
		return baseDao.selectListBySql(Constants.GET_BASE_CROSS_TRAIN_BY_MAP,
				paramMap);
	}

	/**
	 * 查询base_cross_train,返回:List-CrossTrainInfo.
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<CrossTrainInfo> getBaseCrossTrainObjByMap(
			Map<String, String> paramMap) {
		return baseDao.selectListBySql(
				Constants.GET_BASE_CROSS_TRAIN_OBJ_BY_MAP, paramMap);
	}

	/**
	 * 查询base_cross_train,返回:List-Map.
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> getBaseCrossByMap(
			Map<String, String> paramMap) {
		return baseDao.selectListBySql(Constants.GET_BASE_CROSS_BY_MAP,
				paramMap);
	}

	/**
	 * 处理间隔日期.
	 * 
	 * @param crossTrainInfoList
	 * @param chartId
	 *            方案id
	 */
	public void dealWithDayGap(CrossTrainInfo train1, CrossTrainInfo train2) {
		setDayGap(train1, train2);
	}

	/**
	 * 设置交路的结束日期和列车的开始和结束日期.
	 * 
	 * @param crosstrain
	 * @param crossStartDate
	 *            主表：base_cross，开始时间.
	 * @param dayGapForCross
	 * @param i
	 *            当前传递进来的车辆，是第几个?
	 * @return
	 */
	public Integer dealWithRunDateAndEndDate(CrossTrainInfo crosstrain,
			String crossStartDate, Integer dayGapForCross, Integer i) {
		return setEndDateForCross(crosstrain, crossStartDate, dayGapForCross, i);
	}

	/**
	 * 处理始发、终到站、时间.
	 * 
	 * @param train
	 * @param charId
	 * @param beforeBureau
	 */
	public void dealWithStartAndEndStn(CrossTrainInfo train, String charId,
			String beforeBureau,String endStn,String baseTrainId,Integer bI) {
		trainInfoFromPain(train, charId, beforeBureau,endStn,baseTrainId,bI);
	}

	/**
	 * 需要将交路分成两部分，第2部分处理.
	 * 
	 */
	private void trainInfoFromPain(CrossTrainInfo train, String charId,
			String beforeBureau,String endStn,String baseTrainId,Integer bI) {
		try {
			String trainNbr = train.getTrainNbr();
			String stn = null;
			System.err.println("trainNbr==" + trainNbr);
			String regex = "^(.+?)[\\[【\\[](.+?)[\\】\\]]";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(trainNbr);

			if (matcher.find()) {
				trainNbr = matcher.group(1);
				stn = matcher.group(2);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("trainNbr", trainNbr);
			map.put("chartId", charId);
			if(bI == 1){
				map.put("id", baseTrainId);
			}
			
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
//						if (beforeBureau.equals(baseTrainInfo
//								.getStartBureauShortName())) {// 该站的始发==参照的终到局（上一个站）
//							currTrain = baseTrainInfo;
//						}
						if (endStn.equals(baseTrainInfo
								.getStartStn())) {// 该站的始发==参照的终到局（上一个站）
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
	 * 设置交路的结束日期和列车的开始和结束日期.
	 * 
	 * @param crosstrain
	 * @param crossStartDate
	 *            主表：base_cross，开始时间.
	 * @param dayGapForCross
	 * @param i
	 *            当前传递进来的车辆，是第几个?
	 * @return
	 */
	private Integer setEndDateForCross(CrossTrainInfo crosstrain,
			String crossStartDate, Integer dayGapForCross, Integer i) {
		try {
			// 设置交路的终到日期
			if (i == 0) {
				crosstrain.setRunDate(crossStartDate);
				Date date = this.dateFormat.parse(crossStartDate);
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, crosstrain.getRunDay());
				crosstrain
						.setEndDate(this.dateFormat.format(calendar.getTime()));
				dayGapForCross += crosstrain.getRunDay();
			} else {
				// 第二个车+前面的车的总天数 + daygap
				Date date = this.dateFormat.parse(crossStartDate);
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE,
						(dayGapForCross + crosstrain.getDayGap()));

				crosstrain
						.setRunDate(this.dateFormat.format(calendar.getTime()));

				dayGapForCross += crosstrain.getRunDay()
						+ crosstrain.getDayGap();

				// 设置结束时间
				calendar.add(Calendar.DATE, crosstrain.getRunDay());
				crosstrain
						.setEndDate(this.dateFormat.format(calendar.getTime()));

			}
			return dayGapForCross;
		} catch (Exception e) {
			logger.error("设置结束时间出错 ", e);
		}
		return 0;
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

				if (sourceTime.compareTo(targetTime) < 0) {
					train1.setDayGap(1);
				} else {
					train1.setDayGap(0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		;
	}

	/**
	 * 根据Obj(CrossTrainInfo)修改base_cross_train.
	 * 
	 * @param reqMap
	 * @return
	 */
	public int updBaseCrossTrainByObj(Map<String, Object> reqMap) {
		return baseDao.updateBySql(Constants.UPD_BASE_CROSS_TRAIN_BY_OBJ,
				reqMap);
	}
}
