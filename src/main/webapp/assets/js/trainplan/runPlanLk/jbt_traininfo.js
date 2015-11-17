$(function() { 
	
	var appModel = new ApplicationModel();
	ko.applyBindings(appModel); 
	 
	appModel.init();   
});  

function SelectCheckModle(){
	var self = this; 
	
	self.crossAllcheckBox = ko.observable(0);
	
	self.setCurrentCross = function(cross){
		self.currentCross(cross);
		if(self.searchModle().showCrossMap() == 1){
			$("#cross_map_dlg").find("iframe").attr("src", "cross/provideCrossChartData?crossId=" + cross.crossId);
		}
	}; 
	
	 
	self.selectCross = function(row){
//		self.crossAllcheckBox();
		if(row.selected() == 0){
			self.crossAllcheckBox(1);
			$.each(self.crossRows.rows(), function(i, crossRow){ 
				if(crossRow.selected() != 1 && crossRow != row){
					self.allcheckBox(0);
					return false;
				}  
			}); 
		}else{
			self.crossAllcheckBox(0);
		} 
	}; 
	
}

function ApplicationModel() {

	var _tabType = "";//bjdd(本局担当)   wjdd（外局担当）
	var self = this;
	//列车列表
	self.trains = ko.observableArray();
	

	self.trainAllTimes = ko.observableArray();//保存列车详情时刻所有数据，用于简图复选框点击事件列表值变更来源
	
	self.trainLines = ko.observableArray();
	//交路列表   
	self.gloabBureaus = [];   
	//担当局
	self.searchModle = ko.observable(new searchModle()); 
	
	self.currentTrain = ko.observable(); 
	
	self.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year+"-"+month+"-"+days;
	};
	
	
	
	self.init = function(){

		commonJsScreenLock(2);
		$("#run_plan_train_times_canvas_dialog").dialog("close");
		_tabType = $("#tabType_hidden").val();//bjdd(本局担当)   wjdd（外局担当）
		if (_tabType == "wjdd") {
			$("#bjdd_btn_useTrainStnAndTime").hide();//隐藏套用经由及时刻按钮
		}
		
		 $.ajax({
				url : basePath+"/jbtcx/querySchemes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) { 
							self.searchModle().loadChats(result.data); 
						} 
					} else {
						showErrorDialog("获取方案失败");
					} 

				},
				error : function() {
					showErrorDialog("获取方案失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
		    });
	    $.ajax({
			url : basePath+"/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					self.searchModle().loadBureau(result.data); 
					if (result.data !=null) { 
						$.each(result.data,function(n, bureau){  
							self.gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym}); 
						});
					} 

				} else {
					showErrorDialog("");
				} 
				
			},
			error : function() {
				showErrorDialog("接口调用失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
	    });
	    
	    
	    /**
	     * 详情时刻表中 简图复选框点击事件
	     */
	    $("#input_checkbox_stationType_jt").click(function(){
		    var _stationTypeArray = [];
			$("[name='input_checkbox_stationType']").each(function(){
				if($(this).is(":checked")) {
					//查看简图 只包含始发、终到
					_stationTypeArray = ["SFZ","FJK","TZ","ZDZ"];
					//_stationTypeArray.push($(this).val());
				} else {
					//显示所有 包含始发、终到、分界口、停站、不停站
					_stationTypeArray = ["SFZ","FJK","BTZ","TZ","ZDZ"];//"0","FJK","TZ","BT"
				}
		    });
			

			self.trainLines.remove(function(item){
				return true;
			});
			
			$.each(self.trainAllTimes(), function(i, n){
				if ($.inArray(n.stationFlag, _stationTypeArray) > -1) {
					self.trainLines.push(n);
				}
				
				if(i == self.trainAllTimes().length - 1){
					$("#plan_runline_table_trainLine").freezeHeader(); 
				}
			});
	    });
		
	};  
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().trainNbr(event.target.value.toUpperCase());
	};
	
	self.loadTrains = function(){
		commonJsScreenLock(1);
		self.trainRows.loadRows();
	};
	self.loadTrainsForPage = function(startIndex, endIndex) {  
		var trainNbr = self.searchModle().trainNbr();  
		var startBureauShortName = self.searchModle().startBureau();  
		var endBureauShortName = self.searchModle().endBureau();   
		var chart = self.searchModle().chart(); 
		var fuzzyFlag = self.searchModle().fuzzyFlag();
		
		if(chart == null){
			commonJsScreenUnLock();
			showErrorDialog("请选择方案");
			return;
		} 
		$.ajax({
				url : basePath+"/jbtcx/queryTrains",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					startBureauShortName : startBureauShortName, 
					endBureauShortName : endBureauShortName,
					trainNbr : trainNbr,
					fuzzyFlag : fuzzyFlag,
					chartId : chart.chartId,
					rownumstart : startIndex,
					rownumend : endIndex
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
							if (result.data !=null) {   
								var rows = [];
								$.each(result.data.data,function(n, crossInfo){
									rows.push(new TrainRow(crossInfo));  
								}); 
								 $("#plan_runline_table_trainInfo").freezeHeader();  
								self.trainRows.loadPageRows(result.data.totalRecord, rows);
							}

							
					} else {
						showErrorDialog("查询失败");
					};
				},
				error : function() {
					showErrorDialog("查询失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	}; 
	
	self.trainRows = new PageModle(50, self.loadTrainsForPage);
	 
	self.showTrainTimes = function(row) {
		self.currentTrain(row);
		self.trainAllTimes.remove(function(item){
			return true;
		});
		self.trainLines.remove(function(item){
			return true;
		});
		if(row.times().length > 0){
			$("[name='input_checkbox_stationType']").each(function(){
				if($(this).is(":checked")) {
					//查看简图 只包含始发、终到
					_stationTypeArray = ["SFZ","FJK","TZ","ZDZ"];
					//_stationTypeArray.push($(this).val());
				} else {
					//显示所有 包含始发、终到、分界口、停站、不停站
					_stationTypeArray = ["SFZ","FJK","BTZ","TZ","ZDZ"];//"0","FJK","TZ","BT"
				}
		    });
			
			$.each(row.times(), function(i, n){
				self.trainAllTimes.push(n);
				
				if ($.inArray(n.stationFlag, _stationTypeArray) > -1) {
					self.trainLines.push(n);
				}
				
				if(i == row.times().length - 1){
					$("#plan_runline_table_trainLine").freezeHeader(); 
				}
			});
		}else{
			commonJsScreenLock(1);
			$.ajax({
				url : basePath+"/jbtcx/queryTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					trainId : row.id
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
						row.loadTimes(result.data);
						var _stationTypeArray = [];
						$("[name='input_checkbox_stationType']").each(function(){
							if($(this).is(":checked")) {
								//查看简图 只包含始发、终到
								_stationTypeArray = ["SFZ","FJK","TZ","ZDZ"];
								//_stationTypeArray.push($(this).val());
							} else {
								//显示所有 包含始发、终到、分界口、停站、不停站
								_stationTypeArray = ["SFZ","FJK","BTZ","TZ","ZDZ"];//"0","FJK","TZ","BT"
							}
					    });
						
						$.each(row.times(), function(i, n){
							self.trainAllTimes.push(n);
							
							if ($.inArray(n.stationFlag, _stationTypeArray) > -1) {
								self.trainLines.push(n);
							}
							
							if(i == row.times().length - 1){
								$("#plan_runline_table_trainLine").freezeHeader(); 
							}
						});
					} else {
						showErrorDialog("获取列车时刻表失败");
					};
							
					$("#plan_runline_table_trainLine").freezeHeader(); 
				},
				error : function() {
					showErrorDialog("获取列车时刻表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		};
		
		
		if(!$("#run_plan_train_times_canvas_dialog").is(":hidden")){
			self.showTrainTimeCanvas();
		};
		
	};  
	
	self.fuzzyChange = function(){
		if(self.searchModle().fuzzyFlag() == 0){
			self.searchModle().fuzzyFlag(1);
		}else{
			self.searchModle().fuzzyFlag(0);
		}
	};
	
	
	
	/**
	 * 图形化显示列车运行时刻
	 */
	self.showTrainTimeCanvas = function(){
		if (self.currentTrain() == null || self.currentTrain().id ==null || self.currentTrain().id =="undefind") {
			showWarningDialog("请选择列车记录");
			return;
		}

		$("#run_plan_train_times_canvas_dialog").find("iframe").attr("src", basePath+"/jbtcx/getTrainTimeCanvasPage?planTrainId=" + self.currentTrain().id+"&trainNbr="+self.currentTrain().name);
		$('#run_plan_train_times_canvas_dialog').dialog({title: "列车运行时刻图 &nbsp;&nbsp;&nbsp;车次："+self.currentTrain().name, autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#run_plan_train_times_canvas_dialog").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#run_plan_train_times_canvas_dialog').height();
				var WW = $('#run_plan_train_times_canvas_dialog').width();
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
	
	
	/**
	 * 套用经由按钮点击事件
	 * 
	 * 将详情时刻表站名引用至临客命令 时刻表
	 * @author Think
	 * @since 2014-07-25
	 */
	self.useTrainStn = function() {
		if (self.trainAllTimes().length == 0) {
			showWarningDialog("请先选择列车记录");
			return;
		}
		
		if(_tabType == "bjdd") {//bjdd(本局担当)   wjdd（外局担当）
			//清除
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
		}
		commonJsScreenLock();
		var _LkCMDTrain_len = window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows().length;
		var _parentRcBureauShortName = "";//临时保存上一条记录中局码
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().passBureau("");
		$.each(self.trainAllTimes(), function(i, n){
			
			if(_parentRcBureauShortName != n.bureauShortName) {//局码与上一条记录中局码不相等
				_parentRcBureauShortName = n.bureauShortName;
				window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().passBureau(window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().passBureau() + _parentRcBureauShortName);
			}
			
			n.childIndex = _LkCMDTrain_len+i;	//增加childIndex属性
			
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.push(new window.parent.runPlanLkCmdPageModel.cmdTrainStnTimeRow(toCmdLkTrainTimeRow(n, "2")));
			
		});
		//设置其他字段
		
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().routeId(self.currentTrain().routeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().trainTypeId(self.currentTrain().trainTypeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().business(self.currentTrain().operation);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().startBureauId(self.currentTrain().sourceBureauId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().startStnId(self.currentTrain().sourceNodeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().endBureauId(self.currentTrain().targetBureauId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().endStnId(self.currentTrain().targetNodeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().endDays(self.currentTrain().targetTimeScheduleDates);
		

		//设置套用的基本图车次id
		window.parent.runPlanLkCmdPageModel.useBaseTrainId(self.currentTrain().id);
		
		commonJsScreenUnLock(1);

	};
	
	
	

	/**
	 * 套用经由及时刻按钮点击事件
	 * 
	 * 将详情时刻表站名及时刻引用至临客命令 时刻表
	 * @author Think
	 * @since 2014-07-25
	 */
	self.useTrainStnAndTime = function() {
		
		if (self.trainAllTimes().length == 0) {
			showWarningDialog("请先选择列车记录");
			return;
		}
		

		if(_tabType == "bjdd") {//bjdd(本局担当)   wjdd（外局担当）
			//清除
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
		}
		commonJsScreenLock(2);
		var _LkCMDTrain_len = window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows().length;
		var _parentRcBureauShortName = "";//临时保存上一条记录中局码
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().passBureau("");
		$.each(self.trainAllTimes(), function(i, n){
			if(_parentRcBureauShortName != n.bureauShortName) {//局码与上一条记录中局码不相等
				_parentRcBureauShortName = n.bureauShortName;
				window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().passBureau(window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().passBureau() + _parentRcBureauShortName);
			}
			
			n.childIndex = _LkCMDTrain_len+i;	//增加childIndex属性
//			console.log("n.stnName::" + n.stnName);
//			console.log("n.nodeStationName::" + n.nodeStationName);
			window.parent.runPlanLkCmdPageModel.runPlanLkCMDTrainStnRows.push(new window.parent.runPlanLkCmdPageModel.cmdTrainStnTimeRow(toCmdLkTrainTimeRow(n, "1")));
		});
		commonJsScreenUnLock(1);
		//设置其他字段
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().routeId(self.currentTrain().routeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().trainTypeId(self.currentTrain().trainTypeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().business(self.currentTrain().operation);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().startBureauId(self.currentTrain().sourceBureauId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().startStnId(self.currentTrain().sourceNodeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().endBureauId(self.currentTrain().targetBureauId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().endStnId(self.currentTrain().targetNodeId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().endDays(self.currentTrain().targetTimeScheduleDates);

		// 新增字段
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().sourceNodeStationId(self.currentTrain().sourceNodeStationId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().sourceNodeStationName(self.currentTrain().sourceNodeStationName);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().sourceNodeTdcsId(self.currentTrain().sourceNodeTdcsId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().sourceNodeTdcsName(self.currentTrain().sourceNodeTdcsName);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().targetNodeStationId(self.currentTrain().targetNodeStationId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().targetNodeStationName(self.currentTrain().targetNodeStationName);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().targetNodeTdcsId(self.currentTrain().targetNodeTdcsId);
		window.parent.runPlanLkCmdPageModel.currentCmdTxtMl().targetNodeTdcsName(self.currentTrain().targetNodeTdcsName);
		
		//设置套用的基本图车次id
		window.parent.runPlanLkCmdPageModel.useBaseTrainId(self.currentTrain().id);
		commonJsScreenUnLock(1);
	};
	
	
	/**
	 * 将详细时刻数据转换为临客命令时刻格式
	 * 
	 * useType   1:套用经由及时刻，赋值所有信息
	 * 			2:套用经由，只赋值站名
	 */
	function toCmdLkTrainTimeRow(trainTimeRowData, useType) {
		var cmdLkData = {};
		cmdLkData.childIndex = trainTimeRowData.childIndex;
		cmdLkData.stnName = trainTimeRowData.stnName;//车站名
		cmdLkData.stnBureau = trainTimeRowData.bureauShortName;//车站所属路局
		cmdLkData.arrTime = useType=="1"?trainTimeRowData.sourceTime:"";
		cmdLkData.dptTime = useType=="1"?trainTimeRowData.targetTime:"";
		cmdLkData.trackName = useType=="1"?trainTimeRowData.trackName:"";
		cmdLkData.platform = "";
		cmdLkData.arrTrainNbr = "";
		cmdLkData.dptTrainNbr = "";
		cmdLkData.arrRunDays = useType=="1"?trainTimeRowData.sourceDay:0;//到达运行天数
		cmdLkData.dptRunDays = useType=="1"?trainTimeRowData.targetDay:0;//出发运行天数
		cmdLkData.isSelectLine = useType=="1"?1:0;	//选线标识 用于parent时刻列表计算运行天数
		cmdLkData.nodeId = trainTimeRowData.nodeId;
		cmdLkData.jobs = trainTimeRowData.jobs;
		cmdLkData.nodeName = trainTimeRowData.nodeName;
		cmdLkData.bureauId = trainTimeRowData.bureauId;
		// 新增字段
		//console.log("toCmdLkTrainTimeRow" + trainTimeRowData.nodeStationId);
		cmdLkData.nodeStationId = trainTimeRowData.nodeStationId;
		cmdLkData.nodeStationName = trainTimeRowData.nodeStationName;
		cmdLkData.nodeTdcsId = trainTimeRowData.nodeTdcsId;
		cmdLkData.nodeTdcsName = trainTimeRowData.nodeTdcsName;
		
//		console.log("trainTimeRowData.stnName::" + trainTimeRowData.stnName);
//		console.log("trainTimeRowData.nodeStationName::" + trainTimeRowData.nodeStationName);
		
		return cmdLkData;
	};
	
}

function searchModle(){
	
	self = this;   
	
	self.startBureaus = ko.observableArray(); 
	
	self.endBureaus = ko.observableArray(); 
	
	self.charts = ko.observableArray(); 
 
	self.startBureau = ko.observable();
	
	self.endBureau = ko.observable();
	
	
	self.trainNbr = ko.observable($("#trainNbr_hidden").val()); 
	
	self.chart = ko.observable(); 
	
	self.fuzzyFlag = ko.observable(1); 
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {   
			self.startBureaus.push(new BureausRow(bureaus[i]));  
			self.endBureaus.push(new BureausRow(bureaus[i]));
		} 
	}; 
	
	self.loadChats = function(charts){   
		for ( var i = 0; i < charts.length; i++) {   
			self.charts.push({"chartId": charts[i].schemeId, "name": charts[i].schemeName});  
		} 
	}; 
	
}

function BureausRow(data) {
	var self = this;  
	self.shortName = data.ljjc;   
	self.code = data.ljpym;  
} 

function filterValue(value){
	return value == null || value == "null" ? "--" : value;
}
function TrainTimeRow(data) { 
	var self = this; 
	self.index = data.childIndex + 1;
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
//	self.sourceTime = filterValue(data.arrTime);
//	self.targetTime = filterValue(data.dptTime);
	self.sourceTime = data.stationFlag=='SFZ'?'--':(moment(data.arrTimeAll).format("HH:mm:SS"))//filterValue(data.arrTime);//moment(startDate).format("YYYY-MM-DD")
	self.targetTime = data.stationFlag=='ZDZ'?'--':(moment(data.dptTimeAll).format("HH:mm:SS"))//filterValue(data.dptTime);
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);
	self.arrRunDays = data.stationFlag=='SFZ'?'--':data.arrRunDays;
	self.runDays = data.stationFlag=='ZDZ'?'--':data.runDays;
	self.stationFlag = data.stationFlag;
//	self.sourceDay = data.arrRunDays;
//	self.targetDay = data.targetDay;
	self.targetDay = data.stationFlag=='ZDZ'?'--':data.targetDay;
	self.sourceDay = data.stationFlag=='SFZ'?'--':data.arrRunDays;

	self.nodeId = data.nodeId;
	self.jobs = data.jobs;
	self.nodeName = data.nodeName;
	self.bureauId = data.bureauId;
	// 新增字段
	//console.log("TrainTimeRow" + data.nodeStationId);
	self.nodeStationId = data.nodeStationId;
	self.nodeStationName = data.nodeStationName;
	self.nodeTdcsId = data.nodeTdcsId;
	self.nodeTdcsName = data.nodeTdcsName;
	 
}; 
function GetDateDiff(data)
{ 
	if(data.childIndex == 0)
		return "";
	else if(data.dptTime == '-'){
		return "";
	} 
	var startTime = new Date(data.arrTimeAll);
	var endTime = new Date(data.dptTimeAll);  
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
function TrainRow(data) {   
	var self = this;  
	self.id = data.planTrainId;
	self.name = data.trainNbr; 
	self.times = ko.observableArray();  
	self.selected  = ko.observable();  
	self.startBureau = data.startBureau; 
	self.startStn =  data.startStn; 
	self.sourceTime = filterValue(data.startTimeStr); 
	self.endStn = data.endStn; 
	self.endBureau = data.endBureau; 
	self.routingBureau = data.routingBureauShortName; 
	self.runDays = data.relativeTargetTimeDay;
	self.targetTime =  filterValue(data.endTimeStr);

	//增加字段以便套用经由使用
	self.routeId =  data.routeId;
	self.trainTypeId =  data.trainTypeId;
	self.operation =  data.operation;
	self.sourceBureauId =  data.sourceBureauId;
	self.sourceNodeId =  data.sourceNodeId;
	self.targetBureauId =  data.targetBureauId;
	self.targetNodeId =  data.targetNodeId;
	self.targetTimeScheduleDates =  data.targetTimeScheduleDates;
	
	// 新增字段
	self.sourceNodeStationId = data.sourceNodeStationId;
	self.sourceNodeStationName = data.sourceNodeStationName;
	self.sourceNodeTdcsId = data.sourceNodeTdcsId;
	self.sourceNodeTdcsName = data.sourceNodeTdcsName;
	self.targetNodeStationId = data.targetNodeStationId;
	self.targetNodeStationName = data.targetNodeStationName;
	self.targetNodeTdcsId = data.targetNodeTdcsId;
	self.targetNodeTdcsName = data.targetNodeTdcsName;
	
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			self.times.push(new TrainTimeRow(n));
		});
	}; 
	
} ; 