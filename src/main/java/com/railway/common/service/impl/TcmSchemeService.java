package com.railway.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.common.repository.TcmSchemeMapper;

@Service
public class TcmSchemeService {
	private static final Logger logger = Logger.getLogger(TcmSchemeService.class);
	@Autowired
	private TcmSchemeMapper schemeMapper;
	
	public List<SchemeInfo> getschemeByCrossId(String crossId){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("crossId", crossId);
		return schemeMapper.getSchemeInfoByCrossId(paramMap);
	}
}
