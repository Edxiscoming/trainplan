/**
 * 调整列车时刻表
 * @author Denglj
*/

var userLoginBureauShortName;

var _trainUpdateList = [];
//列车运行规律下拉框数组
var _runRuleArray = [{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"},{"code": "3", "text": "择日"}];
var pageModel ;
var checkRepeat = false;
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
		pageModel.getFullNodeList();
		pageModel.searchModle().loadBureauData();//加载路局下拉框数据
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
		_self.bureauArray = ko.observableArray();	//路局简称list
		_self.bureauOption = ko.observable();		//路局下拉框选项
		_self.cmdNbrBureau = ko.observable("");		//路局命令号
		_self.cmdNbrSuperior = ko.observable("");	//总公司命令号
		_self.trainNbr = ko.observable("");			//车次号
		_self.selectStateOption = ko.observable();		//选线状态	下拉框选项 
		_self.createStateOption = ko.observable();		//生成开行计划状态	下拉框选项 
		_self.baseStationArray = ko.observableArray();	//所有车站的基本信息
//		_self.cmdTypeArrayBjdd = ko.observableArray(cmdTypeArrayBjdd);		//本局担当命令类型下拉框
		_self.cmdTypeOptionBjdd = ko.observable();		//本局担当命令类型下拉框选项 (既有加开；既有停运；高铁加开；高铁停运)
//		_self.cmdTypeArrayWjdd = ko.observableArray(cmdTypeArrayWjdd);		//外局担当命令类型下拉框
		_self.cmdTypeOptionWjdd = ko.observable();		//外局担当命令类型下拉框选项 (既有加开；既有停运；高铁加开；高铁停运)
		
		
		
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
						if (result.data !=null) {
							_self.bureauArray.push({"value": "","text": "","ljqc":"","ljpym":"","ljdm":""});
							for ( var i = 0; i < result.data.length; i++) {
								_self.bureauArray.push({"value": result.data[i].ljjc,
									"text": result.data[i].ljjc, 
									"ljqc":result.data[i].ljqc,
									"ljpym":result.data[i].ljpym, 
									"ljdm":result.data[i].ljdm});
							}
							
							//暂时不启用  自动补齐插件
//							renderInputTjj();//渲染途经局自动补全输入框
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
		
		
		//加载车站字典数据
		_self.loadStnData = function() {
			$.ajax({
				url : basePath+"/runPlanLk/getBaseStationInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
//					stnName : value//车站名称
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
						if (result.data !=null) {
//							for (var i = 0; i < result.data.length; i++) {
//								if (value == result.data[i].STNNAME) {
//									self.currentBureauJc(result.data[i].STNBUREAUSHORTNAME);//路局简称
//									break;
//								}
//							}
						}
					} else {
						showErrorDialog("获取车站信息失败");
					}
				},
				error : function() {
					showErrorDialog("获取车站信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
		    });
		};
		
		
		
		/**
		 * 渲染途经局自动补全输入框
		 */
		function renderInputTjj() {
			$('#runPlanLk_cmd_input_tjj')
	        .textext({
	            plugins : 'tags autocomplete'
	        })
	        .bind('getSuggestions', function(e, data)
	        {
	                textext = $(e.target).textext()[0],
	                query = (data ? data.query : '') || ''
	                ;

	            $(this).trigger(
	                'setSuggestions',
	                { result : textext.itemManager().filter(_self.bureauArray(), query) }
	            );
	        });
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
		_self.runRuleArray = ko.observableArray(_runRuleArray);	//开行规律下拉框
		_self.runRuleOption = ko.observable();					//开行规律下拉框选中项
		_self.trainStns = ko.observableArray();	//列车经由站列表
		_self.currentTrainInfoMessage = ko.observable("");
/*		_self.currentTrainLineTimesStn = ko.observable(new _self.TrainTimeRow({
			index:0,
			stnName:"",
			arrTrainNbr:"",
			dptTrainNbr:"",
			bureauShortName:"",
			arrTime:"",
			dptTime:"",
			trackName:"",
			platform:"",
			arrRunDays:0,
			dptRunDays:0,
			nodeId:"",
			jobs:"",
			nodeName:"",
			bureauId:"",
			isChangeValue:1,
			nodeStationId:"",
			nodeStationName:"",
			nodeTdcsId:"",
			nodeTdcsName:"",
			planTrainStnId:"",
			runDays:0,
			stationFlag:"BTZ",
			jobItems:"",
			jobItemsText:"",
			 kyyy:"",
		}));//列车时刻表选中行记录  用于上下移动
*/		 _self.stnNameTemps = ko.observableArray();	//站名
		 

		_self.items = ko.observableArray();
		_self.nonSelectItems = ko.observableArray();
		_self.selectedDate = ko.observable();//择日
		_self.cmdEndFlag = ko.observable(false);//令止复选框

		 
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
    								_self.stnNameTemps.push(obj);
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
        
        
		/**
		 * 列表行对象
		 * @param data
		 */
	 _self.TrainTimeRow = function(data) {
			var self = this;
			
			self.kyyyOptions = ko.observableArray([{code:"n",text:""},{code:"y",text:"是"}]);//客运营业下拉框数据
			
			self.currentBureauJc = ko.observable(data.bureauShortName);//当前行路局简称
			self.index = ko.observable(data.stnSort);
			self.data = data;
			self.stnName = ko.observable(data.stnName);
			self.bureauId = ko.observable(filterValue(data.bureauId));
			self.bureauShortName = ko.observable(data.bureauShortName);
			self.stnBureauFull = ko.observable(data.stnBureauFull);
			self.currentBureauJc = ko.observable(data.bureauShortName);//当前行路局简称
			self.isChangeValue = ko.observable(0);
			self.permissionDenied = ko.computed(function () {
//				if(user.bureauShortName=="" || user.bureauShortName==_self.bureauShortName()){
				if(userLoginBureauShortName=="" || userLoginBureauShortName==self.bureauShortName()){
					return false;//拥有此数据操作权限
				} else {
					return true;//没有此数据操作权限
				}
			}, this);
			self.planTrainStnId = ko.observable(data.planTrainStnId);
			self.planTrainId = ko.observable(data.planTrainId);
			self.stnSort = ko.observable(data.stnSort);
			self.nodeId = ko.observable(filterValue(data.nodeId));
			self.nodeName = ko.observable(filterValue(data.nodeName));
			self.arrTrainNbr = ko.observable(filterValue(data.arrTrainNbr));
			self.arrTime = ko.observable(data.stationFlag=="SFZ"?data.dptTime:data.arrTime);
			self.setColor = ko.observable(0);
//			self.arrTime = ko.computed(function(){
//				if(data.stationFlag!=null && data.stationFlag!=undefined){
//					if(data.stationFlag == 'SFZ'){
//						return "--";
//					}else{
//						return data.arrTime;
//					}
//				}
//			});
			self.baseArrTime = ko.observable(filterTimeValue(data.baseArrTime));
			self.arrRunDays = ko.observable(data.arrRunDays);
//			self.arrRunDays = ko.computed(function(){
//				if(data.stationFlag!=null && data.stationFlag!=undefined){
//					if(data.stationFlag == 'SFZ'){
//						return "--";
//					}else{
//						return data.arrRunDays;
//					}
//				}
//			});
			self.dptTrainNbr = ko.observable(filterValue(data.dptTrainNbr));
			self.dptTime = ko.observable(data.stationFlag=="ZDZ"?data.arrTime:data.dptTime);
//			self.dptTime = ko.computed(function(){
//				if(data.stationFlag!=null && data.stationFlag!=undefined){
//					if(data.stationFlag == 'ZDZ'){
//						return "--";
//					}else{
//						return data.dptTime;
//					}
//				}
//			});
			self.baseDptTime = ko.observable(filterTimeValue(data.baseDptTime));
			self.runDays = ko.observable(data.stationFlag=="ZDZ"?data.arrRunDays:data.runDays);
//			self.data.runDays = self.runDays();
//			self.runDays = ko.computed(function(){
//				if(data.stationFlag!=null && data.stationFlag!=undefined){
//					if(data.stationFlag == 'ZDZ'){
//						return "--";
//					}else{
//						return data.runDays;
//					}
//				}
//			});
			self.trackName = ko.observable(filterValue(data.trackName));
			self.platForm = ko.observable(filterValue(data.platForm));
			self.jobs = ko.observable(filterValue(data.jobs));
			
			self.stepStr = ko.observable(GetDateDiff(data));
			self.stationFlag = ko.observable(data.stationFlag);
			self.nodeStationId = ko.observable(data.nodeStationId);
			self.nodeStationName = ko.observable(data.nodeStationName);
			self.nodeTdcsId = ko.observable(data.nodeTdcsId);
			self.nodeTdcsName = ko.observable(data.nodeTdcsName);
			
			var jobsText = typeof data.jobs=='object'?'':data.jobs;
			jobsText = jobsText.replace('<客运营业>','').replace('<经由>','');
			jobsText = jobsText.substring(1,jobsText.length-1);
			self.jobItems = ko.observableArray(typeof data.jobs=="string"?jobsText.split("><"):[]);
			self.jobItemsText = ko.computed(function () {
					return self.jobItems().join(",");
			}, this);
			//客运营业下拉框选择值
			var _val = "";
//			if(self.stepStr() !="" || data.stationFlag=="SFZ" || data.stationFlag=="ZDZ") {
			if(data.stationFlag=="SFZ" || data.stationFlag=="ZDZ") {
				_val = "y";
			} else {
				_val = "n";
			}
			var jobsValue = data.jobs==undefined?"":data.jobs;
			if(jobsValue.indexOf("客运营业")>0){
				_val = "y";
			}
			
			var _array = self.kyyyOptions();
			var _arrayOp = [];
			for(var i=0;i<_array.length;i++) {
				if(_val == _array[i].code) {
					_arrayOp = _array[i];
				}
			}
				
			self.kyyy = ko.observable(_arrayOp);//客运营业
			
			
			/**
			 * 更新站点作业信息
			 */
			self.setKyyy = function() {
				var kyyy = self.kyyy();
				$.each(pageModel.trainStns(), function(i, obj){
					if(obj.planTrainStnId()==self.planTrainStnId()){
						obj.kyyy(self.kyyy())
					}
				});
			};
			
			self.stepStrChange = function() {
				var stepStr = self.stepStr();
				var _val = "";
				if(self.stepStr() !="") {
					_val = "y";
				} else {
					_val = "n";
				}
				var _array = self.kyyyOptions();
				var _arrayOp = [];
				for(var i=0;i<_array.length;i++) {
					if(_val == _array[i].code) {
						_arrayOp = _array[i];
					}
				}
				self.kyyy(_arrayOp);
			}
			
			
			//return true:可用/false:不可用
			self.btnEnable = ko.computed(function () {
				//timesDivBtnDisabled 时刻表table操作功能按钮false:可用/true：禁用
				//permissionDenied 权限 false拥有此数据操作权限/true没有此数据操作权限
				return !self.permissionDenied();
			}, this);
			
			self.inputReadOnly = ko.computed(function () {
				return self.permissionDenied();
			}, this);
			
			//车站名称自动补全
			self.stnNameTempOnfocus = function(n, event){
				$(event.target).autocomplete(pageModel.stnNameTemps(),{
					max: 50,    	//列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 500,   	//提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false ,   	//是否自动填充
					formatItem: function(obj) {
						self.bureauId(obj.bureauId);
						return obj.pinyinInitials+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+obj.name+'['+obj.bureauShortName+']';
					},
					formatResult: function(obj) {
						return obj;
					}
		       }).result(function(event, stnNameTemp, formatted) {
		    	   if(formatted!=null && formatted!=undefined){
		    		   var str = new String(formatted);
		    		   var name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));
		    		   var bureauShortName = str.substring(str.indexOf("[")+1, str.indexOf("]"));
//		    		   alert(name);
//		    		   alert(bureauShortName);
		    		   self.stnName(name);
         	    	   self.nodeName(name);
		    		   self.bureauShortName=bureauShortName;
		    		   self.currentBureauJc(bureauShortName);
		    	   }
		    	   
		    	   
		       });
			};
			
			//路局下拉框选择值
			self.bureauOptionWjdd = ko.computed(function () {
				var _array = _self.searchModle().bureauArray();
				for(var i=0;i<_array.length;i++) {
					if(self.currentBureauJc() == _array[i].text) {
						return _array[i];
					}
				}
				
				return {"value": "","text": "","ljqc":"","ljpym":"","ljdm":""};
			}, this);
			

			//路局简称
			self.stnBureau = ko.computed(function () {
				if (self.bureauOptionWjdd()!=null && self.bureauOptionWjdd()!=""&& self.bureauOptionWjdd()!="undefined") {
					return self.bureauOptionWjdd().value;
				}
				return "";
			});

			//路局全称
			self.stnBureauFull = ko.computed(function () {
				if (self.bureauOptionWjdd()!=null && self.bureauOptionWjdd()!=""&& self.bureauOptionWjdd()!="undefined") {
					return self.bureauOptionWjdd().ljqc;
				}
				return "";
			});
			
			
			//到达时间
			self.arrTimeOnblur = function(){
				self.setColor(0);
				$('#isSave').removeAttr("disabled");
				var arrT = self.arrTime();//到达时间
				var dptT = self.dptTime();//出发时间
				if(arrT!="" && arrT!=null && arrT!=undefined && dptT!=null && dptT!=undefined && dptT!=""){
					if(arrT > dptT){
						showErrorDialog("到达时间不能大于出发时间!");
						$("#isSave").attr('disabled',true);
						self.setColor(1);
						return ;
					}
				}
				for(var i=0;i<_self.trainStns().length;i++){
					if(self.index()==_self.trainStns()[i].index()){
						var obj = _self.trainStns()[i];
//						alert("obj.index()=="+obj.index());
//						alert("obj.arrTime()=="+obj.arrTime());
//						alert("obj.dptTime()=="+obj.dptTime());
						var upobj = _self.trainStns()[i-1];
//						var downobj = _self.trainStns()[i-1];
						if(obj!=null && obj!=undefined && upobj!=null && upobj!=undefined){
							if(obj.arrTime()!=null && obj.arrTime()!="" && obj.arrTime()!=undefined 
									&& upobj.dptTime()!="" && upobj.dptTime()!=undefined && upobj.dptTime()!=null){
								if(obj.arrTime() < upobj.dptTime()){
									showErrorDialog("下一站到达时间不能大于上一站出发时间!");
									$("#isSave").attr('disabled',true);
									self.setColor(1);
									return ;
								}
							}
						}
			
					}
				}
				

			};
			
			
			/*
			 * 出发到达车次  离焦事件
			*/		
				self.sourceTrainNbrOnblur = function(n, event){
					self.isChangeValue(1);
					if(self.arrTrainNbr()!="" && self.arrTrainNbr()!=null){
						self.dptTrainNbr(self.arrTrainNbr());
						var index = 10000000;
						$.each(pageModel.trainStns(), function(i, obj){
							if(obj.planTrainStnId()==self.planTrainStnId()){
								index = i;
							}
							if (i > index) {
								
								//只改本局的车次
								if(obj.bureauShortName()==userLoginBureauShortName){
									obj.arrTrainNbr(self.arrTrainNbr());
									obj.dptTrainNbr(self.arrTrainNbr());
								}
							}
						});
					}
				};
				
	
				self.targetTrainNbrOnblur = function(n, event){
					self.isChangeValue(1);
					var index = 10000000;
					if(self.dptTrainNbr()!="" && self.dptTrainNbr()!=null ){
						$.each(pageModel.trainStns(), function(i, obj){
							if(obj.planTrainStnId()==self.planTrainStnId()){
								index = i;
							}
							if (i > index) {
								//只改本局的车次
								if(obj.bureauShortName()==userLoginBureauShortName){
									obj.arrTrainNbr(self.dptTrainNbr());
									obj.dptTrainNbr(self.dptTrainNbr());
								}
							}
						});
					}
				};
				self.sourceTrainNbrChange = function(n, event){
					self.isChangeValue(1);
					var isAdd = true;	//是否将该行记录增加到需要保存数组中
					for (var i=0;i<_trainUpdateList.length;i++) {
						var _tempData = _trainUpdateList[i];
						if (_tempData.planTrainStnId == self.planTrainStnId()) {
							_tempData.arrTrainNbr = self.arrTrainNbr();
							
							isAdd = false;
							break;
						}
					}
					if (isAdd) {
						_trainUpdateList.push(self.data);
					}
				};
				
				
				self.targetTrainNbrChange = function(n, event){
					self.isChangeValue(1);
					
					var isAdd = true;	//是否将该行记录增加到需要保存数组中
					for (var i=0;i<_trainUpdateList.length;i++) {
						var _tempData = _trainUpdateList[i];
						if (_tempData.planTrainStnId == self.planTrainStnId()) {
							_tempData.dptTrainNbr = self.dptTrainNbr();
							
							isAdd = false;
							break;
						}
					}
					if (isAdd) {
						_trainUpdateList.push(self.data);
					}
				};
				self.onPlatFormChange = function(n, event){
					self.isChangeValue(1);
					
					var isAdd = true;	//是否将该行记录增加到需要保存数组中
					for (var i=0;i<_trainUpdateList.length;i++) {
						var _tempData = _trainUpdateList[i];
						if (_tempData.planTrainStnId == self.planTrainStnId()) {
							_tempData.platForm = self.platForm();
							
							isAdd = false;
							break;
						}
					}
					if (isAdd) {
						_trainUpdateList.push(self.data);
					}
				};
			
			//出发时间
			self.deptTimeOnblur = function(){
				self.setColor(0);
				$('#isSave').removeAttr("disabled");
				var arrT = self.arrTime();//到达时间
				var dptT = self.dptTime();//出发时间
				if(arrT!=null && arrT!=undefined && dptT!=null && dptT!=undefined){
					self.stepStr(GetDateDiff2(arrT,dptT));
					self.arrRunDays(getDaysBetween(moment(arrT).format('YYYY-MM-DD'),moment(dptT).format('YYYY-MM-DD')));
					self.runDays(getDaysBetween(moment(arrT).format('YYYY-MM-DD'),moment(dptT).format('YYYY-MM-DD')));
					if(arrT!=null && arrT!="" && arrT!=undefined && dptT!=null && dptT!="" && dptT!=undefined){
						if(arrT > dptT){
							showErrorDialog("到达时间不能大于出发时间!");
							$("#isSave").attr('disabled',true);
							self.setColor(1);
							return ;
						}
					}
				
				}
				for(var i=0;i<_self.trainStns().length;i++){
					if(self.index()==_self.trainStns()[i].index()){
						var obj = _self.trainStns()[i];
//						alert("obj.index()=="+obj.index());
//						alert("obj.arrTime()=="+obj.arrTime());
//						alert("obj.dptTime()=="+obj.dptTime());
//						var upobj = _self.trainStns()[i+1];
						var downobj = _self.trainStns()[i+1];
						if(obj!=null && obj!=undefined && downobj!=null && downobj!=undefined){
							if(obj.dptTime()!="" && obj.dptTime()!=undefined && downobj.arrTime()!="" && downobj.arrTime()!=undefined){
								if(obj.dptTime() > downobj.arrTime()){
									showErrorDialog("下一站到达时间不能大于上一站出发时间!");
									$("#isSave").attr('disabled',true);
									self.setColor(1);
									return ;
								}
							}	
						}
				
					
					}
				}
			};
			
		};
		
	
		
		
		

		
		
		self.stnNameTemp = ko.computed({
	        read: function () {
	        	if(self.stnName==null || self.stnName==undefined || self.stnName=='null' || self.stnName=='"null"'){
	        		return "";
	        	}else{
	        		return self.stnName();	
	        	}
	        },
	        write: function (value) {
	        	self.currentBureauJc("");//清除当前路局值
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
										self.currentBureauJc(result.data[i].STNBUREAUSHORTNAME);//路局简称
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
							showWarningDialog("当前车次客运经路数据不存在。");
							return;
						}
						
						var message = "车次："+$("#trainRunTime_trainNbr_hidden").val()+"&nbsp;&nbsp;&nbsp;&nbsp;"+
						"始发日期："+$("#trainRunTime_runDate_hidden").val()+
						"&nbsp;&nbsp;&nbsp;&nbsp;" + $("#trainRunTime_startStn_hidden").val() + "&nbsp;→&nbsp;" +  $("#trainRunTime_endStn_hidden").val();
						_self.currentTrainInfoMessage(message);
						$.each(result.data, function(i, obj){
							obj.index = i;	//增加元素在集合中的序号属性，便于调整顺序
							_self.trainStns.push(new _self.TrainTimeRow(obj)); 
//							console.log(ko.toJSON(_self.trainStns()));
						});

			            // 表头固定
			            //$("#div_form").freezeHeader();
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
		 * 是否更改过值
		 */
		_self.checkValueRepeat = function() {
			console.log("进入checkValueRepeat");
			checkRepeat = false;
		};
		
		
		/**
		 * 保存
		 */
		_self.savePathWay = function() {
//			if(checkRepeat){
//				showWarningDialog("操作取消！当前车次客运时刻未做任何改动。");
//				return;
//			}
			var isNull = false;
			if(_self.runRuleOption().code == "3" && (_self.selectedDate()==null||_self.selectedDate()=="")) {//择日
				showWarningDialog("无效操作，请先生成择日内容。");
				return;
			}
			if(!_self.checkSelectEndDate()) {//校验截至日期是否有效
				//截至日期 必须<=最大开行日期
				return;
			}
			var _saveTrainStnArray = [];
			$.each(_self.trainStns(),function(n, obj){
				var z=n+1;
//				obj.stnName=$("#"+z+"jy").val();
//				obj.stnNameTemp=$("#"+z+"jy").val();
//				obj.currentBureauJc=$("#"+z+"jy100").find("option:selected").text();
				var bsn = $("#"+z+"jy100").find("option:selected").text();//获取页面下拉框的值
				obj.bureauShortName= bsn;
				if(obj.nodeName()=="" || obj.nodeName()==undefined || obj.nodeName()==null){
					showErrorDialog("站名不能为空");
					isNull = true;
					return false;
				}
				if(bsn==null || bsn=="" || bsn==undefined){
					showErrorDialog("路局不能为空");
					isNull = true;
					return false;
				}
				if(obj.arrTime()=="" || obj.arrTime()==undefined || obj.arrTime()==""){
					showErrorDialog("到达时间不能为空");
					isNull = true;
					return false;
				}
				if(obj.dptTime()=="" || obj.dptTime()==undefined || obj.dptTime()==""){
					showErrorDialog("出发时间不能为空");
					isNull = true;
					return false;
				}
				
				_saveTrainStnArray.push(ko.toJSON(obj));
//				console.log("obj>>>>>>>>>>>>"+ko.toJSON(obj));
			});
			var tName = $("#textarea_trainRunTime_telName").val();
			if(tName!=null && tName!=undefined){
				if(tName.length > 40){
					showWarningDialog("调整依据内容不能大于40字符");
					return ;
				}
			}
			var s = $("#input_trainRunTime_startDate").val();
			var e = $("#input_trainRunTime_endDate").val();
			if(s  > e ){
				showWarningDialog("开始日期不能大于截止日期");
		    	return ;
			}
			if(isNull){
				return ;
			}
			if(_self.trainStns().length > 0){
				for(var i =0;i<_self.trainStns().length;i++){
					var obj = _self.trainStns()[i];
					var uobj =null;
					var nobj = null;
					if(i!=0){
						uobj = _self.trainStns()[i-1];
					}
					if(nobj!= _self.trainStns().length-1){
						nobj = _self.trainStns()[i+1];
					}
					if(obj.arrTime() > obj.dptTime()){
						showErrorDialog("同一站的到达时间不能大于出发时间");
				    	return ;
					}
					if(obj!=null && obj!=undefined && uobj!=null && uobj!=undefined){
						if(obj.arrTime() < uobj.dptTime()){
							obj.setColor(1);//设置颜色
							showErrorDialog("上一站的出发时间不能大于下一站的到达时间");
					    	return ;
						}
					}
					if(obj!=null && obj!=undefined && nobj!=null && nobj!=undefined){
						if(obj.dptTime() > nobj.arrTime()){
							obj.setColor(1);//设置颜色
							showErrorDialog("上一站的出发时间不能大于下一站的到达时间");
					    	return ;
						}
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
					isCmdEnd: _self.cmdEndFlag()==true? "1":"0",
					selectedDate : _self.selectedDate(),
					planTrainId: $("#trainRunTime_trainPlanId_hidden").val(),
//					times: _trainUpdateList
					times:_saveTrainStnArray
				};
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/jbtcx/editPlanLineTrainPathWay",
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
						checkRepeat = true;
					} else if(result != null && result != "undefind" && result.code == "01") {
						showErrorDialog(result.message);
					}  else {
						showErrorDialog(result.message);
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
		 * 上移
		 */
		_self.up = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().index()<0) {
				showWarningDialog("请选择时刻表中需要调整顺序的记录");
				return;
			}
			
			if(_self.currentCmdTrainStn().index() == 0) {
				showWarningDialog("当前记录顺序号已经为最小，不能执行上移操作");
				return;
			}
			
			
			//设置序号
			var _arrayList = _self.trainStns();
			for(var i=0;i<_arrayList.length;i++) {
				if(i == (_self.currentCmdTrainStn().index())) {
					
					_arrayList[i].index(i-1);
					_arrayList[i-1].index(i);
					_self.trainStns.splice(i - 1, 2, _arrayList[i], _arrayList[i-1]);
					break;
				}
			}
			checkRepeat = false;
		};
		
		
		
		/**
		 * 下移
		 */
		_self.down = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().index()<0) {
				showWarningDialog("请选择时刻表中需要调整顺序的记录");
				return;
			}

			var _maxLen = _self.trainStns().length -1;
			if(_self.currentCmdTrainStn().index() == _maxLen) {
				showWarningDialog("当前记录顺序号已经为最大，不能执行下移操作");
				return;
			}
			
			//设置序号
			var _arrayList = _self.trainStns();
			for(var i=0;i<_arrayList.length;i++) {
				if(i == (_self.currentCmdTrainStn().index())) {
					
					_arrayList[i].index(i+1);
					_arrayList[i+1].index(i);
					_self.trainStns.splice(i , 2, _arrayList[i+1], _arrayList[i]);
					break;
				}
			}
			checkRepeat = false;
		};
		
		/**
		 * 插入行
		 * 追加到当前选中行之前
		 */
		_self.insertTrainStnRow = function() {
			var _maxRowLen = _self.trainStns().length;//追加记录前集合大小

			//1.当_maxRowLen==0时，直接push
			if (_maxRowLen == 0) {
				_self.trainStns.push(new _self.TrainTimeRow({
					index:0,
					stnName:"",
					arrTrainNbr:"",
					dptTrainNbr:"",
					bureauShortName:"",
					arrTime:"",
					dptTime:"",
					trackName:"",
					platform:"",
					arrRunDays:0,
					dptRunDays:0,
					nodeId:"",
					jobs:"",
					nodeName:"",
					bureauId:"",
					isChangeValue:1,
					nodeStationId:"",
					nodeStationName:"",
					nodeTdcsId:"",
					nodeTdcsName:"",
					planTrainStnId:"",
					runDays:0,
					stationFlag:"BTZ"
					
				}));
				return;
			}
			
			
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
				|| _self.currentCmdTrainStn().index()<0) {
				showWarningDialog("请选择时刻表中需要插入记录的位置");
				return;
			}

			//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
			_self.trainStns.splice(_self.currentCmdTrainStn().index(), 0, new _self.TrainTimeRow({
				index:_self.currentCmdTrainStn().index(),
				stnName:"",
				arrTrainNbr:"",
				dptTrainNbr:"",
				bureauShortName:"",
				arrTime:"",
				dptTime:"",
				trackName:"",
				platform:"",
				arrRunDays:0,
				dptRunDays:0,
				nodeId:_self.currentCmdTrainStn().nodeId()==null?"":_self.currentCmdTrainStn().nodeId(),
				jobs:"",
				nodeName:"",
				bureauId:"",
				isChangeValue:1,
				nodeStationId:"",
				nodeStationName:"",
				nodeTdcsId:"",
				nodeTdcsName:"",
				planTrainStnId:"",
				runDays:0,
				stationFlag:"BTZ"
				
				
			}));

			//2.2.设置序号
			for(var i=0;i<_self.trainStns().length;i++) {
				if(i==_self.currentCmdTrainStn().index()){
					_self.trainStns()[i].index(_self.currentCmdTrainStn().index());
				}
				if(i > (_self.currentCmdTrainStn().index())) {//从新插入记录位置开始index  + 1
					_self.trainStns()[i].index(_self.trainStns()[i].index()+1);//当前行位置后的所有记录index加1
				}
				_self.trainStns()[i].isChangeValue(1);
			}
			checkRepeat = false;
		};
		
		/**
		 * 追加行
		 * 追加到当前选中行之后
		 */
		_self.addTrainStnRow = function() {
			var _maxRowLen = _self.trainStns().length;//追加记录前集合大小

			//1.当_maxRowLen==0时，直接push
			if (_maxRowLen == 0 || _self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
				|| _self.currentCmdTrainStn().index()<0) {
				_self.trainStns.push(new _self.TrainTimeRow({
					index:_self.trainStns().length,//0
					stnName:"",
					arrTrainNbr:"",
					dptTrainNbr:"",
					bureauShortName:"",
					arrTime:"",
					dptTime:"",
					trackName:"",
					platform:"",
					arrRunDays:0,
					dptRunDays:0,
					nodeId:"",
					jobs:"",
					nodeName:"",
					bureauId:"",
					isChangeValue:1,
					nodeStationId:"",
					nodeStationName:"",
					nodeTdcsId:"",
					nodeTdcsName:"",
					planTrainStnId:"",
					runDays:0,
					stationFlag:"BTZ"
				}));
				return;
			}
			
			
//			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
//				|| _self.currentCmdTrainStn().index()<0) {
//				showWarningDialog("请选择时刻表中需要追加记录的位置");
//				return;
//			}
			//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
			_self.trainStns.splice(_self.currentCmdTrainStn().index()+1, 0, new _self.TrainTimeRow({
				index:_self.currentCmdTrainStn().index()+1,
				stnName:"",
				arrTrainNbr:"",
				dptTrainNbr:"",
				bureauShortName:"",
				arrTime:"",
				dptTime:"",
				trackName:"",
				platform:"",
				arrRunDays:0,
				dptRunDays:0,
				nodeId:_self.currentCmdTrainStn().nodeId()==null?"":_self.currentCmdTrainStn().nodeId(),
				jobs:"",
				nodeName:"",
				bureauId:"",
				isChangeValue:1,
				nodeStationId:"",
				nodeStationName:"",
				nodeTdcsId:"",
				nodeTdcsName:"",
				planTrainStnId:"",
				runDays:0,
				stationFlag:"BTZ"
			}));

			//2.2.设置序号
//			if(_self.currentCmdTrainStn().index() != _maxRowLen-1) {//当前选中行不为增加前集合中最后一行   则移除后集合需要重新排序
				for(var i=0;i<_self.trainStns().length;i++) {
					if(i==_self.currentCmdTrainStn().index()+1){
						_self.trainStns()[i].index(_self.currentCmdTrainStn().index()+1);//为追加行设置index值
					}
					if(i > (_self.currentCmdTrainStn().index()+1)) {//从新插入记录位置开始index  + 1
						_self.trainStns()[i].index(_self.trainStns()[i].index()+1);//当前行位置后的所有记录index加1
					}
				}
//			}
			checkRepeat = false;
			
		};
		
		
		/**
		 * 清除时刻列表当前选中值
		 */
		_self.cleanCurrentCmdTrainStn = function() {
			_self.currentCmdTrainStn(new _self.TrainTimeRow({
				index:-1,
				stnName:"",
				arrTrainNbr:"",
				dptTrainNbr:"",
				bureauShortName:"",
				arrTime:"",
				dptTime:"",
				trackName:"",
				platform:"",
				arrRunDays:0,
				dptRunDays:0,
				nodeId:"",
				jobs:"",
				nodeName:"",
				bureauId:"",
				isChangeValue:1,
				nodeStationId:"",
				nodeStationName:"",
				nodeTdcsId:"",
				nodeTdcsName:"",
				planTrainStnId:"",
				runDays:0,
				stationFlag:"BTZ"
					
		}));
		};
		
		/**
		 * 时刻列表行点击事件
		 */
		_self.setCMDTrainStnCurrentRec = function(row) {
			_self.currentCmdTrainStn(row);
		};
		
		

		/**
		 * 删除行
		 */
		_self.deleteTrainStnRow = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().index()<0) {
				showWarningDialog("请选择时刻表中需要移除的记录");
				return;
			}
			var _maxRowLen = _self.trainStns().length;
			//从集合中移除
			_self.trainStns.remove(_self.currentCmdTrainStn());
			if(_self.currentCmdTrainStn().index() != _maxRowLen-1) {//移除行不为移除前集合中最后一行   则移除后集合需要重新排序
				//重新设置index
				$.each(_self.trainStns(),function(n, obj){
					obj.index(n);
				});
			}
			//清除当前选中项值
			_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
			checkRepeat = false;
		};
		
		_self.currentCmdTrainStn = ko.observable(new _self.TrainTimeRow({
			index:-1,
			stnName:"",
			arrTrainNbr:"",
			dptTrainNbr:"",
			stnBureau:"",
			arrTime:"",
			dptTime:"",
			trackName:"",
			platform:"",
			arrRunDays:0,
			dptRunDays:0,
			nodeId:"",
			jobs:"",
			nodeName:"",
			bureauId:"",
			isChangeValue:0,
			nodeStationId:"",
			nodeStationName:"",
			nodeTdcsId:"",
			nodeTdcsName:"",
			planTrainStnId:"",
			runDays:0,
			stationFlag:"BTZ",
			jobItems:[],
			jobItemsText:"",
			kyyy:"",
			job: {
                code: "",
                items: [],
                itemsText: ""
            }
		}));//列车时刻表选中行记录  用于上下移动
		
		
		/**
		 * 时刻列表行点击事件
		 */
	/*	_self.setTrainLineTimesCurrentRec = function(row) {
			_self.newTrainLineTimesRow(row);
		};
		*/
		
		/**
		 * 更新站点作业信息
		 */
		_self.editJobs = function(row) {
			//1.清除数据
			_self.setCMDTrainStnCurrentRec(row);
			
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
				if($.inArray($(this).val(), _self.currentCmdTrainStn().jobItems()) > -1) {
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
			var jobsTypeSubstring = _self.currentCmdTrainStn().jobs().substring(1,3);
			_items.unshift(jobsTypeSubstring);
//			_self.currentCmdTrainStn().job.items = _items;
			_self.currentCmdTrainStn().jobItems(_items);
			
			$.each(_self.trainStns(), function(i, obj){
				if(obj.planTrainStnId()==_self.currentCmdTrainStn().planTrainStnId()){
//					obj.data.job.items = _items;
					obj.jobItems(_items);
					obj.jobs('<'+_items.join('><')+'>');
				}
			});
		};
		

		
	};
	
	
	
	
	
	
	/**
	 * 格式化字符串
	 * 
	 * 将 null转换为""
	 * @param str
	 */
	function formatString(str) {
		if(str == null || str=="null") {
			return "";
		}
		return str;
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
	


	function filterTimeValue(value){
		return (value == null || value == "null" || value =="" )? "--" : value;
	};
	
	function filterValue(value){
		return value == null || value == "null" ? "" : value;
	};
	
	
};

/**
 * 是否更改过值
 */
 function checkValueRepeat(){
	//console.log("进入checkValueRepeat");
	checkRepeat = false;
};


$(function() {
	new TrainRunTimePage().initPage();
});

