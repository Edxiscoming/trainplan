package org.railway.com.trainplan.entity;

public class SchemeInfo {

    private String schemeId;
    private String schemeName;
    private String publishTime;
    private String executionTime;

    public String getSchemeId() {
        return schemeId;
    }
    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }
    public String getSchemeName() {
        return schemeName;
    }
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

}