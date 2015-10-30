package org.railway.com.trainplan.service.dto;

public class PlanBureauTsDto {

	//图定始发终到
	private int sourceTargetTrainlineCounts;
	//图定始发交出
	private int sourceSurrenderTrainlineCounts;
	public int getSourceTargetTrainlineCounts() {
		return sourceTargetTrainlineCounts;
	}
	public void setSourceTargetTrainlineCounts(int sourceTargetTrainlineCounts) {
		this.sourceTargetTrainlineCounts = sourceTargetTrainlineCounts;
	}
	public int getSourceSurrenderTrainlineCounts() {
		return sourceSurrenderTrainlineCounts;
	}
	public void setSourceSurrenderTrainlineCounts(
			int sourceSurrenderTrainlineCounts) {
		this.sourceSurrenderTrainlineCounts = sourceSurrenderTrainlineCounts;
	}
	
	
}
