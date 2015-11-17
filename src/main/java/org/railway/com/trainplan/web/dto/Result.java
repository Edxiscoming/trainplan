package org.railway.com.trainplan.web.dto;

import org.railway.com.trainplan.common.constants.StaticCodeType;

public class Result {

	private String code;

	private String message;

	private Object data;
	
	private String str1;

	private Object data1;

	public Result(){
		this.code = StaticCodeType.SYSTEM_SUCCESS.getCode();
		this.message = StaticCodeType.SYSTEM_SUCCESS.getDescription();
	}
	public Result(String code,String message){
		this.code = code;
		this.message = message;
	}
	public Result(String code,String message,Object data){
		this(code,message);
		this.data = data;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public Object getData1() {
		return data1;
	}
	public void setData1(Object data1) {
		this.data1 = data1;
	} 
	
	
}
