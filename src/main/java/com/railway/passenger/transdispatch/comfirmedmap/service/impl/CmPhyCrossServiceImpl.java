package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmPhyCrossTrain;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmPhyCrossMapper;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmPhyCrossTrainMapper;
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmPhyCrossService;

@Service
public class CmPhyCrossServiceImpl implements ICmPhyCrossService {
	@Autowired
	TCmPhyCrossMapper tCmPhyCrossMapper;
	
	@Autowired
	TCmPhyCrossTrainMapper tCmPhyCrossTrainMapper;

	@Override
	public List<TCmPhyCross> getTcmPhyCross(String cmCrossId) {
		return tCmPhyCrossMapper.selectByCmCrossId(cmCrossId);
	}

	@Override
	public List<TCmPhyCrossTrain> getTcmPhyCrossTrain(String cmPhyCrossId) {
		return tCmPhyCrossTrainMapper.selectByCmPhyCrossId(cmPhyCrossId);
	}

	@Override
	public int insertTCmPhyCross(TCmPhyCross phyCross) {
		return tCmPhyCrossMapper.insertSelective(phyCross);
	}

	@Override
	public int insertTCmPhyCrossTrain(TCmPhyCrossTrain phyTrain) {
		return tCmPhyCrossTrainMapper.insertSelective(phyTrain);
	}

	@Override
	public int delTCmPhyCross(String cmPhyCrossId, boolean flag) {
		if(flag){
			return tCmPhyCrossMapper.deleteByPrimaryKey(cmPhyCrossId);
		} else {
			return tCmPhyCrossMapper.deleteByPrimaryKeyLogic(cmPhyCrossId);
		}
	}

	@Override
	public int delTCmPhyCrossTrain(String cmPhyCrossId, boolean flag) {
		if(flag){
			return tCmPhyCrossTrainMapper.deleteByCmPhyCross(cmPhyCrossId);
		} else {
			return tCmPhyCrossTrainMapper.deleteByCmPhyCrossLogic(cmPhyCrossId);
		}
	}

	@Override
	public int delTCmPhyCrossByCmCross(String cmCrossId, boolean flag) {
		if(flag){
			return tCmPhyCrossMapper.deleteByCmCross(cmCrossId);
		} else {
			return tCmPhyCrossMapper.deleteByCmCrossLogic(cmCrossId);
		}
	}
}
