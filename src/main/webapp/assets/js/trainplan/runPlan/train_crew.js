/**
 * 查看列车乘务信息
 * @author Denglj
*/



var TrainCrewPage = function () {
	/**
	 * 页面加载时执行
	 * public
	 */
	this.initPage = function () {
		//页面元素绑定
		var pageModel = new PageModel();
		ko.applyBindings(pageModel);
		
		pageModel.initPageData();
	};
	
	
	/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
									
		_self.trainCrewRows = ko.observableArray();	//列车经由站列表

		
		
		/**
		 * 初始化
		 */
		_self.initPageData = function() {
			commonJsScreenLock();
			$.ajax({
				url : basePath+"/crew/highline/getHighlineCrewForCrewDateAndTrainNbr",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({
					trainNbr: $("#trainCrew_trainNbr_hidden").val(),
					crewDate: moment($("#trainCrew_runDate_hidden").val()).format("YYYYMMDD")
				}),
				success : function(result) {
					if (result != null && result != "undefind" && result.code == "0") {
					
						$.each(result.data, function(i, obj){
							//乘务类型（1车长、2司机、3机械师）
							if (obj.crewType == "1") {
								obj.crewTypeName = "车长";
							} else if (obj.crewType == "2") {
								obj.crewTypeName = "司机";
							} else if (obj.crewType == "3") {
								obj.crewTypeName = "机械师";
							} else {
								obj.crewTypeName = "";
							}
							
							_self.trainCrewRows.push(obj); 
						});
						
			            // 表头固定
						
			            $("#table_run_plan_train_crew_edit").freezeHeader();
					} else {
						showErrorDialog("接口调用返回错误，code="+result.code+"   message:"+result.message);
					} 
				},
				error : function() {
					showErrorDialog("接口调用失败");
				},
				complete : function(){ 
					commonJsScreenUnLock();  
				}
		    });
		};
		
		
		
		
	};
	
	
};






$(function() {
	new TrainCrewPage().initPage();
});
