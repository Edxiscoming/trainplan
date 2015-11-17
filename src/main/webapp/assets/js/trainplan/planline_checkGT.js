/**
 * Created by star on 5/21/14.
 */

var currentUserBureauName1;
var trainCfIds = "";
var trainCfNum = ""; 
var app = null;
$(function() {
	heightAuto();
	app = new ApplicationModel();

	ko.applyBindings(app);
	app.initPage();
});

function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#left_height").css("height",WH-115+"px");
};


// ----------- 应用模型 -------------------
function ApplicationModel() {
	var self = this;

	self.sents = ko.observableArray();
	self.currentUserBureauName = ko.observable();


	self.tableModel = ko.observable(new TableModel());
	self.searchModle = ko.observable(new SearchModle());		//页面查询对象
	self.paramModel = ko.observable(new ParamModel());

	self.allBtn = ko.observable(false);

	self.currCheckNbr = ko.observable(0);
	
	self.isTrainCfMatch = ko.observable("0");
//	self.trainCfIds = ko.observable();
	self.isTrainCfData = ko.observableArray();
	self.isTrainCfArray = ko.observableArray();

	self.openOuterTrainLine = function() {
		var url = "outer/trainline/" + $("#date_selector").val();
		// top._getDialog(url, {title: "冗余运行线", height: $(window).height(),
		// width: 1300}).dialog("open");
		top.modelDialog(url, {
			width : 1300,
			height : 600,
			title : "冗余运行线"
		});
	}
	
	self.openTrainCf = function() {
		if (null != trainCfIds && "" != trainCfIds) {
			$('#trainCf').dialog("open");
			self.isTrainCfArray.remove(function(item) {
				return true;
			});
			commonJsScreenLock();
			$.ajax({

				url : basePath + "/audit/plan/getTrainCf",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data : JSON.stringify({
					planTrainId : trainCfIds
				}),
				success : function(result) {
					if (result != null && result != "undefind") {
						$.each(result.data, function(n, z) {
							self.isTrainCfArray.push(new TrainCf(z))
						});
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

		}else{
			showWarningDialog("暂无重复列车信息！");
		}
	}

	self.search = function() {
		self.tableModel().loadTable();
	}

	self.checkStatus = ko.computed(function() {
		if (self.currCheckNbr() > 0
				&& self.currCheckNbr() < self.tableModel().planList().length) {
			return " | 正在校验： " + self.currCheckNbr() + " / "
					+ self.tableModel().planList().length;
		} else {
			return "";
		}
	})

	self.canCheckLev1 = ko.computed(function() {
		var flag = false;
		if (self.currCheckNbr() > 0
				&& self.currCheckNbr() != self.tableModel().planList().length) {
			return flag;
		}
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if (plan.needLev1()) {
				flag = true;
				;
				return true;
			}
		});
		return flag;
	});

	self.canCheckLev2 = ko.computed(function() {
		var flag = false;
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if (plan.needLev2()) {
				flag = true;
				;
				return true;
			}
		});
		return flag;
	});

	self.autoCheck333 = function() {
		self.currCheckNbr(0);
//		self.trainCfIds();
		trainCfIds = "";
		trainCfNum = 0;
		commonJsScreenLock();
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			// if(plan.dailyLineFlag() != 0 || !plan.dailyLineId()) {
			// self.currCheckNbr(self.currCheckNbr() + 1);
			// plan.isTrainInfoMatch(-1);
			// plan.isTimeTableMatch(-1);
			// plan.isRoutingMatch(-1);
			// }
			// else {
			$.ajax(
					{
						url : "audit/plan/" + plan.id() + "/line/"
								+ plan.dailyLineId() + "/check",
						method : "GET",
						contentType : "application/json; charset=UTF-8"
					}).done(function(data) {
				plan.isTrainInfoMatch(data.isTrainInfoMatch);
				plan.isTimeTableMatch(data.isTimeTableMatch);
//				plan.isRoutingMatch(data.isRoutingMatch);
				trainCfNum += data.isTrainCfMatch;
				self.isTrainCfMatch(trainCfNum);
				trainCfIds += data.isTrainCfData;
			}).fail(function() {

			}).always(function() {
				self.currCheckNbr(self.currCheckNbr() + 1);
				commonJsScreenUnLock();
			})
			// }
		});
		commonJsScreenUnLock();
	}
	
	//新架构下的校验方式
	self.autoCheck = function() {

		$(this).prop("disabled", true)
		var map = new Array();

		var p = new Object();
		p.date = moment($("#date_selector").val()).format("YYYYMMDD");
		p.trainType = 1;// 高铁
		map.push(p);

		var data = new Array();
		var data2 = new Array();
		var errorI = false;
		var completionCount = 0;//校验成功的数量

		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			// 判断是否选中
			if (plan.isSelected()) {
				// 判断是否生成1条线
				if (plan.dailyLineFlag() == 1) {
					var paramObj = new Object();
					paramObj.planId = plan.id();
					paramObj.lineId = plan.dailyLineId();
					data2.push(paramObj);
				} else {
					errorI = true;
				}
			}
		});
		
		if (errorI) {
			showWarningDialog("校验失败,请检查您所选择的数据是否已生成一条线！");
			return;
		}
		if (data2.length <= 0) {
			$(this).prop("disabled", false);
			return false;
		}
		commonJsScreenLock();
		$.ajax({
			url : basePath + "/audit/plan/completedVaild",
			cache : false,
			type : "POST",
			dataType : "json",
			data : JSON.stringify({
				data : data2,
				msgReceiveUrl : "/trainplan/audit",
				msgReceiveMethod:"hdCompletionVaild"
			}),
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					showSuccessDialog("请求成功，请等待结果返回！");				
				} else {
					showErrorDialog("校验请求失败失败！");
				}
			},
			error : function() {
				showErrorDialog("校验请求失败失败！");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});


	
	}

	self.selectAllLev1 = function() {
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if (self.allBtn() == 0) {
				if (plan.needLev1()) {
					plan.isSelected(true);
				}
			} else {
				plan.isSelected(false);
			}
		})
		return true;
	}

	self.selectAllLev2 = function() {
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if (self.allBtn()) {
				if (plan.needLev2()) {
					plan.isSelected(true);
				}
			} else {
				plan.isSelected(false);
			}
		})
		return true;
	}
	//checkbox change事件
	self.selectBox = function(row) {
		if(row.isSelected() == 0){  //选中的时候
			self.allBtn(1);
			ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
				if (plan.isSelected() !=1 && plan != row && plan.needLev1() == 1){
					self.allBtn(0);
				}
			});
		}
		else{
//			ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
				self.allBtn(0);
//			});
		}
	}
	
	// 一级审核
	self.checkLev1 = function() {
		$(this).prop("disabled", true)
		var data = new Array();
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if (plan.isSelected()) {
				var paramObj = new Object();
				paramObj.planId = plan.id();
				paramObj.lineId = plan.dailyLineId();
				data.push(paramObj);
			}
		})
		if (data.length <= 0) {
			$(this).prop("disabled", false);
			return false;
		}
		$
				.ajax({
					url : "audit/plan/checklev1/1",
					method : "POST",
					dataType : "json",
					data : JSON.stringify(data),
					contentType : "application/json; charset=UTF-8"
				})
				.done(
						function(response) {
							ko.utils
									.arrayForEach(
											response,
											function(resp) {
												for (var i = 0; i < self
														.tableModel()
														.planList().length; i++) {
													if (resp.id == self
															.tableModel()
															.planList()[i].id()) {
														self.tableModel()
																.planList()[i]
																.checkLev1(resp.checkLev1);
														self.tableModel()
																.planList()[i]
																.checkLev2(resp.checkLev2);
														self.tableModel()
																.planList()[i]
																.lev1Checked(resp.lev1Checked);
//														self.tableModel()
//																.planList()[i]
//																.lev2Checked(resp.lev2Checked);
//														self.tableModel()
//																.planList()[i]
//																.isSelected(false);
													}
												}
											});
							$(this).prop("disabled", false);
							$.gritter.add({
								title : getHintTitle(data.length,
										response.length),
								text : '审核成功[' + response.length + ']条计划',
								class_name : getHintCss(data.length,
										response.length),
								image : 'assets/img/screen.png',
								sticky : false,
								time : 3000
							});
							self.paramModel().loadPies();
						}).fail(function(resp) {
					$(this).prop("disabled", false);
					$.gritter.add({
						title : '审核出错',
						text : resp,
						class_name : 'growl-danger',
						image : 'assets/img/screen.png',
						sticky : false,
						time : 3000
					});
				}).always(function() {
				})
	}

	// 二级审核
	self.checkLev2 = function() {
		$(this).prop("disabled", true)
		var data = new Array();
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if (plan.isSelected()) {
				var paramObj = new Object();
				paramObj.planId = plan.id();
				paramObj.lineId = plan.dailyLineId();
				data.push(paramObj);
			}
		})
		if (data.length <= 0) {
			$(this).prop("disabled", false);
			return false;
		}
		$
				.ajax({
					url : "audit/plan/checklev2/1",
					method : "POST",
					dataType : "json",
					data : JSON.stringify(data),
					contentType : "application/json; charset=UTF-8"
				})
				.done(
						function(response) {
							ko.utils
									.arrayForEach(
											response,
											function(resp) {
												for (var i = 0; i < self
														.tableModel()
														.planList().length; i++) {
													if (resp.id == self
															.tableModel()
															.planList()[i].id()) {
														// self.tableModel().planList()[i].checkLev1(resp.checkLev1);
														// self.tableModel().planList()[i].checkLev2(resp.checkLev2);
														self.tableModel()
																.planList()[i]
																.lev1Checked(resp.lev1Checked);
//														self.tableModel()
//																.planList()[i]
//																.lev2Checked(resp.lev2Checked);
//														self.tableModel()
//																.planList()[i]
//																.isSelected(false);
													}
												}
											});
							$(this).prop("disabled", false);
							$.gritter.add({
								title : getHintTitle(data.length,
										response.length),
								text : '审核成功[' + response.length + ']条计划',
								class_name : getHintCss(data.length,
										response.length),
								image : 'assets/img/screen.png',
								sticky : false,
								time : 3000
							});
							self.paramModel().loadPies();
						}).fail(function(resp) {
					$(this).prop("disabled", false);
					$.gritter.add({
						title : '审核出错',
						text : resp,
						class_name : 'growl-danger',
						image : 'assets/img/screen.png',
						sticky : false,
						time : 3000
					});
				}).always(function() {
				})
	}
	
	self.updCompletionDWR = function(data){
//		console.log("进入updCompletionDWR");
//		console.log(data);
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if(plan.id() == data.planTrainId){
				plan.lev2Checked(data.createFlag);
			}
		})
	}
	
	self.updCompletionVaildDWR = function(data){
		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			if(plan.id() == data.planTrainId){
				plan.vaildStatus(data.createFlag);
				plan.isTrainInfoMatch(data.createFlag);
				plan.isTimeTableMatch(data.createFlag);

			}
		})
	}
	
	// 落成，业务待开发
	self.completion1 = function() {
		$(this).prop("disabled", true)
		var map = new Array();

		var p = new Object();
		p.date = moment($("#date_selector").val()).format("YYYYMMDD");
		p.trainType = 1;// 高铁
		map.push(p);

		var data = new Array();
		var data2 = new Array();
		var errorI = false;
		var errorVaild = false;
		var completionCount = 0;//落成成功的数量

		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			// 判断是否选中
			if (plan.isSelected()) {
				// 判断是否生成1条线
				if (plan.dailyLineFlag() == 1 && plan.vaildStatus() == 1) {
					var paramObj = new Object();
					paramObj.planId = plan.id();
					paramObj.lineId = plan.dailyLineId();
					data2.push(paramObj);
				} else if(plan.dailyLineFlag() != 1){
					errorI = true;
				}
				else {
					errorVaild = true;
				}
				
				//判断数据是否已经通过校验
				
			}
		});
		
		if (errorI) {
			showWarningDialog("落成失败,请检查您所选择的数据是否已生成一条线！");
			return;
		}
		
		if (errorVaild) {
			showWarningDialog("落成失败,请检查您所选择的数据是否已通过校验！");
			return;
		}
		if (data2.length <= 0) {
			$(this).prop("disabled", false);
			return false;
		}
		commonJsScreenLock();
		$.ajax({
			url : basePath + "/audit/plan/completion1",
			cache : false,
			type : "POST",
			dataType : "json",
			data : JSON.stringify({
				data : data2,
				msgReceiveUrl : "/trainplan/audit",
				msgReceiveMethod:"hdCompletion"
			}),
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {					
						showSuccessDialog("落成成功！");
					
				} else {
					showErrorDialog("落成失败！");
				}
			},
			error : function() {
				showErrorDialog("落成失败！");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});


	}
	
	//落成，业务待开发
	self.completion = function() {
		$(this).prop("disabled", true)
		var map = new Array();

		var p = new Object();
		p.date = moment($("#date_selector").val()).format("YYYYMMDD");
		p.trainType = 1;// 高铁
		map.push(p);

		var data = new Array();
		var data2 = new Array();
		var errorI = false;
		var completionCount = 0;//落成成功的数量

		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
			// 判断是否选中
			if (plan.isSelected()) {
				// 判断是否生成1条线
//				if (plan.dailyLineFlag() == 1) {
					var paramObj = new Object();
					paramObj.planId = plan.id();
					paramObj.lineId = plan.dailyLineId();
					data2.push(paramObj);
//				} else {
//					errorI = true;
//				}
			}
		})
		
		if (errorI) {
			showWarningDialog("落成失败,请检查您所选择的数据是否已生成一条线！");
			return;
		}
		if (data2.length <= 0) {
			$(this).prop("disabled", false);
			return false;
		}
		commonJsScreenLock();
		$.ajax({
			url : basePath + "/audit/plan/completion1",
			cache : false,
			type : "POST",
			dataType : "json",
			data : JSON.stringify({
				data : data2,
				msgReceiveUrl : "/trainplan/audit",
				msgReceiveMethod:"hdCompletion"
			}),
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind"
						&& result.code == "0") {
					if (result.data != null && result.data != undefined) {
						// 成功返回以后的操作.
						showSuccessDialog("落成成功！");
					}
				} else {
					showErrorDialog("落成失败！");
				}
			},
			error : function() {
				showErrorDialog("落成失败！");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
		
		// 2015-5-11 10:46:24 注释.
//		ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
//			// 判断是否选中
//			if (plan.isSelected()) {
//				// 判断是否生成1条线
//				if (plan.dailyLineFlag() == 1) {
//					var paramObj = new Object();
//					var data2 = new Array();
//					
//					paramObj.planId = plan.id();
//					paramObj.lineId = plan.dailyLineId();
//					paramObj.sentStatus = plan.lev2Checked();
//					data.push(paramObj);
//					data2.push(paramObj);
//					var isChecked = false;
//					//ajax1 start
//					$.ajax({
//								url : "audit/plan/" + plan.id() + "/line/" + plan.dailyLineId() + "/check",
//								method : "GET",
//								contentType : "application/json; charset=UTF-8"
//							}).done(function(data) {
//								  plan.isTrainInfoMatch(data.isTrainInfoMatch);
//								  plan.isTimeTableMatch(data.isTimeTableMatch);
//								  trainCfNum += data.isTrainCfMatch;
//								self.isTrainCfMatch(trainCfNum);
//								trainCfIds += data.isTrainCfData;
//								isChecked = true;
//							}).fail(function() {
//				
//							}).always(function() {
//								self.currCheckNbr(self.currCheckNbr() + 1);
//								if(isChecked){
//									//ajax2 start
//									$.ajax({
//										url : "audit/plan/completion",
//										method : "POST",
//										dataType : "json",
//										data : JSON.stringify({
//											data : data2,
//											map : map,
//											currentUserBureauName : currentUserBureauName
//										}),
//										contentType : "application/json; charset=UTF-8"
//									})
//									.done(
//											function(response) {
//												ko.utils
//														.arrayForEach(
//																response,
//																function(resp) {
//																	if (null != resp.error) {
//																		showErrorDialog(resp.error);
//																	} else {
//																		for ( var i = 0; i < self
//																				.tableModel()
//																				.planList().length; i++) {
//																			for ( var j = 0; j < data.length; j++) {
//																				console
//																						.log(data[j].planId);
//																				console
//																						.log(self
//																								.tableModel()
//																								.planList()[i]
//																								.id());
//																				if (data[j].planId == self
//																						.tableModel()
//																						.planList()[i]
//																						.id()) {
//																					self
//																							.tableModel()
//																							.planList()[i]
//																							.lev2Checked(resp.lev2Checked);
//																					// self
//																					// .tableModel()
//																					// .planList()[i]
//																					// .isSelected(false);
//																				}
//																			}
//																		}
//																		if (null != resp.successwarn) {
//																			showWarningDialog(resp.successwarn);
//																		}
//																		if (null != resp.success) {
//																			showSuccessDialog(resp.success);
//																		}
//																	}
//																});
//												$(this).prop("disabled", false);
//												self.paramModel().loadPies();
//											}).fail(function(resp) {
//										$(this).prop("disabled", false);
//									}).always(function() {
////										commonJsScreenUnLock();
//									})//ajax2 end
//								}
//							})////ajax1 end
//					
//					
//					
//					
//				} else {
//					errorI = true;
//				}
//			}
//		})
//		if (errorI) {
//			showWarningDialog("落成失败,请检查您所选择的数据是否已生成一条线！");
//			return;
//		}
//		if (data.length <= 0) {
//			$(this).prop("disabled", false);
//			return false;
//		}
		var currentUserBureauName = self.currentUserBureauName;

		//先
		/*
		 * 	
		self.autoCheck = function() {
		self.currCheckNbr(0);
		// self.trainCfIds();
		trainCfIds = "";
		trainCfNum = 0;
		commonJsScreenLock();
//		console.log(1111111111);
		$.each(self.tableModel().planList(), function(i, plan) {
			console.log(plan.id());
			$.ajax({
						url : "audit/plan/" + plan.id() + "/line/" + plan.dailyLineId() + "/check",
						method : "GET",
						contentType : "application/json; charset=UTF-8"
					}).done(function(data) {
						  plan.isTrainInfoMatch(data.isTrainInfoMatch);
						  plan.isTimeTableMatch(data.isTimeTableMatch);
//						  plan.isRoutingMatch(data.isRoutingMatch);
				trainCfNum += data.isTrainCfMatch;
				self.isTrainCfMatch(trainCfNum);
				trainCfIds += data.isTrainCfData;
			}).fail(function() {

			}).always(function() {
				self.currCheckNbr(self.currCheckNbr() + 1);
				commonJsScreenUnLock();
			})
		});
		commonJsScreenUnLock();
		// ko.utils.arrayForEach(self.tableModel().planList(), function(plan) {
		// // if(plan.dailyLineFlag() != 0 || !plan.dailyLineId()) {
		// // self.currCheckNbr(self.currCheckNbr() + 1);
		// // plan.isTrainInfoMatch(-1);
		// // plan.isTimeTableMatch(-1);
		// // plan.isRoutingMatch(-1);
		// // }
		// // else {
		// console.log(plan);
		// $.ajax(
		// {
		// url : "audit/plan/" + plan.id() + "/line/"
		// + plan.dailyLineId() + "/check",
		// method : "GET",
		// contentType : "application/json; charset=UTF-8"
		// }).done(function(data) {
		// // plan.isTrainInfoMatch(data.isTrainInfoMatch);
		// // plan.isTimeTableMatch(data.isTimeTableMatch);
		// // plan.isRoutingMatch(data.isRoutingMatch);
		// trainCfNum += data.isTrainCfMatch;
		// self.isTrainCfMatch(trainCfNum);
		// trainCfIds += data.isTrainCfData;
		// }).fail(function() {
		//
		// }).always(function() {
		// self.currCheckNbr(self.currCheckNbr() + 1);
		// commonJsScreenUnLock();
		//			})
		//			// }
		//		});
	}
		 * 
		 */
		
		
		
		
	/*	
		commonJsScreenLock();
		$
				.ajax({
					url : "audit/plan/completion",
					method : "POST",
					dataType : "json",
					data : JSON.stringify({
						data : data,
						map : map,
						currentUserBureauName : currentUserBureauName
					}),
					contentType : "application/json; charset=UTF-8"
				})
				.done(
						function(response) {
							ko.utils
									.arrayForEach(
											response,
											function(resp) {
												if (null != resp.error) {
													showErrorDialog(resp.error);
												} else {
													for ( var i = 0; i < self
															.tableModel()
															.planList().length; i++) {
														for ( var j = 0; j < data.length; j++) {
															console
																	.log(data[j].planId);
															console
																	.log(self
																			.tableModel()
																			.planList()[i]
																			.id());
															if (data[j].planId == self
																	.tableModel()
																	.planList()[i]
																	.id()) {
																self
																		.tableModel()
																		.planList()[i]
																		.lev2Checked(resp.lev2Checked);
																// self
																// .tableModel()
																// .planList()[i]
																// .isSelected(false);
															}
														}
													}
													if (null != resp.successwarn) {
														showWarningDialog(resp.successwarn);
													}
													if (null != resp.success) {
														showSuccessDialog(resp.success);
													}
												}
											});
							$(this).prop("disabled", false);
							// $.gritter.add({
							// title : getHintTitle(data.length,
							// response.length),
							// text : '落成成功[' + response.length + ']条计划',
							// class_name : getHintCss(data.length,
							// response.length),
							// image : 'assets/img/screen.png',
							// sticky : false,
							// time : 3000
							// });
							self.paramModel().loadPies();
						}).fail(function(resp) {
					$(this).prop("disabled", false);
					// 上线使用,一直报错,先暂时注释掉,保证不弹出那个报错信息.2015-1-7 10:17:33
					// $.gritter.add({
					// title : '落成出错',
					// text : resp,
					// class_name : 'growl-danger',
					// image : 'assets/img/screen.png',
					// sticky : false,
					// time : 3000
					// });
				}).always(function() {
					commonJsScreenUnLock();
				})*/

	}
	self.isShowCrossDetailInfo = ko.observable(false);// 显示交路详情复选框 默认no勾选
	self.showChatPia = function() {
		var WH = $(window).height();
		if (!self.isShowCrossDetailInfo()) {// 勾选
			$("#chatId").show();
			//$("#body_right").attr("style","height: 530px; overflow-y: scroll; padding: 15px; margin-bottom: 15px;margin:-5px -3px 10px -17px");
			$("#left_height").css({"height":WH-105-270+"px"});
			$("#body_right").css({"margin-bottom":"10px"});
			self.paramModel().loadPies();
		} else {
			$("#chatId").hide();
			$("#left_height").css("height",WH-115+"px");
			$("#body_right").css({"margin-bottom":"0px"});
			//$("#body_right").attr("style","height: 795px; overflow-y: scroll; padding: 15px; margin-bottom: 15px;margin: -5px -5px 0 -17px;");
		}
	}
	
	self.initPage = function () {
		$("#completion_log").dialog("close");
		$("#kyvsyx").dialog("close");
		$("#kyskvsyxsk").dialog("close");
		$('#trainCf').dialog("close");
		
		$.ajax({
			url : basePath+"/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {
				var bureaus = new Array();
				if (result != null && result != "undefind" && result.code == "0") {
					var curUserBur = currentUserBureauName;
					var bureau = currentUserBureauName;
					self.currentUserBureauName = currentUserBureauName;
					currentUserBureauName1 = currentUserBureauName;
					var fistObj =null;
					if(result.data!=null && result.data!=undefined){
						for(var i=0;i<result.data.length;i++){
							var obj = result.data[i];
							if(obj.ljjc==curUserBur){
								fistObj = obj;
							}else{
								bureaus.push(obj);
							}
						}
						
						bureaus.unshift(fistObj,"");
					}
					self.searchModle().loadBureau(bureaus,bureau);
				}
			}
	    });
	}
	
