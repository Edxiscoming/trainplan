<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
String  currentUserBureauName = user.getBureauShortName();
String basePath = request.getContextPath();
String currentUserBureau = user.getBureau();
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>审核次日客车运行线</title>
    <link type="text/css" href="${ctx}/assets/css/datepicker.css" rel="stylesheet">
    <link type="text/css" href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/font-awesome.min.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/minified/jquery-ui.min.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/style.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/easyui/themes/default/easyui.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/easyui/themes/icon.css" rel="stylesheet" />
    
    <script type="text/javascript">
	var currentUserBureau = "<%=currentUserBureau %>";
	var currentUserBureauName ="<%=currentUserBureauName %>";
	</script>
	
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/highcharts.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
<script src="<%=basePath %>/assets/lib/fishcomponent.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script> --%>
<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script> 
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_rightmenu.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/planline_check.js"></script>
    <script type="text/javascript">
var basePath = "<%=basePath %>";
var currentUserBureau = "<%=currentUserBureau %>";
</script>
    <script type="text/javascript">
        var isHighLine = <c:out value="${train_type}"></c:out>
    </script>
    
    <style type="text/css">
    .table thead > tr > th{
    padding: 0px 2px;
    }
    .modal-dialog{
      width:900px;
    }
    .table td,.table th {
	text-align: center;
    }
    </style>
    <jsp:include page="/assets/commonpage/include-dwr-script.jsp" flush="true" /> 
</head>
<body class="Iframe_body">
<%-- <ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置:</span>
    <c:if test="${train_type==0}">
        <li><a href="#">发布开行计划 -> 审核既有开行计划</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">发布开行计划 -> 审核高铁开行计划</a></li>
    </c:if>
</ol> --%>

<!--左右分开-->
<div class="modal fade" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none; ">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">落成记录</h4>
      </div>   
      <!--panel-heading-->
      <div class="panel-body row">

         <div class="table-responsive">
				<table class="table table-bordered table-striped table-hover">				
					<thead>
						<tr style="height: 25px">
						    <th>序号</th>
							<th style="width: 5%">开行日期</th>
							<th style="width: 5%">车次</th>
							<th style="width: 5%">所属交路</th>
							<th style="width: 5%">落成局</th>
							<th style="width: 10%">落成时间</th>
							<th style="width: 5%">落成人</th>
							<th style="width: 5%">岗位</th>
							<th style="width: 450px">列车ID</th>
							<th style="width: 450px">前续ID</th>
							<th style="width: 450px">后续ID</th>
						</tr>
					</thead>
				</table>
				<table class="table table-bordered table-striped table-hover" style="border: 0;">
				    <tbody>
							<tr>
							</tr>
					</tbody>
				</table>
			</div>

      </div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="configBtn" data-dismiss="modal">提交</button>
        <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content --> 
  </div>
  <!-- /.modal-dialog --> 
</div>


