<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>高铁乘务计划查询</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
    <style type="text/css">
    .table thead > tr > th{
    padding: 0px 5px;
    }
    .form-horizontal .control-label, .form-horizontal .radio, .form-horizontal .checkbox, .form-horizontal .radio-inline, .form-horizontal .checkbox-inline{
    padding-top: 5px;
    }
    select.form-control{
    padding:1px 3px 0 3px;
    }
    </style>
</head>
<body class="Iframe_body" style="margin:-8px -8px -5px -9px;width:100%;padding-bottom: 0;">
<!-- <ol class="breadcrumb">
  <span><i class="fa fa-anchor"></i>当前位置：</span>
  <li><a href="javascript:void(0);">车底三乘计划 -> 高铁乘务计划查询</a></li>
</ol> -->
<!--以上为必须要的--> 

<div class="row" style="padding-top:10px;padding-bottom:10px;">
  <form class="form-horizontal" role="form">
  	<div class="row">
  		<div class="pull-left">
	  		<div class="row" style="margin-top:-20px;">
	  			<div class="row" style="width: 100%; margin-top: 5px;">
			  		<div class="form-group" style="float:left;margin-left:20px;margin-bottom:5px;">
			  			<label for="exampleInputEmail2" class="control-label pull-left" style="margin-left:-5px;">开始日期:&nbsp;</label>
					    <div class="pull-left">
					        <input id="crew_input_startDate" type="text" class="form-control" style="width:100px;" placeholder="">
						</div>
						<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;截至日期:&nbsp;</label>
					    <div class="pull-left">
					        <input id="crew_input_endDate" type="text" class="form-control" style="width:100px;" placeholder="">
						</div>
						<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;乘务类型:&nbsp;</label>
					    <div class="pull-left">
					    	<select class="form-control" style="width: 90px;display:inline-block;"
								 data-bind="options: crewTypeArray, value: searchModle().crewTypeOption, optionsText: 'text'">
							</select>
						</div>
						<label for="exampleInputEmail3" class="control-label pull-left">&nbsp;&nbsp;路局:</label>
						<div class="pull-left" style="margin:0 5px; ">
							<select style="width: 50px" class="form-control" data-bind="options:searchModle().bureauSelect, value: searchModle().bureauOption, optionsText: 'text',event:{change: searchModle().bureauSelectChange}"></select>
						</div>
			  		</div>

			  		<div class="form-group" style="float:left;margin-left:15px;margin-bottom:0;">
			  			
						<label for="exampleInputEmail2" class="control-label pull-left">部门:&nbsp;</label>
					    <div class="pull-left">
					        <select class="form-control" style="width:120px" data-bind="options:searchModle().orgSelect, value: searchModle().orgOption, optionsText: 'text'"></select>
						</div>
						<label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;车次:&nbsp;</label>
					    <div class="pull-left">
					        <input id="crew_input_trainNbr" type="text" class="form-control" style="width:100px;" data-bind="value: searchModle().trainNbr">
					    </div>
					    <label for="exampleInputEmail2" class="control-label pull-left">&nbsp;&nbsp;乘务员姓名:&nbsp;</label>
					    <div class="pull-left">
					        <input id="crew_input_crewPeopleName" type="text" class="form-control" style="width:110px;" data-bind="value: searchModle().crewPeopleName">
					    </div>
					    <a type="button" href="#" class="btn btn-success btn-input" data-bind="click : queryList" style="float:left;margin-left:20px;margin-bottom:0;"><i class="fa fa-search"></i>查询</a>
 		   		        <button type="button" class="btn btn-success btn-input" data-bind="click : exportExcel" style="float:left;margin-left:5px;margin-bottom:0;"><i class="fa fa-sign-out"></i>导出EXCEL</button>
  			
			  		</div>
				
			  	</div>
			  	

			  	
	  		</div>
	  		
	  		
  		</div>
  	</div>
  	
  	
  	
  	
  </form>
</div>



