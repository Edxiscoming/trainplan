var canvas = null;//画布
var context = null;//上下文
var canvasData = {};
var currentXScale = 10;	//x轴缩放比例
var currentXScaleCount = 1;//x轴放大总倍数
var currentYScale = 1;	//y轴缩放比例
var currentYScaleCount = 1;//y轴放大总倍数


var TrainRunLineCanvaPage = function() {
	var _self = this;
	
	/**
	 * public
	 */
	_self.initPage = function() {
		canvasData = {};
		canvas = document.getElementById("div_canvas");
		context = canvas.getContext('2d');
		

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
		
		

		//显示停站时刻 复选框事件
		$("#canvas_checkbox_trainTime").click(function(){
			_self.drawChart();
			
		});
		

		//简图 复选框事件
		$("#canvas_checkbox_stationType_jt").click(function(){
			_self.drawChart();
			
		});
		
		
		
		/*************************************************/
		//初始化获取绘图数据
		_self.initCanvasData();
		
	};
	
	

	/**
	 * public
	 */
	this.clearChart = function() {
		//清除画布所有元素
		context.clearRect(0,0,canvas.width,canvas.height);
	};
	
	
	
	_self.initCanvasData = function () {
		commonJsScreenLock();
		
	
		$.ajax({
			url : basePath+"/jbtcx/getTrainTimeInfoByPlanTrainIdjy",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				planTrainId : $("#input_hidden_planTrainId").val(),
				trainNbr : $("#input_hidden_trainNbr").val()
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					canvasData.grid = result.data.gridData;
					canvasData.jlData = result.data.myJlData;
					_self.drawChart();
				} else {
					showErrorDialog("获取绘图数据失败");
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
	
	
	this.drawChart = function(scale) {
		if(typeof canvasData != "object" || canvasData==null || canvasData.grid==null ) {
			showWarningDialog("当前不存在列车数据！");
			return;
		}
		
		this.clearChart();	//清除画布
		
		
		var myCanvasComponent = new MyCanvasComponent(context, canvasData.grid.days, canvasData.grid.crossStns,getScale(scale));
		//绘制客运开行计划
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		//2.绘制运行线
		new myCanvasComponent.drawTrainRunLine(true, "#8236ac", canvasData.jlData[0].trains[0]).drawLine(context);
	};
	
	
	
	/**
	 * 获取绘图扩展条件对象
	 * @param scale
	 * @returns
	 */
	function getScale(scale) {
		//生成分界口、停站标记
		var _stationTypeArray = [];
		$("[name='canvas_checkbox_stationType']").each(function(){
			if($(this).is(":checked")) {
				//查看简图 只包含始发、终到
			/*	_stationTypeArray = ["0","FJK","TZ"];
				//_stationTypeArray.push($(this).val());
			} else {
				//显示所有 包含始发、终到、分界口、停站、不停站
				_stationTypeArray = ["0","FJK","TZ","BT"];//"0","FJK","TZ","BT"
//				removeArrayValue(_stationTypeArray, $(this).val());
*/			
				_stationTypeArray = ["SFZ","FJK","TZ","ZDZ"];
				//_stationTypeArray.push($(this).val());
			} else {
				//显示所有 包含始发、终到、分界口、停站、不停站
				_stationTypeArray = ["SFZ","FJK","BTZ","TZ","ZDZ"];//"0","FJK","TZ","BT"
			
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
			return scale;
		} else {
			return {
				xScale : currentXScale,				//x轴缩放比例
				xScaleCount : currentXScaleCount,	//x轴当前放大倍数，用于控制时刻线条数
				yScale : currentYScale,				//y轴缩放比例
				stationTypeArray:_stationTypeArray,	//分界口复选框
				isDrawTrainTime:_canvasIsDrawTrainTime,	//是否绘制列车经由站到达及出发时间
			};
		}
	};
	
	
	
	
};


$(function(){
	
	new TrainRunLineCanvaPage().initPage();
	
	
	
	
	
//	var myCanvasComponent = new MyCanvasComponent(context, gridData.data.days, gridData.data.crossStns);
//	
//	
//	//绘制客运开行计划
//	//1.绘制网格
//	myCanvasComponent.drawGrid("green");
//	
//	//2.绘制运行线
//	new myCanvasComponent.drawTrainRunLine(true, "#8236ac", trainRunLineCanvasData.data).drawLine(context);
//
//	//3.绘制运行线
////	new myCanvasComponent.drawTrainRunLine(true, "#72b5d2", jl_error_trainRunLineCanvasData.data).drawLine(context);
	
	
	
	
});




