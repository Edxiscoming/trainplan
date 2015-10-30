$(function() { 
	heightAuto();
	var cross = new CrossModel();
	ko.applyBindings(cross); 
	gcross = cross;
	cross.init(); 
	var windowW = $(window).width();
	var widthB = windowW*0.3;
	var widthC = windowW*0.32-50;
	$("#widthBUS").attr("style","width:"+widthB+"px");
	$("#widthvehicle").attr("style","width:"+widthC+"px");

});

function heightAuto(){
    var WH = $(window).height();
	//alert("window:---"+WH);
	$("#body_left").css("height",WH-470+"px");
	$("#left_height").css("height",WH-381+"px");	
	
};


var gcross ;
var setting = {
		view: {
			dblClickExpand: false,
			showLine: true,
			selectedMulti: false
		},
		data: {
			simpleData: {
				enable:true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: 0,
			}
		},
		callback: {
			beforeClick: function(treeId, treeNode) {
//				var zTree = $.fn.zTree.getZTreeObj("tree");
//				if (treeNode.isParent) {
//					zTree.expandNode(treeNode);
//					return false;
//				} else {
//					//demoIframe.attr("src",treeNode.file + ".html");
//					return true;
//				}
			},
			onClick:zTreeOnClick
			
		}
	};

var setting2 = {
		view: {
			dblClickExpand: false,
			showLine: true,
			selectedMulti: false
		},
		data: {
			simpleData: {
				enable:true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: 0,
			}
		},
		callback: {
			beforeClick: function(treeId, treeNode) {
//				var zTree = $.fn.zTree.getZTreeObj("tree");
//				if (treeNode.isParent) {
//					zTree.expandNode(treeNode);
//					return false;
//				} else {
//					//demoIframe.attr("src",treeNode.file + ".html");
//					return true;
//				}
			},
			onClick:zTreeOnClick2
			
		}
	};
var treeType = "";
var key = "";
var keyType = "";
var id ="";
var pid = "";
function zTreeOnClick(event, treeId, treeNode) {
//	  alert("树1=="+treeNode.treeType +","+treeNode.key+","+treeNode.keyType);
	  treeType = treeNode.treeType;
	  key = treeNode.key;
	  keyType = treeNode.keyType;
	  id = treeNode.id;
	  pid = treeNode.pId;
	  gcross.loadCrosses();
	  
};


