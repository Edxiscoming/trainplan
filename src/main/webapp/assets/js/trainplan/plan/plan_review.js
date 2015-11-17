/**
 * 路局审核界面
 * 
 * @author denglj 
 */
var PlanReViewPage = function(){
	var _self = this;
	
	//获取当期系统日期
	this.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		return year+"-"+month+"-"+days;
	};
	
	
	this.initPage = function() {
		//1.初始化日期控件值
		_plan_review_selectdate.datepicker();
		_plan_review_selectdate.val(this.currdate());//获取当期系统日期
		//日期控件回车事件
		_plan_review_selectdate.keypress(function(event){
		    var keycode = (event.keyCode ? event.keyCode : event.which);
		    if(keycode == '13'){
		    	_PlanReViewPage.getOneStationTrains();
		    }
		});
		
		
		//2.初始化路局下拉控件值
		this.initLjSelectValue();
		
	};
	
	
	//2.初始化路局下拉控件值
	this.initLjSelectValue = function() {
		_plan_review_select_lj.empty();//清除路局下拉控件值
		$.ajax({
			url : basePath+"/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {
						$.each(result.data,function(n,ljObj){
							if (n==0) {
								_plan_review_select_lj.append('<option selected="selected" value="'+ljObj.ljqc+'">'+ljObj.ljjc+'</option>');
							} else {
								_plan_review_select_lj.append('<option value="'+ljObj.ljqc+'">'+ljObj.ljjc+'</option>');
							}
						});
						
						
						//2.加载路局汇总统计信息
						_self.getOneStationTrains();
					}
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			}
		});
	};
	
	
	//加载路局开行计划统计信息
	this.getOneStationTrains = function() {
		commonJsScreenLock();//锁屏
		
		//清除统计信息
		$("#plan_review_table_ljtjxx_sfhj").text("");
		$("#plan_review_table_ljtjxx_sftdxj").text("");
		$("#plan_review_table_ljtjxx_sftdjc").text("");
		$("#plan_review_table_ljtjxx_sftdzd").text("");
		$("#plan_review_table_ljtjxx_jrhj").text("");
		$("#plan_review_table_ljtjxx_jrtdxj").text("");
		$("#plan_review_table_ljtjxx_jrtdjc").text("");
		$("#plan_review_table_ljtjxx_jrtdzd").text("");
		$.ajax({
			url : basePath+"/plancheck/getOneStationTrains",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_review_selectdate.val(),
				startBureauFull:_plan_review_select_lj.val()
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {
//						$.each(result.data,function(n,trainObj){
							$("#plan_review_table_ljtjxx_sfhj").text(result.data.TDSFXJ);
							$("#plan_review_table_ljtjxx_sftdxj").text(result.data.TDSFXJ);
							$("#plan_review_table_ljtjxx_sftdjc").text(result.data.TDSFJC);
							$("#plan_review_table_ljtjxx_sftdzd").text(result.data.TDSFZD);
							
							$("#plan_review_table_ljtjxx_jrhj").text(result.data.TDJRXJ);
							$("#plan_review_table_ljtjxx_jrtdxj").text(result.data.TDJRXJ);
							$("#plan_review_table_ljtjxx_jrtdjc").text(result.data.TDJRJC);
							$("#plan_review_table_ljtjxx_jrtdzd").text(result.data.TDJRZD);
//						});
					}
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			},
			complete : function() {
				commonJsScreenUnLock();//屏幕解锁
			}
		});
		
		
		switchToPage(1);
	};
	
	
	//查询xx日列车开行计划信息
	this.loadTrainInfo = function(_currentPage) {
		commonJsScreenLock();//锁屏
		
		_plan_review_table_trainInfo.find("tr:gt(1)").remove();//清除车次详细信息列表所有数据
		_plan_review_table_trainDetail.find("tr:gt(0)").remove();//清除车次详细信息列表所有数据
		$.ajax({
			url : basePath+"/plancheck/getPlanTrainByStartBureau",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_review_selectdate.val(),
				trainNbr:_plan_review_input_trainNbr.val(),
				startBureau:_plan_review_select_lj.val(),
				currentPage:_currentPage,
				pageSize : pageSize
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {

						//分页参数设置result.total
						totalPage = result.data.totalPage;
						initPagination("page_footer_ul", result.data.totalRecord);
						
						var index= pagingUtilRecordStartIndex;
						var endBureau="";
						var endStn="";
						$.each(result.data.data,function(n,trainObj){
							if (trainObj.endBureau!=null && trainObj.endBureau!="null") {
								endBureau = trainObj.endBureau;
							}
							if (trainObj.endStn!=null && trainObj.endStn!="null") {
								endStn = trainObj.endStn;
							}
							
//							var tr = $("<tr onclick=showTrainTimeDetail('"+trainObj.trainNbr+"')/>");
							var tr = $("<tr/>");
							tr.append("<td>"+index+"</td>");
							tr.append("<td><a onclick=showTrainTimeDetail('"+trainObj.trainNbr+"')>"+trainObj.trainNbr+"</a></td>");
							tr.append("<td><div class='popover01' data-container='body' data-trigger='hover' data-toggle='popover' data-placement='top' data-content='始发站名： "+trainObj.startStn+"' data-original-title='' title=''>"+trainObj.startStn+"</div></td>");
							tr.append("<td>"+trainObj.startBureau+"</td>");
							tr.append("<td><div class='popover01' data-container='body' data-trigger='hover' data-toggle='popover' data-placement='top' data-content='始发时间： "+trainObj.startTimeStr+"' data-original-title='' title=''>"+trainObj.startTimeStr+"</div></td>");
							tr.append("<td><div class='popover01' data-container='body' data-trigger='hover' data-toggle='popover' data-placement='top' data-content='终到站名： "+endStn+"' data-original-title='' title=''>"+endStn+"</div></td>");
							tr.append("<td>"+endBureau+"</td>");
							tr.append("<td><div class='popover01' data-container='body' data-trigger='hover' data-toggle='popover' data-placement='top' data-content='终到时间： "+trainObj.endTimeStr+"' data-original-title='' title=''>"+trainObj.endTimeStr+"</div></td>");
							tr.append("<td><div class='popover01' data-container='body' data-trigger='hover' data-toggle='popover' data-placement='top' data-content='经过局：  "+trainObj.jylj+"' data-original-title='' title=''>"+trainObj.jylj+"</div></td>");
							tr.append("<td></td>");
							_plan_review_table_trainInfo.append(tr);
							index = index+1;
						});
						

						$(".popover01").popover("toggle");
						$(".popover01").popover("hide");
					}
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			},
			complete : function() {
				commonJsScreenUnLock();//屏幕解锁
			}
		});
	};
	
	
	
	
	/**
	 * 车次列表行点击事件
	 * 显示xx日车次详细信息
	 * @author 邓柳江
	 */
	self.showTrainTimeDetail = function (_trainNbr) {
		$("#plan_view_div_palnDayDetail_title").text("     车次号："+_trainNbr);
		_plan_review_table_trainDetail.find("tr:gt(0)").remove();//清除车次详细信息列表所有数据
		commonJsScreenLock();//锁屏
		
		$.ajax({
			url : basePath+"/plancheck/showTrainTimeDetail",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_review_selectdate.val(),
				trainNbr : _trainNbr
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {
						var index=1;
						var _trainTimeDetailDptTime = "";//出发时间
						var _trainTimeDetailArrtime = "";//到站时间
						var _track_name = "";//股道名
						var _runday = "";//运行天数
						$.each(result.data,function(n,trainObj){
							if (trainObj.ARR_TIME !=null && trainObj.ARR_TIME !="null") {
								_trainTimeDetailArrtime = trainObj.ARR_TIME;
							}
							if (trainObj.DPT_TIME !=null && trainObj.DPT_TIME !="null") {
								_trainTimeDetailDptTime = trainObj.DPT_TIME;
							}
							
							if (trainObj.TRACK_NAME !=null && trainObj.TRACK_NAME !="null") {
								_track_name = trainObj.TRACK_NAME;
							}
							if (trainObj.RUN_DAYS !=null && trainObj.RUN_DAYS !="null") {
								_runday = trainObj.RUN_DAYS;
							}
							
							var tr = $("<tr/>");
							tr.append("<td>"+index+"</td>");
							tr.append("<td><div class='popover01' data-container='body' data-trigger='hover' data-toggle='popover' data-placement='top' data-content='站名： "+trainObj.STN_NAME+"' data-original-title='' title=''>"+trainObj.STN_NAME+"</div></td>");
							tr.append("<td>"+trainObj.LJJC+"</td>");
							tr.append("<td>"+_trainTimeDetailArrtime+"</td>");
							tr.append("<td>"+_trainTimeDetailDptTime+"</td>");
							tr.append("<td>"+trainObj.STOP_TIME+"</td>");
							tr.append("<td>"+_track_name+"</td>");//"+trainObj.TRACK_NBR+"
							tr.append("<td>"+_runday+"</td>");//"+trainObj.RUN_DAYS+"
							_plan_review_table_trainDetail.append(tr);
							
							index = index+1;
						});
						

						$(".popover01").popover("toggle");
						$(".popover01").popover("hide");
					}
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			},
			complete : function() {
				commonJsScreenUnLock();//屏幕解锁
			}
		});
	};
	
	
	
	
	
	
	
	
	
	//生成运行线
	this.createRunLine = function() {
		$.ajax({
			url : basePath+"/plan/handleTrainLines",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_review_selectdate.val(),
				startBureauFull:_plan_review_select_lj.val()
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			}
		});
	};
	
	
	
};

