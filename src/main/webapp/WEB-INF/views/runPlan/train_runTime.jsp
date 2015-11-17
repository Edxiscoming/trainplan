<%@page import="javax.sound.midi.SysexMessage"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<%  
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}
String userLoginBureauShortName = user.getBureauShortName();
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title></title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/jquery.multiselect2side.css" />
<style type="text/css">
.form-horizontal .control-label, .form-horizontal .radio, .form-horizontal .checkbox, .form-horizontal .radio-inline, .form-horizontal .checkbox-inline{
padding-top: 4px;
}
.ms2side__div select {
width: 148px;
float: left;
}
.form-control{
padding:3px 1px
}
</style>
</head>
<body class="">
<input id="trainRunTime_trainPlanId_hidden" type="hidden" value="${trainPlanId}">
<input id="trainRunTime_trainNbr_hidden" type="hidden" value="${trainNbr}">
<input id="trainRunTime_runDate_hidden" type="hidden" value="${runDate}">
<input id="trainRunTime_startStn_hidden" type="hidden" value="${startStn}">
<input id="trainRunTime_endStn_hidden" type="hidden" value="${endStn}">
<input id="trainRunTime_maxRunDate_hidden" type="hidden" value="${maxRunDate}">
<input id="trainRunTime_trainStnSource_hidden" type="hidden" value="${trainStnSource}">

<input id="userLoginBureauShortName" type="hidden" value="<%=userLoginBureauShortName %>">



<div id="div_form" class="row" style="padding-bottom:10px;">
  <form class="form-horizontal" role="form">
    <div class="form-group" style="float:left;margin-left:12px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left" data-bind="html: currentTrainInfoMessage()"></label>
    </div>
    <a type="button" id="isSave" href="#" class="btn btn-success btn-xs" data-bind="click: saveTrainTime" style="float:right;margin-right:20px;margin-top:4px;"><i class="fa fa-floppy-o"></i>保存</a>
  </form>
