<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String basePath = request.getContextPath();
%>
<!-- 每个页面必须引入 -->
<script type="text/javascript" src="<%=basePath%>/assets/js/jquery.js"></script>
<!-- respond.min.js 使得那些不支持CSS3 Media Queryes特性的浏览器能够支持响应性设计 越早运行，ie用户看到非media内容的闪烁的几率越小 -->
<script type="text/javascript" src="<%=basePath%>/assets/js/respond.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/html5.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/bootstrap.min.js"></script>


<script type="text/javascript" src="<%=basePath%>/assets/js/datepicker.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/jquery.gritter.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/knockout.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/js/moment.min.js"></script>




<!-- ================ 		项目公用js		======================== -->
<!-- common.security.js 提供1.禁止直接访问非index页面功能 2.js获取项目路径 -->
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.security.js"></script>
<!-- 提供 消息提示框方法   依赖于jquery.gritter.min.js -->
<script type="text/javascript" src="<%=basePath%>/assets/js/trainplan/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/util/util.js"></script>





