package com.railway.passenger.transdispatch.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import commonToolKit.IO.Xml.mainFrame;


/**
 * 
 * <p>
 * Title：TimeUtils.java
 * </p>
 * <p>
 * Description：iBaseFramework
 * </p>
 * <p>
 * Copyright：Copyright (c)2006 BaoSight-HY,Inc
 * </p>
 * <p>
 * Company：BaoSight-HY,Inc
 * </p>
 * 
 * @author david.liu 2006-5-20
 * @version 1.0
 */
public class TimeUtils {
  public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
  
  public static final String YYYYMMDD = "yyyyMMdd";

  // Default date long
  public static final long DEFAULT_DATE = -5364691200000L;

  public static final String DEFAULTFORMAT = "yyyy-MM-dd HH:mm:ss";

  // "1800-01-01 00:00:00.0"

  /**
	 * 
	 */
  public static Calendar date2Calendar(Date date) {
    Calendar cal = null;
    if (date != null) {
      cal = GregorianCalendar.getInstance();
      cal.setTime(date);
    }
    return cal;
  }

  /**
   * 
   * <p>
   * <code>timestamp2Calendar</code>
   * </p>
   * 
   * @param timestamp
   * @return
   * @author david.liu 2005-10-18
   */
  public static Calendar timestamp2Calendar(java.sql.Timestamp timestamp) {
    Calendar cal = null;
    if (timestamp != null) {
      cal = GregorianCalendar.getInstance();
      cal.setTime(timestamp);
    }
    return cal;
  }

  /**
   * 
   * <p>
   * <code>getDefaultCalendar</code>
   * </p>
   * 
   * @return
   * @author david.liu 2005-10-18
   */
  // public static final Calendar getDefaultCalendar() {
  // Calendar cal = Calendar.getInstance();
  // cal.setTimeInMillis(DEFAULT_DATE); // "1800-01-01 00:00:00.0"
  // return cal;
  // }
  /**
   * 
   * <p>
   * <code>getDefaultTimestamp</code>
   * </p>
   * 
   * @return
   * @author david.liu 2005-10-18
   */
  public static final Timestamp getDefaultTimestamp() {
    return new Timestamp(DEFAULT_DATE); // "1800-01-01 00:00:00.0"
  }

  /**
   * 
   * <p>
   * <code>getCurrentDate</code>
   * </p>
   * 
   * @return
   * @author david.liu 2005-10-18
   */
  public static Calendar getCurrentDate() {
    return Calendar.getInstance();
  }

  /**
   * 
   * <p>
   * <code>getCurrentTimestamp</code>
   * </p>
   * 
   * @return
   * @author david.liu 2005-10-18
   */
  public static Timestamp getCurrentTimestamp() {
    long time = System.currentTimeMillis();
    return new Timestamp(time);
  }

  /**
   * 
   * <p>
   * <code>getCurrentTimestamp</code>
   * </p>
   * 
   * @param format
   * @return
   * @author david.liu 2005-10-18
   */
  public static Timestamp getCurrentTimestamp(String format) {
    if (format == null) {
      format = YYYY_MM_DD_FORMAT;
    }
    java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(format);

    java.util.Date date = null;
    try {
      date = simpleDateFormat.parse(getCurrentTime(format));
    } catch (java.text.ParseException e) {
      return null;
    }
    return new Timestamp(date.getTime());
  }

  /**
   * 
   * <p>
   * <code>getCurrentTime</code>
   * </p>
   * 
   * @param parrten
   * @return
   * @author david.liu 2005-10-18
   */
  public static String getCurrentTime(String parrten) {
    String timestr;
    if (parrten == null || parrten.equals("")) {
      parrten = YYYY_MM_DD_FORMAT;
    }
    java.util.Date cday = new java.util.Date();

    SimpleDateFormat sdf = new SimpleDateFormat(parrten);
    timestr = sdf.format(cday);
    return timestr;
  }

