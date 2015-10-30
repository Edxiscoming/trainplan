<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.LockedAccountException "%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
    <link rel="stylesheet" type="text/css" href="${ctx}/assets/css/custom-bootstrap.css"/>
    <link type="text/css" rel="stylesheet" href="${ctx}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/assets/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/assets/css/login.css"/>
    <script type="application/javascript" src="${ctx}/assets/js/jquery.js"></script>
    <script type="application/javascript" src="${ctx}/assets/js/html5.js"></script>
    <script type="application/javascript" src="${ctx}/assets/js/bootstrap.min.js"></script>
    <script type="application/javascript" src="${ctx}/assets/js/respond.min.js"></script>
    <script type="application/javascript" type="application/javascript" src="${ctx}/assets/js/trainplan/login.js"></script>
    <title>路局客运计划编制</title>
    <script type="text/javascript">
 /*  $(function(){
   	    window.location.reload();
    }); */
    </script>
</head>
<body>
<div class="login-top">
    <div class="login-logo background">
    	<div class="right-link">
    		<p class="right-t9ext pull-left">您的IP地址：${ip }</p>
    		<!-- <a href="http://dlsw.baidu.com/sw-search-sp/soft/9d/14744/ChromeStandaloneSetup41.0.2272.89.1426235198.exe" class="pull-right">下载Chrome</a> -->
    		<!-- <a href="http://10.1.4.123/Soft/ShowSoftDown.asp?UrlID=1&SoftID=2178" class="pull-right">下载Chrome</a> -->
    		<a href="javascript:void(0)" onclick="openChrome()" class="pull-right">下载Chrome</a>
    	</div>
    </div>
</div>    
<div class="login">
    <div class="login_backgroud">
    	<div class="login_center row">
        <div class="login_btns">
            <!-- <button class="btn btn-warning btn-left active" type="button">哈局</button> -->
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">哈局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">沈局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">京局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">太局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">呼局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">郑局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">武局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">西局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">济局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">上局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">南局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">广铁</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">宁局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">成局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">昆局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">兰局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">乌局</button>
            <button class="btn btn-warning btn-left" type="button" onclick="focusUserName()">青藏</button>
        </div>
        <div class="login_overlay">
            <div class="login-logo backg-no">请您登录</div>
            <div class="login_input" style="margin-left:">
                <%
                    String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
                    if(error != null){
                %>
                <div class="alert alert-danger controls input-large">
                    <button class="close" data-dismiss="alert">×</button>
                    <%
                        if(error.contains("DisabledAccountException")){
                            out.print("用户已被屏蔽,请登录其他用户.");
                        } else if(error.contains("IncorrectCredentialsException")) {
                            out.println("用户名或密码错误");
                        } else if(error.contains("Exception")) {
                            out.println("路局和用户不匹配");
                        }
                        else{
                            out.print("登录失败，请重试.");
                        }
                    %>
                </div>
                <%
                    }
                %>
                <div class="form-group paddingtop30">
                    <label for="bureauShortName" class="col-sm-2 control-label">路局 </label>
                    <div class="">
                        <input type="text" id="department" tabindex="1" class="form-control" placeholder="路局" disabled="disabled" value="">
                        <span id="lj" style="color:red"><font color="red"></font> </span>
                    </div>
                </div>  
                <div class="form-group paddingtop30">
                    <label for="inputUsername" class="col-sm-2 control-label">用户 </label>
                    <div class="">
                        <input type="text" tabindex="2" class="form-control" id="inputUsername" placeholder="用户名">
                        <span id="userName" style="color:red"><font color="red"></font> </span>
                    </div>
                </div>
                <!--form-group-->
                <div class="form-group paddingtop30">
                    <label for="inputAccount" class="col-sm-2 control-label">岗位 </label>
                    <div class="">
                        <select id="inputAccount" tabindex="3" class="form-control"></select>
                        <span id="acc" style="color:red"><font color="red"></font> </span>
                    </div>
                </div>
                <!--form-group-->
                <div class="form-group paddingtop30">
                    <label for="inputPassword" class="col-sm-2 control-label">密码 </label>
                    <div class="">
                        <div class="input-group">
                            <input type="password" tabindex="4" class="form-control" id="inputPassword" placeholder="密码">
                        <span id="pw" style="color:red"><font color="red"></font> </span>
                        </div>
                    </div>
                </div>

                <div class="form-group paddingtop30">
                        <form class="form-horizontal" role="form" method="post" action="${ctx}/login" id="loginForm" name="loginForm" style="margin:20px 0;">
                            <div class="row">
                                <input type="hidden" name="username">
                                <input type="hidden" name="password">
                                <button class="btn btn-warning btn-login" tabindex="5" type="submit" id="login">登&nbsp;&nbsp;录</button>
                                <button class="btn btn-success btn-login btn-login-last" tabindex="6" type="button" onclick="resetData()" id="">重&nbsp;&nbsp;置</button>
                            </div>
                        </form>
                </div>

                <!--输入框结束-->
                <!--错误提示开始-->
                <!--      <div class="alert alert-danger" style="margin-top:15px;width:200px;height:26px;line-height:26px;font-size:10px;padding-top:0px; ">您输入的用户名或密码有误！ </div>-->
                <!--错误提示结束-->
            </div>
            <!-- <div class="aquila" style="margin-right: -500px;"></div> -->
        </div>
      </div>
    </div>
</div>
<div class="login-bottom">中国铁路总公司&nbsp&nbsp运输局&nbsp&nbsp·&nbsp&nbsp信息技术中心</div>

<script type="text/javascript">
    $(document).ready(function () {
        $(".login_backgroud").css("min-height", $(window).height() -260 + "px");
        $(window).resize(function () {
            $(".login_backgroud").css("min-height", $(window).height() -260 + "px");
        });
        $(".login_btns .btn-left").click(function() {
            $(".login_btns .btn-left").removeClass("active");
            $(this).addClass("active");
            var department = $(this).text();
            $("#department").val(department);
        })
    });
</script>
</body>
</html>
