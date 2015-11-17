package org.railway.com.trainplan.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/crew")
public class CrewController {

	/**
	 * 乘务信息查询跳转
	 * @return
	 */
	@RequestMapping(value = "page/all", method = RequestMethod.GET)
	public ModelAndView pageCrewAll(@RequestParam(defaultValue = "") String crewType, ModelAndView modelAndView) {
        modelAndView.setViewName("hightlineCrew/hightline_crew_all");
        modelAndView.addObject("crewType", crewType);
        return modelAndView;
	}
	@RequestMapping(value = "page/jyQuery", method = RequestMethod.GET)
	public ModelAndView pageCrewAllJy(@RequestParam(defaultValue = "") String crewType, ModelAndView modelAndView) {
        modelAndView.setViewName("hightlineCrew/hightline_crew_all_jy");
        modelAndView.addObject("crewType", crewType);
        return modelAndView;
	}

	/**
	 * 车长乘务信息跳转
	 * @return
	 */
	@RequestMapping(value = "page/cz", method = RequestMethod.GET)
	public String pageCrewCz() {
		return "hightlineCrew/hightline_crew_cz";
	}
	@RequestMapping(value = "page/cz/jy", method = RequestMethod.GET)
	public String pageCrewCzJy() {
		return "hightlineCrew/hightline_crew_cz_jy";
	}
	@RequestMapping(value = "page/cz/add", method = RequestMethod.GET)
	public String pageCrewCzAdd() {
		return "hightlineCrew/hightline_crew_cz_add";
	}
	
	
	@RequestMapping(value = "page/jy", method = RequestMethod.GET)
	public String pageCrewJy() {
		return "hightlineCrew/hightline_crew_cz_jy";
	}
	@RequestMapping(value = "page/jy/add", method = RequestMethod.GET)
	public String pageCrewJyAdd() {
		return "hightlineCrew/hightline_crew_cz_add";
	}
	
	/**
	 * 司机乘务信息跳转
	 * @return
	 */
	@RequestMapping(value = "page/sj", method = RequestMethod.GET)
	public String pageCrewSj() {
		return "hightlineCrew/hightline_crew_sj";
	}
	@RequestMapping(value = "page/sj/jy", method = RequestMethod.GET)
	public String pageCrewSjJy() {
		return "hightlineCrew/hightline_crew_sj_jy";
	}
	
	/**
	 * 机械师乘务信息跳转
	 * @return
	 */
	@RequestMapping(value = "page/jxs", method = RequestMethod.GET)
	public String pageCrewJxs() {
		return "hightlineCrew/hightline_crew_jxs";
	}
	@RequestMapping(value = "page/jxs/jy", method = RequestMethod.GET)
	public String pageCrewJxsJy() {
		return "hightlineCrew/hightline_crew_jxs_jy";
	}
}
