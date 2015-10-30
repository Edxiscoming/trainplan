package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the PLAN_TRAIN_STN database table.
 * 
 */
public class PlanTrainStnModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String planTrainStnId;

	private Integer arrRunDays;

	private Date arrTime;

	private String arrTrainNbr;

	private Date baseArrTime;

	private Date baseDptTime;

	private Integer boundaryInOut;

	private Date dptTime;

	private String dptTrainNbr;

	private Integer locoFlag;

	private String planTrainId;

	private Integer platform;

	private Integer psgFlg;

	private Integer runDays;

	private String stnBureau;

	private String stnBureauFull;

	private String stnName;

	private Integer stnSort;

	private String stnType;

	private String tecType;

	private String trackName;

	private String trackNbr;

	private String upDown;

    public PlanTrainStnModel() {
    }

	public String getPlanTrainStnId() {
		return this.planTrainStnId;
	}

	public void setPlanTrainStnId(String planTrainStnId) {
		this.planTrainStnId = planTrainStnId;
	}

	public Integer getArrRunDays() {
		return this.arrRunDays;
	}

	public void setArrRunDays(Integer arrRunDays) {
		this.arrRunDays = arrRunDays;
	}

	public Date getArrTime() {
		return this.arrTime;
	}

	public void setArrTime(Date arrTime) {
		this.arrTime = arrTime;
	}

	public String getArrTrainNbr() {
		return this.arrTrainNbr;
	}

	public void setArrTrainNbr(String arrTrainNbr) {
		this.arrTrainNbr = arrTrainNbr;
	}

	public Date getBaseArrTime() {
		return this.baseArrTime;
	}

	public void setBaseArrTime(Date baseArrTime) {
		this.baseArrTime = baseArrTime;
	}

	public Date getBaseDptTime() {
		return this.baseDptTime;
	}

	public void setBaseDptTime(Date baseDptTime) {
		this.baseDptTime = baseDptTime;
	}

	public Integer getBoundaryInOut() {
		return this.boundaryInOut;
	}

	public void setBoundaryInOut(Integer boundaryInOut) {
		this.boundaryInOut = boundaryInOut;
	}

	public Date getDptTime() {
		return this.dptTime;
	}

	public void setDptTime(Date dptTime) {
		this.dptTime = dptTime;
	}

	public String getDptTrainNbr() {
		return this.dptTrainNbr;
	}

	public void setDptTrainNbr(String dptTrainNbr) {
		this.dptTrainNbr = dptTrainNbr;
	}

	public Integer getLocoFlag() {
		return this.locoFlag;
	}

	public void setLocoFlag(Integer locoFlag) {
		this.locoFlag = locoFlag;
	}

	public String getPlanTrainId() {
		return this.planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	public Integer getPlatform() {
		return this.platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public Integer getPsgFlg() {
		return this.psgFlg;
	}

	public void setPsgFlg(Integer psgFlg) {
		this.psgFlg = psgFlg;
	}

	public Integer getRunDays() {
		return this.runDays;
	}

	public void setRunDays(Integer runDays) {
		this.runDays = runDays;
	}

	public String getStnBureau() {
		return this.stnBureau;
	}

	public void setStnBureau(String stnBureau) {
		this.stnBureau = stnBureau;
	}

	public String getStnBureauFull() {
		return this.stnBureauFull;
	}

	public void setStnBureauFull(String stnBureauFull) {
		this.stnBureauFull = stnBureauFull;
	}

	public String getStnName() {
		return this.stnName;
	}

	public void setStnName(String stnName) {
		this.stnName = stnName;
	}

	public Integer getStnSort() {
		return this.stnSort;
	}

	public void setStnSort(Integer stnSort) {
		this.stnSort = stnSort;
	}

	public String getStnType() {
		return this.stnType;
	}

	public void setStnType(String stnType) {
		this.stnType = stnType;
	}

	public String getTecType() {
		return this.tecType;
	}

	public void setTecType(String tecType) {
		this.tecType = tecType;
	}

	public String getTrackName() {
		return this.trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getTrackNbr() {
		return this.trackNbr;
	}

	public void setTrackNbr(String trackNbr) {
		this.trackNbr = trackNbr;
	}

	public String getUpDown() {
		return this.upDown;
	}

	public void setUpDown(String upDown) {
		this.upDown = upDown;
	}

}