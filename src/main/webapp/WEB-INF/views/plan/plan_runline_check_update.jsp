<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
<link href="<%=basePath %>/assets/css/cross/custom-bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<!--font-awesome-->
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css" rel="stylesheet">
<link href="<%=basePath %>/assets/css/datepicker.css" rel="stylesheet">
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css"/>
<link  type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/datepicker.css">
<!-- Custom styles for this template -->
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<script src="<%=basePath %>/assets/js/jquery.js"></script>
<script src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/resizable.js"></script>
<script src="<%=basePath %>/assets/js/bootstrap.min.js"></script> 
<script src="<%=basePath %>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/jquery.dataTables.js"></script>
<script src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
<script src="<%=basePath %>/assets/js/datepicker.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/plan/plan_runline_check.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
<script type="text/html" id="tablefooter-short-template">  


 <div class="row footer" style="margin: 0 10px;">
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
	 });
</script>
<script type="text/html" id="tablefooter-long-template">  
	<span class="pagination pull-left">共<span data-bind="html: totalCount()"></span>条</td><td>当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span>
	<ul data-bind="foreach: new Array(pageCount())" class="pagination pull-right" style="margin: 0px; display: block;">
     
	  <!-- ko if: $index() == 0 -->
		<li data-bind="attr:{class: $parent.currentPage() == 0 ? 'disabled' : ''}"><a data-bind="text:'<<', click: $parent.loadPre"></a></li>
	  <!-- /ko --> 
 
	  <!-- ko if: $parent.pageCount() > 7 && $index() > 0 && $index() < $parent.pageCount() - 1 -->  
              <!-- ko if: ($index() > $parent.currentPage() - 2) && ($index() <= $parent.currentPage() + 2) -->
		            <!-- ko if: $index() == $parent.currentPage() - 1 && $parent.currentPage() > 3 && $parent.currentPage() < $parent.pageCount() - 4 --> 
                         <li><a>...</a></li>
	                     <li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	                <!-- /ko -->  
					<!-- ko if: $index() == $parent.currentPage() - 1 && ($parent.currentPage() <= 3 || $parent.currentPage() >= $parent.pageCount() - 4 )--> 
                         <li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	                <!-- /ko -->  
                   <!-- ko if: $index() > $parent.currentPage() - 1 && $index() <= $parent.currentPage() + 2 --> 
						 <li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	               <!-- /ko --> 
			  <!-- /ko -->   
              <!-- ko if: $index() <= $parent.currentPage() - 2 &&  $index() < 4-->   
						<li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
              <!-- /ko -->   

              <!-- ko if: $index() >= $parent.currentPage() + 2 && $index() > $parent.pageCount() - 4 -->  
						<li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
              <!-- /ko --> 
	  <!-- /ko --> 
  
	  <!-- ko if: $parent.pageCount() <= 7 && $index() > 0 && $index() < $parent.pageCount() - 1 --> 
	      <li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	  <!-- /ko -->
   
	  <!-- ko if: $index() == $parent.pageCount() - 1 -->
		<li data-bind="attr:{class: $parent.currentPage() == $parent.pageCount()-1 ? 'disabled' : ''}" style="cursor:pointer"><a data-bind="text:'>>', click: $parent.loadNext"></a></li>
	  <!-- /ko -->
   </ul> 
