package com.railway.passenger.transdispatch.comfirmedmap.entity;

/**
 * 集中基本图版本
 */
public class TCmVersion {
    /**
     * 主键
     */
    private String cmVersionId;

    /**
     * 原始基本图版本ID
     */
    private String mTemplateScheme;

    /**
     * 版本名称
     */
    private String name;

    /**
     * 版本说明
     */
    private String description;

    /**
     * 版本发布时间
     */
    private String publishTime;

    /**
     * 版本生效时间
     */
    private String executionTime;

    /**
     * 使用状态
     */
    private Short useStatus;

    /**
     * @return 主键
     */
    public String getCmVersionId() {
        return cmVersionId;
    }

    /**
     * @param cmVersionId 
	 *            主键
     */
    public void setCmVersionId(String cmVersionId) {
        this.cmVersionId = cmVersionId;
    }

    /**
     * @return 原始基本图版本ID
     */
    public String getmTemplateScheme() {
        return mTemplateScheme;
    }

    /**
     * @param mTemplateScheme 
	 *            原始基本图版本ID
     */
    public void setmTemplateScheme(String mTemplateScheme) {
        this.mTemplateScheme = mTemplateScheme;
    }

    /**
     * @return 版本名称
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            版本名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 版本说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            版本说明
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 版本发布时间
     */
    public String getPublishTime() {
        return publishTime;
    }

    /**
     * @param publishTime 
	 *            版本发布时间
     */
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * @return 版本生效时间
     */
    public String getExecutionTime() {
        return executionTime;
    }

    /**
     * @param executionTime 
	 *            版本生效时间
     */
    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * @return 使用状态
     */
    public Short getUseStatus() {
        return useStatus;
    }

    /**
     * @param useStatus 
	 *            使用状态
     */
    public void setUseStatus(Short useStatus) {
        this.useStatus = useStatus;
    }
}