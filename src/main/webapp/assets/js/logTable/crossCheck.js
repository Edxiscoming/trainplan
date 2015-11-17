/**
 * Created by Administrator on 2015/9/24.
 */

//var $table ={}; //datagrid对象
var crossManageData={}; //我的全局变量
$(function(){
	resizeTable();
	getPlan();
	initHeaderBureau();


	$("#otherTab").click(function() {
		$("#token").show();
		$("#tokenVehBureau").show();
		$("#crossBtn").hide();
		$("#selfSearchBtn").hide();
		$("#otherSearchBtn").show();
		searchData(1);
	});

	$("#selfTab").click(function() {
		$("#token").hide();
		$("#tokenVehBureau").hide();
		$("#crossBtn").show();
		$("#selfSearchBtn").show();
		$("#otherSearchBtn").hide();
		searchData(0);
	});

})
 
//初始化table
function initTable(){
	var columns =[[
	       	    {field:"ck",checkbox:true,width:5,align:"center"},
	       	    {field:'cmPartOriginalCrossId',hidden:true},
	       	    {field:'alternateDate',hidden:true},
	       		{field:'crossName',title:'车底交路',width:20,align:"left",sortable:true},

	       			
	       		{field:'waitToAdd',title:'运行区段',width:20,align:"center",sortable:true},
	       		{field:'pairNbr',title:'对数',width:5,align:"center",sortable:true},
	       		{field:'groupTotalNbr',title:'组数',width:5,align:"center",sortable:true},
	       		{field:'tokenVehBureau',title:'担当局',width:5,align:"center",sortable:true,
	       			formatter:function(value,row,index) {
	       				return getBureau(value);
	       			}
	       		},
	       		{field:'relevantBureau',title:'经由局',width:10,align:"center",sortable:true,
	       			formatter:function(value,row,index) {
	       				return getBureauS(value);
	       			}
	       		},
	       		{field:'createCrossFlag',title:'生成状态',width:5,align:"center",sortable:true,
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
	       		{field:'airCondition',title:'操作',width:10,align:"center",sortable:true,
	       			formatter:function(value,row,index) {
	       				if(currentUserBureau===row.tokenVehBureau && !row.createCrossFlag){
	       						return '<a style="cursor:pointer" onclick="showBlock(\''+
		       					row.cmOriginalCrossId+
		       					'\',false);">详情</a> <a style="margin-left:10px;cursor:pointer" onclick="showBlock(\''+
		       					row.cmOriginalCrossId+
		       					'\',true);">修改</a> <a style="margin-left:10px;cursor:pointer" onclick="deleteCross(\''+
		       					row.cmOriginalCrossId+'\');">删除</a>';
	       				}else if (currentUserBureau!==row.tokenVehBureau && !row.createCrossFlag){
	       					return '<a style="cursor:pointer" onclick="showBlock(\''+
		       					row.cmOriginalCrossId+
		       					'\',false);">详情</a> <a style="margin-left:10px;cursor:pointer" onclick="crossManage(\''+row.cmOriginalCrossId+
		       					'\')">修改</a>';
	       				} else {
	       					return '<a style="cursor:pointer" onclick="showBlock(\''+
		       					row.cmOriginalCrossId+
		       					'\',false);">详情</a>';
	       				}
	       				
	       			}
	       		},
	       		
	       		{field:'commonlineRule',title:'开行规律',width:15,align:"center",sortable:true,formatter:function(value,row,index) {
       		        var html = row.commonlineRule!=="null"&&row.commonlineRule!==null?row.commonlineRule:"";
       		        var ruleTitle = "普线规律：";
       		        if(html===""){
       		        	html = row.appointWeek!=="null"&&row.appointWeek!==null?row.appointWeek:"";
       		        	ruleTitle = "指定星期：";
       		        	if(html===""){
       		        		html = row.appointDay!=="null"&&row.appointDay!==null?row.appointDay:"";
       		        		ruleTitle = "指定日期：";
       		        		if(html===""){
       		        			html = row.appointPeriod!=="null"&&row.appointPeriod!==null?row.appointPeriod:"";
       		        			ruleTitle = "指定周期：";
       		        			if(html===""){
       		        				html=row.highlineRule!=="null"&&row.highlineRule!==null?row.highlineRule:"";
       		        				if(html!==""){
       		        					ruleTitle = "高线规律：";	
       		        				}else{
       		        					ruleTitle = "";
       		        				}
       		        				
       		        			}
       		        		}
       		        	}
       		        }
       		        return ruleTitle+html;
       		    }},
       		 {field:'alternateTrainNbr',title:'交替车次',width:10,align:"center",sortable:true}
	       	    ]];
			//查询条件
			var cmVersionId = $("#cmVersionId").val();
			var cmOriginalTrainId = $("#cmOriginalTrainId").val();
		
			var paras = {
					cmVersionId : cmVersionId,
//					highlineFlag:"-1"
					tokenVehBureau : currentUserBureau,
					tokenFlag: 0
			};
	       	var url = basePath + "/cmOriginalCross/pageQueryCross";
	       	var onClickRow= function(obj){ //点击行显示详情，
	       		//showBlock(obj.cmOriginalCrossId);
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
		url: basePath + "/partOriginalCross/queryVersionInfo",
		success:function(data){
			var option="";
			var $cmVersionId=$("#cmVersionId");
			$cmVersionId.empty();
			var $cmVersionId2=$("#cmVersionId2");
			$cmVersionId2.empty();
			for(var i=0; i<data.list.length;i++){
				option += '<option value="'+data.list[i].cmVersionId+'">'+data.list[i].name+'</option>';
			}
			$cmVersionId.append(option);
			$cmVersionId2.append(option);
			initTable();
		}
		
	});
}
/**
 * 查询
 */
