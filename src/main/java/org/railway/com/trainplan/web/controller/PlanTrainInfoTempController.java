package org.railway.com.trainplan.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mor.railway.cmd.adapter.model.CmdInfoModel;
import mor.railway.cmd.adapter.util.ConstantUtil;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CmdTrain;
import org.railway.com.trainplan.entity.PlanTrainInFoTemp;
import org.railway.com.trainplan.service.PlanTrainInfoTempService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *存生成运行线时临时数据
 * 
 * @author Think
 * 
 */
@Controller
@RequestMapping(value = "/planTrainInfoTemp")
public class PlanTrainInfoTempController {

	private static Log logger = LogFactory.getLog(PlanTrainInfoTempController.class
			.getName());
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private PlanTrainInfoTempService planTrainInfoTempService;

	/**
	 * 保存存生成运行线时临时数据
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savePlanTrainInfoTemp", method = RequestMethod.POST)
	public Result savePlanTrainInfoTemp(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("saveWdCmdTrain~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String trainStr = (String) reqObj.get("cmdTrainMap");
			JSONObject trainMap = JSONObject.fromObject(trainStr);
			 PlanTrainInFoTemp planTrainInFoTemp = new  PlanTrainInFoTemp();
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureuaShortName = user.getBureauShortName();
			logger.debug("bureuaShortName==" + bureuaShortName);

			planTrainInfoTempService.insertPlanTrainInFoTemp(planTrainInFoTemp);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}


	/**
	 * 删除存生成运行线时临时数据
	 * 
	 * @param reqStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePlanTrainInfoTemp", method = RequestMethod.DELETE)
	public Result deletePlanTrainInfoTemp(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("deletePlanTrainInfoTemp~~reqStr==" + reqStr);

		JSONObject reqObj = JSONObject.fromObject(reqStr);

		try {
			List<String> ids = null;
			ids = reqObj.getJSONArray("ids");
			//
			if (!ids.isEmpty()) {
				for (String id : ids) {
					planTrainInfoTempService.deletePlanTrainInFoTemp(id);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}
	
	/**
	 * 根据requestid删除存生成运行线时临时数据
	 * 
	 * @param reqStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePlanTrainInfoTempByRequestId", method = RequestMethod.DELETE)
	public Result deletePlanTrainInfoTempByRequestId(@RequestBody String requestid) {
		Result result = new Result();
		logger.info("deletePlanTrainInfoTempByRequestId~~requestid==" + requestid);


		try {
			if (requestid!=null) {
					planTrainInfoTempService.deletePlanTrainInfoTempByRequestId(requestid);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}
	
	/**
	 * 删除存生成运行线时临时数据
	 * 删除date之前的所以数据
	 * @param reqStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePlanTrainInfoTempByDate", method = RequestMethod.DELETE)
	public Result deletePlanTrainInfoTempByDate(@RequestBody String date) {
		Result result = new Result();
		logger.info("deletePlanTrainInfoTemp~~reqStr==" + date);


		try {
			if(date!=null){
				planTrainInfoTempService.deletePlanTrainInFoTempByDate(date);
			}
					

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 编辑存生成运行线时临时数据
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editPlanTrainInfoTemp", method = RequestMethod.PUT)
	public Result editPlanTrainInfoTemp(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("editPlanTrainInfoTemp~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String trainStr = (String) reqObj.get("cmdTrainMap");
			JSONObject trainMap = JSONObject.fromObject(trainStr);
			PlanTrainInFoTemp planTrainInFoTemp = null;
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureuaShortName = user.getBureauShortName();
			logger.debug("bureuaShortName==" + bureuaShortName);

			planTrainInfoTempService.updateCmdTrainForCmdTraindId(planTrainInFoTemp);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}
	
	/**
	 * 查询本局的PlanTrainInfoTemp信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPlanTrainInfoTemp", method = RequestMethod.POST)
	public Result getPlanTrainInfoTemp(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		logger.info("getCmdTrainInfo~~reqMap==" + reqMap);
		String startDate = StringUtil.objToStr(reqMap.get("startDate"));
		String endDate = StringUtil.objToStr(reqMap.get("endDate"));

		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		// 本局局码
		String bureuaCode = user.getBureau();
		logger.debug("bureuaCode==" + bureuaCode);

		List<PlanTrainInFoTemp> returnList = new ArrayList<PlanTrainInFoTemp>();
		try {
			PlanTrainInFoTemp planTrainInFoTemp = new PlanTrainInFoTemp();

				returnList = planTrainInfoTempService.getPlanTrainInFoTemp(planTrainInFoTemp);
				logger.info("文电命令:" + returnList.size());
			result.setData(returnList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}
	
	
}
