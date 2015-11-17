
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();

boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}
String currentUserBureau = user.getBureau();
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
} 
String basePath = request.getContextPath();
String  currentBureauShortName = user.getBureauShortName();
%>
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>既有临客开行计划</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link href="<%=basePath%>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath%>/assets/easyui/themes/icon.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>/assets/css/cross/cross.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="<%=basePath%>/assets/css/table.scroll.css">


<script src="<%=basePath%>/assets/js/trainplan/knockout.pagemodle.js"></script>
<script type="text/javascript">
var all_role = "<%=userRolesString%>";
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
var currentUserBureau = "<%=currentUserBureau%>";
var currentBureauShortName = "<%=currentBureauShortName%>";
</script>
<!--#include virtual="assets/js/trainplan/knockout.pagefooter.tpl"-->
<style type="text/css">
.pagination>li>a, .pagination>li>span {
	position: relative;
	float: left;
	line-height: 1.428571429;
	text-decoration: none;
	background-color: #ffffff;
	border: 1px solid #dddddd;
	margin-left: -1px;
}

.ckbox.disabled {
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity = 65);
	-webkit-box-shadow: none;
	box-shadow: none;
}

.control-label0 {
	width: 132px;
}

.form-control0 {
	padding: 1px 10px;
}

.table caption+thead tr:first-child th, .table colgroup+thead tr:first-child th,
	.table thead:first-child tr:first-child th, .table caption+thead tr:first-child td,
	.table colgroup+thead tr:first-child td, .table thead:first-child tr:first-child td
	{
	border-bottom: 0;
}
</style>


