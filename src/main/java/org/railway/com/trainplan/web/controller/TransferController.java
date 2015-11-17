package org.railway.com.trainplan.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用于默认的页面跳转
 * @author join
 *
 */
@Controller
@RequestMapping(value = "/default/transfer")
public class TransferController {
    
	@RequestMapping(value = "planReviewLines", method = RequestMethod.GET)
	public ModelAndView planReviewLines(@RequestParam(defaultValue = "") String train_type, ModelAndView modelAndView) {
		if("0".equals(train_type))//既有上图
			modelAndView.setViewName("plan/plan_review_lines");
		if("1".equals(train_type))//高铁上图
			modelAndView.setViewName("plan/plan_review_GTlines");
	    modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}
	
	@RequestMapping(value = "planReviewAll", method = RequestMethod.GET)
	public ModelAndView planReviewAll(@RequestParam(defaultValue = "") String train_type, ModelAndView modelAndView) {
        modelAndView.setViewName("plan/plan_review_all");
        modelAndView.addObject("train_type", train_type);
		return modelAndView;
	}
	
	@RequestMapping(value = "planReview", method = RequestMethod.GET)
	public String planReview() {
		return "plan/plan_review";
	}
	
	@RequestMapping(value = "planRunlineBatch", method = RequestMethod.GET)
	public String planRunlineBatch() {
		return "plan/plan_runline_batch";
	}
	
	@RequestMapping(value = "planDesign", method = RequestMethod.GET)
	public String planDesign() {
		return "plan/plan_design";
	}
	
	@RequestMapping(value = "planConstruction", method = RequestMethod.GET)
	public String planConstruction() {
		return "plan/plan_construction";
	}
	
	@RequestMapping(value = "/plan/planView", method = RequestMethod.POST)
	public ModelAndView planReviews(HttpServletRequest request){
		ModelAndView model = new ModelAndView("plan/plan_view");
		
		model.addObject("schemeVal", request.getParameter("schemeVal"));
		model.addObject("schemeText", request.getParameter("schemeText"));
		model.addObject("days",request.getParameter("days"));
		model.addObject("startDate",request.getParameter("startDate"));

		return model;
	}
}
