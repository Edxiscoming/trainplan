package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import mor.railway.cmd.adapter.service.ICmdAdapterService;
import mor.railway.cmd.adapter.service.impl.CmdAdapterServiceImpl;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.SortUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.DicRelaCrossPost;
import org.railway.com.trainplan.entity.DicThroughLine;
import org.railway.com.trainplan.entity.HighLineCrossTrainInfo;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.HighlineCrossTrainBaseInfo;
import org.railway.com.trainplan.entity.HighlineThroughlineInfo;
import org.railway.com.trainplan.entity.HighlineTrainRunLine;
import org.railway.com.trainplan.service.CommonService;
import org.railway.com.trainplan.service.HighLineService;
import org.railway.com.trainplan.service.MessageService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.service.dto.OptionDto;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/highLine")
public class HighlineController {
	private static Log logger = LogFactory.getLog(HighlineController.class
			.getName());

	@Autowired
	private HighLineService highLineService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private MessageService messageService;

	@RequestMapping(method = RequestMethod.GET)
	public String highLine() {
		return "highLine/highLine_cross";
	}

	@RequestMapping(value = "/vehicle", method = RequestMethod.GET)
	public String highLineVehicle() {
		return "highLine/highLine_cross_vehicle";
	}

	@RequestMapping(value = "/vehicleCheck", method = RequestMethod.GET)
	public String highLineVehicleCheck() {
		return "highLine/highLine_cross_vehicleCheck";
	}

	@RequestMapping(value = "/vehicleSearch", method = RequestMethod.GET)
	public String highLineVehicleSearch() {
		return "highLine/highLine_cross_vehicleSearch";
	}

