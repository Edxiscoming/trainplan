<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>客运计划编制-消息</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link rel="stylesheet" type="text/css" media="screen" href="${basePath }/assets/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="${basePath }/assets/css/table.scroll.css">
<link rel="stylesheet" type="text/css" media="screen" href="${basePath }/assets/css/table.scroll.css">
<link rel="stylesheet" type="text/css" media="screen" href="${basePath }/assets/css/jquery.autocomplete.css">
<!-- 自动补全插件 -->
<link rel="stylesheet" type="text/css" media="screen" href="${basePath }/assets/station/station.css">

<script type="text/javascript" src="${basePath }/assets/station/station.js"></script>
<script type="text/javascript">
	var basePath = "${basePath }";
	var bureauShortName = "${user.bureauShortName }";
	var bureau = "${user.bureau }";
</script>
<style>
	.form-horizontal .control-label, .form-horizontal .radio, .form-horizontal .checkbox, .form-horizontal .radio-inline, .form-horizontal .checkbox-inline{
	padding-top:3px;
	}
	.table thead > tr > th {
	padding: 0px 5px;
	}
	.table-cc input,.table-cc select{
	padding:4px 4px;
	text-align: center;
	}
	.table-cc .table_input{
	line-height: 25px;
	}
</style>
</head>
<body class="Iframe_body" style="margin:-24px 12px 0px -4px">
<!--以上为必须要的--> 
<div class="panel panel-default" style="margin:15px -7px 0px 10px;">
	<div class="panel-body" style="padding: 10px 10px 0 10px;">
		<div class="bs-example bs-example-tabs">
		      <ul id="myTab" class="nav nav-tabs">
		      	<!-- 
		        <li class="active"><a href="#msg_receive" data-toggle="tab" data-bind="click:cleanPageData">收到的消息</a></li>
		        <li class=""><a href="#msg_send" data-toggle="tab" data-bind="click:cleanPageData">发出的消息</a></li>
		         -->
		        <li class="active"><a href="#msg_receive" data-toggle="tab" data-bind="click:cleanPageData">收到的消息</a></li>
		        <li class=""><a href="#msg_send" data-toggle="tab" data-bind="click:cleanPageData">发出的消息</a></li>
		      </ul>
		    <div id="myTabContent" class="tab-content" >
		  	<!--tab1 开始-->
			  <div id="msg_receive" class="tab-pane fade active in row" style="position: relative;">
			  		<!--  -->
					  <div id="left_div" style="float:left; width:100%;">

					    <!--分栏框开始-->
					    <div id="left_div_panel" class="panel" style="width:100%;margin:0 532px 10px 0;">
							<div class="row" style="padding-bottom:10px;">
							  <form class="form-horizontal" role="form">
							  <input type="hidden" id="currentB"/>
							  	<div class="row">
							  		<div class="pull-left">
								  		<div class="row">
								  			<div class="row" style="width: 100%; margin-top: 5px; ">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
										  			<label for="exampleInputEmail2" class="control-label pull-left">开始日期:&nbsp;</label>
												    <div class="pull-left">
												   		<input type="text" class="form-control" style="padding: 4px 12px; width: 95px; margin-top: 3px" placeholder="" id="receive_start_date" name="receive_start_date" data-bind="value: startDate" />
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">&nbsp;&nbsp;发消息局:&nbsp;</label>
												    <div class="pull-left">
												    	<select id="receive_msg_bureaus" style="padding:4px 12px; ;width:130px;  margin-top: 3px" class="form-control" 
												    	data-bind="options:bureaus(), value: bureaus(), optionsText: 'name', optionsValue:'id'"></select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left"  style="width:100px;">发消息人:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" id="sendPeople" class="form-control" style="width:130px;" data-bind="value: sendPeople">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:90px;">&nbsp;&nbsp;签收状态:&nbsp;</label>
												    <div class="pull-left">
												        <select id="receive_msg_status" style="padding:4px 12px; ;width:130px;  margin-top: 3px" class="form-control" 
												    	data-bind="options:receiveMsgStatus(), value: receiveMsgStatus(), optionsText: 'name', optionsValue:'id'"></select>
												    </div>
													
										  		</div>
										  	</div>
										  	<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
										  			<label for="exampleInputEmail2" class="control-label pull-left">截至日期:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="padding: 4px 12px; width: 95px; margin-top: 3px" placeholder="" id="receive_end_date" name="receive_end_date" data-bind="value: endDate()" />
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">消息类型:&nbsp;</label>
												    <div class="pull-left">
												    	<select id="receive_msg_types" style="padding:4px 12px; ;width:130px;  margin-top: 3px" class="form-control" 
												    	data-bind="options:msgType(), value: msgType(), optionsText: 'name', optionsValue:'code'"></select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:100px;">&nbsp;&nbsp;内容关键字:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" id="kw" class="form-control" style="width:130px;" data-bind="value: kw" />
													</div>
												    <div class="pull-left">
											  			<a type="button" href="#" class="btn btn-success btn-input" data-bind="click : loadReceiveMsg" style="float:left;margin-left:33px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
											  			<a type="button" href="#" class="btn btn-success btn-input" data-bind="click : receiveOk" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-pencil-square-o"></i>签收</a>
											  			<!-- <a type="button" href="#" class="btn btn-success btn-input" data-bind="click : receiveDel" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-trash-o"></i>删除</a> -->
										  			</div>
										  		</div>
										  	</div>
								  		</div>
							  		</div>
							  	</div>
							  </form>
							</div>
					      
					      <div class="panel-body" style="padding:0; overflow:scroll;">
					        <!-- 既有、高铁加开table div -->
					        <div id="div_bjdd_cmdTrainTable_jk" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:5px"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}"></th>
					                <th class="text-center" style="vertical-align: middle;width:50px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:110px">消息类型</th>
					                <th class="text-center" style="vertical-align: middle;">消息内容</th>
					                <th class="text-center" style="vertical-align: middle;width:50px">发消息<br>局</th>
					                <th class="text-center" style="vertical-align: middle;width:150px">发消息<br>岗位</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">发消息<br>人</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">发消息<br>时间</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">签收<br>人</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">签收<br>时间</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">签收<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:17px" align="center"></th>
					              </tr>
					            </thead>
				    	 </table>
		              <div id="left_height" style="overflow-y:auto;">
		                  <table class="table table-bordered table-striped table-hover" style="border:0;">
					            <tbody data-bind="foreach: receiveMsg">
					              <tr data-bind="style:{color: $parent.receiveMsg().id == id ? 'blue':''}">
					              	<td style="width:5px;"><input name="cmd_list_checkbox" value="1" type="checkbox" data-bind="event:{change: $parent.selectCross}, checked: selected"></td>
					                <td style="width:50px;" data-bind="text: ($index() + 1)"></td>
					                <td style="width:110px;padding:4px 2px;" data-bind="text: typeName, attr:{title: typeName}"></td>
					                <td style="padding:4px 2px;white-space: normal;" data-bind="text: msgContents, attr:{title: msgContents}"></td>
					                <td style="width:50px;padding:4px 2px;" data-bind="text: sendBureau, attr:{title: sendBureau}"></td>
					                <td style="width:150px;padding:4px 2px;" data-bind="text: sendPost, attr:{title: sendPost}"></td>
					                <td style="width:80px;padding:4px 2px;" data-bind="text: sendPeople, attr:{title: sendPeople}"></td>
					                <td style="width:100px;padding:4px 2px;" data-bind="text: sendTime, attr:{title: sendTime}"></td>
					                <td style="width:80px;padding:4px 2px;" data-bind="text: qsPeople, attr:{title: qsPeople}"></td>
					                <td style="width:100px;padding:4px 2px;" data-bind="text: qsTime, attr:{title: qsTime}"></td>
					                <td style="width:100px;padding:4px 2px;vertical-align: middle;text-align:center;" data-bind="html: selectStateStr"></td>
					              	<td style="width: 17px" align="center" class="td_17"></td>
					              </tr>
					            </tbody>
					          </table>
					         </div>
					        </div>
					      </div>
					    </div>
					    <!--分栏框结束--> 
					  </div>
			  		<!-- tab1内容结束 -->
			  </div>
		  	<!--tab1  结束--> 
		  	
	        <!--tab2   开始-->
	        <div id="msg_send" class="tab-pane fade" style="position: relative;">
			  		<!--  -->
					  <div id="left_div" style="float:left; width:100%;">

					    <!--分栏框开始-->
					    <div id="left_div_panel" class="panel" style="width:100%;margin:0 532px 10px 0;">
							<div class="row" style="padding-bottom:10px;">
							  <form class="form-horizontal" role="form">
							  <input type="hidden" id="currentB"/>
							  	<div class="row">
							  		<div class="pull-left">
								  		<div class="row">
								  			<div class="row" style="width: 100%; margin-top: 5px; ">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">开始日期:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="padding: 4px 12px; width: 95px; margin-top: 3px" placeholder="" id="send_start_date" name="send_start_date" data-bind="value: startDate()" />
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">签收局:&nbsp;</label>
												    <div class="pull-left">
												    	<select id="send_msg_bureaus" style="padding:4px 12px; ;width:130px;  margin-top: 3px" class="form-control" 
												    	data-bind="options:bureaus(), value: bureaus(), optionsText: 'name', optionsValue:'id'"></select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:100px;">&nbsp;&nbsp;签收人:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" id="receive_people" class="form-control" style="width:130px;" data-bind="value: sendPeople()">
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:90px;">&nbsp;&nbsp;签收状态:&nbsp;</label>
												    <div class="pull-left">
												        <select id="send_msg_status" style="padding:4px 12px; ;width:130px;  margin-top: 3px" class="form-control" 
												    	data-bind="options:sendMsgStatus(), value: sendMsgStatus(), optionsText: 'name', optionsValue:'id'"></select>
													</div>
										  		</div>
										  	</div>
										  	<div class="row" style="width: 100%; margin-top: 5px;">
										  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
													<label for="exampleInputEmail2" class="control-label pull-left">截至日期:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" class="form-control" style="padding: 4px 12px; width: 95px; margin-top: 3px" placeholder="" id="send_end_date" name="send_end_date" data-bind="value: endDate()" />
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:85px;">消息类型:&nbsp;</label>
												    <div class="pull-left">
												    	<select id="send_msg_types" style="padding:4px 12px; ;width:130px;  margin-top: 3px" class="form-control" 
												    	data-bind="options:msgType(), value: msgType(), optionsText: 'name', optionsValue:'code'"></select>
													</div>
													<label for="exampleInputEmail2" class="control-label pull-left" style="width:100px;">&nbsp;&nbsp;内容关键字:&nbsp;</label>
												    <div class="pull-left">
												        <input type="text" id="send_kw" class="form-control" style="width:130px;" data-bind="value: kw()" />
												    </div>
													<div class="pull-left">
											  			<a type="button" href="#" class="btn btn-success btn-input" data-bind="click : loadSendMsg" style="float:left;margin-left:33px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
										  			</div>
										  		</div>
										  	</div>
								  		</div>
							  		</div>
							  	</div>
							  </form>
							</div>
					      
					      <div id="left_height2" class="panel-body" style="padding:0; overflow:scroll;">
					        <!-- 既有、高铁加开table div -->
					        <div id="div_bjdd_cmdTrainTable_jk" class="table-responsive table-hover">
					          <table id ="bjdd_jk_runPlanLkCMD_table_send" class="table table-bordered table-striped table-hover">
					            <thead>
					              <tr>
					                <th class="text-center" style="vertical-align: middle;width:50px">序号</th>
					                <th class="text-center" style="vertical-align: middle;width:110px">消息类型</th>
					                <th class="text-center" style="vertical-align: middle;">消息内容</th>
					                <th class="text-center" style="vertical-align: middle;width:150px">发消息<br>岗位</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">发消息<br>人</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">发消息<br>时间</th>
					                <th class="text-center" style="vertical-align: middle;width:50px">签收<br>局</th>
					                <th class="text-center" style="vertical-align: middle;width:150px">签收<br>岗位</th>
					                <th class="text-center" style="vertical-align: middle;width:80px">签收<br>人</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">签收<br>时间</th>
					                <th class="text-center" style="vertical-align: middle;width:100px">签收<br>状态</th>
					                <th class="text-center" style="vertical-align: middle;width:17px" align="center"></th>
					              </tr>
					            </thead>
				    	 </table>
		              <div style="overflow-y:auto;">
		                  <table class="table table-bordered table-striped table-hover" style="border:0;">
					            <tbody data-bind="foreach: sendMsg" >
									<!-- ko foreach:{data:receiveList,as : 'm'} -->
					                <tr >
		                				<!-- ko if: $index() == 0 -->
					                	<td style="width:50px;" data-bind="text: indexI,attr: { rowspan:$parent.sendMsgLength}""></td>
								        <td style="width: 110px" data-bind="text: typeName,attr: { rowspan:$parent.sendMsgLength}"></td>
								        <td style="white-space: normal;" data-bind="text: msgContents,attr: { rowspan:$parent.sendMsgLength}"></td>
								        <td style="width: 150px" data-bind="text: sendPost,attr: { rowspan:$parent.sendMsgLength}"></td>
								        <td style="width: 80px" data-bind="text: sendPeople,attr: { rowspan:$parent.sendMsgLength}"></td>
								        <td style="width: 100px" data-bind="text: sendTime,attr: { rowspan:$parent.sendMsgLength}"></td>
								         <!-- <td style="width: 10px" data-bind="text: receiveBureau,attr: { rowspan:$parent.sendMsgLength}"></td> -->
								        <!-- /ko -->
								        <td style="width: 50px" data-bind="text: receiveBureau "></td>
								        <td style="width: 150px" data-bind="text: receivePost  "></td>
								        <td style="width: 80px" data-bind="text: qsPeople"></td>
								        <td style="width: 100px" data-bind="text: qsTime"></td>
								        <!-- <td style="width: 10px" data-bind="text: receiveStatus"></td> -->
								        <td style="width:100px;padding:4px 2px;vertical-align: middle;text-align:center;" data-bind="html: receiveStatusSpan"></td>
								        <td style="width: 17px" align="center" class="td_17"></td>
								    </tr>
					                <!-- /ko -->	
					                
								</tbody>
					          </table>
					         </div>
					        </div>
					      </div>
					    </div>
					    <!--分栏框结束--> 
					  </div>
			  		<!-- tab1内容结束 -->
			  </div>
	        	<!-- tab2内容结束 -->
	        </div>
	        <!--tab2  结束-->
	        
	        </div>
		</div>
	</div>
