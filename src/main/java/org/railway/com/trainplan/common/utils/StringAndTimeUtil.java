package org.railway.com.trainplan.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 字符串及日期工具类
 * 
 * @author ITC
 * 
 */
public class StringAndTimeUtil {

	public static SimpleDateFormat hourMinuteSdf = new SimpleDateFormat("HHmm");
	public static SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
	public static SimpleDateFormat minuteSdf = new SimpleDateFormat("mm");
	public static SimpleDateFormat hourMinuteSecondSdf = new SimpleDateFormat(
			"HHmmss");
	public static SimpleDateFormat hourColonMinuteSdf = new SimpleDateFormat(
			"HH:mm");
	public static SimpleDateFormat minuteSecondFullSdf = new SimpleDateFormat(
			"mm分ss秒");
	public static SimpleDateFormat hourColonMinuteSecondSdf = new SimpleDateFormat(
			"HH:mm:ss");
	public static SimpleDateFormat yearMonthDaySimpleSdf = new SimpleDateFormat(
			"yyyyMMdd");
	public static SimpleDateFormat yearMonthDaySdf = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat yearMonthDayFullSdf = new SimpleDateFormat(
			"yyyy年MM月dd日");
	public static SimpleDateFormat yearMonthDayHourMinuteSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public static SimpleDateFormat yearMonthDayHourMinuteSecondSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat yearMonthDayHourMinuteSecondMicroSecondSdf = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss.SSSZ");

	public static SimpleDateFormat ymdHourMinuteSdf = new SimpleDateFormat(
			"yyyyMMdd HHmmss");

	/**
	 * 分离字符串
	 */
	public static List<String> getSplitedString(String string, String token) {
		List<String> sList = new ArrayList<String>();
		if (string != null && !string.trim().equals("")) {
			StringTokenizer st = new StringTokenizer(string, token);
			while (st.hasMoreElements()) {
				String s = ((String) st.nextElement()).trim();
				sList.add(s);
			}
		}

		return sList;
	}

	/**
	 * 分离字符串,最后一部分
	 */
	public static String getLastSplitedString(String string, String token) {
		String sList = null;
		;
		StringTokenizer st = new StringTokenizer(string, token);
		while (st.hasMoreElements()) {
			sList = ((String) st.nextElement()).trim();
		}
		return sList;
	}

	/**
	 * 分离字符串,第一部分
	 */
	public static String getFirstSplitedString(String string, String token) {
		String sList = null;
		StringTokenizer st = new StringTokenizer(string, token);
		while (st.hasMoreElements()) {
			sList = ((String) st.nextElement()).trim();
			break;
		}
		return sList;
	}

	/**
	 * 分离字符串通过两个标记
	 */
	public static List<String> getSplitedStringByTwoToken(String string,
			String token1, String token2) {
		List<String> sList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(string, token1);
		while (st.hasMoreElements()) {
			sList.add(((String) st.nextElement()).trim());
		}
		if (sList.isEmpty() || sList.size() == 1) {
			sList.clear();
			st = new StringTokenizer(string, token2);
			while (st.hasMoreElements()) {
				sList.add(((String) st.nextElement()).trim());
			}
		}
		return sList;
	}

	/**
	 * 规范时间格式 HH:MM:SS
	 * 
	 * @param timeText
	 * @return
	 */
	public static String getFormatedTimeText(String timeText) {
		timeText = timeText.trim();
		int length = timeText.length();
		if (length == 4) {
			if (!timeText.contains(":")) {
				String s1 = timeText.substring(0, 2);
				String s2 = timeText.substring(2, 4);
				timeText = s1 + ":" + s2 + ":00";
			}
		} else if (length == 6) {
			if (!timeText.contains(":")) {
				String s1 = timeText.substring(0, 2);
				String s2 = timeText.substring(2, 4);
				String s3 = timeText.substring(4, 6);
				timeText = s1 + ":" + s2 + ":" + s3;
			}
		}
		return timeText;
	}

	/**
	 * 规范时间格式 HHMMSS
	 * 
	 * @param timeText
	 * @return
	 */
	public static String getFormatedTimeTextWithoutColon(String timeText) {
		timeText = timeText.trim().replace(":", "");
		int length = timeText.length();
		if (length == 4) {
			String s1 = timeText.substring(0, 2);
			String s2 = timeText.substring(2, 4);
			timeText = s1 + s2 + "00";
		}
		return timeText;
	}

