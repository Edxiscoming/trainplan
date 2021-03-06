<%@page import="javax.sound.midi.SysexMessage"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
boolean isZgsUser = false;	//当前用户是否为总公司用户
if (user!=null && user.getBureau()==null) {
	isZgsUser = true;
}


String basePath = request.getContextPath();
Object jlData =  request.getAttribute("myJlData");
Object grid =  request.getAttribute("gridData");
Object editStnSort =  request.getAttribute("editStnSort");


%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <script type="text/javascript">
		var basePath = "<%=basePath %>";
		var canvasData = {};
		canvasData.grid = <%=grid%>;
		canvasData.editStnSort = <%=editStnSort%>;
		canvasData.jlData = <%=jlData%>;//交路数据
		var _isZgsUser = <%=isZgsUser%>;//当前用户是否为总公司用户
	</script>
</head>
<body style="overflow: auto">
<div class="panel-body" id="mainCanvas">
    <div style="" class="row">
        <form role="form" class="form-inline">
            <div style="margin:5px 0 10px 50px;" class="row">
                <button id="canvas_event_btn_refresh" class="btn btn-success btn-xs" type="button"><i class="fa fa-refresh"></i>刷新</button>
                <button id="canvas_event_btn_x_magnification" class="btn btn-success btn-xs" type="button"><i class="fa fa-search-plus"></i>X+</button>
                <button id="canvas_event_btn_x_shrink" class="btn btn-success btn-xs" type="button"><i class="fa fa-search-minus"></i>X-</button>
                <button id="canvas_event_btn_y_magnification" class="btn btn-success btn-xs" type="button"><i class="fa fa-search-plus"></i>Y+</button>
                <button id="canvas_event_btn_y_shrink" class="btn btn-success btn-xs" type="button"><i class="fa fa-search-minus"></i>Y-</button>
                比例：｛X:
                <label id="canvas_event_label_xscale">1</label>
                倍；Y:
                <label id="canvas_event_label_yscale">1</label>
                倍｝ <span>
                    <input type="checkbox" style="margin-left:10px" checked="checked" name="canvas_checkbox_stationType" id="canvas_checkbox_stationType_jt">
                    简图</span>
                <input type="checkbox" value="" style="margin-left:10px;margin-top:2px" id="canvas_checkbox_trainTime">
                显示时刻
                &nbsp;&nbsp;车底：
                <select id="canvas_select_groupSerialNbr">
                    <option selected="selected" value="">全部</option>
                    <option value="1">①组</option>
                    <option value="2">②组</option>
                </select>
                <!-- <button id="fullBtn" class="btn btn-default greenBtn btn-xs pull-right" onclick="parent.fullScreen();" style="margin-left:75px; background-color:#27bdaf;border-color:#ccc;color:#fff;">全屏</button>
                <button id="closeBtn" class="btn btn-default greenBtn btn-xs pull-right" onclick="parent.closeFull();" style="margin-left:5px;background-color:#d04541;color:#fff;border-color:#ccc; display:none;">关闭</button> -->
            </div>
        </form>
    </div>

    <div style="height: 100%;width:100%; overflow: auto;position: relative;">
        <canvas id="canvas_event_getvalue" width="682" height="395"></canvas>
       <%--  <div style="height: 55px;width:982px; overflow: hidden;display: none;position: fixed; left:8px; top: 45px;background: white;" id="header">
            <canvas id="canvas_event_header" height="55" style="left: 0px; top: 0px;position: absolute;"></canvas>
        </div>
        <div style="height: 435px;width:152px; overflow: hidden;display: none;position: fixed; left:0px; top: 95px;background: white;" id="leftX">
            <canvas id="canvas_event_leftX" width="148" style="left: 0px; position: absolute;"></canvas>
        </div>
        <div style="height: 55px;width:145px; display: none;position: fixed; left:8px; top: 45px;background: white;z-index: 100" id="coner">
        </div> --%>
    </div>


    </div>

    <!-- <div id="modifyRecordDiv"  style="margin:5px;overflow: auto;display:none;background:lightcyan">
        <div style="float: right"><button value="关闭" onclick="$('#modifyRecordDiv').hide();">关闭</button></div>
        <div id="left_height2" style="overflow-y:auto;height:180px;">
           <table class="table table-bordered table-striped table-hover" id="yourTableID2" width="100%" border="0" cellspacing="0" cellpadding="0"  >
               <thead>
                   <tr style="height: 25px">
                       <th align="center" style="width: 40px">序号</th>
                       <th align="center" style="width: 160px">交路名</th>
                       <th align="center" style="width: 80px">车次</th>
                       <th align="center" style="width: 80px">开行日期</th>
                       <th align="center" style="width: 60px;padding: 4px 0;">调整局</th>
                       <th align="center" style="width: 80px">调整时间</th>
                       <th align="center" style="width: 80px">调整类型</th>
                       <th align="center" style="width: 80px">调整依据</th>
                       <th align="center" style="width: 80px">起始日期</th>
                       <th align="center" style="width: 80px">终止日期</th>
                       <th align="center" style="width: 70px">规律</th>
                       <th align="center" style="width: 100px">择日日期</th>
                       <th align="center" style="width: ">调整内容</th>
                       <th align="center" style="width: 70px">调整人</th>
                       <th align="center" style="width: 90px">岗位</th>
                       <th style="width: 17px" align="center"></th>
                   </tr>
               </thead>
            </table>
             <div id="" style="overflow-y:auto;">
                 <table class="table table-bordered table-striped table-hover" style="border:0;">
                   <tbody data-bind="foreach: modifyPlanRecords ">
                       <tr>
                           <td style="width: 40px" data-bind="text:$index() + 1"></td>

                           <td style="width: 160px" data-bind="text: crossName, attr:{title: crossName}"></td>

                           <td style="width: 80px" data-bind="text: trainNbr"></td>

                           <td style="width: 80px" data-bind="text: runDate "></td>
                           <td style="width: 60px;padding: 4px 0;" data-bind="text: modifyPeopleBureau"></td>
                           <td style="width: 80px"  data-bind="text: modifyTime  "></td>
                           <td style="width: 80px" data-bind="text: modifyType  "></td>
                           <td style="width: 80px"  data-bind="text: modifyReason, attr:{title: modifyReason}"></td>
                           <td style="width: 80px" data-bind="text: startDate  "></td>
                           <td style="width: 80px" data-bind="text: endDate  "></td>
                           <td style="width: 70px" data-bind="text: rule  "></td>
                           <td style="width: 100px"  data-bind="text: selectedDate, attr:{title: selectedDate}"></td>
                           <td style="width: " data-bind="text: modifyContent , attr:{title: modifyContent} "></td>
                           <td style="width: 70px" data-bind="text: modifyPeople  , attr:{title: modifyPeople}  "></td>
                           <td style="width: 90px" data-bind="text: modifyPeopleOrg  , attr:{title: modifyPeopleOrg}  "></td>
                           <td style="width: 17px" align="center" class="td_17"></td>
                       </tr>
                   </tbody>
               </table>
        </div>
        </div>
    </div> -->

</body>
<script type="text/javascript" src="<%=basePath%>/assets/js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/util/canvas.util.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/moment.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/util/canvas.event.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/util/fishcomponent.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/util/canvas.component.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/logTable/crossGraph.js"></script>
</html>
