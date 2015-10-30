
function GetDateDiff(startTime, endTime)
{ 
	var result = "";
	
	var date3=endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
	
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000));
	
	result += days > 0 ? days + "天" : "";  
	//计算出小时数
	var leave1=date3%(24*3600*1000);     //计算天数后剩余的毫秒数
	var hours=Math.floor(leave1/(3600*1000));
	
	result += hours > 0 ? hours + "小时" : ""; 
	
	//计算相差分钟数
	var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
	var minutes=Math.floor(leave2/(60*1000));
	
	result += minutes > 0 ? minutes + "分" : "";
	//计算相差秒数
	var leave3=leave2%(60*1000);          //计算分钟数后剩余的毫秒数
	var seconds=Math.round(leave3/1000);
	
	result += seconds > 0 ? seconds + "秒" : "";  
	 
	return result == "" ? "不停靠" : result; 
} 
 
function filterValue(value){
	return value == null || value == "null" ? "--" : value;
}

function TrainInfo(checkbox){
	var _self = this;
	_self.checkbox = checkbox;
	_self.requestId = null;
	_self.routLines = null;
	_self.setLines = function(arr){
		_self.routLines = arr;
	}; 
	_self.setRequestId = function(requestId){
		_self.requestId = requestId;
	};
	
}
 
