<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();

boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}
String  currentBureauName = user.getBureauShortName();
String currentUserBureau = user.getBureau();
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
} 
String basePath = request.getContextPath();
%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>既有图定开行计划</title>
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
	<script>
	    window.alert = function(txt) {
	    	return;
	    }
    </script>
<script type="text/javascript">
var currentBureauName = "<%=currentBureauName %>";
var basePath = "<%=basePath %>";
var all_role = "<%=userRolesString %>";
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
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
.ckbox.disabled {
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
}
.table caption + thead tr:first-child th, .table colgroup + thead tr:first-child th, .table thead:first-child tr:first-child th, .table caption + thead tr:first-child td, .table colgroup + thead tr:first-child td, .table thead:first-child tr:first-child td{
border-bottom: 0;
}
.table-calendar .space {
	max-width: none;
}
</style>
</head>
<body class="Iframe_body">
<div class="row" style="margin: -3px 13px 10px 7px;"> 
  <!--分栏框开始-->
  <div class="pull-left" style="width: 30%;height:100%" id="leftBox"> 
    <!--分栏框开始-->
    <div class="row" style="margin:  -5px 0px 5px 0px;">
      <div>
        <div class="row"  style="width: 100%;">
          <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 5px;"> 开始日期:</label>
          <div class="pull-left" style="margin-left: 5px;">
            <input type="text" class="form-control" style="padding:4px 12px; ;width:105px;" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
          </div>
          <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;margin-top: 5px;"> 截至日期:</label>
          <div class="pull-left" style="margin-left: 5px; ">
            <input type="text" class="form-control" style="padding:4px 12px; ;width:105px;" placeholder="" id="runplan_input_endDate"  name="endDate" data-bind="value: searchModle().planEndDate" />
          </div>
        </div>
        <div class="row"  style="width: 100%; margin-top: 5px;">
          <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 5px;"> 担当局:</label>
          <div class="pull-left" style="margin-left: 5px;">
            <select style="padding:4px 12px; ;width:118px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: '',event:{change: bureauChange}">
            </select>
          </div>
          <input type="checkBox" class="pull-left" class="form-control" value="1" data-bind="checked: searchModle().currentBureanFlagFei,click: checkFei" style="width: 20px; margin-top: 7px; margin-left:18px" class="form-control">
          <label for="exampleInputEmail5" class="control-label pull-left" style="margin-top: 5px;"> 非本局担当</label>
          <input type="checkBox" class="pull-left" class="form-control"
													value="1" data-bind="checked: searchModle().currentBureanFlag"
													style="width: 20px; margin-top: 7px; margin-left:18px"
													class="form-control">
          <label for="exampleInputEmail5" class="control-label pull-left" style="margin-top: 5px;"> 本局相关</label>
        </div>
        <div class="row"  style="margin-top: 5px;">
          <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 5px;"> 车次:&nbsp;</label>
          <div class="pull-left">
            <input type="text" class="form-control" style="padding:4px 12px;width: 134px;"
												 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
          </div>
          <!-- <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;" >
													生成状态:</label>
												<div class="pull-left" style="margin-left: 5px;">
													<select style="width:50px" id="input_cross_sure_flag"
														class="form-control" data-bind="options: searchModle().unitCreateFlags, value: searchModle().unitCreateFlag, optionsText: 'text' , optionsCaption: '' ">
													</select>
												</div> -->
          <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left: 20px;margin-top: 5px;"> 审核状态:</label>
          <div class="pull-left" style="margin-left: 5px;">
            <select style="padding:4px 12px; ;width:115px" id="input_cross_sure_flag"
														class="form-control" data-bind="options: searchModle().checkFlags1, value: searchModle().checkFlag, optionsText: 'text' , optionsCaption: ''">
            </select>
          </div>
          <div class="pull-left" style="margin-left: 20px;"> 
          	<a type="button" class="btn btn-success btn-input" data-toggle="modal" data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a> 
          </div>
        </div>
        
        <!--    <div class="row"  style="margin-top: 5px;"> 
												<span style="margin-left: 20px;"><span style="">途经局:</span><span data-bind="text: '京郑武广'"></span><span style="margin-left:3px" data-bind="text: '已审核: 郑  ; 未审核 : 京武广'"></span></span>   
											</div>  -->
        
        <div id="plan_cross_default_panel" class="panel panel-default" style="margin-top:10px;padding-bottom:10px;">
          <div class="row" style="margin-top:10px">
             <shiro:hasPermission name="JHPT.KYJH.LJ.KD"> 
            <div class="form-group" style="margin-left: 10px;">
             	审核：
	            <a type="button" data-bind="attr:{class: searchModle().checkActiveFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: checkCrossInfo"  data-toggle="modal" data-target="#" id="btn_cross_sure">
	            	<i class="fa fa-check"></i>通过
	            </a>
	                                       <span style="margin-left: 50px;">滚动：</span>
	            <a type="button" class="btn btn-success" data-toggle="modal" data-target="#" data-bind="click: modalRun">
	            	<i class="fa fa-life-buoy"></i>开始
	            </a>
		         <a  type="button" class="btn btn-success" data-toggle="modal" data-target="#" id="btn_cross_delete" style="margin-left: 50px;" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: deleteCrosses">
			         <i class="fa fa-trash-o"></i>删除
		         </a>
             <div class="form-group" style="margin-left: 0px;margin-top:5px">
		         <a  type="button" class="btn btn-success" data-toggle="modal" data-target="#" id="btn_cross_dh" style="margin-left: 32px;" data-bind="attr:{class: searchModle().dhActiveFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: dhCrosses">
			         <i class="fa fa-times"></i>不通过
		         </a>
	            <a type="button" class="btn btn-success" data-toggle="modal" data-target="#" style="margin-left: 92px;" data-bind="click: modalRunStop">
	            	<i class="fa fa-pause"></i>停止
	            </a>
		         <a  type="button" class="btn btn-success" data-toggle="modal" data-target="#" id="btn_cross_sendMsg" style="margin-left: 50px;" data-bind="click: sendMsg">
			         <i class="fa fa-volume-up"></i>发消息
		         </a>
            </div>
            </div>
	         </shiro:hasPermission>
	         <shiro:hasPermission name="JHPT.KYJH.LJ.JJS"> 
	         <div class="form-group" style="margin-left: 10px;">
		         <a  type="button" class="btn btn-success" data-toggle="modal" data-target="#" id="btn_cross_delete" style="margin-left: 18px;" data-bind="attr:{class: searchModle().activeFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: deleteCrosses">
			         <i class="fa fa-trash-o"></i>删除
		         </a>
            </div>
	         </shiro:hasPermission>
          </div>
          <div style="margin-left:49px;">相关局: <span style="margin-top:5px;margin-left:5px;" data-bind="html: currentCross().relevantBureauShowValue()"></span></div>
          <div style="margin-left:23px;">
          	审核通过局: <span style="margin-top:5px;margin-left:5px;color:green;" data-bind="text:checkburu()"></span>
          	&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
          	审核不通过局: <span style="margin-top:5px;margin-left:5px;color:red;" data-bind="text:checkburuNo()"></span>
          </div>
          <div class="row" style="padding:0 10px;">
            <div class="pull-left" style="width: 75%;">
              <table class="table table-bordered table-striped table-hover" id="cross_table_crossInfo">
                <thead>
                  <tr style="height: 25px">
                    <th style="width: 30px" align="center"><input type="checkbox" style="margin-top:0" value="1" data-bind="checked: crossAllcheckBox, event:{change: selectCrosses}"></th>
                    <th style="width: 38px" align="center">序号</th>
                    <th style="width: 30px" align="center">局</th>                    
                    <th align="center">  
						<div style="position: relative;">
						 <label for="exampleInputEmail5" style="font-weight: bold;vertical-align: bottom;">交路名</label> 
						 <select class="form-control" style="width: 58px;display:inline-block;position: absolute;top:-4px;margin-left: 5px;" id="input_cross_filter_showFlag" data-bind="options: [{'code': 2, 'text': '全称'},{'code': 1, 'text': '简称'}], value: searchModle().shortNameFlag, optionsText: 'text', optionsValue: 'code'"><option value="2">全称</option><option value="1">简称</option>
						 </select>
						</div>  
					</th>
					<th style="width: 38px" align="center">滚动</th>
                    <th style="width: 17px" align="center"></th>
                  </tr>
                </thead>
              </table>
                 
              <div id="plan_train_panel_body" style="overflow-y:auto;">
                          <table class="table table-bordered table-striped table-hover" style="border:0;">
                            <tbody data-bind="foreach: planCrossRows">
                              <tr data-bind=" visible: visiableRow, style:{color: $parent.currentCrossRow().planCrossId == planCrossId ? 'blue':''}" >
                                <td align="center" style="width:30px"><input type="checkbox" value="1" data-bind="attr:{class: activeFlag() == 1  || checkActiveFlag() == 1?  '' : 'ckbox disabled'},event:{change: $parent.selectCross}, checked: selected"></td>
                                <td style="width: 38px" align="center" data-bind="text: $index() + 1,event:{mousedown:$parent.showTrains}"></td>
                                <td style="width: 30px" align="center" data-bind="text: tokenVehBureauShowValue,event:{mousedown:$parent.showTrains}"></td>
                                <td><i  data-bind="attr:{class: checkCss}"></i><span data-bind="text: $parent.searchModle().shortNameFlag() == 1 ? shortName : crossName, attr:{title: crossName},event:{mousedown:$parent.showTrains}"></span></td>
                                <td style="width: 38px" align="center">
                                	<a href="#" data-bind="click: $parent.chooseModalRun">
                                		<i data-bind="attr:{class: isAutoGenerateCss}"></i>
                                	</a>
                                </td>
                                <td style="width: 17px" align="center" class="td_17"></td>
                              </tr>
                            </tbody>
                          </table>
              </div>

            </div>
            <div class="pull-left" style="width: 25%;">
              <table class="table table-bordered table-striped" style="margin:0 0 0 -2px;">
                <thead>
                  <tr style="height: 26px">
                    <th style="width: 99%" align="center">方案/车次</th>
                  </tr>
                </thead>
                <tbody style="padding:0">
                
                <tr style="padding:0">
                  <td colspan="6" style="padding:0"><div id="plan_cross_panel_body" style="overflow-y:auto;padding:8px;border-top:1px solid #d1d2d4">
                      <table class="table table-bordered table-striped table-hover"
																				id="cross_trainInfo" >
                        <tbody data-bind="foreach: trains" >
                          <tr  data-bind="click: $parent.showTrainTimes, style:{color: $parent.currentTrain() != null && $parent.currentTrain().trainNbr == trainNbr ? 'blue':''}">
                            <td data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
                          </tr>
                        <div>
                          <pre><span data-bind="text: crossSearchMode"/></pre>
                        </div>
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
  <div class="modal fade" id="Modal-run" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="padding-top: 8%;">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">滚动状态</h4>
      </div>
      <!--panel-heading-->
      <div class="panel-body row">
              <h4 class="blue" style="text-align: center;margin-top:20px;">是否停止滚动？</h4>
      </div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary btn-input" data-dismiss="modal" id="yes-btn">确定</button>
        <button type="button" class="btn btn-warning btn-input" data-dismiss="modal">取消</button>
      </div>
      
      <!-- /.modal-content --> 
    </div>
  </div>
  <!-- /.modal-dialog --> 
