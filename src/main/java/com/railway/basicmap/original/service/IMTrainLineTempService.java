package com.railway.basicmap.original.service;

import java.util.List;
import java.util.Map;

import com.railway.basicmap.original.entity.MTrainlineTemp;
import com.railway.common.entity.Result;

public interface IMTrainLineTempService {

	/**
	 * @param reqMap
	 * @return
	 * 基本图列表查询
	 */
	public Result<MTrainlineTemp> GetTrainLineInfo(Map<String, Object> map);
	
	/**
	 * 查询路局名称局码数据字典
	 * @Description: TODO
	 * @param @return   
	 * @return List<BureauDic>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	public Map<String,String> getBureauNameDic();
	
	/**
	 * 查询路局简称局码数据字典
	 * @Description: TODO
	 * @param @return   
	 * @return List<BureauDic>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	public Map<String,String> getBureauShortNameDic();
	
}