	@ResponseBody
	@RequestMapping(value = "/getVehicles", method = RequestMethod.GET)
	public Result getVehicles() {
		Result result = new Result();
		try {
			List<OptionDto> list = highLineService.getVehicles();
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getVehicles error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getDepots/{roleType}", method = RequestMethod.GET)
	public Result getDepots(@PathVariable String roleType) {
		Result result = new Result();
		try {
			if ("ALL".equals(roleType.toUpperCase())) {
				List<OptionDto> list = commonService.getDepots(null);
				result.setData(list);
				return result;
			}

			// 仅查询本局的铁路线
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			List<OptionDto> list = commonService.getDepots(user.getBureau());
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getDepots error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getCrhTypes", method = RequestMethod.GET)
	public Result getCrhTypes() {
		Result result = new Result();
		try {
			List<OptionDto> list = commonService.getCrhTypes();
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getCrhTypes error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getThroughLines/{roleType}", method = RequestMethod.GET)
	public Result getThroughLines(@PathVariable String roleType) {
		Result result = new Result();
		try {
			if ("ALL".equals(roleType.toUpperCase())) {
				List<OptionDto> list = commonService.getThroughLines(null);
				result.setData(list);
				return result;
			}

			// 仅查询本局的铁路线
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			List<OptionDto> list = commonService.getThroughLines(user
					.getBureau());
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getThroughLines error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getThroughLineName", method = RequestMethod.GET)
	public Result getThroughLineName() {
		Result result = new Result();
		try {
			// 仅查询本局的铁路线
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			List<DicThroughLine> list = commonService.getThroughLineName(user
					.getBureau());
			result.setData(list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getThroughLines error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 分离字符串
	 */
	public static List<String> getSplitedString(String string) {
		List<String> sList = new ArrayList<String>();
		if (string != null && !string.trim().equals("")) {
			int length = string.length();
			int i = 0;
			while (i < length) {
				String s = string.substring(i, i + 1);
				i++;
				sList.add(s);
			}
		}

		return sList;
	}

	/**
	 * 初始化线台
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/initDicRelaCrossPost", method = RequestMethod.POST)
	public Result initDicRelaCrossPost(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("initDicRelaCrossPost~~reqStr==" + reqStr);
		try {
//			JSONObject reqObj = JSONObject.fromObject(reqStr);

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureau = user.getBureau();
			if (StringUtils.isEmpty(bureau)) {
				result.setCode(StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				result.setMessage(StaticCodeType.SYSTEM_PARAM_LOST
						.getDescription());
				return result;
			}
			commonService.deleteDicrelacrosspostByBureau(bureau);
			List<DicRelaCrossPost> resultList = commonService
					.initDicRelaCrossPost(bureau);
			List<OptionDto> list = commonService.getAccs(user.getBureau());// 动车台
			if (resultList != null && resultList.size() > 0) {
				for (DicRelaCrossPost rela : resultList) {
					// List<String> bureauList =
					// this.getSplitedString(rela.getBureau());
					// if(bureauList != null && bureauList.size()>0 ){
					// for(String str :bureauList){
					DicRelaCrossPost dicRelaCrossPost = new DicRelaCrossPost();
					dicRelaCrossPost.setLoaddateOffset(0);
					dicRelaCrossPost.setBureau(bureau);
					dicRelaCrossPost.setCrossName(rela.getCrossName());
					dicRelaCrossPost
							.setDepotName(rela.getDepotName() == null ? ""
									: rela.getDepotName());
					dicRelaCrossPost.setTokenVehBureau(rela.getTokenVehBureau());
					if (list.size() > 0) {
						dicRelaCrossPost.setPostName(list.get(0).getName());
						dicRelaCrossPost.setPost_id(list.get(0).getCode());
					}
					commonService.insertDisRelaCrossPost(dicRelaCrossPost);
					// }
					// }

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
	 * 保存线台
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveDicRelaCrossPost", method = RequestMethod.POST)
	public Result saveDicRelaCrossPost(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("saveWdCmdTrain~~reqStr==" + reqStr);
		try {
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String crossName = (String) reqObj.get("crossName");// 交路名
			String tokenVehDepot = (String) reqObj.get("tokenVehDepot");// 动车所
			String postName = (String) reqObj.get("postName");// 动车台
			String note = (String) reqObj.get("note");// 备注
			String throughLine = (String) reqObj.get("throughLine");// 铁路线
			String post_id = (String) reqObj.get("post_id");
			String tqts = (String) reqObj.get("tqts");
//String ddjName =(String)reqObj.get("ddjName");
String ddjCode = (String)reqObj.get("ddjCode");
			
			DicRelaCrossPost dicRelaCrossPost = new DicRelaCrossPost();
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureau = user.getBureau();
			dicRelaCrossPost.setBureau(bureau);
			dicRelaCrossPost.setCrossName(crossName);
			dicRelaCrossPost.setNote(note);
			dicRelaCrossPost.setThroughLine(throughLine);
			dicRelaCrossPost.setDepotName(tokenVehDepot);
			dicRelaCrossPost.setPostName(postName);
			dicRelaCrossPost.setPost_id(post_id);
			dicRelaCrossPost.setTokenVehBureau(ddjCode);
			if (!StringUtils.isEmpty(tqts)) {
				dicRelaCrossPost.setLoaddateOffset(Integer.parseInt(tqts));
			} else {
				dicRelaCrossPost.setLoaddateOffset(0);
			}

			commonService.insertDisRelaCrossPost(dicRelaCrossPost);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 保存铁路线
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveDicThroughLine", method = RequestMethod.POST)
	public Result saveDicThroughLine(@RequestBody String reqStr) {
		Result result = new Result();
		List<DicThroughLine> count = new ArrayList<DicThroughLine>();
		logger.info("saveDicThroughLine~~reqStr==" + reqStr);
		try {
			DicThroughLine dicThroughLine = new DicThroughLine();
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			// 局简称
			String bureau = user.getBureau();
			JSONObject reqObj = JSONObject.fromObject(reqStr);
			String throughLineName = (String) reqObj.get("throughLineName");// 铁路线
			if (!StringUtil.isEmpty(throughLineName)) {
				count = commonService.isExistDicThroughLine(throughLineName,
						bureau);
			}
			if (count.size() > 0) {
				result.setCode("100");
				result.setMessage("铁路线已经存在");
				return result;
			}

			String throughLineLineId = UUID.randomUUID().toString();

			dicThroughLine.setBureau(bureau);
			dicThroughLine.setThroughLineLineId(throughLineLineId);
			dicThroughLine.setThroughLineName(throughLineName);
			dicThroughLine.setHighline_flag("1");// 暂时写死
			commonService.insertDicThroughLine(dicThroughLine);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 删除线台字典
	 * 
	 * @param reqStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteDicRelaCrossPost", method = RequestMethod.POST)
	public Result deleteDicRelaCrossPost(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("deleteDicRelaCrossPost~~reqStr==" + reqStr);

		JSONObject reqObj = JSONObject.fromObject(reqStr);

		try {
			List<Integer> dicIds = null;
			dicIds = reqObj.getJSONArray("dicIds");
			//
			if (!dicIds.isEmpty()) {
				for (Integer dicId : dicIds) {
					commonService.deleteDicRelaCrossPost(dicId);
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
	 * 删除铁路线
	 * 
	 * @param reqStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteDicThroughLine", method = RequestMethod.POST)
	public Result deleteDicThroughLine(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("deleteDicThroughLine~~reqStr==" + reqStr);

		JSONObject reqObj = JSONObject.fromObject(reqStr);

		try {
			List<String> dicThrougLineIds = null;
			dicThrougLineIds = reqObj.getJSONArray("dicThrougLineIds");
			//
			if (!dicThrougLineIds.isEmpty()) {
				for (String dicId : dicThrougLineIds) {
					commonService.deleteDicThroughLine(dicId);
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
	 * 设置线台字典
	 * 
	 * @param reqStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/setDicRelaCrossPost", method = RequestMethod.POST)
	public Result setDicRelaCrossPost(@RequestBody String reqStr) {
		Result result = new Result();
		logger.info("setDicRelaCrossPost~~reqStr==" + reqStr);

		JSONObject reqObj = JSONObject.fromObject(reqStr);
		Map<String, Object> params = Maps.newHashMap();
		try {
//			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
			// 局简称
//			String bureau = user.getBureau();

			List<Integer> dicIds = null;
			dicIds = reqObj.getJSONArray("dicIds");
			params.put("ids", dicIds);
			List<DicRelaCrossPost> dicList = new ArrayList<DicRelaCrossPost>();
			dicList = commonService.getDicRelaCrossPostList(params);

//			String trainNbr = (String) reqObj.get("trainNbr");// 车次
			String throughLine = (String) reqObj.get("throughLine");// 铁路线
			String tokenVehDepot = (String) reqObj.get("tokenVehDepot");// 动车所
			String postName = (String) reqObj.get("postName");// 动车台
			String post_id = (String) reqObj.get("post_id");// 动车台
			String loaddateOffset = (String) reqObj.get("loaddateOffset");// 提前天数

			if (dicList != null && dicList.size() > 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < dicList.size(); i++) {
					// DicRelaCrossPost dic = dicList.get(i);
					// dic.setBureau(bureau);
					// dic.setCrossName(trainNbr);
					map.put("id", dicList.get(i).getId());
					// 2015-4-21 15:28:30
//					if (!StringUtils.isEmpty(throughLine)) {
//						// dic.setThroughLine(throughLine);
//						map.put("through_line", throughLine);
//					}
//					if (!StringUtils.isEmpty(post_id)) {
//						// dic.setPost_id(post_id);
//						map.put("post_id", post_id);
//					}
//					if (!StringUtils.isEmpty(postName)) {
//						// dic.setPostName(postName);
//						map.put("post_name", postName);
//					}
//					if (!StringUtils.isEmpty(tokenVehDepot)) {
//						// dic.setDepotName(tokenVehDepot);
//						map.put("depot_name", tokenVehDepot);
//					}
//					if (!StringUtils.isEmpty(loaddateOffset)) {
//						// dic.setDepotName(tokenVehDepot);
//						map.put("loaddate_offset", loaddateOffset);
//					}

					// commonService.updateRelaCrossPost(dic);
						map.put("through_line", throughLine != null ? throughLine : "");
						map.put("post_id", post_id != null ? post_id : "");
						map.put("post_name", postName != null ? postName : "");
						map.put("depot_name", tokenVehDepot != null ? tokenVehDepot : "");
						if(StringUtils.isEmpty(loaddateOffset)){
							map.put("loaddate_offset", Integer.parseInt("0"));
						}else{
							map.put("loaddate_offset", loaddateOffset);
						}
					commonService.updateRelaCrossPostByMap(map);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getAccs/{roleType}", method = RequestMethod.GET)
	public Result getAccs(@PathVariable String roleType) {
		Result result = new Result();
		try {
			if ("ALL".equals(roleType.toUpperCase())) {
				List<OptionDto> list = commonService.getAccs(null);
				result.setData(list);
				return result;
			}

			// 仅查询本局的动车台
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			List<OptionDto> list = commonService.getAccs(user.getBureau());
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getAccs error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 加载交路信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createHighLineCross", method = RequestMethod.POST)
	public Result createHighLineCross(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String startDate = StringUtil.objToStr(reqMap.get("startDate"));
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			String bureauName = user.getBureauShortName();
			String bureauCode = user.getBureau();
			List<HighlineCrossInfo> lisetResult = new ArrayList<HighlineCrossInfo>();
			logger.info("HighlineController-createHighLineCross(514H)进入接口方法");
			lisetResult = highLineService.createHighLineCross(bureauName,
					bureauCode, startDate);
			logger.info("HighlineController-createHighLineCross(514H)退出接口方法");

			result.setData(lisetResult);

		} catch (Exception e) {
			logger.error("createHighLineCross error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 查询highlineCross信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHighlineCrossList/{roleType}", method = RequestMethod.POST)
	public Result getHighlineCrossList(@RequestBody Map<String, Object> reqMap,
			@PathVariable String roleType) {
		Result result = new Result();
		try {
			/**
			 * roleType取值： CROSS_CHECK 高铁交路计划审核 VEHICLE_SUB 高铁车底计划审核
			 * VEHICLE_CHECK 高铁车底计划报告 ALL 高铁交路/计划查询
			 */
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			if ("CROSS_CHECK".equals(roleType)) {
				reqMap.put("crossBureau", user.getBureau());// 仅查询本局
				List<HighlineCrossInfo> list = highLineService
						.getHighlineCrossList(reqMap);
				List<HighlineCrossInfo> returnList = new ArrayList<HighlineCrossInfo>();
				if (list != null && list.size() > 0) {
					for (HighlineCrossInfo crossInfo : list) {
						HighlineCrossInfo temp = new HighlineCrossInfo();
						Integer vehicleCheckType = crossInfo
								.getVehicleCheckType();
						BeanUtils.copyProperties(temp, crossInfo);
						// 车底计划审核状态（0:未审核1:审核），已经审核的才能显示车底1和车底2
						if (vehicleCheckType != 1) {
							temp.setVehicle1("");
							temp.setVehicle2("");
						}
						returnList.add(temp);
					}
					result.setData(returnList);
				}
			} else if ("VEHICLE_SUB".equals(roleType)) {// 高铁车底计划报告
				reqMap.put("crossBureau", user.getBureau());// 仅查询本局
				reqMap.put("crossCheckType", 1);
				reqMap.put("userDeptName", user.getDeptName());// 用于填报权限控制
				List<HighlineCrossInfo> list = highLineService
						.getHighlineCrossList(reqMap);
				result.setData(list);
			} else if ("VEHICLE_CHECK".equals(roleType)) {// 高铁车底计划审核
				reqMap.put("crossBureau", user.getBureau());// 仅查询本局
				reqMap.put("crossCheckType", 1);
				reqMap.put("vehicleSubType", 1);
				reqMap.put("userPostId", user.getPostId());// 用于填报权限控制
				List<HighlineCrossInfo> list = highLineService
						.getHighlineCrossList(reqMap);
				result.setData(list);
			} else if ("ALL".equals(roleType)) {
				reqMap.put("crossCheckType", 1);

				List<HighlineCrossInfo> list = highLineService
						.getHighlineCrossList(reqMap);
				List<HighlineCrossInfo> returnList = new ArrayList<HighlineCrossInfo>();
				if (list != null && list.size() > 0) {
					for (HighlineCrossInfo crossInfo : list) {
						HighlineCrossInfo temp = new HighlineCrossInfo();
						Integer vehicleCheckType = crossInfo
								.getVehicleCheckType();
						BeanUtils.copyProperties(temp, crossInfo);
						// 车底计划审核状态（0:未审核1:审核），已经审核的才能显示车底1和车底2
						if (vehicleCheckType != 1) {
							temp.setVehicle1("");
							temp.setVehicle2("");
						}
						returnList.add(temp);
					}
					SortUtil.sortHighlineCrossAsCrossNameAndStarteDateInfo(returnList);
					result.setData(returnList);
				}
			}

		} catch (Exception e) {
			logger.error("getHighlineCrossList error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/*
	 * 查询DisRelaCrossPost信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDisRelaCrossPostList", method = RequestMethod.POST)
	public Result getDisRelaCrossPostList(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			reqMap.put("bureau", user.getBureau());// 仅查询本局
			List<DicRelaCrossPost> list = highLineService
					.getDisRelaCrossPostList(reqMap);
			result.setData(list);
		} catch (Exception e) {
			logger.error("getDisRelaCrossPostList error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/*
	 * 查询DIC_THROUGH_LINE信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDicThroughLine", method = RequestMethod.POST)
	public Result getDicThroughLine(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			reqMap.put("bureau", user.getBureau());// 仅查询本局
			List<DicThroughLine> list = highLineService
					.getDicThroughLine(reqMap);
			result.setData(list);
		} catch (Exception e) {
			logger.error("getDicThroughLine error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 查询highlineCrossTrain信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHighlineCrossTrainList", method = RequestMethod.POST)
	public Result getHighlineCrossTrainList(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String highlineCrossId = StringUtil.objToStr(reqMap
					.get("highlineCrossId"));
			List<HighLineCrossTrainInfo> list = highLineService
					.getHighlineCrossTrainList(highlineCrossId);
			result.setData(list);
		} catch (Exception e) {
			logger.error("getHighlineCrossTrainList error==" + e.getMessage(),
					e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * * 通过highlineCrossId查询 交路下所有列车的始发站，终到站，始发时间和终到时间
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHighlineCrossTrainBaseInfoList", method = RequestMethod.POST)
	public Result getHighlineCrossTrainBaseInfoList(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String highlineCrossId = StringUtil.objToStr(reqMap
					.get("highlineCrossId"));
			logger.debug("highlineCrossId==" + highlineCrossId);
			List<HighlineCrossTrainBaseInfo> list = highLineService
					.getHighlineCrossTrainBaseInfoList(highlineCrossId);
			result.setData(list);
		} catch (Exception e) {
			logger.error(
					"getHighlineCrossTrainBaseInfoList error=="
							+ e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 通过highlineCrossId查询该交路下的列车经由站信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHighlineTrainTimeForHighlineCrossId", method = RequestMethod.POST)
	public Result getHighlineTrainTimeForHighlineCrossId(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			String highlineCrossId = StringUtil.objToStr(reqMap
					.get("highlineCrossId"));
			logger.debug("highlineCrossId==" + highlineCrossId);
			List<HighlineTrainRunLine> list = highLineService
					.getHighlineTrainTimeForHighlineCrossId(highlineCrossId);
			result.setData(list);
		} catch (Exception e) {
			logger.error(
					"getHighlineTrainTimeForHighlineCrossId error=="
							+ e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 更新车底编号
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateHighLineVehicle", method = RequestMethod.POST)
	public Result updateHighLineVehicle(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			List<Map> highLineCrossesList = (List<Map>) reqMap
					.get("highLineCrosses");
			if (highLineCrossesList != null && highLineCrossesList.size() > 0) {
				for (Map map : highLineCrossesList) {
					logger.debug("map==" + map);
					String highlineCrossId = StringUtil.objToStr(map
							.get("highlineCrossId"));
					String vehicle1 = StringUtil.objToStr(map.get("vehicle1"));
					String vehicle2 = StringUtil.objToStr(map.get("vehicle2"));
					int count = highLineService.updateHighLineVehicle(
							highlineCrossId, vehicle1, vehicle2);
					logger.debug("updateHighLineVehicle~~count==" + count);
				}
			}

		} catch (Exception e) {
			logger.error("updateHighLineVehicle error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 保存重组交路信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveHighlineCrossAndTrainInfo", method = RequestMethod.POST)
	public Result saveHighlineCrossAndTrainInfo(
			@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		/**
		 * {highLineCrossIds:",", newCrosses:[{"", "", "", trains:[{}]}]}
		 */
		try {

			String datePattern = "yyyy-MM-dd";
			String highLineCrossIds = StringUtil.objToStr(reqMap
					.get("highLineCrossIds"));
			List<Map> newCrosses = (List<Map>) reqMap.get("newCrosses");
			if (newCrosses != null && newCrosses.size() > 0) {
				ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
						.getSubject().getPrincipal();
				String crossBureau = user.getBureau();
				List<HighlineCrossInfo> hList = new ArrayList<HighlineCrossInfo>();
				for (Map crossMap : newCrosses) {
					HighlineCrossInfo highlineCrossInfo = new HighlineCrossInfo();
					String highLineCrossId = UUID.randomUUID().toString();
					String planCrossId = StringUtil.objToStr(crossMap
							.get("planCrossId"));
					String crossName = StringUtil.objToStr(crossMap
							.get("crossName"));
					String crossDate = StringUtil.objToStr(crossMap
							.get("crossDate"));
					String crossStartDate = StringUtil.objToStr(crossMap
							.get("crossStartDate"));
					String crossEndDate = StringUtil.objToStr(crossMap
							.get("crossEndDate"));
					String throughLine = StringUtil.objToStr(crossMap
							.get("throughLine"));
					String startStn = StringUtil.objToStr(crossMap
							.get("crossStartStn"));
					String endStn = StringUtil.objToStr(crossMap
							.get("crossEndStn"));
					String spareFlag = StringUtil.objToStr(crossMap
							.get("spareFlag"));
					String relevantBureau = StringUtil.objToStr(crossMap
							.get("relevantBureau"));
					String tokenVehBureau = StringUtil.objToStr(crossMap
							.get("tokenVehBureau"));
					String tokenVehDept = StringUtil.objToStr(crossMap
							.get("tokenVehDept"));
					String tokenVehDepot = StringUtil.objToStr(crossMap
							.get("tokenVehDepot"));
					String tokenPsgBureau = StringUtil.objToStr(crossMap
							.get("tokenPsgBureau"));
					String tokenPsgDept = StringUtil.objToStr(crossMap
							.get("tokenPsgDept"));
					String crhType = StringUtil.objToStr(crossMap
							.get("crhType"));
					String note = StringUtil.objToStr(crossMap.get("note"));
					String createReason = StringUtil.objToStr(crossMap
							.get("createReason"));
					System.err.println("createReason : "
							+ crossMap.get("createReason"));
					// String createPeople =
					// StringUtil.objToStr(crossMap.get("createPeople"));
					String postName = StringUtil.objToStr(crossMap
							.get("postName"));
					String postId = StringUtil.objToStr(crossMap.get("postId"));
					// set值
					highlineCrossInfo.setThroughLine(throughLine);
					highlineCrossInfo.setCrossDate(crossDate);
					highlineCrossInfo.setCrossBureau(crossBureau);
					highlineCrossInfo.setHighLineCrossId(highLineCrossId);
					highlineCrossInfo.setPlanCrossId(planCrossId == null ? ""
							: planCrossId);
					highlineCrossInfo.setCrossName(crossName == null ? ""
							: crossName);
					// String formateType = "yyyy-MM-dd hh:mm:ss";
					highlineCrossInfo
							.setCrossStartDate(crossStartDate == null ? ""
									: DateUtil.format(DateUtil.parseDate(
											crossStartDate, datePattern),
											"yyyyMMdd"));
					highlineCrossInfo.setCrossEndDate(crossEndDate == null ? ""
							: DateUtil.format(DateUtil.parseDate(crossEndDate,
									datePattern), "yyyyMMdd"));
					highlineCrossInfo.setStartStn(startStn == null ? ""
							: startStn);
					highlineCrossInfo.setCrossStartStn(startStn == null ? ""
							: startStn);
					highlineCrossInfo.setCrossEndStn(endStn == null ? ""
							: endStn);
					highlineCrossInfo.setEndStn(endStn == null ? "" : endStn);
					highlineCrossInfo.setSpareFlag(spareFlag == null ? ""
							: spareFlag);
					highlineCrossInfo.setPostId(postId == null ? "" : postId);
					highlineCrossInfo.setPostName(postName == null ? ""
							: postName);
					highlineCrossInfo
							.setRelevantBureau(relevantBureau == null ? ""
									: relevantBureau);
					highlineCrossInfo
							.setTokenVehBureau(tokenVehBureau == null ? ""
									: tokenVehBureau);
					highlineCrossInfo.setTokenVehDept(tokenVehDept == null ? ""
							: tokenVehDept);
					highlineCrossInfo
							.setTokenVehDepot(tokenVehDepot == null ? ""
									: tokenVehDepot);
					highlineCrossInfo
							.setTokenPsgBureau(tokenPsgBureau == null ? ""
									: tokenPsgBureau);
					highlineCrossInfo.setTokenPsgDept(tokenPsgDept == null ? ""
							: tokenPsgDept);
					highlineCrossInfo
							.setCrhType(crhType == null ? "" : crhType);
					highlineCrossInfo.setCreateReason(createReason == null ? ""
							: createReason);
					highlineCrossInfo.setNote(note == null ? "" : note);

					highlineCrossInfo.setCreatPeople(user.getName());
					highlineCrossInfo.setCrossBureau(user.getBureau());
					highlineCrossInfo.setCreatPeopleOrg(user.getPostName());// 创建人单位
																			// 实际须填入用户岗位名称

					// hList.add(highlineCrossInfo);//先主 后子
					highLineService.addHighlineCrossSingle(highlineCrossInfo);
					// 保存到表highline_cross
					List<Map> trainsList = (List<Map>) crossMap.get("trains");

					if (trainsList != null && trainsList.size() > 0) {
						for (Map trainMap : trainsList) {// 修改列车映射到新建的交路上
							highLineService.updateHighLineTrain(StringUtil
									.objToStr(trainMap.get("highLineTrainId")),
									highlineCrossInfo.getHighLineCrossId());
							// HighLineCrossTrainInfo crossTrain = new
							// HighLineCrossTrainInfo();
							// String highLineTrainId =
							// UUID.randomUUID().toString();
							// String planTrainId =
							// StringUtil.objToStr(trainMap.get("planTrainId"));
							// int trainSort =
							// Integer.valueOf(StringUtil.objToStr(trainMap.get("trainSort")));
							// String trainNbr =
							// StringUtil.objToStr(trainMap.get("trainNbr"));
							// String runDate =
							// StringUtil.objToStr(trainMap.get("runDate"));
							//
							// crossTrain.setPlanTrainId(planTrainId ==
							// null?"":planTrainId);
							// crossTrain.setRunDate(runDate ==
							// null?"":runDate);
							// crossTrain.setHighLineTrainId(highLineTrainId);
							// crossTrain.setTrainNbr(trainNbr ==
							// null?"":trainNbr);
							// crossTrain.setTrainSort(trainSort);
							// crossTrain.setHighLineCrossId(highlineCrossInfo.getHighLineCrossId());
							// tList.add(crossTrain);
						}
						// 保存数据到highline_cross_train中
					}

				}
				// highLineService.batchAddHighlineCross(hList);
			}
			// 删除表highline_cross表中数据
			highLineService
					.deleteHighlienCrossForHighlineCrossId(highLineCrossIds);

		} catch (Exception e) {
			logger.error("saveHighlineCrossAndTrainInfo error=="
					+ e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 更新highlinecross中check的基本信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateHiglineCheckInfo", method = RequestMethod.POST)
	public Result updateHiglineCheckInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.debug("updateHiglineCheckInfo~~~reqMap==" + reqMap);
			String highlineCrossIds = StringUtil.objToStr(reqMap
					.get("highlineCrossIds"));
			// check_type:0_未审核 1_已审核
			String checkType = "1";
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			int count = highLineService.updateHiglineCheckInfo(checkType,
					user.getName(), user.getDeptName(), highlineCrossIds);
			logger.debug("updateHiglineCheckInfo~~count==" + count);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateHiglineCheckInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	@RequestMapping(value = "/lineDictionary", method = RequestMethod.GET)
	public ModelAndView lineDictionary(HttpServletRequest request) {
		return new ModelAndView("highLine/highLine_dictionary");
	}

	@RequestMapping(value = "/line_add_dictionary", method = RequestMethod.GET)
	public ModelAndView line_add_dictionary(HttpServletRequest request) {
		return new ModelAndView("highLine/highline_add_dictionary");
	}

	@RequestMapping(value = "/addDicRelaCrossPost", method = RequestMethod.GET)
	public ModelAndView addDicRelaCrossPost(HttpServletRequest request) {
		return new ModelAndView("highLine/addDicRelaCrossPost");
	}

	@RequestMapping(value = "/dicThroughLine", method = RequestMethod.GET)
	public ModelAndView dicThroughLine(HttpServletRequest request) {
		return new ModelAndView("highLine/addDicThroughLine");
	}

	/**
	 * 通过startDate和crossBureau删除数据
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cleanHighLineForDate", method = RequestMethod.POST)
	public Result cleanHighLineForDate(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			logger.debug("cleanHighLineForDate~~~reqMap==" + reqMap);
			// 交路开始时间，格式yyyyMMdd
			String startDate = StringUtil.objToStr(reqMap.get("startDate"));
			// 获取登录用户信息
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			HighlineCrossInfo crossInfo = new HighlineCrossInfo();
			crossInfo.setCrossDate(startDate);
			crossInfo.setCrossBureau(user.getBureau());
			// 获取数据
			List<HighlineCrossInfo> list = highLineService
					.getHighlineCrossInfo(crossInfo);
			StringBuffer highlineCrossIdBf = new StringBuffer();
			if (list != null && list.size() > 0) {
				int size = list.size();
				for (int i = 0; i < size; i++) {
					HighlineCrossInfo tempCrossInfo = list.get(i);
					String highlineCrossId = tempCrossInfo.getHighLineCrossId();
					if (i != size - 1) {
						highlineCrossIdBf.append("'").append(highlineCrossId)
								.append("',");
					} else {
						highlineCrossIdBf.append("'").append(highlineCrossId)
								.append("'");
					}
				}

				// 先删除子表HIGHLINE_CROSS_TRAIN的数据
				highLineService
						.deleteHighlienCrossTrainForHighlineCrossId(highlineCrossIdBf
								.toString());
				// 再删除父表HIGHLINE_CROSS的数据
				highLineService
						.deleteHighlienCrossForHighlineCrossId(highlineCrossIdBf
								.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("cleanHighLineForDate error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 通过highlineCrossIds删除数据
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteHighLineForIds", method = RequestMethod.POST)
	public Result deleteHighLineForIds(@RequestBody Map<String, Object> reqMap) {

		Result result = new Result();
		try {
			logger.debug("deleteHighLineForIds~~~reqMap==" + reqMap);
			String higlineCrossids = StringUtil.objToStr(reqMap
					.get("higlineCrossids"));
			// 先删除子表HIGHLINE_CROSS_TRAIN的数据
			highLineService
					.deleteHighlienCrossTrainForHighlineCrossId(higlineCrossids);
			// 再删除父表HIGHLINE_CROSS的数据
			highLineService
					.deleteHighlienCrossForHighlineCrossId(higlineCrossids);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("deleteHighLineForIds error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 获取命令内容
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/previewCmdInfo", method = RequestMethod.POST)
	public Result previewCmdInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {
			/**
			 * 2014年08月09日京广高铁日计划命令： 一、各站列车开行车次： 1.交路:0D2002-D2001-D2006-D6721;
			 * 车底:CRH5049A+CRH5057A; 2.交路:0D6721; 车底:CRH5049A+CRH5057A;
			 * 3.交路:0G2503; 车底:京局担当 4.交路:0G2504-G2503-G2504; 车底:京局担当
			 */
			final String f_crossName = "交路:";
			final String f_vehicleName = "车底:";
			final String f_point = ".";
			final String f_semicolon = ";";
			final String f_cmdinfo = "日计划命令:";
			final String f_oneCatalog = "一、各站列车开行车次：";
			final String f_bureauInfo = "局担当";
			final String f_plus = " + ";
			final String f_mul = " — ";
			HighlineCrossInfo crossInfoReq = new HighlineCrossInfo();
			String crossStartDate = StringUtil
					.objToStr(reqMap.get("startDate"));
			crossInfoReq.setCrossStartDate(crossStartDate);
			// 获取登录用户信息
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			crossInfoReq.setCrossBureau(user.getBureau());
			// 查询数据
			List<HighlineThroughlineInfo> list = highLineService
					.getHighlineThroughCrossInfo(crossInfoReq);
			if (list != null && list.size() > 0) {
				// 获取18个路局的基本信息
				Map<String, String> ljMap = commonService.getStationJCMapping();

				String dateStr = crossStartDate.substring(0, 4) + "年"
						+ crossStartDate.substring(4, 6) + "月"
						+ crossStartDate.substring(6, 8) + "日";
				StringBuffer cmdInfoBf = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					HighlineThroughlineInfo throughInfo = list.get(i);
					if (i != 0) {
						cmdInfoBf.append("\n").append("\n");
					}
					// 铁路线
					String throughLine = throughInfo.getThroughLine();
					cmdInfoBf.append(dateStr).append(throughLine)
							.append(f_cmdinfo).append("\n");
					cmdInfoBf.append(f_oneCatalog).append("\n");
					List<HighlineCrossInfo> crossInfoList = throughInfo
							.getListCrossInfo();
					if (crossInfoList != null && crossInfoList.size() > 0) {
						// 本局
						List<String> localList = new ArrayList<String>();

						// 外局
						List<String> otherList = new ArrayList<String>();
						for (int j = 0; j < crossInfoList.size(); j++) {
							StringBuffer localCmdInfoBf = new StringBuffer();
							StringBuffer otherCmdInfoBf = new StringBuffer();
							HighlineCrossInfo crossInfo = crossInfoList.get(j);
							String crossName = crossInfo.getCrossName();
							String crhType = crossInfo.getCrhType();
//							if ("0D904-D903-D2346-D2345-D904-0D903"
//									.equals(crossName)) {
//								int a = 1;
//							}
							String vehicle1 = crossInfo.getVehicle1();
							String vehicle2 = crossInfo.getVehicle2();
							// 车辆担当局
							String tokenVehBureau = crossInfo
									.getTokenVehBureau();
							// 交路计划所属局（局码）
							String crossBureau = crossInfo.getCrossBureau();
							// cmdInfoBf.append(j+1).append(f_point).append(f_crossName).append(crossName).append("    ");
							// 组装车底
							if (tokenVehBureau.equals(crossBureau)) {
								localCmdInfoBf.append(f_crossName)
										.append(crossName).append("    ");
								localCmdInfoBf.append(f_vehicleName);
								if ((null != vehicle1 && !"".equals(vehicle1))
										|| (null != vehicle2 && !""
												.equals(vehicle2))) {
									localCmdInfoBf.append(crhType);
								}
								if (vehicle1 != null && !"".equals(vehicle1)) {
									if (vehicle2 != null
											&& !"".equals(vehicle2)) {
										localCmdInfoBf.append(f_mul)
												.append(vehicle1)
												.append(f_plus)
												.append(vehicle2)
												.append(f_semicolon)
												.append("\n");
									} else {
										localCmdInfoBf.append(f_mul)
												.append(vehicle1)
												.append(f_semicolon)
												.append("\n");
									}
								} else {
									if (vehicle2 != null
											&& !"".equals(vehicle2)) {
										localCmdInfoBf.append(f_mul)
												.append(vehicle2)
												.append(f_semicolon)
												.append("\n");
									} else {
										// localCmdInfoBf.append(f_mul).append("\n");
										localCmdInfoBf.append("\n");
									}
								}
								localList.add(localCmdInfoBf.toString());
							} else {
								otherCmdInfoBf.append(f_crossName)
										.append(crossName).append("    ");
								otherCmdInfoBf.append(f_vehicleName)
										.append(ljMap.get(tokenVehBureau))
										.append(f_bureauInfo).append("\n");
								otherList.add(otherCmdInfoBf.toString());
							}

						}
						int count = 1;
						if (localList != null && localList.size() > 0) {
							for (String localStr : localList) {
								cmdInfoBf.append(count).append(f_point)
										.append(localStr);
								count++;
							}
						}

						if (otherList != null && otherList.size() > 0) {
							for (String otherStr : otherList) {
								cmdInfoBf.append(count).append(f_point)
										.append(otherStr);
								count++;
							}
						}
					}

				}
				// logger.debug("cmdInfo==" + cmdInfoBf.toString());
				result.setData(cmdInfoBf.toString());
			} else {
				// 没有命令
				result.setData("-1");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("previewCmdInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 生成命令
	 * 
	 * @param reqMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCmdInfo", method = RequestMethod.POST)
	public Result createCmdInfo(@RequestBody Map<String, Object> reqMap) {
		Result result = new Result();
		try {

			String cmdInfo = StringUtil.objToStr(reqMap.get("cmdInfo"));

			List<String> cmdContent = new ArrayList<String>();
			cmdContent.add(cmdInfo);

			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			String bureauCode = user.getBureau();
			String peopleName = user.getName();
			String accontName = user.getPostName();
			logger.debug("cmdInfo==" + cmdInfo);
			logger.debug("bureauCode==" + bureauCode + " peopleName=="
					+ peopleName + " accontName==" + accontName);

			// 构造服务实例
			ICmdAdapterService service = CmdAdapterServiceImpl.getInstance();
			// 服务初始化
			service.initilize(bureauCode);

			// 生成日计划命令
			String identifyStr = service.buildPlanCmd(cmdContent, accontName,
					peopleName);
			logger.debug("流水号=" + identifyStr);
			result.setMessage(identifyStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createCmdInfo error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 保存
	 * 
	 * @param reqMap
	 * @param roleType
	 *            取值：VEHICLE_SUB，VEHICLE_CHECK
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveHighLineWithRole/{roleType}", method = RequestMethod.POST)
	public Result saveHighLineWithRole(@RequestBody Map<String, Object> reqMap,
			@PathVariable String roleType) {
		Result result = new Result();
		try {
			List<Map> highLineCrossesList = (List<Map>) reqMap
					.get("highLineCrosses");
			if (highLineCrossesList != null && highLineCrossesList.size() > 0) {
				for (Map map : highLineCrossesList) {

					String highlineCrossId = StringUtil.objToStr(map
							.get("highLineCrossId"));
					String vehicle1 = StringUtil.objToStr(map.get("vehicle1"));
					String vehicle2 = StringUtil.objToStr(map.get("vehicle2"));
					int count = highLineService.updateHighLineVehicle(
							highlineCrossId, vehicle1, vehicle2);
					logger.debug("saveHighLineWithRole~~count==" + count);
				}
			}

		} catch (Exception e) {
			logger.error("saveHighLineWithRole error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param reqMap
	 * @param roleType
	 *            取值：CROSS_CHECK，VEHICLE_SUB，VEHICLE_CHECK
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/submitHighLineWithRole/{roleType}", method = RequestMethod.POST)
	public Result submitHighLineWithRole(
			@RequestBody Map<String, Object> reqMap,
			@PathVariable String roleType) {
		Result result = new Result();
		try {
			String typeCode = "";
			String contents = "";
			HighlineCrossInfo crossInfo = new HighlineCrossInfo();
			logger.debug("deleteHighLineForIds~~~reqMap==" + reqMap);
			String crossStartDate = StringUtil
					.objToStr(reqMap.get("startDate"));
			crossInfo.setCrossStartDate(crossStartDate);
			// 获取登录用户信息
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			String bureauCode = user.getBureau();
			String people = user.getName();
			String org = user.getDeptName();
			if ("CROSS_CHECK".equals(roleType)) {
				crossInfo.setCrossCheckPeople(people);
				crossInfo.setCrossCheckPeopleOrg(org);
				crossInfo.setCrossCheckType(1);
				typeCode = Constants.MSG_GT_CROSS_PLAN;
				// 消息内容：2015年6月3日广州局高铁交路计划已提交
				contents = DateUtil.formateDate3(crossStartDate) + user.getBureauFullName() + "高铁交路计划已提交";
			} else if ("VEHICLE_SUB".equals(roleType)) {
				crossInfo.setVehicleSubPeople(people);
				crossInfo.setVehicleSubPeopleOrg(org);
				crossInfo.setVehicleSubType(1);
				typeCode = Constants.MSG_TB_GT_CD;
				// 消息内容：2015年6月3日太原动车所已填报高铁车底。
				contents = DateUtil.formateDate3(crossStartDate) + user.getPostName() + "已填报高铁车底";
			} else if ("VEHICLE_CHECK".equals(roleType)) {
				crossInfo.setVehicleCheckPeople(people);
				crossInfo.setVehicleCheckPeopleOrg(org);
				crossInfo.setVehicleCheckType(1);
				typeCode = Constants.MSG_GT_CD_PLAN;
				// 消息内容：2015年6月3日太原局高铁车底计划已提交
				contents = DateUtil.formateDate3(crossStartDate) + user.getBureauFullName() + "高铁车底计划已提交";
			}
			crossInfo.setCrossBureau(bureauCode);

			highLineService.updateHighlineCrossInfo(crossInfo);
			
			if(!messageService.getMsgType().isEmpty()){
				if(StringUtils.isNotEmpty(crossStartDate) && StringUtils.isNotEmpty(user.getBureauFullName())){
					Integer msgCount = messageService.dealWithMsg(user, typeCode, contents,null,null);
					if(msgCount == 0){
						result.setCode(StaticCodeType.MSG_ERROR.getCode());
						result.setMessage(StaticCodeType.MSG_ERROR.getDescription());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("submitHighLineWithRole error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}

		return result;
	}

	/**
	 * 根据路局、开行日期yyyy-MM-dd查询高铁开行计划
	 * 
	 * @param reqMap
	 *            { key: bureauName 路局简称 key: bureauCode 路局拼音码 key: runDate
	 *            运行日期yyyy-MM-dd
	 * 
	 *            trainNbr 车次 （可选） runTypeCode 车次 （可选）(SFZD：始发终到 SFJC：始发交出
	 *            JRJC：接入交出 JRZD：接入终到) 来源于界面运行方式下拉框 treeType 车次 （可选）
	 *            树类型trainTypeTree/stnNameTree key 树节点key （可选
	 *            ''、G、C、D、DJ（动检）、KS(空送=回送+回场)、回场、回送、通勤、F（折返）、OTHER（其他）、）
	 *            keyType 树节点keyType （可选
	 *            ''、LK、TD、SF、JR、JC、ZD）//临客、图定、始发、接入、交出、终到
	 * 
	 *            }
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHighLinePlanByBureau", method = RequestMethod.POST)
	public Result getHighLinePlanByBureau(
			@RequestBody Map<String, String> reqMap) {
		Result result = new Result();
		boolean paramFlag = false;
		try {
			if (reqMap.get("bureauName") == null
					|| "".equals(reqMap.get("bureauName").trim())) {
				paramFlag = true;
			}
			if (reqMap.get("bureauCode") == null
					|| "".equals(reqMap.get("bureauCode").trim())) {
				paramFlag = true;
			}
			if (reqMap.get("runDate") == null
					|| "".equals(reqMap.get("runDate").trim())) {
				paramFlag = true;
			}

			if (paramFlag) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}

			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("bureauName", reqMap.get("bureauName"));
			queryMap.put("bureauCode", reqMap.get("bureauCode"));
			queryMap.put("runDate", reqMap.get("runDate"));
			queryMap.put("highlineFlag", reqMap.get("highlineFlag"));

			if ("trainTypeTree".equals(reqMap.get("treeType"))) {
				queryMap.put("trainTypeShortName", reqMap.get("key"));
				queryMap.put("creatType", reqMap.get("keyType"));
			} else if ("stnNameTree".equals(reqMap.get("treeType"))) {
				queryMap.put("stnName", reqMap.get("key"));
				queryMap.put("stnNameTreeRunType", reqMap.get("keyType"));// (SF：始发
																			// JR：接入
																			// JC：交出
																			// ZD：终到)
			}

			if (reqMap.get("trainNbr") != null
					&& !"".equals(reqMap.get("trainNbr"))) {
				queryMap.put("trainNbr", reqMap.get("trainNbr"));
			}
			if (reqMap.get("runTypeCode") != null
					&& !"".equals(reqMap.get("runTypeCode"))) {
				queryMap.put("runTypeCode", reqMap.get("runTypeCode"));// (SFZD：始发终到
																		// SFJC：始发交出
																		// JRZD：接入交出
																		// JRJC：接入终到)
			}

			result.setData(highLineService.getHighLinePlanByBureau(queryMap));
		} catch (Exception e) {
			logger.error("getHighLinePlanByBureau error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 根据路局、开行日期yyyy-MM-dd查询高铁开行计划
	 * 
	 * @param reqMap
	 *            { key: bureauName 路局简称 key: bureauCode 路局拼音码 key: runDate
	 *            运行日期yyyy-MM-dd
	 * 
	 *            trainNbr 车次 （可选） runTypeCode 车次 （可选）(SFZD：始发终到 SFJC：始发交出
	 *            JRJC：接入交出 JRZD：接入终到) 来源于界面运行方式下拉框 treeType 车次 （可选）
	 *            树类型trainTypeTree/stnNameTree key 树节点key （可选
	 *            ''、G、C、D、DJ（动检）、KS(空送=回送+回场)、回场、回送、通勤、F（折返）、OTHER（其他）、）
	 *            keyType 树节点keyType （可选
	 *            ''、LK、TD、SF、JR、JC、ZD）//临客、图定、始发、接入、交出、终到
	 * 
	 *            }
	 * 
	 *            既有线
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getExistLinePlanByBureau", method = RequestMethod.POST)
	public Result getExistLinePlanByBureau(
			@RequestBody Map<String, String> reqMap) {
		Result result = new Result();
		boolean paramFlag = false;
		try {
			if (reqMap.get("bureauName") == null
					|| "".equals(reqMap.get("bureauName").trim())) {
				paramFlag = true;
			}
			if (reqMap.get("bureauCode") == null
					|| "".equals(reqMap.get("bureauCode").trim())) {
				paramFlag = true;
			}
			if (reqMap.get("runDate") == null
					|| "".equals(reqMap.get("runDate").trim())) {
				paramFlag = true;
			}

			if (paramFlag) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}

			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("bureauName", reqMap.get("bureauName"));
			queryMap.put("bureauCode", reqMap.get("bureauCode"));
			queryMap.put("runDate", reqMap.get("runDate"));
			queryMap.put("highlineFlag", reqMap.get("highlineFlag"));

			if ("trainTypeTree".equals(reqMap.get("treeType"))) {
				queryMap.put("trainTypeShortName", reqMap.get("key"));
				queryMap.put("creatType", reqMap.get("keyType"));
			} else if ("stnNameTree".equals(reqMap.get("treeType"))) {
				queryMap.put("stnName", reqMap.get("key"));
				queryMap.put("stnNameTreeRunType", reqMap.get("keyType"));// (SF：始发
																			// JR：接入
																			// JC：交出
																			// ZD：终到)
			}

			if (reqMap.get("trainNbr") != null
					&& !"".equals(reqMap.get("trainNbr"))) {
				queryMap.put("trainNbr", reqMap.get("trainNbr"));
			}
			if (reqMap.get("runTypeCode") != null
					&& !"".equals(reqMap.get("runTypeCode"))) {
				queryMap.put("runTypeCode", reqMap.get("runTypeCode"));// (SFZD：始发终到
																		// SFJC：始发交出
																		// JRZD：接入交出
																		// JRJC：接入终到)
			}

			result.setData(highLineService.getExistLinePlanByBureau(queryMap));
		} catch (Exception e) {
			logger.error("getExistLinePlanByBureau error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 查询列车车底信息
	 * 
	 * 业务规则：1、根据planTrainId查询车底信息 2.根据crhType查询车型编组信息
	 * 
	 * @param queryMap
	 *            { key: planTrainId id key: creatType
	 *            //"创建方式  TD（0:基本图；1:基本图滚动)；LK(2:文件电报；3:命令）" }
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHighLineVehInfo", method = RequestMethod.POST)
	public Result getHighLineVehInfo(@RequestBody Map<String, String> queryMap) {
		Result result = new Result();
		try {
			if (queryMap.get("planTrainId") == null
					|| "".equals(queryMap.get("planTrainId").trim())) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}

			if (queryMap.get("creatType") == null
					|| (!"TD".equals(queryMap.get("creatType")) && !"LK"
							.equals(queryMap.get("creatType")))) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}

			result.setData(highLineService.getHighLineVehInfo(queryMap));
		} catch (Exception e) {
			logger.error("getHighLineVehInfo error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 查询列车车底信息
	 * 
	 * 业务规则：1、根据planTrainId查询车底信息 2.根据crhType查询车型编组信息
	 * 
	 * @param queryMap
	 *            { key: planTrainId id(必须参数) key: planCrossId 交路id(必须参数) key:
	 *            trainNbr 车次(必须参数) key: crewDate 乘务计划日期 yyyy-MM-dd(必须参数) key:
	 *            bureauName 查询局简称(必须参数) key: creatType
	 *            "创建方式  TD(图定) or LK(临客)"(必须参数) }
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getHighLineCrewInfo", method = RequestMethod.POST)
	public Result getHighLineCrewInfo(@RequestBody Map<String, String> queryMap) {
		Result result = new Result();
		try {
			if (queryMap.get("planTrainId") == null
					|| "".equals(queryMap.get("planTrainId").trim())) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}

			if (queryMap.get("creatType") == null
					|| (!"TD".equals(queryMap.get("creatType")) && !"LK"
							.equals(queryMap.get("creatType")))) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}

			if ("TD".equals(queryMap.get("creatType"))) {// 图定车planCrossId为必须参数
				if (queryMap.get("planCrossId") == null
						|| "".equals(queryMap.get("planCrossId").trim())) {
					result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
					result.setMessage("非法参数");
					return result;
				}
			}

			if (queryMap.get("crewDate") == null
					|| "".equals(queryMap.get("crewDate").trim())) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}
			if (queryMap.get("bureauName") == null
					|| "".equals(queryMap.get("bureauName").trim())) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}
			if (queryMap.get("trainNbr") == null
					|| "".equals(queryMap.get("trainNbr").trim())) {
				result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
				result.setMessage("非法参数");
				return result;
			}

			queryMap.put("crewDate",
					queryMap.get("crewDate").replaceAll("-", ""));
			queryMap.put("trainNbr", "-" + queryMap.get("trainNbr") + "-");
			result.setData(highLineService.getHighLineCrewInfo(queryMap));
		} catch (Exception e) {
			logger.error("getHighLineVehInfo error==" + e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
}
