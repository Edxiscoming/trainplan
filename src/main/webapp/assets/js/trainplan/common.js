var timeout = true;
var userId = null;
var userName = null;
var roleType = null;
var currentRole = null;

var _common_div_error_message_text = "";
var _common_div_success_message_text = "";
var _common_div_warning_message_text = "";
var _common_div_confirm_message_text = "";

/*修改后的——小三角+小气泡*/

function triangle(value){

};

Date.prototype.format =function(format)
{
	var o = {
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter
		"S" : this.getMilliseconds() //millisecond
	};
		if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
		(this.getFullYear()+"").substr(4- RegExp.$1.length));
		for(var k in o)if(new RegExp("("+ k +")").test(format))
		format = format.replace(RegExp.$1,
		RegExp.$1.length==1? o[k] :
		("00"+ o[k]).substr((""+ o[k]).length));
	return format;
};

var portal={
	common:{
		dialogmsg:{//portal.common.dialogmsg
			REQUESTSUCCESS:"操作成功",
			REQUESTFAIL:"请求发送失败",
			SYSYEMERROR:"系统发生异常",
			DATALOADFAIL:"数据加载失败",
			GETDATEFAIL:"获取当前日期失败",
			DATETIMEOVER:"你选择的时间跨度超过9周，请重新选择",
			DATEERROR:"请选择正确的时间范围"
		},
		tip:{//portal.common.tip
			APPNAMETEXT:" 应用名称不能为空，由中文、字母、数字、下划线组成，长度不能大于20",
			VERSIONTEXT:"版本不能为空，由数字和小数点组成，长度为3到10",
			APPDESCRIPIONTEXT:"应用描述长度不能大于500",
			SUBDOMAINTEXT:"二级域名仅允许由数字，字母组成，长度为4到18位."
		}
	},
	application:{//portal.application.dialogmsg
		dialogmsg:{
			ONLYONEAPP:"只能实施监控一个应用 !",
			CREATESUCESS:"创建应用基础信息成功",
			CREATEFAIL:"创建应用基础信息失败",
			UPLOADFAIL:"上传文件失败!",
			UPLOADERROR:"请先上传应用包,在编辑运行环境信息"
		},
		tip:{//portal.application.tip
			FILETEXT:"请上传应用包附件，且不能超过100M",
			VMSNUMVALIDATE:"虚拟机数量为必填项，输入为数字，范围：1~9",
			VMSNUMVALIDATE_2:"虚拟机数量为必填项，输入为数字，范围：1~9，必须小于弹性伸缩上限",
			UPLIMITVALIDATE:"弹性伸缩上限为必填项，输入为数字，范围：1~9",
			UPLIMITVALIDATE_2:"弹性伸缩上限必须大于弹性伸缩下限",
	
			MIGRATETEXT:"请选择需要迁移的应用."
		}
	},
	monitor:{
		dialogmsg:{//portal.monitor.dialogmsg
			SELECTSTRATEGY:"请选择监控策略!"
		},
		tip:{//portal.monitor.tip
			LABEL_STRATEGYEXPRESSION_T:"持续时间必须为正整数或0",
			LABEL_STRATEGYEXPRESSION_N:"阈值必须为正整数或0",
			LABEL_STRATEGYEXPRESSION:"请添加策略条件",
			LABEL_INPUT_STRATEGYDESCRIPT:"策略描述信息长度不能大于500",
			_LABEL_STRATEGYNAME:"策略名称由数字、字母、下划线组成长度不能大于100"
			
		}
	},
	resource:{
		dialogmsg:{//portal.resource.dialogmsg
			REQUESTSUCCESS:"创建安装包请求发送成功",
			REQUESTFAIL:"创建安装包请求发送失败",
			DELETEMIDDLESUCCESS:"删除安装包请求发送成功",
			DELETEMIDDLEFAIL:"删除安装包请求发送失败",
			MIDDLESTARTSUCCESS:"安装包启动请求发送成功",
			MIDDLESTARTFAIL:"安装包启动请求发送失败",
			MIDDLESTOPSUCCESS:"安装包启动请求发送成功",
			MIDDLESTOPFAIL:"安装包启动请求发送失败",
			VMSUCCESS:"创建虚拟机规格请求发送成功",
			VMFAIL:"创建虚拟机规格请求发送失败",
			DELETENETSUCCESS:"删除网络资源请求发送成功",
			DELETENETFAIL:"删除网络资源请求发送失败",
		},
		tip:{//portal.resource.tip
			FITEM:"只能选择一个",
		}
	}
	
};



