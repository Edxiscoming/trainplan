/**
 * Created by Administrator on 2015/9/24.
 */
/**
 * 创建一个datagrid表
 * @param targetTable 目标table jquery对象
 * @param url 服务器URL
 * @param columns 表列定义
 * @param param 查询条件
 * @param onClickRow 点击行事件
 * @returns {*}
 */
var creatGrid = function(targetTable,columns,url,queryParams,onClickRow){
    var tableGrid = targetTable.datagrid({
        height: 'auto',
        width:'100%',
        nowrap: true,
        striped: true,
        loadMsg : '请稍候......',
       // border: true,
        url:url,
        //checkOnSelect:false, //点击不选中行
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
        		value.rows = data.pageInfo.list;
            	value.total= data.pageInfo.totalCount;
        	}
        	return value;
        },
        rowStyler: function(index,row){
                    return "height:30px";
                },
        onClickRow:function(rowIndex, rowData){
        	//为了在管理本局担当和外局担当页面的多选
        	/*tableGrid.datagrid("clearSelections");
        	tableGrid.datagrid("clearSelections");*/
        	tableGrid.datagrid("selectRow",rowIndex);
        	if(onClickRow!==undefined){
        		onClickRow(rowData.cmPartOriginalCrossId ? rowData.cmPartOriginalCrossId : rowData);
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
    return tableGrid;
}


var creatTimeGrid = function(targetTable,columns,url,queryParams){
    var tableGrid = targetTable.datagrid({
        height: "auto",
        width:'100%',
        nowrap: true,
        striped: true,
        loadMsg : '请稍候......',
       // border: true,
        url:url,
        checkOnSelect:false, //点击不选中行
        collapsible:false,//是否可折叠的
        striped: true,//显示斑马线效果(默认false)
        fit: true,//自动大小
        remoteSort:false,
        singleSelect:false,//是否单选
        pagination:true,//分页控件
        fitColumns:true,
        pageNumber:1,
        pageSize:10,
        pageList : [5,10,20],
        columns:columns,
        queryParams : queryParams,
        loadFilter : function(data){
        	if(data.rows){ //标准格式的data，直接返回data
        		return data;
        	}
        	var value = {};
        	if(data.size==0){//没有数据
        		value.rows = [];
            	value.total= 0;
        	}else{
        		//value.rows = data.data;
        		
        		
            	var simpletable = new Array();
            	var simpletable_detail = new Array();
            	for(var i=0;i<data.data.length;i++){
            		if(data.data[i].stationFlag != 'BTZ'){
            			simpletable[i] = data.data[i];
            		}
            	}
            	value.rows = simpletable;
            	value.total = simpletable.length;
        	}
        	return value;
        },
        rowStyler: function(index,row){
                    return "height:30px";
        }
    });
    //设置分页控件
    var $p = targetTable.datagrid('getPager');
    $p.pagination({
        showPageList: true,
        showRefresh: false,
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to}条记录       共 {total} 条记录'
    });
    return tableGrid;
}

var creatTimeGrid_detail = function(targetTable,columns,url,queryParams){
    var tableGrid = targetTable.datagrid({
        height: "auto",
        width:'100%',
        nowrap: true,
        striped: true,
        loadMsg : '请稍候......',
       // border: true,
        url:url,
        checkOnSelect:false, //点击不选中行
        collapsible:false,//是否可折叠的
        striped: true,//显示斑马线效果(默认false)
        fit: true,//自动大小
        remoteSort:false,
        singleSelect:false,//是否单选
       // pagination:true,//分页控件
        fitColumns:true,
        pageNumber:1,
        pageSize:500,
        //pageList : [5,10,20],
        columns:columns,
        queryParams : queryParams,
        loadFilter : function(data){
        	if(data.rows){ //标准格式的data，直接返回data
        		return data;
        	}
        	var value = {};
        	if(data.size==0){//没有数据
        		value.rows = [];
            	value.total= 0;
        	}else{
        		value.rows = data.data;
            	value.total = data.data.length;
        	}
        	return value;
        },
        rowStyler: function(index,row){
                    return "height:30px";
        }
    });
    //设置分页控件
//    var $p = targetTable.datagrid('getPager');
//    $p.pagination({
//        showPageList: false,
//        showRefresh: false,
//        beforePageText: '第',//页数文本框前显示的汉字
//        afterPageText: '页    共 {pages} 页',
//        displayMsg: '当前显示 {from} - {to}条记录       共 {total} 条记录'
//    });
    return tableGrid;
}


var creatUnitGrid = function(targetTable,columns,url,queryParams){
    var tableGrid = targetTable.datagrid({
        height: 'auto',
        width:'100%',
        nowrap: true,
        striped: true,
        loadMsg : '请稍候......',
       // border: true,
        url:url,
        checkOnSelect:false, //点击不选中行
        collapsible:false,//是否可折叠的
        striped: true,//显示斑马线效果(默认false)
        fit: true,//自动大小
        remoteSort:false,
        singleSelect:false,//是否单选
        pagination:true,//分页控件
        fitColumns:true,
        pageNumber:1,
        pageSize:10,
        pageList : [5,10,20],
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
        		value.rows = data.pageInfo.list;
            	value.total= data.pageInfo.totalCount;
        	}
        	return value;
        }
    });
    //设置分页控件
    var $p = targetTable.datagrid('getPager');
    $p.pagination({
        showPageList: true,
        showRefresh: false,
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to}条记录       共 {total} 条记录'
    });
    return tableGrid;
}

var creatCrossGrid = function(targetTable,columns,url,queryParams){
    var tableGrid = targetTable.datagrid({
        height: 'auto',
        width:'100%',
        nowrap: true,
        striped: true,
        loadMsg : '请稍候......',
       // border: true,
        url:url,
        checkOnSelect:false, //点击不选中行
        collapsible:false,//是否可折叠的
        striped: true,//显示斑马线效果(默认false)
        fit: true,//自动大小
        remoteSort:false,
        singleSelect:false,//是否单选
        pagination:true,//分页控件
        fitColumns:true,
        pageNumber:1,
        pageSize:10,
        pageList : [5,10,20],
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
        		value.rows = data.pageInfo.list;
            	value.total= data.pageInfo.totalCount;
        	}
        	return value;
        }
    });
    //设置分页控件
    var $p = targetTable.datagrid('getPager');
    $p.pagination({
        showPageList: true,
        showRefresh: false,
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to}条记录       共 {total} 条记录'
    });
    return tableGrid;
}