package com.railway.common.entity;

import java.util.List;

public class Page<T> {
	private int totalCount;	//总共数据数量
	private int pageSize;	//每页现实数据量
	private int pageIndex;	//当前页数
	private int num;		//当前查询出的数据量个数
	private List<T> list;	//查询出的数据列表
	
	public Page(){
		
	}
	public Page(int totalCount, int pageSize, int pageIndex, int num, List<T> list){
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
		this.num = num;
		this.list = list;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
}