function searchData(tokenFlag){
	//方案
	var cmVersionId = $("#cmVersionId").val();
	var tokenVehBureau=currentUserBureau;
	if (tokenFlag===1) {
		tokenVehBureau = $("#tokenVehBureau").val();
	};
	var cmOriginalTrainId=$("#cmOriginalTrainId").val();
	//模糊查询
	var checkFlag=$("#checkFlag:checked");
	if(checkFlag.length)
		checkFlag=1;
	else
		checkFlag='';
	var paras = {
			cmVersionId : cmVersionId,
			tokenVehBureau : tokenVehBureau,
			crossName:cmOriginalTrainId,
			tokenFlag: tokenFlag,
			crossNamecheckflag:checkFlag
	};
	console.log(paras);
	crossManageData.$table.datagrid("load",paras);
}
function closeSlideBlock(){ //点击右侧的按钮收起div
	$(".slideblock").hide("300");
	$("#shadowWindow").hide();
}

/**
 * 获取详情
 * @param id
 */

function showBlock(cmPartOriginalCrossId,isModefy){
	$("#orginalCrossId").val(cmPartOriginalCrossId);
	$("#shadowWindow").show();
	$("#selfBlock").show("300");
	showDetail(cmPartOriginalCrossId,isModefy);
}

function showDetail (cmPartOriginalCrossId,isModify) {
    console.log(cmPartOriginalCrossId);
    $.ajax({
        type: "post",
        url: basePath + "/cmOriginalCross/queryCrossById",
        data: JSON.stringify({crossId : cmPartOriginalCrossId}),
        dataType: "json",
        contentType : "application/json",
        success: function (json) {
            var data = json.obj;
            
            $("#cmPartOriginalCrossId2").val(cmPartOriginalCrossId);
            $("#plan_construction_input_trainNbr2").text(data.crossName?data.crossName:"");
            $("#undertake2").text(data.tokenVehBureau?getBureau(data.tokenVehBureau):"");
            if(isModify){
            	$("#groupData2").val(data.groupTotalNbr?data.groupTotalNbr:"").removeClass("hidden");
            	$("a[name='operationBtn']").removeClass("hidden");
            	$("#groupData2Span").addClass("hidden");
            }else{
            	$("#groupData2Span").text(data.groupTotalNbr?data.groupTotalNbr:"").removeClass("hidden");
            	$("a[name='operationBtn']").addClass("hidden");
            	$("#groupData2").addClass("hidden");
            }
            $("#openState2").text(data.spareFlag!==null?data.spareFlag:"");
            $("#mold2").text(data.locoType!==null?data.locoType:"");
            $("#relaceDate2").text(data.alternateDate!==null?data.alternateDate:"");
            $("#replace_train_number2").text(data.alternateTrainNbr!==null?data.alternateTrainNbr:"");
            $("#normalRule2").text(data.commonlineRule!==null?data.commonlineRule:"");
            $("#week2").text(data.appointWeek!==null?data.appointWeek:"");
            $("#date2").text(data.appointDay!==null?data.appointDay:"");
            $("#cycle2").text(data.appointPeriod!==null?data.appointPeriod:"");
            $("#runSection2").text(data.crossSection!==null?data.crossSection:"");
            $("#runDistance2").text(data.runRange!==null?data.runRange:"");
            $("#level2").text(data.crossLevel!==null?data.crossLevel:"");
            $("#pairNbr2").val(data.pairNbr!==null?data.pairNbr:"");
            $("#orgnize2").text(data.marshallingNums!==null?data.marshallingNums:"");
            $("#createPeople2").text(data.peopleNums!==null?data.peopleNums:"");
            $("#orgnizeContent2").text(data.marshallingContent!==null?data.marshallingContent:"");
            $("#groupLevel2").text(data.crhType!==null?data.crhType:"");
            $("#electricity2").text(data.elecSupply!==null?data.elecSupply:"");
            $("#mass2").text(data.dejCollect!==null?data.dejCollect:"");
            $("#airconditioner2").text(data.airCondition!==null?data.airCondition:"");
            $("#remark2").text(data.note!==null?data.note:"");
            $("#groupTotalNbr2").text(data.groupTotalNbr!==null?data.groupTotalNbr:"");
            
            
        },
        error: function () {
            openStatus("获取信息失败！");
        },
    });
    
}
/**
 * 新增保存
 */
