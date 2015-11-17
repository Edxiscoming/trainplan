package org.railway.com.trainplan.service.dto;

/**
 * 高铁乘务查询功能中
 * org.railway.com.trainplan.repository.mybatis.HighLineCrewDao getRecordPeopleOrgList 部门下拉框用了该类
 * @author Think
 *
 */
public class OptionDto {
	
	private String code;
	private String name;
	private String bureauCode;
	private String crhType;
	
	 
	public String getBureauCode() {
		return bureauCode;
	}
	public void setBureauCode(String bureauCode) {
		this.bureauCode = bureauCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCrhType() {
		return crhType;
	}
	public void setCrhType(String crhType) {
		this.crhType = crhType;
	}

}
