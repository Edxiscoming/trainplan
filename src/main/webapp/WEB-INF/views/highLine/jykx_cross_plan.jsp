<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String basePath = request.getContextPath();
%>
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>高铁开行计划</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="<%=basePath%>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath%>/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="<%=basePath%>/assets/css/cross/custom-bootstrap.css"
	rel="stylesheet">
<link href="<%=basePath%>/assets/css/cross/zTreeStyle.css"
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
	
<script type="text/javascript" src="<%=basePath%>/assets/js/resizable.js"></script>

<script type="text/javascript"
	src="<%=basePath%>/assets/js/knockout.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/ajaxfileupload.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/trainplan/highLine/jykx_crossplan.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/datepicker.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/trainplan/common.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/trainplan/jquery.ztree.core-3.5.js"></script>
<script src="<%=basePath%>/assets/js/trainplan/knockout.pagemodle.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/moment.min.js"></script>
<style type="text/css">
#testIframe {
	margin-left: 10px;
}

.pagination>li>a,.pagination>li>span {
	position: relative;
	float: left;
	line-height: 1.428571429;
	text-decoration: none;
	background-color: #ffffff;
	border: 1px solid #dddddd;
	margin-left: -1px;
}

.Iframe_body {
	padding: 12px 10px 0 10px;
}

.ckbox.disabled {
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity = 65);
	-webkit-box-shadow: none;
	box-shadow: none;
}
</style>
<script type="text/javascript">
var basePath = "<%=basePath%>";
</script>
<script type="text/html" id="crossPlanVehicleSort"> 
   <!-- ko if: $index() == 0 -->
   <td style="vertical-align: middle;" style="width:20px">车厢编号</td>
	<!-- /ko -->
	<td align='center' data-bind="text: vehicleSort" style="width:20px" ></td>
   <!-- ko if: $index() == 19 -->
   <td align="center" style="width:20px" >计</td>
   <!-- /ko -->
</script>
<script type="text/html" id="crossPlanVehicleType">
     <!-- ko if: $index() == 0 -->
	  <td style="vertical-align: middle;width:20px">车种</td>
    <!-- /ko -->
	  <td align='center' data-bind="text: vehicleType" style="width:20px"></td>
    <!-- ko if: $index() == 19 -->
     <td align="center" style="width:20px" ></td>
    <!-- /ko -->
</script>
<script type="text/html" id="crossPlanVehicleCapacity">
    <!-- ko if: $index() == 0 -->
    <td style="vertical-align: middle;width:20px">定员</td> 
    <!-- /ko -->
	 <td align='center' data-bind="text: vehicleCapacity" style="width:20px"></td>
    <!-- ko if: $index() == 19 -->
    <td align="center" data-bind=" text :$parent.cartPeople" style="width:20px"></td>
    <!-- /ko -->
</script>
<script type="text/javascript">

	$(function() {
	   resize("leftBox","rightBox","270","420");
	 });

</script>
</head>

