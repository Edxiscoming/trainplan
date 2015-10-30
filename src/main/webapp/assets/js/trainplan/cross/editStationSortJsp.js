var cross = null;
var stnSortOldValue = 0;
$(function() { 
	cross = new CrossModel();
	ko.applyBindings(cross); 
	
	cross.init();   
});





/**
 * 实际暴露给用户的model对象,knockout的最外层
 */
function CrossModel() {
	var _self = this;
	_self.stations = ko.observableArray();
			
	
	_self.init = function(){
		commonJsScreenLock(4); 
		//获取当期系统日期  
		var drawGraphId = $("#drawGraphId").val();
	    $.ajax({
	    	
			url : basePath+"/cross/getCrossDicStnInfo?drawGraphId="+drawGraphId,
			cache : false,
			type : "GET",
			dataType : "json",
			contentType : "application/json", 
			success : function(result) {    
				if (result != null && result != "undefind" && result.code == "0") { 
					if (result.data != null && result.data != undefined) {
						for (var i = 0; i < result.data.length; i++) {
							_self.stations.push(new Station(result.data[i]));
						}
					}
				}
			},
			error : function() {
				showErrorDialog("获取字典信息失败");
			},
			complete : function(){ 
				commonJsScreenUnLock(); 
			}
	    });
	    
		
	};

	/**
	 * 保存按钮
	 */
	_self.saveDicStationSort = function() {
		var stationList = _self.stations();
		for(var i = 0; i<stationList.length-1; i++){
			for(var j = i+1; j<stationList.length; j++){
				if(stationList[i].stnSort==stationList[j].stnSort){
//					alert("输入的站序，有重复，请检查！ 提示：重复数字为 "+stationList[i].stnSort);
					showWarningDialog("输入的站序，有重复; 提示：重复数字为 "+stationList[i].stnSort);
					return;
				}
			}
		}
		
		commonJsScreenLock();
		$.ajax({
			url : basePath+"/cross/saveDicStationSort",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				stationList : ko.toJSON(stationList)
			}),
			success : function(result) {
				if (result != null && typeof result == "object" && result.code == "0") {
					showSuccessDialog("保存成功");
					commonJsScreenUnLock(1);
				} else {
					showErrorDialog("保存失败");
				};
			},
			error : function() {
				showErrorDialog("保存失败");
			},
			complete : function(){
				commonJsScreenUnLock(1);
			}
		});
	};
	
//	_self.btnCancel = function(){
//		$("#add_disRelaCross_dialog").dialog("close");
//	};
	
}



function Station(data){
	var self = this; 
	self.drawGraphStnId = data.drawGraphStnId;
	self.stnName = data.stnName;
	self.stnSort = data.stnSort;
	self.stnSortBak = data.stnSort;
	self.heightDetail = data.heightDetail;
	self.isChangeed = ko.observable("0");
	
//	self.stnSortInput = function(){
//		return "stnSortInput"
//	};
	self.stnSortOld = function(){
		if(!isNaN(self.stnSort)){
			stnSortOldValue = self.stnSort;
		}
	};
	self.sortChange = function(){
		var stnSortNewValue = self.stnSort;
		var stnSortBak_ = self.stnSortBak;
		if(isNaN(stnSortNewValue)){
//			alert("请输入数字！")
			showWarningDialog("请输入数字！");
			$("#stnSortInput"+stnSortBak_).val(stnSortOldValue);
			return;
		}		
		self.isChangeed("1");
		//暂时隐藏间距高度；
		self.heightDetail = (Number(stnSortNewValue)+1)*100;
		$("#heightDetailInput"+stnSortBak_).val((Number(stnSortNewValue)+1)*100);
		
	};
	self.heightDetailChange = function(){
		self.isChangeed("1");
	};
}

function filterValue(value){
	return value == null || value == "null" ? "--" : value;
};


