
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>既有临客上图</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/table.scroll.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/table.scroll.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/jquery.autocomplete.css">
<!-- 自动补全插件 -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/station/station.css">

<script type="text/javascript" src="<%=basePath %>/assets/station/station.js"></script>
<style>
	.form-horizontal .control-label, .form-horizontal .radio, .form-horizontal .checkbox, .form-horizontal .radio-inline, .form-horizontal .checkbox-inline{
	padding-top:3px;
	}
	.table thead > tr > th {
	padding: 0px 5px;
	}
	.table-cc input,.table-cc select{
	padding:4px 4px;
	text-align: center;
	}
	.table-cc .table_input{
	line-height: 25px;
	}
</style>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
</head>
<body class="Iframe_body" style="margin:-24px 12px 0px -4px">
<!-- 
<ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置:</span>
    <c:if test="${train_type==0}">
        <li><a href="#">编制开行计划 -> 既有加开临客生成开行计划</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">编制开行计划 -> 高铁加开临客生成开行计划</a></li>
    </c:if>
</ol>
 -->
<!--以上为必须要的--> 
<div class="panel panel-default" style="margin:15px -7px 0px 10px;">
	<div class="panel-body" style="padding: 10px 10px 0 10px;">
		<div class="bs-example bs-example-tabs">
		      <ul id="myTab" class="nav nav-tabs">
		        <li class="active"><a href="#pageDiv_ddj" data-toggle="tab" data-bind="click:cleanPageData">本局担当</a></li>
		        <li class=""><a href="#pageDiv_bjxg" data-toggle="tab" data-bind="click:cleanPageData">外局担当</a></li>
		         <li class="pull-right"><input type="checkbox" class="pull-left" data-bind="checked: isShowRunPlans, event:{change: showRunPlans}"> <label for="exampleInputEmail5"
							class="control-label pull-left" style="margin: 2px 0 0 5px;"> 显示时刻表</label></li>
		      </ul>
		    <div id="myTabContent" class="tab-content" >
		  	<!--tab1 开始-->
			  <div id="pageDiv_ddj" class="tab-pane fade active in row" style="position: relative;">
			  		<!--  -->
					  <div id="left_div" style="float:left; width:100%;margin:10px -590px 0 0;position: absolute;z-index:0;">

					    <!--分栏框开始-->
					    <div id="left_div_panel" class="panel" style="margin:0 532px 10px 0;">
							<div class="row" style="padding-bottom:10px;">
							  <form class="form-horizontal" role="form">
							  <input type="hidden" id="currentB"/>
							  	<div class="row">
							  		<div class="pull-left">
								  		<div class="row">
								  			<div class="row" style="width: 100%; margin-top: 5px; ">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
										  			<label for="exampleInputEmail2" class="control-label pull-left">令电日期(开始):&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_startDate" type="text" class="form-control" style="width:130px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">&nbsp;&nbsp;路局令电号:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:130px;" data-bind="value: searchModle().cmdNbrBureau">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left"  style="width:85px;">铁总令电号:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:130px;" data-bind="value: searchModle().cmdNbrSuperior">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left: 5px">&nbsp;&nbsp;选线状态:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 130px;display:inline-block;margin-left: 1px"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().selectStateOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
													
										  		</div>
										  	</div>
										  	<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
										  			<label for="exampleInputEmail2" class="control-label pull-left">令电日期(截至):&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_endDate" type="text" class="form-control" style="width:130px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">令电类型:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 130px;display:inline-block;"
															 data-bind="options: searchModle().cmdTypeArrayBjdd, value: searchModle().cmdTypeOptionBjdd, optionsText: 'text', event:{change :cmdTypeChangeEvent}">
														</select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">&nbsp;&nbsp;车次:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:130px;" data-bind="value: searchModle().trainNbr">
												    </div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left: 5px">&nbsp;&nbsp;生成状态:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 130px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().createStateOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
										  			

										  			
										  		</div>
										  	</div>
										  	
								  		</div>
								  		
							  		</div>
							  		<div style="float:left;margin-left:20px;margin-top: 35px;margin-bottom:0;vertical-align: middle">
							  			<a type="button" href="#" class="btn btn-success btn-input" data-bind="click : queryListBjdd" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
							  		</div>
							  	</div>
							  </form>
							</div>
					      
					      <div class="panel-body" style="padding:0">
					        <div class="row" style="margin-bottom:10px;">
					        <div class="pull-left">
					              <button type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: batchCreateRunPlanLine" data-target="#saveHightLineCrewModal"><i class="fa fa-external-link"></i> 生成开行计划</button>
					          </div>
					          <div class="pull-left" style="margin-left:50px;">时刻：</div>
					          <div class="pull-left" style="margin-left:10px;">
					              <button id="bjdd_btn_selectLine" type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: loadTrainInfoFromJbt"><i class="fa fa-book"></i>取基本图时刻</button>
					          </div>
					          <div class="pull-left" style="margin-left:10px;">
					              <button id="bjdd_btn_selectLine" type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: loadTrainInfoFromHis"><i class="fa fa-clock-o"></i>复制历史时刻</button>
					          </div>
					          <div class="pull-left" style="margin-left:10px;">
					              <button id="bjdd_btn_drwjsk" type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: importCmdTrainStnByExcel"><i class="fa fa-sign-in"></i>导入文件时刻</button>
					          </div>
					          <div class="pull-left" style="margin-left:10px;">
					              <button id="bjdd_btn_qmlsk" type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: findCmdTrainStn"><i class="fa fa-sign-in"></i>取命令时刻</button>
					          </div>
					          <div id="wdx" class="pull-left" style="margin-left:50px;">文电项：</div>
						        <div class="pull-left" style="margin-left:10px;">
						          <button id="bjdd_btn_addWdCmd" type="button" class="btn btn-success btn-xs" data-toggle="modal" style="display:none;" data-bind="click : onWdCmdAddOpen" data-target="#bjddAddWdjkCmdTrainModal"><i class="fa fa-plus"></i>添加</button>
						        </div>
						        <div class="pull-left" style="margin-left:10px;">					         
						          <button id="bjdd_btn_editWdCmd" type="button" class="btn btn-success btn-xs" data-toggle="modal" style="display:none;" data-bind="click : onWdCmdEditOpen" data-target="#bjddAddWdjkCmdTrainModal"><i class="fa fa-pencil-square-o"></i> 修改</button>
						        </div>
						        <div class="pull-left" style="margin-left:10px;">						         
						          <button id="bjdd_btn_deleteWdCmd" type="button" class="btn btn-success btn-xs" style="display:none;" data-bind="click : deleteWdCmd"><i class="fa fa-minus-square"></i>删除</button>
						          <!-- <button id="bjdd_btn_addWdCmd" type="button" class="btn btn-success btn-xs" style="display:none;" data-toggle="modal" data-bind="click: btnEventAddWdCmdTrain"><i class="fa fa-plus"></i>添加文电</button>-->
						          </div>
						        <div class="pull-left" style="margin-left:50px;">						         
						          <button id="bjdd_btn_sendMsg" type="button" class="btn btn-success btn-xs" data-bind="click : sendMsg"><i class="fa fa-volume-up"></i>发消息</button>
						          </div>
					        </div>
					        
					        <!-- 既有、高铁加开table div -->
					        <div id="div_bjdd_cmdTrainTable_jk" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:24px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:37px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:83px">命令类型</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">发令日期</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">局令号</th>
					                <th class="text-center" style="vertical-align: middle;width:37px">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:55px">铁总<br>令号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">始发站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">终到站</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">截至日期</th>
					                <th class="text-center" style="vertical-align: middle;width:45px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:120px">择日</th>
					                 <th class="text-center" style="vertical-align: middle;width:37px">重点</th>
					                <th class="text-center" style="vertical-align: middle;">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:37px">选线<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:37px">生成<br>状态</th>
					                <th rowspan="2" style="width: 17px" align="center"></th>				               
					              </tr>
					            </thead>
				    	 </table>
		              <div id="left_height" style="overflow-y:auto;">
		                  <table class="table table-bordered table-striped table-hover" style="border:0;">
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td style="width:24px;"><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="event:{change:$parent.setCurrentRec1 },checked: isSelect" ></td>
					                <td style="width:37px;" data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td style="width:83px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td style="width:80px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td style="width:60px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td style="width:37px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td style="width:55px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td style="width:90px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td style="width:90px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td style="width:90px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td style="width:80px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td style="width:80px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td style="width:45px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td style="width:120px;padding:4px 2px;" data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td style="width:37px;padding:4px 2px;" style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: importantFlagStr"></td>
					                
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                <!--  
					                <td align="center" style="vertical-align: middle;" data-bind="click: $parent.setCurrentRec, text: selectStateStr, attr:{title: selectStateStr}"></td>
					                -->
					                <td style="width:37px;padding:4px 2px;vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: selectStateStr"></td>
					                <td style="width:37px;padding:4px 2px;vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					                <td style="width: 17px" align="center" class="td_17"></td>
					              </tr>
					            </tbody>
					          </table>
					         </div>
					        </div>
					        <!-- 既有、高铁加开table div end -->
					        
					        
					        
					        <!-- 文电加开table div -->
					        <div id="div_bjdd_cmdTrainTable_wd" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table_wd" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:20px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:38px">序号</th>
					                 <th class="text-center" style="vertical-align: middle;width:80px;max-width:80px;">车次</th>
					                    <th class="text-center" style="vertical-align: middle;width:70px">始发站</th>
					                <th class="text-center" style="vertical-align: middle;width:70px">终到站</th>
					                       <th class="text-center" style="vertical-align: middle;width:80px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">截至日期</th>
					                     <th class="text-center" style="vertical-align: middle;width:45px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">择日日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">文电类型</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">文电日期</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">路局<br>文电号</th>
					                 <th class="text-center" style="vertical-align: middle;width:70px;">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">总公司<br>文电号</th>
					                 <th class="text-center" style="vertical-align: middle;width:38px">重点</th>
					                <th class="text-center" style="vertical-align: middle;width:">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:38px">选线<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:38px">生成<br>状态</th>
					                <th style="width: 17px" align="center"></th>
					              </tr>
					            </thead>
					          </table>
					          <div id="left_height" style="overflow-y:auto;">
                               <table class="table table-bordered table-striped table-hover" style="border: 0;">
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td style="width:20px"><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="event:{change:$parent.setCurrentRec1 },checked: isSelect" ></td>
					                <td style="width:38px" data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td style="width:80px" data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td style="width:70px" data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td style="width:70px" data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td style="width:80px" data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td style="width:80px" data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td style="width:45px" data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td style="width:80px" data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                
					                <td style="width:90px" data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td style="width:80px" data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td style="width:100px" data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td style="width:70px;" data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td style="width:100px;" data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					       			<td style="width:38px;text-align:center;" data-bind="click: $parent.setCurrentRec, html: importantFlagStr"></td>
					               
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                 <!--  
					                <td align="center" style="vertical-align: middle;" data-bind="click: $parent.setCurrentRec, text: selectStateStr, attr:{title: selectStateStr}"></td>
					                -->
					                <td style="text-align:center;width:38px;" data-bind="click: $parent.setCurrentRec, html: selectStateStr"></td>
					                <td style="text-align:center;width:38px;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					                <td style="width: 17px" align="center" class="td_17"></td>
					              </tr>
					            </tbody>
					          </table>
					         </div>
					        </div>
					        <!-- 文电加开table div end -->
					        
					        
					        
					         <!-- 文电停运table div -->
					        <div id="div_bjdd_cmdTrainTable_wd_stop" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table_stop" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:20px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:25px">序号</th>
					                 <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">停运站</th>
					        		<th class="text-center" style="vertical-align: middle;width:90px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">截至日期</th>
					                <th class="text-center" style="vertical-align: middle;width:50px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:300px">停运择日</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">文电类型</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">文电日期</th>
					                <th class="text-center" style="vertical-align: middle;width:120px">路局文电号</th>
					                 <th class="text-center" style="vertical-align: middle;width:60px">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">总公司文电号</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr id="tridclick" data-bind="click: $parent.setCurrentRec,style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="checked: isSelect"></td>
					                <td data-bind="text: ($index() + 1)"></td>
					                <td data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="text: startStn, attr:{title: startStn}"></td>					               
					                <td data-bind="text: startDateStr, attr:{title: startDateStr}"></td>
					                <td data-bind="text: endDateStr, attr:{title: endDateStr}"></td>
					                <td data-bind="text: rule, attr:{title: rule}"></td>
					                <td data-bind="text: selectedDate, attr:{title: selectedDate}"></td>
	              				    <td data-bind="text: cmdType, attr:{title: cmdType}"></td>
					                <td data-bind="text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td data-bind="text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="text: cmdItem, attr:{title: cmdItem}"></td>
					                <td data-bind="text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                 <!--  
					                <td align="center" style="vertical-align: middle;" data-bind="click: $parent.setCurrentRec, text: selectStateStr, attr:{title: selectStateStr}"></td>
					                -->
					                <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					             
					              </tr>
					            </tbody>
					          </table>
					        </div>
					        <!-- 文电停运table div end -->
					        
					        
					        
					        
					        <!-- 既有、高铁停运table div -->
					        <div id="div_bjdd_cmdTrainTable_ty" class="table-responsive table-hover">
					          <table id ="runPlanLkCMD_table" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:20px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:25px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">命令类型</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">发令日期</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">局令号</th>
					                <th class="text-center" style="vertical-align: middle;width:30px"">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">铁总<br>令号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">停运站</th>
					                <th class="text-center" style="vertical-align: middle;width:450px">停运日期</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td><input name="cmd_list_checkbox" type="checkbox" value="1" data-bind="checked: isSelect"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					        <!-- 既有、高铁停运table div end -->
					        
					        
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					        
					    <!--分栏框结束--> 
					  </div>
					
					
					
					
					  <!--列车开行计划-->
					  <div id="time_div" class="pull-right" style="width:540px;margin-top: 10px;z-index: 1000;right:0;"> 
					    <!--分栏框开始-->
					    <div class="panel panel-default">
					      <div class="panel-heading" >
					        <h3 class="panel-title" style="text-align: left;"> <i class="fa fa-list-ul"></i>时刻表</h3>
					      </div>
					      <!--panle-heading-->
					      <div class="panel-body" style="padding:5px 5px;">
							<div class="row" style="width: 100%; margin:5px 0 10px 0;">
						  		<div class="form-group" style="float:left;margin-bottom:0;">
						  			<label for="exampleInputEmail2" class="control-label pull-left text-right" style="width:70px;">途经局:&nbsp;</label>
								    <div class="pull-left">
								        <input id="runPlanLk_cmd_input_tjj" class="form-control" style="width:360px;height:23px" data-bind="event:{change: checkValueRepeat},value:currentCmdTxtMl().passBureau"></textarea>
									</div>
									<label style="color:#ff3030;font-size: 16px;margin-left:3px"><b>*</b></label>
						  		</div>
						  	</div>
							<div class="row" style="width: 100%; margin:5px 0 10px 0;">
						  		<div class="form-group" style="float:left;margin-bottom:0;">
						  			<label for="exampleInputEmail2" class="control-label pull-left text-right" style="width:70px;">列车类型:&nbsp;</label>
								    <div class="pull-left">
								         <!-- <select class="form-control" style="display:inline-block;width:360px;" >
									         <option value="0">0</option>
									         <option value="1">1</option>
									         <option value="2">2</option>
								         </select> -->
								         <select id="train_line_type" style="display:inline-block;width:360px;" class="form-control" data-bind="event:{change: checkValueRepeat},options:trainlineTypes(), value: trainlineTypes(), optionsText: 'name', optionsValue:'id'"></select>
									</div>
									<label style="color:#ff3030;font-size: 16px;margin-left:3px"><b>*</b></label>
						  		</div>
						  	</div>
						  	<div class="row" style="width: 100%; margin:5px 0 10px 0;">
						  		<div class="form-group" style="float:left;margin-bottom:0;">
									<div class="pull-left">
								        <button type="button" class="btn btn-success btn-xs" id="isSave"  data-bind="click: saveCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: up"><i class="fa fa-arrow-up"></i> 上移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: down"><i class="fa fa-arrow-down"></i> 下移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: insertTrainStnRow"><i class="fa fa-plus"></i> 插入行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: addTrainStnRow"><i class="fa fa-plus"></i> 追加行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: deleteTrainStnRow"><i class="fa fa-trash-o"></i> 删除行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: loadFillSite"><i class="fa fa-share-square-o"></i>补点</button>
					          		</div>
						  		</div>
						  	</div>
					        <div class="table-responsive table-hover" style="overflow-y:auto;">
					          <table id="runPlanLkCMD_trainStn_table" border="0" style="width:1280px" class="table table-bordered table-striped table-hover table-cc">
					            <thead>
					              <tr>
					                <th style="vertical-align: middle;width:30px;" rowspan="2" class="text-center">序号</th>
					                <th style="vertical-align: middle;width:200px;" rowspan="2" class="text-center">站名<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:60px" rowspan="2" class="text-center">路局<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:85px" rowspan="2" class="text-center">到达时间<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:85px" rowspan="2" class="text-center">出发时间<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:px" colspan="2" class="text-center">运行天数</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">股道</th>
					                <th style="vertical-align: middle;width:60px" rowspan="2" class="text-center">站台</th>
					                <th style="vertical-align: middle;width:px" colspan="2" class="text-center">车次</th>
					                <th style="vertical-align: middle;width:270px" rowspan="2">作业</th>
				                    <th style="vertical-align: middle;width:" rowspan="2">客运</th>
					                <th style="width: 17px"  rowspan="2" align="center"></th>
					                
					                
					              </tr>
					              <tr>
					                <th style="vertical-align: middle;width:70px" class="text-center">到达</th>
					                <th style="vertical-align: middle;width:70px" class="text-center">出发</th>
					                <th style="vertical-align: middle;width:90px" class="text-center">到达</th>
					                <th style="vertical-align: middle;width:90px" class="text-center">出发</th>
					              </tr>
					            </thead>
					          </table>
		              <div style="overflow-y:auto;width:1280px;" id="time1">
		                  <table id="ddddddd" class="table table-bordered table-striped table-hover table-cc" style="border:0;">
								<tbody  id="saveCmdTxtMl" data-bind="foreach: runPlanLkCMDTrainStnRows">
					              <tr data-bind="style:{color: $root.currentCmdTrainStn().childIndex == childIndex ? 'blue':''}, event:{click:$parent.ctrlSels}" id="childIndex">
					              
					               <td style="width:37px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, text: ($index() + 1)" class="table_input"></td>
 					               <td style="width:200px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: nodeName}">
 					               	<input name="input_cmdTrainTimes_stnName" class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: nodeName,event:{keyup:stnNameKeyUp,focus: stnNameTempOnfocus}" 
 					               	oninput="checkValueRepeat();" onpropertychange="checkValueRepeat();"/>
 					               </td>
					               <td style="width:60px;" data-bind="click: $parent.setCMDTrainStnCurrentRec"><select class="form-control" data-bind="event:{change: $parent.checkValueRepeat},options:$parent.searchModle().bureauArray, value: bureauOptionWjdd, optionsText: 'text',attr:{id: ($index() + 1)+'jy100'}"></select></td>
					               <td style="width:85px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTime}">
					                	<input class="form-control keydown_change" data-bind="click: $parent.setCMDTrainStnCurrentRec, style:{color: setColor() =='1' ? 'red':''},value: arrTime, event: {change: $parent.checkValueRepeat,blur: arrTimeOnblur,keyup:arrTimeKeyUp}"/>
					                	<span data-bind="validationMessage: arrTime"></span>
					               </td>
					               <td style="width:85px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTime}">
					                	<input class="form-control keydown_change" data-bind="click: $parent.setCMDTrainStnCurrentRec,style:{color: setColor() =='1' ? 'red':''}, value: dptTime, event: {change: $parent.checkValueRepeat,blur:dptTimeOnblur,keyup:dptTimeKeyUp}"/>
					                	<span data-bind="validationMessage: dptTime"></span>
					               </td>
					               <td style="width:70px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrRunDays}"><input class="form-control" data-bind="event:{blur:arrRunDaysOnblur,keyup:arrKeyUp,change: $parent.checkValueRepeat},style:{color: setColor() =='2' ? 'red':''},click: $parent.setCMDTrainStnCurrentRec, value: arrRunDays"/></td>
					               <td style="width:70px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptRunDays}"><input class="form-control" data-bind="event:{blur:dptRunDaysOnblur,keyup:dptKeyUp,change: $parent.checkValueRepeat},style:{color: setColor() =='2' ? 'red':''},click: $parent.setCMDTrainStnCurrentRec, value: dptRunDays"/></td>
					               <td style="width:80px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: trackName}"><input class="form-control" data-bind="event:{keyup:trackKeyUp,change: $parent.checkValueRepeat},click: $parent.setCMDTrainStnCurrentRec, value: trackName"/></td>
					               <td style="width:60px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: platform}"><input class="form-control" data-bind="event:{keyup:platformKeyUp,change: $parent.checkValueRepeat},click: $parent.setCMDTrainStnCurrentRec, value: platform"/></td>
					               <td style="width:90px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTrainNbr}"><input class="form-control" maxlength="20" data-bind="event:{blur: sourceTrainNbrOnblur},click: $parent.setCMDTrainStnCurrentRec, value: arrTrainNbr"/></td>
					               <td style="width:90px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTrainNbr}"><input class="form-control" maxlength="20" data-bind="event:{blur: targetTrainNbrOnblur},click: $parent.setCMDTrainStnCurrentRec, value: dptTrainNbr"/></td>
					               
					                <td style="width:270px;" data-bind="click:$parent.setCMDTrainStnCurrentRec,  attr:{title: jobItemsText}">
					                  	<a href="#"><i class="fa fa-plus-square" data-bind="click: $parent.editJobs" data-toggle="modal" data-target="#editJobsModal"></i></a>
					                  	<label data-bind="click:  $parent.setCMDTrainStnCurrentRec,  text: jobItemsText, attr:{title: jobItemsText}"></label>
					                  </td>
					                  <td data-bind="click:  $parent.setCMDTrainStnCurrentRec">
					                  	<select id="kyId" style="width: 52px" class="form-control" data-bind="options:kyyyOptions, value: kyyy, optionsText: 'text', event:{change: setKyyy}"></select></td>
					                  
					               <td style="width: 17px" align="center" class="td_17 "></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					       </div>
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					  <!--列车开行计划 end--> 
  
  
			  		
			  		
			  		
			  		<!-- tab1内容结束 -->
			  </div>
		  	<!--tab1  结束--> 
	        <!--tab2   开始-->
	        <div id="pageDiv_bjxg" class="tab-pane fade" style="position: relative;">
	        	<!-- tab2内容开始 -->
	        		<!--  -->
					  <div id="right_div" style="margin-right:-590px; float:left; width:100%;position: absolute;">
					    <!--分栏框开始-->
					    <div id="right_div_panel" class="panel" style="margin-right:532px;">
							<div class="row" style="padding-top:10px;padding-bottom:10px;">
							  <form class="form-horizontal" role="form">
							  	<div class="row">
							  		<div class="pull-left">
								  		<div class="row">
								  			<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">令电日期(开始):&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_startDate_wjdd" type="text" class="form-control" style="width:130px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">铁总令电号:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:130px;" data-bind="value: searchModle().cmdNbrSuperior">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:80px;">&nbsp;&nbsp;担当局:&nbsp;</label>
												    <div class="pull-left">
												    	<select style="width:130px" class="form-control" data-bind="options:searchModle().bureauArray, value: searchModle().bureauOption, optionsText: 'text'"></select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:55px;">&nbsp;&nbsp;车次:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:130px;" data-bind="value: searchModle().trainNbr">
												    </div>
										  		</div>
										  	</div>
										  	<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">令电日期(截至):&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_endDate_wjdd" type="text" class="form-control" style="width:130px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">令电类型:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 130px;display:inline-block;"
															 data-bind="options: searchModle().cmdTypeArrayWjdd, value: searchModle().cmdTypeOptionWjdd, optionsText: 'text', event:{change :cmdTypeWjddChangeEvent}">
														</select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:80px;">&nbsp;&nbsp;生成状态:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 130px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().createStateOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
										  			
										  		</div>
										  	</div>
										  	
								  		</div>
							  		</div>
							  		<div style="float:left;margin-left:20px;margin-top: 25px;margin-bottom:0;vertical-align: middle">
							  			<a type="button" href="#" class="btn btn-success btn-input" data-bind="click : queryListWjdd" style="float:left;margin:11px 0 0 -73px;"><i class="fa fa-search"></i>查询</a>
							  		</div>
							  	</div>
							  </form>
							</div>
					      
					      <div class="panel-body" style="padding:0">
					      	<!-- 外局担当 既有、高铁加开table div 开始 -->
					        <div id="div_wjdd_cmdTrainTable_jk" class="table-responsive table-hover">
					          <table class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:35px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">铁总<br>令号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">始发站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">终到站</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">截至日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:300px">择日</th>
					                <th class="text-center" style="vertical-align: middle;width:168px">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:40px">担当<br>局</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">发令局<br>令号</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">发令日期</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					                <td data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdBureau, attr:{title: cmdBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					        <!-- 外局担当 既有、高铁加开table div 结束 -->
					        
					        <!-- 外局担当文电加开table div -->
					        <div id="div_wjdd_cmdTrainTable_wd" class="table-responsive table-hover">
					           <table id ="bjdd_jk_runPlanLkCMD_table_wd" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:25px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:38px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:110px">总公司<br>文电号</th>
					                 <th class="text-center" style="vertical-align: middle;width:80px">车次</th>
					                <th class="text-center" style="vertical-align: middle;width:70px">始发站</th>
					                <th class="text-center" style="vertical-align: middle;width:70px">终到站</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">截至日期</th>
					                <th class="text-center" style="vertical-align: middle;width:45px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">择日日期</th>
					                <th class="text-center" style="vertical-align: middle;width:">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:50px">担当局</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">文电类型</th>
					                <th class="text-center" style="vertical-align: middle;width:110px">路局<br>文电号</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">文电日期</th>
					                <th class="text-center" style="vertical-align: middle;width:38px">生成<br>状态</th>
					                <th rowspan="2" style="width: 17px" align="center"></th>
					              </tr>
					            </thead>
					          </table>
								<div id="left_height2" style="overflow-y: auto; height: 610px;">
	                               <table class="table table-bordered table-striped table-hover" style="border: 0;">
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<!-- <td style="width:25px;"><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="checked: isSelect"></td> -->
					              	<td style="width:25px;"><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="event:{change:$parent.setCurrentRec1 },checked: isSelect" ></td>
					                <td style="width:38px;" data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td style="width:110px;" data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td style="width:80px;" data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td style="width:70px;" data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td style="width:70px;" data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td style="width:80px;" data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td style="width:80px;" data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td style="width:45px;" data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td style="width:80px;" data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td style="width:;" data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                <td style="width:50px;" data-bind="click: $parent.setCurrentRec, text: cmdBureau, attr:{title: cmdBureau}"></td>
					                <td style="width:90px;" data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td style="width:110px;" data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td style="width:80px;" data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                
					                <td style="width:38px;text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					                <td style="width: 17px" align="center" class="td_17"></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					        </div>
					        <!-- 外局担当文电加开table div end -->
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					
					
					
					
					  <!--列车开行计划-->
					  <div id="time_div2" class="pull-right" style="width:520px;margin-top:10px;"> 
					    <!--分栏框开始-->
					    <div class="panel panel-default">
					      <div class="panel-heading" >
					        <h3 class="panel-title" style="text-align: left;"> <i class="fa fa-list-ul"></i>时刻表</h3>
					      </div>
					      <!--panle-heading-->
					      <div class="panel-body" style="padding:5px 5px;">
							<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-bottom:0;">
						  			<label for="exampleInputEmail2" class="control-label pull-left text-right" style="width:70px;">途经局:&nbsp;</label>
								    <div class="pull-left">
								        <input readonly="readonly" id="runPlanLk_cmd_input_tjj_wjdd" class="form-control" data-bind="value:currentCmdTxtMl().passBureau" style="width:360px;height:23px"></label>
									</div>
									<label style="color:#ff3030;font-size: 16px;margin-left:3px"><b>*</b></label>
									<!-- <div class="pull-left" style="margin-left:345px;">
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
					          		</div> -->
						  		</div>
						  	</div>
							<div class="row" style="width: 100%; margin:5px 0 10px 0;">
						  		<div class="form-group" style="float:left;margin-bottom:0;">
						  			<label for="exampleInputEmail2" class="control-label pull-left text-right" style="width:70px;">列车类型:&nbsp;</label>
								     <div class="pull-left">
								         <!-- <select class="form-control" style="display:inline-block;width:360px;" >
									         <option value="0">0</option>
									         <option value="1">1</option>
									         <option value="2">2</option>
								         </select> -->
								         <select disabled="disabled" id="train_line_type1" style="display:inline-block;width:360px;" class="form-control" data-bind="event:{change: checkValueRepeat},options:trainlineTypes(), value: trainlineTypes(), optionsText: 'name', optionsValue:'id'"></select>
									</div>
									<label style="color:#ff3030;font-size: 16px;margin-left:3px"><b>*</b></label>
						  		</div>
						  	</div>
						  	<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-bottom:0;">
									<div class="pull-left">
								        <button type="button" class="btn btn-success btn-xs" id="isSave2" data-bind="click: saveOtherCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: up"><i class="fa fa-arrow-up"></i> 上移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: down"><i class="fa fa-arrow-down"></i> 下移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: insertTrainStnRow"><i class="fa fa-plus"></i> 插入行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: addTrainStnRow"><i class="fa fa-plus"></i> 追加行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: deleteTrainStnRow"><i class="fa fa-minus-square"></i> 删除行</button>
					          		</div>
						  		</div>
						  	</div>
					        <div class="table-responsive table-hover" style=" overflow:auto;">
					          <table id="runPlanLkCMD_trainStn_table" border="0" style="width:1280px" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th style="vertical-align: middle;width:30px;" rowspan="2" class="text-center">序号</th>
					                <th style="vertical-align: middle;width:200px;" rowspan="2" class="text-center">站名<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:65px" rowspan="2" class="text-center">路局<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:85px" rowspan="2" class="text-center">到达时间<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:85px" rowspan="2" class="text-center">出发时间<span class="red">*</span></th>
					                <th style="vertical-align: middle;width:px" colspan="2" class="text-center">运行天数</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">股道</th>
					                <th style="vertical-align: middle;width:60px" rowspan="2" class="text-center">站台</th>
					                <th style="vertical-align: middle;width:px" colspan="2" class="text-center">车次</th>
					                <th style="vertical-align: middle;width:270px" rowspan="2">作业</th>
				                    <th style="vertical-align: middle;width:" rowspan="2">客运</th>
					                <th style="width: 12px"  rowspan="2" align="center"></th>
					              </tr>
					              <tr>
					                <th style="vertical-align: middle;width:70px" class="text-center">到达</th>
					                <th style="vertical-align: middle;width:70px" class="text-center">出发</th>
					                <th style="vertical-align: middle;width:90px" class="text-center">到达</th>
					                <th style="vertical-align: middle;width:90px" class="text-center">出发</th>
					              </tr>
					            </thead>
					         </table>
		              <div style="overflow-y:auto;width:1280px;" id="time2">
		                  <table class="table table-bordered table-striped table-hover table-cc" style="border:0;">
								<tbody  id ="saveOtherCmdTxtMl" data-bind="foreach: runPlanLkCMDTrainStnRows">
					              <tr data-bind="style:{color: $root.currentCmdTrainStn().childIndex == childIndex ? 'blue':''}">
					                <td style="width:37px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, text: ($index() + 1)"></td>
					                <td style="width:200px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: nodeName}">
					                <input name="input_cmdTrainTimes_stnName" class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: nodeName,event:{keyup:stnNameKeyUp,focus: stnNameTempOnfocus}" oninput="checkValueRepeat();" onpropertychange="checkValueRepeat();"/>
					                </td> 
 					                <td style="width:65px;" data-bind="click: $parent.setCMDTrainStnCurrentRec"><select style="width: 45px" class="form-control" data-bind="event:{change: $parent.checkValueRepeat},options:$parent.searchModle().bureauArray, value: bureauOptionWjdd, optionsText: 'text',attr:{id: ($index() + 1)+'wj100'}"></select></td>
					                <td style="width:85px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTime}">
					                	<input class="form-control keydown_change" data-bind="click: $parent.setCMDTrainStnCurrentRec,style:{color: setColor() =='1' ? 'red':''}, value: arrTime, event: {change: $parent.checkValueRepeat,blur: arrTimeOnblur,keyup:arrTimeKeyUp}"/>
					                	<span data-bind="validationMessage: arrTime"></span>
					                </td>
					                <td style="width:85px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTime}">
					                	<input class="form-control keydown_change" data-bind="click: $parent.setCMDTrainStnCurrentRec,style:{color: setColor() =='1' ? 'red':''},value: dptTime, event: {change: $parent.checkValueRepeat,blur:dptTimeOnblur,keyup:dptTimeKeyUp}"/>
					                	<span data-bind="validationMessage: dptTime"></span>
					                </td>
					                <td style="width:70px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrRunDays}"><input class="form-control" data-bind="event:{blur:arrRunDaysOnblur,keyup:arrKeyUp,change: $parent.checkValueRepeat},style:{color: setColor() =='2' ? 'red':''},click: $parent.setCMDTrainStnCurrentRec, value: arrRunDays"/></td>
					                <td style="width:70px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptRunDays}"><input class="form-control" data-bind="event:{change: $parent.checkValueRepeat,blur:dptRunDaysOnblur,keyup:dptKeyUp},style:{color: setColor() =='2' ? 'red':''},click: $parent.setCMDTrainStnCurrentRec, value: dptRunDays"/></td>
					                <td style="width:80px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: trackName}"><input class="form-control" data-bind="event:{keyup:trackKeyUp,change: $parent.checkValueRepeat},click: $parent.setCMDTrainStnCurrentRec, value: trackName"/></td>
					                <td style="width:60px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: platform}"><input class="form-control" data-bind="event:{keyup:platformKeyUp,change: $parent.checkValueRepeat},click: $parent.setCMDTrainStnCurrentRec, value: platform"/></td>
					                <td style="width:90px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTrainNbr}"><input class="form-control" maxlength="20" data-bind="event:{blur: sourceTrainNbrOnblur},click: $parent.setCMDTrainStnCurrentRec, value: arrTrainNbr"/></td>
					                <td style="width:90px;" data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTrainNbr}"><input class="form-control" maxlength="20" data-bind="event:{blur: targetTrainNbrOnblur},click: $parent.setCMDTrainStnCurrentRec, value: dptTrainNbr"/></td>
					                 <td style="width:270px;" data-bind="click:$parent.setCMDTrainStnCurrentRec,  attr:{title: jobItemsText}">
					                  	<a href="#"><i class="fa fa-plus-square" data-bind="click: $parent.editJobs" data-toggle="modal" data-target="#editJobsModal"></i></a>
					                  	<label data-bind="click:  $parent.setCMDTrainStnCurrentRec,  text: jobItemsText, attr:{title: jobItemsText}"></label>
					                  </td>
					                  <td data-bind="click:  $parent.setCMDTrainStnCurrentRec">
					                  	<select style="width: 52px" class="form-control" data-bind=" click:  $parent.setCMDTrainStnCurrentRec,  options:kyyyOptions, value: kyyy, optionsText: 'text'"></select></td>
					                  <td style="width: 12px" align="center" class="td_17 "></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					  <!--列车开行计划 end--> 
  
	        		
	        	<!-- tab2内容结束 -->
	        </div>
	        <!--tab2  结束-->
	        
	        </div>
		</div>
	</div>
