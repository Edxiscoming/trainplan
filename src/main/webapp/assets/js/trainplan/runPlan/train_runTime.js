/**
 * 调整列车时刻表
 * @author Denglj
*/

var userLoginBureauShortName;

var _trainUpdateList = [];
//列车运行规律下拉框数组
var _runRuleArray = [{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"},{"code": "3", "text": "择日"}];
var trainStnsArray =[];
var trainStnsArrayObj =[];
var pageModel ;
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
	   
	   
	
		userLoginBureauShortName = $("#userLoginBureauShortName").val();
		
		//页面元素绑定
		pageModel = new PageModel();
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
		_self.trainStns = ko.observableArray();	//列车经由站列表
		_self.currentTrainInfoMessage = ko.observable("");
		_self.currentTrainLineTimesStn = ko.observable(newTrainLineTimesRow());//列车时刻表选中行记录  用于上下移动

		_self.items = ko.observableArray();
		_self.nonSelectItems = ko.observableArray();
		_self.selectedDate = ko.observable();//择日
		_self.cmdEndFlag = ko.observable(false);//令止复选框
		
		//modify
		_self.planTrainId = ko.observable();

		
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
			
//			if(_trainUpdateList.length == 0) {
//				showWarningDialog("操作取消！当前车次客运时刻未做任何改动。");
//				return;
//			}
			if(_self.runRuleOption().code == "3" && (_self.selectedDate()==null||_self.selectedDate()=="")) {//择日
				showWarningDialog("无效操作，请先生成择日内容。");
				return;
			}
			if(!_self.checkSelectEndDate()) {//校验截至日期是否有效
				//截至日期 必须<=最大开行日期
				return;
			}
			var tName = $("#textarea_trainRunTime_telName").val();
			if(tName!=null && tName!=undefined){
				if(tName.length > 40){
					showWarningDialog("调整依据内容不能大于40字符");
					return;
				}
			}
			
			var s = $("#input_trainRunTime_startDate").val();
			var e = $("#input_trainRunTime_endDate").val();
			
				if(s  > e ){
					showWarningDialog("开始日期不能大于截止日期");
			    	return;
				}
			
				
			for(var i=0;i<pageModel.trainStns().length;i++){
				var obj = pageModel.trainStns()[i];
				var uobj = null;
				if(i!=0){
					uobj = pageModel.trainStns()[i-1];
				}
				if(obj!=null && obj!=undefined && uobj!=null && uobj!=undefined){
					if(obj.arrTime() > obj.dptTime()){
						obj.setColor(1);//设置颜色
						$("#isSave").attr('disabled',true);
						showErrorDialog("到达时间不能大于出发时间!");
						return;
					}
					if(obj.arrTime() < uobj.dptTime()){
						obj.setColor(1);//设置颜色
						$("#isSave").attr('disabled',true);
						showErrorDialog("上一行的出发时间不能大于下一行的到达时间时间!");
						return;
					}
				}
			}	
				
			var obj = {
					telName : tName,
					startDate : $("#input_trainRunTime_startDate").val(),
					endDate : $("#input_trainRunTime_endDate").val(),
					runRule: _self.runRuleOption().code,//{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"},{"code": "3", "text": "择日"}
					trainNbr: $("#trainRunTime_trainNbr_hidden").val(),
					startStn:$("#trainRunTime_startStn_hidden").val(),
					endStn:$("#trainRunTime_endStn_hidden").val(),
					
					planTrainId:_self.planTrainId,
					
					isCmdEnd: _self.cmdEndFlag()==true? "1":"0",
					selectedDate : _self.selectedDate(),
//					times: _trainUpdateList
					times:ko.toJSON(pageModel.trainStns())
				};
			
			var trainArray2 = pageModel.trainStns();
			
			var cccc2 = trainArray2[1];
			var arrtime7 = cccc2.arrTime();
			var dpttime7 = cccc2.dptTime();
			
			var trainArray3 = _self.trainStns();
			
			var cccc3 = trainArray3[1];
			var arrtime8 = cccc3.arrTime();
			var dpttime8 = cccc3.dptTime();
			
//			console.log(trainStnsArrayObj);
//			console.log(obj);
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/jbtcx/editPlanLineTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify(obj),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
			            showSuccessDialog("保存成功");
						window.parent.refreshParent();
						_self.resetPageStatus();	//重置页面状态
					} else {
//						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
						showErrorDialog(result.message);
					} 
				},
				error : function() {
//					showErrorDialog("接口调用失败");
					showErrorDialog(result.message);
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
						trainStnsArray=[];//每次清空数组
						$.each(result.data, function(i, obj){
							_self.trainStns.push(new TrainTimeRow(obj)); 
//							console.log(ko.toJSON(_self.trainStns()));
							trainStnsArray.push(new TrainTimeRow(obj));
							//modify
							_self.planTrainId = obj.planTrainId;
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
		
		/**
		 * 时刻列表行点击事件
		 */
		_self.setTrainLineTimesCurrentRec = function(row) {
			_self.currentTrainLineTimesStn(row);
		};
		
		

		/**
		 * 更新站点作业信息
		 */
		_self.editJobs = function(row) {
			//1.清除数据
			_self.setTrainLineTimesCurrentRec(row);
			
//			if(_self.permissionDenied()){//是本局  有操作
			if(row.permissionDenied()) {
				$("#editJobsModal").prop("aria-hidden", "true");
				$("#editJobsModal").hide();
				return;//没有权限
			}else{
				_self.setJobCheckBoxVal();
			}
			
			
		};
		/**
		 * 设置复选框选中状态
		 * 编辑站点作业界面初始化及“重置”按钮点击事件
		 * @param jobs "items": [
	                        "始发",
	                        "机车换乘挂",
	                        "车底转头"
	                    ]
		 */
		_self.setJobCheckBoxVal = function() {
			$('input[name="jobCheckbox"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
				$(this).prop("checked", false);
				$(this).removeAttr("checked");
				if($.inArray($(this).val(), _self.currentTrainLineTimesStn().jobItems()) > -1) {
					$(this).prop("checked",true);
				} else {
					$(this).prop("checked", false);
				}
	        });
		};
		
		
		_self.saveJobs = function() {
			var _items = [];
			$('input[name="jobCheckbox"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
				if($(this).prop("checked")) {
					_items.push($(this).val());
				}
	        });
			var jobsTypeSubstring = _self.currentTrainLineTimesStn().data.jobs.substring(1,3);
			_items.unshift(jobsTypeSubstring);
			_self.currentTrainLineTimesStn().data.jobs.items = _items;
			_self.currentTrainLineTimesStn().jobItems(_items);
			
			$.each(_self.trainStns(), function(i, obj){
				if(obj.planTrainStnId()==_self.currentTrainLineTimesStn().planTrainStnId()){
					obj.data.jobs.items = _items;
					obj.jobItems(_items);
					obj.jobs('<'+_items.join('><')+'>');
				}
			});
		};
		
		
	};
	
	
	/**
	 * 创建运行线时刻列表行空对象
	 * 用于插入、追加行时
	 * @param index trainLineTimes(当前运行线时刻表table绑定对象数组长度)，若为null则为追加
	 * @returns {trainLineTimesRowModel}
	 */
	function newTrainLineTimesRow(index) {
		var _index = (typeof index=="undefined"||index==null)?-10:index;
		return new TrainTimeRow({
			index:_index,
			stnName:"",
			planTrainStnId:"",
//			bureauShortName:user.bureauShortName,
			trackName:"",
			platForm:"",
			nodeId:"",
			jobs:"",
			nodeName:"",
			bureauId:"",
			arrTrainNbr:"",
			sourceTimeSchedule: {
	            dates: "0",
	            hour: "00",
	            minute: "00",
	            second: "00",
	            text: "当天 00:00:00"
	        },
	        dptTrainNbr:"",
	        targetTimeSchedule: {
	            dates: "0",
	            hour: "00",
	            minute: "00",
	            second: "00",
	            text: "当天 00:00:00"
	        },
	        jobItems:[],
			jobItemsText:"",
			kyyy:"",
//	        ,
//            jobs: {
//                code: "",
//                items: [],
//                itemsText: ""
//            }
		});
	};
	
	/**
	 * 列表行对象
	 * @param data
	 */
	function TrainTimeRow(data) {
		var _self = this;
		_self.kyyyOptions = ko.observableArray([{"code":"n","text":""},{"code":"y","text":"是"}]);//客运营业下拉框数据
		
		_self.data = data;
		
		_self.hiddenIndex = ko.observable(data.rn);
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
		
		_self.permissionDenied = ko.computed(function () {
//			if(user.bureauShortName=="" || user.bureauShortName==_self.bureauShortName()){
			if(userLoginBureauShortName=="" || userLoginBureauShortName==_self.bureauShortName()){
				return false;//拥有此数据操作权限
			} else {
				return true;//没有此数据操作权限
			}
		}, this);
//		_self.arrTime = ko.observable(data.stationFlag=="SFZ"?data.dptTime:data.arrTime).extend({
//            required: { params: true, message: "请输入到达时间" },
//            pattern: {
//                params: _regexStr,
//                message: "格式不正确"//00:59  23:59:59
//            }
//        });
		
		_self.setColor = ko.observable(0);
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
//		_self.dptTime = ko.observable(data.stationFlag=="ZDZ"?data.arrTime:data.dptTime).extend({
//            required: { params: true, message: "请输入到达时间" },
//            pattern: {
//                params: _regexStr,
//                message: "格式不正确"//00:59  23:59:59
//            }
//        });
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
		_self.runDays = ko.observable(data.stationFlag=="ZDZ"?data.arrRunDays:data.runDays);
		_self.data.runDays = _self.runDays();
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
		
		_self.nodeStationId = ko.observable(data.nodeStationId);
		_self.nodeStationName = ko.observable(data.nodeStationName);
		_self.nodeTdcsId = ko.observable(data.nodeTdcsId);
		_self.nodeTdcsName = ko.observable(data.nodeTdcsName);
		
		var jobsText = typeof data.jobs=='object'?'':data.jobs;
		jobsText = jobsText.replace('<客运营业>','').replace('<经由>','');
		jobsText = jobsText.substring(1,jobsText.length-1);
		_self.jobItems = ko.observableArray(typeof data.jobs=="string"?jobsText.split("><"):[]);
		_self.jobItemsText = ko.computed(function () {
				return _self.jobItems().join(",");
		}, this);
		
		
		//客运营业下拉框选择值
		var _val = "";
//		if(_self.stepStr() !="" || data.stationFlag=="SFZ" || data.stationFlag=="ZDZ") {
		if(data.stationFlag=="SFZ" || data.stationFlag=="ZDZ") {
			_val = "y";
		} else {
			_val = "n";
		}
		var jobsValue = data.jobs==undefined?"":data.jobs;
		if(jobsValue.indexOf("客运营业")>0){
			_val = "y";
		}
		
		var _array = _self.kyyyOptions();
		var _arrayOp = [];
		for(var i=0;i<_array.length;i++) {
			if(_val == _array[i].code) {
				_arrayOp = _array[i];
			}
		}
			
		_self.kyyy = ko.observable(_arrayOp);//客运营业
		
		
		/**
		 * 更新站点作业信息
		 */
		_self.setKyyy = function() {
			var kyyy = _self.kyyy();
			$.each(pageModel.trainStns(), function(i, obj){
				if(obj.planTrainStnId()==_self.planTrainStnId()){
					obj.kyyy(_self.kyyy())
				}
			});
		};
		
		_self.stepStrChange = function() {
			var stepStr = _self.stepStr();
			var _val = "";
			if(_self.stepStr() !="") {
				_val = "y";
			} else {
				_val = "n";
			}
			var _array = _self.kyyyOptions();
			var _arrayOp = [];
			for(var i=0;i<_array.length;i++) {
				if(_val == _array[i].code) {
					_arrayOp = _array[i];
				}
			}
			_self.kyyy(_arrayOp);
		}
		
		
		//return true:可用/false:不可用
		_self.btnEnable = ko.computed(function () {
			//timesDivBtnDisabled 时刻表table操作功能按钮false:可用/true：禁用
			//permissionDenied 权限 false拥有此数据操作权限/true没有此数据操作权限
			return !_self.permissionDenied();
		}, this);
		
		_self.inputReadOnly = ko.computed(function () {
			return _self.permissionDenied();
		}, this);
/*
 * 出发到达车次  离焦事件
*/		
		_self.sourceTrainNbrOnblur = function(n, event){
			_self.isChangeValue(1);
			if(_self.arrTrainNbr()!="" && _self.arrTrainNbr()!=null){
				_self.dptTrainNbr(_self.arrTrainNbr());
				var index = 10000000;
				$.each(pageModel.trainStns(), function(i, obj){
					if(obj.planTrainStnId()==_self.planTrainStnId()){
						index = i;
					}
					if (i > index) {
						
						//只改本局的车次
						if(obj.bureauShortName()==userLoginBureauShortName){
							obj.arrTrainNbr(_self.arrTrainNbr());
							obj.dptTrainNbr(_self.arrTrainNbr());
						}
					}
				});
			}
		};
		

		_self.targetTrainNbrOnblur = function(n, event){
			_self.isChangeValue(1);
			var index = 10000000;
			if(_self.dptTrainNbr()!="" && _self.dptTrainNbr()!=null ){
				$.each(pageModel.trainStns(), function(i, obj){
					if(obj.planTrainStnId()==_self.planTrainStnId()){
						index = i;
					}
					if (i > index) {
						//只改本局的车次
						if(obj.bureauShortName()==userLoginBureauShortName){
							obj.arrTrainNbr(_self.dptTrainNbr());
							obj.dptTrainNbr(_self.dptTrainNbr());
						}
					}
				});
			}
		};
		
		_self.sourceTrainNbrChange = function(n, event){
			_self.isChangeValue(1);
			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.arrTrainNbr = _self.arrTrainNbr();
					
					isAdd = false;
					break;
				}
			}
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		
		
		_self.targetTrainNbrChange = function(n, event){
			_self.isChangeValue(1);
			
			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.dptTrainNbr = _self.dptTrainNbr();
					
					isAdd = false;
					break;
				}
			}
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
		_self.onPlatFormChange = function(n, event){
			_self.isChangeValue(1);
			
			var isAdd = true;	//是否将该行记录增加到需要保存数组中
			for (var i=0;i<_trainUpdateList.length;i++) {
				var _tempData = _trainUpdateList[i];
				if (_tempData.planTrainStnId == _self.planTrainStnId()) {
					_tempData.platForm = _self.platForm();
					
					isAdd = false;
					break;
				}
			}
			if (isAdd) {
				_trainUpdateList.push(_self.data);
			}
		};
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
		
		
		//到达时间
		_self.arrTimeOnblur = function(){
			var firstA = null;
			$('#isSave').removeAttr("disabled");
			_self.setColor(0);
			var j =0;
			var date3 = 0;//时间差的毫秒数 
			var _regex = new RegExp("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
			var arrT = _self.arrTime();//到达时间
			var dptT = _self.dptTime();//出发时间
			if(arrT!=null && arrT!="" && arrT!=undefined){
				if(!_regex.test(arrT)){
					_self.setColor(1);//设置颜色
					$("#isSave").attr('disabled',true);
					showErrorDialog("请输入有效的到达时间!");
					return;
				}
			}
//			if(arrT!=null && arrT!=undefined && dptT!=null && dptT!=undefined){
//				if(arrT > dptT){
//					_self.setColor(1);
//					$("#isSave").attr('disabled',true);
//					showErrorDialog("出发时间不能小于到达时间!");
//					return ;
//				}
//			}
			var planTrainStnId = _self.planTrainStnId();
			if(trainStnsArray!=null && trainStnsArray.length > 0){
				for(var i = 0; i < trainStnsArray.length; i++){
					var trainArray =  pageModel.trainStns();//页面trainStns数组对象
					firstA = trainArray[0];
					j = i;
					var obj = trainStnsArray[i];
					var uobj = null;
					if(i!=0){
						uobj = trainStnsArray[i-1];
					}
					var arrtime = obj.arrTime();
					var dptTi = obj.dptTime();
					var planTrainId = obj.planTrainStnId();
					if(planTrainStnId == planTrainId){//找到对应的那一行数据
						var startTime = new Date(arrtime);
						var endTime = new Date(arrT);
						if(uobj!=null && uobj!=undefined ){
							if(uobj.dptTime() > arrT){
								_self.setColor(1);
								$("#isSave").attr('disabled',true);
								showErrorDialog("调整的时间不能小于上一站的出发时间!");
								return ;
							}
						}
						date3 = endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
						var flag ="+";
						if(date3 >0){
							flag ="+";
						}else if(date3 <0){
							flag ="-";
						}else{
							flag ="";
						}
						var diff = GetDateDiff2(arrtime,arrT);
						if(diff!= null && date3!=0){
								showConfirmDiv2('确定修改','“'+obj.nodeName()+'”'+'的到达时间</br>由'+'“'+arrtime+'”'+'改为'+'“'+arrT+'”'+',</br>是否从本站'+'“'+obj.nodeName()+'”'+'的出发时间开始，以及后续所有车站的到发时间都顺延'+'“'+flag+diff+'”'+'？');
							
						}
						
						break;
					}
					
					
				}
				//否
				$("#_confirm_cancel_btn2").unbind("click");  
				$("#_confirm_cancel_btn2").click(function(){
					
					if(arrT!=null && arrT!=undefined && dptT!=null && dptT!=undefined){
						if(arrT > dptT){
							_self.setColor(1);//设置颜色
							$("#isSave").attr('disabled',true);
							showErrorDialog("到达时间不能大于出发时间!");
							return;
						}
					}
					
					if(trainStnsArray!=null && trainStnsArray.length>0){
						for(var i = 0; i < trainStnsArray.length; i++){
							    var trainArray =  pageModel.trainStns();//页面trainStns数组对象
							    var newObj = trainArray[i];
								var upObj = trainStnsArray[i-1];//上一站
								var obj = trainStnsArray[i];
								var planTrainId = obj.planTrainStnId();
//								trainStnsArrayObj.push(new TrainTimeRow(newObj));
								//始发站的到达时间和出发时间一致
								if(obj.stationFlag()=="SFZ"){
									if(newObj.arrTime() != newObj.dptTime()){
										_self.setColor(1);//设置颜色
										$("#isSave").attr('disabled',true);
										showErrorDialog("始发站的到达时间和出发时间不一致");
										return;
									}
								}
								
								//始发站的到达时间和出发时间一致
								if(obj.stationFlag()=="ZDZ"){
									if(newObj.arrTime() != newObj.dptTime()){
										_self.setColor(1);//设置颜色
										$("#isSave").attr('disabled',true);
										showErrorDialog("终到站的到达时间和出发时间不一致");
										return;
									}
								}
								
								
								if(planTrainStnId == planTrainId){
									newObj.arrTime(arrT);
									obj.arrTime(arrT);
									obj.dptTime(dptT);
									newObj.stepStr(GetDateDiff3(newObj));
									newObj.arrRunDays(getDaysBetween(moment(firstA.arrTime()).format('YYYY-MM-DD'),moment(arrT).format('YYYY-MM-DD')));
									newObj.runDays(getDaysBetween(moment(firstA.dptT()).format('YYYY-MM-DD'),moment(dptT).format('YYYY-MM-DD')));
									if(upObj!=null && upObj!=undefined){
										if(upObj.dptTime() > arrT){
											_self.setColor(1);//设置颜色
											$("#isSave").attr('disabled',true);
											showErrorDialog("上一站的出发时间不能大于下一站的到达时间!");
											return;
										}
									}
								
								}
							
						}
					}
				});
				
				//取消
				$("#_confirm_quxiao_btn").unbind("click");  
				$("#_confirm_quxiao_btn").click(function(){
					if(trainStnsArray!=null && trainStnsArray.length > 0){
						for(var i = 0; i < trainStnsArray.length; i++){
							var trainArray =  pageModel.trainStns();//页面trainStns数组对象
							var newObj = trainArray[i];
							var obj = trainStnsArray[i];
							var planTrainId = obj.planTrainStnId();
							if(planTrainStnId == planTrainId){
								newObj.arrTime(obj.arrTime());
								newObj.dptTime(newObj.dptTime());
								newObj.stepStr(GetDateDiff3(newObj));
								break;
							}
							
						}
					}
					
				});
				
				//是
				$("#_confirm_btn2").unbind("click"); 
				$("#_confirm_btn2").click(function(){
					var dis = 0;
					for(var i = j; i < trainStnsArray.length; i++){
					
						var trainArray =  pageModel.trainStns();//页面trainStns数组对象
						var newObj = trainArray[i];
						var arrtime2 = newObj.arrTime();
						var dpttime2 = newObj.dptTime();
						
						var oldObj = trainStnsArray[i];
						
						var arrtime = oldObj.arrTime();
						var dpttime = oldObj.dptTime();
						
						var stnName = oldObj.stnName();
						
						var startTime = new Date(arrtime);
						var endTime = new Date(dpttime);
						
						var starthms =  startTime.getTime();//得到毫秒数
						var endhms =  endTime.getTime();//得到毫秒数
						
						if(i==j){
							var at = new Date(arrT);//改变后的输入框的时间
							var athms = at.getTime();
							dis = athms - startTime;//毫秒数差
						}
						
						//只改本局的时间
						if(oldObj.bureauShortName()==userLoginBureauShortName){
							
							var shms = starthms + dis;
							var ehms = endhms + dis;
							
							var newSTime = new Date(shms); //就得到普通的时间了 
							var newETime = new Date(ehms); //就得到普通的时间了 
							
							var sdata=newSTime.format("yyyy-MM-dd hh:mm:ss"); 
							var edata = newETime.format("yyyy-MM-dd hh:mm:ss");
							
							newObj.arrTime(sdata);
							newObj.dptTime(edata);
							newObj.arrRunDays(getDaysBetween(moment(firstA.arrTime()).format('YYYY-MM-DD'),moment(sdata).format('YYYY-MM-DD')));
							newObj.runDays(getDaysBetween(moment(firstA.dptTime()).format('YYYY-MM-DD'),moment(edata).format('YYYY-MM-DD')));
							
							
							
							oldObj.arrTime(sdata);
							oldObj.dptTime(edata);
	//						trainStnsArrayObj.push(new TrainTimeRow(newObj));
							newObj.stepStr(GetDateDiff3(newObj));
							newObj.isChangeValue(1);
						}
					}
					
					
				});
				
				
			}
		
			
			

			
		};
		
		
		_self.deptTimeOnblur = function(){
			var _regex = new RegExp("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
			var arrT = _self.arrTime();//到达时间
			var dptT = _self.dptTime();//出发时间
			var firstD = null;
			$('#isSave').removeAttr("disabled");
			_self.setColor(0);
			var j =0;
			var date3 = 0;//时间差的毫秒数 
			var planTrainStnId = _self.planTrainStnId();
			if(dptT!=null && dptT!="" && dptT!=undefined){
				if(!_regex.test(dptT)){
					_self.setColor(1);//设置颜色
					$("#isSave").attr('disabled',true);
					showErrorDialog("请输入有效的出发时间!");
					return;
				}
			}
//			if(arrT!=null && arrT!=undefined && dptT!=null && dptT!=undefined){
//				if(arrT > dptT){
//					_self.setColor(1);
//					$("#isSave").attr('disabled',true);
//					showErrorDialog("出发时间不能小于到达时间!");
//					return ;
//				}
//			}
			if(trainStnsArray!=null && trainStnsArray.length > 0){
				for(var i = 0; i < trainStnsArray.length; i++){
					 var trainArray =  pageModel.trainStns();//页面trainStns数组对象
					 firstD = trainArray[0];
					j = i;
					var obj = trainStnsArray[i];
					var nextobj = trainStnsArray[i+1];
					var dptTime = obj.dptTime();
					var planTrainId = obj.planTrainStnId();
					if(planTrainStnId == planTrainId){//找到对应的那一行数据
						var startTime = new Date(dptTime);
						var endTime = new Date(dptT);
						date3 = endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
						var flag ="+";
						if(date3 >0){
							flag ="+";
						}else{
							flag ="-";
						}
						var diff = GetDateDiff2(dptTime,dptT);
						if(diff!= null && date3!=0 ){
								showConfirmDiv2('确定修改','“'+obj.nodeName()+'”'+'的出发时间</br>由'+'“'+dptTime+'”'+'改为'+'“'+dptT+'”'+'，</br>是否从下一站'+'“'+nextobj.nodeName()+'”'+'开始，以及后续所有车站的到发时间都顺延'+'“'+flag+diff+'”'+'？');
							
						}
						
						break;
					}
					
					
				}
				
				//否
				$("#_confirm_cancel_btn2").unbind("click");  
				$("#_confirm_cancel_btn2").click(function(){
					
					if(arrT!=null && arrT!=undefined && dptT!=null && dptT!=undefined){
						if(arrT > dptT){
							_self.setColor(1);
							$("#isSave").attr('disabled',true);
							showErrorDialog("出发时间不能小于到达时间!");
							return ;
						}
					}
					var planTrainStnId = _self.planTrainStnId();
					if(trainStnsArray!=null && trainStnsArray.length>0){
						for(var i = 0; i < trainStnsArray.length; i++){
							    var trainArray =  pageModel.trainStns();//页面trainStns数组对象
							    var newObj = trainArray[i];
								var upObj = trainStnsArray[i-1];//上一站
								var downObj = trainStnsArray[i+1];//下一站
								var obj = trainStnsArray[i];
								var planTrainId = obj.planTrainStnId();
//								trainStnsArrayObj.push(new TrainTimeRow(newObj));
								
								//始发站的到达时间和出发时间一致
								if(obj.stationFlag()=="SFZ"){
									if(newObj.arrTime() != newObj.dptTime()){
										_self.setColor(1);//设置颜色
										$("#isSave").attr('disabled',true);
										showErrorDialog("始发站的到达时间和出发时间不一致");
										return;
									}
								}
								
								//始发站的到达时间和出发时间一致
								if(obj.stationFlag()=="ZDZ"){
									if(newObj.arrTime() != newObj.dptTime()){
										_self.setColor(1);//设置颜色
										$("#isSave").attr('disabled',true);
										showErrorDialog("终到站的到达时间和出发时间不一致");
										return;
									}
								}
								
								if(planTrainStnId == planTrainId){
									newObj.arrTime(arrT);
									obj.arrTime(arrT);
									obj.dptTime(dptT);
									newObj.stepStr(GetDateDiff3(newObj));
									newObj.arrRunDays(getDaysBetween(moment(firstD.arrTime()).format('YYYY-MM-DD'),moment(arrT).format('YYYY-MM-DD')));
									newObj.runDays(getDaysBetween(moment(firstD.dptT()).format('YYYY-MM-DD'),moment(dptT).format('YYYY-MM-DD')));
									if(dptT> downObj.arrTime()){
										_self.setColor(1);
										$("#isSave").attr('disabled',true);
										showErrorDialog("上一站的出发时间不能大于下一站的到达时间!");
										return ;
									}
								}
							
						}
					}
				});
				
				//取消
				$("#_confirm_quxiao_btn").unbind("click");  
				$("#_confirm_quxiao_btn").click(function(){
					if(trainStnsArray!=null && trainStnsArray.length > 0){
						for(var i = 0; i < trainStnsArray.length; i++){
							var trainArray =  pageModel.trainStns();//页面trainStns数组对象
							var newObj = trainArray[i];
							var obj = trainStnsArray[i];
							var planTrainId = obj.planTrainStnId();
							if(planTrainStnId == planTrainId){
								newObj.arrTime(newObj.arrTime());
								newObj.dptTime(obj.dptTime());
								newObj.stepStr(GetDateDiff3(newObj));
								break;
							}
							
						
						}
					}
					
				});
				
				//是
				$("#_confirm_btn2").unbind("click"); 
				$("#_confirm_btn2").click(function(){
					var dis = 0;
					for(var i = j; i < trainStnsArray.length; i++){
						
						var trainArray =  pageModel.trainStns();//页面trainStns数组对象
						var newObj = trainArray[i];
						var arrtime2 = newObj.arrTime();
						var dpttime2 = newObj.dptTime();
						
						
						var oldObj = trainStnsArray[i];
						
						
						var arrtime = oldObj.arrTime();
						var dpttime = oldObj.dptTime();
						
						var stnName = oldObj.stnName();
						
						var startTime = new Date(arrtime);
						var endTime = new Date(dpttime);
						
						var starthms =  startTime.getTime();//得到毫秒数
						var endhms =  endTime.getTime();//得到毫秒数
						
						if(i==j){
							var at = new Date(dptT);//改变后的输入框的时间
							var athms = at.getTime();
							dis = athms - endhms;//毫秒数差
						}
						
						
						//只改本局的时间
						if(oldObj.bureauShortName()==userLoginBureauShortName){
							
							var shms = starthms + dis;
							var ehms = endhms + dis;
							
							var newSTime = new Date(shms); //就得到普通的时间了 
							var newETime = new Date(ehms); //就得到普通的时间了 
							
							var sdata=newSTime.format("yyyy-MM-dd hh:mm:ss"); 
							var edata = newETime.format("yyyy-MM-dd hh:mm:ss");
							if(i==j){
	//							newObj.arrTime(sdata);
								newObj.dptTime(edata);
								
								oldObj.arrTime(sdata);
								oldObj.dptTime(edata);
							}else{
								newObj.arrTime(sdata);
								newObj.dptTime(edata);
								
								oldObj.arrTime(sdata);
								oldObj.dptTime(edata);
							}
	//						trainStnsArrayObj.push(new TrainTimeRow(newObj));
							newObj.stepStr(GetDateDiff3(newObj));
							newObj.isChangeValue(1);
							newObj.arrRunDays(getDaysBetween(moment(firstD.arrTime()).format('YYYY-MM-DD'),moment(sdata).format('YYYY-MM-DD')));
							newObj.runDays(getDaysBetween(moment(firstD.dptTime()).format('YYYY-MM-DD'),moment(edata).format('YYYY-MM-DD')));
							
						}
					}
					
				});
				
				
			}
			
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
			
			if(trainStnsArray!=null && trainStnsArray.length>0){
				for(var i = 0; i < trainStnsArray.length; i++){
					    var trainArray =  pageModel.trainStns();//页面trainStns数组对象
					    var newObj = trainArray[i];
						var upObj = trainStnsArray[i-1];//上一站
						var obj = trainStnsArray[i];
						var planTrainId = obj.planTrainStnId();
						if (planTrainId == _self.planTrainStnId()) {
							newObj.trackName(_self.trackName());
						}
						
//						trainStnsArrayObj.push(new TrainTimeRow(newObj));
				}
			}	
			
			
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
	 * 计算时间差2
	 * @param data
	 * @returns
	 */
	function GetDateDiffHms(data1,data2) {
		if(data1 == '-' 
				 || data1 == null 
				 || data2 == null
				 || data2 == '-'){
			return "";
		}
		
		var startTime = new Date(data1);
		var endTime = new Date(data2);
		var result = "";
		
		result=endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
		

		 
		return result == "" ? "" : result; 
	};
	
	
	/**
	 * 计算时间差2
	 * @param data
	 * @returns
	 */
	function GetDateDiff2(data1,data2) {
		if(data1 == '-' 
				 || data1 == null 
				 || data2 == null
				 || data2 == '-'){
			return "";
		}
		
		var startTime = new Date(data1);
		var endTime = new Date(data2);
		var result = "";
		
		var date3=endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
		if(date3 < 0){
			date3=startTime.getTime()-endTime.getTime(); //时间差的毫秒数 
		}
		
		//计算出相差天数
		var days=Math.floor(date3/(24*3600*1000));
		
		result += days > 0 ?  days + "天" : "";  
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
		
		var startTime = new Date(data.arrTime==undefined?data.arrTime():data.arrTime);
		var endTime = new Date(data.dptTime==undefined?data.dptTime():data.dptTime);
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
	
	/**
	 * 计算时间差
	 * @param data
	 * @returns
	 */
	function GetDateDiff3(data) {
		if(data.dptTime == '-' 
				 || data.dptTime == null 
				 || data.arrTime == null
				 || data.arrTime == '-'){
			return "";
		}
		
		var startTime = new Date(data.arrTime());
		var endTime = new Date(data.dptTime());
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

