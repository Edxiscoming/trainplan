/**
 * Created by Administrator on 2015/10/26.
 */

var crossManageData={}; //我的全局变量
$(function(){
	// resizeTable();
	getPlan();
	initHeaderBureau();

	 $("#jianTab").on('shown.bs.tab', function(){
        crossManageData.$jianTable.datagrid('resize',{"width":"100%"});
      });
      $("#xiangTab").on('shown.bs.tab', function(){
        crossManageData.$xiangTable.datagrid('resize',{"width":"100%"});
      });
})
 
//初始化table
function initTable(){
	var columns =[[
	       	    
	       	    {field:'cmPartOriginalCrossId',rowspan:2,hidden:true},
	       	    {field:'alternateDate',rowspan:2,hidden:true},
	       	    {field:'cmCrossId',rowspan:2,hidden:true},
	       	    {field:'appointWeek',rowspan:2,hidden:true},
	       	    {field:'appointDay',rowspan:2,hidden:true},
	       	    {field:'appointPeriod',rowspan:2,hidden:true},
	       		{field:'crossName',title:'车底交路',width:22,align:"left",rowspan:2,sortable:true},
	       		{field:'commonlineRule',title:'开行规律',width:15,align:"left",rowspan:2,sortable:true,formatter:function(value,row,index) {
       		        var html = row.commonlineRule!=="null"&&row.commonlineRule!==null?row.commonlineRule:"";
       		        if(html!=="")
       		        {
       		        	html=html.replace(/1/g, "每日").replace(/2/g, "隔日");
       		        	var htmlArr=html.split("-");
		        			var flag=true;//判断是否所有车次都一样
		        			for(var i=0;i<htmlArr.length-1;i++)
		        			{
		        				if(htmlArr[i]!=htmlArr[i+1])
		        				{	
		        					flag=false;
		        					break;
		        				}
		        			}
		        			if(flag==true)
		        				html=htmlArr[0];
       		        }
       		        var ruleTitle = "普线规律：";
       		        if(html===""){
       		        	html = row.appointWeek!=="null"&&row.appointWeek!==null?row.appointWeek:"";
       		        	if(html!=="")
       		        	{
       		        		htmlArr=html.split("-");
       		        		//把字符串中的1换成位置
       		        		for(var i =0;i<htmlArr.length;i++)
       		        		{
       		        			var from=0;
       		        			for(var j=0;j<htmlArr[i].length;j++)
       		        			{
       		        				from=htmlArr[i].indexOf("1", j);
       		        				if(from>=0)
       		        				{
       		        					htmlArr[i]=htmlArr[i].substring(0,from) +(from+1)+ htmlArr[i].substring(from+1,htmlArr[i].length);
       		        				}
       		        			}
       		        		}
       		        		html="";
       		        		for(i =0;i<htmlArr.length;i++)
       		        		{
       		        			html+="-"+htmlArr[i];
       		        		}
       		        		html=html.substring(1).replace(/0/g,"").replace(/1/g,"周一").replace(/2/g,"周二").replace(/3/g,"周三").replace(/4/g,"周四").replace(/5/g,"周五").replace(/6/g,"周六").replace(/7/g,"周日");
       		        		htmlArr=html.split("-")
       		        		var flag=true;//判断是否所有车次都一样
		        			for(var i=0;i<htmlArr.length-1;i++)
		        			{
		        				if(htmlArr[i]!=htmlArr[i+1])
		        				{	
		        					flag=false;
		        					break;
		        				}
		        			}
		        			if(flag==true)
		        				html=htmlArr[0];
       		        	}
       		        	ruleTitle = "指定星期：";
       		        	if(html===""){
       		        		html = row.appointDay!=="null"&&row.appointDay!==null?row.appointDay:"";
       		        		if(html!=="")
       		        		{
       		        			var htmlArr=html.split("-");
       		        			var flag=true;//判断是否所有车次都一样
       		        			for(var i=0;i<htmlArr.length-1;i++)
       		        			{
       		        				if(htmlArr[i]!=htmlArr[i+1])
       		        				{	
       		        					flag=false;
       		        					break;
       		        				}
       		        			}
       		        			if(flag==true)
       		        				html=htmlArr[0];
       		        		}
       		        		ruleTitle = "指定日期：";
       		        		if(html===""){
       		        			html = row.appointPeriod!=="null"&&row.appointPeriod!==null?row.appointPeriod:"";
       		        			ruleTitle = "指定周期："; 
       		        		}
       		        	}
       		        }
       		        return ruleTitle+html;
       		    }},
       		    {field:'spareFlag',title:'开行状态',width:10,align:"center",rowspan:2,sortable:true,
       		    	formatter:function(value,row,index){
	            		   if(value==1){
	            			   return "开行";
	            		   }
	            		   else if(value==0)
	            			   return "停运";
	            		   else if(value==2)
	            			   return "备用";
	            		   else
	            			   return "";
	            	   }
       			},
	       		{field:'pairNbr',title:'对数',width:5,align:"center",rowspan:2,sortable:true,
	       			formatter:function(value,row,index) {
	       				var html = value;
//	       		        if(value==0 || !value){
//	       		            html= "<input type=text name='pairNbr' style='width:25px'>";
//	       		        }
//	       		        else html="<input type=text name='pairNbr' style='width:25px;text-align:center' value="+value+">";
	       		        return html;
	       		    }
       		    },
	       		{field:'groupTotalNbr',title:'组数',width:5,align:"center",rowspan:2,sortable:true,
	       			formatter:function(value,row,index) {
	       				var html = value;
//	       		        if(value==0 || !value){
//	       		            html= "<input name='groupTotalNbr' type=text style='width:25px'>";
//	       		        }
//	       		        else html="<input name='groupTotalNbr' type=text style='width:25px;text-align:center' value="+value+">";
	       		        return html;
	       		    }
	       		},
	       		{title:'审核状态',colspan:2,align:"center"},
//	       		{field:'createCrossFlag',title:'生成状态',width:5,align:"center",rowspan:2,sortable:true,
//	       			formatter:function(value,row,index) {
//	       		        var html = "";
//	       		        if(value){
//	       		            html= '<div style="background:#367ab4;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">已</div>';
//	       		        }else{
//	       		            html = '<div style="background:#ef6b00;border-radius:10px; width:30px;text-align: center;margin: 0px auto;color: #FFFFFF;">未</div>'
//	       		        }
//	       		        return html;
//	       		    }
//	       		}
	       		/*,
	       		{field:'allCheckFlag',title:'相关局审核',width:10,align:"center",sortable:true,
	       			formatter:function(value,row,index) {
	       		        var html = "";
	       		        if(value == 1){
	       		            html= '<div style="background:#48a048;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">已</div>';
	       		        }else {
	       		            html = '<div style="background:#ef6b00;border-radius:10px; width:30px;text-align: center;margin: 0px auto;color: #FFFFFF;">未</div>';
	       		        }
	       		        return html;
	       		    }
	       		}*/
	       	    ],[
                   {field:'relevantBureau',title:'相关局',width:20,align:"center",sortable:true,
	       			formatter:function(value,row,index) {
	       				//return getBureauS(value);
	       				//所有经由局
	       				//var allRelevantBureau = getBureauS(row.relevantBureau);
	       				//审核通过的局
	       				//var passRelevantBureau = getBureauS(row.check_t);
	       				//审核不通过的局
	       				//var NotPassRelevantBureau = getBureauS(row.check_b);
	       				//未审核的局
	       				var notAuditRelevantBureau = getBureauS(row.check_n);
	       				var html='';
	       				if(row.check_t!="无")
	       				{
	       					for(var i =0;i<row.check_t.length;i++)
	       				
	       					{
	       						html+='<span style="background:#48a048;border-radius:20px; width:30px; text-align: center;margin:0px 1px;color: #FFFFFF;padding:5px;">'+getBureau(row.check_t.charAt(i))+'</span>'
	       					}
	       				}
	       				if(row.check_b!="无")
	       				{
	       					for(i =0;i<row.check_b.length;i++)
	       					{
	       						html+='<span style="background:#ef6b00;border-radius:20px; width:30px; text-align: center;margin: 0px 1px;color: #FFFFFF;padding:5px;">'+getBureau(row.check_b.charAt(i))+'</span>'
	       					}
	       				}
	       				if(row.check_n!="无")
	       				{
	       					html+=notAuditRelevantBureau;
	       				}
	       				return html;
	       				
	       				
	       			}
	       		}
//	       		{field:'checkFlag',title:'本局',width:8,align:"center",sortable:true,
//	       			formatter:function(value,row,index) {
//	       		        var html = "";
//	       		        if(row.check_wor == 1){
//	       		            html= '<div style="background:#48a048;border-radius:10px; width:30px; text-align: center;margin: 0px auto;color: #FFFFFF;">已</div>';
//	       		        }else {
//	       		        	html = '<div style="text-align: center;">未</div>';
//	       		        }
//	       		        return html;
//	       		    }
//	       		},
                   ]];
			//查询条件
			var cmVersionId = $("#cmVersionId").val();
			var cmOriginalTrainId = $("#cmOriginalTrainId").val();
		
			var paras = {
					cmVersionId : cmVersionId,
//					highlineFlag:"-1"
					crossNamecheckflag:1,
					tokenVehBureau : currentUserBureau,
					//tokenFlag: 0,
					createCrossFlag: 1,
					exceptionflag: 0
			};
	       	var url = basePath + "/cmOriginalCross/pageQueryCross";
	       	crossManageData.$table = creatGrid($("#mcrossTable"), columns, url, paras, onClickRow);
	       	crossManageData.$table.datagrid("options").nowrap = false;
	       	crossManageData.$table.datagrid("options").singleSelect = true;
	      
	       
}

