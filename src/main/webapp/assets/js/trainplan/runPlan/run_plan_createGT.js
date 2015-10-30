var canvasData = {};  
var cross = null;
var oldchartName = null;
$(function() { 
	heightAuto();
	cross = new CrossModel();
	ko.applyBindings(cross); 
	
	cross.init();   
	

	
});



function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#plan_view_div_palnDayDetail").css("height",WH-150+"px");
};

var highlingFlags = [{"value": "0", "text": "普速"},{"value": "1", "text": "高铁"},{"value": "2", "text": "混合"}];
var checkFlags = [{"value": "1", "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "0", "text": "未"}, {"value": "1", "text": "已"},{"value": "2", "text": "全"}];
var highlingrules = [{"value": "1", "text": "平日"},{"value": "2", "text": "周末"},{"value": "3", "text": "高峰"}];
var commonlinerules = [{"value": "1", "text": "每日"},{"value": "2", "text": "隔日"}];
var isAll = false;
var isAllCount =0;
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


//{unitCrossId:"1",status: "1"}
function updateTrainRunPlanStatus(message){  
	var runPlan = $.parseJSON(message);
//	console.log("---------  更新交路的当前状态 ----------"+message);
	cross.updateTrainRunPlanStatus(runPlan);
	
}
//{unitCrossId:"1",trainNbr: "G1", day:"20140723", runFlag: "1"}; 
function updateTrainRunPlanDayFlag(data){ 
	var runPlan = $.parseJSON(data);
//	console.log("---------  更新车次每天当前状态 ----------"+data);
	cross.updateTrainRunPlanDayFlag(runPlan);
}

var gloabBureaus = [];

function CrossModel() {
	var self = this;
		//列车列表
	self.trains = ko.observableArray();
	
	self.stns = ko.observableArray();
	//交路列表 
	self.crossAllcheckBox = ko.observable(0); 
	//当前展示的列车信息
	self.trainPlans = ko.observableArray();
	//表头的数据列表
	self.planDays = ko.observableArray(); 
	//路局列表
	self.gloabBureaus = [];    
	//担当局
	self.searchModle = ko.observable(new searchModle());  
	//数据表格的宽度
	self.runPlanTableWidth = ko.computed(function(){
		return self.planDays().length * 40 + 80 + 'px';
	});
	//总共需要生成的数量
	self.createRunPlanTotalCount = ko.observable(0);
	//已生成数量
	self.createRunPlanCompletedCount = ko.observable(0);
	self.selectedRunPlan = ko.observableArray();
	//已生成数量
	self.createRunPlanErrorCount = ko.observable(0);
	//生成说明文字
	self.completedMessage = ko.computed(function(){ 
		return self.createRunPlanTotalCount() == 0 ? "" : "选中：" + self.createRunPlanTotalCount() + "个交路，目前已成功生成：" + self.createRunPlanCompletedCount() + "个交路的开行计划，另有：" + self.createRunPlanErrorCount() + "个交路生成开行计划失败";
	});
	
	//更新列车某天的开行情况
	self.updateTrainRunPlanDayFlag = function(runPlan){
		$.each(self.trainPlans(), function(x, n){ 
//			console.log("-----");
//			console.log("startStn=" + n.startStn + "," + runPlan.startStn);
//			console.log("endStn=" + n.endStn + "," + runPlan.endStn);
			if(n.unitCrossId == runPlan.unitCrossId && n.trainNbr == runPlan.trainNbr 
					//&& n.trainSort == runPlan.trainSort
					&& n.startStn == runPlan.startStn
					&& n.endStn == runPlan.endStn
					){
				for(var i = 0; i < n.runPlans().length; i++){ 
					if(n.runPlans()[i].day.replace(/-/g, "") == runPlan.day){
						n.runPlans()[i].runFlag(runPlan.runFlag);
						break;
					};
				};
				return false;
			};
		});
	};
	//更新交路的当前状态 status 1：未完成 2：完成
	self.updateTrainRunPlanStatus = function(runPlan){
		if(runPlan.status == 2){
			self.createRunPlanCompletedCount(self.createRunPlanCompletedCount() + 1);
		}else if(runPlan.status == -1){
			self.createRunPlanErrorCount(self.createRunPlanErrorCount() + 1);
		}
		else if (runPlan.status == -2) {
			self.createRunPlanErrorCount(self.createRunPlanErrorCount() + 1);
		}
		$.each(self.trainPlans(), function(x, n){
			if(n.unitCrossId == runPlan.unitCrossId && n.trainSort == 0){ 
				n.createStatus(runPlan.status);
				return false;
			};
		});
	}; 
	
	self.dragRunPlan = function(n,event){
		$(event.target).dialog("open"); 
	};  
	
	//全选按钮
	self.selectCrosses = function(){
		$.each(self.trainPlans(), function(i, crossRow){
			if(self.crossAllcheckBox() == 1){//全不选
				
				$.each(crossRow.runPlans(), function(z, n){
					if((n.createFlag() == 0||n.createFlag() == "0")&& n.runFlagShowValue() !=""){
						n.selected(0);
						self.selectedRunPlan.remove(n);
					}
//					if(n.canCheck2){
//						n.selected = true;
//					}
//					else{
//						n.selected = false;
//					}
				});
				crossRow.selected(0);
				isAll = false;
			}else{//全选
				if(crossRow.canCheck2){
//					crossRow.selected = true;
					$.each(crossRow.runPlans(), function(z, n){
						if((n.createFlag() == "0" || n.createFlag() == 0)&& n.runFlagShowValue() !=""){
							n.selected(1);
							if ($.inArray(n, self.selectedRunPlan()) <0) {//已选中集合中不包含该对象
								self.selectedRunPlan.push(n);
							}
						} 
					}); 
					crossRow.selected(1);
				}
				else{
					crossRow.selected = false;
				}
				isAll = true;
			}  
		});
		isAllCount =0;
		isAllCount = $("input[type='checkbox'][name='checkedAll'][value='1']").length;
	};
	
	//数据checkbox点击事件
	self.selectCross = function(row){ 
		if(row.selected() == 0){  //整条交路全选
			$("#handCreatePlanCrossName").text(row.crossName);
			self.crossAllcheckBox(1);   
			$.each(self.trainPlans(), function(i, crossRow){   
				$.each(self.trainPlans(), function(i, crossRow){   
					//$("#handCreatePlanCrossName").text(crossRow.crossName);
					if(crossRow.selected() != 1 && crossRow != row){
						self.crossAllcheckBox(0);
						isAll = false;
						return false;
					} 
				}); 
			});  
			$.each(self.trainPlans(), function(i, train){
				if(train.crossName == row.crossName){ 
					$.each(train.runPlans(), function(z, n){
						if(n.createFlag() == "0"){
							n.selected(1);
							if ($.inArray(n, self.selectedRunPlan()) <0) {//已选中集合中不包含该对象
								self.selectedRunPlan.push(n);
							}
						} 
					});
				}
			});
		}else{
			$("#handCreatePlanCrossName").text("");
			$.each(self.trainPlans(), function(i, train){
				if(train.crossName == row.crossName){
					train.selected(0);
					$.each(train.runPlans(), function(z, n){
						if(n.createFlag() == 0){
							n.selected(0);
							self.selectedRunPlan.remove(n);
						} 
						self.crossAllcheckBox(0);
					});
				}
			});
		}; 
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
	
	//列车输入框的大写转换事件
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	}; 
	 
	//currentIndex 
	//格式化出yyyy-MM-dd这样的字符串
	self.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year+"-"+month+"-"+days;
	};
	//格式化出MMdd这样的字符串
	self.dayHeader =function(d){ 
	 
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return month + "-"+ days;
	};
	//格式化出一、二
	self.getWeek = function(d){
		 var week = ""; 
		//获取当前星期X(0-6,0代表星期天)
		if(d.getDay()==0)          week="日"; 
		if(d.getDay()==1)          week="一"; 
		if(d.getDay()==2)          week="二"; 
		if(d.getDay()==3)          week="三"; 
		if(d.getDay()==4)          week="四"; 
		if(d.getDay()==5)          week="五"; 
		if(d.getDay()==6)          week="六"; 
		
		return week;
	 };
	
	 //获取30天后的时日期
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
		
		// 开始日期.
		$("#runplan_input_startDate").datepicker();
		
		
		//手动结束时间
		$("#create_plan_endDate").datepicker();
		// 生成日期.
		$("#produce_runplan_startDate").datepicker();
		
		// 截至日期.
		$("#runplan_input_endDate").datepicker();
		
		// 交路开行日期.
		$("#cross_start_date").datepicker();
		
		//x放大2倍 
		$("#produce_runplan_days").val(30); 
		self.searchModle().planStartDate(self.currdate());
		self.searchModle().producePlanStartDate(self.currdate());
		self.searchModle().planEndDate(self.get40Date());
		self.searchModle().createPlanEndDate(self.get40Date());
		commonJsScreenLock(2);
		//获取方案列表
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
		//获取路局列表
	    $.ajax({
			url : "../plan/getFullStationInfo",
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {  
				var bureaus = new Array();
				if (result != null && result != "undefind" && result.code == "0") { 
					var curUserBur = currentBureauShortName;
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
					self.searchModle().loadBureau(bureaus); 
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
	    
	    self.initDataHeader();
	};  
	//查询所有的交路单元，因为没有使用提供未分页的查询函数，这里设置一个无法达到的总数5000后台提供以后可以替换掉
	self.loadCrosses = function(){
		self.crossRows.loadRows();//.loadCrosseForPage(0, 5000);
	};
	
	function loadCrosses() {
		self.crossRows.loadRows();
	};
	
	//初始化数据表的表头数据
	self.initDataHeader = function(){
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate =  $("#runplan_input_endDate").val();   

		self.crossAllcheckBox(0);
		var currentTime = new Date(startDate);
		var endTime = new Date(endDate);
		//endTime.setDate(endTime.getDate() + 10); 
		var endTimeStr = self.dayHeader(endTime);  
		self.planDays.remove(function(item) {
			return true;
		});  
		self.trainPlans.remove(function(item) {
			return true;
		});
		
		self.planDays.push({"day": self.dayHeader(currentTime).replace(/-/g, ""), "week": self.getWeek(currentTime)}); 
		while(self.dayHeader(currentTime) != endTimeStr){
			currentTime.setDate(currentTime.getDate() + 1); 
			self.planDays.push({"day": self.dayHeader(currentTime).replace(/-/g, ""), "week": self.getWeek(currentTime)}); 
		} 
		
		
	};
	//查询所有的交路单元
	self.loadCrosseForPage = function(startIndex, endIndex) {  
		
		var re =  /^[\w\u4e00-\u9fa5]+$/gi;
		var reg = new RegExp(/^((((19|20)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((19|20)\d{2})-(0?[469]|11)-(0?[1-9]|[12]\d|30))|(((19|20)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-(0?[1-9]|[12]\d)))$/);
		
    	var str = $("#input_cross_filter_trainNbr").val().trim();
    /*	if(str!="" && str!=null && str!=undefined){
    		if(!re.test(str)){      
                showErrorDialog("不能输入特殊字符");
                return ;
        	}
    	}*/
    	var str = $("#runplan_input_startDate").val().trim();
    	if(str!="" && str!=null && str!=undefined){
    		if(!reg.test(str)){      
                showErrorDialog("请输入有效日期");
                return ;
        	}
    	}
//    	var str = $("#produce_runplan_startDate").val().trim();
//    	if(str!="" && str!=null && str!=undefined){
//    		if(!reg.test(str)){  
//                showErrorDialog("请输入有效日期");
//                return ;
//        	}
//    	}
		
    	var str = $("#runplan_input_endDate").val().trim();
    	if(str!="" && str!=null && str!=undefined){
    		if(!reg.test(str)){       
                showErrorDialog("请输入有效日期");
                return ;
        	}
    	}
    	
		var startDate = $("#runplan_input_startDate").val(); 
		var endDate =  $("#runplan_input_endDate").val();  
		if(startDate!=null && startDate!=undefined && endDate!=null && endDate!=undefined){
			if(startDate > endDate){
				showWarningDialog("开始日期不能大于截止日期!");
				return ;
			}
		}
		
		var str = $("#cross_start_date").val().trim();
    	if(str!="" && str!=null && str!=undefined){
    		if(!reg.test(str)){       
                showErrorDialog("请输入有效日期");
                return ;
        	}
    	}
		
	 	commonJsScreenLock();
		 
		var bureauCode = self.searchModle().bureau();  
		var trainNbr = self.searchModle().filterTrainNbr();  
		if(!trainNbr){
			trainNbr = '';
		}
		var startBureauCode = self.searchModle().startBureau();   
		var chart = self.searchModle().chart();   
	
		$("#searchModleHiddenValue").val(chart.name);
		oldchartName = chart.name;
		self.initDataHeader();
		
    	var isFuzzy = 0;
    	if($("#isFuzzy")[0].checked){
    		isFuzzy = 1;
    	}
		$.ajax({
			    url : basePath+"/runPlan/getTrainRunPlansForCreateGT",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					startDate : startDate.replace(/-/g, ""),
					endDate : endDate.replace(/-/g, ""),
					tokenVehBureau : bureauCode, 
					startBureau : startBureauCode,
					trainNbr : trainNbr,
					chartId : chart.chartId,
					rownumstart : startIndex, 
					rownumend : endIndex,
					isFuzzy : isFuzzy,
					type:"creatPlan"//只查询已审核交路信息
					,cross_start_date : $("#cross_start_date").val().replace(/-/g, "")
				}),
				success : function(result) {
					var _chirldrenIndex = 0;
					var rows = [];
					if(null != $("#cross_start_date").val() && "" != $("#cross_start_date").val()){
						$("#produce_runplan_startDate").val($("#cross_start_date").val());
					}
					if (result != null && result != "undefind" && result.code == "0") { 
						var trainPlans = {};
						var sort=0;
						var trainNbr="";
						var cn;
						var pcId = "";
						 $.each(result.data.data, function(z, n){
//								if(cn != n.crossName){
//								sort = 0;
//							}
//							if(pcId != n.unitCrossId){
//								sort = 0;
//							}
							if(trainNbr!=n.trainNbr){
								trainNbr=n.trainNbr;
								//cn = n.crossName;
								pcId = n.planCrossId;
//								++sort;
							}
//							console.log(n.unitCrossId);
//							console.log(trainPlans[n.unitCrossId]);
						 var planCross = trainPlans[n.unitCrossId];
//							 var planCross = null;
//							 if(n.crossName!=null && n.crossName!=undefined){
//								 planCross = trainPlans[n.crossName];
//							 }
//						 console.log("planCross::" + planCross);
//						 console.log("self.trainPlans()" + self.trainPlans());
						 if(planCross == null){
//							 console.log("planCross == null");
							 var trainPlanData = {
										unitCrossId : n.unitCrossId,
										crossName: n.crossName, 
										planCrossId: n.planCrossId,
										tokenVehBureau: n.tokenVehBureau,
										startDate: startDate,
										endDate: endDate, 
										baseTrainId: n.baseTrainId,
										createFlag: 0,
										trainSort: 0,
										chirldrenIndex : _chirldrenIndex,
										cross_start_date : n.cross_start_date,
										startStn: n.startStn,
										endStn: n.endStn
								};
							 
							 rows.push(trainPlanData);//分页集合对象，只用记录条数，数据暂未使用
							 
							 _chirldrenIndex ++ ;
								//默认吧交路作为第一条记录
							    var planCross = new TrainRunPlanRow(trainPlanData);
								self.trainPlans.push(planCross);
								var crossNames = n.crossName.split("-");
								var tn = "";
								var pcid = "";
								var forRdd = 0;
								

//								if(n.runDay != null){
									// 有开行计划,就需要对交路进行分割，填写对应的始发/终到站信息，以便可以在686行进行准确判断
									$.each(result.data.data, function(rdd, rdd1){
										if(rdd1.groupSerialNbr == 1 || rdd1.groupSerialNbr == null){
											if(n.crossName == rdd1.crossName){
												if(tn != rdd1.trainNbr){
													if(pcid != rdd1.unitCrossId){
														forRdd = 0;
														pcid = rdd1.unitCrossId;
													}
													if(n.runDay != null){
//														console.log(pcid);
														var trainPlanData = {
																unitCrossId : pcid,
																crossName: rdd1.crossName, 
																startDate: startDate,
																endDate: endDate,
																planCrossId: rdd1.planCrossId,
																trainNbr: rdd1.crossName.split("-")[forRdd], 
																trainSort: forRdd + 1,
																createFlag: 0,
																startStn: rdd1.startStn,
																endStn: rdd1.endStn,
																tokenVehBureau: rdd1.tokenVehBureau
														};
														self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
														tn = rdd1.trainNbr;
														forRdd++;
													}else{
//														console.log("没有开行计划");
														for(var i = 0; i < crossNames.length; i++){
//															console.log(i);
															$.each(result.data1, function(a, b){
//																console.log("----" + a + "----");
//																console.log("unitCrossId=" + n.unitCrossId + "," + b.unitCrossId);
																if(n.unitCrossId == b.unitCrossId){
//																	console.log("trainNbr=" + crossNames[i] + "," + b.trainNbr);
																	if(crossNames[i] == b.trainNbr && (i+1) == b.trainSortBase){
//																		console.log("trainSort=" + (i+1) + "," + b.trainSort);
																		var trainPlanData = {
																				unitCrossId : n.unitCrossId,
																				crossName: n.crossName, 
																				startDate: startDate,
																				endDate: endDate,
																				planCrossId: n.planCrossId,
																				trainNbr: crossNames[i], 
																				trainSort: i + 1,
																				createFlag: 0,
																				startStn: b.startStn,
																				endStn: b.endStn,
																				tokenVehBureau: n.tokenVehBureau
																		};
																		self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
																		return false;
																	}
																}
															})
														}
													}
												}
											}
										}
									})
//								}else{
//									// 没有开行计划,就不存在“开”怎么显示，不存在车次应该显示的那个位置，只需要在列表中显示出来就行
//									for(var i = 0; i < crossNames.length; i++){
//										var trainPlanData = {
//												unitCrossId : n.unitCrossId,
//												crossName: n.crossName, 
//												startDate: startDate,
//												endDate: endDate,
//												planCrossId: n.planCrossId,
//												trainNbr: crossNames[i], 
//												trainSort: i + 1,
//												createFlag: 0,
//												startStn: n.startStn,
//												endStn: n.endStn,
//												tokenVehBureau: n.tokenVehBureau
//										};
//										self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
//									} ; 
//								}
								
								/** 1.0 **/
//								if(n.runDay != null){
//									// 有开行计划,就需要对交路进行分割，填写对应的始发/终到站信息，以便可以在686行进行准确判断
//									$.each(result.data.data, function(rdd, rdd1){
//										if(rdd1.groupSerialNbr == 1 || rdd1.groupSerialNbr == null){
//											if(n.crossName == rdd1.crossName){
//												if(tn != rdd1.trainNbr){
//													if(pcid != rdd1.unitCrossId){
//														forRdd = 0;
//														pcid = rdd1.unitCrossId;
//													}
////													console.log(pcid);
//													var trainPlanData = {
//															unitCrossId : pcid,
//															crossName: rdd1.crossName, 
//															startDate: startDate,
//															endDate: endDate,
//															planCrossId: rdd1.planCrossId,
//															trainNbr: rdd1.crossName.split("-")[forRdd], 
//															trainSort: forRdd + 1,
//															createFlag: 0,
//															startStn: rdd1.startStn,
//															endStn: rdd1.endStn,
//															tokenVehBureau: rdd1.tokenVehBureau
//													};
//													self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
//													tn = rdd1.trainNbr;
//													forRdd++;
//												}
//											}
//										}
//									})
//								}else{
//									// 没有开行计划,就不存在“开”怎么显示，不存在车次应该显示的那个位置，只需要在列表中显示出来就行
//									for(var i = 0; i < crossNames.length; i++){
//										var trainPlanData = {
//												unitCrossId : n.unitCrossId,
//												crossName: n.crossName, 
//												startDate: startDate,
//												endDate: endDate,
//												planCrossId: n.planCrossId,
//												trainNbr: crossNames[i], 
//												trainSort: i + 1,
//												createFlag: 0,
//												startStn: n.startStn,
//												endStn: n.endStn,
//												tokenVehBureau: n.tokenVehBureau
//										};
//										self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
//									} ; 
//								}
								/** 原版 **/
//								for(var i = 0; i < crossNames.length; i++){
//									var trainPlanData = {
//											crossName: n.crossName, 
//											startDate: startDate,
//											endDate: endDate,
//											planCrossId: n.planCrossId,
//											trainNbr: crossNames[i], 
//											trainSort: i + 1,
//											createFlag: 0,
//											startStn: n.startStn,
//											endStn: n.endStn,
//											tokenVehBureau: n.tokenVehBureau
//									};
//									console.log("for循环" + i + "次" + ",startStn=" + n.startStn + ",endStn=" +  n.endStn);
//									self.trainPlans.push(new TrainRunPlanRow(trainPlanData));
//								} ; 
								trainPlans[n.unitCrossId] = planCross; 
						 } 
//						 console.log("↑↑↑↑" +  self.trainPlans().length);
//						 console.log("-----------------------------------");
//						 console.log(trainPlans);
						 $.each(self.trainPlans(), function(x, t){ 
//							 console.log("↑↑↑↑" +  n.trainNbr + "," + n.startStn + "↓↓↓↓" +  t.trainNbr + "," + t.startStn);
//							 console.log("↑↑↑↑" +  n.trainNbr + "," + sort + "↓↓↓↓" +  t.trainNbr + "," + t.trainSort);
							 // 取消trainSort判断，是因为现在讲“开”显示改为：第二组的车辆根据 始发/终到确定在第一组的位置
								if(t.planCrossId == n.planCrossId && t.trainNbr == n.trainNbr
										//&& t.trainSort == sort
										&& t.startStn == n.startStn
										&& t.endStn == n.endStn
								){
									for(var i = 0; i < t.runPlans().length; i++){ 
										if(t.runPlans()[i].day.replace(/-/g, "") == n.runDay){ 
											t.runPlans()[i].runFlag(parseInt(n.runFlag));
											t.runPlans()[i].createFlag(parseInt(n.createFlag));
											t.runPlans()[i].planTrainId(n.planTrainId);
											t.runPlans()[i].baseTrainId(n.baseTrainId);
											break;
										};
									};
									return false;
								};
						});
						  
					 });
						
						
						self.crossRows.loadPageRows(result.data.totalRecord, rows);
						 
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
	//必须定义在load函数之后
	self.crossRows = new PageModle(20, self.loadCrosseForPage);  
   
	//生成运行线
	self.createTrainLines = function(){  
		 var crossIds = [];
		 var crossNames = [];
		 var createCrosses = [];//选中交路总数
		 var startDate = $("#runplan_input_startDate").val();
		 var produceStartDate = $("#produce_runplan_startDate").val(); 
		 var endDate =  $("#runplan_input_endDate").val();  
		// var days = GetDays(startDate, endDate);
			var days = $("#produce_runplan_days").val();
			var reg = new RegExp("^[0-9]*$");
			if(!reg.test(days)){
				$("#produce_runplan_days").val("30");
				showErrorDialog("生成天数必须是数字类型");
				return;
			}
       	 
       	   if(days >= 50){
	       		$("#produce_runplan_days").val("30");
				showErrorDialog("生成天数不能超过50天");
				return;
       	   }
		 
		 var chart = self.searchModle().chart();
	     if(chart == null){
	    	showErrorDialog("请选择一个方案"); 
	    	return;
	     }
	     else{
		     if(chart.name != $("#searchModleHiddenValue").val()){
		    	 showWarningDialog("方案已改变，请重新查询结果");
		    	 return;
		     }
	     }
	     showConfirmDiv("提示", "确认要生成方案：" + chart.name + " 对应的开行计划吗?", function (r) { 
		        if (r) {
				     var crosses = self.trainPlans();
					 for(var i = 0; i < crosses.length; i++){   
						if(crosses[i].selected() == 1){  
							crossIds.push(crosses[i].unitCrossId);
							crossNames.push(crosses[i].crossName);
							crosses[i].createStatus(3);//1:正在生成    2：已生成   3：等待生成
							createCrosses.push(crosses[i]); 
						 }
					 }
					 
					 //重置生成总数和已生成数
					 if(isAll){
						 self.createRunPlanTotalCount(isAllCount); 
					 }else{
						 self.createRunPlanTotalCount(createCrosses.length); 
					 }
					 self.createRunPlanCompletedCount(0);
			
					 
					 if(crossIds.length == 0){
						 showWarningDialog("未选中数据");
						 return;
					 }
					 
					 if(null == days || "" == days){
						 showWarningDialog("请填写生成天数");
						 return;
					 }
					 
					 commonJsScreenLock();
					 $.ajax({
						 url : "../runPlan/plantrain/gen",
							cache : false,
							type : "POST",
							dataType : "json",
							contentType : "application/json",
							data : JSON
									.stringify({
										baseChartId : chart.chartId,
										startDate : produceStartDate
												.replace(/-/g, ""),
										// days: days + 1,
										days : days,
										unitcrossId : crossIds,
										crossName : crossNames,
										msgReceiveUrl : "/trainplan/runPlan/runPlanCreate"
									}),
							success : function(result) {
								if(result.code == 0 && result.data != null && result.data.length > 0){
									showSuccessDialog("正在生成开行计划");
								}
								else {
									showErrorDialog("生成开行计划失败  " + result.message);
									loadCrosses();
//									for (var i = 0; i < createCrosses.length; i++) {
//										createCrosses.createStatus(0);
//									}		
								}																
							},
							error : function() {
								showErrorDialog("生成开行计划失败");
								loadCrosses();
//								for (var i = 0; i < createCrosses.length; i++) {
//									createCrosses.createStatus(0);
//								}								
							},
							complete : function(){
								//loadCrosses();
								commonJsScreenUnLock();
								
							}
						}); 
//					 $.ajax({
//							url : "../runPlan/plantrain/gen",
//							cache : false,
//							type : "POST",
//							dataType : "json",
//							contentType : "application/json",
//							data :JSON.stringify({
//								baseChartId: chart.chartId,
//								startDate: produceStartDate.replace(/-/g, ""), 
//								//days: days + 1, 
//								days: days, 
//								unitcrossId: crossIds,
//								crossName: crossNames,
//								msgReceiveUrl: "/trainplan/runPlan/runPlanCreate"}),
//							success : function(result) { 
//								if(result != null && result.length >= 0){ 
//									showSuccessDialog("正在生成开行计划");
//									//self.loadCrosses();
//								}else{
//									showErrorDialog("生成开行计划失败");
//								}
//							},
//							error : function() {
//								showErrorDialog("生成开行计划失败");
//								 for(var i = 0; i < createCrosses.length; i++){   
//									 createCrosses.createStatus(0);
//								 }  
//							},
//							complete : function(){ 
//								commonJsScreenUnLock();
//							}
//						}); 
		        }
		        else{
		        	return false;
		        }    
			 });
	}; 
	
	self.bureauChange = function(){ 
		if(hasActiveRole(self.searchModle().bureau())){
			self.searchModle().activeFlag(1);  
		}else if(self.searchModle().activeFlag() == 1){
			self.searchModle().activeFlag(0); 
		}
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
	
	self.canCheckAll = ko.observable(false);
	
	//强制生成
	self.handOpen_force = function(){
		var chart = self.searchModle().chart();
		showConfirmDivIsNow(
				"提示",
				"是否将“ " + chart.name + " ”方案中本局担当的所有基本交路生成开行计划？",
				function(r) {
					if (r) {
						$.ajax({
							url : "/trainplan/operationplan/doNewPLanWork",
							cache : false,
							type : "POST",
							dataType : "json",
							contentType : "application/json",
							data : "",
							success : function() {
								
							},
							error : function() {
								
							},
							complete : function(){
								
							}
						});
						alertMessage("success","数据正在生成中，请稍后进行查询！");
						$("#generateInfo").show();
						
					} else {
						$('#setTime').show().dialog({
					        title: '设置时间',
					        width: 400,
					        height: 200,
					        cache: false,
					        modal: true
					    });
					    $('#setTime').dialog('open');
					}
				});

	}
	
	//生成开行计划 suntao cm_t
	
	self.createPlan = function() {
		
		var crossIds = [];
		var crossNames = [];
		var createCrosses = [];// 选中交路总数
//		var startDate = $("#runplan_input_startDate").val();
//		var produceStartDate = $("#produce_runplan_startDate").val();
		var endDate = $("#create_plan_endDate").val();
		// var days = GetDays(startDate, endDate);
//		var reg = new RegExp("^[0-9]*$");
//		if(!reg.test(days)){
//			$("#produce_runplan_days").val("30");
//			showErrorDialog("生成天数必须是数字类型");
//			return;
//		}
		var chart = self.searchModle().chart();
		if (chart == null) {
			showErrorDialog("请选择一个方案");
			return;
		} else {
			if (chart.name != oldchartName) {
//				if (chart.name != $("#searchModleHiddenValue").val()) {
				showWarningDialog("方案已改变，请重新查询结果");
				return;
			}
		}
		var eventCrossName = window.event.srcElement.parentElement.innerText.split('（')[0];
		
		showConfirmDiv(
				"提示",
				"是否将基本交路“" + eventCrossName + "”生成开行计划？",
				function(r) {
					if (r) {
						var crosses = self.trainPlans();
						for (var i = 0; i < crosses.length; i++) {
							if (crosses[i].selected() == 1 || eventCrossName == crosses[i].crossName) {
								crossIds.push(crosses[i].unitCrossId);
								crossNames.push(crosses[i].crossName);
								crosses[i].createStatus(3);// 1:正在生成 2：已生成
															// 3：等待生成
								createCrosses.push(crosses[i]);
								break;
							}
						}

						// 重置生成总数和已生成数
						if (isAll) {
							self.createRunPlanTotalCount(isAllCount);
						} else {
							self.createRunPlanTotalCount(createCrosses.length);
						}
						self.createRunPlanCompletedCount(0);

						if (crossIds.length == 0) {
							showWarningDialog("未选中数据");
							return;
						}
						if (crossIds.length > 1) {
							showWarningDialog("只能选取一个交路");
							return;
						}


						commonJsScreenLock();
						 $.ajax({
							 url : "..//operationplan/plantrain/gen",
								cache : false,
								type : "POST",
								dataType : "json",
								contentType : "application/json",
								data : JSON
										.stringify({
											baseChartId : chart.chartId,
//											startDate : produceStartDate
//													.replace(/-/g, ""),
											endDate : endDate
													.replace(/-/g, ""),
											// days: days + 1,
											unitcrossId : crossIds,
											crossName : crossNames,
											msgReceiveUrl : "/trainplan/runPlan/runPlanCreate"
										}),
								success : function(result) {
//									if(result.code == 0 && result.data != null && result.data.length > 0){
//										showSuccessDialog("正在生成开行计划");
//									}
//									else {
//										showErrorDialog("生成开行计划失败  " + result.message);
//										loadCrosses();
//									}
									showSuccessDialog("生成开行计划成功");
										
								},
								error : function() {
									showErrorDialog("生成开行计划失败");
									loadCrosses();
								},
								complete : function(){
									//loadCrosses();
									commonJsScreenUnLock();
									
								}
							}); 
						
					} else {
						return false;
					}
				});
		
	};
	
	//自动生成设置
	self.autoCreatePlan = function() {
		
		var isAutoGenerate = $("#isAutoGenerate").val();
		
		if (isAutoGenerate == -1) {
			showErrorDialog("请选择自动生成开关");
			return;
		}
		
		var createDays = $("#createDays").val();
		var createHours = $("#createHours").val();
		
		var buearu = self.searchModle().bureau();
		
//		commonJsScreenLock();
		$.ajax({
			url : "/trainplan/operationplan/saveAutoGenInfo",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON
			.stringify({
				 isAutoGenerate : isAutoGenerate,
				 genTime : createHours,
				 maintainDays : createDays,
				 tokenVehBureau : buearu
			}),
			success : function(result) {
				if(result.code == 0 && result.data != null && result.data.length > 0){
					showSuccessDialog("设置成功");
				}
				else {
					showErrorDialog("设置失败  " + result.message);
					loadCrosses();
				}
				
				
			},
			error : function() {
				showErrorDialog("设置失败");
				loadCrosses();
			},
			complete : function(){
				//loadCrosses();
//								commonJsScreenUnLock();
				
			}
		}); 
		
		
	};
}

function searchModle(){
	self = this;  
	 
	self.activeFlag = ko.observable(0);   
	 
	self.planStartDate = ko.observable(); 
	
	// 交路始发日期,默认为空
	self.cross_start_date = ko.observable();
	
	self.producePlanStartDate = ko.observable();
	
	self.createPlanEndDate = ko.observable();
	
	self.planEndDate = ko.observable();
	
	self.bureaus = ko.observableArray();
	
	self.startBureaus = ko.observableArray();
	
	self.charts = ko.observableArray();
	  
	self.bureau = ko.observable();
	 
	self.chart =  ko.observable(); 
	
	self.startBureau = ko.observable();
	
	self.filterTrainNbr = ko.observable(); 
	
	self.loadBureau = function(bureaus){   
		for ( var i = 0; i < bureaus.length; i++) {  
			self.bureaus.push(new BureausRow(bureaus[i])); 
			self.startBureaus.push(new BureausRow(bureaus[i]));  
		} 
	}; 
	
	self.loadChats = function(charts) {
		self.charts.push({
			"chartId" : null,
			"name" : "全部方案"
		});
		for (var i = 0; i < charts.length; i++) {
			self.charts.push({
				"chartId" : charts[i].schemeId,
				"name" : charts[i].schemeName
			});
		}
	};
	
}

function BureausRow(data) {
	var self = this;  
	self.shortName = data.ljjc;   
	self.code = data.ljpym;   
	//方案ID 
}   

function TrainModel() {
	var self = this;
	self.rows = ko.observableArray();
	self.rowLookup = {};
} 
 
function filterValue(value){
	return value == null || value == "null" ? "--" : value;
};

function TrainRunPlanRow(data){
var self = this; 
	
	self.chirldrenIndex = data.chirldrenIndex;//用于界面序号显示
	self.unitCrossId = data.unitCrossId;
	self.trainNbr = data.trainNbr;
	self.crossName = data.crossName;
	self.runPlans =  ko.observableArray();  
	self.selected = ko.observable(0); 
	self.cross_start_date = data.cross_start_date;
	
	self.startStn =data.startStn; 
	self.endStn =data.endStn; 
	
	self.planCrossId = data.planCrossId;
	self.trainNbr = data.trainNbr;
	self.createStatus = ko.observable(0); 
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
		 if(result == currentBureauShortName){
			 self.canCheck2 = true;
		 }
		 else
			 self.canCheck2 = false;
		 return result; 
});
	
	self.createStatusShowValue = ko.computed(function(){
		switch (self.createStatus()) {
			case 0: 
				return self.crossName + "（" + self.cross_start_date + "）";//"";
				break;
			case 3: 
				return self.crossName + "（" + self.cross_start_date + "）"+"&nbsp;&nbsp;(等待生成)";//"";
				break;
			case 1: 
				return self.crossName + "（" + self.cross_start_date + "）"+"&nbsp;&nbsp;<span class='label label-info'>(正在生成。。。。。。)</span>";//"(正在生成。。。。。。)";
				break;
			case 2: 
				return self.crossName + "（" + self.cross_start_date + "）"+"&nbsp;&nbsp;<span class='label label-success'>(已生成开行计划)</span>";//"(已生成开行计划)";
				break;	
			case -1: 
				return self.crossName + "（" + self.cross_start_date + "）"+"&nbsp;&nbsp;<span class='label label-danger'>(发生异常)</span>";//"(发生异常)";
				break;
			case -2:
				return self.crossName + "（" + self.cross_start_date + "）"
						+ "&nbsp;&nbsp;<span class='label label-danger'>(发生异常，按星期规律生成开行计划数据错误，请校验数据)</span>";// "(发生异常)";
				break;
			default: 
				return self.crossName + "（" + self.cross_start_date + "）";//'';
				break;
		} 
	});
	 
	self.rowspan = ko.computed(function(){ 
		if( data.trainSort == 1){
			return data.crossName.split("-").length;
		}else{
			return 1;
		}
	}); 
	
	self.colspan = ko.computed(function(){ 
		 return self.runPlans().length + 2;
	}); 
	
	self.trainSort = data.trainSort;  
	
	self.yyyyMMdd = function(d){ 
		var year = d.getFullYear();   
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		month = ("" + month).length == 1 ? "0" + month : month;
		days = ("" + days).length == 1 ? "0" + days : days;
		return year + "-" + month + "-" + days;
	};
	//初始化开行计划的列表
	if(data.runPlans == null){
		var currentTime = new Date(data.startDate); 
		var endTime = new Date(data.endDate);
		//endTime.setDate(endTime.getDate() + 10); 
		var endTimeStr = self.yyyyMMdd(endTime);    
	 
		self.runPlans.remove(function(item) {
			return true;
		});   
		var curDay = self.yyyyMMdd(currentTime); 
		self.runPlans.push(new RunPlanRow({"day": curDay, "runFlag": data.runFlag, "createFlag": data.createFlag, planCrossId: data.planCrossId,crossName: data.crossName, baseChartId: data.baseChartId})); 
		while(curDay != endTimeStr){
			currentTime.setDate(currentTime.getDate() + 1); 
			curDay = self.yyyyMMdd(currentTime);
			self.runPlans.push(new RunPlanRow({"day": curDay, "runFlag": data.runFlag, "createFlag": data.createFlag, planCrossId: data.planCrossId,crossName: data.crossName, baseChartId: data.baseChartId}));
		} 
	}  
}

function RunPlanRow(data){
	var self = this; 
	self.color = ko.observable("gray");
	self.day = data.day;
	self.runFlag = ko.observable(""); 
	self.createFlag = ko.observable("");  
	self.planTrainId = ko.observable("");  
	self.crossName = ko.observable(""); 
	self.baseTrainId =  ko.observable("");  
	self.unitCrossId = ko.observable(0);
	self.selected = ko.observable(0);
	self.color = ko.computed(function(){
		if(self.createFlag() == 1){
			return "green";
		}else{
			return "gray";
		} 
	});
	
	self.runFlagShowValue = ko.computed(function(){ 
//		if(self.createFlag() == 1){
			switch (self.runFlag()) {
			case 9: 
				return "<span class='label label-danger'>停</span>";//"停";
				break;
			case 1: 
				return "<span class='label label-success'>开</span>";//"开";
				break;
			case 2: 
				return "<span class='label label-info'>备</span>";//"备";
				break;
			default: 
				return '';
				break;
			}
//		}else{
//			switch (self.runFlag()) {
//			case 9: 
//				return "停";
//				break;
//			case 1: 
//				return "开";
//				break;
//			case 2: 
//				return "备";
//				break;
//			default: 
//				return '';
//				break;
//			}
//		}
	});
} 

function GetDays(data1, data2)
{  
	var startTime = new Date(data1);
	var endTime = new Date(data2);   
	
	var date3=endTime.getTime()-startTime.getTime(); //时间差的毫秒数  
	//计算出相差天数
	var days = Math.floor(date3/(24*3600*1000));
	
	return days;
}
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
 
function closeSetTime(){
	$('#setTime').dialog('close');
	$.ajax({
		url : "/trainplan/operationplan/doNewPLanWork",
		cache : false,
		type : "POST",
		dataType : "json",
		contentType : "application/json",
		data : "",
		success : function() {
			
		},
		error : function() {
			
		},
		complete : function(){
			
		}
	});
	alertMessage("success","数据正在生成中，请稍后进行查询！");
	$("#generateInfo").show();
}