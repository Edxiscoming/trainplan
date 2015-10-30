var text = self.frameElement.id;
$(function() {
	new SJAddPage().initPage();

});


var sjAddPageModel = null;//界面绑定元素对象
var SJAddPage = function () {
	
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		//页面元素绑定
		sjAddPageModel = new PageModel();
		ko.applyBindings(sjAddPageModel);
		sjAddPageModel.getHightLineCrewListByWeek();
	};
	
	function PageModel() {
		var _self = this;
		_self.hightLineCrewModelTitle = ko.observable();	//用于乘务计划新增、修改窗口标题
		_self.hightLineCrewModel = ko.observable(new HighlineCrewModel());	//用于乘务计划新增、修改
		_self.hightLineCrewSaveFlag = ko.observable();		//用于乘务计划新增、修改标识
		_self.crewGroups =  ko.observableArray();
	    _self.throughLines =  ko.observableArray();
	    _self.identity1s =  ko.observableArray();
	    _self.identity2s =  ko.observableArray();
		
		/**
		 * 新增/修改乘务计划
		 */
		_self.saveHightLineCrew = function(){
			var crewCross =  _self.hightLineCrewModel().crewCross();
			var name1 = _self.hightLineCrewModel().name1();
			var tel1 = _self.hightLineCrewModel().tel1();
			if(crewCross==null || crewCross==undefined || crewCross==""){
				showErrorDialog("请填写乘务交路");
				return false;
			}
			if(name1==null || name1==undefined || name1==""){
				showErrorDialog("请填写司机1姓名");
				return false;
			}
			if(tel1==null || tel1==undefined ||tel1==""){
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
				_url = basePath+"/crew/highline/add";
				_type = "POST";
			$.ajax({
				url : _url,
				cache : false,
				type : _type,
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					crewHighlineId : _self.hightLineCrewModel().crewHighlineId(),
					crewType : "2",//乘务类型（1车长、2车长、3机械师）
					crewDate : window.parent.pageModel.searchModle().runDate(),
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
//						showSuccessDialog("保存成功");
						parent.$("#add_sj_info_dialog").dialog("close"); //子页面关闭父页面的dialog
						window.parent.pageModel.hightLineCrewRows.loadRows();//调用父类js的查询方法
					} else {
						commonJsScreenUnLock(1);
						showErrorDialog("保存车长乘务计划信息失败");
					};
					commonJsScreenUnLock(1);
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

  };
  
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


