package com.railway.passenger.transdispatch.comfirmedmap.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.railway.common.entity.Result;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmOriginalCross;
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmCrossService;
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmOriginalCrossService;
import com.railway.passenger.transdispatch.comfirmedmap.service.IComfirmedmapService;
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "/cmOriginalCross")
public class CmOriginalCrossController {
	
	private static Log logger = LogFactory.getLog(CmOriginalCrossController.class
			.getName());
	
	@Autowired
	private ICmOriginalCrossService originalService;
	@Autowired
	private IComfirmedmapService comfirmedService;
	@Autowired
	private ICmCrossService cmCrossService;
	
	@RequestMapping(value = "/pageIndex/{index}", method = RequestMethod.GET)
	public String pageIndex(@PathVariable("index") String index) {
		String url = "";
		switch(index){
			case "crossTrain" : url="/logTable/originalCrossTrain";break;
			case "outerCrossTrain" : url="/logTable/originalCrossTrainOut";break;
			case "manager" : url="/logTable/bureauManage";break;
			case "check" : url="/logTable/crossCheck";break;
			case "bjdd" : url="/logTable/bjdd_manage";break;
			case "wjdd" : url="/logTable/wjdd_manage";break;
			default : url = "";
		}
		return url;
	}
	
	@ResponseBody
	@RequestMapping(value = "/generalCross", method = RequestMethod.POST)
	public Result generalCross(@RequestBody Map<String, Object> map) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		Result result = new Result();
		String crossIds = String.valueOf(map.get("crossIds"));
		if(crossIds == null || crossIds.equals("")){
			result.setCode(-1);
			result.setMessage("生成交路失败,参数错误！");
			return result;
		}
		result = originalService.queryByPrimaryKeys(crossIds);
		List<TCmOriginalCross> list = result.getList();
		if(list == null || list.size() == 0){
			logger.error("generalCross未查询出原始对数信息！");
			result.setCode(-1);
			result.setMessage("生成交路失败！");
			return result;
		}
		for(int i = 0; i < list.size(); i++){
			TCmCross cmCross = comfirmedService.generateTcmCrossInfo(list.get(i));
			if(cmCross == null){
				list.remove(i);
				continue;
			}
			boolean flag = comfirmedService.generateTcmPhyCrossInfo(cmCross);
			if(!flag){
				list.remove(i);
				continue;
			}
		}
		map.put("list", list);
		map.put("user", user);
		result = originalService.markCrossCreateFlag(map);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryVersionInfo", method = RequestMethod.GET)
	public Result queryVersionInfo() {
		Result result = originalService.queryVersionInfo();
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkCross", method = RequestMethod.POST)
	public Result checkCross(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.check(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkCrossNotPass", method = RequestMethod.POST)
	public Result checkCrossNotPass(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.checkNotPass(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/batchSaveCross", method = RequestMethod.POST)
	public Result batchSaveCross(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.batchSaveCross(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/batchConfigOperationRule", method = RequestMethod.POST)
	public Result batchConfigOperationRule(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.batchConfigOperationRule(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteTrain", method = RequestMethod.POST)
	public Result deleteTrain(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.deleteTrainInfo(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryCrossTrainDetail", method = RequestMethod.POST)
	public Result queryCrossTrainDetail(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.queryCrossWithTrain(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryCrossById", method = RequestMethod.POST)
	public Result queryCrossById(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.queryByPrimaryKey(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/pageQueryCrossAndTrain", method = RequestMethod.POST)
	public Result pageQueryCrossAndTrain(HttpServletRequest request,
			HttpServletResponse response) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String cmVersionId = request.getParameter("cmVersionId");
		String pageIndex = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		String highlineFlag = request.getParameter("highlineFlag");
		String tokenVehBureau = request.getParameter("tokenVehBureau");
		String tokenFlag = request.getParameter("tokenFlag");
		String loginBureau = request.getParameter("loginBureau");
		String crossName = request.getParameter("crossName");
		String crossNamecheckflag = request.getParameter("crossNamecheckflag");
		String exceptionflag = request.getParameter("exceptionflag");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmVersionId", cmVersionId);
		map.put("pageIndex", pageIndex);
		map.put("pageSize", pageSize);
		map.put("highlineFlag", highlineFlag);
		map.put("tokenVehBureau", tokenVehBureau);
		map.put("crossName", crossName);
		map.put("user", user);
		map.put("exceptionflag", exceptionflag);
		map.put("tokenFlag", tokenFlag);
		map.put("loginBureau", loginBureau);
		map.put("crossNamecheckflag",crossNamecheckflag);
		map.put("cross_name_left", "-"+crossName);
		map.put("cross_name_mid", "-"+crossName+"-");
		map.put("cross_name_right", crossName+"-");
		Result result = originalService.pageQueryCrossAndTrain(map);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/pageQueryCross", method = RequestMethod.POST)
	public Result pageQueryCross(HttpServletRequest request,
			HttpServletResponse response) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		String cmVersionId = request.getParameter("cmVersionId");
		String pageIndex = request.getParameter("page");
		String pageSize = request.getParameter("rows");
		String highlineFlag = request.getParameter("highlineFlag");
		String tokenVehBureau = request.getParameter("tokenVehBureau");
		String crossName = request.getParameter("crossName");
		String tokenFlag = request.getParameter("tokenFlag");
		String crossNamecheckflag = request.getParameter("crossNamecheckflag");
		String exceptionflag = request.getParameter("exceptionflag");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmVersionId", cmVersionId);
		map.put("pageIndex", pageIndex);
		map.put("pageSize", pageSize);
		map.put("highlineFlag", highlineFlag);
		map.put("tokenVehBureau", tokenVehBureau);
		map.put("crossName", crossName);
		map.put("user", user);
		map.put("loginBureau", user.getBureau());
		map.put("tokenFlag", tokenFlag);
		map.put("crossNamecheckflag",crossNamecheckflag);
		map.put("cross_name_left", "-"+crossName);
		map.put("cross_name_mid", "-"+crossName+"-");
		map.put("cross_name_right", crossName+"-");
		map.put("exceptionflag", exceptionflag);
		Result result = originalService.pageQueryCross(map);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/addCrossTrain", method = RequestMethod.POST)
	public Result addCrossTrain(@RequestBody Map<String, Object> reqMap) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		reqMap.put("user", user);
		Result result = originalService.add(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateCross", method = RequestMethod.POST)
	public Result updateCross(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.updateCross(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateCrosses", method = RequestMethod.POST)
	public Result updateCross(@RequestBody Map<String, Object>[] req) {
		Result result = null;
		for(int i=0; i<req.length; i++) {
			result = originalService.updateCross(req[i]);
		}
	
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateCrossTrain", method = RequestMethod.POST)
	public Result updateCrossTrain(@RequestBody Map<String, Object> reqMap) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		reqMap.put("user", user);
		Result result = originalService.updateCrossTrain(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateTrainInfo", method = RequestMethod.POST)
	public Result updateTrainInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = originalService.updateTrainInfo(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Result delete(@RequestBody Map<String, Object> reqMap) {
		//原始对数表信息删除前，先删除其关联的其他信息(逻辑交路信息、车底交路信息、开行计划信息)
		String crossIds = String.valueOf(reqMap.get("crossIds"));
		String[] crossIdArr = crossIds.split(",");
		for(String cmOriginalCrossId : crossIdArr){
			TCmOriginalCross ocross = originalService.getTCmOriginalCross(cmOriginalCrossId);
			System.out.println("【" + comfirmedService.deleteAllInfoByTCmOriginalCross(ocross) + "】");
		}
		Result result = originalService.deleteCrossTrain(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Result upload(HttpServletRequest request,
			HttpServletResponse response) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		Result result = new Result();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String versionId = request.getParameter("versionId");
			String versionName = request.getParameter("versionName");
			String coverFlag = request.getParameter("coverFlag");
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			String xlsName;
			String[] name;
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				// 上传文件
				MultipartFile mf = entity.getValue();
				xlsName = mf.getOriginalFilename();
				if (StringUtils.isNotEmpty(xlsName)) {
					name = xlsName.split("\\.");
					if (name.length == 2) {
						if (StringUtils.equals(name[1], "xls")
								|| StringUtils.equals(name[1], "xlsx")) {
							Map<String,Object> paramMap = new HashMap<String,Object>();
							paramMap.put("inputStream", mf.getInputStream());
							paramMap.put("versionId", versionId);
							paramMap.put("versionName", versionName);
							paramMap.put("coverFlag", coverFlag);
							paramMap.put("user", user);
							result = originalService.upload(paramMap);
						} else {
							result.setCode(401);
							result.setMessage("上传失败,不是有效的EXCEL文件");
						}
					} else {
						result.setCode(401);
						result.setMessage("上传失败,文件名不正确");
					}
				} else {
					result.setCode(401);
					result.setMessage("上传失败,文件名不能为空");
				}
			}

		} catch (Exception e) {
			logger.error(e);
			result.setCode(401);
			result.setMessage("上传失败");
		}
		return result;
	}
	
}
