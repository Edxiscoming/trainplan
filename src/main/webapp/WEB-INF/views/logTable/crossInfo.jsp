<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List"%>
<%
	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
	List<String> permissionList = user.getPermissionList();
	String userRolesString = "";
	for(String p : permissionList){
		userRolesString += userRolesString.equals("") ? p : "," + p;
	} 
	String basePath = request.getContextPath();
	System.out.println(basePath);
	String  currentBureauShortName = user.getBureauShortName();
	String currentUserBureau = user.getBureau();
%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>交路单元管理</title>
<!-- Bootstrap core CSS -->

<!--font-awesome-->
<link href="<%=basePath%>/assets/css/datepicker.css" rel="stylesheet">
<link href="<%=basePath%>/assets/easyui/themes/icon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath%>/assets/css/font-awesome.min.css" />
<link href="<%=basePath%>/assets/css/style.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/bootstrap/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/logTable/importCrossData.css">
<!--     <link href="../style/cross.css" rel="stylesheet"> -->
<!-- Custom styles for this template -->

<script type="text/javascript" src="<%=basePath%>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/html5.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/trainplan/common.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/jquery.bootstrap.js"></script>
<script src="<%=basePath%>/assets/js/logTable/crossInfo.js"></script>
<script src="<%=basePath%>/assets/js/logTable/timeTable.js"></script>
<style type="text/css">
.nav-pills>li {
	color: #333;
	background-color: #cccccc;
}

.nav-pills>li.active>a, .nav-pills>li.active>a:hover, .nav-pills>li.active>a:focus
	{
	color: #fff;
	background-color: #666666;
	font-weight: bold;
}

th, td {
	text-align: center;
}

.nav-pills>li>a {
	color: #fff;
}

.slideblock {
	width: 0px;
}

.left {
	width: 100%;
}

