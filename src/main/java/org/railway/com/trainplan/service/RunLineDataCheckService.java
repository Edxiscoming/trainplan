package org.railway.com.trainplan.service;

import org.railway.com.trainplan.repository.mybatis.MTrainLineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RunLineDataCheckService {
	@Autowired
	private MTrainLineDao mTrainLineDao;
	
	public boolean mTrainLineisExist(String mTrainLineId) {
		int count = mTrainLineDao.selectMTrainLineCount(mTrainLineId);
		if(count > 0) {
			return true;
		}
		else return false;
		
		
	}
	
	public boolean mTrainLineItemisExist(String mTrainLineId) {
		int count = mTrainLineDao.selectMTrainLineItemCount(mTrainLineId);
		if(count > 0) {
			return true;
		}
		else return false;
		
	}
}
