/**
 * 临客命令js
 * 
 */

var runPlanLkCmdPageModel = null;//界面绑定元素对象
var runPlanLkCMDTrainStnArray =[];
var checkRepeat = false;
var isAdd = false;
var isCR = false;
var isZJ = false;
// 点击行,保存的id
var cmdTrainId = null;
var selectState = 0;
var RunPlanLkCmdPage = function () {
	var cmdTypeArrayBjdd = [{"code": "1", "text": "既有加开命令"},
	                        {"code": "5", "text": "既有加开文电"}
	                    ];
	var cmdTypeArrayWjdd = [{"code": "1", "text": "既有加开命令"},
	                        {"code": "5", "text": "既有加开文电"}
		                    ];
	var wdCmdRuleArray = [{"code": "1", "text": "每日"},{"code": "2", "text": "隔日"}];//文电加开 开行规律下拉框选择列表数据
	
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		$("#jbt_fill_site_dialog").dialog("close");//关闭选线窗口
		$("#jbt_traininfo_dialog").dialog("close");//关闭选线窗口
		$("#his_traininfo_dialog").dialog("close");//关闭选线窗口
		$("#runPlanLk_cmd_input_startDate").datepicker();
		$("#runPlanLk_cmd_input_endDate").datepicker();
		$("#runPlanLk_cmd_input_startDate_wjdd").datepicker();//外局担当
		$("#runPlanLk_cmd_input_endDate_wjdd").datepicker();//外局担当
		

		$("#input_wd_cmdTime").datepicker();//文电日期输入框
		$("#input_wd_startDate").datepicker();//文电起始日期输入框
		$("#input_wd_endDate").datepicker();//文电终止日期输入框

		
		//锁定命令列表table表头
//		$("#bjdd_jk_runPlanLkCMD_table").chromatable({
//            width: "100%", // specify 100%, auto, or a fixed pixel amount
//            height: "620px",
//            scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
//        });
//		//锁定时刻表列表table表头
//		$("#runPlanLkCMD_trainStn_table").chromatable({
//            width: "100%", // specify 100%, auto, or a fixed pixel amount
//            height: "660px",
//            scrolling: "yes" // must have the jquery-1.3.2.min.js script installed to use
//        });
		
		
		//页面元素绑定
		runPlanLkCmdPageModel = new PageModel();
		ko.applyBindings(runPlanLkCmdPageModel);
		
//		runPlanLkCmdPageModel.initPageData();
		runPlanLkCmdPageModel.getFullNodeList();
		
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
		_self.cmdTypeArrayBjdd = ko.observableArray(cmdTypeArrayBjdd);		//本局担当命令类型下拉框
		_self.cmdTypeOptionBjdd = ko.observable();		//本局担当命令类型下拉框选项 (既有加开；既有停运；高铁加开；高铁停运)
		_self.cmdTypeArrayWjdd = ko.observableArray(cmdTypeArrayWjdd);		//外局担当命令类型下拉框
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
		
		
		

		/**
		 * 初始化查询条件值
		 */
		_self.initData = function() {
//			commonJsScreenLock(1);
			$("#runPlanLk_cmd_input_startDate").val(moment().subtract("day", 1).format('YYYY-MM-DD'));
			$("#runPlanLk_cmd_input_endDate").val(moment().format('YYYY-MM-DD'));

			$("#runPlanLk_cmd_input_startDate_wjdd").val(moment().subtract("day", 1).format('YYYY-MM-DD'));//外局担当
			$("#runPlanLk_cmd_input_endDate_wjdd").val(moment().format('YYYY-MM-DD'));//外局担当

			
			_self.loadBureauData();//加载路局下拉框数据
			
//			_self.loadStnData();//加载车站字典数据
		};
		
		
		

		_self.initData();//初始化查询条件
		
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
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		_self.searchModle = ko.observable(new SearchModle());		//页面查询对象
		_self.runPlanLkCMDRows = ko.observableArray();				//页面命令列表
		_self.runPlanLkCMDTrainStnRows = ko.observableArray();		//页面命令时刻列表
		_self.selectState = ko.observable(0);		//页面命令时刻列表
		_self.runPlanLkTrainStnRows = ko.observableArray();		//页面命令时刻列表
	  _self.stnNameTemps = ko.observableArray();	//站名
		
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
    								_self.stnNameTemps.push(obj.pinyinInitials+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+obj.name+'['+obj.bureauShortName+']');
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
		 * 是否更改过值
		 */
		_self.checkValueRepeat = function() {
			checkRepeat = false;
			
		};
        
		/**
		 * 时刻表列表行对象
		 * @param data
		 */
		_self.cmdTrainStnTimeRow = function(data) {
			var self = this;
			var _regexStr = "(^([0-1]\\d|2[0-3])\:([0-5]\\d)\:([0-5]\\d)$|^([0-1]\\d|2[0-3])\:([0-5]\\d)$|^(\\d{4}$)|^(\\d{6}$))";//23:59:59   00:59
			var _regex = new RegExp(_regexStr);
			
			self.kyyyOptions = ko.observableArray([{code:"n",text:""},{code:"y",text:"是"}]);//客运营业下拉框数据
			
			// 新增字段
//			self.node_Station_Id = ko.observable(data.nodeStationId);
//			self.node_Station_Name = ko.observable(data.nodeStationName);
			self.stnId = ko.observable(data.nodeStationId);
			// 站名更为:nodeStationName 依据:20141223 新旧结构对比
			self.stnName = ko.observable(data.nodeStationName);
			self.stnNameTemp = ko.observable(data.nodeStationName);
			self.node_Tdcs_Id = ko.observable(data.nodeTdcsId);
			self.node_Tdcs_Name = ko.observable(data.nodeTdcsName);
			
			self.nodeId = ko.observable(data.nodeId);
			self.jobs = ko.observable(data.jobs);
			self.nodeName = ko.observable(data.nodeName);
			self.bureauId = ko.observable(data.bureauId);
			self.childIndex = ko.observable(data.childIndex);
//			self.stnName = ko.observable(data.stnName);
//			self.stnNameTemp = ko.observable(data.stnName);
			self.setColor = ko.observable(0);
			
			self.arrTrainNbr = ko.observable(formatString(data.arrTrainNbr));
			self.dptTrainNbr = ko.observable(formatString(data.dptTrainNbr));
//			self.arrTime  =  ko.observable(data.arrTime);
//			self.dptTime = ko.observable(data.dptTime);

			self.arrTime = ko.observable(data.arrTime=='null' || data.arrTime=='"null"'?"":data.arrTime).extend({
	            required: { params: true, message: "请输入到达时间" },
	            pattern: {
	                params: _regexStr,
	                message: "格式不正确"//00:59  23:59:59
	            }
	        });
			self.dptTime = ko.observable(data.dptTime=='null'||data.dptTime=='"null"'?"":data.dptTime).extend({
	            required: { params: true, message: "请输入出发时间" },
	            pattern: {
	                params: _regexStr,
	                message: "格式不正确"//00:59  23:59:59
	            }
	        });
			self.stepStr = ko.observable(self.arrTime()==self.dptTime()?"0":"1");
			//车站名称自动补全
			self.stnNameTempOnfocus = function(n, event){
				$(event.target).autocomplete(runPlanLkCmdPageModel.stnNameTemps(),{
					max: 50,    	//列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 500,   	//提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false,    	//是否自动填充
					formatResult: function(row) {
						var name =null;
						if(row!=null && row!=undefined){
							var str = new String(row);
							name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));//此处进行了bug修改和优化
						}
//						console.log('name='+name);
						return name;//这个name就是显示到
					}
		       }).result(function(event, stnNameTemp, formatted) {
		    	   if(stnNameTemp!=null && stnNameTemp!=undefined){
		    		   var str = new String(stnNameTemp);
//		    		   console.log('str='+str);
		    		   var name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));
		    		   var bureauShortName = str.substring(str.indexOf("[")+1, str.indexOf("]"));
//		    		   console.log('name='+name);
//		    		   console.log('bureauShortName='+bureauShortName);
		    		   self.stnNameTemp(name);//赋值
//		    		   self.nodeId(id);
		    		   self.nodeName(name);//赋值
		    		   self.currentBureauJc(bureauShortName);
		    	   }
		    	   
		    	   
		       });
			};
				
			self.trackName = ko.observable(formatString(data.trackName));
			self.platform = ko.observable(data.platform=="0"?"":formatString(data.platform));//站台
			self.arrRunDays = ko.observable(data.arrRunDays);//到达运行天数
			self.dptRunDays = ko.observable(data.dptRunDays);//出发运行天数
			self.currentBureauJc = ko.observable(data.stnBureau);//当前行路局简称
			
			var jobsText = typeof data.jobs=='object'?'':data.jobs;
			jobsText = jobsText.replace('<客运营业>','').replace('<经由>','');
			jobsText = jobsText.substring(1,jobsText.length-1);
			self.jobItems = ko.observableArray(typeof data.jobs=="string"?jobsText.split("><"):[]);
			self.jobItemsText = ko.computed(function () {
					return self.jobItems().join(",");
			}, this);
			
			//---
			//客运营业下拉框选择值
			var _val = "";
			if(data.jobs.indexOf("始发")>-1 || data.jobs.indexOf("终到")>-1) {
//				if(data.jobs.indexOf("始发")>-1 || data.jobs.indexOf("终到")>-1 || self.stepStr() !="0") {
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
				$.each(_self.runPlanLkCMDTrainStnRows(), function(i, obj){
					if(obj.childIndex()== self.childIndex()){
						obj.kyyy(self.kyyy())
					}
				});
			};
			
			//---
			
			
			//选线 且 套用经由及时刻则沿用选线运行天数
//			if (typeof data.isSelectLine !=undefined && data.isSelectLine ==1) {
//				self.arrRunDays = ko.observable(data.arrRunDays);
//				self.dptRunDays = ko.observable(data.dptRunDays);
//				
//			} else {//非选线
				//******************计算运行天数会导致保存时 会提交自动计算结果，无法提交用户输入值   故而暂不启用******************//
//				//到达运行天数
//				self.arrRunDays = ko.computed(function () {
//					if (_self.runPlanLkCMDTrainStnRows().length > 0) {
//						if (self.childIndex() <= 0 || self.arrTime()=="") {//始发站到达运行日期为0
//							return 0;
//						}
//
////						if (!/^[0-9]\d*$/.test(self.arrTime().replace(":", "").replace("：", ""))) {
////							showWarningDialog("到达时刻或出发时刻格式不正确，正确格式如“23:57”或“23:57:48”。若为始发站则到达时刻和出发时刻填同一值");
////							return "";
////						}
//						
//						var parent = _self.runPlanLkCMDTrainStnRows()[self.childIndex()-1];
//						if (parent.dptTime()!=null && parent.dptTime()!="undefined" && parent.dptTime()!="" && self.arrTime()<parent.dptTime()) {//到达时刻小于上一条记录的出发时刻  视为跨天 则返回上一条记录的出发运行天数+1
//							return parent.dptRunDays() +1;
//						} else {
//							return parent.dptRunDays();
//						}
//					}
//					return 0;//缺省值为0
//				});
				
