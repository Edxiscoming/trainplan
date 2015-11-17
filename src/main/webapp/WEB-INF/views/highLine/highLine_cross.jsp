
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
/*
boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
} */
/* String currentUserBureau = "";
List<String> permissionList = "";
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
}  */
String basePath = request.getContextPath();
String  currentBureauShortName = user.getBureauShortName();
String currentUserBureau = user.getBureau();
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>高铁车底交路计划编制</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css"
	rel="stylesheet">
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/cross/custom-bootstrap.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/style.css"> 
	
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" />
 
<!-- Custom styles for this template --> 
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/rightmenu.css">



   <script type="text/javascript">

var basePath = "<%=basePath %>";
var all_role = "";
var _isZgsUser = true;//当前用户是否为总公司用户
var currentUserBureau = "<%=currentUserBureau %>";
var currentBureauShortName = "<%=currentBureauShortName %>";
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
.ckbox.disabled{
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
}

 </style>

 
</head>
<body class="Iframe_body" style="height:100%;margin:-9px -6px 0px -3px;width:100%" >
	
	<!-- <ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">车底三乘计划 -> 高铁车底交路计划编制</a></li>
	</ol>   -->
	 <div id="div_searchForm" class="row" style="margin:-15px 0 10px 0;"> 
	    <form class="form-horizontal" role="form">
	   	<div class="row" style="margin:15px 0 10px 0;">
		      <div class="pull-left"> 
		        <div class="pull-left"> 
					 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 12px;margin-top: 6px;">
														日期:&nbsp;</label> 
					 <input type="text" class="form-control" style="padding:4px 12px;width:95px;margin-top:3px" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
				 </div>
				 <div class="pull-left" style="margin-left:10px"> 
					 <label for="exampleInputEmail5" style="margin-top: 6px;">铁路线:&nbsp;</label> 
					 <select class="form-control" style="padding:4px 12px;width: 120px;display:inline-block;" id="input_cross_filter_showFlag"
							 data-bind="options: searchModle().throughLines, value: searchModle().searchThroughLine, optionsText: 'name', optionsValue: 'code', optionsCaption: ''">
					 </select>  
				  </div>
				   <div class="pull-left" style="margin-left:10px"> 
					 <label for="exampleInputEmail5" style="margin-top: 6px;">担当局:&nbsp;</label> 
					 <select class="form-control" style="padding:4px 12px;width: 76px;display:inline-block;" id="input_cross_filter_showFlag1"
							 data-bind="options: searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue: 'code'">
					 </select> 
				  </div>
				   <div class="pull-left" style="margin-left:10px"> 
					 <label for="exampleInputEmail5" style="margin-top: 6px;">动车所:&nbsp;</label> 
					 <select class="form-control" style="padding:4px 12px;width: 150px;display:inline-block;" id="input_cross_filter_showFlag2"
							 data-bind="options: searchModle().tokenVehDepots, value: searchModle().searchTokenVehDepot, optionsText: 'name', optionsValue: 'name' , optionsCaption: ''">
					 </select> 
				  </div>
				   <div class="pull-left" style="margin-left:10px"> 
					 <label for="exampleInputEmail5" style="margin-top: 6px;">动车台:&nbsp;</label> 
					 <select class="form-control" style="padding:4px 12px;width: 150px;display:inline-block;" id="input_cross_filter_showFlag3"
							 data-bind="options:searchModle().accs, value: searchModle().searchAcc, optionsText: 'name', optionsValue: 'name' , optionsCaption: ''">
					 </select> 
				  </div>
				  <div class="pull-left"> 
					 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 6px;margin-left: 15px;">
														车次:&nbsp;</label> 
					 <input type="text" id="input_cross_filter_showFlag4" class="form-control" style="padding:4px 12px;margin-top:3px;width:95px;" data-bind="value: searchModle().trainNbr"/>
				 </div>
				  <a type="button"  style="margin:3px 0 0 5px" class="btn btn-success btn-input" data-toggle="modal" data-target="#"  data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a>
			 </div>
		 </div>
		 
		 <div class="row" > 
	        <div class="pull-left">   
			  <a type="button"  style="margin-left:15px" class="btn btn-success" data-bind="click: createHighLineCrosses"><i class="fa fa-sign-in"></i>加载</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: deleteAllHighLineCrosses"><i class="fa fa-trash-o"></i>清空</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: showActiveHighLineCrossDlg"><i class="fa fa-pencil-square-o"></i>调整</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: submitHighLineWithRole"><i class="fa fa-external-link"></i>提交</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: previewCmdInfo"><i class="fa fa-sign-out"></i>生成命令</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: importExcel"><i class="fa fa-sign-out"></i>导出EXCEL</a>
			  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: lineDictionary"><i class="fa fa-book"></i>线台字典</a>
			<!-- <a type="button" style="margin-left: 15px;margin-top: -5px" class="btn btn-success" data-toggle="modal" data-target="#" id="cross_train_save" ><i class="fa fa-bar-chart-o"></i>交路图</a>
			 -->
			</div>
			<div class="pull-right" style="margin-right:10px;">
				<input type="checkbox" class="pull-left" class="form-control" data-bind="checked: isShowCrossDetailInfo, event:{change: showRunPlans}"> 
				<label for="exampleInputEmail5" class="control-label pull-left"><b>显示交路详情</b></label>  
		 	</div>
	    </div>
	    </form> 
	  </div>  
	    <div class="row" style="margin: 10px 10px 0px 10px;">   
		    <!--分栏框开始-->
		    <div id="div_hightline_planDayDetail" class="panel panel-default">
				     <div class="row" style="margin:15px 10px 10px 10px;"> 
					     <div class="table-responsive" >
					          <table class="table table-bordered table-striped table-hover">
									<thead>
										<tr> 
											<th style="width: 45px">序号</th> 
											<th style="width: 100px">铁路线</th>
											<th style="width: 75px">首车日期</th>  
											<th style="width: ">  
											      <div  style="position: relative;">
													<label for="exampleInputEmail5" style="font-weight: bold;vertical-align: bottom;">交路</label> 
														<select class="form-control" style="width: 58px;display:inline-block;position: absolute;top:-4px;margin-left: 5px;" id="input_cross_filter_showFlag" 
														data-bind="options: [{'code': 2, 'text': '全称'},{'code': 1, 'text': '简称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'">
													</select>
												  </div>  
											</th>

											<th style="width: 100px">车型</th>
											<th style="width: 100px">车底1</th>
											<th style="width: 100px">车底2</th>
											<th style="width: 120px">出库所/始发站</th>  
											<th style="width: 120px">入库所/终到站</th>
											<th style="width: 40px">热备</th>
											<th style="width: 55px" >担当局</th>
											<th style="width: 140px;" >动车所</th>
											<th style="width: 120px" >管辖动车台</th>
											<th style="width: 60px" >来源</th>
											<th style="width: 17px" ></th>
										</tr>
									</thead>
							 </table>
					         <div id="left_height" style="overflow-y:auto;">
						          <table class="table table-bordered table-striped table-hover" style="border: 0;">
									<tbody data-bind="foreach: highLineCrossRows">
										<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().highLineCrossId == highLineCrossId ? 'blue':''}" >
									        <td style="width: 45px" data-bind="text: ($index() + 1), click: $parent.showTrains"></td> 
									        <td align="center" style="width: 100px" data-bind="text: throughLine, click: $parent.showTrains"></td> 
									        <td align="center" style="width: 75px" data-bind="text: crossStartDate, click: $parent.showTrains"></td> 
										    <td style="width: " data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, attr:{title: crossName}, click: $parent.showTrains" ></td>
										    <td style="width: 100px" data-bind="text: crhType, click: $parent.showTrains"></td>
										    <td style="width: 100px" data-bind="text: vehicle1, click: $parent.showTrains"></td>
										    <td style="width: 100px" data-bind="text: vehicle2, click: $parent.showTrains"></td>       
										    <td style="width: 120px" data-bind="text: startStn, click: $parent.showTrains"></td>
										    <td style="width: 120px" data-bind="text: endStn, click: $parent.showTrains"></td> 
										    <td align="center" style="width: 40px"data-bind="text: (spareFlag() == 2 ? '是' : '否'), click: $parent.showTrains"></td>
										    <td align="center" style="width: 55px" data-bind="text: tokenVehBureauShowValue, click: $parent.showTrains"></td>
										    <td style="width: 140px;" data-bind="text: tokenVehDepot, click: $parent.showTrains"></td>
										    <td style="width: 120px;" data-bind="text: postName, click: $parent.showTrains"></td>
										    <td align="center" style="width:60px;" data-bind="text: createReason, click: $parent.showTrains"></td>
										    <td style="width:17px" class="td_17"></td>
										</tr>
									</tbody>
								</table>
						 </div>
								<div data-bind="template: { name: 'tablefooter-short-template'}" style="margin-bottom: 5px"></div>
					        </div> 
				       </div>
				  </div>  
	    </div>
	   <div id="div_crossDetailInfo" class="row" style="margin: 10px 0px 0px 10px;">  
	        <!--分栏框开始-->
		    <div class="pull-left" style="width: 39%;" id="leftBox">
			<!--分栏框开始-->   
			         <div id="div_crossDetailInfo_trainStnTable" style="overflow: auto;" class="panel panel-default"> 
				       <div class="panel-body"> 
							<table class="table table-bordered table-striped table-hover" style="margin-left:2px; margin-right:5px;">
								<thead>
									<tr style="height: 26px">
										<th class="text-center" style="vertical-align: middle;width:20px" rowspan="2">序号</th>
										<th class="text-center" style="vertical-align: middle;width:100px" rowspan="2">车次</th>
										<th class="text-center" style="vertical-align: middle;width:20px" colspan="3">始发</th>
										<th class="text-center" style="vertical-align: middle;width:20px" colspan="3">终到</th>
									</tr>
									<tr style="height: 26px"> 
										<th style="width: 30px" align="center">局</th> 
										<th style="width: 120px" align="center">站名</th> 
										<th style="width: 70px" align="center">时间</th>
										<th style="width: 30px" align="center">局</th> 
										<th style="width: 120px" align="center">站名</th> 
										<th style="width: 70px" align="center">时间</th>
									</tr>
								</thead>
								<tbody data-bind="foreach: trains" >
									<tr  data-bind="click: $parent.showTrainTimes, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'blue':''}">
										<td data-bind="text: ($index() + 1)"></td>
										<td data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
										
										<td data-bind="text: startStnBureau, attr:{title: startStnBureau}"></td>
										<td data-bind="text: startStn, attr:{title: startStn}"></td>
										<td data-bind="text: startTime, attr:{title: startTime}"></td>
										
										
										<td data-bind="text: endStnBureau, attr:{title: endStnBureau}"></td>
										<td data-bind="text: endStn, attr:{title: endStn}"></td>
										<td data-bind="text: endTime, attr:{title: endTime}"></td>
									</tr>
								</tbody>
							</table>   
					 </div>  
					 </div>
					</div>
			 	<div class="blankBox"></div>
		        <div class="pull-left" style="width: 60%;" id="rightBox">
		        <div class="panel panel-default"> 
					 <div class="panel-body" style="overflow:auto;">
					      	<div class="row" style="margin:5px 0 10px 0;">
						      <form class="form-inline" role="form">
					              <div class="row" style="margin:5px 0 10px 50px;">
						         	  <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_refresh"><i class="fa fa-refresh"></i>刷新</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_magnification"><i class="fa fa-search-plus"></i>X+</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_shrink"><i class="fa fa-search-minus"></i>X-</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_magnification"><i class="fa fa-search-plus"></i>Y+</button>
						              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_shrink"><i class="fa fa-search-minus"></i>Y-</button>
						                                                 比例：｛X:<label id="canvas_event_label_xscale">1</label>倍；Y:<label id="canvas_event_label_yscale">1</label>倍｝
									  <span><input type="checkbox" id="canvas_checkbox_stationType_jt" name="canvas_checkbox_stationType" checked="checked" style="margin-left:10px">简图</span>
						         	  <input type="checkbox" id="canvas_checkbox_trainTime" style="margin-left:10px;margin-top:2px"  value=""/>时刻
						         	   &nbsp;&nbsp;车底：<select id="canvas_select_groupSerialNbr"></select>
						                       
						         </div>
					          </form>
						    </div> 
					        <div id="canvas_parent_div" class="table-responsive" style="width:100%;height:390px;overflow-x:auto; overflow-y:auto;">
					        	<canvas id="canvas_event_getvalue"></canvas>
					        </div> 
					</div> 
				</div> 
			</div>
		</div>  
		<!--时刻表--> 
		<div id="train_time_dlg" class="easyui-dialog" title="时刻表"
			data-options="iconCls:'icon-save'"
			style="width: 750px; height: 700px;overflow-y: hidden;">
		 <iframe style="width: 100%;border: 0;overflow-y: hidden;" src=""></iframe>
		
	   <!-- 命令查看界面  开始-->
	   <div id="cmdInfo_dialog" class="easyui-dialog"
	      title="命令预览" data-options="iconCls:'icon-save'"
		   style="width: 600px; height: 500px; padding: 10px;">
	   		<div id="cmdInfo_dialog_row1" class="row" style="width:100%;overflow: hidden;">
	   			<textarea id="textarea_cmdInfoStr" rows="24" cols="94" data-bind="value:cmdInfoStr()"></textarea>
	   			<!-- <span data-bind="html : cmdInfoStr()"></span> -->
	   		</div>
	   		<div id="cmdInfo_dialog_row2" class="row" style="width:100%;height:30px;margin-top:10px;margin-bottom:5px">
	   			<div align="center">
	   				<a type="button" class="btn btn-success" data-bind="click: createCmdInfo">确定</a>
	   				<a type="button" class="btn btn-success" data-bind="click: createCmdInfoCancel" style="margin-left:5px">取消</a>
	   			</div>
	   		</div>
	   </div>
	   <!-- 命令查看界面  end-->
	   
	   
	    <div id="active_highLine_cross_dialog" class="easyui-dialog"
	      title="交路调整"
		  data-options="iconCls:'icon-save'"
		   style="width: 1200px; height: 500px; padding: 10px;" >  
		      <div class="row" style="width:100%;height:90%"> 
		    	 <div class="pull-left" style="width: 27%;height:100%" >
				<!--分栏框开始-->    
					   <section class="panel panel-default" style="height:100%">
				        <div class="panel-heading">
				        	<span>
				              <i class="fa fa-table"></i>交路列表
						   </span>
						</div> 
				          <div class="panel-body" style="height:93%">
					         <div class="row" style="width:100%;height:95%"> 
						        	<select multiple="multiple" id="current_highLineCrosses" style="width:100%;height:100%" data-bind="options: highLineCrossRows, optionsText: 'crossName', selectedOptions:selectedHighLineCrossRows" >
						        	 
						        	</select>
							  </div>
							  <div class="row" style="margin-top:5px"> 
							    <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click: deleteHighLineCrosses">删除</a>
							  </div>
						</div>
						</section>
						
				</div>
				<div class="pull-left" style="width: 25%;height:100%" >
				<!--分栏框开始-->    
				  <section class="panel panel-default" style="height:100%">
				        <div class="panel-heading">
				        	<span>
				              <i class="fa fa-table"></i>交路组合列表
						   </span>
						</div> 
				          <div class="panel-body" style="height:93%">
				             <div class="row" style="width:100%;height:95%"> 
						          <div class="pull-left" style="width:85%;height:100%"> 
							        	<select multiple="multiple" style="width:100%;height:100%" data-bind="options: acvtiveHighLineCrosses, optionsText: 'crossName', selectedOptions: selectedActiveHighLineCrossRows, event:{change: selectedActiveHighLineCrossChange} "></select>
								  </div>
								  <div class="pull-left" style="width:15%;height:100%"> 
								          <div style="height:40%;" > 
									      </div> 
									      <div style="height:60%;" > 
									        <a data-bind="click: activeCrossUp" style="margin-left: 2px"><i class="fa fa-arrow-up"></i>上</a><!-- fa fa-chevron-up blue -->
									        <p>&nbsp;</p>
									        <a data-bind="click: activeCrossDown" style="margin-left:2px;"><i class="fa fa-arrow-down"></i>下</a><!-- fa fa-chevron-down blue -->
									      </div> 
								  </div> 
							 </div>
							  <div class="row" style="margin-top:5px"> 
							    <a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#"  data-bind="click: cjHighLineCross">拆解</a>
								<a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: hbHighLineCrossConfirm">组合</a>
							  </div> 
						</div>
					</section>
				</div>
				<div class="pull-left" style="width: 45%;height:100%" >
				<!--分栏框开始-->    
				   <section class="panel panel-default" style="height:100%">
				        <div class="panel-heading" id="aaaaa">
				        	<span>
				              <i class="fa fa-table"></i>时刻表
						   
						   
						   <input type="checkbox" id="input_checkbox_stationType_jt" name="input_checkbox_stationType1" checked="checked"  style="margin-left:10px">
							</span>
							简点
						</div> 
				          <div class="panel-body" style="height:90%"> 
					        	<table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead> 
							         <tr>
							         <th style="width:10%">序号</th>
							          <th style="width:10%">车次</th>
							          <th style="width:10%">站序</th>
					                  <th style="width:20%">站名</th> 
					                
					                  <th style="width:15%">到达时间</th> 
					                  <th style="width:15%">出发时间</th>  
					                    <th style="width:10%">停时</th>
					                 </tr> 
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="allTimes_table" style="height: 360px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" > 
														 <tbody data-bind="foreach: activeTimes1">
												           <tr>  
												           	<td style="width:10%;" align="center"  data-bind="text: seq"></td>
												           <td style="width:10%" align="center" data-bind=" text: trainNbr"></td>
															<td style="width:10%" align="center" data-bind=" text: stnSort"></td>
															<td style="width:20%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:15%" align="center" data-bind="text: simS"></td>
															<td style="width:15%" align="center" data-bind="text: simD"></td>
												        	<td style="width:10%" align="center" data-bind="text: stepStr"></td> 
												        	<td style="width:20%;display:none" align="center" data-bind="text: sourceTime"></td>
															<td style="width:20%;display:none" align="center"  data-bind="text: targetTime"></td>
														
												        	</tr>
												        </tbody>
													</table> 
											 	</div>
											</td>
										</tr>
									</tbody> 
						        </table>
						  </div> 
						</section>
				</div>
			</div>
			<div class="row" style="width:100%;" align="center">  
			   <div class="panel panel-default" style="padding-top:10px;padding-bottom:5px"> 
			      <a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#"  data-bind="click: submitHighLineCross">确定交路</a>
			      <a type="button"  style="margin-left:5px" class="btn btn-success" data-toggle="modal" data-target="#"  data-bind="click: cancelHighLineCross">取消</a>
		        </div>  
		     </div>
	    </div> 
	    <div id="hb_highLine_cross" class="easyui-dialog" title="合并交路"
			data-options="iconCls:'icon-save'"
			style="width: 300px; height: 310px; padding: 0px"> 
				 <div class="row" style="width: 100%; margin-top: 10px;margin-left:25px">
						<label for="exampleInputEmail3" class="control-label pull-left" >动车台:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 170px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().accs, value: searchModle().acc, optionsText: 'name', optionsValue:'code', optionsCaption: ''">
							</select>
						</div>   
						<label style="color:#ff3030;font-size: 20px;margin: -3px 0 0 3px;"><b>*</b></label>
				 </div> 
				 <div class="row" style="width: 100%; margin-top: 10px;margin-left:25px">
						<label for="exampleInputEmail3" class="control-label pull-left">
										动车所:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 170px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().tokenVehDepots, value: searchModle().tokenVehDepot, optionsText: 'name', optionsValue: 'name', optionsCaption: ''">
							</select>
						</div>
						<label style="color:#ff3030;font-size: 20px;margin: -3px 0 0 3px;"><b>*</b></label>
				 </div>
				 <div class="row" style="width: 100%; margin-top: 10px;margin-left:25px">
						<!-- <label for="exampleInputEmail3" class="control-label pull-left">客运担当局:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 60px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().tokenPsgBureaus, value: searchModle().tokenPsgBureau, optionsText: 'shortName',optionsValue:'code', optionsCaption: ''">
							</select>
						</div> -->
						<label for="exampleInputEmail3" class="control-label pull-left">
										 车&nbsp;&nbsp;&nbsp;&nbsp;型:&nbsp;</label>
						<div class="pull-left">
							<select style="width: 170px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().crhTypes, value: searchModle().crhType, optionsText: 'name',optionsValue:'code', optionsCaption: ''">
							</select>
						</div>
						<label style="color:#ff3030;font-size: 20px;margin: -3px 0 0 3px;"><b>*</b></label>
				 </div>
				  
				  <div class="row" style="width: 100%; margin-top: 10px;margin-left:25px">
						<label for="exampleInputEmail3" class="control-label pull-left">
										铁路线:&nbsp;</label> 
						<div class="pull-left">
							<select style="width: 170px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().throughLines, value: searchModle().throughLine, optionsText: 'name',optionsValue:'code', optionsCaption: ''">
							</select>
						</div>   
						<!-- <label style="color:#ff3030;font-size: 20px;margin: -3px 0 0 3px;"><b>*</b></label> -->
				 </div>
