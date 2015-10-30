var crossGloble = {} //交路信息的全局变量

$(function(){
	resizeTable();
	getPlan();
	initHeaderBureau();
})

function showDetail (crossId, trainId) {
    
    $.ajax({
        type: "post",
        url: basePath + "/cmOriginalCross/queryCrossTrainDetail",
        data: JSON.stringify({crossId : crossId, trainId : trainId}),
        dataType: "json",
        contentType : "application/json",
        success: function (json) {
            var data = json.data;
            $("#plan_construction_input_trainNbr").val(data.cross.crossName);
            $("#trainName").val(data.train.trainName);
            $("#crossSection").val(data.train.crossSection);
            for(var i=0;i<data.trainList.length;i++)
            {
            	if(data.trainList[i].cmOriginalTrainId==trainId)
            	{    var train = data.trainList[i];
            		 $("#cmOriginalCrossId").val(train.cmOriginalCrossId);
            		 $("#trainSort").val(train.trainSort);
            		 $("#crossLevel").val(train.crossLevel);
                     $("#pairNbr").val(train.pairNbr);
                     $("#tokenVehBureau").val(train.tokenVehBureau);
                     $("#tokenVehBureauShow").val(getBureau(train.tokenVehBureau));
                     $("#throughLine").val(train.throughLine);
                     $("#locoType").val(train.locoType);
                     //开行规律类型和数值 
                     $("div.tab-content div.tab-pane").removeClass("active");
                     $("ul.nav-tabs li").removeClass("active");
                     var appointType = "commonTab";
                     var appointValue = train.commonlineRule ||train.appointWeek||train.appointDay||train.appointPeriod||1;
                     if(train.commonlineRule===null){
                    	 if(train.appointWeek!==null){
                        	 appointType = "weekTab";
                        	 $("a[aria-controls='weekTab']").closest("li").addClass("active");
                        	 $("#weekTab").addClass("active");
                        	 $("input[name='appointWeek']").each(function(index) {
                        		 if(train.appointWeek.charAt(index)==="1"){
                        			 $(this).attr("checked",true);
                        		 }
                        	 });
                         }else{
                        	 if(train.appointDay!==null){
                        		 var appointDay = train.appointDay;
                            	 appointType = "dateTab";
                            	 $("a[aria-controls='dateTab']").closest("li").addClass("active");
                            	 $("#dateTab").addClass("active");
                            	 if(appointDay.indexOf("0")<0){
                            		 var days = appointDay.split(",");
                            		 $("#apointDaySolid").val(days);
                            	 }else{
                            		 appointDay = appointDay.substr(0,4)+"-"+appointDay.substr(4,2)+"-"+appointDay.substr(6,2);
                            		 $("#apointDayDateSolid").val(appointDay);
                            	 }
                             }else{
                            	 if(train.appointPeriod!==null){
                            		 $("#cycleTab").addClass("active");
                            		 $("a[aria-controls='cycleTab']").closest("li").addClass("active");
                            		 appointType = "cycleTab";
                            		 var appointPeriod = train.appointPeriod;
                            		 if(/^1+0*$/.test(appointPeriod)){
                            			 var runDays=0;
                                		 var stopDays= 0;
                            			 for(var i=0;i<appointPeriod.length;i++){
                            				 if(appointPeriod.charAt(i)==="1"){
                            					 runDays ++;
                            				 }else{
                            					 stopDays ++;
                            				 }
                            			 }
                            			 $("#runDays").val(runDays);
                            			 $("#stopDays").val(stopDays);
                            		 }else{
                            			 $("#irregularRule").val(appointPeriod);
                            		 }
                            	 }else{
                            		 $("#commonTab").addClass("active");
                            		 $("a[aria-controls='commonTab']").closest("li").addClass("active");
                            	 }
                             }
                         }
                     }else{
                    	 $("#commonTab").addClass("active");
                    	 $("a[aria-controls='commonTab']").closest("li").addClass("active");
                    	 $("input[name='commonLineRule'][value='"+appointValue+"'").attr("checked",true);
                     }
                     $("#alternateTrainNbr").val(train.alternateTrainNbr);
                     var alternateDate = train.alternateDate;
                     if(!alternateDate||alternateDate==="null")
                     {
                    	 var now=new Date();
                    	 now=now.getFullYear()+"-"+(now.getMonth()+1)+"-"+now.getDate();
                    	 $("#alternateDate").val(now);
                     }
                     else{
                     var alternateDate = alternateDate.substr(0,4)+"-"+alternateDate.substr(4,2)+"-"+alternateDate.substr(6,2); 
                    	 $("#alternateDate").val(alternateDate);
                     }
                     $("#runState").val(train.spareFlag);
                     $("#note").val(train.note);
                     break;
            	}
            }

        },
        error: function () {
            openStatus("获取信息失败！");
        },
    });
    
}



