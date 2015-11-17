var currentXScale = 10;	//x轴缩放比例
var currentXScaleCount = 1;//x轴放大总倍数
var currentYScale = 1;	//y轴缩放比例
var currentYScaleCount = 1;//y轴放大总倍数
var _currentGroupSerialNbr = "";
var canvasData_pre = {}; //旧图数据
var canvasData = {}; //新图数据
var canvas = document.getElementById("oldGraph");
var context = canvas.getContext('2d');
var _MyCanvas = null;
var canvasNew = document.getElementById("newGraph");
var contextNew = canvasNew.getContext('2d');
var _MyCanvasNew = null;
var stepLeft = 100;
var stepRight = 100;

var lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
var jlList = [];	//用于保存交路数据元素，以便重新绘图
/**
 * 画交路图，带交替信息
 * @param planCrossId
 * @param startTime
 * @param endTime
 */
function getGraphData(planCrossId,startTime,endTime){
	commonJsScreenLock();
	$.ajax({
		url : basePath + "/cross/createCrossAlternateMap",
		cache : false,
		type : "POST",
		dataType : "json",
		contentType : "application/json",
		data : JSON.stringify({
			planCrossId : planCrossId,
			startTime : startTime,
			endTime : endTime
		}),
		success : function(result) {
			if (result != null && result != "undefind"
					&& result.code == "0") {
				if (result.data != null) {
					canvasData_pre = {
							grid : null,
							jlData : null
					};
					if(result.data.gridData_pre){
						canvasData_pre = {
								grid : $.parseJSON(result.data.gridData_pre),
								jlData : $.parseJSON(result.data.myJlData_pre)
						};
					}
					if(result.data.gridData){
						canvasData = {
								grid : $.parseJSON(result.data.gridData),
								jlData : $.parseJSON(result.data.myJlData)
						};
					}
				}else{
					return;
				}
				//canvasData_pre.grid = null;
				//计算步长
				if(canvasData_pre.grid !==null){
					var stepNo_left  = canvasData_pre.grid.crossStns.length;
					var stepNo_right  = canvasData.grid.crossStns.length;
					if(stepNo_left === stepNo_right){
						stepLeft = null;
						stepRight = null;
					}else if(stepNo_left > stepNo_right){
						if(stepNo_left > 3){
							stepLeft = 40;
							stepRight = 40 *(stepNo_left/stepNo_right);
						}else{
							stepLeft = 100;
							stepRight = 100 *(stepNo_left/stepNo_right);
						}
					}else{
						if(stepNo_right > 3){
							stepRight = 40;
							stepLeft = 40 *(stepNo_right/stepNo_left);
						}else{
							stepRight = 100;
							stepLeft = 100 *(stepNo_right/stepNo_left);
						}
					}
			        _MyCanvas = new CrossGraph(context,canvas);
			        _MyCanvas.drawGraph({stepY:stepLeft},canvasData_pre);
			        _MyCanvasNew = new CrossGraph(contextNew,canvasNew);
			        _MyCanvasNew.drawGraph({startX:1,stepY:stepRight},canvasData,"left");
				}else if(canvasData.grid != null){
					 _MyCanvas = new CrossGraph(context,canvas);
				     _MyCanvas.drawGraph(null,canvasData);
				}
		        drawMainGraph();

			} else {
				showErrorDialog("获取车底交路绘图数据失败");
			}
		},
		error : function() {
			showErrorDialog("获取车底交路绘图数据失败");
		},
		complete : function() {
			commonJsScreenUnLock();
		}
	});
}

