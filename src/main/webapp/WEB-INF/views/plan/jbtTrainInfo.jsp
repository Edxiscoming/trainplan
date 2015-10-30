<%@page import="javax.sound.midi.SysexMessage"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}
String basePath = request.getContextPath();
Object trainlineId =  request.getAttribute("trainLineId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--font-awesome-->
<link  type="text/css" rel="stylesheet" href="<%=basePath%>/assets/css/font-awesome.min.css"/>
<!-- Custom styles for this template -->
<link href="<%=basePath%>/assets/js/jquery-slider/css/jquery.sidr.light.css" rel="stylesheet">
<link href="<%=basePath%>/assets/css/style.css" rel="stylesheet" type="text/css" media="screen">
<link href="<%=basePath%>/assets/css/menu.css" rel="stylesheet" type="text/css" media="screen">

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

<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-slider/jquery.sidr.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-ui-1.10.4.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/moment.min.js"></script>

<%--JQuery UI --%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/js/autocomplete/jquery-ui-1.10.4/css/jquery-ui-1.10.4.custom.css"/>
<script src="<%=basePath%>/assets/js/autocomplete/jquery-ui-1.10.4/js/jquery-ui-1.10.4.custom.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/plan/jbtTrainInfo.js"></script>

<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>

<title>车次信息</title>
</head>
<body>
<!-- 页面左侧部分 -->
  <div style="margin-left:5px; width:500px;  position:absolute;">
<!--     分栏框开始 -->
    <div class="panel panel-default">
		<div class="row" style="padding-top:10px;padding-bottom:10px;">
		  <form class="form-horizontal" role="form">
		  	<div class="row">
		  		<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;车次:&nbsp;</label>
			    <div class="pull-left">
			        <input type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().trainNbr">
			    </div>
		  		<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;方案:&nbsp;</label>
<!-- 			    <label for="exampleInputEmail2" class="control-label pull-left" style="margin-top:5px;margin-left:10px"> -->
<!-- 								方案:&nbsp;</label>  -->
				<div class="pull-left">
					<select style="padding:1px 6px; width:230px" id="input_cross_chart_id"
						class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
					</select>
				</div>
		  		<a type="button" href="#" class="btn btn-success" data-bind="click : queryTrainLineData" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
		    </div>
		  </form>
		</div>
	
<!-- 		列车table -->
		<div class="row">
			<div class="table-responsive table-hover" style="overflow: auto">
				<table
					class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 30px">序号</th>
							<th style="width: 80px">车次</th>
							<th style="width: 200px">始发站</th>
							<th style="width: 70px">始发时间</th>
							<th style="width: 200px">终到站</th>
							<th style="width: 70px">终到时间</th>
						</tr>
					</thead>
					<tbody data-bind="foreach: trainLines">
						<tr  data-bind="click: $parent.selectTrainLine, style:{color: $parent.currentTrain() != null && $parent.currentTrain().id == id ? 'blue':''}">
							<td align="center" data-bind=" text: $index() + 1"></td>
							<td data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
							<td data-bind="text: sourceNodeName"></td>
							<td data-bind="text: sourceTime , attr:{title: sourceTime }"></td>
							<td data-bind="text: targetNodeName , attr:{title: targetNodeName }"></td>
							<td data-bind="text: targetTime , attr:{title: targetTime }"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
<!-- 	列车table end -->
	</div>
</div> 
<!-- 页面左侧部分 end -->	
	
	
	
	<input type="hidden" id="trainlineId" value="<%=trainlineId%>"/>
<!-- 页面右侧部分 -->
  <!--列车开行计划-->
  <div style="margin-left:510px; float:left;"> 
    <!--分栏框开始-->
    <div class="panel panel-default">
      
      <!--panle-heading-->
      <div class="panel-body" style="padding:5px 5px;">
		<!-- 运行线详细信息 -->
		<form class="form-inline" style="margin-bottom:10px;">
			 <div class="checkbox">
   				 <label><i class="fa fa-table"></i>运行线信息</label>
 			 </div>
 			 <div class="form-group" style="margin-left:13px;margin-bottom:0;">
				<div class="pull-left" style="margin-left:3px;">
			        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveTrainLine, attr:{disabled:tranlineSaveBtnDisabled}"><i class="fa fa-floppy-o"></i> 保存</button>
			    </div>
	  		</div>
		</form>
		<div class="row" style="width: 100%; margin-top: 15px;margin-bottom: 5px;height:250px; overflow-y:auto;">
			<div class="row">
				<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">车&nbsp;&nbsp;&nbsp;次:&nbsp;</label>
							<div class="pull-left">
								<p>
									<input class="form-control" data-bind="value: currentTrain().trainNbr"/>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">类&nbsp;&nbsp;&nbsp;型:&nbsp;</label>
							<div class="pull-left">
								<p>
<!-- 									<select style="width: 100px" class="form-control" data-bind="options:typeNameOptions, value: typeName, optionsText: 'text'"></select></td> -->
									<select style="width: 230px" class="form-control" data-bind="options:typeNameOptions(), value: trainType, optionsText: 'typeName'"></select></td>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">有效期起始日期:&nbsp;</label>
							<div class="pull-left">
								<p style="text-align: left;">
<!-- 									<input type="text" class="form-control" style="padding:4px 12px; ;width:105px;" placeholder="" id="executionStatrDate"  name="endDate"/> -->
									<input id="executionStatrDate" class="form-control" style="width:130px"  placeholder="" data-bind="value: currentTrain().sourceTimeActivity"/>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">有效期截至日期:&nbsp;</label>
							<div class="pull-left">
								<p style="text-align: left;">
									<input id="executionEndDate" class="form-control" style="width:130px"  placeholder="" data-bind="value: currentTrain().targetTimeActivity"/>
								</p>
							</div>
						</div>
					</div>
			  	</div>
			  	<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">始发站:&nbsp;</label>
							<div class="pull-left">
								<p>
									<label data-bind="text: currentTrain().sourceNodeName" class="text-primary"></label>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">始发时间:&nbsp;</label>
							<div class="pull-left">
								<p>
									<label data-bind="text: currentTrain().sourceTime" class="text-primary"></label>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">始发局:&nbsp;</label>
							<div class="pull-left">
								<p>
									<label data-bind="text: currentTrain().sourceBureauShortName" class="text-primary"></label>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">历&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时:&nbsp;</label>
							<div class="pull-left" id="widthvehicle">
								<p style="text-align: left">
									<label data-bind="text: currentTrain().lastTimeText" class="text-primary"></label>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">铁路线:&nbsp;</label>
							<div class="pull-left">
								<p style="text-align: left;">
									<select style="width: 100px" class="form-control" data-bind="options:highSpeedOptions, value: highSpeed, optionsText: 'text'"></select></td>
									
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
					
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">终到站:&nbsp;</label>
							<div class="pull-left">
								<p>
									<label data-bind="text: currentTrain().targetNodeName" class="text-primary"></label>
								</p>
							</div>
						</div><div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">终到时间:&nbsp;</label>
							<div class="pull-left">
								<p style="text-align: left;">
									<label data-bind="text: currentTrain().targetTime" class="text-primary"></label>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">终到局:&nbsp;</label>
							<div class="pull-left">
								<p style="text-align: left;">
									<label data-bind="text: currentTrain().targetBureauShortName" class="text-primary"></label>
								</p>
							</div>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<label for="exampleInputEmail3" class="control-label pull-left">途经局:</label>
							<div class="pull-left" id="widthvehicle">
								<p style="text-align: left">
									<label data-bind="text: currentTrain().routeBureauShortNames" class="text-primary"></label>
								</p>
							</div>
						</div>
					</div>

				</div>
			</div>
	  	</div>
	  	<!-- 运行线详细信息  end-->
	  	
	  	<!-- 运行线时刻表 -->
		<form class="form-inline" style="margin-bottom:10px;">
			 <div class="checkbox">
   				 <label><i class="fa fa-table"></i>运行线时刻表</label>
 			 </div>
			  <div class="form-group">
			  <select style="width: 110px; " class="form-control" data-bind="options:searchModle().filterOptions, value: searchModle().filterOption, optionsText: 'text',event:{change:timesFilterChange}"></select>			    
			  </div> 
			  <div class="form-group" style="margin-left:13px;margin-bottom:0;">
				<div class="pull-left" style="margin-left:3px;">
			        <button type="button" class="btn btn-success btn-xs" data-bind="click: saveTrainLineTimes, attr:{disabled:timesDivBtnDisabled}"><i class="fa fa-floppy-o"></i> 保存</button>
			        <button type="button" class="btn btn-success btn-xs" data-bind="click: up, attr:{disabled:timesDivBtnDisabled}"><i class="fa fa-arrow-up"></i> 上移</button>
			        <button type="button" class="btn btn-success btn-xs" data-bind="click: down, attr:{disabled:timesDivBtnDisabled}"><i class="fa fa-arrow-down"></i> 下移</button>
			        <button type="button" class="btn btn-success btn-xs" data-bind="click: insertTrainLineStnRow, attr:{disabled:timesDivBtnDisabled}"><i class="fa fa-plus"></i> 插入行</button>
			        <button type="button" class="btn btn-success btn-xs" data-bind="click: addTrainLineStnRow, attr:{disabled:timesDivBtnDisabled}"><i class="fa fa-plus"></i> 追加行</button>
			        <button type="button" class="btn btn-success btn-xs" data-bind="click: deleteTrainLineStnRow, attr:{disabled:timesDivBtnDisabled}"><i class="fa fa-trash-o"></i> 删除行</button>
          		</div>
	  		</div>
		</form>
        <div id="div_table_trainLineTimes" class="table-responsive table-hover" style="width: 1200px;overflow:auto;">
          <table border="0" class="table table-bordered table-striped table-hover">
            <thead>
              <tr>
                <th style="vertical-align: middle;width:30px;" rowspan="2" class="text-center">序号</th>
                <th style="vertical-align: middle;width:150px" rowspan="2" class="text-center">站名</th>
                <th style="vertical-align: middle;width:30px" rowspan="2" class="text-center">路局</th>
                <th style="vertical-align: middle;width:100px" rowspan="2" class="text-center">到达时间</th>
                <th style="vertical-align: middle;width:100px" rowspan="2" class="text-center">出发时间</th>
                <th style="vertical-align: middle;width:130px" rowspan="2" class="text-center">停时</th>
                <th style="vertical-align: middle;width:120px" colspan="2" class="text-center">运行天数</th>
                <th style="vertical-align: middle;width:220px" colspan="2" class="text-center">车次</th>
                <th style="vertical-align: middle;width:120px" rowspan="2" class="text-center">股道</th>
                <th style="vertical-align: middle;width:70px" rowspan="2" class="text-center">站台</th>
                <th style="vertical-align: middle;width:250px" rowspan="2" class="text-center">作业</th>
                <th style="vertical-align: middle;width:100px" rowspan="2" class="text-center">客运营业</th>
              </tr>
              <tr>
                <th style="vertical-align: middle;width:60px" class="text-center">到达</th>
                <th style="vertical-align: middle;width:60px" class="text-center">出发</th>
                <th style="vertical-align: middle;width:110px" class="text-center">到达</th>
                <th style="vertical-align: middle;width:110px" class="text-center">出发</th>
              </tr>
            </thead>
			<tbody data-bind="foreach: trainLineTimes">
              <tr data-bind="style:{color: $root.currentTrainLineTimesStn().hiddenIndex == hiddenIndex ? 'blue':''}">
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, text: ($index() + 1)"></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: stnNameTemp}">
                	<input name="input_stnName" class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, value: stnNameTemp"/></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, text:bureauShortName"></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: sourceTime}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec,
                	 value: sourceTime, event: {blur: sourceTimeOnblur}, style:{color:timeInputColor}"/>
                	<span data-bind="validationMessage: sourceTime"></span>
                </td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: targetTime}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, 
                		value: targetTime, event: {blur:targetTimeOnblur}, style:{color:timeInputColor}"/>
                	<span data-bind="validationMessage: targetTime"></span>
                </td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec,text:stepStr, attr:{title: stepStr}"></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: sourceRunDays}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, value: sourceRunDays, event: {blur: sourceRunDaysOnblur}"/></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: targetRunDays}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, value: targetRunDays, event: {blur: targetRunDaysOnblur}"/></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: sourceTrainNbr}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, value: sourceTrainNbr, event: {blur: sourceTrainNbrOnblur}"/></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: targetTrainNbr}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, value: targetTrainNbr, event: {blur: targetTrainNbrOnblur}"/></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: trackName}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, value: trackName"/></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: platForm}">
                	<input class="form-control" data-bind="attr:{readonly:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, value: platForm"/></td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec, attr:{title: jobItemsText}">
                	<a href="#"><i class="fa fa-plus-square" data-bind="click: $parent.editJobs, enable:btnEnable" data-toggle="modal" data-target="#editJobsModal"></i></a>
                	<label data-bind="click: $parent.setTrainLineTimesCurrentRec, text: jobItemsText"></label>
                </td>
                <td data-bind="click: $parent.setTrainLineTimesCurrentRec">
                	<select style="width: 70px" class="form-control" data-bind="attr:{disabled:inputReadOnly}, click: $parent.setTrainLineTimesCurrentRec, options:$parent.searchModle().kyyyOptions, value: kyyy, optionsText: 'text'"></select></td>
                
              </tr>
            </tbody>
          </table>
        </div>
	  	<!-- 运行线时刻表 end -->
      </div>
      <!--panel-body--> 
      
    </div>
    
    <!--分栏框结束--> 
  </div>
  <!--列车开行计划 end--> 
  
