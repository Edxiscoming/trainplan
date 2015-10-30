
$(function(){
	
	var canvas = document.getElementById("trainplan_canvas");
	var context = canvas.getContext('2d');
	var myCanvasComponent = new MyCanvasComponent(context, canvasData.grid.days, canvasData.grid.crossStns);
	var _self = this;
	$("#runline_ky").click(function() {
		context.clearRect(0,0,canvas.width,canvas.height);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		if($("#runline_ky").is(':checked')){
			//绘制客运开行计划
			_self.drawPlanLines();
		}
		if ($("#runline_jbt").is(':checked')) {
			//绘制客运开行计划
			_self.drawRunLines();
		}
    });
	
	$("#runline_jbt").click(function() {
		context.clearRect(0,0,canvas.width,canvas.height);
		//1.绘制网格
		myCanvasComponent.drawGrid("green");
		
		if($("#runline_ky").is(':checked')){
			//绘制客运开行计划
			_self.drawPlanLines();
		}
		if ($("#runline_jbt").is(':checked')) {
			//绘制运行线
			_self.drawRunLines();
		}
		
    });
	
	
	
	/**
	 * 绘制客运开行计划
	 */
	this.drawPlanLines = function() {
		for(var i=0, _len=canvasData.runplan.length;i<_len;i++) {
			new myCanvasComponent.drawTrainRunLine(true, "#8236ac", canvasData.runplan[i]).drawLine(context);
		}
		
	};
	
	/**
	 * 绘制运行线
	 */
	this.drawRunLines = function() {
		for(var i=0, _len=canvasData.runline.length;i<_len;i++) {
			new myCanvasComponent.drawTrainRunLine(true, "#72b5d2", canvasData.runline[i]).drawLine(context);
		}
		
	};
	
	
	
	//绘制客运开行计划
	//1.绘制网格
	myCanvasComponent.drawGrid("green");
	
	//2.绘制客运开行计划
	_self.drawPlanLines();

	//3.绘制运行线
	_self.drawRunLines();
});




