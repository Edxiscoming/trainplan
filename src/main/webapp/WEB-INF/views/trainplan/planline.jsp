<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getContextPath();
Object runplan =  request.getAttribute("runplan");
Object runline =  request.getAttribute("runline");
Object grid =  request.getAttribute("grid");

System.err.println("grid="+grid);
System.err.println("runplan="+runplan);
System.err.println("runline="+runline);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>运行线对比</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body>
<!--分栏框开始-->
<div class="row">
  <div style="float:left; width:100%;">
    <!--分栏框开始-->
    <div class="panel panel-default">
      <!--panle-heading-->
      <div class="panel-body">
      	<div class="row" style="margin:-5px 0 10px 0;">
	      <form class="form-inline" role="form">
              <div class="input-group">
                  <font color="#8236ac"><label class="margin-right-10">客运开行计划:</label></font>
                  <input id="runline_ky" class="checkbox-inline" style="margin: 0px 10px 0px 0px;" type="checkbox"  checked='checked'>
                  <font color="#72b5d2"><label class="margin-right-10">运行线:</label></font>
                  <input id="runline_jbt" class="checkbox-inline" style="margin: 0px 10px 0px 0px;" type="checkbox"  checked='checked'>
              </div>
          </form>
	    </div>
	    
    
        <div class="table-responsive" >
        	<canvas id="trainplan_canvas"></canvas>
        </div>
        
      </div>
      <!--panel-body--> 
    </div>
    <!--分栏框结束--> 
    <!--分栏框开始-->
    <!--分栏框结束--> 
  </div>
  
  
</div>



<script type="text/javascript">
var canvasData = {};
canvasData.grid = <%=grid%>;
canvasData.runplan = <%=runplan%>;//客运计划线
canvasData.runline = <%=runline%>;//运行线
</script>



<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/train_runline_canvas.js"></script>

</body>
</html>
