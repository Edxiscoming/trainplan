package org.railway.com.trainplanv2.dto;

import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by star on 5/22/14.
 */
public class PlanLineInfoDto2 {

    private String trainName;

    private String startBureau;
    
    private String startBureauShort;

	private String endBureau;
	
	private String endBureauShort;

	private String startStn;

    private String endStn;

    private String startTime;
    private String startTimeShort;

    private String endTime;
    private String endTimeShort;
    
    //2014 12 24

    private String sourceNodeStationId;
    private String sourceNodeStationName;
    private String sourceNodeTdcsId;
    private String sourceNodeTdcsName;
    private String targetNodeStationId;
    private String targetNodeStationName;
    private String targetNodeTdcsId;
    private String targetNodeTdcsName;
    
	private String states;
	private String previousTrailineId;
	private String previousTrailineName;
	private String nextTrailineId;
	private String vehicle;
	private String nextTrailineName;
  


	private String highLineFlag;

    private String  dailyPlanId;
    private String  dailyPlanIdLast;
    private String planTrainId;
    private String passBureau;
    private String endBureauFull;
    private String startBureauFull;
    /*****/
    private String business;
    private String startBureauId;
    private String startStnId;
    private String endBureauId;
    private String endStnId;
    private String endDays;
    private String creatType;
    private String cmdTrainId;
    private String cmdTxtmlItemId;
    private String cmdShortInfo;
    private String telId;
    private String telShortInfo;
    private String trainTypeId;
    private String routeId;
    private String routeName;
    private String baseChartId;
    
    
    
    /*****/
    
    public PlanLineInfoDto2(Map<String, Object> map) {
        this.trainName = MapUtils.getString(map, "TRAIN_NAME","");
        this.startBureau = MapUtils.getString(map, "START_BUREAU","");
        this.startBureauShort = MapUtils.getString(map, "START_BUREAU_SHORT","");
        this.endBureau = MapUtils.getString(map, "END_BUREAU","");
        this.endBureauShort = MapUtils.getString(map, "END_BUREAU_SHORT","");
        this.startStn = MapUtils.getString(map, "START_STN","");
        this.endStn = MapUtils.getString(map, "END_STN","");
        this.startTime = MapUtils.getString(map, "START_TIME");
        this.startTimeShort = MapUtils.getString(map, "START_TIME");
        if(this.startTimeShort.length()>0){
        	this.startTimeShort = this.startTimeShort.substring(5,16).replace("-", "");
        }
        this.endTime = MapUtils.getString(map, "END_TIME");
        this.endTimeShort = MapUtils.getString(map, "END_TIME");
        if(this.endTimeShort.length()>0){
        	this.endTimeShort = this.endTimeShort.substring(5,16).replace("-", "");
        }
        this.dailyPlanId = MapUtils.getString(map, "DAILYPLAN_ID","");
        this.dailyPlanIdLast = MapUtils.getString(map, "DAILYPLAN_ID_LAST","");
        this.planTrainId = MapUtils.getString(map, "PLAN_TRAIN_ID","");
        this.passBureau = MapUtils.getString(map, "PASS_BUREAU","");
        this.endBureauFull = MapUtils.getString(map, "END_BUREAU_FULL","");
        this.startBureauFull = MapUtils.getString(map, "START_BUREAU_FULL","");
        this.business = MapUtils.getString(map, "BUSINESS","");
        this.startBureauId = MapUtils.getString(map, "START_BUREAU_ID","");
        this.startStnId = MapUtils.getString(map, "START_STN_ID","");
        this.endBureauId = MapUtils.getString(map, "END_BUREAU_ID","");
        this.endStnId = MapUtils.getString(map, "END_STN_ID","");
        this.endDays = MapUtils.getString(map, "END_DAYS","");
        this.creatType = MapUtils.getString(map, "CREAT_TYPE","");
        this.cmdShortInfo = MapUtils.getString(map, "CMD_SHORTINFO","");
        this.cmdTrainId = MapUtils.getString(map, "CMD_TRAIN_ID","");
        this.cmdTxtmlItemId = MapUtils.getString(map, "CMD_TXTMLITEMID","");
        this.telId = MapUtils.getString(map, "TEL_ID","");
        this.telShortInfo = MapUtils.getString(map, "TEL_SHORTINFO","");
        this.trainTypeId =  MapUtils.getString(map, "TRAIN_TYPE_ID",""); 
        this.routeId =  MapUtils.getString(map, "ROUTE_ID","");
        this.routeName =  MapUtils.getString(map, "ROUTE_NAME","");
        this.baseChartId = MapUtils.getString(map, "BASE_CHART_ID","");
        this.highLineFlag = MapUtils.getString(map, "HIGHLINE_FLAG","");
        
        this.sourceNodeStationId = MapUtils.getString(map, "SOURCE_NODE_STATION_ID","");
        this.sourceNodeStationName = MapUtils.getString(map, "SOURCE_NODE_STATION_NAME","");
        this.sourceNodeTdcsId = MapUtils.getString(map, "SOURCE_NODE_TDCS_ID","");
        this.sourceNodeTdcsName = MapUtils.getString(map, "SOURCE_NODE_TDCS_NAME","");
        this.targetNodeStationId = MapUtils.getString(map, "TARGET_NODE_STATION_ID","");
        this.targetNodeStationName = MapUtils.getString(map, "TARGET_NODE_STATION_NAME","");
        this.targetNodeTdcsId = MapUtils.getString(map, "TARGET_NODE_TDCS_ID","");
        this.targetNodeTdcsName = MapUtils.getString(map, "TARGET_NODE_TDCS_NAME","");
        this.states = MapUtils.getString(map, "STATES","");
        this.previousTrailineId = MapUtils.getString(map, "PREVIOUS_TRAILINE_ID","");
        this.previousTrailineName = MapUtils.getString(map, "PREVIOUS_TRAILINE_NAME","");
        this.nextTrailineId = MapUtils.getString(map, "NEXT_TRAILINE_ID","");
        this.vehicle = MapUtils.getString(map, "VEHICLE","");
        this.nextTrailineName = MapUtils.getString(map, "NEXT_TRAILINE_NAME","");
        
        
    }