</div>


    <div class="panel panel-default">
      
      <div class="panel-body">
      
      	<!-- 择日 -->
		<div class="row" style="margin-top: 0px;width: 345px;float:left">
			<div class="row" style="margin-top: 0px;">
				<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:;">调整依据：</label>
				<div class="pull-left" style="margin-top: 0px;">
	            	<textarea rows="3" cols="62" name="telName" class="form-control" style="width: 270px;" id="textarea_trainRunTime_telName" onfocus="if(value == '请填写调整依据！'){value = ''}" onblur="if (value == ''){value = '请填写调整依据！'}">${telName}</textarea>
	            </div>
			</div>
	      	<form class="form-horizontal" role="form">
	      	
			<div class="row" style="margin-bottom: 10px;">
				<div class="form-group00">
					<label for="exampleInputEmail2" class="control-label pull-left" style="">开始日期：</label>
				    <div class="pull-left">
				        <input id="input_trainRunTime_startDate" type="text" class="form-control" style="width:77px;padding: 4px 4px;"/>
					</div>
					
					<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:7px;">截至日期：</label>
				 	<div class="pull-left">
			        <input id="input_trainRunTime_endDate" type="text" class="form-control" style="width:77px;padding: 4px 4px;" data-bind="disable: cmdEndFlag" />
					</div>
					
					
				
					<!-- 
				 	<div class="pull-left">
			        <input type="checkbox" style="margin-top: 7px;margin-left:4px;" data-bind="checked: cmdEndFlag, event: { change: cmdEndFlagChange}" disabled/></select>
					</div>
					<label for="exampleInputEmail2" class="control-label pull-left" >令止</label>
					 -->
				</div>
			</div>
			<div class="row"  style="margin-bottom: 10px;">
					<label class="control-label pull-left" style="margin-left:0px;">开行规律：</label>
	            	<div class="pull-left" style="margin-left:3px;">
	              	<select class="form-control" style="width: 74px;display:inline-block;padding: 4px 4px;"
						 data-bind="options: runRuleArray, value: runRuleOption, optionsText: 'text', event: { change: runRuleChange }"><!-- event: { mouseover: $root.selectorChange, mouseup: $root.selectorChange }, -->
				  	</select>
	            	</div>
	            	<div class="pull-left" style="margin-left:8px; margin-top:1px;">
	            		<button type="button" data-bind="click: btnLoadSelectDate, enable: runRuleOption().code == '3' && !cmdEndFlag()">加载</button>
	            	</div>
			</div>
			<div class="row">
				<div class="form-group" style="position: relative;width:338px;">
			     	<div class="row" style="width:338px; height:auto; float:left; display:inline;margin-left:15px;">
			      		<select name="liOption[]" id='liOption' multiple='multiple' size='4'></select>
			     	</div>
			     	
			     	<div class="pull-left" style="margin-top:104px;height:auto; float:left; display:inline;position: absolute;bottom: 0;margin-bottom: -25px;right: -14px;">
		  				 <button type="button" data-bind="click: btnSubmitSelectDate, enable: runRuleOption().code == '3' && !cmdEndFlag()">确定</button>
	  				</div>
	          	</div>
	          	
			</div>
			
			<div class="row" style="margin-bottom: 15px">
				<div class="form-group">
				
	  			<label class="control-label" style="margin-left:15px;text-align: left;float: none;">择日日期：</label>
	            <div class="" style="margin-left:15px;">
	            	<textarea rows="3"  class="form-control" data-bind="value : selectedDate, enable: false" style="width: 337px;"></textarea>
	            </div>
	            </div>
	  		</div>
	  		
	  					
			</form>
		</div>
      	<!-- 择日  end-->
      
      	<!-- 时刻table -->
		<div class="row">
        <div class="table-responsive table-hover" style="overflow:auto">
          <table id="table_run_plan_train_times_edit" class="table table-bordered table-striped table-hover">
            <thead>
		        <tr>
		          <th style="vertical-align: middle;width:45px" rowspan="2">序号</th>
                  <th style="vertical-align: middle;width:150px" rowspan="2">站名</th>
                  <th style="vertical-align: middle;width:" rowspan="2">路局</th>
                  <th style="vertical-align: middle;width:150px" rowspan="2">到达时间</th>
                  <th style="vertical-align: middle;width:150px" rowspan="2">出发时间</th>
                  <th style="vertical-align: middle;width:" rowspan="2">停时</th>
                  <th style="vertical-align: middle;width:" colspan="2">天数</th>
                  <th style="vertical-align: middle;width:" colspan="2">车次</th>
                  <th style="vertical-align: middle;width:" rowspan="2">站台</th>
                  <th style="vertical-align: middle;width:" rowspan="2">股道</th>  
                  <th style="vertical-align: middle;width:150px" rowspan="2">作业</th>
                  <th style="vertical-align: middle;width:" rowspan="2">客运营业</th>
                 </tr>
                 <tr>
                 	<th style="vertical-align: middle;width:" class="text-center">到达</th>
	                <th style="vertical-align: middle;width:" class="text-center">出发</th>
	                <th style="vertical-align: middle;width:" class="text-center">到达</th>
	                <th style="vertical-align: middle;width:" class="text-center">出发</th>
                 </tr>
            </thead>
            <tbody data-bind="foreach: trainStns">
	           <tr data-bind="attr:{class : isChangeValue()==1? 'danger':''}">
				<td align="center" data-bind=" text: $index() + 1"></td>
				<td data-bind="text: nodeName, attr:{title: nodeName}"></td>
				<td align="center" data-bind="text: bureauShortName"></td>
				<td align="center"><input  style="text-align:center" class="form-control" data-bind="attr:{readonly:inputReadOnly},value: arrTime,style:{color: setColor() =='1' ? 'red':''}, event:{change: onArrTimeChange,blur: arrTimeOnblur}"/>
				</td>
				<td align="center"><input  style="text-align:center" class="form-control" data-bind="attr:{readonly:inputReadOnly},value: dptTime, style:{color: setColor() =='1' ? 'red':''},event:{change: onDeptTimeChange,blur: deptTimeOnblur}"/>
				</td>
				<td align="center" data-bind="text: stepStr, event:{change: stepStrChange}"></td>
				<td align="center" data-bind="text: arrRunDays"></td>
				<td align="center" data-bind="text: runDays"></td>
				<td align="center"><input style="width:50px;text-align:center" data-bind="attr:{readonly:inputReadOnly},value: arrTrainNbr, event: {blur: sourceTrainNbrOnblur,change: sourceTrainNbrChange}" class="form-control"/></td>
				<td align="center"><input style="width:50px;text-align:center" data-bind="attr:{readonly:inputReadOnly},value: dptTrainNbr, event: {blur: targetTrainNbrOnblur,change: targetTrainNbrChange}" class="form-control"/></td>
				<td align="center"><input style="width:45px;text-align:center" data-bind="attr:{readonly:inputReadOnly},value: platForm, event:{change: onPlatFormChange}", class="form-control"/></td>
				<td align="center"><input style="width:60px;text-align:center" data-bind="attr:{readonly:inputReadOnly},value: trackName, event:{change: onTrackNameChange}" class="form-control"/></td>
				
				<td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: jobItemsText}">
                	<a href="#"><i class="fa fa-plus-square" data-bind="attr:{readonly:inputReadOnly},click: $parent.editJobs,enable:btnEnable" data-toggle="modal" data-target="#editJobsModal"></i></a>
                	<label data-bind="click: $parent.setTrainLineTimesCurrentRec, text: jobItemsText, attr:{title: jobItemsText}"></label>
                </td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec">
                	<select style="width: 45px;text-align:center" class="form-control" data-bind="attr:{disabled:inputReadOnly}, options:kyyyOptions, value: kyyy, optionsText: 'text', event:{change: setKyyy}" class="form-control"></select></td>
                
				
				
				<!-- 
				<td data-bind="attr:{title: jobItemsText}">
                	<a href="#"><i class="fa fa-plus-square" data-bind="click: $parent.editJobs", data-toggle="modal" data-target="#editJobsModal"></i></a>
                	<label data-bind="text: jobItemsText"></label>
                </td>
                <td>
                	<select style="width: 50px" class="form-control" data-bind="options:kyyyOptions, value: kyyy, optionsText: 'text'"></select></td>
                  -->
				
	        	</tr>
	        </tbody>
          </table>
        </div>
        </div>
      	<!-- 时刻table end -->
        
        
      </div>
    </div>




