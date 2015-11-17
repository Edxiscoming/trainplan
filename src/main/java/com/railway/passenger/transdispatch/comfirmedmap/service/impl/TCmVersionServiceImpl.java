package com.railway.passenger.transdispatch.comfirmedmap.service.impl;

import java.util.List;
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
	
	public TCmVersion getNextVersion(String id){
		List<TCmVersion> versions = tCmVersionMapper.queryList();
		int tempSort = 0;
		for(int i = 0;i<versions.size();i++){
			TCmVersion temp = versions.get(i);
			if(temp.getCmVersionId().equals(id) || temp.getmTemplateScheme().equals(id)){
				tempSort = i;
				break;
			}
		}
		if(tempSort != 0){
			return versions.get(tempSort-1);
		}
		return null;
	}
	
	public TCmVersion getPreVersion(String id){
		List<TCmVersion> versions = tCmVersionMapper.queryList();
		int tempSort = 0;
		for(int i = 0;i<versions.size();i++){
			TCmVersion temp = versions.get(i);
			if(temp.getCmVersionId().equals(id) || temp.getmTemplateScheme().equals(id)){
				tempSort = i;
				break;
			}
		}
		if(tempSort != (versions.size()-1)){
			return versions.get(tempSort+1);
		}
		return null;
	}

	public int deleteByPrimaryKey(String cmVersionId) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.deleteByPrimaryKey(cmVersionId);
	}

	public int insert(TCmVersion record) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.insert(record);
	}

	public int insertSelective(TCmVersion record) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.insertSelective(record);
	}

	public TCmVersion selectByPrimaryKey(String cmVersionId) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.selectByPrimaryKey(cmVersionId);
	}

	public TCmVersion selectVagueByPrimaryKey(String cmVersionId) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.selectVagueByPrimaryKey(cmVersionId);
	}

	public TCmVersion selectByMTemplateScheme(String mTemplateScheme) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.selectByMTemplateScheme(mTemplateScheme);
	}

	public int updateByPrimaryKeySelective(TCmVersion record) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(TCmVersion record) {
		// TODO Auto-generated method stub
		return tCmVersionMapper.updateByPrimaryKey(record);
	}

	public List<TCmVersion> queryList() {
		// TODO Auto-generated method stub
		return tCmVersionMapper.queryList();
	}

}