/**
 * 点击行显示右边运行图、时刻表、交路属性
 */
function onClickRow (row) {
	var cmCrossId = row.cmCrossId;
	var cmVersionId = row.cmVersionId;
	var cmOriginalCrossId = row.cmOriginalCrossId;
	//所有经由局
	var allRelevantBureau = getBureauS(row.relevantBureau);
	//审核通过的局
	var passRelevantBureau = getBureauS(row.check_t);
	//审核不通过的局
	var NotPassRelevantBureau = getBureauS(row.check_b);
	//未审核的局
	var notAuditRelevantBureau = getBureauS(row.check_n);
	$("#allRelevantBureau").html(allRelevantBureau);
	$("#passRelevantBureau").html(passRelevantBureau);
	$("#NotPassRelevantBureau").html(NotPassRelevantBureau);
	$("#notAuditRelevantBureau").html(notAuditRelevantBureau);
	
	if(row.check_wor == 1){
		$("#isRuningChat").html("运行图");
	}else{
		$("#isRuningChat").html("预览图");
	}

	if (!(typeof(cmCrossId) == "undefined" || cmCrossId == "" || cmCrossId == null)) {
		$("#crossGraphAlert").empty();
		showDetail (cmCrossId);
		showunittrain(cmCrossId);
		showGraph(cmCrossId,row.crossName,cmVersionId);
	}else{
		$("#crossGraphAlert").empty();
		 $("#plan_construction_input_trainNbr2").text("");
            $("#undertake2").text("");
            $("#groupData2Span").text("")
            $("#openState2").text("");
            $("#mold2").text("");
            $("#relaceDate2").text("");
            $("#replace_train_number2").text("");
            $("#normalRule2").text("");
            $("#week2").text("");
            $("#date2").text("");
            $("#cycle2").text("");
            $("#runSection2").text("");
            $("#runDistance2").text("");
            $("#level2").text("");
            $("#pairNbr2").val("");
            $("#orgnize2").text("");
            $("#createPeople2").text("");
            $("#orgnizeContent2").text("");
            $("#groupLevel2").text("");
            $("#electricity2").text("");
            $("#mass2").text("");
            $("#airconditioner2").text("");
            $("#remark2").text("");
            $("#groupTotalNbr2").text("");
            $("#crossNameTitle").text('');
            showunittrain('');
            $("#crossGraph").find("iframe").attr("src","");
            openGraph(cmOriginalCrossId);
	} 
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
	var checkFlag=$("#checkFlag:checked");
	if(checkFlag.length)
		checkFlag=1;
	else
		checkFlag='';
	var tokenVehBureau=currentUserBureau;
	if (tokenFlag===1) {
		tokenVehBureau = $("#tokenVehBureau").val();
	};
	var cmOriginalTrainId=$("#cmOriginalTrainId").val();
	var paras = {
			cmVersionId : cmVersionId,
			tokenVehBureau : tokenVehBureau,
			crossName:cmOriginalTrainId,
			crossNamecheckflag:checkFlag,
			createCrossFlag: 1,
			//tokenFlag: tokenFlag,
			exceptionflag: 0
	};
	//console.log(paras);
	crossManageData.$table.datagrid("load",paras);
}

