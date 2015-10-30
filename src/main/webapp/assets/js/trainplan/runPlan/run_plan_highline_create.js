var canvasData = {};
var cross = null;
$(function() {
	heightAuto();
	cross = new CrossModel();
	ko.applyBindings(cross);

	cross.init();
});

function heightAuto() {
	var WH = $(window).height();
	// alert("window: 860---"+WH);
	$("#plan_view_div_palnDayDetail").css("height", WH - 110 + "px");
};

var highlingFlags = [ {
	"value" : "0",
	"text" : "普速"
}, {
	"value" : "1",
	"text" : "高铁"
}, {
	"value" : "2",
	"text" : "混合"
} ];
var checkFlags = [ {
	"value" : "1",
	"text" : "已"
}, {
	"value" : "0",
	"text" : "未"
} ];
var unitCreateFlags = [ {
	"value" : "0",
	"text" : "未"
}, {
	"value" : "1",
	"text" : "已"
}, {
	"value" : "2",
	"text" : "全"
} ];
var highlingrules = [ {
	"value" : "1",
	"text" : "平日"
}, {
	"value" : "2",
	"text" : "周末"
}, {
	"value" : "3",
	"text" : "高峰"
} ];
var commonlinerules = [ {
	"value" : "1",
	"text" : "每日"
}, {
	"value" : "2",
	"text" : "隔日"
} ];

var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";
function hasActiveRole(bureau) {
	// var roleKey = _cross_role_key_pre + bureau;
	// return all_role.indexOf(roleKey) > -1;

	if (currentUserBureau == bureau) {
		return true;
	} else {
		return false;
	}
}

// {unitCrossId:"1",status: "1"}
/**
 * 接受后台推送消息
 */
function updateTrainRunPlanStatus(message) {
	var runPlan = $.parseJSON(message);
	cross.updateTrainRunPlanStatus(runPlan);

}
// {unitCrossId:"1",trainNbr: "G1", day:"20140723", runFlag: "1"};
function updateTrainRunPlanDayFlag(data) {
	var runPlan = $.parseJSON(data);
	cross.updateTrainRunPlanDayFlag(runPlan);
}

var gloabBureaus = [];

