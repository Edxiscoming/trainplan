
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
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/common.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/crossCheck.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
    <script type="text/javascript" src="<%=basePath%>/assets/js/bootstrap.min.js"></script>
    
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

    </style>
    
</head>
<body>
	<div style="margin:5px; overflow-y:auto; overflow-x:hidden;">
        <ul id="myTab" class="nav nav-tabs" role="tablist">
            <li class="active"><a href="#selfToken" role="tab" data-toggle="tab" id="selfTab">本局担当</a></li>
            <li><a href="#" role="tab" data-toggle="tab" id="otherTab">外局担当</a></li>
        </ul>
        <!-- 选项卡面板 -->
        <div id="myTabContent" class="tab-content" >
            <div class="tab-pane fade in active" id="selfToken">
                <div class="container-fluid">
			        <div class="row" style="padding: 15px 0px;background: #f9f9f9; border-bottom: solid 1px #E0E0E0;">
			            <div class="col-xs-12">
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
			                <div class="pull-left" style="margin-left:10px;margin-top:5px">
								<input type="checkbox" id="checkFlag" checked>模糊
							</div>
			                <a type="button" id="selfSearchBtn" class="btn btn-default blueBtn btn-sm pull-left" style="margin-left: 20px;" onclick="searchData(0);"><span class="glyphicon glyphicon-search" style="margin-right:5px;"></span>查询</a>
			                <a type="button" id="otherSearchBtn" class="btn btn-default blueBtn btn-sm pull-left" style="margin-left: 20px; display:none;" onclick="searchData(1);"><span class="glyphicon glyphicon-search" style="margin-right:5px;"></span>查询</a>
			                
			                <button type="button" id="crossBtn" class="btn btn-default blueBtn btn-sm pull-right" style="height:30px;width:90px; margin-right:3px;background:#27bdaf" onclick="generateCross();"><span class="glyphicon glyphicon-random" style="margin-right:5px;"></span>生成交路</button>
			            </div>
			        
			        </div>
			        
			        <div class="row" style="margin-top: 15px;">
			            <div class="col-xs-12 left" style="height:800px;padding-left:15px;">
			                <table class="easyui-datagrid" id="mcrossTable">
			                </table>
			            </div>
			            <div class="pull-right slideblock" id="selfBlock" style="height: 800px;width:400px;overflow-y: auto;margin-right: 15px;float:right;display:none;z-index:1000; position:absolute;top:80px;right:10px;">
			                <div class="panel panel-default">
				                <div style="height: 45px" class="panel-heading">
				                    <div style="margin-top:5px" class="pull-left"> <span onclick="closeSlideBlock();" class="closeSlide">&gt;</span> 对数表信息</div>
				                    <a type="button" onclick="closeSlideBlock();" class="btn btn-default blueBtn btn-sm pull-right hidden" style="margin-left: 10px;" name="operationBtn">取消</a>
				                    <a class="btn btn-default greenBtn btn-sm pull-right hidden" type="button" style="margin-left: 10px;" onclick="changeData();" name="operationBtn">
				                        保存
				                    </a>
				                    <a class="btn btn-default greenBtn btn-sm pull-right" type="button" onclick="openGraph();" >
				                        预览交路
				                    </a>
				                </div>
				                <div class="container-fluid" style="border: solid 1px #E0E0E0;">
				                     <form data-bind="with: currentCross" role="form" class="form-horizontal" id="update">
				                            <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none;">
				                            <input class="form-control input-sm" style="display:none" value=""  type="text" name="cmOriginalCrossId" id="cmPartOriginalCrossId2">
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
				                                        京局
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                   <div class=" pull-left div-header" style="padding-left:30px;">
				                                    组数：
				                                   </div>
				                                <div class="pull-left ">
				                                    <input class="form-control input-sm hidden" id="groupData2" pattern="^[\d.]{1,}$" style="width: 100px;"  type="text" name="groupTotalNbr" >
				                                    <span class="hidden" id="groupData2Span"></span>
				                                </div>
				                               </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        开行状态：
				                                    </div>
				                                    <div class="pull-left" id="openState2">
				                                        开行
				                                    </div>
				
				                                </div>
				                                <div class="row margin-top-5">
				                                    <div class=" pull-left div-header">
				                                        类型：
				                                    </div>
				                                    <div class="pull-left" id="mold2">
				                                        普速
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
				                                <div class="clearfix"></div>
				                            </div>
				                            <div style="">
				                                <label style="" class="margin-top-5">开行规律</label>
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
				                                <div class="clearfix"></div>
				                            </div>
				                            <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none;">
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
				                            <div class="margin-top-5">
				                                <div class="row">
				                                    <div class="div-header pull-left">
				                                        备注：
				                                    </div>
				                                        <div class="pull-left" style="margin-bottom: 5px;width:250px;height:120px" id="remark2">
				                                        
				                                        </div>
				                                </div>
				                            </div>
				
				                        </form>
				                </div>
			            	</div>
			            </div>
			          </div>
			    </div>  
            </div>
	        <div class="tab-pane fade" id="otherTokeb">

	        </div>
    	</div>
    </div>

    <!-- 外局担当“修改” -->
    <div class="pull-right slideblock " id="crossBlock" style="height: 800px;width:700px;overflow-y: auto;margin-right: 15px;float:right;display:none;z-index:999; position:absolute;top:80px;right:10px;">
            <div class="panel panel-default">
                <div class="panel-heading" style="height:40px;">
                    <div style="margin-top:5px" class="pull-left"> <span onclick="closeSlideBlock();" class="closeSlide">&gt;</span> 交路信息</div>
                    <a type="button" class="btn btn-default blueBtn btn-sm pull-right closeBlock" style="margin-left: 20px; margin-top:-5px;" onclick="closeSlideBlock();">取消</a>
                    <a class="btn btn-default greenBtn btn-sm pull-right" type="button" style="margin-left: 20px; margin-top:-5px;" onclick="saveCross();">保存</a>
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
				                <input id="searchKey" type="text" name="searchKey" onkeyup="upperCase(this.id)" class="form-control input-sm pull-left" style="width:150px; margin-right:30px;"/>
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
                            <input class="form-control inputColor input-sm" style="width: 250px; margin-left:5px;" id="crossName" disabled>
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

    <div id="basicalGraph" title="运行图" style="display:none;">
		<iframe src="" width="99%" height="99%" scroll="no"></iframe>
    </div>
    <div id="shadowBlock" style="width:100%;height:100%;z-index:100;background:#ccc;opacity:0.40;display:none;position:absolute;top:0px;left:0px;">
        <div style="background:#fff;border:1px solid #ccc;border-radius:5px; margin: auto; width:200px;text-align:center;">正在生成交路.....</div>
    </div>
    <div id="shadowWindow" style="width:100%;height:100%;z-index:100;background:#ccc;opacity:0.40;display:none;position:absolute;top:0px;left:0px;">
    </div>
	
    <script type="text/javascript">
        
        //验证表单
        var log2=document.getElementById("groupData2");
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
        },false)
        
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