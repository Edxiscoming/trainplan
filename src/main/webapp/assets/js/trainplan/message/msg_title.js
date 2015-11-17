var msg = null;
$(function() {
	heightAuto();

	msg = new MsgModel();
	ko.applyBindings(msg);
	msg.init();
});

function heightAuto() {
	var WH = $(window).height();
//	$("#div_hightline_planDayDetail").css("height", WH - 360 + "px");
//	$("#div_crossDetailInfo_trainStnTable").css("height", 270 + "px");
//	$("#canvas_parent_div").css("height", 203 + "px");
//	$("#left_height").css("height", WH - 150 - 72 + "px");
//	$("#left_height2").css("height", WH - 150 - 72 + "px");
};

function MsgModel() {
	var self = this;

	self.sendPeople = ko.observable();
	self.msglists = ko.observableArray();
	// 列表
	self.receiveMsg = ko.observableArray();
	self.sendMsg = ko.observableArray();

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

		
		var postName = $("#postnameId").val();
		
	//	var postName = null;
		
//		var base = basePath;
//		var _height = $(window).height() - $('#div_searchForm').height() - 500
//				- 150;
//		_height = _height < 80 ? 200 : _height;
//
//		// 接收消息状态
//		self.receiveMsgStatus.push({
//			"name" : "",
//			"id" : null
//		}, {
//			"name" : "未签收",
//			"id" : 0
//		}, {
//			"name" : "已签收",
//			"id" : 1
//		}/*
//			 * , { "name" : "已删除", "id" : 2 }
//			 */);
//		// 发送消息状态
//		self.sendMsgStatus.push({
//			"name" : "",
//			"id" : null
//		}, {
//			"name" : "全部未签收",
//			"id" : 0
//		}, {
//			"name" : "部分签收",
//			"id" : 1
//		}, {
//			"name" : "全部签收",
//			"id" : 2
//		});
		// 消息类型
//		commonJsScreenLock(1);
		$.ajax({
			url : "/trainplan/rmsg/getMsgTitle?postname="+postName,
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
							self.msglists.push(new MsgObj(types));
						});
					// 是否出现右边17
					}

				} else {
//					showErrorDialog("查询不到消息类型");
				}
			},
			error : function() {
//				showErrorDialog("获取消息类型失败");
			},
			complete : function() {
//				commonJsScreenUnLock();
			}
		});

//		$.ajax({
//			url : basePath + "/plan/getFullStationInfo",
//			cache : false,
//			type : "GET",
//			dataType : "json",
//			contentType : "application/json",
//			success : function(result) {
//				if (result != null && result != "undefind"
//						&& result.code == "0") {
//					var bureaus = new Array();
//					var curUserBur = bureauShortName;
//					var fistObj = null;
//					if (result.data != null && result.data != undefined) {
//						for (var i = 0; i < result.data.length; i++) {
//							var obj = result.data[i];
//							if (obj.ljjc == curUserBur) {
//								fistObj = obj;
//							} else {
//								bureaus.push(obj);
//							}
//						}
//						if (null != fistObj) {
//							bureaus.unshift("",fistObj);
//						}
//						$.each(bureaus, function(n, bureau) {
//							self.bureaus.push({
//								"name" : bureau.ljjc,
//								"id" : bureau.ljpym
//							});
//						});
//						// 是否出现右边17
//						var tableH = $("#left_height table").height();
//						var tableH2 = $("#left_height2 table").height();
//						var boxH = $("#left_height").height();
//						var boxH2 = $("#left_height2").height();
//						if (tableH <= boxH) {
//							$("#left_height .td_17").removeClass("display");
//						} else if(tableH > boxH) {
//							$("#left_height .td_17").addClass("display");
//						} else if(tableH2 <= boxH2){
//							$("#left_height2 .td_17").removeClass("display");
//						} else if(tableH2 > boxH2){
//							$("#left_height2 .td_17").addClass("display");
//						}
//					}
//
//				} else {
//					showErrorDialog("获取路局列表失败");
//				}
//			},
//			error : function() {
//				showErrorDialog("获取路局列表失败");
//			},
//			complete : function() {
//				commonJsScreenUnLock();
//			}
//		});
//
//	};
//
//	self.loadReceiveMsg = function() {
//		var receiveStartDate = $("#receive_start_date").val();
//		var receiveEndDate = $("#receive_end_date").val();
//		var bureauCode = $("#receive_msg_bureaus").val();
//		var typeCode = $("#receive_msg_types").val();
//		var statusCode = $("#receive_msg_status").val();
//		var sendPeople = $("#sendPeople").val();
//		var kw = $("#kw").val();
//
//		if ("" == receiveStartDate || "" == receiveEndDate) {
//			showWarningDialog("请选择日期！");
//			return;
//		}
////		if (receiveStartDate >= receiveEndDate) {
////			showWarningDialog("截至日期必须大于开始日期！");
////			return;
////		}
////		if (null == bureauCode || "" == bureauCode) {
////			showWarningDialog("发消息局不能为空！");
////			return;
////		}
//
//		self.receiveMsg.remove(function(item) {
//			return true;
//		});
//
//		commonJsScreenLock();
//		$.ajax({
//			url : basePath + "/msg/getReceiveMsg",
//			cache : false,
//			type : "POST",
//			dataType : "json",
//			contentType : "application/json",
//			data : JSON.stringify({
//				receiveStartDate : receiveStartDate,
//				receiveEndDate : receiveEndDate,
//				bureauCode : bureauCode != "" ? bureauCode : null,
//				typeCode : typeCode != "" ? typeCode : null,
//				statusCode : statusCode != "" ? statusCode : null,
//				sendPeople : sendPeople != "" ? sendPeople : null,
//				kw : kw != "" ? kw : null
//			}),
//			success : function(result) {
//				if (result != null && result.code == "0") {
//					if (result.data != null) {
//						$.each(result.data, function(i, n) {
//							self.receiveMsg.push(new ReceiveMsg(n));
//						});
//					}
//				} else {
//					showErrorDialog("查询失败" + result.message);
//				}
//			},
//			error : function() {
//				showErrorDialog("查询失败" + XMLHttpRequest.status);
//			},
//			complete : function() {
//				commonJsScreenUnLock();
//			}
//		});
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


	}



}

function MsgObj(data) {
	var self = this;
//	self.context = ko.observable(data.serial);
	self.typeName = ko.observable(data.typeName);
	self.sendBureau = ko.observable(data.sendBureau);
	self.sendPost = ko.observable(data.sendPost);
	self.sendTime = ko.observable(data.sendTime);
	self.msgStatus = ko.observable(data.msgStatus);
}