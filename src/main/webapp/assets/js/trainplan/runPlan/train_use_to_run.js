/**
 * 调整列车时刻表
 * @author Denglj
*/



var _trainUpdateList = [];
//列车运行规律下拉框数组
var _runRuleArray = [{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"},{"code": "3", "text": "择日"}];
var _stopTypeArray = [{"code": "3", "text": "单列"},{"code": "4", "text": "整组"}];

var TrainRunTimePage = function () {
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		$("#input_trainRunTime_startDate").datepicker();
		$("#input_trainRunTime_endDate").datepicker();
		
		$("#liOption").multiselect2side({
			selectedPosition: 'right',
		    moveOptions: false,
			labelsx: '待选区',
			labeldx: '已选区'
		});
	   
	   
	
		
		
		//页面元素绑定
		var pageModel = new PageModel();
		ko.applyBindings(pageModel);
		
		pageModel.initPageData();
	};
	
	
	/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		

		_self.runRuleArray = ko.observableArray(_runRuleArray);	//开行规律下拉框
		_self.runRuleOption = ko.observable();					//开行规律下拉框选中项
		
		_self.stopTypeArray = ko.observableArray(_stopTypeArray);
		_self.stopTypeOption = ko.observable();			
		
		_self.trainStns = ko.observableArray();	//列车经由站列表
		_self.currentTrainInfoMessage = ko.observable("");


		_self.items = ko.observableArray();
		_self.nonSelectItems = ko.observableArray();
		_self.selectedDate = ko.observable();//择日
		_self.cmdEndFlag = ko.observable(false);//令止复选框
		
		

		//开行规律下拉框change事件
		_self.runRuleChange = function() {
			if(_self.runRuleOption().code != "3") {//!=择日
				_self.nonSelectItems.remove(function(item) {
					return true;
				});
				

				_self.items.remove(function(item) {
					return true;
				});
				
				_self.selectedDate("");
			}
		};
		
		//令止复选框change事件
		_self.cmdEndFlagChange = function() {
//			var obj = _self.wdCmdTrain().booleanValue();
//			//console.log(obj);
//			if(!obj) {
//				_self.wdCmdTrain().nonSelectItems([]);
//				_self.wdCmdTrain().items([]);
//				_self.wdCmdTrain().selectedDate("");
//			}
			
		};
		
		
		/**
		 * 加载 日期列表按钮
		 */
		_self.btnLoadSelectDate = function() {
			_self.nonSelectItems.remove(function(item) {
				return true;
			});
			
			if(!_self.checkSelectEndDate()) {//校验截至日期是否有效
				//截至日期 必须<=最大开行日期
				return;
			}
			
			var startDate = moment($("#input_trainRunTime_startDate").val());
			var endDate = moment($("#input_trainRunTime_endDate").val());
			while(startDate.isBefore(endDate)) {
				_self.nonSelectItems.push(startDate.format('YYYY-MM-DD'));
				startDate = startDate.add("day", 1);
			}
			if (startDate.format('YYYY-MM-DD') == endDate.format('YYYY-MM-DD')) {
				_self.nonSelectItems.push(startDate.format('YYYY-MM-DD'));
			}
		};
		
		
		
		
		/**
		 * 选择日期提交按钮
		 */
		_self.btnSubmitSelectDate = function() {
			var obj = $("#liOptionms2side__dx").find("option");//获取已选区日期数据
			var dateArray = [];
			
			if(obj.size() > 0) {
				for(var i=0; i<obj.size(); i++) {
					dateArray.push(obj.eq(i).text());
				}
			}
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/getSelectDateStr",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({"dateList" : dateArray}),
				success : function(result) {
					if (result != null) {
						_self.selectedDate(result.message);
					} else {
						showErrorDialog("生成择日信息失败");
					};
				},
				error : function() {
					showErrorDialog("生成择日信息出错");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		_self.checkSelectEndDate = function() {
			var _retBoolean = true;
			var _selectEndDate = $("#input_trainRunTime_endDate").val();
			var _maxRunDate = $("#trainRunTime_maxRunDate_hidden").val();
			
			if (_selectEndDate != _maxRunDate) {
				if (moment(_maxRunDate).isBefore(moment(_selectEndDate))) {
					showWarningDialog("无效截至日期！当前车次最大有效开行日期为："+_maxRunDate);
					_retBoolean = false;
				}
			}
			
			return _retBoolean;
		};
		
		
		
		/**
		 * 保存
		 */
		_self.saveTrainTime = function() {
			/*if(_trainUpdateList.length == 0) {
				showWarningDialog("操作取消！当前车次客运时刻未做任何改动。");
				return;
			}*/
			var startDate = $("#input_trainRunTime_startDate").val();
			var endDate = $("#input_trainRunTime_endDate").val();
			if(startDate!=null && startDate!=undefined && endDate!=null && endDate!=undefined){
				if(startDate > endDate){
					showWarningDialog("开始日期不能大于截至日期");
					return;
				}
			}
			if(_self.runRuleOption().code == "3" && (_self.selectedDate()==null||_self.selectedDate()=="")) {//择日
				showWarningDialog("无效操作，请先生成择日内容。");
				return;
			}
			if(!_self.checkSelectEndDate()) {//校验截至日期是否有效
				//截至日期 必须<=最大开行日期
				return;
			}
			
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/jbtcx/editPlanLineTrainStop",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					telName : $("#textarea_trainRunTime_telName").val(),
					startDate : $("#input_trainRunTime_startDate").val(),
					endDate : $("#input_trainRunTime_endDate").val(),
					runRule: _self.runRuleOption().code,//{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"},{"code": "3", "text": "择日"}
					trainNbr: $("#trainRunTime_trainNbr_hidden").val(),
					startStn:$("#trainRunTime_startStn_hidden").val(),
					endStn:$("#trainRunTime_endStn_hidden").val(),
					isCmdEnd: _self.cmdEndFlag()==true? "1":"0",
					selectedDate : _self.selectedDate(),
//					times: _trainUpdateList
					times:ko.toJSON(_self.trainStns()),
					stopType:_self.stopTypeOption().code,
					planTrainId: $("#trainRunTime_trainPlanId_hidden").val(),
					planCrossId: $("#trainRunTime_planCrossId_hidden").val()
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
			            showSuccessDialog("保存成功");

						window.parent.refreshParent();

						_self.resetPageStatus();	//重置页面状态
					} else {
						showErrorDialog(result.message);
						window.parent.refreshParent();
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
		
		
		
		/**
		 * 重置页面元素状态
		 */
		_self.resetPageStatus = function() {
			for (var i=0; i<_self.trainStns().length;i++) {
				var _oldObj = _self.trainStns()[i];
				for (var j=0;j<_trainUpdateList.length;j++) {

					if (_oldObj.planTrainStnId() == _trainUpdateList[j].planTrainStnId) {
						_oldObj.isChangeValue(0);	//改变该行颜色
						break;
					}
				}
			}
			
			//状态改变完成后 清空临时保存的修改对象
			_trainUpdateList = [];
		};
		
		
		/**
		 * 初始化
		 */
		_self.initPageData = function() {
			//清空待保存list
			_trainUpdateList = [];
			
			
			$("#input_trainRunTime_startDate").val($("#trainRunTime_runDate_hidden").val());
			$("#input_trainRunTime_endDate").val($("#trainRunTime_runDate_hidden").val());
			
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlan/trainStnByPlanTrainId",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					planTrainId: $("#trainRunTime_trainPlanId_hidden").val(),
					trainStnSource:$("#trainRunTime_trainStnSource_hidden").val()// (检索条件 	必填)查询时刻时数据来源  JBT:基本图   KY:客运
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data.length == 0) {
							showWarningDialog("当前车次客运时刻数据不存在。");
							return;
						}
						
						var message = "车次："+$("#trainRunTime_trainNbr_hidden").val()+"&nbsp;&nbsp;&nbsp;&nbsp;"+
						"始发日期："+$("#trainRunTime_runDate_hidden").val()+
						"&nbsp;&nbsp;&nbsp;&nbsp;" + $("#trainRunTime_startStn_hidden").val() + "&nbsp;→&nbsp;" +  $("#trainRunTime_endStn_hidden").val();
						_self.currentTrainInfoMessage(message);
					
						$.each(result.data, function(i, obj){
							_self.trainStns.push(new TrainTimeRow(obj)); 
						});
						
			            // 表头固定
						
			            $("#div_form").freezeHeader();
			            $("#table_run_plan_train_times_edit").freezeHeader();
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
		
		
		
		
	};
	
	
	/**
	 * 列表行对象
	 * @param data
	 */
	function TrainTimeRow(data) {
		var _self = this;
		_self.data = data;
		_self.index = ko.observable(data.stnSort + 1);
		_self.planTrainStnId = ko.observable(data.planTrainStnId);
		_self.stnSort = ko.observable(data.stnSort);
		_self.stnName = ko.observable(data.stnName);
		_self.nodeId = ko.observable(filterValue(data.nodeId));
		_self.nodeName = ko.observable(filterValue(data.nodeName));
		_self.bureauId = ko.observable(filterValue(data.bureauId));
		_self.bureauShortName = ko.observable(data.bureauShortName);
		_self.stnBureauFull = ko.observable(data.stnBureauFull);
		_self.arrTrainNbr = ko.observable(filterValue(data.arrTrainNbr));
		_self.arrTime = ko.observable(data.stationFlag=="SFZ"?data.dptTime:data.arrTime);
//		_self.arrTime = ko.computed(function(){
//			if(data.stationFlag!=null && data.stationFlag!=undefined){
//				if(data.stationFlag == 'SFZ'){
//					return "--";
//				}else{
//					return data.arrTime;
//				}
//			}
//		});
		_self.baseArrTime = ko.observable(filterTimeValue(data.baseArrTime));
		_self.arrRunDays = ko.observable(data.arrRunDays);
//		_self.arrRunDays = ko.computed(function(){
//			if(data.stationFlag!=null && data.stationFlag!=undefined){
//				if(data.stationFlag == 'SFZ'){
//					return "--";
//				}else{
//					return data.arrRunDays;
//				}
//			}
//		});
		_self.dptTrainNbr = ko.observable(filterValue(data.dptTrainNbr));
		_self.dptTime = ko.observable(data.stationFlag=="ZDZ"?data.arrTime:data.dptTime);
//		_self.dptTime = ko.computed(function(){
//			if(data.stationFlag!=null && data.stationFlag!=undefined){
//				if(data.stationFlag == 'ZDZ'){
//					return "--";
//				}else{
//					return data.dptTime;
//				}
//			}
//		});
		_self.baseDptTime = ko.observable(filterTimeValue(data.baseDptTime));
		_self.runDays = ko.observable(data.runDays);
//		_self.runDays = ko.computed(function(){
//			if(data.stationFlag!=null && data.stationFlag!=undefined){
//				if(data.stationFlag == 'ZDZ'){
//					return "--";
//				}else{
//					return data.runDays;
//				}
//			}
//		});
		_self.trackName = ko.observable(filterValue(data.trackName));
		_self.platForm = ko.observable(filterValue(data.platForm));
		_self.jobs = ko.observable(filterValue(data.jobs));
		
		_self.stepStr = ko.observable(GetDateDiff(data));
		_self.stationFlag = ko.observable(data.stationFlag);
		_self.isChangeValue = ko.observable(0);
		
		/**
		 * 获取2个日期天数差
		 * dateStr1 yyyy-MM-dd
		 * dateStr2 yyyy-MM-dd
		 */
		function getDaysBetween(dateStr1, dateStr2) {
			var days = 0;
			if (dateStr1 == dateStr2) {//dateStr2 = dateStr1
				return 0;
			}
			
			var _date1 = moment(dateStr1);
			var _date2 = moment(dateStr2);
			
			if (_date1.isBefore(_date2)) {//dateStr2 > dateStr1
				while(_date1.isBefore(_date2)) {
					days ++ ;
					_date1 = _date1.add("day", 1);
				}
			} else {
				while(_date2.isBefore(_date1)) {//dateStr2 < dateStr1
					days -- ;
					_date1 = _date1.add("day", -1);
				}
			}
			
			return days;
		};
		
		/**
		 * 到达时间变更
		 */
		_self.onArrTimeChange = function() {
			_self.data.arrTime = _self.arrTime();
			_self.stepStr(GetDateDiff(_self.data));
			_self.data.stepStr = _self.stepStr();
			_self.isChangeValue(1);
			
			//计算到达运行天数=到达日期-始发日期天数差
			_self.arrRunDays(getDaysBetween($("#trainRunTime_runDate_hidden").val(), moment(_self.arrTime()).format('YYYY-MM-DD')));
			_self.data.arrRunDays = _self.arrRunDays();

			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.arrTime = _self.arrTime();
					_tempData.stepStr = _self.stepStr();
					
					isAdd = false;
					break;
				}
			}
			
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		

		/**
		 * 出发时间变更
		 */
		_self.onDeptTimeChange = function() {
			_self.data.dptTime = _self.dptTime();
			_self.stepStr(GetDateDiff(_self.data));
			_self.data.stepStr = _self.stepStr();
			_self.isChangeValue(1);

			//计算到达运行天数=到达日期-始发日期天数差
			_self.runDays(getDaysBetween($("#trainRunTime_runDate_hidden").val(), moment(_self.dptTime()).format('YYYY-MM-DD')));
			_self.data.runDays = _self.runDays();

			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.dptTime = _self.dptTime();
					_tempData.stepStr = _self.stepStr();
					
					isAdd = false;
					break;
				}
			}
			
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		

		/**
		 * 股道变更
		 */
		_self.onTrackNameChange = function() {
			_self.data.trackName = _self.trackName();
			_self.isChangeValue(1);
			
			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.trackName = _self.trackName();
					
					isAdd = false;
					break;
				}
			}
			
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		
	};

	
	/**
	 * 计算时间差
	 * @param data
	 * @returns
	 */
	function GetDateDiff(data) {
		if(data.dptTime == '-' 
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
	

	function filterTimeValue(value){
		return (value == null || value == "null" || value =="" )? "--" : value;
	};
	
	function filterValue(value){
		return value == null || value == "null" ? "" : value;
	};
	
	
};






$(function() {
	new TrainRunTimePage().initPage();
});

