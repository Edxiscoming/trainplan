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
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/assets/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/assets/bootstrap/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/assets/css/logTable/mainCross.css">
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=basePath %>/assets/js/logTable/homepage.js"></script>
<script type="text/javascript"
	src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
<script type="text/javascript"
	src="<%=basePath %>/assets/js/logTable/common.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/assets/js/bootstrap.min.js"></script>
<title>基本数据首页</title>
<style type="text/css">
.nav-pills>li {
	color: #333;
	background-color: #cccccc;
}

.nav-pills>li.active>a,.nav-pills>li.active>a:hover,.nav-pills>li.active>a:focus
	{
	color: #fff;
	background-color: #666666;
	font-weight: bold;
}

th,td {
	text-align: center;
}

.nav-pills>li>a {
	color: #fff;
}

#back {
	width: 50px;
	height: 30px;
	margin-top: 5px;
	margin-right: 15px;
}

.margin-top-5 {
	margin-top: 5px;
}

.nav-pills>li>a {
	border-radius: 0px;
}
#bootrapTable td {
	border: 1px solid #ccc;
}
#bootrapTable table {
	border-collapse: collapse;
	border-spacing: 0;
}
#bootrapTable th {
	border: 1px solid #ccc;
}
#bootrapTable tr {
	height: 30px;
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
			<div class="col-md-12" style="padding: 15px 0px;width: 1200px;">
				<span class="pull-left" style="margin-top:7px; margin-left:15px;">方案：</span>
				<div class="pull-left">
					<select
						style="padding: 1px 8px;; width: 250px; color:#e60302; font-weight:normal;"
						id="cmVersionId" class="form-control input-sm">
						<option value="">20150520__0519导入</option>
					</select>
				</div>
				<a type="button" class="btn btn-default blueBtn btn-sm pull-right"
					style="margin-left: 20px;" onclick="searchData();"><span
					class="glyphicon glyphicon-search" style="margin-right:5px;"></span>查询</a>
				<span class="pull-left padding-5" style="margin-left: 400px; margin-top:7px;">车次：</span>
				<input type="text" class="input-sm form-control pull-left" id="trainNo" style="width: 100px;" onkeyup="upperCase(this.id)">
				 <span	class="pull-right" style="margin-left：50px; margin-top:7px;" hidden="hidden">模糊</span>
				<input type="checkbox" id="selfRelevant" class="pull-right" style="margin-left:150px; margin-top:11px;" hidden="hidden">
			</div>
		<div class="pull-left col-md-5" style="height:780px; padding-left:0px;">
			<div class="col-md-12" style="margin-left:-15px;height:200px;width:100%;display:none; ">
				<table id="crossInfos">

				</table>
			</div>
			<div class="col-xs-12" id="bootrapTable" style="width:100%;">
				<table style="width:100%; border:1px solid #ccc; " id="sumtable">
				   <thead>
				     <tr>
				       <th rowspan="2">局别</th>
				       <th rowspan="2">合计</th>
				       <th colspan="2">直通</th>
				       <th colspan="2">管内</th>
				     </tr>
				     <tr>
				     	<th>小计</th>
				     	<th>已生成</th>
				     	<th>小计</th>
				     	<th>已生成</th>
				     </tr>
				   </thead>
				 </table>

			</div>
		</div>
		<div class="pull-right col-md-7" style="padding-right:0px;;padding-top:0px;overflow-y:auto;height:900px;">
			<div class="col-xs-12" style="height:400px; width:100%; border:1px solid #ccc;">
				<table id="crossInfos2">

				</table>

			</div>
			<div class="row">
			<div class="col-xs-12" style="padding-top: 10px;padding-bottom: 10px;">
			<span>交路：<span id="cname"></span></span>
			</div>
			<div class="col-xs-12">
							<ul id="myTab" class="nav nav-pills" role="tablist"
					style="background-color:#f5f5f5;">
					<li class="active"><a href="#crossGraph" role="tab"data-toggle="pill">交路图</a></li>
					<li><a href="#crossInfo" role="tab" data-toggle="pill">交路属性</a></li>
					<li><a href="#bulletin" role="tab" data-toggle="pill" id="Tbulletin">列车属性</a></li>
				</ul>
				<!-- 选项卡面板 -->
				<div id="myTabContent" class="tab-content"
					style="height:290px; overflow-y:auto; overflow-x:hidden;">
					<div class="tab-pane fade in active" id="crossGraph"style="height:99%;">
						<iframe src="" width="100%" height="100%" scroll="no"></iframe>
					</div>
					<div class="tab-pane fade" id="crossInfo">
						<form role="form" class="form-horizontal">
							<div
								style="margin-left:30px; border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
								<div class="row margin-top-5">
									<div class=" pull-left div-header">车底交路：</div>
									<div class="pull-left" id="plan_construction_input_trainNbr">

									</div>
								</div>

								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:14px;">
										担当局：</div>
									<div class="pull-left" id="undertake"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										组数：</div>
									<div class="pull-left" id="groupData"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">开行状态：</div>
									<div class="pull-left" id="openState"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										类型：</div>
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
							<div style="margin-left:30px;">
								<label style="" class="margin-top-5">开行规律</label>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">普通规律：</div>
									<div class="pull-left" id="normalRule"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">指定星期：</div>
									<div class="pull-left" id="week"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">指定日期：</div>
									<div class="pull-left" id="date"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">指定周期：</div>
									<div class="pull-left" id="cycle"></div>
								</div>
								<div class="clearfix"></div>
							</div>
							<div
								style="margin-left:30px; border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none;">
								<div class="row margin-top-5">
									<div class=" pull-left div-header">运行区段：</div>
									<div class="pull-left" id="runSection"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">运行距离：</div>
									<div class="pull-left" id="runDistance"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										等级：</div>
									<div class="pull-left" id="level"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										对数：</div>
									<div class="pull-left" id="log"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">编组辆数：</div>
									<div class="pull-left" id="orgnize"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										定员：</div>
									<div class="pull-left" id="createPeople"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header">编组内容：</div>
									<div class="pull-left" id="orgnizeContent"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:-14px;">
										动车组类型：</div>
									<div class="pull-left" id="groupLevel"></div>
								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										供电：</div>
									<div class="pull-left" id="electricity"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										集便：</div>
									<div class="pull-left" id="mass"></div>

								</div>
								<div class="row margin-top-5">
									<div class=" pull-left div-header" style="margin-left:28px;">
										空调：</div>
									<div class="pull-left" id="airconditioner"></div>
								</div>
								<div class="clearfix"></div>
							</div>
							<div>
								<div class="row margin-top-5" style="margin-left:15px;">
									<div class="div-header pull-left" style="margin-left:28px;">
										备注：</div>
									<div class="pull-left" id="remark"
										style="margin-bottom: 5px; width:270px; height:50px;">
										</div>
								</div>
							</div>
						</form>
					</div>
						<div class="tab-pane fade" id="bulletin" style="height: 100%;">
									<table id="cross_trainInfo">
									</table>
						</div>
					</div>
			</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="../js/jquery.min.js"></script>
	<script type="text/javascript" src="../js/jquery.bootstrap.js"></script>
	<script type="text/javascript" src="../js/bootstrap.min.js"></script>
</body>
</html>