</div>
  <div class=' rightMenu' id= "checkhis" style="display:none">
  <ul>
  	<li class='vm-list'><i class='fa fa-eye'></i>&nbsp;<a data-bind="click:showhis" >&nbsp;审核记录</a></li>
  	<li class='vm-list'><i class='fa fa-list-ol'></i>&nbsp;<a data-bind="click:showHisModify" >&nbsp;调整记录</a></li>
  </ul>
  
  </div>
  
  <div class="blankBox"></div>
  
  
  <div class="pull-left" style="width: 69%;"  id="rightBox"> 
    <!-- Nav tabs -->
    <div class="panel panel-default" style="margin:-5px -14px 0 0;">
      <ul class="nav nav-tabs" style="margin-top:10px;margin-left:5px;margin-right:5px;"  >
        <li class="active"><a style="padding:3px 10px;" href="#profile" data-toggle="tab" data-bind="click: toCrossTab">交路信息</a></li>
        <li><a style="padding:3px 10px;" href="#home" data-toggle="tab" data-bind="click: toRunMapTab">交路图</a></li>
        <li style="float:right">
          <input type="checkbox" class="pull-left" class="form-control" data-bind="checked: isShowRunPlans, event:{change: showRunPlans}"
							class="form-control">
          <label for="exampleInputEmail5" class="control-label pull-left" style="margin:2px 0 0 5px;"> 显示开行情况</label>
        </li>
      </ul>
      <!-- Tab panes -->
      <div class="tab-content" >
        <div class="tab-pane" id="home">
          <div class="panel">
            <div class="panel-body" >
              <div class="row" style="margin:5px 0 10px 0;">
                <form class="form-inline" role="form">
                  <div class="row" style="margin:5px 0 10px 50px;">
                    <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_refresh"><i class="fa fa-refresh"></i>刷新</button>
                    <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_magnification"><i class="fa fa-search-plus"></i>X+</button>
                    <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_shrink"><i class="fa fa-search-minus"></i>X-</button>
                    <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_magnification"><i class="fa fa-search-plus"></i>Y+</button>
                    <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_shrink"><i class="fa fa-search-minus"></i>Y-</button>
                    比例：｛X:
                    <label id="canvas_event_label_xscale">1</label>
                    倍；Y:
                    <label id="canvas_event_label_yscale">1</label>
                    倍｝ <span>
                    <input type="checkbox" id="canvas_checkbox_stationType_jt" name="canvas_checkbox_stationType" checked="checked" style="margin-left:10px">
                    简图</span>
                    <input type="checkbox" id="canvas_checkbox_trainTime" style="margin-left:10px;margin-top:2px"  value=""/>
                    显示时刻
                    &nbsp;&nbsp;车底：
                    <select id="canvas_select_groupSerialNbr">
                    </select>
                  </div>
                </form>
              </div>
              <div id="canvas_parent_div" class="table-responsive" style="width:100%;overflow-x:auto ; overflow-y:auto;">
                <canvas id="canvas_event_getvalue" style="margin-top: 50px;"></canvas>
              </div>
              <!--  <div id ="dddddddddddd">bbbbbbbbbbb</div> --> 
            </div>
          </div>
        </div>
        <div class="tab-pane active" id="profile">
          <div class="row" style="margin: 0px 10px 10px 10px;">
            <div class="row" >
              <form class="form-horizontal" role="form" data-bind="with: currentCross">
                <div class="row" style="margin: 0px 0 5px 0;">
                  <label for="exampleInputEmail3"
											class="control-label pull-left"> 车底交路名:&nbsp;</label>
                  <div class="pull-left" style="margin-left: 26px;">
                    <input type="text" class="form-control" style="width: 470px;" data-bind="value: crossName"
												id="plan_construction_input_trainNbr" >
                  </div>
                  <label for="exampleInputEmail5" class="control-label pull-left" style="margin-left:40px"> 铁路线型:</label>
                  <div class="pull-left">
                    <input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" data-bind="checked: highlineFlag" value="0" disabled>
                  </div>
                  <label for="exampleInputEmail5" class="control-label pull-left"> 普速</label>
                  <div class="pull-left">
                    <input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" value="1" data-bind="checked: highlineFlag" disabled>
                  </div>
                  <label for="exampleInputEmail5" class="control-label pull-left"> 高铁</label>
                  <div class="pull-left">
                    <input type="radio" class="pull-left" class="form-control" 
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" value="2" data-bind="checked: highlineFlag" disabled>
                  </div>
                  <label for="exampleInputEmail5" class="control-label pull-left"> 混合</label>
                </div>
                <div class="row" style="margin: 5px 0 0px 0;">
                  <label for="exampleInputEmail3"
												class="control-label pull-left"> 备用套跑交路名:&nbsp;</label>
                  <div class="pull-left">
                    <input type="text" class="form-control" style="width: 470px;" data-bind="value: crossSpareName">
                  </div>
                  <label for="exampleInputEmail5" style="margin-left: 40px;" class="control-label pull-left"> 开行状态:</label>
                  <div class="pull-left">
                    <input type="radio" class="pull-left" class="form-control"
												value="1" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" disabled>
                  </div>
                  <label for="exampleInputEmail5" class="control-label pull-left"> 开行</label>
                  <div class="pull-left">
                    <input type="radio" class="pull-left" class="form-control"
												value="2" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" disabled>
                  </div>
                  <label for="exampleInputEmail5" class="control-label pull-left"> 备用</label>
                  <div class="pull-left">
                    <input type="radio" class="pull-left" class="form-control"
												value="9" data-bind="checked: spareFlag"
												style="width: 20px; margin-left: 5px; margin-top: 5px"
												class="form-control" disabled>
                  </div>
                  <label for="exampleInputEmail5" class="control-label pull-left"> 停运</label>
                </div>
                <div class="row" style="margin: 5px 0 0px 0;">
                  <div class="pull-left" style="width: 16%;">
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail3"
												class="control-label pull-left"> 组数:&nbsp;</label>
                      <div class="pull-left">
                        <input type="text" class="form-control" style="width: 40px;" data-bind="value: groupTotalNbr">
                      </div>
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail2" class="control-label pull-left">对数:&nbsp;</label>
                      <input type="text" class="form-control" style="width: 40px;" data-bind="value: pairNbr">
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail5"  class="control-label pull-left"> 开始:&nbsp;</label>
                      <input type="text" class="form-control" style="width: 70px;"
													placeholder=""  data-bind="value: crossStartDate">
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 结束:&nbsp;</label>
                      <input type="text" class="form-control" style="width: 70px;"
													placeholder=""  data-bind="value: crossEndDate">
                    </div>
                    <!-- <div class="row" style="margin: 5px 0 0px 0;">  
											<input type="checkBox" class="pull-left" class="form-control"
												value="1" data-bind="checked: cutOld"
												style="width: 20px; margin-top: 5px"
												class="form-control"> 
											<label for="exampleInputEmail5" class="control-label pull-left">
												截断原交路</label>
										</div> --> 
                  </div>
                  <div class="pull-left" style="width: 28%;">
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 普速规律:</label>
                      <input type="radio" class="pull-left" class="form-control"
													value="1" data-bind="checked: commonlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 每日</label>
                      <input type="radio" class="pull-left" class="form-control"
													value="2" data-bind="checked: commonlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 隔日</label>
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail5"  class="control-label pull-left"> 高铁规律:</label>
                      <input type="radio" class="pull-left" class="form-control"
													value="1" data-bind="checked: highlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 日常</label>
                      <input type="radio" class="pull-left" class="form-control"
													value="2" data-bind="checked: highlineRule"
													style="width: 20px; margin-left: 5px; margin-top: 5px"
													class="form-control">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 周末</label>
                      <input type="radio" class="pull-left" class="form-control"
														value="3" data-bind="checked: highlineRule"
														style="width: 20px; margin-left: 5px; margin-top: 5px"
														class="form-control">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 高峰</label>
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 指定星期:&nbsp;</label>
                      <div class="pull-left">
                        <input type="text" class="form-control" style="width: 71px;"
													placeholder=""   data-bind="value: appointWeek">
                      </div>
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 指定日期:&nbsp;</label>
                      <div class="pull-left">
                        <input type="text" class="form-control" style="width: 140px;"
													placeholder=""  data-bind="value: appointDay">
                      </div>
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 指定周期:&nbsp;</label>
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
                        <input type="text" class="form-control" style="width: 50px;"  data-bind="value: tokenVehBureauShowValue">
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
                        <input type="text" class="form-control disabled" style="width: 50px;" data-bind="value: tokenPsgBureauShowValue" >
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
                      <input type="text" maxlength ="200" class="form-control pull-left" style="width: 182px;" data-bind="value: crossSection">
                      <label for="exampleInputEmail3"
												class="control-label pull-left" ><!-- 不能超过200字符 --></label>
                    </div>
                    <div class="row" style="margin: 5px 0 0px 0;">
                      <label for="exampleInputEmail3"
												class="control-label pull-left"  style="margin-left: 13px;" > 经由线:&nbsp;</label>
                      <input type="text" class="form-control" style="width: 182px;" data-bind="value: throughLine">
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
                      <label for="exampleInputEmail5" class="control-label pull-left"> 供电</label>
                      <input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: dejCollect"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 集便</label>
                      <input type="checkbox" class="pull-left" class="form-control"
											value="1" data-bind="checked: airCondition"
											style="width: 20px; margin-left: 5px; margin-top: 5px"
											class="form-control">
                      <label for="exampleInputEmail5" class="control-label pull-left"> 空调</label>
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
                <!-- <div class="row" style="margin: 5px 0 0px 0;">
							      <a type="button" style="margin-left: 15px"
										class="btn btn-success" data-toggle="modal" data-target="#"
										id="cross_train_save" data-bind="attr:{class: $parent.searchModle().activeCurrentCrossFlag() == 1 ? 'btn btn-success' : 'btn btn-success disabled'}, click: $parent.saveCrossInfo"> 保存</a>
							</div> --> 
                <!--col-md-3 col-sm-4 col-xs-4-->
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div id="big-learn-more-content" class="row" style="margin: -6px 6px -10px 7px;" >
  <div class="panel" style="height:220px;overflow: auto;">
    <div id="learn-more-content">
      <div class="panel-body">
        <ul class="nav nav-tabs">
          <li class="active"><a style="padding:3px 10px;" href="#runPlan" data-toggle="tab">开行情况</a></li>
        </ul>
        <!-- Tab panes -->
        <div class="tab-content">
          <div class="tab-pane active" id="runPlan" >
            <div class=""> 
              <!--panle-heading-->
              <div style="padding:10px 0;overflow: auto">
                <div class="table-responsive" >
                  <table class="table table-bordered table-striped table-hover table-calendar">
                    <thead>
                      <tr data-bind="template: { name: 'runPlanTableDateHeader', foreach: planDays }"></tr>
                      <tr data-bind="template: { name: 'runPlanTableWeekHeader', foreach: planDays }"></tr>                      
                    </thead>
                    <tbody data-bind="template: { name: 'runPlanTableVlaues', foreach: trainPlans }" style="border-top: 1px solid #d1d2d4;">
                    </tbody>
                  </table>
                
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>



