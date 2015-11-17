var cross = null;
$(function() { 
	cross = new CrossModel();
	ko.applyBindings(cross); 
	
	cross.init();   
});



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
 * 实际暴露给用户的model对象,knockout的最外层
 */
function CrossModel() {
	var self = this;
	self.trains = ko.observableArray();
	//经由站列表左下角的显示使用
	self.stns = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0);
	
	self.trainPlans = ko.observableArray();
	
	self.planDays = ko.observableArray(); 
	
	self.gloabBureaus = [];  
	
	self.times = ko.observableArray();
	
	self.simpleTimes = ko.observableArray();
	
//	self.runPlanCanvasPage = new RunPlanCanvasPage(self);
	self.currentPlanCrossId = ko.observable("");//当前选择交路planid 用于车底交路图tab
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
	self.activeTimes1 = ko.observableArray();
	



	
	
	
	
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
						 console.dir(result.data+"=====================")
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
		if (self.selectedActiveHighLineCrossRows().length == 0) {
			showWarningDialog("请先将需要重组的交路加载到调整区域");
			return;
		}
		
		var firstHighLineCross = self.selectedActiveHighLineCrossRows()[0];
		//默认设置为第一个交路的基本信息
		self.searchModle().tokenVehDepot(firstHighLineCross.tokenVehDepot());//担当动车所（用于高铁）
		self.searchModle().acc(firstHighLineCross.postId());//动车台
		self.searchModle().tokenPsgBureau(firstHighLineCross.tokenPsgBureau());//客运担当局（局码）
		self.searchModle().tokenPsgDept(firstHighLineCross.tokenPsgDept());//担当客运段
		self.searchModle().crhType(firstHighLineCross.crhType());//动车组车型（用于高铁）
		self.searchModle().throughLine(firstHighLineCross.throughLine());//铁路线
		self.searchModle().createReason(firstHighLineCross.createReason());//来源
		var selectedActiveHighLineCrossRows = self.selectedActiveHighLineCrossRows();
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
			
			
		}
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
		}
//		else if(self.searchModle().tokenPsgBureau() == null){
//			showWarningDialog("请选择担当所");
//			return;
//		}
//		else if(self.searchModle().tokenPsgDept() == null){
//			showWarningDialog("请填写客运段");
//			return;
		else if(self.searchModle().crhType() == null){
			showWarningDialog("请选择车型");
			return;
//		}else if(self.searchModle().throughLine() == null){
//			showWarningDialog("请选择铁路线");
//			return;
		}else if(self.searchModle().createReason() == null){
			showWarningDialog("请填写来源");
			return;
		}
		
		//担当动车所（用于高铁）
		cross.tokenVehDepot(self.searchModle().tokenVehDepot()); 
		$.each(self.searchModle().accs(),function(i, n){
			if(n.code == self.searchModle().acc()){
				cross.postName(n.name);//设置动车台中文名称
				return false;
			}
		});
		cross.postId(self.searchModle().acc());//设置动车台id
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
			/*if(i > 0){
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
			}*/
			var trains = cr.trains();
			for(var j = 0; j < trains.length; j++ ){
				cross.addTrain(trains[j]);
			};
			
		}
		
		while(selectedActiveHighLineCrossRows.length > 0){
			self.acvtiveHighLineCrosses.remove(selectedActiveHighLineCrossRows[0]);//左侧待重组列表
			self.selectedActiveHighLineCrossRows.remove(selectedActiveHighLineCrossRows[0]);//右侧重组列表
		}
		cross.createReason(self.searchModle().createReason());
		self.acvtiveHighLineCrosses.push(cross);
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
			 url : "jbtcx/queryPlanLineTrainTimesNewIframe?trainId="+currentTrain.obj.planTrainId,
				cache : false,
				type : "GET",
				success : function(result) {  
					var message = "车次：" + currentTrain.obj.trainName + "&nbsp;&nbsp;&nbsp;";
					self.currentTrainInfoMessage(message);
					$("#train_time_dlg").find("iframe").attr("src", "jbtcx/queryPlanLineTrainTimesNewIframe?trainId=" + currentTrain.obj.planTrainId);
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
	 
	

	
	
	
	
	
	self.init = function(){
		commonJsScreenLock(4); 
		
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
//							 if(obj.ljjc==curUserBur){
//								 fistObj = obj;
//							 }else{
								 bureaus.push(obj);
//							 }
						}
//						if(null != fistObj){
							bureaus.unshift("");
//						}
					}
//					console.log(bureaus.length);
					self.searchModle().loadBureau(bureaus);
					//self.searchModle().loadBureau(result.data); 
