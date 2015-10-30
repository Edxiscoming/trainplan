
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
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css" rel="stylesheet">
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
<!-- 	<script> -->
<!-- 	    window.alert = function(txt) { -->
<!-- 	    	return; -->
<!-- 	    } -->
<!--     </script> -->
<script type="text/javascript">
var basePath = "<%=basePath %>";
var all_role = "<%=userRolesString %>";
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
var currentUserBureau = "<%=currentUserBureau %>";
var currentBureauShortName = "<%=currentBureauShortName %>";
</script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>

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
	     <div class="row" style="margin:-5px 0 5px 0;">  
	        <!--分栏框开始-->
		    <div class="pull-left top" style="width: 100%;height:100%">
			<!--分栏框开始--> 
			      <div class="row" style="margin: -5px 15px 10px 10px;"> 
				        	<div class="panel-body">   
				        		<!-- <span for="exampleInputEmail3" class="control-label pull-left" style="margin-top:7px;margin-left: -2px; ">
									担当局:</span>-->
								<div class="pull-left" style="margin-left: 5px;display:none" >
									<select style="padding:4px 12px; ;width:75px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code' ,event:{change: bureauChange}" disabled></select>
								</div> 
								<!-- <input type="checkbox" class="pull-left" style="margin-left:20px; margin-top:8px; height:15px !important;">
								<span class="pull-left" style="margin-left:5px; margin-top:7px;">本局相关</span> -->
				        	     <div class="">
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
									<input id="isFuzzy" type="checkbox" checked="checked" class="pull-left" style="margin-left:10px; margin-top:8px; height:15px !important;">
									<span class="pull-left" style="margin-left:5px; margin-top:7px;">模糊</span>
								
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
								<div class="pull-right">
							<div class="pull-left" style="margin-left: 13px;">
								<button style="line-height:2.2;" class="btn btn-default greenBtn btn-sm pull-right" data-toggle="modal"
									data-target="#" id="generateAll"  data-bind="click: handOpen" disabled><i class="fa fa-hand-o-right"></i>生成全部</button>
								<!-- <button style="line-height:2.2;" class="btn btn-default greenBtn btn-sm pull-right" data-toggle="modal"
									data-target="#" id="generateAll"  data-bind="click: createPlan" disabled><i class="fa fa-hand-o-right"></i>生成全部</button>  -->
							</div>  
							<!-- <div class="pull-left" style="margin-left: 20px; margin-right:20px;">
								<a type="button" style="line-height:2.2;" class="btn btn-default blueBtn btn-sm pull-right" data-toggle="modal"
									data-target="#" id=""  onclick="autoOpen();"><i class="fa fa-life-saver"></i>自动生成设置</a> 
							</div>  -->   
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
				</div>
		</div>
										   
   	 	<div class="row"  style="margin: 5px 10px 10px 10px;" >
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

	 <!-- s -->
     <div id="shadowBlock" style="width:100%;height:100%;z-index:100;background:#ccc;opacity:0.40;display:none;position:absolute;top:0px;left:0px;"></div>
     <!-- 手动生成开行计划 -->
     <div id="handCreate" title="手动生成开行计划" style="display: none;overflow-x:hidden; overflow-y:hidden;">
    	<img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;"> 
        <form class="form-horizontal" style="margin-left: 30px;">
            <div class="form-group" style="margin-left:80px; margin-top:60px;">
                <span class="pull-left">已选择的交路：</span>
                <span class="pull-left" style="margin-left:5px;" id="handCreatePlanCrossName"></span>
            </div>
            <div class="form-group" style="margin-left:80px; margin-top:20px;">
                <span class="pull-left" style="margin-left:12px;margin-top:7px;">追加生成至：</span>
                <input class="pull-left" type="text"  style="padding:2px; border-radius:5px;margin-left:5px;" placeholder="" id="create_plan_endDate"  name="createPlanEndDate" data-bind="value: searchModle().createPlanEndDate">
            </div>
            <div class="form-group pull-right" style="margin-right:20px; margin-top:90px;">
                <a type="button" style="line-height:2.2;" class="btn btn-default blueBtn btn-sm pull-left" data-toggle="modal"
				data-target="#" id="" data-bind="click: createPlan">生成</a> 
				<a type="button" style="line-height:2.2; margin-left:20px;" class="btn btn-default grayBtn btn-sm pull-left" data-toggle="modal"
				data-target="#" id="" onclick="handClose();">取消</a> 
            </div>
        </form>

    </div> 

<%--     <!-- 自动生成设置 -->
     <div id="autoCreate" title="自动生成设置" style="display: none;overflow-x:hidden; overflow-y:hidden;">
    	<img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;"> 
        <form class="form-horizontal" style="margin-left: 30px;">
            <div class="form-group" style="margin-left:50px; margin-top:60px;">
                <span class="pull-left" style="margin-top:7px; margin-left:60px;">自动生成：</span>
                <input type="hidden" id="isAutoGenerate" value="-1"/>
                <div class="btn-group" style="margin-left:5px; margin-bottom:15px; margin-top:5px;" id="highlineFlag">
                    <button type="button" class="btn btn-default" style="width:70px; height:30px;;" id="highlineFlag_all" onClick="isAutoGenerateON()" value="1">打开</button>
                    <button type="button" class="btn btn-default" style="width:70px; height:30px;" id="highlineFlag_g" onClick="isAutoGenerateOff()" value="0">关闭</button>
                </div>
            </div>
            <div class="form-group" style="margin-left:50px; margin-top:20px;">
                <span class="pull-left" style="margin-left:84px;margin-top:7px;">步进：</span>
                <input class="pull-left" type="text" style="margin-left:5px;border-radius:5px;width:60px; padding-left:20px;" id="createDays" value="40">
                <span class="pull-left" style="margin-left:12px;padding-top:7px;">天</span>
            </div>
            <div class="form-group" style="margin-left:50px; margin-top:20px;">
                <span class="pull-left" style="margin-left:12px;margin-top:7px;">每日自动生成时间：</span>
