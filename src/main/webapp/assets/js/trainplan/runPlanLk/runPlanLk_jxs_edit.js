var crewHighLineId = self.frameElement.name;
var text = self.frameElement.id;

$(function() {
	
	new SJAddPage().initPage();

});

var sjAddPageModel = null;// 界面绑定元素对象
var SJAddPage = function() {

	/**
	 * 页面加载时执行 public
	 */
	this.initPage = function() {
		// 页面元素绑定
		sjAddPageModel = new PageModel();
		ko.applyBindings(sjAddPageModel);
		sjAddPageModel.getHightLineCrewListByWeek();
	};


	function PageModel() {
		var _self = this;
		_self.hightLineModel = ko.observable();
		_self.hightLineModel(new HighlineCrewModel(createEmptyRow()));// 创建一个空对象
		_self.crewGroups =  ko.observableArray();
	    _self.throughLines =  ko.observableArray();
	    _self.identity1s =  ko.observableArray();
	    _self.identity2s =  ko.observableArray();
		
		/**
		 * 新增/修改乘务计划
		 */
		_self.saveHightLineCrew = function() {
			var crewCross =  _self.hightLineModel().crewCross();
			var name1 = _self.hightLineModel().name1();
			var tel1 = _self.hightLineModel().tel1();
			if(crewCross==null || crewCross==undefined || crewCross==""){
				showErrorDialog("请填写乘务交路");
				return false;
			}
			if(name1==null || name1==undefined || name1==""){
				showErrorDialog("请填写机械师1姓名");
				return false;
			}
			if(tel1==null || tel1==undefined || tel1==""){
				showErrorDialog("请填写电话");
				return false;
			}
//			}else{
//				if(!Validate.isPhone(tel1)){
//					showErrorDialog("电话格式不正确");
//					return false;
//				}
//			}
			commonJsScreenLock(2);

			var _url = "";
			var _type = "";
			 _url = basePath+"/crew/highline/update";
			 _type = "PUT";
			$.ajax({
				url : _url,
				cache : false,
				type : _type,
				dataType : "json",
				contentType : "application/json",
				data : JSON.stringify({
					crewHighlineId : crewHighLineId,
					crewType : "3",// 乘务类型（1车长、2司机、3机械师）
					crewDate : new Date(),
					crewCross : _self.hightLineModel().crewCross(),
					crewGroup : _self.hightLineModel().crewGroup(),
					throughLine : _self.hightLineModel().throughLine(),
					name1 : _self.hightLineModel().name1(),
					tel1 : _self.hightLineModel().tel1(),
					identity1 : _self.hightLineModel().identity1(),
					name2 : _self.hightLineModel().name2(),
					tel2 : _self.hightLineModel().tel2(),
					identity2 : _self.hightLineModel().identity2(),
					note : _self.hightLineModel().note()
				}),
				success : function(result) {
					if (result != null && typeof result == "object"
							&& result.code == "0") {
						commonJsScreenUnLock(1);
//						showSuccessDialog("保存成功");
						parent.$("#add_jxs_info_dialog").dialog("close"); //子页面关闭父页面的dialog
						window.parent.pageModel.hightLineCrewRows.loadRows();//调用父类js的查询方法
						window.parent.pageModel.isSelectAll(0);
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("保存司机乘务计划信息失败");
					}
					;
				},
				error : function() {
					commonJsScreenUnLock(1);
					showErrorDialog("保存司机乘务计划信息失败");
				},
				complete : function() {
					commonJsScreenUnLock(1);
				}
			});
		};
		/**
		 * 查询一周的数据
		 */
		_self.getHightLineCrewListByWeek = function(){
			commonJsScreenLock(2);
			$.ajax({
				url : basePath+"/crew/highline/getHighlineCrewListForWeek",
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json", 
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						commonJsScreenUnLock(1);
						var data = result.data;
						for(var i=0;i<data.length;i++){
							var obj = data[i];
							if(obj.crewGroup!=null && obj.crewGroup!=undefined){
								_self.crewGroups.push(obj.crewGroup);
							}
							if(obj.throughLine!=null && obj.throughLine!=undefined){
								 _self.throughLines.push(obj.throughLine);
							}
							if(obj.identity1!=null && obj.identity1!=undefined){
								 _self.identity1s.push(obj.identity1);
							}
							if(obj.identity2!=null && obj.identity2!=undefined){
								 _self.identity2s.push(obj.identity2);
							}
							
						}
						
						
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("获取信息失败");
					};
				},
				error : function() {
					commonJsScreenUnLock(1);
					showErrorDialog("获取信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		function createEmptyRow() {
			var _dataObj = {};

			_dataObj.crewHighlineId = "";
			_dataObj.crewDate = "";
			_dataObj.crewBureau = "";
			_dataObj.crewType = "";
			_dataObj.crewCross = "";
			_dataObj.crewGroup = "";
			_dataObj.throughLine = "";
			_dataObj.name1 = "";
			_dataObj.tel1 = "";
			_dataObj.identity1 = "";
			_dataObj.name2 = "";
			_dataObj.tel2 = "";
			_dataObj.identity2 = "";
			_dataObj.note = "";
			_dataObj.recordPeople = "";
			_dataObj.recordPeopleOrg = "";
			_dataObj.recordTime = "";
			_dataObj.submitType = "";
			
			//获取文电字段值
			$.ajax({
				url : basePath + "/crew/highline/getHighLineCrew/"+crewHighLineId,
				async : false,
				cache : false,
				type : "GET",
				dataType : "json",
				contentType : "application/json",
				success : function(result) {			
					if (result != null) {
						console.log(result.data);
						_dataObj.crewHighlineId = result.data.crewHighlineId;
						_dataObj.crewDate = result.data.crewDate;
						_dataObj.crewBureau = result.data.crewBureau;
						_dataObj.crewType = result.data.crewType;
						_dataObj.crewCross = result.data.crewCross;
						_dataObj.crewGroup = result.data.crewGroup;
						_dataObj.throughLine = result.data.throughLine;
						_dataObj.name1 = result.data.name1;
						_dataObj.tel1 = result.data.tel1;
						_dataObj.identity1 = result.data.identity1;
						_dataObj.name2 = result.data.name2;
						_dataObj.tel2 = result.data.tel2;
						_dataObj.identity2 = result.data.identity2;
						_dataObj.note = result.data.note;
						_dataObj.recordPeople = result.data.recordPeople;
						_dataObj.recordPeopleOrg = result.data.recordPeopleOrg;
						_dataObj.recordTime = result.data.recordTime;
						_dataObj.submitType = result.data.submitType;
					} else {
						showErrorDialog("获取司机乘务计划信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取司机乘务计划信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
			
			
			
			
			return _dataObj;
		};
		

		/**
		 * 乘务计划model用于新增、修改
		 */
		function HighlineCrewModel(obj) {
			
			var _self = this;
			
			_self.crewHighlineId = ko.observable(obj.crewHighlineId);
			_self.crewDate = ko.observable(obj.crewDate);
			_self.crewBureau = ko.observable(obj.crewBureau);
			_self.crewType = ko.observable(obj.crewType);
			_self.crewCross = ko.observable(obj.crewCross);
			_self.crewGroup = ko.observable(obj.crewGroup);
			_self.throughLine = ko.observable(obj.throughLine);
			_self.name1 = ko.observable(obj.name1);
			_self.tel1 = ko.observable(obj.tel1);
			_self.identity1 = ko.observable(obj.identity1);
			_self.name2 = ko.observable(obj.name2);
			_self.tel2 = ko.observable(obj.tel2);
			_self.identity2 = ko.observable(obj.identity2);
			_self.note = ko.observable(obj.note);
			_self.recordPeople = ko.observable(obj.recordPeople);
			_self.recordPeopleOrg = ko.observable(obj.recordPeopleOrg);
			_self.recordTime = ko.observable(obj.recordTime);
			_self.submitType = ko.observable(obj.submitType);
			_self.crewGroupOnfocus = function(n, event){
				$(event.target).autocomplete(sjAddPageModel.crewGroups(),{
					max: 50,    //列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 120,   //提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false,    //是否自动填充
					formatResult: function(row) {
						return row;
					}
		       }).result(function(event, crewGroup, formatted) {
		    	   if(crewGroup!=null && crewGroup!=undefined){
		    		   for(var i=0;i<crewGroup.length;i++){
		    			   _self.crewGroup(crewGroup[i]);
		    		   }
		    	   }
		    	   
		       });
			};
			_self.throughLineOnfocus = function(n, event){
				$(event.target).autocomplete(sjAddPageModel.throughLines(),{
					max: 50,    //列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 120,   //提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false,    //是否自动填充
					formatResult: function(row) {
						return row;
					}
		       }).result(function(event, throughLine, formatted) {
		    	   if(throughLine!=null && throughLine!=undefined){
		    		   for(var i=0;i<throughLine.length;i++){
		    			   _self.throughLine(throughLine[i]);
		    		   }
		    	   }
		    	   
		       });
			};
			_self.identity1Onfocus = function(n, event){
				$(event.target).autocomplete(sjAddPageModel.identity1s(),{
					max: 50,    //列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 120,   //提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false,    //是否自动填充
					formatResult: function(row) {
						return row;
					}
		       }).result(function(event, identity1, formatted) {
		    	   if(identity1!=null && identity1!=undefined){
		    		   for(var i=0;i<identity1.length;i++){
		    			   _self.identity1(identity1[i]);
		    		   }
		    	   }
		    	   
		       });
			};
			_self.identity2Onfocus = function(n, event){
				$(event.target).autocomplete(sjAddPageModel.identity2s(),{
					max: 50,    //列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 120,   //提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false,    //是否自动填充
					formatResult: function(row) {
						return row;
					}
		       }).result(function(event, identity2, formatted) {
		    	   if(identity2!=null && identity2!=undefined){
		    		   for(var i=0;i<identity2.length;i++){
		    			   _self.identity2(identity2[i]);
		    		   }
		    	   }
		    	   
		       });
			};
		};
		
		
	};

};
