package org.railway.com.trainplan.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/21/14.
 */
public class PlanLineCheckResultDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private int isTrainInfoMatch;

	private int isTimeTableMatch;

	private int isRoutingMatch;

	/**
	 * 列车是否重复.
	 */
	private int isTrainCfMatch;

	private String isTrainCfData;

	public int getIsTrainInfoMatch() {
		return isTrainInfoMatch;
	}

	public void setIsTrainInfoMatch(int isTrainInfoMatch) {
		this.isTrainInfoMatch = isTrainInfoMatch;
	}

	public int getIsTimeTableMatch() {
		return isTimeTableMatch;
	}

	public void setIsTimeTableMatch(int isTimeTableMatch) {
		this.isTimeTableMatch = isTimeTableMatch;
	}

	public int getIsRoutingMatch() {
		return isRoutingMatch;
	}

	public void setIsRoutingMatch(int isRoutingMatch) {
		this.isRoutingMatch = isRoutingMatch;
	}

	public int getIsTrainCfMatch() {
		return isTrainCfMatch;
	}

	public void setIsTrainCfMatch(int isTrainCfMatch) {
		this.isTrainCfMatch = isTrainCfMatch;
	}

	public void setIsTrainCfData(String isTrainCfData) {
		this.isTrainCfData = isTrainCfData;
	}

	public String getIsTrainCfData() {
		return isTrainCfData;
	}

}