var _PlanReViewPage = null;
//分页对象
var _plan_review_paging_total = $("#plan_review_paging_total");

var _plan_review_btnQuery_ljtjxx = $("#plan_review_btnQuery_ljtjxx");
var _plan_review_input_search2 = $("#plan_review_input_search2");
var _plan_review_btnQuery2 = $("#plan_review_btnQuery2");


var _plan_review_select_lj = $("#plan_review_select_lj");//路局下拉框
var _param_startBureau = "Q";//路局编码
//var _param_pageSize = 20;
var _plan_review_selectdate = $("#plan_review_selectdate");
var _plan_review_table_ljtjxx = $("#plan_review_table_ljtjxx");
var _plan_review_table_trainInfo = $("#plan_review_table_trainInfo");
var _plan_review_table_trainDetail = $("#plan_review_table_trainDetail");
var _plan_review_input_trainNbr = $("#plan_review_input_trainNbr");



$(function(){
	_PlanReViewPage = new PlanReViewPage();

	//车次详情div 车次查询按钮增加事件
	$("#plan_review_btnQuery2").click(function(){
		switchToPage(1);
	});
	//车次详情div 车次号input 增加回车事件
//	_plan_review_input_trainNbr.keypress(function(event){
//	    var keycode = (event.keyCode ? event.keyCode : event.which);
//	    if(keycode == '13'){
//			switchToPage(1);
//	    }
//	});
	
	
	
	
	_PlanReViewPage.initPage();
	
	$("#plan_review_btn_createRunLine").click(function(){
		_PlanReViewPage.createRunLine();
		$("#plan_review_btn_createRunLine").attr("disabled",true);
	});
	

});



//分页查询方法
function switchToPage(_paramCurrentPageValue) {
	hiddenPaginationDiv("page_footer_ul");
	pageNo = _paramCurrentPageValue;
	_PlanReViewPage.loadTrainInfo(_paramCurrentPageValue);
}

//重新刷新统计信息
function refreshPlanReviewTableLjtjxx() {
	_PlanReViewPage.getOneStationTrains();
}