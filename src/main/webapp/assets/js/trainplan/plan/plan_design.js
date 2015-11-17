/**
 * 计划参数设置页面
 * @author denglj
 */


var PlanDesignPage = function(){

	var _self = this;
	
	//获取当期系统日期
	this.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		return year+"-"+month+"-"+days;
	};
	
	//获取所有方案信息
	self.loadSchemeInfo = function() {
		_input_plan_design_scheme.empty();
		$.ajax({
			url : basePath+"/plan/getSchemeList",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					$.each(result.data,function(n,level){
						_input_plan_design_scheme.append('<option value="'+level.schemeId+'">'+level.schemeName+'</option>');
					});
					
					//初始化隐藏值
					$("#input_plan_design_scheme_text").val(_input_plan_design_scheme.find("option:selected").text());
					
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}

			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			}
		});
	};

};

var _planDesignPage = null;
var _input_plan_design_startDate = $("#input_plan_design_startDate");//计划开始日期
var _input_plan_design_scheme = $("#input_plan_design_scheme");//方案
var _input_plan_design_days = $("#input_plan_design_days");//天数

$(function(){
	_planDesignPage = new PlanDesignPage();
	var currdays = _planDesignPage.currdate();
	
	self.loadSchemeInfo();
	
	//渲染开始日期
	_input_plan_design_startDate.datepicker({
	      dateFormat : "yy-mm-dd",
	      defaultDate: "+1w",
	      changeMonth: true,
	      numberOfMonths: 1,
	      minDate  : currdays
	});
	_input_plan_design_startDate.val(currdays);
	
	
	
});



function planDesignSchemeChange() {
	$("#input_plan_design_scheme_text").val(_input_plan_design_scheme.find("option:selected").text());
//	var url ="?schemeVal="+_input_plan_design_scheme.val()+"&schemeText="+_input_plan_design_scheme.find("option:selected").text()+"&days="+_input_plan_design_days.val()+"&startDate="+_input_plan_design_startDate.val();
//	url = encodeURI(encodeURI(url));
//	window.location.replace("plan_view.html"+url);
}
