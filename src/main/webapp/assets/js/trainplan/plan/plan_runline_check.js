$(function() { 
	heightAuto();
	var appModel = new ApplicationModel();
	ko.applyBindings(appModel); 
	 
	appModel.getFullNodeList();
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

/**
 * 
 */
function ApplicationModel() {
	
	var self = this;
	//列车列表
	self.trains = ko.observableArray();
	

	self.trainAllTimes = ko.observableArray();//保存列车详情时刻所有数据，用于简图复选框点击事件列表值变更来源
	
	self.trainLines = ko.observableArray();
	//交路列表   
	self.gloabBureaus = [];   
	//担当局
	self.searchModle = ko.observable(new searchModle()); 
	self.stnNameTemps = ko.observableArray();	//站名
    self.getFullNodeList = function(){
	    $.ajax({
			url : basePath+"/getFullNodeInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {   
				if (result != null && result != "undefind" && result.code == "0") { 
					if(result.data!=null && result.data!=undefined){
						for(var i=0;i<result.data.length;i++){
							var obj = result.data[i];
							if(obj.name!=null && obj.name!=undefined){
								self.stnNameTemps.push(obj.pinyinInitials+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+obj.name+'['+obj.bureauShortName+']');
							}
						}
					}
				} else {
					showErrorDialog("获取站名失败");
				} 
			},
			error : function() {
				showErrorDialog("获取站名失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
    };
	
	self.currentTrain = ko.observable(); 
	self.isExit =  ko.observable(); 
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

		$("#run_plan_train_times_canvas_dialog").dialog("close");
		$("#run_plan_train_times_update").dialog("close");
		
		
		 $.ajax({
				url : "jbtcx/querySchemes",
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
		 //获取businesses，列车类型
		 $.ajax({
				url : "jbtcx/queryBusinesses",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) { 
							self.searchModle().loadBusinesses(result.data); 
						} 
					} else {
						showErrorDialog("获取列车类型失败");
					} 
				},
				error : function() {
					showErrorDialog("获取列车类型失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
		    });
	    $.ajax({
			url : "plan/getFullStationInfo",
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
			//alert(1);
			//是否出现右边17

			

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
			//alert(5);
			var tableH = $("#right_height table").height();
			var boxH = $("#right_height").height();
			if(tableH <= boxH){
				//alert("没");
				$("#right_height .td_17").removeClass("display");
			}else{
				//alert("有");
				$("#right_height .td_17").addClass("display");
			}
	    });
		
	};  
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().trainNbr(event.target.value.toUpperCase());
	};

	//车站名称自动补全
	self.stnNameTempOnfocusS = function(n, event){
//		console.log($(event.target));
		$(event.target).autocomplete(self.stnNameTemps(),{
			max: 50,    	//列表里的条目数
			width: 200,     //提示的宽度，溢出隐藏
			scrollHeight: 500,   	//提示的高度，溢出显示滚动条
			matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill: false,    	//是否自动填充
			formatResult: function(row) {
				var name =null;
				if(row!=null && row!=undefined){
					var str = new String(row);
					name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));//此处进行了bug修改和优化
				}
//				console.log('name='+name);
				return name;//这个name就是显示到
			}
       }).result(function(event, stnNameTemp, formatted) {
    	   if(stnNameTemp!=null && stnNameTemp!=undefined){
    		   var str = new String(stnNameTemp);
//    		   console.log('str='+str);
    		   var name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));
    		   var bureauShortName = str.substring(str.indexOf("[")+1, str.indexOf("]"));
//    		   console.log('name='+name);
//    		   console.log('bureauShortName='+bureauShortName);
//    		   self.stnNameTemp(name);//赋值
//    		   self.nodeId(id);
    		   self.searchModle().sourceNodeName(name);//赋值
//    		   self.currentBureauJc(bureauShortName);
    	   }
       });
	};

	//车站名称自动补全
	self.stnNameTempOnfocusE = function(n, event){
//		console.log($(event.target));
		$(event.target).autocomplete(self.stnNameTemps(),{
			max: 50,    	//列表里的条目数
			width: 200,     //提示的宽度，溢出隐藏
			scrollHeight: 500,   	//提示的高度，溢出显示滚动条
			matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
			autoFill: false,    	//是否自动填充
			formatResult: function(row) {
				var name =null;
				if(row!=null && row!=undefined){
					var str = new String(row);
					name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));//此处进行了bug修改和优化
				}
//				console.log('name='+name);
				return name;//这个name就是显示到
			}
       }).result(function(event, stnNameTemp, formatted) {
    	   if(stnNameTemp!=null && stnNameTemp!=undefined){
    		   var str = new String(stnNameTemp);
//    		   console.log('str='+str);
    		   var name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));
    		   var bureauShortName = str.substring(str.indexOf("[")+1, str.indexOf("]"));
//    		   console.log('name='+name);
//    		   console.log('bureauShortName='+bureauShortName);
//    		   self.stnNameTemp(name);//赋值
//    		   self.nodeId(id);
    		   self.searchModle().targetNodeName(name);//赋值
//    		   self.currentBureauJc(bureauShortName);
    	   }
       });
	};
	
	self.loadTrains = function(){
		self.trainRows.loadRows();
		commonJsScreenLock();
	};
	
	/**
	 * 交换站.
	 */
	self.exchangeStn = function(){
		var sourceNodeName = self.searchModle().sourceNodeName(); 
		var targetNodeName = self.searchModle().targetNodeName();
		self.searchModle().sourceNodeName(targetNodeName);
		self.searchModle().targetNodeName(sourceNodeName);
	};
	self.loadTrainsForPage = function(startIndex, endIndex) {  
		var trainNbr = self.searchModle().trainNbr();  
		var startBureauShortName = self.searchModle().startBureau();  
		var endBureauShortName = self.searchModle().endBureau();   
		var chart = self.searchModle().chart(); 
		var passBureauShortName = self.searchModle().passBureau();  
		var business = self.searchModle().business(); 
		var fuzzyFlag = self.searchModle().fuzzyFlag();
		var sourceNodeName = self.searchModle().sourceNodeName(); 
		var targetNodeName = self.searchModle().targetNodeName();

		self.currentTrain(null);
		if(chart == null){
			showErrorDialog("请选择方案");
			return;
		} 
		$.ajax({
				url : "jbtcx/queryTrains",
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
					passBureauShortName : passBureauShortName,
					business : business.name,
					rownumstart : startIndex,
					rownumend : endIndex,
//					sourceNodeName : self.searchModle().sourceNodeName(),
//					targetNodeName : self.searchModle().targetNodeName()
					sourceNodeName : sourceNodeName,
					targetNodeName : targetNodeName
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
							if (result.data !=null) {   
								var rows = [];
								$.each(result.data.data,function(n, crossInfo){
									rows.push(new TrainRow(crossInfo));  
								}); 
							//  稍稍th间有点没对齐~
							//	$("#plan_runline_table_trainInfo").freezeHeader();  
								self.trainRows.loadPageRows(result.data.totalRecord, rows);
								
								//是否出现右边17
								var tableH = $("#left_height table").height();
								var boxH = $("#left_height").height();
								if(tableH <= boxH){
									//alert("没");
									$("#left_height .td_17").removeClass("display");
								}else{
									//alert("有");
									$("#left_height .td_17").addClass("display");
								}
								
								
//										var skarr = []; 
//										if(temp.scheduleDto.sourceItemDto != null){
//											skarr.push(temp.scheduleDto.sourceItemDto); 
//										}   
//										
//										if(temp.scheduleDto.routeItemDtos != null && temp.scheduleDto.routeItemDtos.length > 0){
//											$.each(temp.scheduleDto.routeItemDtos,function(i, a){ 
//												skarr.push(a);
//											});
//										} 
//										if(temp.scheduleDto.targetItemDto != null){
//											skarr.push(temp.scheduleDto.targetItemDto);
//										} 
//										
//										skarr.sort(function(a, b){  
//											return a.index - b.index;
//										}); 
//										
//										train.loadTimes(skarr); 
							 
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
	 
	self.getTrainInfos = function(row) {
		$.ajax({
			url : "jbtcx/toUpdateJbtTrainNewIframe",//此处地址需要更改
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				trainLineId:row.id
			}),
			success : function(data) {
				
			},
			error : function() {
			},
			complete : function(){
				
			}
	    });
		$("#run_plan_train_times_update").dialog("open");
	}
	self.showTrainTimes = function(row) {
		self.currentTrain(row);
		self.trainAllTimes.remove(function(item){
			return true;
		});
		self.trainLines.remove(function(item){
			return true;
		});
		if(row.times().length > 0){
			//alert(1);
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
			}) ;
			//是否出现右边17
			var tableH = $("#right_height table").height();
			var boxH = $("#right_height").height();
			if(tableH <= boxH){
				//alert("没");
				$("#right_height .td_17").removeClass("display");
			}else{
				//alert("有");
				$("#right_height .td_17").addClass("display");
			}
			 
		}else{
			//alert(2);
			$.ajax({
				url : "jbtcx/queryTrainTimes",
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
						//是否出现右边17
						var tableH = $("#right_height table").height();
						var boxH = $("#right_height").height();
						if(tableH <= boxH){
							//alert("没");
							$("#right_height .td_17").removeClass("display");
						}else{
							//alert("有");
							$("#right_height .td_17").addClass("display");
						}


					} else {
						showErrorDialog("获取列车时刻表失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车时刻表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		};
		/*
		$.ajax({
			url : "jbtcx/queryTrainTimesNewIframe?trainId="+row.id,
			cache : false,
			type : "GET",
		}); 
	
		$("#train_time_dlg").find("iframe").attr("src", "jbtcx/queryTrainTimesNewIframe?trainId=" + row.id);
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
		*/
		if(!$("#run_plan_train_times_canvas_dialog").is(":hidden")){
			if(self.isExit!=null && self.isExit!=undefined){
				$("#run_plan_train_times_canvas_dialog").find("iframe").attr("src", basePath+"/jbtcx/getTrainTimeCanvasPage?planTrainId=" + self.currentTrain().id+"&trainNbr="+self.currentTrain().name);
//				self.isExit.attr('titile',self.currentTrain().name);
				$(".panel8-title.panel8-with-icon").html("列车运行时刻图 &nbsp;&nbsp;&nbsp;车次："+self.currentTrain().name);
			
			}else{
				self.showTrainTimeCanvas();
			}
			
			
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
		self.isExit = $('#run_plan_train_times_canvas_dialog').dialog({title: "列车运行时刻图 &nbsp;&nbsp;&nbsp;车次："+self.currentTrain().name, autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#run_plan_train_times_canvas_dialog").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#run_plan_train_times_canvas_dialog').height();
				var WW = $('#run_plan_train_times_canvas_dialog').width();
				var CW = $('#div_canvas').width();
				
                if (isChrome) {
                	iframeBox.css({ "height": (WH) + "px"});
                	iframeBox.css({ "min-height": (WH) + "px"});
                	iframeBox.attr("width", (CW));

                }else{
                	iframeBox.css({ "height": (WH)  + "px"});
                	iframeBox.css({ "min-height": (WH) + "px"});
                	iframeBox.attr("width", (CW));
                }
		}});
	};
}

function searchModle(){
	
	self = this;   
	
	self.startBureaus = ko.observableArray(); 
	
	self.endBureaus = ko.observableArray(); 
	
	self.passBureaus = ko.observableArray(); 
	
	self.charts = ko.observableArray(); 
	
	self.businesses = ko.observableArray(); //列车类型
 
	self.startBureau = ko.observable();
	
	self.endBureau = ko.observable();
	
	self.passBureau = ko.observable();//途径局
	
	self.trainNbr = ko.observable(); 
	
	self.chart = ko.observable(); 
	
	self.business =  ko.observable(); //列车类型
	
	self.fuzzyFlag = ko.observable(1); 
	
	self.sourceNodeName = ko.observable();
	self.targetNodeName = ko.observable(); 
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {   
			self.startBureaus.push(new BureausRow(bureaus[i]));  
			self.endBureaus.push(new BureausRow(bureaus[i]));
			self.passBureaus.push(new BureausRow(bureaus[i]));
		} 
	}; 
	
	self.loadChats = function(charts){   
		for ( var i = 0; i < charts.length; i++) {   
			self.charts.push({"chartId": charts[i].schemeId, "name": charts[i].schemeName});  
		} 
	}; 
	
	self.loadBusinesses = function(businesses){   
		var types = new Array();
		types.unshift("");
		for ( var i = 0; i < businesses.length; i++) {  
			types.push(businesses[i]);
			// self.businesses.push({"businessId": businesses[i].businessId, "name": businesses[i].businessName});  
		} 
		for ( var i = 0; i < types.length; i++) {  
			self.businesses.push({"businessId": types[i].businessId, "name": types[i].businessName});  
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
	self.sourceTime = data.stationFlag=="SFZ"?'--':filterValue(data.arrTime);
	self.targetTime = data.stationFlag=="ZDZ"?'--':filterValue(data.dptTime);
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);
	self.arrRunDays = data.stationFlag=="SFZ"?'--':data.arrRunDays;
	
	self.runDays = data.stationFlag=="ZDZ"?'--':data.runDays;
	self.stationFlag = data.stationFlag;
	self.kyyy = (data.stationFlag=="SFZ" || data.stationFlag=="ZDZ" || data.jobs.indexOf('客运营业')>0)?'是':'--';
	var jobsText = data.jobs;
	jobsText = jobsText.replace('<客运营业>','').replace('<经由>','');
	jobsText = jobsText.substring(1,jobsText.length-1);
	jobsText = jobsText.split("><").join(",");
	self.jobsText = jobsText;
	
	self.platForm = data.platForm;
	self.sourceTrainNbr = data.arrTrainNbr;
	self.targetTrainNbr = data.dptTrainNbr;
	
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
	self.expirationDateStart = data.sourceTime;
	self.expirationDateEnd = data.targetTime=='20991231'?'令止':data.targetTime;
	self.expirationDate = self.expirationDateStart + "--" + self.expirationDateEnd;
	self.expirationDate = ko.computed(function(){
		if(self.expirationDateStart==null){
			return ;
		}else{
			return self.expirationDateStart + "--" + self.expirationDateEnd;
		}
	});
	 
	self.targetTime =  filterValue(data.endTimeStr); 
	
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			self.times.push(new TrainTimeRow(n));
		});
	}; 
	
} ; 

function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#left_height").css("height",WH-161+"px");
	$("#right_height").css("height",WH-101+"px");
	
	
};