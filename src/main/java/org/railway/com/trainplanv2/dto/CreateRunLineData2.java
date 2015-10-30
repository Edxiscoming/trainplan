package org.railway.com.trainplanv2.dto;

import java.util.List;

public class CreateRunLineData2 {
	private RunlineModel2 trainline;
	private List<RunLineItem2> items;
	
	
	public List<RunLineItem2> getItems() {
		return items;
	}
	public void setItems(List<RunLineItem2> items) {
		this.items = items;
	}
	public RunlineModel2 getTrainline() {
		return trainline;
	}
	public void setTrainline(RunlineModel2 trainline) {
		this.trainline = trainline;
	}

	
}
