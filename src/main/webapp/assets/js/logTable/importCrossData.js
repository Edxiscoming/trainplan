/**
 * Created by Administrator on 2015/9/25.
 */
/**
 * 创建一个"导入对数表页面的"datagrid表
 * @param targetTable 目标table jquery对象
 * @param data 表数据
 * @param columns 表列定义
 * @param onselect 选中行事件
 * @returns {*}
 */

var crossManageData={}; //我的全局变量
$(function(){
	getPlan();
})

//初始化table
function initTable(){
	var columns =[[
	               {field:'cmPartOriginalCrossId',hidden:true},
	               {field:'cmVersionId',title:'序号',width:5,align:"center",
	                   formatter:function(value,row,index) {
	                       return index+1;
	                   }
	               },
	               {field:'crossName',title:'车底交路',width:25,align:"center",sortable:true},
	               {field:'alternateDate',title:'交替日期',width:15,align:"center",sortable:true},
	               {field:'spareFlag',title:'开行状态',width:15,align:"center",sortable:true},
	               {field:'groupTotalNbr',title:'组数',width:10,align:"center",sortable:true},
	               {field:'pairNbr',title:'对数',width:10,align:"center",sortable:true},
	               {field:'tokenVehBureau',title:'担当局',width:10,align:"center",sortable:true,
	                   /*formatter:function(value,row,index) {
	                       return "济";
	                   }*/
	               },
	               {field:'dejCollect',title:'数据异常',width:10,align:"center",sortable:true,
	                   formatter:function(value,row,index) {
	                       var html = "";
	                       if(!value){
	                           html= '<div style="background:#48a048;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">无</div>';
	                       }else{
	                           html = '<div style="background:#ef6b00;border-radius:10px; width:30px;text-align: center;margin: 0px auto;color: #FFFFFF;">有</div>'
	                       }
	                       return html;
	                   }
	               },
	                          
	               ]];
			//查询条件
		   	var cmVersionId = $("#cmVersionId").val();
		   	var highlineFlag = $("#trainType").val();
		   	//车次
			var trainNo = $("#trainNo").val();
			//开行状态
			var runState = $("#runState").val();
		   	var paras = {
		   			cmVersionId : cmVersionId,
		   			highlineFlag:highlineFlag,
		   			trainNbr:trainNo,
					spareFlag:runState
		   	};
	       	var url = basePath + "/partOriginalCross/pageQuery";
//	       	var onselect = function(crossId){
//	       		showBlock(crossId);
//	       	}
	       	crossManageData.$table = creatGrid($("#mcrossTable"), columns, url, paras, showBlock);
}

/**
 * 查询
 */
function searchData(){
	//方案
	var cmVersionId = $("#cmVersionId").val();
	//类型
	var highlineFlag = $("#trainType").val();
	//车次
	var trainNo = $("#trainNo").val();
	//开行状态
	var runState = $("#runState").val();
	
	var paras = {
			cmVersionId : cmVersionId,
			highlineFlag : highlineFlag,
			trainNbr:trainNo,
			spareFlag:runState
	};
	crossManageData.$table.datagrid("load",paras);
}

/**
 * 获取方案
 */
function getPlan(){
	$.ajax({
		type: "GET",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/partOriginalCross/queryVersionInfo",
		success:function(data){
			var option="";
			var $cmVersionId=$("#cmVersionId");
			var $cmVersionId_dialog=$("#cmVersionId_dialog");
			$cmVersionId.empty();
			$cmVersionId_dialog.empty();
			for(var i=0; i<data.list.length;i++){
				option += '<option value="'+data.list[i].cmVersionId+'">'+data.list[i].name+'</option>';
			}
			$cmVersionId.append(option);
			$cmVersionId_dialog.append(option);
			initTable();
		}
		
	});
}
 

function uploadFile(){
	if($("#fileToUpload").val() == null || $("#fileToUpload").val() == ""){
    	alert("请选择文件！");
    	return;
    }
	$("#loading").show();
	$("#upload_btn").attr("disabled", "disabled");
	$.ajaxFileUpload
    ({
        url : basePath + '/cmOriginalCross/upload',
        secureuri:false,
        fileElementId:'fileToUpload',
        type : "POST",
        dataType: 'json',  
        data:{
        	versionName : $("#versionSel").find("option:selected").text(),
        	versionId : $("#cmVersionId_dialog").val()
        },
        success: function (data, status)
        {  
        	debugger;
        	$("#loading").hide();
    		$("#upload_btn").removeAttr("disabled");
        },
        error: function(result){
        	alert("上传失败");
        	$("#loading").hide();
        	$("#btn_fileToUpload").removeAttr("disabled");
        }
    }); 
}


function closeSlideBlock(){ //点击右侧的按钮收起div
    $(".slideblock").hide("300");
    $(".left").css({width:"98%"});
    crossManageData.is_on=false;
    crossManageData.$table.datagrid('resize',{"width":"100%"});
}
/**
 * 获取详情
 * @param id
 */

function showBlock(cmPartOriginalCrossId){
    if(crossManageData.is_on&&crossManageData.currentcmPartOriginalCrossId===cmPartOriginalCrossId){
        return;  //连续点击同一行，不重复发送请求
    }
    crossManageData.currentcmPartOriginalCrossId=cmPartOriginalCrossId;
    var slideblock=$(".slideblock");
    var left=$(".left");
    if(!crossManageData.is_on){
        //打开并显示当前行数据
        slideblock.show("300");
        left.css({width:"75%"});
        crossManageData.is_on=true;
    }
    showDetail(cmPartOriginalCrossId);
    crossManageData.$table.datagrid('resize',{"width":"100%"});
}

function showDetail (cmPartOriginalCrossId) {
    
    $.ajax({
        type: "post",
        url: basePath + "/partOriginalCross/queryCrossById",
        data: JSON.stringify({crossId : cmPartOriginalCrossId}),
        dataType: "json",
        contentType : "application/json",
        success: function (json) {
            var data = json.obj;
            $("#plan_construction_input_trainNbr").text(data.crossName);
            $("#undertake").text(data.tokenVehBureau);
            $("#groupData").text(data.groupTotalNbr);
            $("#openState").text(data.spareFlag);
            $("#mold").text(data.locoType);
            $("#relaceDate").text(data.alternateDate);
            $("#replace_train_number").text(data.alternateTrainNbr);
            $("#normalRule").text(data.commonlineRule);
            $("#week").text(data.appointWeek);
            $("#date").text(data.appointDay);
            $("#cycle").text(data.appointPeriod);
            $("#runSection").text(data.crossSection);
            $("#runDistance").text(data.runRange);
            $("#level").text(data.crossLevel);
            $("#log").text(data.pairNbr);
            $("#orgnize").text(data.marshallingNums);
            $("#createPeople").text(data.peopleNums);
            $("#orgnizeContent").text(data.marshallingContent);
            $("#groupLevel").text(data.crhType);
            $("#electricity").text(data.elecSupply);
            $("#mass").text(data.dejCollect);
            $("#airconditioner").text(data.airCondition);
            $("#remark").text(data.note);
        },
        error: function () {
            openStatus("获取信息失败！");
        },
    });
}
