package org.railway.com.trainplan.common.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 审核时需要的一些校验.
 * 
 * @author zhangPengDong
 *
 *         2015年3月24日 下午3:16:22
 */
public class CheckUtil {

	/**
	 * 校验：发站和到站是否为空.
	 * 
	 * @param startStn
	 *            发站.
	 * @param endStn
	 *            到站.
	 */
	public static boolean checkStartEndStnIsNotNull(String startStn, String endStn) {
		if (null != startStn && !"".equals(startStn) && null != endStn
				&& !"".equals(endStn)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验：第一趟车的到站是否与下一趟车的发站相等.
	 * 
	 * @param startStn
	 *            发站.
	 * @param endStn
	 *            到站.
	 */
	public static boolean checkStartEndStnIsEquals(String endStn,String startStn) {
		if(StringUtils.equals(endStn, startStn)){
			return true;
		}
		return false;
	}

}
