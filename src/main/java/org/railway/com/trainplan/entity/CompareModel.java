package org.railway.com.trainplan.entity;

import java.util.Date;

/**
 * 判定对象
 * 用作判定某条列车开行计划在某局的最小到达时间和最大出发时间
 * @author ITC
 *
 */
public class CompareModel {
	private String bureau;
	private Date minArriveDate;
	private Date maxDepartDate;
	
	private String minArriveDateStr;
	private String maxArriveDateStr;

	public String getBureau() {
		return bureau;
	}

	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getMinArriveDateStr() {
		return minArriveDateStr;
	}

	public void setMinArriveDateStr(String minArriveDateStr) {
		this.minArriveDateStr = minArriveDateStr;
	}

	public String getMaxArriveDateStr() {
		return maxArriveDateStr;
	}

	public void setMaxArriveDateStr(String maxArriveDateStr) {
		this.maxArriveDateStr = maxArriveDateStr;
	}
	public Date getMinArriveDate() {
		return minArriveDate;
	}

	public void setMinArriveDate(Date minArriveDate) {
		this.minArriveDate = minArriveDate;
	}

	public Date getMaxDepartDate() {
		return maxDepartDate;
	}

	public void setMaxDepartDate(Date maxDepartDate) {
		this.maxDepartDate = maxDepartDate;
	}


	

}
