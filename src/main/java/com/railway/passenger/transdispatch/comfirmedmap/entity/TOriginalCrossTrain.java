package com.railway.passenger.transdispatch.comfirmedmap.entity;

import java.util.List;

public class TOriginalCrossTrain {
	private TCmOriginalCross cross;
	private List<TCmOriginalTrain> trainList;
	private TCmOriginalTrain train;
	
	public TCmOriginalCross getCross() {
		return cross;
	}
	public void setCross(TCmOriginalCross cross) {
		this.cross = cross;
	}
	public List<TCmOriginalTrain> getTrainList() {
		return trainList;
	}
	public void setTrainList(List<TCmOriginalTrain> trainList) {
		this.trainList = trainList;
	}
	public TCmOriginalTrain getTrain() {
		return train;
	}
	public void setTrain(TCmOriginalTrain train) {
		this.train = train;
	}
}
