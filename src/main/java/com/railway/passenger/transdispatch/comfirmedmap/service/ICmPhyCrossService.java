package com.railway.passenger.transdispatch.comfirmedmap.service;

import java.util.List;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCrossTrain;

/**
 * 
 * ClassName: ICmPhyCrossService
 * @Description: 车底交路相关
 * @author xjx
 * @date 2015年9月20日
 */
public interface ICmPhyCrossService {
	/**
	 * 
	 * @Description: 根据逻辑交路ID获取物理交路列表
	 * @param @param cmCrossId
	 * @param @return   
	 * @return List<TCmPhyCross>  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	List<TCmPhyCross> getTcmPhyCross(String crossId);
	/**
	 * 
	 * @Description: 根据物理交路ID获取对应的车次列表
	 * @param @param cmCrossId
	 * @param @return   
	 * @return List<TCmPhyCross>  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	List<TCmPhyCrossTrain> getTcmPhyCrossTrain(String cmPhyCrossId);
	/**
	 * 
	 * @Description: 插入一条车底交路记录
	 * @param @param phyCross
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	@Deprecated
	int insertTCmPhyCross(TCmPhyCross phyCross);
	/**
	 * 
	 * @Description: 插入一条物理交路车次记录
	 * @param @param phyTrain
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	@Deprecated
	int insertTCmPhyCrossTrain(TCmPhyCrossTrain phyTrain);
	/**
	 * 
	 * @Description: 根据车底交路ID删除车底交路
	 * @param @param cmPhyCrossId
	 * @param @param flag (true-物理删除 false-逻辑删除)
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	int delTCmPhyCross(String cmPhyCrossId, boolean flag);
	/**
	 * 
	 * @Description: 根据逻辑交路ID删除对应的车底交路
	 * @param @param cmCrossId
	 * @param @param flag (true-物理删除 false-逻辑删除)
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月22日
	 */
	int delTCmPhyCrossByCmCross(String cmCrossId,boolean flag);
	/**
	 * 
	 * @Description: 根据车底交路ID删除其对应的车次信息
	 * @param @param cmPhyCrossId
	 * @param @param flag (true-物理删除 false-逻辑删除)
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Administrator
	 * @date 2015年9月21日
	 */
	int delTCmPhyCrossTrain(String cmPhyCrossId, boolean flag);
}
