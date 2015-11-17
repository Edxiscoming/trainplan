package org.railway.com.trainplan.entity.runline;


public class MessageBody {
	private MqHead head;
	private Object param;
	
	
	public MqHead getHead() {
		return head;
	}
	public void setHead(MqHead head) {
		this.head = head;
	}
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	
	
}
