
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
<title>客运处对数表管理</title>
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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/cross_manage_passengerServiceExcel.js"></script>  
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
</style>
<script type="text/javascript">
	$(function() {
	   resize("leftBox","rightBox","490","540");
	 });
</script>
 
</head>
<body class="Iframe_body" style="margin:-8px -8px 0px -19px;width:100%" >
	<div class="pull-left" style="width: 20%;" id="leftBox"> 
			<div class="row" style="margin: 0px 5px 5px 5px;">  
						<!-- <select id="selectTest" class="easyui-combobox" name="organs"  style="width:200px;" multiple="multiple" valueField="id" textField="text" ></select> -->
								<div class="form-group"
										style="float: left; margin-left: -5px; margin-top: 0px;width: 100%"> 
									  <div class="row" style="width: 100%;" >
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
															方案:&nbsp;</label> 
											<div class="pull-left">
												<select style="padding:1px 8px; ;width: 288px" id="input_cross_chart_id"
													class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
												</select>
											</div> 
									   </div>  
										<div class="row"  style="width: 100%; margin-top: 5px;">
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
												担当局:</label>
											<div class="pull-left" style="margin-left: 5px;">
												<select style="padding:1px 8px; ;width:106px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code',event:{change: bureauChange}"></select>
											</div>
											 
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: searchModle().currentBureanFlag"
												style="width: 20px; margin-top: 7px; margin-left:14px"
												class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left" style="margin-top:5px;">
												本局相关</label> 
										</div>    
										<div class="row"  style="margin-top: 5px;">
											<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
												铁路线型:</label>
											<div class="pull-left" style="margin-left: 5px;">
											    <select  style="padding:1px 8px; ;width:92px" class="form-control" data-bind="options: searchModle().highlingFlags, value: searchModle().highlingFlag, optionsText: 'text' , optionsCaption: ''"></select>
											</div>
											
										</div>  
										
										<div class="row"  style="margin-top: 5px;">
										    <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
												车次:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="padding:1px 8px; ;width: 120px;"
											 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
											</div> 
										</div>  
										
										
										<hr/>
										<div class="row"  style="margin-top: 5px;">
											<shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
												<a type="button" class="btn btn-success btn-input" data-toggle="modal" style="margin-left: 30px;" data-target="#" id="btn_cross_search"  data-bind="click: showUploadDlg">
													<i class="fa fa-sign-in"></i>导入
												</a> 
											</shiro:hasPermission>

												<a type="button" class="btn btn-success btn-input" data-toggle="modal" style="margin-left: 30px;"
														data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a>   
										</div>  
									 </div> 
								</div>
			<%-- 					<div class="row" >
								  <div class="panel panel-default"> 
									<div class="table-responsive">
										<div class="form-group"style="margin-left: 5px; margin-top:10px">
											<shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
												<a type="button" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: checkCrossInfo"  data-toggle="modal"
													data-target="#" id="btn_cross_sure"><i class="fa fa-eye"></i>审核</a>
												<a  type="button" class="btn btn-success" data-toggle="modal"
													data-target="#" id="btn_cross_delete" style="margin-left: 2px;" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: deleteCrosses"><i class="fa fa-trash-o"></i>删除</a>
												<a  type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 2px;" 
													data-target="#" id="btn_cross_createCrossUnit" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: createUnitCrossInfo"><i class="fa fa-external-link"></i>生成交路单元</a>
											</shiro:hasPermission>
										</div> 
										<span style="margin-bottom:5px;margin-left:5px;" data-bind="html: currentCross().relevantBureauShowValue"></span> 
										<table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%"
												id="cross_table_crossInfo">
												<col width="35px"></col> 
												<col width="45px"></col> 
												<col width="40px"></col>
												<col></col>
												<col width="50px"></col> 
												<col width="50px"></col>
												<col width="17px"></col> 											
												
												<thead>
													<tr style="height: 25px"> 
														<th><input type="checkbox" style="margin-top:0" value="1" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th>
														<th>序号</th>
														<th>局</th>
											              <th align="center">  
											              <div  style="position: relative;">
															<label for="exampleInputEmail5" style="font-weight: bold;vertical-align: bottom;">交路名</label> 
															<select class="form-control" style="width: 58px;display:inline-block;position: absolute;top:-4px;margin-left: 5px;" id="input_cross_filter_showFlag" data-bind="options: [{'code': 2, 'text': '全称'},{'code': 1, 'text': '简称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'">
															</select>
														  </div>  
														  </th>
														<th>审核</th>
														<th>生成</th> 
														<th></th>
													</tr>
												</thead>
											</table>
															 <div id="crossInfo_Data" style="overflow-y:auto;margin-bottom:5px;margin-left:5px;width:98%"> 
																<table class="table table-bordered table-striped table-hover" style="border-top:0" id="table_left">
																	<col width="35px"></col> 
																	<col width="45px"></col> 
																	<col width="40px"></col>
																	<col></col>
																	<col width="50px"></col> 
																	<col width="50px"></col> 
																	<col width="17px"></col> 
																	<tbody data-bind="foreach: crossRows.rows">
																		<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().crossId == crossId ? 'rgb(21, 124, 255)':''}" >
																		    <td align="center"><input type="checkbox" value="1" data-bind="attr:{class: activeFlag() == 0 ? 'ckbox disabled' : ''}, event:{change: $parent.selectCross}, checked: selected"></td>
																			<td data-bind=" text: $parent.crossRows.currentIndex()+$index()+1 , click: $parent.showTrains"></td>
																			<td data-bind=" text: tokenVehBureauShowValue" align="center"></td>
																			<td data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, click: $parent.showTrains , attr:{title: crossName()}"></td>
																			<td data-bind="html: checkFlagStr" align="center"></td>
																			<td data-bind="html: unitCreateFlagStr" align="center"></td>
																			<td class="td_17"></td>
																		</tr> 
																	</tbody> 
																</table> 
														 	</div>			 
											
										<div data-bind="template: { name: 'tablefooter-short-template', foreach: crossRows }" style="margin: 10px 0;"></div>
									</div>
								</div>
							</div>  --%> 
		</div> 
		<div class="blankBox"></div>
	<div class="pull-right" style="width: 80%;" id="rightBox"> 
	       <div class="panel panel-default" style="margin-right:-41px;"> 
			<div class="row" style="margin: 10px 5px 5px 5px;">
			   <section class="panel panel-default">
			        <div class="panel-heading">
			        	<span>
			              <i class="fa fa-table"></i>交路信息
			              <shiro:hasPermission name="JHPT.KYJH.LJ.JJS">   
				              <a  type="button" style="margin-left: 15px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#" id="cross_train_save" data-bind="attr:{class: searchModle().activeCurrentCrossFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: saveCrossInfo">
				              	<i class="fa fa-floppy-o"></i>保存
				              </a>
			              </shiro:hasPermission>
					   </span>
					</div> 
			          <div class="panel-body">
						<div class="row" >
							<form class="form-horizontal" role="form" data-bind="with: currentCross"> 
							<div class="row" style="margin: 0px 0 5px 0;"> 
										<label for="exampleInputEmail3"
											class="control-label pull-left"> 车底交路名:&nbsp;</label>
										<div class="pull-left" style="margin-left: 26px;">
											<input type="text" class="form-control" style="width: 470px;" data-bind="value: crossName"
												id="plan_construction_input_trainNbr" disabled>
										</div> 
										<label for="exampleInputEmail5" class="control-label pull-left" style="margin-left:40px">
											铁路线型:</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control" data-bind="checked: highlineFlag" value="0" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											普速</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control" value="1" data-bind="checked: highlineFlag" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											高铁</label> 
										<div class="pull-left">
											<input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 3px"
												class="form-control" value="2" data-bind="checked: highlineFlag" disabled>
										</div>
										<label for="exampleInputEmail5" class="control-label pull-left">
											混合</label> 
								</div>
								<div class="row" style="margin: 5px 0 0px 0;">
								   
									    <label for="exampleInputEmail3"
												class="control-label pull-left"> 备用套跑交路名:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 470px;" data-bind="value: crossSpareName" disabled>
										</div> 
										<label for="exampleInputEmail5" style="margin-left: 40px;" class="control-label pull-left">
											开行状态:</label>
												
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
												<input type="text" class="form-control" style="width: 40px;" data-bind="value: groupTotalNbr" onkeyup="value=value.replace(/[^1-9]/g,'')">
											</div> 
										</div>
										<div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail2" class="control-label pull-left">对数:&nbsp;</label>
											<!-- <input type="text" class="form-control" style="width: 40px;" data-bind="value: pairNbr" onkeyup="value=value.replace(/[^1-9]/g,'')"> -->
											<input type="text" class="form-control" style="width: 40px;" data-bind="value: pairNbr">
										 
										</div> 
										<div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail5"  class="control-label pull-left">
												开始:&nbsp;</label> 
											<input type="text" class="form-control" style="width: 70px;"
													placeholder=""  data-bind="value: crossStartDate"> 
										</div>
										<div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												结束:&nbsp;</label> 
											<input type="text" class="form-control" style="width: 70px;"
													placeholder=""  data-bind="value: crossEndDate">
											 
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">  
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: cutOld"
												style="width: 20px; margin-top: 5px"
												class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												截断原交路</label>
										</div>
									</div>
									 <div class="pull-left" style="width: 28%;"> 
										 <div class="row" style="margin: 5px 0 0px 0;"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												普速规律:</label> 
											<input type="radio" class="pull-left" class="form-control"
													value="1" data-bind="checked: commonlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 3px"
													class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												每日</label> 
											<input type="radio" class="pull-left" class="form-control"
													value="2" data-bind="checked: commonlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 3px"
													class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												隔日</label>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">  
											<label for="exampleInputEmail5"  class="control-label pull-left">
												高线规律:</label>	 
											   <input type="radio" class="pull-left" class="form-control"
													value="1" data-bind="checked: highlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 3px"
													class="form-control"> 
												<label for="exampleInputEmail5" class="control-label pull-left">
												日常</label> 
												<input type="radio" class="pull-left" class="form-control"
													value="2" data-bind="checked: highlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 3px"
													class="form-control"> 
												<label for="exampleInputEmail5" class="control-label pull-left">
													周末</label>  
												<input type="radio" class="pull-left" class="form-control"
														value="3" data-bind="checked: highlineRule"
														style="width: 20px; margin-left: 5px; margin-top:3px"
														class="form-control">
												 
												<label for="exampleInputEmail5" class="control-label pull-left">
													高峰</label>  
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">  		
											<label for="exampleInputEmail5" class="control-label pull-left">
												指定星期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 71px;"
													placeholder=""   data-bind="value: appointWeek">
											</div>
										</div>
										 <div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5" class="control-label pull-left">
												指定日期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 140px;"
													placeholder=""  data-bind="value: appointDay">
											</div> 
										</div> 
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail5" class="control-label pull-left">
												指定周期:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 140px;"
													placeholder=""  data-bind="value: appointPeriod">
											</div> 
										</div> 
								   </div>
								   
								   <div class="pull-left" style="width: 25%;"> 
								      <div class="row" style="margin: 5px 0 0px 0;">
										<label class="control-label pull-left" style="margin-left: 32px;"> 担当局:&nbsp;</label>
										<div class="pull-left">
											<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenVehBureau, optionsText: 'shortName', optionsValue:'code' , optionsCaption: ''"></select> -->
											<input type="text" class="form-control" disabled  style="width: 50px;"  data-bind="value: tokenVehBureauShowValue">
										</div>
									  </div>
									  <div class="row" style="margin: 5px 0 0px 0;">
										<label  class="control-label pull-left" > 车辆/动车段:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 90px;" data-bind="value: tokenVehDept">
											</div>
									   </div>
									    <div class="row" style="margin: 5px 0 0px 0;">
											<label  class="control-label pull-left" style="margin-left: 30px;"> 动车所:&nbsp;</label>
												<div class="pull-left">
													<input type="text" class="form-control" style="width: 90px;" data-bind="value: tokenVehDepot">
												</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 5px;"
													class="control-label pull-left"> 客运担当局:&nbsp;</label>
											<div class="pull-left">
												<!-- <input type="text" class="form-control" style="width: 30px;" data-bind="value: tokenPsgDept"> -->
												<input type="text" class="form-control disabled" style="width: 50px;" disabled  data-bind="value: tokenPsgBureauShowValue" >
												<!-- <select style="width: 50px" class="form-control" data-bind="options: $parent.gloabBureaus, value: tokenPsgBureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: ''"></select> -->
											</div>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" style="margin-left: 30px;"
												class="control-label pull-left"> 客运段:&nbsp;</label>
											<div class="pull-left">
												<input type="text" class="form-control" style="width: 90px;" data-bind="value: tokenPsgDept">
											</div>
										</div>
								   </div>
								   
								   <div class="pull-left" style="width: 30%;"> 
									   <div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left" > 运行区段:&nbsp;</label>
											<input type="text" maxlength="200" class="form-control pull-left" style="width: 182px;" data-bind="value: crossSection">
											<label for="exampleInputEmail3"
												class="control-label pull-left" > <!-- 不能超过200字符 --></label>
										</div>
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3"
												class="control-label pull-left"  style="margin-left: 13px;" > 经由线:&nbsp;</label>
											<input type="text" class="form-control" maxlength="30" style="width: 182px;" data-bind="value: throughline">
										</div> 
										<div class="row" style="margin: 5px 0 0px 0;">
											<label for="exampleInputEmail3" 
													class="control-label pull-left"> 机车类型:&nbsp;</label> 
											<input type="text" class="form-control pull-left" style="width: 40px;" data-bind="value: locoType">
											<label for="exampleInputEmail3" style="margin-left:10px"
													class="control-label pull-left"> 动车组车型:&nbsp;</label> 
											<input type="text" class="form-control pull-left" style="width: 60px;" data-bind="value: crhType">
										</div>
									<div class="row" style="margin: 5px 0 0px 0;">
									    <label for="exampleInputEmail3"  style="margin-left: 26px;"
													class="control-label pull-left"> 其他:&nbsp;</label> 
										<input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: elecSupply"
											style="width: 20px; margin-top: 5px;"
											class="form-control"> 
										<label for="exampleInputEmail5" class="control-label pull-left margin-2">
											供电</label> 
										<input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: dejCollect"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control"> 
										<label for="exampleInputEmail5" class="control-label pull-left margin-2">
											集便</label> 
										<input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: airCondition"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control"> 
										<label for="exampleInputEmail5" class="control-label pull-left margin-2">
											空调</label> 
									</div>  
									<div class="row" style="margin: 5px 0 0px 0;">
									     <label for="exampleInputEmail3"  style="margin-left: 26px;"
												class="control-label pull-left"> 备注:&nbsp;</label>
										<div class="pull-left">
											<input type="text" class="form-control" style="width: 182px;" data-bind="value: note">
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
					<section class="panel panel-default" >
				         <div class="panel-heading">
				         
				         <span>
			              <i class="fa fa-table"></i>列车信息   
			                           
										<a  type="button" style="margin-left: 15px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="click: showCrossMapDlg"><i class="fa fa-line-chart"></i>交路图</a>
<!-- 										<a  type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#" -->
<!-- 										id="cross_train_save" data-bind="click: showCrossTrainTimeDlg"><i class="fa fa-clock-o"></i>时刻表</a> -->
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
					</div>			
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
		<img id="loading" src="assets/images/loading.gif" style="display:none;"> 
		<form id="file_upload_id" name="file_upload_name" action="cross/fileUpload" method="post" enctype="multipart/form-data"> 
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
			<div class="row" style="width: 100%; margin-top: 5px;">
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
	</div> 
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
