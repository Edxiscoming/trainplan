$(function() { 

	var chartIds = $("#chartIds").val();
	var crossNames = $("#crossNames").val();
	var crossIds = $("#crossIds").val();
	var message = "检测到该方案中存在同名交路单元，是否覆盖？ \n同名的交路名：\n\n";
	var names = crossNames.split(",");
	if(names.length==1){
		message = message + "1. " + crossNames;
	}else{
		for (var int = 0; int < names.length; int++) {
			message = message + (int+1) + ". " + names[int]+"\n"
		}
		
	}
	$("#ms").val(message);
	
//	windows.location.href = basePath+"/cross/repeatCreateUnitCross?crossIds="+crossIds;
	
	$("#shengcheng").click(function(){
//		$.ajax({
//			url : "repeatCreateUnitCross?chartIds="+chartIds+"&crossNames="+crossNames+"&crossIds="+crossIds,
//			cache : false,
//			type : "GET",
//			success : function(result) { 
//				showSuccessDialog("生成成功");  
//			},
//			error : function() {
//				showErrorDialog("生成成功");  
//			},
//			complete : function(){ 
//			}
//		});
		window.parent.repeatCreateCrossUnit(chartIds,crossNames,crossIds);//刷新
	});
	$("#cencel").click(function(){
		window.parent.closeCreateUnitDialog();//刷新
	});
});
