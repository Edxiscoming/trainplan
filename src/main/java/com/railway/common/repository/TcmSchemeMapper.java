package com.railway.common.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.SchemeInfo;


public interface TcmSchemeMapper {
	/**
	 * 根据交路id查询方案
	 * @Description: TODO
	 * @param @return   
	 * @return List<SchemeInfo> 
	 * @throws
	 * @author suntao
	 * @date 2015年9月23日
	 */
	public List<SchemeInfo> getSchemeInfoByCrossId(Map<String, String> paramMap);
}
