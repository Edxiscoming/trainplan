package org.railway.com.trainplan.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.railway.com.trainplan.common.constants.OperationConstants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.PlanSent;
import org.railway.com.trainplan.jdbcConnection.CmdAdapterServiceImpl;
import org.railway.com.trainplan.jdbcConnection.ICmdAdapterService;
import org.railway.com.trainplan.service.ChartService;
import org.railway.com.trainplan.service.CompletedRunlineService;
import org.railway.com.trainplan.service.HttpClientService;
import org.railway.com.trainplan.service.PlanCheckService;
import org.railway.com.trainplan.service.PlanLineService;
import org.railway.com.trainplan.service.PlanSentService;
import org.railway.com.trainplan.service.RunLineService;
import org.railway.com.trainplan.service.RunPlanService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PlanTrainDTO2;
import org.railway.com.trainplan.service.dto.PlanTrainDto;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.railway.com.trainplan.web.dto.ChartDto;
import org.railway.com.trainplan.web.dto.PlanLineCheckResultDto;
import org.railway.com.trainplan.web.dto.PublishHeader;
import org.railway.com.trainplan.web.dto.Result;
import org.railway.com.trainplan.web.dto.RunPlanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 审核相关不涉及页面跳转rest接口
 * Created by star on 5/12/14.
 */
@RestController
@RequestMapping(value = "/audit")
public class AuditController {

    private final static Log logger = LogFactory.getLog(AuditController.class);

    @Autowired
    private RunPlanService runPlanService;
    
    @Autowired
    CompletedRunlineService completedRunlineService;

    @Autowired
    private PlanLineService planLineService;

    @Autowired
    private RunLineService runLineService;

    @Autowired
    private ChartService chartService;
  //落成头文件请求地址
    @Value("#{restConfig['completion.header.url']}")
    private String headerUrl;
    //落成头文件请求地址
    @Value("#{restConfig['completion.text.before.url']}")
    private String textBeforeUrl;
    //落成头文件请求地址
    @Value("#{restConfig['completion.text.after.url']}")
    private String textAfterUrl;
    
    @Autowired
    private HttpClientService httpClientService;
    
    List<RunPlanDTO> resultGlb = null;
    
    @Autowired
    private PlanSentService planSentService;

	@Autowired
	private SendMsgService msgService;

	@Autowired
	private PlanCheckService planCheckService;
	
	
    
