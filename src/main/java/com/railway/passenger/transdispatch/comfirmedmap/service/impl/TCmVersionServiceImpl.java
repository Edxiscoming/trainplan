package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.util.UUID;

import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmVersion;
import com.railway.passenger.transdispatch.comfirmedmap.repository.TCmVersionMapper;
import com.railway.passenger.transdispatch.comfirmedmap.service.ITCmVersionService;

@Service
public class TCmVersionServiceImpl implements ITCmVersionService {
	@Autowired
	SchemeService schemeService;
	
	@Autowired
	private TCmVersionMapper tCmVersionMapper;

	@Override
	public void synSchemeData() {
		for(SchemeInfo scheme : schemeService.getSchemes()){
			TCmVersion version = tCmVersionMapper.selectByMTemplateScheme(scheme.getSchemeId());
			if(version != null){
				version.setmTemplateScheme(scheme.getSchemeId());
				version.setName(scheme.getSchemeName());
				version.setExecutionTime(scheme.getExecutionTime());
				version.setPublishTime(scheme.getPublishTime());
				tCmVersionMapper.updateByPrimaryKeySelective(version);
			} else {
				version = new TCmVersion();
				version.setCmVersionId(UUID.randomUUID().toString());
				version.setmTemplateScheme(scheme.getSchemeId());
				version.setName(scheme.getSchemeName());
				version.setExecutionTime(scheme.getExecutionTime());
				version.setPublishTime(scheme.getPublishTime());
				version.setUseStatus((short)1);
				tCmVersionMapper.insertSelective(version);
			}
		}
	}

}