<!--详情时刻表--> 
<!--时刻表-->
<div id="train_time_dlg" class="easyui-dialog" title="时刻表"
			data-options="iconCls:'icon-save'"
			style="width: 750px; height: 700px;overflow-y: hidden;">
<iframe style="width: 100%;border: 0;overflow-y: hidden;" src=""></iframe>
<!-- 
	 <div id="run_plan_train_times" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 608px; height: 500px; padding: 10px;"> 
			      panle-heading
			      <div class="panel-body" style="padding:10px;margin-right:10px;">
				       <ul class="nav nav-tabs" >
						  <li class="active"><a style="padding:3px 10px;" href="#simpleTimes" data-toggle="tab">简点</a></li> 
						  <li><a style="padding:3px 10px;" href="#allTimes" data-toggle="tab">详点</a></li> 
						  <li style="float:right" ><span style="font: -webkit-small-control;" data-bind="html: currentTrainInfoMessage()"></span></li>
						</ul> 
						Tab panes
						<div class="tab-content" >
						  <div class="tab-pane active" id="simpleTimes" > 
					      	<div class="table-responsive" > 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead> 
							         <tr>
							          <th style="width:7%">序号</th>
					                  <th style="width:19%">站名</th>
					                  <th style="width:7%">路局</th>
					                  <th style="width:13%">到达时间</th>
					                  <th style="width:13%">出发时间</th>
					                  <th style="width:14%">停时</th>
					                  <th style="width:9%">到达天数</th> 
					                  <th style="width:9%">运行天数</th>
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr>
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="simpleTimes_table" style="height: 400px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" >
														 <tbody data-bind="foreach: simpleTimes">
												           <tr data-bind="visible: stationFlag != 'BTZ'">  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:14%" align="center" data-bind="text: stepStr"></td>
															<td style="width:10%" align="center" data-bind="text: arrRunDays"></td>
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
					      	<div class="table-responsive" > 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead> 
							         <tr>
							          <th style="width:7%">序号</th>
					                  <th style="width:19%">站名</th>
					                  <th style="width:7%">路局</th>
					                  <th style="width:13%">到达时间</th>
					                  <th style="width:13%">出发时间</th>
					                  <th style="width:14%">停时</th>
					                  <th style="width:9%">到达天数</th> 
					                  <th style="width:9%">运行天数</th>
					                  <th style="width:15%" colspan="2">股道</th>  
					                 </tr> 
							        </thead>
							        <tbody style="padding:0">
										 <tr style="padding:0">
										   <td colspan="9" style="padding:0">
												 <div id="allTimes_table" style="height: 400px; overflow-y:auto;"> 
													<table class="table table-bordered table-striped table-hover" > 
														 <tbody data-bind="foreach: times">
												           <tr>  
															<td style="width:7.5%" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:19%" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:7.5%" align="center" data-bind="text: bureauShortName"></td>
															<td style="width:14.3%" align="center" data-bind="text: sourceTime"></td>
															<td style="width:14.3%" align="center" data-bind="text: targetTime"></td>
															<td style="width:14%" align="center" data-bind="text: stepStr"></td>
															<td style="width:10%" align="center" data-bind="text: arrRunDays"></td>
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
	   
	    --> 

