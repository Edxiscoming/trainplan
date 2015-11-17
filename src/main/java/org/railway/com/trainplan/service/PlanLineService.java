package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.utils.CreateRunlineDataUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.runline.CreateRunLineData;
import org.railway.com.trainplan.entity.runline.Job;
import org.railway.com.trainplan.entity.runline.Origin;
import org.railway.com.trainplan.entity.runline.RunLineItem;
import org.railway.com.trainplan.entity.runline.RunlineModel;
import org.railway.com.trainplan.entity.runline.TrainTypeModel;
import org.railway.com.trainplan.repository.mybatis.RunLineDao;
import org.railway.com.trainplan.repository.mybatis.RunPlanDao;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.dto.PlanLineInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by star on 5/21/14.
 */
@Component
@Transactional
@Monitored
public class PlanLineService {

    private static final Log logger = LogFactory.getLog(PlanLineService.class);

    @Autowired
    private RunPlanDao runPlanDao;

    @Autowired
    private RunLineDao runLineDao;
    
    @Autowired
	private RunPlanService runPlanService;

	@Autowired
	private RunPlanLkService runPlanLkService;
	
	@Autowired
	private SendMQMessageService sendMQMessageService;
	
    @Value("#{restConfig['trainline.generatr.thread']}")
    private int threadNbr;

    public int checkTrainInfo(String planId, String lineId) {
        logger.debug(":::::::::::::::::::checkTrainInfo:::::::::::::::::::");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("planId", planId);
        param.put("lineId", lineId);
        try {
            List<Map<String, Object>> result = runPlanDao.checkTrainInfo(param);
            if (result.size() == 0) {
                return -1;
            }
            Map<String, Object> cells = result.get(0);
            if (cells.containsValue(0)) {
                return -1;
            }
        } catch (Exception e) {
            logger.error("planId: " + planId + " - lineId:" + lineId, e);
            return 0;
        }
        return 1;
    }

    public int checkTimeTable(String planId, String lineId) {
        logger.debug(":::::::::::::::::::checkTimeTable::::::::::::::::");
        int status = 1;
        try {
            List<Map<String, Object>> plans = runPlanDao.findPlanTimeTableByPlanId(planId);
            List<Map<String, Object>> lines = runLineDao.findLineTimeTableByLineId(lineId);
            if (plans.size() != lines.size()) {
                return -1;
            }
            if (plans.size() == 0) {
                return -1;
            }
            // 客运计划和日计划存储的始发站-到站时间 和 终到站-出发时间 不一样，且时刻表不关心这2个时间，所以不校验。
            Map<String, Object> plan_start = plans.get(0);
            plan_start.remove("ARR_TIME");
            Map<String, Object> line_start = lines.get(0);
            line_start.remove("ARR_TIME");
            Map<String, Object> plan_end = plans.get(plans.size() - 1);
            plan_end.remove("DPT_TIME");
            Map<String, Object> line_end = lines.get(lines.size() - 1);
            line_end.remove("DPT_TIME");
           
            if(plans!=null && plans.size()>0 && lines!=null && lines.size()>0){
            	label:for(int i = 0;i<lines.size();i++){
            		Map<String, Object> linesMap = plans.get(i);
            		Set<String> keys = linesMap.keySet();
            		Iterator<String> keyIt = keys.iterator();
            		while(keyIt.hasNext()){
            			String key = keyIt.next();
            			String lineValue = StringUtil.objToStr(linesMap.get(key));
            			String trainValue = StringUtil.objToStr(linesMap.get(key));
            			if(!lineValue.equals(trainValue)){
            				status = -1;
            				break label;
            			}
            		}
            	}
            }
           
        } catch (Exception e) {
            logger.error("planId: " + planId + " - lineId:" + lineId, e);
            return 0;
        }
        return status;
    }

    public int checkRouting(String planId, String lineId) {
        return 0;
    }  

    /**
     * 检查列车是否存在重复数据.
     * 
     * @param planId
     * @return
     */
    public int checkTrainCfMatch(Map<String, Object> param) {
        try {
            List<Map<String, Object>> result = runPlanDao.checkTrainCfMatch(param);
            if (result.size() == 0) {
                return -1;
            }
            Map<String, Object> cells = result.get(0);
            if (cells.containsValue(0)) {
                return -1;
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    } 

    /**
     * 检查列车是否存在重复数据.
     * 
     * @param planId
     * @return
     */
    public List<Map<String, Object>> checkTrainCfData(Map<String, Object> param) {
            List<Map<String, Object>> result = runPlanDao.checkTrainCfData(param);
            if (result.size() == 0) {
                return result;
            }
        return result;
    }
}
