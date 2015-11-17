package org.railway.com.trainplan.entity.runline;

public class ReceiveMsg {
	private String planTrainId;
	private String msgReceiveUrl;
	private String createPeople;
	private String bureau;
	private boolean delete;
	private CreateRunLineData createRunLineData;
	
	
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
	public CreateRunLineData getCreateRunLineData() {
		return createRunLineData;
	}
	public void setCreateRunLineData(CreateRunLineData createRunLineData) {
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
