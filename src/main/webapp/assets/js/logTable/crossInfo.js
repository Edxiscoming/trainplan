var crossManageData={}; //我的全局变量
$(function(){
	getPlan();
	   	//点击交路车次列表
//	   	$("#cmCrossId_unit").datagrid({
//	   		onClickRow:function(index, row){
//	   			$("#cmUnitCrossId").val(row.trainNbr);
//	   			timeOpen(row.baseTrainId);
//	   		}
//	   	});
	   	//点击物理交路车次列表
//	   	$("#cmCrossId_cross").datagrid({
//	   		onClickRow:function(index, row){
//	   			$("#cmUnitCrossId").val(row.baseTrainId);
//	   			timeOpen(row.baseTrainId);
//	   		}
//	   	});
	   	//点击tab加载列表
	   	$("#crossInfoTab").on('shown.bs.tab', function(){
	   		crossManageData.$unitTable.datagrid('resize',{"width":"100%"});
	   	});
	   	$("#showcrosstrainTab").on('shown.bs.tab', function(){
	   		crossManageData.$crossTable.datagrid('resize',{"width":"100%"});
	   	});
	   	$("#jianTab").on('shown.bs.tab', function(){
	   		crossManageData.$jianTable.datagrid('resize',{"width":"100%"});
	   	});
	   	$("#xiangTab").on('shown.bs.tab', function(){
	   		crossManageData.$xiangTable.datagrid('resize',{"width":"100%"});
	   	});
});
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
			$cmVersionId.empty();
			for(var i=0; i<data.list.length;i++){
				option += '<option value="'+data.list[i].cmVersionId+'">'+data.list[i].name+'</option>';
			}
			$cmVersionId.append(option);
			initTable();
		}
		
	});
}
/*
 * 初始化页面主列表
 * 
 */
function initTable(){
	var columns =[[
	         	   /* {field:"ck",checkbox:true,width:5,align:"center"},*/
	         	    {field:'cmCrossId',title:'ID',width:10,align:"center"},
	         		{field:'cmSortId',title:'序号',width:5,align:"center",
	         		    formatter:function(value,row,index) {
	         		        return index+1;
	         		    }
	         		},
	         		{field:'cmCrossName',title:'车底交路',width:51,align:"center",sortable:true},
	         		{field:'tokenPsgBureau',title:'担当局',width:10,align:"center",sortable:true},
	         		{field:'throughLine',title:'相关局',width:34,align:"center",sortable:true},
	         		{field:'checkFlag',title:'审核状态',width:10,align:"center",sortable:true},
	   		   		       		   
	   		]];
	   		   	
	   	   	var trainNbr=$("#trainNbr").val();
	   	   	var chartId="e49e0fc2-3095-4796-ac2f-a47bb05da38e";
	   	   	var paras = { chartId : chartId, trainNbr : trainNbr };
	   	   	var url = basePath + "/cmcross/getCmUnitCrossInfo";
	   	   	
	   	   	crossManageData.$table = creatGrid($("#crossInfos"), columns, url, paras);
	   	   	//点击交路信息
	   	   	$("#crossInfos").datagrid({
	   	   		onClickRow:function(index, row){
	   	   			$("#cmUnitCrossId").val(row.cmCrossId);
	   	   			$("#cmCrossName").val(row.cmCrossName);
	   	   			showBlock(row.cmCrossId);
	   	   		}
	   	   	});
}


/**
 * 逻辑交路列表查询
 */
