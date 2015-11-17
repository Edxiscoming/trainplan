package org.railway.com.trainplan.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.javasimon.aop.Monitored;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.entity.CrossInfo;
import org.railway.com.trainplan.entity.CrossTrainInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author heyy
 *
 */
@Component
@Transactional
@Monitored
public class CreateUnitCrossTrainUtilService {

	public int addFirstdayGap(CrossInfo cross) {

        int rundays = Days.daysBetween(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(cross.getCrossStartDate()), 
        		DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(cross.getCrossEndDate())).getDays();
        
        if((rundays >= 0) && rundays < cross.getGroupTotalNbr()) {
        	return (cross.getGroupTotalNbr() - rundays);
        }
		
		return -1;
		
	}
	
	//将车次重新排序
	public List<CrossTrainInfo> trainsortSorting(List<CrossTrainInfo> crossTrainInfos, CrossInfo cross) {
		
		int totalTrainNbr = crossTrainInfos.size() / cross.getGroupTotalNbr();
		
		//先核算出第一组车下一轮是哪一天开，得到间隔天数。
		
		String firstGroupEndDate = crossTrainInfos.get(crossTrainInfos.size() - cross.getGroupTotalNbr()).getEndDate();
		String runDate = crossTrainInfos.get(0).getRunDate();
		LocalDate newStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(firstGroupEndDate).
				plusDays(crossTrainInfos.get(0).getDayGap());
		
		LocalDate firstRundate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(runDate);
		
		int intervalDays = Days.daysBetween(firstRundate, newStartDate).getDays();
		
		List<CrossTrainInfo> mergeCrossTrainInfos = new LinkedList<CrossTrainInfo>();
		
		//将原来的crossTrainInfos再复制一份
		List<CrossTrainInfo> newCrossTrainInfos = new LinkedList<CrossTrainInfo>();
		for(CrossTrainInfo crossTrainInfo : crossTrainInfos) {
			CrossTrainInfo c = new CrossTrainInfo();
			try {
				BeanUtils.copyProperties(c, crossTrainInfo);
				/*c.setRunDate(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getRunDate()).
						plusDays(intervalDays).toString().replaceAll("-", ""));
				c.setEndDate(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getEndDate()).
						plusDays(intervalDays).toString().replaceAll("-", ""));*/
				newCrossTrainInfos.add(c);
				
				if(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getRunDate()).compareTo(newStartDate) >= 0) {
					//在这一步将开行日期迁移1中得到的间隔天数
					crossTrainInfo.setRunDate(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getRunDate()).
					minusDays(intervalDays).toString().replaceAll("-", ""));
					crossTrainInfo.setEndDate(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getEndDate()).
					minusDays(intervalDays).toString().replaceAll("-", ""));
					mergeCrossTrainInfos.add(crossTrainInfo);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {				
				e.printStackTrace();
			}
		}
		
		//将第一轮的车在第二轮开始日期之前的车都从list中删除，将新的list接续到这个list里面
		mergeCrossTrainInfos.addAll(newCrossTrainInfos);

		//最后得到的这个list，遍历这个list，以map格式保存，key为（组号 + 车次），一个组号 + 车次只允许出现一次。
		Map<Integer, List<CrossTrainInfo>> crossTrainInfoByGroup = new HashMap<Integer, List<CrossTrainInfo>>();
		//key为（组号 + 车次），一个组号 + 车次只允许出现一次，这样可以保证删除无用的重复车次
		Map<String, Integer> recordTempMap = new HashMap<String, Integer>(); 
		
		for(CrossTrainInfo crossTrainInfo : mergeCrossTrainInfos) {
			//重排trainSort
			if(crossTrainInfoByGroup.containsKey(crossTrainInfo.getGroupSerialNbr())) {
				//重复车次不保存，且重设sort,sort重排，采用序号顺延的形式，且列车list长度不能超过车次总数
				//totalTrainNbr;

				if(!recordTempMap.containsKey(crossTrainInfo.getGroupSerialNbr() + ":" + crossTrainInfo.getTrainNbr())) {
					recordTempMap.put(crossTrainInfo.getGroupSerialNbr() + ":" + crossTrainInfo.getTrainNbr(), 1);
					List<CrossTrainInfo> temp = crossTrainInfoByGroup.get(crossTrainInfo.getGroupSerialNbr());
					crossTrainInfo.setTrainSort(temp.get(temp.size() - 1).getTrainSort() + 1);
					crossTrainInfoByGroup.get(crossTrainInfo.getGroupSerialNbr()).add(crossTrainInfo);
				}
				else if(crossTrainInfoByGroup.get(crossTrainInfo.getGroupSerialNbr()).size() < totalTrainNbr){
					recordTempMap.put(crossTrainInfo.getGroupSerialNbr() + ":" + crossTrainInfo.getTrainNbr(), 
							recordTempMap.get(crossTrainInfo.getGroupSerialNbr() + ":" + crossTrainInfo.getTrainNbr()) + 1);					
					List<CrossTrainInfo> temp = crossTrainInfoByGroup.get(crossTrainInfo.getGroupSerialNbr());
					crossTrainInfo.setTrainSort(temp.get(temp.size() - 1).getTrainSort() + 1);
					crossTrainInfoByGroup.get(crossTrainInfo.getGroupSerialNbr()).add(crossTrainInfo);
				}
				
			}
			else {
				List<CrossTrainInfo> crossTrainInfoByGroups = new LinkedList<CrossTrainInfo>();
				crossTrainInfo.setTrainSort(1);
				crossTrainInfoByGroups.add(crossTrainInfo);
				crossTrainInfoByGroup.put(crossTrainInfo.getGroupSerialNbr(), crossTrainInfoByGroups);
				recordTempMap.put(crossTrainInfo.getGroupSerialNbr() + ":" + crossTrainInfo.getTrainNbr() , 1);
			}
		}
		