</script>
<style type="text/css">
.panel-title i:before{
margin-top:-17px;
}
.table td,.table th {
	text-align: center;
}
</style>
</head>
<body class="Iframe_body" style="margin-left: 11px; margin-top: -7px;margin-right: -5px;width:99%">
<input id="basePath_hidden" type="hidden" value="<%=basePath %>">
<div class="pull-left" style="width:62%;margin-left:-5px;" id="leftBox"> 
	<div class="row" style="margin:-3px 0 10px 0;"> 
      <form class="form-horizontal margin" role="form">
     
        <div class="pull-left" style="margin-left: -8px;">
        		<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:10px">
								方案:&nbsp;</label> 
				<div class="pull-left">
					<select style="padding:1px 6px; width:230px" id="input_cross_chart_id"
						class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
					</select>
				</div>  
		  <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:13px"> 
								始发局:&nbsp;</label> 
				<div class="pull-left">
					<select style="padding:1px 8px; width: 66px" id="input_cross_chart_id"
						class="form-control" data-bind="options:searchModle().startBureaus(), value: searchModle().startBureau,  optionsValue: 'shortName', optionsText: 'shortName', optionsCaption: ''">
					</select>
				</div>   
		     <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:13px">
								终到局:&nbsp;</label> 
				<div class="pull-left">
					<select style="padding:1px 8px; width:66px" id="input_cross_chart_id"
						class="form-control" data-bind="options:searchModle().endBureaus(), value: searchModle().endBureau, optionsValue: 'shortName', optionsText: 'shortName', optionsCaption: ''">
					</select> 
				</div>
		
          <div class="form-group" style="float:left;margin-left:13px;margin-bottom:0;">
          	<label for="exampleInputEmail3" style="margin-top:5px" class="control-label pull-left"> 车次:&nbsp;</label>
            <div class="pull-left">
                  	<input type="text" class="form-control" style="padding:4px 12px; width:100px;" data-bind="value: searchModle().trainNbr, event:{keyup: trainNbrChange}" id="plan_construction_input_trainNbr">
		    </div> 
          </div>

        </div> 
        <!--col-md-3 col-sm-4 col-xs-4-->
        <br/>
        <div class="pull-left" style="margin-top: 5px;">
        		<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;">
								列车类型:&nbsp;</label> 
				<div class="pull-left">
					<select style="padding:1px 6px; width:120px" id="input_cross_chart_id"
						class="form-control" data-bind="options:searchModle().businesses, value: searchModle().business, optionsText: 'name'">
					</select>
				</div>
				 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:13px"> 
								途经局:&nbsp;</label> 
				<div class="pull-left">
					<select style="padding:1px 8px; width: 66px" id="input_cross_chart_id"
						class="form-control" data-bind="options:searchModle().passBureaus, value: searchModle().passBureau,  optionsValue: 'shortName', optionsText: 'shortName', optionsCaption: ''">
					</select>
				</div>  
            <div class="pull-left">
			<input type="checkbox" class="pull-left" class="form-control"
				value="1" data-bind="checked: searchModle().fuzzyFlag(), event:{change: fuzzyChange}"
				style="width: 20px; margin-left: 25px; margin-top: 7px"
				class="form-control">
		</div>
		<label for="exampleInputEmail5" class="control-label pull-left" style="margin-top:5px;">
			模糊</label>    
		<a type="button"  style="margin-left:13px" class="btn btn-success btn-input" data-toggle="modal" data-target="#" id="plan_construction_createRunLine"  data-bind="click: loadTrains"><i class="fa fa-search"></i>查询</a>
		<a type="button"  style="margin-left:13px" class="btn btn-success btn-input" data-toggle="modal" data-target="#" id="plan_construction_canvas_trainTime"  data-bind="click: showTrainTimeCanvas"><i class="fa fa-bar-chart-o"></i>运行图</a>	 

		</div>
      </form> 
    </div> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
       		     	<div style="margin:8px 0;" data-bind="template: { name: 'tablefooter-short-template', foreach: trainRows }"></div>

		     <div class="row" style="margin:5px 10px 10px 10px;"> 
			     <div class="table-responsive" >
			          <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainInfo"> 									 
					        <thead>
					        <tr>
					          <th style="width:55px;">序号</th>
			                  <th style="width:100px;">车次</th>
			                  <th style="width:60px;">始发局</th>
			                  <th style="width:140px;max-width:140px">始发站</th>
			                  <th style="width:80px;max-width:80px">始发时间</th>
			                  <th style="width:60px;">终到局</th>
			                  <th style="width:140px;max-width:140px">终到站</th>
			                  <th style="width:80px;max-width:80px">终到时间</th>  
			                  <th style="width:px;">经由局</th>
			                  <th style="width:65px;">运行天数</th>
			                  <th style="width:146px;">有效期</th>
			                  <th style="width:50px;">操作</th>
			                  <th style="width: 17px" align="center"></th>
			                 </tr>
					        </thead> 
					   </table>

              <div id="left_height" style="overflow-y:auto;">
                  <table class="table table-bordered table-striped table-hover" style="border: 0;">
					        <tbody data-bind="foreach: trainRows.rows">
					             <tr data-bind="style:{color: $parent.currentTrain() != null && $parent.currentTrain().name == name ? 'blue':''}">
								    <td style="width:55px;" align="center" data-bind="click: $parent.showTrainTimes, text: $parent.trainRows.currentIndex()+$index()+1"></td>
									<td style="width:100px;" data-bind="click: $parent.showTrainTimes, text: name , attr:{title: name}"></td>
									<td style="width:60px;" align="center" data-bind="click: $parent.showTrainTimes, text: startBureau"></td>
									<td style="width:140px; max-width:140px" data-bind="click: $parent.showTrainTimes, text: startStn , attr:{title: startStn}"></td>
									<td style="width:80px;max-width:80px" align="center" data-bind="click: $parent.showTrainTimes, text: sourceTime"></td>
									<td style="width:60px;" align="center" data-bind="click: $parent.showTrainTimes, text: endBureau"></td>
									<td style="width:140px; max-width:140px" data-bind="click: $parent.showTrainTimes, text: endStn , attr:{title: endStn}"></td>
									<td style="width:80px;max-width:80px" align="center" data-bind="click: $parent.showTrainTimes, text: targetTime"></td>
									<td style="width:px;" align="center" data-bind="click: $parent.showTrainTimes, text: routingBureau, attr:{title: routingBureau}"></td>
									<td style="width:65px;" align="center" data-bind="click: $parent.showTrainTimes, text: runDays"></td>
									<td style="width:146px;" align="center" data-bind="click: $parent.showTrainTimes, text: expirationDate , attr:{title: expirationDate}"></td>
									<td style="width:50px;" align="center" data-bind="click: $parent.getTrainInfos"><span>修改</span></td>
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
  <div class="pull-right" style="width:37%;" id="rightBox"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default" style="margin-left:-10px;">
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
      	<div class="table-responsive" > 
          <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
		        <thead>
		         <tr>
		          <th style="width:40px">序号</th>
                  <th style="width:120px">站名</th>
                  <th style="width:40px">路局</th>
                  <th style="width:80px">到达时间</th>
                  <th style="width:80px">出发时间</th>
                  <th style="width:80px">停时</th>
                  <th style="width:63px">到达天数</th> 
                  <th style="width:63px">出发天数</th>
                  <th style="width:%">股道</th> 
                  <th style="width: 17px" align="center"></th>
                 </tr>
		        </thead>
					   </table>

              <div id="right_height" style="overflow-y:auto;">
                  <table class="table table-bordered table-striped table-hover" style="border: 0;">
		        <tbody data-bind="foreach: trainLines">
		           <tr>  
					<td style="width:40px" align="center" data-bind=" text: $index() + 1"></td>
					<td style="width:120px" align="center" data-bind="text: stnName, attr:{title: stnName}"></td>
					<td style="width:40px" align="center" data-bind="text: bureauShortName"></td>
					<td style="width:80px" align="center" data-bind="text: sourceTime"></td>
					<td style="width:80px" align="center" data-bind="text: targetTime"></td>
					<td style="width:80px" align="center" data-bind="text: stepStr"></td>
					<td style="width:63px" align="center" data-bind="text: arrRunDays"></td>
					<td style="width:63px" align="center" data-bind="text: runDays"></td>
					<td align="center" data-bind="text: trackName"></td>
					<td style="width: 17px" align="center" class="td_17"></td>
		        	</tr>
		        </tbody>
		      </table>
		    <div>
        </div>  
        </div>  
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
  </div>
  <!--分栏框结束--> 
</div>





	   
<!--时刻表图形显示-->
<div id="run_plan_train_times_canvas_dialog" class="easyui-dialog" title="调整时刻表"
	data-options="iconCls:'icon-save'"
	style="width: 1000px; height: 700px;overflow: hidden;">
	<input id="parentParamIsShowJt" type="hidden" value="1">
	<input id="parentParamIsShowTrainTime" type="hidden" value="0">
	 <iframe style="width: 100%;border: 0;" src=""></iframe>
</div>
<!--时刻表图形显示-->
<div id="run_plan_train_times_update" class="easyui-dialog" title="编辑列车信息"
	data-options="iconCls:'icon-save'"
	style="width: 1300px; height: 800px;overflow: hidden;">
	<input id="parentParamIsShowJt" type="hidden" value="1">
	<input id="parentParamIsShowTrainTime" type="hidden" value="0">
	 <iframe style="width: 100%;border: 0;" src=""></iframe>
</div>
	
	
	
</body>

</html>