function submitData(){
	//验证输入
	if(!document.getElementById("pairNbr").checkValidity()){
		alertMessage("warning","请输入正确的对数值");
		return false;
	}
//	if(!document.getElementById("operationValue").checkValidity()){
//		//alert("请输入正确的开行规律数值。");
//		alertMessage("warning","请输入正确的开行规律数值。");
//		return false;
//	}
	if(!document.getElementById("alternateTrainNbr").checkValidity()){
		//alert("请输入正确的交替车次。");
		alertMessage("warning","请输入正确的交替车次。");
		return false;
	}
	if(!document.getElementById("alternateDate").checkValidity()){
		alertMessage("warning","交替日期不能为空。");
		return false;
	}
	var cross ={
			cmOriginalCrossId:$("#cmOriginalCrossId").val(),
			crossName:$("#plan_construction_input_trainNbr").val(),
			trainName:$("#trainName").val()
	};
	var train={
			crossSection:$("#crossSection").val(),
			cmOriginalTrainId:$("#trainId").val(),
			crossLevel:$("#crossLevel").val(),
			trainSort:$("#trainSort").val(),
			pairNbr:$("#pairNbr").val(),
			tokenVehBureau:$("#tokenVehBureau").val(),
			throughLine:$("#throughLine").val(),
			alternateTrainNbr:$("#alternateTrainNbr").val(),
			alternateDate:$("#alternateDate").val().replace(/-/g,""),
			note:$("#note").val(),
			spareFlag:$("#runState").val()
	};
	
//	var locoType= $("#operationType").val();   
//	var operationValue = $("#operationValue").val();
//	switch(locoType){
//		case "appointPeriod" : train.appointPeriod = operationValue;train.appointDay=""; train.appointWeek = "";train.commonlineRule = "";train.highlineRule = ""; break;
//		case "appointDay" : train.appointDay = operationValue; train.appointPeriod = ""; train.appointWeek = "";train.commonlineRule = ""; train.highlineRule = "";break;
//		case "appointWeek" : train.appointWeek = operationValue;train.appointPeriod = ""; train.appointDay = "";train.commonlineRule = ""; train.highlineRule = "";break;
//		case "commonLineRule_everyday" : train.commonlineRule = operationValue;train.appointPeriod = ""; train.appointWeek = "";train.appointDay = "";train.highlineRule = "";break;
//		case "commonLineRule_subday" : train.commonlineRule = operationValue;train.appointPeriod = ""; train.appointWeek = "";train.appointDay = "";train.highlineRule = "";break;
//	}
//	
	//开行规律
	var runType = $("#runType div.active").attr("id");
	switch(runType){
		case "commonTab" : train.commonlineRule = $("input[name='commonLineRule']:checked").val();train.appointPeriod = ""; train.appointWeek = "";train.appointDay = "";train.highlineRule = "";break;
		case "weekTab" : 
			var operationValue="";
			$("input[name='appointWeek']").each(function(index){
				if($(this).is(":checked")){
					operationValue += "1";
				}else{
					operationValue += "0";
				}
			});
			if(operationValue==='0000000'){
				alertMessage("warning","开行规律指定周期必选一天");
				return;
			}
			train.appointWeek = operationValue;
			train.appointPeriod = ""; 
			train.appointDay = "";
			train.commonlineRule = "";
			train.highlineRule = "";
			break;
		case "dateTab" : 
			var operationValue = $("#apointDayDateSolid").val();
			if(operationValue===""){
				var tempDates = $("#apointDaySolid").val();
				for(var i=0;i<tempDates.length;i++){
					operationValue += ","+tempDates[i];
				}
				operationValue = operationValue.substr(1);
			}else{
				operationValue = operationValue.replace(/-/g,"");
			}
			if(operationValue ===""){
				alertMessage("warning","开行规律指定日期必选一天");
				return;
			}
			train.appointDay = operationValue; 
			train.appointPeriod = ""; 
			train.appointWeek = "";
			train.commonlineRule = ""; 
			train.highlineRule = "";break;
		case "cycleTab" : 
			var runDays = $("#runDays").val();
			var stopDays = $("#stopDays").val();
			var irregularRule = $("#irregularRule").val();
			var operationValue="";
			if(runDays !== "" &&runDays.match(/^\d+$/) &&stopDays!=="" && stopDays.match(/^\d+$/)){
				for(var i=0;i<runDays;i++){
					operationValue +="1";
				}
				for(var i=0;i<stopDays;i++){
					operationValue +="0";
				}
			}else{
				operationValue = irregularRule;
			}
			if(!/^1(1|0)*$/.test(operationValue)){
				alertMessage("warning","开行规律指定周期不合理！");
				return;
			}
			train.appointPeriod = operationValue;
			train.appointDay=""; 
			train.appointWeek = "";
			train.commonlineRule = "";
			train.highlineRule = ""; 
			break;
	} 
	//新增
	if(!$("#trainId").val())
	{
		fdata.substr(from, fdata.length-1);
		fdata="{"+fdata+"}";
		$.ajax(
		{
			type: "POST",
	        dataType: "json",
	        contentType : "application/json",
			url: basePath + "/partOriginalCross/addCross",
			data: JSON.stringify({result : fdata}),
			success:function(data){
				if(data.code===0){
					alertMessage("success","新增成功");
			    	crossGloble.$table.datagrid("reload");
			    }else{
			    	//alert(data.message);
			    	alertMessage("warning",data.message);
			    }
			}
			
		});
	
	}
	//修改
	else{
		$.ajax(
		{
			type: "POST",
	        dataType: "json",
	        contentType : "application/json",
			url: basePath + "/cmOriginalCross/updateTrainInfo",
			data: JSON.stringify({cross : cross, train : train}),
			success:function(data){
				if(data.code===0){
					//alert("保存成功。");
					alertMessage("success","保存成功。");
					closeSlideBlock();
			    	crossGloble.$table.datagrid("reload");
			    }else{
			    	//alert(data.message);
			    	alertMessage("warning",data.message);
			    }
			}
			
		});
	}
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
			//alert("获取信息失败！");
			alertMessage("warning","获取信息失败！");
		}
	});
}

