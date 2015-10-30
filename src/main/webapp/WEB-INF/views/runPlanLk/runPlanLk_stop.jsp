
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

<!-- 自动补全插件 -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/jquery.autocomplete.css">


</head>
<body class="Iframe_body" style="margin:-25px -2px 0px -3px;width:100%">
<!--  
<ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置:</span>
    <c:if test="${train_type==0}">
        <li><a href="#">编制开行计划 -> 既有停运临客生成开行计划</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">编制开行计划 -> 高铁停运临客生成开行计划</a></li>
    </c:if>
</ol>
-->
<!--以上为必须要的--> 
<div class="row" style="padding-top:10px;padding-bottom:10px;margin-top: -3px;margin-left: -9px;">
  <form class="form-horizontal" role="form">
  	<div class="row">
  		<div class="pull-left">
	  		<div class="row">
	  			<div class="row" style="width: 100%; margin-top: 5px;">
			  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
			  			<label for="exampleInputEmail2" class="control-label pull-left">令电日期(开始):&nbsp;</label>
					    <div class="pull-left">
					        <input id="runPlanLk_cmd_input_startDate" type="text" class="form-control" style="width:95px;" placeholder="">
						</div>
						<label for="exampleInputEmail2" class="control-label pull-left" style="width:80px;">路局令电号:&nbsp;</label>
					    <div class="pull-left">
					        <input type="text" class="form-control" style="width:140px;" data-bind="value: searchModle().cmdNbrBureau">
						</div>
						<label for="exampleInputEmail2" class="control-label pull-left" style="width:80px;">铁总令电号:&nbsp;</label>
					    <div class="pull-left">
					        <input type="text" class="form-control" style="width:140px;" data-bind="value: searchModle().cmdNbrSuperior">
						</div>
						<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:12px;">&nbsp;&nbsp;车次:&nbsp;</label>
					    <div class="pull-left">
					        <input type="text" class="form-control" style="width:140px;" data-bind="value: searchModle().trainNbr">
					    </div>
			  		</div>
			  	</div>
			  	<div class="row" style="width: 100%; margin-top: 5px;">
			  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
			  			<label for="exampleInputEmail2" class="control-label pull-left">令电日期(截至):&nbsp;</label>
					    <div class="pull-left">
					        <input id="runPlanLk_cmd_input_endDate" type="text" class="form-control" style="width:95px;" placeholder="">
						</div>
						<label for="exampleInputEmail2" class="control-label pull-left" style="width:80px;">令电类型:&nbsp;</label>
					    <div class="pull-left">
					    	<select class="form-control" style="width: 140px;display:inline-block;"
								 data-bind="options: searchModle().cmdTypeArrayBjdd, value: searchModle().cmdTypeOptionBjdd, optionsText: 'text', event:{change :cmdTypeChangeEvent}">
							</select>
						</div>

						<label for="exampleInputEmail2" class="control-label pull-left" style="width:80px;">生成状态:&nbsp;</label>
					    <div class="pull-left">
					    	<select class="form-control" style="width: 140px;display:inline-block;"
								 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().createStateOption, optionsText: 'text',optionsValue:'code'">
							</select>
						</div>
			  			
			  		</div>
			  	</div>
			  	
	  		</div>
  		</div>
  		<div style="float:left;margin-left:0px;margin-top: 32px;margin-bottom:0;vertical-align: middle">
  			<a type="button" href="#" class="btn btn-success btn-input " data-bind="click : queryListBjdd" style="float:left;margin:4px 0 0 -53px;"><i class="fa fa-search"></i>查询</a>
  		</div>
  	</div>
  </form>
