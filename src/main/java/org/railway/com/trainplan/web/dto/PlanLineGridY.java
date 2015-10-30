package org.railway.com.trainplan.web.dto;

/**
 * Created by star on 5/14/14.
 */
public class PlanLineGridY {
    private String stnName;
    private int isCurrentBureau;
    //车站类型（1:发到站，2:分界口，3:停站,4:不停站）
    private String stationType;
//    private String dptTime;
//    private int stnSort;
    public PlanLineGridY(String stnName,int isCurrentBureau,String dptTime,String stationType){
    	this(stnName);
    	this.isCurrentBureau = isCurrentBureau;
//    	this.dptTime = dptTime;
    	this.stationType = stationType;
    }
    public PlanLineGridY(String stnName,String stationType,int stnSort){
    	this.stnName = stnName;
    	this.stationType = stationType;
//    	this.stnSort = stnSort;
    }
    public PlanLineGridY(String stnName,int isCurrentBureau,String stationType){
    	this(stnName);
    	this.isCurrentBureau = isCurrentBureau;
    	this.stationType = stationType;
    }
    public PlanLineGridY(String stnName) {
        this.stnName = stnName;
    }

    
//    public int getStnSort() {
//		return stnSort;
//	}
//	public void setStnSort(int stnSort) {
//		this.stnSort = stnSort;
//	}
//	public String getDptTime() {
//		return dptTime;
//	}
//	public void setDptTime(String dptTime) {
//		this.dptTime = dptTime;
//	}
	public String getStnName() {
        return stnName;
    }

    public void setStnName(String stnName) {
        this.stnName = stnName;
    }
	public int getIsCurrentBureau() {
		return isCurrentBureau;
	}
	public void setIsCurrentBureau(int isCurrentBureau) {
		this.isCurrentBureau = isCurrentBureau;
	}
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
    
    
}
