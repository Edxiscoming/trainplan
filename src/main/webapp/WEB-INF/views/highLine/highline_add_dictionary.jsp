
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
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
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
	    <form class="form-horizontal" role="form">
	   	<div class="row" style="margin:15px 0 10px 0;">
		      <div class="pull-left"> 
			      <a type="button"  style="margin:3px 0 0 5px" class="btn btn-success btn-input" data-bind="click: addDicRelaCrossPost"><i class="fa fa-plus"></i>增加</a>
			      <a type="button"  style="margin:3px 0 0 5px" class="btn btn-success btn-input" data-bind="click: deleteDicThroughLine"><i class="fa fa-trash-o"></i>删除</a>
			 </div>
		 </div>
	    </form> 
	  </div>  
	    <div class="row" style="margin: 10px 10px 0px 10px;">   
		    <!--分栏框开始-->
		    <div id="div_hightline_planDayDetail" class="panel panel-default" style="height: auto;">
				     <div class="row" style="margin:15px 10px 10px 10px;"> 
					     <div class="table-responsive" >
					          <table class="table table-bordered table-striped table-hover">
									<thead>
										<tr> 
										    <th style="width: 24px" align="center"><input type="checkbox" data-bind="checked: isSelectAll, event:{change: checkBoxSelectAllChange}" style="margin-top:0" value="1"></th>
											<th style="width: 45px">序号</th> 
											<th style="width: 100px">路局</th>
											<th style="width: 100px">铁路线</th>  
										</tr>
									</thead>
							 </table>
					         <div id="left_height" style="overflow-y:auto;">
						          <table class="table table-bordered table-striped table-hover" style="border: 0;">
									<tbody data-bind="foreach: dicThroughLineCrossRows">
										<tr data-bind=" visible: visiableRow, style:{color: $parent.currentCross().highLineCrossId == highLineCrossId ? 'blue':''}" >
									        <td style="width:2px"><input type="checkbox" value="1" data-bind="event:{change:$parent.setCurrentRec1 },checked: isSelect"></td>
									        <td style="width: 45px" data-bind="text: ($index() + 1)"></td> 
										    <td style="width: 100px" data-bind="text: tokenVehBureauShowValue" ></td>
										      <td style="width: 100px" data-bind="text: throughLineName" ></td>
										</tr>
									</tbody>
								</table>
						 </div>
								<div data-bind="template: { name: 'tablefooter-short-template'}" style="margin-bottom: 5px"></div>
					        </div> 
				       </div>
				  </div>  
	    </div>
	     <div id="line_add_dictionary_dialog" class="easyui-dialog" title=""
			data-options="iconCls:'icon-save'"
			style="width: 500px; height: 150px;overflow: hidden;">
		 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
		</div>
		  <div id="add_disRelaCross_dialog" class="easyui-dialog" title="新增线台字典"
			data-options="iconCls:'icon-save'"
			style="width: 400px; height: 240px;overflow: hidden;">
		 	<iframe style="width: 100%; height: 395px;border: 0;overflow: hidden;" src=""></iframe>
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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/highLine/highLine_add_dictionary.js"></script>  
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
