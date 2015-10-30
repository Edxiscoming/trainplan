
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
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/style.css">
    <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/bjdd_manage.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
    <script type="text/javascript" src="<%=basePath%>/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/common.js"></script>
    <title>对数表审核</title>
    
    <script type="text/javascript">
            var basePath = "<%=basePath %>";
            var currentUserBureau="<%=currentUserBureau%>";/* 当前局 */
    </script>
    <style type="text/css">
/*     .left{ */
/*         width:98%; */
/*     } */
		#numberDetail li:first-child button:nth-child(2),
		#numberDetail li:last-child button:last-child{
			cursor:not-allowed;
			color:#ccc;
		}
		.trainNo{
			width: 100px;
		    display: inline-block;
		}
		ul {
			list-style: none;
		}
		#numberDetail li {
			margin-top: 10px;
		}
		.circle {
			width: 30px;
			height:30px;
            border-radius: 7.5px;
		}
		.tableTop {
			font-size: 12px;
		}
		#headerBureau {
			margin-left: 5px;
			margin-right: 10px;
			background-color: #fff;
			border-radius: 3px;
			border: 1px solid #ccc;
			padding: 5px 10px;
			color: blue;
			font-weight: bold;
		}
    </style>
    
</head>
<body>
	<div style="margin:5px; height:970px; overflow-y:hidden; overflow-x:hidden;">
        <div class="container-fluid">
	        <div class="row" style="padding: 15px 0px;background: #f9f9f9; border-bottom: solid 1px #E0E0E0;">
	            <div class="col-xs-12">
	            	<label class="pull-left padding-5" style="margin-left: 10px;">路局</label>
        			<span id="headerBureau" class=" pull-left" ></span>
	                <span class="pull-left padding-5" >方案：</span>
	                <select class="form-control input-sm pull-left" style="width: 280px; color:red" id="cmVersionId">
	                    <option class="templatePlan" style="display:none;"></option>
	                </select>
	                <span class="pull-left padding-5" id="token" style="margin-left: 10px; display:none; ">担当局：</span>
	                <select class="form-control input-sm pull-left" style="width: 60px;padding: 1px 5px; display:none;" id="tokenVehBureau">
	                    <option class="templatePlan" value="-1">全部</option>
	                </select>
	                <span class="pull-left padding-5" style="margin-left: 30px;">车次：</span>
	                <input class="form-control input-sm pull-left" style="width:200px" type="text" id="cmOriginalTrainId"  onkeyup="upperCase(this.id)">
	                <div class="pull-left" style="margin-left:20px;margin-top:5px">
						<input type="checkbox" id="checkFlag" checked>
						<span style="margin-left:3px;">模糊</span>
					</div>
	                <a type="button" id="selfSearchBtn" class="btn btn-default blueBtn btn-sm pull-left" style="margin-left: 20px;" onclick="searchData(0);"><span class="glyphicon glyphicon-search" style="margin-right:5px;"></span>查询</a>

	               <a class="btn btn-default greenBtn btn-sm pull-left" type="button" style="margin-left: 20px;" onclick="changeData();" name="operationBtn"><span class="glyphicon glyphicon-folder-close" style="margin-right:5px;"></span>保存</a>

	               <a class="btn btn-default greenBtn btn-sm pull-left" type="button" style="margin-left: 20px;" onclick="checkData();" name="operationBtn"><span class="glyphicon glyphicon-check" style="margin-right:5px;"></span>审核</a>
	                
	                <button type="button" id="crossBtn" class="btn btn-default blueBtn btn-sm pull-left" style="height:30px;width:90px; margin-left:20px;background:#27bdaf" onclick="generateCross();"><span class="glyphicon glyphicon-random" style="margin-right:5px;"></span>生成交路</button>
	                <a class="btn btn-default redBtn btn-sm pull-left" type="button" style="margin-left: 20px;" onclick="crossesDelete();" name="operationBtn"><span class="glyphicon glyphicon-trash" style="margin-right:5px;"></span>删除</a>
	            </div>
	        
	        </div>
	    </div> 

	    <div class="container-fluid">
	    	<div class="row">
	    		<div class="col-md-5">
	    			<div class="row tableTop" style="margin-top: 15px;">
	    				<div class="col-md-12">
	    					<span class="pull-left padding-5" >相关局：</span>
	    					<span class="pull-left padding-5" id="allRelevantBureau"></span>
	    				</div>
	    				<div class="col-md-12">
	    					<span class="pull-left padding-5" >审核通过局：</span>
	    					<span class="pull-left padding-5" id="passRelevantBureau"></span>
	    					<span class="pull-left padding-5" style="margin-left:30px;">审核不通过局：</span>
	    					<span class="pull-left padding-5" id="NotPassRelevantBureau"></span>
	    					<span class="pull-left padding-5" style="margin-left:30px;">未审核局：</span>
	    					<span class="pull-left padding-5" id="notAuditRelevantBureau"></span>
	    				</div>
			            <div class="col-md-12 left" style="height:830px;padding-left:15px;margin-top:5px;">
			                <table class="easyui-datagrid" id="mcrossTable">
			                </table>
			            </div>
			         </div>
	    		</div>
	    		<div class="col-md-7" style="height:900px; overflow-x:hidden; overflow-y:hidden;">
	    			<div class="row" style="margin-top: 15px;">
	    				<div class="col-md-12">
	    					<div class="panel panel-default">
							  <div class="panel-heading">
							    <h3 class="panel-title">运行图
							    	<span style="margin-left:20px; font-weight:bold;" id="crossNameTitle"></span>
							    	<!-- <a class="btn btn-default greenBtn btn-sm pull-right circle" type="button" style="margin-right: 10px;" onclick="" name="operationBtn"><span class="glyphicon glyphicon-download" style="margin-right:5px;"></span></a>
							    	<a class="btn btn-default greenBtn btn-sm pull-right circle" type="button" style="margin-right: 10px;" onclick="" name="operationBtn"><span class="glyphicon glyphicon-upload" style="margin-right:5px;"></span></a> -->
							    	<a class="btn btn-default greenBtn btn-sm pull-right" type="button" style="margin-right: 10px; margin-bottom:-3px;" onclick="checkData();" name="operationBtn">审核</a>
							    </h3>
							  </div>
							  <div class="panel-body">
							    <div id="crossGraph" style="height:99%;">
			                        <iframe src="" width="100%" height="400px" scroll="no"></iframe>
			                    </div>
			                    <div class="table-responsive" style="height:150px;">
		                            <table id="cross_trainInfo">
		                            </table>
		                            <input type="text" id="crossHideId" style="display:none;">
			                        <input type="text" id="trainNumber" style="display:none;">
			                        <input type="text" id="startStation" style="display:none;">
			                        <input type="text" id="endStation" style="display:none;">
		                        </div>
							  </div>
							</div>
	    				</div>
	    			</div>
	    			<!-- <div class="row">
	    				<div class="col-md-12">
	    					<div class="panel panel-default">
							  <div class="panel-body">
							     <div class="table-responsive" style="height:150px;">
		                            <table id="cross_trainInfo">
		                            </table>
		                            <input type="text" id="crossHideId" style="display:none;">
			                        <input type="text" id="trainNumber" style="display:none;">
			                        <input type="text" id="startStation" style="display:none;">
			                        <input type="text" id="endStation" style="display:none;">
		                        </div>
							  </div>
							</div>
	    				</div>
	    			</div> -->
	    			<div class="row">
	    				<div class="col-md-12">
	    					<div class="panel panel-default">
							  <div class="panel-heading">
							    <h3 class="panel-title">交路属性</h3>
							  </div>
							  <div class="panel-body" style="height:200px; overflow-x:hidden; overflow-y:auto;">
							    <div class="row">
							    	<div class="col-md-4">
							    		<div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        车底交路：
				                                    </div>
				                                    <div class="pull-left" id="plan_construction_input_trainNbr2">
				                                       
				                                    </div>
				                                </div>
				
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        担当局：
				                                    </div>
				                                    <div class="pull-left" id="undertake2">
				                                        
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                   <div class=" pull-left div-header" style="padding-left:30px;">
				                                    组数：
				                                   </div>
				                                <div class="pull-left ">
				                                    <span class="hidden" id="groupData2Span"></span>
				                                </div>
				                               </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        开行状态：
				                                    </div>
				                                    <div class="pull-left" id="openState2">
				                                        
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        类型：
				                                    </div>
				                                    <div class="pull-left" id="mold2">
				                                        
				                                    </div>
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        交替日期：
				                                    </div>
				                                    <div class="pull-left" id="relaceDate2">
				                                        
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        交替车次：
				                                    </div>
				                                    <div class="pull-left" id="replace_train_number2">
				                                        
				                                    </div>
				                                </div>
							    	</div>
							    	<div class="col-md-4">
							    		<label style="margin-left:23px;" class="margin-top-5">开行规律</label>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        普通规律:
				                                        </div>
				                                    <div class="pull-left" id="normalRule2"></div>
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        指定星期:</div>
				                                    <div class="pull-left" id="week2"></div>
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        指定日期:
				                                    </div>
				                                    <div class="pull-left" id="date2"></div>
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        指定周期:
				                                    </div>
				                                    <div class="pull-left" id="cycle2"></div>
				                                </div>
							    	</div>
							    	<div class="col-md-4">
							    		<div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        运行区段：
				                                    </div>
				                                    <div class="pull-left" id="runSection2">
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        运行距离：
				                                    </div>
				                                    <div class="pull-left" id="runDistance2">
				                                        
				                                    </div>
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        等级：
				                                    </div>
				                                    <div class="pull-left" id="level2">
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                   <div class=" pull-left div-header" style="padding-left:30px;">
				                                      对数：
				                                   </div>
				                                <div class="pull-left " id="pairNbr2">
				                                     
				                                </div>
				                            </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        编组辆数：
				                                    </div>
				                                    <div class="pull-left" id="orgnize2">
				                                        
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        定员：
				                                    </div>
				                                    <div class="pull-left" id="createPeople2">
				                                        
				                                    </div>
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        编组内容：
				                                    </div>
				                                    <div class="pull-left" id="orgnizeContent2">
				                                     
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        动车组类型：
				                                    </div>
				                                    <div class="pull-left" id="groupLevel2">
				                                        
				                                    </div>
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        供电：
				                                    </div>
				                                    <div class="pull-left" id="electricity2">
				                                        
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        集便：
				                                    </div>
				                                    <div class="pull-left" id="mass2">
				                                        
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        空调：
				                                    </div>
				                                    <div class="pull-left" id="airconditioner2">
				                                        
				                                    </div>
				                                </div>
				                                <div class="clearfix"></div>
				                            </div>
				                            <div class="margin-top-5 pull-right">
				                                <div class="row">
				                                    <div class="div-header pull-left">
				                                        备注：
				                                    </div>
				                                        <div class="pull-left" style="margin-bottom: 5px;width:250px;height:120px" id="remark2">
				                                        
				                                        </div>
				                                </div>
				                            </div>
							    	</div>
							    </div>
							  </div>
							</div>
	    				</div>
	    			</div>
	    		</div>
	    	</div>
	    </div> 	
    	
    </div>

   <div id="timeTable" style="display:none;">
        <div style="margin:5px; overflow-y:auto; overflow-x:hidden;">
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
	
    <script type="text/javascript">
        
        //验证表单
/*         var log2=document.getElementById("groupData2");
        log2.addEventListener("blur",function(event){
            event=window.event;
            var target=event.srcElement;
            if(/^[1-9]\d{0,}/.test(target.value))
            {
                target.style.borderColor="";
            }
            else
            {
                console.log(target.style.borderColor)
                target.style.borderColor="red";
            }
        },false) */
        
        //车次 回车时触发提交
        // var cmOriginalTrainId=document.getElementById("cmOriginalTrainId");
        // cmOriginalTrainId.addEventListener("keyup",function(event){
        //     event=window.event;
        //     if(event.keyCode==13)
        //     {
        //         searchData(0);
        //     }
        // })
        
    </script>
</body>
</html>