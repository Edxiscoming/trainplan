package org.railway.com.trainplan.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.repository.mybatis.ChartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 各种统计图服务类
 * Created by speeder on 2014/5/27.
 */
@Component
@Transactional
@Monitored
public class ChartService {

    private static final Log logger = LogFactory.getLog(ChartService.class);

    @Autowired
    private ChartDao chartDao;

    /**
     * 开行状态统计图
     * @param map 查询统计图参数
     * @return 统计结果
     */
    public Map<String, Object> getPlanTypeChart(Map<String, Object> map) {
        logger.debug("getPlanTypeChart::::");
        return chartDao.getPlanTypeCount(map);
    }

    /**
     *
     * @param map 查询条件
     * @return 查询结果
     */
    public Map<String, Object> getPlanLineCount(Map<String, Object> map) {
        logger.debug("getPlanLineCount::");
        return chartDao.getPlanLineCount(map);
    }

    /**
     * 一级审核统计图
     * @param map 查询条件
     * @return 查询结果
     */
    public Map<String, Object> getLev1CheckCount(Map<String, Object> map) {
        logger.debug("getPlanTypeChart::::");
        return chartDao.getLev1CheckCount(map);
    }

    /**
     * 二级审核统计图
     * @param map 查询条件
     * @return 查询结果
     */
    public Map<String, Object> getLev2CheckCount(Map<String, Object> map) {
        logger.debug("getPlanLineCount::");
        return chartDao.getLev2CheckCount(map);
    }
}
