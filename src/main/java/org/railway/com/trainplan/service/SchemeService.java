package org.railway.com.trainplan.service;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.entity.SchemeInfo;
import org.railway.com.trainplan.repository.mybatis.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
 
@Component
@Transactional
@Monitored
public class SchemeService {
	
	@Autowired
	private BaseDao baseDao;
	  
    public java.util.List<SchemeInfo> getSchemes(){
	   return baseDao.selectListBySql(Constants.SCHEME_GETSCHEMEINFO, null);
    }
    
    public SchemeInfo getScheme(String schemeId){
    	for(SchemeInfo scheme : getSchemes()){
    		if(schemeId.equals(scheme.getSchemeId())){
    			return scheme;
    		}
    	}
    	return null;
    }
    
}