function showDetail (cmCrossId) {
    $.ajax({
        type: "post",
        url: basePath + "/cmcross/getCmUnitCrossTrainInfoDetail",
        data: JSON.stringify({crossId : cmCrossId}),
        dataType: "json",
        contentType : "application/json",
        success: function (result) {
            var data = result.data[0].oCrossinfo;
            $("#plan_construction_input_trainNbr2").text(data.crossName!==null?data.crossName:"");
            $("#undertake2").text(data.tokenVehBureau?getBureau(data.tokenVehBureau):"");
            $("#groupData2Span").text(data.groupTotalNbr!==null?data.groupTotalNbr:"")
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
            $("#crossNameTitle").text(data.crossName);
            
        },
        error: function () {
            openStatus("获取信息失败！");
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
      {field:'useStatus',title:'时刻表',width:10,align:"center",sortable:true,
      	formatter:function (value,row,index) {
      		var html = '<span style="color:#179edd;">时刻表</span>';
          	return html;
        }
  	}
  ]];
  
  var paras = {crossId:cmCrossId};
  var url = basePath + "/cmcross/getCmUnitCrossTrainInfo";
    crossManageData.$unitTable = creatGrid($("#cross_trainInfo"), columns, url, paras,clickUnitRow); 
}

/**
*显示“运行图”
*/
function showGraph (cmCrossId,cmCrossName,cmVersionId) {
	var chartId=cmVersionId;
	$("#crossGraph").find("iframe").attr("src",basePath+"/drawCross/provideCrossChartData?crossName="+cmCrossName+"&crossId="+cmCrossId+"&chartId="+chartId);
}

/**
 * 修改保存
 */


function changeData(){
	 var checkedrows=$('#mcrossTable').datagrid("getSelections");
	 
	 var dateArray=new Array();
	 if (checkedrows.length == 0) {
	 	return;
	 };
	 for(var i=0;i<checkedrows.length;i++) {
	 	if(!checkedrows[i].createCrossFlag&&!checkedrows[i].checkFlag)
			 {
				 var obj=new Object();
				 var rowIndex = $('#mcrossTable').datagrid("getRowIndex",checkedrows[i]); 
				 obj.cross={
						 cmOriginalCrossId:checkedrows[i].cmOriginalCrossId,
						 pairNbr:$("input[name='pairNbr']")[rowIndex].value,
						 groupTotalNbr:$("input[name='groupTotalNbr']")[rowIndex].value
				 }
				 
				 dateArray.push(obj);
			 } else {
			 	alertMessage("warning","审核状态和生成状态都必须是未才能保存");
			 }
	 }

	$.ajax({
	type: "POST",
   dataType: "json",
   contentType : "application/json",
	url: basePath + "/cmOriginalCross/updateCrosses",
	data: JSON.stringify(dateArray),
	success:function(data){
		if(data.code==0)
		{
		 alertMessage("success","保存成功")
		 crossManageData.$table.datagrid("reload");
		}
		else{
			alertMessage("warning","保存失败");
		}
	}
	
	})
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
 * 批量审核
 */
function checkData(){
		var crossIds = "";
		var updateCrosses = [];
		var crosses = crossManageData.$table.datagrid('getSelections')
		if (crosses.length < 1) {
			return;
		}; 
		for(var i = 0; i < crosses.length; i++){
			if(crosses[i].checkFlag == 1 || crosses[i].checkFlag == -1){  
				alertMessage("warning","不能重复审核"); 
				return;
			}else if(crosses[i].checkFlag == 0){
				crossIds += (crossIds == "" ? "" : ",");
				crossIds += crosses[i].cmOriginalCrossId;
				updateCrosses.push(crosses[i]); 
			} 
			if (typeof(crosses[i].pairNbr) == "undefined" || crosses[i].pairNbr == "" || crosses[i].pairNbr == null) {
				alertMessage("warning","对数为空，不能审核！");
				return;
			};
		}  
		if(crossIds == ""){
			alertMessage("warning","没有可审核的");
			return;
		}
		 $.ajax({
				url : basePath+"/cmOriginalCross/checkCross",
				cache : false,
				type : "POST",
				dataType : "json",
				contentType : "application/json",
				data :JSON.stringify({  
					crossIds : crossIds,
					currentUserBureau:currentUserBureau
				}),
				success : function(result) {     
					if(result.code == 0){
						/*$.each(updateCrosses, function(i, n){ 
							n.checkFlag = 1;
						});*/
						alertMessage("success","审核成功！");
						crossManageData.$table.datagrid('reload');
					}else{
						}
					},
				error : function() {
					alertMessage("warning","审核失败！");
				},
				complete : function(){
				}
			}); 
			
}

/**
*批量删除
*/

function crossesDelete(){
	if(!confirm("是否确认删除"))
		return false;
	var rows = crossManageData.$table.datagrid("getChecked");
	if(rows.length<1){
		return;
	}
	var toDelete="";
	for(var i=0;i<rows.length;i++)
	{
		toDelete+=","+rows[i].cmOriginalCrossId;
	}
	toDelete=toDelete.substring(1);
	$.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/cmOriginalCross/delete",
		data:JSON.stringify({
			crossIds:toDelete
		}),
		success:function(data){
			//alert(data.message)	
//			alertMessage("success",data.message);
			crossManageData.$table.datagrid('reload');
		}
	});
}

