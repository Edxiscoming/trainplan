package org.railway.com.trainplan.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.javasimon.aop.Monitored;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@Monitored
@Scope("singleton")
public class MessageHandlerThreadPool {
	private ExecutorService executorService = null;
	
	@Value("#{restConfig['trainline.msghandler.thread']}")
	private int threadNbr;

	//通过spring，配置一个单例的线程池
	MessageHandlerThreadPool() {
		//executorService = Executors.newFixedThreadPool(threadNbr);
	}
	
	@PreDestroy
	public void destroy() {
		executorService.shutdown();
	}
	
	public ExecutorService getExecutorService() {
		
    	if(null == executorService) {
			synchronized(MessageHandlerThreadPool.class){ 
				if(null == executorService) {
					executorService = Executors.newFixedThreadPool(threadNbr);
				}
			}
    	}

		return executorService;
	}

}	
