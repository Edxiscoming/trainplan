package org.railway.com.trainplan.web.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TrainRunDto {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private String day;
	private String runFlag;
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getRunFlag() {
		return runFlag;
	}
	public void setRunFlag(String runFlag) {
		this.runFlag = runFlag;
	}
	
	public static void main(String[] args) throws ParseException {
		Date startDay = dateFormat.parse("20140601");
		Date endDay = dateFormat.parse("20140603");
		int r = endDay.getDate() - startDay.getDate();
		Calendar caleandar = GregorianCalendar.getInstance();
		caleandar.setTime(startDay);
		for(int i = 0; i <= r; i++){
			
			caleandar.add(Calendar.DATE, i == 0 ? 0 : 1);
			
			String currDay = dateFormat.format(caleandar.getTime());
		 
			System.out.println(currDay);
		}
	}

}
