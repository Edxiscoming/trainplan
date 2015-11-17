<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.apache.shiro.SecurityUtils,org.railway.com.trainplan.service.ShiroRealm,java.util.List" %>
<% 
ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal(); 
List<String> permissionList = user.getPermissionList();
String userRolesString = "";
for(String p : permissionList){
	userRolesString += userRolesString.equals("") ? p : "," + p;
}
 
String basePath = request.getContextPath(); 
String  currentBureauShortName = user.getBureauShortName();
String currentUserBureau = user.getBureau();
%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
  <head>
    <title>本局对数表</title>
	
    <meta name="keywords" content="keyword1,keyword2,keyword3">
    <meta name="description" content="this is my page">
    <meta name="content-type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/logTable/logTable.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/minified/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/logTable/mainCross.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>/assets/css/style.css">
	<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/creatGrid.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/common.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/ajaxfileupload.js"></script>	
	<script type="text/javascript" src="<%=basePath %>/assets/js/jquery.gritter.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/assets/js/logTable/alternate_config.js"></script>

  	<script type="text/javascript">
        var basePath = "<%=basePath %>";
        var currentUserBureau="<%=currentUserBureau%>";/* 当前局 */
    </script> 
    <SCRIPT language=javascript>
/////////////////////////////////////////////////////////////////////////
// Generic Resize                                     //
//                                                                     //
// You may use this script as long as this disclaimer is remained.     //
               //
//                                                                     //
// How to use this script!                                             //
// Link the script in the HEAD and create a container (DIV, preferable //
// absolute positioned) and add the class="resizeMe" to it.            //
/////////////////////////////////////////////////////////////////////////

var leftObject = null; //This gets a value as soon as a resize start

function resizeObject() {
 this.el        = null; //pointer to the object
 this.dir    = "";      //type of current resize (n, s, e, w, ne, nw, se, sw)
 this.grabx = null;     //Some useful values
 this.graby = null;
 this.width = null;
 this.height = null;
 this.left = null;
 this.top = null;
}
 

//Find out what kind of resize! Return a string inlcluding the directions
function getDirection(el) {
 var xPos, yPos, offset, dir;
 dir = "";

 xPos = window.event.offsetX;
 yPos = window.event.offsetY;

 offset = 8; //The distance from the edge in pixels

 if (yPos<offset) dir += "n";
 else if (yPos > el.offsetHeight-offset) dir += "s";
 if (xPos<offset) dir += "w";
 else if (xPos > el.offsetWidth-offset) dir += "e";

 return dir;
}

function doDown() {
 var el = getReal(event.srcElement, "className", "resizeMe");
 var er = getReal(event.srcElement, "className", "resizeRight");

 if (el == null) {
  theobject = null;
  return;
 }  

 dir = getDirection(el);
 if (dir == "") return;

 leftObject = new resizeObject();
  
 leftObject.el = el;
 leftObject.dir = dir;

 leftObject.grabx = window.event.clientX;
 leftObject.graby = window.event.clientY;
 leftObject.width = el.offsetWidth;
 leftObject.height = el.offsetHeight;
 leftObject.left = el.offsetLeft;
 leftObject.top = el.offsetTop;

 window.event.returnValue = false;
 window.event.cancelBubble = true;
}

function doUp() {
 if (leftObject != null) {
  leftObject = null;
 }
}

function doMove() {
 var el, er, xPos, yPos, str, xMin, yMin;
 xMin = 200; //The smallest width possible
 yMin = 200; //             height

 el = getReal(event.srcElement, "className", "resizeMe");
 er = getReal(event.srcElement, "className", "resizeRight");

 if (el.className == "resizeMe") {
  str = getDirection(el);
 //Fix the cursor 
  if (str == "") str = "default";
  else str += "-resize";
  el.style.cursor = str;
 }
 
//Dragging starts here
 if(leftObject != null) {
 
  if (dir.indexOf("s") != -1)
   leftObject.el.style.height = Math.max(yMin, leftObject.height + window.event.clientY - leftObject.graby) + "px";
   crossGloble.$table2.datagrid("resize",{height:"94%"});
  

  if (dir.indexOf("n") != -1) {
   leftObject.el.style.top = Math.min(leftObject.top + window.event.clientY - leftObject.graby, leftObject.top + leftObject.height - yMin) + "px";
   leftObject.el.style.height = Math.max(yMin, leftObject.height - window.event.clientY + leftObject.graby) + "px";
   crossGloble.$table2.datagrid("resize",{height:"94%"});
   
  }
  
  window.event.returnValue = false;
  window.event.cancelBubble = true;
 } 
}


