
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.railway.passenger.transdispatch.operationplan.entity.TCmPlanAutogenerate"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
String  currentBureauShortName = user.getBureauShortName();
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
Map<String, List<TCmPlanAutogenerate>> data = (Map<String, List<TCmPlanAutogenerate>>)request.getAttribute("data");
String dataStr = JSONObject.fromObject(data).toString();
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>自动生成设置</title>
<!-- Bootstrap core CSS -->

<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan_autoGenSet.js"></script>

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
<link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/css/runPlan/runPlan_new.css">	
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" />
 
<!-- Custom styles for this template --> 
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/rightmenu.css">
	<script>
		var data = JSON.parse('<%=dataStr%>');
	    window.alert = function(txt) {
	    	return;
	    }
    </script>
<script type="text/javascript">
var basePath = "<%=basePath %>";
var all_role = "<%=userRolesString %>";
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
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
.table td, .table th {
max-width: none;
}
 </style>

 <jsp:include page="/assets/commonpage/include-dwr-script.jsp" flush="true" /> 
</head>
<body class="Iframe_body"  >
<!--  
<ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置:</span>
    <c:if test="${train_type==0}">
        <li><a href="#">编制开行计划 -> 既有图定生成开行计划</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">编制开行计划 -> 高铁图定生成开行计划</a></li>
    </c:if>
</ol>
-->	


<!-- 	   <div class="row" style="margin: 10px 10px 10px 10px;">   -->




	<div class="row" style="margin:-5px 0 5px 0;width:50%" >
	<span><strong>自动生成设置</strong></span>
	<button onclick="submit()" class="blueBtn pull-right">保存</span>
	</div>
	     <div class="row" style="margin:-5px 0 5px 0;">
	              	
            	
            	<table id="autoGenTab" class=" table-bordered" style="width:50%;padding:15px;text-align:center">
            	<tr>
            		<th rowspan="2">序号</th>
            		<th rowspan="2">局别</th>
            		<th colspan="2">普速</th>
            		<th colspan="2">高铁</th>
            	</tr>
            	<tr>
            		<th>提前天数</th>
            		<th>执行时间</th>
            		<th>提前天数</th>
            		<th>执行时间</th>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>哈</td>
            		<td></td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>2</td>
            		<td>沈</td>
            		<td>200</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>3</td>
            		<td>京</td>
            		<td>200</td>
            		<td>100</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>4</td>
            		<td>太</td>
            		<td>200</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>5</td>
            		<td>呼</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>6</td>
            		<td>郑</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>7</td>
            		<td>武</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>西</td>
            		<td>200</td>
            		<td>100</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>济</td>
            		<td>200</td>
            		<td>100</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>上</td>
            		<td>200</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>南</td>
            		<td>200</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>广</td>
            		<td>200</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>宁</td>
            		<td>200</td>
            		<td>100</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>成</td>
            		<td>200</td>
            		<td>100</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>昆</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>兰</td>
            		<td>200</td>
            		<td>100</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>乌</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>青</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            	</tr>
            	</table>
	     </div>
	      
</body>


<script type="text/javascript" src="<%=basePath %>/assets/js/jsloader.js"></script>   

<script type="text/javascript"> 

        JSLoader.loadJavaScript(basePath + "/assets/js/jquery.js");
        JSLoader.loadJavaScript(basePath + "/assets/js/html5.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/bootstrap.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/respond.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/easyui/jquery.easyui.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/validate.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/knockout.js");  
        JSLoader.loadJavaScript(basePath + "/assets/js/jquery.freezeheader.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/ajaxfileupload.js"); 
        //JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/run_plan_create.js");  
        JSLoader.loadJavaScript(basePath + "/assets/js/datepicker.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/jquery.gritter.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/common.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/respond.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/moment.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/lib/fishcomponent.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/knockout.pagemodle.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/common.security.js"); 
        