<!-- 页面右侧部分  end -->






<!--弹出框-->
<div id="editJobsModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">调整站点作业</h4>
      </div>
      
      <!--panel-heading-->
      <div class="panel-body row">
        <div class="row">
				<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="车底转头"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底转头</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="机车换乘挂"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">机车换乘挂</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="司机换班"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">司机换班</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="车底立折"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底立折</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input name="jobCheckbox" type="checkbox" value="车底下线"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底下线</label>
						</div>
					</div>
			  	</div>
			  	<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="到达车底"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">到达车底</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="车底上线"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">车底上线</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="入库"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">入库</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="出库"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">出库</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="运转车长"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">运转车长</label>
						</div>
					</div>
				</div>
				<div class=" col-md-4 col-sm-4 col-xs-4 " style="margin: 10px 0 0 0;">
					<div class="pull-left">
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="客运乘务"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">客运乘务</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="职工乘降"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">职工乘降</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="上水站"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">上水站</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="技术停点"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">技术停点</label>
						</div>
						<div class="row" style="margin: 5px 0 0px 0;">
							<div class="pull-left">
						        <input type="checkbox" name="jobCheckbox" value="分界口"/>
							</div>
							<label style="margin-left:5px;" for="exampleInputEmail3" class="control-label pull-left">分界口</label>
						</div>
					</div>

				</div>
			</div>
		</div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal" data-bind="click:saveJobs">确定</button>
        <button type="button" class="btn btn-info" data-bind="click:setJobCheckBoxVal">重置</button>
        <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content --> 
  </div>
  <!-- /.modal-dialog --> 
</div>
<!--弹出框--> 




</body>
</html>