String.prototype.endWith=function(s){
	  if(s==null||s==""||this.length==0||s.length>this.length)
	     return false;
	  if(this.substring(this.length-s.length)==s)
	     return true;
	  else
	     return false;
	  return true;
	 };

 String.prototype.startWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
   return false;
  if(this.substr(0,s.length)==s)
     return true;
  else
     return false;
  return true;
 };

$(function(){
	var common_message_modal_div_str="<!-- Error Button trigger modal -->"
		+"<a id='btn_common_errorMessageModal' data-toggle='modal' href='#commonErrorModal' class='btn btn-primary btn-lg' style='display:none'>Error</a>"
		+"<!-- Modal -->"
		+" <div class='modal fade' id='commonErrorModal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
		+"    <div class='modal-dialog'>"
		+"      <div class='modal-content'>"
		+"      <div class='modal-header'>"
		+"        <button id='btn_common_errorMessageModal_close' type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
		+"        <h4 class='modal-title'>操作失败</h4>"
		+"      </div>"
		+"      <div class='modal-body'>"
		+"        <div class='alert alert-block alert-danger'>"
		+"      <h4>您有一个错误消息!</h4>"
		+"      <p><label id='common_errorModalMessage_text'></label></p>"
		+"    </div>"
		+"      </div>"
		+"    <div class='modal-footer'>"
		+"      <button type='button' id='btn_common_errorMessageModal_confirm' class='btn btn-warning' data-dismiss='modal'>确定</button>"
		+"    </div>"
		+"    </div><!-- /.modal-content -->"
		+"  </div><!-- /.modal-dialog -->"
		+"  </div><!-- /.modal -->"
		+"<!-- Success Button trigger modal -->"
		+"  <a id='btn_common_successMessageModal' data-toggle='modal' href='#commonSuccessMessageModal' class='btn btn-primary btn-lg' style='display:none'>Success</a>"
		+"<!-- Modal -->"
		+"  <div class='modal fade' id='commonSuccessMessageModal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
		+"  <div class='modal-dialog'>"
		+"    <div class='modal-content'>"
		+"      <div class='modal-header'>"
		+"        <button id='btn_common_successMessageModal_close' type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
		+"        <h4 class='modal-title'>操作成功</h4>"
		+"      </div>"
		+"      <div class='modal-body'>"
		+"        <div class='alert alert-block alert-success'>"
		+"      <h4>您有一个成功消息!</h4>"
		+"      <p><label id='common_successModalMessage_text'></label></p>"
		+"    </div>"
		+"      </div>"
		+"    <div class='modal-footer'>"
		+"      <button type='button' id='btn_common_successMessageModal_confirm' class='btn btn-warning' data-dismiss='modal'>确定</button>"
		+"    </div>"
		+"    </div><!-- /.modal-content -->"
		+"  </div><!-- /.modal-dialog -->"
		+"</div><!-- /.modal -->      ";
	
	$("body").append(common_message_modal_div_str);
	
	var common_login_div_str = "<!-- 登录框 -->"
		+ "<div class='modal fade' id='login_popup_div' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
		+ "	<div class='modal-dialog' style='width:420px; margin-top:40px;'>"
		+ "		<div class='modal-content'>"
		+ "			<div class='modal-header' style='padding:10px 15px;'>"
		+ "				<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>"
		+ "				<h4 class='modal-title'><span id='_login_title'>您尚未登陆</span></h4>"
		+ "			</div>"
		+ "		<div class='panel-body row' style=' padding:20px 15px;'>"
		+ "		  <div id='_login_div'>"
		+ "			<div class='row'>"
		+ "				<div class='modal_login'>"
		+ "                 <p style='margin-bottom:5px;'>用户名:</p>"
		+ "					<input type='text' class='form-control'  placeholder='' id='_user_name' style='padding: 4px 30px 4px 12px;'>"
		+ "					<i class='fa fa-user modal_i'></i>"
		+ "				</div>"
		+ "			</div>"
		+ "			<div class='row' >"
		+ "				<div class='modal_login'>"
		+ "                 <p style='margin-bottom:5px;'>密码:</p>"
		+ "					<input type='password' class='form-control'  placeholder='' id='_password' style='padding: 4px 30px 4px 12px;'>"
		+ "					<i class='fa fa-unlock-alt modal_i' style='font-size:18px;'></i>"
		+ "				</div>"
		+ "			</div>"
		+ "			<div id='_msg_div' class='alert alert-danger' style='margin:10px auto;width:200px;height:26px;line-height:26px;font-size:10px;padding-top:0px;display:none; '></div>"	
		+ "			<div class='row'>"
		+ "				<div class='modal_login' style='margin:10px auto;'>"
		+ "					<a type='button' class='btn btn-primary' style='display:block;' id='login_popup_btn' >登&nbsp;&nbsp;&nbsp;录</a>"
		+ "				</div>"
		+ "			</div>"
		+ "		  </div>"
		+ "		  <div class='login_input2 btn-display' id='_chooserole_div' style='display:none'>"
		+ "         <div class='row'>"
		+ "			<div class='col-md-12 col-sm-12  col-xs-12' style='margin-bottom:20px;'> <a id='choose_type_1' type='button' class='btn btn-default btn-big2' ><i class='fa fa-user'></i>管理员</a></div>"	
		+ "			<div class='col-md-12 col-sm-12  col-xs-12' > <a id='choose_type_0' type='button' class='btn btn-primary btn-big2' ><i class='fa fa-user-md'></i>用&nbsp;&nbsp;户</a> </div>"		
        + "       </div>"
		+ "		  </div>"
		+ "		</div>"
		
		+ "	</div>"
		+ "</div>";
	
	$("body").append(common_login_div_str);
	 
	var common_confirm_div_str = ""
		+ '<div class="modal fade" id="_confirm_div" tabindex="-1" style="z-index:9999" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
		+ "	<div class='modal-dialog'>"
		+ "		<div class='modal-content'>"
		+ "			<div class='modal-header'>"
		+ "				<button type='button' class='close' id='_confirm_close_btn' data-dismiss='modal' aria-hidden='true'>&times;</button>"
		+ "				<h4 class='modal-title' id='_confirm_title'></h4>"
		+ "			</div>"
		+ "		<div class='panel-body row'>"
		+ "			<p class='blue' style='text-align:center; font-size:17px; margin:10px 0 0 0;' id='_confirm_content'></p>"
		+ "		</div>"
		+ "		<div class='modal-footer'>"
		+ "			<button type='button' class='btn btn-primary' data-dismiss='modal' id='_confirm_btn'>是</button>"
		+ "			<a type='button' id='_confirm_cancel_btn' class='btn btn-warning' data-dismiss='modal'>否</a> </div>"
		+ "		</div>"
		+ "	</div>"
		+ "</div>";
	
	$("body").append(common_confirm_div_str);
	
	
	var common_confirm_div_str_isnowCreate = ""
		+ '<div class="modal fade" id="_confirm_div_isnowCreate" tabindex="-1" style="z-index:9999" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
		+ "	<div class='modal-dialog'>"
		+ "		<div class='modal-content'>"
		+ "			<div class='modal-header'>"
		+ "				<button type='button' class='close' id='_confirm_close_btn_isnowCreate' data-dismiss='modal' aria-hidden='true'>&times;</button>"
		+ "				<h4 class='modal-title' id='_confirm_title'></h4>"
		+ "			</div>"
		+ "		<div class='panel-body row'>"
		+ "			<p class='blue' style='text-align:center; font-size:17px; margin:10px 0 0 0;' id='_confirm_content_isnowCreate'></p>"
		+ "		</div>"
		+ "		<div class='modal-footer'>"
		+ "			<button type='button' class='btn btn-primary' data-dismiss='modal' id='_confirm_btn_isnowCreate'>是</button>"
		+ "			<a type='button' id='_confirm_cancel_btn_isnowCreate' class='btn btn-warning' data-dismiss='modal'>否</a> </div>"
		+ "		</div>"
		+ "	</div>"
		+ "</div>";
	
	$("body").append(common_confirm_div_str_isnowCreate);
	
	var common_confirm_div_str2 = ""
		+ '<div class="modal fade" id="_confirm_div2" tabindex="-1" style="z-index:9999" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">'
		+ "	<div class='modal-dialog'>"
		+ "		<div class='modal-content'>"
		+ "			<div class='modal-header'>"
		+ "				<button type='button' class='close' id='_confirm_close_btn2' data-dismiss='modal' aria-hidden='true'>&times;</button>"
		+ "				<h4 class='modal-title' id='_confirm_title2'></h4>"
		+ "			</div>"
		+ "		<div class='panel-body row'>"
		+ "			<p class='blue' style='text-align:center; font-size:17px; margin:10px 0 0 0;' id='_confirm_content2'></p>"
		+ "		</div>"
		+ "		<div class='modal-footer'>"
		+ "			<button type='button' class='btn btn-primary' data-dismiss='modal' id='_confirm_btn2'>是</button>"
		+ "			<a type='button' id='_confirm_cancel_btn2' class='btn btn-warning' data-dismiss='modal'>否</a> "
		+ "         <a type='button' id='_confirm_quxiao_btn' class='btn btn-cancel' data-dismiss='modal'>取消</a> </div>"
		+ "		</div>"
		+ "	</div>"
		+ "</div>";
	
	$("body").append(common_confirm_div_str2);
	
	var common_confirm_div_str3 = ""
		+ '<div class="modal fade" id="_confirm_div3" tabindex="-1" style="z-index:9999" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">'
		+ "	<div class='modal-dialog'>"
		+ "		<div class='modal-content'>"
		+ "			<div class='modal-header'>"
		+ "				<button type='button' class='close' id='_confirm_close_btn3' data-dismiss='modal' aria-hidden='true'>&times;</button>"
		+ "				<h4 class='modal-title' id='_confirm_title3'></h4>"
		+ "			</div>"
		+ "		<div class='panel-body row'>"
		+ "			<p class='blue' style='text-align:center; font-size:17px; margin:10px 0 0 0;' id='_confirm_content3'></p>"
		+ "		</div>"
		+ "		<div class='modal-footer'>"
		+ "			<button type='button' class='btn btn-primary' data-dismiss='modal' id='_confirm_btn3'>确定</button>"
		+ "		</div>"
		+ "	</div>"
		+ "</div>";
	
	$("body").append(common_confirm_div_str3);
	
	
	
	/**
		new 消息提示相关
	 */
	//增加消息框按钮div
	$("body").append("<a href='' class='btn btn-success' style='display:none;' id='common_div_success_message'></a>");
	$("body").append("<a href='' class='btn btn-warning' style='display:none;' id='common_div_warning_message'></a>");
	$("body").append("<a href='' class='btn btn-danger' style='display:none;' id='common_div_error_message'></a>"); 
	jQuery('#common_div_success_message').hide();
	jQuery('#common_div_warning_message').hide();
//	jQuery('#common_div_warning_message1').hide();
	jQuery('#common_div_error_message').hide();
	jQuery('#common_div_confirm_message').hide();
	//增加消息框按钮点击事件
	$('#common_div_error_message').unbind("click");
	jQuery('#common_div_error_message').click(function(){
		jQuery.gritter.add({
			title: '失败消息通知!',
			text: _common_div_error_message_text,
			class_name: 'growl-danger',
			image: basePath+'/assets/img/screen.png',
			sticky: false,
			time: ''
		});
		return false;
	}); 
	$('#common_div_warning_message').unbind("click");
	jQuery('#common_div_warning_message').click(function(){
		jQuery.gritter.add({
			title: '警告消息通知!',
			text: _common_div_warning_message_text,
			class_name: 'growl-warning',
			image: basePath+'/assets/img/screen.png',
			sticky: false,
			time: ''
		});
		return false;
	});
//	$('#common_div_warning_message1').unbind("click");
//	console.log("305::");
//	jQuery('#common_div_warning_message').click(function(){
//		jQuery.gritter.add({
//			title: '警告消息通知!',
//			text: _common_div_warning_message_text,
//			class_name: 'growl-warning',
//			image: $("#basePath").val()+'/assets/img/screen.png',
//			sticky: false,
//			time: ''
//		});
//		return false;
//	});
	$('#common_div_success_message').unbind("click");
	jQuery('#common_div_success_message').click(function(){
		jQuery.gritter.add({
			title: '成功消息通知!',
			text: _common_div_success_message_text,
			class_name: 'growl-success',
			image: basePath+'/assets/img/screen.png',
			sticky: false,
			time: ''
		});
		return false;
	});
	/**
		new 消息提示相关  end
	*/
	
	
	
	
	/**
		锁屏modal
	 */
	var _common_div_screen_modal = ""
	+ "<div class='modal fade active' id='common_div_screen_modal' tabindex='-1'  style='z-index:19999' data-backdrop='static' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
	+ "	<div class='modal-dialog'>"
	+ "	<div class='modal-content'>"
	+ "	<!-- <div class='modal-header'></div>-->"
	+ "        <div class='modal-body'>"
	+ "            <div class='progress progress-striped active'>"
	+ "                <div class='progress-bar progress-bar-default active' role='progressbar' aria-valuenow='80' aria-valuemin='0' aria-valuemax='100' style='width: 100%'> <span class='sr-only'>)</span> </div>"
	+ "            </div>"
	+ "        </div>"
	+ "	<!-- <div class='modal-footer'></div>-->"
	+ "    </div>"
	+ "	</div>"
	+ "</div>";
	$("body").append(_common_div_screen_modal);
	
	
});


