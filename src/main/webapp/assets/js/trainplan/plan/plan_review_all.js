/**
 * 路局XX日开行计划汇总统计信息
 * 
 * @author denglj 
 */
var PlanReViewAllPage = function(){
	
	this.initPage = function() {
		_plan_review_all_selectdate.datepicker();
		_plan_review_all_selectdate.val(this.currdate());
		
		//加载路局开行计划统计信息
		this.loadFullStationTrains();
	};
	
	
	//获取当期系统日期
	this.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate(); 
		return year+"-"+month+"-"+days;
	};
	
	
	

	//加载路局开行计划统计信息
	this.loadFullStationTrains = function() {
		_plan_review_all_btnQuery_ljtjxx.attr("disabled",true);
		commonJsScreenLock();//锁屏
		
		_plan_review_all_table_tjxx.find("tr:gt(1)").remove();//清除路局开行计划统计信息
		
		
		
		$.ajax({
			url : basePath+"/plancheck/getFullStationTrains",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				runDate : _plan_review_all_selectdate.val()
			}),
			success : function(result) {
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {

						var index = 1;
						var startTotal = 0;//始发总合计 = 图定始发总合计 + 临客始发总合计
						var startTotal_Tdsfx = 0;//图定始发线总合计
						var startTotal_Td = 0;//图定始发总合计
						var startTotal_Lk = 0;//临客始发总合计
						$.each(result.data,function(n,trainObj){
							if (!isNaN(parseInt(trainObj.TDSFXJ))) {
								startTotal_Td += parseInt(trainObj.TDSFXJ);
							}
							if (!isNaN(parseInt(trainObj.TDSFXJX))) {
								startTotal_Tdsfx += parseInt(trainObj.TDSFXJX);
							}
							
							var tr = $("<tr/>");
//							tr.append("<td><div style='text-align:center;margin:-5px 0 10px 0;'><input type='checkbox'/></div></td>");
							//序号
							tr.append("<td align='center'>"+index+"</td>");
							tr.append("<td align='center'>"+trainObj.LJJC+"</td>");
							//始发合计
							tr.append("<td align='center'>"+trainObj.TDSFXJ+"</td>");
							
							//始发线合计
//							tr.append("<td>"+trainObj.TDSFXJX+"</td>");
							
							//图定始发信息
							tr.append("<td align='center'>"+trainObj.TDSFXJ+"</td>");
							tr.append("<td align='center'>"+trainObj.TDSFJC+"</td>");
							tr.append("<td align='center'>"+trainObj.TDSFJCX+"</td>");
							tr.append("<td align='center'>"+trainObj.TDSFZD+"</td>");
							tr.append("<td align='center'>"+trainObj.TDSFZDX+"</td>");
							
							//临客始发
							tr.append("<td></td>");
							tr.append("<td></td>");
							tr.append("<td></td>");
							tr.append("<td></td>");
							tr.append("<td></td>");

							//接入合计
							tr.append("<td align='center'>"+trainObj.TDJRXJ+"</td>");
							//图定接入
							tr.append("<td align='center'>"+trainObj.TDJRXJ+"</td>");
							tr.append("<td align='center'>"+trainObj.TDJRJC+"</td>");
							tr.append("<td align='center'>"+trainObj.TDJRZD+"</td>");

							//临客接入
							tr.append("<td></td>");
							tr.append("<td></td>");
							tr.append("<td></td>");

//							//审核状态
//							tr.append("<td></td>");
//
//							//是否生成运行线
//							tr.append("<td></td>");
							_plan_review_all_table_tjxx.append(tr);
							
							index +=1;
						});

						
						//////////////////////////////////////////////////////
						//总合计
						startTotal = startTotal_Td+startTotal_Lk;//始发总合计 = 图定始发总合计 + 临客始发总合计
						if (startTotal ==0) {
							startTotal ="";
						}
						if (startTotal_Td ==0) {
							startTotal_Td ="";
						}
						var tr = $("<tr/>");
						tr.append("<td colspan='2'><div style='text-align:center;'>总合计</div></td>");
						tr.append("<td><div style='text-align:center;'>"+startTotal+"</div></td>");
						_plan_review_all_table_tjxx.append(tr);
					}
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
				}
				
			},
			error : function() {
				showErrorDialog("请求发送失败");
//				showErrorDialog(portal.common.dialogmsg.REQUESTFAIL);
			},
			complete : function() {
				commonJsScreenUnLock();//屏幕解锁

				_plan_review_all_btnQuery_ljtjxx.attr("disabled",false);
				
			}
		});
	};
	
	
	
};

var _PlanReViewAllPage = null;
var _plan_review_all_selectdate = $("#plan_review_all_selectdate");
var _plan_review_all_btnQuery_ljtjxx = $("#plan_review_all_btnQuery_ljtjxx");
var _plan_review_all_table_tjxx = $("#plan_review_all_table_tjxx");


$(function(){
	_PlanReViewAllPage = new PlanReViewAllPage();
	_PlanReViewAllPage.initPage();
	
	_plan_review_all_btnQuery_ljtjxx.click(function(){
		_PlanReViewAllPage.loadFullStationTrains();
	});
	
	

});



