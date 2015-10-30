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
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/jquery.autocomplete1.js"></script>

<!-- 锁定命令列表table表头 -->
<script type="text/javascript" src="<%=basePath %>/assets/js/chromatable_1.js"></script>

<script type="text/javascript" src="<%=basePath %>/assets/js/knockout.validation.min.js"></script>
<!-- 

<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
--> 
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/station/station.css">

<script type="text/javascript" src="<%=basePath %>/assets/station/station.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/runPlanLk_wd_cmd_add.js"></script>
 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.multiselect2side.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/dateUtil.js"></script>

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
<style>
.control-label{
width:120px;
text-align: right;
}
.form-control1{
width:395px;
}
.control-label2{
width:70px;
}
.ms2side__div select {
width: 195px;
margin:0 11px;
height: 145px;
}
.btn-cancel{
	color: #8D8D8D;
	background-color: #FCFCFC;
	border-color: #B6B6B6;
}
.btn-cancel:hover{
	color: #FFFFFF;
	background-color: #B1B1B1;
	border-color: #9B9B9B;
}
</style>
</head>

<body>

<div class="panel-body row">
        <form class="bs-example form-horizontal" style="margin-top:15px;" data-bind="with: wdCmdTrain">
          <!--  	-->
          
           <div class="form-group">
           <label for="exampleInputEmail2" class="control-label pull-left"  >文电日期：&nbsp;</label>
            <div class="pull-left">
              <input id="input_wd_cmdTime" type="text" class="form-control form-control1"/>
            </div>
            <div class="pull-left"><label style="color:#ff3030;font-size: 20px;margin-left:px;"><b>*</b></label></div>
          </div>
          
           <div class="form-group">
            <label for="exampleInputEmail2" class="control-label pull-left"  >总公司文电号：&nbsp;</label>
            <div class="pull-left">
              <input type="text" class="form-control form-control1" data-bind="value : cmdNbrSuperior"/>
            </div>
          </div>
          
            <div class="form-group">
             <label for="exampleInputEmail2" class="control-label pull-left"  >路局文电号：&nbsp;</label>
            <div class="pull-left">
              <input type="text" class="form-control form-control1" data-bind="value : cmdNbrBureau"/>
              <!-- 
              <span data-bind="validationMessage: cmdNbrBureau"></span>
               -->
            </div>
            <div  class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
          </div>
          
          <div class="form-group">
        <div class="pull-left"  style="width:295px">
         <label for="exampleInputEmail2" class="control-label pull-left" >项号：&nbsp;</label>
			 
			 <div class="pull-left">
		        <input type="text" class="form-control" style="width:150px;" data-bind="value : cmdItem"/>
			</div>
			<div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
		</div>
		<div class="pull-left">
		<label for="exampleInputEmail2" class="control-label control-label2 pull-left">车次：&nbsp;</label>
		    <div class="pull-left">
		        <input type="text" class="form-control" style="width:150px;"  data-bind="value : trainNbr"/>
			</div>
			<div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
		</div>
		</div>
		
		 <div class="form-group">
        <div class="pull-left"  style="width:295px">
		<label for="exampleInputEmail2" class="control-label pull-left"  >始发站：&nbsp;</label>
			 
			 <div class="pull-left" >
		       <!--  <input type="text" class="form-control" style="margin-left:13px;width:150px;" data-bind="value : startStn"/> -->
		        
		        
 <input name="ddlOrgCity" style="width:150px;" id="ddlOrgCity" type="text" data-bind="value : startStn,event:{focus: stnNameTempOnfocus}"  class="form-control">
		        
			</div>
			<div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
		</div>
		<div class="pull-left">
		<label for="exampleInputEmail2" class="control-label control-label2 pull-left">终到站：&nbsp;</label>
			 
			 <div class="pull-left">
		       <!--  <input type="text" class="form-control" style="margin-left:13px;width:150px;" data-bind="value : endStn"/> -->
		        
		         <input style="width:150px;" name="ddlOrgCity1" id="ddlOrgCity1" type="text" data-bind="value : endStn,event:{focus: stnNameTempOnfocus}" class="form-control">
			</div>
			<div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
		</div>
		</div>
	<div class="form-group">
        <div class="pull-left"  style="width:295px">
		<label for="exampleInputEmail2" class="control-label pull-left"  >开始日期：&nbsp;</label>
		    <div class="pull-left">
		        <input id="input_wd_startDate" type="text" class="form-control" style="width:150px;"/>
		        <!--  
                <span data-bind="validationMessage: startDate"></span>
                -->
			</div>
			<div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
		</div>
		<div class="pull-left">
		<label for="exampleInputEmail2" class="control-label pull-left control-label2" >截至日期：&nbsp;</label>
			 
			 <div class="pull-left">
		        <input id="input_wd_endDate" type="text" class="form-control" style="width:150px;" data-bind="disable: booleanValue" />
                <!-- 
               <span data-bind="validationMessage: endDate"></span>
                 -->
			</div>
			
			<div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
			 
			
		</div>
		                    