    public String getSourceNodeStationId() {
  		return sourceNodeStationId;
  	}



  	public void setSourceNodeStationId(String sourceNodeStationId) {
  		this.sourceNodeStationId = sourceNodeStationId;
  	}



  	public String getSourceNodeStationName() {
  		return sourceNodeStationName;
  	}



  	public void setSourceNodeStationName(String sourceNodeStationName) {
  		this.sourceNodeStationName = sourceNodeStationName;
  	}



  	public String getSourceNodeTdcsId() {
  		return sourceNodeTdcsId;
  	}



  	public void setSourceNodeTdcsId(String sourceNodeTdcsId) {
  		this.sourceNodeTdcsId = sourceNodeTdcsId;
  	}



  	public String getSourceNodeTdcsName() {
  		return sourceNodeTdcsName;
  	}



  	public void setSourceNodeTdcsName(String sourceNodeTdcsName) {
  		this.sourceNodeTdcsName = sourceNodeTdcsName;
  	}



  	public String getTargetNodeStationId() {
  		return targetNodeStationId;
  	}



  	public void setTargetNodeStationId(String targetNodeStationId) {
  		this.targetNodeStationId = targetNodeStationId;
  	}



  	public String getTargetNodeStationName() {
  		return targetNodeStationName;
  	}



  	public void setTargetNodeStationName(String targetNodeStationName) {
  		this.targetNodeStationName = targetNodeStationName;
  	}



  	public String getTargetNodeTdcsId() {
  		return targetNodeTdcsId;
  	}



  	public void setTargetNodeTdcsId(String targetNodeTdcsId) {
  		this.targetNodeTdcsId = targetNodeTdcsId;
  	}



  	public String getTargetNodeTdcsName() {
  		return targetNodeTdcsName;
  	}



  	public void setTargetNodeTdcsName(String targetNodeTdcsName) {
  		this.targetNodeTdcsName = targetNodeTdcsName;
  	}



  	public String getStartTimeShort() {
  		return startTimeShort;
  	}



  	public void setStartTimeShort(String startTimeShort) {
  		this.startTimeShort = startTimeShort;
  	}



  	public String getEndTimeShort() {
  		return endTimeShort;
  	}



