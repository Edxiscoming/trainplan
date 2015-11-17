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
<title>基本图车次查询</title>
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
<script src="<%=basePath %>/assets/js/bootstrap.min.js"></script> 
<script src="<%=basePath %>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/jquery.dataTables.js"></script>
<script src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
<script src="<%=basePath %>/assets/js/datepicker.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/moment.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/jbt_traininfo.js"></script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
<script type="text/html" id="tablefooter-short-template">  
 <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left" style="margin-right:10px">共<span data-bind="html: totalCount()"></span>条  当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span> 								 
  	 </td>
     <td style="width:40%;height:20px;padding:0px;float: right;text-align: right;">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table>
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
.table td, .table th {
max-width: none;
}
</style>
</head>
<body class="Iframe_body" style="padding:5px;">
<input id="basePath_hidden" type="hidden" value="<%=basePath %>">
<input id="tabType_hidden" type="hidden" value="${tabType}">
<input id="trainNbr_hidden" type="hidden" value="${trainNbr}">

<!--以上为必须要的-->


<div class="pull-left" style="width:60%;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default" style="margin-left:;">
       <div class="row" style="margin:10px 0 10px 0;"> 
		      <form class="form-horizontal" role="form">
		      	<div class="row">
			      	<div class="pull-left">
		  				<div class="row">
					     	<div class="row" style="width: 100%; margin-top: 5px;">
						  		<div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
						  			
						  			<label for="exampleInputEmail3" class="control-label pull-left" style="margin:3px 0 0 10px">方案:&nbsp;</label> 
									<div class="pull-left">
										<select style="width:230px" id="input_cross_chart_id"
											class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
										</select>
									</div>

						  			<label for="exampleInputEmail3" class="control-label pull-left" style="margin:3px 0 0 10px">始发局:&nbsp;</label> 
									<div class="pull-left">
										<select style="width: 45px" id="input_cross_chart_id"
											class="form-control" data-bind="options:searchModle().startBureaus(), value: searchModle().startBureau,  optionsValue: 'shortName', optionsText: 'shortName', optionsCaption: ''">
										</select>
									</div>
							     	<label for="exampleInputEmail3" class="control-label pull-left" style="margin:3px 0 0 10px"">终到局:&nbsp;</label> 
									<div class="pull-left">
										<select style="width:45px" id="input_cross_chart_id"
											class="form-control" data-bind="options:searchModle().endBureaus(), value: searchModle().endBureau, optionsValue: 'shortName', optionsText: 'shortName', optionsCaption: ''">
										</select> 
									</div>
						  			<label for="exampleInputEmail3" class="control-label pull-left" style="margin:3px 0 0 10px"> 车次：&nbsp;</label>
						            <div class="pull-left">
				                    	<input type="text" class="form-control" style="width:80px;" data-bind="value: searchModle().trainNbr, event:{keyup: trainNbrChange}" id="plan_construction_input_trainNbr">
								    </div>		
								    <label for="exampleInputEmail5" class="control-label pull-left" style="margin:3px 0 0 10px"">模糊</label>
									<div class="pull-left">
									<input type="checkbox" class="pull-left" class="form-control"
										value="1" data-bind="checked: searchModle().fuzzyFlag(), event:{change: fuzzyChange}"
										style="margin:6px 0 0 5px;" class="form-control">
									</div>					    
									
						  		</div>
						  	</div>
				  		</div>
				  	</div>
			     	<div style="float:left;margin-left:10px;margin-top:10px;margin-bottom:0;vertical-align: middle">
	  					<a type="button"  class="btn btn-success btn-input" data-toggle="modal" data-target="#" id="plan_construction_createRunLine"  data-bind="click: loadTrains"><i class="fa fa-search"></i>查询</a>
						<a type="button"  class="btn btn-success btn-input" data-toggle="modal" data-target="#" id="plan_construction_canvas_trainTime"  data-bind="click: showTrainTimeCanvas"><i class="fa fa-bar-chart-o"></i>运行图</a>
				 		<a id="bjdd_btn_useTrainStnAndTime" type="button" class="btn btn-success btn-input" data-bind="click: useTrainStnAndTime">套用经由及时刻</a>
						<a type="button"  class="btn btn-success btn-input" data-bind="click: useTrainStn"></i>套用经由</a>
	  			 	</div>
		     </div>
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
				 
		        <!--col-md-3 col-sm-4 col-xs-4-->
		      </form> 
		    </div> 
		     <div class="row" style="margin:10px 0 0px 0;"> 
		     	<div class="" data-bind="template: { name: 'tablefooter-short-template', foreach: trainRows }" style="margin:0 10px;"></div>
		     </div>
		     <div class="row" style="margin:10px 10px 10px 10px;height:550px; overflow-y:auto"> 
			     <div class="table-responsive" >
			          <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainInfo"> 
					        <thead>
					        <tr>
					          <th style="width:5%">序号</th>
			                  <th style="width:15%">车次</th>
			                  <th style="width:5%">始发局</th>
			                  <th style="width:15%">始发站</th>
			                  <th style="width:10%">始发时间</th>
			                  <th style="width:5%">终到局</th>
			                  <th style="width:15%">终到站</th>
			                  <th style="width:10%">终到时间</th>  
			                  <th style="width:10%">经由局</th>
			                  <th style="width:8%">运行天数</th>
			                 </tr>
					        </thead> 
					        <tbody data-bind="foreach: trainRows.rows">
					             <tr data-bind="click: $parent.showTrainTimes, style:{color: $parent.currentTrain() != null && $parent.currentTrain().name == name ? 'blue':''}">
					               <!-- data-bind="event:{change: $parent.checkboxSelect}, checked: selected" -->
								    <td align="center" data-bind="text: $parent.trainRows.currentIndex()+$index()+1"></td>
									<td data-bind=" text: name"></td>
									<td  align="center" data-bind="text: startBureau"></td>
									<td data-bind="text: startStn , attr:{title: startStn}"></td>
									<td align="center" data-bind="text: sourceTime"></td>
									<td align="center" data-bind="text: endBureau"></td>
									<td data-bind="text: endStn , attr:{title: endStn}"></td>
									<td align="center" data-bind="text: targetTime"></td>
									<td data-bind="text: routingBureau, attr:{title: routingBureau}"></td>
									<td align="center" data-bind="text: runDays"></td>
									 
								 </tr> 
					        </tbody>
					      </table> 
			        </div> 
		       </div>
		  </div> 
      </div> 
  <div class="pull-right" style="width:40%;padding-left:5px;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title" style="float: left;"> 
        	<i class="fa fa fa-folder-open"></i>时刻表  
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
		        <tr >
		          <th style="width:40px">序号</th>
                  <th style="width:">站名</th>
                  <th style="width:">路局</th>
                  <th style="width:">到达时间</th>
                  <th style="width:">出发时间</th>
                  <th style="width:">停时</th>
                   <th style="width:">到达天数</th> 
                   <th style="width:">出发天数</th>
                  <th style="width:">股道</th> 
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
					<td align="center" data-bind="text: arrRunDays"></td>
					<td align="center" data-bind="text: runDays"></td>
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





	   
<!--时刻表图形显示-->
<div id="run_plan_train_times_canvas_dialog" class="easyui-dialog" title="调整时刻表"
	data-options="iconCls:'icon-save'"
	style="width: 700px; height: 600px;overflow: hidden;">
	 <iframe style="width: 100%;border: 0;overflow: hidden;" src=""></iframe>
</div>
	
	
	
</body>
</html>