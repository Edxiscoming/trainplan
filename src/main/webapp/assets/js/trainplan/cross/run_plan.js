$(function() { 
	
	var cross = new CrossModel();
	ko.applyBindings(cross); 
	
	cross.init();   
});

var highlingFlags = [{"value": "0", "text": "普速"},{"value": "1", "text": "高铁"},{"value": "2", "text": "混合"}];
var checkFlags = [{"value": "1", "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "1", "text": "已"},{"value": "0", "text": "未"}];
var highlingrules = [{"value": "1", "text": "平日"},{"value": "2", "text": "周末"},{"value": "3", "text": "高峰"}];
var commonlinerules = [{"value": "1", "text": "每日"},{"value": "2", "text": "隔日"}];
 


var gloabBureaus = [];

function CrossModel() {
	var self = this;
		//列车列表
	self.trains = ko.observableArray();
	
	self.stns = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0);
	
	self.trainPlans = ko.observableArray([{"trainNbr": "G1", "runPlans": [{"day": "05-26", "runFlag": "1"},{"day": "05-27", "runFlag": "1"},
	                                                                      {"day": "05-26", "runFlag": "1"},{"day": "05-26", "runFlag": "1"},
	                              	                                      {"day": "05-26", "runFlag": "1"},{"day": "05-26", "runFlag": "1"},
	                              	                                      {"day": "05-26", "runFlag": "1"},{"day": "05-26", "runFlag": "1"},
	                            	                                      {"day": "05-26", "runFlag": "1"}]},
	                            	      {"trainNbr": "G2", "runPlans": [{"day": "05-26", "runFlag": "1"},{"day": "05-27", "runFlag": "1"},
	                            	                                      {"day": "05-26", "runFlag": "1"},{"day": "05-26", "runFlag": "1"},
	                            	                              	      {"day": "05-26", "runFlag": "1"},{"day": "05-26", "runFlag": "1"},
	                            	                              	      {"day": "05-26", "runFlag": "1"},{"day": "05-26", "runFlag": "1"},
	                            	                            	      {"day": "05-26", "runFlag": "1"}]}]);
	
	self.planDays = ko.observableArray([{"day": "05-26"},{"day": "05-27"},{"day": "05-26"},{"day": "05-26"},
	                                    {"day": "05-26"},{"day": "05-26"},{"day": "05-26"},{"day": "05-26"},
	                                    {"day": "05-26"}]);
	
	
	self.gloabBureaus = [];  
	
	self.runPlanCanvasPage = new RunPlanCanvasPage(self);
	
	self.currentTrain = ko.observable();
	
	//担当局
	self.searchModle = ko.observable(new searchModle());
	
	
	self.trainRunPlanChange = function(row, event){ 
//		console.log(row);
//		console.log(event.target.name);
//		console.log("trainRunPlanChange test");
	};
	
	self.dragRunPlan = function(n,event){
		$(event.target).dialog("open");
		
	};
	
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.crossRows.rows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0);
			}else{
				crossRow.selected(1); 
			} 
		}); 
	};
	
	self.loadStns = function(stns){ 
		self.stns.remove(function(item) {
			return true;
		});   
		if(stns){
			 $.each(stns, function(z, n){
				 self.stns.push(new TrainTimeRow(n));
			 }); 
		};
	};
	self.setCurrentTrain = function(train){
		self.currentTrain(train); 
	};
	
	self.setCurrentCross = function(cross){
		self.currentCross(cross);
		if(self.searchModle().showCrossMap() == 1){
			$("#cross_map_dlg").find("iframe").attr("src", "cross/provideCrossChartData?crossId=" + cross.crossId);
		}
	};
	self.clearData = function(){
		 self.crossRows.clear(); 
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
		 
		 self.trains.remove(function(item){
			return true;
		 });  
		 self.currentTrain = ko.observable();
	};
	self.selectCross = function(row){
//		self.crossAllcheckBox();
		if(row.selected() == 0){
			self.crossAllcheckBox(1);
			$.each(self.crossRows.rows(), function(i, crossRow){ 
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
	self.filterCrosses = function(){  
		 var filterTrainNbr = self.searchModle().filterTrainNbr();
		 filterTrainNbr = filterTrainNbr || filterTrainNbr != "" ? filterTrainNbr.toUpperCase() : "all";
		 if(filterTrainNbr == "all"){
			 $.each(self.crossRows.rows(),function(n, crossRow){
				  crossRow.visiableRow(true);
			  });
		 }else{
			 $.each(self.crossRows.rows(),function(n, crossRow){
				 if(crossRow.crossName().indexOf(filterTrainNbr) > -1){
					 crossRow.visiableRow(true);
				 }else{
					 crossRow.visiableRow(false);
				 } 
			  }); 
		 };
	};
	
	self.loadCrossRunPaln =function(){
		
	};
	
	self.uploadCrossFile = function(){ 
	        //starting setting some animation when the ajax starts and completes
		    var chart = self.searchModle().chart();
		    var startDay = $("#cross_start_day").val().replace(/-/g, "");
		    if(chart == null){
		    	showErrorDialog("请选择一个方案");
		    	$("#file_upload_dlg").dialog("close"); 
		    	return;
		    }  
		    if($("#fileToUpload").val() == null || $("#fileToUpload").val() == ""){
		    	showErrorDialog("没有可导入的文件"); 
		    	return;
		    }
		    $("#loading").show();
		    $("#btn_fileToUpload").attr("disabled", "disabled");
	        $.ajaxFileUpload
	        ({
                url:'cross/fileUpload',
                secureuri:false,
                fileElementId:'fileToUpload',
                type : "POST",
                dataType: 'json', 
                timeout:4000,
                data:{
                	chartId:chart.chartId,
                	startDay:startDay,
                	chartName:chart.name
                },
                success: function (data, status)
                {  
                	showSuccessDialog("上传成功");
                	$("#loading").hide();
                	$("#btn_fileToUpload").removeAttr("disabled");
                },
                error: function(result){
                	showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
                	$("#loading").hide();
                	$("#btn_fileToUpload").removeAttr("disabled");
                }
            });  
	        return true;
	} ;
	
	 
	
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow({"crossId":"",
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
	
	self.get40Date = function(){
		var d = new Date();
		d.setDate(d.getDate() + 40);
		
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year+"-"+month+"-"+days;
	};
	
	
	
	self.init = function(){  
		//self.gloabBureaus = [{"shortName": "上", "code": "S"}, {"shortName": "京", "code": "B"}, {"shortName": "广", "code": "G"}];
		//self.searchModle().loadBureau(self.gloabBureaus); 
		//self.searchModle().loadChats([{"name":"方案1", "chartId": "1234"},{"name":"方案2", "chartId": "1235"}])
//		$("#cross_map_dlg").dialog({
//		    onClose:function(){
//		    		self.searchModle().showCrossMap(0);
//		       }
//		   });
//		
//		
//		$("#file_upload_dlg").dialog("close"); 
//		$("#cross_train_time_dlg").dialog("close");
//		$("#cross_map_dlg").dialog("close"); 
//		$("#cross_train_dlg").dialog("close");
//		$("#cross_train_time_dlg").dialog("close"); 
//		$("#cross_start_day").datepicker();
//		
		
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
					 yScale : currentYScale			//y轴缩放比例
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
				 yScale : currentYScale			//y轴缩放比例
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
					 yScale : currentYScale			//y轴缩放比例
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
				 yScale : currentYScale			//y轴缩放比例
			 });
		}); 
		
//		runPlanCanvasPage = new RunPlanCanvasPage(cross);
		self.runPlanCanvasPage.initPage(); 
		
		self.searchModle().startDay(self.currdate()); 
		
		self.searchModle().planStartDate(self.currdate());
		
		self.searchModle().planEndDate(self.get40Date());
		
		commonJsScreenLock();
		var initFlag = 0;
		//获取当期系统日期 
		 $.ajax({
				url : "../plan/getSchemeList",
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
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					} 
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){
					initFlag++;
					if(initFlag == 2){
						commonJsScreenUnLock();
					}
					
				}
		    }); 
		 
	    $.ajax({
			url : "../plan/getFullStationInfo",
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
							gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym});
						});
					} 
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				} 
			},
			error : function() {
				showErrorDialog("接口调用失败");
			},
			complete : function(){
				initFlag++;
				if(initFlag == 2){
					commonJsScreenUnLock();
				}
			}
	    });
		
		
	};  
	
	self.loadCrosses = function(){
		self.crossRows.loadRows();
	};
	self.loadCrosseForPage = function(startIndex, endIndex) {  
		commonJsScreenLock();
		/* $.each(crosses,function(n, crossInfo){
			var row = new CrossRow(crossInfo);
			self.crossRows.push(row);
			rowLookup[row.crossName] = row;
		});  */
		var highlingFlag = self.searchModle().highlingFlag();
		var trainNbr = self.searchModle().filterTrainNbr(); 
		var checkFlag = self.searchModle().checkFlag();
		var unitCreateFlag = self.searchModle().unitCreateFlag();
		var chart = self.searchModle().chart();

		if(chart == null){
			showErrorDialog("请选择方案!");
			//return;
		} 
		$.ajax({
				url : "runPlan/getPlanCross",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					chartId : chart == null ? null: chart.chartId,
					trainNbr : trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex
				}),
				success : function(result) {    
 
					if (result != null && result != "undefind" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){  
							$.each(result.data.data,function(n, crossInfo){
								rows.push(new CrossRow(crossInfo));  
							});   
							self.crossRows.loadPageRows(result.data.totalRecord, rows);
						} 
						
						 $("#cross_table_crossInfo").freezeHeader(); 
						 
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					};
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};

	self.crossRows = new PageModle(50, self.loadCrosseForPage);
	
	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau());
	};
	 
	self.showUploadDlg = function(){
		
		$("#file_upload_dlg").dialog("open");
//		var diag = new Dialog();
//		diag.Title = "上传对数文件";
//		diag.Width = 400;
//		diag.Height = 200;
//		diag.InnerHtml = $("#file_upload_dlg").html();
//		//diag.URL = "javascript:void(document.write(\'这是弹出窗口中的内容\'))";
//		diag.show();
	};
	
	self.showCrossMapDlg = function(){ 
		if(!self.currentCross().crossId || self.currentCross().crossId == ''){
			return;
		}
		var crossId = self.currentCross().crossId; 
		if(self.searchModle().showCrossMap() == 0){
			
			$("#cross_map_dlg").find("iframe").attr("src", "cross/provideCrossChartData?crossId=" + crossId);
			$('#cross_map_dlg').dialog({ title: self.currentCross().crossName(), autoOpen: true, height:600,width: 800, modal: false, draggable: false, resizable:true });
		};
	};  
	
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	
	self.showCrossTrainTimeDlg = function(){
		
		$("#cross_train_time_dlg").dialog("open");
	};
	
	self.showTrainTimes = function(row) {
		self.currentTrain(row);
		self.runPlanCanvasPage.reDrawByTrainNbr(row.trainNbr);
		self.stns.remove(function(item){
			return true;
		});
		if(row.times().length > 0){ 
			$.each(row.times(), function(i, n){
				self.stns.push(n); 
			}) ;
			 
		}else{
			$.ajax({
				url : "../jbtcx/queryTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({   
					trainId : row.baseTrainId
				}),
				success : function(result) {  
					if (result != null && result != "undefind" && result.code == "0") {  
						row.loadTimes(result.data);  
						$.each(row.times(), function(i, n){
							self.stns.push(n); 
						});
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					};
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		}
		
	};  
	
	self.deleteCrosses = function(){
		
		showConfirmDiv("提示", " 确定要执行删除操作?");
		var crossIds = "";
		var crosses = self.crossRows.rows(); 
		var delCrosses = [];
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].crossId; 
				delCrosses.push(crosses[i]); 
			}  
		}   