function drawMainGraph(){
    $mainCanvas = $("#graphPanel");
    //有滚动条，固定左侧坐标
    if(canvas.width>$mainCanvas.width()){
    	var canvasLeft = document.getElementById("canvas_leftX");
        canvasLeft.height=canvas.height;
        var contextLeft = canvasLeft.getContext('2d');
        contextLeft.drawImage(canvas,0,0,150,canvas.height,0,0,150,canvas.height);
        canvasLeft.style.top=-$mainCanvas.scrollTop()+"px";
    }
	if(canvasData_pre.grid !==null){
		var heightAll = Math.max(canvasNew.height,canvas.height);
	    var widthAll = canvas.width +canvasNew.width-150;
	    var canvasAll= document.getElementById("mainGraph");
	    canvasAll.height = heightAll;
	    canvasAll.width = widthAll;
	    var contextAll = canvasAll.getContext('2d');
	    contextAll.clearRect(0,0,canvasAll.width,canvasAll.height); //清空画布
	    contextAll.drawImage(canvas,0,0);  //复制旧图
	    contextAll.drawImage(canvasNew,canvas.width-150,0); //复制新图
	  //存在滚动条，右侧坐标fixed。
	    if(widthAll>=$mainCanvas.width()){
	    	var canvasRight = document.getElementById("canvas_rightX");
	    	canvasRight.height=canvasNew.height;
            var contextRight = canvasRight.getContext('2d');
            contextRight.drawImage(canvasNew,canvasNew.width-145,0,150,canvasNew.height,0,0,150,canvasNew.height);
            canvasRight.style.top=-$mainCanvas.scrollTop()+"px";
            $("#rightX:hidden").show();
	    }else{
	    	$("#rightX:visible").hide();
	    }
	    
	}else{
		$("#mainGraph").hide();
		$("#oldGraph").show();
	}
	}
