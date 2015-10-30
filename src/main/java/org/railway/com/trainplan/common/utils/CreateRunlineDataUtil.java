package org.railway.com.trainplan.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.railway.com.trainplan.entity.runline.TimeSchedule;
import org.railway.com.trainplanv2.dto.TimeSchedule2;

public class CreateRunlineDataUtil {
	
	public static Long getMillisecondTime(String time) {
			
		return parseStringToDate(time).getTime();
		
	}
	
	public static Long getMillisecondTime1(String date, String time, long days) {
		int days1 = Integer.parseInt(Long.toString(days));
		String timeStr = getStringTime(date, time, days1);		
		return parseStringToDate(timeStr).getTime();
		
	}
	
	public static String getStringTime(String date, String time, int days) {
		String timeStr = date.substring(0, 10) + time.substring(10, 19);
		
		if(0 == days) {
			return timeStr;
		}
		else {
			//字符串转Date
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				java.util.Date timeDate = dfs.parse(timeStr);
				//在原有日期上加上days
				java.util.Date newDate = DateUtil.addDateOneDay(timeDate, days);
				timeStr = dfs.format(newDate);			
			} catch (ParseException e) {
				e.printStackTrace();
			}		
			return timeStr;
		}
		
		
		
	}
	
	public static String getJobString() {
		return null;
	}
	
	public static TimeSchedule getTimeSchedule(String startTime, String endTime) {
		TimeSchedule timeSchedule = new TimeSchedule();
		
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		
		if(null == startTime) {
			
			startTime = DateUtil.format(DateUtil.parse(endTime), "yyyy-MM-dd");
			startTime = startTime + " 00:00:00";
	
		}
		else {
			startTime = DateUtil.format(DateUtil.parse(startTime), "yyyy-MM-dd");
			startTime = startTime + " 00:00:00";
		}
		
		
		try {
			
			java.util.Date begin = dfs.parse(startTime);
			java.util.Date end = dfs.parse(endTime);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

			long days = (between / 60) / (24 * 60);
			long hours = (between / 60) % (24 * 60) / 60;
			long minutes = (between / 60) % 60;
			long seconds = between - days * (24 * 60 * 60) - hours * (60 * 60) - minutes * 60;
			
			timeSchedule.setDates(days);
			timeSchedule.setHour(hours);
			timeSchedule.setMinute(minutes);
			timeSchedule.setSecond(seconds);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		
		return timeSchedule;
	}
	
	
	public static TimeSchedule2 getTimeSchedule2(String startTime, String endTime) {
		TimeSchedule2 timeSchedule = new TimeSchedule2();
		
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		if(null == startTime) {
			
			startTime = DateUtil.format(DateUtil.parse(endTime), "yyyy-MM-dd");
			startTime = startTime + " 00:00:00";
			
		}
		else {
			startTime = DateUtil.format(DateUtil.parse(startTime), "yyyy-MM-dd");
			startTime = startTime + " 00:00:00";
		}
		
		
		try {
			
			java.util.Date begin = dfs.parse(startTime);
			java.util.Date end = dfs.parse(endTime);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			
			long days = (between / 60) / (24 * 60);
			long hours = (between / 60) % (24 * 60) / 60;
			long minutes = (between / 60) % 60;
			long seconds = between - days * (24 * 60 * 60) - hours * (60 * 60) - minutes * 60;
			
			timeSchedule.setDates(days);
			timeSchedule.setHour(hours);
			timeSchedule.setMinute(minutes);
			timeSchedule.setSecond(seconds);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		
		return timeSchedule;
	}
	
	
	public static TimeSchedule getTimeSchedule(String endTime, int runDays) {
		TimeSchedule timeSchedule = new TimeSchedule();
		
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String startTime;
		startTime = DateUtil.format(DateUtil.parse(endTime), "yyyy-MM-dd");
		startTime = startTime + " 00:00:00";
		
//		if(null == startTime) {
//			
//			startTime = DateUtil.format(DateUtil.parse(endTime), "yyyy-MM-dd");
//			startTime = startTime + " 00:00:00";
//	
//		}
//		else {
//			startTime = DateUtil.format(DateUtil.parse(startTime), "yyyy-MM-dd");
//			startTime = startTime + " 00:00:00";
//		}
		
		
		try {
			
			java.util.Date begin = dfs.parse(startTime);
			java.util.Date end = dfs.parse(endTime);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

			long days = (between / 60) / (24 * 60);
			long hours = (between / 60) % (24 * 60) / 60;
			long minutes = (between / 60) % 60;
			long seconds = between - days * (24 * 60 * 60) - hours * (60 * 60) - minutes * 60;
			
			timeSchedule.setDates(runDays);
			timeSchedule.setHour(hours);
			timeSchedule.setMinute(minutes);
			timeSchedule.setSecond(seconds);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		
		return timeSchedule;
	}
	
	public static TimeSchedule2 getTimeSchedule2(String endTime, int runDays) {
		TimeSchedule2 timeSchedule = new TimeSchedule2();
		
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String startTime;
		startTime = DateUtil.format(DateUtil.parse(endTime), "yyyy-MM-dd");
		startTime = startTime + " 00:00:00";
		
//		if(null == startTime) {
//			
//			startTime = DateUtil.format(DateUtil.parse(endTime), "yyyy-MM-dd");
//			startTime = startTime + " 00:00:00";
//	
//		}
//		else {
//			startTime = DateUtil.format(DateUtil.parse(startTime), "yyyy-MM-dd");
//			startTime = startTime + " 00:00:00";
//		}
		
		
		try {
			
			java.util.Date begin = dfs.parse(startTime);
			java.util.Date end = dfs.parse(endTime);
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			
			long days = (between / 60) / (24 * 60);
			long hours = (between / 60) % (24 * 60) / 60;
			long minutes = (between / 60) % 60;
			long seconds = between - days * (24 * 60 * 60) - hours * (60 * 60) - minutes * 60;
			
			timeSchedule.setDates(runDays);
			timeSchedule.setHour(hours);
			timeSchedule.setMinute(minutes);
			timeSchedule.setSecond(seconds);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		
		return timeSchedule;
	}
	
	public static Date parseStringToDate(String time) {
		Date date = null;
		try {
			date = DateUtil.parseDate(time,"yyyy-MM-dd HH:mm");
		} catch (ParseException e) {
			
		}
		return date;
	}
	
	public static String getLastTimeText(TimeSchedule timeSchedule) {
		String lastTimeText = "";
		
		
		if(timeSchedule.getDates() > 0) {
			lastTimeText = lastTimeText + timeSchedule.getDates() + "天";
		}
		
		if(timeSchedule.getHour() < 10) {
			lastTimeText = lastTimeText + "0" + timeSchedule.getHour() + "小时";
		}
		else {
			lastTimeText = lastTimeText + timeSchedule.getHour() + "小时";
		}
		
		if(timeSchedule.getMinute() < 10) {
			lastTimeText = lastTimeText + "0" + timeSchedule.getMinute() + "分钟";
		}
		else {
			lastTimeText = lastTimeText + timeSchedule.getMinute() + "分钟";
		}
		
		return lastTimeText;
				
	}
	public static String getLastTimeText2(TimeSchedule2 timeSchedule) {
		String lastTimeText = "";
		
		
		if(timeSchedule.getDates() > 0) {
			lastTimeText = lastTimeText + timeSchedule.getDates() + "天";
		}
		
		if(timeSchedule.getHour() < 10) {
			lastTimeText = lastTimeText + "0" + timeSchedule.getHour() + "小时";
		}
		else {
			lastTimeText = lastTimeText + timeSchedule.getHour() + "小时";
		}
		
		if(timeSchedule.getMinute() < 10) {
			lastTimeText = lastTimeText + "0" + timeSchedule.getMinute() + "分钟";
		}
		else {
			lastTimeText = lastTimeText + timeSchedule.getMinute() + "分钟";
		}
		
		return lastTimeText;
		
	}
}
