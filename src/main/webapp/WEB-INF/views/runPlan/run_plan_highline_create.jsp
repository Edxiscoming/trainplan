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
%>

<!DOCTYPE HTML>
<html lang="en">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>生成运行线</title>
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

	<script>
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

</head>
<jsp:include page="/assets/commonpage/include-dwr-script.jsp" flush="true" /> 
<body class="Iframe_body" style="margin:-28px -5px 0px -8px;width:100%"  >
<!--  
<ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置:</span>
    <c:if test="${train_type==0}">
        <li><a href="#">发布开行计划 -> 发布既有开行计划</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">发布开行计划 -> 发布高铁开行计划</a></li>
    </c:if>
</ol>  
-->	
    <input type="hidden" id="currentBureauShortName" value="<%=currentBureauShortName %>"/>
	   <div class="row" style="margin:10px 0 0px 0px;">  
	        <!--分栏框开始-->
		    <div class="pull-left" style="width: 100%;height:100%">
			<!--分栏框开始--> 
			      <div class="row" style="margin: 5px 10px 5px 10px;"> 
				        	<div class="panel-body"> 
				        		<!-- --> 
<!-- 				        		<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 13px; "> -->
<!-- 															方案:&nbsp;</label>  -->
								<input type="hidden" id="searchModleHiddenValue">
								<div class="pull-left" style="display:none">
									<select style="padding:4px 12px; ;width: 249px" id="input_cross_chart_id"
										class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
									</select>
								</div>
								 
				        	     <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 3px; " >
									客车类型:</label>
								<div class="pull-left" style="margin-left: 5px;">
									<select style="padding:4px 12px; width:130px" class="form-control" data-bind="options:searchModle().searchTypes, value: searchModle().searchType, optionsText: 'name', optionsValue:'code',  event:{change: searchTypeChange}"></select>
								</div>  
								 <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:13px">
									开始日期:</label>
								<div class="pull-left" style="margin-left: 5px;">
									<input type="text" class="form-control" style="padding:4px 12px; width:130px;" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
								</div>
								<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 13px;">
									截至日期:</label>
								<div class="pull-left" style="margin-left: 5px; ">
									<input type="text" class="form-control" style="padding:4px 12px; width:130px;" placeholder="" id="runplan_input_endDate"  name="endDate" data-bind="value: searchModle().planEndDate" />
								</div>
								<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 13px; " >
									担当局:</label>
								<div class="pull-left" style="margin-left: 5px;">
									<select style="padding:4px 12px; width:130px" id="tokenVehBureau" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code',event:{change: bureauChange}"></select>
								</div>
								
								   
								    <label for="exampleInputEmail3" class="control-label pull-left" style="margin-left:13px;margin-top:5px;">
										车次:&nbsp;</label>
									<div class="pull-left">
										<input type="text" class="form-control" style="padding:4px 12px; width: 130px;"
									 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
									</div>  	 
									<div class="pull-left" style="margin-left: 10px;">
											<a type="button" class="btn btn-success btn-input" data-toggle="modal"
												data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a> 
											<a  type="button" class="btn btn-success btn-input" data-toggle="modal" style="margin-left: 10px;" 
										data-target="#" id="btn_cross_createTrainLines" data-bind="click: createTrainLines"><i class="fa fa-external-link"></i>生成1条线</a>
										</div>  
										
						</div>
					</div> 
				</div>
		</div> 						   
   	 	<div class="row"  style="margin: 5px 5px 10px 15px;" >
   	 	<div data-bind="template: { name: 'tablefooter-short-template', foreach: crossRows }" style="margin-bottom: 5px"></div>
				  
   	 	  <div>   
	 	    <div id="learn-more-content" >
              <div style="padding:10px 5px;" class="panel"> 
				<!-- Tab panes --> 
	  		    <div id="plan_view_div_palnDayDetail" style="overflow-y: auto;">
				  
				  <div class="row" style="margin: 5px 10px 10px 10px;" data-bind="html: completedMessage()"></div>
				  	
			      <div class="panel-body">
			      	<div class="table-responsive" > 
			          <table class="table table-bordered table-striped table-hover" id="run_plan_table" data-bind="style:{width: runPlanTableWidth()}">
				      <thead>  
				      	<tr data-bind="template: { name: 'runPlanTableDateHeader', foreach: planDays }" ></tr>
				      	<tr data-bind="template: { name: 'runPlanTableWeekHeader', foreach: planDays }" ></tr> 
				      </thead> 
				      <tbody data-bind="template: { name: 'runPlanTableVlaues', foreach: trainPlans }"> 
				      </tbody> 
					  </table>
			        </div>  
			        </div>  
			      </div>
				</div>  
			</div>
		 </div>
	 </div>  
