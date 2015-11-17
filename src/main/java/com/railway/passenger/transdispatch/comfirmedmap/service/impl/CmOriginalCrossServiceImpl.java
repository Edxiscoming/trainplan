package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.sprm.CharacterSprmCompressor;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.basicmap.original.entity.MTrainlineTemp;
import com.railway.basicmap.original.repository.MTrainlineTempMapper;
import com.railway.basicmap.original.service.IMTrainLineTempService;
import com.railway.common.entity.Page;
import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCrossCheck;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TOriginalCrossTrain;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmOriginalCrossCheckMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmOriginalCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmOriginalTrainMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmVersionMapper;
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmOriginalCrossService;
import com.railway.passenger.transdispatch.common.Constant;
import com.railway.passenger.transdispatch.util.ExcelUtil;
import com.railway.passenger.transdispatch.util.PageUtil;
import com.railway.passenger.transdispatch.util.StringUtil;
import com.railway.passenger.transdispatch.util.TimeUtils;

@Service
public class CmOriginalCrossServiceImpl implements ICmOriginalCrossService {
	
	private static Log logger = LogFactory.getLog(CmOriginalCrossServiceImpl.class);

	@Autowired
	private TCmOriginalCrossMapper tCmOriginalCrossMapper;
	
	@Autowired
	private TCmOriginalCrossCheckMapper tCmOriginalCrossCheckMapper;
	
	@Autowired
	private TCmOriginalTrainMapper trainMapper;
	
	@Autowired
	private TCmVersionMapper versionMapper;
	@Autowired
	private TCmCrossMapper cmCrossMapper;
	@Autowired
	private IMTrainLineTempService trainLineService;
	@Autowired
	private MTrainlineTempMapper trainLineMapper;
	
	@Override
	public int insertTCmOriginalCross(TCmOriginalCross ocross, String cmVersionId) {
		ocross.setCmVersionId(cmVersionId);
		return tCmOriginalCrossMapper.insertSelective(ocross);
	}
	