</div>
	<div class="panel-body" style="padding: 0 15px;">
		<div class="bs-example bs-example-tabs">
		      <ul id="myTab" class="nav nav-tabs" style="border:0;">
		        
		      </ul>
		    <div id="myTabContent" class="tab-content" >
		  	<!--tab1 开始-->
			  <div id="pageDiv_ddj" class="tab-pane fade active in" >
			  		<!--  -->
					  <div style="margin-right:-590px; float:left; width:100%;">
					    <!--分栏框开始-->
					    <div class="panel panel-default" style="margin:0px -12px 0 -7px">
							
					      
					      <div class="panel-body">
					        <div class="row" style="margin-bottom:10px;">
					          <button id="bjdd_btn_selectLine" type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: loadTrainInfoFromJbt"><i class="fa fa-plus"></i>选线</button>
					          <button type="button" class="btn btn-success btn-xs" data-toggle="modal" data-bind="click: batchCreateRunPlanLine" data-target="#saveHightLineCrewModal"><i class="fa fa-external-link"></i> 生成开行计划</button>
					          <button id="bjdd_btn_addWdCmd" type="button" class="btn btn-success btn-xs" data-toggle="modal" style="display:none;" data-bind="click : onWdCmdAddOpen" data-target="#bjddAddWdjkCmdTrainModal"><i class="fa fa-plus"></i>添加</button>
					          <button id="bjdd_btn_editWdCmd" type="button" class="btn btn-success btn-xs" data-toggle="modal" style="display:none;" data-bind="click : onWdCmdEditOpen" data-target="#bjddAddWdjkCmdTrainModal"><i class="fa fa-pencil-square-o"></i> 修改</button>
					          <button id="bjdd_btn_deleteWdCmd" type="button" class="btn btn-success btn-xs" style="display:none;" data-bind="click : deleteWdCmd"><i class="fa fa-minus-square"></i>删除</button>
					          <!-- <button id="bjdd_btn_addWdCmd" type="button" class="btn btn-success btn-xs" style="display:none;" data-toggle="modal" data-bind="click: btnEventAddWdCmdTrain"><i class="fa fa-plus"></i>添加文电</button>-->
					        </div>
					        
					        <!-- 既有、高铁加开table div -->
					        <div id="div_bjdd_cmdTrainTable_jk" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:20px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:25px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">命令类型</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">发令日期</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">局令号</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">铁总<br>令号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">始发站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">终到站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">截至日期</th>
					                <th class="text-center" style="vertical-align: middle;width:50px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:300px">择日</th>
					                <th class="text-center" style="vertical-align: middle;width:168px">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">选线<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="checked: isSelect"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                <!--  
					                <td align="center" style="vertical-align: middle;" data-bind="click: $parent.setCurrentRec, text: selectStateStr, attr:{title: selectStateStr}"></td>
					                -->
					                <td style="vertical-align: middle; text-align:center;" data-bind="click: $parent.setCurrentRec, html: selectStateStr"></td>
					                <td style="vertical-align: middle; text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					              </tr>
					            </tbody>
					          </table>
					        </div>
					        <!-- 既有、高铁加开table div end -->
					        
					        
					        
					        <!-- 文电加开table div -->
					        <div id="div_bjdd_cmdTrainTable_wd" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table_wd" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:20px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:25px">序号</th>
					                 <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                    <th class="text-center" style="vertical-align: middle;width:90px">始发站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">终到站</th>
					                       <th class="text-center" style="vertical-align: middle;width:90px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">截至日期</th>
					                     <th class="text-center" style="vertical-align: middle;width:50px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:300px">择日日期</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">文电类型</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">文电日期</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">路局<br>文电号</th>
					                 <th class="text-center" style="vertical-align: middle;width:60px">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">总公司<br>文电号</th>
					               
					                <th class="text-center" style="vertical-align: middle;width:168px">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">选线<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="checked: isSelect"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                
					                <td data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					               
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                 <!--  
					                <td align="center" style="vertical-align: middle;" data-bind="click: $parent.setCurrentRec, text: selectStateStr, attr:{title: selectStateStr}"></td>
					                -->
					                <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: selectStateStr"></td>
					                <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					             
					              </tr>
					            </tbody>
					          </table>
					        </div>
					        <!-- 文电加开table div end -->
					        
					        
					        
					         <!-- 文电停运table div -->
					        <div id="div_bjdd_cmdTrainTable_wd_stop" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table_stop" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:40px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:50px">序号</th>
					                 <th class="text-center" style="vertical-align: middle;width:110px">车次</th>
					                <th class="text-center" style="vertical-align: middle;width:120px">停运站</th>
					        		<th class="text-center" style="vertical-align: middle;width:110px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:110px">截至日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:">停运择日</th>
					                <th class="text-center" style="vertical-align: middle;width:140px">文电类型</th>
					                <th class="text-center" style="vertical-align: middle;width:110px">文电日期</th>
					                <th class="text-center" style="vertical-align: middle;width:150px">路局文电号</th>
					                 <th class="text-center" style="vertical-align: middle;width:110px">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:150px">总公司文电号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">生成状态</th>
					                <th style="width: 17px" align="center"></th>
					              </tr>
					            </thead>
					          </table>
					          <div id="left_height" style="overflow-y:auto;">
                               <table class="table table-bordered table-striped table-hover" style="border: 0;">
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td style="width:40px;text-align: center;"><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="checked: isSelect"></td>
					                <td style="width:50px;" data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td style="width:110px;" data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td style="width:120px;" data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>					               
					                <td style="width:110px;text-align: center;" data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td style="width:110px;text-align: center;" data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td style="width:90px;text-align: center;" data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
	              				    <td style="width:140px;text-align: center;" data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td style="width:110px;text-align: center;" data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td style="width:150px" data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                
					                <td style="width:110px" data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td style="width:150px" data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					                <td style="width:90px; text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					                <td style="width: 17px" align="center" class="td_17"></td>
					              </tr>
					            </tbody>
					          </table>
					          </div>
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
					                <th class="text-center" style="vertical-align: middle;width:60px">铁总令号</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                 <!--   <th class="text-center" style="vertical-align: middle;width:30px">重点</th> -->
					                <th class="text-center" style="vertical-align: middle;width:90px">停运站</th>
					                <th class="text-center" style="vertical-align: middle;width:450px">停运日期</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成状态</th>
					                <th style="width: 17px" align="center"></th>
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
					               <!--  <td data-bind="click: $parent.setCurrentRec, html: importantFlagStr"></td> -->
					                
					                <td data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title:  startStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                <td style="vertical-align: middle; text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					                <td style="width: 17px" align="center" class="td_17"></td>
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
					
			  		
			  		
			  		<!-- tab1内容结束 -->
			  </div>
		  	<!--tab1  结束--> 
	        <!--tab2   开始-->
	        <div id="pageDiv_bjxg" class="tab-pane fade">
	        	<!-- tab2内容开始 -->
	        		<!--  -->
					  <div style="margin-right:-590px; float:left; width:100%;">
					    <!--分栏框开始-->
					    <div class="panel panel-default" style="margin-right:1px;">
							<div class="row" style="padding-top:10px;padding-bottom:10px;">
							  <form class="form-horizontal" role="form">
							  	<div class="row">
							  		<div class="pull-left">
								  		<div class="row">
								  			<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">发令日期(开始):&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_startDate_wjdd" type="text" class="form-control" style="width:100px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:10px;">铁总令号:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:110px;" data-bind="value: searchModle().cmdNbrSuperior">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:25px;">&nbsp;&nbsp;车次:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().trainNbr">
												    </div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;担当局:&nbsp;</label>
												    <div class="pull-left">
												    	<select style="width: 80px" class="form-control" data-bind="options:searchModle().bureauArray, value: searchModle().bureauOption, optionsText: 'text'"></select>
													</div>
										  		</div>
										  	</div>
										  	<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">发令日期(截至):&nbsp;</label>
												    <div class="pull-left">
												        <input id="runPlanLk_cmd_input_endDate_wjdd" type="text" class="form-control" style="width:100px;" placeholder="">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:10px;">命令类型:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 110px;display:inline-block;"
															 data-bind="options: searchModle().cmdTypeArrayWjdd, value: searchModle().cmdTypeOptionWjdd, optionsText: 'text', event:{change :cmdTypeWjddChangeEvent}">
														</select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;生成状态:&nbsp;</label>
												    <div class="pull-left">
												    	<select class="form-control" style="width: 100px;display:inline-block;"
															 data-bind="options: [{'code': 'all', 'text': ''},{'code': '1', 'text': '已'},{'code': '0', 'text': '未'}], value: searchModle().createStateOption, optionsText: 'text',optionsValue:'code'">
														</select>
													</div>
										  			
										  		</div>
										  	</div>
										  	
								  		</div>
							  		</div>
							  		<div style="float:left;margin-left:20px;margin-top: 25px;margin-bottom:0;vertical-align: middle">
							  			<a type="button" href="#" class="btn btn-success" data-bind="click : queryListWjdd" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
							  		</div>
							  	</div>
							  </form>
							</div>
					      
					      <div class="panel-body">
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
					                <th class="text-center" style="vertical-align: middle;width:20px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:25px">序号</th>
					                 <th class="text-center" style="vertical-align: middle;width:90px">车次</th>
					                    <th class="text-center" style="vertical-align: middle;width:90px">始发站</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">终到站</th>
					                       <th class="text-center" style="vertical-align: middle;width:90px">开始日期</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">截至日期</th>
					                     <th class="text-center" style="vertical-align: middle;width:50px">规律</th>
					                <th class="text-center" style="vertical-align: middle;width:300px">择日日期</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">文电类型</th>
					                <th class="text-center" style="vertical-align: middle;width:90px">文电日期</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">路局<br>文电号</th>
					                 <th class="text-center" style="vertical-align: middle;width:60px">项号</th>
					                <th class="text-center" style="vertical-align: middle;width:60px">总公司<br>文电号</th>
					                <th class="text-center" style="vertical-align: middle;width:168px">途经局</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">选线<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:30px">生成<br>状态</th>
					              </tr>
					            </thead>
					            <tbody data-bind="foreach: runPlanLkCMDRows">
					              <tr data-bind="style:{color: $parent.currentCmdTxtMl().cmdTxtMlItemId == cmdTxtMlItemId ? 'blue':''}">
					              	<td><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="checked: isSelect"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: trainNbr, attr:{title: trainNbr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startStn, attr:{title: startStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endStn, attr:{title: endStn}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: startDateStr, attr:{title: startDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: endDateStr, attr:{title: endDateStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: rule, attr:{title: rule}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: selectedDate, attr:{title: selectedDate}"></td>
					                
					                <td data-bind="click: $parent.setCurrentRec, text: cmdType, attr:{title: cmdType}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdTimeStr, attr:{title: cmdTimeStr}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrBureau, attr:{title: cmdNbrBureau}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdItem, attr:{title: cmdItem}"></td>
					                <td data-bind="click: $parent.setCurrentRec, text: cmdNbrSuperior, attr:{title: cmdNbrSuperior}"></td>
					               
					                <td data-bind="click: $parent.setCurrentRec, text: passBureau, attr:{title: passBureau}"></td>
					                 <!--  
					                <td align="center" style="vertical-align: middle;" data-bind="click: $parent.setCurrentRec, text: selectStateStr, attr:{title: selectStateStr}"></td>
					                -->
					                <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: selectStateStr"></td>
					                <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: createStateStr"></td>
					             
					              </tr>
					            </tbody>
					          </table>
					        </div>
					        <!-- 外局担当文电加开table div end -->
					      </div>
					      <!--panel-body--> 
					      
					    </div>
					    
					    <!--分栏框结束--> 
					  </div>
					
					
					
					
					  <!--列车开行计划-->
					  <div class="pull-right" style="width:580px;"> 
					    <!--分栏框开始-->
					    <div class="panel panel-default">
					      <div class="panel-heading" >
					        <h3 class="panel-title" > <i class="fa fa-user-md"></i>列车时刻表</h3>
					      </div>
					      <!--panle-heading-->
					      <div class="panel-body" style="padding:5px 5px;">
							<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-left:3px;margin-bottom:0;">
						  			<label for="exampleInputEmail2" class="control-label pull-left">途经局:&nbsp;</label>
								    <div class="pull-left">
								        <label id="runPlanLk_cmd_input_tjj_wjdd" class="control-label pull-left" data-bind="html:currentCmdTxtMl().passBureau" style="width:500px;height:23px"></label>
									</div>
									<!-- <div class="pull-left" style="margin-left:345px;">
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
					          		</div> -->
						  		</div>
						  	</div>
						  	<div class="row" style="width: 100%; margin-top: 5px;margin-bottom: 5px;">
						  		<div class="form-group" style="float:left;margin-left:3px;margin-bottom:0;">
									<div class="pull-left" style="margin-left:3px;">
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveOtherCmdTxtMl"><i class="fa fa-floppy-o"></i> 保存</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: up"><i class="fa fa-arrow-up"></i> 上移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: down"><i class="fa fa-arrow-down"></i> 下移</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: insertTrainStnRow"><i class="fa fa-plus"></i> 插入行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: addTrainStnRow"><i class="fa fa-plus"></i> 追加行</button>
								        <button type="button" class="btn btn-success btn-xs" data-bind="click: deleteTrainStnRow"><i class="fa fa-minus-square"></i> 删除行</button>
					          		</div>
						  		</div>
						  	</div>
					        <div class="table-responsive table-hover" style="height: 620px; overflow:auto;">
					          <table id="runPlanLkCMD_trainStn_table" border="0" style="width:1000px" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th style="vertical-align: middle;width:30px;" rowspan="2" class="text-center">序号</th>
					                <th style="vertical-align: middle;width:100px" rowspan="2" class="text-center">站名</th>
					                <th style="vertical-align: middle;width:40px" rowspan="2" class="text-center">路局</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">到达时间</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">出发时间</th>
					                <th style="vertical-align: middle;width:100px" colspan="2" class="text-center">运行天数</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">股道</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">站台</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">到达车次</th>
					                <th style="vertical-align: middle;width:80px" rowspan="2" class="text-center">出发车次</th>
					              </tr>
					              <tr>
					                <th style="vertical-align: middle;width:50px" class="text-center">到达</th>
					                <th style="vertical-align: middle;width:50px" class="text-center">出发</th>
					              </tr>
					            </thead>
								<tbody data-bind="foreach: runPlanLkCMDTrainStnRows">
					              <tr data-bind="style:{color: $root.currentCmdTrainStn().childIndex == childIndex ? 'blue':''}">
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, text: ($index() + 1)"></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: stnNameTemp}"><input name="input_cmdTrainTimes_stnName" class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: stnNameTemp"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec"><select style="width: 70px" class="form-control" data-bind="options:$parent.searchModle().bureauArray, value: bureauOptionWjdd, optionsText: 'text'"></select></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTime}">
					                	<input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: arrTime, event: {blur: arrTimeOnblur}"/>
					                	<span data-bind="validationMessage: arrTime"></span>
					                </td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTime}">
					                	<input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: dptTime, event: {blur:dptTimeOnblur}"/>
					                	<span data-bind="validationMessage: dptTime"></span>
					                </td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrRunDays}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: arrRunDays"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptRunDays}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: dptRunDays"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: trackName}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: trackName"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: platform}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: platform"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: arrTrainNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: arrTrainNbr"/></td>
					                <td data-bind="click: $parent.setCMDTrainStnCurrentRec, attr:{title: dptTrainNbr}"><input class="form-control" data-bind="click: $parent.setCMDTrainStnCurrentRec, value: dptTrainNbr"/></td>
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
	style="width: 1200px; height: 500px;overflow: hidden;">
 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
<!-- <div id="bjdd_dialog_addWdCmdTrain" class="easyui-dialog" title="添加文电信息"
		data-options="iconCls:'icon-save'"
		style="width: 500px; height: 500px; padding: 10px; ">  
</div> -->

<!--添加文电加开信息弹出框-->

<div id="add_wd_cmd_info_dialog" class="easyui-dialog" title="添加文电命令"
	data-options="iconCls:'icon-save'"
	style="width: 600px; height: 650px;overflow: hidden;">
	 <iframe style="width: 100%;border: 0;overflow: hidden;" src="" name=""></iframe>
</div>


<!--文电加开新增弹出框  end--> 







<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<!-- 自动补全插件 -->
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/jquery.autocomplete.js"></script>

<!-- 锁定命令列表table表头 -->
<script type="text/javascript" src="<%=basePath %>/assets/js/chromatable_1.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.validation.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/runPlanLk_cmd_stop.js"></script>
</body>

</html>
