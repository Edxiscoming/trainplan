/**
 * Created by star on 5/12/14.
 */
$(function() {
    $("timetable").freezeHeader();
})

var isCheckBox = false;
function showTimes(){
	if(isCheckBox){
		
		$("#mdhm").show();
		$("#hm").hide();
		isCheckBox = false;
	}else{
		$("#mdhm").hide();
		$("#hm").show();
		isCheckBox = true;
	}
	
}
