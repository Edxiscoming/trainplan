function submit(){
	var bureaus='';
	var common_days='';
	var common_genTime='';
	var high_days='';
	var high_genTime='';
	var flag=true;
	$("#autoGenTab tr").each(function(){
		var tds = this.children;
		if(tds.length == 6){
			if(tds[2].children[0].value>50 || checkV(tds[2].children[0])){
				tds[2].children[0].style.borderColor="red";
				alertMessage("warning","请输入小于50的值");
				flag=false;
			}else{
				tds[2].children[0].style.borderColor="";
			}
			if(tds[4].children[0].value>50 || checkV(tds[2].children[0])){
				tds[4].children[0].style.borderColor="red";
				alertMessage("warning","请输入小于50的值");
				flag=false;
			}else{
				tds[4].children[0].style.borderColor="";
			}
			
			if(tds[3].children[0].value == ''){
				tds[3].children[0].style.borderColor="red";
				alertMessage("warning","请填写正确的执行时间");
				flag=false;
			}else{
				tds[3].children[0].style.borderColor="";
			}
			if(tds[5].children[0].value == ''){
				tds[5].children[0].style.borderColor="red";
				alertMessage("warning","请填写正确的执行时间");
				flag=false;
			}else{
				tds[5].children[0].style.borderColor="";
			}
			
			bureaus += tds[1].innerHTML+',';
			common_days += tds[2].children[0].value+',';
			common_genTime += tds[3].children[0].value+',';
			high_days += tds[4].children[0].value+',';
			high_genTime += tds[5].children[0].value+',';
		}
		
	});
	
	if(flag==false)
	{
		return;
	}
	$.ajax({
		url : "/trainplan/operationplan/saveAutoGenInfo",
		cache : false,
		type : "POST",
		dataType : "json",
		contentType : "application/json",
		data : JSON.stringify({
			common_days : common_days,
			common_genTime : common_genTime,
			high_days : high_days,
			high_genTime : high_genTime,
			bureaus : bureaus
		}),
		success : function(result) {
			if (result != null && result != "undefind"
					&& result.code == "0") {
				showSuccessDialog("保存成功");
				if (result.data != null) {
					self.searchModle().loadChats(result.data);
				}
			} else {
				showErrorDialog("接口调用返回错误，code=" + result.code
						+ "   message:" + result.message);
			}
		},
		error : function() {
			showErrorDialog("接口调用失败");
		},
		complete : function() {
			commonJsScreenUnLock();
		}
	});
}

function checkV(obj){
	var reg = /^[\d]{1,}$/;
	if(obj.value>50 || !reg.test(obj.value))
	{
		console.log( );
		obj.style.borderColor="red";
	}
	else
		obj.style.borderColor="";
		
}