
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
var currentUserBureau = "";
var currentBureauShortName = "<%=currentBureauShortName %>";
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
.ckbox.disabled{
	cursor: not-allowed;
	pointer-events: none;
	opacity: 0.65;
	filter: alpha(opacity=65);
	-webkit-box-shadow: none;
	box-shadow: none;
}

 </style>

 
</head>
<body class="Iframe_body" style="height:100%;margin:-9px -6px 0px -3px;width:100%" >
	
	 <div id="div_searchForm" class="row" style="margin:-15px 0 10px 0;"> 
	    <form class="form-horizontal cc" role="form">
	   	<div class="row" style="margin:15px 0 10px 0;">
		      <div style="width: 422px;margin: 20px auto 20px auto;"> 
		        <div class="pull-left"> 
					 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 6px;margin-left: 50px;">
														铁路线:&nbsp;</label> 
					 <input type="text" id="input_cross_filter_showFlag4" class="form-control" style="padding:4px 26px;margin-top:3px;width:200px;" data-bind="value: throughLineName"/>
				 </div>
			 </div>
		 </div>
		 
		 <div class="row" > 
	        <div style="text-align: center;">   
			  <a type="button"  style="margin-left:15px" class="btn btn-success" data-bind="click: btnSaveDicThroughLine"><i class="fa fa-clock-o"></i>保存</a>
			<!--  <a type="button"  style="margin-left:5px" class="btn btn-success" data-bind="click:btnCancel"><i class="fa fa-trash-o"></i>取消</a>  --> 
			</div>
	    </div>
	    </form> 
	  </div>  
</body>  
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left"><span data-bind="html: $root.currentTotalCount()"></span></span>
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		  
     </td >
  </tr>
</table> 
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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/highLine/addDicThroughLine.js"></script>  
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
