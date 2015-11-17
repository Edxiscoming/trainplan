var basePath = "";
$(function(){
	basePath = getRootPath();
	
	var _self = this;
	var currentLocation = _self.location;	//当前页面的location
	var topLocation = top.location;			//当前页面所属的父页面的location
	
	if (currentLocation == topLocation) {
		_self.location = getRootPath()+"/index";
	} else if (currentLocation == "login") {
		top.location.reload();
	}

});


/**
 * 获取项目根路径
 * @returns /projectPath
 */
function getRootPath(){
	var _self = this;
    //获取主机地址之后的目录，如： /uimcardprj/share/meun.jsp
    var pathName = _self.location.pathname;
    
    //获取带"/"的项目名，如：/uimcardprj
    var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return projectName;
}