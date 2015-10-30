package org.railway.com.trainplan.repository.mybatis;

import java.util.Map;

/**
 * jhpt_jbt.m_trainline_item.
 * 
 * @author zhangPengDong
 *
 *         2015年3月20日 下午2:32:58
 */
 
public interface MTrainLineItemDao {

	/**
	 * 删除.
	 * 
	 * @param map
	 */
	public void deleteMTrainLineItemByCmdId(Map<String, Object> map);

}