  /**
   * 
   * <p>
   * <code>isDefaultTimestamp</code>
   * </p>
   * 
   * @param time
   * @return
   * @author david.liu 2005-10-18
   */
  public static boolean isDefaultTimestamp(Timestamp time) {
    if (time.getTime() == DEFAULT_DATE) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 
   * <p>
   * <code>isDefaultTimestamp</code>
   * </p>
   * 
   * @param time
   * @return
   * @author david.liu 2005-10-18
   */
  public static boolean isDefaultTimestamp(Object time) {
    if (time instanceof java.sql.Timestamp) {
      Timestamp timeValue = (Timestamp) time;
      if (timeValue.getTime() == DEFAULT_DATE) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * 
   * <p>
   * <code>isDefaultCalendar</code>
   * </p>
   * 
   * @param time
   * @return
   * @author david.liu 2005-10-18
   */
  // public static boolean isDefaultCalendar(Calendar time) {
  // if (time.getTimeInMillis() == DEFAULT_DATE) {
  // return true;
  // } else {
  // return false;
  // }
  // }
  /**
   * 
   * <p>
   * <code>calendar2String</code>
   * </p>
   * 
   * @param format
   * @param cal
   * @return
   * @author david.liu 2005-10-18
   */
  // public static String calendar2String(String format, Calendar cal) {
  // if (cal.equals(getDefaultCalendar())) {
  // return "";
  // }
  //
  // if (format == null) {
  // format = "yyyy-MM-dd";
  // }
  // java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
  // format);
  //
  // if (cal == null) {
  // return null;
  // }
  // return formatter.format(cal.getTime());
  // }
  /**
   * 
   * <p>
   * <code>calendar2Timestamp</code>
   * </p>
   * 
   * @param cal
   * @return
   * @author david.liu 2005-10-18
   */
  public static final Timestamp calendar2Timestamp(Calendar cal) {

    java.util.Date date = null;
    date = cal.getTime();

    return new Timestamp(date.getTime());
  }

  /**
   * 
   * <p>
   * <code>date2String</code>
   * </p>
   * 
   * @param format
   * @param date
   * @return
   * @author david.liu 2005-10-18
   */
  // public static String date2String(String format, Date date) {
  // if (date.equals(getDefaultCalendar())) {
  // return "";
  // }
  //
  // if (format == null) {
  // format = "yyyy-MM-dd";
  // }
  // java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
  // format);
  //
  // if (date == null) {
  // return null;
  // }
  // return formatter.format(date);
  // }
  /**
   * 
   * <p>
   * <code>timestamp2String</code>
   * </p>
   * 
   * @param format
   * @param time
   * @return
   * @author david.liu 2005-10-18
   */
  public static String timestamp2String(String format, Timestamp time) {
    if (getDefaultTimestamp().equals(time)) {
      return "";
    }

    if (format == null) {
      format = YYYY_MM_DD_FORMAT;
    }
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);

    if (time == null) {
      return null;
    }
    return formatter.format(time);
  }

  /**
   * 
   * <p>
   * <code>string2Timestamp</code>
   * </p>
   * 
   * @param format
   * @param time
   * @return
   * @author david.liu 2005-10-18
   */
  public static final Timestamp string2Timestamp(String format, String time) {
    if (format == null) {
      format = YYYY_MM_DD_FORMAT;
    }
    java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(format);

    java.util.Date date = null;
    try {
      date = simpleDateFormat.parse(time);
    } catch (java.text.ParseException e) {
      return null;
    }
    return new Timestamp(date.getTime());
  }
  
  public static final Timestamp date2Timestamp(Date date){
	  return new Timestamp(date.getTime());
  }

  /**
   * 
   * <p>
   * <code>string2Calendar</code>
   * </p>
   * 
   * @param format
   * @param cal
   * @return
   * @author david.liu 2005-10-18
   */
  public static final Calendar string2Calendar(String format, String cal) {
    if (format == null) {
      format = YYYY_MM_DD_FORMAT;
    }
    java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(format);
    java.util.Date date = null;
    try {
      date = simpleDateFormat.parse(cal);
    } catch (java.text.ParseException e) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  /**
   * 
   * <p>
   * <code>getMonthDays</code>
   * </p>
   * 
   * @param year
   * @param month
   * @return
   * @author david.liu 2005-10-18
   */
  public static final int getMonthDays(int year, int month) {
    switch (month) {
      case 1:
        return 31;
      case 2:
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
          return 29;
        } else {
          return 28;
        }
      case 3:
        return 31;
      case 4:
        return 30;
      case 5:
        return 31;
      case 6:
        return 30;
      case 7:
        return 31;
      case 8:
        return 31;
      case 9:
        return 30;
      case 10:
        return 31;
      case 11:
        return 30;
      case 12:
        return 31;
    }
    return 0;
  }

  /**
   * 
   * <p>
   * <code>getDaysDiff</code>
   * </p>
   * 
   * @param sdate1
   * @param sdate2
   * @param format
   * @param tz
   * @return
   * @author david.liu 2005-10-18
   */
  public static int getDaysDiff(String sdate1, String sdate2, String format, java.util.TimeZone tz) {
    SimpleDateFormat df = new SimpleDateFormat(format);

    java.util.Date date1 = null;
    java.util.Date date2 = null;

    try {
      date1 = df.parse(sdate1);
      date2 = df.parse(sdate2);
    } catch (java.text.ParseException pe) {
      return -1;
    }

    Calendar cal1 = null;
    Calendar cal2 = null;

    if (tz == null) {
      cal1 = Calendar.getInstance();
      cal2 = Calendar.getInstance();
    } else {
      cal1 = Calendar.getInstance(tz);
      cal2 = Calendar.getInstance(tz);
    }

    // different date might have different offset
    cal1.setTime(date1);
    long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);

    cal2.setTime(date2);
    long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);

    // Use integer calculation, truncate the decimals
    int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
    int hr2 = (int) (ldate2 / 3600000);

    int days1 = (int) hr1 / 24;
    int days2 = (int) hr2 / 24;
    int dateDiff = days2 - days1;
    return dateDiff;
    // int dateDiff = days2 - days1;
    // int weekOffset = (cal2.get(Calendar.DAY_OF_WEEK) -
    // cal1.get(Calendar.DAY_OF_WEEK)) < 0 ? 1 : 0;
    // int weekDiff = dateDiff / 7 + weekOffset;
    // int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
    // int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) -
    // cal1.get(Calendar.MONTH);
  }

