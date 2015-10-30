$(function() { 
	heightAuto();
	var appModel = new ApplicationModel();
	ko.applyBindings(appModel); 
	 
	appModel.init();   
});  


function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#left_height").css("height",WH-180+"px");
	$("#right_height").css("height",WH-77+"px");
	

	
};



var isLineReloadStatus = [{"value": "1", "text": "已落成"},{"value": "0", "text": "未落成"}];
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
	
	var self = this;
	//列车列表
	self.trains = ko.observableArray();
	
self.trainAllTimes = ko.observableArray();
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
		$("#run_plan_train_times_canvas_dialog2").dialog("close");
		$("#runplan_input_startDate").datepicker();
		$("#runplan_input_endDate").datepicker();
		self.searchModle().planStartDate(self.currdate());
		self.searchModle().planEndDate(self.currdate());
		 
	    $.ajax({
			url : basePath + "/plan/getFullStationInfo",
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
		self.trainRows.loadRows();
		commonJsScreenLock();
	};
	self.loadTrainsForPage = function(startIndex, endIndex) {   
		var startBureauShortName = self.searchModle().startBureau();  
		var	endBureauShortName = self.searchModle().endBureau();  
		var chart = self.searchModle().chart(); 
		var fuzzyFlag = self.searchModle().fuzzyFlag();
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate = $("#runplan_input_endDate").val(); 
		if(startDate!=null && startDate!=undefined && endDate!=null && endDate!=undefined){
			if(startDate > endDate){
				showWarningDialog("开始日期不能大于截止日期!");
				return ;
			}
		}
		var trainNbr = self.searchModle().trainNbr();
		var likeFlag = self.searchModle().likeFlag();
		var lineReloadStatus = self.searchModle().lineReloadStatus();
		
		
		
		$.ajax({
				url : basePath + "/jbtcx/queryTrainLines",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					startBureauShortName : startBureauShortName, 
					endBureauShortName : endBureauShortName, 
					startDate : startDate.replace(/-/g, ''), 
					endDate : endDate.replace(/-/g, ''), 
					trainNbr : trainNbr,
					likeFlag : likeFlag,
					fuzzyFlag : fuzzyFlag,
	             //   fuzzyFlag(), event:{change: fuzzyChange}
					lineReloadStatus : lineReloadStatus,
					rownumstart : startIndex,
					rownumend : endIndex,
					highline_flag : 1
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
						showErrorDialog("获取车底失败");
					};
				},
				error : function() {
					showErrorDialog("获取车底失败");
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
			 
		}else{
			commonJsScreenLock();
			$.ajax({
				url : basePath + "/jbtcx/queryTrainLineTimes",
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
				},
				error : function() {
					showErrorDialog("获取列车时刻表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		}
      if(!$("#run_plan_train_times_canvas_dialog2").is(":hidden")){
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

		///alert(basePath+"/jbtcx/getTrainTimeCanvasPagejy?planTrainId=" + self.currentTrain().id+"&trainNbr="+self.currentTrain().name)
	
		$("#run_plan_train_times_canvas_dialog2").find("iframe").attr("src", basePath+"/jbtcx/getTrainTimeCanvasPagegt?planTrainId=" + self.currentTrain().id+"&trainNbr="+self.currentTrain().name);
	
	   $('#run_plan_train_times_canvas_dialog2').dialog({
		   		title: "列车运行时刻图 &nbsp;&nbsp;&nbsp;车次："+self.currentTrain().name,
		   		autoOpen: true,
		   		modal: false, 
		   		draggable: true, 
		   		resizable:true,
			    onResize:function() {
				var iframeBox = $("#run_plan_train_times_canvas_dialog2").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#run_plan_train_times_canvas_dialog2').height();
				var WW = $('#run_plan_train_times_canvas_dialog2').width();
                if (isChrome) {
                	iframeBox.css({ "height": (WH) + "px"});
                	iframeBox.css({ "min-height": (WH) + "px"});
                	//iframeBox.attr("width", (WW));

                }else{
                	iframeBox.css({ "height": (WH)  + "px"});
                	iframeBox.css({ "min-height": (WH) + "px"});
                	//iframeBox.attr("width", (WW));
                }
		}});
	};
}

function searchModle(){
	
	self = this;   
	
	self.startBureaus = ko.observableArray(); 
	
	self.endBureaus = ko.observableArray(); 
	
	self.charts = ko.observableArray(); 
 
	self.startBureau = ko.observable();
	
	self.endBureau = ko.observable();
	
	self.planStartDate = ko.observable();
	
	self.planEndDate = ko.observable();
	
	self.trainNbr = ko.observable(); 
	
	self.chart = ko.observable(); 
	
	self.likeFlag = ko.observable(); //模糊查询
	
	self.lineReloadStatuss = isLineReloadStatus; //落成状态

	self.lineReloadStatus = ko.observable();
	
	self.fuzzyFlag = ko.observable(1);
	
	self.checklev1breau = ko.observable();


	self.redundance  =ko.observable();




















	

	/*self.redundance = ko.computed(function(){
		switch (data.redundance) {
			case 1: 
				return "<span class='label label-success'>已</span>";//"已";
				break;
			
			default: 
				return "<span class='label label-danger'>未</span>";//"未";
				break;
		}
	});*/
	
	

     self.source_time_schedule_dates = ko.observable();
	
	self.target_time_schedule_dates = ko.observable();
	



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
	self.sourceTime = data.stationFlag=='SFZ'?'--':moment(data.arrTime).format("MMDD HH:mm");
	self.targetTime = data.stationFlag=='ZDZ'?'--':moment(data.dptTime).format("MMDD HH:mm");
	self.stepStr = GetDateDiff(data); 

	self.trackName = filterValue(data.trackName);  
	self.runDays = data.runDays;
    self.stationFlag = data.stationFlag;

    self.sourceDay =  data.stationFlag=='SFZ'?'--':data.sourceDay;
    self.targetDay = data.stationFlag=='ZDZ'?'--':data.targetDay;




	 
}; 
function GetDateDiff(data)
{ 
	if(data.childIndex == 0)
		return "";
	else if(data.dptTime == '-'){
		return "";
	} 
	var startTime = new Date(data.arrTime);
	var endTime = new Date( data.dptTime);  
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
	self.likeFlag = data.likeFlag; 
	self.lineReloadStatus = data.lineReloadStatus; 
	 


	self.targetTime =  filterValue(data.endTimeStr); 
	
	self.checklev1breau=data.checklev1breau;

	
	self.checklev1breauStr = ko.computed(function () {
		//冗余0：无冗余1
		if (self.checklev1breau == "1") {
			return "<span class='label label-success'>已</span>";//"已";
		} else if (self.checklev1breau == "0") {
			return "";//"未";
		} else {
			return "";
		}
	}); 
	
	


	self.redundance =data.redundance;
	


    self.source_time_schedule_dates = data.source_time_schedule_dates;
	

	self.target_time_schedule_dates = data.target_time_schedule_dates;
	





	self.redundanceStr = ko.computed(function () {
		//冗余0：无冗余1
		if (self.redundance == "0") {
			return "<span class='label label-danger'>是</span>";//"已";
		} else if (self.redundance == "1") {
			return "";//"未";
		} else {
			return "";
		}
	}); 
	
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			self.times.push(new TrainTimeRow(n));
		});
	}; 
	


} ; 
