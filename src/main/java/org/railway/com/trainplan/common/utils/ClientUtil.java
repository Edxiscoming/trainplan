package org.railway.com.trainplan.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.railway.com.trainplan.common.constants.OperationConstants;

public class ClientUtil {
private static Logger log = Logger.getLogger(ClientUtil.class);
	
	public static String excuteRequest(String url, String params, OperationConstants.REQUEST_METHOD method, int timout) throws Exception {
		Map<String, String> header = new HashMap<String, String>();
		return excuteRequest(url, params, method, timout, header);
	}

	public static String excuteRequest(String url, String params,
			OperationConstants.REQUEST_METHOD method, int timout, Map<String, String> header) throws Exception {
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		String responseBody = null;

		try {
			httpclient = HttpClients.createDefault();
			
			response = httpclient.execute(httpMethodFactory(url, params,
					method, timout, header));
			//如果http状态不是200，则抛出异常
			 if(!response.getStatusLine().toString().contains("200")) {
				 //抛出异常
				 return null;
			 }
			 else {
				 HttpEntity responseEntity = response.getEntity();
					if (null != responseEntity) {
						responseBody = EntityUtils.toString(responseEntity, "UTF-8");
						return responseBody;
					}
					else {
						return null;
					}
			 }		
		}catch(Exception e) {
			return null;
		} finally {
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e);
				}
			}

			if (null != httpclient) {
				try {
					httpclient.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	
	}

	private static HttpUriRequest httpMethodFactory(String url, String params,
			OperationConstants.REQUEST_METHOD method, int timout, Map<String, String> header) {

		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timout).setConnectTimeout(timout).build();

		switch (method) {
		case POST:
			HttpPost httpPost = new HttpPost(url);
			StringEntity entityPost = new StringEntity(params,
					ContentType.create("text/plain", "UTF-8"));
			httpPost.setEntity(entityPost);
			httpPost.setConfig(requestConfig);
			constructHeader(header, httpPost);
			return httpPost;

		case GET:
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			constructHeader(header, httpGet);
			return httpGet;

		case PUT:
			HttpPut httpPut = new HttpPut(url);
			StringEntity entityPut = new StringEntity(params,
					ContentType.create("text/plain", "UTF-8"));
			httpPut.setEntity(entityPut);
			httpPut.setConfig(requestConfig);
			constructHeader(header, httpPut);
			return httpPut;

		case DELETE:
			HttpDelete httpDelete = new HttpDelete(url);
			httpDelete.setConfig(requestConfig);
			constructHeader(header, httpDelete);
			return httpDelete;

		default:
			break;

		}
		return null;

	}
	
	private static void constructHeader(Map<String, String> header, HttpUriRequest httpUriRequest) {
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		
		Set<String> keySet = header.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = header.get(key);
			if(null != value) {
				httpUriRequest.setHeader(key, value);
			}
		}
	}
}
