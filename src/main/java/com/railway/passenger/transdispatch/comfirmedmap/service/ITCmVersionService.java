package com.railway.passenger.transdispatch.comfirmedmap.service;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;

public interface ITCmVersionService{
	/**
	 * 
	 * @Description: 同步基本图中的方案信息到本地库
	 * @param    
	 * @return void  
	 * @throws
	 * @author qs
	 * @date 2015年10月6日
	 */
	void synSchemeData();
	
	/**
	 * 
	 * @Description: 获取下一个executionTime的版本信息
	 * @param @param id 版本id或者方案id
	 * @param @return   
	 * @return TCmVersion  
	 * @throws
	 * @author qs
	 * @date 2015年11月6日
	 */
	TCmVersion getNextVersion(String id);
	
	/**
	 * 
	 * @Description: 获取上一个executionTime的版本信息
	 * @param @param id 版本id或者方案id
	 * @param @return   
	 * @return TCmVersion  
	 * @throws
	 * @author qs
	 * @date 2015年11月6日
	 */
	TCmVersion getPreVersion(String id);
}
