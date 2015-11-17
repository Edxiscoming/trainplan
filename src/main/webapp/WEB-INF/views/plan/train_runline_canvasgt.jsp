<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%

String basePath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列车运行线图</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body>

<input id="input_hidden_planTrainId" type="hidden" value="${planTrainId}"/>

<input id="input_hidden_trainNbr" type="hidden" value="${trainNbr}"/>

<!--分栏框开始-->
<div class="row">
  <div style="float:left;">
    <!--分栏框开始-->
    <div class="panel panel-default margin">
      <!--panle-heading-->
      <div class="panel-body panel-body_iframe" style="padding:0 34px;">
    	<div class="row">
	      <div class="row fix_box">
              <div class="row Operation_box">
              
	              <button style="margin-left:10px" type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_magnification"><i class="fa fa-search-plus"></i>X+</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_x_shrink"><i class="fa fa-search-minus"></i>X-</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_magnification"><i class="fa fa-search-plus"></i>Y+</button>
	              <button type="button" class="btn btn-success btn-xs" id="canvas_event_btn_y_shrink"><i class="fa fa-search-minus"></i>Y-</button>
	                                                 比例：｛X:<label id="canvas_event_label_xscale">1</label>倍；Y:<label id="canvas_event_label_yscale">1</label>倍｝
				  <span><input type="checkbox" id="canvas_checkbox_stationType_jt" name="canvas_checkbox_stationType" checked="checked" style="margin-left:10px">简图</span>
	         	  <input type="checkbox" id="canvas_checkbox_trainTime" style="margin-left:10px;margin-top:2px"  value=""/>时刻	                       
	         </div>
          </div>
	    </div> 
        <div id="canvas_parent_div" class="table-responsive" style="width:100%;overflow-x: visible;overflow-y: visible;margin-top: 60px;">
        	<canvas id="div_canvas"></canvas>
        </div>
        
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
    <!--分栏框开始-->
    <!--分栏框结束--> 
  </div>
  
  
</div>





<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/plan/train_runline_canvasgt.js"></script>





</body>
</html>