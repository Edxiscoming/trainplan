package org.railway.com.trainplanv2.dto;

public class ReceiveMsg2 {
	private String planTrainId;
	private String msgReceiveUrl;
	private String createPeople;
	private String bureau;
	private boolean delete;
	private CreateRunLineData2 createRunLineData;
	
	
	public String getPlanTrainId() {
		return planTrainId;
	}
	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public CreateRunLineData2 getCreateRunLineData() {
		return createRunLineData;
	}
	public void setCreateRunLineData(CreateRunLineData2 createRunLineData) {
		this.createRunLineData = createRunLineData;
	}
	public String getMsgReceiveUrl() {
		return msgReceiveUrl;
	}
	public void setMsgReceiveUrl(String msgReceiveUrl) {
		this.msgReceiveUrl = msgReceiveUrl;
	}
	
	public String getCreatePeople() {
		return createPeople;
	}
	public void setCreatePeople(String createPeople) {
		this.createPeople = createPeople;
	}
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
}
