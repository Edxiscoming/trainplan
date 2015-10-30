package org.railway.com.trainplan.entity;



/**
 * 数据字典列车类型对象
 * @author join
 *
 */
public class TrainType  {

	/**
	 * ID	VARCHAR2(36)
NAME	VARCHAR2(50)
PINYINCODE	VARCHAR2(25)
SHORT_NAME	VARCHAR2(50)
ZUOYE_DENGJI	VARCHAR2(50)
LIECHE_DENGJI	NUMBER(2)
CHECI_BIAOSHI	VARCHAR2(3)
	 */
	
	private String id;
	private String name;
	private String pinYinCode;
	private String shortName;
	//作业等级，表明是货车还是客运
	private String zuoyeDengji;
	private String liecheDengji;
	private String checiBiaoshi;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinYinCode() {
		return pinYinCode;
	}
	public void setPinYinCode(String pinYinCode) {
		this.pinYinCode = pinYinCode;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getZuoyeDengji() {
		return zuoyeDengji;
	}
	public void setZuoyeDengji(String zuoyeDengji) {
		this.zuoyeDengji = zuoyeDengji;
	}
	public String getLiecheDengji() {
		return liecheDengji;
	}
	public void setLiecheDengji(String liecheDengji) {
		this.liecheDengji = liecheDengji;
	}
	public String getCheciBiaoshi() {
		return checiBiaoshi;
	}
	public void setCheciBiaoshi(String checiBiaoshi) {
		this.checiBiaoshi = checiBiaoshi;
	}
	
	
}