</div>


<script type="text/javascript" src="${basePath }/assets/easyui/jquery.easyui.min.js"></script>
<!-- 锁定命令列表table表头 -->
<script type="text/javascript" src="${basePath }/assets/js/chromatable_1.js"></script>
<script type="text/javascript" src="${basePath }/assets/js/knockout.validation.min.js"></script>
<!-- <script type="text/javascript" src="${basePath }/assets/js/trainplan/runPlanLk/runPlanLk_cmd_add.js"></script> -->
<script type="text/javascript" src="${basePath }/assets/js/trainplan/message/msg.js"></script>
<script type="text/javascript" src="${basePath }/assets/js/trainplan/validate.js"></script>
<script type="text/javascript">

function changeColor(feild){
		  	var count = 0;
			  var trList =feild.parentElement.parentElement.parentElement.children;
			 
		      for (var i=0;i<trList.length;i++) {
		        var tdArr = trList[i].children;
		        
		        if(tdArr[0].firstChild.checked==true){
		        	count++;
		        }
		     	console.dir(tdArr[0].firstChild.checked)
		         
		    
		      }
			 if(count==1){
				 if(feild.checked){
					feild.parentElement.parentElement.style.color="blue"
				}
			 }else{
				 feild.parentElement.parentElement.style.color=""
			 }
}

</script>

<script type="text/javascript">
	$(function() {
	    $("#time_div").resizable({
    	    minWidth: 520,
            maxWidth: 1300,
            handles: 'w', 
            //edge:4,  
            onStartResize: function(e){
               // alert("左");
            },            
            onResize: function(e){
            }            
  		  });
	    $("#time_div2").resizable({
    	    minWidth: 520,
            maxWidth: 1300,
            handles: 'w', 
            //edge:4,  
            onStartResize: function(e){
            },            
            onResize: function(e){
            }            
  		  });
	});
</script>
</body>
 

</html>