  	public void setEndTimeShort(String endTimeShort) {
  		this.endTimeShort = endTimeShort;
  	}

    
    public String getRouteName() {
		return routeName;
	}



	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}



	public String getRouteId() {
		return routeId;
	}



	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}



	public String getTrainTypeId() {
		return trainTypeId;
	}



	public void setTrainTypeId(String trainTypeId) {
		this.trainTypeId = trainTypeId;
	}



	public String getTelId() {
		return telId;
	}



	public void setTelId(String telId) {
		this.telId = telId;
	}



	public String getTelShortInfo() {
		return telShortInfo;
	}



	public void setTelShortInfo(String telShortInfo) {
		this.telShortInfo = telShortInfo;
	}



	public String getCmdTxtmlItemId() {
		return cmdTxtmlItemId;
	}



	public void setCmdTxtmlItemId(String cmdTxtmlItemId) {
		this.cmdTxtmlItemId = cmdTxtmlItemId;
	}



	public String getCmdShortInfo() {
		return cmdShortInfo;
	}



	public void setCmdShortInfo(String cmdShortInfo) {
		this.cmdShortInfo = cmdShortInfo;
	}



	public String getBusiness() {
		return business;
	}



	public void setBusiness(String business) {
		this.business = business;
	}



	public String getStartBureauId() {
		return startBureauId;
	}


    public String getStartBureauShort() {
		return startBureauShort;
	}

	public void setStartBureauShort(String startBureauShort) {
		this.startBureauShort = startBureauShort;
	}


	public void setStartBureauId(String startBureauId) {
		this.startBureauId = startBureauId;
	}



	public String getStartStnId() {
		return startStnId;
	}



	public void setStartStnId(String startStnId) {
		this.startStnId = startStnId;
	}



	public String getEndBureauId() {
		return endBureauId;
	}



	public void setEndBureauId(String endBureauId) {
		this.endBureauId = endBureauId;
	}


    public String getEndBureauShort() {
		return endBureauShort;
	}



	public void setEndBureauShort(String endBureauShort) {
		this.endBureauShort = endBureauShort;
	}




	public String getEndStnId() {
		return endStnId;
	}



	public void setEndStnId(String endStnId) {
		this.endStnId = endStnId;
	}



	public String getEndDays() {
		return endDays;
	}



	public void setEndDays(String endDays) {
		this.endDays = endDays;
	}



	public String getCreatType() {
		return creatType;
	}



	public void setCreatType(String creatType) {
		this.creatType = creatType;
	}



	public String getPassBureau() {
		return passBureau;
	}


	public void setPassBureau(String passBureau) {
		this.passBureau = passBureau;
	}


	public String getEndBureauFull() {
		return endBureauFull;
	}


	public void setEndBureauFull(String endBureauFull) {
		this.endBureauFull = endBureauFull;
	}


	public String getStartBureauFull() {
		return startBureauFull;
	}


	public void setStartBureauFull(String startBureauFull) {
		this.startBureauFull = startBureauFull;
	}


	public String getDailyPlanId() {
		return dailyPlanId;
	}

	public void setDailyPlanId(String dailyPlanId) {
		this.dailyPlanId = dailyPlanId;
	}

	public String getPlanTrainId() {
		return planTrainId;
	}

	public void setPlanTrainId(String planTrainId) {
		this.planTrainId = planTrainId;
	}

	public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStartBureau() {
        return startBureau;
    }

    public void setStartBureau(String startBureau) {
        this.startBureau = startBureau;
    }

    public String getEndBureau() {
        return endBureau;
    }

    public void setEndBureau(String endBureau) {
        this.endBureau = endBureau;
    }

    public String getStartStn() {
        return startStn;
    }

    public void setStartStn(String startStn) {
        this.startStn = startStn;
    }

    public String getEndStn() {
        return endStn;
    }

    public void setEndStn(String endStn) {
        this.endStn = endStn;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }



	public String getBaseChartId() {
		return baseChartId;
	}
		
	public String getHighLineFlag() {
		return highLineFlag;
	}



	public void setBaseChartId(String baseChartId) {
		this.baseChartId = baseChartId;
	}
		
	public void setHighLineFlag(String highLineFlag) {
		
		this.highLineFlag = highLineFlag;
	}



	public String getDailyPlanIdLast() {
		return dailyPlanIdLast;
	}



	public void setDailyPlanIdLast(String dailyPlanIdLast) {
		this.dailyPlanIdLast = dailyPlanIdLast;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getPreviousTrailineId() {
		return previousTrailineId;
	}

	public void setPreviousTrailineId(String previousTrailineId) {
		this.previousTrailineId = previousTrailineId;
	}

	public String getPreviousTrailineName() {
		return previousTrailineName;
	}

	public void setPreviousTrailineName(String previousTrailineName) {
		this.previousTrailineName = previousTrailineName;
	}

	public String getNextTrailineId() {
		return nextTrailineId;
	}

	public void setNextTrailineId(String nextTrailineId) {
		this.nextTrailineId = nextTrailineId;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getNextTrailineName() {
		return nextTrailineName;
	}

	public void setNextTrailineName(String nextTrailineName) {
		this.nextTrailineName = nextTrailineName;
	}

	public String getCmdTrainId() {
		return cmdTrainId;
	}

	public void setCmdTrainId(String cmdTrainId) {
		this.cmdTrainId = cmdTrainId;
	}
}
