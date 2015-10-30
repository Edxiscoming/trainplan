<%@ page contentType="text/html;charset=UTF-8"%>
<%
	String basePath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<%-- <meta http-equiv="refresh" content="10"> --%>
<title>客运计划编制</title>
<link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
<link type="text/css"
	href="${ctx}/assets/css/minified/jquery-ui.min.css" rel="stylesheet" />
<link href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet">


<%--   <link href="${ctx}/assets/easyui/themes/default/easyui.css" rel="stylesheet">
    <link href="${ctx}/assets/easyui/themes/icon.css" rel="stylesheet">
    <script type="text/javascript" src="${ctx}/assets/easyui/jquery.easyui.min.js"></script> --%>
<!--font-awesome-->
<link type="text/css" rel="stylesheet"
	href="${ctx}/assets/css/font-awesome.min.css" />
<!-- Custom styles for this template -->
<link href="${ctx}/assets/css/style.css" rel="stylesheet">

<script>
        window.alert = function(txt) {
            return;
        }
       
    </script>

<script src="${ctx}/assets/js/html5.js"></script>

<script src="${ctx}/assets/js/jquery.js"></script>
<script type="text/javascript"
	src="${ctx}/assets/js/minified/jquery-ui.min.js"></script>
<script src="${ctx}/assets/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${ctx}/assets/js/trainplan/common.js"></script>
<script src="${ctx}/assets/js/password.js"></script>
<script type="text/javascript">
	var basePath = "<%=basePath%>";
</script>
<script language="JavaScript">
	setInterval('msgCount()', 2000);
/* 	function msgCount() {
		$.ajax({
			url : basePath + "/msg/getReceiveMsgCount",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				statusCode : 0
			}),
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null && result.data != undefined) {
						$("#msgCount").text(result.data);
					}
				}
			},
			error : function() {
			},
			complete : function() {
			}
		});
	} */
</script>