//function submitData(){
//	var data1=$("#newCross").serialize();
//	var fdata=JSON.stringify(data1).replace(/&/g,'","' ).replace(/=/g,'":"');
//	fdata="{"+fdata+"}";
//	console.log(fdata);
//	$.ajax(
//	{
//		type: "POST",
//        dataType: "json",
//        contentType : "application/json",
//		url: basePath + "/partOriginalCross/addCross",
//		data: JSON.stringify({result : fdata}),
//		success:function(data){
//			openStatus(data);
//			crossManageData.$table.datagrid("reload");
//		}
//		
//	});
//}
/**
 * 修改保存
 */


function changeData(){
	if(document.getElementById("groupData2").checkValidity())
		console.log("ok");
	else
	{
		alert("请输入正确的组数数值");
		return;
	}
	var data2=$("#update").serialize();
	console.log(data2);
	var fdata=JSON.stringify(data2).replace(/&/g,'","' ).replace(/=/g,'":"');
	console.log(fdata);
	fdata="{"+fdata+"}";
	$.ajax({
	type: "POST",
    dataType: "json",
    contentType : "application/json",
	url: basePath + "/cmOriginalCross/updateCross",
	data: JSON.stringify({cross : fdata}),
	success:function(data){
		openStatus(data);
		crossManageData.$table.datagrid("reload");
	}
})
}

