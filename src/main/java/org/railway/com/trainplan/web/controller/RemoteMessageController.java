package org.railway.com.trainplan.web.controller;

import java.util.ArrayList;
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
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.SqlUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.MsgReceive;
import org.railway.com.trainplan.entity.MsgSend;
import org.railway.com.trainplan.service.MessageService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 消息处理.
 * 
 * @author zhangPengDong
 *
 *         2015年6月5日 上午11:17:49
 */
@RestController
@RequestMapping(value = "/rmsg")
public class RemoteMessageController {
	private static Log logger = LogFactory.getLog(RemoteMessageController.class
			.getName());

	@Autowired
	private MessageService messageService;

//	/**
//	 * ..
//	 * 
//	 * @param type
//	 *            send:发送；accepted
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(method = RequestMethod.GET)
//	public ModelAndView view(@RequestParam(defaultValue = "") String type,
//			HttpServletRequest req) {
//		ModelAndView model = new ModelAndView();
//		model.setViewName("message/msg");
//		model.addObject("user", (ShiroRealm.ShiroUser) SecurityUtils
//				.getSubject().getPrincipal());
//		model.addObject("basePath", req.getContextPath());
//		return model;
//	}

	/**
	 * 获取消息类型.
	 * 
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value = "/getMsgType", method = RequestMethod.GET)
//	public Result getMsgType() {
//		Result result = new Result();
//		try {
//			result.setData(messageService.getMsgType());
//		} catch (Exception e) {
//			logger.error("getFullStationInfo error==" + e.getMessage());
//			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
//			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
//		}
//		return result;
//	}


	/**
	 * 获取接收消息.
	 * 
	 * @param reqMap
	 * @param req
	 * @return
	 */
//	@RequestMapping(value = "/getReceiveMsgCount", method = RequestMethod.POST)
//	public Result getReceiveMsgCount(@RequestBody Map<String, Object> reqMap) {
//		Result result = new Result();
//		try {
//			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
//					.getSubject().getPrincipal();
//			reqMap.put("receivePost", user.getPostName());
//			result.setData(messageService.getReceiveMsgCount(reqMap) == 0 ? "" : messageService.getReceiveMsgCount(reqMap));
//		} catch (Exception e) {
//			logger.error("getFullStationInfo error==" + e.getMessage());
//			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
//			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
//		}
//		return result;
//	}

	/**
	 * 获取接收消息.
	 * 
	 * @param reqMap
	 * @param req
	 * @return
	 */
//	@RequestMapping(value = "/getReceiveMsg", method = RequestMethod.POST)
//	public Result getReceiveMsg(@RequestBody Map<String, Object> reqMap) {
//		Result result = new Result();
//		try {
//			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
//					.getSubject().getPrincipal();
//			reqMap.put("receivePost", user.getPostName());
////			result.setData(messageService.getReceiveMsg(reqMap));
//			// 将发送局P - 京
//			List<MsgReceive> mrList = messageService.getReceiveMsg(reqMap);
//			if(!mrList.isEmpty()){
//				for (MsgReceive msgReceive : mrList) {
//					msgReceive.setSendBureau(LjUtil.getLjByNameBs(msgReceive.getSendBureau(), 2));
//				}
//			}
//			result.setData(mrList);
//		} catch (Exception e) {
//			logger.error("getFullStationInfo error==" + e.getMessage());
//			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
//			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
//		}
//		return result;
//	}
	