<script type="text/javascript" defer="defer">
	//被嵌入的Iframe根据不同的屏幕高度自适应
	$(document).ready(
			function() {
				var header = $(".header");
				var headerH = header.height();
				var body = $("body");
				var bodyW = body.width();
				var bodyH = body.height();
				var Win = $(document);
				var WW = $(document).width();
				var WH = $(document).height();
				var TargetBox = $("#contentLayerFrame");
				var isChrome = navigator.userAgent.toLowerCase()
						.match(/chrome/) != null;
				var Content_frame = function() {
					var WH = $(window).height();
					TargetBox.css({
						"height" : (WH - headerH - 45) + "px"
					});
					TargetBox.css({
						"min-height" : (WH - headerH - 45) + "px"
					});

				};
				Content_frame();
				$(window).resize(function() {
					Content_frame();
				});

			});
	//被嵌入的Iframe根据不同的屏幕高度自适应

	$(document).ready(function() {
		$(".navHref").click(function() {
			var src = $(this).attr("data-href");
			$("#contentLayerFrame").attr("src", src);
			location.hash = src;
		});
		if (location.hash !== '') {
			var src = location.hash.substring(1, location.hash.length);
			$("#contentLayerFrame").attr("src", src);
		}

		//            $("#kanban").get(0).click();
	});

	$(function() {
		$("#indexLoginBtn").click(function() {
			window.location = "${ctx}/login";
		});
	});

	function getcheckPlan() {/*有:首页查询*/

		$("li[class='active']").removeAttr("class");
		$("#getcheckPlan2").attr("class", "active");
		$("#ContentBox").attr("style", "margin-left: 80px");

		$("section.leftMenu").hide();
		$("#getcheckPlan").show();

		$("#hideindex").show();
		$("#location").text("综合查询 -> ");
		$("#location2").text("普速开行");
	}

	function getRunPlan() {/*有:普速开行计划*/

		$("li[class='active']").removeAttr("class");
		//$("#runPlan2").attr("class", "active"); 
		$("#runPlan ul li:first-child").attr("class", "active");
		$("#ContentBox").attr("style", "margin-left: 80px");

		$("section.leftMenu").hide();
		$("#runPlan").show();

		$("#hideindex").show();
		$("#location").text("普速开行计划 -> ");
		$("#location2").text($("#runPlan ul li:first-child").text());
	}

	function getRunPlanGt() {/*有:高铁开行计划*/

		$("li[class='active']").removeAttr("class");
		//$("#runPlanGt2").attr("class", "active"); 
		$("#runPlanGt ul li:first-child").attr("class", "active");
		$("#ContentBox").attr("style", "margin-left: 80px");

		$("section.leftMenu").hide();
		$("#runPlanGt").show();

		$("#hideindex").show();
		$("#location").text("高铁开行计划 -> ");
		$("#location2").text($("#runPlanGt ul li:first-child").text());
	}

	function overplan() {/*有:普速次日计划*/

		$("li[class='active']").removeAttr("class");
		//$("#overplan2").attr("class", "active"); 
		$("#overplan ul li:first-child").attr("class", "active");
		$("#ContentBox").attr("style", "margin-left: 80px");

		$("section.leftMenu").hide();
		$("#overplan").show();

		$("#hideindex").show();
		$("#location").text("普速次日计划 -> ");
		$("#location2").text($("#overplan ul li:first-child").text());
	}

	function overplanGt() {/*有:高铁次日计划*/

		$("li[class='active']").removeAttr("class");
		//$("#overplanGt2").attr("class", "active"); 
		$("#overplanGt ul li:first-child").attr("class", "active");
		$("#ContentBox").attr("style", "margin-left: 80px");

		$("section.leftMenu").hide();
		$("#overplanGt").show();

		$("#hideindex").show();
		$("#location").text("高铁次日计划 -> ");
		$("#location2").text($("#overplanGt ul li:first-child").text());
	}

	function getBase() {/*有:技术资料*/

		$("li[class='active']").removeAttr("class");
		//$("#getBase2").attr("class", "active"); 
		$("#getBase ul li:first-child").attr("class", "active");
		$("#ContentBox").attr("style", "margin-left: 80px");

		$("section.leftMenu").hide();
		$("#getBase").show();

		$("#hideindex").show();

		$("#location").text("技术资料 -> ");
		$("#location2").text($("#getBase ul li:first-child").text());
	}

	function getMsg() {
		$("li[class='active']").removeAttr("class");
		//$("#getBase2").attr("class", "active"); 
		$("#getMsg ul li:first-child").attr("class", "active");
		$("#ContentBox").attr("style", "margin-left: 80px");

		$("section.leftMenu").hide();
		$("#getMsg").show();

		$("#hideindex").show();

		$("#location").text("我的消息 -> ");
		$("#location2").text($("#getMsg ul li:first-child").text());
	}

	function getHJbt() {
		$("li[class='active']").removeAttr("class");
		$("#ContentBox").attr("style", "margin-left: 10px");
		$("section.leftMenu").hide();
		$("#location").text("核基本图");
		$("#location2").text("");
	}

	function getEditJbt() {
		$("li[class='active']").removeAttr("class");
		$("#ContentBox").attr("style", "margin-left: 10px");
		$("section.leftMenu").hide();
		$("#location").text("改基本图");
		$("#location2").text("");
	}

	function getSReport() {
		$("li[class='active']").removeAttr("class");
		$("#ContentBox").attr("style", "margin-left: 10px");
		$("section.leftMenu").hide();
		$("#location").text("统计报表");
		$("#location2").text("");
	}

	function hideAll() {
		$("#ContentBox").attr("style", "margin-left: 0px");
		$("section.leftMenu").hide();
	}

	$(function() {
		$(".Navigation a.menu_one").click(function() {
			//$("li[class='active']").removeAttr("class");
			$(".Navigation a.menu_one").removeClass("active");
			$(this).addClass("active");
		});

		$(".leftMenu a").click(function() {
			var locationName = $(this).text();
			$("#location2").html(locationName);
			//console.log("hold on");
			//console.log(locationName);
		});

		//$(".Navigation a.menu_one:first").click();

		$(".nav li").click(function() {
			$("li[class='active']").removeAttr("class");
			$(this).addClass("active");
		});
	});

	/*   $(".nav li").click(function(){
	   $(this).addClass("cur").siblings().removeClass("cur")
	}); */
