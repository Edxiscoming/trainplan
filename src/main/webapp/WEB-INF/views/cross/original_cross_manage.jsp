
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
}
 
String basePath = request.getContextPath(); 
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
<title>对数表管理</title>
<!-- Bootstrap core CSS -->
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">
<!--font-awesome-->

<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">

<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" />
 
<!-- Custom styles for this template --> 
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet"> 
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-ui-1.10.4.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.ui.widget.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/resizable.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/original_cross_add.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/original_cross_manage.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/validate.js"></script>

<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript">
var basePath = "<%=basePath %>";
var all_role = "<%=userRolesString %>"; 
var currentBureauShortName = "<%=currentBureauShortName %>";
var currentUserBureau = "<%=currentUserBureau %>";
</script>

<!--#include virtual="assets/js/trainplan/knockout.pagefooter.tpl"-->
 <style type="text/css">
.pagination > li > a, .pagination > li > span {
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
.dropdown-menu.datepicker { max-width:220px; z-index: 10000 }

.ckbox.disabled{
	pointer-events: none;
	cursor: not-allowed;
	opacity: 0.4;
	filter: alpha(opacity=4);
	-webkit-box-shadow: none;
	box-shadow: none;
}
button:active{
background-color:#333;
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

.btn-info {
  color: #fff;
  background-color: #5bc0de;
  border-color: #46b8da;
}
.btn-info:hover,
.btn-info:focus,
.btn-info:active,
.btn-info.active,
.open .dropdown-toggle.btn-info {
  color: #fff;
  background-color: #39b3d7;
  border-color: #269abc;
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
  background-color: #5bc0de;
  border-color: #46b8da;
}
.btn-info .badge {
  color: #5bc0de;
  background-color: #fff;
}
.inputColor {
	color: #27bdaf;
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

.btn-success {
	color: #ffffff;
	background-color: #27bdaf;
	border-color: #27bdaf;
}
.btn-success:hover, .btn-success:focus, .btn-success:active, .btn-success.active, .open .dropdown-toggle.btn-success {
	color: #ffffff;
	background-color: #27bdaf;
	border-color: #27bdaf;
}
.btn-success:active, .btn-success.active, .open .dropdown-toggle.btn-success {
	background-image: none;
}
.btn-success.disabled, .btn-success[disabled], fieldset[disabled] .btn-success, .btn-success.disabled:hover, .btn-success[disabled]:hover, fieldset[disabled] .btn-success:hover, .btn-success.disabled:focus, .btn-success[disabled]:focus, fieldset[disabled] .btn-success:focus, .btn-success.disabled:active, .btn-success[disabled]:active, fieldset[disabled] .btn-success:active, .btn-success.disabled.active, .btn-success[disabled].active, fieldset[disabled] .btn-success.active {
	background-color: #27bdaf;
	border-color: #27bdaf;
}

</style>
<script type="text/javascript">
	$(function() {
	   resize("leftBox","rightBox","490","540");
	 });
</script>
 
</head>
<body class="Iframe_body" style="margin:-8px -8px 0px -19px;width:100%" >
    <!--Jess-把宽度由30%改为65%  -->
	<div class="pull-left" style="width: 65%;" id="leftBox"> 

			<div class="row">  
						<!-- <select id="selectTest" class="easyui-combobox" name="organs"  style="width:200px;" multiple="multiple" valueField="id" textField="text" ></select> -->
								<div class="form-group"
										style="float: left; margin-top: 0px;width: 100%"> 
									   <div style="width: 100%; height: 50px;" class="row">
									        <!-- Jess 新增span -->
											<label style="margin: 15px 10px 15px 0px;" class="control-label pull-left">
															方案:&nbsp;</label> 
											<div style="margin:10px 5px" class="pull-left">
												<select style="padding:1px 8px;color:red ;width: 288px" id="input_cross_chart_id"
													class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
												</select>
											</div>
											
											<div style="margin:10px 15px" class="pull-left">
												<!-- <label style="margin-top:5px;" class="control-label pull-left" for="exampleInputEmail3">
												担当局:</label>
												<div style="margin-left: 5px;" class="pull-left">
													<select style="padding:1px 8px; ;width:80px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code',event:{change: bureauChange}"></select>
												</div> -->
												 
												<input type="checkBox" id="exampleInputEmail5" style="width: 20px; margin-top: 7px; margin-left:14px;display: none;" data-bind="checked: searchModle().currentBureanFlag" value="1" class="pull-left"> 
												<label style="margin-top:5px;margin-right:20px;display: none;" class="control-label pull-left" for="exampleInputEmail5">
													本局相关</label> 
												<!-- <label class="control-label pull-left" style="margin-top:5px;">车次:&nbsp;</label>
												<div class="pull-left">
													<input type="text" class="form-control" style="padding:1px 8px; ;width: 120px;"
											 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
												</div> -->
												
												<!-- 0:全部 1:本局担当 2:本局相关 -->
												
												<label class="control-label pull-left" style="margin: 5px 10px 5px 15px;">
															类型:&nbsp;</label>
												<div class="pull-left">
													<select style="padding:1px 8px;width: 70px" class="form-control">
														<option value="-1">全部</option>
														<option value="0">既有</option>
														<option value="1">高线</option>
														<option value="2">混跑</option>
													</select>
												</div> 	
											</div>
											<div class="pull-left"><button class="btn btn-primary" style="height:30px;width:50px; margin-left:30px;margin-top:7px;background:#27bdaf" data-toggle="modal"
														 id="btn_cross_search"  data-bind="click: loadCrosses">查询</button></div>
									   </div>
										<div class="row"  style="width: 100%; margin-top: 5px;">
											<div class="btn-group pull-left">
											      <button type="button" style="width:100px; height:40px;" class="btn btn-default" data-bind="click: all_relevant_tokenValue" value="0">全部</button>
											      <button type="button" style="width:100px; height:40px;" class="btn btn-default" data-bind="click: all_relevant_tokenValue" value="1">本局担当</span></button>
											      <button type="button" style="width:100px; height:40px;" class="btn btn-default" data-bind="click: all_relevant_tokenValue" value="2">本局相关</span></button>
<!-- 											      <button type="button" class="btn btn-danger" style="height:40px;width:100px;margin-right:3px">异常<span style="background-color:white;color:red;border-radius:3px;margin:0 1px">331</span></button>   -->
						  					</div>
											
											<div class="pull-right">
											<shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
												<a type="button" class="btn btn-primary" data-toggle="modal" style="height:30px;width:70px; margin-right:3px;background:#27bdaf; padding-top:6px;" data-target="#" id="btn_cross_search"  data-bind="click: showUploadDlg">
													导入
												</a> 
											</shiro:hasPermission>
											<button type="button" class="btn btn-primary" style="height:30px;width:70px; margin-right:3px;background:#27bdaf" onclick="$('#c_dlg').dialog('open');"><i class="fa fa-plus"></i>新增</button>
											<button data-toggle="modal" data-target="#" id="btn_cross_delete" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: deleteCrosses" type="button" class="btn btn-warning" style="height:30px;width:60px;margin-right:3px">删除</button>
											<button type="button" class="btn btn-primary" style="height:30px;width:60px;margin-right:3px">发消息</button>
											<button type="button" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: checkCrossInfo"  data-toggle="modal"
													data-target="#" id="btn_cross_sure" style="height:30px;width:60px;margin-right:3px;background:#27bdaf">审核</button>
											<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#" id="btn_cross_createCrossUnit" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: createUnitCrossInfo" style="height:30px;width:70px;margin-right:3px">生成交路</button>
											</div>
										</div>    
										<div class="row"  style="margin-top: 5px;">
										</div>  
									 </div> 
								</div>
								<div class="row" >
								  <div class="panel panel-default"> 
									<div class="table-responsive">
										<table class="table table-bordered table-striped table-hover" style="width:100%"
												id="cross_table_crossInfo">
											<col width="35px"></col> 
											<col width="300px"></col> 
											<col width="200px"></col>
											<col width="50px"></col>
											<col width="50px"></col> 
											<col width="50px"></col>
											<col width="50px"></col>
											<col width="50px"></col>
											<col width="50px"></col>
											<col width="50px"></col>
											<thead>
												<tr style="height: 25px"> 
													<th><input type="checkbox" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}" value="1" style="margin-top: 0"></th>
													<th>车底交路</th>
													<th>交替日期</th>
													<th>开行状态</th>
													<th>组数</th>
													<th>对数</th>
													<th>担当局</th>
													<th>数据异常</th>
													<th>审核状态</th>
													<th>生成状态</th> 
										
												</tr>
											</thead>
										</table>
										 <div id="crossInfo_Data" style="overflow-y:auto;width:100%"> 
											<table class="table table-bordered table-striped table-hover" style="border-top:0;text-align:center" id="table_left">
												<col width="35px"></col> 
												<col width="300px"></col> 
												<col width="200px"></col>
												<col width="50px"></col>
												<col width="50px"></col> 
												<col width="50px"></col>
												<col width="50px"></col>
												<col width="50px"></col>
												<col width="50px"></col>
												<col width="50px"></col>
												<tbody data-bind="foreach: crossRows.rows">
													<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().crossId == crossId ? 'rgb(21, 124, 255)':''}" >
														<td><input type="checkbox" data-bind="attr:{class: activeFlag() == 0 ? 'ckbox disabled' : ''}, event:{change: $parent.selectCross}, checked: selected" value="1"></td>
														<td data-bind=" text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains , attr:{title: crossName()}" align="center"></td>														
														<td data-bind=" text: alterNateDate" align="center"></td>
														<td data-bind=" text: spareFlag" align="center"></td>
														<td data-bind=" text: groupTotalNbr" align="center"></td>
														<td data-bind=" text: pairNbr" align="center"></td>
														<td data-bind=" text: tokenVehBureauShowValue" align="center"></td>
														<td><div style="background-color:#48a048;border-radius:10px">无</div></td>
														<td><div data-bind="html: checkFlagStr" style="border-radius:10px"/></td>
														<td><div data-bind="html: createCrossFlagStr" style="border-radius:10px">未</div></td>
													</tr> 
												</tbody>
											</table>
											
									 	</div>			 
											
										<div data-bind="template: { name: 'tablefooter-short-template', foreach: crossRows }" style="margin: 10px 0;"></div>
									</div>
								</div>
							</div>  
		</div> 
		<div class="blankBox"></div>
	<!-- Jess 把宽度从69%改为34% -->
	<div class="pull-right" style="width: 34%; height:800px;overflow-y:scroll;overflow-x: hidden;" id="rightBox"> 
		<div class="row" style="margin: 10px 5px 5px 5px;">
			<section class="panel panel-default">
				<div class="panel-heading" style="height: 35px">
		   			<span> 对数表信息</span>
					 <a  type="button" style="margin-left: 15px;margin-top: -4px" class="btn btn-success pull-right btn-input" data-toggle="modal" data-target="#" id="cross_train_save" data-bind="attr:{class: searchModle().activeCurrentCrossFlag() == 1 ? 'btn btn-success pull-right btn-input' : 'btn btn-success pull-right btn-input disabled'}, click: saveCrossInfo">
				              	保存
				              </a>
				</div>
		        <div class="panel-body">
			    	<div class="row">
						<form class="form-horizontal" role="form" data-bind="with: currentCross"> 
						    <div style="padding-left:40px;border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
						    	<div class="form-group"> 
									<label class="control-label pull-left">
										车底交路：
									</label>
									<div class="pull-left">
											<input type="text" class="form-control inputColor" style="width: 470px;" data-bind="value: crossName"
												id="plan_construction_input_trainNbr">
									</div>
								</div>
								
								<div class="form-group"> 
									<label style="padding-left:13px;" class="control-label pull-left">
										担当局：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control inputColor"   style="width: 150px;"  data-bind="value: tokenVehBureauShowValue">
									</div>
									
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										组数：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: groupTotalNbr" onkeyup="value=value.replace(/[^1-9]/g,'')">
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										开行状态：
									</label>
									<div class="pull-left">
<!-- 										<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: spareFlag"> -->
									<select style="width: 150px" class="form-control" data-bind="options: spareFlags, value: spareFlag, optionsText: 'text', optionsValue:'value'"></select>
									</div>
									
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										类型：
									</label>
									<div class="pull-left">
									<select style="width: 150px" class="form-control" data-bind="options: highlingFlags, value: highlineFlag, optionsText: 'text', optionsValue:'value'"></select>
<!-- 										<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: highlineFlag"> -->
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										交替日期：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: alterNateDate">
									</div>
									
								</div>
								<div class="form-group">
									<label class="control-label pull-left">
										交替车次：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: alterNateTranNbr">
									</div>
								</div>
							</div>
							<div  style="padding:10px">
								<span style="margin:5px">开行规律：</span>
								<table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%"
								id="cross_table_crossInfo">
									<col width="25%"></col>
									<col width="25%"></col>
									<col width="25%"></col>
									<col width="25%"></col>
									<thead>
										<tr style="height: 25px"> 
<!-- 											<th>高线</th> -->
											<th>普通规律</th>
											<th>指定星期</th>
											<th>指定日期</th>
											<th>指定周期</th>										
										</tr>
									</thead>
								</table>
								<div id="crossInfo_Data" style="overflow-y:auto;margin-bottom:5px;margin-left:5px;width:98%"> 
									<table class="table table-bordered table-striped table-hover" style="border-top:0;text-align:center" id="table_left">
<!-- 										<col width="25%"></col>  -->
										<col width="25%"></col> 
										<col width="25%"></col> 
										<col width="25%"></col> 
										<col width="25%"></col> 
										<tbody>
											<tr>
<!-- 												<td class="inputColor" data-bind="text: highlineRule"> </td> -->
												<td class="inputColor" data-bind="text: commonlineRule"> </td>
												<td class="inputColor" data-bind="text: appointWeek"></td>
												<td class="inputColor" data-bind="text: appointDay"></td>
												<td class="inputColor" data-bind="text: appointPeriod"> </td>
											</tr> 
										</tbody>
									</table>
						   		</div>
					      	</div>
							<div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 20px 40px;">
								<div class="form-group"> 
									<label class="control-label pull-left">
										运行区段：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 170px;" data-bind="value: crossSection">
									</div>
									
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										运行距离：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 170px;"  data-bind="value: runRange">
									</div>
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										等级：
									</label>
									<div class="pull-left">
										<input type="text" style="width: 170px;" class="form-control" data-bind="value: crossLevel">
									</div>
									
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										对数：
									</label>
									<div class="pull-left">
										<input type="text" style="width: 170px;" class="form-control" data-bind="value: pairNbr">
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										编组辆数：
									</label>
									<div class="pull-left">
										<input type="text" style="width: 170px;" class="form-control" data-bind="value: marshallingNums">
									</div>
									
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										定员：
									</label>
									<div class="pull-left">
										<input type="text" style="width: 170px;" class="form-control" data-bind="value: peopleNums">
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										编组内容：
									</label>
									<div class="pull-left">
										<input type="text" style="width: 170px;" class="form-control" data-bind="value: marshallingContent">
									</div>
									
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left" style="margin-left:-13px;">
										动车组类型：
									</label>
									<div class="pull-left">
										<input type="text" style="width: 170px;" class="form-control" data-bind="value: crhType">
									</div>
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										供电：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 60px;" data-bind="value: elecSupply">
									</div>
									
								</div>
								<div class="form-group">
									<label class="control-label pull-left" style="padding-left:23px;">
										集便：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 60px;" data-bind="value: dejCollect">
									</div>
									
								</div>
								<div class="form-group">
									<label class="control-label pull-left" style="padding-left:23px;">
										空调：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 60px;" data-bind="value: airCondition">
									</div>
								</div>
						    </div>
						    <div style="padding-top:10px; padding-left:15px;"> 
									<label style="width:500px; padding-bottom:5px;" >
										备注：
									</label>
									<div class="row pull-left">
										<textarea data-bind="value: note" style="width: 535px;" rows="4" class="form-control color-27bdaf"></textarea>
									</div>
							</div>
						</form>
		  			</div>
				</div>
			</section>
		</div>
	</div>

	 <!--导入弹窗--> 
	<div id="file_upload_dlg" class="easyui-dialog" title="导入对数表文件"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 280px; padding: 10px">
		<img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;"> 
		<form id="file_upload_id" name="file_upload_name" action="/cross/fileUpload" method="post" enctype="multipart/form-data"> 
			<div class="row" style="width: 100%;">
				<label for="exampleInputEmail5" class="control-label pull-left">
					导入类型:</label> 
			</div>
			<div class="row" style="width: 100%; margin-top: 5px;">
				<div class="pull-left">
					<input type="radio" class="pull-left" class="form-control" 
						style="width: 20px; margin-left: 35px;"
						class="form-control" data-bind="checked: searchModle().upLoadFlag" value="0">
				</div> 
				<label for="exampleInputEmail5" class="control-label pull-left">
					覆盖同名交路，其他追加</label> 
			</div>
			<!-- <div class="row" style="width: 100%; margin-top: 5px;">
				<div class="pull-left">
					<input type="radio" class="pull-left" class="form-control" 
						style="width: 20px; margin-left: 35px;"
						class="form-control" value="1" data-bind="checked: searchModle().upLoadFlag">
				</div>
				<label for="exampleInputEmail5" class="control-label pull-left">
					清空当前方案内全部交路,重新导入</label> 
			</div> -->
			<!-- 
			<div class="row" style="width: 100%; margin-top: 5px;">
				<div class="pull-left">
					<input type="radio" class="pull-left" class="form-control" 
						style="width: 20px; margin-left: 35px;"
						class="form-control" value="2" data-bind="checked: searchModle().upLoadFlag" >
				</div>
				<label for="exampleInputEmail5" class="control-label pull-left">
					不覆盖,追加</label> 
			</div>
			 -->
			<div class="row" style="width: 100%; margin-top: 5px;">
					<label for="exampleInputEmail3" class="control-label pull-left">
									方案:&nbsp;</label> 
					<div class="pull-left">
						<select style="width: 273px" id="input_cross_chart_id"
							class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name', optionsCaption: ''">
						</select>
					</div>   
			 </div> 
			 <div class="row" style="margin: 10px 0 5px 0;">
				<label for="exampleInputEmail2" class="control-label pull-left">启用日期:&nbsp;</label>
		        <div class="pull-left">
		           <input class="form-control" id="cross_start_day" style="width:150px;" placeholder="" data-bind="value: searchModle().startDay">
		        </div> 
		   </div>
			<div class="row" style="width: 100%; margin-top: 10px;">
				<input id="fileToUpload" type="file" size="45" name="fileToUpload"  name="fileName" accept=".xls,.xlsx"/>
			</div>
			<div  class="row" style="width: 100%; margin-top: 10px;">
			     <a type="button" id="btn_fileToUpload"
					class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: uploadCrossFile">导入</a>
			</div>
		</form>
	</div>
	<!-- 新增对数行 -->
	<div id="c_dlg" class="easyui-dialog" title="新增对数行"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 700px;overflow: hidden;">
		<section class="panel panel-default" data-bind="with: addModle">
				<div class="panel-heading" style="height: 35px">
		   			<span> 对数表信息</span>
					<button style="height:25px;width: 50px;  margin-right:5px;background:#27bdaf " class="btn btn-primary pull-right" data-bind="click: addSaveCrossInfo" type="button">保存</button>
				</div>
		        <div class="panel-body">
			    	<div class="row">
			    		<div class="form-group" style="height:30px;margin-left:45px;">
				    		<label class="control-label pull-left">方案：</label> 
							<div class="pull-left">
								<select style="color:red ;width: 288px" id="input_cross_chart_id" class="form-control" data-bind="options: charts, value: chart, optionsText: 'name'">
								</select>
							</div>
				    	</div>
						<form class="form-horizontal" role="form" data-bind="with: entityModel"> 
						    <div style="padding-left:30px;border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
						    	<div class="form-group"> 
									<label class="control-label pull-left">
										车底交路：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="width: 440px;" data-bind="value: crossName">
									</div>
								</div>
								
								<div class="form-group"> 
									<label style="padding-left:13px;" class="control-label pull-left">
										担当局：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: tokenVehBureau" style="width: 170px;" class="form-control">
									</div>
									<label style="padding-left:60px;" class="control-label pull-left">
										组数：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: groupTotalNbr" style="width: 170px;" class="form-control">
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										开行状态：
									</label>
									<div class="pull-left">
										<select style="width: 170px;"  class="form-control" data-bind="options: spareFlags, value: spareFlag, optionsText: 'text', optionsValue:'value'">
										</select>
									</div>
									<label style="padding-left:60px;" class="control-label pull-left">
										类型：
									</label>
									<div class="pull-left">
										<select style="width: 170px;"  class="form-control" data-bind="options: highlineFlags, value: highlineFlag, optionsText: 'text', optionsValue:'value'">
										</select>
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										交替日期：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: alterNateDate" style="width: 170px;" class="form-control">
									</div>
									<label style="padding-left:34px;" class="control-label pull-left">
										交替车次：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: alterNateTranNbr" style="width: 170px;" class="form-control">
									</div>
								</div>
							</div>
							<div  style="padding:10px">
								<span style="margin:5px">开行规律：</span>
								<table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%"
								id="cross_table_crossInfo">
									<col width="20%"></col> 
									<col width="20%"></col>
									<col width="20%"></col>
									<col width="20%"></col>
									<col width="20%"></col>
									<thead>
										<tr style="height: 25px"> 
											<th>普线规律</th>
											<th>高线规律</th>
											<th>指定星期</th>
											<th>指定日期</th>
											<th>指定周期</th>										
										</tr>
									</thead>
								</table>
								<div id="crossInfo_Data" style="overflow-y:auto;margin-bottom:5px;margin-left:5px;width:98%"> 
									<table class="table table-bordered table-striped table-hover" style="border-top:0;text-align:center" id="table_left">
										<col width="20%"></col> 
										<col width="20%"></col> 
										<col width="20%"></col> 
										<col width="20%"></col> 
										<col width="20%"></col> 
										<col width="20%"></col> 
										<tbody>
											<tr>
												<td><input type="text" class="form-control" data-bind="value: commonlineRule" sytle="width:70px;"> </td>
												<td><input type="text" class="form-control" data-bind="value: highlineRule" sytle="width:70px;"> </td>
												<td><input type="text" class="form-control" data-bind="value: appointWeek" sytle="width:70px;"> </td>
												<td><input type="text" class="form-control" data-bind="value: appointDay" sytle="width:70px;"></td>
												<td><input type="text" class="form-control" data-bind="value: appointPeriod" sytle="width:70px;"></td>
											</tr> 
										</tbody>
									</table>
						   		</div>
					      	</div>
							<div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 10px 30px;">
								<div class="form-group"> 
									<label class="control-label pull-left">
										运行区段：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 170px;" data-bind="value: crossSection">
									</div>
									<label class="control-label pull-left" style="padding-left:50px;">
										运行距离：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 170px;" data-bind="value: runRange">
									</div>
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										等级：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: crossLevel" style="width: 170px;" class="form-control">
									</div>
									<label style="padding-left:76px;" class="control-label pull-left">
										对数：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: pairNbr" style="width: 170px;" class="form-control">
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										编组辆数：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: marshallingNums" style="width: 170px;" class="form-control">
									</div>
									<label style="padding-left:75px;" class="control-label pull-left">
										定员：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: peopleNums" style="width: 170px;" class="form-control">
									</div>
								</div>
								<div class="form-group"> 
									<label class="control-label pull-left">
										编组内容：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: marshallingContent" style="width: 170px;" class="form-control">
									</div>
									<label style="padding-left:36px;" class="control-label pull-left">
										动车组类型：
									</label>
									<div class="pull-left">
										<input type="text" data-bind="value: crhType" style="width: 170px;" class="form-control">
									</div>
								</div>
								<div class="form-group"> 
									<label style="padding-left:25px;" class="control-label pull-left">
										供电：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 60px;" data-bind="value: elecSupply">
									</div>
									<label class="control-label pull-left" style="padding-left:23px;">
										集便：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 60px;" data-bind="value: dejCollect">
									</div>
									<label class="control-label pull-left" style="padding-left:23px;">
										空调：
									</label>
									<div class="pull-left">
										<input type="text" class="form-control color-27bdaf" style="width: 60px;" data-bind="value: airCondition">
									</div>
								</div>
						    </div>
						    <div style="padding-top:10px; padding-left:15px;"> 
									<label style="width:500px; padding-bottom:5px;" >
										备注：
									</label>
									<div class="row pull-left">
										<textarea data-bind="value: note" style="width: 535px;" rows="4" class="form-control color-27bdaf"></textarea>
									</div>
							</div>
						</form>
		  			</div>
				</div>
			</section>
			
	</div> 


	<div id="checkCorssInfoErrMsg" class="easyui-dialog" title="审核失败信息" data-options="iconCls:'icon-save'" style="width: 800px; height: 600px;overflow: hidden;">
  	<textarea id="testErrMsg" name="txta" style="width: 800px; height: 600px;">
     </textarea>
</div>
<div id="creatCorssUnitErrMsg" class="easyui-dialog" title="生成交路单元警告信息" data-options="iconCls:'icon-save'" style="width: 800px; height: 600px;overflow: hidden;">
  	<iframe style="width: 790px; height: 590px;border: 0;overflow-y: hidden;" src=""></iframe>
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
