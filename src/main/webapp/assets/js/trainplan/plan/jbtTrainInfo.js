/**
 * 运行线Model
 */
var PageModel = /**
 * 
 */
function() {

	var _currentDate = moment().format("YYYY-MM-DD");//当前日期
	var _self = this;
	_self.timesDivBtnDisabled = ko.observable(false);//时刻表table操作按钮是否禁用
	_self.trainLines = ko.observableArray();//运行线列表绑定对象
	_self.searchModle = ko.observable(new SearchModle());		//页面查询对象
	_self.currentTrain = ko.observable(new createEmptyTrainLine());		//运行线详情绑定数据对象
	_self.trainLineTimesBak = ko.observableArray();//运行线时刻点单数据备份，用于运行线时刻表过滤事件，当列表插入、追加、删除时应同时操作该对象
	_self.trainLineTimes = ko.observableArray();//运行线时刻点单列表绑定对象
	_self.currentTrainLineTimesStn = ko.observable(newTrainLineTimesRow());//列车时刻表选中行记录  用于上下移动
	_self.highSpeedOptions = ko.observableArray([{code:"",text:""},{code:"F",text:"普速"},{code:"T",text:"高铁"},{code:"H",text:"混跑"}]),		//是否高线
	_self.highSpeed = ko.observable();
	_self.typeNameOptions = ko.observableArray();
	_self.trainlineTypes = ko.observableArray();// 列车类型
	_self.trainType = ko.observable();
	_self.trainId = ko.observable();
	_self.typeName = ko.observable();
	
	_self.tranlineSaveBtnDisabled = ko.computed(function () {
		if (typeof _self.currentTrain()!="object" || _self.currentTrain().trainNbr()=="") {
			return true;
		} else {
			return false;
		}
	}, this);

	_self.saveTrainLine = function() {
		$.ajax({
			url : "",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				trainline : ko.toJSON(_self.currentTrain().data)
			}),
			success : function(result) {
				
			},
			error : function() {
				alert("保存失败");
			},
			complete : function(){
				
			}
		});
	};
	
	
	/**
	 * 
	 * @param type	
	 * 			seconds:从index站点出发时间开始增加number秒；target:从index+1站点开始到达、出发时间增加number秒
	 * 			days:从index站点出发时间开始增加number天；target:从index+1站点开始到达、出发时间增加number天
	 * @param index _self.trainLineTimes集合中序号
	 * @param number	需要增加的秒数/天数
	 */
	_self.delayTime = function(type, index, number){
		var _newSourceTime = "";//新到达时间YYYY-MM-DD HH:mm:ss
		var _newTargetTime = "";//新的出发时间YYYY-MM-DD HH:mm:ss
		if("days" == type){
			$.each(_self.trainLineTimesBak(), function(i, obj){
				if (i > index) {
					//重新计算新的到达时间、到达运行天数
					if (obj.sourceTime()!="--") {
						_newSourceTime = moment(obj.sourceTimeAll()).add(type, number).format("YYYY-MM-DD HH:mm:ss");

						obj.sourceRunDays(moment(moment(_newSourceTime).format("YYYY-MM-DD")).diff(moment(obj.sourceTimeAll()).format("YYYY-MM-DD"), "days") + Number(obj.sourceRunDays()));
						obj.sourceTime(moment(_newSourceTime).format("HH:mm:ss"));
						obj.data.sourceTime = moment(_newSourceTime).format("HH:mm:ss");
						obj.data.sourceDay = Number(obj.sourceRunDays());
//						obj.data.sourceTime = _newSourceTime;
//						obj.data.sourceTimeSchedule.dates = obj.sourceRunDays();
//						obj.data.sourceTimeSchedule.hour = moment(_newSourceTime).format("HH");
//						obj.data.sourceTimeSchedule.minute = moment(_newSourceTime).format("mm");
//						obj.data.sourceTimeSchedule.second = moment(_newSourceTime).format("ss");
					}
					
					//重新计算新的到达时间、到达运行天数
					if (obj.targetTime()!="--") {
						_newTargetTime = moment(obj.targetTimeAll()).add(type, number).format("YYYY-MM-DD HH:mm:ss");

						obj.targetRunDays(moment(moment(_newTargetTime).format("YYYY-MM-DD")).diff(moment(obj.targetTimeAll()).format("YYYY-MM-DD"), "days") + Number(obj.targetRunDays()));
						obj.targetTime(moment(_newTargetTime).format("HH:mm:ss"));
						obj.data.targetTime = moment(_newTargetTime).format("HH:mm:ss");
						obj.data.targetDay = Number(obj.targetRunDays());
//						obj.data.targetTimeSchedule.dates = obj.targetRunDays();
//						obj.data.targetTimeSchedule.hour = moment(_newTargetTime).format("HH");
//						obj.data.targetTimeSchedule.minute = moment(_newTargetTime).format("mm");
//						obj.data.targetTimeSchedule.second = moment(_newTargetTime).format("ss");
					}
					
				}
			});
		} else if("seconds" == type){
			$.each(_self.trainLineTimesBak(), function(i, obj){
				if (i > index) {
					//重新计算新的到达时间、到达运行天数
					if (obj.sourceTime()!="--") {
						_newSourceTime = moment(obj.sourceTimeAll()).add(type, number).format("YYYY-MM-DD HH:mm:ss");

//						obj.sourceRunDays(moment(moment(_newSourceTime).format("YYYY-MM-DD")).diff(moment(obj.sourceTimeAll()).format("YYYY-MM-DD"), "days") + obj.sourceRunDays());
						obj.sourceTime(moment(_newSourceTime).format("HH:mm:ss"));
						obj.data.sourceTime = moment(_newSourceTime).format("HH:mm:ss");
//						obj.data.sourceTimeSchedule.dates = obj.sourceRunDays();
//						obj.data.sourceTimeSchedule.hour = moment(_newSourceTime).format("HH");
//						obj.data.sourceTimeSchedule.minute = moment(_newSourceTime).format("mm");
//						obj.data.sourceTimeSchedule.second = moment(_newSourceTime).format("ss");
					}
					
					//重新计算新的到达时间、到达运行天数
					if (obj.targetTime()!="--") {
						_newTargetTime = moment(obj.targetTimeAll()).add(type, number).format("YYYY-MM-DD HH:mm:ss");

//						obj.targetRunDays(moment(moment(_newTargetTime).format("YYYY-MM-DD")).diff(moment(obj.targetTimeAll()).format("YYYY-MM-DD"), "days") + obj.targetRunDays());
						obj.targetTime(moment(_newTargetTime).format("HH:mm:ss"));
						obj.data.targetTime = moment(_newTargetTime).format("HH:mm:ss");
//						obj.data.targetTimeSchedule.dates = obj.targetRunDays();
//						obj.data.targetTimeSchedule.hour = moment(_newTargetTime).format("HH");
//						obj.data.targetTimeSchedule.minute = moment(_newTargetTime).format("mm");
//						obj.data.targetTimeSchedule.second = moment(_newTargetTime).format("ss");
					}
					
				}
			});
		}
	};
	
	/**
	 * 更新站点作业信息
	 */
	_self.editJobs = function(row) {
		//1.清除数据
		_self.setTrainLineTimesCurrentRec(row);
		
		if(row.permissionDenied()) {
			$("#editJobsModal").prop("aria-hidden", "true");
			$("#editJobsModal").hide();
			return;//没有权限
		}
		
		_self.setJobCheckBoxVal();
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
		var jobsTypeSubstring = _self.currentTrainLineTimesStn().data.jobsValue.substring(1,3);
		_items.unshift(jobsTypeSubstring);
		_self.currentTrainLineTimesStn().data.jobs.items = _items;
		_self.currentTrainLineTimesStn().jobItems(_items);
	};
	
	
	/**
	 * 运行线时刻表过滤事件
	 * xd详点：所有经由点 jd简点：始发、终到、分界口、有停时的站	kyyy客运营业：始发、终到、有停时的站
	 */
	_self.timesFilterChange = function() {
		//1.清除列表显示数据
		_self.trainLineTimes.remove(function(item) {
			return true;
		});
		
		//2.重新生成列表显示数据
		var filterCode = typeof _self.searchModle().filterOption()!= "object"?"xd":_self.searchModle().filterOption().code;
		if ("xd"==filterCode) {
			_self.timesDivBtnDisabled(false);//时刻表table操作功能按钮可用
			//所有经由点
			$.each(_self.trainLineTimesBak(), function(i, obj){
				_self.trainLineTimes.push(obj);
			});
		} else if("jd"==filterCode){
			_self.timesDivBtnDisabled(true);//时刻表table操作功能按钮禁用
			//始发、终到、分界口、有停时的站
			$.each(_self.trainLineTimesBak(), function(i, obj){
				if (obj.sourceTime()=="--"
						||obj.targetTime()=="--"
						||obj.stepStr()!="") {
					_self.trainLineTimes.push(obj);
				}
			});
		} else if("kyyy"==filterCode){
			_self.timesDivBtnDisabled(true);//时刻表table操作功能按钮禁用
			//始发、终到、有停时的站
			$.each(_self.trainLineTimesBak(), function(i, obj){
				if (obj.sourceTime()=="--"
						||obj.targetTime()=="--"
						||obj.stepStr()!="") {
					_self.trainLineTimes.push(obj);
				}
			});
		}
		
		
		
		
		
	};
	
	
	/**
	 * 查询model
	 * 
	 * private
	 */
	function SearchModle(){
		var _self = this;
		
		_self.trainNbr = ko.observable("");		//车次号
		_self.charts = ko.observableArray(); //方案
		_self.chart = ko.observable(); 
		_self.filterOption = ko.observable("");		//列表过滤条件对象
		//xd详点：所有经由点   		jd简点：始发、终到、分界口、有停时的站	kyyy客运营业：始发、终到、有停时的站
		_self.loadChats = function(charts){   
			for ( var i = 0; i < charts.length; i++) {   
				_self.charts.push({"chartId": charts[i].schemeId, "name": charts[i].schemeName});  
			} 
		}; 
		_self.filterOptions = ko.observableArray([{code:"xd",text:"详点"},{code:"jd",text:"简点"},{code:"kyyy",text:"客运营业"}]);
		_self.kyyyOptions = ko.observableArray([{code:"n",text:""},{code:"y",text:"是"}]);//客运营业下拉框数据
//		_self.kyyyOptions = ko.observableArray([{code:"n",text:""},{code:"y",text:"是"},{code:"n",text:""}]);//客运营业下拉框数据
		
	};
	
	
	
	
	/**
	 * 创建运行线时刻列表行空对象
	 * 用于插入、追加行时
	 * @param index trainLineTimes(当前运行线时刻表table绑定对象数组长度)，若为null则为追加
	 * @returns {trainLineTimesRowModel}
	 */
	function newTrainLineTimesRow(index) {
		var _index = (typeof index=="undefined"||index==null)?-10:index;
		return new trainLineTimesRowModel({
			index:_index,
			name:"",
//			bureauShortName:user.bureauShortName,
			trackName:"",
			platForm:"",
			nodeId:"",
			jobs:"",
			nodeName:"",
			bureauId:"",
			sourceTrainNbr:"",
			sourceTimeSchedule: {
	            dates: "0",
	            hour: "00",
	            minute: "00",
	            second: "00",
	            text: "当天 00:00:00"
	        },
	        targetTrainNbr:"",
	        targetTimeSchedule: {
	            dates: "0",
	            hour: "00",
	            minute: "00",
	            second: "00",
	            text: "当天 00:00:00"
	        },
            jobs: {
                code: "",
                items: [],
                itemsText: ""
            }
		});
	};
	
	
	/**
	 * 定义列车运行线列表行数据对象
	 * private
	 */
	function trainLineRowModel(data) {
		var _self = this;
		_self.data = data;//用于其他操作
		//以下字段仅做table显示
		_self.id = ko.observable(data.planTrainId);
		_self.trainNbr = ko.observable(data.trainNbr);		//车次号
		_self.typeName = ko.observable(data.trainTypeName);		//类型
		_self.typeId = ko.observable(data.trainTypeId);		//类型id
		
		_self.highSpeedStr = ko.observable(data.highline_flag);		//true为运行在高铁线路，false为运行在既有线
		_self.sourceBureauShortName = ko.observable(data.startBureau);		//始发局
		_self.sourceNodeName = ko.observable(data.startStn);	//始发站
		_self.sourceTime = ko.observable(data.startTimeStr);		//始发时间
		_self.targetTime = ko.observable(data.endTimeStr);		//终到时间
		_self.sourceTimeActivity = ko.observable(data.sourceTimeActivity);		//始发时间
		_self.targetTimeActivity = ko.observable(data.targetTimeActivity);		//终到时间
		_self.sourceTimeAll = ko.observable(data.startTimeAll);		//始发时间
		_self.targetTimeAll = ko.observable(data.endTimeAll);		//终到时间
		_self.targetBureauShortName = ko.observable(data.endBureau);		//终到局
		_self.targetNodeName = ko.observable(data.endStn);	//终到站
		_self.runDays = ko.observable("");		//运行天数
		_self.lastTimeText = ko.observable(data.lastTimeText);		//历时
		_self.routeBureauShortNames = ko.observable(data.routingBureauShortName);		//途经局
		_self.executionText = ko.observable("");		//执行有效期
//		
//		if(typeof data.result.sourceTimeSchedule == "object" && data.result.sourceTimeSchedule!=null) {
//			var _time = data.result.sourceTimeSchedule.hour+":"+data.result.sourceTimeSchedule.minute + 
//			":"+data.result.sourceTimeSchedule.second;
//			_self.sourceTime(moment(moment().format("YYYY-MM-DD")+" "+_time).format("HH:mm:ss"));
//			_self.data.result.sourceTime = _self.sourceTime();
//		}
//		if(typeof data.result.targetTimeSchedule == "object" && data.result.targetTimeSchedule!=null) {
//			var _time = data.result.targetTimeSchedule.hour+":"+data.result.targetTimeSchedule.minute + 
//			":"+data.result.targetTimeSchedule.second;
//			_self.targetTime(moment(moment().format("YYYY-MM-DD")+" "+_time).format("HH:mm:ss"));
//			_self.data.result.targetTime = _self.targetTime();
//			_self.runDays(data.result.targetTimeSchedule.dates);
//		}
	};
	
	
	
	_self.init = function () {
		$("#executionStatrDate").datepicker();//有效期起始日期
		$("#executionEndDate").datepicker();//有效期截至日期
//		$("#executionStatrDate").blur(function(){
//	    	var str = $("#executionStatrDate").val().trim();
//	    	if(!Validate.isValidDateFormat(str)){      
//	            showErrorDialog("请输入有效日期");
//	            return ;
//	    	}
//		});
//		$("#executionEndDate").blur(function(){
//	    	var str = $("#executionEndDate").val().trim();
//	    	if(!Validate.isValidDateFormat(str)){      
//	            showErrorDialog("请输入有效日期");
//	            return ;
//	    	}
//		});
		var _height = $(window).height();
		$('#div_table_trainLineTimes').css({height:_height-250});//canvas图形div高度
		
		//获取类型下拉框数据
		$.ajax({//加载方案
			url : "jbtcx/querySchemes",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({}),
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) { 
						_self.searchModle().loadChats(result.data); 
					} 
				} else {
					showErrorDialog("获取方案失败");
				} 
			},
			error : function() {
				showErrorDialog("获取方案失败");
			},
			complete : function(){
//				commonJsScreenUnLock();
			}
	    });
		
		
		
		//加载列车信息
		
		
	};
	
	
	/**
	 * 创建空的运行线对象
	 */
	function createEmptyTrainLine () {
		return {
			id : ko.observable(""),		//id
			trainNbr : ko.observable(""),		//车次号
			typeId : ko.observable(""),		//类型
			typeName : ko.observable(""),		//类型
			highSpeedStr : ko.observable(""),		//是否高线
			sourceBureauShortName : ko.observable(""),		//始发局
			sourceNodeName : ko.observable(""),	//始发站
			sourceTime : ko.observable(""),		//始发时间
			sourceTimeAll : ko.observable(""),		//始发时间
			targetTime : ko.observable(""),		//终到时间
			targetTimeAll : ko.observable(""),		//终到时间
			targetBureauShortName : ko.observable(""),		//终到局
			targetNodeName : ko.observable(""),	//终到站
			runDays : ko.observable(""),		//运行天数
			lastTimeText : ko.observable(""),		//历时
			routeBureauShortNames : ko.observable(""),		//途经局
			executionText :ko.observable("")//执行有效期
		};
	};
	
	
	/**
	 * 页面查询按钮点击事件
	 * @public
	 * @功能：查询列车信息列表
	 */
	_self.queryTrainLineData = function() {
		var trainNbr = _self.searchModle().trainNbr();  
		var chart = _self.searchModle().chart(); 

		
		_self.trainLines.remove(function(item){
			return true;
		});
		if(chart == null){
			showErrorDialog("请选择方案");
			return;
		} 
		$.ajax({
			url : "jbtcx/queryTrainsForEdit",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				trainNbr:trainNbr,
				chartId : chart.chartId
			}),
			success : function(result) {
				
					$.each(result.data, function(i, obj){
						_self.trainLines.push(new trainLineRowModel(obj)); 
					});
				
			},
			error : function() {
				//TODO 请求失败，输出错误提示信息
			},
			complete : function(){
				//TODO ajax请求结束后必须要处理的逻辑
			}
	    });
	};
	
	
	/**
	 * 车次列表行点击事件
	 */
	_self.selectTrainLine = function(row) {
		
		getMTrainlineType(row.trainNbr(),row.typeId());//获取列车类型
		_self.currentTrain(row);
		
		
		
		//设置列车信息--类型下拉框选值
		var _typeNameArray = _self.typeNameOptions();
		for(var i=0;i<_typeNameArray.length;i++) {
			if(row.typeId() == _typeNameArray[i].typeId) {
				_self.trainType(_typeNameArray[i]);
			}
		}
		//设置列车信息--铁路线下拉框选值
		var _highSpeedArray = _self.highSpeedOptions();
		for(var i=0;i<_highSpeedArray.length;i++) {
			if(row.highSpeedStr() == _highSpeedArray[i].code) {
				_self.highSpeed(_highSpeedArray[i]);
			}
		}
		
		//根据列车id查询运行线时刻数据
		queryTrainLineTimesData(row.id());
	};
	/**
	 * 是否更改过值
	 */
	 function checkValueRepeat(){
		//console.log("进入checkValueRepeat");
		checkRepeat = false;
	};
	
	/**
	 * 计算运行总用时间
	 */
	function getLastTextTime(startTIme,endTime){
		
		var lastText = endTime - startTime;
		alert(lastText);
//		_newSourceTime = moment(obj.data.sourceTime).add(type, number).format("YYYY-MM-DD HH:mm:ss");
//		
//		obj.sourceRunDays(moment(moment(_newSourceTime).format("YYYY-MM-DD")).diff(moment(obj.data.sourceTime).format("YYYY-MM-DD"), "days") + obj.sourceRunDays());
//		obj.sourceTime(moment(_newSourceTime).format("HH:mm:ss"));
//		obj.data.sourceTime = _newSourceTime;
//		obj.data.sourceTimeSchedule.dates = obj.sourceRunDays();
//		obj.data.sourceTimeSchedule.hour = moment(_newSourceTime).format("HH");
//		obj.data.sourceTimeSchedule.minute = moment(_newSourceTime).format("mm");
//		obj.data.sourceTimeSchedule.second = moment(_newSourceTime).format("ss");
	}

	/**
	 * 加载列车类型.
	 * 
	 */
	function getMTrainlineType(trainNbr,typeId){
//		commonJsScreenLock();
	    $.ajax({
			url : "runPlanLk/getMTrainlineType",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json", 
			data :JSON.stringify({
				trainNbr : trainNbr,
				typeId : typeId
			}),
			success : function(result) {   
				if (result != null && result != "undefind" && result.code == "0") { 
					if(result.data!=null && result.data!=undefined){
						var types = new Array();
						var lx = result.str1;
//						console.log(lx);
						var fistObj = null;
							for (var i = 0; i < result.data.length; i++) {
								var obj = result.data[i];
								 if(obj.ID==lx){
									 fistObj = obj;
								 }else{
									 types.push(obj);
								 }
							}
							if(null != fistObj){
								types.unshift(fistObj,'');
							}else{
								types.unshift('');
							}
						$.each(types,function(n, types){  
							_self.typeNameOptions.push({"typeName": types.NAME, "typeId": types.ID});
//							_self.trainlineTypes.push({"name": types.NAME, "id": types.ID});
						});
					}
				} else {
					showErrorDialog("获取列车类型失败");
				} 
			},
			error : function() {
				showErrorDialog("获取列车类型失败");
			},
			complete : function(){ 
//				commonJsScreenUnLock(); 
			}
	    });
    	
    };
	
	/**
	 * 
	 * @public
	 * @功能：根据trainLineId查询运行线时刻
	 */
	function queryTrainLineTimesData(trainLineId) {
		//清除运行线时刻数据
		_self.trainLineTimesBak.remove(function(item) {
			return true;
		});
		
		$.ajax({
			url : "jbtcx/queryTrainTimesforJbtQuery",//此处地址需要更改
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				trainId:trainLineId
			}),
			success : function(result) {
//				if (typeof data =="object") {
					//1.
					$.each(result.data, function(i, obj){
						_self.trainLineTimesBak.push(new trainLineTimesRowModel(obj));
//						_self.trainLineTimes.push(new trainLineTimesRowModel(obj));
					});
					

					//2.设置过滤
					_self.timesFilterChange();

					
					//3.设置站名input框为自动补全
//					$('input[name="input_stnName"]').each(function(i, obj){
//				        //这个函数已经处理的分页，可以根据拼音码、名称等属性查询节点
//				        // node就是最终选中的节点，可以获得节点的所有属性，包括id、名称
//						$(this).nodePicker(function (node) {
//				            //这里写最终选中节点后的处理过程
//
							
/////////////////////////////////以下注释需要放开，
//							_self.trainLineTimesBak()[i].stnNameTemp(node.name);//站名界面显示用
//							_self.trainLineTimesBak()[i].data.name = node.name;//站名保存用
//							_self.trainLineTimesBak()[i].data.bureauId = node.bureauId;
//							_self.trainLineTimesBak()[i].data.bureauName = node.bureauName;
//							_self.trainLineTimesBak()[i].data.bureauShortName = node.bureauShortName;//所属路局
//							_self.trainLineTimesBak()[i].data.nodeId = node.nodeId;
//							_self.trainLineTimesBak()[i].data.nodeName = node.nodeName;
//							_self.trainLineTimesBak()[i].data.nodeTdcsId = node.nodeTdcsId;
//							_self.trainLineTimesBak()[i].data.nodeTdcsName = node.nodeTdcsName;
//							_self.trainLineTimesBak()[i].bureauShortName(node.bureauShortName);//所属路局
//							_self.trainLineTimes()[i].stnNameTemp(node.name);//站名界面显示用
//							_self.trainLineTimes()[i].data.name = node.name;//站名保存用
//							_self.trainLineTimes()[i].data.bureauId = node.bureauId;
//							_self.trainLineTimes()[i].data.bureauName = node.bureauName;
//							_self.trainLineTimes()[i].data.bureauShortName = node.bureauShortName;//所属路局
//							_self.trainLineTimes()[i].data.nodeId = node.nodeId;
//							_self.trainLineTimes()[i].data.nodeName = node.nodeName;
//							_self.trainLineTimes()[i].data.nodeTdcsId = node.nodeTdcsId;
//							_self.trainLineTimes()[i].data.nodeTdcsName = node.nodeTdcsName;
//							_self.trainLineTimes()[i].bureauShortName(node.bureauShortName);//所属路局
							
//				        });
//			        });
//				} else {
//					//TODO 输出错误提示信息
//				}
			},
			error : function() {
				//TODO 请求失败，输出错误提示信息
			},
			complete : function(){
				//TODO ajax请求结束后必须要处理的逻辑
				
				
			}
	    });
	};
	
	
	

	/**
	 * 计算时间差
	 * @param data
	 * @returns
	 */
	function GetDateDiff(sourceTime, sourceRunDays, targetTime,targetRunDays) {
//		if(data.targetTime == '--' || data.sourceTime == '--'){
//			return "";
//		}
		if(targetTime == '--' || sourceTime == '--'){
			return "";
		}
		
		//因基本图运行线接口返回数据中只有时间，所以需要构造日期
		var sourceRunDate = moment().add("day", sourceRunDays).format('YYYY-MM-DD');//到达日期
		var targetRunDate = moment().add("day", targetRunDays).format('YYYY-MM-DD');//出发日期
		
//		var startTime = new Date(sourceRunDate+" "+data.sourceTime);
//		var endTime = new Date(targetRunDate+" "+data.targetTime);
		var startTime = new Date(sourceRunDate+" "+sourceTime);
		var endTime = new Date(targetRunDate+" "+targetTime);
		
//		var startTime = new Date(data.sourceTime);
//		var endTime = new Date(data.targetTime);
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
	 * 时刻表列表行对象封装
	 * @param data
	 */
	function trainLineTimesRowModel(data) {
		var self = this;
		var _regexStr = "(^([0-1]\\d|2[0-3])\:([0-5]\\d)\:([0-5]\\d)$|^([0-1]\\d|2[0-3])\:([0-5]\\d)$)";//23:59:59   00:59
		var _regex = new RegExp(_regexStr);
		
		
		self.data = data;
		self.trainlineTempId = ko.observable(data.trainlineTempId);
		self.timeInputColor = ko.observable("");
		self.index = ko.observable(data.index + 1);
		self.hiddenIndex = ko.observable(data.rn);
		self.trainStnId = ko.observable(data.nodeId);
		self.stnSort = ko.observable(data.stnSort);
		self.stnName = ko.observable(data.stnName);
		self.nodeId = ko.observable(data.nodeId);
		self.nodeName = ko.observable(data.nodeName);
		self.bureauId = ko.observable(data.bureauId);
		self.bureauShortName = ko.observable(data.bureauShortName);
		//站点所属局与当前用户所属局是否一致，
		self.permissionDenied = ko.computed(function () {
//			if(user.bureauShortName=="" || user.bureauShortName==self.bureauShortName()){
//				return false;//拥有此数据操作权限
//			} else {
//				return true;//没有此数据操作权限
//			}
			return false;
		}, this);
		
		//return true:可用/false:不可用
		self.btnEnable = ko.computed(function () {
			//timesDivBtnDisabled 时刻表table操作功能按钮false:可用/true：禁用
			//permissionDenied 权限 false拥有此数据操作权限/true没有此数据操作权限
			if(!_self.timesDivBtnDisabled() && !self.permissionDenied()) {
				return true;
			} else {
				return false;
			}
		}, this);
		//return false:可编辑/true:只读
		self.inputReadOnly = ko.computed(function () {
//			return !self.btnEnable();
			return false;
		}, this);
		
		self.stnBureauFull = ko.observable(data.stnBureauFull);

		self.sourceTrainNbr = ko.observable(data.arrTrainNbr);		//到达车次
		self.sourceTimeAll = ko.observable(data.arrTimeAll);
		self.sourceTime = ko.observable(data.stationFlag=='SFZ'?'--':data.arrTime);
		self.data.sourceTime = self.sourceTime==null?self.targetTime:self.sourceTime;
		self.sourceRunDays = ko.observable(data.arrRunDays);
//		if(typeof data.sourceTimeSchedule == "object" && data.sourceTimeSchedule!=null) {
//			self.sourceRunDays(data.sourceTimeSchedule.dates);//到达运行天数
			
			//时分秒
//			var _time = data.sourceTimeSchedule.hour+":"+data.sourceTimeSchedule.minute + 
//			":"+data.sourceTimeSchedule.second;
			
//			self.data.sourceTime = moment(_currentDate+" "+_time)
//									.add("day", data.sourceTimeSchedule.dates).format("YYYY-MM-DD HH:mm:ss");
//			
//			self.sourceTime(moment(self.data.sourceTime).format("HH:mm:ss"));
//		}

		self.targetTrainNbr = ko.observable(data.dptTrainNbr);		//出发车次
		self.targetTime = ko.observable(data.stationFlag=='ZDZ'?"--":data.dptTime);		//出发时间
		self.targetTimeAll = ko.observable(data.dptTimeAll);		//出发时间
		self.data.targetTime = self.targetTime==null?self.sourceTime:self.targetTime;
		self.targetRunDays = ko.observable(data.targetDay);
//		if(typeof data.targetTimeSchedule == "object" && data.targetTimeSchedule!=null) {
//			self.targetRunDays(data.targetTimeSchedule.dates);
//			var _time = data.targetTimeSchedule.hour+":"+data.targetTimeSchedule.minute + 
//			":"+data.targetTimeSchedule.second;
//
//			self.data.targetTime = moment(_currentDate+" "+_time)
//									.add("day", data.targetTimeSchedule.dates).format("YYYY-MM-DD HH:mm:ss");
//			self.targetTime(moment(self.data.targetTime).format("HH:mm:ss"));
//		}
		
		self.trackName = ko.observable(data.trackName);//股道
		self.platForm = ko.observable(data.platForm);//站台
//		self.jobItems = ko.observableArray(typeof data.jobs=="object"?data.jobs.items:[]);
		var jobsText = data.jobsValue==undefined?'':data.jobsValue;
		jobsText = jobsText.replace('<客运>','');
		jobsText = jobsText.substring(1,jobsText.length-1);
//		jobsText = jobsText.split("><").join(",");
		
		self.jobItems = ko.observableArray(typeof data.jobsValue=="string"?jobsText.split("><"):[]);
		self.jobItemsText = ko.computed(function () {
				return self.jobItems().join(",");
		}, this);
//		self.jobs = ko.observable(data.jobs);
//		self.jobItems = ko.observableArray(typeof data.jobs=="object"?data.jobs.split(","):[]);
//		self.jobItemsText = ko.computed(function () {
//			return self.jobItems().join(",");
//		}, this);
		
		self.stepStr = ko.observable(GetDateDiff(self.data));
		self.stepStr = ko.computed(function () {
			return GetDateDiff(self.sourceTime(), self.sourceRunDays(),self.targetTime(),self.targetRunDays());
		});
		self.isChangeValue = ko.observable(0);
		self.stnNameTemp = ko.observable(data.stnName);
		self.kyyy = ko.observable();//客运营业
		
		//客运营业下拉框选择值
		self.kyyy = ko.computed(function () {
			var _val = "";
			if(self.stepStr() !="" || self.sourceTime()=="--" || self.targetTime()=="--") {
				_val = "y";
			} else {
				_val = "n";
			}
			var jobsValue = data.jobsValue==undefined?"":data.jobsValue;
			if(jobsValue.indexOf("客运")>0){
				_val = "y";
			}
			
			var _array = _self.searchModle().kyyyOptions();
			for(var i=0;i<_array.length;i++) {
				if(_val == _array[i].code) {
					return _array[i];
				}
			}
			
		}, this);
		
		
		
		self.sourceTimeOnblur = function(n, event){
			var _sourceTime = self.sourceTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
			var _targetTime = self.targetTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
			if (_sourceTime != "" && _sourceTime!="--") {
				if (_sourceTime.indexOf(":") < 0) {
					if(_sourceTime.length == 4){//1259格式化为12:59:00
						_sourceTime = _sourceTime.substring(0, 2) +":"+_sourceTime.substring(2, 4)+":00";
					} else if(_sourceTime.length == 6){//125910格式化为12:59:10
						_sourceTime = _sourceTime.substring(0, 2) +":"+_sourceTime.substring(2, 4)+":"+_sourceTime.substring(4, 6);
					}
				} else if (_sourceTime.split(":").length == 2) {//12:59格式化为12:59:00
					_sourceTime = _sourceTime+":00";
				}
				self.sourceTime(_sourceTime);
				if(!_regex.test(_targetTime) && _targetTime!="--"){
					self.targetTime(_sourceTime);
				}
				
				////////////只计算时间差（不含日期）
				var _oldSourceTime = moment(_currentDate+" "+moment(self.sourceTimeAll()).format("HH:mm:ss"));
				var _newSourceTime = moment(_currentDate+" "+self.sourceTime());
				var _diffMinute = _newSourceTime.diff(_oldSourceTime, "second", true);//秒数差
				var _tipsText = "";
				if (_diffMinute >0 ) {
					//当前站点到达时间后延
					_tipsText = "后续所有站点是否顺延"+_diffMinute+"秒?";
				} else if (_diffMinute <0 ) {
					//当前站点到达时间提前
					_tipsText = "后续所有站点是否提前"+(-_diffMinute)+"秒?";
				}
				if (typeof _diffMinute=="number" && _diffMinute!=0) {
					//当前站点到达时间增加_diffMinute秒
//					self.sourceRunDays(moment(moment(self.data.sourceTime).add("seconds", _diffMinute).format("YYYY-MM-DD"))
//							.diff(moment(self.data.sourceTime).format("YYYY-MM-DD"), "days") + self.sourceRunDays());
					self.data.sourceTime = moment(self.sourceTimeAll()).add("seconds", _diffMinute).format("YYYY-MM-DD HH:mm:ss");					
//					self.data.sourceTimeSchedule.dates = self.sourceRunDays();
//					self.data.sourceTimeSchedule.hour = moment(self.data.sourceTime).format("HH");
//					self.data.sourceTimeSchedule.minute = moment(self.data.sourceTime).format("mm");
//					self.data.sourceTimeSchedule.second = moment(self.data.sourceTime).format("ss");
					
					
					if(window.confirm(_tipsText)){
						//当前站点出发时间增加_diffMinute秒
//						self.targetRunDays(moment(moment(self.data.targetTime).add("seconds", _diffMinute).format("YYYY-MM-DD"))
//								.diff(moment(self.data.targetTime).format("YYYY-MM-DD"), "days") + self.targetRunDays());
						self.data.targetTime = moment(self.targetTimeAll()).add("seconds", _diffMinute).format("YYYY-MM-DD HH:mm:ss");					
//						self.data.targetTimeSchedule.dates = self.targetRunDays();
//						self.data.targetTimeSchedule.hour = moment(self.data.targetTime).format("HH");
//						self.data.targetTimeSchedule.minute = moment(self.data.targetTime).format("mm");
//						self.data.targetTimeSchedule.second = moment(self.data.targetTime).format("ss");
//						self.targetTime(moment(self.data.targetTime).format("HH:mm:ss"));
						
						_self.delayTime("seconds", self.hiddenIndex(), _diffMinute);
					}
		        }
			}
		};
		
		self.targetTimeOnblur = function(n, event){
			var _sourceTime = self.sourceTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
			var _targetTime = self.targetTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
			if (_targetTime != "" && _targetTime!="--") {
				if (_targetTime.indexOf(":") < 0) {
					if(_targetTime.length == 4){//1259格式化为12:59:00
						_targetTime = _targetTime.substring(0, 2) +":"+_targetTime.substring(2, 4)+":00";
					} else if(_targetTime.length == 6){//125910格式化为12:59:10
						_targetTime = _targetTime.substring(0, 2) +":"+_targetTime.substring(2, 4)+":"+_targetTime.substring(4, 6);
					}
				} else if (_targetTime.split(":").length == 2) {//12:59格式化为12:59:00
					_targetTime = _targetTime+":00";
				}
				self.targetTime(_targetTime);
				if(!_regex.test(_sourceTime) && _sourceTime!="--"){
					self.sourceTime(_targetTime);
				}
				
				

				////////////只计算时间差（不含日期）
				var _oldTargetTime = moment(_currentDate+" "+moment(self.targetTimeAll()).format("HH:mm:ss"));
				var _newTargetTime = moment(_currentDate+" "+self.targetTime());
				var _diffMinute = _newTargetTime.diff(_oldTargetTime, "second", true);//秒数差
				var _tipsText = "";
				if (_diffMinute >0 ) {
					//当前站点到达时间后延
					_tipsText = "后续所有站点是否顺延"+_diffMinute+"秒?";
				} else if (_diffMinute <0 ) {
					//当前站点到达时间提前
					_tipsText = "后续所有站点是否提前"+(-_diffMinute)+"秒?";
				}
				if (typeof _diffMinute=="number" && _diffMinute!=0) {
					
					//当前站点出发时间增加_diffMinute秒
//					self.targetRunDays(moment(moment(self.data.targetTime).add("seconds", _diffMinute).format("YYYY-MM-DD"))
//							.diff(moment(self.data.targetTime).format("YYYY-MM-DD"), "days") + self.targetRunDays());
					
					self.data.targetTime = moment(self.targetTimeAll()).add("seconds", _diffMinute).format("YYYY-MM-DD HH:mm:ss");					
//					self.data.targetTimeSchedule.dates = self.targetRunDays();
//					self.data.targetTimeSchedule.hour = moment(self.data.targetTime).format("HH");
//					self.data.targetTimeSchedule.minute = moment(self.data.targetTime).format("mm");
//					self.data.targetTimeSchedule.second = moment(self.data.targetTime).format("ss");
					
					if(window.confirm(_tipsText)) {
						_self.delayTime("seconds", self.hiddenIndex(), _diffMinute);
					}
		        }
				
			}
		};
		
		
		self.sourceRunDaysOnblur = function(n, event){
			var _sourceTime = self.sourceTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
			if (_sourceTime != "" && _sourceTime!="--") {
				////////////只计算天数差
				var _oldSourceRunDays = self.data.sourceDay;
				var _newSourceRunDays = self.sourceRunDays();
				var _diff = _newSourceRunDays - _oldSourceRunDays;//天数差
				var _tipsText = "";
				if (_diff >0 ) {
					//当前站点到达时间后延
					_tipsText = "后续所有站点是否顺延"+_diff+"天?";
				} else if (_diff <0 ) {
					//当前站点到达时间提前
					_tipsText = "后续所有站点是否提前"+(-_diff)+"天?";
				}
				
				if (typeof _diff=="number" && _diff!=0) {
					//当前站点到达时间增加_diff天
					self.data.sourceTime = moment(self.data.sourceTime).add("days", _diff).format("YYYY-MM-DD HH:mm:ss");
					self.data.sourceDay = self.sourceRunDays();
					
					if(window.confirm(_tipsText)) {
						//当前站点出发时间增加_diff天
						self.data.targetTime = moment(self.data.targetTime).add("days", _diff).format("YYYY-MM-DD HH:mm:ss");
						self.data.targetDay = Number(self.targetRunDays()) + _diff;
						self.targetRunDays(Number(self.data.targetDay));
						_self.delayTime("days", self.hiddenIndex(), _diff);
					}
		        }
				
		        
			}
		};
		
		self.targetRunDaysOnblur = function(n, event){
			var _targetTime = self.targetTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
			if (_targetTime != "" && _targetTime!="--") {
				////////////只计算天数差
				var _oldTargetRunDays = self.data.targetDay;
				var _newTargetRunDays = self.targetRunDays();
				var _diff = _newTargetRunDays - _oldTargetRunDays;//天数差
				var _tipsText = "";
				if (_diff >0 ) {
					//当前站点到达时间后延
					_tipsText = "后续所有站点是否顺延"+_diff+"天?";
				} else if (_diff <0 ) {
					//当前站点到达时间提前
					_tipsText = "后续所有站点是否提前"+(-_diff)+"天?";
				}
				if (typeof _diff=="number" && _diff!=0) {
					//当前站点出发时间增加_diff天
					self.data.targetTime = moment(self.data.targetTime).add("days", _diff).format("YYYY-MM-DD HH:mm:ss");
					self.data.targetDay = Number(self.targetRunDays());
					
					if(window.confirm(_tipsText)) {
						_self.delayTime("days", self.hiddenIndex(), _diff);
					}
		        }
				
			}
		};
		
		

		self.sourceTrainNbrOnblur = function(n, event){
			self.targetTrainNbr(self.sourceTrainNbr());
			$.each(_self.trainLineTimes(), function(i, obj){
				if (i > self.hiddenIndex()) {
					obj.sourceTrainNbr(self.sourceTrainNbr());
					obj.targetTrainNbr(self.sourceTrainNbr());
				}
			});
		};
		

		self.targetTrainNbrOnblur = function(n, event){
			$.each(_self.trainLineTimes(), function(i, obj){
				if (i > self.hiddenIndex()) {
					obj.sourceTrainNbr(self.targetTrainNbr());
					obj.targetTrainNbr(self.targetTrainNbr());
				}
			});
		};
		
		self.stnNameTemp = ko.computed({
	        read: function () {
	            return self.stnName();
	        },
	        write: function (value) {
	        	self.bureauShortName("");//清除当前路局值
	        	self.stnName(value);//清除当前路局值
	        	//根据站名查询所属路局信息
				$.ajax({
					url : basePath+"/runPlanLk/getBaseStationInfo",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({
						stnName : value//车站名称
					}),
					success : function(result) {
						if (result != null && result != "undefind" && result.code == "0") {
							if (result.data !=null) {
								for (var i = 0; i < result.data.length; i++) {
									if (value == result.data[i].STNNAME) {
										self.bureauShortName(result.data[i].STNBUREAUSHORTNAME);//路局简称
										break;
									}
								}
							}
						} else {
							showErrorDialog("获取车站信息失败");
						}
					},
					error : function() {
						showErrorDialog("获取车站信息失败");
					},
					complete : function(){
//						commonJsScreenUnLock(1);
					}
			    });
	        },
	        owner: this
	    });

		
	};
	///////////////////////////////////////时刻表列表行对象封装end
	

	/**
	 * 运行线时刻批量保存按钮点击事件
	 */
	_self.saveTrainLineTimes = function() {
		if (typeof _self.currentTrain()!="object" || _self.currentTrain().id()=="") {
			return;
		}
		//列车信息
//		_self.currentTrain().typeId = _self.trainType().typeId;
//		_self.currentTrain().typeName = _self.trainType().typeName;
		_self.currentTrain().highSpeedStr = _self.highSpeed().code;
		
		
		
		var _trainLineTimes = [];
		var _isExecuteSave = true;//是否执行保存
		$.each(_self.trainLineTimesBak(), function(i, obj){
			obj.timeInputColor("");//清除错误状态
			obj.data.sourceParentName = obj.sourceTrainNbr();
			obj.data.targetParentName = obj.targetTrainNbr();
			obj.data.name = obj.stnNameTemp();
			obj.data.trackName = obj.trackName();
			obj.data.platForm = obj.platForm();
			if (i==0 && obj.sourceTime()=="--") {
				obj.data.sourceDay = null;
			} else if (i==(_self.trainLineTimes().length-1) && obj.targetTime()=="--") {
				obj.data.targetDay = null;
			}
			obj.data.sourceTime = obj.sourceTime();
			obj.data.targetTime = obj.targetTime();
			obj.data.sourceRunDays = obj.sourceRunDays();
			obj.data.targetRunDays = obj.targetRunDays();
//			obj.data.sourceRunDays = obj.sourceDay();
//			obj.data.targetRunDays = obj.targetDay();
			
			var jobs = obj.data.jobs;
			var jobItemsText = obj.jobItemsText();
			var kyyy = obj.kyyy();
			console.log("1135H::" + kyyy);
			console.log("1135H::"+kyyy.code);
			console.log("1136::"+obj.data.jobs);
			if(jobItemsText!='' && jobItemsText!=null){
//					obj.data.jobs = obj.data.jobs+"<"+jobItems+">";
				var jobsValue = '';
				for(var i=0;i<obj.jobItems().length;i++){
					jobsValue = jobsValue + "<" + obj.jobItems()[i] + ">";
				}
				obj.data.jobs = jobsValue;
			}
			if(kyyy.code=='y'){
				if((obj.data.jobs).indexOf("客运")<0){
					obj.data.jobs = obj.data.jobs+"<客运>";
				}
			}else{
				if((obj.data.jobs).indexOf("客运")>0){
					obj.data.jobs.replace('<客运>','');
				}
			}
			console.log("1154::"+obj.data.jobs);
			_trainLineTimes.push(obj.data);
			
			if (i < _self.trainLineTimes().length-1) {
				//当前站点出发时间必须小于下一站点到达时间
				if(moment(_self.trainLineTimes()[i+1].data.sourceTime).diff(moment(obj.data.targetTime), "second") <= 0) {
					obj.timeInputColor("red");
					_isExecuteSave = false;
				}
				if(i>0){
					//当前站点出发时间必须》=到达时间
					if(moment(obj.data.targetTime).diff(moment(obj.data.sourceTime), "second") < 0
							||moment(_self.trainLineTimes()[i+1].data.sourceTime).diff(moment(obj.data.targetTime), "second") <= 0) {
						obj.timeInputColor("red");
						_isExecuteSave = false;
					}
				}
			}
		});

		if (_isExecuteSave) {
			commonJsScreenLock();
			$.ajax({
				url : "jbtcx/editJbtPlanLineTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					trainline : ko.toJSON(_self.currentTrain()),
					items : ko.toJSON(_trainLineTimes)
				}),
				success : function(result) {
					if (result.code == "0") {
						showSuccessDialog("保存成功");
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
		}
	};
	
	
	

	
	/**
	 * 上移
	 */
	_self.up = function() {
		if(typeof _self.currentTrainLineTimesStn()!="object" || _self.currentTrainLineTimesStn().hiddenIndex()<0) {
			alert("请选择时刻表中需要调整顺序的记录");
			return;
		}
		if(_self.currentTrainLineTimesStn().permissionDenied()) {
			alert("不能修改其他路局站点数据");
			return;
		}
		if(_self.currentTrainLineTimesStn().hiddenIndex() == 0) {
			alert("当前记录顺序号已经为最小，不能执行上移操作");
			return;
		}
		
		
		//设置序号
		var _arrayList = _self.trainLineTimes();
		for(var i=0;i<_arrayList.length;i++) {
			if(i == (_self.currentTrainLineTimesStn().hiddenIndex())) {
				_arrayList[i].hiddenIndex(i-1);
				_arrayList[i-1].hiddenIndex(i);
				_arrayList[i].data.index = _arrayList[i].hiddenIndex();
				_arrayList[i-1].data.index = _arrayList[i-1].hiddenIndex();
				_self.trainLineTimes.splice(i - 1, 2, _arrayList[i], _arrayList[i-1]);
				break;
			}
		}
	};
	
	
	
	/**
	 * 下移
	 */
	_self.down = function() {
		if(typeof _self.currentTrainLineTimesStn()!="object" || _self.currentTrainLineTimesStn().hiddenIndex()<0) {
			alert("请选择时刻表中需要调整顺序的记录");
			return;
		}
		if(_self.currentTrainLineTimesStn().permissionDenied()) {
			alert("不能修改其他路局站点数据");
			return;
		}

		var _maxLen = _self.trainLineTimes().length -1;
		if(_self.currentTrainLineTimesStn().hiddenIndex() == _maxLen) {
			alert("当前记录顺序号已经为最大，不能执行下移操作");
			return;
		}
		
		//设置序号
		var _arrayList = _self.trainLineTimes();
		for(var i=0;i<_arrayList.length;i++) {
			if(i == (_self.currentTrainLineTimesStn().hiddenIndex())) {
				
				_arrayList[i].hiddenIndex(i+1);
				_arrayList[i+1].hiddenIndex(i);
				_arrayList[i].data.index = _arrayList[i].hiddenIndex();
				_arrayList[i+1].data.index = _arrayList[i+1].hiddenIndex();
				_self.trainLineTimes.splice(i , 2, _arrayList[i+1], _arrayList[i]);
				break;
			}
		}
	};
	
	

	/**
	 * 插入行
	 * 追加到当前选中行之前
	 */
	_self.insertTrainLineStnRow = function() {
		var _maxRowLen = _self.trainLineTimes().length;//追加记录前集合大小

		//1.当_maxRowLen==0时，直接push
		if (_maxRowLen == 0) {
			_self.trainLineTimes.push(newTrainLineTimesRow(0));
			return;
		}
		
		if(typeof _self.currentTrainLineTimesStn()!="object"|| _self.currentTrainLineTimesStn().hiddenIndex()<0) {
			alert("请选择时刻表中需要插入记录的位置");
			return;
		}
//		if(_self.currentTrainLineTimesStn().permissionDenied()) {
//			alert("不能修改其他路局站点数据");
//			return;
//		}


		//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
		_self.trainLineTimes.splice(_self.currentTrainLineTimesStn().hiddenIndex(), 0, newTrainLineTimesRow(_self.currentTrainLineTimesStn().hiddenIndex()));

		//2.2.设置序号
		for(var i=0;i<_self.trainLineTimes().length;i++) {
			if(i > (_self.currentTrainLineTimesStn().hiddenIndex())) {//从新插入记录位置开始hiddenIndex  + 1
				_self.trainLineTimes()[i].hiddenIndex(_self.trainLineTimes()[i].hiddenIndex()+1);//当前行位置后的所有记录hiddenIndex加1
				_self.trainLineTimes()[i].data.index = _self.trainLineTimes()[i].hiddenIndex();
			}
		}
		
	};
	
	

	/**
	 * 追加行
	 * 追加到当前选中行之后
	 */
	_self.addTrainLineStnRow = function() {
		var _maxRowLen = _self.trainLineTimes().length;//追加记录前集合大小
		
		//1.当_maxRowLen==0时，直接push
		if (_maxRowLen == 0 || typeof _self.currentTrainLineTimesStn()!="object"|| _self.currentTrainLineTimesStn().hiddenIndex()<0) {
			_self.trainLineTimes.push(newTrainLineTimesRow(_self.trainLineTimes().length));
			return;
		}
		

		//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
		_self.trainLineTimes.splice(_self.currentTrainLineTimesStn().hiddenIndex()+1, 0, newTrainLineTimesRow(_self.trainLineTimes().length));

		//2.2.设置序号
		if(_self.currentTrainLineTimesStn().hiddenIndex() != _maxRowLen-1) {//当前选中行不为增加前集合中最后一行   则移除后集合需要重新排序
			for(var i=0;i<_self.trainLineTimes().length;i++) {
				if(i > (_self.currentTrainLineTimesStn().hiddenIndex()+1)) {//从新插入记录位置开始hiddenIndex  + 1
					_self.trainLineTimes()[i].hiddenIndex(_self.trainLineTimes()[i].hiddenIndex()+1);//当前行位置后的所有记录hiddenIndex加1
					_self.trainLineTimes()[i].data.index = _self.trainLineTimes()[i].hiddenIndex();
				}
			}
		}
	};
	
	

	/**
	 * 删除行
	 */
	_self.deleteTrainLineStnRow = function() {
		if(typeof _self.currentTrainLineTimesStn()!="object" || _self.currentTrainLineTimesStn().hiddenIndex()<0) {
			alert("请选择时刻表中需要移除的记录");
			return;
		}
		if(_self.currentTrainLineTimesStn().permissionDenied()) {
			alert("不能删除其他路局站点数据");
			return;
		}
		
		var _maxRowLen = _self.trainLineTimes().length;
		
		//从集合中移除
		_self.trainLineTimes.remove(_self.currentTrainLineTimesStn());
		
		if(_self.currentTrainLineTimesStn().hiddenIndex() != _maxRowLen-1) {//移除行不为移除前集合中最后一行   则移除后集合需要重新排序
			//重新设置hiddenIndex
			$.each(_self.trainLineTimes(),function(n, obj){
				obj.hiddenIndex(n);
				obj.data.index = obj.hiddenIndex();
			});
		}
		
		//清除当前选中项值
		_self.currentTrainLineTimesStn(newTrainLineTimesRow());//清除时刻列表当前选中值
	};
	
	
	

	/**
	 * 时刻列表行点击事件
	 */
	_self.setTrainLineTimesCurrentRec = function(row) {
		_self.currentTrainLineTimesStn(row);
	};
	
	
	
	
	
	
	
	
	
};





$(function() { 
	//页面元素绑定
	var pageModel = new PageModel();
	ko.applyBindings(pageModel);
	
	pageModel.init();
});