#back {
	width: 50px;
	height: 30px;
	margin-top: 5px;
	margin-right: 15px;
}
</style>
<script type="text/javascript">
var basePath = "<%=basePath%>";
var all_role = "<%=userRolesString%>";
var currentBureauShortName = "<%=currentBureauShortName%>";
var currentUserBureau = "<%=currentUserBureau %>";
</script>
</head>
<body style="width: 100%; height:1080px; overflow-y:hidden;">
	<div class="container-fluid">
		<div class="row"
			style="padding: 15px 0px;background: #f9f9f9; border-bottom: solid 1px #E0E0E0;">
			<div class="col-xs-8">
				<span class="pull-left padding-5" style="margin-left: 10px;">方案：</span>
				<select class="form-control input-sm pull-left"
					style="width: 280px;" id="cmVersionId">
				</select> <span class="pull-left padding-5" style="margin-left: 30px;">车次：</span><input
					type="text" id="trainNbr" class="input-sm form-control pull-left"
					style="width: 200px;"> <a type="button"
					class="btn btn-default greenBtn btn-sm pull-left"
					style="margin-left: 20px; line-height:2.0;"
					onclick="loadCrosses();">查询</a>
			</div>
		</div>
		<div class="row" style="margin-top: 15px;">
			<input type="text" id="cmUnitCrossId" style="display:none"/>
			<input type="text" id="cmCrossName" style="display:none"/>
            <div class="pull-left left" style="height:800px;margin-left: 30px;float:left;">
                <table class="easyui-datagrid" id="crossInfos">
                </table>
            </div>
            <div class="pull-right slideblock " style="height: 800px;width:50%;overflow-y: auto;margin-right: 15px;float:right;display:none;">
				<div class="row">
					<div class="pull-left col-xs-12"
						style="margin-top:20px; overflow-y:auto;">
						<section class="panel panel-default">
							<!-- 选项卡组件（菜单项nav-pills）-->
							<ul id="myTab" class="nav nav-pills" role="tablist">
								<li class="active"><a href="#crossData" role="tab"
									data-toggle="pill">交路信息</a></li>
								<li><a href="#bulletin" role="tab" data-toggle="pill" id="crossInfoTab">列车信息（对）</a></li>
								<li><a href="#rule" role="tab" data-toggle="pill" id="showcrosstrainTab">列车信息（交）</a></li>
								<li style="background-color:#fff;"><a type="button"
									style="margin-left: 15px; margin-top: 0px; border-radius:15px; padding-left:15px;"
									class="btn btn-primary blueBtn btn-sm pull-left"
									data-toggle="modal" data-target="#" id=""
									onclick="openGraph();"> 交路图</a></li>
								<li style="background-color:#fff;"><a type="button"
									style="margin-left: 5px; margin-top: 0px; border-radius:15px; padding-left:15px;"
									class="btn btn-primary blueBtn btn-sm pull-left"
									data-toggle="modal" onclick="timeOpen();"> 时刻表 </a></li>
								<a id="back" type="button"
									class="btn btn-default greenBtn btn-sm pull-right closeBlock"><span
									class="glyphicon glyphicon-arrow-right" aria-hidden="true" onclick="closeSlideBlock();">关闭</span></a>
							</ul>
							<!-- 选项卡面板 -->
							<div id="myTabContent" class="tab-content" >
								<div class="tab-pane fade in active" id="crossData">
									<div class="container-fluid" style="border: solid 1px #E0E0E0;">
										<form data-bind="with: currentCross" role="form"
											class="form-horizontal">
											<div
												style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
												<div class="row margin-top-5">
													<div class=" pull-left div-header">车底交路：</div>
													<div class="pull-left"
														id="plan_construction_input_trainNbr"></div>
												</div>

												<div class="row margin-top-5">
													<div class=" pull-left div-header">担当局：</div>
													<div class="pull-left" id="undertake"></div>

												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">组数：</div>
													<div class="pull-left" id="groupData"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">开行状态：</div>
													<div class="pull-left" id="openState"></div>

												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">类型：</div>
													<div class="pull-left" id="mold"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">交替日期：</div>
													<div class="pull-left" id="relaceDate"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">交替车次：</div>
													<div class="pull-left" id="replace_train_number"></div>
												</div>
												<div class="clearfix"></div>
											</div>
											<div style="">
												<label style="" class="margin-top-5">开行规律</label>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">普通规律:</div>
													<div class="pull-left" id="normalRule"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">指定星期:</div>
													<div class="pull-left" id="week"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">指定日期:</div>
													<div class="pull-left" id="date"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">指定周期:</div>
													<div class="pull-left" id="cycle"></div>
												</div>
												<div class="clearfix"></div>
											</div>
											<div
												style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none;">
												<div class="row margin-top-5">
													<div class=" pull-left div-header">运行区段：</div>
													<div class="pull-left" id="runSection"></div>

												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">运行距离：</div>
													<div class="pull-left" id="runDistance"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">等级：</div>
													<div class="pull-left" id="level"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">对数：</div>
													<div class="pull-left" id="log"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">编组辆数：</div>
													<div class="pull-left" id="orgnize"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">定员：</div>
													<div class="pull-left" id="createPeople"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">编组内容：</div>
													<div class="pull-left" id="orgnizeContent"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">动车组类型：</div>
													<div class="pull-left" id="groupLevel"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">供电：</div>
													<div class="pull-left" id="electricity"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">集便：</div>
													<div class="pull-left" id="mass"></div>
												</div>
												<div class="row margin-top-5">
													<div class=" pull-left div-header">空调：</div>
													<div class="pull-left" id="airconditioner"></div>
												</div>
												<div class="clearfix"></div>
											</div>
											<div>
												<div class="row">
													<div class="div-header pull-left">备注：</div>
													<div class="pull-left" id="remark"
														style="margin-bottom: 5px; width:270px; height:50px;">
													</div>
												</div>
												<div class="row" style="width:100%; height:50px;"></div>
											</div>

										</form>
									</div>
								</div>
								<div class="tab-pane fade" id="bulletin" >
									<div class="panel-body" style="height:700px;width:900px;">
									<input type="text" id="cmCrossId_unit" style="display:none"/>
										<table class="" id="cross_trainInfo">
										 
										 </table>
									</div>
								</div>
								<div class="tab-pane fade" id="rule">
									<div class="panel-body" style="height:700px;width:900px;">
									<input type="text" id="cmCrossId_cross" style="display:none"/>
										<table class="table table-bordered table-striped table-hover" id="unti_trainInfo">
											
										</table>
									</div>
								</div>
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="crossGraph" title="运行图" >
			<input id="parentParamIsShowJt" type="hidden" value="1">
			<input id="parentParamIsShowTrainTime" type="hidden" value="0">
		<iframe src="" width="100%" height="100%" scroll="no"></iframe>
	</div>

	<div id="timeTable" style="display:none;">
		<div style="margin:5px; overflow-y:auto;">
			<ul id="myTab" class="nav nav-tabs" role="tablist">
				<li class="active"><a href="#jian" role="tab" data-toggle="tab" id="jianTab">简点</a></li>
				<li><a href="#xiang" role="tab" data-toggle="tab" id="xiangTab">详点</a></li>
			</ul>
			<!-- 选项卡面板 -->
			<div id="myTabContent" class="tab-content" >
				<div class="tab-pane fade in active" id="jian">
					<div class="panel-body" style="height:700px; width:100%;">
					<table class="" id="timedetailinfo">
					
					</table>
					</div>
				</div>
				<div class="tab-pane fade" id="xiang">
					<div class="panel-body" style="height:700px; width:100%;">
						<table class="" id="timedetailinfo_detail">
							
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