<!--调整时刻表-->
<div id="run_plan_train_times_edit_dialog" class="easyui-dialog" title="调整时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 1560px; height: 700px;overflow: hidden;">
  <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
<!--调整经路-->
<div id="run_plan_train_pathway_edit_dialog" class="easyui-dialog" title="调整径路"
		data-options="iconCls:'icon-save'"
		style="width: 1560px; height: 700px;overflow: hidden;">
  <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
<!--停运 和  备用-->
<div id="run_plan_train_times_stop_dialog" class="easyui-dialog" title="停运"
		data-options="iconCls:'icon-save'"
		style="width: 880px; height: 550px;overflow: hidden;">
  <iframe style="width: 100%; height: 100%;border: 0;overflow: hidden;" src=""></iframe>
</div>
<div id="run_plan_train_crew_dialog" class="easyui-dialog" title="调整时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 1200px; height: 400px;overflow: hidden;">
  <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
<!--发消息-->
<div id="run_plan_sendMsg" class="easyui-dialog" title="发消息"
		data-options="iconCls:'icon-save'"
		style="width: 600px; height: 365px;overflow: hidden;">
  <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>

<!--<div id="run_plan_train_times_edit_div" class="easyui-dialog" title="调整时刻表" data-options="iconCls:'icon-save'"
		style="width: 500px; height: 500px; padding: 10px; "> 
			      panle-heading
			      <div class="panel-body" style="padding:10px;margin-right:10px;">
						  <div style="float:right;position:fixed;z-index:2;" ><span style="font: -webkit-small-control;" data-bind="html: currentTrainInfoMessage()"></span></div>
						Tab panes
			        	<div class="panel panel-default" id="train_run_allTimes" > 
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover">
							        <thead style="position:fixed;z-index:2;">
							        <tr>
							          <th style="width:40px">序号</th>
					                  <th style="width:200px">站名</th>
					                  <th style="width:50px">路局</th>
					                  <th style="width:110px">到达时间</th>
					                  <th style="width:110px">出发时间</th>
					                  <th style="width:50px">停留时间</th>
					                  <th style="width:50px">天数</th> 
					                  <th style="width:50px" colspan="2">股道</th>  
					                 </tr>
							        </thead>
														 <tbody data-bind="foreach: times">
												           <tr>  
															<td align="center" data-bind=" text: $index() + 1"></td>
															<td data-bind="text: stnName, attr:{title: stnName}"></td>
															<td align="center" data-bind="text: bureauShortName"></td>
										
															<td align="center" data-bind="text: targetTime" contentEditable="true"></td>
															<td align="center" data-bind="text: stepStr"></td>
															<td align="center" data-bind="text: runDays"></td>
															<td align="center" data-bind="text: trackName" contentEditable="true"></td>
												        	</tr>
												        </tbody>
						        </table>
			        		</div>   
			        	</div>
      		</div>
	   </div>  --> 
