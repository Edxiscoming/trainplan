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
   	    {field:'cmPartOriginalCrossId',hidden:true},
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
   		        if(!value){
   		        	html = '<div style="background:#ef6b00;border-radius:10px; width:30px;text-align: center;margin: 0px auto;color: #FFFFFF;">未</div>';
   		        }else{
   		        	html= '<div style="background:#48a048;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">已</div>';
   		        }
   		        return html;
   		    }
   		},
   		{field:'useStatus',title:'生成状态',width:10,align:"center",sortable:true,
   			formatter:function(value,row,index) {
   		        var html = "";
   		        if(!value){
   		        	html = '<div style="background:#ef6b00;border-radius:10px; width:30px;text-align: center;margin: 0px auto;color: #FFFFFF;">未</div>';
   		        }else{
   		        	html= '<div style="background:#48a048;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">已</div>';
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
   	var url = basePath + "/partOriginalCross/pageQuery";
   	var onClickRow= function(cmPartOriginalCrossId){ //点击行显示详情，
   		showBlock(cmPartOriginalCrossId);
   	}
   	crossManageData.$table = creatGrid($("#mcrossTable"), columns, url, paras, onClickRow);
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
            $("#plan_construction_input_trainNbr").val(data.crossName);
            $("#undertake").val(data.tokenVehBureau);
            $("#groupData").val(data.groupTotalNbr);
            $("#openState").val(data.spareFlag);
            $("#mold").val(data.locoType);
            $("#relaceDate").text(data.alternateDate);
            $("#replace_train_number").text(data.alternateTrainNbr);
            $("#normalRule").val(data.commonlineRule);
            $("#week").val(data.appointWeek);
            $("#date").val(data.appointDay);
            $("#cycle").val(data.appointPeriod);
            $("#runSection").val(data.crossSection);
            $("#runDistance").val(data.runRange);
            $("#level").val(data.crossLevel);
            $("#log").val(data.pairNbr);
            $("#orgnize").val(data.marshallingNums);
            $("#createPeople").val(data.peopleNums);
            $("#orgnizeContent").val(data.marshallingContent);
            $("#groupLevel").val(data.crhType);
            $("#electricity").val(data.elecSupply);
            $("#mass").val(data.dejCollect);
            $("#airconditioner").val(data.airCondition);
            $("#remark").val(data.note);
        },
        error: function () {
        	openStatus("获取信息失败！");
        },
    });
}


/**
 * 新增保存
 */
function submitData(){
	var data1=$("#newCross").serialize();
	var fdata=JSON.stringify(data1).replace(/&/g,'","' ).replace(/=/g,'":"');
	fdata="{"+fdata+"}";
	var fdObj = $.parseJSON(fdata);
	console.log(fdata);
	fdObj.cmVersionId = $("#cmVersionId_dialog").val();
	$.ajax(
	{
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/partOriginalCross/addCross",
		data: JSON.stringify({result : fdObj}),
		success:function(data){
			openStatus(data);
			crossManageData.$table.datagrid("reload");
		}
		
	});
}
/**
 * 修改保存
 */

function changeData(){
	var data2=$("#update").serialize();
	var fdata=JSON.stringify(data2).replace(/&/g,'","' ).replace(/=/g,'":"');
	fdata="{"+fdata+"}";
	console.log(fdata);
	$.ajax(
	{
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/partOriginalCross/updateCross",
		data: JSON.stringify({result : fdata}),
		success:function(data){
			openStatus(data);
			crossManageData.$table.datagrid("reload");
		}
		
	});
}

function deleteCross(){
	//获取选中行
	var rows = crossManageData.$table.datagrid("getSelections");
	var toDelete="";

	for(var i = 0 ; i < rows.length; i++)
	{
		toDelete+=(","+rows[i].cmPartOriginalCrossId);
	}

	toDelete.substring(1);

	if(!toDelete){ //未选中直接返回。
		openStatus("请至少选择一条数据进行删除");
		return;
	}

	$.ajax(
	{
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/partOriginalCross/deleteCross",
		data: JSON.stringify({crossIds : toDelete}),
				success:function(data){
					openStatus(data.message);
					crossManageData.$table.datagrid("reload");
				}
				
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
