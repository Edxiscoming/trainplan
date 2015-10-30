package org.railway.com.trainplan.entity.runline;

public class TimeSchedule {
	private long dates;
	private long hour;
	private long minute;
	private long second;
	private String text;
	
	
	public long getDates() {
		return dates;
	}
	public void setDates(long dates) {
		this.dates = dates;
	}
	public long getHour() {
		return hour;
	}
	public void setHour(long hour) {
		this.hour = hour;
	}
	public long getMinute() {
		return minute;
	}
	public void setMinute(long minute) {
		this.minute = minute;
	}
	public long getSecond() {
		return second;
	}
	public void setSecond(long second) {
		this.second = second;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