<body class="Iframe_body">
	<input id="trainRunTime_runDate_hidden" type="hidden" value="${runDate}">
	<!--分栏框开始-->
	<!--上-->
	<div class="row" id="div_searchForm" style="margin: -8px 0 0 0;">
		<div class="pull-left" style="width: 19%;min-width:;" id="leftBox">
			<div class="row">
				<div class="form-group"
					style="float: left; margin:0 0 8px 0; width: 100%">
					<div class="row" style="margin-top: 5px;">
						<div class="col-md-6" style="padding: 0;">
							<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
								日期:&nbsp;</label>
							<div style="margin-left:35px; width: auto;padding-right: 5px;">
								<input type="text" class="form-control" style="padding:4px 12px; width:;"
									placeholder="" id="cross_lan_input_Date" name="runDate"
									data-bind="value: searchModle().runDate" />
							</div>
						</div>
						<div class="col-md-6 pull-right" style="padding: 0;margin-left: -10px;">
							<label for="exampleInputEmail3" class="control-label" style="margin-top:5px;float: left;width: 35px;">
								路局:&nbsp;</label>
							<div style="margin-left:-35px; float:right; width:100%;">
								
							    <div style="margin-left:35px; width: auto;">
							        <select class="form-control" style="padding:4px 12px; width:"
									data-bind="options:searchModle().startBureaus, value: searchModle().startBureau, optionsText: 'shortName'"></select>							    
							    </div> 
							</div>
						</div>
					</div>
					<div class="row" style="margin-top: 5px;">

						<div class="col-md-6" style="padding: 0;">
							<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
								车次:&nbsp;</label>
							<div style="margin-left:35px; width: auto;padding-right: 5px;">
								<input type="text" class="form-control" style="padding:4px 12px;"
									id="input_cross_filter_trainNbr"
									data-bind=" value: searchModle().trainNbr, event:{keyup: trainNbrChange}">
							</div>
						</div>

						<div class="col-md-6 pull-right" style="padding: 0;margin-left: -10px;">
							<label for="exampleInputEmail3" class="control-label" style="margin-top:5px;float: left;width: 62px;">
								运行方式:&nbsp;</label>
							<div style="margin-left:-62px; float:right; width:100%;">
								
							    <div style="margin-left:62px; width: auto;">
								<select class="form-control"
									style="padding:4px 0 4px 10px; ;width:"
									data-bind="options: [{'code': '', 'text': '全部'} ,{'code': 'SFZD', 'text': '始发终到'},{'code': 'SFJC', 'text': '始发交出'},{'code': 'JRJC', 'text': '接入交出'},{'code': 'JRZD', 'text': '接入终到'}], value: searchModle().runTypeCode, optionsText: 'text',optionsValue:'code'">
								</select>							    
							    </div> 
							</div>
						</div>

					</div>
					<div class="row" style="margin-top: 7px;">
						<a type="button" class="btn btn-success pull-right btn-input" data-toggle="modal"
							style=""data-target="#" id="btn_cross_search"
							data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a>
					</div>
				</div>
				<div class="form-group panel panel-default"
					style="width: ; height:px;margin-bottom: -10px;">
					<ul class="nav nav-tabs"
						style="margin-top: 10px; margin-left: 5px; margin-right: 5px;">
						<li class="active"><a style="padding: 3px 10px;" href="#tab1"
							data-toggle="tab">按列车类型</a></li>
						<li><a style="padding: 3px 10px;" href="#tab2"
							data-toggle="tab">按发到站及分界口</a></li>
					</ul>
					<div class="tab-content" id="body_left"
						style="overflow-y:auto;margin: 20px 0px 10px 0px;">
						<div class="tab-pane active" id="tab1">
							<ul id="tree" class="ztree"
								style="overflow: auto; padding: 0 20px;"></ul>
						</div>
						<div class="tab-pane" id="tab2">
							<ul id="tree2" class="ztree"
								style="overflow: auto; padding: 0 20px;"></ul>
						</div>
					</div>
				</div>
			</div>


		</div>
		<div class="blankBox"></div>
		<div class="pull-left" style="width:80%;" id="rightBox">
			<section class="panel panel-default" style="margin:0;">
				<div class="panel-heading">
					<span> <i class="fa fa-table"></i>列车信息 <a type="button"
						style="margin-left: 5px; margin-top: -5px" class="btn btn-success"
						data-toggle="modal" data-target="#" id="cross_train_save"
						data-bind="click: showCrossTrainTimeDlg2"><i
							class="fa fa-list-ul"></i>时刻表</a>
					</span>
					
				</div>
				<div class="panel-body" id="body_right"
					style="padding: 15px; margin-bottom: 15px;">
					<div class="table-responsive" style="">
						<table class="table table-bordered table-striped table-hover"
							id="cross_trainInfo">
							<thead>
								<tr>
									<th style="width: 50px;">序号</th>
									<th style="width: 65px">运行方式</th>
									<th style="width: 90px;">车次</th>
									<th style="width: 80px">开行状态</th>
									<th style="width: 110px">始发站</th>
									<th style="width: 100px">始发时间</th>
									<th style="width: 110px">接入站</th>
									<th style="width: 100px">接入时间</th>
									<th style="width: 110px">交出站</th>
									<th style="width: 100px">交出时间</th>
									<th style="width: 110px">终到站</th>
									<th style="width: 100px">终到时间</th>
									<th style="width: 60px">天数</th>
									<th style="width: ">途经局</th>
									<th style="width: 100px">来源</th>
									<th style="width: 17px" align="center"></th>
								</tr>
							</thead>
					  </table>
					  <div id="left_height" style="overflow-y:auto;">
                		  <table class="table table-bordered table-striped table-hover" style="border: 0;">
							<tbody data-bind="foreach: trains">
								<tr data-bind="click: $parent.setCurrentTrain, style:{color: $parent.currentTrain() != null && $parent.currentTrain().planTrainId == planTrainId ? 'blue':''}">
									<td style="width: 50px" align="center" data-bind="text: $index() + 1"></td>
									<td style="width: 65px" align="center"
										data-bind="text: runType,attr:{title:runType}"></td>
									<td style="width: 90px" align="center"
										data-bind="text: trainNbr,attr:{title:trainNbr}"></td>
									<td style="width: 80px" align="center"
										data-bind="text: spareFlagTxt,attr:{title:spareFlagTxt}"></td>
									<td style="width: 110px" align="center"
										data-bind="text: startStn,attr:{title:startStn}"></td>
									<td style="width: 100px;padding: 4px 0;" align="center"
										data-bind="text: startTime,attr:{title:startTime}"></td>
									<td style="width: 110px" align="center"
										data-bind="text: jrStn,attr:{title:jrStn}"></td>
									<td style="width: 100px;padding: 4px 0;" align="center"
										data-bind="text: jrTime,attr:{title:jrTime}"></td>
									<td style="width: 110px" align="center"
										data-bind="text: jcStn,attr:{title:jcStn}"></td>
									<td style="width: 100px;padding: 4px 0;" align="center"
										data-bind="text: jcTime,attr:{title:jcTime}"></td>
									<td style="width: 110px" align="center"
										data-bind="text: endStn,attr:{title:endStn}"></td>
									<td style="width: 100px;padding: 4px 0;" align="center"
										data-bind="text: endTime,attr:{title:endTime}"></td>
									<td style="width: 60px" align="center"
										data-bind="text: endDays,attr:{title:endDays}"></td>
									<td style="width: px" align="center"
										data-bind="text: passBureau,attr:{title:passBureau}"></td>
									<td style="width: 100px" align="center"
										data-bind="text: laiyuan,attr:{title:laiyuan}"></td>
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
	<!--详情时刻表-->
	<div id="train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 700px;overflow-y: visible;">
		<iframe style="width: 700px; height: 650px;border: 0;overflow-y: hidden;position:absolute;" src=""></iframe>
	</div>  
	
	<!-- 
	<div id="cross_train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 500px; padding: 10px;">
		panle-heading
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
			Tab panes
			<div class="tab-content">
				<div class="tab-pane active" id="simpleTimes">
					<div class="table-responsive">
						<table class="table table-bordered table-striped table-hover"
							id="plan_runline_table_trainLine">
							<thead>
								<tr>
									<th style="width: 6.5%">序号</th>
									<th style="width: 9%">站名</th>
									<th style="width: 3%">路局</th>
									<th style="width: 13%">到达时间</th>
									<th style="width: 13%">出发时间</th>
									<th style="width: 5%">停时
									</th>
									<th style="width: 5%">到达天数
									</th>
									<th style="width: 5%">出发天数
									</th>
									<th style="width: 5%">股道</th>
								</tr>
							</thead>
							<tbody style="padding: 0">
								<tr style="padding: 0">
									<td colspan="9" style="padding: 0">
										<div id="simpleTimes_table"
											style="height: 350px; overflow-y: auto;">
											<table class="table table-bordered table-striped table-hover">
												<tbody data-bind="foreach: simpleTimes">
													<tr
														data-bind="visible: stationFlag != 'BTZ',attr:{class : isChangeValue()==1? 'danger':''}">
														<td style="width: 7.5%" align="center"
															data-bind=" text: $index() + 1"></td>
														<td style="width: 10%"
															data-bind="text: stnName, attr:{title: stnName}"></td>
														<td style="width: 5%" align="center"
															data-bind="text: bureauShortName,attr:{title: bureauShortName}"></td>
														<td style="width: 15%" align="center"
															data-bind="text: arrTime,attr:{title: arrTime}"></td>
														<td style="width: 15%" align="center"
															data-bind="text: dptTime,attr:{title: dptTime}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: stepStr,attr:{title: stepStr}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: arrRunDays,attr:{title: arrRunDays}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: runDays,attr:{title: runDays}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: trackName,attr:{title: trackName}"></td>
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
				<div class="tab-pane" id="allTimes">
					<div class="table-responsive">
						<table class="table table-bordered table-striped table-hover"
							id="plan_runline_table_trainLine">
							<thead>
								<tr>
									<th style="width: 6.5%">序号</th>
									<th style="width: 9%">站名</th>
									<th style="width: 3%">路局</th>
									<th style="width: 13%">到达时间</th>
									<th style="width: 13%">出发时间</th>
									<th style="width: 5%">停留<br>时间
									</th>
									<th style="width: 5%">到达运<br>行天数
									</th>
									<th style="width: 5%">出发运<br>行天数
									</th>
									<th style="width: 5%">股道</th>
								</tr>
							</thead>
							<tbody style="padding: 0">
								<tr style="padding: 0">
									<td colspan="9" style="padding: 0">
										<div id="allTimes_table"
											style="height: 350px; overflow-y: auto;">
											<table class="table table-bordered table-striped table-hover">
												<tbody data-bind="foreach: times">
													<tr>
														<td style="width: 7.5%" align="center"
															data-bind=" text: $index() + 1"></td>
														<td style="width: 10%"
															data-bind="text: stnName, attr:{title: stnName}"></td>
														<td style="width: 5%" align="center"
															data-bind="text: bureauShortName,attr:{title: bureauShortName}"></td>
														<td style="width: 15%" align="center"
															data-bind="text: arrTime,attr:{title: arrTime}"></td>
														<td style="width: 15%" align="center"
															data-bind="text: dptTime,attr:{title: dptTime}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: stepStr,attr:{title: stepStr}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: arrRunDays,attr:{title: arrRunDays}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: runDays,attr:{title: runDays}"></td>
														<td style="width: 7%" align="center"
															data-bind="text: trackName,attr:{title: trackName}"></td>
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
	 -->

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
</html>
