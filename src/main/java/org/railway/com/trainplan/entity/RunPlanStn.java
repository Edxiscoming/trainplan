package org.railway.com.trainplan.entity;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by speeder on 2014/5/28.
 */
public class RunPlanStn {

    private String planTrainStnId;
    private String planTrainId;
    private int stnSort;
    private String stnName;
    private String stnBureauShortName;
    private String stnBureauFullName;
    private String arrTrainNbr;
    private String dptTrainNbr;
    private Timestamp arrTime;
    private String arrTimeStr;
    private Timestamp dptTime;
    private String dptTimeStr;
    private Timestamp baseArrTime;
    private Timestamp baseDptTime;
    private String baseArrTimeStr;
    private String baseDptTimeStr;
    private String upDown;
    private String trackNbr;
    private String trackName;
    private int platform;
    private int psgFlag;
    private int locoFlag;
    private String tecType;
    private String stnType;
    private int boundaryInOut;
    private int sRunDays;
    private int tRunDays;
    private String jobs;
    private String bureauId;
    private String nodeId;
    private String nodeName;
 
    private String nodeStationId;
    private String nodeStationName;
    private String nodeTdcsId;
    private String nodeTdcsName;
    
    
    public String getNodeStationId() {
		return nodeStationId;
	}

	public void setNodeStationId(String nodeStationId) {
		this.nodeStationId = nodeStationId;
	}

	public String getNodeStationName() {
		return nodeStationName;
	}

	public void setNodeStationName(String nodeStationName) {
		this.nodeStationName = nodeStationName;
	}

	public String getNodeTdcsId() {
		return nodeTdcsId;
	}

	public void setNodeTdcsId(String nodeTdcsId) {
		this.nodeTdcsId = nodeTdcsId;
	}

	public String getNodeTdcsName() {
		return nodeTdcsName;
	}

	public void setNodeTdcsName(String nodeTdcsName) {
		this.nodeTdcsName = nodeTdcsName;
	}

	public String getJobs() {
		return jobs;
	}

	public void setJobs(String jobs) {
		this.jobs = jobs;
	}

	public String getBureauId() {
		return bureauId;
	}

	public void setBureauId(String bureauId) {
		this.bureauId = bureauId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getBaseArrTimeStr() {
		return baseArrTimeStr;
	}

	public void setBaseArrTimeStr(String baseArrTimeStr) {
		this.baseArrTimeStr = baseArrTimeStr;
	}

	public String getBaseDptTimeStr() {
		return baseDptTimeStr;
	}

	public void setBaseDptTimeStr(String baseDptTimeStr) {
		this.baseDptTimeStr = baseDptTimeStr;
	}

	public String getPlanTrainStnId() {
        return planTrainStnId;
    }

    public void setPlanTrainStnId(String planTrainStnId) {
        this.planTrainStnId = planTrainStnId;
    }

    public String getPlanTrainId() {
        return planTrainId;
    }

    public void setPlanTrainId(String planTrainId) {
        this.planTrainId = planTrainId;
    }

    public int getStnSort() {
        return stnSort;
    }

    public void setStnSort(int stnSort) {
        this.stnSort = stnSort;
    }

    public String getStnName() {
        return stnName;
    }

    public void setStnName(String stnName) {
        this.stnName = stnName;
    }

    public String getStnBureauShortName() {
        return stnBureauShortName;
    }

    public void setStnBureauShortName(String stnBureauShortName) {
        this.stnBureauShortName = stnBureauShortName;
    }

    public String getStnBureauFullName() {
        return stnBureauFullName;
    }

    public void setStnBureauFullName(String stnBureauFullName) {
        this.stnBureauFullName = stnBureauFullName;
    }

    public String getArrTrainNbr() {
        return arrTrainNbr;
    }

    public void setArrTrainNbr(String arrTrainNbr) {
        this.arrTrainNbr = arrTrainNbr;
    }

    public String getDptTrainNbr() {
        return dptTrainNbr;
    }

    public void setDptTrainNbr(String dptTrainNbr) {
        this.dptTrainNbr = dptTrainNbr;
    }

    public Timestamp getArrTime() {
        return arrTime;
    }

    public void setArrTime(Timestamp arrTime) {
        this.arrTime = arrTime;
    }

    public Timestamp getDptTime() {
        return dptTime;
    }

    public void setDptTime(Timestamp dptTime) {
        this.dptTime = dptTime;
    }

    public Timestamp getBaseArrTime() {
        return baseArrTime;
    }

    public void setBaseArrTime(Timestamp baseArrTime) {
        this.baseArrTime = baseArrTime;
    }

    public Timestamp getBaseDptTime() {
        return baseDptTime;
    }

    public void setBaseDptTime(Timestamp baseDptTime) {
        this.baseDptTime = baseDptTime;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    public String getTrackNbr() {
        return trackNbr;
    }

    public void setTrackNbr(String trackNbr) {
        this.trackNbr = trackNbr;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getPsgFlag() {
        return psgFlag;
    }

    public void setPsgFlag(int psgFlag) {
        this.psgFlag = psgFlag;
    }

    public int getLocoFlag() {
        return locoFlag;
    }

    public void setLocoFlag(int locoFlag) {
        this.locoFlag = locoFlag;
    }

    public String getTecType() {
        return tecType;
    }

    public void setTecType(String tecType) {
        this.tecType = tecType;
    }

    public String getStnType() {
        return stnType;
    }

    public void setStnType(String stnType) {
        this.stnType = stnType;
    }

    public int getBoundaryInOut() {
        return boundaryInOut;
    }

    public void setBoundaryInOut(int boundaryInOut) {
        this.boundaryInOut = boundaryInOut;
    }

    public int getsRunDays() {
		return sRunDays;
	}

	public void setsRunDays(int sRunDays) {
		this.sRunDays = sRunDays;
	}

	public int gettRunDays() {
		return tRunDays;
	}

	public void settRunDays(int tRunDays) {
		this.tRunDays = tRunDays;
	}

	public String getArrTimeStr() {
        return arrTimeStr;
    }

    public void setArrTimeStr(String arrTimeStr) {
        this.arrTimeStr = arrTimeStr;
    }

    public String getDptTimeStr() {
        return dptTimeStr;
    }

    public void setDptTimeStr(String dptTimeStr) {
        this.dptTimeStr = dptTimeStr;
    }
}
