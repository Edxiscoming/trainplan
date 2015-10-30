package org.railway.com.trainplan.service.dto;

import java.util.UUID;

/**
 * 基本运行线中始发站，终到站以及经由站基本信息对象
 * @author join
 *
 */
public class TrainlineTemplateSubDto {
	
	/**
	 *  #{item.planTrainStnId,jdbcType=VARCHAR},
	       #{item.planTrainId,jdbcType=VARCHAR},
	       #{item.name,jdbcType=VARCHAR},
	       #{item.stnBureauFull,jdbcType=VARCHAR},
	       #{item.index,jdbcType=NUMERIC},
	       #{item.trackName,jdbcType=VARCHAR},
	       #{item.runDays,jdbcType=NUMERIC},
	       to_date(#{item.sourceTime,jdbcType=VARCHAR},'yyyy-MM-dd hh24:mi:ss'),
		   to_date(#{item.targetTime,jdbcType=VARCHAR},'yyyy-MM-dd hh24:mi:ss'),
		    to_date(#{item.baseArrTime,jdbcType=VARCHAR},'yyyy-MM-dd hh24:mi:ss'),
			to_date(#{item.baseDptTime,jdbcType=VARCHAR},'yyyy-MM-dd hh24:mi:ss')
	 */
	private String planTrainStnId ;
	private String planTrainId;
	 //站点名称
	private String name;
	//到站时间
	private String sourceTime;
	//出发时间
	private String targetTime;
	//站点类型1：始发站 2：经由站  3：终到站
	private String stationType;
	//局码
	private String stnBureau;
	//局码中文描述
	private String stnBureauFull;
	//局码简称
	private String bureauShortName;
	private Integer index;
	//股道
	private String trackName;
	//图定到达时间
	private String baseArrTime;
	//图定出发时间
	private String baseDptTime;
	//运行天数
	private Integer runDays;
	
	private Integer sourceDay;
	private Integer targetDay;
	//站点序号
	private int childIndex;
	//车次
	private String trainNbr;
	
	
	
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public int getChildIndex() {
		return childIndex;
	}
	public void setChildIndex(int childIndex) {
		this.childIndex = childIndex;
	}
	public String getBureauShortName() {
		return bureauShortName;
	}
	public void setBureauShortName(String bureauShortName) {
		this.bureauShortName = bureauShortName;
	}
	public Integer getSourceDay() {
		return sourceDay;
	}
	public void setSourceDay(Integer sourceDay) {
		this.sourceDay = sourceDay;
	}
	public Integer getTargetDay() {
		return targetDay;
	}
	public void setTargetDay(Integer targetDay) {
		this.targetDay = targetDay;
	}
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
	public String getStnBureauFull() {
		return stnBureauFull;
	}
	public void setStnBureauFull(String stnBureauFull) {
		this.stnBureauFull = stnBureauFull;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSourceTime() {
		return sourceTime;
	}
	public void setSourceTime(String sourceTime) {
		this.sourceTime = sourceTime;
	}
	public String getTargetTime() {
		return targetTime;
	}
	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}
	
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
	public String getStnBureau() {
		return stnBureau;
	}
	public void setStnBureau(String stnBureau) {
		this.stnBureau = stnBureau;
	}
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getBaseArrTime() {
		return baseArrTime;
	}
	public void setBaseArrTime(String baseArrTime) {
		this.baseArrTime = baseArrTime;
	}
	public String getBaseDptTime() {
		return baseDptTime;
	}
	public void setBaseDptTime(String baseDptTime) {
		this.baseDptTime = baseDptTime;
	}
	
		
}