function loadCrosses(){
   	var trainNbr=$("#trainNbr").val();
   	var chartId=$("#cmVersionId").val();
   	var paras = {trainNbr:trainNbr,chartId:chartId};
   	
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

function showBlock(cmCrossId){
    if(crossManageData.is_on&&crossManageData.currentcmPartOriginalCrossId===cmCrossId){
        return;  //连续点击同一行，不重复发送请求
    }
    crossManageData.currentcmPartOriginalCrossId=cmCrossId;
    var slideblock=$(".slideblock");
    var left=$(".left");
    if(!crossManageData.is_on){
        //打开并显示当前行数据
        slideblock.show("300");
        left.css({width:"40%"});
        crossManageData.is_on=true;
    }
    showDetail(cmCrossId);
    crossManageData.$table.datagrid('resize',{"width":"100%"});
}

//交路详情
function showDetail (cmCrossId) {
    $.ajax({
        type: "post",
        url: basePath + "/cmcross/getCmUnitCrossTrainInfoDetail",
        data: JSON.stringify({crossId : cmCrossId}),
        dataType: "json",
        contentType : "application/json",
        success: function (result) {
        	var data = result.data[0].oCrossinfo;
            $("#plan_construction_input_trainNbr").text(data.cmCrossName);
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
            //openStatus("获取信息失败！");
        },
    });
    showunittrain();
    showcrosstrain();
}

/**
 * 交路列表
 */
function showunittrain(){
	//e.preventDefault();
	
	var columns =[[
  	    {field:'trainSort',title:'组序',width:15,align:"center"},
  		{field:'trainNbr',title:'车次',width:15,align:"center",sortable:true},
  		{field:'startStn',title:'始发站',width:15,align:"center",sortable:true},
  		{field:'startBureau',title:'发局',width:15,align:"center",sortable:true},
  		{field:'endStn',title:'终到站',width:15,align:"center",sortable:true},
  		{field:'endBureau',title:'终局',width:15,align:"center",sortable:true},
//  		{field:'checkFlag',title:'线型',width:10,align:"center",sortable:true},
//  		{field:'checkFlag',title:'组内<br>间隔',width:5,align:"center",sortable:true},
//  		{field:'checkFlag',title:'组间<br>间隔',width:5,align:"center",sortable:true},
  		{field:'useStatus',title:'状态',width:10,align:"center",sortable:true},
//  		{field:'checkFlag',title:'高铁规律',width:10,align:"center",sortable:true},
//  		{field:'checkFlag',title:'普速规律',width:9,align:"center",sortable:true},
//  		{field:'checkFlag',title:'交替时间',width:15,align:"center",sortable:true},
	]];
	
	var cmUnitCrossId = $("#cmUnitCrossId").val();
	var paras = {crossId:cmUnitCrossId};
   	var url = basePath + "/cmcross/getCmUnitCrossTrainInfo";
   	crossManageData.$unitTable = creatUnitGrid($("#cross_trainInfo"), columns, url, paras);
   	//$(this).tab('show');
	
}

/**
 * 对数列表
 */
function showcrosstrain(){
	var columns =[[
  	    {field:'cmCrossId',title:'组序',width:5,align:"center"},
  		{field:'cmSortId',title:'车序',width:5,align:"center"},
  		{field:'cmCrossName',title:'车次',width:5,align:"center",sortable:true},
  		{field:'tokenPsgBureau',title:'始发站',width:15,align:"center",sortable:true},
  		{field:'throughLine',title:'发局',width:5,align:"center",sortable:true},
  		{field:'checkFlag',title:'终到站',width:10,align:"center",sortable:true},
  		{field:'checkFlag',title:'终局',width:5,align:"center",sortable:true},
  		{field:'checkFlag',title:'线型',width:5,align:"center",sortable:true},
  		{field:'checkFlag',title:'组内<br>间隔',width:5,align:"center",sortable:true},
  		{field:'checkFlag',title:'组间<br>间隔',width:5,align:"center",sortable:true},
  		{field:'checkFlag',title:'状态',width:5,align:"center",sortable:true},
  		{field:'checkFlag',title:'高铁规律',width:10,align:"center",sortable:true},
  		{field:'checkFlag',title:'普速规律',width:10,align:"center",sortable:true},
  		{field:'checkFlag',title:'交替时间',width:10,align:"center",sortable:true},
	]];
	
	var cmUnitCrossId = $("#cmUnitCrossId").val();
	var paras = {crossId:cmUnitCrossId};
   	var url = basePath + "/cmcross/getPhyCrossTrainInfo";
   	
   	crossManageData.$crossTable = creatCrossGrid($("#unti_trainInfo"), columns, url, paras);
	
}

//列车运行图
function openGraph(){
	var chartId="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3";
	var cmUnitCrossId = $("#cmUnitCrossId").val();
	var cmCrossName = $("#cmCrossName").val();
	var paras = {crossName:cmCrossName,crossId:cmUnitCrossId,chartId:chartId};
	$("#crossGraph").find("iframe").attr("src",basePath+"/drawCross/provideCrossChartData?crossName="+cmCrossName+"&crossId="+cmUnitCrossId+"&chartId="+chartId);
	$('#crossGraph').dialog({
        title: '运行图',
        width: 1000,
        height: 600,
        cache: false,
        modal: true
    });
	
    $('#crossGraph').dialog('open');
}
