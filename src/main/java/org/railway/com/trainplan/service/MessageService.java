package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.IdUtil;
import org.railway.com.trainplan.common.utils.SqlUtil;
import org.railway.com.trainplan.entity.MsgReceive;
import org.railway.com.trainplan.entity.MsgSend;
import org.railway.com.trainplan.entity.MsgType;
import org.railway.com.trainplan.repository.mybatis.MessageDao;
import org.railway.com.trainplan.repository.mybatis.UserDao;
import org.railway.com.trainplan.service.ShiroRealm.ShiroUser;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息service.
 * 
 * @author zhangPengDong
 *
 *         2015年6月5日 下午4:04:13
 */
@Component
@Transactional
@Monitored
public class MessageService {

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private UserDao userDao;

	@Autowired
	private SendMsgService sendMsgService;

	/**
	 * 消息类型.
	 */
	private static List<MsgType> msgTypes = new ArrayList<MsgType>();

	public List<MsgType> getMsgType() {
		if (msgTypes.isEmpty()) {
			msgTypes = messageDao.getMsgType();
		}
		return msgTypes;
	}

	public Integer getReceiveMsgCount(Map<String, Object> map) {
		return messageDao.getReceiveMsgCount(map);
	}

	public List<MsgReceive> getReceiveMsg(Map<String, Object> map) {
		return messageDao.getReceiveMsg(map);
	}

	public List<MsgSend> getSendMsg(Map<String, Object> map) {
		return messageDao.getSendMsg(map);
	}

	/**
	 * 修改接收表.
	 * 
	 * @param map
	 * @return
	 */
	public Integer updateReceiveByMap(Map<String, Object> map) {
		return messageDao.updateReceiveByMap(map);
	}

	/**
	 * 处理消息.
	 * 
	 * @param user
	 *            发送系列.
	 * @param typeCode
	 *            发送消息类型.
	 * @param contents
	 *            发送消息内容.
	 * @param relevantBureau
	 *            相关局（'id1','id2','id3'）（仅用于第二个场景）.
	 * @param type
	 *            类型（高铁1？普速0?）（仅用于第二个场景）.
	 * @return
	 */
	public Integer dealWithMsg(ShiroUser user, String typeCode,
			String contents, String relevantBureau, String type) {
		String sendId = IdUtil.getIdByUUID();
		int sentI = insertMsgSent(user, typeCode, contents, sendId);
		if (sentI > 0) {
			return dealWithReceive(user, typeCode, sendId, relevantBureau, type);
		}
		return 0;
	}

	/**
	 * 处理消息.
	 * 
	 * @param user
	 * @param typeCode
	 * @param contents
	 * @param receiverList
	 * @return
	 */
	public Integer dealWithMsg1(ShiroUser user, String typeCode,
			String contents, List<Map<String, Object>> receiverList) {
		String sendId = IdUtil.getIdByUUID();
		int sentI = insertMsgSent(user, typeCode, contents, sendId);
		if (sentI > 0) {
			return dealWithReceive1(typeCode, sendId, receiverList);
		}
		return 0;
	}

	/**
	 * ..
	 * 
	 * @param user
	 *            发送系列.
	 * @param typeCode
	 *            发送消息类型.
	 * @param msgContents
	 *            发送消息内容.
	 * @param id
	 *            发送ID.
	 * @return
	 */
	public Integer insertMsgSent(ShiroUser user, String typeCode,
			String msgContents, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type_code", typeCode);
		// 依哲哥要求，消息内容需要直接展现在列表里，而当消息内容过长时，依哲哥要求，将内容自动换行.
		map.put("msg_contents", msgContents);
		map.put("send_bureau", user.getBureau());
		map.put("send_post", user.getPostName());
		map.put("send_people", user.getName());
		return messageDao.insertMsgSent(map);
	}

	public Integer insertMsgReceive(List<MsgReceive> insList) {
		return messageDao.insertMsgReceive(insList);
	}

