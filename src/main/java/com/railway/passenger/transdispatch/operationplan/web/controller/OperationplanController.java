package com.railway.passenger.transdispatch.operationplan.web.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmCrossServiceImpl;
import com.railway.passenger.transdispatch.comfirmedmap.service.impl.CmOriginalCrossServiceImpl;
import com.railway.passenger.transdispatch.operationplan.entity.TCmPlanAutogenerate;
import com.railway.passenger.transdispatch.operationplan.service.impl.OperationlanServiceImpl;
import com.railway.passenger.transdispatch.util.CrossInfoGenHelper;
import com.railway.passenger.transdispatch.util.LogUtil;
import com.railway.passenger.transdispatch.util.TimeUtils;

@Controller
@RequestMapping(value = "/operationplan")
public class OperationplanController {
	
	@Autowired
	private OperationlanServiceImpl operationlanServiceImpl;
	
	@Autowired
	private CmOriginalCrossServiceImpl cmOriginalCrossServiceImpl;
	
	@Autowired
	private CmCrossServiceImpl cmCrossServiceImpl;
	
	private ExecutorService pool = Executors.newFixedThreadPool(10);
	private CompletionService<String> completion = new ExecutorCompletionService<String>(pool);
	
	private static Log logger = LogFactory.getLog(OperationplanController.class
			.getName());
	
	/**
	 * 
	 * @Description: 自动生成设置
	 * @param @param modelAndView
	 * @param @return   
	 * @return ModelAndView  
	 * @throws
	 * @author qs
	 * @date 2015年10月26日
	 */
	@RequestMapping(value = "/autoGenSet", method = RequestMethod.GET)
	public ModelAndView autoGenSet(ModelAndView modelAndView) {
		modelAndView.setViewName("runPlan/run_plan_autoGenSet");
		List<TCmPlanAutogenerate> tpas = operationlanServiceImpl.getAll();
		Map<String, List<TCmPlanAutogenerate>> map = new HashMap<String, List<TCmPlanAutogenerate>>();
		for(TCmPlanAutogenerate tpa : tpas){
			if(!map.containsKey(tpa.getTokenVehBureau())){
				map.put(tpa.getTokenVehBureau(), new ArrayList<TCmPlanAutogenerate>());
			}
			map.get(tpa.getTokenVehBureau()).add(tpa);
		}
		modelAndView.addObject("data", map);
		return modelAndView;
	}
	
	/**
	 * 
	 * @Description: 保存路局的自动生成开行计划信息
	 * @param @param params
	 * @param @return   
	 * @return Result  
	 * @throws
	 * @author qs
	 * @date 2015年10月9日
	 */
	@SuppressWarnings({ "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/saveAutoGenInfo", method = RequestMethod.POST)
	public Result saveAutoGenInfo(@RequestBody Map<String, Object> params){
		Result result = new Result();
		try{
			//获取用户信息
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
			String [] bureaus = MapUtils.getString(params, "bureaus").split(",");
			String [] common_days = MapUtils.getString(params, "common_days").split(",");
			String [] common_genTime = MapUtils.getString(params, "common_genTime").split(",");
			String [] high_days = MapUtils.getString(params, "high_days").split(",");
			String [] high_genTime = MapUtils.getString(params, "high_genTime").split(",");
			for(int i = 0;i<bureaus.length;i ++){
				TCmPlanAutogenerate common = operationlanServiceImpl.selectByParam(bureaus[i], "0");
				if(common == null){
					common = new TCmPlanAutogenerate();
					common.setCmAutogenId(UUID.randomUUID().toString());
					common.setCreatePeople(user.getName());
					common.setCreateTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
					common.setGenerateTime(common_genTime[i]);
					common.setHighlineFlag((short)0);
					common.setIsAutoGenerate((short)1);
					common.setMaintainDays(Short.valueOf(common_days[i]));
					common.setTokenVehBureau(bureaus[i]);
					operationlanServiceImpl.save(common);
				} else {
					common.setCreatePeople(user.getName());
					common.setCreateTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
					common.setGenerateTime(common_genTime[i]);
					common.setHighlineFlag((short)0);
					common.setIsAutoGenerate((short)1);
					common.setMaintainDays(Short.valueOf(common_days[i]));
					common.setTokenVehBureau(bureaus[i]);
					operationlanServiceImpl.update(common);
				}
				
				TCmPlanAutogenerate high = operationlanServiceImpl.selectByParam(bureaus[i], "1");
				if(high == null){
					high = new TCmPlanAutogenerate();
					high.setCmAutogenId(UUID.randomUUID().toString());
					high.setCreatePeople(user.getName());
					high.setCreateTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
					high.setGenerateTime(high_genTime[i]);
					high.setHighlineFlag((short)1);
					high.setIsAutoGenerate((short)1);
					high.setMaintainDays(Short.valueOf(high_days[i]));
					high.setTokenVehBureau(bureaus[i]);
					operationlanServiceImpl.save(high);
				} else {
					high.setCreatePeople(user.getName());
					high.setCreateTime(TimeUtils.getDateString(new Date(), TimeUtils.DEFAULTFORMAT));
					high.setGenerateTime(high_genTime[i]);
					high.setHighlineFlag((short)1);
					high.setIsAutoGenerate((short)1);
					high.setMaintainDays(Short.valueOf(high_days[i]));
					high.setTokenVehBureau(bureaus[i]);
					operationlanServiceImpl.update(high);
				}
			}
		} catch(Exception e){
			result.setCode(-1);
			result.setMessage("保存失败:" + e.getMessage());
		}
		return result;
	}
	
	/**
	 * 生成开行计划
	 * 
	 * @return 正在生成计划的基本交路id
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/plantrain/gen", method = RequestMethod.POST)
	public Result generatePlanTrainBySchemaId(
			@RequestBody Map<String, Object> params) {
		Result result = new Result();
		//获取用户信息
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		//获取方案id(基本图中的方案)		
		String baseChartId = MapUtils.getString(params, "baseChartId");
		//获取开行计划开始的时间
		String startDate = MapUtils.getString(params, "startDate");
		if(StringUtils.isEmpty(startDate)){
			startDate = TimeUtils.date2String(new Date(), TimeUtils.YYYYMMDD);
		}
		//获取开行计划截止的时间
		String endDate = MapUtils.getString(params, "endDate");
		//获取生成天数
		int days = MapUtils.getIntValue(params, "days");
		if(StringUtils.isEmpty(endDate)){
			endDate = TimeUtils.getNextDateString(startDate, days, TimeUtils.YYYYMMDD);
		}
		//获取cm_cross_id，参数获取用了"unitcrossId"，但值已经是cm_cross_id，这里是为了
		//减少前端工作量暂时的处理，不影响程序运行结果。
		List<String> cmCrossId = (List<String>) params.get("unitcrossId");
		for(String id : cmCrossId){
			TCmCross cross = cmCrossServiceImpl.getTCmCross(id);
			operationlanServiceImpl.generatePlanCrossInfo(cross, startDate, endDate);
		}
		//获取交路名称cm_cross_name
		List<String> crossName = (List<String>) params.get("crossName");
		//用于dwr推送消息定位
		String msgReceiveUrl = MapUtils.getString(params, "msgReceiveUrl");


		return result;
	}
	
	@RequestMapping(value = "/doNewPLanWork", method = RequestMethod.POST)
	//遍历设置了自动滚动生成的开行计划 依次往后生成一天的开行计划
	public void doNewPLanWork(){
		operationlanServiceImpl.autoGeneratePlan();
	}
	
}