//				//出发运行天数
//				self.dptRunDays = ko.computed(function () {
//					if (_self.runPlanLkCMDTrainStnRows().length > 0) {
//						if (self.childIndex() <= 0 || self.dptTime()=="") {//始发站到达运行日期为0
//							return 0;
//						}
//						
//
////						if (!/^[0-9]\d*$/.test(self.dptTime().replace(":", "").replace("：", ""))) {
////							showWarningDialog("到达时刻或出发时刻格式不正确，正确格式如“23:57”或“23:57:48”。若为始发站则到达时刻和出发时刻填同一值");
////							return "";
////						}
//						
//						if (self.arrTime()!="" && self.dptTime()<self.arrTime()) {//出发时刻小于到达时刻  视为跨天 则+1
//							return self.arrRunDays() +1;
//						} else {
//							return self.arrRunDays();
//						}
//					}
//					return 0;//缺省值为0
//				});
//				
//
//				
//				
//				
//			}
//			-------------是否选线判断结束

			self.formatDate = function(){
				var _arrTime = self.arrTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
				var _dptTime = self.dptTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
				
				if (_arrTime != "") {
					if (_arrTime.indexOf(":") < 0) {
						if(_arrTime.length == 4){//1259格式化为12:59:00
							_arrTime = _arrTime.substring(0, 2) +":"+_arrTime.substring(2, 4)+":00";
						} else if(_arrTime.length == 6){//125910格式化为12:59:10
							_arrTime = _arrTime.substring(0, 2) +":"+_arrTime.substring(2, 4)+":"+_arrTime.substring(4, 6);
						}
					} else if (_arrTime.split(":").length == 2) {//12:59格式化为12:59:00
						_arrTime = _arrTime+":00";
					}
					self.arrTime(_arrTime);
					if(!_regex.test(_dptTime)){
						self.dptTime(_arrTime);
					}
				}
			};
			
			self.formatDateByDptTime = function(){
				var _arrTime = self.arrTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
				var _dptTime = self.dptTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
				
				if (_dptTime != "") {
					if (_dptTime.indexOf(":") < 0) {
						if(_dptTime.length == 4){//1259格式化为12:59:00
							_dptTime = _dptTime.substring(0, 2) +":"+_dptTime.substring(2, 4)+":00";
						} else if(_dptTime.length == 6){//125910格式化为12:59:10
							_dptTime = _dptTime.substring(0, 2) +":"+_dptTime.substring(2, 4)+":"+_dptTime.substring(4, 6);
						}
					} else if (_dptTime.split(":").length == 2) {//12:59格式化为12:59:00
						_dptTime = _dptTime+":00";
					}
					self.dptTime(_dptTime);
				}
			};
			self.stnNameKeyUp =function(n, event){
				//键盘回车键enter
				if(event.keyCode == 13){ 
					$(event.target).parent().nextAll().find("input")[0].focus();
					$(event.target).parent().nextAll().find("input")[0].select();
				} 
				//键盘PgUp
				if(event.keyCode == 33){
					if ($(event.target).parent().parent().prev().length >= 1) { 
						$(event.target).parent().parent().prev().children().children().eq($(event.target).parent().prevAll().length-1).focus(); 
						$(event.target).parent().parent().prev().children().children().eq($(event.target).parent().prevAll().length-1).select();
					} 
				}
				//键盘PgDn
				if(event.keyCode == 34){
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().children().eq($(event.target).parent().prevAll().length-1).focus(); 
						$(event.target).parent().parent().next().children().children().eq($(event.target).parent().prevAll().length-1).select();
					}
				}
            };
			self.arrTimeKeyUp =function(n, event){
				if(event.keyCode == 13){ 
					self.formatDate();
					$(event.target).parent().next().find("input")[0].focus();
					$(event.target).parent().next().find("input")[0].select();
				} 
				//键盘PgUp
				if(event.keyCode == 33){
					if ($(event.target).parent().parent().prev().length >= 1) { 
						$(event.target).parent().parent().prev().children().children().eq($(event.target).parent().prevAll().length-1).focus(); 
						$(event.target).parent().parent().prev().children().children().eq($(event.target).parent().prevAll().length-1).select();
					} 
				}
				//键盘PgDn
				if(event.keyCode == 34){
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().children().eq($(event.target).parent().prevAll().length-1).focus(); 
						$(event.target).parent().parent().next().children().children().eq($(event.target).parent().prevAll().length-1).select();
					}
				}
            };
			self.dptTimeKeyUp = function(n, event){
				if(event.keyCode == 13){ 
					self.formatDateByDptTime();
					$(event.target).parent().next().find("input")[0].focus();
					$(event.target).parent().next().find("input")[0].select();				
				} 
				//键盘PgUp
				if(event.keyCode == 33){
					if ($(event.target).parent().parent().prev().length >= 1) { 
						$(event.target).parent().parent().prev().children().find("input")[2].focus();
						$(event.target).parent().parent().prev().children().find("input")[2].select();			
					} 
				}
				//键盘PgDn
				if(event.keyCode == 34){
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().find("input")[2].focus();
						$(event.target).parent().parent().next().children().find("input")[2].select();			
					}
				}
            };
            self.arrKeyUp = function(n, event){
				if(event.keyCode == 13){ 
					$(event.target).parent().next().find("input")[0].focus();
					$(event.target).parent().next().find("input")[0].select();				
				} 
				//键盘PgUp
				if(event.keyCode == 33){
					if ($(event.target).parent().parent().prev().length >= 1) { 
						$(event.target).parent().parent().prev().children().find("input")[3].focus();
						$(event.target).parent().parent().prev().children().find("input")[3].select();		
					} 
				}
				//键盘PgDn
				if(event.keyCode == 34){
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().find("input")[3].focus();
						$(event.target).parent().parent().next().children().find("input")[3].select();	
					}
				}
            };
            self.dptKeyUp = function(n, event){
				if(event.keyCode == 13){ 
					$(event.target).parent().next().find("input")[0].focus();
					$(event.target).parent().next().find("input")[0].select();		
				} 
				//键盘PgUp
				if(event.keyCode == 33){
					if ($(event.target).parent().parent().prev().length >= 1) { 
						$(event.target).parent().parent().prev().children().find("input")[4].focus();
						$(event.target).parent().parent().prev().children().find("input")[4].select();	
					} 
				}
				//键盘PgDn
				if(event.keyCode == 34){
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().find("input")[4].focus();
						$(event.target).parent().parent().next().children().find("input")[4].select();	
					}
				}
            };
            self.trackKeyUp = function(n, event){
				if(event.keyCode == 13){ 
					$(event.target).parent().next().find("input")[0].focus();
					$(event.target).parent().next().find("input")[0].select();					
				} 
				//键盘PgUp
				if(event.keyCode == 33){
					if ($(event.target).parent().parent().prev().length >= 1) { 
						$(event.target).parent().parent().prev().children().find("input")[5].focus();
						$(event.target).parent().parent().prev().children().find("input")[5].select();	
					} 
				}
				//键盘PgDn
				if(event.keyCode == 34){
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().find("input")[5].focus();
						$(event.target).parent().parent().next().children().find("input")[5].select();	
					}
				}
            };
            
            self.platformKeyUp = function(n, event){
				if(event.keyCode == 13){ 
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().children().eq(0).focus(); 
						$(event.target).parent().parent().next().children().children().eq(0).select();
					}
				} 
				//键盘PgUp
				if(event.keyCode == 33){
					if ($(event.target).parent().parent().prev().length >= 1) { 
						$(event.target).parent().parent().prev().children().find("input")[6].focus();
						$(event.target).parent().parent().prev().children().find("input")[6].select();	
					} 
				}
				//键盘PgDn
				if(event.keyCode == 34){
					if ($(event.target).parent().parent().next().length >= 1) { 
						$(event.target).parent().parent().next().children().find("input")[6].focus();
						$(event.target).parent().parent().next().children().find("input")[6].select();	
					}
				}
            };
            
			self.arrRunDaysOnblur = function(n, event){  
				var childIndex = self.childIndex();
				var len = _self.runPlanLkCMDTrainStnRows().length;
				self.setColor(0);
				$('#isSave').removeAttr("disabled");
				$('#isSave2').removeAttr("disabled");
				var days = self.arrRunDays();
				for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
					var obj = _self.runPlanLkCMDTrainStnRows()[i];
					var upobj = _self.runPlanLkCMDTrainStnRows()[i-1];
					var npobj = _self.runPlanLkCMDTrainStnRows()[i+1];
					if(childIndex!=0 && childIndex!=len-1){
						if(days!=="--" && days!=null && days!=undefined  && obj.arrRunDays() >=1){//输入零的情况
							if(obj.arrTime() < upobj.dptTime()){
								$('#isSave').removeAttr("disabled");
								$('#isSave2').removeAttr("disabled");
							}
							
						}
//						if(days!=="--" && days!=null && days!=undefined && obj.arrRunDays() <1){
//							if(obj.arrTime() < upobj.dptTime()){
//								self.setColor(1);//设置颜色
//								$("#isSave").attr('disabled',true);
//								$("#isSave2").attr('disabled',true);
//								showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//								return ;
//							}
//						}
//						if((days!=="--" && days!=null && days!=undefined) && (parseInt(obj.arrRunDays()) > parseInt(obj.dptRunDays()) || 
//								parseInt(obj.arrRunDays()) > parseInt(npobj.arrRunDays()) || parseInt(obj.arrRunDays()) > parseInt(npobj.dptRunDays()) ) ){
//								self.setColor(2);//设置颜色
//								$("#isSave").attr('disabled',true);
//								$("#isSave2").attr('disabled',true);
//								showErrorDialog("该站的出发天数与到达天数不能大于以后站的出发天数和到达天数");
//								return ;
//							
//						}
						
					}
//					if(childIndex ==0 && childIndex==i && len >1){//第一行
//						if(obj.arrRunDays()!='--' || obj.dptRunDays()!='--'){
//							if(parseInt(obj.arrRunDays()) > parseInt(obj.dptRunDays()) || parseInt(obj.arrRunDays()) > parseInt(npobj.arrRunDays()) || parseInt(obj.arrRunDays()) > parseInt(npobj.dptRunDays()) ){
//								$("#isSave").attr('disabled',true);
//								$("#isSave2").attr('disabled',true);
//								showErrorDialog("该站的出发天数与到达天数不能大于以后站的出发天数和到达天数");
//								return ;
//							}
//							
//						}
//					}
//					if(childIndex ==i && childIndex==len-1 && len >1){//最后一行
//						if(obj.arrRunDays()!='--' || obj.dptRunDays()!='--'){
//							if(parseInt(obj.arrRunDays()) > parseInt(obj.dptRunDays()) || parseInt(obj.arrRunDays()) < parseInt(upobj.arrRunDays()) || 
//									parseInt(obj.arrRunDays()) < parseInt(upobj.dptRunDays()) || parseInt(upobj.arrRunDays()) < parseInt(obj.arrRunDays()) ||
//									parseInt(upobj.dptRunDays()) < parseInt(obj.arrRunDays()) || parseInt(upobj.arrRunDays()) < parseInt(obj.dptRunDays()) || 
//									parseInt(upobj.dptRunDays()) < parseInt(obj.dptRunDays()) ){
//								$("#isSave").attr('disabled',true);
//								$("#isSave2").attr('disabled',true);
//								showErrorDialog("该站的出发天数与到达天数不能大于以后站的出发天数和到达天数");
//								return ;
//							}
//							
//						}
//					}
					
				}
				
			};
			
			
			self.dptRunDaysOnblur = function(n, event){
				var childIndex = self.childIndex();
				var len = _self.runPlanLkCMDTrainStnRows().length;
				self.setColor(0);
				$('#isSave').removeAttr("disabled");
				$('#isSave2').removeAttr("disabled");
				var days = self.dptRunDays();
				for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
					var obj = _self.runPlanLkCMDTrainStnRows()[i];
					var upobj = _self.runPlanLkCMDTrainStnRows()[i-1];
					var npobj = _self.runPlanLkCMDTrainStnRows()[i+1];
					if(childIndex!=0  && childIndex!=len-1){
						if(days!=="--" && days!=null && days!=undefined && parseInt(obj.dptRunDays()) >=1){//大于等于1
							if(obj.arrTime() < upobj.dptTime()){
								$('#isSave').removeAttr("disabled");
								$('#isSave2').removeAttr("disabled");
							}
							
						}
//						if(days!=="--" && days!=null && days!=undefined && parseInt(obj.dptRunDays()) <1){//小于1
//							if(childIndex==i && (obj.arrTime() < upobj.dptTime())){
//								self.setColor(1);//设置颜色
//								$("#isSave").attr('disabled',true);
//								$("#isSave2").attr('disabled',true);
//								showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//								return ;
//							}
//						}
//						if((days!=="--" && days!=null && days!=undefined) && childIndex==i && (parseInt(obj.arrRunDays()) > parseInt(obj.dptRunDays()) || 
//								parseInt(obj.arrRunDays()) > parseInt(npobj.arrRunDays()) || parseInt(obj.arrRunDays()) > parseInt(npobj.dptRunDays()) || 
//								parseInt(obj.dptRunDays()) > parseInt(npobj.arrRunDays())  || parseInt(obj.dptRunDays()) >  parseInt(npobj.dptRunDays())) ){
//							self.setColor(2);//设置颜色
//							$("#isSave").attr('disabled',true);
//							$("#isSave2").attr('disabled',true);
//							showErrorDialog("该站的出发天数与到达天数不能大于以后站的出发天数和到达天数");
//							return ;
//						
//					   }
						
					}
//					if(childIndex ==0 && childIndex==i && len >1){//第一行
//						if(obj.arrRunDays()!='--' || obj.dptRunDays()!='--'){
//							if(parseInt(obj.arrRunDays()) > parseInt(obj.dptRunDays()) || parseInt(obj.dptRunDays()) > parseInt(npobj.arrRunDays())
//									|| parseInt(obj.dptRunDays()) > parseInt(npobj.dptRunDays()) ||  parseInt(npobj.arrRunDays()) >  parseInt(npobj.dptRunDays()) ){
//								$("#isSave").attr('disabled',true);
//								$("#isSave2").attr('disabled',true);
//								showErrorDialog("该站的出发天数与到达天数不能大于以后站的出发天数和到达天数");
//								return ;
//							}
//							
//						}
//					}
//					if(childIndex ==i && childIndex==len-1 && len >1){//最后一行
//						if(obj.arrRunDays()!='--' || obj.dptRunDays()!='--'){
//							if(parseInt(obj.dptRunDays()) < parseInt(obj.arrRunDays()) || parseInt(obj.dptRunDays()) < parseInt(upobj.arrRunDays()) || parseInt(obj.dptRunDays()) < parseInt(upobj.dptRunDays()) ){
//								$("#isSave").attr('disabled',true);
//								$("#isSave2").attr('disabled',true);
//								showErrorDialog("该站的出发天数与到达天数不能大于以后站的出发天数和到达天数");
//								return ;
//							}
//							
//						}
//					}
					
					
				}
				
			};
			
			self.arrTimeOnblur = function(n, event){
				var childIndex = self.childIndex();
				var len = _self.runPlanLkCMDTrainStnRows().length;
				self.setColor(0);
				$('#isSave').removeAttr("disabled");
				$('#isSave2').removeAttr("disabled");
				self.formatDate();
				var _arrTime = self.arrTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
				var _dptTime = self.dptTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
//				var ds =0;
//				if(self.arrRunDays()!='--' && self.dptRunDays()!='--'){
//					ds = self.arrRunDays()-self.dptRunDays();//A站的出发天数-A站的到达天数>=1
//				}
//				if(_arrTime!=null && _arrTime!="" && _arrTime!=undefined && 
//						_dptTime!="" && _dptTime!=null && _dptTime!=undefined){
//					if(ds< 1 && _arrTime > _dptTime){
//						self.setColor(1);//设置颜色
//						$("#isSave").attr('disabled',true);
//						$("#isSave2").attr('disabled',true);
//						showErrorDialog("到达时间不能大于出发时间");
//						return ;
//					}
//				}
//				
//				if(isAdd){//是否是新增行
//					if(isCR){//是否是插入行
//						if(len >0){
//							for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
//								var obj = _self.runPlanLkCMDTrainStnRows()[i];
//								var upobj = _self.runPlanLkCMDTrainStnRows()[i-1];
//								var npobj = _self.runPlanLkCMDTrainStnRows()[i+1];
//								if(childIndex == 0 && len >1){//第一行的情况
//									var ds =0;
//									if(self.arrRunDays()!='--' && self.dptRunDays()!='--'){
//										ds = self.arrRunDays()-self.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" &&
//											npobj.arrTime()!=null && npobj.arrTime()!=undefined && npobj.arrTime()!=""){
//										if(ds<1 && obj.dptTime() > npobj.arrTime()){
//											obj.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//										}
//										
//									}
//								
//								}else if(childIndex == i && i!=0 && childIndex!=len-1){//中间的行
//									var ds =0;
//									if(obj.arrRunDays()!='--' && upobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-upobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!=""
//										&& upobj.dptTime()!=null && upobj.dptTime()!="" && upobj.dptTime()!=undefined){
//										if(ds < 1 && obj.arrTime() < upobj.dptTime()){
//											obj.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//											
//										}
//									}
//								
//								}else if(childIndex == i && i!=0 && childIndex ==len-1 && len >1){//最后一行
//									var ds =0;
//									if(obj.arrRunDays()!='--' && upobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-upobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											upobj.dptTime()!=null && upobj.dptTime()!=undefined && upobj.dptTime()!=""){
//										if(ds<1 && obj.arrTime() < upobj.dptTime()){
//											obj.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//										}
//										
//									}
//								
//									
//								}
//								
//								
//							}
//						}
//					}
//			        if(isZJ){//是否是追加行
//						if(len >0){
//							for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
//								var obj = _self.runPlanLkCMDTrainStnRows()[i];
//								var nobj = _self.runPlanLkCMDTrainStnRows()[i+1];
//								var uobj = _self.runPlanLkCMDTrainStnRows()[i-1];
//								if(childIndex == 0 && childIndex==i && len >1 ){//第一行
//										var ds =0;
//										if(nobj.arrRunDays()!='--' && obj.dptRunDays()!='--'){
//											ds = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//										}
//										if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//												nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//										    if(ds<1 && obj.dptTime() > nobj.arrTime()){
//		                                    	   obj.setColor(1);//设置颜色
//													$("#isSave").attr('disabled',true);
//													$("#isSave2").attr('disabled',true);
//													showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//													isTrue1 = true;
//													return ;
//		                                        }
//										}
// 		
//								}else if(childIndex == i && i!=0 && childIndex!=len-1 ){//中间的行
//									var ds =0;
//									if(obj.arrRunDays()!='--' && uobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										if(ds<1 && obj.arrTime() < uobj.dptTime() ){
//											   obj.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												isTrue1 = true;
//												return ;
//										}
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											nobj.dptTime()!=null && nobj.dptTime()!=undefined && nobj.dptTime()!=""){
//										 if(ds<1 && obj.dptTime() > nobj.arrTime()){
//											   obj.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												isTrue1 = true;
//												return ;
//										}
//									}
//								
//									
//								
//								}else if(childIndex == i && i!=0 && childIndex ==len-1 ){//最后一行
//									var ds =0;
//									if(obj.arrRunDays()!='--' && uobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										if(ds<1 && obj.arrTime() <  uobj.dptTime()){
//											   obj.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												isTrue1 = true;
//												return ;
//										}
//									}
//								
//									
//								}
//								
//								
//							}
//						}
//			        }
//				}else{//不是追加和插入的行
//						for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
//							var obj = _self.runPlanLkCMDTrainStnRows()[i];
//							var uobj = _self.runPlanLkCMDTrainStnRows()[i-1];
//							var nobj = _self.runPlanLkCMDTrainStnRows()[i+1];
//							if(childIndex ==0 && childIndex==i && len >1){//第一行
//								var ds =0;
//								if(nobj.arrRunDays()!='--' && -obj.dptRunDays()!='--'){
//									ds = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//								}
//								if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//										nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//									 if(ds <1 &&  obj.dptTime() > nobj.arrTime()){
//										   self.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//										 }
//								}
//							
//							}else if(childIndex == i && i!=0 && childIndex!=len-1){//中间行
//								   var ds =0;
//									if(obj.arrRunDays()!='--' && -uobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//								   var ds2 =0;
//									if(nobj.arrRunDays()!='--' && -obj.dptRunDays()!='--'){
//										ds2 = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										 if(ds <1 &&   obj.arrTime() < uobj.dptTime() ){
//											   self.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												return ;
//										 }
//									}
//									if(ds2<1 && obj.dptTime() > nobj.arrTime()){
//										 self.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//									}
//									
//									
//									if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//											nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//									     if(ds2 <1 &&  obj.dptTime() > nobj.arrTime()){
//											 self.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												return ;
//										 }
//									}
//							
//								
//							}else if(childIndex == i && i!=0 && childIndex == len-1){//最后一行
//								    var ds =0;
//									if(obj.arrRunDays()!='--' && -uobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										if(ds <1 && obj.arrTime() < uobj.dptTime()){
//											   self.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												return ;
//											}
//									}
//									
//							}
//							
//							
//						}
//				}
		
				
			
			};
			
			self.dptTimeOnblur = function(n, event){
				self.setColor(0);
				$('#isSave').removeAttr("disabled");
				$('#isSave2').removeAttr("disabled");
				self.formatDateByDptTime();
				var childIndex = self.childIndex();
				var _arrTime = self.arrTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
				var _dptTime = self.dptTime().replace("：",":").replace("：",":");//利用正则表达式/：/g全部替换
//				var ds =0;
//				if(self.arrRunDays()!='--' && self.dptRunDays()!='--'){
//					ds = self.arrRunDays()-self.dptRunDays();//A站的出发天数-A站的到达天数>=1
//				}
//				var len = _self.runPlanLkCMDTrainStnRows().length;
//				if(_arrTime!=null && _arrTime!=undefined && _dptTime!=null && _dptTime!=undefined){
//					if(ds< 1 && _arrTime > _dptTime){
//						self.setColor(1);//设置颜色
//						$("#isSave").attr('disabled',true);
//						$("#isSave2").attr('disabled',true);
//						showErrorDialog("到达时间不能大于出发时间");
//						return ;
//					}
//				}
//				
//				if(isAdd){//是否是新增行
//					if(isCR){//是否是插入行
//						if(len >0){
//							for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
//								var obj = _self.runPlanLkCMDTrainStnRows()[i];
//								var nobj = _self.runPlanLkCMDTrainStnRows()[i+1];
//								var uobj = _self.runPlanLkCMDTrainStnRows()[i-1];
//								if(childIndex == 0 && childIndex==i && len >1){//第一行
//								   var ds =0;
//									if(nobj.arrRunDays()!='--' && -obj.dptRunDays()!='--'){
//										ds = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//											nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//										if(ds<1 && obj.dptTime() > nobj.arrTime()){
//											obj.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//										}
//									}
//									
//									
//								}else if(childIndex == i && childIndex!=len-1){//中间行
//								   var ds =0;
//									if(nobj.arrRunDays()!='--' && -uobj.dptRunDays()!='--'){
//										ds = nobj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//								  if(obj.dptTime()!="" && obj.dptTime()!=undefined && obj.dptTime()!=null && 
//										  nobj.arrTime()!=null && nobj.arrTime()!="" && nobj.arrTime()!=undefined){
//										if(ds < 1 && obj.dptTime() > nobj.arrTime()){
//											obj.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//											
//										}else if(ds < 1 && obj.arrTime() < uobj.dptTime()){
//											obj.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//										}
//								  }
//								
//								}else if(childIndex == i && i!=0 && childIndex ==len-1){//最后一行
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										if(obj.arrTime() < uobj.dptTime()){
//											obj.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//										}
//									}
//								
//								}
//								
//								
//							}
//						}
//					}
//					if(isZJ){//是否是追加行
//						if(len >0){
//							for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
//								var obj = _self.runPlanLkCMDTrainStnRows()[i];
//								var nobj = _self.runPlanLkCMDTrainStnRows()[i+1];
//								var uobj = _self.runPlanLkCMDTrainStnRows()[i-1];
//								if(childIndex == 0 && childIndex==i && len >1 ){//第一行
//										var ds =0;
//										if(nobj.arrRunDays()!='--' && obj.dptRunDays()!='--'){
//											ds = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//										}
//										if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//												nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//										    if(ds<1 && obj.dptTime() > nobj.arrTime()){
//		                                    	   obj.setColor(1);//设置颜色
//													$("#isSave").attr('disabled',true);
//													$("#isSave2").attr('disabled',true);
//													showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//													isTrue1 = true;
//													return ;
//		                                        }
//										}
//                                    
//										
//										
//										
//								}else if(childIndex == i && i!=0 && childIndex!=len-1 ){//中间的行
//									var ds =0;
//									if(obj.arrRunDays()!='--' && uobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										if(ds<1 && obj.arrTime() < uobj.dptTime() ){
//											   obj.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												isTrue1 = true;
//												return ;
//										}
//									}
//									if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//											nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//										 if(ds<1 && obj.dptTime() > nobj.arrTime()){
//											   obj.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												isTrue1 = true;
//												return ;
//										}
//									}
//							
//									
//								
//								}else if(childIndex == i && i!=0 &&  childIndex ==len-1 ){//最后一行
//									var ds =0;
//									if(obj.arrRunDays()!='--' && uobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										if(ds<1 && obj.arrTime() <  uobj.dptTime()){
//											   obj.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												isTrue1 = true;
//												return ;
//										}
//									}
//							
//									
//								}
//								
//								
//							}
//						}
//					}
//				}else{//不是插入和追加
//						for(var i=0; i< _self.runPlanLkCMDTrainStnRows().length; i++){
//							var obj = _self.runPlanLkCMDTrainStnRows()[i];
//							var uobj = _self.runPlanLkCMDTrainStnRows()[i-1];
//							var nobj = _self.runPlanLkCMDTrainStnRows()[i+1];
//							if(childIndex ==0 && childIndex==i && len >1){//第一行
//								var ds =0;
//								if(nobj.arrRunDays()!='--' && -obj.dptRunDays()!='--'){
//									ds = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//								}
//								if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//										nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//									 if(ds <1 &&  obj.dptTime() > nobj.arrTime()){
//										   self.setColor(1);//设置颜色
//											$("#isSave").attr('disabled',true);
//											$("#isSave2").attr('disabled',true);
//											showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//											return ;
//										 }
//								}
//							
//							}else if(childIndex == i && i!=0 &&  childIndex!=len-1){//中间行
//								    var ds =0;
//									if(nobj.arrRunDays()!='--' && -obj.dptRunDays()!='--'){
//										ds = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//								   var ds2 =0;
//									if(obj.arrRunDays()!='--' && -uobj.dptRunDays()!='--'){
//										ds2 = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										 if(ds <1 &&  obj.dptTime() > nobj.arrTime()){
//											   self.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												return ;
//										 }
//									}
//									if(obj.dptTime()!=null && obj.dptTime()!=undefined && obj.dptTime()!="" && 
//											nobj.arrTime()!=null && nobj.arrTime()!=undefined && nobj.arrTime()!=""){
//										  if(ds <1 &&  obj.dptTime() > nobj.arrTime()){
//												 self.setColor(1);//设置颜色
//													$("#isSave").attr('disabled',true);
//													$("#isSave2").attr('disabled',true);
//													showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//													return ;
//											 }
//									}
//									if(ds2<1 && obj.arrTime() < uobj.dptTime()){
//										self.setColor(1);//设置颜色
//										$("#isSave").attr('disabled',true);
//										$("#isSave2").attr('disabled',true);
//										showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//										return ;
//									}
//						
//								
//							}else if(childIndex == i && i!=0 && childIndex == len-1){//最后一行
//								    var ds =0;
//									if(obj.arrRunDays()!='--' && -uobj.dptRunDays()!='--'){
//										ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
//									}
//									if(obj.arrTime()!=null && obj.arrTime()!=undefined && obj.arrTime()!="" && 
//											uobj.dptTime()!=null && uobj.dptTime()!=undefined && uobj.dptTime()!=""){
//										if(ds <1 && obj.arrTime() < uobj.dptTime()){
//											   self.setColor(1);//设置颜色
//												$("#isSave").attr('disabled',true);
//												$("#isSave2").attr('disabled',true);
//												showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
//												return ;
//											}
//									}
//								
//							}
//							
//							
//						}
//				}
		
				
			};
			/*
			 * 出发到达车次  离焦事件
			*/		
				self.sourceTrainNbrOnblur = function(n, event){
					if(self.arrTrainNbr()!="" && self.arrTrainNbr()!=null){
						self.dptTrainNbr(self.arrTrainNbr());
						var index = self.childIndex()-1;
						$.each(_self.runPlanLkCMDTrainStnRows(), function(i, obj){
							if (i > index) {
								obj.arrTrainNbr(self.arrTrainNbr());
								obj.dptTrainNbr(self.arrTrainNbr());
							}
						});
					}
					checkRepeat = false;
				};
				
	
				self.targetTrainNbrOnblur = function(n, event){
					var index = self.childIndex();
					if(self.dptTrainNbr()!="" && self.dptTrainNbr()!=null ){
						$.each(_self.runPlanLkCMDTrainStnRows(), function(i, obj){
							if (i > index) {
								obj.arrTrainNbr(self.dptTrainNbr());
								obj.dptTrainNbr(self.dptTrainNbr());
							}
						});
					}
					checkRepeat = false;
				};
			
			self.stnNameTemp = ko.computed({
		        read: function () {
		        	if(self.stnName()==null || self.stnName()==undefined || self.stnName()=='null' || self.stnName()=='"null"'){
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
//							commonJsScreenUnLock(1);
						}
				    });
		        },
		        owner: this
		    });

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
			
			
			