function deleteCross(cmPartOriginalCrossId,event){
	window.event.stopPropagation();
	//获取选中行
	
	if(!cmPartOriginalCrossId){ //未选中直接返回。
		openStatus("请至少选择一条数据进行删除");
		return;
	}
	
	if(!confirm("确认删除吗？"))
		return false;
	
	
	$.ajax(
	{
		type: "POST",
        dataType: "json",
        contentType : "application/json",

		url: basePath + "/cmOriginalCross/delete",
		data: JSON.stringify({crossIds : cmPartOriginalCrossId}),
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
				crossIds += crosses[i].cmPartOriginalCrossId;
				updateCrosses.push(crosses[i]); 
			}; 
		}  
		if(crossIds == ""){
			alert("没有可审核的");
			return;
		}
		 $.ajax({
				url : basePath+"/partOriginalCross/checkCross",
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

function selectLine(highlineFlag){
	//方案
	var cmVersionId=$("#cmVersionId").val();
	var paras = {
			cmVersionId : cmVersionId,
			highlineFlag : highlineFlag
	};
	crossManageData.$table.datagrid("load",paras);
}

function generateCross(){
	
	var cross=crossManageData.$table.datagrid('getSelections');
	if(cross.length===0){
		return;
	}
	var toGenerate=""
	for(var i =0;i<cross.length;i++)
	{
		if(cross[i].createCrossFlag){
			alert("已生成的交路不能重复生成。");
			return;
		}
		if(cross[i].groupTotalNbr < 1) {
			alert("组数不能为零！");
			return;
		}
		if (typeof(cross[i].alternateDate) == "undefined" || cross[i].alternateDate == "" || cross[i].alternateDate == null) {
			alert("交替日期为空，不能生成交路！")
			return;
		}
		if ($.grep(cross[i].alternateDate.split("-"), function(n) {return $.trim(n).length > 0;}).length !== cross[i].crossName.split("-").length) {
			alert("交替日期不完整，不能生成交路！")
			return;
		}
		if (typeof(cross[i].commonlineRule) == "undefined" || cross[i].commonlineRule == "" || cross[i].commonlineRule == null) {
			alert("开行规律为必填项！")
			return;
		}
		if ($.grep(cross[i].commonlineRule.split("-"), function(n) {return $.trim(n).length > 0;}).length !== cross[i].crossName.split("-").length) {
			alert("开行规律填写不完整，不能生成交路！");
		};
		toGenerate+=","+cross[i].cmOriginalCrossId;
	}
	toGenerate=toGenerate.substring(1);
	$("#shadowBlock").show();
	$.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/cmOriginalCross/generalCross",
		data:JSON.stringify({  
			crossIds : toGenerate
		}),
		success:function(data){
			crossManageData.$table.datagrid("load");
			$("#shadowBlock").hide();
		}
		
	});
}


function openGraph(){
	var cross=crossManageData.$table.datagrid('getSelections');
	for(var i =0;i<cross.length;i++)
	{
		if(cross[i].groupTotalNbr < 1) {
			alert("组数不能为零！");
			return;
		}
		if (typeof(cross[i].alternateDate) == "undefined" || cross[i].alternateDate == "" || cross[i].alternateDate == null) {
			alert("交替日期为空，不能预览交路！")
			return;
		}
		if ($.grep(cross[i].alternateDate.split("-"), function(n) {return $.trim(n).length > 0;}).length !== cross[i].crossName.split("-").length) {
			alert("交替日期不完整，不能预览交路！")
			return;
		}
		if (typeof(cross[i].commonlineRule) == "undefined" || cross[i].commonlineRule == "" || cross[i].commonlineRule == null) {
			alert("开行规律为必填项！")
			return;
		}
		if ($.grep(cross[i].commonlineRule.split("-"), function(n) {return $.trim(n).length > 0;}).length !== cross[i].crossName.split("-").length) {
			alert("开行规律填写不正确，不能预览交路！");
		};
	}
	var orginalCrossId = $("#cmPartOriginalCrossId2").val();
	$("#basicalGraph").find("iframe")[0].src="/trainplan/drawCross/orginalCrossChartData?orginalCrossId="+orginalCrossId;
    $('#basicalGraph').show().dialog({
        title: '运行图',
        width: 800,
        height: 600,
        cache: false,
        resizable:true,
        modal: true
    });
    $('#basicalGraph').dialog('open');
}

/**
 * 初始化担当局
 */
function initHeaderBureau(){
	$.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/cmcross/getFullStationInfo",
		success:function(data){
			var option="<option value='-1'>全部</option>";
			var $headerBureau=$("#tokenVehBureau");
			$headerBureau.empty();
			for(var i=0; i<data.data.length;i++){
				option += '<option value="'+data.data[i].ljpym+'">'+data.data[i].ljjc+'</option>';
				setBureau(data.data[i].ljpym,data.data[i].ljjc);//设置中英文对照。
			}
			$headerBureau.append(option);
			// $("#tokenVehBureau option[value='"+currentUserBureau+"']").attr("selected","selected");
		}
		
	});
}