var commonLockNum = 1;
/**
 * 屏幕解锁
 */
function commonJsScreenUnLock() {
	//屏幕解锁 Screen Lock FX screen unlock  
	commonLockNum--;
	if(commonLockNum > 0){
	}else{
		$("#common_div_screen_modal").modal('hide');
	}  
};


/**
 * 屏幕锁定
 */
function commonJsScreenLock(loackNum) {
	//屏幕锁定  
	if(commonLockNum == null){ 
		commonLockNum = 1; 
	}else{
		commonLockNum = loackNum;
	}  
	$("#common_div_screen_modal").modal('show').css({
        "margin-top": "230px"
    });
};



/**
 * 显示错误消息提示框
 * @param errorMessage
 */
function showErrorDialog(errorMessage) {
//	$("#common_errorModalMessage_text").text(errorMessage);
//	$("#btn_common_errorMessageModal").click();
	
	_common_div_error_message_text = errorMessage;
	$("#common_div_error_message").click();
};



function showSuccessDialog(successMessage) {
	_common_div_success_message_text = successMessage;
	$("#common_div_success_message").click();
}; 
 
/**
 * 显示警告消息提示框
 * @param warningMessage
 */
function showWarningDialog(warningMessage) {
	_common_div_warning_message_text = warningMessage;
	$("#common_div_warning_message").click();
};

