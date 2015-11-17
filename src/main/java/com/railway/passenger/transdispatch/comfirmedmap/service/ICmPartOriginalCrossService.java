package com.railway.passenger.transdispatch.comfirmedmap.service;

import java.util.Map;

import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPartOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;

public interface ICmPartOriginalCrossService {
	/**
	 * 上传对数表
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmPartOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	Result<TCmPartOriginalCross> upload(Map<String, Object> map);
	
	/**
	 * 审核对数表
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmPartOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	Result<TCmPartOriginalCross> check(Map<String, Object> map);
	
	/**
	 * 分页查询
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmPartOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	Result<TCmPartOriginalCross> pageQuery(Map<String, Object> map);
	
	/**
	 * 根据主键查询对数表
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmPartOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	Result<TCmPartOriginalCross> queryByPrimaryKey(Map<String, Object> map);
	
	/**
	 * 修改对数表
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmPartOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	Result<TCmPartOriginalCross> update(Map<String, Object> map);
	
	/**
	 * 删除对数表
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmPartOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	Result<TCmPartOriginalCross> delete(Map<String, Object> map);
	
	/**
	 * 新增对数表
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmPartOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月23日
	 */
	Result<TCmPartOriginalCross> add(Map<String, Object> map);
	
	/**
	 * 查询版本号信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmVersion>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月25日
	 */
	Result<TCmVersion> queryVersionInfo();
}
