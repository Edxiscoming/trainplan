<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>核查编制图定开行</title>
<!-- Bootstrap core CSS -->
<jsp:include page="/assets/commonpage/include-canvas-base.jsp" flush="true" />
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<!--font-awesome-->
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css"/>
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/datepicker.css">
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<script src="<%=basePath %>/assets/js/jquery.js"></script>
<script src="<%=basePath %>/assets/js/html5.js"></script>
<script src="<%=basePath %>/assets/js/bootstrap.min.js"></script> 
<script src="<%=basePath %>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/jquery.dataTables.js"></script>
<script src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
<script src="<%=basePath %>/assets/js/datepicker.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/resizable.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/plan/plan_review_GTlines.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/moment.min.js"></script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
<!-- 
<script type="text/html" id="tablefooter-short-template">  
 <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-right" style="margin-right:10px">共<span data-bind="html: totalCount()"></span>条  当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span> 								 
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
 -->
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

<script type="text/javascript">

	$(function() {
	   resize("leftBox","rightBox","1050","1200");
	   //resize("downleftBox","downRightBox","100","900");
	 });

</script>
<style type="text/css">
.panel-title i:before{
margin-top: -17px;
}
.panel-title i{
line-height: 31px;
}
</style> 
</head>
<body class="Iframe_body" style="margin-left:15px;">
<input id="basePath_hidden" type="hidden" value="<%=basePath %>">
<!--以上为必须要的-->
<%-- 
	<ol class="breadcrumb" style="text-align: left;">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		    <c:if test="${train_type==0}">
        <li><a href="#">发布开行计划 -> 既有运行线查询</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">发布开行计划-> 高铁运行线查询 </a></li>
    </c:if>
	</ol>   --%>
