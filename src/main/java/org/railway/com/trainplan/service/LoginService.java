package org.railway.com.trainplan.service;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/15/14.
 */
@Component
@Monitored
@Transactional
public class LoginService {

    @Autowired
    private UserDao userDao;

    public List<Map<String, Object>> getAccountByLoginName(String username) {
        return userDao.getAccountbyUsername(username);
    }
    
    
    public Integer updateUser(Map<String,Object>parame) {
		return userDao.uptdateUser(parame);
	}
}
