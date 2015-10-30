
<%@page import="net.sf.json.JSONObject"%>
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

String highData = JSONObject.fromObject(request.getAttribute("highData")).toString();
String commonData = JSONObject.fromObject(request.getAttribute("commonData")).toString();
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>首页</title>
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
<link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/css/runPlan/runPlan_new.css"> 
	
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath %>/assets/css/font-awesome.min.css" />
 
<!-- Custom styles for this template --> 
<link href="<%=basePath %>/assets/css/cross/cross.css" rel="stylesheet">  
<link href="<%=basePath %>/assets/css/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/rightmenu.css">

<!-- 	<script>
	    window.alert = function(txt) {
	    	return;
	    }
   </script>  -->
<script type="text/javascript">
var highData = JSON.parse('<%=highData%>');
var commonData = JSON.parse('<%=commonData%>');
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
<body class="Iframe_body"  style="margin: -2px -2px 0 -3px;width: 100%;">
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
	     <div class="row" style="margin:-5px 0 5px 0;">  
	        <!--分栏框开始-->
		    <div class="pull-left top" style="width: 100%;height:100%">
			<!--分栏框开始--> 
			      <div class="row" style="margin: -5px 15px 10px 10px;"> 
				       <div class="panel-body">   
				        	 <div class="pull-left">
				        	     <span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 13px; ">
															方案:&nbsp;</span> 
								<input type="hidden" id="searchModleHiddenValue">
							<div class="pull-left" style="margin-left:5px;">
									<select style="padding:4px 12px; ;width: 280px; color:#e60302;" id="input_cross_chart_id"
										class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name', optionsValue: 'chartId'">
									</select>
							</div>   	 
				        	    
							<div class="pull-left" style="margin-left: 20px;">
											<a type="button" style="line-height:2.2;" class="btn btn-default blueBtn btn-sm pull-right" data-toggle="modal"
												data-target="#" id="btn_cross_search"  onclick="query()"><i class="fa fa-search"></i>查询</a> 
							</div>   
				       </div>
				</div>
					</div> 
				</div>
		</div>
	 	<div class="row" style="margin-top: 15px;">
        	<div class="col-xs-12 left" style="height:100%;padding-left:15px;">
            	<table id="runPlanTable" class=" table-bordered" style="width:50%;padding:15px;text-align:center">
            	<tr>
            		<th rowspan="2">序号</th>
            		<th rowspan="2">局别</th>
            		<th rowspan="2">合计</th>
            		<th colspan="2">普速</th>
            		<th colspan="2">高铁</th>
            	</tr>
            	<tr>
            		<th>小计</th>
            		<th>已完成</th>
            		<th>小计</th>
            		<th>已完成</th>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>哈</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>2</td>
            		<td>沈</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>3</td>
            		<td>京</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>4</td>
            		<td>太</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>5</td>
            		<td>呼</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>6</td>
            		<td>郑</td>
            		<td>200</td>
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
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>西</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>济</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>上</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>南</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>广</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>宁</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>成</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>昆</td>
            		<td>200</td>
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
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>乌</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	<tr>
            		<td>1</td>
            		<td>青</td>
            		<td>200</td>
            		<td>100</td>
            		<td>80</td>
            		<td>100</td>
            		<td>60</td>
            	</tr>
            	</table>
        	</div>					   
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
        //JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/run_plan_createGT.js");  
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
// 	function handOpen(){
// 		$('#handCreate').show().dialog({
// 		    title: '手动生成开行计划',
// 		    width: 500,
// 		    height: 300,
// 		    cache: false,
// 		    modal: true
// 		});
// 		$('#handCreate').dialog('open');
// 	}
//序号自动生成
$(function(){
        //$('table tr:not(:first)').remove();
        var len = $('table tr').length;
        for(var i = 0;i<len;i++){
            $('table tr:eq('+i+') td:first').text(i-1);
        }
        setData()
});

function query(){
	var baseChartId = $("#input_cross_chart_id").find("option:selected").val();
	commonJsScreenLock();
	$.ajax({
		url : "/trainplan/runPlan/countPlan",
		cache : false,
		type : "POST",
		dataType : "json",
		contentType : "application/json",
		data : JSON.stringify({
			baseChartId : baseChartId
		}),
		success : function(result) {
			if (result != null && result != "undefind"
					&& result.code == "0") {
				if (result.data != null) {
					highData = result.data.highData;
					commonData = result.data.commonData;
					setData();
				}
			} else {
				showErrorDialog("接口调用返回错误，code=" + result.code
						+ "   message:" + result.message);
			}
		},
		error : function() {
			showErrorDialog("接口调用失败");
		},
		complete : function() {
			commonJsScreenUnLock();
		}
	});
	commonJsScreenUnLock();
}

function setData(){
	$("#runPlanTable tr").each(function(){
		var tds = this.children;
		if(tds.length == 7){
			var bureau = tds[1].innerHTML;
			if(highData[bureau]){
				tds[5].innerHTML = highData[bureau];
				tds[6].innerHTML = highData[bureau];
			}else{
				tds[5].innerHTML = 0;
				tds[6].innerHTML = 0;
			}
			if(commonData[bureau]){
				tds[3].innerHTML = commonData[bureau];
				tds[4].innerHTML = commonData[bureau];
			}else{
				tds[3].innerHTML = 0;
				tds[4].innerHTML = 0;
			}
			tds[2].innerHTML = parseInt(tds[3].innerHTML) + parseInt(tds[5].innerHTML);
		}
		
	});
}

	function handClose(){
		$('#handCreate').hide();
		$('#handCreate').dialog('close');
	}
	function autoOpen(){
		$('#autoCreate').show().dialog({
		    title: '自动生成设置',
		    width: 500,
		    height: 370,
		    cache: false,
		    modal: true
		});
		$('#autoCreate').dialog('open');
	}

	function autoClose(){
		$('#autoCreate').hide();
		$('#autoCreate').dialog('close');
	}
	function isAutoGenerateON(){
		$('#isAutoGenerate').val(1);
	}
	function isAutoGenerateOff(){
		$('#isAutoGenerate').val(0);
	}
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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan_createGT.js"></script>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>  --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>  --%>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/moment.min.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/lib/fishcomponent.js"></script> --%>
<%-- <script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script>  --%>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script> --%> 


<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left">交路信息（共<span data-bind="html: totalCount()"></span>个交路  &nbsp;&nbsp;当前页<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>   &nbsp;&nbsp;共<span data-bind="text: pageCount()"></span>页）</span> 								 
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
