package com.railway.passenger.transdispatch.util;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PageUtil {
	
	private static Log logger = LogFactory.getLog(PageUtil.class);
	private int rowNumStart = 0;
	private int rowNumEnd = 0;
	public PageUtil(){
		
	}
	public PageUtil(Map<String, Object> map){
		try {
			int pageIndex = Integer.parseInt(String.valueOf(map.get("pageIndex")));
			int pageSize = Integer.parseInt(String.valueOf(map.get("pageSize")));
			this.rowNumStart = (pageIndex - 1) * pageSize + 1;
			this.rowNumEnd = pageIndex * pageSize;
		} catch (Exception e) {
			logger.error("拼装分页信息异常！",e);
		}
		map.put("rownumstart", this.rowNumStart);
		map.put("rownumend", this.rowNumEnd);
		
	}
}