/*
删除交路车次
*/
crossGloble.deleteList = []; //要删除的list
function deleteTrainNumber (obj) {
	if($(obj).attr("isexist")==="1"){
		crossGloble.deleteList.push($(obj).data("trainObj").trainId);
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
			var $cmVersionId2=$("#cmVersionId2");
			var $cmVersionDlg=$("#cmVersionId_dialog");
			$cmVersionId2.empty();
			$cmVersionId.empty();
			$cmVersionDlg.empty();
			for(var i=0; i<data.list.length;i++){
				option += '<option value="'+data.list[i].cmVersionId+'">'+data.list[i].name+'</option>';
			}
			$cmVersionId.append(option);
			$cmVersionId2.append(option);
			$cmVersionDlg.append(option);
			initTable(); //初始化table
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
			var $headerBureau=$("#tokenBureau");
			$headerBureau.empty();
			for(var i=0; i<data.data.length;i++){
				option += '<option value="'+data.data[i].ljpym+'">'+data.data[i].ljjc+'</option>';
				setBureau(data.data[i].ljpym,data.data[i].ljjc);//设置中英文对照。
			}
			$("#headerBureau").text(globelBureau[currentUserBureau]);
			$headerBureau.append(option);
			//$("#headerBureau option[value='"+currentUserBureau+"']").attr("selected","selected");
		}
		
	});
}

/**
 * 查询
 */
function searchCross(obj){
	//方案
	var cmVersionId = $("#cmVersionId").val();
	//担当局
	var headerBureau = currentUserBureau;
	//车次
	var trainNbr = $("#trainNbr").val();
	var checkFlag=$("#checkFlag:checked");
	if(checkFlag.length)
		checkFlag=1;
	else
		checkFlag='';
	//等级
	//var levelSelect = $("#levelSelect").val();
	//火车类型：全部、高铁/动车、普速
	
	var trainType = 1;
	
	if(obj!==undefined){
		$(".btn-group button.active").removeClass("active");
		$(obj).addClass("active");
		trainType = $(obj).val();
	}else{
		trainType=$(".btn-group button.active").val();
	}
	var param = {
		cmVersionId	:cmVersionId,
		tokenVehBureau : $("#tokenBureau").val(),
		tokenFlag : 1,
		loginBureau : currentUserBureau,
		crossName: trainNbr,
		crossNamecheckflag:checkFlag,
		exceptionflag:trainType
	};
	crossGloble.$table.datagrid("load",param);
}