</script>
<style type="text/css">
.leftMenu .nav-pills>li>a {
	padding: 10px 9px;
}
</style>
</head>
<body>
	<div class="header">
		<div class="row">
			<div class="pull-left logo_name">
				<img src="${ctx}/assets/img/login-logo.png" height="28">
			</div>
			<div class="pull-left">

				<nav class="Navigation">
					<ul>
						<li>
						    <a class="menu_one"  href="${ctx}/msg" target="contentFrame" onclick="getMsg()">
						    	<i class="fa fa-envelope-o"></i>我的消息 <span class="badge" id="msgCount">${msgCount }</span>
						    </a>   
						</li>
						<li>
							<a class="menu_one" href="/trainplan/cross/plan?type=jykx" target="contentFrame" onclick="getcheckPlan()">
								<i class="fa fa-desktop"></i>综合查询
							</a>
						</li>
						<shiro:authenticated>
							<shiro:hasPermission name="JHPT.KYJH.LJ.KD">
								<%@ include file="/assets/template/parent/JHPT.KYJH.LJ.KD.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.LJ.GTJHD">
								<%@ include file="/assets/template/parent/JHPT.KYJH.LJ.GTJHD.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.LJ.DCD">
								<%@ include file="/assets/template/parent/JHPT.KYJH.LJ.DCD.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.LJ.JJS">
								<%@ include file="/assets/template/parent/JHPT.KYJH.LJ.JJS.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.ZD.DCS">
								<%@ include file="/assets/template/parent/JHPT.KYJH.ZD.DCS.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.ZD.DCKCD">
								<%@ include file="/assets/template/parent/JHPT.KYJH.ZD.DCKCD.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.ZD.CLD">
								<%@ include file="/assets/template/parent/JHPT.KYJH.ZD.CLD.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.ZD.KYD">
								<%@ include file="/assets/template/parent/JHPT.KYJH.ZD.KYD.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.ZD.JWD">
								<%@ include file="/assets/template/parent/JHPT.KYJH.ZD.JWD.vm"%>
							</shiro:hasPermission>
							<shiro:hasPermission name="JHPT.KYJH.ZGS.KTZ">
								<%@ include file="/assets/template/parent/JHPT.KYJH.ZGS.KTZ.vm"%>
							</shiro:hasPermission>
						</shiro:authenticated>
					</ul>
				</nav>

			</div>
			<div class=" pull-right">
				<div class="pull-right" style="margin-top: 10px;">
					<!-- 已登录用户 -->
					<shiro:authenticated>
						<jsp:include page="/assets/commonpage/include-dwr-script.jsp"
							flush="true" />
						<button type="button" class="btn btn-success dropdown-toggle"
							data-toggle="dropdown">
							<i class="fa fa-user"></i>
							<shiro:principal />
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li onclick="passwordfix()" data-toggle="modal"
								data-target="#myModal" class="pointer"><a><i
									class="fa fa-cog"></i>修改密码</a></li>
							<%--   <li><a href="${ctx}/user/switch"><i class="fa fa-user"></i>切换用户</a></li> --%>
							<li class="divider"></li>
							<li class="pointer"><a href="${ctx}/user/switch"><i
									class="fa fa-sign-out"></i> 退 出</a></li>
						</ul>
					</shiro:authenticated>

					<!-- 未登录用户 -->
					<shiro:notAuthenticated>
						<button class="btn btn-success" type="button" id="indexLoginBtn"
							style="padding: 6px 12px; border-radius: 4px; width: 100px; margin-left: 170px;">登&nbsp;录</button>
					</shiro:notAuthenticated>
				</div>
				<!--btn-group-->
			</div>
			<!--col-md-6-->
		</div>
		<!--row-->
	</div>
	<!--header-->
	<div class="content">
	    <div style='height: 15px;'></div>
		<!-- 
		<ol class="breadcrumb" id="hideindex">
			<span><i class="fa fa-anchor"></i>当前位置：</span>
			<li><a href="#" id="location">综合查询</a><span id="location2"></span></li>

		</ol>
 		-->
		<section class="leftMenu" style="display: none" id="getcheckPlan">
			<ul class="nav nav-pills nav-stacked" role="tablist">
				<li class="active" id="getcheckPlan2"><a style="width: 100%;"
					href="${ctx}/cross/plan?type=jykx" target="contentFrame">普速开行</a></li>
				<li><a style="width: 100%;"
					href="${ctx}/cross/plan?type=examine" target="contentFrame">高铁开行</a></li>
		        <!-- 2015-6-18 16:03:48
				<li><a style="width: 100%;" href="${ctx}/default/transfer/planReviewLines?train_type=0" target="contentFrame">普速1条线</a></li>
				<li><a style="width: 100%;" href="${ctx}/default/transfer/planReviewLines?train_type=1" target="contentFrame">高铁1条线</a></li>
				 -->
				<li><a style="width: 100%;"
					href="/trainplan/highLine/vehicleSearch" target="contentFrame">高铁车底</a></li>
				<li><a style="width: 100%;"
					href="${ctx}/crew/page/jyQuery?crewType=cz" target="contentFrame">普速乘务</a></li>
				<li><a style="width: 100%;"
					href="${ctx}/crew/page/all?crewType=cz" target="contentFrame">高铁乘务</a></li>
			</ul>
		</section>

		<shiro:authenticated>
			<shiro:hasPermission name="JHPT.KYJH.LJ.KD">
				<%@ include file="/assets/template/child/JHPT.KYJH.LJ.KD.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.LJ.GTJHD">
				<%@ include file="/assets/template/child/JHPT.KYJH.LJ.GTJHD.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.LJ.DCD">
				<%@ include file="/assets/template/child/JHPT.KYJH.LJ.DCD.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.LJ.JJS">
				<%@ include file="/assets/template/child/JHPT.KYJH.LJ.JJS.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.ZD.DCS">
				<%@ include file="/assets/template/child/JHPT.KYJH.ZD.DCS.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.ZD.DCKCD">
				<%@ include file="/assets/template/child/JHPT.KYJH.ZD.DCKCD.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.ZD.CLD">
				<%@ include file="/assets/template/child/JHPT.KYJH.ZD.CLD.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.ZD.KYD">
				<%@ include file="/assets/template/child/JHPT.KYJH.ZD.KYD.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.ZD.JWD">
				<%@ include file="/assets/template/child/JHPT.KYJH.ZD.JWD.vm"%>
			</shiro:hasPermission>
			<shiro:hasPermission name="JHPT.KYJH.ZGS.KTZ">
				<%@ include file="/assets/template/child/JHPT.KYJH.ZGS.KTZ.vm"%>
			</shiro:hasPermission>
		</shiro:authenticated>

		<!--左侧菜单-->

		<!--Iframe嵌入页面-->
		<div class="iframebox" style="margin-left: 80px; height: auto;"
			id="ContentBox">
			<iframe id="contentLayerFrame" src="" frameborder=0
				name="contentFrame"
				style="width: 100%; height: auto; overflow-x: hidden;"> </iframe>
		</div>
		<!--嵌入页面end-->

		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog"
				style="margin-top: 10%; width: 640px; padding: 10px 0;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">×</button>
						<h4 class="modal-title">修改密码</h4>
					</div>
					<div class="panel-body row" style="padding: 15px 0;">

						<div id="passwordinner" class="row">
							<div class="form-group row">
								<label for="inputUsername"
									class="col-sm-4 control-label text-right">用户名：</label>
								<div class="pull-left col-md-5">
									<input type="text" tabindex="1" class="form-control"
										id="username1" readonly="readonly">
								</div>
							</div>

							<div class="form-group row">
								<label for="inputUsername"
									class="col-sm-4 control-label text-right"">岗位：</label>
								<div class="pull-left col-md-5">
									<input type="text" tabindex="1" class="form-control"
										id="deptname" readonly="readonly">
								</div>
							</div>

							<div class="form-group row">
								<label for="inputUsername"
									class="col-sm-4 control-label text-right">原密码<span
									class="red" style="margin-left: 4px;">*</span>：
								</label>
								<div class="pull-left col-md-5">
									<input type="password" tabindex="1" class="form-control"
										id="oldpass">
								</div>
								<p class="red margin display" id="tipOldpassword"
									style="margin-top: 5px;">与原密码不相符！</p>
							</div>
							<div class="form-group row">
								<label for="inputUsername"
									class="col-sm-4 control-label text-right"">新密码<span
									class="red" style="margin-left: 4px;">*</span>：
								</label>
								<div class="pull-left col-md-5">
									<input type="password" tabindex="1" class="form-control"
										id="newpass">
								</div>
								<p class="red margin display" id="tipChange"
									style="margin-top: 5px;">新密码不能与原密码相同！</p>
							</div>
							<div class="form-group row">
								<label for="inputUsername"
									class="col-sm-4 control-label text-right">确认新密码<span
									class="red" style="margin-left: 4px;">*</span>：
								</label>
								<div class="pull-left col-md-5">
									<input type="password" tabindex="1" class="form-control"
										id="newpassagain">
								</div>
								<p class="red margin display" id="tipNewpassagain"
									style="margin-top: 5px;">两次新密码不相同！</p>
							</div>

						</div>

						<div id="changed"
							style="text-align: center; font-size: 18px; margin: 20px 0;"
							class="blue display">修改密码成功！</div>




					</div>
					<!--panel-heading-->
					<div style="padding-left: 6%;">
						<!--panel-body-->
						<div class="modal-footer margin">
							<button type="button" class="btn btn-primary"
								onclick="submitpass()">确定</button>
							<button type="button" class="btn btn-warning"
								data-dismiss="modal">取消</button>
						</div>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>


		<div id="passwo0" title="修改密码" style="display: none">

			<div id="passwordinner" class="row"
				style="margin: 5px; overflow: auto;">
				<div class="form-group paddingtop30">
					<label for="inputUsername" class="col-sm-2 control-label">用户名</label>
					<div class="pull-left">
						<input type="text" tabindex="1" class="form-control"
							id="username1" readonly="readonly">
					</div>
				</div>



				<div class="form-group paddingtop30">
					<label for="inputUsername" class="col-sm-2 control-label">岗位</label>
					<div class="pull-left">
						<input type="text" tabindex="1" class="form-control" id="deptname"
							readonly="readonly">
					</div>
				</div>

				<div class="form-group paddingtop30">
					<label for="inputUsername" class="col-sm-2 control-label">原密码</label>
					<div class="pull-left">
						<input type="password" tabindex="1" class="form-control"
							id="oldpass">
					</div>
				</div>
				<div class="form-group paddingtop30">
					<label for="inputUsername" class="col-sm-2 control-label">新密码</label>
					<div class="pull-left">
						<input type="password" tabindex="1" class="form-control"
							id="newpass">
					</div>
				</div>
				<div class="form-group paddingtop30">
					<label for="inputUsername" class="col-sm-2 control-label">确认新密码</label>
					<div class="pull-left">
						<input type="password" tabindex="1" class="form-control"
							id="newpassagain">
					</div>
				</div>


				<button type="button" class="btn btn-success dropdown-toggle"
					data-toggle="dropdown" onclick="">
					<i class="fa fa-user"></i> 取消 <span class="caret"></span>
				</button>


				<button type="button" class="btn btn-success dropdown-toggle"
					data-toggle="dropdown" onclick="submitpass()">
					<i class="fa fa-user"></i> 确认 <span class="caret"></span>
				</button>


			</div>





		</div>
		<!--content-->
</body>
</html>

