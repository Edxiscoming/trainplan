<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String basePath = request.getContextPath();
%>

<script type="text/javascript" src="<%=basePath%>/dwr/util.js"></script>
<script type="text/javascript" src="<%=basePath%>/dwr/engine.js"></script>
<script type="text/javascript" src="<%=basePath%>/dwr/interface/PushMsg.js"></script>
<script type="text/javascript" src="<%=basePath%>/dwr/interface/MsgConnection.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/util/dwr.overwrite.js"></script>
<script type="text/javascript">
<!-- -->
dwr.engine.setActiveReverseAjax(true);//js中开启dwr推功能
dwr.engine.setNotifyServerOnPageUnload(true);//设置在页面关闭时，通知服务器销毁session

MsgConnection.register();

</script>


