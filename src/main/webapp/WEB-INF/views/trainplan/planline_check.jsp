<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
String  currentUserBureauName = user.getBureauShortName();
String basePath = request.getContextPath();
String currentUserBureau = user.getBureau();
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>审核次日客车运行线</title>
    <link type="text/css" href="${ctx}/assets/css/datepicker.css" rel="stylesheet">
    <link type="text/css" href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/font-awesome.min.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/minified/jquery-ui.min.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/style.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/easyui/themes/default/easyui.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/easyui/themes/icon.css" rel="stylesheet" />
    
    <script type="text/javascript">
	var currentUserBureau = "<%=currentUserBureau %>";
	var currentUserBureauName ="<%=currentUserBureauName %>";
	</script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath %>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
<script src="<%=basePath %>/assets/lib/fishcomponent.js"></script>
<%-- <script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script> --%>
<script src="<%=basePath %>/assets/js/trainplan/util/fishcomponent.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.util.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/util/canvas.component.js"></script> 
<script src="<%=basePath %>/assets/js/trainplan/runPlan/canvas_rightmenu.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/planline_check.js"></script>
    <script type="text/javascript">
var basePath = "<%=basePath %>";
var currentUserBureau = "<%=currentUserBureau %>";
</script>
    <script type="text/javascript">
        var isHighLine = <c:out value="${train_type}"></c:out>
    </script>
    
    <style type="text/css">
    .table thead > tr > th{
    padding: 0px 5px;
    }
    </style>
</head>
<body class="Iframe_body">
<%-- <ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置:</span>
    <c:if test="${train_type==0}">
        <li><a href="#">发布开行计划 -> 审核既有开行计划</a></li>
    </c:if>
    <c:if test="${train_type==1}">
        <li><a href="#">发布开行计划 -> 审核高铁开行计划</a></li>
    </c:if>
</ol> --%>
<section class="mainContent">
    <div class="row">
        <div class="col-xs-12 col-md-12 col-lg-12">
            <div class="row"  >
                <div class="col-xs-12 col-md-12 col-lg-12 paddingtop5 marginbottom5">
                    <form class="form-inline" role="form">
                        <div class="input-group" style="width: 100%;margin-left: -17px;margin-top: -12px;">
                        <label class="control-label paddingleftright5">路局:</label>
                        <div class="pull-left">
                            <select style="width: 70px; margin-right: 10px; border-radius: 4px" class="form-control" data-bind="options:searchModle().bureaus, value: searchModle().bureau, optionsText: 'shortName', optionsValue:'code', optionsCaption: searchModle().bureau " disabled>
                            </select>
                         </div>
                         <label class="control-label paddingleftright5" style="margin-left:5px;">日期:</label>
                          <div class="pull-left">
                           <input type="text" class="form-control" style="width: 100px; margin-right: 10px; border-radius: 4px" placeholder="请选择日期" id="date_selector"/>                         
                          </div>  

                            <label class="control-label paddingleftright5">车次:</label>
                             <div class="pull-left">
                            <input type="text" class="form-control" style="width: 100px; margin-right: 10px; border-radius: 4px" placeholder="输入车次" id="train_nbr">
                            </div> 

                            <label class="control-label paddingleftright5">运行方式:</label>
                            <div class="pull-left">
                            <select id="train_type" class="form-control" style="width: 110px; margin-right: 10px; border-radius: 4px">
                                <option value="0">全部</option>
                                <option value="1">始发终到</option>
                                <option value="2">始发交出</option>
                                <option value="3">接入终到</option>
                                <option value="4">接入交出</option>
                            </select>
                            </div> 
                            
							<label class="control-label paddingleftright5">担当局:</label>
                            <div class="pull-left">
                            <select id="token_veh_bureau_nbr" class="form-control" style="width: 110px; margin-right: 10px; border-radius: 4px">
                                <option value="0">全部</option>
                                <option value="1">本局</option>
                                <option value="2">外局</option>
                            </select>
                            </div>
                            <input type="hidden" id="sentFlag" value='0'/> 
                            <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: search"><i class="fa fa-search"></i>查询</button>
                            <!-- <input type="hidden" id="autoCheckall" data-bind="click: autoCheck"></input> -->
                            <input type="hidden" id="check_lev2_type" value="3">
                            <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: autoCheck"><i class="fa fa-retweet"></i>校验</button>
                            <!-- <button type="button" class="btn btn-primary btn-input" style=" margin-right: 10px; border-radius: 4px" data-bind="click: cbPlan"><i class="fa fa-retweet"></i>差别计划</button> -->
