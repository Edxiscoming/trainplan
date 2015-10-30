package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
//@Entity
//@Table(name = "PLAN_TRAIN_STN")
public class PlanTrainStn implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
PLAN_TRAIN_STN_ID
PLAN_TRAIN_ID
ARR_TRAIN_NBR
DPT_TRAIN_NBR
ARR_TIME
DPT_TIME
BASE_ARR_TIME
BASE_DPT_TIME
	 */
	public PlanTrainStn(){}
	private String planTrainStnId  = UUID.randomUUID().toString();
	//车站名
	private String stnName;
	private String stnType;
	private String planTrainId;
	//到达车次
	private String arrTrainNbr;
	//出发车次
	private String dptTrainNbr;
	private String arrTime;
	private String dptTime;
	private String baseArrTime;
	private String baseDptTime;
	private String stnBureauFull;
	private Integer stnSort;
	private String  trackName;
	private Integer runDays;
	//@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	//@Column(name = "PLAN_TRAIN_STN_ID", unique = true, nullable = false)
	public String getPlanTrainStnId() {
		return planTrainStnId;
	}
	public void setPlanTrainStnId(String planTrainStnId) {
		this.planTrainStnId = planTrainStnId;
	}
	
	
	public Integer getRunDays() {
		return runDays;
	}
	public void setRunDays(Integer runDays) {
		this.runDays = runDays;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public Integer getStnSort() {
		return stnSort;
	}
	public void setStnSort(Integer stnSort) {
		this.stnSort = stnSort;
	}
	public String getStnBureauFull() {
		return stnBureauFull;
	}
	public void setStnBureauFull(String stnBureauFull) {
		this.stnBureauFull = stnBureauFull;
	}
	//@Column(name = "PLAN_TRAIN_ID")
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	//@Column(name = "ARR_TRAIN_NBR")
	public String getArrTrainNbr() {
		return arrTrainNbr;
	}
	public void setArrTrainNbr(String arrTrainNbr) {
		this.arrTrainNbr = arrTrainNbr;
	}
	//@Column(name = "DPT_TRAIN_NBR")
	public String getDptTrainNbr() {
		return dptTrainNbr;
	}
	public void setDptTrainNbr(String dptTrainNbr) {
		this.dptTrainNbr = dptTrainNbr;
	}
	//@Column(name = "ARR_TIME")
	public String getArrTime() {
		return arrTime;
	}
	public void setArrTime(String arrTime) {
		this.arrTime = arrTime;
	}
	//@Column(name = "DPT_TIME")
	public String getDptTime() {
		return dptTime;
	}
	public void setDptTime(String dptTime) {
		this.dptTime = dptTime;
	}
	//@Column(name = "BASE_ARR_TIME")
	public String getBaseArrTime() {
		return baseArrTime;
	}
	public void setBaseArrTime(String baseArrTime) {
		this.baseArrTime = baseArrTime;
	}
	//@Column(name = "BASE_DPT_TIME")
	public String getBaseDptTime() {
		return baseDptTime;
	}
	public void setBaseDptTime(String baseDptTime) {
		this.baseDptTime = baseDptTime;
	}
	
	
	
	
	//@Column(name = "STN_NAME")
	public String getStnName() {
		return stnName;
	}
	public void setStnName(String stnName) {
		this.stnName = stnName;
	}
	//@Column(name = "STN_TYPE")
	public String getStnType() {
		return stnType;
	}
	public void setStnType(String stnType) {
		this.stnType = stnType;
	}
	
	
	
	
}