<!--左右分开-->
<div class="row">
	  
  
  <!--乘务计划-->
  <div style="float:left; width:100%;">
    <!--分栏框开始-->
    <div class="panel panel-default" style="margin:0px -3px 0px 15px;">
      
      <div class="panel-body">
        <div class="table-responsive table-hover">
          <table class="table table-bordered table-striped table-hover">
            <thead>
              <tr>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:50px;">序号</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:100px;">乘务类型</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:100px;">日期</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:140px;">乘务交路</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:120px;">乘务组编号</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;">经由铁路线</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">乘务员1</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">乘务员2</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:120px;">备注</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:60px;">路局</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:120px;">部门</th>
                <th rowspan="2" style="width: 17px" align="center"></th>
              </tr>
              <tr>
                <th class="text-center" style="width:100px;">姓名</th>
                <th class="text-center" style="width:120px;">电话</th>
                <th class="text-center" style="width:110px;">政治面貌</th>
                <th class="text-center" style="width:100px;">姓名</th>
                <th class="text-center" style="width:120px;">电话</th>
                <th class="text-center" style="width:110px;">政治面貌</th>
              </tr>
            </thead>
		</table>
        <div id="left_height" style="overflow-y:auto;">
          <table class="table table-bordered table-striped table-hover" style="border: 0;">
            <tbody data-bind="foreach: hightLineCrewRows.rows">
              <tr data-bind="click: $parent.setCurrentRec,style:{color: $parent.currentRowCrewHighlineId() == crewHighlineId ? 'blue':''}">
                <td style="width:50px;" data-bind=" text: ($parent.hightLineCrewRows.currentIndex()+$index() + 1)"></td>
                <td style="width:100px;" align="center" data-bind=" text: crewTypeName"></td>
                <td style="width:100px;" align="center" data-bind=" text: crewDate"></td>
                <td style="width:140px;" align="center" data-bind=" text: crewCross, attr:{title: crewCross}"></td>
                <td style="width:120px;" data-bind=" text: crewGroup, attr:{title: crewGroup}"></td>
                <td style="" data-bind="text: throughLine, attr:{title: throughLine}"></td>
                
                <td style="width:100px;" align="center" data-bind=" text: name1, attr:{title: name1}"></td>
                <td style="width:120px;" align="center" data-bind=" text: tel1, attr:{title: tel1}"></td>
                <td style="width:110px;" data-bind=" text: identity1, attr:{title: identity1}"></td>
                <td style="width:100px;" align="center" data-bind=" text: name2, attr:{title: name2}"></td>
                <td style="width:120px;" align="center" data-bind=" text: tel2, attr:{title: tel2}"></td>
                <td style="width:110px;" data-bind=" text: identity2, attr:{title: identity2}"></td>
                <td style="width:120px" data-bind=" text: note, attr:{title: note}"></td>
                <td style="width:60px;" align="center" data-bind=" text: crewBureau"></td>
                <td style="width:120px;" align="center" data-bind=" text: recordPeopleOrg, attr:{title: recordPeopleOrg}"></td>
                <td style="width: 17px" align="center" class="td_17"></td>
              </tr>
            </tbody>
          </table>
         </div>
			<div data-bind="template: { name: 'tablefooter-short-template', foreach: hightLineCrewRows }"></div>
        </div>
      </div>
      <!--panel-body--> 
      
    </div>
    
    <!--分栏框结束--> 
  </div>
  <!--乘务计划end--> 
<input type="hidden" id="crewType" value='${crewType}'>
</div>
<!--左右分开--> 



<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script> 
<script type="text/javascript"  src="<%=basePath %>/assets/js/trainplan/knockout.pagemodle.js"></script> 
<script type="text/javascript"  src="<%=basePath %>/assets/js/trainplan/hightlineCrew/hightline.crew.all.js"></script> 
<script type="text/html" id="tablefooter-short-template"> 
 <div class="row footer">
     <div class="pull-left" style="line-height:24px;">
  		<span class="pull-left">共<span class="space-pre" data-bind="html: totalCount()"></span>条&nbsp&nbsp当前<span class="space-pre" data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span class="space-pre" data-bind="html: endIndex()"></span>条&nbsp&nbsp共<span class="space-pre" data-bind="text: pageCount()"></span>页</span> 								 
  	 </div>
     <div class="pull-right" style="padding:0px;">   
	   <span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}">
	        <a class="pre-btn" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
		    <input type="text" class="pre-input" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
			<a class="pre-btn after-btn" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"></a>
       </span>  
     </div>
</div>
</script>

</body>
</html>