package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.service.RunLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.web.dto.PlanLineInfoDto;
import org.railway.com.trainplan.web.dto.PlanLineSTNDTOCOM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Created by star on 5/12/14.
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

    private final static Log logger = LogFactory.getLog(IndexController.class);

    @Autowired
    private RunPlanService runPlanService;

    @Autowired
    private RunLineService runLineService;

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public ModelAndView audit(@RequestParam(defaultValue = "") String train_type, ModelAndView modelAndView) {
    	if("0".equals(train_type)){
    		modelAndView.setViewName("trainplan/planline_check");//开行  核对
    	}
    	if("1".equals(train_type)){
    		modelAndView.setViewName("trainplan/planline_checkGT");
    	}
    	if("2".equals(train_type)){
    		modelAndView.setViewName("completionplan/planline_check");//落成
    	}
    	if("3".equals(train_type)){
    		modelAndView.setViewName("completionplan/planline_checkGT");
    	}
        modelAndView.addObject("train_type", train_type);
        return modelAndView;
    }
    
    @RequestMapping(value = "releasePlan", method = RequestMethod.GET)
    public ModelAndView releasePlan(@RequestParam(defaultValue = "") String train_type, ModelAndView modelAndView) {
        modelAndView.setViewName("trainplan/releasePlan");
        modelAndView.addObject("train_type", train_type);
        return modelAndView;
    }

    @RequestMapping(value = "audit/compare/traininfo/plan/{planId}/line/{lineId}", method = RequestMethod.GET)
    public ModelAndView compareTrainInfo(@PathVariable String planId, @PathVariable String lineId, ModelAndView modelAndView) {
        logger.info("compareTrainInfo##################");
        // 查询客运计划主体信息
        Map<String, Object> plan = runPlanService.findPlanInfoByPlanId(planId);
        PlanLineInfoDto planDto = new PlanLineInfoDto(plan);
        modelAndView.addObject("plan", planDto);
        // 查询日计划主体信息
        Map<String, Object> line = runLineService.findLineInfoByLineId(lineId);
        PlanLineInfoDto lineDto = null;
        if(null != line){
        	lineDto = new PlanLineInfoDto(line);
        }
        modelAndView.addObject("line", lineDto);

        // 添加跳转信息
        modelAndView.setViewName("trainplan/compare_traininfo");
        return modelAndView;
    }

    /**
     * 比较客运计划和日计划时刻表
     * @param bureau
     * @param planId
     * @param lineId
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "audit/compare/timetable/{bureau}/plan/{planId}/line/{lineId}", method = RequestMethod.GET)
    public ModelAndView compareTimetable(@PathVariable String bureau, @PathVariable String planId,
                                  @PathVariable String lineId, ModelAndView modelAndView) {
        logger.info("compareTimetable###################");
        modelAndView.setViewName("trainplan/compare_timetable");
        // 取客运计划时刻表
        List<PlanLineSTNDTOCOM> planList = new ArrayList<PlanLineSTNDTOCOM>();
        List<Map<String, Object>> plans = runPlanService.findPlanTimeTableByPlanId(planId);
        for(Map<String, Object> map: plans) {
            planList.add(new PlanLineSTNDTOCOM(map, bureau));
        }
        modelAndView.addObject("planList", planList);
        // 取日计划时刻表
        List<PlanLineSTNDTOCOM> lineList = new ArrayList<PlanLineSTNDTOCOM>();
        List<Map<String, Object>> lines = runLineService.findLineTimeTableByLineId(lineId);
        for(Map<String, Object> map: lines) {
            lineList.add(new PlanLineSTNDTOCOM(map, bureau));
        }
        modelAndView.addObject("lineList", lineList);
        return modelAndView;
    }

    @RequestMapping(value = "audit/plan/routing", method = RequestMethod.GET)
    public String routing() {
        return "trainplan/routing";
    }


    @RequestMapping(value = "audit/planline", method = RequestMethod.GET)
    public ModelAndView graphic(@RequestParam(value = "date") String date, @RequestParam(value = "bureau") String bureau,
                          @RequestParam(value = "plans", defaultValue = "") String plans,
                          @RequestParam(value = "lines", defaultValue = "") String lines,
                          ModelAndView modelAndView) throws JsonProcessingException {
        modelAndView.setViewName("trainplan/planline");
        logger.debug("get grid");
        logger.debug("get runplan");
        logger.debug("get runline");
        return modelAndView;
    }

    @RequestMapping(value = "outer/trainline/{date}", method = RequestMethod.GET)
    public ModelAndView outerTrainLine(@PathVariable String date, ModelAndView modelAndView) {
        modelAndView.setViewName("trainplan/outer_train_line");

        return modelAndView;
    }
}