function resizeTable(){
	$(".left").height($(window).height()-130);
	$(".slideblock").height($(window).height()-80);
	$("#trainBlockBody").height($(window).height()-110);
	$(window).resize(function () {
		crossManageData.$table.datagrid('resize',{width:"100%",height:"100%"});
	})
}




/*
显示交路车次信息
*/
function initTrainNumber (cmPartOriginalCrossId) {
	$("#crossId").val(cmPartOriginalCrossId);
	$.ajax({
		type: "post",
		url: basePath + "/cmOriginalCross/queryCrossTrainDetail",
		data: JSON.stringify({crossId : cmPartOriginalCrossId}),
		dataType: "json",
		contentType: "application/json",
		success: function (json) {
			var trainList = json.data.trainList;
			//显示交路名
			$("#crossName").val(json.data.cross.crossName);
			$("#tokenVehBureau").val(json.data.cross.tokenVehBureau);
			//初始化滑块信息
			$.each(trainList, function (index, val) {
				//获取当前行的车次名称
				var trainName = val.trainName;
				var $liList = $('<li class="row" isexist="1"><label class="control-label pull-left"><span class="indexOfTrain">' + (index+1)+'</span><span class="trainNo">．'+trainName+ '</span><span style="margin-left:20px;">'+val.sourceNode+'—'+val.targetNode+'</span></label>'+
                                '<div class="pull-right" style="margin-right:20px;"><button class="btn btn-default btn-sm redBtn" onclick="deleteTrainNumber(this.parentNode.parentNode);">删除</button>'+
                                '<button class="btn btn-default btn-sm" style="margin-left:45px;" onclick="upRow(this.parentNode.parentNode);">上移</button>'+
                                '<button class="btn btn-default btn-sm" style="margin-left:20px;" onclick="downRow(this.parentNode.parentNode);">下移</button></div></li>');
				val.trainId = val.cmOriginalTrainId;
				val.crossId = cmPartOriginalCrossId;
				val.sourceNodeName = val.sourceNode;
				val.targetNodeName = val.targetNode;
				$liList.data("trainObj",val);
				$("#numberDetail").append($liList);
			}); 
			
		},
		error: function () {
			alert("获取信息失败！");
		}
	});
}

/*
删除交路车次
*/
crossManageData.deleteList = []; //要删除的list
function deleteTrainNumber (obj) {
	if($(obj).attr("isexist")==="1"){
		crossManageData.deleteList.push($(obj).data("trainObj").trainId);
	}
	$(obj).nextAll().each(function(){
		$(this).find(".indexOfTrain").text($(this).index());
	});
	$(obj).remove();
	var crossName = "";
	$("#numberDetail li").each(function(){
		crossName += "-" + $(this).data("trainObj").trainName;
	});
	crossName = crossName.substr(1);
	$("#crossName").val(crossName);
	
}


/**
 * 新建交路和管理交路
 * @param crossId 值为不为空表示修改，为空表示新增 
 */
function crossManage(crossId){   
    // if(crossId===undefined){ //新增
    // 	$("#shadowBlock").show();
    //     $("#crossBlock").show("300");
    //     $("#numberDetail").empty();
    //     $("#crossName").val("");
    // 	return;
    // }
    if(crossId===1){ //管理
	var row = crossManageData.$table.datagrid("getChecked");
	if(row.length===0 || row.length>1){
    	//alert("请只选中一条交路进行操作。");
    	return;
    }else if(row && row.length===1){
    	if(currentUserBureau!==row[0].tokenVehBureau){
    		return; //不可编辑非本局的交路
    	}
    	crossId = row[0].cmOriginalCrossId;
    }
    }
    $("#shadowWindow").show();
    $("#crossBlock").show("300");
    $("#numberDetail").empty();
    $("#crossName").val("");
    initTrainNumber(crossId);
    
}

/**
 * 上移
 * @param obj 上移行的html对象
 */
function upRow(obj){
	var $objBefore = $(obj).prev();
	if(!$objBefore){ //第一条数据不上移
		return false;
	}
	$(obj).insertBefore($objBefore).find(".indexOfTrain").text($(obj).index()+1);
	$objBefore.find(".indexOfTrain").text($objBefore.index()+1);
	var crossName = "";
	$("#numberDetail li").each(function(){
		crossName += "-" + $(this).data("trainObj").trainName;
	});
	crossName = crossName.substr(1);
	$("#crossName").val(crossName);
}
/**
 * 下移
 * @param obj 下移行的html对象
 */
