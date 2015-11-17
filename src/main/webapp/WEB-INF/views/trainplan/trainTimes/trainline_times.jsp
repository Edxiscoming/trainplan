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
Object trainId =  request.getAttribute("trainId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>时刻表</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body >


<input type="hidden" id="trainId" value="<%=trainId%>"/>
<!--分栏框开始-->
<div class="row" >
  <div >
    <!--分栏框开始-->
    <!--详情时刻表--> 
	<div id="cross_train_time_dlg" class="easyui-dialog" title=""
		data-options="iconCls:'icon-save'"> 
			      <!--panle-heading-->
			      <div class="panel-body" style="padding:10px;margin-right:10px;">
				       <ul class="nav nav-tabs" >
						  <li class="active"><a style="padding:3px 10px;" href="#simpleTimes" data-toggle="tab">简点</a></li> 
						  <li><a style="padding:3px 10px;" href="#allTimes" data-toggle="tab">详点</a></li> 
						  <li style="float:right" ><span style="font: -webkit-small-control;"></span></li>
						</ul> 
						<!-- Tab panes -->
						<div class="tab-content" >
						  <div class="tab-pane active" id="simpleTimes" > 
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead>
							        <tr>
								          <th style="vertical-align: middle;width:3%"  rowspan="2";>序号</th>
						                  <th style="vertical-align: middle;width:12%" rowspan="2";>站名</th>
						                  <th style="vertical-align: middle;width:5%"  rowspan="2";>路局</th>
						                  <th style="vertical-align: middle;width:12%" rowspan="2";>到达时间</th>
						                  <th style="vertical-align: middle;width:12%" rowspan="2";>出发时间</th>
						                  <th style="vertical-align: middle;width:6%" rowspan="2";>停时</th>
						                  <th style="vertical-align: middle;width:12%" colspan="2";>天数</th> 
						                  <th style="vertical-align: middle;width:14%" colspan="2">车次</th>
						                  <th style="vertical-align: middle;width:6%" rowspan="2">股道</th>  
						                  <th style="vertical-align: middle;width:5%" rowspan="2">站台</th> 
						                  <th style="vertical-align: middle;width:8%" rowspan="2";>作业</th> 
						                  <th style="vertical-align: middle;width:5%"  rowspan="2";>客运</th> 
					                 </tr>
					                <tr>
						                <th style="vertical-align: middle;width:6%" class="text-center">到达</th>
						                <th style="vertical-align: middle;width:6%" class="text-center">出发</th>
						                <th style="vertical-align: middle;width:7%" class="text-center">到达</th>
						                <th style="vertical-align: middle;width:7%" class="text-center">出发</th>
						            </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
<!-- 												 <div id="simpleTimes_table">  -->
												 <div id="simpleTimes_table" style="height: 500px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" >
														 <tbody data-bind="foreach: simpleTimes">
												           <tr data-bind="visible: stationFlag != 'BTZ'">  
															<td style="width:3%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:12%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:12%" align="center" data-bind="text: sourceTime, attr:{title: sourceTime}"></td>
															<td style="width:12%" align="center" data-bind="text: targetTime, attr:{title: targetTime}"></td>
															<td style="width:6%" align="center" data-bind="text: stepStr, attr:{title: stepStr}"></td>
															<td style="width:6%" align="center" data-bind="text: sourceDay"></td>
															<td style="width:6%" align="center" data-bind="text: runDays"></td>
															<td style="width:7%" align="center" data-bind="text: sourceTrainNbr, attr:{title: sourceTrainNbr}"></td>
															<td style="width:7%" align="center" data-bind="text: targetTrainNbr, attr:{title: targetTrainNbr}"></td>
															<td style="width:6%" align="center" data-bind="text: trackName, attr:{title: trackName}"></td>
															<td style="width:5%" align="center" data-bind="text: platForm"></td>
															<td style="width:8%" align="center" data-bind="text: jobsText, attr:{title: jobsText}"></td>
															<td style="width:5%" align="center" data-bind="text: kyyy"></td>
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
								          <th style="vertical-align: middle;width:3%"  rowspan="2";>序号</th>
						                  <th style="vertical-align: middle;width:12%" rowspan="2";>站名</th>
						                  <th style="vertical-align: middle;width:5%"  rowspan="2";>路局</th>
						                  <th style="vertical-align: middle;width:12%" rowspan="2";>到达时间</th>
						                  <th style="vertical-align: middle;width:12%" rowspan="2";>出发时间</th>
						                  <th style="vertical-align: middle;width:6%" rowspan="2";>停时</th>
						                  <th style="vertical-align: middle;width:12%" colspan="2";>天数</th> 
						                  <th style="vertical-align: middle;width:14%" colspan="2">车次</th>
						                  <th style="vertical-align: middle;width:6%" rowspan="2">股道</th>  
						                  <th style="vertical-align: middle;width:5%" rowspan="2">站台</th> 
						                  <th style="vertical-align: middle;width:6%" rowspan="2";>作业</th> 
						                  <th style="vertical-align: middle;width:5%"  rowspan="2";>客运</th> 
						                 </tr>
						                <tr>
							                <th style="vertical-align: middle;width:6%" class="text-center">到达</th>
							                <th style="vertical-align: middle;width:6%" class="text-center">出发</th>
							                <th style="vertical-align: middle;width:7%" class="text-center">到达</th>
							                <th style="vertical-align: middle;width:7%" class="text-center">出发</th>
							              </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="allTimes_table" style="height: 500px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" > 
														 <tbody data-bind="foreach: times">
												           <tr>  
															<td style="width:3%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:12%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:12%" align="center" data-bind="text: sourceTime, attr:{title: sourceTime}"></td>
															<td style="width:12%" align="center" data-bind="text: targetTime, attr:{title: targetTime}"></td>
															<td style="width:6%" align="center" data-bind="text: stepStr, attr:{title: stepStr}"></td>
															<td style="width:6%" align="center" data-bind="text: sourceDay"></td>
															<td style="width:6%" align="center" data-bind="text: runDays"></td>
															<td style="width:7%" align="center" data-bind="text: sourceTrainNbr, attr:{title: sourceTrainNbr}"></td>
															<td style="width:7%" align="center" data-bind="text: targetTrainNbr, attr:{title: targetTrainNbr}"></td>
															<td style="width:6%" align="center" data-bind="text: trackName, attr:{title: trackName}"></td>
															<td style="width:5%" align="center" data-bind="text: platForm"></td>
															<td style="width:8%" align="center" data-bind="text: jobsText, attr:{title: jobsText}"></td>
															<td style="width:5%" align="center" data-bind="text: kyyy"></td>
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
  
  
</div>

<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.ui.widget.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-ui-1.10.4.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/moment.min.js"></script>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/cross.js"></script> --%>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/trainTimes/trainline_times.js"></script>  
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>  --%>
</body>
</html>

