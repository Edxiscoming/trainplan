<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>差别计划</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="${basePath }/assets/css/datepicker.css" rel="stylesheet">
<link href="${basePath }/assets/easyui/themes/default/easyui.css"
	rel="stylesheet">
<link href="${basePath }/assets/easyui/themes/icon.css" rel="stylesheet">
<link href="${basePath }/assets/css/cross/custom-bootstrap.css"
	rel="stylesheet">

<link rel="stylesheet" type="text/css" media="screen"
	href="${basePath }/assets/css/cross/custom-bootstrap.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="${basePath }/assets/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="${basePath }/assets/css/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="${basePath }/assets/css/style.css">

<link href="${basePath }/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="${basePath }/assets/css/font-awesome.min.css" />

<!-- Custom styles for this template -->
<link href="${basePath }/assets/css/cross/cross.css" rel="stylesheet">
<link href="${basePath }/assets/css/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen"
	href="${basePath }/assets/css/rightmenu.css">
<link rel="stylesheet" type="text/css" media="screen"
	href="${basePath }/assets/css/jquery.autocomplete.css">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<script type="text/javascript">
	var basePath = "${basePath }";
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
</style>


</head>
<body class="Iframe_body"
	style="height: 100%; margin: -9px -6px 0px -3px; width: 100%">
	<input type="hidden" id="basePath" value="${basePath }" />
	<input type="hidden" id="bureauShortName"
		value="${user.bureauShortName }" />
	<div id="div_searchForm" class="row" style="margin: -15px 0 10px 0;">
		<form class="form-horizontal" role="form">
			<div class="row" style="margin: 15px 0 10px 0;">
				<!-- 
				<div class="pull-left" style="margin-left: 10px">
					<label for="exampleInputEmail5" style="margin-top: 6px;">担当局:&nbsp;</label>
					<select id="plan_bureaus"
						style="display: inline-block; width: 50px;" class="form-control"
						data-bind="options:bureaus(), value: bureaus(), optionsText: 'name', optionsValue:'id'"></select>
				</div>
			 -->
				<div class="pull-left" style="margin-left: 10px">
					<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 6px;"> 路局:&nbsp;</label>
					<select id="plan_pass_bureaus"
						style="padding:4px 12px; ;width:70px;  margin-top: 3px" class="form-control"
						data-bind="options:bureaus(), value: bureaus(), optionsText: 'name', optionsValue:'id'"></select>
				</div>
				<div class="pull-left">
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-left: 12px; margin-top: 6px;"> 日期1:&nbsp;</label> <input
						type="text" class="form-control"
						style="padding: 4px 12px; width: 95px; margin-top: 3px"
						placeholder="" id="runplan_input_startDate1" name="startDate1"
						data-bind="value: planStartDate1()" />
				</div>
				<div class="pull-left">
					<label for="exampleInputEmail3" class="control-label pull-left"
						style="margin-left: 12px; margin-top: 6px;"> 日期2:&nbsp;</label> <input
						type="text" class="form-control"
						style="padding: 4px 12px; width: 95px; margin-top: 3px"
						placeholder="" id="runplan_input_startDate2" name="startDate2"
						data-bind="value: planStartDate2()" />
				</div>
				<a type="button" style="margin-top: 3px; margin-left: 15px"
					class="btn btn-success btn-input" data-toggle="modal"
					data-target="#" data-bind="click: loadCbPlans"><i
					class="fa fa-search"></i>查询</a>
			</div>
		</form>
	</div>

	<div id="div_crossDetailInfo" class="row"
		style="margin: 10px 0px 0px 10px;">
		<div class="pull-left" style="width: 50%;" id="leftBox">
			<!--分栏框开始-->
			<div id="div_crossDetailInfo_trainStnTable" style="overflow: auto;"
				class="panel panel-default">
				<div class="panel-body">
					<span id="qcb"></span>（差别计划）：
					<table class="table table-bordered table-striped table-hover"
						style="margin-left: 2px; margin-right: 5px;">
						<thead>
							<tr style="height: 26px">
								<th class="text-center"
									style="vertical-align: middle; width: 20px">序号</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">车次</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">来源</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">开行状态</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">运行方式</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发时间</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到时间</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: qCbPlans">
							<tr
								data-bind="style:{color: $parent.qCbPlans() != null && $parent.qCbPlans().trainNbr == trainNbr ? 'blue':''}">
								<td data-bind="text: ($index() + 1)" class="text-center"></td>
								<td data-bind="text: trainNbr, attr:{title: trainNbr}"
									class="text-center"></td>
								<td data-bind="text: sourceType, attr:{title: sourceType}"
									class="text-center"></td>
								<td data-bind="text: spareFlag, attr:{title: spareFlag}"
									class="text-center"></td>
								<td data-bind="text: yxType, attr:{title: yxType}"
									class="text-center"></td>
								<td data-bind="text: startStn, attr:{title: startStn}"
									class="text-center"></td>
								<td data-bind="text: startTime, attr:{title: startTime}"
									class="text-center"></td>
								<td data-bind="text: endStn, attr:{title: endStn}"
									class="text-center"></td>
								<td data-bind="text: endTime, attr:{title: endTime}"
									class="text-center"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="blankBox"></div>
		<div class="pull-left" style="width: 50%;" id="leftBox">
			<div id="div_crossDetailInfo_trainStnTable" style="overflow: auto; height: 270px;"
				class="panel panel-default">
				<div class="panel-body">
					<span id="hcb"></span>（差别计划）：
					<table class="table table-bordered table-striped table-hover"
						style="margin-left: 2px; margin-right: 5px;">
						<thead>
							<tr style="height: 26px">
								<th class="text-center"
									style="vertical-align: middle; width: 20px">序号</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">车次</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">来源</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">开行状态</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">运行方式</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发时间</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到时间</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: hCbPlans">
							<tr
								data-bind="style:{color: $parent.hCbPlans() != null && $parent.hCbPlans().trainNbr == trainNbr ? 'blue':''}">
								<td data-bind="text: ($index() + 1)" class="text-center"></td>
								<td data-bind="text: trainNbr, attr:{title: trainNbr}"
									class="text-center"></td>
								<td data-bind="text: sourceType, attr:{title: sourceType}"
									class="text-center"></td>
								<td data-bind="text: spareFlag, attr:{title: spareFlag}"
									class="text-center"></td>
								<td data-bind="text: yxType, attr:{title: yxType}"
									class="text-center"></td>
								<td data-bind="text: startStn, attr:{title: startStn}"
									class="text-center"></td>
								<td data-bind="text: startTime, attr:{title: startTime}"
									class="text-center"></td>
								<td data-bind="text: endStn, attr:{title: endStn}"
									class="text-center"></td>
								<td data-bind="text: endTime, attr:{title: endTime}"
									class="text-center"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<div id="div_crossDetailInfo" class="row"
		style="margin: 10px 0px 0px 10px;">
		<div class="pull-left" style="width: 50%;" id="leftBox">
			<div id="div_crossDetailInfo_trainStnTable" style="overflow: auto; height: 450px;"
				class="panel panel-default">
				<div class="panel-body">
					<span id="qjj"></span>（相同计划）：
					<table class="table table-bordered table-striped table-hover"
						style="margin-left: 2px; margin-right: 5px;">
						<thead>
							<tr style="height: 26px">
								<th class="text-center"
									style="vertical-align: middle; width: 20px">序号</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">车次</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">来源</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">开行状态</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">运行方式</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发时间</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到时间</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: qPlans">
							<tr
								data-bind="style:{color: $parent.qCbPlans() != null && $parent.qCbPlans().trainNbr == trainNbr ? 'blue':''}">
								<td data-bind="text: ($index() + 1)" class="text-center"></td>
								<td data-bind="text: trainNbr, attr:{title: trainNbr}"
									class="text-center"></td>
								<td data-bind="text: sourceType, attr:{title: sourceType}"
									class="text-center"></td>
								<td data-bind="text: spareFlag, attr:{title: spareFlag}"
									class="text-center"></td>
								<td data-bind="text: yxType, attr:{title: yxType}"
									class="text-center"></td>
								<td data-bind="text: startStn, attr:{title: startStn}"
									class="text-center"></td>
								<td data-bind="text: startTime, attr:{title: startTime}"
									class="text-center"></td>
								<td data-bind="text: endStn, attr:{title: endStn}"
									class="text-center"></td>
								<td data-bind="text: endTime, attr:{title: endTime}"
									class="text-center"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="blankBox"></div>
		<div class="pull-left" style="width: 50%;" id="leftBox">
			<div id="div_crossDetailInfo_trainStnTable" style="overflow: auto; height: 450px;"
				class="panel panel-default">
				<div class="panel-body">
					<span id="hjj"></span>（相同计划）：
					<table class="table table-bordered table-striped table-hover"
						style="margin-left: 2px; margin-right: 5px;">
						<thead>
							<tr style="height: 26px">
								<th class="text-center"
									style="vertical-align: middle; width: 20px">序号</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">车次</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">来源</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">开行状态</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">运行方式</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发时间</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到时间</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: hPlans">
							<tr
								data-bind="style:{color: $parent.hCbPlans() != null && $parent.hCbPlans().trainNbr == trainNbr ? 'blue':''}">
								<td data-bind="text: ($index() + 1)" class="text-center"></td>
								<td data-bind="text: trainNbr, attr:{title: trainNbr}"
									class="text-center"></td>
								<td data-bind="text: sourceType, attr:{title: sourceType}"
									class="text-center"></td>
								<td data-bind="text: spareFlag, attr:{title: spareFlag}"
									class="text-center"></td>
								<td data-bind="text: yxType, attr:{title: yxType}"
									class="text-center"></td>
								<td data-bind="text: startStn, attr:{title: startStn}"
									class="text-center"></td>
								<td data-bind="text: startTime, attr:{title: startTime}"
									class="text-center"></td>
								<td data-bind="text: endStn, attr:{title: endStn}"
									class="text-center"></td>
								<td data-bind="text: endTime, attr:{title: endTime}"
									class="text-center"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!--  
	<div class="pull-left" style="margin: 10px 0px 0px 10px; width: 100%;"
		id="leftBox">
		<div id="div_crossDetailInfo_trainStnTable"
			style="overflow: auto; height: 450px;" class="panel panel-default">
			<div class="panel-body">
				<span id="cb"></span>（相同计划）：
				<table class="table table-bordered table-striped table-hover"
					style="margin-left: 2px; margin-right: 5px;">
					<thead>
						<tr style="height: 26px">
								<th class="text-center"
									style="vertical-align: middle; width: 20px">序号</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">车次</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">来源</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">开行状态</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">运行方式</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">始发时间</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到站</th>
								<th class="text-center"
									style="vertical-align: middle; width: 20px">终到时间</th>
						</tr>
					</thead>
					<tbody data-bind="foreach: plans">
						<tr
							data-bind="style:{color: $parent.qCbPlans() != null && $parent.qCbPlans().trainNbr == trainNbr ? 'blue':''}">
							<td data-bind="text: ($index() + 1)" class="text-center"></td>
							<td data-bind="text: trainNbr, attr:{title: trainNbr}"
									class="text-center"></td>
								<td data-bind="text: sourceType, attr:{title: sourceType}"
									class="text-center"></td>
								<td data-bind="text: spareFlag, attr:{title: spareFlag}"
									class="text-center"></td>
								<td data-bind="text: yxType, attr:{title: yxType}"
									class="text-center"></td>
								<td data-bind="text: startStn, attr:{title: startStn}"
									class="text-center"></td>
								<td data-bind="text: startTime, attr:{title: startTime}"
									class="text-center"></td>
								<td data-bind="text: endStn, attr:{title: endStn}"
									class="text-center"></td>
								<td data-bind="text: endTime, attr:{title: endTime}"
									class="text-center"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	 -->
</body>

<script type="text/javascript" src="${basePath }/assets/js/jquery.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/jquery.browser.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/trainplan/jquery.autocomplete.js"></script>
<script type="text/javascript" src="${basePath }/assets/js/html5.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/respond.min.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${basePath }/assets/js/resizable.js"></script>
<script type="text/javascript" src="${basePath }/assets/js/knockout.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/ajaxfileupload.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/trainplan/knockout.pagemodle.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/trainplan/cb_plan.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/datepicker.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/trainplan/common.js"></script>
<script type="text/javascript"
	src="${basePath }/assets/js/respond.min.js"></script>
<script src="${basePath }/assets/js/moment.min.js"></script>
<script src="${basePath }/assets/lib/fishcomponent.js"></script>

<script src="${basePath }/assets/js/trainplan/util/fishcomponent.js"></script>
<script type="text/javascript">
	$(function() {
		resize("leftBox", "rightBox", "670", "730");
	});
</script>

</html>
