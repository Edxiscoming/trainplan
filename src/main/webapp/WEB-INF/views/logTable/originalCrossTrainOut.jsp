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
	<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/common.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script>	
	<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/originalCrossTrainOut.js"></script>

  	<script type="text/javascript">
        var basePath = "<%=basePath %>";
        var currentUserBureau="<%=currentUserBureau%>";/* 当前局 */
    </script>
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
			<div class="btn-group pull-left" style="margin-left:10px;">
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
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 10px 40px;">
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
                            <div class="form-group">
                                <span style="font-weight:bold;">开行规律：</span>
                            </div>
                            <div class="form-group">
							  <!-- Nav tabs -->
							  <ul class="nav nav-tabs" role="tablist">
							    <li role="presentation" class="active"><a href="#commonTab" aria-controls="commonTab" role="tab" data-toggle="tab">普通</a></li>
							    <li role="presentation"><a href="#weekTab" aria-controls="weekTab" role="tab" data-toggle="tab">星期</a></li>
							    <li role="presentation"><a href="#dateTab" aria-controls="dateTab" role="tab" data-toggle="tab">日期</a></li>
							    <li role="presentation"><a href="#cycleTab" aria-controls="cycleTab" role="tab" data-toggle="tab">周期</a></li>
							  </ul>
							
							  <!-- Tab panes -->
							  <div class="tab-content container-fluid" style="height: 140px; border: 1px solid #cccccc;padding-left:30px;" id="runType">
							    <div role="tabpanel" class="tab-pane text-center active" id="commonTab">
								    <div class="row" style="margin-top:50px;">
								      	<label class="col-xs-6"><input type="radio" name="commonLineRule" checked value="1"> 每日</label>
								    	<label class="col-xs-6"><input type="radio" name="commonLineRule" value="2"> 隔日</label>
								    </div>
								</div>
							    <div role="tabpanel" class="tab-pane" id="weekTab">
								    <div class="row" style="margin-top:30px;">
								    	<label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1" checked> 周一</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周二</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周三</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周四</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周五</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周六</label>
									     <label class="col-xs-4"><input type="checkbox" name="appointWeek" value="1"> 周日</label>
								    </div>
								</div>
							    <div role="tabpanel" class="tab-pane" id="dateTab">
 								<div class="form-group" style="margin-top:10px;">
	   								<span class="pull-left padding-5">固定日期：</span>
	   								<input type="date" class="form-control input-sm pull-left" style="width: 150px;" id="apointDayDateSolid" onclick="clear('#apointDaySolid');">
   								</div>
							   <div class="form-group">
									<span class="pull-left padding-5">每月日期：</span>
								    <select class="form-control input-sm pull-left" style="width: 150px;" id="apointDaySolid" multiple="multiple" onclick="clear('#apointDayDateSolid');">
								    	<option>1</option>
								    	<option>2</option>
								    	<option>3</option>
								    	<option>4</option>
								    	<option>5</option>
								    	<option>6</option>
								    	<option>7</option>
								    	<option>8</option>
								    	<option>9</option>
								    	<option>10</option>
								    	<option>11</option>
								    	<option>12</option>
								    	<option>13</option>
								    	<option>14</option>
								    	<option>15</option>
								    	<option>16</option>
								    	<option>17</option>
								    	<option>18</option>
								    	<option>19</option>
								    	<option>20</option>
								    	<option>21</option>
								    	<option>22</option>
								    	<option>23</option>
								    	<option>24</option>
								    	<option>25</option>
								    	<option>26</option>
								    	<option>27</option>
								    	<option>28</option>
								    	<option>29</option>
								    	<option>30</option>
								    	<option>31</option>
	                                 </select>
                                 </div>
								</div>
							    <div role="tabpanel" class="tab-pane" id="cycleTab">
							    <div class="form-group padding-5">周期为：</div>
							    <div class="row form-group">
							    <span class="pull-left padding-5">开</span>
							    <input type="number" class="form-control input-sm pull-left" style="width: 60px;" id="runDays" onfocus="clear('#irregularRule')">
                                  <span class="pull-left padding-5">天</span>
                                  <span class="pull-left padding-5" style="margin-left: 50px;">停</span>
									<input type="number" class="form-control input-sm pull-left" style="width: 60px;" id="stopDays" onfocus="clear('#irregularRule')">
									<span class="padding-5 pull-left">天</span></div>
							   <div class="row"> 
							   <span class="pull-left padding-5">不规则规律</span>
							   <input type="text" class="form-control input-sm pull-left" style="width: 150px;" id="irregularRule" onfocus="clear('#runDays','#stopDays')">
							   </div>
							    
								</div>
							  </div>
							
							</div>
                            <!-- <div class="form-group">
                                <label style="padding-left:17px;" class="control-label pull-left">
                                   <span style="color:red;padding-right:5px;">*</span>类型：
                                </label>
                                <div class="pull-left">
                                    <select style="width: 170px" class="form-control input-sm" name="operationType" id="operationType" onchange="changeOperationValue();">
                                    <option value="commonLineRule_everyday" >每日</option>
                                    <option value="commonLineRule_subday" >隔日</option>
                                    <option value="appointPeriod" >指定周期</option>
                                    <option value="appointDay" >指定日期</option>
                                    <option value="appointWeek" >指定星期</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label style="padding-left:17px;" class="control-label pull-left">
                                   <span style="color:red;padding-right:5px;">*</span>数值：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm" style="width: 170px;" pattern="^[0-1]{1,}$"  type="text" name="operationValue" id="operationValue" required value=1 disabled>

                                </div>
                            </div> -->
                            
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none none none; padding: 10px 40px;">
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
				                <input id="searchKey" type="text" name="searchKey" onkeyup="upperCase(this.id)" class="form-control input-sm pull-left" style="width:150px;"/>
				                <div class="pull-left" style="margin-left:10px;margin-top:5px;margin-right:20px;">
								<input type="checkbox" id="checkFlag1" checked>模糊
								</div>
				                <a type="button"  onclick="searchTrain()" class="btn btn-default btn-default blueBtn btn-xs">查询</a>
				                <a type="button"  onclick="confirmedTrain()" class="btn btn-default btn-default blueBtn btn-xs">添加</a>
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
    <!-- 查询车次 -->
     <%-- <div id="addTrainDlg" title="导入对数表" style="display: none;">
    	<img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;"> 
    	<input type="hidden" value="" id="trainAdded">
        <div class="" style="margin:15px;">
            <div class="form-group">
            	<span class="pull-left padding-5">车次：</span>
                <input id="searchKey" type="text" name="searchKey" class="form-control input-sm pull-left" style="width:250px; margin-right:30px;"/>
                <a type="button"  onclick="searchTrain()" class="btn btn-default btn-default blueBtn">查询</a>
            </div>
            <ul class="container-fluid" style="border:1px solid #ccc;height:30px;">
            	<li class="row"><span class="col-xs-5">车次</span><span class="col-xs-7">始发终到</span></li>
            </ul>
            <div id="result" class="list-group container-fluid" style="border:1px solid #ccc;overflow-y:auto;height:200px;">
           	</div>
            <div class="form-group text-center">
                <a type="button"  onclick="confirmedTrain()" class="btn btn-default btn-default blueBtn">确定</a>
                <a type="button"  onclick="canselSelect()" class="btn btn-default btn-default" style="margin-left:20px;">关闭</a>
            </div>
        </div>
    </div> --%>
    <script>
    //验证表单
       //对数
       /*  var pairNbr=document.getElementById("pairNbr");
        pairNbr.addEventListener("blur",function(event){
        	event=window.event;
        	var target=event.srcElement;
        	if(/^[\d.]{1,}$/g.test(target.value))
        	{
        		target.style.borderColor="";
        	}
        	else
        	{
        	    target.style.borderColor="red";
        	}
        },false);
        //数值
        var operationValue=document.getElementById("operationValue");
        operationValue.addEventListener("blur",function(event){
        	event=window.event;
        	var target=event.srcElement;
        	if(/^[0-1]{1,}$/g.test(target.value))
        	{
        		target.style.borderColor="";
        	}
        	else
        	{
        	    target.style.borderColor="red";
        	}
        },false); */
        /* //交替车次
        var alternateTrainNbr=document.getElementById("alternateTrainNbr");
        alternateTrainNbr.addEventListener("blur",function(event){
        	event=window.event;
        	var target=event.srcElement;
        	if(/^[a-zA-Z0-9]{1,}$/g.test(target.value))
        	{
        		target.style.borderColor="";
        	}
        	else
        	{
        	    target.style.borderColor="red";
        	}
        },false); */
    </script>
  </body>
</html>