	/**
	 * 处理接收消息.
	 * 
	 * @param user
	 *            用于查询岗位.
	 * @param typeCode
	 *            消息类型.
	 * @param sendId
	 *            发送ID，父级ID.
	 * @param relevantBureau
	 *            相关局（'id1','id2','id3'）（仅用于第二个场景）.
	 * @param type
	 *            类型（高铁1？普速0?）（仅用于第二个场景）.
	 * @return
	 */
	public Integer dealWithReceive(ShiroUser user, String typeCode,
			String sendId, String relevantBureau, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<MsgReceive> insList = new ArrayList<MsgReceive>();
		// 下面的判断有好几个都可以合并判断的，为什么没有，是担心客户再次更改需求.
		if (StringUtils.equals(Constants.MSG_GT_CROSS_PLAN, typeCode)) {
			// 高铁交路计划 - 下级所有动车所 - 站段动车所
			params.put("rol_name", Constants.SEL_DCS);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dcsList = userDao
					.getDctOrDcsByMap(params);
			if (!dcsList.isEmpty()) {
				insList = zzMsgReceiveIns(dcsList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_TB_GT_CD, typeCode)) {
			// 填报高铁车底 - 上级动车调 - 铁路局动车调
			params.put("rol_name", Constants.SEL_DCT);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_GT_CD_PLAN, typeCode)) {
			// 高铁车底计划 - 动车调 - 铁路局高铁计划调
			params.put("rol_name", Constants.SEL_GT_JHD);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_GT_CZ_CW, typeCode)) {
			// 高铁车长乘务 - 客运段 - 本局高铁计划调
			params.put("rol_name", Constants.SEL_GT_JHD);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_PS_CZ_CW, typeCode)) {
			// 普速车长乘务 - 客运段 - 本局客调
			params.put("rol_name", Constants.SEL_KD);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_GT_SJ_CW, typeCode)) {
			// 高铁司机乘务 - 机务段 - 动车调
			params.put("rol_name", Constants.SEL_DCT);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_PS_SJ_CW, typeCode)) {
			// 普速司机乘务 - 机务段 - 客调
			params.put("rol_name", Constants.SEL_KD);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_GT_JXS_CW, typeCode)) {
			// 高铁机械师乘务 - 动车所 - 动车调
			params.put("rol_name", Constants.SEL_DCT);
			params.put("ljpym", user.getBureau());
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		} else if (StringUtils.equals(Constants.MSG_ADD_TD_PLAN, typeCode)
				|| StringUtils.equals(Constants.MSG_UPD_TD_PLAN, typeCode)
				|| StringUtils.equals(Constants.MSG_ADD_LK_PLAN, typeCode)
				|| StringUtils.equals(Constants.MSG_UPD_LK_PLAN, typeCode)
				|| StringUtils.equals(Constants.MSG_BC_LK_STN, typeCode)) {
			// 啊大 - 相关的外局客调/高铁计划调
			if (StringUtils.isNotEmpty(relevantBureau)) {
				if (null != type) {
					if (StringUtils.equals(type, "0")) {
						params.put("rol_name", Constants.SEL_KD);
					} else if (StringUtils.equals(type, "1")) {
						params.put("rol_name", Constants.SEL_GT_JHD);
					} else {
						return 0;
					}
				}
				params.put("ljpyms", relevantBureau);
				List<Map<String, Object>> dctList = userDao
						.getDctOrDcsByMap(params);
				if (!dctList.isEmpty()) {
					insList = zzMsgReceiveIns(dctList, sendId);
				}
			}
		} else if (StringUtils.equals(Constants.MSG_JBT_CHECKED, typeCode)) {
			// 核基本图 - 当前局的技教室 - 相关局的技教室
			params.put("rol_name", Constants.SEL_JJS);
			params.put("ljpyms", SqlUtil.strSqlIn1(relevantBureau));
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (!dctList.isEmpty()) {
				insList = zzMsgReceiveIns(dctList, sendId);
			}
		}

		if (!insList.isEmpty()) {
			return insertMsgReceive(insList);
		}
		return 0;
	}

	/**
	 * 处理接收消息.
	 * 
	 * @param typeCode
	 * @param sendId
	 * @param receiverList
	 * @return
	 */
	public Integer dealWithReceive1(String typeCode, String sendId,
			List<Map<String, Object>> receiverList) {
		List<MsgReceive> insList = zzMsgReceiveIns1(receiverList, sendId);
		if (!insList.isEmpty()) {
			return insertMsgReceive(insList);
		}
		return 0;
	}

	/**
	 * 组装消息集合.
	 * 
	 * @param dcstList
	 * @param sendId
	 * @param bureau
	 * @param bureauFullName
	 * @return
	 */
	private List<MsgReceive> zzMsgReceiveIns(
			List<Map<String, Object>> dcstList, String sendId) {
		List<MsgReceive> insList = new ArrayList<MsgReceive>();
		for (int i = 0; i < dcstList.size(); i++) {
			MsgReceive mr = new MsgReceive();
			mr.setId(IdUtil.getIdByUUID());
			mr.setSendId(sendId);
			mr.setReceiveBureau((String) dcstList.get(i).get("LJPYM"));
			mr.setReceivePost((String) dcstList.get(i).get("ACC_NAME"));
			// 由于是给岗位发，所以没有接收人.
			mr.setReceivePeople("");
			insList.add(mr);
		}
		return insList;
	}

	/**
	 * 组装消息集合.
	 * 
	 * @param dcstList
	 * @param sendId
	 * @param bureau
	 * @param bureauFullName
	 * @return
	 */
	private List<MsgReceive> zzMsgReceiveIns1(
			List<Map<String, Object>> dcstList, String sendId) {
		List<MsgReceive> insList = new ArrayList<MsgReceive>();
		for (int i = 0; i < dcstList.size(); i++) {
			MsgReceive mr = new MsgReceive();
			mr.setId(IdUtil.getIdByUUID());
			mr.setSendId(sendId);
			mr.setReceiveBureau((String) dcstList.get(i).get("bureau"));
			mr.setReceivePost((String) dcstList.get(i).get("position"));
			mr.setReceivePeople((String) dcstList.get(i).get("userName"));
			insList.add(mr);
		}
		return insList;
	}

	/**
	 * 修改发送表.
	 * 
	 * @param map
	 * @return
	 */
	public Integer updateSendByMap(Map<String, Object> map) {
		return messageDao.updateSendByMap(map);
	}

	/**
	 * 校验传递过来的接收方是否存在.
	 * 
	 * @param m
	 *            接收方信息.
	 * @param typeCode
	 *            消息类型编码.
	 * @return
	 */
	public Boolean checkReceiver(Map<String, Object> m, String typeCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.equals(Constants.MSG_JBT_CHECKED, typeCode)) {
			// 核基本图 - 当前局的技教室 - 相关局的技教室
			params.put("rol_name", Constants.SEL_JJS);
			params.put("ljpym", MapUtils.getString(m, "bureau"));
			params.put("acc_name", MapUtils.getString(m, "position"));
			List<Map<String, Object>> dctList = userDao
					.getDctOrDcsByMap(params);
			if (dctList.size() == 1) {
				return true;
			}
		}
		return false;
	}
}
