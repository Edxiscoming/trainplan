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
Object chartIds =  request.getAttribute("chartIds");
Object crossNames =  request.getAttribute("crossNames");
Object crossIds =  request.getAttribute("crossIds");
Object message =  request.getAttribute("message");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>时刻表</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body >


<input type="hidden" id="chartIds" value="<%=chartIds%>"/>
<input type="hidden" id="crossNames" value="<%=crossNames%>"/>
<input type="hidden" id="crossIds" value="<%=crossIds%>"/>
<!--分栏框开始-->
<div class="row" >
  <div >
  <textarea id="ms" rows="28" cols="126"></textarea>
  <br/><br/><br/>
<div align="center">
  <a id="shengcheng" type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success">是</a>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <a id="cencel" type="button" style="margin-left: 5px;margin-top: -5px" class="btn btn-success">否</a>
 </div>
</div>

<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.ui.widget.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-ui-1.10.4.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script> 
<script type="text/javascript" src="<%=basePath%>/assets/js/moment.min.js"></script>
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/cross.js"></script> --%>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/cross/crossRepeat/crossRepeat.js"></script>  
<%-- <script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>  --%>
</body>
</html>

