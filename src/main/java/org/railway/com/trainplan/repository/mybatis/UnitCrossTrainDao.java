package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.UnitCrossTrain;

/**
 * unit_cross_train.
 * 
 * @author zhangPengDong
 *
 *         2015年5月18日 下午2:55:16
 */
 
public interface UnitCrossTrainDao {

	/**
	 * sel - unit_cross_train.
	 * 
	 * @param map
	 */
	public List<UnitCrossTrain> getUnitCrossTrainByMap(Map<String, Object> map);
}