<!--                             <button type="button" class="btn btn-primary  btn-input" style="margin-right: 10px; border-radius: 4px" data-bind="click: autoCheck"><i class="fa fa-retweet"></i>校验</button> -->

                           <%--  <shiro:hasPermission name="JHPT.RJH.KDSP"><!-- 客运调度审批 -->
                            <button type="button" class="btn btn-primary" style="width: 100px; margin-right: 10px; border-radius: 4px" data-bind="click: checkLev1, enable: canCheckLev1"><i class="fa fa-eye"></i>审核</button>
                            </shiro:hasPermission>

                            <shiro:hasPermission name="JHPT.RJH.ZBZRSP"><!-- 值班主任审批权限 -->
                            <button type="button" class="btn btn-primary" style="width: 100px; margin-right: 10px; border-radius: 4px" data-bind="click: checkLev2, enable: canCheckLev2">审核</button>
                            </shiro:hasPermission> --%>

                            <label class="control-label text-center pull-right paddingtop5">
                            	<!-- 
						                            存在 <a style="color: #ff0000" href="#" data-bind="text: paramModel().unknownRunLine, click: openOuterTrainLine"></a>
								条冗余运行线<span data-bind="text: checkStatus"></span>
								-->
								重复列车：<a style="color: #ff0000;font-size:14px" href="#" data-bind="text: isTrainCfMatch, click: openTrainCf"></a>
                            </label>
                            <div class="pull-right" style="margin-right:10px;margin-top:5px">
								<input type="checkbox" style="margin: 7px 5px 0 0;" class="pull-left" class="form-control" data-bind="checked: isShowCrossDetailInfo, event:{change: showChatPia}"> 
								<label for="exampleInputEmail5" class="control-label pull-left"><b>显示数据图</b></label>  
						 	</div>
                        </div>

                    </form>
                </div>
            </div>
            <div class="panel-body" id="body_right"
					style="padding: 5px 0 0 0;margin: 0px">
            
            <div class="row">
                <div class="margin panel panel-default" style="padding:5px;">
                    <div class="table-responsive">
                        <table id="plan_table" class="table table-bordered table-striped table-hover tableradius" data-bind="with: tableModel">
                            <thead>
                            <tr>
                                <shiro:hasPermission name="JHPT.RJH.KDSP"><!-- 客运调度审批 -->
<!--                                 <th rowspan="2" class="text-center"><input class="checkbox-inline" type="checkbox" data-bind="checked: $root.allBtn, click: $root.selectAllLev1, enable: $root.canCheckLev1"/></th> -->
                                </shiro:hasPermission>
                                <th  rowspan="2" style="vertical-align: middle;width:45px;">序号</th>
                                <th rowspan="2" style="width:130px;vertical-align: middle">车次</th>
                                <th style="width:;vertical-align: middle" rowspan="2">来源</th>
                                <th style="width:70px;vertical-align: middle" rowspan="2">开行<br>状态</th>
                                <th rowspan="2" style="width:90px;vertical-align: middle">运行方式</th>
                                <th rowspan="2" style="width:150px;vertical-align: middle">始发站</th>
                                <th rowspan="2" style="width:110px; vertical-align: middle">始发时间</th>
                                <th rowspan="2" style="width:150px;vertical-align: middle">接入站</th>
                                <th rowspan="2" style="width:110px;vertical-align: middle">接入时间</th>
                                <th rowspan="2" style="width:150px;vertical-align: middle">交出站</th>
                                <th rowspan="2" style="width:110px;vertical-align: middle">交出时间</th>
                                <th rowspan="2" style="width:150px;vertical-align: middle">终到站</th>
                                <th style="width:110px;vertical-align: middle" rowspan="2">终到时间</th>
                                <th style="width:60px;vertical-align: middle" rowspan="2">1条线</th>