</div>
         
          
          <div class="form-group">
            <div class="pull-left" style="width:295px">
            <label for="exampleInputEmail2" class="control-label pull-left">开行规律：&nbsp;</label>
           <div class="pull-left">
              <select class="form-control" style="width: 90px;"
					 data-bind="options: wdCmdRuleArray, value: wdCmdRuleOption, optionsText: 'text', event: { change: $root.selectorChange }"><!-- event: { mouseover: $root.selectorChange, mouseup: $root.selectorChange }, -->
			  </select>	
			</div>  
			 <div class="pull-left"><label style="color:#ff3030;font-size: 20px;"><b>*</b></label></div>
			 <div class="pull-left"  style="margin:2px 0 0 10px;">
			  <button type="button" data-bind="click: $root.btnLoadSelectDate, enable: wdCmdRuleOption().code == '3' && !booleanValue()">加载</button> 
              </div>
         </div>
          <div class="pull-left">
          <label for="exampleInputEmail2" class="control-label control-label2 pull-left"  >重点车：&nbsp;</label>
           <div class="pull-left">
              <select class="form-control" style="width: 150px;"
					 data-bind="options: importArray, value: importOption, optionsText: 'text', event: { change: $root.importChange }"><!-- event: { mouseover: $root.selectorChange, mouseup: $root.selectorChange }, -->
			  </select>	
			</div>
            </div>
          </div>
       
<!--  -->
          <div class="form-group">
			     <div id="sel" style="text-align:left;margin-left:50px">
			      <select name="liOption[]" id='liOption' multiple='multiple' size='8'>
			      
			      </select>
			     </div>
			     
          </div>
      
           
        
          <div class="form-group">
             <label for="exampleInputEmail2" class="control-label pull-left">择日日期：</label>
            <div class="pull-left">
            <textarea rows="3" id="textarea" class="form-control1 form-control" readonly cols="55"  data-bind="value : $parent.wdCmdTrain().selectedDate, enable: $parent.wdCmdTrain().wdCmdRuleOption().code == '3' && !$parent.wdCmdTrain().booleanValue()"></textarea>      
            </div>
           
          </div>
      </form>
        
	  </div>
	   
	  <div style="text-align:center;margin-left:40px">
	  
        <button type="button" class="btn btn-primary" data-dismiss="modal" data-bind="click: btnEventSaveWdCmdTrain">保存</button>
        <button type="button" class="btn btn-info" data-bind="click: btnEventResetWdCmdTrain"  style="margin-left:10px">清空全部信息</button>
        <button type="button" class="btn btn-cancel"  data-bind="click: btnEventCleanWdInfo" style="margin-left:10px">清空列车信息</button>
        <!--  
        <button type="button" class="btn btn-warning" data-dismiss="modal" id="btnEventCancelWdCmdTrain">取消</button>
      -->
      </div>
 
</body>

</html>