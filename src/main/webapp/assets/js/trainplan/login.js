/**
 * Created by star on 5/15/14.
 */
$(function() {
	// 不需要了，要先选择路局
	// $("#inputUsername").focus();
	$("#inputUsername").blur(
			function() {
				if ($("#inputUsername").val().trim().length == 0) {
					return false;
				}
				$.ajax(
						{
							url : "user/" + $("#inputUsername").val().trim()
									+ "/account",
							method : "GET",
							contentType : "application/json; charset=UTF-8"
						}).done(
						function(list) {
							$("#inputAccount option").remove();
							for (var i = 0; i < list.length; i++) {
								$("#inputAccount").append(
										"<option value='" + list[i].ACC_ID
												+ "'>" + list[i].ACC_NAME
												+ "</option>");
							}
						}).fail(function() {

				}).always(function() {
				})
			});

	$("#loginForm").submit(
			function() {
				if (!$("#department").val()) {
					$('#lj').text(' * 请选择路局');
					return false;
				} else {
					$('#lj').text('');
				}
				if (!$.trim($("#inputUsername").val())) {
					$('#userName').text(' * 请输入用户名');
					return false;
				} else {
					$('#userName').text('');
				}
				if (!$("#inputAccount").val()) {
					$('#acc').text(' * 请选择岗位');
					return false;
				} else {
					$('#acc').text('');
				}
				if (!$("#inputPassword").val()) {
					$('#pw').text(' * 请输入密码');
					return false;
				} else {
					$('#pw').text('');
				}
				$("input[name='username']").val(
						$("#inputUsername").val() + "@"
								+ $("#inputAccount").val() + "@"
								+ $("#department").val());
				$("input[name='password']").val($("#inputPassword").val());

			});

	$("#inputPassword").bind("keydown", function(e) {

		if (e.keyCode == 13) {
			e.preventDefault();

			$("#loginForm").submit();
		}
	});

	/**
	 * 获取项目根路径
	 * 
	 * @returns /projectPath
	 */
	function getRootPath() {
		var _self = this;
		// 获取主机地址之后的目录，如： /uimcardprj/share/meun.jsp
		var pathName = _self.location.pathname;

		// 获取带"/"的项目名，如：/uimcardprj
		var projectName = pathName.substring(0,
				pathName.substr(1).indexOf('/') + 1);
		return projectName;
	}
	;

	var _self = this;
	var currentLocation = _self.location; // 当前页面的location
	var topLocation = top.location; // 当前页面所属的父页面的location

	if (topLocation != currentLocation) {
		top.location = getRootPath() + "/login";
	}

});

/**
 * 2015-4-16.
 */
function focusUserName() {
	$("#inputUsername").focus();
}

function openChrome() {
	window.open('/trainplan/Chrome_Setup.exe', '_blank');
}

function resetData() {
	$("input").val("");
	$("select").val("");
}