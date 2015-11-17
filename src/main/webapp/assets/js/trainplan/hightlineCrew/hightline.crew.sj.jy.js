var pageModel = null;
$(function() {
	heightAuto();
	//boxScroll();
	pageModel = new PageModel();
	ko.applyBindings(pageModel);
	pageModel.initPage();

});
var gloabBureaus = [];
	
	/**
	 * 查询model
	 * 
	 * private
	 */
	function SearchModle(){
		
		var _self = this;
		
		/**
		 * 查询条件
		 */
		_self.runDate =  ko.observable();		//日期
		_self.trainNbr = ko.observable();		//车次号
		_self.xwyName = ko.observable();		//乘务员姓名
		
		_self.bureau = ko.observable();
		_self.bureaus = ko.observableArray();
		
		_self.loadBureau = function(bureaus){   
			for ( var i = 0; i < bureaus.length; i++) {  
				_self.bureaus.push(new BureausRow(bureaus[i])); 
			} 
		}; 
		_self.crewGroup =  ko.observable();
		_self.crewGroups =  ko.observableArray();
		_self.loadCrewGroup = function(crewGroups){   
			for ( var i = 0; i < crewGroups.length; i++) {
				var crewG = crewGroups[i].crewGroup;
				if(crewGroups[i].crewType=='11'||crewGroups[i].crewType=='12'){
					if(crewG!=null && crewG!=undefined){
						if(_self.crewGroups().length == 0){
							_self.crewGroups.push(new CrewGroupRow(crewGroups[i]));
						}else{
							var hasBoolean = true;
							for( var j = 0; j < _self.crewGroups().length;j++){
								if(_self.crewGroups()[j].crewGroupName==new CrewGroupRow(crewGroups[i]).crewGroupName){
									hasBoolean = false;
								}
							}
							if(hasBoolean ==true){
								_self.crewGroups.push(new CrewGroupRow(crewGroups[i]));
							}
						}
					}
				}
			} 
		}; 
		
		
		
		_self.initData = function() {
			_self.runDate(currdate());
		};
		
		//currentIndex 
		function currdate(){
			var d = new Date();
			var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
			var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
			var days = d.getDate(); 
			month = ("" + month).length == 1 ? "0" + month : month;
			days = ("" + days).length == 1 ? "0" + days : days;
			return year+"-"+month+"-"+days;
		};
		
	};
	
	function BureausRow(data) {
		var self = this;  
		self.shortName = data.ljjc;   
		self.code = data.ljpym;   
		//方案ID 
	} 
	
	function CrewGroupRow(data) {
		var self = this;  
		self.crewGroupName = data.crewGroup;   
		self.crewHighlineId = data.crewHighlineId;   
	} 
	
	
	/**
	 * 乘务计划model用于新增、修改
	 */
	function HighlineCrewModel() {
		var _self = this;
		_self.crewHighlineId = ko.observable();
		_self.crewDate = ko.observable();
		_self.crewBureau = ko.observable();
		_self.crewType = ko.observable();
		_self.crewCross = ko.observable();
		_self.crewGroup = ko.observable();
		_self.throughLine = ko.observable();
		_self.name1 = ko.observable();
		_self.tel1 = ko.observable();
		_self.identity1 = ko.observable();
		_self.name2 = ko.observable();
		_self.tel2 = ko.observable();
		_self.identity2 = ko.observable();
		_self.note = ko.observable();
		_self.recordPeople = ko.observable();
		_self.recordPeopleOrg = ko.observable();
		_self.recordTime = ko.observable();
		_self.submitType = ko.observable();
		_self.isSelect = ko.observable(0);
		_self.update = function (obj) {
			if (obj == null) {
				_self.crewHighlineId("");
				_self.crewDate("");
				_self.crewBureau("");
				_self.crewType("");
				_self.crewCross("");
				_self.crewGroup("");
				_self.throughLine("");
				_self.name1("");
				_self.tel1("");
				_self.identity1("");
				_self.name2("");
				_self.tel2("");
				_self.identity2("");
				_self.note("");
				_self.recordPeople("");
				_self.recordPeopleOrg("");
				_self.recordTime("");
				_self.submitType("");
			} else {
				_self.crewHighlineId(obj.crewHighlineId);
				_self.crewDate(obj.crewDate);
				_self.crewBureau(obj.crewBureau);
				_self.crewType(obj.crewType);
				_self.crewCross(obj.crewCross);
				_self.crewGroup(obj.crewGroup);
				_self.throughLine(obj.throughLine);
				_self.name1(obj.name1);
				_self.tel1(obj.tel1);
				_self.identity1(obj.identity1);
				_self.name2(obj.name2);
				_self.tel2(obj.tel2);
				_self.identity2(obj.identity2);
				_self.note(obj.note);
				_self.recordPeople(obj.recordPeople);
				_self.recordPeopleOrg(obj.recordPeopleOrg);
				_self.recordTime(obj.recordTime);
				_self.submitType(obj.submitType);
			}
		};
	};
	
	
	
	/**
	 * 开行计划列表行数据model
	 * @param palnTrainObj
	 */
	function PlanTrainRowModel(palnTrainObj) {
		var _self = this;
		_self.planTrainId = ko.observable(palnTrainObj.planTrainId);
		_self.trainNbr = ko.observable(palnTrainObj.trainNbr);
		_self.startStn = ko.observable(palnTrainObj.startStn);
		_self.startTimeStr = ko.observable(moment(palnTrainObj.startTimeStr).format("MMDD HH:mm"));
		_self.endStn = ko.observable(palnTrainObj.endStn);
		_self.endTimeStr = ko.observable(moment(palnTrainObj.endTimeStr).format("MMDD HH:mm"));
		_self.isMatch = ko.observable(palnTrainObj.isMatch);	//是否已上报车长乘务计划	1：真 0：假
		_self.times = ko.observableArray();  
		_self.simpleTimes = ko.observableArray(); 
		_self.loadTimes = function(times){
			$.each(times, function(i, n){ 
				var timeRow = new TrainTimeRow(n);
				_self.times.push(timeRow);
				if(n.stationFlag != 'BTZ'){
				_self.simpleTimes.push(timeRow);
				}
			});
		};
		_self.passBureau = ko.observable(palnTrainObj.passBureau);//途经局
		_self.tokenVehBureau = ko.observable(palnTrainObj.tokenVehBureau); //担当局
		_self.tokenPsgBureau = ko.observable(palnTrainObj.tokenPsgBureau); //客运担当局
	};
	
	
	
	/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		_self.queryBtnCount = ko.observable(0);	//查询按钮点击次数 
		_self.searchModle = ko.observable(new SearchModle());		//页面查询对象
		_self.planTrainRows = new PageModle(5000, loadPlanDataForPage);		//页面开行计划列表对象
		_self.hightLineCrewRows = new PageModle(10000, loadHightLineCrewSjDataForPage);		//页面车长乘务计划列表对象
		_self.hightLineCrewModel = ko.observable(new HighlineCrewModel());	//用于乘务计划新增、修改
		_self.hightLineCrewModelTitle = ko.observable();	//用于乘务计划新增、修改窗口标题
		_self.hightLineCrewSaveFlag = ko.observable();		//用于乘务计划新增、修改标识
		
		_self.currentRowCrewHighlineId = ko.observable();//列表行id
		_self.currentRowPlanTrainId = ko.observable();//开行计划列表行id
		_self.isSelectAll = ko.observable(false);	//本局担当命令列表是否全选 	全选标识  默认false
		//被选中的当前一行列车
		_self.currentTrain =  ko.observable();
		//时刻表详点
		_self.times = ko.observableArray();
		//时刻表简点
		_self.simpleTimes = ko.observableArray();
		_self.currentTrainInfoMessage = ko.observable("");
		_self.gloabBureaus = []; 
		/**
//		 * 初始化查询条件
//		 */
//		_self.initPageData = function() {
//			_self.searchModle().initData();
//		};
		_self.searchModle = ko.observable(new SearchModle()); 

		/**
		 * 查询按钮事件
		 */
		_self.queryList = function(){
			commonJsScreenLock(2);
			_self.currentRowCrewHighlineId("");
			_self.currentRowPlanTrainId("");
			//1.查询开行计划
			var listId = $("#chaxun").attr('id');
			_self.planTrainRows.loadRows(listId);	//loadRows为分页组件中方法

			//2.查询车长乘务计划信息
			_self.hightLineCrewRows.loadRows();	//loadRows为分页组件中方法
			
			_self.queryBtnCount += 1;	//查询按钮点击次数+1
			
			boxScroll();
		};
		
		
		/**
		 * 校验按钮事件
		 * 检测车次是否上报车长乘务计划
		 * 
		 * 当planTrainRows中车次存在于hightLineCrewRows中乘务交路 则视为该车次已上报
		 */
		_self.checkCrew = function() {
			for(var i=0; i<_self.planTrainRows.rows().length;i++) {
				_self.planTrainRows.rows()[i].isMatch("0");//恢复匹配颜色默认值;
				var _trainNbr = _self.planTrainRows.rows()[i].trainNbr();
				for(var j=0; j<_self.hightLineCrewRows.rows().length;j++) {
					var crewCrossArray = _self.hightLineCrewRows.rows()[j].crewCross.split("-");
					
					if($.inArray(_trainNbr, crewCrossArray) > -1) {
						_self.planTrainRows.rows()[i].isMatch("1");//车次匹配  已上报车长乘务计划 
						break;
					}
				}
			}
		};
		
		/**
		 * 全选复选框change事件
		 */
		_self.checkBoxSelectAllChange = function() {
			if (!_self.isSelectAll()) {//全选     将runPlanLkCMDRows中isSelect属性设置为1
				$.each(_self.hightLineCrewRows.rows(), function(i, row){
					row.isSelect(1);
				});
			} else {//全不选    将runPlanLkCMDRows中isSelect属性设置为0
				$.each(_self.hightLineCrewRows.rows(), function(i, row){
					row.isSelect(0);
				});
			}
			
		};
		
		/**
		 * 检验按钮是否允许点击
		 * 
		 * 当查询按钮点击次数 ==0 或开行计划list==0时  按钮不可用
		 */
		_self.checkAndSendBtnEnable = ko.computed(function() {
			//查询按钮点击次数 
	        if(_self.queryBtnCount == 0 || _self.planTrainRows.rows().length==0) {
	            return false;
	        }
	        
	        return true;
	    });
		
		

		/**
		 * 添加文电按钮点击事件
		 * 
		 */
		_self.onAddOpen = function() {
				_self.hightLineCrewSaveFlag("add");
				_self.hightLineCrewModelTitle("新增车长乘务计划");
				_self.hightLineCrewModel().update(null);
				var path = "";
				var title = "";
				var cmdTypeText = "";
				path = "/runPlanLk/sjAddPageJy";
				title = "新增填报司机";
				cmdTypeText = "新增填报司机";
				$("#add_sj_info_dialog").find("iframe").attr("src", basePath + path).attr("id", cmdTypeText);
				$('#add_sj_info_dialog').dialog({title: title, autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#add_sj_info_dialog").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#add_sj_info_dialog').height();
						var WW = $('#add_sj_info_dialog').width();
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
				
			//};
			//$("#add_wd_cmd_info_dialog").find("iframe").find()
//			_self.wdCmdTrain(new WdCmdTrainModel(createEmptyCmdTrainRow()));//创建一个空对象
		};
		
		

		/**
		 * 修改页面打开时
		 */
		_self.onEditOpen = function() {
			_self.hightLineCrewSaveFlag("update");
			_self.hightLineCrewModelTitle("修改司机乘务计划");
			
			var currentCrewHighlineId = "";
			var currentCrewHighlineIdArray = [];
			$("[name='crew_checkbox']").each(function(){
				if($(this).is(":checked")) {
					currentCrewHighlineId = $(this).val();
					currentCrewHighlineIdArray.push(currentCrewHighlineId);
				}
		    });
			
			if(currentCrewHighlineIdArray.length >1){
				showWarningDialog("只能选择一条乘务计划记录");
				return;
			}
			
			if (currentCrewHighlineIdArray.length == 0) {
				showWarningDialog("请选择一条乘务计划记录");
				return;
			}
			
			var objSelect = null;
			for(var i = 0;i<_self.hightLineCrewRows.rows().length;i++){
				obj = _self.hightLineCrewRows.rows()[i];
				if (obj.isSelect() == 1) {
					objSelect = _self.hightLineCrewRows.rows()[i];
				}
			}
			
			var path = "";
			var title = "";
			var cmdTypeText = "";
			path = "/runPlanLk/sjEditPageJy";
			title = "新增填报司机";
			cmdTypeText = "新增填报司机";
			$("#add_sj_info_dialog").find("iframe").attr("src", basePath + path).attr("name", objSelect.crewHighlineId).attr("id", cmdTypeText);
			$('#add_sj_info_dialog').dialog({title: title, autoOpen: true, modal: false, draggable: true, resizable:true,
				onResize:function() {
					var iframeBox = $("#add_sj_info_dialog").find("iframe");
					var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
					var WH = $('#add_sj_info_dialog').height();
					var WW = $('#add_sj_info_dialog').width();
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
		 * 删除
		 */
		_self.deleteHightLineCrew = function() {
			var _deleteHightLines = [];
			for(var i=0;i<_self.hightLineCrewRows.rows().length;i++){
				var obj = _self.hightLineCrewRows.rows()[i];
				if (obj.isSelect() == 1) {
					_deleteHightLines.push(obj.crewHighlineId);
				}
			}
			
			
			if(_deleteHightLines.length == 0) {
				showWarningDialog("请选择车长记录");
				return;
			} 

			showConfirmDiv("提示", "你确定要执行删除操作?", function (r) {
				if (r) {
					commonJsScreenLock(2);
					$.ajax({
						url : basePath+"/crew/highline/deleteHighLineCrewInfo",
						cache : false,
						type : "POST",
						dataType : "json",
						contentType : "application/json",
						data :JSON.stringify({
							crewHighLineIds : _deleteHightLines
						}),
						success : function(result) {
							if (result != null && typeof result == "object" && result.code == "0") {
								//2.查询车长乘务计划信息
								_self.hightLineCrewRows.loadRows();	//loadRows为分页组件中方法
								showSuccessDialog("成功删除车长乘务计划信息");
							} else {
								commonJsScreenUnLock(1);
								showErrorDialog("删除车长乘务计划信息失败");
							};
						},
						error : function() {
							commonJsScreenUnLock(1);
							showErrorDialog("删除车长乘务计划信息失败");
						},
						complete : function(){
							commonJsScreenUnLock(1);
						}
					});
				}
			});
			_self.isSelectAll(0);
		};
		
		
		

		/**
		 * 发布按钮事件
		 */
		_self.sendCrew = function(){
			
			
			var a=""
				console.dir(_self.hightLineCrewRows.rows())
				for(var j = 0;j < _self.hightLineCrewRows.rows().length;j++){
				if(_self.hightLineCrewRows.rows()[j].isSelect()==true||_self.hightLineCrewRows.rows()[j].isSelect()==1){
				a+=_self.hightLineCrewRows.rows()[j].crewHighlineId+","
				};
				}
				if(a.length<10){
					showWarningDialog("请先选择交路");
					return "";
				}
			commonJsScreenLock(2);
			$.ajax({
				url : basePath+"/crew/highline/updateSubmitType",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewType : "12",//乘务类型（1车长、2车长、3机械师）
					crewDate : $("#crew_input_rundate").val(),//_self.searchModle().runDate()
					a:a
				}),
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						//2.查询车长乘务计划信息
						_self.hightLineCrewRows.loadRows();	//loadRows为分页组件中方法
						showSuccessDialog("提交成功");
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("提交车长乘务计划信息失败");
					};
					
					

				},
				error : function() {
					commonJsScreenUnLock(1);
					showErrorDialog("提交车长乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
			
		};
		
		

		/**
		 * 新增/修改乘务计划
		 */
		_self.saveHightLineCrew = function(){
			commonJsScreenLock(2);
			
			var _url = "";
			var _type = "";
			if (_self.hightLineCrewSaveFlag() == "add") {
				_url = basePath+"/crew/highline/add";
				_type = "POST";
			} else if (_self.hightLineCrewSaveFlag() == "update") {
				_url = basePath+"/crew/highline/update";
				_type = "PUT";
			}
			
			$.ajax({
				url : _url,
				cache : false,
				type : _type,
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewHighlineId : _self.hightLineCrewModel().crewHighlineId(),
					crewType : "12",//乘务类型（1车长、2车长、3机械师）
					crewDate : $("#crew_input_rundate").val(),//_self.searchModle().runDate(),
					crewCross : _self.hightLineCrewModel().crewCross(),
					crewGroup : _self.hightLineCrewModel().crewGroup(),
					throughLine : _self.hightLineCrewModel().throughLine(),
					name1 : _self.hightLineCrewModel().name1(),
					tel1 : _self.hightLineCrewModel().tel1(),
					identity1 : _self.hightLineCrewModel().identity1(),
					name2 : _self.hightLineCrewModel().name2(),
					tel2 : _self.hightLineCrewModel().tel2(),
					identity2 : _self.hightLineCrewModel().identity2(),
					note : _self.hightLineCrewModel().note()
				}),
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						//2.查询车长乘务计划信息
						_self.hightLineCrewRows.loadRows();	//loadRows为分页组件中方法
						showSuccessDialog("保存成功");
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("保存车长乘务计划信息失败");
					};
				},
				error : function() {
					commonJsScreenUnLock(1);
					showErrorDialog("保存车长乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		_self.bureauChange = function(){
			var xialaId = $("#xiala").attr("id");
			_self.planTrainRows.loadRows(xialaId);	//loadRows为分页组件中方法
		};
		
		
		/**
		 * 分页查询开行计划列表
		 */
		function loadPlanDataForPage(startIndex, endIndex,flag) {
			var _runDate = $("#crew_input_rundate").val();//_self.searchModle().runDate();	//运行日期
			var _trainNbr =_self.searchModle().trainNbr()!="undefined"?_self.searchModle().trainNbr():"";//车次
			if(_runDate == null || typeof _runDate!="string"){
				showErrorDialog("请选择日期!");
				commonJsScreenUnLock();
				return;
			}
	    	$.ajax({
				url : basePath+"/crew/highline/getRunLineListForRunDate",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					flag:flag,
					currentBureau: _self.searchModle().bureau().shortName,
					runDate : _runDate,
					trainNbr : _trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){
							$.each(result.data.data,function(n, obj){
//										var row = new PlanTrainRowModel(obj);
//										row.isMatch("0");
								obj.isMatch = "0";
								rows.push(new PlanTrainRowModel(obj));
							});
							_self.planTrainRows.loadPageRows(result.data.totalRecord, rows);
						}
						
					} else {
						showErrorDialog("获取开行计划信息失败");
					};
					//是否出现右边17
					var tableH = $("#right_height table").height();
					var boxH = $("#right_height").height();
					if(tableH <= boxH){
					    //alert("没");
						$("#right_height .td_17").removeClass("display");
					}else{
						//alert("有");
						$("#right_height .td_17").addClass("display");
					}
				},
				error : function() {
					showErrorDialog("获取开行计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});

		};
		
		
		
		/**
		 * 分页查询车长乘务计划列表
		 */
		function loadHightLineCrewSjDataForPage(startIndex, endIndex) {
			
			var _runDate = $("#crew_input_rundate").val();//_self.searchModle().runDate();	//运行日期
			var _trainNbr = _self.searchModle().trainNbr();	//车次
			var xwyName = _self.searchModle().xwyName();//乘务员姓名
			var crewGroupName ;
			if(_self.searchModle().crewGroup()!=null && _self.searchModle().crewGroup()!=undefined){
				crewGroupName = _self.searchModle().crewGroup().crewGroupName;//乘务组编号
			}else{
				crewGroupName ="";
			}
			
			
			if(_runDate == null || typeof _runDate!="string"){
				showErrorDialog("请选择日期!");
				commonJsScreenUnLock();
				return;
			}
			
			$.ajax({
				url : basePath+"/crew/highline/getHighlineCrewListForRunDate",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewGroupName:crewGroupName,
					crewType : "12",	//乘务类型（1车长、2车长、3机械师）
					crewDate : _runDate,
					xwyName :  xwyName,
					trainNbr : _trainNbr,
					rownumstart : startIndex, 
					rownumend : endIndex
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){
							$.each(result.data.data,function(n, obj){
								if (obj.submitType != null && obj.submitType==0) {
									obj.submitTypeStr = "<span class='label label-danger'>未</span>";
								} else if (obj.submitType != null && obj.submitType==1) {
									obj.submitTypeStr = "<span class='label label-success'>已</span>";
								} else {
									obj.submitTypeStr = "";
								}
								obj.isSelect = ko.observable(0);
								rows.push(obj);
							});
							_self.hightLineCrewRows.loadPageRows(result.data.totalRecord, rows);
							//是否出现右边17
							var tableH = $("#left_height table").height();
							var boxH = $("#left_height").height();
							if(tableH <= boxH){
							    //alert("没");
								$("#left_height .td_17").removeClass("display");
							}else{
								//alert("有");
								$("#left_height .td_17").addClass("display");
							}
						}
						 
					} else {
						showErrorDialog("获取车长乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取车长乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		
		/**
		 * 导出excel
		 */
		_self.exportExcel = function() {
			var _runDate = $("#crew_input_rundate").val();//_self.searchModle().runDate();	//运行日期
			var _trainNbr = _self.searchModle().trainNbr();	//车次
			var _highlineFlag = "0";
			if(_trainNbr=="undefined" || _trainNbr=="" || typeof _trainNbr=="undefined") {
				_trainNbr = "-1";
			}
			
			if(_runDate == null || typeof _runDate!="string" || _runDate==""){
				showErrorDialog("请选择日期!");
				return;
			}
			window.open(basePath+"/crew/highline/exportExcel/12/"+_runDate+"/"+_trainNbr+"/"+_highlineFlag);
		};
		
		
		
		/**
		 * 导入excel点击事件
		 * 
		 * 检查导入条件
		 */
		_self.checkBeforImportExcel = function() {
			var _runDate = $("#crew_input_rundate").val();//_self.searchModle().runDate();	//运行日期
			if(_runDate == null || typeof _runDate!="string" || _runDate==""){
				showErrorDialog("请选择日期!");
				return;
			}
		};
		
		
		/**
		 * 导入excel
		 */
		_self.uploadExcel = function() {
			var _runDate = $("#crew_input_rundate").val();//_self.searchModle().runDate();	//运行日期
			if(_runDate == null || typeof _runDate!="string" || _runDate==""){
				showErrorDialog("请选择日期!");
				return;
			}
		    if($("#crewExcelFile").val() == null || $("#crewExcelFile").val() == ""){
		    	showErrorDialog("没有可导入的文件"); 
		    	return;
		    }
		    $("#loading").show();
		    $("#btn_fileToUpload").attr("disabled", "disabled");
	        $.ajaxFileUpload ({
                url : basePath+"/crew/highline/importExcel",
                secureuri:false,
                fileElementId:'crewExcelFile',
                type : "POST",
                dataType: 'json',  
                data:{
                	crewDate:_runDate,
                	crewType:"12"//乘务类型（1车长、2车长、3机械师）
                },
                success: function (data, status) {
					_self.hightLineCrewRows.loadRows();	//loadRows为分页组件中方法
                	
                	showSuccessDialog("导入成功");
                	$("#loading").hide();
                	$("#btn_fileToUpload").removeAttr("disabled");
                	$("#btn_fileToUpload_cancel").click();
                	
                },
                error: function(result){
                	showErrorDialog("导入失败");
                	$("#loading").hide();
                	$("#btn_fileToUpload").removeAttr("disabled");
                }
            });  
	        return true;
		};
		
		

	
		
		
		self.isShowRunPlans = ko.observable(true);// 显示开行情况复选框   默认勾选
		self.showRunPlans = function(){
			if(!self.isShowRunPlans()) {//勾选
				$('#right_div').show();
				$("#panel_div").css("margin","0 550px 0 15px");
				//$("#panel_right_div").attr("style","margin-left: -2px;margin-right: -18px;margin-bottom: 0;");
				$("#all_div").css("margin-right","-550px");
				$("#right_div").css("width","540px");
			} else {//未勾选
				$('#right_div').hide();
				$("#panel_div").attr("style","margin-left:15px;margin-bottom: 0;");
				//$("#panel_right_div").attr("style","margin-left: 0px;width:0%;margin-bottom: 0;");
				$("#all_div").css("margin-right","0px");
				//$("#right_div").attr("style","width:0px;");
			}
		};
	

		_self.showCrossTrainTimeDlg = function(row){
			if(_self.currentTrain() == null){
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
		
		/**
		 * 乘务列表行点击事件
		 */
		_self.setCurrentRec = function(row) {
			for(var j = 0;j < _self.hightLineCrewRows.rows().length;j++){
				_self.hightLineCrewRows.rows()[j].isSelect(0);
			}
			_self.currentRowCrewHighlineId(row.crewHighlineId);
			for(var i = 0;i < _self.hightLineCrewRows.rows().length;i++){
				if(_self.hightLineCrewRows.rows()[i].crewHighlineId==row.crewHighlineId){
					row.isSelect(1);
				}
			}
		};
		
		
		//选中当前列车
		_self.setCurrentTrainPlan = function(row){
			_self.currentTrain(row);//列车开行计划当前对象
			_self.currentRowPlanTrainId(row.planTrainId);//列车开行计划行id
			_self.currentTrainInfoMessage("车次：" + row.trainNbr() + "&nbsp;&nbsp;&nbsp;" + row.startStn() + "——" + row.endStn());
			
			_self.times.remove(function(item){
				return true;
			});
			_self.simpleTimes.remove(function(item){
				return true;
			});
			if(row.times().length > 0){ 
				$.each(row.times(), function(i, n){
					_self.times.push(n); 
				}) ;
				$.each(row.simpleTimes(), function(i, n){
					_self.simpleTimes.push(n); 
				}) ; 
			}else{
//				$.getJSON("/trainplan/assets/js/trainplan/highLine/time.json", function(result){
//					if (result != null && result != "undefind" && result.code == "0") {  
//						row.loadTimes(result.data);  
//						$.each(row.times(), function(i, n){
//							_self.times.push(n); 
//						}) ;
//						$.each(row.simpleTimes(), function(i, n){
//							_self.simpleTimes.push(n); 
//						}) ; 
//					}   
//			     });
		 
				_self.queryPlanLineTrainTimes(row);
			}
		};
		
		
		
		
		_self.queryPlanLineTrainTimes = function(row){
		 	commonJsScreenLock();
			$.ajax({
	    	url : basePath+"/jbtcx/queryPlanLineTrainTimes",//获取时刻表数据url
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({   
				trainId : row.planTrainId()
			}),
			success : function(result) {  
				if (result.data != null && result.data != "undefind" && result.code == "0") {  
					row.loadTimes(result.data);  
					$.each(row.times(), function(i, n){
						_self.times.push(n); 
					}) ;
					$.each(row.simpleTimes(), function(i, n){
						_self.simpleTimes.push(n); 
					}) ; 
				}
				
//				alert(1089);
				//是否出现右边17
				var tableH = $("#simpleTimes_table table").height();
				var boxH = $("#simpleTimes_table").height();
				if(tableH <= boxH){
//				    alert("没");
					$("#simpleTimes_table .td_17").removeClass("display");
				}else{
//					alert("有");
					$("#simpleTimes_table .td_17").addClass("display");
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
		
		/**
		 * 页面加载时执行
		 * public
		 */
		_self.initPage = function () {
			$("#crew_input_rundate").datepicker();
			$("#cross_train_time_dlg").dialog("close");
			$("#add_sj_info_dialog").dialog("close");
			//页面元素绑定
			_self.searchModle().initData();
			
		    $.ajax({
				url : basePath+"/plan/getFullStationInfo",
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json", 
				success : function(result) {   
					var bureaus = new Array();
					if (result != null && result != "undefind" && result.code == "0") { 
						var curUserBur = currentUserBureauName;
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
						_self.searchModle().loadBureau(bureaus); 
						if (result.data !=null) { 
							$.each(result.data,function(n, bureau){  
								_self.gloabBureaus.push({"shortName": bureau.ljjc, "code": bureau.ljpym});
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
				url : basePath+"/crew/highline/getFullHighLineCrewInfo",
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json", 
				success : function(result) {   
					if (result != null && result != "undefind" && result.code == "0") { 
						_self.searchModle().loadCrewGroup(result.data); 
					} else {
						showErrorDialog("获取乘务组数据失败");
					} 
					

				},
				error : function() {
					showErrorDialog("获取乘务组数据失败");
				},
				complete : function(){ 
					commonJsScreenUnLock(); 
				}
		    });
		    
			
 };
		
		

		
		
		
		
		
		
};
	




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
				return  moment(data.dptTime).format("MMDD HH:mm");
			}
		}
	});
	self.stepStr = ko.observable(GetDateDiff(data));//停留时间
	self.trackName = ko.observable(filterValue(data.trackName));//股道  
	self.runDays = ko.computed(function(){//出发天数
		if(data.stationFlag!=null && data.stationFlag!=undefined){
			if(data.stationFlag == 'ZDZ'){
				return "";
			}else{
				return data.runDays;
			}
		}
	});
	self.arrRunDays = ko.computed(function(){//到达运行天数
		if(data.stationFlag!=null && data.stationFlag!=undefined){
			if(data.stationFlag == 'SFZ'){
				return "";
			}else{
				return data.arrRunDays;
			}
		}
	});
	self.stationFlag = ko.observable(data.stationFlag);
	self.isChangeValue = ko.observable(0);
	
};
function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860-760--"+WH);
	$("#left_height").css("height",WH-197+"px");
	$("#right_height").css("height",WH-134+"px");
};
function boxScroll(){
 /*   
	var ST = $("#left_height").scrollTop();
	var OH = $("#left_height").offsetHeight;
	var SH = $("#left_height").scrollHeight;
	alert("ST:"+OH);
	alert("OH:"+OH);
	alert("SH:"+SH);*/
	
	$(".box").addClass("display");
};

