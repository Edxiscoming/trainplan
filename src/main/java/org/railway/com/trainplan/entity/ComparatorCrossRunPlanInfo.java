package org.railway.com.trainplan.entity;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

public class ComparatorCrossRunPlanInfo implements Comparator {


	/**
	 * 单条数据没问题,多条的情况下,就乱了.
	 * 
	 */
//	@Override
//	public int compare(Object o1, Object o2) {
//		CrossRunPlanInfo crossRunPlanInfo1 = (CrossRunPlanInfo) o1;
//		CrossRunPlanInfo crossRunPlanInfo2 = (CrossRunPlanInfo) o2;
//		Integer i1 = crossRunPlanInfo1.getTrainSort() != null ?crossRunPlanInfo1.getTrainSort() : 0;
//		Integer i2 = crossRunPlanInfo2.getTrainSort() != null ?crossRunPlanInfo2.getTrainSort() : 0;
//		int flag = i1.compareTo(i2);
//		if (flag == 0) {
//			return crossRunPlanInfo1.getTrainNbr().compareTo(
//					crossRunPlanInfo2.getTrainNbr());
//		} else {
//			return flag;
//		}
//	}
	
	@Override
	public int compare(Object o1, Object o2) {
		CrossRunPlanInfo crossRunPlanInfo1 = (CrossRunPlanInfo) o1;
		CrossRunPlanInfo crossRunPlanInfo2 = (CrossRunPlanInfo) o2;
		

		Integer i1 = crossRunPlanInfo1.getTrainSort() != null ?crossRunPlanInfo1.getTrainSort() : 0;
		Integer i2 = crossRunPlanInfo2.getTrainSort() != null ?crossRunPlanInfo2.getTrainSort() : 0;
		
		int flag = crossRunPlanInfo1.getCrossName().compareTo(crossRunPlanInfo2.getCrossName());
//		System.out.println(crossRunPlanInfo1.getPlanCrossId());
//		System.out.println(crossRunPlanInfo2.getPlanCrossId());
//		String str1 = "";
//		String str2 = "";
//		if(StringUtils.isNotEmpty(crossRunPlanInfo1.getPlanCrossId())){
//			str1 = crossRunPlanInfo1.getPlanCrossId();
//		}else if(StringUtils.isNotEmpty(crossRunPlanInfo2.getPlanCrossId())){
//			str2 = crossRunPlanInfo2.getPlanCrossId();
//		}
//		int flag = (str1).compareTo(str2);
		// 
		if (flag == 0) {
			if(i1.compareTo(i2) == 0){
				return (crossRunPlanInfo1.getTrainNbr() != null ?crossRunPlanInfo1.getTrainNbr() : "").compareTo(crossRunPlanInfo2.getTrainNbr() != null ?crossRunPlanInfo2.getTrainNbr() : "");
			}else{
				return i1.compareTo(i2);
			}
			
		} else {
			return flag;
		}
	}

}
