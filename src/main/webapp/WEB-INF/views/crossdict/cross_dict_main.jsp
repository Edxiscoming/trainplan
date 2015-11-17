<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
}
 
String basePath = request.getContextPath(); 
%>
<!DOCTYPE HTML>
<html lang="en">
<head> 
<title>交路字典管理</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />

<script type="text/javascript">
var basePath = "<%=basePath %>";
var all_role = "<%=userRolesString %>"; 
</script>

 
</head>
<body class="Iframe_body"  >
	
	<ol class="breadcrumb">
		<span><i class="fa fa-anchor"></i>当前位置:</span>
		<li><a href="#">交路字典管理</a></li>
	</ol>
	
	
	<!--分栏框开始-->
	<div class="pull-left" style="width: 30%;">
    
	<!--分栏框开始-->  
		<div class="row" style="margin: 0px 5px 5px 5px;">  
		<!-- <select id="selectTest" class="easyui-combobox" name="organs"  style="width:200px;" multiple="multiple" valueField="id" textField="text" ></select> -->
				<div class="form-group"
						style="float: left; margin-left: 0px; margin-top: 0px;width: 100%"> 
					  <div class="row" style="width: 100%;" >
							<label for="exampleInputEmail3" class="control-label pull-left">
											方&nbsp;&nbsp;&nbsp;&nbsp;案:&nbsp;</label> 
							<div class="pull-left">
								<select style="width: 320px" id="input_cross_chart_id"
									class="form-control" data-bind="options: crossDictSearchModle().schemeCombox, value: crossDictSearchModle().scheme, optionsText: 'text'">
								</select>
							</div> 
					   </div>  
						<div class="row"  style="margin-top: 5px;">
							<label for="exampleInputEmail3" class="control-label pull-left" >
								担当局:</label>
							<div class="pull-left" style="margin-left: 5px;">
								<select class="form-control" data-bind="options: crossDictSearchModle().tokenVehBureauCombox, value: crossDictSearchModle().tokenVehBureau, optionsText: 'text'"></select>
							</div>
						    <label for="exampleInputEmail3" class="control-label pull-left">
								交路名:&nbsp;</label>
							<div class="pull-left">
								<input type="text" class="form-control" style="width: 95px;" data-bind=" value: crossDictSearchModle().crossName">
							</div> 
								<a type="button" class="btn btn-success" data-toggle="modal" style="margin-left: 30px;"
										data-target="#" id="btn_cross_search"  data-bind="click: queryCrossDictList">查询</a>   
							
						</div>  
					 </div> 
				</div>
				<div class="row" >
				  <div class="panel panel-default"> 
					<div class="table-responsive">
						<table class="table table-bordered table-striped table-hover" style="margin-left:5px; margin-right:5px; width:98%"
								id="cross_table_crossInfo">
								<thead>
									<tr style="height: 25px"> 
										<th style="width: 45px" align="center">序号</th>
										<th style="width: 50px" align="center">局</th>
										<th style="width: 400%;" align="center">  
											    <label for="exampleInputEmail5" style="font-weight: bold;vertical-align: bottom;">交路名</label> 
												<select class="form-control" style="width: 75px;display:inline-block;"
													 data-bind="options: [{'code': 1, 'text': '简称'},{'code': 0, 'text': '全称'}], value: crossDictSearchModle().isShortName, optionsText: 'text', optionsValue: 'code'">
												</select>  
										</th>
										<th style="width: 80px" align="center" colspan="2">字典</th> 
									</tr>
								</thead>
								<tbody data-bind="foreach: crossDictRows.rows">
									<tr>
										<td data-bind=" text: ($index() + 1)"></td>
										<td align="center" data-bind=" text: tokenVehBureauShowValue"></td>
										<td data-bind="text: $parent.crossDictSearchModle().isShortName() == 1 ? shortName : crossName, attr:{title: crossName()}"></td>
										<td align="center" data-bind="style:{color:isDict() == 1 ? 'green' : ''}, text: isDict() == 1 ? '已' : '未' "></td>
									</tr> 
								</tbody> 
							</table>
						<!-- <div data-bind="template: { name: 'tablefooter-short-template', foreach: crossDictRows }" style="margin-bottom: 5px"></div>
						 -->
					</div>
				</div>
			</div>  
		</div> 
	
	
<script src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script src="<%=basePath %>/assets/js/trainplan/crossdict/cross_dict.js"></script> 
</body>
</html>