/**
 * 初始化列表
 */
function initTable(){
	var columns =[[
	               {field:'cmOriginalCrossId',hidden:true},
	               {field:'cmOriginalTrainId',hidden:true},
	               {field:'exceptionflag',hidden:true},
	               {field:'squeueNo',title:'序号',width:20,align:"center"},
	               {field:"ck",checkbox:true,width:30,align:"center"},
	               {field:'crossName',title:'交路',width:200,align:"left",
	            	   formatter:function(value,row,index){
            			   if(row.exceptionflag&&row.exceptionflag!==0){
            				   return '<a href="#" style="color:red;" onclick="crossManage(\''+row.cmOriginalCrossId+'\')">'+value+'</a>';
            			   }else{
            				   return '<a href="#" onclick="crossManage(\''+row.cmOriginalCrossId+'\')">'+value+'</a>';
            			   }
	            		   
	            	   }},
	               {field:'trainName',title:'车次',width:70,align:"center",
            		   formatter:function(value,row,index){
        				   if(row.exceptionflag&&row.exceptionflag!==0){
        					   return '<a href="#" style="color:red;" onclick="trainManage(\''+row.cmOriginalTrainId+'\',\''+row.cmOriginalCrossId+'\')">'+value+'</a>';
        				   }else{
        					   return '<a href="#" onclick="trainManage(\''+row.cmOriginalTrainId+'\',\''+row.cmOriginalCrossId+'\')">'+value+'</a>';
        				   }
            		   }},
	               {field:'crossSection',title:'运行区段',width:80,align:"left"},
	               {field:'sourceNode',title:'发站',width:50,align:"left"},
	               {field:'sourceTime',title:'始发时间',width:40,align:"center",
	            	   formatter:function(value,row,index){
	            		   //value=2016-01-10 18:55:00 ||2016-01-10 8:55:00
	            		   if(!value)
	            			   return ;
	            		   var html=value.split(' ')[1].substring(0,value.split(' ')[1].length-3);
	            		   
	            		   if(html.indexOf("NaN")>0)
	            			   html="";
	            		   return html;
	            	   }
	               },
	               {field:'targetNode',title:'到站',width:50,align:"left"},
	               {field:'targetTime',title:'终到时间',width:45,align:"center",
	            	   formatter:function(value,row,index){
	            		   if((!value)||(!row.sourceTime))
	            			   return;
	            		   var html="";
	            		   //计算日期间隔
	            		   var from=row.sourceTime.substring(0,10).split("-");
	            		   var to=value.substring(0,10).split("-");
	            		   from=new Date(from[0],from[1],from[2]);
	            		   to=new Date(to[0],to[1],to[2]);
	            		   var day=parseInt(Math.abs(from-to))/1000/3600/24;
	            		   if(day==0)
	            			   html=value.substring(11,value.length-3);
	            		   else
	            			   html="+"+day+" "+value.substring(11,value.length-3);//不要秒
	            		   //判断值是否有效
	            		   if(html.indexOf("NaN")>0)
	            			   html="";
	            		   return html;
	            	   }
	               },
	               {field:'crossLevel',title:'等级',width:50,align:"center"},
	               {field:'pairNbr',title:'对数',width:35,align:"center"},
	               {field:'tokenVehBureau',title:'担当',width:35,align:"center",formatter:function(value,row,index){
	            	   return getBureau(value);
	               }},
	           	   {field:'throughLine',title:'经由',width:150,align:"left"},
	               {field:'note',title:'备注',width:80,align:"center"},
	               /*{field:'useStatus',title:'操作',width:60,align:"center",
	                   formatter:function(value,row,index) {
	                	   var html ="";
	                	   if(currentUserBureau===row.tokenVehBureau){
	                		   html = '<div style="text-align:center;">'+
                    			  '<a href="#" style="margin-left:10px;" onclick="trainManage(\''+row.cmOriginalTrainId+'\',\''+row.cmOriginalCrossId+'\')">修改</a>'+
                    			  '<a href="#" style="margin-left:10px;" onclick="deleteTrain(\''+row.cmOriginalTrainId+'\',\''+row.cmOriginalCrossId+'\')">删除</a></div>';
	                	   } 
	                       return html;
	                   }
	               },*/
	               {field:'commonlineRule',title:'开行规律',width:80,align:"center",formatter:function(value,row,index){
	            	   if(value!==null){
	            		   if(value==1){
	            			   return "每日";
	            		   }
	            		   else{
	            			  return "隔日";
	            		   }
	            			   
	            	   }else{
	            		   return row.appointDay!==null?"指定日期："+row.appointDay:row.appointWeek!==null?"指定星期："+row.appointWeek:row.appointPeriod!==null?"指定周期："+row.appointPeriod:row.highlineRule!==null?"高线规律："+row.highlineRule:"";
	            	   }
	               }},
	               {field:'spareFlag',title:'开行状态',width:40,align:"center",
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
	            	   }}
	               /*{field:'alternateTrainNbr',title:'交替车次',width:60,align:"center"},
	               {field:'alternateDate',title:'交替日期',width:60,align:"center"},*/
	               ]];
			//查询条件
			//方案
		   	var cmVersionId = $("#cmVersionId").val();
		    //担当局
			//var headerBureau = $("#headerBureau").val();
		   	//车次
			//var trainNo = $("#trainNo").val();
			
		   	var queryParams = {
		   			cmVersionId : cmVersionId,
		   			tokenVehBureau : -1,
		   			crossNamecheckflag:1,
		   			tokenFlag : 1,
		   			loginBureau : currentUserBureau,
//		   			highlineFlag:-1 //全部
		   			exceptionflag:1
		   	};
	       	var url = basePath + "/cmOriginalCross/pageQueryCrossAndTrain";
	       	var targetTable = $("#crossTable");
	       	crossGloble.$table = targetTable.datagrid({
	            height: 'auto',
	            width:'100%',
	            nowrap: true,
	            striped: true,
	            loadMsg : '请稍候......',
	           // border: true,
	            url:url,
	            checkOnSelect:false, //点击不选中行
	            //selectOnCheck:false, //点击不选中行
	            collapsible:false,//是否可折叠的
	            striped: true,//显示斑马线效果(默认false)
	            fit: true,//自动大小
	            remoteSort:false,
	            singleSelect:false,//是否单选
	            pagination:true,//分页控件
	            fitColumns:true,
	            pageNumber:1,
	            pageSize:100,
	            pageList : [50,100],
	            columns:columns,
	            queryParams : queryParams,
	            loadFilter : function(data){
	            	if(data.rows){ //标准格式的data，直接返回data
	            		return data;
	            	}
	            	var value = {};
	            	if(data.pageInfo===null || data.pageInfo.list===null){//没有数据
	            		value.rows = [];
	                	value.total= 0;
	            	}else{
	            		var array = [];
	            		for(var i=0; i<data.pageInfo.list.length; i++){
	            			var temp = data.pageInfo.list[i];
	            			var trainList = temp.trainList;
	            			for(var j=0;j<trainList.length;j++){
	            				trainList[j].crossName = temp.cross.crossName;
	            				trainList[j].squeueNo = i+1;
	            			}
	            			array = array.concat(trainList);
	            		}
	            		value.rows = array;
	                	value.total= data.pageInfo.totalCount;
	            	}
	            	return value;
	            },
	            rowStyler: function(index,row){
	            	//预留异常的接口
	            	/*console.log(row)
	            	console.log(row.exceptionflag+" "+typeof(row.exceptionflag))*/
	            	if(row.exceptionflag===-1)
	            	{
	            		return "height:30px;color:red !important;";
	            	}
	            	else
	            		return "height:30px";
	            },
	            onClickRow:function(rowIndex, rowData){
	            	crossGloble.$table.datagrid("clearSelections");
	            	crossGloble.$table.datagrid("clearChecked");
	            	var cmOriginalCrossId = rowData.cmOriginalCrossId;
	            	var rows = crossGloble.$table.datagrid("getRows");
	            	var firstMatch = true;
	            	for(var i=0;i<rows.length;i++){
	            		if(rows[i].cmOriginalCrossId===cmOriginalCrossId){
	            			crossGloble.$table.datagrid("selectRow",i);
	            			if(firstMatch){
	            				crossGloble.$table.datagrid("checkRow",i);
	            				firstMatch = false;
	            			}
	            		}
	            	}
	            },
	            onCheck:function(rowIndex, rowData){
	            	var cmOriginalCrossId = rowData.cmOriginalCrossId;
	            	var rows = crossGloble.$table.datagrid("getRows");
	            	for(var i=0;i<rows.length;i++){
	            		if(rows[i].cmOriginalCrossId===cmOriginalCrossId){
	            			crossGloble.$table.datagrid("selectRow",i);
	            		}
	            	}
	            },
	            onUncheck:function(rowIndex, rowData){
	            	var cmOriginalCrossId = rowData.cmOriginalCrossId;
	            	var rows = crossGloble.$table.datagrid("getRows");
	            	for(var i=0;i<rows.length;i++){
	            		if(rows[i].cmOriginalCrossId===cmOriginalCrossId){
	            			crossGloble.$table.datagrid("unselectRow",i);
	            		}
	            	}
	            },
	            onLoadSuccess:function(data){
	            	var list = data.rows;
	            	if(list.length<1){
	            		return;
	            	}
	            	var currentCrossId= list[0].cmOriginalCrossId;
	            	var count = 0;
	            	var index = 0;
	            	var merges = [];
	            	var merge=new Object();
	            	merge.index=0;
	            	for(var i=0;i<list.length;i++){
	            		if(currentCrossId==list[i].cmOriginalCrossId){
	            			merge.rowspan=++count;
	            			if(i===list.length-1){
	            				merges.push(merge);
	            			}
	            		}else{
	            			merges.push(merge);
	            			merge = new Object();
	            			merge.index=i;
	            			currentCrossId = list[i].cmOriginalCrossId;
	            			count=1;
	            		}
	            		
	            	}
	        			for(var i=0; i<merges.length; i++){
	        				crossGloble.$table.datagrid('mergeCells',{
	        					index:merges[i].index,
	        					field:'crossName',
	        					rowspan:merges[i].rowspan
	        				});
	        				crossGloble.$table.datagrid('mergeCells',{
	        					index:merges[i].index,
	        					field:'ck',
	        					rowspan:merges[i].rowspan
	        				});
	        				crossGloble.$table.datagrid('mergeCells',{
	        					index:merges[i].index,
	        					field:'squeueNo',
	        					rowspan:merges[i].rowspan
	        				});
	        			}
	        		}
	        	
	            
	        });
	        //设置分页控件
	        var $p = targetTable.datagrid('getPager');
	        $p.pagination({
	            showPageList: false,
	            showRefresh: false,
	            beforePageText: '第',//页数文本框前显示的汉字
	            afterPageText: '页    共 {pages} 页',
	            displayMsg: '当前显示 {from} - {to}条记录       共 {total} 条记录'
	        });
	        
}


