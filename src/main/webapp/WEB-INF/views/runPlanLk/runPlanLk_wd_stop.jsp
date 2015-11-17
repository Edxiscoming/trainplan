<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增文电停运命令</title>
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

<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/runPlanLk/runPlanLk_wd_cmd_stop.js"></script>
 
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.multiselect2side.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/dateUtil.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<%=basePath%>/assets/station/station.css">
<script type="text/javascript" src="<%=basePath %>/assets/station/station.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/trainplan/jquery.autocomplete1.js"></script>
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

<div class="panel-body row">
        <form class="bs-example form-horizontal" style="margin-top:10px;" data-bind="with: wdCmdTrain">
          <!--  	-->
          
           <div class="form-group">
           <label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">文电日期：&nbsp;</label>
            <div class="col-md-8 col-sm-8 col-xs-8" style="margin-left:26px;">
              <input id="input_wd_cmdTime" type="text" class="form-control"/>
            </div>
            <div class="pull-left"><label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 0px;"><b>*</b></label></div>
            <!--  
            		<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:10px;">发令日期：&nbsp;</label>
		    <div class="pull-left">
		        <input id="input_wd_cmdTime" type="text" class="form-control" style="width:180px;"/>
			</div>
			
			 data-bind="value : trainNbr"
			-->
          </div>
          
           <div class="form-group">
            <label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">总公司文电号：&nbsp;</label>
            <div class="col-md-8 col-sm-8 col-xs-8">
              <input type="text" class="form-control" data-bind="value : cmdNbrSuperior"/>
            </div>
          </div>
          
            <div class="form-group">
             <label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">路局文电号：&nbsp;</label>
            <div class="col-md-8 col-sm-8 col-xs-8" style="margin-left:13px;">
              <input type="text" class="form-control" data-bind="value : cmdNbrBureau"/>
              <!--  
              <span data-bind="validationMessage: cmdNbrBureau"></span>
              -->
            </div>
            <div class="pull-left"><label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 0;float: left;"><b>*</b></label></div>
          </div>
          
          <div class="form-group">
          
         <label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">项号：&nbsp;</label>
			 
			 <div class="pull-left">
		        <input type="text" class="form-control" style="margin-left:26px;width:155px;" data-bind="value : cmdItem"/>
			</div>
			<label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 5px;float: left;"><b>*</b></label>
			

			
		</div>
		
		
		 <div class="form-group">
				<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">车次：&nbsp;</label>
		    <div class="pull-left">
		        <input type="text" class="form-control" style="margin-left:26px;width:155px;"  data-bind="value : trainNbr"/>
			</div>
			<label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 5px;float: left;"><b>*</b></label>
		<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:25px;width:70px;text-align: left;">停运站：&nbsp;</label>
			 
			 <div class="pull-left">		
  <input name="ddlOrgCity" style="width:150px;" id="ddlOrgCity" type="text" data-bind="value : startStn ,event:{focus: stnNameTempOnfocus}"  class="form-control">
 	
			
			
			</div>
			<label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 5px;float: left;"><b>*</b></label>
		</div>
		
	<div class="form-group">
		<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">开始日期：&nbsp;</label>
		    <div class="pull-left">
		        <input id="input_wd_startDate" type="text" class="form-control" style="width:155px;"/>
		        <!--  
                <span data-bind="validationMessage: startDate"></span>
                -->
			</div>
			<label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 5px;float: left;"><b>*</b></label>
		<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:25px;width:70px;text-align: left;">截至日期：&nbsp;</label>
			 
			 <div class="pull-left">
		        <input id="input_wd_endDate" type="text" class="form-control" style="width:150px;" data-bind="disable: booleanValue" />
                <!-- 
               <span data-bind="validationMessage: endDate"></span>
                 -->
			</div>
			
			 <label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 5px;float: left;"><b>*</b></label>
			 
			
		</div>
		                    

         
          
          <div class="form-group">
           <div class="pull-left">
            <label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">停运规律：</label>
           
              <select class="form-control" style="width: 90px;margin-left:5px;margin-top:-10px;display:inline-block;"
					 data-bind="options: wdCmdRuleArray, value: wdCmdRuleOption, optionsText: 'text', event: { change: $root.selectorChange }"><!-- event: { mouseover: $root.selectorChange, mouseup: $root.selectorChange }, -->
			  </select>	
			 <label style="color:#ff3030;font-size: 20px;margin: 3px 0 0 2px;"><b>*</b></label>
			  <button type="button" style="margin-left:10px;" data-bind="click: $root.btnLoadSelectDate, enable: wdCmdRuleOption().code == '3' && !booleanValue()">加载</button> 
               <!-- 
              <span data-bind="validationMessage: rule"></span>
               -->
         </div>
          <!-- <div class="pull-left">
          <label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:50px;">重点车：</label>
           
              <select class="form-control" style="width: 90px;margin-left:5px;margin-top:0px;display:inline-block;"
					 data-bind="options: importArray, value: importOption, optionsText: 'text', event: { change: $root.importChange }">event: { mouseover: $root.selectorChange, mouseup: $root.selectorChange },
			  </select>	
			
            </div> -->
            
          </div>
       
<!--  -->
          <div class="form-group">
			     <div id="sel" style="text-align:left;margin-left:50px">
			      <select name="liOption[]" id='liOption' multiple='multiple' size='8'>
			      
			      </select>
			      <!-- 
			     <button type="button" data-bind="click: btnSubmitSelectDate, enable: wdCmdTrain().wdCmdRuleOption().code == '3' && !wdCmdTrain().booleanValue()">确定</button> 
			      -->
			     </div>
			     
          </div>
           </form>
           
      <div style="text-align:left;margin-left:40px">
      <!--    -->
      
      
	  
	  
	   
	   <!-- 
	   <button type="button" onClick="test();">测试</button> 
	   <button type="button" onClick="submitSelectedDate();">确定</button> 
	    -->
      </div>
        
          <div class="form-group">
             <label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:33px;">停运日期：</label>
            <div class="col-md-7 col-sm-7 col-xs-6">
            <textarea rows="3" id="textarea" readonly cols="55"  data-bind="value : wdCmdTrain().selectedDate, enable: wdCmdTrain().wdCmdRuleOption().code == '3' && !wdCmdTrain().booleanValue()"></textarea>
            <!--  
              <textarea readonly rows="3" cols="42" disable="disable" data-bind="value : selectedDate"></textarea>
          
            
              <input type="text" class="form-control" data-bind="value : cmdNbrSuperior"/>
           -->
      
            </div>
           
          </div>
       
        
	  </div>
	   
	  <div style="text-align:center;margin-left:40px">
	  
        <button type="button" class="btn btn-primary" data-dismiss="modal" data-bind="click: btnEventSaveWdCmdTrain">保存</button>
        <button type="button" class="btn btn-info" data-bind="click: btnEventResetWdCmdTrain"  style="margin-left:10px">清空全部信息</button>
        <button type="button" class="btn btn-info"  data-bind="click: btnEventCleanWdInfo" style="margin-left:10px">清空列车信息</button>
        <!--  
        <button type="button" class="btn btn-warning" data-dismiss="modal" id="btnEventCancelWdCmdTrain">取消</button>
      -->
      </div>
 
</body>

</html>