</body>  
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
		<!-- ko if: $root.searchModle().searchType() != '3' -->
  			<span class="pull-left" style="">共<span data-bind="html: totalCount()"></span>个交路  &nbsp;&nbsp;当前页<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>   &nbsp;&nbsp;共<span data-bind="text: pageCount()"></span>页</span> 								 
		<!-- /ko -->
		<!-- ko if: $root.searchModle().searchType() == '3' -->
			<span class="pull-left" style="margin-left: -10px; ">共<span data-bind="html: totalCount()"></span>个临客  &nbsp;&nbsp;当前页<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>   &nbsp;&nbsp;共<span data-bind="text: pageCount()"></span>页</span>
		<!-- /ko -->
  	 </td>
     <td  align="right" style="width:40%;height:20px;padding:0px;pading-bottom:-14;">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table> 
</script> 


<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan_highline_create.js"></script>  
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
<script src="<%=basePath %>/assets/lib/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script> --%> 

<%-- <script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_rightmenu.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_event_getvalue.js"></script> --%>
<script type="text/javascript">
var basePath = "<%=basePath %>";
</script>

<script  type="text/html" id="runPlanTableDateHeader"> 
    <!-- ko if: $index() == 0 -->
	<td style="vertical-align: middle;width:30px;" rowspan="2">序<br>号</td>
 	<td colspan="3" rowspan="2" style="vertical-align: middle;width:80px;" ><input type="checkbox" value="1" data-bind="event:{change: $root.selectCrosses}, checked: $root.crossAllcheckBox"> </td>
 	<!-- /ko --> 
 	<td align='center' data-bind="text: day" style="width:40px"></td> 
</script> 
<script  type="text/html" id="runPlanTableWeekHeader">  
 	<td align='center' data-bind="text: week,style:{color:(week=='日'||week=='六')?'blue':''}" ></td>
</script> 

<script  type="text/html" id="runPlanTableVlaues"> 
 <!-- ko if: trainSort == 0 --> 
 <tr data-bind="foreach: runPlans" >
    <!-- ko if: $index() == 0 --> 
	<td style="width:30px" data-bind="text:($root.crossRows.currentPage()*$root.crossRows.pageSize() + $parent.chirldrenIndex +1)"></td>
    <td style="width:40px"><input type="checkbox" value="1" data-bind="event:{change: $root.selectCross.bind($data, $parent)},checked: $parent.selected,enable:$parent.canCheck2"></td>
    <td data-bind="text: $parent.tokenVehBureauShowValue" style="width:40px"></td>
    <td data-bind="attr:{colspan: $parent.colspan} "><span style="vertical-align: middle;" data-bind="html: $parent.createStatusShowValue"></span></td> 
 	<!-- /ko -->   
 </tr> 
 <!-- /ko -->
 <!-- ko if: trainSort == 1 --> 
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
		<!-- ko if: $root.searchModle().searchType() != '3' -->
			<td data-bind="attr:{rowspan: $parent.rowspan} " colspan="3"></td>
		<!-- /ko -->
		<!-- ko if: $root.searchModle().searchType() == '3' -->
			<td style="width:30px" data-bind="text:($root.crossRows.currentPage()*$root.crossRows.pageSize() + $parent.chirldrenIndex +1)"></td>
			<td colspan="1">
         		<input type="checkbox" value="1" data-bind="event:{change: $root.selectCross.bind($data, $parent)},checked: $parent.selected,enable:$parent.canCheck2">
    		</td>
			<td data-bind="text: $parent.tokenVehBureauShowValue" style="width:40px"></td>
		<!-- /ko -->
    <td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->   
 	<td  align='center' data-bind="style:{'color': color}">
          <!-- ko if: createFlag() != 1 && runFlagShowValue() !='' -->  
             <input type='checkbox' value='1' data-bind="event:{change: $root.selectRunPlan}, enable:$parent.canCheck2, checked: selected, attr:{class: createFlag() == 1 ? 'ckbox disabled' : ''}" ><span style="vertical-align: middle;" data-bind="html: runFlagShowValue"></span>
          <!-- /ko -->
		  <!-- ko if: createFlag() == 1 -->  
             <span style="vertical-align: middle;" data-bind="html: runFlagShowValue"></span>
          <!-- /ko -->
    </td>
	

	




 </tr> 
 <!-- /ko -->
 <!-- ko if: trainSort > 1 --> 
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 -->  
    <td data-bind="text: $parent.trainNbr"></td>
 	<!-- /ko -->   
 	<td  align='center' data-bind="style:{'color': color}">
          <!-- ko if: createFlag() != 1 && runFlagShowValue() !='' -->
             <input type='checkbox' value='1' data-bind="event:{change: $root.selectRunPlan}, enable:$parent.canCheck2, checked: selected, attr:{class: createFlag() == 1 ? 'ckbox disabled' : ''}" ><span style="vertical-align: middle;" data-bind="html: runFlagShowValue"></span>
          <!-- /ko -->
		  <!-- ko if: createFlag() == 1 -->  
             <span style="vertical-align: middle;" data-bind="html: runFlagShowValue"></span>
          <!-- /ko -->
    </td>
 </tr> 
 <!-- /ko -->
</script>
</html>