//					if (result.data !=null) { 
//						$.each(result.data,function(n, bureau){  
//							self.gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym});
//						});
//						console.log(self.gloabBureaus().length);
//					} 
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
			url : basePath+"/highLine/getThroughLineName",
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
					showErrorDialog("获取初始数据失败");
				} 
			},
			error : function() {
				showErrorDialog("获取初始数据失败");
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
			//url : basePath+"/highLine/getDepots/CROSS_CHECK",
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
					showErrorDialog("获取初始数据失败");
				} 
			},
			error : function() {
				showErrorDialog("获取初始数据失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
	    
	    $.ajax({
			//url : basePath+"/highLine/getAccs/CROSS_CHECK",
	    	url : basePath+"/highLine/getAccs/ALL",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					if (result.data !=null) { 
						self.searchModle().loadAccs(result.data); ;
					} 
				} else {
					showErrorDialog("获取初始数据失败");
				} 
			},
			error : function() {
				showErrorDialog("获取初始数据失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
		
		
		
	};



	
	
	/**
	 * 取消发布命令按钮点击事件
	 */
	self.createCmdInfoCancel = function() {
		$("#cmdInfo_dialog").dialog("close");//命令查看窗口div
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


	
	
	
	
	
	
	/**
	 * 保存按钮
	 */
	self.btnSaveDicRelaCrossPost = function() {
		
//		var crossName = self.searchModle().crossName();
//		var note = self.searchModle().note();
//		var searchThroughLine = self.searchModle().searchThroughLine().throughLineName;
//		var searchTokenVehDepot = self.searchModle().searchTokenVehDepot().name;
//		var postName = "";
//		var post_id = "";
//		if(self.searchModle().searchAcc()!=null && self.searchModle().searchAcc()!=undefined){
//			 postName = self.searchModle().searchAcc().name;//动车台名称
//			 post_id = self.searchModle().searchAcc().code;//动车台id
//		}
		
		if('' == $("#jlName").val() || null == $("#jlName").val()){
			showWarningDialog("请填写交路名！");
			return;
		}
		if('' == $("#tlLine option:selected").text() || null == $("#tlLine option:selected").text()){
			showWarningDialog("请选择铁路线！");
			return;
		}
		if('' == $("#ddj").val() || null == $("#ddj").val()){
			showWarningDialog("请选择担当局！");
			return;
		}
		
		self.highLineCrossRows.remove(function(item) {
			return true;
		});
		commonJsScreenLock();
		$.ajax({
			url : basePath+"/highLine/saveDicRelaCrossPost",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
//				crossName : crossName,//交路名
//				throughLine:searchThroughLine,//铁路线
//				tokenVehDepot: searchTokenVehDepot,//动车所
//				postName: postName,
//				note :note,
//				post_id:post_id,
				crossName : $("#jlName").val(),//交路名
				throughLine:$("#tlLine option:selected").text(),//铁路线
				tokenVehDepot: $("#dcs option:selected").text(),//动车所
				postName: $("#dct option:selected").text(),
				post_id:$("#dct").val(),
				tqts : $("#tqts").val(),
				//ddjName: $("#ddj option:selected").text(),
				ddjCode: $("#ddj").val()
			}),
			success : function(result) {
				
				if (result != null && typeof result == "object" && result.code == "0") {
					showSuccessDialog("保存成功");
					window.parent.highLine.loadCrosses();
					commonJsScreenUnLock(1);
				} else {
					showErrorDialog("保存失败");
				};
			},
			error : function() {
				showErrorDialog("保存失败");
			},
			complete : function(){
				commonJsScreenUnLock(1);
			}
		});
	};
	
	self.btnCancel = function(){
		$("#add_disRelaCross_dialog").dialog("close");
	};
	
	
}

function searchModle(){
	self = this;  
	self.crossName = ko.observable();
	self.note = ko.observable();
	self.activeFlag = ko.observable(0);  
	
	self.checkActiveFlag = ko.observable(0);  
	
	self.activeCurrentCrossFlag = ko.observable(0);  
	  
	self.planStartDate = ko.observable(); 
	 
	self.bureaus = ko.observableArray();
	
	self.drawFlags =ko.observableArray(['0']); 
	
	self.startBureaus = ko.observableArray(); 
	
	self.bureau = ko.observable();
	 
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
	
	
	self.loadAccs = function(accs){   
		for ( var i = 0; i < accs.length; i++) {  
			self.accs.push({code:accs[i].code, name:accs[i].name});  
		} 
	}; 
	
	self.loadTokenVehDepots = function(options){   
		for ( var i = 0; i < options.length; i++) {  
			self.tokenVehDepots.push(options[i]);  
		} 
	};  
	
	self.loadThroughLines = function(options){   
		for ( var i = 0; i < options.length; i++) {  
			if(options[i] != null){
				self.throughLines.push({throughLineLineId: options[i].throughLineLineId, throughLineName: options[i].throughLineName});  
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
	
	
	
}

function BureausRow(data) {
	var self = this;  
//	console.log("ljjc::" + data.ljjc + ",ljpym::" + data.ljpym);
	self.shortName = data.ljjc;   
	self.code = data.ljpym;   
	//方案ID 
} 

 
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
	//self.trainNbr = ko.computed(function(){
//	return self.times().length > 0 ? self.times()[0].trainNbr : ""; 
//});;
	/*self.trainNbr = ko.computed(function(){
		return self.times().length > 0 ? self.times()[0].trainNbr : ""; 
	});;*/
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

