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


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>交路单元</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body >
<!--分栏框开始-->
<div class="row" >
  <div style="float:left;">
    <!--分栏框开始-->
    <div class="panel panel-default margin">
     <div class="panel-body panel-body_iframe" style="padding: 0 43px 0 43px;">
	   	<div class="row">
	      <div class="row fix_box">
	         <div class="row" style="margin:15px auto 5px auto;width:260px;">
	         	  <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_refresh"><i class="fa fa-refresh"></i>刷新</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_magnification"><i class="fa fa-search-plus"></i>X+</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_shrink"><i class="fa fa-search-minus"></i>X-</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_magnification"><i class="fa fa-search-plus"></i>Y+</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_shrink"><i class="fa fa-search-minus"></i>Y-</button>
	         </div>
	         <div class="row" style="margin:5px auto 15px auto;width:410px;">   
	                                     比例：｛X:<label id="canvas_event_label_xscale">1</label>倍；Y:<label id="canvas_event_label_yscale">1</label>倍｝
				<input type="checkbox" id="canvas_checkbox_stationType_jt" name="canvas_checkbox_stationType" checked="checked" style="margin-left:10px">简图
	         	<input type="checkbox" id="canvas_checkbox_trainTime"  value=""/>显示时刻
	         	&nbsp;&nbsp;选择车底：<select id="canvas_select_groupSerialNbr"></select>
	          </div> 	  
	      </div>
	    </div>
   
        <div class="table-responsive" style="width:100%;overflow-x: visible;overflow-y: visible;margin: 82px 0 20px 0;padding: 10px 0;">
        	<canvas id="unit_cross_canvas">您的浏览器不支持HTML5</canvas>
        <!--<canvas id="unit_cross_canvas_x">您的浏览器不支持HTML5</canvas>
        	<canvas id="unit_cross_canvas_y">您的浏览器不支持HTML5</canvas>-->
        </div>
        
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
  </div>
 </div> 
  
</div>



<script type="text/javascript">
var canvasData = {};
canvasData.grid = <%=grid%>;
canvasData.jlData = <%=jlData%>;//交路数据
var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
</script>



<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/cross/unit_cross_canvas_rightmenu.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/cross/unit_cross_canvas.js"></script>

</body>
</html>
