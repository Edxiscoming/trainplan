package org.railway.com.trainplan.entity.jdrunline;

public class JDMessage {
	private JDMessageHeader header;
	private JDMessageBody body;
	
	public JDMessageHeader getHeader() {
		return header;
	}
	public void setHeader(JDMessageHeader header) {
		this.header = header;
	}
	public JDMessageBody getBody() {
		return body;
	}
	public void setBody(JDMessageBody body) {
		this.body = body;
	}
	
	
}