</div>


<!--选线按钮点击事件打开   基本图查询界面-->
<div id="jbt_traininfo_dialog" class="easyui-dialog" title="调整时刻表"
	data-options="iconCls:'icon-save'"
	style="width: 1200px; height: 750px;overflow: hidden;">
 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>

<!--选线按钮点击事件打开   基本图查询界面-->
<div id="jbt_fill_site_dialog" class="easyui-dialog" title="补点"
	data-options="iconCls:'icon-save'"
	style="width: 1300px; height: 750px;overflow: hidden;">
 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
<div id="his_traininfo_dialog" class="easyui-dialog" title="调整时刻表"
	data-options="iconCls:'icon-save'"
	style="width: 1200px; height: 750px;overflow: hidden;">
 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
<!-- <div id="bjdd_dialog_addWdCmdTrain" class="easyui-dialog" title="添加文电信息"
		data-options="iconCls:'icon-save'"
		style="width: 500px; height: 500px; padding: 10px; ">  
</div> -->

<!--添加文电加开信息弹出框-->

<div id="add_wd_cmd_info_dialog" class="easyui-dialog" title="添加文电命令"
	data-options="iconCls:'icon-save'"
	style="width: 600px; height: 700px;overflow: hidden;">
	 <iframe style="width: 100%;border: 0;overflow: hidden;" src="" name=""></iframe>
