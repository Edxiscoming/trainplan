package org.railway.com.trainplan.service.dto;

public class ParamDto {

	private String sourceEntityId;
	private String planTrainId;
	//开行日期yyyy-mm-dd
	private String time;
	private String source = "1";
	private String action = "1";
	private String msgReceiveUrl = "";
	public String getSourceEntityId() {
		return sourceEntityId;
	}
	public void setSourceEntityId(String sourceEntityId) {
		this.sourceEntityId = sourceEntityId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public String getMsgReceiveUrl() {
		return msgReceiveUrl;
	}
	public void setMsgReceiveUrl(String msgReceiveUrl) {
		this.msgReceiveUrl = msgReceiveUrl;
	}
	 
	
	
}