function downRow(obj){
	var $objNext = $(obj).next();
	if(!$objNext){ //最后一条数据不下移
		return false;
	}
	$(obj).insertAfter($objNext).find(".indexOfTrain").text($(obj).index()+1);
	$objNext.find(".indexOfTrain").text($objNext.index()+1);
	var crossName = "";
	$("#numberDetail li").each(function(){
		crossName += "-" + $(this).data("trainObj").trainName;
	});
	crossName = crossName.substr(1);
	$("#crossName").val(crossName);
}

/**
 * 查询车次。
 */
function searchTrain(){
	var cmVersionId = $("#cmVersionId2").val();
	$.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/trainLine/getAllTrainLine",
		data:JSON.stringify({trainName:$("#searchKey").val(),versionId:cmVersionId}),
		success:function(data){
			var $result=$("#result");
			$result.empty();
			var trainList = data.list;
			if(trainList.length===0){
				option="<span>未查询出车次。</span>";
				$result.append(option);
				return;
			}
			for(var i=0; i<trainList.length;i++){
				var trainObj=new Object();
				trainObj = trainList[i];
				trainObj.trainId = trainList[i].id;
				trainObj.trainName = trainList[i].name;
				trainObj.trainDateFrom = trainList[i].executionSourceTime;
				trainObj.trainDateTo = trainList[i].executionTargetTime;
				trainObj.sourceTime = toNeedDate(trainList[i].sourceTime);
				trainObj.targetTime = toNeedDate(trainList[i].targetTime);
				var newDateFrom = new Date(trainObj.trainDateFrom);
				var newDateTo = new Date(trainObj.trainDateTo);
				//newDate.setTime(trainDate);
				var sourceName= trainObj.sourceNodeStationName!==null?trainObj.sourceNodeStationName:trainObj.sourceNodeName;
				var targetName= trainObj.targetNodeStationName!==null?trainObj.targetNodeStationName:trainObj.targetNodeName;
				var $a = $('<a class="row list-group-item" href="#"><span class="col-xs-3">'+
						trainObj.trainName+
						'</span><span class="col-xs-5">'+
						sourceName+
						'—'+targetName+
						'</span><span class="col-xs-4">'+
						newDateFrom.getFullYear()+'-'+newDateFrom.getMonth()+1+'-'+newDateFrom.getDate()+
						'至'+
						newDateTo.getFullYear()+'-'+newDateTo.getMonth()+1+'-'+newDateTo.getDate()+
						'</span></a>');
				trainObj.crossSection=sourceName+"—"+targetName;
				trainObj.versionId=trainList[i].parentId;
				trainObj.versionName=trainList[i].parentName;
				trainObj.sourceNodeName=sourceName;
				trainObj.targetNodeName=targetName;
				
				$a.data("trainObj",trainObj);
				$result.append($a);
			}
			
			//绑定click事件
			$("#result a").click(function(){
				$("#result a.active").removeClass("active");
				$(this).addClass("active");
			});
		}
		
	});
}

/**
 * 确定添加车次
 */

function confirmedTrain(){
	var selectedTrain = $($("#result a.active")[0]).data("trainObj");
	var trainList = $("#numberDetail");
	var index = trainList.children().length+1;
	var $liList = $('<li class="row"><label class="control-label pull-left"><span class="indexOfTrain">' + index + '</span><span class="trainNo">．'+selectedTrain.trainName+ '</span><span style="margin-left:20px;">'+selectedTrain.crossSection+'</span></label>'+
    '<div class="pull-right" style="margin-right:20px;"><button class="btn btn-default btn-sm redBtn" onclick="deleteTrainNumber(this.parentNode.parentNode);">删除</button>'+
    '<button class="btn btn-default btn-sm" style="margin-left:45px;" onclick="upRow(this.parentNode.parentNode);">上移</button>'+
    '<button class="btn btn-default btn-sm" style="margin-left:20px;" onclick="downRow(this.parentNode.parentNode);">下移</button></div></li>');
	$liList.data("trainObj",selectedTrain);
	trainList.append($liList);
	//更新交路名
	var crossName = $("#crossName").val();
	if(crossName===""){
		crossName = selectedTrain.trainName;
	}else{
		crossName +="-"+selectedTrain.trainName
	}
	$("#crossName").val(crossName);
	//$('#addTrainDlg').dialog('close');
}

