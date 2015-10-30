var MessageDialog = (function() {

	var _success = function(prev, text) {
			prev.$.messager.show({
				title: "新消息",
				msg: text,
				timeout: 5000,
				showType: "slide"
			});
	};

	return {

		confirm: function(text, ok, cancel) {
			parent.$.messager.confirm("请确认", text, function(result) {

				if(result && Data.isFunction(ok)) {
					ok();

				} else if(Data.isFunction(cancel)) {
					cancel();
				}
			});
		},

		success: function(text) {
			_success(parent, text);
		
		},
		
		alert: function(text) {
			
			$("#_alert-message__").html(text);
			$("#_alert_dialog__").show();
			$("#_alert_dialog_close__").click(function(){
				$("#_alert_dialog__").hide();
			});
			
		},
		
		error: function(text) {
			$.messager.alert("出错啦", text, "error");
		},

		loading: function(flag) {
			$("#_loading_dialog__").dialog(flag === false ? "close" : "open");
		}
	};

})();