<!-- 				 <div class="row" style="width: 100%; margin-top: 10px;margin-left:25px"> -->
<!-- 						<label for="exampleInputEmail3" class="control-label pull-left"> -->
<!-- 										客运段:&nbsp;</label>  -->
<!-- 						<div class="pull-left"> -->
<!-- 							<input class="form-control"  style="width:170px;" placeholder="" data-bind="value: searchModle().tokenPsgDept"> -->
<!-- 						</div>    -->
<!-- 				 </div>    -->
				 <div class="row" style="margin: 10px 0 5px 0;margin-left:25px">
					<label for="exampleInputEmail2" class="control-label pull-left">来&nbsp;&nbsp;&nbsp;&nbsp;源:&nbsp;</label>
			        <div class="pull-left">
              			<textarea class="form-control" rows="4" style="width:170px;" data-bind="value : searchModle().createReason"></textarea>
			        </div> 
			   </div> 
			   <div  class="row" style="width: 100%; margin-top: 10px;" align="center">
				     <a type="button" id="btn_fileToUpload"
						class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: $root.hbHighLineCrossYes">确定</a>
					 <a type="button" id="btn_fileToUpload"
						class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: $root.hbHighLineCrossCancel">取消</a>
				</div> 
		</div>  
 <div id="lineDictionary_dialog" class="easyui-dialog" title="线台字典管理"
	data-options="iconCls:'icon-save'"
	style="width: 1200px; height: 750px;overflow: hidden;">
 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
</body>  
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left"><span data-bind="html: $root.currentTotalCount()"></span></span>
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		  
     </td >
  </tr>
</table> 
</script> 

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/resizable.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/highLine/highLine_cross.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
<script src="<%=basePath %>/assets/lib/fishcomponent.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script> --%> 

<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_rightmenu_2015.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_event_getvalue.js"></script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
<script type="text/javascript">
	$(function() {
	   resize("leftBox","rightBox","670","730");
	 });
</script>
<script  type="text/html" id="runPlanTableDateHeader"> 
    <!-- ko if: $index() == 0 -->
 	<td></td>
 	<!-- /ko --> 
 	<td align='center' data-bind="text: day"></td>
</script>
<script  type="text/html" id="runPlanTableVlaues">
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
 	<td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->  
 	<td  align='center' data-bind="text: runFlag, style:{'color': color}"></td>
 </tr> 
</script>
</html>