var CrossGraph = function(context,canvas){

    function getScale(scale) {
        // 生成分界口、停站标记
        var _stationTypeArray = [ "0" ];
        $("[name='canvas_checkbox_stationType']").each(function() {
            if ($(this).is(":checked")) {
                // 查看简图 只包含始发、终到
                _stationTypeArray = [ "0" ];
            } else {
                // 显示所有 包含始发、终到、分界口、停站、不停站
                _stationTypeArray = [ "0", "FJK", "TZ" ];// "0","FJK","TZ","BT"
            }
        });
        var _canvasIsDrawTrainTime = false; // 是否绘制列车经由站到达及出发时间
        if ($("#canvas_checkbox_trainTime").is(":checked")) {
            _canvasIsDrawTrainTime = true;
        } else {
            _canvasIsDrawTrainTime = false;
        }

        if (scale && scale != null && scale != "undefine"
            && typeof scale == "object") {
            scale.xScale = currentXScale; // x轴缩放比例
            scale.xScaleCount = currentXScaleCount; // x轴当前放大倍数，用于控制时刻线条数
            scale.yScale = currentYScale; // y轴缩放比例
            scale.stationTypeArray = _stationTypeArray; // 分界口复选框
            scale.isDrawTrainTime = _canvasIsDrawTrainTime; // 是否绘制列车经由站到达及出发时间
            scale.currentGroupSerialNbr = _currentGroupSerialNbr; // 当前组号
            scale.isZgsUser = _isZgsUser;// 当前用户是否为总公司用户
            return scale;
        } else {
            return {
                xScale : currentXScale, // x轴缩放比例
                xScaleCount : currentXScaleCount, // x轴当前放大倍数，用于控制时刻线条数
                yScale : currentYScale, // y轴缩放比例
                stationTypeArray : _stationTypeArray, // 分界口复选框
                isDrawTrainTime : _canvasIsDrawTrainTime, // 是否绘制列车经由站到达及出发时间
                currentGroupSerialNbr : _currentGroupSerialNbr, // 当前组号
                isZgsUser : _isZgsUser
                // 当前用户是否为总公司用户
            };
        }
    };

    function clearChart() {
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
    this.drawGraph=function(scale,canvasData,align){
        var _groupSerialNbrComboxData = [];
        var booleanDrawJlStartAndEnd = true;	//是否绘制交路起止标记
        clearChart();
        scale = getScale(scale);
        var myCanvasComponent = new MyCanvasComponent(context, canvasData.grid.days, canvasData.grid.crossStns, scale);
        //1.绘制网格
        myCanvasComponent.drawGrid("green",align);

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


    }
};

/*
 *重画
 */
function reDraw(){
	commonJsScreenLock();
	if(canvasData_pre.grid !==null){
		_MyCanvas.drawGraph({stepY:stepLeft},canvasData_pre);
	    _MyCanvasNew.drawGraph({startX:1,stepY:stepRight},canvasData,"left");
	}else{
		_MyCanvas.drawGraph(null,canvasData);
	}
	
	drawMainGraph();
	commonJsScreenUnLock();
}
/**
 * 绑定交路图交互事件
 */
$(function(){
	//刷新按钮点击事件
    $("#canvas_event_btn_refresh").click(function(){
    	reDraw();
    });
	//车底下拉框事件
    _canvas_select_groupSerialNbr = $("#canvas_select_groupSerialNbr");
    _canvas_select_groupSerialNbr.change(function(){
        _currentGroupSerialNbr = _canvas_select_groupSerialNbr.val();
        reDraw();
    });

    //显示停站时刻 复选框事件
    $("#canvas_checkbox_trainTime").click(function(){
        reDraw();
    });


    //分界口 复选框事件
    $("#canvas_checkbox_stationType_jt").click(function(){
    	reDraw();
    });


    //x放大2倍
    $("#canvas_event_btn_x_magnification").click(function(){
        if (currentXScaleCount == 4) {
            alert("当前已经不支持继续放大啦！");
            return;
        }

        //计算画布比例及倍数
        currentXScale = currentXScale/2;
        currentXScaleCount = currentXScaleCount*2;


        $("#canvas_event_label_xscale").text(currentXScaleCount);
        reDraw();

    });

    //x缩小2倍
    $("#canvas_event_btn_x_shrink").click(function(){
        if (currentXScaleCount == 0.25) {
            alert("当前已经不支持继续缩小啦！");
            return;
        }
        //计算画布比例及倍数
        currentXScale = currentXScale*2;
        currentXScaleCount = currentXScaleCount/2;

        $("#canvas_event_label_xscale").text(currentXScaleCount);
        reDraw();
    });
    //y放大2倍
    $("#canvas_event_btn_y_magnification").click(function(){
        if (currentYScaleCount == 4) {
            alert("当前已经不支持继续放大啦！");
            return;
        }

        //计算画布比例及倍数
        currentYScale = currentYScale/2;
        currentYScaleCount = currentYScaleCount*2;

        $("#canvas_event_label_yscale").text(currentYScaleCount);
        reDraw();

    });

    //y缩小2倍
    $("#canvas_event_btn_y_shrink").click(function(){
        if (currentYScaleCount == 0.25) {
            alert("当前已经不支持继续缩小啦！");
            return;
        }
        //计算画布比例及倍数
        currentYScale = currentYScale*2;
        currentYScaleCount = currentYScaleCount/2;

        $("#canvas_event_label_yscale").text(currentYScaleCount);
        reDraw();
    });
    // 纵坐标固定
    var $mainCanvas= $("#graphPanel");
    $mainCanvas.scroll(function(){
        var $mainCanvasScrollLeft = $mainCanvas.scrollLeft();
        var $mainCavasScrollTop = $mainCanvas.scrollTop();
        document.getElementById("canvas_leftX").style.top=-$mainCanvas.scrollTop()+"px";
        document.getElementById("canvas_rightX").style.top=-$mainCanvas.scrollTop()+"px";
        if($mainCanvasScrollLeft>0){
            $("#leftX:hidden").show();
            if($("#mainGraph").is(":visible")){
            	if($mainCanvasScrollLeft>=$("#mainGraph").width()-$mainCanvas.width()){
            		$("#rightX:visible").hide();
                }else{
                	$("#rightX:hidden").show();
                }
            }
            	
        }else{
            $("#leftX:visible").hide();
            if($("#mainGraph").is(":visible")){
            	$("#rightX:hidden").show();
            }
            
        }
    });
});
