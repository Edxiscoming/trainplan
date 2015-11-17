<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page
	import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List"%>
<%

    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String basePath1 = request.getContextPath();
			String user = request.getParameter("user");
			String acc = request.getParameter("acc");
			String ljjc = request.getParameter("ljjc");
			ljjc = java.net.URLDecoder.decode(ljjc.replaceAll("-", "%"),
					"UTF-8");

			String pass = request.getParameter("pass");
			String url = "http://10.1.186.139:8080" + basePath1 + "/cmdLogin2?username=";
			url += user + "@" + acc + "@" + ljjc + "&password=" + pass;
			String username = user + "@" + acc + "@" + ljjc;
			String actionUrl = ".." + basePath1 +"/login";
			
	String isLogin = "-1";	
	ShiroRealm.ShiroUser shiroUser = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
	if(shiroUser!=null){
		isLogin = "1";
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'mocklogin.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>

<body>
	<br>
	<iframe src=""></iframe>
	<input type="hidden" value="<%=isLogin%>" id="isLoginFlag"/>
	<form method="post" action="<%=actionUrl%>" id="loginForm">
		<input type="text" name="username" value="<%=username%>"> <input
			type="text" name="password" value="<%=pass%>">
	</form>
	<script type="text/javascript">
		var islogin = document.getElementById('isLoginFlag').value;
		var uu = "<%=url %>";
 		//alert(uu);
		if(islogin=="-1"){
			document.getElementById('loginForm').submit();
		}
<%-- 		window.location.href=<%=url%>; --%>
<%-- 		window.open(<%=url%>); --%>
	</script>
</body>
</html>
