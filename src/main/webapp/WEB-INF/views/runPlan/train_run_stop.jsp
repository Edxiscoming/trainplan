<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/jquery.multiselect2side.css" />
</head>
<body class="Iframe_body">
<input id="trainRunTime_trainPlanId_hidden" type="hidden" value="${trainPlanId}">
<input id="trainRunTime_trainNbr_hidden" type="hidden" value="${trainNbr}">
<input id="trainRunTime_runDate_hidden" type="hidden" value="${runDate}">
<input id="trainRunTime_startStn_hidden" type="hidden" value="${startStn}">
<input id="trainRunTime_endStn_hidden" type="hidden" value="${endStn}">
<input id="trainRunTime_maxRunDate_hidden" type="hidden" value="${maxRunDate}">
<input id="trainRunTime_trainStnSource_hidden" type="hidden" value="${trainStnSource}">
<input id="trainRunTime_planCrossId_hidden" type="hidden" value="${planCrossId}">




<div id="div_form" class="row" style="padding-top:10px;padding-bottom:10px;">
  <form class="form-horizontal" role="form">
    <div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left" data-bind="html: currentTrainInfoMessage()"></label>
    </div>
    <a type="button" href="#" class="btn btn-success btn-xs" data-bind="click: saveTrainTime" style="float:right;margin-right:20px;margin-bottom:0;"><i class="fa fa-floppy-o"></i>保存</a>
  </form>
</div>


    <div class="panel panel-default">
      
      <div class="panel-body">
      
      	<!-- 择日 -->
		<div class="row" style="margin-top: 0px">
			<div class="row" style="margin-top: 0px;margin-left:-15px;">
				<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:15px;">停运方式：&nbsp;</label>
				<div class="pull-left" style="margin-top: 0px;">
	              	<select class="form-control" style="width: 100px;display:inline-block;"
						 data-bind="options: stopTypeArray, value: stopTypeOption, optionsText: 'text'">
				  	</select>
	            </div>
			</div>
			
			<div class="row" style="margin-top: 10px;margin-left:-15px;">
				<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:15px;">停运依据：&nbsp;</label>
				<div class="pull-left" style="margin-top: 0px;">
	            	<textarea rows="3" cols="62" name="telName" id="textarea_trainRunTime_telName">${telName}</textarea>
	            </div>
			</div>
	      	<form class="form-horizontal" role="form">
	      	
			<div class="row" style="margin-top: 10px">
				<div class="form-group">
					<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:15px;">开始日期：&nbsp;</label>
				    <div class="pull-left">
				        <input id="input_trainRunTime_startDate" type="text" class="form-control" style="width:120px;"/>
					</div>
					
					<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:10px;">截至日期：&nbsp;</label>
				 	<div class="pull-left">
			        <input id="input_trainRunTime_endDate" type="text" class="form-control" style="width:120px;" data-bind="disable: cmdEndFlag" />
					</div>
					
					
				
					
				 	<div class="pull-left">
			        <input type="checkbox" style="margin-top: 10px;margin-left:10px;" data-bind="checked: cmdEndFlag, event: { change: cmdEndFlagChange}" disabled/></select>
					</div>
					<label for="exampleInputEmail2" class="control-label pull-left" >令止&nbsp;</label>
				</div>
			</div>
			<div class="row">
					<label class="control-label pull-left" style="margin-left:0px;">停运规律：</label>
	            	<div class="pull-left" style="margin-left:3px;">
	              	<select class="form-control" style="width: 100px;display:inline-block;"
						 data-bind="options: runRuleArray, value: runRuleOption, optionsText: 'text', event: { change: runRuleChange }"><!-- event: { mouseover: $root.selectorChange, mouseup: $root.selectorChange }, -->
				  	</select>
	            	</div>
	            	<div class="pull-left" style="margin-left:30px;">
	            		<button type="button" data-bind="click: btnLoadSelectDate, enable: runRuleOption().code == '3' && !cmdEndFlag()">加载</button>
	            	</div>
			</div>
			<div class="row" style="margin-top: 10px">
				<div class="form-group">
			     	<div class="row" style="width:482px; height:auto; float:left; display:inline;margin-left:15px;">
			      		<select name="liOption[]" id='liOption' multiple='multiple' size='4'></select>
			     	</div>
			     	
			     	<div class="pull-left" style="margin-top:104px;height:auto; float:left; display:inline;margin-left:15px;">
		  				 <button type="button" data-bind="click: btnSubmitSelectDate, enable: runRuleOption().code == '3' && !cmdEndFlag()">确定</button>
	  				</div>
	          	</div>
	          	
			</div>
			
			<div class="row" style="margin-bottom: 15px">
				<div class="form-group">
				
	  			<label class="control-label pull-left" style="margin-left:15px;">择日日期：</label>
	            <div class="pull-left">
	            	<textarea rows="3" cols="62"  data-bind="value : selectedDate, enable: false"></textarea>
	            </div>
	            </div>
	  		</div>
	  		
	  					
			</form>
		</div>
      	<!-- 择日  end-->
      
      	<!-- 时刻table -->
      	<!-- 
		<div class="row">
        <div class="table-responsive table-hover" style="overflow:auto">
          <table id="table_run_plan_train_times_edit" class="table table-bordered table-striped table-hover">
            <thead>
		        <tr>
		          <th style="width:30px">序号</th>
                  <th style="width:200px">站名</th>
                  <th style="width:40px">路局</th>
                  <th style="width:200px">到达时间</th>
                  <th style="width:200px">出发时间</th>
                  <th style="width:80px">停时</th>
                  <th style="width:70px">到达天数</th>
                  <th style="width:70px">出发天数</th>
                  <th style="width:70px" colspan="2">股道</th>  
                 </tr>
            </thead>
            <tbody data-bind="foreach: trainStns">
	           <tr data-bind="attr:{class : isChangeValue()==1? 'danger':''}">
				<td align="center" data-bind=" text: $index() + 1"></td>
				<td data-bind="text: stnName, attr:{title: stnName}"></td>
				<td align="center" data-bind="text: bureauShortName"></td>
				<td align="center"><input  style="text-align:center"  data-bind="value: arrTime, event:{change: onArrTimeChange}"/></td>
				<td align="center"><input  style="text-align:center"  data-bind="value: dptTime, event:{change: onDeptTimeChange}"/></td>
				<td align="center" data-bind="text: stepStr"></td>
				<td align="center" data-bind="text: arrRunDays"></td>
				<td align="center" data-bind="text: runDays"></td>
				<td align="center"><input  data-bind="value: trackName, event:{change: onTrackNameChange}"/></td>
	        	</tr>
	        </tbody>
          </table>
        </div>
        </div>
         -->
      	<!-- 时刻table end -->
        
        
      </div>
    </div>











<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/multiselect2side.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>

<script src="<%=basePath %>/assets/js/trainplan/runPlan/train_run_stop.js"></script>
</body>
</html>
