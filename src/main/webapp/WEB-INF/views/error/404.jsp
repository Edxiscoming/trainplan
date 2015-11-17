<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="author" content="author">
    <title>addRole</title>
    <!-- Bootstrap core CSS -->
    <link type="text/css" href="${ctx}/assets/css/custom-bootstrap.css" rel="stylesheet">
    <!--font-awesome-->
    <link type="text/css" rel="stylesheet" href="${ctx}/assets/css/font-awesome.min.css"/>
    <!-- Custom styles for this template -->
    <link type="text/css" href="${ctx}/assets/css/style.css" rel="stylesheet">
    <style type="text/css">
        form p {
            padding-top: 7px;
        }
    </style>
</head>
<body class="Iframe_body">
<section class="error"><img src="${ctx}/assets/img/404.png" class="img-responsive">
    <hr>
    <hr>
    <hr>
    <h4>你请求的页面不存在</h4>
    <h4>点击下面的按钮返回</h4>

    <p>
        <button type="button" class="btn btn-success btn-lg">返回</button>
    </p>
</section>
</body>
</html>
