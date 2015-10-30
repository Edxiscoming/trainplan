package org.railway.com.trainplan.entity;

import java.util.Date;

/**
 * 加开停运命令概要信息
 * @author ITC
 *
 */
public class CmdInfoModel {

	/**
	 * 命令主表ID
	 */
	private Integer cmdTxtMlId;
	/**
	 * 命令子表ID
	 */
	private Integer cmdTxtMlItemId;
	/**
	 * 命令类型
	 * 加开命令：加开
	 * 停运命令：停运
	 */
	private String cmdType;
	/**
	 * 路局命令号
	 */
	private String cmdNbrBureau;
	/**
	 * 项号
	 */
	private Integer cmdItem;
	/**
	 * 总公司命令号
	 */
	private String cmdNbrSuperior;
	
	/**
	 * 发令时间
	 */
	private Date cmdTime;
	
	/**
	 * 发令人
	 */
	private String cmdReleasePeople;
	/**
	 * 车次
	 */
	private String trainNbr;
	/**
	 * 加开命令：始发站名
	 * 停运命令：停运站名 
	 */
	private String startStn;
	/**
	 * 加开命令：终止站名
	 * 停运命令：终止站名，可为空 
	 */
	private String endStn;
	/**
	 * 起始日期
	 */
	private Date startDate;
	/**
	 * 终止日期
	 */
	private Date endDate;
	/**
	 * 开行规律
	 */
	private String rule;
	/**
	 * 加开命令：择日
	 * 停运命令：停运日期
	 */
	private String selectedDate;
	
	/**
	 * 担当局
	 * CmdInfoModel对象做参数时，该字段用作是否查看全局所有或只查看本局的参数
	 * 不为"":只看本局；"":查看全局；默认是查看全局所有
	 */
	private String cmdBureau = "";
	
	/**
	 * 重点揭示命令标记  true:重点揭示命令；默认是false
	 */
	private boolean importCmdFlag = false;
	
	/**
	 * 命令正文
	 */
	private String largeContent;
	
	public Integer getCmdTxtMlId() {
		return cmdTxtMlId;
	}
	public void setCmdTxtMlId(Integer cmdTxtMlId) {
		this.cmdTxtMlId = cmdTxtMlId;
	}
	public Integer getCmdTxtMlItemId() {
		return cmdTxtMlItemId;
	}
	public void setCmdTxtMlItemId(Integer cmdTxtMlItemId) {
		this.cmdTxtMlItemId = cmdTxtMlItemId;
	}
	public String getCmdType() {
		return cmdType;
	}
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}
	public String getCmdNbrBureau() {
		return cmdNbrBureau;
	}
	public void setCmdNbrBureau(String cmdNbrBureau) {
		this.cmdNbrBureau = cmdNbrBureau;
	}
	public Integer getCmdItem() {
		return cmdItem;
	}
	public void setCmdItem(Integer cmdItem) {
		this.cmdItem = cmdItem;
	}
	public String getCmdNbrSuperior() {
		return cmdNbrSuperior;
	}
	public void setCmdNbrSuperior(String cmdNbrSuperior) {
		this.cmdNbrSuperior = cmdNbrSuperior;
	}
	public Date getCmdTime() {
		return cmdTime;
	}
	public void setCmdTime(Date cmdTime) {
		this.cmdTime = cmdTime;
	}
	public String getTrainNbr() {
		return trainNbr;
	}
	public void setTrainNbr(String trainNbr) {
		this.trainNbr = trainNbr;
	}
	public String getStartStn() {
		return startStn;
	}
	public void setStartStn(String startStn) {
		this.startStn = startStn;
	}
	public String getEndStn() {
		return endStn;
	}
	public void setEndStn(String endStn) {
		this.endStn = endStn;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getSelectedDate() {
		return selectedDate;
	}
	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}
	public String getCmdBureau() {
		return cmdBureau;
	}
	public void setCmdBureau(String cmdBureau) {
		this.cmdBureau = cmdBureau;
	}
	public boolean isImportCmdFlag() {
		return importCmdFlag;
	}
	public void setImportCmdFlag(boolean importCmdFlag) {
		this.importCmdFlag = importCmdFlag;
	}
	public String getCmdReleasePeople() {
		return cmdReleasePeople;
	}
	public void setCmdReleasePeople(String cmdReleasePeople) {
		this.cmdReleasePeople = cmdReleasePeople;
	}
	public String getLargeContent() {
		return largeContent;
	}
	public void setLargeContent(String largeContent) {
		this.largeContent = largeContent;
	}
	
}