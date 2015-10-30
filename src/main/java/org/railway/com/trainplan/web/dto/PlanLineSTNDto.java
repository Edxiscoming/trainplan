package org.railway.com.trainplan.web.dto;

import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by star on 5/12/14.
 */
public class PlanLineSTNDto {
    // 记录id
//    private String id;
    
    private String bureauShortName;
    
    // 站顺序
//    private int index;

    // 站名
    private String stnName;

    // 所属局
//    private String bureau;
    // 所属局简称
//    private String stnBureau;
    

	// 到站时间
    private String arrTime;

    private String dptTime;

//    private String trackName;
    //停留时间
//    private int stayTime;

//    private int psg;

    private boolean owner;
    private String stationType;

    public PlanLineSTNDto(){}
    public PlanLineSTNDto(Map<String, Object> map) {
//        this.index = MapUtils.getIntValue(map, "STN_INDEX");
        this.stnName = MapUtils.getString(map, "STN_NAME");
//        this.bureau = MapUtils.getString(map, "BUREAU");
//        this.trackName = MapUtils.getString(map, "TRACK_NAME");
        this.arrTime = MapUtils.getString(map, "ARR_TIME");
        this.dptTime = MapUtils.getString(map, "DPT_TIME");
    }

    public PlanLineSTNDto(Map<String, Object> map, String bureau) {
//        this.index = MapUtils.getIntValue(map, "STN_INDEX");
        this.stnName = MapUtils.getString(map, "STN_NAME");
//        this.bureau = MapUtils.getString(map, "BUREAU");
//        this.stnBureau = MapUtils.getString(map, "STNBUREAU");
//        this.trackName = MapUtils.getString(map, "TRACK_NAME");
        this.arrTime = MapUtils.getString(map, "ARR_TIME", "");
        if(this.arrTime.length() > 0) {
            this.arrTime = this.arrTime.substring(5, 16).replace("-", "");
        }
        this.dptTime = MapUtils.getString(map, "DPT_TIME", "");
        if(this.dptTime.length() > 0) {
            this.dptTime = this.dptTime.substring(5, 16).replace("-", "");
        }
        this.owner = MapUtils.getString(map, "STN_BUREAU", "").equals(bureau);
    }

    public PlanLineSTNDto(Map<String, Object> map, String bureau, String id) {
//        this.id = id;
//        this.index = MapUtils.getIntValue(map, "STN_INDEX");
        this.stnName = MapUtils.getString(map, "STN_NAME");
//        this.bureau = MapUtils.getString(map, "BUREAU");
//        this.trackName = MapUtils.getString(map, "TRACK_NAME");
        this.arrTime = MapUtils.getString(map, "ARR_TIME");
        this.dptTime = MapUtils.getString(map, "DPT_TIME");
        this.owner = MapUtils.getString(map, "STN_BUREAU", "").equals(bureau);
    }

    
//    public int getStayTime() {
//		return stayTime;
//	}
//	public void setStayTime(int stayTime) {
//		this.stayTime = stayTime;
//	}
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
//	public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getStnName() {
        return stnName;
    }

    public void setStnName(String stnName) {
        this.stnName = stnName;
    }
//
//    public String getBureau() {
//        return bureau;
//    }
//
//    public void setBureau(String bureau) {
//        this.bureau = bureau;
//    }
    
//    public String getStnBureau() {
//		return stnBureau;
//	}
//    
//	public void setStnBureau(String stnBureau) {
//		this.stnBureau = stnBureau;
//	}

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDptTime() {
        return dptTime;
    }

    public void setDptTime(String dptTime) {
        this.dptTime = dptTime;
    }

//    public String getTrackName() {
//        return trackName;
//    }
//
//    public void setTrackName(String trackName) {
//        this.trackName = trackName;
//    }
//
//    public int getPsg() {
//        return psg;
//    }
//
//    public void setPsg(int psg) {
//        this.psg = psg;
//    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

//    public int getIndex() {
//        return index;
//    }
//
//    public void setIndex(int index) {
//        this.index = index;
//    }
	public String getBureauShortName() {
		return bureauShortName;
	}
	public void setBureauShortName(String bureauShortName) {
		this.bureauShortName = bureauShortName;
	}
}