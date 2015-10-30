package com.railway.common.entity;

import java.util.List;

public class Result<T> {
	private int code = 0;			//返回状态码
	private String message = "成功";		//返回状态信息
	private T obj;				//返回单个业务对象
	private List<T> list;		//返回业务对象列表
	private Page<T> pageInfo;	//分页信息
	private Object data;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public Page<T> getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(Page<T> pageInfo) {
		this.pageInfo = pageInfo;
	}
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