//得到不为空的开行规律
function getRules (row) {
	var rules = "";
	if (row.commonlineRule && row.commonlineRule != "null") {
		rules = row.commonlineRule;
	} else {
		if (row.appointWeek && row.appointWeek != "null") {
			rules = row.appointWeek;
		} else {
			if (row.appointDay && row.appointDay != "null") {
				rules = row.appointDay;
			} else {
				if (row.appointPeriod && row.appointPeriod) {
					rules = row.appointPeriod;
				} else {
					rules = "--";
				}
			}
		}
	}
	return rules;
}

/**
*预览交路
*/
function openGraph(cmOriginalCrossId){
	var cross=crossManageData.$table.datagrid('getSelections');
	for(var i =0;i<cross.length;i++)
	{
		if(cross[i].groupTotalNbr < 1) {
			$("#crossGraphAlert").text("组数为零不能预览交路");
			return;
		}
		
		if (typeof(cross[i].alternateDate) == "undefined" || cross[i].alternateDate == "" || cross[i].alternateDate == null) {
			$("#crossGraphAlert").text("交替日期为空，不能预览交路！");
			return;
		}
		var reg=/^\d{8}(-\d{8})*$/;
		if (!(reg.test(cross[i].alternateDate))){
			$("#crossGraphAlert").text("交替日期不完整，不能预览交路！");
			return;
		}
		if (typeof(cross[i].commonlineRule) == "undefined" || cross[i].commonlineRule == "" || cross[i].commonlineRule == null) {
			$("#crossGraphAlert").text("开行规律未填，不能预览交路！");
			return;
		} 
		if (getRules(cross[i]).indexOf("--") > -1) {
			$("#crossGraphAlert").text("开行规律填写不正确，不能预览交路！");
			return;
		};
	}
	$("#crossGraph").find("iframe")[0].src="/trainplan/drawCross/orginalCrossChartData?orginalCrossId="+cmOriginalCrossId;
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
			alertMessage("warning","已生成的交路不能重复生成。");
			return;
		}
		if (cross[i].allCheckFlag  != 1) {
			alertMessage("warning","必须要全部相关局审核通过才能生成交路！");
			return;
		}
		if(cross[i].groupTotalNbr < 1) {
			alertMessage("warning","组数为零不能生成交路！");
			return;
		}
		if (typeof(cross[i].alternateDate) == "undefined" || cross[i].alternateDate == "" || cross[i].alternateDate == null) {
			alertMessage("warning","交替日期为空，不能生成交路！");
			return;
		}
		var reg2=/^\d{8}(-\d{8})*$/;
		if (!(reg2.test(cross[i].alternateDate))) {
			alertMessage("warning","交替日期不完整，不能预览交路！");
			return;
		}
		if (typeof(cross[i].commonlineRule) == "undefined" || cross[i].commonlineRule == "" || cross[i].commonlineRule == "null") {
			alertMessage("warning","开行规律未填不能生成交路！");
			return;
		}
		if (getRules(cross[i]).indexOf("--") > -1) {
			alertMessage("warning","开行规律填写不完整，不能生成交路！");
			return;
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
//			$("#headerBureau").text(getBureau(currentUserBureau));
			 $("#tokenVehBureau option[value='"+currentUserBureau+"']").attr("selected","selected");
		}
		
	});
}

