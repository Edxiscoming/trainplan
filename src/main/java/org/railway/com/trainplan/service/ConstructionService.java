package org.railway.com.trainplan.service;

import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.constants.Constants;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
//import org.springframework.amqp.core.AmqpTemplate;
 

@Component
@Transactional
@Monitored
public class ConstructionService {
	
	private static final Logger logger = Logger.getLogger(ConstructionService.class);
	//@Autowired
	//private QuoteService quoteService; 
	
	@Autowired
	private AmqpTemplate amqpTemplate;

	
	public String getConstructions(String trainNbr) {
		//http://10.1.191.135:7003/rail/template/TrainlineTemplates/942be8cd-7df1-42c9-a5e0-3aaa82561a97
		// TODO Auto-generated method stub
		//http://10.1.191.135:7003/rail/template/TrainlineTemplates/
		//return "{\"code\", -1, \"message\": \"获取货运列车ID失败\"}";
		String result = "{\"code\":\"201\",\"name\":null,\"dataSize\":1,\"data\":[{\"id\":\"942be8cd-7df1-42c9-a5e0-3aaa82561a97\",\"name\":\"G11\",\"pinyinCode\":null,\"description\":null,\"versionDto\":null,\"state\":\"SYNCHRONIZED\",\"index\":0,\"resourceId\":\"7eeb336a-f024-4ec6-bf55-5fee5bd00c97\",\"resourceName\":\"基础资料\",\"typeId\":\"0019cb5f-509a-42f5-afc4-e43e4a7eafc0\",\"typeName\":\"高速动车组旅客列车\",\"sourceNodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"sourceNodeName\":\"北京南高速场\",\"targetNodeId\":null,\"targetNodeName\":null,\"sourceTime\":null,\"sourceTime1\":null,\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"targetTime\":null,\"targetTime1\":null,\"dataSourceDto\":{\"source\":\"southwest\",\"id\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"name\":null,\"handleTime\":null,\"manager\":null},\"scheduleDto\":{\"sourceItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:8:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}},\"routeItemDtos\":[{\"id\":\"1c0814fa-2d67-47d9-93d2-68b1f319afcd\",\"name\":\"津沪线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":3,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e57ac46a-8cf2-41f2-b616-48148b835da1\",\"nodeName\":\"津沪线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:29:48\",\"targetTimeDto2\":\"0:8:29:48\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48}},{\"id\":\"8fe3ec66-b042-4a14-a9cc-79ca0c0bc8ad\",\"name\":\"济南西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":7,\"bureauId\":\"d03a250a-b06a-425f-83f2-28f4314f5623\",\"bureauName\":\"济南铁路局\",\"nodeId\":\"59bfd98e-a895-44b6-a879-088e09798a9b\",\"nodeName\":\"济南西\",\"trackName\":\"3\",\"sourceTimeDto2\":\"0:9:32:10\",\"targetTimeDto2\":\"0:9:34:10\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":32,\"second\":10},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":34,\"second\":10}},{\"id\":\"aad1ed10-ea06-449c-a809-1aaca9a64457\",\"name\":\"廊坊\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":1,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"4493a648-e2ee-4fd8-8b8c-c49fe8dc1c3e\",\"nodeName\":\"廊坊\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:18:20\",\"targetTimeDto2\":\"0:8:18:20\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20}},{\"id\":\"3863a9b4-d17c-45d8-887a-bfb1e5bca91a\",\"name\":\"德州东\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":6,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"da2c01a8-5593-4c53-b2af-f9a465cb5245\",\"nodeName\":\"德州东\",\"trackName\":\"5\",\"sourceTimeDto2\":\"0:9:10:2\",\"targetTimeDto2\":\"0:9:10:2\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2}},{\"id\":\"34f24822-8037-4ee2-a295-7ffd2fbf6a39\",\"name\":\"天津南\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":4,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e6a4321b-ed5c-44dc-a10d-2117fde8c937\",\"nodeName\":\"天津南\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:31:5\",\"targetTimeDto2\":\"0:8:31:5\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5}},{\"id\":\"f9a92718-0d07-409e-b6e8-82843bd4b906\",\"name\":\"沧州西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":5,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"601f327b-54db-4b96-8c18-b724e2aaac98\",\"nodeName\":\"沧州西\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:48:56\",\"targetTimeDto2\":\"0:8:48:56\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56}},{\"id\":\"6d1e8b33-7e00-49be-9e72-bc343538d627\",\"name\":\"京津线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":2,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"35f4333a-12e4-48ca-b8f3-6b1665b22887\",\"nodeName\":\"京津线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:28:13\",\"targetTimeDto2\":\"0:8:28:13\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13}}],\"targetItemDto\":null},\"trainlineWorkDto\":null,\"routeDto\":{\"directionalRailwayLineSegmentSiteDtos\":[{\"id\":\"9898d998-a1fc-4525-86a0-511668b6a019\",\"name\":\"铁路线[京沪高速下行]-[北京南高速场-济南西]区段\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"railwayLineId\":\"7354bd2d-cc29-4e2a-a73f-da21a274a9eb\",\"railwayLineName\":\"京沪高速\",\"directionalRailwayLineId\":\"06103c4d-28c2-47fb-9a28-7dccc1d24263\",\"directionalRailwayLineName\":\"京沪高速下行\",\"sourceSegmentId\":\"d97b6821-62b8-47a6-a963-1bf6258e6538\",\"sourceSegmentName\":\"北京南高速场\",\"targetSegmentId\":\"2d219a35-9ee4-4cd2-850e-722e6e39d16e\",\"targetSegmentName\":\"济南西\"}],\"directionalRailwayLineKilometerMarkSiteDtos\":[],\"lineSiteDtos\":[],\"nodeSiteDtos\":[]},\"schemeId\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"schemeName\":\"测试3-京沪高铁-30列\",\"vehicleCycleId\":null}],\"exceptionSize\":0,\"exceptions\":[]}";
		//http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=G11 
		try {
			Client client = Client.create();
			
			client.setConnectTimeout(Constants.CONNECT_TIME_OUT);
			 
			WebResource webResource = client.resource("http://10.1.191.135:7003/rail/template/TrainlineTemplates?name=" + trainNbr); 
			
//			webResource.method(this.method, GenericType)
			
			ClientResponse response = webResource.type("application/json")
					.accept("application/json").method("GET", ClientResponse.class); 
		    
			//将返回结果转换为指定对象 
			result = response.getEntity(String.class);  
			return result;
			 
		}catch (Exception e) {
			// TODO: handle exception
			return "{\"code\": -1, \"message\":\"生成货运线的时候发生异常:" + e.getMessage() + "\"}";
		 
		}   
	}
	
