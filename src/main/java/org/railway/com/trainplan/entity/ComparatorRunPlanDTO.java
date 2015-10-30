package org.railway.com.trainplan.entity;

import java.util.Comparator;

import org.railway.com.trainplan.web.dto.RunPlanDTO;

/**
 * @author zhangPengDong
 *
 *         2015年4月3日 上午10:03:54
 */
public class ComparatorRunPlanDTO implements Comparator {
	@Override
	public int compare(Object o1, Object o2) {

		RunPlanDTO runPlanDTO1 = (RunPlanDTO) o1;
		RunPlanDTO runPlanDTO2 = (RunPlanDTO) o2;
		// Integer i1 = Integer.parseInt(runPlanDTO1.getStartTime().replace("-",
		// "").replace(":", "").replace(" ", ""));
		// Integer i2 = Integer.parseInt(runPlanDTO2.getStartTime().replace("-",
		// "").replace(":", "").replace(" ", ""));
		
		//suntao   按照jr时间排序
		if (runPlanDTO1.getJrTime() != null
				&& !"".equals(runPlanDTO1.getJrTime())
				&& runPlanDTO2.getJrTime() != null
				&& !"".equals(runPlanDTO2.getJrTime())) {
//			if (runPlanDTO1.getJrTime().compareTo(runPlanDTO2.getJrTime()) == 0) {
//				return runPlanDTO1.getSerial().compareTo(
//						runPlanDTO2.getSerial());
//			} else {
//				return runPlanDTO1.getJrTime().compareTo(
//						runPlanDTO2.getJrTime());
//			}
			return runPlanDTO1.getJrTime().compareTo(runPlanDTO2.getJrTime());
		}
		else {
			return 1;
		}
		//suntao   按照jr时间排序  end
		
		
		//始发时间和车次
//			if (runPlanDTO1.getStartTime()
//					.compareTo(runPlanDTO2.getStartTime()) == 0) {
//				return runPlanDTO1.getSerial().compareTo(
//						runPlanDTO2.getSerial());
//			} else {
//				return runPlanDTO1.getStartTime().compareTo(
//						runPlanDTO2.getStartTime());
//			}
	}
}
