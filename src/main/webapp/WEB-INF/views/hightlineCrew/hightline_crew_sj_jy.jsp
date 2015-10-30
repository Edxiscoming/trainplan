<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
String  currentUserBureauName = user.getBureauShortName();
boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}
String currentUserBureau = user.getBureau();
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
} 
String basePath = request.getContextPath();
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>高铁司机乘务计划报告</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
<script type="text/javascript">
var currentUserBureau = "<%=currentUserBureau %>";
var currentUserBureauName ="<%=currentUserBureauName %>";
</script>
    <style type="text/css">
    .table thead > tr > th{
    padding: 0px 5px;
    }
    .table thead > tr.changePadding > th{
    padding: 4px 5px;
    }
    .form-horizontal .control-label{
    padding-top: 5px;
    }
    </style>
</head>
<body class="Iframe_body" style="margin:-8px -10px -5px -8px;width:100%">
<!-- <ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="javascript:void(0);">车底三乘计划 -> 高铁站段报告 -> 高铁司机乘务计划报告</a></li>
</ol> -->
<!--以上为必须要的--> 

<div class="row" style="padding-top:10px;padding-bottom:10px;">
  <form class="form-horizontal" role="form" style="margin-top:-12px;">
    <div class="form-group" style="float:left;margin-left:15px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left">日期:&nbsp;</label>
      <div class="pull-left">
        <input id="crew_input_rundate" type="text" class="form-control" style="width:100px;" placeholder="" data-bind="value: searchModle().runDate">
      </div>
    </div>
    <div class="form-group" style="float:left;margin-left:30px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left"> 车次:&nbsp;</label>
      <div class="pull-left">
        <input id="crew_input_trainNbr" type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().trainNbr">
      </div>
    </div>
    <div class="form-group" style="float:left;margin-left:30px;margin-bottom:0;">
      <label for="exampleInputEmail2" class="control-label pull-left"> 乘务员姓名:&nbsp;</label>
      <div class="pull-left">
        <input id="crew_input_xwyName" type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().xwyName">
      </div>
    </div>
     <div class="form-group" style="float:left;margin-left:5px;margin-top:7px; margin-bottom:0;">
         <label for="exampleInputEmail3" class="control-label" style="margin-left:30px;margin-top:-7px;">乘务组编号:&nbsp;</label>
		    <div style="margin-left:105px;margin-top:-6px;margin-right:70px;width:50px;">
		        <select  style="width:120px" class="form-control" data-bind="options:searchModle().crewGroups, value: searchModle().crewGroup,optionsCaption: '', optionsText: 'crewGroupName' "></select>						    
		    </div>  
     </div>
    <a type="button" href="#" class="btn btn-success btn-input" data-bind="click : queryList" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
  	<button type="button" class="btn btn-success btn-input"  style="float:left;margin-left:5px;margin-bottom:0;" data-bind="click: checkCrew, enable: checkAndSendBtnEnable"><i class="fa fa-check-square-o"></i>校验</button>
  	<button type="button" class="btn btn-success btn-input"  style="float:left;margin-left:5px;margin-bottom:0;" data-bind="click: sendCrew, enable: checkAndSendBtnEnable"><i class="fa fa-external-link"></i>提交</button>
  	 <li style="float:right;margin-right: 15px;">  					 
						<input type="checkbox" style="margin-top:9px;"  class="pull-left" data-bind="checked: isShowRunPlans, event:{change: showRunPlans}"> 
						<label for="exampleInputEmail5" class="control-label pull-left">显示开行情况</label>  
  	 </li>
  </form>
</div>