function getReal(el, type, value) {
 temp = el;
 while ((temp != null) && (temp.tagName != "BODY")) {
  if (eval("temp." + type) == value) {
   el = temp;
   return el;
  }
  temp = temp.parentElement;
 }
 return el;
}

document.onmousedown = doDown;
document.onmouseup   = doUp;
document.onmousemove = doMove;
</SCRIPT>
  </head>
  
  <body class="container-fluid">
    <div class="div-header row">
    <div class="col-xs-12">
  		
		<label class="pull-left padding-5" style="margin-left: 10px;">路局</label>
		<!-- <select class="form-control pull-left input-sm" id="headerBureau" style="width: 60px;padding: 1px 5px;">
             <option value=""></option>
        </select> -->
        <span id="headerBureau" class="pull-left"></span>
        
        
        <a type="button" class="btn btn-default blueBtn btn-sm pull-right"  onclick="addBatchRow()"><span class="glyphicon glyphicon-log-in"></span>保存</a>
  	</div>
    </div>
  	
     <div class="row" style="margin-top: 15px;">
      
        <div class="resizeMe" style="height:300px;  border-bottom: 2px solid rgb(149, 184, 231);margin:0 15px;">
        	<div class="col-xs-12 left" style="height:100%;float:left;padding:0px">
            	<table id="crossTable2">
            	</table>
        	</div>
        </div>
        <div style="margin:10px;">
        <span class="pull-left padding-5" style="margin-left:15px">方案：</span>
		<select class="form-control input-sm pull-left"
			style="width: 280px; color:red;" id="cmVersionId">
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">20150520__0519导入</option>
		</select>
        
        <span class="pull-left padding-5" style="margin-left: 10px;">车次：</span>
         	<input type="text" id="trainNbr" class="input-sm form-control pull-left" style="width: 150px;" onkeyup="upperCase(this.id)">
			<!-- <span class="pull-left padding-5" style="margin-left: 10px;">等级：</span>
			<select class="form-control input-sm pull-left" id="levelSelect" style="width: 100px;" >
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">全部0</option>
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">全部1</option>
			<option value="a47c4b91-5a8f-48e6-abb0-bd9cb4c0ecf3">全部2</option>
			</select> -->
			<div class="pull-left" style="margin-left:10px;margin-top:5px">
				<input type="checkbox" id="checkFlag" checked>模糊
			</div>
			<!-- <div class="btn-group pull-left" style="margin-left:10px;">
	    		<button type="button" class="btn btn-default active" value="1" onclick="searchCross(this);" id="allType">全部</button>
	        	<button type="button" class="btn btn-default"  value="0" onclick="searchCross(this);">正常</button>
	        	<button type="button" class="btn btn-default" value="-1" onclick="searchCross(this);">异常</button>
			</div> -->
			
			<a type="button" class="btn btn-default blueBtn btn-sm pull-left"
					style="margin-left: 20px;" onclick="searchCross();"><span class="glyphicon glyphicon-search"style="margin-right:5px;"></span>查询</a>
         
         	<a type="button" class="btn btn-default greenBtn btn-sm pull-right" style="margin-left: 10px;" onclick="crossesSet();"><span class="glyphicon glyphicon-cog"style="margin-right:5px;"></span>批量设置</a>
			<a type="button" class="btn btn-default blueBtn btn-sm pull-right" style="margin-left: 10px;" onclick="addRow()"><span class="glyphicon glyphicon-log-in"style="margin-right:5px;"></span>添加</a>
		</div>
        <div class="col-xs-12 left" style="height:400px;float:left;padding-left:15px;margin-top:5px">
            <table id="crossTable">
            </table>
        </div>
    <div>
       
        
     <div id="setDateTime" title="批量设置交替时间" style="display: none;">
    	<img id="loading" src="<%=basePath %>/assets/images/loading.gif" style="display:none;"> 
        <form class="form-horizontal container-fluid" style="margin-left: 30px; margin-top:15px;">
            <div class="form-group">
            	<span>交替时间：</span>
            	<input type="date" id="ToSetTime"><br>
                <a type="button" id="upload_btn" onclick="confirmBatchConfig()" class="btn btn-default btn-default blueBtn">确定</a>
            </div>
        </form>

    </div>   

<script>
var trainNbr=document.getElementById("trainNbr");
trainNbr.addEventListener("keyup",function(event){
           event=window.event;
        if(event.keyCode==13)
        {
        	searchCross();
           }
       })
</script>
</body>
</html>
