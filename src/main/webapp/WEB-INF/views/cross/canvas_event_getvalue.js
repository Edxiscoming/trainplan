
var runTimeModel;
var myCanvasComponent = null;
var lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
var jlList = [];	//用于保存交路数据元素，以便重新绘图

var canvas = document.getElementById("canvas_event_getvalue");
var context = canvas.getContext('2d');
    
var RunPlanCanvasPage = function() {
	var _self = this;
	
	/**
	 * public
	 */
	this.initPage = function() {
		$("#canvas_runplan_input_startDate").datepicker();
		$("#canvas_runplan_input_endDate").datepicker();
		

	    //1.绑定车次详情列表事件
		runTimeModel = new KYJHModel();
		ko.applyBindings(runTimeModel);
		
		//2.画图
		_self.drawChart();
		
		//3.增加canvas监听事件
		canvas.onmousedown = function(e) {
	        var loc = windowToCanvas(canvas, e.clientX,e.clientY);
	        var x = loc.x;
	        var y = loc.y;
	        
	        for(var i = 0; i < lineList.length; i++) {
	            var c = lineList[i];
	            if(c.isPointInStroke(context, x, y) || c.isCurrent) {
	            	reDraw({x:x, y:y,booleanShowTrainDetail:false});

	            	lineList[i] = c;//修改鼠标选中对象线状态
	            	break;
	            }
	        }
	    };
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
		}
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
		}
	};
	
	
	
	/**
	 * 绘制交路线
	 */
	function drawJlLine() {
		var booleanDrawJlStartAndEnd = true;	//是否绘制交路起止标记
		for (var i=0, _len=canvasData.jlData.length; i<_len; i++) {
			var _obj = canvasData.jlData[i];
			var _color = getRandomColor();
			var _lenJlTrain=_obj.trains.length;
			
			//2.1 绘制交路列车运行线
			for (var j=0; j<_lenJlTrain; j++) {
				var line = new myCanvasComponent.drawTrainRunLine(false, _color, _obj.trains[j]);
				line.drawLine(context);
				lineList.push(line);
			}
			
			//2.2 绘制交路接续关系
			myCanvasComponent.drawJxgx(_color, _obj.jxgx);
			
			//2.3绘制交路起止标识
			if (booleanDrawJlStartAndEnd){
				myCanvasComponent.drawJlStartAndEnd(_color, _obj.trains);
			}
			
			jlList.push({color:_color,data:_obj});
		};
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
        
        for (var i=0, _len=jlList.length; i<_len; i++) {
			var _obj = jlList[i];
			var _color = _obj.color;
			
			//2.2 绘制交路接续关系
			myCanvasComponent.drawJxgx(_color, _obj.data.jxgx);
			
			//2.3绘制交路起止标识
			myCanvasComponent.drawJlStartAndEnd(_color, _obj.data.trains);
		};
    };
	
	
	this.drawChart = function(scale) {
		myCanvasComponent = new MyCanvasComponent(context, canvasData.grid.days, canvasData.grid.crossStns,scale);
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

var runPlanCanvasPage = null;
var currentXScale = 10;	//x轴缩放比例
var currentXScaleCount = 1;//x轴放大总倍数
var currentYScale = 1;	//y轴缩放比例
var currentYScaleCount = 1;//y轴放大总倍数

$(function(){
	//x放大2倍
	$("#canvas_event_btn_x_magnification").click(function(){
		if (currentXScaleCount == 32) {
			showErrorDialog("当前已经不支持继续放大啦！");
			return;
		}
		
		//必须清除
		lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
		jlList = [];	//用于保存交路数据元素，以便重新绘图
		
		//计算画布比例及倍数
		currentXScale = currentXScale/2;
		currentXScaleCount = currentXScaleCount*2;

		$("#canvas_event_label_xscale").text(currentXScaleCount);
		runPlanCanvasPage.clearChart();	//清除画布
		runPlanCanvasPage.drawChart({
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale			//y轴缩放比例
			 });
		
	});
	
	//x缩小2倍
	$("#canvas_event_btn_x_shrink").click(function(){
		if (currentXScaleCount == 0.25) {
			showErrorDialog("当前已经不支持继续缩小啦！");
			return;
		}
		
		//必须清除
		lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
		jlList = [];	//用于保存交路数据元素，以便重新绘图

		//计算画布比例及倍数
		currentXScale = currentXScale*2;
		currentXScaleCount = currentXScaleCount/2;

		$("#canvas_event_label_xscale").text(currentXScaleCount);
		runPlanCanvasPage.clearChart();	//清除画布
		runPlanCanvasPage.drawChart({
			 xScale : currentXScale,			//x轴缩放比例
			 xScaleCount : currentXScaleCount,	//x轴放大总倍数
			 yScale : currentYScale			//y轴缩放比例
		 });
	});
	//y放大2倍
	$("#canvas_event_btn_y_magnification").click(function(){
		if (currentYScaleCount == 8) {
			showErrorDialog("当前已经不支持继续放大啦！");
			return;
		}
		
		//必须清除
		lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
		jlList = [];	//用于保存交路数据元素，以便重新绘图
		
		//计算画布比例及倍数
		currentYScale = currentYScale/2;
		currentYScaleCount = currentYScaleCount*2;

		$("#canvas_event_label_yscale").text(currentYScaleCount);
		runPlanCanvasPage.clearChart();	//清除画布
		runPlanCanvasPage.drawChart({
				 xScale : currentXScale,			//x轴缩放比例
				 xScaleCount : currentXScaleCount,	//x轴放大总倍数
				 yScale : currentYScale			//y轴缩放比例
			 });
		
	});
	
	//y缩小2倍
	$("#canvas_event_btn_y_shrink").click(function(){
		if (currentYScaleCount == 0.25) {
			showErrorDialog("当前已经不支持继续缩小啦！");
			return;
		}
		
		//必须清除
		lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
		jlList = [];	//用于保存交路数据元素，以便重新绘图

		//计算画布比例及倍数
		currentYScale = currentYScale*2;
		currentYScaleCount = currentYScaleCount/2;

		$("#canvas_event_label_yscale").text(currentYScaleCount);
		runPlanCanvasPage.clearChart();	//清除画布
		runPlanCanvasPage.drawChart({
			 xScale : currentXScale,			//x轴缩放比例
			 xScaleCount : currentXScaleCount,	//x轴放大总倍数
			 yScale : currentYScale			//y轴缩放比例
		 });
	});
	
	
	//根据车次号重新绘制图形
	$("#canvas_trainNbr_test").click(function(){
		runPlanCanvasPage.reDrawByTrainNbr("K817");
	});
	

	
	
	runPlanCanvasPage = new RunPlanCanvasPage();
	runPlanCanvasPage.initPage();
	
});