	/**
	 * 验证是否为铁路时间格式 "HH:MM:SS"表示：HH时MM分SS秒
	 * 
	 * @param datastr
	 * @return
	 */
	public static boolean validateTimeStr(String datestr) {

		try {

			// 检测长度
			if (datestr == null || datestr.length() != 6)
				return false;

			// 检测始发为数字
			int tp = Integer.parseInt(datestr);

			// 检测前是否有负号
			if (tp < 0)
				return false;

			// 检测数据正确性，第一二位不能大于23 第三四位和第五六位不能大于59
			String hourstr = datestr.substring(0, 2);
			int hour = Integer.parseInt(hourstr);
			if (hour > 23)
				return false;

			String mmstr = datestr.substring(2, 4);
			int mm = Integer.parseInt(mmstr);
			if (mm > 59)
				return false;
			String ssstr = datestr.substring(4, 6);
			int ss = Integer.parseInt(ssstr);
			if (ss > 59)
				return false;

		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	// 规范交路格式
	public static String getFormatedCycleNameText(String cycleName) {
		cycleName = cycleName != null ? cycleName.trim() : "";
		if (cycleName.contains("/")) {
			cycleName = cycleName.replace("/", "-");
		}
		return cycleName;
	}

	/**
	 * 计算出发时间与到达时间的差值
	 * 
	 * @param arriveTime
	 *            到达时间
	 * @param departTime
	 *            出发时间
	 * @return
	 */
	public static String computeStationStopTime(Date arriveTime, Date departTime) {

		String stopTime = "";
		if (arriveTime == null || departTime == null)
			return stopTime;

		String secondTimeStr = ((departTime.getTime() - arriveTime.getTime()) % (60 * 1000)) / 1000 == 0 ? ""
				: ((departTime.getTime() - arriveTime.getTime()) % (60 * 1000))
						/ 1000 + "秒";

		stopTime = departTime.after(arriveTime) ? (departTime.getTime() - arriveTime
				.getTime()) / (60 * 1000) + "分" + secondTimeStr
				: "";
		return stopTime;
	}

	/**
	 * 时间字符串转化为Date
	 * 
	 * @param timeText
	 *            时间字符串
	 * @param sdf
	 *            日期格式
	 * @return
	 */
	public static Date getDateByStringText(String timeText, SimpleDateFormat sdf) {
		Date time = null;
		try {
			time = sdf.parse(timeText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 日期时间字符串转化为Timestamp
	 * 
	 * @param dateString
	 *            日期字符串
	 * @param timeText
	 *            时间字符串
	 * @param sdf
	 *            日期格式
	 * @return
	 */
	public static Timestamp getTimestampByStringText(String dateString,
			String timeText, SimpleDateFormat sdf) {
		Timestamp time = null;
		try {
			String string = dateString + " " + timeText;
			time = new Timestamp(sdf.parse(string).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 计算初始时间间隔天后的时间:时、分、秒、毫秒置0
	 * 
	 * @param targetTime
	 *            初始时间
	 * @param interval
	 *            间隔
	 * @return
	 */
	public static Date computerIntervalDate(Date targetTime, int interval) {
		Date resultDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(targetTime);
		cal.add(Calendar.DAY_OF_MONTH, interval);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		resultDate = cal.getTime();
		return resultDate;
	}

	/**
	 * 计算初始时间间隔天之的间隔秒时间
	 * 
	 * @param targerTime
	 *            初始时间
	 * @param targerTime
	 *            间隔天数
	 * @param targerTime
	 *            间隔秒
	 * @return
	 */
	public static Date computerLateDate(Date targetTime, int dayInteval,
			int secondInteval) {
		if (targetTime == null) {
			return targetTime;
		}
		Date resultDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(targetTime);
		cal.add(Calendar.DAY_OF_MONTH, dayInteval);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		resultDate = new Date(cal.getTime().getTime() + secondInteval);
		;
		return resultDate;
	}

	/**
	 * 验证是否为铁路时间格式 "1145"表示：11点45分
	 * 
	 * @param datastr
	 * @return
	 */

	/*
	 * 非法字符判定,有非法字符就是true
	 */
	public static boolean unValidText(String str) {
		boolean unValid = false;
		String errChar = "!@#$%^&*?~";

		if (str.equals(""))
			unValid = false;

		for (int i = 0; i < errChar.length(); i++) {
			if (str.indexOf(errChar.charAt(i)) != -1) {
				unValid = true;
				break;

			}
		}

		return unValid;
	}

	/**
	 * 日期和时间字符串转化为Date
	 * 
	 * @param dateString
	 *            日期字符串
	 * @param timeText
	 *            时间字符串
	 * @return
	 */
	public static Date getDateByStringText(String dateString, String timeText) {
		Date time = null;
		timeText = getFormatedTimeTextWithoutColon(timeText);
		try {
			String string = dateString + " " + timeText;
			time = ymdHourMinuteSdf.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	

	/**
	 * 20150506 只判断开始时间
	 * 判断开始日期是否在开行日期整天时间段范围内
	 * 
	 * @param startDate
	 *            开始日期
	 * @param runDate
	 *            开行日期
	 * @return
	 */
	public static boolean isStartAndTargetDateInRunDate2(Date startDate,Date runDate) {
		boolean flag = false;

		if (startDate != null) {

			Date runDateYestoday = StringAndTimeUtil.computerLateDate(runDate,
					0, -1);
			Date runDatetomorrow = StringAndTimeUtil.computerLateDate(runDate,
					1, 0);
			if (startDate.after(runDateYestoday)
					&& startDate.before(runDatetomorrow)) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * 判断开始日期和结束日期是否在开行日期整天时间段范围内
	 * 
	 * @param startDate
	 *            开始日期
	 * @param targetDate
	 *            结束日期
	 * @param runDate
	 *            开行日期
	 * @return
	 */
	public static boolean isStartAndTargetDateInRunDate(Date startDate,
			Date targetDate, Date runDate) {
		boolean flag = false;

		if (startDate != null && targetDate != null) {

			Date runDateYestoday = StringAndTimeUtil.computerLateDate(runDate,
					0, -1);
			Date runDatetoday = StringAndTimeUtil.computerLateDate(runDate, 0,
					1);
			Date runDatetomorrow = StringAndTimeUtil.computerLateDate(runDate,
					1, 0);

			if ((startDate.before(runDatetoday)
					&& targetDate.after(runDateYestoday))
					|| (startDate.before(runDatetomorrow)
					&& startDate.after(runDateYestoday)
					&& targetDate.after(runDateYestoday))) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * 取字符串的第一个字，用作过滤路局简称中的"局"
	 * 
	 * @param ljjc
	 * @return
	 */
	public static String getFirstSubString(String ljjc) {
		return ljjc.length() == 2 ? ljjc.substring(0, 1) : ljjc;
	}

	public static void main(String[] a) {

	}
}
