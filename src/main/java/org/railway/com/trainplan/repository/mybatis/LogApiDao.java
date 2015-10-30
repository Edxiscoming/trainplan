package org.railway.com.trainplan.repository.mybatis;

import java.util.Map;

import org.railway.com.trainplan.entity.LogApi;

/**
 * 对应log_api.
 * 
 * @author zhangPengDong
 *
 *         2015年7月7日 下午3:56:20
 */
 
public interface LogApiDao {

	Integer insertLogApi(LogApi la);

	Integer updateLogApiById(Map<String, Object> map);

}
