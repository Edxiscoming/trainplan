package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息发送表msg_send.
 * 
 * @author zhangPengDong
 *
 *         2015年6月9日 上午10:54:20
 */
public class MsgSend implements Serializable {
	private static final long serialVersionUID = 4256504879521207814L;

	/**
	 * 
	 */
	private String id;
	/**
	 * 类型编码.
	 */
	private String typeCode;
	/**
	 * 消息内容，长度200，哲哥定的.
	 */
	private String msgContents;
	/**
	 * 消息状态(0=未签收；1=部分签收；2=已签收).
	 */
	private String msgStatus;
	/**
	 * 发送局，字母.
	 */
	private Integer sendBureau;
	/**
	 * 发送岗位.
	 */
	private String sendPost;
	/**
	 * 发送人.
	 */
	private String sendPeople;
	/**
	 * 发送时间.
	 */
	private Date sendTime;
	/**
	 * 修改时间.
	 */
	private Date updateTime;

	/**
	 * 
	 */
	private String typeName;

	private List<MsgReceive> receiveList = new ArrayList<MsgReceive>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getMsgContents() {
		return msgContents;
	}

	public void setMsgContents(String msgContents) {
		this.msgContents = msgContents;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	public Integer getSendBureau() {
		return sendBureau;
	}

	public void setSendBureau(Integer sendBureau) {
		this.sendBureau = sendBureau;
	}

	public String getSendPost() {
		return sendPost;
	}

	public void setSendPost(String sendPost) {
		this.sendPost = sendPost;
	}

	public String getSendPeople() {
		return sendPeople;
	}

	public void setSendPeople(String sendPeople) {
		this.sendPeople = sendPeople;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<MsgReceive> getReceiveList() {
		return receiveList;
	}

	public void setReceiveList(List<MsgReceive> receiveList) {
		this.receiveList = receiveList;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
