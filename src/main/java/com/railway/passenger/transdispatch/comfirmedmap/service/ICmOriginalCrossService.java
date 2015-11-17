package com.railway.passenger.transdispatch.comfirmedmap.service;

import java.util.Map;

import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TOriginalCrossTrain;

/**
 * 
 * ClassName: ICmOriginalCrossService
 * @Description: 对数表相关
 * @author xjx
 * @date 2015年9月20日
 */
public interface ICmOriginalCrossService {
	
	/**
	 * 
	 * @Description: TODO
	 * @param @param 存一条对数表数据
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
	int insertTCmOriginalCross(TCmOriginalCross ocross, String schemeId);
	
	/**
	 * 
	 * @Description: 根据主键取一条对数表数据
	 * @param @param ocross
	 * @param @return   
	 * @return TCmOriginalCross  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
	TCmOriginalCross getTCmOriginalCross(String cmOriginalCrossId);
	
	/**
	 * 根据主键查询对数表信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月26日
	 */
	Result<TCmOriginalCross> queryByPrimaryKey(Map<String, Object> map);
	
	/**
	 * 根据主键列表批量查询对数表信息
	 * @Description: TODO
	 * @param @param cmOriginalCrossIds
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月5日
	 */
	Result<TCmOriginalCross> queryByPrimaryKeys(String cmOriginalCrossIds);
	
	/**
	 * 审核原始对数表
	 * @Description: TODO
	 * @param @param cmOriginalCrossIds 对数表ID数组
	 * @param @return   
	 * @return Result  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月22日
	 */
	Result<TCmOriginalCross> check(Map<String, Object> map);
	
	/**
	 * 审核原始对数表不通过
	 * @Description: TODO
	 * @param @param cmOriginalCrossIds 对数表ID数组
	 * @param @return   
	 * @return Result  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月22日
	 */
	Result<TCmOriginalCross> checkNotPass(Map<String, Object> map);
	
	/**
	 * 分页查询对数表和车次信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月22日
	 */
	Result<TOriginalCrossTrain> pageQueryCrossAndTrain(Map<String, Object> map);
	
	/**
	 * 分页查询对数表信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月5日
	 */
	Result<TCmOriginalCross> pageQueryCross(Map<String, Object> map);
	
	/**
	 * 查询版本号信息
	 * @Description: TODO
	 * @param @return   
	 * @return Result<TCmVersion>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年9月26日
	 */
	Result<TCmVersion> queryVersionInfo();
	
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
	Result<TCmOriginalCross> upload(Map<String, Object> map);
	
	/**
	 * 新增对数表信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月4日
	 */
	Result<TCmOriginalCross> add(Map<String, Object> map);
	
	/**
	 * 修改对数表信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月5日
	 */
	Result<TCmOriginalCross> updateCross(Map<String, Object> map);
	
	/**
	 * 修改对数表跟车次信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月4日
	 */
	Result<TCmOriginalCross> updateCrossTrain(Map<String, Object> map);
	
	/**
	 * 修改车次详细信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月4日
	 */
	Result<TCmOriginalCross> updateTrainInfo(Map<String, Object> map);
	
	/**
	 * 删除对数表跟车次信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月5日
	 */
	Result<TCmOriginalCross> deleteCrossTrain(Map<String, Object> map);
	
	/**
	 * 修改原始对数表生成交路标志
	 * @Description: TODO
	 * @param @param list
	 * @param @return   
	 * @return Result<TCmOriginalCross>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月5日
	 */
	Result<TCmOriginalCross> markCrossCreateFlag(Map<String, Object> map);
	
	/**
	 * 根据主键查询单个对数表信息和单个车次信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TOriginalCrossTrain>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月6日
	 */
	Result<TOriginalCrossTrain> queryCrossWithTrain(Map<String, Object> map);
	
	/**
	 * 删除车次信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TOriginalCrossTrain>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月7日
	 */
	Result<TOriginalCrossTrain> deleteTrainInfo(Map<String, Object> map);
	
	/**
	 * 批量设置开行计划
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TOriginalCrossTrain>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月25日
	 */
	Result<TOriginalCrossTrain> batchConfigOperationRule(Map<String, Object> map);
	
	/**
	 * 批量保存交路
	 * @Description: TODO
	 * @param @param map
	 * @param @return   
	 * @return Result<TOriginalCrossTrain>  
	 * @throws
	 * @author yuyangxing
	 * @date 2015年10月25日
	 */
	Result<TOriginalCrossTrain> batchSaveCross(Map<String, Object> map);
	
	/**
	 * 批量设置交替信息
	 * @param map
	 * @return
	 */
	Result<TOriginalCrossTrain> batchConfigAlternateInfo(Map<String, Object> map);
	
	/**
	 * @param map
	 * @return
	 * 用于临时审核
	 */
	public Result<TCmOriginalCross> check_prim(Map<String, Object> map);
}
