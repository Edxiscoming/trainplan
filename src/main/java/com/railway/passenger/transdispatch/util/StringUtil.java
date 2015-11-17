package com.railway.passenger.transdispatch.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * 
 * ClassName:StringUtil <br/>
 * Date: 2013-9-5 下午9:01:54 <br/>
 * 
 * @author jinwei
 * @version
 * @since JDK 1.6
 * @see
 */
public class StringUtil {

	/**
	 * 字符串转整形
	 * 
	 * @param obj
	 * @return
	 */
	public static Integer strToInteger(String str) {
		Integer result = 0;
		try {
			if ("".equals(str) || null == str) {
				return 0;
			} else {
				result = Integer.parseInt(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// done
		return result;
	}

	public static Long strToLong(String str) {
		Long result = 0L;
		try {
			if ("".equals(str) || null == str) {
				return result;
			} else {
				result = Long.parseLong(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// done
		return result;
	}

	public static String objToStr(Object obj) {
		String result = "";
		try {
			if (null == obj) {
				return result;
			} else {
				result = String.valueOf(obj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// done
		return result;
	}

	public static boolean strIsNull(String obj) {
		boolean result = false;
		try {
			if (null == obj || "".equals(obj)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// done
		return result;
	}

	public static boolean isboolIp(String ipAddress) {
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

	public static String summaryToJSON(String summary) {
		summary = summary.replace("\n", "");
		char[] summaryChar = summary.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < summaryChar.length; i++) {
			if (summaryChar[i] == ',') {
				int j = i + 1;
				// 判断是否是空格
				for (; (int) summaryChar[j] == 32; j++) {
				}
				if (summaryChar[j] != ']') {
					sb.append(summaryChar[i]);
				}
				sb.append(summaryChar[j]);
				i = j;
			} else {
				sb.append(summaryChar[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * isNum:判断一个字符串是否为数字. <br/>
	 * 
	 * @author xuhualong
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static boolean isNum(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 获取当前主机IP
	public static String getLocalHost() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		return addr.getHostAddress();
	}

	public static String handleTime(String time) {
		if (time != null && !"".equals(time)) {
			return time.substring(2);
		}
		return null;
	}

	public static List<String> mergeStation(List one, List two) {
		List<String> result = new ArrayList<String>();
		List<String> shortList = null;
		List<String> longList = null;
		List<String> test = new LinkedList<String>();

		if (one.size() >= two.size()) {
			longList = one;
			shortList = two;
		} else {
			longList = two;
			shortList = one;
		}
		for (int i = 0; i < longList.size(); i++) {
			String temp1 = longList.get(i);
			if (!shortList.contains(temp1)) {
				result.add(temp1);
			} else {
				int indexTemp1 = shortList.indexOf(temp1);

				while ((indexTemp1 + 1) > 0) {
					result.add(shortList.get(0));
					shortList.remove(0);
					indexTemp1--;
				}
			}
		}
		if (shortList != null && shortList.size() > 0) {
			for (int j = 0; j < shortList.size(); j++) {
				result.add(shortList.get(j));
			}
		}
		return result;
	}

	public static boolean isEmpty(String s) {
		if (s == null || "".equals(s))
			return true;

		return false;
	}

//	public static void main(String[] args) {
//		LocalDate LsatDate = DateTimeFormat.forPattern(TimeUtils.YYYYMMDD)
//				.parseLocalDate("20150214");
//		LocalDate nowDate = DateTimeFormat.forPattern(TimeUtils.YYYYMMDD)
//				.parseLocalDate(
//						DateUtil.getStringFromDate(new Date(), TimeUtils.YYYYMMDD));
//
//		int initInterval = Days.daysBetween(LsatDate, nowDate).getDays();
//		System.out.println(initInterval);
//
//		LocalDate runPlanStartDate = DateTimeFormat.forPattern(TimeUtils.YYYYMMDD)
//				.parseLocalDate("20140228");
//		LocalDate runPlanEndDate = DateTimeFormat.forPattern(TimeUtils.YYYYMMDD)
//				.parseLocalDate("20141101").plusMonths(2);
//		System.out.println(runPlanEndDate.toString().replaceAll("-", "")
//				.substring(4, 6));
//		System.out.println(DateUtil.getDaysBetween("2015-01-31", "2015-02-02"));
//		// System.out.println(System.currentTimeMillis());
//		// System.out.println(new Date("2014-04-22 10:10:10"));
//		// System.out.println("" + handleTime("0:9:0:0"));
//		// System.out.println("0:9:0:0".substring(0,1));
//		// System.err.println(278910/1000);
//		List<String> list = new ArrayList<String>();
//		List<String> list1 = new ArrayList<String>();
//		List<String> list2 = new ArrayList<String>();
//
//		list2.add("J");
//		list2.add("I");
//		list2.add("H");
//		list2.add("F");
//		list2.add("D");
//		list2.add("C");
//		list2.add("P");
//
//		list1.add("K");
//		list1.add("J");
//		list1.add("I");
//		list1.add("H");
//		list1.add("G");
//		list1.add("E");
//		list1.add("D");
//		list1.add("C");
//		list1.add("B");
//		list1.add("A");
//		// [K, J, I, H, G, E, F, D, C, B, A, P]
//		// [K, J, I, H, G, E, F, D, C, B, A, P]
//		for (int i = 0; i < list1.size(); i++) {
//			String temp1 = list1.get(i);
//			if (!list2.contains(temp1)) {
//				list.add(temp1);
//			} else {
//				int indexTemp1 = list2.indexOf(temp1);
//
//				while ((indexTemp1 + 1) > 0) {
//					list.add(list2.get(0));
//					list2.remove(0);
//					indexTemp1--;
//				}
//			}
//		}
//		if (list2 != null && list2.size() > 0) {
//			for (int j = 0; j < list2.size(); j++) {
//				list.add(list2.get(j));
//			}
//		}
//
//		// System.err.println(list);
//		// System.err.println(list1);
//		Collections.reverse(list1);
//		// System.err.println(list1);
//
//		Map<String, Object> reqMap = new HashMap<String, Object>();
//		System.err.println(objToStr(reqMap.get("test")));
//	}

	public static List<String> getDwrmsgShareUrl(String str) {
		StringTokenizer st = new StringTokenizer(str, ";");
		int count = st.countTokens();
		List<String> urlList = new ArrayList<String>();
		for (int i = 0; i < (count); i++) {
			urlList.add(st.nextToken());
		}
		return urlList;
	}

	public static List<String> getDateStr(String str) {
		StringTokenizer st = new StringTokenizer(str, ",");
		int count = st.countTokens();
		List<String> dateStrs = new ArrayList<String>();
		for (int i = 0; i < (count); i++) {
			dateStrs.add(st.nextToken());
		}
		return dateStrs;
	}

	/**
	 * 数组内是否有重复数据(indexOf).
	 * 
	 * @param str
	 *            String[].
	 * @return
	 */
	public static boolean strIsChongFu(String[] str) {
		String strAll = "";
		for (int i = 0; i < str.length; i++) {
			if (strAll.indexOf(str[i]) < 0) {
				strAll += str[i] + "\n";
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数组内是否有重复数据(set).
	 * 
	 * @param str
	 *            String[].
	 * @return
	 */
	public static boolean strIsChongFu1(String[] str) {
		HashSet<String> hs = new HashSet<String>();
		for (int i = 0; i < str.length; i++) {
			if (!hs.add(str[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断数组内是否有相同的数据.
	 * 
	 * @param str
	 *            数组
	 * @param s
	 *            指定数据.
	 * @return
	 */
	public static boolean strIsChongFu2(String[] str, String s) {
		int checkI = 0;
		for (int i = 0; i < str.length; i++) {
			if(StringUtils.equals(str[i], s)){
				checkI++;
				if(checkI > 1){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 将数组中的数据,已SQL in的格式组装起来; '1','2','3','4'.(已迁移至：SqlUtil.strSqlIn)
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
	 * 按规则获取分类信息List
	 * 
	 * @param crossName
	 * @return
	 */
	public static List<String> getRegexInfos(String allInfo, String regex) {
		List<String> infos = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(allInfo, regex);
		int count = st.countTokens();
		for (int i = 0; i < (count); i++) {
			infos.add(st.nextToken());
		}

		return infos;
	}
}
