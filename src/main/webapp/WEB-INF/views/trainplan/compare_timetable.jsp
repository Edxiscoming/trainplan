<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head lang="en">
<title>时刻表比较</title>
<jsp:include page="/assets/commonpage/global.jsp" flush="true" />
<script type="text/javascript"
	src="${ctx}/assets/js/jquery.freezeheader.js"></script>
<script type="text/javascript" src="${ctx}/assets/js/purl.js"></script>
<script type="text/javascript"
	src="${ctx}/assets/js/trainplan/compare_timetable.js"></script>
</head>
<body>
	<div class="container padding0 margin0" style="width: 100%;">
		<ul class="nav nav-tabs">
			<li style="float: right"><input type="checkbox"
				class="pull-left" class="form-control" onClick="showTimes()"
				class="form-control"> <label for="exampleInputEmail5"
				class="control-label pull-left" style="margin: 2px 0 0 5px;">
					隐藏日期</label></li>
		</ul>
		<div class="row padding0 margin0" id="mdhm">
			<div class="col-md-7 col-xs-7 col-lg-7 padding0 margin0">
				<div class="panel-body padding0 margin0">
					<div class="table-responsive padding0 margin0">
						<table id="planTable"
							class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									<th rowspan="2" class="text-center">序号</th>
									<th colspan="5" class="text-center">客运计划</th>
								</tr>
								<tr>
									<th class="text-center">站名</th>
									<th class="text-center">所属局</th>
									<th class="text-center">到达</th>
									<th class="text-center">出发</th>
									<th class="text-center">股道</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="plan" items="${planList}" varStatus="idx">
									<tr>
										<td class="text-center">${idx.index + 1}</td>
										<td class="text-center">${plan.stnName}</td>
										<td class="text-center">${plan.stnBureau}</td>
										<td class="text-center">${plan.arrTime}</td>
										<td class="text-center">${plan.dptTime}</td>
										<td class="text-center">${plan.trackName}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-md-5 col-xs-5 col-lg-5 padding0 margin0" style="margin-left: -2px;">
				<div class="panel-body padding0 margin0">
					<div class="table-responsive padding0 margin0">
						<table id="lineTable"
							class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									<th colspan="5" class="text-center">运行线</th>
								</tr>
								<tr>
									<th class="text-center">站名</th>
									<th class="text-center">所属局</th>
									<th class="text-center">到达</th>
									<th class="text-center">出发</th>
									<th class="text-center">股道</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="line" items="${lineList}">
									<tr>
										<td class="text-center">${line.stnName}</td>
										<td class="text-center">${line.stnBureau}</td>
										<td class="text-center">${line.arrTime}</td>
										<td class="text-center">${line.dptTime}</td>
										<td class="text-center">${line.trackName}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row padding0 margin0"  style="display:none" id="hm">
			<div class="col-md-7 col-xs-7 col-lg-7 padding0 margin0">
				<div class="panel-body padding0 margin0">
					<div class="table-responsive padding0 margin0">
						<table id="planTable"
							class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									<th rowspan="2" class="text-center">序号</th>
									<th colspan="5" class="text-center">客运计划</th>
								</tr>
								<tr>
									<th class="text-center">站名</th>
									<th class="text-center">所属局</th>
									<th class="text-center">到达</th>
									<th class="text-center">出发</th>
									<th class="text-center">股道</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="plan" items="${planList}" varStatus="idx">
									<tr>
										<td class="text-center">${idx.index + 1}</td>
										<td class="text-center">${plan.stnName}</td>
										<td class="text-center">${plan.stnBureau}</td>
										<td class="text-center">${plan.arrTime_hm}</td>
										<td class="text-center">${plan.dptTime_hm}</td>
										<td class="text-center">${plan.trackName}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-md-5 col-xs-5 col-lg-5 padding0 margin0"
				style="margin-left: -2px;">
				<div class="panel-body padding0 margin0">
					<div class="table-responsive padding0 margin0">
						<table id="lineTable"
							class="table table-bordered table-striped table-hover">
							<thead>
								<tr>
									<th colspan="5" class="text-center">运行线</th>
								</tr>
								<tr>
									<th class="text-center">站名</th>
									<th class="text-center">所属局</th>
									<th class="text-center">到达</th>
									<th class="text-center">出发</th>
									<th class="text-center">股道</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="line" items="${lineList}">
									<tr>
										<td class="text-center">${line.stnName}</td>
										<td class="text-center">${line.stnBureau}</td>
										<td class="text-center">${line.arrTime_hm}</td>
										<td class="text-center">${line.dptTime_hm}</td>
										<td class="text-center">${line.trackName}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		
	</div>
</body>
</html>