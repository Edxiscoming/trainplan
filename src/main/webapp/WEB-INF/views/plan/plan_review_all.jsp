<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>开行计划汇总</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body class="Iframe_body">
<!--以上为必须要的-->

<ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
    <c:if test="${train_type==0}">
        <li><a href="#">既有日历计划发布</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">高铁日历计划发布</a></li>
    </c:if>
</ol>



<!--分栏框开始-->
<div class="row">
	<div class="panel panel-default">
      <!--panle-heading-->
        <div class="panel-body">
		    <div class="row" style="margin:-5px 0 10px 0;">
		      <form class="form-horizontal" role="form">
		        <div class="pull-left">
		          <div class="form-group" style="float:left;margin-left:20px;margin-bottom:0;">
		            <label for="exampleInputEmail2" class="control-label pull-left"> 日期:&nbsp;</label>
		            <div class="pull-left">
		              <input type="text" class="form-control" style="width:110px;" placeholder="" id="plan_review_all_selectdate">
		            </div>
		            <div class="pull-left">
		            	<button class="btn btn-primary" type="button" id="plan_review_all_btnQuery_ljtjxx">查询</button>
		            </div>
		          </div>
		        </div>
		        <!--col-md-3 col-sm-4 col-xs-4-->
		      </form>
		    </div>
		    <div class="table-responsive">
		      <table class="table table-bordered table-striped table-hover" id="plan_review_all_table_tjxx">
		        <thead>
		          <tr>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">序号</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">路局简称</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">始发合计</div></th>
		            <!-- <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">始发线合计</div></th> -->
		            <th colspan="5"><div style="text-align:center;">始发（图定）</div></th>
		            <th colspan="5"><div style="text-align:center;">始发（临客）</div></th>
		            <th rowspan="2"><div style="text-align:center;margin:-5px 0 10px 0;">接入合计</div></th>
		            <th colspan="3"><div style="text-align:center;">接入（图定）</div></th>
		            <th colspan="3"><div style="text-align:center;">接入（临客）</div></th>
		          </tr>
		          <tr>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">交出线</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">终到线</div></th>
		            
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">交出线</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">终到线</div></th>
		            
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		            <th><div style="text-align:center;">小计</div></th>
		            <th><div style="text-align:center;">交出</div></th>
		            <th><div style="text-align:center;">终到</div></th>
		          </tr>
		        </thead>
		        <tbody>
		        </tbody>
		      </table>
		    </div>
		  </div>
      <!--panel panel-default--> 
    </div>
</div>



<script src="<%=basePath %>/assets/js/trainplan/plan/plan_review_all.js"></script>
</body>
</html>