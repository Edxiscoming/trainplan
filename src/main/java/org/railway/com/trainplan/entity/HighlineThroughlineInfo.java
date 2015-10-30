package org.railway.com.trainplan.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 按铁路线分组的highline_cross信息对象
 * @author join
 *
 */
public class HighlineThroughlineInfo {

	private String  throughLine;
	private List<HighlineCrossInfo> listCrossInfo = new ArrayList<HighlineCrossInfo>();
	public String getThroughLine() {
		return throughLine;
	}
	public void setThroughLine(String throughLine) {
		this.throughLine = throughLine;
	}
	public List<HighlineCrossInfo> getListCrossInfo() {
		return listCrossInfo;
	}
	public void setListCrossInfo(List<HighlineCrossInfo> listCrossInfo) {
		this.listCrossInfo = listCrossInfo;
	}
	
	
}
