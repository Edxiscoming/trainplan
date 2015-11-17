package org.railway.com.trainplan.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.CreateRunLineDate;
import org.railway.com.trainplan.entity.TrainNbrAndStartTime;
import org.railway.com.trainplan.entity.UnitCross;
import org.railway.com.trainplan.entity.UnitCrossTrain;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生成运行线辅助服务类
 * @author heyy
 *
 */
@Component
@Monitored
@Transactional
public class RunPlanCreateAuxiliaryService {
	 private static final Log logger = LogFactory.getLog(RunPlanCreateAuxiliaryService.class);
	/**
	 * 
	 * @param startDate
	 * @param days
	 * @param appointWeek
	 * @return
	 * @throws ParseException
	 * 这个方法不能自动补全交路，暂不使用
	 * 
	
	public List<CreateRunLineDate> createRunPlanByAppointWeek(String startDate, int days, String appointWeek ) throws ParseException {
		//String weekDay = DateUtil.getWeekOfDate(DateUtil.parse(startDate), 1);
		
		//将一周的开行情况使用boolean类型进行记录	
		List<Boolean> appointWeekDays = new ArrayList<Boolean>();
		if(appointWeek.length() == 7) {
			for (int n=0; n<7; n++) {
				if ('1' == appointWeek.charAt(n)) {
					appointWeekDays.add(true);
				}
				else {
					appointWeekDays.add(false);
				}
			}
		}
		else return null;
		
		
		List<CreateRunLineDate> createRunLineDateList = new ArrayList<CreateRunLineDate>();
		Date startDateTemp = DateUtil.parseDate(startDate, "yyyyMMdd");
		//这个值用来记录一次（一个开始时间）需要生成多少天的开行计划
		int m = 0;
		Date startDateRecord = startDateTemp;
//		if(!isCreateRunPlan(DateUtil.getWeekOfDate(startDateTemp, 1))) {
//			m = 1;
//		}
		
		for(int i=0; i<days; i++) {
		
			String weekDay = DateUtil.getWeekOfDate(startDateTemp, 1);					
			if(isCreateRunPlan(weekDay, appointWeekDays)) {
				m++;			
			}
			else {
				if(m != 0) {
					CreateRunLineDate createRunLineDate = new CreateRunLineDate();
					
					//记录m
					createRunLineDate.setDays(m);
					//记录startDate
					createRunLineDate.setStartDate(DateUtil.format(startDateRecord, "yyyyMMdd"));
					
					createRunLineDateList.add(createRunLineDate);
									
				}
				//重置m
				m = 0;
				//重置startDateRecord
				startDateRecord = DateUtil.addDateOneDay(startDateTemp);
			}		
			startDateTemp = DateUtil.addDateOneDay(startDateTemp);
		}
		return createRunLineDateList;
		
	}
	
	 */
	 
	 /**
	  * 获取车次，并根据起始时间和车次交替日期，获取每个车次对应的起始时间
	  * @param startDateStr
	  * @param unitCross
	  * @return
	  */
	 public List<TrainNbrAndStartTime> getTrainNbrAndStartTime(String startDateStr, UnitCross unitCross) {
		LocalDate startDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDateStr);
		
		
		List<String> trainNbrs = getAllTrainNbr(unitCross.getCrossName());
		List<String> dates = getAllTrainDate(unitCross.getAlternateDate());
		
