var crossManageData={}; //我的全局变量
$(function(){
    getTokenVehBureau();
	getPlan();

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
})


//初始化页面主table
function initTable(){
  var columns =[[
      {field:'CMCROSSID',title:'ID',hidden:true},
      {field:'cmSortId',title:'序号',width:6,align:"center",
          formatter:function(value,row,index) {
              return index+1;
          }
      },
      {field:'CMCROSSNAME',title:'车底交路',width:45,align:"center",sortable:true},
      {field:'TOKENVEHBUREAU',title:'担当局',width:10,align:"center",sortable:true,
        formatter:function (value,row,index) {
          return getBureau(value);
        }
    },
      {field:'RELEVANTBUREAU',title:'相关局',width:39,align:"center",sortable:true,
    	formatter:function(value,row,index) {
				return getBureauS(value);
			}
      },
                    
  ]];
  //查询条件
  var input_cross_chart_id = $("#input_cross_chart_id").val();
  var selfRelevant=null;
  if(document.getElementById("selfRelevant").checked){
	  selfRelevant = currentUserBureau;
  }else{
	  selfRelevant = null;
  }
  
  //var selfRelevant = $("#selfRelevant：checked").val();
  var cmVersionId = $("#cmVersionId").val();
  var trainNo = $("#trainNo").val();
  // var chartId="e49e0fc2-3095-4796-ac2f-a47bb05da38e";
  var paras = {
      input_cross_chart_id: input_cross_chart_id,
      selfRelevant: selfRelevant,
      // cmVersionId : cmVersionId,
      trainNbr:trainNo,
      chartId: cmVersionId
  };
    var url = basePath + "/cmcross/getCmUnitCrossInfo";
    crossManageData.$table = creatGrid($("#crossInfos"), columns, url, paras,clickRow);
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
 * 获取担当局
 */
function getTokenVehBureau(){
  $.ajax({
    type: "POST",
        dataType: "json",
        contentType : "application/json",
    url: basePath + "/cmcross/getFullStationInfo",
    success:function(data){
      var option="<option value=''>全部</option>";
      var $input_cross_chart_id=$("#input_cross_chart_id");
      $input_cross_chart_id.empty();
      for(var i=0; i<data.data.length;i++){
        option += '<option value="'+data.data[i].ljpym+'">'+data.data[i].ljjc+'</option>';
        setBureau(data.data[i].ljpym,data.data[i].ljjc);
      }
      $input_cross_chart_id.append(option);
      $("#input_cross_chart_id option[value='"+currentUserBureau+"']").attr("selected","selected");
    }
    
  });
}

/**
 * 查询
 */
function searchData(){
  //担当局
  var input_cross_chart_id = $("#input_cross_chart_id").val();
  //本局相关
  var selfRelevant;
  if(document.getElementById("selfRelevant").checked){
	  selfRelevant = currentUserBureau;
  }else{
	  selfRelevant = null;
  }
  //方案
  var cmVersionId = $("#cmVersionId").val();
  //车次
  var trainNo = $("#trainNo").val();
  var paras = {
      input_cross_chart_id: input_cross_chart_id,
      selfRelevant: selfRelevant,
      // cmVersionId : cmVersionId,
      trainNbr:trainNo,
      chartId: cmVersionId
  };
  crossManageData.$table.datagrid("load",paras);
}

/**
*点击左边列表中的行在右边显示对应的“运行图、交路信息、列车信息（交）、列车信息（对）”
*/
function clickRow (row) {
	var cmCrossId =row.CMCROSSID;
    showDetail(cmCrossId);
    showunittrain(cmCrossId);
  //  showcrosstrain(cmCrossId);
    showGraph(cmCrossId,row.CMCROSSNAME,row.CMVERSIONID);
    $("#crossHideId").val("");
}

/**
*显示“运行图”
*/
function showGraph (cmCrossId,cmCrossName,cmVersionId) {
	//debugger;
	
  // body...
	var chartId=cmVersionId;
	//var cmUnitCrossId = $("#cmUnitCrossId").val();
	//var cmCrossName = $("#cmCrossName").val();
	//var paras = {crossName:cmCrossName,crossId:cmCrossId,chartId:chartId};
	$("#crossGraph").find("iframe").attr("src",basePath+"/drawCross/provideCrossChartData?crossName="+cmCrossName+"&crossId="+cmCrossId+"&chartId="+chartId);
}

/**
*显示“交路信息”
*/
function showDetail (cmCrossId) {
  $.ajax({
        type: "post",
        url: basePath + "/cmcross/getCmUnitCrossTrainInfoDetail",
        data: JSON.stringify({crossId : cmCrossId}),
        dataType: "json",
        contentType : "application/json",
        success: function (result) {
            var data = result.data[0].oCrossinfo;
            $("#plan_construction_input_trainNbr").text(data.crossName!==null?data.crossName:"");
            $("#undertake").text(data.tokenVehBureau!==null?getBureau(data.tokenVehBureau):"");
            $("#groupData").text(data.groupTotalNbr!==null?data.groupTotalNbr:"");
            $("#openState").text(data.spareFlag!==null?data.spareFlag:"");
            $("#mold").text(data.locoType!==null?data.locoType:"");
            $("#relaceDate").text(data.alternateDate!==null?data.alternateDate:"");
            $("#replace_train_number").text(data.alternateTrainNbr!==null?data.alternateTrainNbr:"");
            $("#normalRule").text(data.commonlineRule!==null&&data.commonlineRule!=="null"?data.commonlineRule:"");
            $("#week").text(data.appointWeek!==null&&data.appointWeek!=="null"?data.appointWeek:"");
            $("#date").text(data.appointDay!==null&&data.appointDay!=="null"?data.appointDay:"");
            $("#cycle").text(data.appointPeriod!==null&&data.appointPeriod!=="null"?data.appointPeriod:"");
            $("#runSection").text(data.crossSection!==null?data.crossSection:"");
            $("#runDistance").text(data.runRange!==null?data.runRange:"");
            $("#level").text(data.crossLevel!==null?data.crossLevel:"");
            $("#log").text(data.pairNbr!==null?data.pairNbr:"");
            $("#orgnize").text(data.marshallingNums!==null?data.marshallingNums:"");
            $("#createPeople").text(data.peopleNums!==null?data.peopleNums:"");
            $("#orgnizeContent").text(data.marshallingContent!==null?data.marshallingContent:"");
            $("#groupLevel").text(data.crhType!==null?data.crhType:"");
            $("#electricity").text(data.elecSupply!==null?data.elecSupply:"");
            $("#mass").text(data.dejCollect!==null?data.dejCollect:"");
            $("#airconditioner").text(data.airCondition!==null?data.airCondition:"");
            $("#remark").text(data.note!==null?data.note:"");
        },
        error: function () {
            //openStatus("获取信息失败！");
        },
    });
}


/**
*显示“列车信息（交）”
*/
function showunittrain(cmCrossId){  
  var columns =[[
      {field:'baseTrainId',title:'ID',hidden:true},
      {field:'trainSort',title:'组序',width:15,align:"center"},
      {field:'trainNbr',title:'车次',width:15,align:"center",sortable:true},
      {field:'startStn',title:'始发站',width:15,align:"center",sortable:true},
      {field:'startBureau',title:'发局',width:15,align:"center",sortable:true,
        formatter:function (value,row,index) {
          return getBureau(value);
        }
    },
      {field:'endStn',title:'终到站',width:15,align:"center",sortable:true},
      {field:'endBureau',title:'终局',width:15,align:"center",sortable:true,
        formatter:function (value,row,index) {
          return getBureau(value);
        }
    },
      {field:'useStatus',title:'状态',width:10,align:"center",sortable:true},
  ]];
  
  var paras = {crossId:cmCrossId};
  var url = basePath + "/cmcross/getCmUnitCrossTrainInfo";
    crossManageData.$unitTable = creatGrid($("#cross_trainInfo"), columns, url, paras,clickUnitRow); 
}

/**
*显示“列车信息（对）”
*/
// function showcrosstrain(cmCrossId){
//   var columns =[[
//       {field:'cmCrossId',title:'组序',width:5,align:"center"},
//       {field:'cmSortId',title:'车序',width:5,align:"center"},
//       {field:'cmCrossName',title:'车次',width:5,align:"center",sortable:true},
//       {field:'tokenPsgBureau',title:'始发站',width:15,align:"center",sortable:true},
//       {field:'throughLine',title:'发局',width:5,align:"center",sortable:true},
//       {field:'checkFlag',title:'终到站',width:10,align:"center",sortable:true},
//       {field:'checkFlag',title:'终局',width:5,align:"center",sortable:true},
//       {field:'checkFlag',title:'线型',width:5,align:"center",sortable:true},
//       {field:'checkFlag',title:'组内<br>间隔',width:5,align:"center",sortable:true},
//       {field:'checkFlag',title:'组间<br>间隔',width:5,align:"center",sortable:true},
//       {field:'checkFlag',title:'状态',width:5,align:"center",sortable:true},
//       {field:'checkFlag',title:'高铁规律',width:10,align:"center",sortable:true},
//       {field:'checkFlag',title:'普速规律',width:10,align:"center",sortable:true},
//       {field:'checkFlag',title:'交替时间',width:10,align:"center",sortable:true},
//   ]];
  
//   var paras = {crossId:cmCrossId};
//   var url = basePath + "/cmcross/getPhyCrossTrainInfo";
    
//   crossManageData.$crossTable = creatGrid($("#unti_trainInfo"), columns, url, paras);
  
// }

/**
*点击“列车信息（交）右上角“时刻表”
*/
function clickUnitRow (row) {
  var timeId = row.baseTrainId;
  $("#crossHideId").val(timeId);
  $("#trainNumber").val(row.trainNbr);
  $("#startStation").val(row.startStn);
  $("#endStation").val(row.endStn);

}

//时刻表
function timeOpen() {
  var timeId = $("#crossHideId").val();
  if(timeId==""){
	  	alert("请先选择车次！");
		return;
  }
  var columns =[[
    {field:'childIndex',title:'序号',rowspan:2,width:46,align:"center",
    	formatter:function(value,row,index){
        	return index+1;
     }},
    {field:'nodeName',title:'站名',rowspan:2,width:60,align:"center"},
    {field:'bureauShortName',title:'路局',rowspan:2,width:55,align:"center",sortable:true},
    {field:'arrTime',title:'到达时间',rowspan:2,width:87,align:"center",sortable:true},
    {field:'dptTime',title:'出发时间',rowspan:2,width:87,align:"center",sortable:true},
    {title:'停时',colspan:2,align:"center"},
    {title:'天数',colspan:2,align:"center"},
    {field:'trainlineTempId',title:'车次',rowspan:2,width:116,align:"center",sortable:true,
    	formatter:function(value,row,index){
         	return $("#trainNumber").val();//用一行不用的先占位
      }},
    {field:'dptTrainNbr',title:'股道',rowspan:2,width:46,align:"center",sortable:true},
    {field:'trackName',title:'站台',rowspan:2,width:46,align:"center",sortable:true},
    {field:'platForm',title:'作业',rowspan:2,width:54,align:"center",sortable:true},
    {field:'nodeTdcsName',title:'客运',rowspan:2,width:87,align:"center",sortable:true},
    {field:'kyyy',title:'普速规律',rowspan:2,width:61,align:"center",sortable:true},
    {field:'nodeTdcsId',title:'交替时间',rowspan:2,width:61,align:"center",sortable:true,
    	formatter:function(value,row,index){
         	return '';//先不展示
      }},
    
    ],[
      {field:'source_TIME_SCHEDULE_HOUR',title:'到达',width:37,align:"center"},
      {field:'source_TIME_SCHEDULE_MINUTE',title:'出发',width:37,align:"center"},
      {field:'target_TIME_SCHEDULE_MINUTE',title:'到达',width:37,align:"center"},
      {field:'target_TIME_SCHEDULE_SECOND',title:'出发',width:37,align:"center"},
      ]
    ];
  var jianColumns =[[
                 {field:'childIndex1',title:'序号',rowspan:2,width:46,align:"center",
                 	formatter:function(value,row,index){
                     	return index+1;
                  }},
                 {field:'nodeName1',title:'站名',rowspan:2,width:60,align:"center"},
                 {field:'bureauShortName1',title:'路局',rowspan:2,width:55,align:"center",sortable:true},
                 {field:'arrTime1',title:'到达时间',rowspan:2,width:87,align:"center",sortable:true},
                 {field:'dptTime1',title:'出发时间',rowspan:2,width:87,align:"center",sortable:true},
                 {title:'停时',colspan:2,align:"center"},
                 {title:'天数',colspan:2,align:"center"},
                 {field:'trainlineTempId1',title:'车次',rowspan:2,width:116,align:"center",sortable:true,
                 	},
                 {field:'dptTrainNbr1',title:'股道',rowspan:2,width:46,align:"center",sortable:true},
                 {field:'trackName1',title:'站台',rowspan:2,width:46,align:"center",sortable:true},
                 {field:'platForm1',title:'作业',rowspan:2,width:54,align:"center",sortable:true},
                 {field:'nodeTdcsName1',title:'客运',rowspan:2,width:87,align:"center",sortable:true},
                 {field:'kyyy1',title:'普速规律',rowspan:2,width:61,align:"center",sortable:true},
                 {field:'nodeTdcsId1',title:'交替时间',rowspan:2,width:61,align:"center",sortable:true},
                 
                 ],[
                   {field:'source_TIME_SCHEDULE_HOUR1',title:'到达',width:37,align:"center"},
                   {field:'source_TIME_SCHEDULE_MINUTE1',title:'出发',width:37,align:"center"},
                   {field:'target_TIME_SCHEDULE_MINUTE1',title:'到达',width:37,align:"center"},
                   {field:'target_TIME_SCHEDULE_SECOND1',title:'出发',width:37,align:"center"},
                   ]
                 ];

  //var timeId = "ff8a020a-2b64-48be-8e7b-30c6e41c438a";
  var trainNumber = $("#trainNumber").val();
  var startStation = $("#startStation").val();
  var endStation = $("#endStation").val();
  var paras = {baseTrainId:timeId};
  var url = basePath + "/cmcross/queryCmTrainTimesDepands";
  
  crossManageData.$xiangTable = creatTimeGrid_detail($("#timedetailinfo_detail"), columns, url, paras);
  crossManageData.$xiangTable.datagrid({onLoadSuccess:function(data){
	  	var jianItems =[];
	  	var xiangItem = data.rows;
	  	for(var i=0;i<xiangItem.length;i++){
	  		if(xiangItem[i].stationFlag!="BTZ"){
	  			var jianItem = new Object(); //防止列名相同，列表列宽异常，将简点列表的列名重命名。
	  			jianItem.childIndex1= xiangItem[i].childIndex;
	  			jianItem.nodeName1= xiangItem[i].nodeName;
	  			jianItem.bureauShortName1= xiangItem[i].bureauShortName;
	  			jianItem.arrTime1= xiangItem[i].arrTime;
	  			jianItem.dptTime1= xiangItem[i].dptTime;
	  			jianItem.trainlineTempId1= trainNumber;//xiangItem[i].trainlineTempId;
	  			jianItem.dptTrainNbr1= xiangItem[i].dptTrainNbr;
	  			jianItem.trackName1= xiangItem[i].trackName;
	  			jianItem.planForm1= xiangItem[i].planForm;
	  			jianItem.nodeTdcsName1= xiangItem[i].nodeTdcsName;
	  			jianItem.kyyy1= xiangItem[i].kyyy;
	  			jianItem.nodeTdcsId1= '';//xiangItem[i].nodeTdcsId;
	  			jianItem.source_TIME_SCHEDULE_HOUR1= xiangItem[i].source_TIME_SCHEDULE_HOUR;
	  			jianItem.source_TIME_SCHEDULE_MINUTE1= xiangItem[i].source_TIME_SCHEDULE_MINUTE;
	  			jianItem.target_TIME_SCHEDULE_MINUTE1= xiangItem[i].target_TIME_SCHEDULE_MINUTE;
	  			jianItem.target_TIME_SCHEDULE_SECOND1= xiangItem[i].target_TIME_SCHEDULE_SECOND;
	  			jianItems.push(jianItem);
	  		}
	  	}
	  	crossManageData.$jianTable = $("#timedetailinfo").datagrid({
	  		height: "auto",
		        width:'100%',
		        nowrap: true,
		        striped: true,
		        checkOnSelect:false, //点击不选中行
		        collapsible:false,//是否可折叠的
		        striped: true,//显示斑马线效果(默认false)
		        fit: true,//自动大小
		        remoteSort:false,
		        singleSelect:false,//是否单选
		        fitColumns:true,
		        pageNumber:1,
		        pageSize:500,
		        columns:jianColumns,
	     		data:jianItems,
          rowStyler: function(index,row){
                    return "height:30px";
        }
	     	})
	  	  }
	  });

  $('#timeTable').show().dialog({
      title: '时刻表:车次：' + trainNumber  +"&nbsp;&nbsp;" + startStation + '——' + endStation,
      width:1000,
      height:800,
      cache: false,
      modal: true
  });
  
  $('#timeTable').dialog('open');
}

function fullScreen () {
    var $graph = $("#crossGraph").clone().attr("id","fullGraph");
    $("#fullScreen").append($graph);
    $("#shadowBlock").show();
    $("#fullScreen").show();

  }

function closeFull () {
  $("#shadowBlock").hide();
  $("#fullScreen").hide();
  $("#fullGraph").remove();
}