<!--                 <input class="pull-left" type="text" style="padding:2px; border-radius:5px;margin-left:5px;" id="createHours" value="00:00"> -->
                <select class="pull-left" type="text" style="padding:2px; border-radius:5px;margin-left:5px; width:58px;padding-left:15px;" id="createHours">
                	<option value="0">0</option>
                	<option value="1">1</option>
                	<option value="2">2</option>
                	<option value="3">3</option>
                	<option value="4">4</option>
                	<option value="5">5</option>
                	<option value="6">6</option>
                	<option value="7">7</option>
                	<option value="8">8</option>
                	<option value="9">9</option>
                	<option value="10">10</option>
                	<option value="11">11</option>
                	<option value="12">12</option>
                	<option value="13">13</option>
                	<option value="14">14</option>
                	<option value="15">15</option>
                	<option value="16">16</option>
                	<option value="17">17</option>
                	<option value="18">18</option>
                	<option value="19">19</option>
                	<option value="20">20</option>
                	<option value="21">21</option>
                	<option value="22">22</option>
                	<option value="23">23</option>
                </select>
                <span class="pull-left" style="margin-left:12px;padding-top:7px;">时</span>
            </div>
            <!-- <HR style="FILTER: progid:DXImageTransform.Microsoft.Shadow(color:#987cb9,direction:145,strength:15)" width="100%" color="#987cb9" SIZE=1> -->
            <div class="form-group pull-right" style="margin-right:20px; margin-top:70px;">
                <a type="button" style="line-height:2.2;" class="btn btn-default blueBtn btn-sm pull-left" data-toggle="modal"
				data-target="#" id="" data-bind="click: autoCreatePlan">确定</a> 
				<a type="button" style="line-height:2.2; margin-left:20px;" class="btn btn-default grayBtn btn-sm pull-left" data-toggle="modal"
				data-target="#" id="" onclick="autoClose();">取消</a> 
            </div>
        </form>

    </div>  --%>
    
    <div id="setTime" style="display: none; margin: auto; width:400px; height:80px; border:1px solid #000">
    	<input type="time"><button class="blueBtn"  href="javascript:void(0)"  onclick="closeSetTime();">确认</button>
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
        JSLoader.loadJavaScript(basePath + "/assets/js/logTable/common.js"); 
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
<script>
//方案中默认是“全部方案”，但选择“全部方案”时，最右侧的“生成全部”按钮disabled。
$("#input_cross_chart_id").change(function(){
if(document.getElementById("input_cross_chart_id")[0].selected == true)
	$("#generateAll").attr("disabled",true);
	else{
	$("#generateAll").attr("disabled",false);
	}
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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlan/run_plan_create.js"></script>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script> --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>  --%>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>  --%>
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
		<td style="width:300px;height:20px;">
  			<span class="pull-left">开行信息（共<span data-bind="html: totalCount()"></span>个交路  &nbsp;&nbsp;当前页<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>   &nbsp;&nbsp;共<span data-bind="text: pageCount()"></span>页）</span> 								 
  	 	</td>
	 	<td style="display:none;color:red;" id="generateInfo">
			总交路：<span data-bind="html: totalCount()"></span>
			已生成：<span data-bind="html: totalCount()"></span>
			失败：<span data-bind="html: totalCount()-totalCount()">
	 	</td>
	
     
     <td align="right" style="height:20px;padding:0px;pading-bottom:-14;">   
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
	<td style="vertical-align: middle;width:30px;" rowspan="2" colspan="3">序<br>号</td>
 	<!--<td colspan="3" rowspan="2" style="vertical-align: middle;width:80px" ><input type="checkbox" value="1" data-bind="event:{change: $root.selectCrosses}, checked: $root.crossAllcheckBox"enable:$root.canCheckAll> </td> -->
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
    <!-- <td style="width:40px"><input name="checkedAll" type="checkbox" value="1" data-bind="event:{change: $root.selectCross.bind($data, $parent)},checked: $parent.selected,enable:$parent.canCheck2"></td> -->
    <td data-bind="text: $parent.tokenVehBureauShowValue" style="width:40px"></td>
    <td data-bind="attr:{colspan: $parent.colspan} "><span style="vertical-align: middle;" data-bind="html: $parent.createStatusShowValue"></span><button data-bind="click: $root.createPlan" class="blueBtn">生成</button></td> 
 	<!-- /ko -->   
 </tr> 
 <!-- /ko -->
 <!-- ko if: trainSort == 1 --> 
 <tr data-bind="foreach: runPlans">
    <!-- ko if: $index() == 0 --> 
    <td data-bind="text: '', attr:{rowspan: $parent.rowspan} " colspan="2" ></td> 
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
