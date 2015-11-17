
var runTimeModel;
var myCanvasComponent = null;
var lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
var jlList = [];	//用于保存交路数据元素，以便重新绘图

var canvas = document.getElementById("canvas_event_getvalue");
var context = canvas.getContext('2d');

var runPlanCanvasPage = null;
var currentXScale = 10;	//x轴缩放比例
var currentXScaleCount = 1;//x轴放大总倍数
var currentYScale = 1;	//y轴缩放比例
var currentYScaleCount = 1;//y轴放大总倍数
var _canvas_select_groupSerialNbr = null;
var _currentGroupSerialNbr = "";		//当前组号
    
var RunPlanCanvasPage = function(cross) {
	var _self = this; 
	_self.app = cross;
	
	
	/**
	 * 绘图条件绑定模型
	 * private
	 */
	function CanvasQueryModle(){
		_self = this;
		_self.drawFlags =ko.observableArray(['0']);		//分界口、停站、经由站复选框 "0"表示显示始发及终到
		_self.drawFlagChange = function(a, n){			//分界口、停站、经由站复选框点击事件
			if(n.target.checked){
				_self.drawFlags.push(n.target.value);
			}else{
				self.removeArrayValue(_self.drawFlags(), n.target.value);
			}
			runPlanCanvasPage.drawChart({startX:100}); 
		};
		
//		_self.canvasIsDrawTrainTime = ko.observable();
	}
	
	
	function removeArrayValue(arr, value){
		var index = -1;
		for(var i = 0 ; i < arr.length; i++){
			if(value == arr[i]){
				index = i;
				break;
			}
		}
		if(index > -1){
			arr.splice(index, 1);
		}
	};
	
	
	/**
	 * 获取绘图扩展条件对象
	 * @param scale
	 * @returns
	 */
	function getScale(scale) {
		//生成分界口、停站标记
		var _stationTypeArray = ["0"];
		$("[name='canvas_checkbox_stationType']").each(function(){
			if($(this).is(":checked")) {
				//显示所有 包含始发、终到、分界口、停站、不停站
//				_stationTypeArray = ["0","FJK","TZ","BT"];//"0","FJK","TZ","BT"
				_stationTypeArray = ["0","FJK"];//"0","FJK","TZ","BT"
//				removeArrayValue(_stationTypeArray, $(this).val());
			} else {
				//查看简图 只包含始发、终到
				_stationTypeArray = ["0","FJK","TZ"];
				//_stationTypeArray.push($(this).val());
			}
	    });
		//保留方便以后复选框扩展
//		if (_stationTypeArray.length >0) {
//			_stationTypeArray.push("0");	//增加显示始发及终到
//		}
		
		
		var _canvasIsDrawTrainTime = false;	//是否绘制列车经由站到达及出发时间
		if($("#canvas_checkbox_trainTime").is(":checked")){
			_canvasIsDrawTrainTime = true;
		} else {
			_canvasIsDrawTrainTime = false;
		}
		
		
		
		if (scale && scale!=null && scale!="undefine" && typeof scale == "object") {
			scale.xScale = currentXScale;				//x轴缩放比例
			scale.xScaleCount = currentXScaleCount;	//x轴当前放大倍数，用于控制时刻线条数
			scale.yScale = currentYScale;				//y轴缩放比例
			scale.stationTypeArray = _stationTypeArray;	//分界口复选框
			scale.isDrawTrainTime = _canvasIsDrawTrainTime;	//是否绘制列车经由站到达及出发时间
//			scale.isZgsUser = _isZgsUser;//当前用户是否为总公司用户
			return scale;
		} else {
			return {
				xScale : currentXScale,				//x轴缩放比例
				xScaleCount : currentXScaleCount,	//x轴当前放大倍数，用于控制时刻线条数
				yScale : currentYScale,				//y轴缩放比例
				stationTypeArray:_stationTypeArray,	//分界口复选框
				isDrawTrainTime:_canvasIsDrawTrainTime	//是否绘制列车经由站到达及出发时间
			};
		}
		
		
		
		
	};
	
	
	
	/**
	 * public
	 */
	this.initPage = function() {

		//初始化事件类
		new CanvasEventComponent("canvas_event_getvalue");
		
	    

		//x放大2倍
		$("#canvas_event_btn_x_magnification").click(function(){
			if (currentXScaleCount == 32) {
				showErrorDialog("当前已经不支持继续放大啦！");
				return;
			}
			//计算画布比例及倍数
			currentXScale = currentXScale/2;
			currentXScaleCount = currentXScaleCount*2;
			$("#canvas_event_label_xscale").text(currentXScaleCount);
			
			_self.drawChart();
		});
		

		//x缩小2倍
		$("#canvas_event_btn_x_shrink").click(function(){
			if (currentXScaleCount == 0.25) {
				showErrorDialog("当前已经不支持继续缩小啦！");
				return;
			}
			//计算画布比例及倍数
			currentXScale = currentXScale*2;
			currentXScaleCount = currentXScaleCount/2;
			$("#canvas_event_label_xscale").text(currentXScaleCount);
			
			_self.drawChart();
		});
		

		//y放大2倍
		$("#canvas_event_btn_y_magnification").click(function(){
			if (currentYScaleCount == 8) {
				showErrorDialog("当前已经不支持继续放大啦！");
				return;
			}
			
			//计算画布比例及倍数
			currentYScale = currentYScale/2;
			currentYScaleCount = currentYScaleCount*2;
			$("#canvas_event_label_yscale").text(currentYScaleCount);
			
			_self.drawChart();
		});
		

		//y缩小2倍
		$("#canvas_event_btn_y_shrink").click(function(){
			if (currentYScaleCount == 0.25) {
				showErrorDialog("当前已经不支持继续缩小啦！");
				return;
			}
			
			//计算画布比例及倍数
			currentYScale = currentYScale*2;
			currentYScaleCount = currentYScaleCount/2;
			$("#canvas_event_label_yscale").text(currentYScaleCount);
			
			_self.drawChart();
		});
		
		

		//刷新按钮点击事件
		$("#canvas_event_btn_refresh").click(function(){
			_self.drawChart();
		});
		

		//显示停站时刻 复选框事件
		$("#canvas_checkbox_trainTime").click(function(){
			_self.drawChart();
			
		});
		

		//分界口 复选框事件
		$("#canvas_checkbox_stationType_jt").click(function(){
			_self.drawChart();
			
		});
		
		
	};
	
	
	/**
	 * public
	 */
	this.clearChart = function() {
		//清除画布所有元素
		context.clearRect(0,0,canvas.width,canvas.height);
	};
	
	
	
	

	
	
	/**
	 * private
	 */
	function trainModel() {
		var self = this;
		
		self.trainName = ko.observable();
		self.startStn = ko.observable();
		self.endStn = ko.observable();
		
		self.update = function(train) {
			self.trainName(train.trainName);
			self.startStn(train.startStn);
			self.endStn(train.endStn);
		};
	};
	
	/**
	 * showRunTime
	 */
	function KYJHModel() {
		var _self = this;
		_self.runTimeList = ko.observableArray();//list
		_self.trainInfo = ko.observable(new trainModel());		//单个对象
		
		_self.loadData = function(trainObj) {
			_self.trainInfo().update(trainObj);
			_self.runTimeList.removeAll();
            for( var i = 0; i < trainObj.trainStns.length; i++) {
            	_self.runTimeList.push(trainObj.trainStns[i]);
            }

			$(".popover01").popover("toggle");
			$(".popover01").popover("hide");
		};
	};
	
	
	/**
	 * 渲染组号下拉框数据
	 * @param groupSerialNbrComboxData
	 */
	function initGroupSerialNbrCombox(groupSerialNbrComboxData) {
		_canvas_select_groupSerialNbr.empty();
		_canvas_select_groupSerialNbr.append("<option value='' selected='selected'>全部</option>");
		for(var j=0;j<groupSerialNbrComboxData.length;j++) {
			if (groupSerialNbrComboxData[j].value == _currentGroupSerialNbr) {
				_canvas_select_groupSerialNbr.append("<option selected='selected' value='"+groupSerialNbrComboxData[j].value+"'>"+groupSerialNbrComboxData[j].text+"</option>");
			} else {
				_canvas_select_groupSerialNbr.append("<option value='"+groupSerialNbrComboxData[j].value+"'>"+groupSerialNbrComboxData[j].text+"</option>");
			}
			
		}
		
	};
	
	
	/**
	 * 绘制交路线
	 */
	function drawJlLine() {
//		var booleanDrawJlStartAndEnd = true;	//是否绘制交路起止标记
		
		for (var i=0, _len=canvasData.jlData.length; i<_len; i++) {
			var _obj = canvasData.jlData[i];
			var _color = getRandomColor();
			var _lenJlTrain=_obj.trains.length;
			
			
			//2.1 绘制交路列车运行线
			for (var j=0; j<_lenJlTrain; j++) {
				var line = new myCanvasComponent.drawTrainRunLine(true, _color, _obj.trains[j]);
				line.drawLine(context);
				lineList.push(line);
			}
			
			//2.2 绘制交路接续关系
//			myCanvasComponent.drawJxgx(_color, _obj.jxgx, i);
			
			//2.3绘制交路起止标识
//			if (booleanDrawJlStartAndEnd){
//				myCanvasComponent.drawJlStartAndEnd(_color, _obj.trains, i);
//			}
			
			jlList.push({color:_color,data:_obj});
		};
		
		

		//3.渲染组号下拉框数据
//		initGroupSerialNbrCombox(_groupSerialNbrComboxData);
	};
	
	
	/**
	 * 鼠标左键选中后重新绘图
	 * 
	 * @param expandObj 可选参数 扩展对象
	 * 			{
	 * 				x : 247,						//可选参数 当前鼠标x坐标
	 * 				y : 458,					 	//可选参数 当前鼠标y坐标
	 * 				trainNbr : "K818",				//可选参数 车次号，外部传入条件，以便绘图时该车次高亮显示，类是鼠标选中效果
	 * 				booleanShowTrainDetail : true, 	//可选参数 是否显示该车次详细信息
	 * 			}
	 */
	function reDraw(expandObj) {
		//清除画布所有元素
		_self.clearChart();
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		//2.重新绘线
    	//2.1 绘制交路列车运行线
        for(var i = 0; i < lineList.length; i++) {
            var c = lineList[i];
            c.drawLine(context, expandObj);
        }
        
//        for (var i=0, _len=jlList.length; i<_len; i++) {
//			var _obj = jlList[i];
//			var _color = _obj.color;
//			
//			//2.2 绘制交路接续关系
//			myCanvasComponent.drawJxgx(_color, _obj.data.jxgx);
//			
//			//2.3绘制交路起止标识
//			myCanvasComponent.drawJlStartAndEnd(_color, _obj.data.trains);
//		};
    };
	
	
	this.drawChart = function(scale) {
		if(typeof canvasData != "object" || canvasData==null || canvasData.grid==null ) {
			showWarningDialog("当前不存在列车数据！");
			return;
		}
		
		lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
		jlList = [];	//用于保存交路数据元素，以便重新绘图
		
		this.clearChart();	//清除画布
		//context.beginPath();
		myCanvasComponent = new MyCanvasComponent(context, canvasData.grid.days, canvasData.grid.crossStns,getScale(scale));
		//绘制客运开行计划
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		//2.绘制交路线
		drawJlLine();
	};
	
	
	
	
	/**
	 * 根据车次号重新绘制图形
	 * @param trainNbr 必选参数 车次号
	 */
	this.reDrawByTrainNbr = function(trainNbr) {
		if (lineList.length == 0 || trainNbr==null ||trainNbr =="undefine") {
			showErrorDialog("无有效车次数据显示");
			return;
		}
		
		reDraw({trainNbr:trainNbr,booleanShowTrainDetail:false});
	};
	
};






