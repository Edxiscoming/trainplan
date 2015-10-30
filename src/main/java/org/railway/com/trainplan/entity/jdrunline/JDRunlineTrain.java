package org.railway.com.trainplan.entity.jdrunline;

public class JDRunlineTrain {
	//类型（1.“add”; 2.“upd"; 3.“stop”; 4.“del”）
	private String operationType;
	//操作时间（发布或落成时间）
	private String operationTime;
	//运行线ID
	private String runlineId;
	//车次
	private String trainNbr;
	//始发局
	private String sourceBureau;
	//经由局
	private String routeBureau;
	//始发时间
	private String startTime;
	
	
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
	public String getRunlineId() {
		return runlineId;
	}
	public void setRunlineId(String runlineId) {
		this.runlineId = runlineId;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getSourceBureau() {
		return sourceBureau;
	}
	public void setSourceBureau(String sourceBureau) {
		this.sourceBureau = sourceBureau;
	}
	public String getRouteBureau() {
		return routeBureau;
	}
	public void setRouteBureau(String routeBureau) {
		this.routeBureau = routeBureau;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
}