//	/**
//	 * 落成记录.
//	 */
//	self.completionLog = function(){
//		// 当前登录局
//		var currentUserBureauName = self.currentUserBureauName;
//		console.log(currentUserBureauName);
//		if (null == currentUserBureauName) {
//			showWarningDialog("获取不到路局");
//			return;
//		}
//		// 当前选中行
//		var data = [];
//		$.each(self.tableModel().planList(), function(i, row) {
//			if (row.isSelected()) {
//				data.push(row);
//			}
//		});
//
//		if (data.length == 0) {
//			showWarningDialog("请选择记录");
//			return;
//		}
//		if (data.length != 1) {
//			showWarningDialog("只能选择一条记录");
//			return;
//		}
//		self.sents.removeAll();
//		$('#completion_log').dialog("open");
//		//$("#completion_log").show();
//		$.ajax({
//			url : basePath + "/audit/plan/completionLog",
//			cache : false,
//			type : "POST",
//			dataType : "json",
//			contentType : "application/json",
//			data : JSON.stringify({
//				sent_people_bureau : currentUserBureauName,
//				plan_train_Id : data[0].id()
//			}),
//			success : function(result) {
//				if (result != null && result != "undefind") {
//					$.each(result.data, function(n, z) {
//						self.sents.push(new PlanSentRow(z))
//					});
//				} else {
//					showErrorDialog("获取记录失败");
//				}
//			},
//			error : function() {
//				showErrorDialog("获取记录失败");
//			},
//			complete : function() {
//				commonJsScreenUnLock();
//			}
//		});
//	}
	
}


