var pageModel ;
var TrainRunTimePage = function () {
	
	this.initPage = function () {
		//页面元素绑定
		pageModel = new PageModel();
		ko.applyBindings(pageModel);
		pageModel.getFullNodeList();
	
	};
/**
	 * 页面元素绑定对象
	 * 
	 * private
	 */
	function PageModel() {
		var _self = this;
		 _self.stnNameTemps = ko.observableArray();	//站名
        _self.getFullNodeList = function(){
    	    $.ajax({
    			url : basePath+"/getFullNodeInfo",
    			cache : false,
    			type : "GET",
    			dataType : "json",
    			contentType : "application/json", 
    			success : function(result) {   
    				if (result != null && result != "undefind" && result.code == "0") { 
    					if(result.data!=null && result.data!=undefined){
    						for(var i=0;i<result.data.length;i++){
    							var obj = result.data[i];
    							if(obj.name!=null && obj.name!=undefined){
    								_self.stnNameTemps.push(obj);
    							}
    						}

    					}
    				} else {
    					showErrorDialog("获取站名失败");
    				} 
    			},
    			error : function() {
    				showErrorDialog("获取站名失败");
    			},
    			complete : function(){ 
    				commonJsScreenUnLock(); 
    			}
    	    });
        	
        };
        
        


			self.stnNameTempOnfocus = function(n, event){
				$(event.target).autocomplete(pageModel.stnNameTemps(),{
					max: 50,    	//列表里的条目数
					width: 200,     //提示的宽度，溢出隐藏
					scrollHeight: 500,   	//提示的高度，溢出显示滚动条
					matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
					autoFill: false ,   	//是否自动填充
					formatItem: function(obj) {
						
						return obj.pinyinInitials+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+obj.name+'['+obj.bureauShortName+']';
					},
					formatResult: function(obj) {
						return obj;
					}
		       }).result(function(event, stnNameTemp, formatted) {
		    	   if(formatted!=null && formatted!=undefined){
		    		   var str = new String(formatted);
		    		   var name = str.substring(str.lastIndexOf(";")+1, str.indexOf("["));
		    		   var bureauShortName = str.substring(str.indexOf("[")+1, str.indexOf("]"));
		    		   self.stnName(name);
         	    	 
		    	   }
		    	   
		    	   
		       });
			};

		};

};
$(function() {
	new TrainRunTimePage().initPage();
});

