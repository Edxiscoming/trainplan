package org.railway.com.trainplan.web.controller.api;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.IdUtil;
import org.railway.com.trainplan.common.utils.IpUtil;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.common.utils.ResponseUtil;
import org.railway.com.trainplan.common.utils.SignUtil;
import org.railway.com.trainplan.entity.LogApi;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.repository.mybatis.UserDao;
import org.railway.com.trainplan.service.LogApiService;
import org.railway.com.trainplan.service.MessageService;
import org.railway.com.trainplan.service.ShiroRealm.ShiroUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangPengDong
 *
 *         2015年7月3日 上午11:55:27
 */
@RestController
@RequestMapping(value = "/api/msg")
public class MessageApiController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private LogApiService logApiService;

	/**
	 * 发消息.
	 * 
	 * @param req
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	// @RequestMapping(value = "/sendMsg", method = RequestMethod.GET)
	public ResponseEntity<String> sendMsg(HttpServletRequest req)
			throws UnsupportedEncodingException {
		req.setCharacterEncoding("utf-8");
		JSONObject returnObj = new JSONObject();
		// s
		JSONObject jsonObject = JSONObject.fromObject(req.getParameter("msg"));
		if (null != jsonObject) {
			// param
			boolean paramsB = false;
			Map<String, Object> map = new HashMap<String, Object>();
			if (jsonObject.containsKey("userName")) {
				map.put("userName", jsonObject.getString("userName"));
			} else {
				paramsB = true;
			}
			if (jsonObject.containsKey("accId")) {
				map.put("accId", jsonObject.getString("accId"));
			} else {
				paramsB = true;
			}
			if (jsonObject.containsKey("typeCode")) {
				map.put("typeCode", jsonObject.getString("typeCode"));
			} else {
				paramsB = true;
			}
			if (!paramsB) {
				if (jsonObject.containsKey("contents")) {
					map.put("contents", jsonObject.getString("contents"));
				}
				if (jsonObject.containsKey("relevantBureau")) {
					map.put("relevantBureau",
							jsonObject.getString("relevantBureau"));
				}
				// sign
				String sign = jsonObject.getString("sign");
				if (StringUtils.equals(SignUtil.checkSendMsg(map), sign)) {
					// user
					ShiroUser shiroUser = dealWithUser(
							MapUtils.getString(map, "userName"),
							MapUtils.getString(map, "accId"), null);
					if (null != shiroUser) {
						// send
						Integer msgCount = messageService
								.dealWithMsg(shiroUser, MapUtils.getString(map,
										"typeCode"), MapUtils.getString(map,
										"contents"), MapUtils.getString(map,
										"relevantBureau"), null);
						if (msgCount == 0) {
							returnObj.put("status",
									StaticCodeType.MSG_ERROR.getCode());
							returnObj.put("msg",
									StaticCodeType.MSG_ERROR.getDescription());
						} else {
							returnObj.put("status",
									StaticCodeType.SYSTEM_SUCCESS.getCode());
							returnObj.put("msg", StaticCodeType.SYSTEM_SUCCESS
									.getDescription());
						}
					} else {
						returnObj.put("status",
								StaticCodeType.SYSTEM_USER_ISNULL.getCode());
						returnObj.put("msg", StaticCodeType.SYSTEM_USER_ISNULL
								.getDescription());
					}
				} else {
					returnObj.put("status",
							StaticCodeType.SYSTEM_SIGN_EQUALS.getCode());
					returnObj.put("msg",
							StaticCodeType.SYSTEM_SIGN_EQUALS.getDescription());
				}
			} else {
				returnObj.put("status",
						StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				returnObj.put("msg",
						StaticCodeType.SYSTEM_PARAM_LOST.getDescription());
			}
		} else {
			returnObj.put("status", StaticCodeType.SYSTEM_PARAM_LOST.getCode());
			returnObj.put("msg",
					StaticCodeType.SYSTEM_PARAM_LOST.getDescription());
		}
		// e
		return ResponseUtil.toJsonResponse(returnObj.toString());
	}

	/**
	 * 发消息.
	 * 
	 * @param req
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	public ResponseEntity<String> sendMsg1(HttpServletRequest req)
			throws UnsupportedEncodingException {
		LogApi la = new LogApi();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		la.setApi_time(df.format(new Date()).toString());
		la.setId(IdUtil.getIdByUUID());
		la.setApi_ip(IpUtil.getIpAddr(req));

		req.setCharacterEncoding("utf-8");

		JSONObject returnObj = new JSONObject();
		// s
		JSONObject jsonObject = JSONObject.fromObject(req.getParameter("msg"));
		if (null != jsonObject
				&& !StringUtils.equals(jsonObject.toString(), "null")) {
			la.setApi_json(jsonObject.toString());
			// param
			String typeCode = null;
			String sign = null;
			JSONObject sender = null;
			String sendBureau = null;
			String sendAccId = null;
			String sendUserName = null;
			JSONArray receiver = null;
			List<Map<String, Object>> receiverList = new ArrayList<Map<String, Object>>();
			;
			// 判断如此之多，是因为文档里要求返回的错误信息要锁定到具体的那个参数，所以只能把文档中所有必填的参数都做判断，并且都做返回.
			if (jsonObject.containsKey("typeCode")) {
				typeCode = jsonObject.getString("typeCode");
				la.setApi_type(typeCode);
				if (StringUtils.isEmpty(typeCode)) {
					returnObj.put("status",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR.getCode());
					returnObj.put(
							"msg",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
									.getDescription() + ": {typeCode}");
					logApi(returnObj.getString("status"),
							returnObj.getString("msg"), la);
					return ResponseUtil.toJsonResponse(returnObj.toString());
				}
			} else {
				returnObj.put("status",
						StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				returnObj.put("msg",
						StaticCodeType.SYSTEM_PARAM_LOST.getDescription()
								+ ": {typeCode}");
				logApi(returnObj.getString("status"),
						returnObj.getString("msg"), la);
				return ResponseUtil.toJsonResponse(returnObj.toString());
			}
			if (jsonObject.containsKey("sign")) {
				sign = jsonObject.getString("sign");
				if (StringUtils.isEmpty(sign)) {
					returnObj.put("status",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR.getCode());
					returnObj.put(
							"msg",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
									.getDescription() + ": {sign}");
					logApi(returnObj.getString("status"),
							returnObj.getString("msg"), la);
					return ResponseUtil.toJsonResponse(returnObj.toString());
				}
			} else {
				returnObj.put("status",
						StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				returnObj.put("msg",
						StaticCodeType.SYSTEM_PARAM_LOST.getDescription()
								+ ": {sign}");
				logApi(returnObj.getString("status"),
						returnObj.getString("msg"), la);
				return ResponseUtil.toJsonResponse(returnObj.toString());
			}
			if (jsonObject.containsKey("sender")) {
				sender = jsonObject.getJSONObject("sender");
				if (null != sender) {
					if (sender.containsKey("bureau")) {
						sendBureau = sender.getString("bureau");
						if (StringUtils.isEmpty(sendBureau)) {
							returnObj.put("status",
									StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
											.getCode());
							returnObj.put(
									"msg",
									StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
											.getDescription()
											+ ": {sender-bureau}");
							logApi(returnObj.getString("status"),
									returnObj.getString("msg"), la);
							return ResponseUtil.toJsonResponse(returnObj
									.toString());
						}
					} else {
						returnObj.put("status",
								StaticCodeType.SYSTEM_PARAM_LOST.getCode());
						returnObj
								.put("msg",
										StaticCodeType.SYSTEM_PARAM_LOST
												.getDescription()
												+ ": {sender-bureau}");
						logApi(returnObj.getString("status"),
								returnObj.getString("msg"), la);
						return ResponseUtil
								.toJsonResponse(returnObj.toString());
					}
					if (sender.containsKey("accId")) {
						sendAccId = sender.getString("accId");
						if (StringUtils.isEmpty(sendBureau)) {
							returnObj.put("status",
									StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
											.getCode());
							returnObj.put(
									"msg",
									StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
											.getDescription()
											+ ": {sender-accId}");
							logApi(returnObj.getString("status"),
									returnObj.getString("msg"), la);
							return ResponseUtil.toJsonResponse(returnObj
									.toString());
						}
					} else {
						returnObj.put("status",
								StaticCodeType.SYSTEM_PARAM_LOST.getCode());
						returnObj.put(
								"msg",
								StaticCodeType.SYSTEM_PARAM_LOST
										.getDescription() + ": {sender-accId}");
						logApi(returnObj.getString("status"),
								returnObj.getString("msg"), la);
						return ResponseUtil
								.toJsonResponse(returnObj.toString());
					}
					if (sender.containsKey("userName")) {
						sendUserName = sender.getString("userName");
						if (StringUtils.isEmpty(sendBureau)) {
							returnObj.put("status",
									StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
											.getCode());
							returnObj.put(
									"msg",
									StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
											.getDescription()
											+ ": {sender-userName}");
							logApi(returnObj.getString("status"),
									returnObj.getString("msg"), la);
							return ResponseUtil.toJsonResponse(returnObj
									.toString());
						}
					} else {
						returnObj.put("status",
								StaticCodeType.SYSTEM_PARAM_LOST.getCode());
						returnObj.put(
								"msg",
								StaticCodeType.SYSTEM_PARAM_LOST
										.getDescription()
										+ ": {sender-userName}");
						logApi(returnObj.getString("status"),
								returnObj.getString("msg"), la);
						return ResponseUtil
								.toJsonResponse(returnObj.toString());
					}
				} else {
					returnObj.put("status",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR.getCode());
					returnObj.put(
							"msg",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
									.getDescription() + ": {sender}");
					logApi(returnObj.getString("status"),
							returnObj.getString("msg"), la);
					return ResponseUtil.toJsonResponse(returnObj.toString());
				}
			} else {
				returnObj.put("status",
						StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				returnObj.put("msg",
						StaticCodeType.SYSTEM_PARAM_LOST.getDescription()
								+ ": {sender}");
				logApi(returnObj.getString("status"),
						returnObj.getString("msg"), la);
				return ResponseUtil.toJsonResponse(returnObj.toString());
			}
			if (jsonObject.containsKey("receiver")) {
				receiver = jsonObject.getJSONArray("receiver");
				if (null != receiver && !receiver.isEmpty()) {
					for (int i = 0; i < receiver.size(); i++) {
						JSONObject jo = receiver.getJSONObject(i);
						Map<String, Object> m = new HashMap<String, Object>();
						if (jo.containsKey("bureau")) {
							if (StringUtils.isNotEmpty(jo.getString("bureau"))) {
								m.put("bureau", jo.getString("bureau"));
							} else {
								returnObj.put("status",
										StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
												.getCode());
								returnObj.put(
										"msg",
										StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
												.getDescription()
												+ ": {receiver-bureau}");
								logApi(returnObj.getString("status"),
										returnObj.getString("msg"), la);
								return ResponseUtil.toJsonResponse(returnObj
										.toString());
							}
						} else {
							returnObj.put("status",
									StaticCodeType.SYSTEM_PARAM_LOST.getCode());
							returnObj.put(
									"msg",
									StaticCodeType.SYSTEM_PARAM_LOST
											.getDescription()
											+ ": {receiver-bureau}");
							logApi(returnObj.getString("status"),
									returnObj.getString("msg"), la);
							return ResponseUtil.toJsonResponse(returnObj
									.toString());
						}
						if (jo.containsKey("position")) {
							if (StringUtils
									.isNotEmpty(jo.getString("position"))) {
								m.put("position", jo.getString("position"));
							} else {
								returnObj.put("status",
										StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
												.getCode());
								returnObj.put(
										"msg",
										StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
												.getDescription()
												+ ": {receiver-position}");
								logApi(returnObj.getString("status"),
										returnObj.getString("msg"), la);
								return ResponseUtil.toJsonResponse(returnObj
										.toString());
							}
						} else {
							returnObj.put("status",
									StaticCodeType.SYSTEM_PARAM_LOST.getCode());
							returnObj.put(
									"msg",
									StaticCodeType.SYSTEM_PARAM_LOST
											.getDescription()
											+ ": {receiver-position}");
							logApi(returnObj.getString("status"),
									returnObj.getString("msg"), la);
							return ResponseUtil.toJsonResponse(returnObj
									.toString());
						}
						if (jo.containsKey("userName")) {
							m.put("userName", jo.getString("userName"));
						}
						receiverList.add(m);
					}
				} else {
					returnObj.put("status",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR.getCode());
					returnObj.put(
							"msg",
							StaticCodeType.SYSTEM_DATA_FORMAT_ERROR
									.getDescription() + ": {receiver}");
					logApi(returnObj.getString("status"),
							returnObj.getString("msg"), la);
					return ResponseUtil.toJsonResponse(returnObj.toString());
				}
			} else {
				returnObj.put("status",
						StaticCodeType.SYSTEM_PARAM_LOST.getCode());
				returnObj.put("msg",
						StaticCodeType.SYSTEM_PARAM_LOST.getDescription()
								+ ": {receiver}");
				logApi(returnObj.getString("status"),
						returnObj.getString("msg"), la);
				return ResponseUtil.toJsonResponse(returnObj.toString());
			}

			String contents = jsonObject.getString("contents");
			// sign
			if (StringUtils.equals(SignUtil.checkSendMsg1(contents), sign)) {
				// user
				ShiroUser shiroUser = dealWithUser(sendUserName, sendAccId,
						LjUtil.getLjByNameBs(sendBureau, 2));
				if (null != shiroUser) {
					// receiverList
					if (!receiverList.isEmpty()) {
						for (int i = 0; i < receiverList.size(); i++) {
							Map<String, Object> m = receiverList.get(i);
							if (!m.isEmpty()) {
								if (!messageService.checkReceiver(m, typeCode)) {
									returnObj.put("status",
											StaticCodeType.SYSTEM_USER_ISNULL
													.getCode());
									returnObj.put(
											"msg",
											StaticCodeType.SYSTEM_USER_ISNULL
													.getDescription()
													+ "：{bureau}, {position}。");
									logApi(returnObj.getString("status"),
											returnObj.getString("msg"), la);
									return ResponseUtil
											.toJsonResponse(returnObj
													.toString());
								}
							}
						}
						// send
						Integer msgCount = messageService.dealWithMsg1(
								shiroUser, typeCode, contents, receiverList);
						if (msgCount == 0) {
							returnObj.put("status",
									StaticCodeType.MSG_ERROR.getCode());
							returnObj.put("msg",
									StaticCodeType.MSG_ERROR.getDescription());
							logApi(returnObj.getString("status"),
									returnObj.getString("msg"), la);
							return ResponseUtil.toJsonResponse(returnObj
									.toString());
						} else {
							returnObj.put("status",
									StaticCodeType.SYSTEM_SUCCESS.getCode());
							returnObj.put("msg", StaticCodeType.SYSTEM_SUCCESS
									.getDescription());
							logApi(returnObj.getString("status"),
									returnObj.getString("msg"), la);
							return ResponseUtil.toJsonResponse(returnObj
									.toString());
						}
					}
				} else {
					returnObj.put("status",
							StaticCodeType.SYSTEM_USER_ISNULL.getCode());
					returnObj
							.put("msg",
									StaticCodeType.SYSTEM_USER_ISNULL
											.getDescription()
											+ "：{sendBureau}, {sendAccId}, {sendUserName}。");
					logApi(returnObj.getString("status"),
							returnObj.getString("msg"), la);
					return ResponseUtil.toJsonResponse(returnObj.toString());
				}
			} else {
				returnObj.put("status",
						StaticCodeType.SYSTEM_SIGN_EQUALS.getCode());
				returnObj.put("msg",
						StaticCodeType.SYSTEM_SIGN_EQUALS.getDescription());
				logApi(returnObj.getString("status"),
						returnObj.getString("msg"), la);
				return ResponseUtil.toJsonResponse(returnObj.toString());
			}
		} else {
			returnObj.put("status",
					StaticCodeType.SYSTEM_DATA_FORMAT_ERROR.getCode());
			returnObj.put("msg",
					StaticCodeType.SYSTEM_DATA_FORMAT_ERROR.getDescription());
			logApi(returnObj.getString("status"), returnObj.getString("msg"),
					la);
			return ResponseUtil.toJsonResponse(returnObj.toString());
		}
		// e
		return ResponseUtil.toJsonResponse(returnObj.toString());
	}

	/**
	 * 处理发送用户.
	 * 
	 * @param userName
	 *            用户名.
	 * @param accId
	 *            accId.
	 * @param accId
	 *            京.
	 * @return
	 */
	private ShiroUser dealWithUser(String userName, String accId, String lj) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", userName);
		params.put("accId", accId);
		params.put("ljjc", lj);
		User user = userDao.getUserByUsernameAndAccId(params);
		if (null != user) {
			ShiroUser shiroUser = new ShiroUser(user.getUsername(),
					user.getName(), Integer.parseInt(accId), user.getLjpym(),
					user.getLjjc(), user.getLjqc(), user.getDeptname(),
					user.getPostId(), user.getPostName());
			return shiroUser;
		}
		return null;
	}

	private void logApi(String code, String msg, LogApi la) {
		la.setApi_status(code);
		la.setApi_msg(msg);
		logApiService.insertLogApi(la);
	}

}
