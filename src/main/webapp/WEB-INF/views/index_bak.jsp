<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>路局客运计划编制</title>
    <link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
    <link type="text/css" href="${ctx}/assets/css/minified/jquery-ui.min.css" rel="stylesheet"/>
    <link href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet">
    <!--font-awesome-->
    <link  type="text/css" rel="stylesheet" href="${ctx}/assets/css/font-awesome.min.css"/>
    <!-- Custom styles for this template -->
    <link href="${ctx}/assets/css/style.css" rel="stylesheet">
    <script src="${ctx}/assets/js/html5.js"></script>
    <script src="${ctx}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/minified/jquery-ui.min.js"></script>
    <script src="${ctx}/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/common.js"></script>
    <script type="text/javascript" defer="defer">

        //被嵌入的Iframe根据不同的屏幕高度自适应
        $(document).ready(function () {
            var header = $(".header");
            var headerH = header.height();
            var body = $("body");
            var bodyW = body.width();
            var bodyH = body.height();
            var Win = $(document);
            var WW = $(document).width();
            var WH = $(document).height();
            var TargetBox = $("#contentLayerFrame");
            var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
            var Content_frame = function() {
                var WH = $(window).height();
                if (isChrome) {
                    TargetBox.css({ "height": (WH - headerH - 40)-5 + "px"});
                    TargetBox.css({ "min-height": (WH - headerH - 40)-5 + "px"});

                }else{
                    TargetBox.css({ "height": (WH - headerH - 40) - 5 + "px"});
                    TargetBox.css({ "min-height": (WH - headerH - 40) - 5 + "px"});
                }
            };
            Content_frame();
            $(window).resize(function () {
                Content_frame();
            });
        });
        //被嵌入的Iframe根据不同的屏幕高度自适应

        $(document).ready(function(){
            $(".navHref").click(function(){
                var src = $(this).attr("data-href");
                $("#contentLayerFrame").attr("src",src);
                location.hash=src;
            });
            if(location.hash!==''){
                var src = location.hash.substring(1, location.hash.length);
                $("#contentLayerFrame").attr("src",src);
            }

//            $("#kanban").get(0).click();
        });
        
        
        $(function(){
        	$("#indexLoginBtn").click(function(){
                window.location = "${ctx}/login";
            });
        });
        
    </script>
</head>
<body>
<div class="header">
    <div class="row">
        <div class="pull-left logo_name"><img src="${ctx}/assets/img/login-logo.png" height="50px"> </div>
        <div class="col-md-4 col-sm-4 col-xs-4 pull-right">
            <div class="btn-group pull-right" style="margin-top:15px;">
            	<!-- 已登录用户 -->
            	<shiro:authenticated>
	                <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
	                    <i class="fa fa-user"></i>
	                    <shiro:principal/>
	                    <span class="caret"></span>
	                </button>
	                <ul class="dropdown-menu" role="menu">
	                    <li><a href="#"><i class="fa fa-cog"></i>修改密码</a></li>
                       <%--  <li><a href="${ctx}/user/switch"><i class="fa fa-user"></i>切换用户</a></li> --%>
	                    <li class="divider"></li>
	                    <li><a href="${ctx}/logout"><i class="fa fa-sign-out"></i> 退 出</a></li>
	                </ul>
                </shiro:authenticated>
                
                <!-- 未登录用户 -->
                <shiro:notAuthenticated>
                	<button class="btn btn-warning" type="button" id="indexLoginBtn" style="padding:6px 12px; border-radius: 4px; width: 100px;margin-left: 170px;">登&nbsp;录</button>
                </shiro:notAuthenticated>
            </div>
            <!--btn-group-->
        </div>
        <!--col-md-6-->
    </div>
    <!--row-->
