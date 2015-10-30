
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
<!DOCTYPE HTML>
<html lang="en">
<head> 
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/logTable/importCrossData.css">
	<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/crossManage.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
	
    <title>对数表管理</title>
    
    <script type="text/javascript">
    		var basePath = "<%=basePath %>";
    </script>
    <style type="text/css">
    .left{
    	width:98%;
    }
    </style>
    
</head>
<body>
    <div class="container-fluid" >
        <div class="row" style="padding: 15px 0px;background: #f9f9f9; border-bottom: solid 1px #E0E0E0;">
            <div class="col-xs-8">
                <span class="pull-left padding-5" style="margin-left: 10px;">方案：</span>
                <select class="form-control input-sm pull-left" style="width: 280px;" id="cmVersionId">
                    <option class="templatePlan" style="display:none;"></option>
                </select>
                <span class="pull-left padding-5" style="margin-left: 30px;">类型：</span>
                <select class="form-control input-sm pull-left" style="padding:1px 8px;width: 80px" id="trainType">
                    <option value="-1">全部</option>
                    <option value="0">普线</option>
                    <option value="1">高线</option>
                    <option value="2">混合</option>
                </select>
                <a type="button" class="btn btn-default greenBtn btn-sm pull-left" style="margin-left: 20px;" onclick="searchData();">查询</a>
            </div>
            <div class="col-xs-4">
                <div class="pull-right">
                    <button type="button" class="btn btn-default greenBtn btn-sm pull-left" style="height:30px;width:70px; margin-right:3px;background:#27bdaf" onclick="addOpen();"><span class="glyphicon glyphicon-plus"></span>新增</button>
                    <button data-toggle="modal" data-target="#" id="btn_cross_delete" type="button" class="btn btn-default redBtn btn-sm pull-left" style="height:30px;width:60px;margin-left:20px;background: #ec9820;" onclick="deleteCross();">删除</button>
                </div>

            </div>
        </div>
        <div class="row" style="margin-top: 15px;">
            <div class="pull-left left" style="height:800px;margin-left: 30px;float:left;">
                <table class="easyui-datagrid" id="mcrossTable">
                </table>
            </div>
            <div class="pull-right slideblock " style="height: 800px;width:400px;overflow-y: auto;margin-right: 15px;float:right;display:none;">
                <div style="height: 40px" class="panel-header">
                    <div style="margin-top:5px" class="pull-left"> 对数表信息</div>
                    <a type="button" onclick="closeSlideBlock();" class="btn btn-default greenBtn btn-sm pull-right closeBlock" style="margin-left: 20px;"><span class="glyphicon glyphicon-arrow-right">关闭</span></a>
                    <a class="btn btn-default greenBtn btn-sm pull-right" type="button" style="margin-left: 20px;" onclick="changeData();">
                        保存
                    </a>
                    <a class="btn btn-default greenBtn btn-sm pull-right" type="button" onclick="openGraph();" >
                        预览交路
                    </a>
                </div>
                <div class="container-fluid" style="border: solid 1px #E0E0E0;">
                    <form class="form-horizontal" role="form" id="update">
                        <div style="padding-top:20px; padding-left:40px;border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
                        	
                              <input class="form-control inputColor input-sm" style="display:none" value="617b0e6a-5845-4564-921a-1a94c3d82937"  type="text" name="cmPartOriginalCrossId">
                       
                            
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    车底交路：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control inputColor input-sm" style="width: 170px;" value="K15-K16" id="plan_construction_input_trainNbr" type="text" name="crossName">
                                </div>
                            </div>

                            <div class="form-group">
                                <label style="padding-left:15px;" class="control-label pull-left">
                                    担当局：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control inputColor input-sm" id="undertake" style="width: 170px;" value="成" type="text" name="tokenVehBureau">
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    组数：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm inputColor" id="groupData" style="width: 170px;" value="1" type="text" name="groupTotalNbr">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    开行状态：
                                </label>
                                <div class="pull-left">
                                    <select id="openState" style="width: 170px" class="form-control input-sm" name="spareFlag"><option value="1">1:开行</option><option value="2">2:备用</option><option value="9">9:停运</option></select>
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    类型：
                                </label>
                                <div class="pull-left">
                                    <select style="width: 170px" id="mold" class="form-control input-sm" name="locoType"><option value="0">0:普速</option><option value="1">1:高铁</option><option value="2">2:混合</option></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    交替日期：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm inputColor" id="relaceDate" style="width: 170px;" value="2015-09-21" type="text" name="alternateDate">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    交替车次：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm inputColor" id="replace_train_number" style="width: 170px;" value="K15" type="text" name="alternateTrainNbr">
                                </div>
                            </div>
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 20px 40px;">
                            <div class="form-group" style="padding:10px">
                                <span style="margin:4px; font-weight:bold; font-size:16px;">开行规律：</span>
                            </div>
                            <div class="form-group">
                                    <label class="control-label pull-left">
                                        普通规律：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" id="normalRule" style="width: 170px;" type="text" disabled  name="commonlineRule">
                                    </div>
                            </div>
                            <div class="form-group">
                                    <label class="control-label pull-left">
                                        指定星期：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" id="week" style="width: 170px;" value="00011" type="text" disabled name="appointWeek">
                                    </div>
                            </div>
                                <div class="form-group">
                                    <label class="control-label pull-left">
                                        指定日期：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" id="date" style="width: 170px;" type="text" disabled name="appointDay">
                                    </div>
                            </div>
                            <div class="form-group">
                                    <label class="control-label pull-left">
                                        指定周期：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" id="cycle" style="width: 170px;" type="text" disabled name="appointPeriod">
                                    </div>
                            </div>
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 20px 40px;">
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    运行区段：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" style="width: 170px;" type="text" id="runSection" name="crossSection">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    运行距离：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" id="runDistance" style="width: 170px;" type="text" name="runRange">
                                </div>
                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    等级：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" id="level" type="text" name="crossLevel">
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    对数：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" id="log" value="1" type="text" name="pairNbr">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    编组辆数：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" id="orgnize" type="text" name="marshallingNums">
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    定员：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" id="createPeople" type="text" name="peopleNums">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    编组内容：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" id="orgnizeContent" type="text" name="marshallingContent">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left" style="margin-left:-13px;">
                                    动车组类型：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" id="groupLevel" type="text" name="crhType">
                                </div>
                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    供电：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" id="electricity" style="width: 60px;" value="1" type="text" name="elecSupply">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left" style="padding-left:30px;">
                                    集便：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" id="mass" style="width: 60px;" value="1" type="text" name="dejCollect">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left" style="padding-left:30px;">
                                    空调：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" id="airconditioner" style="width: 60px;" value="1" type="text" name="airCondition">
                                </div>
                            </div>
                        </div>
                        <div style="padding-top:10px; padding-left:15px;">
                            <label style="padding-bottom:5px; padding-left:37px;" class="pull-left">
                                备注：
                            </label>
                                <textarea style="width: 170px;" rows="4" id="remark" class="form-control input-sm color-27bdaf" name="note"></textarea>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 新增对数行 -->
    <div id="add_data"  title="新增对数行"
        style="overflow-y:auto;display:none;">
        <section class="panel panel-default">
                <div style="height: 35px" class="panel-header">
                    <span> 对数表信息</span>
                    <a class="btn btn-default greenBtn pull-right btn-xs" type="button" onclick="submitData()">
                        保存 
                    </a>
                </div>
                <div class="container-fluid" style="border: solid 1px #E0E0E0;">
                    <form class="form-horizontal" role="form"  id="newCross">
                        <div style="padding-top:20px; padding-left:40px;border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
                        	<div class="form-group">
                                <label class="control-label pull-left">
                                    版本号：
                                </label>
                                <div class="pull-left">
                                    <select class="form-control input-sm pull-left" style="width: 280px;" id="cmVersionId_dialog">
					                    <option class="templatePlan" style="display:none;"></option>
					                </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    车底交路：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm inputColor" style="width: 170px;" name="crossName" id="plan_construction_input_trainNbr" type="text">
                                </div>
                            </div>

                            <div class="form-group">
                                <label style="padding-left:15px;" class="control-label pull-left">
                                    担当局：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm inputColor" style="width: 170px;" value="成" type="text" name="tokenVehBureau">
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    组数：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control inputColor" style="width: 170px;" value="1" onkeyup="value=value.replace(/[^1-9]/g,'')" type="text" name="groupTotalNbr">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    开行状态：
                                </label>
                                <div class="pull-left">
                                    <select style="width: 170px" class="form-control input-sm" name="spareFlag"><option value="1">1:开行</option><option value="2">2:备用</option><option value="9">9:停运</option></select>
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    类型：
                                </label>
                                <div class="pull-left">
                                    <select style="width: 170px" class="form-control input-sm" name="locoType"><option value="0">0:普速</option><option value="1">1:高铁</option><option value="2">2:混合</option></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    交替日期：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm inputColor" style="width: 170px;" value="2015-09-21" type="text" name="alternateDate">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    交替车次：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm inputColor" style="width: 170px;" value="K15" type="text" name="alternateTrainNbr">
                                </div>
                            </div>
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 20px 40px;">
                            <div class="form-group" style="padding:10px">
                                <span style="margin:4px; font-weight:bold; font-size:16px;">开行规律：</span>
                            </div>
                            <div class="form-group">
                                    <label class="control-label pull-left">
                                        普通规律：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" style="width: 170px;" type="text" name="commonlineRule">
                                    </div>
                            </div>
                            <div class="form-group">
                                    <label class="control-label pull-left">
                                    
                                        指定星期：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" style="width: 170px;" value="00011" type="text" name="appointWeek">
                                    </div>
                            </div>
                                <div class="form-group">
                                    <label class="control-label pull-left">
                                        指定日期：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" style="width: 170px;" type="text" name="appointDay">
                                    </div>
                            </div>
                            <div class="form-group">
                                    <label class="control-label pull-left">
                                        指定周期：
                                    </label>
                                    <div class="pull-left">
                                        <input class="form-control input-sm color-27bdaf" style="width: 170px;" type="text" name="appointPeriod">
                                    </div>
                            </div>
                        </div>
                        <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none; padding: 20px 40px;">
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    运行区段：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" style="width: 170px;" type="text" name="crossSection">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    运行距离：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" style="width: 170px;" type="text" name="runRange">
                                </div>
                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    等级：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" type="text" name="crossLevel">
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    对数：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" value="1" type="text" name="pairNbr">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    编组辆数：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" type="text" name="marshallingNums">
                                </div>

                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    定员：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" type="text" name="peopleNums">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left">
                                    编组内容：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" type="text" name="marshallingContent">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left" style="margin-left:-13px;">
                                    动车组类型：
                                </label>
                                <div class="pull-left">
                                    <input style="width: 170px;" class="form-control input-sm" type="text" name="crhType">
                                </div>
                            </div>
                            <div class="form-group">
                                <label style="padding-left:30px;" class="control-label pull-left">
                                    供电：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" style="width: 60px;" value="1" type="text" name="elecSupply">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left" style="padding-left:30px;">
                                    集便：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" style="width: 60px;" value="1" type="text" name="dejCollect">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="control-label pull-left" style="padding-left:30px;">
                                    空调：
                                </label>
                                <div class="pull-left">
                                    <input class="form-control input-sm color-27bdaf" style="width: 60px;" value="1" type="text" name="airCondition">
                                </div>
                            </div>
                        </div>
                        <div style="padding-top:10px; padding-left:15px;">
                            <label style="padding-bottom:5px; padding-left:37px;" class="pull-left">
                                备注：
                            </label>
                                <textarea style="width: 170px;" rows="4" class="form-control input-sm color-27bdaf" name="note"></textarea>
                        </div>
                    </form>
                </div>
            </section>
            
    </div> 
    <div id="basicalGraph" title="运行图"> </div>
    <div id="returnedMsg" title="状态报告"></div>
    
    <script type="text/javascript">
        function addOpen () {

            $('#add_data').show().dialog({
                title: '新增对数行',
                width:700,
                height:700,
                cache: false,
                modal: true
            });

            $('#add_data').dialog('open');
        }

        function openGraph(){
            $('#basicalGraph').dialog({
                title: '运行图',
                content:"<img src='crossGraph.png' alt='运行图'/>",
                width: 800,
                height: 600,
                cache: false,
                modal: true
            });
            $('#basicalGraph').dialog('open');
        }
    </script>
    <div style="clear:both;"></div>
</body>
</html>