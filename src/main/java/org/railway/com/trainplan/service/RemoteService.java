package org.railway.com.trainplan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.common.utils.RestClientUtils;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.service.dto.PlanBureauStatisticsDto;
import org.railway.com.trainplan.service.dto.PlanBureauTsDto;
import org.railway.com.trainplan.service.dto.SchemeDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.dto.TrainlineTemplateSubDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
/**
 * 调用远程接口服务的service
 * @author join
 *
 */
@Component
@Transactional
@Monitored
public class RemoteService {
	private static final Logger logger = Logger.getLogger(RemoteService.class);

	@Value("#{restConfig['SERVICE_URL']}")
    private String restUrl;
	
	
	/**
	 * 6.1.1	查询始发日期为给定日期范围的日计划运行线统计数
	 * @param code:
	 * 	查询统计信息编码
         1.	code="01"，查询所有列车统计项的统计数
         2.	code="02"，只查询运行线统计项的统计数
         3.	code="10"，查询给定时间范围内路局统计数
	 * @param sourceTime  格式：yyyy-MM-dd HH:mm:ss
	 * @param targetTime  格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception
	 */
	public List<PlanBureauStatisticsDto>  getTrainLinesWithDay(String code,String sourceTime,String targetTime) throws Exception{
		List<PlanBureauStatisticsDto> returnList = new ArrayList<PlanBureauStatisticsDto>();
		Map<String,String> request = new HashMap<String,String>();
		request.put("code",code);
		request.put("sourceTime",sourceTime );
		request.put("targetTime", targetTime);
		logger.debug("getTrainLinesWithDay---request==" + request);
		//调用后台的接口
		//Map response = RestClientUtils.post(restUrl
		//		+ Constants.GET_TRAINLINS, request, Map.class);
		
		String result = remoteRestPost(restUrl+ Constants.GET_TRAINLINS,request);
		
		JSONObject response = JSONObject.fromObject(result);
		logger.info("getTrainLinesWithDay---response==" + response);
		//解析后台报文
		if (response != null && response.size() > 0){
			String codeMessage = StringUtil.objToStr(response.get("code"));
			if (!"".equals(codeMessage) && codeMessage.equals("200")){
				List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
				if (dataList != null && dataList.size() > 0){
					//取第一个,只有一条数据
					Map<String,Object> dataMap = dataList.get(0);
					//18个路局列表
					List<Map<String,Object>> stationsList = (List<Map<String,Object>>)dataMap.get("planBureauStatisticsDtos");
				    if(stationsList != null && stationsList.size() > 0){
				    	//循环18个路局，并取数据
				    	for(Map<String,Object> stationMap : stationsList){
				    		PlanBureauStatisticsDto dto = new PlanBureauStatisticsDto();
				    		dto.setBureauName(StringUtil.objToStr(stationMap.get("bureauName")));
				    		dto.setBureauShortName(StringUtil.objToStr(stationMap.get("bureauShortName")));
				    		dto.setBureauId(StringUtil.objToStr(stationMap.get("bureauId")));
				    		dto.setBureauCode(StringUtil.objToStr(stationMap.get("bureauCode")));
				    		
				    		List<Map<String,Object>> listDtos =  (List<Map<String,Object>>)stationMap.get("planBureauTsDtos");
				    		if(listDtos != null && listDtos.size() > 0){
				    			for(Map<String,Object> subMap : listDtos){
				    				
				    				String id = StringUtil.objToStr(subMap.get("id"));
				    				if("客运图定".equals(id)){
				    					PlanBureauTsDto subDto = new PlanBureauTsDto();
				    					subDto.setSourceSurrenderTrainlineCounts((Integer)subMap.get("sourceSurrenderTrainlineCounts"));
				    					subDto.setSourceTargetTrainlineCounts((Integer)subMap.get("sourceTargetTrainlineCounts"));
				    					dto.getListBureauDto().add(subDto);
				    					break;
				    				}
				    			}
				    		}
				    		
				    		returnList.add(dto);
				    	}
				    }
				}
			}
		}
		logger.info("getTrainLinesWithDay---returnList==="+returnList);
		return returnList ;
	}
	
	/**
	 * 5.2.4	更新给定列车的基本图运行线车底交路id
	 * @param schemeId 方案id
	 * @param vehicleCycleId 交路id
	 * @param trainNbrs  以逗号分隔的车次字符串
	 * @return
	 */
	public String updateUnitCrossId(String schemeId,String vehicleCycleId,List<String> trainNbrs)  throws Exception{
		String result = "";
		Map<String,Object> request = new HashMap<String,Object>();
		request.put("schemeId",schemeId);
		request.put("vehicleCycleId", vehicleCycleId);
		request.put("property", "name");
		request.put("trainlines", trainNbrs);
		logger.info("updateUnitCrossId---request==" + request);
		//调用后台的接口
		//Map response = RestClientUtils.post(restUrl
		//		+ Constants.UPDATE_UNIT_CROSS_ID, request, Map.class);
		String response = remoteRestPost(restUrl+ Constants.UPDATE_UNIT_CROSS_ID,request);
		logger.info("updateUnitCrossId---response==" + response);
		JSONObject obj = JSONObject.fromObject(response);
		if(obj != null){
			result = obj.get("code") == null?"" : obj.get("code").toString();
		}
		return result;
	}
	
	
	
	
	/**
	 * 调用远程接口
	 * @param url
	 * @param request
	 * @return
	 */
	private String remoteRestPost(String url, Object request){
		Client client = Client.create();
		
		client.setConnectTimeout(Constants.CONNECT_TIME_OUT);
		
		WebResource webResource = client.resource(url); 
		JSONObject obj = JSONObject.fromObject(request);
		ClientResponse response = webResource.type("application/json")
				.accept("application/json").method("POST", ClientResponse.class, obj.toString()); 
		
	    
		//将返回结果转换为指定对象 
		return  response.getEntity(String.class);  
	}
	
	
	
}