//		$.ajax({
//			url : "cross/deleteCrosses",
//			cache : false,
//			type : "POST",
//			dataType : "json",
//			contentType : "application/json",
//			data :JSON.stringify({  
//				crossIds : crossIds
//			}),
//			success : function(result) {     
//				if(result.code == 0){
//					$.each(delCrosses, function(i, n){ 
//						self.crossRows.rows.remove(n); 
//					});
//					showSuccessDialog("删除交路单元成功"); 
//				}else{
//					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
//				}
//			}
//		}); 
	};
	
	self.createUnitCrossInfo = function(){ 
		commonJsScreenLock();
		var crossIds = "";
		var delCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].crossId;
				delCrosses.push( crosses[i]);
			}
		} 
		 $.ajax({
				url : "cross/completeUnitCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(delCrosses, function(i, n){ 
							n.unitCreateFlag("1");
						});
						showSuccessDialog("生成交路单元成功");
					}else{
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
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
	
	self.checkCrossInfo = function(){
		commonJsScreenLock();
		var crossIds = "";
		var delCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1 && crosses[i].checkFlag()){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].crossId;
				delCrosses.push(crosses[i]);
			}
		} 
		 $.ajax({
				url : "../cross/checkCorssInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(delCrosses, function(i, n){ 
							n.checkFlag("1");
						});
						showSuccessDialog("审核成功");
					}else{
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
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
	
	self.createCrossMap = function(row){
		 $.ajax({
				url : "../cross/createCrossMap",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossId : row.crossId  
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {   
							canvasData = {
									grid: $.parseJSON(result.data.gridData),
									jlData: $.parseJSON(result.data.myJlData)
							};
							lineList = [];
							jlList = [];
							self.runPlanCanvasPage.drawChart({startX:60, yScale: 2});
							
						}
						 
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
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
	self.showTrains = function(row) {  
		commonJsScreenLock();
		self.createCrossMap(row);
		self.trains.remove(function(item) {
			return true;
		});   
		 $.ajax({
				url : "../cross/getCrossTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossId : row.crossId  
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {  
							self.setCurrentCross(new CrossRow(result.data[0].crossInfo));
//							self.currentCross(new CrossRow(result.data[0].crossInfo));
							if(result.data[0].crossTrainInfo != null){
								$.each(result.data[0].crossTrainInfo,function(n, crossInfo){
									var row = new TrainRow(crossInfo);
									self.trains.push(row); 
								});
							}
						}
						 
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
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
	
	self.planStartDate = ko.observable();
	
	self.planEndDate = ko.observable();
	
	self.bureaus = ko.observableArray();
	
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	 
	self.highlingFlags = highlingFlags;
	
	self.unitCreateFlags = unitCreateFlags; 
	
	self.filterCheckFlag = ko.observable(0);
	
	self.filterUnitCreateFlag = ko.observable(0);
		
	self.checkFlags = checkFlags;
	
	self.highlingFlag = ko.observable();
	
	self.checkFlag = ko.observable(); 
	 
	self.unitCreateFlag = ko.observable(); 
	
	self.bureau = ko.observable();
	 
	self.chart =  ko.observable();
	
	self.startDay = ko.observable();
	
	self.startBureau = ko.observable();
	
	self.filterTrainNbr = ko.observable();
	
	self.showCrossMap = ko.observable(0);
	
	self.shortNameFlag = ko.observable(1);
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {  
			self.bureaus.push(new BureausRow(bureaus[i])); 
			self.startBureaus.push(new BureausRow(bureaus[i]));  
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
	//方案ID 
}

function CrossRow(data) {
	var self = this; 
	self.id = data.chartId;
	self.chartName = data.crossName; 
	//方案ID 
}

 

function CrossRow(data) {
	var self = this; 
	
	self.visiableRow =  ko.observable(true); 
	
	self.selected =  ko.observable(0);
	
	self.crossId = data.crossId; 
	
	self.shortNameFlag =  ko.observable(true);
	
	self.crossName = ko.observable(data.crossName);  
	
	self.shortName = ko.computed(function(){
		trainNbrs = data.crossName.split('-');
		if(trainNbrs.length > 2){
			return trainNbrs[0] + '-......-' + trainNbrs[trainNbrs.length-1];
		}else{
			return data.crossName;
		}
	});  
	
	self.checkFlag = ko.observable(data.checkFlag);
	
	self.unitCreateFlag = ko.observable(data.unitCreateFlag);
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
	self.crossSection = ko.observable(data.crossSection);
	self.throughline = ko.observable(data.throughline);
	self.startBureau = ko.observable(data.startBureau); 
	//担当局 
	self.tokenVehBureau = ko.observable(data.tokenVehBureau); 
	
	self.tokenVehDept = ko.observable(data.tokenVehDept);
	self.tokenVehDepot = ko.observable(data.tokenVehDepot);
	self.tokenPsgBureau = ko.observable(data.tokenPsgBureau);
	self.tokenPsgDept = ko.observable(data.tokenPsgDept);
	self.tokenPsgDepot = ko.observable(data.tokenPsgDepot);
	self.locoType = ko.observable(data.locoType);
	self.crhType = ko.observable(data.crhType);
	self.elecSupply = ko.observable(data.elecSupply);
	self.dejCollect = ko.observable(data.dejCollect);
	self.airCondition = ko.observable(data.airCondition);
	self.note = ko.observable(data.note);  
};

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
}

function TrainRow(data) {
	var self = this; 
	self.crossTainId  = data.crossTainId;//BASE_CROSS_TRAIN_ID
	self.crossId = data.crossId;//BASE_CROSS_ID
	self.trainSort = data.trainSort;//TRAIN_SORT
	self.baseTrainId = data.baseTrainId;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	//self.startBureau = data.startBureau;//START_BUREAU 
	self.times = ko.observableArray(); 
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			self.times.push(new TrainTimeRow(n));
		});
	}; 
	
	self.startBureau = ko.computed(function(){
		for(var i = 0; i < gloabBureaus.length; i++){
			if(gloabBureaus[i].code == data.startBureau){ 
				return gloabBureaus[i].shortName;
			}
		} 
	});
	self.endStn = data.endStn;//END_STN
	//self.endBureau = data.endBureau;//END_BUREAU
	self.endBureau = ko.computed(function(){
		for(var i = 0; i < gloabBureaus.length; i++){
			if(gloabBureaus[i].code == data.endBureau){ 
				return gloabBureaus[i].shortName;
			}
		} 
	});
	self.dayGap = data.dayGap;//DAY_GAP
	self.alertNateTrainNbr = data.alertNateTrainNbr;//ALTERNATE_TRAIN_NBR
	self.alertNateTime =  ko.computed(function(){
		 return data.alertNateTime == null ? "": data.alertNateTime.substring(5).replace(/-/g, "").substring(0, data.alertNateTime.substring(5).replace(/-/g, "").length - 3);
	});//ALTERNATE_TIME
	//self.spareFlag = data.spareFlag;//SPARE_FLAG
	self.spareFlag = ko.computed(function(){ 
		switch (data.spareFlag) {
			case 0:
				return "停运";
				break;
			case 1:
				return "开行";
				break;
			case 2:
				return "备用";
				break;
			default:
				break;
		}
	});
	 
	self.spareApplyFlage =  ko.computed(function(){  
		return data.spareApplyFlage == 1 ? "是" : "否";
	});
	//SPARE_APPLY_FLAG
	//self.highlineFlag = data.highlineFlag ;//HIGHLINE_FLAG 
	self.highlineFlag = ko.computed(function(){  
			switch (data.highlineFlag) {
				case 0:
					return "普速";
					break;
				case 1:
					return "高铁";
					break;
				case 2:
					return "混合";
					break;
				default:
					break;
			}
	});
//	var highlingFlags = [{"value": -1, "text": ""},{"value": 0, "text": "普线"},{"value": 1, "text": "高线"},{"value": 2, "text": "混合"}];
//	var sureFlags = [{"value": -1, "text": ""},{"value": "0", "text": "已审核"},{"value": 1, "text": "未审核"}];
//	var highlingrules = [{"value": 1, "text": "平日"},{"value": 2, "text": "周末"},{"value": 3, "text": "高峰"}];
//	var commonlinerules = [{"value": 1, "text": "每日"},{"value": 2, "text": "隔日"}];
	self.highlineRule = ko.computed(function(){  
			switch (data.highlineRule) {
				case 1: 
					return "平日";
					break;
				case 2:
					return "周末";
					break;
				case 3:
					return "高峰";
					break;
				default:
					break;
		   }
	});
	self.commonLineRule = ko.computed(function(){ 
		switch (data.commonLineRule) {
			case 1:
				return "每日";
				break;
			case 2:
				return "隔日";
				break; 
			default:
				break;
		}
	});
	self.appointWeek = data.appointWeek;//APPOINT_WEEK
	self.appointDay = data.appointDay;//APPOINT_DAY 
	
	self.otherRule = self.appointWeek == null ? "" : self.appointWeek + " " + self.appointDay == null ? "" : self.appointDay;

} ;
function filterValue(value){
	return value == null || value == "null" ? "--" : value;
};

function TrainTimeRow(data) { 
	var self = this; 
	self.index = data.childIndex + 1;
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.arrTime = data.arrTime != null ? data.arrTime.length > 8 ? data.arrTime.replace(/-/g,"").substring(4, 14) : data.arrTime: "--";
	self.dptTime = data.dptTime != null ? data.dptTime.length > 8 ? data.dptTime.replace(/-/g,"").substring(4, 14) : data.dptTime: "--";
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);  
	self.runDays = data.runDays; 
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
function openLogin() {
	$("#file_upload_dlg").dialog("open");
}
 