///**
// * 显示警告消息提示框
// * @param warningMessage
// */
//function showWarningDialog1(warningMessage) {
//	_common_div_warning_message_text = warningMessage;
//	$("#common_div_warning_message1").click();
//};




function showSuccessDialogWithFunc(successMessage, methodName) {
	$("#btn_common_successMessageModal_confirm").bind("click",methodName);
	$("#btn_common_successMessageModal_close").bind("click",methodName);
	$("#common_successModalMessage_text").text(successMessage);
	$("#btn_common_successMessageModal").click();
};

function showErrorDialogWithFunc(successMessage, methodName) {
	$("#btn_common_errorMessageModal_confirm").bind("click",methodName);
	$("#btn_common_errorMessageModal_close").bind("click",methodName);
	
	$("#common_errorModalMessage_text").text(successMessage);
	$("#btn_common_errorMessageModal").click();
};

function showLoginDiv(current_role){
	if(current_role==2){
		$("#_login_div").hide();
		$("#_login_title").empty();
		$("#_login_title").append("请选择角色类型");
		$("#_chooserole_div").show();
	}
	
	$("#login_popup_div").modal("show");
}

function showConfirmDiv(title,content, callback){
	$("#_confirm_title").empty();
	$("#_confirm_title").append(title);
	$("#_confirm_content").empty();
	$("#_confirm_content").append(content); 
	
	$("#_confirm_btn").unbind("click"); 
	$("#_confirm_btn").click(function(){
		callback(true);
		$("#_confirm_div").hide();
	});
	
	$("#_confirm_cancel_btn").unbind("click");  
	$("#_confirm_cancel_btn").click(function(){ 
		callback(false);
		$("#_confirm_div").hide();
	});
	
	$("#_confirm_close_btn").unbind("click");  
	$("#_confirm_close_btn").click(function(){
		callback(false);
		$("#_confirm_div").hide();
	}); 
	
	$("#_confirm_div").modal('show');
}

