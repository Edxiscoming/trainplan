package com.railway.passenger.transdispatch.util;

import org.apache.commons.logging.Log;

public class LogUtil {
	public static void info(Log logger,String info){
		logger.info(info);
		System.out.println(info);
	}
}
