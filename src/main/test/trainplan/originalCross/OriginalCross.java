package trainplan.originalCross;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.railway.com.trainplan.common.constants.OperationConstants;
import org.railway.com.trainplan.service.HttpClientService;

import trainplan.dto.TestOriginalCross;

public class OriginalCross {
	
	
    private HttpClientService httpClientService = new HttpClientService();

    
	public void testUpdate() {
		TestOriginalCross cross = new TestOriginalCross();
		cross.setCrossId("1d5d0570-ea8b-439e-841b-56bf6493132f");
		cross.setNote("test");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", cross);
		String params = JSONObject.fromObject(map).toString();
		try {
			httpClientService.sendHttpClient("http://127.0.0.1:8080/trainplan/originalCross/updateOriginalCross", params, OperationConstants.REQUEST_METHOD.PUT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testAdd() {
		TestOriginalCross cross = new TestOriginalCross();
		cross.setChartId("123");
		cross.setChartName("测试方案名称");
		cross.setCrossName("测试新建交路");
		cross.setThroughline("成都");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", cross);
		String params = JSONObject.fromObject(map).toString();
		try {
			httpClientService.sendHttpClient("http://127.0.0.1:8080/trainplan/originalCross/addOriginalCross", params, OperationConstants.REQUEST_METHOD.PUT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testTransfer() {
		String ids = "1d5ed5bb-3ad9-423f-8ea9-94ac5a9b435c";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crossIds", ids);
		String params = JSONObject.fromObject(map).toString();
		try {
			httpClientService.sendHttpClient("http://127.0.0.1:8080/trainplan/originalCross/transferCross", params, OperationConstants.REQUEST_METHOD.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetCross() {
		String id = "1d5d0570-ea8b-439e-841b-56bf6493132f";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("crossId", id);
		String params = JSONObject.fromObject(map).toString();
		try {
			httpClientService.sendHttpClient("http://127.0.0.1:8080/trainplan/originalCross/getOriginalCross", params, OperationConstants.REQUEST_METHOD.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