    @RequestMapping(value = "plan/runplan/{date}/{type}/{checkLev2Type}/{tokenVehBureauNbr}", method = RequestMethod.GET)
    public ResponseEntity<List<RunPlanDTO>> getRunPlan(@PathVariable String date, @PathVariable int type,
                                                       @RequestParam(defaultValue = "") String name,
                                                       @PathVariable String checkLev2Type,
                                                       @PathVariable String tokenVehBureauNbr,
                                                       @RequestParam(defaultValue = "") int trainType) throws ParseException {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        logger.debug("-X GET plan/runplan/");
        List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
        List<PlanTrainDTO2> list =  runPlanService.findRunPlan(date,checkLev2Type,tokenVehBureauNbr,user.getBureau(), user.getBureauShortName(), type, trainType);
        long b = System.currentTimeMillis();
        for(PlanTrainDTO2 train: list) {
        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date));
        }
        //去掉分界站时间不满足查询时间的站
        resultGlb = new ArrayList<RunPlanDTO>();
        for (RunPlanDTO runPlanDTO : resultCache) {
			if("1".equals(runPlanDTO.getIsValid())){
				resultGlb.add(runPlanDTO);
			}
		}
        logger.debug("getRunPlan end::::");
        ResponseEntity<List<RunPlanDTO>> responseEntity = new ResponseEntity<List<RunPlanDTO>>(resultGlb, HttpStatus.OK);

        return responseEntity;
    }
    
    /**
     * ...
     * 
     * @param date
     * @param type
     * @param name
     * @param checkLev2Type
     * @param tokenVehBureauNbr
     * @param train_nbr
     * @param trainType
     * @return
     * @throws ParseException
     */
	@RequestMapping(value = "plan/runplan1/{date}/{type}/{sentFlag}/{tokenVehBureauNbr}", method = RequestMethod.GET)
    public ResponseEntity<List<RunPlanDTO>> getRunPlan1(@PathVariable String date, @PathVariable int type,
                                                       @RequestParam(defaultValue = "") String name,
                                                       @PathVariable String sentFlag,
                                                       @PathVariable String tokenVehBureauNbr,
                                                       @RequestParam String train_nbr,
                                                       @RequestParam(defaultValue = "") int trainType) throws ParseException {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
        List<PlanTrainDTO2> list =  runPlanService.findRunPlan1(date,sentFlag,tokenVehBureauNbr,user.getBureau(), user.getBureauShortName(), type, trainType,train_nbr);
//        long b = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        for(PlanTrainDTO2 train: list) {
        	// 根据PLAN_CROSS_ID查询出str1
        	map.clear();
        	if(StringUtils.isNotEmpty(train.getPlanCrossId())){
        		map.put("planCrossId", train.getPlanCrossId());
        	}else{
        		if(StringUtils.isNotEmpty(train.getCmdTrainId())){
        			map.put("cmdTelId", train.getCmdTrainId());
        		}else{
        			if(StringUtils.isNotEmpty(train.getTelId())){
        				map.put("cmdTelId", train.getTelId());
        			}
        		}
        	}
        	if(!map.isEmpty()){
        		map.put("checkHisFlag", 0);
            	map.put("checkState", 0);
            	List<Map<String, Object>> lm = planCheckService.getcheckStateByPlanCrossId(map);
            	if(!lm.isEmpty()){
            		StringBuilder sb = new StringBuilder();
            		for (Map<String, Object> map2 : lm) {
    					sb.append(LjUtil.getLjByNameBs(MapUtils.getString(map2, "CHECK_BUREAU"), 2));
    				}
//            		System.out.println(sb);
            		train.setStr1(sb.toString());
            	}
        	}
        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date));
        }
        //去掉分界站时间不满足查询时间的站
        resultGlb = new ArrayList<RunPlanDTO>();
        for (RunPlanDTO runPlanDTO : resultCache) {
			if("1".equals(runPlanDTO.getIsValid())){
				
				resultGlb.add(runPlanDTO);
			}
		}
        
        logger.debug("getRunPlan end::::");
        ResponseEntity<List<RunPlanDTO>> responseEntity = new ResponseEntity<List<RunPlanDTO>>(resultGlb, HttpStatus.OK);

        return responseEntity;
    }
    
	/**
	 * 预览命令
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "plan/creatCmdInfo/{date}/{type}/{checkLev2Type}/{tokenVehBureauNbr}/{train_nbr}", method = RequestMethod.GET)
	public Result creatCmdInfo(@PathVariable String date, @PathVariable int type,
            @RequestParam(defaultValue = "") String name,
            @PathVariable String checkLev2Type,
            @PathVariable String tokenVehBureauNbr,
            @PathVariable String train_nbr,
            
            @RequestParam(defaultValue = "") int trainType){
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
		Result result = new Result();
		 try{
			 
			 final String f_pointBlank = ". ";
			 final String f_oneCatalog = "一、普速图外临客（含图外旅游列车）";
			 final String f_twoCatalog = "二、停运的图定客车（指有停运起止日期的，不含停运至另止的）";
			 
			 StringBuffer  tdInfoBf = new StringBuffer();
			 StringBuffer  cmdInfoBf = new StringBuffer();

			/*
			
			 2015年1月29日北京局普速图外临客及图定客车停运计划：
			 一、普速图外临客（含图外旅游列车）
			1. L27 北京(0129 14:00)——济南(0130 03:10)，德州(0129 19:05)交出，来源：0127-7001(铁总1705)
			2. L2 广州(0129 17:00)——沈阳(0130 05:10)，孟庙(0129 18:05)接入，山海关(0129 19:05)交出，来源：0128-7002(铁总1706)
			...
			
			二、停运的图定客车（指有停运起止日期的，不含停运至另止的）
			1. K41 北京(0129 13:02)——庙城(0129 14:10)
			2. 02550 北京(0129 13:30)——天津(0129 16:05)
			...*/
			 cmdInfoBf.append(DateUtil.parseDateLocalChanese(date)+user.getBureauFullName()+"普速图外临客及图定客车停运计划：\n\n");
	        List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
	        List<PlanTrainDTO2> list =  runPlanService.findRunPlan1(date,checkLev2Type,tokenVehBureauNbr,user.getBureau(), user.getBureauShortName(), type, trainType,train_nbr);
	        for(PlanTrainDTO2 train: list) {
	        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date));
	        }
			 
		     
			 if(resultCache.size()>0){
				int countLK = 0;
				int conutTD = 0;
				cmdInfoBf.append(f_oneCatalog+"\n");
				for (RunPlanDTO plan : resultCache) {
					if("1".equals(plan.getIsValid())){
						if(!"基本图".equals(plan.getCreatType())){//临客
							cmdInfoBf.append(++countLK).append(f_pointBlank).append(plan.getSerial()+"\t\t").append(plan.getStartStn()+"(").append(plan.getStartTime()+")--").append(plan.getEndStn()+"(").append(plan.getEndTime()+")，");
							if(plan.getJrStn()!=null){
								cmdInfoBf.append(plan.getJrStn()).append("("+plan.getJrTime()+")接入，");
							}
							if(plan.getJcStn()!=null){
								cmdInfoBf.append(plan.getJcStn()).append("("+plan.getJcTime()+")交出，");
							}
							cmdInfoBf.append("来源:"+plan.getCreatType()).append("\n");
						}
						else{
							if("停运".equals(plan.getSpareFlag())){
								tdInfoBf.append(++countLK).append(f_pointBlank).append(plan.getSerial()+"\t\t").append(plan.getStartStn()+"(").append(plan.getStartTime()+")--").append(plan.getEndStn()+"(").append(plan.getEndTime()+")，");
								if(plan.getJrStn()!=null){
									tdInfoBf.append(plan.getJrStn()).append("("+plan.getJrTime()+")接入，");
								}
								if(plan.getJcStn()!=null){
									tdInfoBf.append(plan.getJcStn()).append("("+plan.getJcTime()+")交出，");
								}
								tdInfoBf.append("来源:"+plan.getCreatType()).append("\n");
							}
						}
					}
				}
				String cmdInfo = cmdInfoBf.toString() + "\n" + f_twoCatalog + tdInfoBf.toString();
				 
				result.setData(cmdInfo);
			 }else{
				 //没有命令
				 result.setData("-1");
			 }
			 
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("previewCmdInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	
	/**
	 * 普通列车生成命令
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCmd", method = RequestMethod.POST)
	public Result createCmdInfo(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		 try{
			 
			 String cmdInfo = StringUtil.objToStr(reqMap.get("cmdInfo"));
			 
			 List<String> cmdContent = new ArrayList<String>();
			 cmdContent.add(cmdInfo);
			 
			 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
						.getSubject().getPrincipal();
			 String bureauCode = user.getBureau();
			 String peopleName = user.getName();
			 String accontName = user.getPostName();
			 logger.debug("cmdInfo==" + cmdInfo);
			 logger.debug("bureauCode==" + bureauCode + " peopleName==" + peopleName + " accontName==" + accontName);
		 
		 
			//构造服务实例
			ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
			//服务初始化
			service.initilize(bureauCode);
				
			//生成日计划命令
			String identifyStr = service.buildOrdinaryPlanCmd(cmdContent, accontName, peopleName);
			logger.debug("流水号="+identifyStr);
			result.setMessage(identifyStr);
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("createCmdInfo error==" + e.getMessage());
			 result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			 result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());	
		 }
		
		 return result;
	}
	
	
    @RequestMapping(value = "plan/{planId}/line/{lineId}/check", method = RequestMethod.GET)
    @RequiresRoles({"局客运调度", "局值班主任"})
	public PlanLineCheckResultDto checkPlanLine(@PathVariable String planId,
			@PathVariable String lineId) {
		PlanLineCheckResultDto result = new PlanLineCheckResultDto();
		// 存在？条冗余运行线
		 // 检查列车信息
		 result.setIsTrainInfoMatch(planLineService.checkTrainInfo(planId, lineId));
		 // 检查时刻表
		 result.setIsTimeTableMatch(planLineService.checkTimeTable(planId, lineId));
		 // 检查经由
		
		// 检查重复列车
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("plan_train_id", planId);
		List<Map<String, Object>> lm = planLineService.checkTrainCfData(param);
		StringBuilder sb = new StringBuilder();
		if(lm.size() > 1){
			result.setIsTrainCfMatch(planLineService.checkTrainCfMatch(param));
			for (int i = 0; i < lm.size(); i++) {
				Map<String, Object> m = lm.get(i);
				sb.append(m.get("PLAN_TRAIN_ID")).append(",");
			}
		}else{
			result.setIsTrainCfMatch(0);
		}
		result.setIsTrainCfData(sb.toString());
		return result;
	}


    @RequestMapping(value = "plan/checklev1/{checkType}", method = RequestMethod.POST)
    @RequiresPermissions("JHPT.RJH.KDSP")//局客运调度权限
    public ResponseEntity<List<Map<String, Object>>> checkLev1(@PathVariable int checkType, @RequestBody List<Map<String, Object>> data) {
        logger.debug("data::::" + data);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> resp = runPlanService.checkLev1(data, user, checkType);
        return new ResponseEntity<List<Map<String, Object>>>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "plan/checklev2/{checkType}", method = RequestMethod.POST)
    @RequiresPermissions("JHPT.RJH.KDSP")//局值班主任
    public ResponseEntity<List<Map<String, Object>>> checkLev2(@PathVariable int checkType, @RequestBody List<Map<String, Object>> data) {
        logger.debug("data::::" + data);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<Map<String, Object>> resp = runPlanService.checkLev2(data, user, checkType);
        return new ResponseEntity<List<Map<String, Object>>>(resp, HttpStatus.OK);
    }


    @RequestMapping(value = "plan/chart/traintype/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getTrainType(@PathVariable String date, 
    		@PathVariable int type, @RequestParam(defaultValue = "") String name,
    		@RequestParam(defaultValue = "") int trainType) throws ParseException {
        /*ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<PlanTrainDTO2> list =  runPlanService.findRunPlan(date,user.getBureau(), user.getBureauShortName(), name, type, trainType);
        List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
        for(PlanTrainDTO2 train: list) {
        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date));
        }
        //去掉分界站时间不满足查询时间的站
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        for (RunPlanDTO runPlanDTO : resultCache) {
			if("1".equals(runPlanDTO.getIsValid())){
				result.add(runPlanDTO);
			}
		}*/
    	HttpHeaders httpHeaders = new HttpHeaders();
        List<ChartDto> charts = Lists.newArrayList();
        int running = 0;
        int backup = 0;
        int stopped = 0;
        int unkown = 0;
        for(RunPlanDTO train: resultGlb) {
        	if("开行".equals(train.getSpareFlag())){
        		running++;
        	}
        	else if("备用".equals(train.getSpareFlag())){
        		backup++;
        	}
        	else if("停运".equals(train.getSpareFlag())){
        		stopped++;
        	}
        	else 
        		unkown++;
        }
        
        ChartDto chart1 = new ChartDto();
        chart1.setName("开行");
        chart1.setCount(running);
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("备用");
        chart2.setCount(backup);
        charts.add(chart2);

        ChartDto chart3 = new ChartDto();
        chart3.setName("停运");
        chart3.setCount(stopped);
        charts.add(chart3);

        ChartDto chart4 = new ChartDto();
        chart4.setName("未知");
        chart4.setCount(unkown);
        charts.add(chart4);
        return new ResponseEntity<List<ChartDto>>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 上图状态统计图接口
     * @param date 日期，格式：yyyymmdd
     * @param type 是否高线，0：既有线，1：高线
     * @param name 车次
     * @return 统计结果
     * @throws ParseException 
     */
    @RequestMapping(value = "plan/chart/planline/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getPlanLine(@PathVariable String date, @PathVariable int type, @RequestParam(defaultValue = "") String name,
    		@RequestParam(defaultValue = "") int trainType) throws ParseException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ChartDto> charts = Lists.newArrayList();

        int line = 0;
        int unline = 0;
        int unkown = 0;
        /*
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        List<PlanTrainDTO2> list =  runPlanService.findRunPlan(date,user.getBureau(), user.getBureauShortName(), name, type, trainType);
        List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
        for(PlanTrainDTO2 train: list) {
        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date));
        }
        //去掉分界站时间不满足查询时间的站
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        for (RunPlanDTO runPlanDTO : resultCache) {
			if("1".equals(runPlanDTO.getIsValid())){
				result.add(runPlanDTO);
			}
		}*/
        for(RunPlanDTO train: resultGlb) {
        	if(1==train.getDailyLineFlag()){
        		line++;
        	}
        	else if(0==train.getDailyLineFlag()){
        		unline++;
        	}
        	else
        		unkown++;
        }
        
        
        ChartDto chart1 = new ChartDto();
        chart1.setName("已上图");
        chart1.setCount(line);
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("未上图");
        chart2.setCount(unline);
        charts.add(chart2);

        ChartDto chart3 = new ChartDto();
        chart3.setName("未知");
        chart3.setCount(unkown);
        charts.add(chart3);
        return new ResponseEntity<>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 一级审核统计图
     * @param date 日期
     * @param type 是否高线，0：否，1：是
     * @param name 车次
     * @return 统计结果
     * @throws ParseException 
     */
    @RequestMapping(value = "plan/chart/lev1check/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getLev1CheckPie(@PathVariable String date, @PathVariable int type, @RequestParam(defaultValue = "") String name,
    		@RequestParam(defaultValue = "") int trainType) throws ParseException {
//        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ChartDto> charts = Lists.newArrayList();

        int checked = 0;
        int unchecked = 0;
        int unkown = 0;
        
        /*List<PlanTrainDTO2> list =  runPlanService.findRunPlan(date,user.getBureau(), user.getBureauShortName(), name, type, trainType);
        List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
        for(PlanTrainDTO2 train: list) {
        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date));
        }
        //去掉分界站时间不满足查询时间的站
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        for (RunPlanDTO runPlanDTO : resultCache) {
			if("1".equals(runPlanDTO.getIsValid())){
				result.add(runPlanDTO);
			}
		}*/
        for(RunPlanDTO train: resultGlb) {
        	if(1==train.getLev1Checked()){
        		checked++;
        	}
        	else if(0==train.getLev1Checked()){
        		unchecked++;
        	}
        	else
        		unkown++;
        }
        
        
        ChartDto chart1 = new ChartDto();
        chart1.setName("已审核");
        chart1.setCount(checked);
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("未审核");
        chart2.setCount(unchecked);
        charts.add(chart2);
        return new ResponseEntity<List<ChartDto>>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 二级审核统计图
     * @param date 日期
     * @param type 是否高线，0：否， 1：是
     * @param name 车次
     * @return 统计结果
     * @throws ParseException 
     */
    @RequestMapping(value = "plan/chart/lev2check/{date}/{type}", method = RequestMethod.GET)
    public ResponseEntity<List<ChartDto>> getLev2CheckPie(@PathVariable String date, @PathVariable int type, @RequestParam(defaultValue = "") String name,
    		@RequestParam(defaultValue = "") int trainType) throws ParseException {
//        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<ChartDto> charts = Lists.newArrayList();

        int checked = 0;
        int unchecked = 0;
        int unkown = 0;
        
       /* List<PlanTrainDTO2> list =  runPlanService.findRunPlan(date,user.getBureau(), user.getBureauShortName(), name, type, trainType);
        List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
        for(PlanTrainDTO2 train: list) {
        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date));
        }
        //去掉分界站时间不满足查询时间的站
        List<RunPlanDTO> result = new ArrayList<RunPlanDTO>();
        for (RunPlanDTO runPlanDTO : resultCache) {
			if("1".equals(runPlanDTO.getIsValid())){
				result.add(runPlanDTO);
			}
		}*/
        for(RunPlanDTO train: resultGlb) {
        	if(1==train.getLev2Checked()){
        		checked++;
        	}
        	else if(0==train.getLev2Checked()){
        		unchecked++;
        	}
        	else
        		unkown++;
        }
        
        ChartDto chart1 = new ChartDto();
        chart1.setName("已审核");
        chart1.setCount(checked);
        charts.add(chart1);

        ChartDto chart2 = new ChartDto();
        chart2.setName("未审核");
        chart2.setCount(unchecked);
        charts.add(chart2);
        return new ResponseEntity<List<ChartDto>>(charts, httpHeaders, HttpStatus.OK);
    }

    /**
     * 查询找不到对应客运计划的运行线数量（冗余运行线数量）
     * @param date 日期
     * @return 数量
     */
    @RequestMapping(value = "check/line/{date}/unknown", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findUnknownRunLine(@PathVariable String date) {
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> result;
        try {
            result = runLineService.findUnknownRunLineCount(user.getBureauShortName(), date);
        } catch (ParseException e) {
            logger.error("findUnknownRunLine", e);
            Map<String, Object> error = Maps.newHashMap();
            error.put("code", "500");
            error.put("message", "日期格式错误");
            return new ResponseEntity<Map<String, Object>>(error, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("findUnknownRunLine", e);
            Map<String, Object> error = Maps.newHashMap();
            error.put("code", "500");
            error.put("message", "查询冗余运行线出错");
            return new ResponseEntity<Map<String, Object>>(error, HttpStatus.OK);
        }
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
    @ResponseBody
    @RequestMapping(value = "plan/completion", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> completion(@RequestBody Map<String,Object> reqMap) throws Exception {
//    	,@RequestBody List<Map<String, Object>> data
        logger.debug("reqMap::::" + reqMap);
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
        boolean thereNodata = false;
        boolean isError = false;
        int count = 0;
        // 返回页面的数据
        List<Map<String, Object>> resp = new ArrayList<Map<String, Object>>();
        Map<String, Object> respMap = new HashMap<String, Object>();
        
        // 页面传递过来的数据
        List<Map<String, Object>> data = (List<Map<String, Object>>) reqMap.get("data");
        List<Map<String, Object>> map = (List<Map<String, Object>>) reqMap.get("map");
        // 当前路局简称
        String currentUserBureauName = (String)reqMap.get("currentUserBureauName");

        String status = "首次";
        for (Map<String, Object> train : data) {
			//落成单个列车信息
        	if("2".equals(train.get("sentStatus").toString())){
        		status = "再次";
        	}
        }
        
        String date = map.get(0).get("date").toString();
        logger.debug("落成：plan/completion::::");
        //落成头文件
        PublishHeader header = new PublishHeader();
        header.setJhrq(date);
        header.setJhbc("0");
        if("0".equals(map.get(0).get("trainType").toString())){//普线
        	String date1Jhsk = DateUtil.parseStringDateTOyyyymmdd(DateUtil.mulDateOneDay(DateUtil.parseStringDateTOYYYYMMDD(date)))+"180000";
        	String date2Jdjs = date+"180000";
        	header.setJdsk(date1Jhsk);
        	header.setJdjs(date2Jdjs);
        }
        if("1".equals(map.get(0).get("trainType").toString())){//高铁
        	header.setJdsk(date+"000000");
        	header.setJdjs(date+"235959");
        }
        header.setSbry(user.getName());
        header.setState(status);
        header.setLcs(data.size());
        header.setJsdw(user.getBureau());
        String params = JSONObject.fromObject(header).toString();
        
        // 获取落成发送时的时间,用户保存plan_sent
        Date dt = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        
        String returnMsg = httpClientService.sendHttpClient(headerUrl, params, OperationConstants.REQUEST_METHOD.POST);
        JSONObject msg = JSONObject.fromObject(new String(returnMsg));
        if(msg != null ){ 			 
//			 JSONObject result = msg.getJSONObject("success");
			 String isSuccess = msg.get("success").toString();
			 if("true".equals(isSuccess)){
					 logger.debug("========================");
	    			 logger.debug("========================");
	    			 logger.debug("========================");
		            PublishHeader headerTrain = new PublishHeader();
		            headerTrain.setJhrq(date);
		            headerTrain.setJhbc("0");
		            headerTrain.setJsdw(user.getBureau());
		            
		           
		        	for (Map<String, Object> train : data) {
		        			//落成单个列车信息
		        		headerTrain.setState("2".equals(train.get("sentStatus").toString()) ? "再次" : "首次");
		        		String paramsTrain = JSONObject.fromObject(headerTrain).toString();
		        		 String r = httpClientService.sendHttpClient(textBeforeUrl+train.get("lineId")+textAfterUrl, paramsTrain, OperationConstants.REQUEST_METHOD.POST);
		        		 if(r!=null){
			        		 JSONObject msgTrain = JSONObject.fromObject(new String(r));
			        		 String isSuccessTrain = msgTrain.get("success").toString();
			        		 Object isErrorTrainObject = msgTrain.get("error");
			        		 String isErrorTrain = "";
			        		 if(isErrorTrainObject!=null){
			        			 isErrorTrain = isErrorTrainObject.toString();
			        		 }
			        		 if("true".equals(isSuccessTrain)){
			        			 count++;
			        			 //暂时  不管
	//		        			 runPlanService.updateCompletion(train.get("planId").toString());
			        			 // 保存路局简称
								runPlanService.updPlanTrainBureau(
										user.getBureauShortName(), user.getBureauShortName(),
										(String) train.get("planId"));
			        			 // 记录日志
								planSentService.insertPlanSent(user,(String) train.get("planId"),sdf.format(dt));
								
			        			logger.debug(train.get("lineId")+"----已落成---");
			        			respMap.put("id", train.get("planId"));
			        			// 落成成功,会向2个字段都赋值,不会出现一个有一个没有的情况,所以就直接写死1
			        			respMap.put("lev2Checked", 1);
			        			resp.add(respMap);
			        		 }
			        		 else if(isErrorTrain!=null && isErrorTrain.contains("There is not a Trainline whose id is")){
			        			 thereNodata = true;
			        		 }
			        		 else{
			        			 isError = true;
			        		 }
		        		 }
		        		 else{
		        			 respMap.put("id", train.get("planId"));
		        			// 落成成功,会向2个字段都赋值,不会出现一个有一个没有的情况,所以就直接写死1
		        			respMap.put("lev2Checked", 0);
		        			resp.add(respMap);
		        			isError = true;
		        		 }
		     		}
		        	
		        	logger.debug("==========落成成功率："+count/data.size());
		        	logger.debug("========================");
		        	logger.debug("========================");
		        	logger.debug("========================");
		        }else{
		        	respMap.put("error", "落成失败!");
		        	resp.add(respMap);
		        }
        }else{
        	 isError = true;
        }
        
        
        int bl = count/data.size();
        if(bl == 0){//全部失败
        	if(isError){
	           	 respMap.put("error", "落成失败!");
        	}
        	if(thereNodata){
            	respMap.put("error", "落成失败,原因：未找到对应的1列车1条线数据。");
            }
        	resp.add(respMap);
        }
        else if(bl<1){
        	respMap.put("successwarn", "落成成功比例为： "+bl);
        	if(thereNodata){
            	respMap.put("successwarn", "落成失败,原因：未找到对应的1列车1条线数据。");
            }
        	resp.add(respMap);
        }
        else if(bl == 1){
        	respMap.put("success", "落成成功");
        	resp.add(respMap);
        }
        
        return new ResponseEntity<List<Map<String, Object>>>(resp, HttpStatus.OK);
	}
    
    
    /**
     * 2015-5-11 10:19:03.
     * 
     * @param reqMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "plan/completion1", method = RequestMethod.POST)
	public Result completion1(
			@RequestBody Map<String, Object> reqMap) throws Exception {
		List<Map<String, Object>> data = (List<Map<String, Object>>) reqMap
				.get("data");
		Result result = new Result();
		List<String> planTrainIdList = new ArrayList<String>();
		for (Map<String, Object> map : data) {
			// train.get("planId")
			// train.get("lineId")
			planTrainIdList.add(MapUtils.getString(map, "planId"));
		}
		
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
		completedRunlineService.completedRunline(user, planTrainIdList);
//		runPlanService.dealWithCompletion(planTrainIdList);
		
		result.setCode("0");
		return result;
	}
    
    /**
     * 单纯的校验功能
     * @param reqMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
   	@ResponseBody
    @RequestMapping(value = "plan/completedVaild", method = RequestMethod.POST)
   	public Result completionVaild(
   			@RequestBody Map<String, Object> reqMap) throws Exception {
   		List<Map<String, Object>> data = (List<Map<String, Object>>) reqMap
   				.get("data");
   		Result result = new Result();
   		List<String> planTrainIdList = new ArrayList<String>();
   		for (Map<String, Object> map : data) {
   			// train.get("planId")
   			// train.get("lineId")
   			planTrainIdList.add(MapUtils.getString(map, "planId"));
   		}
   		
   		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
   		completedRunlineService.completedVaild(user, planTrainIdList);
//   		runPlanService.dealWithCompletion(planTrainIdList);
   		
   		result.setCode("0");
   		return result;
   	}

	/**
	 * 查看落成记录.
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "plan/completionLog", method = RequestMethod.POST)
	public Result completionLog(@RequestBody Map<String, Object> reqMap) throws Exception {
		Result result = new Result();
		Map<String, Object> map  = new HashMap<String, Object>();
		map.put("sent_people_bureau", StringUtil.objToStr(reqMap.get("sent_people_bureau")));
		map.put("plan_train_Id", StringUtil.objToStr(reqMap.get("plan_train_Id")));
//		map.put("plan_train_Id", "6c6fabfd-abd2-45c5-b007-acccee32ab77");
		List<PlanSent> sents = planSentService.getPlanSentByMap(map);
		result.setData(sents);
		return result;
	}

	/**
	 * 获取重复列车.
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "plan/getTrainCf", method = RequestMethod.POST)
	public Result getTrainCf(@RequestBody Map<String, Object> reqMap) throws Exception {
		Result result = new Result();
		Map<String, Object> map  = new HashMap<String, Object>();
		map.put("idList", StringUtil.objToStr(reqMap.get("planTrainId")).split(","));
		List<PlanTrainDto> ptdList = runPlanService.getTrainCfByMap(map);
		result.setData(ptdList);
		return result;
	}

    /**
     * 差别计划
     * 
     * @param req
     * @return
     */
    @RequestMapping(value = "cbPlan", method = RequestMethod.GET)
    public ModelAndView cbPlan(HttpServletRequest req) {
    	ModelAndView model = new ModelAndView();
    	String train_type = req.getParameter("train_type");
    	if("0".equals(train_type)){
    		model.setViewName("trainplan/cb_plan");
    	}else if("1".equals(train_type)){
    		// 高铁差别
    		model.setViewName("trainplan/cb_plan_gt");
    	}
        
        ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        model.addObject("user", user);
        String basePath = req.getContextPath();
    	model.addObject("basePath", basePath);
        return model;
    }

    /**
     * 差别计划
     * 
     * @param req
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "getCbPlan", method = RequestMethod.POST)
    public Result getCbPlan(@RequestBody Map<String, Object> reqMap,HttpServletRequest req) {
    	Result result = new Result();
    	try {
    		/** 条件 **/
    		Map<String, Object> paramMap = new HashMap<String, Object>();
    		String date1 =(String)reqMap.get("planStartDate1");
    		String date2 =(String)reqMap.get("planStartDate2");
    		String bureauCode = (String)reqMap.get("passBureauCode");
    		String bureauName = (String)reqMap.get("passBureauName");
    		Integer trainType = (Integer)reqMap.get("trainType");
    		
    		List<RunPlanDTO> resultCache = new ArrayList<RunPlanDTO>();
    		/** 第1天的数据 **/
			List<PlanTrainDTO2> list1 =  runPlanService.findRunPlan1(date1,"0","0",bureauCode,bureauName,0,trainType,"");
	        for(PlanTrainDTO2 train: list1) {
	        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date1,null));
	        }
	        //去掉分界站时间不满足查询时间的站
	        List<RunPlanDTO> result1 = new ArrayList<RunPlanDTO>();
	        for (RunPlanDTO runPlanDTO : resultCache) {
				if("1".equals(runPlanDTO.getIsValid())){
					result1.add(runPlanDTO);
				}
			}
	        /** 第2天的数据 **/
	        resultCache.clear();
			List<PlanTrainDTO2> list2 =  runPlanService.findRunPlan1(date2,"0","0",bureauCode,bureauName,0,trainType,"");
	        for(PlanTrainDTO2 train: list2) {
	        	resultCache.add(new RunPlanDTO(train,train.getPlanTrainStnList(),date2,null));
	        }
	        //去掉分界站时间不满足查询时间的站
	        List<RunPlanDTO> result2 = new ArrayList<RunPlanDTO>();
	        for (RunPlanDTO runPlanDTO : resultCache) {
				if("1".equals(runPlanDTO.getIsValid())){
					result2.add(runPlanDTO);
				}
			}
	    	
	        /** 结果集处理 **/
//	        List<RunPlanDTO> jjList = new ArrayList<RunPlanDTO>();
	        List<RunPlanDTO> qjjList = new ArrayList<RunPlanDTO>();
	        List<RunPlanDTO> hjjList = new ArrayList<RunPlanDTO>();
	        boolean b1 = false;
	        boolean b2 = false;
//	    	if(result1.size() > result2.size()){
//	    		b1 = true;
//	    	}else if(result2.size() > result1.size()){
//	    		b2 = true;
//	    	}
	        // 第一天的显示结果
	    	Map<String, Object> returnMap = operationData(result1,result2,b1);
//	    	if(b1){
//	    		jjList = (List<RunPlanDTO>) returnMap.get("jj");
//	    	}
	    	qjjList = (List<RunPlanDTO>) returnMap.get("jj");
	    	List<RunPlanDTO> cjList1 = (List<RunPlanDTO>) returnMap.get("cj");
	    	// 第二天的显示结果
	    	returnMap.clear();
	    	returnMap = operationData(result2,result1,b2);
//	    	if(b2){
//	    		jjList = (List<RunPlanDTO>) returnMap.get("jj");
//	    	}
	    	hjjList = (List<RunPlanDTO>) returnMap.get("jj");
	    	List<RunPlanDTO> cjList2 = (List<RunPlanDTO>) returnMap.get("cj");
	        
	    	/** 页面 **/
	    	// sel：例如：查询 29号、30号的；那么页面需要显示的是： 1.29號展示（30号没有的数据）；2.30號（29号没有的数据）；3.2个日期都有的
	    	// 2015-4-17 15:58:58 哲哥更改需求，分为4个表格
	    	// 想不通問哲哥
	    	paramMap.put("cbPlansQ", cjList1);
	    	paramMap.put("cbPlansH", cjList2);
//	    	paramMap.put("cbPlans", jjList);
	    	paramMap.put("qjjList", qjjList);
	    	paramMap.put("hjjList", hjjList);
	    	result.setData(paramMap);
	        
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return result;
    }

    public Map<String, Object> operationData(List<RunPlanDTO> result1,List<RunPlanDTO> result2,boolean b){
    	
    	Map<String, Object> returnMap = new HashMap<String, Object>();
		 List<RunPlanDTO> resultGlb3 = new ArrayList<RunPlanDTO>();
		 List<RunPlanDTO> resultGlb4 = new ArrayList<RunPlanDTO>();
		 
		 List<RunPlanDTO> copyList = new ArrayList<RunPlanDTO>();
		 copyList.addAll(result2);
		 
		for (int i = 0; i < result1.size(); i++) {
//			System.out.println("i::" + i);
			RunPlanDTO rI = result1.get(i);
			// 如果一直有相同的，那么最后会把第二个集合全部都清空，导致第一个集合中的数据循环没有任何意义了.
			if (!copyList.isEmpty()) {
				for (int j = 0; j < copyList.size(); j++) {
					// System.out.println("j::" + j);
					RunPlanDTO rJ = copyList.get(j);
					// 交集，相同条件
//					System.out.println(rJ.getStartTime());
//					System.out.println(rJ.getStartTime().substring(5));
					if (StringUtils.equals(rJ.getStartStn(), rI.getStartStn())
							&& StringUtils.equals(rJ.getTrainNbr(),
									rI.getTrainNbr())
							&& StringUtils.equals(rJ.getEndStn(),
									rI.getEndStn())
							&& StringUtils.equals(rJ.getStartTime()
									.substring(5),
									rI.getStartTime().substring(5))
							&& StringUtils.equals(rJ.getSpareFlag(),
											rI.getSpareFlag())) {
//						if (b) {
							resultGlb3.add(rI);
//						}
						copyList.remove(j);
						break;
					} else if (j == (copyList.size() - 1)) {
						// 最后都没找到相同的数据，差集
						resultGlb4.add(rI);
					}
				}
			} else {
				resultGlb4.add(rI);
			}
		}
//		 System.out.println(resultGlb3);
//		 System.out.println(resultGlb4);
		 returnMap.put("jj", resultGlb3);
		 returnMap.put("cj", resultGlb4);
		 return returnMap;
    }
}