<section class="mainContent">
    <div class="row">
        <div class="col-xs-12 col-md-12 col-lg-12">
            <div class="row"  >
                <div class="col-xs-12 col-md-12 col-lg-12 paddingtop5 marginbottom5">
                    <form class="form-inline" role="form">
                        <div class="input-group" style="width: 100%;margin-left: -17px;margin-top: -12px;">
                        <!-- 
                        <label class="control-label paddingleftright5">路局:</label>
                        <div class="pull-left">
                            <select style="width: 70px; margin-right: 10px; border-radius: 4px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: searchModle().bureau " disabled></select>
                         </div>
                          -->
                         <label class="control-label paddingleftright5">日期:</label>
                         <div class="pull-left">
                            <input type="text" class="form-control" style="width: 100px; margin-right: 10px; border-radius: 4px" placeholder="请选择日期" id="date_selector"/>
                         </div>

                            <label class="control-label paddingleftright5">车次:</label>
                             <div class="pull-left">
                            <input type="text" class="form-control" style="width: 100px; margin-right: 10px; border-radius: 4px"  id="train_nbr">
                            </div> 
                            
                            <label class="control-label paddingleftright5">担当局:</label>
                            <div class="pull-left">
                            <select id="token_veh_bureau_nbr" class="form-control" style="width: 110px; margin-right: 10px; border-radius: 4px">
                                <option value="0">全部</option>
                                <option value="1">本局</option>
                                <option value="2">外局</option>
                            </select>
                            </div>
                            
                            <label class="control-label paddingleftright5">运行方式:</label>
                            <div class="pull-left">
                            <select id="train_type" class="form-control" style="width: 110px; margin-right: 10px; border-radius: 4px">
                                <option value="0">全部</option>
                                <option value="1">始发终到</option>
                                <option value="2">始发交出</option>
                                <option value="3">接入终到</option>
                                <option value="4">接入交出</option>
                            </select>
                            </div>
                            <label class="control-label paddingleftright5">落成状态:</label>
                            <div class="pull-left">
                            <select id="sentFlag" class="form-control" style="width: 110px; margin-right: 10px; border-radius: 4px">
                                <option value="0">全部</option>
                                <option value="1">未落成</option>
                                <option value="2">已落成</option>
                                <option value="3">调整</option>
                            </select>
                            </div>
                            
                            <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: search"><i class="fa fa-search"></i>查询</button>
							<button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: autoCheck"><i class="fa fa-check-square-o"></i>校验</button>
<!--                             <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: checkLev1, enable: canCheckLev1"><i class="fa fa-eye"></i>审核</button> -->
                            <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: completion1"><i class="fa fa-arrow-circle-o-down"></i>落成</button>
                            <!-- <a  type="button" class="btn btn-primary btn-input" data-toggle="modal" data-target="#" id="cross_train_save" data-bind="click: completionLog"><i class="fa fa-clock-o"></i>落成记录</a> -->
							
							                            <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: showallcreat" ><i class="fa fa-sign-out"></i>生成命令</button>
							<!-- <input type="hidden" id="autoCheckall" data-bind="click: autoCheck"></input> -->
<%--                             <shiro:hasPermission name="JHPT.RJH.KDSP"><!-- 客运调度审批 --> --%>
<%--                             </shiro:hasPermission> --%>
<%--                             <shiro:hasPermission name="JHPT.RJH.ZBZRSP"><!-- 值班主任审批权限 --> --%>
<!--                             <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: checkLev2, enable: canCheckLev2"><i class="fa fa-eye"></i>审核</button> -->
<%--                             </shiro:hasPermission> --%>

                            <label class="control-label text-center pull-right paddingtop5">
                            	<!-- 
						                            存在 <a style="color: #ff0000" href="#" data-bind="text: paramModel().unknownRunLine, click: openOuterTrainLine"></a>
								条冗余运行线<span data-bind="text: checkStatus"></span>
								-->
								重复列车：<a style="color: #ff0000;font-size:14px" href="#" data-bind="text: isTrainCfMatch, click: openTrainCf"></a>
                            </label>
                            <div class="pull-right" style="margin-right:10px;">
								<input style="margin-top:7px" type="checkbox" class="pull-left" class="form-control" data-bind="checked: isShowCrossDetailInfo, event:{change: showChatPia}"> 
								<label style="margin-top:5px" for="exampleInputEmail5" class="control-label pull-left"><b>显示数据图</b></label>  
						 	</div>
                        </div>

                    </form>
                </div>
            </div>
    
            <div class="panel-body" id="body_right"
					style="padding: 5px 0 0 0;margin: 0px">
            
            <div class="row">
                <div class="margin panel panel-default" style="padding:5px;">
                    <div class="table-responsive">
                        <table id="plan_table" class="table table-bordered table-striped table-hover tableradius">
                            <thead>
                            <tr>
