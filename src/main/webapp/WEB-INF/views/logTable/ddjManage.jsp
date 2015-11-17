<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="../easyUI/themes/easyUI/easyui.css">
    <link rel="stylesheet" type="text/css" href="../style/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../style/cross/importCrossData.css">
    <script src="../js/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script><!--easyUI-->

    <title>担当局管理</title>
        <script type="text/javascript">
        function toggelDetail(){
            $("#detailPanel").toggle("slow");
        }
        function openGraph(){
            $('#crossGraph').dialog({
                title: '交路图',
                content:"<img src='crossGraph.png' alt='交路图'/>",
                width: 800,
                height: 600,
                cache: false,
                modal: true
            });
            $('#crossGraph').dialog('open');
        }
        
		$(document).ready(function(){
        	$(".trigger").click(alr);
        	$(".closeBlock").click(closeBlock);
        	var slideblock=$(".slideblock");
        	var left=$(".left");
        	var is_on=false;//默认是关闭
        	
        	function alr(){
        		if(is_on){
        		//换数据
        		$("#note")[0].innerText="我是假装换掉的数据";
        		}
        		else{
        		//打开并显示当前行数据
        			slideblock.animate({width:"400px"},500);
        			left.animate({width:"1455px"},500);
        			is_on=true;
        		}
        	}
        	function closeBlock(){
        		slideblock.animate({width:"0px"},500);
        		left.animate({width:"98%"},500);
        		is_on=false;
        	}
        	 
       })
    </script>
    <style type="text/css">
    .slideblock{
    	width:0px;
    }
    .left{
    	width:98%;
    }
    </style>
    
    </script>
