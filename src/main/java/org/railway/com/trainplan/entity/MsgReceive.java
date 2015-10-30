package org.railway.com.trainplan.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息接收表msg_receive.
 * 
 * @author zhangPengDong
 *
 *         2015年6月9日 上午10:58:37
 */
public class MsgReceive implements Serializable {
	private static final long serialVersionUID = 4256504879521207814L;

	/**
	 * 
	 */
	private String id;
	/**
	 * 发送ID.
	 */
	private String sendId;
	/**
	 * 消息状态(0=新消息；1=已签收；2=已删除).
	 */
	private Integer msgStatus;
	/**
	 * 接收局.
	 */
	private String receiveBureau;
	/**
	 * 接收岗位.
	 */
	private String receivePost;
	/**
	 * 接收人.
	 */
	private String receivePeople;
	/**
	 * 接收时间.
	 */
	private Date receiveTime;
	/**
	 * 签收人.
	 */
	private String qsPeople;
	/**
	 * 签收时间.
	 */
	private Date qsTime;
	/**
	 * 修改时间.
	 */
	private Date updateTime;
	
	/** SQL **/
	private String typeName;
	private String msgContents;
	private String sendBureau;
	private String sendPost;
	private String sendPeople;
	private String sendTime;
	private String receiveTime1;
	private String qsTime1;
	private String bureauName;
	private String receiveStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public Integer getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(Integer msgStatus) {
		this.msgStatus = msgStatus;
	}

	public String getReceiveBureau() {
		return receiveBureau;
	}

	public void setReceiveBureau(String receiveBureau) {
		this.receiveBureau = receiveBureau;
	}

	public String getReceivePost() {
		return receivePost;
	}

	public void setReceivePost(String receivePost) {
		this.receivePost = receivePost;
	}

	public String getReceivePeople() {
		return receivePeople;
	}

	public void setReceivePeople(String receivePeople) {
		this.receivePeople = receivePeople;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getQsPeople() {
		return qsPeople;
	}

	public void setQsPeople(String qsPeople) {
		this.qsPeople = qsPeople;
	}

	public Date getQsTime() {
		return qsTime;
	}

	public void setQsTime(Date qsTime) {
		this.qsTime = qsTime;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getMsgContents() {
		return msgContents;
	}

	public void setMsgContents(String msgContents) {
		this.msgContents = msgContents;
	}

	public String getSendBureau() {
		return sendBureau;
	}

	public void setSendBureau(String sendBureau) {
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

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getBureauName() {
		return bureauName;
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public void setBureauName(String bureauName) {
		this.bureauName = bureauName;
	}

	public String getReceiveTime1() {
		return receiveTime1;
	}

	public void setReceiveTime1(String receiveTime1) {
		this.receiveTime1 = receiveTime1;
	}

	public String getQsTime1() {
		return qsTime1;
	}

	public void setQsTime1(String qsTime1) {
		this.qsTime1 = qsTime1;
	}

}
