
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
<title>生成开行计划</title>
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


<!-- 	   <div class="row" style="margin: 10px 10px 10px 10px;">   -->
	     <div class="row" style="margin:-5px 0 5px 0;">  
	        <!--分栏框开始-->
		    <div class="pull-left top" style="width: 100%;height:100%">
			<!--分栏框开始--> 
			      <div class="row" style="margin: -5px 15px 10px 10px;"> 
				        	<div class="panel-body">   
				        		<span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:7px;margin-left: -2px; ">
									担当局:</span>
								<div class="pull-left" style="margin-left: 5px;">
									<input style="padding:4px 12px; ;width:75px" class="form-control" value="<%=currentBureauShortName %>" data-bind="">
								</div>
				        	     <div class="pull-right">
				        	     	<span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 13px; ">
															方案:&nbsp;</span> 
								<input type="hidden" id="searchModleHiddenValue">
								<div class="pull-left" style="margin-left:5px;">
									<select style="padding:4px 12px; ;width: 280px; color:#e60302;" id="input_cross_chart_id"
										class="form-control" data-bind="options:searchModle().charts, value: searchModle().chart, optionsText: 'name'">
									</select>
								</div>   	 
				        	     	<span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:7px;margin-left:20px">
									开始日期:</span>
								<div class="pull-left" style="margin-left: 5px;">
									<input type="text" class="form-control" style="padding:4px 12px; ;width:95px;" placeholder="" id="runplan_input_startDate"  name="startDate" data-bind="value: searchModle().planStartDate" />
								</div>
								<span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:7px;margin-left: 20px;">
									截至日期:</span>
								<div class="pull-left" style="margin-left: 5px; ">
									<input type="text" class="form-control" style="padding:4px 12px; ;width:95px;" placeholder="" id="runplan_input_endDate"  name="endDate" data-bind="value: searchModle().planEndDate" />
								</div>
								   
								<span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:7px;margin-left:20px">
										车次:&nbsp;</span>
									<div class="pull-left" style="margin-left:5px;">
										<input type="text" class="form-control" style="padding:4px 12px; ;width: 95px;"
									 		 id="input_cross_filter_trainNbr" data-bind=" value: searchModle().filterTrainNbr, event:{keyup: trainNbrChange}">
									</div>
								<span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:7px;margin-left: 20px;">
									交替日期:
								</span>
								<div class="pull-left" style="margin-left: 5px; ">
									<input type="text" class="form-control" style="padding:4px 12px; ;width:95px;" placeholder="" id="cross_start_date"  name="cross_start_date" data-bind="value: searchModle().cross_start_date" />
								</div>  	 
									<div class="pull-left" style="margin-left: 20px;">
											<a type="button" style="line-height:2.2;" class="btn btn-default blueBtn btn-sm pull-right" data-toggle="modal"
												data-target="#" id="btn_cross_search"  data-bind="click: loadCrosses"><i class="fa fa-search"></i>查询</a> 
									</div>   
				        	     </div>
										
						</div>
						
							<!-- <div class="panel-body">   
				        	     <label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left:-2px" > 
									生成日期:</label>
								<div class="pull-left" style="margin-left: 5px;">
									<input type="text" class="form-control" style="padding:4px 12px; ;width:95px;" placeholder="" id="produce_runplan_startDate" data-bind="value: searchModle().producePlanStartDate"/>
								</div>
								   
								<label for="exampleInputEmail3" class="control-label pull-left" style="margin-top:5px;margin-left: 13px;">
									生成天数:</label>
								<div class="pull-left" style="margin-left: 5px; ">
									<input type="text" class="form-control" style="padding:4px 12px; ;width:47px;" placeholder="" id="produce_runplan_days"/>
								</div>
									<div class="pull-left" style="margin-left: 10px;">
											
									        <a  type="button" class="btn btn-success btn-input" data-toggle="modal" style="margin-left: 13px;" data-target="#" id="btn_cross_createTrainLines" data-bind="click: createTrainLines">
												<i class="fa fa-external-link"></i>生成开行计划
											</a>	
									</div>   
										
						</div> -->
						
					</div> 
					<!-- <div class="row">
						<div class="pull-left" style="margin: 5px 15px 10px 10px;">
							<div class="btn-group" style="margin-left:-15px; margin-bottom:15px; margin-top:5px;" id="btnGroup">
			                    <button type="button" class="btn btn-default active" style="width:90px;height:34px;line-height:2.2;margin-left:20px;">全部</button>
			                    <button type="button" class="btn btn-default" style="width:90px;height:34px;line-height:2.2;" onClick="searchPlanForTD()">图定</button>
			                    <button type="button" class="btn btn-default" style="width:90px;height:34px;line-height:2.2;" onClick="searchPlanForLK()">临客</button>
			                </div>
						</div>
					</div> -->
				</div>
		</div>
					
		<div class="row">
   	 	<div class="pull-left" style="width:30px; margin-top:38px;">
            <div class="btn-group-vertical" role="group" id="tokenGrop">
	            <button class="btn btn-default active" type="button" value="">全部</button>
	            <button class="btn btn-default" type="button" value="B" onClick="chioseBureauClick('B')" data-bind="" >哈</button>
	            <button class="btn btn-default" type="button" value="T" onClick="chioseBureauClick('T')">沈</button>
	            <button class="btn btn-default" type="button" value="P" onClick="chioseBureauClick('P')">京</button>
	            <button class="btn btn-default" type="button" value="V" onClick="chioseBureauClick('V')">太</button>
	            <button class="btn btn-default" type="button" value="C" onClick="chioseBureauClick('C')">呼</button>
	            <button class="btn btn-default" type="button" value="F" onClick="chioseBureauClick('F')">郑</button>
	            <button class="btn btn-default" type="button" value="N" onClick="chioseBureauClick('N')">武</button>
	            <button class="btn btn-default" type="button" value="Y" onClick="chioseBureauClick('Y')">西</button>
	            <button class="btn btn-default" type="button" value="K" onClick="chioseBureauClick('K')">济</button>
	            <button class="btn btn-default" type="button" value="H" onClick="chioseBureauClick('H')">上</button>
	            <button class="btn btn-default" type="button" value="G" onClick="chioseBureauClick('G')">南</button>
	            <button class="btn btn-default" type="button" value="Q" onClick="chioseBureauClick('Q')">广</button>
	            <button class="btn btn-default" type="button" value="Z" onClick="chioseBureauClick('Z')">宁</button>
	            <button class="btn btn-default" type="button" value="W" onClick="chioseBureauClick('W')">成</button>
	            <button class="btn btn-default" type="button" value="M" onClick="chioseBureauClick('M')">昆</button>
	            <button class="btn btn-default" type="button" value="J" onClick="chioseBureauClick('J')">兰</button>
	            <button class="btn btn-default" type="button" value="R" onClick="chioseBureauClick('R')">乌</button>
	            <button class="btn btn-default" type="button" value="O" onClick="chioseBureauClick('O')">青</button>
	        </div>
        </div>
   	 	<div class="pull-left" style="width:96%; margin-left:30px;">
   	 		<div data-bind="template: { name: 'tablefooter-short-template', foreach: crossRows }" style="margin-bottom: 5px"></div>
					
   	 	  <div>   
	 	    <div id="learn-more-content" >
              <div style="padding:10px 5px;" class="panel"> 
				<!-- Tab panes --> 
	  		    <div id="plan_view_div_palnDayDetail" style="overflow-y: auto;"> 
				  <div class="row" data-bind="html: completedMessage()"></div>
				  
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
	$(function(){
		$('#btnGroup button').bind('click', function(){
	        $(this).addClass('active').siblings().removeClass('active');
// 	        searchData();
	    }); 

	    $('#tokenGrop button').bind('click', function(){
	        $(this).addClass('active').siblings().removeClass('active');
// 	        searchData();
	    });
	})
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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan_searchGT.js"></script>
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