function showConfirmDivIsNow(title,content, callback){
	$("#_confirm_title_isnowCreate").empty();
	$("#_confirm_title_isnowCreate").append(title);
	$("#_confirm_content_isnowCreate").empty();
	$("#_confirm_content_isnowCreate").append(content); 
	
	$("#_confirm_btn_isnowCreate").unbind("click"); 
	$("#_confirm_btn_isnowCreate").click(function(){
		callback(true);
		$("#_confirm_div_isnowCreate").hide();
	});
	
	$("#_confirm_cancel_btn_isnowCreate").unbind("click");  
	$("#_confirm_cancel_btn_isnowCreate").click(function(){ 
		 $('#setTime').show().dialog({
		        title: '设置时间',
		        width: 400,
		        height: 200,
		        cache: false,
		        modal: true
		    });
		    $('#setTime').dialog('open');
	});
	
	$("#_confirm_close_btn_isnowCreate").unbind("click");  
	$("#_confirm_close_btn_isnowCreate").click(function(){
		callback(false);
		$("#_confirm_div_isnowCreate").hide();
		
	}); 
	
	$("#_confirm_div_isnowCreate").modal('show');
}


function showConfirmDiv2(title,content, callback){
	$("#_confirm_title2").empty();
	$("#_confirm_title2").append(title);
	$("#_confirm_content2").empty();
	$("#_confirm_content2").append(content); 
	
	$("#_confirm_btn2").unbind("click"); 
	$("#_confirm_btn2").click(function(){
		callback(true);
		$("#_confirm_div2").hide();
	});
	
	$("#_confirm_cancel_btn2").unbind("click");  
	$("#_confirm_cancel_btn2").click(function(){ 
		callback(false);
		$("#_confirm_div2").hide();
	});
	
	$("#_confirm_quxiao_btn").unbind("click");  
	$("#_confirm_quxiao_btn").click(function(){ 
		callback(false);
		$("#_confirm_div2").hide();
	});
	
	$("#_confirm_close_btn2").unbind("click");  
	$("#_confirm_close_btn2").click(function(){
		callback(false);
		$("#_confirm_div2").hide();
	}); 
	
	$("#_confirm_div2").modal('show');
}

