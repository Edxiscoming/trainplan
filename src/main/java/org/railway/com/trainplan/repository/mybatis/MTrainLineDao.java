package org.railway.com.trainplan.repository.mybatis;

import java.util.Map;

/**
 * 计划dao Created by star on 5/12/14.
 */
 
public interface MTrainLineDao {

	/**
	 * 查询运行线数量
	 * 
	 * @param
	 * @return
	 */
	public int selectMTrainLineCount(String MTrainLineId);

	/**
	 * 查询运行线子项数量
	 * 
	 * @param
	 * @return
	 */
	public int selectMTrainLineItemCount(String MTrainLineId);

	/**
	 * 删除运行线
	 * 
	 * @param
	 * @return
	 */
	public void deleteMTrainLineByMTrainLineId(String MTrainLineId);

	/**
	 * 删除运行线子项
	 * 
	 * @param
	 * @return
	 */
	public void deleteMTrainLineItemByMTrainLineId(String MTrainLineId);

	/**
	 * 删除.
	 * 
	 * @param map
	 */
	public void deleteMTrainLineByCmdId(Map<String, Object> map);

}
