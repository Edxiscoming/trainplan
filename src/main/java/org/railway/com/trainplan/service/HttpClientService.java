package org.railway.com.trainplan.service;

import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.OperationConstants;
import org.railway.com.trainplan.common.utils.ClientUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HttpClientService {
	private static Logger log = Logger.getLogger(HttpClientService.class);
	//连接超时时间（毫秒）
	private int timeout = 20000;
	
	public String sendHttpClient(String url, String params, OperationConstants.REQUEST_METHOD method) throws Exception {
		String myResponse = "";
		try {
			log.debug("HttpClientService sendHttpClient");
			log.debug("url: " + url);
			log.debug("params: " + params);
			myResponse = ClientUtil.excuteRequest(url, params, method, timeout);
			log.debug("response: " + myResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return myResponse;
		
	}
}