function showConfirmDiv3(title,content, callback){
	$("#_confirm_title3").empty();
	$("#_confirm_title3").append(title);
	$("#_confirm_content3").empty();
	$("#_confirm_content3").append(content); 
	
	$("#_confirm_btn3").unbind("click"); 
	$("#_confirm_btn3").click(function(){
		callback(true);
		$("#_confirm_div3").hide();
	});
	$("#_confirm_div3").modal('show');
}

function _login(path){
	var _userName = $("#_user_name").val();
	var _password = $("#_password").val();
	var json = '{'
			 + '"user_name":' + '"' + _userName +'",'
			 + '"password":' + '"' + _password +'"'
			 + '}';
	if(_checkInput(_userName,_password)){
		$.ajax({
			url : path+'rest/login',
			type : 'post',
			data : json,
			async: false,
			success : function(result){
				if(result.success){
					switch(result.role_type){
						case -1: 
							$("#_chooserole_div").hide();
							$("#_login_div").show();
							showDivById("msg_div",result.message);
							break;
						case 0:
							top.location.reload();
							break;
						case 1: 
							top.location.reload();
							break;
						case 2: 
							$("#_login_div").hide();
							$("#_login_title").empty();
							$("#_login_title").append("请选择角色类型");
							$("#_chooserole_div").show();
							break;
					}
				}else{
					showDivById("_msg_div",result.message);
					return;
				}
			},
			error : function(error){
				showDivById("_msg_div","登录出错，请稍后重试...");
			}
		});
	}
}

