$(function() {
	var urlstr = "../../user/loadUserMsg";
//	$.getJSON(url, function(json) {
//		alert(json);
//		$('#selYhgwId').html('');
//		if (json.length == 0) {
//			alert('该用户没有分配岗位，请与管理员联系！');
//			return false;
//		}
//		var opt = "";
//		for (var i = 0; i < json.length; i++) {
//			opt += "<option value='" + json[i].id + "'>" + json[i].name
//					+ "</option>";
//		}
//		$('#selYhgwId').append(opt);
//	});
	
	
	$.ajax({
	    type: "GET",
	    url: urlstr,
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",
	    success: function (data) {
	        // Play with returned data in JSON format
//	        alert(data.data);
	    	$("#loadUserSpan").text(data.data);
	    },
	    error: function (msg) {
	    	$("#loadUserSpan").text("未登录");
	    }
	});
});