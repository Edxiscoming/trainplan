
var cross = null;
$(function() { 
	cross = new CrossModel();
	ko.applyBindings(cross); 
});
  

/**
 * 实际暴露给用户的model对象,knockout的最外层
 */
function CrossModel() {
	var self = this;
	self.throughLineLineId = ko.observable();
	self.throughLineName = ko.observable();
	self.bureau = ko.observable();
	self.note = ko.observable();
	
	//全选按钮
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.highLineCrossRows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0); 
			}else{ 
				crossRow.selected(1);   
			}  
		});  
	};
	

	self.setCurrentTrain = function(train){ 
		self.currentTrain(train); 
	};
	
	self.setCurrentCross = function(cross){ 
		self.currentCross(cross); 
	};
	
	
	
	//全选按钮
	self.selectCrosses = function(){
//		self.crossAllcheckBox(); 
		$.each(self.highLineCrossRows(), function(i, crossRow){ 
			if(self.crossAllcheckBox() == 1){
				crossRow.selected(0); 
			}else{ 
				crossRow.selected(1);   
			}  
		});  
	};
	//数据行前面的checkbox点击事件
	self.selectCross = function(row){ 
//		self.crossAllcheckBox();  
		if(row.selected() == 0){  
			self.crossAllcheckBox(1);   
			$.each(self.highLineCrossRows(), function(i, crossRow){  
				//如果可操作并且该记录被选中，表示没有全部选中
				if(crossRow.selected() != 1 && crossRow != row){
					self.crossAllcheckBox(0);
					return false;
				}  
			}); 
		}else{ 
			self.crossAllcheckBox(0); 
		} 
	}; 
	
	
	
	/**
	 * 保存按钮
	 */
	self.btnSaveDicThroughLine = function() {
		var throughLineName = self.throughLineName();
		commonJsScreenLock();
		$.ajax({
			url : basePath+"/highLine/saveDicThroughLine",
			cache : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			data :JSON.stringify({
				throughLineName : throughLineName//铁路线
			}),
			success : function(result) {
				
				if (result != null && typeof result == "object" && result.code == "0") {
					showSuccessDialog("保存成功");
					window.parent.cross.loadCrosseForPage();
					window.parent.parent.highLine.searchThroughLineName();
					window.parent.parent.parent.cross.searchThroughLineName();
					
					commonJsScreenUnLock(1);
				}else if(result != null && typeof result == "object" && result.code == "100"){
					showErrorDialog("铁路线已经存在！");
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
	
	self.btnCancel = function(){
		$('#add_disRelaCross_dialog').hide();
	};
	
}