<!--调整时刻表	end-->


<div id="hisshow" class="easyui-dialog" title="审核记录"
			data-options="iconCls:'icon-save'"
			style="width: 888px; height:600px;overflow-y: hidden;">
		
		 <div id="hisshow1" class="row" style="margin:5px;overflow: auto;">
		     <div class="table-responsive" > 
				<table class="table table-bordered table-striped table-hover" id="yourTableID2" width="100%" border="0" cellspacing="0" cellpadding="0"  >
					<thead>
						<tr style="height: 25px"> 
							<th align="center" style="width: 20px">序号</th>
							<th align="center" style="width: 50px">审核局</th>
							<th align="center" style="width: 50px">审核时间</th>
							<th align="center" style="width: 50px">审核人</th>
							<th align="center" style="width: 80px">审核人岗位</th>
							<th align="center" style="width: 50px">历史/当前</th>
							<th align="center" style="width: 50px">审核结果</th>
							<th align="center" style="width: 50px">联系方式</th>
							<th align="center" style="width: 200px">不通过原因</th>
							<th style="width: 10px" align="center"></th>
						</tr>
					</thead>
		    	 </table>
              <div id="left_height0" style="overflow-y:auto;height:540px;">
                  <table class="table table-bordered table-striped table-hover" style="border:0;">
					<tbody data-bind="foreach: checkplans ">
						<tr  >
					        <td style="width: 25px" data-bind="text:$index() + 1"></td>
					        <td style="width: 50px" data-bind="text: checkBureau "></td>
					        <td style="width: 50px" data-bind="text: checkTime "></td>
					        <td style="width: 50px" data-bind="text:checkPeople "></td>
					        <td style="width: 80px" data-bind="text: checkDept"></td>
					        <td style="width: 50px" data-bind="text:hisornow  "></td>
					        <td style="width: 50px" data-bind="text:checkState  "></td>
					        <td style="width: 50px" data-bind="text:checkPeopleTel  "></td>
					        <td class="normal" style="width: 200px" data-bind="text:checkRejectReason  "></td>
						    <td style="width: 10px" align="center" class="td_17"></td>     
						</tr> 
					</tbody> 
				</table>
	    	</div>
		</div>  
		
	<!-- 	self.planCrossId  =ko.observable(data.planCrossId ) ;
	self.startDate    =ko.observable(data.startDate   ) ;
	self.endDate      =ko.observable(data.endDate     ) ;
	self.checkPeople  =ko.observable(data.checkPeople ) ;
	self.checkTime    =ko.observable(data.checkTime   ) ;
	self.checkDept    =ko.observable(data.checkDept   ) ;
	self.checkBureau  =ko.observable(data.checkBureau ) ;
	self.checkHisFlag =ko.observable(data.checkHisFlag) ;
	self.checkCmdtel  =ko.observable(data.checkCmdtel ) ; -->
  </div> 
		
  </div>
