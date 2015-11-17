package org.railway.com.trainplan.service;

import java.util.List;
import java.util.Map;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.UnitCrossTrain;
import org.railway.com.trainplan.repository.mybatis.UnitCrossTrainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * unit_cross_train.
 * 
 * @author zhangPengDong
 *
 * 2015年5月18日 下午2:54:34
 */
@Component
@Transactional
@Monitored
public class UnitCrossTrainService {
	
	@Autowired
	private UnitCrossTrainDao unitCrossTrainDao;

	/**
	 * sel - unit_cross_train.
	 * 
	 * @param map
	 */
	public List<UnitCrossTrain> getUnitCrossTrainByMap(Map<String, Object> map){
		return unitCrossTrainDao.getUnitCrossTrainByMap(map);
	}
	
	
}