/**
 * 保存交路车次
 */
function saveCross(){
	var check = validateCross();
	if(check>0){
		alert("选择的前车终到与后车始发不一致，不能保存。");
		//$("#numberDetail li:eq("+(check-1)+")").addClass("redWarn");
		//$("#numberDetail li:eq("+check+")").addClass("redWarn");
		return;
	}else if(check===-2){//交路收尾站点不一致
		alert("交路首尾站点不一致，不能保存。");
		return;
	}else if(check===-1){ //没有车次，直接返回
		return;
	}
	var crossName = $("#crossName").val();
	var trainList = [];
	$("#numberDetail li").each(function(index){
		var trainOriginal = $(this).data("trainObj");
		var train=new Object();
		train.trainSort = index+1;
		train.trainName=trainOriginal.trainName;
		train.cmOriginalCrossId=trainOriginal.crossId;
		train.cmOriginalTrainId = trainOriginal.trainId;
		train.alternateDate = trainOriginal.alternateDate;
		train.alternateTrainNbr = trainOriginal.alternateTrainNbr;
		train.highlineRule = trainOriginal.highlineRule;
		train.appointWeek = trainOriginal.appointWeek;
		train.appointDay = trainOriginal.appointDay;
		train.appointPeriod = trainOriginal.appointPeriod;
		train.commonlineRule = trainOriginal.commonlineRule;
		train.sourceNode = trainOriginal.sourceNodeName;
		train.targetNode = trainOriginal.targetNodeName;
		train.sourceTime = trainOriginal.sourceTime;
		train.targetTime = trainOriginal.targetTime;
		train.versionId = trainOriginal.versionId;
		train.versionName = trainOriginal.versionName;
		train.executionSourceTime = trainOriginal.executionSourceTime;
		train.executionTargetTime = trainOriginal.executionTargetTime;
		train.crossSection = trainOriginal.crossSection;
		trainList.push(train);
	})
	var data = {
		cross:{crossName:crossName,cmVersionId:$("#cmVersionId").val(), cmOriginalCrossId : $("#crossId").val(),tokenVehBureau:$("#tokenVehBureau").val()},
		trains:trainList,
		deleteIds:crossManageData.deleteList
	}
	var url = "/cmOriginalCross/addCrossTrain";
	if(crossManageData.deleteList.length>0 || $("li[isexist]").length>0){ //修改，更新
		url = "/cmOriginalCross/updateCrossTrain";
	}
	$.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + url,
		data:JSON.stringify(data),
		success:function(data){
			if(data.code===0){
				alert("保存成功。");
				closeSlideBlock();
				crossManageData.$table.datagrid('reload');
			}else{
				alert(data.message);
			}
		}
	});
	crossManageData.deleteList = []; 
}

/**
 * 验证交路是否合理
 * @returns {Boolean}
 */
function validateCross(){
	var crossSectionArray = []; 
	$("#numberDetail li").each(function(index){
		var trainObj = $(this).data("trainObj");
		crossSectionArray.push(trainObj);
	});
	if(crossSectionArray.length===0){//没有车次，返回-1
		return -1;
	}
	var from = crossSectionArray[0].sourceNodeName;
	var to = crossSectionArray[0].targetNodeName;
	var toLast =crossSectionArray[crossSectionArray.length-1].targetNodeName;
	if(from!==toLast){ //交路不闭合，返回-2
		return -2;
	}
	var toComp = to; //逐一比较判断交路是否连续。
	
	for(var i=1;i<crossSectionArray.length;i++){
		var fromT = crossSectionArray[i].sourceNodeName;
		if(toComp!==fromT){
			return i;
		}
		toComp = crossSectionArray[i].targetNodeName;
	}
	return 0;
}
