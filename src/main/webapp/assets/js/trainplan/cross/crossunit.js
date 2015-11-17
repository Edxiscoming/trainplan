$(function() { 
	heightAuto();
	var cross = new CrossModel();
	ko.applyBindings(cross); 
	 
	cross.init();   
});

function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#crossInfo_Data").css("height",WH-286+"px");
	$("#train_information").css("height",WH-404+"px");	
};


var highlingFlags = [{"value": 0, "text": "普速"},{"value": 1, "text": "高铁"},{"value": 2, "text": "混合"}];
var checkFlags = [{"value": '1', "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "1", "text": "已"},{"value": 0, "text": "未"}];
var highlingrules = [{"value": 1, "text": "平日"},{"value": 2, "text": "周末"},{"value": 3, "text": "高峰"}];
var commonlinerules = [{"value": 1, "text": "每日"},{"value": 2, "text": "隔日"}];
 
var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";

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

function CrossModel() {
	var self = this;
		//列车列表
	self.trains = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0);
	
	self.gloabBureaus = [];  
	
	self.currentTrain =  ko.observable();
	
	self.currentTrainInfoMessage = ko.observable("");
	
	self.currentTrainId = ko.observable("");
	
	self.times = ko.observableArray();
	
	self.simpleTimes = ko.observableArray();
	
	//担当局
	self.searchModle = ko.observable(new searchModle()); 
	self.defaultCrossTemp = {"crossId":"",
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
		"appointPeriod":"",
		"locoType":"",
		"crhType":"",
		"elecSupply":"",
		"dejCollect":"",
		"airCondition":"",
		"note":"", 
		"createPeople":"", 
		"createPeopleOrg":"",  
		"createTime":""};
	self.clearData = function(){
		 self.crossRows.clear(); 
		 self.currentCross(new CrossRow(self.defaultCrossTemp)); 
		 self.times.remove(function(item){
			return true;
		 });
		 
		 self.trains.remove(function(item){
			return true;
		 });  
		 self.currentTrain = ko.observable();
	};
	
	self.setCurrentTrain = function(row){
		self.currentTrain(row);
		self.currentTrainInfoMessage("车次：" + row.trainNbr + "&nbsp;&nbsp;&nbsp;" + row.startStn + "——" + row.endStn);
		self.currentTrainId(row.baseTrainId);
		self.times.remove(function(item){
			return true;
		});
		self.simpleTimes.remove(function(item){
			return true;
		});
		if(row.times().length > 0){ 
			$.each(row.times(), function(i, n){
				self.times.push(n); 
			}) ;
			$.each(row.simpleTimes(), function(i, n){
				self.simpleTimes.push(n); 
			}) ; 
		}/*else{
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
							self.times.push(n); 
						}) ;
						$.each(row.simpleTimes(), function(i, n){
							self.simpleTimes.push(n); 
						}) ; 
					}  
				},
				error : function() {
				 
				},
				complete : function(){
					 
				}
			}); 
		}*/
		else{
			
//			$.ajax({
//				url : "../jbtcx/queryTrainTimesNewIframe?trainId="+row.baseTrainId,
//				cache : false,
//				type : "GET",
//			}); 
			if(!$('#train_time_dlg').is(":hidden")){   
				$("#train_time_dlg").find("iframe").attr("src", "../jbtcx/queryTrainTimesNewIframe?trainId=" + row.baseTrainId);
				$('.panel8-title.panel8-with-icon').html("时刻表:"+self.currentTrainInfoMessage());
			}
		} 
//		if(!$('#train_time_dlg').is(":hidden")){ 
//			$('#train_time_dlg').dialog("close"); 
//			self.showCrossTrainTimeDlg2();
//		}
	};
	
	self.setCurrentCross = function(cross){
		if(hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeCurrentCrossFlag() == 0){
			self.searchModle().activeCurrentCrossFlag(1);  
		}else if(!hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeCurrentCrossFlag() == 1){
			self.searchModle().activeCurrentCrossFlag(0); 
		} 
		self.currentCross(cross);
		if(!$('#cross_map_dlg').is(":hidden")){
			$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideUnitCrossChartData?unitCrossId=" + cross.unitCrossId);
			$("#cross_map_dlg").dialog("setTitle", "交路单元图     交路名:" + self.currentCross().crossName());
		}
	};
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};
	
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.crossRows.rows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0);
				self.searchModle().activeFlag(0);
			}else{
				if(hasActiveRole(crossRow.tokenVehBureau())){ 
					crossRow.selected(1); 
					self.searchModle().activeFlag(1);
				}
			}  
		});  
	};
	
	self.selectCross = function(row){ 
//		self.crossAllcheckBox();  
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);  
			self.searchModle().activeFlag(1);
			$.each(self.crossRows.rows(), function(i, crossRow){  
				if(crossRow.selected() != 1 && crossRow != row && crossRow.activeFlag() == 1){
					self.crossAllcheckBox(0);
					return false;
				}  
			}); 
		}else{
			self.searchModle().activeFlag(0);  
			self.crossAllcheckBox(0);
			$.each(self.crossRows.rows(), function(i, crossRow){  
				if(crossRow.selected() == 1 && crossRow != row && crossRow.activeFlag() == 1){
					self.searchModle().activeFlag(1);
					return false;
				}  
			}); 
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
	 
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow(self.defaultCrossTemp)); 
	
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
						showErrorDialog("获取列车详点失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车详点失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		}
		
	};  
	 
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
	
	$("#cross_map_dlg").dialog({
	    onClose:function(){
	    		self.searchModle().showCrossMap(0);
       },
       onResize:function() {
			var iframeBox = $("#cross_map_dlg").find("iframe");
			var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
			var WH = $('#cross_map_dlg').height();
			var WW = $('#cross_map_dlg').width();
           if (isChrome) {
           	iframeBox.css({ "height": (WH) + "px"});
           	iframeBox.css({ "min-height": (WH) + "px"});
           	iframeBox.attr("width", (WW));

           }else{
           	iframeBox.css({ "height": (WH)  + "px"});
           	iframeBox.css({ "min-height": (WH) + "px"});
           	iframeBox.attr("width", (WW));
           }
		}
	 });
	
	
	self.init = function(){  
		//self.gloabBureaus = [{"shortName": "上", "code": "S"}, {"shortName": "京", "code": "B"}, {"shortName": "广", "code": "G"}];
		//self.searchModle().loadBureau(self.gloabBureaus); 
		//self.searchModle().loadChats([{"name":"方案1", "chartId": "1234"},{"name":"方案2", "chartId": "1235"}])
		$("#cross_map_dlg").dialog({
		    onClose:function(){
		    		self.searchModle().showCrossMap(0);
		       }
		   });
		
		$("#train_time_dlg").dialog("close"); 
		$("#file_upload_dlg").dialog("close"); 
		$("#cross_train_time_dlg").dialog("close");
		$("#cross_map_dlg").dialog("close"); 
		$("#cross_train_dlg").dialog("close");
		$("#cross_train_time_dlg").dialog("close"); 
		$("#cross_start_day").datepicker();
		
		//获取当期系统日期 
		 $.ajax({
				url : "../jbtcx/querySchemes",
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
						showErrorDialog("获取方案列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取方案列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
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
						}else{
							bureaus.unshift("");
						}
					}
					self.searchModle().loadBureau(bureaus);
//					self.searchModle().loadBureau(result.data); 
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
		
		
	};  
	
	self.loadCrosses = function(){
		self.crossRows.loadRows();
	};
	self.loadCrosseForPage = function(startIndex, endIndex) {   
		self.crossAllcheckBox(0);
		commonJsScreenLock();
		/* $.each(crosses,function(n, crossInfo){
			var row = new CrossRow(crossInfo);
			self.crossRows.push(row);
			rowLookup[row.crossName] = row;
		});  */
		
		
		var bureauCode = self.searchModle().bureau(); 
		var highlingFlag = self.searchModle().highlingFlag();
		var trainNbr = self.searchModle().filterTrainNbr(); 
		var checkFlag = self.searchModle().checkFlag();
		var unitCreateFlag = self.searchModle().unitCreateFlag();
		var startBureauCode = self.searchModle().startBureau();  
		var currentBureanFlag = self.searchModle().currentBureanFlag() ? '1' : '0'; 
		
		var chart = self.searchModle().chart(); 
		
		if(hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 0){
			self.searchModle().activeFlag(1);  
		}else if(!hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0); 
		} 
		
		if(chart == null){
			showErrorDialog("请选择方案!");
			commonJsScreenUnLock();
			return;
		}
		
		$.ajax({
				url : "../cross/getUnitCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					tokenVehBureau : bureauCode, 
					highlineFlag : highlingFlag == null ? null : highlingFlag.value,  
					checkFlag : checkFlag == null ? null : checkFlag.value,
					startBureau : startBureauCode,
					unitCreateFlag :  unitCreateFlag == null ? null : unitCreateFlag.value,
							chartId : chart == null ? null: chart.chartId,
					trainNbr : trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex,
					currentBureanFlag : currentBureanFlag
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
						//右边出现17
						var tableH = $("#crossInfo_Data .table").height();
						var boxH = $("#crossInfo_Data").height();
						if(tableH <= boxH){
							//alert("无");
							$("#crossInfo_Data .td_17").removeClass("display");
						}else{
							//alert("有");
							$("#crossInfo_Data .td_17").addClass("display");
						}
						 
					} else {
						showErrorDialog("获取交路单元信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取交路单元信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};

	self.crossRows = new PageModle(200, self.loadCrosseForPage);
	
	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau())
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
	
	self.showCrossMapDlg = function(n, e){ 
		if(self.currentCross().unitCrossId == '' || self.currentCross().unitCrossId == undefined || self.currentCross().unitCrossId == "undefined"){
			showWarningDialog("请先选择交路单元");  
			return;
		}
		var unitCrossId = self.currentCross().unitCrossId; 
		if($('#cross_map_dlg').is(":hidden")){
			$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideUnitCrossChartData?unitCrossId=" + unitCrossId);
			$("#cross_map_dlg").dialog({title: "交路单元图     交路名:" + self.currentCross().crossName(),draggable: true, resizable:true});
		};
	};  
	//#################################################################
	self.showCrossTrainTimeDlg2 = function(){ 
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		var trainId = self.currentTrainId();  
		if($('#train_time_dlg').is(":hidden")){  
			$("#train_time_dlg").find("iframe").attr("src", "../jbtcx/queryTrainTimesNewIframe?trainId=" + trainId);
			$('#train_time_dlg').dialog({ title: "时刻表:" +self.currentTrainInfoMessage(), autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#train_time_dlg").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#train_time_dlg').height();
					var WW = $('#train_time_dlg').width();
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
		}
	};  
	
/*	self.attrsave =function(){	
		$("#save_trainsort").attr("class","btn btn-success")
	}*/
	
	self.save_trainsort= function (){
		
		var array =self.trains();
		if(array.length==0){
			showWarningDialog("请选择交路");
			return "";
		}
		
		
		// ^[1-9]\\d*$
//   var reg = new RegExp("^[0-9]\\d*$");
    	
	for(var  i =0;i<array.length;i++){
			console.dir(array[i].groupGap  + array[i].dayGap)
			
			
			if(array[i].groupGap!=null && array[i].groupGap!=undefined && array[i].groupGap!=""){
				if(isNaN(array[i].groupGap)){
					showErrorDialog("组内间隔天数必须为数字");
			    	 return;
				}
			}
			if(array[i].dayGap!=null && array[i].dayGap!=undefined && array[i].dayGap!=""){
				if(isNaN(array[i].trainSort)){
					showErrorDialog("车序必须为数字");
			    	 return;
				}
			}
		}
	
		
		//console.dir(self.trains())
		//var array = self.trains();
		var id  =ko.toJSON(self.trains());
		
		
		showConfirmDiv("提示", "你确定要修改?", function (r) { 
	        if (r) { 
				$.ajax({
					url : "../cross/fixUnitCorssInfo",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						unitCrossIds : id						
					}),
					success : function(result) {     
						if(result.code == 0){ 
							
							showSuccessDialog("保存成功"); 
							self.crossRows.loadRows();
						}else{
							showErrorDialog("保存失败");
						}
					}
				}); 
	        }
		});
		
		
		
		
		
	}
	//#################################################################
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	
	self.showCrossTrainTimeDlg = function(){
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		$("#cross_train_time_dlg").dialog({draggable: true, resizable:true, top:50, onResize:function() {
			var simpleTimes_table = $("#simpleTimes_table");
			var allTimes_table = $("#allTimes_table");
			var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
			var WH = $('#cross_train_time_dlg').height() - 100;
			var WW = $('#cross_train_time_dlg').width();
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
	};
	
	self.deleteCrosses = function(){
		var crossIds = "";
		var crosses = self.crossRows.rows(); 
		var delCrosses = [];
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId; 
				delCrosses.push(crosses[i]);
			}; 
		} 
		if(crossIds == ""){
			showErrorDialog("没有可删除的记录");
			return;
		}
		showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
	        if (r) { 
				$.ajax({
					url : "../cross/deleteUnitCorssInfo",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						unitCrossIds : crossIds
					}),
					success : function(result) {     
						if(result.code == 0){ 
							self.crossRows.addLoadRows(delCrosses);  
							showSuccessDialog("删除交路单元成功"); 
						}else{
							showErrorDialog("删除交路单元失败");
						}
					}
				}); 
	        }
		});
	};
	
	self.createUnitCrossInfo = function(){ 
//		var crossIds = "";
//		var crosses = self.crossRows.rows();
//		for(var i = 0; i < crosses.length; i++){ 
//			if(crosses[i].selected() == 1){ 
//				crossIds += (crossIds == "" ? "" : ",");
//				crossIds += crosses[i].crossId;
//			}
//		} 
		var crossIds = "";
		var delCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){
			if(crosses[i].checkFlag() == 0 && crosses[i].selected() == 1){
				showWarningDialog("你选择了未审核的记录，请先审核");
				return;
			}else if(crosses[i].unitCreateFlag() == 1 && crosses[i].selected() == 1){
				showWarningDialog("不能重复生成");
				return;
			}else if(crosses[i].checkFlag() == 1 && crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId;
				delCrosses.push( crosses[i]);
			};
		} 
		if(crossIds == ""){
			showWarningDialog("没有选中数据"); 
			return;
		} 
		commonJsScreenLock();
		 $.ajax({
				url : "../cross/updateUnitCrossId",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossIds : crossIds 
				}),
				success : function(result) {     
					if(result.code == 0){ 
						for(var j = 0; j < result.data.length; j++){
							for(var i = 0; i < delCrosses.length; i++){
								if(result.data[j].unitCrossId == delCrosses[i].unitCrossId){
									delCrosses[i].unitCreateFlag(result.data[j].flag);
								 }
							}
						} 
						if(result.data.length == 0){
							showErrorDialog("更新失败");
							return;
						}else if(result.data.length < delCrosses.length){  
							showWarningDialog("部分更新成功");  
					   }else{
						   showSuccessDialog("更新成功");  
					   }
					}else{
						showSuccessDialog("更新失败");  
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
	
	self.bureauChange = function(){
		if(hasActiveRole(self.searchModle().bureau())){
			self.searchModle().activeFlag(1); 
			self.clearData();
		}else if(self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0);
			self.clearData();
		}
	};
	
	self.showTrains = function(row) {  
		self.trains.remove(function(item) {
			return true;
		});   
		 $.ajax({
				url : "../cross/getUnitCrossTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossId : row.unitCrossId  
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {   
							if(result.data[0].crossinfo == null){
								showWarningDialog("交路单元信息已经不存在");
							}else{
								self.setCurrentCross(new CrossRow(result.data[0].crossinfo));
//								self.currentCross(new CrossRow(result.data[0].crossInfo));
								
								if(result.data[0].unitCrossTrainInfo != null){
									for(var i = 0; i < result.data[0].unitCrossTrainInfo.length; i++){
										var row = new TrainRow(result.data[0].unitCrossTrainInfo[i]); 
										self.trains.push(row); 
									} 
								}
							}
							
						}
						
					
						var tableH = $("#train_information .table").height();
						var boxH = $("#train_information").height();
						//alert(tableH+"cc"+boxH);
						if(tableH <= boxH){
							//alert("无");
							$("#train_information .td_17").removeClass("display");
						}else{
							//alert("有");
							$("#train_information .td_17").addClass("display");
						}
						 
					} else {
						showErrorDialog("获取列车信息失败");
					} 
				},
				error : function() {
					showErrorDialog("获取列车信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		
	};  
	
	self.checkCrossInfo = function(){
		
		var crossIds = "";
		var updateCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].checkFlag() == 1 && crosses[i].selected() == 1){  
				showWarningDialog("不能重复审核"); 
				return;
			}else if(crosses[i].checkFlag() == 0 && crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId;
				updateCrosses.push(crosses[i]); 
			} 
		}  
		if(crossIds == ""){
			showWarningDialog("没有可审核的");
			return;
		}
		commonJsScreenLock(); 
		 $.ajax({
				url : "../cross/checkUnitCorssInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(updateCrosses, function(i, n){ 
							n.checkFlag("1");
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
	
	self.activeCurrentCrossFlag = ko.observable(0);
	
	self.bureaus = ko.observableArray();  
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	 
	self.highlingFlags = highlingFlags;
	
	self.unitCreateFlags = unitCreateFlags; 
	
	self.filterCheckFlag = ko.observable(0);
	
	self.filterUnitCreateFlag = ko.observable(0);
	
	self.currentBureanFlag = ko.observable(0);
		
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
	self.visiableRow =  ko.observable(true); 
	
	self.selected =  ko.observable(0); 
	
	self.unitCrossId = data.unitCrossId;
	
	self.shortNameFlag =  ko.observable(true);
	
	self.crossName = ko.observable(data.crossName);  
	 
	self.shortName = ko.computed(function(){
		if(data.crossName ==  null){
			return "";
		}
		trainNbrs = data.crossName.split('-');
		if(trainNbrs.length > 2){
			return trainNbrs[0] + '-......-' + trainNbrs[trainNbrs.length-1];
		}else{
			return data.crossName;
		}
	});  
 
	self.checkFlag = ko.observable(data.checkFlag);
	
	self.unitCreateFlag = ko.observable(data.unitCreateFlag == null ? '0' : data.unitCreateFlag);
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
	self.throughline = ko.observable(data.throughline);
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
						 }
					 }
				 } 
			 }
			 return result; 
	});
	
	self.activeFlag = ko.computed(function(){
		return hasActiveRole(data.tokenVehBureau);
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
					 }
				 }
			 } 
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
					 }
				 }
			 } 
		 } 
		 return  result == "" ? "" : result; 
	});
	self.relevantBureau = ko.observable(data.relevantBureau);
	self.tokenPsgDept = ko.observable(data.tokenPsgDept);
	self.tokenPsgDepot = ko.observable(data.tokenPsgDepot);
	self.locoType = ko.observable(data.locoType);
	self.crhType = ko.observable(data.crhType);
	self.elecSupply = ko.observable(data.elecSupply);
	self.dejCollect = ko.observable(data.dejCollect);
	self.airCondition = ko.observable(data.airCondition);
	self.note = ko.observable(data.note);

	//列表界面显示生成、审核状态
	self.checkFlagStr = ko.computed(function(){
		switch (self.checkFlag()) {
			case "1": 
				return "<span class='label label-success'>已</span>";//"已";
				break;
			case 1: 
				return "<span class='label label-success'>已</span>";//"已";
				break;
			default: 
				return "<span class='label label-danger'>未</span>";//"未";
				break;
		}
	});
};

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
}