<!--左右分开-->
<div class="row">
	  
  
  <!--乘务计划-->
  <div id="all_div" style="margin-right:-550px; float:left; width:100%;">
    <!--分栏框开始-->
    <div id="panel_div" class="panel panel-default" style="margin:0 550px 0 15px;">
      <div id="head_div" class="panel-heading" >
        <h3 class="panel-title" > <i class="fa fa-user-md"></i>司机乘务计划</h3>
        <!--        <div class="col-md-8 col-sm-6 col-xs-4  pull-right" style=" width: 10%; text-align:right;">  <a  class="panel-title" href="application-Status.html" >返回</a></div>--> 
      </div>
      <!--panle-heading-->
      
      <div class="panel-body" style="padding:10px 5px;">
        <div class="row" style="margin-bottom:10px;">
          <button id="bjdd_btn_addWdCmd" type="button" class="btn btn-success btn-input" data-toggle="modal" data-bind="click : onAddOpen" data-target="#bjddAddWdjkCmdTrainModal"><i class="fa fa-plus"></i>添加</button>
          <button id="bjdd_btn_editWdCmd" type="button" class="btn btn-success btn-input" data-toggle="modal" data-bind="click:onEditOpen" data-target="#bjddAddWdjkCmdTrainModal"><i class="fa fa-pencil-square-o"></i> 修改</button>
          <button type="button" class="btn btn-success btn-input" data-bind="click : deleteHightLineCrew"><i class="fa fa-trash-o"></i>删除</button>
          <button type="button" class="btn btn-success btn-input" data-bind="" data-toggle="modal" data-target="#uploadCrewExcelModal"><i class="fa fa-sign-in"></i>导入EXCEL</button>
          <button type="button" class="btn btn-success btn-input" data-bind="click : exportExcel"><i class="fa fa-sign-out"></i>导出EXCEL</button>
        </div>
        <div class="table-responsive table-hover">
          <table class="table table-bordered table-striped table-hover">
            <thead>
              <tr>
                <th rowspan="2" style="width:40px;padding: 4px 0;"><input type="checkbox" class="checkbox-inline" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
                <th rowspan="2" style="width:50px; vertical-align: middle;">序号</th>
                <th rowspan="2" style="width:200px;padding:4px 0; vertical-align: middle">乘务交路</th>
                <th rowspan="2" style="width:110px;padding:4px 0;  vertical-align: middle">乘务组编号</th>
                <th rowspan="2" style="width:140px; vertical-align: middle">经由铁路线</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">司机1</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">司机2</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">备注</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:36px;padding: 4px 0;">提交<br>状态</th>
                <th rowspan="2" style="width: 17px" align="center"></th>
              </tr>
              <tr>
                <th class="text-center" style="width:80px;padding:4px 0;">姓名</th>
                <th class="text-center" style="width:100px;padding:4px 0;">电话</th>
                <th class="text-center" style="width:70px;padding:4px 0;">政治面貌</th>
                <th class="text-center" style="width:90px;padding:4px 0;">姓名</th>
                <th class="text-center" style="width:100px;padding:4px 0;">电话</th>
                <th class="text-center" style="width:70px;padding:4px 0;">政治面貌</th>
              </tr>
            </thead>
           </table>
          <div id="left_height" style="overflow-y:auto;">
	          <table class="table table-bordered table-striped table-hover" style="border: 0;">
	            <tbody data-bind="foreach: hightLineCrewRows.rows">
	              <tr data-bind="style:{color: $parent.currentRowCrewHighlineId() == $parent.crewHighlineId ? 'blue':''}">
	                <td align="center" style="width:40px;"><input name="crew_checkbox" type="checkbox" value="1" data-bind="checked: isSelect"></td>
	                <td style="width:50px;" data-bind="click: $parent.setCurrentRec, text: ($index() + 1)"></td>
	                <td style="width:200px;" data-bind="click: $parent.setCurrentRec, text: crewCross, attr:{title: crewCross}"></td>
	                <td style="width:110px;" data-bind="click: $parent.setCurrentRec, text: crewGroup, attr:{title: crewGroup}"></td>
	                <td style="width:140px;" data-bind="click: $parent.setCurrentRec, text: throughLine, attr:{title: throughLine}"></td>
	                <td style="width:80px;padding:4px 0;" align="center" data-bind="click: $parent.setCurrentRec, text: name1, attr:{title: name1}"></td>
	                <td style="width:100px;padding:4px 0;" align="center" data-bind="click: $parent.setCurrentRec, text: tel1, attr:{title: tel1}"></td>
	                <td style="width:70px;padding:4px 0;" data-bind="click: $parent.setCurrentRec, text: identity1, attr:{title: identity1}"></td>
	                <td style="width:90px;padding:4px 0;" data-bind="click: $parent.setCurrentRec, text: name2, attr:{title: name2}"></td>
	                <td style="width:100px;padding:4px 0;" align="center" data-bind="click: $parent.setCurrentRec, text: tel2, attr:{title: tel2}"></td>
	                <td style="width:70px;padding:4px 0;"  data-bind="click: $parent.setCurrentRec, text: identity2, attr:{title: identity2}"></td>
	                <td data-bind="click: $parent.setCurrentRec, text: note, attr:{title: note}"></td>
	                <td style="width:36px;padding: 6px 0 0 0;" align="center" data-bind="click: $parent.setCurrentRec,html : submitTypeStr"></td>
	                <td style="width:17px" class="td_17"></td>
	              </tr>
	            </tbody>
             </table>
           </div>
        </div>
      </div>
      <!--panel-body--> 
      
    </div>
    <!--分栏框结束--> 
  </div>
  <!--乘务计划end--> 
  <!--列车开行计划-->
  <div id="right_div" class="pull-right" style="width:540px;margin-right:-3px;margin-left:-5px;"> 
    <!--分栏框开始-->
    <div class="panel panel-default margin" style="margin-left: -7px;">
      <div class="panel-heading" >
        <h3 class="panel-title" > <i class="fa fa-list-alt"></i>列车开行计划</h3>

        <div class="pull-right" style="width: 204px;">
	      	<label for="exampleInputEmail3" class="control-label" style="margin-top:-15px;">
							路局:&nbsp;</label>
			<div class="pull-left" style="margin-left:5px;margin-top:-20px;width:50px;">
						        <select id="xiala" style="width:70px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName',event:{change: bureauChange} "></select>						    
			</div>  
	        <div class="pull-left">
	        <a type="button" style="margin-left:30px; margin-top: -20px" class="btn btn-input btn-success"
							data-toggle="modal" data-target="#" id="cross_train_save"
							data-bind="click: showCrossTrainTimeDlg"><i
								class="fa fa-list-ul"></i>时刻表</a>
			</div>
		</div>
							
									    
      </div>
      <!--panle-heading-->
      <div class="panel-body" style="padding:5px 5px;">
        <div class="table-responsive table-hover">
          <table border="0" class="table table-bordered table-striped table-hover">
            <thead>
              <tr class="changePadding">
                <th style="width:40px;">序号</th>
                <th style="width:70px;">车次</th>
                <th style="width:100px;">始发站</th>
                <th style="width:75px;">始发时间</th>
                <th style="width:100px;">终到站</th>
                <th style="width:75px;">终到时间</th>
                <th style="width:">途经局</th>
