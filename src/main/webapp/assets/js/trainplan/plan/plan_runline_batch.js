/**
 * 批量上图js
 * 
 * @author denglj 
 */
var PlanRunLineBatchPage = function(){
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
		_plan_runline_batch_selectdate.datepicker();
		_plan_runline_batch_selectdate.val(this.currdate());//获取当期系统日期
		//日期控件回车事件
		_plan_runline_batch_selectdate.keypress(function(event){
		    var keycode = (event.keyCode ? event.keyCode : event.which);
		    if(keycode == '13'){
		    	_PlanRunLineBatchPage.getOneStationTrains(1);
		    }
		});
		
		
		initPagination("page_footer_ul", 0);
		
		
		
		//2.初始化路局下拉控件值
		this.initLjSelectValue();
		
	};
	
	
	//2.初始化路局下拉控件值
	this.initLjSelectValue = function() {
		_plan_runline_batch_select_lj.empty();//清除路局下拉控件值
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
								_plan_runline_batch_select_lj.append('<option selected="selected" value="'+ljObj.ljqc+'">'+ljObj.ljjc+'</option>');
							} else {
								_plan_runline_batch_select_lj.append('<option value="'+ljObj.ljqc+'">'+ljObj.ljjc+'</option>');
							}
						});
						
						
						//2.加载路局汇总统计信息
						_self.getOneStationTrains(1);
						
					}
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			}
		});
	};
	
	
	
	this.renderTableHead = function(_paramList) {
		//循环生成表头
		var tr = $('<tr/>');
		tr.append("<th width='40px'><div style='text-align:center;width:40px'>序号</div></th>");
		tr.append("<th width='100px'><div style='text-align:center;width:100px'>车次</div></th>");
		$.each(_paramList,function(n,headObj){
			tr.append("<th width='40px'><div style='text-align:center;'>"+headObj+"</div></th>");
		});
		_plan_runline_batch_table_ljtjxx_head.append(tr);
		
	};
	
	
	
	//加载路局开行计划统计信息
	this.getOneStationTrains = function(_paramCurrentPageValue) {
		if (!$.isNumeric(_plan_runline_batch_input_days.val())) {
			showErrorDialog("天数必须为数字");
			return;
		}
		
		
		/*======================================================
		 * 表格信息初始化
		 ======================================================*/

		$("#plan_runline_batch_btn_save").attr("disabled",true);
		$("#plan_runline_batch_btnQuery_ljtjxx").attr("disabled",true);

		commonJsScreenLock();//锁屏
		
		$("#plan_runline_input_trainNbr").val($("#plan_runline_input_trainNbr").val().toUpperCase());
		pageNo = _paramCurrentPageValue;
		_plan_runline_batch_table_ljtjxx_body.find("tr:gt(0)").remove();//清除表格表体信息
		var tr0 = $('<tr/>');
		_plan_runline_batch_table_ljtjxx_body.append(tr0);
		
		_plan_runline_batch_table_ljtjxx_head.find("tr:gt(0)").remove();//清除表格表头信息
		var trHead = $('<tr/>');
		_plan_runline_batch_table_ljtjxx_head.append(trHead);
		
		hiddenPaginationDiv("page_footer_ul");
		/*======================================================
		 * 表格信息初始化   结束
		 ======================================================*/
		
		
		$.ajax({
			url : basePath+"/plancheck/findPlanTrainWithPeriodRunDate",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_runline_batch_selectdate.val(),
				trainNbr : $("#plan_runline_input_trainNbr").val(),
				startBureauFull:_plan_runline_batch_select_lj.val(),
				dayCount:_plan_runline_batch_input_days.val(),
				currentPage:_paramCurrentPageValue,
				pageSize : pageSize
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {
						//生成表头
						_self.renderTableHead(result.data.days);

						//分页参数设置result.total
						totalPage = result.data.pageInfo.totalPage;
						initPagination("page_footer_ul", result.data.pageInfo.totalRecord);
						
						
						
						var _index= pagingUtilRecordStartIndex;
						$.each(result.data.pageInfo.data,function(n,trainObj){
							var tr = $('<tr/>');
							tr.append("<td width='40px' align='center'>"+_index+"</td>");
							tr.append("<td width='100px'>"+trainObj.trainNbr+"</td>");
							
							
							//解析该车次计划天数内开行情况
							$.each(trainObj.days,function(m,trainDayObj){
								if (trainDayObj.status == "1") {
									tr.append("<td width='40px'><div style='text-align:center;'><input type='checkbox' name='runLineBatchCheckbox'  checked='checked' value='"+trainDayObj.key+"'/></div></td>");
								} else {
									tr.append("<td width='40px'></td>");
								}
							});
							
							
							

							_plan_runline_batch_table_ljtjxx_body.append(tr);
							_index = _index +1;
						});
					}
					

				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
			},
			complete : function() {
				commonJsScreenUnLock();//屏幕解锁
				$("#plan_runline_batch_btn_save").attr("disabled",false);
				$("#plan_runline_batch_btnQuery_ljtjxx").attr("disabled",false);
			}
		});
		
		
	};
	
	
	
	//生成运行线
	this.createRunLine = function() {
		$.ajax({
			url : basePath+"/plan/batchHandleTrainLines",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_runline_batch_selectdate.val(),
				dayCount:_plan_runline_batch_input_days.val(),
				trainNbr: $("#plan_runline_input_trainNbr").val(),
				startBureauFull:_plan_runline_batch_select_lj.val()
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
	
	
	
	/**
	 * 批量删除列车开行计划数据
	 */
	this.deleteTrainPlan = function () {
		commonJsScreenLock();//锁屏
		$("#plan_runline_batch_btn_save").attr("disabled",true);
		$("#plan_runline_batch_btnQuery_ljtjxx").attr("disabled",true);
		
		var _deleteKey = "";
		$("input[name='runLineBatchCheckbox']").each(function(){
			if (this.checked == false) {
				_deleteKey = _deleteKey+"#"+this.value;
			}
		});
		if (_deleteKey !="") {
			_deleteKey = _deleteKey.substring(1);
		}
		
		
		
		$.ajax({
			url : basePath+"/plancheck/deletePlanTrainWithRunDate",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				deleteKey : _deleteKey
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					//删除成功后，重新加载列表
					_self.getOneStationTrains(1);
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				commonJsScreenUnLock();//屏幕解锁
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			}
		});
	};
	
	
	
};

var _PlanRunLineBatchPage = null;
//分页对象

var _plan_runline_batch_select_lj = $("#plan_runline_batch_select_lj");//路局下拉框
var _plan_runline_batch_selectdate = $("#plan_runline_batch_selectdate");
var _plan_runline_batch_table_ljtjxx_head = $("#plan_runline_batch_table_ljtjxx_head");//统计信息表头
var _plan_runline_batch_table_ljtjxx_body = $("#plan_runline_batch_table_ljtjxx_body");//统计信息表体
var _plan_runline_batch_input_days = $("#plan_runline_batch_input_days");//天数输入框

$(function(){
	_PlanRunLineBatchPage = new PlanRunLineBatchPage();

	//车次详情div 车次查询按钮增加事件
	$("#plan_runline_batch_btnQuery_ljtjxx").click(function(){
		_PlanRunLineBatchPage.getOneStationTrains(1);
	});
	$("#plan_runline_batch_btn_createRunLine").click(function(){
		_PlanRunLineBatchPage.createRunLine();
		$("#plan_runline_batch_btn_createRunLine").attr("disabled",true);
	});
	$("#plan_runline_batch_btn_save").click(function(){
		_PlanRunLineBatchPage.deleteTrainPlan();
	});
	
	_PlanRunLineBatchPage.initPage();
	
	
	

});



//分页查询方法
function switchToPage(_paramCurrentPageValue) {
	_PlanRunLineBatchPage.getOneStationTrains(_paramCurrentPageValue);
}

//重新刷新统计信息
function refreshPlanRunlineBatchTableLjtjxx() {
	_PlanRunLineBatchPage.getOneStationTrains(1);
}