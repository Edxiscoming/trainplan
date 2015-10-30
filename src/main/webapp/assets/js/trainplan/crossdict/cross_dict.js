


var CrossDictPage = function() {
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
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
		_self.crossDictSearchModle = ko.observable(new CrossDictSearchModle());		//页面查询对象
		_self.crossDictRows = new PageModle(200, loadCrossDictForPage);	//页面交路列表对象
		
		
		/**
		 * 初始化查询条件
		 */
		_self.initPageData = function() {
			_self.crossDictSearchModle().initData();
		};
		

		/**
		 * 交路字典查询按钮事件
		 */
		_self.queryCrossDictList = function(){
			_self.crossDictRows.loadRows();	//loadRows为分页组件中方法
		};
		
		
		
		function loadCrossDictForPage() {
			commonJsScreenLock();
			
			var schemeId = _self.crossDictSearchModle().scheme().value;	//方案id
			var tokenVehBureau = _self.crossDictSearchModle().tokenVehBureau().value;//担当局局码
			var crossName = _self.crossDictSearchModle().crossName();//交路名
			
			
			if(schemeId == null || typeof schemeId=="undefine"){
				showErrorDialog("请选择方案!");
				commonJsScreenUnLock();
				return;
			}
			 
			$.ajax({
				url : basePath+"/cross/getCrossInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					tokenVehBureau : tokenVehBureau,
					crossName : crossName,
					baseChartId: schemeId
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						var rows = [];
						if(result.data.data != null){
							$.each(result.data.data,function(n, crossDictInfo){
								var crossDictModel = new CrossDictModel();
					        	crossDictModel.setData(crossDictInfo);
								rows.push(crossDictModel);
							});
							_self.crossDictRows().loadPageRows(result.data.totalRecord, rows);
						}  
						// $("#cross_table_crossInfo").freezeHeader(); 
						 
					} else {
						showErrorDialog("获取交路基本信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取交路基本信息失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			});
		};
		
		
		
	};
	
	
	/**
	 * 交路字典查询model
	 * 
	 * private
	 */
	function CrossDictSearchModle(){
		
		var _self = this;
		
		/**
		 * 查询条件
		 */
		_self.scheme =  ko.observable();		//方案下拉框中选择值
		_self.schemeCombox = ko.observableArray();	//方案列表
		_self.crossName = ko.observable();		//交路名input值
		_self.tokenVehBureauCombox = ko.observableArray();	//担当局下拉框列表
		_self.tokenVehBureau = ko.observable();	//担当局下拉框选择值
		_self.isShortName = ko.observable();	//交路table中交路列  是否显示简称		{'code': 1, 'text': '简称'},{'code': 2, 'text': '全称'}
		

		
		_self.initData = function() {
			commonJsScreenLock(2);		//锁屏
			
			//初始化数据
			loadSchemeComboxData();		//初始化方案列表下拉框数据
			loadTokenVehBureauCombox();//初始化担当局列表下拉框数据
			
		};
		
		
		//方案列表下拉框数据
		function loadSchemeComboxData(screenLockCount){
			$.ajax({
				url : basePath+"/jbtcx/querySchemes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({}),
				success : function(result) {    
					if (result != null && typeof result == "object" && result.code == "0") {
						if (result.data !=null && typeof result.data == "object") {
							for ( var i = 0; i < result.data.length; i++) {
								_self.schemeCombox.push({"value": result.data[i].schemeId, "text": result.data[i].schemeName});
							}
						}
					} else {
						showErrorDialog("获取方案列表失败");
					}
				},
				error : function() {
					showErrorDialog("获取方案列表失败");
				},
				complete : function(){
					commonJsScreenUnLock();//屏幕解锁
				}
		    });
		};
		
		//担当局列表下拉框数据
		function loadTokenVehBureauCombox(screenLockCount){
			$.ajax({
				url : basePath+"/plan/getFullStationInfo",
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json", 
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") { 
						if (result.data !=null && typeof result.data == "object") { 
							$.each(result.data,function(n, bureau){  
								_self.tokenVehBureauCombox.push({"value": bureau.ljpym, "text": bureau.ljjc});
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
					commonJsScreenUnLock();//屏幕解锁
				}
		    });
		};
		
		
		
	};



	/**
	 * 交路字典model
	 * private
	 */
	function CrossDictModel() {
		var _self = this;
		_self.schemeId = ko.observable();		//方案id
		_self.schemeName = ko.observable();		//方案名称
		_self.tokenVehBureau = ko.observable();	//担当局
		_self.shortName = ko.observable();		//交路名简称
		_self.crossName = ko.observable();		//交路名全称
		_self.isDict = ko.observable();			//字典中是否存在
		
		_self.setData = function(crossDict) {
			_self.schemeId = crossDict.baseChartId;		//方案id
			_self.schemeName = crossDict.baseChartName;		//方案名称
			_self.tokenVehBureau = crossDict.ljjc;	//担当局
			_self.crossName = crossDict.crossName;		//交路名
			_self.isDict = crossDict.isDict;			//字典中是否存在
		};
	};



	
	
	
};


$(function() {
	new CrossDictPage().initPage();
});