  /**
   * 
   * <p>
   * <code>getWeek</code>
   * </p>
   * 
   * @param num
   * @return
   * @author david.liu 2005-10-18
   */
  public static Calendar getWeek(int num) {

    Calendar date = Calendar.getInstance();
    int weekOfYear = date.get(Calendar.WEEK_OF_YEAR);
    weekOfYear += num;
    date.set(Calendar.WEEK_OF_YEAR, weekOfYear);
    return date;
  }

  /**
   * 
   * <p>
   * <code>getBeforeWeek</code>
   * </p>
   * 
   * @param num
   * @return
   * @author david.liu 2005-10-18
   */
  public static Calendar getBeforeWeek(int num) {

    Calendar date = Calendar.getInstance();
    int weekOfYear = date.get(Calendar.WEEK_OF_YEAR);
    weekOfYear -= num;
    date.set(Calendar.WEEK_OF_YEAR, weekOfYear);
    return date;
  }

  /**
   * 
   * <p>
   * <code>getMonth</code>
   * </p>
   * 
   * @param num
   * @return
   * @author david.liu 2005-10-18
   */
  public static Calendar getMonth(int num) {

    Calendar date = Calendar.getInstance();
    int monthOfYear = date.get(Calendar.MONTH);
    monthOfYear += num;
    date.set(Calendar.MONTH, monthOfYear);
    return date;
  }

  /**
   * 
   * <p>
   * <code>getBeforeMonth</code>
   * </p>
   * 
   * @param num
   * @return
   * @author david.liu 2005-10-18
   */
  public static Calendar getBeforeMonth(int num) {

    Calendar date = Calendar.getInstance();
    int monthOfYear = date.get(Calendar.MONTH);
    monthOfYear += num;
    date.set(Calendar.MONTH, monthOfYear);
    return date;
  }

  /**
   * 
   * <p>
   * <code>getDay</code>
   * </p>
   * 
   * @param num
   * @return
   * @author david.liu 2005-10-18
   */
  public static Calendar getDay(int num) {

    Calendar date = Calendar.getInstance();
    int dayOfYear = date.get(Calendar.DATE);
    dayOfYear += num;
    date.set(Calendar.DATE, dayOfYear);
    return date;
  }

  /**
   * 
   * <p>
   * <code>date2TimestampStart</code>
   * </p>
   * 
   * @param date
   * @return
   * @author david.liu 2005-10-18
   */
  public static Timestamp date2TimestampStart(Date date) {
    return new Timestamp(date.getTime());
  } // end date2TimestampStart

  /**
   * 
   * <p>
   * <code>date2TimestampEnd</code>
   * </p>
   * 
   * @param date
   * @return
   * @author david.liu 2005-10-18
   */
  // public static Timestamp date2TimestampEnd(Date date) {
  // Calendar cal = null;
  // if (date != null) {
  // cal = GregorianCalendar.getInstance();
  // cal.setTime(date);
  // }
  // cal.add(Calendar.DAY_OF_YEAR, 1);
  //
  // return new Timestamp(cal.getTimeInMillis());
  // } // end date2TimestampEnd
  /**
   * 
   * <p>
   * <code>getDateString</code>
   * </p>
   * 
   * @param date
   * @return
   * @author david.liu 2005-10-18
   */
  public static String getDateString(Date date) {
    SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
    return format.format(date);
  } // end getDateString

