package org.railway.com.trainplan.entity.jdrunline;

public class JDMessageHeader {
	//消息类型（1.“发布计划”; 2.“落成计划”）
	private String messageType;
	//线路类型（0.“既有”; 1.高铁”）
	private String lineType;
	//开始时间
	private String startTime;
	//截至时间
	private String endTime;
	//路局（界面上点击“发布”或“落成”按钮的用户所属局）
	private String bureau;
	//人员（界面上点击“发布”或“落成”按钮的用户）
	private String user;
	//处理时间（界面上点击“发布”或“落成”按钮的时间）
	private String handleTime;
	
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}
	
	
	
	
}
