package org.railway.com.trainplan.entity;

import java.io.Serializable;

/**
 * 对应表plan_check
 * 
 * @author join
 *
 */
public class PlanCheckInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String planCheckId;
	private String planCrossId;
	private String startDate;
	private String endDate;
	private String checkPeople;
	private String checkTime;
	private String checkDept;
	private String checkBureau;
	private String checkHisFlag;
	private String checkCmdtel;

	/**
	 * 联系方式.
	 */
	private String checkPeopleTel;
	/**
	 * 原因(2015-5-5目前用于"打回原因").
	 */
	private String checkRejectReason;
	/**
	 * 操作类型(2015-5-5通过/不通过)
	 */
	private String checkState;

	private Integer count;

	public String getCheckHisFlag() {
		return checkHisFlag;
	}

	public void setCheckHisFlag(String checkHisFlag) {
		this.checkHisFlag = checkHisFlag;
	}

	public String getCheckCmdtel() {
		return checkCmdtel;
	}

	public void setCheckCmdtel(String checkCmdtel) {
		this.checkCmdtel = checkCmdtel;
	}

	public String getPlanCheckId() {
		return planCheckId;
	}

	public void setPlanCheckId(String planCheckId) {
		this.planCheckId = planCheckId;
	}

	public String getPlanCrossId() {
		return planCrossId;
	}

	public void setPlanCrossId(String planCrossId) {
		this.planCrossId = planCrossId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCheckPeople() {
		return checkPeople;
	}

	public void setCheckPeople(String checkPeople) {
		this.checkPeople = checkPeople;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckDept() {
		return checkDept;
	}

	public void setCheckDept(String checkDept) {
		this.checkDept = checkDept;
	}

	public String getCheckBureau() {
		return checkBureau;
	}

	public void setCheckBureau(String checkBureau) {
		this.checkBureau = checkBureau;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getCheckPeopleTel() {
		return checkPeopleTel;
	}

	public void setCheckPeopleTel(String checkPeopleTel) {
		this.checkPeopleTel = checkPeopleTel;
	}

	public String getCheckRejectReason() {
		return checkRejectReason;
	}

	public void setCheckRejectReason(String checkRejectReason) {
		this.checkRejectReason = checkRejectReason;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

}
