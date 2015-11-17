package org.railway.com.trainplan.web.dto;

/**
 * 交路接续关系对象
 * @author join
 *
 */
public class CrossRelationDto {
/**
 * fromStnName:"北京西",fromTime:"2014-05-11 21:07",
 * toStnName:"北京西",toTime:"2014-05-12 08:35"
 * 
 */
	private String fromStnName;
	private String fromTime;
	private String toStnName;
	private String toTime;
	private String fromStartStnName;
	private String toEndStnName;
	
	
	public String getFromStartStnName() {
		return fromStartStnName;
	}
	public void setFromStartStnName(String fromStartStnName) {
		this.fromStartStnName = fromStartStnName;
	}
	public String getToEndStnName() {
		return toEndStnName;
	}
	public void setToEndStnName(String toEndStnName) {
		this.toEndStnName = toEndStnName;
	}
	public String getFromStnName() {
		return fromStnName;
	}
	public void setFromStnName(String fromStnName) {
		this.fromStnName = fromStnName;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToStnName() {
		return toStnName;
	}
	public void setToStnName(String toStnName) {
		this.toStnName = toStnName;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	
	
}
