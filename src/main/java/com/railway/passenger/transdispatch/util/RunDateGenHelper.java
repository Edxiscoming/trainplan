package com.railway.passenger.transdispatch.util;

import java.util.Calendar;

import org.railway.com.trainplan.common.utils.StringUtil;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainAlternate;
import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmTrainRule;

/**
 * 
 * ClassName: RunDateGenHelper
 * @Description: 开行日期生成帮助类
 * @author Administrator
 * @date 2015年9月25日
 */
public class RunDateGenHelper {
		/**
		 * 
		 * @Description: 生成开行日期
		 * @param @param lastRunDate yyyyMMdd
		 * @param @param alternate 交替对象
		 * @param @param rule 开行规律对象
		 * @param @return   
		 * @return String  
		 * @throws
		 * @author Administrator
		 * @date 2015年9月25日
		 */
		public static String generateDate(String lastRunDate, TCmTrainAlternate alternate, TCmTrainRule rule){
			if("".equals(lastRunDate)){
				return alternate.getAlternateDate();
			} else {
				if(!StringUtil.isEmpty(rule.getAppointDay())){
					return appointDayDate(lastRunDate, rule.getAppointDay(), alternate);
				}
				if(!StringUtil.isEmpty(rule.getAppointWeek())){
					return appointWeekDate(lastRunDate, rule.getAppointWeek(), alternate);
				}
				if(!StringUtil.isEmpty(rule.getAppointPeriod())){
					return appointPeriodDate(lastRunDate, rule.getAppointPeriod(), alternate);
				}
				if(!StringUtil.isEmpty(rule.getHighlineRule())){
					return highlineRuleDate(lastRunDate, rule.getHighlineRule(), alternate);
				}
				if(!StringUtil.isEmpty(rule.getCommonlineRule())){
					return commonlineRuleDate(lastRunDate, rule.getCommonlineRule(), alternate);
				}
			}
			return "";
		}
		
