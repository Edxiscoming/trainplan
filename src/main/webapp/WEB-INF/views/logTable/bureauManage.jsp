
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
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/bureauManage.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
	
    <title>对数表管理</title>
    
    <script type="text/javascript">
    		var basePath = "<%=basePath %>";
    		$(document).ready(function(){
        	/* //$(".trigger").click(alr);
        	$(".closeBlock").click(closeBlock);
        	var slideblock=$(".slideblock");
        	var left=$(".left");
        	var is_on=false;//默认是关闭
        	
        	function alr(){
        		if(is_on){
        		//换数据
        		}
        		else{
        		//打开并显示当前行数据
        			slideblock.animate({width:"400px"},500);
        			left.animate({width:"75%"},500);
        			is_on=true;
        		}
        	}
        	function closeBlock(){
        		slideblock.animate({width:"0px"},500);
        		left.animate({width:"98%"},500);
        		is_on=false;
        	} */
        	 
       })
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
                    <button type="button" class="btn btn-default greenBtn btn-sm pull-left" style="height:30px;width:70px; margin-right:3px;background:#27bdaf" onclick="checkData();"><span class="glyphicon glyphicon-plus">审核</span></button>
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
                    
                    <a class="btn btn-default greenBtn btn-sm pull-right" type="button" onclick="openGraph();" >
                        预览交路
                    </a>
                </div>
                <div class="container-fluid" style="border: solid 1px #E0E0E0;">
                    <form data-bind="with: currentCross" role="form" class="form-horizontal">
                            <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        车底交路：
                                    </div>
                                    <div class="pull-left" id="plan_construction_input_trainNbr">
                                        K15-K16
                                    </div>
                                </div>

                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        担当局：
                                    </div>
                                    <div class="pull-left" id="undertake">
                                        京局
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        组数：
                                    </div>
                                    <div class="pull-left" id="groupData">
                                        4
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        开行状态：
                                    </div>
                                    <div class="pull-left" id="openState">
                                        开行
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        类型：
                                    </div>
                                    <div class="pull-left" id="mold">
                                        普速
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        交替日期：
                                    </div>
                                    <div class="pull-left" id="relaceDate">
                                        2015-09-22
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        交替车次：
                                    </div>
                                    <div class="pull-left" id="replace_train_number">
                                        K15
                                    </div>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div style="">
                                <label style="" class="margin-top-5">开行规律</label>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        普通规律:
                                        </div>
                                    <div class="pull-left" id="normalRule">1</div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        指定星期:</div>
                                    <div class="pull-left" id="week">3</div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        指定日期:
                                    </div>
                                    <div class="pull-left" id="date">2015-08-01</div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        指定周期:
                                    </div>
                                    <div class="pull-left" id="cycle">110</div>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none;">
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        运行区段：
                                    </div>
                                    <div class="pull-left" id="runSection">
                                       运行区段2
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        运行距离：
                                    </div>
                                    <div class="pull-left" id="runDistance">
                                        2000KM
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        等级：
                                    </div>
                                    <div class="pull-left" id="level">
                                        等级1
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        对数：
                                    </div>
                                    <div class="pull-left" id="log">
                                        3
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        编组辆数：
                                    </div>
                                    <div class="pull-left" id="orgnize">
                                        2
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        定员：
                                    </div>
                                    <div class="pull-left" id="createPeople">
                                        900
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        编组内容：
                                    </div>
                                    <div class="pull-left" id="orgnizeContent">
                                        编组内容2
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        动车组类型：
                                    </div>
                                    <div class="pull-left" id="groupLevel">
                                        HXH
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        供电：
                                    </div>
                                    <div class="pull-left" id="electricity">
                                        1
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        集便：
                                    </div>
                                    <div class="pull-left" id="mass">
                                        1
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        空调：
                                    </div>
                                    <div class="pull-left" id="airconditioner">
                                        1
                                    </div>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div>
                                <div class="row">
                                    <div class="div-header pull-left">
                                        备注：
                                    </div>
                                        <div id="note" class="margin-top-5 pull-left" style="margin-bottom: 5px;width:250px;" id="remark">
                                        0.0备注要长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长
                                    	</div>
                                </div>
                            </div>

                        </form>
                </div>
            </div>
        </div>
    </div>    
    <div id="basicalGraph" title="运行图">

    </div>
    <script type="text/javascript">
  
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
</body>
</html>