</head>
<body>
    <div class="container-fluid" >
        <div class="row" style="padding: 15px 0px;background: #f9f9f9; border-bottom: solid 1px #E0E0E0;">
        	<div class="col-xs-1">
                <div class="pull-right">
                    <span class="pull-left padding-5">方案：</span>
                    <span class="pull-left padding-5">20150901</span>
                </div>

            </div>
        	 <div class="col-xs-8">
                <span class="pull-left padding-5">车次：</span><input type="text" class="input-sm form-control pull-left" style="width: 200px;">
                <span class="pull-left padding-5" style="margin-left: 30px;">开行状态：</span>
                <select class="form-control input-sm pull-left" style="width: 80px;">
                    <option value="volvo">开行</option>
                    <option value="saab">备用</option>
                    <option value="opel">停运</option>
                </select>
                <span class="pull-left padding-5" style="margin-left: 30px;">类型：</span>
                <select class="form-control input-sm pull-left" style="padding:1px 8px;width: 80px">
                    <option value="-1">全部</option>
                    <option value="0">既有</option>
                    <option value="1">高线</option>
                    <option value="2">混跑</option>
                </select>
                <a type="button" class="btn btn-default greenBtn btn-sm pull-left" style="margin-left: 20px;">查询</a>


                 <a type="button" class="btn btn-default greenBtn btn-sm pull-right" >批量审核</a>
                
                
            </div>
            <div class="col-xs-3"></div>
        </div>
        <div class="row" style="margin-top: 15px;">
            <div class="pull-left left" style="height:800px;margin-left: 30px;">
                <div class="col-xs-12">
                    <div style="line-height:24px;" class="pull-left">
                        <span class="pull-left">共<span>4</span>条&nbsp;&nbsp;当前<span>1</span>到<span>4</span>条&nbsp;&nbsp;共<span>1</span>页</span>
                    </div>
                    <div style="padding:0px;" class="pull-right">
                       <span >
                            <a>&lt;&lt;</a>
                            <input type="text" style="width: 20px;" value="1">
                            <a>&gt;&gt;</a>
                       </span>
                    </div>
                </div>
                <table class="table table-striped table-bordered table-hover table-condensed table-center trigger">
                    <colgroup>
                        <col width="3%">
                        <col width="3%">
                        <col width="13%">
                        <col width="15%">
                        <col width="5%">
                        <col width="5%">
                        <col width="5%">
                        <col width="4%">
                        <col width="4%">
                        <col width="4%">
                        <col width="6%">

                    </colgroup><thead>
                <tr>
                    <th><input type="checkbox" value="1" style="margin-top:0" id="CheckAll"></th>
                    <th> 序号</th>
                    <th>车底交路</th>
                    <th>交替日期</th>
                    <th>开行状态</th>
                    <th>组数</th>
                    <th>对数</th>
                    <th>担当局</th>
                    <th>数据异常</th>
                    <th>审核状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                    <tbody>
                    <tr>
                        <td ><input type="checkbox" name="subBox"></td>
                        <td>1</td>
                        <td>K15-K16</td>
                        <td>20150923</td>
                        <td>开行</td>
                        <td>3</td>
                        <td>1</td>
                        <td>京</td>
                        <td><div class="btn btn-default btn-xs greenBtn">无</div></td>
                        <td><div class="btn btn-default btn-xs redBtn">未</div></td>
                        <td><div class="btn btn-default btn-xs blueBtn">通</div>
                        	<div class="btn btn-default btn-xs BredBtn">不</div></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="subBox"></td>
                        <td>2</td>
                        <td>K15-K16</td>
                        <td>20150923</td>
                        <td>开行</td>
                        <td>3</td>
                        <td>1</td>
                        <td>京</td>
                        <td><div class="btn btn-default btn-xs greenBtn">无</div></td>
                        <td><div class="btn btn-default btn-xs redBtn">未</div></td>
                        <td><div class="btn btn-default btn-xs blueBtn">通</div>
                        	<div class="btn btn-default btn-xs BredBtn">不</div></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="subBox"></td>
                        <td>3</td>
                        <td>K15-K16</td>
                        <td>20150923</td>
                        <td>开行</td>
                        <td>3</td>
                        <td>1</td>
                        <td>京</td>
                        <td><div class="btn btn-default btn-xs greenBtn">无</div></td>
                        <td><div class="btn btn-default btn-xs redBtn">未</div></td>
                        <td><div class="btn btn-default btn-xs blueBtn">通</div>
                        	<div class="btn btn-default btn-xs BredBtn">不</div></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="subBox"></td>
                        <td>4</td>
                        <td>K15-K16</td>
                        <td>20150923</td>
                        <td>开行</td>
                        <td>3</td>
                        <td>1</td>
                        <td>京</td>
                        <td><div class="btn btn-default btn-xs greenBtn">无</div></td>
                        <td><div class="btn btn-default btn-xs redBtn">未</div></td>
                        <td><div class="btn btn-default btn-xs blueBtn">通</div>
                        	<div class="btn btn-default btn-xs BredBtn">不</div></td>
                    </tr></tbody>
                </table>
            </div>
            <div class="pull-right slideblock" style="height: 800px;overflow-y: auto;margin-right: 15px;">
                <div style="height: 40px" class="panel-header">
                    <div style="margin-top:5px" class="pull-left"> 对数表信息</div>
                   <!-- <a type="button" class="btn btn-default greenBtn btn-sm pull-right" style="margin-left: 20px;">生成交路</a>-->
                   <a type="button" class="btn btn-default greenBtn btn-sm pull-right closeBlock" style="margin-left: 20px;"><span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span></a>
                    <a type="button" class="btn btn-default greenBtn btn-sm pull-right" style="margin-left: 20px;" onclick="openGraph();">预览交路</a>
                </div>
                <div class="container-fluid" style="border: solid 1px #E0E0E0;">
                    <form data-bind="with: currentCross" role="form" class="form-horizontal">
                            <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: none none dashed none; ">
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        车底交路：
                                    </div>
                                    <div class="pull-left">
                                        K15-K16
                                    </div>
                                </div>

                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        担当局：
                                    </div>
                                    <div class="pull-left">
                                        京局
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        组数：
                                    </div>
                                    <div class="pull-left">
                                        4
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        开行状态：
                                    </div>
                                    <div class="pull-left">
                                        开行
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        类型：
                                    </div>
                                    <div class="pull-left">
                                        普速
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        交替日期：
                                    </div>
                                    <div class="pull-left">
                                        2015-09-22
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        交替车次：
                                    </div>
                                    <div class="pull-left">
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
                                    <div class="pull-left">1</div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        指定星期:</div>
                                    <div class="pull-left">3</div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        指定日期:
                                    </div>
                                    <div class="pull-left">2015-08-01</div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        指定周期:
                                    </div>
                                    <div class="pull-left">110</div>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                            <div style="border-width: 1px; border-color: rgb(204, 204, 204); border-style: dashed none;">
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        运行区段：
                                    </div>
                                    <div class="pull-left">
                                       运行区段2
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        运行距离：
                                    </div>
                                    <div class="pull-left">
                                        2000KM
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        等级：
                                    </div>
                                    <div class="pull-left">
                                        等级1
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        对数：
                                    </div>
                                    <div class="pull-left">
                                        3
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        编组辆数：
                                    </div>
                                    <div class="pull-left">
                                        2
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        定员：
                                    </div>
                                    <div class="pull-left">
                                        900
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        编组内容：
                                    </div>
                                    <div class="pull-left">
                                        编组内容2
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        动车组类型：
                                    </div>
                                    <div class="pull-left">
                                        HXH
                                    </div>
                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        供电：
                                    </div>
                                    <div class="pull-left">
                                        1
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        集便：
                                    </div>
                                    <div class="pull-left">
                                        1
                                    </div>

                                </div>
                                <div class="row margin-top-5">
                                    <div class=" pull-left div-header">
                                        空调：
                                    </div>
                                    <div class="pull-left">
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
                                    <div class="margin-top-5 pull-left" style="margin-bottom: 5px;">
                                        <div id="note" class="margin-top-5 pull-left" style="margin-bottom: 5px;width:250px;">
                                        0.0备注要长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长长
                                    	</div>
                                    </div>
                                </div>
                            </div>

                        </form>
                </div>
            </div>
        </div>
    </div>
    <div id="crossGraph" title="交路图">
    </div>
        <script>
    var checkAll=document.getElementById("CheckAll");
    checkAll.addEventListener("click",function(){
    	var flag=checkAll.checked;    
    	var arr = document.getElementsByName("subBox");
    	var i=0,len=arr.length;
    	for(i=0;i<len;i++)
    	{
    		arr[i].checked=flag;
    	}
    },false);
    
    </script>
</body>
</html>