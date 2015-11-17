package org.railway.com.trainplan.common.utils;

import java.util.UUID;

/**
 * 
 * @author zhangPengDong
 *
 *         2015年6月9日 下午2:38:48
 */
public class IdUtil {

	/**
	 * by UUID.
	 * 
	 * @return
	 */
	public static String getIdByUUID() {
		return UUID.randomUUID().toString();
	}

}
