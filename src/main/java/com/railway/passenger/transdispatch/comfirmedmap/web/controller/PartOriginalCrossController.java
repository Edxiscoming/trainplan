package com.railway.passenger.transdispatch.comfirmedmap.web.controller;

import java.util.HashMap;
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
import com.railway.passenger.transdispatch.comfirmedmap.service.ICmPartOriginalCrossService;
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "/partOriginalCross")
public class PartOriginalCrossController {
	
	private static Log logger = LogFactory.getLog(PartOriginalCrossController.class);
	@Autowired
	private ICmPartOriginalCrossService partService;
	
	
	@RequestMapping(value = "/pageIndex/{index}", method = RequestMethod.GET)
	public String pageIndex(@PathVariable("index") String index) {
		String url = "";
		switch(index){
			case "import" : url="/logTable/importCrossData";break;
			case "manager" : url="/logTable/crossManage";break;
			case "check" : url="/logTable/crossCheck";break;
			default : url = "";
		}
		return url;
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryVersionInfo", method = RequestMethod.GET)
	public Result queryVersionInfo() {
		Result result = partService.queryVersionInfo();
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkCross", method = RequestMethod.POST)
	public Result checkCross(@RequestBody Map<String, Object> reqMap) {
		Result result = partService.check(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryCrossById", method = RequestMethod.POST)
	public Result queryCrossById(@RequestBody Map<String, Object> reqMap) {
		Result result = partService.queryByPrimaryKey(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/addCross", method = RequestMethod.POST)
	public Result addCross(@RequestBody Map<String, Object> reqMap) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		reqMap.put("createBureau", user.getBureau());
		Result result = partService.add(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateCross", method = RequestMethod.POST)
	public Result updateCross(@RequestBody Map<String, Object> reqMap) {
		Result result = partService.update(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteCross", method = RequestMethod.POST)
	public Result deleteCross(@RequestBody Map<String, Object> reqMap) {
		Result result = partService.delete(reqMap);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
	public Result getOriginalCross(HttpServletRequest request,
			HttpServletResponse response) {
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmVersionId", request.getParameter("cmVersionId"));
		map.put("pageIndex", request.getParameter("page"));
		map.put("pageSize", request.getParameter("rows"));
		map.put("highlineFlag", request.getParameter("highlineFlag"));
		map.put("spareFlag", request.getParameter("spareFlag"));
		map.put("trainNbr", request.getParameter("trainNbr"));
		map.put("createBureau", user.getBureau());
		Result result = partService.pageQuery(map);
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
							paramMap.put("user", user);
							result = partService.upload(paramMap);
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