function zTreeOnClick2(event, treeId, treeNode) {
//	 alert("树2=="+treeNode.treeType +","+treeNode.key+","+treeNode.keyType);
	  treeType = treeNode.treeType;
	  key = treeNode.key;
	  keyType = treeNode.keyType;
	  id = treeNode.id;
	  pid = treeNode.pId;
	  gcross.loadCrosses();
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
	//被选中的当前一行列车
	self.currentTrain =  ko.observable();
	
	self.currentTrainInfoMessage = ko.observable("");
	//时刻表详点
	self.times = ko.observableArray();
	//时刻表简点
	self.simpleTimes = ko.observableArray();
	
	//planTrainId
	self.planTrainId = ko.observable();//ko的所有方法都是function
	self.runTypeCode = ko.observable();
	//车底信息
	self.vehicle = ko.observableArray();
	//车底信息表格
	self.vehicleList = ko.observableArray();
	//车底信息技术定员总数
	self.vehiclerides = ko.observableArray();
	self.treeTypes = ko.observableArray();
	self.treeTypes2 = ko.observableArray();
	//担当局
	self.searchModle = ko.observable(new searchModle()); 
	
	self.cartNbr = ko.observable(0);
	self.cartPeople = ko.observable(0);
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
	//选中当前列车
	self.setCurrentTrain = function(row){
		self.currentTrain(row);
		self.planTrainId(row.planTrainId);
		self.currentTrainInfoMessage("车次：" + row.trainNbr + "&nbsp;&nbsp;&nbsp;" + row.startStn + "——" + row.endStn);
        
		self.vehicleList.remove(function(item){
			return true;
		});
//		$.getJSON("/trainplan/assets/js/trainplan/highLine/vehicleResult.json", function(result){
//	     	   if(result.data != null){
//	     		  var vehicle = new VehicleRow(result.data);
//	     		  self.vehicle(vehicle);
//	     		  var vlist = result.data.vehicleList;
//	     		  self.cartPeople(0);
//	     		  if(vlist!=null && vlist!=undefined){
//	     			  for(var i=0;i<vlist.length;i++){
//	     				  var vehicleListRow = new VehicleListRow(vlist[i]);
//	     				  self.vehicleList.push(vehicleListRow);
//	     				  var vc = parseInt(vlist[i].vehicleCapacity);
//	     				  if(vc!=null && vc!=undefined){
//	     					  if(!isNaN(vc)){
//	     						 self.cartPeople(self.cartPeople() + vc);//定员总数
//	     					  }
//	     				  }
//	     				 
//	     			  }
//	     		  }
//	     		
//			  }else{
//				  showErrorDialog("获取车底信息失败");
//			  }   
//	     });
	 	
		self.loadVehicleInfo(row);
		
		
//		$.getJSON("/trainplan/assets/js/trainplan/highLine/chegnwuresult.json", function(result){
//	     	   if(result.data != null){
//	     		  var vRidesRow = new VehicleRidesRow(result.data);
//	     		  self.vehiclerides(vRidesRow);
//			  }else{
//				  showErrorDialog("获取车底与车乘信息失败");
//			  }   
//	     });
		
		self.getHighLineCrewInfo(row);
		
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
		}else{
//			$.getJSON("/trainplan/assets/js/trainplan/highLine/time.json", function(result){
//				if (result != null && result != "undefind" && result.code == "0") {  
//					row.loadTimes(result.data);  
//					$.each(row.times(), function(i, n){
//						self.times.push(n); 
//					}) ;
//					$.each(row.simpleTimes(), function(i, n){
//						self.simpleTimes.push(n); 
//					}) ; 
//				}   
//		     });
			$.ajax({
				url : "../jbtcx/queryPlanLineTrainTimesNewIframe?trainId="+row.planTrainId,
				cache : false,
				type : "GET",
			}); 
//			self.queryPlanLineTrainTimes(row);
		}
		if(!$('#train_time_dlg').is(":hidden")){ 
			$('#train_time_dlg').dialog("close"); 
			self.showCrossTrainTimeDlg();
		}
	};
	
	//获取车底信息
	self.loadVehicleInfo = function(row){
		commonJsScreenLock();
		$.ajax({
			url : basePath+"/highLine/getHighLineVehInfo",//获取车底信息url
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({   
				planTrainId : row.planTrainId,//来源于列车信息中选择行planTrainId
				creatType:  row.creatType,//来源于列车信息中选择行creatType  创建方式  JBT；LK"
			}),
			success : function(result) {  
				if (result != null && result != "undefind" && result.code == "0") {  
					var vehicle = new VehicleRow(result.data);
		     		  self.vehicle(vehicle);
		     		  var vlist = result.data.vehicleList;
		     		  self.cartPeople(0);
		     		  if(vlist!=null && vlist!=undefined){
		     			  for(var i=0;i<vlist.length;i++){
		     				  var vehicleListRow = new VehicleListRow(vlist[i]);
		     				  self.vehicleList.push(vehicleListRow);
		     				  var vc = parseInt(vlist[i].vehicleCapacity);
		     				  if(vc!=null && vc!=undefined){
		     					  if(!isNaN(vc)){
		     						 self.cartPeople(self.cartPeople() + vc);//定员总数
		     					  }
		     				  }
		     				 
		     			  }
		     		  }
				} else {
					 showErrorDialog("获取车底信息失败");
				};
			},
			error : function() {
				 showErrorDialog("获取车底信息失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
		}); 
	};
	
	
	
	
	self.getHighLineCrewInfo = function(row){
		var crewDate = $("#cross_lan_input_Date").val();
		var startBureaushortName = self.searchModle().startBureau().shortName;  //始发路局
	 	commonJsScreenLock();
		$.ajax({
			url : basePath+"/highLine/getHighLineCrewInfo",//获取车底和乘务信息url
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({   
				planTrainId : row.planTrainId,//来源于列表行选择记录
				planCrossId:  row.planCrossId,//来源于列表行选择记录
				crewDate :crewDate,//来源于界面查询条件--日期
				bureauName:startBureaushortName,//来源于界面查询条件--铁路局
				trainNbr:row.trainNbr,//来源于列表行选择记录
				creatType:row.creatType //来源于列表行选择记录
			}),
			success : function(result) {  
				if (result != null && result != "undefind" && result.code == "0") {  
					  var vRidesRow = new VehicleRidesRow(result.data);
		     		  self.vehiclerides(vRidesRow);

				} else {
					showErrorDialog("获取车底与车乘信息失败");
				};
			},
			error : function() {
				showErrorDialog("获取车底与车乘信息失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
		}); 
	};
	
	self.queryPlanLineTrainTimes = function(row){
	 	commonJsScreenLock();
		$.ajax({
    	url : basePath+"/jbtcx/queryPlanLineTrainTimes",//获取时刻表数据url
		cache : false,
		type : "POST",
		dataType : "json",
		contentType : "application/json",
		data :JSON.stringify({   
			trainId : self.planTrainId()
		}),
		success : function(result) {  
			if (result.data != null && result.data != "undefind" && result.code == "0") {  
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
			showErrorDialog("获取列车列表失败");
		},
		complete : function(){
			 	commonJsScreenUnLock();
		}
	 });
			
	};
	
	self.trainNbrChange = function(n,  event){
		self.searchModle().filterTrainNbr(event.target.value.toUpperCase());
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
	 
	//当前选中的交路对象
//	self.currentCross = ko.observable(new CrossRow(self.defaultCrossTemp)); 
 
	 
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
		$("#cross_lan_input_Date").datepicker();
		self.searchModle().runDate(self.currdate());//给日期付初始值
		$("#cross_map_dlg").dialog({
		    onClose:function(){
		    		self.searchModle().showCrossMap(0);
		       }
		   });
		
		$("#file_upload_dlg").dialog("close"); 
		$("#cross_train_time_dlg").dialog("close");
		$("#cross_map_dlg").dialog("close"); 
		$("#cross_train_dlg").dialog("close");
		$("#cross_train_time_dlg").dialog("close"); 
		$("#train_time_dlg").dialog("close");
		$("#cross_start_day").datepicker();
		  
//	   $.getJSON("/trainplan/assets/js/trainplan/highLine/18luju.json", function(result){
//	    	if (result.data != null && result.data != "undefind" && result.code == "0") { 
//				self.searchModle().loadBureau(result.data);//铁路局下拉框 
//			} else {
//				showErrorDialog("获取路局列表失败");
//			}   
//		});
		commonJsScreenLock();
	    $.ajax({
			url : basePath+"/plan/getFullStationInfo",//获取铁路局url
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result.data != null && result.data != "undefind" && result.code == "0") { 

					var bureaus = new Array();
//					var curUserBur = currentBureauShortName;
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
//							bureaus.unshift(fistObj,"");
//						}
					}
					self.searchModle().loadBureau(bureaus);
					//self.searchModle().loadBureau(result.data); 
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
	
	self.loadTrainTypeTree = function(trainTypeTree){
		if(trainTypeTree!= null ){
			self.treeTypes.remove(function(item){
				return true;
			});
  			for(var i =0; i<trainTypeTree.length;i++){
  				var json;
  				var obj = trainTypeTree[i];
  				if(id == obj.id){
  					json = { 
  		  	                "id": obj.id,
  		  	                "pId": obj.pId,
  		  	                "name": obj.name+"("+obj.count+")",
  		  	                "treeType": obj.treeType,
  		  	                "key": obj.key,
  		  	                "keyType": obj.keyType,
  		  	                "count": obj.count,
  		  	                "open": obj.open
  		  	            };
  				}else{
  					json = { 
  		  	                "id": obj.id,
  		  	                "pId": obj.pId,
  		  	                "name": obj.name+"("+obj.count+")",
  		  	                "treeType": obj.treeType,
  		  	                "key": obj.key,
  		  	                "keyType": obj.keyType,
  		  	                "count": obj.count,
  		  	                "open": obj.open
  		  	            };
  				}
  		
  				self.treeTypes.push(json);
  				
  			}
  		}
	};
	
	self.loadStnNameTree  = function(stnNameTree){
		
		if(stnNameTree!= null){
			self.treeTypes2.remove(function(item){
				return true;
			});
  			for(var i =0; i<stnNameTree.length;i++){
  				var obj2 = stnNameTree[i];
  				var  json2;
  				if(id == obj2.id){
  					json2 = { 
  		  	                "id": obj2.id,
  		  	                "pId": obj2.pId,
  		  	                "name": obj2.name+"("+obj2.count+")",
  		  	                "treeType": obj2.treeType,
  		  	                "key": obj2.key,
  		  	                "keyType": obj2.keyType,
  		  	                "count": obj2.count,
  		  	                "open": obj2.open
  		  	            };
  				}else{
  					json2 = { 
  		  	                "id": obj2.id,
  		  	                "pId": obj2.pId,
  		  	                "name": obj2.name+"("+obj2.count+")",
  		  	                "treeType": obj2.treeType,
  		  	                "key": obj2.key,
  		  	                "keyType": obj2.keyType,
  		  	                "count": obj2.count,
  		  	                "open": obj2.open
  		  	            };
  				}
	  				self.treeTypes2.push(json2);
  			}
  		} 
	};
	
	
	self.loadCrosseForPage = function(startIndex, endIndex) { 
		self.currentTrain(null);//清空当前选中对象
		$.fn.zTree.destroy("tree");
		$.fn.zTree.destroy("tree2");
		self.trains.remove(function(item){
			return true;
		});
		
		var trainNbr = self.searchModle().trainNbr(); //车次
		var bureauCode = self.searchModle().startBureau().code;  //铁路局
		var bureaushortName = self.searchModle().startBureau().shortName;  //铁路局
		var startDate = $("#cross_lan_input_Date").val();//日期
		var runTypeCode = self.searchModle().runTypeCode();//运行方式
		var treeTypeOne = treeType;
		var keyOne = key;
		var keyTypeOne = keyType;
//	    $.getJSON("/trainplan/assets/js/trainplan/highLine/trianResult.json", function(result){
//	  	   if(result.data!= null){
//		  		 var trainDatas = result.data.trainDatas;
//		  		 var trainTypeTree = result.data.trainTypeTreeNodes;//按列车类型
//		  		 var stnNameTree = result.data.stnNameTreeNodes;//按发到站及分界口
//		  		 for(var i = 0; i < trainDatas.length; i++){
//						var row = new TrainRow(trainDatas[i]); 
//						self.trains.push(row); 
//				} 
//		  		self.loadTrainTypeTree(trainTypeTree);
//		  		self.loadStnNameTree(stnNameTree);
//		  	
//		  		var obj1 = $.fn.zTree.init($("#tree"), setting, self.treeTypes());
//		  		var obj2 = $.fn.zTree.init($("#tree2"), setting2, self.treeTypes2());
////		  		var node = obj1.getNodeByTId(id);
//		  		var node = obj1.getNodeByParam("id", id, null);
////		  		var node2 = obj2.getNodeByTId(id);
//		  		var node2 = obj2.getNodeByParam("id", id, null);
//		  		if (node!=null && node !=undefined) {
//		  			obj1.selectNode(node);
//		  		}
//		  	
//		  		if (node2!=null && node2 !=undefined) {
//		  			obj2.selectNode(node2);
//		  		}
//
//
//		  }   
//	  });
	 	commonJsScreenLock();
		$.ajax({
				url : basePath+"/highLine/getHighLinePlanByBureau",//点查询按钮执行的url
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({ 
					bureauCode :bureauCode,//路局编码
					highlineFlag : "1",//高铁//highlingFlag == null ? null : highlingFlag.value,
					bureauName:bureaushortName,//路局名称
					runDate : (startDate != null ? startDate : self.currdate()),//日期
					trainNbr : trainNbr,//车次
					runTypeCode:runTypeCode,//运行方式
					treeType:treeTypeOne,
					key:keyOne,
					keyType:keyTypeOne	
				}),
				success : function(result) {    
					if (result.data!= null && result.data != "undefind" && result.code == "0") {
						 var trainDatas = result.data.trainDatas;
				  		 var trainTypeTree = result.data.trainTypeTreeNodes;//按列车类型
				  		 var stnNameTree = result.data.stnNameTreeNodes;//按发到站及分界口
				  		 for(var i = 0; i < trainDatas.length; i++){
								var row = new TrainRow(trainDatas[i]); 
								self.trains.push(row); 
						} 
				  		self.loadTrainTypeTree(trainTypeTree);
				  		self.loadStnNameTree(stnNameTree);
				  	
				  		var obj1 = $.fn.zTree.init($("#tree"), setting, self.treeTypes());
				  		var obj2 = $.fn.zTree.init($("#tree2"), setting2, self.treeTypes2());
//				  		var node = obj1.getNodeByTId(id);
				  		var node = obj1.getNodeByParam("id", id, null);
//				  		var node2 = obj2.getNodeByTId(id);
				  		var node2 = obj2.getNodeByParam("id", id, null);
				  		if (node!=null && node !=undefined) {
				  			obj1.selectNode(node);
				  		}
				  	
				  		if (node2!=null && node2 !=undefined) {
				  			obj2.selectNode(node2);
				  		}
				  		
						//是否出现右边17
						var tableH = $("#left_height table").height();
						var boxH = $("#left_height").height();
						if(tableH <= boxH){
							//alert("没");
							$(".td_17").removeClass("display");
						}else{
							//alert("有");
							$(".td_17").addClass("display");
						}
						//
					} else {
						showErrorDialog("获取列车信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			}); 

  
	};

	self.crossRows = new PageModle(200, self.loadCrosseForPage);
	
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
	
	self.showCrossMapDlg = function(n, e){ 
		var unitCrossId = self.currentCross().unitCrossId; 
		if($('#cross_map_dlg').is(":hidden")){
			$("#cross_map_dlg").find("iframe").attr("src", "../cross/provideUnitCrossChartData?unitCrossId=" + unitCrossId);
			$("#cross_map_dlg").dialog({title: "交路单元图     交路名:" + self.currentCross().crossName(),draggable: true, resizable:true});
		};
	};  
	
	self.showCrossTrainDlg = function(){
		$("#cross_train_dlg").dialog("open");
	};
	//#################################################################
	self.showCrossTrainTimeDlg = function(){ 
		if(self.currentTrain() == null){
			showWarningDialog("请先选择列车");
			return;
		}
		var trainId = self.planTrainId();  
		if($('#train_time_dlg').is(":hidden")){  
			$("#train_time_dlg").find("iframe").attr("src", "../jbtcx/queryPlanLineTrainTimesNewIframe?trainId=" + trainId);
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
	//#################################################################
	self.showCrossTrainTimeDlg_bak = function(row){
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
	
	

	
	self.isShowRunPlans = ko.observable(true);// 显示车底与乘务   默认不勾选
	self.showRunPlans = function(){
		var _height = $(window).height()- 120;
		var _height2 = $(window).height()- 236;
		var WH = $(window).height();
		
		if(!self.isShowRunPlans()) {//勾选
			$('#vehicle_dlg').show();
			/*$("#big_body_left").attr("style","height:450px;width: 105%;");
			$("#body_right").attr("style","height:505px; overflow-y: scroll; padding:15px;margin-bottom:15px;");
			$("#body_left").attr("style","height:384px;overflow-y: scroll;margin:20px 0;");	*/
			$("#body_left").css("height",WH-470+"px");
			$("#left_height").css("height",WH-381+"px");
			
		} else {//未勾选
			$('#vehicle_dlg').hide();
			/*$("#big_body_left").attr("style","height: 720px;width: 105%;margin-bottom: -10px;");
			$("#body_right").attr("style","height: 790px; overflow-y: scroll; padding:15px;");
			$("#body_left").attr("style","height: 660px;overflow-y: scroll; margin:20px 0px -10px 0px;");*/
			$("#body_left").css("height",WH-200+"px");
			$("#left_height").css("height",WH-110+"px");
		    
			
		}
	};
	
	
}

function searchModle(){
	self = this;  
	
	//==================高铁开行计划 查询条件====start=======
	self.runDate = ko.observable();//日期
	self.bureauName = ko.observable();//来源于路局下拉框
	self.bureauCode = ko.observable();//来源于路局下拉框
	self.trainNbr = ko.observable();//车次
	self.runTypeCode = ko.observable();//(SF：始发  JR：接入  JC：交出  ZD：终到)   来源于运行方式下拉框
	self.trainType = ko.observable();//可选列车类型    来源于‘按列车类型树节点查询’
	self.stnName = ko.observable();//可选车站名称       来源于‘发到站及分界口tab查询’
	
	self.startBureaus = ko.observableArray();//铁路局
	self.bureaus = ko.observableArray(); 
	self.startBureau = ko.observable();
	//==================高铁开行计划 查询条件=====end======
	
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

//路局对象
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


function TrainRow(data) {//列车一行的数据对象
	var self = this; 
	//=======================列车列表=====start=================
	self.laiyuan=data.laiyuan;
	self.spareFlagTxt = data.spareFlagTxt;
	self.planTrainId = data.planTrainId;//列车id
	self.runType = data.runType;//运行方式 (SFJC：始发交出  JRZD：接入终到  JRJC：接入交出  SFZD：始发终到)
	self.trainNbr = data.trainNbr;//车次
	self.startStn = data.startStn;//始发站
	self.startTime = ko.computed(function(){
		if(data.startTime!=null && data.startTime!=undefined){
			return moment(data.startTime).format("MMDD HH:mm");
		}else{
			return "";
		}
	});
	self.jrStn = data.jrStn;//接入站
	self.jrTime =   ko.computed(function(){
		if(data.jrTime!=null && data.jrTime!=undefined){
			return moment(data.jrTime).format("MMDD HH:mm");
		}else{
			return " ";
		}
	});
	self.jcStn = data.jcStn;//接出站
	self.jcTime = ko.computed(function(){
		if(data.jcTime!=null && data.jcTime!=undefined){
			return moment(data.jcTime).format("MMDD HH:mm");
		}else{
			return " ";
		}
	});
	self.endStn = data.endStn;//终到站
	self.endTime = moment(filterValue2(data.endTime)).format("MMDD HH:mm");//终到时间yyyy-MM-dd HH:mm:ss
	self.endTime = ko.computed(function(){
		if(data.endTime!=null && data.endTime!=undefined){
			return moment(data.endTime).format("MMDD HH:mm");
		}else{
			return " ";
		}
	});
	self.endDays = data.endDays;//终到运行天数
	self.passBureau = data.passBureau;//途经局（按顺序列出途经路局简称）
	
	//以下为非界面显示元素
	self.runDate = data.runDate;//开行日期
	self.startBureau = data.startBureau;//始发局简称
	self.endBureau = data.endBureau;//终到局简称
	self.planCrossId = data.planCrossId ;//交路ID（对应PLAN_CROSS中的交路ID）
	self.creatType = data.creatType;//创建方式
	//=======================列车列表========end==============
	
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
		 return data.alertNateTime == null ? "": data.alertNateTime.substring(5).replace(/-/g, "").substring(0, data.alertNateTime.substring(5).replace(/-/g, "").length - 3);
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

//车底行对象
function VehicleRow(data){
	var self = this;
	self.tokenVehBureauCode = ko.observable(filterValue2(data.tokenVehBureauCode));//担当局(局码)
	self.tokenVehBureauName = ko.observable(filterValue2(data.tokenVehBureauName));//担当局(简称)
	self.tokenVehDept = ko.observable(filterValue2(data.tokenVehDept));//担当车辆段/动车段
	self.tokenVehDepot = ko.observable(filterValue2(data.tokenVehDepot));//车辆/动车段 担当动车所(用于高铁)
	self.crossName = ko.observable(filterValue2(data.crossName));//交路名称
	self.crhType = ko.observable(filterValue2(data.crhType));//动车组车型(用于高铁)
	self.vehicle = ko.computed(function(){
		if(data.vehicle1!=null && data.vehicle1!=" " && 
				data.vehicle1!=undefined && data.vehicle2!=null && data.vehicle2!=" "
					&& data.vehicle2!=undefined){
			return filterValue2(data.vehicle1)+" + "+filterValue2(data.vehicle2);
		}else{
			return filterValue2(data.vehicle1)+"  "+filterValue2(data.vehicle2);
		}
		
	});
	self.vehicle1 = ko.observable(data.vehicle1);//车底1
	self.vehicle2 = ko.observable(data.vehicle2);//车底2
	self.crossSection = ko.observable(filterValue2(data.crossSection));//运行区段
	self.vehicleNum =  ko.computed(function(){ //获取车号
		var v1 = "";
		var v2 = "";
		if(data.vehicle1!=null && data.vehicle1!=undefined){
			v1 = filterValue2(data.vehicle1);
		}
		if(data.vehicle2!=null && data.vehicle2!=undefined){
			v2 = filterValue2(data.vehicle2);
		}
		return v1+" "+v2;
	});
	
}

//车底信息表格row
function VehicleListRow(data){
	var self = this;
	self.vehicleSort = ko.observable(data.vehicleSort);
	self.vehicleType = ko.observable(data.vehicleType);
	self.vehicleCapacity = ko.observable(data.vehicleCapacity);
}


//车底与乘务信息对象
function VehicleRidesRow(data){
	var self = this;
	self.czCrossName = ko.observable(filterValue2(data.czCrossName));//车长值乘交路
	self.tokenPsgBureau = ko.observable(filterValue2(data.tokenPsgBureau));//客运担当局局码
	self.jxsCrossName = ko.observable(data.jxsCrossName);//机械师值乘交路
	self.tokenPsgBureauName = ko.observable(filterValue2(data.tokenPsgBureauName));//客运担当局简称
	self.sjDeptName = ko.observable(filterValue2(data.sjDeptName));//司机所属单位
	self.sjName = ko.observable(filterValue2(data.sjName));//司机
	self.tokenPsgDept = ko.observable(filterValue2(data.tokenPsgDept));//客运段
	self.jxsDeptName = ko.observable(filterValue2(data.jxsDeptName));//机械师所属单位
	self.czName = ko.observable(filterValue2(data.czName));//车长
	self.sjCrossName = ko.observable(filterValue2(data.sjCrossName));//司机值乘交路
	self.jxsName = ko.observable(filterValue2(data.jxsName));//机械师
	self.czDeptName = ko.observable(filterValue2(data.czDeptName));//车长所属单位

}

function filterValue(value){
	return value == null || value == "null" ? "--" : value;
}

function filterValue2(value){
	return value == null || value == "null" ? " " : value;
}

function GetDateDiff(data)
{ 
	//始发站、终到站的停时字段空起
	 if(data.stationFlag=='SFZ' || data.stationFlag =='ZDZ' ||
			 data.dptTime == '-' 
			 || data.dptTime == null 
			 || data.arrTime == null){
		return "";
	}else if(data.arrTime == data.dptTime){//当到达时间和出发时间一样的时候  停时字段也空起
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
function TrainTimeRow(data) {//时刻表的行对象 
	var self = this; 
	self.data = data;
	self.index = ko.observable(data.childIndex + 1);//序号
	self.stnName =  ko.observable(filterValue(data.stnName));//站名
	self.bureauShortName = ko.observable(filterValue(data.bureauShortName));//路局
	self.arrTime = ko.computed(function(){
		if(data.stationFlag!=null && data.stationFlag!=undefined){
			if(data.stationFlag == 'SFZ'){
				return "--";
			}else{
				return moment(data.arrTime).format("MMDD HH:mm");
			}
		}
	});
	self.dptTime = ko.computed(function(){
		if(data.stationFlag!=null && data.stationFlag!=undefined){
			if(data.stationFlag == 'ZDZ'){
				return "--";
			}else{
				return moment(data.dptTime).format("MMDD HH:mm");
			}
		}
	});
	self.stepStr = ko.observable(GetDateDiff(data));//停留时间
	self.trackName = ko.observable(filterValue(data.trackName));//股道  
	self.runDays = ko.computed(function(){//出发天数
		if(data.stationFlag!=null && data.stationFlag!=undefined){
			if(data.stationFlag == 'ZDZ'){
				return "--";
			}else{
				return data.runDays;
			}
		}
	});
	self.arrRunDays = ko.computed(function(){//到达运行天数
		if(data.stationFlag!=null && data.stationFlag!=undefined){
			if(data.stationFlag == 'SFZ'){
				return "--";
			}else{
				return data.arrRunDays;
			}
		}
	});
	self.stationFlag = ko.observable(data.stationFlag);
	self.isChangeValue = ko.observable(0);
	
};

function openLogin() {
	$("#file_upload_dlg").dialog("open");
}

$(window.document).scroll(function () {
	    var scrolltop = $(document).scrollTop(); 
});
