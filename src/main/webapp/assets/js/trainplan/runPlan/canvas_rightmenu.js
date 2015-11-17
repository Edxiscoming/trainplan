var _canvasEventComponent = null;
 
var CanvasEventComponent = function(canvasDivId) {
	var _self = this;
	_canvasEventComponent = this;
	var _canvas = document.getElementById(canvasDivId);
	var _context = canvas.getContext('2d');
//	var _mouseCanvsPoint = {};	//鼠标在canvas图形中的位置

	var _currentTrainObj = null;	//保存右键菜单中选中对象
	

	init();
	
	
	function init() {
		initMenu();

	    //2.鼠标右键事件--点击其他地方消失
		$('.rightMenu').hover(function(e) {
			$(".rightMenu").unbind("mousedown");
		},function(){
			$('html').click(function(e) {
				$(".rightMenu").hide();
			});
		});
		
		//3.增加canvas监听事件
		canvas.onmousedown = function(e) {
			_currentTrainObj = null;//置空已选择对象
			$("#"+canvasDivId).bind("contextmenu", function(e){ return false; });
		  	$(".rightMenu").bind("contextmenu", function(e){ return false; });
			//alert(e.which) // 1 = 鼠标左键 left; 2 = 鼠标中键; 3 = 鼠标右键
		  	

	        var loc = windowToCanvas(canvas, e.clientX,e.clientY);
	        var x = loc.x;
	        var y = loc.y;
//	        _mouseCanvsPoint = {x:loc.x, y:loc.y};
	        
			if(3 == e.which){
		        for(var i = 0; i < lineList.length; i++) {
		            var c = lineList[i];
		            if(c.isPointInStroke(context, x, y)) {
		            	_self.reDraw({x:x, y:y,booleanShowTrainDetail:false});//右键点击显示选择颜色
		            	c.isCurrent = true;
		            	_currentTrainObj = c;
		            	
		            	//显示右键菜单
		    		    $(".rightMenu").show();
		    			$(".rightMenu").css("top",e.pageY);
		    			$(".rightMenu").css("left",e.pageX);
		    			e.stopPropagation();
		            	
//		            	break;
		            } else {
		            	c.isCurrent = false;
		            }
	            	lineList[i] = c;//修改鼠标选中对象线状态
		        }
				
				
			}else if(1 == e.which){
				for(var i = 0; i < lineList.length; i++) {
		            var c = lineList[i];
		            if(c.isPointInStroke(context, x, y) || c.isCurrent) {
		            	_self.reDraw({x:x, y:y,booleanShowTrainDetail:false});
		            	
		            	_currentTrainObj = c;
		            	lineList[i] = c;//修改鼠标选中对象线状态
		            	
		            }
		        }
				
			}
	    };
		
		
	};
	
	
	

	/**
	 * public
	 */
	this.clearChart = function() {
		//清除画布所有元素
		_context.clearRect(0,0,_canvas.width,_canvas.height);
	};
	
	
	
	/**
	 * 鼠标左键选中后重新绘图
	 * public
	 * 
	 * @param expandObj 可选参数 扩展对象
	 * 			{
	 * 				x : 247,						//可选参数 当前鼠标x坐标
	 * 				y : 458,					 	//可选参数 当前鼠标y坐标
	 * 				trainNbr : "K818",				//可选参数 车次号，外部传入条件，以便绘图时该车次高亮显示，类是鼠标选中效果
	 * 				booleanShowTrainDetail : true, 	//可选参数 是否显示该车次详细信息
	 * 				groupSerialNbr : "1" 			//可选参数 列车组号
	 * 			}
	 */
	 this.reDraw =function(expandObj) {
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
			myCanvasComponent.drawJxgx(_color, _obj.data.jxgx, i);
			
			var diagram = false;
			//分界口 复选框事件（简图/详图）
			if($("#canvas_checkbox_stationType_jt").attr("checked")) {
				diagram = true;
			}
			
			//2.3绘制交路起止标识
			myCanvasComponent.drawJlStartAndEnd(_color, _obj.data.trains, i, diagram);
		};
    };
    
    /**
     * 交路突出显示
     */
	this.highlightJl = function() {
//		var _currentTrainObj = null;
//		for(var i = 0; i < lineList.length; i++) {
//            var c = lineList[i];
//            if(c.isPointInStroke(context, _mouseCanvsPoint.x, _mouseCanvsPoint.y)) {
//            	//_self.reDraw({x:x, y:y,booleanShowTrainDetail:false});
//            	c.isCurrent = true;
//            	lineList[i] = c;//修改鼠标选中对象线状态
//            	_currentTrainObj = c;
//            	
//            } else {
//            	c.isCurrent = false;
//            	lineList[i] = c;//修改鼠标选中对象线状态
//            }
//        }
		
		

    	_self.reDraw({groupSerialNbr:_currentTrainObj.obj.groupSerialNbr, booleanShowTrainDetail:false});
    	$(".rightMenu").hide();
		
	};
	
	
    /**
     * 显示时刻表
     */
	this.showTrainRunTime = function() {
    	$(".rightMenu").hide();  
    	cross.loadStns(_currentTrainObj); 
		//showWarningDialog("抱歉！时刻表功能尚未开发，请耐心等待！");
		return;
	};
	
	/**
	 * 显示trian调整记录
	 */
	this.showTrainModifyRecord = function() {
		$(".rightMenu").hide();  
		cross.loadTrainModifyRecord(_currentTrainObj); 
		return;
	};
	
	/**
	 * 显示整组
	 */
	this.showTrainGroup1 = function() {
		$(".rightMenu").hide();  
		cross.showTrainGroup1(_currentTrainObj); 
		return;
	};

    /**
     * 停运
     */
	this.stopTrain = function() {
    	$(".rightMenu").hide();
    	cross.loadStop(_currentTrainObj.obj);
    	/*
    	showConfirmDiv("提示", "您确定要执行停运操作?</br>" +
    			"（车次："+_currentTrainObj.obj.trainName+"   始发日期："+_currentTrainObj.obj.startDate+"）", function (r) { 
	        if (r) {
				$.ajax({
					url : basePath+"/jbtcx/updateSpareFlag",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({
						spareFlag : "9",
						planTrainId : _currentTrainObj.obj.planTrainId
					}),
					success : function(result) {
						if(result.code == 0){
							showSuccessDialog("(车次："+_currentTrainObj.obj.trainName+")停运成功");
						}else{
							showErrorDialog("(车次："+_currentTrainObj.obj.trainName+")停运失败");
						};
					}
				});
	        }
	        
		});
    	*/
		return;
	};

    /**
     * 停运
     */
	this.stopToStartTrain = function() {
    	$(".rightMenu").hide();
    	cross.loadStopToStart(_currentTrainObj.obj);
		return;
	};
	
    /**
     * 备用转开行
     */
	this.heartBeat = function() {
    	$(".rightMenu").hide();
    	cross.loadBakToUse(_currentTrainObj.obj);
    	/*
    	showConfirmDiv("提示", "您确定要执行启动备用操作?</br>" +
    			"（车次："+_currentTrainObj.obj.trainName+"   始发日期："+_currentTrainObj.obj.startDate+"）", function (r) { 
	        if (r) {
				$.ajax({
					url : basePath+"/jbtcx/updateSpareFlag",
					cache : false,
					type : "POST",
					dataType : "json",
					contentType : "application/json",
					data :JSON.stringify({
						spareFlag : "1",
						planTrainId : _currentTrainObj.obj.planTrainId
					}),
					success : function(result) {
						if(result.code == 0){
							showSuccessDialog("(车次："+_currentTrainObj.obj.trainName+")启动备用成功");
						}else{
							showErrorDialog("(车次："+_currentTrainObj.obj.trainName+")启动备用失败");
						};
					}
				});
	        }
	        
		});
		*/
		return;
	};
	

    /**
     * 开行转备用
     */
	this.loadUseToBak = function() {
    	$(".rightMenu").hide();
    	cross.loadUseToBak(_currentTrainObj.obj);
		return;
	};
	
	
	
	

    /**
     * 调整路径
     */
	this.editTrainPath = function() {
    	$(".rightMenu").hide();
    	
		showWarningDialog("抱歉！调整路径功能尚未开发，请耐心等待！");
		return;
	};
	
	
    /**
     * 调整时刻表
     */
	this.editTrainRunTime = function() {
		$(".rightMenu").hide();
    	cross.loadTrainAllStns(_currentTrainObj.obj);
		return;
	};
	
	/**
	 * 调整径路
	 */
	this.editPathWay = function(){
		$(".rightMenu").hide();
    	cross.loadTrainAllPathway(_currentTrainObj.obj);
		return;
	};
	
