$(function() { 

	var trainTimed = new TrainTimes();
	ko.applyBindings(trainTimed);  

	trainTimed.init();   
});

//var requestTimes = ${times};
var highlingFlags = [{"value": "0", "text": "普速"},{"value": "1", "text": "高铁"},{"value": "2", "text": "混合"}];
var checkFlags = [{"value": "1", "text": "已"},{"value": "0", "text": "未"}];
var unitCreateFlags = [{"value": "1", "text": "已"},{"value": "0", "text": "未"}];
var highlingrules = [{"value": "1", "text": "平日"},{"value": "2", "text": "周末"},{"value": "3", "text": "高峰"}];
var commonlinerules = [{"value": "1", "text": "每日"},{"value": "2", "text": "隔日"}];
var periodArray = [{"code": "1", "text": "20140122-------20140822"},{"code": "2", "text": "20140822-------20990122"}];


var gloabBureaus = [];

var _cross_role_key_pre = "JHPT.KYJH.JHBZ.";

function TrainTimes(){
	var self = this;
	self.times = ko.observableArray();
	self.simpleTimes = ko.observableArray();
	self.init = function(){  
		var trainId = $("#trainId").val();
		$.ajax({
			url : "queryPlanLineTrainTimesDepands?trainId="+trainId,
			cache : false,
			type : "GET",
			success : function(result) {  
				if (result != null && result != "undefind" && result.code == "0") {  
					$.each(result.data,function(i, n){
						self.times.push(new TrainTimeRow(n));
						if(n.stationFlag != 'BTZ'){
							self.simpleTimes.push(new TrainTimeRow(n));
						}
					});
				}
				
				//是否出现右边17
				var tableH = $("#simpleTimes_table table").height();
				var boxH = $("#simpleTimes_table").height();
				if(tableH <= boxH){
					$("#simpleTimes_table .td_17").removeClass("display");
				}else{
					$("#simpleTimes_table .td_17").addClass("display");
				}
				
				

				//是否出现右边17
				var tableH1 = $("#allTimes_table table").height();
				var boxH1 = $("#allTimes_table").height();
				if(tableH1 <= boxH1){
					$("#allTimes_table .td_17").removeClass("display");
				}else{
					$("#allTimes_table .td_17").addClass("display");
				}
				
			},
			error : function() {
				 
			},
			complete : function(){ 
			}
		});
		
		$(".window-shadow").attr("style","display: block; left: 0px; top: 13.5px; z-index: 9000; width: 700px; height: 1px;");
	}
	
}



function TrainTimeRow(data) { 
	var self = this; 
	self.index = data.childIndex + 1;
	self.stnName = data.stnName;
	self.nodeName = data.nodeName;
	self.bureauShortName = data.bureauShortName;
	// 0101 01:01
	self.sourceTime = data.stationFlag=='SFZ'?'--':moment(data.arrTime).format('MMDD HH:mm');
	self.targetTime = data.stationFlag=='ZDZ'?'--':moment(data.dptTime).format('MMDD HH:mm');
	// 01:01
	self.sourceTime_hm = data.stationFlag=='SFZ'?'--':moment(data.arrTime).format('HH:mm');
	self.targetTime_hm = data.stationFlag=='ZDZ'?'--':moment(data.dptTime).format('HH:mm');
	
	self.stepStr = GetDateDiff(data); 
	self.trackName = data.trackName=='null'?"":data.trackName;  
	self.runDays = data.stationFlag=='ZDZ'?'--':data.runDays;
	self.sourceDay = data.stationFlag=='SFZ'?'--':data.arrRunDays;
	
	self.stationFlag = data.stationFlag;
	self.kyyy = (data.stationFlag=="SFZ" || data.stationFlag=="ZDZ" || data.jobs.indexOf('客运营业')>0)?'是':'--';
	var jobsText = data.jobs;
	jobsText = jobsText.replace('<客运营业>','').replace('<经由>','');
	jobsText = jobsText.substring(1,jobsText.length-1);
	jobsText = jobsText.split("><").join(",");
	self.jobsText = jobsText;
	
	self.platForm = data.platForm;
	self.sourceTrainNbr = data.arrTrainNbr;
	self.targetTrainNbr = data.dptTrainNbr;
	 
}; 
function GetDateDiff(data)
{ 
	 if(data.childIndex == 0 
			 || data.dptTime == '-' 
			 || data.dptTime == null 
			 || data.arrTime == null){
		return "";
	} 
	var startTime = new Date(data.arrTime);
	if(data.arrRunDays != null){
//		startTime.setDate(startTime.getDate() + data.arrRunDays);//计算时间差时，本来就有时间日期
		startTime.setDate(startTime.getDate());
	}  
	var endTime = new Date(data.dptTime);   
	if(data.targetDay != null){
//		endTime.setDate(endTime.getDate() + data.runDays);
		endTime.setDate(endTime.getDate());
	} 
	 
	var result = "";
	
	var date3=endTime.getTime()-startTime.getTime(); //时间差的毫秒数 
	
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000));
	
	result += days > 0 ? days + "天" : "";  
	//计算出小时数
	var leave1=date3%(24*3600*1000);     //计算天数后剩余的毫秒数
	var hours=Math.floor(leave1/(3600*1000));
	
	result += hours > 0 ? hours + "小时" : ""; 
	
	//计算相差分钟数
	var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
	var minutes=Math.floor(leave2/(60*1000));
	
	result += minutes > 0 ? minutes + "分" : "";
	//计算相差秒数
	var leave3=leave2%(60*1000);          //计算分钟数后剩余的毫秒数
	var seconds=Math.round(leave3/1000);
	
	result += seconds > 0 ? seconds + "秒" : "";  
	 
	return result == "" ? "" : result; 
};
function openLogin() {
	$("#file_upload_dlg").dialog("open");
}


var isCheckBox = false;
function showTimes(){
	if(isCheckBox){
		// 简点
		$("#simpleTimes_table").css('display','block','height','530px','overflow-y','auto');
		$("#simpleTimes_table_none").css('display','none','height','530px','overflow-y','auto');
		// 详点
		$("#allTimes_table").css('display','block','height','530px','overflow-y','auto');
		$("#allTimes_table_none").css('display','none','height','530px','overflow-y','auto');
		isCheckBox = false;
	}else{
		
		$("#simpleTimes_table").css('display','none','height','530px','overflow-y','auto');
		$("#simpleTimes_table_none").css('display','block','height','530px','overflow-y','auto');
		
		$("#allTimes_table").css('display','none','height','530px','overflow-y','auto');
		$("#allTimes_table_none").css('display','block','height','530px','overflow-y','auto');
		isCheckBox = true;
	}
	
}
 