<%--                                 <shiro:hasPermission name="JHPT.RJH.KDSP"><!-- 客运调度审批 --> --%>
                                <th style="width:40px;padding:4px 0;" rowspan="2" class="text-center"><input class="checkbox-inline" type="checkbox" data-bind="event:{change:$root.selectAllLev1}, checked: $root.allBtn"/></th>
                                <th  rowspan="2" style="vertical-align: middle;width:42px;">序号</th>
                                <th rowspan="2" style="width:100px;vertical-align: middle">车次</th>
                                <th style="width:;vertical-align: middle" rowspan="2">来源</th>
                                <th style="width:60px;padding:4px 0;vertical-align: middle" rowspan="2">开行<br>状态</th>
                                <th rowspan="2" style="width:90px;vertical-align: middle">运行方式</th>
                                <th rowspan="2" style="width:130px;vertical-align: middle">始发站</th>
                                <th rowspan="2" style="width:90px; vertical-align: middle">始发时间</th>
                                <th rowspan="2" style="width:130px;vertical-align: middle">接入站</th>
                                <th rowspan="2" style="width:90px;vertical-align: middle">接入时间</th>
                                <th rowspan="2" style="width:130px;padding:4px 0;vertical-align: middle">交出站</th>
                                <th rowspan="2" style="width:90px;padding:4px 0;vertical-align: middle">交出时间</th>
                                <th rowspan="2" style="width:130px;padding:4px 0;vertical-align: middle">终到站</th>
                                <th style="width:90px;vertical-align: middle" rowspan="2">终到时间</th>
                                <th style="width:50px;padding:4px 0;vertical-align: middle" rowspan="2">1条线</th>
                                    <th colspan="2" class="text-center">校验项</th>
								<th rowspan="2" style="width:90px;padding:4px 0;vertical-align: middle">相关局</th>
								<th rowspan="2" style="width:120px;vertical-align: middle">日常审核</th>
<!--                                 <th rowspan="2" style="width:50px;padding:4px 0;vertical-align: middle">次日<br>审核</th> -->
                                <th rowspan="2" style="width:70px;padding:4px 0;vertical-align: middle">落成</th>
                                <th rowspan="2" style="width: 17px" align="center"></th>

                            </tr>
                            <tr>
                                    <th  style="width:75px;padding:4px 0;" class="text-center">列车</th>
                                    <th  style="width:75px;padding:4px 0;" class="text-center">时刻</th>
                            </tr>
                            </thead>
                       </table> 
                       <div id="left_height" style="overflow-y:auto;">
                 		 <table class="table table-bordered table-striped table-hover" style="border: 0;"  data-bind="with: tableModel">  
                            <tbody data-bind="foreach: planList">
                            <tr>
                                <td style="width:40px;padding:4px 0;" class="text-center"><input class="checkbox-inline" name="plan" type="checkbox" data-bind="event:{change: $root.selectBox}, checked: isSelected"/></td>
                                <td style="width:42px;padding:4px 0;" class="text-center" data-bind="text: $index() + 1"></td>
                                <td style="width:100px;"class="text-center"><a href="#" data-bind="text: name, click: showGraphic"></a></td>
                                <td style="" class="text-center" data-bind="text: sourceType, attr:{title: sourceType}"></td>
                                <td style="width:60px;padding:4px 0;" class="text-center" data-bind="text: spareFlag, attr:{title: spareFlag}"></td>
                                <td style="width:90px;" class="text-center" data-bind="text: trainType, attr:{title: trainType}"></td>
                                <td style="width:130px;padding:4px 0;" class="text-center" data-bind="text: startStn, attr:{title: startStn}"></td>
                                <td style="width:90px;padding:4px 0;" class="text-center" data-bind="text: startTime, attr:{title: startTime}"></td>
                                <td style="width:130px;padding:4px 0;" class="text-center" data-bind="text: jrStn, attr:{title: jrStn}"></td>
                                <td style="width:90px;padding:4px 0;" class="text-center" data-bind="text: jrTime1, attr:{title: jrTime1}"></td>
                                <td style="width:130px;padding:4px 0;" class="text-center" data-bind="text: jcStn, attr:{title: jcStn}"></td>
                                <td style="width:90px;padding:4px 0;" class="text-center" data-bind="text: jcTime1, attr:{title: jcTime1}"></td>
                                <td style="width:130px;padding:4px 0;" class="text-center" data-bind="text: endStn, attr:{title: endStn}"></td>
                                <td style="width:90px;padding:4px 0;" class="text-center" data-bind="text: endTime, attr:{title: endTime}"></td>
                                <td style="width:50px;padding:4px 0;" class="text-center" data-bind="html: dailyLineFlagView"></td>
                                    <td  style="width:75px;padding:4px 0;" class="text-center">
                                        <button type="button" class="btn" style="padding: 0px 5px 0px 5px" data-bind="css: isTrainInfoMatchClass, text: isTrainInfoMatchText, click: showInfoComparePanel"></button>
                                    </td>
                                    <td  style="width:75px;padding:4px 0;"  class="text-center">
                                        <button type="button" class="btn" style="padding: 0px 5px 0px 5px" data-bind="css: isTimeTableMatchClass, text: isTimeTableMatchText, click: showTimeTableComparePanel"></button>
                                    </td>
								<td style="width:90px;" class="text-center" data-bind="text: passBureau, attr:{title: passBureau}"></td>
								<td style="width:120px;" class="text-center" data-bind="text: str1, attr:{title: str1}"></td>
