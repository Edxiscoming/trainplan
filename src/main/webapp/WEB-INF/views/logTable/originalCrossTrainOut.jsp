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
String  currentBureauShortName = user.getBureauShortName();
String currentUserBureau = user.getBureau();
%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
  <head>
    <title>外局对数表</title>
	
    <meta name="keywords" content="keyword1,keyword2,keyword3">
    <meta name="description" content="this is my page">
    <meta name="content-type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/logTable/logTable.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/logTable/mainCross.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/style.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/jquery.multiselect2side.css" />
	<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/common.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script>	
	<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.multiselect2side.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/moment.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/originalCrossTrainOut.js"></script>

  	<script type="text/javascript">
        var basePath = "<%=basePath %>";
        var currentUserBureau="<%=currentUserBureau%>";/* 当前局 */
    </script>
    <script src="<%=basePath %>/assets/js/dateChoseMore/kit.js"></script>
    <style type="text/css">
        .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber {
            font-size: 13px !important;
        }
        .datepicker {
            position: absolute;
            top: 750px !important;
            left: 885px !important;
        }
        a {
            color: #337ab7 !important;
            text-decoration: none !important;
        }
        .list-group-item.active, .list-group-item.active:focus, .list-group-item.active:hover {
            color: #fff !important;
        }
    </style>
  </head>
  
  <body class="container-fluid">
    <div class="div-header row">
    <div class="col-xs-12">
  		
		<label class="pull-left padding-5" style="margin-left: 10px;">路局</label>
        <span id="headerBureau" class=" pull-left" ></span>
        
        <span class="pull-left padding-5">方案：</span>
		<select class="form-control input-sm pull-left"
			style="width: 280px; color:red;" id="cmVersionId">
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">20150520__0519导入</option>
		</select>
        <span class="pull-left padding-5" style="margin-left: 10px;">担当局：</span>
		<select class="form-control pull-left input-sm" id="tokenBureau" style="width: 65px;padding: 1px 5px;">
             <option value=""></option>
        </select>
        <span class="pull-left padding-5" style="margin-left: 10px;">车次：</span>
         	<input type="text" id="trainNbr" class="input-sm form-control pull-left" style="width: 150px;" onkeyup="upperCase(this.id)">
			<!-- <span class="pull-left padding-5" style="margin-left: 10px;">等级：</span>
			<select class="form-control input-sm pull-left" id="levelSelect" style="width: 100px;" >
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">全部0</option>
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">全部1</option>
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">全部2</option>
			</select> -->
			<div class="pull-left" style="margin-left:10px;margin-top:5px">
				<input type="checkbox" id="checkFlag" checked>模糊
			</div>
            <div class="btn-group pull-left" style="margin-left:20px;" id="activeChange">
                <button type="button" class="btn btn-default active" value="0" onclick="searchCross2(this)">全部</button>
                <button type="button" class="btn btn-default"  value="1" onclick="searchCross2(this)">通过</button>
                <button type="button" class="btn btn-default" value="2" onclick="searchCross2(this)">到发</button>
            </div>
			<div class="btn-group pull-left" style="margin-left:20px;" id="exceptionChange">
	    		<button type="button" class="btn btn-default active" value="1" onclick="searchCross(this);" id="allType">全部</button>
	        	<button type="button" class="btn btn-default"  value="0" onclick="searchCross(this);">正常</button>
	        	<button type="button" class="btn btn-default" value="-1" onclick="searchCross(this);">异常</button>
			</div>
			
			<a type="button" class="btn btn-default blueBtn btn-sm pull-left"
					style="margin-left: 20px;" onclick="searchCross();"><span class="glyphicon glyphicon-search"style="margin-right:5px;"></span>查询</a>
         <div class="pull-right">
            <!-- <a type="button" class="btn btn-default blueBtn btn-sm pull-left" style="margin-left: 10px;" onclick="saveConfirm();"><span class="glyphicon glyphicon-floppy-disk"style="margin-right:5px;"></span>保存</a> -->
            <!--<a type="button" class="btn btn-default greenBtn btn-sm pull-left" style="margin-left: 10px;" onclick="crossesSet();"><span class="glyphicon glyphicon-cog"style="margin-right:5px;"></span>批量设置</a> -->
		</div>
  	</div>
    </div>
  	
     <div class="row" style="margin-top: 15px;">
        <div class="col-xs-12 left" style="height:100%;float:left;padding-left:15px;">
            <table id="crossTable">
            </table>
        </div>
        <div class="pull-right slideblock " id="trainBlock" style="height: 800px;width:400px;margin-right: 15px;float:right;display:none;z-index:1000; position:absolute;top:80px;right:10px; overflow-y:auto;">
        <section class="panel panel-default">
                <div style="height: 45px" class="panel-heading">
                    <span style="font-weight:bold;display: inline-block;margin-top: 4px;"> <span onclick="closeSlideBlock();" class="closeSlide">&gt;</span>修改车次</span>
                    <!-- <a class="btn btn-default blueBtn pull-right btn-xs" type="button" onclick="closeSlideBlock();">
                        取消 
                    </a> -->
                    <a class="btn btn-default greenBtn pull-right btn-xs" type="button" onclick="submitData()" style="margin-right:5px;">
                        保存 
                    </a>
                </div>
                <div class="container-fluid" style="border: solid 1px #E0E0E0;">
                    <form class="form-horizontal" role="form"  id="newCross">
                   		<input name="cmOriginalCrossId" id="cmOriginalCrossId" type="hidden">
                        <div style="padding-top:10px; padding-left:40px;border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
                            <div class="form-group">
                                <label style="padding-left:28px;" class="control-label pull-left">
                                    交路：
                                </label>
                                <div class="pull-left">
                                    <textarea class="form-control input-sm inputColor" style="width: 170px;" rows="3" name="crossName" id="plan_construction_input_trainNbr" type="text" disabled></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label style="padding-left:28px;" class="control-label pull-left">
                                    车次：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm" style="width: 170px;" type="text" name="trainName" id="trainName" disabled>
                                </div>

                            </div>
                            
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 10px 40px;">
                             <input name="cmOriginalTrainId" id="trainId" type="hidden">
                             <input name="trainSort" id="trainSort" type="hidden">
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    运行区段：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm" style="width: 170px;" type="text" name="crossSection" id="crossSection" disabled>
                                </div>

                            </div>
                             <div class="form-group">
                                <label style="padding-left:28px;" class="control-label pull-left">
                                    等级：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" type="text" name="crossLevel" id="crossLevel" disabled>
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:28px;" class="control-label pull-left">
                                    对数：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" pattern="^[\d.]{1,}$" type="text" name="pairNbr" id="pairNbr" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label style="padding-left:28px;" class="control-label pull-left">
                                    担当：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm" style="width: 170px;"  type="text" name="tokenVehBureau" id="tokenVehBureauShow" disabled>
                                    <input type="hidden" name="tokenVehBureau" id="tokenVehBureau">
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:28px;" class="control-label pull-left">
									经由： </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm" style="width: 170px;"  type="text" name="throughLine" id="throughLine" disabled>
                                </div>

                            </div>
                             <div class="form-group">
                            <label style="padding-left:28px;" class="pull-left">
                                备注：
                            </label>
                                <textarea style="width: 170px;" rows="4" class="form-control input-sm color-27bdaf" name="note" id="note" disabled></textarea>
                         </div>
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none;padding-top: 10px; padding-left:40px;padding-right:30px;">
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    开行状态：
                                </label>
                                <div class="pull-left">
                                    <select name="runState" id="runState" class="form-control input-sm" style=" width: 170px;" disabled>
                                    <option value=""></option>
                                    <option value="1">开行</option>
                                    <option value="2">备用</option>
                                    <option value="0">停运</option>
                                    </select>
                                </div>

                            </div>
                           <div class="row">
                                <span style="font-weight:bold;" class="col-xs-4">开行规律：</span>
                                <label class="col-xs-4"><input type="radio" class="runRule" name="commonLineRule" target="#commonTab" value="1">每日</label>
                                <label class="col-xs-4"><input type="radio" class="runRule" name="commonLineRule" target="#commonTab" value="2">隔日</label>
                                <label><input type="radio" style="margin-left:15px;" class="runRule" name="commonLineRule" target="#weekTab">每周</label>
                                <label><input type="radio" style="margin-left:25px;" class="runRule" name="commonLineRule" target="#monthTab">每月</label>
                                <label><input type="radio" style="margin-left:25px;" class="runRule" name="commonLineRule" target="#dateTab">日期</label>
                                <label><input type="radio" style="margin-left:25px;" class="runRule" name="commonLineRule" target="#cycleTab">周期</label>
                            </div>
                            <div class="form-group">
							
							  <!-- Tab panes -->
							  <div class="tab-content container-fluid" style="height: 140px; border: 1px solid #cccccc;padding-left:30px; border-radius: 4px;" id="runType">
							    <div role="tabpanel" class="tab-pane active" id="commonTab" style="background-color:#eeeeee;height: 100%; margin-left:-30px;margin-right:-15px;">
								</div>
							    <div role="tabpanel" class="tab-pane" id="weekTab">
								    <div class="row" style="margin-top:30px;">
								    	<label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周一</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周二</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周三</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周四</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周五</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周六</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周日</label>
								    </div>
								</div>
							    <div role="tabpanel" class="tab-pane" id="dateTab" style="background-color:#eeeeee;height: 100%; margin-left:-30px;margin-right:-15px;">
                                    <textarea  type="text" class="pull-left" id="J_input" value="" style="*zoom:1;width: 129px; height:100px;margin-top:20px;margin-left:10px;" onclick="clear('#apointDaySolid');"></textarea>
                                    <a class="btn btn-default greenBtn pull-left btn-xs setDate" id="dateTabSet" type="button" style="margin-left:25px;margin-top:30px;" onclick="openRegularSet();"> 设置 </a>
                                    <a class="btn btn-default greenBtn pull-left btn-xs" id="dateTabClear" style="margin-top:30px; margin-left:10px;" type="button" onclick="clearAll();">清空</a>
                                </div>
                                <div role="tabpanel" class="tab-pane" id="monthTab">
                                    <div class="row" id="apointDaySolid" style="margin-top:10px;overflow-y:scroll;height:120px;margin-left:-28px;" onclick="clear('#J_input');">
                                        <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="1" >1</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="2">2</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="3">3</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="4">4</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="5">5</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="6">6</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="7">7</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="8">8</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="9">9</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="10">10</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="11">11</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="12">12</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="13">13</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="14">14</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="15">15</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="16">16</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="17">17</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="18">18</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="19">19</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="20">20</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="21">21</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="22">22</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="23">23</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="24">24</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="25">25</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="26">26</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="27">27</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="28">28</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="29">29</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="30">30</label>
                                         <label class="col-xs-1"><input type="checkbox" name="apointDaySolid" value="31">31</label>
                                    </div>
                                </div>
							    <div role="tabpanel" class="tab-pane" id="cycleTab">
							    <div class="form-group padding-5">周期为：</div>
							    <div class="row form-group">
							    <span class="pull-left padding-5">开</span>
							    <input type="number" min="1" class="form-control input-sm pull-left" style="width: 60px;" id="runDays" onfocus="clear('#irregularRule')">
                                  <span class="pull-left padding-5">天</span>
                                  <span class="pull-left padding-5" style="margin-left: 50px;">停</span>
									<input type="number" min="0" class="form-control input-sm pull-left" style="width: 60px;" id="stopDays" onfocus="clear('#irregularRule')">
									<span class="padding-5 pull-left">天</span></div>
							   <!-- <div class="row"> 
							   <span class="pull-left padding-5">不规则规律</span>
							   <input type="text" class="form-control input-sm pull-left" style="width: 150px;" id="irregularRule" onfocus="clear('#runDays','#stopDays')">
							   </div> -->
							    
								</div>
							  </div>
							
							</div>
                            
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none none none; padding: 10px 40px; display:none;">
                        	<div class="form-group">
                                <label class="control-label pull-left">
                                   交替车次：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm " style="width: 170px;" pattern="^[a-zA-Z0-9]{1,}$" type="text" name="alternateTrainNbr" id="alternateTrainNbr">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left" style="margin-left:-10px;">
                                   <span style="color:red;padding-right:5px;">*</span>交替日期：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm " style="width: 170px;"  type="date" name="alternateDate" id="alternateDate" required>
                                </div>

                            </div>
                        </div>
                    </form>
                </div>
            </section>
        </div>
        <div class="pull-right slideblock " id="crossBlock" style="height: 800px;width:700px;overflow-y: auto;margin-right: 15px;float:right;display:none;z-index:999; position:absolute;top:80px;right:10px;">
            <div class="panel panel-default">
                <div class="panel-heading" style="height:40px;">
                    <div style="margin-top:5px" class="pull-left"> <span onclick="closeSlideBlock();" class="closeSlide">&gt;</span> 交路信息</div>
                   <!--  <a type="button" class="btn btn-default blueBtn btn-sm pull-right closeBlock" style="margin-left: 20px; margin-top:-5px;" onclick="closeSlideBlock();">取消</a> -->
                    <a class="btn btn-default greenBtn btn-sm pull-right" type="button" style="margin-right: 5px; margin-top:-5px;" onclick="saveCross();">保存</a>
                </div>
                <div class="panel-body" style="height:800px;">
                    <div id="addTrainDlg" title="导入对数表" >
				    	<input type="hidden" value="" id="trainAdded">
				        <div class="" style="margin:15px;">
				            <div class="form-group">
				            	<span class="pull-left padding-5">方案：</span>
				            	<select class="form-control input-sm pull-left" style="width: 200px; color:red;" id="cmVersionId2">
									<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">20150520__0519导入</option>
								</select>
				            	<span class="pull-left padding-5">车次：</span>
				                <input id="searchKey" type="text" name="searchKey" onkeyup="upperCase(this.id)" class="form-control input-sm pull-left" style="width:60px;"/>
				                <div class="pull-left" style="margin-left:10px;margin-top:5px;margin-right:20px;">
								<input type="checkbox" id="checkFlag1" checked>模糊
								</div>
				                <a type="button"  onclick="searchTrain()" class="btn btn-default btn-default blueBtn btn-xs">查询</a>
				                <a type="button"  onclick="confirmedTrain(0)" class="btn btn-default btn-default blueBtn btn-xs">插入</a>
                                <a type="button"  onclick="confirmedTrain(1)" class="btn btn-default btn-default blueBtn btn-xs">追加</a>
				            </div>
				            <ul class="container-fluid" style="border:1px solid #ccc;height:30px;">
				            	<li class="row">
				            	<span class="col-xs-3">车次</span>
				            	<span class="col-xs-5">始发终到</span>
				            	<span class="col-xs-4">有效期</span>
				            	</li>
				            </ul>
				            <div id="result" class="list-group container-fluid" style="border:1px solid #ccc;overflow-y:auto;height:200px;">
				           	</div>
				            <!-- <div class="form-group text-center">
				                
				                <a type="button"  onclick="canselSelect()" class="btn btn-default btn-default" style="margin-left:20px;">关闭</a>
				            </div> -->
				        </div>
			    	</div>
			    	<div style="BORDER-TOP: #647B71 1px dashed; OVERFLOW: hidden; HEIGHT: 1px; margin:40px 0px;">
                    </div>
			    	<div class="row">
                        <label class="control-label pull-left" style="margin-left:28px; margin-top:4px;">
                            交路：
                        </label>
                        <div class="pull-left">
                            <textarea class="form-control inputColor input-sm" style="width: 450px; margin-left:5px;" id="crossName" disabled></textarea>
                        	<input type="hidden" id="crossId" value=""><!-- 交路id -->
                        	<input type="hidden" id="tokenVehBureau" value=""><!-- 担当局id -->
                        	<input type="hidden" id="createCrossFlag" value=""><!-- 担当局id -->
                        </div>
                    </div>
                    <ul id="numberDetail" style="height:350px;overflow-y:auto;overflow-x:hidden; margin-top:10px;">
                        <li class="row">
                             <label class="control-label pull-left">车次<span class="indexOfTrain">1</span>：OG112/3</label>
                             <div class="pull-right" style="margin-right:20px;">
                             	 <button class="btn btn-default btn-xs redBtn" onclick="deleteTrainNumber(this.parentNode.parentNode)">删除</button>
	                             <button class="btn btn-default btn-xs" style="margin-left:45px;" onclick="upRow(this.parentNode.parentNode)">上移</button>
	                             <button class="btn btn-default btn-xs" style="margin-left:10px;" onclick="downRow(this.parentNode.parentNode)">下移</button>
                             </div>
                        </li>
                    </ul>
            	</div>
     		</div>
     	</div>
     </div>
     <!-- s -->
     <div id="shadowBlock" onclick="closeSlideBlock();" style="width:100%;height:100%;z-index:100;background:#ccc;opacity:0.40;display:none;position:absolute;top:0px;left:0px;"></div>
     <!-- 导入对数表 -->
     <div id="importExl" title="导入对数表" style="display: none;">
    	<img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;"> 
        <form class="form-horizontal container-fluid" style="margin-left: 30px; margin-top:15px;">
            <div class="form-group">
                <span class="pull-left padding-5">方案：</span>
                <select class="form-control input-sm pull-left" style="width: 280px;" id="cmVersionId_dialog" disabled>
                    <option class="templatePlan" style="display:none;"></option>
                </select>
            </div>
            <div class="form-group">
                <input id="fileToUpload" type="file" size="45" name="fileToUpload"  name="fileName" accept=".xls,.xlsx"/>
            </div>
            <div class="form-group">
                <a type="button" id="upload_btn" onclick="uploadFile('false')" class="btn btn-default btn-default blueBtn">导入</a>
            </div>
        </form>

    </div>

    <!-- 设置开行规律 -->
     <div id="regularSet" title="设置开行规律——日期" style="display: none;">
        <img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;"> 
        <div class="panel-body" style="margin-left:85px; margin-top:30px;">
            <div class="row">
                <div class="col-md-12">
                    <label class="pull-left" style="margin-top:8px;">开始日期：</label>
                    <input type="date" name="alternateDate" id="input_wd_startDate" class="pull-left form-control" style="width:180px;">
                    <div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>

                    <label class="pull-left" style="margin-top:8px;margin-left:30px;">截至日期：</label>
                    <input type="date" id="input_wd_endDate" name="alternateDate" class="pull-left form-control" style="width:180px;">
                    <div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
                    <button type="button" class="pull-left" style="margin-top:5px; margin-left:10px;" onclick="btnLoadSelectDate ();">加载</button>
                </div>
            </div>
            <div class="row" style="margin-left:20px; margin-top:20px;">
                <div class="col-md-12">
                    <select name="liOption[]" id='liOption' multiple='multiple' size='8' > 
                        
                    </select> 
                </div>
            </div>
            <div class="row" style="margin-top:20px;">
                <div class="col-md-12">
                    <label class="pull-left" style="margin-top:10px;">择日日期：</label>
                    <textarea class="pull-left form-control" id="include_days" rows="3" disabled style="width:510px;"></textarea>
                </div>
            </div>
            <div class="row" style="margin-top:40px;">
                <div class="col-md-12">
                    <button type="button" class="btn btn-default btn-sm blueBtn" style="margin-left:250px;" onclick="sureClose();">确定</button>
                </div>
            </div>
        </div>
    </div>   
  </body>
</html>
