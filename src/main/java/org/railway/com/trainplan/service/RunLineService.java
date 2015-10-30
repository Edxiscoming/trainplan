package org.railway.com.trainplan.service;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.RunLineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 运行线服务类
 * Created by star on 5/13/14.
 */
@Component
@Monitored
@Transactional
public class RunLineService {

	
    private final static Log logger = LogFactory.getLog(RunLineService.class);

    @Autowired
    private RunLineDao runLineDao;

    /**
     * 根据运行线id查询运行线时刻表
     * @param lineId 运行线id
     * @return 时刻表
     */
    public List<Map<String, Object>> findLineTimeTableByLineId(String lineId) {
        logger.debug("findLineTimeTableByLineId::::");
        return runLineDao.findLineTimeTableByLineId(lineId);
    }

    /**
     * 根据运行线id查询运行线详情
     * @param lineId 运行线id
     * @return 运行线详情
     */
    public Map<String, Object> findLineInfoByLineId(String lineId) {
        logger.debug("findLineInfoByLineId::::");
        return runLineDao.findLineInfoByLineId(lineId);
    }

    /**
     * 查询冗余运行线数量
     * @param bureau 局简称
     * @param date 日期
     * @return 查询结果
     * @throws ParseException
     */
    public Map<String, Object> findUnknownRunLineCount(String bureau, String date) throws ParseException {
        logger.debug("findUnknownRunLineCount::::::");
        Map<String, Object> params = Maps.newHashMap();
        params.put("bureau", bureau);
        try {
            params.put("startDate", new Timestamp(DateUtils.parseDate(date + " 00:00:00", new String[] {"yyyy-MM-dd HH:mm:ss"}).getTime()));
            params.put("endDate", new Timestamp(DateUtils.parseDate(date + " 23:59:59", new String[] {"yyyy-MM-dd HH:mm:ss"}).getTime()));
        } catch (ParseException e) {
            logger.error("findUnknownRunLineCount::::", e);
            throw e;
        }
        return runLineDao.findUnknownRunLineCount(params);
    }
}
