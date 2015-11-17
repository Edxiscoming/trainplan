package org.railway.com.trainplan.common.constants;


import java.util.concurrent.ConcurrentHashMap;

import org.railway.com.trainplan.entity.runline.ReceiveMsg;

public class PlanTrainIDMap {
	public static ConcurrentHashMap<String, ReceiveMsg> planTrainIDMap = new ConcurrentHashMap<String, ReceiveMsg>();
	//public static HashMap<String, ReceiveMsg> planTrainIDMap = new HashMap<String, ReceiveMsg>();
}
