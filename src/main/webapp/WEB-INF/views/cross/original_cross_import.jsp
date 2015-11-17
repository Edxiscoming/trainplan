
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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/original_cross_import.js"></script>  
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
	<div class="pull-left" style="width: 60%;" id="leftBox"> 

			<div class="row" style="margin: 0px 5px 5px 5px;">  
						<!-- <select id="selectTest" class="easyui-combobox" name="organs"  style="width:200px;" multiple="multiple" valueField="id" textField="text" ></select> -->
								<div class="form-group"
										style="float: left; margin-left: -5px; margin-top: 0px;width: 100%"> 
									   <div class="row" style="width: 100%;height:50px" >

									   		<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
												担当局:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="padding:1px 8px; ;width:80px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code',event:{change: bureauChange}"></select>
											</div>
											 
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: searchModle().currentBureanFlag"
												style="width: 20px; margin-top: 7px; margin-left:14px;display: none;"
												class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left" style="margin-top:5px;margin-right:20px;display: none;">
												本局相关</label> 
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px; margin-left:25px;">
												车次:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="padding:1px 8px; ;width: 220px;"
											 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
											</div> 

											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 27px;" >
												开行状态:</label>
											<div class="control-label pull-left" >
												<div class="pull-left" style="margin-left: 5px;">
											    <select  style="padding:1px 8px; ;width:112px" class="form-control">
											    	<option value ="volvo">开行</option>
													<option value ="saab">备用</option>
													<option value="opel">停运</option>
											    </select>
												</div>
											</div>

											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px; margin-left: 25px;">
												类型:</label>
											<div class="pull-left" style="margin-left: 5px;">
											    <select  style="padding:1px 8px; ;width:112px" class="form-control" data-bind="options: searchModle().highlingFlags, value: searchModle().highlingFlag, optionsText: 'text' , optionsCaption: ''"></select>
											</div>
											<a type="button" class="btn btn-success btn-input" data-toggle="modal" style="margin-left: 40px;"
												data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses">查询</a>  

									        <!-- Jess 新增span -->
									        <!-- <span class="pull-left" style="color:blue;margin-top:5px;margin-right:10px">济局</span>
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
															方案:&nbsp;</label> 
											<div class="pull-left" >
												<select style="padding:1px 8px;color:red ;width: 288px" id="input_cross_chart_id"
													class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
												</select>
											</div> 
											<div class=" btn-group pull-right" style="padding: 8px 15px;line-height: 1.23;border-radius: 6px;" role="group">
										
											  <button type="button" class="btn" style="background-color:white;color:black;border-color:#ccc;font-size: 15px;width:65px;height:30px">全部</button>
											  <button type="button" class="btn" style="background-color:white;color:black;border-color:#ccc;font-size: 15px;width:65px;height:30px">0.既有</button>
											  <button type="button" class="btn" style="background-color:white;color:black;border-color:#ccc;font-size: 15px;width:65px;height:30px">1.高线</button>
											  <button type="button" class="btn" style="background-color:white;color:black;border-color:#ccc;font-size: 15px;width:65px;height:30px">2.混跑</button>
									
									
											</div> -->
									   </div>  
										<div class="row"  style="width: 100%; margin-top: 15px;">
											<div class="btn-group pull-left">
											      <button type="button" style="width:100px; height:40px;" class="btn btn-default" data-bind="click: all_relevant_tokenValue" value="0">全部</button>
											      <button type="button" style="width:100px; height:40px;" class="btn btn-default" data-bind="click: all_relevant_tokenValue" value="1">本局担当</span></button>
											      <button type="button" style="width:100px; height:40px;" class="btn btn-default" data-bind="click: all_relevant_tokenValue" value="2">本局相关</span></button>
											      <button type="button" class="btn btn-danger" style="height:40px;width:100px;margin-right:3px">异常<span style="background-color:white;color:red;border-radius:3px;margin:0 1px">331</span></button>  
						  					</div>

						  					<shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
												<a type="button" class="btn btn-info btn-input pull-right" data-toggle="modal" style="margin-left: 30px;" data-target="#" id="btn_cross_search"  data-bind="click: showUploadDlg">
													导入对数表
												</a> 
											</shiro:hasPermission>
											<div class="pull-right" >
												<select style="padding:1px 8px;color:red ;width: 288px" id="input_cross_chart_id"
													class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
												</select>
											</div> 
											<label for="exampleInputEmail3" class="control-label pull-right" style="margin-top:5px;">
															方案:&nbsp;</label> 
						  					<span class="pull-right" style="color:blue;margin-top:5px;margin-right:10px">济局</span>
										
										
											 <!-- <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 17px;">
												 审核</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="padding:1px 8px; ;width:82px" id="input_cross_sure_flag"
													class="form-control" data-bind="options: searchModle().checkFlags, value: searchModle().checkFlag, optionsText: 'text' , optionsCaption: ''">
												</select>
											</div>
											<a type="button" class="btn btn-success btn-input" data-toggle="modal" style="margin-left: 10px;"
														data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a>  
											<div class="pull-right">
											<button type="button" class="btn btn-primary" style="height:30px;width:100px; margin-right:3px">智能核对</button>
											<button type="button" class="btn btn-primary" style="height:30px;width:100px;margin-right:3px">批量确认</button>
											<button type="button" class="btn btn-danger" style="height:30px;width:100px;margin-right:3px">异常<span style="background-color:white;color:red;border-radius:3px;margin:0 1px">331</span></button>
											</div> -->
										</div>    
 
										
										<!-- <div class="row"  style="margin-top: 5px;">
										    
												 
											
										</div>   -->
									 </div> 
								</div>
								<div class="row" >
								  <div class="panel panel-default"> 
									<div class="table-responsive">
										
										
										<table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%"
												id="cross_table_crossInfo">
												<col width="3%"></col> 
												<col width="3%px"></col> 
												<col width="32%"></col> 
												<col width="21%"></col>
												<col width="5%"></col>
												<col width="5%"></col> 
												<col width="5%"></col>
												<col width="4%"></col>
												<col width="4%"></col>
												<col width="4%"></col>
												<col width="14%"></col>
																					
												
												<thead>
													<tr style="height: 25px">
														<th><input type="checkbox" style="margin-top:0" value="1" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th> 
														<th> </th>
														<th>车底交路</th>
														<th>交替日期</th>
														<th>开行状态</th>
														<th>组数</th>
														<th>对数</th>
														<th>担当局</th>
														<th>数据异常</th>
											
													</tr>
												</thead>
											</table>
															 <div id="crossInfo_Data" style="overflow-y:auto;margin-bottom:5px;margin-left:5px;width:98%"> 
																<table class="table table-bordered table-striped table-hover" style="border-top:0;text-align:center" id="table_left">
																	<col width="3%"></col> 
																	<col width="3%px"></col> 
																	<col width="32%"></col> 
																	<col width="21%"></col>
																	<col width="5%"></col>
																	<col width="5%"></col> 
																	<col width="5%"></col>
																	<col width="4%"></col>
																	<col width="4%"></col>
																	<col width="4%"></col>
																	<col width="14%"></col>
														
																	<!-- <tbody data-bind="foreach: crossRows.rows">
																		<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().crossId == crossId ? 'rgb(21, 124, 255)':''}" >
																		    <td align="center"><input type="checkbox" value="1" data-bind="attr:{class: activeFlag() == 0 ? 'ckbox disabled' : ''}, event:{change: $parent.selectCross}, checked: selected"></td>
																			<td data-bind=" text: $parent.crossRows.currentIndex()+$index()+1 , click: $parent.showTrains"></td>
																			<td data-bind=" text: tokenVehBureauShowValue" align="center"></td>
																			<td data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains , attr:{title: crossName()}"></td>
																			<td data-bind="html: checkFlagStr" align="center"></td>
																			<td data-bind="html: unitCreateFlagStr" align="center"></td>
																			<td class="td_17"></td>
																		</tr> 
																	</tbody>  -->
																	<tbody data-bind="foreach: crossRows.rows">
																		<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().crossId == crossId ? 'rgb(21, 124, 255)':''}" >
																			<td align="center"><input type="checkbox" value="1" data-bind="attr:{class: activeFlag() == 0 ? 'ckbox disabled' : ''}, event:{change: $parent.selectCross}, checked: selected"></td>
																			<td data-bind=" text: $parent.crossRows.currentIndex()+$index()+1 , click: $parent.showTrains"></td>
																			<td data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains , attr:{title: crossName()}"></td>
																			<td data-bind=" text: alterNateDate" align="center"></td>
																			<td data-bind=" text: spareFlag" align="center"></td>
																			<td data-bind=" text: groupTotalNbr" align="center"></td>
																			<td data-bind=" text: pairNbr" align="center"></td>
																			<td data-bind=" text: tokenVehBureauShowValue" align="center"></td>
																			<td align="center"><div style="background-color:#48a048;border-radius:10px; width:30px;">无</div></td>
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
	<div class="pull-right" style="width: 40%;" id="rightBox"> 
	       <div class="panel panel-default" style="margin-right:-41px;"> 
			<div class="row" style="margin: 15px 5px 5px 5px;">
			   <section class="panel panel-default">
			        <div class="panel-heading">
			        	<span>
			              对数表信息
			              <shiro:hasPermission name="JHPT.KYJH.LJ.JJS">   
				              <a  type="button" style="margin-left: 15px;margin-top: -4px" class="btn btn-success pull-right btn-input" data-toggle="modal" data-target="#" id="cross_train_save" data-bind="attr:{class: searchModle().activeCurrentCrossFlag() == 1 ? 'btn btn-success pull-right btn-input' : 'btn btn-success pull-right btn-input disabled'}, click: saveCrossInfo">
				              	保存
				              </a>
			              </shiro:hasPermission>
			              
					   </span>
					</div> 
			          <div class="panel-body">
						<div class="row" >
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
											<input type="text" class="form-control inputColor" style="width: 470px;" data-bind="value: crossName"
												id="plan_construction_input_trainNbr">
										</div> 
								    </div> 
								 	<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 38px;"> 担当局:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;"  data-bind="value: tokenVehBureauShowValue">
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 45px;"> 组数:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: groupTotalNbr" onkeyup="value=value.replace(/[^1-9]/g,'')">
											</div> 
										<!-- <div>
											<input type="text" class="form-control pull-right" style="width:75%;margin:5px" data-bind="value: crossName"
												id="plan_construction_input_trainNbr" disabled>
										</div>
										<label for="exampleInputEmail3"
											class="control-label pull-right" style="margin:auto 0px"> 截断原交路:&nbsp;</label> -->
									</div> 

									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 开行状态:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<select style="width: 150px" class="form-control" data-bind="options: spareFlags, value: spareFlag, optionsText: 'text', optionsValue:'value'"></select>