	private String gettrainId(String trainNbr){
		
		String result = "";
		
		try {  
			Client client = Client.create();
			
			client.setConnectTimeout(Constants.CONNECT_TIME_OUT);

			WebResource webResource = client.resource("http://10.1.191.135:7003/rail/template/TrainlineTemplates/" + trainNbr); 
			
//			webResource.method(this.method, GenericType)
			
			ClientResponse response = webResource.type("application/json")
					.accept("application/json").method("GET", ClientResponse.class); 
		    
			//将返回结果转换为指定对象 
			result = response.getEntity(String.class); 
			
			JSONObject train = JSONObject.fromObject(result);
			result = train.getString("id");
			
			return result;
			 
		}catch (Exception e) {
			// TODO: handle exception
			return "error";
		 
		}   
	}

	
	public String createConstructionPlain(String trainNbr, String runDate) {
		JSONObject  json = new JSONObject();
		JSONObject head = new JSONObject();
		String uuid = UUID.randomUUID().toString();
		head.put("event", "createFreightTransportEvent");
		head.put("requestId",uuid);
		head.put("batch", 1);
		head.put("user", "test");
		json.put("head", head); 
		JSONArray jsonArray = new JSONArray(); 
		JSONObject  temp = new JSONObject();
		temp.put("sourceEntityId", trainNbr);
		temp.put("time", runDate);
		temp.put("source", 1);
		temp.put("action", 1);
		jsonArray.add(temp);  
		
		json.put("param", jsonArray);
		 
		amqpTemplate.convertAndSend("crec.event.createFreightTransport", json.toString()); 
		return "{\"code\": 0, \"requestId\":\"" + uuid + "\"}";
	}

	
	public String queryTrainForBureau(String bureauNrm, String runDate, int currentPage, int pageSize) {
		
		String result = "{\"code\":\"201\",\"name\":null,\"dataSize\":1,\"data\":[{\"id\":\"942be8cd-7df1-42c9-a5e0-3aaa82561a97\",\"name\":\"G11\",\"pinyinCode\":null,\"description\":null,\"versionDto\":null,\"state\":\"SYNCHRONIZED\",\"index\":0,\"resourceId\":\"7eeb336a-f024-4ec6-bf55-5fee5bd00c97\",\"resourceName\":\"基础资料\",\"typeId\":\"0019cb5f-509a-42f5-afc4-e43e4a7eafc0\",\"typeName\":\"高速动车组旅客列车\",\"sourceNodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"sourceNodeName\":\"北京南高速场\",\"targetNodeId\":null,\"targetNodeName\":null,\"sourceTime\":null,\"sourceTime1\":null,\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"targetTime\":null,\"targetTime1\":null,\"dataSourceDto\":{\"source\":\"southwest\",\"id\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"name\":null,\"handleTime\":null,\"manager\":null},\"scheduleDto\":{\"sourceItemDto\":{\"id\":\"09f5fcf2-b83b-4c5f-aae0-603134482d48\",\"name\":\"北京南高速场\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"fc4812cc-6659-4556-8f34-e933bf3a1b33\",\"nodeName\":\"北京南高速场\",\"trackName\":\"10\",\"sourceTimeDto2\":\"0:8:0:0\",\"targetTimeDto2\":\"0:8:0:0\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"timeDto\":null,\"timeDto1\":{\"day\":0,\"hour\":8,\"minute\":0,\"second\":0}},\"routeItemDtos\":[{\"id\":\"1c0814fa-2d67-47d9-93d2-68b1f319afcd\",\"name\":\"津沪线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":3,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e57ac46a-8cf2-41f2-b616-48148b835da1\",\"nodeName\":\"津沪线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:29:48\",\"targetTimeDto2\":\"0:8:29:48\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":29,\"second\":48}},{\"id\":\"8fe3ec66-b042-4a14-a9cc-79ca0c0bc8ad\",\"name\":\"济南西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":7,\"bureauId\":\"d03a250a-b06a-425f-83f2-28f4314f5623\",\"bureauName\":\"济南铁路局\",\"nodeId\":\"59bfd98e-a895-44b6-a879-088e09798a9b\",\"nodeName\":\"济南西\",\"trackName\":\"3\",\"sourceTimeDto2\":\"0:9:32:10\",\"targetTimeDto2\":\"0:9:34:10\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":32,\"second\":10},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":34,\"second\":10}},{\"id\":\"aad1ed10-ea06-449c-a809-1aaca9a64457\",\"name\":\"廊坊\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":1,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"4493a648-e2ee-4fd8-8b8c-c49fe8dc1c3e\",\"nodeName\":\"廊坊\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:18:20\",\"targetTimeDto2\":\"0:8:18:20\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":18,\"second\":20}},{\"id\":\"3863a9b4-d17c-45d8-887a-bfb1e5bca91a\",\"name\":\"德州东\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":6,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"da2c01a8-5593-4c53-b2af-f9a465cb5245\",\"nodeName\":\"德州东\",\"trackName\":\"5\",\"sourceTimeDto2\":\"0:9:10:2\",\"targetTimeDto2\":\"0:9:10:2\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":9,\"minute\":10,\"second\":2}},{\"id\":\"34f24822-8037-4ee2-a295-7ffd2fbf6a39\",\"name\":\"天津南\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":4,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"e6a4321b-ed5c-44dc-a10d-2117fde8c937\",\"nodeName\":\"天津南\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:31:5\",\"targetTimeDto2\":\"0:8:31:5\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":31,\"second\":5}},{\"id\":\"f9a92718-0d07-409e-b6e8-82843bd4b906\",\"name\":\"沧州西\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":5,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"601f327b-54db-4b96-8c18-b724e2aaac98\",\"nodeName\":\"沧州西\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:48:56\",\"targetTimeDto2\":\"0:8:48:56\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":48,\"second\":56}},{\"id\":\"6d1e8b33-7e00-49be-9e72-bc343538d627\",\"name\":\"京津线路所\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":2,\"bureauId\":\"1ceca80f-2860-47fe-a3f3-415b5fee9e20\",\"bureauName\":\"北京铁路局\",\"nodeId\":\"35f4333a-12e4-48ca-b8f3-6b1665b22887\",\"nodeName\":\"京津线路所\",\"trackName\":\"1\",\"sourceTimeDto2\":\"0:8:28:13\",\"targetTimeDto2\":\"0:8:28:13\",\"timeFormat\":\"yyyy-MM-dd HH:mm:ss\",\"sourceTimeDto\":null,\"sourceTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13},\"targetTimeDto\":null,\"targetTimeDto1\":{\"day\":0,\"hour\":8,\"minute\":28,\"second\":13}}],\"targetItemDto\":null},\"trainlineWorkDto\":null,\"routeDto\":{\"directionalRailwayLineSegmentSiteDtos\":[{\"id\":\"9898d998-a1fc-4525-86a0-511668b6a019\",\"name\":\"铁路线[京沪高速下行]-[北京南高速场-济南西]区段\",\"pinyinCode\":null,\"description\":null,\"versionDto\":{\"major\":0,\"minor\":0,\"micro\":0,\"qualifier\":\"\"},\"state\":\"SYNCHRONIZED\",\"index\":0,\"railwayLineId\":\"7354bd2d-cc29-4e2a-a73f-da21a274a9eb\",\"railwayLineName\":\"京沪高速\",\"directionalRailwayLineId\":\"06103c4d-28c2-47fb-9a28-7dccc1d24263\",\"directionalRailwayLineName\":\"京沪高速下行\",\"sourceSegmentId\":\"d97b6821-62b8-47a6-a963-1bf6258e6538\",\"sourceSegmentName\":\"北京南高速场\",\"targetSegmentId\":\"2d219a35-9ee4-4cd2-850e-722e6e39d16e\",\"targetSegmentName\":\"济南西\"}],\"directionalRailwayLineKilometerMarkSiteDtos\":[],\"lineSiteDtos\":[],\"nodeSiteDtos\":[]},\"schemeId\":\"0336fdb6-c008-45da-bff2-3ba405e65b29\",\"schemeName\":\"测试3-京沪高铁-30列\",\"vehicleCycleId\":null}],\"exceptionSize\":0,\"exceptions\":[]}";
		try {
			Client client = Client.create();
			
			client.setConnectTimeout(Constants.CONNECT_TIME_OUT);
			 
			WebResource webResource = client.resource("http://10.1.191.135:7003/rail/plan/Trainlines?query"); 
			
			
			String sourceTime = runDate + " 00:00:00";
			
			String targetTime = runDate + " 23:59:59";
			
			ClientResponse response = webResource.type("application/json")
					.accept("application/json").method("POST", ClientResponse.class, "{\"code\":\"01\", \"sourceTime\":\"" + sourceTime+ "\", \"targetTime\": \"" 
							+ targetTime + "\", \"sourceBureauShortName\": \"" + bureauNrm + "\", \"currentPage\": " + currentPage + ", \"pageSize\": " + pageSize + "}"); 
			
			logger.debug("发送查询参数:{\"code\":\"01\", \"sourceTime\":\"" + sourceTime+ "\", \"targetTime\": \"" 
							+ targetTime + "\", \"sourceBureauShortName\": \"" + bureauNrm + "\", \"currentPage\": " + currentPage + ", \"pageSize\": " + pageSize + "}");
			//将返回结果转换为指定对象 
			result = response.getEntity(String.class);    
			logger.debug("结果返回:" + result);
			return result;
			 
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return "{\"code\": -1, \"message\":\"生成货运线的时候发生异常:" + e.getMessage() + "\"}";
		 
		}  
	}

}