</div>
<!--header-->
<div class="sidebar">
    <!--nav-->
    <nav class="Navigation">
        <ul>
            <li>
                <a href="http://10.1.186.116:8090/dashboard/kanban/kanban.html" id="kanban" target="contentFrame" class="menu_one" style="cursor: hand;"><i class="fa fa-bar-chart-o"></i>首页（看板） </a>
            </li>
            
            <shiro:authenticated>
            <li>
                    <a target="contentFrame" class="menu_one"><i class="fa fa-list-ul"></i>编制开行计划<i class="fa fa-caret-down pull-right"></i></a>
                    
                     <ul class="second-menu" style="width:230px">

					<!-- <shiro:hasPermission name="JHPT.KYJH.JYXKD">  </shiro:hasPermission> -->
                      	<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlan" target="contentFrame"><i class="fa fa-external-link"></i>既有图定开行计划管理</a></li>
                      	<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlan/runPlanCreate?train_type=0" target="contentFrame"><i class="fa fa-sign-out"></i>既有图定生成开行计划</a></li>
                         <li style="width: 100%;height: 2px; /* border:#f00 2px solid; */ background-color: rgba(0, 0, 0, 0.75);"><i style="width: 100%;height: 2px;"></i></li>
                        <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlanLk/mainPage?train_type=0" target="contentFrame"><i class="fa fa-list-ol"></i>既有临客开行计划管理</a></li>
                         <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlanLk/addPage?train_type=0" target="contentFrame"><i class="fa fa-list-ol"></i>既有加开临客生成开行计划</a></li>
                         <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlanLk/stopPage?train_type=0" target="contentFrame"><i class="fa fa-list-ol"></i>既有停运临客生成开行计划</a></li>
                        <li style="width: 100%;height: 2px; /* border:#f00 2px solid; */ background-color: rgba(0, 0, 0, 0.75);"><i style="width: 100%;height: 2px;"></i></li>

                     	
                    <!--<shiro:hasPermission name="JHPT.KYJH.GTKD">    </shiro:hasPermission> -->
           	 			<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlan/runPlanGt" target="contentFrame"><i class="fa fa-list-alt"></i>高铁图定开行计划管理</a></li>
           	 			<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlan/runPlanCreate?train_type=1" target="contentFrame"><i class="fa fa-sign-out"></i>高铁图定生成开行计划</a></li>
                         <li style="width: 100%;height: 2px; /* border:#f00 2px solid; */ background-color: rgba(0, 0, 0, 0.75);"><i style="width: 100%;height: 2px;"></i></li>
                         <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlanLk/mainPage?train_type=1" target="contentFrame"><i class="fa fa-list-ol"></i>高铁临客开行计划管理</a></li>
                         <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlanLk/addPage?train_type=1" target="contentFrame"><i class="fa fa-list-ol"></i>高铁加开临客生成开行计划</a></li>
                   		  <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlanLk/stopPage?train_type=1" target="contentFrame"><i class="fa fa-list-ol"></i>高铁停运临客生成开行计划</a></li>
                   		<li style="width: 100%;height: 2px; /* border:#f00 2px solid; */ background-color: rgba(0, 0, 0, 0.75);"><i style="width: 100%;height: 2px;"></i></li>	
                    </ul>
                    
                </li>
            
            
            
            
            
       
                <li><a target="contentFrame" class="menu_one"><i class="fa fa-list-ul"></i>发布开行计划<i class="fa fa-caret-down pull-right"></i></a>
                    <ul class="second-menu">

								<!-- <shiro:hasPermission name="JHPT.KYJH.JYXKD">  </shiro:hasPermission> -->
		                        
		                         	<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlan/runPlanLineCreate?train_type=0" target="contentFrame"><i class="fa fa-sign-out"></i>发布既有开行计划</a></li>
		                            <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/audit?train_type=0" target="contentFrame"><i class="fa fa-external-link"></i>审核既有开行计划</a></li>
		                            <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/default/transfer/planReviewLines?train_type=0" target="contentFrame"><i class="fa fa-external-link"></i>既有运行线查询</a></li>
		                       
		                           <li style="width: 100%;height: 2px; /* border:#f00 2px solid; */ background-color: rgba(0, 0, 0, 0.75);"><i style="width: 100%;height: 2px;"></i></li>
		                       <!--<shiro:hasPermission name="JHPT.KYJH.GTKD">    </shiro:hasPermission> -->
				            	
				            	 	<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/runPlan/runPlanLineCreate?train_type=1" target="contentFrame"><i class="fa fa-sign-out"></i>发布高铁开行计划</a></li>
		                            <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/cross/plan?type=examine" target="contentFrame"><i class="fa fa-external-link"></i>审核高铁开行计划</a></li>
		                            <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/default/transfer/planReviewLines?train_type=1" target="contentFrame"><i class="fa fa-external-link"></i>高铁运行线查询</a></li>
		                      
	
                    </ul>
                </li>

                





				<li>
                    <a target="contentFrame" class="menu_one"><i class="fa fa-list-ul"></i>车底三乘计划<i class="fa fa-caret-down pull-right"></i></a>
		            <ul class="second-menu" style="width:200px">
                            <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/highLine" target="contentFrame"><i class="fa fa-external-link"></i>高铁车底交路计划编制</a></li>
      
                            <li style="width: 100%;"><a href="${ctx}/highLine/vehicleSearch" target="contentFrame" style="width: 100%;"><i class="fa fa-list-ol"></i>高铁车底交路计划查询</a></li>  
                           <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/highLine/vehicleCheck" target="contentFrame" ><i class="fa fa-external-link"></i>高铁车底运用计划审核</a></li>              
		           			<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/crew/page/all" target="contentFrame"><i class="fa fa-external-link"></i>高铁乘务计划查询</a></li>
		           	
		           		<li style="width: 100%;height: 2px; /* border:#f00 2px solid; */ background-color: rgba(0, 0, 0, 0.75);"><i style="width: 100%;height: 2px;"></i></li>
		        		<li style="width: 100%;"><a style="width: 100%;" target="contentFrame" ><i class="fa fa-external-link"></i>高铁站段报告<i class="fa fa-caret-right pull-right"></i></a>
				      		<ul class="third-menu" style="width:200px">
			               			<li style="width: 100%;"><a style="width: 100%;" href="${ctx}/highLine/vehicle" target="contentFrame"><i class="fa fa-external-link"></i>高铁车底运用计划报告</a></li>
			                        <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/crew/page/cz" target="contentFrame"><i class="fa fa-external-link"></i>高铁车长乘务计划报告</a></li>
			                        <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/crew/page/sj" target="contentFrame"><i class="fa fa-external-link"></i>高铁司机乘务计划报告</a></li>
			                        <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/crew/page/jxs" target="contentFrame"><i class="fa fa-external-link"></i>高铁机械师乘务计划报告</a></li>
		                        
				            </ul>
			          </li>
		            </ul>
                </li>
				
				
                                
                <li>
                    <a target="contentFrame" class="menu_one"><i class="fa fa-list-ul"></i>下达客运计划<i class="fa fa-caret-down pull-right"></i></a>
		            <ul class="third-menu" style="width:180px">
		            	<!-- <shiro:hasPermission name="JHPT.KYJH.JYXKD">  </shiro:hasPermission> -->
                      
                            <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/releasePlan?train_type=0" target="contentFrame"><i class="fa fa-external-link"></i>下达既有客运计划</a></li>
                        
                        <li style="width: 100%;height: 2px; /* border:#f00 2px solid; */ background-color: rgba(0, 0, 0, 0.75);"><i style="width: 100%;height: 2px;"></i></li>
		            	 <!--<shiro:hasPermission name="JHPT.KYJH.GTKD">    </shiro:hasPermission> -->
                            <li style="width: 100%;"><a style="width: 100%;" href="${ctx}/cross/plan?type=release" target="contentFrame"><i class="fa fa-external-link"></i>下达高铁客运计划</a></li>
                        
		            </ul>
                </li>

                <li><a href="#" class="menu_one" enabled = "false"><i class="fa fa-file"></i>计划兑现</a></li>
                
                <li>
                    <a target="contentFrame" class="menu_one"><i class="fa fa-list-ul"></i>管理基本图<i class="fa fa-caret-down pull-right"></i></a>
                    <ul>
                        <li><a href="${ctx}/jbtcx" target="contentFrame"><i class="fa fa-search"></i>基本图查询</a></li>
                        <li><a href="${ctx}/cross" target="contentFrame"><i class="fa fa-pencil"></i>对数表管理</a>
                        <li><a href="${ctx}/cross/unit" target="contentFrame"><i class="fa fa-retweet"></i>交路单元管理</a></li>
                    </ul>
                </li>
                
	            <!-- 消息测试  发布时需要注释 -->
	            <%-- <li><a target="contentFrame" class="menu_one"><i class="fa fa-road"></i>消息测试<i class="fa fa-caret-down pull-right"></i> </a>
	                <ul>
	                    <li><a href="${ctx}/message/send" target="contentFrame"><i class="fa fa-pencil"></i>消息发送</a></li>
	                    <li><a href="${ctx}/message/receive" target="contentFrame"><i class="fa fa-retweet"></i>接收消息</a></li>
	                </ul>
	            </li> --%>
	            
            </shiro:authenticated>
        </ul>
    </nav>
</div>
<!--sidebar-->
<div class="content">
    <!--content-menu-->
    
    <!--左侧菜单-->
    <section class="leftMenu">
        <ul class="nav nav-pills nav-stacked" role="tablist">
            <li class="active"><a href="#">资源管理</a></li>
            <li ><a href="#">资源管理</a></li>
            <li ><a href="#">资源管理</a></li>
            <li ><a href="#">资源管理</a></li>
        </ul>
    </section>
    <!--左侧菜单-->
    
    <!--Iframe嵌入页面-->
    <div class="iframebox" style="margin-left: 80px; height:auto;" id="ContentBox">
        <iframe id="contentLayerFrame" src="" frameborder=0 name="contentFrame" style="width:100%; height:auto; overflow-x:hidden;" > </iframe>
    </div>
    <!--嵌入页面end-->
</div>
<!--content-->
</body>
</html>