		//指定日期开行规律日期生成
		private static String appointDayDate(String lastRunDate, String appointDay, TCmTrainAlternate alternate){
			String[]  appointDays=appointDay.split(",");
			boolean b=TimeUtils.isDate(appointDays[0]);//如果b为true，则为具体的日期开行，如果为false则几号开
			if(b){
				for(int i=0;i<appointDays.length;i++){
					if(lastRunDate.equals(appointDays[i])&&(i!=appointDays.length-1)){
						return appointDays[i+1].toString();
					}else if(lastRunDate.equals(appointDays[i])&&(i==appointDays.length-1)){
						Calendar cal = TimeUtils.date2Calendar(TimeUtils.string2Timestamp(TimeUtils.YYYYMMDD, appointDays[0]));
						cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
						return TimeUtils.date2String(cal.getTime(), TimeUtils.YYYYMMDD);
					}
				}
				
			}else{
				int day=TimeUtils.getDay(lastRunDate,TimeUtils.YYYYMMDD);
				for(int i=0;i<appointDays.length;i++){
					if(Integer.valueOf(appointDays[i].toString())==day&&(i!=appointDays.length-1)){
						int year=TimeUtils.getYear(lastRunDate,TimeUtils.YYYYMMDD);
						int month=TimeUtils.getMonth(lastRunDate,TimeUtils.YYYYMMDD);
						int days=Integer.valueOf(appointDays[i+1].toString());
						String sm="";
						String sd="";
						if(month<10){
							sm="0"+month;
						}else{
							sm=month+"";
						}
						if(days<10){
							sd="0"+days;
						}else{
							sd=days+"";
						}
						return year+sm+sd;
					}else if(Integer.valueOf(appointDays[i].toString())==day&&(i==appointDays.length-1)){
						int year=TimeUtils.getYear(lastRunDate,TimeUtils.YYYYMMDD);
						int month=TimeUtils.getMonth(lastRunDate,TimeUtils.YYYYMMDD);
						int days=Integer.valueOf(appointDays[0].toString());
						String sm="";
						String sd="";
						if(month==11){
							year=year+1;
							sm="0"+1;
						}else if(month<10){
							sm="0"+(month+1);
						}else{
							sm=(month+1)+"";
						}
						if(days<10){
							sd="0"+days;
						}else{
							sd=days+"";
						}
						return year+sm+sd;
					}
				}
			}
			return "";
		}
		//指定星期开行规律日期生成
		private static String appointWeekDate(String lastRunDate, String appointWeek, TCmTrainAlternate alternate){
			String[]  appointDays=new String[7];
			for(int i=0;i<appointWeek.length();i++){
				appointDays[i]=appointWeek.charAt(i)+"";
			}
			int wd=0;
			try {
				wd=TimeUtils.dayForWeek(lastRunDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			int nextDays = 0;
			//上一个开行时间刚好是星期天
			if(wd==7){
				for(int i=0;i<appointDays.length;i++){
					if(Integer.valueOf(appointDays[i].toString())==1){
						nextDays=i+1;
						break;
					}
					
				}
			}
			else if(wd<7){//上一个开行时间不是星期天
				boolean flag=true;
				//如果上一个交替日期不是一个周期的最后一天
				for(int i=wd;i<appointDays.length;i++){
					if(Integer.valueOf(appointDays[i].toString())==1){
						nextDays=nextDays+1+(i-wd);
						flag=false;
						break;
					}
				}
				//如果上一个开行时间是开行规律中最后一个交替时间所以下一个交替时间就
				//要加下这个周剩下的天数的下一个周第一个交替时间之前的天数
				if(flag){
				for(int i=0;i<appointDays.length;i++){
					if(Integer.valueOf(appointDays[i].toString())==1){
						nextDays=i+1;
						break;
					}
					
				}
				nextDays=nextDays+(7-wd);
				}
			}
			return TimeUtils.getNextDateString(lastRunDate, nextDays, TimeUtils.YYYYMMDD);
		}
		//指定周期开行规律日期生成
		private static String appointPeriodDate(String lastRunDate, String appointPeriod, TCmTrainAlternate alternate){
			String[]  appointDays=new String[appointPeriod.length()];
			for(int i=0;i<appointPeriod.length();i++){
				appointDays[i]=appointPeriod.charAt(i)+"";
			}
			int nextDays = 0;
			int dd=TimeUtils.getDaysDiff(alternate.getAlternateDate(),lastRunDate, TimeUtils.YYYYMMDD, null);
			int md=dd%appointDays.length;//算出与第一个交替日期差几天
			int wk=0;//在开行规律里面前面有几天是没有开的
			for(int i=0;i<appointDays.length;i++){
				if(Integer.valueOf(appointDays[i])==0){
					wk=wk+1;
				}else{
					break;
				}
			}
				boolean flag=true;
				//是以lastRunDate为起始开始算
				for(int i=(md+wk+1);i<appointDays.length;i++){
					nextDays=nextDays+1;
					if(Integer.valueOf(appointDays[i])==1){
						flag=false;
						break;
					}
				}
				if(flag){
					nextDays=nextDays+wk+1;
					
				}
			return TimeUtils.getNextDateString(lastRunDate, nextDays, TimeUtils.YYYYMMDD);
		}
		//高线开行规律日期生成
		private static String highlineRuleDate(String lastRunDate, String highlineRule, TCmTrainAlternate alternate){
			return "";
		}
		//普通开行规律日期生成
		private static String commonlineRuleDate(String lastRunDate, String commonlineRule, TCmTrainAlternate alternate){
			//1:每日;2:隔日
			int nextDays = 0;
			if("1".equals(commonlineRule)){
				nextDays = 1;
			}
			if("2".equals(commonlineRule)){
				nextDays = 2;
			}
			return TimeUtils.getNextDateString(lastRunDate, nextDays, TimeUtils.YYYYMMDD);
		}
}
