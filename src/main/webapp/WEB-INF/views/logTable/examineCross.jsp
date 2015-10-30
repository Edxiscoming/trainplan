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
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/assets/css/logTable/mainCross.css">
    <script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/examineCross.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/logTable/common.js"></script>
	<script type="text/javascript" src="<%=basePath%>/assets/js/bootstrap.min.js"></script>
    <title>核对交路单元</title>
    <style type="text/css">
    	.nav-pills > li{
			color: #333;
		  	background-color: #cccccc;
		}
		.nav-pills > li.active > a,
		.nav-pills > li.active > a:hover,
		.nav-pills > li.active > a:focus {
		  color: #fff;
		  background-color: #666666;
          font-weight: bold;
		}
		th, td {
			text-align: center;
		}
        .nav-pills > li > a{
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
        .nav-pills>li>a{
        	border-radius: 0px;
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
    <div class="container-fluid" >
        <div class="pull-left col-md-5" style="height:800px; padding-left:0px;">
            <div class="col-md-12" style="padding: 15px 0px;">
                <span class="pull-left" style="margin-top:7px;">担当局：</span>
                <div class="pull-left">
                    <select style="padding: 1px 5px;; width: 60px;"
                        id="input_cross_chart_id" class="form-control input-sm">
                        <option value="">全部</option>
                    </select>
                </div>
                <input type="checkbox" id="selfRelevant" class="pull-left" style="margin-left:20px; margin-top:11px;">
                <span class="pull-left" style="margin-left:5px; margin-top:7px;">本局相关</span>
                <span class="pull-left" style="margin-top:7px; margin-left:15px;">方案：</span>
                <div class="pull-left">
                    <select style="padding: 1px 8px;; width: 250px; color:#e60302; font-weight:normal;"
                        id="cmVersionId" class="form-control input-sm">
                        <option value="">20150520__0519导入</option>
<!--                         <option value="">20150520__0505导入</option> -->
<!--                         <option value="">20150320__0317导入</option> -->
<!--                         <option value="">20150204__2月春运图_0204导入</option> -->
<!--                         <option value="">20150201__2月春运图_0131导入</option> -->
                    </select>
                </div>
                <a type="button" class="btn btn-default blueBtn btn-sm pull-right" style="margin-left: 20px;" onclick="searchData();"><span class="glyphicon glyphicon-search" style="margin-right:5px;"></span>查询</a>
                <input type="text" class="input-sm form-control pull-right" id="trainNo" style="width: 100px;" onkeyup="upperCase(this.id)">
                <span class="pull-right padding-5" style="margin-left: 10px; margin-top:7px;">车次：</span>
            </div>
            <div class="col-md-12" style="margin-left:-15px;height:800px;">
                <table id="crossInfos" >
                    
                </table>
            </div>
        </div>
        <div class="pull-right col-md-7" style="padding-right:0px;">
            <div class="row" style="height:550px; width:100%; border:1px solid #ccc;">
                <!-- 选项卡组件（菜单项nav-pills）-->
                <ul id="myTab" class="nav nav-pills" role="tablist" style="background-color:#f5f5f5;">
                    <li class="active"><a href="#crossGraph" role="tab" data-toggle="pill">运行图</a></li>
                    <li><a href="#crossInfo" role="tab" data-toggle="pill">交路信息</a></li>
                </ul>
                <!-- 选项卡面板 -->
                <div id="myTabContent" class="tab-content" style="height:500px; overflow-y:auto; overflow-x:hidden;">
                    <div class="tab-pane fade in active" id="crossGraph" style="height:99%;">
                        <!-- <div onclick="fullScreen();"><button class="btn btn-default greenBtn btn-xs pull-right" style="margin-right:10px; positon:absolute; right:10px; top:20px;">全屏</button></div> -->
                    	<iframe src="" width="100%" height="100%" scroll="no"></iframe>
                    </div>
                    <div class="tab-pane fade" id="crossInfo">
                        <form role="form" class="form-horizontal">
                        <div style="margin-left:30px; border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    车底交路：
                                </div>
                                <div class="pull-left" id="plan_construction_input_trainNbr">
                                    
                                </div>
                            </div>

                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:14px;">
                                    担当局：
                                </div>
                                <div class="pull-left" id="undertake">
                                    
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    组数：
                                </div>
                                <div class="pull-left" id="groupData">
                                    
                                </div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    开行状态：
                                </div>
                                <div class="pull-left" id="openState">
                                    
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    类型：
                                </div>
                                <div class="pull-left" id="mold">
                                    
                                </div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    交替日期：
                                </div>
                                <div class="pull-left" id="relaceDate">
                                   
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    交替车次：
                                </div>
                                <div class="pull-left" id="replace_train_number">
                                   
                                </div>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div style="margin-left:30px;">
                            <label style="" class="margin-top-5">开行规律</label>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    普通规律：
                                    </div>
                                <div class="pull-left" id="normalRule"></div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    指定星期：</div>
                                <div class="pull-left" id="week"></div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    指定日期：
                                </div>
                                <div class="pull-left" id="date"></div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    指定周期：
                                </div>
                                <div class="pull-left" id="cycle"></div>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div style="margin-left:30px; border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none;">
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    运行区段：
                                </div>
                                <div class="pull-left" id="runSection">
                                   
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    运行距离：
                                </div>
                                <div class="pull-left" id="runDistance">
                                    
                                </div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    等级：
                                </div>
                                <div class="pull-left" id="level">
                                    
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    对数：
                                </div>
                                <div class="pull-left" id="log">
                                    
                                </div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    编组辆数：
                                </div>
                                <div class="pull-left" id="orgnize">
                                    
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    定员：
                                </div>
                                <div class="pull-left" id="createPeople">
                                    
                                </div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header">
                                    编组内容：
                                </div>
                                <div class="pull-left" id="orgnizeContent">
                                    
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:-14px;">
                                    动车组类型：
                                </div>
                                <div class="pull-left" id="groupLevel">
                                    
                                </div>
                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    供电：
                                </div>
                                <div class="pull-left" id="electricity">
                                    
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    集便：
                                </div>
                                <div class="pull-left" id="mass">
                                    
                                </div>

                            </div>
                            <div class="row margin-top-5">
                                <div class=" pull-left div-header" style="margin-left:28px;">
                                    空调：
                                </div>
                                <div class="pull-left" id="airconditioner">
                                    
                                </div>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div>
                            <div class="row margin-top-5" style="margin-left:15px;">
                                <div class="div-header pull-left" style="margin-left:28px;">
                                    备注：
                                </div>
                                <div class="pull-left" id="remark" style="margin-bottom: 5px; width:270px; height:50px;">
                                    
                                </div>
                            </div>
                        </div>

                    </form>
                    </div>
                </div>

            </div>
            <div class="row">
			<div class="pull-left" style="width: 97%; height:500px; margin-top:20px;">
				<section class="panel panel-default">
					<!-- 选项卡组件（菜单项nav-pills）-->
					<ul id="myTab" class="nav nav-pills" role="tablist" style="background-color:#f5f5f5;">
					    <li class="active"><a href="#bulletin" role="tab" data-toggle="pill" id="crossInfoTab">列车信息</a></li>
					    <!-- <li><a href="#rule" role="tab" data-toggle="pill" id="showcrosstrainTab">列车信息（对）</a></li> -->
					    <li class="pull-right" style="background-color:#f5f5f5; margin-right:10px;"><a type="button" style="padding: 5px 15px;margin-top: 5px;" class="btn btn-default blueBtn btn-sm" data-toggle="modal" onclick="timeOpen();">
							<span class="glyphicon glyphicon-list" style="margin-right:5px;"></span>时刻表
						</a></li>
					</ul>
					<!-- 选项卡面板 -->
					<div id="myTabContent" class="tab-content">
					    <div class="tab-pane fade in active" id="bulletin">
					    <div class="panel-body" style="overflow-y: auto; height:320px;">
                        <input type="text" id="crossHideId" style="display:none;">
                        <input type="text" id="trainNumber" style="display:none;">
                        <input type="text" id="startStation" style="display:none;">
                        <input type="text" id="endStation" style="display:none;">
						<div class="table-responsive" style="height:220px;">
							<table id="cross_trainInfo">
							</table>
						</div>
					</div>
					    </div>
					    <div class="tab-pane fade" id="rule">
					    	<div class="panel-body" style="overflow-y:auto; height:320px;">
						<div class="table-responsive" style="height:220px;">							
							<table id="unti_trainInfo">
								
						    </table>
					</div>
					</div>
					    </div>
					</div>
				</section>
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
                    <div class="panel-body" style="height:700px; width:1000px;">
                    <table class="" id="timedetailinfo">
                    
                    </table>
                    </div>
                </div>
                <div class="tab-pane fade" id="xiang">
                    <div class="panel-body" style="height:700px; width:1000px;">
                        <table class="" id="timedetailinfo_detail">
                            
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

   <!--  全屏运行图 -->
   <div id="shadowBlock" style="width:100%;height:100%;z-index:100;background:#ccc;opacity:0.40;display:none;position:absolute;top:0px;left:0px;"></div>

   <div class="slideblock " id="fullScreen" style="height: 1000px;width:1850px;overflow-y: auto;margin:auto;display:none;z-index:999;background-color:#fff;border:1px solid #333; position:absolute;top:0px;right:20px;">
        <!-- <div onclick="closeFull();"><button class="btn btn-default greenBtn btn-xs pull-right">关闭</button></div> -->
       <!-- <div id="crossGraphs" style="height:100%; width:100%;">
            <iframe src="" width="100%" height="100%" scroll="no"></iframe>
        </div> -->
   </div>

    <script type="text/javascript" src="../js/jquery.min.js"></script>
    <script type="text/javascript" src="../js/jquery.bootstrap.js"></script>
    <script type="text/javascript" src="../js/bootstrap.min.js"></script>
</body>
</html>