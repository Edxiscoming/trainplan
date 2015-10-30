var _MyCanvas = null;

var canvas = null;
var context = null;
var myCanvasComponent = null;//绘图组件对象
var lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
var jlList = [];	//用于保存交路数据元素，以便重新绘图
var _canvas_select_groupSerialNbr = null;
var _currentGroupSerialNbr = "";		//当前组号

var currentXScale = 10;	//x轴缩放比例
var currentXScaleCount = 1;//x轴放大总倍数
var currentYScale = 1;	//y轴缩放比例
var currentYScaleCount = 1;//y轴放大总倍数
var dataIsNull = false;
var pageModel = null;			//knockout绑定对象

var MyCanvas = function(){
	
	
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
				//查看简图 只包含始发、终到
				_stationTypeArray = ["0"];
				$("#parentParamIsShowJt", window.parent.document).val("1");//父窗口变量 为1时，图形中简图复选框则选中	用于第二次打开图形界面时初始值用
				//_stationTypeArray.push($(this).val());
			} else {
				//显示所有 包含始发、终到、分界口、停站、不停站
				_stationTypeArray = ["0","FJK","TZ"];
				$("#parentParamIsShowJt", window.parent.document).val("0");//父窗口变量 为1时，图形中简图复选框则选中	用于第二次打开图形界面时初始值用
//				removeArrayValue(_stationTypeArray, $(this).val());
			}
	    });
		//保留方便以后复选框扩展
//		if (_stationTypeArray.length >0) {
//			_stationTypeArray.push("0");	//增加显示始发及终到
//		}
		
		var _canvasIsDrawTrainTime = false;	//是否绘制列车经由站到达及出发时间
		if($("#canvas_checkbox_trainTime").is(":checked")){
			_canvasIsDrawTrainTime = true;
			$("#parentParamIsShowTrainTime", window.parent.document).val("1");//父窗口变量 为1时，图形中显示时刻复选框则选中	用于第二次打开图形界面时初始值用
		} else {
			_canvasIsDrawTrainTime = false;
			$("#parentParamIsShowTrainTime", window.parent.document).val("0");//父窗口变量 为1时，图形中显示时刻复选框则选中	用于第二次打开图形界面时初始值用
		}
		
		
		
		if (scale && scale!=null && scale!="undefine" && typeof scale == "object") {
			scale.xScale = currentXScale;				//x轴缩放比例
			scale.xScaleCount = currentXScaleCount;	//x轴当前放大倍数，用于控制时刻线条数
			scale.yScale = currentYScale;				//y轴缩放比例
			scale.stationTypeArray = _stationTypeArray;	//分界口复选框
			scale.isDrawTrainTime = _canvasIsDrawTrainTime;	//是否绘制列车经由站到达及出发时间
			scale.currentGroupSerialNbr = _canvas_select_groupSerialNbr.val();	//当前组号
			scale.isZgsUser = _isZgsUser;//当前用户是否为总公司用户
			return scale;
		} else {
			return {
				xScale : currentXScale,				//x轴缩放比例
				xScaleCount : currentXScaleCount,	//x轴当前放大倍数，用于控制时刻线条数
				yScale : currentYScale,				//y轴缩放比例
				stationTypeArray:_stationTypeArray,	//分界口复选框
				isDrawTrainTime:_canvasIsDrawTrainTime,	//是否绘制列车经由站到达及出发时间
				currentGroupSerialNbr : _canvas_select_groupSerialNbr.val(),	//当前组号
				isZgsUser : _isZgsUser//当前用户是否为总公司用户
			};
		}
		
		
		
		
	};
	
	
	/**
	 * public
	 */
	this.clearChart = function() {
		//清除数据
		lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
		jlList = [];	//用于保存交路数据元素，以便重新绘图
		
		//清除画布所有元素
		context.clearRect(0,0,canvas.width,canvas.height);
		
	};
	
	
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
	
	
	this.drawGraph = function (scale) {
		this.clearChart();
		
		var _groupSerialNbrComboxData = [];
		var booleanDrawJlStartAndEnd = true;	//是否绘制交路起止标记
		scale = getScale(scale);				//获取过滤条件
		myCanvasComponent = new MyCanvasComponent(context, canvasData.grid.days, canvasData.grid.crossStns, scale);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		//2.绘制交路线
		for (var i=0, _len=canvasData.jlData.length; i<_len; i++) {
			var _obj = canvasData.jlData[i];
			var _color = getRandomColor();
			var _lenJlTrain=_obj.trains.length;
			
			//临时保存车组号
			if (_obj.groupSerialNbr!=null && _obj.groupSerialNbr!="undefine") {
				_groupSerialNbrComboxData.push({
					value:_obj.groupSerialNbr,
					text:myCanvasComponent.convertGroupSerialNbr(_obj.groupSerialNbr)+"组"
				});
			}
			
			if (typeof scale=="object" && scale.currentGroupSerialNbr!=null && typeof scale.currentGroupSerialNbr!="undefine"
				&&scale.currentGroupSerialNbr!="" && scale.currentGroupSerialNbr!=_obj.groupSerialNbr) {
				continue;	//当前组号下拉框条件存在，且值不等于交路数据中组 则跳过不绘制该交路图形		""表示查看所有
			}
			
			
			//2.1 绘制交路列车运行线
			for (var j=0; j<_lenJlTrain; j++) {
				var line = new myCanvasComponent.drawTrainRunLine(false, _color, _obj.trains[j]);
				line.drawLine(context);
				//保存列车信息，以便重新绘图
				lineList.push(line);
			}
			
			//2.2 绘制交路接续关系
			myCanvasComponent.drawJxgx(_color, _obj.jxgx, i);
			
			
			//2.3绘制交路起止标识
			if (booleanDrawJlStartAndEnd){
				var diagram = false;
				$("[name='canvas_checkbox_stationType']").each(function(){
					if($(this).is(":checked")) {
						//查看简图 只包含始发、终到,则影响画图时，起止标识的画图算法
						diagram = true;
					} 
			    });
				myCanvasComponent.drawJlStartAndEnd(_color, _obj.trains, i, diagram);
			}
			
			//保存交路对象，以便重新绘图
			jlList.push({color:_color,data:_obj});
		}
		
		
		//3.渲染组号下拉框数据
		initGroupSerialNbrCombox(_groupSerialNbrComboxData);
		
	};
	
	
	
};




