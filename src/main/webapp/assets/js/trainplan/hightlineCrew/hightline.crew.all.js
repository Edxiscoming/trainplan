var crewTypeArray = [{'code': 'all', 'text': ''},{'code': '1', 'text': '车长'},{'code': '2', 'text': '司机'},{'code': '3', 'text': '机械师'}];
var crewType = $("#crewType").val();
var HightLineCrewSjPage = function () {
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		$("#crew_input_startDate").datepicker();
		$("#crew_input_endDate").datepicker();
		
		//页面元素绑定
		
		var pageModel = new PageModel();
		ko.applyBindings(pageModel);
		
		pageModel.initPageData();
		
		
	};
	
	
	
	
	
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
		_self.orgSelectAll = ko.observableArray();	//部门全部数据
		_self.orgSelect = ko.observableArray();	//部门下拉框
		_self.bureauSelect = ko.observableArray();	//路局下拉框
		
		_self.trainNbr = ko.observable("");		//车次号
		_self.crewTypeArray = ko.observableArray(crewTypeArray);
		if('cz' == crewType) {
			_self.crewTypeOption = ko.observable(crewTypeArray[1]);		//乘务类型下拉框选项
		}
		else if('sj' == crewType) {
			_self.crewTypeOption = ko.observable(crewTypeArray[2]);		//乘务类型下拉框选项
		}
		else if('jxs' == crewType) {
			_self.crewTypeOption = ko.observable(crewTypeArray[3]);		//乘务类型下拉框选项
		}
		else {
			_self.crewTypeOption = ko.observable();		//乘务类型下拉框选项
		}
		
		_self.bureauOption = ko.observable();	//路局下拉框选项
		_self.orgOption = ko.observable();	//部门下拉框选项
		_self.crewPeopleName = ko.observable("");		//乘务员姓名

		/**
		 * 初始化查询条件值
		 */
		_self.initData = function() {
//			commonJsScreenLock(2);
			$("#crew_input_startDate").val(currdate());
			$("#crew_input_endDate").val(currdate());

			_self.bureauSelect.push({"value": "", "text": ""});
			
			_self.loadBureauData();//加载路局下拉框数据
			_self.loadOrgData();//加载部门下拉框数据
			
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
		
		
		/**
		 * 加载路局下拉框数据
		 */
		_self.loadBureauData = function() {
			$.ajax({
				url : basePath+"/plan/getFullStationInfo",
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json", 
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {
						for ( var i = 0; i < result.data.length; i++) {
							_self.bureauSelect.push({"value": result.data[i].ljpym, "text": result.data[i].ljjc}); 
						}
					} else {
						showErrorDialog("获取路局列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取路局列表失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
		    });
		};
		
		
		
		/**
		 * 加载部门下拉框数据
		 */
		_self.loadOrgData = function() {
			$.ajax({
				url : basePath+"/crew/highline/getRecordPeopleOrgList",
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json", 
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
						for ( var i = 0; i < result.data.length; i++) {
							_self.orgSelectAll.push(result.data[i]);
							_self.orgSelect.push({"value": result.data[i].code, "text": result.data[i].name}); 
						}
					} else {
						showErrorDialog("获取部门列表失败");
					} 
				},
				error : function() {
					showErrorDialog("获取部门列表失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
		    });
		};
		
		
		/**
		 * 计划局下拉框change事件
		 */
		_self.bureauSelectChange = function() {
			//清除当前部门下拉框数据
			_self.orgSelect.remove(function(item){
				return true;
			});
			_self.orgSelect.push({"value": "all", "text": ""});//添加空选项
			
			//加载数据
			if ("all" == _self.bureauOption().value || _self.bureauOption().value == "" || _self.bureauOption().value=="undefined") {//路局下拉框当前选项为空
				for (var i = 0; i < _self.orgSelectAll().length; i++) {
					_self.orgSelect.push({"value": _self.orgSelectAll()[i].code, "text": _self.orgSelectAll()[i].name});
				}
			} else {//只加载查询局的数据
				for (var i = 0; i < _self.orgSelectAll().length; i++) {
					if(_self.bureauOption().value == _self.orgSelectAll()[i].bureauCode) {
						_self.orgSelect.push({"value": _self.orgSelectAll()[i].code, "text": _self.orgSelectAll()[i].name});
					}
				}
			}
		};
		
		
	};
	
	
	
	
	
	
	/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		_self.searchModle = ko.observable(new SearchModle());		//页面查询对象
		_self.hightLineCrewRows = new PageModle(200, loadHightLineCrewSjDataForPage);		//页面乘务计划列表对象

		_self.currentRowCrewHighlineId = ko.observable();//列表行id
		
		/**
		 * 初始化查询条件
		 */
		_self.initPageData = function() {
			_self.searchModle().initData();
		};
		

		/**
		 * 查询按钮事件
		 */
		_self.queryList = function(){
			_self.currentRowCrewHighlineId("");

			//2.查询乘务计划信息
			_self.hightLineCrewRows.loadRows();	//loadRows为分页组件中方法
		};
		
		
		
		/**
		 * 分页查询乘务计划列表
		 */
		function loadHightLineCrewSjDataForPage(startIndex, endIndex) {
			var crewStartDate = $("#crew_input_startDate").val();
			var crewEndDate = $("#crew_input_endDate").val();
			if(crewStartDate !=null && crewStartDate!=undefined && crewEndDate!=null && crewEndDate!=undefined){
				if(crewStartDate > crewEndDate){
					showWarningDialog("开始日期不能大于截止日期!");
					return false;
				}
			}
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/crew/highline/getHighlineCrewBaseInfoForPage",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewType : _self.searchModle().crewTypeOption().code,	//乘务类型（all全部、1车长、2司机、3机械师）
					crewStartDate : crewStartDate,
					crewEndDate : crewEndDate,
					crewBureau : _self.searchModle().bureauOption().text,//路局
					recordPeopleOrg : _self.searchModle().orgOption().value,//部门
					trainNbr : _self.searchModle().trainNbr(),//车次
					name : _self.searchModle().crewPeopleName(),//乘务员姓名
					rownumstart : startIndex, 
					rownumend : endIndex,
					highlineFlag : 1
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
								
								//乘务类型（1车长、2司机、3机械师）
								if (obj.crewType == "1") {
									obj.crewTypeName = "车长";
								} else if (obj.crewType == "2") {
									obj.crewTypeName = "司机";
								} else if (obj.crewType == "3") {
									obj.crewTypeName = "机械师";
								} else {
									obj.crewTypeName = "";
								}
								
								
								rows.push(obj);
							});
							_self.hightLineCrewRows.loadPageRows(result.data.totalRecord, rows);
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
				
						 
					} else {
						showErrorDialog("获取乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取乘务计划信息失败");
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
			var _crewStartDate = $("#crew_input_startDate").val();	//日期
			var _crewEndDate = $("#crew_input_endDate").val();	//日期
			var _crewType = _self.searchModle().crewTypeOption().code;;//乘务类型（all全部、1车长、2司机、3机械师）
			var _crewBureau = _self.searchModle().bureauOption().text;//路局
			var _recordPeopleOrg = _self.searchModle().orgOption().value;//部门
			var _trainNbr = _self.searchModle().trainNbr();	//车次
			var _name = _self.searchModle().crewPeopleName();//乘务员姓名
			var _url = basePath+"/crew/highline/exportAllCrewTypeExcel?"
					+ "crewStartDate="+_crewStartDate+"&crewEndDate="+_crewEndDate
					+ "&crewType="+_crewType+"&crewBureau="+encodeURI(encodeURI(_crewBureau))
					+ "&recordPeopleOrg="+_recordPeopleOrg+"&trainNbr="+_trainNbr
					+ "&name="+encodeURI(encodeURI(_name));
					
			window.open(_url);
		};
		
		

		/**
		 * 列表行点击事件
		 */
		_self.setCurrentRec = function(row) {
			_self.currentRowCrewHighlineId(row.crewHighlineId);
		};
		
		
		
		
		
		
	};
	
	
	
	
	
	
	
};




$(function() {
	heightAuto();
	new HightLineCrewSjPage().initPage();
});


function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#left_height").css("height",WH-160+"px");	
};