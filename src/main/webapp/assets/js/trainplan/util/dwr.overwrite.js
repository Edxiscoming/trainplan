/**
 * 重写dwr 错误处理方法
 */
dwr.engine._errorHandler = function(message, ex) {
//	dwr.engine._debug("Error: " + ex.name + ", " + ex.message, true);
	
	if (ex.message !=null && ex.message=="Error") {
		//showErrorDialog("消息服务异常： 未知错误。");
	}else if (ex.name.indexOf("dwr.engine.http.0") > -1) {
		//showErrorDialog("消息服务异常： 服务器连接断开。请尝试刷新页面，重新连接");
	} else {
		//showErrorDialog("消息服务异常： 未知错误。");
	}
	
};
dwr.engine.setActiveReverseAjax(true);