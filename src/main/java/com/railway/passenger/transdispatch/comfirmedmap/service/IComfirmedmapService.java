package com.railway.passenger.transdispatch.comfirmedmap.service;

import java.util.List;

import org.railway.com.trainplan.entity.BaseCrossTrainInfo;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;

/**
 * 
 * ClassName: IComfirmedmapService
 * @Description: 图定相关
 * @author xjx
 * @date 2015年9月20日
 */
public interface IComfirmedmapService {
	/**
	 * 根据原始对数表信息生成逻辑交路及其所有相关信息
	 * @param cross
	 * @return
	 */
	public TCmCross generateTcmCrossInfo(TCmOriginalCross cross);
	
	/**
	 * 根据逻辑交路信息生成物理逻辑交路及其所有相关信息
	 * @param cross
	 * @return
	 */
	public boolean generateTcmPhyCrossInfo(TCmCross cross);
	public List<BaseCrossTrainInfo> getBaseCrossTrainInfo(String OriginalCrossid);
	
	public boolean deleteAllInfoByTCmOriginalCross(TCmOriginalCross ocross);
	
}
