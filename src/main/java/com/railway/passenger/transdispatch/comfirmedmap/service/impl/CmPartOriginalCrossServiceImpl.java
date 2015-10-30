package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.basicmap.original.service.IMTrainLineTempService;
import com.railway.common.entity.Page;
import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPartOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmOriginalCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmPartOriginalCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmVersionMapper;
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmPartOriginalCrossService;
import com.railway.passenger.transdispatch.common.Constant;
import com.railway.passenger.transdispatch.util.ExcelUtil;
import com.railway.passenger.transdispatch.util.PageUtil;
@Service
public class CmPartOriginalCrossServiceImpl implements ICmPartOriginalCrossService{
	
	private static Log logger = LogFactory.getLog(CmPartOriginalCrossServiceImpl.class);
	
	@Autowired
	private TCmPartOriginalCrossMapper partMapper;
	@Autowired
	private TCmOriginalCrossMapper originalMapper;
	@Autowired
	private IMTrainLineTempService trainLineService;
	@Autowired
	private TCmVersionMapper versionMapper;
	
	@Override
	public Result<TCmVersion> queryVersionInfo() {
		Result<TCmVersion> result = new Result<TCmVersion>();
		try {
			List<TCmVersion> list = versionMapper.queryList();
			result.setList(list);
		} catch (Exception e) {
			logger.error("queryVersionInfo 查询版本信息失败！" + e.getMessage());
			result.setCode(-1);
			result.setMessage("查询版本号信息失败");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TCmPartOriginalCross> add(Map<String, Object> map) {
		Result<TCmPartOriginalCross> result = new Result<TCmPartOriginalCross>();
		JSONObject object = JSONObject.fromObject(map.get("result"));
		TCmPartOriginalCross crossInfo = null;
		try {
			crossInfo = (TCmPartOriginalCross) JSONObject.toBean(object,
					TCmPartOriginalCross.class);
		} catch (Exception e) {
			logger.error("add JSONObject 转换对象失败！" + e.getMessage());
			result.setCode(-1);
			result.setMessage("新增对数表信息失败！");
			return result;
		}
		crossInfo.setCreateBureau(String.valueOf(map.get("createBureau")));
		// 可以从数据库中获取
		Map<String, String> tokenPsgDeptValuesMap = trainLineService.getBureauNameDic();
		if(tokenPsgDeptValuesMap != null){
			for(String key : tokenPsgDeptValuesMap.keySet()){
				if(crossInfo.getTokenVehBureau().equals(tokenPsgDeptValuesMap.get(key))){
					crossInfo.setTokenVehBureau(key);
				}
			}
		}else{
			logger.error("获取路局数据字典!");
			result.setMessage("路局局码转换失败！");
		}
		try {
			int code = partMapper.insertSelective(crossInfo);
		} catch (Exception e) {
			logger.error("存储对数表信息失败！",e);
			result.setCode(-1);
			result.setMessage("对数表信息存储失败！");
		}
		return result;
	}
	@Override
	public Result<TCmPartOriginalCross> check(Map<String, Object> map) {
		Result<TCmPartOriginalCross> result = new Result<TCmPartOriginalCross>();
		String crossIds = String.valueOf(map.get("crossIds"));
		String[] crossIdArr = crossIds.split(",");
		List<String> crossIdList = Arrays.asList(crossIdArr);
		try {
			int code = partMapper.batchUpdateCheckFlag(crossIdList);
		} catch (Exception e) {
			logger.error("批量修改对数表审核信息失败！",e);
			result.setCode(-1);
			result.setMessage("批量修改对数表审核信息失败！");
			return result;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("crossIds", crossIds);
		try {
			List<TCmPartOriginalCross> list = partMapper.queryByParam(queryMap);
			if(list != null && list.size() > 0){
				for(TCmPartOriginalCross cross : list){
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("crossName", cross.getCrossName());
					paramMap.put("versionId", cross.getCmVersionId());
					paramMap.put("tokenVhBureau", cross.getTokenVehBureau());
					mergePartOriginalCross(paramMap);
				}
			}
		} catch (Exception e) {
			logger.error("check根据条件查询对数表信息失败！",e);
			return result;
		}
		
		return result;
	}
	@Override
	public Result<TCmPartOriginalCross> delete(Map<String, Object> map) {
		Result<TCmPartOriginalCross> result = new Result<TCmPartOriginalCross>();
		String crossIds = String.valueOf(map.get("crossIds"));
		String[] crossIdArr = crossIds.split(",");
		List<String> crossIdList = Arrays.asList(crossIdArr);
		try {
			int code = partMapper.batchDeleteByKeys(crossIdList);
		} catch (Exception e) {
			logger.error("删除对数表信息失败",e);
			result.setCode(-1);
			result.setMessage("删除对数表信息失败！");
		}
		return result;
	}
	@Override
	public Result<TCmPartOriginalCross> pageQuery(Map<String, Object> map) {
		Result<TCmPartOriginalCross> result = new Result<TCmPartOriginalCross>();
		List<TCmPartOriginalCross> list = null;
		int count = 0;
		new PageUtil(map);
		try {
			list = partMapper.pageQuery(map);
			if(list == null || list.size() == 0){
				logger.error("分页查询对数表信息失败,为查询出数据列表！");
				result.setCode(-1);
				result.setMessage("分页查询对数表信息失败！");
				return result;
			}
		} catch (Exception e) {
			logger.error("分页查询对数表信息失败！",e);
			result.setCode(-1);
			result.setMessage("分页查询对数表信息失败！");
			return result;
		}
		try {
			count = partMapper.queryTotalCount(map);
		} catch (Exception e) {
			logger.error("查询符合条件的数据总数错误！",e);
			result.setCode(-1);
			result.setMessage("分页查询对数表信息失败！");
			return result;
		}
		Page<TCmPartOriginalCross> pageInfo = new Page<TCmPartOriginalCross>();
		pageInfo.setList(list);
		pageInfo.setNum(list.size());
		pageInfo.setTotalCount(count);
		pageInfo.setPageIndex(Integer.parseInt(String.valueOf(map.get("pageIndex"))));
		pageInfo.setPageSize(Integer.parseInt(String.valueOf(map.get("pageSize"))));
		result.setPageInfo(pageInfo);
		return result;
	}
	@Override
	public Result<TCmPartOriginalCross> queryByPrimaryKey(
			Map<String, Object> map) {
		Result<TCmPartOriginalCross> result = new Result<TCmPartOriginalCross>();
		String crossId = (String)map.get("crossId");
		try {
			TCmPartOriginalCross crossInfo = partMapper.selectByPrimaryKey(crossId);
			if(crossInfo == null){
				logger.error("queryByPrimaryKey根据ID查询单个对数表信息失败！");
				result.setCode(-1);
				result.setMessage("查询对数表信息失败！");
				return result;
			}
			result.setObj(crossInfo);
		} catch (Exception e) {
			logger.error("根据对数表ID查询对数表信息失败！",e);
			result.setCode(-1);
			result.setMessage("修改对数表信息失败！");
			return result;
		}
		return result;
	}
	@Override
	public Result<TCmPartOriginalCross> update(Map<String, Object> map) {
		Result<TCmPartOriginalCross> result = new Result<TCmPartOriginalCross>();
		JSONObject object = JSONObject.fromObject(map.get("result"));
		String crossId = StringUtil.objToStr(object.get("cmPartOriginalCrossId"));
		TCmPartOriginalCross crossInfo = null;
		try {
			crossInfo = partMapper.selectByPrimaryKey(crossId);
		} catch (Exception e) {
			logger.error("根据对数表ID查询对数表信息失败！",e);
			result.setCode(-1);
			result.setMessage("修改对数表信息失败！");
			return result;
		}
		if(crossInfo == null){
			logger.error("未查询到相关对数表信息！");
			result.setCode(-1);
			result.setMessage("修改对数表信息失败！");
			return result;
		}
		crossInfo.setHighlineRule(StringUtil.objToStr(
				object.get("highlineRule")).equals("null") ? ""
				: StringUtil.objToStr(object.get("highlineRule")));
		crossInfo.setCommonlineRule(StringUtil.objToStr(
				object.get("commonlineRule")).equals("null") ? ""
				: StringUtil.objToStr(object.get("commonlineRule")));
		crossInfo.setPairNbr(StringUtil.objToStr(object.get("pairNbr")));
		crossInfo.setGroupTotalNbr(Short.parseShort(StringUtil
				.objToStr(object.get("groupTotalNbr"))));
		if (object.get("cutOld") != null && (object.get("cutOld").equals("0")
				|| object.get("cutOld").equals("1"))) {
			crossInfo.setCutOld(Short.parseShort(StringUtil.objToStr(object
					.get("cutOld"))));
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
		crossInfo.setElecSupply(Short.parseShort(StringUtil.objToStr(object
				.get("elecSupply"))));
		crossInfo.setDejCollect(Short.parseShort(StringUtil.objToStr(object
				.get("dejCollect"))));
		crossInfo.setAirCondition(Short.parseShort(StringUtil
				.objToStr(object.get("airCondition"))));
		crossInfo.setNote(StringUtil.objToStr(object.get("note")).equals(
				"null") ? "" : StringUtil.objToStr(object.get("note")));

		crossInfo.setMarshallingContent(StringUtil.objToStr(
				object.get("marshallingContent")).equals("null") ? ""
				: StringUtil.objToStr(object.get("marshallingContent")));
		crossInfo.setRunRange(Long.parseLong(StringUtil.objToStr(
				object.get("runRange")).equals("null") ? "" : StringUtil
				.objToStr(object.get("runRange"))));
		crossInfo.setPeopleNums(Integer.valueOf(StringUtil.objToStr(
				object.get("peopleNums")).equals("null") ? "" : StringUtil
				.objToStr(object.get("peopleNums"))));
		crossInfo.setMarshallingNums(Short.parseShort(StringUtil.objToStr(
				object.get("marshallingNums")).equals("null") ? ""
				: StringUtil.objToStr(object.get("marshallingNums"))));
		crossInfo.setCrossLevel(StringUtil.objToStr(
				object.get("crossLevel")).equals("null") ? "" : StringUtil
				.objToStr(object.get("crossLevel")));
		try {
			int code = partMapper.updateByPrimaryKeySelective(crossInfo);
		} catch (Exception e) {
			logger.error("根据对数表ID修改对数表信息失败！",e);
			result.setCode(-1);
			result.setMessage("修改对数表信息失败！");
			return result;
		}
		return result;
	}
	@Override
	public Result<TCmPartOriginalCross> upload(Map<String, Object> map) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)map.get("user");
		Result<TCmPartOriginalCross> result = new Result<TCmPartOriginalCross>();
		InputStream inputStream = (InputStream)map.get("inputStream");
		String versionId = (String)map.get("versionId");
		String versionName = (String)map.get("versionName");
		Map<String, String> bureauCodeMap = trainLineService.getBureauNameDic();
		if(bureauCodeMap == null){
			logger.error("为查询出路局数据字典！");
			result.setCode(-1);
			result.setMessage("导入对数表出错！");
			return result;
		}
		Map<String, Map<String, String>> tokenVehMap = new HashMap<String, Map<String, String>>();
		tokenVehMap.put("tokenVehBureau", bureauCodeMap);
		// 可以从数据库中获取
		
		// 初始化表头映射，因为映射的标题是符合标题行目前不支持，所以直接使用顺序映射(该排列顺序与excel字段排列顺序保持一致)
		LinkedHashMap<String, String> pm = new LinkedHashMap<String, String>();
		int startIndex = 0;
		try {
			Properties pro = new Properties();
			pro.load(this.getClass().getClassLoader().getResourceAsStream("config/originalCrossExcel.properties"));
			String headers = pro.getProperty("originalCross.excel.head");
			startIndex = Integer.parseInt(pro.getProperty("originalCross.excel.startIndex"));
			String[] headArr = headers.split(",");
			for(int i=0; i<headArr.length; i++){
				pm.put(headArr[i], "");
			}
		} catch (Exception e1) {
			logger.error("加载对数表excel配置文件出错",e1);
			result.setCode(-1);
			result.setMessage("导入对数表出错！");
			return result;
		}
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//			int num = workbook.getNumberOfSheets();
			int num = 1;
			for(int i=0; i<num; i++){
				HSSFSheet sheet = workbook.getSheetAt(i);
//				ExcelUtil<TCmPartOriginalCross> excelUtil = new ExcelUtil<TCmPartOriginalCross>(
//						pm, sheet, TCmPartOriginalCross.class);
//				excelUtil.setValueMapping(tokenVehMap);
//				List<TCmPartOriginalCross> list = excelUtil.getEntitiesHasNoHeader(startIndex);
				List<TCmPartOriginalCross> list = null;
				if(list == null || list.size() == 0){
					logger.error("为从excel中解析出对数信息！");
					result.setCode(-1);
					result.setMessage("导入对数表出错！");
					return result;
				}
				for(int j = 0; j < list.size(); j++){
					TCmPartOriginalCross cross = list.get(j);
					String crossName = cross.getCrossName();
					try {
						String[] tranlines = crossName.split("-");
						List<String> viaBureau = null;
//						List<String> viaBureau = trainLineService.getViaBureau(Arrays.asList(tranlines));
						if(!viaBureau.contains(user.getBureauShortName())){
							list.remove(j);
							continue;
						}
						StringBuffer sb = new StringBuffer();
						for(String str : viaBureau){
							sb.append(str + ",");
						}
						cross.setRelevantBureau(sb.toString());
					} catch (Exception e) {
						logger.error("根据交路名获取经由局失败",e);
					}
					cross.setCmVersionId(versionId);
					cross.setCreateBureau(user.getBureau());
					cross.setUseStatus(Constant.USE_STATUS_NORMAL);
				}
				int deleteCode = deleteHomologyData(list);
//				if(deleteCode == -1){
//					logger.error("删除已有对数表信息失败！");
//					result.setCode(-1);
//					result.setMessage("导入对数表信息失败！未能成功覆盖已有的对数表信息！");
//					return result;
//				}
				try {
					int code = partMapper.batchInsert(list);
				} catch (Exception e) {
					logger.error("批量存储对数表信息失败",e);
					result.setCode(-1);
					result.setMessage("存储对数表信息失败！");
					return result;
				}
				
			}
		} catch (Exception e) {
			logger.error("解析excel出错！",e);
			result.setCode(-1);
			result.setMessage("导入对数表出错！");
			return result;
		}
		return result;
	}
	
	/**
	 * 删除导入的重复数据
	 * @Description: TODO
	 * @param @param list
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月26日
	 */
	private int deleteHomologyData(List<TCmPartOriginalCross> list){
		int code = 0;
		List<String> deleteIds = new ArrayList<String>();
		for(TCmPartOriginalCross cross : list){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("crossName", cross.getCrossName());
			paramMap.put("versionId", cross.getCmVersionId());
			paramMap.put("tokenVhBureau", cross.getTokenVehBureau());
			paramMap.put("createBureau", cross.getCreateBureau());
			try {
				List<TCmPartOriginalCross> resultList =  partMapper.queryByParam(paramMap);
				if(resultList != null && resultList.size() > 0){
					for(TCmPartOriginalCross part : resultList){
						deleteIds.add(part.getCmPartOriginalCrossId());
					}
				}else{
					logger.error("deleteHomologyData未查询出符合相关条件的对数表信息！");
					return -1;
				}
			} catch (Exception e) {
				logger.error("deleteHomologyData根据条件查询对数表信息失败！",e);
				return -1;
			}
		}
		if(deleteIds.size() > 0){
			try {
				partMapper.batchDeleteByKeys(deleteIds);
			} catch (Exception e) {
				logger.error("deleteHomologyData批量删除对数表信息失败！",e);
				return -1;
			}
			
		}
		return code;
	}
	
	/**
	 * 判断当前登录用户是否为对数表信息的担当局
	 * @Description: TODO
	 * @param @param bureau
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
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
	
		
	/**
	 * 合并经由局上传的对数表为原始对数表
	 * @Description: TODO
	 * @param @param map   
	 * @return void  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	private void mergePartOriginalCross(Map<String, Object> map){
		List<TCmPartOriginalCross> list = null;
		boolean ifMerge = true;
		try {
			list = partMapper.queryByParam(map);
		} catch (Exception e) {
			logger.error("mergePartOriginalCross根据条件查询对数表信息失败！",e);
			return;
		}
		if(list == null || list.size() == 0){
			logger.error("mergePartOriginalCross根据条件查询对数表信息为空！");
			return;
		}
		for(TCmPartOriginalCross cross : list){
			if(cross.getCheckFlag() == Constant.CHECK_FLAG_RETURN){//若还有没被经由局审核的对数表，则不合并
				ifMerge = false;
			}
		}
		if(ifMerge){
			TCmPartOriginalCross partInfo = list.get(0);
			TCmOriginalCross originalInfo = new TCmOriginalCross();
			try {
				BeanUtils.copyProperties(partInfo, originalInfo, "cmPartOriginalCrossId,createBureau");
				originalInfo.setCheckFlag(Constant.CHECK_FLAG_RETURN);
			} catch (Exception e) {
				logger.error("mergePartOriginalCross BeanUtils复制属性值出错！",e);
				return;
			}
			try {
				originalMapper.insertSelective(originalInfo);
			} catch (Exception e) {
				logger.error("mergePartOriginalCross 存储原始对数表信息出错！",e);
				return;
			}
		}
	}
}
