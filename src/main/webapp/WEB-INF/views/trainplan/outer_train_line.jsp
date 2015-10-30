<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>冗余运行线</title>
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
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/common.js"></script>
    <script type="text/javascript" src="${ctx}/assets/js/trainplan/common.security.js"></script>
</head>
<body class="Iframe-body">
    <section class="mainContent">
        <div class="row">
            <div class="col-xs-12 col-md-12 col-lg-12 paddingleftright5">
                <div class="table-responsive">
                    <table id="plan_table" class="table table-bordered table-striped table-hover tableradius">
                        <thead>
                        <tr>
                            <th rowspan="2" class="text-center"><input class="checkbox-inline" type="checkbox"/></th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">序号111</th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">车次</th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">来源</th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">运行方式</th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">是否高线</th>
                            <th colspan="2" class="text-center">始发/接入</th>
                            <th colspan="2" class="text-center">终到/交出</th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">上图状态</th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">一审</th>
                            <th rowspan="2" class="text-center" style="vertical-align: middle">二审</th>
                            <th colspan="2" class="text-center">校验项</th>
                        </tr>
                        <tr>
                            <th class="text-center">站名</th>
                            <th class="text-center">时间</th>
                            <th class="text-center">站名</th>
                            <th class="text-center">时间</th>
                            <th class="text-center">列车</th>
                            <th class="text-center">时刻</th>
                            <%--<th class="text-center">经由</th>--%>
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
    </section>
</body>
</html>