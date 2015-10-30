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
    <script src="${ctx}/assets/js/jquery.js"></script>
    <script src="${ctx}/assets/js/bootstrap.min.js"></script>
    <style type="text/css">
        form p {
            padding-top: 7px;
        }
        #num{
        margin:0 10px;
        }
    </style>
 <script>
    $(function(){
    	
    	$('#myModal').modal();
    	
     	setTimeout("$('#returnLogin').click()",10000); 
    	$("#returnLogin").click(function(){
    		window.location.reload();
    		
//             window.top.location = "http://10.1.132.117:8080/ljdd";
            window.location = "${ctx}/login";
        });
    	
    	
    	$("#returnNew").click(function(){
    		window.location.reload();
    		
//     		window.top.location = "http://10.1.132.117:8080/ljdd";
        });
    	
    	
    /*	var num = [8,7,6,5,4,3,2,1,0];
    	$('#num').html(num[i]);
    	setInterval("$('#num').html(num[i])",1000);
    	
    	for(i=0;i<10;i++){
    		setInterval("$('#num').html(num[i])",1000); 	
    		//$('#num').html(num[i]);
    	}*/
    	var num = [8,7,6,5,4,3,2,1,0];   	
    	var listNum =9;
    	setInterval(function(){
	    	if(listNum){
		    	$("#num").html(listNum-1);
		    	listNum--;
	    	}
    	},1000)
    	
    	
    	
    });
    </script>
</head>
<body class="Iframe_body">
<section class="error"><img src="${ctx}/assets/img/500.png" class="img-responsive">
    <hr>
    <hr>
    <hr>
    <h4>系统错误</h4>
    <h4>点击下面的按钮返回</h4>

    <p>
        <button type="button" class="btn btn-success btn-lg">返回</button>
    </p>
</section>
<div id="returnLogin"></div>


<!-- 弹出 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop='static'>
  <div class="modal-dialog" style="width: 800px;margin-top:10%;">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h4 class="modal-title">系统错误</h4>
      </div>
      <div class="panel-body row">
      <!-- <div style="text-align: center;font-size:18px;margin:20px 0;" class="blue">将会在<span id="num" style="color:#000;"></span>秒后，跳转至登陆页</div> -->
      <div style="text-align: center;font-size:18px;margin:20px 0;" class="blue">将会在<span id="num" style="color:#000;">9</span>秒后，为您跳转至登陆页<span><img alt="" src="/trainplan/assets/img/load2.gif" height="16"></span></div>
	  </div>
      <!--panel-heading-->
      <div style="padding-left:6%;">
	      <!--panel-body-->
	      <div class="modal-footer margin">
	        <button type="button" class="btn btn-primary" id="returnNew">立即跳转</button>
	      </div>
      </div>
      </div>
    <!-- /.modal-content --> 
    </div>
  <!-- /.modal-dialog --> 
  </div>
</body>
</html>
