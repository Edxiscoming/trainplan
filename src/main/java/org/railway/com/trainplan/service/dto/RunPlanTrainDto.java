package org.railway.com.trainplan.service.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class RunPlanTrainDto {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private String trainNbr;
	private List<TrainRunDto> runPlans = new LinkedList<TrainRunDto>(); 
	private String planTrainId;
	private String groupSerialNbr;
	private String startStn;
	private String endStn;

	
	public RunPlanTrainDto(){}
	public RunPlanTrainDto(String sd, String ed,String telName,String planTrainId,String isModified) { 
		try { 
			Date startDay = dateFormat.parse(sd);
			Date endDay = dateFormat.parse(ed);
			int r = Integer.parseInt(String.valueOf((endDay.getTime() - startDay.getTime())/(1000*3600*24)));
			Calendar caleandar = GregorianCalendar.getInstance();
			caleandar.setTime(startDay);
			for(int i = 0; i <= r; i++){ 
				caleandar.add(Calendar.DATE, i == 0 ? 0 : 1);  
				String currDay = dateFormat.format(caleandar.getTime()); 
				this.runPlans.add(new TrainRunDto(currDay, null,telName,planTrainId,isModified));
			} 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	
	public void setRunDay(String sd, String ed){
		try { 
			Date startDay = dateFormat.parse(sd);
			Date endDay = dateFormat.parse(ed);
			int r = Integer.parseInt(String.valueOf((endDay.getTime() - startDay.getTime())/(1000*3600*24)));
			Calendar caleandar = GregorianCalendar.getInstance();
			caleandar.setTime(startDay);
			for(int i = 0; i <= r; i++){ 
				caleandar.add(Calendar.DATE, i == 0 ? 0 : 1);  
				String currDay = dateFormat.format(caleandar.getTime()); 
				this.runPlans.add(new TrainRunDto(currDay, null,null,null,null));
			} 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public String getTrainNbr() {
		return trainNbr;
	}

	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}

	public List<TrainRunDto> getRunPlans() {
		return runPlans;
	}

	public void setRunPlans(List<TrainRunDto> runPlans) {
		this.runPlans = runPlans;
	}
	
	public void setRunFlag(String runDay, String runFlag) {
		 for(TrainRunDto tr : this.runPlans){
			 if(tr.getDay().equals(runDay)){
				 tr.setRunFlag(runFlag); 
				 return;
			 }
		 }
	}
	
	public void setRunFlag(String runDay, String runFlag, String createFlag) {
		 for(TrainRunDto tr : this.runPlans){
			 if(tr.getDay().equals(runDay)){
				 tr.setRunFlag(runFlag); 
				 tr.setCreateFlag(createFlag);
				 return;
			 }
		 }
	}
	public void setRunFlag(String runDay, String runFlag, String createFlag,String planTrainId,String isModified) {
		for(TrainRunDto tr : this.runPlans){
			if(tr.getDay().equals(runDay)){
				tr.setRunFlag(runFlag); 
				tr.setCreateFlag(createFlag);
				tr.setIsModified(isModified);
				tr.setPlanTrainId(planTrainId);
				return;
			}
		}
	}
	public void setRunFlag(String runDay, String runFlag,String planTrainId,String isModified) {
		for(TrainRunDto tr : this.runPlans){
			if(tr.getDay().equals(runDay)){
				tr.setRunFlag(runFlag); 
				tr.setIsModified(isModified);
				tr.setPlanTrainId(planTrainId);
				return;
			}
		}
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getGroupSerialNbr() {
		return groupSerialNbr;
	}
	public void setGroupSerialNbr(String groupSerialNbr) {
		this.groupSerialNbr = groupSerialNbr;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}

}