/**
 * 新建交路和管理交路
 * @param crossId 值为不为空表示修改，为空表示新增 
 */
function crossManage(crossId){ 
	var $cmVersionIdSlect = $("#cmVersionId").val();
	$("#cmVersionId2").val($cmVersionIdSlect);
    if(crossId===undefined){ //新增
    	$("#shadowBlock").show();
        $("#crossBlock").show("300");
        $("#numberDetail").empty();
        $("#crossName").val("");
    	return;
    }
    if(crossId===1){ //管理
    	var row = crossGloble.$table.datagrid("getChecked");
    	if(row.length===0 || row.length>1){
        	//alert("请只选中一条交路进行操作。");
    		alertMessage("warning","请只选中一条交路进行操作。");
        	return;
        }else if(row && row.length===1){
        	if(currentUserBureau!==row[0].tokenVehBureau){
        		return; //不可编辑非本局的交路
        	}
        	crossId = row[0].cmOriginalCrossId;
        }
    }
    $("#shadowBlock").show();
    $("#crossBlock").show("300");
    $("#numberDetail").empty();
    $("#crossName").val("");
    initTrainNumber(crossId);
    
}
/**
 * 新建和修改车次
 * @param trainId 值不为空表示修改，为空表示新增 
 */
function trainManage(trainId,crossId){
	showDetail(crossId,trainId);
	$("#shadowBlock").show();
    $("#trainBlock").show("300");
	if(arguments.length>1)
		$("#trainId").val(trainId);
	else{
		$("#trainId").val("");
	}
	
}

