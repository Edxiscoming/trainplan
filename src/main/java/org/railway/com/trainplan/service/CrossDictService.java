package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.CrossDictInfo;
import org.railway.com.trainplan.entity.CrossDictStnInfo;
import org.railway.com.trainplan.entity.QueryResult;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CrossDictService {
	private static final Logger logger = Logger.getLogger(CrossDictService.class);

	@Autowired
	private BaseDao baseDao;
	
	/**
	 * 查询cross信息
	 * @param reqMap
	 * @return
	 * @throws Exception 
	 */
	public QueryResult  getCrossDictInfo(Map<String,Object> reqMap) throws Exception{
		return baseDao.selectListForPagingBySql(Constants.SQL_ID_CROSSDICT_INFO, reqMap);
	}
	
	
	/**
	 * 对表DRAW_GRAPH添加一条数据
	 * @param crossDictInfo
	 * @return
	 */
	public int  addCrossDictInfo(CrossDictInfo crossDictInfo){
		return baseDao.insertBySql(Constants.SQL_ADD_CROSS_DIC_INFO, crossDictInfo);
	}
	
	/**
	 * 批量对表DRAW_GRAPH_STN添加数据
	 * @param list
	 * @return
	 */
	public int batchAddCrossDictStnInfo(List<CrossDictStnInfo> list){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("trainCrossList",list );
		return baseDao.insertBySql(Constants.SQL_BATCH_ADD_CROSS_DIC_STN_INFO, reqMap);
	}
	
	
	/**
	 * 通过baseCrossId查询表DRAW_GRAPH的对象
	 * @param baseCrossId
	 * @return
	 */
	public CrossDictInfo  getDrawGraphForBaseCrossId(String baseCrossId){
		return (CrossDictInfo) baseDao.selectOneBySql(Constants.SQL_GET_DRAW_GRAPH_FOR_BASECROSSID, baseCrossId);
	}
	
	/**
	 * 通过baseCrossId查询CrossDictStnInfo对象
	 * @param crossName 交路名
	 * @param chartId 方案id
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDictStnForChartId(String crossName,String chartId){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crossName", crossName);
		reqMap.put("chartId", chartId);
		return baseDao.selectListBySql(Constants.SQL_GET_CROSS_DIC_STN_FOR_BASECROSSID, reqMap);
	}
	
	
	/**
	 * 通过baseCrossId查询CrossDictStnInfo对象
	 * @param crossName 交路名
	 * @param chartId 方案id
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDictStnByChartId(String crossName,String chartId){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crossName", crossName);
		reqMap.put("chartId", chartId);
		return baseDao.selectListBySql(Constants.SQL_GET_CROSS_DIC_STN_BY_BASECROSSID, reqMap);
	}
	
	/**
	 * 通过baseCrossId查询CrossDictStnInfo对象
	 * @param crossName 交路名
	 * @param chartId 方案id
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDictStnByChartIdVehicle(String crossName,String chartId){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crossName", crossName);
		reqMap.put("chartId", chartId);
		return baseDao.selectListBySql(Constants.SQL_GET_CROSS_DIC_STN_BY_BASECROSSID_VEHICLE, reqMap);
	}
	
	/**
	 * 通过baseCrossId查询CrossDictStnInfo对象
	 * @param crossName 交路名
	 * @param chartId 方案id
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDictStnByChartIdVehicleBy2(String crossName,String chartId){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crossName", crossName);
		reqMap.put("chartId", chartId);
		return baseDao.selectListBySql(Constants.SQL_GET_CROSS_DIC_STN_BY_BASECROSSID_VEHICLEBY2, reqMap);
	}
	
	
	/**
	 * 通过baseCrossId查询CrossDictStnInfo对象
	 * @param crossName 交路名
	 * @param chartId 方案id
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDictStnByChartIdVehicleSearch(String crossName,String chartId){
		Map<String,Object> reqMap = new HashMap<String,Object>();
		reqMap.put("crossName", crossName);
		reqMap.put("chartId", chartId);
		return baseDao.selectListBySql(Constants.SQL_GET_CROSS_DIC_STN_BY_BASECROSSID_VEHICLESEARCH, reqMap);
	}
	
	
	/**
	 * 通过unitCrossId查询CrossDictStnInfo对象
	 * @param unitCrossId
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDictStnForUnitCorssId(String unitCrossId){
		return baseDao.selectListBySql(Constants.GET_CROSS_DICT_STN_FOR_UNITCROSSID, unitCrossId);
	}

	/**
	 * 通过DrawGraphId查询CrossDictStnInfo对象
	 * @param DrawGraphId
	 * @return
	 */
	public List<CrossDictStnInfo> getCrossDicStnInfoByDrawGraphId(
			String drawGraphId) {
		return baseDao.selectListBySql(Constants.GET_CROSS_DICT_STN_BY_DRAWGRAPHID, drawGraphId);
	}


	public int updateDicStnSortAndDetail(Map<String, Object> map) {
		return baseDao.updateBySql(Constants.UPDATE_DRAWGRAPHSTN_SORT_DETAIL, map);
	}


	public void deleteDrawGraphStnByDrawGraphId(String drawGraphId) {
		//删除stn
		baseDao.updateBySql(Constants.DELETED_RAWGRAPHSTN_BY_DRAWGRAPHID, drawGraphId);
		baseDao.updateBySql(Constants.DELETED_RAWGRAPH_BY_DRAWGRAPHID, drawGraphId);
		
	}
}
