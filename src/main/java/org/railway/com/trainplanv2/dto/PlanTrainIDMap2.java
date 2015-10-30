package org.railway.com.trainplanv2.dto;


import java.util.concurrent.ConcurrentHashMap;

import org.railway.com.trainplan.entity.runline.ReceiveMsg;

public class PlanTrainIDMap2 {
	public static ConcurrentHashMap<String, ReceiveMsg2> planTrainIDMap = new ConcurrentHashMap<String, ReceiveMsg2>();
	//public static HashMap<String, ReceiveMsg> planTrainIDMap = new HashMap<String, ReceiveMsg>();
}
