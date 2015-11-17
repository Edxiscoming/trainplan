<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>message</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<jsp:include page="/assets/commonpage/include-dwr-script.jsp" flush="true" />


<script type="text/javascript">
$(function(){
	$("#msgAll").click(function(){
		PushMsg.sendMessage("这是一个测试广播消息","<%=basePath%>/message/receive", "showMessage");
	});
	
	$("#msgZbzr").click(function(){
		PushMsg.sendMessageToUser("yszbzr","这是发给演示值班主任的消息","<%=basePath%>/message/receive", "showMessage");
	});
	
	$("#msgNckd").click(function(){
		PushMsg.sendMessageToUser("nckd","这是发给南昌客调的消息","<%=basePath%>/message/receive", "showMessage");
	});
	
	$("#msgHebkd").click(function(){
		PushMsg.sendMessageToUser("hebkd","这是发给哈尔滨客调的消息","<%=basePath%>/message/receive", "showMessage");
	});
	

	$("#msgRoleAll").click(function(){
		PushMsg.sendMessage("这是发给所有客调角色的消息","<%=basePath%>/message/receive", "showMessage");
	});
	
	$("#msgRoleHeb").click(function(){
		PushMsg.sendMessageToRole("哈局客调","这是发给哈局客调角色的消息","<%=basePath%>/message/receive", "showMessage");
	});
	
	
	$("#msgRoleJjzbzr").click(function(){
		PushMsg.sendMessageToRole("京局值班主任","这是发给京局值班主任角色的消息","<%=basePath%>/message/receive", "showMessage");
	});
});


</script> 
</head>
<body>
这是消息发送页面
</br>
</br>
<input id="msgAll" type="button" value="广播消息">
<input id="msgZbzr" type="button" value="发消息给演示值班主任">
<input id="msgNckd" type="button" value="发消息给南昌客调">
<input id="msgHebkd" type="button" value="发消息给哈尔滨客调">
</br>
</br>
<input id="msgRoleAll" type="button" value="发给所有客调角色">
<input id="msgRoleHeb" type="button" value="发给哈局客调角色">
<input id="msgRoleJjzbzr" type="button" value="发给京局值班主任角色">

</body>
</html>