function TrainRow(data) {
	var self = this; 
	
	self.unitCrossTrainId =data.unitCrossTrainId;
	self.groupGap =data.groupGap;
	self.crossTainId  = data.crossTainId;//BASE_CROSS_TRAIN_ID
	self.crossId = data.crossId;//BASE_CROSS_ID
	self.trainSort = data.trainSort;//TRAIN_SORT
	self.baseTrainId = data.baseTrainId;
	self.runDate = data.runDate;
	self.endDate = data.endDate;
	self.periodSourceTime = data.periodSourceTime;
	self.periodTargetTime = data.periodTargetTime;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	self.groupSerialNbr = data.groupSerialNbr;//GROUP_SERIAL_NBR
	self.marshallingName = data.marshallingName;
	
	self.times = ko.observableArray();  
	self.simpleTimes = ko.observableArray(); 
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			var timeRow = new TrainTimeRow(n);
			self.times.push(timeRow);
			if(n.stationFlag != 'BTZ'){
				self.simpleTimes.push(timeRow);
			}
		});
	}; 
	
	//self.startBureau = data.startBureau;//START_BUREAU 
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
		 //return data.alertNateTime == null ? "": data.alertNateTime.substring(5).replace(/-/g, "").substring(0, data.alertNateTime.substring(5).replace(/-/g, "").length - 3);
		return data.alertNateTime == null ? "": data.alertNateTime.substring(0, 10).replace(/-/g, "");
	});//ALTERNATE_TIME
	//self.spareFlag = data.spareFlag;//SPARE_FLAG
	self.spareFlag = ko.computed(function(){ 
		switch (data.spareFlag) {
			case 9:
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
		return data.spareApplyFlage == 1 ? "是" : "";
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
}

function GetDateDiff(data)
{ 
	 if(data.childIndex == 0 
			 || data.dptTime == '-' 
			 || data.dptTime == null 
			 || data.arrTime == null){
		return "";
	} 
	var startTime = new Date(data.arrTime);
	if(data.sourceDay != null){
		startTime.setDate(startTime.getDate() + data.sourceDay);
	}  
	var endTime = new Date(data.dptTime);   
	if(data.targetDay != null){
		endTime.setDate(endTime.getDate() + data.targetDay);
	} 
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
function TrainTimeRow(data) { 
	var self = this; 
	self.index = data.childIndex + 1;
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = data.stationFlag=='SFZ'?'--':filterValue(data.arrTime);
	self.targetTime = data.stationFlag=='ZDZ'?'--':filterValue(data.dptTime);
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);  
	self.runDays = data.stationFlag=='ZDZ'?'--':data.runDays;
	self.sourceDay = data.stationFlag=='SFZ'?'--':data.sourceDay;
	self.stationFlag = data.stationFlag;
	 
};

function openLogin() {
	$("#file_upload_dlg").dialog("open");
}

$(window.document).scroll(function () {
	    var scrolltop = $(document).scrollTop(); 
});
$(function() { 
	var crosstab;
	var unittab;
	var tabflag;
	
	heightAuto();
	var cross = new CrossModel();
	ko.applyBindings(cross); 
	 
	cross.init();
});

function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#crossInfo_Data").css("height",WH-286+"px");
	$("#train_information").css("height",WH-404+"px");	
};


var highlingFlags = [{"value": 0, "text": "普速"},{"value": 1, "text": "高铁"},{"value": 2, "text": "混合"}];
var checkFlags = [{"value": '1', "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "1", "text": "已"},{"value": 0, "text": "未"}];
var highlingrules = [{"value": 1, "text": "平日"},{"value": 2, "text": "周末"},{"value": 3, "text": "高峰"}];
var commonlinerules = [{"value": 1, "text": "每日"},{"value": 2, "text": "隔日"}];
 
var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";

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

function CrossModel() {
	var self = this;
		//列车列表
	self.trains = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0);
	
	self.gloabBureaus = [];  
	
	self.currentTrain =  ko.observable();
	
	self.currentTrainInfoMessage = ko.observable("");
	
	self.currentTrainId = ko.observable("");
	
	self.times = ko.observableArray();
	
	self.simpleTimes = ko.observableArray();
	
	//担当局
	self.searchModle = ko.observable(new searchModle()); 
	self.defaultCrossTemp = {"crossId":"",
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
		"appointPeriod":"",
		"locoType":"",
		"crhType":"",
		"elecSupply":"",
		"dejCollect":"",
		"airCondition":"",
		"note":"", 
		"createPeople":"", 
		"createPeopleOrg":"",  
		"createTime":""};
	self.clearData = function(){
		 self.crossRows.clear(); 
		 self.currentCross(new CrossRow(self.defaultCrossTemp)); 
		 self.times.remove(function(item){
			return true;
		 });
		 
		 self.trains.remove(function(item){
			return true;
		 });  
		 self.currentTrain = ko.observable();
	};
	
	self.setCurrentTrain = function(row){
		self.currentTrain(row);
		self.currentTrainInfoMessage("车次：" + row.trainNbr + "&nbsp;&nbsp;&nbsp;" + row.startStn + "——" + row.endStn);
		self.currentTrainId(row.baseTrainId);
		self.times.remove(function(item){
			return true;
		});
		self.simpleTimes.remove(function(item){
			return true;
		});
		if(row.times().length > 0){ 
			$.each(row.times(), function(i, n){
				self.times.push(n); 
			}) ;
			$.each(row.simpleTimes(), function(i, n){
				self.simpleTimes.push(n); 
			}) ; 
		}/*else{
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
							self.times.push(n); 
						}) ;
						$.each(row.simpleTimes(), function(i, n){
							self.simpleTimes.push(n); 
						}) ; 
					}  
				},
				error : function() {
				 
				},
				complete : function(){
					 
				}
			}); 
		}*/
		else{
			
//			$.ajax({
//				url : "../jbtcx/queryTrainTimesNewIframe?trainId="+row.baseTrainId,
//				cache : false,
//				type : "GET",
//			}); 
			if(!$('#train_time_dlg').is(":hidden")){   
				$("#train_time_dlg").find("iframe").attr("src", "../jbtcx/queryTrainTimesNewIframe?trainId=" + row.baseTrainId);
				$('.panel8-title.panel8-with-icon').html("时刻表:"+self.currentTrainInfoMessage());
			}
		} 
//		if(!$('#train_time_dlg').is(":hidden")){ 
//			$('#train_time_dlg').dialog("close"); 
//			self.showCrossTrainTimeDlg2();
//		}
	};
	
	self.setCurrentCross = function(cross){
		if(hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeCurrentCrossFlag() == 0){
			self.searchModle().activeCurrentCrossFlag(1);  
		}else if(!hasActiveRole(cross.tokenVehBureau()) && self.searchModle().activeCurrentCrossFlag() == 1){
			self.searchModle().activeCurrentCrossFlag(0); 
		} 
		self.currentCross(cross);
		if(!$('#cross_map_dlg').is(":hidden")){
			$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideUnitCrossChartData?unitCrossId=" + cross.unitCrossId);
			$("#cross_map_dlg").dialog("setTitle", "交路单元图     交路名:" + self.currentCross().crossName());
		}
	};
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};
	
	self.all_relevant_tokenValue = function(n,  event){
		if(event && event.target && event.target.value){
			self.searchModle().all_relevant_token(event.target.value);
			self.loadCrosses();
		}
	}
	
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.crossRows.rows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0);
				self.searchModle().activeFlag(0);
			}else{
				if(hasActiveRole(crossRow.tokenVehBureau())){ 
					crossRow.selected(1); 
					self.searchModle().activeFlag(1);
				}
			}  
		});  
	};
	
	self.selectCross = function(row){ 
//		self.crossAllcheckBox();  
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);  
			self.searchModle().activeFlag(1);
			$.each(self.crossRows.rows(), function(i, crossRow){  
				if(crossRow.selected() != 1 && crossRow != row && crossRow.activeFlag() == 1){
					self.crossAllcheckBox(0);
					return false;
				}  
			}); 
		}else{
			self.searchModle().activeFlag(0);  
			self.crossAllcheckBox(0);
			$.each(self.crossRows.rows(), function(i, crossRow){  
				if(crossRow.selected() == 1 && crossRow != row && crossRow.activeFlag() == 1){
					self.searchModle().activeFlag(1);
					return false;
				}  
			}); 
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
	 
	//当前选中的交路对象
	self.currentCross = ko.observable(new CrossRow(self.defaultCrossTemp)); 
	
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
						showErrorDialog("获取列车详点失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车详点失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		}
		
	};  
	 
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
	
	$("#cross_map_dlg").dialog({
	    onClose:function(){
	    		self.searchModle().showCrossMap(0);
       },
       onResize:function() {
			var iframeBox = $("#cross_map_dlg").find("iframe");
			var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
			var WH = $('#cross_map_dlg').height();
			var WW = $('#cross_map_dlg').width();
           if (isChrome) {
           	iframeBox.css({ "height": (WH) + "px"});
           	iframeBox.css({ "min-height": (WH) + "px"});
           	iframeBox.attr("width", (WW));

           }else{
           	iframeBox.css({ "height": (WH)  + "px"});
           	iframeBox.css({ "min-height": (WH) + "px"});
           	iframeBox.attr("width", (WW));
           }
		}
	 });
	
	
	self.init = function(){  
		//self.gloabBureaus = [{"shortName": "上", "code": "S"}, {"shortName": "京", "code": "B"}, {"shortName": "广", "code": "G"}];
		//self.searchModle().loadBureau(self.gloabBureaus); 
		//self.searchModle().loadChats([{"name":"方案1", "chartId": "1234"},{"name":"方案2", "chartId": "1235"}])
		$("#cross_map_dlg").dialog({
		    onClose:function(){
		    		self.searchModle().showCrossMap(0);
		       }
		   });
		
		$("#train_time_dlg").dialog("close"); 
		$("#file_upload_dlg").dialog("close"); 
		$("#cross_train_time_dlg").dialog("close");
		$("#cross_map_dlg").dialog("close"); 
		$("#cross_train_dlg").dialog("close");
		$("#cross_train_time_dlg").dialog("close"); 
		$("#cross_start_day").datepicker();
		
		//获取当期系统日期 
		 $.ajax({
				url : "../jbtcx/querySchemes",
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
						showErrorDialog("获取方案列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取方案列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
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
						}else{
							bureaus.unshift("");
						}
					}
					self.searchModle().loadBureau(bureaus);
//					self.searchModle().loadBureau(result.data); 
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
		
		
	};  
	
	self.loadCrosses = function(){
		self.crossRows.loadRows();
	};
	self.loadCrosseForPage = function(startIndex, endIndex) {   
		self.crossAllcheckBox(0);
		commonJsScreenLock();
		/* $.each(crosses,function(n, crossInfo){
			var row = new CrossRow(crossInfo);
			self.crossRows.push(row);
			rowLookup[row.crossName] = row;
		});  */
		
		
		var bureauCode = self.searchModle().bureau(); 
		var highlingFlag = self.searchModle().highlingFlag();
		var trainNbr = self.searchModle().filterTrainNbr(); 
		var checkFlag = self.searchModle().checkFlag();
		var unitCreateFlag = self.searchModle().unitCreateFlag();
		var startBureauCode = self.searchModle().startBureau();  
		var currentBureanFlag = self.searchModle().currentBureanFlag() ? '1' : '0'; 
		var all_relevant_token = self.searchModle().all_relevant_token();
		
		var chart = self.searchModle().chart(); 
		
		if(hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 0){
			self.searchModle().activeFlag(1);  
		}else if(!hasActiveRole(bureauCode) && self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0); 
		} 
		
		if(chart == null){
			showErrorDialog("请选择方案!");
			commonJsScreenUnLock();
			return;
		}
		
		$.ajax({
				url : "../cross/getUnitCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					tokenVehBureau : bureauCode, 
					highlineFlag : highlingFlag == null ? null : highlingFlag.value,  
					checkFlag : checkFlag == null ? null : checkFlag.value,
					startBureau : startBureauCode,
					unitCreateFlag :  unitCreateFlag == null ? null : unitCreateFlag.value,
							chartId : chart == null ? null: chart.chartId,
					trainNbr : trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex,
					currentBureanFlag : currentBureanFlag,
					all_relevant_token : all_relevant_token
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
						//右边出现17
						var tableH = $("#crossInfo_Data .table").height();
						var boxH = $("#crossInfo_Data").height();
						if(tableH <= boxH){
							//alert("无");
							$("#crossInfo_Data .td_17").removeClass("display");
						}else{
							//alert("有");
							$("#crossInfo_Data .td_17").addClass("display");
						}
						 
					} else {
						showErrorDialog("获取交路单元信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取交路单元信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
	};

	self.crossRows = new PageModle(200, self.loadCrosseForPage);
	
	self.saveCrossInfo = function() { 
		alert(self.currentCross().tokenVehBureau())
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
	
	self.showCrossMapDlg = function(n, e){ 
		if(typeof(tabflag) != "undefined"){
			if(tabflag == 0){
				if(self.currentCross().unitCrossId == '' || self.currentCross().unitCrossId == undefined || self.currentCross().unitCrossId == "undefined"){
					showWarningDialog("请先选择交路单元");  
					return;
				}
				var unitCrossId = self.currentCross().unitCrossId; 
				if($('#cross_map_dlg').is(":hidden")){
					$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideUnitCrossChartData?unitCrossId=" + unitCrossId);
					$("#cross_map_dlg").dialog({title: "交路单元图     交路名:" + self.currentCross().crossName(),draggable: true, resizable:true});
				};
			}
			if(tabflag == 1){
				if(!self.currentCross().crossId || self.currentCross().crossId == ''){ 
					showWarningDialog("请先选择交路"); 
					return;
				} 
				var crossId = self.currentCross().crossId;  
				if($('#cross_map_dlg').is(":hidden")){  
					$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideCrossChartData?crossId=" + crossId
							+"&crossName="+self.currentCross().crossName()
							+"&chartId="+self.searchModle().chart().chartId);
					$('#cross_map_dlg').dialog({ title: "基本交路图     交路名:" + self.currentCross().crossName(), autoOpen: true, modal: false, draggable: true, resizable:true,
						onResize:function() {
							var iframeBox = $("#cross_map_dlg").find("iframe");
							var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
							var WH = $('#cross_map_dlg').height();
							var WW = $('#cross_map_dlg').width();
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
			}
		}else{
			showWarningDialog("请先选择交路");
		}
	}; 
	
	//#################################################################
	self.showCrossTrainTimeDlg2 = function(){ 
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		var trainId = self.currentTrainId();  
		if($('#train_time_dlg').is(":hidden")){  
			$("#train_time_dlg").find("iframe").attr("src", "../jbtcx/queryTrainTimesNewIframe?trainId=" + trainId);
			$('#train_time_dlg').dialog({ title: "时刻表:" +self.currentTrainInfoMessage(), autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#train_time_dlg").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#train_time_dlg').height();
					var WW = $('#train_time_dlg').width();
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
		}
	};  
	
/*	self.attrsave =function(){	
		$("#save_trainsort").attr("class","btn btn-success")
	}*/
	
	self.save_trainsort= function (){
		
		var array =self.trains();
		if(array.length==0){
			showWarningDialog("请选择交路");
			return "";
		}
		
		
		// ^[1-9]\\d*$
//   var reg = new RegExp("^[0-9]\\d*$");
    	
	for(var  i =0;i<array.length;i++){
			console.dir(array[i].groupGap  + array[i].dayGap)
			
			
			if(array[i].groupGap!=null && array[i].groupGap!=undefined && array[i].groupGap!=""){
				if(isNaN(array[i].groupGap)){
					showErrorDialog("组内间隔天数必须为数字");
			    	 return;
				}
			}
			if(array[i].dayGap!=null && array[i].dayGap!=undefined && array[i].dayGap!=""){
				if(isNaN(array[i].trainSort)){
					showErrorDialog("车序必须为数字");
			    	 return;
				}
			}
		}
	
		
		//console.dir(self.trains())
		//var array = self.trains();
		var id  =ko.toJSON(self.trains());
		
		
		showConfirmDiv("提示", "你确定要修改?", function (r) { 
	        if (r) { 
				$.ajax({
					url : "../cross/fixUnitCorssInfo",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						unitCrossIds : id						
					}),
					success : function(result) {     
						if(result.code == 0){ 
							
							showSuccessDialog("保存成功"); 
							self.crossRows.loadRows();
						}else{
							showErrorDialog("保存失败");
						}
					}
				}); 
	        }
		});
		
		
		
		
		
	}
	//#################################################################
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	
	self.showCrossTrainTimeDlg = function(){
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		$("#cross_train_time_dlg").dialog({draggable: true, resizable:true, top:50, onResize:function() {
			var simpleTimes_table = $("#simpleTimes_table");
			var allTimes_table = $("#allTimes_table");
			var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
			var WH = $('#cross_train_time_dlg').height() - 100;
			var WW = $('#cross_train_time_dlg').width();
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
	};
	
	self.deleteCrosses = function(){
		var crossIds = "";
		var crosses = self.crossRows.rows(); 
		var delCrosses = [];
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].selected() == 1){ 
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId; 
				delCrosses.push(crosses[i]);
			}; 
		} 
		if(crossIds == ""){
			showErrorDialog("没有可删除的记录");
			return;
		}
		showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
	        if (r) { 
				$.ajax({
					url : "../cross/deleteUnitCorssInfo",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						unitCrossIds : crossIds
					}),
					success : function(result) {     
						if(result.code == 0){ 
							self.crossRows.addLoadRows(delCrosses);  
							showSuccessDialog("删除交路单元成功"); 
						}else{
							showErrorDialog("删除交路单元失败");
						}
					}
				}); 
	        }
		});
	};
	
	self.createUnitCrossInfo = function(){ 
//		var crossIds = "";
//		var crosses = self.crossRows.rows();
//		for(var i = 0; i < crosses.length; i++){ 
//			if(crosses[i].selected() == 1){ 
//				crossIds += (crossIds == "" ? "" : ",");
//				crossIds += crosses[i].crossId;
//			}
//		} 
		var crossIds = "";
		var delCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){
			if(crosses[i].checkFlag() == 0 && crosses[i].selected() == 1){
				showWarningDialog("你选择了未审核的记录，请先审核");
				return;
			}else if(crosses[i].unitCreateFlag() == 1 && crosses[i].selected() == 1){
				showWarningDialog("不能重复生成");
				return;
			}else if(crosses[i].checkFlag() == 1 && crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId;
				delCrosses.push( crosses[i]);
			};
		} 
		if(crossIds == ""){
			showWarningDialog("没有选中数据"); 
			return;
		} 
		commonJsScreenLock();
		 $.ajax({
				url : "../cross/updateUnitCrossId",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossIds : crossIds 
				}),
				success : function(result) {     
					if(result.code == 0){ 
						for(var j = 0; j < result.data.length; j++){
							for(var i = 0; i < delCrosses.length; i++){
								if(result.data[j].unitCrossId == delCrosses[i].unitCrossId){
									delCrosses[i].unitCreateFlag(result.data[j].flag);
								 }
							}
						} 
						if(result.data.length == 0){
							showErrorDialog("更新失败");
							return;
						}else if(result.data.length < delCrosses.length){  
							showWarningDialog("部分更新成功");  
					   }else{
						   showSuccessDialog("更新成功");  
					   }
					}else{
						showSuccessDialog("更新失败");  
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
	
	self.bureauChange = function(){
		if(hasActiveRole(self.searchModle().bureau())){
			self.searchModle().activeFlag(1); 
			self.clearData();
		}else if(self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0);
			self.clearData();
		}
	};
	
	self.showTrains = function(row) {  
		self.trains.remove(function(item) {
			return true;
		});
		crosstab = row.crossId;
		unittab = row.unitCrossId;
		tabflag = 0;
		$.ajax({
			url : "../cross/getUnitCrossTrainInfo",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({  
				unitCrossId : row.unitCrossId
			}),
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null && result.data.length > 0) {   
						if(result.data[0].oCrossinfo == null){
							showWarningDialog("交路单元信息已经不存在");
						}else{
							self.setCurrentCross(new CrossRow(result.data[0].oCrossinfo));
//								self.currentCross(new CrossRow(result.data[0].crossInfo));
							
							if(result.data[0].unitCrossTrainInfo != null){
								for(var i = 0; i < result.data[0].unitCrossTrainInfo.length; i++){
									var row = new TrainRow(result.data[0].unitCrossTrainInfo[i]); 
									self.trains.push(row); 
								} 
							}
						}
						
					}
					
				
					var tableH = $("#train_information .table").height();
					var boxH = $("#train_information").height();
					//alert(tableH+"cc"+boxH);
					if(tableH <= boxH){
						//alert("无");
						$("#train_information .td_17").removeClass("display");
					}else{
						//alert("有");
						$("#train_information .td_17").addClass("display");
					}
					 
				} else {
					showErrorDialog("获取列车信息失败");
				} 
			},
			error : function() {
				showErrorDialog("获取列车信息失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
		});
	};
	
	self.showcrosstrain = function(){
		self.trains.remove(function(item) {
			return true;
		});   
		tabflag = 1;
		commonJsScreenLock();
		if(typeof(crosstab) != "undefined"){
			$.ajax({
				url : "../cross/getCrossTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossId : crosstab  
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {  
							if(result.data[0].crossInfo == null){
								showWarningDialog("交路信息已经不存在");
							}else{
								self.setCurrentCross(new CrossRow(result.data[0].crossInfo));
								//self.setCurrentChartName(result.data[0].crossInfo.chartName);
//								self.currentCross(new CrossRow(result.data[0].crossInfo));
								if(result.data[0].crossTrainInfo != null){
									$.each(result.data[0].crossTrainInfo,function(n, crossInfo){
										var row = new crossTrainRow(crossInfo);
										self.trains.push(row); 
									});
								} 
							}
							
						}
						//是否出现右边17
						var tableH = $("#train_information table").height();
						var boxH = $("#train_information").height();
						if(tableH <= boxH){
							//alert("没");
							$("#train_information .td_17").removeClass("display");
						}else{
							//alert("有");
							$("#train_information .td_17").addClass("display");
						}
						 
					} else {
						showErrorDialog("获取交路列车列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取交路列车列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 
		}
	}
	
	self.showunittrain = function(){
		self.trains.remove(function(item) {
			return true;
		});
		tabflag = 0;
		if(typeof(unittab) != "undefined"){
			$.ajax({
				url : "../cross/getUnitCrossTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossId : unittab  
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null && result.data.length > 0) {   
							if(result.data[0].crossinfo == null){
								showWarningDialog("交路单元信息已经不存在");
							}else{
								self.setCurrentCross(new CrossRow(result.data[0].crossinfo));
//									self.currentCross(new CrossRow(result.data[0].crossInfo));
								
								if(result.data[0].unitCrossTrainInfo != null){
									for(var i = 0; i < result.data[0].unitCrossTrainInfo.length; i++){
										var row = new TrainRow(result.data[0].unitCrossTrainInfo[i]); 
										self.trains.push(row); 
									} 
								}
							}
							
						}
					
						var tableH = $("#train_information .table").height();
						var boxH = $("#train_information").height();
						//alert(tableH+"cc"+boxH);
						if(tableH <= boxH){
							//alert("无");
							$("#train_information .td_17").removeClass("display");
						}else{
							//alert("有");
							$("#train_information .td_17").addClass("display");
						}
						 
					} else {
						showErrorDialog("获取列车信息失败");
					} 
				},
				error : function() {
					showErrorDialog("获取列车信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			});
		}
	}

	self.checkCrossInfo = function(){
		
		var crossIds = "";
		var updateCrosses = [];
		var crosses = self.crossRows.rows();
		for(var i = 0; i < crosses.length; i++){ 
			if(crosses[i].checkFlag() == 1 && crosses[i].selected() == 1){  
				showWarningDialog("不能重复审核"); 
				return;
			}else if(crosses[i].checkFlag() == 0 && crosses[i].selected() == 1){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].unitCrossId;
				updateCrosses.push(crosses[i]); 
			} 
		}  
		if(crossIds == ""){
			showWarningDialog("没有可审核的");
			return;
		}
		commonJsScreenLock(); 
		 $.ajax({
				url : "../cross/checkUnitCorssInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					unitCrossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(updateCrosses, function(i, n){ 
							n.checkFlag("1");
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
	
	self.activeCurrentCrossFlag = ko.observable(0);
	
	self.bureaus = ko.observableArray();  
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	 
	self.highlingFlags = highlingFlags;
	
	self.unitCreateFlags = unitCreateFlags; 
	
	self.filterCheckFlag = ko.observable(0);
	
	self.filterUnitCreateFlag = ko.observable(0);
	
	self.currentBureanFlag = ko.observable(0);
	
	self.all_relevant_token = ko.observable();
		
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
	self.visiableRow =  ko.observable(true); 
	
	self.selected =  ko.observable(0); 
	
	self.unitCrossId = data.unitCrossId;
	
	self.crossId = data.crossId;
	
	self.shortNameFlag =  ko.observable(true);
	
	self.crossName = ko.observable(data.crossName);  
	 
	self.shortName = ko.computed(function(){
		if(data.crossName ==  null){
			return "";
		}
		trainNbrs = data.crossName.split('-');
		if(trainNbrs.length > 2){
			return trainNbrs[0] + '-......-' + trainNbrs[trainNbrs.length-1];
		}else{
			return data.crossName;
		}
	});  
 
	self.checkFlag = ko.observable(data.checkFlag);
	
	self.runRange = ko.observable(data.runRange);
	self.marshallingNums = ko.observable(data.marshallingNums);
	self.peopleNums = ko.observable(data.peopleNums);
	self.marshallingContent = ko.observable(data.marshallingContent);
	self.crossLevel = ko.observable(data.crossLevel);
	
	self.unitCreateFlag = ko.observable(data.unitCreateFlag == null ? '0' : data.unitCreateFlag);
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
	self.throughline = ko.observable(data.throughline);
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
						 }
					 }
				 } 
			 }
			 return result; 
	});
	
	self.activeFlag = ko.computed(function(){
		return hasActiveRole(data.tokenVehBureau);
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
					 }
				 }
			 } 
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
					 }
				 }
			 } 
		 } 
		 return  result == "" ? "" : result; 
	});
	self.relevantBureau = ko.observable(data.relevantBureau);
	self.tokenPsgDept = ko.observable(data.tokenPsgDept);
	self.tokenPsgDepot = ko.observable(data.tokenPsgDepot);
	self.locoType = ko.observable(data.locoType);
	self.crhType = ko.observable(data.crhType);
	self.elecSupply = ko.observable(data.elecSupply);
	self.dejCollect = ko.observable(data.dejCollect);
	self.airCondition = ko.observable(data.airCondition);
	self.note = ko.observable(data.note);

	//列表界面显示生成、审核状态
	self.checkFlagStr = ko.computed(function(){
		switch (self.checkFlag()) {
			case "1": 
				return "<span class='label label-success'>已</span>";//"已";
				break;
			case 1: 
				return "<span class='label label-success'>已</span>";//"已";
				break;
			default: 
				return "<span class='label label-danger'>未</span>";//"未";
				break;
		}
	});
};

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
}