	@Override
	public Result<TCmOriginalCross> queryByPrimaryKey(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		String crossId = (String)map.get("crossId");
		try {
			TCmOriginalCross crossInfo = tCmOriginalCrossMapper.selectByPrimaryKey(crossId);
			if(crossInfo == null){
				logger.error("queryByPrimaryKey根据ID查询单个对数表信息失败！");
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("查询对数表信息失败！");
				return result;
			}
			result.setObj(crossInfo);
		} catch (Exception e) {
			logger.error("根据对数表ID查询对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改对数表信息失败！");
			return result;
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 根据对数表信息获取其Version信息
	 * @param @param ocross
	 * @param @return   
	 * @return TCmVersion  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	public TCmVersion getTCmVersion(String cmVersionId){
		return versionMapper.selectByPrimaryKey(cmVersionId);
	}
	
	@Override
	public TCmOriginalCross getTCmOriginalCross(String cmOriginalCrossId) {
		return tCmOriginalCrossMapper.selectByPrimaryKey(cmOriginalCrossId);
	}


	@Override
	public Result<TCmOriginalCross> check(
			Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		String crossIds = String.valueOf(map.get("crossIds"));
		String roadBureau = String.valueOf(map.get("currentUserBureau"));
		String[] crossIdArr = crossIds.split(",");
		List<String> crossIdList = Arrays.asList(crossIdArr);
		try {
		//	tCmOriginalCrossMapper.batchUpdateCheckFlag(crossIdList);
			//添加路局审核信息
			for(int i=0;i<crossIdList.size();i++){
				String cmOriginalCrossId = crossIdList.get(i);
//				List list = tCmOriginalCrossCheckMapper.selectByPrimaryKey_original(cmOriginalCrossId);
//				if(list.size()>0){
//					tCmOriginalCrossCheckMapper.deleteByPrimaryKey_original(cmOriginalCrossId,roadBureau);
//				}
				TCmOriginalCrossCheck tocc = new TCmOriginalCrossCheck();
				tocc.setCmOriginalCrossId(cmOriginalCrossId);
				tocc.setRoadBureau(roadBureau);
				tocc.setCheckFlag("1");//审核通过
				tCmOriginalCrossCheckMapper.insert(tocc);
			}
			
		} catch (Exception e) {
			logger.error("批量修改对数表审核信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("审核对数表信息失败！");
			return result;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * 用户模拟审核
	 * @see com.railway.passenger.transdispatch.comfirmedmap.service.ICmOriginalCrossService#check_prim(java.util.Map)
	 */
	
	@Override
	public Result<TCmOriginalCross> check_prim(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		String crossIds = String.valueOf(map.get("crossIds"));
		String roadBureau = String.valueOf(map.get("currentUserBureau"));
		String relevantBureau = String.valueOf(map.get("relevantBureau"));
		String[] crossIdArr = crossIds.split(",");
		List<String> crossIdList = Arrays.asList(crossIdArr);
		String[] relevantBureauArrays = relevantBureau.split(",");
		List<String> relevantBureauList = Arrays.asList(relevantBureauArrays);
		try {
		//	tCmOriginalCrossMapper.batchUpdateCheckFlag(crossIdList);
			//添加路局审核信息
			for(int i=0;i<crossIdList.size();i++){
				String cmOriginalCrossId = crossIdList.get(i);
				String relevantBureau_every = relevantBureauList.get(i);
				
//				List list = tCmOriginalCrossCheckMapper.selectByPrimaryKey_original(cmOriginalCrossId);
//				if(list.size()>0){
//					tCmOriginalCrossCheckMapper.deleteByPrimaryKey_original(cmOriginalCrossId,roadBureau);
//				}
				char[] relevantBureau_every_array = relevantBureau_every.toCharArray();
				for(char b : relevantBureau_every_array){
					String bs = String.valueOf(b);
					TCmOriginalCrossCheck tocc = new TCmOriginalCrossCheck();
					tocc.setCmOriginalCrossId(cmOriginalCrossId);
					tocc.setRoadBureau(bs);
					tocc.setCheckFlag("1");//审核通过
					tCmOriginalCrossCheckMapper.insert(tocc);
				}
			}
			
		} catch (Exception e) {
			logger.error("批量修改对数表审核信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("审核对数表信息失败！");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TCmOriginalCross> checkNotPass(
			Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		String crossIds = String.valueOf(map.get("crossIds"));
		String roadBureau = String.valueOf(map.get("currentUserBureau"));
		String[] crossIdArr = crossIds.split(",");
		List<String> crossIdList = Arrays.asList(crossIdArr);
		try {
		//	int code = tCmOriginalCrossMapper.batchUpdateCheckFlagNotPass(crossIdList);
			
			for(int i=0;i<crossIdList.size();i++){
				String cmOriginalCrossId = crossIdList.get(i);
//				List list = tCmOriginalCrossCheckMapper.selectByPrimaryKey_original(cmOriginalCrossId);
//				if(list.size()>0){
//					tCmOriginalCrossCheckMapper.deleteByPrimaryKey_original(cmOriginalCrossId,roadBureau);
//				}
				TCmOriginalCrossCheck tocc = new TCmOriginalCrossCheck();
				tocc.setCmOriginalCrossId(cmOriginalCrossId);
				tocc.setRoadBureau(roadBureau);
				tocc.setCheckFlag("-1");//审核不通过
				tCmOriginalCrossCheckMapper.insert(tocc);
			}
		} catch (Exception e) {
			logger.error("批量修改对数表审核信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("审核对数表信息失败！");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TOriginalCrossTrain> pageQueryCrossAndTrain(
			Map<String, Object> map) {
		Result<TOriginalCrossTrain> result = new Result<TOriginalCrossTrain>();
		List<TOriginalCrossTrain> crossTrainList = new ArrayList<TOriginalCrossTrain>();
		List<TCmOriginalCross> list = null;
		int count = 0;
		new PageUtil(map);
		try {
			list = tCmOriginalCrossMapper.pageQuery(map);
			if(list == null || list.size() == 0){
				logger.error("分页查询对数表信息失败,为查询出数据列表！");
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("分页查询对数表信息失败！");
				return result;
			}
		} catch (Exception e) {
			logger.error("分页查询对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("分页查询对数表信息失败！");
			return result;
		}
		try {
			count = tCmOriginalCrossMapper.queryTotalCount(map);
		} catch (Exception e) {
			logger.error("查询符合条件的数据总数错误！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("分页查询对数表信息失败！");
			return result;
		}
		
		for(TCmOriginalCross cross : list){
			TOriginalCrossTrain crossTrain = new TOriginalCrossTrain();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cmOriginalCrossId", cross.getCmOriginalCrossId());
			List<TCmOriginalTrain> trainList = trainMapper.queryByParam(paramMap);
			crossTrain.setCross(cross);
			crossTrain.setTrainList(trainList);
			crossTrainList.add(crossTrain);
		}
		Page<TOriginalCrossTrain> pageInfo = new Page<TOriginalCrossTrain>();
		pageInfo.setList(crossTrainList);
		pageInfo.setNum(crossTrainList.size());
		pageInfo.setTotalCount(count);
		pageInfo.setPageIndex(Integer.parseInt(String.valueOf(map.get("pageIndex"))));
		pageInfo.setPageSize(Integer.parseInt(String.valueOf(map.get("pageSize"))));
		result.setPageInfo(pageInfo);
		return result;
	}
	
	@Override
	public Result<TCmVersion> queryVersionInfo() {
		Result<TCmVersion> result = new Result<TCmVersion>();
		try {
			List<TCmVersion> list = versionMapper.queryList();
			result.setList(list);
		} catch (Exception e) {
			logger.error("queryVersionInfo 查询版本信息失败！" + e.getMessage());
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("查询版本号信息失败");
			return result;
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result<TCmOriginalCross> upload(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		int importTotalCount = 0;
		int successCount = 0;
		int failCount = 0;
		InputStream inputStream = (InputStream)map.get("inputStream");
		String versionId = (String)map.get("versionId");
		String coverFlag = (String)map.get("coverFlag");
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)map.get("user");
		Map<String, String> bureauCodeMap = trainLineService.getBureauNameDic();
		if(bureauCodeMap == null){
			logger.error("为查询出路局数据字典！");
			result.setCode(Constant.RETURN_CODE_ERROR);
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
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("导入对数表出错！");
			return result;
		}
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//			int num = workbook.getNumberOfSheets();
			int num = 1;
			for(int i=0; i<num; i++){
				HSSFSheet sheet = workbook.getSheetAt(i);
				ExcelUtil<TCmOriginalTrain> excelUtil = new ExcelUtil<TCmOriginalTrain>(
						pm, sheet, TCmOriginalTrain.class, user.getBureau());
				excelUtil.setValueMapping(tokenVehMap);
				List<TCmOriginalTrain> trainList = excelUtil.getEntitiesHasNoHeader(startIndex);
				if(trainList == null || trainList.size() == 0){
					logger.error("为从excel中解析出对数信息！");
					result.setCode(Constant.RETURN_CODE_ERROR);
					result.setMessage("导入对数表出错！");
					return result;
				}
				
				filterTrainByBlank(trainList);//去除解析后有空值的数据
				completeTrainInfo(trainList, versionId);//从基本图中获取车次完整信息
				filterTrainByTokenBureau(trainList, user.getBureau());//去除担当局不是当前登录局的数据
				Map<String, List> listMap = completeDataList(trainList, versionId, user);//根据车次拼装交路信息
				
				List<TCmOriginalCross> originalCrossList = (List<TCmOriginalCross>)listMap.get("crossList");
				List<TCmOriginalTrain> originalTrainList = (List<TCmOriginalTrain>)listMap.get("trainList");
				importTotalCount = originalCrossList.size();
				successCount = originalCrossList.size();
				failCount = importTotalCount - successCount;
				result.setMessage("导入成功！本局担当总交路数：" + importTotalCount + "， 成功导入：" + successCount + "， 失败：" + failCount);
				if(originalCrossList.size() == 0 || originalTrainList.size() == 0){
					return result;
				}
				List<TCmOriginalCross> existCrossList = checkExistCross(originalCrossList);
				if(existCrossList != null && existCrossList.size() > 0){
					if("false".equals(coverFlag)){
						result.setCode(Constant.RETURN_CODE_EXIST);
						result.setMessage("存在相同交路名的数据！");
						return result;
					}
					List<String> existCrossIds = new ArrayList<String>();
					for(TCmOriginalCross cross : existCrossList){
						existCrossIds.add(cross.getCmOriginalCrossId());
					}
					try {
						tCmOriginalCrossMapper.deleteByParam(existCrossList);
						trainMapper.batchDeleteByFKeys(existCrossIds);
					} catch (Exception e) {
						logger.error("覆盖删除对数表信息失败",e);
						result.setCode(Constant.RETURN_CODE_ERROR);
						result.setMessage("覆盖删除相关对数表信息失败");
						return result;
					}
				}
				
				try {
					trainMapper.batchInsert(originalTrainList);
					tCmOriginalCrossMapper.batchInsert(originalCrossList);
				} catch (Exception e) {
					logger.error("批量存储对数表信息失败",e);
					result.setCode(Constant.RETURN_CODE_ERROR);
					result.setMessage("存储对数表信息失败！");
					return result;
				}
				
			}
		} catch (Exception e) {
			logger.error("解析excel出错！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("导入对数表出错！");
			return result;
		}
		return result;
	}
	
	/**
	 * 查询数据库中已存在同条件的交路信息
	 * @Description: TODO
	 * @param @param list
	 * @param @return   
	 * @return List<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月25日
	 */
	private List<TCmOriginalCross> checkExistCross(List<TCmOriginalCross> list){
		List<TCmOriginalCross> crossList = null;
		try {
			crossList = tCmOriginalCrossMapper.queryExistCross(list);
		} catch (Exception e) {
			logger.error("查询数据库queryExistCross失败！",e);
		}
		return crossList;
	}
	
	/**
	 * 去除掉exel中错误录入的空值
	 * @Description: TODO
	 * @param @param list   
	 * @return void  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月24日
	 */
	private void filterTrainByBlank(List<TCmOriginalTrain> list){
		for(Iterator<TCmOriginalTrain> it = list.iterator();it.hasNext();){
			TCmOriginalTrain train = (TCmOriginalTrain)it.next();
			if(train.getTrainName() == null){
				it.remove();
			}
		}
	}
	
	/**
	 * 过滤担当局为非当前登录局的车次
	 * @Description: TODO
	 * @param @param list   
	 * @return void  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月24日
	 */
	private void filterTrainByTokenBureau(List<TCmOriginalTrain> list, String loginBureau){
		for(Iterator<TCmOriginalTrain> it = list.iterator();it.hasNext();){
			TCmOriginalTrain train = (TCmOriginalTrain)it.next();
			if(!loginBureau.equals(train.getTokenVehBureau())){
				it.remove();
			}
		}
	}
	
	
	/**
	 * 从基本图获取车次信息拼装完整对数车次信息(并筛选掉基本图没有对应的车次信息)
	 * @param list
	 * @param versionId
	 */
	private void completeTrainInfo(List<TCmOriginalTrain> list, String versionId){
		TCmVersion version = null;
		try {
			version  = versionMapper.selectByPrimaryKey(versionId);
			if(version == null){
				logger.error("completeTrainInfo 为查询出相关版本号信息");
				return;
			}
		} catch (Exception e) {
			logger.error("completeTrainInfo查询版本号失败！",e);
			return;
		}
		for(TCmOriginalTrain train : list){
			train.setVersionId(version.getCmVersionId());
			train.setVersionName(version.getName());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("trainName", train.getTrainName());
			map.put("versionId", version.getmTemplateScheme());
			List<MTrainlineTemp> trainLineList = null;
			try {
				trainLineList = trainLineMapper.selectByParam(map);
				if(trainLineList == null || trainLineList.size() == 0){
//					logger.info("未在基本图中查询到相关车次信息 trainName:" + train.getTrainName() + ", versionId : " + version.getmTemplateScheme() + ", excelId : " + train.getExcelId());
					continue;
				}
			} catch (Exception e) {
				logger.error("completeTrainInfo 根据车次名称跟版本号ID查询车次信息失败！",e);
				continue;
			}
			MTrainlineTemp trainLineTemp = trainLineList.get(0);
			String sourceNode = trainLineTemp.getSourceNodeStationName() == null || "".equals(trainLineTemp.getSourceNodeStationName()) ? trainLineTemp.getSourceNodeName() : trainLineTemp.getSourceNodeStationName();
			String targetNode = trainLineTemp.getTargetNodeStationName() == null || "".equals(trainLineTemp.getTargetNodeStationName()) ? trainLineTemp.getTargetNodeName() : trainLineTemp.getTargetNodeStationName();
			train.setSourceNode(sourceNode);
			train.setTargetNode(targetNode);
			train.setSourceTime(TimeUtils.date2String(trainLineTemp.getSourceTime(), TimeUtils.DEFAULTFORMAT));
			train.setTargetTime(TimeUtils.date2String(trainLineTemp.getTargetTime(), TimeUtils.DEFAULTFORMAT));
			train.setExecutionSourceTime(TimeUtils.date2String(trainLineTemp.getExecutionSourceTime(), TimeUtils.DEFAULTFORMAT));
			train.setExecutionTargetTime(TimeUtils.date2String(trainLineTemp.getExecutionTargetTime(), TimeUtils.DEFAULTFORMAT));
			String sourceBureauShortName = trainLineTemp.getSourceBureauShortName() == null || "".equals(trainLineTemp.getSourceBureauShortName()) ? "" : trainLineTemp.getSourceBureauShortName();
			String targetBureauShortName = trainLineTemp.getTargetBureauShortName() == null || "".equals(trainLineTemp.getTargetBureauShortName()) ? "" : trainLineTemp.getTargetBureauShortName();
			train.setSourceBureauShortName(sourceBureauShortName);
			train.setTargetBureauShortName(targetBureauShortName);
		}
	}
	
	/**
	 * 检查车次异常情况( 1发站、时间、到站、时间四列是否有为空的，2前车到站与后车发站是否一致，3首车发站与尾车到站是否一致)
	 * @Description: TODO
	 * @param @param list
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月24日
	 */
	private int checkTrainDataException(List<TCmOriginalTrain> list){
		int resultFlag = Constant.EXCEPTION_FLAG_NORMAL;
		int size = list.size();
		int start = 0;
		int end = size - 1;
		int index = 0;
		TCmOriginalTrain startTrain = list.get(start);
		TCmOriginalTrain endTrain = list.get(end);
		TCmOriginalTrain tempTrain = null;
		while(index < size){
			TCmOriginalTrain train = list.get(index);
			if(StringUtil.isEmpty(train.getSourceNode()) || StringUtil.isEmpty(train.getSourceTime()) || StringUtil.isEmpty(train.getTargetNode()) || StringUtil.isEmpty(train.getTargetTime())){
				resultFlag = Constant.EXCEPTION_FLAG_ERROR;
				return resultFlag;
			}
			if(tempTrain == null){
				tempTrain = train;
				index++;
				continue;
			}
			if(!tempTrain.getTargetNode().equals(train.getSourceNode())){
				resultFlag = Constant.EXCEPTION_FLAG_ERROR;
				break;
			}
			tempTrain = train;
			index++;
		}
		if(!startTrain.getSourceNode().equals(endTrain.getTargetNode())){
			resultFlag = Constant.EXCEPTION_FLAG_ERROR;
			return resultFlag;
		}
		return resultFlag;
	}
	
	/**
	 * 根据解析出的车次列表拼装完整的交路信息列表和车次信息列表
	 * @Description: TODO
	 * @param @param list
	 * @param @param versionId
	 * @param @return   
	 * @return Map<String,List>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月4日
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, List> completeDataList(List<TCmOriginalTrain> list, String versionId, ShiroRealm.ShiroUser user){
		Map<String, List> resultMap = new HashMap<String, List>();
		List<TCmOriginalCross> crossList = new ArrayList<TCmOriginalCross>();
		List<TCmOriginalTrain> trainList = new ArrayList<TCmOriginalTrain>();
		int index = 0;
		Map<String, List<TCmOriginalTrain>> groupMap = new HashMap<String, List<TCmOriginalTrain>>();
		for(Iterator<TCmOriginalTrain> it = list.iterator();it.hasNext();){
			TCmOriginalTrain train = (TCmOriginalTrain)it.next();
			if(groupMap.containsKey(train.getExcelId())){
				List<TCmOriginalTrain> valueList = groupMap.get(train.getExcelId());
				train.setTrainSort(String.valueOf(++index));//车次排序
				valueList.add(train);
			}else{
				index = 1;
				List<TCmOriginalTrain> valueList = new ArrayList<TCmOriginalTrain>();
				train.setTrainSort(String.valueOf(index));//车次排序
				valueList.add(train);
				groupMap.put(train.getExcelId(), valueList);
			}
		}
		
		for(Map.Entry<String, List<TCmOriginalTrain>> entry : groupMap.entrySet()){
			List<TCmOriginalTrain> valueList = entry.getValue();
			int exceptionFlag = checkTrainDataException(valueList);//检查车次异常状态
			TCmOriginalCross cross = new TCmOriginalCross();
			StringBuilder crossName = new StringBuilder();
			StringBuilder alternateDate = new StringBuilder();
			StringBuilder alternateTrainNbr = new StringBuilder();
			StringBuilder appointWeek = new StringBuilder();
			StringBuilder appointDay = new StringBuilder();
			StringBuilder appointPeriod = new StringBuilder();
			StringBuilder highlineRule = new StringBuilder();
			StringBuilder commonlineRule = new StringBuilder();
			StringBuilder sourcebureaushortName = new StringBuilder();
			StringBuilder targetbureaushortName = new StringBuilder();
			for(TCmOriginalTrain train : valueList){
				train.setCmOriginalCrossId(cross.getCmOriginalCrossId());
				train.setExceptionflag(exceptionFlag);
				train.setUseStatus(Constant.USE_STATUS_NORMAL);
				cross.setTokenVehBureau(train.getTokenVehBureau());
				crossName.append("-").append(train.getTrainName());
				if(train.getAlternateDate() != null && !"".equals(train.getAlternateDate())){
					alternateDate.append("-").append(train.getAlternateDate());
				}
				if(train.getAlternateTrainNbr() != null && !"".equals(train.getAlternateTrainNbr())){
					alternateTrainNbr.append("-").append(train.getAlternateTrainNbr());
				}
				if(train.getAppointWeek() != null && !"".equals(train.getAppointWeek())){
					appointWeek.append("-").append(train.getAppointWeek());
				}
				if(train.getAppointDay() != null && !"".equals(train.getAppointDay())){
					appointDay.append("-").append(train.getAppointDay());
				}
				if(train.getAppointPeriod() != null && !"".equals(train.getAppointPeriod())){
					appointPeriod.append("-").append(train.getAppointPeriod());
				}
				if(train.getHighlineRule() != null && !"".equals(train.getHighlineRule())){
					highlineRule.append("-").append(train.getHighlineRule());
				}
				if(train.getCommonlineRule() != null && !"".equals(train.getCommonlineRule())){
					commonlineRule.append("-").append(train.getCommonlineRule());
				}
				if(train.getSourceBureauShortName() != null && !"".equals(train.getSourceBureauShortName())){
					sourcebureaushortName.append("-").append(train.getSourceBureauShortName());
				}
				if(train.getTargetBureauShortName() != null && !"".equals(train.getTargetBureauShortName())){
					targetbureaushortName.append("-").append(train.getTargetBureauShortName());
				}
			}
			cross.setCrossName(crossName.length() > 0 ? crossName.substring(1) : "");
			cross.setAlternateDate(alternateDate.length() > 0 ? alternateDate.substring(1) : "");
			cross.setAlternateTrainNbr(alternateTrainNbr.length() > 0 ? alternateTrainNbr.substring(1) : "");
			cross.setAppointWeek(appointWeek.length() > 0 ? appointWeek.substring(1) : "");
			cross.setAppointDay(appointDay.length() > 0 ? appointDay.substring(1) : "");
			cross.setAppointPeriod(appointPeriod.length() > 0 ? appointPeriod.substring(1) : "");
			cross.setHighlineRule(highlineRule.length() > 0 ? highlineRule.substring(1) : "");
			cross.setHighlineRule(commonlineRule.length() > 0 ? commonlineRule.substring(1) : "");
			String s="";
			String t="";
			if(sourcebureaushortName.length() > 0){
				s = sourcebureaushortName.substring(1,2)+sourcebureaushortName.substring(sourcebureaushortName.length()-1);
			}
			if(targetbureaushortName.length() > 0){
				t = targetbureaushortName.substring(1,2)+targetbureaushortName.substring(targetbureaushortName.length()-1);
			}
			
			cross.setSourceBureauShortName(s);
			cross.setTargetBureauShortName(t);
			cross.setCmVersionId(versionId);
			cross.setExceptionflag(exceptionFlag);
			cross.setUseStatus(Constant.USE_STATUS_UNLOAD);
			cross.setCreateCrossFlag(Constant.CREATE_CROSS_NO);
			cross.setRelevantBureau(getViaBureau(valueList));
			cross.setHighlineFlag(Constant.HIGHLINE_FLAG_HIGHLINE);
			cross.setCreatPeople(user.getBureauFullName());
			cross.setCheckPeopleOrg(user.getBureauFullName());
			crossList.add(cross);
			trainList.addAll(valueList);
		}
		resultMap.put("crossList", crossList);
		resultMap.put("trainList", trainList);
		return resultMap;
	}
	
	@Override
	public Result<TCmOriginalCross> add(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)map.get("user");
		JSONObject object = JSONObject.fromObject(map.get("cross"));
		JSONArray trainJsonArray = JSONArray.fromObject(map.get("trains"));
		TCmOriginalCross crossInfo  = (TCmOriginalCross)JSONObject.toBean(object, TCmOriginalCross.class);
		List<TCmOriginalTrain> trainList = (List<TCmOriginalTrain>)JSONArray.toCollection(trainJsonArray, TCmOriginalTrain.class);
		crossInfo.setTokenVehBureau(user.getBureau());
		crossInfo.setCreatPeople(user.getBureauFullName());
		crossInfo.setCheckPeopleOrg(user.getBureauFullName());
		crossInfo.setCreatTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
		crossInfo.setCmOriginalCrossId(UUID.randomUUID().toString());
		crossInfo.setHighlineFlag(Constant.HIGHLINE_FLAG_HIGHLINE);
		for(TCmOriginalTrain train : trainList){
			transferSchemaId2VersionId(train);
			train.setCmOriginalCrossId(crossInfo.getCmOriginalCrossId());
			train.setCmOriginalTrainId(UUID.randomUUID().toString());
			train.setTokenVehBureau(user.getBureau());
		}
		crossInfo.setRelevantBureau(getViaBureau(trainList));
		try {
			trainMapper.batchInsert(trainList);
		} catch (Exception e) {
			logger.error("add 数据库报错车次失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("新增对数表信息失败！");
			return result;
		}
		try {
			tCmOriginalCrossMapper.insertSelective(crossInfo);
		} catch (Exception e) {
			logger.error("add 数据库报错对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("新增对数表信息失败！");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TCmOriginalCross> updateCrossTrain(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		JSONObject object = JSONObject.fromObject(map.get("cross"));
		JSONArray trainJsonArray = JSONArray.fromObject(map.get("trains"));
		JSONArray delIdArray = JSONArray.fromObject(map.get("deleteIds"));
		TCmOriginalCross crossInfo  = (TCmOriginalCross)JSONObject.toBean(object, TCmOriginalCross.class);
		String tokenVehBureau = crossInfo.getTokenVehBureau();
		List<TCmOriginalTrain> trainList = (List<TCmOriginalTrain>)JSONArray.toCollection(trainJsonArray, TCmOriginalTrain.class);
		List<String> delIdList = (List<String>)JSONArray.toCollection(delIdArray, String.class);
		if(delIdList != null && delIdList.size() > 0){
			try {
				trainMapper.batchDeleteByKeys(delIdList);
			} catch (Exception e) {
				logger.error("update 根据ID删除对数表车次信息失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("修改对数表信息失败！");
				return result;
			}
		}
		if(trainList == null || trainList.size() == 0){
			try {
				tCmOriginalCrossMapper.deleteByPrimaryKey(crossInfo.getCmOriginalCrossId());
			} catch (Exception e) {
				logger.error("update 根据ID删除对数表信息失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("修改对数表信息失败！");
				return result;
			}
			return result;
		}
		for(TCmOriginalTrain train : trainList){
			if(train.getCmOriginalCrossId() != null){
				try {
					trainMapper.updateByPrimaryKeySelective(train);
				} catch (Exception e) {
					logger.error("update 数据库修改车次信息失败！",e);
					result.setCode(Constant.RETURN_CODE_ERROR);
					result.setMessage("修改对数表信息失败！");
					return result;
				}
			}else{
				transferSchemaId2VersionId(train);
				train.setCmOriginalCrossId(crossInfo.getCmOriginalCrossId());
				train.setCmOriginalTrainId(UUID.randomUUID().toString());
				train.setTokenVehBureau(tokenVehBureau);
				train.setCreatTime(TimeUtils.date2String(new Date(), TimeUtils.DEFAULTFORMAT));
				try {
					trainMapper.insertSelective(train);
				} catch (Exception e) {
					logger.error("update 数据存储新车次信息失败！",e);
					result.setCode(Constant.RETURN_CODE_ERROR);
					result.setMessage("修改对数表信息失败！");
					return result;
				}
			}
		}
		crossInfo.setRelevantBureau(getViaBureau(trainList));
		updateCrossOperationRule(crossInfo, trainList);
		try {
			tCmOriginalCrossMapper.updateByPrimaryKeySelective(crossInfo);
		} catch (Exception e) {
			logger.error("update 数据库修改对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改对数表信息失败！");
			return result;
		}
		return result;
	}
	
	/**
	 * 根据车次列表拼装交路开心计划相关信息
	 * @param crossInfo
	 * @param trainList
	 */
	private void updateCrossOperationRule(TCmOriginalCross crossInfo, List<TCmOriginalTrain> trainList){
		StringBuilder alternateDateB = new StringBuilder();
		StringBuilder alternateTrainNbrB = new StringBuilder();
		StringBuilder appointWeekB = new StringBuilder();
		StringBuilder appointDayB = new StringBuilder();
		StringBuilder appointPeriodB = new StringBuilder();
		StringBuilder highlineRuleB = new StringBuilder();
		StringBuilder commonlineRuleB = new StringBuilder();
		StringBuilder spareFlagB = new StringBuilder();
		for(TCmOriginalTrain train : trainList){
			alternateDateB.append("-").append(train.getAlternateDate() == null || (train.getAlternateDate() != null && "".equals(train.getAlternateDate())) ? " " : train.getAlternateDate());
			alternateTrainNbrB.append("-").append(train.getAlternateTrainNbr() == null || (train.getAlternateTrainNbr() != null && "".equals(train.getAlternateTrainNbr())) ? " " : train.getAlternateTrainNbr());
			appointWeekB.append("-").append(train.getAppointWeek() == null || (train.getAppointWeek() != null && "".equals(train.getAppointWeek())) ? " " : train.getAppointWeek());
			appointDayB.append("-").append(train.getAppointDay() == null || (train.getAppointDay() != null && "".equals(train.getAppointDay())) ? " " : train.getAppointDay());
			appointPeriodB.append("-").append(train.getAppointPeriod() == null || (train.getAppointPeriod() != null && "".equals(train.getAppointPeriod())) ? " " : train.getAppointPeriod());
			highlineRuleB.append("-").append(train.getHighlineRule() == null || (train.getHighlineRule() != null && "".equals(train.getHighlineRule())) ? " " : train.getHighlineRule());
			commonlineRuleB.append("-").append(train.getCommonlineRule() == null || (train.getCommonlineRule() != null && "".equals(train.getCommonlineRule())) ? " " : train.getCommonlineRule());
			spareFlagB.append("-").append(train.getSpareFlag() == null || (train.getSpareFlag() != null && "".equals(train.getSpareFlag())) ? " " : train.getSpareFlag());
		}
		String alternateDate = alternateDateB.toString().replaceAll("-", "").trim().length() == 0 ? "" : alternateDateB.toString().substring(1);
		String alternateTrainNbr = alternateTrainNbrB.toString().toString().replaceAll("-", "").trim().length() == 0 ? "" : alternateTrainNbrB.toString().substring(1);
		String appointWeek =appointWeekB.toString().toString().replaceAll("-", "").trim().length() == 0 ? "" : appointWeekB.toString().substring(1);
		String appointDay = appointDayB.toString().toString().replaceAll("-", "").trim().length() == 0 ? "" : appointDayB.toString().substring(1);
		String appointPeriod = appointPeriodB.toString().toString().replaceAll("-", "").trim().length() == 0 ? "" : appointPeriodB.toString().substring(1);
		String highlineRule = highlineRuleB.toString().toString().replaceAll("-", "").trim().length() == 0 ? "" : highlineRuleB.toString().substring(1);
		String commonlineRule = commonlineRuleB.toString().toString().replaceAll("-", "").trim().length() == 0 ? "" : commonlineRuleB.toString().substring(1);
		String spareFlag = spareFlagB.toString().toString().replaceAll("-", "").trim().length() == 0 ? "" : spareFlagB.toString().substring(1);
		crossInfo.setAlternateDate(alternateDate);
		crossInfo.setAlternateTrainNbr(alternateTrainNbr);
		crossInfo.setAppointWeek(appointWeek);
		crossInfo.setAppointDay(appointDay);
		crossInfo.setAppointPeriod(appointPeriod);
		crossInfo.setHighlineRule(highlineRule);
		crossInfo.setCommonlineRule(commonlineRule);
		crossInfo.setSpareFlag(spareFlag);
	}
	
	@Override
	public Result<TCmOriginalCross> updateTrainInfo(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		JSONObject crossObj = JSONObject.fromObject(map.get("cross"));
		JSONObject trainObj = JSONObject.fromObject(map.get("train"));
		TCmOriginalCross cross  = (TCmOriginalCross)JSONObject.toBean(crossObj, TCmOriginalCross.class);
		TCmOriginalTrain trainInfo  = (TCmOriginalTrain)JSONObject.toBean(trainObj, TCmOriginalTrain.class);
		TCmOriginalCross crossInfo = null;
		int trainCount = 0;
		try {
			trainCount = trainMapper.queryTrainCountByCrossId(cross.getCmOriginalCrossId());
		} catch (Exception e) {
			logger.error("updateTrainInfo 数据库查询车次总数失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改车次信息失败！");
			return result;
		}
		if(trainCount == 0){
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改交路信息失败！");
			return result;
		}
		try {
			crossInfo = tCmOriginalCrossMapper.selectByPrimaryKey(cross.getCmOriginalCrossId());
		} catch (Exception e) {
			logger.error("updateTrainInfo 数据库根据ID查询对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改车次信息失败！");
			return result;
		}
		if(crossInfo == null){
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改交路信息失败！");
			return result;
		}
		int index = Integer.valueOf(trainInfo.getTrainSort());
		
		String[] alternateDate = crossInfo.getAlternateDate() != null ? crossInfo.getAlternateDate().split("-") : new String[trainCount];
		String[] alternateTrainNbr = crossInfo.getAlternateTrainNbr() != null ? crossInfo.getAlternateTrainNbr().split("-") : new String[trainCount];
		String[] appointWeek = crossInfo.getAppointWeek() != null ? crossInfo.getAppointWeek().split("-") : new String[trainCount];
		String[] appointDay = crossInfo.getAppointDay() != null ? crossInfo.getAppointDay().split("-") : new String[trainCount];
		String[] appointPeriod = crossInfo.getAppointPeriod() != null ? crossInfo.getAppointPeriod().split("-") : new String[trainCount];
		String[] highlineRule = crossInfo.getHighlineRule() != null ? crossInfo.getHighlineRule().split("-") : new String[trainCount];
		String[] commonlineRule = crossInfo.getCommonlineRule() != null ? crossInfo.getCommonlineRule().split("-") : new String[trainCount];
		String[] spareFlag = crossInfo.getSpareFlag() != null ? crossInfo.getSpareFlag().split("-") : new String[trainCount];
//		String[] highLineFlag = crossInfo.getHighlineFlag() != null ? crossInfo.getHighlineFlag().split("-") : new String[trainCount];
		
		try {
			alternateDate[index - 1] = trainInfo.getAlternateDate() !=null && trainInfo.getAlternateDate().equals("") ? " " : trainInfo.getAlternateDate();
			alternateTrainNbr[index - 1] = trainInfo.getAlternateTrainNbr() !=null && trainInfo.getAlternateTrainNbr().equals("") ? " " : trainInfo.getAlternateTrainNbr();
			appointWeek[index - 1] = trainInfo.getAppointWeek() !=null && trainInfo.getAppointWeek().equals("") ? " " : trainInfo.getAppointWeek();
			appointDay[index - 1] = trainInfo.getAppointDay() !=null && trainInfo.getAppointDay().equals("") ? " " : trainInfo.getAppointDay();
			appointPeriod[index - 1] = trainInfo.getAppointPeriod() !=null && trainInfo.getAppointPeriod().equals("") ? " " : trainInfo.getAppointPeriod();
			highlineRule[index - 1] = trainInfo.getHighlineRule() !=null && trainInfo.getHighlineRule().equals("") ? " " : trainInfo.getHighlineRule();
			commonlineRule[index - 1] = trainInfo.getCommonlineRule() !=null && trainInfo.getCommonlineRule().equals("") ? " " : trainInfo.getCommonlineRule();
			spareFlag[index - 1] = trainInfo.getSpareFlag() !=null && trainInfo.getSpareFlag().equals("") ? " " : trainInfo.getSpareFlag();
//			highLineFlag[index - 1] = trainInfo.getHighlineFlag() !=null && trainInfo.getHighlineFlag().equals("") ? " " : trainInfo.getHighlineFlag();
		
			StringBuilder alternateDateB = new StringBuilder();
			StringBuilder alternateTrainNbrB = new StringBuilder();
			StringBuilder appointWeekB = new StringBuilder();
			StringBuilder appointDayB = new StringBuilder();
			StringBuilder appointPeriodB = new StringBuilder();
			StringBuilder highlineRuleB = new StringBuilder();
			StringBuilder commonlineRuleB = new StringBuilder();
			StringBuilder spareFlagB = new StringBuilder();
//			StringBuilder highLineFlagB = new StringBuilder();
			
			for(int i = 0; i < alternateDate.length; i++){
				if(i == 0){
					alternateDateB.append(alternateDate[i] != null ? alternateDate[i] : " ");
				}else{
					alternateDateB.append("-").append(alternateDate[i] != null ? alternateDate[i] : " ");
				}
			}
			for(int i = 0; i < alternateTrainNbr.length; i++){
				if(i == 0){
					alternateTrainNbrB.append(alternateTrainNbr[i] != null ? alternateTrainNbr[i] : " ");
				}else{
					alternateTrainNbrB.append("-").append(alternateTrainNbr[i] != null ? alternateTrainNbr[i] : " ");
				}
			}
			for(int i = 0; i < appointWeek.length; i++){
				if(i == 0){
					appointWeekB.append(appointWeek[i] != null ? appointWeek[i] : " ");
				}else{
					appointWeekB.append("-").append(appointWeek[i] != null ? appointWeek[i] : " ");
				}
			}
			for(int i = 0; i < appointDay.length; i++){
				if(i == 0){
					appointDayB.append(appointDay[i] != null ? appointDay[i] : " ");
				}else{
					appointDayB.append("-").append(appointDay[i] != null ? appointDay[i] : " ");
				}
			}
			for(int i = 0; i < appointPeriod.length; i++){
				if(i == 0){
					appointPeriodB.append(appointPeriod[i] != null ? appointPeriod[i] : " ");
				}else{
					appointPeriodB.append("-").append(appointPeriod[i] != null ? appointPeriod[i] : " ");
				}
			}
			for(int i = 0; i < highlineRule.length; i++){
				if(i == 0){
					highlineRuleB.append(highlineRule[i] != null ? highlineRule[i] : " ");
				}else{
					highlineRuleB.append("-").append(highlineRule[i] != null ? highlineRule[i] : " ");
				}
			}
			for(int i = 0; i < commonlineRule.length; i++){
				if(i == 0){
					commonlineRuleB.append(commonlineRule[i] != null ? commonlineRule[i] : " ");
				}else{
					commonlineRuleB.append("-").append(commonlineRule[i] != null ? commonlineRule[i] : " ");
				}
			}
			for(int i = 0; i < spareFlag.length; i++){
				if(i == 0){
					spareFlagB.append(spareFlag[i] != null ? spareFlag[i] : " ");
				}else{
					spareFlagB.append("-").append(spareFlag[i] != null ? spareFlag[i] : " ");
				}
			}
//			for(int i = 0; i < highLineFlag.length; i++){
//				if(i == 0){
//					highLineFlagB.append(highLineFlag[i] != null ? highLineFlag[i] : " ");
//				}else{
//					highLineFlagB.append("-").append(highLineFlag[i] != null ? highLineFlag[i] : " ");
//				}
//			}
	
			crossInfo.setAlternateDate(alternateDateB.toString().replaceAll("-", "").trim().length() == 0 ? "" : alternateDateB.toString());
			crossInfo.setAlternateTrainNbr(alternateTrainNbrB.toString().replaceAll("-", "").trim().length() == 0 ? "" : alternateTrainNbrB.toString());
			crossInfo.setAppointWeek(appointWeekB.toString().replaceAll("-", "").trim().length() == 0 ? "" : appointWeekB.toString());
			crossInfo.setAppointDay(appointDayB.toString().replaceAll("-", "").trim().length() == 0 ? "" : appointDayB.toString());
			crossInfo.setAppointPeriod(appointPeriodB.toString().replaceAll("-", "").trim().length() == 0 ? "" : appointPeriodB.toString());
			crossInfo.setHighlineRule(highlineRuleB.toString().replaceAll("-", "").trim().length() == 0 ? "" : highlineRuleB.toString());
//			crossInfo.setHighlineFlag(highLineFlagB.toString().replaceAll("-", "").trim().length() == 0 ? "0" : highLineFlagB.toString());
			crossInfo.setCommonlineRule(commonlineRuleB.toString().replaceAll("-", "").trim().length() == 0 ? "" : commonlineRuleB.toString());
			crossInfo.setSpareFlag(spareFlagB.toString().replaceAll("-", "").trim().length() == 0 ? "" : spareFlagB.toString());
		} catch (Exception e) {
			logger.error("updateTrainInfo 组合车次信息为交路信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改车次信息失败！");
			return result;
		}
		
		try {
			trainMapper.updateByPrimaryKeySelective(trainInfo);
		} catch (Exception e) {
			logger.error("updateTrainInfo 数据库修改车次信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改车次信息失败！");
			return result;
		}
		
		try {
			tCmOriginalCrossMapper.updateByPrimaryKeySelective(crossInfo);
		} catch (Exception e) {
			logger.error("updateTrainInfo 修改对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改车次信息失败！");
			return result;
		}
		
		return result;
	}
	
	@Override
	public Result<TCmOriginalCross> pageQueryCross(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		List<TCmOriginalCross> list = null;
		List<TCmOriginalCrossCheck> list_check = null;
		int count = 0;
		
		new PageUtil(map);
		try {
			list = tCmOriginalCrossMapper.pageQuery(map);
			
			for(int i=0;i<list.size();i++){
				TCmOriginalCross tcoc = list.get(i);
				String cmOriginalId = tcoc.getCmOriginalCrossId();
				String rb = tcoc.getRelevantBureau();
				int roadBureau = tcoc.getRelevantBureau().length();
				//根据对数ID查询审核信息
				list_check = tCmOriginalCrossCheckMapper.selectByPrimaryKey_original(cmOriginalId);
				String flag="";
				String check_t="";
				String check_b="";
				String check_n="";
				String check_wor="";
				if(list_check.size()>0){
					for(int m=0;m<list_check.size();m++){
						TCmOriginalCrossCheck tcocc = list_check.get(m);
						flag = tcocc.getCheckFlag();
						if(("1").equals(flag)){
							check_t += tcocc.getRoadBureau();//审核通过的路局
						}else if(("-1").equals(flag)){
							check_b += tcocc.getRoadBureau();//审核不通过的路局
						}else{
							
						}
						//如果有本局审核数据，说明本局已经审核
						if(tcocc.getRoadBureau().equals(map.get("loginBureau"))){
							check_wor = tcocc.getCheckFlag();
						}else{
							
						}
						
						check_n = rb.replace(tcocc.getRoadBureau(), "");
						rb = check_n;
					}
					if(check_t.length()<=0){
						check_t = "无";
					}
					if(check_b.length()<=0){
						check_b = "无";
					}
					if(check_n.length()<=0){
						check_n = "无";
					}
					
					tcoc.setCheck_wor(check_wor);
					tcoc.setCheck_t(check_t);
					tcoc.setCheck_b(check_b);
					tcoc.setCheck_n(check_n);//未审核的路局
					
					
				}else{
					
					check_t = "无";
					check_b = "无";
					check_n = tcoc.getRelevantBureau();
					tcoc.setCheck_t(check_t);
					tcoc.setCheck_b(check_b);
					tcoc.setCheck_n(check_n);//未审核的路局
					tcoc.setCheck_wor(check_wor);
				}
				//本局管理
				if(!("-1").equals(map.get("tokenVehBureau"))){
					if(roadBureau>list_check.size()){
						tcoc.setAllCheckFlag("0");//不是所有路局都通过了审核或者某些路局未审核
					}else{
						tcoc.setAllCheckFlag("1");
					}
				}else{//外局管理
					if(roadBureau>list_check.size()){
						tcoc.setAllCheckFlag("0");//不是所有路局都通过了审核或者某些路局未审核
					}else if(check_b.length()>0){
						tcoc.setAllCheckFlag("-1");
					}else{
						tcoc.setAllCheckFlag("1");
					}
				}
				
				list.set(i, tcoc);
			}
			
			if(list == null || list.size() == 0){
				logger.error("分页查询对数表信息失败,为查询出数据列表！");
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("分页查询对数表信息失败！");
				return result;
			}
		} catch (Exception e) {
			logger.error("分页查询对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("分页查询对数表信息失败！");
			return result;
		}
		try {
			count = tCmOriginalCrossMapper.queryTotalCount(map);
		} catch (Exception e) {
			logger.error("查询符合条件的数据总数错误！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("分页查询对数表信息失败！");
			return result;
		}
		Page<TCmOriginalCross> pageInfo = new Page<TCmOriginalCross>();
		pageInfo.setList(list);
		pageInfo.setNum(list.size());
		pageInfo.setTotalCount(count);
		pageInfo.setPageIndex(Integer.parseInt(String.valueOf(map.get("pageIndex"))));
		pageInfo.setPageSize(Integer.parseInt(String.valueOf(map.get("pageSize"))));
		result.setPageInfo(pageInfo);
		return result;
	} 
	public int queryTotalCount(Map<String, Object> map){
		return tCmOriginalCrossMapper.queryTotalCount(map);
	}
	@Override
	public Result<TCmOriginalCross> updateCross(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		JSONObject object = JSONObject.fromObject(map.get("cross"));
		TCmOriginalCross crossInfo  = (TCmOriginalCross)JSONObject.toBean(object, TCmOriginalCross.class);
		if(crossInfo == null){
			logger.error("updateCross 参数错误，未解析出对数表对象！");
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改对数表信息失败");
			return result;
		}
		try {
			tCmOriginalCrossMapper.updateByPrimaryKeySelective(crossInfo);
		} catch (Exception e) {
			logger.error("updateCross 数据库修改对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("修改对数表信息失败！");
			return result;
		}
		return result;
	}
	@Override
	public Result<TCmOriginalCross> deleteCrossTrain(Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		String crossIds = String.valueOf(map.get("crossIds"));
		String[] crossIdArr = crossIds.split(",");
		List<String> crossIdList = Arrays.asList(crossIdArr);
		
		try {
			trainMapper.batchDeleteByFKeys(crossIdList);
		} catch (Exception e) {
			logger.error("deleteCrossTrain 数据库批量删除对数表车次信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除对数表跟车次信息失败！");
			return result;
		}
		try {
			tCmOriginalCrossMapper.deleteByKeys(crossIdList);
		} catch (Exception e) {
			logger.error("deleteCrossTrain 数据库批量删除对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除对数表跟车次信息失败！");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TCmOriginalCross> queryByPrimaryKeys(String cmOriginalCrossIds) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		try {
			String[] crossIdArray = cmOriginalCrossIds.split(",");
			List<TCmOriginalCross> list = tCmOriginalCrossMapper.selectByPrimaryKeys(Arrays.asList(crossIdArray));
			result.setList(list);
		} catch (Exception e) {
			logger.error("queryByPrimaryKeys 数据库根据主键列表批量查询对数表信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("批量查询对数表信息失败！");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TCmOriginalCross> markCrossCreateFlag(
			Map<String, Object> map) {
		Result<TCmOriginalCross> result = new Result<TCmOriginalCross>();
		List<TCmOriginalCross> list = (List<TCmOriginalCross>)map.get("list");
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)map.get("user");
		if(list != null && list.size() > 0){
			for(TCmOriginalCross cross : list){
				//cross.setCheckFlag((short)1);
				cross.setCheckTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
				cross.setCheckPeople(user.getName());
				cross.setCheckPeopleOrg(user.getBureau());
				cross.setCreatCrossTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
				cross.setCreateCrossFlag(1);
			}
			try {
				tCmOriginalCrossMapper.batchUpdateOriginalCross(list);
			} catch (Exception e) {
				logger.error("markCrossCreateFlag 数据库批量修改对数表交路生成标志失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("标注交路生成信息失败！");
				return result;
			}
		}
		return result;
	}
	
	/**
	 * 过去经由局信息
	 * @Description: TODO
	 * @param @param trainLines
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月7日
	 */
	private String getViaBureau(List<TCmOriginalTrain> trainLines){
		String viaBureau = "";
		Map<String, String> versionMap = new HashMap<String, String>();
		try {
			List<TCmVersion> list = versionMapper.queryList();
			for(TCmVersion version : list){
				versionMap.put(version.getCmVersionId(), version.getmTemplateScheme());
			}
		} catch (Exception e) {
			logger.error("查询客运系统版本号信息失败", e);
			return viaBureau;
		}
		List<Map<String, Object>> paramMapList = null;
		if(trainLines != null && trainLines.size() > 0){
			paramMapList = new ArrayList<Map<String, Object>>();
			for(TCmOriginalTrain train : trainLines){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", train.getTrainName());
				map.put("versionId", versionMap.get(train.getVersionId()));
				paramMapList.add(map);
			}
		}
		if(paramMapList == null){
			return viaBureau;
		}
		try {
			List<String> viaList = trainLineMapper.queryViaBureauByTrainNames(paramMapList);
			Map<String,String> map = trainLineService.getBureauShortNameDic();
			if(viaList == null || viaList.size() == 0){
				logger.debug("未从基本图里查询到相关车次：" + paramMapList);
				return viaBureau;
			}
			if(map == null){
				logger.error("未查询出路局数据字典");
				return viaBureau;
			}
			Set<String> result = new HashSet<String>();
			StringBuffer sb = new StringBuffer();
			for(String str : viaList){
				char[] ca = str.toCharArray();
				for(int i=0;i<ca.length;i++){
					result.add(map.get(String.valueOf(ca[i])));
				}
			}
			for(String str : result){
				sb.append(str);
			}
			viaBureau = sb.toString();
		} catch (Exception e) {
			logger.error("getViaBureau 查询基本图车次经由局失败！",e);
		}
		return viaBureau;
	}
	
	@Override
	public Result<TOriginalCrossTrain> queryCrossWithTrain(
			Map<String, Object> map) {
		Result<TOriginalCrossTrain> result = new Result<TOriginalCrossTrain>();
		String crossId = String.valueOf(map.get("crossId"));
		String trainId = String.valueOf(map.get("trainId"));
		try {
			TCmOriginalCross cross = tCmOriginalCrossMapper.selectByPrimaryKey(crossId);
			TCmOriginalTrain train = trainMapper.selectByPrimaryKey(trainId);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cmOriginalCrossId", crossId);
			List<TCmOriginalTrain> trainList = trainMapper.queryByParam(paramMap);
			TOriginalCrossTrain resultObj = new TOriginalCrossTrain();
			resultObj.setCross(cross);
			resultObj.setTrain(train);
			resultObj.setTrainList(trainList);
			result.setData(resultObj);
		} catch (Exception e) {
			logger.error("queryCrossWithTrain 数据库查询 对数表信息跟车次信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("查询对数表车次详情失败！");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TOriginalCrossTrain> deleteTrainInfo(Map<String, Object> map) {
		Result<TOriginalCrossTrain> result = new Result<TOriginalCrossTrain>();
		String trainId = String.valueOf(map.get("trainId"));
		String crossId = String.valueOf(map.get("crossId"));
		List<TCmOriginalTrain> trainList = null;
		TCmOriginalCross crossInfo = null;
		int trainSort = 0;
		try {
			TCmOriginalTrain delTrain = trainMapper.selectByPrimaryKey(trainId);
			if(delTrain == null){
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("删除车次信息失败");
				return result;
			}
			trainSort = Integer.parseInt(delTrain.getTrainSort());
		} catch (Exception e) {
			logger.error("deleteTrainInfo 数据库查询车次信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除车次信息失败");
			return result;
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("cmOriginalCrossId", crossId);
			trainList = trainMapper.queryByParam(paramMap);
			if(trainList == null || trainList.size() == 0){
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("删除车次信息失败");
				return result;
			}
		} catch (Exception e) {
			logger.error("deleteTrainInfo 数据库查询车次信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除车次信息失败");
			return result;
		}
		if(trainList.size() == 1){
			try {
				trainMapper.deleteByPrimaryKey(trainId);
				tCmOriginalCrossMapper.deleteByPrimaryKey(crossId);
			} catch (Exception e) {
				logger.error("deleteTrainInfo 数据库删除原始对数车次信息失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("删除车次信息失败");
				return result;
			}
			return result;
		}
		try {
			crossInfo = tCmOriginalCrossMapper.selectByPrimaryKey(crossId);
			if(crossInfo == null){
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("删除车次信息失败");
				return result;
			}
		} catch (Exception e) {
			logger.error("deleteTrainInfo 数据库查询原始对数信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除车次信息失败");
			return result;
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < trainList.size(); i++){
			int thisSort = Integer.parseInt(trainList.get(i).getTrainSort());
			if(thisSort != trainSort){
				sb.append("-").append(trainList.get(i).getTrainName());
			}else{
				trainList.remove(i);
				i--;
				continue;
			}
			if(thisSort > trainSort){
				thisSort--;
			}
			trainList.get(i).setTrainSort(String.valueOf(thisSort));
		}
		crossInfo.setCrossName(sb.substring(1));
		updateCrossOperationRule(crossInfo, trainList);
		try {
			trainMapper.deleteByPrimaryKey(trainId);
		} catch (Exception e) {
			logger.error("deleteTrainInfo 数据库删除车次信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除车次信息失败");
			return result;
		}
		try {
			trainMapper.batchUpdateTrainsSort(trainList);
		} catch (Exception e) {
			logger.error("deleteTrainInfo 批量修改车次信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除车次信息失败");
			return result;
		}
		try {
			tCmOriginalCrossMapper.updateByPrimaryKeySelective(crossInfo);
		} catch (Exception e) {
			logger.error("deleteTrainInfo 修改原始对数信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("删除车次信息失败");
			return result;
		}
		return result;
	}
	
	public int update(TCmOriginalCross ocross){
		return tCmOriginalCrossMapper.updateByPrimaryKeySelective(ocross);
	}
	
	/**
	 * 转换基本图方案ID为客运系统方案ID
	 * @param train
	 */
	private void transferSchemaId2VersionId(TCmOriginalTrain train){
		String schemaId = train.getVersionId();
		if(schemaId == null || "".equals(schemaId)){
			return;
		}
		try {
			TCmVersion versionInfo = versionMapper.selectByMTemplateScheme(schemaId);
			if(versionInfo == null){
				logger.error("transferSchemaId2VersionId 未根据基本图方案ID查询出versionId!  schemaId :" + schemaId);
				return;
			}
			train.setVersionId(versionInfo.getCmVersionId());
		} catch (Exception e) {
			logger.error("transferSchemaId2VersionId 根据基本图方案ID查询versionID失败！",e);
			return;
		}
	}
	
	@Override
	public Result<TOriginalCrossTrain> batchConfigOperationRule(
			Map<String, Object> paramMap) {
		Result<TOriginalCrossTrain> result = new Result<TOriginalCrossTrain>();
		try {
			//查询所有对数交路信息
			List<TCmOriginalCross> crossList = tCmOriginalCrossMapper.queryNoOperationCross(paramMap);
			if(crossList == null || crossList.size() == 0){
				result.setCode(Constant.RETURN_CODE_WARNING);
				result.setMessage("所有交路已设置开行规律！11111");
				return result;
			}
			// 查询所有对数车次信息
			List<TCmOriginalTrain> trainList = trainMapper.queryNoOperationTrain(paramMap);
			Map<Object, Object> groupMap = new HashMap<Object, Object>();
			for(TCmOriginalCross cross : crossList){
				String crossId = cross.getCmOriginalCrossId();
				List<TCmOriginalTrain> tempTrains = new ArrayList<TCmOriginalTrain>();
				for(TCmOriginalTrain train : trainList){
					if(crossId.equals(train.getCmOriginalCrossId())){
						tempTrains.add(train);
					}
				}
				groupMap.put(cross, tempTrains);
			}
			List<TCmOriginalCross> updateCrossList = new ArrayList<TCmOriginalCross>();
			List<TCmOriginalTrain> updateTrainList = new ArrayList<TCmOriginalTrain>();
			for(Map.Entry<Object, Object> entry : groupMap.entrySet()){
				TCmOriginalCross cross = (TCmOriginalCross)entry.getKey();
				List<TCmOriginalTrain> trains = (List<TCmOriginalTrain>)entry.getValue();
				cross.setSpareFlag(Constant.SPARE_FLAG_RUN);
				for(TCmOriginalTrain train : trains){
					train.setCommonlineRule(Constant.COMMONLINE_RULE_EVERYDAY);
					train.setSpareFlag(Constant.SPARE_FLAG_RUN);
				}
				updateCrossOperationRule(cross, trains);
				updateCrossList.add(cross);
				updateTrainList.addAll(trains);
			}
			if(updateCrossList.size() == 0 || updateTrainList.size() == 0){
				result.setCode(Constant.RETURN_CODE_WARNING);
				result.setMessage("所有交路已设置开行规律！2222");
				return result;
			}
			try {
				trainMapper.batchConfigOperationRule(updateTrainList);
			} catch (Exception e) {
				logger.error("batchConfigOperationRule 数据库批量修改对数表车次开行规律失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("保存失败!");
				return result;
			}
			try {
				tCmOriginalCrossMapper.batchConfigOperationRule(updateCrossList);
			} catch (Exception e) {
				logger.error("batchConfigOperationRule 数据库批量修改对数表交路开行规律失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("保存失败!");
				return result;
			}
		} catch (Exception e) {
			logger.error("batchConfigOperationRule 批量设置开行规律失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("批量设置开行规律失败!");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TOriginalCrossTrain> batchSaveCross(Map<String, Object> map) {
		Result<TOriginalCrossTrain> result = new Result<TOriginalCrossTrain>();
		JSONArray crossJsonArray = JSONArray.fromObject(map.get("crosses"));
		JSONArray trainJsonArray = JSONArray.fromObject(map.get("trains"));
		List<TCmOriginalTrain> trainList = (List<TCmOriginalTrain>)JSONArray.toCollection(trainJsonArray, TCmOriginalTrain.class);
		List<TCmOriginalCross> crossList = (List<TCmOriginalCross>)JSONArray.toCollection(crossJsonArray, TCmOriginalCross.class);
		try {
			Map<Object, Object> groupMap = new HashMap<Object, Object>();
			for(TCmOriginalCross cross : crossList){
				String crossId = cross.getCmOriginalCrossId();
				List<TCmOriginalTrain> tempTrains = new ArrayList<TCmOriginalTrain>();
				for(TCmOriginalTrain train : trainList){
					if(crossId.equals(train.getCmOriginalCrossId())){
						tempTrains.add(train);
					}
				}
				groupMap.put(cross, tempTrains);
			}
			List<TCmOriginalCross> updateCrossList = new ArrayList<TCmOriginalCross>();
			List<TCmOriginalTrain> updateTrainList = new ArrayList<TCmOriginalTrain>();
			for(Map.Entry<Object, Object> entry : groupMap.entrySet()){
				TCmOriginalCross cross = (TCmOriginalCross)entry.getKey();
				List<TCmOriginalTrain> trains = (List<TCmOriginalTrain>)entry.getValue();
				for(TCmOriginalTrain train : trains){
					train.setAutoFlag(Constant.AUTO_FLAG_MANUAL);
				}
				updateCrossOperationRule(cross, trains);
				updateCrossList.add(cross);
				updateTrainList.addAll(trains);
			}
			if(updateCrossList.size() == 0 || updateTrainList.size() == 0){
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("保存失败!");
				return result;
			}
			try {
				trainMapper.batchConfigAlternate(updateTrainList);
			} catch (Exception e) {
				logger.error("batchConfigAlternateInfo 数据库批量修改对数表车次交替信息失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("保存失败!");
				return result;
			}
			try {
				tCmOriginalCrossMapper.batchConfigAlternate(updateCrossList);
			} catch (Exception e) {
				logger.error("batchConfigAlternateInfo 数据库批量修改对数表交路交替信息失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("保存失败!");
				return result;
			}
		} catch (Exception e) {
			logger.error("batchConfigAlternateInfo 数据库批量修改对数表交路交替信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("保存失败!");
			return result;
		}
		return result;
	}
	
	@Override
	public Result<TOriginalCrossTrain> batchConfigAlternateInfo(
			Map<String, Object> paramMap) {
		Result<TOriginalCrossTrain> result = new Result<TOriginalCrossTrain>();
		String alternateDate = String.valueOf(paramMap.get("date"));
		try {
			List<TCmOriginalCross> crossList = tCmOriginalCrossMapper.queryNoAlternateCross(paramMap);
			if(crossList == null || crossList.size() == 0){
				result.setCode(Constant.RETURN_CODE_WARNING);
				result.setMessage("所有交路已设置交替时间！");
				return result;
			}
			List<TCmOriginalTrain> trainList = trainMapper.queryNoAlternateTrain(paramMap);
			Map<Object, Object> groupMap = new HashMap<Object, Object>();
			for(TCmOriginalCross cross : crossList){
				String crossId = cross.getCmOriginalCrossId();
				List<TCmOriginalTrain> tempTrains = new ArrayList<TCmOriginalTrain>();
				for(TCmOriginalTrain train : trainList){
					if(crossId.equals(train.getCmOriginalCrossId())){
						tempTrains.add(train);
					}
				}
				groupMap.put(cross, tempTrains);
			}
			List<TCmOriginalCross> updateCrossList = new ArrayList<TCmOriginalCross>();
			List<TCmOriginalTrain> updateTrainList = new ArrayList<TCmOriginalTrain>();
			for(Map.Entry<Object, Object> entry : groupMap.entrySet()){
				TCmOriginalCross cross = (TCmOriginalCross)entry.getKey();
				List<TCmOriginalTrain> trains = (List<TCmOriginalTrain>)entry.getValue();
				for(TCmOriginalTrain train : trains){
					train.setAlternateDate(alternateDate);
					train.setAutoFlag(Constant.AUTO_FLAG_BATCH);
				}
				updateCrossOperationRule(cross, trains);
				updateCrossList.add(cross);
				updateTrainList.addAll(trains);
			}
			if(updateCrossList.size() == 0 || updateTrainList.size() == 0){
				result.setCode(Constant.RETURN_CODE_WARNING);
				result.setMessage("所有交路已设置交替信息！");
				return result;
			}
			try {
				trainMapper.batchConfigAlternate(updateTrainList);
			} catch (Exception e) {
				logger.error("batchConfigAlternateInfo 数据库批量修改对数表车次交替信息失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("保存失败!");
				return result;
			}
			try {
				tCmOriginalCrossMapper.batchConfigAlternate(updateCrossList);
			} catch (Exception e) {
				logger.error("batchConfigAlternateInfo 数据库批量修改对数表交路交替信息失败！",e);
				result.setCode(Constant.RETURN_CODE_ERROR);
				result.setMessage("保存失败!");
				return result;
			}
		} catch (Exception e) {
			logger.error("batchConfigAlternateInfo 批量设置交替信息失败！",e);
			result.setCode(Constant.RETURN_CODE_ERROR);
			result.setMessage("批量设置交替信息失败!");
			return result;
		}
		return result;
	}
	
}
