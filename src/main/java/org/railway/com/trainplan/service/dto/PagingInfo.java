package org.railway.com.trainplan.service.dto;

public class PagingInfo {
	private int currentPage = 0;
	private int pageSize = 0;

	public PagingInfo(int currentPage, int pageSize) {
		this.currentPage = currentPage-1;
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}
	
	@Override
	public String toString() {
		
		return "{currentPage:"+currentPage+",pageSize:"+pageSize+"}";
	}
	
}
