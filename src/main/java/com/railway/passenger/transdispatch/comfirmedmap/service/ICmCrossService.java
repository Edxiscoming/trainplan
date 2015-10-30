package com.railway.passenger.transdispatch.comfirmedmap.service;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.TrainTimeInfoJbt;

import com.railway.passenger.transdispatch.comfirmedmap.entity.Ljzd;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCrossAndOriginaCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrain;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainAlternate;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainRule;
import com.railway.common.entity.Result;

/**
 * 
 * ClassName: ICmCrossService
 * @Description: 与逻辑交路相关
 * @author xjx
 * @date 2015年9月20日
 */
public interface ICmCrossService {
	/**
	 * 
	 * @Description: 存一条逻辑交路信息
	 * @param @param cross
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
	int insertTCmCross(TCmCross cross);
	/**
	 * 
	 * @Description: TODO
	 * @param @param 根据主键ID获取一条逻辑交路信息
	 * @param @return   
	 * @return TCmCross  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
	TCmCross getTCmCross(String cmCrossId);
	/**
	 * 
	 * @Description: TODO
	 * @param @param 根据逻辑交路ID获取其对应的车次列表
	 * @param @return   
	 * @return List<TCmTrain>  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
	Result<TCmTrain> getTCmTrain(Map<String, Object> reqMap);
	/**
	 * 
	 * @Description: TODO
	 * @param @param 根据车次ID获取其交替信息
	 * @param @return   
	 * @return TCmTrainAlternate  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
	TCmTrainAlternate getTCmTrainAlternate(String trainId);
	/**
	 * 
	 * @Description: TODO
	 * @param @param cross
	 * @param @return   
	 * @return 根据车次ID获取其开行规律  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月20日
	 */
	TCmTrainRule getTCmTrainRule(String trainId);
	
	/**
	 * @Description: TODO
	 * @param reqMap
	 * @return List<TCmCross>
	 * @throws
	 * @author Administrator
	 * @date 2015年9月22日
	 */
	public Result<TCmCrossAndOriginaCross> getCmUnitCrossInfo(Map<String, Object> reqMap);
	
	/**
	 * @param reqMap
	 * @return
	 * long
	 * Administrator
	 * 2015年9月23日
	 */
	public int getCmUnitCrossInfoCount(Map<String, Object> reqMap);
	
	/**
	 * @param trainId
	 * @return
	 * 对数表查询
	 */
	public TCmOriginalCross getOriginalCrossInfoByCmCrossId(String trainId);
	
	/**
	 * @param trainId
	 * @return
	 * 物理车次查询
	 */
	public List<TCmCross> getCrossInfoForCmCrossid_Phy(String trainId);
	
	/**
	 * @param unitCrossIds
	 * @return
	 * 删除交路
	 */
	public int deleteCmUnitCrossInfoForCorssIds(List<String> unitCrossIds);
	
	/**
	 * @param baseTrainId
	 * @return
	 * 时刻表查询
	 */
	public List<TrainTimeInfoJbt> getTrainTimes(String baseTrainId);
	/**
	 * @param baseTrainId
	 * @return 无分页
	 * 时刻表查询
	 */
	public List<TrainTimeInfoJbt> getTrainTimesNp(String baseTrainId);
	
	/**
	 * @return
	 * 查询18个路局
	 */
	public List<Ljzd> getFullStationInfo();
	
	public List queryHomePage();
}
