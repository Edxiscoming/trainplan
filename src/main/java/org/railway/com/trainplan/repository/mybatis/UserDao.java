package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.User;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/15/14.
 */
 
public interface UserDao {
	User getUserByUsernameAndAccId(Map<String, Object> params);

	List<Map<String, Object>> getAccountbyUsername(String username);

	Integer uptdateUser(Map<String, Object> params);

	/**
	 * 查询动车所 or 动车台.
	 * 
	 * @param params
	 *            rol_name(铁路局动车调,站段动车所);ljpym(P,V)
	 * @return
	 */
	List<Map<String, Object>> getDctOrDcsByMap(Map<String, Object> params);
}
