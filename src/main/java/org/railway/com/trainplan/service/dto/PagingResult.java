package org.railway.com.trainplan.service.dto;


public class PagingResult {
	
	private long totalRecord = 0;
	private long totalPage;//总页数
	private Object data = null;

	public PagingResult(long totalRecord, long pageSize, Object data) {
		this.totalRecord = totalRecord;
		if (totalRecord%pageSize >0) {
			this.totalPage = (totalRecord/pageSize)+1;
		} else {
			this.totalPage = totalRecord/pageSize;
		}
		this.data = data;
	}

	public PagingResult(long totalRecord,Object data){
		this.totalRecord = totalRecord;
		this.data = data;
	}
	public long getTotalRecord() {
		return totalRecord;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public Object getData() {
		return data;
	}
	
	

}