<!--            <th style="width:75px;padding:4px 0;">担当局</th>
                <th style="width:75px;padding:4px 0;">客运担当局</th>--> 
                <th style="width: 17px" align="center"></th>
              </tr>
            </thead>
          </table>
          <div id="right_height" style="overflow-y:auto;">
	          <table class="table table-bordered table-striped table-hover" style="border: 0;">          
			<tbody data-bind="foreach: planTrainRows.rows">
			  <tr data-bind="click: $parent.setCurrentTrainPlan,style:{color: $parent.currentRowPlanTrainId() == planTrainId ? 'blue':''}, attr:{class : isMatch()==1? 'success':''}">
                <td style="width:40px;" data-bind=" text: ($index() + 1)"></td>
                <td style="width:70px;" data-bind=" text: trainNbr, attr:{title: trainNbr}"></td>
                <td style="width:100px;" data-bind=" text: startStn, attr:{title: startStn}"></td>
                <td style="width:75px;" data-bind=" text: startTimeStr, attr:{title: startTimeStr}"></td>
                <td style="width:100px;" data-bind=" text: endStn, attr:{title: endStn}"></td>
                <td style="width:75px;" data-bind=" text: endTimeStr, attr:{title: endTimeStr}"></td>
                <td style="width:;" data-bind=" text: passBureau, attr:{title: passBureau}"></td>
<!--            <td style="width:75px;padding:4px 0;" data-bind=" text: tokenVehBureau, attr:{title: tokenVehBureau}"></td>
                <td style="width:75px;padding:4px 0;" data-bind=" text: tokenPsgBureau, attr:{title: tokenPsgBureau}"></td>--> 
                <td style="width:17px" class="td_17"></td>
              </tr>
            </tbody> 
          </table>
         </div>
        </div>
      </div>
      <!--panel-body--> 
      
    </div>
    
    <!--分栏框结束--> 
  </div>
  <!--列车开行计划 end--> 
  
  
  
  
  
  
  
  

  
</div>
<!--左右分开--> 













<!--新增/修改弹出框-->
<div class="modal fade" id="saveHightLineCrewModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" data-bind=""></h4>
      </div>
      
      <!--panel-heading-->
      <div class="panel-body row">
        <form id="hightLineCrewForm" class="bs-example form-horizontal" style="margin-top:10px;" data-bind="with : hightLineCrewModel">
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">乘务交路：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewCross" type="text" class="form-control" data-bind="value : crewCross">
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">乘务组编号：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewGroup" type="text" class="form-control" data-bind="value : crewGroup">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">经由铁路线：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_throughLine" type="text" class="form-control" data-bind="value : throughLine">
               </div>
          </div>
          
          
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">司机姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_name1" type="text" class="form-control" data-bind="value : name1">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_tel1" type="text" class="form-control" data-bind="value : tel1">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_identity1" type="text" class="form-control" data-bind="value : identity1">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">司机2姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_name2" type="text" class="form-control" data-bind="value : name2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_tel2" type="text" class="form-control" data-bind="value : tel2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_identity2" type="text" class="form-control" data-bind="value : identity2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">备注：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <textarea id="add_note" class="form-control" rows="4" data-bind="value : note"></textarea>
            </div>
          </div>
        </form>
        <!--        <p class="pull-right" style="margin:0;">说明：当您申请后需要等待管理员审批才能使用。</p>
