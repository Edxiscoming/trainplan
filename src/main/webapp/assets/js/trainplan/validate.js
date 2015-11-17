/**
 * 
 */
var Validate = {
            isPhone: function (value) {//验证手机 电话
                var validateReg = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
                var patrn = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
                if(validateReg.test(value) || patrn.test(value)){
                    return true;
                }
                else{
                    return false;
                }
            },
            isNumber:function(value){//必须为数字
            	var reg = new RegExp("^[0-9]*$");
            	if(reg.test(value)){
            		return true;
            	}else{
            		return false;
            	}
            },
            isDateFormat:function(value){//日期格式20140910
           	 var reg = new RegExp(/^([1-2]\d{3})(0?[1-9]|10|11|12)([1-2]?[0-9]|0[1-9]|30|31)$/ig);
           	if(reg.test(value)){
           		return true;
           	}else{
           		return false;
           	}
           },
           isValidDateFormat:function(value){//日期格式2015-02-12
             	 var reg = new RegExp(/^((((19|20)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((19|20)\d{2})-(0?[469]|11)-(0?[1-9]|[12]\d|30))|(((19|20)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-(0?[1-9]|[12]\d)))$/);
             	if(reg.test(value)){
             		return true;
             	}else{
             		return false;
             	}
             },
            isOand1:function(value){//必须是7位1和0组成的串
	           	 var reg = new RegExp(/^[0-1]{7}$/);
	           	if(reg.test(value)){
	           		if(value.indexOf("0") < 0 || value.indexOf("1") < 0){
	           			return false;
	           		}
	           		return true;
	           	}else{
	           		return false;
	           	}
           },
           isEmail:function(value){//验证邮件
        	   var pattern = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;  
	           	if(pattern.test(value)){
	           		return true;
	           	}else{
	           		return false;
	           	}
         },
         checkNumSize:function(value,maxValue){
        	 // 校验长度
        	 if(value >= maxValue){
        		 return true;
        	 }else{
        		 return false;
        	 }
         },
         arrayTime:function(startDate,endDate){
        	// 得到2个时间段的每一天
     		var arrayTime = new Array();
     		for(var i=Date.parse(startDate);i<=Date.parse(endDate);i+=(3600000*24)){
     			arrayTime.push(new Date(i).format("yyyy-MM-dd "));
     		}
     		return arrayTime;
         },
         timeAddDays:function(startDate,days){
        	 // 根据传递的日期,增加天数
        	 var dateTemp = startDate.split("-");  
     	    var nDate = new Date(dateTemp[1] + '-' + dateTemp[2] + '-' + dateTemp[0]); //转换为MM-DD-YYYY格式    
     	    var millSeconds = Math.abs(nDate) + (days * 24 * 60 * 60 * 1000);  
     	    var rDate = new Date(millSeconds);  
     	    var year = rDate.getFullYear();  
     	    var month = rDate.getMonth() + 1;  
     	    if (month < 10) {
     	    	month = "0" + month
     	    }  
     	    var date = rDate.getDate();  
     	    if (date < 10) {
     	    	date = "0" + date
     	    }
     	    return year + "-" + month + "-" + date;
         },
         isNumber1:function(str){
        	 var regu = /^[-]{0,1}[0-9]{1,}$/;
     	    return regu.test(str);
         }
  };