/**
 * 滑出隐藏的右侧维护div
 * @param id 交路还是车次div的ID
 */

function showSlideBlock(id){
    $("#shadowBlock").show();
    var slideblock=$("#crossBlock").show("300");
    slideblock.show("300");
}
/**
 * 隐藏右侧维护div
 */
function closeSlideBlock(){ //点击右侧的按钮收起div
    $(".slideblock").hide("300");
    $("#shadowBlock").hide();
    $("#numberDetail").empty();
    $("#crossName").val("");
    crossGloble.deleteList = [];
}
/**
 * 导入对数表
 */
function openimportExlDlg(){
	var $cmVersionIdSlect = $("#cmVersionId").val();
	$("#cmVersionId_dialog").val($cmVersionIdSlect);
    $('#importExl').show().dialog({
        title: '导入对数表',
        width: 400,
        height: 200,
        cache: false,
        modal: true
    });
    $('#importExl').dialog('open');
}
/**
 * 上传对数表exl
 */
function uploadFile(coverFlag){
	if($("#fileToUpload").val() == null || $("#fileToUpload").val() == ""){
		//alert("请选择文件！");
		alertMessage("warning","请选择文件！");
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
        	versionId : $("#cmVersionId_dialog").val(),
        	coverFlag:coverFlag
        },
        success: function (data, status)
        {  
        	$("#loading").hide();
    		$("#upload_btn").removeAttr("disabled");
    		if(data.code===0){
    			alertMessage("success",data.message,true);
        		$('#importExl').dialog('close');
        		crossGloble.$table.datagrid('load');
    		}else if(data.code===-1){
    			alertMessage("warning",data.message);
    		}else if(data.code===1){
    			if(confirm("目标中包含相同的交路，是否覆盖？")){
    				uploadFile('true');
    			}
    		}
    		
        },
        error: function(result){
        	//alert("导入失败");
        	alertMessage("danger","导入失败");
        	$("#loading").hide();
        	$("#upload_btn").removeAttr("disabled");
        }
    }); 
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
 * 打开新增车次窗口
 *//*
