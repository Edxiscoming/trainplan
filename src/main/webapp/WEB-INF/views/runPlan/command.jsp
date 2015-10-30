
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();

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
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>既有临客开行计划</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/cross/cross.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/table.scroll.css">



<script type="text/javascript">
var all_role = "<%=userRolesString %>";
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
var currentUserBureau = "<%=currentUserBureau %>";
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
.control-label0{
width:132px;
}
.form-control0 {
padding: 1px 10px;
}
.table caption + thead tr:first-child th, .table colgroup + thead tr:first-child th, .table thead:first-child tr:first-child th, .table caption + thead tr:first-child td, .table colgroup + thead tr:first-child td, .table thead:first-child tr:first-child td{
border-bottom: 0;
}
 </style>

 
</head>
<body class="Iframe_body" style="margin:-7px -2px 0px -3px;width:100%" >
<input id="planTrainId" type="hidden" value="${planTrainId}">
	   <div class="row" style="margin:0px 0 0px 0;">  
	        <!--分栏框开始-->
		    <div class="pull-left" style="width: 30%;height:100%">
			<!--分栏框开始--> 
			      <div class="row" style="margin: 0 10px 10px 10px;"> 
				        	<div> 
										   <div class="row"  style="width: 100%;">
												<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 5px;">
													担当:</label>
												<div class="pull-left" style="margin-left: 5px;">
												   <span  style="width: 38px" data-bind="text: $root.tokenVehBureauShowValue"></span>
												</div>
												<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top: 5px;margin-left: 20px;">
													车次:</label>
												<div class="pull-left" style="margin-left: 5px; ">
													 <span  style="width: 38px" data-bind="text: $root.trainNbr"></span>
												</div> 
											</div>   
						 </div> 
					</div>
				</div> 
		</div> 

 
<!-- Custom styles for this template -->   
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>	   
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/chromatable_1.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/command.js"></script>

</body>  
</html>
