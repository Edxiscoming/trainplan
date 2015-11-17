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
	
	self.currentTrain = ko.observable(new TrainRow(createEmptyCmdTrainRow())); 
	
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
		$("#runPlanLk_cmd_input_startDate").datepicker();
		$("#runPlanLk_cmd_input_endDate").datepicker();
		
		$("#runPlanLk_cmd_input_startDate").val(moment().subtract("day", 1).format('YYYY-MM-DD'));
		$("#runPlanLk_cmd_input_endDate").val(moment().format('YYYY-MM-DD'));
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
	    
		
	};  
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().trainNbr(event.target.value.toUpperCase());
	};
	
	self.loadTrains = function(){
		commonJsScreenLock(1);
		self.trainRows.loadRows();
	};
	
	function createEmptyCmdTrainRow() {
		var _dataObj = {};
		_dataObj.cmdTrainId = "";
		_dataObj.baseTrainId = "";
		_dataObj.cmdBureau = "";
		_dataObj.cmdType = "";
		_dataObj.cmdTxtMlId = "";
		_dataObj.cmdTxtMlItemId = "";
		_dataObj.cmdNbrBureau = "";
		_dataObj.cmdItem = "";
		_dataObj.cmdNbrSuperior = "";
		_dataObj.trainNbr = "";
		_dataObj.startStn = "";
		_dataObj.endStn = "";
		_dataObj.rule = "";
		_dataObj.selectedDate = "";
		_dataObj.startDate = "";
		_dataObj.endDate = "";
		_dataObj.passBureau = "";
		_dataObj.updateTime = "";
		_dataObj.cmdTime = "";
		_dataObj.isExsitStn = "";
		_dataObj.userBureau = "";
		_dataObj.selectState = "";
		_dataObj.createState = "";
		
		//8.21增加字段
		_dataObj.routeId = "";
		_dataObj.trainTypeId = "";
		_dataObj.business = "";
		_dataObj.startBureauId = "";
		_dataObj.startStnId = "";
		_dataObj.endBureauId = "";
		_dataObj.endStnId = "";
		_dataObj.endDays = "";
		
		// 新增字段
		_dataObj.sourceNodeStationId   = "";
		_dataObj.sourceNodeStationName = "";
		_dataObj.sourceNodeTdcsId      = "";
		_dataObj.sourceNodeTdcsName    = "";
		_dataObj.targetNodeStationId   = "";
		_dataObj.targetNodeStationName = "";
		_dataObj.targetNodeTdcsId      = "";
		_dataObj.targetNodeTdcsName    = "";
		
		return _dataObj;
	};
	
	//复杂历史弹出窗口查询列表
	self.loadTrainsForPage = function(startIndex, endIndex) {
		var startDate = moment($("#runPlanLk_cmd_input_startDate").val()).format("YYYY-MM-DD");
		var endDate = moment($("#runPlanLk_cmd_input_endDate").val()).format("YYYY-MM-DD");
		if(startDate!=null && startDate!=undefined && endDate!=null && endDate!=undefined){
			if(startDate > endDate){
				showWarningDialog("令电日期(开始)不能大于令电日期(截至)!");
				return ;
			}
		}
		$.ajax({
				url : basePath+"/runPlanLk/querycmdTrain",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					startDate :startDate,//起始日期
					endDate : endDate,//终止日期
					cmdNbrBureau : self.searchModle().cmdNbrBureau(),//路局命令号
					cmdNbrSuperior : self.searchModle().cmdNbrSuperior(),//总公司命令号
					trainNbr : self.searchModle().trainNbr(),//车次
					cmdType : self.searchModle().cmdTypeOptionBjdd().text,//命令类型 (既有加开；既有停运；高铁加开；高铁停运)
					cmdTrainId : self.searchModle().cmdTrainId(),
					rownumstart : startIndex,
					rownumend : endIndex
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
							if (result.data !=null) {   
								var rows = [];
//								$.each(result.data.data,function(n, crossInfo){
//									rows.push(new TrainRow(crossInfo));
//								}); 
								$.each(result.data,function(n, crossInfo){
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
				
//				if ($.inArray(n.stationFlag, _stationTypeArray) > -1) {
					self.trainLines.push(n);
//				}
				
				if(i == row.times().length - 1){
					$("#plan_runline_table_trainLine").freezeHeader(); 
				}
			});
		}else{
			commonJsScreenLock(1);
			$.ajax({
				url : basePath+"/runPlanLk/queryTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					cmdTrainId : row.cmdTrainId()
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
							
//							if ($.inArray(n.stnType, _stationTypeArray) > -1) {
								self.trainLines.push(n);
//							}
							
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
		
		
//		if(!$("#run_plan_train_times_canvas_dialog").is(":hidden")){
//			self.showTrainTimeCanvas();
//		};
		
	};  
	
	self.fuzzyChange = function(){
		if(self.searchModle().fuzzyFlag() == 0){
			self.searchModle().fuzzyFlag(1);
		}else{
			self.searchModle().fuzzyFlag(0);
		}
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
		cmdLkData.dptRunDays = useType=="1"?trainTimeRowData.dptRunDays:0;//出发运行天数
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
var cmdTypeArrayBjdd = [{"code": "1", "text": "既有加开命令"},
                        {"code": "5", "text": "既有加开文电"}
                    ];
var cmdTypeArrayWjdd = [{"code": "1", "text": "既有加开命令"},
                        {"code": "5", "text": "既有加开文电"}
	                    ];
var wdCmdRuleArray = [{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"}];//文电加开 开行规律下拉框选择列表数据

/**
 * 查询model
 * 
 * private
 */
function searchModle(){
	
	_self = this;   
	
	_self.startBureaus = ko.observableArray(); 
	
	_self.endBureaus = ko.observableArray(); 
	
	_self.charts = ko.observableArray(); 
 
	_self.startBureau = ko.observable();
	
	_self.endBureau = ko.observable();
	
	
	_self.trainNbr = ko.observable($("#trainNbr_hidden").val()); 
	_self.cmdTrainId = ko.observable($("#cmdTrainId_hidden").val()); 
	
	_self.chart = ko.observable(); 
	
	_self.fuzzyFlag = ko.observable(1); 
	
	_self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {   
			_self.startBureaus.push(new BureausRow(bureaus[i]));  
			_self.endBureaus.push(new BureausRow(bureaus[i]));
		} 
	}; 
	
	_self.loadChats = function(charts){   
		for ( var i = 0; i < charts.length; i++) {   
			_self.charts.push({"chartId": charts[i].schemeId, "name": charts[i].schemeName});  
		} 
	}; 
	//--------------------------------------------------------------------
	/**
	 * 查询条件
	 */
	_self.bureauArray = ko.observableArray();	//路局简称list
	_self.bureauOption = ko.observable();		//路局下拉框选项
	_self.cmdNbrBureau = ko.observable("");		//路局命令号
	_self.cmdNbrSuperior = ko.observable("");	//总公司命令号
	_self.trainNbr = ko.observable("");			//车次号
	_self.selectStateOption = ko.observable();		//选线状态	下拉框选项 
	_self.createStateOption = ko.observable();		//生成开行计划状态	下拉框选项 
	_self.baseStationArray = ko.observableArray();	//所有车站的基本信息
	_self.cmdTypeArrayBjdd = ko.observableArray(cmdTypeArrayBjdd);		//本局担当命令类型下拉框
	_self.cmdTypeOptionBjdd = ko.observable();		//本局担当命令类型下拉框选项 (既有加开；既有停运；高铁加开；高铁停运)
	_self.cmdTypeArrayWjdd = ko.observableArray(cmdTypeArrayWjdd);		//外局担当命令类型下拉框
	_self.cmdTypeOptionWjdd = ko.observable();		//外局担当命令类型下拉框选项 (既有加开；既有停运；高铁加开；高铁停运)
	
	
	/**
	 * 加载路局下拉框数据
	 */
	_self.loadBureauData = function() {
		$.ajax({
			url : basePath+"/plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json",
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {
						_self.bureauArray.push({"value": "","text": "","ljqc":"","ljpym":"","ljdm":""});
						for ( var i = 0; i < result.data.length; i++) {
							_self.bureauArray.push({"value": result.data[i].ljjc,
								"text": result.data[i].ljjc, 
								"ljqc":result.data[i].ljqc,
								"ljpym":result.data[i].ljpym, 
								"ljdm":result.data[i].ljdm});
						}
						
						//暂时不启用  自动补齐插件
//						renderInputTjj();//渲染途经局自动补全输入框
					}
					
				} else {
					showErrorDialog("获取路局列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取路局列表失败");
			},
			complete : function(){
				commonJsScreenUnLock(1);
			}
	    });
	};
	
	//加载车站字典数据
	_self.loadStnData = function() {
		$.ajax({
			url : basePath+"/runPlanLk/getBaseStationInfo",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
//				stnName : value//车站名称
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {
//						for (var i = 0; i < result.data.length; i++) {
//							if (value == result.data[i].STNNAME) {
//								self.currentBureauJc(result.data[i].STNBUREAUSHORTNAME);//路局简称
//								break;
//							}
//						}
					}
				} else {
					showErrorDialog("获取车站信息失败");
				}
			},
			error : function() {
				showErrorDialog("获取车站信息失败");
			},
			complete : function(){
				commonJsScreenUnLock(1);
			}
	    });
	};
	
	
	
	/**
	 * 渲染途经局自动补全输入框
	 */
	function renderInputTjj() {
		$('#runPlanLk_cmd_input_tjj')
        .textext({
            plugins : 'tags autocomplete'
        })
        .bind('getSuggestions', function(e, data)
        {
                textext = $(e.target).textext()[0],
                query = (data ? data.query : '') || ''
                ;

            $(this).trigger(
                'setSuggestions',
                { result : textext.itemManager().filter(_self.bureauArray(), query) }
            );
        });
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
	self.stnName = filterValue(data.nodeName);
	self.bureauShortName = filterValue(data.stnBureau);
	self.sourceTime = filterValue(data.arrTime);
	self.targetTime = filterValue(data.dptTime);
//	self.sourceTime = data.stnType=='SFZ'?'--':(moment(data.arrTime).format("HH:mm:SS"))//filterValue(data.arrTime);//moment(startDate).format("YYYY-MM-DD")
//	self.targetTime = data.stnType=='ZDZ'?'--':(moment(data.dptTime).format("HH:mm:SS"))//filterValue(data.dptTime);
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);
	self.arrRunDays = data.arrRunDays;
//	self.arrRunDays = data.stnType=='SFZ'?'--':data.arrRunDays;
	self.dptRunDays = data.dptRunDays;
//	self.runDays = data.stnType=='ZDZ'?'--':data.runDays;
	self.stationFlag = data.stationFlag;
//	self.sourceDay = data.arrRunDays;
//	self.targetDay = data.targetDay;
	self.targetDay = data.stnType=='ZDZ'?'--':data.targetDay;
	self.sourceDay = data.stnType=='SFZ'?'--':data.arrRunDays;

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
function TrainRow(data) {  
	var _self = this;  
	_self.importantFlag = ko.observable(data.importantFlag);
	
	_self.importantFlagStr = ko.computed(function () {
		//选线状态0：未选择 1：已选择
		if (_self.importantFlag() == "1") {
			return "<span>是</span>";//"已";
		} else {
		return "";//"未";
		} 
	});
	_self.cmdTrainId = ko.observable(data.cmdTrainId);
	_self.baseTrainId = ko.observable(data.baseTrainId);
	_self.cmdBureau = ko.observable(data.cmdBureau);
	_self.cmdType = ko.observable(data.cmdType);
	_self.cmdTxtMlId = ko.observable(data.cmdTxtMlId);
	_self.cmdTxtMlItemId = ko.observable(data.cmdTxtMlItemId);
	_self.cmdNbrBureau = ko.observable(data.cmdNbrBureau);
	_self.cmdItem = ko.observable(data.cmdItem);
	_self.cmdNbrSuperior = ko.observable(data.cmdNbrSuperior);
	_self.trainNbr = ko.observable(data.trainNbr);
	_self.startStn = ko.observable(data.startStn);
	_self.endStn = ko.observable(data.endStn);
	_self.rule = ko.observable(data.rule);
	_self.selectedDate = ko.observable(data.selectedDate);
	_self.startDateStr = ko.observable(moment(data.startDate).format("YYYYMMDD"));//起始日期仅用于界面显示
	_self.startDate = ko.observable(data.startDate);
	_self.endDate = ko.observable(data.endDate);
	_self.endDateStr = ko.observable(moment(data.endDate).format("YYYYMMDD"));//终止日期仅用于界面显示
	_self.passBureau = ko.observable(data.passBureau=='null'||data.passBureau=='"null"'?"":data.passBureau);
	_self.updateTime = ko.observable(data.updateTime);
	_self.cmdTime = ko.observable(data.cmdTime);
	_self.cmdTimeStr = ko.observable(moment(data.cmdTime).format("YYYYMMDD"));//发令日期仅用于界面显示
	_self.isExsitStn = ko.observable(data.isExsitStn);
	_self.userBureau = ko.observable(data.userBureau);
	_self.selectState = ko.observable(data.selectState);
	_self.createState = ko.observable(data.createState);
	//增加isSelect属性  便于全选复选框事件	默认0=false
	_self.isSelect = ko.observable(0);
//	if(null != data.cmdTrainId){
//		if(data.cmdTrainId == cmdTrainId){
//			_self.isSelect = ko.observable(1);
//		}
//	}
	
	_self.selectStateStr = ko.computed(function () {
		//选线状态0：未选择 ;1：已选择;2:选线后,又再次调整时刻表
		if (_self.selectState() == "0") {
			return "<span class='label label-danger'>未</span>";//"未";
		} else if (_self.selectState() == "1") {
			return "<span class='label label-success'>是</span>";//"已";
		} else if (_self.selectState() == "2") {
			return "<span class='label label-warning'>非</span>";
		}
	});
	

	_self.createStateStr = ko.computed(function () {
		//生成状态0：未生成 1：已生成
		if (_self.createState() == "1") {
			return "<span class='label label-success'>已</span>";//"已";
		} else if (_self.createState() == "0") {
			return "<span class='label label-danger'>未</span>";//"未";
		} else {
			return "";
		}
	});
	

//	_self.routeId = ko.observable(formatString(data.routeId));
//	_self.trainTypeId = ko.observable(formatString(data.trainTypeId));
//	_self.business = ko.observable(formatString(data.business));
//	_self.startBureauId = ko.observable(formatString(data.startBureauId));
//	_self.startStnId = ko.observable(formatString(data.startStnId));
//	_self.endBureauId = ko.observable(formatString(data.endBureauId));
//	_self.endStnId = ko.observable(formatString(data.endStnId));
//	_self.endDays = ko.observable(formatString(data.endDays));
//	
//	// 新增字段
//	_self.sourceNodeStationId = ko.observable(formatString(data.sourceNodeStationId));
//	_self.sourceNodeStationName = ko.observable(formatString(data.sourceNodeStationName));
//	_self.sourceNodeTdcsId = ko.observable(formatString(data.sourceNodeTdcsId));
//	_self.sourceNodeTdcsName = ko.observable(formatString(data.sourceNodeTdcsName));
//	_self.targetNodeStationId = ko.observable(formatString(data.targetNodeStationId));
//	_self.targetNodeStationName = ko.observable(formatString(data.targetNodeStationName));
//	_self.targetNodeTdcsId = ko.observable(formatString(data.targetNodeTdcsId));
//	_self.targetNodeTdcsName = ko.observable(formatString(data.targetNodeTdcsName));
//	
//	
//	
//	_self.id = data.cmdTrainId;
//	_self.name = data.trainNbr; 
	_self.times = ko.observableArray();  
//	_self.selected  = ko.observable();  
//	_self.startBureau = ""; 
//	_self.startStn =  data.startStn; 
//	_self.sourceTime = filterValue(data.startDate); 
//	_self.endStn = data.endStn; 
//	_self.endBureau = ""; 
//	_self.routingBureau = data.passBureau; 
//	_self.runDays = data.endDays;
//	_self.targetTime =  filterValue(data.endDate);
//
//	//增加字段以便套用经由使用
//	_self.routeId =  data.routeId;
//	_self.trainTypeId =  data.trainTypeId;
//	_self.operation =  data.operation;
//	_self.sourceBureauId =  data.sourceBureauId;
//	_self.sourceNodeId =  data.sourceNodeId;
//	_self.targetBureauId =  data.targetBureauId;
//	_self.targetNodeId =  data.targetNodeId;
//	_self.targetTimeScheduleDates =  data.targetTimeScheduleDates;
//	
//	// 新增字段
//	_self.sourceNodeStationId = data.sourceNodeStationId;
//	_self.sourceNodeStationName = data.sourceNodeStationName;
//	_self.sourceNodeTdcsId = data.sourceNodeTdcsId;
//	_self.sourceNodeTdcsName = data.sourceNodeTdcsName;
//	_self.targetNodeStationId = data.targetNodeStationId;
//	_self.targetNodeStationName = data.targetNodeStationName;
//	_self.targetNodeTdcsId = data.targetNodeTdcsId;
//	_self.targetNodeTdcsName = data.targetNodeTdcsName;
	
	_self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			_self.times.push(new TrainTimeRow(n));
		});
	}; 
	
} ; 