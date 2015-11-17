package org.railway.com.trainplan.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * SQL参数处理.
 * 
 * @author zhangPengDong
 *
 *         2015年3月20日 上午11:47:55
 */
public class SqlUtil {

	/**
	 * 将数组中的数据,已SQL in的格式组装起来; '1','2','3','4'.
	 * 
	 * @param str
	 *            String[].
	 * @return
	 */
	public static String strSqlIn(String[] str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			sb.append("'").append(str[i]).append("'");
			if (i != (str.length - 1)) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 数组转换为list，作用于mybatis批量操作.
	 * 
	 * @param str
	 *            需要处理的数组.
	 * @return List<String>.
	 */
	public static List<String> strArrayToList(String[] str) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		if (!list.isEmpty()) {
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 将字符串循环组装成：'z','p','d'.
	 * 
	 * @param str
	 *            字符串：zpd
	 * @return
	 */
	public static String strSqlIn1(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (StringUtils.isNotEmpty(sb.toString())) {
				sb.append(",");
			}
			sb.append("'").append(str.substring(i, i + 1)).append("'");

		}
		return sb.toString();
	}

}