function openAddTrainDlg(){
	$("#result").empty();
	$('#addTrainDlg').show();.dialog({
        title: '新增车次',
        width: 600,
        height: 400,
        cache: false,
        modal: true
    });
    $('#addTrainDlg').dialog('open');
}*/

/**
 * 查询车次。
 */
function searchTrain(){
	var cmVersionId = $("#cmVersionId2").val();
	var checkFlag=$("#checkFlag1:checked");
	if(checkFlag.length)
		checkFlag=1;
	else
		checkFlag=0;
	$.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/trainLine/getAllTrainLine",
		data:JSON.stringify({trainName:$("#searchKey").val(),versionId:cmVersionId,checkFlag:checkFlag}),
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
		crossName +="-"+selectedTrain.trainName;
	}
	$("#crossName").val(crossName);
	//$('#addTrainDlg').dialog('close');
}

/**
 * 取消添加车次
 */
function canselSelect(){
	$('#addTrainDlg').dialog('close');
}
/**
 * 保存交路车次
 */
function saveCross(){
	var check = validateCross();
	if(check>0){
		//alert("选择的前车终到与后车始发不一致，不能保存。");
		alertMessage("warning","选择的前车终到与后车始发不一致，不能保存。");
		//$("#numberDetail li:eq("+(check-1)+")").addClass("redWarn");
		//$("#numberDetail li:eq("+check+")").addClass("redWarn");
		return;
	}else if(check===-2){//交路收尾站点不一致
		//alert("交路首尾站点不一致，不能保存。");
		alertMessage("warning","交路首尾站点不一致，不能保存。");
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
		deleteIds:crossGloble.deleteList
	}
	var url = "/cmOriginalCross/addCrossTrain";
	if(crossGloble.deleteList.length>0 || $("li[isexist]").length>0){ //修改，更新
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
				//alert("保存成功。");
				alertMessage("success","保存成功。");
				closeSlideBlock();
				crossGloble.$table.datagrid('reload');
			}else{
				//alert(data.message);
				alertMessage("warning",data.message);
			}
		}
	});
	crossGloble.deleteList = []; 
}


function crossesDelete(){
	if(!confirm("是否确认删除"))
		return false;
	var rows = crossGloble.$table.datagrid("getChecked");
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
			alertMessage("success",data.message);
			crossGloble.$table.datagrid('reload');
		}
	});
}
/**
 * 删除一个车次
 * @param trainId 车次id
 * @param crossId 交路id
 */
