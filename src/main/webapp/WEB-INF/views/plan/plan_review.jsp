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

</head>
<body class="Iframe_body">
<!--以上为必须要的-->


<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="#">核查编制图定开行</a></li>
</ol>


<!--分栏框开始-->
<div class="row">
	<div class="panel panel-default">
      <!--panle-heading-->
		    <div class="table-responsive">
		      <table class="table table-bordered table-striped table-hover" id="plan_review_table_ljtjxx">
		        <thead>
		          <tr>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">日期</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">路局</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">始发合计</div></th>
		            <th colspan="3">
		            	<div style="text-align:center;padding-bottom: -10px;">
		            	始发（图定） &nbsp;&nbsp;&nbsp;&nbsp;<a type="button" class="btn btn-success btn-xs" data-toggle="modal" data-target="#" id="plan_review_btn_createRunLine" >生成运行线</a>
		            	</div>
		            </th>
		            <th colspan="3">
		            	<div style="text-align:center;padding-bottom: -10px;">
		            	始发（临客） &nbsp;&nbsp;&nbsp;&nbsp;<a type="button" class="btn btn-success btn-xs">生成运行线</a>
		            	</div>
		            </th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">接入合计</div></th>
		            <th colspan="3"><div style="text-align:center;">接入（图定）</div></th>
		            <th colspan="3"><div style="text-align:center;">接入（临客）</div></th>
		          </tr>
		          <tr>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		          </tr>
		        </thead>
		        <tbody>
		          <tr>
		            <td width="110px"><input type="text" class="form-control" style="width:110px;" placeholder="" id="plan_review_selectdate"></td>
		          	<td style="width:90px"><select id="plan_review_select_lj" onchange="refreshPlanReviewTableLjtjxx()" class="form-control"></select></td>
		            <td><div id="plan_review_table_ljtjxx_sfhj" style="text-align:right;padding-top:8px;"></div></td>
		          	
		          	<td><div id="plan_review_table_ljtjxx_sftdxj" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div id="plan_review_table_ljtjxx_sftdjc" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div id="plan_review_table_ljtjxx_sftdzd" style="text-align:right;padding-top:8px;"></div></td>
		          	
		          	<td><div id="plan_review_table_ljtjxx_sflkxj" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div id="plan_review_table_ljtjxx_sflkjc" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div id="plan_review_table_ljtjxx_sflkzd" style="text-align:right;padding-top:8px;"></div></td>
		          	
		          	<td><div id="plan_review_table_ljtjxx_jrhj" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div id="plan_review_table_ljtjxx_jrtdxj" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div id="plan_review_table_ljtjxx_jrtdjc" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div id="plan_review_table_ljtjxx_jrtdzd" style="text-align:right;padding-top:8px;"></div></td>
		          	<td><div></div></td>
		          	<td><div></div></td>
		          	<td><div></div></td>
		          </tr>
		        </tbody>
		      </table>
		    </div>
      <!--panel panel-default--> 
    </div>
</div>



<!--分栏框开始-->
<div class="row">
  <div style="margin-right:-490px; float:left; width:100%;">
    <!--分栏框开始-->
    <div class="panel panel-default"  style="margin-right:490px;">
      <!--panle-heading-->
      <div class="panel-body">
      	<div class="row" style="margin:-5px 0 10px 0;">
	      <form class="form-horizontal" role="form">
	        <div class="pull-left">
	          <div class="form-group" style="float:left;margin-left:0px;margin-bottom:0;">
	            <div class="pull-left">
	              <input type="text" class="form-control" style="width:180px;" placeholder="输入车次查询" id="plan_review_input_trainNbr">
	            </div>
	            <div class="pull-left">
	              <button class="btn btn-primary" type="button" id="plan_review_btnQuery2">查询</button>
	            </div>
	          </div>
	        </div>
	        <!--col-md-3 col-sm-4 col-xs-4-->
	      </form>
	    </div>
	    
    
        <div class="table-responsive" >
          <table class="table table-bordered table-striped table-hover" id="plan_review_table_trainInfo">
		        <thead>
		          <tr>
		            <th rowspan="2" width="40px">
		            <div style="text-align:center;margin:-5px 0 10px 0;">序号</div></th>
		            <th rowspan="2" width="100px">
		            	<div style="text-align:center;margin:-5px 0 10px 0;">车次</div></th>
		            <th colspan="3"><div style="text-align:center;">始发</div></th>
		            <th colspan="3"><div style="text-align:center;">终到</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">经过局</div></th>
		            <th rowspan="2" width="60px"><div style="text-align:center;margin:-5px 0 10px 0;">所属交路</div></th>
		          </tr>
		          <tr>
		            <th width="160px"><div style="text-align:center;">站名</div></th>
		            <th style="width:30px"><div style="text-align:center;">局</div></th>
		            <th style="width:80px"><div style="text-align:center;">时间</div></th>
		            <th width="160px"><div style="text-align:center;">站名</div></th>
		            <th style="width:30px"><div style="text-align:center;">局</div></th>
		            <th style="width:80px"><div style="text-align:center;">时间</div></th>
		          </tr>
		        </thead>
		        <tbody>
		        </tbody>
		      </table>
        </div>
        <div class="pull-left" style="line-height:34px;"><label id="page_footer_ul_desc"></label></div>
        <ul id="page_footer_ul" class="pagination pull-right" style="margin:0px;"></ul>
        
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
    <!--分栏框开始-->
    <!--分栏框结束--> 
  </div>
  
  
  <div class="pull-right" style="width:480px;"> 
    <!--分栏框开始-->
    <div id="plan_view_div_palnDayDetail" class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title" style="float: left;"> <i class="fa fa fa-folder-open"></i>客运时刻表&nbsp;&nbsp;<label id="plan_view_div_palnDayDetail_title"></label></h3>
      </div>
      <!--panle-heading-->
      <div class="panel-body" style="padding:10px;">
      	<div class="table-responsive" >
          <table class="table table-bordered table-striped table-hover"  id="plan_review_table_trainDetail">
            <thead>
              <tr>
		        <th style="width:25px"><div style="text-align:center;">序号</div></th>
                <th><div style="text-align:center;">站名</div></th>
		        <th style="width:30px"><div style="text-align:center;">局</div></th>
                <th style="width:45px"><div style="text-align:center;">到达时间</div></th>
                <th style="width:45px"><div style="text-align:center;">出发时间</div></th>
                <th style="width:40px"><div style="text-align:center;">停留时间</div></th>
                <th style="width:20px"><div style="text-align:center;">股道</div></th>
                <th style="width:20px"><div style="text-align:center;">天数</div></th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
  </div>
  <!--分栏框结束--> 
</div>







<script src="<%=basePath %>/assets/js/trainplan/util/pagination.js" type="text/javascript"></script>
<script src="<%=basePath %>/assets/js/trainplan/plan/plan_review.js"></script>
</body>
</html>
