package org.railway.com.trainplan.service.task;

public class DaytaskDto {

	private String runDate;
	private String chartId;
	private String operation;
	private int rownumstart;
	private int rownumend;
	
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getChartId() {
		return chartId;
	}
	public void setChartId(String chartId) {
		this.chartId = chartId;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public int getRownumstart() {
		return rownumstart;
	}
	public void setRownumstart(int rownumstart) {
		this.rownumstart = rownumstart;
	}
	public int getRownumend() {
		return rownumend;
	}
	public void setRownumend(int rownumend) {
		this.rownumend = rownumend;
	}
	
	
}
