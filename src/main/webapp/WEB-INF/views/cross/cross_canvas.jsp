<%@page import="javax.sound.midi.SysexMessage"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}


String basePath = request.getContextPath();
Object jlData =  request.getAttribute("myJlData");
Object grid =  request.getAttribute("gridData");
Object editStnSort =  request.getAttribute("editStnSort");


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>交路单元</title>
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


<%-- <jsp:include page="/assets/commonpage/global.jsp" flush="true" /> --%>

<script type="text/javascript">
var basePath = "<%=basePath %>";
var canvasData = {};
canvasData.grid = <%=grid%>;
canvasData.editStnSort = <%=editStnSort%>;
canvasData.jlData = <%=jlData%>;//交路数据
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
</script>

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
<%-- 
<link href="<%=basePath %>/assets/easyui/themes/default/easyui.css" rel="stylesheet">
<link href="<%=basePath %>/assets/easyui/themes/icon.css" rel="stylesheet">
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/cross/cross_canvas.js"></script>
 --%>
<body >
<!--分栏框开始-->
<div class="row" >
  <div  style="float:left;">
    <!--分栏框开始-->
    <div class="panel panel-default margin">
      <div class="panel-body panel-body_iframe" style="padding: 0 43px 0 43px;">
	   	<div class="row">
	      <div class="row fix_box">
	          <div class="row" style="margin:15px auto 5px auto;width:600px;">
	         	  <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_refresh"><i class="fa fa-refresh"></i>刷新</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_magnification"><i class="fa fa-search-plus"></i>X+</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_shrink"><i class="fa fa-search-minus"></i>X-</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_magnification"><i class="fa fa-search-plus"></i>Y+</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_shrink"><i class="fa fa-search-minus"></i>Y-</button>&nbsp;&nbsp;
	              
	              <button type="button" style="display:none" class="btn btn-success btn-xs" id="editStationSortBut"><i class="fa fa-pencil-square-o"></i>修改站序</button>
	              <button type="button" style="display:online" class="btn btn-success btn-xs" id="deleteStationSortBut"><i class="fa fa-trash-o red"></i>格式化站序</button>
	          </div>
	          <div class="row" style="margin:5px auto 15px auto;width:410px;">    
	                                                比例：｛X:<label id="canvas_event_label_xscale">1</label>倍；Y:<label id="canvas_event_label_yscale">1</label>倍｝
				<input type="checkbox" id="canvas_checkbox_stationType_jt" name="canvas_checkbox_stationType" checked="checked" style="margin-left:10px">简图
	         	<input type="checkbox" id="canvas_checkbox_trainTime"  value=""/>显示时刻
	         	&nbsp;&nbsp;选择车底：<select id="canvas_select_groupSerialNbr"></select>
	          </div>
	         </div>
	      </form>
	    </div>
   
      <div>
        <div class="table-responsive" style="width:100%;overflow-x: visible; overflow-y:visible;margin:82px 0 20px 0;">
        	<canvas id="unit_cross_canvas">您的浏览器不支持HTML5</canvas>
        </div>
        
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
    </div>
  </div>
  
  
</div>

<!-- <div id="editStationSort" class="easyui-dialog" title="修改站序"
	data-options="iconCls:'icon-save'"
	style="width: 500px; height: 600px;overflow-y: hidden;">
	<iframe style="width: 100%; height: 590px;border: 0;overflow-y: hidden;" src="editStationSort?drawGraphId=0D5304-D5303-D5306-D5311-D5314-D5319-0D5319-0D8536-DJ8536-D5301-D5304-D5309-D5312-0D5311_a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf5"></iframe>
</div>  -->

</body>

 <script type="text/html" id="tablefooter-short-template"> 
</script> 

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
        JSLoader.loadJavaScript(basePath + "/assets/js/datepicker.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/jquery.gritter.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/common.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/respond.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/moment.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/lib/fishcomponent.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/resizable.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/knockout.pagemodle.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/moment.min.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/lib/fishcomponent.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/util/fishcomponent.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/util/canvas.util.js"); 
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/util/canvas.component.js"); 
//         	JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/canvas_rightmenu.js");
        //JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/canvas_rightmenu.js"); 
//         JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/canvas_event_getvalue.js");
     
//         JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/runPlan/run_plan_gt.js");
        JSLoader.loadJavaScript(basePath + "/assets/js/trainplan/cross/cross_canvas.js");
</script>

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