<!--                                 <td style="padding:4px 0;width:50px;" class="text-center" data-bind="html: lev1Status"></td> -->
                                <td  style="width:70px;padding:4px 0;"  class="text-center">
                                    <button type="button" class="btn" data-bind="css: lev2StatusClass, text: lev2Status, click: completionLog" style="width: 50px;padding: 3px 0 4px 0;"></button>
                                </td>
                                <td style="width: 17px" align="center" class="td_17"></td>
                            </tr>
                            </tbody>
                        </table>
                       </div>
                    </div>
                </div>

                
            </div>
            </div>
            <!-- char start -->
            <div id="chatId" style="display: none" class="margin panel panel-default">
            <table>
            	<tr style="border: 0 px">
            		<td style="border: 0 px"><div id="chart_01" style="margin: 0; height: 253px;width: 31%"></div></td>
            		<td style="border: 0 px"><div id="chart_02" style="margin:0; height: 253px;width: 31%"></div></td>
            		<td style="border: 0 px"><div id="chart_03" style="margin:0; height: 253px;width: 31%"></div></td>
            	</tr>
            </table>
            </div>
            <!-- char end -->
            
        </div>
    </div>
</section>

	<div id="completion_log" class="easyui-dialog" title="落成记录"
		data-options="iconCls:'icon-save'"
		style="width: 950px; height: 600px;">

		<div id="completion_log1" class="row"
			style="overflow: auto;">
			<div class="table-responsive">
				<table class="table table-bordered table-striped table-hover"
					id="yourTableID2">
					<thead>
						<tr style="height: 25px">
							<th style="width: 80px">开行日期</th>
							<th style="width: 80px">车次</th>
							<th style="width: ">所属交路</th>
							<th style="width: 70px">落成局</th>
							<th style="width: 80px">落成时间</th>
							<th style="width: 70px">落成人</th>
							<th style="width: 80px">岗位</th>
							<th style="width: 120px">列车ID</th>
							<th style="width:120px">前续ID</th>
							<th style="width: 120px">后续ID</th>
							<th style="width: 17px"></th>
						</tr>
					</thead>
				</table>
                 <div style="overflow-y:auto;height:550px;" id="tan_completion">
					<table class="table table-bordered table-striped table-hover" style="border: 0;">
						<tbody data-bind="foreach: sents ">
							<tr>
								<td style="width:80px" data-bind="text: run_date, attr:{title: run_date}"></td>
								<td style="width:80px" align="center" data-bind="text: train_nbr"></td>
								<td style="width: " align="center"
									data-bind="text: cross_name"></td>
								<td style="width: 70px" align="center"
									data-bind="text: sent_people_bureau"></td>
								<td style="width: 80px" align="center"
									data-bind="text: sent_time"></td>
								<td style="width: 70px" align="center"
									data-bind="text: sent_people"></td>
								<td style="width: 80px" align="center"
									data-bind="text: sent_people_org"></td>
								<td style="width: 120px" align="center"
									data-bind="text: plan_train_id"></td>
								<td style="width: 120px" align="center"
									data-bind="text: pre_train_id"></td>
								<td style="width: 120px" align="center"
									data-bind="text: next_train_id"></td>
								<td style="width: 17px" align="center" class="td_17"></td>
							</tr>
						</tbody>
					</table>
                 </div>
				</div>
			</div>
		</div>

	</div>
	
	
	
		<div id="creatline" class="easyui-dialog" title="命令预览" data-options="iconCls:'icon-save'" style="width: 950px; height: 600px;">
		<div id="cmdInfo_dialog_rows1" class="row" style="width:100%;overflow: hidden;">
	   			<textarea id="textarea_cmdInfoStrs" rows="30" cols="150" data-bind="value : cmdTextStr()"></textarea>
