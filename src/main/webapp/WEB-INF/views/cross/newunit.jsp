<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List"%>
<%
	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
	List<String> permissionList = user.getPermissionList();
	String userRolesString = "";
	for(String p : permissionList){
		userRolesString += userRolesString.equals("") ? p : "," + p;
	} 
	String basePath = request.getContextPath();
	System.out.println(basePath);
	String  currentBureauShortName = user.getBureauShortName();
	String currentUserBureau = user.getBureau();
%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>交路单元管理</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="<%=basePath%>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath%>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath%>/assets/css/cross/custom-bootstrap.css"
	rel="stylesheet">
<link href="<%=basePath%>/assets/css/cross/cross.css" rel="stylesheet">
<link href="<%=basePath%>/assets/easyui/themes/icon.css"
	rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="<%=basePath%>/assets/css/font-awesome.min.css" />
<!-- Custom styles for this template -->

<link href="<%=basePath%>/assets/css/style.css" rel="stylesheet">
<script type="text/javascript" src="<%=basePath%>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/html5.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/respond.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/resizable.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/knockout.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/ajaxfileupload.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/trainplan/cross/crossunitnew.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/datepicker.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/trainplan/common.js"></script>
<script src="<%=basePath%>/assets/js/trainplan/knockout.pagemodle.js"></script>
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

.Iframe_body {
	padding: 12px 2%;
}

.label-danger {
	background-color: #ec9820;
}
.ckbox.disabled {
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity = 65);
	-webkit-box-shadow: none;
	box-shadow: none;
}

table td {
	text-align: center;
}

.nav-pills > li{
	color: #333;
  	background-color: #cccccc;
}
.nav-pills > li.active > a,
.nav-pills > li.active > a:hover,
.nav-pills > li.active > a:focus {
  color: #fff;
  background-color: #666666;
}

.btn-default { /* 定义default风格相关的颜色 */  
  color: #333;  
  background-color: #fff;  
  border-color: #ccc;  
}  
.btn-default:hover,  
.btn-default:focus,  
.btn-default:active,  
.btn-default.active,  
.open .dropdown-toggle.btn-default {  /* hover、focus、active变化时的颜色 */  
  color: #333;  
  background-color: #ebebeb;  
  border-color: #adadad;  
}  
.btn-default:active,  
.btn-default.active,  
.open .dropdown-toggle.btn-default {  /* active状态下,背景图设置为none */  
  background-image: none;  
}  
.btn-default.disabled,  
.btn-default[disabled],  
/* 此处省略部分选择符 */  
.btn-default[disabled].active,  
fieldset[disabled] .btn-default.active {  /* 禁用状态下,各种变化时的颜色 */  
  background-color: #fff;  
  border-color: #ccc;  
}  
.btn-default .badge {  /* 按钮内部有徽章的话,设置徽章的显示颜色和背景色 */  
  color: #fff;  
  background-color: #333;  
} 

.btn-danger {
	color: #fff;
	/*	background-color: #e2493d;
	border-color: #df3427;*/
background-color: #d74f4b;
	border-color:#b71f1b;
}
.btn-danger:hover, .btn-danger:focus, .btn-danger:active, .btn-danger.active, .open .dropdown-toggle.btn-danger {
	color: #fff;
	/*	background-color: #FF5A5A;
	border-color: #ae251a;*/
	background-color: 
#d74f4b;
	border-color: 
#b71f1b;
}
.inputColor {
	color: #27bdaf;
}

.btn-info {
  color: #fff;
  background-color: #27bdaf;
  border-color: #27bdaf;
}
.btn-info:hover,
.btn-info:focus,
.btn-info:active,
.btn-info.active,
.open .dropdown-toggle.btn-info {
  color: #fff;
  background-color: #27bdaf;
  border-color: #27bdaf;
}
.btn-info:active,
.btn-info.active,
.open .dropdown-toggle.btn-info {
  background-image: none;
}
.btn-info.disabled,
.btn-info[disabled],
fieldset[disabled] .btn-info,
.btn-info.disabled:hover,
.btn-info[disabled]:hover,
fieldset[disabled] .btn-info:hover,
.btn-info.disabled:focus,
.btn-info[disabled]:focus,
fieldset[disabled] .btn-info:focus,
.btn-info.disabled:active,
.btn-info[disabled]:active,
fieldset[disabled] .btn-info:active,
.btn-info.disabled.active,
.btn-info[disabled].active,
fieldset[disabled] .btn-info.active {
  background-color: #27bdaf;
  border-color: #27bdaf;
}
.btn-info .badge {
  color: #5bc0de;
  background-color: #27bdaf;
}
</style>
<script type="text/javascript">
	$(function() {
	   resize("leftBox","rightBox","490","540");
	 });