//	editTrainStn()
	

    /**
     * 查看乘务信息
     */
	this.showTrainPersonnel = function() {
    	$(".rightMenu").hide();
    	
    	cross.loadTrainPersonnel(_currentTrainObj.obj);
		return;
	};
	

    /**
     * 查看编组信息
     */
	this.showTrainGroup = function() {
    	$(".rightMenu").hide();
    	
		showWarningDialog("抱歉！查看编组信息功能尚未开发，请耐心等待！");
		return;
	};
	

    /**
     * x放大2倍
     */
	this.xMagnification = function() {
    	$(".rightMenu").hide();
    	
    	$("#canvas_event_btn_x_magnification").click();
	};
	

    /**
     * x缩小2倍
     */
	this.xShrink = function() {
    	$(".rightMenu").hide();
    	
    	$("#canvas_event_btn_x_shrink").click();
	};
	

    /**
     * y放大2倍
     */
	this.yMagnification = function() {
    	$(".rightMenu").hide();
    	
    	$("#canvas_event_btn_y_magnification").click();
	};
	


    /**
     * y缩小2倍
     */
	this.yShrink = function() {
    	$(".rightMenu").hide();
    	
    	$("#canvas_event_btn_y_shrink").click();
	};

	/**
	 * 初始化右键出现的菜单
	 * private
	 * 
	 * @param menuParentDivId 右键菜单父div id
	 */
	function initMenu(menuParentDivId) {
		var menuStr = "" +
		"<div class=' rightMenu'>" +
		  "<ul>" +
			  "<li class='vm-list'><i class='fa fa-list-ul'></i>&nbsp;<a href='javascript:_canvasEventComponent.showTrainRunTime();'>&nbsp;时刻表查询</a>" +
//		      	"<ul class='vm-dropdown-menu'>" +
//		      		"<li><a href='javascript:_canvasEventComponent.showTrainRunTime();'><i class='fa fa-hdd-o'></i>&nbsp;时刻表</a></li>" +
//			        "<li><a href='javascript:_canvasEventComponent.showTrainPersonnel();'><i class='fa fa-hdd-o'></i>&nbsp;乘务</a></li>" +
//			        "<li><a href='javascript:_canvasEventComponent.showTrainGroup();'><i class='fa fa-hdd-o'></i>&nbsp;编组</a></li>" +
//			     "</ul>" +
			  "</li>" +
			 
//			  "<li class='vm-list'><i class='fa fa-retweet'></i>&nbsp;&nbsp;计划调整<i class='fa fa-caret-right pull-right vm-list-right'></i>" +
//		      	"<ul class='vm-dropdown-menu'>" +
//		      		"<li><a href='javascript:_canvasEventComponent.editTrainRunTime();'><i class='fa fa-pencil'></i>&nbsp;调整时刻</a></li>" +
//		      		"<li><a href='javascript:_canvasEventComponent.editPathWay();'><i class='fa fa-pencil'></i>&nbsp;调整经路</a></li>" +
//		      		"<li><a href='javascript:_canvasEventComponent.stopTrain();'><i class='fa fa-caret-square-o-up'></i>&nbsp;停运</a></li>" +
//		      		"<li><a href='javascript:_canvasEventComponent.heartBeat();'><i class='fa fa-caret-square-o-up'></i>&nbsp;启动备用</a></li>" +
////				    "<li><a href='javascript:_canvasEventComponent.editTrainPath();'><i class='fa fa-pencil'></i>&nbsp;调整径路</a></li>" +
////				    "<li><a href='javascript:_canvasEventComponent.editTrainRunTime();'><i class='fa fa-pencil'></i>&nbsp;调整时刻</a></li>" +
//			     "</ul>" +
//			  "</li>" +
			  
			  "<li class='vm-list'><i class='fa fa-retweet'></i>&nbsp;&nbsp;计划调整<i class='fa fa-caret-right pull-right vm-list-right'></i>" +
		      	"<ul class='vm-dropdown-menu'>" +
		      		"<li><a href='javascript:_canvasEventComponent.editTrainRunTime();'><i class='fa fa-arrows'></i>&nbsp;调整时刻</a></li>" +
		      		"<li><a href='javascript:_canvasEventComponent.editPathWay();'><i class='fa fa-arrows-alt'></i>&nbsp;调整经路</a></li>" +
//				    "<li><a href='javascript:_canvasEventComponent.editTrainPath();'><i class='fa fa-pencil'></i>&nbsp;调整径路</a></li>" +
//				    "<li><a href='javascript:_canvasEventComponent.editTrainRunTime();'><i class='fa fa-pencil'></i>&nbsp;调整时刻</a></li>" +
			     "</ul>" +
			  "</li>" +
			  
			  "<li class='vm-list'><i class='fa fa-ban'></i>&nbsp;&nbsp;停运<i class='fa fa-caret-right pull-right vm-list-right'></i>" +
		      	"<ul class='vm-dropdown-menu'>" +
		      	"<li><a href='javascript:_canvasEventComponent.stopTrain();'><i class='fa fa-toggle-off'></i>&nbsp;开行转停运</a></li>" +
		      	"<li><a href='javascript:_canvasEventComponent.stopToStartTrain();'><i class='fa fa-toggle-on'></i>&nbsp;停运转开行</a></li>" +
//				    "<li><a href='javascript:_canvasEventComponent.editTrainPath();'><i class='fa fa-pencil'></i>&nbsp;调整径路</a></li>" +
//				    "<li><a href='javascript:_canvasEventComponent.editTrainRunTime();'><i class='fa fa-pencil'></i>&nbsp;调整时刻</a></li>" +
			     "</ul>" +
			  "</li>" +
			  
			  "<li class='vm-list'><i class='fa fa-dot-circle-o'></i>&nbsp;&nbsp;备用<i class='fa fa-caret-right pull-right vm-list-right'></i>" +
		      	"<ul class='vm-dropdown-menu'>" +
		      	"<li><a href='javascript:_canvasEventComponent.loadUseToBak();'><i class='fa fa-circle-o'></i>&nbsp;开行转备用</a></li>" +
		      	"<li><a href='javascript:_canvasEventComponent.heartBeat();'><i class='fa fa-circle'></i>&nbsp;备用转开行</a></li>" +
//				    "<li><a href='javascript:_canvasEventComponent.editTrainPath();'><i class='fa fa-pencil'></i>&nbsp;调整径路</a></li>" +
//				    "<li><a href='javascript:_canvasEventComponent.editTrainRunTime();'><i class='fa fa-pencil'></i>&nbsp;调整时刻</a></li>" +
			     "</ul>" +
			  "</li>" +
			  
			  "<li class='vm-list'><i class='fa fa-list-ol'></i>&nbsp;<a href='javascript:_canvasEventComponent.showTrainModifyRecord();'>&nbsp;调整记录查询</a>" +
			  "</li>" +
			  
			  "<li class='vm-list'><i class='fa fa-server'></i>&nbsp;<a href='javascript:_canvasEventComponent.showTrainGroup1();'>&nbsp;显示整组</a>" +
			  "</li>" +
//		  	"<li><a href='javascript:_canvasEventComponent.highlightJl();'><i class='fa fa-crosshairs'></i>&nbsp;交路突出显示</a></li>" +
//		    "<li><a href='javascript:_canvasEventComponent.stopTrain();'><i class='fa fa-caret-square-o-up'></i>&nbsp;停运</a></li>" +
//		    "<li><a href='javascript:_canvasEventComponent.editTrainPath();'><i class='fa fa-pencil'></i>&nbsp;调整径路</a></li>" +
//		    "<li><a href='javascript:_canvasEventComponent.editTrainPath();' data-toggle='modal' data-target='#myModal-update'><i class='fa fa-pencil'></i>&nbsp;调整径路</a></li>" +
		    
//		    "<li class='vm-list'><i class='fa fa-search'></i>&nbsp;缩放<i class='fa fa-caret-right pull-right vm-list-right'></i>" +
//		      "<ul class='vm-dropdown-menu'>" +
//		        "<li><a href='javascript:_canvasEventComponent.xMagnification();'><i class='fa fa-search-plus'></i>&nbsp;X+</a></li>" +
//		        "<li><a href='javascript:_canvasEventComponent.xShrink();'><i class='fa fa-search-minus'></i>&nbsp;X-</a></li>" +
//		        "<li><a href='javascript:_canvasEventComponent.yMagnification();'><i class='fa fa-search-plus'></i>&nbsp;Y+</a></li>" +
//		        "<li><a href='javascript:_canvasEventComponent.yShrink();'><i class='fa fa-search-minus'></i>&nbsp;Y-</a></li>" +
//		      "</ul>" +
//		    "</li>" +
//		  		"</ul>" +
		  "</ul>" +
		"</div>";
		$("body").append(menuStr);
	};
	
	
	
	
	
	
};



//$(function(){
//	_canvasEventComponent = new CanvasEventComponent("canvas_event_getvalue");
//	_canvasEventComponent.init();