  public static String getDateString(Date date,String formatString) {
    SimpleDateFormat format = new SimpleDateFormat(formatString);
    return format.format(date);
  } 
  /**获取本周的第一天日期
   * @Title: getFristDayCurrentWeek
   * @Description: TODO
   * @param @return
   * @return String
   * @throws
   * @author shijiyu
   * @date 2013-7-1 上午11:19:36
   */
  public static String getFristDayCurrentWeek(){
    Calendar cal = new  GregorianCalendar();
    int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
    cal.add(Calendar.DATE, -day_of_week);
    SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
    return (simpleFormate.format(cal.getTime()));
  }
  /**获取与当前时间相差一周的时间
   * @Title: getFristDayCurrentWeek
   * @Description: TODO
   * @param @return
   * @return String
   * @throws
   * @author shijiyu
   * @date 2013-7-1 上午11:19:36
   */
  public static String getLastDayCurrentWeek(){
    Calendar cal = new  GregorianCalendar();
    int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
    cal.add(Calendar.DATE, -day_of_week);
    cal.add(Calendar.DATE, -6);
    SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
    return (simpleFormate.format(cal.getTime()));
  }
  
  /**获取本月的第一天日期
   * @Title: getFristDayCurrentWeek
   * @Description: TODO
   * @param @return
   * @return String
   * @throws
   * @author shijiyu
   * @date 2013-7-1 上午11:19:36
   */
  public static String getFristDayCurrentMonth(){
    Calendar cal = new  GregorianCalendar();
    cal.set( Calendar.DATE,  1 );
    SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
    return (simpleFormate.format(cal.getTime()));
  }
  /**获取本日的上月日期
   * @Title: getFristDayCurrentWeek
   * @Description: TODO
   * @param @return
   * @return String
   * @throws
   * @author shijiyu
   * @date 2013-7-1 上午11:19:36
   */
  public static String getLastDayCurrentMonth(){
	  Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, -1);
      SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
      return (simpleFormate.format(cal.getTime()));
  }
  public static int getDays(Date date, String period) {
		if (StringUtils.isEmpty(period))
			return 0;
		period = period.toLowerCase();
		String numstr;
		char u;
		int pos = period.indexOf('/'), num;

		if (pos != -1) {
			numstr = period.substring(0, pos);
			u = period.charAt(pos + 1);
		} else {
			numstr = period;
			u = 'd';
		}

		try {
			num = Integer.parseInt(numstr);
		} catch (Exception e) {
			e.printStackTrace();
			num = 2;
		}
		Calendar calendar = Calendar.getInstance();
		switch (u) {
		case 'w':
			calendar.setTime(date);
			calendar.add(Calendar.WEEK_OF_YEAR, -num);
			break;
		case 'd':
			return num;
		case 'm':
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, -num); // 得到前几月
		}

		int hr1 = (int) (calendar.getTime().getTime() / 3600000); // 60*60*1000
		int hr2 = (int) (date.getTime() / 3600000);

		int days1 = (int) hr1 / 24;
		int days2 = (int) hr2 / 24;
		int dateDiff = days2 - days1;
		return dateDiff;
	}
  public static String getBeforeDateByPeriod(Date date, String period) {
		if (StringUtils.isEmpty(period))
			return date2String(date, DEFAULTFORMAT);
		period = period.toLowerCase();
		String numstr;
		char u;
		int pos = period.indexOf('/'), num;

		if (pos != -1) {
			numstr = period.substring(0, pos);
			u = period.charAt(pos + 1);
		} else {
			numstr = period;
			u = 'd';
		}

		try {
			num = Integer.parseInt(numstr);
		} catch (Exception e) {
			e.printStackTrace();
			num = 2;
		}

		switch (u) {
		case 'w':
			return datetimeBeforeWeeks(date, num);
		case 'd':
			return datetimeBeforeDays(date, num);
		case 'm':
			return datetimeBeforeMonthes(date, num);
		}
		return date2String(date, DEFAULTFORMAT);
	}
  /**
	 * 
	 * <p>
	 * <code>date2String</code>
	 * </p>
	 * 
	 * @param format
	 * @param date
	 * @return
	 * @author david.liu 2005-10-18
	 */
	public static String date2String(Date date, String format) {
		if (format == null) {
			format = "yyyy-MM-dd";
		}
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				format);

		if (date == null) {
			return null;
		}
		return formatter.format(date);
	}
	public static String datetimeBeforeWeeks(Date date, int weeks) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, -weeks);
		// calendar.set(Calendar.MINUTE, 0);
		// calendar.set(Calendar.SECOND, 0);
		return startDatePerDay(calendar.getTime());
	}

	public static String datetimeBeforeDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -days); // 得到前一天
		// calendar.add(Calendar.MONTH, -1); //得到前一个月
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH)+1;
		return startDatePerDay(calendar.getTime());
	}

	public static String datetimeBeforeMonthes(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -months); // 得到前几月
		return startDatePerDay(calendar.getTime());
	}
	public static String startDatePerDay(Date date) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd 00:00:00");
		return formatter.format(date);
	}
	public static String endDatePerDay() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		return formatter.format(new Date());
	}

	public static String endDatePerDay(Date date) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyy-MM-dd 23:59:59");
		return formatter.format(date);
	}
	
	public static String getNextDateString(String time, int nextDays, String format){
		Date date = TimeUtils.string2Timestamp(format, time);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE)+nextDays);
		Date newDate = cal.getTime();
		return TimeUtils.date2String(newDate, format);
	}
	
