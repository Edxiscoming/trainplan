var pageSize=20;//每页显示的记录条数
var pageNo=1;//当前页页码
var totalPage=1;//总页数
var pagingUtilRecordStartIndex = 1;//列表开始序号

//初始化分页div
function initPagination(id, _paramTotalRecord){
	

	initPageFooterUlDesc(id, _paramTotalRecord);
	
	
	var obj = $("#"+id);
	obj.empty();
	
	if(totalPage > 0){
		showPagination(obj);
		obj.show();
	}else{
		obj.hide();
	}
}



function hiddenPaginationDiv(id){
	$("#"+id).hide();
	$("#"+id+"_desc").hide();
}


//渲染分页描述信息
function initPageFooterUlDesc(_paramId, _paramTotalRecord) {
	
	var paginationDescObj = $("#"+_paramId+"_desc");
	paginationDescObj.text("");
	paginationDescObj.show();
	if (_paramTotalRecord == 0) {
		paginationDescObj.text("当前没有可显示的数据");
	} else {
		pagingUtilRecordStartIndex = 1;
		var endRecord = 1;
		if (pageNo == 1) {
			if (pageNo == totalPage) {
				endRecord = _paramTotalRecord;
			} else {
				endRecord = pageNo*pageSize;
			}
		} else{
			pagingUtilRecordStartIndex = (pageNo-1)*pageSize + 1;
			if(pageNo == totalPage) {
				endRecord = _paramTotalRecord;
			} else {
				endRecord = pageNo*pageSize;
			}
		}
		paginationDescObj.text("第"+pagingUtilRecordStartIndex+"-"+endRecord+"条记录/共"+_paramTotalRecord+"条记录     共"+totalPage+"页");
	}
	
}


function showPagination(obj){
	if(pageNo==1){
		obj.append('<li class="disabled"><a>«</a></li>');
	}else{
		obj.append('<li><a style="cursor:pointer" onclick="switchToPage(1)">«</a></li>');
	}
	
	if(totalPage<=7){
		for(var i=1;i<=totalPage;i++){
			if(pageNo == i){
				obj.append('<li class="active"><a>'+i+'<span class="sr-only"></span></a></li>');
			}else{
				obj.append('<li><a style="cursor:pointer" onclick="switchToPage('+i+')">'+i+'</a></li>');
			}
		}
	}else{
		var startNo = 1;
		var endNo = totalPage;
		if(pageNo-3>1){
			obj.append('<li><a>......</a></li>');
			startNo = pageNo -3 ;
		}
		
		if(totalPage-pageNo>3){
			endNo = pageNo + 3;
		}
		
		for(var i=startNo;i<=endNo;i++){
			if(pageNo == i){
				obj.append('<li class="active"><a>'+i+'<span class="sr-only"></span></a></li>');
			}else{
				obj.append('<li><a style="cursor:pointer" onclick="switchToPage('+i+')">'+i+'</a></li>');
			}
		}
		
		if(totalPage-pageNo>3){
			obj.append('<li><a>......</a></li>');
		}
	}
	
	if(pageNo==totalPage){
		obj.append('<li class="disabled"><a>»</a></li>');
	}else{
		obj.append('<li><a style="cursor:pointer" onclick="switchToPage('+totalPage+')">»</a></li>');
	}
}