var PlanConstructionPage = function(){
	

	var _self = this; 
	
	_self.selectTrains = [];
	
	_self.trainLineMap = [];
	
	_self.results = {};
	
	_self.showTable = function(skarr){
		
		_plan_review_table_trainLine.find("tr:gt(0)").remove();  
		
		$.each(skarr, function(i, trainObj){
			var tr = $("<tr/>");
			tr.append("<td>"+(trainObj.index+1)+"</td>");
			tr.append("<td>"+filterValue(trainObj.name)+"</td>");
			tr.append("<td>"+filterValue(trainObj.bureauShortName)+"</td>"); 
			if(trainObj.sourceTimeDto2 != null){
				var a = trainObj.sourceTimeDto2.substring(5).replace(/-/, "");
				tr.append("<td>"+a+"</td>");
			}else{
				tr.append("<td>--</td>");
			}
			
			if(trainObj.targetTimeDto2 != null){
				var a = trainObj.targetTimeDto2.substring(5).replace(/-/, "");
				tr.append("<td>"+a+"</td>");
			}else{
				tr.append("<td>--</td>");
			} 
			
			if(trainObj.sourceTimeDto2 && trainObj.targetTimeDto2){
			
				var sourceTimeDto1 =  new Date(trainObj.sourceTimeDto2); 
				
				var targetTimeDto1 = new Date(trainObj.targetTimeDto2);  
				 
				tr.append("<td>"+GetDateDiff(sourceTimeDto1, targetTimeDto1)+"</td>");  
			}else{
				tr.append("<td>出发</td>"); 
			}
			
			
			tr.append("<td>"+filterValue(trainObj.trackName)+"</td>");
			
			 
			_plan_review_table_trainLine.append(tr);   
		});
	};
	 
   _self.onReplay = function(message){
	   var trainLines = $.parseJSON(message); 
		 if(trainLines.code != "-1"){   
			 if (trainLines.data != null) { 
				 var temp = trainLines.data[0];
					var skarr = []; 
					if(temp.scheduleDto.sourceItemDto != null){
						skarr.push(temp.scheduleDto.sourceItemDto); 
					}   
					
					if(temp.scheduleDto.routeItemDtos != null && temp.scheduleDto.routeItemDtos.length > 0){
						$.each(temp.scheduleDto.routeItemDtos,function(i, a){ 
							skarr.push(a);
						});
					} 
					if(temp.scheduleDto.targetItemDto != null){
						skarr.push(temp.scheduleDto.targetItemDto);
					} 
					
					skarr.sort(function(a, b){  
						return a.index - b.index;
					});  
					
					 
					for(var i = 0; i < _self.trainLineMap.length; i++){  
						if(_self.trainLineMap[i].requestId == trainLines.requestId){  
							_self.trainLineMap[i].setLines(skarr);
							$(_self.trainLineMap[i].checkbox).parent().parent().find("td:eq(7)").html("<span color=\'red\'>生成运行线成功</span>");
							break;
						}
					} 
					 
					//_self.showTable(skarr);
			 }
		 }else{   
			 for(var i = 0; i < _self.trainLineMap.length; i++){
				 if(_self.trainLineMap[i].routLines != null){
					 _self.trainLineMap[i].routLines = null;
				 }
				 if(_self.trainLineMap[i].requestId == trainLines.requestId){  
					   $(_self.trainLineMap[i].checkbox).parent().parent().find("td:eq(7)").html("<span color=\'red\'>生成运行线失败</span>");
						break;
				}
				
			} 
		 }   
   };
	/**
	 * socket连接
	 */
//	_self.connect = function() {
//	     stompClient.connect({}, function(frame) { 
//	    	 //监听xx日计划开始事件
//			 stompClient.subscribe("/railwayplan/plan.freightTransport.replay", function(message) {   
//				 var trainLines = $.parseJSON($.parseJSON(message.body.toString())); 
//				 if(trainLines.code != "-1"){   
//					 if (trainLines.data != null) { 
//						 var temp = trainLines.data[0];
//							var skarr = []; 
//							if(temp.scheduleDto.sourceItemDto != null){
//								skarr.push(temp.scheduleDto.sourceItemDto); 
//							}   
//							
//							if(temp.scheduleDto.routeItemDtos != null && temp.scheduleDto.routeItemDtos.length > 0){
//								$.each(temp.scheduleDto.routeItemDtos,function(i, a){ 
//									skarr.push(a);
//								});
//							} 
//							if(temp.scheduleDto.targetItemDto != null){
//								skarr.push(temp.scheduleDto.targetItemDto);
//							} 
//							
//							skarr.sort(function(a, b){  
//								return a.index - b.index;
//							});  
//							
//							 
//							for(var i = 0; i < _self.trainLineMap.length; i++){  
//								if(_self.trainLineMap[i].requestId == trainLines.requestId){  
//									_self.trainLineMap[i].setLines(skarr);
//									$(_self.trainLineMap[i].checkbox).parent().parent().find("td:eq(7)").html("<span color=\'red\'>生成运行线成功</span>");
//									break;
//								}
//							} 
//							 
//							//_self.showTable(skarr);
//					 }
//				 }else{   
//					 for(var i = 0; i < _self.trainLineMap.length; i++){
//						 if(_self.trainLineMap[i].routLines != null){
//							 _self.trainLineMap[i].routLines = null;
//						 }
//						 if(_self.trainLineMap[i].requestId == trainLines.requestId){  
//							   $(_self.trainLineMap[i].checkbox).parent().parent().find("td:eq(7)").html("<span color=\'red\'>生成运行线失败</span>");
//								break;
//						}
//						
//					} 
//				 }   
//			 }); 
//	    }, function(error) {
////	    	console.log("STOMP protocol error " + error);
//	    });
//	};
	_self.logout = function() {
		stompClient.disconnect();
		window.location.href = "../logout.html";
	};

	//获取当期系统日期
	this.currdate =function(){
		var d = new Date();
		var year = d.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = d.getMonth()+1;       //获取当前月份(0-11,0代表1月)
		var days = d.getDate() + 1; 
		return year+"-"+month+"-"+days;
	};
	
	
	this.initPage = function() {
		//1.初始化日期控件值
		_plan_construction_selectdate.datepicker();
		_plan_construction_selectdate.val(this.currdate());//获取当期系统日期 
	 
		//_plan_construction_table.find("tr:gt(0)").remove();//清除计划列表所有数据  
		
		
		//初始化socket连接
//		_self.connect();
		
		
	};
	
	_self.createConstructionPlain = function(){ 
		commonJsScreenLock();
		if(_plan_construction_selectdate.val() == null || _plan_construction_selectdate.val() == ""){
			showErrorDialog("日期不能为空");
			_plan_construction_selectdate.focus();
			commonJsScreenUnLock();
			return;
		}
		if(_self.selectTrains.length <= 0){
			showErrorDialog("请选中一行记录");
			commonJsScreenUnLock();
			return
		} 
		for(var i = 0; i < _self.selectTrains.length; i++){
			var train = _self.selectTrains[i];
			 $(train).parent().parent().find("td:eq(7)").html("<span color=\'red\'>正在生成运行线</span>"); 
			 var trainInfo = null;
			 for(var z = 0; z < _self.trainLineMap.length; z++){
				 if(_self.trainLineMap[z].checkbox == train){
					 trainInfo = _self.trainLineMap[z];
					 break;
				 }
			 } 
			 if(trainInfo == null){
				 trainInfo = new TrainInfo(train);
			 }
			 _self.trainLineMap.push(trainInfo);  
			$.ajax({
				url : basePath+"/construction/createConstructionPlain",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					runDate : _plan_construction_selectdate.val(),//开行日期
					trainNbr : train.value //车次号 
				}),
				success : function(result) {    
					if (result != null && result != "undefind" && result.code == "0") {  
						var trainNbr = $.parseJSON(this.data).trainNbr;
						var result1 = $.parseJSON(result.data.result); 
						if(result1.code == "-1"){
							showErrorDialog("接口调用返回错误，code="+result1.code+"   message:"+result1.message);
							return;
						}
						 
						for(var j = 0; j < _self.trainLineMap.length; j++){
							 
							if(_self.trainLineMap[j].checkbox.value == trainNbr){
								
								_self.trainLineMap[j].setRequestId(result1.requestId);
							}
						}  
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					} 
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){
					commonJsScreenUnLock();
				}
			})
		}
	
		
	};
	 
	_self.queryConstructionDetail = function() { 
		commonJsScreenLock();
		_plan_construction_btnQuery.attr("disabled", "disabled");
		if(_plan_construction_input_trainNbr.val() == null 
				|| _plan_construction_input_trainNbr.val() == "输入车次查询"
				||  _plan_construction_input_trainNbr.val() == ""){
			showErrorDialog("车次不能为空");
			_plan_construction_input_trainNbr.focus();
			_plan_construction_btnQuery.removeAttr("disabled");
			return;
		}
		//_plan_construction_table.find("tr:gt(0)").remove();//清除车次详细信息列表所有数据
		$.ajax({
			url : basePath+"/construction/queryConstructionDetail",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({ 
				trainNbr : _plan_construction_input_trainNbr.val()//车次号 
			}),
			success : function(result) {   
//	            <td><div style="text-align:center;margin:2px 0 10px 0;"><input type="checkbox"/></div></td> 
				if (result != null && result != "undefind" && result.code == "0") {
					if (result.data !=null) {  
						$.each(result.data,function(n,constructionObj){   
							var result = $.parseJSON(constructionObj);
							if(result.code == "-1"){
								showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
								commonJsScreenUnLock();
								return;
							}
							var temp = result.data[0];
							if(temp == null){ 
								showErrorDialog("没有查到该列车，请核对后重新输入查询");  
								commonJsScreenUnLock();
								return;
							}
							//alert("input:checkbox[value='" + temp.id + "']") 
							_plan_review_table_trainInfo.find("input:checkbox[value='" + temp.id + "']").parent().parent().remove(); 
							 
							var tr = $("<tr/>");
							tr.append("<td><input type='checkbox' name='subBox' value='" + temp.id + "'></td>");
							tr.append("<td>"+filterValue(temp.name)+"</td>");
							 
							var sourceItemDto = temp.scheduleDto.sourceItemDto;
							var targetItemDto = temp.scheduleDto.targetItemDto; 
							
							if(sourceItemDto != null){
								tr.append("<td>"+filterValue(sourceItemDto.bureauShortName)+"</td>");
								tr.append("<td>"+sourceItemDto.name+"</td>"); 
								if(sourceItemDto.sourceTimeDto2 != null){
									tr.append("<td>"+sourceItemDto.sourceTimeDto2.substring(sourceItemDto.sourceTimeDto2.indexOf(":") + 1)+"</td>");
								}else{
									tr.append("<td>--</td>");
								}
							}else{
								tr.append("<td>--</td>");
								tr.append("<td>--</td>");
								tr.append("<td>--</td>");
							}
							if(targetItemDto != null){
								tr.append("<td>"+targetItemDto.name+"</td>");
								if(targetItemDto.sourceTimeDto2 != null){
									tr.append("<td>"+targetItemDto.sourceTimeDto2.substring(targetItemDto.sourceTimeDto2.indexOf(":") + 1)+"</td>");
								}else{
									tr.append("<td>--</td>");
								} 
							}else{
								tr.append("<td>--</td>");
								tr.append("<td>--</td>");
							} 
							tr.append("<td color='red'>可生成运行线</td>");
							tr.click(function(){
								_plan_review_table_trainLine.find("tr:gt(0)").remove();  
								var value = $(this).find("input:checkbox").val(); 
								for(var i = 0; i < _self.trainLineMap.length; i++){  
									if(value == _self.trainLineMap[i].checkbox.value){   
										if(_self.trainLineMap[i].routLines != null){ 
											_self.showTable(_self.trainLineMap[i].routLines);
										}
										break;
									}
								} 
							});
							_plan_review_table_trainInfo.append(tr);    
						});
						
					}
					_plan_review_table_trainInfo.find("tr:gt(0) input:checkbox").unbind();
					_plan_review_table_trainInfo.find("tr:gt(0) input:checkbox").change(function(){
						//_self.createConstructionPlain(this);
						//_self.createConstructionPlain(); 
//						if($("input[name='subBox']").length == $("input[name='subBox']:checked").length){
//							$("#checkAll").attr("checked", true);
//						}else{
//							$("#checkAll").attr("checked", false);
//						}
						if(this.checked){  
							if(_self.selectTrains.length > 0){
								for(var i = 0; _self.selectTrains.length; i++){
									if(_self.selectTrains[i] == this){
										break;
									}else{ 
										_self.selectTrains.push(this);
										break;
									}
								}
							}else{ 
								_self.selectTrains.push(this);
							} 
						}else{   
							for(var i = 0; _self.selectTrains.length; i++){
								if(_self.selectTrains[i] == this){
									_self.selectTrains.splice(i,1); 
									break;
								}
							}
						}
						
						//;
					});
				} else {
					showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					commonJsScreenUnLock();
				} 

			},
			error : function() {
				commonJsScreenUnLock();
				showErrorDialog("接口调用失败");
			},
			complete: function(){
				_plan_construction_btnQuery.removeAttr("disabled");
				commonJsScreenUnLock();
			}
		});
	};
	
	
	
	
	
};

