String.prototype.trim = function () {
	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
}

function isEmpty(str){
	if(str == null || str== undefined || str.trim().length == 0)
		return true;
	else
		return false;
}

/**
 * 根据id显示一个div并填入内容，type：'add'表示内容是在现有内容后增加，否则表示将现有内容清除掉，展示新输入的内容
 * @param type
 * @param div_id
 */
function showDivById(div_id,content){
	$("#"+div_id).empty();
	
	$("#"+div_id).append(content);
	$("#"+div_id).show();
}

function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) 
		return unescape(r[2]); 
	return null; //返回参数值
} 

var chooseAllCheckBoxes = function(id,name){
	$("input[name='"+name+"']:checkbox").each(function(){ 
        this.checked = $("#"+id).is(':checked');
    });
}