package org.railway.com.trainplan.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.OriginalCross;
import org.railway.com.trainplan.service.CrossService;
import org.railway.com.trainplan.service.OriginalCrossService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.PagingResult;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(value = "/originalCross")
public class OriginalCrossController {
	
	private static Log logger = LogFactory.getLog(OriginalCrossController.class
			.getName());
	
	@Autowired
	private OriginalCrossService originalCrossService;
	
	@Autowired
	private CrossService crossService;
	
	@ResponseBody
	@RequestMapping(value = "/getOriginalCross", method = RequestMethod.POST)
	public Result getOriginalCross(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String crossId = StringUtil.objToStr(reqMap.get("crossId"));
			OriginalCross cross = originalCrossService.getOriginalCrossInfoById(crossId);
			cross.setCrossId(crossId);
			result.setData(cross);
		} catch (Exception e) {
			logger.error("getOriginalCross error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteOriginalCross", method = RequestMethod.POST)
	public Result deleteOriginalCross(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String crossIds = StringUtil.objToStr(request.getParameter("crossIds"));
			if("".equals(crossIds)){
				crossIds = StringUtil.objToStr(reqMap.get("crossIds"));
			}
			if (crossIds != null) {
				String[] crossIdsArray = crossIds.split(",");
				//删除其关联信息
				originalCrossService.deleteOriginalCrossRelevant(crossIdsArray);
				//删除其本身
				originalCrossService.deleteOriginalCrossInfoForCorssIds(crossIdsArray);
			}
		} catch (Exception e) {
			logger.error("deleteCorssInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/addOriginalCross", method = RequestMethod.PUT)
	public Result addOriginalCross(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.info("updateOriginalCross~~~~reqMap==" + reqMap);
			originalCrossService.addOriginalCross(reqMap);
		} catch (Exception e) {
			logger.error("addOriginalCross error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateOriginalCross", method = RequestMethod.PUT)
	public Result updateOriginalCross(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.error("updateOriginalCross~~~~reqMap==" + reqMap);
			
			originalCrossService.updateOriginalCross(reqMap);
		} catch (Exception e) {
			logger.error("updateOriginalCross error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
	
	/**
	 * 获取原始对数表信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getOriginalCrossInfo", method = RequestMethod.POST)
	public Result getOriginalCrossInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		List<OriginalCross> list = null;
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			String all_relevant_token = StringUtil.objToStr(reqMap.get("all_relevant_token"));
			if("1".equals(all_relevant_token)){
				//1:本局担当 
				reqMap.put("tokenVehBureau", user.getBureau());
				reqMap.put("relevantBureau", null);
			} else if("2".equals(all_relevant_token)){
				//2:本局相关
				reqMap.put("tokenVehBureau", null);
				reqMap.put("relevantBureau", user.getBureau());
			} else {
				//others:全部，按照原有的的查询条件进行查询
//				reqMap.put("relevantBureau", null);
//				reqMap.put("tokenVehBureau", null);
			}

			list = originalCrossService.getOriginalCrossInfo(reqMap);
			PagingResult page = new PagingResult(
					originalCrossService.getOriginalCrossInfoCount(reqMap), list);
			result.setData(page);
		} catch (Exception e) {
			logger.error("getCrossInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}
	/**
	 * 获取原始对数表信息
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/transferCross", method = RequestMethod.POST)
	public Result transferCross(@RequestBody Map<String, Object> reqMap){
		Result result = new Result();
		try {
			originalCrossService.transferCross(reqMap);
		} catch (Exception e) {
			logger.error("transferCross error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/insertOriginalCross", method = RequestMethod.POST)
	public Result insertOriginalCross(HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			String chartId = request.getParameter("chartId");
			String chartName = request.getParameter("chartName");
			String startDay = request.getParameter("startDay");
			String addFlag = request.getParameter("addFlag");
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
							originalCrossService.actionExcel(mf.getInputStream(),
									chartId, startDay, chartName, addFlag);
						} else {
							result.setCode("401");
							result.setMessage("上传失败,不是有效的EXCEL文件");
						}
					} else {
						result.setCode("401");
						result.setMessage("上传失败,文件名不正确");
					}
				} else {
					result.setCode("401");
					result.setMessage("上传失败,文件名不能为空");
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			result.setCode("401");
			result.setMessage("上传失败");
		}
		return result;
	}
	
	/**
	 * 校验单条原始对数表信息
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyOriginalCross/{id}", method = RequestMethod.GET)
	public Result verifyOriginalCross(@PathVariable("id") String id) {
		Result result = new Result();
		result.setData(originalCrossService.verifyOriginalCross(id));
		return result;
	}
	
	/**
	 * 批量校验原始对数表信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyOriginalCross", method = RequestMethod.POST)
	public Result verifyOriginalCross(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		String ids = StringUtil.objToStr(request.getParameter("crossIds"));
		if("".equals(ids)){
			ids = StringUtil.objToStr(reqMap.get("crossIds"));
		}
		Map<String, Object> map = new HashMap<String, Object>(); 
		int trueNum = 0;
		int falseNum = 0;
		for(String id : ids.split(",")){
			if(id != null && !"".equals(id.trim())){
				if(originalCrossService.verifyOriginalCross(id)){
					map.put(id, true);
					trueNum++;
				} else {
					map.put(id, false);
					falseNum++;
				}
			}
		}
		map.put("trueNum", trueNum);
		map.put("falseNum", falseNum);
		result.setData(map);
		return result;
	}
	
	/**
	 * 审批原始对数表信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkOriginalCross", method = RequestMethod.POST)
	public Result checkOriginalCross(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try{
			String ids = StringUtil.objToStr(request.getParameter("crossIds"));
			if("".equals(ids)){
				ids = StringUtil.objToStr(reqMap.get("crossIds"));
			}
			originalCrossService.checkBaseCrossInfo(ids.split(","));
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
}
