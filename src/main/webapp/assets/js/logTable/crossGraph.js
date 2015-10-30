/**
 * Created by Administrator on 2015/9/18.
 */
var _MyCanvas =null;

var myCanvasComponent = null;//绘图组件对象
var lineList = [];	//列车线对象封装类  用于保存列车线元素，以便重新绘图
var jlList = [];	//用于保存交路数据元素，以便重新绘图
var canvas = document.getElementById("canvas_event_getvalue");
var context = canvas.getContext('2d');
var _canvas_select_groupSerialNbr = $("#canvas_select_groupSerialNbr");
var _currentGroupSerialNbr = "";		//当前组号
var currentXScale = 10;	//x轴缩放比例
var currentXScaleCount = 1;//x轴放大总倍数
var currentYScale = 1;	//y轴缩放比例
var currentYScaleCount = 1;//y轴放大总倍数
var dataIsNull = false;
var pageModel = null;			//knockout绑定对象
var CrossGraph = function(){

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
            scale.currentGroupSerialNbr = _canvas_select_groupSerialNbr.val(); // 当前组号
            scale.isZgsUser = _isZgsUser;// 当前用户是否为总公司用户
            return scale;
        } else {
            return {
                xScale : currentXScale, // x轴缩放比例
                xScaleCount : currentXScaleCount, // x轴当前放大倍数，用于控制时刻线条数
                yScale : currentYScale, // y轴缩放比例
                stationTypeArray : _stationTypeArray, // 分界口复选框
                isDrawTrainTime : _canvasIsDrawTrainTime, // 是否绘制列车经由站到达及出发时间
                currentGroupSerialNbr : _canvas_select_groupSerialNbr.val(), // 当前组号
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
    this.drawGraph=function(scale){
        var _groupSerialNbrComboxData = [];
        var booleanDrawJlStartAndEnd = true;	//是否绘制交路起止标记
        clearChart();
        scale = getScale(scale);
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


    }
};
$(function () {
//    canvasData.grid= grid;
//    canvasData.jlData = jlData;
    _MyCanvas = new CrossGraph();
    _MyCanvas.drawGraph();
//    var $mainCanvas= $("#mainCanvas");
//    $mainCanvas.scroll(function(){
//        var $mainCanvasScrollLeft = $mainCanvas.scrollLeft();
//        var $mainCavasScrollTop = $mainCanvas.scrollTop();
//        if($mainCanvasScrollLeft>0){
//            var canvasLeft = document.getElementById("canvas_event_leftX");
//            canvasLeft.height=canvas.height;
//            var contextLeft = canvasLeft.getContext('2d');
//            var imgData=context.getImageData(0,50,152,canvas.height-150);
//            contextLeft.putImageData(imgData,0,0);
//            canvasLeft.style.top=-$mainCanvas.scrollTop()+"px";
//           // $("#leftX").css({top:$mainCanvas.offset().top-$mainCanvas.scrollTop()+50});
//            $("#leftX:hidden").show();
//            $("#coner:hidden").show();
//        }else{
//            $("#leftX:visible").hide();
//            $("#coner:visible").hide();
//        }
//        if($mainCavasScrollTop>0){
//            var canvasHeader = document.getElementById("canvas_event_header");
//            canvasHeader.width=canvas.width;
//            var contextHeader = canvasHeader.getContext('2d');
//            var imgData=context.getImageData(0,0,canvas.width,55);
//            contextHeader.putImageData(imgData,0,0);
//            canvasHeader.style.left=-$mainCanvas.scrollLeft()+"px";
//            $("#header:hidden").show();
//            $("#coner:hidden").show();
//        }else{
//            $("#header:visible").hide();
//            $("#coner:visible").hide();
//        }
//    });

    // 初始化事件类
    //new CanvasEventComponent("canvas_event_getvalue");
    //刷新按钮点击事件
    $("#canvas_event_btn_refresh").click(function(){
        _MyCanvas.drawGraph();
    });


    //车底下拉框事件
    //_canvas_select_groupSerialNbr = $("#canvas_select_groupSerialNbr");
    _canvas_select_groupSerialNbr.change(function(){
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
            alert("当前已经不支持继续放大啦！");
            return;
        }
        if (dataIsNull) {
            alert("当前不存在列车数据！");
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
            alert("当前已经不支持继续缩小啦！");
            return;
        }
        if (dataIsNull) {
            alert("当前不存在列车数据！");
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
            alert("当前已经不支持继续放大啦！");
            return;
        }
        if (dataIsNull) {
            alert("当前不存在列车数据！");
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
            alert("当前已经不支持继续缩小啦！");
            return;
        }
        if (dataIsNull) {
            alert("当前不存在列车数据！");
            return;
        }


        //计算画布比例及倍数
        currentYScale = currentYScale*2;
        currentYScaleCount = currentYScaleCount/2;

        $("#canvas_event_label_yscale").text(currentYScaleCount);
        _MyCanvas.drawGraph();
    });
});



