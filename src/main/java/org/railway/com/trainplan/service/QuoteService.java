/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.railway.com.trainplan.service;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.service.dto.Quote;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
@Monitored
@Transactional
public class QuoteService  {

	private static Log logger = LogFactory.getLog(QuoteService.class);
	@Autowired
	private SendMsgService sendMsgService;
	
   

	/**
	 * 
	 * @param schemeId
	 * @param startDate
	 * @param dayIndex
	 * @param trainCount  列车总数
	 * @param messageType
	 */
	public void sendQuotes(String startDate,int dayIndex,int trainCount,String messageType) throws Exception{
		Quote quote = new Quote();
		quote.setRundate(startDate);
		quote.setTraincount(trainCount);
		quote.setDayindex(String.valueOf(dayIndex));
		ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(quote);
		String pageUrl = "/trainplan/default/transfer/plan/planView";
		//处理一天开始
		if("plan.day.begin".equals(messageType)){
			quote.setRefreshtime(DateUtil.getStringTimestamp(System.currentTimeMillis()));
			//推一个开始的消息
			this.sendMsgService.sendMessage(message, pageUrl, "importPlanDayBegin");
			
		}else if("plan.day.end".equals(messageType)){
			//处理一天的数据结束
			 
			quote.setRefreshtime(DateUtil.getStringTimestamp(System.currentTimeMillis()));
			message = objectMapper.writeValueAsString(quote);
			this.sendMsgService.sendMessage(message, pageUrl, "importPlanDayEnd");
		}else if("plan.end".equals(messageType)){
			
			//全部处理完成
			this.sendMsgService.sendMessage(message, pageUrl, "importPlanEnd");
			//System.err.println("message333==" + message);
			
		}else if("plan.getInfo.begin".equals(messageType)){
			//调用后台接口之前
			this.sendMsgService.sendMessage(message, pageUrl, "importPlanBegin");
			
		}
	}

}
