package org.railway.com.trainplan.common.utils;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 返回消息.
 * 
 * @author zhangPengDong
 *
 */
public class ResponseUtil {
	public static ResponseEntity<String> toJsonResponse(String body) {
		HttpHeaders headers = new HttpHeaders();
		MediaType mt = new MediaType("text", "html", Charset.forName("UTF-8"));
		headers.setContentType(mt);
		ResponseEntity<String> re = null;
		re = new ResponseEntity<String>(body, headers, HttpStatus.OK);
		return re;
	}

}
