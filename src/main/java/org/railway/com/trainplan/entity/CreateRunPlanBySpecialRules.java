package org.railway.com.trainplan.entity;

import java.util.List;

public class CreateRunPlanBySpecialRules {
	//规律类型
	private String ruleType;
	private UnitCross unitCross;
	private List<CreateRunLineDate> createRunLineDates;
	private String state;
	
	
	public UnitCross getUnitCross() {
		return unitCross;
	}
	public void setUnitCross(UnitCross unitCross) {
		this.unitCross = unitCross;
	}
	public List<CreateRunLineDate> getCreateRunLineDates() {
		return createRunLineDates;
	}
	public void setCreateRunLineDates(List<CreateRunLineDate> createRunLineDates) {
		this.createRunLineDates = createRunLineDates;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}
