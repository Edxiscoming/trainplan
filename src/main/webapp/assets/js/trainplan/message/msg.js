var msg = null;
$(function() {
	heightAuto();

	msg = new MsgModel();
	ko.applyBindings(msg);
	msg.init();
});

function heightAuto() {
	var WH = $(window).height();
	$("#div_hightline_planDayDetail").css("height", WH - 360 + "px");
	$("#div_crossDetailInfo_trainStnTable").css("height", 270 + "px");
	$("#canvas_parent_div").css("height", 203 + "px");
	$("#left_height").css("height", WH - 150 - 72 + "px");
	$("#left_height2").css("height", WH - 150 - 72 + "px");
};

function MsgModel() {
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
	self.currdate1 = function() {
		var d = new Date();
		var year = d.getFullYear(); // 获取完整的年份(4位,1970-????)
		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
		var days = d.getDate();
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};

//	self.get40Date = function() {
//		var d = new Date();
//		d.setDate(d.getDate() + 1);// + 30
//
//		var year = d.getFullYear(); // 获取完整的年份(4位,1970-????)
//		var month = d.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
//		var days = d.getDate();
//		month = ("" + month).length == 1 ? "0" + month : month;
//		days = ("" + days).length == 1 ? "0" + days : days;
//		return year + "-" + month + "-" + days;
//	};

	self.sendPeople = ko.observable();
	self.startDate = ko.observable(self.currdate());
//	self.endDate = ko.observable(self.get40Date());
	self.endDate = ko.observable(self.currdate());
	self.bureaus = ko.observableArray();
	self.kw = ko.observable();
	self.msgType = ko.observableArray();
	self.receiveMsgStatus = ko.observableArray();
	self.sendMsgStatus = ko.observableArray();

	// 列表
	self.receiveMsg = ko.observableArray();
	self.sendMsg = ko.observableArray();
	self.isSelectAll = ko.observable(false);

	/**
	 * 命令列表全选复选框change事件
	 */
	self.checkBoxSelectAllChange = function() {
//		 console.log("~~~~~~~~ 全选标识 self.isSelectAll="+self.isSelectAll());
		if (!self.isSelectAll()) {// 全选 将runPlanLkCMDRows中isSelect属性设置为1
//			console.log("全选");
			$.each(self.receiveMsg(), function(i, row) {
				row.selected(1);
			});
		} else {// 全不选 将runPlanLkCMDRows中isSelect属性设置为0
//			console.log("全不选");
			$.each(self.receiveMsg(), function(i, row) {
				row.selected(0);
			});
		}
	};

	self.selectCross = function(row) {
		if (row.selected() == 0) {
			row.selected(1);
		} else {
			row.selected(0)
		}
	};

	self.cleanPageData = function() {
		self.sendPeople("");
		self.kw("");
		self.startDate(self.currdate());
		self.endDate(self.currdate());
		// self.receiveMsg.remove(function(item) {
		// return true;
		// });
		// self.sendMsg.remove(function(item) {
		// return true;
		// });
	};

	self.init = function() {
		$("#receive_start_date").datepicker();
		$("#receive_end_date").datepicker();
		$("#send_start_date").datepicker();
		$("#send_end_date").datepicker();

		var _height = $(window).height() - $('#div_searchForm').height() - 500
				- 150;
		_height = _height < 80 ? 200 : _height;

		// 接收消息状态
		self.receiveMsgStatus.push({
			"name" : "",
			"id" : null
		}, {
			"name" : "未签收",
			"id" : 0
		}, {
			"name" : "已签收",
			"id" : 1
		}/*
			 * , { "name" : "已删除", "id" : 2 }
			 */);
		// 发送消息状态
		self.sendMsgStatus.push({
			"name" : "",
			"id" : null
		}, {
			"name" : "全部未签收",
			"id" : 0
		}, {
			"name" : "部分签收",
			"id" : 1
		}, {
			"name" : "全部签收",
			"id" : 2
		});
		// 消息类型
		commonJsScreenLock(1);
		$.ajax({
			url : basePath + "/msg/getMsgType",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null && result.data != undefined) {
						var ts = new Array();
						$.each(result.data, function(n, types) {
							ts.push(types);
						});
						ts.unshift('');
						$.each(ts, function(n, types) {
							self.msgType.push({
								"name" : types.name,
								"code" : types.code
							});
						});
					// 是否出现右边17
					var tableH = $("#left_height table").height();
					var tableH2 = $("#left_height2 table").height();
					var boxH = $("#left_height").height();
					var boxH2 = $("#left_height2").height();
					if (tableH <= boxH) {
						$("#left_height .td_17").removeClass("display");
					} else if(tableH > boxH) {
						$("#left_height .td_17").addClass("display");
					} else if(tableH2 <= boxH2){
						$("#left_height2 .td_17").removeClass("display");
					} else if(tableH2 > boxH2){
						$("#left_height2 .td_17").addClass("display");
					}
					}

				} else {
					showErrorDialog("查询不到消息类型");
				}
			},
			error : function() {
				showErrorDialog("获取消息类型失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});

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
					var curUserBur = bureauShortName;
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
							bureaus.unshift("",fistObj);
						}
						$.each(bureaus, function(n, bureau) {
							self.bureaus.push({
								"name" : bureau.ljjc,
								"id" : bureau.ljpym
							});
						});
						// 是否出现右边17
						var tableH = $("#left_height table").height();
						var tableH2 = $("#left_height2 table").height();
						var boxH = $("#left_height").height();
						var boxH2 = $("#left_height2").height();
						if (tableH <= boxH) {
							$("#left_height .td_17").removeClass("display");
						} else if(tableH > boxH) {
							$("#left_height .td_17").addClass("display");
						} else if(tableH2 <= boxH2){
							$("#left_height2 .td_17").removeClass("display");
						} else if(tableH2 > boxH2){
							$("#left_height2 .td_17").addClass("display");
						}
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

	self.loadReceiveMsg = function() {
		var receiveStartDate = $("#receive_start_date").val();
		var receiveEndDate = $("#receive_end_date").val();
		var bureauCode = $("#receive_msg_bureaus").val();
		var typeCode = $("#receive_msg_types").val();
		var statusCode = $("#receive_msg_status").val();
		var sendPeople = $("#sendPeople").val();
		var kw = $("#kw").val();

		if ("" == receiveStartDate || "" == receiveEndDate) {
			showWarningDialog("请选择日期！");
			return;
		}
//		if (receiveStartDate >= receiveEndDate) {
//			showWarningDialog("截至日期必须大于开始日期！");
//			return;
//		}
//		if (null == bureauCode || "" == bureauCode) {
//			showWarningDialog("发消息局不能为空！");
//			return;
//		}

		self.receiveMsg.remove(function(item) {
			return true;
		});

		commonJsScreenLock();
		$.ajax({
			url : basePath + "/msg/getReceiveMsg",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				receiveStartDate : receiveStartDate,
				receiveEndDate : receiveEndDate,
				bureauCode : bureauCode != "" ? bureauCode : null,
				typeCode : typeCode != "" ? typeCode : null,
				statusCode : statusCode != "" ? statusCode : null,
				sendPeople : sendPeople != "" ? sendPeople : null,
				kw : kw != "" ? kw : null
			}),
			success : function(result) {
				if (result != null && result.code == "0") {
					if (result.data != null) {
						$.each(result.data, function(i, n) {
							self.receiveMsg.push(new ReceiveMsg(n));
						});
					}
				} else {
					showErrorDialog("查询失败" + result.message);
				}
			},
			error : function() {
				showErrorDialog("查询失败" + XMLHttpRequest.status);
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	}

	self.loadSendMsg = function() {
		// 不变的条件
		var sendStartDate = $("#send_start_date").val();
		var sendEndDate = $("#send_end_date").val();
		var bureauCode = $("#send_msg_bureaus").val();
		var typeCode = $("#send_msg_types").val();
		var receivePeople = $("#receive_people").val();
		var kw = $("#send_kw").val();
		// 
		var statusCode = $("#send_msg_status").val();

		if ("" == sendStartDate || "" == sendEndDate) {
			showWarningDialog("请选择日期！");
			return;
		}
//		if (sendStartDate >= sendEndDate) {
//			showWarningDialog("截至日期必须大于开始日期！");
//			return;
//		}
//		if (null == bureauCode || "" == bureauCode) {
//			showWarningDialog("发消息局不能为空！");
//			return;
//		}

		self.sendMsg.remove(function(item) {
			return true;
		});

		commonJsScreenLock();
		$.ajax({
			url : basePath + "/msg/getSendMsg",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				sendStartDate : sendStartDate,
				sendEndDate : sendEndDate,
				bureauCode : bureauCode != "" ? bureauCode : null,
				typeCode : typeCode != "" ? typeCode : null,
				statusCode : statusCode != "" ? statusCode : null,
				receivePeople : receivePeople != "" ? receivePeople : null,
				kw : kw != "" ? kw : null
			}),
			success : function(result) {
				if (result != null && result.code == "0") {
					if (result.data != null) {
						$.each(result.data, function(i, n) {
							self.sendMsg.push(new SendMsg(n,(i+1)));
						});
					}
				} else {
					showErrorDialog("查询失败" + result.message);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showErrorDialog("查询失败" + XMLHttpRequest.status);
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	}

	self.receiveOk = function() {
		var ids = "";
		var checkedReceiveMsgs = [];
		var rak = "";
		var receiveMsg = self.receiveMsg();
		for (var i = 0; i < receiveMsg.length; i++) {
//			console.log(receiveMsg[i].selected());
			if (receiveMsg[i].selected() == 1) {
				if (receiveMsg[i].msgStatus() == 0) {
					ids += (ids == "" ? "" : ";");
					ids += receiveMsg[i].id();
					checkedReceiveMsgs.push(receiveMsg[i]);
				} else if (receiveMsg[i].msgStatus() == 1) {
					rak += (rak == "" ? "" : "，");
					rak += "第" + (i + 1) + "条";
				} else {
					rak = " ";
				}
			}
		}
		if (rak != "") {
			showWarningDialog(rak + "已签收，请勿重复签收！");
			return;
		}
		if("" == ids){
			showWarningDialog("请选择数据！");
			return;
		}

		commonJsScreenLock();
		$.ajax({
			url : basePath + "/msg/receiveMsgQs",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				ids : ids
			}),
			success : function(result) {
				if (result != null && result.code == "0") {
					$.each(result.data, function(j, n) {
						$.each(checkedReceiveMsgs, function(k, receiveMsg) {
//							console.log(n.id);
//							console.log(receiveMsg.id());
							if(n.id == receiveMsg.id()){
								receiveMsg.msgStatus(n.msgStatus);
								receiveMsg.qsPeople(n.qsPeople);
								receiveMsg.qsTime(n.qsTime1);
							}
						});
					});
					// 全部修改，如果以后出现后台只能完成一部分的话，就需要调整一下.
					$.each(checkedReceiveMsgs, function(j, receiveMsg) {
						receiveMsg.msgStatus(1);
					});
				} else {
					showErrorDialog("签收失败！");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
//				showErrorDialog("获取差别计划失败2");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});

	}

	self.receiveDel = function() {
		var ids = "";
		var checkedReceiveMsgs = [];
		var rak = "";
		var receiveMsg = self.receiveMsg();
		for (var i = 0; i < receiveMsg.length; i++) {
//			console.log(receiveMsg[i].selected());
			if (receiveMsg[i].selected() == 1) {
				if (receiveMsg[i].msgStatus() == 1) {
					ids += (ids == "" ? "" : ";");
					ids += receiveMsg[i].id();
					checkedReceiveMsgs.push(receiveMsg[i]);
				} else {
					rak += (rak == "" ? "" : "，");
					rak += "第" + (i + 1) + "行";
				}
			}
		}
		if (rak != "") {
			showWarningDialog(rak + "不是已签收状态！");
			return;
		}

		commonJsScreenLock();
		$.ajax({
			url : basePath + "/msg/receiveDel",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				ids : ids
			}),
			success : function(result) {
				if (result != null && result.code == "0") {
					// 注释同签收.
					$.each(checkedReceiveMsgs, function(i, n) {
						self.receiveMsg.remove(n);
					});
				} else {
					showErrorDialog("删除失败！");
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
//				showErrorDialog("获取差别计划失败2");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	}

}

function ReceiveMsg(data) {
	var self = this;
	self.id = ko.observable(data.id);
	self.sendId = ko.observable(data.sendId);
	self.typeName = ko.observable(data.typeName);
	self.msgContents = ko.observable(data.msgContents);
	self.sendBureau = ko.observable(data.sendBureau);
	self.sendPost = ko.observable(data.sendPost);
	self.sendPeople = ko.observable(data.sendPeople);
	self.sendTime = ko.observable(data.sendTime);
	self.qsPeople = ko.observable(data.qsPeople);
	self.qsTime = ko.observable(data.qsTime1);
	self.msgStatus = ko.observable(data.msgStatus);
	self.bureauName = ko.observable(data.bureauName);
	self.selected = ko.observable(0);

	self.selectStateStr = ko.computed(function() {
		if (self.msgStatus() == "0") {
			return "<span class='label label-warning'>未签收</span>";
		} else if (self.msgStatus() == "1") {
			return "<span class='label label-success'>已签收</span>";
		} else if (self.msgStatus() == "2") {
			return "<span class='label label-danger'>已删除</span>";
		}
	});
}

function SendMsg(data,i) {
	var self = this;

	self.receiveList = ko.observableArray();
	$.each(data.receiveList, function(n, receive) {
		self.receiveList.push(new ReceiveList(receive,i))
	})
	self.sendMsgLength = ko.observable(self.receiveList().length);
}

function ReceiveList(data,i) {
	var self = this;
	self.indexI = ko.observable(i);
	self.id = ko.observable(data.id);
	self.msgContents = ko.observable(data.msgContents);
	self.sendPost = ko.observable(data.sendPost);
	self.sendPeople = ko.observable(data.sendPeople);
	self.sendTime = ko.observable(data.sendTime);
	self.msgStatus = ko.observable(data.msgStatus);
	self.typeName = ko.observable(data.typeName);

	self.receiveId = ko.observable(data.id);
	self.receiveBureau = ko.observable(data.receiveBureau);
	self.receivePost = ko.observable(data.receivePost);
	self.receivePeople = ko.observable(data.receivePeople);
	self.receiveTime = ko.observable(data.receiveTime1);
	self.qsPeople = ko.observable(data.qsPeople);
	self.qsTime = ko.observable(data.qsTime1);
	self.receiveStatus = ko.observable(data.receiveStatus);
	self.receiveStatusSpan = ko.computed(function() {
		if (self.receiveStatus() == "0") {
			// 新消息 --> 未签收
			return "<span class='label label-warning'>未签收</span>";
		} else if (self.receiveStatus() == "1") {
			return "<span class='label label-success'>已签收</span>";
		} 
			  else if (self.msgStatus() == "2") { 
				  return "<span class='label label-danger'>已删除</span>"; 
			  }
			 
	});
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