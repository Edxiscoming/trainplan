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
	src="<%=basePath%>/assets/js/trainplan/cross/crossunit.js"></script>
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

.ckbox.disabled {
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity = 65);
	-webkit-box-shadow: none;
	box-shadow: none;
}

.table td, .table th {
	
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
	style="margin: -8px -8px 0px -19px; width: 100%">
	<!--分栏框开始-->
	<div class="pull-left" style="width: 30%;" id="leftBox">
		<!--分栏框开始-->
		<div class="row" style="margin: 0px 10px 10px 10px;">
			<div class="form-group"
				style="float: left; margin-left: -10px; margin-top: 0px; width: 100%">
				<div class="row" style="width: 100%;">
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-top: 5px;"> 方案:&nbsp;</label>
					<div class="pull-left">
						<select style="padding: 1px 8px;; width: 288px"
							id="input_cross_chart_id" class="form-control"
							data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
						</select>
					</div>
				</div>
				<div class="row" style="width: 100%; margin-top: 5px;">
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-top: 5px;"> 担当局:</label>
					<div class="pull-left" style="margin-left: 5px;">
						<select style="padding: 1px 8px;; width: 106px"
							class="form-control"
							data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code',event:{change: bureauChange}">
						</select>
					</div>
					<input type="checkBox" class="pull-left" class="form-control"
						value="1" data-bind="checked: searchModle().currentBureanFlag"
						style="width: 20px; margin-top: 7px; margin-left: 14px"
						class="form-control"> <label for="exampleInputEmail5"
						class="control-label pull-left" style="margin-top: 5px;">
						本局相关</label>
				</div>
				<div class="row" style="margin-top: 5px;">
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-top: 5px;"> 铁路线型:</label>
					<div class="pull-left" style="margin-left: 5px;">
						<select style="padding: 1px 8px;; width: 92px"
							class="form-control"
							data-bind="options: searchModle().highlingFlags, value: searchModle().highlingFlag, optionsText: 'text' , optionsCaption: ''">
						</select>
					</div>
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-top: 5px; margin-left: 17px;"> 审核状态:</label>
					<div class="pull-left" style="margin-left: 5px;">
						<select style="padding: 1px 8px;; width: 92px"
							id="input_cross_sure_flag" class="form-control"
							data-bind="options: searchModle().checkFlags, value: searchModle().checkFlag, optionsText: 'text' , optionsCaption: ''">
						</select>
					</div>
				</div>
				<div class="row" style="margin-top: 5px;">
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-top: 5px;"> 车次:&nbsp;</label>
					<div class="pull-left">
						<input type="text" class="form-control"
							style="padding: 1px 8px; width: 120px;"
							id="input_cross_filter_trainNbr"
							data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
					</div>
					<a type="button" class="btn btn-success btn-input"
						data-toggle="modal" style="margin-left: 17px;" data-target="#"
						id="btn_cross_search" data-bind="click: loadCrosses"><i
						class="fa fa-search"></i>查询</a>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="panel panel-default" style="margin-top: -7px">
				<div class="table-responsive">
					<div class="form-group" style="margin-left: 5px; margin-top: 10px">
						<shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
							<a type="button" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: checkCrossInfo" data-toggle="modal" data-target="#" id="btn_cross_sure">
								<i class="fa fa-eye"></i>审核
							</a> 
						 	<a type="button" class="btn btn-success" data-toggle="modal" data-target="#" id="btn_cross_delete" style="margin-left: 2px;" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: deleteCrosses">
						 		<i class="fa fa-trash-o"></i>删除
						 	</a>
					 	</shiro:hasPermission>
					</div>
					&nbsp;&nbsp;相关局: <span style="margin-bottom: 5px; margin-left: 5px;"
						data-bind="html: currentCross().relevantBureauShowValue"></span>
					<table class="table table-bordered table-striped table-hover"
						style="margin-left: 5px; margin-right: 5px; width: 98%"
						id="cross_table_crossInfo">
						<thead>
							<tr style="height: 25px">
								<th style="width: 30px;" align="center"><input
									type="checkbox" style="margin-top: 0" value="1"
									data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th>
								<th style="width: 45px" align="center">序号</th>
								<th style="width: 40px" align="center">局</th>
								<th align="center">
									<div style="position: relative;">
										<label for="exampleInputEmail5"
											style="font-weight: bold; vertical-align: bottom;">交路名</label>
										<select class="form-control"
											style="width: 58px; display: inline-block; position: absolute; top: -4px; margin-left: 5px;"
											id="input_cross_filter_showFlag"
											data-bind="options: [{'code': 2, 'text': '全称'},{'code': 1, 'text': '简称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'">
										</select>
									</div>
								</th>
								<th style="width: 45px" align="center">审核</th>
								<th style="width: 17px" align="center"></th>
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
									<td style="width: 30px;" align="center"><input
										type="checkbox" value="1"
										data-bind="attr:{class: activeFlag() == 0 ? 'ckbox disabled' : ''}, event:{change: $parent.selectCross}, checked: selected"></td>
									<td style="width: 45px"
										data-bind=" text: $parent.crossRows.currentIndex()+$index()+1 , click: $parent.showTrains"></td>
									<td style="width: 40px" align="center"
										data-bind=" text: tokenVehBureauShowValue"></td>
									<td
										data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains , attr:{title: crossName()}"></td>
									<td style="width: 45px" align="center"
										data-bind="html: checkFlagStr"></td>
									<td style="width: 17px" align="center" class="td_17"></td>
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
	</div>
	<div class="blankBox"></div>
	<div class="pull-left" style="width: 69%;" id="rightBox">
		<div class="panel panel-default" style="margin-right: -43px;">
			<div class="row" style="margin: 10px 5px 5px 5px;">
				<section class="panel panel-default">
					<div class="panel-heading">
						<span> <i class="fa fa-table"></i>交路信息 
						<shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
							<a type="button" style="margin-left: 5px; margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#" id="save_trainsort" data-bind="click: save_trainsort"> 
								<i class="fa fa-floppy-o"></i>保存
							</a>
						</shiro:hasPermission>
						</span>
					</div>
					<div class="panel-body">
						<div class="row">
							<form class="form-horizontal" role="form"
								data-bind="with: currentCross">
								<div class="row" style="margin: 0px 0 5px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left">
										车底交路名:&nbsp;</label>
									<div class="pull-left" style="margin-left: 26px;">
										<input type="text" class="form-control" style="width: 470px;"
											data-bind="value: crossName"
											id="plan_construction_input_trainNbr" disabled>
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left"
										style="margin-left: 40px"> 铁路线型:</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											style="width: 20px; margin-left: 5px; margin-top: 3px"
											class="form-control" data-bind="checked: highlineFlag"
											value="0" disabled>
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
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
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<label for="exampleInputEmail3" class="control-label pull-left">
										备用套跑交路名:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 470px;"
											data-bind="value: crossSpareName" disabled>
									</div>
									<label for="exampleInputEmail5" style="margin-left: 40px;"
										class="control-label pull-left"> 开行状态:</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											value="1" data-bind="checked: spareFlag"
											style="width: 20px; margin-left: 5px; margin-top: 3px"
											class="form-control" disabled>
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										开行</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											value="2" data-bind="checked: spareFlag"
											style="width: 20px; margin-left: 5px; margin-top: 3px"
											class="form-control" disabled>
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										备用</label>
									<div class="pull-left">
										<input type="radio" class="pull-left" class="form-control"
											value="9" data-bind="checked: spareFlag"
											style="width: 20px; margin-left: 5px; margin-top: 3px"
											class="form-control" disabled>
									</div>
									<label for="exampleInputEmail5" class="control-label pull-left">
										停运</label>
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
									<div class="pull-left" style="width: 16%;">
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left"> 组数:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 40px;"
													data-bind="value: groupTotalNbr" disabled>
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail2"
												class="control-label pull-left">对数:&nbsp;</label> <input
												type="text" class="form-control" style="width: 40px;"
												data-bind="value: pairNbr" disabled>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5"
												class="control-label pull-left"> 开始:&nbsp;</label> <input
												type="text" class="form-control" style="width: 70px;"
												placeholder="" data-bind="value: crossStartDate" disabled>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5"
												class="control-label pull-left"> 结束:&nbsp;</label> <input
												type="text" class="form-control" style="width: 70px;"
												placeholder="" data-bind="value: crossEndDate" disabled>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: cutOld"
												style="width: 20px; margin-top: 5px" class="form-control"
												disabled> <label for="exampleInputEmail5"
												class="control-label pull-left"> 截断原交路</label>
										</div>
									</div>
									<div class="pull-left" style="width: 28%;">
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5"
												class="control-label pull-left"> 普速规律:</label> <input
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
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5"
												class="control-label pull-left"> 高线规律:</label> <input
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
											<label for="exampleInputEmail5"
												class="control-label pull-left"> 指定星期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" disabled
													style="width: 71px;" placeholder=""
													data-bind="value: appointWeek">
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5"
												class="control-label pull-left"> 指定日期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" disabled
													style="width: 140px;" placeholder=""
													data-bind="value: appointDay">
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5"
												class="control-label pull-left"> 指定周期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" disabled
													style="width: 140px;" placeholder=""
													data-bind="value: appointPeriod">
											</div>
										</div>
									</div>
									<div class="pull-left" style="width: 25%;">
										<div class="row" style="margin: 5px 0 0px 0;">
											<label class="control-label pull-left"
												style="margin-left: 32px;"> 担当局:&nbsp;</label>
											<div class="pull-left">
												<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
												<input type="text" class="form-control" disabled
													style="width: 50px;"
													data-bind="value: tokenVehBureauShowValue">
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label class="control-label pull-left">
												车辆/动车段:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" disabled
													style="width: 90px;" data-bind="value: tokenVehDept">
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label class="control-label pull-left"
												style="margin-left: 30px;"> 动车所:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" disabled
													style="width: 90px;" data-bind="value: tokenVehDepot">
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 5px;"
												class="control-label pull-left"> 客运担当局:&nbsp;</label>
											<div class="pull-left">
												<!-- <input type="text" class="form-control" style="width: 30px;" data-bind="value: tokenPsgDept"> -->
												<input type="text" disabled class="form-control disabled"
													style="width: 50px;"
													data-bind="value: tokenPsgBureauShowValue">
												<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenPsgBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select> -->
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 30px;"
												class="control-label pull-left"> 客运段:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" disabled
													style="width: 90px;" data-bind="value: tokenPsgDept">
											</div>
										</div>
									</div>
									<div class="pull-left" style="width: 30%;">
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left"> 运行区段:&nbsp;</label> <input
												type="text" maxlength="200" class="form-control pull-left"
												style="width: 182px;" data-bind="value: crossSection"
												disabled> <label for="exampleInputEmail3"
												class="control-label pull-left"></label>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 13px;">
												经由线:&nbsp;</label> <input type="text" class="form-control"
												style="width: 182px;" data-bind="value: throughline"
												disabled>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left"> 机车类型:&nbsp;</label> <input
												type="text" class="form-control pull-left"
												style="width: 40px;" data-bind="value: locoType" disabled>
											<label for="exampleInputEmail3" style="margin-left: 10px"
												class="control-label pull-left"> 动车组车型:&nbsp;</label> <input
												type="text" class="form-control pull-left"
												style="width: 60px;" data-bind="value: crhType" disabled>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 26px;"
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
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 26px;"
												class="control-label pull-left"> 备注:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control"
													style="width: 182px;" data-bind="value: note" disabled>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</section>
			</div>
			<div class="row" style="margin: 15px 10px 10px 10px;">
				<div class="pull-left" style="width: 100%;">
					<section class="panel panel-default">
						<div class="panel-heading">
							<span> <i class="fa fa-table"></i>列车信息 <a type="button"
								style="margin-left: 15px; margin-top: -5px"
								class="btn btn-success" data-toggle="modal" data-target="#"
								id="cross_train_save"
								data-bind="
											click: showCrossMapDlg"> <i
									class="fa fa-line-chart"></i>交路图
							</a> <a type="button" style="margin-left: 5px; margin-top: -5px"
								class="btn btn-success" data-toggle="modal" data-target="#"
								id="cross_train_save" data-bind="click: showCrossTrainTimeDlg2">
									<i class="fa fa-list-ul"></i>时刻表
							</a>
							</span>






						</div>
						<div class="panel-body" style="overflow-y: auto">
							<div class="table-responsive">
								<table class="table table-bordered table-striped table-hover"
									id="cross_trainInfo">
									<thead>
										<tr>
											<th
												style="width: 35px; padding: 4px 0; vertical-align: middle"
												rowspan="2">组序</th>
											<th
												style="width: 35px; padding: 4px 0; vertical-align: middle"
												rowspan="2">车序</th>
											<th style="width: 90px; vertical-align: middle" rowspan="2">车次</th>
											<th style="width: 115px; vertical-align: middle" rowspan="2">发站</th>
											<th style="width: 42px; vertical-align: middle" rowspan="2">发局</th>
											<th style="width: 115px; vertical-align: middle" rowspan="2">到站</th>
											<th style="width: 42px; vertical-align: middle" rowspan="2">到局</th>
											<th style="width: 42px; vertical-align: middle" rowspan="2">线型</th>
											<th style="width: 60px">组内</br>车次间隔
											</th>
											<th style="width: 42px">组间</br>间隔
											</th>
											<th style="width: 42px; vertical-align: middle" rowspan="2">状态</th>
											<th style="width:">备用</br>套跑
											</th>
											<th
												style="width: 60px; padding: 4px 0; vertical-align: middle"
												rowspan="2">高铁规律</th>
											<th
												style="width: 60px; padding: 4px 0; vertical-align: middle"
												rowspan="2">普速规律</th>
											<th style="width: 60px; padding: 4px 0px;">特殊</br>规律
											</th>
											<th
												style="width: 80px; padding: 4px 0px; vertical-align: middle"
												rowspan="2">交替车次</th>
											<th
												style="width: 80px; padding: 4px 0px; vertical-align: middle"
												rowspan="2">交替时间</th>
											<th
												style="width: 75px; padding: 4px 0px; vertical-align: middle"
												rowspan="2">起用日期</th>
											<th
												style="width: 75px; padding: 4px 0px; vertical-align: middle"
												rowspan="2">截止日期</th>
											<th style="width: 17px" align="center"></th>
										</tr>
									</thead>
								</table>
								<div id="train_information" style="overflow-y: auto;">
									<table class="table table-bordered table-striped table-hover"
										style="border-top: 0">
										<tbody data-bind="foreach: trains" id="fixtrainsort">
											<tr
												data-bind="click: $parent.setCurrentTrain, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'rgb(21, 124, 255)':''}">
												<td style="width: 35px;" data-bind="text: groupSerialNbr"></td>
												<td
													style="width: 35px; padding: 4px 1px; text-align: center">
													<input type="text" style="width: 25px; text-align: center;"
													data-bind="value: trainSort" />
												</td>

												<td style="width: 90px; text-align: center;"
													data-bind="text: trainNbr"></td>
												<td style="width: 115px; text-align: center;"
													data-bind="text: startStn, attr:{title: startStn}"></td>
												<td style="width: 42px; text-align: center;"
													data-bind="text: startBureau"></td>
												<td style="width: 115px; text-align: center;"
													data-bind="text: endStn, attr:{title: endStn}"></td>
												<td style="width: 42px; text-align: center;"
													data-bind="text: endBureau"></td>
												<td style="width: 42px; text-align: center;"
													data-bind="text: highlineFlag"></td>
												<td style="width: 60px; text-align: center;"
													data-bind="text: dayGap"></td>
												<td
													style="width: 42px; padding: 4px 1px; text-align: center">
													<input type="text" style="width: 25px; text-align: center;"
													data-bind="value: groupGap" />
												</td>


												<td style="width: 42px; text-align: center;"
													data-bind="text: spareFlag"></td>
												<td style="width:" data-bind="text: spareApplyFlage"></td>
												<td style="width: 60px; padding: 4px 0; text-align: center"
													data-bind="text: highlineRule"></td>
												<td style="width: 60px; padding: 4px 0; text-align: center"
													data-bind="text: commonLineRule"></td>
												<td style="width: 60px; padding: 4px 0px;"
													data-bind="text: otherRule"></td>
												<td
													style="width: 80px; padding: 4px 0px; text-align: center"
													data-bind="text: alertNateTrainNbr"></td>
												<td
													style="width: 80px; padding: 4px 0px; text-align: center"
													data-bind="text: alertNateTime"></td>
												<td
													style="width: 75px; padding: 4px 0px; text-align: center"
													data-bind="text: periodSourceTime"></td>
												<td
													style="width: 75px; padding: 4px 0px; text-align: center"
													data-bind="text: periodTargetTime"></td>
												<td style="width: 17px" align="center" class="td_17"></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</section>
				</div>
			</div>
		</div>
		</div>
		<!--交路图-->
		<div id="cross_map_dlg" class="easyui-dialog" title="交路图"
			data-options="iconCls:'icon-save'"
			style="width: 700px; height: 650px; overflow: hidden;">
			<input id="parentParamIsShowJt" type="hidden" value="1"> <input
				id="parentParamIsShowTrainTime" type="hidden" value="0">
			<iframe style="width: 100%; border: 0;" src=""></iframe>
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