<!--弹出框-->
<div id="editJobsModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">调整站点作业</h4>
      </div>
      
      <!--panel-heading-->
            <div class="panel-body row">
        <div class="row">
				<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="技术停点"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">技术停点</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="分界口"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">分界口</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="上客"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">上客</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="下客"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">下客</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="客运乘务"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">客运乘务</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="职工乘降"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">职工乘降</label>
						</div>
					</div>
			  	</div>
			  	<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="吸污"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">吸污</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="上水"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">上水</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="运转车长"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">运转车长</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="司机换班"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">司机换班</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="机车换乘挂"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">机车换乘挂</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="到达车底"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">到达车底</label>
						</div>
						
					</div>
				</div>
				<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="出库"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">出库</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="入库"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">入库</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="车底上线"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底上线</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="车底下线"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底下线</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="车底转头"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底转头</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="车底立折"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底立折</label>
						</div>
						
					</div>

				</div>
			</div>
		</div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal" data-bind="click:saveJobs">确定</button>
        <button type="button" class="btn btn-info" data-bind="click:setJobCheckBoxVal">重置</button>
        <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content --> 
  </div>
  <!-- /.modal-dialog --> 
</div>
<!--弹出框--> 






<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/multiselect2side.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script src="<%=basePath %>/assets/js/trainplan/runPlan/train_runTime.js"></script>
</body>
</html>
