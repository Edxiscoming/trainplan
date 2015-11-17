package org.railway.com.trainplan.entity.runline;

import java.util.List;

public class CreateRunLineData {
	private RunlineModel runline;
	private List<RunLineItem> items;
	
	
	public RunlineModel getRunline() {
		return runline;
	}
	public void setRunline(RunlineModel runline) {
		this.runline = runline;
	}
	public List<RunLineItem> getItems() {
		return items;
	}
	public void setItems(List<RunLineItem> items) {
		this.items = items;
	}

	
}