<%--                                 <shiro:hasPermission name="JHPT.RJH.KDSP"> --%>
                                    <th colspan="2" class="text-center">校验项</th>
                                    <th rowspan="2" style="width: 17px" align="center"></th>
<%--                                 </shiro:hasPermission> --%>

                            </tr>
                            <tr>
                                    <th  style="width:90px;" class="text-center">列车</th>
                                    <th  style="width:90px;" class="text-center">时刻</th>
                            </tr>
                            </thead>
                       </table> 
                       <div id="left_height" style="overflow-y:auto;">
                 		 <table class="table table-bordered table-striped table-hover" style="border: 0;"  data-bind="with: tableModel">  
                            <tbody data-bind="foreach: planList">
                            <tr>
                                <shiro:hasPermission name="JHPT.RJH.KDSP"><!-- 客运调度审批 -->
<!--                                 <td class="text-center"><input class="checkbox-inline" name="plan" type="checkbox" data-bind="enable: needLev1, checked: isSelected"/></td> -->
                                </shiro:hasPermission>
                                <td style="width:45px;" class="text-center" data-bind="text: $index() + 1"></td>
                                <td style="width:130px; "class="text-center"><a href="#" data-bind="text: name, click: showGraphic"></a></td>
                                <td style="width:" class="text-center" data-bind="text: sourceType, attr:{title: sourceType}"></td>
                                <td style="width:70px;" class="text-center" data-bind="text: spareFlag, attr:{title: spareFlag}"></td>
                                <td style="width:90px; " class="text-center" data-bind="text: trainType, attr:{title: trainType}"></td>
                                <td style="width:150px;" class="text-center" data-bind="text: startStn, attr:{title: startStn}"></td>
                                <td style="width:110px;" class="text-center" data-bind="text: startTime, attr:{title: startTime}"></td>
                                <td style="width:150px;" class="text-center" data-bind="text: jrStn, attr:{title: jrStn}"></td>
                                <td style="width:110px;" class="text-center" data-bind="text: jrTime, attr:{title: jrTime}"></td>
                                <td style="width:150px;" class="text-center" data-bind="text: jcStn, attr:{title: jcStn}"></td>
                                <td style="width:110px;" class="text-center" data-bind="text: jcTime, attr:{title: jcTime}"></td>
                                <td style="width:150px;" class="text-center" data-bind="text: endStn, attr:{title: endStn}"></td>
                                <td style="width:110px;" class="text-center" data-bind="text: endTime, attr:{title: endTime}"></td>
                                <td style="width:60px;" class="text-center" data-bind="html: dailyLineFlagView"></td>
<!--                                 <td class="text-center" data-bind="html: checkTrain"></td> -->
<!--                                 <td class="text-center" data-bind="html: checkTime"></td> -->
<%--                                 <shiro:hasPermission name="JHPT.RJH.KDSP"> --%>
                                    <td  style="width:90px;" class="text-center">
                                        <button type="button" class="btn" style="padding: 0px 5px 0px 5px" data-bind="css: isTrainInfoMatchClass, text: isTrainInfoMatchText, click: showInfoComparePanel"></button>
                                    </td>
                                    <td  style="width:90px;"  class="text-center">
                                        <button type="button" class="btn" style="padding: 0px 5px 0px 5px" data-bind="css: isTimeTableMatchClass, text: isTimeTableMatchText, click: showTimeTableComparePanel"></button>
                                    </td>
                                    <td style="width: 17px" align="center" class="td_17"></td>
<%--                                 </shiro:hasPermission> --%>
                            </tr>
                            </tbody>
                        </table>
                       </div>
                    </div>
                </div>