function PlanSentRow(data) {
	var self = this;
	self.run_date = ko.observable(data.run_date);
	self.train_nbr = ko.observable(data.train_nbr);
	self.cross_name = ko.observable(data.cross_name);
	self.sent_people_bureau = ko.observable(data.sent_people_bureau);
	self.sent_time = ko.observable(data.sent_time);
	self.sent_people = ko.observable(data.sent_people);
	self.sent_people_org = ko.observable(data.sent_people_org);
	self.plan_train_id = ko.observable(data.plan_train_id);
	self.pre_train_id = ko.observable(data.pre_train_id);
	self.next_train_id = ko.observable(data.next_train_id);
}

function SearchModle(){
	var _self = this;
	_self.bureau = ko.observable();
	_self.bureaus = ko.observableArray();
	
	_self.loadBureau = function(bureaus,bureau){   
		for ( var i = 0; i < bureaus.length; i++) {  
			_self.bureaus.push(new BureausRow(bureaus[i])); 
			if(bureaus[i].ljjc==currentUserBureauName){
				_self.bureau = bureaus[i].ljjc;
			}
		} 
	};
	function BureausRow(data) {
		var self = this;  
		self.shortName = data.ljjc;   
		self.code = data.ljpym;   
		//方案ID 
	}
}
// ########### 页面参数模型 ###############
function ParamModel() {
	var self = this;

	self.unknownRunLine = ko.observable(0);

	// 初始化时间控件
	$("#date_selector").datepicker({
		format : "yyyy-mm-dd",
		// weekStart: 1,
		autoclose : true,
		todayBtn : 'linked',
		language : 'zh-CN'
	});
//	var date = $.url().param("date");
	var date = moment(new Date((new Date()/1000+86400*1)*1000)).format("YYYY-MM-DD");
	if (date) {
		$("#date_selector").val(date);
	} else {
		$("#date_selector").datepicker('setValue', new Date());
	}
	;

	self.loadPies = function() {
		var date = moment($("#date_selector").val()).format("YYYY-MM-DD");
		// 统计图
		$.ajax(
				{
					url : "audit/plan/chart/traintype/" + date + "/"
							+ $("#train_type").val() + "?trainType=1&name="
							+ $("#train_nbr").val(),
					method : "GET",
					contentType : "application/json; charset=UTF-8"
				}).done(
				function(resp) {
					if (resp && resp.length) {
						var name = Array();
						var data = Array();
						var dataArrayFinal = Array();
						for (var i = 0; i < resp.length; i++) {
							name[i] = resp[i].name;
							data[i] = resp[i].count;
							dataArrayFinal[i] = new Array(name[i], data[i]);
						}
						drawPie($("#chart_01"), '开行状态', dataArrayFinal, [
								'#006400', '#808080', '#8b0000', '#C800C8' ]);
					}

				}).fail(function() {

		}).always(function() {

		})

		$.ajax(
				{
					url : "audit/plan/chart/planline/" + date + "/"
							+ $("#train_type").val() + "?trainType=1&name="
							+ $("#train_nbr").val(),
					method : "GET",
					contentType : "application/json; charset=UTF-8"
				}).done(
				function(resp) {
					if (resp && resp.length) {
						var name = Array();
						var data = Array();
						var dataArrayFinal = Array();
						for (var i = 0; i < resp.length; i++) {
							name[i] = resp[i].name;
							data[i] = resp[i].count;
							dataArrayFinal[i] = new Array(name[i], data[i]);
						}
						drawPie($("#chart_02"), '一条线状态', dataArrayFinal, [
								'#006400', '#8b0000', '#C800C8' ]);
					}

				}).fail(function() {

		}).always(function() {

		});

		if ($("#chart_03").size() == 1) {
			$.ajax(
					{
						url : "audit/plan/chart/lev1check/" + date + "/"
								+ $("#train_type").val() + "?trainType=1&name="
								+ $("#train_nbr").val(),
						method : "GET",
						contentType : "application/json; charset=UTF-8"
					})
					.done(
							function(resp) {
								if (resp && resp.length) {
									var name = Array();
									var data = Array();
									var dataArrayFinal = Array();
									for (var i = 0; i < resp.length; i++) {
										name[i] = resp[i].name;
										data[i] = resp[i].count;
										dataArrayFinal[i] = new Array(name[i],
												data[i]);
									}
									drawPie($("#chart_03"), '审核状态',
											dataArrayFinal, [ '#006400',
													'#8b0000' ]);
								}

							}).fail(function() {

					}).always(function() {

					})
		} else if ($("#chart_04").size() == 1) {
			$.ajax(
					{
						url : "audit/plan/chart/lev2check/" + date + "/"
								+ $("#train_type").val() + "?trainType=1&name="
								+ $("#train_nbr").val(),
						method : "GET",
						contentType : "application/json; charset=UTF-8"
					})
					.done(
							function(resp) {
								if (resp && resp.length) {
									var name = Array();
									var data = Array();
									var dataArrayFinal = Array();
									for (var i = 0; i < resp.length; i++) {
										name[i] = resp[i].name;
										data[i] = resp[i].count;
										dataArrayFinal[i] = new Array(name[i],
												data[i]);
									}
									drawPie($("#chart_04"), '审核状态',
											dataArrayFinal, [ '#006400',
													'#8b0000' ]);
								}

							}).fail(function() {

					}).always(function() {

					})
		}

		$.ajax({
			url : "audit/check/line/" + $("#date_selector").val() + "/unknown",
			method : "GET",
			contentType : "application/json; charset=UTF-8"
		}).done(function(resp) {
			if (resp.code) {

			} else {
				self.unknownRunLine(resp.NUM);
			}

		}).fail(function() {

		}).always(function() {

		})
	}

}