</script>    
<script type="text/javascript">
//序号和时间控件自动生成
$(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 0;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i-1);
            $('table tr:eq('+i+') td:nth-child(3)').html("<input type='number' max=50 onblur=checkV(this); value='40' style='width:80px'>");
            $('table tr:eq('+i+') td:nth-child(4)').html("<input type='time' value='11:59'>");
            $('table tr:eq('+i+') td:nth-child(5)').html("<input type='number'  max=50  value='50' onblur=checkV(this); style='width:80px'>");
            $('table tr:eq('+i+') td:nth-child(6)').html("<input type='time' value='22:20'>");
        }
        
        $("#autoGenTab tr").each(function(){
    		var tds = this.children;
    		if(tds.length == 6){
    			var bureau = tds[1].innerHTML;
    			$.each(data[bureau],function(n,obj) { 
    				if(0 == obj.highlineFlag){
    					//common_days
    	    			tds[2].children[0].value=obj.maintainDays;
    	    			//common_genTime
    	    			tds[3].children[0].value=obj.generateTime;
    				} 
    				if(1 == obj.highlineFlag){
    					//high_days
    	    			tds[4].children[0].value=obj.maintainDays;
    	    			//high_genTime
    	    			tds[5].children[0].value=obj.generateTime;
    				}
    			});
    			
    		}
    		
    	});
});
</script>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/validate.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script>  --%>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan_create.js"></script>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script> --%>
 <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/common.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/moment.min.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/lib/fishcomponent.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script>  --%>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script>  --%>

<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left">共<span data-bind="html: totalCount()"></span>个交路  &nbsp;&nbsp;当前页<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>   &nbsp;&nbsp;共<span data-bind="text: pageCount()"></span>页</span> 								 
  	 </td>
     <td align="right" style="width:40%;height:20px;padding:0px;pading-bottom:-14;">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table> 
</script> 

<script  type="text/html" id="runPlanTableDateHeader"> 
    <!-- ko if: $index() == 0 -->
	<td style="vertical-align: middle;width:30px;" rowspan="2">序<br>号</td>
 	<td colspan="3" rowspan="2" style="vertical-align: middle;width:80px" ><input type="checkbox" value="1" data-bind="event:{change: $root.selectCrosses}, checked: $root.crossAllcheckBox"enable:$root.canCheckAll> </td>
 	<!-- /ko --> 
 	<td align='center' data-bind="text: day" style="width:40px"></td> 
</script> 
<script  type="text/html" id="runPlanTableWeekHeader">
 	<td align='center' data-bind="text: week,style:{color:(week=='日'||week=='六')?'blue':''}"></td>
</script> 

<script  type="text/html" id="runPlanTableVlaues"> 
 <!-- ko if: trainSort == 0 --> 
 <tr data-bind="foreach: runPlans" >
    <!-- ko if: $index() == 0 --> 
	<td style="width:30px" data-bind="text:($root.crossRows.currentPage()*$root.crossRows.pageSize() + $parent.chirldrenIndex +1)"></td>
    <td style="width:40px"><input name="checkedAll" type="checkbox" value="1" data-bind="event:{change: $root.selectCross.bind($data, $parent)},checked: $parent.selected,enable:$parent.canCheck2"></td>
    <td data-bind="text: $parent.tokenVehBureauShowValue" style="width:40px"></td>
    <td data-bind="attr:{colspan: $parent.colspan} "><span style="vertical-align: middle;" data-bind="html: $parent.createStatusShowValue"></span></td> 
 	<!-- /ko -->   
 </tr> 
 <!-- /ko -->
 <!-- ko if: trainSort == 1 --> 
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
    <td data-bind="text: '', attr:{rowspan: $parent.rowspan} " colspan="3"></td> 
    <td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->   
 	<td style="vertical-align: middle;" data-bind="html: runFlagShowValue"></td>
 </tr> 
 <!-- /ko -->
 <!-- ko if: trainSort > 1 --> 
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 -->
    <td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->   
 	<td style="vertical-align: middle;" data-bind="html: runFlagShowValue"></td>
 </tr> 
 <!-- /ko -->
</script>
</html>
