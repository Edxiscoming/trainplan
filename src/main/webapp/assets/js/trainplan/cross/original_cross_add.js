//$(function() { 
//	var cross = new OriginalCross();
//	ko.applyBindings(cross,$("#c_dlg"));  
////	
////	var data, json;
////	json = '[{"id":"年计划1","text":"年计划1","selected":true},{"id":"年计划2","text":"年计划2","selected":true},{"id":"年计划3","text":"年计划3","selected":true}]';
////	data = $.parseJSON(json);
////	$("#bureauTest").combobox();
//
//	cross.init();   
//});

var spareFlags = [{"value": "1", "text": "开行"},{"value": "2", "text": "备用"},{"value": "9", "text": "停运"}];
var highlineFlags = [{"value": "1", "text": "平日"},{"value": "2", "text": "周末"},{"value": "3", "text": "高峰"}];

function OriginalAddCross(){
	var self = this;
	
	self.entityModel = ko.observable(new EntityModel());
	
	self.charts = ko.observableArray();
	
	self.chart =  ko.observable();
	
	self.loadChats = function(){   
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
						for ( var i = 0; i < result.data.length; i++) {   
							self.charts.push({"chartId": result.data[i].schemeId, "name": result.data[i].schemeName});  
						} 
						self.chart = self.charts()[0];
					} 
				} else {
					showErrorDialog("获取方案列表失败");
				} 
			},
			error : function() {
				showErrorDialog("获取方案列表失败");
			},
			complete : function(){
				
			}
	    }); 
	}; 
	
	self.loadChats();
	
	self.addSaveCrossInfo = function(){
		
		var groupTotalNbr = self.entityModel().groupTotalNbr();
		var pairNbr = self.entityModel().pairNbr();
		var appointWeek = self.entityModel().appointWeek();
		var appointDay  = self.entityModel().appointDay();
		
		if(groupTotalNbr!=null && groupTotalNbr!=undefined && groupTotalNbr!=""){
			if(isNaN(groupTotalNbr)){
				showErrorDialog("组数必须为数字");
		    	 return;
			}
		}
		if(pairNbr!=null && pairNbr!=undefined && pairNbr!=""){  
			if(isNaN(pairNbr)){
				showErrorDialog("对数必须为数字");
		    	 return;
			}
		}
//		if(appointWeek!=null && appointWeek!=undefined && appointWeek!=""){  
//			if(!Validate.isOand1(appointWeek)){
//				showErrorDialog("指定星期格式不对,必须是7位0和1组成的数组");
//		    	 return;
//			}
//		}
		
		var dataObj = new Object();
		self.entityModel().chartId(self.chart.chartId);
		self.entityModel().chartName(self.chart.name);
		dataObj.result = ko.toJSON(self.entityModel());
		var result = ko.toJSON(dataObj);
		commonJsScreenLock();
		$.ajax({
			url : "../originalCross/addOriginalCross",
			cache : false,
			type : "PUT",
			dataType : "json",
			contentType : "application/json",
			data :result,				
			success : function(result) {     
				if (result != null && result.code == "0") { 
					$("#c_dlg").dialog("close");
					showSuccessDialog("保存交路信息成功"); 
				} else {
					showErrorDialog("保存交路信息失败");
				};
			},
			error : function() {
				showErrorDialog("保存交路信息失败");
			},
			complete : function(){
				commonJsScreenUnLock();
			}
		}); 
	}
}

function EntityModel(){
	
	var self = this;

//	self.originalCrossId = ko.observable();
	//高线开行规律
	self.highlineRule = ko.observable();
	//普线开行规律
	self.commonlineRule = ko.observable();
	//是否截断原交路
	self.cutOld = ko.observable();
	//组数
	self.groupTotalNbr = ko.observable();
	//供电
	self.elecSupply = ko.observable();
	//集便
	self.dejCollect = ko.observable();
	//空调
	self.airCondition = ko.observable();
	//车辆担当局
	self.tokenVehBureau = ko.observable();
	//对数
	self.pairNbr = ko.observable();
	//交替日期
	self.alterNateDate = ko.observable();
	//交易原车次
	self.alterNateTranNbr = ko.observable();
	//交路名
	self.crossName = ko.observable();
	//备用套跑交路名
	self.crossSpareName = ko.observable();
	
	//经由铁路线
	self.throughline = ko.observable();
	//指定星期
	self.appointWeek = ko.observable();
	//指定日期
	self.appointDay = ko.observable();
	//创建人
	self.createPeople = ko.observable(); 
	//创建人单位
	self.createPeopleOrg = ko.observable(); 
	//运行区段
	self.crossSection = ko.observable();
	//审核人
	self.checkPeople = ko.observable(); 
	//审核时间
	self.checkTime = ko.observable(); 
	//是否审核
	self.checkFlag = ko.observable();
	//指定周期
	self.appointPeriod = ko.observable();
	//审核人单位
	self.checkPeopleOrg = ko.observable();
	//动车组车型
	self.crhType = ko.observable();
	//备注
	self.note = ko.observable();
	
	//机车类型
	self.locoType = ko.observable();
	//创建时间
	self.createTime = ko.observable();
	//基本方案id
	self.chartId = ko.observable();
	//基本方案名
	self.chartName = ko.observable();
	//生成交路时间
	self.creatCrossTime = ko.observable();	
	//交路等级
	self.crossLevel = ko.observable();
	//运行距离	
	self.runRange = ko.observable();
	//编组辆数
	self.marshallingNums = ko.observable();
	//定员
	self.peopleNums = ko.observable();
	//编组内容
	self.marshallingContent = ko.observable();
	//相关局
	self.relevantBureau = ko.observable();
	//为了匹配前端js
	self.crossId = ko.observable();
	
	self.chartId = ko.observable();
	
	self.chartName = ko.observable();
	
	self.spareFlags = spareFlags;
	
	self.highlineFlags = highlineFlags;
	
	//备用及停运标记
	self.spareFlag = ko.observable(spareFlags[0]);
	
	//高线标记
	self.highlineFlag = ko.observable(highlineFlags[0]); 
	
}
