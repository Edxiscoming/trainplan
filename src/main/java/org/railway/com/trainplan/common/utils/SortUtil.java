package org.railway.com.trainplan.common.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.railway.com.trainplan.entity.BaseCrossTrainSubInfo;
import org.railway.com.trainplan.entity.HighlineCrossCmd;
import org.railway.com.trainplan.entity.HighlineCrossInfo;
import org.railway.com.trainplan.entity.PlanTrainCMD;


/**
 * 排序工具类
 * 
 * @author ITC
 * 
 */
public class SortUtil {

	/**
	 * 按照始发时间对列车开行计划排序
	 * 
	 * @param trainList
	 * @return
	 */
	public static List<PlanTrainCMD> sortPlanTrainAsStartTime(
			List<PlanTrainCMD> trainList) {

		Collections.sort(trainList, new Comparator<PlanTrainCMD>() {
			@Override
			public int compare(PlanTrainCMD o1, PlanTrainCMD o2) {
				// TODO
				try {
					if (o1.getStartTime() != null && o2.getStartTime() != null)
						return o1.getStartTime().compareTo(o2.getStartTime());
					else
						return o1.getStartTime() == null ? (o2.getStartTime() == null ? 0
								: 1)
								: -1;

				} catch (Exception ex) {
					return 0;
				}
			}
		});
		return trainList;
	}

	/**
	 * 按照交路名和开行时间对高铁交路计划排序
	 * 
	 * @param crossList
	 * @return
	 */
	public static List<HighlineCrossCmd> sortHighlineCrossAsCrossNameAndStarteDate(
			List<HighlineCrossCmd> crossList) {

		Collections.sort(crossList, new Comparator<HighlineCrossCmd>() {
			@Override
			public int compare(HighlineCrossCmd o1, HighlineCrossCmd o2) {
				// TODO
				try {
					String startDateO1 = o1.getCrossStartDate();
					String startDateO2 = o2.getCrossStartDate();

					if (startDateO1 != null && startDateO2 != null) {
						if (startDateO1.equals(startDateO2)) {
							return o1.getCrossName().compareTo(
									o2.getCrossName());
						} else {
							return startDateO2.compareTo(startDateO1);
						}
					} else
						return startDateO1 == null ? (startDateO2 == null ? 0
								: 1) : -1;

				} catch (Exception ex) {
					return 0;
				}
			}
		});
		return crossList;
	}
	/**
	 * 按照train 的 trainsort 排序 升序
	 * 
	 * @param crossList
	 * @return
	 */
	public static List<BaseCrossTrainSubInfo> sortBaseCrossTrainSubInfo(
			List<BaseCrossTrainSubInfo> list) {
		
		Collections.sort(list, new Comparator<BaseCrossTrainSubInfo>() {
			@Override
			public int compare(BaseCrossTrainSubInfo o1, BaseCrossTrainSubInfo o2) {
				// TODO
				try {
					int startDateO1 = o1.getTrainSort();
					int startDateO2 = o2.getTrainSort();
					
						if (startDateO1==startDateO2) {
							return (o1.getTrainSort()+"").compareTo(
									o2.getTrainSort()+"");
						} else {
							return (startDateO1+"").compareTo(startDateO2+"");
						}
						
				} catch (Exception ex) {
					return 0;
				}
			}
		});
		return list;
	}
	/**
	 * 按照交路名和开行时间对高铁交路计划排序
	 * 
	 * @param crossList
	 * @return
	 */
	public static List<HighlineCrossInfo> sortHighlineCrossAsCrossNameAndStarteDateInfo(
			List<HighlineCrossInfo> crossList) {
		
		Collections.sort(crossList, new Comparator<HighlineCrossInfo>() {
			@Override
			public int compare(HighlineCrossInfo o1, HighlineCrossInfo o2) {
				// TODO
				try {
					String startDateO1 = o1.getCrossStartDate();
					String startDateO2 = o2.getCrossStartDate();
					
					if (startDateO1 != null && startDateO2 != null) {
						if (startDateO1.equals(startDateO2)) {
							return o1.getCrossName().compareTo(
									o2.getCrossName());
						} else {
							return startDateO2.compareTo(startDateO1);
						}
					} else
						return startDateO1 == null ? (startDateO2 == null ? 0
								: 1) : -1;
						
				} catch (Exception ex) {
					return 0;
				}
			}
		});
		return crossList;
	}
}
