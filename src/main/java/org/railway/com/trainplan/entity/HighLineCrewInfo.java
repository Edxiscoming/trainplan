package org.railway.com.trainplan.entity;

public class HighLineCrewInfo {
    
	private String index;
	private String crewHighlineId;
	private String crewDate;
	private String crewBureau;
	//乘务类型（1:车长、2:司机、3:机械师）
	private String crewType;
	//乘务交路
	private String crewCross;
	//车队组号
	private String crewGroup;
	//经由铁路线
	private String throughLine;
	//乘务员1姓名
	private String name1;
	//乘务员1电话
	private String tel1;
	//乘务员1身份
	private String identity1;
	//乘务员2姓名
	private String name2;
	//乘务员2电话
	private String tel2;
	//乘务员2身份
	private String identity2;
	//
	private String note;
	private String recordPeople;
	private String recordPeopleOrg;
	private String recordTime;
	//提交状态（0：编辑，1：提交）
	private int submitType;
	private  int sortId;//排序id
	
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	public String getCrewHighlineId() {
		return crewHighlineId;
	}
	public void setCrewHighlineId(String crewHighlineId) {
		this.crewHighlineId = crewHighlineId;
	}
	public String getCrewDate() {
		return crewDate;
	}
	public void setCrewDate(String crewDate) {
		this.crewDate = crewDate;
	}
	public String getCrewBureau() {
		return crewBureau;
	}
	public void setCrewBureau(String crewBureau) {
		this.crewBureau = crewBureau;
	}
	public String getCrewType() {
		return crewType;
	}
	public void setCrewType(String crewType) {
		this.crewType = crewType;
	}
	public String getCrewCross() {
		return crewCross;
	}
	public void setCrewCross(String crewCross) {
		this.crewCross = crewCross;
	}
	public String getCrewGroup() {
		return crewGroup;
	}
	public void setCrewGroup(String crewGroup) {
		this.crewGroup = crewGroup;
	}
	public String getThroughLine() {
		return throughLine;
	}
	public void setThroughLine(String throughLine) {
		this.throughLine = throughLine;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getTel1() {
		return tel1;
	}
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	public String getIdentity1() {
		return identity1;
	}
	public void setIdentity1(String identity1) {
		this.identity1 = identity1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getIdentity2() {
		return identity2;
	}
	public void setIdentity2(String identity2) {
		this.identity2 = identity2;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getRecordPeople() {
		return recordPeople;
	}
	public void setRecordPeople(String recordPeople) {
		this.recordPeople = recordPeople;
	}
	public String getRecordPeopleOrg() {
		return recordPeopleOrg;
	}
	public void setRecordPeopleOrg(String recordPeopleOrg) {
		this.recordPeopleOrg = recordPeopleOrg;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public int getSubmitType() {
		return submitType;
	}
	public void setSubmitType(int submitType) {
		this.submitType = submitType;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}

	
}
