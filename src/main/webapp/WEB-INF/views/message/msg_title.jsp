<%@page import="javax.sound.midi.SysexMessage"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<title>客运计划编制-消息</title>
<%-- <jsp:include page="/assets/commonpage/global.jsp" flush="true" /> --%>

<% 
String basePath = request.getContextPath();
Object postname =  request.getAttribute("postname");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" type="text/css" media="screen" href="/trainplan/assets/css/msg/main.css">
<link rel="stylesheet" type="text/css" href="/trainplan/assets/bootstrap/bootstrap2.min.css">

<script type="text/javascript" src="/trainplan/assets/js/jquery.js"></script>
<script type="text/javascript" src="/trainplan/assets/js/knockout.js"></script>
<script type="text/javascript" src="/trainplan/assets/js/html5.js"></script>
<script type="text/javascript" src="/trainplan/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/trainplan/assets/js/respond.min.js"></script>
<script type="text/javascript" src="/trainplan/assets/js/trainplan/message/msg_title.js"></script>
<style type="text/css">
	tr {
		height:25px;
		line-height:25px;
	}
</style>
</head>
<body class="Iframe_body" style="margin:-24px 12px 0px -4px">
<input type="hidden" id="postnameId" value="<%=postname%>"/>
<table class="table table-striped table-hover" width="95%" border="0" cellspacing="0" cellpadding="0"
			align="center">
			<tbody data-bind="foreach: msglists">
		           <tr>  
		           <td width="20px"><div class="reddot"></div></td>
<!-- 					<td class="message_tit" width="7px" align="center" data-bind=" text: $index()"></td> -->
					<td class="message_tit" width="35px" align="center"  data-bind="text: sendBureau, attr:{title: sendBureau}"></td>
					<td width="30px"><div class="reddot"></div></td>
					<td class="message_tit" width="220px" align="left" data-bind="text: sendPost"></td>
					<td class="message_tit" align="left" data-bind="text: typeName"></td>
					<td class="message_date" width="100px" align="c"  data-bind="text: sendTime"></td>
					<td width="20px"><div class="reddot"></div></td>
				</tr>
			</tbody>
			
			
<!-- 			<tr> -->
<!-- 				<td width="10px"><div class="reddot"></div></td> -->
<!-- 				<td><a href="javascript:void(0)" -->
<!-- 					onclick="$('#pop1').window('open')" class="message_tit">[成]新增图定开行计划（K771-K772）</a></td> -->
<!-- 				<td class="message_date" width="80px">07-17&nbsp; &nbsp;19:01</td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
<!-- 				<td>&nbsp;</td> -->
<!-- 				<td class="message_tit_disable">[上] 修改图定开行计划（T179-T180)</td> -->
<!-- 				<td class="message_date_disable">07-17&nbsp; &nbsp;13:22</td> -->
<!-- 			</tr> -->
			
		</table>
</body>
</html>