var _PlanViewPage = null;
var _plan_construction_table = $("#plan_construction_table");
var _plan_construction_btnQuery = $("#plan_construction_btnQuery");// 错误数总计 
var _plan_construction_input_trainNbr = $("#plan_construction_input_trainNbr");// 车次详情div 车次号input
var _plan_construction_selectdate = $("#plan_construction_selectdate");// 日期

var _plan_construction_createRunLine = $("#plan_construction_createRunLine");// 
 
 
var _plan_review_table_trainInLine = $("#plan_review_table_trainInLine");
 
var _plan_review_table_trainLine = $("#plan_review_table_trainLine");
 
var _plan_review_table_trainInfo = $("#plan_review_table_trainInfo");
var socket = null;
var stompClient = null;
$(function(){
	_PlanConstructionPage = new PlanConstructionPage();  
	
	//车次详情div 车次查询按钮增加事件
	_plan_construction_btnQuery.click(function(){
		_PlanConstructionPage.queryConstructionDetail();
	});
	
	_plan_construction_createRunLine.click(function(){
		_PlanConstructionPage.createConstructionPlain();
	});
	//车次详情div 车次号input 增加回车事件
	_plan_construction_input_trainNbr.keypress(function(event){  
	    var keycode = (event.keyCode ? event.keyCode : event.which);
	    if(keycode == '13'){
	    	_PlanConstructionPage.queryConstructionDetail();
	    }
	});  
	_plan_construction_input_trainNbr.keyup(function(event){
		_plan_construction_input_trainNbr.val(_plan_construction_input_trainNbr.val().toUpperCase());
	});
	
//	 $("#checkAll").change(function() {
//		 $('input[name="subBox"]').attr("checked", this.checked);
//     });
     
	//创建websocket连接
//	socket = new SockJS(basePath+'/portfolio');
//    stompClient = Stomp.over(socket);
    _PlanConstructionPage.initPage(); 
	
});



