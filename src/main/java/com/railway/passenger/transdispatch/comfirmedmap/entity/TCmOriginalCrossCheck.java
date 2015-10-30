package com.railway.passenger.transdispatch.comfirmedmap.entity;

import java.util.UUID;

/**
 * 对数表审核信息
 */
public class TCmOriginalCrossCheck {
    /**
     * 主键
     */
    private String cmOriginalCrossCheckId = UUID.randomUUID().toString();

    /**
     * 对数信息ID
     */
    private String cmOriginalCrossId;

    /**
     * 路局
     */
    private String roadBureau;

    /**
     * 审核状态
     */
    private String checkFlag;

    /**
     * @return 主键
     */
    public String getCmOriginalCrossCheckId() {
        return cmOriginalCrossCheckId;
    }

    /**
     * @param cmOriginalCrossCheckId 
	 *            主键
     */
    public void setCmOriginalCrossCheckId(String cmOriginalCrossCheckId) {
        this.cmOriginalCrossCheckId = cmOriginalCrossCheckId;
    }

    /**
     * @return 对数信息ID
     */
    public String getCmOriginalCrossId() {
        return cmOriginalCrossId;
    }

    /**
     * @param cmOriginalCrossId 
	 *            对数信息ID
     */
    public void setCmOriginalCrossId(String cmOriginalCrossId) {
        this.cmOriginalCrossId = cmOriginalCrossId;
    }

    /**
     * @return 路局
     */
    public String getRoadBureau() {
        return roadBureau;
    }

    /**
     * @param roadBureau 
	 *            路局
     */
    public void setRoadBureau(String roadBureau) {
        this.roadBureau = roadBureau;
    }

    /**
     * @return 审核状态
     */
    public String getCheckFlag() {
        return checkFlag;
    }

    /**
     * @param checkFlag 
	 *            审核状态
     */
    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }
}