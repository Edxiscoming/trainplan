var cross = null;
$(function() {
	heightAuto();

	cross = new Corss();
	ko.applyBindings(cross);
	cross.init();
});

function heightAuto() {
	var WH = $(window).height();
	$("#div_hightline_planDayDetail").css("height", WH - 360 + "px");
	$("#div_crossDetailInfo_trainStnTable").css("height", 270 + "px");
	$("#canvas_parent_div").css("height", 203 + "px");
	$("#left_height").css("height", WH - 360 - 72 + "px");
};

function Corss() {
	var self = this;

	self.type = ko.observable();
	self.cross = ko.observable();
	self.remark = ko.observable();

	self.init = function() {
		var _height = $(window).height() - $('#div_searchForm').height() - 500
				- 150;
		_height = _height < 80 ? 200 : _height;
	};


	self.save = function() {
		commonJsScreenLock();
		$.ajax({
			url : basePath + "/runPlanLk/saveRunPlanLkSendMsg",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify({
				crossIds : $("#cmdTrainIds").val(),
				operType : $('input:radio:checked').val(),
				remark: $("#remark").val(),
				type : $("#type").val()
			}),
			success : function(result) {
				if (result != null && result.code == "0") {
					showSuccessDialog("保存成功");
				} else {
					showErrorDialog("保存失败" + result.message);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showErrorDialog("保存失败");
			},
			complete : function() {
				commonJsScreenUnLock();
			}
		});
	}
}