<div id="modifyRecordDiv" class="easyui-dialog" title="调整记录"
			data-options="iconCls:'icon-save'"
			style="width: 1400px; height: 650px;overflow-y: hidden;">
		
		 <div id="modifyRecordDiv_1" class="row" style="margin:5px;overflow: auto;">
		     <div class="table-responsive" > 
				<table class="table table-bordered table-striped table-hover" id="yourTableID2" width="100%" border="0" cellspacing="0" cellpadding="0"  >
					<thead>
						<tr style="height: 25px"> 
							<!-- <th align="center" style="width: 40px">序号</th> -->
							<th align="center" style="width: 160px">交路名</th>
							<th align="center" style="width: 80px">车次</th>
							<th align="center" style="width: 60px;padding: 4px 0;">调整局</th>
							<th align="center" style="width: 80px">调整类型</th>
							<th align="center" style="width: 80px">调整依据</th>
							<th align="center" style="width: 80px">起始日期</th>
							<th align="center" style="width: 80px">终止日期</th>
							<th align="center" style="width: 70px">规律</th>
							<th align="center" style="width: 100px">择日日期</th>
							<th align="center" style="width: 70px">调整人</th>
							<th align="center" style="width: 90px">岗位</th>
							<th align="center" style="width: 80px">开行日期</th>
							<th align="center" style="width: 80px">调整时间</th>
							<th align="center" style="width: ">调整内容</th>
							<th style="width: 17px" align="center"></th>	
						</tr>
					</thead>
		    	 </table>
	              <div id="left_height2" style="overflow-y:auto;height:580px;">
	                  <table class="table table-bordered table-striped table-hover" style="border:0;">
    					<!-- <tbody data-bind='template: { name: "giftRowTemplate", foreach: modifyPlanRecords1 }'> -->
    					<tbody data-bind="foreach: modifyPlanRecords1" >
							<!-- ko foreach:{data:modifyRecordS,as : 'm'} -->
			                <tr>
                				<!-- ko if: $index() == 0 -->
                				<!-- <td style="width: 40px" data-bind="text:$index() + 1,attr: { rowspan:$parent.modifyRecordSLength}"></td> -->
						        <td style="width: 160px" data-bind="text: crossName,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 80px" data-bind="text: trainNbr,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 60px;padding: 4px 0;" data-bind="text: modifyPeopleBureau,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 80px" data-bind="text: modifyType,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 80px"  data-bind="text: modifyReason,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 80px" data-bind="text: startDate,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 80px" data-bind="text: endDate,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 70px" data-bind="text: rule,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 100px"  data-bind="text: selectedDate,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <!-- <td style="width: " data-bind="text: modifyContent,attr: { rowspan:$parent.modifyRecordSLength}"></td> -->
						        <td style="width: 70px" data-bind="text: modifyPeople,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <td style="width: 90px" data-bind="text: modifyPeopleOrg,attr: { rowspan:$parent.modifyRecordSLength}"></td>
						        <!-- /ko -->
						        <td style="width: 80px" data-bind="text: runDate "></td>
						        <td style="width: 80px"  data-bind="text: modifyTime  "></td>
						        <td style="width: " data-bind="text: modifyContent"></td>
							    <td style="width: 17px" align="center" class="td_17"></td>
						    </tr>
			                <!-- /ko -->	
			                
						</tbody>
					</table>
		     	<!-- 
				<table class="table table-bordered table-striped table-hover" id="yourTableID2" width="100%" border="0" cellspacing="0" cellpadding="0"  >
					<thead>
						<tr style="height: 25px"> 
							<th align="center" style="width: 40px">序号</th>
							<th align="center" style="width: 160px">交路名</th>
							<th align="center" style="width: 80px">车次</th>
							<th align="center" style="width: 80px">开行日期</th>
							<th align="center" style="width: 60px;padding: 4px 0;">调整局</th>
							<th align="center" style="width: 80px">调整时间</th>
							<th align="center" style="width: 80px">调整类型</th>
							<th align="center" style="width: 80px">调整依据</th>
							<th align="center" style="width: 80px">起始日期</th>
							<th align="center" style="width: 80px">终止日期</th>
							<th align="center" style="width: 70px">规律</th>
							<th align="center" style="width: 100px">择日日期</th>
							<th align="center" style="width: ">调整内容</th>
							<th align="center" style="width: 70px">调整人</th>
							<th align="center" style="width: 90px">岗位</th>
							<th style="width: 17px" align="center"></th>												
						</tr>
					</thead>
		    	 </table>
	              <div id="left_height2" style="overflow-y:auto;height:580px;">
	                  <table class="table table-bordered table-striped table-hover" style="border:0;">
						<tbody data-bind="foreach: modifyPlanRecords ">
							<tr>
						        <td style="width: 40px" data-bind="text:$index() + 1"></td>
						        
						        <td style="width: 160px" data-bind="text: crossName, attr:{title: crossName}"></td>

						        <td style="width: 80px" data-bind="text: trainNbr"></td>
						        
						        <td style="width: 80px" data-bind="text: runDate "></td>
						        <td style="width: 60px;padding: 4px 0;" data-bind="text: modifyPeopleBureau"></td>
						        <td style="width: 80px"  data-bind="text: modifyTime  "></td>
						        <td style="width: 80px" data-bind="text: modifyType  "></td>
						        <td style="width: 80px"  data-bind="text: modifyReason, attr:{title: modifyReason}"></td>
						        <td style="width: 80px" data-bind="text: startDate  "></td>
						        <td style="width: 80px" data-bind="text: endDate  "></td>
						        <td style="width: 70px" data-bind="text: rule  "></td>
						        <td style="width: 100px"  data-bind="text: selectedDate, attr:{title: selectedDate}"></td>
						        <td style="width: " data-bind="text: modifyContent , attr:{title: modifyContent} "></td>
						        <td style="width: 70px" data-bind="text: modifyPeople  , attr:{title: modifyPeople}  "></td>
						        <td style="width: 90px" data-bind="text: modifyPeopleOrg  , attr:{title: modifyPeopleOrg}  "></td>
							    <td style="width: 17px" align="center" class="td_17"></td>     
							</tr> 
						</tbody> 
					</table>
					 -->
		    	</div>
			</div>  
	  </div> 
  </div>
  
