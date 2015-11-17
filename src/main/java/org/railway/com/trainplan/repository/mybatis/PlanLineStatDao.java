package org.railway.com.trainplan.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.entity.PlanLineHourCount;
import org.railway.com.trainplan.entity.PlanLineStat;




/**
 * 生成运行线的统计
 * Created by sg on 2/9/15.
 */
 
public interface PlanLineStatDao {

	int insertStat(PlanLineStat planLineStat);
	
	List<PlanLineHourCount> getHourCount();
	
	List<PlanLineHourCount> getHourCount(Map<String, String> resParam);
}
