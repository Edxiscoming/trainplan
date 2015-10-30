<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
</head>
<body class="Iframe_body">
<input id="trainCrew_trainNbr_hidden" type="hidden" value="${trainNbr}">
<input id="trainCrew_runDate_hidden" type="hidden" value="${runDate}">
<input id="trainCrew_startStn_hidden" type="hidden" value="${startStn}">
<input id="trainCrew_endStn_hidden" type="hidden" value="${endStn}">


<div class="row">
    <div class="panel panel-default">
      
      <div class="panel-body">
        <div class="table-responsive table-hover" style="overflow:auto">
          <table id="table_run_plan_train_crew_edit" class="table table-bordered table-striped table-hover">
            <thead>
              <tr>
                <th rowspan="2" class="text-center" style="vertical-align: middle;width:40px;">序号</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">乘务类型</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">日期</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">乘务交路</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">乘务组编号</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">经由铁路线</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">乘务员1</th>
                <th colspan="3" class="text-center" style="vertical-align: middle">乘务员2</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">备注</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">路局</th>
                <th rowspan="2" class="text-center" style="vertical-align: middle">部门</th>
              </tr>
              <tr>
                <th class="text-center">姓名</th>
                <th class="text-center">电话</th>
                <th class="text-center">政治面貌</th>
                <th class="text-center">姓名</th>
                <th class="text-center">电话</th>
                <th class="text-center">政治面貌</th>
              </tr>
            </thead>
            <tbody data-bind="foreach: trainCrewRows">
              <tr>
                <td data-bind=" text: ($index() + 1)"></td>
                <td data-bind=" text: crewTypeName, attr:{title: crewTypeName}"></td>
                <td data-bind=" text: crewDate, attr:{title: crewDate}"></td>
                <td data-bind=" text: crewCross, attr:{title: crewCross}"></td>
                <td data-bind=" text: crewGroup, attr:{title: crewGroup}"></td>
                <td data-bind=" text: throughLine, attr:{title: throughLine}"></td>
                <td data-bind=" text: name1, attr:{title: name1}"></td>
                <td data-bind=" text: tel1, attr:{title: tel1}"></td>
                <td data-bind=" text: identity1, attr:{title: identity1}"></td>
                <td data-bind=" text: name2, attr:{title: name2}"></td>
                <td data-bind=" text: tel2, attr:{title: tel2}"></td>
                <td data-bind=" text: identity2, attr:{title: identity2}"></td>
                <td data-bind=" text: note, attr:{title: note}"></td>
                <td data-bind=" text: crewBureau"></td>
                <td data-bind=" text: recordPeopleOrg, attr:{title: recordPeopleOrg}"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
	
</div>












<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.freezeheader.js"></script>
<script src="<%=basePath %>/assets/js/trainplan/runPlan/train_crew.js"></script>
</body>
</html>