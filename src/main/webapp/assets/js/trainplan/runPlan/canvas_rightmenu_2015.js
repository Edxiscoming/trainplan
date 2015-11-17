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
    	_self.reDraw({groupSerialNbr:_currentTrainObj.obj.groupSerialNbr, booleanShowTrainDetail:false});
    	$(".rightMenu").hide();
		
	};
	
	
    /**
     * 显示时刻表
     */
	this.showTrainRunTime = function() {
    	$(".rightMenu").hide();  
    	cross.loadStns(_currentTrainObj); 
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
			  "</li>" +
		  "</ul>" +
		"</div>";
		$("body").append(menuStr);
	};
};
