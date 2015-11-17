package org.railway.com.trainplan.common.constants;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public enum TPResponseCode {
	SYSTEM_SUCCESS				("0","成功"),
	SYSTEM_ERROR				("T01","未知错误"),
	SYSTEM_DATA_FORMAT_ERROR	("T02","不是合法的JSON数据"),
	SYSTEM_PARAM_LOST			("T03","缺少必要的请求参数"),
	SYSTEM_PARAM_TYPE_ERROR		("T04","请求参数类型错误"),
	SYSTEM_PARAM_SCOPE_ERROR	("T05","请求参数取值范围或者长度错误"),
	
	GENERATE_UNITCROSS_ERROR	("T06","交路未审核"),
	DATA_CHECKED_ERROR			("T07","数据已审核，请勿重复审核");
	
		

	
	private String code;
	private String msg;
	
	
	private static final Map<String, TPResponseCode> DICT = new HashMap<String, TPResponseCode>();
	private static final Map<String, TPResponseCode> DICT2 = new HashMap<String, TPResponseCode>();

	private static Logger log = Logger.getLogger(TPResponseCode.class);
	
	static {
		for (TPResponseCode item : values()) {
			DICT.put(item.code, item);
			DICT2.put(item.msg, item);
			
		}
	}
	
	private TPResponseCode(String code,String msg){
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}
	
	
	public String getMsg(){
		byte[] bs = msg.getBytes();
		try{
			msg = new String(bs,"UTF-8");
		}catch(UnsupportedEncodingException e){
			
		}
		
		return msg;
	}

	public static TPResponseCode parseCode(String code){
		try {
			
			return DICT.get(code);
		} catch (Exception ex) {
			log.debug("Null Pointer Exception", ex);
			return null;
		}
		
	}
	
	
	public static TPResponseCode parseDescription(String description){
		try {
			return DICT2.get(description);

		} catch (Exception ex) {
			return null;
		}
		
	}
}