function deleteTrain(trainId,crossId){
	$.extend($.messager.defaults,{  
	    ok:"确定",  
	    cancel:"取消"  
	});  
	$.messager.confirm('删除车次','是否确定删除此车次？',function(yes){
		if(yes){
			$.ajax({
				type: "POST",
		        dataType: "json",
		        contentType : "application/json",
				url: basePath + "/cmOriginalCross/deleteTrain",
				data:JSON.stringify({
					crossId:crossId,
					trainId:trainId
				}),
				success:function(data){
					//alert(data.message);
					alertMessage("success",data.message);
					crossGloble.$table.datagrid('reload');
				}
			});
		}
	});
	
}

/**
 * resize table
 */
function resizeTable(){
	$(".left,.slideblock").height($(window).height()-90);
	$("#trainBlockBody").height($(window).height()-110);
//	crossGloble.$table.datagrid('resize',{"width":"100%"});
	
	$(window).resize(function () {
		crossGloble.$table.datagrid('resize',{width:"100%",height:"100%"});
	})
}

function changeOperationValue(){
	value=document.getElementById("operationType").value;
	switch(value){
		case "commonLineRule_everyday" : 
			document.getElementById("operationValue").disabled=true;
			document.getElementById("operationValue").value=1;
			break;
		case "commonLineRule_subday" : 
			document.getElementById("operationValue").disabled=true;
			document.getElementById("operationValue").value=2;
			break;
		case "appointPeriod" : 
			document.getElementById("operationValue").disabled=false;
			break;
		case "appointDay" : 
			document.getElementById("operationValue").disabled=false;
			break;
		case "appointWeek" : 
			document.getElementById("operationValue").disabled=false;
			break;}
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
//日期规律和周期规律只选其一。
function clear(target0,target1){
	$(target0).val("");
	if(target1){
		$(target1).val("");
	}
	
}

/**
*批量设置
*/
function crossesSet () {
	var cross=crossGloble.$table.datagrid('getSelections');
	var crossAarray = [];
	if(cross.length===0){
		return;
	}
	var crossItem =new Object();
	crossItem.trainInfos = new Array();
	var crossIdComp = cross[0].cmOriginalCrossId;
	crossItem.crossInfo  ={cmOriginalCrossId:crossIdComp};
	for (var i = 0; i < cross.length; i++) {
		if(cross[i].exceptionflag ===-1){
			alertMessage("warning","异常数据不能批量设置。");
			return;
		}
		if(cross[i].cmOriginalCrossId===crossIdComp){
			crossItem.trainInfos.push({cmOriginalTrainId:cross[i].cmOriginalTrainId});
			if(i===cross.length-1){
				crossItem.cross ={cmOriginalCrossId:crossIdComp};
				crossAarray.push(crossItem);
			}
		}else{
			crossAarray.push(crossItem);
			var crossItem =new Object();
			crossItem.trainInfos = new Array();
			crossIdComp = cross[i].cmOriginalCrossId;
			crossItem.crossInfo  ={cmOriginalCrossId:crossIdComp};
			crossItem.trainInfos.push({cmOriginalTrainId:cross[i].cmOriginalTrainId});
		}
	};

	 $.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/cmOriginalCross/batchConfigOperationRule",
		data:JSON.stringify({datas: crossAarray}),
		success:function(data){
			if(data.code===0){
				alertMessage("success","批量设置成功。");
				crossGloble.$table.datagrid('reload');
			}else{
				alertMessage("warning",data.message);
			}
			
		}
	});
}

function saveConfirm(){
	var cross=crossGloble.$table.datagrid('getSelections');
	if(cross.length===0){
		return;
	}
	var crossIdComp = cross[0].cmOriginalCrossId;
	var crossIds = crossIdComp;
	for (var i = 0; i < cross.length; i++) {
		if(cross[i].exceptionflag ===-1){
			alertMessage("warning","异常数据不能保存。");
			return;
		}
		if(crossIdComp!==cross[i].cmOriginalCrossId){
			crossIds += "," + cross[i].cmOriginalCrossId;
			crossIdComp = cross[i].cmOriginalCrossId;
		}
	}
	$.ajax({
		type: "POST",
        dataType: "json",
        contentType : "application/json",
		url: basePath + "/cmOriginalCross/batchSaveCross",
		data:JSON.stringify({crossIds: crossIds}),
		success:function(data){
			if(data.code===0){
				alertMessage("success","保存成功。");
				crossGloble.$table.datagrid('reload');
			}else{
				alertMessage("success",data.message);
			}
			
		}
	});
} 