	/**
	 * 转到消息页面
	 */
	@RequestMapping(value = "/getMsgPage", method = RequestMethod.GET)
	public ModelAndView getMsgPage(HttpServletRequest request) {
		request.setAttribute("postname", request.getParameter("postname"));
		ModelAndView result = new ModelAndView("/message/msg_title");
		return result;
	}
	/**
	 * 获取消息 poto title.
	 * author suntao
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMsgTitle", method = RequestMethod.GET)
	public Result getMsgTitle(HttpServletRequest request) {
		Result result = new Result();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		try {
			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
					.getSubject().getPrincipal();
			
			reqMap.put("receivePost", null);
//			reqMap.put("receivePost", user.getPostName());
			// 将发送局P - 京
			List<MsgReceive> mrList = new ArrayList<MsgReceive>();
			List<MsgReceive> bdList = messageService.getReceiveMsg(reqMap);
			MsgReceive ms = null;
			int count = Math.min(30, bdList.size());
			for (int i = 0; i < count; i++) {
				ms = bdList.get(i);
				ms.setSendBureau(LjUtil.getLjByNameBs(ms.getSendBureau(), 2));
				mrList.add(ms);
			}
			result.setData(mrList);
		} catch (Exception e) {
			logger.error("getMsgTitle error==" + e.getMessage());
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
		}
		return result;
	}
	/**
	 * 获取发送消息.
	 * 
	 * @param reqMap
	 * @param req
	 * @return
	 */
//	@RequestMapping(value = "/getSendMsg", method = RequestMethod.POST)
//	public Result getSendMsg(@RequestBody Map<String, Object> reqMap) {
//		Result result = new Result();
//		try {
//			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
//					.getSubject().getPrincipal();
//			reqMap.put("sendPost", user.getPostName());
////			result.setData(messageService.getSendMsg(reqMap));
//			// 将接收局P - 京
//			List<MsgSend> msList = messageService.getSendMsg(reqMap);
//			if(!msList.isEmpty()){
//				for (MsgSend msgSend : msList) {
//					if(!msgSend.getReceiveList().isEmpty()){
//						for (MsgReceive msgReceive : msgSend.getReceiveList()) {
//							msgReceive.setReceiveBureau(LjUtil.getLjByNameBs(null != msgReceive.getReceiveBureau() ? msgReceive.getReceiveBureau() : "", 2));
//						}
//					}
//				}
//			}
//			result.setData(msList);
//		} catch (Exception e) {
//		e.printStackTrace();
//			logger.error("getFullStationInfo error==" + e.getMessage());
//			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
//			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
//		}
//		return result;
//	}

	/**
	 * 接收消息：签收.
	 * 
	 * @param reqMap
	 * @param req
	 * @return
	 */
//	@RequestMapping(value = "/receiveMsgQs", method = RequestMethod.POST)
//	public Result receiveMsgQs(@RequestBody Map<String, Object> reqMap) {
//		Result result = new Result();
//		try {
//			ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
//					.getSubject().getPrincipal();
//			String ids = (String) reqMap.get("ids");
//			String[] str;
//			if (StringUtils.isNotEmpty(ids)) {
//				if (ids.indexOf(";") > -1) {
////					String str[] = ids.split(";");
//					str = ids.split(";");
//					ids = SqlUtil.strSqlIn(str);
//				} else {
//					ids = "'" + ids + "'";
//				}
//				reqMap.clear();
//				reqMap.put("ids", ids);
//				reqMap.put("qsPeople", user.getName());
//				reqMap.put("msgStatus", 1);
//				Integer updI = messageService.updateReceiveByMap(reqMap);
//				if(updI > 0){
//					reqMap.clear();
//					reqMap.put("ids", ids);
//					reqMap.put("statusCode", 1);
//					List<MsgReceive> mrList = messageService.getReceiveMsg(reqMap);
//					result.setData(mrList);
//					
//					// 更改send表状态
//					if(!mrList.isEmpty()){
//						reqMap.clear();
//						for (MsgReceive msgReceive : mrList) {
//							reqMap.put("sendId", msgReceive.getSendId());
//							reqMap.put("msgStatus", "根据接收表更改主表状态");
//							messageService.updateSendByMap(reqMap);
//						}
//					}
//				}
//				
//			}
//		} catch (Exception e) {
//			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
//			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
//			e.printStackTrace();
//		}
//		return result;
//	}

	/**
	 * 接收消息：删除.
	 * 
	 * @param reqMap
	 * @param req
	 * @return
	 */
//	@RequestMapping(value = "/receiveDel", method = RequestMethod.POST)
//	public Result receiveMsgDel(@RequestBody Map<String, Object> reqMap) {
//		Result result = new Result();
//		try {
//			String ids = (String) reqMap.get("ids");
//			if (StringUtils.isNotEmpty(ids)) {
//				if (ids.indexOf(";") > -1) {
//					String str[] = ids.split(";");
//					ids = SqlUtil.strSqlIn(str);
//				} else {
//					ids = "'" + ids + "'";
//				}
//				reqMap.clear();
//				reqMap.put("ids", ids);
//				reqMap.put("msgStatus", 2);
//				messageService.updateReceiveByMap(reqMap);
//			}
//		} catch (Exception e) {
//			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
//			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());
//			e.printStackTrace();
//		}
//		return result;
//	}
}
