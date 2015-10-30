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
    <link type="text/css" href="${ctx}/assets/css/style.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/html5.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/fuelUX.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery-ui-1.9.2.custom.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/datepicker.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.knob.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.gritter.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.sparkline.min.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/jquery.freezeheader.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/purl.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/knockout.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/routing.js"></script>
    <style>
        #hdrouting table tr th{
            border-color:#dfe4ee;
            text-align:center;
        }
    </style>
</head>
<body>
<div class="container padding0 margin0" style="overflow: hidden; width: 100%;">
    <div class="row padding0 margin0">
        <div class="col-md-12 col-xs-12 col-lg-12 padding0 margin0">
            <div class="panel-body padding0 margin0">
                <div class="table-responsive padding0 margin0">
                    <table id="routing" class="table table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <th class="text-center">序号</th>
                            <th class="text-center">开始站</th>
                            <th class="text-center">结束站</th>
                            <th class="text-center">线路名</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        <tr>
                            <td class="text-center">1</td>
                            <td class="text-center">成都</td>
                            <td class="text-center">12:00</td>
                            <td class="text-center">12:04</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>