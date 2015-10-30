$(function() { 
	
	var appModel = new ApplicationModel();
	ko.applyBindings(appModel); 
	 
	appModel.init();   
});  
var childIndexs = [];
var setTrainNbr="";
var isCilckSetTrain = false;
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

var tempPlanLkTrainStnArray = [];


function ApplicationModel() {

	var _tabType = "";//bjdd(本局担当)   wjdd（外局担当）
	var self = this;
	//列车列表
	self.trains = ko.observableArray();
	//关键站列表
    self.stnArray = ko.observableArray();
	self.trainAllTimes = ko.observableArray();//保存列车详情时刻所有数据，用于简图复选框点击事件列表值变更来源
	
	self.trainLines = ko.observableArray();
	//交路列表   
	self.gloabBureaus = [];   
	//担当局
	self.searchModle = ko.observable(new searchModle()); 
	self.currentTrainInfoMessage = ko.observable("");
	self.currentTrainId = ko.observable(); 
	self.currentTrain = ko.observable(); 
	self.runPlanLkTrainStnRows = ko.observableArray();		//页面命令时刻列表
	self.isSelectAll = ko.observable(false);	//列表是否全选 	全选标识  默认false
	self.currentCmdTxtMl = ko.observable(new CmdTrainStnTimeRow(createEmptyTrainRow()));
	self.fillStnArray = ko.observableArray();//补点的列表	
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
		$("#train_time_dlg").dialog("close");  
		self.runPlanLkTrainStnRows.remove(function(item) {
			return true;
		});
		var array = window.parent.runPlanLkCmdPageModel.runPlanLkTrainStnRows();
		console.log('array='+array);
		var str = '['+array+']';//加个【】转为数组
//		console.log('str='+str);
		var data = eval('(' + str + ')');//把字符串转为数组
		tempPlanLkTrainStnArray = data;
		
//		 console.log("data="+data);
		if(data.length < 1){//如果只有一个站或者没有站，则给出提示信息
			showWarningDialog("补点的关键站必须是2个站以上");
			return;
		}
		for(var i=0; i< data.length;i++){
			var curObj = data[i];
			var nextObj = null;
			var obj = {};
			if(i!= data.length - 1){
				nextObj = data[i+1];
			}
			if(curObj!=null && curObj!=undefined && nextObj!=null && nextObj!=undefined){
				obj.nodeName = curObj.nodeName+"--->"+nextObj.nodeName;
				obj.startArrTime = curObj.arrTime;
				obj.startDptTime = curObj.dptTime;
				obj.endArrTime = nextObj.arrTime;
				obj.endDptTime = nextObj.dptTime;
				obj.childIndex = curObj.childIndex;
				self.runPlanLkTrainStnRows.push(new CmdTrainStnTimeRow(obj));
			}
			
		}
       
		commonJsScreenLock(2);
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
	
	/**
	 * 列表全选复选框change事件
	 */
	self.checkBoxSelectAllChange = function() {
		
		childIndexs.splice(0,childIndexs.length);//复选框清空事件
		
//		console.log("~~~~~~~~ 全选标识  _self.isSelectAll="+_self.isSelectAll());
		if (!self.isSelectAll()) {//全选     将runPlanLkTrainStnRows中isSelect属性设置为1
//			console.log("全选");
			$.each(self.runPlanLkTrainStnRows(), function(i, row){
				row.isSelect(1);
				childIndexs.push(row.childIndex());
			});
		} else {//全不选    将runPlanLkCMDRows中isSelect属性设置为0
//			console.log("全不选");
			$.each(self.runPlanLkTrainStnRows(), function(i, row){
				row.isSelect(0);
			});
		}
	};
	
	
	/**
	 * 设置事件
	 */
	self.setTrain = function() {
		if(childIndexs.length <1){
			showErrorDialog("请选择区段!");
			return;
		}
		
		var trainNbr = self.searchModle().trainNbr();  
		var chart = self.searchModle().chart(); 
		for(var i=0; i<self.runPlanLkTrainStnRows().length; i++){
//			var obj = self.runPlanLkTrainStnRows()[i];
			for(var j=0; j< childIndexs.length; j++){
				var childIndex2 = childIndexs[j];
				if(self.runPlanLkTrainStnRows()[i].childIndex() == childIndex2){
					self.runPlanLkTrainStnRows()[i].trainNbr(setTrainNbr);
					self.runPlanLkTrainStnRows()[i].chart(chart.name);
				}
			}
		}
//		childIndexs.splice(0,childIndexs.length);
//		setTrainNbr="";
//		isCilckSetTrain = true;
	};
	
	
	/**
	 * 补点事件
	 */
	self.fillSite = function() {
		if(childIndexs.length <1){
			showErrorDialog("请选择区段!");
			return;
		}
//		if(!isCilckSetTrain){
//			showErrorDialog("请选择区段!");
//			return;
//		}
		
		var trainLine = null;
		var trainStnRow = null;
		var temp = null;
		var stn1 = null;
		var stn2 = null;
		
		var startIndex =0;
		var endIndex =0;
		var trainNbr = self.searchModle().trainNbr();//车次  
		var chart = self.searchModle().chart();//方案 
		var trainLines = self.trainLines();//时刻表数据
		var trainStnRows =  self.runPlanLkTrainStnRows();//区段列表
		
		var detTime = null;
		var startDptTime = null;
		var detDate = null;//参照车，参照站出发时间
		var startDate =null;//设置车，出发站出发时间
		var endDate =null;//设置车，出发站出发时间
//		var dis = null;//相差秒数（正）
		
		var dephms = null;//记录始发站始发时间毫秒值（参照车）
		var starthms1 = null;
		var endhms1 = null;
//		var vector = null;//相差秒数（正负，参照车减去设置车）
		var ratio = null;//相对比例(设置车/参照车)就是参照车的站站时间间隔可能和设置车站站时间间隔不一样，需要按照比例来设置补点站时间（到达时间和出发时间是相同的）
		for(var i=0; i < trainLines.length; i++){
			for(var j=0; j < trainStnRows.length; j++){
				trainLine = trainLines[i];
				trainStnRow = trainStnRows[j];
				temp = trainStnRow.nodeName();
				stn1 = temp.substring(0, temp.indexOf('-'));
				stn2 = temp.substring(temp.indexOf(">")+1, temp.length);
				var indexStn1;
				var indexStn2;
				var mul_inner1;
				var mul_inner2;
				
				if(trainLine.nodeName == stn1){
					startIndex = i;//找到站的位置
					indexStn1 = i;
					
					detTime = "1973-01-01 "+(trainLine.sourceTime=='--'?trainLine.targetTime:trainLine.sourceTime);
					startArrTime = "1973-01-01 "+trainStnRow.startArrTime();
					startDptTime = "1973-01-01 "+trainStnRow.startDptTime();
					
					detDate = new Date(detTime);
					startDate = new Date(startArrTime);
					endDate = new Date(startDptTime);
					
					dephms =  detDate.getTime();//得到毫秒数,参照车
					starthms1 = startDate.getTime();//补点车得到毫秒数,到达时间
					endhms1 = endDate.getTime(); //补点车得到毫秒数,出发时间
//					vector = dephms - starthms;
//					if(dephms > starthms){
//						dis = dephms - starthms;
//					}else if(dephms < starthms){
//						dis = starthms - dephms ;
//					}
				}
				if(trainLine.nodeName == stn2){
					endIndex = i;
					indexStn2 = i;
					
					var endDetDate = new Date("1973-01-01 "+trainLine.sourceTime);//参照车
					var endStartDate = new Date("1973-01-01 "+trainStnRow.endArrTime());//补点车
					var endDetDateMM = endDetDate.getTime();
					var endStartDateMM = endStartDate.getTime();
					mul_inner1 = trainLines[indexStn2].sourceDay - trainLines[indexStn1].sourceDay;
					mul_inner2 = tempPlanLkTrainStnArray[j+1].arrRunDays-tempPlanLkTrainStnArray[j].arrRunDays;
					
					//参照车  stn1 和stn2  不在同一天的情况
					if(indexStn1==0){//参照车  stn1 和stn2  不在同一天的情况
						if(trainLines[indexStn2].sourceDay > 0){
							endDetDateMM = endDetDateMM + parseInt(trainLines[indexStn2].sourceDay) * 3600 * 1000 * 24;//参照车  跨天了
						}
					}
					else{
						if(mul_inner1 > 0){
							endDetDateMM = endDetDateMM + parseInt(mul_inner1) * 3600 * 1000 * 24;//参照车  跨天了
						}
					}//参照车  stn1 和stn2  不在同一天的情况 end
					
					//补点车  stn1 和stn2  不在同一天的情况 start
					if(j==0){//j=0的时候tempPlanLkTrainStnArray[0].arrRunDays= "--"
						if(tempPlanLkTrainStnArray[j+1].arrRunDays>0){
							endStartDateMM = endStartDateMM + parseInt(tempPlanLkTrainStnArray[j+1].arrRunDays) * 3600 * 1000 * 24;//补点车  跨天了
						}
					}
					else{
						if(mul_inner2 > 0){
							endStartDateMM = endStartDateMM + parseInt(mul_inner2) * 3600 * 1000 * 24;//补点车  跨天了
						}
					}//补点车  stn1 和stn2  不在同一天的情况 end
					ratio = (endStartDateMM - starthms1)/(endDetDateMM - dephms);//    补点车/参照车
				}
				//slice返回数组片段
				var train = null;
				if(endIndex >0){
					train =  trainLines.slice(startIndex,endIndex);
					var tobj = null;
					var startTime = null;
//					var endTime = null;
					var sdate = null;
					var edata = null;
					var starthms2 =  null;
					var starthms3 =  null;
					var endhms3 =  null;
					var endhms =  null;
					var shms =  null;
					var ehms =  null;
					var newSTime = null; 
					var endTime = null;
					var sdata=null; 
					var edata=null; 
					for(var k=0; k< train.length; k++){
						tobj = train[k];
						startTime = "1973-01-01 "+ (tobj.sourceTime=='--'?tobj.targetTime:tobj.sourceTime);//参照车到达时间
						endTime = "1973-01-01 "+ (tobj.targetTime=='--'?tobj.sourceTime:tobj.targetTime);//参照车
						sdate = new Date(startTime);//参照车
						edata = new Date(endTime);//参照车
						starthms2 =  sdate.getTime();//得到毫秒数//参照车
						endhms =  edata.getTime();//得到毫秒数//参照车
						
						
						if(indexStn1==0){//参照车  stn1 和stn2  不在同一天的情况
							if(trainLines[indexStn2].sourceDay > 0){
								if(tobj.sourceDay>0){
									starthms2 = starthms2 + parseInt(trainLine.sourceDay) * 3600 * 1000 * 24;//参照车  跨天了
								}
								if(tobj.targetDay>0){
									endhms = endhms + parseInt(trainLine.targetDay) * 3600 * 1000 * 24;//参照车  跨天了
								}
							}
						}
						else{
							if(mul_inner1 > 0){
								if(tobj.sourceDay>0){
									starthms2 = starthms2 + parseInt(trainLine.sourceDay) * 3600 * 1000 * 24;//参照车  跨天了
								}
								if(tobj.targetDay>0){
									endhms = endhms + parseInt(trainLine.targetDay) * 3600 * 1000 * 24;//参照车  跨天了
								}
							}
						}
						
						
						
						/* 暂时不处理suntao 20150724，但是需要考虑的是  出发到达天数
						
						//补点车  stn1 和stn2  不在同一天的情况 start
						if(j==0){//j=0的时候tempPlanLkTrainStnArray[0].arrRunDays= "--"
							if(tempPlanLkTrainStnArray[j+1].arrRunDays>0){
								endStartDateMM = endStartDateMM + parseInt(tempPlanLkTrainStnArray[j+1].arrRunDays) * 3600 * 1000 * 24;//补点车  跨天了
							}
						}
						else{
							if(mul_inner2 > 0){
								endStartDateMM = endStartDateMM + parseInt(mul_inner2) * 3600 * 1000 * 24;//补点车  跨天了
							}
						}//补点车  stn1 和stn2  不在同一天的情况 end
						
						*/
						
						//实际补点站得到达时间
						starthms3 = starthms1 + (starthms2 - dephms)*ratio
						endhms3 = endhms1 + (starthms2 - dephms)*ratio
//						shms =  starthms + dis;
//						ehms =  endhms + dis;
						newSTime = new Date(starthms3); //就得到补点车的时间了 ，停站 到达
						endTime = new Date(endhms3); //就得到补点车的时间了 ，停站 出发
						sdata=newSTime.format("hh:mm:ss"); 
						edata=endTime.format("hh:mm:ss"); 
						tobj.arrTime = sdata;
						tobj.sourceTime = sdata;
						if(k==0){
							tobj.detTime = edata;
							tobj.targetTime = edata;
						}
						else{
							tobj.detTime = sdata;
							tobj.targetTime = sdata;
							tobj.jobs = tobj.jobs.replace('<客运营业>','');
						}
					}
//					trainStnRow.stnTimeArray(train);
					trainStnRows[j].stnTimeArray(train);
					startIndex =0;
					endIndex =0;
					startFlag = false;
					endFlag = false;
				}	
			}
		}
		var array = [];
		for(var i=0; i < self.runPlanLkTrainStnRows().length; i++){
			var len = self.runPlanLkTrainStnRows()[i].stnTimeArray().length;
			var obj = self.runPlanLkTrainStnRows()[i];
			if(len ==0){
				showErrorDialog(obj.nodeName()+"  未匹配到对应参考站");
				continue;
			}
			if(len >0){
				array = array.concat(obj.stnTimeArray());
			}
			if(i==self.runPlanLkTrainStnRows().length-1){
				var zdz = trainLines[trainLines.length-1];
				zdz.arrTime = obj.endArrTime();
				zdz.sourceTime = obj.endArrTime();
				array = array.concat(zdz);
			}
		}
		self.trainLines.remove(function(item){
			return true;
		});
		self.trainLines(array);
		//调用 套用经由及时刻按钮点击事件
		self.useTrainStnAndTime();
	};
	
	
	/**
	 * 套用经由及时刻按钮点击事件
	 * 
	 * 将详情时刻表站名及时刻引用至临客命令 时刻表
	 * @author Think
	 * @since 2014-07-25
	 */
	self.useTrainStnAndTime = function() {
		
		if (self.trainLines().length == 0) {
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
		$.each(self.trainLines(), function(i, n){
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
	
	
	
	//选择区段
	self.setCurrentRec1 = function(row) {
		if(row.isSelect()==false ||row.isSelect()==0){
			row.isSelect(1);//复选框勾中
			self.currentCmdTxtMl(row);
			console.log("row.childIndex()="+row.childIndex());
			childIndexs.push(row.childIndex());
		}else{
//			childIndexs.remove(row.childIndex());
			childIndexs.splice(row.childIndex(), 1);
		}
		
		
		
		
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
	 
	self.showCrossTrainTimeDlg2 = function(){ 
		var trainId = self.currentTrainId();  
		if($('#train_time_dlg').is(":hidden")){  
			$("#train_time_dlg").find("iframe").attr("src", basePath+"/jbtcx/queryTrainTimesByTrainId?trainId=" + trainId);
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
	
	
	self.showTrainTimes = function(row) {
		var _stationTypeArray  =[];
		self.currentTrain(row);
		self.currentTrainId(row.id);
		setTrainNbr = row.name;
		self.currentTrainInfoMessage("车次：" + row.name + "&nbsp;&nbsp;&nbsp;" + row.startStn + "——" + row.endStn);
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
				
//				if(i == row.times().length - 1){
//					$("#plan_runline_table_trainLine").freezeHeader(); 
//				}
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
							
//							if ($.inArray(n.stationFlag, _stationTypeArray) > -1) {
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
		
		
		
	};  
	
	self.fuzzyChange = function(){
		if(self.searchModle().fuzzyFlag() == 0){
			self.searchModle().fuzzyFlag(1);
		}else{
			self.searchModle().fuzzyFlag(0);
		}
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
	
	
	
	function createEmptyTrainRow() {
		var _dataObj = {};
		_dataObj.nodeName = "";
		_dataObj.isSelect = "";
		_dataObj.childIndex = "";
		_dataObj.trainNbr = "";
		_dataObj.chart =  "";
		return _dataObj;
	};
	
}

function searchModle(){
	
	self = this;   
	
	self.startBureaus = ko.observableArray(); 
	
	self.endBureaus = ko.observableArray(); 
	
	self.charts = ko.observableArray(); 
 
	self.startBureau = ko.observable();
	
	self.endBureau = ko.observable();
	
	
	self.trainNbr = ko.observable(); 
	
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

function  CmdTrainStnTimeRow (data) {
	var self = this;
	self.nodeName = ko.observable(data.nodeName);//站名
	self.isSelect = ko.observable(0);
	self.childIndex = ko.observable(data.childIndex);//序号
	self.trainNbr = ko.observable(data.trainNbr);//车次
	self.chart =  ko.observable(data.chart);//方案
	self.stnTimeArray = ko.observableArray(); 
	self.startArrTime = ko.observable(data.startArrTime);
	self.startDptTime = ko.observable(data.startDptTime);
	self.endArrTime = ko.observable(data.endArrTime);
	self.endDptTime = ko.observable(data.endDptTime);
	
};


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
