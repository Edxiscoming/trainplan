package org.railway.com.trainplan.service;

import org.railway.com.trainplan.repository.mybatis.MTrainLineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RunLineDataRollBackService {
	@Autowired
	private MTrainLineDao mTrainLineDao;
	
	public void deleteRunLineById(String mTrainLineId) {
		mTrainLineDao.deleteMTrainLineByMTrainLineId(mTrainLineId);
	}
	
	public void deleteRunLineItemByRunLineId(String mTrainLineId) {
		mTrainLineDao.deleteMTrainLineItemByMTrainLineId(mTrainLineId);
	}
}