<div id="dhCross_dialog" class="easyui-dialog" title="交路不通过" data-options="iconCls:'icon-save'" style="width: 600px; height: 300px;overflow: hidden;">
     <iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
</div>
  
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

<script type="text/javascript" src="<%=basePath %>/assets/js/jsloader.js"></script>   

<script type="text/javascript"> 

        JSLoader.loadJavaScript(basePath + "/assets/js/jquery.js");
        JSLoader.loadJavaScript(basePath + "/assets/js/html5.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/bootstrap.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/respond.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/easyui/jquery.easyui.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/validate.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/knockout.js");  
        JSLoader.loadJavaScript(basePath + "/assets/js/jquery.freezeheader.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/ajaxfileupload.js");   
        JSLoader.loadJavaScript(basePath + "/assets/js/datepicker.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/jquery.gritter.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/common.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/respond.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/moment.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/lib/fishcomponent.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/resizable.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/knockout.pagemodle.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/moment.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/lib/fishcomponent.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/util/fishcomponent.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/util/canvas.util.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/util/canvas.component.js"); 
        if(all_role == "JHPT.KYJH.LJ.JJS"){
        	JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/canvas_rightmenu_only_sel.js");	
        }else{
        	JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/canvas_rightmenu.js");
        }
        //JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/canvas_rightmenu.js"); 
        //JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/canvas_event_getvalue.js");
     
        // JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/run_plan.js");
