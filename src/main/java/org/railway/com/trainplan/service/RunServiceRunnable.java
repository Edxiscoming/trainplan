package org.railway.com.trainplan.service;

import org.railway.com.trainplan.entity.UnitCross;

public class RunServiceRunnable implements Runnable{
	private UnitCross unitCross;
	private String startDate; 
	private int days;
	private String msgReceiveUrl;
	private boolean specialRuleIsSendMsg;
	private RunPlanService2 runPlanService2;
	private int createType = 0;
	private String tokenVehBureau;
	
	public RunServiceRunnable(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, 
			boolean specialRuleIsSendMsg, RunPlanService2 runPlanService2, String tokenVehBureau) {

        this.unitCross = unitCross;
        this.startDate = startDate;
        this.days = days;
        this.specialRuleIsSendMsg = specialRuleIsSendMsg;
        this.msgReceiveUrl = msgReceiveUrl;
        this.runPlanService2 = runPlanService2;
        this.tokenVehBureau = tokenVehBureau;
    }
	
	public RunServiceRunnable(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, 
			boolean specialRuleIsSendMsg, RunPlanService2 runPlanService2, int createType, String tokenVehBureau) {

        this.unitCross = unitCross;
        this.startDate = startDate;
        this.days = days;
        this.specialRuleIsSendMsg = specialRuleIsSendMsg;
        this.msgReceiveUrl = msgReceiveUrl;
        this.runPlanService2 = runPlanService2;
        this.createType = createType;
        this.tokenVehBureau = tokenVehBureau;
    }

	public RunServiceRunnable(UnitCross unitCross, String startDate, int days, String msgReceiveUrl,
			 RunPlanService2 runPlanService2, String tokenVehBureau) {

        this.unitCross = unitCross;
        this.startDate = startDate;
        this.days = days;
        this.msgReceiveUrl = msgReceiveUrl;
        this.runPlanService2 = runPlanService2;
        this.tokenVehBureau = tokenVehBureau;
    }
	
	public RunServiceRunnable(UnitCross unitCross, String startDate, int days, String msgReceiveUrl,
			 RunPlanService2 runPlanService2, int createType, String tokenVehBureau) {

       this.unitCross = unitCross;
       this.startDate = startDate;
       this.days = days;
       this.msgReceiveUrl = msgReceiveUrl;
       this.runPlanService2 = runPlanService2;
       this.createType = createType;
       this.tokenVehBureau = tokenVehBureau;
   }

	@Override
	public void run() {
		runPlanService2.generateRunPlan(unitCross,
				startDate, days, msgReceiveUrl, specialRuleIsSendMsg, createType, tokenVehBureau);
		
	}

}
