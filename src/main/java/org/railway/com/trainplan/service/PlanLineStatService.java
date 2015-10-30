package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javasimon.aop.Monitored;
import org.joda.time.format.DateTimeFormat;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.entity.PlanLineHourCount;
import org.railway.com.trainplan.repository.mybatis.PlanLineStatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class PlanLineStatService {
    @Autowired
	private PlanLineStatDao planLineStatDao;
	
	public List<PlanLineHourCount> getBanPlanLineStat() {
		Map<String, String> dateParam = new HashMap<String, String>();
		//根据当前时间，获取之前12个班的时间
		String nowDate = DateUtil.getStringFromDate(new Date(), "yyyy-MM-dd HH");		
		//根据当前时刻，判断出应该从哪个班取到哪个班
		int nowHour = Integer.parseInt(nowDate.substring(11));
		
		String startDate;
		String endDate;
		if(nowHour < 18 && nowHour >= 6) {
			startDate = DateTimeFormat.forPattern("yyyy-MM-dd HH").parseLocalDate(nowDate).minusDays(12).toString() + " 06";
			endDate = nowDate.substring(0, 10) + " 05";
		}
		else {
			startDate = DateTimeFormat.forPattern("yyyy-MM-dd HH").parseLocalDate(nowDate).minusDays(12).toString() + " 18";
			endDate = nowDate.substring(0, 10) + " 17";
		}
		
		dateParam.put("startDate", startDate);
		dateParam.put("endDate", endDate);
		//将查询出的数据，12个一组进行组装
		List<PlanLineHourCount> planLineStatList = planLineStatDao.getHourCount(dateParam);
		List<PlanLineHourCount> planLineStatListnew = new ArrayList<PlanLineHourCount>();
		
		long count = 0;
		String time = null;
		for(int i=0; i<=planLineStatList.size(); i++) {			
			if((i % 12) == 0) {
				if(i != 0) {
					PlanLineHourCount planLineHourCount = new PlanLineHourCount();
					planLineHourCount.setCount(count);
					planLineHourCount.setTime(time);
					planLineStatListnew.add(planLineHourCount);
					count = 0;
				}
				if(i == planLineStatList.size()) {
					break;
				}
				else {
					time = planLineStatList.get(i).getTime();
					count = count + planLineStatList.get(i).getCount();
				}
				
			}
			
		}
		
		return planLineStatListnew;
		
	}
}