		if(trainNbrs.size() != dates.size()) {
			return null;
		}
		else {
			List<TrainNbrAndStartTime> trainNbrAndStartTimes = new ArrayList<TrainNbrAndStartTime>();
			TrainNbrAndStartTime trainNbrAndStartTime1 = new TrainNbrAndStartTime();
			trainNbrAndStartTime1.setTrainNbr(trainNbrs.get(0));
			trainNbrAndStartTime1.setStartDate(startDateStr);
			trainNbrAndStartTimes.add(trainNbrAndStartTime1);
			
			LocalDate standard = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(dates.get(0));
			
			for(int i=1; i<trainNbrs.size(); i++) {
				LocalDate currentDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(dates.get(i));
				
				if(currentDate.compareTo(standard) > 0) {
					int internalDays = Days.daysBetween(standard, currentDate).getDays();
					
					String dateStr = startDate.plusDays(internalDays).toString().replace("-", "");
					
					TrainNbrAndStartTime trainNbrAndStartTime = new TrainNbrAndStartTime();
					trainNbrAndStartTime.setTrainNbr(trainNbrs.get(i));
					trainNbrAndStartTime.setStartDate(dateStr);
					trainNbrAndStartTimes.add(trainNbrAndStartTime);
				}
				else if(currentDate.compareTo(standard) < 0){
					int internalDays = Days.daysBetween(currentDate, standard).getDays();
					
					String dateStr = startDate.minusDays(internalDays).toString().replace("-", "");
					
					TrainNbrAndStartTime trainNbrAndStartTime = new TrainNbrAndStartTime();
					trainNbrAndStartTime.setTrainNbr(trainNbrs.get(i));
					trainNbrAndStartTime.setStartDate(dateStr);
					trainNbrAndStartTimes.add(trainNbrAndStartTime);
				}
				else {
					TrainNbrAndStartTime trainNbrAndStartTime = new TrainNbrAndStartTime();
					trainNbrAndStartTime.setTrainNbr(trainNbrs.get(i));
					trainNbrAndStartTime.setStartDate(startDateStr);
					trainNbrAndStartTimes.add(trainNbrAndStartTime);
				}				
			}
			
			return trainNbrAndStartTimes;
		}
	
	 }
	
	/**
	 * 
	 * @param startDate
	 * @param days
	 * @param appointWeek
	 * @return
	 * @throws ParseException
	 * 该方法可以补全交路，星期规律中的某一天只是代表这一天是一个开始时间。
	 */
	public List<CreateRunLineDate> createRunPlanByAppointWeek(String startDate, int days, String appointWeek, UnitCross unitCross) throws ParseException {
		//String weekDay = DateUtil.getWeekOfDate(DateUtil.parse(startDate), 1);
		
		//一个交路单元完整运行一次所需的时间
		int runDays = getIntervalDays(unitCross.getCrossStartDate(), unitCross.getCrossEndDate()) + 1;
		
		//将一周的开行情况使用boolean类型进行记录	
		List<Boolean> appointWeekDays = new ArrayList<Boolean>();
		if(appointWeek.length() == 7) {
			for (int n=0; n<7; n++) {
				if ('1' == appointWeek.charAt(n)) {
					appointWeekDays.add(true);
				}
				else {
					appointWeekDays.add(false);
				}
			}
		}
		else return null;
		
		
		List<CreateRunLineDate> createRunLineDateList = new ArrayList<CreateRunLineDate>();
		Date startDateTemp = DateUtil.parseDate(startDate, "yyyyMMdd");
		//这个值用来记录一次（一个开始时间）需要生成多少天的开行计划
		//不需要了，现在只需要一个开始时间，days是固定的了，就是一个交路的完整运行时间,忽略这个值
//		int m = 0;
//		Date startDateRecord = startDateTemp;
//		if(!isCreateRunPlan(DateUtil.getWeekOfDate(startDateTemp, 1))) {
//			m = 1;
//		}
		
		for(int i=0; i<days; i++) {
		
			String weekDay = DateUtil.getWeekOfDate(startDateTemp, 1);
			if(isCreateRunPlan(weekDay, appointWeekDays)) {
				//m++;
				CreateRunLineDate createRunLineDate = new CreateRunLineDate();				
				createRunLineDate.setDays(runDays);
				createRunLineDate.setStartDate(DateUtil.format(startDateTemp, "yyyyMMdd"));				
				createRunLineDateList.add(createRunLineDate);
			}
			startDateTemp = DateUtil.addDateOneDay(startDateTemp);
		}
		/*if(1 == unitCross.getGroupTotalNbr()) {
			return createRunLineDateList;
		}
		else {*/
			return getContinuousDate(createRunLineDateList, runDays);
		//}
	
	}
	
	//对星期规律的createRunLineDateList进行处理，找出连续生成的日期，直接使用开头天+连续天数
	private List<CreateRunLineDate> getContinuousDate(List<CreateRunLineDate> createRunLineDateList, int runDays) {
		List<CreateRunLineDate> createRunLineDateListNew = new ArrayList<CreateRunLineDate>();
		if(!createRunLineDateList.isEmpty()){
			CreateRunLineDate currentCreateRunLineDate = createRunLineDateList.get(0);
			LocalDate runPlanStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(currentCreateRunLineDate.getStartDate());
			int days = 1;
			
			for(int i=1; i<createRunLineDateList.size(); i++) {
				
				if(0 == runPlanStartDate.plusDays(days).compareTo(DateTimeFormat.forPattern("yyyyMMdd").
						parseLocalDate(createRunLineDateList.get(i).getStartDate()))) {
					days = days + 1;
				}
				else {
					//如果天数小于一个完整交路跑完的天数，则忽略该days，使用一个完整交路跑完的天数
					currentCreateRunLineDate.setDays(runDays);
					createRunLineDateListNew.add(currentCreateRunLineDate);
					
					runPlanStartDate = DateTimeFormat.forPattern("yyyyMMdd").
							parseLocalDate(createRunLineDateList.get(i).getStartDate());
					currentCreateRunLineDate = createRunLineDateList.get(i);
					
					days = 1;
				}
						
			}
			
			if(createRunLineDateListNew.isEmpty()) {
				currentCreateRunLineDate.setDays(days);
				createRunLineDateListNew.add(currentCreateRunLineDate);
			}
			else {
				createRunLineDateListNew.add(createRunLineDateList.get(createRunLineDateList.size() - 1));
			}
		}
		return createRunLineDateListNew;
		
	}
	
	//判断该规律周期内，列车是否只开行一次
	public boolean isGenerationOnce(UnitCross unitCross) {
		if(getMaxSerialDays(unitCross.getAppointWeek()) == unitCross.getGroupTotalNbr()) {
			return true;
		}
		
		return false;
		
	}
	
	//是否可以根据星期规律生成开行计划
	public boolean isGenerationByWeek(UnitCross unitCross) {
		//暂时不验证了
		return true;
		
/*		*//**
		 * 判断算法：先判断一个完整的交路单元跑完需要多少天，如果星期规律里，两个开行日期的间隔天数小于一个交路跑完
		 * 的最小天数，则说明数据有问题，不能按星期生成开行计划。
		 *//*
		
		//一个交路单元完整运行一次所需的时间
		//int runDays = getUnitCrossRunDays(unitCross.getCrossStartDate(), unitCross.getCrossEndDate());
		
		//因为是以第一组车的第一个车为计算点，所以运行周期应该以第一组车的第一个车来计算
		int runDays = -1;
		List<UnitCrossTrain> unitCrossTrains = new ArrayList<UnitCrossTrain>();
		for(UnitCrossTrain unitCrossTrain : unitCross.getUnitCrossTrainList()) {
			if(1 == unitCrossTrain.getGroupSerialNbr()) {					
				unitCrossTrains.add(unitCrossTrain);
			}
		}

		runDays = getDays(unitCrossTrains);
		
		String appointWeek = unitCross.getAppointWeek();
		int minDays = getMinInterValDays(appointWeek);
		
		if(minDays >= 7 || -1 == runDays) {
			//表示一个星期一天都不生成
			return false;
		}
		else if(minDays >= (runDays - 1)) {
			//最小间隔天数大于等于一个交路运行的完整天数，则可按星期规律生成开行计划
			return true;
		}
		else {
			return false;
		}*/
					
	}
	
	public List<CreateRunLineDate> createRunPlanByAppointDay(String startDate, int days, String appointDay, UnitCross unitCross) {
		try {
			List<CreateRunLineDate> createRunLineDateList = new ArrayList<CreateRunLineDate>();
			LocalDate runPlanStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDate);
			LocalDate runPlanEndDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(startDate).plusDays(days);
			
			List<String> dates = assemblyDate(runPlanStartDate, runPlanEndDate, getDateList(appointDay));
			
			int runDays = -1;
/*			List<UnitCrossTrain> unitCrossTrains = new ArrayList<UnitCrossTrain>();
			for(UnitCrossTrain unitCrossTrain : unitCross.getUnitCrossTrainList()) {
				if(1 == unitCrossTrain.getGroupSerialNbr()) {					
					unitCrossTrains.add(unitCrossTrain);
				}
			}*/

			runDays = getIntervalDays(unitCross.getCrossStartDate(), unitCross.getCrossEndDate());
			
			if(null != dates && !dates.isEmpty()) {
				for(String dateStr : dates) {
					CreateRunLineDate createRunLineDate = new CreateRunLineDate();				
					createRunLineDate.setDays(runDays + 1);
					createRunLineDate.setStartDate(dateStr);				
					createRunLineDateList.add(createRunLineDate);
				}
			}
			else {
				return null;
			}
			/*if(1 == unitCross.getGroupTotalNbr()) {
				return createRunLineDateList;
			}
			else {*/
				//return getContinuousDate(createRunLineDateList, runDays);
			return createRunLineDateList;
			//}
			//return createRunLineDateList;
		}catch(Exception e) {
			logger.error("不是一个合法的日期 error: " + e);
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
	//对指定日期规律的createRunLineDateList进行处理，找出连续生成的日期，直接使用开头天+连续天数
	private List<CreateRunLineDate> getContinuousDateForAppointDay(List<CreateRunLineDate> createRunLineDateList, int runDays) {
		List<CreateRunLineDate> createRunLineDateListNew = new ArrayList<CreateRunLineDate>();
		CreateRunLineDate currentCreateRunLineDate = createRunLineDateList.get(0);
		LocalDate runPlanStartDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(currentCreateRunLineDate.getStartDate());
		int days = 1;
		
		for(int i=1; i<createRunLineDateList.size(); i++) {
			
			if(0 == runPlanStartDate.plusDays(days).compareTo(DateTimeFormat.forPattern("yyyyMMdd").
					parseLocalDate(createRunLineDateList.get(i).getStartDate()))) {
				days = days + 1;
			}
			else {
				//如果天数小于一个完整交路跑完的天数，则忽略该days，使用一个完整交路跑完的天数
				currentCreateRunLineDate.setDays(days);
				createRunLineDateListNew.add(currentCreateRunLineDate);
				
				runPlanStartDate = DateTimeFormat.forPattern("yyyyMMdd").
						parseLocalDate(createRunLineDateList.get(i).getStartDate());
				currentCreateRunLineDate = createRunLineDateList.get(i);
				
				days = 1;
			}
			
			
		}
			
		return createRunLineDateListNew;		
	}

	//根据星期，判断这一天是否需要生成开行计划
	private boolean isCreateRunPlan(String weekDay, List<Boolean> appointWeekDays) {
		switch(weekDay) {
		
		case "1" :
			return appointWeekDays.get(0);
		case "2" :
			return appointWeekDays.get(1);
		case "3" :
			return appointWeekDays.get(2);
		case "4" :
			return appointWeekDays.get(3);
		case "5" :
			return appointWeekDays.get(4);
		case "6" :
			return appointWeekDays.get(5);
		case "7" :
			return appointWeekDays.get(6);
		default : 
			return false;
			
		}
	}
	
	//获取一个星期规律中，两个开行日期间的最小间隔天数
	private int getMinInterValDays(String appointWeek) {
		if(appointWeek.contains("1")) {
			int[] aStr = new int[appointWeek.length()+1];
			for(int i=0;i<appointWeek.length();i++){
				aStr[i] = Integer.parseInt(appointWeek.substring(i,i+1));
			}
			aStr[appointWeek.length()] = aStr[0];
			int minDay=6;
			for (int i=0;i<aStr.length;i++) {
				if(aStr[i]!=1){
					continue;
				}
				for (int j=i+1;j<aStr.length;j++) {
					if(aStr[j]==1){
						int minDayTemp = j-i-1;
						if(minDayTemp<minDay){
							minDay = minDayTemp;
						}
						break;
					}
				}
			}
			return minDay;
		}
		else {
			return 7;
		}

	}
	
	//获取最大连续天数
	private int getMaxSerialDays(String appointWeek) {
		int days = 0;
		int m = 0;
		if(appointWeek.contains("1")) {
			for(int i=0; i<appointWeek.length(); i++) {
				if('1' == appointWeek.charAt(i)) {
					m = m + 1;
				}
				else {
					if(days < m)
						days = m;
					m = 0;
				}
			}
		}
		if(days == 0 && m != 0) {
			days = m;
		}
		
		return days;
		
	}
	
	
	//获取一个交路单元完整运行一次所需的时间
	private int getIntervalDays(String startDate, String endDate) {
		int intervalDays = -1;
		
		//转化日期格式20141205转化为2014-12-05
		StringBuilder startDateSb=new StringBuilder(startDate);
		StringBuilder endDateSb=new StringBuilder(endDate);
		
		startDateSb.insert(4, '-');
		startDateSb.insert(7, '-');
		
		endDateSb.insert(4, '-');
		endDateSb.insert(7, '-');
		
		//一个交路单元完整运行一次所需的时间
		intervalDays = DateUtil.getDaysBetween(startDateSb.toString(), endDateSb.toString());
		
		return intervalDays;
	}
	
	private int getDays(List<UnitCrossTrain> unitCrossTrains) {
		
		String startDate = unitCrossTrains.get(0).getRunDate();
		String endDate = unitCrossTrains.get(unitCrossTrains.size() - 1).getEndDate();
		int days = getIntervalDays(startDate, endDate);
		
		return days;		
	}
	
	private List<Integer> getDatenums(String dateStr) {
		dateStr.replaceAll("，", ",");
		List<String> dateList = StringUtil.getDateStr(dateStr);
		if(null != dateList && !dateList.isEmpty()) {
			List<Integer> dateNums = new ArrayList<Integer>();
			for(String s : dateList) {
				int i = Integer.valueOf(s);
				if(i>0 && i<31) {
					dateNums.add(Integer.valueOf(s));
				}
				else {
					return null;	
				}
				
			}
			return dateNums;
		}	
		return null;		
	}
	
	private List<String> getDateList(String dateStr) {
		dateStr.replaceAll("，", ",");
		List<String> dateList = StringUtil.getDateStr(dateStr);
		
		return dateList;	
	}
	
	private List<String> assemblyDate(LocalDate runPlanStartDate, LocalDate runPlanEndDate, List<String> dateDays) {
		List<String> dates = new ArrayList<String>();
		//判断这个时间间隔有几年，几个月
		int startYear = runPlanStartDate.getYear();
		int startMounth = runPlanStartDate.getMonthOfYear();
		int endYear = runPlanEndDate.getYear();
		int endMounth = runPlanEndDate.getMonthOfYear();
		
		int intervalYear = endYear - startYear;
		int intervalMounth = endMounth - startMounth;
		
		String runPlanStartYear = runPlanStartDate.toString().replaceAll("-", "").substring(0, 4);
		String runPlanStartMounth = runPlanStartDate.toString().replaceAll("-", "").substring(4, 6);
		List<String> yearAndMounths = new ArrayList<String>();
		
		//组合一个字符串组，里面包含了所有可能的年份和月份，然后利用这个字符串组，去和日期拼接，得到所有日期
		
		if(intervalYear > 0) {
			intervalMounth = 12 * intervalYear + intervalMounth;			
			for(int i=0; i<intervalMounth + 1; i++) {
				yearAndMounths.add(runPlanStartDate.plusMonths(i).toString().replaceAll("-", "").substring(0, 6));
			}
		}
		else {
			if(intervalMounth > 0) {
				for(int i=0; i<intervalMounth + 1; i++) {
					yearAndMounths.add(runPlanStartDate.plusMonths(i).toString().replaceAll("-", "").substring(0, 6));
				}
			}
			else {
				yearAndMounths.add(runPlanStartYear + runPlanStartMounth);
			}
			
		}
		
		if(!yearAndMounths.isEmpty()) {
			int i = 0;
			for(String yearAndMounth : yearAndMounths) {
				
				for(String day : dateDays) {
					if(8 == day.length()) {
						if(0 == i) {
							try {	
								LocalDate nowDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(day);
								if(nowDate.compareTo(runPlanStartDate) >= 0 && nowDate.compareTo(runPlanEndDate) <= 0)
									dates.add(day);
							}catch(Exception e) {
								logger.error("不是一个合法的日期 error: " + e);
								e.printStackTrace();
							}
						}
					
					}
					else {
						if(1 == day.length()) {
							day = "0" + day;
						}
						try {	
							LocalDate nowDate = DateTimeFormat.forPattern("yyyyMMdd").parseLocalDate(yearAndMounth + day);
							if(nowDate.compareTo(runPlanStartDate) >= 0 && nowDate.compareTo(runPlanEndDate) <= 0)
								dates.add(yearAndMounth + day);
						}catch(Exception e) {
							logger.error("不是一个合法的日期 error: " + e);
							e.printStackTrace();
						}
					}					
				
				}
				i++;
			}
			
		}

		return dates;
		
	}
	
	private List<String> getAllTrainNbr(String crossName) {
		return StringUtil.getRegexInfos(crossName, "-");		
	}
	
	private List<String> getAllTrainDate(String alternateDate) {
		return StringUtil.getRegexInfos(alternateDate, "-");		
	}
}