<!-- 											<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: spareFlag"> -->
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 45px;"> 类型:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
											<select style="width: 150px" class="form-control" data-bind="options: highlingFlags, value: highlineFlag, optionsText: 'text', optionsValue:'value'"></select>
<!-- 												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: highlineFlag"> -->
											</div> 
									</div>

									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 交替日期:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: alterNateDate">
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 19px;"> 交替车次:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: alterNateTranNbr">
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
																			<td class="inputColor" data-bind="text: commonlineRule"> </td>
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
											<input type="text" maxlength="200" class="form-control pull-left inputColor" style="width: 150px;" data-bind="value: crossSection">
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 19px;"> 运行距离:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: runRange">

											</div>  
										</div>

										<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 51px;"> 等级:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;"  data-bind="value: crossLevel">
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 45px;"> 对数:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: pairNbr">
											</div> 
										<!-- <div>
											<input type="text" class="form-control pull-right" style="width:75%;margin:5px" data-bind="value: crossName"
												id="plan_construction_input_trainNbr" disabled>
										</div>
										<label for="exampleInputEmail3"
											class="control-label pull-right" style="margin:auto 0px"> 截断原交路:&nbsp;</label> -->
									</div> 

									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 编组辆数:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: marshallingNums">
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 45px;"> 定员:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control inputColor" style="width: 150px;" data-bind="value: peopleNums">
											</div> 
									</div>

									<div class="row" style="margin: 15px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 25px;"> 编组内容:&nbsp;</label>
										<div class="pull-left"style="margin-left: 26px;">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control inputColor"   style="width: 150px;" data-bind="value: marshallingContent">
										</div>
										<label for="exampleInputEmail3"
												class="control-label pull-left" style="margin-left: 5px;"> 动车组类型:&nbsp;</label>
											<div class="pull-left"style="margin-left: 26px;">
												<input type="text" class="form-control pull-left inputColor" style="width: 150px;" data-bind="value: crhType">
											</div> 

									</div>  

									<div class="row" style="margin: 15px 0 0px 0;">
										<label for="exampleInputEmail5" class="control-label pull-left margin-2" style="margin-left:45px">
											供电：</label> 
										<input type="text" class="pull-left" class="form-control inputColor"
											value="1" data-bind="checked: elecSupply"
											style="width: 60px; margin-top: 5px;text-align:center;border-radius:8px; margin-left:5px;"
											class="form-control">
										<label for="exampleInputEmail5" class="control-label pull-left margin-2" style="margin-left:40px">
											集便：</label>  
										<input type="text" class="pull-left" class="form-control inputColor"
											value="1" data-bind="checked: dejCollect"
											style="width: 60px; margin-left: 5px; margin-top: 5px; text-align:center; border-radius:8px;margin-left:5px;";
											class="form-control">
										<label for="exampleInputEmail5" class="control-label pull-left margin-2" style="margin-left:40px">
											空调：</label>  
										<input type="text" class="pull-left" class="form-control inputColor"
											value="1" data-bind="checked: airCondition"
											style="width: 60px; margin-left: 5px; margin-top: 5px ;text-align:center;border-radius:8px;margin-left:5px;"
											class="form-control"> 
									</div>  

									<!--虚线分隔线-->
								<DIV style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin-top:25px; margin-bottom:25px;"></DIV>
									  <div class="row" style="margin: 15px 0 50px 0;">
									     <label for="exampleInputEmail3"  style="margin-left: 26px;"
												class="control-label pull-left"> 备注:&nbsp;</label>
										<div>
											<textarea type="text" data-bind="value: note" class="form-control inputColor" style="margin-left: 26px;width: 90%;height:100px" ></textarea><!-- data-bind="value: note" -->
										</div> 
									  </div>
						</form>
					    </div>
					  </div>
				   </section>
				</div>
				
				<!-- Jess 注释掉了下面class=row的全部内容 -->
				<!-- 
				<div class="row" style="margin: 15px 10px 10px 10px;">
					<div class="pull-left" style="width: 100%;">
					<section class="panel panel-default" >
				         <div class="panel-heading">
				         
				         <span>
			              <i class="fa fa-table"></i>列车信息   
			                           
										<a  type="button" style="margin-left: 15px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="click: showCrossMapDlg"><i class="fa fa-line-chart"></i>交路图</a>