		crossTrainInfos.clear();
		Set<Integer> keySet = crossTrainInfoByGroup.keySet();
		Iterator<Integer> it = keySet.iterator();
		//记录每组第一个车，以便计算组间间隔
		CrossTrainInfo firstCrossTrainInGroup = null;
		//记录每组最后一个车，以便计算交路结束时间
		CrossTrainInfo lastEndDateCrossTrain = null;
		//记录这一组需要设置的组间间隔
		int groupDayGap = 0;
		
		while (it.hasNext()) {
			Integer key = it.next();
			for(CrossTrainInfo crossTrainInfo : crossTrainInfoByGroup.get(key)) {
//				System.out.println("goupNbr:" + crossTrainInfo.getGroupSerialNbr() + "   sort:" + crossTrainInfo.getTrainSort() +
//						"  trainNbr:" + crossTrainInfo.getTrainNbr() + "  rundate:" + crossTrainInfo.getRunDate());
				
				//同时重设组间间隔，避免多次循环遍历（直接用时间去排，每一组的首车时间去对比上一组，可以出现负数）
				
				if(1 == crossTrainInfo.getTrainSort()) {
					if(null == firstCrossTrainInGroup){
						firstCrossTrainInGroup = crossTrainInfo;
						groupDayGap = 0;
					}
					else {
						groupDayGap = Days.daysBetween(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(firstCrossTrainInGroup.getRunDate()), 
								DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getRunDate())).getDays();
						
						firstCrossTrainInGroup = crossTrainInfo;
					}
				}
				else if(totalTrainNbr == crossTrainInfo.getTrainSort()) {
					if(null == lastEndDateCrossTrain){
						lastEndDateCrossTrain = crossTrainInfo;
					}
					else {
						if(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getEndDate()).
								compareTo(DateTimeFormat.forPattern("yyyyMMdd").
										parseLocalDate(lastEndDateCrossTrain.getEndDate())) > 0) {
							lastEndDateCrossTrain = crossTrainInfo;
						}
					}
				}
				crossTrainInfo.setGroupGap(groupDayGap);
				crossTrainInfos.add(crossTrainInfo);
			}
			
		}
		
		//重设unitCross的结束时间
		cross.setCrossEndDate(lastEndDateCrossTrain.getEndDate());
		
/*		for(CrossTrainInfo crossTrainInfo : crossTrainInfos) {
			if(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getRunDate()).compareTo(newStartDate) < 0) {
				crossTrainInfos.remove(crossTrainInfo);
			}
			
			//newCrossTrainInfos.add(e)
		}
		
		for(CrossTrainInfo crossTrainInfo : crossTrainInfos) {
			if(DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(crossTrainInfo.getRunDate()).compareTo(newStartDate) < 0) {
				crossTrainInfos.remove(crossTrainInfo);
			}
			
		}
		
		
		for(CrossTrainInfo crossTrainInfo : crossTrainInfos) {
			System.out.println("goupNbr:" + crossTrainInfo.getGroupSerialNbr() + "   sort:" + crossTrainInfo.getTrainSort() +
					"  runtime:" + crossTrainInfo.getSourceTargetTime() + "  rundate:" + crossTrainInfo.getRunDate());
		} */
		
		
		//将原来的crossTrainInfos再复制一份，日期修改为第二轮的日期
		//将第一轮的车在第二轮开始日期之前的车都从list中删除，将新的list接续到这个list里面
		//最后得到的这个list，遍历这个list，以map格式保存，key为（组号 + 车次），一个组号 + 车次只允许出现一次。
		//遍历这个map，按组号分类，存为新的map<key,list>
		//遍历这个map,重排trainSort,这样每一组车的trainsort就排好了
		//重设组间间隔（直接用时间去排，每一组的首车时间去对比上一组，可以出现负数）,同时在这一步将开行日期迁移1中得到的间隔天数。
		//设置完组间间隔后，遍历这个map,将所有车放入一个list，并获取新的交路结束时间
		//重设unitCross的开始时间和结束时间
		
		return null;
	}
}