</script>    

<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/resizable.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script>  --%>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan.js"></script>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/validate.js"></script>   --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/moment.min.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/lib/fishcomponent.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>  --%>
<%-- <script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_rightmenu.js"></script> --%>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_event_getvalue.js"></script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
<script  type="text/html" id="runPlanTableDateHeader"> 
    <!-- ko if: $index() == 0 -->
 	<td class="space" rowspan="2" style="vertical-align: middle;">车次</td>
 	<!-- /ko -->
 	<td align='center' data-bind="text: day"></td>
    <!--<td class="space" rowspan="2"></td>-->
</script>
<script  type="text/html" id="runPlanTableWeekHeader">  
 	<td  style='border-bottom: 0;padding-top: 0;'  align='center' data-bind="html: week, style:{color: (weekDay==0||weekDay==6) ? 'blue':''}"></td>
</script>
<script  type="text/html" id="runPlanTableVlaues">
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
 	<td class="space" data-bind="text: $parent.trainNbr, attr:{title: $parent.trainNbr}"></td>
 	<!-- /ko -->  
 	<td style="vertical-align: middle;" align='center' data-bind="html: runFlagStr"></td>
 </tr> 
</script>

<script type="text/javascript">
	$(function() {
	   resize("leftBox","rightBox","500","650");
	 });
	$(document).ready(function(){
		/*还没写后台代码*/
		   $("#yes-btn").click(function(){
			   $("#plan_train_panel_body .fa-life-buoy").toggleClass("fa-spin");
			});
	});
</script>
</html>
