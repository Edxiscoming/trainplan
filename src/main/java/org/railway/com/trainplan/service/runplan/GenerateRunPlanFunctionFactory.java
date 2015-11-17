package org.railway.com.trainplan.service.runplan;

import java.text.SimpleDateFormat;

import org.railway.com.trainplan.entity.UnitCross;

public class GenerateRunPlanFunctionFactory {
	private UnitCross unitCross;

    private String startDate;

    private int days;

    private String msgReceiveUrl;

    private boolean specialRuleIsSendMsg = false;
    
    public GenerateRunPlanFunctionFactory(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, boolean specialRuleIsSendMsg, int type) {
    	  this.unitCross = unitCross;
          this.startDate = startDate;
          this.days = days;
          this.specialRuleIsSendMsg = specialRuleIsSendMsg;
          this.msgReceiveUrl = msgReceiveUrl;
    }
    
    public GenerateRunPlanFunctionFactory(UnitCross unitCross, String startDate, int days, String msgReceiveUrl, int type) {
    	 this.unitCross = unitCross;
         this.startDate = startDate;
         this.days = days;
         this.msgReceiveUrl = msgReceiveUrl;
    }
}