<div class="pull-left" style="width:63%;margin-left: -10px;margin-top:-5px;" id="leftBox"> 
    <!--分栏框开始-->
    <div class="row" style="margin:-3px 0 13px -14px;"> 
		      <form class="form-horizontal" role="form">
		     
		        <div class="pull-left"> 
         <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:15px;margin-top:5px;">
													开始日期:&nbsp;</label>
					<div class="pull-left" style="margin-left: 5px;">
						<input type="text" class="form-control" style="padding:4px 12px; width:95px;" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
					</div>
				 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:15px">
													截止日期:&nbsp;</label>
					<div class="pull-left" style="margin-left: 5px;">
						<input type="text" class="form-control" style="padding:4px 12px; width:95px;" placeholder="" id="runplan_input_endDate"  name="endDate" data-bind="value: searchModle().planEndDate" />
					</div>
				  <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:15px">始发局:&nbsp;</label> 
				   <div class="pull-left">
						<select style="padding:4px 12px; width: 65px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().startBureaus(), value: searchModle().startBureau,  optionsValue: 'shortName', optionsText: 'shortName', optionsCaption: ''">
					   </select>
				   </div>  
				  	<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:15px">终到局:&nbsp;</label> 
				   <div class="pull-left">
						<select style="padding:4px 12px; width: 65px" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().endBureaus(), value: searchModle().endBureau,  optionsValue: 'shortName', optionsText: 'shortName', optionsCaption: ''">
					   </select>
				   </div> 
		           
				
					
					 
					<br/>
					<br/>
					&nbsp;
					<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:15px;">落成状态:&nbsp;</label> 

				   <div class="pull-left">
						<select style="padding:4px 12px; width: 95px;margin-left:5px;" id="input_cross_chart_id"
								class="form-control" data-bind="options:searchModle().lineReloadStatuss, value: searchModle().lineReloadStatus,  optionsValue: 'value', optionsText: 'text', optionsCaption: ''">
					   </select>
				   </div>
					<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:15px">车次:&nbsp;</label>
					<div class="pull-left" style="margin-left: 5px;">
						<input type="text" class="form-control" style="padding:4px 12px; width:95px;"id="trainNbr"  name="trainNbr" data-bind="value: searchModle().trainNbr" />
					</div>
					

		<!-- 			<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 20px;" >
													模糊:</label>
 -->
					<div class="pull-left" style="margin-left: 15px;margin-top:5px">
						<input type="checkBox" class="pull-left" class="form-control"

						value="1" data-bind="checked: searchModle().fuzzyFlag(), event:{change: fuzzyChange}"
						style="width: 20px;" class="form-control">

					
		</div>
					
					
				
											 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 3px;" >
													模糊</label> 

				 <a type="button"  style="margin-left:12px" class="btn btn-success  btn-input" data-toggle="modal" data-target="#" id="plan_construction_createRunLine"  data-bind="click: loadTrains"><i class="fa fa-search"></i>查询</a>
				 <a type="button"  style="margin-left:12px" class="btn btn-success btn-input" data-toggle="modal" data-target="#" id="plan_construction_canvas_trainTime"  data-bind="click: showTrainTimeCanvas"><i class="fa fa-bar-chart-o"></i>运行图</a>
				
			
				 </div>
		        <!--col-md-3 col-sm-4 col-xs-4-->
		      </form> 
		    </div> 
    <div id="plan_view_div_palnDayDetail" class="panel panel-default" style="margin-top: -5px;">
       
		
		     	<div data-bind="template: { name: 'tablefooter-short-template', foreach: trainRows }" style="margin: 10px 6px 0 6px;"></div>
		   
		  <!--      <div data-bind="template: { name: 'tablefooter-short-template', foreach: trainRows }" style="margin: 10px 0;"></div>--> 
		     
		     <div class="row" style="margin:10px;"> 
			     <div class="table-responsive" >
			          <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainInfo"> 
					        <thead>
					        <tr>
					          <th style="width:55px">序号</th>
			                  <th style="width:110px">车次</th>
			                  <th style="width:80px">始发局</th>
			                  <th style="width:110px">始发站</th>
			                  <th style="width:100px">始发时间</th>
			                  <th style="width:80px">终到局</th>
			                  <th style="width:110px">终到站</th>
			                  <th style="width:100px">终到时间</th>  
			                  <th style="width:">经由局</th>                          
			                   <th style="width:80px">运行天数</th>                      
	                           <th style="width:45px;">落成</th>
			                  <th  style="width:45px;">冗余</th>
			                  <th rowspan="2" style="width: 17px" align="center"></th>
			                 </tr>
					        </thead> 
					   </table>
              <div id="left_height" style="overflow-y:auto;">
                  <table class="table table-bordered table-striped table-hover" style="border: 0;">
					        <tbody data-bind="foreach: trainRows.rows">
					             <tr data-bind="click: $parent.showTrainTimes, style:{color: $parent.currentTrain() != null && $parent.currentTrain().id == id ? 'blue':''}">
								    <td style="width:55px" align="center" data-bind="text: $parent.trainRows.currentIndex()+$index()+1"></td>
									<td style="width:110px" data-bind=" text: name"></td>									
									<td style="width:80px" align="center" data-bind="text: startBureau"></td>
									<td style="width:110px" data-bind="text: startStn , attr:{title: startStn}"></td>
									<td style="width:100px" align="center" data-bind="text: sourceTime"></td>
									<td style="width:80px" align="center" data-bind="text: endBureau"></td>
									<td style="width:110px" data-bind="text: endStn , attr:{title: endStn}"></td>
									<td style="width:100px" align="center" data-bind="text: targetTime"></td>
									<td data-bind="text: routingBureau, attr:{title: routingBureau}"></td>								
									<td style="width:80px" align="center" data-bind="text: target_time_schedule_dates"></td>
							        <td style="width:45px;" align="center" style="vertical-align: middle;" data-bind="html: checklev1breauStr"></td>
								
									<td style="width:45px;" align="center" style="vertical-align: middle;" data-bind="html: redundanceStr"> 
																	  
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
      <div class="blankBox"></div>
  <div class="pull-left" style="width:36%;" id="rightBox"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
     
      
      <div class="panel-heading" style="height:33px;">
        <h3 class="panel-title" style="float: left;"> 
        	<i class="fa fa fa-folder-open" style="line-height: 32px;padding-right: 7px;"></i>时刻表  
        	<span><input type="checkbox" id="input_checkbox_stationType_jt" name="input_checkbox_stationType" checked="checked" style="margin-left:10px">简点</span>
        	<span style="margin-left:5px" data-bind="text:currentTrain() == null ? '' : '车次:' + currentTrain().name"></span>
        	<label id="plan_view_div_palnDayDetail_title"></label>
        </h3>
      </div>
      
      <!--panle-heading-->
      <div class="panel-body" style="padding:10px;">
      	<div class="table-responsive" id="right_height"> 
          <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
		        <thead>
		        <tr >
		          <th style="width:5%">序号</th>
                  <th style="width:20%">站名</th>
                  <th style="width:5%">路局</th>
                  <th style="width:15%">到达时间</th>
                  <th style="width:15%">出发时间</th>
                  <th style="width:5%">停时</th>
                   <th style="width:10%">到达天数</th> 
                   <th style="width:10%">出发天数</th> 
                  <th style="width:10%">股道</th> 
                 </tr>
		        </thead>
		        <tbody data-bind="foreach: trainLines">
		           <tr>  
					<td align="center" data-bind=" text: $index() + 1"></td>
					<td data-bind="text: stnName, attr:{title: stnName}"></td>
					<td align="center" data-bind="text: bureauShortName"></td>
					<td align="center" data-bind="text: sourceTime"></td>
					<td align="center" data-bind="text: targetTime"></td>
					<td align="center" data-bind="text: stepStr"></td>
					 <td align="center" data-bind="text: sourceDay"></td>
					<td align="center" data-bind="text: targetDay"></td> 
					
					<!--rundays    self.sourceDay =  data.sourceDay;
               self.targetDay = data.targetDay;  改为到达天数和出发天数  -->
					
					<td align="center" data-bind="text: trackName"></td>  
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
  <!--分栏框结束--> 

</div> 

<div id="run_plan_train_times_canvas_dialog2" class="easyui-dialog" 
	data-options="iconCls:'icon-save'"
	style="width: 700px; height: 600px;" title="列车运行时刻图">
	 <iframe style="width: 100%;border: 0;" src=""></iframe>
</div>	   
<!--时刻表图形显示-->
<!-- <div id="run_plan_train_times_canvas_dialog" class="easyui-dialog" 
	data-options="iconCls:'icon-save'"
	style="width: 700px; height: 600px;overflow: hidden;" title="列车运行时刻图">
	 <iframe style="width: 100%;border: 0;overflow: hidden;" src=""></iframe>
</div>
 -->
 


</body>

</html>
