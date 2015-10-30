
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>审核次日客车运行线</title>
    <link type="text/css" href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/font-awesome.min.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/minified/jquery-ui.min.css" rel="stylesheet"/>
    <link type="text/css" href="${ctx}/assets/css/style.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/html5.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/fuelUX.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.knob.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.gritter.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.sparkline.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.freezeheader.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/minified/jquery-ui.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/datepicker.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/purl.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/knockout.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/moment.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/audit.js"></script>
    <style>
        #hdleft_table table tr th{
            border-color:#dfe4ee;
            text-align:center;
        }
        .buttonlines {
            width: 100px;
            border-radius: 4px;
            margin-right: 10px
        }
    </style>
</head>
<body class="Iframe-body" style="margin-right:-5px">
<ol class="breadcrumb">
    <span><i class="fa fa-anchor"></i>当前位置：</span>
    <li><a href="#">审核次日客车运行线</a></li>
</ol>
<section class="mainContent">
    <div class="row">
        <div class="col-xs-12 col-md-12 col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-home"></i>审核
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-12 col-md-12 col-lg-12">
                            <form class="form-inline" role="form">
                                <div class="input-group">
                                    <input type="text" class="form-control" style="width: 100px; margin-right: 10px; border-radius: 4px" placeholder="请选择日期" id="date_selector"/>
                                    <button href="#" class="btn btn-primary" style="width: 100px; margin-right: 10px; border-radius: 4px" role="button">校验</button>
                                    <select id="bureau" class="form-control" style="width: 100px; margin-right: 10px; border-radius: 4px" data-bind="options: bureauList, optionsText: function(item) {
                                        return item.name
                                    }, value: bureau">
                                    </select>
                                    <button href="#" class="btn btn-primary" style="width: 100px; margin-right: 10px; border-radius: 4px" role="button">生成运行线</button>
                                    <button href="#" class="btn btn-primary" style="width: 100px; margin-right: 10px; border-radius: 4px" role="button">客调审核</button>
                                    <button href="#" class="btn btn-primary disabled" style="width: 100px; margin-right: 10px; border-radius: 4px" role="button">值班主任审核</button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="row paddingtop5">
                        <div class="panel panel-default marginbottom0">
                            <div class="panel-body paddingbottom0">
                                <p>客运计划共709条，其中停运8条，热备52条，需下达709条。</p>
                                <p>已生成运行线709条，未生成0条，调整0条。</p>
                                <p>客调已审核709条，值班主任已审核12条。</p>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-md-12 col-lg-12 paddingleft0 paddingtop5">
                            <div class="table-responsive">
                                <table id="left_table" class="table table-bordered table-striped table-hover tableradius">
                                    <thead>
                                    <tr>
                                        <th rowspan="2" class="text-center"><input class="checkbox-inline" type="checkbox"/></th>
                                        <th rowspan="2" class="text-center">序号</th>
                                        <th rowspan="2" class="text-center">车次</th>
                                        <th rowspan="2" class="text-center">来源</th>
                                        <th rowspan="2" class="text-center">始发站</th>
                                        <th rowspan="2" class="text-center">始发时间</th>
                                        <th rowspan="2" class="text-center">终到站</th>
                                        <th rowspan="2" class="text-center">终到时间</th>
                                        <th colspan="3" class="text-center">校验项</th>

                                    </tr>
                                    <tr>
                                        <th class="text-center">列车</th>
                                        <th class="text-center">时刻</th>
                                        <th class="text-center">经由</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="text-center"><input class="checkbox-inline" name="plan" type="checkbox"/></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"><a href="#"></a></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"></td>
                                        <td class="text-center"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>

</html>
