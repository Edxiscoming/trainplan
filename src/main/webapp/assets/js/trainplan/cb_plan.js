var plan = null;
$(function() {
	heightAuto();

	plan = new PlanModel();
	ko.applyBindings(plan);
	plan.init();
});

function heightAuto() {
	var WH = $(window).height();
	// alert("window: 860---"+WH);
	$("#div_hightline_planDayDetail").css("height", WH - 360 + "px");
	$("#div_crossDetailInfo_trainStnTable").css("height", 270 + "px");
	$("#canvas_parent_div").css("height", 203 + "px");
	$("#left_height").css("height", WH - 360 - 72 + "px");
};

function PlanModel() {
	var self = this;

	// currentIndex
	self.currdate = function() {
		var d = new Date();
		var year = d.getFullYear(); // 获取完整的年份(4位,1970-????)
		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
		var days = d.getDate();
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};

	self.get40Date = function() {
		var d = new Date();
		d.setDate(d.getDate() + 1);// + 30

		var year = d.getFullYear(); // 获取完整的年份(4位,1970-????)
		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
		var days = d.getDate();
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};

	// 查询条件
	self.planStartDate1 = ko.observable(self.currdate());
	self.planStartDate2 = ko.observable(self.get40Date());

	self.bureaus = ko.observableArray();

	// 列表
	self.qPlans = ko.observableArray();
	self.hPlans = ko.observableArray();
	self.qCbPlans = ko.observableArray();
	self.hCbPlans = ko.observableArray();
	self.plans = ko.observableArray();

	self.init = function() {
		$("#runplan_input_startDate1").datepicker();
		$("#runplan_input_startDate2").datepicker();

		var _height = $(window).height() - $('#div_searchForm').height() - 500
				- 150;
		_height = _height < 80 ? 200 : _height;

		commonJsScreenLock(1);
		$.ajax({
			url : $("#basePath").val() + "/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null && result.data != undefined) {
						var types = new Array();
						var fistObj = null;
						var bureauShortName = $("#bureauShortName").val();
						for (var i = 0; i < result.data.length; i++) {
							var obj = result.data[i];
							if (bureauShortName == obj.ljjc) {
								fistObj = obj;
							} else {
								types.push(obj);
							}
						}
						if (null != fistObj) {
							types.unshift(fistObj, '');
						} else {
							types.unshift('');
						}
						$.each(types, function(n, types) {
							self.bureaus.push({
								"name" : types.ljjc,
								"id" : types.ljpym
							});
						});
					}
				} else {
					showErrorDialog("获取路局列表失败");
				}
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});

	};

	self.loadCbPlans = function() {

		// var bureauCode = $("#plan_bureaus").val();
		var passBureauCode = $("#plan_pass_bureaus").val();
		var passBureauName = $('#plan_pass_bureaus option:selected').text();

		var planStartDate1 = $("#runplan_input_startDate1").val();
		var planStartDate2 = $("#runplan_input_startDate2").val();

		if ("" == planStartDate1 || "" == planStartDate2) {
			showWarningDialog("请选择日期！");
			return;
		}

		if (planStartDate2 <= planStartDate1) {
			showWarningDialog("日期2必须大于日期1！");
			return;
		}

		document.getElementById('qcb').innerHTML = planStartDate1;
		document.getElementById('hcb').innerHTML = planStartDate2;
		document.getElementById('qjj').innerHTML = planStartDate1;
		document.getElementById('hjj').innerHTML = planStartDate2;
//		document.getElementById('cb').innerHTML = planStartDate1 + "&nbsp/&nbsp" + planStartDate2;

		self.qPlans.remove(function(item) {
			return true;
		});
		self.hPlans.remove(function(item) {
			return true;
		});
		self.qCbPlans.remove(function(item) {
			return true;
		});
		self.hCbPlans.remove(function(item) {
			return true;
		});
		// self.plans.remove(function(item) {
		// return true;
		// });

		commonJsScreenLock();
		$.ajax({
			url : $("#basePath").val() + "/audit/getCbPlan",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				passBureauName : passBureauName,
				passBureauCode : passBureauCode,
				planStartDate1 : planStartDate1,
				planStartDate2 : planStartDate2,
				trainType : 0
			}),
			success : function(result) {
				if (result != null && result.code == "0") {
					if (result.data != null) {
						// 第一天
						$.each(result.data.cbPlansQ, function(i, n) {
							self.qCbPlans.push(new Plan(n));
						});
						// 第二天
						$.each(result.data.cbPlansH, function(i, n) {
							self.hCbPlans.push(new Plan(n));
						});
						// 相同的
						// $.each(result.data.cbPlans, function(i, n){
						// self.plans.push(new Plan(n));
						// });
						$.each(result.data.qjjList, function(i, n) {
							self.qPlans.push(new Plan(n));
						});
						$.each(result.data.hjjList, function(i, n) {
							self.hPlans.push(new Plan(n));
						});
						
					}
				} else {
					showErrorDialog("获取差别计划失败");
				}
				;
			},
			error : function() {
				showErrorDialog("获取差别计划失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	}

}

function Plan(data) {
	var self = this;
	self.trainNbr = ko.observable(data.serial);
	self.startStn = ko.observable(data.startStn);
	self.startTime = ko.observable(data.startTime);
	self.endStn = ko.observable(data.endStn);
	self.endTime = ko.observable(data.endTime);
	self.spareFlag = ko.observable(data.spareFlag);
	self.sourceType = ko.observable(data.sourceType);
	self.yxType = ko.observable(data.trainType);
}