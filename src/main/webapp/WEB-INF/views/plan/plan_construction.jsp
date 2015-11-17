<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>核查编制图定开行</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<jsp:include page="/assets/commonpage/include-dwr-script.jsp" flush="true" /> 
</head>
<body class="Iframe_body">
<!--以上为必须要的-->

<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="#">货运运行线</a></li>
</ol> 
  <div class="pull-right" style="width:32%;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title" style="float: left;"> <i class="fa fa fa-folder-open"></i>生成运行线详情&nbsp;&nbsp;<label id="plan_view_div_palnDayDetail_title"></label></h3>
      </div>
      <!--panle-heading-->
      <div class="panel-body" style="padding:10px;">
      	<div class="table-responsive" > 
          <table class="table table-bordered table-striped table-hover" id="plan_review_table_trainLine">
		        <thead>
		        <tr>
		          <th style="width:25px">序号</th>
                  <th>站名</th>
                  <th style="width:40px">路局</th>
                  <th style="width:135px">到达时间</th>
                  <th style="width:135px">出发时间</th>
                  <th style="width:50px">停留时间</th>
                  <th style="width:20px">股道</th> 
                 </tr>
		        </thead>
		        <tbody>
		          
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


<!--分栏框开始-->
<div class="pull-left" style="width:67%;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
       <div class="row" style="margin:15px 0 10px 0;"> 
		      <form class="form-horizontal" role="form">
		      <button class="btn btn-primary" type="button" id="plan_construction_btnQuery">新增</button> 
		        <div class="pull-left">
		       		 <label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:&nbsp;</label>
				      <div class="pull-left">
				        <input type="text" class="form-control" style="width:150px;" placeholder="" id="plan_construction_selectdate">
				      </div>
		          <div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
		          	<label for="exampleInputEmail3" class="control-label pull-left"> 车次：&nbsp;</label>
		            <div class="pull-left">
                    	<input type="text" class="form-control" style="width:180px;" placeholder="输入车次查询" id="plan_construction_input_trainNbr">
				    </div> 
		          </div>
		        </div> 
					
					 &nbsp;&nbsp;&nbsp;<a type="button" class="btn btn-success" data-toggle="modal" data-target="#" id="plan_construction_createRunLine">生成运行线</a>
				 
		        <!--col-md-3 col-sm-4 col-xs-4-->
		      </form> 
		    </div> 
		     <div class="table-responsive" style="margin:10px 10px 10px 10px;">
		          <table class="table table-bordered table-striped table-hover" id="plan_review_table_trainInfo"> 
				        <thead>
				        <tr>
				          <th style="width:25px"></th>
		                  <th style="width:100px">车次</th>
		                  <th style="width:25px">始发局</th>
		                  <th style="width:150px">始发站</th>
		                  <th style="width:130px">始发时间</th>
		                  <th style="width:150px">终到站</th>
		                  <th style="width:130px">终到时间</th> 
		                   <th style="width:250px">状态</th> 
		                 </tr>
				        </thead> 
				        <tbody>
				          
				        </tbody>
				      </table>
		        </div> 
		  </div> 
      </div>  
 
<script src="<%=basePath %>/assets/js/trainplan/plan/plan_construction.js"></script> 
</body>
</html>