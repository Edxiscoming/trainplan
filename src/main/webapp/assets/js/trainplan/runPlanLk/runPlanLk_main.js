var canvasData = {};
var cross = null;
var cmdTrainId = null;
$(function() {
	// showTrains();
	heightAuto();
	cross = new CrossModel();
	ko.applyBindings(cross);

	cross.init();
});

function heightAuto() {
	var WH = $(window).height();
	// alert("window: 860---"+WH);
	$('#left_height').css("height", WH - 490 + "px");
	$('#canvas_parent_div').css("height", WH - 380 + "px");
};

var weekDays = [ "<b>日</b>", "一", "二", "三", "四", "五", "<b>六</b>" ];// 星期X数组
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
var checkFlagsju = [ {
	"value" : "1",
	"text" : "部分审核"
}, {
	"value" : "2",
	"text" : "全部审核"
}, {
	"value" : "0",
	"text" : "未审核"
} ];

var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";
function hasActiveRole(bureau) {
//	var roleKey = _cross_role_key_pre + bureau;
//	return all_role.indexOf(roleKey) > -1; 

	if (currentUserBureau == bureau) {
		return true;
	} else {
		return false;
	}
}

var gloabBureaus = [];

/**
 * 
 */
function CrossModel() {
	var self = this;
	// 列车列表
	self.trains = ko.observableArray();

	self.stns = ko.observableArray();
	// 交路列表
	self.crossAllcheckBox = ko.observable(0);

	self.trainPlans = ko.observableArray();

	self.planDays = ko.observableArray();

	self.gloabBureaus = [];

	self.times = ko.observableArray();

	self.simpleTimes = ko.observableArray();

	self.runPlanCanvasPage = new RunPlanCanvasPage(self);
	self.currentTrainInfoMessage = ko.observable("");
	self.currentTrain = ko.observable();
	self.checkplans = ko.observableArray();
	self.modifyPlanRecords = ko.observableArray();
	self.modifyPlanRecords1 = ko.observableArray();

	// 担当局
	self.searchModle = ko.observable(new searchModle());

	self.planTrainLkRows = ko.observableArray();

	self.cmdTrainLkRows = ko.observableArray();
	// 临客开行计划list

	self.checkburu = ko.observable();
	self.checkburuNo = ko.observable();
	/**
	 * 加载开行情况
	 * 
	 * @param crossId
	 */
	self.loadRunPlans = function(_trainNbr, _cmdTrainId) {
		var startDate = $("#runplan_input_startDate").val();
		var endDate = $("#runplan_input_endDate").val();

		var currentTime = moment(startDate).format("YYYY-MM-DD");
		var endTime = moment(endDate).format("MMDD");
		self.planDays.remove(function(item) {
			return true;
		});
		self.planDays.push({
			"day" : moment(currentTime).format("MMDD"),
			"week" : weekDays[moment(currentTime).weekday()],
			"weekDay" : moment(currentTime).weekday()
		});
		while (moment(currentTime).format("MMDD") != endTime) {
			currentTime = moment(currentTime).add("day", 1)
					.format('YYYY-MM-DD');
			self.planDays.push({
				"day" : moment(currentTime).format("MMDD"),
				"week" : weekDays[moment(currentTime).weekday()],
				"weekDay" : moment(currentTime).weekday()
			});
		}
		$.ajax({
			url : basePath + "/runPlanLk/getTrainLkRunPlans",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				startDate : startDate.replace(/-/g, ""),
				endDate : endDate.replace(/-/g, ""),
				trainNbr : _trainNbr,
				cmdTrainId : _cmdTrainId
			}),
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					self.trainPlans.remove(function(item) {
						return true;
					});
					$.each(result.data, function(i, n) {
						self.trainPlans.push(new TrainRunPlanRow(n));
					});

				} else {
					showErrorDialog("获取运行规律失败");
				}
			},
			error : function() {
				showErrorDialog("接口调用失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	};

	var ptId = null;
	// 右键事件
	self.showRightTrains = function(row, event) {
		$("#ml").hide();
		$(event.target).bind("contextmenu", function(e) {
			return false;
		});
		$("#checkhis").bind("contextmenu", function(e) {
			return false;
		});
		$("#checkhis").children().children().children().attr("id",
				row.cmdTrainId())
		// $("#checkhis").children().children().children().attr("planTrainId",row.planTrainId())
		ptId = row.planTrainId();
		if (3 == event.which) {
			if (3 == row.createType()) {
				$("#ml").show();
			}
			$("#checkhis").show();
			var locx = event.pageX;
			var locy = event.pageY;
			$("#checkhis").css("top", locy);
			$("#checkhis").css("left", locx);
		} else {
		}

	};
	// 右键事件结束,第一个弹出框(审核记录)

	self.showhis = function(event) {
		$('#hisshow').dialog("open");

		var id = $("#checkhis").children().children().children()[1].id;
		self.checkplans.remove(function(iten) {
			return true;

		})

		console.dir(id)
		$.ajax({

			url : basePath + "/runPlan/getCheckPlancmd1",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				checkCmdtel : id
			}),
			success : function(result) {
				if (result != null && result != "undefind") {
					// console.dir(result)

					$.each(result.data, function(n, checkplan) {

						self.checkplans.push(new CheckPlan(checkplan))
					});

				}
			},

			complete : function() {
				commonJsScreenUnLock();
			}

		});

	};

	// 第一个右键弹出结束
	// 第二个右键弹出————查看交路调整记录
	self.showHisModify = function(event) {
		$('#modifyRecordDiv').dialog("open");
		// var id =
		// $("#checkhis").children().children().children()[1].planTrainId;
//		self.modifyPlanRecords.remove(function(item) {
//			return true;
//		});

		self.modifyPlanRecords1.remove(function(item) {
			return true;
		});
		$.ajax({

			url : basePath + "/runPlanLk/getLkTrainModifyRecords",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				planTrainId : ptId
			}),
			success : function(result) {
				if (result != null && result != "undefind") {
//					$.each(result.data, function(n, modifyplan) {
//						self.modifyPlanRecords.push(new ModifyRecord(modifyplan))
//					});
					
					$.each(result.data, function(n, modifyplans) {
						self.modifyPlanRecords1.push(new ModifyRecordS(modifyplans));
					});
					
					/*
					 * if (typeof result.data =="object") {
					 * self.setCurrentCross(new CrossRow(result.data)); }else{
					 * self.setCurrentCross(new CrossRow(self.defualtCross));
					 * showWarningDialog("没有找打对应的交路被找到"); }
					 */
				} else {
					showErrorDialog("获取记录失败");
				}
			},
			error : function() {
				showErrorDialog("获取记录失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	};

	// ===========
	// 查看列车调整记录
	self.loadTrainModifyRecord = function(currentTrain) {
		$('#modifyRecordDiv').dialog("open");
		self.modifyPlanRecords.remove(function(item) {
			return true;
		});
		$.ajax({

			url : basePath + "/runPlanLk/getLkTrainModifyRecords",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				planTrainId : currentTrain.obj.planTrainId
			}),
			success : function(result) {
				if (result != null && result != "undefind") {
					$.each(result.data, function(n, modifyplan) {
						self.modifyPlanRecords
								.push(new ModifyRecord(modifyplan))
					});
					/*
					 * if (typeof result.data =="object") {
					 * self.setCurrentCross(new CrossRow(result.data)); }else{
					 * self.setCurrentCross(new CrossRow(self.defualtCross));
					 * showWarningDialog("没有找打对应的交路被找到"); }
					 */
				} else {
					showErrorDialog("获取记录失败");
				}
			},
			error : function() {
				showErrorDialog("获取记录失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	}
	// ===========
	// 查看某天某列车调整记录
	self.showOneModifyRecord = function(planTrainId) {

		$('#modifyRecordDiv').dialog("open");
		self.modifyPlanRecords.remove(function(item) {
			return true;
		});
		$.ajax({

			url : basePath + "/runPlan/getTrainModifyRecords",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				planTrainId : planTrainId
			}),
			success : function(result) {
				if (result != null && result != "undefind") {
					$.each(result.data, function(n, modifyplan) {
						self.modifyPlanRecords
								.push(new ModifyRecord(modifyplan))
					});
					if (typeof result.data == "object") {
						self.setCurrentCross(new CrossRow(result.data));
					} else {
						self.setCurrentCross(new CrossRow(self.defualtCross));
						showWarningDialog("没有找打对应的交路被找到");
					}
				} else {
					showErrorDialog("获取记录失败");
				}
			},
			error : function() {
				showErrorDialog("获取记录失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	}
	self.loadStns = function(currentTrain) {
		self.times.remove(function(item) {
			return true;
		});
		self.simpleTimes.remove(function(item) {
			return true;
		});
		commonJsScreenLock();
		$.ajax({
			url : basePath + "/jbtcx/queryPlanLineTrainTimesNewIframe?trainId="
					+ currentTrain.obj.planTrainId,
			cache : false,
			type : "GET",
			success : function(result) {
				$("#train_time_dlg").dialog("open");
				$("#train_time_dlg").find("iframe").attr(
						"src",
						"../jbtcx/queryPlanLineTrainTimesNewIframe?trainId="
								+ currentTrain.obj.planTrainId);
				$('#train_time_dlg').dialog(
						{
							title : "时刻表           " + "车次："
									+ currentTrain.obj.trainName,
							autoOpen : true,
							modal : false,
							draggable : true,
							resizable : true,
							onResize : function() {
								var iframeBox = $("#train_time_dlg").find(
										"iframe");
								var isChrome = navigator.userAgent
										.toLowerCase().match(/chrome/) != null;
								var WH = $('#train_time_dlg').height();
								var WW = $('#train_time_dlg').width();
								if (isChrome) {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));

								} else {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));
								}
							}
						});
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
		/*
		 * $.ajax({ url : basePath+"/jbtcx/queryPlanLineTrainTimes", cache :
		 * false, type : "POST", dataType : "json", contentType :
		 * "application/json", data :JSON.stringify({ trainId:
		 * currentTrain.obj.planTrainId }), success : function(result) { if
		 * (result != null && result != "undefind" && result.code == "0") { var
		 * message = "车次：" + currentTrain.obj.trainName + "&nbsp;&nbsp;&nbsp;";
		 * 
		 * if (result.data.length == 0) { showWarningDialog("当前车次客运时刻数据不存在。");
		 * return; }
		 * 
		 * $.each(result.data, function(i, n){ var timeRow = new
		 * TrainTimeRow(n); self.times.push(timeRow); if(n.stationFlag !=
		 * 'BTZ'){ self.simpleTimes.push(timeRow); } if(i == 0){ message +=
		 * n.stnName; }else if(i == result.data.length - 1){
		 * self.currentTrainInfoMessage(message + "——" + n.stnName);
		 * if($("#run_plan_train_times").is(":hidden")){
		 * $("#run_plan_train_times").dialog({top:10, draggable: true,
		 * resizable:true, onResize:function() { var simpleTimes_table =
		 * $("#simpleTimes_table"); var allTimes_table = $("#allTimes_table");
		 * var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) !=
		 * null; var WH = $('#run_plan_train_times').height() - 100; var WW =
		 * $('#run_plan_train_times').width(); if (isChrome) {
		 * simpleTimes_table.css({ "height": (WH) + "px"});
		 * simpleTimes_table.css({ "min-height": (WH) + "px"});
		 * simpleTimes_table.attr("width", (WW));
		 * 
		 * allTimes_table.css({ "height": (WH) + "px"}); allTimes_table.css({
		 * "min-height": (WH) + "px"}); allTimes_table.attr("width", (WW));
		 * 
		 * }else{ simpleTimes_table.css({ "height": (WH) + "px"});
		 * simpleTimes_table.css({ "min-height": (WH) + "px"});
		 * simpleTimes_table.attr("width", (WW));
		 * 
		 * allTimes_table.css({ "height": (WH) + "px"}); allTimes_table.css({
		 * "min-height": (WH) + "px"}); allTimes_table.attr("width", (WW)); }
		 * }}); } } }); } else { showErrorDialog("接口调用返回错误，code="+result.code+"
		 * message:"+result.message); } }, error : function() {
		 * showErrorDialog("接口调用失败"); }, complete : function(){
		 * commonJsScreenUnLock(); } });
		 */
		// $("#run_plan_train_times").dialog("open");
	};

	/**
	 * 用于调整时刻表
	 * 
	 * @param currentTrain
	 */
	self.loadTrainAllStns = function(currentTrain) {	
		if(!currentTrain.isModify){
			showWarningDialog("您没有权限执行当前操作！");
			return;
		}
		// if($('#run_plan_train_times_edit_dialog').is(":hidden")){
		$("#run_plan_train_times_edit_dialog").find("iframe").attr(
				"src",
				basePath + "/runPlan/trainRunTimePage?trainNbr="
						+ currentTrain.trainName + "&trainPlanId="
						+ currentTrain.planTrainId + "&runDate="
						+ moment(currentTrain.startDate).format("YYYY-MM-DD"));
		$('#run_plan_train_times_edit_dialog')
				.dialog(
						{
							title : "编辑列车运行时刻",
							autoOpen : true,
							modal : false,
							draggable : true,
							resizable : true,
							onResize : function() {
								var iframeBox = $(
										"#run_plan_train_times_edit_dialog")
										.find("iframe");
								var isChrome = navigator.userAgent
										.toLowerCase().match(/chrome/) != null;
								var WH = $('#run_plan_train_times_edit_dialog')
										.height();
								var WW = $('#run_plan_train_times_edit_dialog')
										.width();
								if (isChrome) {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));

								} else {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));
								}
							}
						});
		// }
	};

	/**
	 * 用于调整时径路
	 * 
	 * @param currentTrain
	 */
	self.loadTrainAllPathway = function(currentTrain) {	
		if(!currentTrain.isModify){
			showWarningDialog("您没有权限执行当前操作！");
			return;
		}
		$("#run_plan_train_pathway_edit_dialog").find("iframe").attr(
				"src",
				basePath + "/runPlan/pathwayPage?trainNbr="
						+ currentTrain.trainName + "&trainPlanId="
						+ currentTrain.planTrainId + "&runDate="
						+ moment(currentTrain.startDate).format("YYYY-MM-DD"));
		$('#run_plan_train_pathway_edit_dialog')
				.dialog(
						{
							title : "编辑列车运行经路",
							autoOpen : true,
							modal : false,
							draggable : true,
							resizable : true,
							onResize : function() {
								var iframeBox = $(
										"#run_plan_train_pathway_edit_dialog")
										.find("iframe");
								var isChrome = navigator.userAgent
										.toLowerCase().match(/chrome/) != null;
								var WH = $(
										'#run_plan_train_pathway_edit_dialog')
										.height();
								var WW = $(
										'#run_plan_train_pathway_edit_dialog')
										.width();
								if (isChrome) {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));

								} else {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));
								}
							}
						});
	};

	/**
	 * 用于命令
	 * 
	 * @param currentTrain
	 */
	self.loadCommand = function(currentTrain) {
		$
				.ajax({
					// 通过cmdtrainid 查询详细信息 展示
					url : basePath + "/runPlanLk/getCmdInfo/" + cmdTrainId,
					cache : false,
					type : "GET",
					dataType : "json",
					contentType : "application/json",
					success : function(result) {

						if (result != null && result != "undefind"
								&& result.code == "0") {
							var message = "车次：";

							if (result.length == 0) {
								showWarningDialog("当前车次客运时刻数据不存在。");
								return;
							}

							self.searchModle().cmdNbrBureau(
									result.data.cmdNbrBureau)
							self.searchModle().cmdItem(result.data.cmdItem)
							self.searchModle().cmdNbrSuperior(
									result.data.cmdNbrSuperior)
							self.searchModle().cmdTime(
									moment(result.data.cmdTime).format(
											"YYYY-MM-DD"))
							// moment(data.arrTime).format("MMDD HH:mm")
							self.searchModle().largeContent(
									result.data.largeContent)
							self.searchModle().releasePeople(
									result.data.releasePeople)

							if ($("#run_plan_train_times").is(":hidden")) {
								$("#run_plan_train_times")
										.dialog(
												{
													top : 10,
													draggable : true,
													resizable : true,
													onResize : function() {
														var isChrome = navigator.userAgent
																.toLowerCase()
																.match(/chrome/) != null;
														var WH = $(
																'#run_plan_train_times')
																.height() - 100;
														var WW = $(
																'#run_plan_train_times')
																.width();

													}
												});
							}

						}
					},

					complete : function() {
						commonJsScreenUnLock();
					}
				});
	};

	/**
	 * 用于停运
	 * 
	 * @param currentTrain
	 */
	self.loadStop = function(currentTrain) {	
		if(!currentTrain.isModify){
			showWarningDialog("您没有权限执行当前操作！");
			return;
		}
		// if($('#run_plan_train_times_edit_dialog').is(":hidden")){
		$("#run_plan_train_times_stop_dialog").find("iframe").attr(
				"src",
				basePath + "/runPlan/trainRunTimeStop?trainNbr="
						+ currentTrain.trainName + "&trainPlanId="
						+ currentTrain.planTrainId + "&runDate="
						+ moment(currentTrain.startDate).format("YYYY-MM-DD"));
		$('#run_plan_train_times_stop_dialog')
				.dialog(
						{
							title : "开行转停运",
							autoOpen : true,
							modal : false,
							draggable : true,
							resizable : true,
							onResize : function() {
								var iframeBox = $(
										"#run_plan_train_times_stop_dialog")
										.find("iframe");
								var isChrome = navigator.userAgent
										.toLowerCase().match(/chrome/) != null;
								var WH = $('#run_plan_train_times_stop_dialog')
										.height();
								var WW = $('#run_plan_train_times_stop_dialog')
										.width();
								if (isChrome) {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));

								} else {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));
								}
							}
						});
		// }
	};

	/**
	 * 用于停运（停运转开行）.
	 * 
	 * @param currentTrain
	 */
	self.loadStopToStart = function(currentTrain) {	
		if(!currentTrain.isModify){
			showWarningDialog("您没有权限执行当前操作！");
			return;
		}
		// if($('#run_plan_train_times_edit_dialog').is(":hidden")){
		$("#run_plan_train_times_stop_dialog").find("iframe").attr(
				"src",
				basePath + "/runPlan/trainRunTimeStop?trainNbr="
						+ currentTrain.trainName + "&trainPlanId="
						+ currentTrain.planTrainId + "&runDate="
						+ moment(currentTrain.startDate).format("YYYY-MM-DD")
						+ "&type=" + 2);
		$('#run_plan_train_times_stop_dialog')
				.dialog(
						{
							title : "停运转开行",
							autoOpen : true,
							modal : false,
							draggable : true,
							resizable : true,
							onResize : function() {
								var iframeBox = $(
										"#run_plan_train_times_stop_dialog")
										.find("iframe");
								var isChrome = navigator.userAgent
										.toLowerCase().match(/chrome/) != null;
								var WH = $('#run_plan_train_times_stop_dialog')
										.height();
								var WW = $('#run_plan_train_times_stop_dialog')
										.width();
								if (isChrome) {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));

								} else {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));
								}
							}
						});
		// }
	};

	/**
	 * 用于启动备用
	 * 
	 * @param currentTrain
	 */
	self.loadBakToUse = function(currentTrain) {	
		if(!currentTrain.isModify){
			showWarningDialog("您没有权限执行当前操作！");
			return;
		}
		// if($('#run_plan_train_times_edit_dialog').is(":hidden")){
		$("#run_plan_train_times_stop_dialog").find("iframe").attr(
				"src",
				basePath + "/runPlan/trainRunBakToUse?trainNbr="
						+ currentTrain.trainName + "&trainPlanId="
						+ currentTrain.planTrainId + "&runDate="
						+ moment(currentTrain.startDate).format("YYYY-MM-DD") + "&type=2");
		$('#run_plan_train_times_stop_dialog')
				.dialog(
						{
							title : "备用转开行",
							autoOpen : true,
							modal : false,
							draggable : true,
							resizable : true,
							onResize : function() {
								var iframeBox = $(
										"#run_plan_train_times_stop_dialog")
										.find("iframe");
								var isChrome = navigator.userAgent
										.toLowerCase().match(/chrome/) != null;
								var WH = $('#run_plan_train_times_stop_dialog')
										.height();
								var WW = $('#run_plan_train_times_stop_dialog')
										.width();
								if (isChrome) {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));

								} else {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));
								}
							}
						});
		// }
	};
	
	/**
	 * 开行转备用
	 * 
	 * @param currentTrain
	 */
	self.loadUseToBak = function(currentTrain) {	
		if(!currentTrain.isModify){
			showWarningDialog("您没有权限执行当前操作！");
			return;
		}
		// if($('#run_plan_train_times_edit_dialog').is(":hidden")){
		$("#run_plan_train_times_stop_dialog").find("iframe").attr(
				"src",
				basePath + "/runPlan/trainRunBakToUse?trainNbr="
						+ currentTrain.trainName + "&trainPlanId="
						+ currentTrain.planTrainId + "&runDate="
						+ moment(currentTrain.startDate).format("YYYY-MM-DD"));
		$('#run_plan_train_times_stop_dialog')
				.dialog(
						{
							title : "开行转备用",
							autoOpen : true,
							modal : false,
							draggable : true,
							resizable : true,
							onResize : function() {
								var iframeBox = $(
										"#run_plan_train_times_stop_dialog")
										.find("iframe");
								var isChrome = navigator.userAgent
										.toLowerCase().match(/chrome/) != null;
								var WH = $('#run_plan_train_times_stop_dialog')
										.height();
								var WW = $('#run_plan_train_times_stop_dialog')
										.width();
								if (isChrome) {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));

								} else {
									iframeBox.css({
										"height" : (WH) + "px"
									});
									iframeBox.css({
										"min-height" : (WH) + "px"
									});
									iframeBox.attr("width", (WW));
								}
							}
						});
		// }
	};
	
	/**
	 * 查看列车乘务信息
	 * 
	 * @param currentTrain
	 */
	self.loadTrainPersonnel = function(currentTrain) {
		// if($('#run_plan_train_crew_dialog').is(":hidden")){
		var _param = "trainNbr=" + currentTrain.trainName + "&runDate="
				+ currentTrain.startDate + "&startStn=" + currentTrain.startStn
				+ "&endStn=" + currentTrain.endStn;
		var _title = "车次：" + currentTrain.trainName
				+ "&nbsp;&nbsp;&nbsp;&nbsp;" + currentTrain.startStn
				+ "&nbsp;→&nbsp;" + currentTrain.endStn
				+ "&nbsp;&nbsp;&nbsp;&nbsp;" + "始发时间：" + currentTrain.startDate
				+ "&nbsp;&nbsp;&nbsp;&nbsp;" + "终到时间：" + currentTrain.endDate;

		$("#run_plan_train_crew_dialog").find("iframe").attr("src",
				basePath + "/runPlan/trainCrewPage?" + _param);
		$('#run_plan_train_crew_dialog').dialog(
				{
					title : "乘务信息【" + _title + "】",
					autoOpen : true,
					modal : false,
					draggable : true,
					resizable : true,
					onResize : function() {
						var iframeBox = $("#run_plan_train_crew_dialog").find(
								"iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(
								/chrome/) != null;
						var WH = $('#run_plan_train_crew_dialog').height();
						var WW = $('#run_plan_train_crew_dialog').width();
						if (isChrome) {
							iframeBox.css({
								"height" : (WH) + "px"
							});
							iframeBox.css({
								"min-height" : (WH) + "px"
							});
							iframeBox.attr("width", (WW));

						} else {
							iframeBox.css({
								"height" : (WH) + "px"
							});
							iframeBox.css({
								"min-height" : (WH) + "px"
							});
							iframeBox.attr("width", (WW));
						}
					}
				});
		// }
	};

	self.setCurrentTrain = function(train) {
		self.currentTrain(train);
	};

	self.setCurrentCross = function(cross) {
		// if(hasActiveRole(cross.tokenVehBureau()) &&
		// self.searchModle().activeFlag() == 0){
		// self.searchModle().activeFlag(1);
		// }else if(!hasActiveRole(cross.tokenVehBureau()) &&
		// self.searchModle().activeFlag() == 1){
		// self.searchModle().activeFlag(0);
		// }
		self.currentCross(cross);
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

	self.defualtCross = {
		"planTrainId" : "",
		"tokenVehBureau" : "",
		"startBureauShortName" : "",
		"trainNbr" : "",
		"cmdShortInfo" : "",
		"startStn" : "",
		"endStn" : ""
	};
	// 当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow(self.defualtCross));

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

	self.get40Date = function() {
		var d = new Date();
		d.setDate(d.getDate() + 35);

		var year = d.getFullYear(); // 获取完整的年份(4位,1970-????)
		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
		var days = d.getDate();
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};

	self.init = function() {

		$("#hisshow").dialog("close");
		$("#modifyRecordDiv").dialog("close");
		$("#train_time_dlg").dialog("close");
		$("#run_plan_train_times").dialog("close");
		$("#run_plan_train_times_edit_dialog").dialog("close");
		$("#run_plan_train_pathway_edit_dialog").dialog("close");
		$("#run_plan_train_times_stop_dialog").dialog("close");
		$("#run_plan_train_crew_dialog").dialog("close");
		$("#run_plan_sendMsg").dialog("close");
		
		$("#dhCross_dialog").dialog("close");
		$("#run_plan_train_Command_edit_dialog").dialog("close");
		$("#runplan_input_startDate").datepicker();
		$("#runplan_input_endDate").datepicker();

		self.runPlanCanvasPage.initPage();

		self.searchModle().startDay(self.currdate());
		self.searchModle().planStartDate(self.currdate());
		self.searchModle().planEndDate(self.get40Date());

		// commonJsScreenLock();
		// 获取路局列表 始发局及担当局
		$.ajax({
			url : basePath + "/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					var bureaus = new Array();
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
						if (null != fistObj) {
							bureaus.unshift(fistObj);
						}
					}
					self.searchModle().loadBureau(bureaus);
					// self.searchModle().loadBureau(result.data);
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

	};

	self.loadCrosses = function() {
		self.checkburu("");
		self.checkburuNo("");
		self.loadPlanTrainLkPage();
	};

	/**
	 * 查询临客开行计划信息
	 * 
	 * @param startIndex
	 * @param endIndex
	 */
	self.loadPlanTrainLkPage = function(startIndex, endIndex) {
		self.runPlanCanvasPage.clearChart();
		self.loadRunPlans("", "");
		commonJsScreenLock();// 锁屏
		var planStartDate = $("#runplan_input_startDate").val();// 开始日期
		var planEndDate = $("#runplan_input_endDate").val();// 截至日期
		if (planStartDate != null && planStartDate != undefined
				&& planEndDate != null && planEndDate != undefined) {
			if (planStartDate > planEndDate) {
				showWarningDialog("开始日期不能大于截止日期!");
				return;
			}

		}

		var checkflagchoice = $("#input_cross_sure_flag").val();

		// console.dir(checkflagchoice)
		var currentBureanFlag = self.searchModle().currentBureanFlag() ? '1'
				: '0';// 本局相关
		var currentBureanFlagFei = self.searchModle().currentBureanFlagFei() ? '1'
				: '0';

		self.planTrainLkRows.remove(function(item) {
			return true;
		});
		$.ajax({
			url : basePath + "/runPlanLk/getPlanTrainLkInfo",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON
					.stringify({
						// 2015-1-9 原先是根据简称查询例如"P",现在改为根据汉字:"京"
						// tokenVehBureau : self.searchModle().bureau(),//担当局
						tokenVehBureau : $('#bureausSel  option:selected')
								.text(),// 担当局
						trainNbr : self.searchModle().filterTrainNbr(),// 车次
						startBureau : $('#sflj  option:selected').text(),// 始发局,
						startDate : (planStartDate != null ? planStartDate
								: self.currdate()).replace(/-/g, ''),// 开始日期
						endDate : (planEndDate != null ? planEndDate : self
								.get40Date()).replace(/-/g, ''),// 截至日期
						isRelationBureau : currentBureanFlag,// 本局相关
						checkflagchoice : checkflagchoice,
						currentBureanFlagFei : currentBureanFlagFei
					}),
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null) {
						$.each(result.data, function(n, crossInfo) {
							self.planTrainLkRows.push(new CrossRow(crossInfo));
						});

						// 是否出现右边17
						var tableH = $("#left_height table").height();
						var boxH = $("#left_height").height();
						if (tableH <= boxH) {
							$(".td_17").removeClass("display");
						} else {
							$(".td_17").addClass("display");
						}

					}
				} else {
					showErrorDialog("获取临客开行计划信息列表失败");
				}
				;
			},
			error : function() {
				showErrorDialog("获取临客开行计划信息列表失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	};
	// 必须定义在load函数之后
	self.crossRows = new PageModle(50, self.loadPlanTrainLkPage);

	self.saveCrossInfo = function() {
		// alert(self.currentCross().tokenVehBureau());
	};

	self.showUploadDlg = function() {

		$("#file_upload_dlg").dialog("open");
	};

	self.isShowRunPlans = ko.observable(true);// 显示开行情况复选框 默认勾选
	self.showRunPlans = function() {
		var WH = $(window).height();
		if (self.isShowRunPlans()) {// 未勾选
			$('#big-learn-more-content').hide();
			$('#left_height').css("height", WH - 229 + "px");
			$('#canvas_parent_div').css("height", WH - 119 + "px");
		} else {// 勾选
			$('#big-learn-more-content').show();
			$('#left_height').css("height", WH - 490 + "px");
			$('#canvas_parent_div').css("height", WH - 380 + "px");
		}

	};

	self.showDialog = function(id, title) {
		$('#' + id).dialog({
			title : title,
			autoOpen : true,
			height : 600,
			width : 800,
			modal : false,
			draggable : true,
			resizable : true
		});
	};

	self.showCrossTrainDlg = function() {
		$("#cross_train_dlg").dialog("open");
	};

	self.showCrossTrainTimeDlg = function() {

		$("#run_plan_train_times").dialog({
			inline : false,
			top : 40
		});
	};

	self.trainNbrChange = function(n, event) {
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};

	self.clearData = function() {
		self.currentCross(new CrossRow(self.defualtCross));
		self.stns.remove(function(item) {
			return true;
		});
		self.planTrainLkRows.remove(function(item) {
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
		self.searchModle().currentBureanFlagFei(false);
		if (hasActiveRole(self.searchModle().bureau())) {
			self.searchModle().activeFlag(1);
			self.clearData();
		} else if (self.searchModle().activeFlag() == 1) {
			self.searchModle().activeFlag(0);
			self.clearData();
		}
	};
	self.checkFei = function() {
		if(null != self.searchModle().bureau()){
			return false;
		}
		return true;
	};

	self.checkCrossInfo = function(row) {

		var planStartDate = $("#runplan_input_startDate").val();
		var planEndDate = $("#runplan_input_endDate").val();
		var allrows = self.planTrainLkRows();
		var array = new Array();
		var trlist = $("#choicall").children("tr")
		var cmdid = "";

		var oneselect = new Array()

		for (var i = 0; i < trlist.length; i++) {
			var tdArr = trlist.eq(i).find("td");
			if (tdArr.eq(0).find("input[name='testcheck']")[0].checked == true) {

				cmdid += allrows[i].cmdTrainId() + ","
				oneselect.push(allrows[i])

			}

		}
		if (cmdid.length < 10) {
			showWarningDialog("没有可审核的");
			return;
		}

		var bol = false;
		if (cmdid.length > 10) {
			var mes = ""
			$.ajax({
				url : basePath + "/runPlan/checkLkRunLine",
				cache : false,
				async : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data : JSON.stringify({
					startTime : (planStartDate != null ? planStartDate : self
							.currdate()).replace(/-/g, ''),
					endTime : (planEndDate != null ? planEndDate : self
							.get40Date()).replace(/-/g, ''),
					planCrossIds : cmdid
				}),
				success : function(result) {

					if (result.code == 3) {
						// showErrorDialog("本局已经审核");

						bol = true
						mes = result.message + "车次本局已经审核"
						// showWarningDialog( result.message+"车次本局已经审核");
					}
					if (result.code == 5) {
						// showErrorDialog("本局已经审核");
						bol = true
						mes = result.message + "车次已审核"
						// showWarningDialog( result.message+"车次已审核");
					}

				},
				error : function() {
					showErrorDialog("审核失败");
				},
				complete : function() {
					commonJsScreenUnLock();
				}
			});
		}
		if (bol) {
			showWarningDialog(mes);
		} else {
			if (cmdid.length > 10) {
				var bol = false;
				var mes = ""
				$.ajax({
					url : basePath + "/runPlan/checkLkRunLine1",
					cache : false,
					async : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data : JSON.stringify({
						startTime : (planStartDate != null ? planStartDate
								: self.currdate()).replace(/-/g, ''),
						endTime : (planEndDate != null ? planEndDate : self
								.get40Date()).replace(/-/g, ''),
						planCrossIds : cmdid,
						highlineFlag : 2
					}),
					success : function(result) {

						if (result.code == 1) {
							// showErrorDialog("本局已经审核");

							bol = true
							mes = result.message + "车次审核成功"
							// showWarningDialog( result.message+"车次本局已经审核");
						} else {
							bol = true
							mes = "审核失败：" + result.message;
						}

					},
					error : function() {
						showErrorDialog("审核失败");
					},
					complete : function() {
						commonJsScreenUnLock();
					}
				});

				if (bol) {
					showSuccessDialog(mes);
				}

			}
		}
		if (oneselect.length == 1) {

			console.dir(oneselect[0])

			var row = oneselect[0]

			self.setCurrentCross(row);
			commonJsScreenLock(2);// 锁屏2次
			self.createCrossMap(row);

			self.trains.remove(function(item) {
				return true;
			});

			// 加载开行情况
			self.loadRunPlans(row.trainNbr(), row.cmdTrainId());
			self.checkplans.remove(function(item) {
				return true;
			});
			$.ajax({

				url : basePath + "/runPlan/getCheckPlancmd",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data : JSON.stringify({
					checkCmdtel : row.cmdTrainId()
				}),
				success : function(result) {
					if (result != null && result != "undefind") {
						// console.dir(result)

						$.each(result.data, function(n, checkplan) {

							self.checkplans.push(new CheckPlan(checkplan))
						});
						var array = self.checkplans()
						var a = "";
						var b = "";
						for (var i = 0; i < array.length; i++) {
							if(i == 0){
								if(array[i].checkStateIf() == 0){
									a += array[i].checkBureau();
								}else if(array[i].checkStateIf() == 1){
									b += array[i].checkBureau();
								}
							}else{
								if(array[i].checkStateIf() == 0){
									if(a != ""){
										a += "、" + array[i].checkBureau();
									}else{
										a += array[i].checkBureau();
									}
								}else if(array[i].checkStateIf() == 1){
									if(b != ""){
										b += "、" + array[i].checkBureau();
									}else{
										b += array[i].checkBureau();
									}
								}
							}
						}

						self.checkburu(a);
						self.checkburuNo(b);

					}
				},

				complete : function() {
					commonJsScreenUnLock();
				}

			});

		}

	}

	/**
	 * 重写审核,上面的那个看不懂啊.
	 */
	self.checkCrossInfo1 = function(row) {
		// 原有的获取数据,以及校验项,复制之前的方法
		var planStartDate = $("#runplan_input_startDate").val();
		var planEndDate = $("#runplan_input_endDate").val();
		var allrows = self.planTrainLkRows();
		var array = new Array();
		var trlist = $("#choicall").children("tr")
		var cmdid = "";
		var oneselect = new Array()
		for (var i = 0; i < trlist.length; i++) {
			var tdArr = trlist.eq(i).find("td");
			if (tdArr.eq(0).find("input[name='testcheck']")[0].checked == true) {
				cmdid += allrows[i].cmdTrainId() + ","
				oneselect.push(allrows[i])
			}
		}
		if ("" == cmdid) {
			showWarningDialog("请选择数据！");
			return;
		}

		var bol = false;
		var mes = ""
		$.ajax({
			url : basePath + "/runPlan/checkLkRunLineNew",
			cache : false,
			async : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON
					.stringify({
						startTime : (planStartDate != null ? planStartDate
								: self.currdate()).replace(/-/g, ''),
						endTime : (planEndDate != null ? planEndDate : self
								.get40Date()).replace(/-/g, ''),
						planCrossIds : cmdid
					}),
			success : function(result) {
				if (result.code == 3) {
					bol = true
					showWarningDialog(result.message + "车次本局已经审核");
				} else if (result.code == 5) {
					bol = true
					showWarningDialog(result.message + "车次已审核");
				} else if (result.code == 1) {
					showSuccessDialog(result.message + "车次审核成功");
				} else {
					bol = true
					showWarningDialog("审核失败：" + result.message);
				}
			},
			error : function() {
				showErrorDialog("审核失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});

		if (!bol) {

			$.each(oneselect, function(i, n) {
				n
						.checkedBureau((n.checkedBureau() != null ? (n
								.checkedBureau() + ",") : "")
								+ currentBureauShortName);
				// 将选中数据的,图标状态改掉.
				// n.checkFlag(1);
			});

			if (oneselect.length == 1) {
				console.dir(oneselect[0])
				var row = oneselect[0]
				// 更新选中数据
				self.setCurrentCross(row);
				commonJsScreenLock(2);// 锁屏2次
				self.createCrossMap(row);
				self.trains.remove(function(item) {
					return true;
				});

				// 加载开行情况
				self.loadRunPlans(row.trainNbr(), row.cmdTrainId());
				self.checkplans.remove(function(item) {
					return true;
				});
				$.ajax({
					url : basePath + "/runPlan/getCheckPlancmd",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data : JSON.stringify({
						checkCmdtel : row.cmdTrainId()
					}),
					success : function(result) {
						if (result != null && result != "undefind") {
							$.each(result.data, function(n, checkplan) {
								self.checkplans.push(new CheckPlan(checkplan))
							});
							var array = self.checkplans()
							var a = "";
							var b = "";
							for (var i = 0; i < array.length; i++) {
								if(i == 0){
									if(array[i].checkStateIf() == 0){
										a += array[i].checkBureau();
									}else if(array[i].checkStateIf() == 1){
										b += array[i].checkBureau();
									}
								}else{
									if(array[i].checkStateIf() == 0){
										if(a != ""){
											a += "、" + array[i].checkBureau();
										}else{
											a += array[i].checkBureau();
										}
									}else if(array[i].checkStateIf() == 1){
										if(b != ""){
											b += "、" + array[i].checkBureau();
										}else{
											b += array[i].checkBureau();
										}
									}
								}
							}
							self.checkburu(a);
							self.checkburuNo(b);
						}
					},
					complete : function() {
						commonJsScreenUnLock();
					}

				});
			}
		}
	}

	self.deleteCrosses = function() {
		// 审核的代码.
		var allrows = self.planTrainLkRows();
		var array = new Array();
		var trlist = $("#choicall").children("tr")
		var cmdid = "";
		var oneselect = new Array()
		var delCrosses = [];
		for (var i = 0; i < trlist.length; i++) {
			var tdArr = trlist.eq(i).find("td");
			if (tdArr.eq(0).find("input[name='testcheck']")[0].checked == true) {
				if(hasActiveRole(allrows[i].tokenVehBureauLjpym())){
					cmdid += allrows[i].cmdTrainId() + ",";
					delCrosses.push(allrows[i]);
				}else{
					showWarningDialog("你没有权限删除:" + allrows[i].trainNbr());
					return;
				}
			}
		}
		if ("" == cmdid) {
			showWarningDialog("请选择数据！");
			return;
		}
		
		showConfirmDiv("提示", "当前操作会同时删除对应的'1列车1条线'数据！", function(r) {
			if (r) {
				commonJsScreenLock();
				$.ajax({
					url : basePath + "/runPlanLk/deleteRunPlanLk",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data : JSON.stringify({
						cmdIds : cmdid
					}),
					success : function(result) {
						if (result.code == 0) {
							$.each(delCrosses, function(i, n) {
								self.planTrainLkRows.remove(n);
							});
							showSuccessDialog("删除车底交路成功");
						} else {
							showErrorDialog("删除车底交路失败");
						}
						;
					},
					error : function() {
						showErrorDialog("删除车底交路失败");
					},
					complete : function() {
						commonJsScreenUnLock();
					}
				});
			}
			;
		});
	}

	self.dhCrosses = function() {
		var crossIds = "";
//		var crosses = self.planTrainLkRows();
		var delCrosses = [];
		var crossName = "";
//		for (var i = 0; i < crosses.length; i++) {
//			if (crosses[i].selected() == 1) {
//						crossIds += (crossIds == "" ? "" : ",");
//						crossIds += crosses[i].cmdTrainId();
//						crossName += crosses[i].crossName();
//						delCrosses.push(crosses[i]);
//			}
//		}
//				if (crossIds == "") {
//				showWarningDialog("没有可不通过的数据！");
//				return;
//			}else if(delCrosses.length > 1){
//				showWarningDialog("只能选择一条数据！");
//				return;
//			}
				

				var allrows = self.planTrainLkRows();
				var array = new Array();
				var trlist = $("#choicall").children("tr")
				for (var i = 0; i < trlist.length; i++) {
					var tdArr = trlist.eq(i).find("td");
					if (tdArr.eq(0).find("input[name='testcheck']")[0].checked == true) {
						crossIds += allrows[i].cmdTrainId() + ","
						delCrosses.push(allrows[i])
						crossName += allrows[i].trainNbr();
					}
				}
				if ("" == crossIds) {
					showWarningDialog("请选择数据！");
					return;
				}else if(delCrosses.length > 1){
					showWarningDialog("只能选择一条数据！");
					return;
				}
			
			$("#dhCross_dialog").find("iframe").attr("src", basePath+"/runPlan/addDhPlanCrosses?startTime="+($("#runplan_input_startDate").val() != null ? $("#runplan_input_startDate").val() : self.currdate()).replace(/-/g, '') + "&endTime="+($("#runplan_input_endDate").val() != null ? $("#runplan_input_endDate").val() : self.get40Date()).replace(/-/g, '') + "&planCrossIds="+crossIds+"&crossName="+crossName+"&type="+2);
			$('#dhCross_dialog').dialog({title: "交路不通过", autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#dhCross_dialog").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#dhCross_dialog').height();
					var WW = $('#dhCross_dialog').width();
	                if (isChrome) {
	                	iframeBox.css({ "height": (WH) + "px"});
	                	iframeBox.css({ "min-height": (WH) + "px"});
	                	iframeBox.attr("width", (WW));

	                }else{
	                	iframeBox.css({ "height": (WH)  + "px"});
	                	iframeBox.css({ "min-height": (WH) + "px"});
	                	iframeBox.attr("width", (WW));
	                }
				}});
			
	};

	self.sendMsg = function() {
		var cmdTrainIds = "";
		var delCrosses = [];
		var trainNbrs = "";
				var allrows = self.planTrainLkRows();
				var array = new Array();
				var trlist = $("#choicall").children("tr")
				for (var i = 0; i < trlist.length; i++) {
					var tdArr = trlist.eq(i).find("td");
					if (tdArr.eq(0).find("input[name='testcheck']")[0].checked == true) {
						cmdTrainIds += allrows[i].cmdTrainId() + ","
						delCrosses.push(allrows[i])
						trainNbrs += allrows[i].trainNbr() + ",";
					}
				}
				if ("" == cmdTrainIds) {
					showWarningDialog("请选择数据！");
					return;
				}
			
				$("#run_plan_sendMsg").find("iframe").attr("src", basePath+"/runPlanLk/runPlanLkSendMsg?cmdTrainIds="+cmdTrainIds+"&trainNbrs="+trainNbrs+"&type=0");
				$('#run_plan_sendMsg').dialog({title: "临客-发消息", autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#run_plan_sendMsg").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#run_plan_sendMsg').height();
						var WW = $('#run_plan_sendMsg').width();
		                if (isChrome) {
		                	iframeBox.css({ "height": (WH) + "px"});
		                	iframeBox.css({ "min-height": (WH) + "px"});
		                	iframeBox.attr("width", (WW));

		                }else{
		                	iframeBox.css({ "height": (WH)  + "px"});
		                	iframeBox.css({ "min-height": (WH) + "px"});
		                	iframeBox.attr("width", (WW));
		                }
					}});
			
	};

	self.testallcheck = function() {

		if ($("#checkall")[0].checked == true) {
			$("#btn_cross_sure").attr("class", "btn btn-success")
			$("#btn_cross_delete").attr("class", "btn btn-success")
			var trlist = $("#choicall").children("tr")

			for (var i = 0; i < trlist.length; i++) {
				var tdArr = trlist.eq(i).find("td");
				tdArr.eq(0).find("input[name='testcheck']")[0].checked = true
			}

		} else {
			$("#btn_cross_sure").attr("class", "btn btn-success disabled")
			$("#btn_cross_delete").attr("class", "btn btn-success disabled")
			var trlist = $("#choicall").children("tr")

			for (var i = 0; i < trlist.length; i++) {
				var tdArr = trlist.eq(i).find("td");
				tdArr.eq(0).find("input[name='testcheck']")[0].checked = false

			}

			// find("input[name='testcheck']"))

		}

		var notexistburu = new Array();
		var arrayall = self.planTrainLkRows();

		var trlist = $("#choicall").children("tr")

		if (arrayall.length > 0) {
			for (var i = 0; i < arrayall.length; i++) {
				if (arrayall[i].relevantBureau()
						.indexOf(currentBureauShortName) < 0) {
					// showWarningDialog("相关局不包含本局不能审核");
					// event.target.checked=false
					// return ""

					notexistburu.push(i)

					var tdArr = trlist.eq(i).find("td");
					tdArr.eq(0).find("input[name='testcheck']")[0].checked = false

				}
			}
		}

	}

	self.testonecheck = function(row, event) {
		var allburu = row.relevantBureau();
		
		if (allburu.indexOf(currentBureauShortName) < 0) {
			// showWarningDialog("相关局不包含本局不能审核");
			event.target.checked = false;
			return "";
		}
		if (event.target.checked == true) {
			$("#btn_cross_sure").attr("class", "btn btn-success");
			$("#btn_cross_no").attr("class", "btn btn-success");
			$("#btn_cross_sendMsg").attr("class", "btn btn-success");
			if(currentBureauShortName == row.tokenVehBureau()){
				$("#btn_cross_delete").attr("class", "btn btn-success");
			}

		} else {
			$("#btn_cross_sure").attr("class", "btn btn-success disabled")
			$("#btn_cross_no").attr("class", "btn btn-success disabled")
			$("#btn_cross_delete").attr("class", "btn btn-success disabled");
			$("#btn_cross_sendMsg").attr("class", "btn btn-success");
		}

	}

	self.createCrossMap = function(row) {

		self.runPlanCanvasPage.clearChart(); // 清除画布
		var planStartDate = $("#runplan_input_startDate").val();
		var planEndDate = $("#runplan_input_endDate").val();

		$.ajax({
			url : basePath + "/runPlanLk/getTrainLkInfoForPlanTrainId",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON
					.stringify({
						planTrainId : row.planTrainId(),
						trainNbr : row.trainNbr(),
						startDate : (planStartDate != null ? planStartDate
								: self.currdate()),// 开始日期
						endDate : (planEndDate != null ? planEndDate : self
								.get40Date())
					// 截至日期
					}),
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null) {
						canvasData = {
							grid : result.data.gridData,// $.parseJSON(result.data.gridData),
							jlData : result.data.myJlData
						// $.parseJSON(result.data.myJlData)
						};
						self.runPlanCanvasPage.drawChart({
							stationTypeArray : self.searchModle().drawFlags()
						});
					}

				} else {
					showErrorDialog("获取车底绘图数据失败");
				}
			},
			error : function() {
				showErrorDialog("获取车底绘图数据失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	};
	self.showTrains = function(row, event) {
		cmdTrainId = null;
		cmdTrainId = row.cmdTrainId();

		$(event.target).bind("contextmenu", function(e) {
			return false;
		});
		/*
		 * if(3==event.which){
		 * 
		 * $.ajax({ //通过cmdtrainid 查询详细信息 展示 url :
		 * basePath+"/runPlanLk/getCmdInfo/"+row.cmdTrainId(), cache : false,
		 * type : "GET", dataType : "json", contentType : "application/json",
		 * success : function(result) {
		 * 
		 * 
		 * if (result != null && result != "undefind" && result.code == "0") {
		 * var message = "车次：";
		 * 
		 * if (result.length == 0) { showWarningDialog("当前车次客运时刻数据不存在。");
		 * return; }
		 * 
		 * self.searchModle().cmdNbrBureau(result.data.cmdNbrBureau)
		 * self.searchModle().cmdItem(result.data.cmdItem)
		 * self.searchModle().cmdNbrSuperior(result.data.cmdNbrSuperior)
		 * self.searchModle().cmdTime(moment(result.data.cmdTime).format("YYYY-MM-DD"))
		 * //moment(data.arrTime).format("MMDD HH:mm")
		 * self.searchModle().largeContent(result.data.largeContent)
		 * self.searchModle().releasePeople(result.data.releasePeople)
		 * 
		 * 
		 * if($("#run_plan_train_times").is(":hidden")){
		 * $("#run_plan_train_times").dialog({top:10, draggable: true,
		 * resizable:true, onResize:function() { var isChrome =
		 * navigator.userAgent.toLowerCase().match(/chrome/) != null; var WH =
		 * $('#run_plan_train_times').height() - 100; var WW =
		 * $('#run_plan_train_times').width();
		 * 
		 * }}); }
		 * 
		 *  } },
		 * 
		 * complete : function(){ commonJsScreenUnLock(); } });
		 * 
		 * 
		 * 
		 * 
		 *  }
		 */

		if (1 == event.which) {
			self.setCurrentCross(row);
			commonJsScreenLock(2);// 锁屏2次
			self.createCrossMap(row);

			self.trains.remove(function(item) {
				return true;
			});

			// 加载开行情况
			self.loadRunPlans(row.trainNbr(), row.cmdTrainId());
			self.checkplans.remove(function(item) {
				return true;
			});
			$.ajax({

				url : basePath + "/runPlan/getCheckPlancmd",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data : JSON.stringify({
					checkCmdtel : row.cmdTrainId()
				}),
				success : function(result) {
					if (result != null && result != "undefind") {
						// console.dir(result)

						$.each(result.data, function(n, checkplan) {

							self.checkplans.push(new CheckPlan(checkplan))
						});
						var array = self.checkplans()
						var a = "";
						var b = "";
						for (var i = 0; i < array.length; i++) {
							if(i == 0){
								if(array[i].checkStateIf() == 0){
									a += array[i].checkBureau();
								}else if(array[i].checkStateIf() == 1){
									b += array[i].checkBureau();
								}
							}else{
								if(array[i].checkStateIf() == 0){
									if(a != ""){
										a += "、" + array[i].checkBureau();
									}else{
										a += array[i].checkBureau();
									}
								}else if(array[i].checkStateIf() == 1){
									if(b != ""){
										b += "、" + array[i].checkBureau();
									}else{
										b += array[i].checkBureau();
									}
								}
							}
						}

						self.checkburu(a);
						self.checkburuNo(b);

					}
				},

				complete : function() {
					commonJsScreenUnLock();
				}

			});
		}

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
		self.runPlanCanvasPage.drawChart({
			stationTypeArray : self.searchModle().drawFlags()
		});
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

	self.cmdNbrBureau = ko.observable();
	self.cmdItem = ko.observable();
	self.cmdNbrSuperior = ko.observable();
	self.cmdTime = ko.observable();
	self.largeContent = ko.observable();
	self.releasePeople = ko.observable();

	self.activeFlag = ko.observable(0);

	self.checkActiveFlag = ko.observable(0);

	self.activeCurrentCrossFlag = ko.observable(0);

	self.drawFlags = ko.observableArray([ '0' ]);

	self.planStartDate = ko.observable();

	self.currentBureanFlag = ko.observable(1);

	self.currentBureanFlagFei = ko.observable(false);

	self.planEndDate = ko.observable();

	self.bureaus = ko.observableArray();

	self.startBureaus = ko.observableArray();

	self.charts = ko.observableArray();

	self.highlingFlags = highlingFlags;

	self.unitCreateFlags = unitCreateFlags;

	self.filterCheckFlag = ko.observable(0);

	self.filterUnitCreateFlag = ko.observable(0);

	self.checkFlags = checkFlags;
	self.checkFlagsju = checkFlagsju;
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

function ModifyRecordS(data) {
	var self = this;
	self.modifyRecordS = ko.observableArray();
	$.each(data, function(n, modifyplans) {
		self.modifyRecordS.push(new ModifyRecord(modifyplans))
	})
	self.modifyRecordSLength = ko.observable(self.modifyRecordS().length);
}

function ModifyRecord(data) {
	var self = this;
	// self.planCheckId =ko.observable(data.planCheckId ) ;

	self.planModifyId = ko.observable(data.planModifyId);
	self.planCrossId = ko.observable(data.planCrossId);
	self.planTrainId = ko.observable(data.planTrainId);
	self.crossName = ko.observable(data.crossName);
	self.runDate = ko.observable(data.runDate);
	self.trainNbr = ko.observable(data.trainNbr);

	self.modifyReason = ko.observable(data.modifyReason);
	self.startDate = ko.observable(moment(filterValue(data.startDate)).format(
			"YYYYMMDD"));
	self.endDate = ko.observable(moment(filterValue(data.endDate)).format(
			"YYYYMMDD"));

	self.selectedDate = ko.observable(data.selectedDate);
	self.modifyContent = ko.observable(data.modifyContent);
	self.modifyTime = ko.observable(moment(filterValue(data.modifyTime))
			.format("MMDD HH:mm"));// MMDD HH:mm
	self.modifyPeople = ko.observable(data.modifyPeople);
	self.modifyPeopleOrg = ko.observable(data.modifyPeopleOrg);
	self.modifyPeopleBureau = ko.observable(data.modifyPeopleBureau);

	self.modifyType = ko.computed(function() {
		// private String modifyType;//0:调整时刻；1：调整经路；2：停运；3：启动备用
		if (data.modifyType == '0') {
			return "时刻调整"
		} else if (data.modifyType == '1') {
			return "经路调整"
		} else if (data.modifyType == '2') {
			return "开行转停运"
		} else if (data.modifyType == '3') {
			return "备用转开行"
		} else if (data.modifyType == '4') {
			return "停运转开行"
		} else if (data.modifyType == '5') {
			return "开行转备用"
		} else {
			return "其他";
		}

	});
	self.rule = ko.computed(function() {
		if (data.rule == '1') {
			return "每日"
		}
		if (data.rule == '2') {
			return "隔日"
		}
		if (data.rule == '3') {
			return "择日"
		}
	});
}

function CrossRow(data) {

	var self = this;

	if (data == null) {
		return;
	}

	self.importantFlag = ko.observable(data.importantFlag);// id

	self.importantFlagStr = ko.computed(function() {
		// 选线状态0：未选择 1：已选择
		if (self.importantFlag() == "1") {
			return "<span >是</span>";// "已";
		} else {
			return "";// "未";
		}
	});

	// "passBureau: "上济"releasePeople:
	self.cmdTrainId = ko.observable(data.cmdTrainId);
	self.planTrainId = ko.observable(data.planTrainId);// id
	self.tokenVehBureau = ko.observable(data.tokenVehBureau);// 担当局
	self.startBureauShortName = ko.observable(data.startBureauShortName); // 始发局
	self.trainNbr = ko.observable(data.trainNbr); // 车次
	self.cmdShortInfo = ko.observable(data.cmdShortInfo); // 来源
															// 命令简要信息(发令日期+命令号+命令子项号)
	self.startStn = ko.observable(data.startStn);// 车辆始发站
	self.endStn = ko.observable(data.endStn); // 终到站
	self.cmdNbrBureau = ko.observable(data.cmdNbrBureau);
	self.cmdItem = ko.observable(data.cmdItem);
	self.cmdNbrSuperior = ko.observable(data.cmdNbrSuperior);
	self.cmdTime = ko.observable(data.cmdTime);
	self.largeContent = ko.observable(data.largeContent);
	self.releasePeople = ko.observable(data.releasePeople);
	self.createType = ko.observable(data.createType);
	// private String cmdNbrBureau;
	// //项号
	// private Integer cmdItem;
	// //总公司命令号
	// private String cmdNbrSuperior;
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
		return result;
	});

	// 担当局：字母
	self.tokenVehBureauLjpym = ko.computed(function() {
		var result = "";
		if (data.tokenVehBureau != null && data.tokenVehBureau != "null") {
			var bs = data.tokenVehBureau.split("、");
			for (var j = 0; j < bs.length; j++) {
				for (var i = 0; i < gloabBureaus.length; i++) {
					if (bs[j] == gloabBureaus[i].shortName) {
						result = gloabBureaus[i].code;
						break;
					}
					;
				}
				;
			}
			;
		}
		return result;
	});
	
	// 列表记录行点击事件 显示相关局
	self.relevantBureauShowValue = ko.computed(function() {
		var result = "";
		if (data.passBureau != null && data.passBureau != "null"
				&& data.passBureau != "") {
			for (var i = 0; i < data.passBureau.length; i++) {
				// console.log("---"+ data.passBureau.substring(i, i+1));
				result = result + data.passBureau.substring(i, i + 1);
				if (i != data.passBureau.length - 1) {
					result = result + "、";
				}
			}
		}
		return result;
	});

	self.relevantBureau = ko.observable(data.passBureau);// 相关局
	self.checkedBureau = ko.observable(data.checkBureau);
	self.checkFlag = ko.observable(data.checkType);
	self.checkCss = ko
			.computed(function() {
				if (self.checkFlag() != 2 && data.passBureau != null
						&& data.passBureau.indexOf(currentBureauShortName) > -1) {
					// 和当前局相关并且被当前局审核过了的
					if (self.checkedBureau() != null) {
						if (self.checkFlag() == 3) {
							return "fa fa-check-square-o red";
						}
						var checkedBureau = self.checkedBureau().split(",");
						var temp = "";
						for (var i = 0; i < self.relevantBureau().length; i++) {
							if (self.checkedBureau().indexOf(
									self.relevantBureau().substring(i, i + 1)) > -1) {
								temp += self.relevantBureau().substring(i,
										i + 1);
							}
							;
						}
						if (temp == self.relevantBureau()) {
							self.checkFlag(2);
							return "fa fa-check-square-o green";
						} else if (self.checkedBureau().indexOf(
								currentBureauShortName) > -1) {
							return "fa fa-pencil-square-o green";
						} else {
							return "fa fa-pencil-square-o orange";
						}
						;
					} else {
						return "fa fa-pencil-square-o orange";
					}
					;
				} else if (self.checkFlag() != 2
						&& (data.passBureau == null || (data.passBureau != null && data.passBureau
								.indexOf(currentBureauShortName) < 0))) {
					// 和当前局无关 未被完全审核的
					return "fa fa-pencil-square-o gray";
				} else if (self.checkFlag() == 2) {
					if (data.passBureau == null
							|| (data.passBureau != null && data.passBureau
									.indexOf(currentBureauShortName) < 0)) {
						return "fa fa-check-square-o gray";
					}
					return "fa fa-check-square-o green";
				}
				;
			});
};

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
	self.trainNbr = data.trainNbr;
	self.runPlans = ko.observableArray();

	if (data.runPlans != null) {
		$.each(data.runPlans, function(i, n) {
			self.runPlans.push(new RunPlanRow(n));
		});
	}
}

function RunPlanRow(data) {
	var self = this;
	var str = '';

	self.planTrainId = data.planTrainId;
	if (data.isModified == 0 || data.isModified == "0") {
		str = "<span class='label label-success'>开</span>";
	} else {
		self.planTrainId = data.planTrainId;
		str = '<span class="label label-success"><i class="fa fa-book triangle"></i><a href="javascript:void(0)" onclick="showOneModifyRecord(\''
				+ self.planTrainId + '\')" class="span_text">开</a></span>';
	}

	self.color = ko.observable("gray");

	self.runFlagStr = ko.computed(function() {
		switch (data.runFlag) {
		case '9':
			self.color("gray");
			return "<span class='label label-danger'>停</span>";// "停";
			break;
		case '1':
			self.color("green");
			return str;// "开";
			break;
		case '2':
			self.color("blue");
			return "<span class='label label-info'>备</span>";// "备";
			break;
		default:
			return '';
			break;
		}
	});
}

function TrainTimeRow(data) {
	var self = this;
	self.index = data.childIndex + 1;
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = filterValue(data.arrTime != null ? data.arrTime.replace(
			/-/g, "").substring(4).substring(0, 10) : "");
	self.targetTime = filterValue(data.dptTime != null ? data.dptTime.replace(
			/-/g, "").substring(4).substring(0, 10) : "");
	// self.sourceTime = data.stationFlag=='SFZ'?'--':filterValue(data.arrTime);
	// self.targetTime = data.stationFlag=='ZDZ'?'--':filterValue(data.dptTime);

	self.stepStr = GetDateDiff(data);
	self.trackName = filterValue(data.trackName);
	self.arrRunDays = data.arrRunDays;
	self.runDays = data.runDays;
	self.stationFlag = data.stationFlag;

};
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

function CheckPlan(data) {
	var self = this;
	// self.planCheckId =ko.observable(data.planCheckId ) ;
	self.planCrossId = ko.observable(data.planCrossId);
	self.startDate = ko.observable(data.startDate);
	self.endDate = ko.observable(data.endDate);
	self.checkPeople = ko.observable(data.checkPeople);
	self.checkTime = ko.observable(data.checkTime);
	self.checkDept = ko.observable(data.checkDept);
	self.checkBureau = ko.observable(data.checkBureau);
	self.checkHisFlag = ko.observable(data.checkHisFlag);
	self.checkStateIf = ko.observable(data.checkState);
	self.checkState = ko.computed(function() {
		if (data.checkState == 0) {
			return "通过"
		} else {
			return "不通过";
		}

	});
	self.checkPeopleTel = ko.observable(data.checkPeopleTel);
	self.checkRejectReason = ko.observable(data.checkRejectReason);

	self.hisornow = ko.computed(function() {
		if (data.checkHisFlag == 0) {
			return "当前"
		} else {
			return "历史";
		}

	});
	self.checkCmdtel = ko.observable(data.checkCmdtel);

}
function showOneModifyRecord(planTrainId) {
	cross.showOneModifyRecord(planTrainId);
	return false;
}
function refreshParent() {
	cross.loadCrosses();
}