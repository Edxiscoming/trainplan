<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增文电加开命令</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath %>/assets/css/table.scroll.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/jquery.multiselect2side.css" />
<!-- 自动补全插件 -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/css/jquery.autocomplete.css">
<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
<!-- 自动补全插件 -->
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/jquery.autocomplete.js"></script>

<!-- 锁定命令列表table表头 -->
<script type="text/javascript" src="<%=basePath %>/assets/js/chromatable_1.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.validation.min.js"></script>
<!-- 

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
--> 
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/runPlanLk_cz_edit_jy.js"></script>
 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.multiselect2side.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/dateUtil.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/validate.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/common.js"></script>
<script src="<%=basePath %>/assets/js/moment.min.js"></script>
  <!-- 
<script type="text/javascript">
$(function(){
   $("#liOption").multiselect2side({
	    selectedPosition: 'right',
	    moveOptions: false,
		labelsx: '待选区',
		labeldx: '已选区'
   });
});
</script>
 -->

</head>

<body>

<!--新增/修改弹出框-->
<div class="panel-body row">
      <!--panel-heading-->
       <div class="panel-body row">
        <form id="hightLineCrewForm" class="bs-example form-horizontal" style="margin-top:10px;" data-bind="with : hightLineModel">
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">乘务交路：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewCross" type="text" class="form-control" data-bind="value : crewCross">
            </div>
                <label style="color:#ff3030;font-size: 20px;margin-left:-5px"><b>*</b></label>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">乘务组编号：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_crewGroup" type="text" class="form-control" data-bind="value : crewGroup,event:{focus: crewGroupOnfocus}">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">经由铁路线：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_throughLine" type="text" class="form-control" data-bind="value : throughLine,event:{focus: throughLineOnfocus}">
               </div>
          </div>
          
          
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">车长姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_name1" type="text" class="form-control" data-bind="value : name1">
               </div>
                <label style="color:#ff3030;font-size: 20px;margin-left:-5px"><b>*</b></label>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_tel1" type="text" class="form-control" data-bind="value : tel1">
               </div>
                <label style="color:#ff3030;font-size: 20px;margin-left:-5px"><b>*</b></label>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_identity1" type="text" class="form-control" data-bind="value : identity1,event:{focus: identity1Onfocus}">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">业务员姓名：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_name2" type="text" class="form-control" data-bind="value : name2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">电话：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_tel2" type="text" class="form-control" data-bind="value : tel2">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">政治面貌：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <input id="add_identity2" type="text" class="form-control" data-bind="value : identity2,event:{focus: identity2Onfocus}">
               </div>
          </div>
          <div class="form-group">
            <label class="col-md-3 col-sm-4 col-xs-4 control-label text-right">备注：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
              <textarea id="add_note" class="form-control" rows="4" data-bind="value : note"></textarea>
            </div>
          </div>
        </form>
        <!--        <p class="pull-right" style="margin:0;">说明：当您申请后需要等待管理员审批才能使用。</p>
--> </div>
      <!--panel-body-->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bind="click : saveHightLineCrew" data-dismiss="modal">确定</button>
       <!-- <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button> --> 
      </div>
    <!-- /.modal-content --> 
  <!-- /.modal-dialog --> 
</div>
<!--新增/修改弹出框 end-->

 
</body>

</html>