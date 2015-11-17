package org.railway.com.trainplan.web.dto;

import java.util.List;

public class RunPlanTrainDto {
	
	private String trainNbr;
	
	private List<TrainRunDto> runPlans;

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

}