</script>
<script type="text/javascript">
var basePath = "<%=basePath%>";
var all_role = "<%=userRolesString%>";
var currentBureauShortName = "<%=currentBureauShortName%>";
var currentUserBureau = "<%=currentUserBureau %>";
</script>
</head>
<body class="Iframe_body"
	style="margin: -8px -8px 0px -19px; width: 100%; height:1080px; overflow-y:hidden;">
	<!--分栏框开始-->
	<div class="pull-left" style="width: 50%;" id="leftBox">
		<!--分栏框开始-->
		<div class="row" style="margin: 0px 10px 10px 10px;">
			<div class="form-group"
				style="float: left; margin-left: -10px; margin-top: 0px; width: 100%">
				<div class="row" style="width: 100%;">
					<span style="color:#1106aa; margin-top:5px; font-weight:bold;" class="pull-left"></span>
					<label for="exampleInputEmail3" class="control-label"
						style="margin-top: 5px; font-weight:bold; margin-left:10px;"> 方案:&nbsp;</label>
					<div class="pull-left">
						<select style="padding: 1px 8px;; width: 250px; color:#e60302; font-weight:bold;"
							id="input_cross_chart_id" class="form-control"
							data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
						</select>
					</div>
					<!-- <label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-top: 5px; margin-left:20px;"> 担当局:</label>
					<div class="pull-left" style="margin-left: 5px;">
						<select style="padding: 1px 8px;; width: 106px"
							class="form-control"
							data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code',event:{change: bureauChange}">
						</select>
					</div>
					<input type="checkBox" class="pull-left" class="form-control"
						value="1" data-bind="checked: searchModle().currentBureanFlag"
						style="width: 20px; margin-top: 7px; margin-left: 20px"
						class="form-control"> <label for="exampleInputEmail5"
						class="control-label pull-left" style="margin-top: 5px;">
						本局相关</label> -->
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-top: 5px;margin-left:20px;"> 车次:&nbsp;</label>
					<div class="pull-left">
						<input type="text" class="form-control"
							style="padding: 1px 8px; width: 120px;"
							id="input_cross_filter_trainNbr"
							data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
					</div>
					<a type="button" class="btn btn-primary btn-input"
						data-toggle="modal" style="margin-left: 30px;" data-target="#"
						id="btn_cross_search" data-bind="click: loadCrosses">查询</a>
				</div>
				<div class="row" style="margin: 25px 5px 5px 5px;">
					<div class="btn-group pull-left">
					      <button  value="0" type="button" data-bind="click: all_relevant_tokenValue" style="width:100px; height:40px;" class="btn btn-default">全部</button>
					      <button  value="1" type="button" data-bind="click: all_relevant_tokenValue" style="width:100px; height:40px;" class="btn btn-default">本局担当</span></button>
					      <button  value="2" type="button"data-bind="click: all_relevant_tokenValue" style="width:100px; height:40px;" class="btn btn-default">本局相关</span></button>
					      <!-- <button type="button" class="btn btn-danger" style="height:40px;width:100px;margin-right:3px">异常<span style="background-color:white;color:red;border-radius:3px;margin:0 1px">331</span></button>   -->
  					</div>
					<shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
							<a type="button" class="btn btn-warning btn-input pull-right" data-toggle="modal" data-target="#" id="btn_cross_delete" style="margin-left: 2px; margin-top:10px;" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-warning btn-input pull-right' : 'btn btn-danger  pull-right btn-input disabled'}, click: deleteCrosses">
						 		删除交路
						 	</a>
							<a type="button" class="btn btn-primary btn-input pull-right" style=" margin-top:10px;" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-primary btn-input pull-right' : 'btn btn-primary  pull-right btn-input disabled'}, click: checkCrossInfo" data-toggle="modal" data-target="#" id="btn_cross_sure">
								审核
							</a> 
					</shiro:hasPermission>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="panel panel-default" style="margin-top: -7px">
				<div class="table-responsive">
					<span style="margin-bottom: 5px; margin-left: 5px;"
						data-bind="html: currentCross().relevantBureauShowValue"></span>
					<table class="table table-bordered table-striped table-hover"
						style="margin-left: 5px; margin-right: 5px; width: 98%"
						id="cross_table_crossInfo">
						<thead>
							<tr style="height: 25px">
							<th style="width: 3%" align="center"><input
									type="checkbox" style="margin-top: 0" value="1"
									data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th>
								<th style="width: 3%;" align="center"> </th>
								<th style="width: 40%;" align="center">
									<div style="position: relative;">
										<label for="exampleInputEmail5"
											style="font-weight: bold; vertical-align: bottom;">车底交路</label>
										<!-- <select class="form-control"
											style="width: 58px; display: inline-block; position: absolute; top: -4px; margin-left: 5px;"
											id="input_cross_filter_showFlag"
											data-bind="options: [{'code': 2, 'text': '全称'},{'code': 1, 'text': '简称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'">
										</select> -->
									</div>
								</th>
								<th style="width: 10%;" align="center">担当局</th>
								<th style="width: 34%;" align="center">相关局</th>
								<th style="width: 10%;" align="center">审核状态</th>
							</tr>
						</thead>
					</table>

					<div id="crossInfo_Data"
						style="overflow-y: auto; width: 98%; margin: 0 5px;">
						<table class="table table-bordered table-striped table-hover"
							style="border: 0;">
							<tbody data-bind="foreach: crossRows.rows">
								<tr
									data-bind=" visible: visiableRow, style:{color: $parent.currentCross().unitCrossId == unitCrossId ? 'rgb(21, 124, 255)':''}">
									<td style="width: 3%" align="center"><input
										type="checkbox" value="1" 
										data-bind="attr:{class: activeFlag() == 0 ? 'ckbox disabled' : ''}, event:{change: $parent.selectCross}, checked: selected"></td>
									<td style="width: 3%;"
										data-bind=" text: $parent.crossRows.currentIndex()+$index()+1 , click: $parent.showTrains"></td>
									<td style="width: 40%;"
										data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains , attr:{title: crossName()}"></td>
									<td style="width: 10%;" align="center"
										data-bind=" text: tokenVehBureauShowValue"></td>
									<td style="width: 34%;" align="center" data-bind="text: relevantBureauShowValue"> </td>
									<td style="width: 10%;" align="center"
										data-bind="html: checkFlagStr"></td>
									

								</tr>
							</tbody>
						</table>
					</div>


					<div
						data-bind="template: { name: 'tablefooter-short-template', foreach: crossRows }"
						style="margin: 10px 0;"></div>
				</div>
			</div>
		</div>
		<!-- <div class="row">
			<div class="panel panel-default" style="margin-top: -7px">
					<div id="crossInfo_Data"
						style="overflow-y: auto; width: 98%; margin: 0 5px; height:auto;">
						<table class="table table-bordered table-striped table-hover"
							style="border: 0;">
							<thead>
							<tr style="height: 25px">
								<th style="width: 10px" align="center"> </th>
								<th style="width: 190px" align="center">车底交路</th>
								<th style="width: 60px" align="center">担当局</th>
								<th style="width: 140px" align="center">相关局</th>
								<th style="width: 15px" align="center">审核状态</th>
								<th style="width: 50px" align="center">操作</th>
							</tr>
						</thead>
							<tbody>
								<tr>
									<td style="width: 10px;" align="center">1</td>
									<td style="width: 190px" align="center">K914/1-K912/3-5002/3-5024/1</td>
									<td style="width: 60px" align="center">成</td>
									<td style="width: 140px" align="center"> </td>
									<td style="width: 15px" align="center"><button class="btn btn-warning" type="button">未</button></td>
									<td style="width: 50px" align="center"><button class="btn btn-primary" type="button">通</button>
									<button class="btn btn-danger" type="button">不</button></td>
								</tr>
								
							</tbody>
						</table>
					</div>

			</div>
		</div> -->
	</div>
	<div class="blankBox"></div>
	<div class="pull-left" style="width: 50%;" id="rightBox">
		<div class="panel panel-default" style="margin-right: -43px; width:100%;">
			<div class="row" style="margin: 10px 5px 5px 5px;">
				<section class="panel panel-default">
					<div class="panel-heading">
						<span>交路信息 
						</span>
					</div>
					<div class="panel-body"  style="height:450px; overflow-y: auto;">
						<div class="row"  style="width:98%;padding:2px;">
							<form class="form-horizontal" role="form" data-bind="with: currentCross"> 
							      <div class="row" style="margin: 3px;border-width:1px;"> 
							         <!-- <div class="row">        
										<div>
											<input type="text" class="form-control pull-right" style="width: 75%;margin:5px" data-bind="value: crossSpareName" disabled>
										</div> 
										<label for="exampleInputEmail3"
												class="control-label pull-right" style="margin:auto 0px"> 备用套跑交路名:&nbsp;</label>
								    </div> -->
								    <div class="row" style="margin: 5px 0 0px 0;">	
										<label for="exampleInputEmail3"
											class="control-label pull-left" style="margin-left:25px;"> 车底交路:&nbsp;</label>
										<div class="pull-left" style="margin-left: 26px;">
											<input type="text" class="form-control inputColor" style="width: 470px;" data-bind="value: crossName" disabled="disabled" 
												id="plan_construction_input_trainNbr">
										</div> 
								    </div> 
								 	<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 38px;"> 担当局:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;"  data-bind="value: tokenVehBureauShowValue" disabled="disabled" >
										</div>
										
										<!-- <div>
											<input type="text" class="form-control pull-right" style="width:75%;margin:5px" data-bind="value: crossName"
												id="plan_construction_input_trainNbr" disabled>
										</div>
										<label for="exampleInputEmail3"
											class="control-label pull-right" style="margin:auto 0px"> 截断原交路:&nbsp;</label> -->
									</div> 

									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 50px;"> 组数:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: groupTotalNbr" onkeyup="value=value.replace(/[^1-9]/g,'')" disabled="disabled" >
											</div> 
									</div>
									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 开行状态:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: spareFlag" disabled="disabled" >
										</div>
										
									</div>

									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 50px;"> 类型:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: locoType" disabled="disabled" >
											</div> 
									</div>
									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 交替日期:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: alterNateDate" disabled="disabled" >
										</div>
										

									</div>  
									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 25px;"> 交替车次:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: alterNateTranNbr" disabled="disabled" >
											</div> 
									</div>
						<!--虚线分隔线-->
								<DIV style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin-top:25px; margin-bottom:25px;"></DIV>

						    <span style="margin:5px">开行规律：</span>
							<table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%"
												id="cross_table_crossInfo">
												<col width="25%"></col> 
												<col width="25%"></col>
												<col width="25%"></col>
												<col width="25%"></col>
											<thead>
													<tr style="height: 25px"> 
														<th>普通规律</th>
														<th>指定星期</th>
														<th>指定日期</th>
														<th>指定周期</th>										
													</tr>
												</thead>
											</table>
															 <div id="crossInfo_Data" style="overflow-y:auto;margin-bottom:5px;margin-left:5px;width:98%"> 
																<table class="table table-bordered table-striped table-hover" style="border-top:0;text-align:center" id="table_left">
																	<col width="25%"></col> 
																	<col width="25%"></col> 
																	<col width="25%"></col> 
																	<col width="25%"></col> 
														
																	<tbody >
																		<tr>
																			<td class="inputColor" align="center" data-bind="text: commonlineRule"></td>
																			<td class="inputColor" data-bind="text: appointWeek"></td>
																			<td class="inputColor" data-bind="text: appointDay"></td>
																			<td class="inputColor" data-bind="text: appointPeriod"> </td>
																		</tr> 
																	</tbody>
																</table>
						    </div>
						</div>
					 
					 <!--虚线分隔线-->
								<DIV style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin-top:25px; margin-bottom:25px;"></DIV>
						                <div class="row" style="margin: 15px 0 0px 0;">
											
										<label for="exampleInputEmail3"
												class="control-label pull-left"  style="margin-left: 25px;"> 运行区段:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" maxlength="200" class="form-control pull-left inputColor" style="width: 150px;" data-bind="value: crossSection" disabled="disabled" >
										</div>
										
										</div>

										<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 25px;"> 运行距离:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" disabled="disabled" data-bind="value: runRange">

											</div>  
										</div>
										<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 50px;"> 等级:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;"  data-bind="value: crossLevel" disabled="disabled" >
										</div>
										
										<!-- <div>
											<input type="text" class="form-control pull-right" style="width:75%;margin:5px" data-bind="value: crossName"
												id="plan_construction_input_trainNbr" disabled>
										</div>
										<label for="exampleInputEmail3"
											class="control-label pull-right" style="margin:auto 0px"> 截断原交路:&nbsp;</label> -->
									</div> 

									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 50px;"> 对数:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: pairNbr" disabled="disabled" >
											</div> 
									</div>
									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 编组辆数:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;" disabled="disabled" data-bind="value: marshallingNums">
										</div>
										
									</div>

									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 50px;"> 定员:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" disabled="disabled" data-bind="value: peopleNums">
											</div> 
									</div>
									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 编组内容:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;" disabled="disabled" data-bind="value: marshallingContent" >
										</div>
										

									</div>  

									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 10px;"> 动车组类型:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control pull-left inputColor" style="width: 150px;" data-bind="value: crhType" disabled="disabled" >
											</div> 
									</div>
									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail5" class="control-label pull-left margin-2" style="margin-left:43px">
											供电：</label> 
											
										<input type="text" class=" pull-left form-control inputColor"
											value="1" data-bind="checked: elecSupply" disabled="disabled" 
											style="width: 60px; margin-top: 5px;text-align:center; margin-left:26px;"
											class="form-control">
										
									</div>  
									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail5" class="control-label pull-left margin-2" style="margin-left:43px">
											集便：</label>  
										<input type="text" class=" pull-left form-control inputColor"
											value="1" data-bind="checked: dejCollect" disabled="disabled" 
											style="width: 60px; margin-left: 5px; margin-top: 5px; text-align:center;margin-left:26px;";
											class="form-control">
										
									</div>
									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail5" class="control-label pull-left margin-2" style="margin-left:43px">
											空调：</label>  
										<input type="text" class="pull-left form-control inputColor"
											value="1" data-bind="checked: airCondition" disabled="disabled" 
											style="width: 60px; margin-left: 5px; margin-top: 5px ;text-align:center;margin-left:26px;"
											class="form-control"> 
									</div>
									<!--虚线分隔线-->
								<DIV style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin-top:25px; margin-bottom:25px;"></DIV>
									  <div class="row" style="margin: 15px 0 50px 0;">
									     <label for="exampleInputEmail3"  style="margin-left: 26px;"
												class="control-label pull-left"> 备注:&nbsp;</label>
										<div>
											<textarea type="text" class="form-control inputColor" style="margin-left: 26px;width: 90%;height:100px" data-bind="value: note" disabled="disabled" ></textarea><!-- data-bind="value: note" -->
										</div>
									</div>
							</div> 
						</form>
					    </div>
					  </div>

					<!-- <div class="panel-body" style="height:500px; overflow-y: auto;"> 
						<div class="row" style="width:700px;padding:2px;">
							<form class="form-horizontal" role="form"
								data-bind="with: currentCross">
								<div class="row" style="margin: 0px 0 5px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:95px;">
										车底交路:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 470px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:95px;">
										交替日期:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 470px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:95px;">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;组数:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 170px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:73px;">
										对数:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 170px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:94px;">
										开行状态:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 170px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:73px;">
										线型:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 170px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
								</div> -->
								<!--虚线分隔线-->
						<!-- 		<DIV style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin-top:15px; margin-bottom:15px;"></DIV>
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:55px;">
										备用套跑交路名:&nbsp;</label>
									<div class="pull-left"style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 470px;"
											data-bind="value: crossSpareName" disabled>
									</div>
									
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:81px;">
										交替原车次:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 170px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:33px;">
										截断原交路:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 170px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
								</div>	 -->
								<!--虚线分隔线-->
								<!-- <DIV style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin-top:15px; margin-bottom:15px;"></DIV>

								<div class="row" style="margin: 5px 0 0px 0; color:#5bc0de; font-weight:bold;">
									<p>开行规律：</p>
									<table class="table table-striped table-bordered table-hover">
									   <thead>
									     <tr>
									       <th>高线</th>
									       <th>普线</th>
									       <th>指定星期</th>
									       <th>指定日期</th>
									       <th>指定周期</th>
									     </tr>
									   </thead>
									   <tbody>
									     <tr>
									       <td> </td>
									       <td> </td>
									       <td>0000011</td>
									       <td> </td>
									       <td> </td>
									     </tr>
									   </tbody>
									</table>
								</div> -->	

								<!--虚线分隔线-->
								<!-- <DIV style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin-top:15px; margin-bottom:15px;"></DIV> -->
