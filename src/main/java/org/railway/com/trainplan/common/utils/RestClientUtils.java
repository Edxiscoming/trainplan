package org.railway.com.trainplan.common.utils;


import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;




/**
 * 
 * @author daiw
 */
public class RestClientUtils {
	private static final int errorCode = -999;
	private static final String TYPE_JSON = "application/json";  
	private static final Logger log = Logger.getLogger(RestClientUtils.class);
	private static final int connectTime = 3000;
	
	static {
//		resetPlatToken();
	}
	
	/**
	 * Retrieve a representation by doing a GET on the specified URL.
	 * The response (if any) is converted and returned.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * @param url the URL
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand the template
	 * @return the converted object
	 * @throws VmsException 
	 */
	public static <T> T get(String url, Class<T> responseType, Object... urlVariables) throws Exception {
		
		if (!url.toLowerCase().startsWith("http")) {
			//url = Constants.VMS_URL + url;
		}
		log.info("调用接口(GET)："+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(connectTime);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
			HttpEntity httpEntity = new HttpEntity(headers);
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.GET, httpEntity, responseType, urlVariables);
			if (responseEntity.getStatusCode().value() != 200) {
				log.error("调用接口(GET)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new Exception();
			} else {
				//将vms返回结果转换为指定对象
				log.debug("调用接口(GET)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回状态码(GET):"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new Exception();
		}finally{
			if (factory != null){
				factory.destroy();
			}
		}
		
		return t;
	}

	/**
	 * Retrieve a representation by doing a GET on the specified URL.
	 * The response (if any) is converted and returned.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * @param url the URL
	 * @param request the Object to be POSTed, may be {@code null}
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand the template
	 * @return the converted object
	 * @throws VmsException 
	 */
	public static <T> T get(String url,Object request,  Class<T> responseType, Object... urlVariables) throws Exception {
		
		if (!url.toLowerCase().startsWith("http")) {
			//url = Constants.VMS_URL + url;
		}
		log.info("调用接口(GET)："+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(connectTime);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
			HttpEntity httpEntity = new HttpEntity(request,headers);
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.GET, httpEntity, responseType, urlVariables);
			if (responseEntity.getStatusCode().value() != 200) {
				log.error("调用接口(GET)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new Exception();
			} else {
				//将vms返回结果转换为指定对象
				log.debug("调用接口(GET)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回状态码(GET):"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new Exception();
		}finally{
			if (factory != null){
				factory.destroy();
			}
		}
		
		return t;
	}
	
	
	 
	/**
	 * Create a new resource by POSTing the given object to the URI template,
	 * and returns the representation found in the response.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * <p>The {@code request} parameter can be a {@link HttpEntity} in order to
	 * add additional HTTP headers to the request.
	 * @param url the URL
	 * @param request the Object to be POSTed, may be {@code null}
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand the template
	 * @return the converted object
	 * @see HttpEntity
	 */
	public static <T> T post(String url, Object request, Class<T> responseType, Object... urlVariables) throws Exception {
		if (!url.toLowerCase().startsWith("http")) {
			//url = Constants.VMS_URL + url;
		}
		log.debug("调用接口(POST):"+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(connectTime);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
			HttpEntity httpEntity = new HttpEntity(request, headers);
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.POST, httpEntity, responseType, urlVariables);
			 if (responseEntity.getStatusCode().value()!= 200) {
				log.error("调用接口(POST)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new Exception();
			} else {
				//将vms返回结果转换为指定对象
				log.debug("调用接口(POST)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回Post 状态码:"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new Exception();
		} finally{
			if (factory != null){
				factory.destroy();
			}
		}
		return t;
	}

	/**
	 * Delete the resources at the specified URI.
	 * <p>URI Template variables are expanded using the given URI variables, if any.
	 * @param url the URL
	 * @param responseType the type of the return value
	 * @param uriVariables the variables to expand in the template
	 * @return the converted object
	 * @throws VmsException 
	 */
	public static <T> T delete(String url, Object request, Class<T> responseType, Object... urlVariables) throws Exception {
		if (!url.toLowerCase().startsWith("http")) {
			//url = Constants.VMS_URL + url;
		}
		log.debug("调用接口(DELETE):"+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			factory.setConnectTimeout(connectTime);
			template.setRequestFactory(factory);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
			HttpEntity httpEntity = new HttpEntity(request, headers);
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.DELETE, httpEntity, responseType, urlVariables);
			
			if (responseEntity.getStatusCode().value() != 200) {
				log.error("调用接口(DELETE)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				throw new Exception();
			} else {
				//将vms返回结果转换为指定对象
				log.debug("调用接口(DELETE)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回DELETE 状态码:"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new Exception();
		} finally{
			if (factory != null){
				factory.destroy();
			}
		}
		return t;
	}


	
	public static <T> T put(String url, Object request, Class<T> responseType, Object... urlVariables) throws Exception {
		
		if (!url.toLowerCase().startsWith("http")) {
			//url = Constants.VMS_URL + url;
		}
		log.debug("调用接口(PUT):"+url);
		RestTemplate template = new RestTemplate();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		T t = null;
		try {
			HttpEntity<?> requestEntity = new HttpEntity<Object>(request);
			
			//设置http头
			HttpHeaders headers = new HttpHeaders();
			HttpEntity httpEntity = new HttpEntity(request,headers);
			ResponseEntity<T> responseEntity = template.exchange(url, HttpMethod.PUT, httpEntity, responseType, urlVariables);
			if (responseEntity.getStatusCode().value() != 200) {
				log.error("调用接口(PUT)失败，状态码="+responseEntity.getStatusCode().value() +"  接口返回信息="+responseEntity.getBody());
				//将vms返回结果转换为指定对象
				log.debug("调用接口(PUT)请求发送成功    接口返回信息:"+responseEntity.getBody());
				t = responseEntity.getBody();
			}
		}catch(HttpClientErrorException e)  {
			log.debug("调用接口返回PUT 状态码:"+e.getStatusCode().value() +"   message:"+e.getStatusText());
			throw new Exception();
		} finally{
			if (factory != null){
				factory.destroy();
			}
		}
		return t;
	}
	
	
	public static void main(String[] s) {
		try {
			//System.err.println(get("http://192.168.0.51:8889/v1/volumes", Map.class));
			//RestClientUtils.resetPlatToken();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