<!-- 	   			<span data-bind="html : cmdTextStr()"></span> -->
	   		</div>
	   		<div id="cmdInfo_dialog_rows2" class="row" style="width:100%;height:30px;margin-top:10px;margin-bottom:5px">
	   			<div align="center">
	   				<a type="button" class="btn btn-success" data-bind="click: createCmd">确定</a>
	   				<a type="button" class="btn btn-success" data-bind="click: createCmdInfoCancel" style="margin-left:5px">取消</a>
	   			</div>
	   		</div>
<!-- 		<div> xxx年xx月xx日  xxx局普速列车日计划命令如下   </div> -->
<!-- 		<div>一 、临客列车   </div> -->
<!-- 		<div id="completion_log1lk" class="row" -->
<!-- 			style="overflow: auto;"> -->
<!-- 			<div class="table-responsive"> -->
<!-- 			<!-- 	<table class="table table-bordered table-striped table-hover" -->
<!-- 					id="yourTableID2"> -->
<!-- 					<thead> -->
<!-- 						<tr style="height: 25px"> -->
<!-- 							<th style="width: 80px">开行日期</th> -->
<!-- 							<th style="width: 80px">车次</th> -->
<!-- 							<th style="width: ">所属交路</th> -->
<!-- 							<th style="width: 70px">落成局</th> -->
<!-- 							<th style="width: 80px">落成时间</th> -->
<!-- 							<th style="width: 70px">落成人</th> -->
<!-- 							<th style="width: 80px">岗位</th> -->
<!-- 							<th style="width: 120px">列车ID</th> -->
<!-- 							<th style="width:120px">前续ID</th> -->
<!-- 							<th style="width: 120px">后续ID</th> -->
<!-- 							<th style="width: 17px"></th> -->
<!-- 						</tr> -->
<!-- 					</thead> -->
<!-- 				</table> --> 
            
<!-- 					<table  style="border: 0;"> -->
<!-- 						<tbody data-bind=""> -->
<!-- 							<tr> -->
<!-- 								<td style="width:80px" ></td> -->
<!-- 								<td style="width:80px" align="center"></td> -->
<!-- 								<td style="width:80px " align="center"></td> -->
<!-- 								<td style="width: 70px" align="center"></td> -->
<!-- 								<td style="width: 80px" align="center"></td> -->
<!-- 							</tr> -->
<!-- 						</tbody> -->
<!-- 					</table> -->
<!--                  </div> -->
<!-- 				</div> -->
				