/**
*点击“列车信息（交）右上角“时刻表”
*/
function clickUnitRow (row) {
	var timeId = row.baseTrainId;
	timeOpen(timeId,row.trainNbr);
    $("#crossHideId").val(timeId);
    $('#timeTable').show().dialog({
	      title: '时刻表:车次：' + row.trainNbr +"&nbsp;&nbsp;"+ row.startStn + '——' + row.endStn,
	      width:1000,
	      height:800,
	      cache: false,
	      modal: true
	  });
	  
	  $('#timeTable').dialog('open');

}

//时刻表
function timeOpen(baseTrainId,trainNbr) {
  var timeId = baseTrainId;
  if(timeId==""){
	  	alert("请先选择车次！");
		return;
  }
  var columns =[[
    {field:'childIndex',title:'序号',rowspan:2,width:4,align:"center",
    	formatter:function(value,row,index){
    	return index+1;
    }},
    {field:'nodeName',title:'车站',rowspan:2,width:10,align:"center"},
    {field:'bureauShortName',title:'路局',rowspan:2,width:4,align:"center",sortable:true},
    {field:'arrTime',title:'到达时间',rowspan:2,width:10,align:"center",sortable:true},
    {field:'dptTime',title:'出发时间',rowspan:2,width:10,align:"center",sortable:true},
    {title:'车次',colspan:2,align:"center"},
    {title:'天数',colspan:2,align:"center"},
    {field:'trainlineTempId',title:'停时',rowspan:2,width:10,align:"center",sortable:true,
 	  formatter:function(value,row,index){
         	return GetDateDiff(row);
      }
    	},
    {field:'dptTrainNbr',title:'股道',rowspan:2,width:5,align:"center",sortable:true},
    {field:'trackName',title:'站台',rowspan:2,width:5,align:"center",sortable:true},
    {field:'planForm',title:'作业',rowspan:2,width:6,align:"center",sortable:true},
    {field:'nodeTdcsName',title:'办客',rowspan:2,width:10,align:"center",sortable:true,
    	formatter:function(value,row,index){
     	return "";
    }},
    /*{field:'kyyy',title:'普速规律',rowspan:2,width:10,align:"center",sortable:true},
    {field:'nodeTdcsId',title:'交替时间',rowspan:2,width:8,align:"center",sortable:true,
    	formatter:function(value,row,index){
         	return '';//先不展示
      }},*/
    
    ],[
      {field:'source_TIME_SCHEDULE_HOUR',title:'到达',width:5,align:"center"},
      {field:'source_TIME_SCHEDULE_MINUTE',title:'出发',width:5,align:"center"},
      {field:'target_TIME_SCHEDULE_MINUTE',title:'到达',width:5,align:"center",
    	  formatter:function(value,row,index){
      		return row.stationFlag=='ZDZ'?'--':row.runDays;
      	  }
      },
      {field:'target_TIME_SCHEDULE_SECOND',title:'出发',width:5,align:"center",
    	  formatter:function(value,row,index){
      		return row.stationFlag=='SFZ'?'--':row.arrRunDays;
      	}
      },
      ]
    ];
  var jianColumns = [[
                      {field:'childIndex1',title:'序号',rowspan:2,width:4,align:"center",
                      	formatter:function(value,row,index){
                      	return index+1;
                      }},
                      {field:'nodeName1',title:'车站',rowspan:2,width:10,align:"center"},
                      {field:'bureauShortName1',title:'路局',rowspan:2,width:4,align:"center",sortable:true},
                      {field:'arrTime1',title:'到达时间',rowspan:2,width:10,align:"center",sortable:true},
                      {field:'dptTime1',title:'出发时间',rowspan:2,width:10,align:"center",sortable:true},
                      {title:'车次',colspan:2,align:"center"},
                      {title:'天数',colspan:2,align:"center"},
                      {field:'trainlineTempId1',title:'停时',rowspan:2,width:10,align:"center",sortable:true//,
//                    	  formatter:function(value,row,index){
//                           	return trainNbr;
//                        }
                      },
                      {field:'dptTrainNbr1',title:'股道',rowspan:2,width:5,align:"center",sortable:true},
                      {field:'trackName1',title:'站台',rowspan:2,width:5,align:"center",sortable:true},
                      {field:'planForm1',title:'作业',rowspan:2,width:6,align:"center",sortable:true},
                      {field:'nodeTdcsName1',title:'办客',rowspan:2,width:10,align:"center",sortable:true,},
                     /* {field:'kyyy1',title:'普速规律',rowspan:2,width:10,align:"center",sortable:true},
                      {field:'nodeTdcsId1',title:'交替时间',rowspan:2,width:8,align:"center",sortable:true},*/
                      
                      ],[
                        {field:'source_TIME_SCHEDULE_HOUR1',title:'到达',width:5,align:"center"},
                        {field:'source_TIME_SCHEDULE_MINUTE1',title:'出发',width:5,align:"center"},
                        {field:'target_TIME_SCHEDULE_MINUTE1',title:'到达',width:5,align:"center"},
                        {field:'target_TIME_SCHEDULE_SECOND1',title:'出发',width:5,align:"center"},
                        ]
                      ];
  //var timeId = "ff8a020a-2b64-48be-8e7b-30c6e41c438a";
  var paras = {baseTrainId:timeId};
  var url = basePath + "/cmcross/queryCmTrainTimesDepands";
    
  crossManageData.$xiangTable = creatTimeGrid_detail($("#timedetailinfo_detail"), columns, url, paras);
  crossManageData.$xiangTable.datagrid({onLoadSuccess:function(data){
	  	var jianItems =[];
	  	var xiangItem = data.rows;
	  	for(var i=0;i<xiangItem.length;i++){
	  		if(xiangItem[i].stationFlag!="BTZ"){
	  			var jianItem = new Object(); //防止详点和简点列表的列名相同导致的异常
	  			jianItem.childIndex1= xiangItem[i].childIndex;
	  			jianItem.nodeName1= xiangItem[i].nodeName;
	  			jianItem.bureauShortName1= xiangItem[i].bureauShortName;
	  			jianItem.arrTime1= xiangItem[i].arrTime;
	  			jianItem.dptTime1= xiangItem[i].dptTime;
	  			jianItem.trainlineTempId1= GetDateDiff(xiangItem[i]);//先占位[i].trainlineTempId;
	  			jianItem.dptTrainNbr1= xiangItem[i].dptTrainNbr;
	  			jianItem.trackName1= xiangItem[i].trackName;
	  			jianItem.planForm1= xiangItem[i].planForm;
	  			jianItem.nodeTdcsName1= '';//xiangItem[i].nodeTdcsName;
	  			jianItem.kyyy1= xiangItem[i].kyyy;
	  			jianItem.nodeTdcsId1= '';//先不显示xiangItem[i].nodeTdcsId;
	  			jianItem.source_TIME_SCHEDULE_HOUR1= xiangItem[i].source_TIME_SCHEDULE_HOUR;
	  			jianItem.source_TIME_SCHEDULE_MINUTE1= xiangItem[i].source_TIME_SCHEDULE_MINUTE;
	  			jianItem.target_TIME_SCHEDULE_MINUTE1= xiangItem[i].stationFlag=='ZDZ'?'--':xiangItem[i].runDays;
	  			jianItem.target_TIME_SCHEDULE_SECOND1= xiangItem[i].stationFlag=='SFZ'?'--':xiangItem[i].arrRunDays;
	  			jianItems.push(jianItem);
	  		}
//	  		data.rows[i].trainlineTempId = GetDateDiff(xiangItem[i]);
//	  		data.rows[i].target_TIME_SCHEDULE_MINUTE = xiangItem[i].stationFlag=='ZDZ'?'--':xiangItem[i].runDays;
//	  		data.rows[i].target_TIME_SCHEDULE_SECOND = xiangItem[i].stationFlag=='SFZ'?'--':xiangItem[i].arrRunDays;
//	  		data.rows[i].nodeTdcsName = '';
	  		//data.rows.push(xiangItem[i]);
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

}

function GetDateDiff(data)
{ 
	 if(data.childIndex == 0 
			 || data.dptTime == '-' 
			 || data.dptTime == null 
			 || data.arrTime == null){
		return "";
	} 
	var startTime = new Date(data.arrTimeAll);
	if(data.arrRunDays != null){
		startTime.setDate(startTime.getDate() + data.arrRunDays);
	}  
	var endTime = new Date(data.dptTimeAll);   
	if(data.targetDay != null){
		endTime.setDate(endTime.getDate() + data.targetDay);
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