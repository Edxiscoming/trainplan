package org.railway.com.trainplan.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.railway.com.trainplan.entity.TrainNbrDwrInfo;

public class CommonUtil {
	public static boolean isContainDoubleTrainNbr(String crossName) {

		return false;

	}

	public static Map<String, TrainNbrDwrInfo> getUnitCrossNameMap(String str) {
		Map<String, TrainNbrDwrInfo> trainNbrMap = new HashMap<String, TrainNbrDwrInfo>();
		boolean isRepetition = false;
		StringTokenizer st = new StringTokenizer(str, "-");
		int count = st.countTokens();
		for (int i = 0; i < (count); i++) {
			String trainNbr = st.nextToken();
			// unitCrossNameList.add(st.nextToken());

			if (trainNbrMap.containsKey(trainNbr)) {
				TrainNbrDwrInfo trainNbrDwrInfo = trainNbrMap.get(trainNbr);
				trainNbrDwrInfo.setTimes(trainNbrDwrInfo.getTimes() + 1);
				trainNbrDwrInfo.getSorts().add(i + 1);
				if (!isRepetition)
					isRepetition = true;
			} else {
				TrainNbrDwrInfo trainNbrDwrInfo = new TrainNbrDwrInfo();
				trainNbrDwrInfo.setTimes(1);
				List<Integer> sorts = new ArrayList<Integer>();
				sorts.add(i + 1);
				trainNbrDwrInfo.setSorts(sorts);
				trainNbrMap.put(trainNbr, trainNbrDwrInfo);
			}
		}

		if (isRepetition) {
			trainNbrMap.put("isRepetition", null);
		}
		return trainNbrMap;
	}

	/**
	 * 查询车辆的次数（根据传递进来的车辆，计算出需要向前查询多少次，向后查询多少次）.
	 * 
	 * @param cross_name
	 * @param train_nbr
	 * @return
	 */
	public static Map<String, Object> getTrainSelCs(String cross_name,
			String train_nbr, Integer train_sort) {
		// 当前车组下共有多少车辆
		String[] nbr = cross_name.split("-");
		// 向前查询的次数
		int before = 0;
		// 向后查询的次数
		// int after = 0;

		// 修改了车序，就不能再按照车名去匹配了，其实需要已总长度 和 当前列车的车序来得到需要的值
		if (train_sort == nbr.length) {
			before = (nbr.length - 1);
		} else if (train_sort == 1) {
			before = 0;
		} else {
			for (int i = 0; i < nbr.length; i++) {
				if (i == train_sort) {
					// 从0开始的
					before = (i - 1);
				}
			}
		}

		// for (int i = 0; i < nbr.length; i++) {
		// if (StringUtils.equals(nbr[i], train_nbr)) {
		// before = i;
		// }
		// }
		// for (int i = 1; i <= (nbr.length-before); i++) {
		// if(StringUtils.equals(nbr[i], train_nbr)){
		// after = i;
		// }
		// }
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("before", before);
		// map.put("after", after);
		map.put("after", nbr.length - before - 1);

		return map;
	}
}
