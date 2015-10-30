/*! HTML5 Shiv vpre3.6 | @afarkas @jdalton @jon_neal @rem | MIT/GPL2 Licensed
  Uncompressed source: https://github.com/aFarkas/html5shiv  */
function passwordfix(){
	var name="";
	var deptName ="";
	 $.ajax({
        url: "user/password",
        method: "POST",
        async:false,
        dataType : "json",
    	contentType : "application/json", 
    	success : function(result) {    
    			name=result.data.username;
    	
    			deptName=result.data.deptName;
    	     
    			},
    			
    			complete : function(){ 
    				commonJsScreenUnLock(); 
    			}
     })
	
	 document.getElementById("username1").value=name
	 document.getElementById("deptname").value=deptName
	 
	

};


function submitpass(){
	
	
	var username1= $("#username1").val()  
	var oldpass =$("#oldpass").val()    
	var newpass = $("#newpass").val()      
	var newpassagain =$("#newpassagain").val()
	
	console.log(oldpass+':'+ newpass);

	if(oldpass==newpass){
		
		$("#tipChange").removeClass("display");
		$("#tipOldpassword").addClass("display");
		$("#tipNewpassagain").addClass("display");
		
	}else if(newpass!=newpassagain){
		$("#tipNewpassagain").removeClass("display");
		$("#tipChange").addClass("display");
		$("#tipOldpassword").addClass("display");
	}
	else{
		
		 $.ajax({
		        url: "user/passwordfix",
		        method: "POST",
		        async:false,
		        dataType : "json",
		    	contentType : "application/json", 
		    	data :JSON.stringify({  
		    		username1 : username1, 
		    		oldpass : oldpass,
		    		newpass : newpass
		    		
					
				}),
		    	success : function(result) {    
		    		
		    		if(result.code==1){
		    			//
		    			console.dir("与原密码不同");
		    			$("#tipOldpassword").removeClass("display");
		    			$("#tipChange").addClass("display");
		    			$("#tipNewpassagain").addClass("display");
		    		}
		    		if(result.code==2){
		    			console.dir("修改成功!");
		    			$("#tipOldpassword").addClass("display");
		    			$("#tipChange").addClass("display");
		    			$("#tipNewpassagain").addClass("display");
		    			$("#changed").removeClass("display");
		    			$("#passwordinner").addClass("display");
		    			

		    			setTimeout("$('#myModal').modal('hide')",900);
		    			setTimeout('$("#changed").addClass("display")',1100);
		    			setTimeout('$("#passwordinner").removeClass("display")',1100);
		    			setTimeout('$("#passwordinner input").val("")',1100);
		    			
		    		}
		    		console.dir(result.code)
		    		
		    		
		    	     
		    			},
		    			
		    			complete : function(){ 
		    				commonJsScreenUnLock(); 
		    			}
		     })
		
	}
	
	
}