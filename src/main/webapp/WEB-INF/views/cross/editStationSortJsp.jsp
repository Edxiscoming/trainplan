
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
 ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
/*
boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
} */
/* String currentUserBureau = "";
List<String> permissionList = "";
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
}  */
String basePath = request.getContextPath();
String  currentBureauShortName = user.getBureauShortName();
String currentUserBureau = user.getBureau();
Object drawGraphId =  request.getAttribute("drawGraphId");
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<style>
form.cc .pull-left{
margin-bottom:10px;
}
</style>
<title>高铁车底交路计划编制</title>
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



   <script type="text/javascript">

var basePath = "<%=basePath %>";
var all_role = "";
var _isZgsUser = true;//当前用户是否为总公司用户
var currentUserBureau = "<%=currentUserBureau %>";
var currentBureauShortName = "<%=currentBureauShortName %>";
</script>

 
</head>
<body class="Iframe_body" style="height:500px;margin:-9px -6px 0px -3px;width:100%" >
	
	
	<input type="hidden" id="drawGraphId" value="<%=drawGraphId%>"/>
	<!-- <ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">车底三乘计划 -> 高铁车底交路计划编制</a></li>
	</ol>   -->
	 <div id="div_searchForm" class="row" style="margin:-15px 0 10px 0;"> 
	    <form class="form-horizontal cc" role="form">
	   	<div class="row" style="margin:15px 0 10px 0;">
		      <div style="width: 100%;margin: 20px auto 0 auto;"> 
		       <div class="tab-pane active" id="simpleTimes" > 
					      	<div class="table-responsive"> 
					            <table class="table table-bordered table-striped table-hover" id="plan_runline_table_trainLine">
							        <thead>
							        <tr>
<!-- 							          <th style="width:49px">序号</th> -->
<!-- 					                  <th style="width:90px">站名</th> -->
<!-- 					                  <th style="width:90px">站序</th> -->
<!-- 					                  <th style="width:90px">站间距</th> -->
					                  
							          <th style="width:51px">序号</th>
					                  <th style="width:170px">站名</th>
					                  <th style="width:170px">站序</th>
					                 </tr>
							        </thead>
							      </table>

									 <div id="simpleTimes_table" style="height: 430px; overflow-y:auto;"> 
										<table class="table table-bordered table-striped table-hover" >
										 <tbody data-bind="foreach: stations">
 									           <tr>  
 									           
<!--  												<td style="width:49px" align="center" data-bind=" text: $index() + 1"> -->
<!--  									           		<input type="hidden" style="width:0px;text-align:center" data-bind="value: drawGraphStnId, attr:{title: stnName}", class="form-control" disabled/> -->
<!--  									            </td> -->
<!--  									            <td align="center"><input style="width:90px;text-align:center" data-bind="value: stnName, attr:{title: stnName}", class="form-control" disabled/></td> -->
<!--  									            <td align="center"><input style="width:90px;text-align:center" data-bind="attr:{id: 'stnSortInput'+ $index()}, value: stnSort, event:{focus:stnSortOld,change: sortChange}", class="form-control"/></td> -->
<!--  									            <td align="center"><input style="width:90px;text-align:center" data-bind="attr:{id: 'heightDetailInput'+ $index()}, value: heightDetail, event:{change: heightDetailChange}", class="form-control"/></td></td> -->
 									            
 									            
 												<td style="width:51px" align="center" data-bind=" text: $index() + 1">
 									           		<input type="hidden" style="width:0px;text-align:center" data-bind="value: drawGraphStnId, attr:{title: stnName}", class="form-control" disabled/>
 									            </td>
 									            <td align="center" style="width:170px"><input style="width:134px;text-align:center" data-bind="value: stnName, attr:{title: stnName}", class="form-control" disabled/></td>
 									            <td align="center" style="width:170px"><input style="width:134px;text-align:center" data-bind="attr:{id: 'stnSortInput'+ $index()}, value: stnSort, event:{change: sortChange}", class="form-control"/>
 									            <input type="hidden" style="text-align:center" data-bind="attr:{id: 'heightDetailInput'+ $index()}, value: heightDetail, event:{change: heightDetailChange}", class="form-control"/></td>
 												</tr>
										</tbody>
										</table> 
								 	</div>
	
			        		</div> 
			 </div>
		 </div>
		 
		 <div class="row" > 
	        <div style="text-align: center;">   
			  <a type="button"  style="margin-left:15px" class="btn btn-success" data-bind="click: saveDicStationSort"><i class="fa fa-clock-o"></i>保存</a>
		<!--   <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click:btnCancel"><i class="fa fa-trash-o"></i>取消</a>  -->	
			</div>
	    </div>
	    </form> 
	  </div>  
</body>  
 <script type="text/html" id="tablefooter-short-template"> 
</script> 

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/resizable.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/editStationSortJsp.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
<script src="<%=basePath %>/assets/lib/fishcomponent.js"></script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
<script type="text/javascript">
	$(function() {
	   resize("leftBox","rightBox","670","730");
	 });
</script>
<script  type="text/html" id="runPlanTableDateHeader"> 
    <!-- ko if: $index() == 0 -->
 	<td></td>
 	<!-- /ko --> 
 	<td align='center' data-bind="text: day"></td>
</script>
<script  type="text/html" id="runPlanTableVlaues">
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
 	<td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->  
 	<td  align='center' data-bind="text: runFlag, style:{'color': color}"></td>
 </tr> 
</script>
</html>
