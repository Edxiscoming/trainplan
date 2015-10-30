/*
 * Copyright 2013 by GGR Corporation.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GGR Corporation ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with GGR.
 *
 */
package org.railway.com.trainplan.entity;

import java.util.List;
/**
 * 查询结果对象
 * @author GGR
 * @version 1.0 
 */
public class QueryResult<T> {
    /**
     * 查询结果列表
     */
	private List<T> rows;
    /**
     * 查询结果的总记录数
     */
    private long total;
    
    private List<T> footer;
    
    public QueryResult(){
    }
    
    public QueryResult(List<T> resultList,int totalRecordNumber){
    	this.rows = resultList;
    	this.total = totalRecordNumber;
    }
    
    public QueryResult(List<T> resultList,List<T> totalList ,int totalRecordNumber){
    	this.rows = resultList;
    	this.total = totalRecordNumber;
    	this.footer = totalList;
    }

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}
    
    
    
}