</head>
<body class="Iframe_body"
	style="margin: -7px -2px 0px -3px; width: 100%">
	<!-- 右键的代码 -->
	<div class="rightMenu" id="checkhis" style="display: none">
		<ul>
			<li class="vm-list"><i class="fa fa-eye"></i>&nbsp;<a
				data-bind="click:showhis">&nbsp;审核记录</a></li>
			<li class="vm-list"><i class="fa fa-list-ol"></i>&nbsp;<a
				data-bind="click:showHisModify">&nbsp;调整记录</a></li>
			<li class="vm-list" id="ml"><i class="fa fa-table"></i>&nbsp;<a
				data-bind="click:loadCommand">&nbsp;命令内容</a></li>
		</ul>
	</div>


	<div id="hisshow" class="easyui-dialog" title="审核记录"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 600px; overflow-y: hidden;">

		<div id="hisshow1" class="row"
			style="margin: 5px; overflow: auto; height: 100%;">
			<div class="table-responsive">
				<table class="table table-bordered table-striped table-hover"
					id="yourTableID2" width="100%" border="0" cellspacing="0"
					cellpadding="0">
					<thead>
						<tr style="height: 25px">
							<th align="center" style="width: 20px">序号</th>
							<th align="center" style="width: 50px">审核局</th>
							<th align="center" style="width: 50px">审核时间</th>
							<th align="center" style="width: 50px">审核人</th>
							<th align="center" style="width: 80px">审核人岗位</th>
							<th align="center" style="width: 50px">历史/当前</th>
							<th align="center" style="width: 50px">审核结果</th>
							<th align="center" style="width: 50px">联系方式</th>
							<th align="center" style="width: 200px">不通过原因</th>
							<th style="width: 10px" align="center"></th>
						</tr>
					</thead>
				</table>
				<div id="left_height0" style="overflow-y: auto; height: 540px;">
					<table class="table table-bordered table-striped table-hover"
						style="border: 0;">
						<tbody data-bind="foreach: checkplans ">
							<tr>
								<td style="width: 25px" data-bind="text:$index() + 1"></td>
								<td style="width: 50px" data-bind="text: checkBureau "></td>
								<td style="width: 50px" data-bind="text: checkTime "></td>
								<td style="width: 50px" data-bind="text:checkPeople "></td>
								<td style="width: 80px" data-bind="text: checkDept"></td>
								<td style="width: 50px" data-bind="text:hisornow  "></td>
								<td style="width: 50px" data-bind="text:checkState  "></td>
								<td style="width: 50px" data-bind="text:checkPeopleTel  "></td>
								<td class="normal" style="width: 200px"
									data-bind="text:checkRejectReason  "></td>
								<td style="width: 10px" align="center" class="td_17"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>





	<div class="row" style="margin: 0px 0 0px 0;">
		<!--分栏框开始-->
		<div class="pull-left" style="width: 30%; height: 100%" id="leftBox">
			<!--分栏框开始-->
			<div class="row" style="margin: 0 10px 10px 10px;">
				<div>
					<div class="row" style="width: 100%;">
						<label for="exampleInputEmail3" class="control-label pull-left"
							style="margin-top: 5px;"> 开始日期:</label>
						<div class="pull-left" style="margin-left: 5px;">
							<input type="text" class="form-control"
								style="padding: 4px 12px;; width: 105px;" placeholder=""
								id="runplan_input_startDate" name="startDate"
								data-bind="value: searchModle().planStartDate" />
						</div>
						<label for="exampleInputEmail3" class="control-label pull-left"
							style="margin-top: 5px; margin-left: 20px;"> 截至日期:</label>
						<div class="pull-left" style="margin-left: 5px;">
							<input type="text" class="form-control"
								style="padding: 4px 12px;; width: 105px;" placeholder=""
								id="runplan_input_endDate" name="endDate"
								data-bind="value: searchModle().planEndDate" />
						</div>
					</div>
					<div class="row" style="width: 100%; margin-top: 5px;">
						<label for="exampleInputEmail3" class="control-label pull-left"
							style="margin-top: 5px;"> 担当局:</label>
						<div class="pull-left" style="margin-left: 5px;">
							<select id="bureausSel" style="padding: 4px 12px;; width: 118px"
								class="form-control"
								data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: '',event:{change: bureauChange}"></select>
						</div>
						<div class="pull-left" style="margin-left: 5px;">
							<input type="checkBox" class="pull-left" class="form-control"
								value="1"
								data-bind="checked: searchModle().currentBureanFlagFei,click: checkFei"
								style="margin-top: 9px; margin-left: 13px; width: 20px;"
								class="form-control">
						</div>
						<label for="exampleInputEmail3" class="control-label pull-left"
							style="margin-top: 5px; margin-left: 1px;"> 非本局担当</label>
						<div class="pull-left" style="margin-left: 5px;">
							<input type="checkBox" class="pull-left" class="form-control"
								value="1" data-bind="checked: searchModle().currentBureanFlag"
								style="margin-top: 9px; margin-left: 18px; width: 20px;"
								class="form-control">
						</div>
						<label for="exampleInputEmail3" class="control-label pull-left"
							style="margin-top: 5px; margin-left: 1px;"> 本局相关</label>

						<!-- 
												<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 5px;margin-left: 20px;">
													始发路局:</label>
												<div class="pull-left" style="margin-left: 5px; ">
													<select id="sflj" style="padding:4px 12px; ;width: 95px" class="form-control" data-bind="options:searchModle().startBureaus, value: searchModle().startBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
												</div> 
												 -->
					</div>
					<div class="row" style="margin-top: 5px;">
						<label for="exampleInputEmail3" class="control-label pull-left"
							style="margin-top: 5px;"> 车次:&nbsp;</label>
						<div class="pull-left">
							<input type="text" class="form-control"
								style="padding: 4px 12px;; width: 133px;"
								id="input_cross_filter_trainNbr"
								data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
						</div>
						<label for="exampleInputEmail3" class="control-label pull-left"
							style="margin-left: 20px; margin-top: 5px;"> 审核状态:</label>
						<div class="pull-left" style="margin-left: 5px;">
							<select style="padding: 4px 12px;; width: 105px"
								id="input_cross_sure_flag" class="form-control">
								<option value=""></option>
								<option value="0">本局未审</option>
								<option value="1">本局通过</option>
								<option value="2">本局不通过</option>
								<option value="3">任意不通过</option>
								<option value="4">全部通过</option>
								<!-- 
								<option value="1">部分审核</option>
								<option value="2">全部审核</option>
								<option value="0">未审核</option>
								 -->
								<!--     [{"value": "1", "text": "部分审核"},{"value": "2", "text": "全部审核"},{"value": "0", "text": "未审核"}]; -->
							</select>
						</div>

						<div class="pull-left" style="margin-left: 10px;">
							<a type="button" class="btn btn-success" data-toggle="modal"
								style="padding: 3px 10px;" data-target="#" id="btn_cross_search"
								data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a>
						</div>
					</div>

					<div id="plan_cross_default_panel" class="panel panel-default"
						style="margin-top: 10px">
						<!-- 
										  <div class="row" style="margin:10px 0 5px 10px;">
										  	<a type="button"     class="btn btn-success disabled" data-toggle="modal" data-target="#" id="btn_cross_sure"  data-bind="click: checkCrossInfo1" ><i class="fa fa-eye"></i>审核</a>
										  	<a  type="button"  class="btn btn-success disabled" data-toggle="modal" data-target="#" id="btn_cross_delete"  data-bind="click: deleteCrosses"><i class="fa fa-trash-o"></i>删除</a>
										  	<!-- <a  type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 2px;" data-target="#" id="btn_cross_createTrainLines" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: createTrainLines">生成运行线</a> -->
						<!--		  	
										  </div>
										 -->
						<div class="form-group" style="margin: 10px 0 5px 10px;">
							审核： <a type="button" class="btn btn-success disabled"
								data-toggle="modal" data-target="#" id="btn_cross_sure"
								data-bind="click: checkCrossInfo1"> <i class="fa fa-check"></i>通过
							</a> <span style="margin-left: 50px;"></span> <a type="button"
								class="btn btn-success disabled" data-toggle="modal"
								data-target="#" id="btn_cross_delete"
								data-bind="click: deleteCrosses"><i class="fa fa-trash-o"></i>删除</a>
							<div class="form-group"
								style="margin-left: 31px; margin-top: 5px">
								<a type="button" class="btn btn-success disabled"
									data-toggle="modal" data-target="#" id="btn_cross_no"
									data-bind="click: dhCrosses"> <i class="fa fa-times"></i>不通过
								</a>
						         <a  type="button" class="btn btn-success" data-toggle="modal" 
						         data-target="#" id="btn_cross_sendMsg" style="margin-left: 50px;" data-bind="click: sendMsg">
							         <i class="fa fa-volume-up"></i>发消息
						         </a>
							</div>
						</div>



						<!-- 
										 <div style="margin-left:10px;">相关局: <span style="margin-top:5px;margin-left:5px;" data-bind="html: currentCross().relevantBureauShowValue()"></span></div> 
									     <div style="margin-left:10px;">已审局: <span style="margin-top:5px;margin-left:5px;" data-bind="text :checkburu()"></span></div> 
									      -->
						<div style="margin-left: 49px;">
							相关局: <span style="margin-top: 5px; margin-left: 5px;"
								data-bind="html: currentCross().relevantBureauShowValue()"></span>
						</div>
						<div style="margin-left: 23px;">
							审核通过局: <span
								style="margin-top: 5px; margin-left: 5px; color: green;"
								data-bind="text:checkburu()"></span>
							&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
							审核不通过局: <span
								style="margin-top: 5px; margin-left: 5px; color: red;"
								data-bind="text:checkburuNo()"></span>
						</div>

						<div id="row_table" class="row"
							style="margin: 0 5px 5px 5px; overflow: auto;">
							<div class="table-responsive">
								<table class="table table-bordered table-striped table-hover"
									id="yourTableID2" width="100%" border="0" cellspacing="0"
									cellpadding="0">
									<thead>
										<tr style="height: 25px">
											<th style="width: 24px" align="center"><input
												type="checkbox" style="margin-top: 0" value="1"
												data-bind="event:{change:testallcheck}" id="checkall"></th>


											<!-- 																<th style="width: 32px" align="center"><input type="checkbox" style="margin-top:0" value="1" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th>
 -->
											<th align="center" style="width: 37px">序号</th>
											<th align="center" style="width: 38px">担当</th>
											<th align="center" style="width: 80px">车次</th>
											<th align="center" style="width: 90px">始发站</th>
											<th align="center" style="width: 90px">终到站</th>
											<th align="center">来源</th>
											<th align="center" style="width: 38px">重点</th>
											<th style="width: 17px" align="center"></th>
										</tr>
									</thead>
								</table>
								<div id="left_height" style="overflow-y: auto;">
									<table class="table table-bordered table-striped table-hover"
										style="border: 0;">
										<tbody data-bind="foreach: planTrainLkRows" id="choicall">
											<tr
												data-bind=" style:{color: $parent.currentCross().planTrainId == planTrainId ? 'blue':''},event:{mousedown:$parent.showRightTrains}">

												<td align="center" style="width: 24px"><input
													type="checkbox" value="1" id="testcheck" name="testcheck"
													data-bind="attr:{class: relevantBureau().indexOf(currentBureauShortName) < 0 ?  'ckbox disabled' : ''},event:{change:$parent.testonecheck}">
												</td>

												<td style="width: 37px"
													data-bind="event:{mousedown: $parent.showTrains},text: $index() + 1"></td>
												<td style="width: 38px"
													data-bind="event:{mousedown: $parent.showTrains},text: tokenVehBureauShowValue"></td>
												<td style="width: 80px;"><i
													data-bind="attr:{class: checkCss}"></i><span
													data-bind="text: trainNbr, attr:{title: trainNbr},event:{mousedown:$parent.showTrains}"></span></td>
												<!-- <td style="width: 70px" data-bind="event:{mousedown: $parent.showTrains},text: trainNbr, attr:{title: trainNbr}"></td> -->
												<td style="width: 90px"
													data-bind="event:{mousedown: $parent.showTrains},text: startStn, attr:{title: startStn}"></td>
												<td style="width: 90px"
													data-bind="event:{mousedown: $parent.showTrains},text: endStn, attr:{title: endStn}"></td>
												<td
													data-bind="event:{mousedown: $parent.showTrains},text: cmdShortInfo, attr:{title: cmdShortInfo}"></td>
												<td style="width: 38px" align="center"
													data-bind="event:{mousedown: $parent.showTrains},html: importantFlagStr, attr:{title: importantFlagStr}"></td>
												<!-- 									    <td style="vertical-align: middle;text-align:center;" data-bind="click: $parent.setCurrentRec, html: importantFlagStr"></td>
 -->
												<td style="width: 17px" align="center" class="td_17"></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="blankBox"></div>
		<div class="pull-right" style="width: 69%; margin: 0 7px 0 0;"
			id="rightBox">
			<!-- Nav tabs -->
			<div class="panel panel-default"
				style="margin-left: -10px; margin-right: -5px">
				<ul class="nav nav-tabs"
					style="margin-top: 10px; margin-left: 5px; margin-right: 5px;">
					<li class="active"><a style="padding: 3px 10px;" href="#home"
						data-toggle="tab">运行图</a></li>
					<li style="float: right"><input type="checkbox"
						class="pull-left" class="form-control"
						data-bind="checked: isShowRunPlans, event:{change: showRunPlans}"
						class="form-control"> <label for="exampleInputEmail5"
						class="control-label pull-left" style="margin: 2px 0 0 5px;">
							显示开行情况</label></li>
				</ul>
				<!-- Tab panes -->
				<div class="tab-content">
					<div class="tab-pane active" id="home">
						<div>
							<div>
								<div class="row" style="margin: 5px 0 10px 0;">
									<form class="form-inline" role="form">
										<div class="row" style="margin: 5px 0 10px 50px;">
											<button type="button" class="btn btn-success btn-xs"
												id="canvas_event_btn_refresh">
												<i class="fa fa-refresh"></i>刷新
											</button>
											<button type="button" class="btn btn-success btn-xs"
												id="canvas_event_btn_x_magnification">
												<i class="fa fa-search-plus"></i>X+
											</button>
											<button type="button" class="btn btn-success btn-xs"
												id="canvas_event_btn_x_shrink">
												<i class="fa fa-search-minus"></i>X-
											</button>
											<button type="button" class="btn btn-success btn-xs"
												id="canvas_event_btn_y_magnification">
												<i class="fa fa-search-plus"></i>Y+
											</button>
											<button type="button" class="btn btn-success btn-xs"
												id="canvas_event_btn_y_shrink">
												<i class="fa fa-search-minus"></i>Y-
											</button>
											比例：｛X:<label id="canvas_event_label_xscale">1</label>倍；Y:<label
												id="canvas_event_label_yscale">1</label>倍｝ <span><input
												type="checkbox" id="canvas_checkbox_stationType_jt"
												name="canvas_checkbox_stationType" style="margin-left: 10px">简图</span>
											<input type="checkbox" id="canvas_checkbox_trainTime"
												style="margin-left: 10px; margin-top: 2px" value="" />时刻

										</div>
									</form>
								</div>
								<div id="canvas_parent_div" class="table-responsive"
									style="width: 100%; overflow-x: auto; overflow-y: auto;">
									<canvas id="canvas_event_getvalue"></canvas>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="big-learn-more-content" class="row" class="panel-collapse"
		style="margin: -5px 2px 0px 10px;">
		<div class="panel panel-default">
			<div id="learn-more-content">
				<div class="panel-body" style="padding: 5px;">
					<ul class="nav nav-tabs">
						<li class="active"><a style="padding: 3px 10px;"
							href="#runPlan" data-toggle="tab">开行情况</a></li>
					</ul>
					<div class="tab-content" style="height: 220px; overflow: auto">
						<div class="tab-pane active" id="runPlan">
							<div id="plan_view_div_palnDayDetail">
								<!--panle-heading-->
								<div style="padding: 10px 0; overflow: auto">
									<div class="table-responsive">
										<table class="table table-bordered table-striped table-hover"
											id="run_plan_table">
											<thead>
												<tr
													data-bind="template: { name: 'runPlanTableDateHeader', foreach: planDays }"></tr>
												<tr
													data-bind="template: { name: 'runPlanTableWeekHeader', foreach: planDays }"></tr>
											</thead>
											<tbody
												data-bind="template: { name: 'runPlanTableVlaues', foreach: trainPlans }"
												style="border-top: 1px solid #d1d2d4;">
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<!-- Tab panes -->
					<!-- 
					<div class="tab-content" style="height: 220px; overflow: auto">
						<div class="tab-pane active" id="runPlan">
							<div id="plan_view_div_palnDayDetail">
								panle-heading
								
								<div style="padding: 10px 0; overflow: auto">
									<div class="table-responsive">
										<table class="table table-bordered table-striped table-hover"
											id="run_plan_table">
											<thead>
												<tr
													data-bind="template: { name: 'runPlanTableDateHeader', foreach: planDays }"></tr>
												<tr
													data-bind="template: { name: 'runPlanTableWeekHeader', foreach: planDays }"></tr>
											</thead>
											<tbody
												data-bind="template: { name: 'runPlanTableVlaues', foreach: trainPlans }"
												style="border-top: 1px solid #d1d2d4;">
											</tbody>
										</table>
									</div>
								</div>
								
							</div>
						</div>
					</div>
					 -->
				</div>
			</div>
		</div>
	</div>
	<!--详情时刻表-->
	<div id="train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 750px; height: 700px; overflow-y: hidden;">
		<iframe style="width: 100%; border: 0; overflow-y: hidden;" src=""></iframe>


		<div id="run_plan_train_times" class="easyui-dialog" title="命令信息查询"
			data-options="iconCls:'icon-save'"
			style="width: 800px; padding: 10px;">
			<!--   <div class="panel-body row">  
                              valus:	self.searchModle().cmdNbrBureau(result.data.cmdNbrBureau)
								valus:	self.searchModle().cmdItem(result.data.cmdItem)
								valus:	self.searchModle().cmdNbrSuperior(result.data.cmdNbrSuperior)
								valus:	self.searchModle().cmdTime(result.data.cmdTime)
								valus:	self.searchModle().largeContent(result.data.largeContent)
								valus:	self.searchModle().releasePeople(result.data.releasePeople)
          <div class="form-group row">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">铁总令号：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewCross" type="text" class="form-control" data-bind="value:searchModle().cmdNbrSuperior">
            </div>          
          </div>
          <div class="form-group row">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">局令号：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewGroup" type="text" class="form-control" data-bind="value:searchModle().cmdNbrBureau ">
               </div>
          </div>
          <div class="form-group row">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">项号：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_throughLine" type="text" class="form-control" data-bind="value: searchModle().cmdItem ">
               </div>
          </div>
          
          
          <div class="form-group row">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">发令日期：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_name1" type="text" class="form-control" data-bind="value: searchModle().cmdTime">
               </div>
             
          </div>
          <div class="form-group row">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">发令人：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_tel1" type="text" class="form-control" data-bind=" value:searchModle().releasePeople ">
               </div>
             
          </div>
          <div class="form-group row">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">命令内容：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <textarea id="add_note" class="form-control" rows="6" data-bind="value:searchModle().largeContent "></textarea>
            </div>
          </div>
     
     
	   </div>  -->
			<div class="panel-body row">
				<form class="bs-example form-horizontal" style="margin: 10px;">

					<div class="form-group row">
						<label for="exampleInputEmail2"
							class="control-label control-label0 pull-left">铁总令号：</label>
						<div class="pull-left">
							<input type="text" class="form-control form-control0"
								style="margin-left: 26px; width: 170px;"
								data-bind="value:searchModle().cmdNbrSuperior" />
							<!--  <input type="text" class="form-control" style="margin-left:26px;width:155px;" data-bind=" value:searchModle().cmdNbrBureau"/>
               -->
							<!-- 
              <span data-bind="validationMessage: cmdNbrBureau"></span>
               -->
						</div>

					</div>

					<div class="form-group row">

						<label for="exampleInputEmail2"
							class="control-label control-label0 pull-left">&nbsp;&nbsp;&nbsp;&nbsp;局令号：</label>

						<div class="pull-left">
							<input type="text" class="form-control form-control0"
								style="margin-left: 26px; width: 170px;"
								data-bind=" value:searchModle().cmdNbrBureau" />
						</div>

						<label for="exampleInputEmail2"
							class="control-label control-label0 pull-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;项号：</label>
						<div class="pull-left">
							<input type="text" class="form-control form-control0"
								style="margin-left: 26px; width: 170px;"
								data-bind="value: searchModle().cmdItem" />
						</div>

					</div>
					<div class="form-group row">

						<label for="exampleInputEmail2"
							class="control-label control-label0 pull-left">&nbsp;发令日期：</label>

						<div class="pull-left">
							<input type="text" class="form-control form-control0"
								style="margin-left: 26px; width: 170px;"
								data-bind=" value: searchModle().cmdTime" />
						</div>

						<label for="exampleInputEmail2"
							class="control-label control-label0 pull-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发令人：</label>
						<div class="pull-left">
							<input type="text" class="form-control form-control0"
								style="margin-left: 26px; width: 170px;"
								data-bind="value:searchModle().releasePeople" />
						</div>

					</div>
					<div class="form-group row">
						<label for="exampleInputEmail2"
							class="control-label control-label0 pull-left">&nbsp;命令内容：</label>
						<div class="pull-left" style="margin-left: 26px;">
							<!--    < <textarea type="text" class="form-control" style="width:375px;" data-bind="value:searchModle().largeContent"/> -->
							<textarea id="add_note" class="form-control form-control0"
								rows="12" style="width: 498px;"
								data-bind="value:searchModle().largeContent "></textarea>
							<!-- 
              <span data-bind="validationMessage: cmdNbrBureau"></span>
                <textarea id="add_note" class="form-control" rows="6" data-bind="value:searchModle().largeContent "></textarea>
               -->
						</div>

					</div>
				</form>
			</div>



			<div id="dhCross_dialog" class="easyui-dialog" title="交路不通过"
				data-options="iconCls:'icon-save'"
				style="width: 600px; height: 300px; overflow: hidden;">
				<iframe
					style="width: 100%; height: 395px; border: 0; overflow: hidden;"
					src=""></iframe>
			</div>
			<!--发消息-->
			<div id="run_plan_sendMsg" class="easyui-dialog" title="发消息"
					data-options="iconCls:'icon-save'"
					style="width: 600px; height: 365px;overflow: hidden;">
			  <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
			</div>

			<!--调整时刻表-->
			<div id="run_plan_train_times_edit_dialog" class="easyui-dialog"
				title="调整时刻表" data-options="iconCls:'icon-save'"
				style="width: 1560px; height: 700px; overflow: hidden;">
				<iframe
					style="width: 100%; height: 395px; border: 0; overflow: hidden;"
					src=""></iframe>
			</div>
			<!--调整经路-->
			<div id="run_plan_train_pathway_edit_dialog" class="easyui-dialog"
				title="调整径路" data-options="iconCls:'icon-save'"
				style="width: 1560px; height: 700px; overflow: hidden;">
				<iframe
					style="width: 100%; height: 395px; border: 0; overflow: hidden;"
					src=""></iframe>
			</div>
			<!--停运 和  备用-->
			<div id="run_plan_train_times_stop_dialog" class="easyui-dialog"
				title="停运" data-options="iconCls:'icon-save'"
				style="width: 880px; height: 550px; overflow: hidden;">
				<iframe
					style="width: 100%; height: 100%; border: 0; overflow: hidden;"
					src=""></iframe>
			</div>
			<div id="run_plan_train_crew_dialog" class="easyui-dialog"
				title="调整时刻表" data-options="iconCls:'icon-save'"
				style="width: 1200px; height: 400px; overflow: hidden;">
				<iframe
					style="width: 100%; height: 395px; border: 0; overflow: hidden;"
					src=""></iframe>
			</div>
			<!--调整记录 -->
			<div id="modifyRecordDiv" class="easyui-dialog" title="调整记录"
				data-options="iconCls:'icon-save'"
				style="width: 1400px; height: 650px; overflow-y: hidden;">

				<div id="modifyRecordDiv_1" class="row"
					style="margin: 5px; overflow: auto;">
					<div class="table-responsive">
						<table class="table table-bordered table-striped table-hover"
							id="yourTableID2" width="100%" border="0" cellspacing="0"
							cellpadding="0">
							<thead>
								<tr style="height: 25px">
									<!-- <th align="center" style="width: 40px">序号</th> -->
									<th align="center" style="width: 160px">交路名</th>
									<th align="center" style="width: 80px">车次</th>
									<th align="center" style="width: 60px; padding: 4px 0;">调整局</th>
									<th align="center" style="width: 80px">调整类型</th>
									<th align="center" style="width: 80px">调整依据</th>
									<th align="center" style="width: 80px">起始日期</th>
									<th align="center" style="width: 80px">终止日期</th>
									<th align="center" style="width: 70px">规律</th>
									<th align="center" style="width: 100px">择日日期</th>
									<th align="center" style="width: 70px">调整人</th>
									<th align="center" style="width: 90px">岗位</th>
									<th align="center" style="width: 80px">开行日期</th>
									<th align="center" style="width: 80px">调整时间</th>
									<th align="center" style="width:">调整内容</th>
									<th style="width: 17px" align="center"></th>
								</tr>
							</thead>
						</table>
						<div id="left_height2" style="overflow-y: auto; height: 580px;">
							<table class="table table-bordered table-striped table-hover"
								style="border: 0;">
								<!-- <tbody data-bind='template: { name: "giftRowTemplate", foreach: modifyPlanRecords1 }'> -->
								<tbody data-bind="foreach: modifyPlanRecords1">
									<!-- ko foreach:{data:modifyRecordS,as : 'm'} -->
									<tr>
										<!-- ko if: $index() == 0 -->
										<!-- <td style="width: 40px" data-bind="text:$index() + 1,attr: { rowspan:$parent.modifyRecordSLength}"></td> -->
										<td style="width: 160px"
											data-bind="text: crossName,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 80px"
											data-bind="text: trainNbr,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 60px; padding: 4px 0;"
											data-bind="text: modifyPeopleBureau,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 80px"
											data-bind="text: modifyType,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 80px"
											data-bind="text: modifyReason,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 80px"
											data-bind="text: startDate,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 80px"
											data-bind="text: endDate,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 70px"
											data-bind="text: rule,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 100px"
											data-bind="text: selectedDate,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<!-- <td style="width: " data-bind="text: modifyContent,attr: { rowspan:$parent.modifyRecordSLength}"></td> -->
										<td style="width: 70px"
											data-bind="text: modifyPeople,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<td style="width: 90px"
											data-bind="text: modifyPeopleOrg,attr: { rowspan:$parent.modifyRecordSLength}"></td>
										<!-- /ko -->
										<td style="width: 80px" data-bind="text: runDate "></td>
										<td style="width: 80px" data-bind="text: modifyTime  "></td>
										<td style="width:" data-bind="text: modifyContent"></td>
										<!-- <td style="width: 17px" align="center" class="td_17"></td> -->
									</tr>
									<!-- /ko -->

								</tbody>
								<!-- 
						<tbody data-bind="foreach: modifyPlanRecords ">
							<tr  >
						        <td style="width: 40px" data-bind="text:$index() + 1"></td>
						        <td style="width: 160px" data-bind="text: crossName, attr:{title: crossName}"></td>
						        <td style="width: 80px" data-bind="text: trainNbr"></td>
						        <td style="width: 80px" data-bind="text: runDate "></td>
						        <td style="width: 60px;padding: 4px 0;" data-bind="text: modifyPeopleBureau"></td>
						        <td style="width: 80px"  data-bind="text: modifyTime  "></td>
						        <td style="width: 80px" data-bind="text: modifyType  "></td>
						        <td style="width: 80px"  data-bind="text: modifyReason, attr:{title: modifyReason}"></td>
						        <td style="width: 80px" data-bind="text: startDate  "></td>
						        <td style="width: 80px" data-bind="text: endDate  "></td>
						        <td style="width: 70px" data-bind="text: rule  "></td>
						        <td style="width: 100px"  data-bind="text: selectedDate, attr:{title: selectedDate}"></td>
						        <td style="width: " data-bind="text: modifyContent , attr:{title: modifyContent} "></td>
						        <td style="width: 70px" data-bind="text: modifyPeople  , attr:{title: modifyPeople}  "></td>
						        <td style="width: 90px" data-bind="text: modifyPeopleOrg  , attr:{title: modifyPeopleOrg}  "></td>
							    <td style="width: 17px" align="center" class="td_17"></td>     
							</tr> 
						</tbody> 
						 -->
							</table>
						</div>
					</div>
				</div>

			</div>
		</div>



		<!-- Custom styles for this template -->
		<script type="text/javascript"
			src="<%=basePath%>/assets/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/resizable.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/jquery.freezeheader.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/chromatable_1.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/trainplan/util/fishcomponent.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/trainplan/util/canvas.util.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/trainplan/util/canvas.component.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/trainplan/knockout.pagemodle.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/trainplan/runPlanLk/canvas_rightmenu_by_main.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/trainplan/runPlanLk/canvas_event_runplanlk.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>/assets/js/trainplan/runPlanLk/runPlanLk_main.js"></script>


		<script>
			$(document).ready(function() {
				//$("#yourTableID2").chromatable({
				//width: "100%", // specify 100%, auto, or a fixed pixel amount
				//height: "580px",
				//scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
				//});

				$("#plan_runline_table_trainLine").chromatable({
					width : "100%", // specify 100%, auto, or a fixed pixel amount
					height : "500px",
					scrolling : "yes" // must have the jquery-1.3.2.min.js script installed to use
				});

				$("#plan_runline_table_trainLine_alltimes").chromatable({
					width : "100%", // specify 100%, auto, or a fixed pixel amount
					height : "500px",
					scrolling : "yes" // must have the jquery-1.3.2.min.js script installed to use
				});

			});
		</script>
		<script type="text/javascript">
			$(function() {
				resize("leftBox", "rightBox", "536", "620");
			});
		</script>
</body>
<script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left">共<span data-bind="html: totalCount()"></span>条  当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span> 								 
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table> 
</script>



<script type="text/html" id="runPlanTableDateHeader"> 
   	<!-- ko if: $index() == 0 -->
 	<td rowspan="2" width="100px" style="vertical-align: middle;">车次</td>
 	<!-- /ko -->
 	<td align='center' data-bind="text: day"></td>
</script>
<script type="text/html" id="runPlanTableWeekHeader">  
 	<td  style='border-bottom: 0;padding-top: 0;' align='center' data-bind="html: week, style:{color: (weekDay==0||weekDay==6) ? 'blue':''}"></td>
</script>
<script type="text/html" id="runPlanTableVlaues">
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
 	<td data-bind="text: $parent.trainNbr, attr:{title: $parent.trainNbr}"></td>
 	<!-- /ko -->  
 	<td style="vertical-align: middle;" align='center' data-bind="html: runFlagStr"></td>
 </tr> 

</script>
</html>