// ################# 列表模型 #############
function TableModel() {
	var self = this;

	self.planList = ko.observableArray();

	self.loadTable = function() {
		commonJsScreenLock();
		var date = moment($("#date_selector").val()).format("YYYY-MM-DD");
		var type = $("#train_type").val();
		var sentFlag = $("#sentFlag").val();
		var tokenVehBureauNbr = $("#token_veh_bureau_nbr").val();
		var train_nbr = null;
		if(null != $("#train_nbr").val() && "" != $("#train_nbr").val()){
			train_nbr = $("#train_nbr").val();
		}
		var url = "audit/plan/runplan1/" + date + "/" + type + "/" + sentFlag + "/" + tokenVehBureauNbr + "?train_nbr=" + train_nbr + "&trainType=1";
		$.ajax(
				{
					url : url,
					method : "GET",
					contentType : "application/json; charset=UTF-8"
				}).done(function(list) {
			self.planList.removeAll();
			for (var i = 0; i < list.length; i++) {
				self.planList.push(new Plan(list[i]));
			}
			//是否出现右边17
			var tableH = $("#left_height table").height();
			var boxH = $("#left_height").height();
			if(tableH <= boxH){
				//alert("没");
				$(".td_17").removeClass("display");
			}else{
				//alert("有");
				$(".td_17").addClass("display");
			}
			
			// 表头固定
			// $("#plan_table").freezeHeader();
		}).fail(function() {

		}).always(function() {
			commonJsScreenUnLock();
			//$("#autoCheckall").click()
		})
	};
}