<!-- 
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:81px;">
										运行区段:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" value="青岛—郑州" style="width: 170px; color:#5bc0de; font-weight:bold;"
					
											id="plan_construction_input_trainNbr" >
									</div>
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:33px;">
										经由铁路线:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" value="京沪高铁、胶济客专" class="form-control" style="width: 170px; color:#5bc0de; font-weight:bold;"
										
											id="plan_construction_input_trainNbr" >
									</div>
								</div>	

								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:31px;">
										车辆担当局:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" value="济" style="width: 30px; color:#5bc0de; font-weight:bold;"
					
											id="plan_construction_input_trainNbr" >
									</div>
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:13px;">
										车辆段/动车段:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" value="青岛车辆段" class="form-control" style="width: 130px; color:#5bc0de; font-weight:bold;"
										
											id="plan_construction_input_trainNbr" >
									</div>
									<label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:13px;">
										动车所:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" value="" class="form-control" style="width: 130px; color:#5bc0de; font-weight:bold;"
										
											id="plan_construction_input_trainNbr" >
									</div>
								</div>	
								
								<div class="row" style="margin: 5px 0 5px 0;">
									
									
									<label for="exampleInputEmail5" class="control-label pull-left" style="margin-left:33px;">
										普速</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											style="width: 20px; margin-left: 5px; margin-top: 3px"
											class="form-control" value="1"
											data-bind="checked: highlineFlag" disabled>
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										高铁</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											style="width: 20px; margin-left: 5px; margin-top: 3px"
											class="form-control" value="2"
											data-bind="checked: highlineFlag" disabled>
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										混合</label>
									
											<label for="exampleInputEmail5"
												class="control-label pull-left" style="margin-left:50px;"> 普速规律:</label> <input
												type="radio" class="pull-left" disabled class="form-control"
												value="1" data-bind="checked: commonlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control"> <label
												for="exampleInputEmail5" class="control-label pull-left">
												每日</label> <input type="radio" class="pull-left" disabled
												class="form-control" value="2"
												data-bind="checked: commonlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control"> <label
												for="exampleInputEmail5" class="control-label pull-left">
												隔日</label>

										<label for="exampleInputEmail5"
												class="control-label pull-left" style="margin-left:50px;"> 高线规律:</label> <input
												type="radio" class="pull-left" disabled class="form-control"
												value="1" data-bind="checked: highlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control"> <label
												for="exampleInputEmail5" class="control-label pull-left">
												日常</label> <input type="radio" class="pull-left" disabled
												class="form-control" value="2"
												data-bind="checked: highlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control"> <label
												for="exampleInputEmail5" class="control-label pull-left">
												周末</label> <input type="radio" class="pull-left" disabled
												class="form-control" value="3"
												data-bind="checked: highlineRule"
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control"> <label
												for="exampleInputEmail5" class="control-label pull-left">
												高峰</label>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="pull-left">
										<div class="row" style="margin: 5px 0 0px 0;">
											<label class="control-label pull-left"
												style="margin-left: 32px;"> 担当局:&nbsp;</label>
											<div class="pull-left">
												<! <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
												<!-- <input type="text" class="form-control" disabled
													style="width: 50px;"
													data-bind="value: tokenVehBureauShowValue">
											</div>
											<label class="control-label pull-left"
												style="margin-left: 40px;"> 动车所:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" disabled
													style="width: 90px;" data-bind="value: tokenVehDepot">
											</div>
											<label for="exampleInputEmail3" style="margin-left: 40px;"
												class="control-label pull-left"> 客运担当局:&nbsp;</label>
											<div class="pull-left">
												<! <input type="text" class="form-control" style="width: 30px;" data-bind="value: tokenPsgDept"> -->
												<!-- <input type="text" disabled class="form-control disabled"
													style="width: 50px;"
													data-bind="value: tokenPsgBureauShowValue">
												<select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenPsgBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select>
											</div>
											<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 40px;"> 机车类型:&nbsp;</label> <input
												type="text" class="form-control pull-left"
												style="width: 40px;" data-bind="value: locoType" disabled>
										</div>
										</div>
									</div>
 -->
								<!-- 	<div class="row" style="margin: 5px 0 0px 0;">
									<div class="pull-left">
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 10px"
												class="control-label pull-left"> 动车组车型:&nbsp;</label> <input
												type="text" class="form-control pull-left"
												style="width: 60px;" data-bind="value: crhType" disabled>
												<label for="exampleInputEmail3" style="margin-left: 30px;"
												class="control-label pull-left"> 其他:&nbsp;</label> <input
												type="checkbox" class="pull-left" class="form-control"
												value="1" data-bind="checked: elecSupply"
												style="width: 20px; margin-top: 5px;" class="form-control"
												disabled> <label for="exampleInputEmail5"
												class="control-label pull-left margin-2"> 供电</label> <input
												type="checkbox" class="pull-left" class="form-control"
												disabled value="1" data-bind="checked: dejCollect"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control"> <label
												for="exampleInputEmail5"
												class="control-label pull-left margin-2"> 集便</label> <input
												type="checkbox" class="pull-left" class="form-control"
												disabled value="1" data-bind="checked: airCondition"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control"> <label
												for="exampleInputEmail5"
												class="control-label pull-left margin-2"> 空调</label>
												<label for="exampleInputEmail3" style="margin-left: 30px;"
												class="control-label pull-left"> 备注:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control"
													style="width: 182px;" data-bind="value: note" disabled>
											</div>
										</div>
										</div>
							</form>
						</div>
					</div> --> 
				</section>
			</div>
			<div class="row" style="margin: 15px 10px 10px 10px;">
				<div class="pull-left" style="width: 100%; height:500px;">
					<section class="panel panel-default">


						<!-- 选项卡组件（菜单项nav-pills）-->
						<ul id="myTab" class="nav nav-pills" role="tablist">
						    <li class="active"><a href="#bulletin" role="tab" data-toggle="pill" data-bind="click: showunittrain">列车信息（交）</a></li>
						    <li><a href="#rule" role="tab" data-toggle="pill" data-bind="click: showcrosstrain">列车信息（对）</a></li>
						    <li style="background-color:#fff;"><a type="button"
								style="margin-left: 15px; margin-top: 5px; border-radius:15px; padding-left:15px;"
								class="btn btn-info btn-input" data-toggle="modal" data-target="#"
								id="cross_train_save"
								data-bind="click: showCrossMapDlg"> 交路图
							</a> </li>
						    <li style="background-color:#fff;"><a type="button" style="margin-left: 5px; margin-top: 5px; border-radius:15px; padding-left:15px;"
								class="btn btn-info btn-input" data-toggle="modal" data-target="#"
								id="cross_train_save" data-bind="click: showCrossTrainTimeDlg2">
								时刻表
							</a></li>
						</ul>

						<!-- 选项卡面板 -->
						<div id="myTabContent" class="tab-content">
						    <div class="tab-pane fade in active" id="bulletin">
						    	<div class="panel-body" style="overflow-y: auto; height:320px;">
							<div class="table-responsive">
								<table class="table table-bordered table-striped table-hover"
									id="cross_trainInfo">
									<colgroup>
    									<col width="5%"> 
										<col width="5%"> 
										<col width="7%"> 
										<col width="10%">
										<col width="5%">
										<col width="10%"> 
										<col width="5%">
										<col width="5%">
										<col width="5%">
										<col width="5%">
										<col width="5%">
										<col width="10%">
										<col width="9%">
										<col width="15%">
  									</colgroup>
									<thead>
										<tr>
											<th
												style="vertical-align: middle"
												rowspan="2">组序</th>
											<th
												style="vertical-align: middle"
												rowspan="2">车序</th>
											<th style="vertical-align: middle" rowspan="2">车次</th>
											<th style="vertical-align: middle" rowspan="2">始发站</th>
											<th style="vertical-align: middle" rowspan="2">发局</th>
											<th style="vertical-align: middle" rowspan="2">终到站</th>
											<th style="vertical-align: middle" rowspan="2">终局</th>
											<th style="vertical-align: middle" rowspan="2">线型</th>
											<th >组内</br>间隔
											</th>
											<th>组间</br>间隔
											</th>
											<th style="vertical-align: middle" rowspan="2">状态</th>
											<!-- <th style="width:">备用</br>套跑
											</th> -->
											<th
												style="vertical-align: middle"
												rowspan="2">高铁规律</th>
											<th
												style="vertical-align: middle"
												rowspan="2">普速规律</th>
											<!-- <th style="width: 60px; padding: 4px 0px;">特殊</br>规律
											</th> -->
											<!-- <th
												style="width: 80px; padding: 4px 0px; vertical-align: middle"
												rowspan="2">交替车次</th> -->
											<th
												style="vertical-align: middle"
												rowspan="2">交替时间</th>
											<!-- <th
												style="width: 75px; padding: 4px 0px; vertical-align: middle"
												rowspan="2">起用日期</th>
											<th
												style="width: 75px; padding: 4px 0px; vertical-align: middle"
												rowspan="2">截止日期</th>
											<th style="width: 17px" align="center"></th> -->
										</tr>
									</thead>
								</table>
								<div id="train_information" style="overflow-y: auto;height:auto;">
									<table class="table table-bordered table-striped table-hover"
										style="border-top: 0">
										<colgroup>
	    									<col width="5%"> 
											<col width="5%"> 
											<col width="7%"> 
											<col width="10%">
											<col width="5%">
											<col width="10%"> 
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="10%">
											<col width="9%">
											<col width="15%">
  										</colgroup>
										<tbody data-bind="foreach: trains" id="fixtrainsort">
											<tr
												data-bind="click: $parent.setCurrentTrain, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'rgb(21, 124, 255)':''}">
												<td data-bind="text: groupSerialNbr"></td>
												<td
													style="text-align: center">
													<input type="text" style="width: 25px; text-align: center;"
													data-bind="value: trainSort" />
												</td>

												<td style="text-align: center;"
													data-bind="text: trainNbr"></td>
												<td style="text-align: center;"
													data-bind="text: startStn, attr:{title: startStn}"></td>
												<td style="text-align: center;"
													data-bind="text: startBureau"></td>
												<td style="text-align: center;"
													data-bind="text: endStn, attr:{title: endStn}"></td>
												<td style="text-align: center;"
													data-bind="text: endBureau"></td>
												<td style="text-align: center;"
													data-bind="text: highlineFlag"></td>
												<td style="text-align: center;"
													data-bind="text: dayGap"></td>
												<td
													style="text-align: center">
													<input type="text" style="width: 25px; text-align: center;"
													data-bind="value: groupGap" />
												</td>


												<td style="text-align: center;"
													data-bind="text: spareFlag"></td>
												<!-- <td style="width:" data-bind="text: spareApplyFlage"></td> -->
												<td style="text-align: center"
													data-bind="text: highlineRule"></td>
												<td style="text-align: center"
													data-bind="text: commonLineRule"></td>
												<!-- <td style="width: 60px; padding: 4px 0px;"
													data-bind="text: otherRule"></td>
												<td
													style="width: 80px; padding: 4px 0px; text-align: center"
													data-bind="text: alertNateTrainNbr"></td> -->
												<td
													style="text-align: center"
													data-bind="text: alertNateTime"></td>
												<!-- <td
													style="width: 75px; padding: 4px 0px; text-align: center"
													data-bind="text: periodSourceTime"></td>
												<td
													style="width: 75px; padding: 4px 0px; text-align: center"
													data-bind="text: periodTargetTime"></td>
												<td style="width: 17px" align="center" class="td_17"></td> -->
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>

						    </div>
						    <div class="tab-pane fade" id="rule">
						    		<div class="panel-body" style="overflow-y:auto; height:320px;">
							<div class="table-responsive">							
								<table class="table table-bordered table-striped table-hover" id="cross_trainInfo">
									<colgroup>
	    									<col width="5%"> 
											<col width="5%"> 
											<col width="7%"> 
											<col width="7%">
											<col width="5%">
											<col width="8%"> 
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="7%">
											<col width="7%">
											<col width="9%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
  										</colgroup>
									<thead>
										<tr>
											<th>序号</th>
											<th>车次</th>
											<th>发站</th>
											<th>发局</th>
											<th>到站</th>
											<th>到局</th>
											<th>线型</th>
											<th>车次间隔</th>
											<th>状态</th> 
											<th>备用套跑</th>
											<th>高铁规律</th>
											<th>普速规律</th>
											<th>特殊规律</th>
											<th>交替车次</th>
											<th>交替时间</th>
											<th>起用日期</th>
											<th>截止日期</th>
										</tr>
									</thead> 
							    </table>
						    	 <div id="train_information" style="overflow-y:auto;"> 
									<table class="table table-bordered table-striped table-hover" style="border-top:0">
									<colgroup>
	    									<col width="5%"> 
											<col width="5%"> 
											<col width="7%"> 
											<col width="7%">
											<col width="5%">
											<col width="8%"> 
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
											<col width="7%">
											<col width="7%">
											<col width="9%">
											<col width="5%">
											<col width="5%">
											<col width="5%">
  										</colgroup>
									<tbody data-bind="foreach: trains"  id="cross_trainInfo1">
										<tr  data-bind="click: $parent.setCurrentTrain, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'rgb(21, 124, 255)':''}">
											<td style="width: 40px" data-bind="text: trainSort"></td>
											<td style="width: 100px" data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
											<td style="width: 115px" data-bind="text: startStn, attr:{title: startStn}"></td>
											<td style="width: 42px;text-align: center;" data-bind="text: startBureau"></td>
											<td style="width: 115px" data-bind="text: endStn, attr:{title: endStn}"></td>
											<td style="width: 42px;text-align: center;" data-bind="text: endBureau"></td>
											<td style="width: 40px" data-bind="text: highlineFlag"></td>
											<td style="width: 100px" >
												<input type="text" class="form-control align" style="width: 45px;margin: 0 auto;height:22px;" data-bind="value: dayGap">
											</td>
											<td style="width: 115px" data-bind="text: spareFlag"></td>
											<td style="width: 42px;text-align: center;" data-bind="text: spareApplyFlage"></td>
											<td style="width: 115px" data-bind="text: highlineRule"></td>
											<td style="width: 42px;text-align: center;" data-bind="text: commonLineRule"></td>
											<td style="width: 40px" data-bind="text: otherRule"></td>
											<td style="width: 100px" data-bind="text: alertNateTrainNbr"></td>
											<td style="width: 115px" data-bind="text: alertNateTime"></td>
											<td style="width: 42px;text-align: center;" data-bind="text: periodSourceTime"></td>
											<td style="width: 115px" data-bind="text: periodTargetTime"></td>
										</tr>
									</tbody>
									</table>		
						    	</div>
							</div>
						</div>
						</div>
					</section>
				</div>
			</div>
		</div>
		<!--交路图-->
		<div id="cross_map_dlg" class="easyui-dialog" title="交路图"
			data-options="iconCls:'icon-save'"
			style="width: 700px; height: 650px; overflow: hidden;">
			<input id="parentParamIsShowJt" type="hidden" value="1"> <input
				id="parentParamIsShowTrainTime" type="hidden" value="0">
			<iframe style="width: 100%; border: 0;" src="/drawCross/provideCrossChartData"></iframe>
		</div>
		<!--时刻表-->
		<div id="train_time_dlg" class="easyui-dialog" title="时刻表"
			data-options="iconCls:'icon-save'"
			style="width: 750px; height: 700px; overflow-y: hidden;">
			<iframe style="width: 100%; border: 0; overflow-y: hidden;" src=""></iframe>
			<!--列车新增和修改-->
			<div id="cross_train_dlg" class="easyui-dialog" title="列车基本信息编辑"
				data-options="iconCls:'icon-save'"
				style="width: 400px; height: 500px; padding: 10px"></div>

			<!--详情时刻表-->
			<div id="cross_train_time_dlg" class="easyui-dialog" title="时刻表"
				data-options="iconCls:'icon-save'"
				style="width: 500px; height: 500px; padding: 10px;">
				<!--panle-heading-->
				<div class="panel-body" style="padding: 10px; margin-right: 10px;">
					<ul class="nav nav-tabs">
						<li class="active"><a style="padding: 3px 10px;"
							href="#simpleTimes" data-toggle="tab">简点</a></li>
						<li><a style="padding: 3px 10px;" href="#allTimes"
							data-toggle="tab">详点</a></li>
						<li style="float: right"><span
							style="font: -webkit-small-control;"
							data-bind="html: currentTrainInfoMessage()"></span></li>
					</ul>
					<!-- Tab panes -->
					<div class="tab-content">
						<div class="tab-pane active" id="simpleTimes">
							<div class="table-responsive">
								<table class="table table-bordered table-striped table-hover"
									id="plan_runline_table_trainLine">
									<thead>
										<tr>
											<th style="width: 5%">序号</th>
											<th style="width: 20%">站名</th>
											<th style="width: 5%">路局</th>
											<th style="width: 15%">到达时间</th>
											<th style="width: 15%">出发时间</th>
											<th style="width: 15%">停时</th>
											<th style="width: 10%">到达天数</th>
											<th style="width: 10%">出发天数</th>
											<th style="width: 15%" colspan="2">股道</th>
										</tr>
									</thead>
									<tbody style="padding: 0">
										<tr style="padding: 0">
											<td colspan="9" style="padding: 0"><div
													id="simpleTimes_table"
													style="height: 350px; overflow-y: auto;">
													<table
														class="table table-bordered table-striped table-hover">
														<tbody data-bind="foreach: simpleTimes">
															<tr data-bind="visible: stationFlag != 'BTZ'">
																<td style="width: 7.5%" align="center"
																	data-bind=" text: $index() + 1"></td>
																<td style="width: 19%"
																	data-bind="text: stnName, attr:{title: stnName}"></td>
																<td style="width: 7.5%" align="center"
																	data-bind="text: bureauShortName"></td>
																<td style="width: 14.3%" align="center"
																	data-bind="text: sourceTime"></td>
																<td style="width: 14.3%" align="center"
																	data-bind="text: targetTime"></td>
																<td style="width: 15%" align="center"
																	data-bind="text: stepStr"></td>
																<td style="width: 10%" align="center"
																	data-bind="text: sourceDay"></td>
																<td style="width: 10%" align="center"
																	data-bind="text: runDays"></td>
																<td style="width: 10%" align="center"
																	data-bind="text: trackName"></td>
															</tr>
														</tbody>
													</table>
												</div></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="tab-pane" id="allTimes">
							<div class="table-responsive">
								<table class="table table-bordered table-striped table-hover"
									id="plan_runline_table_trainLine">
									<thead>
										<tr>
											<th style="width: 5%">序号</th>
											<th style="width: 20%">站名</th>
											<th style="width: 5%">路局</th>
											<th style="width: 15%">到达时间</th>
											<th style="width: 15%">出发时间</th>
											<th style="width: 15%">停时</th>
											<th style="width: 10%">到达天数</th>
											<th style="width: 10%">出发天数</th>
											<th style="width: 15%" colspan="2">股道</th>
										</tr>
									</thead>
									<tbody style="padding: 0">
										<tr style="padding: 0">
											<td colspan="9" style="padding: 0"><div
													id="allTimes_table"
													style="height: 350px; overflow-y: auto;">
													<table
														class="table table-bordered table-striped table-hover">
														<tbody data-bind="foreach: times">
															<tr>
																<td style="width: 7.5%" align="center"
																	data-bind=" text: $index() + 1"></td>
																<td style="width: 19%"
																	data-bind="text: stnName, attr:{title: stnName}"></td>
																<td style="width: 7.5%" align="center"
																	data-bind="text: bureauShortName"></td>
																<td style="width: 14.3%" align="center"
																	data-bind="text: sourceTime"></td>
																<td style="width: 14.3%" align="center"
																	data-bind="text: targetTime"></td>
																<td style="width: 15%" align="center"
																	data-bind="text: stepStr"></td>
																<td style="width: 10%" align="center"
																	data-bind="text: sourceDay"></td>
																<td style="width: 10%" align="center"
																	data-bind="text: runDays"></td>
																<td style="width: 10%" align="center"
																	data-bind="text: trackName"></td>
															</tr>
														</tbody>
													</table>
												</div></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
</body>
<script type="text/html" id="tablefooter-short-template"> 
 <div class="row footer">
     <div class="pull-left" style="line-height:24px;">
  		<span class="pull-left">共<span class="space-pre" data-bind="html: totalCount()"></span>条&nbsp&nbsp当前<span class="space-pre" data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span class="space-pre" data-bind="html: endIndex()"></span>条&nbsp&nbsp共<span class="space-pre" data-bind="text: pageCount()"></span>页</span> 								 
  	 </div>
     <div class="pull-right" style="padding:0px;">   
	   <span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}">
	        <a class="pre-btn" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
		    <input type="text" class="pre-input" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
			<a class="pre-btn after-btn" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"></a>
       </span>  
     </div>
</div>
</script>
</html>
