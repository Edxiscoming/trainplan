package org.railway.com.trainplan.common.utils;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.railway.com.trainplan.web.controller.RunPlanController;

/**
 * sign校验.
 * 
 * @author zhangPengDong
 *
 */
public class SignUtil {
	private static Log log = LogFactory.getLog(SignUtil.class.getName());

	/**
	 * 校验发送消息.
	 * 
	 * @param map
	 * @return
	 */
	public static String checkSendMsg(Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		sb.append(MapUtils.getString(map, "userName"));
		sb.append("|");
		sb.append(MapUtils.getString(map, "accId"));
		sb.append("|");
		sb.append(MapUtils.getString(map, "typeCode"));
		sb.append("|");
		sb.append(MapUtils.getString(map, "contents") == null ? "" : MapUtils
				.getString(map, "contents"));
		sb.append("|");
		sb.append(MapUtils.getString(map, "relevantBureau") == null ? ""
				: MapUtils.getString(map, "relevantBureau"));
		log.info("checkSendMsg【sb：" + sb.toString() + ",sign："
				+ Md5Encrypt.getString(sb.toString()) + "】");
		return Md5Encrypt.getString(sb.toString());
	}

	/**
	 * 校验发送消息.
	 * 
	 * @param map
	 * @return
	 */
	public static String checkSendMsg1(String contents) {
		log.info("checkSendMsg1【sb：" + contents + ",sign："
				+ Md5Encrypt.getString(contents) + "】");
		return Md5Encrypt.getString(contents);
	}
}