$(function(){

	
	
	/*if ("1" == $("#parentParamIsShowJt", window.parent.document).val()) {
		$("#canvas_checkbox_stationType_jt").attr("checked",true);//为1时，图形中简图复选框则选中	用于第二次打开图形界面时初始值用
	} else {
		$("#canvas_checkbox_stationType_jt").attr("checked",false);
	}
	if ("1" == $("#parentParamIsShowTrainTime", window.parent.document).val()) {
		$("#canvas_checkbox_trainTime").attr("checked",true);// 为1时，图形中显示时刻复选框则选中	用于第二次打开图形界面时初始值用
	} else {
		$("#canvas_checkbox_trainTime").attr("checked",false);
	}*/
	
	if(canvasData==null || canvasData.grid==null) {
		showErrorDialog("当前不存在列车数据！");
		dataIsNull = true;
		return;
	}
	_canvas_select_groupSerialNbr = $("#canvas_select_groupSerialNbr");
	canvas = document.getElementById("unit_cross_canvas");
	context = canvas.getContext('2d');
	
	//初始化事件类
//	new CanvasEventComponent("unit_cross_canvas");
	

	_MyCanvas = new MyCanvas();
	_MyCanvas.drawGraph();
	

	//刷新按钮点击事件
	$("#canvas_event_btn_refresh").click(function(){
		_MyCanvas.drawGraph();
	});
	
	
	//车底下拉框事件
	$("#canvas_select_groupSerialNbr").change(function(){
		_currentGroupSerialNbr = _canvas_select_groupSerialNbr.val();
		
		_MyCanvas.drawGraph();
	});
	
	//显示停站时刻 复选框事件
	$("#canvas_checkbox_trainTime").click(function(){
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}

		_MyCanvas.drawGraph();
		
	});
	

	//分界口 复选框事件
	$("#canvas_checkbox_stationType_jt").click(function(){
		_MyCanvas.drawGraph();
	});
	
	
	//x放大2倍
	$("#canvas_event_btn_x_magnification").click(function(){
		if (currentXScaleCount == 32) {
			showErrorDialog("当前已经不支持继续放大啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		
		//计算画布比例及倍数
		currentXScale = currentXScale/2;
		currentXScaleCount = currentXScaleCount*2;
		

		$("#canvas_event_label_xscale").text(currentXScaleCount);
		_MyCanvas.drawGraph();
		
	});
	
	//x缩小2倍
	$("#canvas_event_btn_x_shrink").click(function(){
		if (currentXScaleCount == 0.25) {
			showErrorDialog("当前已经不支持继续缩小啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		

		//计算画布比例及倍数
		currentXScale = currentXScale*2;
		currentXScaleCount = currentXScaleCount/2;

		$("#canvas_event_label_xscale").text(currentXScaleCount);
		_MyCanvas.drawGraph();
	});
	
	//y放大2倍
	$("#canvas_event_btn_y_magnification").click(function(){
		if (currentYScaleCount == 8) {
			showErrorDialog("当前已经不支持继续放大啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		
		
		//计算画布比例及倍数
		currentYScale = currentYScale/2;
		currentYScaleCount = currentYScaleCount*2;

		$("#canvas_event_label_yscale").text(currentYScaleCount);
		_MyCanvas.drawGraph();
		
	});
	
	//y缩小2倍
	$("#canvas_event_btn_y_shrink").click(function(){
		if (currentYScaleCount == 0.25) {
			showErrorDialog("当前已经不支持继续缩小啦！");
			return;
		}
		if (dataIsNull) {
			showErrorDialog("当前不存在列车数据！");
			return;
		}
		

		//计算画布比例及倍数
		currentYScale = currentYScale*2;
		currentYScaleCount = currentYScaleCount/2;

		$("#canvas_event_label_yscale").text(currentYScaleCount);
		_MyCanvas.drawGraph();
	});
	
	
	
	//跳出修改框,父页面cross_manage.jsp
	
	$("#editStationSortBut").click(function(){
		
		window.parent.openEditStationSortD(canvasData.editStnSort.drawGraphIdAllIn);
		/*$("#editStationSort",window.parent.document).find("iframe").attr("src", "editStationSort?drawGraphId="+canvasData.editStnSort.drawGraphIdAllIn);
		$('#editStationSort',window.parent.document).dialog({ title: "修改站序", autoOpen: true, modal: false, draggable: true, resizable:true,
			onResize:function() {
				var iframeBox = $("#editStationSort",window.parent.document).find("iframe");
				var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
				var WH = $('#editStationSort',window.parent.document).height();
				var WW = $('#editStationSort',window.parent.document).width();
				if (isChrome) {
					iframeBox.css({ "height": (WH) + "px"});
					iframeBox.css({ "min-height": (WH) + "px"});
					iframeBox.attr("width", (WW));
					
				}else{
					iframeBox.css({ "height": (WH)  + "px"});
					iframeBox.css({ "min-height": (WH) + "px"});
					iframeBox.attr("width", (WW));
				}
			}});*/
	});
	$("#deleteStationSortBut").click(function(){
		showConfirmDiv("提示", "你确定删除已生成的站序吗?", function (r) {  
			if(r){
				$.ajax({
					url : basePath + "/cross/deleteStationSort?drawGraphId="+canvasData.editStnSort.drawGraphIdAllIn,
					cache : false,
					type : "GET",
					dataType : "json",
					contentType : "application/json", 
					success : function(result) {    
						showSuccessDialog("格式化站序成功，请重新生成");
					},
					error : function() {
						showErrorDialog("格式化站序失败");
					},
					complete : function(){
						commonJsScreenUnLock();
					}
			    });
			}
		});
	});
});