function TrainRow(data) {
	var self = this; 
	
	self.unitCrossTrainId =data.unitCrossTrainId;
	self.groupGap =data.groupGap;
	self.crossTainId  = data.crossTainId;//BASE_CROSS_TRAIN_ID
	self.crossId = data.crossId;//BASE_CROSS_ID
	self.trainSort = data.trainSort;//TRAIN_SORT
	self.baseTrainId = data.baseTrainId;
	self.runDate = data.runDate;
	self.endDate = data.endDate;
	self.periodSourceTime = data.periodSourceTime;
	self.periodTargetTime = data.periodTargetTime;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	self.groupSerialNbr = data.groupSerialNbr;//GROUP_SERIAL_NBR
	self.marshallingName = data.marshallingName;
	
	self.times = ko.observableArray();  
	self.simpleTimes = ko.observableArray(); 
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			var timeRow = new TrainTimeRow(n);
			self.times.push(timeRow);
			if(n.stationFlag != 'BTZ'){
				self.simpleTimes.push(timeRow);
			}
		});
	}; 
	
	//self.startBureau = data.startBureau;//START_BUREAU 
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
		 //return data.alertNateTime == null ? "": data.alertNateTime.substring(5).replace(/-/g, "").substring(0, data.alertNateTime.substring(5).replace(/-/g, "").length - 3);
		return data.alertNateTime == null ? "": data.alertNateTime.substring(0, 10).replace(/-/g, "");
	});//ALTERNATE_TIME
	//self.spareFlag = data.spareFlag;//SPARE_FLAG
	self.spareFlag = ko.computed(function(){ 
		switch (data.spareFlag) {
			case 9:
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
		return data.spareApplyFlage == 1 ? "是" : "";
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

function crossTrainRow(data) {
	var self = this; 
	self.crossTainId  = data.crossTainId;//BASE_CROSS_TRAIN_ID
	self.crossId = data.crossId;//BASE_CROSS_ID
	self.trainSort = data.trainSort;//TRAIN_SORT
	self.baseTrainId = data.baseTrainId;
	self.trainNbr = data.trainNbr;//TRAIN_NBR
	self.startStn = data.startStn;//START_STN
	self.runDate = data.runDate;
	self.endDate = data.endDate;
	self.periodSourceTime = data.periodSourceTime;
	self.periodTargetTime = data.periodTargetTime;
	self.baseCrossTrainId=data.baseCrossTrainId;
	
	self.groupSerialNbr = "";
	self.groupGap = "";
	
	self.times = ko.observableArray();  
	self.simpleTimes = ko.observableArray(); 
	self.loadTimes = function(times){
		$.each(times, function(i, n){ 
			var timeRow = new TrainTimeRow(n);
			self.times.push(timeRow);
			if(n.stationFlag != 'BTZ'){
				self.simpleTimes.push(timeRow);
			}
		});
	}; 
	
	//self.startBureau = data.startBureau;//START_BUREAU 
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
		 //return data.alertNateTime == null ? "": data.alertNateTime.substring(5).replace(/-/g, "").substring(0, data.alertNateTime.substring(5).replace(/-/g, "").length - 3);
		return data.alertNateTime == null ? "": data.alertNateTime.substring(0, 10).replace(/-/g, "");
	});//ALTERNATE_TIME
	//self.spareFlag = data.spareFlag;//SPARE_FLAG
	self.spareFlag = ko.computed(function(){ 
		switch (data.spareFlag) {
			case 9:
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
		return data.spareApplyFlage == 1 ? "是" : "";
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
					return "混合";
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
}

function GetDateDiff(data)
{ 
	 if(data.childIndex == 0 
			 || data.dptTime == '-' 
			 || data.dptTime == null 
			 || data.arrTime == null){
		return "";
	} 
	var startTime = new Date(data.arrTime);
	if(data.sourceDay != null){
		startTime.setDate(startTime.getDate() + data.sourceDay);
	}  
	var endTime = new Date(data.dptTime);   
	if(data.targetDay != null){
		endTime.setDate(endTime.getDate() + data.targetDay);
	} 
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
function TrainTimeRow(data) { 
	var self = this; 
	self.index = data.childIndex + 1;
	self.stnName = filterValue(data.stnName);
	self.bureauShortName = filterValue(data.bureauShortName);
	self.sourceTime = data.stationFlag=='SFZ'?'--':filterValue(data.arrTime);
	self.targetTime = data.stationFlag=='ZDZ'?'--':filterValue(data.dptTime);
	self.stepStr = GetDateDiff(data); 
	self.trackName = filterValue(data.trackName);  
	self.runDays = data.stationFlag=='ZDZ'?'--':data.runDays;
	self.sourceDay = data.stationFlag=='SFZ'?'--':data.sourceDay;
	self.stationFlag = data.stationFlag;
	 
};

function openLogin() {
	$("#file_upload_dlg").dialog("open");
}

$(window.document).scroll(function () {
	    var scrolltop = $(document).scrollTop(); 
});