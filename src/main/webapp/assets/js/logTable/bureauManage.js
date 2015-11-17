/**
 * Created by Administrator on 2015/9/24.
 */

//var $table ={}; //datagrid对象
var crossManageData={}; //我的全局变量
$(function(){
	getPlan();
})
 
//初始化table
function initTable(){
	var columns =[[
	       	    {field:"ck",checkbox:true,width:5,align:"center"},
	       	    {field:'cmOriginalCrossId',hidden:true},
	       		{field:'cmVersionId',title:'序号',width:5,align:"center",
	       		    formatter:function(value,row,index) {
	       		        return index+1;
	       		    }
	       		},
	       		{field:'crossName',title:'车底交路',width:10,align:"center",sortable:true},
	       		{field:'alternateDate',title:'交替日期',width:10,align:"center",sortable:true},
	       		{field:'spareFlag',title:'开行状态',width:10,align:"center",sortable:true},
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
	       		{field:'checkFlag',title:'审核状态',width:10,align:"center",sortable:true,
	       			formatter:function(value,row,index) {
	       		        var html = "";
	       		        if(value){
	       		            html= '<div style="background:#48a048;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">已</div>';
	       		        }else{
	       		            html = '<div style="background:#ef6b00;border-radius:10px; width:30px;text-align: center;margin: 0px auto;color: #FFFFFF;">未</div>'
	       		        }
	       		        return html;
	       		    }
	       		},
	       		{field:'useStatus',title:'生成状态',width:10,align:"center",sortable:true,
	       			formatter:function(value,row,index) {
	       		        var html = "";
	       		        if(value){
	       		            html= '<div style="background:#48a048;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">已</div>';
	       		        }else{
	       		            html = '<div style="background:#ef6b00;border-radius:10px; width:30px;text-align: center;margin: 0px auto;color: #FFFFFF;">未</div>'
	       		        }
	       		        return html;
	       		    }
	       		},
	       	               
	       	    ]];
			//查询条件
			var cmVersionId = $("#cmVersionId").val();
			var highlineFlag = $("#trainType").val();
			var paras = {
					cmVersionId : cmVersionId,
					highlineFlag:highlineFlag
			};
	       	var url = basePath + "/cmOriginalCross/pageQuery";
	       	var onClickRow= function(obj){ //点击行显示详情，
	       		showBlock(obj.cmOriginalCrossId);
	       	}
	       	crossManageData.$table = creatGrid($("#mcrossTable"), columns, url, paras, onClickRow);
}

/**
 * 获取方案
 */
function getPlan(){
	$.ajax({
		type: "GET",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/cmOriginalCross/queryVersionInfo",
		success:function(data){
			var option="";
			var $cmVersionId=$("#cmVersionId");
			$cmVersionId.empty();
			for(var i=0; i<data.list.length;i++){
				option += '<option value="'+data.list[i].cmVersionId+'">'+data.list[i].name+'</option>';
			}
			$cmVersionId.append(option);
			initTable();
		}
		
	});
}
/**
 * 查询
 */
function searchData(){
	//方案
	var cmVersionId = $("#cmVersionId").val();
	//类型
	var highlineFlag = $("#trainType").val();
	var paras = {
			cmVersionId : cmVersionId,
			highlineFlag : highlineFlag,
	};
	crossManageData.$table.datagrid("load",paras);
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

function showBlock(cmOriginalCrossId){
	if(crossManageData.is_on&&crossManageData.currentcmOriginalCrossId===cmOriginalCrossId){
		return;  //连续点击同一行，不重复发送请求
	}
	crossManageData.currentcmOriginalCrossId=cmOriginalCrossId;
	var slideblock=$(".slideblock");
	var left=$(".left");
	if(!crossManageData.is_on){
		//打开并显示当前行数据
		slideblock.show("300");
		left.css({width:"75%"});
		crossManageData.is_on=true;
	}
	showDetail(cmOriginalCrossId);
	crossManageData.$table.datagrid('resize',{"width":"100%"});
}

function showDetail (cmOriginalCrossId) {
    
    $.ajax({
        type: "post",
        url: basePath + "/cmOriginalCross/queryCrossById",
        data: JSON.stringify({crossId : cmOriginalCrossId}),
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
            debugger;
        },
        error: function () {
            openStatus("获取信息失败！");
        },
    });
    
}





function openStatus(data){
    $('#returnedMsg').dialog({
        title: '状态报告',
        content:data,
        width: 200,
        height: 100,
        cache: false,
        modal: true
    });
    $('#returnedMsg').dialog('open');
}


/**
 * 审核
 */
function checkData(){	      
		var crossIds = "";
		var updateCrosses = [];
		var crosses = crossManageData.$table.datagrid('getSelections')
		for(var i = 0; i < crosses.length; i++){
			if(crosses[i].checkFlag == 1 ){  
				alert("不能重复审核"); 
				return;
			}else if(crosses[i].checkFlag == 0){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].cmOriginalCrossId;
				updateCrosses.push(crosses[i]); 
			}; 
		}  
		if(crossIds == ""){
			alert("没有可审核的");
			return;
		}
		 $.ajax({
				url : basePath+"/cmOriginalCross/checkCross",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossIds : crossIds
				}),
				success : function(result) {     
					if(result.code == 0){
						$.each(updateCrosses, function(i, n){ 
							n.checkFlag = 1;
						});
						alert("审核成功");
						crossManageData.$table.datagrid('reload');
					}else{
						}
					
					},
				error : function() {
					alert("审核失败");
				},
				complete : function(){
					
				}
			}); 
			
}