--> </div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bind="click : saveHightLineCrew" data-dismiss="modal">确定</button>
        <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content --> 
  </div>
  <!-- /.modal-dialog --> 
</div>
<!--新增/修改弹出框 end-->






<!--导入弹窗-->
<div class="modal fade" id="uploadCrewExcelModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">导入司机乘务计划EXCEL</h4>
      </div>
      
      <!--panel-heading-->
      <div class="panel-body row">
      	<img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;">
        <form  id="file_upload_crew" name="file_upload_crew" action="crew/highline/importExcel" method="post" enctype="multipart/form-data" class="bs-example form-horizontal" style="margin-top:10px;">
          <div class="form-group">
            <div class="col-md-12 col-sm-12 col-xs-12">
              <input id="crewExcelFile" type="file" size="45" name="crewExcelFile"/>
            </div>
          </div>
        </form>
        <!--        <p class="pull-right" style="margin:0;">说明：当您申请后需要等待管理员审批才能使用。</p>
--> </div>
      <!--panel-body-->
      <div class="modal-footer">
        <button id="btn_fileToUpload" type="button" class="btn btn-primary" data-bind="click : uploadExcel" data-dismiss="modal">导入</button>
        <button id="btn_fileToUpload_cancel" type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
      </div>
    </div>
    <!-- /.modal-content --> 
  </div>
  <!-- /.modal-dialog --> 
</div>
<!-- 导入excel弹窗 end -->

<!--填报司机弹出框-->

<div id="add_sj_info_dialog" class="easyui-dialog" title="添加填报司机"
	data-options="iconCls:'icon-save'"
	style="width: 600px; height: 650px;overflow: hidden;">
	 <iframe style="width: 100%;border: 0;overflow: hidden;" src="" name=""></iframe>
</div>

<!--填报司机弹出框  end--> 



	<!--详情时刻表-->
	<div id="cross_train_time_dlg" class="easyui-dialog" title="时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 700px; height: 500px; padding: 10px;">
		<!--panle-heading-->
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
			<!-- Tab panes -->
			<div class="tab-content">
				<div class="tab-pane active" id="simpleTimes">
					<div class="table-responsive">
						<table class="table table-bordered table-striped table-hover"
							id="plan_runline_table_trainLine">
							<thead>

							        <tr>
							          <th style="width:50px">序号</th>
					                  <th style="width:100px">站名</th>
					                  <th style="width:60px">路局</th>
					                  <th style="width:80px">到达时间</th>
					                  <th style="width:80px">出发时间</th>
					                  <th style="width:60px">停时</th>
					                  <th style="width:70px">到达天数</th> 
					                  <th style="width:">出发天数</th> 
					                  <th style="width:70px">股道</th>  
					                  <th style="width: 17px" align="center"></th>
					                 </tr>

							</thead>
						</table>	
									<div id="simpleTimes_table"
											style="height: 350px; overflow-y: auto;">
											<table class="table table-bordered table-striped table-hover">
												<tbody data-bind="foreach: simpleTimes">
													<tr data-bind="visible: stationFlag != 'BTZ',attr:{class : isChangeValue()==1? 'danger':''}">

															<td style="width:50px" align="center" data-bind=" text: $index() + 1"></td>
															<td style="width:100px" data-bind="text: stnName, attr:{title: stnName}"></td>
															<td style="width:60px" align="center" data-bind="text: bureauShortName,attr:{title: bureauShortName}"></td>
															<td style="width:80px" align="center" data-bind="text: arrTime,attr:{title: arrTime}"></td>
															<td style="width:80px" align="center" data-bind="text: dptTime,attr:{title: dptTime}"></td>
															<td style="width:60px" align="center" data-bind="text: stepStr,attr:{title: stepStr}"></td>
															<td style="width:70px" align="center" data-bind="text: arrRunDays,attr:{title: arrRunDays}"></td>
															<td style="width:" align="center" data-bind="text: runDays,attr:{title: runDays}"></td>
															<td style="width:70px" align="center" data-bind="text: trackName,attr:{title: trackName}"></td>
															<td style="width:17px" class="td_17"></td>

													</tr>
												</tbody>
											</table>
										</div>
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








<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript"  src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript"  src="<%=basePath %>/assets/js/trainplan/hightlineCrew/hightline.crew.sj.jy.js"></script> 
</body>

</html>

