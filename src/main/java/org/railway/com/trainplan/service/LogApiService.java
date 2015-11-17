package org.railway.com.trainplan.service;

import java.util.Map;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.entity.LogApi;
import org.railway.com.trainplan.repository.mybatis.LogApiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对应log_api.
 * 
 * @author zhangPengDong
 *
 *         2015年7月7日 下午3:56:04
 */
@Component
@Transactional
@Monitored
public class LogApiService {

	@Autowired
	private LogApiDao logApiDao;

	public Integer insertLogApi(LogApi la) {
		return logApiDao.insertLogApi(la);
	}

	public Integer updateLogApiById(Map<String, Object> map) {
		return logApiDao.updateLogApiById(map);
	}
}
