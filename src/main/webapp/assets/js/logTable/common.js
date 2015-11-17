
//担当局中英对照
var globelBureau={
		
}
//设置中英对照关系
function setBureau(key,value){
	globelBureau[key] = value;
}
//根据英文获取中文担当局
function getBureau(key){
	return globelBureau[key] ? globelBureau[key]:key;
}

//路局串转换。
function getBureauS(keys){
	var result = '';
	if(keys){
		for(var i=0;i<keys.length;i++){
			result += ","+getBureau(keys.charAt(i));
		}
		result = result.substr(1);
	}
	return result;
}

//小写转化成大写
function upperCase(id){
	var node=document.getElementById(id);
	node.value=node.value.toUpperCase();
}

//毫秒的时间改为后台需要的格式
//输入格式：124789989839
//返回：yyyy-mm-dd hh：mm：ss
function toNeedDate(str){
	var dateAndTime=new Date(str);
	var hours=dateAndTime.getHours();
	var min=dateAndTime.getMinutes();
	var sec=dateAndTime.getSeconds();
	if(hours<10)
		var hours="0"+hours;
	if(min<10)
		var min="0"+min;
	if(sec<10)
		var sec="0"+sec;
	var date=dateAndTime.getFullYear()+"-"+dateAndTime.getMonth()+1+"-"+dateAndTime.getDate();
	var time=hours+":"+min+":"+sec;
	return date+' '+time;
}


//消息弹框
//参数type：success/warning/danger
// text:要显示的文字
//sticky :是否自动关闭，不传值时，自动关闭
function alertMessage(type,text,sticky){
	var defaultStick = false;
	if(sticky){
		defaultStick = sticky;
	}
	if(type=="success")
	{
		$.gritter.add({
		title: '成功',
		text: text,
		image: basePath+'/assets/img/screen.png',
		class_name: 'growl-success',
		sticky: defaultStick,
		time: '1600'
		});	
	}
	else if(type=="warning"){
		$.gritter.add({
			title: '提示',
			text: text,
			image: basePath+'/assets/img/screen.png',
			class_name: 'growl-warning',
			sticky: defaultStick,
			time: '1600'
			});	
	}
	else if(type=="danger"){
		$.gritter.add({
			title: '警告',
			text: text,
			image: basePath+'/assets/img/screen.png',
			class_name: 'growl-danger',
			sticky: defaultStick,
			time: '1600'
			});	
	}
}