//	public static void main(String[] args) {
//		String time = "20150922";
//		System.out.println(TimeUtils.getNextDateString(time, 10, TimeUtils.YYYYMMDD));
//	}
	
	  /**
     * 判断当前日期是星期几<br>
     * <br>
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
	 public static int dayForWeek(String pTime) throws Exception {
	  SimpleDateFormat format = new SimpleDateFormat(TimeUtils.YYYYMMDD);
	  Calendar c = Calendar.getInstance();
	  c.setTime(format.parse(pTime));
	  int dayForWeek = 0;
	  if(c.get(Calendar.DAY_OF_WEEK) == 1){
	   dayForWeek = 7;
	  }else{
	   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
	  }
	  return dayForWeek;
	 }
	 /**
	  * 
	  * @Description:判断是否是日期格式
	  * @param @param date
	  * @param @return   
	  * @return boolean  
	  * @throws
	  * @author Administrator
	  * @date 2015年9月24日
	  */
	 public static boolean isDate(String date)
	    {
	      /**
	       * 判断日期格式和范围
	       */
	      String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
	      Pattern pat = Pattern.compile(rexp);  
	      
	      Matcher mat = pat.matcher(date);  
	      
	      boolean dateType = mat.matches();

	      return dateType;
	    }
	 /**
	  * 
	  * @Description: 获取日期中的天
	  * @param @param date
	  * @param @return   
	  * @return int  
	  * @throws
	  * @author Administrator
	  * @date 2015年9月24日
	  */
	 public static int getDay(String date,String fomat){
		 DateFormat df = new SimpleDateFormat(fomat);
		 Date d=null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 //输出结果
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(d); 
		 int day = cal.get(Calendar.DAY_OF_MONTH); //日
		 return day;
	 }
	 /**
	  * 
	  * @Description: 获取月数
	  * @param @param date
	  * @param @param fomat
	  * @param @return   
	  * @return int  
	  * @throws
	  * @author Administrator
	  * @date 2015年9月24日
	  */
	 public static int getMonth(String date,String fomat){
		 DateFormat df = new SimpleDateFormat(fomat);
		 Date d=null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 //输出结果
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(d); 
		 int month = cal.get(Calendar.MONTH) + 1;  //日
		 return month;
	 }
	 public static int getYear(String date,String fomat){
		 DateFormat df = new SimpleDateFormat(fomat);
		 Date d=null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 //输出结果
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(d); 
		 int year = cal.get(Calendar.YEAR);  //日
		 return year;
	 }
	 
	 /**
	  * 
	  * @Description: 比较两个日期格式的字符串
	  * @param @param sourceDateStr 源
	  * @param @param targetDateStr 目标
	  * @param @param format 日期格式
	  * @param @return  1大于 0等于 -1小于
	  * @return int  
	  * @throws
	  * @author qs
	  * @date 2015年10月5日
	  */
	 public static int compareDate(String sourceDateStr, String targetDateStr, String format){
		 Date source = TimeUtils.string2Timestamp(format, sourceDateStr);
		 Date target = TimeUtils.string2Timestamp(format, targetDateStr);
		 if(source.getTime() > target.getTime()){
			 return 1;
		 }
		 if(source.getTime() == target.getTime()){
			 return 0;
		 }
		 if(source.getTime() < target.getTime()){
			 return -1;
		 }
		 return 0;
	 }
}
