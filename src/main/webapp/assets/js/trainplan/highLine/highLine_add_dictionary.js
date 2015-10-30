var canvasData = {};  
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
	//当前日期可调整的交路
	self.dicThroughLineCrossRows =  ko.observableArray(); 
	self.isSelectAll = ko.observable(false);
	self.currentDicRelaCrossPost = ko.observable(new DicThroughLine());//列表选中行记录
	
	
	
	//列车列表 用于左下角的显示使用
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

	//左边交路选中的记录列表
	self.selecteddicThroughLineCrossRows = ko.observableArray(); 
	//中间的列表选中的记录列表
	self.selectedActivedicThroughLineCrossRows = ko.observableArray(); 
	//车次组合后的时刻点单，用于显示
	self.activeTimes = ko.observableArray(); 
	self.activeTimes1 = ko.observableArray();
	
	//上移事件
	self.activeCrossUp = function(){
		var currentCorss = self.selectedActivedicThroughLineCrossRows()[0]; 
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
		var currentCorss = self.selectedActivedicThroughLineCrossRows()[0]; 
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
		 //$("#aaaaa input:checkbox").removeAttr("checked");
		 $("#aaaaa input:checkbox").prop("checked",true);
		
		self.activeTimes.remove(function(item){
			return true;
		}); 
		
		
		
					
		//$("[name='input_checkbox_stationType1']").attr("chenked",true);
		 $("#input_checkbox_stationType_jt").click(function(){
			   
				$("[name='input_checkbox_stationType1']").each(function(){
					if($(this).is(":checked")) {
						self.activeTimes.remove(function(item){
							return true;
						});
						
						self.activeTimes1.remove(function(item){
							return true;
						}); 
						
						
						$.each(self.selectedActivedicThroughLineCrossRows(), function(i, n){
							console.dir(n)
							$.each(n.trains(), function(a, t){
								
								$.each(t.times(), function(z, time){		
									if(z==0 ){
										time.stnSort=a+1
										self.activeTimes.push(time);
									}
									if(z==t.times().length-1){
										time.seq=a+1
										time.stnSort=2
										self.activeTimes.push(time);
									}
								//	self.activeTimes.push(time);
									
								});
							});
							var array = new Array()
							var hash ={}
							var map ={}
							var listarray=new Array()
							var mv=1
							self.activeTimes1.remove(function(item){
								return true;
							}); 
							
							$.each(self.activeTimes(), function(a, t){
								array[a]=t.trainNbr;
							});
							for(var i=0;i<array.length;i++){
								var item=array[i]
								var key=typeof(item)+item;
								if(hash[key]!==1){
									listarray.push(item)
									hash[key]=1
									map[item]=mv;
									mv++;
								}
							}
							$.each(self.activeTimes(), function(a, t){
								//array[a]=t.trainNbr;
								if(t.trainNbr in map){
									t.seq=map[t.trainNbr]
									
									self.activeTimes1.push(t)
								}
							});
						});
					} else {
						
						self.activeTimes.remove(function(item){
							  return true;
						   });
						self.activeTimes1.remove(function(item){
						     return true;
					        }); 
					
						$.each(self.selectedActivedicThroughLineCrossRows(), function(i, n){
							
							$.each(n.trains(), function(a, t){
								
								$.each(t.times(), function(z, time){		
//									if(z==0 ){
//										time.stnSort=1
//										self.activeTimes.push(time);
//									}
									/*if( z==t.times().length-1){
										time.stnSort=t.times().length;
										self.activeTimes.push(time);
									}*/
								    	time.seq=a+1
									 	time.stnSort=z+1;
										self.activeTimes.push(time);
									
									//self.activeTimes.push(time);
									
								});
							});
							var array = new Array()
							var hash ={}
							var map ={}
							var listarray=new Array()
							var mv=1
							self.activeTimes1.remove(function(item){
								return true;
							}); 
							
							$.each(self.activeTimes(), function(a, t){
								array[a]=t.trainNbr;
							});
							for(var i=0;i<array.length;i++){
								var item=array[i]
								var key=typeof(item)+item;
								if(hash[key]!==1){
									listarray.push(item)
									hash[key]=1
									map[item]=mv;
									mv++;
								}
							}
							$.each(self.activeTimes(), function(a, t){
								//array[a]=t.trainNbr;
								if(t.trainNbr in map){
									t.seq=map[t.trainNbr]
									
									self.activeTimes1.push(t)
								}
							});
						});
					}
			    });
				

			
		    });
		
		$.each(self.selectedActivedicThroughLineCrossRows(), function(i, n){
			$.each(n.trains(), function(a, t){
				//console.dir(a+"=========zzzzzzzzzzzzzz=========================")
				$.each(t.times(), function(z, time){		
					//console.dir(z+"=========aaaaaaaaaaaa=======================")
					if(z==0 ){
						
						time.stnSort=1
						self.activeTimes.push(time);
					}
					if(z==t.times().length-1){
						
						time.stnSort=2
						self.activeTimes.push(time);
					}
					
				//	self.activeTimes.push(time);
					
				});
			});
			
			var array = new Array()
			var hash ={}
			var map ={}
			var listarray=new Array()
			var mv=1
			self.activeTimes1.remove(function(item){
				return true;
			}); 
			
			$.each(self.activeTimes(), function(a, t){
				array[a]=t.trainNbr;
			});
			for(var i=0;i<array.length;i++){
				var item=array[i]
				var key=typeof(item)+item;
				if(hash[key]!==1){
					listarray.push(item)
					hash[key]=1
					map[item]=mv;
					mv++;
				}
			}
			$.each(self.activeTimes(), function(a, t){
				//array[a]=t.trainNbr;
				if(t.trainNbr in map){
					t.seq=map[t.trainNbr]
					
					self.activeTimes1.push(t)
				}
			});
			console.dir(self.activeTimes1())
			
		});
	};
	
	
	
	
	//全选按钮
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.dicThroughLineCrossRows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0); 
			}else{ 
				crossRow.selected(1);   
			}  
		});  
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
		$.each(self.dicThroughLineCrossRows(), function(i, crossRow){ 
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
			$.each(self.dicThroughLineCrossRows(), function(i, crossRow){  
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
	 
	self.loadCrosses = function(){
		self.loadCrosseForPage();
	};
	
	//当前选中的交路对象
	self.currentCross = ko.observable(new DicThroughLine(self.defualtCross)); 
	 
	
	self.init = function(){
		$("#line_add_dictionary_dialog").dialog("close");
		$("#add_disRelaCross_dialog").dialog("close");
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
							bureaus.unshift("");
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
			url : basePath+"/highLine/getDepots/CROSS_CHECK",
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
			url : basePath+"/highLine/getAccs/CROSS_CHECK",
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
		
		self.loadCrosses();	
		
	};
	
	//线台字典
	self.lineDictionary = function(){
		$("#lineDictionary_dialog").find("iframe").attr("src", basePath+"/highLine/lineDictionary");
		$('#lineDictionary_dialog').dialog({title: "线台字典", autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#lineDictionary_dialog").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#lineDictionary_dialog').height();
				var WW = $('#lineDictionary_dialog').width();
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
	
	
  self.addDisRelaCrossPost = function(){
	  $("#add_disRelaCross_dialog").find("iframe").attr("src", basePath+"/highLine/line_add_dictionary");
		$('#add_disRelaCross_dialog').dialog({title: " ", autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#add_disRelaCross_dialog").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#add_disRelaCross_dialog').height();
				var WW = $('#add_disRelaCross_dialog').width();
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
	
	
	
	//删除选中的交路记录
	self.deleteHighLineCrosses = function(){
		if (self.selecteddicThroughLineCrossRows().length == 0) {
			showWarningDialog("请选需要删除的交路信息");
			return;
		}
		
		
		
		
		
		var _deleteIds = "";
		for(var i=0;i<self.selecteddicThroughLineCrossRows().length;i++) {
			_deleteIds = _deleteIds+"'"+self.selecteddicThroughLineCrossRows()[i].highLineCrossId()+"',";
		}
		_deleteIds = _deleteIds.substring(0, _deleteIds.lastIndexOf(","));
		
		
	
		showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
			//commonJsScreenLock();
	        if (r) { 
	        	
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
	    					for(var i=0;i<self.selecteddicThroughLineCrossRows().length;i++) {
	    						self.dicThroughLineCrossRows.remove(self.selecteddicThroughLineCrossRows()[i]);
	    					}
	    					
	    					showSuccessDialog("成功删除"+self.selecteddicThroughLineCrossRows().length+"条交路计划");
	    				} else {
	    					showErrorDialog("删除交路计划失败");
	    				}
	    			},
	    			error : function() {
	    				showErrorDialog("删除交路计划失败");
	    			},
	    			complete : function(){
//	    				showSuccessDialog("成功删除");
	    			}
	    	    });
	        };
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
		
		showConfirmDiv("提示", "确定要提交" + $("#runplan_input_startDate").val() + "日交路计划吗?", function (r) { 
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
	    					showSuccessDialog("成功提交"+$("#runplan_input_startDate").val()+"日交路计划");
	    				} else {
	    					showErrorDialog("提交"+$("#runplan_input_startDate").val()+"日交路计划失败");
	    				}
	    			},
	    			error : function() {
	    				showErrorDialog("提交"+$("#runplan_input_startDate").val()+"日交路计划失败");
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
	
	//铁路线弹出窗口
	self.showHighline = function(){
		$("#line_add_dictionary_dialog").find("iframe").attr("src", basePath+"/highLine/line_add_dictionary");
		$('#line_add_dictionary_dialog').dialog({title: " ", autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#line_add_dictionary_dialog").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#line_add_dictionary_dialog').height();
				var WW = $('#line_add_dictionary_dialog').width();
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
	 * 生成命令按钮点击事件
	 * 
	 * 实则为查看交路生成命令字符串
	 */
	self.previewCmdInfo = function() {
		if($("#runplan_input_startDate").val()==null || $("#runplan_input_startDate").val()=="") {
			showWarningDialog("请选择日期");
			return;
		}
		
		showConfirmDiv("提示", "确定要生成" + $("#runplan_input_startDate").val() + "日命令吗?", function (r) { 
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
					showSuccessDialog("生成命令成功 ，流水号： " + result.message);
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
	
	
	
	self.addDicRelaCrossPost = function(){
		$("#add_disRelaCross_dialog").find("iframe").attr("src", basePath+"/highLine/dicThroughLine");
		$('#add_disRelaCross_dialog').dialog({title: " ", autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#add_disRelaCross_dialog").find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#add_disRelaCross_dialog').height();
				var WW = $('#add_disRelaCross_dialog').width();
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
	 * 列表左下角显示记录总数
	 * 
	 * 临时使用
	 */
	self.currentTotalCount = ko.computed(function(){
		if(self.dicThroughLineCrossRows().length >0) {
			return "共"+self.dicThroughLineCrossRows().length+"条数据";
		} else {
			return "当前没有可显示的数据";
		}
	});
	
	self.loadCrosseForPage = function(startIndex, endIndex) {
	 
		commonJsScreenLock();
		self.dicThroughLineCrossRows.remove(function(item) {
			return true;
		});
		$.ajax({
				url : basePath+"/highLine/getDicThroughLine",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
						var rows = [];
						if(result.data != null){
							$.each(result.data,function(n, dicThroughLine){
								rows.push(new DicThroughLine(dicThroughLine));
								self.dicThroughLineCrossRows.push(new DicThroughLine(dicThroughLine));
							});
							self.crossRows.loadPageRows(result.data.totalRecord, rows);
						
						}   
						 
					} else {
						showErrorDialog("获取铁路线字典列表失败");
					};
				},
				error : function() {
					showErrorDialog("获取铁路线字典列表失败");
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
	 
	
	
	self.isShowCrossDetailInfo = ko.observable(true);// 显示交路详情复选框   默认勾选

	
	self.showDialog = function(id, title){
		$('#' + id).dialog({ title:  title, autoOpen: true, height:600,width: 800, modal: false, draggable: true, resizable:true });
	};
	
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	
	self.showCrossTrainTimeDlg = function(){
		
		$("#run_plan_train_times").dialog({inline: false, top:150});
	};
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
	};
	
	//删除铁路线
	self.deleteDicThroughLine = function(){ 
		var dicThrougLineIds = [];
		for(var i=0;i<self.dicThroughLineCrossRows().length;i++){
			var obj = self.dicThroughLineCrossRows()[i];
			if(obj.isSelect() == 1){
				dicThrougLineIds.push(obj.dicThrougLineId());
			}
		}
	
		if(dicThrougLineIds.length < 1){
			showErrorDialog("没有可删除的记录");
			return;
		}
		
		showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
	        if (r) { 
				$.ajax({
					url : basePath+"/highLine/deleteDicThroughLine",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						dicThrougLineIds : dicThrougLineIds
					}),
					success : function(result) {     
						if(result.code == 0){
							self.loadCrosses();	
							window.parent.highLine.searchThroughLineName();
							showSuccessDialog("删除线台字典成功"); 
						}else{
							showErrorDialog("删除线台字典失败");
						};
					}
				}); 
	        };
		});
	};

	//设置
	self.setDicRelaCrossPost = function(){
		var trainNbr = self.searchModle().trainNbr();
		var searchThroughLine = self.searchModle().searchThroughLine();
		var searchTokenVehDepot = self.searchModle().searchTokenVehDepot();
		var dicIds = [];
		for(var i=0;i<self.dicThroughLineCrossRows().length;i++){
			var obj = self.dicThroughLineCrossRows()[i];
			if(obj.isSelect() == 1){
				dicIds.push(obj.dicId());
			}
		}
	
		if(dicIds.length < 1){
			showErrorDialog("没有可设置的记录");
			return;
		}
				$.ajax({
					url : basePath+"/highLine/setDicRelaCrossPost",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({  
						dicIds : dicIds,
						trainNbr : trainNbr,//车次号
						throughLine:searchThroughLine,//铁路线
						tokenVehDepot: searchTokenVehDepot,//动车所
						postName: self.searchModle().searchAcc()//动车台
					}),
					success : function(result) {     
						if(result.code == 0){
//							for(var i=0; i < self.dicThroughLineCrossRows().length;i++){
//								var oldObj = self.dicThroughLineCrossRows()[i];
//								var oldObj2 = self.dicThroughLineCrossRows()[i+1];
//								var dicId1 = oldObj.dicId();
//								var dicId2 = oldObj2.dicId();
//								for(var j =0 ;j < dicIds.length; j++){
//									var dicId3 = dicIds[j];
//									if(oldObj!=null && oldObj!=undefined && oldObj.dicId()!=null && oldObj.dicId()!=undefined){
//										if(dicId1 == dicId3 ){
//											self.dicThroughLineCrossRows.remove(oldObj); //从集合中移除删除的对象
//										}
//									}
//								}
//								
//							}
							
							self.loadCrosses();	
							showSuccessDialog("设置线台字典成功"); 
						}else{
							showErrorDialog("设置线台字典失败");
						};
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


	/**
	 * 列表全选复选框change事件
	 */
	self.checkBoxSelectAllChange = function() {
		if (!self.isSelectAll()) {//全选     将dicThroughLineCrossRows中isSelect属性设置为1
			$.each(self.dicThroughLineCrossRows(), function(i, row){
				row.isSelect(1);
			});
		} else {//全不选    将dicThroughLineCrossRows中isSelect属性设置为0
			$.each(self.dicThroughLineCrossRows(), function(i, row){
				row.isSelect(0);
			});
		}
	};
	
	self.setCurrentRec1 = function(row) {
		if(row.isSelect()==false ||row.isSelect()==0){
			row.isSelect(1);//复选框勾中
			self.currentDicRelaCrossPost(row);
			
//			self.dicThroughLineCrossRows.remove(function(item) {
//				return true;
//			});
			
		}
		
	};
	
}

function searchModle(){
	self = this;  
	 
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
			self.accs.push({"code":accs[i].code, "name":accs[i].name});  
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
				self.throughLines.push({"throughLineLineId": options[i].throughLineLineId, "throughLineName": options[i].throughLineName});  
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
	self.shortName = data.ljjc;   
	self.code = data.ljpym;   
	//方案ID 
} 

 

function DicThroughLine(data) {
	var self = this; 
	
	if(data == null){
		return ;
	}
	//主键id
	self.dicThrougLineId = ko.observable(data.throughLineLineId);
	self.throughLineName = ko.observable(data.throughLineName);  
	self.bureau = ko.observable(data.bureau);  
	self.note = ko.observable(data.note);  
	self.isSelect = ko.observable(0);
	
	
	
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
	self.crossDate = ko.observable(data.crossDate);//交路计划日期
	self.crossStartDate = ko.observable(data.crossStartDate);//交路首车日期
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
	self.throughLine = ko.observable(data.throughLine);
	self.startBureau = ko.observable(data.startBureau); 
	//担当局 
	self.tokenVehBureau = ko.observable(function(){ 
		var tokenVehBureau = "";
		var result="";
		 if(data.tokenVehBureau != null && data.tokenVehBureau != "null"){
			 tokenVehBureau = data.tokenVehBureau;
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(tokenVehBureau == gloabBureaus[i].code){
						 result = gloabBureaus[i].shortName;
						 break;
					 };
				 };
		 }
		 return result; 
}); 
	//担当局 
	self.tokenVehBureauShowValue = ko.computed(function(){ 
		var bureau = "";
		var result="";
		 if(data.bureau != null && data.bureau != "null"){
			 bureau = data.bureau;
				 for(var i = 0; i < gloabBureaus.length; i++){
					 if(bureau == gloabBureaus[i].code){
						 result = gloabBureaus[i].shortName;
						 break;
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



 
