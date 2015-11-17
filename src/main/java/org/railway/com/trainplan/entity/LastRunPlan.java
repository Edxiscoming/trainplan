package org.railway.com.trainplan.entity;

import java.util.Map;

public class LastRunPlan {
	Map<Integer, RunPlan> lastRunPlans;
	RunPlan runPlan;
	
	
	public Map<Integer, RunPlan> getLastRunPlans() {
		return lastRunPlans;
	}
	public void setLastRunPlans(Map<Integer, RunPlan> lastRunPlans) {
		this.lastRunPlans = lastRunPlans;
	}
	public RunPlan getRunPlan() {
		return runPlan;
	}
	public void setRunPlan(RunPlan runPlan) {
		this.runPlan = runPlan;
	}
	
	
}