//			//车站名称自动补全
//			self.stnNameOnfocus = function(n, event){
//				$(event.target).autocomplete(runPlanLkCmdPageModel.vehicles(),{
//					max: 50,    	//列表里的条目数
//					width: 200,     //提示的宽度，溢出隐藏
//					scrollHeight: 120,   	//提示的高度，溢出显示滚动条
//					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
//					autoFill: false,    	//是否自动填充
//					formatResult: function(row) {
//						return row;
//					}
//		       }).result(function(event, vehicleName, formatted) {
//		    	   
//		       });
//			};
			
		};

		
		// 列车类型
		_self.trainlineTypes = ko.observableArray();
		_self.isSelectAll = ko.observable(false);	//本局担当命令列表是否全选 	全选标识  默认false
		_self.currentCmdTxtMl = ko.observable(new CmdTrainRowModel(createEmptyCmdTrainRow()));//命令列表选中行记录
		_self.currentCmdTrainStnS = ko.observableArray();
		_self.currentCmdTrainStn = ko.observable(new _self.cmdTrainStnTimeRow({
			childIndex:-1,
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
			jobItems:[],
			jobItemsText:"",
			kyyy:"",
		}));//列车时刻表选中行记录  用于上下移动
		//套用的基本图车次id
		_self.useBaseTrainId = ko.observable();
		_self.useBaseTrainIdFunc = ko.computed(function(){
			//当页面命令时刻列表 无数据时 套用列车id置空
			if(_self.runPlanLkCMDTrainStnRows.length == 0){
				_self.useBaseTrainId("");
				return "";
			}
			
		});
		
		_self.input_tjj_wjdd = ko.computed(function(){
			if (typeof _self.currentCmdTxtMl()=="object" && _self.currentCmdTxtMl() != "undefined" && _self.currentCmdTxtMl().passBureau != "undefined" && _self.currentCmdTxtMl().passBureau != "null") {
				return _self.currentCmdTxtMl().passBureau;	//临客命令列表当前选中值   途径局
			} else {
				return "";
			}
			
		});
		
		
		/**
		 * 清除时刻列表当前选中值
		 */
		_self.cleanCurrentCmdTrainStn = function() {
			_self.currentCmdTrainStn(new _self.cmdTrainStnTimeRow({
				childIndex:-1,
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
				bureauId:""
		}));
		};
		
		
		/**
		 * tab标签点击事件
		 * 
		 * 清空页面数据
		 */
		_self.cleanPageData = function() {
			//清除历史值
			_self.currentCmdTxtMl(new CmdTrainRowModel(createEmptyCmdTrainRow()));
			_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
			
			_self.runPlanLkCMDRows.remove(function(item) {
				return true;
			});
			_self.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
			_self.trainlineTypes.remove(function(item) {
				return true;
			});
			_self.trainlineTypes.remove(function(item) {
				return true;
			});
		};
		

		/**
		 * 本局担当tab查询按钮事件
		 */
		_self.queryListBjdd = function(){
			_self.cleanPageData();
			cmdTrainId = null;
			//2.查询本局担当命令信息
			loadDataForBjddPage();	//loadRows方法
		};
		
		
		
		/**
		 * 查询本局担当临客命令列表
		 */
		function loadDataForBjddPage() {
			var startDate = moment($("#runPlanLk_cmd_input_startDate").val()).format("YYYY-MM-DD");
			var endDate = moment($("#runPlanLk_cmd_input_endDate").val()).format("YYYY-MM-DD");
			if(startDate!=null && startDate!=undefined && endDate!=null && endDate!=undefined){
				if(startDate > endDate){
					showWarningDialog("令电日期(开始)不能大于令电日期(截至)!");
					return ;
				}
			}
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/getCmdTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
//					cmdBureau : _self.searchModle().bureauOption().value,//发令局 (发令局、担当局局码)
					startDate :startDate,//起始日期
					endDate : endDate,//终止日期
					cmdNbrBureau : _self.searchModle().cmdNbrBureau(),//路局命令号
					cmdNbrSuperior : _self.searchModle().cmdNbrSuperior(),//总公司命令号
					trainNbr : _self.searchModle().trainNbr(),//车次
					cmdType : _self.searchModle().cmdTypeOptionBjdd().code,//命令类型 (既有加开；既有停运；高铁加开；高铁停运)
					selectState : _self.searchModle().selectStateOption(),//选线状态
					createState : _self.searchModle().createStateOption()//生成开行计划状态
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						if(result.data != null){
							$.each(result.data,function(n, obj){
								_self.runPlanLkCMDRows.push(new CmdTrainRowModel(obj));
								//console.dir(ko.toJSON(obj));
							});
							
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
							isWjdd = false;
						}
					} else {
						showErrorDialog("获取临客命令信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取临客命令信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		function CmdTrainRowModel(data) {
			var _self = this;
			
			
			
//			self.sourceTime = data.stationFlag=='SFZ'?'--':filterValue(data.arrTime);
//			self.targetTime = data.stationFlag=='ZDZ'?'--':filterValue(data.dptTime);
//			self.stepStr = GetDateDiff(data); 
//			self.trackName = filterValue(data.trackName);
//			self.arrRunDays = data.stationFlag=='SFZ'?'--':data.arrRunDays;
//			self.runDays = data.stationFlag=='ZDZ'?'--':data.runDays;
//			self.stationFlag = data.stationFlag;
////			self.sourceDay = data.arrRunDays;
////			self.targetDay = data.targetDay;
//			self.targetDay = data.stationFlag=='ZDZ'?'--':data.targetDay;
//			self.sourceDay = data.stationFlag=='SFZ'?'--':data.arrRunDays;
			
			_self.importantFlag = ko.observable(data.importantFlag);
			
			_self.importantFlagStr = ko.computed(function () {
				//选线状态0：未选择 1：已选择
				if (_self.importantFlag() == "1") {
					return "<span>是</span>";//"已";
				} else {
				return "";//"未";
				} 
			});
			_self.cmdTrainId = ko.observable(data.cmdTrainId);
			_self.baseTrainId = ko.observable(data.baseTrainId);
			_self.cmdBureau = ko.observable(data.cmdBureau);
			_self.cmdType = ko.observable(data.cmdType);
			_self.cmdTxtMlId = ko.observable(data.cmdTxtMlId);
			_self.cmdTxtMlItemId = ko.observable(data.cmdTxtMlItemId);
			_self.cmdNbrBureau = ko.observable(data.cmdNbrBureau);
			_self.cmdItem = ko.observable(data.cmdItem);
			_self.cmdNbrSuperior = ko.observable(data.cmdNbrSuperior);
			_self.trainNbr = ko.observable(data.trainNbr);
			_self.startStn = ko.observable(data.startStn);
			_self.endStn = ko.observable(data.endStn);
			_self.rule = ko.observable(data.rule);
			_self.selectedDate = ko.observable(data.selectedDate);
			_self.startDateStr = ko.observable(moment(data.startDate).format("YYYYMMDD"));//起始日期仅用于界面显示
			_self.startDate = ko.observable(data.startDate);
			_self.endDate = ko.observable(data.endDate);
			_self.endDateStr = ko.observable(moment(data.endDate).format("YYYYMMDD"));//终止日期仅用于界面显示
			_self.passBureau = ko.observable(data.passBureau=='null'||data.passBureau=='"null"'?"":data.passBureau);
			_self.updateTime = ko.observable(data.updateTime);
			_self.cmdTime = ko.observable(data.cmdTime);
			_self.cmdTimeStr = ko.observable(moment(data.cmdTime).format("YYYYMMDD"));//发令日期仅用于界面显示
			_self.isExsitStn = ko.observable(data.isExsitStn);
			_self.userBureau = ko.observable(data.userBureau);
			_self.selectState = ko.observable(data.selectState);
			_self.createState = ko.observable(data.createState);
			//增加isSelect属性  便于全选复选框事件	默认0=false
			_self.isSelect = ko.observable(0);
			if(null != data.cmdTrainId){
				if(data.cmdTrainId == cmdTrainId){
					_self.isSelect = ko.observable(1);
				}
			}
			
			_self.selectStateStr = ko.computed(function () {
				//选线状态0：未选择 ;1：已选择;2:选线后,又再次调整时刻表
				if (_self.selectState() == "0") {
					return "<span class='label label-danger'>未</span>";//"未";
				} else if (_self.selectState() == "1") {
					return "<span class='label label-success'>是</span>";//"已";
				} else if (_self.selectState() == "2") {
					return "<span class='label label-warning'>非</span>";
				}
			});
			

			_self.createStateStr = ko.computed(function () {
				//生成状态0：未生成 1：已生成
				if (_self.createState() == "1") {
					return "<span class='label label-success'>已</span>";//"已";
				} else if (_self.createState() == "0") {
					return "<span class='label label-danger'>未</span>";//"未";
				} else {
					return "";
				}
			});
			

			_self.routeId = ko.observable(formatString(data.routeId));
			_self.trainTypeId = ko.observable(formatString(data.trainTypeId));
			_self.business = ko.observable(formatString(data.business));
			_self.startBureauId = ko.observable(formatString(data.startBureauId));
			_self.startStnId = ko.observable(formatString(data.startStnId));
			_self.endBureauId = ko.observable(formatString(data.endBureauId));
			_self.endStnId = ko.observable(formatString(data.endStnId));
			_self.endDays = ko.observable(formatString(data.endDays));
			
			// 新增字段
			_self.sourceNodeStationId = ko.observable(formatString(data.sourceNodeStationId));
			_self.sourceNodeStationName = ko.observable(formatString(data.sourceNodeStationName));
			_self.sourceNodeTdcsId = ko.observable(formatString(data.sourceNodeTdcsId));
			_self.sourceNodeTdcsName = ko.observable(formatString(data.sourceNodeTdcsName));
			_self.targetNodeStationId = ko.observable(formatString(data.targetNodeStationId));
			_self.targetNodeStationName = ko.observable(formatString(data.targetNodeStationName));
			_self.targetNodeTdcsId = ko.observable(formatString(data.targetNodeTdcsId));
			_self.targetNodeTdcsName = ko.observable(formatString(data.targetNodeTdcsName));
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
			
			// 新增字段
			_dataObj.sourceNodeStationId   = "";
			_dataObj.sourceNodeStationName = "";
			_dataObj.sourceNodeTdcsId      = "";
			_dataObj.sourceNodeTdcsName    = "";
			_dataObj.targetNodeStationId   = "";
			_dataObj.targetNodeStationName = "";
			_dataObj.targetNodeTdcsId      = "";
			_dataObj.targetNodeTdcsName    = "";
			
			return _dataObj;
		};
		
		

		/**
		 * 外局担当tab查询按钮事件
		 */
		_self.queryListWjdd = function(){
			_self.cleanPageData();

			//2.查询外局担当命令信息
			loadDataForWjddPage();	//loadRows方法
		};
		
		
		var isWjdd = false;
		/**
		 * 查询外局担当临客命令列表
		 */
		function loadDataForWjddPage() {
			var startDate = moment($("#runPlanLk_cmd_input_startDate_wjdd").val()).format("YYYY-MM-DD");
			var endDate =   moment($("#runPlanLk_cmd_input_endDate_wjdd").val()).format("YYYY-MM-DD");
			if(startDate!=null && startDate!=undefined && endDate!=null && endDate!=undefined){
				if(startDate > endDate){
					showWarningDialog("令电日期(开始)不能大于令电日期(截至)!");
					return ;
				}
			}
			commonJsScreenLock();
			
			
			$.ajax({
				url : basePath+"/runPlanLk/getOtherCmdTrainInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdBureau : _self.searchModle().bureauOption().value,//发令局 (发令局、担当局局码)
					startDate : startDate,//起始日期
					endDate : endDate,//终止日期
//					cmdNbrBureau : _self.searchModle().cmdNbrBureau(),//路局命令号
					cmdNbrSuperior : _self.searchModle().cmdNbrSuperior(),//总公司命令号
					trainNbr : _self.searchModle().trainNbr(),//车次
					cmdType : _self.searchModle().cmdTypeOptionWjdd().code,//命令类型 (既有加开；既有停运；高铁加开；高铁停运)
					createState : _self.searchModle().createStateOption()//生成开行计划状态
				}),
				success : function(result) {
 
					if (result != null && typeof result == "object" && result.code == "0") {
						if(result.data != null){
							$.each(result.data,function(n, obj){
								_self.runPlanLkCMDRows.push(new CmdTrainRowModel(obj));
							});
							//是否出现右边17
							var tableH = $("#left_height2 table").height();
							var boxH = $("#left_height2").height();
							if(tableH <= boxH){
								//alert("没");
								$(".td_17").removeClass("display");
							}else{
								//alert("有");
								$(".td_17").addClass("display");
							}
							isWjdd = true;
						}
						 
					} else {
						showErrorDialog("获取临客命令信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取临客命令信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		
		/**
		 * 命令列表全选复选框change事件
		 */
		_self.checkBoxSelectAllChange = function() {
//			console.log("~~~~~~~~ 全选标识  _self.isSelectAll="+_self.isSelectAll());
			if (!_self.isSelectAll()) {//全选     将runPlanLkCMDRows中isSelect属性设置为1
//				console.log("全选");
				$.each(_self.runPlanLkCMDRows(), function(i, row){
					row.isSelect(1);
				});
			} else {//全不选    将runPlanLkCMDRows中isSelect属性设置为0
//				console.log("全不选");
				$.each(_self.runPlanLkCMDRows(), function(i, row){
					row.isSelect(0);
				});
			}
		};
		
		

		/**
		 * 命令列表行点击事件
		*/
		_self.setCurrentRec = function(row) {
			$('#isSave').removeAttr("disabled");
			$('#isSave2').removeAttr("disabled");
			cmdTrainId = row.cmdTrainId();
			row.isSelect(1);//复选框勾中
			_self.currentCmdTxtMl(row);
			
			//清除时刻表列表行选中值及时刻表列表数据
			_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
			
			_self.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
			
			selectState = row.selectState();
			//查询cmdTrainStn信息
			loadCmdTrainStnInfo(row.cmdTrainId());
			// ...
			_self.trainlineTypes.remove(function(item) {return true;});
			getMTrainlineType(row.trainNbr(),row.trainTypeId());
			
//			console.log("row.cmdTrainId: " + row.cmdTrainId());
		};
		 
		_self.setCurrentRec1 = function(row) {
			if(row.isSelect()==false ||row.isSelect()==0){
				row.isSelect(1);//复选框勾中
				_self.currentCmdTxtMl(row);
				cmdTrainId = row.cmdTrainId();
				
				//清除时刻表列表行选中值及时刻表列表数据
				_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
				
				_self.runPlanLkCMDTrainStnRows.remove(function(item) {
					return true;
				});
				
				//查询cmdTrainStn信息
				loadCmdTrainStnInfo(row.cmdTrainId());
				// ...
				_self.trainlineTypes.remove(function(item) {return true;});
				getMTrainlineType(row.trainNbr(),row.trainTypeId());
			}
			
			
			
			
		};
		 
		_self.ctrlSels = function(row,event) {
//			console.log(row.childIndex());
			if (event.ctrlKey==1){
				$(event.target.parentNode.parentNode).addClass("color: blue");
				_self.currentCmdTrainStnS.push(_self.currentCmdTrainStn());
		    }else{
		    	console.log("为按下");
//		    	console.log(event.target.parentNode.parentNode.parentNode);
//		    	$(event.target.parentNode.parentNode.parentNode).removeClass("color: blue");
		    	var tbodys = event.target.parentNode.parentNode.parentNode;
		    	console.log(tbodys);
		    	for(var i=0; i<tbodys.length; i++) {
					var tbodyText="";
					var tbodysTds = tbodys[i].getElementsByTagName("td");
					console.log(tbodysTds);
					for(var j=0; j<tbodysTds.length; j++) {
					//tbodyText += tbodysTds[j].firstChild.nodeValue;
						console.log(tbodysTds[j].firstChild.nodeValue);
					$(tbodysTds[j].firstChild.nodeValue).removeClass("color: blue");
					}
		    	}
			  _self.currentCmdTrainStnS.remove(function(item) {
					return true;
				});
		    }
		};
		
		 
		/**
		 * 点击行后,将所点击行的id带过来赋值.
		 * @param row
		 */
//		_self.setCmdTxtMlItemId = function(row) {
//			console.log(row.cmdTxtMlItemId());
//			selCmdTxtMlItemId = row.cmdTxtMlItemId();
//		}
		
		/**
		 * 时刻列表行点击事件
		 */
		_self.setCMDTrainStnCurrentRec = function(row) {
			console.log("进入setCMDTrainStnCurrentRec");
			_self.currentCmdTrainStn(row);
		};
		
		/**
		 * 加载列车类型.
		 * 
		 */
		function getMTrainlineType(trainNbr,typeId){
    	    $.ajax({
    			url : basePath + "/runPlanLk/getMTrainlineType",
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
//    						console.log(lx);
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
    							_self.trainlineTypes.push({"name": types.NAME, "id": types.ID});
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
    				commonJsScreenUnLock(); 
    			}
    	    });
        	
        };
		
		
		
		
		/**
		 * 查询cmdTrainStn信息
		 */
		function loadCmdTrainStnInfo(_cmdTrainId) {
			if(_cmdTrainId==null || _cmdTrainId=="undefind") {
				return;
			}
			_self.runPlanLkCMDTrainStnRows.remove(function(item) {
				return true;
			});
			commonJsScreenLock();
			
			$.ajax({
				url : basePath+"/runPlanLk/getCmdTrainStnInfo",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainId : _cmdTrainId
				}),
				success : function(result) {
					if (result != null && typeof result == "object" && result.code == "0") {
						if(result.data != null){
							$.each(result.data,function(n, obj){
								
								obj.childIndex = n;	//增加元素在集合中的序号属性，便于调整顺序
								if(obj.jobs.indexOf("<始发>")>-1){
									obj.arrRunDays="--"
									_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow(obj));
									runPlanLkCMDTrainStnArray.push(new _self.cmdTrainStnTimeRow(obj));
								
								}
								if( obj.jobs.indexOf("<终到>")>-1){
									obj.dptRunDays="--"
										_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow(obj));
									runPlanLkCMDTrainStnArray.push(new _self.cmdTrainStnTimeRow(obj));
								}
								if(obj.jobs.indexOf("<经由>")>-1){
									_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow(obj));
									runPlanLkCMDTrainStnArray.push(new _self.cmdTrainStnTimeRow(obj));
								
								}
								if(obj.jobs=="null"||obj.jobs==null||obj.jobs=="\"null\""){
									_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow(obj));
									runPlanLkCMDTrainStnArray.push(new _self.cmdTrainStnTimeRow(obj));
								
								}
								
							   
								
									
							});
							checkRepeat = true;
							if(isWjdd){
								if(selectState == 1){
								    $("#time_div2 input").attr("disabled", "disabled");
								    $("#time_div2 select").attr("disabled", "disabled");
								}
							}
							//是否出现右边17
							var tableH = $("#time1 table").height();
							var boxH = $("#time1").height();
							if(tableH <= boxH){
								//alert("没");
								$("#time1 .td_17").removeClass("display");
							}else{
								//alert("有");
								$("#time1 .td_17").addClass("display");
							}
						}
						 
					} else {
						showErrorDialog("获取列车时刻信息失败");
					};
				},
				error : function() {
					showErrorDialog("获取列车时刻信息失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		
		
		
		
		
		
		/**
		 * 选线按钮点击事件	打开基本图查询界面
		 * 
		 * tabType=bjdd (本局担当)
		 * @param 
		 */
		_self.loadTrainInfoFromJbt = function(){
			var _selectCmdTrainArray = [];
			$.each(_self.runPlanLkCMDRows(), function(i, row){
				if(row.isSelect() == 1){
					_selectCmdTrainArray.push(row);
				}
			});
			
			if(_selectCmdTrainArray.length == 0) {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			if(_selectCmdTrainArray.length != 1) {
				showWarningDialog("只能选择一条临客命令记录");
				return;
			}
			
			
//			if($('#run_plan_train_times_edit_dialog').is(":hidden")){
				$("#jbt_traininfo_dialog").find("iframe").attr("src", basePath+"/runPlanLk/jbtTrainInfoPage?tabType=bjdd&trainNbr="+_selectCmdTrainArray[0].trainNbr());
				$('#jbt_traininfo_dialog').dialog({title: "选择车次", autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#jbt_traininfo_dialog").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#jbt_traininfo_dialog').height();
						var WW = $('#jbt_traininfo_dialog').width();
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
				checkRepeat = false;
//			}
		};
		
		
		/**
		 * 补点按钮点击事件	打开基本图查询界面
		 * 
		 * tabType=bjdd (本局担当)
		 * @param 
		 */
		_self.loadFillSite = function(){
			_self.runPlanLkTrainStnRows.remove(function(item){
				return true;
			});
			var _saveTrainStnArray = [];
			var isNull = false;
			$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
//				if(!Validate.isNumber(obj.platform())){
//					showWarningDialog("时刻表第"+(n+1)+"行,站台必须为数字类型");
//					pfE = 1;
//					return false;
//				}
				var z=n+1;
//				obj.stnName=$("#"+z+"jy").val();
//				obj.stnNameTemp=$("#"+z+"jy").val();
//				obj.currentBureauJc=$("#"+z+"jy100").find("option:selected").text();
				var strBur = $("#"+z+"jy100").find("option:selected").text();//获取页面下拉框的值
				if(obj.nodeName()=="" || obj.nodeName()==undefined){
					showWarningDialog("站名不能为空");
					isNull = true;
					return false;
				}
				if(strBur==null || strBur=='' || strBur ==undefined){
					showWarningDialog("路局不能为空");
					isNull = true;
					return false;
				}
				obj.stnBureau=strBur;
				if(obj.arrTime()==undefined || obj.arrTime()=="" || obj.arrTime()==null){
					showWarningDialog("到达时间不能为空");
					isNull = true;
					return false;
				}
				if(obj.dptTime()==undefined || obj.dptTime()=="" || obj.dptTime()==undefined){
					showWarningDialog("出发时间不能为空");
					isNull = true;
					return false;
				}
				 var pattern = /^([0-9]|--)$/;
				    if (!pattern.test(obj.arrRunDays())) {
				    	obj.setColor(2);//设置颜色	
				    	showWarningDialog("到达天数输入不正确");
						isNull = true;
						return false;
				    }
				    if (!pattern.test(obj.dptRunDays())) {
				    	obj.setColor(2);//设置颜色	
				    	showWarningDialog("出发天数输入不正确");
						isNull = true;
						return false;
				    }
				_saveTrainStnArray.push(ko.toJSON(obj));
				_self.runPlanLkTrainStnRows.push(ko.toJSON(obj));
//				 console.log("obj>>>>>>>>>>>>"+ko.toJSON(obj));
			});
			
			if(isNull){
				return;
			}
			
			var _selectCmdTrainArray = [];
			$.each(_self.runPlanLkCMDRows(), function(i, row){
				if(row.isSelect() == 1){
					_selectCmdTrainArray.push(row);
				}
			});
			
			if(_selectCmdTrainArray.length == 0) {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			if(_selectCmdTrainArray.length != 1) {
				showWarningDialog("只能选择一条临客命令记录");
				return;
			}
			
//			console.log(_saveTrainStnArray);
//			if($('#run_plan_train_times_edit_dialog').is(":hidden")){
				$("#jbt_fill_site_dialog").find("iframe").attr("src", basePath+"/runPlanLk/jbtFillSitePage?tabType=bjdd&trainNbr="+_selectCmdTrainArray[0].trainNbr());
				$('#jbt_fill_site_dialog').dialog({title: "选择参照车次进行补点", autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#jbt_fill_site_dialog").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#jbt_fill_site_dialog').height();
						var WW = $('#jbt_fill_site_dialog').width();
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
				checkRepeat = false;
//			}
		};
		
		
		/**
		 * 复制历史按钮点击事件	打开文电界面
		 * 
		 * tabType=bjdd (本局担当)
		 * @param 
		 */
		_self.loadTrainInfoFromHis = function(){
			var _selectCmdTrainArray = [];
			var num = 0;
			var i;
			var obj = null;
			var objSelect = null;
			
			$.each(_self.runPlanLkCMDRows(), function(i, row){
				if(row.isSelect() == 1){
					_selectCmdTrainArray.push(row);
					objSelect = _self.runPlanLkCMDRows()[i];
				}
			});
			
			if(_selectCmdTrainArray.length == 0) {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			if(_selectCmdTrainArray.length != 1) {
				showWarningDialog("只能选择一条临客命令记录");
				return;
			}

				if(1 == objSelect.createState()) {
					showWarningDialog("您所选中的命令中含有已经生成开行计划的命令，不能复制历史");
					return;
				}
			
			
//			if($('#run_plan_train_times_edit_dialog').is(":hidden")){
				$("#his_traininfo_dialog").find("iframe").attr("src", basePath+"/runPlanLk/hisTrainInfoPage?tabType=bjdd&trainNbr="+_selectCmdTrainArray[0].trainNbr()+"&cmdTrainId="+_selectCmdTrainArray[0].cmdTrainId());
				$('#his_traininfo_dialog').dialog({title: "复制历史", autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#his_traininfo_dialog").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#his_traininfo_dialog').height();
						var WW = $('#his_traininfo_dialog').width();
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
				checkRepeat = false;
//			}
		};	


// 2014-12-24 09:33:59,点击复选框无法保存,更换为高铁的.
//		/**
//		 * 本局担当保存按钮点击事件
//		 */
//		_self.saveCmdTxtMl = function() {
////			var _selectCmdTrainArray = [];
////			$.each(_self.runPlanLkCMDRows(), function(i, row){
////				if(row.isSelect() == 1){
////					_selectCmdTrainArray.push(row);
////				}
////			});
////			
////			if(_selectCmdTrainArray.length == 0) {
////				showWarningDialog("请选择临客命令记录");
////				return;
////			}
////			if(_selectCmdTrainArray.length != 1) {
////				showWarningDialog("只能选择一条临客命令记录");
////				return;
////			}
////			
//			if(_self.currentCmdTxtMl() == null || _self.currentCmdTxtMl().cmdTxtMlItemId()=="") {
//				showWarningDialog("请选择选择一条临客命令记录");
//				return;
//			}
//			
//			if(_self.runPlanLkCMDTrainStnRows().length==0) {
//				showWarningDialog("请补充列车时刻数据");
//				return;
//			}
//			
//			
//			if("" == _self.currentCmdTxtMl().passBureau() || null == _self.currentCmdTxtMl().passBureau()) {
//				showWarningDialog("请补充列车途经局数据");
//				return;
//			}
//			
//			console.log("_self.useBaseTrainId()" + _self.useBaseTrainId());
//			commonJsScreenLock();
//			
//
////			_self.currentCmdTxtMl().passBureau = $("#runPlanLk_cmd_input_tjj").val();//设置途经局
//			var pfE = 0;
//			var _saveTrainStnArray = [];
//			$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
//				if(!Validate.isNumber(obj.platform())){
//					showWarningDialog("时刻表第"+(n+1)+"行,站台必须为数字类型");
//					pfE = 1;
//					return false;
//				}
//				_saveTrainStnArray.push(ko.toJSON(obj));
//			});
//			if(pfE > 0){
//				commonJsScreenUnLock(1);
//				return;
//			}
//			
//
//			
//			$.ajax({
//				url : basePath+"/runPlanLk/saveLkTrainTimes",
//				cache : false,
//				type : "POST",
//				dataType : "json",
//				contentType : "application/json",
//				data :JSON.stringify({
//					cmdTrainMap : ko.toJSON(_self.currentCmdTxtMl()),
//					cmdTrainStnList : _saveTrainStnArray,
//					baseTrainId : _self.useBaseTrainId()	//套用的基本图车次id
//					
//				}),
//				success : function(result) {
//					
//					
//					//alert(_self.runPlanLkCMDRows())
//					
//					if (result != null && typeof result == "object" && result.code == "0") {
//						showSuccessDialog("保存成功");
//						//清除
//						_self.runPlanLkCMDRows.remove(function(item) {
//							return true;
//						});
//						
//						loadDataForBjddPage();
//					} else {
//						showErrorDialog("保存失败");
//					};
//				},
//				error : function() {
//					showErrorDialog("保存失败");
//				},
//				complete : function(){
//					commonJsScreenUnLock(1);
//				}
//			});
//		};
		
		
		
		
		/**
		 * 本局担当保存按钮点击事件
		 */
		_self.saveCmdTxtMl = function() {
			$('#isSave').removeAttr("disabled");
			$('#isSave2').removeAttr("disabled");
			if(checkRepeat){
				showWarningDialog("操作取消！当前车次客运时刻未做任何改动。");
				return;
			}
			var typeOk = false;
			var createOk = false;
			var pass =	$("#runPlanLk_cmd_input_tjj").val();
			var _selectCmdTrainArray = [];
			var _selectCmdTrainArray1 = [];
			$.each(_self.runPlanLkCMDRows(), function(i, row){
				if(row.isSelect() == 1 && row.cmdTrainId() == cmdTrainId){
					if(row.createState() == 1){
						createOk = true;
						return false;
					}
					row.passBureau=pass
					if(null != $("#train_line_type").val() && "" != $("#train_line_type").val()){
						row.trainTypeId = $("#train_line_type").val();
					}else{
						typeOk = true;
						return false;
					}
					_selectCmdTrainArray.push(row);
					_selectCmdTrainArray1.push(row);
				
				}
			});
			
			if(createOk){
				showWarningDialog("操作取消！当前车次已生成开行计划。");
				return;
			}
			
			if(typeOk){
				showWarningDialog("操作取消！请选择列车类型。");
				return;
			}
//			
			if(_selectCmdTrainArray.length == 0) {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			if(_selectCmdTrainArray.length != 1) {
				showWarningDialog("只能选择一条临客命令记录");
				return;
			}
			if(pass==null||pass==""){
				showWarningDialog("请补充列车途经局数据");
				return;
			}
			/*if(_selectCmdTrainArray[0].passBureau()==null||_selectCmdTrainArray[0].passBureau()==""){
				showWarningDialog("请补充列车途经局数据");
				return;
			}*/
			
//			if(_self.currentCmdTxtMl() == null || _self.currentCmdTxtMl().cmdTxtMlItemId()=="") {
//				showWarningDialog("请选择选择一条临客命令记录");
//				return;
//			}
			
			if(_self.runPlanLkCMDTrainStnRows().length==0) {
				showWarningDialog("请补充列车时刻数据");
				return;
			}
			
			
			/*if("" == _self.currentCmdTxtMl().passBureau() || null == _self.currentCmdTxtMl().passBureau()) {
				showWarningDialog("请补充列车途经局数据");
				
				return;
			}*/
			
			//console.log("_self.useBaseTrainId()" + _self.useBaseTrainId());
			
			

//			_self.currentCmdTxtMl().passBureau = $("#runPlanLk_cmd_input_tjj").val();//设置途经局
//			var pfE = 0;
			var _saveTrainStnArray = [];
			var isNull = false;
			$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
//				if(!Validate.isNumber(obj.platform())){
//					showWarningDialog("时刻表第"+(n+1)+"行,站台必须为数字类型");
//					pfE = 1;
//					return false;
//				}
				var z=n+1;
//				obj.stnName=$("#"+z+"jy").val();
//				obj.stnNameTemp=$("#"+z+"jy").val();
//				obj.currentBureauJc=$("#"+z+"jy100").find("option:selected").text();
				var strBur = $("#"+z+"jy100").find("option:selected").text();//获取页面下拉框的值
				if(obj.nodeName()=="" || obj.nodeName()==undefined){
					showErrorDialog("站名不能为空");
					isNull = true;
					return false;
				}
				if(strBur==null || strBur=='' || strBur ==undefined){
					showErrorDialog("路局不能为空");
					isNull = true;
					return false;
				}
				obj.stnBureau=strBur;
				if(obj.arrTime()==undefined || obj.arrTime()=="" || obj.arrTime()==null){
					showErrorDialog("到达时间不能为空");
					isNull = true;
					return false;
				}
				if(obj.dptTime()==undefined || obj.dptTime()=="" || obj.dptTime()==undefined){
					showErrorDialog("出发时间不能为空");
					isNull = true;
					return false;
				}
				 var pattern = /^([0-9]|--)$/;
				    if (!pattern.test(obj.arrRunDays())) {
				    	obj.setColor(2);//设置颜色	
						showErrorDialog("到达天数输入不正确");
						isNull = true;
						return false;
				    }
				    if (!pattern.test(obj.dptRunDays())) {
				    	obj.setColor(2);//设置颜色	
				    	showErrorDialog("出发天数输入不正确");
						isNull = true;
						return false;
				    }
				_saveTrainStnArray.push(ko.toJSON(obj));
//				 console.log("obj>>>>>>>>>>>>"+ko.toJSON(obj));
			});
//			if(pfE > 0){
//				commonJsScreenUnLock(1);
//				return;
//			}
		
		    if(isNull){
		    	return ;
		    }
		    
			//保存的时候验证
			if(_self.runPlanLkCMDTrainStnRows().length > 0){
				for(var i =0 ;i < _self.runPlanLkCMDTrainStnRows().length; i++){
					var obj = _self.runPlanLkCMDTrainStnRows()[i];
					var uobj =null;
					var nobj = null;
					if(i!=0){
						uobj = _self.runPlanLkCMDTrainStnRows()[i-1];
					}
					if(i!= _self.runPlanLkCMDTrainStnRows().length-1){
						nobj = _self.runPlanLkCMDTrainStnRows()[i+1];
					}
					if(i!=0 && i!=_self.runPlanLkCMDTrainStnRows().length-1){
						if(obj.arrRunDays()=='--' || obj.dptRunDays()=='--'){
							    obj.setColor(2);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("经由站的到达天数和出发天数必须是数字");
								return ;
						}
						if(obj.arrTime() =='--' || obj.dptTime()=='--'){
							obj.setColor(1);//设置颜色
							$("#isSave").attr('disabled',true);
							$("#isSave2").attr('disabled',true);
							showErrorDialog("经由站的到达时间和出发时间格式不正确");
							return ;
						}
						
					}
				  
					if(obj!=null && obj!=undefined && uobj!=null && uobj!=undefined){
						var ds =0;
						if(obj.arrRunDays()!='--' && -uobj.dptRunDays()!='--'){
							ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
						}
						if(ds <1 && obj.arrTime() < uobj.dptTime()){
							    uobj.setColor(1);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
								return ;
						}
						if(parseInt(obj.arrRunDays()) < parseInt(uobj.dptRunDays())  ||  parseInt(obj.arrRunDays()) <  parseInt(uobj.arrRunDays())){
						    uobj.setColor(2);//设置颜色
							$("#isSave").attr('disabled',true);
							$("#isSave2").attr('disabled',true);
							showErrorDialog("到达天数不能小于上一站的到达天数和出发天数");
							return ;
						}
					}
					if(obj!=null && obj!=undefined && nobj!=null && nobj!=undefined){
						  var ds2 =0;
							if(nobj.arrRunDays()!='--' && -obj.dptRunDays()!='--'){
								ds2 = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
							}
						if(ds2 <1 && nobj.arrTime() < obj.dptTime()){
							nobj.setColor(1);//设置颜色
							$("#isSave").attr('disabled',true);
							$("#isSave2").attr('disabled',true);
							showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
							return ;
						}
						if(parseInt(obj.dptRunDays()) > parseInt(nobj.dptRunDays())  ||  parseInt(obj.dptRunDays()) > parseInt(nobj.arrRunDays())){
						    nobj.setColor(2);//设置颜色
							$("#isSave").attr('disabled',true);
							$("#isSave2").attr('disabled',true);
							showErrorDialog("出发天数不能大于下一站的到达天数和出发天数");
							return ;
						}
					}
					if(obj!=null && obj!=undefined){
						var ds =0;
						if(obj.arrRunDays()!='--' &&  obj.dptRunDays()!='--'){
							ds = obj.dptRunDays()-obj.arrRunDays();//A站的出发天数-A站的到达天数>=1
						}
						if(parseInt(obj.arrRunDays()) > parseInt(obj.dptRunDays()) ){
							obj.setColor(2);//设置颜色
							$("#isSave").attr('disabled',true);
							$("#isSave2").attr('disabled',true);
							showErrorDialog("同一站的到达天数不能大于出发天数");
							return ;
						}
						if(obj.arrTime()!='--' && obj.dptTime()!='--'){
							if(ds <1 && obj.arrTime() > obj.dptTime()){
								obj.setColor(1);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("同一站的到达时间不能大于出发时间");
								return ;
							}
						}
					
					}
					
				}
			}
		    
		    
		    commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/saveLkTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainMap : ko.toJSON(_selectCmdTrainArray[0]),
					cmdTrainStnList : _saveTrainStnArray,
					baseTrainId : _self.useBaseTrainId()	//套用的基本图车次id
					// 新增字段
					,sourceNodeStationId : _self.sourceNodeStationId
					,sourceNodeStationName :_self.sourceNodeStationName
					,sourceNodeTdcsId :  _self.sourceNodeTdcsId
					,sourceNodeTdcsName : _self.sourceNodeTdcsName
					,targetNodeStationId : _self.targetNodeStationId
					,targetNodeStationName : _self.targetNodeStationName
					,targetNodeTdcsId : _self.targetNodeTdcsId
					,targetNodeTdcsName : _self.targetNodeTdcsName
					
				}),
				success : function(result) {
					
					// 保存成功后,将基本图id清空
					_self.useBaseTrainId("");
					//alert(_self.runPlanLkCMDRows())
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("保存成功");
						//清除
						_self.runPlanLkCMDRows.remove(function(item) {
							return true;
						});
						
						loadDataForBjddPage();
						checkRepeat = true;
					}else if(result != null && typeof result == "object" && result.code == "100"){
						$("#isSave").attr('disabled',true);
						showErrorDialog("此数据已经被其他用户更改，请重新刷新一次再操作!");
					}else {
						showErrorDialog("保存失败:" + result.message);
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
		 * 外局担当保存按钮点击事件
		 */
		_self.saveOtherCmdTxtMl = function() {
			if(_self.currentCmdTxtMl() == null || _self.currentCmdTxtMl().cmdTxtMlItemId()=="") {
				showWarningDialog("请选择选择一条临客命令记录");
				return;
			}
			if(_self.runPlanLkCMDTrainStnRows().length==0) {
				showWarningDialog("请补充列车时刻数据");
				return;
			}
			var isNull = false;
			var _saveTrainStnArray = [];
			$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
				var z=n+1;
				obj.stnName=$("#"+z+"wj").val();
				obj.stnNameTemp=$("#"+z+"wj").val();
				obj.currentBureauJc=$("#"+z+"wj100").find("option:selected").text();
				var strBur = $("#"+z+"jy100").find("option:selected").text();//获取页面下拉框的值
				obj.stnBureau=strBur;
				if(obj.nodeName()=="" || obj.nodeName()==undefined){
					showErrorDialog("站名不能为空");
					isNull = true;
					return false;
				}
				if(strBur==null || strBur=='' || strBur ==undefined){
					showErrorDialog("路局必能为空");
					isNull = true;
					return false;
				}
				obj.stnBureau=strBur;
				if(obj.arrTime()==undefined || obj.dptTime()=="" || obj.dptTime()==undefined){
					showErrorDialog("到达时间，出发时间不能为空");
					isNull = true;
					return false;
				}
//				if(obj.arrRunDays()!=0 ||obj.arrRunDays()!="0" || obj.arrRunDays()!='--'){
//					obj.setColor(2);//设置颜色
//					showErrorDialog("到达天数输入不正确");
//					isNull = true;
//					return false;
//				}
//				if(obj.dptRunDays()!=0 || obj.dptRunDays()!="0" || obj.dptRunDays()!='--'){
//					obj.setColor(2);//设置颜色
//					showErrorDialog("出发天数输入不正确");
//					isNull = true;
//					return false;
//				}
				 var pattern = /^([0-9]|--)$/;
				    if (!pattern.test(obj.arrRunDays())) {
				    	obj.setColor(2);//设置颜色	
						showErrorDialog("到达天数输入不正确");
						isNull = true;
						return false;
				    }
				    if (!pattern.test(obj.dptRunDays())) {
				    	obj.setColor(2);//设置颜色	
				    	showErrorDialog("出发天数输入不正确");
						isNull = true;
						return false;
				    }
				_saveTrainStnArray.push(ko.toJSON(obj));
//				console.log("obj======"+ko.toJSON(obj));
			});
			 if(isNull){
			    	return ;
			    }
			 

			 $('#isSave').removeAttr("disabled");
				$('#isSave2').removeAttr("disabled");
				//保存的时候验证
				if(_self.runPlanLkCMDTrainStnRows().length > 0){
					for(var i =0 ;i < _self.runPlanLkCMDTrainStnRows().length; i++){
						var obj = _self.runPlanLkCMDTrainStnRows()[i];
						var uobj =null;
						var nobj = null;
						if(i!=0){
							uobj = _self.runPlanLkCMDTrainStnRows()[i-1];
						}
						if(i!= _self.runPlanLkCMDTrainStnRows().length-1){
							nobj = _self.runPlanLkCMDTrainStnRows()[i+1];
						}
						if(i!=0 && i!=_self.runPlanLkCMDTrainStnRows().length-1){
							if(obj.arrRunDays()=='--' || obj.dptRunDays()=='--'){
								    obj.setColor(2);//设置颜色
									$("#isSave").attr('disabled',true);
									$("#isSave2").attr('disabled',true);
									showErrorDialog("经由站的到达天数和出发天数必须是数字");
									return ;
							}
							if(obj.arrTime() =="--" || obj.dptTime()=='--' ){
							    obj.setColor(2);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("到达时间或出发时间格式不正确");
								return ;
							}
							
						}
					  
						if(obj!=null && obj!=undefined && uobj!=null && uobj!=undefined){
							var ds =0;
							if(obj.arrRunDays()!='--' && -uobj.dptRunDays()!='--'){
								ds = obj.arrRunDays()-uobj.dptRunDays();//A站的出发天数-A站的到达天数>=1
							}
							if(ds <1 && obj.arrTime() < uobj.dptTime()){
								    uobj.setColor(1);//设置颜色
									$("#isSave").attr('disabled',true);
									$("#isSave2").attr('disabled',true);
									showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
									return ;
							}
							if(parseInt(obj.arrRunDays()) < parseInt(uobj.dptRunDays())  ||  parseInt(obj.arrRunDays()) <  parseInt(uobj.arrRunDays())){
							    uobj.setColor(2);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("到达天数不能小于上一站的到达天数和出发天数");
								return ;
							}
						}
						if(obj!=null && obj!=undefined && nobj!=null && nobj!=undefined){
							  var ds2 =0;
								if(nobj.arrRunDays()!='--' && -obj.dptRunDays()!='--'){
									ds2 = nobj.arrRunDays()-obj.dptRunDays();//A站的出发天数-A站的到达天数>=1
								}
							if(ds2 <1 && nobj.arrTime() < obj.dptTime()){
								nobj.setColor(1);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("上一行的出发时间不能大于下一行的到达时间");
								return ;
							}
							if(parseInt(obj.dptRunDays()) > parseInt(nobj.dptRunDays())  ||  parseInt(obj.dptRunDays()) > parseInt(nobj.arrRunDays())){
							    nobj.setColor(2);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("出发天数不能大于下一站的到达天数和出发天数");
								return ;
							}
						}
						if(obj!=null && obj!=undefined){
							var ds =0;
							if(obj.arrRunDays()!='--' &&  obj.dptRunDays()!='--'){
								ds = obj.dptRunDays()-obj.arrRunDays();//A站的出发天数-A站的到达天数>=1
							}
							if(parseInt(obj.arrRunDays()) > parseInt(obj.dptRunDays()) ){
								obj.setColor(2);//设置颜色
								$("#isSave").attr('disabled',true);
								$("#isSave2").attr('disabled',true);
								showErrorDialog("同一站的到达天数不能大于出发天数");
								return ;
							}
							if(i!=0 && i!=_self.runPlanLkCMDTrainStnRows().length-1){
								if(ds<1 && obj.arrTime() > obj.dptTime()){
									obj.setColor(1);//设置颜色
									$("#isSave").attr('disabled',true);
									$("#isSave2").attr('disabled',true);
									showErrorDialog("同一站的到达时间不能大于出发时间");
									return ;
								}
							}
						
						}
						
					}
				}
			 commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/saveOtherLkTrainTimes",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainMap : ko.toJSON(_self.currentCmdTxtMl()),
					cmdTrainStnList : _saveTrainStnArray
					
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("保存成功");
//						//清除
//						_self.runPlanLkCMDRows.remove(function(item) {
//							return true;
//						});
//						
//						loadDataForWjddPage();
					}else if(result != null && typeof result == "object" && result.code == "100"){
						$("#isSave2").attr('disabled',true);
						showErrorDialog("此数据已经被其他用户更改，请重新刷新一次再操作!");
					}else if(result != null && typeof result == "object" && result.code == "M12"){
						showWarningDialog("操作取消！当前车次已生成开行计划。");
					}else {
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
		 * 上移
		 */
		_self.up = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要调整顺序的记录");
				return;
			}
			
			if(_self.currentCmdTrainStn().childIndex() == 0) {
				showWarningDialog("当前记录顺序号已经为最小，不能执行上移操作");
				return;
			}
			
			
			//设置序号
			var _arrayList = _self.runPlanLkCMDTrainStnRows();
			for(var i=0;i<_arrayList.length;i++) {
				if(i == (_self.currentCmdTrainStn().childIndex())) {
					
					_arrayList[i].childIndex(i-1);
					_arrayList[i-1].childIndex(i);
					_self.runPlanLkCMDTrainStnRows.splice(i - 1, 2, _arrayList[i], _arrayList[i-1]);
					break;
				}
			}
			checkRepeat = false;
			isZJ =  false;
		};
		
		
		
		/**
		 * 下移
		 */
		_self.down = function() {
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要调整顺序的记录");
				return;
			}

			var _maxLen = _self.runPlanLkCMDTrainStnRows().length -1;
			if(_self.currentCmdTrainStn().childIndex() == _maxLen) {
				showWarningDialog("当前记录顺序号已经为最大，不能执行下移操作");
				return;
			}
			
			//设置序号
			var _arrayList = _self.runPlanLkCMDTrainStnRows();
			for(var i=0;i<_arrayList.length;i++) {
				if(i == (_self.currentCmdTrainStn().childIndex())) {
					
					_arrayList[i].childIndex(i+1);
					_arrayList[i+1].childIndex(i);
					_self.runPlanLkCMDTrainStnRows.splice(i , 2, _arrayList[i+1], _arrayList[i]);
					break;
				}
			}
			checkRepeat = false;
			isZJ =  false;
		};
		
		
		

		
		/**
		 * 生成开行计划按钮点击事件
		 */
		_self.batchCreateRunPlanLine = function() {
			var _saveCmdTrainArray = [];
			for(var i=0;i<_self.runPlanLkCMDRows().length;i++){
				var obj = _self.runPlanLkCMDRows()[i];
				if (obj.isSelect() == 1) {
					if (1 == obj.createState()) {
						
						
							/*showWarningDialog("序号【"+(i+1)+"】  发令日期【"+obj.cmdTime()+"】 局令号【"+obj.cmdNbrBureau()+"】 项号【"+obj.cmdItem()+"】的临客命令尚未设置时刻表数据，不能生成开行计划");
							return;*/
					
						showConfirmDiv("提示", "序号【"+(i+1)+"】  发令日期【"+obj.cmdTime()+"】 局令号【"+obj.cmdNbrBureau()+"】 项号【"+obj.cmdItem()+"】已经生成计划", function (r) { 
					        if (r) { 
					        	_saveCmdTrainArray.push(obj.cmdTrainId());
					        	commonJsScreenLock();
								$.ajax({
									url : basePath+"/runPlanLk/batchCreateRunPlanLine1",
									cache : false,
									type : "POST",
									dataType : "json",
									contentType : "application/json",
									data :JSON.stringify({
										cmdTrainIds :_saveCmdTrainArray,
										
									}),
									success : function(result) {
										
										if (result != null && typeof result == "object" && result.code == "0") {
											showSuccessDialog("生成开行计划成功");
											//清除
											_self.runPlanLkCMDRows.remove(function(item) {
												return true;
											});
											
											loadDataForBjddPage();
										} else {
											showErrorDialog("生成开行计划失败");
										};
									},
									error : function() {
										showErrorDialog("生成开行计划失败");
									},
									complete : function(){
										commonJsScreenUnLock();
									} 
								}); 
								
					        }
					       
						}); 
						 return;
					}
					if (null == obj.passBureau()) {
					showWarningDialog("序号【"+(i+1)+"】  发令日期【"+obj.cmdTime()+"】 局令号【"+obj.cmdNbrBureau()+"】 项号【"+obj.cmdItem()+"】的临客命令尚未设置时刻表数据，不能生成开行计划");
					return;
				}
					
					_saveCmdTrainArray.push(obj.cmdTrainId());
				}
			}
			
			
			if(_saveCmdTrainArray.length==0) {
				showWarningDialog("请选择临客命令记录");
				return;
			}
			
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/batchCreateRunPlanLine",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainIds : _saveCmdTrainArray,
					
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("生成开行计划成功");
						//清除
						_self.runPlanLkCMDRows.remove(function(item) {
							return true;
						});
						
						loadDataForBjddPage();
					} else {
						showErrorDialog("生成开行计划失败");
					};
				},
				error : function() {
					showErrorDialog("生成开行计划失败");
				},
				complete : function(){
					commonJsScreenUnLock(1);
				}
			});
		};
		
		
		

		/**
		 * 插入行
		 * 追加到当前选中行之前
		 */
		_self.insertTrainStnRow = function() {
//			var index =0;
			var _maxRowLen = _self.runPlanLkCMDTrainStnRows().length;//追加记录前集合大小
//			if(_maxRowLen==0){
//				index =0;
//			}else{
//				index = _maxRowLen;
//			}

			//1.当_maxRowLen==0时，直接push
			if (_maxRowLen == 0) {
				_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow({
					childIndex:0,
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
					nodeStationId : "",
					nodeTdcsId  : "",
					nodeTdcsName : "",
					nodeStationName : ""
				}));
				$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
						_self.currentCmdTrainStn(obj);
				});
				return;
			}
			
			
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
				|| _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要插入记录的位置");
				return;
			}

			//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
			_self.runPlanLkCMDTrainStnRows.splice(_self.currentCmdTrainStn().childIndex(), 0, new _self.cmdTrainStnTimeRow({
				childIndex:_self.currentCmdTrainStn().childIndex(),
				stnName:"",
				arrTrainNbr:"",
				dptTrainNbr:"",
				stnBureau:"",
				arrTime:"",
				dptTime:"",
				trackName:"",
				platform:"",
				arrRunDays:_self.currentCmdTrainStn().arrRunDays()=='--'?_self.currentCmdTrainStn().dptRunDays():_self.currentCmdTrainStn().arrRunDays(),
				dptRunDays:_self.currentCmdTrainStn().dptRunDays()=='--'?_self.currentCmdTrainStn().arrRunDays():_self.currentCmdTrainStn().dptRunDays(),
				nodeId:_self.currentCmdTrainStn().nodeId(),
				jobs:"",
				nodeName:"",
				bureauId:"",
				nodeStationId : "",
				nodeTdcsId  : "",
				nodeTdcsName : "",
				nodeStationName : ""
			}));

			//2.2.设置序号
			for(var i=0;i<_self.runPlanLkCMDTrainStnRows().length;i++) {
				if(i > (_self.currentCmdTrainStn().childIndex())) {//从新插入记录位置开始childIndex  + 1
					_self.runPlanLkCMDTrainStnRows()[i].childIndex(_self.runPlanLkCMDTrainStnRows()[i].childIndex()+1);//当前行位置后的所有记录childIndex加1
				
//					break;
				}
				_self.runPlanLkCMDTrainStnRows()[i].currentBureauJc(_self.currentCmdTrainStn().stnBureau);//点插入行的时候最后一次的路径的值会变成默认局，所以要重新设置
			}
			checkRepeat = false;
			isCR = true;
			isAdd = true;
			

		};
		
		

		/**
		 * 追加行
		 * 追加到当前选中行之后
		 */
		_self.addTrainStnRow = function() {
			var index =0;
			var _maxRowLen = _self.runPlanLkCMDTrainStnRows().length;//追加记录前集合大小
			if(_maxRowLen==0){
				index =0;
			}else{
				index = _maxRowLen;
			}
            console.log("_maxRowLen==="+_maxRowLen);
			//1.当_maxRowLen==0时，直接push
			if (_maxRowLen == 0 || _self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
				|| _self.currentCmdTrainStn().childIndex()<0) {
				_self.runPlanLkCMDTrainStnRows.push(new _self.cmdTrainStnTimeRow({
					childIndex:index,
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
					nodeStationId : "",
					nodeTdcsId  : "",
					nodeTdcsName : "",
					nodeStationName : ""
				}));
				return;
			}
			
			
//			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" 
//				|| _self.currentCmdTrainStn().childIndex()<0) {
//				showWarningDialog("请选择时刻表中需要追加记录的位置");
//				return;
//			}

			//2.1.增加行	splice(start,deleteCount,val1,val2,...)：从start位置开始删除deleteCount项，并从该位置起插入val1,val2,... 
			_self.runPlanLkCMDTrainStnRows.splice(_self.currentCmdTrainStn().childIndex()+1, 0, new _self.cmdTrainStnTimeRow({
				childIndex:_self.currentCmdTrainStn().childIndex()+1,
				stnName:"",
				arrTrainNbr:"",
				dptTrainNbr:"",
				stnBureau:"",
				arrTime:"",
				dptTime:"",
				trackName:"",
				platform:"",
				arrRunDays:_self.currentCmdTrainStn().arrRunDays()=='--'?_self.currentCmdTrainStn().dptRunDays():_self.currentCmdTrainStn().arrRunDays(),
				dptRunDays:_self.currentCmdTrainStn().dptRunDays()=='--'?_self.currentCmdTrainStn().arrRunDays():_self.currentCmdTrainStn().dptRunDays(),
				nodeId:_self.currentCmdTrainStn().nodeId(),
				jobs:"",
				nodeName:"",
				bureauId:"",
				nodeStationId : "",
				nodeTdcsId  : "",
				nodeTdcsName : "",
				nodeStationName : ""
			}));
	          console.log("_self.runPlanLkCMDTrainStnRows().length==="+_self.runPlanLkCMDTrainStnRows().length);
			//2.2.设置序号
			if(_self.currentCmdTrainStn().childIndex() != _maxRowLen-1) {//当前选中行不为增加前集合中最后一行   则移除后集合需要重新排序
				for(var i=0;i<_self.runPlanLkCMDTrainStnRows().length;i++) {
					if(i > (_self.currentCmdTrainStn().childIndex()+1)) {//从新插入记录位置开始childIndex  + 1
						_self.runPlanLkCMDTrainStnRows()[i].childIndex(_self.runPlanLkCMDTrainStnRows()[i].childIndex()+1);//当前行位置后的所有记录childIndex加1
					}
				}
			}
			checkRepeat = false;
			isZJ =  true;
			isAdd = true;
		};
		
		

		/**
		 * 删除行
		 */
		_self.deleteTrainStnRow = function() {
			var temp = _self.currentCmdTrainStn().childIndex();  //当前选中行的索引
			if(_self.currentCmdTrainStn()==null || _self.currentCmdTrainStn()=="undefind" || _self.currentCmdTrainStn().childIndex()<0) {
				showWarningDialog("请选择时刻表中需要移除的记录");
				return;
			}
			
			if(_self.currentCmdTrainStnS().length > 1){
				$.each(_self.currentCmdTrainStnS(),function(n, obj){
					//从集合中移除
					_self.runPlanLkCMDTrainStnRows.remove(obj);
				});
			}else{
				//从集合中移除
				_self.runPlanLkCMDTrainStnRows.remove(_self.currentCmdTrainStn());
			}
			
			//清除当前选中项值
			_self.cleanCurrentCmdTrainStn();//清除时刻列表当前选中值
			
//			var _maxRowLen = _self.runPlanLkCMDTrainStnRows().length;
//			if(_self.currentCmdTrainStn().childIndex() != _maxRowLen-1) {//移除行不为移除前集合中最后一行   则移除后集合需要重新排序
				//重新设置childIndex			   
				$.each(_self.runPlanLkCMDTrainStnRows(),function(n, obj){
					obj.childIndex(n);
					obj.currentBureauJc(obj.stnBureau);
					if(n == temp){
						_self.currentCmdTrainStn(obj);
					}
				});
//			}
				
			checkRepeat = false;
		};
		
		
		
		_self.wdCmdModelTitleLast = ko.observable("");//文电新增、编辑弹出框title后半部分
		_self.wdCmdModelTitleFirst = ko.observable("");//文电新增、编辑弹出框title前半部分
		
		/**
		 * 本局担当命令类型下拉框change事件
		 "1", "text": "既有线加开""code": "2", "text": "高铁加开"  code:"3", "text": "既有线停运"    code:"4", "text": "高铁停运,
		{"code": "5", "text": "文电加开"},
		{"code": "6", "text": "文电停运"}
		 */
		_self.cmdTypeChangeEvent = function() {
			_self.cleanPageData();
			switch(_self.searchModle().cmdTypeOptionBjdd().code) {		
				case "1"://"1", "text": "既有线加开"
					//屏蔽文电相关
					$("#wdx").hide();//文电项标签
					$("#bjdd_btn_qmlsk").show();//显示取命令时刻按钮
					$("#bjdd_btn_addWdCmd").hide();//屏蔽添加文电按钮
					$("#bjdd_btn_editWdCmd").hide();//屏蔽修改文电按钮
					$("#bjdd_btn_deleteWdCmd").hide();//屏蔽删除文电按钮
					$("#div_bjdd_cmdTrainTable_wd").hide();//屏蔽文电命令table
					$("#div_bjdd_cmdTrainTable_wd_stop").hide();//屏蔽文电停运命令table
					$("#div_bjdd_cmdTrainTable_ty").hide();//屏蔽停运table

					$("#bjdd_btn_selectLine").show();//显示选线按钮
					$("#div_bjdd_cmdTrainTable_jk").show();//显示加开table
					break;
				case "2"://"2", "text": "高铁加开"
					//屏蔽文电相关
					$("#wdx").style.display="none";//文电项标签
					$("#bjdd_btn_qmlsk").hide();//屏蔽取命令时刻按钮
					$("#bjdd_btn_addWdCmd").hide();//屏蔽添加文电按钮
					$("#bjdd_btn_editWdCmd").hide();//屏蔽修改文电按钮
					$("#bjdd_btn_deleteWdCmd").hide();//屏蔽删除文电按钮
					$("#div_bjdd_cmdTrainTable_wd").hide();//屏蔽文电命令table
					$("#div_bjdd_cmdTrainTable_wd_stop").hide();//屏蔽文电停运命令table
					$("#div_bjdd_cmdTrainTable_ty").hide();//屏蔽停运table
					
					$("#bjdd_btn_selectLine").show();//显示选线按钮
					$("#div_bjdd_cmdTrainTable_jk").show();//显示加开table
					break;
				case "5":
				case "7"://{"code": "5", "text": "文电加开"},
					$("#wdx").show();//文电项标签
					$("#bjdd_btn_qmlsk").hide();//屏蔽取命令时刻按钮
					$("#div_bjdd_cmdTrainTable_ty").hide();//屏蔽停运table
					$("#div_bjdd_cmdTrainTable_jk").hide();//屏蔽加开table
					$("#div_bjdd_cmdTrainTable_wd_stop").hide();//屏蔽文电停运命令table
					
					_self.wdCmdModelTitleLast("文电加开");//设置文电新增、编辑弹出框title后半部分
					$("#bjdd_btn_addWdCmd").attr("data-target", "#bjddAddWdjkCmdTrainModal");//设置按钮点击时弹出框为加开
					$("#bjdd_btn_addWdCmd").show();//显示添加文电按钮
					$("#bjdd_btn_editWdCmd").attr("data-target", "#bjddAddWdjkCmdTrainModal");//设置按钮点击时弹出框为加开
					$("#bjdd_btn_editWdCmd").show();//显示修改文电按钮
					$("#bjdd_btn_deleteWdCmd").show();//显示删除文电按钮
					$("#bjdd_btn_selectLine").show();//显示选线按钮
					$("#div_bjdd_cmdTrainTable_wd").show();//显示文电加开table
					break;
				case "6"://{"code": "6", "text": "文电停运"}
					$("#div_bjdd_cmdTrainTable_ty").hide();//屏蔽停运table
					$("#div_bjdd_cmdTrainTable_jk").hide();//屏蔽加开table
					$("#div_bjdd_cmdTrainTable_wd").hide();//屏蔽文电命令table
					$("#bjdd_btn_selectLine").hide();//屏蔽选线按钮

					_self.wdCmdModelTitleLast("文电停运");//设置文电新增、编辑弹出框title后半部分
					$("#bjdd_btn_addWdCmd").attr("data-target", "#bjddAddWdtyCmdTrainModal");//设置按钮点击时弹出框为停运
					$("#bjdd_btn_addWdCmd").show();//屏蔽添加文电按钮
					$("#bjdd_btn_editWdCmd").attr("data-target", "#bjddAddWdtyCmdTrainModal");//设置按钮点击时弹出框为停运
					$("#bjdd_btn_editWdCmd").show();//屏蔽修改文电按钮
					$("#bjdd_btn_deleteWdCmd").show();//屏蔽删除文电按钮
					$("#div_bjdd_cmdTrainTable_wd_stop").show();//显示文电加开table
					break;
				default: 
					//code:"3", "text": "既有线停运"    code:"4", "text": "高铁停运,
					//屏蔽文电相关
					$("#wdx").style.display="none";//文电项标签
					$("#bjdd_btn_qmlsk").hide();//屏蔽取命令时刻按钮
					$("#bjdd_btn_addWdCmd").hide();//屏蔽添加文电按钮
					$("#bjdd_btn_editWdCmd").hide();//屏蔽修改文电按钮
					$("#bjdd_btn_deleteWdCmd").hide();//屏蔽删除文电按钮
					$("#div_bjdd_cmdTrainTable_wd").hide();//屏蔽文电命令table
					$("#div_bjdd_cmdTrainTable_wd_stop").hide();//屏蔽文电停运命令table
					$("#div_bjdd_cmdTrainTable_jk").hide();//屏蔽加开table
					$("#bjdd_btn_selectLine").hide();//显示选线按钮
					
					$("#div_bjdd_cmdTrainTable_ty").show();//显示停运table
					break;
			}
			
		};
		
		
		
		/**
		 * 外局担当命令类型下拉框change事件
		 "1", "text": "既有线加开""code": "2", "text": "高铁加开"  
		{"code": "5", "text": "文电加开"},
		 */
		_self.cmdTypeWjddChangeEvent = function() {
			_self.cleanPageData();
			if (_self.searchModle().cmdTypeOptionWjdd().code == "1"
				|| _self.searchModle().cmdTypeOptionWjdd().code == "2") {
					$("#div_wjdd_cmdTrainTable_jk").show();//显示外局担当 既有、高铁加开table div
					$("#div_wjdd_cmdTrainTable_wd").hide();//屏蔽外局担当文电加开table div
			} else if (_self.searchModle().cmdTypeOptionWjdd().code == "5") {
				$("#div_wjdd_cmdTrainTable_jk").hide();//屏蔽外局担当 既有、高铁加开table div
				$("#div_wjdd_cmdTrainTable_wd").show();//显示外局担当文电加开table div
			} else {
				$("#div_wjdd_cmdTrainTable_jk").hide();//屏蔽外局担当 既有、高铁加开table div
				$("#div_wjdd_cmdTrainTable_wd").hide();//屏蔽外局担当文电加开table div
			}
		};
		
		
		
		/**
		 * 编辑文电页面打开时
		 */
		_self.onWdCmdEditOpen = function() {
			var num = 0;
			var i;
			var obj = null;
			var objSelect = null;
			for(i = 0;i<_self.runPlanLkCMDRows().length;i++){
				obj = _self.runPlanLkCMDRows()[i];
				if (obj.isSelect() == 1) {
					num++;
					objSelect = _self.runPlanLkCMDRows()[i];
				}
			}
			
			if(num > 1) {
				showWarningDialog("您选中了多行记录，请选中一行");
				return;
			}
			else if(num == 0){
				showWarningDialog("请选择文电命令记录");
				return;
			}
			else {

//				console.log(objSelect);
				if(1 == objSelect.createState()) {
					showWarningDialog("您所选中的命令中含有已经生成开行计划的命令，不能编辑");
					return;
				}
					//obj.cmdTrainId();	
			}
			//ajax 加载页面
			var path = "";
			var title = "";
			var cmdTypeText = "";
			switch(_self.searchModle().cmdTypeOptionBjdd().code) {
				case "5":
					path = "/runPlanLk/wdAddEditPage";
					title = "既有加开文电";
					cmdTypeText = "既有加开文电";
					break;
				case "7":
					path = "/runPlanLk/wdAddEditPage";
					title = "高铁加开文电";
					cmdTypeText = "高铁加开文电";
					break;
				default : 
					break;
			}	
//			_self.wdCmdModelTitleFirst("新增");//文电新增、编辑弹出框title前半部分
//			$("#input_wd_cmdTime").datepicker();//文电日期输入框
//			$("#input_wd_startDate").datepicker();//文电起始日期输入框
//			$("#input_wd_endDate").datepicker();//文电终止日期输入框
			
			//if(!$("#add_wd_cmd_info_dialog").is(":hidden")){
			
				
				$("#add_wd_cmd_info_dialog").find("iframe").attr("src", basePath + path).attr("name", objSelect.cmdTrainId()).attr("id", cmdTypeText);
				$('#add_wd_cmd_info_dialog').dialog({title: title, autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#add_wd_cmd_info_dialog").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#add_wd_cmd_info_dialog').height();
						var WW = $('#add_wd_cmd_info_dialog').width();
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
			_self.wdCmdTrain(new WdCmdTrainModel(createEmptyCmdTrainRow()));//创建一个空对象

			$("#input_wd_cmdTime").val(moment().subtract("day", 1).format('YYYY-MM-DD'));
			$("#input_wd_startDate").val(moment().subtract("day", 1).format('YYYY-MM-DD'));
			$("#input_wd_endDate").val(moment().format('YYYY-MM-DD'));
			
		};
		
		
		
		/**
		 * 删除文电信息
		 */
		_self.deleteWdCmd = function() {
			
			
			//var _deleteCmdTrainStr = "";
			var _deleteCmdTrains = [];
			for(var i=0;i<_self.runPlanLkCMDRows().length;i++){
				var obj = _self.runPlanLkCMDRows()[i];
				if (obj.isSelect() == 1) {
					if(1 == obj.createState()) {
						showWarningDialog("您所选中的命令中含有已经生成开行计划的命令，不能删除");
						return;
					}
					_deleteCmdTrains.push(obj.cmdTrainId());
//					console.log("obj.createState()" + obj.createState());
				}
			}
			
			
			if(_deleteCmdTrains.length == 0) {
				showWarningDialog("请选择文电命令记录");
				return;
			} 
			
			showConfirmDiv("提示", "你确定要执行删除操作?", function (r) { 
		        if (r) { 
					commonJsScreenLock(1);
					$.ajax({
						url : basePath+"/runPlanLk/deleteWdCmd",
						cache : false,
						type : "POST",
						dataType : "json",
						contentType : "application/json",
						data :JSON.stringify({
							cmdTrainIds : _deleteCmdTrains
						}),
						success : function(result) {
							if (result != null && typeof result == "object" && result.code == "0") {
								showSuccessDialog("删除成功");
								_self.cleanPageData();

								//2.查询本局担当命令信息
								loadDataForBjddPage();	//loadRows方法
							} else {
								showErrorDialog("删除失败");
							};
						},
						error : function() {
							commonJsScreenUnLock(1);
							showErrorDialog("删除失败");
						},
						complete : function(){
							commonJsScreenUnLock(1);
						}
					});
		        };
			});
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
			//路局文电号
			_self.cmdNbrBureau = ko.observable(data.cmdNbrBureau).extend({
	            required: {message: "请输入路局文电号" }
	        });
			_self.cmdItem = ko.observable(data.cmdItem).extend({
	            required: {message: "请输入项号" }
	        });
			_self.cmdNbrSuperior = ko.observable(data.cmdNbrSuperior).extend({
	            required: {message: "请输入总公司文电号" }
	        });
			_self.trainNbr = ko.observable(data.trainNbr).extend({
	            required: {message: "请输入车次" }
	        });
			_self.startStn = ko.observable(data.startStn).extend({
	            required: {message: "请输入始发站" }
	        });
			_self.endStn = ko.observable(data.endStn).extend({
	            required: {message: "请输入终到站" }
	        });
			_self.wdCmdRuleArray = ko.observableArray(wdCmdRuleArray);	//文电加开开行规律下拉框
			_self.wdCmdRuleOption = ko.observable();					//文电加开开行规律下拉框选中项
			_self.rule = ko.observable().extend({
	            required: {message: "请选择开行规律" }
	        });
			_self.selectedDate = ko.observable(data.selectedDate);
			_self.startDate = ko.observable(data.startDate).extend({
	            required: { params: true, message: "请输入起始日期" },
	            pattern: {
	                params: "[2-9]\\d{4}\-[0-9]{2}\-[0-9]{2}$",
	                message: "格式不正确，正确格式如：2014-12-31"//yyyy-mm-dd
	            }
	            
	        });
			_self.endDate = ko.observable(data.endDate).extend({
	            required: {message: "请输入终止日期" },
	            pattern: {
	                params: "[2-9]\\d{4}\-[0-9]{2}\-[0-9]{2}$",
	                message: "格式不正确，正确格式如：2014-12-31"//yyyy-mm-dd
	            }
	        });
			_self.passBureau = ko.observable(data.passBureau);
			_self.updateTime = ko.observable(data.updateTime);
			_self.cmdTime = ko.observable(data.cmdTime);
			_self.isExsitStn = ko.observable(data.isExsitStn);
			_self.userBureau = ko.observable(data.userBureau);
			_self.selectState = ko.observable(data.selectState);
			_self.createState = ko.observable(data.createState);
		};
		_self.wdCmdTrain = ko.observable();//文电新增、编辑界面绑定对象
		/**
		 * 添加文电按钮点击事件
		 * 
		 */
		_self.onWdCmdAddOpen = function() {
			var path = "";
			var title = "";
			var cmdTypeText = "";
			switch(_self.searchModle().cmdTypeOptionBjdd().code) {
				case "5":
					path = "/runPlanLk/wdAddPage";
					title = "既有加开文电";
					cmdTypeText = "既有加开文电";
					break;
				case "7":
					path = "/runPlanLk/wdAddPage";
					title = "高铁加开文电";
					cmdTypeText = "高铁加开文电";
					break;
				default : 
					break;
			}	
//			_self.wdCmdModelTitleFirst("新增");//文电新增、编辑弹出框title前半部分
//			$("#input_wd_cmdTime").datepicker();//文电日期输入框
//			$("#input_wd_startDate").datepicker();//文电起始日期输入框
//			$("#input_wd_endDate").datepicker();//文电终止日期输入框
			
			//if(!$("#add_wd_cmd_info_dialog").is(":hidden")){
			
				
				$("#add_wd_cmd_info_dialog").find("iframe").attr("src", basePath + path).attr("id", cmdTypeText);
				$('#add_wd_cmd_info_dialog').dialog({title: title, autoOpen: true, modal: false, draggable: true, resizable:true,
					onResize:function() {
						var iframeBox = $("#add_wd_cmd_info_dialog").find("iframe");
						var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
						var WH = $('#add_wd_cmd_info_dialog').height();
						var WW = $('#add_wd_cmd_info_dialog').width();
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
			_self.wdCmdTrain(new WdCmdTrainModel(createEmptyCmdTrainRow()));//创建一个空对象

			$("#input_wd_cmdTime").val(moment().subtract("day", 1).format('YYYY-MM-DD'));
			$("#input_wd_startDate").val(moment().subtract("day", 1).format('YYYY-MM-DD'));
			$("#input_wd_endDate").val(moment().format('YYYY-MM-DD'));
		};
		
		/**
		 * 更新站点作业信息
		 */
		_self.editJobs = function(row) {
			//1.清除数据
			_self.setCMDTrainStnCurrentRec(row);
			
//			if(row.permissionDenied()) {
//				$("#editJobsModal").prop("aria-hidden", "true");
//				$("#editJobsModal").hide();
//				return;//没有权限
//			}
			
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
			if(jobsTypeSubstring.indexOf("经由")<0){
				_items.unshift(jobsTypeSubstring);
			}
//			_self.currentCmdTrainStn().job.items = _items;
			_self.currentCmdTrainStn().jobItems(_items);
			
			$.each(_self.runPlanLkCMDTrainStnRows(), function(i, obj){
				if(obj.childIndex()==_self.currentCmdTrainStn().childIndex()){
//					obj.data.job.items = _items;
					obj.jobItems(_items);
					obj.jobs('<'+_items.join('><')+'>');
				}
			});
			checkRepeat = false;
		};
		
		/**
		 * 文电新增界面 保存按钮
		 
		_self.btnEventSaveWdCmdTrain = function() {
			//设置开行规律
			if (_self.wdCmdTrain().wdCmdRuleOption()!=null && _self.wdCmdTrain().wdCmdRuleOption()!="undefined") {
				_self.wdCmdTrain().rule(_self.wdCmdTrain().wdCmdRuleOption().code);
			}
			console.dir(ko.toJSON(_self.wdCmdTrain()));
			
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/runPlanLk/saveWdCmdTrain",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					cmdTrainMap : ko.toJSON(_self.wdCmdTrain())
				}),
				success : function(result) {
					
					if (result != null && typeof result == "object" && result.code == "0") {
						showSuccessDialog("保存成功");
						loadDataForBjddPage();
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
		
*/
		/**
		 * 文电新增界面 重置按钮
		
		_self.btnEventResetWdCmdTrain = function() {
			console.log("reset!!");
		};
		 */
		self.isShowRunPlans = ko.observable(true);// 显示车底与乘务   默认不勾选
		self.showRunPlans = function(){
			var WH = $(window).height();
			if(!self.isShowRunPlans()) {//勾选
				$('#time_div').css("opacity",1);
				$("#time_div2").css("opacity",1);
				$("#left_div").css("margin-right",-590+"px");
				$("#left_div_panel").attr("style","margin-right:590px;");
				
				$("#time_div").css("width",580+"px");
				$("#right_div").attr("style","margin-right:-590px; float:left; width:100%;");
				$("#right_div_panel").attr("style","margin-right:590px;");
				$("#time_div2").css("width",580+"px");
				
			} else {//未勾选
				$('#time_div').css("opacity",0);
				$("#left_div").css("margin-right",0+"px");
				
				$("#left_div_panel").attr("style","margin-right:0px;");
				
				$("#time_div2").css("opacity",0);
				$("#right_div").attr("style","margin-right:0px; float:left; width:100%;");
				$("#right_div_panel").attr("style","margin-right:0px;");
			}
		};
		
		
		/////model end
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
	heightAuto();
	
	$("#add_wd_cmd_info_dialog").dialog("close");
	
	new RunPlanLkCmdPage().initPage();
	$('#isSave').removeAttr("disabled");
	$('#isSave2').removeAttr("disabled");

});

function heightAuto(){
    var WH = $(window).height();
	//alert("window: 860---"+WH);
	$("#left_height").css("height",WH-250+"px");
	$("#time1").css("height",WH-310+"px");
	$("#left_height2").css("height",WH-220+"px");
	$("#time2").css("height",WH-310+"px");
	
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