function CrossModel() {
	var self = this;
	// 列车列表
	self.trains = ko.observableArray();

	self.stns = ko.observableArray();
	// 交路列表
	self.crossAllcheckBox = ko.observable(0);
	// 当前展示的列车信息
	self.trainPlans = ko.observableArray();

	// 表头的数据列表
	self.planDays = ko.observableArray();
	self.removeArrayAfterCreateRunLine = ko.observableArray();// 清除该临时对象，避免每次点击生成运行线按钮重复提交数据

	self.gloabBureaus = [];
	// 担当局
	self.searchModle = ko.observable(new searchModle());

	self.runPlanTableWidth = ko.computed(function() {
		return self.planDays().length * 40 + 80 + 'px';
	});

	self.createRunPlanTotalCount = ko.observable(0);

	self.createRunPlanCompletedCount = ko.observable(0);

	self.createRunPlanErrorCount = ko.observable(0);

	self.selectedRunPlan = ko.observableArray();

	self.searchTypeChange = function() {
		self.selectedRunPlan.remove(function(item) {
			return true;
		});
		self.trainPlans.remove(function(item) {
			return true;
		});
	};

	self.selectRunPlan = function(row) {
		if (row.selected() == 0) {
			row.selected(1);
			self.selectedRunPlan.push(row);
		} else {
			row.selected(0);
			self.selectedRunPlan.remove(row);
		}
	};

	self.completedMessage = ko.computed(function() {
		// return self.createRunPlanTotalCount() == 0 ? "" : "选中：" +
		// self.createRunPlanTotalCount() + "个计划，目前已成功生成：" +
		// self.createRunPlanCompletedCount() + "个运行线，另有：" +
		// self.createRunPlanErrorCount() + "个运行线失败";
		// 2015-2-4 11:14:36 计划=运行线
		return self.createRunPlanTotalCount() == 0 ? "" : "选中："
				+ self.createRunPlanTotalCount() + "个计划，目前已成功生成："
				+ self.createRunPlanTotalCount() + "个运行线，另有："
				+ self.createRunPlanErrorCount() + "个运行线失败";
	});

	self.updateTrainRunPlanDayFlag = function(runPlan) {
		$.each(self.selectedRunPlan(), function(x, n) {
			if (n.planTrainId == runPlan.planTrainId) {
				n.createFlag(runPlan.createFlag);
				return false;
			}
			;
		});
	};

	self.updateTrainRunPlanStatus = function(runPlan) {
		if (runPlan.createFlag == 1) {
			self
					.createRunPlanCompletedCount(self
							.createRunPlanCompletedCount() + 1);
		} else {
			self.createRunPlanErrorCount(self.createRunPlanErrorCount() + 1);
		}
		$.each(self.selectedRunPlan(), function(x, n) {
			if (n.planTrainId() == runPlan.planTrainId) {
				n.selected(0);
				n.createFlag(runPlan.createFlag);

				self.removeArrayAfterCreateRunLine.push(n);// 用于避免每次点击生成运行线按钮重复提交数据
				return false;
			}
			;

		});

	};

	self.trainRunPlanChange = function(row, event) {

	};

	self.dragRunPlan = function(n, event) {
		$(event.target).dialog("open");
	};

	/**
	 * 交路复选框change事件
	 */
	self.selectCrosses = function() {
		$.each(self.trainPlans(), function(i, crossRow) {
			if (self.crossAllcheckBox() == 1) {// 全不选
				if (crossRow.canCheck2) {
					crossRow.selected(0);
					$.each(crossRow.runPlans(), function(z, n) {
						if ((n.createFlag() == 0 || n.createFlag() == "0")
								&& n.runFlagShowValue() != "") {
							n.selected(0);
							self.selectedRunPlan.remove(n);
						}
					});
				}
			} else {// 全选
				if (crossRow.canCheck2) {
					crossRow.selected(1);
					$.each(crossRow.runPlans(), function(z, n) {
						if ((n.createFlag() == "0" || n.createFlag() == 0)
								&& n.runFlagShowValue() != "") {
							n.selected(1);
							if ($.inArray(n, self.selectedRunPlan()) < 0) {// 已选中集合中不包含该对象
								self.selectedRunPlan.push(n);
							}
						}
					});
				}
			}
		});

	};

	self.selectCross = function(row) {
		if (row.selected() == 0) {// 整条交路全选
			self.crossAllcheckBox(1);
			if (self.searchModle().searchType() != '3') {// 临客
				$.each(self.trainPlans(), function(i, crossRow) {
					if (crossRow.trainSort == 0 && crossRow.selected() != 1
							&& crossRow != row) {
						self.crossAllcheckBox(0);
						return false;
					}
				});
			} else {
				$.each(self.trainPlans(), function(i, crossRow) {
					if (crossRow.selected() != 1 && crossRow != row) {
						self.crossAllcheckBox(0);
						return false;
					}
				});
			}
			// 如果是临客的匹配的车的ID
			if (self.searchModle().searchType() == '3') {// 临客
				$.each(self.trainPlans(), function(i, train) {
					if (train.telCmdId == row.telCmdId) {
						$.each(train.runPlans(), function(z, n) {
							if (n.createFlag() == "0") {
								n.selected(1);
								if ($.inArray(n, self.selectedRunPlan()) < 0) {// 已选中集合中不包含该对象
									self.selectedRunPlan.push(n);
								}
							}
						});
					}
				});
			} else {// 如果是秃顶匹配的plancrossID
				$.each(self.trainPlans(), function(i, train) {
					if (train.planCrossId == row.planCrossId) {
						$.each(train.runPlans(), function(z, n) {
							if (n.createFlag() == "0") {
								n.selected(1);
								if ($.inArray(n, self.selectedRunPlan()) < 0) {// 已选中集合中不包含该对象
									self.selectedRunPlan.push(n);
								}
							}
						});
					}
				});
			}

		} else {// 整条交路全不选
			self.crossAllcheckBox(0);
			if (self.searchModle().searchType() == '3') {// 临客
				$.each(self.trainPlans(), function(i, train) {
					if (train.telCmdId == row.telCmdId) {
						$.each(train.runPlans(), function(z, n) {
							n.selected(0);
							if ($.inArray(n, self.selectedRunPlan()) < 0) {// 已选中集合中不包含该对象
								self.selectedRunPlan.push(n);
							}
						});
					}
				});
			} else {
				$.each(self.trainPlans(), function(i, train) {
					if (train.planCrossId == row.planCrossId) {
						$.each(train.runPlans(), function(z, n) {
							if (n.createFlag() == 0) {
								n.selected(0);
								self.selectedRunPlan.remove(n);
							}
						});
					}
				});
			}

		}
		;
	};

	// cross基础信息中的下拉列表
	self.loadBureau = function(bureaus) {
		for (var i = 0; i < bureaus.length; i++) {
			self.tokenVehBureaus.push(new BureausRow(bureaus[i]));
			if (bureaus[i].code == self.tokenVehBureau()) {
				self.tokenVehBureau(bureaus[i]);
				break;
			}
		}
	};

	self.trainNbrChange = function(n, event) {
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};

	self.filterCrosses = function() {
		var filterTrainNbr = self.searchModle().filterTrainNbr();
		filterTrainNbr = filterTrainNbr || filterTrainNbr != "" ? filterTrainNbr
				.toUpperCase()
				: "all";
		if (filterTrainNbr == "all") {
			$.each(self.crossRows.rows(), function(n, crossRow) {
				crossRow.visiableRow(true);
			});
		} else {
			$.each(self.crossRows.rows(), function(n, crossRow) {
				if (crossRow.crossName().indexOf(filterTrainNbr) > -1) {
					crossRow.visiableRow(true);
				} else {
					crossRow.visiableRow(false);
				}
			});
		}
		;
	};

	// currentIndex
	// 格式化出MMdd这样的字符串
	self.dayHeader = function(d) {

		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
		var days = d.getDate();
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return month + "-" + days;
	};
	// 格式化出一、二
	self.getWeek = function(d) {
		var week = "";
		// 获取当前星期X(0-6,0代表星期天)
		if (d.getDay() == 0)
			week = "日";
		if (d.getDay() == 1)
			week = "一";
		if (d.getDay() == 2)
			week = "二";
		if (d.getDay() == 3)
			week = "三";
		if (d.getDay() == 4)
			week = "四";
		if (d.getDay() == 5)
			week = "五";
		if (d.getDay() == 6)
			week = "六";

		return week;
	};

	self.init = function() {

		$("#run_plan_train_times").dialog("close");

		$("#runplan_input_startDate").datepicker();
		$("#runplan_input_endDate").datepicker();

		self.searchModle().startDay(moment().format('YYYY-MM-DD'));
		self.searchModle().planStartDate(moment().format('YYYY-MM-DD'));
		self.searchModle().planEndDate(
				moment().add("day", 40).format('YYYY-MM-DD'));

		commonJsScreenLock(2);
		// 获取当期系统日期
		// $.ajax({
		// url : basePath+"/jbtcx/querySchemes",
		// cache : false,
		// type : "POST",
		// dataType : "json",
		// contentType : "application/json",
		// data :JSON.stringify({}),
		// success : function(result) {
		// if (result != null && result != "undefind" && result.code == "0") {
		// if (result.data !=null) {
		// self.searchModle().loadChats(result.data);
		// }
		// } else {
		// showErrorDialog("接口调用返回错误，code="+result.code+"
		// message:"+result.message);
		// }
		// },
		// error : function() {
		// showErrorDialog("接口调用失败");
		// },
		// complete : function(){
		// commonJsScreenUnLock();
		// }
		// });

		$.ajax({
			url : basePath + "/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json",
			success : function(result) {
				var bureaus = new Array();
				if (result != null && result != "undefind"
						&& result.code == "0") {
					var curUserBur = currentBureauShortName;
					var fistObj = null;
					if (result.data != null && result.data != undefined) {
						for (var i = 0; i < result.data.length; i++) {
							var obj = result.data[i];
							if (obj.ljjc == curUserBur) {
								fistObj = obj;
							} else {
								bureaus.push(obj);
							}
						}
						if (fistObj != null) {
							bureaus.unshift(fistObj);
						}
					}
					self.searchModle().loadBureau(bureaus);
					if (result.data != null) {
						$.each(result.data, function(n, bureau) {
							self.gloabBureaus.push({
								"shortName" : bureau.ljjc,
								"code" : bureau.ljpym
							});
							gloabBureaus.push({
								"shortName" : bureau.ljjc,
								"code" : bureau.ljpym
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

		self.initDataHeader();
	};
	self.initDataHeader = function() {
		var startDate = $("#runplan_input_startDate").val();
		var endDate = $("#runplan_input_endDate").val();

		self.crossAllcheckBox(0);
		var currentTime = new Date(startDate);
		var endTime = new Date(endDate);
		endTime.setDate(endTime.getDate());
		var endTimeStr = self.dayHeader(endTime);
		self.planDays.remove(function(item) {
			return true;
		});
		self.trainPlans.remove(function(item) {
			return true;
		});

		self.planDays.push({
			"day" : self.dayHeader(currentTime).replace(/-/g, ""),
			"week" : self.getWeek(currentTime)
		});
		while (self.dayHeader(currentTime) != endTimeStr) {
			currentTime.setDate(currentTime.getDate() + 1);
			self.planDays.push({
				"day" : self.dayHeader(currentTime).replace(/-/g, ""),
				"week" : self.getWeek(currentTime)
			});
		}

	};

	self.text = function(tokenVehBureau) {
		switch (tokenVehBureau) {
		case 'P':
			return "京";
			break;
		case 'B':
			return "哈";
			break;
		case 'T':
			return "沈";
			break;
		case 'V':
			return "太";
			break;
		case 'C':
			return "呼";
			break;
		case 'F':
			return "郑";
			break;
		case 'N':
			return "武";
			break;
		case 'Y':
			return "西";
			break;
		case 'K':
			return "济";
			break;
		case 'H':
			return "上";
			break;
		case 'G':
			return "南";
			break;
		case 'Q':
			return "广";
			break;
		case 'Z':
			return "宁";
			break;
		case 'W':
			return "成";
			break;
		case 'M':
			return "昆";
			break;
		case 'J':
			return "兰";
			break;
		case 'R':
			return "乌";
			break;
		case 'O':
			return "青";
			break;
		}
	}

	self.yyyyMMdd = function(d) {
		var year = d.getFullYear();
		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
		var days = d.getDate();
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};

	// 查询所有的交路单元，因为没有使用提供未分页的查询函数，这里设置一个无法达到的总数5000后台提供以后可以替换掉
	self.loadCrosses = function() {
		// 清除复选框勾选集合数据
		self.selectedRunPlan.remove(function(item) {
			return true;
		});

		self.crossRows.loadRows();// loadCrosseForPage(0, 5000);
	};

	// 查询所有的交路单元
	self.loadCrosseForPage = function(startIndex, endIndex) {
		var startDate = $("#runplan_input_startDate").val();
		var endDate = $("#runplan_input_endDate").val();
		if (startDate != null && startDate != undefined && endDate != null
				&& endDate != undefined) {
			if (startDate > endDate) {
				showWarningDialog("开始日期不能大于截止日期!");
				return;
			}
		}
		commonJsScreenLock();

		var bureauCode = self.searchModle().bureau();
		var highlingFlag = self.searchModle().highlingFlag();
		var trainNbr = self.searchModle().filterTrainNbr();
		var checkFlag = self.searchModle().checkFlag();
		var unitCreateFlag = self.searchModle().unitCreateFlag();
		var startBureauCode = self.searchModle().startBureau();
		// var tokenVehBureaus = self.text($("#tokenVehBureau").val());
		var tokenVehBureaus = $("#tokenVehBureau").val();

		var endTime = new Date(endDate);
		endTime.setDate(endTime.getDate());
		var endTimeStr = self.yyyyMMdd(endTime);

		self.createRunPlanTotalCount(0);

		self.createRunPlanCompletedCount(0);

		self.createRunPlanErrorCount(0);

		self.initDataHeader();

		var chart = self.searchModle().chart();
		// 记住这次查询所选则的方案名
		// $("#searchModleHiddenValue").val(chart.name);

		$
				.ajax({
					url : basePath + "/runPlan/getTrainRunPlansForCreateLine",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data : JSON.stringify({
						createType : self.searchModle().searchType(),// 0图定
																		// 3：临客
						tokenVehBureau : bureauCode,
						startBureau : startBureauCode,
						trainNbr : trainNbr,
						// chartId : chart.chartId,
						startDay : startDate.replace(/-/g, ""),
						endDay : endTimeStr.replace(/-/g, ""),
						rownumstart : startIndex,
						rownumend : endIndex,
						highlineFlag : "1",
						tokenVehBureaus : tokenVehBureaus
					}),
					success : function(result) {
						var _chirldrenIndex = 0;
						var rows = [];
						if (result != null && result != "undefind"
								&& result.code == "0") {
							if (self.searchModle().searchType() != '3') {// 图定
								var trainPlans = {};
								var sort = 0;
								var trainNbr = "";
								var cn;
								var pcId = "";
								// var nbr =
								// "05133-D638/5-D636/7-D638/5-D636/7-D5134";
								$
										.each(
												result.data.data,
												function(z, n) {
													// if(cn != n.crossName){
													// sort = 0;
													// }
													if (pcId != n.planCrossId) {
														sort = 0;
													}
													if (trainNbr != n.trainNbr) {
														trainNbr = n.trainNbr;
														// cn = n.crossName;
														pcId = n.planCrossId;
														++sort;
													}
													var planCross = trainPlans[n.planCrossId];
													// var planCross = null;
													// if(n.crossName!=null &&
													// n.crossName!=undefined){
													// planCross =
													// trainPlans[n.crossName];
													// }
													// console.log("planCross::"
													// + planCross);
													// console.log("self.trainPlans()"
													// + self.trainPlans());
													if (planCross == null) {
														// console.log("planCross
														// == null");
														var trainPlanData = {
															crossName : n.crossName,
															planCrossId : n.planCrossId,
															tokenVehBureau : n.tokenVehBureau,
															startDate : startDate,
															endDate : endDate,
															baseTrainId : n.baseTrainId,
															createFlag : 0,
															trainSort : 0,
															startStn : n.startStn,
															endStn : n.endStn,
															chirldrenIndex : _chirldrenIndex
														// 用于界面显示序号
														};

														rows
																.push(trainPlanData);// 分页集合对象，只用记录条数，数据暂未使用

														_chirldrenIndex++;
														// 默认吧交路作为第一条记录
														var planCross = new TrainRunPlanRow(
																trainPlanData);
														self.trainPlans
																.push(planCross);
														var crossNames = n.crossName
																.split("-");
														var tn = "";
														var pcid = "";
														var forRdd = 0;
														if (n.runDay != null) {
															// 有开行计划,就需要对交路进行分割，填写对应的始发/终到站信息，以便可以在686行进行准确判断
															$
																	.each(
																			result.data.data,
																			function(
																					rdd,
																					rdd1) {
																				if (rdd1.groupSerialNbr == 1
																						|| rdd1.groupSerialNbr == null) {
																					if (n.crossName == rdd1.crossName
																							&& n.planCrossId == rdd1.planCrossId) {
																						if (tn != rdd1.trainNbr) {
																							if (pcid != rdd1.planCrossId) {
																								forRdd = 0;
																								pcid = rdd1.planCrossId;
																							}
																							var trainPlanData = {
																								crossName : rdd1.crossName,
																								startDate : startDate,
																								endDate : endDate,
																								planCrossId : pcid,
																								trainNbr : rdd1.crossName
																										.split("-")[forRdd],
																								trainSort : forRdd + 1,
																								createFlag : 0,
																								startStn : rdd1.startStn,
																								endStn : rdd1.endStn,
																								tokenVehBureau : rdd1.tokenVehBureau
																							};
																							self.trainPlans
																									.push(new TrainRunPlanRow(
																											trainPlanData));
																							tn = rdd1.trainNbr;
																							forRdd++;
																						}
																					}
																				}
																			})
														} else {
															// 没有开行计划,就不存在“开”怎么显示，不存在车次应该显示的那个位置，只需要在列表中显示出来就行
															for (var i = 0; i < crossNames.length; i++) {
																var trainPlanData = {
																	crossName : n.crossName,
																	startDate : startDate,
																	endDate : endDate,
																	planCrossId : n.planCrossId,
																	trainNbr : crossNames[i],
																	trainSort : i + 1,
																	createFlag : 0,
																	startStn : n.startStn,
																	endStn : n.endStn,
																	tokenVehBureau : n.tokenVehBureau
																};
																self.trainPlans
																		.push(new TrainRunPlanRow(
																				trainPlanData));
															}
															;
														}
														/** 原版 * */
														// for(var i = 0; i <
														// crossNames.length;
														// i++){
														// var trainPlanData = {
														// crossName:
														// n.crossName,
														// startDate: startDate,
														// endDate: endDate,
														// planCrossId:
														// n.planCrossId,
														// trainNbr:
														// crossNames[i],
														// trainSort: i + 1,
														// createFlag: 0,
														// startStn: n.startStn,
														// endStn: n.endStn,
														// tokenVehBureau:
														// n.tokenVehBureau
														// };
														// console.log("for循环" +
														// i + "次" +
														// ",startStn=" +
														// n.startStn +
														// ",endStn=" +
														// n.endStn);
														// self.trainPlans.push(new
														// TrainRunPlanRow(trainPlanData));
														// } ;
														trainPlans[n.planCrossId] = planCross;
													}
													// console.log("↑↑↑↑" +
													// self.trainPlans().length);
													// console.log("-----------------------------------");
													$
															.each(
																	self
																			.trainPlans(),
																	function(x,
																			t) {
																		// console.log("↑↑↑↑"
																		// +
																		// n.trainNbr
																		// + ","
																		// +
																		// n.startStn
																		// +
																		// "↓↓↓↓"
																		// +
																		// t.trainNbr
																		// + ","
																		// +
																		// t.startStn);
																		// console.log("↑↑↑↑"
																		// +
																		// n.trainNbr
																		// + ","
																		// +
																		// sort
																		// +
																		// "↓↓↓↓"
																		// +
																		// t.trainNbr
																		// + ","
																		// +
																		// t.trainSort);
																		// 取消trainSort判断，是因为现在讲“开”显示改为：第二组的车辆根据
																		// 始发/终到确定在第一组的位置
																		if (t.planCrossId == n.planCrossId
																				&& t.trainNbr == n.trainNbr
																				// &&
																				// t.trainSort
																				// ==
																				// sort
																				&& t.startStn == n.startStn
																				&& t.endStn == n.endStn) {
																			for (var i = 0; i < t
																					.runPlans().length; i++) {
																				if (t
																						.runPlans()[i].day
																						.replace(
																								/-/g,
																								"") == n.runDay) {
																					t
																							.runPlans()[i]
																							.runFlag(parseInt(n.runFlag));
																					t
																							.runPlans()[i]
																							.createFlag(parseInt(n.createFlag));
																					t
																							.runPlans()[i]
																							.planTrainId(n.planTrainId);
																					t
																							.runPlans()[i]
																							.baseTrainId(n.baseTrainId);
																					break;
																				}
																				;
																			}
																			;
																			return false;
																		}
																		;
																	});

												});
							} else {// 临客
								var trainPlans = {};
								$
										.each(
												result.data.data,
												function(z, n) {
													var planCross = trainPlans[n.telCmdId];
													if (planCross == null) {

														var trainPlanData = {
															telCmdId : n.telCmdId,
															crossName : n.telCmdId,
															trainNbr : n.trainNbr,
															planCrossId : n.planCrossId,
															tokenVehBureau : n.tokenVehBureau,
															startDate : startDate,
															endDate : endDate,
															baseTrainId : n.baseTrainId,
															createFlag : 0,
															trainSort : 1,
															chirldrenIndex : _chirldrenIndex
														// 用于界面显示序号
														};
														rows
																.push(trainPlanData);// 分页集合对象，只用记录条数，数据暂未使用
														_chirldrenIndex++;
														// 默认吧交路作为第一条记录
														var planCross = new TrainRunPlanRow(
																trainPlanData);
														self.trainPlans
																.push(planCross);
														trainPlans[n.telCmdId] = planCross;
													}
													$
															.each(
																	self
																			.trainPlans(),
																	function(x,
																			t) {
																		if (t.trainNbr == n.trainNbr
																				&& t.telCmdId == n.telCmdId) {
																			for (var i = 0; i < t
																					.runPlans().length; i++) {
																				if (t
																						.runPlans()[i].day
																						.replace(
																								/-/g,
																								"") == n.runDay) {
																					t
																							.runPlans()[i]
																							.runFlag(parseInt(n.runFlag));
																					t
																							.runPlans()[i]
																							.createFlag(parseInt(n.createFlag));
																					t
																							.runPlans()[i]
																							.planTrainId(n.planTrainId);
																					t
																							.runPlans()[i]
																							.baseTrainId(n.baseTrainId);
																					break;
																				}
																				;
																			}
																			;
																			return false;
																		}
																		;
																	});

												});
							}
							self.crossRows.loadPageRows(
									result.data.totalRecord, rows);
						} else {
							showErrorDialog("获取交路单元信息失败");
						}
						;
					},
					error : function() {
						showErrorDialog("获取交路单元信息失败");
					},
					complete : function() {
						commonJsScreenUnLock();
					}
				});
	};
	// 必须定义在load函数之后
	self.crossRows = new PageModle(20, self.loadCrosseForPage);

	/**
	 * 生成运行线
	 */
	self.createTrainLines = function() {
		// 清除该临时对象，避免每次点击生成运行线按钮重复提交数据
		for (var i = 0; i < self.removeArrayAfterCreateRunLine().length; i++) {
			self.selectedRunPlan
					.remove(self.removeArrayAfterCreateRunLine()[i]);
		}

		var planTrains = [];
		var oldPlanTrains = [];
		for (var i = 0; i < self.selectedRunPlan().length; i++) {
			if (self.selectedRunPlan()[i].selected() == 1) {
				planTrains.push({
					planTrainId : self.selectedRunPlan()[i].planTrainId(),
					baseTrainId : self.selectedRunPlan()[i].baseTrainId(),
					day : self.selectedRunPlan()[i].day
				});
			} else {
				oldPlanTrains.push(self.selectedRunPlan()[i]);
			}
		}

		$.each(oldPlanTrains, function(i, n) {
			self.selectedRunPlan.remove(n);
		});

		self.createRunPlanTotalCount(self.selectedRunPlan().length);

		self.createRunPlanCompletedCount(0);

		self.createRunPlanErrorCount(0);

		var planStartDate = $("#runplan_input_startDate").val();
		var planEndDate = $("#runplan_input_endDate").val();

		commonJsScreenLock();
		$.ajax({
			url : basePath + "/runPlan/createRunPlanForPlanTrain",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				planTrains : planTrains,
				msgReceiveUrl : basePath + "/runPlan/runPlanHighLineCreate",
				highlineFlag : "1",
				startDate : planStartDate,
				endDate : planEndDate
			}),

			success : function(result) {
				if (result != null && result.code == 0) {
					showSuccessDialog("正在生成运行线");

				} else {
					showErrorDialog("生成运行线失败");
				}
			},
			error : function() {
				showErrorDialog("生成运行线失败");
			},
			complete : function() {

				commonJsScreenUnLock();
			}
		});
	};
	self.clearData = function() {
		self.currentCross(new CrossRow({
			"crossId" : "",
			"crossName" : "",
			"chartId" : "",
			"chartName" : "",
			"crossStartDate" : "",
			"crossEndDate" : "",
			"crossSpareName" : "",
			"alterNateDate" : "",
			"alterNateTranNbr" : "",
			"spareFlag" : "",
			"cutOld" : "",
			"groupTotalNbr" : "",
			"pairNbr" : "",
			"highlineFlag" : "",
			"highlineRule" : "",
			"commonlineRule" : "",
			"appointWeek" : "",
			"appointDay" : "",
			"crossSection" : "",
			"throughline" : "",
			"startBureau" : "",
			"tokenVehBureau" : "",
			"tokenVehDept" : "",
			"tokenVehDepot" : "",
			"appointPeriod" : "",
			"tokenPsgBureau" : "",
			"tokenPsgDept" : "",
			"tokenPsgDepot" : "",
			"locoType" : "",
			"crhType" : "",
			"elecSupply" : "",
			"dejCollect" : "",
			"airCondition" : "",
			"note" : "",
			"createPeople" : "",
			"createPeopleOrg" : "",
			"createTime" : ""
		}));
		self.stns.remove(function(item) {
			return true;
		});
		self.planCrossRows.remove(function(item) {
			return true;
		});

		self.trainPlans.remove(function(item) {
			return true;
		});
		self.planDays.remove(function(item) {
			return true;
		});
		self.trains.remove(function(item) {
			return true;
		});
		self.currentTrain = ko.observable();
	};
	self.bureauChange = function() {
		if (hasActiveRole(self.searchModle().bureau())) {
			self.searchModle().activeFlag(1);
			// self.clearData();
		} else if (self.searchModle().activeFlag() == 1) {
			self.searchModle().activeFlag(0);
			// self.clearData();
		}
	};

	// 审核
	self.checkCrossInfo = function() {
		var crossIds = "";
		var checkedCrosses = [];
		var crosses = self.planCrossRows();
		for (var i = 0; i < crosses.length; i++) {
			if (crosses[i].checkFlag() == 2 && crosses[i].selected() == 1) {
				showWarningDialog(crosses[i].crossName() + "已审核");
				return;
			} else if (crosses[i].checkedBureau() != null
					&& crosses[i].checkedBureau().indexOf(currentUserBureau) > -1
					&& crosses[i].selected() == 1) {
				showWarningDialog(crosses[i].crossName() + "本局已审核");
				return;
			} else if (crosses[i].selected() == 1) {
				crossIds += (crossIds == "" ? "" : ";");
				crossIds += crosses[i].planCrossId() + "#"
						+ crosses[i].relevantBureau();
				checkedCrosses.push(crosses[i]);
			}
			;
		}
		var planStartDate = $("#runplan_input_startDate").val();
		var planEndDate = $("#runplan_input_endDate").val();

		if (crossIds == "") {
			showWarningDialog("没有可审核的");
			return;
		}
		commonJsScreenLock();
		$.ajax({
			url : basePath + "/runPlan/checkCrossRunLine",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				startTime : (planStartDate != null ? planStartDate : moment()
						.format('YYYY-MM-DD')).replace(/-/g, ''),
				endTime : (planEndDate != null ? planEndDate : moment().add(
						"day", 40).format('YYYY-MM-DD')).replace(/-/g, ''),
				planCrossIds : crossIds
			}),
			success : function(result) {
				if (result.code == "0") {
					$.each(checkedCrosses, function(i, n) {
						n.checkedBureau(n.checkedBureau() + ","
								+ currentUserBureau);
					});
					showSuccessDialog("审核成功");
				} else {
					showErrorDialog("审核失败");
				}
			},
			error : function() {
				showErrorDialog("审核失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});

	};
	// {unitCrossId:"1",trainNbr: "G1", day:"20140723", runFlag: "1"};
	// {unitCrossId:"1",status: "1"}
	self.createCrossMap = function(row) {

		// var planStartDate = self.searchModle().planStartDate();
		//		
		// var planEndDate = self.searchModle().planEndDate();
		var planStartDate = $("#runplan_input_startDate").val();

		var planEndDate = $("#runplan_input_endDate").val();

		$.ajax({
			url : basePath + "/cross/createCrossMap",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				planCrossId : row.planCrossId(),
				startTime : planStartDate != null ? planStartDate : moment()
						.format('YYYY-MM-DD'),
				endTime : planEndDate != null ? planEndDate : moment().add(
						"day", 40).format('YYYY-MM-DD')
			}),
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null) {
					}

				} else {
					showErrorDialog("获取车底交路绘图数据失败");
				}
			},
			error : function() {
				showErrorDialog("获取车底交路绘图数据失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	};

	// 尚未被使用
	self.showTrains = function(row) {
		self.setCurrentCross(row);
		commonJsScreenLock(3);
		self.createCrossMap(row);
		// self.stns.remove(function(item) {
		// return true;
		// });
		self.trains.remove(function(item) {
			return true;
		});

		var trains = row.crossName().split("-");

		$.each(trains, function(n, trainNbr) {
			var row = new TrainRow({
				"trainNbr" : trainNbr
			});
			self.trains.push(row);
		});

		self.loadRunPlans(row.planCrossId());

		$.ajax({
			url : basePath + "/cross/getPlanCrossInfo",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				planCrossId : row.planCrossId()
			}),
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null && result.data.length > 0) {
						if (result.data[0].crossInfo != null) {
							self.setCurrentCross(new CrossRow(
									result.data[0].crossInfo));
						} else {
							self
									.setCurrentCross(new CrossRow(
											self.defualtCross));
							showWarningDialog("没有找打对应的交路被找到");
						}
					}

				} else {
					showErrorDialog("获取列车列表失败");
				}
			},
			error : function() {
				showErrorDialog("获取列车列表失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});

	};
	self.removeArrayValue = function(arr, value) {
		var index = -1;
		for (var i = 0; i < arr.length; i++) {
			if (value == arr[i]) {
				index = i;
				break;
			}
		}
		if (index > -1) {
			arr.splice(index, 1);
		}
	};
	self.drawFlagChange = function(a, n) {
		if (n.target.checked) {
			self.searchModle().drawFlags.push(n.target.value);
		} else {
			self.removeArrayValue(self.searchModle().drawFlags(),
					n.target.value);
		}
	};

	self.filterCrosses = function() {
		var filterCheckFlag = self.searchModle().filterCheckFlag();
		var filterUnitCreateFlag = self.searchModle().filterUnitCreateFlag();
		$.each(self.crossRows.rows(), function(n, crossRow) {
			if (crossRow.checkFlag() == filterCheckFlag
					|| crossRow.unitCreateFlag() == filterUnitCreateFlag) {
				crossRow.visiableRow(true);
			} else {
				crossRow.visiableRow(false);
			}

		});
	};
}

function searchModle() {
	self = this;

	self.activeFlag = ko.observable(0);

	self.checkActiveFlag = ko.observable(0);

	self.activeCurrentCrossFlag = ko.observable(0);

	self.drawFlags = ko.observableArray([ '0' ]);

	self.planStartDate = ko.observable();

	self.currentBureanFlag = ko.observable(0);

	self.planEndDate = ko.observable();

	self.bureaus = ko.observableArray();

	self.startBureaus = ko.observableArray();

	self.charts = ko.observableArray();

	self.highlingFlags = highlingFlags;

	self.unitCreateFlags = unitCreateFlags;

	self.filterCheckFlag = ko.observable(0);

	self.filterUnitCreateFlag = ko.observable(0);

	self.checkFlags = checkFlags;

	self.highlingFlag = ko.observable();

	self.checkFlag = ko.observable();

	self.unitCreateFlag = ko.observable();

	self.bureau = ko.observable();

	self.chart = ko.observable();

	self.startDay = ko.observable();

	self.startBureau = ko.observable();

	self.filterTrainNbr = ko.observable();

	self.showCrossMap = ko.observable(0);

	self.shortNameFlag = ko.observable(2);

	self.searchType = ko.observable();

	self.searchTypes = [ {
		"code" : "0",
		"name" : "图定"
	}, {
		"code" : "3",
		"name" : "临客"
	} ];

	self.loadBureau = function(bureaus) {
		for (var i = 0; i < bureaus.length; i++) {
			self.bureaus.push(new BureausRow(bureaus[i]));
			self.startBureaus.push(new BureausRow(bureaus[i]));
		}
	};

	self.loadChats = function(charts) {
		for (var i = 0; i < charts.length; i++) {
			self.charts.push({
				"chartId" : charts[i].schemeId,
				"name" : charts[i].schemeName
			});
		}
	};

}

function BureausRow(data) {
	var self = this;
	self.shortName = data.ljjc;
	self.code = data.ljpym;
	// 方案ID
}

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
}

function TrainRow(data) {
	var self = this;
	self.crossTainId = data.crossTainId;// BASE_CROSS_TRAIN_ID
	self.crossId = data.crossId;// BASE_CROSS_ID
	self.trainSort = data.trainSort;// TRAIN_SORT
	self.baseTrainId = data.baseTrainId;
	self.trainNbr = data.trainNbr;// TRAIN_NBR
	self.startStn = data.startStn;// START_STN

	self.times = ko.observableArray();
	self.simpleTimes = ko.observableArray();
	self.loadTimes = function(times) {
		$.each(times, function(i, n) {
			var timeRow = new TrainTimeRow(n);
			self.times.push(timeRow);
			if (n.stationFlag != 'BTZ') {
				self.simpleTimes.push(timeRow);
			}
		});
	};
};
function filterValue(value) {
	return value == null || value == "null" ? "--" : value;
};

function TrainRunPlanRow(data) {
	var self = this;
	self.telCmdId = data.telCmdId;
	self.chirldrenIndex = data.chirldrenIndex;// 用于界面序号显示
	self.planCrossId = data.planCrossId;
	self.trainNbr = data.trainNbr;
	self.baseTrainId = data.baseTrainId;
	self.crossName = data.crossName;
	self.runPlans = ko.observableArray();
	self.selected = ko.observable(0);

	self.startStn = data.startStn;
	self.endStn = data.endStn;
	self.canCheck2 = ko.observable(true);
	self.createStatus = ko.observable(0);
	// 担当局
	self.tokenVehBureau = ko.observable(data.tokenVehBureau);
	// 担当局
	self.tokenVehBureauShowValue = ko.computed(function() {
		var result = "";
		if (data.tokenVehBureau != null && data.tokenVehBureau != "null") {
			var bs = data.tokenVehBureau.split("、");
			result = data.tokenVehBureau;
			for (var j = 0; j < bs.length; j++) {
				for (var i = 0; i < gloabBureaus.length; i++) {
					if (bs[j] == gloabBureaus[i].code) {
						result = result.replace(bs[j],
								gloabBureaus[i].shortName);
						break;
					}
					;
				}
				;
			}
			;
		}
		if (result == $("#currentBureauShortName").val()) {
			self.canCheck2 = true;
		} else
			self.canCheck2 = false;
		return result;
	});

	self.createStatusShowValue = ko
			.computed(function() {
				switch (self.createStatus()) {
				case 0:
					return self.crossName;// "";
					break;
				case 1:
					return self.crossName
							+ "&nbsp;&nbsp;<span class='label label-info'>(正在生成。。。。。。)</span>";// "(正在生成。。。。。。)";
					break;
				case 2:
					return self.crossName
							+ "&nbsp;&nbsp;<span class='label label-success'>(已生成运行线)</span>";// "(已生成运行线)";
					break;
				default:
					return self.crossName;// "";
					break;
				}
			});

	self.rowspan = ko.computed(function() {
		if (data.trainSort == 1) {
			return data.crossName.split("-").length;
		} else {
			return 1;
		}
	});

	self.colspan = ko.computed(function() {
		return self.runPlans().length + 2;
	});

	self.trainSort = data.trainSort;

	self.yyyyMMdd = function(d) {
		var year = d.getFullYear();
		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
		var days = d.getDate();
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};
	// 初始化开行计划的列表
	if (data.runPlans == null) {
		var currentTime = new Date(data.startDate);
		var endTime = new Date(data.endDate);
		endTime.setDate(endTime.getDate()); // + 10
		var endTimeStr = self.yyyyMMdd(endTime);

		self.runPlans.remove(function(item) {
			return true;
		});
		var curDay = self.yyyyMMdd(currentTime);
		self.runPlans.push(new RunPlanRow({
			"day" : curDay,
			"runFlag" : null,
			"createFlag" : null,
			planCrossId : data.planCrossId,
			baseChartId : data.baseChartId
		}));
		while (curDay != endTimeStr) {
			currentTime.setDate(currentTime.getDate() + 1);
			curDay = self.yyyyMMdd(currentTime);
			self.runPlans.push(new RunPlanRow({
				"day" : curDay,
				"runFlag" : null,
				"createFlag" : null,
				planCrossId : data.planCrossId,
				baseChartId : data.baseChartId
			}));
		}
	}
}

function RunPlanRow(data) {
	var self = this;
	self.day = data.day;
	self.runFlag = ko.observable("");
	self.createFlag = ko.observable("");
	self.planCrossId = data.planCrossId;
	self.planTrainId = ko.observable("");
	self.baseTrainId = ko.observable("");
	self.selected = ko.observable(0);
	self.color = ko.computed(function() {
		if (self.createFlag() == 1) {
			return "green";
		} else {
			return "gray";
		}
	});
	self.runFlagShowValue = ko.computed(function() {
		if (self.createFlag() == 1) {
			switch (self.runFlag()) {
			case 9:
				return "<span class='label label-danger'>停</span>";// "停";
				break;
			case 1:
				return "<span class='label label-success'>开</span>";// "开";
				break;
			case 2:
				return "<span class='label label-info'>备</span>";// "备";
				break;
			default:
				return '';
				break;
			}
		} else {
			switch (self.runFlag()) {
			case 9:
				return "停";
				break;
			case 1:
				return "开";
				break;
			case 2:
				return "备";
				break;
			default:
				return '';
				break;
			}
		}

		// switch (self.runFlag()) {
		// case 9:
		// return "<span class='label label-danger'>停</span>";//"停";
		// break;
		// case 1:
		// return "<span class='label label-success'>开</span>";//"开";
		// break;
		// case 2:
		// return "<span class='label label-info'>备</span>";//"备";
		// break;
		// default:
		// return '';
		// break;
		// }
	});
}

function TrainTimeRow(data) {
	var self = this;
	self.index = data.childIndex + 1;
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = filterValue(data.arrTime != null ? data.arrTime.replace(
			/-/g, "").substring(4) : "");
	self.targetTime = filterValue(data.dptTime != null ? data.dptTime.replace(
			/-/g, "").substring(4) : "");
	self.stepStr = GetDateDiff(data);
	self.trackName = filterValue(data.trackName);
	self.runDays = data.runDays;
	self.stationFlag = data.stationFlag;

};

function GetDays(data1, data2) {
	var startTime = new Date(data1);
	var endTime = new Date(data2);

	var date3 = endTime.getTime() - startTime.getTime(); // 时间差的毫秒数
	// 计算出相差天数
	var days = Math.floor(date3 / (24 * 3600 * 1000));

	return days;
}
function GetDateDiff(data) {
	if (data.childIndex == 0 || data.dptTime == '-' || data.dptTime == null
			|| data.arrTime == null || data.arrTime == '-') {
		return "";
	}
	var startTime = new Date(data.arrTime);
	var endTime = new Date(data.dptTime);
	var result = "";

	var date3 = endTime.getTime() - startTime.getTime(); // 时间差的毫秒数

	// 计算出相差天数
	var days = Math.floor(date3 / (24 * 3600 * 1000));

	result += days > 0 ? days + "天" : "";
	// 计算出小时数
	var leave1 = date3 % (24 * 3600 * 1000); // 计算天数后剩余的毫秒数
	var hours = Math.floor(leave1 / (3600 * 1000));

	result += hours > 0 ? hours + "小时" : "";

	// 计算相差分钟数
	var leave2 = leave1 % (3600 * 1000); // 计算小时数后剩余的毫秒数
	var minutes = Math.floor(leave2 / (60 * 1000));

	result += minutes > 0 ? minutes + "分" : "";
	// 计算相差秒数
	var leave3 = leave2 % (60 * 1000); // 计算分钟数后剩余的毫秒数
	var seconds = Math.round(leave3 / 1000);

	result += seconds > 0 ? seconds + "秒" : "";

	return result == "" ? "" : result;
};
function openLogin() {
	$("#file_upload_dlg").dialog("open");
}