<!-- 										<a  type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#" -->
<!-- 										id="cross_train_save" data-bind="click: showCrossTrainTimeDlg"><i class="fa fa-clock-o"></i>时刻表</a>  Jess 这里删了一个后面的注释符号
										<a  type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="click: showCrossTrainTimeDlg2"><i class="fa fa-list-ul"></i>时刻表</a>
										<a  type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
										data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'},click: showCrossTrainPeriodDlg"><i class="fa fa-pencil-square-o"></i>调整有效期</a>										
					   </span>
				         </div>
				          <div class="panel-body" style="overflow-y:auto">
							<div class="table-responsive">							
								<table class="table table-bordered table-striped table-hover" id="cross_trainInfo">
									<thead>
										<tr>
											<th style="width: 40px">序号</th>
											<th style="width: 100px">车次</th>
											<th style="width: 115px">发站</th>
											<th style="width: 42px">发局</th>
											<th style="width: 115px">到站</th>
											<th style="width: 42px">到局</th>
											<th style="width: 50px">线型</th>
											<th style="width: 65px">车次间隔</th>
											<th style="width: 50px">状态</th> 
											<th style="width: ">备用套跑</th>
											<th style="width: 65px">高铁规律</th>
											<th style="width: 65px">普速规律</th>
											<th style="width: 65px">特殊规律</th>
											<th style="width: 80px">交替车次</th>
											<th style="width: 85px">交替时间</th>
											<th style="width: 75px">起用日期</th>
											<th style="width: 75px">截止日期</th>
											<th style="width: 17px" align="center"></th>
										</tr>
									</thead> 
							    </table>
						    	 <div id="train_information" style="overflow-y:auto;"> 
									<table class="table table-bordered table-striped table-hover" style="border-top:0">
									<tbody data-bind="foreach: trains"  id="cross_trainInfo1">
										<tr  data-bind="click: $parent.setCurrentTrain, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'rgb(21, 124, 255)':''}">
											<td style="width: 40px" data-bind="text: trainSort"></td>
											<td style="width: 100px" data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
											<td style="width: 115px" data-bind="text: startStn, attr:{title: startStn}"></td>
											<td style="width: 42px;text-align: center;" data-bind="text: startBureau"></td>
											<td style="width: 115px" data-bind="text: endStn, attr:{title: endStn}"></td>
											<td style="width: 42px;text-align: center;" data-bind="text: endBureau"></td>
											<td style="width: 50px;text-align: center;" data-bind="text: highlineFlag"></td>
											<td style="width: 65px;text-align: center;" > <input type="text" class="form-control align" style="width: 45px;margin: 0 auto;height:22px;" data-bind="value: dayGap"/></td>
											<td style="width: 50px;text-align: center;" data-bind="text: spareFlag"></td> 
											<td style="width: " data-bind="text: spareApplyFlage"></td>
											<td style="width:  65px;text-align:center;" data-bind="text: highlineRule"></td>
											<td style="width:  65px;text-align:center;text-align:center;" data-bind="text: commonLineRule"></td>
											<td style="width: 65px" data-bind="text: otherRule"></td>
											<td style="width:  80px" data-bind="text: alertNateTrainNbr"></td>
											<td style="width: 85px;text-align: center;padding:4px 0;" data-bind="text: alertNateTime"></td>
											<td style="width: 75px;text-align: center;" data-bind="text: periodSourceTime"></td>
											<td style="width:75px;text-align: center;" data-bind="text: periodTargetTime"></td>										
											<td style="width:  px;display:none"  data-bind="text: baseCrossTrainId"></td>
											<td style="width: 17px" align="center" class="td_17"></td>
										</tr>
									</tbody>
								</table>
							</div>
							</div>
						</div> 
					</section>
					</div>	 -->		
				</div>
				</div>
			</div>  
	 <!--交路图--> 
	<div id="cross_map_dlg" class="easyui-dialog" title="交路图"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 660px;overflow: hidden;">
			<input id="parentParamIsShowJt" type="hidden" value="1">
			<input id="parentParamIsShowTrainTime" type="hidden" value="0">
		 <iframe style="width: 100%;border: 0;" src=""></iframe>
	</div> 
	<div id="train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 900px; height: 500px;overflow-y: hidden;">
		 <iframe style="width: 100%;border: 0;overflow-y: hidden;" src=""></iframe>
	</div> 
	 <!--列车新增和修改--> 
	<div id="cross_train_dlg" class="easyui-dialog" title="列车基本信息编辑"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 500px; padding: 10px">
	</div>  
	<div id="editStationSort" class="easyui-dialog" title="修改站序"
		data-options="iconCls:'icon-save'"
		style="width: 400px; height: 600px;overflow-y: hidden;">
		<iframe style="width: 400px; height: 490px;border: 0;overflow-y: hidden;" src=""></iframe>
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
			<div class="row" style="width: 100%; margin-top: 5px;display: none;">
				<div class="pull-left">
					<input type="radio" class="pull-left" class="form-control" 
						style="width: 20px; margin-left: 35px;"
						class="form-control" value="1" data-bind="checked: searchModle().upLoadFlag">
				</div>
				<label for="exampleInputEmail5" class="control-label pull-left">
					清空当前方案内全部交路,重新导入</label> 
			</div>
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
	<!-- Jess 注释掉了一个貌似多了的div
	</div>  -->
	<!--详情时刻表--> 
	<div id="cross_train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 500px; height: 500px; padding: 10px; "> 
			      <!--panle-heading-->
			      <div class="panel-body" style="padding:10px;margin-right:10px;">
				       <ul class="nav nav-tabs" >
						  <li class="active"><a style="padding:3px 10px;" href="#simpleTimes" data-toggle="tab">简点</a></li> 
						  <li><a style="padding:3px 10px;" href="#allTimes" data-toggle="tab">详点</a></li> 
						  <li style="float:right" ><span style="font: -webkit-small-control;" data-bind="html: currentTrainInfoMessage()"></span></li>
						</ul> 
						<!-- Tab panes -->
						<div class="tab-content" >
						  <div class="tab-pane active" id="simpleTimes" > 
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead>
							        <tr>
							          <th style="width:5%">序号</th>
					                  <th style="width:20%">站名</th>
					                  <th style="width:5%">路局</th>
					                  <th style="width:15%">到达时间</th>
					                  <th style="width:15%">出发时间</th>
					                  <th style="width:15%">停时</th>
					                  <th style="width:10%">到达天数</th> 
					                  <th style="width:10%">出发天数</th> 
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="simpleTimes_table" style="height: 350px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" >
														 <tbody data-bind="foreach: simpleTimes">
												           <tr data-bind="visible: stationFlag != 'BTZ'">  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:15%" align="center" data-bind="text: stepStr"></td>
															<td style="width:10%" align="center" data-bind="text: sourceDay"></td>
															<td style="width:10%" align="center" data-bind="text: runDays"></td>
															<td style="width:10%" align="center" data-bind="text: trackName"></td>
												        	</tr>
												        </tbody>
													</table> 
											 	</div>
											</td>
										</tr>
									</tbody> 
						        </table>
			        		</div>   
			        	</div>
			        	<div class="tab-pane" id="allTimes" > 
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead>
							        <tr>
							          <th style="width:5%">序号</th>
					                  <th style="width:20%">站名</th>
					                  <th style="width:5%">路局</th>
					                  <th style="width:15%">到达时间</th>
					                  <th style="width:15%">出发时间</th>
					                  <th style="width:15%">停时</th>
					                  <th style="width:10%">到达天数</th> 
					                  <th style="width:10%">出发天数</th> 
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="allTimes_table" style="height: 350px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" > 
														 <tbody data-bind="foreach: times">
												           <tr>  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:15%" align="center" data-bind="text: stepStr"></td>
															<td style="width:10%" align="center" data-bind="text: sourceDay"></td>
															<td style="width:10%" align="center" data-bind="text: runDays"></td>
															<td style="width:10%" align="center" data-bind="text: trackName"></td>
												        	</tr>
												        </tbody>
													</table> 
											 	</div>
											</td>
										</tr>
									</tbody> 
						        </table>
			        		</div>   
			        	</div>
			        </div>
      		</div>
	   </div> 
	   
	   
	   
	   <div id="cross_train_period_dlg" class="easyui-dialog" title="调整有效期"
		data-options="iconCls:'icon-save'"
		style="width: 500px; height: 200px; padding: 10px; "> 
			      <!--panle-heading-->
			      <div class="panel-body" style="padding:10px;margin-right:10px;">
				       <ul class="nav nav-tabs" >
						  <li style="float:right" ><span style="font: -webkit-small-control;" data-bind="html: currentTrainInfoMessage()"></span></li>
						</ul> 
						<!-- Tab panes -->
						<div class="tab-content" >
						  <div class="tab-pane active" id="simpleTimes" > 
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead>
							        <tr>
							          <th style="width:20%">车次</th>
					                  <th style="width:40%">有效期</th>
					                  
					                 </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="simpleTimes_table"> 
													<table class="table table-bordered table-striped table-hover" >
														 <tbody>
												           <tr>  
															<td style="width:20%" align="center" data-bind="text :trainNbr"></td>
															<td style="width:40%" align="center">
																<div class="row">
																	<select class="form-control" 
																	data-bind="options: periodArray, value: periodOption, optionsText: 'text'">	
																	</select>
																</div>
															</td>
												        	</tr>
												        </tbody>
													</table> 
											 	</div>
											</td>
										</tr>
									</tbody> 
						        </table>
						        <div style="margin-top: 30px" align="center">
						        <a  type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
									data-bind="click: savePeriod">保存</a>	
								<a  type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
									data-bind="click: closePeriodDlg">取消</a>
							</div>											
			        		</div>   
			        	</div>
			        </div>
      		</div>
	   </div> 
	   

<div id="checkCorssInfoErrMsg" class="easyui-dialog" title="审核失败信息" data-options="iconCls:'icon-save'" style="width: 800px; height: 600px;overflow: hidden;">
  	<textarea id="testErrMsg" name="txta" style="width: 800px; height: 600px;">
     </textarea>
</div>
<div id="creatCorssUnitErrMsg" class="easyui-dialog" title="生成交路单元警告信息" data-options="iconCls:'icon-save'" style="width: 800px; height: 600px;overflow: hidden;">
  	<iframe style="width: 790px; height: 590px;border: 0;overflow-y: hidden;" src=""></iframe>
</div>
	
	
</body> 



</script> 
 
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