</div>

<!--文电加开新增弹出框  end--> 


<!--发消息-->
<div id="run_plan_sendMsg" class="easyui-dialog" title="发消息"
		data-options="iconCls:'icon-save'"
		style="width: 600px; height: 365px;overflow: hidden;">
  <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>


<div id="file_upload_dlg" class="easyui-dialog" title="导入文件时刻"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 280px; padding: 10px">
		<img id="loading" src="../assets/images/loading.gif" style="display:none;"> 
		<form id="file_upload_id" name="file_upload_name" action="cross/fileUpload" method="post" enctype="multipart/form-data"> 
			<div class="row" style="width: 100%; margin-top: 10px;">
				<input id="fileToUpload" type="file" size="45" name="fileToUpload"  name="fileName" accept=".xls,.xlsx"/>
			</div>
			<div  class="row" style="width: 100%; margin-top: 10px;">
			     <a type="button" id="btn_fileToUpload"
					class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: uploadFile">导入</a>
			</div>
		</form>
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


<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<!-- 自动补全插件 -->
<script type="text/javascript" src="<%=basePath%>/assets/js/resizable.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/jquery.autocomplete.js"></script>

<!-- 锁定命令列表table表头 -->
<script type="text/javascript" src="<%=basePath %>/assets/js/chromatable_1.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.validation.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/runPlanLk_cmd_add.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/validate.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript">

function changeColor(feild){
		
	
	
	
		  	var count = 0;
			  var trList =feild.parentElement.parentElement.parentElement.children;
			 
		      for (var i=0;i<trList.length;i++) {
		        var tdArr = trList[i].children;
		        
		        if(tdArr[0].firstChild.checked==true){
		        	count++;
		        }
		     	console.dir(tdArr[0].firstChild.checked)
		         
		    
		      }
			 if(count==1){
				 if(feild.checked){
					feild.parentElement.parentElement.style.color="blue"
				}
			 }else{
				 feild.parentElement.parentElement.style.color=""
			 }
}





</script>

<script type="text/javascript">

	$(function() {
		
		
	    $("#time_div").resizable({
    	    minWidth: 520,
            maxWidth: 1300,
            handles: 'w', 
            //edge:4,  
            onStartResize: function(e){
               // alert("左");
            },            
            onResize: function(e){
            }            
  		  });
	    $("#time_div2").resizable({
    	    minWidth: 520,
            maxWidth: 1300,
            handles: 'w', 
            //edge:4,  
            onStartResize: function(e){
            },            
            onResize: function(e){
            }            
  		  });
	
	});

</script>
</body>

</html>
