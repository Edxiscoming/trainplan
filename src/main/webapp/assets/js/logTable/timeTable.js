//时刻表
function timeOpen() {
	var columns =[[
 	    {field:'childIndex',title:'序号',rowspan:2,width:4,align:"center"},
 		{field:'nodeName',title:'站名',rowspan:2,width:6,align:"center"},
 		{field:'bureauShortName',title:'路局',rowspan:2,width:7,align:"center",sortable:true},
 		{field:'arrTime',title:'到达时间',rowspan:2,width:10,align:"center",sortable:true},
 		{field:'dptTime',title:'出发时间',rowspan:2,width:10,align:"center",sortable:true},
 		{field:'checkFlag',title:'停时',colspan:2,width:8,align:"center",sortable:true},
 		{field:'runDays',title:'天数',colspan:2,width:5,align:"center",sortable:true},
 		{field:'trainlineTempId',title:'车次',rowspan:2,width:5,align:"center",sortable:true},
 		{field:'dptTrainNbr',title:'股道',rowspan:2,width:5,align:"center",sortable:true},
 		{field:'trackName',title:'站台',rowspan:2,width:5,align:"center",sortable:true},
 		{field:'planForm',title:'作业',rowspan:2,width:6,align:"center",sortable:true},
 		{field:'nodeTdcsName',title:'客运',rowspan:2,width:5,align:"center",sortable:true},
 		{field:'kyyy',title:'普速规律',rowspan:2,width:5,align:"center",sortable:true},
 		{field:'nodeTdcsId',title:'交替时间',rowspan:2,width:5,align:"center",sortable:true},
 		
 		],[
 		  {field:'source_TIME_SCHEDULE_HOUR',title:'到达',width:4,align:"center"},
 		  {field:'source_TIME_SCHEDULE_MINUTE',title:'出发',width:4,align:"center"},
 		  {field:'target_TIME_SCHEDULE_MINUTE',title:'到达',width:4,align:"center"},
 		  {field:'target_TIME_SCHEDULE_SECOND',title:'出发',width:4,align:"center"},
 		  ]
   	];
	
	var timeId = "ff8a020a-2b64-48be-8e7b-30c6e41c438a";
	var paras = {baseTrainId:timeId};
   	var url = basePath + "/cmcross/queryCmTrainTimesDepands";
   	
   	crossManageData.$xiangTable = creatTimeGrid_detail($("#timedetailinfo_detail"), columns, url, paras);
	
    $('#timeTable').show().dialog({
        title: '时刻表:车次：D927  北京西高速场——南宁',
        width:1000,
        height:800,
        cache: false,
        modal: true
    });
    
    $('#timeTable').dialog('open');
}