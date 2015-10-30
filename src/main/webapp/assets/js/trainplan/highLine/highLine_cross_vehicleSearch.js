var canvasData = {};  
var cross = null;
$(function() { 
	heightAuto();
	cross = new CrossModel();
	ko.applyBindings(cross); 
	
	cross.init();   
});

function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#div_crossDetailInfo_trainStnTable").css("height",270+"px");
	$("#canvas_parent_div").css("height",203+"px");
	$("#left_height").css("height",WH-360-65+"px");
};

var highlingFlags = [{"value": "0", "text": "普速"},{"value": "1", "text": "高铁"},{"value": "2", "text": "混合"}];
var checkFlags = [{"value": "1", "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "0", "text": "未"}, {"value": "1", "text": "已"},{"value": "2", "text": "全"}];
var highlingrules = [{"value": "1", "text": "平日"},{"value": "2", "text": "周末"},{"value": "3", "text": "高峰"}];
var commonlinerules = [{"value": "1", "text": "每日"},{"value": "2", "text": "隔日"}];


var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";
//判断是否包含对某局的炒作权限
function hasActiveRole(bureau){
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
 * 抽象的模型，可以包含任意的列车，他的所有基本信息都是由他所包含的列车决定
 * @returns
 */
function HighLineCrossModle(data){
	var self = this;
	//当前对象包含的列车
	self.trains = ko.observableArray();  
	//加载列车到列车数组中
	self.loadTrains = function(trains){
		$.each(trains, function(i, n){
			var train = new TrainRow(n);
			train.loadTimes(n.highlineTrainTimeList);
			self.trains.push(train);
			
		});
	};
	//添加列车到当前数组的末尾
	self.addTrain = function(train){
		train.trainSort(self.trains().length);
		self.trains.push(train);
	};
	//当前交路的第一个列车的起始日期，取第一列车的发车时间 
	self.crossStartDate = ko.computed(function(){  
		return self.trains().length > 0 ? self.trains()[0].times()[0].targetTime : ""; 
	});;
	//结束日期（该日历交路最后一个车次的终到日期）
	self.crossEndDate = ko.computed(function(){  
		return self.trains().length > 0 ? self.trains()[self.trains().length - 1].times()[self.trains()[self.trains().length - 1].times().length - 1].sourceTime : ""; 
	});
	
	//获取
	self.crossStartStn = ko.computed(function(){  
		return self.trains().length > 0 ? self.trains()[0].times()[0].stnName : ""; 
	});;
	//结束日期（该日历交路最后一个车次的终到日期）
	self.crossEndStn = ko.computed(function(){  
		return self.trains().length > 0 ? self.trains()[self.trains().length - 1].times()[self.trains()[self.trains().length - 1].times().length - 1].stnName : ""; 
	});
	 
	 
	//备用及停运标记（1:开行;2:备用;0:停运）
	self.spareFlag = 1;
	//相关局（局码）
	self.relevantBureau = ko.computed(function(){ 
		var result = ""; 
		var trains = self.trains(); 
		for(var j = 0; j < trains.length; j++){ 
			if(trains[j].passBureau != null){
				for(var i = 0; i < trains[j].passBureau.length; i++){
					if(result.indexOf(trains[j].passBureau.substring(i, i + 1)) > -1){
						continue;
					}else{
						result += trains[j].passBureau.substring(i, i + 1);
					}
				} 
			}
			
		};
		return result; 
	});
	
	self.tokenVehBureau = ko.observable(data.tokenVehBureau); 
	//担当车辆段/动车段
	self.tokenVehDept = ko.observable(data.tokenVehDept); 
	//担当动车所（用于高铁）
	self.tokenVehDepot = ko.observable(data.tokenVehDepot); 
	//客运担当局（局码）
	self.tokenPsgBureau = ko.observable(data.tokenPsgBureau); 
	//担当客运段
	self.tokenPsgDept = ko.observable(data.tokenPsgDept); 
	//动车组车型（用于高铁）
	self.crhType = ko.observable(data.crhType); 
	
	self.postName = ko.observable(data.postName); 
	
	self.postId = ko.observable(data.postId); 
	
	self.throughLine = ko.observable(data.throughLine); 

	//动车组车组号1（用于高铁）
	self.vehicle1 =  ko.observable();
	//动车组车组号2（用于高铁）
	self.vehicle2 =  ko.observable();
	//crossName是由所有的列车编号 加上“-”组成的
	self.crossName = ko.computed(function(){ 
		var result = ""; 
		var trains = self.trains();
		for(var j = 0; j < trains.length; j++){
			result += result == "" ? trains[j].trainNbr : "-" + trains[j].trainNbr;
		};
		return result; 
	});
	
	self.createReason = ko.observable();
	self.note = ko.observable(data.note);
}

/**
 * 实际暴露给用户的model对象,knockout的最外层
 */
function CrossModel() {
	var self = this;
	//列车列表 用于左下角的显示使用
	self.trains = ko.observableArray();
	//经由站列表左下角的显示使用
	self.stns = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0);
	
	self.trainPlans = ko.observableArray();
	
	self.currentPlanCrossId = ko.observable("");//当前选择交路planid 用于车底交路图tab
	
	self.planDays = ko.observableArray(); 
	
	self.gloabBureaus = [];  
	
	self.times = ko.observableArray();
	
	self.simpleTimes = ko.observableArray();
	
	self.runPlanCanvasPage = new RunPlanCanvasPage(self);
	
	self.currentTrainInfoMessage = ko.observable("");
	
	self.currentTrain = ko.observable();  
	//中间多选列表的对象
	self.acvtiveHighLineCrosses = ko.observableArray();  
	//组合拆解功能 从左边交路中拉过来未处理的
	self.oldHighLineCrosses = ko.observableArray();  
	//担当局
	self.searchModle = ko.observable(new searchModle());
	//当前日期可调整的交路
	self.highLineCrossRows =  ko.observableArray(); 
	//左边交路选中的记录列表
	self.selectedHighLineCrossRows = ko.observableArray(); 
	//中间的列表选中的记录列表
	self.selectedActiveHighLineCrossRows = ko.observableArray(); 
	//车次组合后的时刻点单，用于显示
	self.activeTimes = ko.observableArray();  
	
	//上移事件
	self.activeCrossUp = function(){
		var currentCorss = self.selectedActiveHighLineCrossRows()[0]; 
		var acvtiveHighLineCrosses = self.acvtiveHighLineCrosses();
		for(var i = 0 ; i < acvtiveHighLineCrosses.length; i++){
			//如果当前的交路和选中的交路，就把他和他前面的交路交换
			if(acvtiveHighLineCrosses[i] == currentCorss){ 
				if(i > 0){ 
					var temp = acvtiveHighLineCrosses[i - 1];
					self.acvtiveHighLineCrosses.splice(i - 1, 2, currentCorss, temp);  
				}
				break;
			}
		}
	}; 
	//下移事件
	self.activeCrossDown = function(){
		//当前选中的对象
		var currentCorss = self.selectedActiveHighLineCrossRows()[0]; 
		//调整列表的中的所有交路
		var acvtiveHighLineCrosses = self.acvtiveHighLineCrosses();
		for(var i = 0 ; i < acvtiveHighLineCrosses.length; i++){
			//如果当前的交路和选中的交路，就把他和他后面的交路交换
			if(acvtiveHighLineCrosses[i] == currentCorss){ 
				if(i < acvtiveHighLineCrosses.length - 1){  
					var temp = acvtiveHighLineCrosses[i + 1];
					self.acvtiveHighLineCrosses.splice(i , 2, temp, currentCorss);   
				}
				break;
			}
		}
	};
	//当重新选择了列车以后触发，重新把选中的交路的列车中的时刻加载出来
	self.selectedActiveHighLineCrossChange = function(){  
		self.activeTimes.remove(function(item){
			return true;
		}); 
		$.each(self.selectedActiveHighLineCrossRows(), function(i, n){
			$.each(n.trains(), function(a, t){
				$.each(t.times(), function(z, time){
					self.activeTimes.push(time);
				});
			});
		});
	};
	
	//点击单条记录前面的单选框
	self.selectedCrosse = function(){ 
		 var currentCorss = self.selectedHighLineCrossRows()[0]; 
		 //做恢复使用
		 self.oldHighLineCrosses.push(currentCorss);
		 commonJsScreenLock();
		 $.ajax({
				url : basePath+"/highLine/getHighlineTrainTimeForHighlineCrossId",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({highlineCrossId: currentCorss.highLineCrossId()}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {  
						 var cross = new HighLineCrossModle($.parseJSON(ko.toJSON(currentCorss))); 
						 cross.loadTrains(result.data);
						 self.highLineCrossRows.remove(currentCorss);
						 self.selectedHighLineCrossRows.remove(currentCorss);
						 self.acvtiveHighLineCrosses.push(cross); 
					} else {
						showErrorDialog("没有加载的交路数据");
					} 
				},
				error : function() {
					showErrorDialog("加载的交路数据失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
		    });   
	};
	//拆解交路
	self.cjHighLineCross = function(){
		$.each(self.selectedActiveHighLineCrossRows(), function(i, n){
			$.each(n.trains(), function(a, t){ 
				var cross = new HighLineCrossModle($.parseJSON(ko.toJSON(n))); 
				 cross.addTrain(t);
				self.acvtiveHighLineCrosses.push(cross);  
			}); 
			self.selectedActiveHighLineCrossRows.remove(n);
			self.acvtiveHighLineCrosses.remove(n);
		});
	};
	//全选按钮
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.highLineCrossRows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0); 
			}else{ 
				crossRow.selected(1);   
			}  
		});  
	};
	//数据行前面的checkbox点击事件
	self.selectCross = function(row){ 
//		self.crossAllcheckBox();  
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);   
			$.each(self.highLineCrossRows(), function(i, crossRow){  
				//如果可操作并且该记录被选中，表示没有全部选中
				if(crossRow.selected() != 1 && crossRow != row){
					self.crossAllcheckBox(0);
					return false;
				}  
			}); 
		}else{ 
			self.crossAllcheckBox(0); 
		} 
	}; 
	self.hbHighLineCrossConfirm = function(){ 
		var firstHighLineCross = self.selectedActiveHighLineCrossRows()[0]; 
		 
		//默认设置为第一个交路的基本信息
		self.searchModle().tokenVehDepot(firstHighLineCross.tokenVehDepot());
		self.searchModle().acc(firstHighLineCross.postId());
		self.searchModle().tokenPsgBureau(firstHighLineCross.tokenPsgBureau());
		self.searchModle().tokenPsgDept(firstHighLineCross.tokenPsgDept());
		self.searchModle().crhType(firstHighLineCross.crhType()); 
		self.searchModle().throughLine(firstHighLineCross.throughLine());
		self.searchModle().createReason(firstHighLineCross.createReason());
		$("#hb_highLine_cross").dialog("open");
	};
	
	self.hbHighLineCrossCancel = function(){
		$("#hb_highLine_cross").dialog("close");
	};
	//合并交路
	self.hbHighLineCrossYes = function(){ 
		var selectedActiveHighLineCrossRows = self.selectedActiveHighLineCrossRows();  
		 
		var cross = new HighLineCrossModle($.parseJSON(ko.toJSON(selectedActiveHighLineCrossRows[0])));  
		if(self.searchModle().tokenVehDepot() == null){
			showWarningDialog("请选择动车所");
			return;
		}else if(self.searchModle().acc() == null){
			showWarningDialog("请选择动车台");
			return;
		}else if(self.searchModle().tokenPsgBureau() == null){
			showWarningDialog("请选择担当所");
			return;
		}else if(self.searchModle().tokenPsgDept() == null){
			showWarningDialog("请填写客运担当段");
			return;
		}else if(self.searchModle().crhType() == null){
			showWarningDialog("请选择动车组型");
			return;
		}else if(self.searchModle().throughLine() == null){
			showWarningDialog("请选择铁路线类型");
			return;
		}else if(self.searchModle().createReason() == null){
			showWarningDialog("请填写来源");
			return;
		}
		
		//担当动车所（用于高铁）
		cross.tokenVehDepot(self.searchModle().tokenVehDepot()); 
		$.each(self.searchModle().accs(),function(i, n){
			if(n.code == self.searchModle().acc()){
				cross.postName(n.name);
				return false;
			}
		});
		cross.postId(self.searchModle().acc()); 
		//客运担当局（局码）
		cross.tokenPsgBureau(self.searchModle().tokenPsgBureau());
		//担当客运段
		cross.tokenPsgDept(self.searchModle().tokenPsgDept());
		//动车组车型（用于高铁）
		cross.crhType(self.searchModle().crhType());
		cross.throughLine(self.searchModle().throughLine());  
		 
		$("#hb_highLine_cross").dialog("close");
		for(var i = 0; i < selectedActiveHighLineCrossRows.length; i++){
			var cr = selectedActiveHighLineCrossRows[i];
			if(i > 0){
				var pre = selectedActiveHighLineCrossRows[i - 1]; 
				if(!timeCompare(pre.crossEndDate(), cr.crossStartDate())){
					showConfirmDiv("提示", "车次" + cr.crossName() + "无法与" + pre.crossName() + "前车接续,时间间隔允许", function(r){
						return;
					});
					return;
				}else if(pre.crossEndStn() != cr.crossStartStn()){
					showConfirmDiv("提示", "车次" + cr.crossName() + "无法与" + pre.crossName() + "前车接续,接续车站不一致", function(r){
						return;
					});
					return;
				};  
			} 
			var trains = cr.trains();  
			for(var j = 0; j < trains.length; j++ ){  
				cross.addTrain(trains[j]);
			};   
			
		} 
		while(selectedActiveHighLineCrossRows.length > 0){
			self.acvtiveHighLineCrosses.remove(selectedActiveHighLineCrossRows[0]); 
			self.selectedActiveHighLineCrossRows.remove(selectedActiveHighLineCrossRows[0]); 
		}
		 
		self.acvtiveHighLineCrosses.push(cross); 
	};
	
	//保存交路合并结果
	self.submitHighLineCross = function(){ 
		var activeCrosses = self.acvtiveHighLineCrosses();
		var crosses = [];
		for(var i = 0; i < activeCrosses.length; i++){  
			var postParam = $.parseJSON(ko.toJSON(activeCrosses[i])); 
			$.each(postParam.trains , function(i, n){
				delete(n.times);
				delete(n.simpleTimes);
			});
			crosses.push(postParam);
		}
		var oldCrosses = self.oldHighLineCrosses();
		var oldCrossIds = "";
		for(var i = 0; i < oldCrosses.length; i++){ 
			oldCrossIds += (oldCrossIds == "" ? "'"  : ",'")  + oldCrosses[i].highLineCrossId() + "'";
		}
		$.ajax({
				url : basePath+"/highLine/saveHighlineCrossAndTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({"highLineCrossIds": oldCrossIds, "newCrosses" : crosses}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						self.loadCrosses();
						self.acvtiveHighLineCrosses.remove(function(item){
							return true;
						});
						showSuccessDialog("交路调整成功"); 
					} else {
						showErrorDialog("交路调整失败");
					} 
				},
				error : function() {
					showErrorDialog("交路调整失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
		    });  
		
	};
	//当填报第一个列车车底的时候触发
	self.vehicle1Change = function(row){
		row.updateFlag(true);
		var highLineCrossRows = self.highLineCrossRows();
		if(row.vehicle1() != null && row.vehicle1() != ""){
			for(var i = 0; i < highLineCrossRows.length; i++){
				if(highLineCrossRows[i] != row && highLineCrossRows[i].vehicle1() == row.vehicle1()){
					showConfirmDiv("提示", "当前车底被交路：" + highLineCrossRows[i].crossName() + "使用是否要重新绑定到当前交路?", function (r) { 
				        if (r) { 
				        	highLineCrossRows[i].vehicle1("");
				        }else{
				        	row.vehicle1("");
				        }
					});
				}else if(highLineCrossRows[i].vehicle2() == row.vehicle1()){
					showConfirmDiv("提示", "当前车底被交路：" + highLineCrossRows[i].crossName() + "使用是否要重新绑定到当前交路?", function (r) { 
				        if (r) { 
				        	highLineCrossRows[i].vehicle2("");
				        }else{
				        	row.vehicle1("");
				        }
					});
				}
			} 
		}
	};
	
	//当填报第二个列车车底的时候触发
	self.vehicle2Change = function(row){ 
		row.updateFlag(true);
		var highLineCrossRows = self.highLineCrossRows();
		if(row.vehicle2() != null && row.vehicle2() != ""){
			for(var i = 0; i < highLineCrossRows.length; i++){
				if(highLineCrossRows[i].vehicle1() == row.vehicle2()){
					showConfirmDiv("提示", "当前车底被交路：" + highLineCrossRows[i].crossName() + "使用是否要重新绑定到当前交路?", function (r) { 
				        if (r) { 
				        	highLineCrossRows[i].vehicle1(""); 
				        	highLineCrossRows[i].updateFlag(true);
				        }else{
				        	row.vehicle2("");
				        	row.updateFlag(false);
				        }
					});
					break;
				}else if(highLineCrossRows[i] != row && highLineCrossRows[i].vehicle2() == row.vehicle2()){
					showConfirmDiv("提示", "当前车底被交路：" + highLineCrossRows[i].crossName() + "使用是否要重新绑定到当前交路?", function (r) { 
				        if (r) { 
				        	highLineCrossRows[i].vehicle2("");
				        	highLineCrossRows[i].updateFlag(true);
				        }else{
				        	row.vehicle2("");
				        	row.updateFlag(false);
				        }
					});
					break;
				}
			}
	   }
		
	};
	 
	self.updateHighLineCrosses = function(){
		var highLineCrossRows = self.highLineCrossRows();
		updateCrossRows = [];
		for(var i = 0; i < highLineCrossRows.length; i++){
			if(highLineCrossRows[i].updateFlag()){
				updateCrossRows.push({"highLineCrossId": highLineCrossRows[i].highLineCrossId(), "vehicle1":  highLineCrossRows[i].vehicle1(), "vehicle2":  highLineCrossRows[i].vehicle2()});
			}
		} 
		 $.ajax({
				url : basePath+"/highLine/updateHighLineVehicle",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({"highLineCrosses": updateCrossRows}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						showSuccessDialog("提交交动车交路计划成功"); 
					} else {
						showErrorDialog("获取运行规律失败");
					} 
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
		    });  
	};
	
	self.trainRunPlanChange = function(row, event){ 
//		console.log(row);
//		console.log(event.target.name);
//		console.log("trainRunPlanChange test");
	};
	
	self.dragRunPlan = function(n,event){
		$(event.target).dialog("open"); 
	}; 
	
	self.loadStns = function(currentTrain){  
		self.times.remove(function(item){
			return true;
		});
		self.simpleTimes.remove(function(item){
			return true;
		});
		commonJsScreenLock();
		
		 $.ajax({ 
			 	url : "../jbtcx/queryPlanLineTrainTimesNewIframe?trainId="+currentTrain.obj.planTrainId,
				cache : false,
				type : "GET",
				success : function(result) {  
					var message = "车次：" + currentTrain.obj.trainName + "&nbsp;&nbsp;&nbsp;";
					self.currentTrainInfoMessage(message);
					$("#train_time_dlg").find("iframe").attr("src", "../jbtcx/queryPlanLineTrainTimesNewIframe?trainId=" + currentTrain.obj.planTrainId);
					$('#train_time_dlg').dialog({ title: "时刻表:" +self.currentTrainInfoMessage(), autoOpen: true, modal: false, draggable: true, resizable:true,
						onResize:function() {
							var iframeBox = $("#train_time_dlg").find("iframe");
							var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
							var WH = $('#train_time_dlg').height();
							var WW = $('#train_time_dlg').width();
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
					/*
					if (result != null && result != "undefind" && result.code == "0") { 
							var message = "车次：" + currentTrain.obj.trainName + "&nbsp;&nbsp;&nbsp;";
							
							$.each(result.data, function(i, n){
								var timeRow = new TrainTimeRow(n); 
								self.times.push(timeRow); 
								if(n.stationFlag != 'BTZ'){
									self.simpleTimes.push(timeRow); 
								}
								if(i == 0){
									message += n.stnName;
								}else if(i == result.data.length - 1){
									self.currentTrainInfoMessage(message + "——" + n.stnName);
									if($("#run_plan_train_times").is(":hidden")){
										$("#run_plan_train_times").dialog({top:150, draggable: true, resizable:true, onResize:function() {
											var simpleTimes_table = $("#simpleTimes_table");
											var allTimes_table = $("#allTimes_table");
											var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
											var WH = $('#run_plan_train_times').height() - 100;
											var WW = $('#run_plan_train_times').width();
								            if (isChrome) {
								            	simpleTimes_table.css({ "height": (WH) + "px"});
								            	simpleTimes_table.css({ "min-height": (WH) + "px"});
								            	simpleTimes_table.attr("width", (WW));
								            	
								            	allTimes_table.css({ "height": (WH) + "px"});
								            	allTimes_table.css({ "min-height": (WH) + "px"});
								            	allTimes_table.attr("width", (WW));

								            }else{
								            	simpleTimes_table.css({ "height": (WH)  + "px"});
								            	simpleTimes_table.css({ "min-height": (WH) + "px"});
								            	simpleTimes_table.attr("width", (WW));
								            	
								            	allTimes_table.css({ "height": (WH) + "px"});
								            	allTimes_table.css({ "min-height": (WH) + "px"});
								            	allTimes_table.attr("width", (WW));
								            }
										}});
									} 
								}  
							}); 
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					}
					*/
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){ 
						commonJsScreenUnLock();  
				}
		    }); 
		   
		// $("#run_plan_train_times").dialog("open");
	};
	self.setCurrentTrain = function(train){ 
		
		self.currentTrain(train); 
	};
	
	self.setCurrentCross = function(cross){ 
		self.currentCross(cross); 
		self.currentPlanCrossId = cross.planCrossId();
	};
	
	
	
	//全选按钮
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.highLineCrossRows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0); 
			}else{ 
				crossRow.selected(1);   
			}  
		});  
	};
	//数据行前面的checkbox点击事件
	self.selectCross = function(row){ 
//		self.crossAllcheckBox();  
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);   
			$.each(self.highLineCrossRows(), function(i, crossRow){  
				//如果可操作并且该记录被选中，表示没有全部选中
				if(crossRow.selected() != 1 && crossRow != row){
					self.crossAllcheckBox(0);
					return false;
				}  
			}); 
		}else{ 
			self.crossAllcheckBox(0); 
		} 
	}; 
	
	
	// cross基础信息中的下拉列表  
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {  
			self.tokenVehBureaus.push(new BureausRow(bureaus[i])); 
			if(bureaus[i].code == self.tokenVehBureau()){
				self.tokenVehBureau(bureaus[i]);
				break;
			}
		} 
	}; 
	 
	self.defualtCross = {"crossId":"",
		"crossName":"", 
		"chartId":"",
		"chartName":"",
		"crossStartDate":"",
		"crossEndDate":"",
		"crossSpareName":"",
		"alterNateDate":"",
		"alterNateTranNbr":"",
		"spareFlag":"",
		"cutOld":"",
		"groupTotalNbr":"",
		"pairNbr":"",
		"highlineFlag":"",
		"highlineRule":"",
		"commonlineRule":"",
		"appointWeek":"",
		"appointDay":"",
		"crossSection":"",
		"throughline":"",
		"startBureau":"",
		"tokenVehBureau":"",
		"tokenVehDept":"",
		"tokenVehDepot":"",
		"appointPeriod":"",
		"tokenPsgBureau":"",
		"tokenPsgDept":"",
		"tokenPsgDepot":"",
		"locoType":"",
		"crhType":"",
		"elecSupply":"",
		"dejCollect":"",
		"airCondition":"",
		"note":"", 
		"createPeople":"", 
		"createPeopleOrg":"",  
		"createTime":""};
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow(self.defualtCross)); 
	 
	//currentIndex 
	self.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year+"-"+month+"-"+days;
	};
	
	self.dayHeader =function(d){ 
	 
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return month + "-"+ days;
	};
	
	self.getDateByInt = function(n){
		var d = new Date();
		d.setDate(d.getDate() + n);
		
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};
	
	self.get40Date = function(){
		var d = new Date();
		d.setDate(d.getDate() + 40);
		
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};
	
	
	
	self.init = function(){
		//显示详情
		//$('#div_hightline_planDayDetail').css({height: '570px'});//交路信息表格div高度
		var _height = $(window).height()-$('#div_searchForm').height()-500-150;
		_height = _height<80?200:_height;
		//$('#div_crossDetailInfo').css({height:_height});//设置交路详情div高度
		//$('#div_crossDetailInfo_trainStnTable').css({height: _height+67});//设置交路详情_车次详情表格div高度
		//$('#canvas_parent_div').css({height:_height});//canvas图形div高度
		

		$("#train_time_dlg").dialog("close");
		$("#cmdInfo_dialog").dialog("close");//命令查看窗口div
		$("#run_plan_train_times").dialog("close"); 
		$("#hb_highLine_cross").dialog("close");  
		$("#runplan_input_startDate").datepicker();
		$("#runplan_input_endDate").datepicker();
		
		//注册双击多选列表中事件
		$("#current_highLineCrosses").on("dblclick", self.selectedCrosse);  
		
		$("#active_highLine_cross_dialog").dialog("close"); 
		
//		$("#current_active_highLineCrosses").on("dblclick", self.selectedCrosse);
		//x放大2倍
		$("#canvas_event_btn_x_magnification").click(function(){
			if (currentXScaleCount == 32) {
				showErrorDialog("当前已经不支持继续放大啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图
			
			//计算画布比例及倍数
			currentXScale = currentXScale/2;
			currentXScaleCount = currentXScaleCount*2;

			$("#canvas_event_label_xscale").text(currentXScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
					 xScale : currentXScale,			//x轴缩放比例
					 xScaleCount : currentXScaleCount,	//x轴放大总倍数
					 yScale : currentYScale,		//y轴缩放比例
					 stationTypeArray:self.searchModle().drawFlags()
				 });
			
		});
		
		//x缩小2倍
		$("#canvas_event_btn_x_shrink").click(function(){
			if (currentXScaleCount == 0.25) {
				showErrorDialog("当前已经不支持继续缩小啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图

			//计算画布比例及倍数
			currentXScale = currentXScale*2;
			currentXScaleCount = currentXScaleCount/2;

			$("#canvas_event_label_xscale").text(currentXScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale,			//y轴缩放比例
				 stationTypeArray:self.searchModle().drawFlags()
			 });
		});
		//y放大2倍
		$("#canvas_event_btn_y_magnification").click(function(){
			if (currentYScaleCount == 8) {
				showErrorDialog("当前已经不支持继续放大啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图
			
			//计算画布比例及倍数
			currentYScale = currentYScale/2;
			currentYScaleCount = currentYScaleCount*2;

			$("#canvas_event_label_yscale").text(currentYScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
					 xScale : currentXScale,			//x轴缩放比例
					 xScaleCount : currentXScaleCount,	//x轴放大总倍数
					 yScale : currentYScale,			//y轴缩放比例
					 stationTypeArray:self.searchModle().drawFlags()
				 }); 
		});
		
		//y缩小2倍
		$("#canvas_event_btn_y_shrink").click(function(){
			if (currentYScaleCount == 0.25) {
				showErrorDialog("当前已经不支持继续缩小啦！");
				return;
			}
			
			//必须清除
			lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
			jlList = [];	//用于保存交路数据元素，以便重新绘图

			//计算画布比例及倍数
			currentYScale = currentYScale*2;
			currentYScaleCount = currentYScaleCount/2;

			$("#canvas_event_label_yscale").text(currentYScaleCount);
			self.runPlanCanvasPage.clearChart();	//清除画布
			self.runPlanCanvasPage.drawChart({
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale,			//y轴缩放比例
				 stationTypeArray:self.searchModle().drawFlags()
			 });
		}); 
		self.runPlanCanvasPage.initPage();   
		self.searchModle().planStartDate(self.getDateByInt(1)); 
		
		commonJsScreenLock(4); 
		//获取当期系统日期  
	    $.ajax({
			url : basePath+"/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					var bureaus = new Array();
					var curUserBur = currentBureauShortName;
					var fistObj = null;
					if (result.data != null && result.data != undefined) {
						for (var i = 0; i < result.data.length; i++) {
							var obj = result.data[i];
							 if(obj.ljjc==curUserBur){
								 fistObj = obj;
							 }else{
								 bureaus.push(obj);
							 }
						}
						if(null != fistObj){
							bureaus.unshift(fistObj,"");
						}
					}
					self.searchModle().loadBureau(bureaus);
					//self.searchModle().loadBureau(result.data); 
					if (result.data !=null) { 
						$.each(result.data,function(n, bureau){  
							self.gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym});
							gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym});
						});
					} 
				} else {
					showErrorDialog("获取路局列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
	    
	    $.ajax({
			url : basePath+"/highLine/getThroughLines/ALL",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					if (result.data !=null) { 
						self.searchModle().loadThroughLines(result.data); ;
					} 
				} else {
					showErrorDialog("获取路局列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
	    
	    $.ajax({
			url : basePath+"/highLine/getCrhTypes",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					if (result.data !=null) { 
						self.searchModle().loadCrhTypes(result.data); ;
					} 
				} else {
					showErrorDialog("获取路局列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
	    
	    $.ajax({
			url : basePath+"/highLine/getDepots/ALL",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					if (result.data !=null) { 
						self.searchModle().loadTokenVehDepots(result.data); ;
					} 
				} else {
					showErrorDialog("获取路局列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
	    
	    $.ajax({
			url : basePath+"/highLine/getAccs/ALL",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					if (result.data !=null) {
						self.searchModle().loadAccs(result.data);
					}
				} else {
					showErrorDialog("获取路局列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
		
		
	};
	
	
	/**
	 * 清空某天全部交路记录
	 */
	self.deleteAllHighLineCrosses = function(){
		if($("#runplan_input_startDate").val()==null || $("#runplan_input_startDate").val()=="") {
			showWarningDialog("请选择日期");
			return;
		}
		
		commonJsScreenLock();
		$.ajax({
			url : basePath+"/highLine/cleanHighLineForDate",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				startDate: moment($("#runplan_input_startDate").val()).format("YYYYMMDD")
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					//清除下拉框数据
					self.highLineCrossRows.remove(function(item){
						return true;
					});
					showSuccessDialog("成功清除"+$("#runplan_input_startDate").val()+"日交路数据");
				} else {
					showErrorDialog("清除"+$("#runplan_input_startDate").val()+"日交路数据失败");
				}
			},
			error : function() {
				showErrorDialog("清除"+$("#runplan_input_startDate").val()+"日交路数据失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
	    });
	};
	//删除选中的交路记录
	self.deleteHighLineCrosses = function(){
		if (self.selectedHighLineCrossRows().length == 0) {
			showWarningDialog("请选需要删除的交路信息");
			return;
		}
		
		var _deleteIds = "";
		for(var i=0;i<self.selectedHighLineCrossRows().length;i++) {
			_deleteIds = _deleteIds+"'"+self.selectedHighLineCrossRows()[i].highLineCrossId()+"',";
		}
		_deleteIds = _deleteIds.substring(0, _deleteIds.lastIndexOf(","));
		
		commonJsScreenLock();
		$.ajax({
			url : basePath+"/highLine/deleteHighLineForIds",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				higlineCrossids: _deleteIds
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					//清除列表交路数据
					for(var i=0;i<self.selectedHighLineCrossRows().length;i++) {
						self.highLineCrossRows.remove(self.selectedHighLineCrossRows()[i]);
					}
					
					showSuccessDialog("成功删除"+self.selectedHighLineCrossRows().length+"条交路数据");
				} else {
					showErrorDialog("删除交路数据失败");
				}
			},
			error : function() {
				showErrorDialog("删除交路数据失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
	    });
	};
	
	
	/**
	 * 提交按钮点击事件
	 */
	self.submitHighLineWithRole = function() {
		if($("#runplan_input_startDate").val()==null || $("#runplan_input_startDate").val()=="") {
			showWarningDialog("请选择日期");
			return;
		}
		
		showConfirmDiv("提示", "确定要提交" + $("#runplan_input_startDate").val() + "日交路数据吗?", function (r) { 
	        if (r) {
	        	commonJsScreenLock();
	    		$.ajax({
	    			url : basePath+"/highLine/submitHighLineWithRole/CROSS_CHECK",
	    			cache : false,
	    			type : "POST",
	    			dataType : "json",
	    			contentType : "application/json",
	    			data :JSON.stringify({
	    				startDate: moment($("#runplan_input_startDate").val()).format("YYYYMMDD")
	    			}),
	    			success : function(result) {
	    				if (result != null && result != "undefind" && result.code == "0") {
	    					showSuccessDialog("成功提交"+$("#runplan_input_startDate").val()+"日交路数据");
	    				} else {
	    					showErrorDialog("提交"+$("#runplan_input_startDate").val()+"日交路数据失败");
	    				}
	    			},
	    			error : function() {
	    				showErrorDialog("提交"+$("#runplan_input_startDate").val()+"日交路数据失败");
	    			},
	    			complete : function(){
	    				commonJsScreenUnLock();
	    			}
	    	    });
	        }else{
	        	return false;
	        	
	        }
		});
		
	};
	
	
	/**
	 * 生成命令按钮点击事件
	 * 
	 * 实则为查看交路生成命令字符串
	 */
	self.previewCmdInfo = function() {
		if($("#runplan_input_startDate").val()==null || $("#runplan_input_startDate").val()=="") {
			showWarningDialog("请选择日期");
			return;
		}
		
		showConfirmDiv("提示", "确定要生成" + $("#runplan_input_startDate").val() + "日命令数据吗?", function (r) { 
	        if (r) {
	        	commonJsScreenLock();
	    		$.ajax({
	    			url : basePath+"/highLine/previewCmdInfo",
	    			cache : false,
	    			type : "POST",
	    			dataType : "json",
	    			contentType : "application/json",
	    			data :JSON.stringify({
	    				startDate: moment($("#runplan_input_startDate").val()).format("YYYYMMDD")
	    			}),
	    			success : function(result) {
	    				if (result != null && result != "undefind" && result.code == "0") {
	    					showSuccessDialog("生成命令成功");
		    				self.cmdInfoStr(result.data);
	    					if($("#cmdInfo_dialog").is(":hidden")){
								$("#cmdInfo_dialog").dialog({top:130, draggable: true, resizable:true, onResize:function() {
									var cmdInfo_dialog_row1 = $("#cmdInfo_dialog_row1");
									var cmdInfo_dialog_row2 = $("#cmdInfo_dialog_row2");
									var WH = $('#cmdInfo_dialog').height();
									var WW = $('#cmdInfo_dialog').width();

					            	cmdInfo_dialog_row1.attr("width", (WW));
									cmdInfo_dialog_row1.css({ "height": (WH-50) + "px"});
					            	cmdInfo_dialog_row2.attr("width", (WW));
								}});
							}
	    				} else {
	    					showErrorDialog("生成命令失败");
		    				self.cmdInfoStr("");
	    				}
	    			},
	    			error : function() {
	    				showErrorDialog("生成命令失败");
	    				self.cmdInfoStr("");
	    			},
	    			complete : function(){
	    				commonJsScreenUnLock();
	    			}
	    	    });
	        }else{
	        	return false;
	        	
	        }
		});
		
	};
	
	
	self.cmdInfoStr = ko.observable("");//命令字符串
	
	/**
	 * 取消发布命令按钮点击事件
	 */
	self.createCmdInfoCancel = function() {
		$("#cmdInfo_dialog").dialog("close");//命令查看窗口div
	};
	
	/**
	 * 生成命令
	 */
	self.createCmdInfo = function() {
		if(self.cmdInfoStr()==null || self.cmdInfoStr()=="") {
			showWarningDialog("无效命令，不能提交");
			return;
		}
		
    	commonJsScreenLock();
		$.ajax({
			url : basePath+"/highLine/createCmdInfo",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				cmdInfo: $("#textarea_cmdInfoStr").val()//self.cmdInfoStr()
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					showSuccessDialog("生成命令成功");
				} else {
					showErrorDialog("生成命令失败");
				}
			},
			error : function() {
				showErrorDialog("生成命令失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
	    });
		
	};
	
	
	/**
	 * 加载按钮点击事件
	 */
	self.createHighLineCrosses = function(){
		var planStartDate = $("#runplan_input_startDate").val();
		if(planStartDate==null || planStartDate=="") {
			showWarningDialog("请选择日期");
			return;
		}
		showConfirmDiv("提示", "该加载操作将重新生成" + $("#runplan_input_startDate").val() + "日所有交路数据。<br>确定要执行吗?", function (r) { 
	        if (r) {
	        	commonJsScreenLock();
				 $.ajax({
						url : basePath+"/highLine/createHighLineCross",
						cache : false,
						type : "POST",
						dataType : "json",
						contentType : "application/json",
						data :JSON.stringify({  
							startDate : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, '') 
						}),
						success : function(result) {   
							if (result != null && result != "undefind" && result.code == "0") { 
								if(result.data != null && result.data.length > 0){
									$.each(result.data, function(i, n){
										self.highLineCrossRows.push(new CrossRow(n));
									});
								}
								showSuccessDialog("加载图定成功");
							} else {
								showErrorDialog("获取车底交路列表失败");
							};
						},
						error : function() {
							showErrorDialog("获取车底交路列表失败");
						},
						complete : function(){
							commonJsScreenUnLock();
						}
					});
	        }else{
	        	return false;
	        	
	        }
		});
	};
	
	self.loadCrosses = function(){
		self.loadCrosseForPage();
	};
	self.importExcel = function(){
		
		

		var _runDate = $("#runplan_input_startDate").val();//_self.searchModle().runDate();	//运行日期
		var _trainNbr = self.searchModle().trainNbr();	//车次
		
		var searchThroughLine = self.searchModle().searchThroughLine();
		var bureaus =self.searchModle().bureau();
		var searchTokenVehDepot = self.searchModle().searchTokenVehDepot();
		console.dir(self.searchModle().bureau())
		var searchAcc = self.searchModle().searchAcc()
		var _highlineFlag = "1";
		if(bureaus=="undefined" || bureaus=="" || typeof bureaus=="undefined" ) {
			bureaus = "-1";
		}
		
		if(searchThroughLine=="undefined" || searchThroughLine=="" || typeof searchThroughLine=="undefined" ) {
			searchThroughLine = "-1";
		}
		if(searchTokenVehDepot=="undefined" || searchTokenVehDepot=="" || typeof searchTokenVehDepot=="undefined") {
			searchTokenVehDepot = "-1";
		}
		if(searchAcc=="undefined" || searchAcc=="" || typeof searchAcc=="undefined") {
			searchAcc = "-1";
		}
		if(_trainNbr=="undefined" || _trainNbr=="" || typeof _trainNbr=="undefined") {
			_trainNbr = "-1";
		}
		
		if(_runDate == null || typeof _runDate!="string" || _runDate==""){
			showErrorDialog("请选择日期!");
			return;
		}
		window.open(basePath+"/crew/highline/exportExcelcrossserach/1/"+_runDate+"/"+_trainNbr+"/"+_highlineFlag+"/"+searchThroughLine+"/"+searchTokenVehDepot+"/"+searchAcc+"/"+bureaus);
	
		
		
	}
	/**
	 * 列表左下角显示记录总数
	 * 
	 * 临时使用
	 */
	self.currentTotalCount = ko.computed(function(){
		if(self.highLineCrossRows().length >0) {
			return "共"+self.highLineCrossRows().length+"条数据";
		} else {
			return "当前没有可显示的数据";
		}
	});
	
	self.loadCrosseForPage = function(startIndex, endIndex) {  
	 
		commonJsScreenLock();

		var bureauCode = self.searchModle().bureau(); 
		var trainNbr = self.searchModle().trainNbr();
		var searchThroughLine = self.searchModle().searchThroughLine();
		var searchTokenVehDepot = self.searchModle().searchTokenVehDepot();
 
		var planStartDate = $("#runplan_input_startDate").val();
		
		self.highLineCrossRows.remove(function(item) {
			return true;
		});
		$.ajax({
				url : basePath+"/highLine/getHighlineCrossList/ALL",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					tokenVehBureau : bureauCode,//担当局局码
					trainNbr : trainNbr,//车次号
					crossDate : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, ''),//交路日期
					throughLine:searchThroughLine,//铁路线
					tokenVehDepot: searchTokenVehDepot,//动车所
					postName: self.searchModle().searchAcc(),//动车台
					crossBureau:self.searchModle().crossBureau()//计划局
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
//						var rows = [];
						if(result.data != null){
							$.each(result.data,function(n, crossInfo){
//								rows.push(new CrossRow(crossInfo));
								self.highLineCrossRows.push(new CrossRow(crossInfo));
							});
//							self.crossRows.loadPageRows(result.data.totalRecord, rows);
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
						}
						
						 
					} else {
						showErrorDialog("获取车底交路列表失败");
					};
				},
				error : function() {
					showErrorDialog("获取车底交路列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};
	//必须定义在load函数之后
	self.crossRows = new PageModle(50, self.loadCrosseForPage);
	
	self.saveCrossInfo = function() {
		alert(self.currentCross().tokenVehBureau());
	};
	 
	self.showUploadDlg = function(){
		
		$("#file_upload_dlg").dialog("open"); 
	};
	
	self.showActiveHighLineCrossDlg = function(){
		$("#active_highLine_cross_dialog").dialog({top:150, draggable: true, resizable:true, onResize:function() {}}); 
	};
	
	
	
	self.isShowCrossDetailInfo = ko.observable(true);// 显示交路详情复选框   默认勾选
	/**
	 * 显示交路详情复选框点击事件
	 */
	self.showRunPlans = function(){
		var WH = $(window).height();
		
		if(!self.isShowCrossDetailInfo()) {//勾选
			//显示详情
			//$("#div_hightline_planDayDetail").css("height",WH-360+"px");
			$("#left_height").css("height",WH-360-65+"px");
			$("#div_crossDetailInfo_trainStnTable").css("height",270+"px");
			$("#canvas_parent_div").css("height",203+"px");
			$('#div_crossDetailInfo').show();
		} else {//未勾选
			$('#div_crossDetailInfo').hide();
			$("#left_height").css("height",WH-145+"px");
			
		}
		
	    
	};
	
	self.showDialog = function(id, title){
		$('#' + id).dialog({ title:  title, autoOpen: true, height:600,width: 800, modal: false, draggable: true, resizable:true });
	};
	
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	
	//#################################################################
	self.showCrossTrainTimeDlg = function(){ 
		var trainId = self.currentTrainId();  
		if($('#train_time_dlg').is(":hidden")){  
			
			$("#train_time_dlg").find("iframe").attr("src", "jbtcx/queryPlanLineTrainTimesNewIframe?trainId=" + trainId);
			$('#train_time_dlg').dialog({ title: "时刻表:" +self.currentTrainInfoMessage(), autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#train_time_dlg").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#train_time_dlg').height();
					var WW = $('#train_time_dlg').width();
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
	};  
	//#################################################################
	/*
	self.showCrossTrainTimeDlg = function(){
		
		$("#run_plan_train_times").dialog({inline: false, top:150});
	};
	*/
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};
	
	self.showTrainTimes = function(row) {
		self.currentTrain(row);
//		self.runPlanCanvasPage.reDrawByTrainNbr(row.trainNbr);//图形化展示
//		self.stns.remove(function(item){
//			return true;
//		});
//		if(row.times().length > 0){ 
//			$.each(row.times(), function(i, n){
//				self.stns.push(n); 
//			}) ;
//			 
//		}else{
//			$.ajax({
//				url : basePath+"/jbtcx/queryTrainTimes",
//				cache : false,
//				type : "POST",
//				dataType : "json",
//				contentType : "application/json",
//				data :JSON.stringify({   
//					trainId : row.baseTrainId
//				}),
//				success : function(result) {  
//					if (result != null && result != "undefind" && result.code == "0") {  
//						row.loadTimes(result.data);  
//						$.each(row.times(), function(i, n){
//							self.stns.push(n); 
//						});
//					} else {
//						showErrorDialog("获取列车时刻表失败");
//					};
//				},
//				error : function() {
//					showErrorDialog("获取列车时刻表失败");
//				},
//				complete : function(){
//					commonJsScreenUnLock();
//				}
//			}); 
//		}
		
	};  
	
	self.deleteCrosses = function(){ 
		var crossIds = "";
		var crosses = self.highLineCrossRows(); 
		var delCrosses = [];
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1 && hasActiveRole(crosses[i].tokenVehBureau())){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].planCrossId();  
				delCrosses.push(crosses[i]); 
			}else if(crosses[i].selected() == 1 && !hasActiveRole(crosses[i].tokenVehBureau())){
				showWarningDialog("你没有权限删除:" + crosses[i].crossName());
				return;
			}  
		}
		if(crossIds == ""){
			showErrorDialog("没有可删除的记录");
			return;
		}
		
		showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
	        if (r) { 
				$.ajax({
					url : basePath+"/runPlan/deletePlanCrosses",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						planCrossIds : crossIds
					}),
					success : function(result) {     
						if(result.code == 0){
							$.each(delCrosses, function(i, n){ 
								self.highLineCrossRows.remove(n); 
							});
							showSuccessDialog("删除车底交路成功"); 
						}else{
							showErrorDialog("删除车底交路失败");
						};
					}
				}); 
	        };
		});
	};
	
	
	/**
	 * 
	 */
	self.createTrainLines = function(){  
		var crossIds = "";
		var delCrosses = [];
		var crosses = self.highLineCrossRows();
		for(var i = 0; i < crosses.length; i++){  
			if(crosses[i].selected() == 1 && crosses[i].checkFlag() == 2 && hasActiveRole(crosses[i].tokenVehBureau())){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].planCrossId();  
				delCrosses.push(crosses[i]); 
			}else if(crosses[i].selected() == 1){
				if(!hasActiveRole(crosses[i].tokenVehBureau())){
					showWarningDialog("你没有权限生成:" + crosses[i].crossName() + " 的运行线");
					return;
				}else if(crosses[i].checkFlag() != 2){
					showWarningDialog(crosses[i].crossName() + "经由局未全部审核");
					return;
				} 
			}  
			
		}  
		 var planStartDate = $("#runplan_input_startDate").val();
			
		 var planEndDate =  $("#runplan_input_endDate").val();
		 if(crossIds == ""){
			 showWarningDialog("未选中数据");
		 }
		 commonJsScreenLock();
		 $.ajax({
				url : basePath+"/runPlan/handleTrainLinesWithCross",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossIds : crossIds,
					startDate : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, ''),
					endDate : (planEndDate != null ? planEndDate : self.get40Date()).replace(/-/g, '')
				}),
				success : function(result) {     
					if(result.code == 0){ 
						showSuccessDialog("正在生成运行线");
					}else{
						showErrorDialog("生成运行线失败");
					}
				},
				error : function() {
					showErrorDialog("生成运行线失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};
	self.clearData = function(){ 
		 self.currentCross(new CrossRow({"crossId":"",
				"crossName":"", 
				"chartId":"",
				"chartName":"",
				"crossStartDate":"",
				"crossEndDate":"",
				"crossSpareName":"",
				"alterNateDate":"",
				"alterNateTranNbr":"",
				"spareFlag":"",
				"cutOld":"",
				"groupTotalNbr":"",
				"pairNbr":"",
				"highlineFlag":"",
				"highlineRule":"",
				"commonlineRule":"",
				"appointWeek":"",
				"appointDay":"",
				"crossSection":"",
				"throughline":"",
				"startBureau":"",
				"tokenVehBureau":"",
				"tokenVehDept":"",
				"tokenVehDepot":"",
				"appointPeriod":"",
				"tokenPsgBureau":"",
				"tokenPsgDept":"",
				"tokenPsgDepot":"",
				"locoType":"",
				"crhType":"",
				"elecSupply":"",
				"dejCollect":"",
				"airCondition":"",
				"note":"", 
				"createPeople":"", 
				"createPeopleOrg":"",  
				"createTime":""})); 
		 self.stns.remove(function(item){
			return true;
		 });
		 self.highLineCrossRows.remove(function(item){
			return true;
		 });
		 
		 self.trainPlans.remove(function(item){
			return true;
		 }); 
		self.planDays.remove(function(item){
			return true;
		 }); 
		 self.trains.remove(function(item){
			return true;
		 });  
		 self.currentTrain = ko.observable();
	};
	self.bureauChange = function(){ 
		if(hasActiveRole(self.searchModle().bureau())){
			self.searchModle().activeFlag(1); 
			self.clearData();
		}else if(self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0);
			self.clearData();
		}
	};
	
	//审核
	self.checkCrossInfo = function(){ 
		var crossIds = "";
		var checkedCrosses = [];
		var crosses = self.highLineCrossRows();
		for(var i = 0; i < crosses.length; i++){  
			if(crosses[i].checkFlag() == 2 && crosses[i].selected() == 1){ 
				showWarningDialog(crosses[i].crossName() + "已审核"); 
				return;
			}else if(crosses[i].checkedBureau() != null && crosses[i].checkedBureau().indexOf(currentUserBureau) > -1 && crosses[i].selected() == 1){  
				showWarningDialog(crosses[i].crossName() + "本局已审核"); 
				return;
			}else if(crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ";");
				crossIds += crosses[i].planCrossId() + "#" + crosses[i].relevantBureau();
				checkedCrosses.push(crosses[i]); 
			}; 
		}  
		var planStartDate = $("#runplan_input_startDate").val(); 
		var planEndDate =  $("#runplan_input_endDate").val();
		
		if(crossIds == ""){
			showWarningDialog("没有可审核的");
			return;
		}
		commonJsScreenLock();
		 $.ajax({
				url : basePath+"/runPlan/checkCrossRunLine",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					startTime : (planStartDate != null ? planStartDate : self.currdate()).replace(/-/g, ''),
					endTime : (planEndDate != null ? planEndDate : self.get40Date()).replace(/-/g, ''),
					planCrossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(checkedCrosses, function(i, n){
							n.checkedBureau(n.checkedBureau() + "," + currentUserBureau); 
						});
						showSuccessDialog("审核成功");
					}else{
						showErrorDialog("审核失败");
					}
				},
				error : function() {
					showErrorDialog("审核失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		
	};
	
	self.createCrossMap = function(row){  
 
		self.runPlanCanvasPage.clearChart();	//清除画布
		
		 var planStartDate = $("#runplan_input_startDate").val();
			
		 var planEndDate =  $("#runplan_input_endDate").val();
		 
		 $.ajax({
				url : basePath+"/cross/createCrossMap",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossId : row.planCrossId(),  
					startTime : planStartDate != null ? planStartDate : self.currdate(),
					endTime : planEndDate != null ? planEndDate : self.get40Date()
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {   
							canvasData = {
									grid: $.parseJSON(result.data.gridData),
									jlData: $.parseJSON(result.data.myJlData)
							};   
							self.runPlanCanvasPage.drawChart({stationTypeArray:self.searchModle().drawFlags()}); 
						}
						 
					} else {
						showErrorDialog("获取车底交路绘图数据失败");
					} 
				},
				error : function() {
					showErrorDialog("获取车底交路绘图数据失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};
	
	function ToData(str)
    {
         var str1=str.substring(0,4).concat('-');
         var str2=str.substring(4,6).concat('-');
         var str3=str.substring(6,8);
         return str1+str2+str3;
    }
	
	self.showTrains = function(row) {   
		self.setCurrentCross(row);  
		self.trains.remove(function(item) {
			return true;
		});   
		$.ajax({
				url : basePath+"/highLine/getHighlineCrossTrainBaseInfoList",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					highlineCrossId : row.highLineCrossId() 
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {   
							$.each(result.data,function(n, trainInfo){
								var row = new HighLineTrain(trainInfo);
								self.trains.push(row); 
							}); 
						}
						 
					} else {
						showErrorDialog("获取交路列车列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取交路列车列表失败");
				} 
			}); 
		
		
		
		self.runPlanCanvasPage.clearChart();	//清除画布
		 var planStartDate = ToData(row.crossStartDate());
		 var planEndDate =   ToData(row.crossEndDate());
		 var highLineCrossId =row.highLineCrossId();
	
		 $.ajax({
				url : basePath+"/cross/createCrossMapVehicleSearch",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					planCrossId : self.currentPlanCrossId,  
					startTime : planStartDate != null ? planStartDate : self.currdate(),
					endTime : planEndDate != null ? planEndDate : self.get40Date(),
					highLineCrossId:highLineCrossId		
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {   
							canvasData = {
									grid: $.parseJSON(result.data.gridData),
									jlData: $.parseJSON(result.data.myJlData)
							};   
							self.runPlanCanvasPage.drawChart({stationTypeArray:self.searchModle().drawFlags()}); 
						}
						 
					} else {
						showErrorDialog("获取车底交路绘图数据失败");
					} 
				},
				error : function() {
					showErrorDialog("获取车底交路绘图数据失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		
		
		
		
		
		
		
		
		
	};  
	self.removeArrayValue = function(arr, value){
		var index = -1;
		for(var i = 0 ; i < arr.length; i++){
			if(value == arr[i]){
				index = i;
				break;
			}
		}
		if(index > -1){
			arr.splice(index, 1);
		}
	};
	self.drawFlagChange = function(a, n){  
		if(n.target.checked){
			self.searchModle().drawFlags.push(n.target.value);
		}else{ 
			self.removeArrayValue(self.searchModle().drawFlags(), n.target.value);
		} 
		self.runPlanCanvasPage.drawChart({stationTypeArray:self.searchModle().drawFlags()}); 
	};
	
	self.filterCrosses = function(){
		var filterCheckFlag = self.searchModle().filterCheckFlag();  
		var filterUnitCreateFlag = self.searchModle().filterUnitCreateFlag(); 
		$.each(self.crossRows.rows(),function(n, crossRow){
			  if(crossRow.checkFlag() == filterCheckFlag || crossRow.unitCreateFlag() == filterUnitCreateFlag){
				  crossRow.visiableRow(true);
			  }else{
				  crossRow.visiableRow(false);
			  }
				 
		  }); 
	};
	
	
	
	
	
}

function searchModle(){
	self = this;  
	 
	self.activeFlag = ko.observable(0);  
	
	self.checkActiveFlag = ko.observable(0);  
	
	self.activeCurrentCrossFlag = ko.observable(0);  
	  
	self.planStartDate = ko.observable(); 
	
	self.drawFlags =ko.observableArray(['0']); 
	
	self.bureaus = ko.observableArray();
	
	self.startBureaus = ko.observableArray(); 
	
	self.bureau = ko.observable();
	self.crossBureau = ko.observable();//计划局
	 
	self.chart =  ko.observable();
	
	self.createReason = ko.observable(); 
	
	self.filterTrainNbr = ko.observable(); 
	
	self.shortNameFlag = ko.observable(2); 
	
	self.searchThroughLine = ko.observable();
	self.searchTokenVehDepot = ko.observable();
	
	//动车段
	self.accs = ko.observableArray(); 
	 
	//担当动车所（用于高铁）
	self.tokenVehDepots = ko.observableArray();
	//客运担当局（局码）
	self.tokenPsgBureaus = ko.observableArray();
	//担当客运段
	self.tokenPsgDepts =ko.observableArray();
	
	//铁路线类型
	self.throughLines = ko.observableArray();
	 
	//动车组车型（用于高铁）
	self.crhTypes = ko.observableArray();
	
	//合并的时候会使用到
	self.tokenVehDept = ko.observable();
	//担当动车所（用于高铁）
	self.tokenVehDepot = ko.observable();
	//客运担当局（局码）
	self.tokenPsgBureau = ko.observable();
	//担当客运段
	self.tokenPsgDept = ko.observable(); 
	//铁路线类型
	self.throughLine = ko.observable();
	//动车台
	self.acc = ko.observable();
	//查询动车台
	self.searchAcc = ko.observable();
	//车次号
	self.trainNbr = ko.observable();
	//动车组车型（用于高铁）
	self.crhType = ko.observable();  
	

	//全部动车台
	self.accsAll = ko.observableArray();
	//全部动车所
	self.tokenVehDepotsAll = ko.observableArray();
	//全部铁路线
	self.throughLinesAll = ko.observableArray();
	
	
	//动车台
	self.loadAccs = function(accs){
		for ( var i = 0; i < accs.length; i++) {  
			self.accs.push(accs[i]);
			self.accsAll.push(accs[i]);
		}
	}; 
	
	//动车所
	self.loadTokenVehDepots = function(options){   
		for ( var i = 0; i < options.length; i++) {  
			self.tokenVehDepots.push(options[i]);
			self.tokenVehDepotsAll.push(options[i]);
		} 
	};  
	
	//铁路线
	self.loadThroughLines = function(options){
		for ( var i = 0; i < options.length; i++) {
			if(options[i] != null){
				self.throughLines.push({"code": options[i].name, "name": options[i].name, "bureauCode":options[i].bureauCode}); 
				self.throughLinesAll.push({"code": options[i].name, "name": options[i].name, "bureauCode":options[i].bureauCode}); 
			}
			
		} 
	}; 
	
	self.loadCrhTypes = function(options){   
		for ( var i = 0; i < options.length; i++) {  
			self.crhTypes.push({code: options[i].name, name: options[i].name});  
		}
	};  
	
	
	self.loadBureau = function(bureaus){
		for ( var i = 0; i < bureaus.length; i++) {  
			self.bureaus.push(new BureausRow(bureaus[i])); 
			self.startBureaus.push(new BureausRow(bureaus[i]));  
			self.tokenPsgBureaus.push(new BureausRow(bureaus[i]));
		} 
	};
	
	/**
	 * 计划局下拉框change事件
	 */
	self.crossBureauChange = function() {
		//清除当前下拉框数据
		self.accs.remove(function(item){
			return true;
		});
		self.tokenVehDepots.remove(function(item){
			return true;
		});
		self.throughLines.remove(function(item){
			return true;
		});
		
		
		//加载数据
		if (self.crossBureau() ==null ||self.crossBureau() == "" || self.crossBureau()=="undefined") {//加载全部数据
			for ( var i = 0; i < self.accsAll().length; i++) {  
				self.accs.push(self.accsAll()[i]);
			}
			for ( var i = 0; i < self.tokenVehDepotsAll().length; i++) {
				self.tokenVehDepots.push(self.tokenVehDepotsAll()[i]);
			}
			for ( var i = 0; i < self.throughLinesAll().length; i++) {
				self.throughLines.push(self.throughLinesAll()[i]);
			}
		} else {//加载查询局相关数据
			for ( var i = 0; i < self.accsAll().length; i++) {  
				if(self.crossBureau() == self.accsAll()[i].bureauCode) {
					self.accs.push(self.accsAll()[i]);
				}
			}
			for ( var i = 0; i < self.tokenVehDepotsAll().length; i++) {
				if(self.crossBureau() == self.tokenVehDepotsAll()[i].bureauCode) {
					self.tokenVehDepots.push(self.tokenVehDepotsAll()[i]);
				}
			}
			for ( var i = 0; i < self.throughLinesAll().length; i++) {
				if(self.crossBureau() == self.throughLinesAll()[i].bureauCode) {
					self.throughLines.push(self.throughLinesAll()[i]);
				}
			}
		}
		
	};
}

function BureausRow(data) {
	var self = this;  
	self.shortName = data.ljjc;   
	self.code = data.ljpym;   
	//方案ID 
} 

 

function CrossRow(data) {
	var self = this; 
	
	if(data == null){
		return ;
	}
	
	self.visiableRow =  ko.observable(true); 
	
	self.updateFlag = ko.observable(false); 
	
	self.selected =  ko.observable(0); 
	
	self.baseCrossId = data.baseCrossId; 
	
	self.crossId = data.crossId; 
	
	self.shortNameFlag =  ko.observable(true);
	
	self.planCrossId = ko.observable(data.planCrossId);  
	
	self.highLineCrossId = ko.observable(data.highLineCrossId);  
	
	self.vehicle1 = ko.observable(data.vehicle1);   
	
	self.vehicle2 = ko.observable(data.vehicle2);   
	 
	self.crossName = ko.observable(data.crossName);
	
	self.crossBureau = ko.computed(function(){
		if(data.crossBureau != null && data.crossBureau != ""){
			for(var i=0;i<cross.searchModle().bureaus().length;i++) {
				if(cross.searchModle().bureaus()[i].code == data.crossBureau) {
					return cross.searchModle().bureaus()[i].shortName;
				}
			}
		}else{
			return "";
		}
	});
	
	self.shortName = ko.computed(function(){
		if(data.crossName != null){
			trainNbrs = data.crossName.split('-');
			if(trainNbrs.length > 2){
				return trainNbrs[0] + '-......-' + trainNbrs[trainNbrs.length-1];
			}else{
				return data.crossName;
			}
		}else{
			return "";
		}
		
	});  
	
	self.checkFlag = ko.observable(data.checkType);
	
	self.unitCreateFlag = ko.observable(data.unitCreateFlag);
	
	self.startStn = ko.observable(data.crossStartStn);
	self.endStn = ko.observable(data.crossEndStn);
	//方案ID
	self.chartId = ko.observable(data.chartId);
	self.chartName = ko.observable(data.chartName);
	self.crossStartDate = ko.observable(data.crossStartDate);
	self.crossEndDate = ko.observable(data.crossEndDate);
	self.crossSpareName = ko.observable(data.crossSpareName);
	self.alterNateDate = ko.observable(data.alterNateDate);
	self.alterNateTranNbr = ko.observable(data.alterNateTranNbr);
	self.spareFlag = ko.observable(data.spareFlag);
	self.cutOld = ko.observable(data.cutOld);
	self.groupTotalNbr = ko.observable(data.groupTotalNbr);
	self.pairNbr = ko.observable(data.pairNbr);
	self.highlineFlag = ko.observable(data.highlineFlag);
	self.highlineRule = ko.observable(data.highlineRule);
	self.commonlineRule = ko.observable(data.commonlineRule);
	self.appointWeek = ko.observable(data.appointWeek);
	self.appointDay = ko.observable(data.appointDay);
	self.appointPeriod = ko.observable(data.appointPeriod); 
	self.crossSection = ko.observable(data.crossSection);
	self.throughline = ko.observable(data.throughLine);
	self.startBureau = ko.observable(data.startBureau); 
	//担当局 
	self.tokenVehBureau = ko.observable(data.tokenVehBureau); 
	//担当局 
	self.tokenVehBureauShowValue = ko.computed(function(){ 
			var result = "";
			 if(data.tokenVehBureau != null && data.tokenVehBureau != "null"){
				 var bs = data.tokenVehBureau.split("、"); 
				 result = data.tokenVehBureau;
				 for(var j = 0; j < bs.length; j++){
					 for(var i = 0; i < gloabBureaus.length; i++){
						 if(bs[j] == gloabBureaus[i].code){
							 result = result.replace(bs[j], gloabBureaus[i].shortName);
							 break;
						 };
					 };
				 }; 
			 }
			 return result; 
	});
	
	self.relevantBureauShowValue =  ko.computed(function(){ 
		var result = "";
		 if(data.relevantBureau != null && data.relevantBureau != "null"){  
			 for(var j = 0; j < data.relevantBureau.length; j++){
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(data.relevantBureau.substring(j, j + 1) == gloabBureaus[i].code){
						 result += result == "" ? gloabBureaus[i].shortName : "、" + gloabBureaus[i].shortName;
						 break;
					 };
				 };
			 }; 
		 } 
		 return  result; 
	});
	
	self.relevantBureau =  ko.observable(data.relevantBureau);
	
	self.checkedBureau = ko.observable(data.checkedBureau);
	
	self.checkedBureauShowValue =  ko.computed(function(){ 
		var result = "";
		 if(self.checkedBureau() != null && self.checkedBureau() != "null"){  
			 var bs = self.checkedBureau().split(","); 
			 for(var j = 0; j < bs.length; j++){
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(bs[j] == gloabBureaus[i].code){
						 result += result == "" ? gloabBureaus[i].shortName : "、" + gloabBureaus[i].shortName;
						 break;
					 };
				 };
			 };
		 } 
		 return result; 
	}); 
	
	self.activeFlag = ko.computed(function(){
		return hasActiveRole(data.tokenVehBureau);
	});   
	
	self.checkActiveFlag = ko.computed(function(){
		return data.relevantBureau != null ? (data.relevantBureau.indexOf(currentUserBureau) > -1 ? 1 : 0) : 0;
	});  
	 
	self.tokenVehDept = ko.observable(data.tokenVehDept);
	
	self.tokenVehDepot = ko.observable(data.tokenVehDepot);
	
	self.tokenPsgBureau = ko.observable(data.tokenPsgBureau);
	
	self.tokenPsgBureauShowValue = ko.computed(function(){ 
		var result = "";
		 if(data.tokenPsgBureau != null && data.tokenPsgBureau != "null"){
			 var bs = data.tokenPsgBureau.split("、"); 
			 result = data.tokenPsgBureau;
			 for(var j = 0; j < bs.length; j++){
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(bs[j] == gloabBureaus[i].code){
						 result = result.replace(bs[j], gloabBureaus[i].shortName);
						 break;
					 };
				 };
			 }; 
		 }
		 return result; 
	});
	self.postId = ko.observable(data.postId);
	self.postName = ko.observable(data.postName);
	self.tokenPsgDept = ko.observable(data.tokenPsgDept);
	self.tokenPsgDepot = ko.observable(data.tokenPsgDepot);
	self.locoType = ko.observable(data.locoType);
	self.crhType = ko.observable(data.crhType);
	self.elecSupply = ko.observable(data.elecSupply);
	self.dejCollect = ko.observable(data.dejCollect);
	self.airCondition = ko.observable(data.airCondition);
	self.note = ko.observable(data.note);  
	self.createReason = ko.observable(data.createReason);
};

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
}


function HighLineTrain(data){
	var self = this; 
	self.trainNbr = data.trainNbr;
	self.startStn = data.startStn;
	self.startTime = data.startTime==""?"":moment(data.startTime).format('MMDD HH:mm');
	self.endTime = data.endTime==""?"":moment(data.endTime).format('MMDD HH:mm');
	self.endStn = data.endStn;
	self.startStnBureau = data.startStnBureau;//始发站所属路局
	self.endStnBureau = data.endStnBureau;//终到站所属路局
}

function TrainRow(data) {
	var self = this;
	self.planTainId  = data.planTainId;//BASE_CROSS_TRAIN_ID
	self.highLineTrainId = data.highLineTrainId;//BASE_CROSS_ID
	self.trainSort = ko.observable(data.trainSort);//TRAIN_SORT
	self.baseTrainId = data.baseTrainId;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	self.times = ko.observableArray(); 
	self.simpleTimes = ko.observableArray();
	
	 
	self.sourceTime = data.sourceTime;
	self.passBureau = data.passBureau;
	
	self.startTime = ko.computed(function(){  
		return self.times().length > 0 ? self.times()[0].sourceTime : ""; 
	});;
	//结束日期（该日历交路最后一个车次的终到日期）
	self.endTime = ko.computed(function(){  
		return self.times().length > 0 ? self.times()[self.times().length - 1].targetTime : ""; 
	});
	
	self.trainNbr = data.trainNbr;
	self.startStn = ko.computed(function(){
		return self.times().length > 0 ? self.times()[0].stnName : ""; 
	});;
	self.endStn = ko.computed(function(){  
		return self.times().length > 0 ? self.times()[self.times().length - 1].stnName : ""; 
	}); 
	
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			var timeRow = new TrainTimeRow(n);
			self.times.push(timeRow);
			if(n.stationFlag != 'BTZ'){
				self.simpleTimes.push(timeRow);
			}
		});
	}; 
} ;
function filterValue(value){
	return value == null || value == "null" ? "--" : value;
};

function TrainRunPlanRow(data){
	var self = this; 
	self.trainNbr = data.trainNbr;
	self.runPlans =  ko.observableArray();
	
	if(data.runPlans !=null){
		$.each(data.runPlans, function(i, n){
			self.runPlans.push(new RunPlanRow(n));
		});
	}
}

function RunPlanRow(data){
	var self = this; 
	self.color = ko.observable("gray");
	self.runFlag = ko.computed(function(){ 
		switch (data.runFlag) {
		case '0':
			self.color("gray");
			return "停";
			break;
		case '1':
			self.color("green");
			return "开";
			break;
		case '2':
			self.color("blue");
			return "备";
			break;
		default: 
			return '';
			break;
		}
	}); 
}

function TrainTimeRow(data) { 
	var self = this;   
	self.stnSort = parseInt(data.stnSort) + 1; 
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = filterValue(data.arrTime);
	self.targetTime = filterValue(data.dptTime);
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);  
	self.runDays = data.runDays;
	self.stationFlag = data.stationFlag;
	 
}; 

function timeCompare(preTargetTime, curSourceTime){
	 
	var preEndTime = Date.parse(preTargetTime);
	var curStartTime = Date.parse(curSourceTime);
	if(preEndTime > curStartTime){
		return false;
	}
	return true;
};

function GetDateDiff(data)
{ 
	 if(data.childIndex == 0 
			 || data.dptTime == '-' 
			 || data.dptTime == null 
			 || data.arrTime == null
			 || data.arrTime == '-'){
		return "";
	} 
	var startTime = new Date(data.arrTime);
	var endTime = new Date(data.dptTime);  
	var result = "";
	
	var date3=endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
	
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000));
	
	result += days > 0 ? days + "天" : "";  
	//计算出小时数
	var leave1=date3%(24*3600*1000);     //计算天数后剩余的毫秒数
	var hours=Math.floor(leave1/(3600*1000));
	
	result += hours > 0 ? hours + "小时" : ""; 
	
	//计算相差分钟数
	var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
	var minutes=Math.floor(leave2/(60*1000));
	
	result += minutes > 0 ? minutes + "分" : "";
	//计算相差秒数
	var leave3=leave2%(60*1000);          //计算分钟数后剩余的毫秒数
	var seconds=Math.round(leave3/1000);
	
	result += seconds > 0 ? seconds + "秒" : "";  
	 
	return result == "" ? "" : result; 
};
function openLogin() {
	$("#file_upload_dlg").dialog("open");
}
 
