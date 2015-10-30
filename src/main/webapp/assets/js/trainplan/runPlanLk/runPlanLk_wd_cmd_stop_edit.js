var cmdTrainId = self.frameElement.name;
var text = self.frameElement.id;

$(function() {
	//console.log("getUrlParam(name): " + getUrlParam("cmdTrainId"));
	console.log("frame" + self.frameElement.name);

	   $("#liOption").multiselect2side({
		    selectedPosition: 'right',
		    moveOptions: false,
			labelsx: '待选区',
			labeldx: '已选区'
	   });




 
	   
	//getCmdTrainData(self.frameElement.name, cmdTrainData);
	
	new WdCmdAddPage().initPage();










});


var wdCmdAddPageModel = null;//界面绑定元素对象
var wdCmdRuleArray = [{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"},{"code": "3", "text": "择日"}];
var importArray = [{"code": "1", "text": "是"},{"code": "0", "text": "否"}];

var WdCmdAddPage = function () {
	
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		$("#input_wd_cmdTime").datepicker();
		$("#input_wd_startDate").datepicker();
		$("#input_wd_endDate").datepicker();
		




		
		//$("#input_wd_startDate").val(moment().add("day", 1).format('YYYY-MM-DD'));
		
		
		
		//页面元素绑定
		wdCmdAddPageModel = new PageModel();
		ko.applyBindings(wdCmdAddPageModel);
		wdCmdAddPageModel.getFullNodeList();
	};
	
	function PageModel() {
		var _self = this;
		_self.wdCmdTrain = ko.observable();//文电新增、编辑界面绑定对象
		_self.wdCmdTrain(new WdCmdTrainModel(createEmptyCmdTrainRow()));//创建一个空对象
		
		 _self.stnNameTemps = ko.observableArray();
		
	       _self.getFullNodeList = function(){
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
	    								_self.stnNameTemps.push(obj.pinyinInitials+";"+obj.name+'['+obj.bureauShortName+']');
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
	        
	        
			self.stnNameTempOnfocus = function(n, event){
				$(event.target).autocomplete(wdCmdAddPageModel.stnNameTemps(),{
					max: 50,    	//列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 500,   	//提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false,    	//是否自动填充
					formatResult: function(row) {
						return row;
					}
		       }).result(function(event, stnNameTemp, formatted) {
		    	   if(formatted!=null && formatted!=undefined){
		    		   var str = new String(formatted);
		    		   var name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));
		    		   var bureauShortName = str.substring(str.indexOf("[")+1, str.indexOf("]"));
		    		  
		    		   console.dir(WdCmdTrainModel())
		    		   _self.startStn(name);
/*//		    		   alert('name=='+name);
//		    		   alert('bureauShortName=='+bureauShortName);
		    		   self.stnNameTemp(name);
//		    		   self.nodeId(id);
		    		  
		    		   self.currentBureauJc(bureauShortName);*/
		    	   }
		    	   
		    	   
		       });
			};
		
		
		/**
		 * 文电新增编辑界面 保存按钮
		 */
		_self.btnEventSaveWdCmdTrain = function() {
			//设置开行规律
			if (_self.wdCmdTrain().wdCmdRuleOption()!=null && _self.wdCmdTrain().wdCmdRuleOption()!="undefined") {
				_self.wdCmdTrain().rule(_self.wdCmdTrain().wdCmdRuleOption().code);
			}
			
			if("3" == _self.wdCmdTrain().wdCmdRuleOption().code && 	("" == _self.wdCmdTrain().selectedDate() || 
					null == _self.wdCmdTrain().selectedDate())) {
				showWarningDialog("请先确定择日日期");
				return;
			}
			

			console.log(_self.wdCmdTrain().wdCmdRuleOption().code);
			//将时间值字符串都设置进去
			_self.wdCmdTrain().startDate($("#input_wd_startDate").val());
			_self.wdCmdTrain().endDate($("#input_wd_endDate").val());
			_self.wdCmdTrain().cmdTime($("#input_wd_cmdTime").val());
			_self.wdCmdTrain().cmdType(text);
			 _self.wdCmdTrain().startStn($("#ddlOrgCity").val());


               _self.wdCmdTrain().importantFlag(0);

			console.dir(ko.toJSON(_self.wdCmdTrain()));
			
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/editWdCmdTrain",
				cache : false,
				type : "PUT",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainMap : ko.toJSON(_self.wdCmdTrain())
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("保存成功");

						//这是重新加载页面，暂时不做在这里
						//loadDataForBjddPage();
						window.parent.runPlanLkCmdPageModel.queryListBjdd();
					} else {
						showErrorDialog("保存失败");
					};
				},
				error : function() {
					showErrorDialog("保存失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
	
	
	
	/**
	 * 文电新增界面 重置按钮
	 */
	_self.btnEventResetWdCmdTrain = function() {
		console.log("reset!!");
		location.reload();











	
	};
	
	/**
	 * 部分清空列车数据
	 */
	_self.btnEventCleanWdInfo = function() {

		_self.wdCmdTrain().startStn("");

		_self.wdCmdTrain().cmdItem("");
		_self.wdCmdTrain().trainNbr("");
		_self.wdCmdTrain().nonSelectItems([]);
		_self.wdCmdTrain().items([]);
		_self.wdCmdTrain().selectedDate("");
		_self.wdCmdTrain().wdCmdRuleOption(wdCmdRuleArray[0]);

		_self.wdCmdTrain().importOption(importArray[0]);

		_self.wdCmdTrain().booleanValue(false);
				
	
		$("#input_wd_startDate").val(moment().format('YYYY-MM-DD'));
		$("#input_wd_endDate").val(moment().format('YYYY-MM-DD'));
	
	};
	
	/**
	 * 文电新增界面 选择日期提交按钮
	 */
	_self.btnSubmitSelectDate = function() {
		var obj = $("#liOptionms2side__dx").find("option");
		var dateArray = [];
		
		if(obj.size() > 0) {
			for(var i=0; i<obj.size(); i++) {
				dateArray[i] = obj.eq(i).text();
			}
		}
		var data = {"dateList" : dateArray};
		commonJsScreenLock();
		console.log(JSON.stringify(data));
		$.ajax({
			url : basePath+"/runPlanLk/getSelectDateStr",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify(data),
			success : function(result) {			
				if (result != null) {
//					showSuccessDialog("生成择日信息成功");
//					console.log(result.message);
					_self.wdCmdTrain().selectedDate(result.message);
					//这是重新加载页面，暂时不做在这里
					//loadDataForBjddPage();
				} else {
					showErrorDialog("生成择日信息失败");
				};
			},
			error : function() {
				showErrorDialog("生成择日信息");
			},
			complete : function(){
				commonJsScreenUnLock(1);
			}
		});
		


		







};



	/**
	 * 文电新增界面 加载日期列表按钮
	 */
	_self.btnLoadSelectDate = function() {
		var selectDate = [];
	
		var beginDate = new Date($("#input_wd_startDate").val());
		var endDate = new Date($("#input_wd_endDate").val());
		var days = parseInt(getDateCha(beginDate, endDate));
		
		var m = moment(new Date($("#input_wd_startDate").val()));
		
		for(var i=0; i<days+1; i++) {
			var date = null;
			if(0==i) {
				date = m.add("day", 0).format('YYYY-MM-DD');
			}
			else {
				date = m.add("day", 1).format('YYYY-MM-DD');
			}		
			selectDate.push(date);
		}
		
		_self.wdCmdTrain().nonSelectItems(selectDate);
		//var m = _self.wdCmdTrain().nonSelectItems();
		//console.log(m);
		
	};
	
	//开行规律变化时的事件
	_self.selectorChange = function() {
		var obj = _self.wdCmdTrain().wdCmdRuleOption();
		//console.log(obj.code);
		if(obj.code != "3") {
			_self.wdCmdTrain().nonSelectItems([]);
			_self.wdCmdTrain().items([]);
			_self.wdCmdTrain().selectedDate("");
		}
		
	};
	


	_self.importChange = function() {
		_self.wdCmdTrain().importOption();
		
		
	};






	//令止变化时的事件
	_self.checkboxChange = function() {
		var obj = _self.wdCmdTrain().booleanValue();
		//console.log(obj);
		if(!obj) {
			_self.wdCmdTrain().nonSelectItems([]);
			_self.wdCmdTrain().items([]);
			_self.wdCmdTrain().selectedDate("");
		}
		
	};
	
	//日期选择变化时
	_self.dateChange = function() {

			console.log("come in!");
			_self.wdCmdTrain().nonSelectItems([]);
			_self.wdCmdTrain().items([]);
			_self.wdCmdTrain().selectedDate("");
		
		
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
			_dataObj.importantFlag="";

		
		//_dataObj.items = [];
		
		//获取文电字段值
		$.ajax({
			url : basePath+"/runPlanLk/editWdCmdTrain/" + cmdTrainId,
			async : false,
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json",
			success : function(result) {			
				if (result != null) {
					console.log(result.data);
					
					_dataObj.cmdTrainId = cmdTrainId;
					_dataObj.cmdNbrSuperior = result.data.cmdNbrSuperior;
					_dataObj.trainNbr = result.data.trainNbr;
					_dataObj.startStn = result.data.startStn;
					_dataObj.endStn = result.data.endStn;
					_dataObj.cmdBureau = result.data.cmdBureau;
					_dataObj.rule = result.data.rule;
					_dataObj.cmdNbrBureau = result.data.cmdNbrBureau;
					_dataObj.cmdItem = result.data.cmdItem;
					_dataObj.selectedDate = result.data.selectedDate;
					
_dataObj.importantFlag=result.data.importantFlag;
					$("#input_wd_cmdTime").val(moment(result.data.cmdTime).format('YYYY-MM-DD'));
					$("#input_wd_startDate").val(moment(result.data.startDate).format('YYYY-MM-DD'));
					$("#input_wd_endDate").val(moment(result.data.endDate).format('YYYY-MM-DD'));
					
					
					//这是重新加载页面，暂时不做在这里
					//loadDataForBjddPage();
				} else {
					showErrorDialog("加载文电信息失败");
				};
			},
			error : function() {
				showErrorDialog("加载文电信息失败");
			},
			complete : function(){
				commonJsScreenUnLock(1);
			}
		});
		
		
		
		
		return _dataObj;
	};

/**
 * 定义文电命令对象，用于文电新增、编辑page
 * 
 * @param data
 */
function WdCmdTrainModel(data) {
	var _self = this;
	_self.cmdTrainId = ko.observable(data.cmdTrainId);
	_self.baseTrainId = ko.observable(data.baseTrainId);
	_self.cmdBureau = ko.observable(data.cmdBureau);
	_self.cmdType = ko.observable(data.cmdType);
	_self.cmdTxtMlId = ko.observable(data.cmdTxtMlId);
	_self.cmdTxtMlItemId = ko.observable(data.cmdTxtMlItemId);
	_self.importantFlag = ko.observable(data.importantFlag);
	//路局文电号
//	_self.cmdNbrBureau = ko.observable(data.cmdNbrBureau).extend({
//        required: {message: "请输入路局文电号" }
//    });
//	_self.cmdItem = ko.observable(data.cmdItem).extend({
//        required: {message: "请输入项号" }
//    });
//	_self.cmdNbrSuperior = ko.observable(data.cmdNbrSuperior).extend({
//        required: {message: "请输入总公司文电号" }
//    });
//	_self.trainNbr = ko.observable(data.trainNbr).extend({
//        required: {message: "请输入车次" }
//    });
//	_self.startStn = ko.observable(data.startStn).extend({
//        required: {message: "请输入始发站" }
//    });
//	_self.endStn = ko.observable(data.endStn).extend({
//        required: {message: "请输入终到站" }
//    });
	_self.cmdNbrBureau = ko.observable(data.cmdNbrBureau);
	_self.cmdItem = ko.observable(data.cmdItem);
	_self.cmdNbrSuperior = ko.observable(data.cmdNbrSuperior);
	_self.trainNbr = ko.observable(data.trainNbr);
	_self.startStn = ko.observable(data.startStn);
	_self.endStn = ko.observable(data.endStn);
	_self.rule = ko.observable(data.rule);
	
	
	_self.wdCmdRuleArray = ko.observableArray(wdCmdRuleArray);	//文电加开开行规律下拉框
	
    _self.importArray = ko.observableArray(importArray);

	var i = 0;
	if("隔日" == data.rule) {
		i = 1;
	}
	else if ("择日" == data.rule){
		i = 2;
	}

	_self.wdCmdRuleOption = ko.observable(wdCmdRuleArray[i]);	
var j=0;
	
	if("1" == data.importantFlag) {
		j = 0;
	}
	else  {
		j = 1;
	}
	_self.importOption = ko.observable(importArray[j]);
				//文电加开开行规律下拉框选中项

//	_self.rule = ko.observable(data.rule).extend({
//        required: {message: "请选择开行规律" }
//    });
	
//	_self.startDate = ko.observable(data.startDate).extend({
//        required: {params: true, message: "请输入起始日期" },
//        pattern: {
//            params: "[2-9]\\d{4}\-[0-9]{2}\-[0-9]{2}$",
//            message: "格式不正确，如：2014-12-31"//yyyy-mm-dd
//        }
//        
//    });
//	_self.endDate = ko.observable(data.endDate).extend({
//        required: {message: "请输入终止日期" },
//        pattern: {
//            params: "[2-9]\\d{4}\-[0-9]{2}\-[0-9]{2}$",
//            message: "格式不正确，如：2014-12-31"//yyyy-mm-dd
//        }
//    });
	
	_self.startDate = ko.observable(data.startDate);
	_self.endDate = ko.observable(data.endDate);

	_self.selectedDate = ko.observable(data.selectedDate);
	_self.passBureau = ko.observable(data.passBureau);
	_self.updateTime = ko.observable(data.updateTime);
	_self.cmdTime = ko.observable(data.cmdTime);
	_self.isExsitStn = ko.observable(data.isExsitStn);
	_self.userBureau = ko.observable(data.userBureau);
	_self.selectState = ko.observable(data.selectState);
	_self.createState = ko.observable(data.createState);
	_self.items = ko.observableArray([]);
	_self.nonSelectItems = ko.observableArray([]);
	_self.booleanValue = ko.observable(false);
	_self.endDays = ko.observable(data.endDays);
//	var datas = ["Alpha", "Gamma", "Beta"];
//	self.nonSelectItems = ko.observableArray(ko.utils.arrayMap(datas, function() {
//        return ["Alpha1", "Gamma1", "Beta1"];
//    }));
	
};
};
}


