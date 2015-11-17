package com.railway.passenger.transdispatch.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.entity.BaseTrainInfo;
import org.railway.com.trainplan.service.ShiroRealm;

import com.railway.passenger.transdispatch.comfirmedmap.entity.TCmCross;

/**
 * 
 * ClassName: TcmPhyCrossInfoGenHelper
 * @Description: 交路信息生成帮助类(逻辑交路、物理交路、计划交路)
 * @author Administrator
 * @date 2015年9月25日
 */
public class CrossInfoGenHelper {
	private static final ConcurrentMap<String, Date> planLock = new ConcurrentHashMap<String, Date>();
	
	/**
	 * 
	 * @Description: 判断能否进行开行计划生成
	 * @param @param cross
	 * @param @return   
	 * @return boolean  true-能 false-不能
	 * @throws
	 * @author qs
	 * @date 2015年10月15日
	 */
	public static boolean canGeneratePlan(TCmCross cross){
		boolean flag = false;
		if(!planLock.containsKey(cross.getCmCrossId())){
			flag = true;
		} else {
			long lockDate = planLock.get(cross.getCmCrossId()).getTime();
			long nowDate = new Date().getTime();
			//30分钟超时时间判定
			if(nowDate - lockDate >= 30*60*1000){
				flag = true;
			}
		}
		return flag;
	}
	
	public static void lockPlan(TCmCross cross){
		planLock.put(cross.getCmCrossId(), new Date());
	}
	
	public static void unlockPlan(TCmCross cross){
		planLock.remove(cross.getCmCrossId());
	}
	
	public static short getNextTrainSort(short currentTrainSort,int totaltrainNbrs){
		if(currentTrainSort == totaltrainNbrs){
			return 1;
		} else {
			return ++currentTrainSort;
		}
	}
	
	public static short getPreTrainSort(short currentTrainSort,int totalTrainNbrs){
		if(currentTrainSort == 1){
			return (short)totalTrainNbrs;
		} else {
			return --currentTrainSort;
		}
	}
	
	//根据逻辑车路id以及物理交路车次开行时间获取该车次的起始和结束日期
	public static Map<String, Date> getStartAndEndDateByBaseTrain(BaseTrainInfo baseTrain, String runDate){
		Map<String, Date> dateMap = new HashMap<String, Date>();
		int runDays = baseTrain.getRunDays();
		dateMap.put("startTime", TimeUtils.string2Timestamp("yyyyMMdd HH:mm:ss", runDate + " " + baseTrain.getStartTime()));
		Calendar cal = TimeUtils.timestamp2Calendar(TimeUtils.string2Timestamp("yyyyMMdd HH:mm:ss", runDate + " " + baseTrain.getEndTime()));
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + runDays);
		dateMap.put("endTime", TimeUtils.date2Timestamp(cal.getTime()));
		return dateMap;
	}
	
	public static String[] analyseRules(String [] rules,int length){
		String [] result = new String[length];
		if(rules.length == 1){
			for(int i = 0;i < length;i++){
				result[i] = rules[0];
			}
		}
		if(rules.length == length){
			return rules;
		}
		return result;
	}
	
	public static ShiroRealm.ShiroUser getUser(){
		ShiroRealm.ShiroUser user = null;
		try{
			user = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
		}catch(Exception e){
			user = new ShiroRealm.ShiroUser("autoGenerate", "autoGenerate", 1);
		}
		return user;
	}
}
