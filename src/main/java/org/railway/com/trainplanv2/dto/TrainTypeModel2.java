package org.railway.com.trainplanv2.dto;

public class TrainTypeModel2 {
	private String id;//列车类型id
	private String name;//列车类型名称
	private String description;//列车类型描述
	private String business;//列车作业内容
	private int grade;//列车等级
	private String identify;//列车标识
	private String shortName;//简称
	private int sourceCode;//车次范围，开始车次
	private int targetCode;//车次范围，结束车次
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public int getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(int sourceCode) {
		this.sourceCode = sourceCode;
	}
	public int getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(int targetCode) {
		this.targetCode = targetCode;
	}
	
	
	
}