function Plan(dto) {
	var self = this;

	self.passBureau = ko.observable(dto.passBureau);
	self.checkedBureau = ko.observable(dto.checkedBureau);
	self.dailyplan_check_bureau = ko.observable(dto.dailyplan_check_bureau);
	self.str1 = ko.observable(dto.str1);
	// properties
	self.id = ko.observable(dto.id);
	self.name = ko.observable(dto.serial);
	self.startStn = ko.observable(dto.startStn);
	self.startTime = ko.observable(moment(dto.startTime).format("MMDD HH:mm"));
	self.endStn = ko.observable(dto.endStn);
	self.endTime = ko.observable(moment(dto.endTime).format("MMDD HH:mm"));

	self.jrStn = ko.observable(dto.jrStn);
	self.jrTime = ko.computed(function(){
		if (dto.jrTime !=null && dto.jrTime !="null"&&dto.jrTime !=undefined && dto.jrTime !="") {
			return moment(dto.jrTime).format("MMDD HH:mm");
			
		} else {
			return "";
		}
	});
	
	
	self.jrTime1 = ko.computed(function() {
		if (self.jrTime()==''||self.jrTime()=='Invalid date'||self.jrTime()==null) {
			return "";
			
		} else {
			
			return moment(dto.jrTime).format("MMDD HH:mm");
		}
	});
	self.jcStn = ko.observable(dto.jcStn);
	self.jcTime = ko.computed(function(){
		if (
				
				dto.jcTime !=null && dto.jcTime !="null" && dto.jcTime !=undefined && dto.jcTime !=""
			) {
				
				return moment(dto.jcTime).format("MMDD HH:mm");
			
			} else {
				return "";
			}
	});
	
	self.jcTime1 = ko.computed(function() {
		if (self.jcTime()==''||self.jcTime()=='Invalid date'||self.jcTime()==null) {
			return "";
			
		} else {
			
			return moment(dto.jcTime).format("MMDD HH:mm");
		}
	});
	
/*	self.jrTime = ko.computed(function() {
		if (dto.jrTime !=null && dto.jrTime !="null"&&dto.jrTime !=undefined) {
			return moment(dto.jrTime).format("MMDD HH:mm");
			
		} else {
			return "";
		}
	});
	self.jcStn = ko.observable(dto.jcStn);
	self.jcTime = ko.computed(function() {
		if (
			
			dto.jcTime !=null && dto.jcTime !="null"&&dto.jcTime !=undefined
		) {
			
			return moment(dto.jcTime).format("MMDD HH:mm");
		
		} else {
			return "";
		}
	});*/

	self.sourceType = ko.observable(dto.sourceType);
	self.dailyLineFlag = ko.observable(dto.dailyLineFlag);
	self.dailyLineTime = ko.observable(dto.dailyLineTime);
	self.checkLev1 = ko.observable(dto.checkLev1);
	self.checkLev2 = ko.observable(dto.checkLev2);
	self.highLineFlag = ko.observable(dto.highLineFlag);
	self.dailyLineId = ko.observable(dto.dailyLineId);
	self.lev1Checked = ko.observable(dto.lev1Checked);
	self.lev2Checked = ko.observable(dto.lev2Checked);
	self.trainType = ko.observable(dto.trainType);
	self.isSelected = ko.observable(false);
	self.spareFlag = ko.observable(dto.spareFlag);
	// 显示审核1，条件上图
	self.needLev1Con = ko.observable(dto.dailyLineFlag); // 0：不可点 1可点
	// 显示审核2，条件审核1已审
	self.needLev2Con = ko.observable(dto.lev1Checked);// 0：不可点 1可点
	// 校验项 0：未校验，1：匹配，-1：不匹配
	self.vaildStatus = ko.observable(dto.vaildStatus);
	self.isTrainInfoMatch = ko.observable(dto.vaildStatus);
	self.isTimeTableMatch = ko.observable(dto.vaildStatus);
	self.isRoutingMatch = ko.observable(0);

	// computed
	self.isTrainInfoMatchClass = ko.computed(function() {
		var className = "btn-default";
		switch (self.isTrainInfoMatch()) {
		case 0:
			className = "btn-warning";
			break;
		case 1:
			className = "btn-info";
			break;
		case -1:
			className = "btn-danger";
			break;
		default:
		}
		return className;
	});

	self.isTrainInfoMatchText = ko.computed(function() {
		var txt = "未知";
		switch (self.isTrainInfoMatch()) {
		case 0:
			txt = "未校验";
			break;
		case 1:
			txt = "匹配";
			break;
		case -1:
			txt = "不匹配";
			break;
		default:
		}
		return txt;
	});

	self.isTimeTableMatchClass = ko.computed(function() {
		var className = "btn-default";
		switch (self.isTimeTableMatch()) {
		case 0:
			className = "btn-warning";
			break;
		case 1:
			className = "btn-info";
			break;
		case -1:
			className = "btn-danger";
			break;
		default:
		}
		return className;
	});

	self.isTimeTableMatchText = ko.computed(function() {
		var txt = "未知";
		switch (self.isTimeTableMatch()) {
		case 0:
			txt = "未校验";
			break;
		case 1:
			txt = "匹配";
			break;
		case -1:
			txt = "不匹配";
			break;
		default:
		}
		return txt;
	});

	self.isRoutingMatchClass = ko.computed(function() {
		var className = "btn-default";
		switch (self.isRoutingMatch()) {
		case 0:
			className = "btn-warning";
			break;
		case 1:
			className = "btn-info";
			break;
		case -1:
			className = "btn-danger";
			break;
		default:
		}
		return className;
	});

	self.isRoutingMatchText = ko.computed(function() {
		var txt = "未知";
		switch (self.isRoutingMatch()) {
		case 0:
			txt = "未校验";
			break;
		case 1:
			txt = "匹配";
			break;
		case -1:
			txt = "不匹配";
			break;
		default:
		}
		return txt;
	});

	self.needLev1 = ko.computed(function() {
//		return self.lev1Checked() == 0 && self.needLev1Con() == 1;
		return self.needLev1Con() == 1;
	});

	self.needLev2 = ko.computed(function() {
		return self.dailyLineFlag() == 1 && self.lev1Checked() == 1
				&& self.lev2Checked() == 0;
	});

	self.lev1Status = ko.computed(function() {
//		注释原因:lev1Checked更换为:dailyplan_check_bureau
		if (self.lev1Checked() == 0) {
//			return "<i class=\"fa fa-times-circle text-danger\"></i>未";
			return "<span class=\"label label-danger\">未</span>";
		} else {
			return "<span class=\"label label-success\">已</span>";
		}
	});

	// computed
	self.lev2StatusClass = ko.computed(function() {
		var className = "";
		switch (self.lev2Checked()) {
		case 0:
			className = "label label-danger";
			break;
		case 1:
			className = "label label-success";
			break;
		case 2:
			className = "label label-adjust";
			break;
		case 3:
			className = "label label-danger";
			break;
		case 4:
			className = "label label-danger";
			break;
		default:
		}
		return className;
	});
	
	self.lev2Status = ko.computed(function() {
//		if (self.lev2Checked() == 0) {
//		return "<span class='label label-danger labelClick' title='点击查看落成记录'>未</span>";
//	} else if (self.lev2Checked() == 1) {
//		return "<span class='label label-success labelClick' title='点击查看落成记录'>已</span>";
//	} else if (self.lev2Checked() == 2) {
//		return "<span class='label label-adjust labelClick' title='点击查看落成记录'>调整</span>";
//	}
	var txt = "";
	switch (self.lev2Checked()) {
	case 0:
		txt = "未";
		break;
	case 1:
		txt = "已";
		break;
	case 2:
		txt = "调整";
		break;
	case 3:
		txt = "匹配失败";
		break;
	case 4:
		txt = "落成失败";
		break;
	default:
	}
	return txt;

	
});

	self.dailyLineFlagView = ko
			.computed(function() {
				if (self.dailyLineFlag() == 1) {
					return "<span class=\"label label-success\">已</span>";
				} else if (self.dailyLineFlag() == 0) {
					return "<span class=\"label label-danger\">未</span>";
				} else {
					return "<span class=\"label label-default\">未知</span>";
				}
			});

	self._default = {
		autoOpen : false,
		height : $(window).height() / 2,
		width : 800,
		title : "",
		position : [ ($(window).width() - 800), 0 ],
		maxWidth : $(window).width(),
		maxHeight : $(window).height(),
		model : true
	};

	self._getDialog = function(page, options) {
		var _default = {
			autoOpen : options.autoOpen || self._default.autoOpen,
			height : options.height || self._default.height,
			width : options.width || self._default.width,
			title : options.title || self._default.title,
			position : options.position || self._default.position,
			maxWidth : self._default.maxWidth,
			maxHeight : self._default.maxHeight,
			closeText : "关闭",
			model : true,
			close : options.close || null
		};
		var $dialog = $('<div class="dialog" style="overflow: hidden"></div>')
				.html(
						'<iframe src="'
								+ page
								+ '" width="100%" height="100%" marginWidth=0 frameSpacing=0 marginHeight=0 scrolling=auto frameborder="0px"></iframe>')
				.dialog(_default);
		return $dialog;
	};

	// actions
	self.showInfoComparePanel = function() {
		if(self.dailyLineId()==null){
			showWarningDialog("一条线状态未生成！");
			return;
		}
		
		if($('#kyvsyx').is(":hidden")){  
			$("#kyvsyx").find("iframe").attr("src", "audit/compare/traininfo/plan/" + self.id() + "/line/"
					+ self.dailyLineId());
			$('#kyvsyx').dialog({ title: "客运计划列车信息 vs 运行线列车信息", autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#kyvsyx").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#kyvsyx').height();
					var WW = $('#kyvsyx').width();
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
		}
		
//		var url = "audit/compare/traininfo/plan/" + self.id() + "/line/"
//				+ self.dailyLineId();
//		self._getDialog(url, {
//			title : "客运计划列车信息 vs 运行线列车信息",
//			width : 850
//		}).dialog("open");
	}

	self.showTimeTableComparePanel = function() {
		if(self.dailyLineId()==null){
			showWarningDialog("一条线状态未生成！");
			return;
		}
		
		if($('#kyskvsyxsk').is(":hidden")){  
			$("#kyskvsyxsk").find("iframe").attr("src", "audit/compare/timetable/"
					+ $("#bureau option:selected").val() + "/plan/" + self.id()
					+ "/line/" + self.dailyLineId());
			$('#kyskvsyxsk').dialog({ title: "客运计划列车时刻表 vs 运行线列车时刻表", autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#kyskvsyxsk").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#kyskvsyxsk').height();
					var WW = $('#kyskvsyxsk').width();
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
		}
		
//		var url = "audit/compare/timetable/"
//				+ $("#bureau option:selected").val() + "/plan/" + self.id()
//				+ "/line/" + self.dailyLineId();
//		self._getDialog(url, {
//			title : "客运计划列车时刻表 vs 运行线列车时刻表",
//			height : $(window).height(),
//			width : 1300
//		}).dialog("open");
	}

	self.sents = ko.observableArray();
	/**
	 * 落成记录.
	 */
	self.completionLog = function() {
		// 当前登录局
		//alert(111111);
		var currentUserBureauName = currentUserBureauName1;
		//alert(currentUserBureauName);
		if (null == currentUserBureauName) {
			showWarningDialog("获取不到路局");
			return;
		}
		if (self.dailyLineId() == null) {
			showWarningDialog("一条线状态未生成！");
			return;
		}
		if(self.id() == null){
			showWarningDialog("请选择记录");
			return;
		}
		self.sents.removeAll();
		$('#completion_log').dialog("open");
		$.ajax({
			url : basePath + "/audit/plan/completionLog",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				sent_people_bureau : currentUserBureauName,
				plan_train_Id : self.id()
			}),
			success : function(result) {
				if (result != null && result != "undefind") {
					$.each(result.data, function(n, z) {
//						self.sents.push(new PlanSentRow(z));
						app.sents.push(new PlanSentRow(z));
					});
					
					// 是否出现右边17
					var tableH = $("#tan_completion table").height();
					var boxH = $("#tan_completion").height();
					if (tableH <= boxH) {
						// alert("没");
						$(".td_17").removeClass("display");
					} else {
						// alert("有");
						$(".td_17").addClass("display");
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
	};
	
	self.showGraphic = function() {
		$(this)
				.parents("tr")
				.after(
						"<tr><td colspan='15'><iframe src='' width='100%' height='100%' marginwidth='0' marginheight='0' scrolling='auto' frameborder='0px'></iframe></td></tr>")
	}
}

function TrainCf(data) {
	var self = this;
	
	self.trainNbr = ko.observable(data.trainNbr);
	self.startTime = ko.observable(data.startTime);
	self.startStn = ko.observable(data.startStn);
	self.endStn = ko.observable(data.endStn);
	
}

function getHintTitle(reqLength, respLength) {
	if (reqLength == respLength) {
		return "审核成功";
	} else if (reqLength > respLength) {
		return "审核部分成功";
	} else {
		return "审核出错";
	}
}

function getHintCss(reqLength, respLength) {
	if (reqLength == respLength) {
		return "growl-success";
	} else if (reqLength > respLength) {
		return "growl-warning";
	} else {
		return "growl-danger";
	}
}

function drawPie($div, chartName, data, colors) {
	$div.highcharts({
		chart : {
			plotBackgroundColor : null,
			plotBorderWidth : null,
			plotShadow : false
		},
		title : {
			text : chartName
		// y: 100
		},
		tooltip : {
			pointFormat : '{series.name}: <b>{point.y}</b>',
			percentageDecimals : 1
		},
		// legend: {
		// layout: 'vertical',
		// align: 'right',
		// verticalAlign: 'top',
		// x: 0,
		// y: 150,
		// labelFormatter: function() {
		// return this.name+'&nbsp';
		// },
		// useHTML:true
		// },
		plotOptions : {
			pie : {
				allowPointSelect : true,
				cursor : 'pointer',
				dataLabels : {
					enabled : false
				},
				showInLegend : true
			}
		},
		colors : colors,
		series : [ {
			type : 'pie',
			name : '列车数量',
			data : data
		} ]
	});
}

/**
 * 落成回调.
 * 
 * @param message
 */
function hdCompletion(message){
	app.updCompletionDWR($.parseJSON(message));
}

function hdCompletionVaild(message){
	app.updCompletionVaildDWR($.parseJSON(message));
}