package org.railway.com.trainplan.service;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.BaseDataDao;
import org.railway.com.trainplan.repository.mybatis.BureauDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/13/14.
 */
@Component
@Monitored
@Transactional
public class BaseDataService {

    @Autowired
    private BaseDataDao baseDataDao;

    @Autowired
    private BureauDao bureauDao;

    public List<Map<String, Object>> getFJCDic(String bureauCode) {
        baseDataDao.getFJKDicByBureauCode(bureauCode);
        return null;
    }

    public List<Map<String, Object>> getBureauList() {
        return bureauDao.getBureauList();
    }
}