function _checkInput(userName,password){
	if(isEmpty(userName) || isEmpty(password)){
		showDivById("_msg_div","请输入用户名和密码");
		return false;
	}
	$("#_msg_div").hide();
	return true;
}

function _chooseRoleType(type,path){
	$.ajax({
		url : path+'rest/login/'+type,
		type : 'get',
		success : function(result){
			if(result.success){
				top.location.reload();
			}else{
				$("#_chooserole_div").hide();
				$("#_login_div").show();
				$("#_user_name").val("");
				$("#_password").val("");
				showDivById("msg_div",result.message);
			}
		},
		error : function(error){
			showDivById("msg_div","登录出错，请稍后重试...");
		}
	});
}

function modelDialog(url, options) {
    this._default = {
        width: (window.screen.availWidth-10) * 0.8,
        height: (window.screen.availHeight-30) * 0.7,
        title: ""
    }

    this.width = options.width || this._default.width;
    this.height = options.height || this._default.height;
    this.title = options.title || this._default.title;

    var _dialog = ""
    + "<div class='modal fade' tabindex='-1' data-backdrop='static' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
    + "<div class='modal-dialog'>"
    + "<div class='modal-content' style='height: " + this.height + "px; width:" + this.width + "px'>"
    + "	       <div class='modal-header' style='padding: 5px 10px 5px 10px'>"
    + "            <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>"
    + "            <h4 class=\"modal-title\" id=\"myModalLabel\">" + this.title + "</h4>"
    + "        </div>"
    + "        <div class='modal-body'>"
    + "            <div>"
    + "                <iframe src='" + url + "' width='100%' height='100%' marginWidth=0 frameSpacing=0 marginHeight=0 scrolling=auto frameborder='0px'></iframe>"
    + "            </div>"
    + "        </div>"
    + "	<!-- <div class='modal-footer'></div>-->"
    + "    </div>"
    + "	</div>"
    + "</div>";
    $(_dialog).modal('show').css({
        'margin-top': (window.screen.availHeight-30-this.height)/2,
        'margin-left': -(window.screen.availWidth-10-this.width) * 2
    });
}

function dialog(page, options) {
    this._default = {
        autoOpen: false,
        height: $(window).height()/2,
        width: 800,
        title: "",
        position: [(window.screen.availWidth-10-this.width)/2,  (window.screen.availHeight-30-this.height)/2],
        maxWidth: $(window).width(),
        maxHeight: $(window).height(),
        model: true
    };

    var defaultOptions = {
        autoOpen: options.autoOpen || this._default.autoOpen,
        height: options.height || this._default.height,
        width: options.width || this._default.width,
        title: options.title || this._default.title,
        position: options.position || this._default.position,
        maxWidth: this._default.maxWidth,
        maxHeight: this._default.maxHeight,
        closeText: "关闭",
        model: options.model || this._default.model,
        close: options.close || null
    };
    var $dialog = $('<div class="dialog" style="overflow: hidden; z-index: 999"></div>')
        .html('<iframe src="' + page + '" width="100%" height="100%" marginWidth=0 frameSpacing=0 marginHeight=0 scrolling=auto frameborder="0px"></iframe>')
        .dialog(defaultOptions);
    return $dialog;
};