<!-- 				<div>二、图定列车  </div> -->
<!-- 		 <div id="completion_log1td" class="row" -->
<!-- 			style="overflow: auto;"> -->
<!-- 			<div class="table-responsive"> -->
<!-- 			<!-- 	<table class="table table-bordered table-striped table-hover" -->
<!-- 					id="yourTableID2"> -->
<!-- 					<thead> -->
<!-- 						<tr style="height: 25px"> -->
<!-- 							<th style="width: 80px">开行日期</th> -->
<!-- 							<th style="width: 80px">车次</th> -->
<!-- 							<th style="width: ">所属交路</th> -->
<!-- 							<th style="width: 70px">落成局</th> -->
<!-- 							<th style="width: 80px">落成时间</th> -->
<!-- 							<th style="width: 70px">落成人</th> -->
<!-- 							<th style="width: 80px">岗位</th> -->
<!-- 							<th style="width: 120px">列车ID</th> -->
<!-- 							<th style="width:120px">前续ID</th> -->
<!-- 							<th style="width: 120px">后续ID</th> -->
<!-- 							<th style="width: 17px"></th> -->
<!-- 						</tr> -->
<!-- 					</thead> -->
<!-- 				</table> --> 
            
<!-- 					<table  style="border: 0;"> -->
<!-- 						<tbody data-bind=" "> -->
<!-- 							<tr> -->
<!-- 								<td style="width:80px" ></td> -->
<!-- 								<td style="width:80px" align="center"></td> -->
<!-- 								<td style="width:80px " align="center"></td> -->
<!-- 								<td style="width: 70px" align="center"></td> -->
<!-- 								<td style="width: 80px" align="center"></td> -->
<!-- 							</tr> -->
<!-- 						</tbody> -->
<!-- 					</table> -->
<!--                  </div> -->
<!-- 				</div> -->
		</div>
	

	<div id="kyvsyx" class="easyui-dialog" title="客运计划列车信息 vs 运行线列车信息"
		data-options="iconCls:'icon-save'"
		style="width: 750px; height: 500px;overflow-y: hidden;">
		 <iframe style="width: 100%;border: 0;overflow-y: hidden;" src=""></iframe>
	</div> 

	<div id="kyskvsyxsk" class="easyui-dialog" title="客运计划列车时刻表 vs 运行线列车时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 750px; height: 500px;overflow-y: hidden;">
		 <iframe style="width: 100%;border: 0;overflow-y: hidden;" src=""></iframe>
	</div> 
	
	<!-- 重复列车 -->
	<div id="trainCf" class="easyui-dialog" title="重复列车"
			data-options="iconCls:'icon-save'"
			style="width: 700px; height: 650px;overflow-y: hidden;">
		
		 <div id="modifyRecordDiv_1" class="row" style="margin:5px;overflow: auto;">
		     <div class="table-responsive" > 
				<table class="table table-bordered table-striped table-hover" id="yourTableID2" width="100%" border="0" cellspacing="0" cellpadding="0"  >
					<thead>
						<tr style="height: 25px"> 
							<th align="center" style="width: 40px">序号</th>
							<th align="center" style="width: 80px">车次</th>
							<th align="center" style="width: 80px">始发日期</th>
							<th align="center" style="width: 80px">始发站</th>
							<th align="center" style="width: 60px;padding: 4px 0;">终到站</th>
						</tr>
					</thead>
		    	 </table>
	              <div id="left_height2" style="overflow-y:auto;height:580px;">
	                  <table class="table table-bordered table-striped table-hover" style="border:0;">
						<tbody data-bind="foreach: isTrainCfArray ">
							<tr  >
						        <td style="width: 40px" data-bind="text:$index() + 1"></td>
						        <td style="width: 80px" data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
						        <td style="width: 80px" data-bind="text: startTime"></td>
						        <td style="width: 80px" data-bind="text: startStn "></td>
						        <td style="width: 60px" data-bind="text: endStn"></td>
							</tr> 
						</tbody> 
					</table>
		    	</div>
			</div>  
	  </div> 
  </div>
</body>

</html>