<!--                 chat -->
                
    <%--             <div class="col-xs-2 col-md-2 col-lg-2 paddingtop5 paddingright0 paddingleft0 marginleft5" style="background: #ffffff; width: 16%">
                    <div class="row">
                        <div class="col-xs-12 col-md-12  col-sm-12 padding0">
                            <div id="chart_01" style="margin: 0; height: 253px;"></div>
                        </div>
                    </div>
                    <div class="row paddingtop100">
                        <div class="col-xs-12 col-md-12  col-sm-12 padding0">
                            <div id="chart_02" style="margin:0; height: 253px;"></div>
                        </div>
                    </div>
                    <div class="row paddingtop100">
                        <shiro:hasPermission name="JHPT.RJH.KDSP">
                            <div class="col-xs-12 col-md-12  col-sm-12 padding0">
                                <div id="chart_03" style="margin:0; height: 253px;"></div>
                            </div>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="JHPT.RJH.ZBZRSP">
                            <div class="col-xs-12 col-md-12  col-sm-12 padding0">
                                <div id="chart_04" style="margin:0; height: 253px;"></div>
                            </div>
                        </shiro:hasPermission>
                    </div>
                </div>
                 --%>
                
            </div>
            </div>
            <!-- char start -->
            <div id="chatId" style="display: none" class="margin panel panel-default">
            <table>
            	<tr style="border: 0 px">
            		<td style="border: 0 px"><div id="chart_01" style="margin: 0; height: 253px;width: 31%"></div></td>
            		<td style="border: 0 px"><div id="chart_02" style="margin:0; height: 253px;width: 31%"></div></td>
            		<td style="border: 0 px"><div id="chart_03" style="margin:0; height: 253px;width: 31%"></div></td>
            	</tr>
            </table>
            </div>
            <!-- char end -->
            
        </div>
    </div>
</section>

	<div id="kyvsyx" class="easyui-dialog" title="客运计划列车信息 vs 运行线列车信息"
		data-options="iconCls:'icon-save'"
		style="width: 750px; height: 500px;overflow-y: hidden;">
		 <iframe style="width: 100%;border: 0;overflow-y: hidden;" src=""></iframe>
	</div> 

	<div id="kyskvsyxsk" class="easyui-dialog" title="客运计划列车时刻表 vs 运行线列车时刻表"
		data-options="iconCls:'icon-save'"
		style="width: 750px; height: 500px;overflow-y: hidden;">
		 <iframe style="width: 100%;border: 0;overflow-y: hidden;" src=""></iframe>
	</div> 
	
	<!-- 重复列车 -->
	<div id="trainCf" class="easyui-dialog" title="重复列车"
			data-options="iconCls:'icon-save'"
			style="width: 700px; height: 650px;overflow-y: hidden;">
		
		 <div id="modifyRecordDiv_1" class="row" style="margin:5px;overflow: auto;">
		     <div class="table-responsive" > 
				<table class="table table-bordered table-striped table-hover" id="yourTableID2" width="100%" border="0" cellspacing="0" cellpadding="0"  >
					<thead>
						<tr style="height: 25px"> 
							<th align="center" style="width: 40px">序号</th>
							<th align="center" style="width: 80px">车次</th>
							<th align="center" style="width: 80px">始发时间</th>
							<th align="center" style="width: 80px">始发站</th>
							<th align="center" style="width: 60px">终到站</th>
						</tr>
					</thead>
		    	 </table>
	              <div id="left_height2" style="overflow-y:auto;height:580px;">
	                  <table class="table table-bordered table-striped table-hover" style="border:0;">
						<tbody data-bind="foreach: isTrainCfArray ">
							<tr  >
						        <td style="width: 40px" data-bind="text:$index() + 1"></td>
						        <td style="width: 80px" data-bind="text: trainNbr, attr:{title: trainNbr}"></td>
						        <td style="width: 80px" data-bind="text: startTime"></td>
						        <td style="width: 80px" data-bind="text: startStn "></td>
						        <td style="width: 60px" data-bind="text: endStn"></td>
							</tr> 
						</tbody> 
					</table>
		    	</div>
			</div>  
	  </div> 
  </div>

</body>

</html>
